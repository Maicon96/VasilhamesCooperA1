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
import com.g3sistemas.vasilhames.entity.QVasilhameEntregueItemBaixaCupom;
import com.g3sistemas.vasilhames.entity.VasilhameEntregueItemBaixaCupom;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;

@Stateless
public class VasilhameEntregueItemBaixaCupomConManagerBean {

	@PersistenceContext(unitName="g3-ds")
	private EntityManager em;
	
	private QVasilhameEntregueItemBaixaCupom qVasilhameEntregueItemBaixaCupom;
	private QEmpresa qEmpresaBaixa;
	private QFilial qFilialBaixa;
	private QVasilhameEntregueItemBaixa qVasilhameEntregueItemBaixa;
	private QVasilhameEntregueItem qVasilhameEntregueItem;
	private QVasilhameEntregue qVasilhameEntregue;	
	private QPessoa qPessoa;	
	private QUsuario qUsuarioEntregue;
	private QProduto qProduto;		
	private QEmpresa qEmpresaCupom;
	private QFilial qFilialCupom;	
	
	private JPAQuery<VasilhameEntregueItemBaixaCupom> query;
	private PathBuilder<VasilhameEntregueItemBaixaCupom> pathBuilder;
	private G3FiltersBuilder<VasilhameEntregueItemBaixaCupom> filtrosBuilder;	
	
	private ConstructorExpression<VasilhameEntregueItemBaixaCupom> projecaoLoad() {
		return QVasilhameEntregueItemBaixaCupom.create(qVasilhameEntregueItemBaixaCupom.idEmpresaBaixa,
				QEmpresa.create(qEmpresaBaixa.id, qEmpresaBaixa.nome), qVasilhameEntregueItemBaixaCupom.idFilialBaixa,
				QFilial.create(qFilialBaixa.codigo, qFilialBaixa.descricao),
				qVasilhameEntregueItemBaixaCupom.idVasilhameEntregueItemBaixa,
				QVasilhameEntregueItemBaixa.create(qVasilhameEntregueItemBaixa.idCodigo,
						qVasilhameEntregueItemBaixa.idVasilhameEntregueItem,
						QVasilhameEntregueItem.create(qVasilhameEntregueItem.idVasilhameEntregue,
								QVasilhameEntregue.create(qVasilhameEntregue.idCodigo, qVasilhameEntregue.idPessoa,
										QPessoa.create(qPessoa.idEmpresa, qPessoa.idPessoa, qPessoa.digito,	qPessoa.nome),
										qVasilhameEntregue.nome, qVasilhameEntregue.tipoContribuinte,
										qVasilhameEntregue.cpfcnpj, qVasilhameEntregue.situacao,
										qVasilhameEntregue.idUsuario, QUsuario.create(qUsuarioEntregue.nome)),
								qVasilhameEntregueItem.idProduto,
								QProduto.create(qProduto.id, qProduto.digito, qProduto.descricao),
								qVasilhameEntregueItem.quantidade, qVasilhameEntregueItem.tipoGarrafeira,
								qVasilhameEntregueItem.situacao),
						qVasilhameEntregueItemBaixa.tipoBaixa),
				qVasilhameEntregueItemBaixaCupom.idEmpresaCupom, QEmpresa.create(qEmpresaCupom.id, qEmpresaCupom.nome),
				qVasilhameEntregueItemBaixaCupom.idFilialCupom,
				QFilial.create(qFilialCupom.codigo, qFilialCupom.descricao),
				qVasilhameEntregueItemBaixaCupom.idEcfCupom, qVasilhameEntregueItemBaixaCupom.numero,
				qVasilhameEntregueItemBaixaCupom.dataEmissao, qVasilhameEntregueItemBaixaCupom.sequencia);
	}
	

