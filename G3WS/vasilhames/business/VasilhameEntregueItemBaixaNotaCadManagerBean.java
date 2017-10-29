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
import com.g3sistemas.fiscal.business.proprias.PropriaNotaCadManagerBean;
import com.g3sistemas.geral.business.EmpresaCadManagerBean;
import com.g3sistemas.geral.business.FilialCadManagerBean;
import com.g3sistemas.geral.entity.QEmpresa;
import com.g3sistemas.geral.entity.QFilial;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregueItemBaixa;
import com.g3sistemas.vasilhames.entity.QVasilhameEntregueItemBaixaNota;
import com.g3sistemas.vasilhames.entity.VasilhameEntregueItemBaixaNota;
import com.g3sistemas.vasilhames.enums.ModeloNota;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.jpa.impl.JPAQuery;

@Stateless
public class VasilhameEntregueItemBaixaNotaCadManagerBean extends AbstractCadManagerBean<VasilhameEntregueItemBaixaNota> {

	@PersistenceContext(unitName = "g3-ds")
	private EntityManager em;

	@EJB
	private EmpresaCadManagerBean empresaManagerBean;

	@EJB
	private FilialCadManagerBean filialCadManagerBean;
	
	@EJB
	private PropriaNotaCadManagerBean notaPropriaNotaCadManagerBean;	
	
	@EJB
	private VasilhameEntregueItemBaixaCadManagerBean vasilhameEntregueItemBaixaCadManagerBean;	
	

	private QVasilhameEntregueItemBaixaNota qVasilhameEntregueItemBaixaNota;
	private QEmpresa qEmpresaBaixa;
	private QFilial qFilialBaixa;
	private QVasilhameEntregueItemBaixa qVasilhameEntregueItemBaixa;
	private QEmpresa qEmpresaNota;
	private QFilial qFilialNota;

	private JPAQuery<VasilhameEntregueItemBaixaNota> query;

	private ConstructorExpression<VasilhameEntregueItemBaixaNota> projecaoLoad() {
		return QVasilhameEntregueItemBaixaNota.create(qVasilhameEntregueItemBaixaNota.idEmpresaBaixa,
				QEmpresa.create(qEmpresaBaixa.id, qEmpresaBaixa.nome), qVasilhameEntregueItemBaixaNota.idFilialBaixa,
				QFilial.create(qFilialBaixa.codigo, qFilialBaixa.descricao),
				qVasilhameEntregueItemBaixaNota.idVasilhameEntregueItemBaixa,
				qVasilhameEntregueItemBaixaNota.idEmpresaNota, QEmpresa.create(qEmpresaNota.id, qEmpresaNota.nome),
				qVasilhameEntregueItemBaixaNota.modelo, qVasilhameEntregueItemBaixaNota.idFilialNota,
				QFilial.create(qFilialNota.codigo, qFilialNota.descricao), qVasilhameEntregueItemBaixaNota.serie,
				qVasilhameEntregueItemBaixaNota.numero, qVasilhameEntregueItemBaixaNota.dataEmissao,
				qVasilhameEntregueItemBaixaNota.sequencia);
	}

	public VasilhameEntregueItemBaixaNota find(VasilhameEntregueItemBaixaNota registro) {
		this.query = new JPAQuery<>(em);
		this.qVasilhameEntregueItemBaixaNota = QVasilhameEntregueItemBaixaNota.vasilhameEntregueItemBaixaNota;	
		this.qEmpresaBaixa = new QEmpresa("qEmpresaBaixa");
		this.qFilialBaixa = new QFilial("qFilialBaixa");
		this.qVasilhameEntregueItemBaixa = QVasilhameEntregueItemBaixa.vasilhameEntregueItemBaixa;	
		this.qEmpresaNota = new QEmpresa("qEmpresaNota");
		this.qFilialNota = new QFilial("qFilialNota");

		return this.query.select(this.projecaoLoad())
				.from(qVasilhameEntregueItemBaixaNota)
				.leftJoin(qVasilhameEntregueItemBaixaNota.empresaBaixa, qEmpresaBaixa)
				.leftJoin(qVasilhameEntregueItemBaixaNota.filialBaixa, qFilialBaixa)
				.leftJoin(qVasilhameEntregueItemBaixaNota.vasilhameEntregueItemBaixa, qVasilhameEntregueItemBaixa)				
				.leftJoin(qVasilhameEntregueItemBaixaNota.empresaNota, qEmpresaNota)
				.leftJoin(qVasilhameEntregueItemBaixaNota.filialNota, qFilialNota)
				.where(qVasilhameEntregueItemBaixaNota.idEmpresaBaixa.eq(registro.getIdEmpresaBaixa())
				.and(qVasilhameEntregueItemBaixaNota.idFilialBaixa.eq(registro.getIdFilialBaixa())
				.and(qVasilhameEntregueItemBaixaNota.idVasilhameEntregueItemBaixa.eq(registro.getIdVasilhameEntregueItemBaixa())
				.and(qVasilhameEntregueItemBaixaNota.idEmpresaNota.eq(registro.getIdEmpresaNota())
				.and(qVasilhameEntregueItemBaixaNota.modelo.eq(registro.getModelo())
				.and(qVasilhameEntregueItemBaixaNota.idFilialNota.eq(registro.getIdFilialNota())
				.and(qVasilhameEntregueItemBaixaNota.serie.eq(registro.getSerie())
				.and(qVasilhameEntregueItemBaixaNota.numero.eq(registro.getNumero())
				.and(qVasilhameEntregueItemBaixaNota.dataEmissao.eq(registro.getDataEmissao())
				.and(qVasilhameEntregueItemBaixaNota.sequencia.eq(registro.getSequencia()))))))))))).fetchOne();
	}
	
