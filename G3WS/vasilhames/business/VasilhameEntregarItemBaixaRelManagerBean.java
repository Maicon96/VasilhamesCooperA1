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
import com.g3sistemas.geral.business.EmpresaCadManagerBean;
import com.g3sistemas.geral.entity.Empresa;
import com.g3sistemas.geral.entity.QEmpresa;
import com.g3sistemas.geral.entity.QFilial;
import com.g3sistemas.sistema.entity.QUsuario;
import com.g3sistemas.sql.filter.G3FiltersBuilder;
import com.g3sistemas.sql.filter.G3SqlFilter;
import com.g3sistemas.vasilhames.dto.VasilhameEntregarItemBaixaRelDTO;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregarItem;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregarItemBaixa;
import com.g3sistemas.vasilhames.entity.VasilhameEntregarItemBaixa;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Stateless
public class VasilhameEntregarItemBaixaRelManagerBean {

	@PersistenceContext(unitName="g3-ds")
	private EntityManager em;
	
	@EJB
	private EmpresaCadManagerBean empresaCadManagerBean;
	
	
	private QVasilhameEntregarItemBaixa qVasilhameEntregarItemBaixa;
	private QEmpresa qEmpresa;
	private QFilial qFilial;
	private QVasilhameEntregarItem qVasilhameEntregarItem;	
	private QUsuario qUsuario;
	
	private JPAQuery<VasilhameEntregarItemBaixa> query;
	private PathBuilder<VasilhameEntregarItemBaixa> pathBuilder;
	private G3FiltersBuilder<VasilhameEntregarItemBaixa> filtrosBuilder;	
	
	private ConstructorExpression<VasilhameEntregarItemBaixa> projecaoVasilhameEntregarItemBaixa() {
		return QVasilhameEntregarItemBaixa.create(qVasilhameEntregarItemBaixa.id, qVasilhameEntregarItemBaixa.idEmpresa,
				QEmpresa.create(qEmpresa.id, qEmpresa.nome), qVasilhameEntregarItemBaixa.idFilial,
				QFilial.create(qFilial.codigo, qFilial.descricao), qVasilhameEntregarItemBaixa.idCodigo,
				qVasilhameEntregarItemBaixa.idVasilhameEntregarItem, qVasilhameEntregarItemBaixa.tipoBaixa,
				qVasilhameEntregarItemBaixa.quantidade, qVasilhameEntregarItemBaixa.dataHoraGravacao,
				qVasilhameEntregarItemBaixa.idUsuario, QUsuario.create(qUsuario.nome));
	}
	
	private List<VasilhameEntregarItemBaixa> loadRecords(LoadRequestDTO filter) throws Exception {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregarItemBaixa = QVasilhameEntregarItemBaixa.vasilhameEntregarItemBaixa;	
		this.qEmpresa = QEmpresa.empresa;
		this.qFilial = QFilial.filial;
		this.qVasilhameEntregarItem = QVasilhameEntregarItem.vasilhameEntregarItem;		
		this.qUsuario = QUsuario.usuario;

		this.query.select(this.projecaoVasilhameEntregarItemBaixa())
			.from(qVasilhameEntregarItemBaixa)
			.leftJoin(qVasilhameEntregarItemBaixa.empresa, qEmpresa)
			.leftJoin(qVasilhameEntregarItemBaixa.filial, qFilial)
			.leftJoin(qVasilhameEntregarItemBaixa.vasilhameEntregarItem, qVasilhameEntregarItem)	
			.leftJoin(qVasilhameEntregarItemBaixa.usuario, qUsuario);		
		
		Map<String, PathBuilder<?>> joins = new HashMap<String, PathBuilder<?>>();
		joins.put(qVasilhameEntregarItemBaixa.empresa.toString(), new PathBuilder<>(Object.class, qEmpresa.toString()));
		joins.put(qVasilhameEntregarItemBaixa.filial.toString(), new PathBuilder<>(Object.class, qFilial.toString()));
		joins.put(qVasilhameEntregarItemBaixa.vasilhameEntregarItem.toString(), new PathBuilder<>(Object.class, qVasilhameEntregarItem.toString()));		
		joins.put(qVasilhameEntregarItemBaixa.usuario.toString(), new PathBuilder<>(Object.class, qUsuario.toString()));
		
		this.pathBuilder = new PathBuilder<>(VasilhameEntregarItemBaixa.class,qVasilhameEntregarItemBaixa.toString());		
		this.filtrosBuilder = new G3FiltersBuilder<>(pathBuilder, VasilhameEntregarItemBaixa.class, joins);
		this.filtrosBuilder.applyFilters(filter, query);
		this.filtrosBuilder.applyOrder(filter, pathBuilder, query);
		
		return this.query.fetch();
	}
	
