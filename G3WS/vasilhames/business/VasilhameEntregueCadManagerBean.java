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
import com.g3sistemas.geral.business.EmpresaCadManagerBean;
import com.g3sistemas.geral.business.FilialCadManagerBean;
import com.g3sistemas.geral.business.pessoas.PessoaCadManagerBean;
import com.g3sistemas.geral.entity.Pessoa;
import com.g3sistemas.geral.entity.QEmpresa;
import com.g3sistemas.geral.entity.QFilial;
import com.g3sistemas.geral.entity.QPessoa;
import com.g3sistemas.pdv.enums.Situacao;
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
import com.g3sistemas.vasilhames.enums.TipoContribuinte;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;

@Stateless
public class VasilhameEntregueCadManagerBean extends AbstractCadManagerBean<VasilhameEntregue> {

	@PersistenceContext(unitName = "g3-ds")
	private EntityManager em;

	@EJB
	private EmpresaCadManagerBean empresaManagerBean;

	@EJB
	private FilialCadManagerBean filialCadManagerBean;
	
	@EJB
	private PessoaCadManagerBean pessoaCadManagerBean;
	
	@EJB
	private UsuarioManagerBean usuarioManagerBean;
	
	@EJB
	private UsuarioFilialCadManagerBean usuarioFilialCadManagerBean;
	
	@EJB
	private VasilhameEntregueItemCadManagerBean vasilhameEntregueItemCadManagerBean;
	
	@EJB
	private VasilhameEntregueCadManagerBean vasilhameEntregueCadManagerBean;
		

	private QVasilhameEntregue qVasilhameEntregue;
	private QVasilhameEntregueItem qVasilhameEntregueItem;	
	private QVasilhameEntregueItemBaixa qVasilhameEntregueItemBaixa;
	private QVasilhameEntregueItemBaixaCupom qVasilhameEntregueItemBaixaCupom;
	private QVasilhameEntregueItemBaixaNota qVasilhameEntregueItemBaixaNota;	
	private QEmpresa qEmpresa;
	private QFilial qFilial;
	private QPessoa qPessoa;
	private QUsuario qUsuario;

	private JPAQuery<VasilhameEntregue> query;

	private ConstructorExpression<VasilhameEntregue> projecaoVasilhameEntregue() {
		return QVasilhameEntregue.create(qVasilhameEntregue.id, qVasilhameEntregue.idEmpresa,
				QEmpresa.create(qEmpresa.id, qEmpresa.nome), qVasilhameEntregue.idFilial,
				QFilial.create(qFilial.codigo, qFilial.descricao), qVasilhameEntregue.idCodigo,
				qVasilhameEntregue.idPessoa,
				QPessoa.create(qPessoa.idPessoa, qPessoa.digito, qPessoa.nome, qPessoa.contribuinte, qPessoa.cpfCnpj),
				qVasilhameEntregue.nome, qVasilhameEntregue.tipoContribuinte, qVasilhameEntregue.cpfcnpj,
				qVasilhameEntregue.observacao, qVasilhameEntregue.situacao, qVasilhameEntregue.dataHoraGravacao,
				qVasilhameEntregue.idUsuario, QUsuario.create(qUsuario.nome));
	}
	
	private ConstructorExpression<VasilhameEntregue> projecaoVasilhameEntregueItem() {
		return QVasilhameEntregue.create(qVasilhameEntregue.id, qVasilhameEntregue.idEmpresa,
				qVasilhameEntregue.idFilial, qVasilhameEntregue.idCodigo, qVasilhameEntregue.idPessoa,
				qVasilhameEntregue.nome, qVasilhameEntregue.tipoContribuinte, qVasilhameEntregue.cpfcnpj,
				qVasilhameEntregue.observacao, qVasilhameEntregue.situacao, qVasilhameEntregue.dataHoraGravacao,
				qVasilhameEntregue.idUsuario,
				GroupBy.list(QVasilhameEntregueItem.create(qVasilhameEntregueItem.idEmpresa,
						qVasilhameEntregueItem.idFilial, qVasilhameEntregueItem.idCodigo,
						qVasilhameEntregueItem.idVasilhameEntregue, qVasilhameEntregueItem.quantidade)));
	}
	
