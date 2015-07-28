package br.com.rtools.relatorios.beans;

import br.com.rtools.arrecadacao.CnaeConvencao;
import br.com.rtools.arrecadacao.Convencao;
import br.com.rtools.arrecadacao.ConvencaoCidade;
import br.com.rtools.arrecadacao.db.CnaeConvencaoDB;
import br.com.rtools.arrecadacao.db.CnaeConvencaoDBToplink;
import br.com.rtools.arrecadacao.db.GrupoCidadeDB;
import br.com.rtools.arrecadacao.db.GrupoCidadeDBToplink;
import br.com.rtools.arrecadacao.db.GrupoCidadesDB;
import br.com.rtools.arrecadacao.db.GrupoCidadesDBToplink;
import br.com.rtools.endereco.Cidade;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.TipoServico;
import br.com.rtools.financeiro.db.ServicoRotinaDB;
import br.com.rtools.financeiro.db.ServicoRotinaDBToplink;
import br.com.rtools.impressao.ParametroMovimentos;
import br.com.rtools.pessoa.Cnae;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.db.JuridicaDB;
import br.com.rtools.pessoa.db.JuridicaDBToplink;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.relatorios.db.RelatorioContribuintesDB;
import br.com.rtools.relatorios.db.RelatorioContribuintesDBToplink;
import br.com.rtools.relatorios.dao.RelatorioDao;
import br.com.rtools.relatorios.db.RelatorioMovimentosDB;
import br.com.rtools.relatorios.db.RelatorioMovimentosDBToplink;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.sistema.Email;
import br.com.rtools.sistema.EmailPessoa;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Download;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Mail;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvaArquivos;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@ManagedBean
@SessionScoped
public class RelatorioMovimentoBean implements Serializable {

    private int idRelatorios = 0;
    private int idServicos = 0;
    private int idTipoServico = 0;
    private int idConvencao = 0;
    private int idCnae = 0;
    private int idGrupoCidade = 0;
    private List<SelectItem> listaTipoRelatorio = new ArrayList<>();
    private List<SelectItem> listaServicos = new ArrayList<>();
    private List<SelectItem> listaTipoServico = new ArrayList<>();
    private List<SelectItem> listaConvencao = new ArrayList<>();
    private List<SelectItem> listaGrupoCidade = new ArrayList<>();
    private List<SelectItem> listaCnaes = new ArrayList<>();
    private String condicao = "todos";
    private String geradosPelaCaixa = "naoverificar";
    private String filtrar = "todas";
    private String tipoDataPesquisa = "vencimento";
    private String dataInicial = "";
    private String dataFinal = "";
    private String dataRefInicial = "";
    private String dataRefFinal = "";
    private boolean chkEmpresa = false;
    private boolean chkContabilidade = false;
    private boolean chkConvencao = false;
    private boolean chkServicos = false;
    private boolean chkTipoServico = false;
    private boolean chkCidadeBase = false;
    private boolean chkData = false;
    private boolean chkOrdemEmpresa = false;
    private boolean chkCnae = false;
    private boolean totaliza = false;
    private Juridica juridica = new Juridica();
    private List<Juridica> listaContabilidade = new ArrayList();
    private List<Juridica> listaContabilidadeSelecionada = new ArrayList();
    private final List<Juridica> listaPesquisa = new ArrayList();
    private String pesquisaContabil = "";
    private String radioContabil = "selecionado";
    private String radioOrdem = "vencimento";
    private List<Cidade> listaCidadesBase = new ArrayList();
    private List<Cidade> listaCidadesBaseSelecionado = new ArrayList();
    private CnaeConvencao[] cnaeConvencaoSelecionado = null;
    private List<CnaeConvencao> listaCnaeConvencaos = new ArrayList<CnaeConvencao>();

    public void porEmpresa() {
        chkEmpresa = (chkEmpresa != true);
        juridica = new Juridica();
    }

    public void porContabilidade() {
        chkContabilidade = (chkContabilidade != true);
        listaContabilidade.clear();
        listaContabilidadeSelecionada.clear();
    }

    public void porConvencao() {
        cnaeConvencaoSelecionado = null;
        chkCnae = false;
        chkConvencao = (chkConvencao != true);
    }

    public void porServicos() {
        chkServicos = (chkServicos != true);
    }

    public void porTipoServico() {
        chkTipoServico = (chkTipoServico != true);
    }

