package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.ContaBanco;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.PrevisaoPagamento;
import br.com.rtools.financeiro.TipoPagamento;
import br.com.rtools.financeiro.dao.PrevisaoPagamentoDao;
import br.com.rtools.impressao.ParametroPrevisaoPagto;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Diretorio;
import br.com.rtools.utilitarios.Download;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.GenericaString;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.PF;
import br.com.rtools.utilitarios.SalvaArquivos;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@ManagedBean
@SessionScoped
public class PrevisaoPagamentoBean implements Serializable {

    private PrevisaoPagamento previsaoPagamento;
    private PrevisaoPagamento[] previsaoSelecionada;
    private Movimento[] movimentoSelecionado;
    private List<PrevisaoPagamento> listPrevisaoPagamentos;
    private List<SelectItem>[] listSelectItem;
    private String message;
    private String[] valor;
    private Integer[] index;
    private Boolean[] hidden;
    private String dataIncial;
    private String dataFinal;

    @PostConstruct
    public void init() {
        previsaoPagamento = new PrevisaoPagamento();
        listPrevisaoPagamentos = new ArrayList<PrevisaoPagamento>();
        listSelectItem = new ArrayList[2];
        listSelectItem[0] = new ArrayList<SelectItem>();
        listSelectItem[1] = new ArrayList<SelectItem>();
        message = "";
        valor = new String[5];
        valor[0] = "0,00";
        valor[1] = "0,00";
        valor[2] = "0,00";
        valor[3] = "0,00";
        index = new Integer[2];
        index[0] = 0;
        index[1] = 0;
        hidden = new Boolean[3];
        hidden[0] = false;
        hidden[1] = false;
        hidden[2] = false;
        previsaoSelecionada = null;
        movimentoSelecionado = null;
        dataIncial = DataHoje.data();
        dataFinal = DataHoje.data();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("previsaoPagamentoBean");
    }

    public void clear() {
        GenericaSessao.remove("previsaoPagamentoBean");
    }

    public void clear(String clear) {
        if (clear.equals("dataFinal")) {
            dataFinal = "";
        }
    }

