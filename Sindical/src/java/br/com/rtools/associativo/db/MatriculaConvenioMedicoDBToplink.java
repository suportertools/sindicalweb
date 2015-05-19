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
    
    @Override
    public List<MatriculaConvenioMedico> listaConvenioPessoa(int id_pessoa, int id_servico){
        String text = "SELECT mm.* \n" +
                      "  FROM matr_convenio_medico mm \n " +
                      " INNER JOIN fin_servico_pessoa sp ON sp.id = mm.id_servico_pessoa \n " +
                      " INNER JOIN fin_servicos s ON s.id = sp.id_servico \n " +
                      " WHERE sp.id_pessoa = "+id_pessoa+" \n " +
                      "   AND mm.dt_inativo is null \n " +
                      "   AND sp.id_servico = "+id_servico;
        
        Query qry = getEntityManager().createNativeQuery(text, MatriculaConvenioMedico.class);
        
        try {
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
            return new ArrayList();
        }
    }
}
