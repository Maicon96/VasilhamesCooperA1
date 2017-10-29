package com.g3sistemas.vasilhames.rest;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.g3sistemas.auth.permissao.Permissao;
import com.g3sistemas.auth.permissao.Programa;
import com.g3sistemas.dto.CampoErroDTO;
import com.g3sistemas.dto.ListRegistroDTO;
import com.g3sistemas.dto.RegistroDTO;
import com.g3sistemas.dto.ResponseDTO;
import com.g3sistemas.geral.business.EmpresaCadManagerBean;
import com.g3sistemas.geral.business.FilialCadManagerBean;
import com.g3sistemas.geral.entity.Empresa;
import com.g3sistemas.geral.entity.Filial;
import com.g3sistemas.geral.rest.FilialCadResource;
import com.g3sistemas.geral.rest.empresa.EmpresaCadResource;
import com.g3sistemas.vasilhames.business.VasilhameEntregueItemBaixaCadManagerBean;
import com.g3sistemas.vasilhames.business.VasilhameEntregueItemBaixaNotaCadManagerBean;
import com.g3sistemas.vasilhames.entity.VasilhameEntregueItemBaixa;
import com.g3sistemas.vasilhames.entity.VasilhameEntregueItemBaixaNota;

@Path("vasilhame/entregues/itens/baixas/notas/cadastro")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Programa(nome = "vasilhames.EntregueItemBaixaNotaCad")
public class VasilhameEntregueItemBaixaNotaCadResource {
	
	private static final Logger LOG = LoggerFactory.getLogger(VasilhameEntregueItemBaixaNotaCadResource.class);
	
	@EJB
	private VasilhameEntregueItemBaixaNotaCadManagerBean managerBean;	
	
	@EJB
	private EmpresaCadManagerBean empresaCadManagerBean;
	
	@EJB
	private FilialCadManagerBean filialCadManagerBean;
	
	@EJB
	private VasilhameEntregueItemBaixaCadManagerBean vasilhameEntregueItemBaixaCadManagerBean;	
	
	
	public VasilhameEntregueItemBaixaNotaCadResource() {
		
	}
	
	public VasilhameEntregueItemBaixaNotaCadResource(VasilhameEntregueItemBaixaNotaCadManagerBean managerBean) {
		this.managerBean = managerBean;
	}
	
	
	@POST
	@Path("/exists")
	public Response exists(RegistroDTO<VasilhameEntregueItemBaixaNota> request) {
		ResponseDTO<VasilhameEntregueItemBaixaNota> dto = new ResponseDTO<>();
		VasilhameEntregueItemBaixaNota vasEntIteBaixaNota = this.managerBean.find(request.registro);
		if (vasEntIteBaixaNota == null) {
			dto.success = false;
			dto.errors.add(new CampoErroDTO("vasilhameEntregueItemBaixaNota", "Vasilhame entregue item baixa - nota não cadastrada"));
		} else {
			dto.registro = vasEntIteBaixaNota;
		}
		return Response.status(dto.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR).entity(dto)
				.type(MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/exists/empresa/baixa")
	public Response existsEmpresa(RegistroDTO<Empresa> request) {
		EmpresaCadResource empresaCadResource = new EmpresaCadResource(empresaCadManagerBean);
		return empresaCadResource.existsBasic(request);
	}
	
	@POST
	@Path("/exists/filial/baixa")
	public Response existsFilial(RegistroDTO<Filial> request) {
		FilialCadResource filialCadResource = new FilialCadResource(filialCadManagerBean);
		return filialCadResource.existsBasic(request);
	}
	
	@POST
	@Path("/exists/baixa")
	public Response existsVasilhameEntregueItem(RegistroDTO<VasilhameEntregueItemBaixa> request) {
		VasilhameEntregueItemBaixaCadResource vasEntIteBaixaCadResource = new VasilhameEntregueItemBaixaCadResource(vasilhameEntregueItemBaixaCadManagerBean);
		return vasEntIteBaixaCadResource.existsBasic(request);
	}	
			
	@POST
	@Path("/exists/empresa/nota")
	public Response existsEmpresaNota(RegistroDTO<Empresa> request) {
		EmpresaCadResource empresaCadResource = new EmpresaCadResource(empresaCadManagerBean);
		return empresaCadResource.existsBasic(request);
	}
	
	@POST
	@Path("/exists/filial/nota")
	public Response existsFilialNota(RegistroDTO<Filial> request) {
		FilialCadResource filialCadResource = new FilialCadResource(filialCadManagerBean);
		return filialCadResource.existsBasic(request);
	}
	
	@POST
	@Path("/save")
	@Permissao(nome = "inclusao")
	public Response save(RegistroDTO<VasilhameEntregueItemBaixaNota> dto) {
		ResponseDTO<VasilhameEntregueItemBaixaNota> response = new ResponseDTO<>();

		try {
			List<CampoErroDTO> errors = this.managerBean.salvar(dto);
			if (errors.isEmpty()) {
				response.registro = dto.registro;
			} else {
				response.success = false;
				response.errors = errors;
			}

		} catch (Exception e) {
			response.success = false;
			LOG.error("Falha ao salvar Vasilhame Entregue Item Baixa - Nota", e);
			response.msg = "Falha ao salvar Vasilhame Entregue Item Baixa - Nota";
		}

		return Response.status(response.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(response).type(MediaType.APPLICATION_JSON).build();
	}
	

	@POST
	@Path("/update")
	@Permissao(nome = "alteracao")
	public Response update(RegistroDTO<VasilhameEntregueItemBaixaNota> dto) {
		ResponseDTO<VasilhameEntregueItemBaixaNota> response = new ResponseDTO<>();

		try {
			List<CampoErroDTO> errors = this.managerBean.atualizar(dto);
			if (errors.isEmpty()) {
				response.registro = dto.registro;
			} else {
				response.success = false;
				response.errors = errors;
			}
		} catch (Exception e) {
			response.success = false;
			LOG.error("Falha ao atualizar Vasilhame Entregue Item Baixa - Nota", e);
			response.msg = "Falha ao atualizar Vasilhame Entregue Item Baixa - Nota";
		}

		return Response.status(response.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(response).type(MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/delete")
	@Permissao(nome = "exclusao")
	public Response delete(ListRegistroDTO<VasilhameEntregueItemBaixaNota> request) {
		ResponseDTO<VasilhameEntregueItemBaixaNota> response = new ResponseDTO<>();
		try {			
			List<CampoErroDTO> errors = this.managerBean.deletar(request.registros);			
			if (errors.isEmpty()) {
				response.success = true;
			} else {
				response.success = false;
				response.errors = errors;
			}		
		} catch (Exception e) {
			LOG.error("Falha ao deletar Vasilhame Entregue Item Baixa - Nota", e);
			response.success = false;
			response.msg = "Falha ao excluir Vasilhames Entregues Itens Baixas - Notas";
		}	
		
		return Response.status(response.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(response).type(MediaType.APPLICATION_JSON).build();
	}

}
