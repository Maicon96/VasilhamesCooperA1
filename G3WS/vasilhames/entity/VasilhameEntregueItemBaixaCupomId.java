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
public class VasilhameEntregueItemBaixaCupomId implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Column(name="id_empresa_baixa")
	private Integer idEmpresaBaixa;
	
	@Column(name="id_filial_baixa")
	private Integer idFilialBaixa;
	
	@Column(name="id_baixa")
	private Integer idVasilhameEntregueItemBaixa;	
	
	@Column(name="id_empresa_cupom")
	private Integer idEmpresaCupom;
	
	@Column(name="id_filial_cupom")
	private Integer idFilialCupom;		
	
	@Column(name = "id_ecf_cupom")
	private Integer idEcfCupom;
	
	@Column(name="nu_coo_cupom")
	private Integer numero;
	
	@Column(name = "dt_emissao_cupom")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate dataEmissao;
	
	@Column(name="nu_sequencia_cupom")
	private Integer sequencia;	
	
	public VasilhameEntregueItemBaixaCupomId(){
	
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

	public Integer getIdEmpresaCupom() {
		return idEmpresaCupom;
	}

	public void setIdEmpresaCupom(Integer idEmpresaCupom) {
		this.idEmpresaCupom = idEmpresaCupom;
	}

	public Integer getIdFilialCupom() {
		return idFilialCupom;
	}

	public void setIdFilialCupom(Integer idFilialCupom) {
		this.idFilialCupom = idFilialCupom;
	}

	public Integer getIdEcfCupom() {
		return idEcfCupom;
	}

	public void setIdEcfCupom(Integer idEcfCupom) {
		this.idEcfCupom = idEcfCupom;
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
		result = prime * result + ((idEcfCupom == null) ? 0 : idEcfCupom.hashCode());
		result = prime * result + ((idEmpresaBaixa == null) ? 0 : idEmpresaBaixa.hashCode());
		result = prime * result + ((idEmpresaCupom == null) ? 0 : idEmpresaCupom.hashCode());
		result = prime * result + ((idFilialBaixa == null) ? 0 : idFilialBaixa.hashCode());
		result = prime * result + ((idFilialCupom == null) ? 0 : idFilialCupom.hashCode());
		result = prime * result
				+ ((idVasilhameEntregueItemBaixa == null) ? 0 : idVasilhameEntregueItemBaixa.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		result = prime * result + ((sequencia == null) ? 0 : sequencia.hashCode());
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
		VasilhameEntregueItemBaixaCupomId other = (VasilhameEntregueItemBaixaCupomId) obj;
		if (dataEmissao == null) {
			if (other.dataEmissao != null)
				return false;
		} else if (!dataEmissao.equals(other.dataEmissao))
			return false;
		if (idEcfCupom == null) {
			if (other.idEcfCupom != null)
				return false;
		} else if (!idEcfCupom.equals(other.idEcfCupom))
			return false;
		if (idEmpresaBaixa == null) {
			if (other.idEmpresaBaixa != null)
				return false;
		} else if (!idEmpresaBaixa.equals(other.idEmpresaBaixa))
			return false;
		if (idEmpresaCupom == null) {
			if (other.idEmpresaCupom != null)
				return false;
		} else if (!idEmpresaCupom.equals(other.idEmpresaCupom))
			return false;
		if (idFilialBaixa == null) {
			if (other.idFilialBaixa != null)
				return false;
		} else if (!idFilialBaixa.equals(other.idFilialBaixa))
			return false;
		if (idFilialCupom == null) {
			if (other.idFilialCupom != null)
				return false;
		} else if (!idFilialCupom.equals(other.idFilialCupom))
			return false;
		if (idVasilhameEntregueItemBaixa == null) {
			if (other.idVasilhameEntregueItemBaixa != null)
				return false;
		} else if (!idVasilhameEntregueItemBaixa.equals(other.idVasilhameEntregueItemBaixa))
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
		return true;
	}	
	
}
