package com.g3sistemas.vasilhames.business;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.g3sistemas.estoques.entity.QProduto;
import com.g3sistemas.geral.business.EmpresaCadManagerBean;
import com.g3sistemas.geral.entity.Empresa;
import com.g3sistemas.geral.entity.QEmpresa;
import com.g3sistemas.geral.entity.QFilial;
import com.g3sistemas.geral.entity.QLogradouro;
import com.g3sistemas.geral.entity.QMunicipio;
import com.g3sistemas.geral.entity.QPessoa;
import com.g3sistemas.sistema.entity.QUsuario;
import com.g3sistemas.vasilhames.dto.VasilhameEntregueComprovanteRelDTO;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregue;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregueItem;
import com.g3sistemas.vasilhames.entity.VasilhameEntregue;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.jpa.impl.JPAQuery;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Stateless
public class VasilhameEntregueComprovanteRelManagerBean {

	@PersistenceContext(unitName="g3-ds")
	private EntityManager em;
	
	@EJB
	private EmpresaCadManagerBean empresaCadManagerBean;
	
	@EJB
	private VasilhameEntregueItemCadManagerBean vasilhameEntregueItemCadManagerBean;
	
	
	private QVasilhameEntregue qVasilhameEntregue;
	private QVasilhameEntregueItem qVasilhameEntregueItem;
	private QProduto qProduto;
	private QEmpresa qEmpresa;	
	private QFilial qFilial;
	private QMunicipio qMunicipio;
	private QLogradouro qLogradouro;
	private QPessoa qPessoa;	
	private QUsuario qUsuario;
	
	private JPAQuery<VasilhameEntregue> query;	
	
	private ConstructorExpression<VasilhameEntregue> projecaoVasilhame() {
		return QVasilhameEntregue.create(qVasilhameEntregue.id, qVasilhameEntregue.idEmpresa,
				QEmpresa.create(qEmpresa.id, qEmpresa.nome, qEmpresa.nomeFantasia, qEmpresa.paginaWeb,
						qEmpresa.extensaoLogo),
				qVasilhameEntregue.idFilial,
				QFilial.create(qFilial.codigoMunicipio,
						QMunicipio.create(qMunicipio.id, qMunicipio.nome, qMunicipio.siglaUF), qFilial.idLogradouro,
						QLogradouro.create(qLogradouro.id, qLogradouro.descricao), qFilial.endereco, qFilial.numero,
						qFilial.complemento, qFilial.bairro, qFilial.cep, qFilial.dddFone, qFilial.fone,
						qFilial.contribuinte, qFilial.cpfcnpj, qFilial.inscricaoEstadual, qFilial.inscricaoMunicipal),
				qVasilhameEntregue.idCodigo, qVasilhameEntregue.idPessoa,
				QPessoa.create(qPessoa.idEmpresa, qPessoa.idPessoa, qPessoa.digito, qPessoa.nome),
				qVasilhameEntregue.nome, qVasilhameEntregue.tipoContribuinte, qVasilhameEntregue.cpfcnpj,
				qVasilhameEntregue.observacao, qVasilhameEntregue.situacao, qVasilhameEntregue.dataHoraGravacao,
				qVasilhameEntregue.idUsuario, QUsuario.create(qUsuario.nome),
				GroupBy.list(QVasilhameEntregueItem.create(qVasilhameEntregueItem.idProduto,
						QProduto.create(qProduto.digito, qProduto.desReduzida),
						qVasilhameEntregueItem.quantidade)));
	}

	public List<VasilhameEntregue> find(VasilhameEntregue vasilhameEntregue) {
		this.query = new JPAQuery<>(this.em);
		this.qVasilhameEntregue = QVasilhameEntregue.vasilhameEntregue;
		this.qVasilhameEntregueItem = QVasilhameEntregueItem.vasilhameEntregueItem;
		this.qProduto = QProduto.produto;
		this.qEmpresa = QEmpresa.empresa;		
		this.qFilial = QFilial.filial;
		this.qMunicipio = QMunicipio.municipio;
		this.qLogradouro = QLogradouro.logradouro;
		this.qPessoa = QPessoa.pessoa;
		this.qUsuario = QUsuario.usuario;		
		
		return this.query.from(qVasilhameEntregue)
				.leftJoin(qVasilhameEntregue.itens, qVasilhameEntregueItem)
				.leftJoin(qVasilhameEntregueItem.produto, qProduto)
				.leftJoin(qVasilhameEntregue.empresa, qEmpresa)
				.leftJoin(qVasilhameEntregue.filial, qFilial)
				.leftJoin(qFilial.municipio, qMunicipio)
				.leftJoin(qFilial.logradouro, qLogradouro)
				.leftJoin(qVasilhameEntregue.pessoa, qPessoa)
				.leftJoin(qVasilhameEntregue.usuario, qUsuario)
				.where(qVasilhameEntregue.idEmpresa.eq(vasilhameEntregue.getIdEmpresa())
						.and(qVasilhameEntregue.idFilial.eq(vasilhameEntregue.getIdFilial()))
						.and(qVasilhameEntregue.idCodigo.eq(vasilhameEntregue.getIdCodigo()))
						.and(qVasilhameEntregueItem.situacao.ne(2)))
				.transform(
				         GroupBy.groupBy(qVasilhameEntregue.idEmpresa, qVasilhameEntregue.idFilial, 
				        		 qVasilhameEntregue.idCodigo)
				         .list(this.projecaoVasilhame()));
						//.as(this.projecaoLoad())).get(vasilhameEntregue.getId());
	}
	
	
	public byte[] generatePdf(VasilhameEntregueComprovanteRelDTO dto, Integer idEmpresa) throws Exception {		
		HashMap<String, Object> param = new HashMap<>();		
		InputStream relatoIS = null;
		InputStream relatoSubReport = null;
		InputStream logo = null;
		byte[] pdf = null;
		
		try {		
			
			List<VasilhameEntregue> vasilhameEntregues = this.find(dto.vasilhameEntregue);
			      
			relatoIS = this.getClass().getClassLoader().getResourceAsStream("/relato/vasilhame/VasilhameEntregueComprovante.jasper");
	        JasperReport relato = (JasperReport) JRLoader.loadObject(relatoIS);        
	        
	        relatoSubReport = this.getClass().getClassLoader().getResourceAsStream("/relato/vasilhame/VasilhameEntregueComprovanteItens.jasper");
	        JasperReport subReportItens = (JasperReport) JRLoader.loadObject(relatoSubReport);        
	        
			logo = new ByteArrayInputStream(new byte[0]);
			Empresa empresa = this.empresaCadManagerBean.findLogo(idEmpresa);
			if (empresa != null) {
				if (empresa.getLogo() != null) {
					logo = new ByteArrayInputStream(empresa.getLogo());
				} else {
					logo = this.getClass().getClassLoader().getResourceAsStream("/relato/images/noimage.jpg");
				}				

			} else {
				logo = this.getClass().getClassLoader().getResourceAsStream("/relato/images/noimage.jpg");
			}
		        
	        param.put("logo", logo);    
	        param.put("item", subReportItens);    
	        
	        pdf = JasperRunManager.runReportToPdf(relato, param, new JRBeanCollectionDataSource(vasilhameEntregues));
        
		} finally {			
			if (relatoIS != null) relatoIS.close();
			if (relatoSubReport != null) relatoSubReport.close();
			if (logo != null) logo.close();			
		}
		
		return pdf;
	}


}