package br.com.rtools.relatorios.beans;

import br.com.rtools.arrecadacao.CnaeConvencao;
import br.com.rtools.arrecadacao.Convencao;
import br.com.rtools.arrecadacao.GrupoCidade;
import br.com.rtools.arrecadacao.db.ConvencaoDB;
import br.com.rtools.arrecadacao.db.ConvencaoDBToplink;
import br.com.rtools.endereco.Bairro;
import br.com.rtools.endereco.Cidade;
import br.com.rtools.endereco.db.CidadeDB;
import br.com.rtools.endereco.db.CidadeDBToplink;
import br.com.rtools.impressao.ParametroContribuintes;
import br.com.rtools.pessoa.CentroComercial;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.TipoEndereco;
import br.com.rtools.pessoa.db.*;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.relatorios.db.RelatorioContribuintesDB;
import br.com.rtools.relatorios.db.RelatorioContribuintesDBToplink;
import br.com.rtools.relatorios.db.RelatorioGenericoDB;
import br.com.rtools.relatorios.db.RelatorioGenericoDBToplink;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.Download;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.SalvaArquivos;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@ManagedBean 
@SessionScoped 
public class RelatorioContribuintesBean implements Serializable {

    private String radioOrdem = "razao";
    private String radioCidades = "todas";
    private String radioEscritorios = "todos";
    private String radioCentroComercial = "nenhum";
//    private String comboCondicao = "contribuintes";
    private String comboCondicao = "ativos";
    private boolean chkConvencao = true;
    private boolean chkConvencaoPesquisa = false;
    private boolean chkCnaeConvencao = true;
    private boolean chkGrupo = false;
    private boolean carregaConvencao = true;
    private boolean carregaCnaeConvencao = true;
    private boolean carregaGrupo = true;
    private String idEmails = "t";
    private int idCidades = 0;
    private int idRelatorios = 0;
    private int idContabilidade = 0;
    private int idTipoEndereco = 0;
    private List resultConvencao = new ArrayList();
    private List resultCnaeConvencao = new ArrayList();
    private List<DataObject> listaCentroComercial = new ArrayList();
    private List<DataObject> listaGrupo = new ArrayList();
    private Bairro bairro = new Bairro();
    private List<Convencao> listaConvencaos = new ArrayList<Convencao>();
    private Convencao[] convencaoSelecionada;
    private List<GrupoCidade> listaGrupoCidades = new ArrayList<GrupoCidade>();
    private GrupoCidade[] grupoCidadeSelecionada;
    private List<CentroComercial> listaCentrosComerciais = new ArrayList<CentroComercial>();
    private CentroComercial[] centroComercialSelecionado;
    private List<CnaeConvencao> listaCnaeConvencaos = new ArrayList<CnaeConvencao>();
    private CnaeConvencao[] cnaeConvencaoSelecionado;    
    List<SelectItem> listaTipoRelatorios = new ArrayList<SelectItem>();
    List<SelectItem> listaTipoEndereco = new ArrayList<SelectItem>();    

