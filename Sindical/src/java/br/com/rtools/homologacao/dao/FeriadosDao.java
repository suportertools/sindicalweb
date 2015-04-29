package br.com.rtools.homologacao.dao;

import br.com.rtools.homologacao.Feriados;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.DataHoje;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class FeriadosDao extends DB {

    public List pesquisarPorData(String data) {
        try {
            Query qry = getEntityManager().createQuery("SELECT F FROM Feriados AS F WHERE F.dtData = :data");
            qry.setParameter("data", DataHoje.converte(data));
            return (qry.getResultList());
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    public boolean exiteFeriadoCidade(Feriados feriados) {
        String queryCidade = "";
        if (feriados.getCidade() != null) {
            if (feriados.getCidade().getId() != -1) {
                queryCidade = " AND F.cidade.id = " + feriados.getCidade().getId();
            }
        } else {
            queryCidade = " AND F.cidade IS NULL ";
        }
        try {
            String queryString = " SELECT F FROM Feriados AS F WHERE F.dtData = :data " + queryCidade;
            Query qry = getEntityManager().createQuery(queryString);
            qry.setParameter("data", DataHoje.converte(feriados.getData()));
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public List pesquisarPorDataFilial(String data, Filial filial) {
        try {
            Query queryFilial = getEntityManager().createQuery("SELECT PE FROM PessoaEndereco AS PE WHERE PE.pessoa.id = :idPessoa");
            queryFilial.setParameter("idPessoa", filial.getFilial().getPessoa().getId());
            queryFilial.setMaxResults(1);
            List listFilial = queryFilial.getResultList();
            List list;
            if (!listFilial.isEmpty()) {
                PessoaEndereco pe = (PessoaEndereco) queryFilial.getSingleResult();
                Query qry = getEntityManager().createQuery("SELECT F FROM Feriados AS F WHERE F.dtData = :data AND F.cidade.id = :idCidade");
                qry.setParameter("data", DataHoje.converte(data));
                qry.setParameter("idCidade", pe.getEndereco().getCidade().getId());
                list = qry.getResultList();
                if (!list.isEmpty()) {
                    return list;
                }
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public List pesquisarPorDataFilialEData(String data, Filial filial) {
        try {
            Query queryFilial = getEntityManager().createQuery("SELECT PE FROM PessoaEndereco AS PE WHERE PE.pessoa.id = :idPessoa");
            queryFilial.setParameter("idPessoa", filial.getFilial().getPessoa().getId());
            queryFilial.setMaxResults(1);
            List listFilial = queryFilial.getResultList();
            List list;
            if (!listFilial.isEmpty()) {
                PessoaEndereco pe = (PessoaEndereco) queryFilial.getSingleResult();
                Query queryNativa = getEntityManager().createNativeQuery("SELECT id FROM hom_feriados WHERE (dt_data = '" + data + "' AND id_cidade = " + pe.getEndereco().getCidade().getId() + ") AND dt_data = '" + DataHoje.converte(data) + "'");
                list = queryNativa.getResultList();
                if (!list.isEmpty()) {
                    String listaFeriadosString = "";
                    for (int i = 0; i < list.size(); i++) {
                        if (i == 0) {
                            listaFeriadosString = ((List) list.get(i)).get(0).toString();
                        } else {
                            listaFeriadosString = "," + ((List) list.get(i)).get(0).toString();
                        }
                    }
                    Query query = getEntityManager().createQuery("SELECT F FROM Feriados AS F WHERE F.id IN ( :listaFeriadosString )");
                    query.setParameter(":listaFeriadosString", listaFeriadosString);
                    list.clear();
                    list = query.getResultList();
                    if (!list.isEmpty()) {
                        return list;
                    }
                }
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public List pesquisaTodosFeriados() {
        try {
            Query query = getEntityManager().createQuery("SELECT F FROM Feriados AS F ORDER BY F.dtData DESC, F.nome ASC ");
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }
}
