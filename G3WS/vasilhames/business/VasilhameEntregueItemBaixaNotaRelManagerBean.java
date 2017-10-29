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
import com.g3sistemas.vasilhames.dto.VasilhameEntregueItemBaixaNotaRelDTO;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregueItemBaixa;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregueItemBaixaNota;
import com.g3sistemas.vasilhames.entity.VasilhameEntregueItemBaixaNota;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Stateless
public class VasilhameEntregueItemBaixaNotaRelManagerBean {

	@PersistenceContext(unitName="g3-ds")
	private EntityManager em;
	
	@EJB
	private EmpresaCadManagerBean empresaCadManagerBean;
	
	
	private QVasilhameEntregueItemBaixaNota qVasilhameEntregueItemBaixaNota;
	private QEmpresa qEmpresaBaixa;
	private QFilial qFilialBaixa;
	private QVasilhameEntregueItemBaixa qVasilhameEntregueItemBaixa;
	private QEmpresa qEmpresaNota;
	private QFilial qFilialNota;
	
	private JPAQuery<VasilhameEntregueItemBaixaNota> query;
	private PathBuilder<VasilhameEntregueItemBaixaNota> pathBuilder;
	private G3FiltersBuilder<VasilhameEntregueItemBaixaNota> filtrosBuilder;	
	
	private ConstructorExpression<VasilhameEntregueItemBaixaNota> projecaoVasilhameEntregueItemBaixaNota() {
		return QVasilhameEntregueItemBaixaNota.create(qVasilhameEntregueItemBaixaNota.idEmpresaBaixa,
				QEmpresa.create(qEmpresaBaixa.id, qEmpresaBaixa.nome), qVasilhameEntregueItemBaixaNota.idFilialBaixa,
				QFilial.create(qFilialBaixa.codigo, qFilialBaixa.descricao),
				qVasilhameEntregueItemBaixaNota.idVasilhameEntregueItemBaixa,
				qVasilhameEntregueItemBaixaNota.idEmpresaNota, QEmpresa.create(qEmpresaNota.id, qEmpresaNota.nome),
				qVasilhameEntregueItemBaixaNota.modelo, qVasilhameEntregueItemBaixaNota.idFilialNota,
				QFilial.create(qFilialNota.codigo, qFilialNota.descricao), qVasilhameEntregueItemBaixaNota.serie,
				qVasilhameEntregueItemBaixaNota.numero, qVasilhameEntregueItemBaixaNota.dataEmissao,
				qVasilhameEntregueItemBaixaNota.sequencia);
	}
	
