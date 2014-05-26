package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.Indice;
import br.com.rtools.financeiro.IndiceMensal;
import br.com.rtools.financeiro.db.IndiceMensalDB;
import br.com.rtools.financeiro.db.IndiceMensalDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.Moeda;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class IndiceMensalBean implements Serializable {

    private IndiceMensal indiceMensal = new IndiceMensal();
    private List<IndiceMensal> listaIndiceMensal = new ArrayList<IndiceMensal>();
    private int idIndex = -1;
    private String msgConfirma = "";
    private int numMes = 0;
    private int ano = 0;
    private String valor = "";
    private int idIndice = 0;
    private boolean limpar = false;

    public String novo() {
        msgConfirma = "";
        indiceMensal = new IndiceMensal();
        limpar = false;
        return "indiceMensal";
    }

    public List<SelectItem> getListaIndices() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        int i = 0;
        IndiceMensalDB db = new IndiceMensalDBToplink();
        List select = null;
        select = db.pesquisaTodosIndices();
        while (i < select.size()) {
            result.add(new SelectItem(i,
                    ((Indice) select.get(i)).getDescricao(),
                    Integer.toString(((Indice) select.get(i)).getId())));
            i++;
        }
        return result;
    }

    public String salvar() {

        msgConfirma = "";

        if (valor.isEmpty()) {
            msgConfirma = "Digite um valor válido!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        IndiceMensalDB imdb = new IndiceMensalDBToplink();
        //Indice indice = db.pesquisaCodigoIndice(Integer.valueOf(getListaIndices().get(idIndice).getDescription()));
        NovoLog novoLog = new NovoLog();
        DaoInterface di = new Dao();
        indiceMensal.setAno(Integer.valueOf(getListaAnos().get(ano).getDescription()));
        indiceMensal.setMes(numMes);
        indiceMensal.setValor(Moeda.substituiVirgulaFloat(valor));
        indiceMensal.setIndice((Indice) di.find(new Indice(), Integer.parseInt(getListaIndices().get(idIndice).getDescription())));
        if (imdb.pesquisaIndMensalExistente(indiceMensal.getIndice().getId(), indiceMensal.getAno(), indiceMensal.getMes()).isEmpty()) {
            if (di.save(indiceMensal, true)) {
                novoLog.save(
                        "ID: " + indiceMensal.getId()
                        + " - Índice: (" + indiceMensal.getIndice().getId() + ") " + indiceMensal.getIndice().getDescricao()
                        + " - Mês: " + indiceMensal.getMes()
                        + " - Ano: " + indiceMensal.getAno()
                        + " - Valor: " + indiceMensal.getValor()
                );
                msgConfirma = "Indice Mensal salvo com Sucesso!";
                GenericaMensagem.info("Sucesso", msgConfirma);
            } else {
                msgConfirma = "Erro ao salvar Indice Mensal";
                GenericaMensagem.warn("Erro", msgConfirma);
            }
        } else {
            msgConfirma = "Indice Mensal já existe no Sistema!";
            GenericaMensagem.warn("Erro", msgConfirma);
        }
        indiceMensal = new IndiceMensal();
        listaIndiceMensal.clear();
        return null;
    }

    public String btnExcluir(IndiceMensal im) {
        indiceMensal = im;
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        if (!di.delete(indiceMensal, true)) {
            msgConfirma = "Erro ao excluir Indice Mensal!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        } else {
            novoLog.delete(
                    "ID: " + indiceMensal.getId()
                    + " - Índice: (" + indiceMensal.getIndice().getId() + ") " + indiceMensal.getIndice().getDescricao()
                    + " - Mês: " + indiceMensal.getMes()
                    + " - Ano: " + indiceMensal.getAno()
                    + " - Valor: " + indiceMensal.getValor()
            );
            msgConfirma = "Registro excluido com Sucesso!";
            GenericaMensagem.info("Sucesso", msgConfirma);
            indiceMensal = new IndiceMensal();
            listaIndiceMensal.clear();
            setLimpar(true);
        }

        return null;
    }

    public List<SelectItem> getListaAnos() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        int an = 0;
        an = Integer.valueOf(DataHoje.data().substring(6, 10));
        for (int o = 0; o < 6; o++) {
            result.add(new SelectItem(new Integer(o),
                    String.valueOf(an),
                    String.valueOf(an)));
            an = an - 1;
        }
        return result;
    }

    public IndiceMensal getIndiceMensal() {
        return indiceMensal;
    }

    public void setIndiceMensal(IndiceMensal indiceMensal) {
        this.indiceMensal = indiceMensal;
    }

    public List<IndiceMensal> getListaIndiceMensal() {
        IndiceMensalDB db = new IndiceMensalDBToplink();
        listaIndiceMensal = db.pesquisaIndiceMensalPorIDIndice(Integer.valueOf(getListaIndices().get(idIndice).getDescription()));
        return listaIndiceMensal;
    }

    public void limpar() {
        if (limpar == true) {
            novo();
        }
        listaIndiceMensal.clear();
        indiceMensal = new IndiceMensal();
        valor = "";
        ano = 0;
        numMes = 0;
    }

    public void setListaIndiceMensal(List<IndiceMensal> listaIndiceMensal) {
        this.listaIndiceMensal = listaIndiceMensal;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public int getNumMes() {
        return numMes;
    }

    public void setNumMes(int numMes) {
        this.numMes = numMes;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getIdIndice() {
        return idIndice;
    }

    public void setIdIndice(int idIndice) {
        this.idIndice = idIndice;
    }

    /**
     * @return the limpar
     */
    public boolean isLimpar() {
        return limpar;
    }

    /**
     * @param limpar the limpar to set
     */
    public void setLimpar(boolean limpar) {
        this.limpar = limpar;
    }
}
