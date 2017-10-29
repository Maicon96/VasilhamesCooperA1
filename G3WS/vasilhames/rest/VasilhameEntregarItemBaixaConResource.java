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
import com.g3sistemas.vasilhames.business.VasilhameEntregarItemBaixaCadManagerBean;
import com.g3sistemas.vasilhames.business.VasilhameEntregarItemBaixaConManagerBean;
import com.g3sistemas.vasilhames.entity.VasilhameEntregarItemBaixa;

@Path("vasilhame/entregar/itens/baixas/consulta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Programa(nome = "vasilhames.EntregarItemBaixaCon")
public class VasilhameEntregarItemBaixaConResource {

	@EJB
	private VasilhameEntregarItemBaixaConManagerBean managerBean;
	
	@EJB
	private VasilhameEntregarItemBaixaCadManagerBean vasilhameEntregarItemCadManagerBean;
	
	
	public VasilhameEntregarItemBaixaConResource() {
		
	}
	
	public VasilhameEntregarItemBaixaConResource(VasilhameEntregarItemBaixaConManagerBean managerBean) {
		this.managerBean = managerBean;
	}

	@POST
	@Path("/load")
	public Response load(LoadRequestDTO filter) {
		LoadResponseDTO<VasilhameEntregarItemBaixa> dto = new LoadResponseDTO<VasilhameEntregarItemBaixa>();
		try {
			dto.total = this.managerBean.count(filter);
			List<VasilhameEntregarItemBaixa> vasilhameEntregarItemBaixa = this.managerBean.load(filter);
			dto.registros = vasilhameEntregarItemBaixa;
		} catch (Exception e) {
			dto.success = false;
			dto.msg = "Falha ao buscar vasilhames a entregar itens - baixas";
		}
		return Response.status(dto.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(dto).type(MediaType.APPLICATION_JSON).build();

	}
	
	@POST
	@Path("/delete")
	@Permissao(nome = "exclusao")
	public Response consultaDelete(ListRegistroDTO<VasilhameEntregarItemBaixa> request) {
		VasilhameEntregarItemBaixaCadResource vasilhameEntregarItemCad = new VasilhameEntregarItemBaixaCadResource(vasilhameEntregarItemCadManagerBean);
		return vasilhameEntregarItemCad.delete(request);
	}

}
