package com.g3sistemas.vasilhames.business;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.g3sistemas.abstracts.AbstractCadManagerBean;
import com.g3sistemas.dto.CampoErroDTO;
import com.g3sistemas.dto.RegistroDTO;
import com.g3sistemas.estoques.entity.QProduto;
import com.g3sistemas.geral.business.EmpresaCadManagerBean;
import com.g3sistemas.geral.business.FilialCadManagerBean;
import com.g3sistemas.geral.entity.QEmpresa;
import com.g3sistemas.geral.entity.QFilial;
import com.g3sistemas.pdv.business.ecf.EcfCadManagerBean;
import com.g3sistemas.pdv.business.ecf.EcfCupomCadManagerBean;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregueItem;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregueItemBaixa;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregueItemBaixaCupom;
import com.g3sistemas.vasilhames.entity.VasilhameEntregueItemBaixaCupom;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.jpa.impl.JPAQuery;

@Stateless
public class VasilhameEntregueItemBaixaCupomCadManagerBean extends AbstractCadManagerBean<VasilhameEntregueItemBaixaCupom> {

	@PersistenceContext(unitName = "g3-ds")
	private EntityManager em;

	@EJB
	private EmpresaCadManagerBean empresaManagerBean;

	@EJB
	private FilialCadManagerBean filialCadManagerBean;
	
	@EJB
	private EcfCadManagerBean ecfCadManagerBean;
	
	@EJB
	private EcfCupomCadManagerBean ecfCupomCadManagerBean;
	
	@EJB
	private VasilhameEntregueItemBaixaCadManagerBean vasilhameEntregueItemBaixaCadManagerBean;	
	
	
	private QVasilhameEntregueItemBaixaCupom qVasilhameEntregueItemBaixaCupom;
	private QEmpresa qEmpresaBaixa;
	private QFilial qFilialBaixa;
	private QVasilhameEntregueItemBaixa qVasilhameEntregueItemBaixa;
	private QVasilhameEntregueItem qVasilhameEntregueItem;
	private QProduto qProduto;
	private QEmpresa qEmpresaCupom;
	private QFilial qFilialCupom;

	private JPAQuery<VasilhameEntregueItemBaixaCupom> query;

	private ConstructorExpression<VasilhameEntregueItemBaixaCupom> projecaoLoad() {
		return QVasilhameEntregueItemBaixaCupom.create(qVasilhameEntregueItemBaixaCupom.idEmpresaBaixa,
				QEmpresa.create(qEmpresaBaixa.id, qEmpresaBaixa.nome), qVasilhameEntregueItemBaixaCupom.idFilialBaixa,
				QFilial.create(qFilialBaixa.codigo, qFilialBaixa.descricao),
				qVasilhameEntregueItemBaixaCupom.idVasilhameEntregueItemBaixa,
				QVasilhameEntregueItemBaixa.create(qVasilhameEntregueItemBaixa.quantidade,
						QVasilhameEntregueItem.create(qVasilhameEntregueItem.idProduto,
								QProduto.create(qProduto.id, qProduto.digito, qProduto.descricao),
								qVasilhameEntregueItem.quantidade)),
				qVasilhameEntregueItemBaixaCupom.idEmpresaCupom, QEmpresa.create(qEmpresaCupom.id, qEmpresaCupom.nome),
				qVasilhameEntregueItemBaixaCupom.idFilialCupom,
				QFilial.create(qFilialCupom.codigo, qFilialCupom.descricao),
				qVasilhameEntregueItemBaixaCupom.idEcfCupom, qVasilhameEntregueItemBaixaCupom.numero,
				qVasilhameEntregueItemBaixaCupom.dataEmissao, qVasilhameEntregueItemBaixaCupom.sequencia);
	}

