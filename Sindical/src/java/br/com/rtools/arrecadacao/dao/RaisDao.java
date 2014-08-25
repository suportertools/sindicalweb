package br.com.rtools.arrecadacao.dao;

import br.com.rtools.arrecadacao.Rais;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.DataHoje;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class RaisDao extends DB {

    public boolean existeCadastroAno(Rais r) {
        try {
            Query q = getEntityManager().createNativeQuery("SELECT id FROM arr_rais WHERE DATE_PART('YEAR', dt_emissao) = DATE_PART('YEAR', CURRENT_TIMESTAMP) AND id_sis_pessoa = " + r.getSisPessoa().getId());
            if (!q.getResultList().isEmpty()) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    public List<Rais> pesquisa(String descricaoPesquisa, String tipoPesquisa, String comoPesquisa) {

        String filtroString = "";
        if (tipoPesquisa.equals("nome")) {
            filtroString = " WHERE UPPER(R.sisPessoa.nome) LIKE :descricaoPesquisa ";
        } else if (tipoPesquisa.equals("cpf")) {
            filtroString = " WHERE R.sisPessoa.documento LIKE :descricaoPesquisa ";
        } else if (tipoPesquisa.equals("empresa")) {
            filtroString = " WHERE UPPER(R.empresa.pessoa.nome) LIKE :descricaoPesquisa ";
        } else if (tipoPesquisa.equals("cnpj")) {
            filtroString = " WHERE R.empresa.pessoa.documento LIKE :descricaoPesquisa ";
        } else if (tipoPesquisa.equals("data")) {
            filtroString = " WHERE R.emissao = '" + DataHoje.livre(DataHoje.converte(descricaoPesquisa), "yyyy-MM-dd") + "'";
        } else if (tipoPesquisa.equals("profissao")) {
            filtroString = " WHERE UPPER(R.profissao.profissao) LIKE :descricaoPesquisa ";
        } else if (tipoPesquisa.equals("todos")) {
            DataHoje dh = new DataHoje();
            filtroString = " WHERE R.emissao >= '" + DataHoje.data() + "' ";
        }
        String queryString = " SELECT R FROM Rais AS R " + (filtroString) + " ORDER BY R.emissao DESC ";
        try {
            Query qry = getEntityManager().createQuery(queryString);
            if (!descricaoPesquisa.equals("") && !tipoPesquisa.equals("todos") && !tipoPesquisa.equals("data")) {
                if (comoPesquisa.equals("Inicial")) {
                    qry.setParameter("descricaoPesquisa", "" + descricaoPesquisa.toUpperCase() + "%");
                } else if (comoPesquisa.equals("Parcial")) {
                    qry.setParameter("descricaoPesquisa", "%" + descricaoPesquisa.toUpperCase() + "%");
                }
            }
            qry.setMaxResults(150);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }
}
