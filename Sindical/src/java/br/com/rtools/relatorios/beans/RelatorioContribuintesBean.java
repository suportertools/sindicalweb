package br.com.rtools.relatorios.beans;

import br.com.rtools.arrecadacao.CnaeConvencao;
import br.com.rtools.arrecadacao.Convencao;
import br.com.rtools.arrecadacao.GrupoCidade;
import br.com.rtools.endereco.Bairro;
import br.com.rtools.endereco.Cidade;
import br.com.rtools.impressao.ParametroContribuintes;
import br.com.rtools.pessoa.CentroComercial;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.TipoEndereco;
import br.com.rtools.pessoa.db.PessoaEnderecoDB;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.relatorios.db.RelatorioContribuintesDB;
import br.com.rtools.relatorios.db.RelatorioContribuintesDBToplink;
import br.com.rtools.relatorios.db.RelatorioGenericoDB;
import br.com.rtools.relatorios.db.RelatorioGenericoDBToplink;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.Jasper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;

@ManagedBean
@SessionScoped
public class RelatorioContribuintesBean implements Serializable {

    private String radioOrdem;
    private String radioCidades;
    private String radioEscritorios;
    private String radioCentroComercial;
    private String dataCadastroInicial;
    private String dataCadastroFinal;
//    private String comboCondicao = "contribuintes";
    private String comboCondicao;
    private boolean chkConvencao;
    private boolean chkConvencaoPesquisa;
    private boolean chkCnaeConvencao;
    private boolean chkGrupo;
    private boolean carregaConvencao;
    private boolean carregaCnaeConvencao;
    private boolean carregaGrupo;
    private Relatorios relatorios;
    private String idEmails;
    private int idCidades;
    private int idRelatorios;
    private int idContabilidade;
    private int idTipoEndereco;
    private List resultConvencao;
    private List resultCnaeConvencao;
    private List<DataObject> listaCentroComercial;
    private List<DataObject> listaGrupo;
    private Bairro bairro;
    private List<Convencao> listaConvencaos;
    private Convencao[] convencaoSelecionada;
    private List<GrupoCidade> listaGrupoCidades;
    private GrupoCidade[] grupoCidadeSelecionada;
    private List<CentroComercial> listaCentrosComerciais;
    private CentroComercial[] centroComercialSelecionado;
    private List<CnaeConvencao> listaCnaeConvencaos;
    private CnaeConvencao[] cnaeConvencaoSelecionado;
    List<SelectItem> listaTipoRelatorios;
    List<SelectItem> listaTipoEndereco;

    @PostConstruct
    public void init() {
        radioOrdem = "razao";
        radioCidades = "todas";
        radioEscritorios = "todos";
        radioCentroComercial = "nenhum";
        dataCadastroInicial = "";
        dataCadastroFinal = "";
        //    private String comboCondicao = "contribuintes";
        comboCondicao = "ativos";
        chkConvencao = true;
        chkConvencaoPesquisa = false;
        chkCnaeConvencao = true;
        chkGrupo = false;
        carregaConvencao = true;
        carregaCnaeConvencao = true;
        carregaGrupo = true;
        idEmails = "t";
        idCidades = 0;
        idRelatorios = 0;
        idContabilidade = 0;
        idTipoEndereco = 0;
        resultConvencao = new ArrayList();
        resultCnaeConvencao = new ArrayList();
        listaCentroComercial = new ArrayList();
        listaGrupo = new ArrayList();
        bairro = new Bairro();
        listaConvencaos = new ArrayList<>();
        convencaoSelecionada = null;
        listaGrupoCidades = new ArrayList<>();
        grupoCidadeSelecionada = null;
        listaCentrosComerciais = new ArrayList<>();
        centroComercialSelecionado = null;
        listaCnaeConvencaos = new ArrayList<>();
        cnaeConvencaoSelecionado = null;
        listaTipoRelatorios = new ArrayList<>();
        listaTipoEndereco = new ArrayList<>();
    }

