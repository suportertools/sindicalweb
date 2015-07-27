package br.com.rtools.seguranca.db;

import br.com.rtools.principal.DB;
import br.com.rtools.seguranca.*;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class PermissaoUsuarioDao extends DB {

    public List pesquisaListaPermissaoPorUsuario(int idUsuario) {
        try {
            Query qry = getEntityManager().createQuery("select pu "
                    + "  from PermissaoUsuario pu "
                    + " where pu.usuario.id = :idUsuario "
                    + " order by pu.departamento.descricao ");
            qry.setParameter("idUsuario", idUsuario);
            if (!qry.getResultList().isEmpty()) {
                return (qry.getResultList());
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public PermissaoUsuario pesquisaPermissaoUsuario(int idUsuario, int idDepartamento, int idNivel) {
        try {
            Query qry = getEntityManager().createQuery("select pu "
                    + "  from PermissaoUsuario pu "
                    + " where pu.usuario.id = :idUsuario "
                    + "   and pu.departamento.id = :idDepartamento"
                    + "   and pu.nivel.id = :idNivel");
            qry.setParameter("idUsuario", idUsuario);
            qry.setParameter("idDepartamento", idDepartamento);
            qry.setParameter("idNivel", idNivel);
            return (PermissaoUsuario) (qry.getSingleResult());
        } catch (Exception e) {
            return null;
        }
    }

    public PermissaoUsuario pesquisaAcessoPermissao(int idUsuario, int idModulo, int idRotina, int idEvento) {
        try {
            Query qry = getEntityManager().createQuery("select pu "
                    + "  from PermissaoUsuario pu, "
                    + "       PermissaoDepartamento pd,"
                    + "       Permissao p "
                    + "where pu.departamento.id = pd.departamento.id "
                    + "  and pu.nivel.id = pd.nivel.id "
                    + "  and pd.permissao.id = p.id"
                    + "  and pu.usuario.id = :idUsuario "
                    + "  and p.modulo.id = :idModulo "
                    + "  and p.rotina.id = :idRotina "
                    + "  and p.evento.id = :idEvento");
            qry.setParameter("idUsuario", idUsuario);
            qry.setParameter("idModulo", idModulo);
            qry.setParameter("idRotina", idRotina);
            qry.setParameter("idEvento", idEvento);
            return (PermissaoUsuario) (qry.getSingleResult());
        } catch (Exception e) {
            return null;
        }
    }

    public PermissaoUsuario pesquisaAcessoPermissaoSM(int idUsuario, int idRotina, int idEvento) {
        try {
            Query qry = getEntityManager().createQuery("select pu "
                    + "  from PermissaoUsuario pu, "
                    + "       PermissaoDepartamento pd,"
                    + "       Permissao p "
                    + "where pu.departamento.id = pd.departamento.id "
                    + "  and pu.nivel.id = pd.nivel.id "
                    + "  and pd.permissao.id = p.id"
                    + "  and pu.usuario.id = :idUsuario "
                    + "  and p.rotina.id = :idRotina "
                    + "  and p.evento.id = :idEvento");
            qry.setParameter("idUsuario", idUsuario);
            qry.setParameter("idRotina", idRotina);
            qry.setParameter("idEvento", idEvento);
            return (PermissaoUsuario) (qry.getSingleResult());
        } catch (Exception e) {
            return null;
        }
    }

    public Permissao pesquisaPermissao(int id_modulo, int id_rotina, int id_evento) {
        Permissao permissao = new Permissao();
        try {
            Query qry = getEntityManager().createQuery("    "
                    + " SELECT p                            "
                    + "   FROM Permissao p                  "
                    + "  WHERE p.modulo.id = " + id_modulo
                    + "    AND p.rotina.id = " + id_rotina
                    + "    AND p.rotina.ativo = true        "
                    + "    AND p.evento.id = " + id_evento);
            if (!qry.getResultList().isEmpty()) {
                return (Permissao) qry.getSingleResult();
            }
        } catch (Exception e) {
        }
        return permissao;
    }

    public PermissaoDepartamento pesquisaPermissaoDepartamento(int id_departamento, int id_nivel, int id_permissao) {
        PermissaoDepartamento permissaoDepartamento = new PermissaoDepartamento();
        try {
            Query qry = getEntityManager().createQuery("select pd "
                    + " from PermissaoDepartamento pd "
                    + "where pd.departamento.id = " + id_departamento
                    + "  and pd.nivel.id = " + id_nivel
                    + "  and pd.permissao.id = " + id_permissao);
            if (!qry.getResultList().isEmpty()) {
                return (PermissaoDepartamento) qry.getSingleResult();
            }
        } catch (Exception e) {
            return permissaoDepartamento;
        }
        return permissaoDepartamento;
    }

    public List<PermissaoDepartamento> pesquisaPDepartamento(int id_departamento, int id_nivel) {
        try {
            Query qry = getEntityManager().createQuery("select pd "
                    + " from PermissaoDepartamento pd "
                    + "where pd.departamento.id = " + id_departamento
                    + "  and pd.nivel.id = " + id_nivel);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    public List<PermissaoUsuario> pesquisaPermissaoUser(int id_depto, int id_nivel) {
        try {
            Query qry = getEntityManager().createQuery("select pu from PermissaoUsuario pu where pu.departamento.id = " + id_depto + " and pu.nivel.id = " + id_nivel);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    public List<PermissaoUsuario> listaPermissaoUsuario(int idUsuario) {
        List list = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("select pu "
                    + "  from PermissaoUsuario pu "
                    + " where pu.usuario.id = :idUsuario "
                    + " order by pu.departamento.descricao ");
            qry.setParameter("idUsuario", idUsuario);
            if (!qry.getResultList().isEmpty()) {
                return qry.getResultList();
            }
        } catch (Exception e) {
        }
        return list;
    }

    public boolean existePermissaoUsuario(PermissaoUsuario permissaoUsuario) {
        try {
            Query qry = getEntityManager().createQuery(" SELECT PU FROM PermissaoUsuario AS PU WHERE PU.usuario.id = :idUsuario AND PU.departamento.id = :idDepartamento AND PU.nivel.id = :idNivel");
            qry.setParameter("idUsuario", permissaoUsuario.getUsuario().getId());
            qry.setParameter("idDepartamento", permissaoUsuario.getDepartamento().getId());
            qry.setParameter("idNivel", permissaoUsuario.getNivel().getId());
            if (!qry.getResultList().isEmpty()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public List<PermissaoUsuario> listaPermissaoUsuarioDepartamento(int id_departamento) {
        try {
            Query qry = getEntityManager().createQuery(
                    " SELECT pu "
                    + "   FROM PermissaoUsuario pu "
                    + "  WHERE pu.departamento.id = :pid "
                    + "  ORDER BY pu.usuario.pessoa.nome"
            );

            qry.setParameter("pid", id_departamento);

            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
}
