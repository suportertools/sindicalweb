package br.com.rtools.relatorios.beans;

import br.com.rtools.arrecadacao.Convencao;
import br.com.rtools.arrecadacao.ConvencaoPeriodo;
import br.com.rtools.arrecadacao.GrupoCidade;
import br.com.rtools.arrecadacao.db.GrupoCidadesDB;
import br.com.rtools.arrecadacao.db.GrupoCidadesDBToplink;
import br.com.rtools.arrecadacao.dao.OposicaoDao;
import br.com.rtools.endereco.Cidade;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.TipoServico;
import br.com.rtools.financeiro.db.ServicoRotinaDB;
import br.com.rtools.financeiro.db.ServicoRotinaDBToplink;
import br.com.rtools.impressao.ParametroComparativoArrecadacao;
import br.com.rtools.pessoa.Cnae;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.beans.JuridicaBean;
import br.com.rtools.pessoa.db.EnviarArquivosDB;
import br.com.rtools.pessoa.db.EnviarArquivosDBToplink;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.relatorios.dao.RelatorioComparativoArrecadacaoDao;
import br.com.rtools.relatorios.db.RelatorioGenericoDB;
import br.com.rtools.relatorios.db.RelatorioGenericoDBToplink;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.GenericaString;
import br.com.rtools.utilitarios.Jasper;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.PF;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.event.TabChangeEvent;

@ManagedBean
@SessionScoped
public class RelatorioComparativoArrecadacaoBean implements Serializable {

    private Juridica juridica;
    private Juridica contabilidade;
    private List selectedConvencao;
    private List selectedGrupoCidades;
    private List selectedCnae;
    private List selectedCidadeBase;
    private List<SelectItem>[] listSelectItem;
    private List<ParametroComparativoArrecadacao> parametroComparativoArrecadacaos;
    private List<ConvencaoPeriodo> listConvencaoPeriodos;
    private Map<String, Integer> listConvencoes;
    private Map<String, Integer> listGrupoCidades;
    private Map<String, Integer> listCnaes;
    private Map<String, Integer> listCidadeBase;
    private Boolean[] filtro;
    private String[] referencia;
    private String[] tipo;
    private Integer[] index;

    private Integer percentual;
    private String tipoRelatorio;
    private String indexAccordion;
    private String order;

    @PostConstruct
    public void init() {
        juridica = new Juridica();
        contabilidade = new Juridica();
        filtro = new Boolean[9];
        filtro[0] = true;
        filtro[1] = true;
        filtro[2] = true;
        filtro[3] = false;
        filtro[4] = false;
        filtro[5] = false;
        filtro[6] = false;
        filtro[7] = false;
        filtro[8] = false;
        referencia = new String[2];
        referencia[0] = "";
        referencia[1] = "";
        tipo = new String[2];
        tipo[0] = "baixado";
        tipo[1] = "baixado";
        selectedConvencao = new ArrayList<>();
        selectedGrupoCidades = new ArrayList<>();
        selectedCnae = new ArrayList<>();
        selectedCnae = new ArrayList<>();
        listCnaes = null;
        listConvencoes = null;
        listGrupoCidades = null;
        listCidadeBase = null;
        listSelectItem = new ArrayList[3];
        listSelectItem[0] = new ArrayList<>();
        listSelectItem[1] = new ArrayList<>();
        listSelectItem[2] = new ArrayList<>();
        parametroComparativoArrecadacaos = new ArrayList<>();
        listConvencaoPeriodos = new ArrayList<>();
        index = new Integer[3];
        index[0] = null;
        index[1] = null;
        index[2] = null;
        percentual = null;
        tipoRelatorio = "Avançado";
        indexAccordion = "Avançado";
        order = "";
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("relatorioComparativoArrecadacaoBean");
        GenericaSessao.remove("juridicaPesquisa");
    }

