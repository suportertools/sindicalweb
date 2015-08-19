package br.com.rtools.financeiro.db;

import br.com.rtools.associativo.LoteBoleto;
import br.com.rtools.financeiro.Historico;
import br.com.rtools.financeiro.Lote;
import br.com.rtools.financeiro.Baixa;
import br.com.rtools.financeiro.BloqueiaServicoPessoa;
import br.com.rtools.financeiro.Caixa;
import br.com.rtools.financeiro.ContaSaldo;
import br.com.rtools.financeiro.FormaPagamento;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.SubGrupoFinanceiro;
import br.com.rtools.financeiro.TransferenciaCaixa;
import br.com.rtools.principal.DB;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.AnaliseString;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;

public class FinanceiroDBToplink extends DB implements FinanceiroDB {

    @Override
    public boolean executarQuery(String textoQuery) {
        try {
            Query qry = getEntityManager().createNativeQuery(textoQuery);
            int result = qry.executeUpdate();
            if (result == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean insert(Object objeto) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(objeto);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public String insertHist(Object objeto) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(objeto);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return "true";
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return e.getMessage() + "  Resto";
        }
    }

    @Override
    public Movimento pesquisaCodigo(Movimento movimento) {
        int id = movimento.getId();
        Movimento result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Movimento.pesquisaID");
            qry.setParameter("pid", id);
            result = (Movimento) qry.getSingleResult();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    @Override
    public Baixa pesquisaCodigo(Baixa loteBaixa) {
        int id = loteBaixa.getId();
        Baixa result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("LoteBaixa.pesquisaID");
            qry.setParameter("pid", id);
            result = (Baixa) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public Lote pesquisaCodigo(Lote lote) {
        int id = lote.getId();
        Lote result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Lote.pesquisaID");
            qry.setParameter("pid", id);
            result = (Lote) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public Usuario pesquisaUsuario(int idUsuario) {
        Usuario result = null;
        try {
            Query qry = getEntityManager().createQuery(
                    "select u "
                    + "  from Usuario u "
                    + " where u.id = :pid");
            qry.setParameter("pid", idUsuario);
            result = (Usuario) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public Historico pesquisaHistorico(int idHistorico) {
        Historico result = null;
        try {
            Query qry = getEntityManager().createQuery(
                    "select u "
                    + "  from Historico u "
                    + " where u.id = :pid");
            qry.setParameter("pid", idHistorico);
            result = (Historico) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public int contarMovimentosPara(int idLote) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select count (m) "
                    + "  from Movimento m "
                    + " where m.lote.id = " + idLote);
            List vetor = qry.getResultList();
            Long longI = (Long) vetor.get(0);
            return Integer.parseInt(Long.toString(longI));
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public List<Movimento> pesquisaMovimentoOriginal(int idLoteBaixa) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select m "
                    + "  from Movimento m left join m.baixa l"
                    + " where l.id = " + idLoteBaixa
                    + "   and m.ativo is false");
            return (List<Movimento>) qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public boolean update(Object objeto) {
        try {
            getEntityManager().merge(objeto);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(Object objeto) {
        try {
            getEntityManager().remove(objeto);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Movimento objeto) {
        try {
            getEntityManager().remove(objeto);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean acumularObjeto(Object objeto) {
        try {
            getEntityManager().persist(objeto);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void abrirTransacao() {
        getEntityManager().getTransaction().begin();
    }

    @Override
    public void comitarTransacao() {
        getEntityManager().getTransaction().commit();
    }

    @Override
    public void desfazerTransacao() {
        getEntityManager().getTransaction().rollback();
    }

    @Override
    public List<BloqueiaServicoPessoa> listaBloqueiaServicoPessoas(int id_pessoa) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select bl from BloqueiaServicoPessoa bl where bl.pessoa.id = " + id_pessoa + " order by bl.servicos.descricao");
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public BloqueiaServicoPessoa pesquisaBloqueiaServicoPessoa(int id_pessoa, int id_servico, Date dt_inicial, Date dt_final) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select bl from BloqueiaServicoPessoa bl where bl.pessoa.id = " + id_pessoa + " and bl.servicos.id = " + id_servico + " and bl.dtInicio = :dtInicial and bl.dtFim = :dtFinal");
            qry.setParameter("dtInicial", dt_inicial);
            qry.setParameter("dtFinal", dt_final);
            return (BloqueiaServicoPessoa) qry.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }
    
    @Override
    public List<Movimento> pesquisaMovimentoPorLote(int id_lote) {
        try {
            Query qry = getEntityManager().createQuery("select m from Movimento m where m.lote.id = " + id_lote);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }
    
    @Override
    public ContaSaldo pesquisaSaldoInicial(int id_caixa) {
        try {
            Query qry = getEntityManager().createQuery("select cs from ContaSaldo cs where cs.caixa.id = "+id_caixa+" and cs.dtData = (select MAX(csx.dtData) from ContaSaldo csx)");
            return (ContaSaldo)qry.getSingleResult();
        } catch (Exception e) {
            return new ContaSaldo();
        }
    }
    
    @Override
    public List<Caixa> listaCaixa(){
        try {
            Query qry = getEntityManager().createQuery("select c from Caixa c where c.caixa <> 1 order by c.caixa");
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }
    
    @Override
    public List listaMovimentoCaixa(int id_caixa, String es, Integer id_usuario) {
        try {
            String and = (id_usuario == null) ? "" : " AND u.id = "+id_usuario;
            Query qry = getEntityManager().createNativeQuery(
                                                        "SELECT distinct(f.id), \n " +
                                                        "       m.ds_es, \n " +
                                                        "	b.dt_baixa, \n " +
                                                        "	b.id_caixa, \n " +
                                                        "	p.ds_nome, \n " +
                                                        "	tp.ds_descricao, \n " +
                                                        "	f.nr_valor, \n " +
                                                        "       cx.id_filial, \n " +
                                                        "       b.id \n " +
                                                        "  FROM fin_forma_pagamento AS f \n " +
                                                        " INNER JOIN fin_baixa AS b ON b.id=f.id_baixa \n " +
                                                        " INNER JOIN seg_usuario AS u ON u.id=b.id_usuario \n " +
                                                        " INNER JOIN pes_pessoa AS p ON p.id=u.id_pessoa \n " +
                                                        " INNER JOIN fin_movimento AS m ON m.id_baixa=b.id \n " +
                                                        " INNER JOIN fin_tipo_pagamento AS tp ON tp.id = f.id_tipo_pagamento \n " +
                                                        " INNER JOIN fin_caixa AS cx ON cx.id = b.id_caixa \n " +
                                                        " WHERE b.id_caixa = "+id_caixa+" AND b.id_fechamento_caixa IS NULL \n " +
                                                        "   AND m.ds_es = '"+es+"' \n "+ and);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }
    
    @Override
    public List<TransferenciaCaixa> listaTransferenciaEntrada(int id_caixa, Integer id_usuario) {
        String and = (id_usuario == null) ? "" : " and tc.usuario.id = "+id_usuario;
        try {
            Query qry = getEntityManager().createQuery("SELECT tc FROM TransferenciaCaixa tc WHERE tc.caixaEntrada.id = "+id_caixa+" AND tc.fechamentoEntrada is null "+and);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }
    
    @Override
    public List<TransferenciaCaixa> listaTransferenciaSaida(int id_caixa, Integer id_usuario) {
        String and = (id_usuario == null) ? "" : " and tc.usuario.id = "+id_usuario;
        try {
            Query qry = getEntityManager().createQuery("SELECT tc FROM TransferenciaCaixa tc WHERE tc.caixaSaida.id = "+id_caixa+" AND (tc.caixaEntrada.caixa <> 1) AND tc.fechamentoSaida is null "+ and);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }
    
    @Override
    public List<Baixa> listaBaixa(int id_fechamento_caixa) {
        try {
            Query qry = getEntityManager().createQuery(
                    "SELECT b " +
                    "  FROM Baixa b " +
                    " WHERE b.fechamentoCaixa.id = " + id_fechamento_caixa
            );
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }
    
    @Override
    public List listaFechamentoCaixaTransferencia(int id_caixa) {
        try {
            String text = 
                    "SELECT 	tc.id_caixa_entrada, " +
                    "        tc.id_fechamento_entrada, " +
                    "        fc.nr_valor_fechamento, " +
                    "        fc.nr_valor_informado, " +
                    "        fc.dt_data, " +
                    "        fc.ds_hora  " +
                    "  FROM fin_fechamento_caixa fc  " +
                    " INNER JOIN fin_transferencia_caixa tc ON tc.id_fechamento_entrada = fc.id AND tc.id_caixa_entrada = " + id_caixa +
                    //" WHERE tc.id_fechamento_entrada NOT IN (SELECT id_fechamento_entrada FROM fin_transferencia_caixa WHERE id_caixa_saida = "+id_caixa+" AND id_status = 12) " +
                    " WHERE tc.id_fechamento_entrada NOT IN (SELECT id_fechamento_saida FROM fin_transferencia_caixa WHERE id_caixa_saida = "+id_caixa+" AND id_status = 12) " +
                    " GROUP BY tc.id_caixa_entrada, " +
                    "          tc.id_fechamento_entrada, " +
                    "          fc.nr_valor_fechamento, " +
                    "          fc.nr_valor_informado, " +
                    "          fc.dt_data, " +
                    "          fc.ds_hora " +
                    "UNION " +
                    "SELECT 	b.id_caixa, " +
                    "        b.id_fechamento_caixa, " +
                    "        fc.nr_valor_fechamento, " +
                    "        fc.nr_valor_informado, " +
                    "        fc.dt_data, " +
                    "        fc.ds_hora   " +
                    "  FROM fin_fechamento_caixa fc  " +
                    " INNER JOIN fin_baixa b ON b.id_caixa = "+id_caixa+" AND b.id_fechamento_caixa = fc.id " +
                    //" WHERE b.id_fechamento_caixa NOT IN (SELECT id_fechamento_entrada FROM fin_transferencia_caixa WHERE id_caixa_saida = "+id_caixa+" AND id_status = 12) " +
                    " WHERE b.id_fechamento_caixa NOT IN (SELECT id_fechamento_saida FROM fin_transferencia_caixa WHERE id_caixa_saida = "+id_caixa+" AND id_status = 12) " +
                    " GROUP BY b.id_caixa, " +
                    "          b.id_fechamento_caixa, " +
                    "          fc.nr_valor_fechamento, " +
                    "          fc.nr_valor_informado, " +
                    "          fc.dt_data, " +
                    "          fc.ds_hora";            
            Query qry = getEntityManager().createNativeQuery(text);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }
    
    @Override
    public List listaFechamentoCaixa(int id_caixa) {
        String text = 
                    "SELECT  tc.id_caixa_entrada, " +
                    "        tc.id_fechamento_entrada, " +
                    "        fc.nr_valor_fechamento, " +
                    "        fc.nr_valor_informado, " +
                    "        fc.dt_data, " +
                    "        fc.ds_hora  " +
                    "  FROM fin_fechamento_caixa fc  " +
                    " INNER JOIN fin_transferencia_caixa tc ON tc.id_fechamento_entrada = fc.id AND tc.id_caixa_entrada = " + id_caixa +
                    " GROUP BY tc.id_caixa_entrada, " +
                    "          tc.id_fechamento_entrada, " +
                    "          fc.nr_valor_fechamento, " +
                    "          fc.nr_valor_informado, " +
                    "          fc.dt_data, " +
                    "          fc.ds_hora " +
                    "UNION " +
                    "SELECT 	b.id_caixa, " +
                    "        b.id_fechamento_caixa, " +
                    "        fc.nr_valor_fechamento, " +
                    "        fc.nr_valor_informado, " +
                    "        fc.dt_data, " +
                    "        fc.ds_hora   " +
                    "  FROM fin_fechamento_caixa fc  " +
                    " INNER JOIN fin_baixa b ON b.id_caixa = "+id_caixa+" AND b.id_fechamento_caixa = fc.id " +
                    " GROUP BY b.id_caixa, " +
                    "          b.id_fechamento_caixa, " +
                    "          fc.nr_valor_fechamento, " +
                    "          fc.nr_valor_informado, " +
                    "          fc.dt_data, " +
                    "          fc.ds_hora " +
                    " ORDER BY 5 desc, 6 desc";
                
        try {
            Query qry = getEntityManager().createNativeQuery(text);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public List<TransferenciaCaixa> listaTransferencia(int id_fechamento_caixa) {
        try {
            
            
            Query qry = getEntityManager().createNativeQuery(
                    "SELECT tc.id " +
                    "  FROM fin_transferencia_caixa tc " +
                    " WHERE (tc.id_fechamento_entrada = " + id_fechamento_caixa +" OR tc.id_fechamento_saida = "+ id_fechamento_caixa+")"
            );
            
            List<Vector> lista = qry.getResultList();
            List<TransferenciaCaixa> result = new ArrayList();
            
            for (int i = 0; i < lista.size(); i++){
                result.add((TransferenciaCaixa)(new SalvarAcumuladoDBToplink()).pesquisaCodigo((Integer) lista.get(i).get(0), "TransferenciaCaixa"));
            }
//            qry = getEntityManager().createQuery(
//                    "SELECT tc " +
//                    "  FROM TransferenciaCaixa tc " +
//                    " WHERE tc.fechamentoEntrada.id = " + id_fechamento_caixa +" OR tc.fechamentoSaida.id = "+ id_fechamento_caixa
//            );
            return result;
        } catch (Exception e) {
            return new ArrayList();
        }
    }    
    
    @Override
    public Caixa pesquisaCaixaUm() {
        try {
            Query qry = getEntityManager().createQuery("select c from Caixa c where c.caixa = 1");
            qry.setMaxResults(1);
            return (Caixa)qry.getSingleResult();
        } catch (Exception e) {
            return new Caixa();
        }
    }
    
    @Override
    public List<Vector> listaDeCheques(int id_status) {
        try {
            Query qry = getEntityManager().createNativeQuery(
                    "SELECT c.id, func_idBaixa_cheque_rec(c.id) as id_baixa, ds_banco, ds_agencia, ds_conta, ds_cheque, dt_emissao, dt_vencimento, f.nr_valor " +
                    "  FROM fin_cheque_rec as c " +
                    " INNER JOIN fin_forma_pagamento as f on f.id_baixa = func_idBaixa_cheque_rec(c.id) AND f.id_cheque_rec = c.id " +
                    " WHERE id_status = " +id_status +
                    "   AND dt_vencimento <= now()"
            );
//            Query qry = getEntityManager().createNativeQuery(
//                    "SELECT c.id, func_idBaixa_cheque_rec(c.id) as id_baixa, ds_banco, ds_agencia, ds_conta, ds_cheque, dt_emissao, dt_vencimento, f.nr_valor " +
//                    "  FROM fin_cheque_rec as c " +
//                    " INNER JOIN fin_forma_pagamento as f on f.id_baixa = func_idBaixa_cheque_rec(c.id) AND f.id_cheque_rec = c.id " +
//                    " WHERE id_status in (7, 8, 9, 10, 11)" +
//                    "   AND dt_vencimento <= now()"
//            );
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }
    
    @Override
    public List<Vector> listaMovimentoBancario(int id_plano5) {
        try {
            Query qry = getEntityManager().createNativeQuery(
                    "SELECT DISTINCT f.id id_forma, b.id id_baixa, b.dt_baixa as data, '' as documento, '' as historico, f.nr_valor, m.ds_es, 0.0 as saldo, ds_descricao as status, f.id_tipo_pagamento, ch.id" +
                    "  FROM fin_lote as l " +
                    " INNER JOIN fin_movimento as m ON m.id_lote = l.id " +
                    " INNER JOIN fin_baixa as b ON b.id = m.id_baixa " +
                    " INNER JOIN fin_forma_pagamento as f ON f.id_baixa = b.id " +
                    "  LEFT JOIN fin_cheque_rec as ch ON ch.id = f.id_cheque_rec " +
                    "  LEFT JOIN fin_status as s ON s.id = ch.id_status " +
                    " WHERE f.id_plano5 = " +id_plano5+
                    " ORDER BY 3 desc"
//                    "SELECT b.dt_baixa as data, l.ds_documento, l.ds_historico, f.nr_valor, m.ds_es, 0.0 as saldo, ds_descricao as status " +
//                    "  FROM fin_lote as l" +
//                    " INNER JOIN fin_movimento as m on m.id_lote = l.id" +
//                    " INNER JOIN fin_baixa as b on b.id = m.id_baixa" +
//                    " INNER JOIN fin_forma_pagamento as f on f.id_baixa = b.id" +
//                    "  LEFT JOIN fin_cheque_rec as ch on ch.id = f.id_cheque_rec" +
//                    "  LEFT JOIN fin_status as s on s.id = ch.id_status" +
//                    " WHERE f.id_plano5 = " +id_plano5+
//                    " ORDER BY dt_baixa"
            );
            return qry.getResultList();
            
        } catch (Exception e) {
            return new ArrayList();
        }
    }
    
    @Override
    public List<TransferenciaCaixa> listaTransferenciaDinheiro(int id_fechamento_caixa, int id_caixa) {
        try {
            Query qry = getEntityManager().createQuery(
                    "SELECT tc "
                  + "  FROM TransferenciaCaixa tc "
                  + " WHERE tc.fechamentoEntrada.id = " +id_fechamento_caixa
                  + "   AND tc.caixaEntrada.id = " + id_caixa
            );
            return qry.getResultList();
            
        } catch (Exception e) {
            return new ArrayList();
        }
    }
    
    @Override
    public List<TransferenciaCaixa> listaTransferenciaDinheiroEntrada(int id_fechamento_caixa, int id_caixa) {
        try {
            Query qry = getEntityManager().createQuery(
                    "SELECT tc "
                  + "  FROM TransferenciaCaixa tc "
                  + " WHERE tc.fechamentoEntrada.id = " +id_fechamento_caixa
                  + "   AND tc.caixaEntrada.id = " + id_caixa
            );
            return qry.getResultList();
            
        } catch (Exception e) {
            return new ArrayList();
        }
    }
    
    @Override
    public List<TransferenciaCaixa> listaTransferenciaDinheiroSaida(int id_fechamento_caixa, int id_caixa) {
        try {
            Query qry = getEntityManager().createQuery(
                    "SELECT tc "
                  + "  FROM TransferenciaCaixa tc "
                  + " WHERE tc.fechamentoSaida.id = " +id_fechamento_caixa
                  + "   AND tc.caixaSaida.id = " + id_caixa
            );
            return qry.getResultList();
            
        } catch (Exception e) {
            return new ArrayList();
        }
    }
    
    @Override
    public List<FormaPagamento> listaTransferenciaFormaPagamento(int id_fechamento_caixa, int id_caixa, String es) {
        try {
            Query qry = getEntityManager().createQuery(
                    "SELECT fp "
                  + "  FROM FormaPagamento fp "
                  + " WHERE fp.baixa.id IN ( "
                  + "   SELECT m.baixa.id FROM Movimento m WHERE m.baixa.caixa.id = "+id_caixa+" AND m.baixa.fechamentoCaixa.id = "+id_fechamento_caixa + " AND m.es = '"+es+"'"
                  //+ "   SELECT b.id FROM Baixa b WHERE b.caixa.id = "+id_caixa+" AND b.fechamentoCaixa.id = "+id_fechamento_caixa
                  + " ) "
            );
            return qry.getResultList();
            
        } catch (Exception e) {
            return new ArrayList();
        }
    }
    
    @Override
    public List<Vector> pesquisaSaldoAtual(int id_caixa) {
        try {
            Query qry = getEntityManager().createNativeQuery(
                    "	SELECT max(fc.id) as id, fc.nr_saldo_atual as valor, fc.dt_data as data	" +
                    "	  FROM fin_fechamento_caixa fc " +
                    "	 INNER JOIN fin_baixa b ON b.id_fechamento_caixa = fc.id " +
                    "	 WHERE b.id_caixa = " + id_caixa +
                    "	 GROUP BY fc.id " +
                    " UNION " +
                    "	SELECT max(fc.id) as id, fc.nr_saldo_atual as valor, fc.dt_data as data " +
                    "	  FROM fin_fechamento_caixa fc " +
                    "	 INNER JOIN fin_transferencia_caixa tc ON tc.id_fechamento_entrada = fc.id " +
                    "	 WHERE tc.id_caixa_entrada = " +id_caixa +
                    "	 GROUP BY fc.id " +
                    "	 ORDER BY 1 DESC LIMIT 1"
//                    "select max(x.id), sum(x.valor) from " +
//                    "	( " +
//                    "	SELECT max(fc.id) as id, fc.nr_saldo_atual as valor" +
//                    "	  FROM fin_fechamento_caixa fc" +
//                    "	 INNER JOIN fin_baixa b ON b.id_fechamento_caixa = fc.id" +
//                    "	 WHERE b.id_caixa = " + id_caixa +
//                    "	 GROUP BY fc.id " +
//                    " UNION " +
//                    "	SELECT max(fc.id) as id, fc.nr_saldo_atual as valor" +
//                    "	  FROM fin_fechamento_caixa fc" +
//                    "	 INNER JOIN fin_transferencia_caixa tc ON tc.id_fechamento_entrada = fc.id" +
//                    "	 WHERE tc.id_caixa_entrada = " + id_caixa +
//                    "	 GROUP BY fc.id" +
//                    "	) as x"
            );
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }
    
    @Override
    public List<Vector> pesquisaSaldoAtualRelatorio(int id_caixa, int id_fechamento) {
        try {
            Query qry = getEntityManager().createNativeQuery(
                    "	SELECT max(fc.id) as id, fc.nr_saldo_atual as valor	" +
                    "	  FROM fin_fechamento_caixa fc " +
                    "	 INNER JOIN fin_baixa b ON b.id_fechamento_caixa = fc.id " +
                    "	 WHERE b.id_caixa = " + id_caixa + " AND fc.id < " + id_fechamento +
                    "	 GROUP BY fc.id " +
                    " UNION " +
                    "	SELECT max(fc.id) as id, fc.nr_saldo_atual as valor " +
                    "	  FROM fin_fechamento_caixa fc " +
                    "	 INNER JOIN fin_transferencia_caixa tc ON tc.id_fechamento_entrada = fc.id " +
                    "	 WHERE tc.id_caixa_entrada = " +id_caixa + " AND fc.id < " + id_fechamento +
                    "	 GROUP BY fc.id " +
                    "	 ORDER BY 1 DESC LIMIT 1"
            );
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }
    
    @Override
    public List<Vector> pesquisaUsuarioFechamento(int id_fechamento) {
        try {
            Query qry = getEntityManager().createNativeQuery(
                    "SELECT p.ds_nome " +
                    "  FROM fin_baixa b " +
                    " INNER JOIN seg_usuario u on u.id = b.id_usuario " +
                    " INNER JOIN pes_pessoa p on p.id = u.id_pessoa " +
                    " WHERE id_fechamento_caixa  = " +id_fechamento+
                    " GROUP BY p.ds_nome "
            );
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }
    
    @Override
    public List<SubGrupoFinanceiro> listaSubGrupo(Integer id_grupo){
        try {
            
            Query qry = getEntityManager().createQuery(
                    "SELECT sgf " +
                    "  FROM SubGrupoFinanceiro sgf " +
                    (id_grupo != null ? " WHERE sgf.grupoFinanceiro.id = " + id_grupo : "") +
                    " ORDER BY sgf.descricao "
            );
            return qry.getResultList();
        }catch(Exception e){
            return new ArrayList();
        }   
    }
    
    @Override
    public List<Vector> listaBoletoSocioAgrupado(String responsavel, String lote, String data, String tipo, String documento) {
        
        String text_qry = "", where = "", inner_join ="";
        
        if (tipo.equals("fisica")){
            inner_join = " INNER JOIN pes_fisica f ON f.id_pessoa = b.codigo ";
        }else if (tipo.equals("juridica")){
            inner_join = " INNER JOIN pes_juridica j ON j.id_pessoa = b.codigo ";
        }
        
        where = " WHERE b.ativo = true";

//        // IF temporário... excluir
//        if (tipo.equals("fisica")){
//            where += " AND b.codigo IN (SELECT id FROM xextrato) ";
//        }
        
        // DOCUMENTO --
        if (!documento.isEmpty()){
//            if (tipo.equals("fisica")){
//                inner_join = " INNER JOIN pes_pessoa p ON p.id = f.id_pessoa ";
//            }else if (tipo.equals("juridica")){
//                inner_join = " INNER JOIN pes_pessoa p ON p.id = j.id_pessoa ";
//            }
            
            inner_join += " INNER JOIN pes_pessoa p ON p.id = b.codigo ";
            where += " AND p.ds_documento = '"+documento+"'";
        }
        
        // RESPONSAVEL --
        if (!responsavel.isEmpty()){
            responsavel = AnaliseString.normalizeLower(responsavel);
            where += " AND TRANSLATE(LOWER(b.responsavel)) like '%"+responsavel+"%'";
        }
        
        // LOTE --
        if (!lote.isEmpty()){
            where += " AND b.id_lote_boleto = "+Integer.valueOf(lote);
            
        }
        
        // DATA --
        if (!data.isEmpty()){
            where += " AND b.processamento = '"+data+"'";
        }
        
            text_qry = " SELECT b.nr_ctr_boleto, b.id_lote_boleto, b.responsavel, b.boleto, to_char(b.vencimento,'dd/MM/yyyy') as vencimento, to_char(b.processamento,'dd/MM/yyyy') as processamento, sum(b.valor) as valor, b.endereco_responsavel, b.codigo " +
                   "   FROM soc_boletos_vw b " + inner_join + where +
                   "  GROUP BY b.nr_ctr_boleto, b.id_lote_boleto, b.responsavel, b.boleto, b.vencimento, b.processamento, b.endereco_responsavel, b.codigo "+
                   "  ORDER BY b.responsavel, b.vencimento desc";
        
        try {
            Query qry = getEntityManager().createNativeQuery(text_qry);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
        
    }   
    
    @Override
    public List<Vector> listaBoletoSocioFisica(String nr_ctr_boleto) {
        try {
            Query qry = getEntityManager().createNativeQuery(
                    " SELECT " +
                    "       id_fin_lote, " + // 0
                    "       id_fin_movimento, " + // 1
                    "       nr_ctr_boleto, " + // 2
                    "       id_lote_boleto, " + // 3
                    "       processamento, " + // 4
                    "       codigo," + // 5
                    "       responsavel," + // 6
                    "       vencimento," + // 7
                    "       matricula," + // 8
                    "       grupo_categoria," + // 9
                    "       categoria," + // 10
                    "       servico," + // 11
                    "       id_beneficiario," + // 12
                    "       nome_beneficiario," + // 13
                    "       valor," + // 14
                    "       mensalidades_corrigidas," + // 15
                    "       mensagem_boleto," + // 16
                    "       banco," + // 17
                    "       agencia," + // 18
                    "       cedente," + // 19
                    "       boleto," + // 20
                    "       email," + // 21
                    "       nome_filial," + // 22
                    "       site_filial," + // 23
                    "       cnpj_filial," + // 24
                    "       tel_filial," + // 25
                    "       endereco_filial," + // 26
                    "       bairro_filial," + // 27
                    "       cidade_filial," + // 28
                    "       uf_filial," + // 29
                    "       cep_filial," + // 30
                    "       logradouro_responsavel," + // 31
                    "       endereco_responsavel," + // 32
                    "       cep_responsavel," + // 33
                    "       uf_responsavel," + // 34
                    "       cidade_responsavel," + // 35
                    "       informativo," + // 36
                    "       local_pagamento, " + // 37
                    "       vencimento_movimento, " + // 38
                    "       vencimento_boleto, " + // 39
                    "       vencimento_original_boleto " + // 40
                    "   FROM soc_boletos_vw " +
                    "  WHERE nr_ctr_boleto IN ('" + nr_ctr_boleto + "') " +
                    "  ORDER BY responsavel, nome_titular, vencimento_movimento, codigo, nome_beneficiario "
            );
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }    
    
    @Override
    public List<Vector> listaBoletoSocioJuridica(String nr_ctr_boleto) {
        try {
            String text_qry = 
                    " SELECT "+
                    "       0 as id_lotex, " +
                    "       0 as id_movimentox, " +
                    "       nr_ctr_boleto, " +
                    "       id_lote_boleto, " +
                    "       processamento, " +
                    "       codigo," +
                    "       responsavel," +
                    "       vencimento," +
                    "       matricula," +
                    "       grupo_categoria," +
                    "       categoria," +
                    "       ''," +
                    "       codigo_titular," +
                    "       nome_titular," +
                    "       SUM(valor_sem_acrescimo)," +
                    "       mensalidades_corrigidas," +
                    "       mensagem_boleto," +
                    "       banco," +
                    "       agencia," +
                    "       cedente," +
                    "       boleto," +
                    "       email," +
                    "       nome_filial," +
                    "       site_filial," +
                    "       cnpj_filial," +
                    "       tel_filial," +
                    "       endereco_filial," +
                    "       bairro_filial," +
                    "       cidade_filial," +
                    "       uf_filial," +
                    "       cep_filial," +
                    "       logradouro_responsavel," +
                    "       endereco_responsavel," +
                    "       cep_responsavel," +
                    "       uf_responsavel," +
                    "       cidade_responsavel," +
                    "       informativo," +
                    "       local_pagamento, " +
                    "       vencimento_movimento, " +
                    "       vencimento_boleto, " +
                    "       vencimento_original_boleto " +
                    "   FROM soc_boletos_vw" +
                    "  WHERE nr_ctr_boleto IN ('" + nr_ctr_boleto + "') " +
                    "  GROUP BY " +
                    //"       id_fin_lote, " +
                    //"       id_fin_movimento, " +
                    "       nr_ctr_boleto, " +
                    "       id_lote_boleto, " +
                    "       processamento, " +
                    "       codigo," +
                    "       responsavel," +
                    "       vencimento," +
                    "       matricula," +
                    "       grupo_categoria," +
                    "       categoria," +
                    //"       servico," +
                    "       codigo_titular," +
                    "       nome_titular," +
                    "       mensalidades_corrigidas," +
                    "       mensagem_boleto," +
                    "       banco," +
                    "       agencia," +
                    "       cedente," +
                    "       boleto," +
                    "       email," +
                    "       nome_filial," +
                    "       site_filial," +
                    "       cnpj_filial," +
                    "       tel_filial," +
                    "       endereco_filial," +
                    "       bairro_filial," +
                    "       cidade_filial," +
                    "       uf_filial," +
                    "       cep_filial," +
                    "       logradouro_responsavel," +
                    "       endereco_responsavel," +
                    "       cep_responsavel," +
                    "       uf_responsavel," +
                    "       cidade_responsavel," +
                    "       informativo," +
                    "       local_pagamento, " +
                    "       vencimento_movimento, " +
                    "       vencimento_boleto, " +
                    "       vencimento_original_boleto " +
                    "  ORDER BY responsavel, nome_titular, vencimento_movimento, codigo, nome_titular ";
            
            Query qry = getEntityManager().createNativeQuery(text_qry);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }
    
    @Override
    public List<Vector> listaBoletoSocioJuridicaAgrupado(String nr_ctr_boleto) {
        try {
            String text_qry = 
                    " SELECT codigo " +
                    "   FROM soc_boletos_vw " +
                    "  WHERE nr_ctr_boleto IN ('" + nr_ctr_boleto + "') " +
                    "  GROUP BY codigo";
            
            Query qry = getEntityManager().createNativeQuery(text_qry);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }
    
    @Override
    public List<Vector> listaQntPorJuridica(String nrCtrBoleto){
        Query qry = getEntityManager().createNativeQuery(
                "select j.id_pessoa, m.id_titular " +
                "  from fin_movimento m " +
                " inner join pes_juridica j on j.id_pessoa = m.id_pessoa " +
                " where m.nr_ctr_boleto = '"+nrCtrBoleto+"' and m.is_ativo = true" +
                "  group by j.id_pessoa, m.id_titular"
        );
        try{
            return qry.getResultList();
        }catch(Exception e){
            e.getMessage();
        }
        return new ArrayList();
    }
    
    @Override
    public List<Vector> listaQntPorFisica(String nrCtrBoleto){
        Query qry = getEntityManager().createNativeQuery(
                "select f.id_pessoa, m.id_titular " +
                "  from fin_movimento m " +
                " inner join pes_fisica f on f.id_pessoa = m.id_pessoa " +
                " where m.nr_ctr_boleto = '"+nrCtrBoleto+"' and m.is_ativo = true" +
                "  group by f.id_pessoa, m.id_titular"
        );
        try{
            return qry.getResultList();
        }catch(Exception e){
            e.getMessage();
        }
        return new ArrayList();
    }
    
    @Override
    public List<Vector> listaServicosSemCobranca() {
        try {
            Query qry = getEntityManager().createNativeQuery(
                    "SELECT " +
                    "  se.id AS id_servico, " +
                    "  se.ds_descricao AS servico, " +
                    "  t.id AS id_tipo, " +
                    "  t.ds_descricao AS tipo " +
                    "  FROM fin_servicos AS se " +
                    " INNER JOIN fin_tipo_servico AS t ON t.id > 0 " +
                    " WHERE 's'||se.id||'t'||t.id NOT IN (SELECT 's'||id_servicos||'t'||id_tipo_servico FROM fin_servico_conta_cobranca) " +
                    "   AND se.id NOT IN (SELECT id_servicos FROM fin_servico_rotina WHERE id_rotina = 4) " +
                    "   AND se.id NOT IN (6,7,8,10,11) " +
                    " ORDER BY se.id, se.ds_descricao, t.id, t.ds_descricao "
            );
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }    
    
    @Override
    public List<Vector> listaPessoaSemComplemento(String referenciaVigoracao) {
        try {
            Query qry = getEntityManager().createNativeQuery(
                    "SELECT p.id, p.ds_nome "+
                    "  FROM pes_pessoa AS p "+
                    "  LEFT JOIN pes_pessoa_complemento AS c ON c.id_pessoa = p.id "+
                    "  LEFT JOIN fin_servico_pessoa AS sp ON sp.id_cobranca = p.id "+
                    " WHERE c.id IS NULL "+
                    "   AND (ds_ref_vigoracao = '' OR( ds_ref_vigoracao != '' AND "+
                    "                CAST(RIGHT(ds_ref_vigoracao, 4) || LEFT(ds_ref_vigoracao, 2) AS INT) <= "+
                    "                CAST(RIGHT('"+referenciaVigoracao+"', 4) || LEFT('"+referenciaVigoracao+"',2) AS INT) "+
                    "      ) "+
                    "   ) "+
                    "   AND (ds_ref_validade = '' OR( ds_ref_validade != '' AND "+
                    "                CAST(RIGHT(ds_ref_validade, 4) || LEFT(ds_ref_validade, 2) AS INT) > "+
                    "                CAST(RIGHT('"+referenciaVigoracao+"', 4) || LEFT('"+referenciaVigoracao+"', 2) AS INT) "+
                    "   ) "+
                    ") "+
                    " GROUP BY p.id, p.ds_nome "
            );
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }  
    
    @Override
    public List<Vector> listaRelatorioAnalitico(Integer id_fechamento_caixa) {
        try {
            String text = "SELECT " +
                    "cx.ds_descricao caixa, \n" +
                    "b.dt_baixa, \n" +
                    "b.id as lote_baixa, \n" +
                    "pu.ds_nome as operador, \n" +
                    "pr.ds_nome as responsavel, \n" +
                    "pt.ds_nome as titular, \n" +
                    "pb.ds_nome as beneficiario, \n" +
                    "se.ds_descricao as servico, \n" +
                    "m.ds_es AS operacao, \n" +
                    "func_es(m.ds_es,m.nr_valor) as valor, \n" +
                    "func_es(m.ds_es,m.nr_valor_baixa) valor_baixa, \n" +
                    "m.id, \n " +
                    "f.dt_data as fechamento \n " +
                    "from fin_movimento as m \n" +
                    "inner join fin_baixa as b on b.id=m.id_baixa \n" +
                    "inner join fin_caixa as cx on cx.id=b.id_caixa \n" +
                    "inner join fin_servicos as se on se.id=m.id_servicos \n" +
                    "inner join pes_pessoa as pr on pr.id=m.id_pessoa \n" +
                    "inner join pes_pessoa as pt on pt.id=m.id_titular \n" +
                    "inner join pes_pessoa as pb on pb.id=m.id_beneficiario \n" +
                    "inner join seg_usuario as u on u.id = b.id_usuario \n" +
                    "inner join pes_pessoa as pu on pu.id=u.id_pessoa \n " +
                    "inner join fin_fechamento_caixa as f on f.id = b.id_fechamento_caixa  \n " +
                    "where b.id_fechamento_caixa= "+id_fechamento_caixa+" \n" +
                    "\n" +
                    "---transferencia entrada \n" +
                    "union \n" +
                    "\n" +
                    "select \n" +
                    "cxe.ds_descricao as caixa, \n" +
                    "t.dt_lancamento as dt_baixa, \n" +
                    "null as lote_baixa, \n" +
                    "pu.ds_nome as operador, \n" +
                    "'' as responsavel, \n" +
                    "'' as titular, \n" +
                    "cxs.ds_descricao||' para '||cxe.ds_descricao  as beneficiario, \n" +
                    "'TRANSFERÊNCIA ENTRE CAIXAS' as servico, \n" +
                    "'E' AS operacao, \n" +
                    "t.nr_valor as valor, \n" +
                    "t.nr_valor as valor_baixa, \n" +
                    "0, \n" +
                    "f.dt_data as fechamento \n" +
                    "from fin_transferencia_caixa as t \n" +
                    "inner join fin_caixa as cxs on cxs.id=id_caixa_saida \n" +
                    "inner join fin_caixa as cxe on cxe.id=id_caixa_entrada \n" +
                    "inner join seg_usuario as u on u.id=t.id_usuario \n" +
                    "inner join pes_pessoa as pu on pu.id=u.id_pessoa \n" +
                    "inner join fin_fechamento_caixa as f on f.id = t.id_fechamento_entrada \n" +
                    "where t.id_fechamento_entrada="+id_fechamento_caixa+" \n" +
                    "\n" +
                    "---transferencia saida \n" +
                    "\n" +
                    "union \n" +
                    "\n" +
                    "select \n" +
                    "cxs.ds_descricao as caixa, \n" +
                    "t.dt_lancamento as dt_baixa, \n" +
                    "null as lote_baixa, \n" +
                    "pu.ds_nome as operador, \n" +
                    "'' as responsavel, \n" +
                    "'' as titular, \n" +
                    "cxs.ds_descricao||' para '||cxe.ds_descricao  as beneficiario, \n" +
                    "'TRANSFERÊNCIA ENTRE CAIXAS' as servico, \n" +
                    "'S' AS operacao, \n" +
                    "func_es('S',t.nr_valor) as valor, \n" +
                    "func_es('S',t.nr_valor) as valor_baixa, \n" +
                    "0, \n" +
                    "f.dt_data as fechamento \n" +
                    "from fin_transferencia_caixa as t \n" +
                    "inner join fin_caixa as cxs on cxs.id=id_caixa_saida \n" +
                    "inner join fin_caixa as cxe on cxe.id=id_caixa_entrada \n" +
                    "inner join seg_usuario as u on u.id=t.id_usuario \n" +
                    "inner join pes_pessoa as pu on pu.id=u.id_pessoa \n" +
                    "inner join fin_fechamento_caixa as f on f.id = t.id_fechamento_saida \n" +
                    "where t.id_fechamento_saida="+id_fechamento_caixa+" \n" +
                    "order by 3,4,5,6 ";
            
            Query qry = getEntityManager().createNativeQuery(text);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }    
    
    @Override
    public List<Vector> listaResumoFechamentoCaixa(String data) {
        try {
            Query qry = getEntityManager().createNativeQuery(
                    "select \n " +
                    "f.dt_data, \n " +
                    "m.ds_es AS operacao, \n " +
                    "g.ds_descricao as grupo, \n " +
                    "sg.ds_descricao as subgrupo, \n " +
                    "se.ds_descricao as servico, \n " +
                    "SUM(func_es(m.ds_es,m.nr_valor_baixa)) valor_baixa \n " +
                    "from fin_movimento as m \n " +
                    "inner join fin_baixa as b on b.id = m.id_baixa \n " +
                    "inner join fin_caixa as cx on cx.id = b.id_caixa \n " +
                    "inner join fin_servicos as se on se.id = m.id_servicos \n " +
                    "left  join fin_subgrupo as sg on sg.id = se.id_subgrupo \n " +
                    "left  join fin_grupo as g on g.id = sg.id_grupo \n " +
                    "inner join pes_pessoa as pr on pr.id = m.id_pessoa \n " +
                    "inner join pes_pessoa as pt on pt.id = m.id_titular \n " +
                    "inner join pes_pessoa as pb on pb.id = m.id_beneficiario \n " +
                    "inner join seg_usuario as u on u.id = b.id_usuario \n " +
                    "inner join pes_pessoa as pu on pu.id = u.id_pessoa \n " +
                    "inner join fin_fechamento_caixa as f on f.id = b.id_fechamento_caixa \n " +
                    "where f.dt_data='"+data+"' \n " +
                    "group by \n " +
                    "f.dt_data, \n " +
                    "m.ds_es, \n " +
                    "g.ds_descricao, \n " +
                    "sg.ds_descricao, \n " +
                    "se.ds_descricao \n " +
                    " \n " +
                    " ---transferencia entrada \n " +
                    "union \n" +
                    " \n " +
                    "select \n " +
                    "f.dt_data, \n " +
                    "'E', \n " +
                    "'', \n " +
                    "'', \n " +
                    "'TRANSFERÊNCIA ENTRE CAIXAS', \n " +
                    "SUM(func_es('E', t.nr_valor)) as valor_baixa \n " +
                    "from fin_transferencia_caixa as t \n " +
                    "inner join fin_caixa as cxs on cxs.id = id_caixa_saida \n " +
                    "inner join fin_caixa as cxe on cxe.id = id_caixa_entrada \n " +
                    "inner join seg_usuario as u on u.id = t.id_usuario \n " +
                    "inner join pes_pessoa as pu on pu.id = u.id_pessoa \n " +
                    "inner join fin_fechamento_caixa as f on f.id=t.id_fechamento_entrada \n " +
                    "where f.dt_data='"+data+"' \n " +
                    "group by f.dt_data \n " +
                    " \n " +
                    "---transferencia saida \n " +
                    " \n " +
                    "union \n " +
                    " \n " +
                    "select \n " +
                    " \n " +
                    "f.dt_data, \n " +
                    "'S', \n " +
                    "'', \n " +
                    "'', \n " +
                    "'TRANSFERÊNCIA ENTRE CAIXAS', \n " +
                    "SUM(func_es('S', t.nr_valor)) as valor_baixa \n " +
                    "from fin_transferencia_caixa as t \n " +
                    "inner join fin_caixa as cxs on cxs.id = id_caixa_saida \n " +
                    "inner join fin_caixa as cxe on cxe.id = id_caixa_entrada \n " +
                    "inner join seg_usuario as u on u.id = t.id_usuario \n" +
                    "inner join pes_pessoa as pu on pu.id = u.id_pessoa \n" +
                    "inner join fin_fechamento_caixa as f on f.id = t.id_fechamento_saida \n" +
                    "where f.dt_data='"+data+"' and f.id <> 1 \n" +
                    "group by f.dt_data \n " +
                    "order by 1,2,3,4,5 "
            );
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }    
    
//        NAO USA --- EXCLUIR DEPOIS DE 01/04/2015
//    @Override
//    public List<Vector> listaPessoaFisicaSemEndereco(int mes, int ano) {
//        try {
//            Query qry = getEntityManager().createNativeQuery(
//                    "SELECT m.id_pessoa, p.ds_nome " +
//                    "  FROM fin_movimento m " +
//                    " INNER JOIN pes_fisica f ON f.id_pessoa = m.id_pessoa " +
//                    " INNER JOIN pes_pessoa p ON p.id = f.id_pessoa " +
//                    " WHERE EXTRACT(MONTH FROM dt_vencimento) = " + mes +
//                    "   AND EXTRACT(YEAR FROM dt_vencimento) = " + ano +
//                    "   AND m.is_ativo = true " +
//                    "   AND m.id_pessoa NOT IN ( " +
//                    "       SELECT f.id_pessoa FROM soc_boletos_vw b " +
//                    "       INNER JOIN pes_fisica f ON f.id_pessoa = b.codigo " +
//                    "   )"
//            );
//            return qry.getResultList();
//        } catch (Exception e) {
//            return new ArrayList();
//        }
//    }
//    
//    @Override
//    public List<Vector> listaPessoaJuridicaSemEndereco(int mes, int ano) {
//        try {
//            Query qry = getEntityManager().createNativeQuery(
//                    "SELECT m.id_pessoa, p.ds_nome " +
//                    "  FROM fin_movimento m " +
//                    " INNER JOIN pes_juridica j ON j.id_pessoa = m.id_pessoa " +
//                    " INNER JOIN pes_pessoa p ON p.id = j.id_pessoa " +
//                    " WHERE EXTRACT(MONTH FROM m.dt_vencimento) = " + mes +
//                    "   AND EXTRACT(YEAR FROM m.dt_vencimento) = " + ano +
//                    "   AND m.is_ativo = true " +
//                    "   AND m.id_pessoa NOT IN ( " +
//                    "       SELECT f.id_pessoa FROM soc_boletos_vw b " +
//                    "	INNER JOIN pes_juridica f ON f.id_pessoa = b.codigo " +
//                    "   ) " +
//                    " GROUP BY m.id_pessoa, p.ds_nome "
//            );
//            return qry.getResultList();
//        } catch (Exception e) {
//            return new ArrayList();
//        }
//    }
    
    @Override
    public List<LoteBoleto> listaLoteBoleto(){
        String text = "SELECT lb.* \n" +
                      "  FROM soc_lote_boleto lb \n" +
                      " -- INNER JOIN soc_boletos_vw b ON b.id_lote_boleto = lb.id \n" +
                      " -- WHERE b.ativo = true \n" +
                      " GROUP BY lb.id, lb.dt_processamento \n" +
                      " ORDER BY lb.id DESC LIMIT 50";
        
        Query qry = getEntityManager().createNativeQuery(text, LoteBoleto.class);
        
        try{
            List<LoteBoleto> result = qry.getResultList();
            return result;
        }catch(Exception e){
            e.getMessage();
        }
        return new ArrayList();
    }
    
    @Override
    public Caixa pesquisaCaixaUsuario(int id_usuario){
        String text = "SELECT c " +
                      "  FROM Caixa c " +
                      " WHERE c.usuario.id = "+id_usuario;
        
        Query qry = getEntityManager().createQuery(text);
        
        try{
            Caixa result = (Caixa) qry.getSingleResult();
            return result;
        }catch(Exception e){
            e.getMessage();
        }
        return null;
    }
    
    @Override
    public List<Vector> listaFechamentoCaixaGeral(){
        String text = 
                    "SELECT \n" +
                    " caixa, \n" +
                    " dt_fechamento, \n" +
                    " hora_fechamento, \n" +
                    " dt_transferencia, \n" +
                    " sum(valor) as valor, \n" +
                    " id_fechamento_caixa, \n" +
                    " id_caixa \n" +
                    "  FROM  fin_fecha_caixa_geral_vw \n" +
                    " GROUP BY \n" +
                    " caixa, \n" +
                    " dt_fechamento, \n" +
                    " hora_fechamento, \n" +
                    " dt_transferencia, \n" +
                    " id_fechamento_caixa, \n" +
                    " id_caixa \n" +
                    " ORDER BY dt_fechamento, caixa ";
                
        try {
            Query qry = getEntityManager().createNativeQuery(text);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }
    
    @Override
    public List<Vector> listaDetalhesFechamentoCaixaGeral(Integer id_caixa, Integer id_fechamento){
        String text;
        
        if ( id_fechamento != null ){
            text = "SELECT operador, sum(valor) FROM fin_fecha_caixa_geral_vw WHERE id_caixa = "+id_caixa+" AND id_fechamento_caixa = "+id_fechamento+" GROUP BY operador ";
        }else{
            text = "SELECT operador, sum(valor) FROM fin_fecha_caixa_geral_vw WHERE id_caixa = "+id_caixa+" AND id_fechamento_caixa IS NULL GROUP BY operador ";
        }
                
        try {
            Query qry = getEntityManager().createNativeQuery(text);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }
}