	private ConstructorExpression<VasilhameEntregue> projecaoSituacao() {
		return QVasilhameEntregue.create(qVasilhameEntregue.id, qVasilhameEntregue.situacao);
	}

	public VasilhameEntregue find(VasilhameEntregue registro) {
		this.query = new JPAQuery<>(this.em);
		this.qVasilhameEntregue = QVasilhameEntregue.vasilhameEntregue;
		this.qEmpresa = QEmpresa.empresa;
		this.qFilial = QFilial.filial;
		this.qPessoa = QPessoa.pessoa;
		this.qUsuario = QUsuario.usuario;

		return this.query.select(this.projecaoVasilhameEntregue())
				.from(qVasilhameEntregue)
				.leftJoin(qVasilhameEntregue.empresa, qEmpresa)
				.leftJoin(qVasilhameEntregue.filial, qFilial)
				.leftJoin(qVasilhameEntregue.pessoa, qPessoa)
				.leftJoin(qVasilhameEntregue.usuario, qUsuario)
				.where(qVasilhameEntregue.idEmpresa.eq(registro.getIdEmpresa())
				.and(qVasilhameEntregue.idFilial.eq(registro.getIdFilial()))
				.and(qVasilhameEntregue.idCodigo.eq(registro.getIdCodigo())))
				.fetchOne();
	}
	
	public VasilhameEntregue find(Integer idEmpresa, Integer idFilial, Integer idCodigo) {
		this.query = new JPAQuery<>(this.em);
		this.qVasilhameEntregue = QVasilhameEntregue.vasilhameEntregue;
		this.qEmpresa = QEmpresa.empresa;
		this.qFilial = QFilial.filial;
		this.qPessoa = QPessoa.pessoa;
		this.qUsuario = QUsuario.usuario;

		return this.query.select(this.projecaoVasilhameEntregue())
				.from(qVasilhameEntregue)
				.leftJoin(qVasilhameEntregue.empresa, qEmpresa)
				.leftJoin(qVasilhameEntregue.filial, qFilial)
				.leftJoin(qVasilhameEntregue.pessoa, qPessoa)
				.leftJoin(qVasilhameEntregue.usuario, qUsuario)
				.where(qVasilhameEntregue.idEmpresa.eq(idEmpresa)
				.and(qVasilhameEntregue.idFilial.eq(idFilial))
				.and(qVasilhameEntregue.idCodigo.eq(idCodigo)))
				.fetchOne();
	}
	
	public VasilhameEntregue findSituacao(VasilhameEntregue registro) {
		this.query = new JPAQuery<>(this.em);
		this.qVasilhameEntregue = QVasilhameEntregue.vasilhameEntregue;		

		return this.query.select(this.projecaoSituacao())
				.from(qVasilhameEntregue)				
				.where(qVasilhameEntregue.idEmpresa.eq(registro.getIdEmpresa())
					.and(qVasilhameEntregue.idFilial.eq(registro.getIdFilial()))
					.and(qVasilhameEntregue.idCodigo.eq(registro.getIdCodigo())))
				.fetchOne();
	}
	
	public VasilhameEntregue findVasilhameEntregueItem(Integer idEmpresa, Integer idFilial, Integer idCodigo) {
		this.query = new JPAQuery<>(this.em);
		this.qVasilhameEntregue = QVasilhameEntregue.vasilhameEntregue;
		this.qVasilhameEntregueItem = QVasilhameEntregueItem.vasilhameEntregueItem;			
		
		return this.query.from(qVasilhameEntregue)
				.leftJoin(qVasilhameEntregue.itens, qVasilhameEntregueItem)						
				.where(qVasilhameEntregue.idEmpresa.eq(idEmpresa)
						.and(qVasilhameEntregue.idFilial.eq(idFilial))
						.and(qVasilhameEntregue.idCodigo.eq(idCodigo)))
				.transform(
				         GroupBy.groupBy(qVasilhameEntregue.id)
				         .list(this.projecaoVasilhameEntregueItem())).get(0);
	}	
	
