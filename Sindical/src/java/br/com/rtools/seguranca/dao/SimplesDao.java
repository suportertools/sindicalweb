package br.com.rtools.seguranca.dao;

import br.com.rtools.principal.DB;

public class SimplesDao extends DB {

//    @Override
//    public List pesquisaTodos() {
//        try {
//            Query qry = getEntityManager().createQuery("select usu from Usuario usu ");
//            return (qry.getResultList());
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    @Override
//    public Pessoa ValidaUsuarioWeb(String login, String senha) {
//        Pessoa result = null;
//        try {
//            Query qry = getEntityManager().createQuery(
//                    "select pes"
//                    + "  from Pessoa pes"
//                    + " where pes.login = :log"
//                    + "   and pes.senha = :sen");
//            qry.setParameter("log", login);
//            qry.setParameter("sen", senha);
//            result = (Pessoa) qry.getSingleResult();
//        } catch (Exception e) {
//        }
//        return result;
//    }
//
//    public Usuario ValidaUsuario(String login, String senha) {
//        Usuario result = null;
//        try {
//            Query qry = getEntityManager().createQuery(
//                    "select usu"
//                    + "  from Usuario usu"
//                    + " where usu.login = :log"
//                    + "   and usu.senha = :sen");
//            qry.setParameter("log", login);
//            qry.setParameter("sen", senha);
//            result = (Usuario) qry.getSingleResult();
//        } catch (Exception e) {
//        }
//        return result;
//    }
//
//    @Override
//    public Usuario ValidaUsuarioSuporteWeb(String login, String senha) {
//        Usuario result = null;
//        try {
//            Query qry = getEntityManager().createQuery(
//                    "select usu"
//                    + "  from Usuario usu"
//                    + " where usu.login = :log"
//                    + "   and usu.senha = :sen");
//            qry.setParameter("log", login);
//            qry.setParameter("sen", senha);
//            result = (Usuario) qry.getSingleResult();
//        } catch (Exception e) {
//            e.getMessage();
//        }
//        return result;
//    }
//
//    @Override
//    public Usuario pesquisaLogin(String login, int idPessoa) {
//        Usuario result = null;
//        String descricao = login.toLowerCase().toUpperCase();
//        try {
//            Query qry = getEntityManager().createQuery("select usu from Usuario usu where UPPER(usu.login) = :d_usuario"
//                    + "    or usu.pessoa.id = :idPessoa");
//            qry.setParameter("d_usuario", descricao);
//            qry.setParameter("idPessoa", idPessoa);
//            result = (Usuario) qry.getSingleResult();
//        } catch (Exception e) {
//            return new Usuario();
//        }
//        return result;
//    }
//
//    @Override
//    public Pessoa ValidaUsuarioContribuinteWeb(int idPessoa) {
//        Pessoa result = null;
//        try {
//            Query qry = getEntityManager().createQuery(
//                    "select pes "
//                    + "  from Pessoa pes, "
//                    + "       Juridica jur, "
//                    + "       CnaeConvencao cnaeCon "
//                    + " where pes.id = :idPes"
//                    + "   and jur.pessoa.id = pes.id "
//                    + "   and cnaeCon.cnae.id = jur.cnae.id");
//            qry.setParameter("idPes", idPessoa);
//            result = (Pessoa) qry.getSingleResult();
//        } catch (Exception e) {
//        }
//        return result;
//    }
//
//    @Override
//    public Pessoa ValidaUsuarioContabilidadeWeb(int idPessoa) {
//        Pessoa result = null;
//        try {
//            Query qry = getEntityManager().createQuery(
//                    "select pes "
//                    + "  from Pessoa pes, "
//                    + "       Juridica jur "
//                    + " where pes.id = :idPes"
//                    + "   and jur.pessoa.id = pes.id"
//                    + "   and jur.id in (select j.contabilidade.id from Juridica j where j.contabilidade.id is not null)");
//            qry.setParameter("idPes", idPessoa);
//            result = (Pessoa) qry.getSingleResult();
//        } catch (Exception e) {
//        }
//        return result;
//    }
//
//    @Override
//    public Usuario ValidaUsuarioContribuinte(int idUsuario) {
//        Usuario result = null;
//        try {
//            Query qry = getEntityManager().createQuery(
//                    "select usu "
//                    + "  from Usuario usu, "
//                    + "       Pessoa pes, "
//                    + "       Juridica jur, "
//                    + "       Contribuintes con "
//                    + " where usu.id = :idUser "
//                    + "   and usu.pessoa.id = pes.id "
//                    + "   and jur.pessoa.id = pes.id "
//                    + "   and con.juridica.id = jur.id");
//            qry.setParameter("idUser", idUsuario);
//            result = (Usuario) qry.getSingleResult();
//        } catch (Exception e) {
//        }
//        return result;
//    }
//
//    @Override
//    public Usuario ValidaUsuarioContabilidade(int idUsuario) {
//        try {
//            Query qry = getEntityManager().createQuery(
//                    "select usu "
//                    + "  from Usuario usu, "
//                    + "       Pessoa pes, "
//                    + "       Juridica jur "
//                    + " where usu.id = :idUser "
//                    + "   and usu.pessoa.id = pes.id "
//                    + "   and jur.pessoa.id = pes.id "
//                    + "   and jur.id in ( select j.contabilidade.id "
//                    + "                     from Juridica j "
//                    + "                    where j.contabilidade.id is not null )");
//            qry.setParameter("idUser", idUsuario);
//            return (Usuario) qry.getSingleResult();
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    @Override
//    public void updateAcordoMovimento() {
//        try {
//            Query qry = getEntityManager().createNativeQuery("update fin_movimento set nr_ativo=0, nr_acordado=1 where nr_ativo = 1 and id_acordo > 0 and id_tipo_servico <> 4");
//            getEntityManager().getTransaction().begin();
//            qry.executeUpdate();
//            getEntityManager().getTransaction().commit();
//        } catch (Exception e) {
//            getEntityManager().getTransaction().rollback();
//        }
//    }
}
