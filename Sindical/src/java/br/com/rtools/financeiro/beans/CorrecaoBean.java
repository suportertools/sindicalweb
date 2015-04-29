package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.Correcao;
import br.com.rtools.financeiro.Indice;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.CorrecaoDB;
import br.com.rtools.financeiro.db.CorrecaoDao;
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
    private List<SelectItem> listServicos;
    private List<SelectItem> listIndices;

    @PostConstruct
    public void init() {
        idServicos = 0;
        idIndices = 0;
        correcao = new Correcao();
        listaCorrecao = new ArrayList<>();
        listServicos = new ArrayList<>();
        listIndices = new ArrayList<>();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("correcaoBean");
    }

    public void save() {
        CorrecaoDB db = new CorrecaoDao();
        Dao dao = new Dao();
        NovoLog novoLog = new NovoLog();
        Servicos servico = (Servicos) dao.find(new Servicos(), Integer.parseInt(getListServicos().get(idServicos).getDescription()));
        correcao.setIndice((Indice) dao.find(new Indice(), Integer.parseInt(getListIndices().get(idIndices).getDescription())));
        correcao.setServicos(servico);
        if (correcao.getId() == -1) {
            if (DataHoje.validaReferencias(correcao.getReferenciaInicial(), correcao.getReferenciaFinal())) {
                List dd = db.pesquisaRefValida(servico, correcao.getReferenciaInicial(), correcao.getReferenciaFinal());
                if (Integer.parseInt(String.valueOf((Long) dd.get(0))) == 0) {
                    if (dao.save(correcao, true)) {
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
            Correcao c = (Correcao) dao.find(correcao);
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
            if (dao.update(correcao, true)) {
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
    }

    public String clear() {
        GenericaSessao.remove("correcaoBean");
        return "correcao";
    }

    public void delete(Correcao co) {
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
        listIndices.clear();
        listServicos.clear();
        listaCorrecao.clear();
    }

    public void edit(Correcao co) {
        correcao = co;
        for (int i = 0; i < getListServicos().size(); i++) {
            if (Integer.parseInt(getListServicos().get(i).getDescription()) == correcao.getServicos().getId()) {
                idServicos = i;
                break;
            }
        }
        for (int i = 0; i < getListIndices().size(); i++) {
            if (Integer.parseInt(getListIndices().get(i).getDescription()) == correcao.getIndice().getId()) {
                idIndices = i;
                break;
            }
        }
    }

    public List<Correcao> getListaCorrecao() {
        if (listaCorrecao.isEmpty()) {
            listaCorrecao = new Dao().list(new Correcao(), true);
        }
        return listaCorrecao;
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

    public List<SelectItem> getListServicos() {
        if (listServicos.isEmpty()) {
            List<Servicos> list = new Dao().list(new Servicos(), true);
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    idServicos = i;
                }
                listServicos.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        return listServicos;
    }

    public void setListServicos(List<SelectItem> listServicos) {
        this.listServicos = listServicos;
    }

    public List<SelectItem> getListIndices() {
        if (listIndices.isEmpty()) {
            List<Indice> list = new Dao().list(new Indice(), true);
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    idIndices = i;
                }
                listIndices.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        return listIndices;
    }

    public void setListIndices(List<SelectItem> listIndices) {
        this.listIndices = listIndices;
    }
}
