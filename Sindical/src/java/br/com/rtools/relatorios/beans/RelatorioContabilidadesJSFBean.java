package br.com.rtools.relatorios.beans;

import br.com.rtools.endereco.Cidade;
import br.com.rtools.endereco.db.CidadeDB;
import br.com.rtools.endereco.db.CidadeDBToplink;
import br.com.rtools.impressao.ParametroEscritorios;
import br.com.rtools.pessoa.Cnae;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.TipoEndereco;
import br.com.rtools.pessoa.db.*;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.relatorios.db.RelatorioContabilidadesDB;
import br.com.rtools.relatorios.db.RelatorioContabilidadesDBToplink;
import br.com.rtools.relatorios.db.RelatorioGenericoDB;
import br.com.rtools.relatorios.db.RelatorioGenericoDBToplink;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.Download;
import br.com.rtools.utilitarios.SalvaArquivos;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
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

public class RelatorioContabilidadesJSFBean {

    List<SelectItem> qntEmpresas = new Vector<SelectItem>();
    List<SelectItem> qntEmpresas2 = new Vector<SelectItem>();
    private int idRelatorios = 0;
    private int idEmpInicial = 0;
    private int idEmpFinal = 0;
    private int idCidades = 0;
    private int idTipoEndereco = 0;
    private String radioEmpresas = "todas";
    private String radioCidades = "todas";
    private String radioOrdem = "razao";
    private boolean renEmpresas = false;
    private boolean renCidades = false;
    private boolean carregaCnae = true;
    private boolean chkCnaes = true;
    private List resultCnae = new ArrayList();

