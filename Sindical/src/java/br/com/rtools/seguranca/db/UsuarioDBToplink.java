package br.com.rtools.seguranca.db;

import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.principal.DB;
import br.com.rtools.seguranca.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class UsuarioDBToplink extends DB implements UsuarioDB {

    @Override
    public List<Usuario> pesquisaTodosPorDescricao(String descricaoPesquisa) {
        try {
            Query qry = getEntityManager().createQuery("SELECT usu FROM Usuario AS USU WHERE UPPER(USU.pessoa.nome) LIKE '%" + descricaoPesquisa.toUpperCase() + "%' OR UPPER(USU.login) LIKE '%" + descricaoPesquisa.toUpperCase() + "%' ORDER BY USU.pessoa.nome ASC ");
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    @Override
    public Pessoa ValidaUsuarioWeb(String login, String senha) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select pes"
                    + "  from Pessoa pes"
                    + " where pes.login = :log"
                    + "   and pes.senha = :sen");
            qry.setParameter("log", login);
            qry.setParameter("sen", senha);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return (Pessoa) qry.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public Usuario ValidaUsuario(String login, String senha) {
        try {
            Query qry = getEntityManager().createQuery(
                    " SELECT usu"
                    + "   FROM Usuario usu"
                    + "  WHERE usu.login = :log"
                    + "    AND usu.senha = :sen");
            qry.setParameter("log", login);
            qry.setParameter("sen", senha);
            List list = qry.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                Usuario usuario = (Usuario) qry.getSingleResult();
                if (usuario.getAtivo()) {
                    return usuario;
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public Usuario ValidaUsuarioSuporteWeb(String login, String senha) {
        Usuario result = null;
        try {
            Query qry = getEntityManager().createQuery(
                    "select usu"
                    + "  from Usuario usu"
                    + " where usu.login = :log"
                    + "   and usu.senha = :sen");
            qry.setParameter("log", login);
            qry.setParameter("sen", senha);
            result = (Usuario) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    @Override
    public List pesquisaLogin(String login, int idPessoa) {
        List result = new ArrayList();
        String descricao = login.toLowerCase().toUpperCase();
        try {
            Query qry = getEntityManager().createQuery("select usu from Usuario usu where UPPER(usu.login) = :d_usuario"
                    + "    or usu.pessoa.id = :idPessoa");
            qry.setParameter("d_usuario", descricao);
            qry.setParameter("idPessoa", idPessoa);
            result = qry.getResultList();
        } catch (Exception e) {
            return result;
        }
        return result;
    }

    @Override
    public Pessoa ValidaUsuarioContribuinteWeb(int idPessoa) {
        Pessoa result = null;
        try {
            Query qry = getEntityManager().createQuery(
                    "select pes "
                    + "  from Pessoa pes, "
                    + "       Juridica jur, "
                    + "       CnaeConvencao cnaeCon "
                    + " where pes.id = :idPes"
                    + "   and jur.pessoa.id = pes.id "
                    + "   and cnaeCon.cnae.id = jur.cnae.id");
            qry.setParameter("idPes", idPessoa);
            result = (Pessoa) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public Pessoa ValidaUsuarioContabilidadeWeb(int idPessoa) {
        Pessoa result = null;
        try {
            Query qry = getEntityManager().createQuery(
                    "select pes "
                    + "  from Pessoa pes, "
                    + "       Juridica jur "
                    + " where pes.id = :idPes"
                    + "   and jur.pessoa.id = pes.id"
                    + "   and jur.id in (select j.contabilidade.id from Juridica j where j.contabilidade.id is not null)");
            qry.setParameter("idPes", idPessoa);
            result = (Pessoa) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public Pessoa ValidaUsuarioPatronalWeb(int idPessoa) {
        Pessoa result = null;
        try {
            Query qry = getEntityManager().createQuery(
                    "select pes "
                    + "  from Pessoa pes "
                    + " where pes.id in (select patro.pessoa.id from Patronal patro where patro.pessoa.id = :idPes )");
            qry.setParameter("idPes", idPessoa);
            result = (Pessoa) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public Usuario ValidaUsuarioContribuinte(int idUsuario) {
        Usuario result = null;
        try {
            Query qry = getEntityManager().createQuery(
                    "select usu "
                    + "  from Usuario usu, "
                    + "       Pessoa pes, "
                    + "       Juridica jur, "
                    + "       Contribuintes con "
                    + " where usu.id = :idUser "
                    + "   and usu.pessoa.id = pes.id "
                    + "   and jur.pessoa.id = pes.id "
                    + "   and con.juridica.id = jur.id");
            qry.setParameter("idUser", idUsuario);
            result = (Usuario) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public Usuario ValidaUsuarioContabilidade(int idUsuario) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select usu "
                    + "  from Usuario usu, "
                    + "       Pessoa pes, "
                    + "       Juridica jur "
                    + " where usu.id = :idUser "
                    + "   and usu.pessoa.id = pes.id "
                    + "   and jur.pessoa.id = pes.id "
                    + "   and jur.id in ( select j.contabilidade.id "
                    + "                     from Juridica j "
                    + "                    where j.contabilidade.id is not null )");
            qry.setParameter("idUser", idUsuario);
            return (Usuario) qry.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void updateAcordoMovimento() {
        try {
            Query qry = getEntityManager().createNativeQuery("update fin_movimento set nr_ativo=0, nr_acordado=1 where nr_ativo = 1 and id_acordo > 0 and id_tipo_servico <> 4");
            getEntityManager().getTransaction().begin();
            qry.executeUpdate();
            getEntityManager().getTransaction().commit();
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
        }
    }

    @Override
    public Usuario pesquisaUsuarioPorPessoa(int id_pessoa) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select usu from Usuario usu where USU.pessoa.id = " + id_pessoa);
            return (Usuario) qry.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
