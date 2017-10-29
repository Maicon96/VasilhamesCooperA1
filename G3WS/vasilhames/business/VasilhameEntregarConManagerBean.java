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
import com.g3sistemas.vasilhames.entity.QVasilhameEntregar;
import com.g3sistemas.vasilhames.entity.VasilhameEntregar;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;

@Stateless
public class VasilhameEntregarConManagerBean {

	@PersistenceContext(unitName="g3-ds")
	private EntityManager em;
	
	private QVasilhameEntregar qVasilhameEntregar;
	private QEmpresa qEmpresa;
	private QFilial qFilial;
	private QPessoa qPessoa;
	private QUsuario qUsuario;
	
	private JPAQuery<VasilhameEntregar> query;
	private PathBuilder<VasilhameEntregar> pathBuilder;
	private G3FiltersBuilder<VasilhameEntregar> filtrosBuilder;	
	
	private ConstructorExpression<VasilhameEntregar> projecaoLoad() {
		return QVasilhameEntregar.create(qVasilhameEntregar.id, qVasilhameEntregar.idEmpresa, QEmpresa.create(qEmpresa.id, qEmpresa.nome),
				qVasilhameEntregar.idFilial, QFilial.create(qFilial.codigo, qFilial.descricao),
				qVasilhameEntregar.idCodigo, qVasilhameEntregar.idPessoa,
				QPessoa.create(qPessoa.idPessoa, qPessoa.digito, qPessoa.nome, qPessoa.contribuinte, qPessoa.cpfCnpj),
				qVasilhameEntregar.nome, qVasilhameEntregar.tipoContribuinte, qVasilhameEntregar.cpfcnpj,
				qVasilhameEntregar.observacao, qVasilhameEntregar.situacao, qVasilhameEntregar.dataHoraGravacao,
				qVasilhameEntregar.idUsuario, QUsuario.create(qUsuario.nome));
	}
	

	public long count(LoadRequestDTO filter) throws NoSuchFieldException {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregar = QVasilhameEntregar.vasilhameEntregar;			

		this.query.select(qVasilhameEntregar.countDistinct())
				.from(qVasilhameEntregar);		
		
		Map<String, PathBuilder<?>> joins = new HashMap<String, PathBuilder<?>>();
		
		this.pathBuilder = new PathBuilder<>(VasilhameEntregar.class,qVasilhameEntregar.toString());		
		this.filtrosBuilder = new G3FiltersBuilder<>(pathBuilder, VasilhameEntregar.class, joins);
		this.filtrosBuilder.applyFilters(filter, query);
		
		return this.query.fetchCount();
	}
	
	
	public List<VasilhameEntregar> load(LoadRequestDTO filter) throws Exception {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregar = QVasilhameEntregar.vasilhameEntregar;	
		this.qEmpresa = QEmpresa.empresa;
		this.qFilial = QFilial.filial;
		this.qPessoa = QPessoa.pessoa;
		this.qUsuario = QUsuario.usuario;
		
		this.query.select(this.projecaoLoad())
				.from(qVasilhameEntregar)
				.leftJoin(qVasilhameEntregar.empresa, qEmpresa)
				.leftJoin(qVasilhameEntregar.filial, qFilial)
				.leftJoin(qVasilhameEntregar.pessoa, qPessoa)
				.leftJoin(qVasilhameEntregar.usuario, qUsuario);
		
		Map<String, PathBuilder<?>> joins = new HashMap<String, PathBuilder<?>>();
		joins.put(qVasilhameEntregar.empresa.toString(), new PathBuilder<>(Object.class, qEmpresa.toString()));
		joins.put(qVasilhameEntregar.filial.toString(), new PathBuilder<>(Object.class, qFilial.toString()));
		joins.put(qVasilhameEntregar.pessoa.toString(), new PathBuilder<>(Object.class, qPessoa.toString()));
		joins.put(qVasilhameEntregar.usuario.toString(), new PathBuilder<>(Object.class, qUsuario.toString()));
		
		this.pathBuilder = new PathBuilder<>(VasilhameEntregar.class,qVasilhameEntregar.toString());		
		this.filtrosBuilder = new G3FiltersBuilder<>(pathBuilder, VasilhameEntregar.class, joins);
		this.filtrosBuilder.applyFilters(filter, query);
		this.filtrosBuilder.applyOrder(filter, pathBuilder, query);
		
		return this.query.limit(filter.limit).offset(filter.start).fetch();
	}

}