    public void porCidadeBase() {
        chkCidadeBase = (chkCidadeBase != true);
        listaCidadesBaseSelecionado.clear();
    }

    public void porData() {
        chkData = (chkData != true);
    }

    public void porCnae() {
        chkCnae = (chkCnae != true);
    }

    public void acaoPesquisaContabil() {
        if (listaPesquisa.isEmpty()) {
            listaPesquisa.addAll(listaContabilidade);
        }

        listaContabilidade.clear();

        if (pesquisaContabil.length() == 0) {
            listaContabilidade.addAll(listaPesquisa);
            return;
        }

        if (!listaPesquisa.isEmpty()) {
            List aux = new ArrayList();
            for (int i = 0; i < listaPesquisa.size(); i++) {
                if (listaPesquisa.get(i).getPessoa().getNome().toUpperCase().contains(pesquisaContabil.toUpperCase())) {
                    aux.add(listaPesquisa.get(i));
                } else if (listaPesquisa.get(i).getPessoa().getDocumento().contains(pesquisaContabil)) {
                    aux.add(listaPesquisa.get(i));
                }
            }

            if (!aux.isEmpty()) {
                listaContabilidade.addAll(aux);
            } else {
                listaContabilidade.addAll(listaPesquisa);
            }
        }

    }

    public boolean validaLista() {
        if (!chkCidadeBase && !chkContabilidade && !chkConvencao && !chkData && !chkEmpresa && !chkServicos && !chkTipoServico) {
            GenericaMensagem.warn("Validação", "Selecione pelo menos um filtro");
            return false;
        }
        if (filtrar.equals("todas")) {
            if (chkEmpresa && juridica.getId() == -1 && !chkData) {
                GenericaMensagem.warn("Validação", "Selecione uma empresa ou período");
                return false;
            } else if (chkData && dataInicial.isEmpty() && dataFinal.isEmpty() && dataRefInicial.isEmpty() && dataRefFinal.isEmpty()) {
                GenericaMensagem.warn("Validação", "Selecione um período válido!");
                return false;
            } else if (chkData && dataInicial.isEmpty() && dataRefInicial.isEmpty()) {
                GenericaMensagem.warn("Validação", "Selecione um período válido!");
                return false;
            }
        }

        if (chkContabilidade && listaContabilidadeSelecionada.isEmpty()) {
            return false;
        }

        return !(chkCidadeBase && listaCidadesBaseSelecionado.isEmpty());
    }

