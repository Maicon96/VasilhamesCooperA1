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
import com.g3sistemas.vasilhames.entity.QVasilhameEntregue;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregueItem;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregueItemBaixa;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregueItemBaixaCupom;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregueItemBaixaNota;
import com.g3sistemas.vasilhames.entity.VasilhameEntregue;
import com.g3sistemas.vasilhames.entity.VasilhameEntregueItem;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;

@Stateless
public class VasilhameEntregueItemCadManagerBean extends AbstractCadManagerBean<VasilhameEntregueItem> {

	@PersistenceContext(unitName = "g3-ds")
	private EntityManager em;

	@EJB
	private EmpresaCadManagerBean empresaManagerBean;

	@EJB
	private FilialCadManagerBean filialCadManagerBean;
	
	@EJB
	private VasilhameEntregueCadManagerBean vasilhameEntregueCadManagerBean;
	
	@EJB
	private ProdutoCadManagerBean produtoCadManagerBean;
	
	@EJB
	private ProdutoVasilhameCadManagerBean produtoVasilhameCadManagerBean;
	
	@EJB
	private UsuarioManagerBean usuarioManagerBean;
	
	@EJB
	private UsuarioFilialCadManagerBean usuarioFilialCadManagerBean;
	

	private QVasilhameEntregueItem qVasilhameEntregueItem;	
	private QEmpresa qEmpresa;
	private QFilial qFilial;
	private QVasilhameEntregue qVasilhameEntregue;
	private QPessoa qPessoa;
	private QProduto qProduto;
	private QUsuario qUsuario;
	private QVasilhameEntregueItemBaixa qVasilhameEntregueItemBaixa;
	private QVasilhameEntregueItemBaixaCupom qVasilhameEntregueItemBaixaCupom;
	private QVasilhameEntregueItemBaixaNota qVasilhameEntregueItemBaixaNota;

	private JPAQuery<VasilhameEntregueItem> query;

	private ConstructorExpression<VasilhameEntregueItem> projecaoItem() {
		return QVasilhameEntregueItem.create(qVasilhameEntregueItem.id, qVasilhameEntregueItem.idEmpresa,
				QEmpresa.create(qEmpresa.id, qEmpresa.nome), qVasilhameEntregueItem.idFilial,
				QFilial.create(qFilial.codigo, qFilial.descricao), qVasilhameEntregueItem.idCodigo,
				qVasilhameEntregueItem.idVasilhameEntregue,
				QVasilhameEntregue.create(qVasilhameEntregue.idPessoa,
						QPessoa.create(qPessoa.idEmpresa, qPessoa.idPessoa, qPessoa.digito, qPessoa.nome),
						qVasilhameEntregue.nome, qVasilhameEntregue.tipoContribuinte, qVasilhameEntregue.cpfcnpj),
				qVasilhameEntregueItem.idProduto, QProduto.create(qProduto.id, qProduto.digito, qProduto.descricao),
				qVasilhameEntregueItem.quantidade, qVasilhameEntregueItem.tipoGarrafeira,
				qVasilhameEntregueItem.situacao, qVasilhameEntregueItem.dataHoraGravacao,
				qVasilhameEntregueItem.idUsuario, QUsuario.create(qUsuario.nome),
				(NumberExpression<Double>) qVasilhameEntregueItemBaixa.quantidade.coalesce(0.0).asNumber().sum(),
				qVasilhameEntregueItem.quantidade
				.subtract((NumberExpression<Double>) qVasilhameEntregueItemBaixa.quantidade.coalesce(0.0)
						.asNumber().sum().as("quantidadeBaixar")));
	}
			 
	private ConstructorExpression<VasilhameEntregueItem> projecaoProduto() {
		return QVasilhameEntregueItem.create(qVasilhameEntregueItem.idProduto,
				QProduto.create(qProduto.id, qProduto.digito, qProduto.descricao), qVasilhameEntregueItem.quantidade);
	}