    public void addItem() {
        if (previsaoSelecionada.length == 0) {
            GenericaMensagem.warn("Validação", "Selecionar itens da lista!");
            PF.update("form_pp:i_msg");
            return;
        }
        boolean err = false;
        Dao dao = new Dao();
        TipoPagamento tp = null;
        ContaBanco cb = null;
        tp = (TipoPagamento) dao.find(new TipoPagamento(), Integer.parseInt(getListTiposPagamento().get(index[0]).getDescription()));
        if (hidden[1]) {
            cb = (ContaBanco) dao.find(new ContaBanco(), Integer.parseInt(getListContasBancos().get(index[1]).getDescription()));
        }
        if (tp.getId() == 4) {
            if (previsaoPagamento.getCheque().isEmpty()) {
                GenericaMensagem.warn("Validação", "Informar dados do cheque!");
                PF.update("form_pp:i_msg");
                return;
            }
        }
        try {
            for (int i = 0; i < previsaoSelecionada.length; i++) {
                for (int j = 0; j < listPrevisaoPagamentos.size(); j++) {
                    if (previsaoSelecionada[i].getMovimento().getId() == listPrevisaoPagamentos.get(j).getMovimento().getId()) {
                        listPrevisaoPagamentos.get(j).setTipoPagamento(tp);
                        listPrevisaoPagamentos.get(j).setContaBanco(cb);
                        listPrevisaoPagamentos.get(j).setCheque(previsaoPagamento.getCheque());
                        previsaoSelecionada[i].setTipoPagamento(tp);
                        previsaoSelecionada[i].setContaBanco(cb);
                        previsaoSelecionada[i].setCheque(previsaoPagamento.getCheque());
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
        previsaoPagamento = new PrevisaoPagamento();
        previsaoSelecionada = null;
        PF.update("form_pp:i_form_panel");
        PF.update("form_pp:i_pg_tp");
        PF.update("form_pp:i_grid_totais");
        PF.closeDialog("dlg_1");
    }

    public void save() {
        if (listSelectItem[0].isEmpty()) {
            message = "Cadastrar tipos de pagamento!";
            return;
        }
        if (previsaoSelecionada.length == 0) {
            message = "Selecionar itens da lista!";
            return;
        }
        Dao dao = new Dao();
        dao.openTransaction();
        boolean err = false;
        for (PrevisaoPagamento ps : previsaoSelecionada) {
            if (ps.getTipoPagamento().getId() != -1) {
                if (ps.getId() == -1) {
                    if (dao.save(ps)) {
                        message = "Registro(s) adicionado(s) com sucesso";
                    } else {
                        message = "Erro ao adicionar registro(s)!";
                        err = true;
                        break;
                    }
                } else {
                    if (dao.update(ps)) {
                        message = "Registro(s) atualizado(s) com sucesso";
                    } else {
                        message = "Erro ao atualizar registro(s)!";
                        err = true;
                        break;
                    }
                }
            }
        }
        if (!err) {
            dao.commit();
            previsaoSelecionada = null;
        } else {
            dao.rollback();
        }
    }

    public void remove(PrevisaoPagamento pg) {
        if (pg.getId() != -1) {
            if (pg.getMovimento().getBaixa() == null) {
                Dao dao = new Dao();
                if (dao.delete(pg, true)) {
                    for (int i = 0; i < listPrevisaoPagamentos.size(); i++) {
                        if (listPrevisaoPagamentos.get(i).getId() == pg.getId()) {
                            listPrevisaoPagamentos.get(i).setId(-1);
                            listPrevisaoPagamentos.get(i).setTipoPagamento(new TipoPagamento());
                            listPrevisaoPagamentos.get(i).setContaBanco(new ContaBanco());
                            listPrevisaoPagamentos.get(i).setCheque("");
                        }
                    }
                    for (int i = 0; i < previsaoSelecionada.length; i++) {
                        if (previsaoSelecionada[i].getId() == pg.getId()) {
                            previsaoSelecionada[i].setId(-1);
                            previsaoSelecionada[i].setTipoPagamento(new TipoPagamento());
                            previsaoSelecionada[i].setContaBanco(new ContaBanco());
                            previsaoSelecionada[i].setCheque("");
                        }
                    }
                    message = "Registro removido com sucesso";
                } else {
                    message = "Erro ao remover registro";
                }
            }
        } else {
            for (int i = 0; i < listPrevisaoPagamentos.size(); i++) {
                if (listPrevisaoPagamentos.get(i).getId() == pg.getId()) {
                    listPrevisaoPagamentos.get(i).setId(-1);
                    listPrevisaoPagamentos.get(i).setTipoPagamento(new TipoPagamento());
                    listPrevisaoPagamentos.get(i).setContaBanco(new ContaBanco());
                    listPrevisaoPagamentos.get(i).setCheque("");
                }
            }
            for (int i = 0; i < previsaoSelecionada.length; i++) {
                if (previsaoSelecionada[i].getId() == pg.getId()) {
                    previsaoSelecionada[i].setId(-1);
                    previsaoSelecionada[i].setTipoPagamento(new TipoPagamento());
                    previsaoSelecionada[i].setContaBanco(new ContaBanco());
                    previsaoSelecionada[i].setCheque("");
                }
            }
            message = "Registro removido com sucesso";
        }
        PF.openDialog("dlg_msg");
        PF.update("form_pp:i_g_msg");
    }

    public void edit(PrevisaoPagamento pg) {
        previsaoPagamento = pg;
    }

    public Integer[] getIndex() {
        return index;
    }

    public void setIndex(Integer[] index) {
        this.index = index;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PrevisaoPagamento getPrevisaoPagamento() {
        return previsaoPagamento;
    }

    public void setPrevisaoPagamento(PrevisaoPagamento previsaoPagamento) {
        this.previsaoPagamento = previsaoPagamento;
    }

    public List<PrevisaoPagamento> getListPrevisaoPagamentos() {
        return listPrevisaoPagamentos;
    }

    public void setListPrevisaoPagamentos(List<PrevisaoPagamento> listPrevisaoPagamentos) {
        this.listPrevisaoPagamentos = listPrevisaoPagamentos;
    }

    public List<SelectItem> getListTiposPagamento() {
        if (listSelectItem[0].isEmpty()) {
            Dao dao = new Dao();
            List<TipoPagamento> list = (List<TipoPagamento>) dao.find("TipoPagamento", new int[]{3, 4, 8, 9, 10, 13});
            for (int i = 0; i < list.size(); i++) {
                listSelectItem[0].add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        if (!listSelectItem[0].isEmpty()) {
            return listSelectItem[0];
        }
        return new ArrayList<SelectItem>();
    }

    public List<SelectItem> getListContasBancos() {
        if (!getListTiposPagamento().isEmpty()) {
            int id = Integer.parseInt(getListTiposPagamento().get(index[0]).getDescription());
            if (id != 13 && id != 4) {
                listSelectItem[1].clear();
                return new ArrayList<SelectItem>();
            }
            if (listSelectItem[1].isEmpty()) {
                Dao dao = new Dao();
                List<ContaBanco> list = (List<ContaBanco>) dao.list("ContaBanco");
                for (int i = 0; i < list.size(); i++) {
                    listSelectItem[1].add(new SelectItem(i, list.get(i).getBanco().getBanco() + " - " + list.get(i).getAgencia() + " - " + list.get(i).getConta(), "" + list.get(i).getId()));
                }
            }
        }
        if (!listSelectItem[1].isEmpty()) {
            hidden[1] = true;
            return listSelectItem[1];
        }
        return new ArrayList<SelectItem>();
    }

    public Boolean[] getHidden() {
        hidden[0] = false;
        hidden[1] = false;
        int id = Integer.parseInt(getListTiposPagamento().get(index[0]).getDescription());
        if (Integer.parseInt(getListTiposPagamento().get(index[0]).getDescription()) == 4) {
            hidden[0] = true;
            hidden[1] = true;
            //index[1] = 0;
        }
        if (id == 13) {
            hidden[0] = true;
            hidden[1] = false;
            //index[1] = 0;
        }
        return hidden;
    }

    public void setHidden(Boolean[] hidden) {
        this.hidden = hidden;
    }

    public PrevisaoPagamento[] getPrevisaoSelecionada() {
        return previsaoSelecionada;
    }

    public void setPrevisaoSelecionada(PrevisaoPagamento[] previsaoSelecionada) {
        this.previsaoSelecionada = previsaoSelecionada;
    }

    public String getDataIncial() {
        return dataIncial;
    }

    public void setDataIncial(String dataIncial) {
        this.dataIncial = dataIncial;
    }

    public String getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(String dataFinal) {
        this.dataFinal = dataFinal;
    }

    public Movimento[] getMovimentoSelecionado() {
        return movimentoSelecionado;
    }

    public void setMovimentoSelecionado(Movimento[] movimentoSelecionado) {
        this.movimentoSelecionado = movimentoSelecionado;
    }

    public String valorC(float valor, float juros, float multa, float correcao, float desconto) {
        float total = valor + ((juros + multa + correcao) - desconto);
        return Moeda.converteR$Float(total);
    }

    public float valorF(float valor, float juros, float multa, float correcao, float desconto) {
        float total = valor + ((juros + multa + correcao) - desconto);
        return total;
    }

    public String[] getValor() {
        float total = 0;
        float dinheiro = 0;
        float outros = 0;
        float cheque = 0;
        if (!getListPrevisaoPagamentos().isEmpty()) {
            for (int i = 0; i < listPrevisaoPagamentos.size(); i++) {
                float v = listPrevisaoPagamentos.get(i).getMovimento().getValor();
                float f = listPrevisaoPagamentos.get(i).getMovimento().getJuros();
                float m = listPrevisaoPagamentos.get(i).getMovimento().getMulta();
                float c = listPrevisaoPagamentos.get(i).getMovimento().getCorrecao();
                float d = listPrevisaoPagamentos.get(i).getMovimento().getDesconto();
                if (listPrevisaoPagamentos.get(i).getTipoPagamento().getId() == 3) {
                    float dn = valorF(v, f, m, c, d);
                    dinheiro += dn;
                    total += dn;
                    dn = 0;
                } else if (listPrevisaoPagamentos.get(i).getTipoPagamento().getId() == 4) {
                    float cn = valorF(v, f, m, c, d);
                    cheque += cn;
                    total += cn;
                    cn = 0;
                } else if (listPrevisaoPagamentos.get(i).getTipoPagamento().getId() == 8
                        || listPrevisaoPagamentos.get(i).getTipoPagamento().getId() == 9
                        || listPrevisaoPagamentos.get(i).getTipoPagamento().getId() == 10
                        || listPrevisaoPagamentos.get(i).getTipoPagamento().getId() == 13) {
                    float db = valorF(v, f, m, c, d);
                    outros += db;
                    total += db;
                    db = 0;
                } else {
                    float db = valorF(v, f, m, c, d);
                    total += db;
                    db = 0;
                }
            }
            valor[0] = Moeda.converteR$Float(total);
            valor[1] = Moeda.converteR$Float(dinheiro);
            valor[2] = Moeda.converteR$Float(cheque);
            valor[3] = Moeda.converteR$Float(outros);
        } else {
            valor[0] = "0,00";
            valor[1] = "0,00";
            valor[2] = "0,00";
            valor[3] = "0,00";
        }
        return valor;
    }

    public void setValor(String[] valor) {
        this.valor = valor;
    }

    // IMPRIMIR RELATÓRIO   
    public synchronized void print() {
        PrevisaoPagamentoDao ppd = new PrevisaoPagamentoDao();
        if (dataIncial.isEmpty()) {
            return;
        }
        if (dataIncial.isEmpty() && dataFinal.isEmpty()) {
            return;
        }
        if (dataFinal.isEmpty()) {
            dataFinal = dataIncial;
        }
        List list = ppd.listaPrevisaoPagamento(dataIncial, dataFinal, true);
        if (list.isEmpty()) {
            return;
        }
        Collection<ParametroPrevisaoPagto> previsaoPagtos = new ArrayList<ParametroPrevisaoPagto>();
        float dinheiro = 0;
        float outros = 0;
        float cheque = 0;
        float valor;
        int tipo;
        for (int i = 0; i < list.size(); i++) {
            valor = Float.parseFloat(GenericaString.converterNullToString((((List) list.get(i)).get(6))));
            tipo = Integer.parseInt(GenericaString.converterNullToString((((List) list.get(i)).get(8))));
            if (tipo == 3) {
                dinheiro += valor;
            } else if (tipo == 4) {
                cheque += valor;
            } else if (tipo == 8
                    || tipo == 9
                    || tipo == 10
                    || tipo == 13) {
                outros += valor;
            }
            previsaoPagtos.add(
                    new ParametroPrevisaoPagto(
                            GenericaString.converterNullToString(((List) list.get(i)).get(0)), // 0 - fin_lote_dt_emissao
                            new BigDecimal(Float.parseFloat(GenericaString.converterNullToString((((List) list.get(i)).get(1))))), // 1 - fin_lote_nr_valor
                            GenericaString.converterNullToString(((List) list.get(i)).get(2)), // 2 - pes_ds_nome
                            GenericaString.converterNullToString(((List) list.get(i)).get(3)), // 3 - fin_plano_5
                            GenericaString.converterNullToString(((List) list.get(i)).get(4)), // 4 - fin_lote_ds_documento
                            GenericaString.converterNullToString(((List) list.get(i)).get(5)), // 5 - fin_movimento_dt_vencimento
                            new BigDecimal(valor), // 6 - nr_valor_devido
                            GenericaString.converterNullToString(((List) list.get(i)).get(7)) // 7 - fin_tipo_documento_ds_descricao
                    )
            );
        }

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/PREVISAO_PAGTO.jasper")));
            Diretorio.criar("Arquivos/downloads/previsao_pagto");
            Map parametros = new HashMap();
            parametros.put("filtro_data_inicial", dataIncial);
            parametros.put("filtro_data_final", dataFinal);
            parametros.put("total_dinheiro", Moeda.converteR$Float(dinheiro));
            parametros.put("total_cheque", Moeda.converteR$Float(cheque));
            parametros.put("total_outros", Moeda.converteR$Float(outros));
            byte[] arquivo = JasperExportManager.exportReportToPdf(JasperFillManager.fillReport(jasperReport, parametros, new JRBeanCollectionDataSource(previsaoPagtos)));
            String nomeDownload = "imp_previsao_pagto_" + DataHoje.horaMinuto().replace(":", "") + ".pdf";
            String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/previsao_pagto");
            SalvaArquivos salvaArquivos = new SalvaArquivos(arquivo, nomeDownload, false);
            salvaArquivos.salvaNaPasta(pathPasta);
            Download download = new Download(nomeDownload, pathPasta, "application/pdf", FacesContext.getCurrentInstance());
            download.baixar();
            download.remover();
        } catch (JRException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }
    
    public void process() {
        listPrevisaoPagamentos.clear();
        PrevisaoPagamentoDao ppd = new PrevisaoPagamentoDao();
        if (dataIncial.isEmpty()) {
            listPrevisaoPagamentos.clear();
        }
        if (dataIncial.isEmpty() && dataFinal.isEmpty()) {
            listPrevisaoPagamentos.clear();
        }
        if (dataFinal.isEmpty()) {
            dataFinal = dataIncial;
        }
        listPrevisaoPagamentos = ppd.listaPrevisaoPagamento(dataIncial, dataFinal);
    }

}
