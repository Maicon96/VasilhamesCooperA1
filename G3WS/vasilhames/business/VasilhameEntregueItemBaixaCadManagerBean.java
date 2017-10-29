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
import com.g3sistemas.vasilhames.entity.QVasilhameEntregue;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregueItem;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregueItemBaixa;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregueItemBaixaCupom;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregueItemBaixaNota;
import com.g3sistemas.vasilhames.entity.VasilhameEntregue;
import com.g3sistemas.vasilhames.entity.VasilhameEntregueItem;
import com.g3sistemas.vasilhames.entity.VasilhameEntregueItemBaixa;
import com.g3sistemas.vasilhames.enums.TipoBaixa;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;

@Stateless
public class VasilhameEntregueItemBaixaCadManagerBean extends AbstractCadManagerBean<VasilhameEntregueItemBaixa> {

	@PersistenceContext(unitName = "g3-ds")
	private EntityManager em;

	@EJB
	private EmpresaCadManagerBean empresaManagerBean;

	@EJB
	private FilialCadManagerBean filialCadManagerBean;
	
	@EJB
	private VasilhameEntregueCadManagerBean vasilhameEntregueCadManagerBean;
	
	@EJB
	private VasilhameEntregueItemCadManagerBean vasilhameEntregueItemCadManagerBean;	
	
	@EJB
	private UsuarioManagerBean usuarioManagerBean;
	
	@EJB
	private UsuarioFilialCadManagerBean usuarioFilialCadManagerBean;
		

	private QVasilhameEntregueItemBaixa qVasilhameEntregueItemBaixa;
	private QVasilhameEntregueItemBaixaCupom qVasilhameEntregueItemBaixaCupom;
	private QVasilhameEntregueItemBaixaNota qVasilhameEntregueItemBaixaNota;
	private QEmpresa qEmpresa;
	private QFilial qFilial;
	private QVasilhameEntregue qVasilhameEntregue;
	private QVasilhameEntregueItem qVasilhameEntregueItem;	
	private QProduto qProduto;	
	private QUsuario qUsuario;

	private JPAQuery<VasilhameEntregueItemBaixa> query;

	private ConstructorExpression<VasilhameEntregueItemBaixa> projecaoBaixa() {
		return QVasilhameEntregueItemBaixa.create(qVasilhameEntregueItemBaixa.id, qVasilhameEntregueItemBaixa.idEmpresa,
				QEmpresa.create(qEmpresa.id, qEmpresa.nome), qVasilhameEntregueItemBaixa.idFilial,
				QFilial.create(qFilial.codigo, qFilial.descricao), qVasilhameEntregueItemBaixa.idCodigo,
				qVasilhameEntregueItemBaixa.idVasilhameEntregueItem,
				QVasilhameEntregueItem.create(qVasilhameEntregueItem.idProduto,
						QProduto.create(qProduto.id, qProduto.digito, qProduto.descricao),
						qVasilhameEntregueItem.quantidade),
				qVasilhameEntregueItemBaixa.tipoBaixa, qVasilhameEntregueItemBaixa.quantidade,
				qVasilhameEntregueItemBaixa.dataHoraGravacao, qVasilhameEntregueItemBaixa.idUsuario, QUsuario.create(qUsuario.nome));
	}
	
	private ConstructorExpression<VasilhameEntregueItem> projecaoVasilhameEntregue() {
		return QVasilhameEntregueItem.create(qVasilhameEntregueItem.idEmpresa,
				qVasilhameEntregueItem.quantidade, qVasilhameEntregueItem.idVasilhameEntregue,
				QVasilhameEntregue.create(qVasilhameEntregue.id, qVasilhameEntregue.idEmpresa, qVasilhameEntregue.idFilial,
						qVasilhameEntregue.idCodigo, qVasilhameEntregue.idPessoa, qVasilhameEntregue.nome,
						qVasilhameEntregue.tipoContribuinte, qVasilhameEntregue.cpfcnpj, qVasilhameEntregue.observacao,
						qVasilhameEntregue.situacao, qVasilhameEntregue.dataHoraGravacao, qVasilhameEntregue.idUsuario,
						GroupBy.list(QVasilhameEntregueItem.create(qVasilhameEntregueItem.idEmpresa,
								qVasilhameEntregueItem.idFilial, qVasilhameEntregueItem.idCodigo,
								qVasilhameEntregueItem.idVasilhameEntregue, qVasilhameEntregueItem.quantidade))));
	}
	
