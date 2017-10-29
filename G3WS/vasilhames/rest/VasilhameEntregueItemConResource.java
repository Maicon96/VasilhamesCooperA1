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
import com.g3sistemas.vasilhames.business.VasilhameEntregueItemCadManagerBean;
import com.g3sistemas.vasilhames.business.VasilhameEntregueItemConManagerBean;
import com.g3sistemas.vasilhames.entity.VasilhameEntregueItem;

@Path("vasilhame/entregues/itens/consulta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Programa(nome = "vasilhames.EntregueItemCon")
public class VasilhameEntregueItemConResource {

	@EJB
	private VasilhameEntregueItemConManagerBean managerBean;
	
	@EJB
	private VasilhameEntregueItemCadManagerBean vasilhameEntregueItemCadManagerBean;
	
	
	public VasilhameEntregueItemConResource() {
		
	}
	
	public VasilhameEntregueItemConResource(VasilhameEntregueItemConManagerBean managerBean) {
		this.managerBean = managerBean;
	}

	@POST
	@Path("/load")
	public Response load(LoadRequestDTO filter) {
		LoadResponseDTO<VasilhameEntregueItem> dto = new LoadResponseDTO<VasilhameEntregueItem>();
		try {
			dto.total = this.managerBean.count(filter);
			List<VasilhameEntregueItem> vasilhameEntregueItem = this.managerBean.load(filter);
			dto.registros = vasilhameEntregueItem;
		} catch (Exception e) {
			dto.success = false;
			dto.msg = "Falha ao buscar vasilhames entregues - itens";
		}
		return Response.status(dto.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(dto).type(MediaType.APPLICATION_JSON).build();

	}
	
	@POST
	@Path("/delete")
	@Permissao(nome = "exclusao")
	public Response consultaDelete(ListRegistroDTO<VasilhameEntregueItem> request) {
		VasilhameEntregueItemCadResource vasilhameEntregueItemCad = new VasilhameEntregueItemCadResource(vasilhameEntregueItemCadManagerBean);
		return vasilhameEntregueItemCad.delete(request);
	}

}
