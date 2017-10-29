package com.g3sistemas.vasilhames.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.g3sistemas.converters.jackson.LocalDateTimeDeserializer;
import com.g3sistemas.converters.jackson.LocalDateTimeSerializer;
import com.g3sistemas.estoques.entity.Produto;
import com.g3sistemas.geral.entity.Empresa;
import com.g3sistemas.geral.entity.Filial;
import com.g3sistemas.interfaces.Registro;
import com.g3sistemas.sistema.entity.Usuario;
import com.querydsl.core.annotations.QueryProjection;

@Table(name="vas_ene_itens")
@Entity
@EntityListeners({VasilhameEntregueItemListener.class})
public class VasilhameEntregueItem implements Registro<VasilhameEntregueItemId> {
	
	@EmbeddedId	
	private VasilhameEntregueItemId id;	
	
	
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
	
	
	@Column(name="id_vasilhame_entregue")
	@JsonInclude(value = Include.NON_NULL)
	private Integer idVasilhameEntregue;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa", insertable = false, updatable = false),
		@JoinColumn(name = "id_filial", referencedColumnName = "id_filial", insertable = false, updatable = false),
		@JoinColumn(name = "id_vasilhame_entregue", referencedColumnName = "id", insertable = false, updatable = false)
	})
	@JsonInclude(value = Include.NON_NULL)
	private VasilhameEntregue vasilhameEntregue;
	
	
	@Column(name="id_produto")
	@JsonInclude(value = Include.NON_NULL)
	private Integer idProduto;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumns({
		  @JoinColumn(name = "id_empresa",  referencedColumnName="empresa_esprod", insertable = false, updatable = false),
		  @JoinColumn(name = "id_produto",  referencedColumnName="codigo_esprod", insertable = false, updatable = false)			
		})
	@JsonInclude(value = Include.NON_NULL)
	private Produto produto;
	
	
	@Column(name="vl_quantidade")
	@JsonInclude(value = Include.NON_NULL)
	private Double quantidade;
	
	@Transient
	private Double quantidadeBaixada;
	
	@Transient
	private Double quantidadeBaixar;
	
	@Column(name="tp_garrafeira")
	@JsonInclude(value = Include.NON_NULL)
	private Integer tipoGarrafeira;
	
	@Column(name="tp_situacao")
	@JsonInclude(value = Include.NON_NULL)
	private Integer situacao;
	
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
	
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "vasilhameEntregueItem")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@JsonInclude(value = Include.NON_EMPTY)
	private List<VasilhameEntregueItemBaixa> vasilhameEntregueItemBaixas = new ArrayList<>();

	
	public VasilhameEntregueItem() {
		
	}
	
	@QueryProjection
	public VasilhameEntregueItem(VasilhameEntregueItemId id, Integer idEmpresa, Empresa empresa, Integer idFilial,
			Filial filial, Integer idCodigo, Integer idVasilhameEntregue, VasilhameEntregue vasilhameEntregue,
			Integer idProduto, Produto produto, Double quantidade, Integer tipoGarrafeira, Integer situacao,
			LocalDateTime dataHoraGravacao, String idUsuario, Usuario usuario, 
			Double quantidadeBaixada, Double quantidadeBaixar) {
		this.id = id;
		this.idEmpresa = idEmpresa;
		this.empresa = empresa;
		this.idFilial = idFilial;
		this.filial = filial;
		this.idCodigo = idCodigo;
		this.idVasilhameEntregue = idVasilhameEntregue;
		this.vasilhameEntregue = vasilhameEntregue;
		this.idProduto = idProduto;
		this.produto = produto;
		this.quantidade = quantidade;
		this.tipoGarrafeira = tipoGarrafeira;
		this.situacao = situacao;
		this.dataHoraGravacao = dataHoraGravacao;
		this.idUsuario = idUsuario;
		this.usuario = usuario;
		this.quantidadeBaixada = quantidadeBaixada;
		this.quantidadeBaixar = quantidadeBaixar;
	}
	
	@QueryProjection
	public VasilhameEntregueItem(VasilhameEntregueItemId id, Integer idEmpresa, Empresa empresa, Integer idFilial,
			Filial filial, Integer idCodigo, Integer idVasilhameEntregue, VasilhameEntregue vasilhameEntregue,
			Integer idProduto, Produto produto, Double quantidade, Integer tipoGarrafeira, Integer situacao,
			LocalDateTime dataHoraGravacao, String idUsuario, Usuario usuario) {
		this.id = id;
		this.idEmpresa = idEmpresa;
		this.empresa = empresa;
		this.idFilial = idFilial;
		this.filial = filial;
		this.idCodigo = idCodigo;
		this.idVasilhameEntregue = idVasilhameEntregue;
		this.vasilhameEntregue = vasilhameEntregue;
		this.idProduto = idProduto;
		this.produto = produto;
		this.quantidade = quantidade;
		this.tipoGarrafeira = tipoGarrafeira;
		this.situacao = situacao;
		this.dataHoraGravacao = dataHoraGravacao;
		this.idUsuario = idUsuario;
		this.usuario = usuario;		
	}
	
	@QueryProjection
	public VasilhameEntregueItem(VasilhameEntregueItemId id, Integer idEmpresa, Empresa empresa, Integer idFilial,
			Filial filial, Integer idCodigo, Integer idVasilhameEntregue, Integer idProduto, Produto produto, Double quantidade,
			Integer tipoGarrafeira, Integer situacao, LocalDateTime dataHoraGravacao, String idUsuario,
			Usuario usuario) {		
		this.id = id;
		this.idEmpresa = idEmpresa;
		this.empresa = empresa;
		this.idFilial = idFilial;
		this.filial = filial;
		this.idCodigo = idCodigo;
		this.idVasilhameEntregue = idVasilhameEntregue;		
		this.idProduto = idProduto;
		this.produto = produto;
		this.quantidade = quantidade;
		this.tipoGarrafeira = tipoGarrafeira;
		this.situacao = situacao;
		this.dataHoraGravacao = dataHoraGravacao;
		this.idUsuario = idUsuario;
		this.usuario = usuario;		
	}
	
	@QueryProjection
	public VasilhameEntregueItem(Integer idVasilhameEntregue, VasilhameEntregue vasilhameEntregue, Integer idProduto,
			Produto produto, Double quantidade, Integer tipoGarrafeira, Integer situacao) {
		this.idVasilhameEntregue = idVasilhameEntregue;
		this.vasilhameEntregue = vasilhameEntregue;
		this.idProduto = idProduto;
		this.produto = produto;
		this.quantidade = quantidade;
		this.tipoGarrafeira = tipoGarrafeira;
		this.situacao = situacao;
	}
	
	@QueryProjection
	public VasilhameEntregueItem(Integer idVasilhameEntregue, VasilhameEntregue vasilhameEntregue, Integer idProduto,
			Produto produto, Double quantidade, Double quantidadeBaixada, Integer tipoGarrafeira, Integer situacao) {
		this.idVasilhameEntregue = idVasilhameEntregue;
		this.vasilhameEntregue = vasilhameEntregue;
		this.idProduto = idProduto;
		this.produto = produto;
		this.quantidade = quantidade;
		this.quantidadeBaixada = quantidadeBaixada;
		this.tipoGarrafeira = tipoGarrafeira;
		this.situacao = situacao;
	}
	
	@QueryProjection
	public VasilhameEntregueItem(Integer idProduto, Produto produto, Double quantidade) {		
		this.idProduto = idProduto;
		this.produto = produto;		
		this.quantidade = quantidade;
	}	

	@QueryProjection
	public VasilhameEntregueItem(Double quantidade, Double quantidadeBaixada) {
		this.quantidade = quantidade;
		this.quantidadeBaixada = quantidadeBaixada;
	}	
	
	@QueryProjection
	public VasilhameEntregueItem(Integer idEmpresa, Double quantidade, Integer idVasilhameEntregue,
			VasilhameEntregue vasilhameEntregue) {
		this.idEmpresa = idEmpresa;
		this.quantidade = quantidade;
		this.idVasilhameEntregue = idVasilhameEntregue;
		this.vasilhameEntregue = vasilhameEntregue;		
	}

	@QueryProjection
	public VasilhameEntregueItem(Integer idEmpresa, Integer idFilial, Integer idCodigo,
			Integer idVasilhameEntregue, Double quantidade) {		
		this.idEmpresa = idEmpresa;		
		this.idFilial = idFilial;
		this.idCodigo = idCodigo;
		this.idVasilhameEntregue = idVasilhameEntregue;
		this.quantidade = quantidade;
	}
	
	public VasilhameEntregueItemId getId() {
		return id;
	}

	public void setId(VasilhameEntregueItemId id) {
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

	public Integer getIdVasilhameEntregue() {
		return idVasilhameEntregue;
	}

	public void setIdVasilhameEntregue(Integer idVasilhameEntregue) {
		this.idVasilhameEntregue = idVasilhameEntregue;
	}

	public VasilhameEntregue getVasilhameEntregue() {
		return vasilhameEntregue;
	}

	public void setVasilhameEntregue(VasilhameEntregue vasilhameEntregue) {
		this.vasilhameEntregue = vasilhameEntregue;
	}

	public Integer getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(Integer idProduto) {
		this.idProduto = idProduto;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}

	public Double getQuantidadeBaixada() {
		return quantidadeBaixada;
	}

	public void setQuantidadeBaixada(Double quantidadeBaixada) {
		this.quantidadeBaixada = quantidadeBaixada;
	}

	public Double getQuantidadeBaixar() {
		return quantidadeBaixar;
	}

	public void setQuantidadeBaixar(Double quantidadeBaixar) {
		this.quantidadeBaixar = quantidadeBaixar;
	}

	public Integer getTipoGarrafeira() {
		return tipoGarrafeira;
	}

	public void setTipoGarrafeira(Integer tipoGarrafeira) {
		this.tipoGarrafeira = tipoGarrafeira;
	}

	public Integer getSituacao() {
		return situacao;
	}

	public void setSituacao(Integer situacao) {
		this.situacao = situacao;
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

	public List<VasilhameEntregueItemBaixa> getVasilhameEntregueItemBaixas() {
		return vasilhameEntregueItemBaixas;
	}

	public void setVasilhameEntregueItemBaixas(List<VasilhameEntregueItemBaixa> vasilhameEntregueItemBaixas) {
		this.vasilhameEntregueItemBaixas = vasilhameEntregueItemBaixas;
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
		result = prime * result + ((idProduto == null) ? 0 : idProduto.hashCode());
		result = prime * result + ((idUsuario == null) ? 0 : idUsuario.hashCode());
		result = prime * result + ((idVasilhameEntregue == null) ? 0 : idVasilhameEntregue.hashCode());
		result = prime * result + ((produto == null) ? 0 : produto.hashCode());
		result = prime * result + ((quantidade == null) ? 0 : quantidade.hashCode());
		result = prime * result + ((quantidadeBaixada == null) ? 0 : quantidadeBaixada.hashCode());
		result = prime * result + ((situacao == null) ? 0 : situacao.hashCode());
		result = prime * result + ((tipoGarrafeira == null) ? 0 : tipoGarrafeira.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		result = prime * result + ((vasilhameEntregue == null) ? 0 : vasilhameEntregue.hashCode());
		result = prime * result + ((vasilhameEntregueItemBaixas == null) ? 0 : vasilhameEntregueItemBaixas.hashCode());
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
		VasilhameEntregueItem other = (VasilhameEntregueItem) obj;
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
		if (idProduto == null) {
			if (other.idProduto != null)
				return false;
		} else if (!idProduto.equals(other.idProduto))
			return false;
		if (idUsuario == null) {
			if (other.idUsuario != null)
				return false;
		} else if (!idUsuario.equals(other.idUsuario))
			return false;
		if (idVasilhameEntregue == null) {
			if (other.idVasilhameEntregue != null)
				return false;
		} else if (!idVasilhameEntregue.equals(other.idVasilhameEntregue))
			return false;
		if (produto == null) {
			if (other.produto != null)
				return false;
		} else if (!produto.equals(other.produto))
			return false;
		if (quantidade == null) {
			if (other.quantidade != null)
				return false;
		} else if (!quantidade.equals(other.quantidade))
			return false;
		if (quantidadeBaixada == null) {
			if (other.quantidadeBaixada != null)
				return false;
		} else if (!quantidadeBaixada.equals(other.quantidadeBaixada))
			return false;
		if (situacao == null) {
			if (other.situacao != null)
				return false;
		} else if (!situacao.equals(other.situacao))
			return false;
		if (tipoGarrafeira == null) {
			if (other.tipoGarrafeira != null)
				return false;
		} else if (!tipoGarrafeira.equals(other.tipoGarrafeira))
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		if (vasilhameEntregue == null) {
			if (other.vasilhameEntregue != null)
				return false;
		} else if (!vasilhameEntregue.equals(other.vasilhameEntregue))
			return false;
		if (vasilhameEntregueItemBaixas == null) {
			if (other.vasilhameEntregueItemBaixas != null)
				return false;
		} else if (!vasilhameEntregueItemBaixas.equals(other.vasilhameEntregueItemBaixas))
			return false;
		return true;
	}
	
}