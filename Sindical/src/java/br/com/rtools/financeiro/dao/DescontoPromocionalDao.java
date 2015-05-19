package br.com.rtools.financeiro.dao;

import br.com.rtools.associativo.Categoria;
import br.com.rtools.financeiro.DescontoPromocional;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.DataHoje;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;

public class DescontoPromocionalDao extends DB {
    public List<Servicos> listaServicosDisponiveis(int idSubGrupoConvenio) {
        try {
            Query qry = getEntityManager().createQuery("SELECT S FROM Servicos AS S WHERE S.situacao = 'A' AND S.subGrupoFinanceiro.id = :pid AND S.id NOT IN (SELECT SR.servicos.id FROM ServicoRotina AS SR WHERE SR.rotina.id = 4 GROUP BY SR.servicos.id) ORDER BY S.descricao ASC ");
            qry.setParameter("pid", idSubGrupoConvenio);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }
    
    /**
     * @param por todos ou naoVencidos
     * @return 
     * 0 - fin_desconto_promocional.id  
     * 1 - fin_servicos.ds_descricao
     * 2 - soc_categoria.ds_categoria
     * 3 - func_valor_servico_idade.valor
     * 4 - fin_desconto_promocional.nr_desconto
     * 5 - fin_desconto_promocional.ds_referencia_inicial
     * 6 - fin_desconto_promocional.ds_referencia_final
     * 7 - fin_servico_valor.nr_idade_ini
     * 8 - fin_servico_valor.nr_idade_fim
     * 
    */
    public List<Vector> listaDescontoPromocional(String por) {
        try {
            String text = "SELECT dp.id, \n" +
                        "       s.ds_descricao, \n" +
                        "       c.ds_categoria, \n" +
                        "       func_valor_servico_idade(sv.nr_idade_ini, s.id, CURRENT_DATE, 0, c.id) AS valor, \n" +
                        "       dp.nr_desconto, \n" +
                        "       dp.ds_referencia_inicial, \n" +
                        "       dp.ds_referencia_final, \n " +
                        "       sv.nr_idade_ini, \n " +
                        "       sv.nr_idade_fim \n " +
                        "  FROM fin_desconto_promocional dp \n " +
                        "  LEFT JOIN soc_categoria c ON c.id = dp.id_categoria \n " +
                        " INNER JOIN fin_servicos s ON s.id = dp.id_servico \n " +
                        " INNER JOIN fin_servico_valor sv ON sv.id_servico = s.id";
            String and = "";
            
            if (por.equals("naoVencidos")){
                and = "  WHERE '"+DataHoje.data().substring(3)+"' BETWEEN dp.ds_referencia_inicial AND dp.ds_referencia_final ";
            }
            Query qry = getEntityManager().createNativeQuery(text + and + " ORDER BY s.ds_descricao, c.ds_categoria ");
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
    
    public List<DescontoPromocional> listaDescontoPromocional(Integer id_servico, Integer id_categoria, String ref_inicial, String ref_final) {
        try {
            String text = 
                    "SELECT ds.* \n " +
                    "  FROM fin_desconto_promocional ds \n " +
                    " WHERE ds.id_servico = " + id_servico + " \n " +
                    "   AND ds.id_categoria " + ((id_categoria == null) ? " IS NULL " : " = " + id_categoria) + " \n ";
            Query qry = getEntityManager().createNativeQuery(text, DescontoPromocional.class);
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
    
    public List<Categoria> listaCategoria(){
        try {
            Query qry = getEntityManager().createQuery(
                    "SELECT c FROM Categoria c ORDER BY c.categoria"
            );
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }        
        return new ArrayList();
    }

}
