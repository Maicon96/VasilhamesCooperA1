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
import com.g3sistemas.enums.SimNaoInt;
import com.g3sistemas.estoques.business.produtos.ProdutoCadManagerBean;
import com.g3sistemas.estoques.business.produtos.ProdutoVasilhameCadManagerBean;
import com.g3sistemas.estoques.entity.ProdutoVasilhame;
import com.g3sistemas.estoques.entity.QProduto;
import com.g3sistemas.geral.business.EmpresaCadManagerBean;
import com.g3sistemas.geral.business.FilialCadManagerBean;
import com.g3sistemas.geral.entity.QEmpresa;
import com.g3sistemas.geral.entity.QFilial;
import com.g3sistemas.geral.entity.QPessoa;
import com.g3sistemas.pdv.enums.Situacao;
import com.g3sistemas.sistema.business.UsuarioFilialCadManagerBean;
import com.g3sistemas.sistema.business.UsuarioManagerBean;
import com.g3sistemas.sistema.entity.QUsuario;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregar;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregarItem;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregarItemBaixa;
import com.g3sistemas.vasilhames.entity.VasilhameEntregar;
import com.g3sistemas.vasilhames.entity.VasilhameEntregarItem;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;

@Stateless
public class VasilhameEntregarItemCadManagerBean extends AbstractCadManagerBean<VasilhameEntregarItem> {

	@PersistenceContext(unitName = "g3-ds")
	private EntityManager em;

	@EJB
	private EmpresaCadManagerBean empresaManagerBean;

	@EJB
	private FilialCadManagerBean filialCadManagerBean;
	
	@EJB
	private VasilhameEntregarCadManagerBean vasilhameEntregarCadManagerBean;
	
	@EJB
	private ProdutoCadManagerBean produtoCadManagerBean;
	
	@EJB
	private ProdutoVasilhameCadManagerBean produtoVasilhameCadManagerBean;
	
	@EJB
	private UsuarioManagerBean usuarioManagerBean;
	
	@EJB
	private UsuarioFilialCadManagerBean usuarioFilialCadManagerBean;
	

	private QVasilhameEntregarItem qVasilhameEntregarItem;
	private QVasilhameEntregarItemBaixa qVasilhameEntregarItemBaixa;
	private QEmpresa qEmpresa;
	private QFilial qFilial;
	private QVasilhameEntregar qVasilhameEntregar;
	private QPessoa qPessoa;
	private QProduto qProduto;
	private QUsuario qUsuario;

	private JPAQuery<VasilhameEntregarItem> query;

	private ConstructorExpression<VasilhameEntregarItem> projecao() {
		return QVasilhameEntregarItem.create(qVasilhameEntregarItem.id, qVasilhameEntregarItem.idEmpresa,
				QEmpresa.create(qEmpresa.id, qEmpresa.nome), qVasilhameEntregarItem.idFilial,
				QFilial.create(qFilial.codigo, qFilial.descricao), qVasilhameEntregarItem.idCodigo,
				qVasilhameEntregarItem.idVasilhameEntregar,
				QVasilhameEntregar.create(qVasilhameEntregar.idPessoa,
						QPessoa.create(qPessoa.idEmpresa, qPessoa.idPessoa, qPessoa.digito, qPessoa.nome),
						qVasilhameEntregar.nome, qVasilhameEntregar.tipoContribuinte, qVasilhameEntregar.cpfcnpj),
				qVasilhameEntregarItem.idProduto, QProduto.create(qProduto.id, qProduto.digito, qProduto.descricao),
				qVasilhameEntregarItem.quantidade, qVasilhameEntregarItem.tipoGarrafeira,
				qVasilhameEntregarItem.situacao, qVasilhameEntregarItem.dataHoraGravacao,
				qVasilhameEntregarItem.idUsuario, QUsuario.create(qUsuario.nome),
				(NumberExpression<Double>) qVasilhameEntregarItemBaixa.quantidade.coalesce(0.0).asNumber().sum(),
					qVasilhameEntregarItem.quantidade
					.subtract((NumberExpression<Double>) qVasilhameEntregarItemBaixa.quantidade.coalesce(0.0)
							.asNumber().sum().as("quantidadeBaixar")));
	}	

