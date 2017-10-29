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
import com.g3sistemas.geral.business.pessoas.PessoaCadManagerBean;
import com.g3sistemas.geral.entity.Empresa;
import com.g3sistemas.geral.entity.Filial;
import com.g3sistemas.geral.entity.Pessoa;
import com.g3sistemas.geral.rest.FilialCadResource;
import com.g3sistemas.geral.rest.empresa.EmpresaCadResource;
import com.g3sistemas.geral.rest.pessoas.PessoaCadResource;
import com.g3sistemas.sistema.business.UsuarioManagerBean;
import com.g3sistemas.sistema.entity.Usuario;
import com.g3sistemas.sistema.rest.UsuarioResource;
import com.g3sistemas.vasilhames.business.VasilhameEntregueCadManagerBean;
import com.g3sistemas.vasilhames.business.VasilhameEntregueItemCadManagerBean;
import com.g3sistemas.vasilhames.entity.VasilhameEntregue;

@Path("vasilhame/entregues/cadastro")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Programa(nome = "vasilhames.EntregueCad")
public class VasilhameEntregueCadResource {
	
	private static final Logger LOG = LoggerFactory.getLogger(VasilhameEntregueCadResource.class);
	
	@EJB
	private VasilhameEntregueCadManagerBean managerBean;	
	
	@EJB
	private VasilhameEntregueItemCadManagerBean vasilhameEntregueItemCadManagerBean;
	
	@EJB
	private EmpresaCadManagerBean empresaCadManagerBean;
	
	@EJB
	private FilialCadManagerBean filialCadManagerBean;
	
	@EJB
	private PessoaCadManagerBean pessoaCadManagerBean;
	
	@EJB
	private UsuarioManagerBean usuarioManagerBean;
	
	
	public VasilhameEntregueCadResource() {
		
	}
	