	public VasilhameEntregueItemBaixaCupom find(VasilhameEntregueItemBaixaCupom registro) {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregueItemBaixaCupom = QVasilhameEntregueItemBaixaCupom.vasilhameEntregueItemBaixaCupom;	
		this.qEmpresaBaixa = new QEmpresa("empresaBaixa");
		this.qFilialBaixa = new QFilial("filialBaixa");
		this.qVasilhameEntregueItemBaixa = QVasilhameEntregueItemBaixa.vasilhameEntregueItemBaixa;
		this.qVasilhameEntregueItem = QVasilhameEntregueItem.vasilhameEntregueItem;
		this.qProduto = QProduto.produto;
		this.qEmpresaCupom = new QEmpresa("empresaCupom");
		this.qFilialCupom = new QFilial("filialCupom");	

		return this.query.select(this.projecaoLoad())
				.from(qVasilhameEntregueItemBaixaCupom)
				.leftJoin(qVasilhameEntregueItemBaixaCupom.empresaBaixa, qEmpresaBaixa)
				.leftJoin(qVasilhameEntregueItemBaixaCupom.filialBaixa, qFilialBaixa)
				.leftJoin(qVasilhameEntregueItemBaixaCupom.vasilhameEntregueItemBaixa, qVasilhameEntregueItemBaixa)
				.leftJoin(qVasilhameEntregueItemBaixa.vasilhameEntregueItem, qVasilhameEntregueItem)
				.leftJoin(qVasilhameEntregueItem.produto, qProduto)
				.leftJoin(qVasilhameEntregueItemBaixaCupom.empresaCupom, qEmpresaCupom)
				.leftJoin(qVasilhameEntregueItemBaixaCupom.filialCupom, qFilialCupom)
				.where(qVasilhameEntregueItemBaixaCupom.idEmpresaBaixa.eq(registro.getIdEmpresaBaixa())
				.and(qVasilhameEntregueItemBaixaCupom.idFilialBaixa.eq(registro.getIdFilialBaixa())
				.and(qVasilhameEntregueItemBaixaCupom.idVasilhameEntregueItemBaixa.eq(registro.getIdVasilhameEntregueItemBaixa())
				.and(qVasilhameEntregueItemBaixaCupom.idEmpresaCupom.eq(registro.getIdEmpresaCupom())				
				.and(qVasilhameEntregueItemBaixaCupom.idFilialCupom.eq(registro.getIdFilialCupom())
				.and(qVasilhameEntregueItemBaixaCupom.idEcfCupom.eq(registro.getIdEcfCupom())
				.and(qVasilhameEntregueItemBaixaCupom.numero.eq(registro.getNumero())
				.and(qVasilhameEntregueItemBaixaCupom.dataEmissao.eq(registro.getDataEmissao())
				.and(qVasilhameEntregueItemBaixaCupom.sequencia.eq(registro.getSequencia())))))))))).fetchOne();
	}
	
	@Override
	public List<CampoErroDTO> validar(RegistroDTO<VasilhameEntregueItemBaixaCupom> dto) {
		List<CampoErroDTO> erros = new ArrayList<CampoErroDTO>();

		this.validaEmpresaBaixa(dto, erros);
		this.validaFilialBaixa(dto, erros);
		this.validaVasilhameEntregueItemBaixa(dto, erros);
		this.validaEmpresaCupom(dto, erros);		
		this.validaFilialCupom(dto, erros);
		this.validaEcf(dto, erros);
		this.validaNumero(dto, erros);
		this.validaDataEmissao(dto, erros);
		this.validaSequencia(dto, erros);
		this.validaCupom(dto, erros);
		this.validaUnique(dto, erros);

		return erros;
	}

