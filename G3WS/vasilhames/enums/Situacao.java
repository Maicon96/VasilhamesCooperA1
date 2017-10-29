package com.g3sistemas.vasilhames.enums;

public enum Situacao {

	ABERTO(1), PENDENTE(2), INUTILIZADO(8), FECHADO(9);
	
	private Integer valor;

	private Situacao(Integer valor) {
		this.valor = valor;
	}

	public Integer getValor() {
		return valor;
	}
	
	public static boolean existe(Integer valor) {
		for (Situacao t : Situacao.values()) {
			if (t.getValor().equals(valor)) {
				return true;
			}
	    }
		return false;
	}
	
}
