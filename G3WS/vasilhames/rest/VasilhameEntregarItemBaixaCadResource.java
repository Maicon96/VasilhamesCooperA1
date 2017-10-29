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
import com.g3sistemas.dto.ListResponseDTO;
import com.g3sistemas.dto.RegistroDTO;
import com.g3sistemas.dto.ResponseDTO;
import com.g3sistemas.estoques.business.produtos.ProdutoCadManagerBean;
import com.g3sistemas.geral.business.EmpresaCadManagerBean;
import com.g3sistemas.geral.business.FilialCadManagerBean;
import com.g3sistemas.geral.entity.Empresa;
import com.g3sistemas.geral.entity.Filial;
import com.g3sistemas.geral.rest.FilialCadResource;
import com.g3sistemas.geral.rest.empresa.EmpresaCadResource;
import com.g3sistemas.sistema.business.UsuarioManagerBean;
import com.g3sistemas.sistema.entity.Usuario;
import com.g3sistemas.sistema.rest.UsuarioResource;
import com.g3sistemas.vasilhames.business.VasilhameEntregarItemBaixaCadManagerBean;
import com.g3sistemas.vasilhames.business.VasilhameEntregarItemCadManagerBean;
import com.g3sistemas.vasilhames.entity.VasilhameEntregarItem;
import com.g3sistemas.vasilhames.entity.VasilhameEntregarItemBaixa;

@Path("vasilhame/entregar/itens/baixas/cadastro")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Programa(nome = "vasilhames.EntregarItemBaixaCad")
public class VasilhameEntregarItemBaixaCadResource {
	
	private static final Logger LOG = LoggerFactory.getLogger(VasilhameEntregarItemBaixaCadResource.class);
	
	@EJB
	private VasilhameEntregarItemBaixaCadManagerBean managerBean;	
	
	@EJB
	private EmpresaCadManagerBean empresaCadManagerBean;
	
	@EJB
	private FilialCadManagerBean filialCadManagerBean;
	
	@EJB
	private VasilhameEntregarItemCadManagerBean vasilhameEntregarItemCadManagerBean;
	
	@EJB
	private ProdutoCadManagerBean produtoCadManagerBean;
	
	@EJB
	private UsuarioManagerBean usuarioManagerBean;
	
	
	public VasilhameEntregarItemBaixaCadResource() {
		
	}
	