    public Collection listaPesquisa() {

        RelatorioMovimentosDB db_rel = new RelatorioMovimentosDBToplink();

        Juridica sindicato = (Juridica) (new Dao()).find(new Juridica(), 1);
        PessoaEndereco endSindicato = (new PessoaEnderecoDBToplink()).pesquisaEndPorPessoaTipo(sindicato.getId(), 3);

        Relatorios relatorio = (new RelatorioDao()).pesquisaRelatorios(Integer.parseInt(listaTipoRelatorio.get(idRelatorios).getDescription()));

        String idsEcs = "";
        if (radioContabil.equals("selecionado")) {
            for (int i = 0; i < listaContabilidadeSelecionada.size(); i++) {
                if (idsEcs.length() > 0 && i != listaContabilidadeSelecionada.size()) {
                    idsEcs += ",";
                }
                idsEcs += listaContabilidadeSelecionada.get(i).getId();
            }
        } else {
            idsEcs = radioContabil;
        }

        int id_convencao = 0, id_grupo = 0;
        if (chkConvencao) {
            id_convencao = Integer.valueOf(listaConvencao.get(idConvencao).getDescription());
            id_grupo = Integer.valueOf(listaGrupoCidade.get(idGrupoCidade).getDescription());
        }

        int id_servico = 0;
        if (chkServicos) {
            id_servico = Integer.valueOf(listaServicos.get(idServicos).getDescription());
        }

        int id_tipo_servico = 0;
        if (chkTipoServico) {
            id_tipo_servico = Integer.valueOf(listaTipoServico.get(idTipoServico).getDescription());
        }

        String cidade_base = "";
        for (int i = 0; i < listaCidadesBaseSelecionado.size(); i++) {
            if (cidade_base.length() > 0 && i != listaCidadesBaseSelecionado.size()) {
                cidade_base += ",";
            }
            cidade_base += listaCidadesBaseSelecionado.get(i).getId();
            if (cidade_base.length() == 1) {
                cidade_base = "0" + cidade_base;
            }
        }

        Date dtInicial = null, dtFinal = null;
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

        String inCnaes = "";
        if (cnaeConvencaoSelecionado != null) {
            for (int i = 0; i < cnaeConvencaoSelecionado.length; i++) {
                if (i == 0) {
                    inCnaes += "" + cnaeConvencaoSelecionado[i].getCnae().getId();
                } else {
                    inCnaes += ", " + cnaeConvencaoSelecionado[i].getCnae().getId();
                }
            }
        }

        List<Vector> result = db_rel.listaMovimentos(
                relatorio,
                condicao, // CONDIÇÃO -- todos -- ativos -- naoativos
                id_servico, // ID SERVICOS
                id_tipo_servico, // ID TIPO SERVICO
                (juridica.getId() != -1) ? juridica.getId() : 0, // ID JURIDICA -- EMPRESA
                chkData, // DATA
                tipoDataPesquisa, // TIPO DATA -- importacao -- recebimento -- vencimento -- referencia
                dtInicial, // DATA INICIAL
                dtFinal, // DATA FINAL
                dataRefInicial, // REFERENCIA INICIAL
                dataRefFinal, // REFERENCIA FINAL
                "vencimento", // ORDEM -- vencimento -- quitacao -- importacao -- referencia
                chkOrdemEmpresa, // ORDEM POR EMPRESA
                filtrar, // FILTRAR POR -- todas -- recebidas -- naorecebidas -- atrasadas
                "empresa", // CASO PESQUISA POR EMPRESA / OU CONTABILIDADE  -- nao usa mais isso
                id_convencao, // ID CONVENCAO
                id_grupo, // ID GRUPO CIDADE
                cidade_base, // IDS DAS CIDADES DA BASE SELECIONADAS
                idsEcs, // IDS DAS CONTABILIDADES SELECIONADAS
                inCnaes // CNAES
        );

        Collection listaParametro = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {

            float valor = Float.parseFloat(getConverteNullString(((Vector) result.get(i)).get(6))); // VALOR ORIGINAL     

            String quitacao = "", importacao = "", usuario = "";

            if (((Vector) result.get(i)).get(38) != null) {
                quitacao = DataHoje.converteData((Date) ((Vector) result.get(i)).get(39));
                importacao = DataHoje.converteData((Date) ((Vector) result.get(i)).get(40));
                usuario = getConverteNullString(((Vector) result.get(i)).get(42));
            }

            String srepasse = getConverteNullString(((Vector) result.get(i)).get(48));
            float repasse = 0;
            if (srepasse.isEmpty()) {
                repasse = Moeda.multiplicarValores(Float.parseFloat(getConverteNullString(((Vector) result.get(i)).get(47))), Moeda.divisaoValores(0, 100));
            } else {
                repasse = Moeda.multiplicarValores(Float.parseFloat(getConverteNullString(((Vector) result.get(i)).get(47))), Moeda.divisaoValores(Float.parseFloat(srepasse), 100));
            }

            float valorLiquido = Moeda.subtracaoValores(Moeda.subtracaoValores(Float.parseFloat(getConverteNullString(((Vector) result.get(i)).get(47))), Float.valueOf(Float.parseFloat(getConverteNullString(((Vector) result.get(i)).get(43))))), repasse);

            listaParametro.add(
                    new ParametroMovimentos(
                            ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
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
                            totaliza
                    )
            );
        }
        return listaParametro;
    }

