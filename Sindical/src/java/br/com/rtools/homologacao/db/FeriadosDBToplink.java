package br.com.rtools.homologacao.db;

import br.com.rtools.homologacao.Feriados;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.DataHoje;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class FeriadosDBToplink extends DB implements FeriadosDB {

    @Override
    public List pesquisaTodos() {
        try{
            Query qry = getEntityManager().createQuery(" SELECT F FROM Feriados AS F ORDER BY F.dtData DESC, F.nome ASC, F.cidade.cidade ASC ");
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        }
        catch(Exception e){
        }
        return new ArrayList();
    }

    @Override
    public Feriados pesquisaCodigo(int id) {
        Feriados result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("Feriados.pesquisaID");
            qry.setParameter("pid", id);
            result = (Feriados) qry.getSingleResult();
        }
        catch(Exception e){
        }
        return result;
    }

    @Override
    public List pesquisarPorData(String data){
        try{
            Query qry = getEntityManager().createQuery("SELECT F from Feriados AS F WHERE F.dtData = :data");
            qry.setParameter("data", DataHoje.converte(data));
            return (qry.getResultList());
        }catch(Exception e){
            return new ArrayList();
        }
    }
    
    @Override
    public boolean exiteFeriadoCidade(Feriados feriados){
        String queryCidade = "";
        if (feriados.getCidade() != null) {
            if (feriados.getCidade().getId() != -1) {
                queryCidade = " AND F.cidade.id = " + feriados.getCidade().getId();
            }
        } else {
            queryCidade = " AND F.cidade IS NULL ";
        }
        try{
            Query qry = getEntityManager().createQuery(" SELECT F FROM Feriados AS F WHERE F.dtData = :data " + queryCidade);
            qry.setParameter("data", DataHoje.converte(feriados.getData()));
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        }catch(Exception e){
        }
        return false;
    }

    @Override
    public List pesquisarPorDataFilial(String data, Filial filial){
        try{
            Query qry = getEntityManager().createQuery("SELECT F FROM Feriados AS F" +
                                                       " WHERE F.dtData = :data" +
                                                       "   AND F.filial.id = :idFilial");
            qry.setParameter("data", DataHoje.converte(data));
            qry.setParameter("idFilial", filial.getId());
            return (qry.getResultList());
        }catch(Exception e){
            return new ArrayList();
        }
    }

}
