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
import com.g3sistemas.vasilhames.business.VasilhameEntregueCadManagerBean;
import com.g3sistemas.vasilhames.business.VasilhameEntregueItemCadManagerBean;
import com.g3sistemas.vasilhames.entity.VasilhameEntregue;
import com.g3sistemas.vasilhames.entity.VasilhameEntregueItem;

@Path("vasilhame/entregues/itens/cadastro")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Programa(nome = "vasilhames.EntregueItemCad")
public class VasilhameEntregueItemCadResource {
	
	private static final Logger LOG = LoggerFactory.getLogger(VasilhameEntregueItemCadResource.class);
	
	@EJB
	private VasilhameEntregueItemCadManagerBean managerBean;	
	
	@EJB
	private EmpresaCadManagerBean empresaCadManagerBean;
	
	@EJB
	private FilialCadManagerBean filialCadManagerBean;
	
	@EJB
	private VasilhameEntregueCadManagerBean vasilhameEntregueCadManagerBean;
	
	@EJB
	private ProdutoCadManagerBean produtoCadManagerBean;
	
	@EJB
	private UsuarioManagerBean usuarioManagerBean;
	
	
	public VasilhameEntregueItemCadResource() {
		
	}
	
	public VasilhameEntregueItemCadResource(VasilhameEntregueItemCadManagerBean managerBean) {
		this.managerBean = managerBean;
	}
	
	
	@POST
	@Path("/exists")
	public Response exists(RegistroDTO<VasilhameEntregueItem> request) {
		ResponseDTO<VasilhameEntregueItem> dto = new ResponseDTO<>();
		VasilhameEntregueItem vasilhameEntregueItem = this.managerBean.find(request.registro);
		if (vasilhameEntregueItem == null) {
			dto.success = false;
			dto.errors.add(new CampoErroDTO("vasilhameEntregueItem", "Vasilhame entregue - item não cadastrado"));
		} else {
			dto.registro = vasilhameEntregueItem;
		}
		return Response.status(dto.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(dto).type(MediaType.APPLICATION_JSON).build();

	}		
	
	
	@POST
	@Path("/exists/basic")
	public Response existsBasic(RegistroDTO<VasilhameEntregueItem> request) {
		ResponseDTO<VasilhameEntregueItem> dto = new ResponseDTO<>();		
		if (!this.managerBean.existeVasilhameEntregueItem(request.registro)) {
			dto.success = false;
			dto.errors.add(new CampoErroDTO("vasilhameEntregueItem", "Vasilhame entregue - item não cadastrado"));
		}
		return Response.status(dto.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(dto).type(MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("/exists/item/produto")
	public Response existsItemProduto(RegistroDTO<VasilhameEntregueItem> request) {
		ResponseDTO<VasilhameEntregueItem> dto = new ResponseDTO<>();
		VasilhameEntregueItem vasilhameEntregueItem = this.managerBean.findProduto(request.registro);
		if (vasilhameEntregueItem == null) {
			dto.success = false;
			dto.errors.add(new CampoErroDTO("vasilhameEntregueItem", "Vasilhame entregue - item não cadastrado"));
		} else {
			dto.registro = vasilhameEntregueItem;
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
	@Path("/exists/vasilhame")
	public Response existsVasilhameEntregue(RegistroDTO<VasilhameEntregue> request) {
		VasilhameEntregueCadResource vasilhameCadResource = new VasilhameEntregueCadResource(vasilhameEntregueCadManagerBean);
		return vasilhameCadResource.exists(request);
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
	public Response save(RegistroDTO<VasilhameEntregueItem> dto) {
		ResponseDTO<VasilhameEntregueItem> response = new ResponseDTO<>();
		
		try {			
			List<CampoErroDTO> errors = this.managerBean.salvar(dto);
			if (errors.isEmpty()) {
				dto.registro.setQuantidadeBaixar(dto.registro.getQuantidade());
				response.registro = dto.registro;
			} else {
				response.success = false;
				response.errors = errors;
			}

		} catch (Exception e) {
			response.success = false;
			LOG.error("Falha ao salvar Vasilhame Entregue - Item", e);
			response.msg = "Falha ao salvar Vasilhame Entregue - Item";
		}

		return Response.status(response.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(response).type(MediaType.APPLICATION_JSON).build();
	}
	

	@POST
	@Path("/update")
	@Permissao(nome = "alteracao")
	public Response update(RegistroDTO<VasilhameEntregueItem> dto) {
		ResponseDTO<VasilhameEntregueItem> response = new ResponseDTO<>();

		try {
			List<CampoErroDTO> errors = this.managerBean.atualizar(dto);
			if (errors.isEmpty()) {
				dto.registro.setQuantidadeBaixar(this.managerBean.findQuantidadeBaixar(dto.registro));
				response.registro = dto.registro;
			} else {
				response.success = false;
				response.errors = errors;
			}
		} catch (Exception e) {
			response.success = false;
			LOG.error("Falha ao atualizar Vasilhame Entregue - Item", e);
			response.msg = "Falha ao atualizar Vasilhame Entregue - Item";
		}

		return Response.status(response.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(response).type(MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("/delete")
	@Permissao(nome = "exclusao")
	public Response delete(ListRegistroDTO<VasilhameEntregueItem> request) {
		ResponseDTO<VasilhameEntregueItem> response = new ResponseDTO<>();
		try {			
			List<CampoErroDTO> errors = this.managerBean.deletar(request.registros);			
			if (errors.isEmpty()) {
				response.success = true;
			} else {
				response.success = false;
				response.errors = errors;
			}		
		} catch (Exception e) {
			LOG.error("Falha ao excluir Vasilhames Entregues - Itens", e);
			response.success = false;
			response.msg = "Falha ao excluir Vasilhames Entregues - Itens";
		}	
		
		return Response.status(response.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(response).type(MediaType.APPLICATION_JSON).build();
	}	

}
