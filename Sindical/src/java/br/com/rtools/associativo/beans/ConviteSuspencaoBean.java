package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.ConviteMotivoSuspencao;
import br.com.rtools.associativo.ConviteSuspencao;
import br.com.rtools.associativo.db.ConviteDB;
import br.com.rtools.associativo.db.ConviteDBToplink;
import br.com.rtools.sistema.SisPessoa;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class ConviteSuspencaoBean implements Serializable {

    private ConviteSuspencao conviteSuspencao = new ConviteSuspencao();
    private String mensagem = "";
    private String comoPesquisa = "I";
    private String porPesquisa = "";
    private String descricaoPesquisa = "";
    private boolean filtro = false;
    private boolean filtroPorPessoa = false;
    private List<ConviteSuspencao> listaPessoasSuspencas = new ArrayList<ConviteSuspencao>();
    private List<SelectItem> listaMotivoSuspencao = new ArrayList<SelectItem>();
    private int idSuspencao = 0;

    public void novo() {
        conviteSuspencao = new ConviteSuspencao();
        mensagem = "";
        listaPessoasSuspencas.clear();
        descricaoPesquisa = "";
    }

    public void salvar() {
        if (conviteSuspencao.getSisPessoa().getId() == -1) {
            mensagem = "Pesquisar pessoa!";
            return;
        }
        if (listaMotivoSuspencao.isEmpty()) {
            mensagem = "Informar o motivo da susponção!";
            return;
        }
        DataHoje hoje = new DataHoje();
        int dataHoje = DataHoje.converteDataParaInteger(DataHoje.data());
        int dataInicio = DataHoje.converteDataParaInteger(conviteSuspencao.getInicio());
        int dataFim = DataHoje.converteDataParaInteger(conviteSuspencao.getFim());
        if (dataInicio < dataHoje) {
            mensagem = "A data de inicio deve ser maior ou igual a data de hoje!";
            return;            
        }
        if (dataFim < dataInicio) {
            mensagem = "A data de fim deve ser maior ou igual a data final!";
            return;            
        }
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        conviteSuspencao.setConviteMotivoSuspencao((ConviteMotivoSuspencao) salvarAcumuladoDB.pesquisaObjeto(Integer.parseInt(listaMotivoSuspencao.get(idSuspencao).getDescription()), "ConviteMotivoSuspencao"));
        if (conviteSuspencao.getInicio().equals("")) {
            mensagem = "Informar data de inicio!";
            return;
        }
        mensagem = "";
        if (conviteSuspencao.getId() == -1) {
            ConviteDB conviteDB = new ConviteDBToplink();
            if (conviteDB.existeSisPessoaSuspensa(conviteSuspencao)) {
                mensagem = "Pessoa já existe para data específicada";
                return;
            }
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.inserirObjeto(conviteSuspencao)) {
                salvarAcumuladoDB.comitarTransacao();
                listaPessoasSuspencas.clear();
                mensagem = "Registro inserido com sucesso";
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                mensagem = "Erro ao inserir este registro!";
            }
        } else {
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.alterarObjeto(conviteSuspencao)) {
                salvarAcumuladoDB.comitarTransacao();
                listaPessoasSuspencas.clear();
                mensagem = "Registro atualizado com sucesso";
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                mensagem = "Pessoa já existe para data específicada";
            }
        }
    }

    public void editar(ConviteSuspencao cs) {
        listaPessoasSuspencas.clear();
        descricaoPesquisa = "";
        conviteSuspencao = cs;
        for (int i = 0; i < listaMotivoSuspencao.size(); i++) {
            if (Integer.parseInt(listaMotivoSuspencao.get(i).getDescription()) == cs.getId()) {
                idSuspencao = i;
                break;
            }
        }
    }

    public void remover(ConviteSuspencao cs) {
        if (cs.getId() != -1) {
            SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
            dB.abrirTransacao();
            if (dB.deletarObjeto((ConviteSuspencao) dB.pesquisaObjeto(cs.getId(), "ConviteSuspencao"))) {
                dB.comitarTransacao();
                listaPessoasSuspencas.clear();
                conviteSuspencao = new ConviteSuspencao();                
                GenericaMensagem.info("Sucesso", "Registro excluído");
            } else {
                dB.desfazerTransacao();
                GenericaMensagem.warn("Erro", "Ao excluir registro!");
            }
        }
    }

    public boolean isFiltro() {
        return filtro;
    }

    public void setFiltro(boolean filtro) {
        this.filtro = filtro;
    }

    public boolean isFiltroPorPessoa() {
        return filtroPorPessoa;
    }

    public void setFiltroPorPessoa(boolean filtroPorPessoa) {
        this.filtroPorPessoa = filtroPorPessoa;
    }

    public String getDescricaoPesquisa() {
        return descricaoPesquisa;
    }

    public void setDescricaoPesquisa(String descricaoPesquisa) {
        this.descricaoPesquisa = descricaoPesquisa;
    }

    public void acaoPesquisaInicial() {
        comoPesquisa = "I";
    }

    public void acaoPesquisaParcial() {
        comoPesquisa = "P";
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public ConviteSuspencao getConviteSuspencao() {
        if (GenericaSessao.exists("sisPessoaPesquisa")) {
            conviteSuspencao.setSisPessoa((SisPessoa) GenericaSessao.getObject("sisPessoaPesquisa", true));
        }
        return conviteSuspencao;
    }

    public void setConviteSuspencao(ConviteSuspencao conviteSuspencao) {
        this.conviteSuspencao = conviteSuspencao;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getComoPesquisa() {
        return comoPesquisa;
    }

    public void setComoPesquisa(String comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    public List<ConviteSuspencao> getListaPessoasSuspencas() {
        if (listaPessoasSuspencas.isEmpty()) {
            ConviteDB conviteDB = new ConviteDBToplink();
            if (descricaoPesquisa.equals("")) {
                if (conviteSuspencao.getSisPessoa().getId() != -1) {
                    listaPessoasSuspencas = (List<ConviteSuspencao>) conviteDB.listaPessoasSuspensas(conviteSuspencao, true, true);
                } else {
                    listaPessoasSuspencas = (List<ConviteSuspencao>) conviteDB.listaPessoasSuspensas(conviteSuspencao, filtro, false);
                }
            } else {
                listaPessoasSuspencas = (List<ConviteSuspencao>) conviteDB.listaPessoasSuspensas(conviteSuspencao, filtro, false, descricaoPesquisa, porPesquisa, comoPesquisa);
            }
        }
        return listaPessoasSuspencas;
    }

    public void setListaPessoasSuspencas(List<ConviteSuspencao> listaPessoasSuspencas) {
        this.listaPessoasSuspencas = listaPessoasSuspencas;
    }

    public List<SelectItem> getListaMotivoSuspencao() {
        if (listaMotivoSuspencao.isEmpty()) {
            SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
            List<ConviteMotivoSuspencao> list = (List<ConviteMotivoSuspencao>) dB.listaObjeto("ConviteMotivoSuspencao", true);
            int i = 0;
            for (ConviteMotivoSuspencao cms : list) {
                listaMotivoSuspencao.add(new SelectItem(i, cms.getDescricao(), "" + cms.getId()));
                i++;
            }
        }
        return listaMotivoSuspencao;
    }

    public void setListaMotivoSuspencao(List<SelectItem> listaMotivoSuspencao) {
        this.listaMotivoSuspencao = listaMotivoSuspencao;
    }

    public int getIdSuspencao() {
        return idSuspencao;
    }

    public void setIdSuspencao(int idSuspencao) {
        this.idSuspencao = idSuspencao;
    }
}