	public VasilhameEntregueItem find(VasilhameEntregueItem registro) {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregueItem = QVasilhameEntregueItem.vasilhameEntregueItem;		
		this.qEmpresa = QEmpresa.empresa;
		this.qFilial = QFilial.filial;
		this.qVasilhameEntregue = QVasilhameEntregue.vasilhameEntregue;
		this.qPessoa = QPessoa.pessoa;
		this.qProduto = QProduto.produto;
		this.qUsuario = QUsuario.usuario;
		this.qVasilhameEntregueItemBaixa = QVasilhameEntregueItemBaixa.vasilhameEntregueItemBaixa;
		
		return this.query.select(this.projecaoItem())
				.from(qVasilhameEntregueItem)		
				.leftJoin(qVasilhameEntregueItem.empresa, qEmpresa)
				.leftJoin(qVasilhameEntregueItem.filial, qFilial)
				.leftJoin(qVasilhameEntregueItem.vasilhameEntregue, qVasilhameEntregue)
				.leftJoin(qVasilhameEntregue.pessoa, qPessoa)
				.leftJoin(qVasilhameEntregueItem.produto, qProduto)
				.leftJoin(qVasilhameEntregueItem.usuario, qUsuario)
				.leftJoin(qVasilhameEntregueItem.vasilhameEntregueItemBaixas, qVasilhameEntregueItemBaixa)
				.where(qVasilhameEntregueItem.idEmpresa.eq(registro.getIdEmpresa())
						.and(qVasilhameEntregueItem.idFilial.eq(registro.getIdFilial()))
						.and(qVasilhameEntregueItem.idCodigo.eq(registro.getIdCodigo())))				
				.groupBy(qVasilhameEntregueItem.idEmpresa, qVasilhameEntregueItem.idFilial,
						qVasilhameEntregueItem.idCodigo, qEmpresa.id, qFilial.codigo, qFilial.descricao,
						qVasilhameEntregue.idPessoa, qPessoa.idEmpresa, qPessoa.idPessoa, qVasilhameEntregue.nome,
						qVasilhameEntregue.tipoContribuinte, qVasilhameEntregue.cpfcnpj, qProduto.idEmpresa,
						qProduto.idProduto, qUsuario.nome)
				.fetchOne();
	}
	
	public Integer findVasilhameEntregue(Integer idEmpresa, Integer idFilial, Integer idCodigo) {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregueItem = QVasilhameEntregueItem.vasilhameEntregueItem;
		
		return this.query.select(qVasilhameEntregueItem.idVasilhameEntregue)
				.from(qVasilhameEntregueItem)		
				.where(qVasilhameEntregueItem.idEmpresa.eq(idEmpresa)
						.and(qVasilhameEntregueItem.idFilial.eq(idFilial))
						.and(qVasilhameEntregueItem.idCodigo.eq(idCodigo)))
				.fetchOne();	
	}

	public Double findQuantidadeBaixar(VasilhameEntregueItem registro) {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregueItem = QVasilhameEntregueItem.vasilhameEntregueItem;
		this.qVasilhameEntregueItemBaixa = QVasilhameEntregueItemBaixa.vasilhameEntregueItemBaixa;
		
		return this.query.select(
				qVasilhameEntregueItem.quantidade.subtract((NumberExpression<Double>) qVasilhameEntregueItemBaixa.quantidade.coalesce(0.0)
						.asNumber().sum()))
				.from(qVasilhameEntregueItem)				
				.leftJoin(qVasilhameEntregueItem.vasilhameEntregueItemBaixas, qVasilhameEntregueItemBaixa)
				.where(qVasilhameEntregueItem.idEmpresa.eq(registro.getIdEmpresa())
						.and(qVasilhameEntregueItem.idFilial.eq(registro.getIdFilial()))
						.and(qVasilhameEntregueItem.idCodigo.eq(registro.getIdCodigo())))				
				.groupBy(qVasilhameEntregueItem.quantidade)
				.fetchOne();
	}
	
	
	public Double findQuantidadeBaixada(VasilhameEntregueItem registro) {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregueItem = QVasilhameEntregueItem.vasilhameEntregueItem;
		this.qVasilhameEntregueItemBaixa = QVasilhameEntregueItemBaixa.vasilhameEntregueItemBaixa;
		
		return this.query.select((NumberExpression<Double>) qVasilhameEntregueItemBaixa.quantidade.coalesce(0.0).asNumber().sum())
				.from(qVasilhameEntregueItem)				
				.leftJoin(qVasilhameEntregueItem.vasilhameEntregueItemBaixas, qVasilhameEntregueItemBaixa)
				.where(qVasilhameEntregueItem.idEmpresa.eq(registro.getIdEmpresa())
						.and(qVasilhameEntregueItem.idFilial.eq(registro.getIdFilial()))
						.and(qVasilhameEntregueItem.idCodigo.eq(registro.getIdCodigo())))				
				.fetchOne();
	}
	
