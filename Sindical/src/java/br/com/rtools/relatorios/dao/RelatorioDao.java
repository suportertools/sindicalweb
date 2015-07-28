package br.com.rtools.relatorios.dao;

import br.com.rtools.principal.DB;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.seguranca.Rotina;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class RelatorioDao extends DB {

    public List findByRotina(Integer rotina_id) {
        try {
            Query query = getEntityManager().createQuery("SELECT R FROM Relatorios AS R WHERE R.rotina.id = :rotina_id ORDER BY R.nome");
            query.setParameter("rotina_id", rotina_id);
            return query.getResultList();
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public Relatorios findByJasper(String relatorio_jasper) {
        try {
            Query query = getEntityManager().createQuery("SELECT R FROM Relatorios AS R WHERE R.jasper = :relatorio_jasper");
            query.setParameter("relatorio_jasper", relatorio_jasper);
            return (Relatorios) query.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }

    public Boolean defineDefault(Relatorios r) {
        if (r.getId() == -1) {
            return false;
        }
        try {
            getEntityManager().getTransaction().begin();
            Query query = getEntityManager().createNativeQuery("UPDATE sis_relatorios SET is_default = false WHERE id_rotina = " + r.getRotina().getId());
            if (query.executeUpdate() == 0) {
                getEntityManager().getTransaction().rollback();
                return false;
            }
            if (r.getPrincipal()) {
                query = getEntityManager().createNativeQuery("UPDATE sis_relatorios SET is_default = true WHERE id = " + r.getId());
                if (query.executeUpdate() == 0) {
                    getEntityManager().getTransaction().rollback();
                    return false;
                }
            }
            getEntityManager().getTransaction().commit();
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
        return true;
    }

    public List<Relatorios> pesquisaTipoRelatorio(int idRotina) {
        List<Relatorios> result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("select rel "
                    + "  from Relatorios rel"
                    + " where rel.rotina.id = :idRotina"
                    + " order by rel.nome");
            qry.setParameter("idRotina", idRotina);
            result = qry.getResultList();
        } catch (Exception e) {
            return result;
        }
        return result;
    }

    public List pesquisaCidadesRelatorio() {
        List result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("select pes.endereco.cidade "
                    + "  from PessoaEndereco pes"
                    + " where pes.tipoEndereco.id = 2"
                    + " group by pes.endereco.cidade"
                    + " order by pes.endereco.cidade.cidade");
            result = qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }

    public Relatorios pesquisaRelatorios(int idRelatorio) {
        Relatorios result = null;
        try {
            Query qry = getEntityManager().createQuery("select rel "
                    + "  from Relatorios rel"
                    + " where rel.id = :idRelatorio");
            qry.setParameter("idRelatorio", idRelatorio);
            result = (Relatorios) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    public Relatorios pesquisaRelatoriosPorJasper(String dsJasper) {
        Relatorios result = null;
        try {
            Query qry = getEntityManager().createQuery("select rel "
                    + "  from Relatorios rel"
                    + " where rel.jasper = :jasper");
            qry.setParameter("jasper", dsJasper);
            result = (Relatorios) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    public List<Rotina> pesquisaRotina() {
        List<Rotina> lista = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("select rot from Rotina rot where rot.rotina like 'RELATÓRIO%'");
            lista = qry.getResultList();
        } catch (Exception e) {
        }
        return lista;
    }

    public List<Relatorios> pesquisaTodosRelatorios() {
        List<Relatorios> lista = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("SELECT R FROM Relatorios AS R ORDER BY R.nome ASC ");
            lista = qry.getResultList();
        } catch (Exception e) {
        }
        return lista;
    }
}
