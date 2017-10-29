package com.g3sistemas.vasilhames.rest;

import static com.g3sistemas.utils.Constantes.MSG_PERMISSAO;
import static com.g3sistemas.utils.Constantes.OPCAO_PERMISSAO;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
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
import com.g3sistemas.vasilhames.business.VasilhameEntregueItemBaixaCadManagerBean;
import com.g3sistemas.vasilhames.business.VasilhameEntregueItemBaixaConManagerBean;
import com.g3sistemas.vasilhames.entity.VasilhameEntregueItemBaixa;

@Path("vasilhame/entregues/itens/baixas/consulta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Programa(nome = "vasilhames.EntregueItemBaixaCon")
public class VasilhameEntregueItemBaixaConResource {

	@EJB
	private VasilhameEntregueItemBaixaConManagerBean managerBean;
	
	@EJB
	private VasilhameEntregueItemBaixaCadManagerBean vasilhameEntregueItemCadManagerBean;
	
	
	public VasilhameEntregueItemBaixaConResource() {
		
	}
	
	public VasilhameEntregueItemBaixaConResource(VasilhameEntregueItemBaixaConManagerBean managerBean) {
		this.managerBean = managerBean;
	}

	@POST
	@Path("/load")
	public Response load(LoadRequestDTO filter) {
		LoadResponseDTO<VasilhameEntregueItemBaixa> dto = new LoadResponseDTO<VasilhameEntregueItemBaixa>();
		try {
			dto.total = this.managerBean.count(filter);
			List<VasilhameEntregueItemBaixa> vasilhameEntregueItemBaixa = this.managerBean.load(filter);			
			dto.registros = vasilhameEntregueItemBaixa;
		} catch (Exception e) {
			dto.success = false;
			dto.msg = "Falha ao buscar vasilhames entregues itens - baixas";
		}
		return Response.status(dto.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(dto).type(MediaType.APPLICATION_JSON).build();

	}	
	
	@POST
	@Path("/delete")
	@Permissao(nome = "exclusao")
	public Response consultaDelete(ListRegistroDTO<VasilhameEntregueItemBaixa> request,
			@HeaderParam(OPCAO_PERMISSAO) String opcao, @HeaderParam(MSG_PERMISSAO) String msg) {
		VasilhameEntregueItemBaixaCadResource vasilhameEntregueItemCad = new VasilhameEntregueItemBaixaCadResource(vasilhameEntregueItemCadManagerBean);
		return vasilhameEntregueItemCad.delete(request, opcao, msg);
	}

}
