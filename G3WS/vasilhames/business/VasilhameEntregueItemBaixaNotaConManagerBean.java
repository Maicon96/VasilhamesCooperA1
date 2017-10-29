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
import com.g3sistemas.vasilhames.entity.QVasilhameEntregueItemBaixaNota;
import com.g3sistemas.vasilhames.entity.VasilhameEntregueItemBaixaNota;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;

@Stateless
public class VasilhameEntregueItemBaixaNotaConManagerBean {

	@PersistenceContext(unitName="g3-ds")
	private EntityManager em;
	
	private QVasilhameEntregueItemBaixaNota qVasilhameEntregueItemBaixaNota;
	private QEmpresa qEmpresaBaixa;
	private QFilial qFilialBaixa;
	private QVasilhameEntregueItemBaixa qVasilhameEntregueItemBaixa;
	private QVasilhameEntregueItem qVasilhameEntregueItem;
	private QVasilhameEntregue qVasilhameEntregue;	
	private QPessoa qPessoa;	
	private QUsuario qUsuarioEntregue;
	private QProduto qProduto;
	private QEmpresa qEmpresaNota;
	private QFilial qFilialNota;	
	
	private JPAQuery<VasilhameEntregueItemBaixaNota> query;
	private PathBuilder<VasilhameEntregueItemBaixaNota> pathBuilder;
	private G3FiltersBuilder<VasilhameEntregueItemBaixaNota> filtrosBuilder;	
	
	private ConstructorExpression<VasilhameEntregueItemBaixaNota> projecaoLoad() {
		return QVasilhameEntregueItemBaixaNota.create(qVasilhameEntregueItemBaixaNota.idEmpresaBaixa,
				QEmpresa.create(qEmpresaBaixa.id, qEmpresaBaixa.nome), qVasilhameEntregueItemBaixaNota.idFilialBaixa,
				QFilial.create(qFilialBaixa.codigo, qFilialBaixa.descricao),
				qVasilhameEntregueItemBaixaNota.idVasilhameEntregueItemBaixa,
				QVasilhameEntregueItemBaixa.create(qVasilhameEntregueItemBaixa.idCodigo,
						qVasilhameEntregueItemBaixa.idVasilhameEntregueItem,
						QVasilhameEntregueItem.create(qVasilhameEntregueItem.idVasilhameEntregue,
								QVasilhameEntregue.create(qVasilhameEntregue.idCodigo, qVasilhameEntregue.idPessoa,
										QPessoa.create(qPessoa.idEmpresa, qPessoa.idPessoa, qPessoa.digito,
												qPessoa.nome),
										qVasilhameEntregue.nome, qVasilhameEntregue.tipoContribuinte,
										qVasilhameEntregue.cpfcnpj, qVasilhameEntregue.situacao,
										qVasilhameEntregue.idUsuario, QUsuario.create(qUsuarioEntregue.nome)),
								qVasilhameEntregueItem.idProduto,
								QProduto.create(qProduto.id, qProduto.digito, qProduto.descricao),
								qVasilhameEntregueItem.quantidade, qVasilhameEntregueItem.tipoGarrafeira,
								qVasilhameEntregueItem.situacao),
						qVasilhameEntregueItemBaixa.tipoBaixa),
				qVasilhameEntregueItemBaixaNota.idEmpresaNota, QEmpresa.create(qEmpresaNota.id, qEmpresaNota.nome),
				qVasilhameEntregueItemBaixaNota.modelo, qVasilhameEntregueItemBaixaNota.idFilialNota,
				QFilial.create(qFilialNota.codigo, qFilialNota.descricao), qVasilhameEntregueItemBaixaNota.serie,
				qVasilhameEntregueItemBaixaNota.numero, qVasilhameEntregueItemBaixaNota.dataEmissao,
				qVasilhameEntregueItemBaixaNota.sequencia);
	}
	