    public String condicaoDePesquisa() {
        String pEmpresas = "";
        String cidades = "";
        String pCidade = "";
        String cnaes = "";
        String ordem = "";
        int indexEmp1 = 0;
        int indexEmp2 = 0;

        RelatorioGenericoDB db = new RelatorioGenericoDBToplink();
        RelatorioContabilidadesDB dbConta = new RelatorioContabilidadesDBToplink();
        CidadeDB dbCidade = new CidadeDBToplink();
        PessoaEnderecoDB dbPesEnd = new PessoaEnderecoDBToplink();
        TipoEnderecoDB dbTipoEnd = new TipoEnderecoDBToplink();
        Juridica sindicato = new Juridica();
        PessoaEndereco endSindicato = new PessoaEndereco();
        PessoaEndereco endEscritorio = new PessoaEndereco();
        JuridicaDB dbJur = new JuridicaDBToplink();

        Cidade cidade = new Cidade();
        List listaCnaes = new ArrayList();
        Relatorios relatorios = new Relatorios();
        TipoEndereco tipoEndereco = new TipoEndereco();
        relatorios = db.pesquisaRelatorios(Integer.parseInt(getListaTipoRelatorios().get(idRelatorios).getDescription()));
        tipoEndereco = dbTipoEnd.pesquisaCodigo(Integer.parseInt(getListaTipoEndereco().get(idTipoEndereco).getDescription()));
        int qntEmpresasPorContabel = -1;

        // CONTABILIDADES DO RELATORIO -----------------------------------------------------------
        if (radioEmpresas.equals("todas")) {
            pEmpresas = "todas";
        } else if (radioEmpresas.equals("semEmpresas")) {
            pEmpresas = "semEmpresas";
        } else if (radioEmpresas.equals("comEmpresas")) {
            pEmpresas = "comEmpresas";
            indexEmp1 = Integer.parseInt(getListaQntEmpresas1().get(idEmpInicial).getLabel());
            indexEmp2 = Integer.parseInt(getListaQntEmpresas2().get(idEmpFinal).getLabel());
        }

        // CIDADE DO RELATORIO -----------------------------------------------------------
        if (radioCidades.equals("todas")) {
            pCidade = "todas";
        } else if (radioCidades.equals("especificas")) {
            cidade = dbCidade.pesquisaCodigo(Integer.parseInt(getListaCidades().get(idCidades).getDescription()));
            cidades = Integer.toString(cidade.getId());
            pCidade = "especificas";
        } else if (radioCidades.equals("local")) {
            cidade = dbPesEnd.pesquisaEndPorPessoaTipo(1, 2).getEndereco().getCidade();
            cidades = Integer.toString(cidade.getId());
            pCidade = "local";
        } else if (radioCidades.equals("outras")) {
            cidade = dbPesEnd.pesquisaEndPorPessoaTipo(1, 2).getEndereco().getCidade();
            cidades = Integer.toString(cidade.getId());
            pCidade = "outras";
        }

        // ORDEM DO RELATORIO -----------------------------------------------------------
        if (radioOrdem.equals("razao")) {
            ordem = "razao";
        } else if (radioOrdem.equals("documento")) {
            ordem = "documento";
        } else if (radioOrdem.equals("endereco")) {
            ordem = "endereco";
        } else if (radioOrdem.equals("cep")) {
            ordem = "cep";
        }

        // CNAES DO RELATORIO -----------------------------------------------------------
        if (!resultCnae.isEmpty()) {
            for (int i = 0; i < resultCnae.size(); i++) {
                if ((Boolean) ((DataObject) resultCnae.get(i)).getArgumento0() == true) {
                    listaCnaes.add((Cnae) ((DataObject) resultCnae.get(i)).getArgumento1());
                }
            }
            for (int i = 0; i < listaCnaes.size(); i++) {
                if (cnaes.length() > 0 && i != resultCnae.size()) {
                    cnaes = cnaes + ",";
                }
                cnaes = cnaes + Integer.toString(((Cnae) listaCnaes.get(i)).getId());
            }
        } else {
            cnaes = "";
        }
        sindicato = dbJur.pesquisaCodigo(1);
        endSindicato = dbPesEnd.pesquisaEndPorPessoaTipo(1, 3);

        List<Juridica> result = dbConta.listaRelatorioContabilidades(pEmpresas, indexEmp1, indexEmp2, pCidade, cidades, ordem, cnaes, tipoEndereco.getId());
        try {
            FacesContext faces = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
            byte[] arquivo = new byte[0];
//             JasperReport jasper = null;
            Collection listaEsc = new ArrayList<ParametroEscritorios>();
            JasperReport jasper = (JasperReport) JRLoader.loadObject(
                    ((ServletContext) faces.getExternalContext().getContext()).getRealPath(relatorios.getJasper()));
            try {
                String dados[] = new String[8];
                for (int i = 0; i < result.size(); i++) {
                    endEscritorio = dbPesEnd.pesquisaEndPorPessoaTipo(result.get(i).getPessoa().getId(), tipoEndereco.getId());
                    qntEmpresasPorContabel = dbJur.quantidadeEmpresas(result.get(i).getId());
                    try {
                        dados[0] = endEscritorio.getEndereco().getDescricaoEndereco().getDescricao();
                        dados[1] = endEscritorio.getEndereco().getLogradouro().getDescricao();
                        dados[2] = endEscritorio.getNumero();
                        dados[3] = endEscritorio.getComplemento();
                        dados[4] = endEscritorio.getEndereco().getBairro().getDescricao();
                        dados[5] = endEscritorio.getEndereco().getCep();
                        dados[6] = endEscritorio.getEndereco().getCidade().getCidade();
                        dados[7] = endEscritorio.getEndereco().getCidade().getUf();
                    } catch (Exception e) {
                        dados[0] = "";
                        dados[1] = "";
                        dados[2] = "";
                        dados[3] = "";
                        dados[4] = "";
                        dados[5] = "";
                        dados[6] = "";
                        dados[7] = "";
                    }
                    listaEsc.add(new ParametroEscritorios(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
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
                            result.get(i).getId(),
                            result.get(i).getPessoa().getNome(),
                            dados[0], // DESCRICAO ENDERECO CONTABIL
                            dados[1], // LOGRADOURO CONTABIL
                            dados[2], // NUMERO CONTABIL
                            dados[3], // COMPLEMENTO CONTABIL
                            dados[4], // BAIRRO CONTABIL
                            dados[5], // CEP CONTABIL
                            dados[6], // CIDADE CONTABIL
                            dados[7], // UF CONTABIL
                            result.get(i).getPessoa().getTelefone1(),
                            result.get(i).getPessoa().getEmail1(),
                            qntEmpresasPorContabel));
                    endEscritorio = new PessoaEndereco();
                    qntEmpresasPorContabel = -1;
                }
                JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(listaEsc);
                JasperPrint print = JasperFillManager.fillReport(
                        jasper,
                        null,
                        dtSource);
                arquivo = JasperExportManager.exportReportToPdf(print);

                String nomeDownload = "relatorio_escritorios_" + DataHoje.horaMinuto().replace(":", "") + ".pdf";

                SalvaArquivos sa = new SalvaArquivos(arquivo, nomeDownload, false);
                String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/relatorios");

                sa.salvaNaPasta(pathPasta);

                Download download = new Download(nomeDownload,
                        pathPasta,
                        "application/pdf",
                        FacesContext.getCurrentInstance());
                download.baixar();

            } catch (Exception erro) {
                System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
                return null;
            }
        } catch (Exception erro) {
            System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
            return null;
        }
        return null;
    }