	@Override
	public List<CampoErroDTO> validar(RegistroDTO<VasilhameEntregueItemBaixaNota> dto) {
		List<CampoErroDTO> erros = new ArrayList<CampoErroDTO>();

		this.validaEmpresaBaixa(dto, erros);
		this.validaFilialBaixa(dto, erros);
		this.validaVasilhameEntregueItemBaixa(dto, erros);
		this.validaEmpresaNota(dto, erros);
		this.validaModelo(dto, erros);
		this.validaFilialNota(dto, erros);
		this.validaSerie(dto, erros);
		this.validaNumero(dto, erros);
		this.validaDataEmissao(dto, erros);
		this.validaSequencia(dto, erros);
		this.validaNotaPropria(dto, erros);
		this.validaUnique(dto, erros);

		return erros;
	}

	private void validaEmpresaBaixa(RegistroDTO<VasilhameEntregueItemBaixaNota> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getIdEmpresaBaixa() == null) {
			erros.add(new CampoErroDTO("idEmpresaBaixa", "Campo obrigatório"));
		} else if (!this.empresaManagerBean.existeEmpresa(dto.registro.getIdEmpresaBaixa())) {
			erros.add(new CampoErroDTO("idEmpresaBaixa", "Empresa Baixa não cadastrada"));
		}
	}

	private void validaFilialBaixa(RegistroDTO<VasilhameEntregueItemBaixaNota> dto, List<CampoErroDTO> erros) {
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

	private void validaVasilhameEntregueItemBaixa(RegistroDTO<VasilhameEntregueItemBaixaNota> dto,
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

	private void validaEmpresaNota(RegistroDTO<VasilhameEntregueItemBaixaNota> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getIdEmpresaNota() == null) {
			erros.add(new CampoErroDTO("idEmpresaNota", "Campo obrigatório"));
		} else if (!this.empresaManagerBean.existeEmpresa(dto.registro.getIdEmpresaNota())) {
			erros.add(new CampoErroDTO("idEmpresaNota", "Empresa Nota não cadastrada"));
		}
	}

	private void validaModelo(RegistroDTO<VasilhameEntregueItemBaixaNota> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getModelo() != null) {
			if (!ModeloNota.existe(dto.registro.getModelo())) {
				erros.add(new CampoErroDTO("modelo", "Modelo da nota inválido"));
			}
		} else {
			erros.add(new CampoErroDTO("modelo", "Campo obrigatório"));
		}
	}

	private void validaFilialNota(RegistroDTO<VasilhameEntregueItemBaixaNota> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getIdFilialNota() != null) {
			if (dto.registro.getIdEmpresaNota() != null) {
				if (!this.filialCadManagerBean.existeFilialEmpresa(dto.registro.getIdEmpresaNota(),
						dto.registro.getIdFilialNota())) {
					erros.add(new CampoErroDTO("idFilialNota", "Filial Baixa não cadastrada"));
				}
			}
		} else {
			erros.add(new CampoErroDTO("idFilialNota", "Campo obrigatório"));
		}
	}

	private void validaSerie(RegistroDTO<VasilhameEntregueItemBaixaNota> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getSerie() == null) {
			erros.add(new CampoErroDTO("serie", "Campo obrigatório"));
		}
	}

	private void validaNumero(RegistroDTO<VasilhameEntregueItemBaixaNota> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getNumero() == null) {
			erros.add(new CampoErroDTO("numero", "Campo obrigatório"));
		}
	}

	private void validaDataEmissao(RegistroDTO<VasilhameEntregueItemBaixaNota> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getDataEmissao() == null) {
			erros.add(new CampoErroDTO("dataEmissao", "Campo obrigatório"));
		}
	}

	private void validaSequencia(RegistroDTO<VasilhameEntregueItemBaixaNota> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getSequencia() == null) {
			erros.add(new CampoErroDTO("sequencia", "Campo obrigatório"));
		}
	}
	
	private void validaNotaPropria(RegistroDTO<VasilhameEntregueItemBaixaNota> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getIdEmpresaNota() != null && dto.registro.getModelo() != null
				&& dto.registro.getIdFilialNota() != null && dto.registro.getSerie() != null
				&& dto.registro.getNumero() != null && dto.registro.getDataEmissao() != null) {				
			if (!this.notaPropriaNotaCadManagerBean.existeNotaPropria(dto.registro.getIdEmpresaNota(), dto.registro.getModelo(), 
					dto.registro.getIdFilialNota(), dto.registro.getSerie(), dto.registro.getNumero(), dto.registro.getDataEmissao())) {
				erros.add(new CampoErroDTO("idEmpresaNota", "Cupom Fiscal não cadastrado"));
				erros.add(new CampoErroDTO("modelo", "Cupom Fiscal não cadastrado", true));
				erros.add(new CampoErroDTO("idFilialNota", "Cupom Fiscal não cadastrado", true));
				erros.add(new CampoErroDTO("serie", "Cupom Fiscal não cadastrado", true));
				erros.add(new CampoErroDTO("numero", "Cupom Fiscal não cadastrado", true));
				erros.add(new CampoErroDTO("dataEmissao", "Cupom Fiscal não cadastrado", true));				
			}	
		}	
	}
	
	private void validaUnique(RegistroDTO<VasilhameEntregueItemBaixaNota> dto, List<CampoErroDTO> erros) {
		this.query = new JPAQuery<>(this.em);
		this.qVasilhameEntregueItemBaixaNota = QVasilhameEntregueItemBaixaNota.vasilhameEntregueItemBaixaNota;
		
		if (dto.registro.getIdEmpresaBaixa() != null && dto.registro.getIdFilialBaixa() != null 
				&& dto.registro.getIdVasilhameEntregueItemBaixa() != null && dto.registro.getIdEmpresaNota() != null
				&& dto.registro.getModelo() != null && dto.registro.getIdFilialNota() != null
				&& dto.registro.getSerie() != null && dto.registro.getNumero() != null 
				&& dto.registro.getDataEmissao() != null && dto.registro.getSequencia() != null) {

			this.query.select(qVasilhameEntregueItemBaixaNota)
				.from(qVasilhameEntregueItemBaixaNota)
				.where(qVasilhameEntregueItemBaixaNota.idEmpresaBaixa.eq(dto.registro.getIdEmpresaBaixa())
					.and(qVasilhameEntregueItemBaixaNota.idFilialBaixa.eq(dto.registro.getIdFilialBaixa()))
					.and(qVasilhameEntregueItemBaixaNota.idVasilhameEntregueItemBaixa.eq(dto.registro.getIdVasilhameEntregueItemBaixa()))
					.and(qVasilhameEntregueItemBaixaNota.idEmpresaNota.eq(dto.registro.getIdEmpresaNota()))
					.and(qVasilhameEntregueItemBaixaNota.modelo.eq(dto.registro.getModelo()))
					.and(qVasilhameEntregueItemBaixaNota.idFilialNota.eq(dto.registro.getIdFilialNota()))
					.and(qVasilhameEntregueItemBaixaNota.serie.eq(dto.registro.getSerie()))
					.and(qVasilhameEntregueItemBaixaNota.numero.eq(dto.registro.getNumero()))
					.and(qVasilhameEntregueItemBaixaNota.dataEmissao.eq(dto.registro.getDataEmissao()))
					.and(qVasilhameEntregueItemBaixaNota.sequencia.eq(dto.registro.getSequencia())));
			
			VasilhameEntregueItemBaixaNota vasilhameEntregueItemBaixaNota = this.query.fetchOne();

			if (vasilhameEntregueItemBaixaNota != null) {
				erros.add(new CampoErroDTO("idEmpresaBaixa", "Registro já existe"));			
			}
		}
	}
	
	@Override
	public List<CampoErroDTO> validaDeletar(List<VasilhameEntregueItemBaixaNota> registros) {
		List<CampoErroDTO> erros = new ArrayList<CampoErroDTO>();
		
		for (VasilhameEntregueItemBaixaNota nota : registros) {
			this.validaDeletarVasilhame(nota, erros);
		}		

		return erros;
	}
	
	private void validaDeletarVasilhame(VasilhameEntregueItemBaixaNota nota, List<CampoErroDTO> erros) {
		if (nota != null) {
			if (nota.getIdVasilhameEntregueItemBaixa() != null) {
				
				Integer situacao = this.vasilhameEntregueItemBaixaCadManagerBean.findSituacaoVasilhaEntregue(
						nota.getIdEmpresaBaixa(), nota.getIdFilialBaixa(), nota.getIdVasilhameEntregueItemBaixa());
				
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
	public Class<VasilhameEntregueItemBaixaNota> getTypeParameterClass() {
		return VasilhameEntregueItemBaixaNota.class;
	}

}
