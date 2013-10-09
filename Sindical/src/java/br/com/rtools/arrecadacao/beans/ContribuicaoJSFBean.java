package br.com.rtools.arrecadacao.beans;

import br.com.rtools.arrecadacao.ConvencaoCidade;
import br.com.rtools.arrecadacao.MensagemConvencao;
import br.com.rtools.arrecadacao.db.ConvencaoCidadeDB;
import br.com.rtools.arrecadacao.db.ConvencaoCidadeDBToplink;
import br.com.rtools.arrecadacao.db.MensagemConvencaoDB;
import br.com.rtools.arrecadacao.db.MensagemConvencaoDBToplink;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.MovimentoDB;
import br.com.rtools.financeiro.db.MovimentoDBToplink;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.movimento.GerarMovimento;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;

public class ContribuicaoJSFBean {

    private String vencimento = "";
    private String referencia = DataHoje.data().substring(3);
    private int idContribuicao = 0;
    private int idIndex = 0;
    private List<DataObject> lista = new ArrayList();
    private List<SelectItem> servicos = new ArrayList<SelectItem>();
    private List<Servicos> listaServicos = new ArrayList<Servicos>();
    private String mensagem = "";
    private boolean enabled = false;
    private Long valorCorrente;

    public void refreshForm() {
    }

    public List<SelectItem> getListaServico() {
        if (servicos.isEmpty()) {
            int i = 0;
            ServicosDB db = new ServicosDBToplink();
            List select = db.pesquisaTodos(4);
            if (select.size() != 0) {
                while (i < select.size()) {
                    servicos.add(new SelectItem(
                            new Integer(i),
                            (String) ((Servicos) select.get(i)).getDescricao(),
                            Integer.toString(((Servicos) select.get(i)).getId())));
                    listaServicos.add(((Servicos) select.get(i)));
                    i++;
                }
            }
        }
        return servicos;
    }

//    public void alterarReferencia(){
//        try{
//            referencia = DataHoje.dataReferencia(vencimento);
//        }catch(Exception e){
//            referencia = "";
//        }
//    }
    public void alterarReferenciaDaGrid() {
        try {
//            GGeracao gGeracao = lista.get(htmlTable.getRowIndex());
//            gGeracao.setReferencia(DataHoje.dataReferencia( gGeracao.getData() ));
        } catch (Exception e) {
        }
    }

    public void alterarServicoDaGrid(int index) {
        try {
            int x = Integer.parseInt(String.valueOf(lista.get(index).getArgumento5()));
            MensagemConvencaoDB mensagemConvencaoDB = new MensagemConvencaoDBToplink();
            MensagemConvencao mensagemConvencao = mensagemConvencaoDB.verificaMensagem(
                    ((ConvencaoCidade) lista.get(index).getArgumento2()).getConvencao().getId(),
                    listaServicos.get(x).getId(),
                    1,
                    ((ConvencaoCidade) lista.get(index).getArgumento2()).getGrupoCidade().getId(),
                    referencia);
            int a = mensagemConvencao.getId();
            lista.get(index).setArgumento3(DataHoje.converteData(mensagemConvencao.getDtVencimento()));
        } catch (Exception e) {
            lista.get(index).setArgumento3(null);
        }
    }

    public List getLista() {
        if (!lista.isEmpty()) {
            return lista;
        } else {
            if ((new DataHoje()).integridadeReferencia(referencia)) {
                MensagemConvencaoDB mensagemConvencaoDB = new MensagemConvencaoDBToplink();
                MensagemConvencao mensagemConvencao = null;
                ConvencaoCidadeDB conDB = new ConvencaoCidadeDBToplink();
                List<ConvencaoCidade> listaConvencaoCidade = conDB.pesquisaTodos();
                if (listaConvencaoCidade == null) {
                    listaConvencaoCidade = new ArrayList();
                }
                for (int i = 0; i < listaConvencaoCidade.size(); i++) {
                    mensagemConvencao = mensagemConvencaoDB.verificaMensagem(
                            listaConvencaoCidade.get(i).getConvencao().getId(),
                            listaServicos.get(idContribuicao).getId(),
                            1,
                            listaConvencaoCidade.get(i).getGrupoCidade().getId(),
                            referencia);
                    if (mensagemConvencao != null && mensagemConvencao.getId() != -1) {
                        vencimento = mensagemConvencao.getVencimento();
                    } else {
                        vencimento = null;
                    }
                    lista.add(new DataObject(false,
                            i,
                            listaConvencaoCidade.get(i),
                            vencimento,
                            referencia,
                            idContribuicao));
                }
            }
        }
        return lista;
    }

    public void processar() {
        boolean selecionado = false;
        mensagem = "";
        for (int i = 0; i < lista.size(); i++) {
            GerarMovimento g = new GerarMovimento();
            if ((Boolean) lista.get(i).getArgumento0()) {
                selecionado = true;
                mensagem += g.gerarBoletos(String.valueOf(lista.get(i).getArgumento4()),
                        String.valueOf(lista.get(i).getArgumento3()),
                        ((ConvencaoCidade) lista.get(i).getArgumento2()).getGrupoCidade().getId(),
                        ((ConvencaoCidade) lista.get(i).getArgumento2()).getConvencao().getId(),
                        listaServicos.get(Integer.valueOf(String.valueOf(lista.get(i).getArgumento5()))).getId(),
                        1,
                        4);
            }
        }
        if (!selecionado) {
            mensagem = "Nenhuma linha foi selecionada!";
        }
        //movimentoDB.gerarContribuicao(lista, listaServicos, 4);
    }

    public void filtrar() {
        lista.clear();
        int i = 0;
        while (i < lista.size()) {
            lista.get(i).setArgumento4(referencia);
            lista.get(i).setArgumento5(idContribuicao);
            i++;
        }
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public int getIdContribuicao() {
        return idContribuicao;
    }

    public void setIdContribuicao(int idContribuicao) {
        this.idContribuicao = idContribuicao;
    }

    public void setListaServico(List<SelectItem> servicos) {
        this.servicos = servicos;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String startProcess() {
        setEnabled(true);
        valorCorrente = (long) 0;
        return null;
    }

    public synchronized Long getCurrentValue() {
        long result = 0;
        if (isEnabled()) {
            long numero = (long) 3.5;
            if ((valorCorrente + numero) < 100) {
                valorCorrente += numero;
            } else {
                valorCorrente += (100 - valorCorrente);
            }
            result = valorCorrente;
            if (valorCorrente == ((long) 100)) {
                result = Long.valueOf(0);
                enabled = false;
            }
        } else {
            result = Long.valueOf(0);
        }
        return result;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }
}
