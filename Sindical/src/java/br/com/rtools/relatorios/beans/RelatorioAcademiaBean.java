package br.com.rtools.relatorios.beans;

import br.com.rtools.academia.AcademiaServicoValor;
import br.com.rtools.academia.dao.AcademiaDao;
import br.com.rtools.arrecadacao.dao.RaisDao;
import br.com.rtools.endereco.Cidade;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.relatorios.db.RelatorioGenericoDB;
import br.com.rtools.relatorios.db.RelatorioGenericoDBToplink;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Jasper;
import br.com.rtools.utilitarios.PF;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;

@ManagedBean
@SessionScoped
public class RelatorioAcademiaBean implements Serializable {

    private Pessoa aluno;
    private Pessoa responsavel;
    private List selectedModalidades;
    private List<SelectItem>[] listSelectItem;
    private Map<String, Integer> listModalidades;
    private Boolean[] filtro;
    private Date dataInicial;
    private Date dataFinal;
    private Integer[] index;
    private String tipoRelatorio;
    private String tipo;
    private String indexAccordion;
    private String order;
    private String sexo;

    @PostConstruct
    public void init() {
        filtro = new Boolean[6];
        filtro[0] = false; // MODALIDADE
        filtro[1] = false; // PERÍODO EMISSÃO
        filtro[2] = false; // RESPONSÁVEL
        filtro[3] = false; // ALUNO
        filtro[4] = false; // SEXO
        filtro[5] = false; // ORDER
        listSelectItem = new ArrayList[2];
        listSelectItem[0] = new ArrayList<>();
        listSelectItem[1] = new ArrayList<>();
        dataInicial = DataHoje.dataHoje();
        dataFinal = DataHoje.dataHoje();
        index = new Integer[2];
        index[0] = 0;
        index[1] = 0;
        tipoRelatorio = "Simples";
        indexAccordion = "Simples";
        order = "";
        aluno = new Pessoa();
        responsavel = new Pessoa();
        sexo = "";
        tipo = "todos";
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("relatorioAcademiaBean");
        GenericaSessao.remove("pessoaPesquisa");
        GenericaSessao.remove("fisicaPesquisa");
        GenericaSessao.remove("juridicaPesquisa");
        GenericaSessao.remove("tipoPesquisaPessoaJuridica");
    }

    public void print() {
        print(0);
    }

