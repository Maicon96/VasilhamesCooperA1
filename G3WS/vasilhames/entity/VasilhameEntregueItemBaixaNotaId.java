package com.g3sistemas.vasilhames.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.g3sistemas.converters.jackson.LocalDateDeserializer;
import com.g3sistemas.converters.jackson.LocalDateSerializer;

@Embeddable
public class VasilhameEntregueItemBaixaNotaId implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Column(name="id_empresa_baixa")	
	private Integer idEmpresaBaixa;
	
	@Column(name="id_filial_baixa")	
	private Integer idFilialBaixa;
		
	@Column(name="id_baixa")	
	private Integer idVasilhameEntregueItemBaixa;
		
	@Column(name="id_empresa_nota")	
	private Integer idEmpresaNota;
	
	@Column(name="tp_modelo_nota")	
	private String modelo;		
		
	@Column(name="id_filial_nota")	
	private Integer idFilialNota;
	
	@Column(name="nu_serie_nota")	
	private Integer serie;
	
	@Column(name="nu_numero_nota")	
	private Integer numero;
	
	@Column(name = "dt_emissao_nota")	
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate dataEmissao;
	
	@Column(name="nu_sequencia_nota")	
	private Integer sequencia;	
	
	
	public VasilhameEntregueItemBaixaNotaId(){
	
	}
	

	public Integer getIdEmpresaBaixa() {
		return idEmpresaBaixa;
	}

	public void setIdEmpresaBaixa(Integer idEmpresaBaixa) {
		this.idEmpresaBaixa = idEmpresaBaixa;
	}

	public Integer getIdFilialBaixa() {
		return idFilialBaixa;
	}

	public void setIdFilialBaixa(Integer idFilialBaixa) {
		this.idFilialBaixa = idFilialBaixa;
	}

	public Integer getIdVasilhameEntregueItemBaixa() {
		return idVasilhameEntregueItemBaixa;
	}

	public void setIdVasilhameEntregueItemBaixa(Integer idVasilhameEntregueItemBaixa) {
		this.idVasilhameEntregueItemBaixa = idVasilhameEntregueItemBaixa;
	}

	public Integer getIdEmpresaNota() {
		return idEmpresaNota;
	}

	public void setIdEmpresaNota(Integer idEmpresaNota) {
		this.idEmpresaNota = idEmpresaNota;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public Integer getIdFilialNota() {
		return idFilialNota;
	}

	public void setIdFilialNota(Integer idFilialNota) {
		this.idFilialNota = idFilialNota;
	}

	public Integer getSerie() {
		return serie;
	}

	public void setSerie(Integer serie) {
		this.serie = serie;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public LocalDate getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(LocalDate dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public Integer getSequencia() {
		return sequencia;
	}

	public void setSequencia(Integer sequencia) {
		this.sequencia = sequencia;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataEmissao == null) ? 0 : dataEmissao.hashCode());
		result = prime * result + ((idEmpresaBaixa == null) ? 0 : idEmpresaBaixa.hashCode());
		result = prime * result + ((idEmpresaNota == null) ? 0 : idEmpresaNota.hashCode());
		result = prime * result + ((idFilialBaixa == null) ? 0 : idFilialBaixa.hashCode());
		result = prime * result + ((idFilialNota == null) ? 0 : idFilialNota.hashCode());
		result = prime * result
				+ ((idVasilhameEntregueItemBaixa == null) ? 0 : idVasilhameEntregueItemBaixa.hashCode());
		result = prime * result + ((modelo == null) ? 0 : modelo.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		result = prime * result + ((sequencia == null) ? 0 : sequencia.hashCode());
		result = prime * result + ((serie == null) ? 0 : serie.hashCode());
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
		VasilhameEntregueItemBaixaNotaId other = (VasilhameEntregueItemBaixaNotaId) obj;
		if (dataEmissao == null) {
			if (other.dataEmissao != null)
				return false;
		} else if (!dataEmissao.equals(other.dataEmissao))
			return false;
		if (idEmpresaBaixa == null) {
			if (other.idEmpresaBaixa != null)
				return false;
		} else if (!idEmpresaBaixa.equals(other.idEmpresaBaixa))
			return false;
		if (idEmpresaNota == null) {
			if (other.idEmpresaNota != null)
				return false;
		} else if (!idEmpresaNota.equals(other.idEmpresaNota))
			return false;
		if (idFilialBaixa == null) {
			if (other.idFilialBaixa != null)
				return false;
		} else if (!idFilialBaixa.equals(other.idFilialBaixa))
			return false;
		if (idFilialNota == null) {
			if (other.idFilialNota != null)
				return false;
		} else if (!idFilialNota.equals(other.idFilialNota))
			return false;
		if (idVasilhameEntregueItemBaixa == null) {
			if (other.idVasilhameEntregueItemBaixa != null)
				return false;
		} else if (!idVasilhameEntregueItemBaixa.equals(other.idVasilhameEntregueItemBaixa))
			return false;
		if (modelo == null) {
			if (other.modelo != null)
				return false;
		} else if (!modelo.equals(other.modelo))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		if (sequencia == null) {
			if (other.sequencia != null)
				return false;
		} else if (!sequencia.equals(other.sequencia))
			return false;
		if (serie == null) {
			if (other.serie != null)
				return false;
		} else if (!serie.equals(other.serie))
			return false;
		return true;
	}		
	
}
