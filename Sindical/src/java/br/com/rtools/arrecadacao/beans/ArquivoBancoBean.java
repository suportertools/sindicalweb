package br.com.rtools.arrecadacao.beans;

import br.com.rtools.cobranca.BancoDoBrasil;
import br.com.rtools.cobranca.CaixaFederalSicob;
import br.com.rtools.cobranca.Cobranca;
import br.com.rtools.financeiro.Boleto;
import br.com.rtools.financeiro.ContaCobranca;
import br.com.rtools.financeiro.MensagemCobranca;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.RemessaBanco;
import br.com.rtools.financeiro.db.FinanceiroDB;
import br.com.rtools.financeiro.db.FinanceiroDBToplink;
import br.com.rtools.financeiro.db.MovimentoDB;
import br.com.rtools.financeiro.db.MovimentoDBToplink;
import br.com.rtools.financeiro.db.RemessaBancoDB;
import br.com.rtools.financeiro.db.RemessaBancoDBToplink;
import br.com.rtools.financeiro.db.ServicoContaCobrancaDB;
import br.com.rtools.financeiro.db.ServicoContaCobrancaDBToplink;
import br.com.rtools.pessoa.DocumentoInvalido;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.db.*;
import br.com.rtools.retornos.*;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.*;
import java.io.*;
import java.util.*;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@ManagedBean
@SessionScoped
public class ArquivoBancoBean implements Serializable{
    private int idContrib = 0;
    private int indexArquivos = -1;
    private File files;
    private FileOutputStream file = null;
    private FileReader reader = null;
    private BufferedReader buffReader = null;
    private FileWriter writer = null;
    private BufferedWriter buffWriter = null;
    private Juridica juridica = new Juridica();
    private Object retornoBanco = new Object();
    //private ServicoContaCobranca servContaCobranca = new ServicoContaCobranca();
    private PessoaEndereco pessoaEndereco = new PessoaEndereco();
    private Movimento movimento = new Movimento();
    private ContaCobranca contaCobranca = new ContaCobranca();
    private String linha = null;
    private String conteudoRemessa = null;
    private String jur = null;
    private String pesEnd = null;
    private String rua = null;
    private String descricaoEndereco = null;
    private String numero = null;
    private String bairro = null;
    private String cidade = null;
    private String cep = null;
    private String codCedente = null;
    private String cnpj = null;
    private String sicas = null;
    private String dtVencimento = null;
    private String dtLiquidacao = null;
    private String nossoNum = null;
    private String vlrLiquido = null;
    private String vlrTaxa = null;
    private String vlrPago = null;
    private String msgOk = "";
    private String lblPendente = "";
    private List lista = new Vector();
    private List listaJuridica = new Vector();
    private List listaMovimentos = new Vector();
    private List listaArquivoRemessa = new Vector<GenericaQuery>();
    private List listaArquivoRetorno = new Vector<GenericaQuery>();
    private List listaPendencias = new Vector<GenericaQuery>();
    private List listaRetorno = new Vector<GenericaQuery>();
    private List<DataObject> listaDocumentos = new Vector<DataObject>();
    private List listaPasta = new ArrayList();
    private boolean outros = false;
    private boolean carregaPastas = false;
    private String strServicos = "";

    public String getStrServicos() {
        if (!listaArquivoRetorno.isEmpty() && strServicos.isEmpty() && contaCobranca.getId() != -1) {
            // LAYOUT 2 = SINDICAL
            if (contaCobranca.getLayout().getId() == 2) {
                strServicos = contaCobranca.getApelido() + " - " + contaCobranca.getSicasSindical();
            } else {
                strServicos = contaCobranca.getApelido() + " - " + contaCobranca.getCodCedente();
            }
        } else {
            strServicos = "";
        }
        return strServicos;
    }

    public String novoServico() {
        return null;
    }

