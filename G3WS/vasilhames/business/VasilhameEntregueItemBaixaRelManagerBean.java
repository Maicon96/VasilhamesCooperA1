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
import com.g3sistemas.vasilhames.dto.VasilhameEntregueItemBaixaRelDTO;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregue;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregueItem;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregueItemBaixa;
import com.g3sistemas.vasilhames.entity.VasilhameEntregueItemBaixa;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Stateless
public class VasilhameEntregueItemBaixaRelManagerBean {

	@PersistenceContext(unitName="g3-ds")
	private EntityManager em;
	
	@EJB
	private EmpresaCadManagerBean empresaCadManagerBean;	
	
	private QVasilhameEntregueItemBaixa qVasilhameEntregueItemBaixa;
	private QEmpresa qEmpresa;
	private QFilial qFilial;
	private QVasilhameEntregue qVasilhameEntregue;
	private QVasilhameEntregueItem qVasilhameEntregueItem;	
	private QUsuario qUsuario;
	
	private JPAQuery<VasilhameEntregueItemBaixa> query;
	private PathBuilder<VasilhameEntregueItemBaixa> pathBuilder;
	private G3FiltersBuilder<VasilhameEntregueItemBaixa> filtrosBuilder;	
	
	private ConstructorExpression<VasilhameEntregueItemBaixa> projecaoVasilhameEntregueItemBaixa() {
		return QVasilhameEntregueItemBaixa.create(qVasilhameEntregueItemBaixa.id, qVasilhameEntregueItemBaixa.idEmpresa,
				QEmpresa.create(qEmpresa.id, qEmpresa.nome), qVasilhameEntregueItemBaixa.idFilial,
				QFilial.create(qFilial.codigo, qFilial.descricao), qVasilhameEntregueItemBaixa.idCodigo,
				qVasilhameEntregueItemBaixa.idVasilhameEntregueItem, qVasilhameEntregueItemBaixa.tipoBaixa,
				qVasilhameEntregueItemBaixa.quantidade, qVasilhameEntregueItemBaixa.dataHoraGravacao,
				qVasilhameEntregueItemBaixa.idUsuario, QUsuario.create(qUsuario.nome));
	}
	
	private List<VasilhameEntregueItemBaixa> loadRecords(LoadRequestDTO filter) throws Exception {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregueItemBaixa = QVasilhameEntregueItemBaixa.vasilhameEntregueItemBaixa;	
		this.qEmpresa = QEmpresa.empresa;
		this.qFilial = QFilial.filial;
		this.qVasilhameEntregueItem = QVasilhameEntregueItem.vasilhameEntregueItem;
		this.qVasilhameEntregue = QVasilhameEntregue.vasilhameEntregue;			
		this.qUsuario = QUsuario.usuario;

		this.query.select(this.projecaoVasilhameEntregueItemBaixa())
			.from(qVasilhameEntregueItemBaixa)
			.leftJoin(qVasilhameEntregueItemBaixa.empresa, qEmpresa)
			.leftJoin(qVasilhameEntregueItemBaixa.filial, qFilial)
			.leftJoin(qVasilhameEntregueItemBaixa.vasilhameEntregueItem, qVasilhameEntregueItem)
			.leftJoin(qVasilhameEntregueItem.vasilhameEntregue, qVasilhameEntregue)			
			.leftJoin(qVasilhameEntregueItemBaixa.usuario, qUsuario);	
		
		Map<String, PathBuilder<?>> joins = new HashMap<String, PathBuilder<?>>();
		joins.put(qVasilhameEntregueItemBaixa.empresa.toString(), new PathBuilder<>(Object.class, qEmpresa.toString()));
		joins.put(qVasilhameEntregueItemBaixa.filial.toString(), new PathBuilder<>(Object.class, qFilial.toString()));
		joins.put(qVasilhameEntregueItemBaixa.vasilhameEntregueItem.toString(), new PathBuilder<>(Object.class, qVasilhameEntregueItem.toString()));
		joins.put(qVasilhameEntregueItem.vasilhameEntregue.toString(), new PathBuilder<>(Object.class, qVasilhameEntregue.toString()));		
		joins.put(qVasilhameEntregueItemBaixa.usuario.toString(), new PathBuilder<>(Object.class, qUsuario.toString()));
		
		this.pathBuilder = new PathBuilder<>(VasilhameEntregueItemBaixa.class,qVasilhameEntregueItemBaixa.toString());		
		this.filtrosBuilder = new G3FiltersBuilder<>(pathBuilder, VasilhameEntregueItemBaixa.class, joins);
		this.filtrosBuilder.applyFilters(filter, query);
		this.filtrosBuilder.applyOrder(filter, pathBuilder, query);
		
		return this.query.fetch();
	}
	
