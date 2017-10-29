package com.g3sistemas.vasilhames.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.g3sistemas.converters.jackson.LocalDateTimeDeserializer;
import com.g3sistemas.converters.jackson.LocalDateTimeSerializer;
import com.g3sistemas.geral.entity.Empresa;
import com.g3sistemas.geral.entity.Filial;
import com.g3sistemas.geral.entity.Pessoa;
import com.g3sistemas.interfaces.Registro;
import com.g3sistemas.sistema.entity.Usuario;
import com.querydsl.core.annotations.QueryProjection;

@Table(name="vas_entregar")
@Entity
@EntityListeners({VasilhameEntregarListener.class})
public class VasilhameEntregar implements Registro<VasilhameEntregarId> {
	
	@EmbeddedId	
	private VasilhameEntregarId id;	
	
	
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
	
	
	@Column(name="id_pessoa")
	@JsonInclude(value = Include.NON_NULL)
	private Integer idPessoa;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "id_empresa", referencedColumnName = "empresa_gepess", insertable = false, updatable = false),
		@JoinColumn(name = "id_pessoa", referencedColumnName = "codigo_gepess", insertable = false, updatable = false) 
	})
	@JsonInclude(value = Include.NON_NULL)
	private Pessoa pessoa;
	
	
	@Column(name="nm_nome")
	@JsonInclude(value = Include.NON_NULL)
	private String nome;
	
	@Column(name="tp_contribuinte")
	@JsonInclude(value = Include.NON_NULL)
	private Integer tipoContribuinte;
	
	@Column(name="nm_cpfcnpj")
	@JsonInclude(value = Include.NON_NULL)
	private String cpfcnpj;
	
	@Column(name="ds_observacao")
	@JsonInclude(value = Include.NON_NULL)
	private String observacao;
	
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
	
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "vasilhameEntregar")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@JsonInclude(value = Include.NON_EMPTY)	
	private List<VasilhameEntregarItem> itens = new ArrayList<>();

		
	public VasilhameEntregar() {
		
	}
	
	@QueryProjection
	public VasilhameEntregar(VasilhameEntregarId id, Integer idEmpresa, Empresa empresa, Integer idFilial, Filial filial, 
			Integer idCodigo, Integer idPessoa, Pessoa pessoa, String nome, Integer tipoContribuinte,
			String cpfcnpj, String observacao, Integer situacao, LocalDateTime dataHoraGravacao,
			String idUsuario, Usuario usuario, List<VasilhameEntregarItem> itens) {
		this.id = id;
		this.idEmpresa = idEmpresa;
		this.empresa = empresa;
		this.idFilial = idFilial;
		this.filial = filial;
		this.idCodigo = idCodigo;
		this.idPessoa = idPessoa;
		this.pessoa = pessoa;
		this.nome = nome;
		this.tipoContribuinte = tipoContribuinte;
		this.cpfcnpj = cpfcnpj;
		this.observacao = observacao;
		this.situacao = situacao;
		this.dataHoraGravacao = dataHoraGravacao;
		this.idUsuario = idUsuario;
		this.usuario = usuario;
		this.itens = itens;
	}
	
	@QueryProjection
	public VasilhameEntregar(VasilhameEntregarId id, Integer idEmpresa, Empresa empresa, Integer idFilial, Filial filial, 
			Integer idCodigo, Integer idPessoa, Pessoa pessoa, String nome, Integer tipoContribuinte,
			String cpfcnpj, String observacao, Integer situacao, LocalDateTime dataHoraGravacao,
			String idUsuario, Usuario usuario) {
		this.id = id;
		this.idEmpresa = idEmpresa;
		this.empresa = empresa;
		this.idFilial = idFilial;
		this.filial = filial;
		this.idCodigo = idCodigo;
		this.idPessoa = idPessoa;
		this.pessoa = pessoa;
		this.nome = nome;
		this.tipoContribuinte = tipoContribuinte;
		this.cpfcnpj = cpfcnpj;
		this.observacao = observacao;
		this.situacao = situacao;
		this.dataHoraGravacao = dataHoraGravacao;
		this.idUsuario = idUsuario;
		this.usuario = usuario;
	}
	
	@QueryProjection
	public VasilhameEntregar(Integer idCodigo, Integer idPessoa, Pessoa pessoa, String nome, 
			Integer tipoContribuinte, String cpfcnpj, Integer situacao, String idUsuario, Usuario usuario) {
		this.idCodigo = idCodigo;
		this.idPessoa = idPessoa;
		this.pessoa = pessoa;
		this.nome = nome;
		this.tipoContribuinte = tipoContribuinte;
		this.cpfcnpj = cpfcnpj;
		this.situacao = situacao;		
		this.idUsuario = idUsuario;
		this.usuario = usuario;
	}
	
	@QueryProjection
	public VasilhameEntregar(Integer idPessoa, Pessoa pessoa, String nome, Integer tipoContribuinte, String cpfcnpj) {
		this.idPessoa = idPessoa;
		this.pessoa = pessoa;
		this.nome = nome;
		this.tipoContribuinte = tipoContribuinte;
		this.cpfcnpj = cpfcnpj;
	}
	
	@QueryProjection
	public VasilhameEntregar(VasilhameEntregarId id, Integer idEmpresa, Integer idFilial, Integer idCodigo, Integer idPessoa, String nome,
			Integer tipoContribuinte, String cpfcnpj, String observacao, Integer situacao,
			LocalDateTime dataHoraGravacao, String idUsuario, List<VasilhameEntregarItem> itens) {
		this.id = id;
		this.idEmpresa = idEmpresa;
		this.idFilial = idFilial;
		this.idCodigo = idCodigo;
		this.idPessoa = idPessoa;
		this.nome = nome;
		this.tipoContribuinte = tipoContribuinte;
		this.cpfcnpj = cpfcnpj;
		this.observacao = observacao;
		this.situacao = situacao;
		this.dataHoraGravacao = dataHoraGravacao;
		this.idUsuario = idUsuario;		
		this.itens = itens;		
	}
	
	@QueryProjection
	public VasilhameEntregar(VasilhameEntregarId id, Integer situacao) {
		this.id = id;		
		this.situacao = situacao;		
	}
	
	
	public VasilhameEntregarId getId() {
		return id;
	}
	
	public void setId(VasilhameEntregarId id) {
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

	public Integer getIdPessoa() {
		return idPessoa;
	}

	public void setIdPessoa(Integer idPessoa) {
		this.idPessoa = idPessoa;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getTipoContribuinte() {
		return tipoContribuinte;
	}

	public void setTipoContribuinte(Integer tipoContribuinte) {
		this.tipoContribuinte = tipoContribuinte;
	}

	public String getCpfcnpj() {
		return cpfcnpj;
	}

	public void setCpfcnpj(String cpfcnpj) {
		this.cpfcnpj = cpfcnpj;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
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

	public List<VasilhameEntregarItem> getItens() {
		return itens;
	}

	public void setItens(List<VasilhameEntregarItem> itens) {
		this.itens = itens;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cpfcnpj == null) ? 0 : cpfcnpj.hashCode());
		result = prime * result + ((dataHoraGravacao == null) ? 0 : dataHoraGravacao.hashCode());
		result = prime * result + ((empresa == null) ? 0 : empresa.hashCode());
		result = prime * result + ((filial == null) ? 0 : filial.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idCodigo == null) ? 0 : idCodigo.hashCode());
		result = prime * result + ((idEmpresa == null) ? 0 : idEmpresa.hashCode());
		result = prime * result + ((idFilial == null) ? 0 : idFilial.hashCode());
		result = prime * result + ((idPessoa == null) ? 0 : idPessoa.hashCode());
		result = prime * result + ((idUsuario == null) ? 0 : idUsuario.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((observacao == null) ? 0 : observacao.hashCode());
		result = prime * result + ((pessoa == null) ? 0 : pessoa.hashCode());
		result = prime * result + ((situacao == null) ? 0 : situacao.hashCode());
		result = prime * result + ((tipoContribuinte == null) ? 0 : tipoContribuinte.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
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
		VasilhameEntregar other = (VasilhameEntregar) obj;
		if (cpfcnpj == null) {
			if (other.cpfcnpj != null)
				return false;
		} else if (!cpfcnpj.equals(other.cpfcnpj))
			return false;
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
		if (idPessoa == null) {
			if (other.idPessoa != null)
				return false;
		} else if (!idPessoa.equals(other.idPessoa))
			return false;
		if (idUsuario == null) {
			if (other.idUsuario != null)
				return false;
		} else if (!idUsuario.equals(other.idUsuario))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (observacao == null) {
			if (other.observacao != null)
				return false;
		} else if (!observacao.equals(other.observacao))
			return false;
		if (pessoa == null) {
			if (other.pessoa != null)
				return false;
		} else if (!pessoa.equals(other.pessoa))
			return false;
		if (situacao == null) {
			if (other.situacao != null)
				return false;
		} else if (!situacao.equals(other.situacao))
			return false;
		if (tipoContribuinte == null) {
			if (other.tipoContribuinte != null)
				return false;
		} else if (!tipoContribuinte.equals(other.tipoContribuinte))
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		return true;
	}
	
	
	
}