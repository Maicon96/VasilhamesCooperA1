package com.g3sistemas.vasilhames.business;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
import com.g3sistemas.sql.filter.G3FiltersBuilder;
import com.g3sistemas.sql.filter.G3SqlFilter;
import com.g3sistemas.vasilhames.dto.VasilhameEntregueItemBaixaCupomRelDTO;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregueItemBaixa;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregueItemBaixaCupom;
import com.g3sistemas.vasilhames.entity.VasilhameEntregueItemBaixaCupom;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Stateless
public class VasilhameEntregueItemBaixaCupomRelManagerBean {

	@PersistenceContext(unitName="g3-ds")
	private EntityManager em;
	
	@EJB
	private EmpresaCadManagerBean empresaCadManagerBean;
	
	
	private QVasilhameEntregueItemBaixaCupom qVasilhameEntregueItemBaixaCupom;
	private QEmpresa qEmpresaBaixa;
	private QFilial qFilialBaixa;
	private QVasilhameEntregueItemBaixa qVasilhameEntregueItemBaixa;
	private QEmpresa qEmpresaCupom;
	private QFilial qFilialCupom;
	
	private JPAQuery<VasilhameEntregueItemBaixaCupom> query;
	private PathBuilder<VasilhameEntregueItemBaixaCupom> pathBuilder;
	private G3FiltersBuilder<VasilhameEntregueItemBaixaCupom> filtrosBuilder;	
	
	private ConstructorExpression<VasilhameEntregueItemBaixaCupom> projecaoVasilhameEntregueItemBaixaCupom() {
		return QVasilhameEntregueItemBaixaCupom.create(qVasilhameEntregueItemBaixaCupom.idEmpresaBaixa, QEmpresa.create(qEmpresaBaixa.id, qEmpresaBaixa.nome),
				qVasilhameEntregueItemBaixaCupom.idFilialBaixa, QFilial.create(qFilialBaixa.codigo, 
				qFilialBaixa.descricao), qVasilhameEntregueItemBaixaCupom.idVasilhameEntregueItemBaixa,
				qVasilhameEntregueItemBaixaCupom.idEmpresaCupom, QEmpresa.create(qEmpresaCupom.id, qEmpresaCupom.nome),
				qVasilhameEntregueItemBaixaCupom.idFilialCupom, QFilial.create(qFilialCupom.codigo, qFilialCupom.descricao),
				qVasilhameEntregueItemBaixaCupom.idEcfCupom, qVasilhameEntregueItemBaixaCupom.numero, 
				qVasilhameEntregueItemBaixaCupom.dataEmissao, qVasilhameEntregueItemBaixaCupom.sequencia);
	}
	
