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
import com.g3sistemas.vasilhames.dto.VasilhameEntregueItemRelDTO;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregue;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregueItem;
import com.g3sistemas.vasilhames.entity.VasilhameEntregueItem;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Stateless
public class VasilhameEntregueItemRelManagerBean {

	@PersistenceContext(unitName="g3-ds")
	private EntityManager em;
	
	@EJB
	private EmpresaCadManagerBean empresaCadManagerBean;
	
	
	private QVasilhameEntregueItem qVasilhameEntregueItem;	
	private QEmpresa qEmpresa;
	private QFilial qFilial;
	private QVasilhameEntregue qVasilhameEntregue;
	private QProduto qProduto;
	private QUsuario qUsuario;
	
	private JPAQuery<VasilhameEntregueItem> query;
	private PathBuilder<VasilhameEntregueItem> pathBuilder;
	private G3FiltersBuilder<VasilhameEntregueItem> filtrosBuilder;	
	
	private ConstructorExpression<VasilhameEntregueItem> projecaoVasilhameEntregueItem() {
		return QVasilhameEntregueItem.create(qVasilhameEntregueItem.id, qVasilhameEntregueItem.idEmpresa,
				QEmpresa.create(qEmpresa.id, qEmpresa.nome), qVasilhameEntregueItem.idFilial,
				QFilial.create(qFilial.codigo, qFilial.descricao), qVasilhameEntregueItem.idCodigo,
				qVasilhameEntregueItem.idVasilhameEntregue, qVasilhameEntregueItem.idProduto,
				QProduto.create(qProduto.id, qProduto.digito, qProduto.descricao), qVasilhameEntregueItem.quantidade,
				qVasilhameEntregueItem.tipoGarrafeira, qVasilhameEntregueItem.situacao,
				qVasilhameEntregueItem.dataHoraGravacao, qVasilhameEntregueItem.idUsuario,
				QUsuario.create(qUsuario.nome));
	}
	
	private List<VasilhameEntregueItem> loadRecords(LoadRequestDTO filter) throws Exception {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregueItem = QVasilhameEntregueItem.vasilhameEntregueItem;	
		this.qEmpresa = QEmpresa.empresa;
		this.qFilial = QFilial.filial;
		this.qVasilhameEntregue = QVasilhameEntregue.vasilhameEntregue;
		this.qProduto = QProduto.produto;
		this.qUsuario = QUsuario.usuario;

		this.query.select(this.projecaoVasilhameEntregueItem())
			.from(qVasilhameEntregueItem)
			.leftJoin(qVasilhameEntregueItem.empresa, qEmpresa)
			.leftJoin(qVasilhameEntregueItem.filial, qFilial)
			.leftJoin(qVasilhameEntregueItem.vasilhameEntregue, qVasilhameEntregue)
			.leftJoin(qVasilhameEntregueItem.produto, qProduto)
			.leftJoin(qVasilhameEntregueItem.usuario, qUsuario);	
		
		Map<String, PathBuilder<?>> joins = new HashMap<String, PathBuilder<?>>();
		joins.put(qVasilhameEntregueItem.empresa.toString(), new PathBuilder<>(Object.class, qEmpresa.toString()));
		joins.put(qVasilhameEntregueItem.filial.toString(), new PathBuilder<>(Object.class, qFilial.toString()));
		joins.put(qVasilhameEntregueItem.vasilhameEntregue.toString(), new PathBuilder<>(Object.class, qVasilhameEntregue.toString()));		
		joins.put(qVasilhameEntregueItem.produto.toString(), new PathBuilder<>(Object.class, qProduto.toString()));
		joins.put(qVasilhameEntregueItem.usuario.toString(), new PathBuilder<>(Object.class, qUsuario.toString()));
		
		this.pathBuilder = new PathBuilder<>(VasilhameEntregueItem.class,qVasilhameEntregueItem.toString());		
		this.filtrosBuilder = new G3FiltersBuilder<>(pathBuilder, VasilhameEntregueItem.class, joins);
		this.filtrosBuilder.applyFilters(filter, query);
		this.filtrosBuilder.applyOrder(filter, pathBuilder, query);
		
		return this.query.fetch();
	}
	
