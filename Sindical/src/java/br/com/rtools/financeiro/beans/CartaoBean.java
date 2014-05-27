package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.Cartao;
import br.com.rtools.financeiro.Plano5;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class CartaoBean implements Serializable {

    private Cartao cartao;
    private List<Cartao> listaCartao;

    @PostConstruct
    public void init() {
        cartao = new Cartao();
        listaCartao = new ArrayList<Cartao>();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("cartaoBean");
        GenericaSessao.remove("pesquisaPlano");
    }

    public void salvar() {
        DaoInterface di = new Dao();
        if (cartao.getDescricao().isEmpty()) {
            GenericaMensagem.warn("Erro", "O campo descrição não pode estar vazio!");
            return;
        }
        NovoLog novoLog = new NovoLog();
        if (cartao.getId() == -1) {
            if (di.save(cartao, true)) {
                novoLog.save(
                        "ID: " + cartao.getId()
                        + " - Plano 5: (" + cartao.getPlano5().getId() + ") " + cartao.getPlano5().getConta()
                        + " - Descrição: " + cartao.getDescricao()
                        + " - Dias: " + cartao.getDias()
                        + " - Taxa: " + cartao.getTaxa()
                        + " - Débito/Crédito: " + cartao.getDebitoCredito()
                );
                GenericaMensagem.info("Sucesso", "Cartão salvo com Sucesso!");
                listaCartao.clear();
                cartao = new Cartao();
            } else {
                GenericaMensagem.warn("Erro", "Não foi possível salvar este Cartão!");
            }
        } else {
            Cartao c = (Cartao) di.find(cartao);
            String beforeUpdate
                    = "ID: " + c.getId()
                    + " - Plano 5: (" + c.getPlano5().getId() + ") " + c.getPlano5().getConta()
                    + " - Descrição: " + c.getDescricao()
                    + " - Dias: " + c.getDias()
                    + " - Taxa: " + c.getTaxa()
                    + " - Débito/Crédito: " + c.getDebitoCredito();
            if (di.update(cartao, true)) {
                novoLog.update(beforeUpdate,
                        "ID: " + cartao.getId()
                        + " - Plano 5: (" + cartao.getPlano5().getId() + ") " + cartao.getPlano5().getConta()
                        + " - Descrição: " + cartao.getDescricao()
                        + " - Dias: " + cartao.getDias()
                        + " - Taxa: " + cartao.getTaxa()
                        + " - Débito/Crédito: " + cartao.getDebitoCredito()
                );
                GenericaMensagem.info("Sucesso", "Cartão alterado com Sucesso!");
                listaCartao.clear();
                cartao = new Cartao();
            } else {
                GenericaMensagem.warn("Erro", "Não foi possível alterar este Cartão!");
            }
        }
    }

    public void excluir(Cartao c) {
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        if (di.delete(c, true)) {
            novoLog.delete(
                    "ID: " + c.getId()
                    + " - Plano 5: (" + c.getPlano5().getId() + ") " + c.getPlano5().getConta()
                    + " - Descrição: " + c.getDescricao()
                    + " - Dias: " + c.getDias()
                    + " - Taxa: " + c.getTaxa()
                    + " - Débito/Crédito: " + c.getDebitoCredito()
            );
            GenericaMensagem.info("Sucesso", "Cartão excluído com Sucesso!");
            listaCartao.clear();
            cartao = new Cartao();
        } else {
            GenericaMensagem.warn("Erro", "Este cartão não pode ser excluido!");
        }
    }

    public void editar(Cartao c) {
        cartao = c;
    }

    public void removerPlano() {
        cartao.setPlano5(new Plano5());
    }

    public Cartao getCartao() {
        if (GenericaSessao.exists("pesquisaPlano")) {
            cartao.setPlano5((Plano5) GenericaSessao.getObject("pesquisaPlano", true));
        }
        return cartao;
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    public List<Cartao> getListaCartao() {
        if (listaCartao.isEmpty()) {
            DaoInterface di = new Dao();
            listaCartao = di.list(new Cartao(), true);
        }
        return listaCartao;
    }

    public void setListaCartao(List<Cartao> listaCartao) {
        this.listaCartao = listaCartao;
    }

}
