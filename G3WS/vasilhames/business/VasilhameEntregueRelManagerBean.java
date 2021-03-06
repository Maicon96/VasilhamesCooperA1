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
import com.g3sistemas.geral.entity.QPessoa;
import com.g3sistemas.sistema.entity.QUsuario;
import com.g3sistemas.sql.filter.G3FiltersBuilder;
import com.g3sistemas.sql.filter.G3SqlFilter;
import com.g3sistemas.vasilhames.dto.VasilhameEntregueRelDTO;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregue;
import com.g3sistemas.vasilhames.entity.VasilhameEntregue;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Stateless
public class VasilhameEntregueRelManagerBean {

	@PersistenceContext(unitName="g3-ds")
	private EntityManager em;
	
	@EJB
	private EmpresaCadManagerBean empresaCadManagerBean;
	
	
	private QVasilhameEntregue qVasilhameEntregue;
	private QEmpresa qEmpresa;
	private QFilial qFilial;	
	private QPessoa qPessoa;	
	private QUsuario qUsuario;
	
	private JPAQuery<VasilhameEntregue> query;
	private PathBuilder<VasilhameEntregue> pathBuilder;
	private G3FiltersBuilder<VasilhameEntregue> filtrosBuilder;	
	
	private ConstructorExpression<VasilhameEntregue> projecaoVasilhameEntregue() {
		return QVasilhameEntregue.create(qVasilhameEntregue.id, qVasilhameEntregue.idEmpresa,
				QEmpresa.create(qEmpresa.id, qEmpresa.nome), qVasilhameEntregue.idFilial,
				QFilial.create(qFilial.codigo, qFilial.descricao), qVasilhameEntregue.idCodigo,
				qVasilhameEntregue.idPessoa,
				QPessoa.create(qPessoa.idPessoa, qPessoa.digito, qPessoa.nome, qPessoa.contribuinte, qPessoa.cpfCnpj),
				qVasilhameEntregue.nome, qVasilhameEntregue.tipoContribuinte, qVasilhameEntregue.cpfcnpj,
				qVasilhameEntregue.observacao, qVasilhameEntregue.situacao, qVasilhameEntregue.dataHoraGravacao,
				qVasilhameEntregue.idUsuario, QUsuario.create(qUsuario.nome));
	}
	
	private List<VasilhameEntregue> loadRecords(LoadRequestDTO filter) throws Exception {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregue = QVasilhameEntregue.vasilhameEntregue;		
		this.qEmpresa = QEmpresa.empresa;
		this.qFilial = QFilial.filial;
		this.qPessoa = QPessoa.pessoa;
		this.qUsuario = QUsuario.usuario;

		this.query.select(this.projecaoVasilhameEntregue())
			.from(qVasilhameEntregue)
			.leftJoin(qVasilhameEntregue.empresa, qEmpresa)
			.leftJoin(qVasilhameEntregue.filial, qFilial)
			.leftJoin(qVasilhameEntregue.pessoa, qPessoa)
			.leftJoin(qVasilhameEntregue.usuario, qUsuario);		
		
		Map<String, PathBuilder<?>> joins = new HashMap<String, PathBuilder<?>>();
		joins.put(qVasilhameEntregue.empresa.toString(), new PathBuilder<>(Object.class, qEmpresa.toString()));
		joins.put(qVasilhameEntregue.filial.toString(), new PathBuilder<>(Object.class, qFilial.toString()));
		joins.put(qVasilhameEntregue.pessoa.toString(), new PathBuilder<>(Object.class, qPessoa.toString()));
		joins.put(qVasilhameEntregue.usuario.toString(), new PathBuilder<>(Object.class, qUsuario.toString()));
		
		this.pathBuilder = new PathBuilder<>(VasilhameEntregue.class,qVasilhameEntregue.toString());		
		this.filtrosBuilder = new G3FiltersBuilder<>(pathBuilder, VasilhameEntregue.class, joins);
		this.filtrosBuilder.applyFilters(filter, query);
		this.filtrosBuilder.applyOrder(filter, pathBuilder, query);
		
		return this.query.fetch();
	}
	