    public void visualizar() {
        Relatorios relatorios = null;
        if (!getListaTipoRelatorios().isEmpty()) {
            RelatorioGenericoDB rgdb = new RelatorioGenericoDBToplink();
            relatorios = rgdb.pesquisaRelatorios(Integer.parseInt(listSelectItem[0].get(index[0]).getDescription()));
        }
        if (relatorios == null) {
            return;
        }
        if (index[1] == null) {
            GenericaMensagem.warn("Validação", "Informar serviço!");
            return;
        }
        if (referencia[0].isEmpty()) {
            GenericaMensagem.warn("Validação", "Informar referência 1!");
            return;
        }
        if (referencia[1].isEmpty()) {
            GenericaMensagem.warn("Validação", "Informar referência 2!");
            return;
        }
        Map parameters = new LinkedHashMap();
        float valor_total_1 = 0;
        float valor_total_2 = 0;
        String detalheRelatorio = "";
        if (parametroComparativoArrecadacaos.isEmpty()) {
            RelatorioComparativoArrecadacaoDao relatorioComparativoArrecadacaoDao = new RelatorioComparativoArrecadacaoDao();
            Integer pEmpresa = null;
            Integer pContabilidade = null;
            Integer servicos = null;
            List listDetalhePesquisa = new ArrayList();
            if (filtro[2]) {
                listDetalhePesquisa.add(" Referência " + referencia[0] + " e " + referencia[1]);
            }
            if (juridica.getId() != -1) {
                pEmpresa = juridica.getId();
                listDetalhePesquisa.add(" Empresa - CNPJ: " + juridica.getPessoa().getDocumento() + " - " + juridica.getPessoa().getNome());
            }
            if (contabilidade.getId() != -1) {
                pContabilidade = contabilidade.getId();
                listDetalhePesquisa.add(" Contabilidade - CNPJ: " + contabilidade.getPessoa().getDocumento() + " - " + contabilidade.getPessoa().getNome());
            }
            if (!order.isEmpty()) {
                relatorioComparativoArrecadacaoDao.setOrder(order);
            }
            String inConvencao = inIdConvencao();
            String inGrupoCidade = inIdGruposCidade();
            String inCnaes = inIdCnaes();
            String inCidadeBase = inIdCidadeBase();
            if (!getListServicos().isEmpty()) {
                if (index[1] != null) {
                    servicos = Integer.parseInt(getListServicos().get(index[1]).getDescription());
                }
            }
            if(!filtro[8]) {
                percentual = null;
            }
            List list = relatorioComparativoArrecadacaoDao.find(relatorios, pEmpresa, pContabilidade, servicos, 1, referencia[0], tipo[0], referencia[1], tipo[1], inConvencao, inGrupoCidade, inCnaes, inCidadeBase, percentual);
            if (listDetalhePesquisa.isEmpty()) {
                detalheRelatorio += "Pesquisar todos registros!";
            } else {
                detalheRelatorio += "";
                for (int i = 0; i < listDetalhePesquisa.size(); i++) {
                    if (i == 0) {
                        detalheRelatorio += "" + listDetalhePesquisa.get(i).toString();
                    } else {
                        detalheRelatorio += "; " + listDetalhePesquisa.get(i).toString();
                    }
                }
            }
            String dt = "";
            for (Object list1 : list) {
                dt = GenericaString.converterNullToString(((List) list1).get(0));
                String valor1 = GenericaString.converterNullToString(((List) list1).get(4));
                String valor2 = GenericaString.converterNullToString(((List) list1).get(6));
                if (valor1 != null && !valor1.isEmpty()) {
                    valor_total_1 += Float.parseFloat(valor1);
                    valor1 = Moeda.converteR$Float(Float.parseFloat(valor1));
                }
                if (valor2 != null && !valor2.isEmpty()) {
                    valor_total_2 += Float.parseFloat(valor2);
                    valor2 = Moeda.converteR$Float(Float.parseFloat(valor2));
                }
                ParametroComparativoArrecadacao pca
                        = new ParametroComparativoArrecadacao(
                                detalheRelatorio,
                                GenericaString.converterNullToString(((List) list1).get(0)), // CNPJ
                                GenericaString.converterNullToString(((List) list1).get(1)), // Nome
                                GenericaString.converterNullToString(((List) list1).get(2)), // Contribuição
                                GenericaString.converterNullToString(((List) list1).get(3)), // Referência 1
                                valor1, // Valor 1
                                GenericaString.converterNullToString(((List) list1).get(5)), // Referência 2
                                valor2, // Valor 2                                
                                GenericaString.converterNullToString(((List) list1).get(7)) // Percentual
                        );
                parametroComparativoArrecadacaos.add(pca);
            }
            parameters.put("valor_total_1", "R$ " + Moeda.converteR$Float(valor_total_1));
            parameters.put("valor_total_2", "R$ " + Moeda.converteR$Float(valor_total_2));

        }
        if (parametroComparativoArrecadacaos.isEmpty()) {
            GenericaMensagem.info("Sistema", "Não existem registros para o relatório selecionado");
            return;
        }
        Jasper.printReports(relatorios.getJasper(), "comparativo_arrecadacao", (Collection) parametroComparativoArrecadacaos, parameters);
    }

