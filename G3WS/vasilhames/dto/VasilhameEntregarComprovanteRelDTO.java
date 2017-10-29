package com.g3sistemas.vasilhames.dto;

import java.util.List;

import com.g3sistemas.sql.filter.G3SqlSort;
import com.g3sistemas.vasilhames.entity.VasilhameEntregar;

public class VasilhameEntregarComprovanteRelDTO {
	
	public String delimitador;
	public String quebraLinha;
	public String formato;	
	public String nomeArquivo;
	public List<G3SqlSort> sort;	
	public VasilhameEntregar vasilhameEntregar;
}
