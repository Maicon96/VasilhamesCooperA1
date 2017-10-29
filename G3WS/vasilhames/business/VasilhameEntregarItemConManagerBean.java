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
import com.g3sistemas.vasilhames.entity.QVasilhameEntregar;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregarItem;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregarItemBaixa;
import com.g3sistemas.vasilhames.entity.VasilhameEntregarItem;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;

@Stateless
public class VasilhameEntregarItemConManagerBean {

	@PersistenceContext(unitName="g3-ds")
	private EntityManager em;
	
	private QVasilhameEntregarItem qVasilhameEntregarItem;
	private QVasilhameEntregarItemBaixa qVasilhameEntregarItemBaixa;
	private QEmpresa qEmpresa;
	private QFilial qFilial;
	private QVasilhameEntregar qVasilhameEntregar;		
	private QPessoa qPessoa;
	private QUsuario qUsuarioEntregar;
	private QProduto qProduto;
	private QUsuario qUsuario;
	
	private JPAQuery<VasilhameEntregarItem> query;
	private PathBuilder<VasilhameEntregarItem> pathBuilder;
	private G3FiltersBuilder<VasilhameEntregarItem> filtrosBuilder;	
	
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
	

	public long count(LoadRequestDTO filter) throws NoSuchFieldException {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregarItem = QVasilhameEntregarItem.vasilhameEntregarItem;			

		this.query.select(qVasilhameEntregarItem.countDistinct())
				.from(qVasilhameEntregarItem);		
		
		Map<String, PathBuilder<?>> joins = new HashMap<String, PathBuilder<?>>();
		
		this.pathBuilder = new PathBuilder<>(VasilhameEntregarItem.class,qVasilhameEntregarItem.toString());		
		this.filtrosBuilder = new G3FiltersBuilder<>(pathBuilder, VasilhameEntregarItem.class, joins);
		this.filtrosBuilder.applyFilters(filter, query);
		
		return this.query.fetchCount();
	}
	
	
	public List<VasilhameEntregarItem> load(LoadRequestDTO filter) throws Exception {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregarItem = QVasilhameEntregarItem.vasilhameEntregarItem;
		this.qVasilhameEntregarItemBaixa = QVasilhameEntregarItemBaixa.vasilhameEntregarItemBaixa;
		this.qEmpresa = QEmpresa.empresa;
		this.qFilial = QFilial.filial;
		this.qVasilhameEntregar = QVasilhameEntregar.vasilhameEntregar;		
		this.qPessoa = QPessoa.pessoa;
		this.qUsuarioEntregar = new QUsuario("usuarioEntregar");		
		this.qProduto = QProduto.produto;
		this.qUsuario = QUsuario.usuario;
		
		this.query.select(this.projecao())
				.from(qVasilhameEntregarItem)
				.leftJoin(qVasilhameEntregarItem.vasilhameEntregarItemBaixas, qVasilhameEntregarItemBaixa)
				.leftJoin(qVasilhameEntregarItem.empresa, qEmpresa)
				.leftJoin(qVasilhameEntregarItem.filial, qFilial)
				.leftJoin(qVasilhameEntregarItem.vasilhameEntregar, qVasilhameEntregar)
				.leftJoin(qVasilhameEntregar.pessoa, qPessoa)
				.leftJoin(qVasilhameEntregar.usuario, qUsuarioEntregar)
				.leftJoin(qVasilhameEntregarItem.produto, qProduto)
				.leftJoin(qVasilhameEntregarItem.usuario, qUsuario)
				.groupBy(qVasilhameEntregarItem.idEmpresa, qVasilhameEntregarItem.idFilial,
						qVasilhameEntregarItem.idCodigo, qEmpresa.id, qFilial.codigo, qFilial.descricao,
						qVasilhameEntregar.idPessoa, qPessoa.idEmpresa, qPessoa.idPessoa, qVasilhameEntregar.nome,
						qVasilhameEntregar.tipoContribuinte, qVasilhameEntregar.cpfcnpj, qProduto.idEmpresa,
						qProduto.idProduto, qUsuario.nome);
		
		Map<String, PathBuilder<?>> joins = new HashMap<String, PathBuilder<?>>();
		joins.put(qVasilhameEntregarItem.vasilhameEntregarItemBaixas.toString(), new PathBuilder<>(Object.class, qVasilhameEntregarItemBaixa.toString()));
		joins.put(qVasilhameEntregarItem.empresa.toString(), new PathBuilder<>(Object.class, qEmpresa.toString()));
		joins.put(qVasilhameEntregarItem.filial.toString(), new PathBuilder<>(Object.class, qFilial.toString()));
		joins.put(qVasilhameEntregarItem.vasilhameEntregar.toString(), new PathBuilder<>(Object.class, qVasilhameEntregar.toString()));
		joins.put(qVasilhameEntregar.pessoa.toString(), new PathBuilder<>(Object.class, qPessoa.toString()));
		joins.put(qVasilhameEntregar.usuario.toString(), new PathBuilder<>(Object.class, qUsuarioEntregar.toString()));
		joins.put(qVasilhameEntregarItem.produto.toString(), new PathBuilder<>(Object.class, qProduto.toString()));
		joins.put(qVasilhameEntregarItem.usuario.toString(), new PathBuilder<>(Object.class, qUsuario.toString()));
		
		this.pathBuilder = new PathBuilder<>(VasilhameEntregarItem.class,qVasilhameEntregarItem.toString());		
		this.filtrosBuilder = new G3FiltersBuilder<>(pathBuilder, VasilhameEntregarItem.class, joins);
		this.filtrosBuilder.applyFilters(filter, query);
		this.filtrosBuilder.applyOrder(filter, pathBuilder, query);
		
		return this.query.limit(filter.limit).offset(filter.start).fetch();
	}

}
