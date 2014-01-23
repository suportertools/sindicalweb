package br.com.rtools.sistema.db;

import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class SisPessoaDBToplink extends DB implements SisPessoaDB {

    @Override
    public List pesquisarSisPessoa(String desc, String por, String como) {
        if (por.equals("cnpj") || por.equals("cpf")) {
            por = "documento";
        }
        List lista;
        String textQuery = null;
        if (como.equals("T")) {
            textQuery = "";
        } else if (como.equals("P")) {
            desc = "%" + desc.toLowerCase().toUpperCase() + "%";
            textQuery = "select objeto from SisPessoa objeto where UPPER(objeto." + por + ") like :desc"
                    + " order by objeto.nome";
        } else if (como.equals("I")) {
            desc = desc.toLowerCase().toUpperCase() + "%";
            textQuery = "select objeto from SisPessoa objeto where UPPER(objeto." + por + ") like :desc"
                    + " order by objeto.nome";
        }
        try {
            Query qry = getEntityManager().createQuery(textQuery);
            if ((desc != null) && (!(como.equals("T")))) {
                qry.setParameter("desc", desc);
            }
            lista = qry.getResultList();
        } catch (Exception e) {
            lista = new ArrayList();
        }
        return lista;
    }
}
