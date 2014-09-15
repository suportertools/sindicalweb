package br.com.rtools.locadoraFilme.beans;

import br.com.rtools.locadoraFilme.Catalogo;
import br.com.rtools.locadoraFilme.Titulo;
import br.com.rtools.locadoraFilme.dao.CatalogoDao;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;

@ManagedBean
@SessionScoped
public class CatalogoBean {

    private Catalogo catalogo;
    private String comoPesquisa;
    private String descPesquisa;
    private List<Catalogo> listCatalogo;
    private int idCatalogo;
    private List<SelectItem> listTitulo;
    private int idTitulo;
    private List<SelectItem> listFilial;
    private int idFilial;
    private int quantidade;
    private Date data;

    @PostConstruct
    public void init() {
        catalogo = new Catalogo();
        listCatalogo = new ArrayList<Catalogo>();
        comoPesquisa = "";
        descPesquisa = "";
        idCatalogo = 0;
        listTitulo = new ArrayList<SelectItem>();
        idTitulo = 0;
        listFilial = new ArrayList<SelectItem>();
        idFilial = 0;
        quantidade = 0;
        data = new Date();
    }

    @PreDestroy
    public void destroy() {
        clear();
    }

    public void clear() {
        GenericaSessao.remove("catalogoBean");
        GenericaSessao.remove("catalogoPesquisa");
    }

    public synchronized void save() {
        CatalogoDao catalogoDao = new CatalogoDao();
        if (catalogo.getQuantidade() <= 0) {
            GenericaMensagem.warn("Validação", "Quantidade deve ser maior que 0!");
            return;
        }
        if (listFilial.isEmpty()) {
            GenericaMensagem.warn("Validação", "Cadastrar filiais!");
            return;
        }
        if (listTitulo.isEmpty()) {
            GenericaMensagem.warn("Validação", "Cadastrar títulos!");
            return;
        }
        Dao dao = new Dao();
        catalogo.setFilial((Filial) dao.find(new Filial(), Integer.parseInt(listFilial.get(idFilial).getDescription())));
        catalogo.setTitulo((Titulo) dao.find(new Titulo(), Integer.parseInt(listTitulo.get(idTitulo).getDescription())));
        NovoLog novoLog = new NovoLog();
        if (catalogo.getId() == -1) {
            if (!catalogoDao.verificaFilial(catalogo.getFilial(), catalogo.getTitulo())) {
                GenericaMensagem.warn("Validação", "Já existe esse catálogo para essa filial!");
                return;
            }
            if (dao.save(catalogo, true)) {
                GenericaMensagem.info("Sucesso", "Registro inserido");
                novoLog.save(""
                        + "ID: " + catalogo.getId()
                        + " - Filial: (" + catalogo.getFilial().getId() + ") - " + catalogo.getFilial().getFilial().getPessoa().getNome()
                        + " - Título: (" + catalogo.getTitulo().getId() + ") - " + catalogo.getTitulo().getDescricao()
                        + " - Quantidade: " + catalogo.getQuantidade()
                );
                listCatalogo.clear();
                catalogo = new Catalogo();
            } else {
                GenericaMensagem.warn("Erro", "Ao inserir registro!");
            }
        } else {
            Catalogo c = (Catalogo) dao.find(catalogo);
            String beforeUpdate = ""
                    + "ID: " + c.getId()
                    + " - Filial: (" + c.getFilial().getId() + ") - " + c.getFilial().getFilial().getPessoa().getNome()
                    + " - Título: (" + c.getTitulo().getId() + ") - " + c.getTitulo().getDescricao()
                    + " - Quantidade: " + c.getQuantidade();
            if (dao.update(catalogo, true)) {
                GenericaMensagem.info("Sucesso", "Registro atualizado");
                novoLog.update(beforeUpdate,
                        "ID: " + catalogo.getId()
                        + " - Filial: (" + catalogo.getFilial().getId() + ") - " + catalogo.getFilial().getFilial().getPessoa().getNome()
                        + " - Título: (" + catalogo.getTitulo().getId() + ") - " + catalogo.getTitulo().getDescricao()
                        + " - Quantidade: " + catalogo.getQuantidade()
                );
                catalogo = new Catalogo();
                listCatalogo.clear();
            } else {
                GenericaMensagem.warn("Erro", "Ao atualizar registro!");
            }
        }
    }

