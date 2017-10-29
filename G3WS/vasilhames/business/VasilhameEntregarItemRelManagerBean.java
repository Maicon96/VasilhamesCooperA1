package com.g3sistemas.vasilhames.business;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.g3sistemas.dto.LoadRequestDTO;
import com.g3sistemas.estoques.entity.QProduto;
import com.g3sistemas.geral.business.EmpresaCadManagerBean;
import com.g3sistemas.geral.entity.Empresa;
import com.g3sistemas.geral.entity.QEmpresa;
import com.g3sistemas.geral.entity.QFilial;
import com.g3sistemas.sistema.entity.QUsuario;
import com.g3sistemas.sql.filter.G3FiltersBuilder;
import com.g3sistemas.sql.filter.G3SqlFilter;
import com.g3sistemas.vasilhames.dto.VasilhameEntregarItemRelDTO;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregar;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregarItem;
import com.g3sistemas.vasilhames.entity.VasilhameEntregarItem;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Stateless
public class VasilhameEntregarItemRelManagerBean {

	@PersistenceContext(unitName="g3-ds")
	private EntityManager em;
	
	@EJB
	private EmpresaCadManagerBean empresaCadManagerBean;
	
	
	private QVasilhameEntregarItem qVasilhameEntregarItem;	
	private QEmpresa qEmpresa;
	private QFilial qFilial;
	private QVasilhameEntregar qVasilhameEntregar;
	private QProduto qProduto;
	private QUsuario qUsuario;
	
	private JPAQuery<VasilhameEntregarItem> query;
	private PathBuilder<VasilhameEntregarItem> pathBuilder;
	private G3FiltersBuilder<VasilhameEntregarItem> filtrosBuilder;	
	
	private ConstructorExpression<VasilhameEntregarItem> projecaoVasilhameEntregarItem() {
		return QVasilhameEntregarItem.create(qVasilhameEntregarItem.id, qVasilhameEntregarItem.idEmpresa,
				QEmpresa.create(qEmpresa.id, qEmpresa.nome), qVasilhameEntregarItem.idFilial,
				QFilial.create(qFilial.codigo, qFilial.descricao), qVasilhameEntregarItem.idCodigo,
				qVasilhameEntregarItem.idVasilhameEntregar, qVasilhameEntregarItem.idProduto,
				QProduto.create(qProduto.id, qProduto.digito, qProduto.descricao), qVasilhameEntregarItem.quantidade,
				qVasilhameEntregarItem.tipoGarrafeira, qVasilhameEntregarItem.situacao,
				qVasilhameEntregarItem.dataHoraGravacao, qVasilhameEntregarItem.idUsuario,
				QUsuario.create(qUsuario.nome));
	}
	
	private List<VasilhameEntregarItem> loadRecords(LoadRequestDTO filter) throws Exception {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregarItem = QVasilhameEntregarItem.vasilhameEntregarItem;	
		this.qEmpresa = QEmpresa.empresa;
		this.qFilial = QFilial.filial;
		this.qVasilhameEntregar = QVasilhameEntregar.vasilhameEntregar;
		this.qProduto = QProduto.produto;
		this.qUsuario = QUsuario.usuario;

		this.query.select(this.projecaoVasilhameEntregarItem())
			.from(qVasilhameEntregarItem)
			.leftJoin(qVasilhameEntregarItem.empresa, qEmpresa)
			.leftJoin(qVasilhameEntregarItem.filial, qFilial)
			.leftJoin(qVasilhameEntregarItem.vasilhameEntregar, qVasilhameEntregar)			
			.leftJoin(qVasilhameEntregarItem.produto, qProduto)
			.leftJoin(qVasilhameEntregarItem.usuario, qUsuario);		
		
		Map<String, PathBuilder<?>> joins = new HashMap<String, PathBuilder<?>>();
		joins.put(qVasilhameEntregarItem.empresa.toString(), new PathBuilder<>(Object.class, qEmpresa.toString()));
		joins.put(qVasilhameEntregarItem.filial.toString(), new PathBuilder<>(Object.class, qFilial.toString()));
		joins.put(qVasilhameEntregarItem.vasilhameEntregar.toString(), new PathBuilder<>(Object.class, qVasilhameEntregar.toString()));		
		joins.put(qVasilhameEntregarItem.produto.toString(), new PathBuilder<>(Object.class, qProduto.toString()));
		joins.put(qVasilhameEntregarItem.usuario.toString(), new PathBuilder<>(Object.class, qUsuario.toString()));
		
		this.pathBuilder = new PathBuilder<>(VasilhameEntregarItem.class,qVasilhameEntregarItem.toString());		
		this.filtrosBuilder = new G3FiltersBuilder<>(pathBuilder, VasilhameEntregarItem.class, joins);
		this.filtrosBuilder.applyFilters(filter, query);
		this.filtrosBuilder.applyOrder(filter, pathBuilder, query);
		
		return this.query.fetch();
	}
	
