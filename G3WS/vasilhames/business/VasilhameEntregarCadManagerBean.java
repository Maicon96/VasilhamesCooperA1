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
import com.g3sistemas.sistema.business.UsuarioFilialCadManagerBean;
import com.g3sistemas.sistema.business.UsuarioManagerBean;
import com.g3sistemas.sistema.entity.QUsuario;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregar;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregarItem;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregarItemBaixa;
import com.g3sistemas.vasilhames.entity.VasilhameEntregar;
import com.g3sistemas.vasilhames.entity.VasilhameEntregarItem;
import com.g3sistemas.vasilhames.enums.Situacao;
import com.g3sistemas.vasilhames.enums.TipoContribuinte;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;

@Stateless
public class VasilhameEntregarCadManagerBean extends AbstractCadManagerBean<VasilhameEntregar> {

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
	private VasilhameEntregarItemCadManagerBean vasilhameEntregarItemCadManagerBean;
	
	

	private QVasilhameEntregar qVasilhameEntregar;
	private QVasilhameEntregarItem qVasilhameEntregarItem;
	private QVasilhameEntregarItemBaixa qVasilhameEntregarItemBaixa;	
	private QEmpresa qEmpresa;
	private QFilial qFilial;
	private QPessoa qPessoa;
	private QUsuario qUsuario;

	private JPAQuery<VasilhameEntregar> query;

	private ConstructorExpression<VasilhameEntregar> projecaoVasilhameEntregar() {
		return QVasilhameEntregar.create(qVasilhameEntregar.id, qVasilhameEntregar.idEmpresa, QEmpresa.create(qEmpresa.id, qEmpresa.nome),
				qVasilhameEntregar.idFilial, QFilial.create(qFilial.codigo, qFilial.descricao),
				qVasilhameEntregar.idCodigo, qVasilhameEntregar.idPessoa,
				QPessoa.create(qPessoa.idPessoa, qPessoa.digito, qPessoa.nome, qPessoa.contribuinte, qPessoa.cpfCnpj),
				qVasilhameEntregar.nome, qVasilhameEntregar.tipoContribuinte, qVasilhameEntregar.cpfcnpj,
				qVasilhameEntregar.observacao, qVasilhameEntregar.situacao, qVasilhameEntregar.dataHoraGravacao,
				qVasilhameEntregar.idUsuario, QUsuario.create(qUsuario.nome));
	}
	
	private ConstructorExpression<VasilhameEntregar> projecaoVasilhameEntregarItem() {
		return QVasilhameEntregar.create(qVasilhameEntregar.id, qVasilhameEntregar.idEmpresa,
				qVasilhameEntregar.idFilial, qVasilhameEntregar.idCodigo, qVasilhameEntregar.idPessoa,
				qVasilhameEntregar.nome, qVasilhameEntregar.tipoContribuinte, qVasilhameEntregar.cpfcnpj,
				qVasilhameEntregar.observacao, qVasilhameEntregar.situacao, qVasilhameEntregar.dataHoraGravacao,
				qVasilhameEntregar.idUsuario,
				GroupBy.list(QVasilhameEntregarItem.create(qVasilhameEntregarItem.idEmpresa,
						qVasilhameEntregarItem.idFilial, qVasilhameEntregarItem.idCodigo,
						qVasilhameEntregarItem.idVasilhameEntregar, qVasilhameEntregarItem.quantidade)));
	}
	
	private ConstructorExpression<VasilhameEntregar> projecaoSituacao() {
		return QVasilhameEntregar.create(qVasilhameEntregar.id, qVasilhameEntregar.situacao);
	}

	public VasilhameEntregar find(VasilhameEntregar registro) {
		this.query = new JPAQuery<>(this.em);
		this.qVasilhameEntregar = QVasilhameEntregar.vasilhameEntregar;
		this.qEmpresa = QEmpresa.empresa;
		this.qFilial = QFilial.filial;
		this.qPessoa = QPessoa.pessoa;
		this.qUsuario = QUsuario.usuario;

		return this.query.select(this.projecaoVasilhameEntregar())
				.from(qVasilhameEntregar)
				.leftJoin(qVasilhameEntregar.empresa, qEmpresa)
				.leftJoin(qVasilhameEntregar.filial, qFilial)
				.leftJoin(qVasilhameEntregar.pessoa, qPessoa)
				.leftJoin(qVasilhameEntregar.usuario, qUsuario)
				.where(qVasilhameEntregar.idEmpresa.eq(registro.getIdEmpresa())
				.and(qVasilhameEntregar.idFilial.eq(registro.getIdFilial())
				.and(qVasilhameEntregar.idCodigo.eq(registro.getIdCodigo())))).fetchOne();
	}
	
