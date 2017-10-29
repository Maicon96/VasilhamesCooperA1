package com.g3sistemas.vasilhames.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.g3sistemas.dto.LoadRequestDTO;
import com.g3sistemas.estoques.entity.QProduto;
import com.g3sistemas.geral.entity.QEmpresa;
import com.g3sistemas.geral.entity.QFilial;
import com.g3sistemas.geral.entity.QPessoa;
import com.g3sistemas.sistema.entity.QUsuario;
import com.g3sistemas.sql.filter.G3FiltersBuilder;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregue;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregueItem;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregueItemBaixa;
import com.g3sistemas.vasilhames.entity.VasilhameEntregueItem;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;

@Stateless
public class VasilhameEntregueItemConManagerBean {

	@PersistenceContext(unitName="g3-ds")
	private EntityManager em;
	
	private QVasilhameEntregueItem qVasilhameEntregueItem;	
	private QEmpresa qEmpresa;
	private QFilial qFilial;
	private QVasilhameEntregue qVasilhameEntregue;
	private QPessoa qPessoa;
	private QUsuario qUsuarioEntregue;
	private QProduto qProduto;
	private QUsuario qUsuario;	
	private QVasilhameEntregueItemBaixa qVasilhameEntregueItemBaixa;
	
	private JPAQuery<VasilhameEntregueItem> query;
	private PathBuilder<VasilhameEntregueItem> pathBuilder;
	private G3FiltersBuilder<VasilhameEntregueItem> filtrosBuilder;	
	
	private ConstructorExpression<VasilhameEntregueItem> projecao() {
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
	

	public long count(LoadRequestDTO filter) throws NoSuchFieldException {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregueItem = QVasilhameEntregueItem.vasilhameEntregueItem;			

		this.query.select(qVasilhameEntregueItem.countDistinct())
				.from(qVasilhameEntregueItem);		
		
		Map<String, PathBuilder<?>> joins = new HashMap<String, PathBuilder<?>>();
		
		this.pathBuilder = new PathBuilder<>(VasilhameEntregueItem.class,qVasilhameEntregueItem.toString());		
		this.filtrosBuilder = new G3FiltersBuilder<>(pathBuilder, VasilhameEntregueItem.class, joins);
		this.filtrosBuilder.applyFilters(filter, query);
		
		return this.query.fetchCount();
	}
	
	
	public List<VasilhameEntregueItem> load(LoadRequestDTO filter) throws Exception {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregueItem = QVasilhameEntregueItem.vasilhameEntregueItem;		
		this.qEmpresa = QEmpresa.empresa;
		this.qFilial = QFilial.filial;
		this.qVasilhameEntregue = QVasilhameEntregue.vasilhameEntregue;
		this.qPessoa = QPessoa.pessoa;
		this.qUsuarioEntregue = new QUsuario("usuarioEntregue");
		this.qProduto = QProduto.produto;
		this.qUsuario = QUsuario.usuario;		
		this.qVasilhameEntregueItemBaixa = QVasilhameEntregueItemBaixa.vasilhameEntregueItemBaixa;		
		
		this.query.select(this.projecao())
				.from(qVasilhameEntregueItem)
				.leftJoin(qVasilhameEntregueItem.empresa, qEmpresa)
				.leftJoin(qVasilhameEntregueItem.filial, qFilial)
				.leftJoin(qVasilhameEntregueItem.vasilhameEntregue, qVasilhameEntregue)
				.leftJoin(qVasilhameEntregue.pessoa, qPessoa)
				.leftJoin(qVasilhameEntregue.usuario, qUsuarioEntregue)
				.leftJoin(qVasilhameEntregueItem.produto, qProduto)
				.leftJoin(qVasilhameEntregueItem.usuario, qUsuario)
				.leftJoin(qVasilhameEntregueItem.vasilhameEntregueItemBaixas, qVasilhameEntregueItemBaixa)
				.groupBy(qVasilhameEntregueItem.idEmpresa, qVasilhameEntregueItem.idFilial,
					qVasilhameEntregueItem.idCodigo, qEmpresa.id, qFilial.codigo, qFilial.descricao,
					qVasilhameEntregue.idPessoa, qPessoa.idEmpresa, qPessoa.idPessoa, qVasilhameEntregue.nome,
					qVasilhameEntregue.tipoContribuinte, qVasilhameEntregue.cpfcnpj, qProduto.idEmpresa,
					qProduto.idProduto, qUsuario.nome);
		
		Map<String, PathBuilder<?>> joins = new HashMap<String, PathBuilder<?>>();
		joins.put(qVasilhameEntregueItem.empresa.toString(), new PathBuilder<>(Object.class, qEmpresa.toString()));
		joins.put(qVasilhameEntregueItem.filial.toString(), new PathBuilder<>(Object.class, qFilial.toString()));
		joins.put(qVasilhameEntregueItem.vasilhameEntregue.toString(), new PathBuilder<>(Object.class, qVasilhameEntregue.toString()));
		joins.put(qVasilhameEntregue.pessoa.toString(), new PathBuilder<>(Object.class, qPessoa.toString()));
		joins.put(qVasilhameEntregue.usuario.toString(), new PathBuilder<>(Object.class, qUsuarioEntregue.toString()));
		joins.put(qVasilhameEntregueItem.produto.toString(), new PathBuilder<>(Object.class, qProduto.toString()));
		joins.put(qVasilhameEntregueItem.usuario.toString(), new PathBuilder<>(Object.class, qUsuario.toString()));
		joins.put(qVasilhameEntregueItem.vasilhameEntregueItemBaixas.toString(), new PathBuilder<>(Object.class, qVasilhameEntregueItemBaixa.toString()));
		
		this.pathBuilder = new PathBuilder<>(VasilhameEntregueItem.class,qVasilhameEntregueItem.toString());		
		this.filtrosBuilder = new G3FiltersBuilder<>(pathBuilder, VasilhameEntregueItem.class, joins);
		this.filtrosBuilder.applyFilters(filter, query);
		this.filtrosBuilder.applyOrder(filter, pathBuilder, query);
		
		return this.query.limit(filter.limit).offset(filter.start).fetch();
	}

}
