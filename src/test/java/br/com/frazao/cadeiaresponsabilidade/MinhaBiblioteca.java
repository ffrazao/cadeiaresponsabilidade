package br.com.frazao.cadeiaresponsabilidade;

public class MinhaBiblioteca extends Biblioteca {

	private static MinhaBiblioteca instance;

	public static final MinhaBiblioteca getInstance() throws Exception {
		if (MinhaBiblioteca.instance == null) {
			MinhaBiblioteca.instance = new MinhaBiblioteca();
			MinhaBiblioteca.instance.carregar(MinhaBiblioteca.class.getPackage());
		}
		return MinhaBiblioteca.instance;
	}

}
