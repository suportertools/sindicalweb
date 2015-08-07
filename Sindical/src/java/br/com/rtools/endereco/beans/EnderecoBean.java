package br.com.rtools.endereco.beans;

import br.com.rtools.endereco.*;
import br.com.rtools.endereco.dao.BairroDao;
import br.com.rtools.endereco.dao.CidadeDao;
import br.com.rtools.endereco.dao.DescricaoEnderecoDao;
import br.com.rtools.endereco.dao.EnderecoDao;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.db.PessoaEnderecoDB;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.seguranca.controleUsuario.ChamadaPaginaBean;
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
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class EnderecoBean implements Serializable {

    private Cidade cidadeBase;
    private Endereco endereco;
    private boolean blDetalhada;
    private boolean pesquisar;
    private boolean btnCadastrar;
    private List<Endereco> listaEndereco;
    private List<SelectItem>[] listSelectItem;
    /**
     * <ul>
     * <li>0 - idIndex</li>
     * <li>1 - Cidade</li>
     * <li>2 - Logradouro</li>
     * <li>3 - Estado</li>
     * </ul>
     */
    private Integer[] index;
    private String mensagem;
    private String msgDetalhada;
    private String porPesquisa;

    @PostConstruct
    public void init() {
        endereco = new Endereco();
        cidadeBase = new Cidade();
        msgDetalhada = "";
        blDetalhada = false;
        listaEndereco = new ArrayList();
        index = new Integer[4];
        index[0] = -1;
        index[1] = 0;
        index[2] = 0;
        index[3] = 0;
        listSelectItem = new ArrayList[4];
        listSelectItem[0] = new ArrayList<SelectItem>();
        listSelectItem[1] = new ArrayList<SelectItem>();
        listSelectItem[2] = new ArrayList<SelectItem>();
        listSelectItem[3] = new ArrayList<SelectItem>();
        porPesquisa = "";
        mensagem = "";
        btnCadastrar = false;
        getListCidade();
        getListLogradouro();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("enderecoBean");
        GenericaSessao.remove("cidadePesquisa");
        GenericaSessao.remove("bairroPesquisa");
        GenericaSessao.remove("logradouroPesquisa");
        GenericaSessao.remove("descricaoEnderecoPesquisa");
    }

    public void pesquisaCep() {
        porPesquisa = "cep";
        listaEndereco.clear();
        find();
    }

    public void pesquisaInicial() {
        pesquisar = true;
        porPesquisa = "inicial";
        listaEndereco.clear();
        find();
    }

    public void pesquisaParcial() {
        pesquisar = true;
        porPesquisa = "parcial";
        listaEndereco.clear();
        find();
    }

    public String chamadaEndereco() {
        return ((ChamadaPaginaBean) GenericaSessao.getObject("chamadaPaginaBean")).endereco();
    }

    public void find() {
        EnderecoDao db = new EnderecoDao();
        Dao dao = new Dao();
        String descricao = "";
        List listDescricaoEndereco = new DescricaoEnderecoDao().find(endereco.getDescricaoEndereco().getDescricao(), false);
        DescricaoEndereco de = new DescricaoEndereco();
        Boolean err = null;
        if (!listDescricaoEndereco.isEmpty() && listDescricaoEndereco.size() == 1) {
            de = (DescricaoEndereco) listDescricaoEndereco.get(0);
            err = false;
        }
        if (err != null && err == false) {
            err = null;
            List<Endereco> list = db.pesquisaEndereco(
                    Integer.parseInt(getListLogradouro().get(index[2]).getDescription()),
                    de.getId(),
                    null,
                    Integer.parseInt(getListCidade().get(index[1]).getDescription()),
                    false
            );
            if (!list.isEmpty()) {
                dao.openTransaction();
            }
            for (int i = 0; i < list.size(); i++) {
                if (!list.get(i).getDescricaoEndereco().getAtivo()) {
                    if (new DescricaoEnderecoDao().exists(list.get(i).getDescricaoEndereco().getDescricao())) {
                        list.get(i).getDescricaoEndereco().setAtivo(true);
                        if (!dao.update(list.get(i).getDescricaoEndereco())) {
                            err = true;
                            break;
                        }
                    }
                }
                if (!list.get(i).getBairro().getAtivo()) {
                    if (new BairroDao().exists(list.get(i).getBairro().getDescricao())) {
                        list.get(i).getBairro().setAtivo(true);
                        if (!dao.update(list.get(i).getBairro())) {
                            err = true;
                            break;
                        }
                    }
                }
                list.get(i).setAtivo(true);
                if (!dao.update(list.get(i))) {
                    err = true;
                    break;
                }
                err = false;
            }
            if (err != null) {
                if (err == true) {
                    dao.rollback();
                    list.clear();
                } else {
                    dao.commit();
                }
            }
        }

        if (porPesquisa.equals("cep")) {
            listaEndereco = db.pesquisaEnderecoCep(endereco.getCep());
            descricao = "CEP: " + endereco.getCep();
        } else if (porPesquisa.equals("inicial") && pesquisar) {
            listaEndereco = db.pesquisaEnderecoDes(cidadeBase.getUf(),
                    ((Cidade) dao.find(new Cidade(), Integer.parseInt(getListCidade().get(index[1]).getDescription()))).getCidade(),
                    ((Logradouro) dao.find(new Logradouro(), Integer.parseInt(getListLogradouro().get(index[2]).getDescription()))).getDescricao(),
                    endereco.getDescricaoEndereco().getDescricao(), "I");
            listaEndereco = db.pesquisaEnderecoDes(cidadeBase.getUf(),
                    ((Cidade) dao.find(new Cidade(), Integer.parseInt(getListCidade().get(index[1]).getDescription()))).getCidade(),
                    ((Logradouro) dao.find(new Logradouro(), Integer.parseInt(getListLogradouro().get(index[2]).getDescription()))).getDescricao(),
                    endereco.getDescricaoEndereco().getDescricao(), "I");
            pesquisar = false;
        } else if (porPesquisa.equals("parcial") && pesquisar) {
            listaEndereco = db.pesquisaEnderecoDes(cidadeBase.getUf(),
                    ((Cidade) dao.find(new Cidade(), Integer.parseInt(getListCidade().get(index[1]).getDescription()))).getCidade(),
                    ((Logradouro) dao.find(new Logradouro(), Integer.parseInt(getListLogradouro().get(index[2]).getDescription()))).getDescricao(),
                    endereco.getDescricaoEndereco().getDescricao(), "P");
            pesquisar = false;
        }
        if (listaEndereco.isEmpty()) {
            GenericaMensagem.warn("Resultado", "Nenhum registro encontrado! " + descricao);
        }
    }

    public void save() throws Exception {
        mensagem = "";
        DaoInterface di = new Dao();
        EnderecoDao db = new EnderecoDao();
        Logradouro logradouro = (Logradouro) di.find(new Logradouro(), Integer.parseInt(getListLogradouro().get(index[2]).getDescription()));
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
        if (endereco.getCep().isEmpty() || endereco.getCep().equals("_____-___")) {
            mensagem = "Informar o cep!";
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
            e = (Endereco) di.find(new Endereco(), endereco.getId());
        }
        List<Endereco> listend = db.pesquisaEndereco(
                endereco.getLogradouro().getId(),
                endereco.getDescricaoEndereco().getId(),
                endereco.getBairro().getId(),
                endereco.getCidade().getId()
        );
        for (int i = 0; i < listend.size(); i++) {
            if (listend.get(i).getCep().equals(endereco.getCep()) && listend.get(i).getFaixa().equals(endereco.getFaixa())) {
                mensagem = "Endereço já Existente no Sistema!";
                return;
            }
        }
        NovoLog log = new NovoLog();
        di.openTransaction();
        if (endereco.getId() == -1) {
            if (di.save(endereco)) {
                di.commit();
                mensagem = "Endereço salvo com Sucesso!";
                log.save(endereco.getId() + " - " + endereco.getLogradouro().getDescricao() + " " + endereco.getDescricaoEndereco().getDescricao() + ", " + endereco.getFaixa() + " - " + endereco.getBairro().getDescricao() + " (" + endereco.getBairro().getId() + ") - " + endereco.getCidade().getCidade() + " (" + endereco.getCidade().getId() + ") - " + endereco.getCidade().getUf());
            } else {
                di.rollback();
                mensagem = "Erro ao Salvar!";
            }
        } else {
            if (di.update(endereco)) {
                di.commit();
                String antes = "De: " + e.getId() + " - " + e.getLogradouro().getDescricao() + " " + e.getDescricaoEndereco().getDescricao() + ", " + e.getFaixa() + " - " + e.getBairro().getDescricao() + " (" + e.getBairro().getId() + ") " + e.getCidade().getCidade() + " (" + e.getCidade().getId() + ") - " + e.getCidade().getUf();
                log.update(antes, endereco.getId() + " - " + endereco.getLogradouro().getDescricao() + " " + endereco.getDescricaoEndereco().getDescricao() + ", " + endereco.getFaixa() + " - " + endereco.getBairro().getDescricao() + " (" + endereco.getBairro().getId() + ") " + endereco.getCidade().getCidade() + " (" + endereco.getCidade().getId() + ") - " + endereco.getCidade().getUf());
                mensagem = "Endereço atualizado com Sucesso!";
            } else {
                di.rollback();
                mensagem = "Erro ao Salvar!";
            }
        }
        GenericaSessao.put("enderecoPesquisa", endereco);
    }

    public String edit() {
        Endereco e = (Endereco) listaEndereco.get(index[0]);
        return edit(e);
    }

    public String edit(Endereco e) {
        endereco = e;
        for (int i = 0; i < (getListLogradouro().size()); i++) {
            if (Integer.parseInt(getListLogradouro().get(i).getDescription()) == endereco.getLogradouro().getId()) {
                index[2] = i;
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

    public void clear() {
        GenericaSessao.remove("enderecoBean");
    }

    public void delete() {
        if (endereco.getId() != -1) {
            DaoInterface di = new Dao();
            di.openTransaction();
            if (di.delete((Endereco) di.find(endereco))) {
                di.commit();
                mensagem = "Endereço excluido com Sucesso!";
                NovoLog log = new NovoLog();
                log.delete(endereco.getId() + " - " + endereco.getLogradouro().getDescricao() + " " + endereco.getDescricaoEndereco().getDescricao() + ", " + endereco.getFaixa() + " - " + endereco.getBairro().getDescricao() + " (" + endereco.getBairro().getId() + ") " + endereco.getCidade().getCidade() + " (" + endereco.getCidade().getId() + ") - " + endereco.getCidade().getUf());
                clear();
            } else {
                di.rollback();
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

    public List<SelectItem> getListLogradouro() {
        if (listSelectItem[2].isEmpty()) {
            Dao dao = new Dao();
            List<Logradouro> list = dao.list(new Logradouro(), true);
            int j = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getDescricao().toUpperCase().equals("RUA")
                        || list.get(i).getDescricao().toUpperCase().equals("AVENIDA")
                        || list.get(i).getDescricao().toUpperCase().equals("TRAVESSA")
                        || list.get(i).getDescricao().toUpperCase().equals("PRAÇA")
                        || list.get(i).getDescricao().toUpperCase().equals("ALAMEDA")
                        || list.get(i).getDescricao().toUpperCase().equals("RODOVIA")
                        || list.get(i).getDescricao().toUpperCase().equals("ESTRADA")) {
                    listSelectItem[2].add(new SelectItem(j, (String) list.get(i).getDescricao().toUpperCase(), Integer.toString(list.get(i).getId())));
                    if (list.get(i).getDescricao().toUpperCase().equals("RUA")) {
                        index[2] = j;
                    }
                    list.remove(i);
                    j++;
                }
            }
            for (int i = 0; i < list.size(); i++) {
                if (!list.get(i).getDescricao().toUpperCase().equals("RUA")
                        || !list.get(i).getDescricao().toUpperCase().equals("AVENIDA")
                        || !list.get(i).getDescricao().toUpperCase().equals("TRAVESSA")
                        || !list.get(i).getDescricao().toUpperCase().equals("PRAÇA")
                        || !list.get(i).getDescricao().toUpperCase().equals("ALAMEDA")
                        || !list.get(i).getDescricao().toUpperCase().equals("RODOVIA")
                        || !list.get(i).getDescricao().toUpperCase().equals("ESTRADA")) {
                    listSelectItem[2].add(new SelectItem(j, (String) list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
                    j++;
                }
            }
        }
        return listSelectItem[2];
    }

    public List<SelectItem> getListEstado() {
        if (listSelectItem[3].isEmpty()) {
            listSelectItem[3].add(new SelectItem("AC", "AC", "DF"));
            listSelectItem[3].add(new SelectItem("AL", "AL", "AL"));
            listSelectItem[3].add(new SelectItem("AP", "AP", "AP"));
            listSelectItem[3].add(new SelectItem("AM", "AM", "AM"));
            listSelectItem[3].add(new SelectItem("BA", "BA", "BA"));
            listSelectItem[3].add(new SelectItem("CE", "CE", "CE"));
            listSelectItem[3].add(new SelectItem("DF", "DF", "DF"));
            listSelectItem[3].add(new SelectItem("ES", "ES", "ES"));
            listSelectItem[3].add(new SelectItem("GO", "GO", "GO"));
            listSelectItem[3].add(new SelectItem("MA", "MA", "MA"));
            listSelectItem[3].add(new SelectItem("MT", "MT", "MT"));
            listSelectItem[3].add(new SelectItem("MS", "MS", "MS"));
            listSelectItem[3].add(new SelectItem("MG", "MG", "MG"));
            listSelectItem[3].add(new SelectItem("PA", "PA", "PA"));
            listSelectItem[3].add(new SelectItem("PB", "PB", "PB"));
            listSelectItem[3].add(new SelectItem("PR", "PR", "PR"));
            listSelectItem[3].add(new SelectItem("PE", "PE", "PE"));
            listSelectItem[3].add(new SelectItem("PI", "PI", "PI"));
            listSelectItem[3].add(new SelectItem("RJ", "RJ", "RJ"));
            listSelectItem[3].add(new SelectItem("RN", "RN", "RN"));
            listSelectItem[3].add(new SelectItem("RS", "RS", "RS"));
            listSelectItem[3].add(new SelectItem("RO", "RO", "RO"));
            listSelectItem[3].add(new SelectItem("RR", "RR", "RR"));
            listSelectItem[3].add(new SelectItem("SC", "SC", "SC"));
            listSelectItem[3].add(new SelectItem("SP", "SP", "SP"));
            listSelectItem[3].add(new SelectItem("SE", "SE", "SE"));
            listSelectItem[3].add(new SelectItem("TO", "TO", "TO"));
        }
        return listSelectItem[3];
    }

    public List<SelectItem> getListCidade() {
        if (listSelectItem[1].isEmpty()) {
            PessoaEnderecoDB dbPes = new PessoaEnderecoDBToplink();
            DaoInterface di = new Dao();
            Filial fili = (Filial) di.find(new Filial(), 1);
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

            CidadeDao db = new CidadeDao();
            List select = db.pesquisaCidadeObj(cidadeBase.getUf());

            for (int i = 0; i < select.size(); i++) {
                listSelectItem[1].add(new SelectItem(i, (String) ((Cidade) select.get(i)).getCidade(), Integer.toString(((Cidade) select.get(i)).getId())));
                if (Integer.parseInt(listSelectItem[1].get(i).getDescription()) == cidadeBase.getId()) {
                    index[1] = i;
                }
            }
        } else {
            DaoInterface di = new Dao();
            cidadeBase = (Cidade) di.find(new Cidade(), Integer.parseInt(listSelectItem[1].get(index[1]).getDescription()));
            for (int i = 0; i < listSelectItem[1].size(); i++) {
                if (Integer.parseInt(listSelectItem[1].get(i).getDescription()) == cidadeBase.getId()) {
                    index[1] = i;
                }
            }
        }
        return listSelectItem[1];
    }

    public List<Endereco> getListaEndereco() {
        return listaEndereco;
    }

    public void setListaEndereco(List<Endereco> listaEndereco) {
        this.listaEndereco = listaEndereco;
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

    public Cidade getCidadeBase() {
        if (!pesquisar) {
            getListCidade().clear();
            index[1] = 0;
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

    public void listenerCadastrar() {
        GenericaSessao.put("cadastrarEndereco", true);
    }

    public boolean isBtnCadastrar() {
        if (GenericaSessao.exists("cadastrarEndereco")) {
            GenericaSessao.remove("cadastrarEndereco");
            btnCadastrar = true;
        }
        return btnCadastrar;
    }

    public void setBtnCadastrar(boolean btnCadastrar) {
        this.btnCadastrar = btnCadastrar;
    }

    public String btnPessoaJuridica() {
        GenericaSessao.put("linkClicado", true);
        return "pessoaJuridica";
    }

    public Integer[] getIndex() {
        return index;
    }

    public void setIndex(Integer[] index) {
        this.index = index;
    }
}
