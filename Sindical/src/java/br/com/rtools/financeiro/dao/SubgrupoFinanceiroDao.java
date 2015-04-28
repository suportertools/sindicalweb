package br.com.rtools.financeiro.dao;

import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class SubgrupoFinanceiroDao extends DB {

    public List listaSubgrupoFinanceiroPorRotina(Integer rotina) {
        try {
            Query query = getEntityManager().createQuery("SELECT SGF FROM SubGrupoFinanceiro AS SGF WHERE SGF.id IN (SELECT SR.servicos.subGrupoFinanceiro.id FROM ServicoRotina AS SR WHERE SR.rotina.id = :rotina)");
            query.setParameter("rotina", rotina);
            return query.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

}