	public VasilhameEntregar find(Integer idEmpresa, Integer idFilial, Integer idCodigo) {
		this.query = new JPAQuery<>(this.em);
		this.qVasilhameEntregar = QVasilhameEntregar.vasilhameEntregar;
		this.qEmpresa = QEmpresa.empresa;
		this.qFilial = QFilial.filial;
		this.qPessoa = QPessoa.pessoa;
		this.qUsuario = QUsuario.usuario;

		return this.query.select(this.projecaoVasilhameEntregar())
				.from(qVasilhameEntregar)
				.leftJoin(qVasilhameEntregar.empresa, qEmpresa)
				.leftJoin(qVasilhameEntregar.filial, qFilial)
				.leftJoin(qVasilhameEntregar.pessoa, qPessoa)
				.leftJoin(qVasilhameEntregar.usuario, qUsuario)
				.where(qVasilhameEntregar.idEmpresa.eq(idEmpresa)
				.and(qVasilhameEntregar.idFilial.eq(idFilial))
				.and(qVasilhameEntregar.idCodigo.eq(idCodigo)))
				.fetchOne();
	}
	
	public VasilhameEntregar findVasilhameEntregarItem(Integer idEmpresa, Integer idFilial, Integer idCodigo) {
		this.query = new JPAQuery<>(this.em);
		this.qVasilhameEntregar = QVasilhameEntregar.vasilhameEntregar;
		this.qVasilhameEntregarItem = QVasilhameEntregarItem.vasilhameEntregarItem;			
		
		return this.query.from(qVasilhameEntregar)
				.leftJoin(qVasilhameEntregar.itens, qVasilhameEntregarItem)						
				.where(qVasilhameEntregar.idEmpresa.eq(idEmpresa)
						.and(qVasilhameEntregar.idFilial.eq(idFilial))
						.and(qVasilhameEntregar.idCodigo.eq(idCodigo)))
				.transform(
				         GroupBy.groupBy(qVasilhameEntregar.id)
				         .list(this.projecaoVasilhameEntregarItem())).get(0);
	}
	
	public VasilhameEntregar findSituacao(VasilhameEntregar registro) {
		this.query = new JPAQuery<>(this.em);
		this.qVasilhameEntregar = QVasilhameEntregar.vasilhameEntregar;		

		return this.query.select(this.projecaoSituacao())
				.from(qVasilhameEntregar)				
				.where(qVasilhameEntregar.idEmpresa.eq(registro.getIdEmpresa())
					.and(qVasilhameEntregar.idFilial.eq(registro.getIdFilial()))
					.and(qVasilhameEntregar.idCodigo.eq(registro.getIdCodigo())))
				.fetchOne();
	}
	
	public Boolean existeVasilhameEntregar(VasilhameEntregar registro){
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregar = QVasilhameEntregar.vasilhameEntregar;
		
		return null!=this.query.select(qVasilhameEntregar.idCodigo)
				.from(qVasilhameEntregar)
				.where(qVasilhameEntregar.idEmpresa.eq(registro.getIdEmpresa())
				.and(qVasilhameEntregar.idFilial.eq(registro.getIdFilial())
				.and(qVasilhameEntregar.idCodigo.eq(registro.getIdCodigo()))))
				.fetchOne();
	}
	
	public Boolean validaSituacaoAberta(Integer idEmpresa, Integer idFilial, Integer idCodigo) {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregar = QVasilhameEntregar.vasilhameEntregar;
		
		return null!=this.query.select(qVasilhameEntregar.id)
			.from(qVasilhameEntregar)
			.where(qVasilhameEntregar.idEmpresa.eq(idEmpresa)
				.and(qVasilhameEntregar.idFilial.eq(idFilial))
				.and(qVasilhameEntregar.idCodigo.eq(idCodigo))
				.and(qVasilhameEntregar.situacao.ne(1)))
			.fetchOne();
	}
	