	public VasilhameEntregarItem find(VasilhameEntregarItem registro) {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregarItem = QVasilhameEntregarItem.vasilhameEntregarItem;	
		this.qEmpresa = QEmpresa.empresa;
		this.qFilial = QFilial.filial;
		this.qVasilhameEntregar = QVasilhameEntregar.vasilhameEntregar;
		this.qPessoa = QPessoa.pessoa;
		this.qProduto = QProduto.produto;
		this.qUsuario = QUsuario.usuario;
		this.qVasilhameEntregarItemBaixa = QVasilhameEntregarItemBaixa.vasilhameEntregarItemBaixa;

		return this.query.select(this.projecao())
				.from(qVasilhameEntregarItem)
				.leftJoin(qVasilhameEntregarItem.empresa, qEmpresa)
				.leftJoin(qVasilhameEntregarItem.filial, qFilial)
				.leftJoin(qVasilhameEntregarItem.vasilhameEntregar, qVasilhameEntregar)
				.leftJoin(qVasilhameEntregar.pessoa, qPessoa)
				.leftJoin(qVasilhameEntregarItem.produto, qProduto)
				.leftJoin(qVasilhameEntregarItem.usuario, qUsuario)
				.leftJoin(qVasilhameEntregarItem.vasilhameEntregarItemBaixas, qVasilhameEntregarItemBaixa)
				.where(qVasilhameEntregarItem.idEmpresa.eq(registro.getIdEmpresa())
				.and(qVasilhameEntregarItem.idFilial.eq(registro.getIdFilial()))
				.and(qVasilhameEntregarItem.idCodigo.eq(registro.getIdCodigo())))
				.groupBy(qVasilhameEntregarItem.idEmpresa, qVasilhameEntregarItem.idFilial,
						qVasilhameEntregarItem.idCodigo, qEmpresa.id, qFilial.codigo, qFilial.descricao,
						qVasilhameEntregar.idPessoa, qPessoa.idEmpresa, qPessoa.idPessoa, qVasilhameEntregar.nome,
						qVasilhameEntregar.tipoContribuinte, qVasilhameEntregar.cpfcnpj, qProduto.idEmpresa,
						qProduto.idProduto, qUsuario.nome).fetchOne();
	}
	
	public Integer findSituacaoVasilhameEntregar(Integer idEmpresa, Integer idFilial, Integer idCodigo) {
		this.query = new JPAQuery<>(em);		
		this.qVasilhameEntregarItem = QVasilhameEntregarItem.vasilhameEntregarItem;
		this.qVasilhameEntregar = QVasilhameEntregar.vasilhameEntregar;

		return this.query.select(qVasilhameEntregar.situacao)
				.from(qVasilhameEntregarItem)				
				.leftJoin(qVasilhameEntregarItem.vasilhameEntregar, qVasilhameEntregar)
				.where(qVasilhameEntregarItem.idEmpresa.eq(idEmpresa)				
				.and(qVasilhameEntregarItem.idFilial.eq(idFilial))
				.and(qVasilhameEntregarItem.idCodigo.eq(idCodigo))).fetchOne();
	}	
	
