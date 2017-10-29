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
import com.g3sistemas.estoques.entity.Produto;
import com.g3sistemas.estoques.rest.produtos.ProdutoCadResource;
import com.g3sistemas.geral.business.EmpresaCadManagerBean;
import com.g3sistemas.geral.business.FilialCadManagerBean;
import com.g3sistemas.geral.entity.Empresa;
import com.g3sistemas.geral.entity.Filial;
import com.g3sistemas.geral.rest.FilialCadResource;
import com.g3sistemas.geral.rest.empresa.EmpresaCadResource;
import com.g3sistemas.sistema.business.UsuarioManagerBean;
import com.g3sistemas.sistema.entity.Usuario;
import com.g3sistemas.sistema.rest.UsuarioResource;
import com.g3sistemas.vasilhames.business.VasilhameEntregarCadManagerBean;
import com.g3sistemas.vasilhames.business.VasilhameEntregarItemCadManagerBean;
import com.g3sistemas.vasilhames.entity.VasilhameEntregar;
import com.g3sistemas.vasilhames.entity.VasilhameEntregarItem;

@Path("vasilhame/entregar/itens/cadastro")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Programa(nome = "vasilhames.EntregarItemCad")
public class VasilhameEntregarItemCadResource {
	
	private static final Logger LOG = LoggerFactory.getLogger(VasilhameEntregarItemCadResource.class);
	
	@EJB
	private VasilhameEntregarItemCadManagerBean managerBean;	
	
	@EJB
	private EmpresaCadManagerBean empresaCadManagerBean;
	
	@EJB
	private FilialCadManagerBean filialCadManagerBean;
	
	@EJB
	private VasilhameEntregarCadManagerBean vasilhameEntregarCadManagerBean;
	
	@EJB
	private ProdutoCadManagerBean produtoCadManagerBean;
	
	@EJB
	private UsuarioManagerBean usuarioManagerBean;
	
	
	public VasilhameEntregarItemCadResource() {
		
	}
	
	public VasilhameEntregarItemCadResource(VasilhameEntregarItemCadManagerBean managerBean) {
		this.managerBean = managerBean;
	}
	
	
	@POST
	@Path("/exists")
	public Response exists(RegistroDTO<VasilhameEntregarItem> request) {
		ResponseDTO<VasilhameEntregarItem> dto = new ResponseDTO<>();
		VasilhameEntregarItem vasilhameEntregarItem = this.managerBean.find(request.registro);
		if (vasilhameEntregarItem == null) {
			dto.success = false;
			dto.errors.add(new CampoErroDTO("vasilhameEntregarItem", "Vasilhame entregar - item não cadastrado"));
		} else {
			dto.registro = vasilhameEntregarItem;
		}
		return Response.status(dto.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(dto).type(MediaType.APPLICATION_JSON).build();

	}	
	
	@POST
	@Path("/exists/basic")
	public Response existsBasic(RegistroDTO<VasilhameEntregarItem> request) {
		ResponseDTO<VasilhameEntregarItem> dto = new ResponseDTO<>();		
		if (!this.managerBean.existeVasilhameEntregarItem(request.registro)) {
			dto.success = false;
			dto.errors.add(new CampoErroDTO("vasilhameEntregarItem", "Vasilhame entregar - item não cadastrado"));
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
	public Response existsVasilhameEntregar(RegistroDTO<VasilhameEntregar> request) {
		VasilhameEntregarCadResource vasilhameCadResource = new VasilhameEntregarCadResource(vasilhameEntregarCadManagerBean);
		return vasilhameCadResource.exists(request);
	}
	
	@POST
	@Path("/exists/produto")
	public Response existsPessoa(RegistroDTO<Produto> request) {
		ProdutoCadResource produtoCadResource = new ProdutoCadResource(produtoCadManagerBean);
		return produtoCadResource.exists(request);
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
	public Response save(RegistroDTO<VasilhameEntregarItem> dto) {
		ResponseDTO<VasilhameEntregarItem> response = new ResponseDTO<>();
		
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
			LOG.error("Falha ao salvar Vasilhame Entregar - Item", e);
			response.msg = "Falha ao salvar Vasilhame Entregar - Item";
		}

		return Response.status(response.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(response).type(MediaType.APPLICATION_JSON).build();
	}
	

	@POST
	@Path("/update")
	@Permissao(nome = "alteracao")
	public Response update(RegistroDTO<VasilhameEntregarItem> dto) {
		ResponseDTO<VasilhameEntregarItem> response = new ResponseDTO<>();

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
			LOG.error("Falha ao atualizar Vasilhame Entregar - Item", e);
			response.msg = "Falha ao atualizar Vasilhame Entregar - Item";
		}

		return Response.status(response.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(response).type(MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("/delete")
	@Permissao(nome = "exclusao")
	public Response delete(ListRegistroDTO<VasilhameEntregarItem> request) {
		ResponseDTO<VasilhameEntregarItem> response = new ResponseDTO<>();
		try {			
			List<CampoErroDTO> errors = this.managerBean.deletar(request.registros);			
			if (errors.isEmpty()) {
				response.success = true;
			} else {
				response.success = false;
				response.errors = errors;
			}		
		} catch (Exception e) {
			LOG.error("Falha ao excluir Vasilhames Entregar - Itens", e);
			response.success = false;
			response.msg = "Falha ao excluir Vasilhames Entregar - Itens";
		}	
		
		return Response.status(response.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(response).type(MediaType.APPLICATION_JSON).build();
	}
		
}
