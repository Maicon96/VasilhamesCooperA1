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
import com.g3sistemas.vasilhames.dto.VasilhameEntregarComprovanteRelDTO;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregar;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregarItem;
import com.g3sistemas.vasilhames.entity.VasilhameEntregar;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.jpa.impl.JPAQuery;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Stateless
public class VasilhameEntregarComprovanteRelManagerBean {

	@PersistenceContext(unitName="g3-ds")
	private EntityManager em;
	
	@EJB
	private EmpresaCadManagerBean empresaCadManagerBean;
	
	@EJB
	private VasilhameEntregarItemCadManagerBean vasilhameEntregueItemCadManagerBean;
	
	
	private QVasilhameEntregar qVasilhameEntregar;
	private QVasilhameEntregarItem qVasilhameEntregarItem;
	private QProduto qProduto;
	private QEmpresa qEmpresa;	
	private QFilial qFilial;
	private QMunicipio qMunicipio;
	private QLogradouro qLogradouro;
	private QPessoa qPessoa;	
	private QUsuario qUsuario;
	
	private JPAQuery<VasilhameEntregar> query;	
	
	private ConstructorExpression<VasilhameEntregar> projecao() {
		return QVasilhameEntregar.create(qVasilhameEntregar.id, qVasilhameEntregar.idEmpresa,
				QEmpresa.create(qEmpresa.id, qEmpresa.nome, qEmpresa.nomeFantasia, qEmpresa.paginaWeb,
						qEmpresa.extensaoLogo),
				qVasilhameEntregar.idFilial,
				QFilial.create(qFilial.codigoMunicipio,
						QMunicipio.create(qMunicipio.id, qMunicipio.nome, qMunicipio.siglaUF), qFilial.idLogradouro,
						QLogradouro.create(qLogradouro.id, qLogradouro.descricao), qFilial.endereco, qFilial.numero,
						qFilial.complemento, qFilial.bairro, qFilial.cep, qFilial.dddFone, qFilial.fone,
						qFilial.contribuinte, qFilial.cpfcnpj, qFilial.inscricaoEstadual, qFilial.inscricaoMunicipal),
				qVasilhameEntregar.idCodigo, qVasilhameEntregar.idPessoa,
				QPessoa.create(qPessoa.idEmpresa, qPessoa.idPessoa, qPessoa.digito, qPessoa.nome),
				qVasilhameEntregar.nome, qVasilhameEntregar.tipoContribuinte, qVasilhameEntregar.cpfcnpj,
				qVasilhameEntregar.observacao, qVasilhameEntregar.situacao, qVasilhameEntregar.dataHoraGravacao,
				qVasilhameEntregar.idUsuario, QUsuario.create(qUsuario.nome),
				GroupBy.list(QVasilhameEntregarItem.create(qVasilhameEntregarItem.idProduto,
						QProduto.create(qProduto.digito, qProduto.desReduzida),
						qVasilhameEntregarItem.quantidade)));
	}

	public List<VasilhameEntregar> find(VasilhameEntregar vasilhameEntregar) {
		this.query = new JPAQuery<>(this.em);
		this.qVasilhameEntregar = QVasilhameEntregar.vasilhameEntregar;
		this.qVasilhameEntregarItem = QVasilhameEntregarItem.vasilhameEntregarItem;
		this.qProduto = QProduto.produto;
		this.qEmpresa = QEmpresa.empresa;		
		this.qFilial = QFilial.filial;
		this.qMunicipio = QMunicipio.municipio;
		this.qLogradouro = QLogradouro.logradouro;
		this.qPessoa = QPessoa.pessoa;
		this.qUsuario = QUsuario.usuario;		
		
		return this.query.from(qVasilhameEntregar)
				.leftJoin(qVasilhameEntregar.itens, qVasilhameEntregarItem)
				.leftJoin(qVasilhameEntregarItem.produto, qProduto)
				.leftJoin(qVasilhameEntregar.empresa, qEmpresa)
				.leftJoin(qVasilhameEntregar.filial, qFilial)
				.leftJoin(qFilial.municipio, qMunicipio)
				.leftJoin(qFilial.logradouro, qLogradouro)
				.leftJoin(qVasilhameEntregar.pessoa, qPessoa)
				.leftJoin(qVasilhameEntregar.usuario, qUsuario)
				.where(qVasilhameEntregar.idEmpresa.eq(vasilhameEntregar.getIdEmpresa())
						.and(qVasilhameEntregar.idFilial.eq(vasilhameEntregar.getIdFilial()))
						.and(qVasilhameEntregar.idCodigo.eq(vasilhameEntregar.getIdCodigo()))
						.and(qVasilhameEntregarItem.situacao.ne(2)))
				.transform(
				         GroupBy.groupBy(qVasilhameEntregar.id)
				         .list(this.projecao()));
	}
	
	
	public byte[] generatePdf(VasilhameEntregarComprovanteRelDTO dto, Integer idEmpresa) throws Exception {		
		HashMap<String, Object> param = new HashMap<>();		
		InputStream relatoIS = null;
		InputStream relatoSubReport = null;
		InputStream logo = null;
		byte[] pdf = null;
		
		try {		
			
			List<VasilhameEntregar> vasilhamesEntregar = this.find(dto.vasilhameEntregar);
			      
			relatoIS = this.getClass().getClassLoader().getResourceAsStream("/relato/vasilhame/VasilhameEntregarComprovante.jasper");
	        JasperReport relato = (JasperReport) JRLoader.loadObject(relatoIS);        
	        
	        relatoSubReport = this.getClass().getClassLoader().getResourceAsStream("/relato/vasilhame/VasilhameEntregarComprovanteItens.jasper");
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
	        
	        pdf = JasperRunManager.runReportToPdf(relato, param, new JRBeanCollectionDataSource(vasilhamesEntregar));
        
		} finally {			
			if (relatoIS != null) relatoIS.close();
			if (relatoSubReport != null) relatoSubReport.close();
			if (logo != null) logo.close();			
		}
		
		return pdf;
	}


}