	public Double findQuantidade(Integer idEmpresa, Integer idFilial, Integer idCodigo) {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregarItem = QVasilhameEntregarItem.vasilhameEntregarItem;	

		return this.query.select(qVasilhameEntregarItem.quantidade.coalesce(0.0))
				.from(qVasilhameEntregarItem)				
				.where(qVasilhameEntregarItem.idEmpresa.eq(idEmpresa)
				.and(qVasilhameEntregarItem.idFilial.eq(idFilial)
				.and(qVasilhameEntregarItem.idCodigo.eq(idCodigo)))).fetchOne();
	}
	
//	public VasilhameEntregarItem findQuantidades(VasilhameEntregarItem registro) {
//		this.query = new JPAQuery<>(em);
//		this.qVasilhameEntregarItem = QVasilhameEntregarItem.vasilhameEntregarItem;
//		this.qVasilhameEntregarItemBaixa = QVasilhameEntregarItemBaixa.vasilhameEntregarItemBaixa;
//		
//		return this.query.select(this.projecaoQuantidades())
//				.from(qVasilhameEntregarItem)				
//				.leftJoin(qVasilhameEntregarItem.vasilhameEntregarItemBaixas, qVasilhameEntregarItemBaixa)
//				.where(qVasilhameEntregarItem.idEmpresa.eq(registro.getIdEmpresa())
//						.and(qVasilhameEntregarItem.idFilial.eq(registro.getIdFilial()))
//						.and(qVasilhameEntregarItem.idCodigo.eq(registro.getIdCodigo())))
//				.groupBy(qVasilhameEntregarItem.quantidade)
//				.fetchOne();
//	}
	
	public Double findQuantidadeBaixada(VasilhameEntregarItem registro) {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregarItem = QVasilhameEntregarItem.vasilhameEntregarItem;
		this.qVasilhameEntregarItemBaixa = QVasilhameEntregarItemBaixa.vasilhameEntregarItemBaixa;
		
		return this.query.select((NumberExpression<Double>) qVasilhameEntregarItemBaixa.quantidade.coalesce(0.0).asNumber().sum())
				.from(qVasilhameEntregarItem)				
				.leftJoin(qVasilhameEntregarItem.vasilhameEntregarItemBaixas, qVasilhameEntregarItemBaixa)
				.where(qVasilhameEntregarItem.idEmpresa.eq(registro.getIdEmpresa())
						.and(qVasilhameEntregarItem.idFilial.eq(registro.getIdFilial()))
						.and(qVasilhameEntregarItem.idCodigo.eq(registro.getIdCodigo())))				
				.fetchOne();
	}
	
	public Double findQuantidadeBaixar(VasilhameEntregarItem registro) {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregarItem = QVasilhameEntregarItem.vasilhameEntregarItem;
		this.qVasilhameEntregarItemBaixa = QVasilhameEntregarItemBaixa.vasilhameEntregarItemBaixa;
		
		return this.query.select(
				qVasilhameEntregarItem.quantidade.subtract((NumberExpression<Double>) qVasilhameEntregarItemBaixa.quantidade.coalesce(0.0)
						.asNumber().sum()))
				.from(qVasilhameEntregarItem)				
				.leftJoin(qVasilhameEntregarItem.vasilhameEntregarItemBaixas, qVasilhameEntregarItemBaixa)
				.where(qVasilhameEntregarItem.idEmpresa.eq(registro.getIdEmpresa())
						.and(qVasilhameEntregarItem.idFilial.eq(registro.getIdFilial()))
						.and(qVasilhameEntregarItem.idCodigo.eq(registro.getIdCodigo())))				
				.groupBy(qVasilhameEntregarItem.quantidade)
				.fetchOne();
	}
	
	public Integer findVasilhameEntregar(Integer idEmpresa, Integer idFilial, Integer idCodigo) {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregarItem = QVasilhameEntregarItem.vasilhameEntregarItem;
		
		return this.query.select(qVasilhameEntregarItem.idVasilhameEntregar)
				.from(qVasilhameEntregarItem)		
				.where(qVasilhameEntregarItem.idEmpresa.eq(idEmpresa)
						.and(qVasilhameEntregarItem.idFilial.eq(idFilial))
						.and(qVasilhameEntregarItem.idCodigo.eq(idCodigo)))
				.fetchOne();	
	}
	
	public Boolean existeVasilhameEntregarItem(Integer idEmpresa, Integer idFilial, Integer idCodigo){
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregarItem = QVasilhameEntregarItem.vasilhameEntregarItem;	
		
		return null!=this.query.select(qVasilhameEntregarItem.idCodigo)
				.from(qVasilhameEntregarItem)
				.where(qVasilhameEntregarItem.idEmpresa.eq(idEmpresa)
				.and(qVasilhameEntregarItem.idFilial.eq(idFilial)
				.and(qVasilhameEntregarItem.idCodigo.eq(idCodigo))))
				.fetchOne();
	}
	