	public LoadRequestDTO generateFilter(VasilhameEntregueRelDTO dto) throws Exception {
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
			G3SqlFilter filtroPessoa = new G3SqlFilter("idPessoa", dto.idPessoa, "int", "in", "and");
			filterFix.add(filtroPessoa);
		}
		if (dto.nome != null && !dto.nome.equals("")) {
			G3SqlFilter filtroNome = new G3SqlFilter("nome", dto.nome, "string", "in", "and");
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
			G3SqlFilter filtroSituacao = new G3SqlFilter("situacao", situacao.toString(), "int", "in", "and");
			filterFix.add(filtroSituacao);
        }		
		if (dto.idUsuario != null && !dto.idUsuario.equals("")) {
			G3SqlFilter filtroUsuario = new G3SqlFilter("idUsuario", dto.idUsuario, "string", "in", "and");
			filterFix.add(filtroUsuario);
		}
		return new LoadRequestDTO(filterFix, dto.sort);
	}

	
	public byte[] generatePdf(VasilhameEntregueRelDTO dto, Integer idEmpresa, String usuario, String nomeUsuario) throws Exception {		
		HashMap<String, Object> param = new HashMap<>();		
		String nomeEmpresa = "";	
		InputStream cabecalhoIS = null;
		InputStream relatoIS = null;
		InputStream logo = null;
		byte[] pdf = null;
		
		try {	
			LoadRequestDTO filtros = this.generateFilter(dto);
			List<VasilhameEntregue> vasilhamesEntregues = this.loadRecords(filtros);
							
	        cabecalhoIS = this.getClass().getClassLoader().getResourceAsStream("/relato/cabecalho_paisagem.jasper");
	        JasperReport cabecalho = (JasperReport) JRLoader.loadObject(cabecalhoIS);       
			relatoIS = this.getClass().getClassLoader().getResourceAsStream("/relato/vasilhame/VasilhameEntregue.jasper");
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
	        param.put("titulo", "RELATÓRIO DE VASILHAMES ENTREGUES");                 
	        
	        pdf = JasperRunManager.runReportToPdf(relato, param, new JRBeanCollectionDataSource(vasilhamesEntregues));
        
		} finally {
			if (cabecalhoIS != null) cabecalhoIS.close();
			if (relatoIS != null) relatoIS.close();
			if (logo != null) logo.close();			
		}
		
		return pdf;
	}

	
	
	public byte[] generateTxt(VasilhameEntregueRelDTO dto) throws Exception {		
		String quebraLinha = java.net.URLDecoder.decode(dto.quebraLinha, "UTF-8");
		LoadRequestDTO filtros = this.generateFilter(dto);
		List<VasilhameEntregue> vasilhamesEntregues = this.loadRecords(filtros);

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
		txt.append("Pessoa");
		txt.append(dto.delimitador);
		txt.append("Nome");
		txt.append(dto.delimitador);
		txt.append("CPF/CNPJ");
		txt.append(dto.delimitador);
		txt.append("Situação");		
		txt.append(dto.delimitador);
		txt.append("Data/Hora Situação");
		txt.append(dto.delimitador);
		txt.append("Usuário");
		txt.append(dto.delimitador);
		txt.append("Nome Usuário");
		txt.append(quebraLinha);		
		
		for (VasilhameEntregue vasilhameEntregue : vasilhamesEntregues) {
			txt.append(vasilhameEntregue.getIdCodigo());
			txt.append(dto.delimitador);
			txt.append(vasilhameEntregue.getIdEmpresa());
			txt.append(dto.delimitador);
			txt.append(vasilhameEntregue.getEmpresa().getNome());
			txt.append(dto.delimitador);
			txt.append(vasilhameEntregue.getIdFilial());
			txt.append(dto.delimitador);
			txt.append(vasilhameEntregue.getFilial().getDescricao());
			txt.append(dto.delimitador);
			if (vasilhameEntregue.getIdPessoa() != null) {
				if (vasilhameEntregue.getPessoa() != null) {
					if (vasilhameEntregue.getPessoa().getDigito() != null) {
						txt.append(vasilhameEntregue.getIdPessoa() + "-" + vasilhameEntregue.getPessoa().getDigito());
					}
				}
			}				
			txt.append(dto.delimitador);
			if (vasilhameEntregue.getNome() != null) {
				txt.append(vasilhameEntregue.getNome());	
			}
			txt.append(dto.delimitador);
			if (vasilhameEntregue.getCpfcnpj() != null) {
				txt.append(vasilhameEntregue.getCpfcnpj());	
			}			
			txt.append(dto.delimitador);
			if (vasilhameEntregue.getSituacao() != null) {
				if (vasilhameEntregue.getSituacao() == 1) {
					txt.append("1 - Aberto");
				} else if (vasilhameEntregue.getSituacao() == 2) {
					txt.append("2 - Pendente");
				} else if (vasilhameEntregue.getSituacao() == 3) {
					txt.append("8 - Inutilizado");
				} else if (vasilhameEntregue.getSituacao() == 4) {
					txt.append("9 - Fechado");
				}
			}
			txt.append(dto.delimitador);
			if (vasilhameEntregue.getDataHoraGravacao() != null) {
				txt.append(vasilhameEntregue.getDataHoraGravacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));	
			}			
			txt.append(dto.delimitador);
			if (vasilhameEntregue.getIdUsuario() != null) {
				txt.append(vasilhameEntregue.getIdUsuario());
			}			
			txt.append(dto.delimitador);
			if (vasilhameEntregue.getUsuario() != null) {
				if (vasilhameEntregue.getUsuario().getNome() != null) {
					txt.append(vasilhameEntregue.getUsuario().getNome());	
				}				
			}			
			txt.append(quebraLinha);
		}

		return txt.toString().getBytes();
	}

}