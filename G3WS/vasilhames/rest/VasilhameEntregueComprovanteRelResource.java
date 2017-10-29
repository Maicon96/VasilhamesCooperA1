package com.g3sistemas.vasilhames.rest;

import static com.g3sistemas.utils.Constantes.EMPRESA_USUARIO;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.g3sistemas.auth.permissao.Programa;
import com.g3sistemas.dto.LoadResponseDTO;
import com.g3sistemas.dto.RegistroDTO;
import com.g3sistemas.vasilhames.business.VasilhameEntregueComprovanteRelManagerBean;
import com.g3sistemas.vasilhames.business.VasilhameEntregueItemCadManagerBean;
import com.g3sistemas.vasilhames.dto.VasilhameEntregueComprovanteRelDTO;
import com.g3sistemas.vasilhames.entity.VasilhameEntregue;

@Path("vasilhame/entregues/comprovante/relatorio")
@Consumes(MediaType.APPLICATION_JSON)
@Programa(nome = "vasilhames.EntregueRel")
public class VasilhameEntregueComprovanteRelResource {
	
	private static final Logger LOG = LoggerFactory.getLogger(VasilhameEntregueComprovanteRelResource.class);
	
	@EJB
	private VasilhameEntregueComprovanteRelManagerBean managerBean;
	
	@EJB
	private VasilhameEntregueItemCadManagerBean vasilhameEntregueItemCadManagerBean;
	
	
	public VasilhameEntregueComprovanteRelResource() {
		
	}	

	public VasilhameEntregueComprovanteRelResource(VasilhameEntregueComprovanteRelManagerBean managerBean) {
		this.managerBean = managerBean;
	}	
	
	
	@POST
	@Path("/pdf")	
	@Produces({"application/pdf", MediaType.APPLICATION_JSON})
	public Response reportPDF(RegistroDTO<VasilhameEntregueComprovanteRelDTO> request, @HeaderParam(EMPRESA_USUARIO) String empresaUsuario) {			
		LoadResponseDTO<VasilhameEntregue> dto = new LoadResponseDTO<VasilhameEntregue>();
		byte[] relato = null;
		try {			
			if (request.registro.vasilhameEntregue.getSituacao() != null) {
				if (request.registro.vasilhameEntregue.getSituacao() != 1) {		
					if (this.vasilhameEntregueItemCadManagerBean.existeVasilhameEntregueItem(request.registro.vasilhameEntregue)) {
						relato = this.managerBean.generatePdf(request.registro, Integer.parseInt(empresaUsuario));
						
						if (relato == null) {
							dto.success = false;
							dto.msg = "Erro ao gerar relatório do comprovante dos vasilhames entregues - PDF null";
						}
					} else {
						dto.success = false;
						dto.msg = "Não existem itens cadastrados para esta estrega de vasilhames";
					}
				} else {
					dto.success = false;
					dto.msg = "Não foi possível gerar o comprovante, situação do vasilhame não pode estar aberto";
				}	
			}
						
		} catch (Exception e) {
			dto.success = false;
			dto.msg = "Falha ao gerar relatório do comprovante dos vasilhames entregues: " + e.getMessage();
			LOG.error("Falha ao gerar relatório do comprovante dos vasilhames entregues", e);
		}
			
		if (dto.success) {
			return Response.ok(relato, "application/pdf").build();		
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(dto).type(MediaType.APPLICATION_JSON).build();		
		}
	}	
	
}
