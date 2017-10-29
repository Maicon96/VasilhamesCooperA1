package com.g3sistemas.vasilhames.entity;

import javax.ejb.EJB;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.g3sistemas.vasilhames.tabelas.VasilhameTabelaCadManagerBean;

public class VasilhameEntregarItemListener {

	@EJB
	private VasilhameTabelaCadManagerBean tabelaCadManagerBean;
	
	@PrePersist
	public void vasilhameEntreguePrePersist(VasilhameEntregarItem vasilhameEntregarItem) {
		if (vasilhameEntregarItem.getIdCodigo() == null) {
			if (vasilhameEntregarItem.getClass().isAnnotationPresent(Table.class)) {
				VasilhameTabela registro = new VasilhameTabela();
				registro.setNomeTabela(vasilhameEntregarItem.getClass().getAnnotation(Table.class).name());
				registro.setIdEmpresa(vasilhameEntregarItem.getIdEmpresa());
				registro.setIdFilial(vasilhameEntregarItem.getIdFilial());		
				vasilhameEntregarItem.getId().setIdCodigo(this.tabelaCadManagerBean.gerarID(registro));
			}
		}
	} 
}
