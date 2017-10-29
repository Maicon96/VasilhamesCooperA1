package com.g3sistemas.vasilhames.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.g3sistemas.converters.jackson.LocalDateTimeDeserializer;
import com.g3sistemas.converters.jackson.LocalDateTimeSerializer;
import com.g3sistemas.geral.entity.Empresa;
import com.g3sistemas.geral.entity.Filial;
import com.g3sistemas.interfaces.Registro;
import com.g3sistemas.sistema.entity.Usuario;
import com.querydsl.core.annotations.QueryProjection;

@Table(name="vas_ene_ite_baixas")
@Entity
@EntityListeners({VasilhameEntregueItemBaixaListener.class})
public class VasilhameEntregueItemBaixa implements Serializable, Registro<VasilhameEntregueItemBaixaId> {
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId	
	private VasilhameEntregueItemBaixaId id;	
	
	
	@Column(name="id_empresa", insertable = false, updatable = false)
	@JsonInclude(value = Include.NON_NULL)
	private Integer idEmpresa;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_empresa", insertable = false, updatable = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@JsonInclude(value = Include.NON_NULL)
	private Empresa empresa;
	
	
	@Column(name="id_filial", updatable = false, insertable = false)
	@JsonInclude(value = Include.NON_NULL)
	private Integer idFilial;
	
	@ManyToOne(fetch=FetchType.LAZY)	
	@JoinColumns({
	  @JoinColumn(name = "id_empresa",  referencedColumnName="empresa_gefili", insertable = false, updatable = false),
	  @JoinColumn(name = "id_filial",  referencedColumnName="codigo_gefili", insertable = false, updatable = false)			
	})
	@JsonInclude(value = Include.NON_NULL)
	private Filial filial;
	
	
	@Column(name="id", updatable = false, insertable = false)
	@JsonInclude(value = Include.NON_NULL)
	private Integer idCodigo;
	
	
	@Column(name="id_item")
	@JsonInclude(value = Include.NON_NULL)
	private Integer idVasilhameEntregueItem;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa", insertable = false, updatable = false),
		@JoinColumn(name = "id_filial", referencedColumnName = "id_filial", insertable = false, updatable = false),
		@JoinColumn(name = "id_item", referencedColumnName = "id", insertable = false, updatable = false)
	})
	@JsonInclude(value = Include.NON_NULL)
