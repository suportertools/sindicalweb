package br.com.rtools.relatorios.beans;

import br.com.rtools.endereco.Cidade;
import br.com.rtools.impressao.ParametroEscritorios;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.TipoEndereco;
import br.com.rtools.pessoa.db.*;
import br.com.rtools.relatorios.RelatorioOrdem;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.relatorios.dao.RelatorioOrdemDao;
import br.com.rtools.relatorios.db.RelatorioContabilidadesDB;
import br.com.rtools.relatorios.db.RelatorioContabilidadesDBToplink;
import br.com.rtools.relatorios.db.RelatorioGenericoDB;
import br.com.rtools.relatorios.db.RelatorioGenericoDBToplink;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Jasper;
import br.com.rtools.utilitarios.PF;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.event.TabChangeEvent;

@ManagedBean
@SessionScoped
public class RelatorioContabilidadesBean implements Serializable {

    private Boolean[] filtro;
    /**
     * 0 - Cidades; 1 - Tipo Relatórios; 2 - Tipo Endereço; 3 - Quantidade
     * Inicio 4 - Quantidade Fim; 5 - Relatório Ordem
     */
    private Integer[] index;
    private String tipoRelatorio;
    private String tipo;
    private String indexAccordion;
    private List<SelectItem> listCidades;
    private List<SelectItem> listRelatorios;
    private List<SelectItem> listTipoEndereco;
    private List<SelectItem> listQuantidadeInicio;
    private List<SelectItem> listQuantidadeFim;
    private List<SelectItem> listRelatorioOrdem;
    private Integer quantidadeEmpresas;
    private String radioEmpresas;
    private String radioCidades;
    private String radioOrdem;
    private boolean ocultaEmpresas;
    private boolean ocultaCidades;
    private Relatorios relatorios;

    @PostConstruct
    public void init() {
        filtro = new Boolean[3];
        filtro[0] = true; // Quantidade Empresas
        filtro[1] = false; // Cidade
        filtro[2] = false; // Ordenação
        index = new Integer[6];
        index[0] = 0;
        index[1] = 0;
        index[2] = 0;
        index[3] = 0;
        index[4] = 0;
        index[5] = 0;
        listCidades = new ArrayList<>();
        listRelatorios = new ArrayList<>();
        listTipoEndereco = new ArrayList<>();
        listQuantidadeInicio = new ArrayList<>();
        listQuantidadeFim = new ArrayList<>();
        listRelatorioOrdem = new ArrayList<>();
        quantidadeEmpresas = 0;
        radioEmpresas = "todas";
        radioCidades = "todas";
        radioOrdem = "razao";
        ocultaEmpresas = false;
        ocultaCidades = false;
        relatorios = null;
        tipoRelatorio = "Resumo";
        indexAccordion = "Resumo";
        tipo = "todos";
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("relatorioContabilidadesBean");
        GenericaSessao.remove("jasperBean");
    }

