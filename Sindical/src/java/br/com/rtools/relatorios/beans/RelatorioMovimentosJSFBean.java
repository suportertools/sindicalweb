package br.com.rtools.relatorios.beans;

import br.com.rtools.arrecadacao.Convencao;
import br.com.rtools.arrecadacao.ConvencaoCidade;
import br.com.rtools.arrecadacao.db.*;
import br.com.rtools.endereco.Cidade;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.TipoServico;
import br.com.rtools.financeiro.beans.MovimentoValorBean;
import br.com.rtools.financeiro.db.*;
import br.com.rtools.impressao.ParametroMovimentos;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.db.*;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.relatorios.dao.RelatorioDao;
import br.com.rtools.relatorios.db.RelatorioMovimentosDB;
import br.com.rtools.relatorios.db.RelatorioMovimentosDBToplink;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.*;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class RelatorioMovimentosJSFBean extends MovimentoValorBean {

    private Juridica juridica = new Juridica();
    private int idRelatorios = 0;
    private int idServicos = 0;
    private int idTipoServico = 0;
    private int idConvencao = 0;
    private int idGrupoCidade = 0;
    private String comboCondicao = "todos";
    private String comboGeradosPelaCaixa = "naoVerificar";
    private String tipoDataPesquisa = "vencimento";
    private String dataInicial = "";
    private String dataFinal = "";
    private String dataRefInicial = "";
    private String dataRefFinal = "";
    private String radioOrdem = "vencimento";
    private String porPesquisa = "todos";
    private String porContabilidade = "todos";
    private String relEmpresa = "empresa";
    private String msgEmail = "";
    private String renEnviarEmail = "true";
    private boolean chkTipo = false;
    private boolean chkData = false;
    private boolean chkPesquisaEmpresa = false;
    private boolean chkContribuicao = false;
    private boolean chkEmpresa = false;
    private boolean chkConvencao = false;
    private boolean chkCidadesBase = false;
    private boolean chkContabilidades = false;
    private boolean totaliza = false;
    private List listaCidadesBase = new ArrayList();
    private List listaContabilidades = new ArrayList();

    public String condicaoDePesquisa() {
        String condicao = "";
        String geradosPelaCaixa = "";
        int idServ = 0;
        int idTipoServ = 0;
        int idJuridica = 0;
        int idConv = 0;
        int idGrup = 0;
        Date dtInicial = null;
        Date dtFinal = null;

        RelatorioDao db = new RelatorioDao();
        RelatorioMovimentosDB dbMov = new RelatorioMovimentosDBToplink();

        Relatorios relatorios = new Relatorios();
        relatorios = db.pesquisaRelatorios(Integer.parseInt(getListaTipoRelatorios().get(idRelatorios).getDescription()));

        Juridica sindicato = new Juridica();
        JuridicaDB dbJur = new JuridicaDBToplink();
        PessoaEndereco endSindicato = new PessoaEndereco();
        PessoaEnderecoDB dbPesEnd = new PessoaEnderecoDBToplink();

        FinanceiroDB dbFin = new FinanceiroDBToplink();

        // CONDICAO DO RELATORIO -----------------------------------------------------------
        condicao = comboCondicao;

        // GERADOS PELA CAIXA DO RELATORIO -------------------------------------------------
        geradosPelaCaixa = comboGeradosPelaCaixa;

        // SERVICO DO RELATORIO -----------------------------------------------------------
        if (chkContribuicao) {
            idServ = Integer.parseInt(getListaServico().get(idServicos).getDescription());
        }

        // TIPO SERVICO DO RELATORIO ------------------------------------------------------
        if (chkTipo) {
            idTipoServ = Integer.parseInt(getListaTipoServico().get(idTipoServico).getDescription());
        }
        // PESSOA DO RELATORIO -----------------------------------------------------------
        if (chkEmpresa && juridica != null) {
            idJuridica = juridica.getId();
        }

        // DATA DO RELATORIO ------------------------------------------------------------
        if (chkData) {
            if (!tipoDataPesquisa.equals("referencia")) {
                if (!dataInicial.equals("") && (!dataFinal.equals(""))) {
                    dtInicial = DataHoje.converte(dataInicial);
                    dtFinal = DataHoje.converte(dataFinal);
                } else {
                    dtInicial = null;
                    dtFinal = null;
                }
            } else {
                if (!dataRefInicial.equals("") && !dataRefFinal.equals("")) {
                }
            }
        }

        // CONVENCAO DO RELATORIO ------------------------------------------------------------
        if (chkConvencao) {
            idConv = Integer.parseInt(getListaConvencao().get(idConvencao).getDescription());
        }

        // GRUPO CIDADE DO RELATORIO -------------------------------------------------------------
        if (chkConvencao) {
            idGrup = Integer.parseInt(getListaGrupoCidade().get(idGrupoCidade).getDescription());
        }

        // IDS DAS CONTABILIDADES -----------------------------------------------------------------
        String idsEcs = "";
        if (!porContabilidade.equals("sem")) {
            for (int i = 0; i < listaContabilidades.size(); i++) {
                if ((Boolean) ((DataObject) listaContabilidades.get(i)).getArgumento0() == true) {
                    if (idsEcs.length() > 0 && i != listaContabilidades.size()) {
                        idsEcs += ",";
                    }
                    idsEcs += ((Juridica) ((DataObject) listaContabilidades.get(i)).getArgumento1()).getId();
                }
            }
        } else {
            idsEcs = "sem";
        }

        // IDS DAS CIDADES DA BASE ----------------------------------------------------------------
        String cidBase = "";
        for (int i = 0; i < listaCidadesBase.size(); i++) {
            if ((Boolean) ((DataObject) listaCidadesBase.get(i)).getArgumento0() == true) {
                if (cidBase.length() > 0 && i != listaCidadesBase.size()) {
                    cidBase += ",";
                }
                cidBase += ((Cidade) ((DataObject) listaCidadesBase.get(i)).getArgumento1()).getId();
                if (cidBase.length() == 1) {
                    cidBase = "0" + cidBase;
                }
            }
        }
        sindicato = dbJur.pesquisaCodigo(1);
        endSindicato = dbPesEnd.pesquisaEndPorPessoaTipo(1, 3);

        List result = dbMov.listaMovimentos(relatorios, condicao, idServ, idTipoServ, idJuridica, chkData, tipoDataPesquisa, dtInicial, dtFinal,
                dataRefInicial, dataRefFinal, radioOrdem, chkPesquisaEmpresa, porPesquisa, relEmpresa, idConv, idGrup, cidBase, idsEcs, "");
        try {
            FacesContext faces = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
            byte[] arquivo = new byte[0];
            JasperReport jasper = null;
            Collection listaMovs = new ArrayList<ParametroMovimentos>();
            jasper = (JasperReport) JRLoader.loadObject(
                    new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath(relatorios.getJasper()))
            );
            String quitacao, importacao, usuario;
            float valor = 0, repasse = 0, valorLiquido = 0;

            try {
                for (int i = 0; i < result.size(); i++) {
                    valor = Float.parseFloat(getConverteNullString(((Vector) result.get(i)).get(6))); // VALOR ORIGINAL     
                    if (((Vector) result.get(i)).get(38) == null) {
                        quitacao = "";
                        importacao = "";
                        usuario = "";
                    } else {
//                        if (!getConverteNullString(((Vector) result.get(i)).get(6)).equals("") && Float.parseFloat(getConverteNullString(((Vector) result.get(i)).get(6))) == 0 ){
//                            valor = Float.parseFloat(getConverteNullString(((Vector) result.get(i)).get(47))); // VALOR DA BAIXA
//                        }else{
//                        }

                        quitacao = DataHoje.converteData((Date) ((Vector) result.get(i)).get(39));
                        importacao = DataHoje.converteData((Date) ((Vector) result.get(i)).get(40));
                        usuario = getConverteNullString(((Vector) result.get(i)).get(42));
                    }
                    String srepasse = getConverteNullString(((Vector) result.get(i)).get(48));
                    if (srepasse.isEmpty()) {
                        repasse = Moeda.multiplicarValores(Float.parseFloat(getConverteNullString(((Vector) result.get(i)).get(47))), Moeda.divisaoValores(0, 100));
                    } else {
                        repasse = Moeda.multiplicarValores(Float.parseFloat(getConverteNullString(((Vector) result.get(i)).get(47))), Moeda.divisaoValores(Float.parseFloat(srepasse), 100));
                    }

                    valorLiquido = Moeda.subtracaoValores(Moeda.subtracaoValores(Float.parseFloat(getConverteNullString(((Vector) result.get(i)).get(47))), Float.valueOf(Float.parseFloat(getConverteNullString(((Vector) result.get(i)).get(43))))), repasse);

                    listaMovs.add(new ParametroMovimentos(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
                            sindicato.getPessoa().getNome(),
                            endSindicato.getEndereco().getDescricaoEndereco().getDescricao(),
                            endSindicato.getEndereco().getLogradouro().getDescricao(),
                            endSindicato.getNumero(),
                            endSindicato.getComplemento(),
                            endSindicato.getEndereco().getBairro().getDescricao(),
                            endSindicato.getEndereco().getCep(),
                            endSindicato.getEndereco().getCidade().getCidade(),
                            endSindicato.getEndereco().getCidade().getUf(),
                            sindicato.getPessoa().getTelefone1(),
                            sindicato.getPessoa().getEmail1(),
                            sindicato.getPessoa().getSite(),
                            sindicato.getPessoa().getTipoDocumento().getDescricao(),
                            sindicato.getPessoa().getDocumento(),
                            getConverteNullInt(((Vector) result.get(i)).get(41)), // ID JURIDICA
                            getConverteNullString(((Vector) result.get(i)).get(10)), // NOME PESSOA
                            getConverteNullString(((Vector) result.get(i)).get(11)), // ENDERECO PESSOA
                            getConverteNullString(((Vector) result.get(i)).get(12)), // LOGRADOURO PESSOA
                            getConverteNullString(((Vector) result.get(i)).get(13)), // NUMERO ENDERECO PESSOA
                            getConverteNullString(((Vector) result.get(i)).get(14)), // COMPLEMENTO PESSOA
                            getConverteNullString(((Vector) result.get(i)).get(15)), // BAIRRO PESSOA
                            getConverteNullString(((Vector) result.get(i)).get(16)), // CEP PESSOA
                            getConverteNullString(((Vector) result.get(i)).get(17)), // CIDADE PESSOA
                            getConverteNullString(((Vector) result.get(i)).get(18)), // UF PESSOA
                            getConverteNullString(((Vector) result.get(i)).get(19)), // TELEFONE PESSOA
                            getConverteNullString(((Vector) result.get(i)).get(20)), // EMAIL PESSOA
                            getConverteNullString(((Vector) result.get(i)).get(21)), // TIPO DOC PESSOA
                            getConverteNullString(((Vector) result.get(i)).get(22)), // DOCUMENTO PESSOA
                            getConverteNullInt(((Vector) result.get(i)).get(23)), // ID CNAE
                            getConverteNullString(((Vector) result.get(i)).get(24)), // NUMERO CNAE
                            getConverteNullString(((Vector) result.get(i)).get(25)), // NOME CNAE
                            getConverteNullInt(((Vector) result.get(i)).get(26)), // ID CONTABILIDADE
                            getConverteNullString(((Vector) result.get(i)).get(27)), // NOME CONTABILIDADE
                            getConverteNullString(((Vector) result.get(i)).get(28)), // ENDERECO CONTABIL
                            getConverteNullString(((Vector) result.get(i)).get(29)), // LOGRADOURO CONTABIL
                            getConverteNullString(((Vector) result.get(i)).get(30)), // NUMERO CONTABIL
                            getConverteNullString(((Vector) result.get(i)).get(31)), // COMPLEMENTO CONTABIL
                            getConverteNullString(((Vector) result.get(i)).get(32)), // BAIRRO CONTABIL
                            getConverteNullString(((Vector) result.get(i)).get(33)), // CEP CONTABIL
                            getConverteNullString(((Vector) result.get(i)).get(34)), // CIDADE CONTABIL
                            getConverteNullString(((Vector) result.get(i)).get(35)), // UF CONTABIL
                            getConverteNullString(((Vector) result.get(i)).get(36)), // TELEFONE CONTABIL
                            getConverteNullString(((Vector) result.get(i)).get(37)), // EMAIL CONTABIL
                            getConverteNullString(((Vector) result.get(i)).get(1)), // NUMERO BOLETO
                            getConverteNullString(((Vector) result.get(i)).get(2)), // SERVICO
                            getConverteNullString(((Vector) result.get(i)).get(3)), // TIPO SERVICO
                            getConverteNullString(((Vector) result.get(i)).get(4)), // REFERENCIA
                            DataHoje.converteData((Date) ((Vector) result.get(i)).get(5)), // VENCIMENTO
                            quitacao, //result.get(i).getLoteBaixa().getQuitacao(),
                            new BigDecimal(Float.parseFloat(getConverteNullString(((Vector) result.get(i)).get(47)))), // VALOR BAIXA
                            new BigDecimal(Float.parseFloat(getConverteNullString(((Vector) result.get(i)).get(43)))),// TAXA
                            importacao, //result.get(i).getLoteBaixa().getImportacao(),
                            usuario,// result.get(i).getLoteBaixa().getUsuario().getPessoa().getNome()
                            new BigDecimal(Float.parseFloat(getConverteNullString(((Vector) result.get(i)).get(44)))),// MULTA,
                            new BigDecimal(Float.parseFloat(getConverteNullString(((Vector) result.get(i)).get(45)))),// JUROS,
                            new BigDecimal(Float.parseFloat(getConverteNullString(((Vector) result.get(i)).get(46)))),// CORRECAO,
                            new BigDecimal(valor), // VALOR TOTAL
                            new BigDecimal(repasse), // REPASSE
                            new BigDecimal(valorLiquido), // VALOR LIQUIDO
                            totaliza));
                }

                JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(listaMovs);

                //String ini = DataHoje.hora();
                JasperPrint print = JasperFillManager.fillReport(
                        jasper,
                        null,
                        dtSource);

                //String fim = DataHoje.hora();
                arquivo = JasperExportManager.exportReportToPdf(print);

                String nomeDownload = "relatorio_movimentos_" + DataHoje.horaMinuto().replace(":", "") + ".pdf";
                SalvaArquivos sa = new SalvaArquivos(arquivo, nomeDownload, false);

                String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/relatorios");
                sa.salvaNaPasta(pathPasta);

                Download download = new Download(nomeDownload,
                        pathPasta,
                        "application/pdf",
                        FacesContext.getCurrentInstance());
                download.baixar();

                //msgEmail = "Inicio: "+ini+" Fim: "+fim;
                return null;
//                 
//         
//                String conteudo = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
//			"<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n" +
//			"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\" />\n" +
//			"<title>Relatório em HTML - Teste </title>\n" +
//			"</head>\n<body>\n";
//			
// 
//                String texto = "";
//                for (int i = 0; i < result.size();i++){
//                    texto += getConverteNullInt( ((Vector) result.get(i)).get(41) )+"\n";
//                }
//                
//                texto += "</body>\n</html>\n";
//                conteudo += texto;
//                
//                 String nomeDownload = "relatorio_movimentos_"+DataHoje.horaMinuto().replace(":", "") +".html";
//                 
//                 SalvaArquivos sa = new SalvaArquivos(new byte[0], nomeDownload, false);
//                 String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Arquivos/downloads/relatorios");
//                 sa.salvaNaPasta(pathPasta);
//                 
//                
//                 ArquiveCreator.execFile(pathPasta+"/"+nomeDownload, conteudo);
//                 
            } catch (Exception erro) {
                System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
                return null;
            }
        } catch (Exception erro) {
            System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
            return null;
        }
        //return null;
    }

    public List<SelectItem> getListaConvencao() {
        List<SelectItem> convencao = new Vector<SelectItem>();
        if (chkConvencao) {
            int i = 0;
            ConvencaoDB db = new ConvencaoDBToplink();
            List select = db.pesquisaTodos();
            while (i < select.size()) {
                convencao.add(new SelectItem(new Integer(i),
                        (String) ((Convencao) select.get(i)).getDescricao(),
                        Integer.toString(((Convencao) select.get(i)).getId())));
                i++;
            }
        }
        return convencao;
    }

    public List<SelectItem> getListaGrupoCidade() {
        List<SelectItem> grupoCidade = new Vector<SelectItem>();
        if (chkConvencao) {
            int i = 0;
            GrupoCidadeDB db = new GrupoCidadeDBToplink();
            List select = db.pesquisaGrupoPorConvencao(Integer.parseInt(getListaConvencao().get(idConvencao).getDescription()));
            while (i < select.size()) {
                grupoCidade.add(new SelectItem(new Integer(i),
                        (String) ((ConvencaoCidade) select.get(i)).getGrupoCidade().getDescricao(),
                        Integer.toString(((ConvencaoCidade) select.get(i)).getGrupoCidade().getId())));
                i++;
            }
        }
        return grupoCidade;
    }

    public String enviarEmail() {
        String condicao = "";
        String geradosPelaCaixa = "";
        int idServ = 0;
        int idTipoServ = 0;
        int idJuridica = 0;
        int idConv = 0;
        int idGrup = 0;
        Date dtInicial = null;
        Date dtFinal = null;

        RelatorioDao db = new RelatorioDao();
        RelatorioMovimentosDB dbMov = new RelatorioMovimentosDBToplink();

        Relatorios relatorios = new Relatorios();
        relatorios = db.pesquisaRelatorios(Integer.parseInt(getListaTipoRelatorios().get(idRelatorios).getDescription()));

        Juridica sindicato = new Juridica();
        JuridicaDB dbJur = new JuridicaDBToplink();
        PessoaEndereco endSindicato = new PessoaEndereco();
        PessoaEnderecoDB dbPesEnd = new PessoaEnderecoDBToplink();

        Registro reg = new Registro();
        reg = (Registro) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(1, "Registro");

        // CONDICAO DO RELATORIO -----------------------------------------------------------
        condicao = comboCondicao;

        // GERADOS PELA CAIXA DO RELATORIO -------------------------------------------------
        geradosPelaCaixa = comboGeradosPelaCaixa;

        // SERVICO DO RELATORIO -----------------------------------------------------------
        if (chkContribuicao) {
            idServ = Integer.parseInt(getListaServico().get(idServicos).getDescription());
        }

        // TIPO SERVICO DO RELATORIO ------------------------------------------------------
        if (chkTipo) {
            idTipoServ = Integer.parseInt(getListaTipoServico().get(idTipoServico).getDescription());
        }
        // PESSOA DO RELATORIO -----------------------------------------------------------
        if (chkEmpresa && juridica != null) {
            idJuridica = juridica.getPessoa().getId();
        }

        // DATA DO RELATORIO ------------------------------------------------------------
        if (chkData) {
            if (!tipoDataPesquisa.equals("referencia")) {
                if (!dataInicial.equals("") && (!dataFinal.equals(""))) {
                    dtInicial = DataHoje.converte(dataInicial);
                    dtFinal = DataHoje.converte(dataFinal);
                } else {
                    dtInicial = null;
                    dtFinal = null;
                }
            } else {
                if (!dataRefInicial.equals("") && !dataRefFinal.equals("")) {
                }
            }
        }

        // IDS DAS CONTABILIDADES -----------------------------------------------------------------
        String idsEcs = "";
        if (!porContabilidade.equals("sem")) {
            for (int i = 0; i < listaContabilidades.size(); i++) {
                if ((Boolean) ((DataObject) listaContabilidades.get(i)).getArgumento0() == true) {
                    if (idsEcs.length() > 0 && i != listaContabilidades.size()) {
                        idsEcs += ",";
                    }
                    idsEcs += ((Juridica) ((DataObject) listaContabilidades.get(i)).getArgumento1()).getId();
                }
            }
        } else {
            idsEcs = "sem";
        }

        // IDS DAS CIDADES DA BASE ----------------------------------------------------------------
        String cidBase = "";
        for (int i = 0; i < listaCidadesBase.size(); i++) {
            if ((Boolean) ((DataObject) listaCidadesBase.get(i)).getArgumento0() == true) {
                if (cidBase.length() > 0 && i != listaCidadesBase.size()) {
                    cidBase += ",";
                }
                cidBase += ((Cidade) ((DataObject) listaCidadesBase.get(i)).getArgumento1()).getId();
                if (cidBase.length() == 1) {
                    cidBase = "0" + cidBase;
                }
            }
        }

        String pathPasta = "";

        sindicato = dbJur.pesquisaCodigo(1);
        endSindicato = dbPesEnd.pesquisaEndPorPessoaTipo(1, 3);

        List result = dbMov.listaMovimentos(relatorios, condicao, idServ, idTipoServ, idJuridica, chkData, tipoDataPesquisa, dtInicial, dtFinal,
                dataRefInicial, dataRefFinal, radioOrdem, chkPesquisaEmpresa, porPesquisa, relEmpresa, idConv, idGrup, cidBase, idsEcs, "");
        FacesContext faces = FacesContext.getCurrentInstance();
        int qnt = 0;
        String nomeDownload = "";
        try {
            byte[] arquivo = new byte[0];
            JasperReport jasper = null;
            Collection listaMovs = new ArrayList<ParametroMovimentos>();
            jasper = (JasperReport) JRLoader.loadObject(
                    new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath(relatorios.getJasper()))
            );
            String quitacao, importacao, usuario;
            float valor = 0, repasse = 0, valorLiquido = 0;
            try {
                for (int i = 0; i < result.size(); i++) {
                    if (((Vector) result.get(i)).get(38) == null) {
                        quitacao = "";
                        importacao = "";
                        usuario = "";
                        valor = 0;
                    } else {
                        if (!getConverteNullString(((Vector) result.get(i)).get(6)).equals("") && Float.parseFloat(getConverteNullString(((Vector) result.get(i)).get(6))) == 0) {
                            valor = super.carregarValor(getConverteNullInt(((Vector) result.get(i)).get(7)), getConverteNullInt(((Vector) result.get(i)).get(8)),
                                    getConverteNullString(((Vector) result.get(i)).get(4)), getConverteNullInt(((Vector) result.get(i)).get(9)));
                        } else {
                            if (porPesquisa.equals("recebidas")) {
                                valor = Float.parseFloat(getConverteNullString(((Vector) result.get(i)).get(47))); // VALOR DA BAIXA
                            } else {
                                valor = Float.parseFloat(getConverteNullString(((Vector) result.get(i)).get(6))); // VALOR ORIGINAL
                            }
                        }

                        quitacao = DataHoje.converteData((Date) ((Vector) result.get(i)).get(39));
                        importacao = DataHoje.converteData((Date) ((Vector) result.get(i)).get(40));
                        usuario = getConverteNullString(((Vector) result.get(i)).get(42));
                    }

                    repasse = Moeda.multiplicarValores(valor, Moeda.divisaoValores(
                            Float.parseFloat(getConverteNullString(((Vector) result.get(i)).get(48))), 100 // 48 % NR_REPASSE DA CONTA COBRANCA
                    ));
                    valorLiquido = Moeda.subtracaoValores(Moeda.subtracaoValores(Float.valueOf(valor), Float.valueOf(Float.parseFloat(getConverteNullString(((Vector) result.get(i)).get(43))))), repasse);

                    listaMovs.add(new ParametroMovimentos(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Imagens/LogoCliente.png"),
                            sindicato.getPessoa().getNome(),
                            endSindicato.getEndereco().getDescricaoEndereco().getDescricao(),
                            endSindicato.getEndereco().getLogradouro().getDescricao(),
                            endSindicato.getNumero(),
                            endSindicato.getComplemento(),
                            endSindicato.getEndereco().getBairro().getDescricao(),
                            endSindicato.getEndereco().getCep(),
                            endSindicato.getEndereco().getCidade().getCidade(),
                            endSindicato.getEndereco().getCidade().getUf(),
                            sindicato.getPessoa().getTelefone1(),
                            sindicato.getPessoa().getEmail1(),
                            sindicato.getPessoa().getSite(),
                            sindicato.getPessoa().getTipoDocumento().getDescricao(),
                            sindicato.getPessoa().getDocumento(),
                            getConverteNullInt(((Vector) result.get(i)).get(41)), // ID JURIDICA
                            getConverteNullString(((Vector) result.get(i)).get(10)), // NOME PESSOA
                            getConverteNullString(((Vector) result.get(i)).get(11)), // ENDERECO PESSOA
                            getConverteNullString(((Vector) result.get(i)).get(12)), // LOGRADOURO PESSOA
                            getConverteNullString(((Vector) result.get(i)).get(13)), // NUMERO ENDERECO PESSOA
                            getConverteNullString(((Vector) result.get(i)).get(14)), // COMPLEMENTO PESSOA
                            getConverteNullString(((Vector) result.get(i)).get(15)), // BAIRRO PESSOA
                            getConverteNullString(((Vector) result.get(i)).get(16)), // CEP PESSOA
                            getConverteNullString(((Vector) result.get(i)).get(17)), // CIDADE PESSOA
                            getConverteNullString(((Vector) result.get(i)).get(18)), // UF PESSOA
                            getConverteNullString(((Vector) result.get(i)).get(19)), // TELEFONE PESSOA
                            getConverteNullString(((Vector) result.get(i)).get(20)), // EMAIL PESSOA
                            getConverteNullString(((Vector) result.get(i)).get(21)), // TIPO DOC PESSOA
                            getConverteNullString(((Vector) result.get(i)).get(22)), // DOCUMENTO PESSOA
                            getConverteNullInt(((Vector) result.get(i)).get(23)), // ID CNAE
                            getConverteNullString(((Vector) result.get(i)).get(24)), // NUMERO CNAE
                            getConverteNullString(((Vector) result.get(i)).get(25)), // NOME CNAE
                            getConverteNullInt(((Vector) result.get(i)).get(26)), // ID CONTABILIDADE
                            getConverteNullString(((Vector) result.get(i)).get(27)), // NOME CONTABILIDADE
                            getConverteNullString(((Vector) result.get(i)).get(28)), // ENDERECO CONTABIL
                            getConverteNullString(((Vector) result.get(i)).get(29)), // LOGRADOURO CONTABIL
                            getConverteNullString(((Vector) result.get(i)).get(30)), // NUMERO CONTABIL
                            getConverteNullString(((Vector) result.get(i)).get(31)), // COMPLEMENTO CONTABIL
                            getConverteNullString(((Vector) result.get(i)).get(32)), // BAIRRO CONTABIL
                            getConverteNullString(((Vector) result.get(i)).get(33)), // CEP CONTABIL
                            getConverteNullString(((Vector) result.get(i)).get(34)), // CIDADE CONTABIL
                            getConverteNullString(((Vector) result.get(i)).get(35)), // UF CONTABIL
                            getConverteNullString(((Vector) result.get(i)).get(36)), // TELEFONE CONTABIL
                            getConverteNullString(((Vector) result.get(i)).get(37)), // EMAIL CONTABIL
                            getConverteNullString(((Vector) result.get(i)).get(1)), // NUMERO BOLETO
                            getConverteNullString(((Vector) result.get(i)).get(2)), // SERVICO
                            getConverteNullString(((Vector) result.get(i)).get(3)), // TIPO SERVICO
                            getConverteNullString(((Vector) result.get(i)).get(4)), // REFERENCIA
                            DataHoje.converteData((Date) ((Vector) result.get(i)).get(5)), // VENCIMENTO
                            quitacao, //result.get(i).getLoteBaixa().getQuitacao(),
                            new BigDecimal(Float.parseFloat(getConverteNullString(((Vector) result.get(i)).get(47)))),// VALOR BAIXA
                            new BigDecimal(Float.parseFloat(getConverteNullString(((Vector) result.get(i)).get(43)))),// TAXA
                            importacao, //result.get(i).getLoteBaixa().getImportacao(),
                            usuario,// result.get(i).getLoteBaixa().getUsuario().getPessoa().getNome()
                            new BigDecimal(Float.parseFloat(getConverteNullString(((Vector) result.get(i)).get(44)))),// MULTA,
                            new BigDecimal(Float.parseFloat(getConverteNullString(((Vector) result.get(i)).get(45)))),// JUROS,
                            new BigDecimal(Float.parseFloat(getConverteNullString(((Vector) result.get(i)).get(46)))),// CORRECAO,
                            new BigDecimal(valor),// VALOR TOTAL
                            new BigDecimal(repasse),// REPASSE
                            new BigDecimal(valorLiquido),// VALOR LIQUIDO
                            totaliza));
                }
                JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(listaMovs);
                JasperPrint print = JasperFillManager.fillReport(
                        jasper,
                        null,
                        dtSource);
                arquivo = JasperExportManager.exportReportToPdf(print);

                nomeDownload = "relatorio_movimentos_" + DataHoje.horaMinuto().replace(":", "") + ".pdf";

                SalvaArquivos sa = new SalvaArquivos(arquivo, nomeDownload, false);
                pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/relatorios");

                sa.salvaNaPasta(pathPasta);

                qnt = listaMovs.size();
            } catch (Exception erro) {
                System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
                return null;
            }
        } catch (Exception erro) {
            System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
            return null;
        }
        if (qnt > 0) {
            if (juridica.getId() != -1) {
                try {
                    List<Pessoa> p = new ArrayList();
                    p.add(juridica.getPessoa());

                    String[] ret = new String[2];
                    if (!reg.isEnviarEmailAnexo()) {
                        ret = EnviarEmail.EnviarEmailPersonalizado(reg,
                                p,
                                " <h5>Visualize seu relatório clicando no link abaixo</5><br /><br />"
                                + " <a href='" + reg.getUrlPath() + "/Sindical/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/relatorios/" + nomeDownload + "' target='_blank'>Clique aqui para abrir relatório</a><br />",
                                new ArrayList(),
                                "Envio de Relatório");
                    } else {
                        List<File> fls = new ArrayList<File>();
                        fls.add(new File(pathPasta + "/" + nomeDownload));

                        ret = EnviarEmail.EnviarEmailPersonalizado(reg,
                                p,
                                " <h5>Baixe seu relatório Anexado neste email</5><br /><br />",
                                fls,
                                "Envio de Relatório");
                    }

                    if (!ret[1].isEmpty()) {
                        msgEmail = ret[1];
                    } else {
                        msgEmail = ret[0];
                    }

                } catch (Exception e) {
                }
            } else {
                msgEmail = "Pesquisar uma Empresa para envio!";
            }
            FacesContext.getCurrentInstance().responseComplete();
        }
        return "relatorioMovimentos";
    }

    public List getListaCidadesBase() {
        if (listaCidadesBase.isEmpty()) {
            GrupoCidadesDB db = new GrupoCidadesDBToplink();
            List select = new ArrayList();
            select.addAll(db.pesquisaCidadesBase());
            for (int i = 0; i < select.size(); i++) {
                listaCidadesBase.add(new DataObject(false, ((Cidade) select.get(i))));
            }
        }
        return listaCidadesBase;
    }

    public void setListaCidadesBase(List listaCidadesBase) {
        this.listaCidadesBase = listaCidadesBase;
    }

    public void marcarCidadesBase() {
        for (int i = 0; i < listaCidadesBase.size(); i++) {
            ((DataObject) listaCidadesBase.get(i)).setArgumento0(chkCidadesBase);
        }
    }

    public List<SelectItem> getListaTipoRelatorios() {
        List<SelectItem> relatorios = new Vector<SelectItem>();
        int i = 0;
        RelatorioDao db = new RelatorioDao();
        List select = db.pesquisaTipoRelatorio(110);
        while (i < select.size()) {
            relatorios.add(new SelectItem(new Integer(i),
                    (String) ((Relatorios) select.get(i)).getNome(),
                    Integer.toString(((Relatorios) select.get(i)).getId())));
            i++;
        }
        return relatorios;
    }

    public List<SelectItem> getListaServico() {
        List<SelectItem> servicos = new Vector<SelectItem>();
        if (chkContribuicao) {
            int i = 0;
            List select = new Dao().list(new Servicos(), true);
            while (i < select.size()) {
                servicos.add(new SelectItem(new Integer(i),
                        (String) ((Servicos) select.get(i)).getDescricao(),
                        Integer.toString(((Servicos) select.get(i)).getId())));
                i++;
            }
        }
        return servicos;
    }

    public List<SelectItem> getListaTipoServico() {
        List<SelectItem> tipoServico = new Vector<SelectItem>();
        if (chkTipo) {
            int i = 0;
            TipoServicoDB db = new TipoServicoDBToplink();
            List select = db.pesquisaTodos();
            while (i < select.size()) {
                tipoServico.add(new SelectItem(new Integer(i),
                        (String) ((TipoServico) select.get(i)).getDescricao(),
                        Integer.toString(((TipoServico) select.get(i)).getId())));
                i++;
            }
        }
        return tipoServico;
    }

    public String getConverteNullString(Object object) {
        if (object == null) {
            return "";
        } else {
            return String.valueOf(object);
        }
    }

    public int getConverteNullInt(Object object) {
        if (object == null) {
            return 0;
        } else {
            return (Integer) object;
        }
    }

    public void carregarFolha(DataObject data) {

    }

    @Override
    public void carregarFolha() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void atualizaValorGrid(String tipo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String novo() {
        juridica = new Juridica();
        return null;
    }

    public int getIdRelatorios() {
        return idRelatorios;
    }

    public void setIdRelatorios(int idRelatorios) {
        this.idRelatorios = idRelatorios;
    }

    public String getComboCondicao() {
        return comboCondicao;
    }

    public void setComboCondicao(String comboCondicao) {
        this.comboCondicao = comboCondicao;
    }

    public String getComboGeradosPelaCaixa() {
        return comboGeradosPelaCaixa;
    }

    public void setComboGeradosPelaCaixa(String comboGeradosPelaCaixa) {
        this.comboGeradosPelaCaixa = comboGeradosPelaCaixa;
    }

    public boolean isChkTipo() {
        return chkTipo;
    }

    public void setChkTipo(boolean chkTipo) {
        this.chkTipo = chkTipo;
    }

    public boolean isChkData() {
        return chkData;
    }

    public void setChkData(boolean chkData) {
        this.chkData = chkData;
    }

    public boolean isChkContribuicao() {
        return chkContribuicao;
    }

    public void setChkContribuicao(boolean chkContribuicao) {
        this.chkContribuicao = chkContribuicao;
    }

    public boolean isChkEmpresa() {
        return chkEmpresa;
    }

    public void setChkEmpresa(boolean chkEmpresa) {
        this.chkEmpresa = chkEmpresa;
    }

    public int getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(int idServicos) {
        this.idServicos = idServicos;
    }

    public int getIdTipoServico() {
        return idTipoServico;
    }

    public void setIdTipoServico(int idTipoServico) {
        this.idTipoServico = idTipoServico;
    }

    public Juridica getJuridica() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa") != null) {
            juridica = (Juridica) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
        }
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }

    public String getTipoDataPesquisa() {
        return tipoDataPesquisa;
    }

    public void setTipoDataPesquisa(String tipoDataPesquisa) {
        this.tipoDataPesquisa = tipoDataPesquisa;
    }

    public String getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(String dataInicial) {
        this.dataInicial = dataInicial;
    }

    public String getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(String dataFinal) {
        this.dataFinal = dataFinal;
    }

    public String getDataRefInicial() {
        return dataRefInicial;
    }

    public void setDataRefInicial(String dataRefInicial) {
        this.dataRefInicial = dataRefInicial;
    }

    public String getDataRefFinal() {
        return dataRefFinal;
    }

    public void setDataRefFinal(String dataRefFinal) {
        this.dataRefFinal = dataRefFinal;
    }

    public boolean isChkPesquisaEmpresa() {
        return chkPesquisaEmpresa;
    }

    public void setChkPesquisaEmpresa(boolean chkPesquisaEmpresa) {
        this.chkPesquisaEmpresa = chkPesquisaEmpresa;
    }

    public String getRadioOrdem() {
        return radioOrdem;
    }

    public void setRadioOrdem(String radioOrdem) {
        this.radioOrdem = radioOrdem;
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public String getMsgEmail() {
        return msgEmail;
    }

    public void setMsgEmail(String msgEmail) {
        this.msgEmail = msgEmail;
    }

    public String getRelEmpresa() {
        return relEmpresa;
    }

    public void setRelEmpresa(String relEmpresa) {
        this.relEmpresa = relEmpresa;
    }

    public String getRenEnviarEmail() {
        if (chkEmpresa) {
            renEnviarEmail = "false";
        } else {
            renEnviarEmail = "true";
        }
        return renEnviarEmail;
    }

    public void setRenEnviarEmail(String renEnviarEmail) {
        this.renEnviarEmail = renEnviarEmail;
    }

    public boolean isChkConvencao() {
        return chkConvencao;
    }

    public void setChkConvencao(boolean chkConvencao) {
        this.chkConvencao = chkConvencao;
    }

    public int getIdConvencao() {
        return idConvencao;
    }

    public void setIdConvencao(int idConvencao) {
        this.idConvencao = idConvencao;
    }

    public int getIdGrupoCidade() {
        return idGrupoCidade;
    }

    public void setIdGrupoCidade(int idGrupoCidade) {
        this.idGrupoCidade = idGrupoCidade;
    }

    public boolean isChkCidadesBase() {
        return chkCidadesBase;
    }

    public void setChkCidadesBase(boolean chkCidadesBase) {
        this.chkCidadesBase = chkCidadesBase;
    }

    public String getPorContabilidade() {
        return porContabilidade;
    }

    public void setPorContabilidade(String porContabilidade) {
        this.porContabilidade = porContabilidade;
    }

    public boolean isChkContabilidades() {
        return chkContabilidades;
    }

    public void setChkContabilidades(boolean chkContabilidades) {
        this.chkContabilidades = chkContabilidades;
    }

    public List getListaContabilidades() {
        if (listaContabilidades.isEmpty()) {
            JuridicaDB db = new JuridicaDBToplink();
            List select = new ArrayList();
            select.addAll(db.pesquisaContabilidade());
            for (int i = 0; i < select.size(); i++) {
                listaContabilidades.add(new DataObject(false, ((Juridica) select.get(i))));
            }
        }
        return listaContabilidades;
    }

    public void setListaContabilidades(List listaContabilidades) {
        this.listaContabilidades = listaContabilidades;
    }

    public void marcarContabilidades() {
        for (int i = 0; i < listaContabilidades.size(); i++) {
            ((DataObject) listaContabilidades.get(i)).setArgumento0(chkContabilidades);
        }
    }

    public boolean isTotaliza() {
        return totaliza;
    }

    public void setTotaliza(boolean totaliza) {
        this.totaliza = totaliza;
    }
}
