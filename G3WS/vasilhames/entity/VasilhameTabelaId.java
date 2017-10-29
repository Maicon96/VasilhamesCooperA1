package com.g3sistemas.vasilhames.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class VasilhameTabelaId implements Serializable { 
	
	private static final long serialVersionUID = 1L;

	@Column(name = "tabela")
	private String nomeTabela;	
	
	@Column(name="id_empresa")
    private Integer idEmpresa;
	
	@Column(name="id_filial")
	private Integer idFilial;
	
	
	public String getNomeTabela() {
		return nomeTabela;
	}

	public void setNomeTabela(String nomeTabela) {
		this.nomeTabela = nomeTabela;
	}

	public Integer getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Integer idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public Integer getIdFilial() {
		return idFilial;
	}

	public void setIdFilial(Integer idFilial) {
		this.idFilial = idFilial;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idEmpresa == null) ? 0 : idEmpresa.hashCode());
		result = prime * result + ((idFilial == null) ? 0 : idFilial.hashCode());
		result = prime * result + ((nomeTabela == null) ? 0 : nomeTabela.hashCode());
		return result;
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VasilhameTabelaId other = (VasilhameTabelaId) obj;
		if (idEmpresa == null) {
			if (other.idEmpresa != null)
				return false;
		} else if (!idEmpresa.equals(other.idEmpresa))
			return false;
		if (idFilial == null) {
			if (other.idFilial != null)
				return false;
		} else if (!idFilial.equals(other.idFilial))
			return false;
		if (nomeTabela == null) {
			if (other.nomeTabela != null)
				return false;
		} else if (!nomeTabela.equals(other.nomeTabela))
			return false;
		return true;
	}

		
	
	

}