	public Boolean existeVasilhameEntregarItem(VasilhameEntregarItem registro){
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregarItem = QVasilhameEntregarItem.vasilhameEntregarItem;
		
		return null!=this.query.select(qVasilhameEntregarItem.idCodigo)
				.from(qVasilhameEntregarItem)
				.where(qVasilhameEntregarItem.idEmpresa.eq(registro.getIdEmpresa())
				.and(qVasilhameEntregarItem.idFilial.eq(registro.getIdFilial())
				.and(qVasilhameEntregarItem.idCodigo.eq(registro.getIdCodigo()))))
				.fetchOne();
	}
	
	public Boolean existeVasilhameEntregueItem(VasilhameEntregar registro){
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregarItem = QVasilhameEntregarItem.vasilhameEntregarItem;
		
		return null!=this.query.select(qVasilhameEntregarItem.idCodigo)
				.from(qVasilhameEntregarItem)
				.where(qVasilhameEntregarItem.idEmpresa.eq(registro.getIdEmpresa())
				.and(qVasilhameEntregarItem.idFilial.eq(registro.getIdFilial()))
				.and(qVasilhameEntregarItem.idVasilhameEntregar.eq(registro.getIdCodigo())))
				.fetchFirst();
	}
	
	public Boolean existeVasilhameEntregarItemBaixa(Integer idEmpresa, Integer idFilial, Integer idCodigo){
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregarItem = QVasilhameEntregarItem.vasilhameEntregarItem;
		this.qVasilhameEntregarItemBaixa = QVasilhameEntregarItemBaixa.vasilhameEntregarItemBaixa;
		
		return null!=this.query.select(qVasilhameEntregarItemBaixa.idCodigo)
				.from(qVasilhameEntregarItem)
				.leftJoin(qVasilhameEntregarItem.vasilhameEntregarItemBaixas, qVasilhameEntregarItemBaixa)
				.where(qVasilhameEntregarItem.idEmpresa.eq(idEmpresa)
				.and(qVasilhameEntregarItem.idFilial.eq(idFilial))
				.and(qVasilhameEntregarItem.idVasilhameEntregar.eq(idCodigo)))
				.fetchFirst();
	}
	

	@Override
	public List<CampoErroDTO> salvar(RegistroDTO<VasilhameEntregarItem> dto) throws Exception {
		final List<CampoErroDTO> erros = this.validar(dto);
		if (erros.isEmpty()) {
			this.getEm().persist(dto.registro);	
			
			VasilhameEntregar vasilhame = this.vasilhameEntregarCadManagerBean.findVasilhameEntregarItem(
					dto.registro.getIdEmpresa(), dto.registro.getIdFilial(), dto.registro.getIdVasilhameEntregar());
			if (vasilhame != null) this.vasilhameEntregarCadManagerBean.mudaSituacaoVasilhame(vasilhame);
						
		}
		return erros;
	}
	
	@Override
	public List<CampoErroDTO> atualizar(RegistroDTO<VasilhameEntregarItem> dto) throws Exception {
		final List<CampoErroDTO> erros = this.validar(dto);
		if (erros.isEmpty()) {					
			dto.registro = this.getEm().merge(dto.registro);
			VasilhameEntregar vasilhame = this.vasilhameEntregarCadManagerBean.findVasilhameEntregarItem(
					dto.registro.getIdEmpresa(), dto.registro.getIdFilial(), dto.registro.getIdVasilhameEntregar());
			if (vasilhame != null) this.vasilhameEntregarCadManagerBean.mudaSituacaoVasilhame(vasilhame);
		}
		return erros;
	}
	