    public void print() {
        String cidades = "";
        int inicio = 0;
        int fim = 0;

        RelatorioGenericoDB db = new RelatorioGenericoDBToplink();
        RelatorioContabilidadesDB dbConta = new RelatorioContabilidadesDBToplink();
        PessoaEnderecoDB dbPesEnd = new PessoaEnderecoDBToplink();
        Cidade cidade;
        Dao dao = new Dao();
        Relatorios r = db.pesquisaRelatorios(Integer.parseInt(getListRelatorios().get(index[0]).getDescription()));
        if (!listRelatorioOrdem.isEmpty()) {
            relatorios.setQryOrdem(((RelatorioOrdem) dao.find(new RelatorioOrdem(), Integer.parseInt(getListaRelatorioOrdem().get(index[5]).getDescription()))).getQuery());
        }
        TipoEndereco tipoEndereco = (TipoEndereco) dao.find(new TipoEndereco(), Integer.parseInt(getListTipoEndereco().get(index[1]).getDescription()));

        // CONTABILIDADES DO RELATORIO -----------------------------------------------------------
        if (radioEmpresas.equals("comEmpresas")) {
            inicio = Integer.parseInt(listQuantidadeInicio.get(index[3]).getDescription());
            fim = Integer.parseInt(listQuantidadeFim.get(index[4]).getDescription());
            if (inicio > fim) {
                inicio = fim;
            }
        }

        // CIDADE DO RELATORIO -----------------------------------------------------------
        if (radioCidades.equals("especificas")) {
            cidade = (Cidade) dao.find(new Cidade(), Integer.parseInt(getListCidades().get(index[2]).getDescription()));
            cidades = Integer.toString(cidade.getId());
        } else if (radioCidades.equals("local")) {
            cidade = dbPesEnd.pesquisaEndPorPessoaTipo(1, 2).getEndereco().getCidade();
            cidades = Integer.toString(cidade.getId());
        } else if (radioCidades.equals("outras")) {
            cidade = dbPesEnd.pesquisaEndPorPessoaTipo(1, 2).getEndereco().getCidade();
            cidades = Integer.toString(cidade.getId());
        }
        List list = dbConta.listaRelatorioContabilidades(radioEmpresas, inicio, fim, radioCidades, cidades, radioOrdem, tipoEndereco.getId());
        if (list.isEmpty()) {
            GenericaMensagem.info("Sistema", "Não existem registros para o relatório selecionado");
            return;
        }
        try {
            FacesContext faces = FacesContext.getCurrentInstance();
            Collection listaEscritorios = new ArrayList<>();

            try {
                String dados[] = new String[8];
                for (int i = 0; i < list.size(); i++) {
                    int quantidade = 0;
                    Juridica juridica = (Juridica) dao.find(new Juridica(), Integer.parseInt(((List) list.get(i)).get(0).toString()));
                    PessoaEndereco pessoaEndereco = (PessoaEndereco) dao.find(new PessoaEndereco(), Integer.parseInt(((List) list.get(i)).get(1).toString()));
                    quantidade = Integer.parseInt(((List) list.get(i)).get(2).toString());
                    try {
                        dados[0] = pessoaEndereco.getEndereco().getDescricaoEndereco().getDescricao();
                        dados[1] = pessoaEndereco.getEndereco().getLogradouro().getDescricao();
                        dados[2] = pessoaEndereco.getNumero();
                        dados[3] = pessoaEndereco.getComplemento();
                        dados[4] = pessoaEndereco.getEndereco().getBairro().getDescricao();
                        dados[5] = pessoaEndereco.getEndereco().getCep();
                        dados[6] = pessoaEndereco.getEndereco().getCidade().getCidade();
                        dados[7] = pessoaEndereco.getEndereco().getCidade().getUf();
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
                    listaEscritorios.add(new ParametroEscritorios(
                            juridica.getId(), // ESCRITÓRIO - ID
                            juridica.getPessoa().getNome(), // ESCRITÓRIO - NOME
                            dados[0], // ESCRITÓRIO - DESCRICAO ENDERECO
                            dados[1], // ESCRITÓRIO - LOGRADOURO
                            dados[2], // ESCRITÓRIO - NUMERO
                            dados[3], // ESCRITÓRIO - COMPLEMENTO
                            dados[4], // ESCRITÓRIO - BAIRRO
                            dados[5], // ESCRITÓRIO - CEP
                            dados[6], // ESCRITÓRIO - CIDADE
                            dados[7], // ESCRITÓRIO - UF
                            juridica.getPessoa().getTelefone1(),
                            juridica.getPessoa().getEmail1(),
                            quantidade));
                }
                Jasper.TYPE = "paisagem";
                if (r.getExcel()) {
                    Jasper.EXCEL_FIELDS = r.getCamposExcel();
                } else {
                    Jasper.EXCEL_FIELDS = "";
                }
                Jasper.printReports(relatorios.getJasper(), "escritorios", listaEscritorios);
            } catch (Exception erro) {
                GenericaMensagem.info("Sistema", "O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
                System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
            }
        } catch (Exception erro) {
            GenericaMensagem.info("Sistema", "O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
            System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
        }
    }

    public List<SelectItem> getListRelatorios() {
        if (listRelatorios.isEmpty()) {
            RelatorioGenericoDB db = new RelatorioGenericoDBToplink();
            List<Relatorios> list = (List<Relatorios>) db.pesquisaTipoRelatorio(6);
            for (int i = 0; i < list.size(); i++) {
                listRelatorios.add(new SelectItem(i, list.get(i).getNome(), Integer.toString(list.get(i).getId())));
            }
        }
        return listRelatorios;
    }

    public List<SelectItem> getListCidades() {
        if (listCidades.isEmpty()) {
            RelatorioGenericoDB db = new RelatorioGenericoDBToplink();
            List<Cidade> list = (List<Cidade>) db.pesquisaCidadesRelatorio();
            for (int i = 0; i < list.size(); i++) {
                listCidades.add(new SelectItem(i, list.get(i).getCidade(), Integer.toString(list.get(i).getId())));
            }
        }
        return listCidades;
    }

    public List<SelectItem> getListQuantidadeInicio() {
        quantidadeEmpresas();
        if (listQuantidadeInicio.isEmpty()) {
            for (int i = 0; i < quantidadeEmpresas; i++) {
                boolean itemSelecionado = false;
                if (i == 0) {
                    itemSelecionado = true;
                }
                listQuantidadeInicio.add(new SelectItem(i, Integer.toString(i + 1), Integer.toString(i + 1), false, false, itemSelecionado));
            }
        }
        return listQuantidadeInicio;
    }

    public List<SelectItem> getListQuantidadeFim() {
        quantidadeEmpresas();
        if (listQuantidadeFim.isEmpty()) {
            for (int i = 0; i < quantidadeEmpresas; i++) {
                boolean itemSelecionado = false;
                if (i + 1 == quantidadeEmpresas) {
                    index[4] = i;
                }
                listQuantidadeFim.add(new SelectItem(i, Integer.toString(i + 1), Integer.toString(i + 1), false, false, itemSelecionado));
            }
        }
        return listQuantidadeFim;
    }

    public void quantidadeEmpresas() {
        if (quantidadeEmpresas <= 0) {
            RelatorioContabilidadesDB db = new RelatorioContabilidadesDBToplink();
            quantidadeEmpresas = db.quantidadeEmpresas();
        }
    }

//    public List<SelectItem> getListaQntEmpresas1() {
//        if (qntEmpresas.isEmpty()) {
//            qntEmpresas = new ArrayList<SelectItem>();
//            int i = 0;
//            RelatorioContabilidadesDB db = new RelatorioContabilidadesDBToplink();
//            List contabilidades = db.pesquisaContabilidades();
//            JuridicaDB dbJur = new JuridicaDBToplink();
////            Juridica contabil = new Juridica();
//            int quantidade = 0;
//            boolean tem = false;
//            int ind = 0;
//            while (i < contabilidades.size()) {
//                tem = false;
//                Juridica contabil = ((Juridica) contabilidades.get(i));
//                quantidade = dbJur.quantidadeEmpresas(contabil.getId());
//                if (quantidade > 0) {
//                    if (qntEmpresas.isEmpty()) {
//                        qntEmpresas.add(new SelectItem(new Integer(ind), Integer.toString(quantidade), String.valueOf(new Integer(ind))));
//                        ind++;
//                    } else {
//                        for (int o = 0; o < qntEmpresas.size(); o++) {
//                            if (quantidade == Integer.parseInt(qntEmpresas.get(o).getLabel())) {
//                                tem = true;
//                            }
//                        }
//                        if (!tem) {
//                            qntEmpresas.add(new SelectItem(new Integer(ind), Integer.toString(quantidade), String.valueOf(new Integer(ind))));
//                            ind++;
//                        }
//                    }
//                }
//                i++;
//            }
//            BubbleSort(qntEmpresas);
//        }
//        return qntEmpresas;
//    }
//    public List<SelectItem> getListaQntEmpresas2() {
//        if (qntEmpresas2.isEmpty()) {
//            qntEmpresas2.addAll(qntEmpresas);
//            idEmpFinal = Integer.parseInt(qntEmpresas2.get(qntEmpresas2.size() - 1).getDescription());
//        }
//        return qntEmpresas2;
//    }
    public List<SelectItem> getListTipoEndereco() {
        if (listTipoEndereco.isEmpty()) {
            Dao dao = new Dao();
            List<TipoEndereco> list = (List<TipoEndereco>) dao.find("TipoEndereco", new int[]{2, 3, 4, 5});
            for (int i = 0; i < list.size(); i++) {
                listTipoEndereco.add(new SelectItem(i, list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
            }
        }
        return listTipoEndereco;
    }

//    public static void BubbleSort(List<SelectItem> dados) {
//        boolean trocou;
//        int limite = dados.size() - 1;
//        String swap1 = null;
//        String swap2 = null;
//        int i = 0;
//        do {
//            trocou = false;
//            i = 0;
//            while (i < limite) {
//                if ((Integer.parseInt(dados.get(i).getLabel())) > (Integer.parseInt(dados.get(i + 1).getLabel()))) {
//                    swap1 = dados.get(i).getLabel();
//                    swap2 = dados.get(i + 1).getLabel();
//                    dados.get(i).setLabel(swap2);
//                    dados.get(i + 1).setLabel(swap1);
//                    trocou = true;
//                }
//                i++;
//            }
//            limite--;
//        } while (trocou);
//    }
    public String getRadioEmpresas() {
        return radioEmpresas;
    }

    public void setRadioEmpresas(String radioEmpresas) {
        this.radioEmpresas = radioEmpresas;
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

    public boolean isOcultaEmpresas() {
        ocultaEmpresas = radioEmpresas.equals("comEmpresas");
        return ocultaEmpresas;
    }

    public void setOcultaEmpresas(boolean ocultaEmpresas) {
        this.ocultaEmpresas = ocultaEmpresas;
    }

    public boolean isOcultaCidades() {
        ocultaCidades = radioCidades.equals("especificas");
        return ocultaCidades;
    }

    public void setOcultaCidades(boolean ocultaCidades) {
        this.ocultaCidades = ocultaCidades;
    }

    public Relatorios getRelatorios() {
        try {
            if (relatorios != null && relatorios.getId() != Integer.parseInt(listRelatorios.get(index[0]).getDescription())) {
                Jasper.EXPORT_TO_EXCEL = false;
            }
            relatorios = (Relatorios) new Dao().find(new Relatorios(), Integer.parseInt(listRelatorios.get(index[0]).getDescription()));
        } catch (Exception e) {
            relatorios = new Relatorios();
            Jasper.EXPORT_TO_EXCEL = false;
        }
        return relatorios;
    }

    /**
     * 0 - Cidades; 1 - Tipo Relatórios; 2 - Tipo Endereço; 3 - Quantidade
     * Inicio; 4 - Quantidade Fim; 5 -Relatório Ordem
     *
     * @return
     */
    public Integer[] getIndex() {
        return index;
    }

    public void setIndex(Integer[] index) {
        if (index[4] < index[3]) {
            index[3] = index[4];
        }
        this.index = index;
    }

    public String getTipoRelatorio() {
        return tipoRelatorio;
    }

    public void setTipoRelatorio(String tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getIndexAccordion() {
        return indexAccordion;
    }

    public void setIndexAccordion(String indexAccordion) {
        this.indexAccordion = indexAccordion;
    }

    /**
     * 0 - Quantidade Empresas; 1 - Cidade; 2 - Ordenação;
     *
     * @return
     */
    public Boolean[] getFiltro() {
        return filtro;
    }

    public void setFiltro(Boolean[] filtro) {
        this.filtro = filtro;
    }

    public void close(String close) {
        switch (close) {
            case "quantidade_empresas":
                index[3] = 0;
                index[4] = 0;
                filtro[0] = false;
                listQuantidadeInicio.clear();
                listQuantidadeFim.clear();
                break;
            case "cidades":
                filtro[1] = false;
                index[2] = 0;
                listCidades.clear();
                break;
            case "order":
                filtro[2] = false;
                radioOrdem = "razao";
                break;
        }
        PF.update("form_relatorio:id_panel");
    }

    public void tipoRelatorioChange(TabChangeEvent event) {
        tipoRelatorio = event.getTab().getTitle();
        indexAccordion = ((AccordionPanel) event.getComponent()).getActiveIndex();
        if (tipoRelatorio.equals("Resumo")) {
            clear();
        }
    }

    public void clear() {
        if (!filtro[0]) {
            index[3] = 0;
            index[4] = 0;
            listQuantidadeInicio.clear();
            listQuantidadeFim.clear();
        }
        if (!filtro[1]) {
            filtro[1] = false;
            index[2] = 0;
            listCidades.clear();
        }
        if (!filtro[2]) {
            filtro[2] = false;
            radioOrdem = "razao";
        }
    }

    public List<SelectItem> getListaRelatorioOrdem() {
        listRelatorioOrdem.clear();
        if (index[0] != null) {
            RelatorioOrdemDao relatorioOrdemDao = new RelatorioOrdemDao();
            List<RelatorioOrdem> list = relatorioOrdemDao.findAllByRelatorio(Integer.parseInt(getListRelatorios().get(index[0]).getDescription()));
            for (int i = 0; i < list.size(); i++) {
                listRelatorioOrdem.add(new SelectItem(i, list.get(i).getNome(), "" + list.get(i).getId()));
            }
        }
        return listRelatorioOrdem;
    }
}