	public LoadRequestDTO generateFilter(VasilhameEntregueItemBaixaRelDTO dto) throws Exception {
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
			G3SqlFilter filtroPessoa = new G3SqlFilter("vasilhameEntregueItem.vasilhameEntregue.idPessoa", dto.idPessoa, "int", "in", "and");
			filterFix.add(filtroPessoa);
		}
		if (dto.nome != null && !dto.nome.equals("")) {
			G3SqlFilter filtroNome = new G3SqlFilter("vasilhameEntregueItem.vasilhameEntregue.nome", dto.nome, "string", "in", "and");
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
			G3SqlFilter filtroSituacao = new G3SqlFilter("vasilhameEntregueItem.vasilhameEntregue.situacao", situacao.toString(), "int", "in", "and");
			filterFix.add(filtroSituacao);
        }			
		if (dto.idUsuarioEntregue != null && !dto.idUsuarioEntregue.equals("")) {
			G3SqlFilter filtroUsuarioEntregue = new G3SqlFilter("vasilhameEntregueItem.vasilhameEntregue.idUsuario", dto.idUsuarioEntregue, "string", "in", "and");
			filterFix.add(filtroUsuarioEntregue);
		}
		if (dto.idVasilhameEntregue != null && !dto.idVasilhameEntregue.equals("")) {
			G3SqlFilter filtroVasilhame = new G3SqlFilter("vasilhameEntregueItem.idVasilhameEntregue", dto.idVasilhameEntregue, "int", "in", "and");
			filterFix.add(filtroVasilhame);
		}
		if (dto.idProduto != null && !dto.idProduto.equals("")) {
			G3SqlFilter filtroProduto = new G3SqlFilter("vasilhameEntregueItem.idProduto", dto.idProduto, "int", "in", "and");
			filterFix.add(filtroProduto);
		}		
		if (dto.garrafeira != null && !dto.garrafeira.equals("")) {
			G3SqlFilter filtroGarrafeira = new G3SqlFilter("vasilhameEntregueItem.tipoGarrafeira", dto.garrafeira, "int", "in", "and");
			filterFix.add(filtroGarrafeira);
		}
		if (dto.situacao != null && !dto.situacao.equals("")) {
			G3SqlFilter filtroSituacao = new G3SqlFilter("vasilhameEntregueItem.situacao", dto.situacao, "int", "in", "and");
			filterFix.add(filtroSituacao);
		}		
		if (dto.idVasilhameEntregueItem != null && !dto.idVasilhameEntregueItem.equals("")) {
			G3SqlFilter filtroItem = new G3SqlFilter("idVasilhameEntregueItem", dto.idVasilhameEntregueItem, "int", "in", "and");
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

	
	public byte[] generatePdf(VasilhameEntregueItemBaixaRelDTO dto, Integer idEmpresa, String usuario, String nomeUsuario) throws Exception {		
		HashMap<String, Object> param = new HashMap<>();		
		String nomeEmpresa = "";
		InputStream cabecalhoIS = null;
		InputStream relatoIS = null;
		InputStream logo = null;
		byte[] pdf = null;

		try {
			LoadRequestDTO filtros = this.generateFilter(dto);
			List<VasilhameEntregueItemBaixa> vasilhamesEntreguesItensBaixas = this.loadRecords(filtros);

			cabecalhoIS = this.getClass().getClassLoader().getResourceAsStream("/relato/cabecalho_retrato.jasper");
			JasperReport cabecalho = (JasperReport) JRLoader.loadObject(cabecalhoIS);
			relatoIS = this.getClass().getClassLoader().getResourceAsStream("/relato/vasilhame/VasilhameEntregueItemBaixa.jasper");
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
			param.put("titulo", "RELATÓRIO DE VASILHAMES ENTREGUES ITENS - BAIXAS");

			pdf = JasperRunManager.runReportToPdf(relato, param, new JRBeanCollectionDataSource(vasilhamesEntreguesItensBaixas));
		} finally {
			if (cabecalhoIS != null) cabecalhoIS.close();
			if (relatoIS != null) relatoIS.close();
			if (logo != null)logo.close();
		}

		return pdf;
	}	
	
	public byte[] generateTxt(VasilhameEntregueItemBaixaRelDTO dto) throws Exception {		
		String quebraLinha = java.net.URLDecoder.decode(dto.quebraLinha, "UTF-8");
		LoadRequestDTO filtros = this.generateFilter(dto);
		List<VasilhameEntregueItemBaixa> vasilhamesEntreguesItensBaixas = this.loadRecords(filtros);
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
		txt.append("Vasilhame Entregue Item");
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
		
		for (VasilhameEntregueItemBaixa vasilhameEntregueItemBaixa : vasilhamesEntreguesItensBaixas) {
			txt.append(vasilhameEntregueItemBaixa.getIdCodigo());
			txt.append(dto.delimitador);
			txt.append(vasilhameEntregueItemBaixa.getIdEmpresa());
			txt.append(dto.delimitador);
			txt.append(vasilhameEntregueItemBaixa.getEmpresa().getNome());
			txt.append(dto.delimitador);
			txt.append(vasilhameEntregueItemBaixa.getIdFilial());
			txt.append(dto.delimitador);
			txt.append(vasilhameEntregueItemBaixa.getFilial().getDescricao());
			txt.append(dto.delimitador);
			if (vasilhameEntregueItemBaixa.getIdVasilhameEntregueItem() != null) {
				txt.append(vasilhameEntregueItemBaixa.getIdVasilhameEntregueItem());
			}
			txt.append(dto.delimitador);
			if (vasilhameEntregueItemBaixa.getTipoBaixa() != null) {
				if (vasilhameEntregueItemBaixa.getTipoBaixa() == 1) {
					txt.append("1 - Venda");
				} else if (vasilhameEntregueItemBaixa.getTipoBaixa() == 2) {
					txt.append("2 - Devolução");
				}
			}
			txt.append(dto.delimitador);			
			if (vasilhameEntregueItemBaixa.getQuantidade() != null) {
				txt.append(df3.format(vasilhameEntregueItemBaixa.getQuantidade()));				
			}			
			txt.append(dto.delimitador);
			if (vasilhameEntregueItemBaixa.getDataHoraGravacao() != null) {
				txt.append(vasilhameEntregueItemBaixa.getDataHoraGravacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
			}
			txt.append(dto.delimitador);
			if (vasilhameEntregueItemBaixa.getIdUsuario() != null) {
				txt.append(vasilhameEntregueItemBaixa.getIdUsuario());
			}
			txt.append(dto.delimitador);
			if (vasilhameEntregueItemBaixa.getUsuario() != null) {
				if (vasilhameEntregueItemBaixa.getUsuario().getNome() != null) {
					txt.append(vasilhameEntregueItemBaixa.getUsuario().getNome());
				}
			}			
			txt.append(quebraLinha);
		}

		return txt.toString().getBytes();
	}

}