	public LoadRequestDTO generateFilter(VasilhameEntregarItemBaixaRelDTO dto) throws Exception {
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
			G3SqlFilter filtroVasilhame = new G3SqlFilter("vasilhameEntregarItem.idVasilhameEntregar", dto.idVasilhameEntregar, "int", "in", "and");
			filterFix.add(filtroVasilhame);
		}
		if (dto.idPessoa != null && !dto.idPessoa.equals("")) {
			G3SqlFilter filtroPessoa = new G3SqlFilter("vasilhameEntregarItem.vasilhameEntregar.idPessoa", dto.idPessoa, "int", "in", "and");
			filterFix.add(filtroPessoa);
		}
		if (dto.nome != null && !dto.nome.equals("")) {
			G3SqlFilter filtroNome = new G3SqlFilter("vasilhameEntregarItem.vasilhameEntregar.nome", dto.nome, "string", "in", "and");
			filterFix.add(filtroNome);
		}		
		if (dto.contribuinte != null && !dto.contribuinte.equals("")) {
			G3SqlFilter filtroContribuinte = new G3SqlFilter("vasilhameEntregarItem.vasilhameEntregar.tipoContribuinte", dto.contribuinte, "int", "in", "and");
			filterFix.add(filtroContribuinte);
		}	
		if (dto.cpfCnpj != null && !dto.cpfCnpj.equals("")) {
			G3SqlFilter filtroCpfcnpj = new G3SqlFilter("vasilhameEntregarItem.vasilhameEntregar.cpfcnpj", dto.cpfCnpj, "string", "in", "and");
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
			G3SqlFilter filtroSituacao = new G3SqlFilter("vasilhameEntregarItem.vasilhameEntregar.situacao", situacaoEntregar.toString(), "int", "in", "and");
			filterFix.add(filtroSituacao);
        }		
		
