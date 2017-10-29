package com.g3sistemas.vasilhames.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.g3sistemas.converters.jackson.LocalDateDeserializer;
import com.g3sistemas.converters.jackson.LocalDateSerializer;
import com.g3sistemas.geral.entity.Empresa;
import com.g3sistemas.geral.entity.Filial;
import com.g3sistemas.interfaces.Registro;
import com.querydsl.core.annotations.QueryProjection;

@Table(name="vas_ene_ite_bai_cupons")
@Entity
public class VasilhameEntregueItemBaixaCupom implements Registro<VasilhameEntregueItemBaixaCupomId> {
		
	
	@EmbeddedId	
	private VasilhameEntregueItemBaixaCupomId id;
	
	
	@Column(name="id_empresa_baixa", insertable = false, updatable = false)
	@JsonInclude(value = Include.NON_NULL)
	private Integer idEmpresaBaixa;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_empresa_baixa", insertable = false, updatable = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@JsonInclude(value = Include.NON_NULL)
	private Empresa empresaBaixa;
	
	
	@Column(name="id_filial_baixa", insertable = false, updatable = false)
	@JsonInclude(value = Include.NON_NULL)
	private Integer idFilialBaixa;
	
	@ManyToOne(fetch=FetchType.LAZY)	
	@JoinColumns({
	  @JoinColumn(name = "id_empresa_baixa",  referencedColumnName="empresa_gefili", insertable = false, updatable = false),
	  @JoinColumn(name = "id_filial_baixa",  referencedColumnName="codigo_gefili", insertable = false, updatable = false)			
	})
	@JsonInclude(value = Include.NON_NULL)
	private Filial filialBaixa;
		
	
	@Column(name="id_baixa", insertable = false, updatable = false)
	@JsonInclude(value = Include.NON_NULL)
	private Integer idVasilhameEntregueItemBaixa;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "id_baixa", referencedColumnName = "id", insertable = false, updatable = false),
		@JoinColumn(name = "id_empresa_baixa", referencedColumnName = "id_empresa", insertable = false, updatable = false),
		@JoinColumn(name = "id_filial_baixa", referencedColumnName = "id_filial", insertable = false, updatable = false) 
	})
	@JsonInclude(value = Include.NON_NULL)
	private VasilhameEntregueItemBaixa vasilhameEntregueItemBaixa;
	
	
	@Column(name="id_empresa_cupom", insertable = false, updatable = false)
	@JsonInclude(value = Include.NON_NULL)
	private Integer idEmpresaCupom;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_empresa_cupom", insertable = false, updatable = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@JsonInclude(value = Include.NON_NULL)
	private Empresa empresaCupom;		
	
	
	@Column(name="id_filial_cupom", insertable = false, updatable = false)
	@JsonInclude(value = Include.NON_NULL)
	private Integer idFilialCupom;
	
	@ManyToOne(fetch=FetchType.LAZY)	
	@JoinColumns({
	  @JoinColumn(name = "id_empresa_cupom",  referencedColumnName="empresa_gefili", insertable = false, updatable = false),
	  @JoinColumn(name = "id_filial_cupom",  referencedColumnName="codigo_gefili", insertable = false, updatable = false)			
	})
	@JsonInclude(value = Include.NON_NULL)
	private Filial filialCupom;
	
	
	@Column(name = "id_ecf_cupom", updatable = false, insertable = false)
	@JsonInclude(value = Include.NON_NULL)
	private Integer idEcfCupom;

	@Column(name="nu_coo_cupom", insertable = false, updatable = false)
	@JsonInclude(value = Include.NON_NULL)
	private Integer numero;
	
	@Column(name = "dt_emissao_cupom", insertable = false, updatable = false)
	@JsonInclude(value = Include.NON_NULL)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate dataEmissao;
	
	@Column(name="nu_sequencia_cupom", insertable = false, updatable = false)
	@JsonInclude(value = Include.NON_NULL)
	private Integer sequencia;	
	
	
	public VasilhameEntregueItemBaixaCupom() {
		
	}	

	
	@QueryProjection
	public VasilhameEntregueItemBaixaCupom(Integer idEmpresaBaixa, Empresa empresaBaixa, Integer idFilialBaixa,
			Filial filialBaixa, Integer idVasilhameEntregueItemBaixa,
			Integer idEmpresaCupom, Empresa empresaCupom,
			Integer idFilialCupom, Filial filialCupom, Integer idEcfCupom, Integer numero, LocalDate dataEmissao,
			Integer sequencia) {
		this.idEmpresaBaixa = idEmpresaBaixa;
		this.empresaBaixa = empresaBaixa;
		this.idFilialBaixa = idFilialBaixa;
		this.filialBaixa = filialBaixa;
		this.idVasilhameEntregueItemBaixa = idVasilhameEntregueItemBaixa;
		this.idEmpresaCupom = idEmpresaCupom;
		this.empresaCupom = empresaCupom;
		this.idFilialCupom = idFilialCupom;
		this.filialCupom = filialCupom;
		this.idEcfCupom = idEcfCupom;
		this.numero = numero;
		this.dataEmissao = dataEmissao;
		this.sequencia = sequencia;
	}
	
	@QueryProjection
	public VasilhameEntregueItemBaixaCupom(Integer idEmpresaBaixa, Empresa empresaBaixa, Integer idFilialBaixa,
			Filial filialBaixa, Integer idVasilhameEntregueItemBaixa,
			VasilhameEntregueItemBaixa vasilhameEntregueItemBaixa, Integer idEmpresaCupom, Empresa empresaCupom,
			Integer idFilialCupom, Filial filialCupom, Integer idEcfCupom, Integer numero, LocalDate dataEmissao,
			Integer sequencia) {
		this.idEmpresaBaixa = idEmpresaBaixa;
		this.empresaBaixa = empresaBaixa;
		this.idFilialBaixa = idFilialBaixa;
		this.filialBaixa = filialBaixa;
		this.idVasilhameEntregueItemBaixa = idVasilhameEntregueItemBaixa;
		this.vasilhameEntregueItemBaixa = vasilhameEntregueItemBaixa;
		this.idEmpresaCupom = idEmpresaCupom;
		this.empresaCupom = empresaCupom;
		this.idFilialCupom = idFilialCupom;
		this.filialCupom = filialCupom;
		this.idEcfCupom = idEcfCupom;
		this.numero = numero;
		this.dataEmissao = dataEmissao;
		this.sequencia = sequencia;
	}


	public VasilhameEntregueItemBaixaCupomId getId() {
		return id;
	}

	public void setId(VasilhameEntregueItemBaixaCupomId id) {
		this.id = id;
	}

	public Integer getIdEmpresaBaixa() {
		return idEmpresaBaixa;
	}

	public void setIdEmpresaBaixa(Integer idEmpresaBaixa) {
		this.idEmpresaBaixa = idEmpresaBaixa;
	}

	public Empresa getEmpresaBaixa() {
		return empresaBaixa;
	}

	public void setEmpresaBaixa(Empresa empresaBaixa) {
		this.empresaBaixa = empresaBaixa;
	}

	public Integer getIdFilialBaixa() {
		return idFilialBaixa;
	}

	public void setIdFilialBaixa(Integer idFilialBaixa) {
		this.idFilialBaixa = idFilialBaixa;
	}

	public Filial getFilialBaixa() {
		return filialBaixa;
	}

	public void setFilialBaixa(Filial filialBaixa) {
		this.filialBaixa = filialBaixa;
	}

	public Integer getIdVasilhameEntregueItemBaixa() {
		return idVasilhameEntregueItemBaixa;
	}

	public void setIdVasilhameEntregueItemBaixa(Integer idVasilhameEntregueItemBaixa) {
		this.idVasilhameEntregueItemBaixa = idVasilhameEntregueItemBaixa;
	}

	public VasilhameEntregueItemBaixa getVasilhameEntregueItemBaixa() {
		return vasilhameEntregueItemBaixa;
	}

	public void setVasilhameEntregueItemBaixa(VasilhameEntregueItemBaixa vasilhameEntregueItemBaixa) {
		this.vasilhameEntregueItemBaixa = vasilhameEntregueItemBaixa;
	}

	public Integer getIdEmpresaCupom() {
		return idEmpresaCupom;
	}
	
	public void setIdEmpresaCupom(Integer idEmpresaCupom) {
		this.idEmpresaCupom = idEmpresaCupom;
	}

	public Empresa getEmpresaCupom() {
		return empresaCupom;
	}

	public void setEmpresaCupom(Empresa empresaCupom) {
		this.empresaCupom = empresaCupom;
	}

	public Integer getIdFilialCupom() {
		return idFilialCupom;
	}

	public void setIdFilialCupom(Integer idFilialCupom) {
		this.idFilialCupom = idFilialCupom;
	}

	public Filial getFilialCupom() {
		return filialCupom;
	}

	public void setFilialCupom(Filial filialCupom) {
		this.filialCupom = filialCupom;
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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataEmissao == null) ? 0 : dataEmissao.hashCode());		
		result = prime * result + ((empresaBaixa == null) ? 0 : empresaBaixa.hashCode());
		result = prime * result + ((empresaCupom == null) ? 0 : empresaCupom.hashCode());
		result = prime * result + ((filialBaixa == null) ? 0 : filialBaixa.hashCode());
		result = prime * result + ((filialCupom == null) ? 0 : filialCupom.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idEcfCupom == null) ? 0 : idEcfCupom.hashCode());
		result = prime * result + ((idEmpresaBaixa == null) ? 0 : idEmpresaBaixa.hashCode());
		result = prime * result + ((idEmpresaCupom == null) ? 0 : idEmpresaCupom.hashCode());
		result = prime * result + ((idFilialBaixa == null) ? 0 : idFilialBaixa.hashCode());
		result = prime * result + ((idFilialCupom == null) ? 0 : idFilialCupom.hashCode());
		result = prime * result
				+ ((idVasilhameEntregueItemBaixa == null) ? 0 : idVasilhameEntregueItemBaixa.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		result = prime * result + ((sequencia == null) ? 0 : sequencia.hashCode());
		result = prime * result + ((vasilhameEntregueItemBaixa == null) ? 0 : vasilhameEntregueItemBaixa.hashCode());
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
		VasilhameEntregueItemBaixaCupom other = (VasilhameEntregueItemBaixaCupom) obj;
		if (dataEmissao == null) {
			if (other.dataEmissao != null)
				return false;
		} else if (!dataEmissao.equals(other.dataEmissao))
			return false;		
		if (empresaBaixa == null) {
			if (other.empresaBaixa != null)
				return false;
		} else if (!empresaBaixa.equals(other.empresaBaixa))
			return false;
		if (empresaCupom == null) {
			if (other.empresaCupom != null)
				return false;
		} else if (!empresaCupom.equals(other.empresaCupom))
			return false;
		if (filialBaixa == null) {
			if (other.filialBaixa != null)
				return false;
		} else if (!filialBaixa.equals(other.filialBaixa))
			return false;
		if (filialCupom == null) {
			if (other.filialCupom != null)
				return false;
		} else if (!filialCupom.equals(other.filialCupom))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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
		if (vasilhameEntregueItemBaixa == null) {
			if (other.vasilhameEntregueItemBaixa != null)
				return false;
		} else if (!vasilhameEntregueItemBaixa.equals(other.vasilhameEntregueItemBaixa))
			return false;
		return true;
	}	
	
	
	
}