	public VasilhameEntregueItem findProduto(VasilhameEntregueItem registro) {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregueItem = QVasilhameEntregueItem.vasilhameEntregueItem;		
		this.qProduto = QProduto.produto;
		
		return this.query.select(this.projecaoProduto())
				.from(qVasilhameEntregueItem)
				.leftJoin(qVasilhameEntregueItem.produto, qProduto)				
				.where(qVasilhameEntregueItem.idEmpresa.eq(registro.getIdEmpresa())
				.and(qVasilhameEntregueItem.idFilial.eq(registro.getIdFilial())
				.and(qVasilhameEntregueItem.idCodigo.eq(registro.getIdCodigo())))).fetchOne();
	}
	
	public Double findQuantidade(Integer idEmpresa, Integer idFilial, Integer idCodigo) {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregueItem = QVasilhameEntregueItem.vasilhameEntregueItem;	

		return this.query.select(qVasilhameEntregueItem.quantidade.coalesce(0.0))
				.from(qVasilhameEntregueItem)				
				.where(qVasilhameEntregueItem.idEmpresa.eq(idEmpresa)
				.and(qVasilhameEntregueItem.idFilial.eq(idFilial)
				.and(qVasilhameEntregueItem.idCodigo.eq(idCodigo))))
				.fetchOne();
	}
	
	public Integer findSituacaoVasilhameEntregue(Integer idEmpresa, Integer idFilial, Integer idCodigo) {
		this.query = new JPAQuery<>(em);		
		this.qVasilhameEntregueItem = QVasilhameEntregueItem.vasilhameEntregueItem;
		this.qVasilhameEntregue = QVasilhameEntregue.vasilhameEntregue;

		return this.query.select(qVasilhameEntregue.situacao)
				.from(qVasilhameEntregueItem)				
				.leftJoin(qVasilhameEntregueItem.vasilhameEntregue, qVasilhameEntregue)
				.where(qVasilhameEntregueItem.idEmpresa.eq(idEmpresa)				
				.and(qVasilhameEntregueItem.idFilial.eq(idFilial))
				.and(qVasilhameEntregueItem.idCodigo.eq(idCodigo)))
				.fetchOne();
	}
	
	public Boolean existeVasilhameEntregueItem(Integer idEmpresa, Integer idFilial, Integer idCodigo){
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregueItem = QVasilhameEntregueItem.vasilhameEntregueItem;	
		
		return null!=this.query.select(qVasilhameEntregueItem.idCodigo)
				.from(qVasilhameEntregueItem)
				.where(qVasilhameEntregueItem.idEmpresa.eq(idEmpresa)
				.and(qVasilhameEntregueItem.idFilial.eq(idFilial))
				.and(qVasilhameEntregueItem.idCodigo.eq(idCodigo)))
				.fetchOne();
	}
	
	public Boolean existeVasilhameEntregueItem(VasilhameEntregue registro){
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregueItem = QVasilhameEntregueItem.vasilhameEntregueItem;
		
		return null!=this.query.select(qVasilhameEntregueItem.idCodigo)
				.from(qVasilhameEntregueItem)
				.where(qVasilhameEntregueItem.idEmpresa.eq(registro.getIdEmpresa())
				.and(qVasilhameEntregueItem.idFilial.eq(registro.getIdFilial()))
				.and(qVasilhameEntregueItem.idVasilhameEntregue.eq(registro.getIdCodigo())))
				.fetchFirst();
	}
	
	
	public Boolean existeVasilhameEntregueItem(VasilhameEntregueItem registro){
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregueItem = QVasilhameEntregueItem.vasilhameEntregueItem;
		
		return null!=this.query.select(qVasilhameEntregueItem.idCodigo)
				.from(qVasilhameEntregueItem)
				.where(qVasilhameEntregueItem.idEmpresa.eq(registro.getIdEmpresa())
				.and(qVasilhameEntregueItem.idFilial.eq(registro.getIdFilial()))
				.and(qVasilhameEntregueItem.idCodigo.eq(registro.getIdCodigo())))
				.fetchOne();
	}
	