	private List<VasilhameEntregueItemBaixaCupom> loadRecords(LoadRequestDTO filter) throws Exception {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregueItemBaixaCupom = QVasilhameEntregueItemBaixaCupom.vasilhameEntregueItemBaixaCupom;	
		this.qEmpresaBaixa = new QEmpresa("qEmpresaBaixa");
		this.qFilialBaixa = new QFilial("qFilialBaixa");
		this.qVasilhameEntregueItemBaixa = QVasilhameEntregueItemBaixa.vasilhameEntregueItemBaixa;	
		this.qEmpresaCupom = new QEmpresa("qEmpresaCupom");
		this.qFilialCupom = new QFilial("qFilialCupom");

		this.query.select(this.projecaoVasilhameEntregueItemBaixaCupom())
			.from(qVasilhameEntregueItemBaixaCupom)
			.leftJoin(qVasilhameEntregueItemBaixaCupom.empresaBaixa, qEmpresaBaixa)
			.leftJoin(qVasilhameEntregueItemBaixaCupom.filialBaixa, qFilialBaixa)
			.leftJoin(qVasilhameEntregueItemBaixaCupom.vasilhameEntregueItemBaixa, qVasilhameEntregueItemBaixa)				
			.leftJoin(qVasilhameEntregueItemBaixaCupom.empresaCupom, qEmpresaCupom)
			.leftJoin(qVasilhameEntregueItemBaixaCupom.filialCupom, qFilialCupom);		
		
		Map<String, PathBuilder<?>> joins = new HashMap<String, PathBuilder<?>>();
		joins.put(qVasilhameEntregueItemBaixaCupom.empresaBaixa.toString(), new PathBuilder<>(Object.class, qEmpresaBaixa.toString()));
		joins.put(qVasilhameEntregueItemBaixaCupom.filialBaixa.toString(), new PathBuilder<>(Object.class, qFilialBaixa.toString()));
		joins.put(qVasilhameEntregueItemBaixaCupom.vasilhameEntregueItemBaixa.toString(), new PathBuilder<>(Object.class, qVasilhameEntregueItemBaixa.toString()));		
		joins.put(qVasilhameEntregueItemBaixaCupom.empresaCupom.toString(), new PathBuilder<>(Object.class, qEmpresaCupom.toString()));
		joins.put(qVasilhameEntregueItemBaixaCupom.filialCupom.toString(), new PathBuilder<>(Object.class, qFilialCupom.toString()));
		
		this.pathBuilder = new PathBuilder<>(VasilhameEntregueItemBaixaCupom.class,qVasilhameEntregueItemBaixaCupom.toString());		
		this.filtrosBuilder = new G3FiltersBuilder<>(pathBuilder, VasilhameEntregueItemBaixaCupom.class, joins);
		this.filtrosBuilder.applyFilters(filter, query);
		this.filtrosBuilder.applyOrder(filter, pathBuilder, query);
		
		return this.query.fetch();
	}
	