	public VasilhameEntregueItemBaixa find(VasilhameEntregueItemBaixa registro) {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregueItemBaixa = QVasilhameEntregueItemBaixa.vasilhameEntregueItemBaixa;	
		this.qEmpresa = QEmpresa.empresa;
		this.qFilial = QFilial.filial;
		this.qVasilhameEntregueItem = QVasilhameEntregueItem.vasilhameEntregueItem;
		this.qProduto = QProduto.produto;
		this.qUsuario = QUsuario.usuario;
		
		return this.query.select(this.projecaoBaixa())
				.from(qVasilhameEntregueItemBaixa)
				.leftJoin(qVasilhameEntregueItemBaixa.empresa, qEmpresa)
				.leftJoin(qVasilhameEntregueItemBaixa.filial, qFilial)
				.leftJoin(qVasilhameEntregueItemBaixa.vasilhameEntregueItem, qVasilhameEntregueItem)
				.leftJoin(qVasilhameEntregueItem.produto, qProduto)
				.leftJoin(qVasilhameEntregueItemBaixa.usuario, qUsuario)
				.where(qVasilhameEntregueItemBaixa.idEmpresa.eq(registro.getIdEmpresa())
					.and(qVasilhameEntregueItemBaixa.idFilial.eq(registro.getIdFilial()))
					.and(qVasilhameEntregueItemBaixa.idCodigo.eq(registro.getIdCodigo())))
				.fetchOne();
	}
	
	
	public Double findSumQuantidade(VasilhameEntregueItemBaixa registro) {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregueItemBaixa = QVasilhameEntregueItemBaixa.vasilhameEntregueItemBaixa;

		return this.query.select((NumberExpression<Double>) qVasilhameEntregueItemBaixa.quantidade.coalesce(0.0).asNumber()
						.sum())
				.from(qVasilhameEntregueItemBaixa)
				.where(qVasilhameEntregueItemBaixa.idEmpresa.eq(registro.getIdEmpresa())
						.and(qVasilhameEntregueItemBaixa.idFilial.eq(registro.getIdFilial()))
						.and(qVasilhameEntregueItemBaixa.idVasilhameEntregueItem
								.eq(registro.getIdVasilhameEntregueItem())))
				.fetchOne();
	}
	
	
	public Integer findSituacaoVasilhaEntregue(Integer idEmpresa, Integer idFilial, Integer idCodigo) {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregueItemBaixa = QVasilhameEntregueItemBaixa.vasilhameEntregueItemBaixa;
		this.qVasilhameEntregueItem = QVasilhameEntregueItem.vasilhameEntregueItem;
		this.qVasilhameEntregue = QVasilhameEntregue.vasilhameEntregue;

		return this.query.select(qVasilhameEntregue.situacao)
				.from(qVasilhameEntregueItemBaixa)
				.leftJoin(qVasilhameEntregueItemBaixa.vasilhameEntregueItem, qVasilhameEntregueItem)
				.leftJoin(qVasilhameEntregueItem.vasilhameEntregue, qVasilhameEntregue)
				.where(qVasilhameEntregueItemBaixa.idEmpresa.eq(idEmpresa)				
				.and(qVasilhameEntregueItemBaixa.idFilial.eq(idFilial))
				.and(qVasilhameEntregueItemBaixa.idCodigo.eq(idCodigo))).fetchOne();
	}
	