    public void visualizar() {
        String escritorio = "";
        String centros = "",
                enderecos = "",
                numeros = "";
        String bairros = "";

        RelatorioGenericoDB db = new RelatorioGenericoDBToplink();
        RelatorioContribuintesDB dbContri = new RelatorioContribuintesDBToplink();
        PessoaEnderecoDB dbPesEnd = new PessoaEnderecoDBToplink();
        Cidade cidade;
        Juridica contabilidade;
        Relatorios relatorios = db.pesquisaRelatorios(Integer.parseInt(listaTipoRelatorios.get(idRelatorios).getDescription()));
        TipoEndereco tipoEndereco = (TipoEndereco) new Dao().find(new TipoEndereco(), Integer.parseInt(listaTipoEndereco.get(idTipoEndereco).getDescription()));
        // CONDICAO DO RELATORIO -----------------------------------------------------------
        String condicao = comboCondicao;

        // ESCRITORIO DO RELATORIO -----------------------------------------------------------
        switch (radioEscritorios) {
            case "todos":
                escritorio = "todos";
                break;
            case "semEscritorio":
                escritorio = "semEscritorio";
                break;
            case "comEscritorio":
                escritorio = "comEscritorio";
                break;
            case "especifico":
                contabilidade = (Juridica) new Dao().find(new Juridica(), Integer.parseInt(getListaContabilidades().get(idContabilidade).getDescription()));
                escritorio = Integer.toString(contabilidade.getId());
                break;
        }

        // CIDADE DO RELATORIO -----------------------------------------------------------
        String cidades = "";
        switch (radioCidades) {
            case "especificas":
                cidade = (Cidade) new Dao().find(new Cidade(), Integer.parseInt(getListaCidades().get(idCidades).getDescription()));
                cidades = Integer.toString(cidade.getId());
                break;
            case "local":
                cidade = dbPesEnd.pesquisaEndPorPessoaTipo(1, 2).getEndereco().getCidade();
                cidades = Integer.toString(cidade.getId());
                break;
            case "outras":
                cidade = dbPesEnd.pesquisaEndPorPessoaTipo(1, 2).getEndereco().getCidade();
                cidades = Integer.toString(cidade.getId());
                break;
        }

        // BAIRRO DO RELATORIO -----------------------------------------------------------
        if (bairro.getId() != -1) {
            List<Bairro> listaBairro = new ArrayList();
            listaBairro.add((Bairro) new Dao().find(bairro));
            if (!listaBairro.isEmpty()) {
                for (int i = 0; i < listaBairro.size(); i++) {
                    if (bairros.length() > 0 && i != listaBairro.size()) {
                        bairros += ",";
                    }
                    bairros += Integer.toString(listaBairro.get(i).getId());
                }
            }
        }

        // CENTRO COMERCIAL -----------------------------------------------------------
        @SuppressWarnings("UseOfObsoleteCollectionType")
        List idsEnderecos;
        if (!radioCentroComercial.equals("nenhum")) {
            String centroComercialSelecionados = "";
            if (centroComercialSelecionado != null && centroComercialSelecionado.length > 0) {
                int icc = 0;
                for (CentroComercial centroComercial : centroComercialSelecionado) {
                    if (icc == 0) {
                        centroComercialSelecionados = Integer.toString(centroComercial.getJuridica().getId());
                    } else {
                        centroComercialSelecionados += "," + Integer.toString(centroComercial.getJuridica().getId());
                    }
                    icc++;
                }
                idsEnderecos = dbContri.listaCentros(centroComercialSelecionados);
                for (int i = 0; i < idsEnderecos.size(); i++) {
                    if (enderecos.length() > 0 && i != idsEnderecos.size()) {
                        enderecos += ",";
                        numeros += ",";
                    }
                    //if (radioCentroComercial.equals("com")){
                    enderecos += ((List) idsEnderecos.get(i)).get(0);
                    numeros += "'" + ((List) idsEnderecos.get(i)).get(1) + "'";
                    //} else
                    //    enderecos += idsEnderecos.get(i).get(2);
                }
            } else {
                GenericaMensagem.warn("Sistema", "Selecione pelo menos um centro comercial.");
                return;
            }
        }

        // CONVENCAO GRUPO CIDADE -------------------------------------------------------
        String gruposCidadesSelecionados = "";
        if (grupoCidadeSelecionada != null) {
            int icc = 0;
            for (GrupoCidade grupoCidade : grupoCidadeSelecionada) {
                if (icc == 0) {
                    gruposCidadesSelecionados = Integer.toString(grupoCidade.getId());
                } else {
                    gruposCidadesSelecionados += "," + Integer.toString(grupoCidade.getId());
                }
                icc++;
            }
        }
        // CNAES DO RELATORIO -----------------------------------------------------------
        String cnaeConvencaoSelecionados = "";
        if (cnaeConvencaoSelecionado != null) {
            int icc = 0;
            for (CnaeConvencao cnaeConvencao : cnaeConvencaoSelecionado) {
                if (icc == 0) {
                    cnaeConvencaoSelecionados = Integer.toString(cnaeConvencao.getCnae().getId());
                } else {
                    cnaeConvencaoSelecionados += "," + Integer.toString(cnaeConvencao.getCnae().getId());
                }
                icc++;
            }
        }

        // CONVENÇÕES DO RELATORIO -----------------------------------------------------------
        String convencoesSelecionadas = "";
        if (convencaoSelecionada != null) {
            int ic = 0;
            for (Convencao convencao : convencaoSelecionada) {
                if (ic == 0) {
                    convencoesSelecionadas = Integer.toString(convencao.getId());
                } else {
                    convencoesSelecionadas += "," + Integer.toString(convencao.getId());
                }
                ic++;
            }
        }
        String inCentroComercial = "";
        Juridica sindicato = (Juridica) new Dao().find(new Juridica(), 1);
        PessoaEndereco endSindicato = dbPesEnd.pesquisaEndPorPessoaTipo(1, 3);
        @SuppressWarnings("UseOfObsoleteCollectionType")
        List<List> result = new ArrayList();
        if (convencaoSelecionada != null) {
            result = dbContri.listaRelatorioContribuintes(relatorios, idEmails, condicao, escritorio, radioCidades, cidades, radioOrdem, cnaeConvencaoSelecionados, tipoEndereco.getId(), enderecos, radioCentroComercial, inCentroComercial, numeros, gruposCidadesSelecionados, bairros, convencoesSelecionadas, dataCadastroInicial, dataCadastroFinal);
        }
        if (result.isEmpty()) {
            GenericaMensagem.info("Sistema", "Não existem registros para o relatório selecionado");
            return;
        }
        FacesContext faces = FacesContext.getCurrentInstance();
        List<ParametroContribuintes> list = new ArrayList<>();
        try {
            for (int i = 0; i < result.size(); i++) {
                list.add(new ParametroContribuintes(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
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
                        getConverteNullInt((result.get(i)).get(0)), // ID
                        getConverteNullString((result.get(i)).get(1)), // NOME PESSOA
                        getConverteNullString((result.get(i)).get(4)), // DESCRICAO ENDERECO
                        getConverteNullString((result.get(i)).get(3)), // LOGRADOURO
                        getConverteNullString((result.get(i)).get(7)), // NUMERO
                        getConverteNullString((result.get(i)).get(8)), // COMPLEMENTO
                        getConverteNullString((result.get(i)).get(11)), // BAIRRO
                        getConverteNullString((result.get(i)).get(9)), // CEP
                        getConverteNullString((result.get(i)).get(5)), // CIDADE
                        getConverteNullString((result.get(i)).get(6)), // UF
                        getConverteNullString((result.get(i)).get(12)), // TELEFONE
                        getConverteNullString((result.get(i)).get(13)), // EMAIL
                        getConverteNullString((result.get(i)).get(14)), // TIPO DOCUMENTO
                        getConverteNullString((result.get(i)).get(2)), // DOCUMENTO
                        getConverteNullInt((result.get(i)).get(15)), //ID CNAE
                        getConverteNullString((result.get(i)).get(16)), // NUMERO CNAE
                        getConverteNullString((result.get(i)).get(17)), // DESCRICAO CNAE
                        getConverteNullInt((result.get(i)).get(18)), // ID CONTABILIDADE
                        getConverteNullString((result.get(i)).get(10)), // NOME CONTABILIDADE
                        getConverteNullString((result.get(i)).get(20)), // DESCRICAO ENDERECO CONTABILIDADE
                        getConverteNullString((result.get(i)).get(19)), // LOGRADOURO CONTABILIDADE
                        getConverteNullString((result.get(i)).get(24)), // NUMERO CONTABILIDADE
                        getConverteNullString((result.get(i)).get(25)), // COMPLEMENTO CONTABILIDADE
                        getConverteNullString((result.get(i)).get(21)), // BAIRRO CONTABILIDADE
                        getConverteNullString((result.get(i)).get(26)), // CEP CONTABILIDADE
                        getConverteNullString((result.get(i)).get(22)), // CIDADE CONTABILIDADE
                        getConverteNullString((result.get(i)).get(23)), // UF CONTABILIDADE
                        getConverteNullString((result.get(i)).get(27)), // TELEFONE CONTABILIDADE
                        getConverteNullString((result.get(i)).get(28)) // EMAIL CONTABILIDADE
                ));
            }
            Jasper.printReports(relatorios.getJasper(), relatorios.getNome(), (Collection) list);
//                JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(listaContrs);
//                JasperPrint print = JasperFillManager.fillReport(
//                        jasper,
//                        null,
//                        dtSource);
//                byte[] arquivo = JasperExportManager.exportReportToPdf(print);
//
//                String nomeDownload = "relatorio_contribuintes_" + DataHoje.horaMinuto().replace(":", "") + ".pdf";
//                SalvaArquivos sa = new SalvaArquivos(arquivo, nomeDownload, false);
//                String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/relatorios");
//                sa.salvaNaPasta(pathPasta);
//                Download download = new Download(nomeDownload, pathPasta, "application/pdf", FacesContext.getCurrentInstance());
//                download.baixar();
//                download.remover();
        } catch (Exception erro) {
            GenericaMensagem.info("Sistema", "O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
            System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
        }
    }

    public Object getConverterNull(Object object) {
        return object;
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

    public List<SelectItem> getListaCidades() {
        List<SelectItem> cidades = new Vector<SelectItem>();
        int i = 0;
        RelatorioGenericoDB db = new RelatorioGenericoDBToplink();
        List select = db.pesquisaCidadesRelatorio();
        while (i < select.size()) {
            cidades.add(new SelectItem(new Integer(i),
                    (String) ((Cidade) select.get(i)).getCidade(),
                    Integer.toString(((Cidade) select.get(i)).getId())));
            i++;
        }
        return cidades;
    }

    public List<SelectItem> getListaTipoRelatorios() {
        if (listaTipoRelatorios.isEmpty()) {
            RelatorioGenericoDB db = new RelatorioGenericoDBToplink();
            List<Relatorios> list = (List<Relatorios>) db.pesquisaTipoRelatorio(5);
            for (int i = 0; i < list.size(); i++) {
                listaTipoRelatorios.add(new SelectItem(new Integer(i), list.get(i).getNome(), Integer.toString(list.get(i).getId())));
            }
        }
        return listaTipoRelatorios;
    }

    public List<SelectItem> getListaTipoEndereco() {
        if (listaTipoEndereco.isEmpty()) {
            List<TipoEndereco> list = (List<TipoEndereco>) new Dao().find("TipoEndereco", new int[]{2, 3, 4, 5});
            for (int i = 0; i < list.size(); i++) {
                listaTipoEndereco.add(new SelectItem(i, list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
            }
        }
        return listaTipoEndereco;
    }

    public List<SelectItem> getListaContabilidades() {
        List<SelectItem> contabilidades = new ArrayList<>();
        int i = 0;
        RelatorioContribuintesDB db = new RelatorioContribuintesDBToplink();
        List select = db.pesquisaContabilidades();
        while (i < select.size()) {
            contabilidades.add(new SelectItem(i,
                    (String) ((Juridica) select.get(i)).getPessoa().getNome() + " - "
                    + (String) ((Juridica) select.get(i)).getPessoa().getDocumento(),
                    Integer.toString(((Juridica) select.get(i)).getId())));
            i++;
        }
        return contabilidades;
    }

    public List getListaConvencoesx() {
        if (carregaConvencao) {
            resultConvencao = new ArrayList();
            List list = new Dao().list(new Convencao(), true);
            DataObject dtObject;
            for (int i = 0; i < list.size(); i++) {
                dtObject = new DataObject(true, ((Convencao) (list.get(i))));
                resultConvencao.add(dtObject);
            }
            carregaConvencao = false;
        }
        return resultConvencao;
    }

    public List getListaCnaeConvencaox() {
        if (carregaCnaeConvencao) {
            RelatorioContribuintesDB db = new RelatorioContribuintesDBToplink();
            resultCnaeConvencao = new ArrayList();
            DataObject dtObject;
            String ids = "";
            for (int i = 0; i < resultConvencao.size(); i++) {
                if ((Boolean) ((DataObject) resultConvencao.get(i)).getArgumento0() == true) {
                    if (ids.length() > 0 && i != resultConvencao.size()) {
                        ids += ",";
                    }
                    ids += String.valueOf(((Convencao) ((DataObject) resultConvencao.get(i)).getArgumento1()).getId());
                }
            }
            List listCnae = db.pesquisarCnaeConvencaoPorConvencao(ids);
            for (int x = 0; x < listCnae.size(); x++) {
                dtObject = new DataObject(true, ((CnaeConvencao) (listCnae.get(x))));
                resultCnaeConvencao.add(dtObject);
            }
            carregaCnaeConvencao = false;
        }
        return resultCnaeConvencao;
    }

    public String marcaTodos() {
        if (chkConvencao) {
            for (int i = 0; i < resultConvencao.size(); i++) {
                ((DataObject) resultConvencao.get(i)).setArgumento0(true);
            }
        } else {
            for (int i = 0; i < resultConvencao.size(); i++) {
                ((DataObject) resultConvencao.get(i)).setArgumento0(false);
            }
        }

        carregaGrupo = true;
        carregaCnaeConvencao = true;
        return "relatorioContribuintes";
    }

    public String marcaTodosCnae() {
        if (chkCnaeConvencao) {
            for (int i = 0; i < resultCnaeConvencao.size(); i++) {
                ((DataObject) resultCnaeConvencao.get(i)).setArgumento0(true);
            }
        } else {
            for (int i = 0; i < resultCnaeConvencao.size(); i++) {
                ((DataObject) resultCnaeConvencao.get(i)).setArgumento0(false);
            }
        }
        return "relatorioContribuintes";
    }

    public String marcaTodosGrupo() {
        if (chkGrupo) {
            for (int i = 0; i < listaGrupo.size(); i++) {
                ((DataObject) listaGrupo.get(i)).setArgumento0(true);
            }
        } else {
            for (int i = 0; i < listaGrupo.size(); i++) {
                ((DataObject) listaGrupo.get(i)).setArgumento0(false);
            }
        }
        return "relatorioContribuintes";
    }

    public void refreshForm() {
    }

    public String atualizaPG() {
        return "relatorioContribuintes";
    }

    public String atualizaPGOrdem() {
        RelatorioGenericoDB db = new RelatorioGenericoDBToplink();
        Relatorios relatorios = db.pesquisaRelatorios(Integer.parseInt(getListaTipoRelatorios().get(idRelatorios).getDescription()));
        if (relatorios.getJasper().equals("/Relatorios/CONTRIBUINTESPORESCRITORIO.jasper")) {
            radioEscritorios = "comEscritorio";
            radioOrdem = "escritorio";
            radioCidades = "todas";
        } else {
            radioEscritorios = "todos";
            radioOrdem = "razao";
            radioCidades = "todas";
        }
        return "relatorioContribuintes";
    }

    public String atualizaGridCnaeConv() {
        carregaCnaeConvencao = true;
        carregaGrupo = true;
        return "relatorioContribuintes";
    }

    public String getRadioOrdem() {
        return radioOrdem;
    }

    public void setRadioOrdem(String radioOrdem) {
        this.radioOrdem = radioOrdem;
    }

    public String getRadioCidades() {
        return radioCidades;
    }

    public void setRadioCidades(String radioCidades) {
        this.radioCidades = radioCidades;
    }

    public int getIdCidades() {
        return idCidades;
    }

    public void setIdCidades(int idCidades) {
        this.idCidades = idCidades;
    }

    public boolean isChkConvencao() {
        return chkConvencao;
    }

    public void setChkConvencao(boolean chkConvencao) {
        this.chkConvencao = chkConvencao;
    }

    public boolean isChkCnaeConvencao() {
        return chkCnaeConvencao;
    }

    public void setChkCnaeConvencao(boolean chkCnaeConvencao) {
        this.chkCnaeConvencao = chkCnaeConvencao;
    }

    public int getIdRelatorios() {
        return idRelatorios;
    }

    public void setIdRelatorios(int idRelatorios) {
        this.idRelatorios = idRelatorios;
    }

    public int getIdContabilidade() {
        return idContabilidade;
    }

    public void setIdContabilidade(int idContabilidade) {
        this.idContabilidade = idContabilidade;
    }

    public String getRadioEscritorios() {
        return radioEscritorios;
    }

    public void setRadioEscritorios(String radioEscritorios) {
        this.radioEscritorios = radioEscritorios;
    }

    public String getComboCondicao() {
        return comboCondicao;
    }

    public void setComboCondicao(String comboCondicao) {
        this.comboCondicao = comboCondicao;
    }

    public int getIdTipoEndereco() {
        return idTipoEndereco;
    }

    public void setIdTipoEndereco(int idTipoEndereco) {
        this.idTipoEndereco = idTipoEndereco;
    }

    public String getIdEmails() {
        return idEmails;
    }

    public void setIdEmails(String idEmails) {
        this.idEmails = idEmails;
    }

    public String getRadioCentroComercial() {
        return radioCentroComercial;
    }

    public void setRadioCentroComercial(String radioCentroComercial) {
        this.radioCentroComercial = radioCentroComercial;
        if (radioCentroComercial.equals("sem")) {
            getListaCentrosComerciais();
            centroComercialSelecionado = new CentroComercial[listaCentrosComerciais.size()];
            for (int i = 0; i < listaCentrosComerciais.size(); i++) {
                centroComercialSelecionado[i] = listaCentrosComerciais.get(i);
            }
        }
        if (radioCentroComercial.equals("com")) {
            idTipoEndereco = 3;
        }
    }

    public List<DataObject> getListaCentroComercialx() {
        if (listaCentroComercial.isEmpty() && !radioCentroComercial.equals("nenhum")) {
            List list = (List<CentroComercial>) new Dao().list(new CentroComercial(), true);
            for (int i = 0; i < list.size(); i++) {
                listaCentroComercial.add(new DataObject(false, list.get(i)));
            }
        } else if (radioCentroComercial.equals("nenhum")) {
            listaCentroComercial.clear();
        }
        return listaCentroComercial;
    }

    public void setListaCentroComercial(List<DataObject> listaCentroComercial) {
        this.listaCentroComercial = listaCentroComercial;
    }

    public boolean isChkConvencaoPesquisa() {
        return chkConvencaoPesquisa;
    }

    public void setChkConvencaoPesquisa(boolean chkConvencaoPesquisa) {
        this.chkConvencaoPesquisa = chkConvencaoPesquisa;
    }

    public List<DataObject> getListaGrupox() {
        if (carregaGrupo) {
            RelatorioContribuintesDB db = new RelatorioContribuintesDBToplink();
            listaGrupo = new ArrayList();
            String ids = "";
            for (int i = 0; i < resultConvencao.size(); i++) {
                if ((Boolean) ((DataObject) resultConvencao.get(i)).getArgumento0() == true) {
                    if (ids.length() > 0 && i != resultConvencao.size()) {
                        ids += ",";
                    }
                    ids += String.valueOf(((Convencao) ((DataObject) resultConvencao.get(i)).getArgumento1()).getId());
                }
            }
            List<GrupoCidade> listGrupo = db.pesquisarGrupoPorConvencao(ids);
            for (int x = 0; x < listGrupo.size(); x++) {
                listaGrupo.add(new DataObject(true, (listGrupo.get(x))));
            }
            carregaGrupo = false;
        }
        return listaGrupo;
    }

    public void setListaGrupo(List<DataObject> listaGrupo) {
        this.listaGrupo = listaGrupo;
    }

    public boolean isChkGrupo() {
        return chkGrupo;
    }

    public void setChkGrupo(boolean chkGrupo) {
        this.chkGrupo = chkGrupo;
    }

    public Bairro getBairro() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("simplesPesquisa") != null) {
            bairro = (Bairro) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("simplesPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("simplesPesquisa");
        }
        return bairro;
    }

    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
    }

    public List<Convencao> getListaConvencaos() {
        if (listaConvencaos.isEmpty()) {
            listaConvencaos = (List<Convencao>) new Dao().list(new Convencao());
        }
        return listaConvencaos;
    }

    public void setListaConvencaos(List<Convencao> listaConvencaos) {
        this.listaConvencaos = listaConvencaos;
    }

    public Convencao[] getConvencaoSelecionada() {
        return convencaoSelecionada;
    }

    public void setConvencaoSelecionada(Convencao[] convencaoSelecionada) {
        this.convencaoSelecionada = convencaoSelecionada;
    }

    public List<GrupoCidade> getListaGrupoCidades() {
        listaGrupoCidades.clear();
        int i = 0;
        String ids = "";
        if (convencaoSelecionada != null) {
            for (Convencao convencao : convencaoSelecionada) {
                if (i == 0) {
                    ids = Integer.toString(convencao.getId());
                } else {
                    ids += "," + Integer.toString(convencao.getId());
                }
                i++;
            }
            RelatorioContribuintesDB db = new RelatorioContribuintesDBToplink();
            listaGrupoCidades = db.pesquisarGrupoPorConvencao(ids);
        }
        return listaGrupoCidades;
    }

    public void setListaGrupoCidades(List<GrupoCidade> listaGrupoCidades) {
        this.listaGrupoCidades = listaGrupoCidades;
    }

    public GrupoCidade[] getGrupoCidadeSelecionada() {
        return grupoCidadeSelecionada;
    }

    public void setGrupoCidadeSelecionada(GrupoCidade[] grupoCidadeSelecionada) {
        this.grupoCidadeSelecionada = grupoCidadeSelecionada;
    }

    public List<CentroComercial> getListaCentrosComerciais() {
        if (listaCentrosComerciais.isEmpty() && !radioCentroComercial.equals("nenhum")) {
            listaCentrosComerciais = (List<CentroComercial>) new Dao().list(new CentroComercial(), true);
        } else if (radioCentroComercial.equals("nenhum")) {
            listaCentrosComerciais.clear();
        }
        return listaCentrosComerciais;
    }

    public void setListaCentrosComerciais(List<CentroComercial> listaCentrosComerciais) {
        this.listaCentrosComerciais = listaCentrosComerciais;
    }

    public CentroComercial[] getCentroComercialSelecionado() {
        return centroComercialSelecionado;
    }

    public void setCentroComercialSelecionado(CentroComercial[] centroComercialSelecionado) {
        this.centroComercialSelecionado = centroComercialSelecionado;
    }

    public List<CnaeConvencao> getListaCnaeConvencaos() {
        listaCnaeConvencaos.clear();
        int i = 0;
        String ids = "";
        if (convencaoSelecionada != null) {
            for (Convencao convencao : convencaoSelecionada) {
                if (i == 0) {
                    ids = Integer.toString(convencao.getId());
                } else {
                    ids += "," + Integer.toString(convencao.getId());
                }
                i++;
            }
            RelatorioContribuintesDB db = new RelatorioContribuintesDBToplink();
            listaCnaeConvencaos = db.pesquisarCnaeConvencaoPorConvencao(ids);
        }
        return listaCnaeConvencaos;
    }

    public void setListaCnaeConvencaos(List<CnaeConvencao> listaCnaeConvencaos) {
        this.listaCnaeConvencaos = listaCnaeConvencaos;
    }

    public CnaeConvencao[] getCnaeConvencaoSelecionado() {
        return cnaeConvencaoSelecionado;
    }

    public void setCnaeConvencaoSelecionado(CnaeConvencao[] cnaeConvencaoSelecionado) {
        this.cnaeConvencaoSelecionado = cnaeConvencaoSelecionado;
    }

    public String getDataCadastroInicial() {
        return dataCadastroInicial;
    }

    public void setDataCadastroInicial(String dataCadastroInicial) {
        this.dataCadastroInicial = dataCadastroInicial;
    }

    public String getDataCadastroFinal() {
        return dataCadastroFinal;
    }

    public void setDataCadastroFinal(String dataCadastroFinal) {
        this.dataCadastroFinal = dataCadastroFinal;
    }

    public void limparData() {
        this.dataCadastroInicial = "";
        this.dataCadastroFinal = "";
    }

    public Relatorios getRelatorios() {
        try {
            if (relatorios.getId() != Integer.parseInt(listaTipoRelatorios.get(idRelatorios).getDescription())) {
                Jasper.EXPORT_TO_EXCEL = false;
            }
            relatorios = (Relatorios) new Dao().find(new Relatorios(), Integer.parseInt(listaTipoRelatorios.get(idRelatorios).getDescription()));
        } catch (Exception e) {
            relatorios = new Relatorios();
            Jasper.EXPORT_TO_EXCEL = false;
        }
        return relatorios;
    }
}
