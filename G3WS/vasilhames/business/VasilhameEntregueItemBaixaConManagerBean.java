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
import com.g3sistemas.vasilhames.entity.VasilhameEntregueItemBaixa;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;

@Stateless
public class VasilhameEntregueItemBaixaConManagerBean {

	@PersistenceContext(unitName="g3-ds")
	private EntityManager em;
	
	private QVasilhameEntregueItemBaixa qVasilhameEntregueItemBaixa;
	private QEmpresa qEmpresa;
	private QFilial qFilial;
	private QVasilhameEntregueItem qVasilhameEntregueItem;
	private QVasilhameEntregue qVasilhameEntregue;	
	private QPessoa qPessoa;	
	private QUsuario qUsuarioEntregue;
	private QProduto qProduto;		
	private QUsuario qUsuario;
	
	private JPAQuery<VasilhameEntregueItemBaixa> query;
	private PathBuilder<VasilhameEntregueItemBaixa> pathBuilder;
	private G3FiltersBuilder<VasilhameEntregueItemBaixa> filtrosBuilder;	
	
	private ConstructorExpression<VasilhameEntregueItemBaixa> projecao() {
		return QVasilhameEntregueItemBaixa.create(qVasilhameEntregueItemBaixa.id, qVasilhameEntregueItemBaixa.idEmpresa,
				QEmpresa.create(qEmpresa.id, qEmpresa.nome), qVasilhameEntregueItemBaixa.idFilial,
				QFilial.create(qFilial.codigo, qFilial.descricao), qVasilhameEntregueItemBaixa.idCodigo,
				qVasilhameEntregueItemBaixa.idVasilhameEntregueItem,
				QVasilhameEntregueItem.create(qVasilhameEntregueItem.idVasilhameEntregue,
						QVasilhameEntregue.create(qVasilhameEntregue.idCodigo, qVasilhameEntregue.idPessoa,
								QPessoa.create(qPessoa.idEmpresa, qPessoa.idPessoa, qPessoa.digito, qPessoa.nome),
								qVasilhameEntregue.nome, qVasilhameEntregue.tipoContribuinte,
								qVasilhameEntregue.cpfcnpj, qVasilhameEntregue.situacao, qVasilhameEntregue.idUsuario,
								QUsuario.create(qUsuarioEntregue.nome)),
						qVasilhameEntregueItem.idProduto,
						QProduto.create(qProduto.id, qProduto.digito, qProduto.descricao),
						qVasilhameEntregueItem.quantidade, qVasilhameEntregueItem.tipoGarrafeira, qVasilhameEntregueItem.situacao),
				qVasilhameEntregueItemBaixa.tipoBaixa, qVasilhameEntregueItemBaixa.quantidade,
				qVasilhameEntregueItemBaixa.dataHoraGravacao, qVasilhameEntregueItemBaixa.idUsuario,
				QUsuario.create(qUsuario.nome));
	}
	
//	private ConstructorExpression<VasilhameEntregueItemBaixa> projecaoLoad() {
//		return QVasilhameEntregueItemBaixa.create(qVasilhameEntregueItemBaixa.id, qVasilhameEntregueItemBaixa.idEmpresa,
//				QEmpresa.create(qEmpresa.id, qEmpresa.nome), qVasilhameEntregueItemBaixa.idFilial,
//				QFilial.create(qFilial.codigo, qFilial.descricao), qVasilhameEntregueItemBaixa.idCodigo,
//				qVasilhameEntregueItemBaixa.idVasilhameEntregueItem,
//				QVasilhameEntregueItem.create(qVasilhameEntregueItem.idVasilhameEntregue,
//						QVasilhameEntregue.create(qVasilhameEntregue.idCodigo, qVasilhameEntregue.idPessoa,
//								QPessoa.create(qPessoa.idEmpresa, qPessoa.idPessoa, qPessoa.digito, qPessoa.nome),
//								qVasilhameEntregue.nome, qVasilhameEntregue.tipoContribuinte,
//								qVasilhameEntregue.cpfcnpj, qVasilhameEntregue.situacao, qVasilhameEntregue.idUsuario,
//								QUsuario.create(qUsuarioEntregue.nome)),
//						qVasilhameEntregueItem.idProduto,
//						QProduto.create(qProduto.id, qProduto.digito, qProduto.descricao),
//						qVasilhameEntregueItem.quantidade, 
//						(NumberExpression<Double>) qVasilhameEntregueItemBaixa.quantidade.coalesce(0.0)
//						.asNumber().sum().as("quantidadeBaixada"),						 
//						qVasilhameEntregueItem.tipoGarrafeira, qVasilhameEntregueItem.situacao),
//				qVasilhameEntregueItemBaixa.tipoBaixa, qVasilhameEntregueItemBaixa.quantidade,
//				qVasilhameEntregueItemBaixa.dataHoraGravacao, qVasilhameEntregueItemBaixa.idUsuario,
//				QUsuario.create(qUsuario.nome));
//	}

