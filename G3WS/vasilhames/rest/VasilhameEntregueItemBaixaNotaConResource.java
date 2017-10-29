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
import com.g3sistemas.vasilhames.business.VasilhameEntregueItemBaixaNotaCadManagerBean;
import com.g3sistemas.vasilhames.business.VasilhameEntregueItemBaixaNotaConManagerBean;
import com.g3sistemas.vasilhames.entity.VasilhameEntregueItemBaixaNota;

@Path("vasilhame/entregues/itens/baixas/notas/consulta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Programa(nome = "vasilhames.EntregueItemBaixaNotaCon")
public class VasilhameEntregueItemBaixaNotaConResource {

	@EJB
	private VasilhameEntregueItemBaixaNotaConManagerBean managerBean;
	
	@EJB
	private VasilhameEntregueItemBaixaNotaCadManagerBean vasilhameEntregueItemCadManagerBean;
	
	
	public VasilhameEntregueItemBaixaNotaConResource() {
		
	}
	
	public VasilhameEntregueItemBaixaNotaConResource(VasilhameEntregueItemBaixaNotaConManagerBean managerBean) {
		this.managerBean = managerBean;
	}

	@POST
	@Path("/load")
	public Response load(LoadRequestDTO filter) {
		LoadResponseDTO<VasilhameEntregueItemBaixaNota> dto = new LoadResponseDTO<VasilhameEntregueItemBaixaNota>();
		try {
			dto.total = this.managerBean.count(filter);
			List<VasilhameEntregueItemBaixaNota> vasEntIteBaixaNota = this.managerBean.load(filter);
			dto.registros = vasEntIteBaixaNota;
		} catch (Exception e) {
			dto.success = false;
			dto.msg = "Falha ao buscar vasilhames entregues itens baixas - notas";
		}
		return Response.status(dto.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(dto).type(MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("/delete")
	@Permissao(nome = "exclusao")
	public Response consultaDelete(ListRegistroDTO<VasilhameEntregueItemBaixaNota> request) {
		VasilhameEntregueItemBaixaNotaCadResource vasilhameEntregueItemCad = new VasilhameEntregueItemBaixaNotaCadResource(vasilhameEntregueItemCadManagerBean);
		return vasilhameEntregueItemCad.delete(request);
	}

}
