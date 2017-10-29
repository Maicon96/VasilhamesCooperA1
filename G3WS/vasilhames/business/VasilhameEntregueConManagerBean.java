package com.g3sistemas.vasilhames.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.g3sistemas.dto.LoadRequestDTO;
import com.g3sistemas.geral.entity.QEmpresa;
import com.g3sistemas.geral.entity.QFilial;
import com.g3sistemas.geral.entity.QPessoa;
import com.g3sistemas.sistema.entity.QUsuario;
import com.g3sistemas.sql.filter.G3FiltersBuilder;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregue;
import com.g3sistemas.vasilhames.entity.VasilhameEntregue;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;

@Stateless
public class VasilhameEntregueConManagerBean {

	@PersistenceContext(unitName="g3-ds")
	private EntityManager em;
	
	private QVasilhameEntregue qVasilhameEntregue;
	private QEmpresa qEmpresa;
	private QFilial qFilial;
	private QPessoa qPessoa;
	private QUsuario qUsuario;
	
	private JPAQuery<VasilhameEntregue> query;
	private PathBuilder<VasilhameEntregue> pathBuilder;
	private G3FiltersBuilder<VasilhameEntregue> filtrosBuilder;	
	
	private ConstructorExpression<VasilhameEntregue> projecaoLoad() {
		return QVasilhameEntregue.create(qVasilhameEntregue.id, qVasilhameEntregue.idEmpresa, QEmpresa.create(qEmpresa.id, qEmpresa.nome),
				qVasilhameEntregue.idFilial, QFilial.create(qFilial.codigo, qFilial.descricao),
				qVasilhameEntregue.idCodigo, qVasilhameEntregue.idPessoa,
				QPessoa.create(qPessoa.idPessoa, qPessoa.digito, qPessoa.nome, qPessoa.contribuinte, qPessoa.cpfCnpj),
				qVasilhameEntregue.nome, qVasilhameEntregue.tipoContribuinte, qVasilhameEntregue.cpfcnpj,
				qVasilhameEntregue.observacao, qVasilhameEntregue.situacao, qVasilhameEntregue.dataHoraGravacao,
				qVasilhameEntregue.idUsuario, QUsuario.create(qUsuario.nome));
	}
	

	public long count(LoadRequestDTO filter) throws NoSuchFieldException {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregue = QVasilhameEntregue.vasilhameEntregue;			

		this.query.select(qVasilhameEntregue.countDistinct())
				.from(qVasilhameEntregue);		
		
		Map<String, PathBuilder<?>> joins = new HashMap<String, PathBuilder<?>>();
		
		this.pathBuilder = new PathBuilder<>(VasilhameEntregue.class,qVasilhameEntregue.toString());		
		this.filtrosBuilder = new G3FiltersBuilder<>(pathBuilder, VasilhameEntregue.class, joins);
		this.filtrosBuilder.applyFilters(filter, query);
		
		return this.query.fetchCount();
	}
	
	
	public List<VasilhameEntregue> load(LoadRequestDTO filter) throws Exception {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregue = QVasilhameEntregue.vasilhameEntregue;	
		this.qEmpresa = QEmpresa.empresa;
		this.qFilial = QFilial.filial;
		this.qPessoa = QPessoa.pessoa;
		this.qUsuario = QUsuario.usuario;
		
		this.query.select(this.projecaoLoad())
				.from(qVasilhameEntregue)
				.leftJoin(qVasilhameEntregue.empresa, qEmpresa)
				.leftJoin(qVasilhameEntregue.filial, qFilial)
				.leftJoin(qVasilhameEntregue.pessoa, qPessoa)
				.leftJoin(qVasilhameEntregue.usuario, qUsuario);
		
		Map<String, PathBuilder<?>> joins = new HashMap<String, PathBuilder<?>>();
		joins.put(qVasilhameEntregue.empresa.toString(), new PathBuilder<>(Object.class, qEmpresa.toString()));
		joins.put(qVasilhameEntregue.filial.toString(), new PathBuilder<>(Object.class, qFilial.toString()));
		joins.put(qVasilhameEntregue.pessoa.toString(), new PathBuilder<>(Object.class, qPessoa.toString()));
		joins.put(qVasilhameEntregue.usuario.toString(), new PathBuilder<>(Object.class, qUsuario.toString()));
		
		this.pathBuilder = new PathBuilder<>(VasilhameEntregue.class,qVasilhameEntregue.toString());		
		this.filtrosBuilder = new G3FiltersBuilder<>(pathBuilder, VasilhameEntregue.class, joins);
		this.filtrosBuilder.applyFilters(filter, query);
		this.filtrosBuilder.applyOrder(filter, pathBuilder, query);
		
		return this.query.limit(filter.limit).offset(filter.start).fetch();
	}

}
