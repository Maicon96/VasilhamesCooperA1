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
import com.g3sistemas.vasilhames.business.VasilhameEntregarComprovanteRelManagerBean;
import com.g3sistemas.vasilhames.business.VasilhameEntregarItemCadManagerBean;
import com.g3sistemas.vasilhames.dto.VasilhameEntregarComprovanteRelDTO;
import com.g3sistemas.vasilhames.entity.VasilhameEntregar;

@Path("vasilhame/entregar/comprovante/relatorio")
@Consumes(MediaType.APPLICATION_JSON)
@Programa(nome = "vasilhames.EntregarRel")
public class VasilhameEntregarComprovanteRelResource {
	
	private static final Logger LOG = LoggerFactory.getLogger(VasilhameEntregarComprovanteRelResource.class);
	
	@EJB
	private VasilhameEntregarComprovanteRelManagerBean managerBean;
	
	@EJB
	private VasilhameEntregarItemCadManagerBean vasilhameEntregarItemCadManagerBean;
	
	
	public VasilhameEntregarComprovanteRelResource() {
		
	}	

	public VasilhameEntregarComprovanteRelResource(VasilhameEntregarComprovanteRelManagerBean managerBean) {
		this.managerBean = managerBean;
	}	
	
	
	@POST
	@Path("/pdf")	
	@Produces({"application/pdf", MediaType.APPLICATION_JSON})
	public Response reportPDF(RegistroDTO<VasilhameEntregarComprovanteRelDTO> request, @HeaderParam(EMPRESA_USUARIO) String empresaUsuario) {			
		LoadResponseDTO<VasilhameEntregar> dto = new LoadResponseDTO<VasilhameEntregar>();
		byte[] relato = null;
		try {
			if (request.registro.vasilhameEntregar.getSituacao() != null) {
				if (request.registro.vasilhameEntregar.getSituacao() != 1) {		
					if (this.vasilhameEntregarItemCadManagerBean.existeVasilhameEntregueItem(request.registro.vasilhameEntregar)) {
						relato = this.managerBean.generatePdf(request.registro, Integer.parseInt(empresaUsuario));
						
						if (relato == null) {
							dto.success = false;
							dto.msg = "Erro ao gerar relatório do comprovante dos vasilhames a entregar - PDF null";
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
			dto.msg = "Falha ao gerar relatório do comprovante dos vasilhames a entregar: " + e.getMessage();
			LOG.error("Falha ao gerar relatório do comprovante dos vasilhames a entregar", e);
		}
			
		if (dto.success) {
			return Response.ok(relato, "application/pdf").build();		
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(dto).type(MediaType.APPLICATION_JSON).build();		
		}
	}	
	
}
