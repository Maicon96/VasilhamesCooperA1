package com.g3sistemas.vasilhames.entity;

import javax.ejb.EJB;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.g3sistemas.vasilhames.tabelas.VasilhameTabelaCadManagerBean;

public class VasilhameEntregarListener {

	@EJB
	private VasilhameTabelaCadManagerBean tabelaCadManagerBean;
	
	@PrePersist
	public void vasilhameEntreguePrePersist(VasilhameEntregar vasilhameEntrega) {
		if (vasilhameEntrega.getIdCodigo() == null) {
			if (vasilhameEntrega.getClass().isAnnotationPresent(Table.class)) {
				VasilhameTabela registro = new VasilhameTabela();
				registro.setNomeTabela(vasilhameEntrega.getClass().getAnnotation(Table.class).name());
				registro.setIdEmpresa(vasilhameEntrega.getIdEmpresa());
				registro.setIdFilial(vasilhameEntrega.getIdFilial());		
				vasilhameEntrega.getId().setIdCodigo(this.tabelaCadManagerBean.gerarID(registro));
			}
		}
	} 
}