	public Boolean existeVasilhameEntregueItemBaixa(Integer idEmpresa, Integer idFilial, Integer idCodigo){
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregueItem = QVasilhameEntregueItem.vasilhameEntregueItem;
		this.qVasilhameEntregueItemBaixa = QVasilhameEntregueItemBaixa.vasilhameEntregueItemBaixa;
		
		return null!=this.query.select(qVasilhameEntregueItemBaixa.idCodigo)
				.from(qVasilhameEntregueItem)
				.leftJoin(qVasilhameEntregueItem.vasilhameEntregueItemBaixas, qVasilhameEntregueItemBaixa)
				.where(qVasilhameEntregueItem.idEmpresa.eq(idEmpresa)
				.and(qVasilhameEntregueItem.idFilial.eq(idFilial))
				.and(qVasilhameEntregueItem.idVasilhameEntregue.eq(idCodigo)))
				.fetchFirst();
	}
	
	@Override
	public List<CampoErroDTO> salvar(RegistroDTO<VasilhameEntregueItem> dto) throws Exception {
		final List<CampoErroDTO> erros = this.validar(dto);
		if (erros.isEmpty()) {
			this.getEm().persist(dto.registro);	
			
			VasilhameEntregue vasilhame = this.vasilhameEntregueCadManagerBean.findVasilhameEntregueItem(
					dto.registro.getIdEmpresa(), dto.registro.getIdFilial(), dto.registro.getIdVasilhameEntregue());
			if (vasilhame != null) this.vasilhameEntregueCadManagerBean.mudaSituacaoVasilhame(vasilhame);
						
		}
		return erros;
	}
	
	@Override
	public List<CampoErroDTO> atualizar(RegistroDTO<VasilhameEntregueItem> dto) throws Exception {
		final List<CampoErroDTO> erros = this.validar(dto);
		if (erros.isEmpty()) {					
			dto.registro = this.getEm().merge(dto.registro);
			VasilhameEntregue vasilhame = this.vasilhameEntregueCadManagerBean.findVasilhameEntregueItem(
					dto.registro.getIdEmpresa(), dto.registro.getIdFilial(), dto.registro.getIdVasilhameEntregue());
			if (vasilhame != null) this.vasilhameEntregueCadManagerBean.mudaSituacaoVasilhame(vasilhame);
		}
		return erros;
	}
	
	@Override
	public List<CampoErroDTO> validar(RegistroDTO<VasilhameEntregueItem> dto) {
		List<CampoErroDTO> erros = new ArrayList<CampoErroDTO>();

		this.validaEmpresa(dto, erros);
		this.validaFilial(dto, erros);
		this.validaUsuarioFilial(dto, erros);
		this.validaVasilhameEntregue(dto, erros);
		this.validaProduto(dto, erros);
		this.validaQuantidade(dto, erros);
		this.validaTipoGarrafeira(dto, erros);
		this.validaSituacao(dto, erros);
		this.validaUsuario(dto, erros);

		return erros;
	}

