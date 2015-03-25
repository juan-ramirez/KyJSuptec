package app.kyjsuptec.kjingenieros.activities;

import java.util.ArrayList;

public class DatoFormularioFactory {

    public int tipo;
    public String titulo;
    public int tipoDato;
    public ArrayList<String> listaSpinner;
    public boolean esSubtitulo = false;

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

    public DatoFormularioFactory(int tipo, String titulo, ArrayList<String> listaSpinner, boolean esSubtitulo) {
        this.tipo = tipo;
        this.titulo = titulo;
        this.listaSpinner = listaSpinner;
        this.esSubtitulo = esSubtitulo;
    }

    public DatoFormularioFactory(int tipo, String titulo, boolean esSubtitulo, int tipoDato) {
        this.tipo = tipo;
        this.titulo = titulo;
        this.esSubtitulo = esSubtitulo;
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

    public boolean isEsSubtitulo() {
        return esSubtitulo;
    }
}
