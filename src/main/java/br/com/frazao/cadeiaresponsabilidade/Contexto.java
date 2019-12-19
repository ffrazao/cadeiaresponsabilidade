package br.com.frazao.cadeiaresponsabilidade;

import java.security.Principal;
import java.util.Map;

public interface Contexto extends Map<String, Object> {

	String getCatalogo();

	String getComando();

	Object getRequisicao();

	Object getResposta();

	Object getResposta(Integer valor);

	Integer getRespostaTamanhoHistorico();

	Principal getUsuario();

	void setCatalogo(String valor);

	void setComando(String valor);

	void setRequisicao(Object valor);

	void setResposta(Object valor);

	void setUsuario(Principal valor);

}
