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