    public List<SelectItem> getListaServicoCobranca() {
        List<SelectItem> servicoCobranca = new ArrayList<SelectItem>();
        int i = 0;
        ServicoContaCobrancaDB servDB = new ServicoContaCobrancaDBToplink();
        List<ContaCobranca> select = servDB.listaContaCobrancaAtivo();
        if (select == null) {
            select = new ArrayList<ContaCobranca>();
        }
        while (i < select.size()) {
            // LAYOUT 2 = SINDICAL
            if (select.get(i).getLayout().getId() == 2) {
                servicoCobranca.add(
                        new SelectItem(
                        new Integer(i),
                        select.get(i).getApelido() + " - "
                        + select.get(i).getSicasSindical(),//SICAS NO CASO DE SINDICAL
                        Integer.toString(select.get(i).getId())));
            } else {
                servicoCobranca.add(
                        new SelectItem(
                        new Integer(i),
                        select.get(i).getApelido() + " - "
                        + select.get(i).getCodCedente(),//CODCEDENTE NO CASO DE OUTRAS
                        Integer.toString(select.get(i).getId())));
            }
            i++;
        }
        if (!servicoCobranca.isEmpty()) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            contaCobranca = (ContaCobranca) sv.pesquisaCodigo(Integer.parseInt(((SelectItem) servicoCobranca.get(idContrib)).getDescription()), "ContaCobranca");
        }
        return servicoCobranca;
    }

    public String criarDataArquivo() {
        MovimentoDB dbMov = new MovimentoDBToplink();
        List movi = null;
        movi = dbMov.pesquisaTodos();
        try {
            criarArquivoTXT(movi);
        } catch (Exception e) {
            System.out.println("Nao foi possivel criar arquivo de envio!");
        }
        return "arquivoBanco";
    }

    public boolean criarArquivoTXT(List<Movimento> movs) {
        PessoaEnderecoDB dbPes = new PessoaEnderecoDBToplink();
        FinanceiroDB dbfin = new FinanceiroDBToplink();
        MovimentoDB dbmov = new MovimentoDBToplink();
        RemessaBancoDB rbd = new RemessaBancoDBToplink();
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        Boleto boleto = new Boleto();
        try {
            String patch = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos");
            File fileA = new File(patch + "/downloads");
            if (!fileA.exists()) {
                fileA.mkdir();
            }
            File fileB = new File(patch + "/downloads/remessa");
            if (!fileB.exists()) {
                fileB.mkdir();
            }
            RemessaBanco rb = new RemessaBanco();
            FacesContext context = FacesContext.getCurrentInstance();
            String caminho = ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/remessa/");
            String destino = caminho + "/" + DataHoje.ArrayDataHoje()[2] + "-" + DataHoje.ArrayDataHoje()[1] + "-" + DataHoje.ArrayDataHoje()[0];
            File flDes = new File(destino); // 0 DIA, 1 MES, 2 ANO
            flDes.mkdir();

            destino += "/ARQX" + DataHoje.hora().replace(":", "") + ".REM";


            file = new FileOutputStream(destino);
            file.close();
            writer = new FileWriter(destino);
            buffWriter = new BufferedWriter(writer);
            conteudoRemessa = "";
            if (movs.isEmpty()) {
                return false;
            }

            boleto = dbmov.pesquisaBoletos(movs.get(0).getNrCtrBoleto());
            String agencia = boleto.getContaCobranca().getContaBanco().getAgencia() + calculaDigitoAgencia(movs.get(0), boleto);
            String cedente = "";
            cedente = ("000000").substring(0, 6 - boleto.getContaCobranca().getCodigoSindical().length()) + boleto.getContaCobranca().getCodigoSindical();

            String nome_sindicato = AnaliseString.removerAcentos((boleto.getContaCobranca().getCedente() + "                    ").substring(0, 30).toUpperCase()),
                    nome_banco = AnaliseString.removerAcentos(boleto.getContaCobranca().getContaBanco().getBanco().getBanco().substring(0, 30).toUpperCase());
            String dia = (DataHoje.data()).substring(0, 2),
                    mes = (DataHoje.data()).substring(3, 5),
                    ano = (DataHoje.data()).substring(6, 10);
            String data = dia + mes + ano;
            String doc_juridica = "";

            int sequencia = 1;

            //HEADER DE ARQUIVO --
            conteudoRemessa = "10400000         "; //NUM BANCO

            // Tipo de Inscrição da Empresa = 1 dig
            // Número de Inscrição da Empresa = 14 dig
            String docs = limpaDocumento(movs.get(0).getLote().getFilial().getFilial().getPessoa().getDocumento());
            conteudoRemessa += "2" + docs;

            // Uso Exclusivo CAIXA 
            conteudoRemessa += "00000000000000000000"; //NUM BANCO

            conteudoRemessa += agencia = ("000000").substring(0, 6 - agencia.length()) + agencia;

            conteudoRemessa += cedente;

            // Uso Exclusivo CAIXA
            conteudoRemessa += "00000000";

            // Nome Sindicato

            conteudoRemessa += nome_sindicato;
            // Nome do Banco
            conteudoRemessa += nome_banco;

            conteudoRemessa += "          ";

            conteudoRemessa += "1" + data;

            String hora = DataHoje.hora();
            //hora = hora.substring(0, 2)+hora.substring(3, 5)+hora.substring(6, 8);
            conteudoRemessa += hora.replace(":", "");

            // Sequencial do arquivo
            conteudoRemessa += "000001";
            conteudoRemessa += "05000000";

            conteudoRemessa += "                    ";
            conteudoRemessa += "REMESSA-PRODUCAO    ";
            conteudoRemessa += "V215";
            conteudoRemessa += "                         ";

            buffWriter.write(conteudoRemessa);
            buffWriter.newLine();

            conteudoRemessa = "104";

            // Lote Serviço
            conteudoRemessa += "0001";

            conteudoRemessa += "1R0200030 ";
            conteudoRemessa += "20" + docs + cedente + "00000000000000";
            conteudoRemessa += agencia + cedente + "00000000";

            conteudoRemessa += nome_sindicato;

            // 40-40
            conteudoRemessa += "                                       ";
            conteudoRemessa += "                                        ";

            conteudoRemessa += " 00000001";
            conteudoRemessa += data + data;

            conteudoRemessa += "                                 ";

            buffWriter.write(conteudoRemessa);
            buffWriter.newLine();

            // Lista Movimento

            sv.abrirTransacao();
            for (int i = 0; i < movs.size(); i++) {
                // FIM DE HEADER DE ARQUIVO
                // SEGUIMENTO P ----------------
                conteudoRemessa = "10400013";

                // Sequencia
                conteudoRemessa += ("00000").substring(0, 5 - Integer.toString(sequencia).length()) + Integer.toString(sequencia);
                sequencia++;

                conteudoRemessa += "P ";

                conteudoRemessa += "01" + agencia + cedente;

                conteudoRemessa += "00000000000";

                /**
                 * NOSSO NUMERO COMPLETO
                 */
                // Modalidade Carteira
                conteudoRemessa += "21";
                // Nosso Numero boleto
                if (movs.get(i).getDocumento().length() > 15) {
                    conteudoRemessa += movs.get(i).getDocumento().substring(movs.get(i).getDocumento().length() - 15, movs.get(i).getDocumento().length());
                } else {
                    conteudoRemessa += ("000000000000000").substring(0, 15 - movs.get(i).getDocumento().length()) + movs.get(i).getDocumento();
                }

                /**
                 * FIM
                 */
                conteudoRemessa += "12211";

                // Número Titulo
                conteudoRemessa += "123            ";

                // Data Vencimento
                conteudoRemessa += movs.get(i).getVencimento().replace("/", "");

                // Valor Nominal do Titulo                    
                String valor = Moeda.converteR$Float(movs.get(i).getValor()).replace(".", "");
                valor = valor.replace(",", "");
                conteudoRemessa += ("000000000000000").substring(0, 15 - valor.length()) + valor;

                conteudoRemessa += "000000";

                conteudoRemessa += "04N";

                // Data Emissão
                conteudoRemessa += data;

                // Codigo Juros Mora
                conteudoRemessa += "1";
                // Data do Juros Mora
                conteudoRemessa += "00000000";
                // Valor Juros Mora
                String juros = "0";
                juros = Moeda.converteR$Float(movs.get(i).getJuros()).replace(".", "");
                juros = juros.replace(",", "");
                conteudoRemessa += ("000000000000000").substring(0, 15 - juros.length()) + juros;

                // Codigo Desconto
                conteudoRemessa += "0";
                // Data Desconto
                conteudoRemessa += "00000000";
                String desconto = "0";
                desconto = Moeda.converteR$Float(movs.get(i).getDesconto()).replace(".", "");
                desconto = desconto.replace(",", "");
                conteudoRemessa += ("000000000000000").substring(0, 15 - desconto.length()) + desconto;

                // IOF
                conteudoRemessa += "000000000000000";

                // Abatimento
                conteudoRemessa += "000000000000000";

                conteudoRemessa += "123                      ";

                conteudoRemessa += "3001005090000000000 ";

                buffWriter.write(conteudoRemessa);
                buffWriter.newLine();

                // SEGUIMENTO Q ---------------------
                conteudoRemessa = "10400013";

                // Sequencia
                conteudoRemessa += ("00000").substring(0, 5 - Integer.toString(sequencia).length()) + Integer.toString(sequencia);
                sequencia++;
                conteudoRemessa += "Q ";

                doc_juridica = limpaDocumento(movs.get(i).getPessoa().getDocumento());
                doc_juridica = ("              ").substring(0, 14 - doc_juridica.length()) + doc_juridica;
                conteudoRemessa += "0120" + doc_juridica;

                conteudoRemessa += (AnaliseString.removerAcentos(movs.get(i).getPessoa().getNome()).toUpperCase() + "                                        ").substring(0, 40);

                pessoaEndereco = dbPes.pesquisaEndPorPessoaTipo(movs.get(i).getPessoa().getId(), 3);

                if (pessoaEndereco != null) {
                    descricaoEndereco = pessoaEndereco.getEndereco().getDescricaoEndereco().getDescricao();
                    rua = pessoaEndereco.getEndereco().getLogradouro().getDescricao();
                    numero = pessoaEndereco.getNumero();
                    bairro = pessoaEndereco.getEndereco().getBairro().getDescricao();
                    cidade = pessoaEndereco.getEndereco().getCidade().getCidade();
                    cep = pessoaEndereco.getEndereco().getCep();

                    conteudoRemessa += (AnaliseString.removerAcentos(descricaoEndereco.toUpperCase()) + "                                        ").substring(0, 40);
                    conteudoRemessa += (AnaliseString.removerAcentos(bairro.toUpperCase()) + "               ").substring(0, 15);
                    cep = cep.replace("-", "");
                    cep = cep.replace(".", "");
                    conteudoRemessa += (AnaliseString.removerAcentos(cep) + "        ").substring(0, 8);
                    conteudoRemessa += (AnaliseString.removerAcentos(cidade.toUpperCase()) + "               ").substring(0, 15) + pessoaEndereco.getEndereco().getCidade().getUf();
                } else {
                    pesEnd = "                                                                                ";
                    conteudoRemessa += pesEnd;
                }

                conteudoRemessa += "0000000000000000";
                conteudoRemessa += "                                                                       ";

                buffWriter.write(conteudoRemessa);
                buffWriter.newLine();

                // SEGUIMENTO R ---------------------
                conteudoRemessa = "10400013";

                // Sequencia
                conteudoRemessa += ("00000").substring(0, 5 - Integer.toString(sequencia).length()) + Integer.toString(sequencia);
                sequencia++;
                conteudoRemessa += "R ";

                conteudoRemessa += "01";

                conteudoRemessa += "000000000000000000000000000000000000000000000000";

                // Codigo Multa
                conteudoRemessa += "1";
                // Data da Multa
                conteudoRemessa += "00000000";
                String multa = "0";
                multa = Moeda.converteR$Float(movs.get(i).getMulta()).replace(".", "");
                multa = multa.replace(",", "");
                conteudoRemessa += ("000000000000000").substring(0, 15 - multa.length()) + multa;

                conteudoRemessa += "          ";
                MensagemCobranca mensa = dbmov.pesquisaMensagemCobranca(movs.get(i).getId());
                if (mensa != null) {
                    // 1 - Mensagem Boleto
                    conteudoRemessa += (AnaliseString.removerAcentos(mensa.getMensagemConvencao().getMensagemContribuinte().toUpperCase()) + "                                        ").substring(0, 40);

                    // 2 - Mensagem Boleto
                    conteudoRemessa += (AnaliseString.removerAcentos(mensa.getMensagemConvencao().getMensagemCompensacao().toUpperCase()) + "                                        ").substring(0, 40);
                } else {
                    conteudoRemessa += "                                                                                ";
                }

                conteudoRemessa += "                                                             ";
                buffWriter.write(conteudoRemessa);
                buffWriter.newLine();
                boleto = dbmov.pesquisaBoletos(movs.get(i).getNrCtrBoleto());
                Object obj = rbd.pesquisaRemessaBancoCobranca(boleto.getContaCobranca().getId());
                rb = new RemessaBanco();
                if (obj != null) {
                    rb.setLote(Integer.parseInt(String.valueOf(obj)));
                } else {
                    rb.setLote(1);
                }

                rb.setMovimento(movs.get(i));

                sv.inserirObjeto(rb);

            }
            /* TRAILER DE LOTE --------------------- */
            // Rodape do arquivo pg 28
            conteudoRemessa = "10400015         ";

            String qntLinhas = ("000000").substring(0, 6 - Integer.toString(((movs.size() * 3) + 2)).length()) + ((movs.size() * 3) + 2);
            // Quantidade de registros do lote
            conteudoRemessa += qntLinhas;

            // Totalização da Cobrança simples
            String qntBoletos = ("000000").substring(0, 6 - Integer.toString(movs.size()).length()) + movs.size();
            //conteudoRemessa += "000001";
            conteudoRemessa += qntBoletos;

            // Valor do Titulo em Carteira
            conteudoRemessa += ("000000000000000").substring(0, 15 - "0".length()) + "0";
            conteudoRemessa += "0000000000000000000000000000000000000000000000                                                                                                                                                      ";

            buffWriter.write(conteudoRemessa);
            buffWriter.newLine();
            /* ------------------------------------ */

            /* TRAILER DE ARQUIVO --------------------- */
            //qntBoletos += Integer.toString(Integer.parseInt(qntBoletos) * 3) ;

            qntLinhas = qntLinhas.substring(0, 6 - Integer.toString(((movs.size() * 3) + 4)).length()) + ((movs.size() * 3) + 4);
            String qntLote = ("000000").substring(0, 6 - Integer.toString(rb.getLote()).length()) + rb.getLote();
            conteudoRemessa = "10499999         " + qntLote + qntLinhas + "                                                                                                                                                                                                                   ";
            buffWriter.write(conteudoRemessa);
            /* -------------------------------------- */
            sv.comitarTransacao();
            buffWriter.flush();
            buffWriter.close();

            return true;
        } catch (Exception e) {
            sv.desfazerTransacao();
            System.out.println(e);
            return false;
        }
    }

    public boolean baixarArquivosGerados() {
        try {

            String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/remessa/");
            caminho += "/" + DataHoje.ArrayDataHoje()[2] + "-" + DataHoje.ArrayDataHoje()[1] + "-" + DataHoje.ArrayDataHoje()[0];

            Zip zip = new Zip();

            File fl = new File(caminho);
            File listFile[] = fl.listFiles();

            if (listFile.length == 0) {
                return false;
            }

            File arqzip = new File(caminho + "/arqzip.zip");
            zip.zip(listFile, arqzip);

            Download download = new Download(
                    "arqzip.zip",
                    arqzip.getParent(),
                    "zip",
                    FacesContext.getCurrentInstance());
            download.baixar();

            arqzip.delete();
            return true;
        } catch (Exception e) {
            System.err.println("O arquivo não foi gerado corretamente! Erro: " + e.getMessage());
            return false;
        }

    }

    public boolean limparDiretorio(String caminho) {
        try {
            if (caminho.isEmpty()) {
                caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/remessa/");
                caminho += "/" + DataHoje.ArrayDataHoje()[2] + "-" + DataHoje.ArrayDataHoje()[1] + "-" + DataHoje.ArrayDataHoje()[0];
            }
            File fl = new File(caminho);
            File listFile[] = fl.listFiles();
            for (int i = 0; i < listFile.length; i++) {
                listFile[i].delete();
            }
            return true;
        } catch (Exception e) {
            System.err.println("O arquivo não pode ser deletado! Erro: " + e.getMessage());
            return false;
        }
    }

    public String calculaDigitoAgencia(Movimento mov, Boleto bol) {
        Cobranca cob = null;
        switch (bol.getContaCobranca().getContaBanco().getBanco().getId()) {
            case ArquivoRetorno.BANCO_BRASIL:
                cob = new BancoDoBrasil(mov, bol);
                return cob.moduloOnze(bol.getContaCobranca().getContaBanco().getAgencia());
            case ArquivoRetorno.CAIXA_FEDERAL:
                cob = new CaixaFederalSicob(mov, bol);
                return cob.moduloOnze(bol.getContaCobranca().getContaBanco().getAgencia());
            case ArquivoRetorno.ITAU:
                cob = new br.com.rtools.cobranca.Itau(mov, bol);
                return cob.moduloOnze(bol.getContaCobranca().getContaBanco().getAgencia());
            case ArquivoRetorno.REAL:
                cob = new br.com.rtools.cobranca.Real(mov, bol);
                return cob.moduloOnze(bol.getContaCobranca().getContaBanco().getAgencia());
        }
        return "";
    }

    public String limpaDocumento(String doc) {
        doc = doc.replace(".", "");
        doc = doc.replace("/", "");
        doc = doc.replace("-", "");
        return doc;
    }

    public String lerArquivoTXTRetorno() {
        GenericaQuery generica = new GenericaQuery();
        Object objs[] = null;

        String caminho = (String) objs[0];
        File fl = new File(caminho);
        File listFile[] = fl.listFiles();
        if (listFile != null) {
            int qntRetornos = listFile.length;
            listaRetorno = new Vector<GenericaQuery>();
            for (int u = 0; u < qntRetornos; u++) {
                try {
                    reader = new FileReader(caminho + "/" + listFile[u].getName());
                    buffReader = new BufferedReader(reader);
                    lista = new Vector();
                    while ((linha = buffReader.readLine()) != null) {
                        lista.add(linha);
                    }
                    int i = 0;
                    while (i < lista.size()) {
                        if (i < 1) {
                            cnpj = ((String) lista.get(i)).substring(18, 32);
                            sicas = ((String) lista.get(i)).substring(33, 39);
                        }
                        if (((String) lista.get(i)).substring(13, 14).equals("T")) {
                            nossoNum = ((String) lista.get(i)).substring(133, 148);
                            dtVencimento = ((String) lista.get(i)).substring(73, 81);
                            vlrTaxa = ((String) lista.get(i)).substring(198, 213);
                        }

                        i++;
                        if (i < lista.size() && ((String) lista.get(i)).substring(13, 14).equals("U")) {
                            vlrPago = ((String) lista.get(i)).substring(77, 92);
                            vlrLiquido = ((String) lista.get(i)).substring(95, 107);
                            dtLiquidacao = ((String) lista.get(i)).substring(145, 153);
                            generica = new GenericaQuery(cnpj, //argumento0
                                    sicas, //argumento1
                                    nossoNum, //argumento2
                                    vlrLiquido, //argumento3
                                    vlrTaxa, //argumento4
                                    dtLiquidacao);//argumento5
                            listaRetorno.add(generica);
                            i++;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Erro ao ler arquivo!");
                    continue;
                }
            }
        }
        return null;
    }

    public String getRetorno() {
        String retCompleto = "";
        String asd = "";
        if (!listaRetorno.isEmpty()) {
            int op = listaRetorno.size();
            for (int i = 0; i < listaRetorno.size(); i++) {
                //retCompleto = (String) ((GenericaQuery) listaRetorno.get(i)).getArgumento0()+" "+ //cnpj
                //              (String) ((GenericaQuery) listaRetorno.get(i)).getArgumento1()+" "+ //sicas
                //              (String) ((GenericaQuery) listaRetorno.get(i)).getArgumento2()+" "+ //nossoNum
                //              (String) ((GenericaQuery) listaRetorno.get(i)).getArgumento3()+" "+ //vlrLiquido
                //              (String) ((GenericaQuery) listaRetorno.get(i)).getArgumento4()+" "+ //vlrTaxa
                //              (String) ((GenericaQuery) listaRetorno.get(i)).getArgumento5()+" "; //dtLiquidacao
                retCompleto = (String) ((GenericaQuery) listaRetorno.get(i)).getArgumento2() + " "; //nossoNum
                asd = asd + retCompleto + " *** " + i + " ***";
            }
        }
        listaRetorno = new ArrayList();
        return asd;
    }

    public List getListaArquivoRemessa() {
        GenericaQuery generica = new GenericaQuery();
        FacesContext context = FacesContext.getCurrentInstance();
        files = new File(((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/remessas/"));
        File listFile[] = files.listFiles();
        String[] parametros = new String[6];
        listaArquivoRemessa.clear();
        int numArq = listFile.length;
        int i = 0;
        while (i < numArq) {
            parametros[0] = "Baixar";
            parametros[1] = listFile[i].getName();
            parametros[2] = "";
            parametros[3] = "";
            parametros[4] = "";
            parametros[5] = "";
            generica = new GenericaQuery(
                    parametros[0],
                    parametros[1],
                    parametros[2],
                    parametros[3],
                    parametros[4],
                    parametros[5]);
            listaArquivoRemessa.add(generica);
            i++;
        }
        return listaArquivoRemessa;
    }

    public List getListaArquivoRetorno() {
        GenericaQuery generica = new GenericaQuery();
        Object objs[] = caminhoPendente();
        String caminho = (String) objs[0];
        try {
            if (!outros) {
                files = new File(caminho);
                File listFile[] = files.listFiles();
                String[] parametros = new String[6];
                listaArquivoRetorno.clear();
                int numArq = listFile.length;
                int i = 0;
                while (i < numArq) {
                    parametros[0] = listFile[i].getName();
                    parametros[1] = "";
                    parametros[2] = "";
                    parametros[3] = "";
                    parametros[4] = "";
                    parametros[5] = "";
                    generica = new GenericaQuery(
                            parametros[0],
                            parametros[1],
                            parametros[2],
                            parametros[3],
                            parametros[4],
                            parametros[5]);
                    listaArquivoRetorno.add(generica);
                    i++;
                }
            } else {
                files = new File(caminho);
                File listFile[] = files.listFiles();
                String[] parametros = new String[6];
                listaArquivoRetorno.clear();
                int numArq = listFile.length;
                int i = 0;
                while (i < numArq) {
                    parametros[0] = listFile[i].getName();
                    parametros[1] = "";
                    parametros[2] = "";
                    parametros[3] = "";
                    parametros[4] = "";
                    parametros[5] = "";
                    generica = new GenericaQuery(
                            parametros[0],
                            parametros[1],
                            parametros[2],
                            parametros[3],
                            parametros[4],
                            parametros[5]);
                    listaArquivoRetorno.add(generica);
                    i++;
                }
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return listaArquivoRetorno;
    }

    public List getListaPendencias() {
        GenericaQuery generica = new GenericaQuery();
        Object objs[] = caminhoServicoPendente();
        String caminho = (String) objs[0];
        try {
            if (!outros) {
                files = new File(caminho);
                File listFile[] = files.listFiles();
                String[] parametros = new String[6];
                listaPendencias.clear();
                int numArq = listFile.length;
                int i = 0;
                while (i < numArq) {
                    parametros[0] = listFile[i].getName();
                    parametros[1] = "";
                    parametros[2] = "";
                    parametros[3] = "";
                    parametros[4] = "";
                    parametros[5] = "";
                    generica = new GenericaQuery(
                            parametros[0],
                            parametros[1],
                            parametros[2],
                            parametros[3],
                            parametros[4],
                            parametros[5]);
                    listaPendencias.add(generica);
                    i++;
                }
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return listaPendencias;
    }

    public List getListaPastas() {
        if (carregaPastas) {
            listaPasta = new ArrayList();
            DataObject dt = null;
            Object objs[] = caminhoServicoPendente();
            String caminho = (String) objs[0];
            //ServicoContaCobranca scc = (ServicoContaCobranca)objs[1];
            Object[] obj = new Object[2];
            try {
                files = new File(caminho);
                File listFile[] = files.listFiles();
                int numArq = listFile.length;
                for (int i = 0; i < numArq; i++) {
                    try {
                        obj = converteDataPasta(listFile[i].getName());
                    } catch (Exception e) {
                    }
                    if ((Boolean) obj[1]) {
                        dt = new DataObject(false,
                                listFile[i].getName(),
                                obj[0],
                                "",
                                "",
                                "");
                        listaPasta.add(dt);
                    }
                }
            } catch (Exception e) {
                return new ArrayList();
            }
            carregaPastas = false;
        }
        return listaPasta;
    }

    public Object[] converteDataPasta(String pasta) {
        Object[] obj = new Object[2];
        try {
            if (pasta.equals("pendentes")) {
                obj[0] = "pendentes";
                obj[1] = true;
                return obj;
            }

            pasta = pasta.substring(8, 10) + "/" + pasta.substring(5, 7) + "/" + pasta.substring(0, 4);
            obj[0] = (Date) DataHoje.converte(pasta);
            if (obj[0] == null) {
                obj[1] = false;
            } else {
                obj[0] = DataHoje.converteData((Date) obj[0]);
                obj[1] = true;
            }
        } catch (Exception e) {
            obj[0] = null;
            obj[1] = false;
        }
        return obj;
    }

    public String relatorioComparacao() {
        if (!listaPasta.isEmpty()) {
            Object objs[] = caminhoServicoPendente();
            ContaCobranca scc = (ContaCobranca) objs[1];
            ArquivoRetorno ar = null;
            List<GenericaRetorno> genericaRetorno = new ArrayList();
            MovimentoDB db = new MovimentoDBToplink();

            // CAIXA FEDERAL ------------------------------------------------------------------------------
            if (ArquivoRetorno.CAIXA_FEDERAL == scc.getContaBanco().getBanco().getId()) {
                if (ArquivoRetorno.SICOB == scc.getLayout().getId()) {
                    ar = new CaixaFederal(scc, true);
                    for (int i = 0; i < listaPasta.size(); i++) {
                        if ((Boolean) ((DataObject) listaPasta.get(i)).getArgumento0()) {
                            genericaRetorno.addAll(ar.sicob(false, ((String) ((DataObject) listaPasta.get(i)).getArgumento1())));
                        }
                    }
                } else if (ArquivoRetorno.SINDICAL == scc.getLayout().getId()) {
                    ar = new CaixaFederal(scc, true);
                    for (int i = 0; i < listaPasta.size(); i++) {
                        if ((Boolean) ((DataObject) listaPasta.get(i)).getArgumento0()) {
                            genericaRetorno.addAll(ar.sindical(false, ((String) ((DataObject) listaPasta.get(i)).getArgumento1())));
                        }
                    }
                } else if (ArquivoRetorno.SIGCB == scc.getLayout().getId()) {
                    ar = new CaixaFederal(scc, true);
                    for (int i = 0; i < listaPasta.size(); i++) {
                        if ((Boolean) ((DataObject) listaPasta.get(i)).getArgumento0()) {
                            genericaRetorno.addAll(ar.sigCB(false, ((String) ((DataObject) listaPasta.get(i)).getArgumento1())));
                        }
                    }
                }
                // BANCO DO BRASIL ------------------------------------------------------------------------------
            } else if (ArquivoRetorno.BANCO_BRASIL == scc.getContaBanco().getBanco().getId()) {
                if (ArquivoRetorno.SICOB == scc.getLayout().getId()) {
                    ar = new BancoBrasil(scc, true);
                    for (int i = 0; i < listaPasta.size(); i++) {
                        if ((Boolean) ((DataObject) listaPasta.get(i)).getArgumento0()) {
                            genericaRetorno.addAll(ar.sicob(false, ((String) ((DataObject) listaPasta.get(i)).getArgumento1())));
                        }
                    }
                } else if (ArquivoRetorno.SINDICAL == scc.getLayout().getId()) {
                } else if (ArquivoRetorno.SIGCB == scc.getLayout().getId()) {
                }
                // REAL ------------------------------------------------------------------------------
            } else if (ArquivoRetorno.REAL == scc.getContaBanco().getBanco().getId()) {
                if (ArquivoRetorno.SICOB == scc.getLayout().getId()) {
                    ar = new Real(scc, true);
                    for (int i = 0; i < listaPasta.size(); i++) {
                        if ((Boolean) ((DataObject) listaPasta.get(i)).getArgumento0()) {
                            genericaRetorno.addAll(ar.sicob(false, ((String) ((DataObject) listaPasta.get(i)).getArgumento1())));
                        }
                    }
                } else if (ArquivoRetorno.SINDICAL == scc.getLayout().getId()) {
                } else if (ArquivoRetorno.SIGCB == scc.getLayout().getId()) {
                }
                // ITAU ------------------------------------------------------------------------------
            } else if (ArquivoRetorno.ITAU == scc.getContaBanco().getBanco().getId()) {
                if (ArquivoRetorno.SICOB == scc.getLayout().getId()) {
                    ar = new Itau(scc, true);
                    for (int i = 0; i < listaPasta.size(); i++) {
                        if ((Boolean) ((DataObject) listaPasta.get(i)).getArgumento0()) {
                            genericaRetorno.addAll(ar.sicob(false, ((String) ((DataObject) listaPasta.get(i)).getArgumento1())));
                        }
                    }
                } else if (ArquivoRetorno.SINDICAL == scc.getLayout().getId()) {
                } else if (ArquivoRetorno.SIGCB == scc.getLayout().getId()) {
                }

            }
            Collection listaComparas = new ArrayList<ComparaMovimentos>();
            ComparaMovimentos compara = null;
            List<Movimento> movs = null;
            String arqNumero = "";
            String movNumero = "";
            String vencimento = "";
            Boleto boleto = new Boleto();

            if (!genericaRetorno.isEmpty()) {
                for (int i = 0; i < genericaRetorno.size(); i++) {
                    movs = db.pesquisaMovPorNumDocumentoListBaixado(genericaRetorno.get(i).getNossoNumero(), scc.getId());
                    if (!movs.isEmpty()) {
                        boleto = db.pesquisaBoletos(movs.get(0).getNrCtrBoleto());
                        arqNumero = genericaRetorno.get(i).getNossoNumero();
                        movNumero = boleto.getBoletoComposto();
                        vencimento = genericaRetorno.get(i).getDataVencimento();
                        if (vencimento.isEmpty()) {
                            vencimento = "00000000";
                        }
                        compara = new ComparaMovimentos(arqNumero.substring(arqNumero.length() - movNumero.length(), arqNumero.length()),
                                Moeda.converteUS$(String.valueOf(Moeda.divisaoValores(Integer.parseInt(genericaRetorno.get(i).getValorPago()), 100))),
                                DataHoje.colocarBarras(genericaRetorno.get(i).getDataPagamento()),
                                DataHoje.colocarBarras(vencimento),
                                movNumero,
                                movs.get(0).getValor(),
                                movs.get(0).getBaixa().getImportacao(),
                                movs.get(0).getVencimento(),
                                movs.get(0).getPessoa().getDocumento(),
                                movs.get(0).getPessoa().getNome(),
                                movs.get(0).getServicos().getDescricao(),
                                movs.get(0).getTipoServico().getDescricao(),
                                genericaRetorno.get(i).getNomePasta(),
                                genericaRetorno.get(i).getNomeArquivo());
                    } else {
                        movs = db.pesquisaMovPorNumDocumentoList(genericaRetorno.get(i).getNossoNumero(), DataHoje.converte(DataHoje.colocarBarras(genericaRetorno.get(i).getDataVencimento())), scc.getId());
                        if (!movs.isEmpty()) {
                            boleto = db.pesquisaBoletos(movs.get(0).getNrCtrBoleto());
                            arqNumero = genericaRetorno.get(i).getNossoNumero();
                            movNumero = boleto.getBoletoComposto();
                            vencimento = genericaRetorno.get(i).getDataVencimento();
                            if (vencimento.isEmpty()) {
                                vencimento = "00000000";
                            }
                            compara = new ComparaMovimentos(arqNumero.substring(arqNumero.length() - movNumero.length(), arqNumero.length()),
                                    Moeda.converteUS$(String.valueOf(Moeda.divisaoValores(Integer.parseInt(genericaRetorno.get(i).getValorPago()), 100))),
                                    DataHoje.colocarBarras(genericaRetorno.get(i).getDataPagamento()),
                                    DataHoje.colocarBarras(vencimento),
                                    boleto.getBoletoComposto(),
                                    movs.get(0).getValor(),
                                    "",
                                    movs.get(0).getVencimento(),
                                    movs.get(0).getPessoa().getDocumento(),
                                    movs.get(0).getPessoa().getNome(),
                                    movs.get(0).getServicos().getDescricao(),
                                    movs.get(0).getTipoServico().getDescricao(),
                                    genericaRetorno.get(i).getNomePasta(),
                                    genericaRetorno.get(i).getNomeArquivo());

                        } else {
                            vencimento = genericaRetorno.get(i).getDataVencimento();
                            if (vencimento.isEmpty()) {
                                vencimento = "00000000";
                            }
                            compara = new ComparaMovimentos(genericaRetorno.get(i).getNossoNumero(),
                                    Moeda.converteUS$(String.valueOf(Moeda.divisaoValores(Integer.parseInt(genericaRetorno.get(i).getValorPago()), 100))),
                                    DataHoje.colocarBarras(genericaRetorno.get(i).getDataPagamento()),
                                    DataHoje.colocarBarras(vencimento),
                                    "",
                                    0,
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    genericaRetorno.get(i).getNomePasta(),
                                    genericaRetorno.get(i).getNomeArquivo());
                        }
                    }
                    listaComparas.add(compara);
                    movs = null;
                }
                try {
                    FacesContext faces = FacesContext.getCurrentInstance();
                    HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
                    byte[] arquivo = new byte[0];
                    JasperReport jasper = null;
                    Collection listaComp = new ArrayList<ComparaMovimentos>();

                    jasper = (JasperReport) JRLoader.loadObject(
                            ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/COMPARATIVO_MOVIMENTO.jasper"));

                    JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(listaComparas);
                    JasperPrint print = JasperFillManager.fillReport(
                            jasper,
                            null,
                            dtSource);
                    arquivo = JasperExportManager.exportReportToPdf(print);
                    response.setContentType("application/pdf");
                    response.setContentLength(arquivo.length);
                    ServletOutputStream saida = response.getOutputStream();
                    saida.write(arquivo, 0, arquivo.length);
                    saida.flush();
                    saida.close();

                    FacesContext.getCurrentInstance().responseComplete();
                    Download download = new Download(
                            "Movimentos Baixados.pdf",
                            ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/retornoBanco.jsf"),
                            "pdf",
                            FacesContext.getCurrentInstance());
                    download.baixar();
                } catch (Exception e) {
                    System.err.println("O arquivo não foi gerado corretamente! Erro: " + e.getMessage());
                }

            }
        }
        return null;
    }

    public void refreshPastas() {
        carregaPastas = true;
    }

    public List<DataObject> getListaDocsAberto() {
        DataObject dtObject = null;
        String documento = "";
        String digito = "";
        boolean encontrado = false;
        DocumentoInvalidoDB dbDocInv = new DocumentoInvalidoDBToplink();
        List<DocumentoInvalido> listaDoc = new ArrayList<DocumentoInvalido>();
        List<DocumentoInvalido> listaDocCadastrado = new ArrayList<DocumentoInvalido>();
        listaDocumentos = new Vector<DataObject>();
        listaDoc = dbDocInv.pesquisaTodos();
        
        if (listaDoc == null)
            return new ArrayList();
        listaDocCadastrado = dbDocInv.pesquisaNumeroBoletoPessoa();
        for (int i = 0; i < listaDoc.size(); i++) {
            encontrado = false;
            for (int w = 0; w < listaDocCadastrado.size(); w++) {
                if (listaDoc.get(i).getId() == listaDocCadastrado.get(w).getId()) {
                    documento = listaDoc.get(i).getDocumentoInvalido().substring(
                            listaDoc.get(i).getDocumentoInvalido().length() - 12,
                            listaDoc.get(i).getDocumentoInvalido().length());
                    digito = ValidaDocumentos.retonarDigitoCNPJ(documento);
                    dtObject = new DataObject(false,
                            AnaliseString.mascaraCnpj(documento + digito),// -- DOCUMENTO
                            "** CADASTRADO **",// -- STATUS
                            listaDoc.get(i),
                            false,
                            listaDoc.get(i).getDtImportacao());
                    
                    listaDocumentos.add(dtObject);
                    
                    encontrado = true;
                }
            }
            if (!encontrado) {
                documento = listaDoc.get(i).getDocumentoInvalido().substring(
                        listaDoc.get(i).getDocumentoInvalido().length() - 12,
                        listaDoc.get(i).getDocumentoInvalido().length());
                digito = ValidaDocumentos.retonarDigitoCNPJ(documento);
                if (ValidaDocumentos.isValidoCNPJ(documento + digito)) {
                    dtObject = new DataObject(false,
                            AnaliseString.mascaraCnpj(documento + digito),// -- DOCUMENTO
                            "** VERIFICAR **",// -- STATUS
                            listaDoc.get(i),
                            true,
                            listaDoc.get(i).getDtImportacao());
                    listaDocumentos.add(dtObject);
                } else {
                    dtObject = new DataObject(false,
                            documento,// -- DOCUMENTO
                            "** INVALIDO **",// -- STATUS
                            listaDoc.get(i),
                            true,
                            listaDoc.get(i).getDtImportacao());
                    listaDocumentos.add(dtObject);
                }
            }
        }
        return listaDocumentos;
    }
    
    public void atualizarBoletoCadastro(){
        MovimentoDB db = new MovimentoDBToplink();
        List<Movimento> lm = new ArrayList<Movimento>();
        List<Juridica> l_juridicax = new ArrayList<Juridica>();
        JuridicaDB dbj = new JuridicaDBToplink();
                
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        for (DataObject listaDocumento : listaDocumentos) {
            DocumentoInvalido di = (DocumentoInvalido)sv.pesquisaCodigo( ((DocumentoInvalido)listaDocumento.getArgumento3()).getId(), "DocumentoInvalido");
            
            if (!(Boolean)listaDocumento.getArgumento4()){
                lm = db.pesquisaMovimentoCadastrado(di.getDocumentoInvalido());
                l_juridicax = dbj.pesquisaJuridicaPorDoc(listaDocumento.getArgumento1().toString());
                if (!l_juridicax.isEmpty() && !lm.isEmpty()){
                    
                    sv.abrirTransacao();
                    for (Movimento lmovimento : lm) {

                        lmovimento.getLote().setPessoa(l_juridicax.get(0).getPessoa());

                        if (!sv.alterarObjeto(lmovimento.getLote())){ sv.desfazerTransacao(); return; }

                        lmovimento.setPessoa(l_juridicax.get(0).getPessoa());
                        lmovimento.setBeneficiario(l_juridicax.get(0).getPessoa());
                        lmovimento.setTitular(l_juridicax.get(0).getPessoa());;

                        if (!sv.alterarObjeto(lmovimento)){ sv.desfazerTransacao(); return; }
                      
                    }
                    
                    di.setChecado(true);
                    if (!sv.alterarObjeto(di)){ sv.desfazerTransacao(); return; }
                    sv.comitarTransacao();
                    
                }
            }else if ((Boolean)listaDocumento.getArgumento0()){
                sv.abrirTransacao();

                di.setChecado(true);
                
                if (!sv.alterarObjeto(di)){ sv.desfazerTransacao(); return; }
                
                sv.comitarTransacao();
            }
        }
        
        listaDocumentos.clear();
    }
    
    public String enviarArquivoBaixar(boolean pendentes) {
        try {
            Usuario usuario = new Usuario();
            usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");
            Object objs[] = caminhoServico();
            String caminhoCompleto = (String) objs[0];
            ContaCobranca scc = (ContaCobranca) objs[1];
            String result = "";
            ArquivoRetorno arquivoRetorno = null;
            if (!validarArquivos(objs)) {
                return null;
            }
            if (!listaArquivoRetorno.isEmpty() || pendentes) {
                if (!outros) {
                    // CAIXA FEDERAL ------------------------------------------------------------------------------
                    if (ArquivoRetorno.CAIXA_FEDERAL == scc.getContaBanco().getBanco().getId()) {
                        if (ArquivoRetorno.SICOB == scc.getLayout().getId()) {
                            arquivoRetorno = new CaixaFederal(scc, pendentes);
                            result = arquivoRetorno.darBaixaSicob(caminhoCompleto, usuario);
                        } else if (ArquivoRetorno.SINDICAL == scc.getLayout().getId()) {
                            arquivoRetorno = new CaixaFederal(scc, pendentes);
                            result = arquivoRetorno.darBaixaSindical(caminhoCompleto, usuario);
                        } else if (ArquivoRetorno.SIGCB == scc.getLayout().getId()) {
                            arquivoRetorno = new CaixaFederal(scc, pendentes);
                            result = arquivoRetorno.darBaixaSigCB(caminhoCompleto, usuario);
                        }
                        // BANCO DO BRASIL ------------------------------------------------------------------------------
                    } else if (ArquivoRetorno.BANCO_BRASIL == scc.getContaBanco().getBanco().getId()) {
                        if (ArquivoRetorno.SICOB == scc.getLayout().getId()) {
                            arquivoRetorno = new BancoBrasil(scc, pendentes);
                            result = arquivoRetorno.darBaixaSicob(caminhoCompleto, usuario);
                        } else if (ArquivoRetorno.SINDICAL == scc.getLayout().getId()) {
                            result = "NÃO EXISTE SINDICAL PARA ESTA CONTA!";
                        } else if (ArquivoRetorno.SIGCB == scc.getLayout().getId()) {
                            result = "NÃO EXISTE SIGCB PARA ESTA CONTA!";
                        }
                        // REAL ------------------------------------------------------------------------------
                    } else if (ArquivoRetorno.REAL == scc.getContaBanco().getBanco().getId()) {
                        if (ArquivoRetorno.SICOB == scc.getLayout().getId()) {
                            arquivoRetorno = new Real(scc, pendentes);
                            result = arquivoRetorno.darBaixaSicob(caminhoCompleto, usuario);
                        } else if (ArquivoRetorno.SINDICAL == scc.getLayout().getId()) {
                            result = "NÃO EXISTE SINDICAL PARA ESTA CONTA!";
                        } else if (ArquivoRetorno.SIGCB == scc.getLayout().getId()) {
                            result = "NÃO EXISTE SIGCB PARA ESTA CONTA!";
                        }
                        // ITAU --------------------------------------------------------------------------------
                    } else if (ArquivoRetorno.ITAU == scc.getContaBanco().getBanco().getId()) {
                        if (ArquivoRetorno.SICOB == scc.getLayout().getId()) {
                            arquivoRetorno = new Itau(scc, pendentes);
                            result = arquivoRetorno.darBaixaSicob(caminhoCompleto, usuario);
                        } else if (ArquivoRetorno.SINDICAL == scc.getLayout().getId()) {
                            result = "NÃO EXISTE SINDICAL PARA ESTA CONTA!";
                        } else if (ArquivoRetorno.SIGCB == scc.getLayout().getId()) {
                            result = "NÃO EXISTE SIGCB PARA ESTA CONTA!";
                        }
                    }
                    //------------------------------------------------------
                } else {
                    arquivoRetorno = new RetornoPadrao(scc, pendentes);
                    result = arquivoRetorno.darBaixaPadrao(usuario);
                }
            } else {
                result = "Lista Vazia!";
            }
            msgOk = "Processamento concluído! --- " + result;
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public void testar() {
        String criei = "";
    }

    public boolean validarArquivos(Object object[]) {
        Object[] objeto = caminhoServico();
        String caminho = (String) objeto[0];
        ContaCobranca scc = (ContaCobranca) objeto[1];

        String caminhoValida = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/retorno/pendentes");
        if (!outros) {
            try {
                File fl = new File(caminhoValida);
                File listFile[] = fl.listFiles();
                int numArq = listFile.length;
                for (int i = 0; i < numArq; i++) {
                    if (verificaArquivos(listFile[i], scc)) {
                        File fil = new File(caminho);
                        fil.mkdir();

//                        fl = new File((caminhoValida+"/"+listFile[i].getName()));
//                        FileInputStream in = new FileInputStream(fl);
//                        byte[] buf = new byte[(int)fl.length()];
//                        fl = new File(fil.getPath()+"/"+listFile[i].getName());
//                        FileOutputStream out = new FileOutputStream(fl.getPath());
//
//                        int count;
//                        while ((count = in.read(buf)) >= 0) {
//                            out.write(buf, 0, count);
//                        }
//                        in.close();
//                        out.flush();
//                        out.close();
                    } else {
                        listFile[i].delete();
                        msgOk = "Serviço e Conta não correspondem com os arquivos!";
                        return false;
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
                msgOk = "Erro ao enviar Arquivo " + e;
                return false;
            }
            msgOk = "Arquivos enviados com Sucesso!";
            return true;
        } else {
            try {
                File flCriado = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/retorno/"));
                flCriado.mkdir();

                File fl = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/retorno/ret.ret"));
                File fl2 = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/retorno/padrao/ARQUIVOBAIXA.ret"));

                FileInputStream in = new FileInputStream(fl);
                FileOutputStream out = new FileOutputStream(fl2.getPath());

                byte[] buf = new byte[(int) fl.length()];
                int count;
                while ((count = in.read(buf)) >= 0) {
                    out.write(buf, 0, count);
                }
                in.close();
                out.flush();
                out.close();
            } catch (Exception e) {
                System.out.println(e);
                msgOk = "Erro ao enviar Arquivo " + e;
                return false;
            }
            msgOk = "Arquivos enviados com Sucesso!";
        }
        return false;
    }

    public Object[] caminhoPendente() {
        Object obj[] = new Object[2];
        String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/retorno/pendentes");
        obj[0] = caminho;
        obj[1] = null;
        return obj;
    }

    public Object[] caminhoServico() {
        Object obj[] = new Object[2];
        if (contaCobranca.getId() != -1) {
            String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/retorno");
            // LAYOUT 2 SINDICAL
            if (contaCobranca.getLayout().getId() == 2) {
                //caminho = caminho +"/"+ contaCobranca.getApelido()+"_"+contaCobranca.getSicasSindical();
                caminho = caminho + "/" + contaCobranca.getSicasSindical();
            } else {
                //caminho = caminho +"/"+ contaCobranca.getApelido()+"_"+contaCobranca.getCodCedente();
                caminho = caminho + "/" + contaCobranca.getCodCedente();
            }
            obj[0] = caminho;
            obj[1] = contaCobranca;
        }
        return obj;
    }

    public Object[] caminhoServicoPendente() {
        Object obj[] = new Object[2];
        if (contaCobranca.getId() != -1) {
            String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/retorno");
            // LAYOUT 2 SINDICAL
            if (contaCobranca.getLayout().getId() == 2) {
                //caminho = caminho +"/"+ contaCobranca.getApelido()+"_"+contaCobranca.getSicasSindical();
                caminho = caminho + "/" + contaCobranca.getSicasSindical();
                caminho = caminho + "/pendentes";
            } else {
                //caminho = caminho +"/"+ contaCobranca.getApelido()+"_"+contaCobranca.getCodCedente();
                caminho = caminho + "/" + contaCobranca.getCodCedente();
                caminho = caminho + "/pendentes";
            }
            obj[0] = caminho;
            obj[1] = contaCobranca;
        }
        return obj;
    }

    public boolean verificaArquivos(File file, ContaCobranca scc) {
        if (!listaArquivoRetorno.isEmpty()) {
            try {
                reader = new FileReader(file);
                buffReader = new BufferedReader(reader);
                lista = new Vector();
                lista.add(buffReader.readLine());

                reader.close();
                buffReader.close();
                // CAIXA FEDERAL --------------------------------------------------------------------------------
                if (ArquivoRetorno.CAIXA_FEDERAL == scc.getContaBanco().getBanco().getId()) {
                    if (ArquivoRetorno.SICOB == scc.getLayout().getId()) {
                        String xxx = ((String) lista.get(0)).substring(59, 70);
                        if (((String) lista.get(0)).substring(59, 70).equals(scc.getCodCedente())) {
                            return true;
                        } else {
                            return false;
                        }
                    } else if (ArquivoRetorno.SINDICAL == scc.getLayout().getId()) {
                        if (((String) lista.get(0)).substring(33, 38).equals(scc.getSicasSindical())) {
                            return true;
                        } else {
                            return false;
                        }
                    } else if (ArquivoRetorno.SIGCB == scc.getLayout().getId()) {
                        if (((String) lista.get(0)).substring(58, 64).equals(scc.getCodCedente())) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                    // BANCO DO BRASIL ------------------------------------------------------------------------------
                } else if (ArquivoRetorno.BANCO_BRASIL == scc.getContaBanco().getBanco().getId()) {
                    if (ArquivoRetorno.SICOB == scc.getLayout().getId()) {
                        String xxx = ((String) lista.get(0)).substring(66, 70);
                        if (((String) lista.get(0)).substring(66, 70).equals(scc.getCodCedente())) {
                            return true;
                        } else {
                            return false;
                        }
                    } else if (ArquivoRetorno.SINDICAL == scc.getLayout().getId()) {
                        return false;
                    } else if (ArquivoRetorno.SIGCB == scc.getLayout().getId()) {
                        return false;
                    }
                    // REAL --------------------------------------------------------------------------------------
                } else if (ArquivoRetorno.REAL == scc.getContaBanco().getBanco().getId()) {
                    if (ArquivoRetorno.SICOB == scc.getLayout().getId()) {
                        if (((String) lista.get(0)).substring(63, 70).equals(scc.getCodCedente())) {
                            return true;
                        } else {
                            return false;
                        }
                    } else if (ArquivoRetorno.SINDICAL == scc.getLayout().getId()) {
                        return false;
                    } else if (ArquivoRetorno.SIGCB == scc.getLayout().getId()) {
                        return false;
                    }
                    // ITAU --------------------------------------------------------------------------------------
                } else if (ArquivoRetorno.ITAU == scc.getContaBanco().getBanco().getId()) {
                    if (ArquivoRetorno.SICOB == scc.getLayout().getId()) {
                        if (((String) lista.get(0)).substring(32, 37).equals(scc.getCodCedente())) {
                            return true;
                        } else {
                            return false;
                        }
                    } else if (ArquivoRetorno.SINDICAL == scc.getLayout().getId()) {
                        return false;
                    } else if (ArquivoRetorno.SIGCB == scc.getLayout().getId()) {
                        return false;
                    }
                }
            } catch (Exception e) {
            }
        }
        return false;
    }

    public String excluirArquivo() {
        Object objs[] = null;
        String caminho = "";//(String)objs[0]+"/"+((GenericaQuery)getHtmlDataTableExcluir().getRowData()).getArgumento0();
        //String caminho = (String)objs[0]+"/"+((GenericaQuery)getHtmlDataTableExcluir().getRowData()).getArgumento0();

        File fl = new File(caminho);
        fl.delete();
        return "retornoBanco";
    }

    public String imprimirDocumentos() {
        try {
            FacesContext faces = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
            byte[] arquivo = new byte[0];
            JasperReport jasper = null;
            Collection listaDocs = new ArrayList<DocumentoInvalido>();

            jasper = (JasperReport) JRLoader.loadObject(
                    ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/DOCUMENTOINVALIDO.jasper"));

            try {
                for (int i = 0; i < listaDocumentos.size(); i++) {
                    //listaDocs.add((DocumentoInvalido)((DataObject) listaDocumentos.get(i)).getArgumento1());
                    listaDocs.add(new DocumentoInvalido(
                            -1,
                            listaDocumentos.get(i).getArgumento1().toString(),
                            true,
                            DataHoje.converteData((Date) ((DataObject) listaDocumentos.get(i)).getArgumento5())));
                }
                JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(listaDocs);
                JasperPrint print = JasperFillManager.fillReport(
                        jasper,
                        null,
                        dtSource);
                arquivo = JasperExportManager.exportReportToPdf(print);
                response.setContentType("application/pdf");
                response.setContentLength(arquivo.length);
                ServletOutputStream saida = response.getOutputStream();
                saida.write(arquivo, 0, arquivo.length);
                saida.flush();
                saida.close();
            } catch (Exception erro) {
                System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
            }
        } catch (Exception erro) {
            System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
        }
        FacesContext.getCurrentInstance().responseComplete();
        Download download = new Download(
                "Documentos Inválidos.pdf",
                ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/retornoBanco.jsf"),
                "pdf",
                FacesContext.getCurrentInstance());
        download.baixar();
        return null;
    }
    
    public void refreshForm() {
    }

    public int getIdContrib() {
        return idContrib;
    }

    public void setIdContrib(int idContrib) {
        this.idContrib = idContrib;
    }

    public String getMsgOk() {
        return msgOk;
    }

    public void setMsgOk(String msgOk) {
        this.msgOk = msgOk;
    }

    public Object getRetornoBanco() {
        retornoBanco = new Integer(-1);
        return retornoBanco;
    }

    public void setRetornoBanco(Object retornoBanco) {
        this.retornoBanco = retornoBanco;
    }

    public boolean isOutros() {
        return outros;
    }

    public void setOutros(boolean outros) {
        this.outros = outros;
    }

    public int getIndexArquivos() {
        return indexArquivos;
    }

    public void setIndexArquivos(int indexArquivos) {
        this.indexArquivos = indexArquivos;
    }

    public void setListaPendencias(List listaPendencias) {
        this.listaPendencias = listaPendencias;
    }

    public String getLblPendente() {
        if (contaCobranca.getId() != -1 && !getListaServicoCobranca().isEmpty()) {
            if (contaCobranca.getLayout().getId() == 2) {
                //caminho = caminho +"/"+ contaCobranca.getApelido()+"_"+contaCobranca.getSicasSindical();
                lblPendente = contaCobranca.getApelido() + "-" + contaCobranca.getSicasSindical();
            } else {
                lblPendente = contaCobranca.getApelido() + "-" + contaCobranca.getCodCedente();
            }
        }
        return lblPendente;
    }

    public void setLblPendente(String lblPendente) {
        this.lblPendente = lblPendente;
    }

    public void setStrServicos(String strServicos) {
        this.strServicos = strServicos;
    }

    public ContaCobranca getContaCobranca() {
        return contaCobranca;
    }

    public void setContaCobranca(ContaCobranca contaCobranca) {
        this.contaCobranca = contaCobranca;
    }
}