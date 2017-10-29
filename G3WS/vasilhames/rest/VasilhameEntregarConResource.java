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
import com.g3sistemas.vasilhames.business.VasilhameEntregarCadManagerBean;
import com.g3sistemas.vasilhames.business.VasilhameEntregarConManagerBean;
import com.g3sistemas.vasilhames.entity.VasilhameEntregar;

@Path("vasilhame/entregar/consulta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Programa(nome = "vasilhames.EntregarCon")
public class VasilhameEntregarConResource {

	@EJB
	private VasilhameEntregarConManagerBean managerBean;
	
	@EJB
	private VasilhameEntregarCadManagerBean vasilhameEntregarCadManagerBean;
	
	
	public VasilhameEntregarConResource() {
		
	}
	
	public VasilhameEntregarConResource(VasilhameEntregarConManagerBean managerBean) {
		this.managerBean = managerBean;
	}

	@POST
	@Path("/load")
	public Response load(LoadRequestDTO filter) {
		LoadResponseDTO<VasilhameEntregar> dto = new LoadResponseDTO<VasilhameEntregar>();
		try {
			dto.total = this.managerBean.count(filter);
			List<VasilhameEntregar> vasilhameEntrega = this.managerBean.load(filter);
			dto.registros = vasilhameEntrega;
		} catch (Exception e) {
			dto.success = false;
			dto.msg = "Falha ao buscar vasilhames a entregar";
		}
		return Response.status(dto.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(dto).type(MediaType.APPLICATION_JSON).build();

	}
	
	@POST
	@Path("/delete")
	@Permissao(nome = "exclusao")
	public Response consultaDelete(ListRegistroDTO<VasilhameEntregar> request) {
		VasilhameEntregarCadResource vasilhameEntregarCad = new VasilhameEntregarCadResource(vasilhameEntregarCadManagerBean);
		return vasilhameEntregarCad.delete(request);
	}

}
