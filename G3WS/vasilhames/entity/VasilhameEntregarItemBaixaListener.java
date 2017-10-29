package com.g3sistemas.vasilhames.entity;

import javax.ejb.EJB;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.g3sistemas.vasilhames.tabelas.VasilhameTabelaCadManagerBean;

public class VasilhameEntregarItemBaixaListener {

	@EJB
	private VasilhameTabelaCadManagerBean tabelaCadManagerBean;
	
	@PrePersist
	public void vasilhameEntreguePrePersist(VasilhameEntregarItemBaixa vasilhameEntregarItemBaixa) {
		if (vasilhameEntregarItemBaixa.getIdCodigo() == null) {
			if (vasilhameEntregarItemBaixa.getClass().isAnnotationPresent(Table.class)) {
				VasilhameTabela registro = new VasilhameTabela();
				registro.setNomeTabela(vasilhameEntregarItemBaixa.getClass().getAnnotation(Table.class).name());
				registro.setIdEmpresa(vasilhameEntregarItemBaixa.getIdEmpresa());
				registro.setIdFilial(vasilhameEntregarItemBaixa.getIdFilial());		
				vasilhameEntregarItemBaixa.getId().setIdCodigo(this.tabelaCadManagerBean.gerarID(registro));
			}
		}
	} 
}
