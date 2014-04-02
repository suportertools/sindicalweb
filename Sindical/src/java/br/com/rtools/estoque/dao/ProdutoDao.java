package br.com.rtools.estoque.dao;

import br.com.rtools.estoque.Estoque;
import br.com.rtools.estoque.Produto;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class ProdutoDao extends DB {

    public List pesquisaProduto(Produto produto, int tipoPesquisa, String porPesquisa) {
        if (produto.getDescricao().isEmpty()) {
            return new ArrayList();
        }
        try {
            String por;
            if (porPesquisa.equals("Inicial")) {
                por = "";
            } else {
                por = "%";
            }
            Query q = getEntityManager().createQuery("SELECT P FROM Produto AS P WHERE UPPER(P.descricao) LIKE :p");
            if (tipoPesquisa == 0) {
                q.setParameter("p", por + produto.getDescricao().toUpperCase() + "%");
            }
            List list = q.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public boolean existeProdutoGrupo(String p) {
        try {
            Query q = getEntityManager().createQuery("SELECT P FROM ProdutoGrupo AS P WHERE UPPER(P.descricao) LIKE :p");
            q.setParameter("p", p.toUpperCase());
            if (!q.getResultList().isEmpty()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public boolean existeProdutoSubGrupo(String p) {
        try {
            Query q = getEntityManager().createQuery("SELECT P FROM ProdutoSubGrupo AS P WHERE UPPER(P.descricao) LIKE :p");
            q.setParameter("p", p.toUpperCase());
            if (!q.getResultList().isEmpty()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public boolean existeProdutoUnidade(String p) {
        try {
            Query q = getEntityManager().createQuery("SELECT P FROM ProdutoUnidade AS P WHERE UPPER(P.descricao) LIKE :p");
            q.setParameter("p", p.toUpperCase());
            if (!q.getResultList().isEmpty()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public boolean existeCor(String p) {
        try {
            Query q = getEntityManager().createQuery("SELECT C FROM Cor AS C WHERE UPPER(C.descricao) LIKE :p");
            q.setParameter("p", p.toUpperCase());
            if (!q.getResultList().isEmpty()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public boolean existeProdutoEstoqueFilialTipo(Estoque es) {
        try {
            Query q = getEntityManager().createQuery("SELECT EST FROM Estoque AS EST WHERE EST.produto.id = :p1 AND EST.filial.id = :p2 AND EST.estoqueTipo.id = :p3");
            q.setParameter("p1", es.getProduto().getId());
            q.setParameter("p2", es.getFilial().getId());
            q.setParameter("p3", es.getEstoqueTipo().getId());
            if (!q.getResultList().isEmpty()) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    public List listaEstoquePorProduto(Produto p) {
        try {
            Query q = getEntityManager().createQuery("SELECT E FROM Estoque AS E WHERE E.produto.id = :p1 ORDER BY E.filial.filial.pessoa.nome ASC, E.estoqueTipo.descricao ASC");
            q.setParameter("p1", p.getId());
            List list = q.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

}