	public VasilhameEntregarItemBaixaCadResource(VasilhameEntregarItemBaixaCadManagerBean managerBean) {
		this.managerBean = managerBean;
	}
	
	
	@POST
	@Path("/exists")
	public Response exists(RegistroDTO<VasilhameEntregarItemBaixa> request) {
		ResponseDTO<VasilhameEntregarItemBaixa> dto = new ResponseDTO<>();
		VasilhameEntregarItemBaixa vasilhameEntregarItemBaixa = this.managerBean.find(request.registro);
		if (vasilhameEntregarItemBaixa == null) {
			dto.success = false;
			dto.errors.add(new CampoErroDTO("vasilhameEntregarItemBaixa", "Vasilhame a entregar item - baixa não cadastrada"));
		} else {
			vasilhameEntregarItemBaixa.setQuantidadeBaixada(this.managerBean.findSumQuantidade(request.registro));
			dto.registro = vasilhameEntregarItemBaixa;
		}
		return Response.status(dto.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(dto).type(MediaType.APPLICATION_JSON).build();
	}	
	
	@POST
	@Path("/exists/basic")
	public Response existsBasic(RegistroDTO<VasilhameEntregarItemBaixa> request) {
		ResponseDTO<VasilhameEntregarItem> dto = new ResponseDTO<>();		
		if (!this.managerBean.existeVasilhameEntregarItemBaixa(request.registro)) {
			dto.success = false;
			dto.errors.add(new CampoErroDTO("vasilhameEntregarItemBaixa", "Vasilhame a entregar item - baixa não cadastrada"));
		}
		return Response.status(dto.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(dto).type(MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("/exists/empresa")
	public Response existsEmpresa(RegistroDTO<Empresa> request) {
		EmpresaCadResource empresaCadResource = new EmpresaCadResource(empresaCadManagerBean);
		return empresaCadResource.existsBasic(request);
	}
	
	@POST
	@Path("/exists/filial")
	public Response existsFilial(RegistroDTO<Filial> request) {
		FilialCadResource filialCadResource = new FilialCadResource(filialCadManagerBean);
		return filialCadResource.existsBasic(request);
	}
	
	@POST
	@Path("/exists/item")
	public Response existsVasilhameEntregarItem(RegistroDTO<VasilhameEntregarItem> request) {
		VasilhameEntregarItemCadResource vasilhameEntregarItemCadResource = new VasilhameEntregarItemCadResource(vasilhameEntregarItemCadManagerBean);
		return vasilhameEntregarItemCadResource.existsBasic(request);
	}	
			
	@POST
	@Path("/exists/usuario")
	public Response existsUsuario(RegistroDTO<Usuario> request) {
		UsuarioResource usuarioResource = new UsuarioResource(usuarioManagerBean);
		return usuarioResource.exists(request);
	}
	
	@POST
	@Path("/save")
	@Permissao(nome = "inclusao")
	public Response save(RegistroDTO<VasilhameEntregarItemBaixa> dto) {
		ResponseDTO<VasilhameEntregarItemBaixa> response = new ResponseDTO<>();
		
		try {			
			List<CampoErroDTO> errors = this.managerBean.salvar(dto);
			if (errors.isEmpty()) {				
				if (dto.registro.getIdEmpresa() != null && dto.registro.getIdFilial() != null && dto.registro.getIdVasilhameEntregarItem() != null) {
					dto.registro.setQuantidadeBaixada(this.managerBean.findSumQuantidade(dto.registro));
				}							
				response.registro = dto.registro;
			} else {
				response.success = false;
				response.errors = errors;
			}

		} catch (Exception e) {
			response.success = false;
			LOG.error("Falha ao salvar Vasilhame a Entregar Item - Baixa", e);
			response.msg = "Falha ao salvar Vasilhame a Entregar Item - Baixa";
		}

		return Response.status(response.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(response).type(MediaType.APPLICATION_JSON).build();
	}
	

	@POST
	@Path("/update")
	@Permissao(nome = "alteracao")
	public Response update(RegistroDTO<VasilhameEntregarItemBaixa> dto) {
		ResponseDTO<VasilhameEntregarItemBaixa> response = new ResponseDTO<>();

		try {
			List<CampoErroDTO> errors = this.managerBean.atualizar(dto);
			if (errors.isEmpty()) {
				dto.registro.setQuantidadeBaixada(this.managerBean.findSumQuantidade(dto.registro));
				response.registro = dto.registro;
			} else {
				response.success = false;
				response.errors = errors;
			}
		} catch (Exception e) {
			response.success = false;
			LOG.error("Falha ao atualizar Vasilhame a Entregar Item - Baixa", e);
			response.msg = "Falha ao atualizar Vasilhame a Entregar Item - Baixa";
		}

		return Response.status(response.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(response).type(MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/delete")
	@Permissao(nome = "exclusao")
	public Response delete(ListRegistroDTO<VasilhameEntregarItemBaixa> request) {
		ListResponseDTO<VasilhameEntregarItemBaixa> response = new ListResponseDTO<>();
		try {			
			List<CampoErroDTO> errors = this.managerBean.deletar(request.registros);			
			if (errors.isEmpty()) {
				response.registros = request.registros;
				response.success = true;
			} else {
				response.success = false;
				response.errors = errors;
			}		
		} catch (Exception e) {
			LOG.error("Falha ao excluir Vasilhames a Entregar Item - Baixas", e);
			response.success = false;
			response.msg = "Falha ao excluir Vasilhames a Entregar Item - Baixas";
		}	
		
		return Response.status(response.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(response).type(MediaType.APPLICATION_JSON).build();
	}

}