    public List<SelectItem> getListaTipoRelatorios() {
        if (listSelectItem[0].isEmpty()) {
            RelatorioGenericoDB db = new RelatorioGenericoDBToplink();
            List<Relatorios> list = (List<Relatorios>) db.pesquisaTipoRelatorio(278);
            for (int i = 0; i < list.size(); i++) {
                listSelectItem[0].add(new SelectItem(i, list.get(i).getNome(), "" + list.get(i).getId()));
            }
            if (listSelectItem[0].isEmpty()) {
                listSelectItem[0] = new ArrayList<>();
            }
        }
        return listSelectItem[0];
    }

    public String getTipoRelatorio() {
        return tipoRelatorio;
    }

    public void setTipoRelatorio(String tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
    }

    public void tipoRelatorioChange(TabChangeEvent event) {
        tipoRelatorio = event.getTab().getTitle();
        indexAccordion = ((AccordionPanel) event.getComponent()).getActiveIndex();
        if (tipoRelatorio.equals("Simples")) {
            limpar();
            filtro[2] = false;
            filtro[0] = false;
        }
    }

    public void limpar() {
        // Serviços
        if (!filtro[0]) {
            index[1] = null;
            listSelectItem[1] = new ArrayList<>();
        }
        // Tipo de Serviços
        if (!filtro[1]) {
            index[2] = null;
            listSelectItem[2] = new ArrayList<>();
        }
        // Referência
        if (!filtro[2]) {
            referencia[0] = "";
            referencia[1] = "";
        }
        if (!filtro[3]) {
            juridica = new Juridica();
        }
        if (!filtro[4]) {
            contabilidade = new Juridica();
        }
        if (!filtro[5]) {
            listCnaes = null;
            listConvencoes = null;
            listGrupoCidades = null;
            listSelectItem[0] = new ArrayList<>();
            listSelectItem[1] = new ArrayList<>();
        }
        // Cidades da Base
        if (!filtro[6]) {
            listCidadeBase = null;
        }
        if (!filtro[7]) {
            order = "";
        }
        if (!filtro[8]) {
            percentual = 0;
        }
    }

