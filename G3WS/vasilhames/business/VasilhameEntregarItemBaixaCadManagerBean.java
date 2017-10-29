package com.g3sistemas.vasilhames.business;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.g3sistemas.abstracts.AbstractCadManagerBean;
import com.g3sistemas.dto.CampoErroDTO;
import com.g3sistemas.dto.RegistroDTO;
import com.g3sistemas.estoques.entity.QProduto;
import com.g3sistemas.geral.business.EmpresaCadManagerBean;
import com.g3sistemas.geral.business.FilialCadManagerBean;
import com.g3sistemas.geral.entity.QEmpresa;
import com.g3sistemas.geral.entity.QFilial;
import com.g3sistemas.sistema.business.UsuarioFilialCadManagerBean;
import com.g3sistemas.sistema.business.UsuarioManagerBean;
import com.g3sistemas.sistema.entity.QUsuario;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregarItem;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregarItemBaixa;
import com.g3sistemas.vasilhames.entity.VasilhameEntregar;
import com.g3sistemas.vasilhames.entity.VasilhameEntregarItemBaixa;
import com.g3sistemas.vasilhames.enums.TipoBaixaEntregar;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;

@Stateless
public class VasilhameEntregarItemBaixaCadManagerBean extends AbstractCadManagerBean<VasilhameEntregarItemBaixa> {

	@PersistenceContext(unitName = "g3-ds")
	private EntityManager em;

	@EJB
	private EmpresaCadManagerBean empresaManagerBean;

	@EJB
	private FilialCadManagerBean filialCadManagerBean;
	
	@EJB
	private VasilhameEntregarItemCadManagerBean vasilhameEntregarItemCadManagerBean;
	
	@EJB
	private VasilhameEntregarCadManagerBean vasilhameEntregarCadManagerBean;	
	
	@EJB
	private UsuarioManagerBean usuarioManagerBean;
	
	@EJB
	private UsuarioFilialCadManagerBean usuarioFilialCadManagerBean;
	
	
	private QVasilhameEntregarItemBaixa qVasilhameEntregarItemBaixa;
	private QEmpresa qEmpresa;
	private QFilial qFilial;
	private QVasilhameEntregarItem qVasilhameEntregarItem;
	private QProduto qProduto;
	private QUsuario qUsuario;

	private JPAQuery<VasilhameEntregarItemBaixa> query;

	private ConstructorExpression<VasilhameEntregarItemBaixa> projecaoBaixa() {
		return QVasilhameEntregarItemBaixa.create(qVasilhameEntregarItemBaixa.id, qVasilhameEntregarItemBaixa.idEmpresa,
				QEmpresa.create(qEmpresa.id, qEmpresa.nome), qVasilhameEntregarItemBaixa.idFilial,
				QFilial.create(qFilial.codigo, qFilial.descricao), qVasilhameEntregarItemBaixa.idCodigo,
				qVasilhameEntregarItemBaixa.idVasilhameEntregarItem,
				QVasilhameEntregarItem.create(qVasilhameEntregarItem.idProduto,
						QProduto.create(qProduto.id, qProduto.digito, qProduto.descricao),
						qVasilhameEntregarItem.quantidade),
				qVasilhameEntregarItemBaixa.tipoBaixa, qVasilhameEntregarItemBaixa.quantidade,
				qVasilhameEntregarItemBaixa.dataHoraGravacao, qVasilhameEntregarItemBaixa.idUsuario,
				QUsuario.create(qUsuario.nome));
	}
	
//	private ConstructorExpression<VasilhameEntregarItemBaixa> projecaoSumQuantidade() {
//		return QVasilhameEntregarItemBaixa.create(
//				(NumberExpression<Double>) qVasilhameEntregarItemBaixa.quantidade.coalesce(0.0).asNumber().sum());
//	}

	public VasilhameEntregarItemBaixa find(VasilhameEntregarItemBaixa registro) {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregarItemBaixa = QVasilhameEntregarItemBaixa.vasilhameEntregarItemBaixa;	
		this.qEmpresa = QEmpresa.empresa;
		this.qFilial = QFilial.filial;
		this.qVasilhameEntregarItem = QVasilhameEntregarItem.vasilhameEntregarItem;
		this.qProduto = QProduto.produto;
		this.qUsuario = QUsuario.usuario;

		return this.query.select(this.projecaoBaixa())
				.from(qVasilhameEntregarItemBaixa)
				.leftJoin(qVasilhameEntregarItemBaixa.empresa, qEmpresa)
				.leftJoin(qVasilhameEntregarItemBaixa.filial, qFilial)
				.leftJoin(qVasilhameEntregarItemBaixa.vasilhameEntregarItem, qVasilhameEntregarItem)
				.leftJoin(qVasilhameEntregarItem.produto, qProduto)
				.leftJoin(qVasilhameEntregarItemBaixa.usuario, qUsuario)
				.where(qVasilhameEntregarItemBaixa.idEmpresa.eq(registro.getIdEmpresa())
				.and(qVasilhameEntregarItemBaixa.idFilial.eq(registro.getIdFilial())
				.and(qVasilhameEntregarItemBaixa.idCodigo.eq(registro.getIdCodigo())))).fetchOne();
	}
	
