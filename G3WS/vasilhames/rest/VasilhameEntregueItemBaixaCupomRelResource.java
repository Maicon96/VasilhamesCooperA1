package com.g3sistemas.vasilhames.rest;

import static com.g3sistemas.utils.Constantes.EMPRESA_USUARIO;
import static com.g3sistemas.utils.Constantes.LOGIN_USUARIO;
import static com.g3sistemas.utils.Constantes.NOME_USUARIO;

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
import com.g3sistemas.vasilhames.business.VasilhameEntregueItemBaixaCupomRelManagerBean;
import com.g3sistemas.vasilhames.dto.VasilhameEntregueItemBaixaCupomRelDTO;
import com.g3sistemas.vasilhames.entity.VasilhameEntregueItemBaixaCupom;

@Path("vasilhame/entregues/itens/baixas/cupons/relatorio")
@Consumes(MediaType.APPLICATION_JSON)
@Programa(nome = "vasilhames.EntregueItemBaixaCupomRel")
public class VasilhameEntregueItemBaixaCupomRelResource {
	
	private static final Logger LOG = LoggerFactory.getLogger(VasilhameEntregueItemBaixaCupomRelResource.class);
	
	@EJB
	private VasilhameEntregueItemBaixaCupomRelManagerBean managerBean;
	
	
	public VasilhameEntregueItemBaixaCupomRelResource() {
		
	}	

	public VasilhameEntregueItemBaixaCupomRelResource(VasilhameEntregueItemBaixaCupomRelManagerBean managerBean) {
		this.managerBean = managerBean;
	}	
	
	
	@POST
	@Path("/pdf")	
	@Produces({"application/pdf", MediaType.APPLICATION_JSON})
	public Response reportPDF(RegistroDTO<VasilhameEntregueItemBaixaCupomRelDTO> request, @HeaderParam(EMPRESA_USUARIO) String empresaUsuario,
			@HeaderParam(LOGIN_USUARIO) String usuario, @HeaderParam(NOME_USUARIO) String nomeUsuario) {			
		LoadResponseDTO<VasilhameEntregueItemBaixaCupom> dto = new LoadResponseDTO<VasilhameEntregueItemBaixaCupom>();
		byte[] relato = null;
		try {			
			relato = this.managerBean.generatePdf(request.registro, Integer.parseInt(empresaUsuario),
					usuario, nomeUsuario);
			
			if (relato == null) {
				dto.success=false;	
				dto.msg = "Erro ao gerar relatório de vasilhames entregues itens baixas cupons - PDF null";
			}
		} catch (Exception e) {
			dto.success = false;
			dto.msg = "Falha ao gerar relatório de vasilhames entregues itens baixas cupons: " + e.getMessage();
			LOG.error("Falha ao gerar relatório de vasilhames entregues itens baixas cupons", e);
		}
			
		if (dto.success) {
			return Response.ok(relato, "application/pdf").build();		
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(dto).type(MediaType.APPLICATION_JSON).build();		
		}
	}
	
	@POST
	@Path("/txt")		
	@Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
	public Response reportTXT(RegistroDTO<VasilhameEntregueItemBaixaCupomRelDTO> request) {			
		LoadResponseDTO<VasilhameEntregueItemBaixaCupom> dto = new LoadResponseDTO<VasilhameEntregueItemBaixaCupom>();
		byte[] relato = null;
		try {
			relato = this.managerBean.generateTxt(request.registro);

			if (relato == null) {
				dto.success = false;
				dto.msg = "Erro ao gerar relatório de vasilhames entregues itens baixas cupons - TXT null";
			}
		} catch (Exception e) {
			dto.success = false;
			dto.msg = "Falha ao gerar relatório de vasilhames entregues itens baixas cupons: " + e.getMessage();
			LOG.error("Falha ao gerar relatório de vasilhames entregues itens baixas cupons", e);
		}

		if (dto.success) {
			return Response.ok(relato).build();
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(dto).type(MediaType.APPLICATION_JSON).build();
		}
	}
	
}