	public LoadRequestDTO generateFilter(VasilhameEntregueItemBaixaCupomRelDTO dto) throws Exception {
		List<G3SqlFilter> filterFix = new ArrayList<>();				
		
		if (dto.idEmpresaBaixa != null && !dto.idEmpresaBaixa.equals("")) {
			G3SqlFilter filtroEmpresaBaixa = new G3SqlFilter("idEmpresaBaixa", dto.idEmpresaBaixa, "int", "in", "and");
			filterFix.add(filtroEmpresaBaixa);
		}
		if (dto.idFilialBaixa != null && !dto.idFilialBaixa.equals("")) {
			G3SqlFilter filtroFilial = new G3SqlFilter("idFilialBaixa", dto.idFilialBaixa, "int", "in", "and");
			filterFix.add(filtroFilial);
		}
		if (dto.idPessoa != null && !dto.idPessoa.equals("")) {
			G3SqlFilter filtroPessoa = new G3SqlFilter("vasilhameEntregueItemBaixa.vasilhameEntregueItem.vasilhameEntregue.idPessoa", dto.idPessoa, "int", "in", "and");
			filterFix.add(filtroPessoa);
		}
		if (dto.nome != null && !dto.nome.equals("")) {
			G3SqlFilter filtroNome = new G3SqlFilter("vasilhameEntregueItemBaixa.vasilhameEntregueItem.vasilhameEntregue.nome", dto.nome, "string", "in", "and");
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
			G3SqlFilter filtroSituacao = new G3SqlFilter("vasilhameEntregueItemBaixa.vasilhameEntregueItem.vasilhameEntregue.situacao", situacao.toString(), "int", "in", "and");
			filterFix.add(filtroSituacao);
        }	
		if (dto.idUsuarioEntregue != null && !dto.idUsuarioEntregue.equals("")) {
			G3SqlFilter filtroUsuarioEntregue = new G3SqlFilter("vasilhameEntregueItemBaixa.vasilhameEntregueItem.vasilhameEntregue.idUsuario", dto.idUsuarioEntregue, "string", "in", "and");
			filterFix.add(filtroUsuarioEntregue);
		}
		if (dto.idVasilhameEntregue != null && !dto.idVasilhameEntregue.equals("")) {
			G3SqlFilter filtroVasilhame = new G3SqlFilter("vasilhameEntregueItemBaixa.vasilhameEntregueItem.idVasilhameEntregue", dto.idVasilhameEntregue, "int", "in", "and");
			filterFix.add(filtroVasilhame);
		}
		if (dto.idProduto != null && !dto.idProduto.equals("")) {
			G3SqlFilter filtroProduto = new G3SqlFilter("vasilhameEntregueItemBaixa.vasilhameEntregueItem.idProduto", dto.idProduto, "int", "in", "and");
			filterFix.add(filtroProduto);
		}		
		if (dto.garrafeira != null && !dto.garrafeira.equals("")) {
			G3SqlFilter filtroGarrafeira = new G3SqlFilter("vasilhameEntregueItemBaixa.vasilhameEntregueItem.tipoGarrafeira", dto.garrafeira, "int", "in", "and");
			filterFix.add(filtroGarrafeira);
		}
		if (dto.situacao != null && !dto.situacao.equals("")) {
			G3SqlFilter filtroSituacao = new G3SqlFilter("vasilhameEntregueItemBaixa.vasilhameEntregueItem.situacao", dto.situacao, "int", "in", "and");
			filterFix.add(filtroSituacao);
		}		
		if (dto.idVasilhameEntregueItem != null && !dto.idVasilhameEntregueItem.equals("")) {
			G3SqlFilter filtroItem = new G3SqlFilter("vasilhameEntregueItemBaixa.idVasilhameEntregueItem", dto.idVasilhameEntregueItem, "int", "in", "and");
			filterFix.add(filtroItem);
		}
		if (dto.tipoBaixa != null && !dto.tipoBaixa.equals("")) {
			G3SqlFilter filtroTipoBaixa = new G3SqlFilter("vasilhameEntregueItemBaixa.tipoBaixa", dto.tipoBaixa, "int", "in", "and");
			filterFix.add(filtroTipoBaixa);
		}
		if (dto.idVasilhameEntregueItemBaixa != null && !dto.idVasilhameEntregueItemBaixa.equals("")) {
			G3SqlFilter filtroBaixa = new G3SqlFilter("idVasilhameEntregueItemBaixa", dto.idVasilhameEntregueItemBaixa, "int", "in", "and");
			filterFix.add(filtroBaixa);
		}
		if (dto.idEmpresaCupom != null && !dto.idEmpresaCupom.equals("")) {
			G3SqlFilter filtroEmpresaCupom = new G3SqlFilter("idEmpresaCupom", dto.idEmpresaCupom, "int", "in", "and");
			filterFix.add(filtroEmpresaCupom);
		}
		if (dto.idFilialCupom != null && !dto.idFilialCupom.equals("")) {
			G3SqlFilter filtroEmpresaCupom = new G3SqlFilter("idFilialCupom", dto.idFilialCupom, "int", "in", "and");
			filterFix.add(filtroEmpresaCupom);
		}
		if (dto.idEcfCupom != null && !dto.idEcfCupom.equals("")) {
			G3SqlFilter filtroEcf = new G3SqlFilter("idEcfCupom", dto.idEcfCupom, "int", "in", "and");
			filterFix.add(filtroEcf);
		}
		
		return new LoadRequestDTO(filterFix, dto.sort);
	}

	
	public byte[] generatePdf(VasilhameEntregueItemBaixaCupomRelDTO dto, Integer idEmpresa, String usuario, String nomeUsuario) throws Exception {		
		HashMap<String, Object> param = new HashMap<>();		
		String nomeEmpresa = "";	
		InputStream cabecalhoIS = null;
		InputStream relatoIS = null;
		InputStream logo = null;
		byte[] pdf = null;
		
		try {		
			LoadRequestDTO filtros = this.generateFilter(dto);
			List<VasilhameEntregueItemBaixaCupom> vasEntIteBaixasCupons = this.loadRecords(filtros);
							
	        cabecalhoIS = this.getClass().getClassLoader().getResourceAsStream("/relato/cabecalho_paisagem.jasper");
	        JasperReport cabecalho = (JasperReport) JRLoader.loadObject(cabecalhoIS);       
		    relatoIS = this.getClass().getClassLoader().getResourceAsStream("/relato/vasilhame/VasilhameEntregueItemBaixaCupom.jasper");
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
	        param.put("titulo", "RELATÓRIO DE VASILHAMES ENTREGUES ITENS BAIXAS - CUPONS");                 
	        
	        pdf = JasperRunManager.runReportToPdf(relato, param, new JRBeanCollectionDataSource(vasEntIteBaixasCupons));
		} finally {
			if (cabecalhoIS != null) cabecalhoIS.close();
			if (relatoIS != null) relatoIS.close();
			if (logo != null)logo.close();
		}

		return pdf;
	}
	
	
	public byte[] generateTxt(VasilhameEntregueItemBaixaCupomRelDTO dto) throws Exception {		
		String quebraLinha = java.net.URLDecoder.decode(dto.quebraLinha, "UTF-8");
		LoadRequestDTO filtros = this.generateFilter(dto);
		List<VasilhameEntregueItemBaixaCupom> vasEntIteBaixasCupoms = this.loadRecords(filtros);
		
		StringBuilder txt = new StringBuilder();		
		txt.append("Empresa Baixa");
		txt.append(dto.delimitador);
		txt.append("Nome Empresa Baixa");
		txt.append(dto.delimitador);
		txt.append("Filial Baixa");
		txt.append(dto.delimitador);
		txt.append("Descrição Filial Baixa");
		txt.append(dto.delimitador);
		txt.append("Vas. Entregue Item Baixa");
		txt.append(dto.delimitador);		
		txt.append("Empresa Cupom");
		txt.append(dto.delimitador);
		txt.append("Nome Empresa Cupom");
		txt.append(dto.delimitador);				
		txt.append(dto.delimitador);
		txt.append("Filial Cupom");
		txt.append(dto.delimitador);
		txt.append("Descrição Filial Cupom");
		txt.append(dto.delimitador);		
		txt.append("ECF Cupom");				
		txt.append(dto.delimitador);
		txt.append("Número");
		txt.append(dto.delimitador);
		txt.append("Data Emissão");
		txt.append(dto.delimitador);
		txt.append("Sequência");
		txt.append(quebraLinha);		
		
		for (VasilhameEntregueItemBaixaCupom vasEntIteBaixaCupom : vasEntIteBaixasCupoms) {			
			txt.append(vasEntIteBaixaCupom.getIdEmpresaBaixa());
			txt.append(dto.delimitador);
			txt.append(vasEntIteBaixaCupom.getEmpresaBaixa().getNome());
			txt.append(dto.delimitador);
			txt.append(vasEntIteBaixaCupom.getIdFilialBaixa());
			txt.append(dto.delimitador);
			txt.append(vasEntIteBaixaCupom.getFilialBaixa().getDescricao());
			txt.append(dto.delimitador);			
			txt.append(vasEntIteBaixaCupom.getIdVasilhameEntregueItemBaixa());
			txt.append(dto.delimitador);
			txt.append(vasEntIteBaixaCupom.getIdEmpresaCupom());
			txt.append(dto.delimitador);
			txt.append(vasEntIteBaixaCupom.getEmpresaCupom().getNome());			
			txt.append(dto.delimitador);
			txt.append(vasEntIteBaixaCupom.getIdFilialCupom());
			txt.append(dto.delimitador);
			txt.append(vasEntIteBaixaCupom.getFilialCupom().getDescricao());
			txt.append(dto.delimitador);
			txt.append(vasEntIteBaixaCupom.getIdEcfCupom());
			txt.append(dto.delimitador);
			txt.append(vasEntIteBaixaCupom.getNumero());
			txt.append(dto.delimitador);			
			txt.append(vasEntIteBaixaCupom.getDataEmissao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			txt.append(dto.delimitador);
			txt.append(vasEntIteBaixaCupom.getSequencia());
			txt.append(dto.delimitador);
			txt.append(quebraLinha);
		}

		return txt.toString().getBytes();
	}

}