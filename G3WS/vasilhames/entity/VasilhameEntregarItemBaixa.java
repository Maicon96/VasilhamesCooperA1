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

@Table(name="vas_ena_ite_baixas")
@Entity
@EntityListeners({VasilhameEntregarItemBaixaListener.class})
public class VasilhameEntregarItemBaixa implements Serializable, Registro<VasilhameEntregarItemBaixaId> {
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId	
	private VasilhameEntregarItemBaixaId id;	
	
	
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
	private Integer idVasilhameEntregarItem;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "id_item", referencedColumnName = "id", insertable = false, updatable = false),
		@JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa", insertable = false, updatable = false),
		@JoinColumn(name = "id_filial", referencedColumnName = "id_filial", insertable = false, updatable = false) 
	})
	@JsonInclude(value = Include.NON_NULL)
	private VasilhameEntregarItem vasilhameEntregarItem;
	
	
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
	
	
	public VasilhameEntregarItemBaixa() {
		
	}	
	
	
	@QueryProjection
	public VasilhameEntregarItemBaixa(VasilhameEntregarItemBaixaId id, Integer idEmpresa, Empresa empresa,
			Integer idFilial, Filial filial, Integer idCodigo, Integer idVasilhameEntregarItem, Integer tipoBaixa,
			Double quantidade, LocalDateTime dataHoraGravacao, String idUsuario, Usuario usuario) {
		this.id = id;
		this.idEmpresa = idEmpresa;
		this.empresa = empresa;
		this.idFilial = idFilial;
		this.filial = filial;
		this.idCodigo = idCodigo;
		this.idVasilhameEntregarItem = idVasilhameEntregarItem;
		this.tipoBaixa = tipoBaixa;
		this.quantidade = quantidade;
		this.dataHoraGravacao = dataHoraGravacao;
		this.idUsuario = idUsuario;
		this.usuario = usuario;
	}
	
	@QueryProjection
	public VasilhameEntregarItemBaixa(VasilhameEntregarItemBaixaId id, Integer idEmpresa, Empresa empresa,
			Integer idFilial, Filial filial, Integer idCodigo, Integer idVasilhameEntregarItem,
			VasilhameEntregarItem vasilhameEntregarItem, Integer tipoBaixa, Double quantidade,			
			LocalDateTime dataHoraGravacao, String idUsuario, Usuario usuario) {
		this.id = id;
		this.idEmpresa = idEmpresa;
		this.empresa = empresa;
		this.idFilial = idFilial;
		this.filial = filial;
		this.idCodigo = idCodigo;
		this.idVasilhameEntregarItem = idVasilhameEntregarItem;
		this.vasilhameEntregarItem = vasilhameEntregarItem;
		this.tipoBaixa = tipoBaixa;
		this.quantidade = quantidade;		
		this.dataHoraGravacao = dataHoraGravacao;
		this.idUsuario = idUsuario;
		this.usuario = usuario;
	}
	
	@QueryProjection
	public VasilhameEntregarItemBaixa(Double quantidade) {		
		this.quantidade = quantidade;		
	}

	
	public VasilhameEntregarItemBaixaId getId() {
		return id;
	}

	public void setId(VasilhameEntregarItemBaixaId id) {
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

	public Integer getIdVasilhameEntregarItem() {
		return idVasilhameEntregarItem;
	}

	public void setIdVasilhameEntregarItem(Integer idVasilhameEntregarItem) {
		this.idVasilhameEntregarItem = idVasilhameEntregarItem;
	}

	public VasilhameEntregarItem getVasilhameEntregarItem() {
		return vasilhameEntregarItem;
	}

	public void setVasilhameEntregarItem(VasilhameEntregarItem vasilhameEntregarItem) {
		this.vasilhameEntregarItem = vasilhameEntregarItem;
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
	
	
		
	
}