package br.com.rtools.relatorios.beans;

import br.com.rtools.associativo.Categoria;
import br.com.rtools.financeiro.GrupoFinanceiro;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.SubGrupoFinanceiro;
import br.com.rtools.financeiro.db.FinanceiroDBToplink;
import br.com.rtools.relatorios.RelatorioOrdem;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.relatorios.dao.RelatorioDao;
import br.com.rtools.relatorios.dao.RelatorioOrdemDao;
import br.com.rtools.relatorios.dao.RelatorioTabelaPrecosDao;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.Filters;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Jasper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class RelatorioTabelaPrecosBean implements Serializable {

    private List<Filters> listFilters;

    private Map<String, Integer> listServicos;
    private List selectedServicos;

    private List<SelectItem> listRelatorio;
    private Integer idRelatorio;

    private List<SelectItem> listRelatorioOrdem;
    private Integer idRelatorioOrdem;

    private List<SelectItem> listGrupoFinanceiro;
    private Integer idGrupoFinanceiro;

    private Map<String, Integer> listSubGrupoFinanceiro;
    private List selectedSubGrupoFinanceiro;

    private Integer[] idade;

    @PostConstruct
    public void init() {
        listFilters = new ArrayList();

        listServicos = null;
        selectedServicos = new ArrayList();

        listRelatorio = new ArrayList<>();
        idRelatorio = null;

        listGrupoFinanceiro = new ArrayList<>();
        idGrupoFinanceiro = null;

        listSubGrupoFinanceiro = null;
        selectedSubGrupoFinanceiro = new ArrayList<>();

        listRelatorioOrdem = new ArrayList<>();
        idRelatorioOrdem = null;

        idade = new Integer[2];
        idade[0] = null;
        idade[1] = null;
        loadListaFiltro();
        loadRelatorio();
        loadRelatorioOrdem();
        loadServicos();
        loadGrupoFinanceiro();
        loadSubGrupoFinanceiro();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("relatorioTabelaPrecosBean");
    }

    public void print() {
        Relatorios r = null;
        if (!listRelatorio.isEmpty()) {
            RelatorioDao rgdb = new RelatorioDao();
            r = rgdb.pesquisaRelatorios(idRelatorio);
        }
        if (r == null) {
            return;
        }
        String order = "";
        String detalheRelatorio = "";
        List<TabelaServicosPaisagem> tsps = new ArrayList<>();
        List list = new RelatorioTabelaPrecosDao().find(inIdServicos(), inIdSubGrupoFinanceiro());
        if (list.isEmpty()) {
            GenericaMensagem.warn("Mensagem", "Nenhum registro encontrado!");
            return;
        }
        if (idRelatorio == 51) {
            String grupo_descricao = "";
            String subgrupo_descricao = "";
            String servico_descricao = "";
            String idade = "";
            Boolean grupo = false;
            Boolean subgrupo = false;
            Boolean add = false;
            TabelaServicosPaisagem tsp = new TabelaServicosPaisagem();
            int count_categoria = new Dao().list(new Categoria()).size();
            int count = 0;
            for (int i = 0; i < list.size(); i++) {
                List object = (List) list.get(i);
                if (!grupo_descricao.equals((String) object.get(0))) {
                    add = true;
                    tsp = new TabelaServicosPaisagem();
                    tsp.setGrupo_descricao(grupo_descricao);
                    tsp.setSubgrupo_descricao(subgrupo_descricao);
                    subgrupo_descricao = (String) object.get(1);
                    grupo_descricao = (String) object.get(0);
                    tsp.setServico_id(object.get(2));
                    tsp.setServico_descricao(object.get(3));
                    servico_descricao = (String) object.get(3);
                    idade = (String) object.get(6);
                    tsp.setIdade(object.get(6));
                    tsp.setValor_cheio(object.get(7));
                    grupo = true;
                    subgrupo = true;
                    if (tsp.getCategoria1() == null) {
                        count++;
                        tsp.setCategoria1(object.get(5));
                        tsp.setValor1(object.get(7));
                    } else if (tsp.getCategoria2() == null) {
                        count++;
                        tsp.setCategoria2(object.get(5));
                        tsp.setValor2(object.get(7));
                    } else if (tsp.getCategoria3() == null) {
                        count++;
                        tsp.setCategoria3(object.get(5));
                        tsp.setValor3(object.get(7));
                    } else if (tsp.getCategoria4() == null) {
                        count++;
                        tsp.setCategoria4(object.get(5));
                        tsp.setValor4(object.get(7));
                    } else if (tsp.getCategoria5() == null) {
                        count++;
                        tsp.setCategoria5(object.get(5));
                        tsp.setValor5(object.get(7));
                    } else if (tsp.getCategoria6() == null) {
                        count++;
                        tsp.setCategoria6(object.get(5));
                        tsp.setValor6(object.get(7));
                    } else if (tsp.getCategoria7() == null) {
                        count++;
                        tsp.setCategoria7(object.get(5));
                        tsp.setValor7(object.get(7));
                    } else if (tsp.getCategoria8() == null) {
                        count++;
                        tsp.setCategoria8(object.get(5));
                        tsp.setValor8(object.get(7));
                    }
                } else if (!subgrupo_descricao.equals((String) object.get(1))) {
                    add = true;
                    tsp = new TabelaServicosPaisagem();
                    tsp.setGrupo_descricao(grupo_descricao);
                    tsp.setSubgrupo_descricao(subgrupo_descricao);
                    subgrupo_descricao = (String) object.get(1);
                    tsp.setServico_id(object.get(2));
                    tsp.setServico_descricao(object.get(3));
                    servico_descricao = (String) object.get(3);
                    tsp.setIdade(object.get(6));
                    idade = (String) object.get(6);
                    tsp.setValor_cheio(object.get(7));
                    if (tsp.getCategoria1() == null) {
                        count++;
                        tsp.setCategoria1(object.get(5));
                        tsp.setValor1(object.get(7));
                    } else if (tsp.getCategoria2() == null) {
                        count++;
                        tsp.setCategoria2(object.get(5));
                        tsp.setValor2(object.get(7));
                    } else if (tsp.getCategoria3() == null) {
                        count++;
                        tsp.setCategoria3(object.get(5));
                        tsp.setValor3(object.get(7));
                    } else if (tsp.getCategoria4() == null) {
                        count++;
                        tsp.setCategoria4(object.get(5));
                        tsp.setValor4(object.get(7));
                    } else if (tsp.getCategoria5() == null) {
                        count++;
                        tsp.setCategoria5(object.get(5));
                        tsp.setValor5(object.get(7));
                    } else if (tsp.getCategoria6() == null) {
                        count++;
                        tsp.setCategoria6(object.get(5));
                        tsp.setValor6(object.get(7));
                    } else if (tsp.getCategoria7() == null) {
                        count++;
                        tsp.setCategoria7(object.get(5));
                        tsp.setValor7(object.get(7));
                    } else if (tsp.getCategoria8() == null) {
                        count++;
                        tsp.setCategoria8(object.get(5));
                        tsp.setValor8(object.get(7));
                    }
                } else if (!servico_descricao.equals((String) object.get(3))) {
                    add = true;
                    tsp = new TabelaServicosPaisagem();
                    tsp.setGrupo_descricao(grupo_descricao);
                    tsp.setSubgrupo_descricao(subgrupo_descricao);
                    subgrupo_descricao = (String) object.get(1);
                    tsp.setServico_id(object.get(2));
                    tsp.setServico_descricao(object.get(3));
                    servico_descricao = (String) object.get(3);
                    tsp.setIdade(object.get(6));
                    tsp.setValor_cheio(object.get(7));
                    idade = (String) object.get(6);
                    if (tsp.getCategoria1() == null) {
                        count++;
                        tsp.setCategoria1(object.get(5));
                        tsp.setValor1(object.get(7));
                    } else if (tsp.getCategoria2() == null) {
                        count++;
                        tsp.setCategoria2(object.get(5));
                        tsp.setValor2(object.get(7));
                    } else if (tsp.getCategoria3() == null) {
                        count++;
                        tsp.setCategoria3(object.get(5));
                        tsp.setValor3(object.get(7));
                    } else if (tsp.getCategoria4() == null) {
                        count++;
                        tsp.setCategoria4(object.get(5));
                        tsp.setValor4(object.get(7));
                    } else if (tsp.getCategoria5() == null) {
                        count++;
                        tsp.setCategoria5(object.get(5));
                        tsp.setValor5(object.get(7));
                    } else if (tsp.getCategoria6() == null) {
                        count++;
                        tsp.setCategoria6(object.get(5));
                        tsp.setValor6(object.get(7));
                    } else if (tsp.getCategoria7() == null) {
                        count++;
                        tsp.setCategoria7(object.get(5));
                        tsp.setValor7(object.get(7));
                    } else if (tsp.getCategoria8() == null) {
                        count++;
                        tsp.setCategoria8(object.get(5));
                        tsp.setValor8(object.get(7));
                    }
                } else {
                    add = false;
                    tsp.setGrupo_descricao(grupo_descricao);
                    tsp.setSubgrupo_descricao(subgrupo_descricao);
                    tsp.setServico_id(object.get(2));
                    tsp.setServico_descricao(servico_descricao);
                    tsp.setIdade(object.get(6));
                    tsp.setValor_cheio(object.get(7));
                    if (tsp.getCategoria1() == null) {
                        count++;
                        tsp.setCategoria1(object.get(5));
                        tsp.setValor1(object.get(7));
                    } else if (tsp.getCategoria2() == null) {
                        count++;
                        tsp.setCategoria2(object.get(5));
                        tsp.setValor2(object.get(7));
                    } else if (tsp.getCategoria3() == null) {
                        count++;
                        tsp.setCategoria3(object.get(5));
                        tsp.setValor3(object.get(7));
                    } else if (tsp.getCategoria4() == null) {
                        count++;
                        tsp.setCategoria4(object.get(5));
                        tsp.setValor4(object.get(7));
                    } else if (tsp.getCategoria5() == null) {
                        count++;
                        tsp.setCategoria5(object.get(5));
                        tsp.setValor5(object.get(7));
                    } else if (tsp.getCategoria6() == null) {
                        count++;
                        tsp.setCategoria6(object.get(5));
                        tsp.setValor6(object.get(7));
                    } else if (tsp.getCategoria7() == null) {
                        count++;
                        tsp.setCategoria7(object.get(5));
                        tsp.setValor7(object.get(7));
                    } else if (tsp.getCategoria8() == null) {
                        count++;
                        tsp.setCategoria8(object.get(5));
                        tsp.setValor8(object.get(7));
                    }
                }
                if (count == count_categoria) {
                    tsps.add(tsp);
                    count = 0;
                }
            }
        } else {
            for (int i = 0; i < list.size(); i++) {
                List object = (List) list.get(i);
                TabelaServicosPaisagem tsp = new TabelaServicosPaisagem();
                tsp.setGrupo_descricao(object.get(0));
                tsp.setSubgrupo_descricao(object.get(1));
                tsp.setServico_id(object.get(2));
                tsp.setServico_descricao(object.get(3));
                tsp.setCategoria_id(object.get(4));
                tsp.setCategoria_descricao(object.get(5));
                tsp.setIdade(object.get(6));
                tsp.setValor_cheio(object.get(7));
                tsp.setValor_final(object.get(8));
                tsps.add(tsp);
            }
        }

        Jasper.printReports(r.getJasper(), r.getNome(), (Collection) tsps);
    }

    // LOAD
    public void loadRelatorio() {
        if (listRelatorio.isEmpty()) {
            List<Relatorios> list = (List<Relatorios>) new RelatorioDao().pesquisaTipoRelatorio(325);
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    idRelatorio = list.get(i).getId();
                }
                listRelatorio.add(new SelectItem(list.get(i).getId(), list.get(i).getNome()));
            }
            loadRelatorioOrdem();
        }
    }

    public void loadRelatorioOrdem() {
        if (idRelatorio != null) {
            listRelatorioOrdem.clear();
            RelatorioOrdemDao relatorioOrdemDao = new RelatorioOrdemDao();
            List<RelatorioOrdem> list = relatorioOrdemDao.findAllByRelatorio(idRelatorio);
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    idRelatorioOrdem = list.get(i).getId();
                }
                listRelatorioOrdem.add(new SelectItem(list.get(i).getId(), list.get(i).getNome()));
            }
        }
    }

    public void loadGrupoFinanceiro() {
        listGrupoFinanceiro.clear();
        idGrupoFinanceiro = null;
        List<GrupoFinanceiro> list = new Dao().list(new GrupoFinanceiro(), true);
        listGrupoFinanceiro.add(new SelectItem(null, "Selecionar"));
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                idGrupoFinanceiro = list.get(i).getId();
            }
            listGrupoFinanceiro.add(new SelectItem(list.get(i).getId(), list.get(i).getDescricao()));
        }
    }

    public void loadSubGrupoFinanceiro() {
        listSubGrupoFinanceiro = null;
        selectedSubGrupoFinanceiro = new ArrayList();
        selectedServicos = new ArrayList<>();
        if (idGrupoFinanceiro != null && listSubGrupoFinanceiro == null) {
            listSubGrupoFinanceiro = new HashMap<>();
            FinanceiroDBToplink fd = new FinanceiroDBToplink();
            List<SubGrupoFinanceiro> list = fd.listaSubGrupo(idGrupoFinanceiro);
            listSubGrupoFinanceiro.put("Selecionar", null);
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    listSubGrupoFinanceiro.put(list.get(i).getDescricao(), list.get(i).getId());
                }
            }
        }
    }

    public void loadServicos() {
        listServicos = new HashMap<>();
        selectedServicos = new ArrayList<>();
        List<Servicos> list = new Dao().list(new Servicos(), true);
        listServicos.put("Selecionar", null);
        for (int i = 0; i < list.size(); i++) {
            listServicos.put(list.get(i).getDescricao(), list.get(i).getId());
        }
    }

    public void loadListaFiltro() {
        listFilters.clear();
        listFilters.add(new Filters("servicos", "Serviços", false));
        listFilters.add(new Filters("subgrupo_categoria", "Subgrupo Categorua", false));

    }

    // LISTENERS
    public void clear(Filters filter) {
        filter.setActive(!filter.getActive());
        switch (filter.getKey()) {
            case "servicos":
                loadServicos();
                break;
            case "subgrupo":
                // load);
                break;
        }
    }

    // TRATAMENTO
    public String inIdSubGrupoFinanceiro() {
        String ids = null;
        if (selectedSubGrupoFinanceiro != null) {
            for (int i = 0; i < selectedSubGrupoFinanceiro.size(); i++) {
                if (ids == null) {
                    ids = "" + selectedSubGrupoFinanceiro.get(i);
                } else {
                    ids += "," + selectedSubGrupoFinanceiro.get(i);
                }
            }
        }
        return ids;
    }

    public String inIdServicos() {
        String ids = null;
        if (selectedServicos != null) {
            for (int i = 0; i < selectedServicos.size(); i++) {
                if (ids == null) {
                    ids = "" + selectedServicos.get(i);
                } else {
                    ids += "," + selectedServicos.get(i);
                }
            }
        }
        return ids;
    }

    // GETTERS AND SETTERS
    public List<SelectItem> getListRelatorios() {
        return listRelatorio;
    }

    public void setListRelatorios(List<SelectItem> listRelatorio) {
        this.listRelatorio = listRelatorio;
    }

    public List<SelectItem> getListRelatorioOrdem() {
        return listRelatorioOrdem;
    }

    public void setListRelatorioOrdem(List<SelectItem> listRelatorioOrdem) {
        this.listRelatorioOrdem = listRelatorioOrdem;
    }

    public List<Filters> getListFilters() {
        return listFilters;
    }

    public void setListFilters(List<Filters> listFilters) {
        this.listFilters = listFilters;
    }

    public Map<String, Integer> getListServicos() {
        return listServicos;
    }

    public void setListServicos(Map<String, Integer> listServicos) {
        this.listServicos = listServicos;
    }

    public List getSelectedServicos() {
        return selectedServicos;
    }

    public void setSelectedServicos(List selectedServicos) {
        this.selectedServicos = selectedServicos;
    }

    public Map<String, Integer> getListSubGrupoFinanceiro() {
        return listSubGrupoFinanceiro;
    }

    public void setListSubGrupoFinanceiro(Map<String, Integer> listSubGrupoFinanceiro) {
        this.listSubGrupoFinanceiro = listSubGrupoFinanceiro;
    }

    public List getSelectedSubGrupoFinanceiro() {
        return selectedSubGrupoFinanceiro;
    }

    public void setSelectedSubGrupoFinanceiro(List selectedSubGrupoFinanceiro) {
        this.selectedSubGrupoFinanceiro = selectedSubGrupoFinanceiro;
    }

    public List<SelectItem> getListRelatorio() {
        return listRelatorio;
    }

    public void setListRelatorio(List<SelectItem> listRelatorio) {
        this.listRelatorio = listRelatorio;
    }

    public Integer getIdRelatorio() {
        return idRelatorio;
    }

    public void setIdRelatorio(Integer idRelatorio) {
        this.idRelatorio = idRelatorio;
    }

    public Integer getIdRelatorioOrdem() {
        return idRelatorioOrdem;
    }

    public void setIdRelatorioOrdem(Integer idRelatorioOrdem) {
        this.idRelatorioOrdem = idRelatorioOrdem;
    }

    public Integer[] getIdade() {
        return idade;
    }

    public void setIdade(Integer[] idade) {
        this.idade = idade;
    }

    public List<SelectItem> getListGrupoFinanceiro() {
        return listGrupoFinanceiro;
    }

    public void setListGrupo(List<SelectItem> listGrupoFinanceiro) {
        this.listGrupoFinanceiro = listGrupoFinanceiro;
    }

    public Integer getIdGrupoFinanceiro() {
        return idGrupoFinanceiro;
    }

    public void setIdGrupo(Integer idGrupoFinanceiro) {
        this.idGrupoFinanceiro = idGrupoFinanceiro;

    }

    public class TabelaServicosPaisagem {

        private Object grupo_descricao;
        private Object subgrupo_descricao;
        private Object servico_id;
        private Object servico_descricao;
        private Object categoria_id;
        private Object categoria_descricao;
        private Object idade;
        private Object valor_cheio;
        private Object valor_final;
        private Object percentual_desconto;
        private Object categoria1;
        private Object valor1;
        private Object categoria2;
        private Object valor2;
        private Object categoria3;
        private Object valor3;
        private Object categoria4;
        private Object valor4;
        private Object categoria5;
        private Object valor5;
        private Object categoria6;
        private Object valor6;
        private Object categoria7;
        private Object valor7;
        private Object categoria8;
        private Object valor8;

        public TabelaServicosPaisagem() {
            this.grupo_descricao = null;
            this.subgrupo_descricao = null;
            this.servico_id = null;
            this.servico_descricao = null;
            this.categoria_id = null;
            this.categoria_descricao = null;
            this.idade = null;
            this.valor_cheio = null;
            this.valor_final = null;
            this.percentual_desconto = null;
            this.categoria1 = null;
            this.valor1 = null;
            this.categoria2 = null;
            this.valor2 = null;
            this.categoria3 = null;
            this.valor3 = null;
            this.categoria4 = null;
            this.valor4 = null;
            this.categoria5 = null;
            this.valor5 = null;
            this.categoria6 = null;
            this.valor6 = null;
            this.categoria7 = null;
            this.valor7 = null;
            this.categoria8 = null;
            this.valor8 = null;
        }

        public TabelaServicosPaisagem(Object grupo_descricao, Object subgrupo_descricao, Object servico_id, Object servico_descricao, Object categoria_id, Object categoria_descricao, Object idade, Object valor_cheio, Object valor_final, Object percentual_desconto, Object categoria1, Object valor1, Object categoria2, Object valor2, Object categoria3, Object valor3, Object categoria4, Object valor4, Object categoria5, Object valor5, Object categoria6, Object valor6, Object categoria7, Object valor7, Object categoria8, Object valor8) {
            this.grupo_descricao = grupo_descricao;
            this.subgrupo_descricao = subgrupo_descricao;
            this.servico_id = servico_id;
            this.servico_descricao = servico_descricao;
            this.categoria_id = categoria_id;
            this.categoria_descricao = categoria_descricao;
            this.idade = idade;
            this.valor_cheio = valor_cheio;
            this.valor_final = valor_final;
            this.percentual_desconto = percentual_desconto;
            this.categoria1 = categoria1;
            this.valor1 = valor1;
            this.categoria2 = categoria2;
            this.valor2 = valor2;
            this.categoria3 = categoria3;
            this.valor3 = valor3;
            this.categoria4 = categoria4;
            this.valor4 = valor4;
            this.categoria5 = categoria5;
            this.valor5 = valor5;
            this.categoria6 = categoria6;
            this.valor6 = valor6;
            this.categoria7 = categoria7;
            this.valor7 = valor7;
            this.categoria8 = categoria8;
            this.valor8 = valor8;
        }

        public Object getGrupo_descricao() {
            return grupo_descricao;
        }

        public void setGrupo_descricao(Object grupo_descricao) {
            this.grupo_descricao = grupo_descricao;
        }

        public Object getSubgrupo_descricao() {
            return subgrupo_descricao;
        }

        public void setSubgrupo_descricao(Object subgrupo_descricao) {
            this.subgrupo_descricao = subgrupo_descricao;
        }

        public Object getServico_id() {
            return servico_id;
        }

        public void setServico_id(Object servico_id) {
            this.servico_id = servico_id;
        }

        public Object getServico_descricao() {
            return servico_descricao;
        }

        public void setServico_descricao(Object servico_descricao) {
            this.servico_descricao = servico_descricao;
        }

        public Object getCategoria_id() {
            return categoria_id;
        }

        public void setCategoria_id(Object categoria_id) {
            this.categoria_id = categoria_id;
        }

        public Object getCategoria_descricao() {
            return categoria_descricao;
        }

        public void setCategoria_descricao(Object categoria_descricao) {
            this.categoria_descricao = categoria_descricao;
        }

        public Object getIdade() {
            return idade;
        }

        public void setIdade(Object idade) {
            this.idade = idade;
        }

        public Object getValor_cheio() {
            return valor_cheio;
        }

        public void setValor_cheio(Object valor_cheio) {
            this.valor_cheio = valor_cheio;
        }

        public Object getValor_final() {
            return valor_final;
        }

        public void setValor_final(Object valor_final) {
            this.valor_final = valor_final;
        }

        public Object getPercentual_desconto() {
            return percentual_desconto;
        }

        public void setPercentual_desconto(Object percentual_desconto) {
            this.percentual_desconto = percentual_desconto;
        }

        public Object getCategoria1() {
            return categoria1;
        }

        public void setCategoria1(Object categoria1) {
            this.categoria1 = categoria1;
        }

        public Object getValor1() {
            return valor1;
        }

        public void setValor1(Object valor1) {
            this.valor1 = valor1;
        }

        public Object getCategoria2() {
            return categoria2;
        }

        public void setCategoria2(Object categoria2) {
            this.categoria2 = categoria2;
        }

        public Object getValor2() {
            return valor2;
        }

        public void setValor2(Object valor2) {
            this.valor2 = valor2;
        }

        public Object getCategoria3() {
            return categoria3;
        }

        public void setCategoria3(Object categoria3) {
            this.categoria3 = categoria3;
        }

        public Object getValor3() {
            return valor3;
        }

        public void setValor3(Object valor3) {
            this.valor3 = valor3;
        }

        public Object getCategoria4() {
            return categoria4;
        }

        public void setCategoria4(Object categoria4) {
            this.categoria4 = categoria4;
        }

        public Object getValor4() {
            return valor4;
        }

        public void setValor4(Object valor4) {
            this.valor4 = valor4;
        }

        public Object getCategoria5() {
            return categoria5;
        }

        public void setCategoria5(Object categoria5) {
            this.categoria5 = categoria5;
        }

        public Object getValor5() {
            return valor5;
        }

        public void setValor5(Object valor5) {
            this.valor5 = valor5;
        }

        public Object getCategoria6() {
            return categoria6;
        }

        public void setCategoria6(Object categoria6) {
            this.categoria6 = categoria6;
        }

        public Object getValor6() {
            return valor6;
        }

        public void setValor6(Object valor6) {
            this.valor6 = valor6;
        }

        public Object getCategoria7() {
            return categoria7;
        }

        public void setCategoria7(Object categoria7) {
            this.categoria7 = categoria7;
        }

        public Object getValor7() {
            return valor7;
        }

        public void setValor7(Object valor7) {
            this.valor7 = valor7;
        }

        public Object getValor8() {
            return valor8;
        }

        public void setValor8(Object valor8) {
            this.valor8 = valor8;
        }

        public Object getCategoria8() {
            return categoria8;
        }

        public void setCategoria8(Object categoria8) {
            this.categoria8 = categoria8;
        }

    }
}
