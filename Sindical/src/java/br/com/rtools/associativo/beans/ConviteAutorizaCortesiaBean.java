package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.ConviteAutorizaCortesia;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class ConviteAutorizaCortesiaBean implements Serializable {

    private ConviteAutorizaCortesia conviteAutorizaCortesia = new ConviteAutorizaCortesia();
    private List<ConviteAutorizaCortesia> listaPessoasAutorizadas = new ArrayList();
    private String mensagem = "";

    public void salvar() {
        if (conviteAutorizaCortesia.getPessoa().getId() == -1) {
            mensagem = "Pesquisar pessoa!";
        }
        for (int i = 0; i < listaPessoasAutorizadas.size(); i++) {
            if(listaPessoasAutorizadas.get(i).getPessoa().getId() == conviteAutorizaCortesia.getPessoa().getId()) {
                mensagem = "Pessoa já cadastrada!";
                return;
            }
        }
        SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
        if (conviteAutorizaCortesia.getId() == -1) {
            dB.abrirTransacao();
            if (dB.inserirObjeto(conviteAutorizaCortesia)) {
                dB.comitarTransacao();
                mensagem = "Registro inserido com sucesso";
                listaPessoasAutorizadas.clear();
                NovoLog log = new NovoLog();
                log.novo("Salvar pessoa autoriza cortesia convite", "Id " + conviteAutorizaCortesia.getId() + " Pessoa: " + conviteAutorizaCortesia.getPessoa().getId() +" - "+ conviteAutorizaCortesia.getPessoa().getNome() +" - Documento: "+conviteAutorizaCortesia.getPessoa().getDocumento());                
            } else {
                dB.desfazerTransacao();
                mensagem = "Erro ao adicionar registro!";
            }
        }
        conviteAutorizaCortesia = new ConviteAutorizaCortesia();
    }

    public void novo() {
        conviteAutorizaCortesia = new ConviteAutorizaCortesia();
        listaPessoasAutorizadas.clear();
    }

    public void excluir(ConviteAutorizaCortesia cac) {
        if (cac.getId() != -1) {
            SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
            dB.abrirTransacao();
            if (dB.deletarObjeto((ConviteAutorizaCortesia) dB.pesquisaCodigo(cac.getId(), "ConviteAutorizaCortesia"))) {
                dB.comitarTransacao();
                mensagem = "Registro excluído com sucesso.";
                NovoLog log = new NovoLog();
                log.novo("Excluir pessoa autoriza cortesia convite", "Id " + cac.getId() + " Pessoa: " + cac.getPessoa().getId() +" - "+ cac.getPessoa().getNome() +" - Documento: "+cac.getPessoa().getDocumento());
                listaPessoasAutorizadas.clear();
            } else {
                dB.desfazerTransacao();
                mensagem = "Erro ao excluir registro!";
            }
        }
    }

    public ConviteAutorizaCortesia getConviteAutorizaCortesia() {
        if (GenericaSessao.exists("pessoaPesquisa")) {
            conviteAutorizaCortesia.setPessoa((Pessoa) GenericaSessao.getObject("pessoaPesquisa", true));
        }
        return conviteAutorizaCortesia;
    }

    public void setConviteAutorizaCortesia(ConviteAutorizaCortesia conviteAutorizaCortesia) {
        this.conviteAutorizaCortesia = conviteAutorizaCortesia;
    }

    public List<ConviteAutorizaCortesia> getListaPessoasAutorizadas() {
        if (listaPessoasAutorizadas.isEmpty()) {
            SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
            listaPessoasAutorizadas = (List<ConviteAutorizaCortesia>) dB.listaObjeto("ConviteAutorizaCortesia", true);
        }
        return listaPessoasAutorizadas;
    }

    public void setListaPessoasAutorizadas(List<ConviteAutorizaCortesia> listaPessoasAutorizadas) {
        this.listaPessoasAutorizadas = listaPessoasAutorizadas;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
