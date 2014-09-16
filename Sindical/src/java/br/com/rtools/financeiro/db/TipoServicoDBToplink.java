package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.TipoServico;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;

public class TipoServicoDBToplink extends DB implements TipoServicoDB {

    @Override
    public boolean insert(TipoServico tipoServico) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(tipoServico);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(TipoServico tipoServico) {
        try {
            getEntityManager().merge(tipoServico);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(TipoServico tipoServico) {
        try {
            getEntityManager().remove(tipoServico);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public TipoServico pesquisaCodigo(int id) {
        TipoServico result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("TipoServico.pesquisaID");
            qry.setParameter("pid", id);
            result = (TipoServico) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select p from TipoServico p ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List pesquisaTodosPeloContaCobranca() {
        try {
            Query qry = getEntityManager().createQuery(
                    "select t "
                    + "  from TipoServico t"
                    + " where t.id in (select s.tipoServico.id from ServicoContaCobranca s)");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<TipoServico> pesquisaTodosComIds(List<Integer> arrayId) {
        try {
            int i = 0;
            if (arrayId == null) {
                return null;
            }
            if (arrayId.isEmpty()) {
                return new ArrayList();
            }
            String valores = "( ";
            while (i < arrayId.size()) {
                valores += arrayId.get(i);
                if (i + 1 < arrayId.size()) {
                    valores += ", ";
                }
                i++;
            }
            valores += " )";
            Query qry = getEntityManager().createQuery(
                    "select ts "
                    + "  from TipoServico ts "
                    + " where ts.id in " + valores);
            return (qry.getResultList());
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public TipoServico idTipoServico(TipoServico des_tipoServico) {
        TipoServico result = null;
        String descricao = des_tipoServico.getDescricao().toLowerCase().toUpperCase();
        try {
            Query qry = getEntityManager().createQuery("select tipos from TipoServico tipos where UPPER(tipos.descricao) = :d_tipoServico");
            qry.setParameter("d_tipoServico", descricao);
            result = (TipoServico) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaTipoServico(String desc, String como) {
        List lista = new Vector<Object>();
        String textQuery = null;
        if (como.equals("P")) {

            desc = "%" + desc.toLowerCase().toUpperCase() + "%";
            textQuery = "select ts from TipoServico ts    "
                    + " where UPPER(ts.descricao) like :desc";
        } else if (como.equals("I")) {
            desc = desc.toLowerCase().toUpperCase() + "%";
            textQuery = "select ts from TipoServico ts    "
                    + " where UPPER(ts.descricao) like :desc";
        }

        try {
            Query qry = getEntityManager().createQuery(textQuery);
            if (!desc.equals("%%") && !desc.equals("%")) {
                qry.setParameter("desc", desc);
            }
            lista = qry.getResultList();
        } catch (Exception e) {
            lista = new Vector<Object>();
        }
        return lista;
    }
}
