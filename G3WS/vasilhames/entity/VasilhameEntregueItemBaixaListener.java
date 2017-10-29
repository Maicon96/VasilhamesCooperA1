package com.g3sistemas.vasilhames.entity;

import javax.ejb.EJB;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.g3sistemas.vasilhames.tabelas.VasilhameTabelaCadManagerBean;

public class VasilhameEntregueItemBaixaListener {

	@EJB
	private VasilhameTabelaCadManagerBean tabelaCadManagerBean;
	
	@PrePersist
	public void vasilhameEntreguePrePersist(VasilhameEntregueItemBaixa vasilhameEntregueItemBaixa) {
		if (vasilhameEntregueItemBaixa.getIdCodigo() == null) {
			if (vasilhameEntregueItemBaixa.getClass().isAnnotationPresent(Table.class)) {
				VasilhameTabela registro = new VasilhameTabela();
				registro.setNomeTabela(vasilhameEntregueItemBaixa.getClass().getAnnotation(Table.class).name());
				registro.setIdEmpresa(vasilhameEntregueItemBaixa.getIdEmpresa());
				registro.setIdFilial(vasilhameEntregueItemBaixa.getIdFilial());		
				vasilhameEntregueItemBaixa.getId().setIdCodigo(this.tabelaCadManagerBean.gerarID(registro));
			}
		}
	} 
}
