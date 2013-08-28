package br.com.rtools.movimento;

//
//package br.com.rtools.classeOperacao;
//
//import br.com.rtools.associativo.CategoriaDesconto;
//import br.com.rtools.associativo.MovimentoResponsavel;
////import br.com.rtools.associativo.Responsavel;
//import br.com.rtools.associativo.Socios;
//import br.com.rtools.associativo.db.CategoriaDescontoDB;
//import br.com.rtools.associativo.db.CategoriaDescontoDBToplink;
////import br.com.rtools.associativo.db.ResponsavelDB;
////import br.com.rtools.associativo.db.ResponsavelDBToplink;
//import br.com.rtools.associativo.db.SociosDB;
//import br.com.rtools.associativo.db.SociosDBToplink;
//import br.com.rtools.erro.ErroGeral;
//import br.com.rtools.erro.ErroMovimento;
//import br.com.rtools.erro.ErroPessoa;
//import br.com.rtools.financeiro.Lote;
//import br.com.rtools.financeiro.Movimento;
//import br.com.rtools.financeiro.ServicoPessoa;
//import br.com.rtools.financeiro.ServicoValor;
//import br.com.rtools.financeiro.Servicos;
//import br.com.rtools.financeiro.db.ContaCobrancaDB;
//import br.com.rtools.financeiro.db.ContaCobrancaDBToplink;
//import br.com.rtools.financeiro.db.FinanceiroDB;
//import br.com.rtools.financeiro.db.FinanceiroDBToplink;
//import br.com.rtools.financeiro.db.ServicoPessoaDB;
//import br.com.rtools.financeiro.db.ServicoPessoaDBToplink;
//import br.com.rtools.financeiro.db.ServicoValorDB;
//import br.com.rtools.financeiro.db.ServicoValorDBToplink;
//import br.com.rtools.financeiro.db.TipoServicoDB;
//import br.com.rtools.financeiro.db.TipoServicoDBToplink;
//import br.com.rtools.pessoa.Fisica;
//import br.com.rtools.pessoa.Pessoa;
//import br.com.rtools.pessoa.PessoaEmpresa;
//import br.com.rtools.pessoa.db.FilialDB;
//import br.com.rtools.pessoa.db.FilialDBToplink;
//import br.com.rtools.pessoa.db.FisicaDB;
//import br.com.rtools.pessoa.db.FisicaDBToplink;
//import br.com.rtools.pessoa.db.PessoaDB;
//import br.com.rtools.pessoa.db.PessoaDBToplink;
//import br.com.rtools.pessoa.db.PessoaEmpresaDB;
//import br.com.rtools.pessoa.db.PessoaEmpresaDBToplink;
//import br.com.rtools.seguranca.Rotina;
//import br.com.rtools.seguranca.db.RotinaDB;
//import br.com.rtools.seguranca.db.RotinaDBToplink;
//import br.com.rtools.utilitarios.DataHoje;
//import br.com.rtools.utilitarios.Moeda;
//import java.util.Date;
//import java.util.List;
//
//
//public class OperacaoSocio {
//
//    private List<ServicoPessoa> listaServicoPessoa;
//    private String mes;
//    private String ano;
//
//    public OperacaoSocio(String mes, String ano){
//        ServicoPessoaDB servicoPessoaDB = new ServicoPessoaDBToplink();
//        this.ano = ano;
//        this.mes = mes;
//        listaServicoPessoa = servicoPessoaDB.pesquisaTodosParaGeracao(this.mes + "/" + this.ano);
//    }
//
//    public OperacaoSocio(String mes, String ano, Pessoa pessoa){
//        ServicoPessoaDB servicoPessoaDB = new ServicoPessoaDBToplink();
//        this.ano = ano;
//        this.mes = mes;
//        listaServicoPessoa = servicoPessoaDB.pesquisaTodosParaGeracao(this.mes + "/" + this.ano, pessoa.getId());
//    }
//
//    public OperacaoSocio(String referencia, Pessoa pessoa){
//        ServicoPessoaDB servicoPessoaDB = new ServicoPessoaDBToplink();
//        this.ano = referencia.substring(0, 2);
//        this.mes = referencia.substring(3, 5);
//        listaServicoPessoa = servicoPessoaDB.pesquisaTodosParaGeracao(this.mes + "/" + this.ano, pessoa.getId());
//    }
//
//    public String geracaoMovimento(){
//        String mensagem = "";
//        ServicoValorDB servicoValorDB = new ServicoValorDBToplink();
//        FisicaDB fisicaDB = new FisicaDBToplink();
//        SociosDB sociosDB = new SociosDBToplink();
//        FilialDB filDB = new FilialDBToplink();
//        FinanceiroDB financeiroDB = new FinanceiroDBToplink();
//        RotinaDB rotinaDB = new RotinaDBToplink();
//        TipoServicoDB tipodb = new TipoServicoDBToplink();
//        ContaCobrancaDB contaCobrancaDB = new ContaCobrancaDBToplink();
//        PessoaDB pessoaDB = new PessoaDBToplink();
//        Rotina rotina = null;
//        Lote lote = null;
//        Pessoa pessoaG = new Pessoa();
//        Socios socio = new Socios();
//        Movimento movimento = new Movimento();
//        Fisica pessoa = new Fisica();
//        ServicoValor  servicoValor = new ServicoValor();
//
//        String vencimento = "";
//        ErroPessoa erroP = new ErroPessoa();
//        ErroGeral erroM = new ErroGeral();
//
//        if ((listaServicoPessoa != null) || (!listaServicoPessoa.isEmpty())){
//            erroP.criarErro(100, "SERVIÇO NÃO POSSUI VALOR");
//            erroP.criarErro(101, "NÃO É PESSOA FÍSICA");
//            erroP.criarErro(102, "ROTINA NÃO EXISTE");
//            erroM.criarErro(200, "MOVIMENTO JÁ EXISTE");
//            for(ServicoPessoa servicoPessoa : listaServicoPessoa){
//                pessoa = fisicaDB.pesquisaFisicaPorPessoa(servicoPessoa.getPessoa().getId());
//                if (pessoa != null){
//                    servicoValor = servicoValorDB.pesquisaServicoValorPorIdade(
//                            servicoPessoa.getServicos().getId(),
//                            (new DataHoje()).calcularIdade(pessoa.getDtNascimento())
//                            );
//                    if (servicoValor != null){
//                        servicoValor.setValor(
//                                Moeda.subtracaoValores(
//                                    servicoValor.getValor(),
//                                    Moeda.multiplicarValores(
//                                        servicoValor.getValor(),
//                                        servicoValor.getDescontoAteVenc() / 100
//                                    )
//                                )
//                        );
//                        socio = sociosDB.pesquisaSocioPorId(servicoPessoa.getId());
//                        servicoValor = this.correcaoPorcategoria(servicoValor, servicoPessoa.getServicos(), socio);
//                        vencimento = (new DataHoje()).mascararData(servicoPessoa.getNrDiaVencimento() + "/" + mes + "/" + ano);
//                        pessoaG = this.selecionaPessoa(pessoaG, servicoPessoa, pessoa);
//
//                        rotina = rotinaDB.pesquisaCodigo(118);
//
//                        if (!pessoaDB.pessoaSemMovimento(pessoaG.getId(), mes + "/" + ano, 1 , servicoPessoa.getServicos().getId() )){
//                            if ((rotina != null) && (vencimento != null)){
//                                try{
//                                    financeiroDB.abrirTransacao();
//                                    lote = new Lote(
//                                            -1,
//                                            rotina,
//                                            null,
//                                            vencimento
//                                            );
//                                    financeiroDB.acumularObjeto(lote);
////                                    movimento = new Movimento(
////                                            -1,
////                                            lote,
////                                            servicoPessoa.getTipoDocumento(),
////                                            servicoPessoa.getServicos().getPlano5Debito(),
////                                            null,
////                                            filDB.pesquisaCodigo(1),
////                                            pessoaG,
////                                            servicoPessoa.getServicos(),
////                                            null,
////                                            tipodb.pesquisaCodigo(1),
////                                            servicoPessoa.getContaCobranca(),
////                                            null,
////                                            servicoPessoa.getContaCobranca().getBoletoInicial(),
////                                            servicoValor.getValor(),
////                                            0,
////                                            1,
////                                            "D",
////                                            mes + "/" + ano,
////                                            vencimento
////                                            );
//                                   financeiroDB.acumularObjeto(movimento);
//                                   financeiroDB.acumularObjeto(this.criarMovimentoResponsavel(servicoPessoa, movimento));
////                                   servicoPessoa.setContaCobranca(contaCobrancaDB.pesquisaCodigo(servicoPessoa.getId()));
////                                   servicoPessoa.getContaCobranca().setBoletoInicial(Moeda.incremento((1 + servicoPessoa.getContaCobranca().getBoletoInicial() ) , "1"));
////                                   financeiroDB.update(servicoPessoa.getContaCobranca());
//                                   financeiroDB.comitarTransacao();
//                               }catch (Exception e){
//                                   financeiroDB.desfazerTransacao();
//                               }
//                            }else{
//                                erroP.adicionarObjetoEmErro(102, servicoPessoa.getPessoa());
//                            }
//                        }else{
//                            erroM.adicionarObjetoEmErro(200, pessoaG.getNome() + " - " +
//                                    servicoPessoa.getServicos().getDescricao() + ", " +
//                                    tipodb.pesquisaCodigo(1).getDescricao() + ", " +
//                                    mes + "/" + ano
//                                    );
//                        }
//                    }else{
//                        erroP.adicionarObjetoEmErro(100, servicoPessoa.getPessoa());
//                    }
//                }else{
//                    erroP.adicionarObjetoEmErro(101, servicoPessoa.getPessoa());
//                }
//            }
//        }else{
//            mensagem = "NÃO HÁ SÓCIOS ATIVOS PARA ESSA REFERÊNCIA\n";
//        }
//        if (erroP.is_ExisteErro(100)){
//            mensagem += erroP.capiturarErro(100);
//        }
//        if (erroP.is_ExisteErro(101)){
//            mensagem += erroP.capiturarErro(101);
//        }
//        if (erroP.is_ExisteErro(102)){
//            mensagem += erroP.capiturarErro(102);
//        }
//        if (erroM.is_ExisteErro(200)){
//            mensagem += erroM.capiturarErro(200);
//        }
//        if(mensagem.isEmpty()){
//            mensagem = "Movimento(s) gerado(s) com sucesso";
//        }
//        return mensagem;
//    }
//
//    public String geracaoMovimento(List<String> listaReferencia){
//        String mensagem = "";
//        ServicoValorDB servicoValorDB = new ServicoValorDBToplink();
//        FisicaDB fisicaDB = new FisicaDBToplink();
//        SociosDB sociosDB = new SociosDBToplink();
//        FilialDB filDB = new FilialDBToplink();
//        FinanceiroDB financeiroDB = new FinanceiroDBToplink();
//        RotinaDB rotinaDB = new RotinaDBToplink();
//        TipoServicoDB tipodb = new TipoServicoDBToplink();
//        ContaCobrancaDB contaCobrancaDB = new ContaCobrancaDBToplink();
//        PessoaDB pessoaDB = new PessoaDBToplink();
//        Rotina rotina = null;
//        Lote lote = null;
//        Pessoa pessoaG = null;
//        Socios socio = null;
//        Movimento movimento = null;
//        Fisica pessoa = null;
//        ServicoValor  servicoValor = null;
//
//        String vencimento = "";
//        ErroPessoa erroP = new ErroPessoa();
//        ErroGeral erroM = new ErroGeral();
//
//        if ((listaServicoPessoa != null) || (!listaServicoPessoa.isEmpty())){
//            erroP.criarErro(100, "SERVIÇO NÃO POSSUI VALOR");
//            erroP.criarErro(101, "NÃO É PESSOA FÍSICA");
//            erroP.criarErro(102, "ROTINA NÃO EXISTE");
//            erroM.criarErro(200, "MOVIMENTO JÁ EXISTE");
//            for(ServicoPessoa servicoPessoa : listaServicoPessoa){
//                for (String referencia : listaReferencia){
//                    pessoa = fisicaDB.pesquisaFisicaPorPessoa(servicoPessoa.getPessoa().getId());
//                    if (pessoa != null){
//                        servicoValor = servicoValorDB.pesquisaServicoValorPorIdade(
//                                servicoPessoa.getServicos().getId(),
//                                (new DataHoje()).calcularIdade(pessoa.getDtNascimento())
//                                );
//                        if (servicoValor != null){
//                            servicoValor.setValor(
//                                    Moeda.subtracaoValores(
//                                        servicoValor.getValor(),
//                                        Moeda.multiplicarValores(
//                                            servicoValor.getValor(),
//                                            servicoValor.getDescontoAteVenc() / 100
//                                        )
//                                    )
//                            );
//                            socio = sociosDB.pesquisaSocioPorId(servicoPessoa.getId());
//                            servicoValor = this.correcaoPorcategoria(servicoValor, servicoPessoa.getServicos(), socio);
//                            vencimento = (new DataHoje()).mascararData(servicoPessoa.getNrDiaVencimento() + "/" + referencia);
//                            pessoaG = this.selecionaPessoa(pessoaG, servicoPessoa, pessoa);
//
//                            rotina = rotinaDB.pesquisaCodigo(118);
//
//                            if (!pessoaDB.pessoaSemMovimento(pessoaG.getId(), referencia, 1 , servicoPessoa.getServicos().getId() )){
//                                if ((rotina != null) && (vencimento != null)){
//                                    try{
//                                        financeiroDB.abrirTransacao();
//                                        lote = new Lote(
//                                                -1,
//                                                rotina,
//                                                null,
//                                                vencimento
//                                                );
//                                        financeiroDB.acumularObjeto(lote);
////                                        movimento = new Movimento(
////                                                -1,
////                                                lote,
////                                                servicoPessoa.getTipoDocumento(),
////                                                servicoPessoa.getServicos().getPlano5Debito(),
////                                                null,
////                                                filDB.pesquisaCodigo(1),
////                                                pessoaG,
////                                                servicoPessoa.getServicos(),
////                                                null,
////                                                tipodb.pesquisaCodigo(1),
////                                                servicoPessoa.getContaCobranca(),
////                                                null,
////                                                servicoPessoa.getContaCobranca().getBoletoInicial(),
////                                                servicoValor.getValor(),
////                                                0,
////                                                1,
////                                                "D",
////                                                referencia,
////                                                vencimento
////                                                );
//                                       financeiroDB.acumularObjeto(movimento);
//                                       financeiroDB.acumularObjeto(this.criarMovimentoResponsavel(servicoPessoa, movimento));
////                                       servicoPessoa.setContaCobranca(contaCobrancaDB.pesquisaCodigo(servicoPessoa.getId()));
////                                       servicoPessoa.getContaCobranca().setBoletoInicial(Moeda.incremento((1 + servicoPessoa.getContaCobranca().getBoletoInicial() ) , "1"));
////                                       financeiroDB.update(servicoPessoa.getContaCobranca());
//                                       financeiroDB.comitarTransacao();
//                                   }catch (Exception e){
//                                       financeiroDB.desfazerTransacao();
//                                   }
//                                }else{
//                                    erroP.adicionarObjetoEmErro(102, servicoPessoa.getPessoa());
//                                }
//                            }else{
//                                erroM.adicionarObjetoEmErro(200, pessoaG.getNome() + " - " +
//                                        servicoPessoa.getServicos().getDescricao() + ", " +
//                                        tipodb.pesquisaCodigo(1).getDescricao() + ", " +
//                                        referencia
//                                        );
//                            }
//                        }else{
//                            erroP.adicionarObjetoEmErro(100, servicoPessoa.getPessoa());
//                        }
//                    }else{
//                        erroP.adicionarObjetoEmErro(101, servicoPessoa.getPessoa());
//                    }
//                }
//            }
//        }else{
//            mensagem = "NÃO HÁ SÓCIOS ATIVOS PARA ESSA REFERÊNCIA\n";
//        }
//        if (erroP.is_ExisteErro(100)){
//            mensagem += erroP.capiturarErro(100);
//        }
//        if (erroP.is_ExisteErro(101)){
//            mensagem += erroP.capiturarErro(101);
//        }
//        if (erroP.is_ExisteErro(102)){
//            mensagem += erroP.capiturarErro(102);
//        }
//        if (erroM.is_ExisteErro(200)){
//            mensagem += erroM.capiturarErro(200);
//        }
//        if(mensagem.isEmpty()){
//            mensagem = "Movimento(s) gerado(s) com sucesso";
//        }
//        return mensagem;
//    }
//
//    private ServicoValor correcaoPorcategoria(ServicoValor servicoValor, Servicos servico, Socios socio){
//        CategoriaDescontoDB categoriaDescontoDB = new CategoriaDescontoDBToplink();
//        CategoriaDesconto categoriaDesconto = null;
//        if (socio != null){
//            categoriaDesconto = categoriaDescontoDB.pesquisaTodosPorServicoCategoria(servico.getId(), socio.getMatriculaSocios().getCategoria().getId());
//            if (categoriaDesconto != null){
//                servicoValor.setValor(
//                        Moeda.subtracaoValores(
//                            servicoValor.getValor(),
//                            Moeda.multiplicarValores(
//                                servicoValor.getValor(),
//                                categoriaDesconto.getDesconto() / 100
//                            )
//                        )
//                );
//            }
//        }
//        return servicoValor;
//    }
//
//    private Pessoa selecionaPessoa(Pessoa pessoaG, ServicoPessoa servicoPessoa, Fisica pessoa){
//        PessoaEmpresaDB pessoaEmpresaDB = new PessoaEmpresaDBToplink();
//        PessoaEmpresa pessoaEmpresa = null;
//        pessoaEmpresa = pessoaEmpresaDB.pesquisaPessoaEmpresaPorFisica(pessoa.getId());
//        if ((servicoPessoa.isDescontoFolha()) && (pessoaEmpresa != null)){
//            pessoaG = pessoaEmpresa.getJuridica().getPessoa();
//           // pessoaG = pessoaDB.pesquisaCodigo(pessoaG.getId());
//        }else{
//            pessoaG = buscaResponsavel(servicoPessoa);
//           // pessoaG = pessoaDB.pesquisaCodigo(pessoaG.getId());
//        }
//        return pessoaG;
//    }
//
//    private Pessoa buscaResponsavel(ServicoPessoa servicoPessoa){
////        Responsavel responsavel = new Responsavel();
//        ServicoPessoaDB servicoPessoaDB = new ServicoPessoaDBToplink();
////        responsavel = servicoPessoaDB.buscaResponsavel(servicoPessoa.getId());
////        if (responsavel != null){
////            return responsavel.getTitular();
////        }else{
////            return servicoPessoa.getPessoa();
////        }
//        return null;
//    }
//
//    private MovimentoResponsavel criarMovimentoResponsavel(ServicoPessoa servicoPessoa, Movimento movimento){
////        Responsavel responsavel = new Responsavel();
//        ServicoPessoaDB servicoPessoaDB = new ServicoPessoaDBToplink();
////        responsavel = servicoPessoaDB.buscaResponsavel(servicoPessoa.getId());
//        MovimentoResponsavel movimentoResponsavel = new MovimentoResponsavel();
//        movimentoResponsavel.setMovimento(movimento);
//
////        if (responsavel != null){
////            movimentoResponsavel.setTitular(servicoPessoa.getPessoa());
////            movimentoResponsavel.setBeneficiario(servicoPessoa.getPessoa());
////        }else{
////            if (responsavel.getTitular().getId() == servicoPessoa.getPessoa().getId()){
////                movimentoResponsavel.setTitular(servicoPessoa.getPessoa());
////                movimentoResponsavel.setBeneficiario(servicoPessoa.getPessoa());
////            }else{
////                movimentoResponsavel.setTitular(responsavel.getTitular());
////                movimentoResponsavel.setBeneficiario(servicoPessoa.getPessoa());
////            }
////        }
//        return movimentoResponsavel;
//    }
//
//
//
//}
