package br.com.rtools.relatorios.db;

import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class RelatorioContabilidadesDBToplink extends DB implements RelatorioContabilidadesDB{

    public List pesquisaContabilidades(){
        List result = new ArrayList();
        try{
            Query qry = getEntityManager().createQuery("select distinct jur.contabilidade " +
                                                       "  from Juridica jur" +
                                                       " where jur.contabilidade is not null" +
                                                       " order by jur.contabilidade.id");
            result = qry.getResultList();
        }
        catch(Exception e){
        }
        return result;
    }

    public List pesquisaQntEmpresas(int id_contabilidade){
        List result = new ArrayList();
        try{
            Query qry = getEntityManager().createQuery("select count(jur) " +
                                                       "  from Juridica jur" +
                                                       " where jur.contabilidade.id = " + id_contabilidade);
            result = qry.getResultList();
        }
        catch(Exception e){
        }
        return result;
    }

    public List pesquisarCnaeContabilidade(){
        List result = new ArrayList();
           try{
               Query qry = getEntityManager().createQuery("select distinct j.contabilidade.cnae " +
                                                          "  from Juridica j" +
                                                          " where j.contabilidade.id is not null" +
                                                          " order by j.contabilidade.cnae.cnae");
               result = qry.getResultList();
           }catch(Exception e ){
           }
    return result;
    }

    public List listaRelatorioContabilidades(String pEmpresas,int indexEmp1, int indexEmp2, String tipoPCidade, String cidade, String ordem, String cnaes, int idTipoEndereco){
        List result = new ArrayList();
        String textQuery = "";
           try{
               textQuery = "select j "+
                          "   from Juridica j," +
                          "        PessoaEndereco pe ";
               if (pEmpresas.equals("todas")){
                   textQuery = textQuery + " where ( (j.cnae.id in (1)) " +
                                           "        or  (j.id in (select jc.contabilidade.id " +
                                           "                        from Juridica jc" +
                                           "                       where jc.contabilidade is not null))) ";
               }else if (pEmpresas.equals("semEmpresas")){
                   textQuery = textQuery + " where ( (j.cnae.id in (1)) " +
                                           "        and  (j.id not in (select jc.contabilidade.id " +
                                           "                        from Juridica jc" +
                                           "                       where jc.contabilidade is not null))) ";
               }else if (pEmpresas.equals("comEmpresas")){
                  textQuery = textQuery + " where j.id in (select jc.contabilidade.id " +
                                          "                   from Juridica jc " +
                                          "                  where jc.contabilidade is not null " +
                                          "                  group by jc.contabilidade.id "+
                                          "                 having count(jc.contabilidade.id) >= "+indexEmp1+" and count(jc.contabilidade.id) <= "+indexEmp2+") ";
               }
               // CIDADE -------------------------------------------------------
               if (tipoPCidade.equals("todas")){
                   textQuery = textQuery + " and pe.pessoa.id = j.pessoa.id " +
                                           " and pe.tipoEndereco.id = "+ idTipoEndereco;
               }else if(tipoPCidade.equals("especificas")){
                   textQuery = textQuery + " and pe.pessoa.id = j.pessoa.id " +
                                           " and pe.tipoEndereco.id = "+ idTipoEndereco+
                                           " and pe.endereco.cidade.id = " + Integer.parseInt(cidade);
               }else if(tipoPCidade.equals("local")){
                   textQuery = textQuery + " and pe.pessoa.id = j.pessoa.id " +
                                           " and pe.tipoEndereco.id = "+ idTipoEndereco+
                                           " and pe.endereco.cidade.id = " + Integer.parseInt(cidade);
               }else if(tipoPCidade.equals("outras")){
                   textQuery = textQuery + " and pe.pessoa.id = j.pessoa.id " +
                                           " and pe.tipoEndereco.id = "+ idTipoEndereco+
                                           " and pe.endereco.cidade.id <> " + Integer.parseInt(cidade);
               }
               // CNAES

               if (cnaes.length() != 0){
                   textQuery = textQuery + " and j.cnae.id in ( "+cnaes+" ) ";
               }

                // ORDEM ------------------------------------------------------------------------
                if (ordem.equals("razao")){
                    textQuery = textQuery + " order by j.pessoa.nome";
                }else if (ordem.equals("documento")){
                    textQuery = textQuery + " order by j.pessoa.documento";
                }else if (ordem.equals("endereco")){
                    textQuery = textQuery + " order by pe.endereco.cidade.uf," +
                                            " pe.endereco.cidade.cidade, pe.endereco.logradouro.logradouro," +
                                            " pe.endereco.descricaoEndereco.descricaoEndereco," +
                                            " pe.numero";
                }else if (ordem.equals("cep")){
                    textQuery = textQuery + " order by pe.endereco.cep," +
                                            " pe.endereco.cidade.uf," +
                                            " pe.endereco.cidade.cidade, pe.endereco.logradouro.logradouro," +
                                            " pe.endereco.descricaoEndereco.descricaoEndereco," +
                                            " pe.numero";
                }
               Query qry = getEntityManager().createQuery(textQuery);
               result = qry.getResultList();
           }catch(Exception e ){
           }
        return result;
    }
}
