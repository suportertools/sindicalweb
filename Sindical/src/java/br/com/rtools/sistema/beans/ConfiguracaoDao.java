package br.com.rtools.sistema.beans;

import br.com.rtools.principal.DB;
import br.com.rtools.sistema.Configuracao;
import br.com.rtools.sistema.Resolucao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class ConfiguracaoDao extends DB {

    public boolean existeIdentificador(Configuracao configuracao) {
        try {
            Query query = getEntityManager().createQuery(" SELECT C FROM Configuracao AS C WHERE C.identifica = :identificador ");
            query.setParameter("identificador", configuracao.getIdentifica());
            if (!query.getResultList().isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public boolean existeIdentificadorPessoa(Configuracao configuracao) {
        try {
            Query query = getEntityManager().createQuery(" SELECT C FROM Configuracao AS C WHERE C.identifica = :identificador AND C.juridica.id = :idJuridica ");
            query.setParameter("identificador", configuracao.getIdentifica());
            query.setParameter("idJuridica", configuracao.getJuridica().getId());
            if (!query.getResultList().isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public List listaConfiguracao(String descricaoPesquisa) {
        try {
            Query query = getEntityManager().createQuery(" SELECT C FROM Configuracao AS C WHERE C.juridica.fantasia LIKE '%" + descricaoPesquisa + "%' OR C.identifica LIKE '%" + descricaoPesquisa + "%' OR C.juridica.pessoa.nome LIKE '%" + descricaoPesquisa + "%' ");
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public Resolucao pesquisaResolucaoUsuario(int id_usuario) {
        try {
            Query query = getEntityManager().createQuery(" SELECT r FROM Resolucao r WHERE r.usuario.id  = :pid");
            query.setParameter("pid", id_usuario);
            Resolucao result = (Resolucao) query.getSingleResult();

            return result;
        } catch (Exception e) {
            return new Resolucao();
        }
    }
}