	public LoadRequestDTO generateFilter(VasilhameEntregueItemRelDTO dto) throws Exception {
		List<G3SqlFilter> filterFix = new ArrayList<>();				
		
		if (dto.idEmpresa != null && !dto.idEmpresa.equals("")) {
			G3SqlFilter filtroEmpresa = new G3SqlFilter("idEmpresa", dto.idEmpresa, "int", "in", "and");
			filterFix.add(filtroEmpresa);
		}
		if (dto.idFilial != null && !dto.idFilial.equals("")) {
			G3SqlFilter filtroFilial = new G3SqlFilter("idFilial", dto.idFilial, "int", "in", "and");
			filterFix.add(filtroFilial);
		}
		
		if (dto.idPessoa != null && !dto.idPessoa.equals("")) {
			G3SqlFilter filtroPessoa = new G3SqlFilter("vasilhameEntregue.idPessoa", dto.idPessoa, "int", "in", "and");
			filterFix.add(filtroPessoa);
		}
		if (dto.nome != null && !dto.nome.equals("")) {
			G3SqlFilter filtroNome = new G3SqlFilter("vasilhameEntregue.nome", dto.nome, "string", "in", "and");
			filterFix.add(filtroNome);
		}
		StringBuilder situacao = new StringBuilder();		
		if (dto.aberto != null && !dto.aberto.equals("")) {
			situacao.append(dto.aberto);
		}		
		if (dto.pendente != null && !dto.pendente.equals("")) {
			if (situacao.length() > 0) {
                situacao.append(",");
            }
			situacao.append(dto.pendente);
        }
		if (dto.inutilizado != null && !dto.inutilizado.equals("")) {
        	if (situacao.length() > 0) {
                situacao.append(",");
            }
        	situacao.append(dto.inutilizado);
        }
		if (dto.fechado != null && !dto.fechado.equals("")) {
        	if (situacao.length() > 0) {
                situacao.append(",");
            }
        	situacao.append(dto.fechado);
        }		
		if (situacao.length() > 0) {          
			G3SqlFilter filtroSituacao = new G3SqlFilter("vasilhameEntregue.situacao", situacao.toString(), "int", "in", "and");
			filterFix.add(filtroSituacao);
        }
		if (dto.idUsuarioEntregue != null && !dto.idUsuarioEntregue.equals("")) {
			G3SqlFilter filtroUsuarioEntregue = new G3SqlFilter("vasilhameEntregue.idUsuario", dto.idUsuarioEntregue, "string", "in", "and");
			filterFix.add(filtroUsuarioEntregue);
		}
		if (dto.idVasilhameEntregue != null && !dto.idVasilhameEntregue.equals("")) {
			G3SqlFilter filtroVasilhame = new G3SqlFilter("idVasilhameEntregue", dto.idVasilhameEntregue, "int", "in", "and");
			filterFix.add(filtroVasilhame);
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

	
	public byte[] generatePdf(VasilhameEntregueItemRelDTO dto, Integer idEmpresa, String usuario, String nomeUsuario) throws Exception {		
		HashMap<String, Object> param = new HashMap<>();		
		String nomeEmpresa = "";
		InputStream cabecalhoIS = null;
		InputStream relatoIS = null;
		InputStream logo = null;
		byte[] pdf = null;
		
		try {	
		
			LoadRequestDTO filtros = this.generateFilter(dto);
			List<VasilhameEntregueItem> vasilhamesEntreguesItens = this.loadRecords(filtros);
							
	        cabecalhoIS = this.getClass().getClassLoader().getResourceAsStream("/relato/cabecalho_paisagem.jasper");
	        JasperReport cabecalho = (JasperReport) JRLoader.loadObject(cabecalhoIS);       
			relatoIS = this.getClass().getClassLoader().getResourceAsStream("/relato/vasilhame/VasilhameEntregueItem.jasper");
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
	        param.put("titulo", "RELATÓRIO DE VASILHAMES ENTREGUES - ITENS");                 
	        
	        pdf = JasperRunManager.runReportToPdf(relato, param, new JRBeanCollectionDataSource(vasilhamesEntreguesItens));
        
		} finally {
			if (cabecalhoIS != null) cabecalhoIS.close();
			if (relatoIS != null) relatoIS.close();
			if (logo != null) logo.close();			
		}
		
		return pdf;
	}

	
	
	public byte[] generateTxt(VasilhameEntregueItemRelDTO dto) throws Exception {		
		String quebraLinha = java.net.URLDecoder.decode(dto.quebraLinha, "UTF-8");
		LoadRequestDTO filtros = this.generateFilter(dto);
		List<VasilhameEntregueItem> vasilhamesEntreguesItens = this.loadRecords(filtros);
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
		txt.append("Vasilhame Entregue");
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
		
		for (VasilhameEntregueItem vasilhameEntregueItem : vasilhamesEntreguesItens) {
			txt.append(vasilhameEntregueItem.getIdCodigo());
			txt.append(dto.delimitador);
			txt.append(vasilhameEntregueItem.getIdEmpresa());
			txt.append(dto.delimitador);
			txt.append(vasilhameEntregueItem.getEmpresa().getNome());
			txt.append(dto.delimitador);
			txt.append(vasilhameEntregueItem.getIdFilial());
			txt.append(dto.delimitador);
			txt.append(vasilhameEntregueItem.getFilial().getDescricao());
			txt.append(dto.delimitador);
			if (vasilhameEntregueItem.getIdVasilhameEntregue() != null) {
				txt.append(vasilhameEntregueItem.getIdVasilhameEntregue());
			}
			txt.append(dto.delimitador);
			if (vasilhameEntregueItem.getIdProduto() != null
					&& vasilhameEntregueItem.getProduto().getDigito() != null) {
				txt.append(vasilhameEntregueItem.getIdProduto() + "-" + vasilhameEntregueItem.getProduto().getDigito());
			}
			txt.append(dto.delimitador);
			if (vasilhameEntregueItem.getProduto() != null) {
				if (vasilhameEntregueItem.getProduto().getDescricao() != null) {
					txt.append(vasilhameEntregueItem.getProduto().getDescricao());
				}
			}
			txt.append(dto.delimitador);
			if (vasilhameEntregueItem.getQuantidade() != null) {
				txt.append(df3.format(vasilhameEntregueItem.getQuantidade()));				
			}
			txt.append(dto.delimitador);
			if (vasilhameEntregueItem.getTipoGarrafeira() != null) {
				if (vasilhameEntregueItem.getTipoGarrafeira() == 0) {
					txt.append("0 - Não");
				} else if (vasilhameEntregueItem.getTipoGarrafeira() == 1) {
					txt.append("1 - Sim");
				}
			}
			txt.append(dto.delimitador);
			if (vasilhameEntregueItem.getSituacao() != null) {
				if (vasilhameEntregueItem.getSituacao() == 1) {
					txt.append("1 - Normal");
				} else if (vasilhameEntregueItem.getSituacao() == 2) {
					txt.append("2 - Cancelado");
				}
			}
			txt.append(dto.delimitador);
			if (vasilhameEntregueItem.getDataHoraGravacao() != null) {
				txt.append(vasilhameEntregueItem.getDataHoraGravacao()
						.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
			}
			txt.append(dto.delimitador);
			if (vasilhameEntregueItem.getIdUsuario() != null) {
				txt.append(vasilhameEntregueItem.getIdUsuario());
			}
			txt.append(dto.delimitador);
			if (vasilhameEntregueItem.getUsuario() != null) {
				if (vasilhameEntregueItem.getUsuario().getNome() != null) {
					txt.append(vasilhameEntregueItem.getUsuario().getNome());
				}
			}			
			txt.append(quebraLinha);
		}

		return txt.toString().getBytes();
	}

}