	private void validaEmpresaBaixa(RegistroDTO<VasilhameEntregueItemBaixaCupom> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getIdEmpresaBaixa() == null) {
			erros.add(new CampoErroDTO("idEmpresaBaixa", "Campo obrigatório"));
		} else if (!this.empresaManagerBean.existeEmpresa(dto.registro.getIdEmpresaBaixa())) {
			erros.add(new CampoErroDTO("idEmpresaBaixa", "Empresa Baixa não cadastrada"));
		}
	}

	private void validaFilialBaixa(RegistroDTO<VasilhameEntregueItemBaixaCupom> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getIdFilialBaixa() != null) {
			if (dto.registro.getIdEmpresaBaixa() != null) {
				if (!this.filialCadManagerBean.existeFilialEmpresa(dto.registro.getIdEmpresaBaixa(),
						dto.registro.getIdFilialBaixa())) {
					erros.add(new CampoErroDTO("idFilialBaixa", "Filial Baixa não cadastrada"));
				}
			}
		} else {
			erros.add(new CampoErroDTO("idFilialBaixa", "Campo obrigatório"));
		}
	}
	
	private void validaUsuarioFilialBaixa(RegistroDTO<VasilhameEntregueItemBaixaCupom> dto, List<CampoErroDTO> erros) {	
//		if ((dto.registro.getIdUsuario() != "" && dto.registro.getIdUsuario() != null) && dto.registro.getIdEmpresa() != null
//				&& dto.registro.getIdFilial() != null) {
//			if (!this.usuarioFilialCadManagerBean.validaFilialLiberada(dto.registro.getIdUsuario(),
//					dto.registro.getIdEmpresa(), dto.registro.getIdFilial())) {
//				erros.add(new CampoErroDTO("idFilial", "Filial não liberada para este usuário"));
//			}
//		}
	}

	private void validaVasilhameEntregueItemBaixa(RegistroDTO<VasilhameEntregueItemBaixaCupom> dto,
			List<CampoErroDTO> erros) {
		if (dto.registro.getIdVasilhameEntregueItemBaixa() != null) {
			if (dto.registro.getIdEmpresaBaixa() != null && dto.registro.getIdFilialBaixa() != null) {
				
				if (!this.vasilhameEntregueItemBaixaCadManagerBean.existeVasilhameEntregueItemBaixa(dto.registro.getIdEmpresaBaixa(), dto.registro.getIdFilialBaixa(),
						dto.registro.getIdVasilhameEntregueItemBaixa())) {
					erros.add(new CampoErroDTO("idVasilhameEntregueItemBaixa", "Vasilhame Entregue Item Baixa não cadastrado"));
				} else {
					Integer situacao = this.vasilhameEntregueItemBaixaCadManagerBean.findSituacaoVasilhaEntregue(
							dto.registro.getIdEmpresaBaixa(), dto.registro.getIdFilialBaixa(),
							dto.registro.getIdVasilhameEntregueItemBaixa());
					
					if (situacao == 8) {
						erros.add(new CampoErroDTO("idVasilhameEntregueItemBaixa", "Situação do vasilhame entregue está inutilizado"));
					}
				}
			}
							
		} else {
			erros.add(new CampoErroDTO("idVasilhameEntregueItemBaixa", "Campo obrigatório"));
		}
	}

	private void validaEmpresaCupom(RegistroDTO<VasilhameEntregueItemBaixaCupom> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getIdEmpresaCupom() == null) {
			erros.add(new CampoErroDTO("idEmpresaCupom", "Campo obrigatório"));
		} else if (!this.empresaManagerBean.existeEmpresa(dto.registro.getIdEmpresaCupom())) {
			erros.add(new CampoErroDTO("idEmpresaCupom", "Empresa Cupom não cadastrada"));
		}
	}

	private void validaFilialCupom(RegistroDTO<VasilhameEntregueItemBaixaCupom> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getIdFilialCupom() != null) {
			if (dto.registro.getIdEmpresaCupom() != null) {
				if (!this.filialCadManagerBean.existeFilialEmpresa(dto.registro.getIdEmpresaCupom(),
						dto.registro.getIdFilialCupom())) {
					erros.add(new CampoErroDTO("idFilialCupom", "Filial Baixa não cadastrada"));
				}
			}
		} else {
			erros.add(new CampoErroDTO("idFilialCupom", "Campo obrigatório"));
		}
	}
	
	private void validaUsuarioFilialCupom(RegistroDTO<VasilhameEntregueItemBaixaCupom> dto, List<CampoErroDTO> erros) {	
//		if ((dto.registro.getIdUsuario() != "" && dto.registro.getIdUsuario() != null) && dto.registro.getIdEmpresa() != null
//				&& dto.registro.getIdFilial() != null) {
//			if (!this.usuarioFilialCadManagerBean.validaFilialLiberada(dto.registro.getIdUsuario(),
//					dto.registro.getIdEmpresa(), dto.registro.getIdFilial())) {
//				erros.add(new CampoErroDTO("idFilial", "Filial não liberada para este usuário"));
//			}
//		}
	}

	private void validaEcf(RegistroDTO<VasilhameEntregueItemBaixaCupom> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getIdEmpresaCupom() != null && dto.registro.getIdFilialCupom() != null) {
			if (dto.registro.getIdEcfCupom() == null) {
				erros.add(new CampoErroDTO("idEcfCupom", "Campo obrigatório"));
			} else if (!this.ecfCadManagerBean.existeEcf(dto.registro.getIdEmpresaCupom(), 
					dto.registro.getIdFilialCupom(), dto.registro.getIdEcfCupom(), dto.registro.getDataEmissao())) {
				erros.add(new CampoErroDTO("idEcfCupom", "Ecf Cupom Produto não cadastrado"));
			}	
		}	
	}

	private void validaNumero(RegistroDTO<VasilhameEntregueItemBaixaCupom> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getNumero() == null) {
			erros.add(new CampoErroDTO("numero", "Campo obrigatório"));
		}
	}

	private void validaDataEmissao(RegistroDTO<VasilhameEntregueItemBaixaCupom> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getDataEmissao() == null) {
			erros.add(new CampoErroDTO("dataEmissao", "Campo obrigatório"));
		}
	}

	private void validaSequencia(RegistroDTO<VasilhameEntregueItemBaixaCupom> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getSequencia() == null) {
			erros.add(new CampoErroDTO("sequencia", "Campo obrigatório"));
		}
	}
	
	private void validaCupom(RegistroDTO<VasilhameEntregueItemBaixaCupom> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getIdEmpresaCupom() != null && dto.registro.getIdFilialCupom() != null 
				&& dto.registro.getIdEcfCupom() != null && dto.registro.getNumero() != null
				&& dto.registro.getDataEmissao() != null) {				
			if (!this.ecfCupomCadManagerBean.existeEcfCupom(dto.registro.getIdEmpresaCupom(), dto.registro.getIdFilialCupom(), 
					dto.registro.getIdEcfCupom(), dto.registro.getNumero(), dto.registro.getDataEmissao())) {
				erros.add(new CampoErroDTO("idEmpresaCupom", "Cupom Fiscal não cadastrado"));
				erros.add(new CampoErroDTO("idFilialCupom", "Cupom Fiscal não cadastrado", true));
				erros.add(new CampoErroDTO("idEcfCupom", "Cupom Fiscal não cadastrado", true));
				erros.add(new CampoErroDTO("numero", "Cupom Fiscal não cadastrado", true));
				erros.add(new CampoErroDTO("dataEmissao", "Cupom Fiscal não cadastrado", true));				
			}	
		}	
	}
	
	private void validaUnique(RegistroDTO<VasilhameEntregueItemBaixaCupom> dto, List<CampoErroDTO> erros) {
		this.query = new JPAQuery<>(this.em);
		this.qVasilhameEntregueItemBaixaCupom = QVasilhameEntregueItemBaixaCupom.vasilhameEntregueItemBaixaCupom;
		
		if (dto.registro.getIdEmpresaBaixa() != null && dto.registro.getIdFilialBaixa() != null
				&& dto.registro.getIdVasilhameEntregueItemBaixa() != null && dto.registro.getIdEmpresaCupom() != null
				&& dto.registro.getIdFilialCupom() != null && dto.registro.getIdEcfCupom() != null
				&& dto.registro.getNumero() != null && dto.registro.getDataEmissao() != null
				&& dto.registro.getSequencia() != null) {

			this.query.select(qVasilhameEntregueItemBaixaCupom)
				.from(qVasilhameEntregueItemBaixaCupom)
				.where(qVasilhameEntregueItemBaixaCupom.idEmpresaBaixa.eq(dto.registro.getIdEmpresaBaixa())
					.and(qVasilhameEntregueItemBaixaCupom.idFilialBaixa.eq(dto.registro.getIdFilialBaixa()))
					.and(qVasilhameEntregueItemBaixaCupom.idVasilhameEntregueItemBaixa.eq(dto.registro.getIdVasilhameEntregueItemBaixa()))
					.and(qVasilhameEntregueItemBaixaCupom.idEmpresaCupom.eq(dto.registro.getIdEmpresaCupom()))					
					.and(qVasilhameEntregueItemBaixaCupom.idFilialCupom.eq(dto.registro.getIdFilialCupom()))
					.and(qVasilhameEntregueItemBaixaCupom.idEcfCupom.eq(dto.registro.getIdEcfCupom()))
					.and(qVasilhameEntregueItemBaixaCupom.numero.eq(dto.registro.getNumero()))
					.and(qVasilhameEntregueItemBaixaCupom.dataEmissao.eq(dto.registro.getDataEmissao()))
					.and(qVasilhameEntregueItemBaixaCupom.sequencia.eq(dto.registro.getSequencia())));
			
			VasilhameEntregueItemBaixaCupom vasilhameEntregueItemBaixaCupom = this.query.fetchOne();

			if (vasilhameEntregueItemBaixaCupom != null) {
				erros.add(new CampoErroDTO("idEmpresaBaixa", "Registro já existe"));			
			}
		}
	}
	
	@Override
	public List<CampoErroDTO> validaDeletar(List<VasilhameEntregueItemBaixaCupom> registros) {
		List<CampoErroDTO> erros = new ArrayList<CampoErroDTO>();
		
		for (VasilhameEntregueItemBaixaCupom cupom : registros) {
			this.validaDeletarVasilhame(cupom, erros);
		}		

		return erros;
	}
	
	private void validaDeletarVasilhame(VasilhameEntregueItemBaixaCupom cupom, List<CampoErroDTO> erros) {
		if (cupom != null) {
			if (cupom.getIdVasilhameEntregueItemBaixa() != null) {
				
				Integer situacao = this.vasilhameEntregueItemBaixaCadManagerBean.findSituacaoVasilhaEntregue(
						cupom.getIdEmpresaBaixa(), cupom.getIdFilialBaixa(), cupom.getIdVasilhameEntregueItemBaixa());

				if (situacao == 8) {
					erros.add(new CampoErroDTO("idVasilhameEntregueItemBaixa", "Situação do vasilhame entregue está inutilizado"));
				}			
			}						
		}
	}

	@Override
	public EntityManager getEm() {
		return em;
	}

	@Override
	public Class<VasilhameEntregueItemBaixaCupom> getTypeParameterClass() {
		return VasilhameEntregueItemBaixaCupom.class;
	}

}
