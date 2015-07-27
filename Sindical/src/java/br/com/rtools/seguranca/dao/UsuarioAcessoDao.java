package br.com.rtools.seguranca.dao;

import br.com.rtools.principal.DB;
import br.com.rtools.seguranca.UsuarioAcesso;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class UsuarioAcessoDao extends DB {

    public List<UsuarioAcesso> pesquisaAcesso(Integer permissao_id) {
        try {
            Query qry = getEntityManager().createQuery("SELECT UA FROM UsuarioAcesso UA WHERE UA.permissao.id = " + permissao_id);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    public UsuarioAcesso pesquisaUsuarioAcesso(Integer usuario_id, Integer permissao_id) {
        UsuarioAcesso usuarioAcesso = new UsuarioAcesso();
        try {
            Query qry = getEntityManager().createQuery("SELECT UA FROM UsuarioAcesso UA WHERE UA.usuario.id = " + usuario_id + " AND UA.permissao.id = " + permissao_id);
            if (!qry.getResultList().isEmpty()) {
                return (UsuarioAcesso) qry.getSingleResult();
            }
        } catch (Exception e) {
        }
        return usuarioAcesso;
    }

    public UsuarioAcesso pesquisaUsuarioAcessoModuloRotinaEvento(int idUsuario, int idModulo, int idRotina, int idEvento) {
        UsuarioAcesso usuarioAcesso = new UsuarioAcesso();
        try {
            Query qry = getEntityManager().createQuery(
                    " SELECT ua                                   "
                    + "   FROM UsuarioAcesso ua                     "
                    + "  WHERE ua.permissao.modulo.id = :idModulo   "
                    + "    AND ua.permissao.rotina.id = :idRotina   "
                    + "    AND ua.permissao.evento.id = :idEvento   "
                    + "    AND ua.usuario.id = :idUsuario           ");
            qry.setParameter("idModulo", idModulo);
            qry.setParameter("idRotina", idRotina);
            qry.setParameter("idEvento", idEvento);
            qry.setParameter("idUsuario", idUsuario);
            if (!qry.getResultList().isEmpty()) {
                usuarioAcesso = (UsuarioAcesso) qry.getSingleResult();
            }
        } catch (Exception e) {
        }
        return usuarioAcesso;
    }
}
