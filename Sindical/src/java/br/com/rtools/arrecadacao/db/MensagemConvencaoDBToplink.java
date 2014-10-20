package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.MensagemConvencao;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.db.JuridicaDB;
import br.com.rtools.pessoa.db.JuridicaDBToplink;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;
import oracle.toplink.essentials.exceptions.EJBQLException;

public class MensagemConvencaoDBToplink extends DB implements MensagemConvencaoDB {

    @Override
    public boolean insert(MensagemConvencao mensagemConvencao) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(mensagemConvencao);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(MensagemConvencao mensagemConvencao) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(mensagemConvencao);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean delete(MensagemConvencao mensagemConvencao) {
        try {
            getEntityManager().remove(mensagemConvencao);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public MensagemConvencao pesquisaCodigo(int id) {
        MensagemConvencao result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("MensagemConvencao.pesquisaID");
            qry.setParameter("pid", id);
            result = (MensagemConvencao) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select cont from MensagemConvencao cont ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List pesquisaTodosOrdenados(String referencia, int idServicos, int idTipoServico) {
        List vetor;
        List<MensagemConvencao> listMen = new ArrayList();
        String textQuery
                = "select m.id                                                                                                                                    "
                + "  from arr_mensagem_convencao m                                                                                                                "
                + " inner join  fin_servicos s on (s.id = m.id_servicos)                                                                                          "
                + " inner join  fin_tipo_servico t on (t.id = m.id_tipo_servico)                                                                                  "
                + " inner join  arr_grupo_cidade g on (g.id = m.id_grupo_cidade)                                                                                  "
                + " inner join  arr_convencao c on (c.id = m.id_convencao)                                                                                        "
                + " where m.ds_referencia = \'" + referencia + "\' "
                + "   and m.id_servicos = " + idServicos
                + "   and m.id_tipo_servico = " + idTipoServico
                + " order by substring(ds_referencia,4,7) desc, substring(ds_referencia,1,2), s.ds_descricao , t.ds_descricao, c.ds_descricao, g.ds_descricao";
        try {
            Query qry = getEntityManager().createNativeQuery(textQuery);
            vetor = qry.getResultList();
            if (!vetor.isEmpty()) {
                for (int i = 0; i < vetor.size(); i++) {
                    listMen.add(pesquisaCodigo((Integer) ((Vector) vetor.get(i)).get(0)));
                }
            }
            return listMen;
        } catch (EJBQLException e) {
            return null;
        }
    }

    @Override
    public List pesquisaSemRef() {
        try {
            Query qry = getEntityManager().createQuery("select cont from MensagemConvencao cont"
                    + " where cont.referencia = '' order by cont.id desc");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List pesquisaSemServ4() {
        try {
            Query qry = getEntityManager().createQuery("select cont from MensagemConvencao cont"
                    + " where cont.tipoServico.id <> 4 order by cont.id desc");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public MensagemConvencao verificaMensagem(int idConvencao, int idServicos, int idTipoServicos, int idGrupoCidade, String referencia) {
        MensagemConvencao result = null;
        List query = null;
        try {
            Query qry = getEntityManager().createQuery(
                    "select m "
                    + "  from MensagemConvencao m"
                    + " where m.convencao.id = :idC"
                    + "   and m.servicos.id = :idS"
                    + "   and m.tipoServico.id = :idT"
                    + "   and m.referencia = :idR"
                    + "   and m.grupoCidade.id = :idG");
            qry.setParameter("idC", idConvencao);
            qry.setParameter("idS", idServicos);
            qry.setParameter("idT", idTipoServicos);
            qry.setParameter("idG", idGrupoCidade);
            qry.setParameter("idR", referencia);
            query = qry.getResultList();
            if (!query.isEmpty() && query.size() == 1) {
                result = (MensagemConvencao) query.get(0);
            } else if (query.size() > 1) {
                result = new MensagemConvencao();
            }
        } catch (Exception e) {
            result = new MensagemConvencao();
            // ANTES DAVA ERRO RETORNAVA NULL...
        }
        return result;
    }

    @Override
    public List mesmoTipoServico(int idServicos, int idTipoServico, String ano) {
        List result = new ArrayList();
        ano = "%" + ano + "%";
        try {
            Query qry = getEntityManager().createQuery(
                    "select m "
                    + "  from MensagemConvencao m"
                    + " where m.servicos.id = :idS"
                    + "   and m.tipoServico.id = :idT"
                    + "   and m.referencia like :ano");
            qry.setParameter("idS", idServicos);
            qry.setParameter("idT", idTipoServico);
            qry.setParameter("ano", ano);
            result = qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public MensagemConvencao retornaDiaString(int idJuridica, String ref, int idTipoServico, int idServicos) {
        // Modificado: 15/10/2014 - Query objeto deixava a rotina lenta;
        JuridicaDB db = new JuridicaDBToplink();
        String textQuery = ""
                + " SELECT m.*                                                  "
                + "   FROM pes_juridica           j,                            "
                + "        pes_pessoa_endereco   pe,                            "
                + "        arr_cnae_convencao    cc,                            "
                + "        arr_grupo_cidades     gc,                            "
                + "        arr_mensagem_convencao m,                            "
                + "        arr_convencao_cidade coc,                            "
                + "        end_endereco         e                               "
                + "  WHERE cc.id_cnae = j.id_cnae                               "
                + "    AND pe.id_pessoa = j.id_pessoa                           "
                + "    AND pe.id_tipo_Endereco = 5                              "
                + "    AND pe.id_endereco=e.id                                  "
                + "    AND e.id_cidade            = gc.id_cidade                "
                + "    AND coc.id_grupo_Cidade    = gc.id_grupo_Cidade          "
                + "    AND coc.id_convencao       = cc.id_convencao             "
                + "    AND m.id_grupo_Cidade      = gc.id_grupo_Cidade          "
                + "    AND m.id_convencao         = cc.id_convencao             "
                + "    AND m.ds_referencia     = '" + ref + "'                  "
                + "    AND m.id_tipo_Servico   =  " + idTipoServico + "         "
                + "    AND m.id_servicos       =  " + idServicos + "            "
                + "    AND j.id = " + idJuridica + ";                           ";
        try {
            Query query = getEntityManager().createNativeQuery(textQuery, MensagemConvencao.class); 
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (MensagemConvencao) query.getSingleResult();
            }            
        } catch(Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public MensagemConvencao retornaDiaString(Pessoa pessoa, String ref, int idTipoServico, int idServicos) {
        MensagemConvencao result = new MensagemConvencao();
        JuridicaDB db = new JuridicaDBToplink();
        Query qry = null;
        List<Vector> listax = db.listaJuridicaContribuinteID();
        String in = "";
        for (int i = 0; listax.size() < i; i++) {
            if (in.length() > 0 && i != listax.size()) {
                in += ",";
            }
            in += String.valueOf(listax.get(i).get(0));
        }
        String textQuery = " select m                                    "
                + "   from Juridica j,                          "
                + "        PessoaEndereco pe,                   "
                + "        CnaeConvencao cc,                    "
                + "        GrupoCidades gc,                     "
                + "        MensagemConvencao m                  "
                + "  where j.id in (" + in + ")                 "
                + "    and cc.cnae.id = j.cnae.id               "
                + "    and cc.convencao.id = m.convencao.id     "
                + "    and pe.pessoa.id = j.pessoa.id           "
                + "    and pe.tipoEndereco.id = 5               "
                + "    and pe.endereco.cidade.id = gc.cidade.id "
                + "    and gc.grupoCidade.id = m.grupoCidade.id "
                + "    and m.referencia = :referencia           "
                + "    and m.tipoServico.id = :idTipo           "
                + "    and m.servicos.id = :idServicos          "
                + "    and j.pessoa.id = :pid                        ";
        try {
            qry = getEntityManager().createQuery(textQuery);
            qry.setParameter("pid", pessoa.getId());
            qry.setParameter("idTipo", idTipoServico);
            qry.setParameter("idServicos", idServicos);
            qry.setParameter("referencia", ref);
            result = ((MensagemConvencao) qry.getSingleResult());
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    @Override
    public MensagemConvencao pesquisarUltimaMensagem(int idConvencao, int idServicos, int idTipoServico, int idGrupoCidade) {
        MensagemConvencao result = new MensagemConvencao();
        try {
            Query qry = null;
            String texto = "select max(m.id)                  "
                    + "  from arr_mensagem_convencao m   "
                    + " where m.id_tipo_servico = " + idTipoServico
                    + "   and m.id_servicos = " + idServicos
                    + "   and m.id_grupo_cidade = " + idGrupoCidade
                    + "   and m.id_convencao = " + idConvencao;

            qry = getEntityManager().createNativeQuery(texto);
            List listaCount = qry.getResultList();
            int idMax = (Integer) ((Vector) listaCount.get(0)).get(0);
            String textQuery = " select m                                    "
                    + "   from MensagemConvencao m                  "
                    + "  where m.id = " + idMax;
            qry = getEntityManager().createQuery(textQuery);
            result = ((MensagemConvencao) qry.getSingleResult());
        } catch (Exception e) {
            result = new MensagemConvencao();
        }
        return result;
    }

    public List<MensagemConvencao> pesquisaTodosAno(String ano) {
        List<Vector> vetor = new ArrayList();
        List<MensagemConvencao> lista = new ArrayList();
        try {
            String textQry = "select mc.id from arr_mensagem_convencao mc where substring(mc.ds_referencia,4,7) = '" + ano + "'";
            Query qry = getEntityManager().createNativeQuery(textQry);
            vetor = qry.getResultList();
            if (!vetor.isEmpty()) {
                for (int i = 0; i < vetor.size(); i++) {
                    lista.add(pesquisaCodigo((Integer) ((Vector) vetor.get(i)).get(0)));
                }
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return lista;
    }
}