		if (dto.idUsuarioEntregar != null && !dto.idUsuarioEntregar.equals("")) {
			G3SqlFilter filtroUsuarioEntregar = new G3SqlFilter("vasilhameEntregarItem.vasilhameEntregar.idUsuario", dto.idUsuarioEntregar, "string", "in", "and");
			filterFix.add(filtroUsuarioEntregar);
		}	
		if (dto.idProduto != null && !dto.idProduto.equals("")) {
			G3SqlFilter filtroProduto = new G3SqlFilter("vasilhameEntregarItem.idProduto", dto.idProduto, "int", "in", "and");
			filterFix.add(filtroProduto);
		}
		if (dto.garrafeira != null && !dto.garrafeira.equals("")) {
			G3SqlFilter filtroGarrafeira = new G3SqlFilter("vasilhameEntregarItem.tipoGarrafeira", dto.garrafeira, "int", "in", "and");
			filterFix.add(filtroGarrafeira);
		}
		if (dto.situacao != null && !dto.situacao.equals("")) {
			G3SqlFilter filtroSituacao = new G3SqlFilter("vasilhameEntregarItem.situacao", dto.situacao, "int", "in", "and");
			filterFix.add(filtroSituacao);
		}
		if (dto.idVasilhameEntregarItem != null && !dto.idVasilhameEntregarItem.equals("")) {
			G3SqlFilter filtroItem = new G3SqlFilter("idVasilhameEntregarItem", dto.idVasilhameEntregarItem, "int", "in", "and");
			filterFix.add(filtroItem);
		}
		if (dto.tipoBaixa != null && !dto.tipoBaixa.equals("")) {
			G3SqlFilter filtroTipoBaixa = new G3SqlFilter("tipoBaixa", dto.tipoBaixa, "int", "in", "and");
			filterFix.add(filtroTipoBaixa);
		}
		if (dto.idUsuario != null && !dto.idUsuario.equals("")) {
			G3SqlFilter filtroUsuario = new G3SqlFilter("idUsuario", dto.idUsuario, "string", "in", "and");
			filterFix.add(filtroUsuario);
		}
		return new LoadRequestDTO(filterFix, dto.sort);
	}

	
	public byte[] generatePdf(VasilhameEntregarItemBaixaRelDTO dto, Integer idEmpresa, String usuario, String nomeUsuario) throws Exception {		
		HashMap<String, Object> param = new HashMap<>();		
		String nomeEmpresa = "";	
        InputStream cabecalhoIS = null;
		InputStream relatoIS = null;
		InputStream logo = null;
		byte[] pdf = null;
		
		try {
			LoadRequestDTO filtros = this.generateFilter(dto);
			List<VasilhameEntregarItemBaixa> vasilhamesEntregarItensBaixas = this.loadRecords(filtros);
							
	        cabecalhoIS = this.getClass().getClassLoader().getResourceAsStream("/relato/cabecalho_retrato.jasper");
	        JasperReport cabecalho = (JasperReport) JRLoader.loadObject(cabecalhoIS);       
			relatoIS = this.getClass().getClassLoader().getResourceAsStream("/relato/vasilhame/VasilhameEntregarItemBaixa.jasper");
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
	        param.put("titulo", "RELATÓRIO DE VASILHAMES A ENTREGAR ITENS - BAIXAS");
	        
	        pdf = JasperRunManager.runReportToPdf(relato, param, new JRBeanCollectionDataSource(vasilhamesEntregarItensBaixas));
		} finally {
			if (cabecalhoIS != null) cabecalhoIS.close();
			if (relatoIS != null) relatoIS.close();
			if (logo != null) logo.close();			
		}
		
		return pdf;
	}
	
	
	public byte[] generateTxt(VasilhameEntregarItemBaixaRelDTO dto) throws Exception {		
		String quebraLinha = java.net.URLDecoder.decode(dto.quebraLinha, "UTF-8");
		LoadRequestDTO filtros = this.generateFilter(dto);
		List<VasilhameEntregarItemBaixa> vasilhamesEntregarItensBaixas = this.loadRecords(filtros);
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
		txt.append("Vasilhame Entregar Item");
		txt.append(dto.delimitador);
		txt.append("Tipo Baixa");				
		txt.append(dto.delimitador);
		txt.append("Quantidade");				
		txt.append(dto.delimitador);
		txt.append("Data/Hora Situação");
		txt.append(dto.delimitador);
		txt.append("Usuário");
		txt.append(dto.delimitador);
		txt.append("Nome Usuário");
		txt.append(quebraLinha);		
		
		for (VasilhameEntregarItemBaixa vasilhameEntregarItemBaixa : vasilhamesEntregarItensBaixas) {
			txt.append(vasilhameEntregarItemBaixa.getIdCodigo());
			txt.append(dto.delimitador);
			txt.append(vasilhameEntregarItemBaixa.getIdEmpresa());
			txt.append(dto.delimitador);
			txt.append(vasilhameEntregarItemBaixa.getEmpresa().getNome());
			txt.append(dto.delimitador);
			txt.append(vasilhameEntregarItemBaixa.getIdFilial());
			txt.append(dto.delimitador);
			txt.append(vasilhameEntregarItemBaixa.getFilial().getDescricao());
			txt.append(dto.delimitador);
			if (vasilhameEntregarItemBaixa.getIdVasilhameEntregarItem() != null) {
				txt.append(vasilhameEntregarItemBaixa.getIdVasilhameEntregarItem());
			}
			txt.append(dto.delimitador);
			if (vasilhameEntregarItemBaixa.getTipoBaixa() != null) {
				if (vasilhameEntregarItemBaixa.getTipoBaixa() == 1) {
					txt.append("1 - Venda");
				} else if (vasilhameEntregarItemBaixa.getTipoBaixa() == 2) {
					txt.append("2 - Devolução");
				}
			}
			txt.append(dto.delimitador);			
			if (vasilhameEntregarItemBaixa.getQuantidade() != null) {
				txt.append(df3.format(vasilhameEntregarItemBaixa.getQuantidade()));				
			}			
			txt.append(dto.delimitador);
			if (vasilhameEntregarItemBaixa.getDataHoraGravacao() != null) {
				txt.append(vasilhameEntregarItemBaixa.getDataHoraGravacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
			}
			txt.append(dto.delimitador);
			if (vasilhameEntregarItemBaixa.getIdUsuario() != null) {
				txt.append(vasilhameEntregarItemBaixa.getIdUsuario());
			}
			txt.append(dto.delimitador);
			if (vasilhameEntregarItemBaixa.getUsuario() != null) {
				if (vasilhameEntregarItemBaixa.getUsuario().getNome() != null) {
					txt.append(vasilhameEntregarItemBaixa.getUsuario().getNome());
				}
			}			
			txt.append(quebraLinha);
		}

		return txt.toString().getBytes();
	}

}