    public synchronized void delete() {
        if (catalogo.getId() != -1) {
            Dao dao = new Dao();
            NovoLog novoLog = new NovoLog();
            if (dao.delete(catalogo, true)) {
                GenericaMensagem.info("Sucesso", "Registro removido");
                novoLog.delete(""
                        + "ID: " + catalogo.getId()
                        + " - Filial: (" + catalogo.getFilial().getId() + ") - " + catalogo.getFilial().getFilial().getPessoa().getNome()
                        + " - Título: (" + catalogo.getTitulo().getId() + ") - " + catalogo.getTitulo().getDescricao()
                        + " - Quantidade: " + catalogo.getQuantidade()
                );
                clear();
            } else {
                GenericaMensagem.warn("Erro", "Ao remover registro!");
            }
        } else {
            GenericaMensagem.warn("Validação", "Nenhum registro selecionado!");
        }
    }

    public void acaoPesquisaInicial() {
        listCatalogo.clear();
        setComoPesquisa("I");
    }

    public void acaoPesquisaParcial() {
        listCatalogo.clear();
        setComoPesquisa("P");
    }

    public Catalogo getCatalogo() {
        return catalogo;
    }

    public List<SelectItem> getListFilial() {
        if (listFilial.isEmpty()) {
            Dao dao = new Dao();
            List<Filial> list = dao.list(new Filial(), true);
            for (int i = 0; i < list.size(); i++) {
                listFilial.add(new SelectItem(i, list.get(i).getFilial().getPessoa().getNome(), "" + list.get(i).getId()));
            }
        }
        return listFilial;
    }

    public void setListFilial(List<SelectItem> listFilial) {
        this.listFilial = listFilial;
    }

    public List<SelectItem> getListTitulo() {
        if (listTitulo.isEmpty()) {
            Dao dao = new Dao();
            List<Titulo> list = dao.list(new Titulo());
            for (int i = 0; i < list.size(); i++) {
                listTitulo.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        return listTitulo;
    }

    public void setistTitulo(List<SelectItem> listTitulo) {
        this.listTitulo = listTitulo;
    }

    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }

    public String getComoPesquisa() {
        return comoPesquisa;
    }

    public void setComoPesquisa(String comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    public String getDescPesquisa() {
        return descPesquisa;
    }

    public void setDescPesquisa(String descPesquisa) {
        this.descPesquisa = descPesquisa;
    }

    public int getIdTitulo() {
        return idTitulo;
    }

    public void setIdTitulo(int idTitulo) {
        this.idTitulo = idTitulo;
    }

    public int getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(int idFilial) {
        this.idFilial = idFilial;
    }

    public int getIdCatalogo() {
        return idCatalogo;
    }

    public void setIdCatalogo(int idCatalogo) {
        this.idCatalogo = idCatalogo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;

    }

    public List<Catalogo> getListCatalogo() {
        if (listCatalogo.isEmpty()) {
            CatalogoDao catalogoDao = new CatalogoDao();
            listCatalogo = catalogoDao.pesquisaCatalogoPorFilial(Integer.parseInt(getListFilial().get(idFilial).getDescription()));

        }
        return listCatalogo;
    }

    public void setListCatalogo(List<Catalogo> listCatalogo) {
        this.listCatalogo = listCatalogo;
    }

    public void edit(Catalogo c) {
        catalogo = c;
        for (int i = 0; i < listFilial.size(); i++) {
            if (catalogo.getFilial().getId() == Integer.parseInt(getListFilial().get(i).getDescription())) {
                idFilial = i;
                break;
            }
        }
        for (int i = 0; i < listTitulo.size(); i++) {
            if (catalogo.getTitulo().getId() == Integer.parseInt(getListTitulo().get(i).getDescription())) {
                idTitulo = i;
                break;
            }
        }
    }

    public String showImagem(Catalogo c) {
        if (c.getTitulo().getFoto() == null) {
            return "";
        }
        String caminho = "/Cliente/" + getCliente() + "/Imagens/locadora/titulo";
        String arquivo = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(caminho);
        for (String imagensTipo1 : new String[]{"png", "jpg", "jpeg", "gif"}) {
            File f = new File(arquivo + "/" + c.getTitulo().getId() + "." + imagensTipo1);
            if (f.exists()) {
                return caminho + "/" + c.getTitulo().getId() + "." + imagensTipo1;
            }
        }
        return "";
    }

    public String getCliente() {
        if (GenericaSessao.exists("sessaoCliente")) {
            return GenericaSessao.getString("sessaoCliente");
        }
        return "";
    }
}
