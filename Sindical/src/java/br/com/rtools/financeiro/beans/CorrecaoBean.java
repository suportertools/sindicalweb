package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.Correcao;
import br.com.rtools.financeiro.Indice;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.CorrecaoDB;
import br.com.rtools.financeiro.db.CorrecaoDBToplink;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class CorrecaoBean implements Serializable {

    private int idServicos = 0;
    private int idIndices = 0;
    private Correcao correcao;
    private List<Correcao> listaCorrecao;

    @PostConstruct
    public void init() {
        idServicos = 0;
        idIndices = 0;
        correcao = new Correcao();
        listaCorrecao = new ArrayList<Correcao>();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("correcaoBean");
    }

    public String salvar() {
        CorrecaoDB db = new CorrecaoDBToplink();
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        Servicos servico = (Servicos) di.find(new Servicos(), Integer.parseInt(getListaServico().get(idServicos).getDescription()));
        correcao.setIndice((Indice) di.find(new Indice(), Integer.parseInt(getListaIndices().get(idIndices).getDescription())));
        correcao.setServicos(servico);
        if (correcao.getId() == -1) {
            if (DataHoje.validaReferencias(correcao.getReferenciaInicial(), correcao.getReferenciaFinal())) {
                List dd = db.pesquisaRefValida(servico, correcao.getReferenciaInicial(), correcao.getReferenciaFinal());
                if (Integer.parseInt(String.valueOf((Long) dd.get(0))) == 0) {
                    if (di.save(correcao, true)) {
                        novoLog.save(
                                "ID: " + correcao.getId()
                                + " - Índice: (" + correcao.getIndice().getId() + ") "
                                + " - Serviços: (" + correcao.getServicos().getId() + ") " + correcao.getServicos().getDescricao()
                                + " - Período: " + correcao.getReferenciaInicial() + " - " + correcao.getReferenciaFinal()
                                + " - Juros Diário: " + correcao.getJurosDiarios()
                                + " - Juros 1º Mês: " + correcao.getJurosPriMes()
                                + " - Juros >= 2º Mês: " + correcao.getJurosApartir2Mes()
                                + " - Multa por Funcionário: " + correcao.getMultaPorFuncionario()
                                + " - Multa 1º Mês: " + correcao.getMultaPriMes()
                                + " - Multa >= 2º Mês: " + correcao.getMultaApartir2Mes()
                        );
                        GenericaMensagem.info("Sucesso", "Correção Salva");
                        correcao = new Correcao();
                        idIndices = 0;
                        idServicos = 0;
                    } else {
                        GenericaMensagem.warn("Erro", "Erro ao Salvar!");
                    }
                } else {
                    GenericaMensagem.warn("Validação", "Correção já existente!");
                }
            } else {
                GenericaMensagem.warn("Validação", "Referencia Invalida!");
            }
        } else if (DataHoje.validaReferencias(correcao.getReferenciaInicial(), correcao.getReferenciaFinal())) {
            Correcao c = (Correcao) di.find(correcao);
            String beforeUpdate
                    = "ID: " + c.getId()
                    + " - Índice: (" + c.getIndice().getId() + ") "
                    + " - Serviços: (" + c.getServicos().getId() + ") " + c.getServicos().getDescricao()
                    + " - Período: " + c.getReferenciaInicial() + " - " + c.getReferenciaFinal()
                    + " - Juros Diário: " + c.getJurosDiarios()
                    + " - Juros 1º Mês: " + c.getJurosPriMes()
                    + " - Juros >= 2º Mês: " + c.getJurosApartir2Mes()
                    + " - Multa por Funcionário: " + correcao.getMultaPorFuncionario()
                    + " - Multa 1º Mês: " + c.getMultaPriMes()
                    + " - Multa >= 2º Mês: " + c.getMultaApartir2Mes();
            if (di.update(correcao, true)) {
                novoLog.update(beforeUpdate,
                        "ID: " + correcao.getId()
                        + " - Índice: (" + correcao.getIndice().getId() + ") "
                        + " - Serviços: (" + correcao.getServicos().getId() + ") " + correcao.getServicos().getDescricao()
                        + " - Período: " + correcao.getReferenciaInicial() + " - " + correcao.getReferenciaFinal()
                        + " - Juros Diário: " + correcao.getJurosDiarios()
                        + " - Juros 1º Mês: " + correcao.getJurosPriMes()
                        + " - Juros >= 2º Mês: " + correcao.getJurosApartir2Mes()
                        + " - Multa por Funcionário: " + correcao.getMultaPorFuncionario()
                        + " - Multa 1º Mês: " + correcao.getMultaPriMes()
                        + " - Multa >= 2º Mês: " + correcao.getMultaApartir2Mes()
                );
                GenericaMensagem.info("Sucesso", "Correção Atualizada!");
                correcao = new Correcao();
                idIndices = 0;
                idServicos = 0;
            } else {
                GenericaMensagem.warn("Erro", "Erro ao atualizar!");
            }
        } else {
            GenericaMensagem.warn("Validação", "Referencia Invalida!");
        }
        listaCorrecao.clear();
        return null;
    }

    public String novo() {
        correcao = new Correcao();
        idIndices = 0;
        idServicos = 0;
        listaCorrecao.clear();
        return "correcao";
    }

    public String btnExcluir(Correcao co) {
        correcao = co;
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        if (di.delete(correcao, true)) {
            novoLog.delete(
                    "ID: " + correcao.getId()
                    + " - Índice: (" + correcao.getIndice().getId() + ") "
                    + " - Serviços: (" + correcao.getServicos().getId() + ") " + correcao.getServicos().getDescricao()
                    + " - Período: " + correcao.getReferenciaInicial() + " - " + correcao.getReferenciaFinal()
                    + " - Juros Diário: " + correcao.getJurosDiarios()
                    + " - Juros 1º Mês: " + correcao.getJurosPriMes()
                    + " - Juros >= 2º Mês: " + correcao.getJurosApartir2Mes()
                    + " - Multa por Funcionário: " + correcao.getMultaPorFuncionario()
                    + " - Multa 1º Mês: " + correcao.getMultaPriMes()
                    + " - Multa >= 2º Mês: " + correcao.getMultaApartir2Mes()
            );
            GenericaMensagem.info("Sucesso", "Correção Excluida");
        } else {
            GenericaMensagem.warn("Erro", "Erro ao excluir Correção!");
        }
        correcao = new Correcao();
        listaCorrecao.clear();
        return null;
    }

    public String editar(Correcao co) {
        correcao = co;
        for (int i = 0; i < getListaServico().size(); i++) {
            if (Integer.parseInt(getListaServico().get(i).getDescription()) == correcao.getServicos().getId()) {
                setIdServicos(i);
                break;
            }
        }
        for (int i = 0; i < getListaIndices().size(); i++) {
            if (Integer.parseInt(getListaIndices().get(i).getDescription()) == correcao.getIndice().getId()) {
                setIdIndices(i);
                break;
            }
        }
        return null;
    }

    public List<Correcao> getListaCorrecao() {
        if (listaCorrecao.isEmpty()) {
            CorrecaoDB db = new CorrecaoDBToplink();
            listaCorrecao = db.pesquisaTodos();
        }
        return listaCorrecao;
    }

    public List<SelectItem> getListaServico() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        int i = 0;
        ServicosDB db = new ServicosDBToplink();
        List select = db.pesquisaTodos();
        while (i < select.size()) {
            result.add(new SelectItem(i,
                    (String) ((Servicos) select.get(i)).getDescricao(),
                    Integer.toString(((Servicos) select.get(i)).getId())));
            i++;
        }
        return result;
    }

    public List<SelectItem> getListaIndices() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        DaoInterface di = new Dao();
        List select = di.list(new Indice(), true);
        for (int i = 0; i < select.size(); i++) {
            result.add(new SelectItem(i,
                    ((Indice) select.get(i)).getDescricao(),
                    Integer.toString(((Indice) select.get(i)).getId())));
        }
        return result;
    }

    public int getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(int idServicos) {
        this.idServicos = idServicos;
    }

    public int getIdIndices() {
        return idIndices;
    }

    public void setIdIndices(int idIndices) {
        this.idIndices = idIndices;
    }

    public Correcao getCorrecao() {
        return correcao;
    }

    public void setCorrecao(Correcao correcao) {
        this.correcao = correcao;
    }

    public void setListaCorrecao(List listaCorrecao) {
        this.listaCorrecao = listaCorrecao;
    }
}
