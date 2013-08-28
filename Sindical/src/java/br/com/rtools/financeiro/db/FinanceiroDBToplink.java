package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.Historico;
import br.com.rtools.financeiro.Lote;
import br.com.rtools.financeiro.Baixa;
import br.com.rtools.financeiro.BloqueiaServicoPessoa;
import br.com.rtools.financeiro.Cobranca;
import br.com.rtools.financeiro.CobrancaLote;
import br.com.rtools.financeiro.CobrancaTipo;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.principal.DB;
import br.com.rtools.seguranca.Usuario;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;

public class FinanceiroDBToplink extends DB implements FinanceiroDB {
    @Override
    public boolean executarQuery(String textoQuery){
        try{
            Query qry = getEntityManager().createNativeQuery(textoQuery);
            int result = qry.executeUpdate();
            if(result == 0){
                return true;
            }else{
                return false;
            }
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean insert(Object objeto) {
        try{
          getEntityManager().getTransaction().begin();
          getEntityManager().persist(objeto);
          getEntityManager().flush();
          getEntityManager().getTransaction().commit();
          return true;
        } catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public String insertHist(Object objeto) {
        try{
          getEntityManager().getTransaction().begin();
          getEntityManager().persist(objeto);
          getEntityManager().flush();
          getEntityManager().getTransaction().commit();
          return "true";
        } catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return e.getMessage() + "  Resto";
        }
    }

    @Override
    public Movimento pesquisaCodigo(Movimento movimento) {
        int id = movimento.getId();
        Movimento result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("Movimento.pesquisaID");
            qry.setParameter("pid", id);
            result = (Movimento) qry.getSingleResult();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return result;
    }

    @Override
    public Baixa pesquisaCodigo(Baixa loteBaixa) {
        int id = loteBaixa.getId();
        Baixa result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("LoteBaixa.pesquisaID");
            qry.setParameter("pid", id);
            result = (Baixa) qry.getSingleResult();
        }
        catch(Exception e){
        }
        return result;
    }

    @Override
    public Lote pesquisaCodigo(Lote lote) {
        int id = lote.getId();
        Lote result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("Lote.pesquisaID");
            qry.setParameter("pid", id);
            result = (Lote) qry.getSingleResult();
        }
        catch(Exception e){
        }
        return result;
    }

    @Override
    public Usuario pesquisaUsuario(int idUsuario) {
        Usuario result = null;
        try{
            Query qry = getEntityManager().createQuery(
                    "select u " +
                    "  from Usuario u "+
                    " where u.id = :pid");
            qry.setParameter("pid", idUsuario);
            result = (Usuario) qry.getSingleResult();
        }
        catch(Exception e){
        }
        return result;
    }

    @Override
    public Historico pesquisaHistorico(int idHistorico) {
        Historico result = null;
        try{
            Query qry = getEntityManager().createQuery(
                    "select u " +
                    "  from Historico u "+
                    " where u.id = :pid");
            qry.setParameter("pid", idHistorico);
            result = (Historico) qry.getSingleResult();
        }
        catch(Exception e){
        }
        return result;
    }
    @Override
    public int contarMovimentosPara(int idLote){
        try{
            Query qry = getEntityManager().createQuery(
                    "select count (m) " +
                    "  from Movimento m "+
                    " where m.lote.id = " + idLote);
            List vetor = qry.getResultList();
            Long longI = (Long) vetor.get(0);
            return Integer.parseInt(Long.toString(longI));
        }catch(Exception e){
            return -1;
        }
    }

    @Override
    public List<Movimento> pesquisaMovimentoOriginal(int idLoteBaixa){
        try{
            Query qry = getEntityManager().createQuery(
                    "select m " +
                    "  from Movimento m left join m.baixa l"+
                    " where l.id = " + idLoteBaixa +
                    "   and m.ativo is false");
            return (List<Movimento>) qry.getResultList();
        }catch(Exception e){
            return new ArrayList<Movimento>();
        }
    }