	public VasilhameEntregueCadResource(VasilhameEntregueCadManagerBean managerBean) {
		this.managerBean = managerBean;
	}
	
	
	@POST
	@Path("/exists")
	public Response exists(RegistroDTO<VasilhameEntregue> request) {
		ResponseDTO<VasilhameEntregue> dto = new ResponseDTO<>();
		VasilhameEntregue vasilhameEntregue = this.managerBean.find(request.registro);
		if (vasilhameEntregue == null) {
			dto.success = false;
			dto.errors.add(new CampoErroDTO("vasilhameEntregue", "Vasilhame entregue não cadastrado"));
		} else {
			dto.registro = vasilhameEntregue;
		}
		return Response.status(dto.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(dto).type(MediaType.APPLICATION_JSON).build();

	}	
	
	@POST
	@Path("/exists/basic")
	public Response existsBasic(RegistroDTO<VasilhameEntregue> request) {
		ResponseDTO<VasilhameEntregue> dto = new ResponseDTO<>();		
		if (!this.managerBean.existeVasilhameEntregue(request.registro)) {
			dto.success = false;
			dto.errors.add(new CampoErroDTO("vasilhameEntregue", "Vasilhame entregue não cadastrado"));
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
	@Path("/exists/pessoa")
	public Response existsPessoa(RegistroDTO<Pessoa> request) {
		PessoaCadResource pessoaCadResource = new PessoaCadResource(pessoaCadManagerBean);
		return pessoaCadResource.existsCpfcnpj(request);
	}
	
	@POST
	@Path("/exists/pessoa/ativa")
	public Response existsPessoaAtiva(RegistroDTO<Pessoa> request) {
		PessoaCadResource pessoaCadResource = new PessoaCadResource(pessoaCadManagerBean);
		return pessoaCadResource.existsPessoaAtiva(request);
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
	public Response save(RegistroDTO<VasilhameEntregue> dto) {
		ResponseDTO<VasilhameEntregue> response = new ResponseDTO<>();
		
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
			LOG.error("Falha ao salvar Vasilhame Entregue", e);
			response.msg = "Falha ao salvar Vasilhame Entregue";
		}

		return Response.status(response.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(response).type(MediaType.APPLICATION_JSON).build();
	}
	

	@POST
	@Path("/update")
	@Permissao(nome = "alteracao")
	public Response update(RegistroDTO<VasilhameEntregue> dto) {
		ResponseDTO<VasilhameEntregue> response = new ResponseDTO<>();

		try {
			Pessoa pessoa = dto.registro.getPessoa();
			List<CampoErroDTO> errors = this.managerBean.atualizar(dto);
			dto.registro.setPessoa(pessoa);
			if (errors.isEmpty()) {				
				response.registro = dto.registro;
			} else {
				response.success = false;
				response.errors = errors;
			}
		} catch (Exception e) {
			response.success = false;
			LOG.error("Falha ao atualizar Vasilhame Entregue", e);
			response.msg = "Falha ao atualizar Vasilhame Entregue";
		}

		return Response.status(response.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(response).type(MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("/delete")
	@Permissao(nome = "exclusao")
	public Response delete(ListRegistroDTO<VasilhameEntregue> request) {
		ResponseDTO<VasilhameEntregue> response = new ResponseDTO<>();
		try {			
			List<CampoErroDTO> errors = this.managerBean.deletar(request.registros);			
			if (errors.isEmpty()) {
				response.success = true;
			} else {	
				response.success = false;
				response.errors = errors;
			}		
		} catch (Exception e) {
			LOG.error("Falha ao excluir Vasilhames Entregues", e);
			response.success = false;
			response.msg = "Falha ao excluir Vasilhames Entregues";
		}	
		
		return Response.status(response.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(response).type(MediaType.APPLICATION_JSON).build();
	}	
	
	@POST
	@Path("/finalizar")
	public Response finalizarVasilhame(RegistroDTO<VasilhameEntregue> request) {
		ResponseDTO<VasilhameEntregue> response = new ResponseDTO<>();

		try {
			if (request.registro.getSituacao() != null) {
				if (request.registro.getSituacao() != 2) {
					if (request.registro.getSituacao() == 1) {
						this.managerBean.atualizaSituacaoVasilhame(request, 2);
						response.registro = request.registro;
					} else {
						response.success = false;
						response.msg = "Não foi possível finalizar o vasilhame entregue, situação precisa ser aberto";
					}						
				} else {
					response.success = false;
					response.msg = "Vasilhame já está finalizado";
				}				
			}

		} catch (Exception e) {
			response.success = false;
			LOG.error("Falha ao finalizar Vasilhame Entregue", e);
			response.msg = "Falha ao finalizar Vasilhame Entregue";
		}

		return Response.status(response.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(response).type(MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("/abrir")
	public Response abrirVasilhame(RegistroDTO<VasilhameEntregue> request) {
		ResponseDTO<VasilhameEntregue> response = new ResponseDTO<>();

		try {
			if (!this.vasilhameEntregueItemCadManagerBean.existeVasilhameEntregueItemBaixa(
					request.registro.getIdEmpresa(), request.registro.getIdFilial(), request.registro.getIdCodigo())) {
				if (request.registro.getSituacao() != null) {
					if (request.registro.getSituacao() != 1) {
						if (request.registro.getSituacao() == 2) {
							this.managerBean.atualizaSituacaoVasilhame(request, 1);
							response.registro = request.registro;
						} else {
							response.success = false;
							response.msg = "Não foi possível abrir o vasilhame entregue, situação precisa ser pendente";
						}	
					} else {
						response.success = false;
						response.msg = "Vasilhame já está aberto";
					}					
				}	
			} else {
				response.success = false;
				response.msg = "Não foi possível abrir o vasilhame entregue, baixas cadastradas";
			}		

		} catch (Exception e) {
			response.success = false;
			LOG.error("Falha ao abrir Vasilhame Entregue", e);
			response.msg = "Falha ao abrir Vasilhame Entregue";
		}

		return Response.status(response.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(response).type(MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("/inutilizar")
	public Response inutilizarVasilhame(RegistroDTO<VasilhameEntregue> request) {
		ResponseDTO<VasilhameEntregue> response = new ResponseDTO<>();

		try {
			if (!this.vasilhameEntregueItemCadManagerBean.existeVasilhameEntregueItemBaixa(
					request.registro.getIdEmpresa(), request.registro.getIdFilial(), request.registro.getIdCodigo())) {
				if (request.registro.getSituacao() != null) {				
					if (request.registro.getSituacao() != 8) {
						if (request.registro.getSituacao() == 1 || request.registro.getSituacao() == 2) {
							this.managerBean.atualizaSituacaoVasilhame(request, 8);
							response.registro = request.registro;
						} else {
							response.success = false;
							response.msg = "Não foi possível inutilizar o vasilhame entregue, situação precisa ser aberto ou pendente";
						}
					} else {
						response.success = false;
						response.msg = "Vasilhame já está inutilizado";
					}
				}
			} else {
				response.success = false;
				response.msg = "Não foi possível inutilizar o vasilhame entregue, baixas cadastradas";
			}

		} catch (Exception e) {
			response.success = false;
			LOG.error("Falha ao inutilizar Vasilhame Entregue", e);
			response.msg = "Falha ao inutilizar Vasilhame Entregue";
		}

		return Response.status(response.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(response).type(MediaType.APPLICATION_JSON).build();
	}	
	
	@POST
	@Path("/consultasituacao")
	public Response consultaSituacao(RegistroDTO<VasilhameEntregue> request) {
		ResponseDTO<VasilhameEntregue> dto = new ResponseDTO<>();
		VasilhameEntregue vasilhameEntregue = this.managerBean.findSituacao(request.registro);
		if (vasilhameEntregue == null) {
			dto.success = false;
			dto.errors.add(new CampoErroDTO("vasilhameEntregue", "Falha ao consultar situação"));
		} else {			
			dto.registro = vasilhameEntregue;
		}
		
		return Response.status(dto.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(dto).type(MediaType.APPLICATION_JSON).build();
	}

}