    public List<SelectItem> getListaTipoRelatorios() {
        List<SelectItem> relatorios = new Vector<SelectItem>();
        RelatorioGenericoDB db = new RelatorioGenericoDBToplink();
        List select = db.pesquisaTipoRelatorio(6);
        for (int i = 0; i < select.size(); i++) {
            relatorios.add(new SelectItem(new Integer(i),
                    (String) ((Relatorios) select.get(i)).getNome(),
                    Integer.toString(((Relatorios) select.get(i)).getId())));
        }
        return relatorios;
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

    public List<SelectItem> getListaQntEmpresas1() {
        if (qntEmpresas.isEmpty()) {
            qntEmpresas = new Vector<SelectItem>();
            int i = 0;
            RelatorioContabilidadesDB db = new RelatorioContabilidadesDBToplink();
            List contabilidades = db.pesquisaContabilidades();
            JuridicaDB dbJur = new JuridicaDBToplink();
            Juridica contabil = new Juridica();
            int quantidade = 0;
            boolean tem = false;
            int ind = 0;
            while (i < contabilidades.size()) {
                tem = false;
                contabil = ((Juridica) contabilidades.get(i));
                quantidade = dbJur.quantidadeEmpresas(contabil.getId());
                if (quantidade > 0) {
                    if (qntEmpresas.isEmpty()) {
                        qntEmpresas.add(new SelectItem(new Integer(ind), Integer.toString(quantidade), String.valueOf(new Integer(ind))));
                        ind++;
                    } else {
                        for (int o = 0; o < qntEmpresas.size(); o++) {
                            if (quantidade == Integer.parseInt(qntEmpresas.get(o).getLabel())) {
                                tem = true;
                            }
                        }
                        if (!tem) {
                            qntEmpresas.add(new SelectItem(new Integer(ind), Integer.toString(quantidade), String.valueOf(new Integer(ind))));
                            ind++;
                        }
                    }
                }
                i++;
            }
            BubbleSort(qntEmpresas);
        }
        return qntEmpresas;
    }

    public List<SelectItem> getListaQntEmpresas2() {
        if (qntEmpresas2.isEmpty()) {
            qntEmpresas2.addAll(qntEmpresas);
            idEmpFinal = Integer.parseInt(qntEmpresas2.get(qntEmpresas2.size() - 1).getDescription());
        }
        return qntEmpresas2;
    }

    public List<SelectItem> getListaTipoEndereco() {
        List<SelectItem> tipoEnderecos = new Vector<SelectItem>();
        int i = 0;
        TipoEnderecoDB db = new TipoEnderecoDBToplink();
        List select = db.listaTipoEnderecoParaJuridica();
        while (i < select.size()) {
            tipoEnderecos.add(new SelectItem(new Integer(i),
                    (String) ((TipoEndereco) select.get(i)).getDescricao(),
                    Integer.toString(((TipoEndereco) select.get(i)).getId())));
            i++;
        }
        return tipoEnderecos;
    }

    public List getListaCnaes() {
        if (carregaCnae) {
            RelatorioContabilidadesDB db = new RelatorioContabilidadesDBToplink();
            CnaeDB dbCnae = new CnaeDBToplink();
            List listCnae = new ArrayList();
            resultCnae = new ArrayList();
            listCnae = db.pesquisarCnaeContabilidade();
            DataObject dtObject;
            boolean tem = false;
            for (int i = 0; i < listCnae.size(); i++) {
                if (((Cnae) (listCnae.get(i))).getId() == 1) {
                    tem = true;
                }
            }
            if (tem == false) {
                dtObject = new DataObject(new Boolean(true), dbCnae.pesquisaCodigo(1));
                resultCnae.add(dtObject);
            }
            for (int i = 0; i < listCnae.size(); i++) {
                dtObject = new DataObject(new Boolean(true), ((Cnae) (listCnae.get(i))));
                resultCnae.add(dtObject);
            }
            carregaCnae = false;
        }
        return resultCnae;
    }

    public String marcaTodos() {
        if (chkCnaes) {
            for (int i = 0; i < resultCnae.size(); i++) {
                ((DataObject) resultCnae.get(i)).setArgumento0(new Boolean(true));
            }
        } else {
            for (int i = 0; i < resultCnae.size(); i++) {
                ((DataObject) resultCnae.get(i)).setArgumento0(new Boolean(false));
            }
        }
        return "relatorioContabilidades";
    }

    public static void BubbleSort(List<SelectItem> dados) {
        boolean trocou;
        int limite = dados.size() - 1;
        String swap1 = null;
        String swap2 = null;
        int i = 0;
        do {
            trocou = false;
            i = 0;
            while (i < limite) {
                if ((Integer.parseInt(dados.get(i).getLabel())) > (Integer.parseInt(dados.get(i + 1).getLabel()))) {
                    swap1 = dados.get(i).getLabel();
                    swap2 = dados.get(i + 1).getLabel();
                    dados.get(i).setLabel(swap2);
                    dados.get(i + 1).setLabel(swap1);
                    trocou = true;
                }
                i++;
            }
            limite--;
        } while (trocou);
    }

    public void refreshForm() {
    }

    public String atualizaPG() {
        return "relatorioContabilidades";
    }

    public int getIdRelatorios() {
        return idRelatorios;
    }

    public void setIdRelatorios(int idRelatorios) {
        this.idRelatorios = idRelatorios;
    }

    public String getRadioEmpresas() {
        return radioEmpresas;
    }

    public void setRadioEmpresas(String radioEmpresas) {
        this.radioEmpresas = radioEmpresas;
    }

    public boolean isRenEmpresas() {
        if (radioEmpresas.equals("comEmpresas")) {
            renEmpresas = true;
        } else {
            renEmpresas = false;
            idEmpInicial = 0;
        }
        return renEmpresas;
    }

    public void setRenEmpresas(boolean renEmpresas) {
        this.renEmpresas = renEmpresas;
    }

    public int getIdEmpInicial() {
        if (idEmpInicial > idEmpFinal) {
            idEmpFinal = idEmpInicial;
        }
        return idEmpInicial;
    }

    public void setIdEmpInicial(int idEmpInicial) {
        this.idEmpInicial = idEmpInicial;
    }

    public int getIdEmpFinal() {
        if (idEmpInicial > idEmpFinal) {
            idEmpFinal = idEmpInicial;
        }
        return idEmpFinal;
    }

    public void setIdEmpFinal(int idEmpFinal) {
        this.idEmpFinal = idEmpFinal;
    }

    public int getIdCidades() {
        return idCidades;
    }

    public void setIdCidades(int idCidades) {
        this.idCidades = idCidades;
    }

    public boolean isRenCidades() {
        if (radioCidades.equals("especificas")) {
            renCidades = true;
        } else {
            renCidades = false;
        }
        return renCidades;
    }

    public void setRenCidades(boolean renCidades) {
        this.renCidades = renCidades;
    }

    public String getRadioCidades() {
        return radioCidades;
    }

    public void setRadioCidades(String radioCidades) {
        this.radioCidades = radioCidades;
    }

    public String getRadioOrdem() {
        return radioOrdem;
    }

    public void setRadioOrdem(String radioOrdem) {
        this.radioOrdem = radioOrdem;
    }

    public boolean isChkCnaes() {
        return chkCnaes;
    }

    public void setChkCnaes(boolean chkCnaes) {
        this.chkCnaes = chkCnaes;
    }

    public int getIdTipoEndereco() {
        return idTipoEndereco;
    }

    public void setIdTipoEndereco(int idTipoEndereco) {
        this.idTipoEndereco = idTipoEndereco;
    }
}