    @Override
    public boolean update(Object objeto) {
        try{
            getEntityManager().merge(objeto);
            getEntityManager().flush();
            return true;
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean delete(Object objeto) {
        try{
            getEntityManager().remove(objeto);
            getEntityManager().flush();
        return true;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Movimento objeto) {
        try{
            getEntityManager().remove(objeto);
            getEntityManager().flush();
        return true;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }


    @Override
    public boolean acumularObjeto(Object objeto) {
        try{
            getEntityManager().persist(objeto);
            getEntityManager().flush();
            return true;
        } catch(Exception e){
            return false;
        }
    }
    
    @Override
    public void abrirTransacao(){
        getEntityManager().getTransaction().begin();
    }

    @Override
    public void comitarTransacao(){
        getEntityManager().getTransaction().commit();
    }

    @Override
    public void desfazerTransacao(){
        getEntityManager().getTransaction().rollback();
    }

    @Override
    public List<CobrancaLote> listaCobrancaLote(){
        try{
            Query qry = getEntityManager().createQuery(
                    "select cl " +
                    "  from CobrancaLote cl order by cl.dtEmissao desc");
            return qry.getResultList();
        }catch(Exception e){
            return new ArrayList();
        }
    }
    
    @Override
    public List<CobrancaTipo> listaCobrancaTipoEnvio(){
        try{
            Query qry = getEntityManager().createQuery(
                    "select ct " +
                    "  from CobrancaTipo ct");
            return qry.getResultList();
        }catch(Exception e){
            return new ArrayList();
        }
    }
    
    @Override
    public List<Cobranca> listaCobranca(int id_lote_cobranca){
        try{
            Query qry = getEntityManager().createQuery(
                    "select c " +
                    "  from Cobranca c where c.lote.id = "+id_lote_cobranca);
            return qry.getResultList();
        }catch(Exception e){
            return new ArrayList();
        }
    }
    
    @Override
    public CobrancaLote pesquisaCobrancaLote(int id_usuario, Date dataEmissao){
        try{
            Query qry = getEntityManager().createQuery(
                    "select cl " +
                    "  from CobrancaLote cl where cl.usuario.id = "+id_usuario+" and cl.dtEmissao = :data"
            );
            qry.setParameter("data", dataEmissao);
            return (CobrancaLote)qry.getSingleResult();
        }catch(Exception e){
            return null;
        }
    }    
    
    @Override
    public List listaNotificacao(int tipo_envio, int id_lote){
        try{
            String textQry = 
                        " select " +
                        "       '' as sinLogo, " +
                        "       sind.jurSite as sindSite, " +
                        "       sind.jurNome        as sinnome, " +
                        "       sind.jurEndereco    as sinendereco, " +
                        "       sind.jurLogradouro  as sinlogradouro, " +
                        "       sind.jurNumero      as sinnumero, " +
                        "       sind.jurComplemento as sincomplemento, " +
                        "       sind.jurBairro      as sinbairro, " +
                        "       substring(sind.jurCep,1,5)||'-'||substring(sind.jurCep,6,3)  as sincep, " +
                        "       sind.jurCidade      as sincidade, " +
                        "       sind.jurUf          as sinuF, " +
                        "       sind.jurDocumento   as sindocumento, " +
                        "       pj.escNome, " +
                        "       pj.escTelefone, " +
                        "       pj.escLogradouro, "+
                        "       pj.escEndereco, "+
                        "       pj.escNumero, "+
                        "       pj.escComplemento, "+
                        "       pj.escBairro, " +
                        "       substring(pj.escCep,1,5)||'-'||substring(pj.escCep,6,3) as escCep," +
                        "       pj.escCidade, "+
                        "       pj.escUf, " +
                        "       pj.jurNome, " +
                        "       pj.jurDocumento, "+
                        "       pj.jurTelefone, " +
                        "       pj.jurcidade, " +
                        "       pj.juruf, " +
                        "       pj.jurcep, " +
                        "       pj.jurlogradouro, " +
                        "       pj.jurendereco, " +
                        "       pj.jurnumero, " +
                        "       pj.jurcomplemento, " +
                        "       pj.jurbairro, " +
                        "       se.ds_descricao    as movServico, " +
                        "       ts.ds_descricao    as movTipoServico, " +
                        "       m.ds_referencia    as movReferencia, " +
                        "       m.ds_documento     as movNumeroBoleto, " +
                        "       l.ds_mensagem, "+
                        "       pj.escid, " +
                        "       pj.id_pessoa " +
                        "  from  pes_juridica_vw      as pj " +
                        " inner join fin_movimento          as m    on m.id_pessoa    = pj.id_pessoa " +
                        " inner join fin_servicos           as se   on se.id          = m.id_servicos " +
                        " inner join fin_tipo_servico       as ts   on ts.id          = m.id_tipo_servico " +
                        " inner join pes_juridica_vw as sind on sind.id_pessoa = 1 " +
                        " inner join pes_juridica           as j    on j.id_pessoa    = pj.id_pessoa " +
                        " inner join arr_contribuintes_vw   as co   on co.id_juridica = j.id " +
                        " inner join fin_cobranca           as c    on c.id_movimento = m.id " +
                        " inner join fin_cobranca_lote      as l    on l.id           = c.id_lote " +
                        " where m.id_baixa is null and is_ativo = true and l.id = "+id_lote;
            
            // 1 "ESCRITÓRIO"
            if (tipo_envio == 1){
                textQry += " and pj.escNome is not null " +
                           " order by pj.escCidade, pj.escCep, pj.escNumero, pj.escNome, pj.jurNome, substring(m.ds_referencia,4,4) || substring(m.ds_referencia,1,2) ";
            // 2 "EMPRESA COM ESCRITÓRIO"
            }else if (tipo_envio == 2){
                textQry += " and pj.escNome is not null " +
                           " order by pj.jurCidade, pj.jurCep, pj.jurBairro, pj.jurEndereco, pj.jurNumero, pj.jurComplemento, pj.jurNome ";
            // 3 "EMPRESA SEM ESCRITÓRIO"    
            }else if (tipo_envio == 3){
                textQry += " and pj.escNome is null " +
                           " order by pj.jurCidade, pj.jurCep, pj.jurBairro, pj.jurEndereco, pj.jurNumero, pj.jurComplemento, pj.jurNome ";
            // 4 "EMAIL PARA OS ESCRITÓRIO"    AGRUPAR POR pj.escid -- id_escritorio
            }else if (tipo_envio == 4){
                textQry += " and pj.escNome is not null " +
                           " order by pj.escNome, pj.escid, pj.jurNome, pj.jurDocumento, substring(m.ds_referencia,4,4) || substring(m.ds_referencia,1,2) ";
            // 5 "EMAIL PARA AS EMPRESAS" -- AGRUPAR POR pj.id_pessoa -- id_pessoa
            }else if (tipo_envio == 5){
                textQry += " order by pj.jurNome, pj.jurDocumento, substring(m.ds_referencia,4,4) || substring(m.ds_referencia,1,2) ";
            }
            
            Query qry = getEntityManager().createNativeQuery(textQry);
            
            return qry.getResultList();
        }catch(Exception e){
            return new ArrayList();
        }
    }
    
    @Override
    public List<BloqueiaServicoPessoa> listaBloqueiaServicoPessoas(int id_pessoa){
        try{
            Query qry = getEntityManager().createQuery(
                    "select bl from BloqueiaServicoPessoa bl where bl.pessoa.id = "+id_pessoa+" order by bl.servicos.descricao"
            );
            return qry.getResultList();
        }catch(Exception e){
            return new ArrayList<BloqueiaServicoPessoa>();
        }
    }    
    
    @Override
    public BloqueiaServicoPessoa  pesquisaBloqueiaServicoPessoa(int id_pessoa, int id_servico, Date dt_inicial, Date dt_final) {
        try{
            Query qry = getEntityManager().createQuery(
                    "select bl from BloqueiaServicoPessoa bl where bl.pessoa.id = "+id_pessoa+" and bl.servicos.id = "+id_servico+" and bl.dtInicio = :dtInicial and bl.dtFim = :dtFinal"
            );
            qry.setParameter("dtInicial", dt_inicial);
            qry.setParameter("dtFinal", dt_final);
            return (BloqueiaServicoPessoa) qry.getSingleResult();
        }catch(Exception e){
            
        }
        return null;
    }
}
