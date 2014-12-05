package br.com.rtools.financeiro.db;

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
            return new ArrayList<Movimento>();
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
            return new ArrayList<BloqueiaServicoPessoa>();
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
            return new ArrayList<Caixa>();
        }
    }
    
    @Override
    public List listaMovimentoCaixa(int id_caixa, String es) {
        try {
            Query qry = getEntityManager().createNativeQuery("select distinct(tp.id), " +
                                                        "       m.ds_es, " +
                                                        "	b.dt_baixa, " +
                                                        "	b.id_caixa, " +
                                                        "	p.ds_nome, " +
                                                        "	tp.ds_descricao, " +
                                                        "	f.nr_valor, " +
                                                        "       cx.id_filial, " +
                                                        "       b.id " +
                                                        " from fin_forma_pagamento as f " +
                                                        " inner join fin_baixa as b on b.id=f.id_baixa " +
                                                        " inner join seg_usuario as u on u.id=b.id_usuario " +
                                                        " inner join pes_pessoa as p on p.id=u.id_pessoa " +
                                                        " inner join fin_movimento as m on m.id_baixa=b.id " +
                                                        " inner join fin_tipo_pagamento tp on tp.id = f.id_tipo_pagamento " +
                                                        " inner join fin_caixa as cx on cx.id = b.id_caixa " +
                                                        " where b.id_caixa = "+id_caixa+" and b.id_fechamento_caixa is null " +
                                                        "   and m.ds_es = '"+es+"'");
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }
    
    @Override
    public List<TransferenciaCaixa> listaTransferenciaEntrada(int id_caixa) {
        try {
            Query qry = getEntityManager().createQuery("SELECT tc FROM TransferenciaCaixa tc WHERE tc.caixaEntrada.id = "+id_caixa+" AND tc.fechamentoEntrada is null");
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }
    
    @Override
    public List<TransferenciaCaixa> listaTransferenciaSaida(int id_caixa) {
        try {
            Query qry = getEntityManager().createQuery("SELECT tc FROM TransferenciaCaixa tc WHERE tc.caixaSaida.id = "+id_caixa+" AND (tc.caixaEntrada.caixa <> 1) AND tc.fechamentoSaida is null");
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
                    " WHERE tc.id_fechamento_entrada NOT IN (SELECT id_fechamento_entrada FROM fin_transferencia_caixa WHERE id_caixa_saida = "+id_caixa+" AND id_status = 12) " +
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
                    " WHERE b.id_fechamento_caixa NOT IN (SELECT id_fechamento_entrada FROM fin_transferencia_caixa WHERE id_caixa_saida = "+id_caixa+" AND id_status = 12) " +
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
            return new ArrayList<Vector>();
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
            return new ArrayList<Vector>();
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
            return new ArrayList<Vector>();
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
            return new ArrayList<Vector>();
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
            return new ArrayList<Vector>();
        }
    }
    
    @Override
    public List<SubGrupoFinanceiro> listaSubGrupo(int id_grupo){
        try {
            
            Query qry = getEntityManager().createQuery(
                    "SELECT sgf " +
                    "  FROM SubGrupoFinanceiro sgf " +
                    " WHERE sgf.grupoFinanceiro.id = " +id_grupo+
                    " ORDER BY sgf.descricao "
            );
            return qry.getResultList();
        }catch(Exception e){
            return new ArrayList<SubGrupoFinanceiro>();
        }   
    }
    
    @Override
    public List<Vector> listaBoletoSocioAgrupado(String responsavel, String lote, String data) {
        
        String text_qry = "";
        String where = "";
        if (!responsavel.isEmpty())
            where = " WHERE UPPER(responsavel) like '%"+responsavel.toUpperCase()+"%'";
        
        if (!lote.isEmpty() && responsavel.isEmpty())
            where += " WHERE id_lote_boleto = "+Integer.valueOf(lote);
        else if (!lote.isEmpty())
            where += " AND id_lote_boleto = "+Integer.valueOf(lote);
            
        if (!data.isEmpty()  && responsavel.isEmpty() && lote.isEmpty())
            where += " WHERE processamento = '"+data+"'";
        else if (!data.isEmpty())
            where += " AND processamento = '"+data+"'";
            
        text_qry = "SELECT nr_ctr_boleto, id_lote_boleto, responsavel, boleto, to_char(vencimento,'dd/MM/yyyy') as vencimento, to_char(processamento,'dd/MM/yyyy') as processamento, sum(valor) as valor " +
                   "  FROM soc_boletos_vw " + where +
                   " GROUP BY nr_ctr_boleto, id_lote_boleto, responsavel, boleto, vencimento, processamento "+
                  "  ORDER BY responsavel, vencimento desc";
        
        try {
            Query qry = getEntityManager().createNativeQuery(text_qry);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList<Vector>();
        }
        
    }   
    
    @Override
    public List<Vector> listaBoletoSocio(String nr_ctr_boleto) {
        try {
            Query qry = getEntityManager().createNativeQuery(
                    "SELECT * FROM soc_boletos_vw" +
                    "  WHERE nr_ctr_boleto IN ('" + nr_ctr_boleto + "')"
            );
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList<Vector>();
        }
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
            return new ArrayList<Vector>();
        }
    }    
    
}
