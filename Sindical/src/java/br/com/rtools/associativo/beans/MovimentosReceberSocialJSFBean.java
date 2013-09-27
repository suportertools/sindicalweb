package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.db.MovimentosReceberSocialDB;
import br.com.rtools.associativo.db.MovimentosReceberSocialDBToplink;
import br.com.rtools.utilitarios.DataObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MovimentosReceberSocialJSFBean {

    private String porPesquisa = "";
    private List<DataObject> listaMovimento = new ArrayList();

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public List<DataObject> getListaMovimento() {
        if (listaMovimento.isEmpty()) {
            MovimentosReceberSocialDB db = new MovimentosReceberSocialDBToplink();
            List<Vector> lista = db.pesquisaListaMovimentos();
            for (int i = 0; i < lista.size(); i++) {

                listaMovimento.add(new DataObject(
                        true,
                        lista.get(i).get(14),
                        lista.get(i).get(0),
                        lista.get(i).get(1),
                        lista.get(i).get(2),
                        lista.get(i).get(3),
                        lista.get(i).get(4),
                        lista.get(i).get(5),
                        lista.get(i).get(6),
                        lista.get(i).get(7),
                        lista.get(i).get(8),
                        lista.get(i).get(9),
                        lista.get(i).get(10),
                        lista.get(i).get(11),
                        lista.get(i).get(12),
                        lista.get(i).get(13))
                );
            }
        }
        return listaMovimento;
    }

    public void setListaMovimento(List<DataObject> listaMovimento) {
        this.listaMovimento = listaMovimento;
    }
}
