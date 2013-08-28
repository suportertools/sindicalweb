package br.com.rtools.movimento;

//
//package br.com.rtools.classeOperacao;
//
//
//import br.com.rtools.associativo.MovimentoResponsavel;
//import br.com.rtools.financeiro.ComplementoValor;
//import br.com.rtools.financeiro.Guia;
//import br.com.rtools.financeiro.Lote;
//import br.com.rtools.financeiro.LoteBaixa;
//import br.com.rtools.financeiro.Movimento;
//import br.com.rtools.financeiro.MovimentoPessoa;
//import br.com.rtools.financeiro.Plano5;
//import br.com.rtools.financeiro.db.FinanceiroDB;
//import br.com.rtools.financeiro.db.FinanceiroDBToplink;
//import br.com.rtools.lista.GGuia;
//import br.com.rtools.lista.Lista;
//import br.com.rtools.pessoa.Pessoa;
//import br.com.rtools.pessoa.db.FilialDB;
//import br.com.rtools.pessoa.db.FilialDBToplink;
//import br.com.rtools.pessoa.db.FisicaDB;
//import br.com.rtools.pessoa.db.FisicaDBToplink;
//import br.com.rtools.pessoa.db.PessoaDB;
//import br.com.rtools.pessoa.db.PessoaDBToplink;
//import br.com.rtools.seguranca.Rotina;
//import br.com.rtools.seguranca.Usuario;
//import br.com.rtools.seguranca.db.RotinaDB;
//import br.com.rtools.seguranca.db.RotinaDBToplink;
//import br.com.rtools.utilitarios.DataHoje;
//import java.util.ArrayList;
//import java.util.List;
//
//public class OperacaoGuia {
//
//    private Lista lista;
//    private List<Movimento> listaMovimento;
//
//    public OperacaoGuia(Lista listaGuia){
//        lista = listaGuia;
//        listaMovimento = new ArrayList<Movimento>();
//    }
//
//    public String debitarGuia(Pessoa pessoa, Pessoa beneficiaria, String dataString){
//        Rotina rotina = null;
//        Lote lote = null;
//        String mensagem = "";
//        GGuia guia = null;
//        String referencia = dataString.substring(3);
//        FilialDB filDB = new FilialDBToplink();
//        FinanceiroDB financeiroDB = new FinanceiroDBToplink();
//        RotinaDB rotinaDB = new RotinaDBToplink();
//        PessoaDB pessoaDB = new PessoaDBToplink();
//        FisicaDB fisicaDB = new FisicaDBToplink();
//
//        if (beneficiaria.getId() == -1){
//            beneficiaria = pessoaDB.pesquisaCodigo(0);
//        }
//
//        if (pessoa.getId() == -1){
//            pessoa = pessoaDB.pesquisaCodigo(0);
//        }
//
//        if(fisicaDB.pesquisaFisicaPorPessoa(pessoa.getId()) != null){
//            rotina = rotinaDB.pesquisaCodigo(132);
//            try{
//                financeiroDB.abrirTransacao();
//                int i = 0;
//                lote = new Lote(
//                    -1,
//                    rotina,
//                    null,
//                    dataString
//                );
//
//                financeiroDB.acumularObjeto(lote);
//                financeiroDB.acumularObjeto( new Guia(-1, lote, pessoa));
//
//                while(i < lista.size()){
//                    guia = (GGuia) lista.get(i);
//                    if (!pessoaDB.pessoaSemMovimento(pessoa.getId(), referencia, 1 , guia.getServico().getId() )){
//                        listaMovimento.add(new Movimento( -1,
//                                                            lote,
//                                                            guia.getServico(),
//                                                            guia.getValorLiquido(),
//                                                            guia.getQuantidade(),
//                                                            guia.getServico().getPlano5Debito(),
//                                                            filDB.pesquisaCodigo(1),
//                                                            pessoa,
//                                                            dataString,
//                                                            referencia,
//                                                            null,
//                                                            1,
//                                                            "D",
//                                                            null
//                                                  ));
//                        financeiroDB.acumularObjeto(listaMovimento.get(i));
//                        financeiroDB.acumularObjeto(new MovimentoResponsavel(-1, listaMovimento.get(i), pessoa, beneficiaria));
//                        financeiroDB.acumularObjeto(new ComplementoValor(-1, listaMovimento.get(i), 0,0,0, guia.getDesconto(), 0));
//                        if(pessoa.getId() == -1){
//                            financeiroDB.acumularObjeto(new MovimentoPessoa(-1, listaMovimento.get(i), pessoa.getNome()));
//                        }
//                    }
//                    i++;
//                }
//                financeiroDB.comitarTransacao();
//                mensagem = "Guia gerada com sucesso!";
//            }catch (Exception e){
//                mensagem = e.getMessage();
//                financeiroDB.desfazerTransacao();
//                listaMovimento = new ArrayList<Movimento>();
//            }
//        }else{
//            mensagem = "Não é pessoa física";
//        }
//        return mensagem;
//    }
//
//    public List<Movimento> getListaMovimento() {
//        return listaMovimento;
//    }
//
//    public void setListaMovimento(List<Movimento> listaMovimento) {
//        this.listaMovimento = listaMovimento;
//    }
//}
