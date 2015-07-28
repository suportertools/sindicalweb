package br.com.rtools.homologacao.db;

import br.com.rtools.principal.DB;

/**
 * Use FeriadosDao
 *
 * @author rtools2
 * @deprecated use FeriadosDao()
 */
@Deprecated
public class FeriadosDBToplink extends DB  {

//    @Override
//    public List pesquisarPorData(String data) {
//        try {
//            Query qry = getEntityManager().createQuery("SELECT F from Feriados AS F WHERE F.dtData = :data");
//            qry.setParameter("data", DataHoje.converte(data));
//            return (qry.getResultList());
//        } catch (Exception e) {
//            return new ArrayList();
//        }
//    }
//
//    @Override
//    public boolean exiteFeriadoCidade(Feriados feriados) {
//        String queryCidade = "";
//        if (feriados.getCidade() != null) {
//            if (feriados.getCidade().getId() != -1) {
//                queryCidade = " AND F.cidade.id = " + feriados.getCidade().getId();
//            }
//        } else {
//            queryCidade = " AND F.cidade IS NULL ";
//        }
//        try {
//            Query qry = getEntityManager().createQuery(" SELECT F FROM Feriados AS F WHERE F.dtData = :data " + queryCidade);
//            qry.setParameter("data", DataHoje.converte(feriados.getData()));
//            List list = qry.getResultList();
//            if (!list.isEmpty()) {
//                return true;
//            }
//        } catch (Exception e) {
//        }
//        return false;
//    }
//
//    @Override
//    public List pesquisarPorDataFilial(String data, Filial filial) {
//        try {
//            Query queryFilial = getEntityManager().createQuery("SELECT PE FROM PessoaEndereco AS PE WHERE PE.pessoa.id = :idPessoa");
//            queryFilial.setParameter("idPessoa", filial.getFilial().getPessoa().getId());
//            queryFilial.setMaxResults(1);
//            List listFilial = queryFilial.getResultList();
//            List list;
//            if (!listFilial.isEmpty()) {
//                PessoaEndereco pe = (PessoaEndereco) queryFilial.getSingleResult();
//                Query qry = getEntityManager().createQuery("SELECT F FROM Feriados AS F WHERE F.dtData = :data AND F.cidade.id = :idCidade");
//                qry.setParameter("data", DataHoje.converte(data));
//                qry.setParameter("idCidade", pe.getEndereco().getCidade().getId());
//                list = qry.getResultList();
//                if (!list.isEmpty()) {
//                    return list;
//                }
//            }
//        } catch (Exception e) {
//            return new ArrayList();
//        }
//        return new ArrayList();
//    }
//
//    @Override
//    public List pesquisarPorDataFilialEData(String data, Filial filial) {
//        try {
//            Query queryFilial = getEntityManager().createQuery("SELECT PE FROM PessoaEndereco AS PE WHERE PE.pessoa.id = :idPessoa");
//            queryFilial.setParameter("idPessoa", filial.getFilial().getPessoa().getId());
//            queryFilial.setMaxResults(1);
//            List listFilial = queryFilial.getResultList();
//            List list;
//            if (!listFilial.isEmpty()) {
//                PessoaEndereco pe = (PessoaEndereco) queryFilial.getSingleResult();
//                Query queryNativa = getEntityManager().createNativeQuery("SELECT id FROM hom_feriados WHERE (dt_data = '" + data + "' AND id_cidade = " + pe.getEndereco().getCidade().getId() + ") AND dt_data = '" + DataHoje.converte(data) + "'");
//                list = queryNativa.getResultList();
//                if (!list.isEmpty()) {
//                    String listaFeriadosString = "";
//                    for (int i = 0; i < list.size(); i++) {
//                        if (i == 0) {
//                            listaFeriadosString = ((List) list.get(i)).get(0).toString();
//                        } else {
//                            listaFeriadosString = "," + ((List) list.get(i)).get(0).toString();
//                        }
//                    }
//                    Query query = getEntityManager().createQuery("SELECT F FROM Feriados AS F WHERE F.id IN (" + listaFeriadosString + ")");
//                    list.clear();
//                    list = query.getResultList();
//                    if (!list.isEmpty()) {
//                        return list;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            return new ArrayList();
//        }
//        return new ArrayList();
//    }
//
//    @Override
//    public List pesquisaTodosFeriados() {
//        try {
//            Query query = getEntityManager().createQuery("SELECT F FROM Feriados AS F ORDER BY F.dtData DESC, F.nome ASC ");
//            List list = query.getResultList();
//            if (!list.isEmpty()) {
//                return list;
//            }
//        } catch (Exception e) {
//
//        }
//        return new ArrayList();
//    }
}