	public long count(LoadRequestDTO filter) throws NoSuchFieldException {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregueItemBaixaNota = QVasilhameEntregueItemBaixaNota.vasilhameEntregueItemBaixaNota;			

		this.query.select(qVasilhameEntregueItemBaixaNota.countDistinct())
				.from(qVasilhameEntregueItemBaixaNota);		
		
		Map<String, PathBuilder<?>> joins = new HashMap<String, PathBuilder<?>>();
		
		this.pathBuilder = new PathBuilder<>(VasilhameEntregueItemBaixaNota.class,qVasilhameEntregueItemBaixaNota.toString());		
		this.filtrosBuilder = new G3FiltersBuilder<>(pathBuilder, VasilhameEntregueItemBaixaNota.class, joins);
		this.filtrosBuilder.applyFilters(filter, query);
		
		return this.query.fetchCount();
	}
	
	
	public List<VasilhameEntregueItemBaixaNota> load(LoadRequestDTO filter) throws Exception {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregueItemBaixaNota = QVasilhameEntregueItemBaixaNota.vasilhameEntregueItemBaixaNota;	
		this.qEmpresaBaixa = new QEmpresa("empresaBaixa");
		this.qFilialBaixa = new QFilial("filialBaixa");
		this.qVasilhameEntregueItemBaixa = QVasilhameEntregueItemBaixa.vasilhameEntregueItemBaixa;	
		this.qVasilhameEntregueItem = QVasilhameEntregueItem.vasilhameEntregueItem;
		this.qVasilhameEntregue = QVasilhameEntregue.vasilhameEntregue;		
		this.qPessoa = QPessoa.pessoa;
		this.qUsuarioEntregue = new QUsuario("usuarioEntregue");
		this.qProduto = QProduto.produto;
		this.qEmpresaNota = new QEmpresa("empresaNota");
		this.qFilialNota = new QFilial("filialNota");
		
		this.query.select(this.projecaoLoad())
				.from(qVasilhameEntregueItemBaixaNota)
				.leftJoin(qVasilhameEntregueItemBaixaNota.empresaBaixa, qEmpresaBaixa)
				.leftJoin(qVasilhameEntregueItemBaixaNota.filialBaixa, qFilialBaixa)
				.leftJoin(qVasilhameEntregueItemBaixaNota.vasilhameEntregueItemBaixa, qVasilhameEntregueItemBaixa)
				.leftJoin(qVasilhameEntregueItemBaixa.vasilhameEntregueItem, qVasilhameEntregueItem)
				.leftJoin(qVasilhameEntregueItem.vasilhameEntregue, qVasilhameEntregue)
				.leftJoin(qVasilhameEntregue.pessoa, qPessoa)
				.leftJoin(qVasilhameEntregue.usuario, qUsuarioEntregue)
				.leftJoin(qVasilhameEntregueItem.produto, qProduto)
				.leftJoin(qVasilhameEntregueItemBaixaNota.empresaNota, qEmpresaNota)
				.leftJoin(qVasilhameEntregueItemBaixaNota.filialNota, qFilialNota);
		
		Map<String, PathBuilder<?>> joins = new HashMap<String, PathBuilder<?>>();
		joins.put(qVasilhameEntregueItemBaixaNota.empresaBaixa.toString(), new PathBuilder<>(Object.class, qEmpresaBaixa.toString()));
		joins.put(qVasilhameEntregueItemBaixaNota.filialBaixa.toString(), new PathBuilder<>(Object.class, qFilialBaixa.toString()));
		joins.put(qVasilhameEntregueItemBaixaNota.vasilhameEntregueItemBaixa.toString(), new PathBuilder<>(Object.class, qVasilhameEntregueItemBaixa.toString()));
		joins.put(qVasilhameEntregueItemBaixa.vasilhameEntregueItem.toString(), new PathBuilder<>(Object.class, qVasilhameEntregueItem.toString()));
		joins.put(qVasilhameEntregueItem.vasilhameEntregue.toString(), new PathBuilder<>(Object.class, qVasilhameEntregue.toString()));
		joins.put(qVasilhameEntregue.pessoa.toString(), new PathBuilder<>(Object.class, qPessoa.toString()));
		joins.put(qVasilhameEntregue.usuario.toString(), new PathBuilder<>(Object.class, qUsuarioEntregue.toString()));
		joins.put(qVasilhameEntregueItem.produto.toString(), new PathBuilder<>(Object.class, qProduto.toString()));
		joins.put(qVasilhameEntregueItemBaixaNota.empresaNota.toString(), new PathBuilder<>(Object.class, qEmpresaNota.toString()));
		joins.put(qVasilhameEntregueItemBaixaNota.filialNota.toString(), new PathBuilder<>(Object.class, qFilialNota.toString()));
		
		this.pathBuilder = new PathBuilder<>(VasilhameEntregueItemBaixaNota.class,qVasilhameEntregueItemBaixaNota.toString());		
		this.filtrosBuilder = new G3FiltersBuilder<>(pathBuilder, VasilhameEntregueItemBaixaNota.class, joins);
		this.filtrosBuilder.applyFilters(filter, query);
		this.filtrosBuilder.applyOrder(filter, pathBuilder, query);
		
		return this.query.limit(filter.limit).offset(filter.start).fetch();
	}

}