    public void close(String close) {
        switch (close) {
            case "servico":
                // Serviços
                filtro[0] = false;
                listSelectItem[1] = new ArrayList<>();
                break;
            case "tipoServico":
                // Tipo Serviços
                filtro[1] = false;
                listSelectItem[2] = new ArrayList<>();
                break;
            case "referencia":
                filtro[2] = false;
                referencia[0] = "";
                referencia[1] = "";
                break;
            case "empresa":
                filtro[3] = false;
                juridica = new Juridica();
                break;
            case "contabilidade":
                filtro[4] = false;
                contabilidade = new Juridica();
                break;
            case "cnae":
                filtro[5] = false;
                listCnaes = null;
                listConvencoes = null;
                listGrupoCidades = null;
                listSelectItem = new ArrayList[2];
                listSelectItem[0] = new ArrayList<>();
                listSelectItem[1] = new ArrayList<>();
                break;
            case "cidadeBase":
                // Cidades da Base
                filtro[6] = false;
                listCidadeBase = null;
                selectedCidadeBase = new ArrayList();
                break;
            case "order":
                order = "";
                filtro[7] = false;
                break;
            case "percentual":
                percentual = null;
                filtro[8] = false;
                break;
        }
        PF.update("form_relatorio:id_panel");
    }

    public String getIndexAccordion() {
        return indexAccordion;
    }

    public void setIndexAccordion(String indexAccordion) {
        this.indexAccordion = indexAccordion;
    }

    public Juridica getJuridica() {
        if (GenericaSessao.exists("juridicaPesquisa")) {
            if (GenericaSessao.exists("tipo")) {
                if (GenericaSessao.getString("tipo", true).equals("empresa")) {
                    juridica = (Juridica) GenericaSessao.getObject("juridicaPesquisa", true);
                }
            }
        }
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }

    public Juridica getContabilidade() {
        if (GenericaSessao.exists("juridicaPesquisa")) {
            if (GenericaSessao.exists("tipo")) {
                if (GenericaSessao.getString("tipo", true).equals("contabilidade")) {
                    contabilidade = (Juridica) GenericaSessao.getObject("juridicaPesquisa", true);
                }
            }
        }
        return contabilidade;
    }

    public void setContabilidade(Juridica contabilidade) {
        this.contabilidade = contabilidade;
    }

    public List<SelectItem>[] getListSelectItem() {
        return listSelectItem;
    }

    public void setListSelectItem(List<SelectItem>[] listSelectItem) {
        this.listSelectItem = listSelectItem;
    }

    /**
     * <strong>Index</strong>
     * <ul>
     * <li>[0] Tipos de Relatórios</li>
     * <li>[1] List[SelectItem] Convenção Período</li>
     * <li>[2] List[SelectItem] Serviços </li>
     * <li>[3] List[SelectItem] Tipo de Serviços </li>
     * </ul>
     *
     * @return Integer
     */
    public Integer[] getIndex() {
        return index;
    }

    public void setIndex(Integer[] index) {
        this.index = index;
    }

    public void removeReferencia(int index) {
        for (int i = 0; i < listConvencaoPeriodos.size(); i++) {
            if (index == i) {
                listConvencaoPeriodos.remove(i);
                return;
            }
        }
    }

    public List<ConvencaoPeriodo> getListConvencaoPeriodos() {
        return listConvencaoPeriodos;
    }

    public void setListConvencaoPeriodos(List<ConvencaoPeriodo> listConvencaoPeriodos) {
        this.listConvencaoPeriodos = listConvencaoPeriodos;
    }

    /**
     * <strong>Filtros</strong>
     * <ul>
     * <li>[0] Serviços </li>
     * <li>[1] Tipo Serviço </li>
     * <li>[2] Referência </li>
     * <li>[3] Empresa </li>
     * <li>[4] Contabilidade </li>
     * <li>[5] Cnae </li>
     * <li>[6] Cidade Base </li>
     * <li>[7] Ordenação </li>
     * <li>[8] Percentual </li>
     * </ul>
     *
     * @return boolean
     */
    public Boolean[] getFiltro() {
        return filtro;
    }

    public void setFiltro(Boolean[] filtro) {
        this.filtro = filtro;
    }

