package com.g3sistemas.vasilhames.enums;

public enum TipoBaixaEntregar {

	ENTREGUE(1);
	
	private Integer valor;

	private TipoBaixaEntregar(Integer valor) {
		this.valor = valor;
	}

	public Integer getValor() {
		return valor;
	}
	
	public static boolean existe(Integer valor) {
		for (TipoBaixaEntregar t : TipoBaixaEntregar.values()) {
			if (t.getValor().equals(valor)) {
				return true;
			}
	    }
		return false;
	}
	
}
