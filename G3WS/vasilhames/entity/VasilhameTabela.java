package com.g3sistemas.vasilhames.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.g3sistemas.geral.entity.Empresa;
import com.g3sistemas.geral.entity.Filial;
import com.g3sistemas.interfaces.Registro;
import com.querydsl.core.annotations.QueryProjection;


@Table(name = "vas_tabelas_id")
@Entity
public class VasilhameTabela implements Registro<VasilhameTabelaId> {	
	
	@EmbeddedId
	private VasilhameTabelaId id;	

	@Column(name = "tabela", updatable = false, insertable = false)
	@JsonInclude(value = Include.NON_NULL)
	private String nomeTabela;	
	
	@Column(name = "id_empresa", updatable = false, insertable = false)
	@JsonInclude(value = Include.NON_NULL)
	private Integer idEmpresa;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_empresa", updatable = false, insertable = false)
	@JsonInclude(value = Include.NON_NULL)
	private Empresa empresa;	

	@Column(name = "id_filial", updatable = false, insertable = false)
	@JsonInclude(value = Include.NON_NULL)
	private Integer idFilial;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "id_filial", referencedColumnName = "codigo_gefili", insertable = false, updatable = false),
			@JoinColumn(name = "id_empresa", referencedColumnName = "empresa_gefili", insertable = false, updatable = false) })
	@JsonInclude(value = Include.NON_NULL)
	private Filial filial;	

	@Column(name = "id")
	@JsonInclude(value = Include.NON_NULL)
	private Integer idTabela;
	
	
	public VasilhameTabela(){
		
	}
	
	
	@QueryProjection
	public VasilhameTabela(String nomeTabela, Integer idEmpresa, Empresa empresa, Integer idFilial, Filial filial,
			Integer idTabela) {
		this.nomeTabela = nomeTabela;
		this.idEmpresa = idEmpresa;
		this.empresa = empresa;
		this.idFilial = idFilial;
		this.filial = filial;
		this.idTabela = idTabela;		
	}
	
	
	public VasilhameTabelaId getId() {
		return id;
	}

	public void setId(VasilhameTabelaId id) {
		this.id = id;
	}

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

	public Integer getIdTabela() {
		return idTabela;
	}

	public void setIdTabela(Integer idTabela) {
		this.idTabela = idTabela;
	}	

}