	public Boolean existeVasilhameEntregar(Integer idEmpresa, Integer idFilial, Integer idCodigo){
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregar = QVasilhameEntregar.vasilhameEntregar;
		
		return null!=this.query.select(qVasilhameEntregar.idCodigo)
				.from(qVasilhameEntregar)
				.where(qVasilhameEntregar.idEmpresa.eq(idEmpresa)
				.and(qVasilhameEntregar.idFilial.eq(idFilial)
				.and(qVasilhameEntregar.idCodigo.eq(idCodigo))))
				.fetchOne();
	}
	
	public Boolean existeItens(Integer idEmpresa, Integer idFilial, Integer idCodigo,
			Integer idItem) {
		this.query = new JPAQuery<>(this.em);
		this.qVasilhameEntregar = QVasilhameEntregar.vasilhameEntregar;
		this.qVasilhameEntregarItem = QVasilhameEntregarItem.vasilhameEntregarItem;
		
		return null!=this.query.select(qVasilhameEntregarItem.idCodigo)
				.from(qVasilhameEntregar)
				.leftJoin(qVasilhameEntregar.itens, qVasilhameEntregarItem)						
				.where(qVasilhameEntregar.idEmpresa.eq(idEmpresa)
						.and(qVasilhameEntregar.idFilial.eq(idFilial))
						.and(qVasilhameEntregar.idCodigo.eq(idCodigo))
						.and(qVasilhameEntregarItem.idCodigo.ne(idItem))).fetchOne();
	}

	
	@Override
	public List<CampoErroDTO> validar(RegistroDTO<VasilhameEntregar> dto) {
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

	private void validaEmpresa(RegistroDTO<VasilhameEntregar> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getIdEmpresa() == null) {
			erros.add(new CampoErroDTO("idEmpresa", "Campo obrigatório"));
		} else if (!this.empresaManagerBean.existeEmpresa(dto.registro.getIdEmpresa())) {
			erros.add(new CampoErroDTO("idEmpresa", "Empresa não cadastrada"));			
		}
	}
	
	private void validaFilial(RegistroDTO<VasilhameEntregar> dto, List<CampoErroDTO> erros) {	
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
	
	private void validaUsuarioFilial(RegistroDTO<VasilhameEntregar> dto, List<CampoErroDTO> erros) {
		if ((dto.registro.getIdUsuario() != "" && dto.registro.getIdUsuario() != null) && dto.registro.getIdEmpresa() != null
				&& dto.registro.getIdFilial() != null) {
			if (!this.usuarioFilialCadManagerBean.validaFilialLiberada(dto.registro.getIdUsuario(),
					dto.registro.getIdEmpresa(), dto.registro.getIdFilial())) {
				erros.add(new CampoErroDTO("idFilial", "Filial não liberada para este usuário"));
			}
		}
	}
	
	private void validaPessoa(RegistroDTO<VasilhameEntregar> dto, List<CampoErroDTO> erros) {
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

	private void validaNome(RegistroDTO<VasilhameEntregar> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getNome() == "") {
			erros.add(new CampoErroDTO("nome", "Campo obrigatório"));
		} 
	}	
	
	private void validaTipoContribuinte(RegistroDTO<VasilhameEntregar> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getTipoContribuinte() != null) {
			if (!TipoContribuinte.existe(dto.registro.getTipoContribuinte())) {
				erros.add(new CampoErroDTO("tipoContribuinte", "Tipo Contribuinte inválido"));
			}
		} else {
			erros.add(new CampoErroDTO("tipoContribuinte", "Campo obrigatório"));
		}
	}
	
