package br.com.rtools.associativo.db;

import br.com.rtools.associativo.MatriculaConvenioMedico;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class MatriculaConvenioMedicoDBToplink extends DB implements MatriculaConvenioMedicoDB {

    @Override
    public List pesquisaConvenioMedico(String desc, String por, String como, Boolean ativo) {
        if(desc.isEmpty()) {
            return new ArrayList();
        }
        List lista = new ArrayList<>();
        String textQuery = null;
        String queryWhere = "";
        if (ativo) {
            queryWhere = " AND MC.dtInativo IS NULL ";
        } else {
            queryWhere = " AND MC.dtInativo IS NOT NULL ";
        }
        if (por.equals("nome")) {
            por = "nome";
            switch (como) {
                case "P":
                    desc = "%" + desc.toLowerCase().toUpperCase() + "%";
                    textQuery = " SELECT MC                                             "
                            + "     FROM MatriculaConvenioMedico AS MC                  "
                            + "    WHERE UPPER(MC.servicoPessoa.pessoa.nome) LIKE :desc " + queryWhere
                            + " ORDER BY MC.servicoPessoa.pessoa.nome ";
                    break;
                case "I":
                    por = "nome";
                    desc = desc.toLowerCase().toUpperCase() + "%";
                    textQuery = " SELECT MC                                             "
                            + "     FROM MatriculaConvenioMedico AS MC                  "
                            + "    WHERE UPPER(MC.servicoPessoa.pessoa.nome) LIKE :desc " + queryWhere
                            + " ORDER BY MC.servicoPessoa.pessoa.nome ";
                    break;
            }
        }
        if (por.equals("cpf")) {
            desc = desc.toLowerCase().toUpperCase() + "%";
            textQuery = " SELECT MC                                                  "
                    + "     FROM MatriculaConvenioMedico AS MC                       "
                    + "    WHERE UPPER(MC.servicoPessoa.pessoa.documento) LIKE :desc " + queryWhere
                    + " ORDER BY MC.servicoPessoa.pessoa.nome ";
        }
        try {
            Query qry = getEntityManager().createQuery(textQuery);
            if (!desc.equals("%%") && !desc.equals("%")) {
                qry.setParameter("desc", desc);
            }
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