//	@JsonBackReference(value="vasilhameEntregueItemBaixa")
	private VasilhameEntregueItem vasilhameEntregueItem;
	
	
	@Column(name="tp_baixa")
	@JsonInclude(value = Include.NON_NULL)
	private Integer tipoBaixa;

	@Column(name="vl_quantidade")
	@JsonInclude(value = Include.NON_NULL)
	private Double quantidade;	
	
	@Column(name = "dt_gravacao")
	@JsonInclude(value = Include.NON_NULL)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime dataHoraGravacao;
	
	
	@Column(name="id_usuario")
	@JsonInclude(value = Include.NON_NULL)
	private String idUsuario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario", insertable = false, updatable = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@JsonInclude(value = Include.NON_NULL)
	private Usuario usuario;	
	
	@Transient
	@JsonInclude(value = Include.NON_NULL)
	private Double quantidadeBaixada;
	
	
	public VasilhameEntregueItemBaixa() {
		
	}
	
	
	@QueryProjection
	public VasilhameEntregueItemBaixa(VasilhameEntregueItemBaixaId id, Integer idEmpresa, Empresa empresa,
			Integer idFilial, Filial filial, Integer idCodigo, Integer idVasilhameEntregueItem, Integer tipoBaixa, Double quantidade,
			LocalDateTime dataHoraGravacao, String idUsuario, Usuario usuario) {
		this.id = id;
		this.idEmpresa = idEmpresa;
		this.empresa = empresa;
		this.idFilial = idFilial;
		this.filial = filial;
		this.idCodigo = idCodigo;
		this.idVasilhameEntregueItem = idVasilhameEntregueItem;
		this.tipoBaixa = tipoBaixa;
		this.quantidade = quantidade;
		this.dataHoraGravacao = dataHoraGravacao;
		this.idUsuario = idUsuario;
		this.usuario = usuario;
	}
	
	@QueryProjection
	public VasilhameEntregueItemBaixa(VasilhameEntregueItemBaixaId id, Integer idEmpresa, Empresa empresa,
			Integer idFilial, Filial filial, Integer idCodigo, Integer idVasilhameEntregueItem, 
			VasilhameEntregueItem vasilhameEntregueItem, Integer tipoBaixa, Double quantidade,
			LocalDateTime dataHoraGravacao, String idUsuario, Usuario usuario) {
		this.id = id;
		this.idEmpresa = idEmpresa;
		this.empresa = empresa;
		this.idFilial = idFilial;
		this.filial = filial;
		this.idCodigo = idCodigo;
		this.idVasilhameEntregueItem = idVasilhameEntregueItem;
		this.vasilhameEntregueItem = vasilhameEntregueItem;
		this.tipoBaixa = tipoBaixa;
		this.quantidade = quantidade;
		this.dataHoraGravacao = dataHoraGravacao;
		this.idUsuario = idUsuario;
		this.usuario = usuario;
	}
	
	@QueryProjection
	public VasilhameEntregueItemBaixa(Integer idCodigo, Integer idVasilhameEntregueItem, 
			VasilhameEntregueItem vasilhameEntregueItem, Integer tipoBaixa) {		
		this.idCodigo = idCodigo;
		this.idVasilhameEntregueItem = idVasilhameEntregueItem;
		this.vasilhameEntregueItem = vasilhameEntregueItem;
		this.tipoBaixa = tipoBaixa;				
	}

	@QueryProjection
	public VasilhameEntregueItemBaixa(Double quantidade, VasilhameEntregueItem vasilhameEntregueItem) {		
		this.quantidade = quantidade;
		this.vasilhameEntregueItem = vasilhameEntregueItem;
	}
	
	@QueryProjection
	public VasilhameEntregueItemBaixa(VasilhameEntregueItem vasilhameEntregueItem) {		
		this.vasilhameEntregueItem = vasilhameEntregueItem;		
	}
	
	
	public VasilhameEntregueItemBaixaId getId() {
		return id;
	}

	public void setId(VasilhameEntregueItemBaixaId id) {
		this.id = id;
	}

	public Integer getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Integer idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Integer getIdFilial() {
		return idFilial;
	}

	public void setIdFilial(Integer idFilial) {
		this.idFilial = idFilial;
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public Integer getIdCodigo() {
		return idCodigo;
	}

	public void setIdCodigo(Integer idCodigo) {
		this.idCodigo = idCodigo;
	}	

	public Integer getIdVasilhameEntregueItem() {
		return idVasilhameEntregueItem;
	}

	public void setIdVasilhameEntregueItem(Integer idVasilhameEntregueItem) {
		this.idVasilhameEntregueItem = idVasilhameEntregueItem;
	}

	public VasilhameEntregueItem getVasilhameEntregueItem() {
		return vasilhameEntregueItem;
	}

	public void setVasilhameEntregueItem(VasilhameEntregueItem vasilhameEntregueItem) {
		this.vasilhameEntregueItem = vasilhameEntregueItem;
	}

	public Integer getTipoBaixa() {
		return tipoBaixa;
	}

	public void setTipoBaixa(Integer tipoBaixa) {
		this.tipoBaixa = tipoBaixa;
	}

	public Double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}

	public LocalDateTime getDataHoraGravacao() {
		return dataHoraGravacao;
	}

	public void setDataHoraGravacao(LocalDateTime dataHoraGravacao) {
		this.dataHoraGravacao = dataHoraGravacao;
	}

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}	

	public Double getQuantidadeBaixada() {
		return quantidadeBaixada;
	}

	public void setQuantidadeBaixada(Double quantidadeBaixada) {
		this.quantidadeBaixada = quantidadeBaixada;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataHoraGravacao == null) ? 0 : dataHoraGravacao.hashCode());
		result = prime * result + ((empresa == null) ? 0 : empresa.hashCode());
		result = prime * result + ((filial == null) ? 0 : filial.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idCodigo == null) ? 0 : idCodigo.hashCode());
		result = prime * result + ((idEmpresa == null) ? 0 : idEmpresa.hashCode());
		result = prime * result + ((idFilial == null) ? 0 : idFilial.hashCode());
		result = prime * result + ((idVasilhameEntregueItem == null) ? 0 : idVasilhameEntregueItem.hashCode());
		result = prime * result + ((idUsuario == null) ? 0 : idUsuario.hashCode());
		result = prime * result + ((quantidade == null) ? 0 : quantidade.hashCode());
		result = prime * result + ((tipoBaixa == null) ? 0 : tipoBaixa.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		result = prime * result + ((vasilhameEntregueItem == null) ? 0 : vasilhameEntregueItem.hashCode());
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
		VasilhameEntregueItemBaixa other = (VasilhameEntregueItemBaixa) obj;
		if (dataHoraGravacao == null) {
			if (other.dataHoraGravacao != null)
				return false;
		} else if (!dataHoraGravacao.equals(other.dataHoraGravacao))
			return false;
		if (empresa == null) {
			if (other.empresa != null)
				return false;
		} else if (!empresa.equals(other.empresa))
			return false;
		if (filial == null) {
			if (other.filial != null)
				return false;
		} else if (!filial.equals(other.filial))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
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
		if (idVasilhameEntregueItem == null) {
			if (other.idVasilhameEntregueItem != null)
				return false;
		} else if (!idVasilhameEntregueItem.equals(other.idVasilhameEntregueItem))
			return false;
		if (idUsuario == null) {
			if (other.idUsuario != null)
				return false;
		} else if (!idUsuario.equals(other.idUsuario))
			return false;
		if (quantidade == null) {
			if (other.quantidade != null)
				return false;
		} else if (!quantidade.equals(other.quantidade))
			return false;
		if (tipoBaixa == null) {
			if (other.tipoBaixa != null)
				return false;
		} else if (!tipoBaixa.equals(other.tipoBaixa))
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		if (vasilhameEntregueItem == null) {
			if (other.vasilhameEntregueItem != null)
				return false;
		} else if (!vasilhameEntregueItem.equals(other.vasilhameEntregueItem))
			return false;
		return true;
	}	
	
		
	
}