	private void validaSituacao(RegistroDTO<VasilhameEntregar> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getSituacao() != null) {
			if (!Situacao.existe(dto.registro.getSituacao())) {
				erros.add(new CampoErroDTO("situacao", "Siutuação inválida"));
			} 
		} else {
			erros.add(new CampoErroDTO("situacao", "Campo obrigatório"));
		}
	}

	private void validaUsuario(RegistroDTO<VasilhameEntregar> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getIdUsuario() != null && dto.registro.getIdUsuario() != "") {
			if (!this.usuarioManagerBean.existeUsuario(dto.registro.getIdUsuario())) {
				erros.add(new CampoErroDTO("idUsuario", "Usuário não cadastrado"));
			}
		}
	}
	
	public void atualizarSituacaoVasilhame(RegistroDTO<VasilhameEntregar> dto, Integer situacao) {		
		if (dto.registro.getIdEmpresa() != null && dto.registro.getIdFilial() != null
				&& dto.registro.getIdCodigo() != null) {

			VasilhameEntregar vasilhame = this.find(dto.registro.getIdEmpresa(), dto.registro.getIdFilial(),
					dto.registro.getIdCodigo());
			vasilhame.setFilial(null);
			vasilhame.setPessoa(null);
			vasilhame.setUsuario(null);
			vasilhame.setSituacao(situacao);			

			dto.registro = this.getEm().merge(vasilhame);
		}		
	}
	
	public void mudaSituacaoVasilhame(VasilhameEntregar vasilhame) {				
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
	public Boolean verificaSituacaoVasilhame(VasilhameEntregar vasilhame) {
		Boolean fechado = true;
		
		if (vasilhame.getItens() != null) {			
			for (VasilhameEntregarItem vasilhameEntregarItem : vasilhame.getItens()) {
				if (vasilhameEntregarItem.getIdEmpresa() != null && vasilhameEntregarItem.getIdFilial() != null && vasilhameEntregarItem.getIdCodigo() != null) {
					Double quantidadeBaixada = this.vasilhameEntregarItemCadManagerBean.findQuantidadeBaixada(vasilhameEntregarItem);					
					if (quantidadeBaixada == null) quantidadeBaixada = 0.0;
					
					if (Double.compare(quantidadeBaixada, vasilhameEntregarItem.getQuantidade()) != 0) {
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
	public List<CampoErroDTO> validaDeletar(List<VasilhameEntregar> registros) {
		return new ArrayList<CampoErroDTO>();
	}
	
	@Override
	public List<CampoErroDTO> deletar(List<VasilhameEntregar> vasilhamesEntregar) throws Exception {
		List<CampoErroDTO> erros = validaDeletar(vasilhamesEntregar);
		if (erros.isEmpty()) {
			this.qVasilhameEntregar = QVasilhameEntregar.vasilhameEntregar;
			this.qVasilhameEntregarItem = QVasilhameEntregarItem.vasilhameEntregarItem;
			this.qVasilhameEntregarItemBaixa = QVasilhameEntregarItemBaixa.vasilhameEntregarItemBaixa;			
			
			for (VasilhameEntregar vasilhameEntregar : vasilhamesEntregar) {
				this.query = new JPAQuery<>(this.em);
				
				List<Integer> resultItem = query.select(qVasilhameEntregarItem.idCodigo).from(qVasilhameEntregarItem)
						.where(qVasilhameEntregarItem.idEmpresa.eq(vasilhameEntregar.getIdEmpresa())
								.and(qVasilhameEntregarItem.idFilial.eq(vasilhameEntregar.getIdFilial())
								.and(qVasilhameEntregarItem.idVasilhameEntregar.eq(vasilhameEntregar.getIdCodigo()))))
						.fetch();

				for (int i = 0; i < resultItem.size(); i++) {
					new JPADeleteClause(em, qVasilhameEntregarItemBaixa)
							.where(qVasilhameEntregarItemBaixa.idEmpresa.eq(vasilhameEntregar.getIdEmpresa())
									.and(qVasilhameEntregarItemBaixa.idFilial.eq(vasilhameEntregar.getIdFilial())
									.and(qVasilhameEntregarItemBaixa.idVasilhameEntregarItem.eq(resultItem.get(i)))))
							.execute();
				}
				
				new JPADeleteClause(em, qVasilhameEntregarItem)
						.where(qVasilhameEntregarItem.idEmpresa.eq(vasilhameEntregar.getIdEmpresa())
								.and(qVasilhameEntregarItem.idFilial.eq(vasilhameEntregar.getIdFilial())
								.and(qVasilhameEntregarItem.idVasilhameEntregar.eq(vasilhameEntregar.getIdCodigo()))))
						.execute();

				this.getEm().remove(this.getEm().find(VasilhameEntregar.class, vasilhameEntregar.getId()));
			}						
		}
		return erros;
	}

	@Override
	public EntityManager getEm() {
		return em;
	}

	@Override
	public Class<VasilhameEntregar> getTypeParameterClass() {
		return VasilhameEntregar.class;
	}

}
