package br.com.rtools.associativo.db;

import br.com.rtools.associativo.MatriculaSocios;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class MatriculaSociosDBToplink extends DB implements MatriculaSociosDB {

    @Override
    public MatriculaSocios pesquisaPorNrMatricula(int idGpCategoria, int nrMatricula) {
        MatriculaSocios result = null;
        try {
            Query qry = getEntityManager().createQuery("select m from MatriculaSocios s"
                    + " where m.grupoCategoria.id = " + idGpCategoria
                    + "   and m.nrMatricula = " + nrMatricula);
            result = (MatriculaSocios) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    @Override
    public List listaMatriculaPorGrupoNrMatricula(int grupoCategoria, int nrMatricula) {
        try {
            String queryString = ""
                    + "        SELECT M.nr_matricula                                      "
                    + "          FROM matr_socios AS M                                    "
                    + "    INNER JOIN soc_categoria AS C ON C.id = M.id_categoria         "
                    + "         WHERE M.nr_matricula = " + nrMatricula + "                    "
                    + "           AND C.id_grupo_categoria = " + grupoCategoria;
            Query qry = getEntityManager().createNativeQuery(queryString);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }
}