	public Boolean existeItens(Integer idEmpresa, Integer idFilial, Integer idCodigo,
			Integer idItem) {
		this.query = new JPAQuery<>(this.em);
		this.qVasilhameEntregue = QVasilhameEntregue.vasilhameEntregue;
		this.qVasilhameEntregueItem = QVasilhameEntregueItem.vasilhameEntregueItem;
		
		return null!=this.query.select(qVasilhameEntregueItem.idCodigo)
				.from(qVasilhameEntregue)
				.leftJoin(qVasilhameEntregue.itens, qVasilhameEntregueItem)						
				.where(qVasilhameEntregue.idEmpresa.eq(idEmpresa)
						.and(qVasilhameEntregue.idFilial.eq(idFilial))
						.and(qVasilhameEntregue.idCodigo.eq(idCodigo))
						.and(qVasilhameEntregueItem.idCodigo.ne(idItem)))
				.fetchOne();
	}
	
	public Boolean existeVasilhameEntregue(VasilhameEntregue registro){
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregue = QVasilhameEntregue.vasilhameEntregue;
		
		return null!=this.query.select(qVasilhameEntregue.idCodigo)
				.from(qVasilhameEntregue)
				.where(qVasilhameEntregue.idEmpresa.eq(registro.getIdEmpresa())
				.and(qVasilhameEntregue.idFilial.eq(registro.getIdFilial())
				.and(qVasilhameEntregue.idCodigo.eq(registro.getIdCodigo()))))
				.fetchOne();
	}
	
	public Boolean existeVasilhameEntregue(Integer idEmpresa, Integer idFilial, Integer idCodigo){
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregue = QVasilhameEntregue.vasilhameEntregue;
		
		return null!=this.query.select(qVasilhameEntregue.idCodigo)
				.from(qVasilhameEntregue)
				.where(qVasilhameEntregue.idEmpresa.eq(idEmpresa)
				.and(qVasilhameEntregue.idFilial.eq(idFilial)
				.and(qVasilhameEntregue.idCodigo.eq(idCodigo))))
				.fetchOne();
	}

	public Boolean validaSituacaoAberta(Integer idEmpresa, Integer idFilial, Integer idCodigo) {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregue = QVasilhameEntregue.vasilhameEntregue;
		
		return null!=this.query.select(qVasilhameEntregue.id)
			.from(qVasilhameEntregue)
			.where(qVasilhameEntregue.idEmpresa.eq(idEmpresa)
				.and(qVasilhameEntregue.idFilial.eq(idFilial))
				.and(qVasilhameEntregue.idCodigo.eq(idCodigo))
				.and(qVasilhameEntregue.situacao.ne(1)))
			.fetchOne();
	}	
	
	@Override
	public List<CampoErroDTO> validar(RegistroDTO<VasilhameEntregue> dto) {
		List<CampoErroDTO> erros = new ArrayList<CampoErroDTO>();

		this.validaEmpresa(dto, erros);
		this.validaFilial(dto, erros);
		this.validaUsuarioFilial(dto, erros);
		this.validaPessoa(dto, erros);
		this.validaNome(dto, erros);
		this.validaTipoContribuinte(dto, erros);
		this.validaSituacao(dto, erros);
		this.validaUsuario(dto, erros);

		return erros;
	}

