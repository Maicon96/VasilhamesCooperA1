package com.g3sistemas.vasilhames.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class VasilhameEntregarId implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Column(name="id_empresa")	
	private Integer idEmpresa;	
	
	@Column(name="id_filial")	
	private Integer idFilial;	
	
	@Column(name="id")	
	private Integer idCodigo;
	
	
	public VasilhameEntregarId(){
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

	public Integer getIdCodigo() {
		return idCodigo;
	}

	public void setIdCodigo(Integer idCodigo) {
		this.idCodigo = idCodigo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idCodigo == null) ? 0 : idCodigo.hashCode());
		result = prime * result + ((idEmpresa == null) ? 0 : idEmpresa.hashCode());
		result = prime * result + ((idFilial == null) ? 0 : idFilial.hashCode());
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
		VasilhameEntregarId other = (VasilhameEntregarId) obj;
		if (idCodigo == null) {
			if (other.idCodigo != null)
				return false;
		} else if (!idCodigo.equals(other.idCodigo))
			return false;
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
		return true;
	}

	
	
}