	public VasilhameEntregueItem findVasilhaEntregue(Integer idEmpresa, Integer idFilial, Integer idItem) {
		this.query = new JPAQuery<>(em);		
		this.qVasilhameEntregueItem = QVasilhameEntregueItem.vasilhameEntregueItem;
		this.qVasilhameEntregue = QVasilhameEntregue.vasilhameEntregue;

		return this.query.from(qVasilhameEntregueItem)				
				.leftJoin(qVasilhameEntregueItem.vasilhameEntregue, qVasilhameEntregue)
				.leftJoin(qVasilhameEntregue.itens, qVasilhameEntregueItem)
				.where(qVasilhameEntregueItem.idEmpresa.eq(idEmpresa)				
				.and(qVasilhameEntregueItem.idFilial.eq(idFilial))
				.and(qVasilhameEntregueItem.idCodigo.eq(idItem)))				
				.transform(
				         GroupBy.groupBy(qVasilhameEntregue.id)
				         .list(this.projecaoVasilhameEntregue())).get(0);
	}
	
	public Boolean existeVasilhameEntregueItemBaixa(VasilhameEntregueItemBaixa registro){
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregueItemBaixa = QVasilhameEntregueItemBaixa.vasilhameEntregueItemBaixa;
		
		return null!=this.query.select(qVasilhameEntregueItemBaixa.idCodigo)
				.from(qVasilhameEntregueItemBaixa)
				.where(qVasilhameEntregueItemBaixa.idEmpresa.eq(registro.getIdEmpresa())
				.and(qVasilhameEntregueItemBaixa.idFilial.eq(registro.getIdFilial())
				.and(qVasilhameEntregueItemBaixa.idCodigo.eq(registro.getIdCodigo()))))
				.fetchOne();
	}
	
	public Boolean existeVasilhameEntregueItemBaixa(Integer idEmpresa, Integer idFilial, Integer idCodigo){
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregueItemBaixa = QVasilhameEntregueItemBaixa.vasilhameEntregueItemBaixa;
		
		return null!=this.query.select(qVasilhameEntregueItemBaixa.idCodigo)
				.from(qVasilhameEntregueItemBaixa)
				.where(qVasilhameEntregueItemBaixa.idEmpresa.eq(idEmpresa)
				.and(qVasilhameEntregueItemBaixa.idFilial.eq(idFilial)
				.and(qVasilhameEntregueItemBaixa.idCodigo.eq(idCodigo))))
				.fetchOne();
	}
	
	@Override
	public List<CampoErroDTO> salvar(RegistroDTO<VasilhameEntregueItemBaixa> dto) throws Exception {
		final List<CampoErroDTO> erros = this.validar(dto);
		if (erros.isEmpty()) {
			this.getEm().persist(dto.registro);

			Integer idVasilhameEntregue = this.vasilhameEntregueItemCadManagerBean.findVasilhameEntregue(
					dto.registro.getIdEmpresa(), dto.registro.getIdFilial(),
					dto.registro.getIdVasilhameEntregueItem());
			
			if (idVasilhameEntregue != null) {
				VasilhameEntregue vasilhameEntregue = this.vasilhameEntregueCadManagerBean
						.findVasilhameEntregueItem(dto.registro.getIdEmpresa(), dto.registro.getIdFilial(),
								idVasilhameEntregue);
				
				if (vasilhameEntregue != null) {
					this.vasilhameEntregueCadManagerBean.mudaSituacaoVasilhame(vasilhameEntregue);				
				}
			}
		}
		return erros;
	}
	
	@Override
	public List<CampoErroDTO> atualizar(RegistroDTO<VasilhameEntregueItemBaixa> dto) throws Exception {
		final List<CampoErroDTO> erros = this.validar(dto);
		if (erros.isEmpty()) {					
			dto.registro = this.getEm().merge(dto.registro);
			
			Integer idVasilhameEntregue = this.vasilhameEntregueItemCadManagerBean.findVasilhameEntregue(
					dto.registro.getIdEmpresa(), dto.registro.getIdFilial(),
					dto.registro.getIdVasilhameEntregueItem());
			
			if (idVasilhameEntregue != null) {
				VasilhameEntregue vasilhameEntregue = this.vasilhameEntregueCadManagerBean
						.findVasilhameEntregueItem(dto.registro.getIdEmpresa(), dto.registro.getIdFilial(),
								idVasilhameEntregue);
				
				if (vasilhameEntregue != null) {
					this.vasilhameEntregueCadManagerBean.mudaSituacaoVasilhame(vasilhameEntregue);				
				}
			}		
		}
		return erros;
	}
	
