package com.g3sistemas.vasilhames.entity;

import javax.ejb.EJB;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.g3sistemas.vasilhames.tabelas.VasilhameTabelaCadManagerBean;

public class VasilhameEntregueListener {

	@EJB
	private VasilhameTabelaCadManagerBean tabelaCadManagerBean;
	
	@PrePersist
	public void vasilhameEntreguePrePersist(VasilhameEntregue vasilhameEntregue) {
		if (vasilhameEntregue.getIdCodigo() == null) {
			if (vasilhameEntregue.getClass().isAnnotationPresent(Table.class)) {
				VasilhameTabela registro = new VasilhameTabela();
				registro.setNomeTabela(vasilhameEntregue.getClass().getAnnotation(Table.class).name());
				registro.setIdEmpresa(vasilhameEntregue.getIdEmpresa());
				registro.setIdFilial(vasilhameEntregue.getIdFilial());		
				vasilhameEntregue.getId().setIdCodigo(this.tabelaCadManagerBean.gerarID(registro));
			}
		}
	} 
}