	private List<VasilhameEntregueItemBaixaNota> loadRecords(LoadRequestDTO filter) throws Exception {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregueItemBaixaNota = QVasilhameEntregueItemBaixaNota.vasilhameEntregueItemBaixaNota;	
		this.qEmpresaBaixa = new QEmpresa("qEmpresaBaixa");
		this.qFilialBaixa = new QFilial("qFilialBaixa");
		this.qVasilhameEntregueItemBaixa = QVasilhameEntregueItemBaixa.vasilhameEntregueItemBaixa;	
		this.qEmpresaNota = new QEmpresa("qEmpresaNota");
		this.qFilialNota = new QFilial("qFilialNota");

		this.query.select(this.projecaoVasilhameEntregueItemBaixaNota())
			.from(qVasilhameEntregueItemBaixaNota)
			.leftJoin(qVasilhameEntregueItemBaixaNota.empresaBaixa, qEmpresaBaixa)
			.leftJoin(qVasilhameEntregueItemBaixaNota.filialBaixa, qFilialBaixa)
			.leftJoin(qVasilhameEntregueItemBaixaNota.vasilhameEntregueItemBaixa, qVasilhameEntregueItemBaixa)				
			.leftJoin(qVasilhameEntregueItemBaixaNota.empresaNota, qEmpresaNota)
			.leftJoin(qVasilhameEntregueItemBaixaNota.filialNota, qFilialNota);		
		
		Map<String, PathBuilder<?>> joins = new HashMap<String, PathBuilder<?>>();
		joins.put(qVasilhameEntregueItemBaixaNota.empresaBaixa.toString(), new PathBuilder<>(Object.class, qEmpresaBaixa.toString()));
		joins.put(qVasilhameEntregueItemBaixaNota.filialBaixa.toString(), new PathBuilder<>(Object.class, qFilialBaixa.toString()));
		joins.put(qVasilhameEntregueItemBaixaNota.vasilhameEntregueItemBaixa.toString(), new PathBuilder<>(Object.class, qVasilhameEntregueItemBaixa.toString()));		
		joins.put(qVasilhameEntregueItemBaixaNota.empresaNota.toString(), new PathBuilder<>(Object.class, qEmpresaNota.toString()));
		joins.put(qVasilhameEntregueItemBaixaNota.filialNota.toString(), new PathBuilder<>(Object.class, qFilialNota.toString()));
		
		this.pathBuilder = new PathBuilder<>(VasilhameEntregueItemBaixaNota.class,qVasilhameEntregueItemBaixaNota.toString());		
		this.filtrosBuilder = new G3FiltersBuilder<>(pathBuilder, VasilhameEntregueItemBaixaNota.class, joins);
		this.filtrosBuilder.applyFilters(filter, query);
		this.filtrosBuilder.applyOrder(filter, pathBuilder, query);
		
		return this.query.fetch();
	}
	
	public LoadRequestDTO generateFilter(VasilhameEntregueItemBaixaNotaRelDTO dto) throws Exception {
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
		if (dto.idEmpresaNota != null && !dto.idEmpresaNota.equals("")) {
			G3SqlFilter filtroEmpresaNota = new G3SqlFilter("idEmpresaNota", dto.idEmpresaNota, "int", "in", "and");
			filterFix.add(filtroEmpresaNota);
		}
		
		StringBuilder modelo = new StringBuilder();		
		if (dto.notaFiscal != null && !dto.notaFiscal.equals("")) {
			modelo.append(dto.notaFiscal);
		}		
		if (dto.notaFiscalAvulsa != null && !dto.notaFiscalAvulsa.equals("")) {
			if (modelo.length() > 0) {
				modelo.append(",");
            }
			modelo.append(dto.notaFiscalAvulsa);
        }
		if (dto.notaFiscalEletronica != null && !dto.notaFiscalEletronica.equals("")) {
        	if (modelo.length() > 0) {
        		modelo.append(",");
            }
        	modelo.append(dto.notaFiscalEletronica);
        }
		if (dto.notaFiscalConsumidorEletronica != null && !dto.notaFiscalConsumidorEletronica.equals("")) {
        	if (modelo.length() > 0) {
        		modelo.append(",");
            }
        	modelo.append(dto.notaFiscalConsumidorEletronica);
        }		
		if (modelo.length() > 0) {          
			G3SqlFilter filtroModelo = new G3SqlFilter("modelo", modelo.toString(), "string", "in", "and");
			filterFix.add(filtroModelo);
        }
		if (dto.idFilialNota != null && !dto.idFilialNota.equals("")) {
			G3SqlFilter filtroFilialNota = new G3SqlFilter("idFilialNota", dto.idFilialNota, "int", "in", "and");
			filterFix.add(filtroFilialNota);
		}
		return new LoadRequestDTO(filterFix, dto.sort);
	}

	
	public byte[] generatePdf(VasilhameEntregueItemBaixaNotaRelDTO dto, Integer idEmpresa, String usuario, String nomeUsuario) throws Exception {		
		HashMap<String, Object> param = new HashMap<>();		
		String nomeEmpresa = "";	
		InputStream cabecalhoIS = null;
		InputStream relatoIS = null;
		InputStream logo = null;
		byte[] pdf = null;
		
		try {		
			LoadRequestDTO filtros = this.generateFilter(dto);		
			List<VasilhameEntregueItemBaixaNota> vasEntIteBaixasNotas = this.loadRecords(filtros);
							
	        cabecalhoIS = this.getClass().getClassLoader().getResourceAsStream("/relato/cabecalho_paisagem.jasper");
	        JasperReport cabecalho = (JasperReport) JRLoader.loadObject(cabecalhoIS);       
		    relatoIS = this.getClass().getClassLoader().getResourceAsStream("/relato/vasilhame/VasilhameEntregueItemBaixaNota.jasper");
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
	        param.put("titulo", "RELATÓRIO DE VASILHAMES ENTREGUES ITENS BAIXAS - NOTAS");                 
	        
	        pdf = JasperRunManager.runReportToPdf(relato, param, new JRBeanCollectionDataSource(vasEntIteBaixasNotas));
		} finally {
			if (cabecalhoIS != null) cabecalhoIS.close();
			if (relatoIS != null) relatoIS.close();
			if (logo != null)logo.close();
		}

		return pdf;
	}	
	
