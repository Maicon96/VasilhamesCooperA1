package com.g3sistemas.vasilhames.rest;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.g3sistemas.auth.permissao.Permissao;
import com.g3sistemas.auth.permissao.Programa;
import com.g3sistemas.dto.ListRegistroDTO;
import com.g3sistemas.dto.LoadRequestDTO;
import com.g3sistemas.dto.LoadResponseDTO;
import com.g3sistemas.vasilhames.business.VasilhameEntregueItemBaixaCupomCadManagerBean;
import com.g3sistemas.vasilhames.business.VasilhameEntregueItemBaixaCupomConManagerBean;
import com.g3sistemas.vasilhames.entity.VasilhameEntregueItemBaixaCupom;

@Path("vasilhame/entregues/itens/baixas/cupons/consulta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Programa(nome = "vasilhames.EntregueItemBaixaCupomCon")
public class VasilhameEntregueItemBaixaCupomConResource {

	@EJB
	private VasilhameEntregueItemBaixaCupomConManagerBean managerBean;
	
	@EJB
	private VasilhameEntregueItemBaixaCupomCadManagerBean vasilhameEntregueItemCadManagerBean;
	
	
	public VasilhameEntregueItemBaixaCupomConResource() {
		
	}
	
	public VasilhameEntregueItemBaixaCupomConResource(VasilhameEntregueItemBaixaCupomConManagerBean managerBean) {
		this.managerBean = managerBean;
	}

	@POST
	@Path("/load")
	public Response load(LoadRequestDTO filter) {
		LoadResponseDTO<VasilhameEntregueItemBaixaCupom> dto = new LoadResponseDTO<VasilhameEntregueItemBaixaCupom>();
		try {
			dto.total = this.managerBean.count(filter);
			List<VasilhameEntregueItemBaixaCupom> vasEntIteBaixaCupom = this.managerBean.load(filter);
			dto.registros = vasEntIteBaixaCupom;
		} catch (Exception e) {
			dto.success = false;
			dto.msg = "Falha ao buscar vasilhames entregues itens baixas - cupons";
		}
		return Response.status(dto.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(dto).type(MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("/delete")
	@Permissao(nome = "exclusao")
	public Response consultaDelete(ListRegistroDTO<VasilhameEntregueItemBaixaCupom> request) {
		VasilhameEntregueItemBaixaCupomCadResource cupomCad = new VasilhameEntregueItemBaixaCupomCadResource(vasilhameEntregueItemCadManagerBean);
		return cupomCad.delete(request);
	}

}
