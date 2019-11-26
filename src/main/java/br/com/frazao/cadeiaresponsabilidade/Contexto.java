package br.com.frazao.cadeiaresponsabilidade;

import java.security.Principal;
import java.util.Map;

public interface Contexto extends Map<String, Object> {

	String getCatalogo();

	String getComando();

	Object getRequisicao();

	Object getResposta();

	Principal getUsuario();

	void setCatalogo(String catalogo);

	void setComando(String comando);

	void setRequisicao(Object requisicao);

	void setResposta(Object resposta);

	void setUsuario(Principal usuario);
}