    public void visualizar() {
        String escritorio = "";
        String centros = "",
                enderecos = "",
                numeros = "";
        String bairros = "";

        RelatorioGenericoDB db = new RelatorioGenericoDBToplink();
        RelatorioContribuintesDB dbContri = new RelatorioContribuintesDBToplink();
        CidadeDB dbCidade = new CidadeDBToplink();
        JuridicaDB dbJur = new JuridicaDBToplink();
        PessoaEnderecoDB dbPesEnd = new PessoaEnderecoDBToplink();
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        Cidade cidade;
        Juridica contabilidade;
        Relatorios relatorios = db.pesquisaRelatorios(Integer.parseInt(listaTipoRelatorios.get(idRelatorios).getDescription()));
        TipoEndereco tipoEndereco = (TipoEndereco) salvarAcumuladoDB.pesquisaCodigo(Integer.parseInt(listaTipoEndereco.get(idTipoEndereco).getDescription()), "TipoEndereco");
        // CONDICAO DO RELATORIO -----------------------------------------------------------
        String condicao = comboCondicao;
        
        // ESCRITORIO DO RELATORIO -----------------------------------------------------------
        if (radioEscritorios.equals("todos")) {
            escritorio = "todos";
        } else if (radioEscritorios.equals("semEscritorio")) {
            escritorio = "semEscritorio";
        } else if (radioEscritorios.equals("comEscritorio")) {
            escritorio = "comEscritorio";
        } else if (radioEscritorios.equals("especifico")) {
            contabilidade = dbJur.pesquisaCodigo(Integer.parseInt(getListaContabilidades().get(idContabilidade).getDescription()));
            escritorio = Integer.toString(contabilidade.getId());
        }

        // CIDADE DO RELATORIO -----------------------------------------------------------
        String cidades = "";
        if (radioCidades.equals("especificas")) {
            cidade = dbCidade.pesquisaCodigo(Integer.parseInt(getListaCidades().get(idCidades).getDescription()));
            cidades = Integer.toString(cidade.getId());
        } else if (radioCidades.equals("local")) {
            cidade = dbPesEnd.pesquisaEndPorPessoaTipo(1, 2).getEndereco().getCidade();
            cidades = Integer.toString(cidade.getId());
        } else if (radioCidades.equals("outras")) {
            cidade = dbPesEnd.pesquisaEndPorPessoaTipo(1, 2).getEndereco().getCidade();
            cidades = Integer.toString(cidade.getId());
        }

        // BAIRRO DO RELATORIO -----------------------------------------------------------
        if (bairro.getId() != -1) {
            List<Bairro> listaBairro = new ArrayList();
            listaBairro.add((Bairro) salvarAcumuladoDB.pesquisaCodigo(bairro.getId(), "Bairro"));
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
            if (centroComercialSelecionado != null) {
                int icc = 0;
                for (CentroComercial centroComercial : centroComercialSelecionado) {
                    if (icc == 0) {
                        centroComercialSelecionados = Integer.toString(centroComercial.getId());
                    } else {
                        centroComercialSelecionados += "," + Integer.toString(centroComercial.getId());
                    }
                    icc++;
                }           
            }              
            if (centros.length() > 0) {
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
            for (CnaeConvencao cnaeConvencao  : cnaeConvencaoSelecionado) {
                if (icc == 0) {
                    cnaeConvencaoSelecionados = Integer.toString(cnaeConvencao.getId());
                } else {
                    cnaeConvencaoSelecionados += "," + Integer.toString(cnaeConvencao.getId());
                }
                icc++;
            }           
        }
        Juridica sindicato = dbJur.pesquisaCodigo(1);
        PessoaEndereco endSindicato = dbPesEnd.pesquisaEndPorPessoaTipo(1, 3);
        @SuppressWarnings("UseOfObsoleteCollectionType")
        List<List> result = new ArrayList();
        if (cnaeConvencaoSelecionado != null) {
            result = dbContri.listaRelatorioContribuintes(relatorios, idEmails, condicao, escritorio, radioCidades, cidades, radioOrdem, cnaeConvencaoSelecionados, tipoEndereco.getId(), enderecos, radioCentroComercial, numeros, gruposCidadesSelecionados, bairros);
        }
        if (result.isEmpty()) {
            GenericaMensagem.info("Sistema", "Não existem registros para o relatório selecionado");
            return;
        }
        try {
            FacesContext faces = FacesContext.getCurrentInstance();
            Collection listaContrs = new ArrayList<ParametroContribuintes>();
            JasperReport jasper = (JasperReport) JRLoader.loadObject(
                    ((ServletContext) faces.getExternalContext().getContext()).getRealPath(relatorios.getJasper()));
            try {
                for (int i = 0; i < result.size(); i++) {
                    listaContrs.add(new ParametroContribuintes(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
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
                JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(listaContrs);
                JasperPrint print = JasperFillManager.fillReport(
                        jasper,
                        null,
                        dtSource);
                byte[] arquivo = JasperExportManager.exportReportToPdf(print);

                String nomeDownload = "relatorio_contribuintes_" + DataHoje.horaMinuto().replace(":", "") + ".pdf";
                SalvaArquivos sa = new SalvaArquivos(arquivo, nomeDownload, false);
                String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/relatorios");
                sa.salvaNaPasta(pathPasta);
                Download download = new Download(nomeDownload, pathPasta, "application/pdf", FacesContext.getCurrentInstance());
                download.baixar();
                download.remover();
            } catch (JRException erro) {
                GenericaMensagem.info("Sistema", "O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
                System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
            }
        } catch (JRException erro) {
            GenericaMensagem.info("Sistema", "O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
            System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
        }
    }

    public void visualizarx() {
        String condicao = "";
        String escritorio = "";
        String cidades = "";
        String pCidade = "";
        String ordem = "";
        String cnaes = "";
        String centros = "",
                enderecos = "",
                numeros = "";
        String idGrupos = "";
        String bairros = "";

        RelatorioGenericoDB db = new RelatorioGenericoDBToplink();
        RelatorioContribuintesDB dbContri = new RelatorioContribuintesDBToplink();
        CidadeDB dbCidade = new CidadeDBToplink();
        JuridicaDB dbJur = new JuridicaDBToplink();
        PessoaEnderecoDB dbPesEnd = new PessoaEnderecoDBToplink();
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        Relatorios relatorios = new Relatorios();
        Cidade cidade = new Cidade();
        Juridica contabilidade = new Juridica();
        Juridica sindicato = new Juridica();
        PessoaEndereco endSindicato = new PessoaEndereco();
        List listaCnaes = new ArrayList();
        TipoEndereco tipoEndereco = new TipoEndereco();
        relatorios = db.pesquisaRelatorios(Integer.parseInt(getListaTipoRelatorios().get(idRelatorios).getDescription()));
        tipoEndereco = (TipoEndereco) salvarAcumuladoDB.pesquisaCodigo(Integer.parseInt(getListaTipoEndereco().get(idTipoEndereco).getDescription()), "TipoEndereco");
        // CONDICAO DO RELATORIO -----------------------------------------------------------
        condicao = comboCondicao;

        // ESCRITORIO DO RELATORIO -----------------------------------------------------------
        if (radioEscritorios.equals("todos")) {
            escritorio = "todos";
        } else if (radioEscritorios.equals("semEscritorio")) {
            escritorio = "semEscritorio";
        } else if (radioEscritorios.equals("comEscritorio")) {
            escritorio = "comEscritorio";
        } else if (radioEscritorios.equals("especifico")) {
            contabilidade = dbJur.pesquisaCodigo(Integer.parseInt(getListaContabilidades().get(idContabilidade).getDescription()));
            escritorio = Integer.toString(contabilidade.getId());
        }

        // CIDADE DO RELATORIO -----------------------------------------------------------
        if (radioCidades.equals("especificas")) {
            cidade = dbCidade.pesquisaCodigo(Integer.parseInt(getListaCidades().get(idCidades).getDescription()));
            cidades = Integer.toString(cidade.getId());
        } else if (radioCidades.equals("local")) {
            cidade = dbPesEnd.pesquisaEndPorPessoaTipo(1, 2).getEndereco().getCidade();
            cidades = Integer.toString(cidade.getId());
        } else if (radioCidades.equals("outras")) {
            cidade = dbPesEnd.pesquisaEndPorPessoaTipo(1, 2).getEndereco().getCidade();
            cidades = Integer.toString(cidade.getId());
        }

        // BAIRRO DO RELATORIO -----------------------------------------------------------
        if (bairro.getId() != -1) {
            List<Bairro> listaBairro = new ArrayList();
            listaBairro.add((Bairro) salvarAcumuladoDB.pesquisaCodigo(bairro.getId(), "Bairro"));
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
            for (int i = 0; i < listaCentroComercial.size(); i++) {
                if ((Boolean) listaCentroComercial.get(i).getArgumento0()) {
                    if (centros.length() > 0 && i != listaCentroComercial.size()) {
                        centros += ",";
                    }
                    centros += Integer.toString(((CentroComercial) ((DataObject) listaCentroComercial.get(i)).getArgumento1()).getJuridica().getId());
                }
            }
            if (centros.length() > 0) {
                idsEnderecos = dbContri.listaCentros(centros);
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
            }
        }

        // CONVENCAO GRUPO CIDADE -------------------------------------------------------
        if (!listaGrupo.isEmpty()) {
            for (int i = 0; i < listaGrupo.size(); i++) {
                if ((Boolean) listaGrupo.get(i).getArgumento0()) {
                    if (idGrupos.length() > 0 && i != listaGrupo.size()) {
                        idGrupos += ",";
                    }
                    idGrupos += Integer.toString(((GrupoCidade) ((DataObject) listaGrupo.get(i)).getArgumento1()).getId());
                }
            }
        }
        // CNAES DO RELATORIO -----------------------------------------------------------
        if (!resultCnaeConvencao.isEmpty()) {
            for (int i = 0; i < resultCnaeConvencao.size(); i++) {
                if ((Boolean) ((DataObject) resultCnaeConvencao.get(i)).getArgumento0() == true) {
                    listaCnaes.add((CnaeConvencao) ((DataObject) resultCnaeConvencao.get(i)).getArgumento1());
                }
            }
            for (int i = 0; i < listaCnaes.size(); i++) {
                if (cnaes.length() > 0 && i != resultCnaeConvencao.size()) {
                    cnaes += ",";
                }
                cnaes += Integer.toString(((CnaeConvencao) listaCnaes.get(i)).getCnae().getId());
            }
        } else {
            cnaes = "";
        }

        sindicato = dbJur.pesquisaCodigo(1);
        endSindicato = dbPesEnd.pesquisaEndPorPessoaTipo(1, 3);
        @SuppressWarnings("UseOfObsoleteCollectionType")
        List<ArrayList> result = new ArrayList();
        if (!resultCnaeConvencao.isEmpty() && !listaCnaes.isEmpty()) {
            result = dbContri.listaRelatorioContribuintes(relatorios, idEmails, condicao, escritorio, radioCidades, cidades, radioOrdem, cnaes, tipoEndereco.getId(), enderecos, radioCentroComercial, numeros, idGrupos, bairros);
        }

        try {
            FacesContext faces = FacesContext.getCurrentInstance();
            Collection listaContrs = new ArrayList<ParametroContribuintes>();
            JasperReport jasper = (JasperReport) JRLoader.loadObject(
                    ((ServletContext) faces.getExternalContext().getContext()).getRealPath(relatorios.getJasper()));
            try {
                for (int i = 0; i < result.size(); i++) {
                    listaContrs.add(new ParametroContribuintes(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
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
                            getConverteNullInt(result.get(i).get(0)), // ID
                            getConverteNullString(result.get(i).get(1)), // NOME PESSOA
                            getConverteNullString(result.get(i).get(4)), // DESCRICAO ENDERECO
                            getConverteNullString(result.get(i).get(3)), // LOGRADOURO
                            getConverteNullString(result.get(i).get(7)), // NUMERO
                            getConverteNullString(result.get(i).get(8)), // COMPLEMENTO
                            getConverteNullString(result.get(i).get(11)), // BAIRRO
                            getConverteNullString(result.get(i).get(9)), // CEP
                            getConverteNullString(result.get(i).get(5)), // CIDADE
                            getConverteNullString(result.get(i).get(6)), // UF
                            getConverteNullString(result.get(i).get(12)), // TELEFONE
                            getConverteNullString(result.get(i).get(13)), // EMAIL
                            getConverteNullString(result.get(i).get(14)), // TIPO DOCUMENTO
                            getConverteNullString(result.get(i).get(2)), // DOCUMENTO
                            getConverteNullInt(result.get(i).get(15)), //ID CNAE
                            getConverteNullString(result.get(i).get(16)), // NUMERO CNAE
                            getConverteNullString(result.get(i).get(17)), // DESCRICAO CNAE
                            getConverteNullInt(result.get(i).get(18)), // ID CONTABILIDADE
                            getConverteNullString(result.get(i).get(10)), // NOME CONTABILIDADE
                            getConverteNullString(result.get(i).get(20)), // DESCRICAO ENDERECO CONTABILIDADE
                            getConverteNullString(result.get(i).get(19)), // LOGRADOURO CONTABILIDADE
                            getConverteNullString(result.get(i).get(24)), // NUMERO CONTABILIDADE
                            getConverteNullString(result.get(i).get(25)), // COMPLEMENTO CONTABILIDADE
                            getConverteNullString(result.get(i).get(21)), // BAIRRO CONTABILIDADE
                            getConverteNullString(result.get(i).get(26)), // CEP CONTABILIDADE
                            getConverteNullString(result.get(i).get(22)), // CIDADE CONTABILIDADE
                            getConverteNullString(result.get(i).get(23)), // UF CONTABILIDADE
                            getConverteNullString(result.get(i).get(27)), // TELEFONE CONTABILIDADE
                            getConverteNullString(result.get(i).get(28)) // EMAIL CONTABILIDADE
                    ));
                }
                JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(listaContrs);
                JasperPrint print = JasperFillManager.fillReport(
                        jasper,
                        null,
                        dtSource);
                byte[] arquivo = JasperExportManager.exportReportToPdf(print);

                String nomeDownload = "relatorio_contribuintes_" + DataHoje.horaMinuto().replace(":", "") + ".pdf";

                SalvaArquivos sa = new SalvaArquivos(arquivo, nomeDownload, false);
                String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/relatorios");

                sa.salvaNaPasta(pathPasta);

                Download download = new Download(nomeDownload,
                        pathPasta,
                        "application/pdf",
                        FacesContext.getCurrentInstance());
                download.baixar();
            } catch (JRException erro) {
                System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
            }
        } catch (JRException erro) {
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
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            List<TipoEndereco> list = (List<TipoEndereco>) salvarAcumuladoDB.pesquisaObjeto(new int[]{2, 3, 4, 5}, "TipoEndereco");
            for (int i = 0; i < list.size(); i++) {
                listaTipoEndereco.add(new SelectItem(new Integer(i), list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
            }
        }
        return listaTipoEndereco;
    }    

    public List<SelectItem> getListaContabilidades() {
        List<SelectItem> contabilidades = new Vector<SelectItem>();
        int i = 0;
        RelatorioContribuintesDB db = new RelatorioContribuintesDBToplink();
        List select = db.pesquisaContabilidades();
        while (i < select.size()) {
            contabilidades.add(new SelectItem(new Integer(i),
                    (String) ((Juridica) select.get(i)).getPessoa().getNome() + " - "
                    + (String) ((Juridica) select.get(i)).getPessoa().getDocumento(),
                    Integer.toString(((Juridica) select.get(i)).getId())));
            i++;
        }
        return contabilidades;
    }

    public List getListaConvencoesx() {
        if (carregaConvencao) {
            ConvencaoDB db = new ConvencaoDBToplink();
            List listConv = new ArrayList();
            resultConvencao = new ArrayList();
            listConv = db.pesquisaTodos();
            DataObject dtObject;
            for (int i = 0; i < listConv.size(); i++) {
                dtObject = new DataObject(true, ((Convencao) (listConv.get(i))));
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
    }

    public List<DataObject> getListaCentroComercialx() {
        if (listaCentroComercial.isEmpty() && !radioCentroComercial.equals("nenhum")) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            List list = (List<CentroComercial>) salvarAcumuladoDB.listaObjeto("CentroComercial", true);
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
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            listaConvencaos = (List<Convencao>) salvarAcumuladoDB.listaObjeto("Convencao");
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
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            listaCentrosComerciais = (List<CentroComercial>) salvarAcumuladoDB.listaObjeto("CentroComercial", true);
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
}