    public void visualizar() {
        if (!validaLista()) {
            return;
        }
        Collection collection = listaPesquisa();
        if (collection.isEmpty()) {
            GenericaMensagem.warn("Sistema", "Nenhum registro encontrado!");
            return;
        }
        JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(collection);
        Relatorios relatorio = (new RelatorioDao()).pesquisaRelatorios(Integer.parseInt(listaTipoRelatorio.get(idRelatorios).getDescription()));
        GenericaMensagem.warn("Sucesso", "Relatório gerado!");
        try {
            JasperPrint print = JasperFillManager.fillReport(
                    (JasperReport) JRLoader.loadObject(new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(relatorio.getJasper()))),
                    null,
                    dtSource
            );

            byte[] arquivo = JasperExportManager.exportReportToPdf(print);

            String nomeDownload = "relatorio_movimentos_" + DataHoje.horaMinuto().replace(":", "") + ".pdf";
            SalvaArquivos sa = new SalvaArquivos(arquivo, nomeDownload, false);

            String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/relatorios");
            sa.salvaNaPasta(pathPasta);

            Download download = new Download(nomeDownload,
                    pathPasta,
                    "application/pdf",
                    FacesContext.getCurrentInstance());
            download.baixar();
        } catch (Exception e) {
            GenericaMensagem.warn("Sistema", e.getMessage());
        }
    }

    public void enviarEmail() {
        if (!validaLista()) {
            return;
        }

        Collection collection = listaPesquisa();

        if (collection.isEmpty()) {
            GenericaMensagem.warn("Erro", "Nenhum movimento foi encontrado nesses valores");
            return;
        }

        JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(collection);
        Relatorios relatorio = (new RelatorioDao()).pesquisaRelatorios(Integer.parseInt(listaTipoRelatorio.get(idRelatorios).getDescription()));

        String nomeDownload = "", pathPasta = "";
        try {
            JasperPrint print = JasperFillManager.fillReport(
                    (JasperReport) JRLoader.loadObject(new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(relatorio.getJasper()))),
                    null,
                    dtSource
            );

            byte[] arquivo = JasperExportManager.exportReportToPdf(print);

            nomeDownload = "relatorio_movimentos_" + DataHoje.horaMinuto().replace(":", "") + ".pdf";
            SalvaArquivos sa = new SalvaArquivos(arquivo, nomeDownload, false);

            pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/relatorios");
            sa.salvaNaPasta(pathPasta);
        } catch (Exception e) {

        }

        Registro registro = (Registro) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(1, "Registro");
        // ENVIO DE EMAIL PARA EMPRESA SELECIONADA
        if (chkEmpresa && juridica.getId() != -1) {
            try {
                List<Pessoa> pessoas = new ArrayList();
                pessoas.add(juridica.getPessoa());

                String mensagem = "";
                List<File> fls = new ArrayList<>();
                if (!registro.isEnviarEmailAnexo()) {
                    mensagem = " <h5>Visualize seu relatório clicando no link abaixo</5><br /><br />"
                            + " <a href='" + registro.getUrlPath() + "/Sindical/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/relatorios/" + nomeDownload + "' target='_blank'>Clique aqui para abrir relatório</a><br />";
//                    
//                    ret = EnviarEmail.EnviarEmailPersonalizado(registro,
//                            p,
//                            " <h5>Visualize seu relatório clicando no link abaixo</5><br /><br />"
//                            + " <a href='" + registro.getUrlPath() + "/Sindical/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/relatorios/" + nomeDownload + "' target='_blank'>Clique aqui para abrir relatório</a><br />",
//                            new ArrayList(),
//                            "Envio de Relatório");
                } else {

                    fls.add(new File(pathPasta + "/" + nomeDownload));
                    mensagem = "<h5>Baixe seu relatório Anexado neste email</5><br /><br />";
//                    ret = EnviarEmail.EnviarEmailPersonalizado(registro,
//                            p,
//                            " <h5>Baixe seu relatório Anexado neste email</5><br /><br />",
//                            fls,
//                            "Envio de Relatório");
                }

                DaoInterface di = new Dao();
                Mail mail = new Mail();
                mail.setFiles(fls);
                mail.setEmail(
                        new Email(
                                -1,
                                DataHoje.dataHoje(),
                                DataHoje.livre(new Date(), "HH:mm"),
                                (Usuario) GenericaSessao.getObject("sessaoUsuario"),
                                (Rotina) di.find(new Rotina(), 110),
                                null,
                                "Envio de Relatório",
                                mensagem,
                                false,
                                false
                        )
                );

                List<EmailPessoa> emailPessoas = new ArrayList<>();
                EmailPessoa emailPessoa = new EmailPessoa();
                for (Pessoa pe : pessoas) {
                    emailPessoa.setDestinatario(pe.getEmail1());
                    emailPessoa.setPessoa(pe);
                    emailPessoa.setRecebimento(null);
                    emailPessoas.add(emailPessoa);
                    mail.setEmailPessoas(emailPessoas);
                    emailPessoa = new EmailPessoa();
                }

                String[] retorno = mail.send("personalizado");

                if (!retorno[1].isEmpty()) {

                    GenericaMensagem.warn("Envio para EMPRESA", retorno[1]);
                } else {
                    GenericaMensagem.info("Envio para EMPRESA", retorno[0]);
                }
            } catch (Exception e) {
            }
        }
        // ENVIO DE EMAIL PARA CONTABILIDADE SELECIONADA

        if (chkContabilidade && !listaContabilidadeSelecionada.isEmpty()) {
            for (int i = 0; i < listaContabilidadeSelecionada.size(); i++) {
                try {
                    List<Pessoa> pessoas = new ArrayList();
                    pessoas.add(listaContabilidadeSelecionada.get(i).getPessoa());

                    String mensagem = "";
                    List<File> fls = new ArrayList<>();
                    if (!registro.isEnviarEmailAnexo()) {
                        mensagem = " <h5>Visualize seu relatório clicando no link abaixo</5><br /><br />"
                                + " <a href='" + registro.getUrlPath() + "/Sindical/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/relatorios/" + nomeDownload + "' target='_blank'>Clique aqui para abrir relatório</a><br />";
//                        ret = EnviarEmail.EnviarEmailPersonalizado(registro,
//                                p,
//                                " <h5>Visualize seu relatório clicando no link abaixo</5><br /><br />"
//                                + " <a href='" + registro.getUrlPath() + "/Sindical/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/relatorios/" + nomeDownload + "' target='_blank'>Clique aqui para abrir relatório</a><br />",
//                                new ArrayList(),
//                                "Envio de Relatório");
                    } else {
                        fls.add(new File(pathPasta + "/" + nomeDownload));
                        mensagem = "<h5>Baixe seu relatório Anexado neste email</5><br /><br />";

//                        ret = EnviarEmail.EnviarEmailPersonalizado(registro,
//                                p,
//                                " <h5>Baixe seu relatório Anexado neste email</5><br /><br />",
//                                fls,
//                                "Envio de Relatório");
                    }

                    DaoInterface di = new Dao();
                    Mail mail = new Mail();
                    mail.setFiles(fls);
                    mail.setEmail(
                            new Email(
                                    -1,
                                    DataHoje.dataHoje(),
                                    DataHoje.livre(new Date(), "HH:mm"),
                                    (Usuario) GenericaSessao.getObject("sessaoUsuario"),
                                    (Rotina) di.find(new Rotina(), 110),
                                    null,
                                    "Envio de Relatório",
                                    mensagem,
                                    false,
                                    false
                            )
                    );

                    List<EmailPessoa> emailPessoas = new ArrayList<>();
                    EmailPessoa emailPessoa = new EmailPessoa();
                    for (Pessoa pe : pessoas) {
                        emailPessoa.setDestinatario(pe.getEmail1());
                        emailPessoa.setPessoa(pe);
                        emailPessoa.setRecebimento(null);
                        emailPessoas.add(emailPessoa);
                        mail.setEmailPessoas(emailPessoas);
                        emailPessoa = new EmailPessoa();
                    }

                    String[] retorno = mail.send("personalizado");

                    if (!retorno[1].isEmpty()) {
                        GenericaMensagem.warn("Envio para CONTABILIDADE", retorno[1]);
                    } else {
                        GenericaMensagem.info("Envio para CONTABILIDADE", retorno[0]);
                    }

                } catch (Exception e) {
                }
            }
        }

    }

    public int getIdRelatorios() {
        return idRelatorios;
    }

    public void setIdRelatorios(int idRelatorios) {
        this.idRelatorios = idRelatorios;
    }

    public List<SelectItem> getListaTipoRelatorio() {
        if (listaTipoRelatorio.isEmpty()) {
            RelatorioDao db = new RelatorioDao();
            List select = db.pesquisaTipoRelatorio(110);
            for (int i = 0; i < select.size(); i++) {
                listaTipoRelatorio.add(new SelectItem(i,
                        (String) ((Relatorios) select.get(i)).getNome(),
                        Integer.toString(((Relatorios) select.get(i)).getId())));
            }
        }
        return listaTipoRelatorio;
    }

    public void setListaTipoRelatorio(List<SelectItem> listaTipoRelatorio) {
        this.listaTipoRelatorio = listaTipoRelatorio;
    }

    public String getCondicao() {
        return condicao;
    }

    public void setCondicao(String condicao) {
        this.condicao = condicao;
    }

    public String getGeradosPelaCaixa() {
        return geradosPelaCaixa;
    }

    public void setGeradosPelaCaixa(String geradosPelaCaixa) {
        this.geradosPelaCaixa = geradosPelaCaixa;
    }

    public String getFiltrar() {
        return filtrar;
    }

    public void setFiltrar(String filtrar) {
        this.filtrar = filtrar;
    }

    public boolean isChkEmpresa() {
        return chkEmpresa;
    }

    public void setChkEmpresa(boolean chkEmpresa) {
        this.chkEmpresa = chkEmpresa;
    }

    public boolean isChkContabilidade() {
        return chkContabilidade;
    }

    public void setChkContabilidade(boolean chkContabilidade) {
        this.chkContabilidade = chkContabilidade;
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

    public boolean isTotaliza() {
        return totaliza;
    }

    public void setTotaliza(boolean totaliza) {
        this.totaliza = totaliza;
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

    public void removerJuridica() {
        juridica = new Juridica();
    }

    public List getListaContabilidade() {
        if (listaContabilidade.isEmpty()) {
            JuridicaDB db = new JuridicaDBToplink();
            listaContabilidade.addAll(db.pesquisaContabilidade());
        }
        return listaContabilidade;
    }

    public void setListaContabilidade(List listaContabilidade) {
        this.listaContabilidade = listaContabilidade;
    }

    public String getPesquisaContabil() {
        return pesquisaContabil;
    }

    public void setPesquisaContabil(String pesquisaContabil) {
        this.pesquisaContabil = pesquisaContabil;
    }

    public List getListaContabilidadeSelecionada() {
        return listaContabilidadeSelecionada;
    }

    public void setListaContabilidadeSelecionada(List listaContabilidadeSelecionada) {
        this.listaContabilidadeSelecionada = listaContabilidadeSelecionada;
    }

    public String getRadioContabil() {
        return radioContabil;
    }

    public void setRadioContabil(String radioContabil) {
        this.radioContabil = radioContabil;
    }

    public boolean isChkConvencao() {
        return chkConvencao;
    }

    public void setChkConvencao(boolean chkConvencao) {
        this.chkConvencao = chkConvencao;
    }

    public List<SelectItem> getListaServicos() {
        if (listaServicos.isEmpty()) {
            ServicoRotinaDB srdb = new ServicoRotinaDBToplink();
            List<Servicos> select = srdb.pesquisaTodosServicosComRotinas(4);
            for (int i = 0; i < select.size(); i++) {
                listaServicos.add(
                        new SelectItem(
                                i,
                                select.get(i).getDescricao(),
                                Integer.toString(select.get(i).getId())
                        )
                );

            }
        }
        return listaServicos;
    }

    public void setListaServicos(List<SelectItem> listaServicos) {
        this.listaServicos = listaServicos;
    }

    public List<SelectItem> getListaConvencao() {
        if (listaConvencao.isEmpty()) {
            List<Convencao> select = (new SalvarAcumuladoDBToplink()).listaObjeto("Convencao");
            for (int i = 0; i < select.size(); i++) {
                listaConvencao.add(new SelectItem(
                        i,
                        select.get(i).getDescricao(),
                        Integer.toString(select.get(i).getId())
                ));
            }
        }
        return listaConvencao;
    }

    public void setListaConvencao(List<SelectItem> listaConvencao) {
        this.listaConvencao = listaConvencao;
    }

    public int getIdTipoServico() {
        return idTipoServico;
    }

    public void setIdTipoServico(int idTipoServico) {
        this.idTipoServico = idTipoServico;
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

    public int getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(int idServicos) {
        this.idServicos = idServicos;
    }

    public List<SelectItem> getListaTipoServico() {
        if (listaTipoServico.isEmpty()) {
            List<TipoServico> select = (new SalvarAcumuladoDBToplink()).listaObjeto("TipoServico");
            for (int i = 0; i < select.size(); i++) {
                listaTipoServico.add(new SelectItem(
                        i,
                        select.get(i).getDescricao(),
                        Integer.toString(select.get(i).getId()))
                );
            }
        }
        return listaTipoServico;
    }

    public void setListaTipoServico(List<SelectItem> listaTipoServico) {
        this.listaTipoServico = listaTipoServico;
    }

    public List<SelectItem> getListaGrupoCidade() {
        if (!listaConvencao.isEmpty()) {
            listaGrupoCidade.clear();
            GrupoCidadeDB db = new GrupoCidadeDBToplink();
            List<ConvencaoCidade> select = db.pesquisaGrupoPorConvencao(Integer.parseInt(getListaConvencao().get(idConvencao).getDescription()));
            for (int i = 0; i < select.size(); i++) {
                listaGrupoCidade.add(new SelectItem(
                        new Integer(i),
                        select.get(i).getGrupoCidade().getDescricao(),
                        Integer.toString(select.get(i).getGrupoCidade().getId()))
                );
            }
        }
        return listaGrupoCidade;
    }

    public void setListaGrupoCidade(List<SelectItem> listaGrupoCidade) {
        this.listaGrupoCidade = listaGrupoCidade;
    }

    public boolean isChkServicos() {
        return chkServicos;
    }

    public void setChkServicos(boolean chkServicos) {
        this.chkServicos = chkServicos;
    }

    public boolean isChkTipoServico() {
        return chkTipoServico;
    }

    public void setChkTipoServico(boolean chkTipoServico) {
        this.chkTipoServico = chkTipoServico;
    }

    public List<Cidade> getListaCidadesBase() {
        if (listaCidadesBase.isEmpty()) {
            GrupoCidadesDB db = new GrupoCidadesDBToplink();
            listaCidadesBase.addAll(db.pesquisaCidadesBase());
        }
        return listaCidadesBase;
    }

    public void setListaCidadesBase(List<Cidade> listaCidadesBase) {
        this.listaCidadesBase = listaCidadesBase;
    }

    public List<Cidade> getListaCidadesBaseSelecionado() {
        return listaCidadesBaseSelecionado;
    }

    public void setListaCidadesBaseSelecionado(List<Cidade> listaCidadesBaseSelecionado) {
        this.listaCidadesBaseSelecionado = listaCidadesBaseSelecionado;
    }

    public boolean isChkCidadeBase() {
        return chkCidadeBase;
    }

    public void setChkCidadeBase(boolean chkCidadeBase) {
        this.chkCidadeBase = chkCidadeBase;
    }

    public boolean isChkData() {
        return chkData;
    }

    public void setChkData(boolean chkData) {
        this.chkData = chkData;
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

    public String getRadioOrdem() {
        return radioOrdem;
    }

    public void setRadioOrdem(String radioOrdem) {
        this.radioOrdem = radioOrdem;
    }

    public boolean isChkOrdemEmpresa() {
        return chkOrdemEmpresa;
    }

    public void setChkOrdemEmpresa(boolean chkOrdemEmpresa) {
        this.chkOrdemEmpresa = chkOrdemEmpresa;
    }

    public boolean isChkCnae() {
        return chkCnae;
    }

    public void setChkCnae(boolean chkCnae) {
        this.chkCnae = chkCnae;
    }

    public int getIdCnae() {
        return idCnae;
    }

    public void setIdCnae(int idCnae) {
        this.idCnae = idCnae;
    }

    public List<SelectItem> getListaCnaes() {
        listaCnaes.clear();
        CnaeConvencaoDB ccdb = new CnaeConvencaoDBToplink();
        List<Cnae> select = ccdb.listaCnaePorConvencao(Integer.parseInt(listaConvencao.get(idConvencao).getDescription()));
        for (int i = 0; i < select.size(); i++) {
            listaCnaes.add(new SelectItem(
                    i,
                    select.get(i).getCnae() + " - " + select.get(i).getCnae(),
                    Integer.toString(select.get(i).getId()))
            );
        }
        return listaCnaes;
    }

    public void setListaCnaes(List<SelectItem> listaCnaes) {
        this.listaCnaes = listaCnaes;
    }

    public CnaeConvencao[] getCnaeConvencaoSelecionado() {
        return cnaeConvencaoSelecionado;
    }

    public void setCnaeConvencaoSelecionado(CnaeConvencao[] cnaeConvencaoSelecionado) {
        this.cnaeConvencaoSelecionado = cnaeConvencaoSelecionado;
    }

    public List<CnaeConvencao> getListaCnaeConvencaos() {
        if (!listaConvencao.isEmpty()) {
            listaCnaeConvencaos.clear();
            int i = 0;
            String ids = "";
            RelatorioContribuintesDB db = new RelatorioContribuintesDBToplink();
            listaCnaeConvencaos = db.pesquisarCnaeConvencaoPorConvencao(listaConvencao.get(idConvencao).getDescription());
        }
        return listaCnaeConvencaos;
    }

    public void setListaCnaeConvencaos(List<CnaeConvencao> listaCnaeConvencaos) {
        this.listaCnaeConvencaos = listaCnaeConvencaos;
    }
}