	public LoadRequestDTO generateFilter(VasilhameEntregarItemRelDTO dto) throws Exception {
		List<G3SqlFilter> filterFix = new ArrayList<>();				
		
		if (dto.idEmpresa != null && !dto.idEmpresa.equals("")) {
			G3SqlFilter filtroEmpresa = new G3SqlFilter("idEmpresa", dto.idEmpresa, "int", "in", "and");
			filterFix.add(filtroEmpresa);
		}
		if (dto.idFilial != null && !dto.idFilial.equals("")) {
			G3SqlFilter filtroFilial = new G3SqlFilter("idFilial", dto.idFilial, "int", "in", "and");
			filterFix.add(filtroFilial);
		}
		if (dto.idVasilhameEntregar != null && !dto.idVasilhameEntregar.equals("")) {
			G3SqlFilter filtroVasilhame = new G3SqlFilter("idVasilhameEntregar", dto.idVasilhameEntregar, "int", "in", "and");
			filterFix.add(filtroVasilhame);
		}
		if (dto.idPessoa != null && !dto.idPessoa.equals("")) {
			G3SqlFilter filtroPessoa = new G3SqlFilter("vasilhameEntregar.idPessoa", dto.idPessoa, "int", "in", "and");
			filterFix.add(filtroPessoa);
		}
		if (dto.nome != null && !dto.nome.equals("")) {
			G3SqlFilter filtroNome = new G3SqlFilter("vasilhameEntregar.nome", dto.nome, "string", "in", "and");
			filterFix.add(filtroNome);
		}		
		if (dto.contribuinte != null && !dto.contribuinte.equals("")) {
			G3SqlFilter filtroContribuinte = new G3SqlFilter("vasilhameEntregar.tipoContribuinte", dto.contribuinte, "int", "in", "and");
			filterFix.add(filtroContribuinte);
		}	
		if (dto.cpfCnpj != null && !dto.cpfCnpj.equals("")) {
			G3SqlFilter filtroCpfcnpj = new G3SqlFilter("vasilhameEntregar.cpfcnpj", dto.cpfCnpj, "string", "in", "and");
			filterFix.add(filtroCpfcnpj);
		}
		
		StringBuilder situacaoEntregar = new StringBuilder();		
		if (dto.pendente != null && !dto.pendente.equals("")) {
			situacaoEntregar.append(dto.pendente);
		}
		if (dto.inutilizado != null && !dto.inutilizado.equals("")) {
        	if (situacaoEntregar.length() > 0) {
        		situacaoEntregar.append(",");
            }
        	situacaoEntregar.append(dto.inutilizado);
        }		
		if (dto.fechado != null && !dto.fechado.equals("")) {
        	if (situacaoEntregar.length() > 0) {
        		situacaoEntregar.append(",");
            }
        	situacaoEntregar.append(dto.fechado);
        }		
		if (situacaoEntregar.length() > 0) {          
			G3SqlFilter filtroSituacao = new G3SqlFilter("vasilhameEntregar.situacao", situacaoEntregar.toString(), "int", "in", "and");
			filterFix.add(filtroSituacao);
        }		
		
		if (dto.idUsuarioEntregar != null && !dto.idUsuarioEntregar.equals("")) {
			G3SqlFilter filtroUsuarioEntregar = new G3SqlFilter("vasilhameEntregar.idUsuario", dto.idUsuarioEntregar, "string", "in", "and");
			filterFix.add(filtroUsuarioEntregar);
		}	
		if (dto.idProduto != null && !dto.idProduto.equals("")) {
			G3SqlFilter filtroProduto = new G3SqlFilter("idProduto", dto.idProduto, "int", "in", "and");
			filterFix.add(filtroProduto);
		}
		if (dto.garrafeira != null && !dto.garrafeira.equals("")) {
			G3SqlFilter filtroGarrafeira = new G3SqlFilter("tipoGarrafeira", dto.garrafeira, "int", "in", "and");
			filterFix.add(filtroGarrafeira);
		}
		if (dto.situacao != null && !dto.situacao.equals("")) {
			G3SqlFilter filtroSituacao = new G3SqlFilter("situacao", dto.situacao, "int", "in", "and");
			filterFix.add(filtroSituacao);
		}
		if (dto.idUsuario != null && !dto.idUsuario.equals("")) {
			G3SqlFilter filtroUsuario = new G3SqlFilter("idUsuario", dto.idUsuario, "string", "in", "and");
			filterFix.add(filtroUsuario);
		}
		return new LoadRequestDTO(filterFix, dto.sort);
	}

	
	public byte[] generatePdf(VasilhameEntregarItemRelDTO dto, Integer idEmpresa, String usuario, String nomeUsuario) throws Exception {		
		HashMap<String, Object> param = new HashMap<>();		
		String nomeEmpresa = "";	
		InputStream cabecalhoIS = null;
		InputStream relatoIS = null;
		InputStream logo = null;
		byte[] pdf = null;		
		
		try {
			LoadRequestDTO filtros = this.generateFilter(dto);
			List<VasilhameEntregarItem> vasilhamesEntregarItens = this.loadRecords(filtros);
							
	        cabecalhoIS = this.getClass().getClassLoader().getResourceAsStream("/relato/cabecalho_paisagem.jasper");
	        JasperReport cabecalho = (JasperReport) JRLoader.loadObject(cabecalhoIS);       
			relatoIS = this.getClass().getClassLoader().getResourceAsStream("/relato/vasilhame/VasilhameEntregarItem.jasper");
	        JasperReport relato = (JasperReport) JRLoader.loadObject(relatoIS);        
	        
	        logo = new ByteArrayInputStream(new byte[0]);       
	        Empresa empresa = this.empresaCadManagerBean.findLogo(idEmpresa);
	        if (empresa != null) {
				if (empresa.getLogo() != null) {
					logo = new ByteArrayInputStream(empresa.getLogo());	
				} else {
					logo = this.getClass().getClassLoader().getResourceAsStream("/relato/images/noimage.jpg");
				}
				if (empresa.getNomeFantasia() != null) {
					nomeEmpresa = empresa.getNomeFantasia();	
				}
				
			} else {
				logo = this.getClass().getClassLoader().getResourceAsStream("/relato/images/noimage.jpg");
			}
							
	        param.put("cabecalho", cabecalho);
	        param.put("logo", logo);
	        param.put("usuario", usuario);
	        param.put("nomeUsuario", nomeUsuario);
	        param.put("empresa", nomeEmpresa);
	        param.put("zebra", Integer.parseInt(dto.zebrado));
	        param.put("titulo", "RELATÓRIO DE VASILHAMES A ENTREGAR - ITENS");  
	        
	        pdf = JasperRunManager.runReportToPdf(relato, param, new JRBeanCollectionDataSource(vasilhamesEntregarItens));
	        
		} finally {
			if (cabecalhoIS != null) cabecalhoIS.close();
			if (relatoIS != null) relatoIS.close();
			if (logo != null) logo.close();			
		}
		
		return pdf;
	}
	
	
	public byte[] generateTxt(VasilhameEntregarItemRelDTO dto) throws Exception {		
		String quebraLinha = java.net.URLDecoder.decode(dto.quebraLinha, "UTF-8");
		LoadRequestDTO filtros = this.generateFilter(dto);
		List<VasilhameEntregarItem> vasilhamesEntregarItens = this.loadRecords(filtros);
		DecimalFormat df3 = new DecimalFormat("0.000");

		StringBuilder txt = new StringBuilder();
		txt.append("ID");
		txt.append(dto.delimitador);
		txt.append("Empresa");
		txt.append(dto.delimitador);
		txt.append("Nome Empresa");
		txt.append(dto.delimitador);
		txt.append("Filial");
		txt.append(dto.delimitador);
		txt.append("Descrição Filial");
		txt.append(dto.delimitador);
		txt.append("Vasilhame Entregar");
		txt.append(dto.delimitador);
		txt.append("Produto");
		txt.append(dto.delimitador);
		txt.append("Descrição Produto");
		txt.append(dto.delimitador);
		txt.append("Quantidade");
		txt.append(dto.delimitador);
		txt.append("Tipo Garrafeira");
		txt.append(dto.delimitador);
		txt.append("Situação");
		txt.append(dto.delimitador);
		txt.append("Data/Hora Situação");
		txt.append(dto.delimitador);
		txt.append("Usuário");
		txt.append(dto.delimitador);
		txt.append("Nome Usuário");
		txt.append(quebraLinha);		
		
		for (VasilhameEntregarItem vasilhameEntregarItem : vasilhamesEntregarItens) {
			txt.append(vasilhameEntregarItem.getIdCodigo());
			txt.append(dto.delimitador);
			txt.append(vasilhameEntregarItem.getIdEmpresa());
			txt.append(dto.delimitador);
			txt.append(vasilhameEntregarItem.getEmpresa().getNome());
			txt.append(dto.delimitador);
			txt.append(vasilhameEntregarItem.getIdFilial());
			txt.append(dto.delimitador);
			txt.append(vasilhameEntregarItem.getFilial().getDescricao());
			txt.append(dto.delimitador);
			if (vasilhameEntregarItem.getIdVasilhameEntregar() != null) {
				txt.append(vasilhameEntregarItem.getIdVasilhameEntregar());
			}
			txt.append(dto.delimitador);
			if (vasilhameEntregarItem.getIdProduto() != null
					&& vasilhameEntregarItem.getProduto().getDigito() != null) {
				txt.append(vasilhameEntregarItem.getIdProduto() + "-" + vasilhameEntregarItem.getProduto().getDigito());
			}
			txt.append(dto.delimitador);
			if (vasilhameEntregarItem.getProduto() != null) {
				if (vasilhameEntregarItem.getProduto().getDescricao() != null) {
					txt.append(vasilhameEntregarItem.getProduto().getDescricao());
				}
			}
			txt.append(dto.delimitador);
			if (vasilhameEntregarItem.getQuantidade() != null) {
				txt.append(df3.format(vasilhameEntregarItem.getQuantidade()));				
			}
			txt.append(dto.delimitador);
			if (vasilhameEntregarItem.getTipoGarrafeira() != null) {
				if (vasilhameEntregarItem.getTipoGarrafeira() == 0) {
					txt.append("0 - Não");
				} else if (vasilhameEntregarItem.getTipoGarrafeira() == 1) {
					txt.append("1 - Sim");
				}
			}
			txt.append(dto.delimitador);
			if (vasilhameEntregarItem.getSituacao() != null) {
				if (vasilhameEntregarItem.getSituacao() == 1) {
					txt.append("1 - Normal");
				} else if (vasilhameEntregarItem.getSituacao() == 2) {
					txt.append("2 - Cancelado");
				}
			}
			txt.append(dto.delimitador);
			if (vasilhameEntregarItem.getDataHoraGravacao() != null) {
				txt.append(vasilhameEntregarItem.getDataHoraGravacao()
						.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
			}
			txt.append(dto.delimitador);
			if (vasilhameEntregarItem.getIdUsuario() != null) {
				txt.append(vasilhameEntregarItem.getIdUsuario());
			}
			txt.append(dto.delimitador);
			if (vasilhameEntregarItem.getUsuario() != null) {
				if (vasilhameEntregarItem.getUsuario().getNome() != null) {
					txt.append(vasilhameEntregarItem.getUsuario().getNome());
				}
			}			
			txt.append(quebraLinha);
		}

		return txt.toString().getBytes();
	}

}