	@Override
	public List<CampoErroDTO> validar(RegistroDTO<VasilhameEntregueItemBaixa> dto) {
		List<CampoErroDTO> erros = new ArrayList<CampoErroDTO>();

		this.validaEmpresa(dto, erros);
		this.validaFilial(dto, erros);
		this.validaUsuarioFilial(dto, erros);
		this.validaVasilhameEntregueItem(dto, erros);
		this.validaTipoBaixa(dto, erros);
		this.validaQuantidade(dto, erros);
		this.validaUsuario(dto, erros);

		return erros;
	}

	private void validaEmpresa(RegistroDTO<VasilhameEntregueItemBaixa> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getIdEmpresa() == null) {
			erros.add(new CampoErroDTO("idEmpresa", "Campo obrigatório"));
		} else if (!this.empresaManagerBean.existeEmpresa(dto.registro.getIdEmpresa())) {
			erros.add(new CampoErroDTO("idEmpresa", "Empresa não cadastrada"));			
		}
	}
	
	private void validaFilial(RegistroDTO<VasilhameEntregueItemBaixa> dto, List<CampoErroDTO> erros) {	
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
	
	private void validaUsuarioFilial(RegistroDTO<VasilhameEntregueItemBaixa> dto, List<CampoErroDTO> erros) {
		if ((dto.registro.getIdUsuario() != "" && dto.registro.getIdUsuario() != null) && dto.registro.getIdEmpresa() != null
				&& dto.registro.getIdFilial() != null) {
			if (!this.usuarioFilialCadManagerBean.validaFilialLiberada(dto.registro.getIdUsuario(),
					dto.registro.getIdEmpresa(), dto.registro.getIdFilial())) {
				erros.add(new CampoErroDTO("idFilial", "Filial não liberada para este usuário"));
			}
		}
	}
	
	private void validaVasilhameEntregueItem(RegistroDTO<VasilhameEntregueItemBaixa> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getIdEmpresa() != null && dto.registro.getIdFilial() != null
				&& dto.registro.getIdVasilhameEntregueItem() != null) {

			if (!this.vasilhameEntregueItemCadManagerBean.existeVasilhameEntregueItem(dto.registro.getIdEmpresa(),
					dto.registro.getIdFilial(), dto.registro.getIdVasilhameEntregueItem())) {
				erros.add(new CampoErroDTO("idVasilhameEntregueItem", "Vasilhame Entregue item não cadastrado"));
			} else {
				Integer situacao = this.vasilhameEntregueItemCadManagerBean.findSituacaoVasilhameEntregue(
						dto.registro.getIdEmpresa(), dto.registro.getIdFilial(), dto.registro.getIdVasilhameEntregueItem());
												
				if (situacao == 8) {
					erros.add(new CampoErroDTO("idVasilhameEntregueItem", "Situação do vasilhame entregue está inutilizado"));
				}	
			}		
		}
	}	
	
	private void validaTipoBaixa(RegistroDTO<VasilhameEntregueItemBaixa> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getTipoBaixa() != null) {
			if (!TipoBaixa.existe(dto.registro.getTipoBaixa())) {
				erros.add(new CampoErroDTO("tipoBaixa", "Tipo Baixa inválida"));
			}
		} else {
			erros.add(new CampoErroDTO("tipoBaixa", "Campo obrigatório"));
		}
	}	

	private void validaQuantidade(RegistroDTO<VasilhameEntregueItemBaixa> dto, List<CampoErroDTO> erros) {
		this.query = new JPAQuery<>(this.em);
		this.qVasilhameEntregueItemBaixa = QVasilhameEntregueItemBaixa.vasilhameEntregueItemBaixa;
		this.qVasilhameEntregueItem = QVasilhameEntregueItem.vasilhameEntregueItem;

		if (dto.registro.getIdVasilhameEntregueItem() != null) {

			Double quantidadeItem = this.vasilhameEntregueItemCadManagerBean.findQuantidade(dto.registro.getIdEmpresa(),
					dto.registro.getIdFilial(), dto.registro.getIdVasilhameEntregueItem());
			if (dto.registro.getQuantidade() > quantidadeItem) {
				erros.add(new CampoErroDTO("quantidade", "Quantidade da baixa maior que a quantidade do Item"));
			} else {
				Double quantidadeBaixada = 0.0;
				
				if (dto.registro.getIdEmpresa() != null && dto.registro.getIdFilial() != null
						&& dto.registro.getIdVasilhameEntregueItem() != null) {
					quantidadeBaixada = this.findSumQuantidade(dto.registro);
				}		
				
				if (quantidadeBaixada == null) quantidadeBaixada = 0.0;
				quantidadeBaixada += dto.registro.getQuantidade();

				if (dto.registro.getIdCodigo() != null) {
					this.query = new JPAQuery<>(this.em);
					Double last = this.query.select(qVasilhameEntregueItemBaixa.quantidade.coalesce(0.0))
							.from(qVasilhameEntregueItemBaixa)
							.where(qVasilhameEntregueItemBaixa.idEmpresa.eq(dto.registro.getIdEmpresa())
									.and(qVasilhameEntregueItemBaixa.idFilial.eq(dto.registro.getIdFilial()))
									.and(qVasilhameEntregueItemBaixa.idCodigo.eq(dto.registro.getIdCodigo())))
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
	
	private void validaUsuario(RegistroDTO<VasilhameEntregueItemBaixa> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getIdUsuario() != null && dto.registro.getIdUsuario() != "") {			
			if (!this.usuarioManagerBean.existeUsuario(dto.registro.getIdUsuario())) {
				erros.add(new CampoErroDTO("idUsuario", "Usuário não cadastrado"));
			} 
		}
	}
	
	
	@Override
	public List<CampoErroDTO> validaDeletar(List<VasilhameEntregueItemBaixa> registros) {
		List<CampoErroDTO> erros = new ArrayList<>();
		erros.add(new CampoErroDTO("idVasilhameEntregueItem", "Método \"validaDeletar\" deve passar a opção da permissão \"excluir\""));
		return erros;
	}

	public List<CampoErroDTO> validaDeletar(List<VasilhameEntregueItemBaixa> registros,
			String opcao, String msg) {
		List<CampoErroDTO> erros = new ArrayList<>();
		
		for (VasilhameEntregueItemBaixa baixa : registros) {
			if (opcao.equals("3")) {
				if (!baixa.getTipoBaixa().equals(2)) {
					erros.add(new CampoErroDTO("idVasilhameEntregueItem", msg));
				}
			}
			this.validaDeletarVasilhame(baixa, erros);
		}		

		return erros;
	}
	
	private void validaDeletarVasilhame(VasilhameEntregueItemBaixa baixa, List<CampoErroDTO> erros) {
		if (baixa != null) {
			if (baixa.getIdVasilhameEntregueItem() != null) {
				
				Integer situacao = this.vasilhameEntregueItemCadManagerBean.findSituacaoVasilhameEntregue(
						baixa.getIdEmpresa(), baixa.getIdFilial(), baixa.getIdVasilhameEntregueItem());

				if (situacao == 8) {
					erros.add(new CampoErroDTO("idVasilhameEntregueItem", "Situação do vasilhame entregue está inutilizado"));
				}									
			}						
		}
	}

	@Override
	public List<CampoErroDTO> deletar(List<VasilhameEntregueItemBaixa> vasilhameEntregueItemBaixas) throws Exception {
		List<CampoErroDTO> erros = new ArrayList<>();
		erros.add(new CampoErroDTO("idVasilhameEntregueItem", "Método \"deletar\" deve passar a opção da permissão \"excluir\""));
		return erros;
	}
	
	public List<CampoErroDTO> deletar(List<VasilhameEntregueItemBaixa> vasilhameEntregueItemBaixas,
			String opcao, String msg) throws Exception {
		List<CampoErroDTO> erros = validaDeletar(vasilhameEntregueItemBaixas, opcao, msg);
		if (erros.isEmpty()) {
			this.qVasilhameEntregueItemBaixa = QVasilhameEntregueItemBaixa.vasilhameEntregueItemBaixa;
//			this.qVasilhameEntregueItemBaixaCupom = QVasilhameEntregueItemBaixaCupom.vasilhameEntregueItemBaixaCupom;
//			this.qVasilhameEntregueItemBaixaNota = QVasilhameEntregueItemBaixaNota.vasilhameEntregueItemBaixaNota;			
						
			for (VasilhameEntregueItemBaixa vasilhameEntregueItemBaixa : vasilhameEntregueItemBaixas) {
//				new JPADeleteClause(em, qVasilhameEntregueItemBaixaCupom)
//					.where(qVasilhameEntregueItemBaixaCupom.idEmpresaBaixa.eq(vasilhameEntregueItemBaixa.getIdEmpresa())
//						.and(qVasilhameEntregueItemBaixaCupom.idFilialBaixa.eq(vasilhameEntregueItemBaixa.getIdFilial())
//						.and(qVasilhameEntregueItemBaixaCupom.idVasilhameEntregueItemBaixa.eq(vasilhameEntregueItemBaixa.getIdCodigo()))))
//					.execute();
//				
//				new JPADeleteClause(em, qVasilhameEntregueItemBaixaNota)
//				.where(qVasilhameEntregueItemBaixaNota.idEmpresaBaixa.eq(vasilhameEntregueItemBaixa.getIdEmpresa())
//						.and(qVasilhameEntregueItemBaixaNota.idFilialBaixa.eq(vasilhameEntregueItemBaixa.getIdFilial())
//						.and(qVasilhameEntregueItemBaixaNota.idVasilhameEntregueItemBaixa.eq(vasilhameEntregueItemBaixa.getIdCodigo()))))
//					.execute();
				
				this.getEm().remove(this.getEm().find(VasilhameEntregueItemBaixa.class, vasilhameEntregueItemBaixa.getId()));
				
				Integer idVasilhameEntregue = this.vasilhameEntregueItemCadManagerBean.findVasilhameEntregue(
						vasilhameEntregueItemBaixa.getIdEmpresa(), vasilhameEntregueItemBaixa.getIdFilial(),
						vasilhameEntregueItemBaixa.getIdVasilhameEntregueItem());
				
				if (idVasilhameEntregue != null) {
					VasilhameEntregue vasilhameEntregue = this.vasilhameEntregueCadManagerBean
							.findVasilhameEntregueItem(vasilhameEntregueItemBaixa.getIdEmpresa(), vasilhameEntregueItemBaixa.getIdFilial(),
									idVasilhameEntregue);
					
					if (vasilhameEntregue != null) {
						this.vasilhameEntregueCadManagerBean.mudaSituacaoVasilhame(vasilhameEntregue);
					}
				}
				
				if (vasilhameEntregueItemBaixa.getIdEmpresa() != null && vasilhameEntregueItemBaixa.getIdFilial() != null && vasilhameEntregueItemBaixa.getIdVasilhameEntregueItem() != null) {
					vasilhameEntregueItemBaixa.setQuantidadeBaixada(this.findSumQuantidade(vasilhameEntregueItemBaixa));					
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
	public Class<VasilhameEntregueItemBaixa> getTypeParameterClass() {
		return VasilhameEntregueItemBaixa.class;
	}

}
