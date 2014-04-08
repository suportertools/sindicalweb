package br.com.rtools.endereco.beans;

import br.com.rtools.endereco.*;
import br.com.rtools.endereco.db.CidadeDB;
import br.com.rtools.endereco.db.CidadeDBToplink;
import br.com.rtools.endereco.db.EnderecoDB;
import br.com.rtools.endereco.db.EnderecoDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.db.PessoaEnderecoDB;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class EnderecoBean implements Serializable {

    private Endereco endereco;
    private Cidade cidadeBase;
    private String msgDetalhada;
    private boolean blDetalhada;
    private boolean pesquisar;
    private List<Endereco> listaEndereco;
    private List<SelectItem> listaLogradouro;
    private List<SelectItem> listaCidade;
    private int idIndex;
    private int idLogradouro;
    private int idCidade;
    private String porPesquisa;
    private String mensagem = "";
    private boolean limpar;

    public EnderecoBean() {
        endereco = new Endereco();
        cidadeBase = new Cidade();
        msgDetalhada = "";
        blDetalhada = false;
        listaEndereco = new ArrayList();
        idIndex = -1;
        porPesquisa = "";
        listaLogradouro = new ArrayList();
        listaCidade = new ArrayList();
        mensagem = "";
        limpar = false;
        idLogradouro = 0;
        getListaCidade();
        getListaLogradouro();
    }

    public void pesquisaCep() {
        porPesquisa = "cep";
        listaEndereco.clear();
    }

    public void pesquisaInicial() {
        pesquisar = true;
        porPesquisa = "inicial";
        listaEndereco.clear();
    }

    public void pesquisaParcial() {
        pesquisar = true;
        porPesquisa = "parcial";
        listaEndereco.clear();
    }

    public String endereco() {
        return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).endereco();
    }

    public void salvar() throws Exception {
        mensagem = "";
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        EnderecoDB db = new EnderecoDBToplink();
        NovoLog log = new NovoLog();
        Logradouro logradouro = (Logradouro) sv.find(new Logradouro(), Integer.parseInt(listaLogradouro.get(idLogradouro).getDescription()));
        endereco.setLogradouro(logradouro);
        if (endereco.getDescricaoEndereco().getId() == -1) {
            mensagem = "O campo Descrição Endereço deve ser preenchido!";
            return;
        }
        if (endereco.getCidade().getId() == -1) {
            mensagem = "O campo Cidade deve ser preenchido!";
            return;
        }
        if (endereco.getBairro().getId() == -1) {
            mensagem = "O campo Bairro deve ser preenchido!";
            return;
        }
        if (endereco.getLogradouro() == null) {
            mensagem = "O campo Logradouro está inválido!";
            return;
        }
        String cep = "";
        if (!endereco.getCep().equals("")) {
            cep = endereco.getCep().substring(0, 5);
            cep = cep + endereco.getCep().substring(6, 9);
            endereco.setCep(cep);
        }
        Endereco e;
        if (endereco.getId() == -1) {
            e = endereco;
        } else {
            e = (Endereco) sv.find(new Endereco(), endereco.getId());
        }
        List<Endereco> listend = db.pesquisaEndereco(endereco.getDescricaoEndereco().getId(),
                endereco.getCidade().getId(),
                endereco.getBairro().getId(),
                endereco.getLogradouro().getId());
        if (!listend.isEmpty()) {
            for (int i = 0; i < listend.size(); i++) {
                if (listend.get(i).getCep().equals(endereco.getCep()) && listend.get(i).getFaixa().equals(endereco.getFaixa())) {
                    mensagem = "Endereço já Existente no Sistema!";
                    return;
                }
            }
        }
        sv.abrirTransacao();
        if (endereco.getId() == -1) {
            if (sv.inserirObjeto(endereco)) {
                sv.comitarTransacao();
                mensagem = "Endereço salvo com Sucesso!";
                log.novo("Novo registro", "Endereco inserido " + endereco.getId() + " - " + endereco.getLogradouro().getDescricao() + " " + endereco.getDescricaoEndereco().getDescricao() + ", " + endereco.getFaixa() + " - " + endereco.getBairro().getDescricao() + " (" + endereco.getBairro().getId() + ") - " + endereco.getCidade().getCidade() + " (" + endereco.getCidade().getId() + ") - " + endereco.getCidade().getUf());
            } else {
                sv.desfazerTransacao();
                mensagem = "Erro ao Salvar!";
            }
        } else {
            if (sv.alterarObjeto(endereco)) {
                sv.comitarTransacao();
                String antes = "De: " + e.getId() + " - " + e.getLogradouro().getDescricao() + " " + e.getDescricaoEndereco().getDescricao() + ", " + e.getFaixa() + " - " + e.getBairro().getDescricao() + " (" + e.getBairro().getId() + ") " + e.getCidade().getCidade() + " (" + e.getCidade().getId() + ") - " + e.getCidade().getUf();
                log.novo("Atualizado", antes + " - para: " + endereco.getId() + " - " + endereco.getLogradouro().getDescricao() + " " + endereco.getDescricaoEndereco().getDescricao() + ", " + endereco.getFaixa() + " - " + endereco.getBairro().getDescricao() + " (" + endereco.getBairro().getId() + ") " + endereco.getCidade().getCidade() + " (" + endereco.getCidade().getId() + ") - " + endereco.getCidade().getUf());
                mensagem = "Endereço atualizado com Sucesso!";
            } else {
                sv.desfazerTransacao();
                mensagem = "Erro ao Salvar!";
            }
        }
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("enderecoPesquisa", endereco);
    }

    public String editar() {
        Endereco e = (Endereco) listaEndereco.get(getIdIndex());
        return editar(e);
    }

    public String editar(Endereco e) {
        endereco = e;
        for (int i = 0; i < (listaLogradouro.size()); i++) {
            if (Integer.parseInt(listaLogradouro.get(i).getDescription()) == endereco.getLogradouro().getId()) {
                idLogradouro = i;
                break;
            }
        }
        String url = (String) GenericaSessao.getString("urlRetorno");
        GenericaSessao.put("linkClicado", true);
        if (url != null) {
            GenericaSessao.put("enderecoPesquisa", endereco);
            return url;
        }
        return "endereco";
    }

    public void novo() {
        endereco = new Endereco();
        listaEndereco.clear();
        listaLogradouro.clear();
        limpar = true;
    }

    public void limpar() {
        if (limpar == true) {
            novo();
        }
    }

    public void excluir() {
        if (endereco.getId() != -1) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            NovoLog log = new NovoLog();
            sv.abrirTransacao();
            endereco = (Endereco) sv.find(endereco);
            if (sv.deletarObjeto(endereco)) {
                sv.comitarTransacao();
                mensagem = "Endereço excluido com Sucesso!";
                log.novo("Excluido", endereco.getId() + " - " + endereco.getLogradouro().getDescricao() + " " + endereco.getDescricaoEndereco().getDescricao() + ", " + endereco.getFaixa() + " - " + endereco.getBairro().getDescricao() + " (" + endereco.getBairro().getId() + ") " + endereco.getCidade().getCidade() + " (" + endereco.getCidade().getId() + ") - " + endereco.getCidade().getUf());
                novo();
            } else {
                sv.desfazerTransacao();
                mensagem = "Endereço não pode ser excluido!";
            }
        }
    }

    public Endereco getEndereco() {
        if (GenericaSessao.exists("simplesPesquisa")) {
            try {
                Bairro bairro = (Bairro) GenericaSessao.getObject("simplesPesquisa");
                endereco.setBairro(bairro);
            } catch (Exception e) {
                DescricaoEndereco descricaoEndereco = (DescricaoEndereco) GenericaSessao.getObject("simplesPesquisa");
                endereco.setDescricaoEndereco(descricaoEndereco);
            }
            GenericaSessao.remove("simplesPesquisa");
        }
        if (GenericaSessao.exists("cidadePesquisa")) {
            endereco.setCidade((Cidade) GenericaSessao.getObject("cidadePesquisa", true));
        }
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getMsgDetalhada() {
        return msgDetalhada;
    }

    public void setMsgDetalhada(String msgDetalhada) {
        this.msgDetalhada = msgDetalhada;
    }

    public List<SelectItem> getListaLogradouro() {
        if (listaLogradouro.isEmpty()) {
            EnderecoDB db = new EnderecoDBToplink();
            List<Logradouro> select = db.pesquisaTodosOrdenado();
            for (int i = 0; i < select.size(); i++) {
                listaLogradouro.add(new SelectItem(i, (String) select.get(i).getDescricao(), Integer.toString(select.get(i).getId())));
                if (select.get(i).getDescricao().equals("Rua")) {
                    idLogradouro = i;
                }
            }
        }
        return listaLogradouro;
    }

    public void setListaLogradouro(List<SelectItem> listaLogradouro) {
        this.listaLogradouro = listaLogradouro;
    }

    public List<SelectItem> getListaCidade() {
        if (listaCidade.isEmpty()) {
            PessoaEnderecoDB dbPes = new PessoaEnderecoDBToplink();
            SalvarAcumuladoDB acumuladoDB = new SalvarAcumuladoDBToplink();
            Filial fili = (Filial) acumuladoDB.pesquisaCodigo(1, "Filial");
            if (fili == null) {
                msgDetalhada = "Não existe filial, CRIE uma e "
                        + " vincule o endereço para evitar futuros erros!";
                return new ArrayList();
            }
            if (cidadeBase.getUf().isEmpty()) {
                Pessoa pes = fili.getMatriz().getPessoa();
                Cidade cidade = ((PessoaEndereco) dbPes.pesquisaEndPorPessoa(pes.getId()).get(0)).getEndereco().getCidade();
                cidadeBase = cidade;
            }

            CidadeDB db = new CidadeDBToplink();
            List select = db.pesquisaCidadeObj(cidadeBase.getUf());

            for (int i = 0; i < select.size(); i++) {
                listaCidade.add(new SelectItem(i, (String) ((Cidade) select.get(i)).getCidade(), Integer.toString(((Cidade) select.get(i)).getId())));
                if (Integer.parseInt(listaCidade.get(i).getDescription()) == cidadeBase.getId()) {
                    idCidade = i;
                }
            }
        } else {
            CidadeDB db = new CidadeDBToplink();
            cidadeBase = db.pesquisaCodigo(Integer.parseInt(listaCidade.get(idCidade).getDescription()));
            for (int i = 0; i < listaCidade.size(); i++) {
                if (Integer.parseInt(listaCidade.get(i).getDescription()) == cidadeBase.getId()) {
                    idCidade = i;
                }
            }
        }
        return listaCidade;
    }

    public void setListaCidade(List<SelectItem> listaCidade) {
        this.listaCidade = listaCidade;
    }

    public List<Endereco> getListaEndereco() {
        if (listaEndereco.isEmpty()) {
            EnderecoDB db = new EnderecoDBToplink();
            if (porPesquisa.equals("cep")) {
                listaEndereco = db.pesquisaEnderecoCep(endereco.getCep());
            } else if (porPesquisa.equals("inicial") && pesquisar) {
                listaEndereco = db.pesquisaEnderecoDes(cidadeBase.getUf(),
                        db.pesquisaCidade(Integer.parseInt(getListaCidade().get(idCidade).getDescription())).getCidade(),
                        db.pesquisaLogradouro(Integer.parseInt(listaLogradouro.get(idLogradouro).getDescription())).getDescricao(),
                        endereco.getDescricaoEndereco().getDescricao(), "I");
                pesquisar = false;
            } else if (porPesquisa.equals("parcial") && pesquisar) {
                listaEndereco = db.pesquisaEnderecoDes(cidadeBase.getUf(),
                        db.pesquisaCidade(Integer.parseInt(getListaCidade().get(idCidade).getDescription())).getCidade(),
                        db.pesquisaLogradouro(Integer.parseInt(listaLogradouro.get(idLogradouro).getDescription())).getDescricao(),
                        endereco.getDescricaoEndereco().getDescricao(), "P");
                pesquisar = false;
            }
        }
        return listaEndereco;
    }

    public void setListaEndereco(List<Endereco> listaEndereco) {
        this.listaEndereco = listaEndereco;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public boolean isBlDetalhada() {
        if (msgDetalhada.isEmpty()) {
            blDetalhada = false;
        } else {
            blDetalhada = true;
        }
        return blDetalhada;
    }

    public void setBlDetalhada(boolean blDetalhada) {
        this.blDetalhada = blDetalhada;
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public int getIdLogradouro() {
        return idLogradouro;
    }

    public void setIdLogradouro(int idLogradouro) {
        this.idLogradouro = idLogradouro;
    }

    public int getIdCidade() {
        return idCidade;
    }

    public void setIdCidade(int idCidade) {
        this.idCidade = idCidade;
    }

    public Cidade getCidadeBase() {
        if (!pesquisar) {
            listaCidade.clear();
            idCidade = 0;
        }
        return cidadeBase;
    }

    public void setCidadeBase(Cidade cidadeBase) {
        this.cidadeBase = cidadeBase;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
