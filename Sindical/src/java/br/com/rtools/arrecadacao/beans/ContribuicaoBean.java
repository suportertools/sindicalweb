package br.com.rtools.arrecadacao.beans;

import br.com.rtools.arrecadacao.ConvencaoCidade;
import br.com.rtools.arrecadacao.MensagemConvencao;
import br.com.rtools.arrecadacao.db.ConvencaoCidadeDB;
import br.com.rtools.arrecadacao.db.ConvencaoCidadeDBToplink;
import br.com.rtools.arrecadacao.db.MensagemConvencaoDB;
import br.com.rtools.arrecadacao.db.MensagemConvencaoDBToplink;
import br.com.rtools.arrecadacao.lista.ListaContribuicao;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.movimento.GerarMovimento;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class ContribuicaoBean {

    private String vencimento;
    private String referencia;
    private int idContribuicao;
    private int idIndex;
    private List<ListaContribuicao> listaContribuicoes;
    private List<ListaContribuicao> selectedListaContribuicoes;
    private List<DataObject> lista;
    private List<SelectItem> servicos;
    private List<Servicos> listServicos;
    private String mensagem;
    private List listMessage;
    private boolean enabled;
    private Long valorCorrente;
    private boolean visibleSelection;

    @PostConstruct
    public void init() {
        idIndex = 0;
        vencimento = "";
        referencia = DataHoje.data().substring(3);
        idContribuicao = 0;
        lista = new ArrayList();
        servicos = new ArrayList<>();
        listServicos = new ArrayList<>();
        listaContribuicoes = new ArrayList<>();
        listMessage = new ArrayList<>();
        mensagem = "";
        enabled = false;
        valorCorrente = null;
        selectedListaContribuicoes = null;
        visibleSelection = false;
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("contribuicaoBean");
    }

    public List<SelectItem> getListServico() {
        if (servicos.isEmpty()) {
            ServicosDB db = new ServicosDBToplink();
            List list = db.pesquisaTodos(4);
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    servicos.add(new SelectItem(i, (String) ((Servicos) list.get(i)).getDescricao(), Integer.toString(((Servicos) list.get(i)).getId())));
                    listServicos.add(((Servicos) list.get(i)));
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
//    public void alterarReferenciaDaGrid() {
//        try {
////            GGeracao gGeracao = lista.get(htmlTable.getRowIndex());
////            gGeracao.setReferencia(DataHoje.dataReferencia( gGeracao.getData() ));
//        } catch (Exception e) {
//        }
//    }
//    public void alterarServicoDaGrid(int index) {
//        try {
//            int x = Integer.parseInt(String.valueOf(lista.get(index).getArgumento5()));
//            MensagemConvencaoDB mensagemConvencaoDB = new MensagemConvencaoDBToplink();
//            MensagemConvencao mensagemConvencao = mensagemConvencaoDB.verificaMensagem(
//                    ((ConvencaoCidade) lista.get(index).getArgumento2()).getConvencao().getId(),
//                    listServicos.get(x).getId(),
//                    1,
//                    ((ConvencaoCidade) lista.get(index).getArgumento2()).getGrupoCidade().getId(),
//                    referencia);
//            int a = mensagemConvencao.getId();
//            lista.get(index).setArgumento3(DataHoje.converteData(mensagemConvencao.getDtVencimento()));
//        } catch (Exception e) {
//            lista.get(index).setArgumento3(null);
//        }
//    }
    public void updateServico(int index) {
        try {
            int x = Integer.parseInt(String.valueOf(lista.get(index).getArgumento5()));
            MensagemConvencaoDB mensagemConvencaoDB = new MensagemConvencaoDBToplink();
            MensagemConvencao mensagemConvencao = mensagemConvencaoDB.verificaMensagem(
                    listaContribuicoes.get(index).getConvencaoCidade().getConvencao().getId(),
                    listServicos.get(x).getId(),
                    1,
                    listaContribuicoes.get(index).getConvencaoCidade().getGrupoCidade().getId(),
                    referencia);
            int a = mensagemConvencao.getId();
            listaContribuicoes.get(index).setVencimento(DataHoje.converteData(mensagemConvencao.getDtVencimento()));
        } catch (Exception e) {
            listaContribuicoes.get(index).setVencimento(null);
        }
    }

//    public List getLista() {
//        if (!lista.isEmpty()) {
//            return lista;
//        } else {
//            if ((new DataHoje()).integridadeReferencia(referencia)) {
//                MensagemConvencaoDB mensagemConvencaoDB = new MensagemConvencaoDBToplink();
//                MensagemConvencao mensagemConvencao;
//                ConvencaoCidadeDB conDB = new ConvencaoCidadeDBToplink();
//                List<ConvencaoCidade> listaConvencaoCidade = conDB.pesquisaTodos();
//                if (listaConvencaoCidade == null) {
//                    listaConvencaoCidade = new ArrayList();
//                }
//                for (int i = 0; i < listaConvencaoCidade.size(); i++) {
//                    mensagemConvencao = mensagemConvencaoDB.verificaMensagem(
//                            listaConvencaoCidade.get(i).getConvencao().getId(),
//                            listServicos.get(idContribuicao).getId(),
//                            1,
//                            listaConvencaoCidade.get(i).getGrupoCidade().getId(),
//                            referencia);
//                    if (mensagemConvencao != null && mensagemConvencao.getId() != -1) {
//                        vencimento = mensagemConvencao.getVencimento();
//                    } else {
//                        vencimento = null;
//                    }
//                    lista.add(new DataObject(false,
//                            i,
//                            listaConvencaoCidade.get(i),
//                            vencimento,
//                            referencia,
//                            idContribuicao));
//                }
//            }
//        }
//        return lista;
//    }
//    public synchronized void processar() {
//        boolean selecionado = false;
//        mensagem = "";
//        for (int i = 0; i < lista.size(); i++) {
//            GerarMovimento g = new GerarMovimento();
//            if ((Boolean) lista.get(i).getArgumento0()) {
//                selecionado = true;
//                mensagem += g.gerarBoletos(String.valueOf(lista.get(i).getArgumento4()),
//                        String.valueOf(lista.get(i).getArgumento3()),
//                        ((ConvencaoCidade) lista.get(i).getArgumento2()).getGrupoCidade().getId(),
//                        ((ConvencaoCidade) lista.get(i).getArgumento2()).getConvencao().getId(),
//                        listServicos.get(Integer.valueOf(String.valueOf(lista.get(i).getArgumento5()))).getId(),
//                        1,
//                        4);
//            }
//        }
//        if (!selecionado) {
//            mensagem = "Nenhuma linha foi selecionada!";
//        }
//        //movimentoDB.gerarContribuicao(lista, listaServicos, 4);
//    }
    public synchronized void proccess() {
        if (selectedListaContribuicoes == null) {
            GenericaMensagem.warn("Validação", "Nenhuma linha foi selecionada!");
        }
        listMessage.clear();
        NovoLog novoLog = new NovoLog();
        novoLog.startList();
        String logList;
        for (int i = 0; i < selectedListaContribuicoes.size(); i++) {
            Object[] o = new Object[2];
            GerarMovimento g = new GerarMovimento();
            o = g.gerarBoletos(String.valueOf(selectedListaContribuicoes.get(i).getReferencia()),
                    String.valueOf(selectedListaContribuicoes.get(i).getVencimento()),
                    selectedListaContribuicoes.get(i).getConvencaoCidade().getGrupoCidade().getId(),
                    selectedListaContribuicoes.get(i).getConvencaoCidade().getConvencao().getId(),
                    listServicos.get(Integer.valueOf(String.valueOf(selectedListaContribuicoes.get(i).getContribuicao()))).getId(),
                    1,
                    4);
            if (o[0].equals(0)) {
                logList = " Referência: " + selectedListaContribuicoes.get(i).getReferencia()
                        + " - Vencimento: " + selectedListaContribuicoes.get(i).getVencimento()
                        + " - Grupo Cidade: (" + selectedListaContribuicoes.get(i).getConvencaoCidade().getGrupoCidade().getId() + ") " + selectedListaContribuicoes.get(i).getConvencaoCidade().getGrupoCidade().getDescricao()
                        + " - Convenção: (" + selectedListaContribuicoes.get(i).getConvencaoCidade().getConvencao().getId() + ") " + selectedListaContribuicoes.get(i).getConvencaoCidade().getConvencao().getDescricao()
                        + " - Serviços: (" + listServicos.get(Integer.valueOf(String.valueOf(selectedListaContribuicoes.get(i).getContribuicao()))).getId() + ") " + listServicos.get(Integer.valueOf(String.valueOf(selectedListaContribuicoes.get(i).getContribuicao()))).getDescricao()
                        + " - Tipo Serviço: (" + 1 + ")"
                        + " - Rotina: (" + 4 + ")";
                novoLog.save(logList);
            }
            o[1] = o[1].toString() + " - Convenção: " + selectedListaContribuicoes.get(i).getConvencaoCidade().getConvencao().getDescricao() + " - Grupo Cidade: " + selectedListaContribuicoes.get(i).getConvencaoCidade().getGrupoCidade().getDescricao();
            listMessage.add(o);
        }
        for (int i = 0; i < listMessage.size(); i++) {
            if (((Object[]) listMessage.get(i))[0].equals(0)) {
                if (i == 0) {
                    novoLog.saveList();
                }
                GenericaMensagem.info("Mensagem " + (i + 1), ((Object[]) listMessage.get(i))[1].toString());
            } else if (((Object[]) listMessage.get(i))[0].equals(1)) {
                GenericaMensagem.warn("Erro" + (i + 1), ((Object[]) listMessage.get(i))[1].toString());
            }
        }
        listMessage.clear();
        selectedListaContribuicoes = null;
        //movimentoDB.gerarContribuicao(lista, listaServicos, 4);
    }

//    public void filtrar() {
//        lista.clear();
//        int i = 0;
//        while (i < lista.size()) {
//            lista.get(i).setArgumento4(referencia);
//            lista.get(i).setArgumento5(idContribuicao);
//            i++;
//        }
//    }
    public void filter() {
        if (referencia.isEmpty()) {
            visibleSelection = false;
        }
        listaContribuicoes.clear();
        for (ListaContribuicao c : listaContribuicoes) {
            c.setReferencia(referencia);
            c.setContribuicao(idContribuicao);
        }
        visibleSelection = true;
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

    public void setListServico(List<SelectItem> servicos) {
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

    public List<ListaContribuicao> getListaContribuicoes() {
        if (!listaContribuicoes.isEmpty()) {
            return listaContribuicoes;
        } else {
            if ((new DataHoje()).integridadeReferencia(referencia)) {
                MensagemConvencaoDB mensagemConvencaoDB = new MensagemConvencaoDBToplink();
                MensagemConvencao mensagemConvencao;
                ConvencaoCidadeDB conDB = new ConvencaoCidadeDBToplink();
                List<ConvencaoCidade> listaConvencaoCidade = conDB.pesquisaTodos();
                if (listaConvencaoCidade == null) {
                    listaConvencaoCidade = new ArrayList();
                }
                for (int i = 0; i < listaConvencaoCidade.size(); i++) {
                    mensagemConvencao = mensagemConvencaoDB.verificaMensagem(
                            listaConvencaoCidade.get(i).getConvencao().getId(),
                            listServicos.get(idContribuicao).getId(),
                            1,
                            listaConvencaoCidade.get(i).getGrupoCidade().getId(),
                            referencia);
                    if (mensagemConvencao != null && mensagemConvencao.getId() != -1) {
                        vencimento = mensagemConvencao.getVencimento();
                    } else {
                        vencimento = null;
                    }
                    listaContribuicoes.add(new ListaContribuicao(i, listaConvencaoCidade.get(i), vencimento, referencia, idContribuicao));
                }
            }
        }
        return listaContribuicoes;
    }

    public void setListaContribuicoes(List<ListaContribuicao> listaContribuicoes) {
        this.listaContribuicoes = listaContribuicoes;
    }

    public List<ListaContribuicao> getSelectedListaContribuicoes() {
        return selectedListaContribuicoes;
    }

    public void setSelectedListaContribuicoes(List<ListaContribuicao> selectedListaContribuicoes) {
        this.selectedListaContribuicoes = selectedListaContribuicoes;
    }

    public boolean isVisibleSelection() {
        return visibleSelection;
    }

    public void setVisibleSelection(boolean visibleSelection) {
        this.visibleSelection = visibleSelection;
    }
}