	private void validaEmpresa(RegistroDTO<VasilhameEntregue> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getIdEmpresa() == null) {
			erros.add(new CampoErroDTO("idEmpresa", "Campo obrigatório"));
		} else if (!this.empresaManagerBean.existeEmpresa(dto.registro.getIdEmpresa())) {
			erros.add(new CampoErroDTO("idEmpresa", "Empresa não cadastrada"));			
		}
	}
	
	private void validaFilial(RegistroDTO<VasilhameEntregue> dto, List<CampoErroDTO> erros) {	
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
	
	private void validaUsuarioFilial(RegistroDTO<VasilhameEntregue> dto, List<CampoErroDTO> erros) {
		if ((dto.registro.getIdUsuario() != "" && dto.registro.getIdUsuario() != null) && dto.registro.getIdEmpresa() != null
				&& dto.registro.getIdFilial() != null) {
			if (!this.usuarioFilialCadManagerBean.validaFilialLiberada(dto.registro.getIdUsuario(),
					dto.registro.getIdEmpresa(), dto.registro.getIdFilial())) {
				erros.add(new CampoErroDTO("idFilial", "Filial não liberada para este usuário"));
			}
		}
	}	
	
	private void validaPessoa(RegistroDTO<VasilhameEntregue> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getIdEmpresa() != null && (dto.registro.getIdPessoa() != null && dto.registro.getIdPessoa() != 0)) {
			if (dto.registro.getIdCodigo() != null) {				
				Pessoa pessoa = this.pessoaCadManagerBean.findBasic(dto.registro.getIdEmpresa(), dto.registro.getIdPessoa());
				if (pessoa == null) {
					erros.add(new CampoErroDTO("idPessoa", "Pessoa não cadastrada"));
				} else if (dto.registro.getPessoa() == null || dto.registro.getPessoa().getDigito() == null 
						|| pessoa.getDigito() == null || !pessoa.getDigito().equals(dto.registro.getPessoa().getDigito())) {
					erros.add(new CampoErroDTO("idPessoa", "Dígito inválido"));
				}				
			} else {
				Pessoa pessoa = this.pessoaCadManagerBean.findPessoaAtiva(dto.registro.getIdEmpresa(), dto.registro.getIdPessoa());
				if (pessoa == null) {
					erros.add(new CampoErroDTO("idPessoa", "Pessoa com situação inativa"));
				} else if (dto.registro.getPessoa() == null || dto.registro.getPessoa().getDigito() == null 
						|| pessoa.getDigito() == null || !pessoa.getDigito().equals(dto.registro.getPessoa().getDigito())) {
					erros.add(new CampoErroDTO("idPessoa", "Dígito inválido"));
				}				
			}
		}	
	}
	