	private void validaEmpresa(RegistroDTO<VasilhameEntregueItem> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getIdEmpresa() == null) {
			erros.add(new CampoErroDTO("idEmpresa", "Campo obrigatório"));
		} else if (!this.empresaManagerBean.existeEmpresa(dto.registro.getIdEmpresa())) {
			erros.add(new CampoErroDTO("idEmpresa", "Empresa não cadastrada"));			
		}
	}
	
	private void validaFilial(RegistroDTO<VasilhameEntregueItem> dto, List<CampoErroDTO> erros) {	
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
	
	private void validaUsuarioFilial(RegistroDTO<VasilhameEntregueItem> dto, List<CampoErroDTO> erros) {
		if ((dto.registro.getIdUsuario() != "" && dto.registro.getIdUsuario() != null) && dto.registro.getIdEmpresa() != null
				&& dto.registro.getIdFilial() != null) {
			if (!this.usuarioFilialCadManagerBean.validaFilialLiberada(dto.registro.getIdUsuario(),
					dto.registro.getIdEmpresa(), dto.registro.getIdFilial())) {
				erros.add(new CampoErroDTO("idFilial", "Filial não liberada para este usuário"));
			}
		}
	}
	
	private void validaVasilhameEntregue(RegistroDTO<VasilhameEntregueItem> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getIdVasilhameEntregue() != null) {
			if (dto.registro.getIdEmpresa() != null && dto.registro.getIdFilial() != null ) {			
				if (!this.vasilhameEntregueCadManagerBean.existeVasilhameEntregue(dto.registro.getIdEmpresa(), dto.registro.getIdFilial(),
						dto.registro.getIdVasilhameEntregue())) {
					erros.add(new CampoErroDTO("idVasilhameEntregue", "Vasilhame Entregue não cadastrado"));			
				} else {							
					
					if (this.vasilhameEntregueCadManagerBean.validaSituacaoAberta(dto.registro.getIdEmpresa(),
							dto.registro.getIdFilial(), dto.registro.getIdVasilhameEntregue())) {
						erros.add(new CampoErroDTO("idVasilhameEntregue", "Situação do vasilhame entregue não está aberto"));
					}										
				}
			}
		} else {
			erros.add(new CampoErroDTO("idVasilhameEntregue", "Campo obrigatório"));
		}
	}
	
	private void validaProduto(RegistroDTO<VasilhameEntregueItem> dto, List<CampoErroDTO> erros) {
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
	
	private void validaQuantidade(RegistroDTO<VasilhameEntregueItem> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getQuantidade() != null) {
			if (dto.registro.getQuantidade() == 0) {
				erros.add(new CampoErroDTO("quantidade", "Quantidade deve ser maior que 0"));	
			}
		} else {
			erros.add(new CampoErroDTO("quantidade", "Campo obrigatório"));
		}
	}
	
	private void validaTipoGarrafeira(RegistroDTO<VasilhameEntregueItem> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getTipoGarrafeira() != null) {
			if (!SimNaoInt.existe(dto.registro.getTipoGarrafeira())) {
				erros.add(new CampoErroDTO("tipoGarrafeira", "Tipo Garrafeira inválido"));
			}
		} else {
			erros.add(new CampoErroDTO("tipoGarrafeira", "Campo obrigatório"));
		}
	}
	
	private void validaSituacao(RegistroDTO<VasilhameEntregueItem> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getSituacao() != null) {
			if (!Situacao.existe(dto.registro.getSituacao())) {
				erros.add(new CampoErroDTO("situacao", "Situação inválida"));
			}
		} else {
			erros.add(new CampoErroDTO("situacao", "Campo obrigatório"));
		}
	}

	private void validaUsuario(RegistroDTO<VasilhameEntregueItem> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getIdUsuario() != null && dto.registro.getIdUsuario() != "") {			
			if (!this.usuarioManagerBean.existeUsuario(dto.registro.getIdUsuario())) {
				erros.add(new CampoErroDTO("idUsuario", "Usuário não cadastrado"));
			} 
		}
	}
	
	@Override
	public List<CampoErroDTO> validaDeletar(List<VasilhameEntregueItem> registros) {
		List<CampoErroDTO> erros = new ArrayList<CampoErroDTO>();
		
		for (VasilhameEntregueItem item : registros) {
			this.validaDeletarVasilhame(item, erros);
		}		

		return erros;
	}
	
	private void validaDeletarVasilhame(VasilhameEntregueItem item, List<CampoErroDTO> erros) {
		if (item != null) {			
			if (item.getIdVasilhameEntregue() != null) {
				if (this.vasilhameEntregueCadManagerBean.validaSituacaoAberta(item.getIdEmpresa(),
						item.getIdFilial(), item.getIdVasilhameEntregue())) {
					erros.add(new CampoErroDTO("idVasilhameEntregue", "Situação do vasilhame entregue não está aberto"));
				}								
			}							
			 
		}
	}
	
	@Override
	public List<CampoErroDTO> deletar(List<VasilhameEntregueItem> vasilhameEntregueItens) throws Exception {
		List<CampoErroDTO> erros = validaDeletar(vasilhameEntregueItens);
		if (erros.isEmpty()) {
			this.qVasilhameEntregueItem = QVasilhameEntregueItem.vasilhameEntregueItem;
			this.qVasilhameEntregueItemBaixa = QVasilhameEntregueItemBaixa.vasilhameEntregueItemBaixa;						
//			this.qVasilhameEntregueItemBaixaCupom = QVasilhameEntregueItemBaixaCupom.vasilhameEntregueItemBaixaCupom;
//			this.qVasilhameEntregueItemBaixaNota = QVasilhameEntregueItemBaixaNota.vasilhameEntregueItemBaixaNota;
						
			for (VasilhameEntregueItem vasilhameEntregueItem : vasilhameEntregueItens) {
				this.query = new JPAQuery<>(em);
				
//				List<Integer> resultBaixa = query.select(qVasilhameEntregueItemBaixa.idCodigo)
//						.from(qVasilhameEntregueItemBaixa)
//						.where(qVasilhameEntregueItemBaixa.idEmpresa.eq(vasilhameEntregueItem.getIdEmpresa())
//								.and(qVasilhameEntregueItemBaixa.idFilial.eq(vasilhameEntregueItem.getIdFilial())
//								.and(qVasilhameEntregueItemBaixa.idVasilhameEntregueItem.eq(vasilhameEntregueItem.getIdCodigo()))))
//								.fetch();
//
//				for (int j = 0; j < resultBaixa.size(); j++) {
//					new JPADeleteClause(em, qVasilhameEntregueItemBaixaCupom)
//						.where(qVasilhameEntregueItemBaixaCupom.idEmpresaBaixa.eq(vasilhameEntregueItem.getIdEmpresa())
//								.and(qVasilhameEntregueItemBaixaCupom.idFilialBaixa.eq(vasilhameEntregueItem.getIdFilial())
//								.and(qVasilhameEntregueItemBaixaCupom.idVasilhameEntregueItemBaixa.eq(resultBaixa.get(j)))))
//							.execute();
//
//					new JPADeleteClause(em, qVasilhameEntregueItemBaixaNota)
//						.where(qVasilhameEntregueItemBaixaNota.idEmpresaBaixa.eq(vasilhameEntregueItem.getIdEmpresa())
//								.and(qVasilhameEntregueItemBaixaNota.idFilialBaixa.eq(vasilhameEntregueItem.getIdFilial())
//								.and(qVasilhameEntregueItemBaixaNota.idVasilhameEntregueItemBaixa.eq(resultBaixa.get(j)))))										
//							.execute();
//				}
				
				new JPADeleteClause(em, qVasilhameEntregueItemBaixa)
					.where(qVasilhameEntregueItemBaixa.idEmpresa.eq(vasilhameEntregueItem.getIdEmpresa())
							.and(qVasilhameEntregueItemBaixa.idFilial.eq(vasilhameEntregueItem.getIdFilial())
							.and(qVasilhameEntregueItemBaixa.idVasilhameEntregueItem.eq(vasilhameEntregueItem.getIdCodigo()))))
					.execute();								
				
				this.getEm().remove(this.getEm().find(VasilhameEntregueItem.class, vasilhameEntregueItem.getId()));
			
				VasilhameEntregue vasilhameEntregue = this.vasilhameEntregueCadManagerBean.findVasilhameEntregueItem(
						vasilhameEntregueItem.getIdEmpresa(), vasilhameEntregueItem.getIdFilial(), vasilhameEntregueItem.getIdVasilhameEntregue());
				
				if (vasilhameEntregue != null) {
					this.vasilhameEntregueCadManagerBean.mudaSituacaoVasilhame(vasilhameEntregue);
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
	public Class<VasilhameEntregueItem> getTypeParameterClass() {
		return VasilhameEntregueItem.class;
	}

}
