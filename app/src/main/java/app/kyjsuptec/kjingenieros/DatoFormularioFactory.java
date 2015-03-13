package app.kyjsuptec.kjingenieros;

import java.util.ArrayList;

public class DatoFormularioFactory {

	public int tipo;
	public String titulo;
	public int tipoDato;
	public ArrayList<String> listaSpinner;

	public DatoFormularioFactory(int tipo, String titulo) {
		super();
		this.tipo = tipo;
		this.titulo = titulo;
	}

	public DatoFormularioFactory(int tipo, String titulo, int tipoDato) {
		super();
		this.tipo = tipo;
		this.titulo = titulo;
		this.tipoDato = tipoDato;
	}

	public DatoFormularioFactory(int tipo, String titulo,
			ArrayList<String> listaSpinner) {
		super();
		this.tipo = tipo;
		this.titulo = titulo;
		this.listaSpinner = listaSpinner;
	}

	public ArrayList<String> getListaSpinner() {
		return listaSpinner;
	}

	public int getTipo() {
		return tipo;
	}

	public String getTitulo() {
		return titulo;
	}

	public int getTipoDato() {
		return tipoDato;
	}

}