	public Double findSumQuantidade(VasilhameEntregarItemBaixa registro) {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregarItemBaixa = QVasilhameEntregarItemBaixa.vasilhameEntregarItemBaixa;		
		
		return this.query.select((NumberExpression<Double>) qVasilhameEntregarItemBaixa.quantidade.coalesce(0.0).asNumber().sum())
				.from(qVasilhameEntregarItemBaixa)				
				.where(qVasilhameEntregarItemBaixa.idEmpresa.eq(registro.getIdEmpresa())
					.and(qVasilhameEntregarItemBaixa.idFilial.eq(registro.getIdFilial()))
					.and(qVasilhameEntregarItemBaixa.idVasilhameEntregarItem.eq(registro.getIdVasilhameEntregarItem())))
				.fetchOne();
	}
	
	public Boolean existeVasilhameEntregarItemBaixa(VasilhameEntregarItemBaixa registro) {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregarItemBaixa = QVasilhameEntregarItemBaixa.vasilhameEntregarItemBaixa;
		
		return null!=this.query.select(qVasilhameEntregarItemBaixa.idCodigo)
				.from(qVasilhameEntregarItemBaixa)
				.where(qVasilhameEntregarItemBaixa.idEmpresa.eq(registro.getIdEmpresa())
				.and(qVasilhameEntregarItemBaixa.idFilial.eq(registro.getIdFilial())
				.and(qVasilhameEntregarItemBaixa.idCodigo.eq(registro.getIdCodigo()))))
				.fetchOne();
	}
	
	public Boolean existeVasilhameEntregarItemBaixa(Integer idEmpresa, Integer idFilial, Integer idCodigo) {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregarItemBaixa = QVasilhameEntregarItemBaixa.vasilhameEntregarItemBaixa;
		
		return null!=this.query.select(qVasilhameEntregarItemBaixa.idCodigo)
				.from(qVasilhameEntregarItemBaixa)
				.where(qVasilhameEntregarItemBaixa.idEmpresa.eq(idEmpresa)
				.and(qVasilhameEntregarItemBaixa.idFilial.eq(idFilial)
				.and(qVasilhameEntregarItemBaixa.idCodigo.eq(idCodigo))))
				.fetchOne();
	}

	@Override
	public List<CampoErroDTO> salvar(RegistroDTO<VasilhameEntregarItemBaixa> dto) throws Exception {
		final List<CampoErroDTO> erros = this.validar(dto);
		if (erros.isEmpty()) {
			this.getEm().persist(dto.registro);

			Integer idVasilhameEntregar = this.vasilhameEntregarItemCadManagerBean.findVasilhameEntregar(
					dto.registro.getIdEmpresa(), dto.registro.getIdFilial(),
					dto.registro.getIdVasilhameEntregarItem());
			
			if (idVasilhameEntregar != null) {
				VasilhameEntregar vasilhameEntregar = this.vasilhameEntregarCadManagerBean
						.findVasilhameEntregarItem(dto.registro.getIdEmpresa(), dto.registro.getIdFilial(),
								idVasilhameEntregar);
				
				if (idVasilhameEntregar != null) {
					this.vasilhameEntregarCadManagerBean.mudaSituacaoVasilhame(vasilhameEntregar);				
				}
			}
		}
		return erros;
	}
	
	@Override
	public List<CampoErroDTO> atualizar(RegistroDTO<VasilhameEntregarItemBaixa> dto) throws Exception {
		final List<CampoErroDTO> erros = this.validar(dto);
		if (erros.isEmpty()) {					
			dto.registro = this.getEm().merge(dto.registro);
			
			Integer idVasilhameEntregar = this.vasilhameEntregarItemCadManagerBean.findVasilhameEntregar(
					dto.registro.getIdEmpresa(), dto.registro.getIdFilial(),
					dto.registro.getIdVasilhameEntregarItem());
			
			if (idVasilhameEntregar != null) {
				VasilhameEntregar vasilhameEntregar = this.vasilhameEntregarCadManagerBean
						.findVasilhameEntregarItem(dto.registro.getIdEmpresa(), dto.registro.getIdFilial(),
								idVasilhameEntregar);
				
				if (vasilhameEntregar != null) {
					this.vasilhameEntregarCadManagerBean.mudaSituacaoVasilhame(vasilhameEntregar);				
				}
			}		
		}
		return erros;
	}
	