//	private void validaPessoaAtiva(RegistroDTO<VasilhameEntregue> dto, List<CampoErroDTO> erros) {
//		if (dto.registro.getIdEmpresa() != null && (dto.registro.getIdPessoa() != null && dto.registro.getIdPessoa() != 0)) {
//			Pessoa pessoa = pessoaCadManagerBean.findPessoaAtiva(dto.registro.getIdEmpresa(), dto.registro.getIdPessoa());
//			if (pessoa == null) {
//				erros.add(new CampoErroDTO("idPessoa", "Pessoa está inativa."));
//			} else if (dto.registro.getPessoa() == null || dto.registro.getPessoa().getDigito() == null 
//					|| pessoa.getDigito() == null || !pessoa.getDigito().equals(dto.registro.getPessoa().getDigito())) {
//				erros.add(new CampoErroDTO("idPessoa", "Dígito inválido"));
//			}
//		}
//	}	

	private void validaNome(RegistroDTO<VasilhameEntregue> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getNome() == "") {
			erros.add(new CampoErroDTO("nome", "Campo obrigatório"));
		} 
	}	
	
	private void validaTipoContribuinte(RegistroDTO<VasilhameEntregue> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getTipoContribuinte() != null) {
			if (!TipoContribuinte.existe(dto.registro.getTipoContribuinte())) {
				erros.add(new CampoErroDTO("tipoContribuinte", "Tipo Contribuinte inválido"));
			}
		} else {
			erros.add(new CampoErroDTO("tipoContribuinte", "Campo obrigatório"));
		}
	}
	
	private void validaSituacao(RegistroDTO<VasilhameEntregue> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getSituacao() != null) {
			if (!Situacao.existe(dto.registro.getSituacao())) {
				erros.add(new CampoErroDTO("situacao", "Siutuação inválida"));
			}
		} else {
			erros.add(new CampoErroDTO("situacao", "Campo obrigatório"));
		}
	}

	private void validaUsuario(RegistroDTO<VasilhameEntregue> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getIdUsuario() != null && dto.registro.getIdUsuario() != "") {
			if (!this.usuarioManagerBean.existeUsuario(dto.registro.getIdUsuario())) {
				erros.add(new CampoErroDTO("idUsuario", "Usuário não cadastrado"));
			}
		}
	}
	
	public void atualizaSituacaoVasilhame(RegistroDTO<VasilhameEntregue> dto, Integer situacao) {		
		if (dto.registro.getIdEmpresa() != null && dto.registro.getIdFilial() != null
				&& dto.registro.getIdCodigo() != null) {

			VasilhameEntregue vasilhame = this.find(dto.registro.getIdEmpresa(), dto.registro.getIdFilial(),
					dto.registro.getIdCodigo());
			vasilhame.setFilial(null);
			vasilhame.setPessoa(null);
			vasilhame.setUsuario(null);
			vasilhame.setSituacao(situacao);			

			dto.registro = this.getEm().merge(vasilhame);
		}		
	}
	
	public void mudaSituacaoVasilhame(VasilhameEntregue vasilhame) {				
		if (vasilhame != null) {
			if (!vasilhame.getSituacao().equals(8)) {
				if (vasilhame.getItens() != null) {
					Boolean fechado = this.verificaSituacaoVasilhame(vasilhame);
					if (fechado != null) {
						if (fechado == true && !vasilhame.getSituacao().equals(9)) {
							vasilhame.setSituacao(9);
							vasilhame.setFilial(null);
							vasilhame.setPessoa(null);
							vasilhame.setUsuario(null);
							vasilhame.setItens(null);
							vasilhame = this.getEm().merge(vasilhame);
						}
						if (fechado == false && vasilhame.getSituacao().equals(9)) {
							vasilhame.setSituacao(2);
							vasilhame.setFilial(null);
							vasilhame.setPessoa(null);
							vasilhame.setUsuario(null);
							vasilhame.setItens(null);
							vasilhame = this.getEm().merge(vasilhame);
						}
					}
				}
			}
		}
	}
	
	//verifica se a quantidade das baixas é igual a quantidade do item, se é retorna true
	public Boolean verificaSituacaoVasilhame(VasilhameEntregue vasilhame) {
		Boolean fechado = true;
		
		if (vasilhame.getItens() != null) {			
			for (VasilhameEntregueItem vasilhameEntregueItem : vasilhame.getItens()) {
				if (vasilhameEntregueItem.getIdEmpresa() != null && vasilhameEntregueItem.getIdFilial() != null && vasilhameEntregueItem.getIdCodigo() != null) {
					Double quantidadeBaixada = this.vasilhameEntregueItemCadManagerBean.findQuantidadeBaixada(vasilhameEntregueItem);					
					if (quantidadeBaixada == null) quantidadeBaixada = 0.0;
					
					if (Double.compare(quantidadeBaixada, vasilhameEntregueItem.getQuantidade()) != 0) {
						fechado = false;
						break;
					}
				} else {
					return null;
				}
			}			
		}		
		
		return fechado;
	}
	
	@Override
	public List<CampoErroDTO> validaDeletar(List<VasilhameEntregue> registros) {
		return new ArrayList<CampoErroDTO>();
	}
	
	@Override
	public List<CampoErroDTO> deletar(List<VasilhameEntregue> vasilhameEntregues) throws Exception {
		List<CampoErroDTO> erros = validaDeletar(vasilhameEntregues);
		if (erros.isEmpty()) {
			this.qVasilhameEntregue = QVasilhameEntregue.vasilhameEntregue;
			this.qVasilhameEntregueItem = QVasilhameEntregueItem.vasilhameEntregueItem;
			this.qVasilhameEntregueItemBaixa = QVasilhameEntregueItemBaixa.vasilhameEntregueItemBaixa;
			this.qVasilhameEntregueItemBaixaCupom = QVasilhameEntregueItemBaixaCupom.vasilhameEntregueItemBaixaCupom;
			this.qVasilhameEntregueItemBaixaNota = QVasilhameEntregueItemBaixaNota.vasilhameEntregueItemBaixaNota;
			
			for (VasilhameEntregue vasilhameEntregue : vasilhameEntregues) {
				this.query = new JPAQuery<>(this.em);
				List<Integer> resultItem = query.select(qVasilhameEntregueItem.idCodigo).from(qVasilhameEntregueItem)
						.where(qVasilhameEntregueItem.idEmpresa.eq(vasilhameEntregue.getIdEmpresa())
								.and(qVasilhameEntregueItem.idFilial.eq(vasilhameEntregue.getIdFilial())
								.and(qVasilhameEntregueItem.idVasilhameEntregue.eq(vasilhameEntregue.getIdCodigo())))).fetch();

				for (int i = 0; i < resultItem.size(); i++) {
					this.query = new JPAQuery<>(this.em);
					List<Integer> resultBaixa = query.select(qVasilhameEntregueItemBaixa.idCodigo)
							.from(qVasilhameEntregueItemBaixa)
							.where(qVasilhameEntregueItemBaixa.idEmpresa.eq(vasilhameEntregue.getIdEmpresa())
									.and(qVasilhameEntregueItemBaixa.idFilial.eq(vasilhameEntregue.getIdFilial())
									.and(qVasilhameEntregueItemBaixa.idVasilhameEntregueItem.eq(resultItem.get(i))))).fetch();

					for (int j = 0; j < resultBaixa.size(); j++) {
						new JPADeleteClause(em, qVasilhameEntregueItemBaixaCupom)
								.where(qVasilhameEntregueItemBaixaCupom.idEmpresaBaixa.eq(vasilhameEntregue.getIdEmpresa())
										.and(qVasilhameEntregueItemBaixaCupom.idFilialBaixa.eq(vasilhameEntregue.getIdFilial())
										.and(qVasilhameEntregueItemBaixaCupom.idVasilhameEntregueItemBaixa.eq(resultBaixa.get(j)))))
								.execute();

						new JPADeleteClause(em, qVasilhameEntregueItemBaixaNota)
								.where(qVasilhameEntregueItemBaixaNota.idEmpresaBaixa.eq(vasilhameEntregue.getIdEmpresa())
										.and(qVasilhameEntregueItemBaixaNota.idFilialBaixa.eq(vasilhameEntregue.getIdFilial())
										.and(qVasilhameEntregueItemBaixaNota.idVasilhameEntregueItemBaixa.eq(resultBaixa.get(j)))))
								.execute();
					}
					new JPADeleteClause(em, qVasilhameEntregueItemBaixa)
							.where(qVasilhameEntregueItemBaixa.idEmpresa.eq(vasilhameEntregue.getIdEmpresa())
									.and(qVasilhameEntregueItemBaixa.idFilial.eq(vasilhameEntregue.getIdFilial())
									.and(qVasilhameEntregueItemBaixa.idVasilhameEntregueItem.eq(resultItem.get(i)))))
							.execute();

				}
				new JPADeleteClause(em, qVasilhameEntregueItem)
						.where(qVasilhameEntregueItem.idEmpresa.eq(vasilhameEntregue.getIdEmpresa())
								.and(qVasilhameEntregueItem.idFilial.eq(vasilhameEntregue.getIdFilial())
								.and(qVasilhameEntregueItem.idVasilhameEntregue.eq(vasilhameEntregue.getIdCodigo()))))						
						.execute();

				this.getEm().remove(this.getEm().find(VasilhameEntregue.class, vasilhameEntregue.getId()));
			}						
		}
		return erros;
	}

	@Override
	public EntityManager getEm() {
		return em;
	}

	@Override
	public Class<VasilhameEntregue> getTypeParameterClass() {
		return VasilhameEntregue.class;
	}

}