	@Override
	public List<CampoErroDTO> validar(RegistroDTO<VasilhameEntregarItem> dto) {
		List<CampoErroDTO> erros = new ArrayList<CampoErroDTO>();

		this.validaEmpresa(dto, erros);
		this.validaFilial(dto, erros);
		this.validaUsuarioFilial(dto, erros);
		this.validaVasilhameEntregar(dto, erros);
		this.validaProduto(dto, erros);
		this.validaQuantidade(dto, erros);
		this.validaTipoGarrafeira(dto, erros);
		this.validaSituacao(dto, erros);
		this.validaUsuario(dto, erros);	

		return erros;
	}

	private void validaEmpresa(RegistroDTO<VasilhameEntregarItem> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getIdEmpresa() == null) {
			erros.add(new CampoErroDTO("idEmpresa", "Campo obrigatório"));
		} else if (!this.empresaManagerBean.existeEmpresa(dto.registro.getIdEmpresa())) {
			erros.add(new CampoErroDTO("idEmpresa", "Empresa não cadastrada"));			
		}
	}
	
	private void validaFilial(RegistroDTO<VasilhameEntregarItem> dto, List<CampoErroDTO> erros) {	
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
	
	private void validaUsuarioFilial(RegistroDTO<VasilhameEntregarItem> dto, List<CampoErroDTO> erros) {
		if ((dto.registro.getIdUsuario() != "" && dto.registro.getIdUsuario() != null) && dto.registro.getIdEmpresa() != null
				&& dto.registro.getIdFilial() != null) {
			if (!this.usuarioFilialCadManagerBean.validaFilialLiberada(dto.registro.getIdUsuario(),
					dto.registro.getIdEmpresa(), dto.registro.getIdFilial())) {
				erros.add(new CampoErroDTO("idFilial", "Filial não liberada para este usuário"));
			}
		}
	}
	
	private void validaVasilhameEntregar(RegistroDTO<VasilhameEntregarItem> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getIdVasilhameEntregar() != null) {		
			if (dto.registro.getIdEmpresa() != null && dto.registro.getIdFilial() != null) {			
				if (!this.vasilhameEntregarCadManagerBean.existeVasilhameEntregar(dto.registro.getIdEmpresa(), dto.registro.getIdFilial(),
						dto.registro.getIdVasilhameEntregar())) {
					erros.add(new CampoErroDTO("idVasilhameEntregar", "Vasilhame a Entregar não cadastrado"));			
				} else {				
					if (this.vasilhameEntregarCadManagerBean.validaSituacaoAberta(dto.registro.getIdEmpresa(),
							dto.registro.getIdFilial(), dto.registro.getIdVasilhameEntregar())) {
						erros.add(new CampoErroDTO("idVasilhameEntregar", "Situação do vasilhame a entregar não está aberto"));
					}	
				}
			}
		} else {
			erros.add(new CampoErroDTO("idVasilhameEntregar", "Campo obrigatório"));
		}
	}
	
	private void validaProduto(RegistroDTO<VasilhameEntregarItem> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getIdProduto() != null) {
			if (dto.registro.getIdEmpresa() != null) {
				ProdutoVasilhame produtoVasilhame = this.produtoVasilhameCadManagerBean.findVasilhame(dto.registro.getIdEmpresa(), dto.registro.getIdProduto());
				if (produtoVasilhame == null) {
					erros.add(new CampoErroDTO("idProduto", "Produto não cadastrado como vasilhame"));									
				} else if (dto.registro.getProduto() == null || dto.registro.getProduto().getDigito() == null 
						|| produtoVasilhame.getVasilhame() == null || produtoVasilhame.getVasilhame().getDigito() == null
						|| !produtoVasilhame.getVasilhame().getDigito().equals(dto.registro.getProduto().getDigito())) {					
					erros.add(new CampoErroDTO("idProduto", "Produto não cadastrado como vasilhame"));
				} 				
			}
		} else {
			erros.add(new CampoErroDTO("idProduto", "Campo obrigatório"));
		}				
	}			
	
	private void validaQuantidade(RegistroDTO<VasilhameEntregarItem> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getQuantidade() != null) {
			if (dto.registro.getQuantidade() == 0) {
				erros.add(new CampoErroDTO("quantidade", "Quantidade deve ser maior que 0"));	
			}
		} else {
			erros.add(new CampoErroDTO("quantidade", "Campo obrigatório"));
		}
	}
	
	private void validaTipoGarrafeira(RegistroDTO<VasilhameEntregarItem> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getTipoGarrafeira() != null) {
			if (!SimNaoInt.existe(dto.registro.getTipoGarrafeira())) {
				erros.add(new CampoErroDTO("tipoGarrafeira", "Tipo Garrafeira inválido"));
			}
		} else {
			erros.add(new CampoErroDTO("tipoGarrafeira", "Campo obrigatório"));
		}
	}
	
	private void validaSituacao(RegistroDTO<VasilhameEntregarItem> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getSituacao() != null) {
			if (!Situacao.existe(dto.registro.getSituacao())) {
				erros.add(new CampoErroDTO("situacao", "Situação inválida"));
			}
		} else {
			erros.add(new CampoErroDTO("situacao", "Campo obrigatório"));
		}
	}

	private void validaUsuario(RegistroDTO<VasilhameEntregarItem> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getIdUsuario() != null && dto.registro.getIdUsuario() != "") {			
			if (!this.usuarioManagerBean.existeUsuario(dto.registro.getIdUsuario())) {
				erros.add(new CampoErroDTO("idUsuario", "Usuário não cadastrado"));
			} 
		}
	}
	
	@Override
	public List<CampoErroDTO> validaDeletar(List<VasilhameEntregarItem> registros) {
		List<CampoErroDTO> erros = new ArrayList<CampoErroDTO>();
		
		for (VasilhameEntregarItem item : registros) {
			this.validaDeletarVasilhame(item, erros);
		}		

		return erros;
	}
	
	private void validaDeletarVasilhame(VasilhameEntregarItem item, List<CampoErroDTO> erros) {
		if (item != null) {
			if (item.getIdVasilhameEntregar() != null) {
				if (this.vasilhameEntregarCadManagerBean.validaSituacaoAberta(item.getIdEmpresa(),
						item.getIdFilial(), item.getIdVasilhameEntregar())) {
					erros.add(new CampoErroDTO("idVasilhameEntregar", "Situação do vasilhame a entregar não está aberto"));
				}								
			}						
		}
	}
	
	@Override
	public List<CampoErroDTO> deletar(List<VasilhameEntregarItem> vasilhameEntregarItens) throws Exception {
		List<CampoErroDTO> erros = validaDeletar(vasilhameEntregarItens);
		if (erros.isEmpty()) {
			this.qVasilhameEntregarItem = QVasilhameEntregarItem.vasilhameEntregarItem;
			this.qVasilhameEntregarItemBaixa = QVasilhameEntregarItemBaixa.vasilhameEntregarItemBaixa;			
						
			for (VasilhameEntregarItem vasilhameEntregarItem : vasilhameEntregarItens) {										
				new JPADeleteClause(em, qVasilhameEntregarItemBaixa)
					.where(qVasilhameEntregarItemBaixa.idEmpresa.eq(vasilhameEntregarItem.getIdEmpresa())
							.and(qVasilhameEntregarItemBaixa.idFilial.eq(vasilhameEntregarItem.getIdFilial())
							.and(qVasilhameEntregarItemBaixa.idVasilhameEntregarItem.eq(vasilhameEntregarItem.getIdCodigo()))))
					.execute();								
				
				this.getEm().remove(this.getEm().find(VasilhameEntregarItem.class, vasilhameEntregarItem.getId()));
				
				VasilhameEntregar vasilhameEntregar = this.vasilhameEntregarCadManagerBean.findVasilhameEntregarItem(
						vasilhameEntregarItem.getIdEmpresa(), vasilhameEntregarItem.getIdFilial(), vasilhameEntregarItem.getIdVasilhameEntregar());
				
				if (vasilhameEntregar != null) {
					this.vasilhameEntregarCadManagerBean.mudaSituacaoVasilhame(vasilhameEntregar);
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
	public Class<VasilhameEntregarItem> getTypeParameterClass() {
		return VasilhameEntregarItem.class;
	}

}
