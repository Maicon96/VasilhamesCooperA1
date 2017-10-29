package com.g3sistemas.vasilhames.enums;

public enum TipoBaixa {

	VENDA(1), DEVOLUCAO(2);
	
	private Integer valor;

	private TipoBaixa(Integer valor) {
		this.valor = valor;
	}

	public Integer getValor() {
		return valor;
	}
	
	public static boolean existe(Integer valor) {
		for (TipoBaixa t : TipoBaixa.values()) {
			if (t.getValor().equals(valor)) {
				return true;
			}
	    }
		return false;
	}
	
}