	public long count(LoadRequestDTO filter) throws NoSuchFieldException {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregueItemBaixaCupom = QVasilhameEntregueItemBaixaCupom.vasilhameEntregueItemBaixaCupom;			

		this.query.select(qVasilhameEntregueItemBaixaCupom.countDistinct())
				.from(qVasilhameEntregueItemBaixaCupom);		
		
		Map<String, PathBuilder<?>> joins = new HashMap<String, PathBuilder<?>>();
		
		this.pathBuilder = new PathBuilder<>(VasilhameEntregueItemBaixaCupom.class,qVasilhameEntregueItemBaixaCupom.toString());		
		this.filtrosBuilder = new G3FiltersBuilder<>(pathBuilder, VasilhameEntregueItemBaixaCupom.class, joins);
		this.filtrosBuilder.applyFilters(filter, query);
		
		return this.query.fetchCount();
	}
	
	
	public List<VasilhameEntregueItemBaixaCupom> load(LoadRequestDTO filter) throws Exception {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregueItemBaixaCupom = QVasilhameEntregueItemBaixaCupom.vasilhameEntregueItemBaixaCupom;	
		this.qEmpresaBaixa = new QEmpresa("empresaBaixa");
		this.qFilialBaixa = new QFilial("filialBaixa");
		this.qVasilhameEntregueItemBaixa = QVasilhameEntregueItemBaixa.vasilhameEntregueItemBaixa;	
		this.qVasilhameEntregueItem = QVasilhameEntregueItem.vasilhameEntregueItem;
		this.qVasilhameEntregue = QVasilhameEntregue.vasilhameEntregue;		
		this.qPessoa = QPessoa.pessoa;
		this.qUsuarioEntregue = new QUsuario("usuarioEntregue");
		this.qProduto = QProduto.produto;
		this.qEmpresaCupom = new QEmpresa("empresaCupom");
		this.qFilialCupom = new QFilial("filialCupom");		
		
		this.query.select(this.projecaoLoad())
				.from(qVasilhameEntregueItemBaixaCupom)
				.leftJoin(qVasilhameEntregueItemBaixaCupom.empresaBaixa, qEmpresaBaixa)
				.leftJoin(qVasilhameEntregueItemBaixaCupom.filialBaixa, qFilialBaixa)
				.leftJoin(qVasilhameEntregueItemBaixaCupom.vasilhameEntregueItemBaixa, qVasilhameEntregueItemBaixa)
				.leftJoin(qVasilhameEntregueItemBaixa.vasilhameEntregueItem, qVasilhameEntregueItem)
				.leftJoin(qVasilhameEntregueItem.vasilhameEntregue, qVasilhameEntregue)
				.leftJoin(qVasilhameEntregue.pessoa, qPessoa)
				.leftJoin(qVasilhameEntregue.usuario, qUsuarioEntregue)
				.leftJoin(qVasilhameEntregueItem.produto, qProduto)
				.leftJoin(qVasilhameEntregueItemBaixaCupom.empresaCupom, qEmpresaCupom)
				.leftJoin(qVasilhameEntregueItemBaixaCupom.filialCupom, qFilialCupom);	
		
		Map<String, PathBuilder<?>> joins = new HashMap<String, PathBuilder<?>>();
		joins.put(qVasilhameEntregueItemBaixaCupom.empresaBaixa.toString(), new PathBuilder<>(Object.class, qEmpresaBaixa.toString()));
		joins.put(qVasilhameEntregueItemBaixaCupom.filialBaixa.toString(), new PathBuilder<>(Object.class, qFilialBaixa.toString()));
		joins.put(qVasilhameEntregueItemBaixaCupom.vasilhameEntregueItemBaixa.toString(), new PathBuilder<>(Object.class, qVasilhameEntregueItemBaixa.toString()));
		joins.put(qVasilhameEntregueItemBaixa.vasilhameEntregueItem.toString(), new PathBuilder<>(Object.class, qVasilhameEntregueItem.toString()));
		joins.put(qVasilhameEntregueItem.vasilhameEntregue.toString(), new PathBuilder<>(Object.class, qVasilhameEntregue.toString()));
		joins.put(qVasilhameEntregue.pessoa.toString(), new PathBuilder<>(Object.class, qPessoa.toString()));
		joins.put(qVasilhameEntregue.usuario.toString(), new PathBuilder<>(Object.class, qUsuarioEntregue.toString()));
		joins.put(qVasilhameEntregueItem.produto.toString(), new PathBuilder<>(Object.class, qProduto.toString()));
		joins.put(qVasilhameEntregueItemBaixaCupom.empresaCupom.toString(), new PathBuilder<>(Object.class, qEmpresaCupom.toString()));
		joins.put(qVasilhameEntregueItemBaixaCupom.filialCupom.toString(), new PathBuilder<>(Object.class, qFilialCupom.toString()));		
		
		this.pathBuilder = new PathBuilder<>(VasilhameEntregueItemBaixaCupom.class,qVasilhameEntregueItemBaixaCupom.toString());		
		this.filtrosBuilder = new G3FiltersBuilder<>(pathBuilder, VasilhameEntregueItemBaixaCupom.class, joins);
		this.filtrosBuilder.applyFilters(filter, query);
		this.filtrosBuilder.applyOrder(filter, pathBuilder, query);
		
		return this.query.limit(filter.limit).offset(filter.start).fetch();
	}

}
