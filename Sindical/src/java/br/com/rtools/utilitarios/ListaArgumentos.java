package br.com.rtools.utilitarios;

import java.util.List;
import java.util.Vector;

public class ListaArgumentos {

    private List lista = new Vector<List>();

    public ListaArgumentos() {
        setLista(new Vector<List>());
    }

    public ListaArgumentos(String campo, String tipo) {
        this.adicionarObjeto(new Vector<List>(), campo, tipo);
    }

    public String adicionarObjeto(List list, String campo, String tipo) {
        list.add(campo);
        list.add(tipo);
        lista.add(list);
        return null;
    }

    public List getLista() {
        return lista;
    }

    public void setLista(List lista) {
        this.lista = lista;
    }
}
