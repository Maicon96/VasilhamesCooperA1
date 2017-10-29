package com.g3sistemas.vasilhames.enums;

public enum TipoContribuinte {

	PESSOA_FISICA(1), PESSOA_JURIDICA(2);
	
	private Integer valor;

	private TipoContribuinte(Integer valor) {
		this.valor = valor;
	}

	public Integer getValor() {
		return valor;
	}
	
	public static boolean existe(Integer valor) {
		for (TipoContribuinte t : TipoContribuinte.values()) {
			if (t.getValor().equals(valor)) {
				return true;
			}
	    }
		return false;
	}
	
}
