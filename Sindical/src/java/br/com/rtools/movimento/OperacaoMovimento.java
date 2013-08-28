package br.com.rtools.movimento;

//package br.com.rtools.classeOperacao;
//
//import br.com.rtools.arrecadacao.Acordo;
//import br.com.rtools.arrecadacao.Convencao;
//import br.com.rtools.arrecadacao.FolhaEmpresa;
//import br.com.rtools.arrecadacao.MensagemConvencao;
//import br.com.rtools.arrecadacao.db.MensagemConvencaoDB;
//import br.com.rtools.arrecadacao.db.MensagemConvencaoDBToplink;
//import br.com.rtools.pessoa.Pessoa;
//import br.com.rtools.seguranca.Rotina;
//import br.com.rtools.arrecadacao.db.CnaeConvencaoDB;
//import br.com.rtools.arrecadacao.db.CnaeConvencaoDBToplink;
//import br.com.rtools.arrecadacao.db.FolhaEmpresaDB;
//import br.com.rtools.arrecadacao.db.FolhaEmpresaDBToplink;
//import br.com.rtools.arrecadacao.db.GrupoCidadesDB;
//import br.com.rtools.arrecadacao.db.GrupoCidadesDBToplink;
//import br.com.rtools.cobranca.BancoDoBrasil;
//import br.com.rtools.cobranca.Bradesco;
//import br.com.rtools.cobranca.CaixaFederalSicob;
//import br.com.rtools.cobranca.CaixaFederalSigCB;
//import br.com.rtools.cobranca.CaixaFederalSindical;
//import br.com.rtools.cobranca.Cobranca;
//import br.com.rtools.cobranca.Itau;
//import br.com.rtools.cobranca.Real;
//import br.com.rtools.cobranca.Santander;
//import br.com.rtools.erro.Erro;
//import br.com.rtools.erro.ErroGeral;
//import br.com.rtools.erro.ErroMovimento;
//import br.com.rtools.financeiro.MensagemCobranca;
//import br.com.rtools.financeiro.db.MovimentoDB;
//import br.com.rtools.financeiro.db.MovimentoDBToplink;
//import br.com.rtools.pessoa.PessoaEndereco;
//import br.com.rtools.pessoa.db.JuridicaDB;
//import br.com.rtools.pessoa.db.JuridicaDBToplink;
//import br.com.rtools.pessoa.db.PessoaEnderecoDB;
//import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
//import br.com.rtools.utilitarios.DataHoje;
//import br.com.rtools.utilitarios.Moeda;
//import br.com.rtools.impressao.ParametroBoleto;
//import java.util.ArrayList;
//import java.util.Collection;
//import javax.faces.context.FacesContext;
//import javax.servlet.ServletContext;
//import javax.servlet.ServletOutputStream;
//import javax.servlet.http.HttpServletResponse;
//import net.sf.jasperreports.engine.JasperExportManager;
//import net.sf.jasperreports.engine.JasperFillManager;
//import net.sf.jasperreports.engine.JasperPrint;
//import net.sf.jasperreports.engine.JasperReport;
//import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
//import net.sf.jasperreports.engine.util.JRLoader;
//import br.com.rtools.financeiro.ComplementoValor;
//import br.com.rtools.financeiro.ContaCobranca;
//import br.com.rtools.financeiro.Correcao;
//import br.com.rtools.financeiro.FTipoDocumento;
//import br.com.rtools.financeiro.ImpressaoWeb;
//import br.com.rtools.financeiro.IndiceMensal;
//import br.com.rtools.financeiro.Lote;
//import br.com.rtools.financeiro.LoteBaixa;
//import br.com.rtools.financeiro.Movimento;
//import br.com.rtools.financeiro.Plano5;
//import br.com.rtools.financeiro.Servicos;
//import br.com.rtools.financeiro.TipoServico;
//import br.com.rtools.financeiro.db.ContaCobrancaDB;
//import br.com.rtools.financeiro.db.ContaCobrancaDBToplink;
//import br.com.rtools.financeiro.db.CorrecaoDB;
//import br.com.rtools.financeiro.db.CorrecaoDBToplink;
//import br.com.rtools.financeiro.db.FinanceiroDB;
//import br.com.rtools.financeiro.db.FinanceiroDBToplink;
//import br.com.rtools.financeiro.db.LoteBaixaDB;
//import br.com.rtools.financeiro.db.LoteBaixaDBToplink;
//import br.com.rtools.financeiro.db.LoteDB;
//import br.com.rtools.financeiro.db.LoteDBToplink;
//import br.com.rtools.financeiro.db.Plano5DB;
//import br.com.rtools.financeiro.Historico;
//import br.com.rtools.financeiro.db.ContaBancoDB;
//import br.com.rtools.financeiro.db.ContaBancoDBToplink;
//import br.com.rtools.financeiro.db.ContaRotinaDB;
//import br.com.rtools.financeiro.db.ContaRotinaDBToplink;
//import br.com.rtools.financeiro.db.Plano5DBToplink;
//import br.com.rtools.financeiro.db.ServicosDB;
//import br.com.rtools.financeiro.db.ServicosDBToplink;
//import br.com.rtools.financeiro.db.TipoServicoDB;
//import br.com.rtools.financeiro.db.TipoServicoDBToplink;
//import br.com.rtools.impressao.DemonstrativoAcordo;
//import br.com.rtools.pessoa.Filial;
//import br.com.rtools.pessoa.Juridica;
//import br.com.rtools.pessoa.db.FilialDB;
//import br.com.rtools.pessoa.db.FilialDBToplink;
//import br.com.rtools.pessoa.db.PessoaDB;
//import br.com.rtools.pessoa.db.PessoaDBToplink;
//import br.com.rtools.seguranca.Departamento;
//import br.com.rtools.seguranca.Usuario;
//import br.com.rtools.seguranca.db.DepartamentoDB;
//import br.com.rtools.seguranca.db.DepartamentoDBToplink;
//import br.com.rtools.seguranca.db.RotinaDB;
//import br.com.rtools.seguranca.db.RotinaDBToplink;
//import br.com.rtools.utilitarios.SalvarAcumuladoDB;
//import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
//import java.math.BigDecimal;
//import java.math.MathContext;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//public class OperacaoMovimento {
//
//    private List<Movimento> lista;
//    private List<ComplementoValor> listaComplemento;
//    private float somatoria;
//    private float desconto;
//    private List<Float> listaDesconto;
//    private Erro mensagemCorrecao;
//    private List<Movimento> listaAcordo;
//    private Rotina rotina;
//    private String mensagemErroMovimento = "";
//
//    public OperacaoMovimento(List<Movimento> lista) {
//        setListaComplemento(new ArrayList<ComplementoValor>());
//        setSomatoria(0);
//        setDesconto(0);
//        setLista(lista);
//        setListaDesconto(new ArrayList<Float>());
//        setListaAcordo(new ArrayList<Movimento>());
//        RotinaDB movDB = new RotinaDBToplink();
//        this.rotina = movDB.pesquisaCodigo(4);
//    }
//
//    public OperacaoMovimento(List<Movimento> lista, float desconto) {
//        RotinaDB movDB = new RotinaDBToplink();
//        setListaComplemento(new ArrayList<ComplementoValor>());
//        setSomatoria(0);
//        setDesconto(desconto);
//        setLista(lista);
//        setListaAcordo(new ArrayList<Movimento>());
//        this.rotina = movDB.pesquisaCodigo(4);
//    }
//
//    public synchronized String salvar(Object object, int id) {
//        FinanceiroDB db = new FinanceiroDBToplink();
//        if (id == -1) {
//            db.insert(object);
//        } else {
//            db.getEntityManager().getTransaction().begin();
//            if (db.update(object)) {
//                db.getEntityManager().getTransaction().commit();
//            } else {
//                db.getEntityManager().getTransaction().rollback();
//            }
//        }
//        return null;
//    }
//
//    public List<Movimento> getLista() {
//        return lista;
//    }
//
//    public void setLista(List<Movimento> lista) {
//        this.lista = lista;
//        //listaComplemento.clear();
//    }
//
//    public List<String> gerarMovimento(Rotina rotina) {
//        PessoaDB pesDB = new PessoaDBToplink();
//        CnaeConvencaoDB dbConvencao = new CnaeConvencaoDBToplink();
//        GrupoCidadesDB dbGrupoCidades = new GrupoCidadesDBToplink();
//        MensagemConvencaoDB dbMen = new MensagemConvencaoDBToplink();
//        ContaCobrancaDB ctaCobraDB = new ContaCobrancaDBToplink();
//        ContaCobranca contaCobranca = null;
//        LoteDB loteDB = new LoteDBToplink();
//        MovimentoDB movDB = new MovimentoDBToplink();
//        boolean loteGerado = false;
//        Lote lote = new Lote();
//        int i = 0;
//        Movimento mov;
//        MensagemConvencao mensagemConvencao = null;
//        List<String> mensagem = new ArrayList<String>();
//        Pessoa pessoa;
//        Servicos servico;
//        TipoServico tipoServico;
//        String referencia;
//        Convencao convencao = null;
//        lote = new Lote(
//                -1,
//                rotina,
//                "R",
//                DataHoje.data());
//        loteGerado = loteDB.insert(lote);
//        if (!loteGerado) {
//            mensagem.add("Erro na geração do lote!");
//            return mensagem;
//        }
//
//        while (i < lista.size()) {
//            mensagem.add("Movimentos gerados com sucesso!");
//
//            if (lista.get(i) != null) {
//                if (lista.get(i).getLoteBaixa() != null) {
//                    mensagem.set(i, "Movimento já foi baixado!");
//                    i++;
//                    continue;
//                }
//            }
//
//            if (lista.get(i).getId() != -1) {
//                mensagem.set(i, "Movimento já foi gerado!");
//                salvar(lista.get(i), lista.get(i).getId());
//                i++;
//                continue;
//            }
//
//            if (lista.get(i).getValor() == 0) {
//                mensagem.set(i, "Valor do boleto não pode ser zero");
//                i++;
//                continue;
//            }
//
//            try {
//                pessoa = pesDB.pessoaPermitida(lista.get(i).getPessoa().getId());
//                if (pessoa == null) {
//                    mensagem.set(i, lista.get(i).getPessoa().getNome() + "esta com grupo de cidade não corespondente a convenção");
//                    i++;
//                    continue;
//                }
//            } catch (Exception e) {
//                mensagem.set(i, e.getMessage());
//                i++;
//                continue;
//            }
//
//            try {
//                servico = lista.get(i).getServicos();
//                tipoServico = lista.get(i).getTipoServico();
//                referencia = lista.get(i).getReferencia();
//            } catch (Exception e) {
//                mensagem.set(i, e.getMessage());
//                i++;
//                continue;
//            }
//
//            if (pesDB.pessoaSemMovimento(pessoa.getId(), referencia, tipoServico.getId(), servico.getId())) {
//                mensagem.set(i, "Já existe um movimento para " + pessoa.getNome() + " com esse servico, tipo e referência.");
//                i++;
//                continue;
//            }
//            if (lista.get(i).getContaCobranca() == null) {
//                mensagem.set(i, "Não existe conta cobrança para o tipo principal e esse serviço.");
//                i++;
//                continue;
//            }
//
//            convencao = dbConvencao.pesquisarCnaeConvencaoPorPessoa(lista.get(i).getPessoa().getId());
//
//            if (convencao == null) {
//                mensagem.set(i, "Não existe cnae vinculado com convenção.");
//                i++;
//                continue;
//            }
//            mensagemConvencao = dbMen.verificaMensagem(
//                    convencao.getId(),
//                    servico.getId(),
//                    tipoServico.getId(),
//                    dbGrupoCidades.grupoCidadesPorPessoa(lista.get(i).getPessoa().getId(), convencao.getId()).getId(),
//                    referencia);
//
//            if (mensagemConvencao == null || mensagemConvencao.getId() == -1) {
//                mensagem.set(i, "Não existe mensagem para esse boleto.");
//                i++;
//                continue;
//            }
//            lista.get(i).setLote(lote);
//            try {
//                contaCobranca = ctaCobraDB.pesquisaCodigo(lista.get(i).getContaCobranca().getId());
//                lista.get(i).setNumero(contaCobranca.getBoletoInicial());
//                contaCobranca.setBoletoInicial(Moeda.incremento((1 + contaCobranca.getBoletoInicial()), "1"));
//                lista.get(i).setContaCobranca(contaCobranca);
//                lista.get(i).setAcordo(1);
//                salvar(contaCobranca, contaCobranca.getId());
//            } catch (Exception e) {
//                mensagem.set(i, "Conta cobrança não esta correto.");
//            }
//            if (loteGerado) {
//                loteDB.commit();
//                loteGerado = false;
//            }
//            lista.get(i).setControleBoleto(0);
//            salvar(lista.get(i), -1);
//            if (lista.get(i).getId() != -1) {
//                if (mensagemConvencao != null) {
//                    salvar(new MensagemCobranca(-1, lista.get(i), mensagemConvencao), -1);
//                }
//            } else {
//                mensagem.set(i, "Erro ao gerar boleto(s).");
//            }
//            i++;
//        }
//
//        return mensagem;
//    }
//
//    public List<String> gerarMovimentoSemVerificacao(Rotina rotina) {
//        PessoaDB pesDB = new PessoaDBToplink();
//        CnaeConvencaoDB dbConvencao = new CnaeConvencaoDBToplink();
//        GrupoCidadesDB dbGrupoCidades = new GrupoCidadesDBToplink();
//        MensagemConvencaoDB dbMen = new MensagemConvencaoDBToplink();
//        ContaCobrancaDB ctaCobraDB = new ContaCobrancaDBToplink();
//        ContaCobranca contaCobranca = null;
//        LoteDB loteDB = new LoteDBToplink();
//        MovimentoDB movDB = new MovimentoDBToplink();
//        boolean loteGerado = false;
//        Lote lote = new Lote();
//        int i = 0;
//        Movimento mov;
//
//        List<String> mensagem = new ArrayList<String>();
//
//        Servicos servico;
//        TipoServico tipoServico;
//        String referencia;
//        Convencao convencao = null;
//        lote = new Lote(
//                -1,
//                rotina,
//                "R",
//                DataHoje.data());
//        loteGerado = loteDB.insert(lote);
//        if (!loteGerado) {
//            mensagem.add("Erro na geração do lote!");
//            return mensagem;
//        }
//
//        while (i < lista.size()) {
//            mensagem.add("Movimentos gerados com sucesso!");
//
//            if (lista.get(i) != null) {
//                if (lista.get(i).getLoteBaixa() != null) {
//                    mensagem.set(i, "Movimento já foi baixado!");
//                    i++;
//                    continue;
//                }
//            }
//
//            if (lista.get(i).getId() != -1) {
//                mensagem.set(i, "Movimento já foi gerado!");
//                salvar(lista.get(i), lista.get(i).getId());
//                i++;
//                continue;
//            }
//
//            try {
//                servico = lista.get(i).getServicos();
//                tipoServico = lista.get(i).getTipoServico();
//                referencia = lista.get(i).getReferencia();
//            } catch (Exception e) {
//                mensagem.set(i, e.getMessage());
//                i++;
//                continue;
//            }
//
//            if (pesDB.pessoaSemMovimento(lista.get(i).getPessoa().getId(), referencia, tipoServico.getId(), servico.getId())) {
//                mensagem.set(i, "Já existe um movimento para " + lista.get(i).getPessoa().getNome() + " com esse servico, tipo e referência.");
//                i++;
//                continue;
//            }
//            if (lista.get(i).getContaCobranca() == null) {
//                mensagem.set(i, "Não existe conta cobrança para o tipo principal e esse serviço.");
//                i++;
//                continue;
//            }
//
//            // FOI COMENTADO PARA BAIXAR MESMO SEM CONVENCAO SE OUVER NECESSIDADE DE DESCOMENTAR FALAR COM ROGERINHO =)
////            convencao = dbConvencao.pesquisarCnaeConvencaoPorPessoa(lista.get(i).getPessoa().getId());
////
////            if (convencao == null) {
////                mensagem.set(i, "Não existe cnae vinculado com convenção.");
////                i++;
////                continue;
////            }
//
//            lista.get(i).setLote(lote);
//            try {
//                contaCobranca = ctaCobraDB.pesquisaCodigo(lista.get(i).getContaCobranca().getId());
//                lista.get(i).setNumero(contaCobranca.getBoletoInicial());
//                contaCobranca.setBoletoInicial(Moeda.incremento((1 + contaCobranca.getBoletoInicial()), "1"));
//                lista.get(i).setContaCobranca(contaCobranca);
//                lista.get(i).setAcordo(1);
//                salvar(contaCobranca, contaCobranca.getId());
//            } catch (Exception e) {
//                mensagem.set(i, "Conta cobrança não esta correto.");
//            }
//            if (loteGerado) {
//                loteDB.commit();
//                loteGerado = false;
//            }
//            salvar(lista.get(i), -1);
//
//            i++;
//        }
//
//        return mensagem;
//    }
//
//    public String gerarMovimentoSemValor(Rotina rotina){
//        PessoaDB pesDB = new PessoaDBToplink();
//        CnaeConvencaoDB dbConvencao = new CnaeConvencaoDBToplink();
//        GrupoCidadesDB dbGrupoCidades = new GrupoCidadesDBToplink();
//        MensagemConvencaoDB dbMen = new MensagemConvencaoDBToplink();
//        ContaCobrancaDB ctaCobraDB = new ContaCobrancaDBToplink();
//        FinanceiroDB financeiroDB = new FinanceiroDBToplink();
//        ContaCobranca contaCobranca = null;
//        LoteDB loteDB = new LoteDBToplink();
//        boolean loteGerado = false;
//        Lote lote = new Lote();
//        int i = 0;
//        Movimento mov;
//        MensagemConvencao mensagemConvencao = null;
//        Pessoa pessoa;
//        Servicos servico;
//        TipoServico tipoServico;
//        String referencia;
//        Convencao convencao = null;
//
//        mensagemCorrecao = new ErroMovimento();
//
//        mensagemCorrecao.criarErro(1, "Movimento já foi baixado!");
//        mensagemCorrecao.criarErro(2, "Movimento já foi gerado!");
//        mensagemCorrecao.criarErro(3, "Esta com grupo de cidade não corespondente a convenção");
//        mensagemCorrecao.criarErro(4, "Pessoa inválida para movimento");
//        mensagemCorrecao.criarErro(5, "Serviço inválido para movimento");
//        mensagemCorrecao.criarErro(6, "Já existe esse movimento");
//        mensagemCorrecao.criarErro(7, "Não existe conta cobrança para o tipo principal e esse serviço");
//        mensagemCorrecao.criarErro(8, "Não existe cnae vinculado com convenção");
//        mensagemCorrecao.criarErro(9, "Não existe mensagem para esse boleto");
//        mensagemCorrecao.criarErro(10, "Conta cobrança não esta correto");
//        mensagemCorrecao.criarErro(11, "Erro ao gerar boleto(s)");
//
//        try{
//            financeiroDB.abrirTransacao();
//            lote = new Lote(
//                    -1,
//                    rotina,
//                    "R",
//                    DataHoje.data());
//
//            loteGerado = financeiroDB.acumularObjeto(lote);
//            if (!loteGerado) {
//                financeiroDB.desfazerTransacao();
//                return "Erro na geração do lote!";
//            }
//
//            financeiroDB.comitarTransacao();
//
//            while (i < lista.size()) {
//                financeiroDB.abrirTransacao();
//                if (lista.get(i).getLoteBaixa() != null) {
//                    mensagemCorrecao.adicionarObjetoEmErro(1, lista.get(i));
//                    i++;
//                    financeiroDB.desfazerTransacao();
//                    continue;
//                }
//
//
//                if (lista.get(i).getId() != -1) {
//                    mensagemCorrecao.adicionarObjetoEmErro(2, lista.get(i));
//                    financeiroDB.update(lista.get(i));
//                    i++;
//                    financeiroDB.desfazerTransacao();
//                    continue;
//                }
//
//    //            if (lista.get(i).getValor() != 0) {
//    //                mensagem.set(i, "Valor do boleto não pode ser diferente de zero");
//    //                i++;
//    //                continue;
//    //            }
//
//                try {
//                    pessoa = pesDB.pessoaPermitida(lista.get(i).getPessoa().getId());
//                    if (pessoa == null) {
//                        mensagemCorrecao.adicionarObjetoEmErro(3, lista.get(i));
//                        i++;
//                        financeiroDB.desfazerTransacao();
//                        continue;
//                    }
//                } catch (Exception e) {
//                    mensagemCorrecao.adicionarObjetoEmErro(4, lista.get(i));
//                    i++;
//                    financeiroDB.desfazerTransacao();
//                    continue;
//                }
//
//                try {
//                    servico = lista.get(i).getServicos();
//                    tipoServico = lista.get(i).getTipoServico();
//                    referencia = lista.get(i).getReferencia();
//                } catch (Exception e) {
//                    mensagemCorrecao.adicionarObjetoEmErro(5, lista.get(i));
//                    i++;
//                    financeiroDB.desfazerTransacao();
//                    continue;
//                }
//
//                if (pesDB.pessoaSemMovimento(pessoa.getId(), referencia, tipoServico.getId(), servico.getId())) {
//                    mensagemCorrecao.adicionarObjetoEmErro(6, lista.get(i));
//                    i++;
//                    financeiroDB.desfazerTransacao();
//                    continue;
//                }
//                if (lista.get(i).getContaCobranca() == null) {
//                    mensagemCorrecao.adicionarObjetoEmErro(7, lista.get(i));
//                    i++;
//                    financeiroDB.desfazerTransacao();
//                    continue;
//                }
//
//                convencao = dbConvencao.pesquisarCnaeConvencaoPorPessoa(lista.get(i).getPessoa().getId());
//
//                if (convencao == null) {
//                    mensagemCorrecao.adicionarObjetoEmErro(8, lista.get(i));
//                    i++;
//                    financeiroDB.desfazerTransacao();
//                    continue;
//                }
//                mensagemConvencao = dbMen.verificaMensagem(
//                        convencao.getId(),
//                        servico.getId(),
//                        tipoServico.getId(),
//                        dbGrupoCidades.grupoCidadesPorPessoa(lista.get(i).getPessoa().getId(), convencao.getId()).getId(),
//                        referencia);
//
//                if (mensagemConvencao == null || mensagemConvencao.getId() == -1) {
//                    mensagemCorrecao.adicionarObjetoEmErro(9, lista.get(i));
//                    i++;
//                    financeiroDB.desfazerTransacao();
//                    continue;
//                }
//                lista.get(i).setLote(lote);
//                try {
//                    contaCobranca = ctaCobraDB.pesquisaCodigo(lista.get(i).getContaCobranca().getId());
//                    lista.get(i).setNumero(contaCobranca.getBoletoInicial());
//                    contaCobranca.setBoletoInicial(Moeda.incremento((1 + contaCobranca.getBoletoInicial()), "1"));
//                    lista.get(i).setContaCobranca(contaCobranca);
//                    financeiroDB.update(contaCobranca);
//                } catch (Exception e) {
//                    mensagemCorrecao.adicionarObjetoEmErro(10, lista.get(i));
//                }
//                lista.get(i).setControleBoleto(0);
//                financeiroDB.acumularObjeto(lista.get(i));
//                if (lista.get(i).getId() != -1) {
//                    if (mensagemConvencao != null) {
//                        financeiroDB.acumularObjeto(new MensagemCobranca(-1, lista.get(i), mensagemConvencao));
//                    }
//                } else {
//                    mensagemCorrecao.adicionarObjetoEmErro(11, lista.get(i));
//                }
//                financeiroDB.comitarTransacao();
//                i++;
//            }
//            String mesag = "";
//            if (mensagemCorrecao.is_ExisteErro(1)){
//                mesag += mensagemCorrecao.capiturarErro(1);
//            }
//            if (mensagemCorrecao.is_ExisteErro(2)){
//                mesag += mensagemCorrecao.capiturarErro(2);
//            }
//            if (mensagemCorrecao.is_ExisteErro(6)){
//                mesag += mensagemCorrecao.capiturarErro(6);
//            }
//
//            if (mesag.isEmpty())
//                mesag = "Movimentos gerados com sucesso! ";
//
//            return mesag;
//        }catch(Exception e){
//            financeiroDB.desfazerTransacao();
//            String mens = "Erro ";
//            for(int z = 1 ; i < 11; z++){
//                mens = (mens + mensagemCorrecao.capiturarErro(i) + " \n");
//            }
//            return mens;
//        }
//
//    }
//
//    public String deleteMovimento() {
//        String mensagem = "Deletados com sucesso!";
//        MovimentoDB movDB = new MovimentoDBToplink();
//        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
//        LoteDB loteDB = new LoteDBToplink();
//        Lote lote = null;
//        MensagemCobranca mensagemCobranca = null;
//        List<ImpressaoWeb> listaLogWeb = new ArrayList();
//        int i = 0;
//        try {
//            while (i < lista.size()) {
//                if (lista.get(i).getAtivo() == 1) {
//                    mensagemCobranca = movDB.pesquisaMensagemCobranca(lista.get(i).getId());
//                    listaLogWeb = movDB.pesquisaLogWeb(lista.get(i).getId());
//                    sv.abrirTransacao();
//
//                    // EXCLUI LISTA IMPRESSAO
//                    for (ImpressaoWeb imp : listaLogWeb) {
//                        if (!sv.deletarObjeto(sv.pesquisaCodigo(imp.getId(),"ImpressaoWeb"))){
//                            sv.desfazerTransacao();
//                            return "Erro na exclusão da lista de LogWeb";
//                        }
//                    }
//
//
//                    // EXCLUI MENSAGEM BOLETO
//                    if (mensagemCobranca != null){
//                        if (!sv.deletarObjeto(sv.pesquisaCodigo(mensagemCobranca.getId(),"MensagemCobranca"))){
//                            sv.desfazerTransacao();
//                            return "Erro na exclusão da mensagem do boleto";
//                        }
//                    }
//
//                    // EXCLUI MOVIMENTO
//                    if( !sv.deletarObjeto(sv.pesquisaCodigo(lista.get(i).getId(), "Movimento")) )  {
//                        sv.desfazerTransacao();
//                        return "Erro na exclusão do boleto.";
//                    }
//
//                    lote = lista.get(i).getLote();
//                    // EXCLUI LOTE
//                    if (movDB.movimentosDoLote(lote.getId()) == 0) {
//                        if (!sv.deletarObjeto(sv.pesquisaCodigo(lote.getId(),"Lote"))) {
//                            sv.desfazerTransacao();
//                            return "Erro na exclusão do lote.";
//                        }
//                    }
//                    sv.comitarTransacao();
//                }
//                sv = new SalvarAcumuladoDBToplink();
//                i++;
//            }
//        } catch (Exception e) {
//            mensagem = e.getMessage();
//        }
//        return mensagem;
//    }
//
//    public byte[] imprimirBoleto(boolean imprimeVerso){
//        byte[] arquivo = new byte[0];
//        try {
//            FacesContext faces = FacesContext.getCurrentInstance();
//            Collection vetor = new ArrayList();
//            PessoaEnderecoDB pesEndDB = new PessoaEnderecoDBToplink();
//            JuridicaDB jurDB = new JuridicaDBToplink();
//            int i = 0;
//            String swap[] = new String[50];
//            PessoaEndereco pe = null;
//            MovimentoDB movDB = new MovimentoDBToplink();
//            CnaeConvencaoDB cnaeConv = new CnaeConvencaoDBToplink();
//            Cobranca cobranca = null;
//            BigDecimal valor;
//            String mensagem = "";
//            MensagemCobranca mensagemCobranca = null;
//            Historico historico = null;
//            String mensagemErroMovimento = "Movimento(s) sem mensagem: ";
//            JasperReport jasper = (JasperReport) JRLoader.loadObject(
//                    ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/BOLETO.jasper"));
//
//            while (i < lista.size()) {
//                if (lista.get(i).getLoteBaixa() != null) {
//                    break;
//                }
//                if (lista.get(i).getContaCobranca().getLayout().getId() == 2) {
//                    swap[40] = ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/SINDICAL.jasper");
//                } else {
//                    swap[40] = ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/SICOB.jasper");
//                }
//                swap[43] = "";
//                swap[42] = "";
//                if (lista.get(i).getContaCobranca().getLayout().getId() == Cobranca.SINDICAL) {
//                    cobranca = new CaixaFederalSindical(lista.get(i));
//                    swap[43] = "EXERC " + lista.get(i).getReferencia().substring(3);
//                    swap[42] = "BLOQUETO DE CONTRIBUIÇÃO SINDICAL URBANA.";
//                } else if ((lista.get(i).getContaCobranca().getContaBanco().getBanco().getNumero().equals(Cobranca.caixaFederal))
//                        && (lista.get(i).getContaCobranca().getLayout().getId() == Cobranca.SICOB)) {
//                    cobranca = new CaixaFederalSicob(lista.get(i));
//                } else if ((lista.get(i).getContaCobranca().getContaBanco().getBanco().getNumero().equals(Cobranca.caixaFederal))
//                        && (lista.get(i).getContaCobranca().getLayout().getId() == Cobranca.SIGCB)) {
//                    cobranca = new CaixaFederalSigCB(lista.get(i));
//                } else if (lista.get(i).getContaCobranca().getContaBanco().getBanco().getNumero().equals(Cobranca.itau)) {
//                    cobranca = new Itau(lista.get(i));
//                } else if (lista.get(i).getContaCobranca().getContaBanco().getBanco().getNumero().equals(Cobranca.bancoDoBrasil)) {
//                    cobranca = new BancoDoBrasil(lista.get(i));
//                } else if (lista.get(i).getContaCobranca().getContaBanco().getBanco().getNumero().equals(Cobranca.real)) {
//                    cobranca = new Real(lista.get(i));
//                } else if (lista.get(i).getContaCobranca().getContaBanco().getBanco().getNumero().equals(Cobranca.bradesco)) {
//                    cobranca = new Bradesco(lista.get(i));
//                } else if (lista.get(i).getContaCobranca().getContaBanco().getBanco().getNumero().equals(Cobranca.santander)) {
//                    cobranca = new Santander(lista.get(i));
//                }
//
//                try {
//                    swap[0] = jurDB.pesquisaJuridicaPorPessoa(lista.get(i).getPessoa().getId()).getContabilidade().getPessoa().getNome();
//                } catch (Exception e) {
//                    swap[0] = "";
//                }
//
//                try {
//                    swap[1] = cnaeConv.pesquisarCnaeConvencaoPorPessoa(lista.get(i).getPessoa().getId()).getConvencao();
//                } catch (Exception e) {
//                    swap[1] = "";
//                }
//
//                // ENDEREÇO DE COBRANCA DA PESSOA -------------------------------------------------------------------------
//                // NO CASO PODE SER OU NÃO O ENDEREÇO DA CONTABILIDADE ----------------------------------------------------
//                try {
//                    pe = pesEndDB.pesquisaEndPorPessoaTipo(lista.get(i).getPessoa().getId(), 3);
//                    swap[2] = pe.getEndereco().getEnderecoSimplesToString();
//                    swap[3] = pe.getNumero();
//                    swap[4] = pe.getComplemento();
//                    swap[5] = pe.getEndereco().getBairro().getBairro();
//                    swap[6] = pe.getEndereco().getCidade().getCidade();
//                    swap[7] = pe.getEndereco().getCidade().getUf();
//                    swap[8] = pe.getEndereco().getCep().substring(0, 5) + "-" + pe.getEndereco().getCep().substring(5);
//                } catch (Exception e) {
//                    swap[2] = "";
//                    swap[3] = "";
//                    swap[4] = "";
//                    swap[5] = "";
//                    swap[6] = "";
//                    swap[7] = "";
//                    swap[8] = "";
//                }
//
//                // ENDEREÇO SOMENTE DA PESSOA -------------------------------------------------------------------------
//                try {
//                    pe = pesEndDB.pesquisaEndPorPessoaTipo(lista.get(i).getPessoa().getId(), 2);
//                    if (pe == null) {
//                        pe = pesEndDB.pesquisaEndPorPessoaTipo(lista.get(i).getPessoa().getId(), 1);
//                    } else {
//                        //pe = pesEndDB.pesquisaEndPorPessoaTipo(lista.get(i).getPessoa().getId(), 2);
//                    }
//                    swap[9] = pe.getEndereco().getEnderecoSimplesToString();
//                    swap[10] = pe.getNumero();
//                    swap[11] = pe.getComplemento();
//                    swap[12] = pe.getEndereco().getBairro().getBairro();
//                    swap[13] = pe.getEndereco().getCidade().getCidade();
//                    swap[14] = pe.getEndereco().getCidade().getUf();
//                    swap[15] = pe.getEndereco().getCep().substring(0, 5) + "-" + pe.getEndereco().getCep().substring(5);
//                } catch (Exception e) {
//                    swap[9] = "";
//                    swap[10] = "";
//                    swap[11] = "";
//                    swap[12] = "";
//                    swap[13] = "";
//                    swap[14] = "";
//                    swap[15] = "";
//                }
//
//                try {
//                    swap[16] = lista.get(i).getContaCobranca().getCedente();
//                    swap[17] = "";//jurDB.pesquisaJuridicaPorPessoa(i).getPessoa().getNome();
//                } catch (Exception e) {
//                    swap[16] = "-1";
//                    swap[17] = "";
//                }
//
//                // ESSE PEGA O ENDEREÇO COMERCIAL DO SINDICATO.
//                try {
//                    pe = pesEndDB.pesquisaEndPorPessoaTipo(1, 2);
//                    swap[18] = pe.getEndereco().getEnderecoSimplesToString();
//                    swap[19] = pe.getNumero();
//                    swap[20] = pe.getComplemento();
//                    swap[21] = pe.getEndereco().getBairro().getBairro();
//                    swap[22] = pe.getEndereco().getCidade().getCidade();
//                    swap[23] = pe.getEndereco().getCidade().getUf();
//                    swap[24] = pe.getEndereco().getCep().substring(0, 5) + "-" + pe.getEndereco().getCep().substring(5);
//                    swap[30] = pe.getPessoa().getDocumento();
//                } catch (Exception e) {
//                    swap[18] = "";
//                    swap[19] = "";
//                    swap[20] = "";
//                    swap[21] = "";
//                    swap[22] = "";
//                    swap[23] = "";
//                    swap[24] = "";
//                }
//
//                try {
//                    swap[26] = cobranca.representacao();
//                    swap[27] = cobranca.codigoBarras();
//                } catch (Exception e) {
//                    swap[26] = "";
//                    swap[27] = "";
//                }
//
//                // VERIFICA SE O ENDEREÇO DE COBRANCA É IGUAL AO ENDERECO COMERCIAL --------------------------------------------------------
////                if (swap[2].equals(swap[9]) &&
////                    swap[3].equals(swap[10]) &&
////                    swap[4].equals(swap[11])){
////                        swap[0] = "";
////                        swap[2] = "";
////                        swap[3] = "";
////                        swap[4] = "";
////                        swap[5] = "";
////                        swap[6] = "";
////                        swap[7] = "";
////                        swap[8] = "";
////                }
//
//                try {
////                    swap[44] = lista.get(i).getContaCobranca().getCodigoSindical().substring(0, 3) + "." + //codigosindical
////                            lista.get(i).getContaCobranca().getCodigoSindical().substring(3, 6) + "."
////                            + lista.get(i).getContaCobranca().getCodigoSindical().substring(6, lista.get(i).getContaCobranca().getCodigoSindical().length()) + "-"
////                            + cobranca.moduloOnze(lista.get(i).getContaCobranca().getCodigoSindical());
//                    FilialDB filialDB = new FilialDBToplink();
//                    String entidade = filialDB.pesquisaRegistroPorFilial(1).getTipoEntidade();
//                    String sicas = lista.get(i).getContaCobranca().getSicasSindical();
//                    if(entidade.equals("S")){
//                        swap[44] = "S-" + sicas;
//                    }else if(entidade.equals("C")){
//                        swap[44] = "C-" + sicas.substring(sicas.length() - 3, sicas.length());
//                    }else if(entidade.equals("F")){
//                        swap[44] = "F-" + sicas.substring(sicas.length() - 3, sicas.length());
//                    }
//                }catch (Exception e) {
//                    swap[44] = "";
//                }
//
//                valor = new BigDecimal(lista.get(i).getValor());
//                if (valor.toString().equals("0")) {
//                    valor = null;
//                }
//
//                if (lista.get(i).getTipoServico().getId() != 4) {
//                        mensagemCobranca = movDB.pesquisaMensagemCobranca(lista.get(i).getId());
//                        mensagem = mensagemCobranca.getMensagemConvencao().getMensagemContribuinte();//mensagem
//                        swap[25] = mensagemCobranca.getMensagemConvencao().getMensagemCompensacao();
//                } else {
//                    historico = movDB.pesquisaHistorico(lista.get(i).getId());
//                    mensagem = historico.getHistorico();
//                    swap[25] = historico.getComplemento();
//                }
//
//                mensagemErroMovimento += " " + swap[0] + "\n " +
//                                         lista.get(i).getPessoa().getNome() + "\n" +
//                                         lista.get(i).getNumero() + "\n";
//
//
//                if((historico == null) && (mensagemCobranca == null)){
//                    break;
//                }
//
//                String codc = cobranca.getCedenteFormatado();
//                // CAIXA EXIGE QUE SE COLOQUE O AGENCIA/COD SINDICAL NA FICHA DE COMPENSACAO NO LUGAR DO AG/COD CEDENDE,
//                // POREM CONCATENANDO COM O DIGITO VERIFICADOR DO COD CEDENTE EX.
//                // 0242/004.136.02507-5 >>>>> FICARA : 0242/S02507-5
//                if (lista.get(i).getContaCobranca().getLayout().getId() == 2) {
//                    codc = swap[44] + "-" + codc.substring(codc.length()-1, codc.length());
//                }
//                vetor.add(new ParametroBoleto(
//                        lista.get(i).getReferencia(), // ref (referencia)
//                        imprimeVerso, // imprimeVerso
//                        swap[0], //escritorio
//                        lista.get(i).getServicos().getDescricao(), //  contribuicao (servico)
//                        lista.get(i).getTipoServico().getDescricao(), // tipo
//                        swap[1], //  grupo (convencao)
//                        lista.get(i).getPessoa().getDocumento(), // cgc (cnpj)
//                        lista.get(i).getPessoa().getNome(), //  sacado
//                        valor, //  valor
//                        swap[2],//endereco
//                        swap[3],//numero
//                        swap[4],//complemento
//                        swap[5],//bairro
//                        swap[6],//cidade
//                        swap[7],//estado
//                        swap[8],//cep
//                        lista.get(i).getNumero(),// boleto
//                        swap[9],// sacado_endereco
//                        swap[10],//sacado_numero
//                        swap[11],//sacado_complemento
//                        swap[12],//sacado_bairro
//                        swap[13],//sacado_cidade
//                        swap[14],//sacado_estado
//                        swap[15],//sacado_cep
//                        cobranca.getNossoNumeroFormatado(),//nossonum (nosso numero)
//                        DataHoje.data(),// datadoc
//                        lista.get(i).getVencimento(),// VENCIMENTO
//                        cobranca.codigoBanco(),// codbanco
//                        lista.get(i).getContaCobranca().getMoeda(),//moeda
//                        lista.get(i).getContaCobranca().getEspecieMoeda(),// especie_doc
//                        lista.get(i).getContaCobranca().getEspecieDoc(),//especie
//                        cobranca.getAgenciaFormatada(),//cod_agencia
//                        codc,//codcedente
//                        lista.get(i).getContaCobranca().getAceite(),//aceite
//                        lista.get(i).getContaCobranca().getCarteira(),//carteira
//                        lista.get(i).getReferencia().substring(3),//exercicio
//                        swap[16],//nomeentidade
//                        swap[40],
//                        //   ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/SICOB.jasper"),//LAYOUT
//                        mensagem,//movDB.pesquisaMensagemCobranca(lista.get(i).getId()).getMensagemConvencao().getMensagemContribuinte(),//mensagem
//                        lista.get(i).getContaCobranca().getLocalPagamento(),//local_pag
//                        swap[18],//endent
//                        swap[19],//nument (numero entidade)
//                        swap[20],//compent
//                        swap[21],//baient
//                        swap[22],//cident
//                        swap[23],//estent
//                        swap[24],//cepent
//                        swap[30],//cgcent
//                        swap[26],//REPNUM
//                        swap[27],//CODBAR
//                        swap[25],//mensagem_boleto
//                        ((ServletContext) faces.getExternalContext().getContext()).getRealPath(lista.get(i).getContaCobranca().getContaBanco().getBanco().getLogo().trim()),
//                        ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Imagens/LogoCliente.png"),//logoEmpresa
//                        ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Imagens/serrilha.GIF"),//serrilha
//                        jurDB.pesquisaJuridicaPorPessoa(lista.get(i).getPessoa().getId()).getCnae().getNumero().substring(0, 3),//cnae
//                        lista.get(i).getContaCobranca().getCategoriaSindical(),//categoria
//                        swap[44], //codigosindical
//                        swap[43], //usoBanco
//                        swap[42], //textoTitulo
//                        ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/BOLETO_VERSO.jasper"),//caminhoVerso
//                        lista.get(i).getFilial().getFilial().getPessoa().getNome()));
//
//                i++;
//            }
//            arquivo = this.imprimir(vetor, jasper);
//        } catch (Exception erro) {
//            System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
//        }
//        FacesContext.getCurrentInstance().responseComplete();
//        if(mensagemErroMovimento.equals("Movimento(s) sem mensagem: ")){
//            mensagemErroMovimento = null;
//        }
//        return arquivo;
//    }
//
//    private byte[] imprimir(Collection vetor, JasperReport jasper) {
//        byte[] arquivo = new byte[0];
//        try {
//            FacesContext faces = FacesContext.getCurrentInstance();
//            HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
//            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(vetor);
//            JasperPrint print = JasperFillManager.fillReport(
//                    jasper,
//                    null,
//                    dtSource);
//            arquivo = JasperExportManager.exportReportToPdf(print);
//            response.setContentType("application/pdf");
//            response.setContentLength(arquivo.length);
//
//            ServletOutputStream saida = response.getOutputStream();
//            saida.write(arquivo, 0, arquivo.length);
//            saida.flush();
//            saida.close();
//        } catch (Exception erro) {
//            System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
//        }
//        FacesContext.getCurrentInstance().responseComplete();
//        return arquivo;
//    }
//
//    public synchronized String darBaixa(Usuario usuario, Rotina rotina, Object[] parametros, List<String> listaPagamentos, List<Float> listaTaxa, List<Float> listaValor, Filial filial, Departamento departamento) {
//        if (lista.isEmpty()) {
//            return "Lista vazia!";
//        }
//
//        FinanceiroDB financeiroDB = new FinanceiroDBToplink();
//        int i = 0;
//        Movimento movSwap;
//        String mensagem = "";
//        try {
//            List<Lote> lote = new ArrayList<Lote>();
//            List<LoteBaixa> loteB = new ArrayList<LoteBaixa>();
//            Plano5DB plano5DB = new Plano5DBToplink();
//            MovimentoDB movDB = new MovimentoDBToplink();
//            Plano5 plano5 = plano5DB.pesquisaPlano5IDContaBanco(lista.get(0).getContaCobranca().getContaBanco().getId());
//            PessoaDB pessoaDB = new PessoaDBToplink();
//            ServicosDB servicosDB = new ServicosDBToplink();
//            TipoServicoDB tiposDB = new TipoServicoDBToplink();
//            DepartamentoDB departamentoDB = new DepartamentoDBToplink();
//            departamento = departamentoDB.pesquisaCodigo(2);
//            int tam = lista.size();
//
//            i = 0;
//            financeiroDB.abrirTransacao();
//            while (i < tam) {
//                if (lista.get(i).getId() == -1) {
//                    i++;
//                    continue;
//                }
//
//                if (movDB.pesquisaMovPorNumDocumentoList(lista.get(i).getNumero(), lista.get(i).getDtVencimento(), lista.get(i).getContaCobranca().getId()).size() > 1) {
//                    i++;
//                    continue;
//                }
//
//                loteB.add(new LoteBaixa(
//                        -1,
//                        usuario,
//                        plano5,
//                        DataHoje.converte(listaPagamentos.get(i)),
//                        DataHoje.dataHoje(),
//                        filial,
//                        departamento
//                        ));
//
//                financeiroDB.acumularObjeto(loteB.get(i));
//                lote.add(new Lote(
//                        -1,
//                        rotina,
//                        "R",
//                        listaPagamentos.get(i)));
//                financeiroDB.acumularObjeto(lote.get(i));
//                lista.get(i).setLoteBaixa(loteB.get(i));
//                lista.get(i).setAtivo(0);
//                financeiroDB.update(lista.get(i));
//
//                movSwap = new Movimento(
//                        -1,
//                        lote.get(i),
//                        lista.get(i).getFTipoDocumento(),
//                        lista.get(i).getServicos().getPlano5Credito(),
//                        lista.get(0).getServicos().getDepartamento(),
//                        lista.get(0).getServicos().getFilial(),
//                        lista.get(i).getPessoa(),
//                        lista.get(i).getVencimento(),
//                        lista.get(i).getNumero(),
//                        listaValor.get(i),
//                        0,
//                        lista.get(i).getGrupoAcordo(),
//                        "C",
//                        loteB.get(i),
//                        lista.get(i).getServicos(),
//                        lista.get(i).getTipoServico(),
//                        lista.get(i).getReferencia(),
//                        lista.get(i).getContaCobranca(),
//                        1,
//                        lista.get(i).getQuantidade());
//                financeiroDB.acumularObjeto(movSwap);
//
//                financeiroDB.acumularObjeto(new Movimento(
//                        -1,
//                        lote.get(i),
//                        null,
//                        plano5,
//                        null,
//                        filial,
//                        pessoaDB.pesquisaCodigo(0),
//                        listaPagamentos.get(i),
//                        "",
//                        listaValor.get(i),
//                        0,
//                        null,
//                        "D",
//                        loteB.get(i),
//                        null,
//                        null,
//                        "",
//                        null,
//                        1));
//
//                if(listaTaxa.get(i) != 0){
//                    financeiroDB.acumularObjeto(new Movimento(
//                            -1,
//                            lote.get(i),
//                            lista.get(i).getFTipoDocumento(),
//                            servicosDB.pesquisaCodigo(8).getPlano5Debito(),
//                            null,
//                            lista.get(0).getFilial(),
//                            pessoaDB.pesquisaCodigo(0),
//                            listaPagamentos.get(i),
//                            lista.get(i).getNumero(),
//                            listaTaxa.get(i),
//                            0,
//                            null,
//                            "D",
//                            loteB.get(i),
//                            servicosDB.pesquisaCodigo(8),
//                            tiposDB.pesquisaCodigo(1),
//                            "",
//                            null,
//                            1));
//
//                    financeiroDB.acumularObjeto(new Movimento(
//                            -1,
//                            lote.get(i),
//                            null,
//                            plano5,
//                            null,
//                            lista.get(0).getFilial(),
//                            pessoaDB.pesquisaCodigo(0),
//                            listaPagamentos.get(i),
//                            "",
//                            listaTaxa.get(i),
//                            0,
//                            null,
//                            "C",
//                            loteB.get(i),
//                            null,
//                            null,
//                            "",
//                            null,
//                            1));
//                    financeiroDB.acumularObjeto(new ComplementoValor(
//                            -1,
//                            movSwap,
//                            0,
//                            0,
//                            0,
//                            0,
//                            listaTaxa.get(i)));
//                }
//                i++;
//            }
//            financeiroDB.comitarTransacao();
//        } catch (Exception e) {
//            financeiroDB.desfazerTransacao();
//            mensagem = e.getMessage();
//        }
//        return mensagem;
//    }
//
//    public synchronized String darBaixaManual(
//            Usuario usuario,
//            Rotina rotina,
//            String quitacao,
//            List<String> listaVencimento,
//            List<String> listaValorB,
//            List<String> listaNumero,
//            Plano5 plano5,
//            List<FTipoDocumento> listaPagamentos,
//            Filial filial,
//            Departamento departamento
//            ) {
//        if (lista.isEmpty()) {
//            return "Lista vazia!";
//        }
//        FinanceiroDB financeiroDB = new FinanceiroDBToplink();
//        ContaRotinaDB contaRotinaDB = new ContaRotinaDBToplink();
//        int caixaBanco = contaRotinaDB.verificaRotinaParaConta(plano5.getId());
//        if(caixaBanco == 2){
//            filial = lista.get(0).getFilial();
//        }
//        Movimento movSwap;
//        String mensagem = "";
//        float valorCredito = 0;
//        financeiroDB.abrirTransacao();
//        try {
//            Lote lote = new Lote();
//            LoteBaixa loteB = new LoteBaixa();
//            MovimentoDB movDB = new MovimentoDBToplink();
//            PessoaDB pessoaDB = new PessoaDBToplink();
//            ServicosDB servicosDB = new ServicosDBToplink();
//            TipoServicoDB tiposDB = new TipoServicoDBToplink();
//            this.getListaDesconto();
//            float[] somaComplemento = somaComplemento();
//            float soma = Moeda.somaValores(
//                    Moeda.somaValores(
//                    somaComplemento[0],
//                    somaComplemento[1]),
//                    somaComplemento[2]);
//            loteB = new LoteBaixa(
//                    -1,
//                    usuario,
//                    plano5,
//                    DataHoje.converte(quitacao),
//                    null,
//                    filial,
//                    departamento);
//            financeiroDB.acumularObjeto(loteB);
//            lote = new Lote(
//                    -1,
//                    rotina,
//                    "R",
//                    quitacao);
//            financeiroDB.acumularObjeto(lote);
//            for (int i = 0; i < lista.size(); i++) {
//                if (lista.get(i).getId() == -1) {
//                    i++;
//                    continue;
//                }
//
//                if (lista.get(i).getContaCobranca() != null){
//                    if (movDB.pesquisaMovPorNumDocumentoList(lista.get(i).getNumero(), lista.get(i).getDtVencimento(), lista.get(i).getContaCobranca().getId()).size() > 1) {
//                        i++;
//                        continue;
//                    }
//                }
//
//                lista.get(i).setLoteBaixa(loteB);
//                lista.get(i).setAtivo(0);
//                financeiroDB.update(lista.get(i));
//
//                if (soma > desconto) {
//                    valorCredito = lista.get(i).getValor();
//                } else {
//                    valorCredito = valorTotalParaMovimentoComDesconto(i);
//
//                }
//
//                movSwap = new Movimento(
//                        -1,
//                        lote,
//                        lista.get(i).getFTipoDocumento(),
//                        lista.get(i).getServicos().getPlano5Credito(),
//                        lista.get(i).getServicos().getDepartamento(),
//                        lista.get(i).getServicos().getFilial(),
//                        lista.get(i).getPessoa(),
//                        lista.get(i).getVencimento(),
//                        lista.get(i).getNumero(),
//                        valorCredito,
//                        0,
//                        lista.get(i).getGrupoAcordo(),
//                        "C",
//                        loteB,
//                        lista.get(i).getServicos(),
//                        lista.get(i).getTipoServico(),
//                        lista.get(i).getReferencia(),
//                        lista.get(i).getContaCobranca(),
//                        1,
//                        lista.get(i).getQuantidade());
//                financeiroDB.acumularObjeto(movSwap);
//                if ((!listaDesconto.isEmpty()) && (i < listaDesconto.size())) {
//                    listaComplemento.get(i).setDesconto(listaDesconto.get(i));
//                }
//                if ((listaComplemento.get(i).getMulta() != 0)
//                        || (listaComplemento.get(i).getJuros() != 0)
//                        || (listaComplemento.get(i).getCorrecao() != 0)
//                        || (listaComplemento.get(i).getDesconto() != 0)) {
//                    listaComplemento.get(i).setMovimento(movSwap);
//                    // SE JA EXISTIR COMPLEMENTO ATUALIZAR -------------------
//                    if (financeiroDB.pesquisaComplementoValor(lista.get(i).getId()) == null){
//                        financeiroDB.acumularObjeto(listaComplemento.get(i));
//                    }else{
//                        financeiroDB.update(listaComplemento.get(i));
//                    }
//                }
//            }
//
//            for (int j = 0; j < listaVencimento.size(); j++) {
//                financeiroDB.acumularObjeto(new Movimento(
//                        -1,
//                        lote,
//                        listaPagamentos.get(j),
//                        plano5,
//                        departamento,
//                        filial,
//                        pessoaDB.pesquisaCodigo(0),
//                        listaVencimento.get(j),
//                        listaNumero.get(j),
//                        Moeda.substituiVirgulaFloat(listaValorB.get(j)),
//                        0,
//                        null,
//                        "D",
//                        loteB,
//                        null,
//                        null,
//                        "",
//                        null,
//                        1));
//            }
//
//            if (soma > desconto) {
//                // multa
//                if (somaComplemento[0] != 0) {
//                    financeiroDB.acumularObjeto(new Movimento(
//                            -1,
//                            lote,
//                            null,
//                            servicosDB.pesquisaCodigo(7).getPlano5Credito(),
//                            null,
//                            lista.get(0).getFilial(),
//                            pessoaDB.pesquisaCodigo(0),
//                            quitacao,
//                            "",
//                            somaComplemento[0],
//                            0,
//                            null,
//                            "C",
//                            loteB,
//                            servicosDB.pesquisaCodigo(7),
//                            tiposDB.pesquisaCodigo(1),
//                            "",
//                            null,
//                            1));
//                }
//
//                //juros
//                if (somaComplemento[1] != 0) {
//                    financeiroDB.acumularObjeto(new Movimento(
//                            -1,
//                            lote,
//                            null,
//                            servicosDB.pesquisaCodigo(6).getPlano5Credito(),
//                            null,
//                            lista.get(0).getFilial(),
//                            pessoaDB.pesquisaCodigo(0),
//                            quitacao,
//                            "",
//                            somaComplemento[1],
//                            0,
//                            null,
//                            "C",
//                            loteB,
//                            servicosDB.pesquisaCodigo(6),
//                            tiposDB.pesquisaCodigo(1),
//                            "",
//                            null,
//                            1));
//                }
//                //correcao
//                if (somaComplemento[2] != 0) {
//                    financeiroDB.acumularObjeto(new Movimento(
//                            -1,
//                            lote,
//                            null,
//                            servicosDB.pesquisaCodigo(11).getPlano5Credito(),
//                            null,
//                            lista.get(0).getFilial(),
//                            pessoaDB.pesquisaCodigo(0),
//                            quitacao,
//                            "",
//                            somaComplemento[2],
//                            0,
//                            null,
//                            "C",
//                            loteB,
//                            servicosDB.pesquisaCodigo(11),
//                            tiposDB.pesquisaCodigo(1),
//                            "",
//                            null,
//                            1));
//                }
//            }
//
//            for (int k = 0; k < listaComplemento.size(); k++) {
//            }
//
//            financeiroDB.comitarTransacao();
//        } catch (Exception e) {
//            financeiroDB.desfazerTransacao();
//            mensagem = e.getMessage();
//        }
//        return mensagem;
//    }
//
//    private float gerarValorBaseMovto(float valor, float desconto, float acrescimo) {
//        //Nome do Metodo: gerarValorBaseMovto
//        // se Desconto > Acrescimo executar este método, contrário não executar
//        // No movimento baixado sempre será gravado o valor original (sem multa,juros, correção e desconto) com excessão
//        // de descontos maiores que o acrescimo onde será gravado no movto o calculo líquido, para efeito de bater débito e crédito,
//        // onde neste caso para acharmos qual o valor base, já que gravamos o valor descontado, executaremos este método.
//        // Aplicará este método em movimentos de Recebimentos(C) ou Pagamentos(D).
//
//
//
//        // Este método será aplicado em todos os valores de movtos baixados para achar o VALOR BASE (não calculado),
//
//        // levando
//        // se o acrescimo for maior igual a zero que o desconto o calculoa é esse : valor = valor + a - d
//
//        if (acrescimo > desconto) {
//            return valor + acrescimo - desconto;
//        } else {
//            return Moeda.somaValores(
//                    valor,
//                    Moeda.subtracaoValores(
//                    desconto,
//                    Moeda.divisaoValores(
//                    Moeda.multiplicarValores(
//                    desconto,
//                    acrescimo),
//                    desconto)));
//        }
//    }
//
//    public float[] somaComplemento() {
//        float[] arrayComplemento = new float[3];
//        //this.getListaComplemento();
//        int i = 0;
//        while (i < listaComplemento.size()) {
//            arrayComplemento[0] += listaComplemento.get(i).getMulta();
//            arrayComplemento[1] += listaComplemento.get(i).getJuros();
//            arrayComplemento[2] += listaComplemento.get(i).getCorrecao();
//            i++;
//        }
//        return arrayComplemento;
//    }
//
//    public String baixarInconsequente(Rotina rotina, Date dataPagamento) {
//        String mensagem = "";
//        FinanceiroDB financeiroDB = new FinanceiroDBToplink();
//        financeiroDB.abrirTransacao();
//        int i = 0;
//        try {
//            Lote lote = new Lote(
//                    -1,
//                    rotina,
//                    "R",
//                    DataHoje.converteData(dataPagamento));
//            financeiroDB.acumularObjeto(lote);
//            while (i < lista.size()) {
//                lista.get(i).setLote(lote);
//                financeiroDB.acumularObjeto(lista.get(i));
//                i++;
//            }
//            financeiroDB.comitarTransacao();
//            mensagem = "Baixa efetuada com sucesso!";
//        } catch (Exception e) {
//            financeiroDB.desfazerTransacao();
//            mensagem = "Indice :" + i + " " + e.getMessage();
//        }
//        return mensagem;
//    }
//
//    public void gerarMovimentoComplementoValor(List<ComplementoValor> complemento, String[] totais) {
//        FinanceiroDB finDB = new FinanceiroDBToplink();
//        ServicosDB dbServ = new ServicosDBToplink();
//        FilialDB filDB = new FilialDBToplink();
//        PessoaDB pesDB = new PessoaDBToplink();
//        TipoServicoDB tipoServicoDB = new TipoServicoDBToplink();
//        int i = 0;
//        i = 0;
//        while (i < complemento.size()) {
//            finDB.insert(complemento.get(i));
//            i++;
//        }
//
//        i = 0;
//        Movimento[] baixar = new Movimento[5];
//        int idServ = 0;
//        while (i < 5) {
//            switch (i) {
//                case 0:
//                    idServ = 7;
//                    break;
//                case 1:
//                    idServ = 6;
//                    break;
//                case 2:
//                    idServ = 11;
//                    break;
//                case 3:
//                    idServ = 10;
//                    break;
//                case 4:
//                    idServ = 8;
//                    break;
//            }
//
//            baixar[i] = new Movimento(
//                    -1,
//                    complemento.get(0).getMovimento().getLote(),
//                    null,
//                    dbServ.pesquisaCodigo(idServ).getPlano5Debito(),
//                    null,
//                    filDB.pesquisaCodigo(1),
//                    pesDB.pesquisaCodigo(0),
//                    complemento.get(0).getMovimento().getVencimento(),
//                    "",
//                    Float.parseFloat(totais[i]),
//                    -1,
//                    "D",
//                    complemento.get(0).getMovimento().getLoteBaixa(),
//                    dbServ.pesquisaCodigo(idServ),
//                    tipoServicoDB.pesquisaCodigo(1),
//                    complemento.get(0).getMovimento().getVencimento().substring(4, complemento.get(0).getMovimento().getVencimento().length()));
//            i++;
//        }
//    }
//
//    public void setListaComplemento(List<ComplementoValor> listaComplemento) {
//        this.listaComplemento = listaComplemento;
//    }
//
//    private List<ComplementoValor> xxxxcalculoAcrescimo(int i, Date data) {
//        try {
//            if (listaComplemento.isEmpty()) {
//                for (int j = 0; j < lista.size(); j++) {
//                    listaComplemento.add(new ComplementoValor());
//                }
//            }
//
//            ServicosDB servDB = new ServicosDBToplink();
//            CorrecaoDB corrDB = new CorrecaoDBToplink();
//            FolhaEmpresa folha = null;
//            float acumularCorrecao = 0;
//            float juros = 0;
//            float multa = 0;
//            float valor = lista.get(i).getValor();
//            int ano = 0;
//            int mes = 0;
//            float valorIndice = 0;
//            Correcao correcao = corrDB.pesquisaCorrecao(
//                    lista.get(i).getServicos(),
//                    lista.get(i).getReferencia());
//            FolhaEmpresaDB dbFolha = new FolhaEmpresaDBToplink();
//            folha = dbFolha.pesquisaPorPessoa(
//                    lista.get(i).getPessoa().getId(),
//                    lista.get(i).getTipoServico().getId(),
//                    lista.get(i).getReferencia());
//            IndiceMensal indice = null;
//            if (correcao != null) {
//                indice = servDB.pesquisaIndiceMensal(
//                        DataHoje.DataToArrayInt(data)[1],
//                        DataHoje.DataToArrayInt(data)[2],
//                        correcao.getIndice().getId());
//                if (indice != null) {
//                    valorIndice = indice.getValor();
//                }
//            } else {
//                indice = null;
//                valorIndice = 0;
//            }
//            int data1 = DataHoje.converteDataParaInteger(DataHoje.converteData(data));
//            int data2 = DataHoje.converteDataParaInteger(DataHoje.data());
//            if ((data1 < data2) && (correcao != null)) {
//
//                long Ddia = 0;
//                int dmes = 0;
//                int DfAno = 0;
//
//
//                Ddia = DataHoje.calculoDosDias(data, DataHoje.dataHoje());
//                dmes = DataHoje.ArrayDataHojeInt()[1] - DataHoje.DataToArrayInt(data)[1];
//                DfAno = DataHoje.ArrayDataHojeInt()[2] - DataHoje.DataToArrayInt(data)[2];
//
//                if (Ddia < 0) {
//                    Ddia = Ddia * -1;
//                }
//                if (dmes < 0) {
//                    dmes = dmes * -1;
//                }
//                if (DfAno < 0) {
//                    DfAno = DfAno * -1;
//                }
//                int j = 0;
//                int k = 0;
//
//                if (folha != null) {
//                    multa = Moeda.somaValores(multa,
//                            ((Moeda.multiplicarValores(correcao.getMultaPorFuncionario(), folha.getNumFuncionarios()))));
//                    multa = Moeda.multiplicarValores(multa, Float.parseFloat(Integer.toString(dmes + 1)));
//                }
//
//
//                if ((dmes == 0) && (DfAno == 0) && (Ddia != 0) && (valor != 0)) {
//                    juros = Moeda.somaValores(juros,
//                            ((Moeda.multiplicarValores(correcao.getJurosPriMes(), valor)) / 100));
//                    multa = Moeda.somaValores(multa,
//                            ((Moeda.multiplicarValores(correcao.getMultaPriMes(), valor)) / 100));
//                    while (j < Ddia) {
//                        juros = Moeda.somaValores(juros,
//                                ((Moeda.multiplicarValores(correcao.getJurosDiarios(), valor)) / 100));
//                        j++;
//                    }
//                    j = 0;
//                } else {
//                    if (DfAno > 0) {
//                        if (dmes == 0) {
//                            dmes = 12 * DfAno;
//                        } else if ((dmes > 0) && (DfAno > 0)) {
//                            dmes += DfAno * 12;
//                        } else {
//                            dmes = (12 - dmes) + (12 * (DfAno - 1));
//                        }
//                    }
//
//                    if (valor != 0) {
//                        if (Moeda.multiplicarValores(correcao.getJurosPriMes(), valor) != 0) {
//                            juros = Moeda.somaValores(juros,
//                                    ((Moeda.multiplicarValores(correcao.getJurosPriMes(), valor)) / 100));
//                        } else {
//                            juros = 0;
//                        }
//
//                        if (Moeda.multiplicarValores(correcao.getMultaPriMes(), valor) != 0) {
//                            multa = Moeda.somaValores(multa,
//                                    ((Moeda.multiplicarValores(correcao.getMultaPriMes(), valor)) / 100));
//                        } else {
//                            multa = 0;
//                        }
//                    }
//
//                    BigDecimal acumularCorrecaoB = new BigDecimal(0);
//                    BigDecimal valorCorrecaoB = new BigDecimal(0);
//                    BigDecimal valorIndiceB = new BigDecimal(Float.toString(valorIndice));
//                    acumularCorrecaoB = new BigDecimal(Float.toString(valor));
//                    String teste = "";
//                    BigDecimal testeB = new BigDecimal(0);
//
//                    if (valorIndiceB.floatValue() != 0) {
//                        testeB = valorIndiceB.multiply(acumularCorrecaoB, new MathContext(4));
//                        teste = testeB.toString();
//                        valorCorrecaoB = testeB.divide(new BigDecimal(100), new MathContext(4));
//                        teste = valorCorrecaoB.toString();
//                    }
//
//                    j = 1;
//                    while (j <= dmes) {
//
//
//                        testeB = acumularCorrecaoB;
//                        teste = testeB.toString();
//                        testeB = valorCorrecaoB;
//                        teste = testeB.toString();
//                        acumularCorrecaoB = acumularCorrecaoB.add(valorCorrecaoB);
//                        testeB = acumularCorrecaoB;
//                        teste = testeB.toString();
//
//                        try {
//                            mes = DataHoje.DataToArrayInt((new DataHoje()).incrementarMeses(j, DataHoje.converteData(data)))[1];
//                            ano = DataHoje.DataToArrayInt((new DataHoje()).incrementarMeses(j, DataHoje.converteData(data)))[2];
//                            correcao = corrDB.pesquisaCorrecao(lista.get(i).getServicos(), lista.get(i).getReferencia());
//                            indice = servDB.pesquisaIndiceMensal(mes, ano, correcao.getIndice().getId());
//                            if (indice == null) {
//                                valorIndiceB = new BigDecimal(0);
//                            } else {
//                                valorIndiceB = new BigDecimal(Float.toString(indice.getValor()));
//                            }
//                            if (valorIndiceB.floatValue() != 0) {
//                                testeB = valorIndiceB.multiply(acumularCorrecaoB, new MathContext(4));
//                                teste = testeB.toString();
//                                valorCorrecaoB = testeB.divide(new BigDecimal(100), new MathContext(4));
//                                teste = valorCorrecaoB.toString();
//                            } else {
//                                valorCorrecaoB = new BigDecimal(0);
//                            }
//                        } catch (Exception e) {
//                            indice = null;
//                            correcao = null;
//                        }
//                        j++;
//                    }
//
//                    acumularCorrecao = acumularCorrecaoB.floatValue();
//
//                    if (valor != 0) {
//                        if (Moeda.multiplicarValores(correcao.getJurosApartir2Mes(), valor) != 0) {
//                            juros = Moeda.somaValores(juros,
//                                    ((Moeda.multiplicarValores(correcao.getJurosApartir2Mes(), valor)) / 100) * dmes);
//                        } else {
//                            juros = 0;
//                        }
//
//                        if (Moeda.multiplicarValores(correcao.getMultaApartir2Mes(), valor) != 0) {
//                            multa = Moeda.somaValores(multa,
//                                    ((Moeda.multiplicarValores(correcao.getMultaApartir2Mes(), valor)) / 100) * dmes);
//                        } else {
//                            multa = 0;
//                        }
//
//                        juros = Moeda.somaValores(juros,
//                                ((Moeda.multiplicarValores(correcao.getJurosPriMes(), valor)) / 100));
//                        multa = Moeda.somaValores(multa,
//                                ((Moeda.multiplicarValores(correcao.getMultaPriMes(), valor)) / 100));
//                        juros = Moeda.somaValores(juros,
//                                ((Moeda.multiplicarValores(correcao.getJurosDiarios(), valor)) / 100) * Ddia);
//                    }
//
//                }
//                acumularCorrecao = Moeda.converteFloatR$Float(Moeda.subtracaoValores(acumularCorrecao, valor));
//            }
//            listaComplemento.get(i).setMulta(Moeda.converteFloatR$Float(multa));
//            listaComplemento.get(i).setJuros(Moeda.converteFloatR$Float(juros));
//            if (acumularCorrecao == 0) {
//                listaComplemento.get(i).setCorrecao(0);
//            } else {
//                listaComplemento.get(i).setCorrecao(acumularCorrecao);
//            }
//        } catch (Exception e) {
//            String a = e.getMessage();
//        }
//        return listaComplemento;
//    }
//
//    public synchronized ComplementoValor calculoComplemento(int i, Date data){
//        ComplementoValor complemento = new ComplementoValor();
//        try {
//            if((lista.get(i).getValor() != 0) &&
//               (data != null)){
//
//                ServicosDB servDB = new ServicosDBToplink();
//                CorrecaoDB corrDB = new CorrecaoDBToplink();
//                FolhaEmpresa folha = null;
//                float acumularCorrecao = 0;
//                float juros = 0;
//                float multa = 0;
//                float valor = lista.get(i).getValor();
//                int ano = 0;
//                int mes = 0;
//                float valorIndice = 0;
//                Correcao correcao = corrDB.pesquisaCorrecao(
//                        lista.get(i).getServicos(),
//                        lista.get(i).getReferencia());
//                FolhaEmpresaDB dbFolha = new FolhaEmpresaDBToplink();
//                folha = dbFolha.pesquisaPorPessoa(
//                        lista.get(i).getPessoa().getId(),
//                        lista.get(i).getTipoServico().getId(),
//                        lista.get(i).getReferencia());
//                IndiceMensal indice = null;
//                if (correcao != null) {
//                    indice = servDB.pesquisaIndiceMensal(
//                            DataHoje.DataToArrayInt(data)[1],
//                            DataHoje.DataToArrayInt(data)[2],
//                            correcao.getIndice().getId());
//                    if (indice != null) {
//                        valorIndice = indice.getValor();
//                    }
//                } else {
//                    indice = null;
//                    valorIndice = 0;
//                }
//                int data1 = DataHoje.converteDataParaInteger(DataHoje.converteData(data));
//                int data2 = DataHoje.converteDataParaInteger(DataHoje.data());
//                if ((data1 < data2) && (correcao != null)) {
//
//                    long Ddia = 0;
//                    int dmes = 0;
//                    int DfAno = 0;
//
//
//                    Ddia   = DataHoje.calculoDosDias(data, DataHoje.dataHoje());
////                  dmes  = DataHoje.ArrayDataHojeInt()[1] - DataHoje.DataToArrayInt(data)[1];//MES FINAL-MES INICIAL
//                    DfAno = DataHoje.ArrayDataHojeInt()[2] - DataHoje.DataToArrayInt(data)[2];
////--------------------------Rogério-----------------
//                        Calendar dataIni = Calendar.getInstance();
//                        dataIni.setTime(data);//onde dataIncial é um objeto do tipo Date representando sua data inicial
//                        Calendar dataFim = Calendar.getInstance();
//                        dataFim.setTime(DataHoje.dataHoje());//onde dataFinal é um objeto do tipo Date representando sua data final
//
//                        dmes = 0;
//
//                        while(dataIni.before(dataFim)){
//                           dmes++;
//                           dataIni.add(Calendar.MONTH, 1);
//                          }
//                        dmes = dmes - 1;
//                       //  dmes=9;///Rogerio 30/04/2010 a 03/01/2011 não conta mes atual
////--------------------------------------------
//
//
//
//                    if (Ddia < 0) {
//                        Ddia = Ddia * -1;
//                    }
//                    if (dmes < 0) {
//                        dmes = dmes * -1;
//                    }
//                    if (DfAno < 0) {
//                        DfAno = DfAno * -1;
//                    }
//                    int j = 0;
//                    int k = 0;
//
//                    if (folha != null) {
//                        multa = Moeda.somaValores(multa,
//                                ((Moeda.multiplicarValores(correcao.getMultaPorFuncionario(), folha.getNumFuncionarios()))));
//                        multa = Moeda.multiplicarValores(multa, Float.parseFloat(Integer.toString(dmes)));
//                    }
//
//
//                    if ((dmes == 0) && (DfAno == 0) && (Ddia != 0) && (valor != 0)) {
//                        juros = Moeda.somaValores(juros,
//                                ((Moeda.multiplicarValores(correcao.getJurosPriMes(), valor)) / 100));
//                        multa = Moeda.somaValores(multa,
//                                ((Moeda.multiplicarValores(correcao.getMultaPriMes(), valor)) / 100));
//                        while (j < Ddia) {
//                            juros = Moeda.somaValores(juros,
//                                    ((Moeda.multiplicarValores(correcao.getJurosDiarios(), valor)) / 100));
//                            j++;
//                        }
//                        j = 0;
//                    }
//                    else {
//                        if (DfAno > 0) {
//
//                            if (dmes == 0) {
////                                dmes = 12 * DfAno;
//                            } else if ((dmes > 0) && (DfAno > 0)) {
////                                dmes += DfAno * 12;
//                            } else {
////                                dmes = (12 - dmes) + (12 * (DfAno - 1));
//                            }
//                        }
//                        if (valor != 0) {
//                            if (Moeda.multiplicarValores(correcao.getJurosPriMes(), valor) != 0) {
//                                juros = Moeda.somaValores(juros,
//                                        ((Moeda.multiplicarValores(correcao.getJurosPriMes(), valor)) / 100));
//                            } else {
//                                juros = 0;
//                            }
//
//                            if (Moeda.multiplicarValores(correcao.getMultaPriMes(), valor) != 0) {
//                                multa = Moeda.somaValores(multa,
//                                        ((Moeda.multiplicarValores(correcao.getMultaPriMes(), valor)) / 100));
//                            } else {
//                                multa = 0;
//                            }
//                        }
//
//                        BigDecimal acumularCorrecaoB = new BigDecimal(0);
//                        BigDecimal valorCorrecaoB = new BigDecimal(0);
//                        BigDecimal valorIndiceB = new BigDecimal(Float.toString(valorIndice));
//                        acumularCorrecaoB = new BigDecimal(Float.toString(valor));
//                        String teste = "";
//                        BigDecimal testeB = new BigDecimal(0);
//
//                        if (valorIndiceB.floatValue() != 0) {
//                            testeB = valorIndiceB.multiply(acumularCorrecaoB, new MathContext(4));
//                            teste = testeB.toString();
//                            valorCorrecaoB = testeB.divide(new BigDecimal(100), new MathContext(4));
//                            teste = valorCorrecaoB.toString();
//                        }
//
//                        j = 1;
//                        while (j <= dmes) {
//
//
//                            testeB = acumularCorrecaoB;
//                            teste = testeB.toString();
//                            testeB = valorCorrecaoB;
//                            teste = testeB.toString();
//                            acumularCorrecaoB = acumularCorrecaoB.add(valorCorrecaoB);
//                            testeB = acumularCorrecaoB;
//                            teste = testeB.toString();
//
//                            try {
//                                mes = DataHoje.DataToArrayInt((new DataHoje()).incrementarMeses(j, DataHoje.converteData(data)))[1];
//                                ano = DataHoje.DataToArrayInt((new DataHoje()).incrementarMeses(j, DataHoje.converteData(data)))[2];
//                                correcao = corrDB.pesquisaCorrecao(lista.get(i).getServicos(), lista.get(i).getReferencia());
//                                indice = servDB.pesquisaIndiceMensal(mes, ano, correcao.getIndice().getId());
//                                if (indice == null) {
//                                    valorIndiceB = new BigDecimal(0);
//                                } else {
//                                    valorIndiceB = new BigDecimal(Float.toString(indice.getValor()));
//                                }
//                                if (valorIndiceB.floatValue() != 0) {
//                                    testeB = valorIndiceB.multiply(acumularCorrecaoB, new MathContext(4));
//                                    teste = testeB.toString();
//                                    valorCorrecaoB = testeB.divide(new BigDecimal(100), new MathContext(4));
//                                    teste = valorCorrecaoB.toString();
//                                } else {
//                                    valorCorrecaoB = new BigDecimal(0);
//                                }
//                            } catch (Exception e) {
//                                indice = null;
//                                correcao = null;
//                            }
//                            j++;
//                        }
//
//                        acumularCorrecao = acumularCorrecaoB.floatValue();
//
//                        if (valor != 0) {
//                            if (Moeda.multiplicarValores(correcao.getJurosApartir2Mes(), valor) != 0) {
//                                juros = Moeda.somaValores(juros,
//                                        ((Moeda.multiplicarValores(correcao.getJurosApartir2Mes(), valor)) / 100) * dmes);
//                            } else {
//                              //  juros = 0;
//                            }
//
//                            if (Moeda.multiplicarValores(correcao.getMultaApartir2Mes(), valor) != 0) {
//                                multa = Moeda.somaValores(multa,
//                                        ((Moeda.multiplicarValores(correcao.getMultaApartir2Mes(), valor)) / 100) * dmes);
//                            } else {
//                              //  multa = 0;
//                            }
////
//                   //         juros = Moeda.somaValores(juros,
//                     //               ((Moeda.multiplicarValores(correcao.getJurosPriMes(), valor)) / 100));
//                      //      multa = Moeda.somaValores(multa,
//                      //              ((Moeda.multiplicarValores(correcao.getMultaPriMes(), valor)) / 100));
//                      //      juros = Moeda.somaValores(juros,
//                      //              ((Moeda.multiplicarValores(correcao.getJurosDiarios(), valor)) / 100) * Ddia);
//                        }
//
//                    }
//                    acumularCorrecao = Moeda.converteFloatR$Float(Moeda.subtracaoValores(acumularCorrecao, valor));
//                }
//                complemento.setMulta(Moeda.converteFloatR$Float(multa));
//                complemento.setJuros(Moeda.converteFloatR$Float(juros));
//                if (acumularCorrecao <= 0) {
//                    complemento.setCorrecao(0);
//                } else {
//                    complemento.setCorrecao(acumularCorrecao);
//                }
//            }
//        } catch (Exception e) {
//            String a = e.getMessage();
//        }
//        return complemento;
//    }
//
//    public synchronized ComplementoValor calculoAcrescimo(int i, Date data) {
//        ComplementoValor complemento = calculoComplemento(i, data);
//
//        listaComplemento.add(complemento);
//        return complemento;
//    }
//
//    public synchronized ComplementoValor recalcularComplemento(int i, Date data) {
//        ComplementoValor complemento = calculoComplemento(i, data);
//        listaComplemento.set(i, complemento);
//        return complemento;
//    }
//
//    public ComplementoValor getComplemento(int i) {
//        Date data = getVencimentoOriginal(i);
//        return getComplemento(i, data);
//    }
//
//    public synchronized  ComplementoValor getComplemento(int i, Date data) {
//        if (data == null) {
//            data = getVencimentoOriginal(i);
//        }
//        ComplementoValor complemento = null;
//        try{
//            complemento = listaComplemento.get(i);
//        }catch(Exception e){
//            complemento = calculoAcrescimo(i, data);
//        }
//        return complemento;
//    }
//
//    public List<ComplementoValor> getListaComplemento() {
//        Date data = null;
//        int i = 0;
//        try {
//            listaComplemento.clear();
//            while (i < lista.size()) {
//                data = DataHoje.converte(geraVencimentoParaReferencia(lista.get(i).getReferencia(), i));
//                calculoAcrescimo(i, data);
//                i++;
//            }
//
//        } catch (Exception e) {
//            String a = e.getMessage();
//        }
//        return listaComplemento;
//    }
//
//    public Date getVencimentoOriginal(int i) {
//        Date data = null;
//        if (getLista().get(i).getTipoServico() != null){
//            String dataSwap = geraVencimentoParaReferencia(lista.get(i).getReferencia(), i);
//            int data1 = 0;
//            if (dataSwap != null){
//                data1 = DataHoje.converteDataParaInteger(dataSwap);
//            }
//            int data2 = DataHoje.converteDataParaInteger(DataHoje.data());
//            if ((getLista().get(i).getTipoServico().getId() == 4)
//                    && (getLista().get(i).getServicos().getId() != 1)) {
//                data = getLista().get(i).getDtVencimento();
//            } else {
//                if (data1 < data2) {
//                    data = DataHoje.converte(geraVencimentoParaReferencia(lista.get(i).getReferencia(), i));
//                } else {
//                    data = getLista().get(i).getDtVencimento();
//                }
//            }
//        }else{
//            data = lista.get(i).getDtVencimento();
//        }
//        return data;
//    }
//
//    public Float[] valorApurado(ComplementoValor complemento, int indice, List marcados) {
//        Float[] result = new Float[3];
//        int i = 0;
//        float pD = 0; // percentual desconto
//        float dpA = 0; // desconto por acrescimo
//        float tmp = 0;
//        float soma = 0;
//        soma = 0;
//        soma = Moeda.somaValores(soma, complemento.getMulta());
//        soma = Moeda.somaValores(soma, complemento.getJuros());
//        soma = Moeda.somaValores(soma, complemento.getCorrecao());
//
//        if ((marcados.size() == 0) || (listaDesconto == null)) {
//            return null;
//        }
//
//        if (listaDesconto.get(indice) == 0) {
//            return null;
//        }
//
//        if (listaDesconto.get(indice) < soma) {
//            pD = Moeda.divisaoValores(listaDesconto.get(indice), soma);
//            dpA = Moeda.multiplicarValores(pD, complemento.getMulta());
//            tmp = Moeda.subtracaoValores(complemento.getMulta(), Moeda.converteFloatR$Float(dpA));
//            result[0] = tmp;
//
//            dpA = Moeda.multiplicarValores(pD, complemento.getJuros());
//            tmp = Moeda.subtracaoValores(complemento.getJuros(), Moeda.converteFloatR$Float(dpA));
//            result[1] = tmp;
//
//            dpA = Moeda.multiplicarValores(pD, complemento.getCorrecao());
//            tmp = Moeda.subtracaoValores(complemento.getCorrecao(), Moeda.converteFloatR$Float(dpA));
//            result[2] = tmp;
//            return result;
//        } else {
//            return new Float[]{Moeda.subtracaoValores(soma, listaDesconto.get(indice))};
//        }
//    }
//
//    public float getSomatoria() {
//        somatoria = 0;
//        for (int o = 0; o < lista.size(); o++) {
//            somatoria = Moeda.somaValores(somatoria, lista.get(o).getValor());
//        }
//        return somatoria;
//    }
//
//    public float getSomatoriaAtivo() {
//        somatoria = 0;
//        for (int o = 0; o < lista.size(); o++) {
//            if (lista.get(o).getAtivo() == 1) {
//                somatoria = Moeda.somaValores(somatoria, lista.get(o).getValor());
//            }
//        }
//        return somatoria;
//    }
//
//    public float somatoriaAcordo() {
//        float somatoriaA = 0;
//        for (int o = 0; o < listaAcordo.size(); o++) {
//            somatoriaA = Moeda.somaValores(somatoriaA, listaAcordo.get(o).getValor());
//        }
//        return somatoriaA;
//    }
//
//    public void setSomatoria(float somatoria) {
//        this.somatoria = somatoria;
//    }
//
//    public float getDesconto() {
//        return desconto;
//    }
//
//    public void setDesconto(float desconto) {
//        this.desconto = desconto;
//    }
//
///*
//    public synchronized  List<Float> getListaDesconto(List<Integer> m) {
//        listaDesconto = new ArrayList<Float>();
//        if (desconto == 0) {
//            return listaDesconto;
//        }
//        float total = valorTotalMarcados(m);
////        List<Float> totalIndi = valorTotalParaMovimento();
//        List<Float> totalIndi = valorTotalAcrescimo(); //Rogerio
//        float pD = 0; // percentual desconto
//        float dpA = 0; // desconto por acrescimo
//        int i = 0;
//        float soma = 0;
//        List<Integer> listI = new ArrayList<Integer>(m);
//        try {
////            pD = Moeda.divisaoValores(getDesconto(), total);
//            pD = Moeda.divisaoValores(getDesconto(), total); //Rogerio
//            int j = 0;
//            while (i < lista.size()) {
//                while (j < listI.size()) {
//                    if (listI.get(j) == i) {
//                        dpA = Moeda.multiplicarValores(pD, totalIndi.get(i));
//                        soma = Moeda.somaValores(Moeda.converteFloatR$Float(dpA), soma);
//                        listaDesconto.add(Moeda.converteFloatR$Float(dpA));
//                        listI.remove(listI.indexOf(listI.get(j)));
//                        break;
//                    }
//                    j++;
//                }
//                j = 0;
//                if (i == listaDesconto.size()) {
//                    listaDesconto.add((float) 0.0);
//                }
//                i++;
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        float diferenca = 0;
//        if (soma > desconto) {
//            diferenca = Moeda.subtracaoValores(soma, desconto);
//            listaDesconto.set(
//                    listaDesconto.size() - 1,
//                    Moeda.subtracaoValores(
//                    listaDesconto.get(listaDesconto.size() - 1),
//                    diferenca));
//        }
//        return listaDesconto;
//    }
//
//
//
//     public List<Float> getListaDesconto() {
//        listaDesconto = new ArrayList<Float>();
//        if (desconto == 0) {
//            return listaDesconto;
//        }
//        float total = valorTotal();
//        List<Float> totalIndi = valorTotalParaMovimento();
//        float pD = 0; // percentual desconto
//        float dpA = 0; // desconto por acrescimo
//        int i = 0;
//        float soma = 0;
//        try {
//            pD = Moeda.divisaoValores(getDesconto(), total);
//            while (i < lista.size()) {
//                dpA = Moeda.multiplicarValores(pD, totalIndi.get(i));
//                soma = Moeda.somaValores(Moeda.converteFloatR$Float(dpA), soma);
//                listaDesconto.add(Moeda.converteFloatR$Float(dpA));
//                i++;
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        float diferenca = 0;
//        if (soma > desconto) {
//            diferenca = Moeda.subtracaoValores(soma, desconto);
//            listaDesconto.set(
//                    listaDesconto.size() - 1,
//                    Moeda.subtracaoValores(
//                    listaDesconto.get(listaDesconto.size() - 1),
//                    diferenca));
//        }
//        return listaDesconto;
//    }
//
//
//
// */
//
//
////------------------------------ Rogério
//
//        public synchronized  List<Float> getListaDesconto(List<Integer> m) {
//        listaDesconto = new ArrayList<Float>();
//        if (desconto == 0) {
//            return listaDesconto;
//        }
//        float total = 0;//valorTotalMarcados(m);
////        List<Float> totalIndi = valorTotalParaMovimento();//henrique
//        List<Float> totalIndi = valorTotalAcrescimo(); //Rogerio //07-12-2011
//        int i = 0;
//        int j = 0;
////---------------------valor Total do Acrescimo -------------------------------
///*
//              ArrayList<Float>totalIndi = new ArrayList<Float>();
//              float xm=0;
//              float xj=0;
//              float xc=0;
//              while (i < lista.size()) {
//                  xm=listaComplemento.get(i).getMulta(); //getComplemento(i).getMulta();
//                  xj =listaComplemento.get(i).getJuros();
//                  xc=listaComplemento.get(i).getCorrecao();
//                  totalIndi.add(
///*
//                    Moeda.somaValores(
//                    Moeda.somaValores(
//                    Moeda.somaValores(
//                    0,
//                    getComplemento(i).getMulta()),
//                    getComplemento(i).getJuros()),
//                    getComplemento(i).getCorrecao()));
//                     xm);
//                    i++;
//             }
//*/
////-------------------Valor total do Desconto
//        i = 0;
//        j = 0;
//        while (j < m.size()) {
//            while (i < totalIndi.size()) {
//                if (m.get(j) == i) {
//                    total = Moeda.somaValores(
//                            totalIndi.get(i),
//                            total);
//                }
//                i++;
//            }
//            i = 0;
//            j++;
//        }
////-------------------------
//        i = 0;
//        float pD  = 0; // percentual desconto
//        float dpA = 0; // desconto por acrescimo
//        float soma = 0;
//        float proporcao = 0; // corresponde ao % do acrescimo da linha pelo acrescimo geral
//        List<Integer> listI = new ArrayList<Integer>(m);
////------------------------------------------------------------
//        try {
//
//           // pD = Moeda.divisaoValores(getDesconto(), total); //Rogerio
//            j = 0;
//            while (i < lista.size()) {
//                while (j < listI.size()) {
//                    if (listI.get(j) == i) {
////                        dpA = Moeda.multiplicarValores(pD, totalIndi.get(i));//rogerio
//                        proporcao = Moeda.divisaoValores(totalIndi.get(i),total/100);//% desc.da linha
//                        dpA = Moeda.multiplicarValores(desconto, proporcao/100);//calculo do desc $ da linha
//                        soma = Moeda.somaValores(Moeda.converteFloatR$Float(dpA), soma);
//                        listaDesconto.add(Moeda.converteFloatR$Float(dpA));
//                        listI.remove(listI.indexOf(listI.get(j)));
//                        break;
//                    }
//                    j++;
//                }
//                j = 0;
//                if (i == listaDesconto.size()) {
//                    listaDesconto.add((float) 0.0);
//                }
//                i++;
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        float diferenca = 0;
//        if (soma > desconto) {
//            diferenca = Moeda.subtracaoValores(soma, desconto);
//            listaDesconto.set(
//                    listaDesconto.size() - 1,
//                    Moeda.subtracaoValores(
//                    listaDesconto.get(listaDesconto.size() - 1),
//                    diferenca));
//        }
//        return listaDesconto;
//    }
//
//  //--------rogerio------------------------------------------
//
//    public List<Float> getListaDesconto() {
//        //float pD  = 0; // percentual desconto
//        float dpA = 0; // desconto por acrescimo
//        int i = 0;
//        float soma = 0;
//        float proporcao = 0; // corresponde ao % do acrescimo da linha pelo acrescimo geral
//        listaDesconto = new ArrayList<Float>();
//        if (desconto == 0) {
//            return listaDesconto;
//        }
//        float total = valorTotalAcrescimoFloat();
////        List<Float> totalIndi = valorTotalParaMovimento();
//        List<Float> totalIndi = valorTotalAcrescimo();
////        List<Integer> listI = new ArrayList<Integer>(m);
//        try {
//            //pD = Moeda.divisaoValores(getDesconto(), total);
//            while (i < lista.size()) {
//                proporcao = Moeda.divisaoValores(totalIndi.get(i),total/100);//% desc.da linha
//                dpA = Moeda.multiplicarValores(desconto, proporcao/100);//calculo do desc $ da linha
//                soma = Moeda.somaValores(Moeda.converteFloatR$Float(dpA), soma);
//                listaDesconto.add(Moeda.converteFloatR$Float(dpA));
//                i++;
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        float diferenca = 0;
//        if (soma > desconto) {
//            diferenca = Moeda.subtracaoValores(soma, desconto);
//            listaDesconto.set(
//                    listaDesconto.size() - 1,
//                    Moeda.subtracaoValores(
//                    listaDesconto.get(listaDesconto.size() - 1),
//                    diferenca));
//        }
//        return listaDesconto;
//    }
//
//    public float valorTotal() {
//        float resultado = 0;
//        List<Float> totalIndi = valorTotalParaMovimento();
//        int i = 0;
//        while (i < lista.size()) {
//            resultado = Moeda.somaValores(
//                    totalIndi.get(i),
//                    resultado);
//            i++;
//        }
//        return resultado;
//    }
//
////----------------- Rogerio --------------------
//        public float valorTotalAcrescimoFloat() {
//        float resultado = 0;
//        List<Float> totalIndi = valorTotalAcrescimo();
//        float zero=0;
//        int i = 0;
//        while (i < lista.size()) {
//            if (lista.get(i).getServicos().getId()!=1)//Se nao for sindical calcula acrescimo
//            {
//
//            resultado = Moeda.somaValores(
//                    totalIndi.get(i),
//                    resultado);
//            }
//            i++;
//        }
//        return resultado;
//    }
////--------------------------------------------------------------------
//    public float valorTotalMarcados(List<Integer> m) {
//        float resultado = 0;
////        List<Float> totalIndi = valorTotalParaMovimento();
//        List<Float> totalIndi = valorTotalAcrescimo();
//        int i = 0;
//        int j = 0;
//        while (j < m.size()) {
//            while (i < lista.size()) {
//                if (m.get(j) == i) {
//                    resultado = Moeda.somaValores(
//                            totalIndi.get(i),
//                            resultado);
//                }
//                i++;
//            }
//            i = 0;
//            j++;
//        }
//        return resultado;
//    }
//
//    ///////Criado por Rogério
//    public List<Float> valorTotalAcrescimo() {
//        List<Float> resultado = new ArrayList<Float>();
//        float zero=0;
//        int i = 0;
//        while (i < lista.size()) {
//            if (lista.get(i).getServicos().getId()!=1)//Se nao for sindical calcula acrescimo
//            {
//            resultado.add(
//                    Moeda.somaValores(
//                    Moeda.somaValores(
//                    listaComplemento.get(i).getMulta(), //getComplemento(i).getMulta()),
//                    listaComplemento.get(i).getJuros()), //getComplemento(i).getJuros()),
//                    listaComplemento.get(i).getCorrecao())); //getComplemento(i).getCorrecao()));
//            }
//            else
//               resultado.add(zero);
//
//            i++;
//        }
//        return resultado;
//    }
//
//
//    public List<Float> valorTotalParaMovimento() {
//        List<Float> resultado = new ArrayList<Float>();
//        int i = 0;
//        while (i < lista.size()) {
//            resultado.add(
//                    Moeda.somaValores(
//                    Moeda.somaValores(
//                    Moeda.somaValores(
//                    getLista().get(i).getValor(),
//
//                    listaComplemento.get(i).getMulta()), //getComplemento(i).getMulta()),
//                    listaComplemento.get(i).getJuros()),//getComplemento(i).getJuros()),
//                    listaComplemento.get(i).getCorrecao()));//getComplemento(i).getCorrecao()));
//            i++;
//        }
//        return resultado;
//    }
//
//    public Float valorTotalParaMovimento(int i) {
//        Float resultado = null;
//        ComplementoValor complementoValor = null;
//        try{
//            complementoValor = listaComplemento.get(i);
//        }catch(Exception e){
//            complementoValor = getComplemento(i);
//        }
//        resultado = Moeda.somaValores(
//                Moeda.somaValores(
//                Moeda.somaValores(
//                getLista().get(i).getValor(),
//                complementoValor.getMulta()),
//                complementoValor.getJuros()),
//                complementoValor.getCorrecao());
//        if (resultado == null) {
//            resultado = new Float(0);
//        }
//        return resultado;
//    }
//
//    public List<Float> valorTotalParaMovimentoComDesconto() {
//        List<Float> resultado = new ArrayList<Float>();
//        List<Float> arrayValor = valorTotalParaMovimento();
//        List<Float> arrayDesconto = getListaDesconto();
//        int i = 0;
//        if (arrayDesconto.isEmpty()) {
//            while (i < lista.size()) {
//                resultado.add(arrayValor.get(i));
//                i++;
//            }
//        } else {
//            while (i < lista.size()) {
//                resultado.add(
//                        Moeda.subtracaoValores(arrayValor.get(i), arrayDesconto.get(i)));
//                i++;
//            }
//        }
//        return resultado;
//    }
//
//    public Float valorTotalParaMovimentoComDesconto(int i) {
//        Float resultado = new Float(0);
//        float valor = valorTotalParaMovimento(i);
//        List<Float> arrayDesconto = getListaDesconto();
//        if (arrayDesconto.isEmpty()) {
//            if(listaComplemento.size() > i){
//                resultado = Moeda.subtracaoValores(valor, listaComplemento.get(i).getDesconto());
//            }else{
//                resultado = valor;
//            }
//        } else {
//            resultado = Moeda.subtracaoValores(valor, arrayDesconto.get(i));
//        }
//        return resultado;
//    }
//
//    public Object[] criarAcordos(Acordo acordo, Rotina rotina, List<Movimento> listaA, Historico historico) {
//        String result = "";
//        boolean ok = true;
//        FinanceiroDB financeiroDB = new FinanceiroDBToplink();
//        financeiroDB.abrirTransacao();
//        try {
//            MensagemConvencaoDB dbMen = new MensagemConvencaoDBToplink();
//            CnaeConvencaoDB dbConvencao = new CnaeConvencaoDBToplink();
//            GrupoCidadesDB dbGrupoCidades = new GrupoCidadesDBToplink();
//            MensagemConvencao mensagemConvencao = null;
//            int i = 0;
//            Usuario usuario = null;
//            Convencao convencao = null;
//            LoteBaixa loteBaixa = null;
//            Lote lote = null;
//
//
//
//            try {
//                usuario = financeiroDB.pesquisaUsuario(((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario")).getId());
//            } catch (Exception e) {
//                usuario = null;
//                result = "Usuário não encontrado";
//            }
//            acordo.setUsuario(usuario);
//            financeiroDB.acumularObjeto(acordo);
//
//
//            loteBaixa = new LoteBaixa(
//                    -1,
//                    usuario,
//                    null,
//                    new Date(),
//                    DataHoje.dataHoje(),
//                    null,
//                    null);
//
//            financeiroDB.acumularObjeto(loteBaixa);
//            while (i < lista.size()) {
//                lista.get(i).setGrupoAcordo(acordo);
//                lista.get(i).setAcordo(1);
//                lista.get(i).setLoteBaixa(loteBaixa);
//                lista.get(i).setAtivo(0);
//                lista.get(i).setControleBoleto(0);
//                financeiroDB.update(lista.get(i));
//                listaComplemento.get(i).setMovimento(lista.get(i));
//                financeiroDB.acumularObjeto(listaComplemento.get(i));
//                i++;
//            }
//
//            setListaAcordo(listaA);
//            lote = new Lote(
//                    -1,
//                    rotina,
//                    "R",
//                    DataHoje.data());
//            i = 0;
//            financeiroDB.acumularObjeto(lote);
//            String texto = historico.getHistorico();
//            while (i < listaAcordo.size()) {
//                listaAcordo.get(i).setLote(lote);
//                listaAcordo.get(i).setGrupoAcordo(acordo);
//                listaAcordo.get(i).setAcordo(0);
//                listaAcordo.get(i).setAtivo(1);
//                if (listaAcordo.get(i).getServicos().getId() != 1)
//                    listaAcordo.get(i).setReferencia(listaAcordo.get(i).getVencimento().substring(3));
//
//                convencao = dbConvencao.pesquisarCnaeConvencaoPorPessoa(listaAcordo.get(i).getPessoa().getId());
//
//                try {
//                    mensagemConvencao = dbMen.verificaMensagem(
//                            convencao.getId(),
//                            listaAcordo.get(i).getServicos().getId(),
//                            listaAcordo.get(i).getTipoServico().getId(),
//                            dbGrupoCidades.grupoCidadesPorPessoa(listaAcordo.get(i).getPessoa().getId(), convencao.getId()).getId(),
//                            "");
//                } catch (Exception e) {
//                    result = "Não pode consultar mensagem";
//                    break;
//                }
//
//                if (mensagemConvencao == null || mensagemConvencao.getId() == -1) {
//                    result = "Não existe mensagem";
//                    break;
//                }
//
//                financeiroDB.acumularObjeto(listaAcordo.get(i));
//                historico.setMovimento(listaAcordo.get(i));//financeiroDB.pesquisaCodigo(listaAcordo.get(i)));
//                historico.setComplemento(mensagemConvencao.getMensagemCompensacao());
//                financeiroDB.acumularObjeto(historico);
//                historico = new Historico(-1, null, texto, "");
//                i++;
//            }
//            financeiroDB.executarQuery("update fin_movimento set nr_ativo=0,nr_acordado=1 where nr_ativo =1 and id_acordo > 0 and id_tipo_servico<>4");
//            if (result.isEmpty()) {
//                financeiroDB.comitarTransacao();
//                result = "Acordo efetuado com sucesso";
//                ok = false;
//            } else {
//                financeiroDB.desfazerTransacao();
//            }
//        } catch (Exception e) {
//            result += " " + e.getMessage();
//            financeiroDB.desfazerTransacao();
//        }
//        return new Object[]{result, ok};
//    }
//
//    public String imprimirAcordo(boolean imprimeVerso, Acordo acordo, Historico historico) {
//        try {
//            FacesContext faces = FacesContext.getCurrentInstance();
//            HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
//            byte[] arquivo = new byte[0];
//            JasperReport jasper;
//            Collection vetor = new ArrayList();
//            PessoaEnderecoDB pesEndDB = new PessoaEnderecoDBToplink();
//            PessoaEndereco pe = null;
//            JuridicaDB jurDB = new JuridicaDBToplink();
//            Juridica juridica = new Juridica();
//            int i = 0;
//            String swap[] = new String[35];
//            Pessoa pessoa = null;
//            Filial filial = null;
//
//            jasper = (JasperReport) JRLoader.loadObject(
//                    ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/DEMOSTRATIVO_ACORDO.jasper"));
//
//            if (!listaAcordo.isEmpty()) {
//                filial = listaAcordo.get(0).getFilial();
//                pessoa = listaAcordo.get(0).getPessoa();
//            }
//
//            try {
//                juridica = jurDB.pesquisaJuridicaPorPessoa(pessoa.getId());
//                swap[0] = juridica.getPessoa().getNome();
//                swap[1] = juridica.getPessoa().getDocumento();
//            } catch (Exception e) {
//                swap[0] = "";
//                swap[1] = "";
//            }
//
//            try {
//                pe = pesEndDB.pesquisaEndPorPessoaTipo(pessoa.getId(), 2);
//                swap[2] = pe.getEndereco().getEnderecoSimplesToString();
//                swap[3] = pe.getNumero();
//                swap[4] = pe.getComplemento();
//                swap[5] = pe.getEndereco().getBairro().getBairro();
//                swap[6] = pe.getEndereco().getCidade().getCidade();
//                swap[7] = pe.getEndereco().getCidade().getUf();
//                swap[8] = pe.getEndereco().getCep().substring(0, 5) + "-" + pe.getEndereco().getCep().substring(5);
//                swap[23] = pessoa.getTelefone1();
//            } catch (Exception e) {
//                swap[2] = "";
//                swap[3] = "";
//                swap[4] = "";
//                swap[5] = "";
//                swap[6] = "";
//                swap[7] = "";
//                swap[8] = "";
//                swap[23] = "";
//            }
//
//            try {
//                pe = pesEndDB.pesquisaEndPorPessoaTipo(filial.getId(), 3);
//                swap[9] = filial.getMatriz().getPessoa().getNome();
//                swap[10] = filial.getMatriz().getPessoa().getTipoDocumento().getTipoDocumento();
//                swap[11] = pe.getEndereco().getDescricaoEndereco().getDescricaoEndereco();
//                swap[12] = pe.getEndereco().getLogradouro().getLogradouro();
//                swap[13] = pe.getNumero();
//                swap[14] = pe.getComplemento();
//                swap[15] = pe.getEndereco().getBairro().getBairro();
//                swap[16] = pe.getEndereco().getCidade().getCidade();
//                swap[17] = pe.getEndereco().getCidade().getUf();
//                swap[18] = pe.getEndereco().getCep().substring(0, 5) + "-" + pe.getEndereco().getCep().substring(5);
//                swap[19] = filial.getMatriz().getPessoa().getDocumento();
//                swap[20] = filial.getMatriz().getPessoa().getTelefone1();
//                swap[21] = filial.getMatriz().getPessoa().getSite();
//                swap[22] = filial.getMatriz().getPessoa().getEmail1();
//            } catch (Exception e) {
//                swap[9] = "";
//                swap[10] = "";
//                swap[11] = "";
//                swap[12] = "";
//                swap[13] = "";
//                swap[14] = "";
//                swap[15] = "";
//                swap[16] = "";
//                swap[17] = "";
//                swap[18] = "";
//                swap[19] = "";
//                swap[20] = "";
//                swap[21] = "";
//                swap[22] = "";
//            }
//
//            try {
//                pe = pesEndDB.pesquisaEndPorPessoaTipo(juridica.getContabilidade().getPessoa().getId(), 3);
//                swap[34] = juridica.getContabilidade().getPessoa().getNome();
//                swap[24] = pe.getEndereco().getDescricaoEndereco().getDescricaoEndereco();
//                swap[25] = pe.getEndereco().getLogradouro().getLogradouro();
//                swap[26] = pe.getNumero();
//                swap[27] = pe.getComplemento();
//                swap[28] = pe.getEndereco().getBairro().getBairro();
//                swap[29] = pe.getEndereco().getCep().substring(0, 5) + "-" + pe.getEndereco().getCep().substring(5);
//                swap[30] = pe.getEndereco().getCidade().getCidade();
//                swap[31] = pe.getEndereco().getCidade().getUf();
//                swap[32] = juridica.getContabilidade().getPessoa().getTelefone1();
//                swap[33] = juridica.getContabilidade().getPessoa().getEmail1();
//            } catch (Exception e) {
//                swap[34] = "";
//                swap[24] = "";
//                swap[25] = "";
//                swap[26] = "";
//                swap[27] = "";
//                swap[28] = "";
//                swap[29] = "";
//                swap[30] = "";
//                swap[31] = "";
//                swap[32] = "";
//                swap[33] = "";
//            }
//
//            while (i < listaAcordo.size()) {
//
//                vetor.add(new DemonstrativoAcordo(
//                        acordo.getId(), // codacordo
//                        acordo.getData(), // data
//                        acordo.getContato(), // contato
//                        swap[0], // razao
//                        swap[1], // cnpj
//                        swap[2], //endereco
//                        swap[3], // numero
//                        swap[4], // complemento
//                        swap[5], // bairro
//                        swap[6], // cidade
//                        swap[8], // cep
//                        swap[7], // uf
//                        swap[23], // telefone
//                        historico.getHistorico(), // obs
//                        listaAcordo.get(i).getServicos().getDescricao(), // desc_contribuicao
//                        listaAcordo.get(i).getNumero(), // boleto
//                        listaAcordo.get(i).getVencimento(), // vencto
//                        new BigDecimal(listaAcordo.get(i).getValor()), // vlrpagar
//                        ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Imagens/LogoCliente.png"), // sinLogo
//                        swap[9], // sinNome
//                        swap[11], // sinEndereco
//                        swap[12], // sinLogradouro
//                        swap[13], // sinNumero
//                        swap[14], // sinComplemento
//                        swap[15], // sinBairro
//                        swap[18], // sinCep
//                        swap[16], // sinCidade
//                        swap[17], // sinUF
//                        swap[20], // sinTelefone
//                        swap[22], // sinEmail
//                        swap[21], // sinSite
//                        swap[10], // sinTipoDocumento
//                        swap[19], // sinDocumento
//                        swap[34], // escNome
//                        swap[24], // escEndereco
//                        swap[25], // escLogradouro
//                        swap[26], // escNumero
//                        swap[27], // escComplemento
//                        swap[28], // escBairro
//                        swap[29], // escCep
//                        swap[30], // escCidade
//                        swap[31], // escUF
//                        swap[32], // escTelefone
//                        swap[33] // escEmail
//                        ));
//                i++;
//            }
//            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(vetor);
//            JasperPrint print = JasperFillManager.fillReport(
//                    jasper,
//                    null,
//                    dtSource);
//            arquivo = JasperExportManager.exportReportToPdf(print);
//            response.setContentType("application/pdf");
//            response.setContentLength(arquivo.length);
//            ServletOutputStream saida = response.getOutputStream();
//            saida.write(arquivo, 0, arquivo.length);
//            saida.flush();
//            saida.close();
//        } catch (Exception erro) {
//            System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
//        }
//        FacesContext.getCurrentInstance().responseComplete();
//        return null;
//    }
//
//    public List<Movimento> getListaAcordo() {
//        return listaAcordo;
//    }
//
//    public void setListaAcordo(List<Movimento> listaAcordo) {
//        this.listaAcordo = listaAcordo;
//    }
//
//    public void setListaDesconto(List<Float> listaDesconto) {
//        this.listaDesconto = listaDesconto;
//    }
//
//    public String geraVencimentoParaReferencia(String referencia, int indice) {
//        String vencimento = null;
//        MensagemConvencao mensagemConvencao = null;
//        try {
//            MensagemConvencaoDB menDB = new MensagemConvencaoDBToplink();
//            JuridicaDB jurDB = new JuridicaDBToplink();
//            FilialDB filDB = new FilialDBToplink();
//            if (lista.get(indice).getTipoServico() != null){
//                mensagemConvencao = menDB.retornaDiaString(
//                        jurDB.pesquisaJuridicaPorPessoa(
//                        lista.get(indice).getPessoa().getId()).getId(),
//                        referencia,
//                        lista.get(indice).getTipoServico().getId(),
//                        lista.get(indice).getServicos().getId());
//                if (mensagemConvencao != null) {
//                    //vencimento = DataHoje.converteReferenciaVencimento(referencia,  DataHoje.DataToArray(mensagemConvencao.getDtVencimento())[0] , filDB.pesquisaRegistroPorFilial(1).getTipoEmpresa());
//                    vencimento = DataHoje.converteData(mensagemConvencao.getDtVencimento());
//                }
//            }else{
//                vencimento = lista.get(indice).getVencimento();
//            }
//            return vencimento;
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    public static String geraVencimentoOriginal(Movimento movimento, String referencia) {
//        String vencimento = null;
//        try {
//            MensagemConvencaoDB menDB = new MensagemConvencaoDBToplink();
//            JuridicaDB jurDB = new JuridicaDBToplink();
//            FilialDB filDB = new FilialDBToplink();
//            MensagemConvencao mensagemConvencao = menDB.retornaDiaString(
//                    jurDB.pesquisaJuridicaPorPessoa(
//                    movimento.getPessoa().getId()).getId(),
//                    referencia,
//                    movimento.getTipoServico().getId(),
//                    movimento.getServicos().getId());
//
//            vencimento = DataHoje.converteData(mensagemConvencao.getDtVencimento());
//            return vencimento;
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    public static String estornarMovimentoArrecadacao(List<Integer> lista) {
//        MovimentoDB movimentoDB = new MovimentoDBToplink();
//        LoteDB loteDB = new LoteDBToplink();
//        LoteBaixaDB loteBaixaDB = new LoteBaixaDBToplink();
//        Movimento movDel = null;
//        Movimento movimentoP = null;
//        FinanceiroDB financeiroDB = new FinanceiroDBToplink();
//        ComplementoValor complemento;
//        Lote lote = null;
//        LoteBaixa loteB = null;
//        List<Movimento> listaExtornar = new ArrayList<Movimento>();
//        List<Movimento> listaOriginal = new ArrayList<Movimento>();
//        int re;
//
//        try {
//            if (!lista.isEmpty()) {
//                for (int k = 0; k < lista.size(); k++) {
//                    movimentoP = movimentoDB.pesquisaCodigo(lista.get(k));
//                    if (movimentoP == null) {
//                        continue;
//                    }
//                    listaExtornar = movimentoDB.movimentosBaixadosAtivos(movimentoP.getLoteBaixa().getId());
//                    if (listaExtornar == null) {
//                        continue;
//                    }
//                    financeiroDB.abrirTransacao();
//                    for (int i = 0; i < listaExtornar.size(); i++) {
//                        listaOriginal = financeiroDB.pesquisaMovimentoOriginal(listaExtornar.get(i).getLoteBaixa().getId());
//                        for (Movimento movimentoOriginal : listaOriginal) {
//                            re = movimentoOriginal.getId();
//                            movimentoOriginal.setLoteBaixa(null);
//                            movimentoOriginal.setAtivo(1);
//                            financeiroDB.update(movimentoOriginal);
//                        }
//
//                        complemento = financeiroDB.pesquisaComplementoValor(listaExtornar.get(i).getId());
//                        if (complemento != null) {
//                            financeiroDB.delete(complemento);
//                        }
//                        re = listaExtornar.get(i).getId();
//                        lote = financeiroDB.pesquisaCodigo(listaExtornar.get(i).getLote());
//                        loteB = financeiroDB.pesquisaCodigo(listaExtornar.get(i).getLoteBaixa());
//                        movDel = financeiroDB.pesquisaCodigo(listaExtornar.get(i));
//                        re = movDel.getId();
//                        financeiroDB.delete(movDel);
//                        if (financeiroDB.contarMovimentosPara(lote.getId()) == 0) {
//                            financeiroDB.delete(lote);
//                        }
//                    }
//                    if (loteB != null) {
//                        financeiroDB.delete(loteB);
//                    }
//                    financeiroDB.comitarTransacao();
//                }
//                return "Extornado com sucesso!";
//            } else {
//                return "Não existe movimentos a serem estornados.";
//            }
//        } catch (Exception e) {
//            financeiroDB.desfazerTransacao();
//            return "Não foi possível estornar. Erro: " + e.getMessage();
//        }
//    }
//
//    private List<Integer> agruparLoteBaixa() {
//        List<Integer> listaLoteBaixa = new ArrayList<Integer>();
//        listaLoteBaixa.add(lista.get(0).getLoteBaixa().getId());
//        for (int i = 0; i < lista.size(); i++) {
//            if (!contemInteger(lista.get(i).getLoteBaixa().getId(), listaLoteBaixa)) {
//                listaLoteBaixa.add(lista.get(i).getLoteBaixa().getId());
//            }
//        }
//        return listaLoteBaixa;
//    }
//
//    private static boolean contemInteger(int id, List<Integer> listaLoteBaixa) {
//        int i = 0;
//        while (i < listaLoteBaixa.size()) {
//            if (listaLoteBaixa.get(i) == id) {
//                return true;
//            }
//            i++;
//        }
//        return false;
//    }
//
//    public String excluirAcordo(){
//        MovimentoDB db = new MovimentoDBToplink();
//        String ids = "";
//        SalvarAcumuladoDB dbSalvar = new SalvarAcumuladoDBToplink();
//        if (listaAcordo.isEmpty()) {
//            return null;
//        }
//
//        for (int i = 0; i < listaAcordo.size(); i++){
//            if (ids.length() > 0 && i != listaAcordo.size())
//                ids = ids+",";
//            ids = ids + String.valueOf(listaAcordo.get(i).getId());
//        }
//        if (ids.isEmpty())
//            return null;
//        else{
//            //dbSalvar.abrirTransacao();
//            db.excluirAcordoIn(ids, listaAcordo.get(0).getGrupoAcordo().getId());
//              //  dbSalvar.comitarTransacao();
//            //else
//                //dbSalvar.desfazerTransacao();
//            return null;
//        }
//    }
//
//    public String getMensagemErroMovimento() {
//        return mensagemErroMovimento;
//    }
//
//    public void setMensagemErroMovimento(String mensagemErroMovimento) {
//        this.mensagemErroMovimento = mensagemErroMovimento;
//    }
//
//
//}
