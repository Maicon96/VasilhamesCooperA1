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
import com.g3sistemas.vasilhames.business.VasilhameEntregueCadManagerBean;
import com.g3sistemas.vasilhames.business.VasilhameEntregueConManagerBean;
import com.g3sistemas.vasilhames.entity.VasilhameEntregue;

@Path("vasilhame/entregues/consulta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Programa(nome = "vasilhames.EntregueCon")
public class VasilhameEntregueConResource {

	@EJB
	private VasilhameEntregueConManagerBean managerBean;
	
	@EJB
	private VasilhameEntregueCadManagerBean vasilhameEntregueCadManagerBean;
	
	
	public VasilhameEntregueConResource() {
		
	}
	
	public VasilhameEntregueConResource(VasilhameEntregueConManagerBean managerBean) {
		this.managerBean = managerBean;
	}

	@POST
	@Path("/load")
	public Response load(LoadRequestDTO filter) {
		LoadResponseDTO<VasilhameEntregue> dto = new LoadResponseDTO<VasilhameEntregue>();
		try {
			dto.total = this.managerBean.count(filter);
			List<VasilhameEntregue> vasilhameEntregue = this.managerBean.load(filter);
			dto.registros = vasilhameEntregue;
		} catch (Exception e) {
			dto.success = false;
			dto.msg = "Falha ao buscar vasilhames entregues";
		}
		return Response.status(dto.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(dto).type(MediaType.APPLICATION_JSON).build();

	}
	
	@POST
	@Path("/delete")
	@Permissao(nome = "exclusao")
	public Response consultaDelete(ListRegistroDTO<VasilhameEntregue> request) {
		VasilhameEntregueCadResource vasilhameEntregueCad = new VasilhameEntregueCadResource(vasilhameEntregueCadManagerBean);
		return vasilhameEntregueCad.delete(request);
	}

}