    public void print(int tcase) {
        Relatorios relatorios = null;
        if (!getListTipoRelatorios().isEmpty()) {
            RelatorioGenericoDB rgdb = new RelatorioGenericoDBToplink();
            relatorios = rgdb.pesquisaRelatorios(index[0]);
        } else {
            GenericaMensagem.info("Sistema", "Nenhum relatório encontrado!");
            return;
        }
        if (relatorios == null) {
            return;
        }
        String order = "";
        String detalheRelatorio = "";
        if (true) {
            RaisDao raisDao = new RaisDao();
            Integer idResponsavel = null;
            Integer idAluno = null;
            String pIStringI = "";
            String pFStringI = "";
            String referencia = "";
            String dReferencia = "";
            String inIdModalidades = inIdModalidades();
            List listDetalhePesquisa = new ArrayList();
            if (filtro[1]) {
                pIStringI = DataHoje.converteData(dataInicial);
                pFStringI = DataHoje.converteData(dataFinal);
                listDetalhePesquisa.add(" Período de Emissão entre " + pIStringI + " e " + pFStringI);
            }
            if (!dReferencia.isEmpty()) {
                listDetalhePesquisa.add(" Período convenção: " + dReferencia + "");
            }
            if (aluno.getId() != -1) {
                idAluno = aluno.getId();
                listDetalhePesquisa.add(" Empresa por Física CPF: " + aluno.getDocumento() + " - " + aluno.getNome());
            }
            if (responsavel.getId() != -1) {
                idResponsavel = responsavel.getId();
                listDetalhePesquisa.add(" Escritório por Responsável: " + responsavel.getDocumento() + " - " + responsavel.getNome());
            }
            String orderString = "";
            AcademiaDao academiaDao = new AcademiaDao();
            List list = academiaDao.filtroRelatorio(relatorios, pIStringI, pFStringI, idResponsavel, idAluno, inIdModalidades, sexo, orderString);
            if (list.isEmpty()) {
                GenericaMensagem.info("Sistema", "Não existem registros para o relatório selecionado");
                return;
            }

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
            String dte = "";
            String dtd = "";
            String dta = "";
//            for (Object list1 : list) {
//                if (raisEnviadas) {
//                    dt = GenericaString.converterNullToString(((List) list1).get(30));
//                    dte = GenericaString.converterNullToString(((List) list1).get(4));
//                    dta = GenericaString.converterNullToString(((List) list1).get(13));
//                    dtd = GenericaString.converterNullToString(((List) list1).get(11));
//                    if (!dt.isEmpty()) {
//                        dt = DataHoje.converteData(DataHoje.converteDateSqlToDate(dt));
//                    }
//                    if (!dte.isEmpty()) {
//                        dte = DataHoje.converteData(DataHoje.converteDateSqlToDate(dte));
//                    }
//                    if (!dta.isEmpty()) {
//                        dta = DataHoje.converteData(DataHoje.converteDateSqlToDate(dta));
//                    }
//                    if (!dtd.isEmpty()) {
//                        dtd = DataHoje.converteData(DataHoje.converteDateSqlToDate(dtd));
//                    }
//                    pr = new ParametroRaisRelatorio(
//                            detalheRelatorio,
//                            AnaliseString.converteNullString(((List) list1).get(0)),
//                            AnaliseString.converteNullString(((List) list1).get(1)),
//                            dte,
//                            AnaliseString.converteNullString(((List) list1).get(3)),
//                            AnaliseString.converteNullString(((List) list1).get(5)),
//                            AnaliseString.converteNullString(((List) list1).get(6)),
//                            AnaliseString.converteNullString(((List) list1).get(7)),
//                            AnaliseString.converteNullString(((List) list1).get(8)),
//                            AnaliseString.converteNullString(((List) list1).get(9)),
//                            AnaliseString.converteNullString(((List) list1).get(10)),
//                            dtd,
//                            AnaliseString.converteNullString(((List) list1).get(12)),
//                            dta,
//                            AnaliseString.converteNullString(((List) list1).get(14)),
//                            AnaliseString.converteNullString(((List) list1).get(15)),
//                            AnaliseString.converteNullString(((List) list1).get(16)),
//                            AnaliseString.converteNullString(((List) list1).get(17)),
//                            AnaliseString.converteNullString(((List) list1).get(18)),
//                            AnaliseString.converteNullString(((List) list1).get(19)),
//                            AnaliseString.converteNullString(((List) list1).get(20)),
//                            AnaliseString.converteNullString(((List) list1).get(21)),
//                            AnaliseString.converteNullString(((List) list1).get(22)),
//                            AnaliseString.converteNullString(((List) list1).get(23)),
//                            AnaliseString.converteNullString(((List) list1).get(24)),
//                            AnaliseString.converteNullString(((List) list1).get(25)),
//                            AnaliseString.converteNullString(((List) list1).get(26)),
//                            AnaliseString.converteNullString(((List) list1).get(27)),
//                            AnaliseString.converteNullString(((List) list1).get(28)),
//                            AnaliseString.converteNullString(((List) list1).get(29)),
//                            AnaliseString.converteNullString(dt),
//                            AnaliseString.converteNullString(((List) list1).get(31))
//                    );
//                    parametroRaisRelatorio.add(pr);
//                } else {
//                    if (escritorios) {
//                        String quantidade = "0";
//                        try {
//                            quantidade = "" + Integer.parseInt(AnaliseString.converteNullString(((List) list1).get(4)));
//                        } catch (Exception e) {
//                            quantidade = "" + 0;
//                        }
//                        prne = new ParametroRaisNaoEnviadasRelatorio(
//                                detalheRelatorio,
//                                AnaliseString.converteNullString(((List) list1).get(0)),
//                                AnaliseString.converteNullString(((List) list1).get(1)),
//                                AnaliseString.converteNullString(((List) list1).get(2)),
//                                AnaliseString.converteNullString(((List) list1).get(3)),
//                                quantidade,
//                                AnaliseString.converteNullString(((List) list1).get(5)),
//                                AnaliseString.converteNullString(((List) list1).get(6)),
//                                AnaliseString.converteNullString(((List) list1).get(7)),
//                                AnaliseString.converteNullString(((List) list1).get(8)),
//                                AnaliseString.converteNullString(((List) list1).get(9)),
//                                AnaliseString.converteNullString(((List) list1).get(10)),
//                                AnaliseString.converteNullString(((List) list1).get(11)),
//                                AnaliseString.converteNullString(((List) list1).get(12))
//                        );
//
//                    } else {
//                        prne = new ParametroRaisNaoEnviadasRelatorio(
//                                detalheRelatorio,
//                                AnaliseString.converteNullString(((List) list1).get(0)),
//                                AnaliseString.converteNullString(((List) list1).get(1)),
//                                AnaliseString.converteNullString(((List) list1).get(2)),
//                                AnaliseString.converteNullString(((List) list1).get(3)),
//                                AnaliseString.converteNullString(((List) list1).get(4)),
//                                AnaliseString.converteNullString(((List) list1).get(5)),
//                                AnaliseString.converteNullString(((List) list1).get(6)),
//                                AnaliseString.converteNullString(((List) list1).get(7)),
//                                AnaliseString.converteNullString(((List) list1).get(8)),
//                                AnaliseString.converteNullString(((List) list1).get(9)),
//                                AnaliseString.converteNullString(((List) list1).get(10)),
//                                AnaliseString.converteNullString(((List) list1).get(11)),
//                                AnaliseString.converteNullString(((List) list1).get(12)),
//                                AnaliseString.converteNullString(((List) list1).get(13)),
//                                AnaliseString.converteNullString(((List) list1).get(14)),
//                                AnaliseString.converteNullString(((List) list1).get(15)),
//                                AnaliseString.converteNullString(((List) list1).get(16)),
//                                AnaliseString.converteNullString(((List) list1).get(17)),
//                                AnaliseString.converteNullString(((List) list1).get(18)),
//                                AnaliseString.converteNullString(((List) list1).get(19)),
//                                AnaliseString.converteNullString(((List) list1).get(20)),
//                                AnaliseString.converteNullString(((List) list1).get(21)),
//                                AnaliseString.converteNullString(((List) list1).get(22)),
//                                AnaliseString.converteNullString(((List) list1).get(23)),
//                                AnaliseString.converteNullString(((List) list1).get(24)),
//                                "0",
//                                AnaliseString.converteNullString(((List) list1).get(25)),
//                                AnaliseString.converteNullString(((List) list1).get(26))
//                        );
//                    }
//                    parametroRaisNaoEnviadasRelatorio.add(prne);
//                }
//            }
        }
        // Jasper.printReports(relatorios.getJasper(), "academia", null);
    }