	public long count(LoadRequestDTO filter) throws NoSuchFieldException {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregueItemBaixa = QVasilhameEntregueItemBaixa.vasilhameEntregueItemBaixa;			

		this.query.select(qVasilhameEntregueItemBaixa.countDistinct())
				.from(qVasilhameEntregueItemBaixa);		
		
		Map<String, PathBuilder<?>> joins = new HashMap<String, PathBuilder<?>>();
		
		this.pathBuilder = new PathBuilder<>(VasilhameEntregueItemBaixa.class,qVasilhameEntregueItemBaixa.toString());		
		this.filtrosBuilder = new G3FiltersBuilder<>(pathBuilder, VasilhameEntregueItemBaixa.class, joins);
		this.filtrosBuilder.applyFilters(filter, query);
		
		return this.query.fetchCount();
	}
	
	
	public List<VasilhameEntregueItemBaixa> load(LoadRequestDTO filter) throws Exception {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregueItemBaixa = QVasilhameEntregueItemBaixa.vasilhameEntregueItemBaixa;	
		this.qEmpresa = QEmpresa.empresa;
		this.qFilial = QFilial.filial;
		this.qVasilhameEntregueItem = QVasilhameEntregueItem.vasilhameEntregueItem;
		this.qVasilhameEntregue = QVasilhameEntregue.vasilhameEntregue;		
		this.qPessoa = QPessoa.pessoa;
		this.qUsuarioEntregue = new QUsuario("usuarioEntregue");
		this.qProduto = QProduto.produto;
		this.qUsuario = QUsuario.usuario;
				
		this.query.select(this.projecao())
				.from(qVasilhameEntregueItemBaixa)
				.leftJoin(qVasilhameEntregueItemBaixa.empresa, qEmpresa)
				.leftJoin(qVasilhameEntregueItemBaixa.filial, qFilial)
				.leftJoin(qVasilhameEntregueItemBaixa.vasilhameEntregueItem, qVasilhameEntregueItem)
				.leftJoin(qVasilhameEntregueItem.vasilhameEntregue, qVasilhameEntregue)
				.leftJoin(qVasilhameEntregue.pessoa, qPessoa)
				.leftJoin(qVasilhameEntregue.usuario, qUsuarioEntregue)
				.leftJoin(qVasilhameEntregueItem.produto, qProduto)		
				.leftJoin(qVasilhameEntregueItemBaixa.usuario, qUsuario);
		
		Map<String, PathBuilder<?>> joins = new HashMap<String, PathBuilder<?>>();
		joins.put(qVasilhameEntregueItemBaixa.empresa.toString(), new PathBuilder<>(Object.class, qEmpresa.toString()));
		joins.put(qVasilhameEntregueItemBaixa.filial.toString(), new PathBuilder<>(Object.class, qFilial.toString()));
		joins.put(qVasilhameEntregueItemBaixa.vasilhameEntregueItem.toString(), new PathBuilder<>(Object.class, qVasilhameEntregueItem.toString()));
		joins.put(qVasilhameEntregueItem.vasilhameEntregue.toString(), new PathBuilder<>(Object.class, qVasilhameEntregue.toString()));
		joins.put(qVasilhameEntregue.pessoa.toString(), new PathBuilder<>(Object.class, qPessoa.toString()));
		joins.put(qVasilhameEntregue.usuario.toString(), new PathBuilder<>(Object.class, qUsuarioEntregue.toString()));
		joins.put(qVasilhameEntregueItem.produto.toString(), new PathBuilder<>(Object.class, qProduto.toString()));
		joins.put(qVasilhameEntregueItemBaixa.usuario.toString(), new PathBuilder<>(Object.class, qUsuario.toString()));
		
		this.pathBuilder = new PathBuilder<>(VasilhameEntregueItemBaixa.class,qVasilhameEntregueItemBaixa.toString());		
		this.filtrosBuilder = new G3FiltersBuilder<>(pathBuilder, VasilhameEntregueItemBaixa.class, joins);
		this.filtrosBuilder.applyFilters(filter, query);
		this.filtrosBuilder.applyOrder(filter, pathBuilder, query);
		
		return this.query.limit(filter.limit).offset(filter.start).fetch();
	}

}
