package com.g3sistemas.vasilhames.enums;

public enum ModeloNota {

	NOTA_FISCAL("01"), NOTA_FISCAL_AVULSA("1B"), NOTA_FISCAL_ELETRONICA("55"), NOTA_FISCAL_CONSUMIDOR_ELETRONICA("65");
	
	private String valor;

	private ModeloNota(String valor) {
		this.valor = valor;
	}

	public String getValor() {
		return valor;
	}
	
	public static boolean existe(String valor) {
		for (ModeloNota t : ModeloNota.values()) {
			if (t.getValor().equals(valor)) {
				return true;
			}
	    }
		return false;
	}
	
}
