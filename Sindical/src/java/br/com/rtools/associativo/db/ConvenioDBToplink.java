package br.com.rtools.associativo.db;

import br.com.rtools.associativo.Convenio;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class ConvenioDBToplink extends DB implements ConvenioDB {

    @Override
    public List listaTodos(boolean orderPessoa, boolean orderGrupoConvenio, boolean orderSubGrupoConvenio) {
        return listaTodosPorPessoa(orderPessoa, orderGrupoConvenio, orderSubGrupoConvenio, null);
    }
    
    @Override
    public List listaTodosPorPessoa(boolean orderPessoa, boolean orderGrupoConvenio, boolean orderSubGrupoConvenio, Convenio convenio) {
        String where = "";
        String order_by = "";
        
        if (orderPessoa) {
            order_by = " ORDER BY c.juridica.pessoa.nome ";
        }
        
        if (orderGrupoConvenio){
            if (order_by.isEmpty()) {
                order_by = " ORDER BY c.subGrupoConvenio.grupoConvenio.descricao ";
            }else{
                order_by += ", c.subGrupoConvenio.grupoConvenio.descricao ";
            }
        }
        
        if (orderSubGrupoConvenio){
            if (order_by.isEmpty()) {
                order_by = " ORDER BY c.subGrupoConvenio.descricao ";
            } else {
                order_by += ", c.subGrupoConvenio.descricao ";
            }
        }
        
        if (convenio != null) {
          where = " WHERE C.juridica.id = "+convenio.getJuridica().getId();  
        }
        try {
            Query query = getEntityManager().createQuery(
                        " SELECT c " +
                        "   FROM Convenio c " +
                            where +
                            order_by);    
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }
    
    public boolean existeSubGrupoEmpresa(Convenio convenio) {
        try {
            Query query = getEntityManager().createQuery(" SELECT C FROM Convenio AS C WHERE C.juridica.id = :idJuridica AND C.subGrupoConvenio.id = :idSubGrupoConvenio");
            query.setParameter("idJuridica", convenio.getJuridica().getId());
            query.setParameter("idSubGrupoConvenio", convenio.getSubGrupoConvenio().getId());
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    @Override
    public List pesquisaConvenioPorGrupoPessoa(int idPessoaJuridica, int idGrupoConvenio) {
        List lista = null;
        try {
            Query qry = getEntityManager().createQuery(
                    "select c"
                    + "  from Convenio c"
                    + " where c.juridica.pessoa.id = :idPessoa"
                    + "   and c.grupoConvenio.id = :idGrupo");
            qry.setParameter("idPessoa", idPessoaJuridica);
            qry.setParameter("idGrupo", idGrupoConvenio);
            lista = qry.getResultList();
            if (lista == null) {
                lista = new ArrayList();
            }
            return lista;
        } catch (Exception e) {
            e.getMessage();
            return new ArrayList();
        }
    }

    @Override
    public List pesquisaJuridicaPorGrupoESubGrupo(int idSubGrupoConvenio, int idGrupo) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select c.juridica"
                    + "  from Convenio c"
                    + " where c.subGrupoConvenio.id = " + idSubGrupoConvenio
                    + "   and c.subGrupoConvenio.grupoConvenio.id = " + idGrupo);
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
            return new ArrayList();
        }
    }
}
