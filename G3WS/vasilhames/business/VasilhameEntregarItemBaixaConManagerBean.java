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
import com.g3sistemas.vasilhames.entity.VasilhameEntregarItemBaixa;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;

@Stateless
public class VasilhameEntregarItemBaixaConManagerBean {

	@PersistenceContext(unitName="g3-ds")
	private EntityManager em;
	
	private QVasilhameEntregarItemBaixa qVasilhameEntregarItemBaixa;
	private QEmpresa qEmpresa;
	private QFilial qFilial;
	private QVasilhameEntregarItem qVasilhameEntregarItem;	
	private QVasilhameEntregar qVasilhameEntregar;	
	private QPessoa qPessoa;	
	private QUsuario qUsuarioEntregar;
	private QProduto qProduto;		
	private QUsuario qUsuario;
	
	private JPAQuery<VasilhameEntregarItemBaixa> query;
	private PathBuilder<VasilhameEntregarItemBaixa> pathBuilder;
	private G3FiltersBuilder<VasilhameEntregarItemBaixa> filtrosBuilder;	
	
	private ConstructorExpression<VasilhameEntregarItemBaixa> projecaoLoad() {
		return QVasilhameEntregarItemBaixa.create(qVasilhameEntregarItemBaixa.id, qVasilhameEntregarItemBaixa.idEmpresa,
				QEmpresa.create(qEmpresa.id, qEmpresa.nome), qVasilhameEntregarItemBaixa.idFilial,
				QFilial.create(qFilial.codigo, qFilial.descricao), qVasilhameEntregarItemBaixa.idCodigo,
				qVasilhameEntregarItemBaixa.idVasilhameEntregarItem,
				QVasilhameEntregarItem.create(qVasilhameEntregarItem.idVasilhameEntregar,
						QVasilhameEntregar.create(qVasilhameEntregar.idCodigo, qVasilhameEntregar.idPessoa,
								QPessoa.create(qPessoa.idEmpresa, qPessoa.idPessoa, qPessoa.digito, qPessoa.nome),
								qVasilhameEntregar.nome, qVasilhameEntregar.tipoContribuinte,
								qVasilhameEntregar.cpfcnpj, qVasilhameEntregar.situacao, qVasilhameEntregar.idUsuario,
								QUsuario.create(qUsuarioEntregar.nome)),
						qVasilhameEntregarItem.idProduto,
						QProduto.create(qProduto.id, qProduto.digito, qProduto.descricao),
						qVasilhameEntregarItem.quantidade, qVasilhameEntregarItem.tipoGarrafeira,
						qVasilhameEntregarItem.situacao),
				qVasilhameEntregarItemBaixa.tipoBaixa, qVasilhameEntregarItemBaixa.quantidade,				
				qVasilhameEntregarItemBaixa.dataHoraGravacao, qVasilhameEntregarItemBaixa.idUsuario,
				QUsuario.create(qUsuario.nome));
	}
	

	public long count(LoadRequestDTO filter) throws NoSuchFieldException {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregarItemBaixa = QVasilhameEntregarItemBaixa.vasilhameEntregarItemBaixa;			

		this.query.select(qVasilhameEntregarItemBaixa.countDistinct())
				.from(qVasilhameEntregarItemBaixa);		
		
		Map<String, PathBuilder<?>> joins = new HashMap<String, PathBuilder<?>>();
		
		this.pathBuilder = new PathBuilder<>(VasilhameEntregarItemBaixa.class,qVasilhameEntregarItemBaixa.toString());		
		this.filtrosBuilder = new G3FiltersBuilder<>(pathBuilder, VasilhameEntregarItemBaixa.class, joins);
		this.filtrosBuilder.applyFilters(filter, query);
		
		return this.query.fetchCount();
	}
	
	
	public List<VasilhameEntregarItemBaixa> load(LoadRequestDTO filter) throws Exception {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregarItemBaixa = QVasilhameEntregarItemBaixa.vasilhameEntregarItemBaixa;	
		this.qEmpresa = QEmpresa.empresa;
		this.qFilial = QFilial.filial;
		this.qVasilhameEntregarItem = QVasilhameEntregarItem.vasilhameEntregarItem;		
		this.qVasilhameEntregar = QVasilhameEntregar.vasilhameEntregar;		
		this.qPessoa = QPessoa.pessoa;
		this.qUsuarioEntregar = new QUsuario("usuarioEntregar");
		this.qProduto = QProduto.produto;
		this.qUsuario = QUsuario.usuario;
		
		this.query.select(this.projecaoLoad())
				.from(qVasilhameEntregarItemBaixa)
				.leftJoin(qVasilhameEntregarItemBaixa.empresa, qEmpresa)
				.leftJoin(qVasilhameEntregarItemBaixa.filial, qFilial)
				.leftJoin(qVasilhameEntregarItemBaixa.vasilhameEntregarItem, qVasilhameEntregarItem)				
				.leftJoin(qVasilhameEntregarItem.vasilhameEntregar, qVasilhameEntregar)
				.leftJoin(qVasilhameEntregar.pessoa, qPessoa)
				.leftJoin(qVasilhameEntregar.usuario, qUsuarioEntregar)
				.leftJoin(qVasilhameEntregarItem.produto, qProduto)
				.leftJoin(qVasilhameEntregarItemBaixa.usuario, qUsuario);
		
		Map<String, PathBuilder<?>> joins = new HashMap<String, PathBuilder<?>>();
		joins.put(qVasilhameEntregarItemBaixa.empresa.toString(), new PathBuilder<>(Object.class, qEmpresa.toString()));
		joins.put(qVasilhameEntregarItemBaixa.filial.toString(), new PathBuilder<>(Object.class, qFilial.toString()));
		joins.put(qVasilhameEntregarItemBaixa.vasilhameEntregarItem.toString(), new PathBuilder<>(Object.class, qVasilhameEntregarItem.toString()));
		joins.put(qVasilhameEntregarItem.vasilhameEntregar.toString(), new PathBuilder<>(Object.class, qVasilhameEntregar.toString()));
		joins.put(qVasilhameEntregar.pessoa.toString(), new PathBuilder<>(Object.class, qPessoa.toString()));
		joins.put(qVasilhameEntregar.usuario.toString(), new PathBuilder<>(Object.class, qUsuarioEntregar.toString()));
		joins.put(qVasilhameEntregarItem.produto.toString(), new PathBuilder<>(Object.class, qProduto.toString()));
		joins.put(qVasilhameEntregarItemBaixa.usuario.toString(), new PathBuilder<>(Object.class, qUsuario.toString()));
		
		this.pathBuilder = new PathBuilder<>(VasilhameEntregarItemBaixa.class,qVasilhameEntregarItemBaixa.toString());		
		this.filtrosBuilder = new G3FiltersBuilder<>(pathBuilder, VasilhameEntregarItemBaixa.class, joins);
		this.filtrosBuilder.applyFilters(filter, query);
		this.filtrosBuilder.applyOrder(filter, pathBuilder, query);
		
		return this.query.limit(filter.limit).offset(filter.start).fetch();
	}

}