	public byte[] generateTxt(VasilhameEntregueItemBaixaNotaRelDTO dto) throws Exception {		
		String quebraLinha = java.net.URLDecoder.decode(dto.quebraLinha, "UTF-8");
		LoadRequestDTO filtros = this.generateFilter(dto);
		List<VasilhameEntregueItemBaixaNota> vasEntIteBaixasNotas = this.loadRecords(filtros);
		
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
		txt.append("Empresa Nota");
		txt.append(dto.delimitador);
		txt.append("Nome Empresa Nota");
		txt.append(dto.delimitador);
		txt.append("Modelo");				
		txt.append(dto.delimitador);
		txt.append("Filial Nota");
		txt.append(dto.delimitador);
		txt.append("Descrição Filial Nota");
		txt.append(dto.delimitador);		
		txt.append("Série");				
		txt.append(dto.delimitador);
		txt.append("Número");
		txt.append(dto.delimitador);
		txt.append("Data Emissão");
		txt.append(dto.delimitador);
		txt.append("Sequência");
		txt.append(quebraLinha);		
		
		for (VasilhameEntregueItemBaixaNota vasEntIteBaixaNota : vasEntIteBaixasNotas) {			
			txt.append(vasEntIteBaixaNota.getIdEmpresaBaixa());
			txt.append(dto.delimitador);
			txt.append(vasEntIteBaixaNota.getEmpresaBaixa().getNome());
			txt.append(dto.delimitador);
			txt.append(vasEntIteBaixaNota.getIdFilialBaixa());
			txt.append(dto.delimitador);
			txt.append(vasEntIteBaixaNota.getFilialBaixa().getDescricao());
			txt.append(dto.delimitador);			
			txt.append(vasEntIteBaixaNota.getIdVasilhameEntregueItemBaixa());
			txt.append(dto.delimitador);
			txt.append(vasEntIteBaixaNota.getIdEmpresaNota());
			txt.append(dto.delimitador);
			txt.append(vasEntIteBaixaNota.getEmpresaNota().getNome());
			txt.append(dto.delimitador);
			txt.append(vasEntIteBaixaNota.getModelo());
			txt.append(dto.delimitador);
			txt.append(vasEntIteBaixaNota.getIdFilialNota());
			txt.append(dto.delimitador);
			txt.append(vasEntIteBaixaNota.getFilialNota().getDescricao());
			txt.append(dto.delimitador);
			txt.append(vasEntIteBaixaNota.getSerie());
			txt.append(dto.delimitador);
			txt.append(vasEntIteBaixaNota.getNumero());
			txt.append(dto.delimitador);			
			txt.append(vasEntIteBaixaNota.getDataEmissao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			txt.append(dto.delimitador);
			txt.append(vasEntIteBaixaNota.getSequencia());
			txt.append(dto.delimitador);
			txt.append(quebraLinha);
		}

		return txt.toString().getBytes();
	}

}