	@Override
	public List<CampoErroDTO> validar(RegistroDTO<VasilhameEntregarItemBaixa> dto) {
		List<CampoErroDTO> erros = new ArrayList<CampoErroDTO>();

		this.validaEmpresa(dto, erros);
		this.validaFilial(dto, erros);
		this.validaUsuarioFilial(dto, erros);
		this.validaVasilhameEntregarItem(dto, erros);
		this.validaTipoBaixa(dto, erros);	
		this.validaQuantidade(dto, erros);		
		this.validaUsuario(dto, erros);

		return erros;
	}

	private void validaEmpresa(RegistroDTO<VasilhameEntregarItemBaixa> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getIdEmpresa() == null) {
			erros.add(new CampoErroDTO("idEmpresa", "Campo obrigatório"));
		} else if (!this.empresaManagerBean.existeEmpresa(dto.registro.getIdEmpresa())) {
			erros.add(new CampoErroDTO("idEmpresa", "Empresa não cadastrada"));			
		}
	}
	
	private void validaFilial(RegistroDTO<VasilhameEntregarItemBaixa> dto, List<CampoErroDTO> erros) {	
		if (dto.registro.getIdFilial() != null) {
			if (dto.registro.getIdEmpresa() != null) {
				if (!this.filialCadManagerBean.existeFilialEmpresa(dto.registro.getIdEmpresa(), dto.registro.getIdFilial())) {
					erros.add(new CampoErroDTO("idFilial", "Filial não cadastrada"));
				}
			}
		} else {
			erros.add(new CampoErroDTO("idFilial", "Campo obrigatório"));
		}
	}	
	
	private void validaUsuarioFilial(RegistroDTO<VasilhameEntregarItemBaixa> dto, List<CampoErroDTO> erros) {
		if ((dto.registro.getIdUsuario() != "" && dto.registro.getIdUsuario() != null) && dto.registro.getIdEmpresa() != null
				&& dto.registro.getIdFilial() != null) {
			if (!this.usuarioFilialCadManagerBean.validaFilialLiberada(dto.registro.getIdUsuario(),
					dto.registro.getIdEmpresa(), dto.registro.getIdFilial())) {
				erros.add(new CampoErroDTO("idFilial", "Filial não liberada para este usuário"));
			}
		}
	}	
	
	private void validaVasilhameEntregarItem(RegistroDTO<VasilhameEntregarItemBaixa> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getIdEmpresa() != null && dto.registro.getIdFilial() != null
				&& dto.registro.getIdVasilhameEntregarItem() != null) {

			if (!this.vasilhameEntregarItemCadManagerBean.existeVasilhameEntregarItem(dto.registro.getIdEmpresa(),
					dto.registro.getIdFilial(), dto.registro.getIdVasilhameEntregarItem())) {
				erros.add(new CampoErroDTO("idVasilhameEntregarItem", "Vasilhame a Entregar item não cadastrado"));
			} else {
				Integer situacao = this.vasilhameEntregarItemCadManagerBean.findSituacaoVasilhameEntregar(
						dto.registro.getIdEmpresa(), dto.registro.getIdFilial(), dto.registro.getIdVasilhameEntregarItem());
				
				if (situacao == 8) {
					erros.add(new CampoErroDTO("idVasilhameEntregarItem", "Situação do vasilhame a entregar está inutilizado"));
				}
			}			
		}
	}
	
	private void validaTipoBaixa(RegistroDTO<VasilhameEntregarItemBaixa> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getTipoBaixa() != null) {
			if (!TipoBaixaEntregar.existe(dto.registro.getTipoBaixa())) {
				erros.add(new CampoErroDTO("tipoBaixa", "Tipo Baixa inválida"));
			}	
		} else {
			erros.add(new CampoErroDTO("tipoBaixa", "Campo obrigatório"));
		}
	}
	
	private void validaQuantidade(RegistroDTO<VasilhameEntregarItemBaixa> dto, List<CampoErroDTO> erros) {
		this.query = new JPAQuery<>(this.em);
		this.qVasilhameEntregarItemBaixa = QVasilhameEntregarItemBaixa.vasilhameEntregarItemBaixa;
		this.qVasilhameEntregarItem = QVasilhameEntregarItem.vasilhameEntregarItem;

		if (dto.registro.getIdVasilhameEntregarItem() != null) {

			Double quantidadeItem = this.vasilhameEntregarItemCadManagerBean.findQuantidade(dto.registro.getIdEmpresa(),
					dto.registro.getIdFilial(), dto.registro.getIdVasilhameEntregarItem());
			if (dto.registro.getQuantidade() > quantidadeItem) {
				erros.add(new CampoErroDTO("quantidade", "Quantidade da baixa maior que a quantidade do Item"));
			} else {
				Double quantidadeBaixada = this.findSumQuantidade(dto.registro);
				if (quantidadeBaixada == null) quantidadeBaixada = 0.0;
				quantidadeBaixada += dto.registro.getQuantidade();

				if (dto.registro.getIdCodigo() != null) {
					this.query = new JPAQuery<>(this.em);
					Double last = this.query.select(qVasilhameEntregarItemBaixa.quantidade.coalesce(0.0))
							.from(qVasilhameEntregarItemBaixa)
							.where(qVasilhameEntregarItemBaixa.idEmpresa.eq(dto.registro.getIdEmpresa())
									.and(qVasilhameEntregarItemBaixa.idFilial.eq(dto.registro.getIdFilial()))
									.and(qVasilhameEntregarItemBaixa.idCodigo.eq(dto.registro.getIdCodigo())))
							.fetchOne();

					quantidadeBaixada -= last;
					if (quantidadeBaixada > quantidadeItem) {
						erros.add(new CampoErroDTO("quantidade", "Quantidade da baixa maior que a quantidade do Item"));
					}

				} else {
					if (quantidadeBaixada > quantidadeItem) {
						erros.add(new CampoErroDTO("quantidade", "Quantidade da baixa maior que a quantidade do Item"));
					}
				}
			}
		}
	}

	private void validaUsuario(RegistroDTO<VasilhameEntregarItemBaixa> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getIdUsuario() != null && dto.registro.getIdUsuario() != "") {			
			if (!this.usuarioManagerBean.existeUsuario(dto.registro.getIdUsuario())) {
				erros.add(new CampoErroDTO("idUsuario", "Usuário não cadastrado"));
			} 
		}
	}
	
	@Override
	public List<CampoErroDTO> validaDeletar(List<VasilhameEntregarItemBaixa> registros) {
		List<CampoErroDTO> erros = new ArrayList<CampoErroDTO>();
		
		for (VasilhameEntregarItemBaixa baixa : registros) {
			this.validaDeletarVasilhame(baixa, erros);
		}		

		return erros;
	}

	private void validaDeletarVasilhame(VasilhameEntregarItemBaixa baixa, List<CampoErroDTO> erros) {
		if (baixa != null) {
			if (baixa.getIdVasilhameEntregarItem() != null) {
				
				Integer situacao = this.vasilhameEntregarItemCadManagerBean.findSituacaoVasilhameEntregar(
						baixa.getIdEmpresa(), baixa.getIdFilial(), baixa.getIdVasilhameEntregarItem());
				
				if (situacao == 8) {
					erros.add(new CampoErroDTO("idVasilhameEntregarItem", "Situação do vasilhame a entregar está inutilizado"));
				}
			}						
		}
	}
	
	@Override
	public List<CampoErroDTO> deletar(List<VasilhameEntregarItemBaixa> vasilhameEntregarItemBaixas) throws Exception {
		List<CampoErroDTO> erros = validaDeletar(vasilhameEntregarItemBaixas);
		if (erros.isEmpty()) {	
			for (VasilhameEntregarItemBaixa vasilhameEntregarItemBaixa : vasilhameEntregarItemBaixas) {
				
				this.getEm().remove(this.getEm().find(VasilhameEntregarItemBaixa.class, vasilhameEntregarItemBaixa.getId()));
				
				Integer idVasilhameEntregar = this.vasilhameEntregarItemCadManagerBean.findVasilhameEntregar(
						vasilhameEntregarItemBaixa.getIdEmpresa(), vasilhameEntregarItemBaixa.getIdFilial(),
						vasilhameEntregarItemBaixa.getIdVasilhameEntregarItem());
				
				if (idVasilhameEntregar != null) {
					VasilhameEntregar vasilhameEntregar = this.vasilhameEntregarCadManagerBean
							.findVasilhameEntregarItem(vasilhameEntregarItemBaixa.getIdEmpresa(), vasilhameEntregarItemBaixa.getIdFilial(),
									idVasilhameEntregar);
					
					if (vasilhameEntregar != null) {
						this.vasilhameEntregarCadManagerBean.mudaSituacaoVasilhame(vasilhameEntregar);
					}
				}
				
				if (vasilhameEntregarItemBaixa.getIdEmpresa() != null && vasilhameEntregarItemBaixa.getIdFilial() != null && vasilhameEntregarItemBaixa.getIdVasilhameEntregarItem() != null) {
					vasilhameEntregarItemBaixa.setQuantidadeBaixada(this.findSumQuantidade(vasilhameEntregarItemBaixa));					
				}
			}							
		}
		return erros;
	}	
	
	@Override
	public EntityManager getEm() {
		return em;
	}

	@Override
	public Class<VasilhameEntregarItemBaixa> getTypeParameterClass() {
		return VasilhameEntregarItemBaixa.class;
	}

}
