package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.MotivoInativacao;
import br.com.rtools.principal.DB;
import java.util.List;
import javax.persistence.Query;

public class MotivoInativacaoDBToplink extends DB implements MotivoInativacaoDB {

    @Override
    public boolean insert(MotivoInativacao motivoInativacao) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(motivoInativacao);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(MotivoInativacao motivoInativacao) {
        try {
            getEntityManager().merge(motivoInativacao);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(MotivoInativacao motivoInativacao) {
        try {
            getEntityManager().remove(motivoInativacao);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public MotivoInativacao pesquisaCodigo(int id) {
        MotivoInativacao result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("MotivoInativacao.pesquisaID");
            qry.setParameter("pid", id);
            result = (MotivoInativacao) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select cont from MotivoInativacao cont ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public MotivoInativacao idMotivoInativacao(MotivoInativacao des_motivoInativacao) {
        MotivoInativacao result = null;
        String descricao = des_motivoInativacao.getDescricao().toLowerCase().toUpperCase();
        try {
            Query qry = getEntityManager().createQuery("select mot from MotivoInativacao mot where UPPER(mot.descricao) = :d_motivoInativacao");
            qry.setParameter("d_motivoInativacao", descricao);
            result = (MotivoInativacao) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }
}
