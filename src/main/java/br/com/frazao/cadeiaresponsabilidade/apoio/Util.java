package br.com.frazao.cadeiaresponsabilidade.apoio;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Util {
	
	public static List<?> itemsRepetidos(List<?> lista) {	
		Set<Object> limpo = new HashSet<>();
		List<Object> result = lista.stream().filter(c -> limpo.add(c) == false).collect(Collectors.toList());
		return result;
	}

}
