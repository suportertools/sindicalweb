package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.ConviteServico;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.utilitarios.GenericaMensagem;
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
public class ConviteServicoBean implements Serializable {

    private Servicos servicos = new Servicos();
    private ConviteServico conviteServico = new ConviteServico();
    private List<ConviteServico> listaConviteServicos = new ArrayList();
    private String mensagem = "";
    private String descricaoPesquisa = "";
    private List<SelectItem> listaServicos = new ArrayList();
    private int idServicos = 0;

    public void salvar() {
        if (listaServicos.isEmpty()) {
            GenericaMensagem.warn("Validação", "Cadastrar serviço!");
            return;
        }
        SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
        conviteServico.setServicos((Servicos) dB.pesquisaObjeto(Integer.parseInt(listaServicos.get(idServicos).getDescription()), "Servicos"));
        if (conviteServico.getId() == -1) {
            dB.abrirTransacao();
            if (dB.inserirObjeto(conviteServico)) {
                dB.comitarTransacao();
                mensagem = "Registro inserido com sucesso";
                listaConviteServicos.clear();
            } else {
                dB.desfazerTransacao();
                mensagem = "Erro ao adicionar registro!";
            }
        } else {
            dB.abrirTransacao();
            if (dB.alterarObjeto(conviteServico)) {
                dB.comitarTransacao();
                mensagem = "Registro atualizado com sucesso";
                listaConviteServicos.clear();
            } else {
                dB.desfazerTransacao();
                mensagem = "Erro ao atualizar registro!";
            }
        }
        conviteServico = new ConviteServico();
    }

    public void novo() {
        conviteServico = new ConviteServico();
        listaConviteServicos.clear();
        listaServicos.clear();
        idServicos = 0;
    }

    public void editar(ConviteServico cs) {
        conviteServico = new ConviteServico();
        conviteServico = cs;
        for (int i = 0; i < listaServicos.size(); i++) {
            if (Integer.parseInt(listaServicos.get(i).getDescription()) == cs.getServicos().getId()) {
                idServicos = i;
                break;
            }
        }
    }

    public void atualizarDiaSemana(ConviteServico cs) {
        if (cs.getId() != -1) {
            SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
            dB.abrirTransacao();
            if (dB.alterarObjeto(cs)) {
                if (cs.getId() == conviteServico.getId()) {
                    conviteServico = cs;
                }
                dB.comitarTransacao();
                GenericaMensagem.info("Sucesso", "Registro atualizado.");
                listaConviteServicos.clear();
            } else {
                dB.desfazerTransacao();
                GenericaMensagem.warn("Erro", "Ao atualizar registro!");
            }

        }
    }

    public void excluir() {
        excluir(conviteServico);
        conviteServico = new ConviteServico();
    }

    public void excluir(ConviteServico cs) {
        if (cs.getId() != -1) {
            SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
            dB.abrirTransacao();
            if (dB.deletarObjeto((ConviteServico) dB.pesquisaCodigo(cs.getId(), "ConviteServico"))) {
                dB.comitarTransacao();
                mensagem = "Registro excluído com sucesso";
                listaConviteServicos.clear();
            } else {
                dB.desfazerTransacao();
                mensagem = "Erro ao excluir registro!";
            }
        }
    }

    public Servicos getServicos() {
        return servicos;
    }

    public void setServicos(Servicos servicos) {
        this.servicos = servicos;
    }

    public ConviteServico getConviteServico() {
        return conviteServico;
    }

    public void setConviteServico(ConviteServico conviteServico) {
        this.conviteServico = conviteServico;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getDescricaoPesquisa() {
        return descricaoPesquisa;
    }

    public void setDescricaoPesquisa(String descricaoPesquisa) {
        this.descricaoPesquisa = descricaoPesquisa;
    }

    public List<ConviteServico> getListaConviteServicos() {
        if (listaConviteServicos.isEmpty()) {
            SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
            listaConviteServicos = (List<ConviteServico>) dB.listaObjeto("ConviteServico", true);
        }
        return listaConviteServicos;
    }

    public void setListaConviteServicos(List<ConviteServico> listaConviteServicos) {
        this.listaConviteServicos = listaConviteServicos;
    }

    public List<SelectItem> getListaServicos() {
        if (listaServicos.isEmpty()) {
            ServicosDB db = new ServicosDBToplink();
            List<Servicos> lista = (List<Servicos>) db.pesquisaTodos(215);
            int i = 0;
            for (Servicos s : lista) {
                listaServicos.add(new SelectItem(i, s.getDescricao(), Integer.toString((s.getId()))));
                i++;
            }
        }
        return listaServicos;
    }

    public void setListaServicos(List<SelectItem> listaServicos) {
        this.listaServicos = listaServicos;
    }

    public int getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(int idServicos) {
        this.idServicos = idServicos;
    }

}
