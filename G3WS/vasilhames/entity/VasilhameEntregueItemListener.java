package com.g3sistemas.vasilhames.entity;

import javax.ejb.EJB;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.g3sistemas.vasilhames.tabelas.VasilhameTabelaCadManagerBean;

public class VasilhameEntregueItemListener {

	@EJB
	private VasilhameTabelaCadManagerBean tabelaCadManagerBean;
	
	@PrePersist
	public void vasilhameEntreguePrePersist(VasilhameEntregueItem vasilhameEntregueItem) {
		if (vasilhameEntregueItem.getIdCodigo() == null) {
			if (vasilhameEntregueItem.getClass().isAnnotationPresent(Table.class)) {
				VasilhameTabela registro = new VasilhameTabela();
				registro.setNomeTabela(vasilhameEntregueItem.getClass().getAnnotation(Table.class).name());
				registro.setIdEmpresa(vasilhameEntregueItem.getIdEmpresa());
				registro.setIdFilial(vasilhameEntregueItem.getIdFilial());		
				vasilhameEntregueItem.getId().setIdCodigo(this.tabelaCadManagerBean.gerarID(registro));
			}
		}
	} 
}