    public Map<String, Integer> getListCnaes() {
        listCnaes = null;
        if (!selectedConvencao.isEmpty()) {
            listCnaes = new LinkedHashMap<>();
            String ids = inIdConvencao();
            if (!ids.isEmpty()) {
                OposicaoDao odb = new OposicaoDao();
                List<Cnae> list = (List<Cnae>) odb.listaCnaesPorOposicaoJuridica(ids);
                for (int i = 0; i < list.size(); i++) {
                    listCnaes.put(list.get(i).getCnae() + " - " + list.get(i).getNumero(), list.get(i).getId());
                }
            }
        } else {
            selectedCnae = new ArrayList();
        }
        return listCnaes;
    }

    public void setListCnaes(Map<String, Integer> listCnaes) {
        this.listCnaes = listCnaes;
    }

    public Map<String, Integer> getListConvencaos() {
        if (listConvencoes == null) {
            listConvencoes = new LinkedHashMap<>();
            EnviarArquivosDB enviarArquivosDB = new EnviarArquivosDBToplink();
            List<Convencao> list = enviarArquivosDB.listaConvencao();
            for (int i = 0; i < list.size(); i++) {
                listConvencoes.put(list.get(i).getDescricao(), list.get(i).getId());
            }
        }
        return listConvencoes;
    }

    public void setListConvencaos(Map<String, Integer> listConvencoes) {
        this.listConvencoes = listConvencoes;
    }

    public Map<String, Integer> getListGrupoCidades() {
        listGrupoCidades = null;
        if (!selectedConvencao.isEmpty()) {
            listGrupoCidades = new LinkedHashMap<>();
            String ids = inIdConvencao();
            if (!ids.isEmpty()) {
                EnviarArquivosDB enviarArquivosDB = new EnviarArquivosDBToplink();
                List<GrupoCidade> list = enviarArquivosDB.listaGrupoCidadePorConvencao(ids);
                for (int i = 0; i < list.size(); i++) {
                    listGrupoCidades.put(list.get(i).getDescricao(), list.get(i).getId());
                }
            }
        } else {
            selectedGrupoCidades = new ArrayList();
        }
        return listGrupoCidades;
    }

    public void setListGrupoCidades(HashMap<String, Integer> listGrupoCidades) {
        this.listGrupoCidades = listGrupoCidades;
    }

    public String inIdConvencao() {
        String ids = "";
        if (selectedConvencao != null) {
            for (int i = 0; i < selectedConvencao.size(); i++) {
                if (ids.isEmpty()) {
                    ids = "" + selectedConvencao.get(i);
                } else {
                    ids += "," + selectedConvencao.get(i);
                }
            }
        }
        return ids;
    }

    public String inIdGruposCidade() {
        String ids = "";
        if (selectedGrupoCidades != null) {
            for (int i = 0; i < selectedGrupoCidades.size(); i++) {
                if (ids.isEmpty()) {
                    ids = "" + selectedGrupoCidades.get(i);
                } else {
                    ids += "," + selectedGrupoCidades.get(i);
                }
            }
        }
        return ids;
    }

    public String inIdCnaes() {
        String ids = "";
        if (selectedCnae != null) {
            for (int i = 0; i < selectedCnae.size(); i++) {
                if (ids.isEmpty()) {
                    ids = "" + selectedCnae.get(i);
                } else {
                    ids += "," + selectedCnae.get(i);
                }
            }

        }
        return ids;
    }

    public String inIdCidadeBase() {
        String ids = "";
        if (selectedCidadeBase != null) {
            for (int i = 0; i < selectedCidadeBase.size(); i++) {
                if (ids.isEmpty()) {
                    ids = "" + selectedCidadeBase.get(i);
                } else {
                    ids += "," + selectedCidadeBase.get(i);
                }
            }

        }
        return ids;
    }

    public List getSelectedConvencao() {
        return selectedConvencao;
    }

    public void setSelectedConvencao(List selectedConvencao) {
        this.selectedConvencao = selectedConvencao;
    }

    public List getSelectedGrupoCidades() {
        return selectedGrupoCidades;
    }

    public void setSelectedGrupoCidades(List selectedGrupoCidades) {
        this.selectedGrupoCidades = selectedGrupoCidades;
    }

