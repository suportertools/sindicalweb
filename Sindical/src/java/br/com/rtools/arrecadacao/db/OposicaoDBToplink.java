package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.ConvencaoPeriodo;
import br.com.rtools.arrecadacao.Oposicao;
import br.com.rtools.arrecadacao.OposicaoPessoa;
import br.com.rtools.pessoa.PessoaEmpresa;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;

public class OposicaoDBToplink extends DB implements OposicaoDB {

    @Override
    public ConvencaoPeriodo pesquisaConvencaoPeriodo(int id_convencao, int id_grupo) {
        ConvencaoPeriodo result = new ConvencaoPeriodo();
        try {
            Query qry = getEntityManager().createQuery("select cp from ConvencaoPeriodo cp where cp.convencao.id = " + id_convencao + " and cp.grupoCidade.id = " + id_grupo);
            result = (ConvencaoPeriodo) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List<ConvencaoPeriodo> listaConvencaoPeriodo() {
        try {
            Query qry = getEntityManager().createQuery("select cp from ConvencaoPeriodo cp order by cp.convencao.descricao");
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public List<Oposicao> listaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select o from Oposicao o order by o.fisica.pessoa.nome");
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public Oposicao pesquisaOposicao(int id_fisica, int id_juridica) {
        Oposicao result = new Oposicao();
        try {
            Query qry = getEntityManager().createQuery("select o from Oposicao o where o.fisica.id = " + id_fisica + " and o.juridica.id = " + id_juridica);
            result = (Oposicao) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }
    
    @Override
    public PessoaEmpresa pesquisaPessoaFisicaEmpresa(String cpf, String rg) {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        PessoaEmpresa pessoaEmpresa = new PessoaEmpresa();
        List list = new Vector();
        String queryString = "  SELECT emp.id                                                                        "
                + "              FROM pes_pessoa pes                                                                 "
                + "         INNER JOIN pes_fisica fis ON(fis.id_pessoa = pes.id)                                     "
                + "         INNER JOIN pes_pessoa_empresa emp ON(emp.id_fisica = fis.id)                             "        
                + "         INNER JOIN pes_juridica jur ON(jur.id = emp.id_juridica)                                 "        
                + "              WHERE (                                                                             "
                + "                         pes.ds_documento = '"+cpf+"'                                             "
                + "                 OR (                                                                             "
                + "                      TRANSLATE(UPPER(fis.ds_rg),'./-', '') = TRANSLATE('"+rg+"','./-', '')       "
                + "                      AND fis.ds_rg is not null                                                   "
                + "                      AND trim(fis.ds_rg)<>''                                                     "
                + "                      AND trim(fis.ds_rg)<>'0'                                                    "    
                + "                     )                                                                            "
                + "               )                                                                                  "
                + "                AND emp.dt_demissao   is null                                                     "        
                + "                AND jur.dt_fechamento is null LIMIT 1                                             ";
                
        try {
            Query qry = getEntityManager().createNativeQuery(queryString);
            list = (Vector) qry.getSingleResult();
            if (list.size() > 0){
                pessoaEmpresa = (PessoaEmpresa) salvarAcumuladoDB.pesquisaCodigo((Integer) list.get(0), "PessoaEmpresa");
                return pessoaEmpresa;
            }
        } catch (Exception e) {
            return pessoaEmpresa;
        }
        return pessoaEmpresa;
    }

    @Override
    public OposicaoPessoa pesquisaOposicaoPessoa(String cpf, String rg) {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        OposicaoPessoa oposicaoPessoa = new OposicaoPessoa();
        List vector = new Vector();
        String queryString = "                                                                          "
                + "      SELECT id                                                                        "
                + "        FROM arr_oposicao_pessoa                                                       "
                + "       WHERE ds_cpf = '"+cpf+"'                                                        "
                + "          OR (                                                                         "
                + "                 TRANSLATE(UPPER(ds_rg),'./-', '') = TRANSLATE('"+rg+"','./-', '')     "
                + "                 AND ds_rg is not null                                                 "
                + "                 AND trim(ds_rg)<>''                                                   "
                + "                 AND trim(ds_rg)<>'0'                                                  "    
                + "          )                                                                            "
                + "                                                                                       ";
        try {
            Query qry = getEntityManager().createNativeQuery(queryString);
            vector = (Vector) qry.getSingleResult();
            if (!vector.isEmpty()){
                oposicaoPessoa = (OposicaoPessoa) salvarAcumuladoDB.pesquisaCodigo((Integer) vector.get(0), "OposicaoPessoa");
                return oposicaoPessoa;
            }
        } catch (Exception e) {
            return oposicaoPessoa;
        }
        return oposicaoPessoa;
    }

    @Override
    public List<Vector> pesquisaPessoaConvencaoGrupoCidade(int id) {
        List<Vector> vetor = new ArrayList();
        String queryString = " SELECT id_convencao, id_grupo_cidade from arr_contribuintes_vw where dt_inativacao is null and id_juridica = " + id;
        try {
            Query qry = getEntityManager().createNativeQuery(queryString);
            vetor = (Vector) qry.getSingleResult();
        } catch (Exception e) {
            return new ArrayList();
        }
        return vetor;
    }

    @Override
    public List<Oposicao> pesquisaOposicao(String descricaoPesquisa, String tipoPesquisa, String comoPesquisa) {

        List<Oposicao> listaOposicaos = new ArrayList();

        String queryString = "";

        if (tipoPesquisa.equals("nome")) {
            queryString = " WHERE UPPER(opo.oposicaoPessoa.nome) LIKE :descricaoPesquisa ";
        } else if (tipoPesquisa.equals("cpf")) {
            queryString = " WHERE opo.oposicaoPessoa.cpf LIKE :descricaoPesquisa ";
        } else if (tipoPesquisa.equals("rgs")) {
            queryString = " WHERE UPPER(opo.oposicaoPessoa.rg) LIKE :descricaoPesquisa ";
        } else if (tipoPesquisa.equals("empresa")) {
            queryString = " WHERE UPPER(opo.juridica.pessoa.nome) LIKE :descricaoPesquisa ";
        } else if (tipoPesquisa.equals("cnpj")) {
            queryString = " WHERE opo.juridica.pessoa.documento LIKE :descricaoPesquisa ";
        } else if (tipoPesquisa.equals("observacao")) {
            queryString = " WHERE UPPER(opo.observacao) LIKE :descricaoPesquisa ";
        } else if (tipoPesquisa.equals("data")) {
            queryString = " WHERE opo.dtEmissao = '" + DataHoje.livre(DataHoje.converte(descricaoPesquisa), "yyyy-MM-dd") +"'";
        } else if (tipoPesquisa.equals("todos")) {
            queryString = "";
        }
        
        try {
            Query qry = getEntityManager().createQuery(" SELECT opo FROM Oposicao opo " + queryString + " ORDER BY opo.dtEmissao DESC ");
            if (!descricaoPesquisa.equals("") && !tipoPesquisa.equals("todos") && !tipoPesquisa.equals("data")) {
                if (comoPesquisa.equals("Inicial")) {
                    qry.setParameter("descricaoPesquisa", "" + descricaoPesquisa.toUpperCase() + "%");
                } else if (comoPesquisa.equals("Parcial")) {
                    qry.setParameter("descricaoPesquisa", "%" + descricaoPesquisa.toUpperCase() + "%");
                }
            }
            listaOposicaos = qry.getResultList();
            return listaOposicaos;
        } catch (Exception e) {
            return new ArrayList<Oposicao>();
        }
    }

    @Override
    public boolean validaOposicao(Oposicao oposicao) {
        List result = null;
        try {
            Query query = getEntityManager().createQuery(""
                    + "SELECT opo "
                    + "  FROM Oposicao opo "
                    + " WHERE opo.juridica.id = :juridica "
                    + "   AND opo.oposicaoPessoa.cpf = :cpf "
                    + "   AND opo.convencaoPeriodo.convencao.id = :convencao "
                    + "   AND opo.convencaoPeriodo.grupoCidade.id = :grupoCidade "
                    + "   AND opo.convencaoPeriodo.referenciaInicial = :referenciaInicial "
                    + "   AND opo.convencaoPeriodo.referenciaFinal= :referenciaFinal ");
            query.setParameter("juridica", oposicao.getJuridica().getId());
            query.setParameter("referenciaInicial", oposicao.getConvencaoPeriodo().getReferenciaInicial());
            query.setParameter("referenciaFinal", oposicao.getConvencaoPeriodo().getReferenciaFinal());
            query.setParameter("cpf", oposicao.getOposicaoPessoa().getCpf());
            query.setParameter("convencao", oposicao.getConvencaoPeriodo().getConvencao().getId());
            query.setParameter("grupoCidade", oposicao.getConvencaoPeriodo().getGrupoCidade().getId());
            result = query.getResultList();
            if (result.size() > 0) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