    public List<SelectItem> getListTipoRelatorios() {
        if (listSelectItem[0].isEmpty()) {
            RelatorioGenericoDB db = new RelatorioGenericoDBToplink();
            List<Relatorios> list = (List<Relatorios>) db.pesquisaTipoRelatorio(-1);
            for (int i = 0; i < list.size(); i++) {
                listSelectItem[0].add(new SelectItem(list.get(i).getId(), list.get(i).getNome()));
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
            clear();
            filtro[2] = false;
            filtro[0] = false;
        }
    }

    public void selecionaDataInicial(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataInicial = DataHoje.converte(format.format(event.getObject()));
    }

    public void selecionaDataFinal(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataFinal = DataHoje.converte(format.format(event.getObject()));
    }

    public void clear() {
        if (!filtro[0]) {
            selectedModalidades = null;
        }
        if (!filtro[1]) {
            dataInicial = DataHoje.dataHoje();
            dataFinal = DataHoje.dataHoje();
        }
        if (!filtro[2]) {
            responsavel = new Pessoa();
        }
        if (!filtro[3]) {
            aluno = new Pessoa();
        }
        if (!filtro[4]) {
            sexo = "";
        }
        if (!filtro[5]) {
            order = "";
        }
    }

    public void close(String close) {
        switch (close) {
            case "modalidade":
                selectedModalidades = null;
                listModalidades = null;
                filtro[0] = false;
                break;
            case "periodoEmissao":
                filtro[1] = false;
                dataInicial = DataHoje.dataHoje();
                dataFinal = DataHoje.dataHoje();
                break;
            case "responsavel":
                responsavel = new Pessoa();
                filtro[2] = false;
                break;
            case "aluno":
                filtro[3] = false;
                aluno = new Pessoa();
                break;
            case "sexo":
                sexo = "";
                filtro[4] = false;
                break;
            case "order":
                order = "";
                filtro[5] = false;
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

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
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

    /**
     * <strong>Filtros</strong>
     * <ul>
     * <li>[0] MODALIDADE</li>
     * <li>[1] PERÍODO EMISSÃO</li>
     * <li>[2] RESPONSÁVEL</li>
     * <li>[3] ALUNO</li>
     * <li>[4] SEXO</li>
     * <li>[5] ORDENAÇÃO </li>
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

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Pessoa getAluno() {
        if (GenericaSessao.exists("fisicaPesquisa")) {
            aluno = ((Fisica) GenericaSessao.getObject("fisicaPesquisa", true)).getPessoa();
        }
        return aluno;
    }

    public void setAluno(Pessoa aluno) {
        this.aluno = aluno;
    }

    public Pessoa getResponsavel() {
        if (GenericaSessao.exists("pessoaPesquisa")) {
            responsavel = (Pessoa) GenericaSessao.getObject("pessoaPesquisa", true);
        }
        return responsavel;
    }

    public void setResponsavel(Pessoa aluno) {
        this.aluno = aluno;
    }

    public Map<String, Integer> getListModalidades() {
        if (listModalidades == null) {
            listModalidades = new HashMap<>();
            AcademiaDao academiaDao = new AcademiaDao();
            List<AcademiaServicoValor> list = academiaDao.listaServicoValorPorRotina();
            int idServicoMemoria = 0;
            int b = 0;
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (idServicoMemoria != list.get(i).getServicos().getId()) {
                        listModalidades.put(list.get(i).getServicos().getDescricao(), list.get(i).getId());
                        idServicoMemoria = list.get(i).getServicos().getId();
                        b++;
                    }
                }
            }
        }
        return listModalidades;
    }

    public void setListListModalidades(Map<String, Integer> listModalidades) {
        this.listModalidades = listModalidades;
    }

    public String inIdModalidades() {
        String ids = null;
        if (selectedModalidades != null) {
            for (int i = 0; i < selectedModalidades.size(); i++) {
                if (ids == null) {
                    ids = "" + selectedModalidades.get(i);
                } else {
                    ids += "," + selectedModalidades.get(i);
                }
            }
        }
        return ids;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List getSelectedModalidades() {
        return selectedModalidades;
    }

    public void setSelectedModalidades(List selectedModalidades) {
        this.selectedModalidades = selectedModalidades;
    }
}