    public List getSelectedCnae() {
        return selectedCnae;
    }

    public void setSelectedCnae(List selectedCnae) {
        this.selectedCnae = selectedCnae;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Map<String, Integer> getListCidadeBase() {
        if (listCidadeBase == null || listCidadeBase.isEmpty()) {
            listCidadeBase = new LinkedHashMap<>();
            GrupoCidadesDB grupoCidadesDB = new GrupoCidadesDBToplink();
            List<Cidade> list = grupoCidadesDB.pesquisaCidadesBase();
            for (int i = 0; i < list.size(); i++) {
                listCidadeBase.put(list.get(i).getCidade(), list.get(i).getId());
            }
        }
        return listCidadeBase;
    }

    public void setListCidadeBase(Map<String, Integer> listCidadesBase) {
        this.listCidadeBase = listCidadesBase;
    }

    public List getSelectedCidadeBase() {
        return selectedCidadeBase;
    }

    public void setSelectedCidadeBase(List selectedCidadeBase) {
        this.selectedCidadeBase = selectedCidadeBase;
    }

    public List<SelectItem> getListServicos() {
        if (listSelectItem[1] == null) {
            listSelectItem[1] = new ArrayList<>();
        }
        if (listSelectItem[1].isEmpty()) {
            ServicoRotinaDB srdb = new ServicoRotinaDBToplink();
            List<Servicos> select = srdb.pesquisaTodosServicosComRotinas(278);
            for (int i = 0; i < select.size(); i++) {
                listSelectItem[1].add(
                        new SelectItem(
                                i,
                                select.get(i).getDescricao(),
                                Integer.toString(select.get(i).getId())
                        )
                );
            }
        }
        return listSelectItem[1];
    }

    public void setListServicos(List<SelectItem> listServicos) {
        this.listSelectItem[1] = listServicos;
    }

    public List<SelectItem> getListTipoServico() {
        if (listSelectItem[2].isEmpty()) {
            List<TipoServico> select = (new Dao()).list(new TipoServico(), true);
            for (int i = 0; i < select.size(); i++) {
                if (select.get(i).getId() == 1) {
                    index[2] = i;
                }
                listSelectItem[2].add(new SelectItem(
                        i,
                        select.get(i).getDescricao(),
                        Integer.toString(select.get(i).getId()))
                );
            }
        }
        return listSelectItem[2];
    }

    public void setListTipoServico(List<SelectItem> listTipoServico) {
        this.listSelectItem[2] = listTipoServico;
    }

    public String[] getReferencia() {
        return referencia;
    }

    public void setReferencia(String[] referencia) {
        this.referencia = referencia;
    }

    public String[] getTipo() {
        return tipo;
    }

    public void setTipo(String[] tipo) {
        this.tipo = tipo;
    }

    public List<ParametroComparativoArrecadacao> getParametroComparativoArrecadacaos() {
        return parametroComparativoArrecadacaos;
    }

    public void setParametroComparativoArrecadacaos(List<ParametroComparativoArrecadacao> parametroComparativoArrecadacaos) {
        this.parametroComparativoArrecadacaos = parametroComparativoArrecadacaos;
    }

    public Integer getPercentual() {
        return percentual;
    }

    public void setPercentual(Integer percentual) {
        this.percentual = percentual;
    }

    public String getPercentualString() {
        if (percentual == null) {
            return "0";
        }
        return Integer.toString(percentual);
    }

    public void setPercentualString(String percentualString) {
        this.percentual = Integer.parseInt(percentualString);
    }

    public void defineTipo(String tipo) {
        JuridicaBean juridicaBean = new JuridicaBean();
        GenericaSessao.put("tipo", tipo);
        switch (tipo) {
            case "empresa":
                juridicaBean.pesquisaTodosEAtivos();
                break;
            case "contabilidade":
                juridicaBean.pesquisaSomenteContabilidades();
                break;
        }
    }

}
