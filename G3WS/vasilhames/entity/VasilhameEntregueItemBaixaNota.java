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

@Table(name="vas_ene_ite_bai_notas")
@Entity
public class VasilhameEntregueItemBaixaNota implements Registro<VasilhameEntregueItemBaixaNotaId> {
		
	
	@EmbeddedId	
	private VasilhameEntregueItemBaixaNotaId id;
	
	
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
	
	
	@Column(name="id_empresa_nota", insertable = false, updatable = false)
	@JsonInclude(value = Include.NON_NULL)
	private Integer idEmpresaNota;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_empresa_nota", insertable = false, updatable = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@JsonInclude(value = Include.NON_NULL)
	private Empresa empresaNota;	
	
	
	@Column(name="tp_modelo_nota", insertable = false, updatable = false)
	@JsonInclude(value = Include.NON_NULL)
	private String modelo;		
	
	
	@Column(name="id_filial_nota", insertable = false, updatable = false)
	@JsonInclude(value = Include.NON_NULL)
	private Integer idFilialNota;
	
	@ManyToOne(fetch=FetchType.LAZY)	
	@JoinColumns({
	  @JoinColumn(name = "id_empresa_nota",  referencedColumnName="empresa_gefili", insertable = false, updatable = false),
	  @JoinColumn(name = "id_filial_nota",  referencedColumnName="codigo_gefili", insertable = false, updatable = false)			
	})
	@JsonInclude(value = Include.NON_NULL)
	private Filial filialNota;
	
	
	@Column(name="nu_serie_nota", insertable = false, updatable = false)
	@JsonInclude(value = Include.NON_NULL)
	private Integer serie;
	
	@Column(name="nu_numero_nota", insertable = false, updatable = false)
	@JsonInclude(value = Include.NON_NULL)
	private Integer numero;
	
	@Column(name = "dt_emissao_nota", insertable = false, updatable = false)
	@JsonInclude(value = Include.NON_NULL)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate dataEmissao;
	
	@Column(name="nu_sequencia_nota", insertable = false, updatable = false)
	@JsonInclude(value = Include.NON_NULL)
	private Integer sequencia;
	
	
	public VasilhameEntregueItemBaixaNota() {
		
	}
	
	
	@QueryProjection
	public VasilhameEntregueItemBaixaNota(Integer idEmpresaBaixa, Empresa empresaBaixa,
			Integer idFilialBaixa, Filial filialBaixa, Integer idVasilhameEntregueItemBaixa, Integer idEmpresaNota,
			Empresa empresaNota, String modelo, Integer idFilialNota, Filial filialNota, Integer serie, Integer numero,
			LocalDate dataEmissao, Integer sequencia) {		
		this.idEmpresaBaixa = idEmpresaBaixa;
		this.empresaBaixa = empresaBaixa;
		this.idFilialBaixa = idFilialBaixa;
		this.filialBaixa = filialBaixa;
		this.idVasilhameEntregueItemBaixa = idVasilhameEntregueItemBaixa;
		this.idEmpresaNota = idEmpresaNota;
		this.empresaNota = empresaNota;
		this.modelo = modelo;
		this.idFilialNota = idFilialNota;
		this.filialNota = filialNota;
		this.serie = serie;
		this.numero = numero;
		this.dataEmissao = dataEmissao;
		this.sequencia = sequencia;
	}
	
	
	@QueryProjection
	public VasilhameEntregueItemBaixaNota(Integer idEmpresaBaixa, Empresa empresaBaixa, Integer idFilialBaixa,
			Filial filialBaixa, Integer idVasilhameEntregueItemBaixa,
			VasilhameEntregueItemBaixa vasilhameEntregueItemBaixa, Integer idEmpresaNota, Empresa empresaNota,
			String modelo, Integer idFilialNota, Filial filialNota, Integer serie, Integer numero,
			LocalDate dataEmissao, Integer sequencia) {
		this.idEmpresaBaixa = idEmpresaBaixa;
		this.empresaBaixa = empresaBaixa;
		this.idFilialBaixa = idFilialBaixa;
		this.filialBaixa = filialBaixa;
		this.idVasilhameEntregueItemBaixa = idVasilhameEntregueItemBaixa;
		this.vasilhameEntregueItemBaixa = vasilhameEntregueItemBaixa;
		this.idEmpresaNota = idEmpresaNota;
		this.empresaNota = empresaNota;
		this.modelo = modelo;
		this.idFilialNota = idFilialNota;
		this.filialNota = filialNota;
		this.serie = serie;
		this.numero = numero;
		this.dataEmissao = dataEmissao;
		this.sequencia = sequencia;
	}
	
	
	public VasilhameEntregueItemBaixaNotaId getId() {
		return id;
	}

	public void setId(VasilhameEntregueItemBaixaNotaId id) {
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

	public Integer getIdEmpresaNota() {
		return idEmpresaNota;
	}

	public void setIdEmpresaNota(Integer idEmpresaNota) {
		this.idEmpresaNota = idEmpresaNota;
	}

	public Empresa getEmpresaNota() {
		return empresaNota;
	}

	public void setEmpresaNota(Empresa empresaNota) {
		this.empresaNota = empresaNota;
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

	public Filial getFilialNota() {
		return filialNota;
	}

	public void setFilialNota(Filial filialNota) {
		this.filialNota = filialNota;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataEmissao == null) ? 0 : dataEmissao.hashCode());
		result = prime * result + ((empresaBaixa == null) ? 0 : empresaBaixa.hashCode());
		result = prime * result + ((empresaNota == null) ? 0 : empresaNota.hashCode());
		result = prime * result + ((filialBaixa == null) ? 0 : filialBaixa.hashCode());
		result = prime * result + ((filialNota == null) ? 0 : filialNota.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		VasilhameEntregueItemBaixaNota other = (VasilhameEntregueItemBaixaNota) obj;
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
		if (empresaNota == null) {
			if (other.empresaNota != null)
				return false;
		} else if (!empresaNota.equals(other.empresaNota))
			return false;
		if (filialBaixa == null) {
			if (other.filialBaixa != null)
				return false;
		} else if (!filialBaixa.equals(other.filialBaixa))
			return false;
		if (filialNota == null) {
			if (other.filialNota != null)
				return false;
		} else if (!filialNota.equals(other.filialNota))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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
		if (vasilhameEntregueItemBaixa == null) {
			if (other.vasilhameEntregueItemBaixa != null)
				return false;
		} else if (!vasilhameEntregueItemBaixa.equals(other.vasilhameEntregueItemBaixa))
			return false;
		return true;
	}	
	
}