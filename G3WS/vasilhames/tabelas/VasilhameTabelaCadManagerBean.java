package com.g3sistemas.vasilhames.tabelas;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.g3sistemas.vasilhames.entity.QVasilhameTabela;
import com.g3sistemas.vasilhames.entity.VasilhameEntregue;
import com.g3sistemas.vasilhames.entity.VasilhameTabela;
import com.g3sistemas.vasilhames.entity.VasilhameTabelaId;
import com.querydsl.jpa.impl.JPAQuery;

@Stateless
public class VasilhameTabelaCadManagerBean {
	
	@PersistenceContext(unitName="g3-ds")
	private EntityManager em;
	
	private QVasilhameTabela qTabela;		
	
	private JPAQuery<VasilhameEntregue> query;


	public Integer gerarID(VasilhameTabela registro) {
		this.query = new JPAQuery<>(this.em);
		this.qTabela = QVasilhameTabela.vasilhameTabela;		
		
		VasilhameTabela tabela = this.query.select(qTabela)
				.from(qTabela)
				.where(qTabela.nomeTabela.eq(registro.getNomeTabela())
				.and(qTabela.idEmpresa.eq(registro.getIdEmpresa()))
				.and(qTabela.idFilial.eq(registro.getIdFilial())))				
				.fetchOne();
		
		if (tabela == null) {
			VasilhameTabelaId tabelaId = new VasilhameTabelaId();
			tabelaId.setNomeTabela(registro.getNomeTabela());
			tabelaId.setIdEmpresa(registro.getIdEmpresa());
			tabelaId.setIdFilial(registro.getIdFilial());		
			
			tabela = new VasilhameTabela();
			tabela.setId(tabelaId);
			tabela.setIdTabela(1);
			
			this.em.persist(tabela);
		} else {
			tabela.setIdTabela(tabela.getIdTabela() + 1);
			tabela = this.em.merge(tabela);
		}
		
		return tabela.getIdTabela();
	}	


}
