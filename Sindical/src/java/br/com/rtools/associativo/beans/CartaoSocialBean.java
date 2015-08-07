package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.AutorizaImpressaoCartao;
import br.com.rtools.associativo.ConfiguracaoSocial;
import br.com.rtools.associativo.GrupoCategoria;
import br.com.rtools.associativo.HistoricoCarteirinha;
import br.com.rtools.associativo.ModeloCarteirinha;
import br.com.rtools.associativo.SocioCarteirinha;
import br.com.rtools.associativo.Socios;
import br.com.rtools.associativo.db.CategoriaDB;
import br.com.rtools.associativo.db.CategoriaDBToplink;
import br.com.rtools.associativo.db.SocioCarteirinhaDB;
import br.com.rtools.associativo.db.SocioCarteirinhaDBToplink;
import br.com.rtools.associativo.db.SociosDB;
import br.com.rtools.associativo.db.SociosDBToplink;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.impressao.Etiquetas;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaEmpresa;
import br.com.rtools.pessoa.db.FisicaDBToplink;
import br.com.rtools.pessoa.db.PessoaEmpresaDB;
import br.com.rtools.pessoa.db.PessoaEmpresaDBToplink;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.ImpressaoParaSocios;
import br.com.rtools.utilitarios.Jasper;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
// import java.util.Vector;
import javax.faces.model.SelectItem;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.event.data.FilterEvent;
import org.primefaces.event.data.PageEvent;

@ManagedBean
@SessionScoped
public class CartaoSocialBean implements Serializable {

    /**
     * NOVO *
     */
    private String descricao = "";
    private List<Vector> listaCarteirinha = new ArrayList();
    private List<Vector> filteredCarteirinha = new ArrayList();
    private List<Vector> listaSelecionado = new ArrayList();
    private List<Vector> listaSelecionadoMemoria = new ArrayList();
    private List<SelectItem> listFilial = new ArrayList();
    private List listaHistorico = new ArrayList();
    private String por = "";
    private String porLabel = "";
    private String indexOrdem = "0";
    private Integer page;
    private Integer idFilial = 0;
    private Boolean toggle = false;
    private Integer firstIndex = 0;
    private Integer lastIndex = 0;
    private Boolean disabled;

    public CartaoSocialBean() {
        ConfiguracaoSocial configuracaoSocial = (ConfiguracaoSocial) new Dao().find(new ConfiguracaoSocial(), 1);
        disabled = false;
        if (configuracaoSocial.isControlaCartaoFilial()) {
            disabled = true;
        }
        getListFilial();
        this.naoImpressoTodos();
        Jasper.load();
    }

    public void historicoCarteirinha() {
        if (listaSelecionado.size() > 0) {
            SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();

            listaHistorico.clear();
            for (int i = 0; i < listaSelecionado.size(); i++) {
                List<HistoricoCarteirinha> listah = db.listaHistoricoCarteirinha((Integer) listaSelecionado.get(i).get(0));
                for (HistoricoCarteirinha listah1 : listah) {

                    AutorizaImpressaoCartao ai = db.pesquisaAutorizaPorHistorico(listah1.getId());

                    listaHistorico.add(new DataObject(listah1, "", listaSelecionado.get(i).get(5) + " - " + listaSelecionado.get(i).get(7), ai));

                }
            }
        }
    }

    public void naoImpressoTodos() {
        por = "niEmpresaTodos";
        porLabel = "Lista de TODOS NÃO IMPRESSOS";
        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        listaCarteirinha = db.pesquisaCarteirinha("niEmpresa", "", indexOrdem, getFilialInteger());
    }

    public void naoImpressoEmpresa() {
        por = "niEmpresa";
        porLabel = "Pesquisa por Não Impressos / EMPRESAS";
        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        listaCarteirinha = db.pesquisaCarteirinha("niEmpresa", descricao, indexOrdem, getFilialInteger());
    }

    public void naoImpressoCNPJ() {
        por = "niCNPJ";
        porLabel = "Pesquisa por Não Impressos / CNPJ";
        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        listaCarteirinha = db.pesquisaCarteirinha("niCNPJ", descricao, indexOrdem, getFilialInteger());
    }

    public void impressoTodos() {
        por = "iEmpresaTodos";
        porLabel = "Lista de TODOS IMPRESSOS";
        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        listaCarteirinha = db.pesquisaCarteirinha("iEmpresa", "", indexOrdem, getFilialInteger());
    }

    public void impressoEmpresa() {
        por = "iEmpresa";
        porLabel = "Pesquisa por Impressos / EMPRESAS";
        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        listaCarteirinha = db.pesquisaCarteirinha("iEmpresa", descricao, indexOrdem, getFilialInteger());
    }

    public void impressoCNPJ() {
        por = "iCNPJ";
        porLabel = "Pesquisa por Impressos / CNPJ";
        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        listaCarteirinha = db.pesquisaCarteirinha("iCNPJ", descricao, indexOrdem, getFilialInteger());
    }

    /**
     * dias == 0 (Hoje) dias == 1 (Ontem) dias == 2 (Ultimos 30 dias)
     *
     * @param dias
     */
    public void impressoDias(Integer dias) {
        por = "iDias";
        porLabel = "Pesquisa por Impressos / ÚLTIMOS 30 DIAS";
        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        String tipo = "";
        if (dias == 0) {
            tipo = "iHoje";
        } else if (dias == 1) {
            tipo = "iOntem";
        } else if (dias == 2) {
            tipo = "iDias";
        }
        listaCarteirinha = db.pesquisaCarteirinha(tipo, descricao, indexOrdem, getFilialInteger());
    }

    public void pessoaNome() {
        por = "iNome";
        porLabel = "Pesquisa por Pessoa / NOME";
        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        listaCarteirinha = db.pesquisaCarteirinha("iNome", descricao, indexOrdem, getFilialInteger());
    }

    public void sociosMatricula() {
        por = "iMatricula";
        porLabel = "Pesquisa por Sócio / MATRÍCULA";
        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        if (descricao.isEmpty()) {
            listaCarteirinha = new ArrayList<>();
            return;
        }
        listaCarteirinha = db.pesquisaCarteirinha("iMatricula", descricao, indexOrdem, getFilialInteger());
    }

    public void pessoaID() {
        por = "iID";
        porLabel = "Pesquisa por Pessoa / Código";
        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        if (!descricao.isEmpty()) {
            listaCarteirinha = db.pesquisaCarteirinha("iID", descricao, indexOrdem, getFilialInteger());
        } else {
            listaCarteirinha = new ArrayList<>();
        }
    }

    public void pessoaCPF() {
        por = "iCPF";
        porLabel = "Pesquisa por Pessoa / CPF";
        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        listaCarteirinha = db.pesquisaCarteirinha("iCPF", descricao, indexOrdem, getFilialInteger());
    }

    public void pesquisar() {
        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        if (por.equals("niEmpresa")) {
            listaCarteirinha = db.pesquisaCarteirinha("niEmpresa", descricao, indexOrdem, getFilialInteger());
        }

        if (por.equals("niCNPJ")) {
            listaCarteirinha = db.pesquisaCarteirinha("niCNPJ", descricao, indexOrdem, getFilialInteger());
        }

        if (por.equals("iEmpresa")) {
            listaCarteirinha = db.pesquisaCarteirinha("iEmpresa", descricao, indexOrdem, getFilialInteger());
        }

        if (por.equals("iCNPJ")) {
            listaCarteirinha = db.pesquisaCarteirinha("iCNPJ", descricao, indexOrdem, getFilialInteger());
        }

        if (por.equals("iDias")) {
            listaCarteirinha = db.pesquisaCarteirinha("iDias", descricao, indexOrdem, getFilialInteger());
        }

        if (por.equals("iNome")) {
            listaCarteirinha = db.pesquisaCarteirinha("iNome", descricao, indexOrdem, getFilialInteger());
        }

        if (por.equals("iID")) {
            listaCarteirinha = db.pesquisaCarteirinha("iID", descricao, indexOrdem, getFilialInteger());
        }

        if (por.equals("iMatricula")) {
            listaCarteirinha = db.pesquisaCarteirinha("iMatricula", descricao, indexOrdem, getFilialInteger());
        }

        if (por.equals("iCPF")) {
            listaCarteirinha = db.pesquisaCarteirinha("iCPF", descricao, indexOrdem, getFilialInteger());
        }
    }

    public void imprimirCarteirinha() {
        imprimirCarteirinha(null);
    }

    public void imprimirCarteirinha(Vector vector) {
        Dao dao = new Dao();
        List<Vector> list = new ArrayList();
        if (!listaSelecionado.isEmpty() && vector == null) {
            list = listaSelecionado;
        } else if (vector != null) {
            list.add(vector);
        }
        if (!list.isEmpty()) {
            dao.openTransaction();
            SocioCarteirinhaDB dbc = new SocioCarteirinhaDBToplink();
            for (int i = 0; i < list.size(); i++) {
                Integer titular_id = (Integer) ((List) list.get(i)).get(40);
                Pessoa pessoa = (Pessoa) dao.find(new Pessoa(), (Integer) ((List) list.get(i)).get(0));
                SocioCarteirinha carteirinha = (SocioCarteirinha) dao.find(new SocioCarteirinha(), (Integer) ((List) list.get(i)).get(19));

                boolean validacao = false;
                if (pessoa.getSocios().getId() != -1){
                    Fisica f = new FisicaDBToplink().pesquisaFisicaPorPessoa(pessoa.getId());
                    if (pessoa.getSocios().getMatriculaSocios().getCategoria().isEmpresaObrigatoria() && 
                        f.getDtAposentadoria() == null && 
                        titular_id == pessoa.getId()
                        ){
                        PessoaEmpresaDB db = new PessoaEmpresaDBToplink();                        
                        PessoaEmpresa pe = db.pesquisaPessoaEmpresaPorPessoa(pessoa.getId());
                        //PessoaEmpresa pe = db.pesquisaPessoaEmpresaPorPessoa(titular_id);
                        if (pe.getId() == -1){
                            GenericaMensagem.error("Atenção", "Empresa Não Vinculada a pessoa "+ pessoa.getNome());
                            validacao = true;
                        }
                    }
                }
                
                if (validacao){
                    dao.rollback();
                    return;
                }
                
                //ModeloCarteirinha modeloc = dbc.pesquisaModeloCarteirinha(-1, 170);
                //ModeloCarteirinha modeloc = (ModeloCarteirinha) sv.pesquisaCodigo((Integer) ((List) listaSelecionado.get(i)).get(19), "ModeloCarteirinha");
                //carteirinha = dbc.pesquisaCarteirinhaPessoa(pessoa.getId(), modeloc.getId());
                if (carteirinha.getDtEmissao() == null) {

                    carteirinha.setEmissao(DataHoje.data());
                    if (!dao.update(carteirinha)) {
                        dao.rollback();
                        return;
                    }
                    list.get(i).set(6, carteirinha.getValidadeCarteirinha());
                    HistoricoCarteirinha hc = new HistoricoCarteirinha();

                    hc.setCarteirinha(carteirinha);
                    hc.setDescricao("Primeira Impressão de Carteirinha");

                    if (list.get(i).get(17) != null) {
                        Movimento m = (Movimento) dao.find(new Movimento(), Integer.valueOf(list.get(i).get(17).toString()));
                        if (m != null) {
                            hc.setMovimento(m);
                        }
                    }

                    if (!dao.save(hc)) {
                        dao.rollback();
                        return;
                    }

                    //AutorizaImpressaoCartao ai = dbc.pesquisaAutorizaSemHistorico(pessoa.getId(), modeloc.getId());
                    AutorizaImpressaoCartao ai = dbc.pesquisaAutorizaSemHistorico(pessoa.getId(), carteirinha.getModeloCarteirinha().getId());

                    if (ai != null) {
                        ai.setHistoricoCarteirinha(hc);
                        if (!dao.update(ai)) {
                            dao.rollback();
                            return;
                        }
                    }

                } else {
                    HistoricoCarteirinha hc = new HistoricoCarteirinha();

                    hc.setCarteirinha(carteirinha);
                    hc.setDescricao("Impressão de Carteirinha");

                    if (list.get(i).get(17) != null) {
                        Movimento m = (Movimento) dao.find(new Movimento(), Integer.valueOf(list.get(i).get(17).toString()));
                        if (m != null) {
                            hc.setMovimento(m);
                        }
                    }

                    if (!dao.save(hc)) {
                        dao.rollback();
                        return;
                    }

                    //AutorizaImpressaoCartao ai = dbc.pesquisaAutorizaSemHistorico(pessoa.getId(), modeloc.getId());
                    AutorizaImpressaoCartao ai = dbc.pesquisaAutorizaSemHistorico(pessoa.getId(), carteirinha.getModeloCarteirinha().getId());

                    if (ai != null) {
                        ai.setHistoricoCarteirinha(hc);
                        if (!dao.update(ai)) {
                            dao.rollback();
                            return;
                        }
                    }
                }
            }

            if (ImpressaoParaSocios.imprimirCarteirinha(list)) {
                dao.commit();
                listaCarteirinha.clear();
                listaSelecionado.clear();
                listaSelecionadoMemoria.clear();
            } else {
                dao.rollback();
            }
        }
    }

    public void reImprimirCarteirinha() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

        if (!listaSelecionado.isEmpty()) {
            SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
            CategoriaDB dbCat = new CategoriaDBToplink();
            DataHoje dh = new DataHoje();
            SociosDB dbs = new SociosDBToplink();

            sv.abrirTransacao();

            for (int i = 0; i < listaSelecionado.size(); i++) {
                Pessoa pessoa = (Pessoa) sv.pesquisaCodigo((Integer) ((List) listaSelecionado.get(i)).get(0), "Pessoa");
                Socios socios = dbs.pesquisaSocioPorPessoa(pessoa.getId());
                SocioCarteirinha carteirinha = (SocioCarteirinha) sv.pesquisaCodigo((Integer) ((List) listaSelecionado.get(i)).get(19), "SocioCarteirinha");

                if (socios.getId() != -1 && socios.getMatriculaSocios().getId() != -1) {
                    GrupoCategoria gpCat = dbCat.pesquisaGrupoPorCategoria(Integer.valueOf(socios.getMatriculaSocios().getCategoria().getId()));
                    Date validadeCarteirinha = DataHoje.converte(dh.incrementarMeses(gpCat.getNrValidadeMesCartao(), DataHoje.data()));
                    carteirinha.setDtValidadeCarteirinha(validadeCarteirinha);
                } else {
                    carteirinha.setDtValidadeCarteirinha(null);
                }

                carteirinha.setVia(carteirinha.getVia() + 1);
                listaSelecionado.get(i).set(6, carteirinha.getValidadeCarteirinha());

                if (carteirinha.getDtEmissao() == null) {
                    carteirinha.setEmissao(DataHoje.data());

                    if (!sv.alterarObjeto(carteirinha)) {
                        sv.desfazerTransacao();
                        return;
                    }

                    HistoricoCarteirinha hc = new HistoricoCarteirinha();

                    hc.setCarteirinha(carteirinha);
                    hc.setDescricao("Primeira ReImpressão de Carteirinha 2º Via");

                    if (listaSelecionado.get(i).get(17) != null) {
                        Movimento m = (Movimento) sv.pesquisaCodigo(Integer.valueOf(listaSelecionado.get(i).get(17).toString()), "Movimento");
                        if (m != null) {
                            hc.setMovimento(m);
                        }
                    }

                    if (!sv.inserirObjeto(hc)) {
                        sv.desfazerTransacao();
                        return;
                    }
                } else {
                    HistoricoCarteirinha hc = new HistoricoCarteirinha();

                    carteirinha.setVia(carteirinha.getVia() + 1);

                    if (!sv.alterarObjeto(carteirinha)) {
                        sv.desfazerTransacao();
                        return;
                    }

                    hc.setCarteirinha(carteirinha);
                    hc.setDescricao("ReImpressão de Carteirinha 2º Via");

                    if (listaSelecionado.get(i).get(17) != null) {
                        Movimento m = (Movimento) sv.pesquisaCodigo(Integer.valueOf(listaSelecionado.get(i).get(17).toString()), "Movimento");
                        if (m != null) {
                            hc.setMovimento(m);
                        }
                    }

                    if (!sv.inserirObjeto(hc)) {
                        sv.desfazerTransacao();
                        return;
                    }
                }
            }

            if (ImpressaoParaSocios.imprimirCarteirinha(listaSelecionado)) {
                sv.comitarTransacao();
            } else {
                sv.desfazerTransacao();
            }
        }
    }

    public String imprimirEtiqueta() {
        SocioCarteirinhaDB dbs = new SocioCarteirinhaDBToplink();

        List<Etiquetas> listax = new ArrayList();
        for (int i = 0; i < listaSelecionado.size(); i++) {
            List l = (List) dbs.listaPesquisaEtiqueta((Integer) ((List) listaSelecionado.get(i)).get(0)).get(0);
            listax.add(new Etiquetas(String.valueOf(l.get(0)),
                    String.valueOf(l.get(1)),
                    String.valueOf(l.get(2)),
                    String.valueOf(l.get(3)),
                    String.valueOf(l.get(4)),
                    String.valueOf(l.get(5)),
                    String.valueOf(l.get(6)),
                    String.valueOf(l.get(7)),
                    String.valueOf(l.get(8))));
        }

        if (listax.isEmpty()) {
            return null;
        }

        Jasper.PART_NAME = "";
        Jasper.PATH = "etiquetas";
        Jasper.printReports("/Relatorios/ETIQUETA_SOCIO.jasper", "etiqueta_coluna", listax);
        return null;
    }

    public String imprimirEtiquetaTermica() {
        SocioCarteirinhaDB dbs = new SocioCarteirinhaDBToplink();

        List<Etiquetas> listax = new ArrayList();
        for (int i = 0; i < listaSelecionado.size(); i++) {
            List l = (List) dbs.listaPesquisaEtiqueta((Integer) ((List) listaSelecionado.get(i)).get(0)).get(0);
            listax.add(new Etiquetas(String.valueOf(l.get(0)),
                    String.valueOf(l.get(1)),
                    String.valueOf(l.get(2)),
                    String.valueOf(l.get(3)),
                    String.valueOf(l.get(4)),
                    String.valueOf(l.get(5)),
                    String.valueOf(l.get(6)),
                    String.valueOf(l.get(7)),
                    String.valueOf(l.get(8))));
        }

        if (listax.isEmpty()) {
            return null;
        }

        Jasper.PART_NAME = "";
        Jasper.PATH = "etiquetas";
        Jasper.printReports("/Relatorios/ETIQUETA_TERMICA_SOCIAL_RETRATO.jasper", "etiqueta_termica", listax);
        return null;
    }

    /**
     * @return
     */
    public String getIndexOrdem() {
        return indexOrdem;
    }

    public void setIndexOrdem(String indexOrdem) {
        this.indexOrdem = indexOrdem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        if (por.equals("iMatricula")) {
            try {
                Integer.parseInt(descricao);
            } catch (Exception e) {
                descricao = "";
            }
        }
        this.descricao = descricao;
    }

    public List<Vector> getListaCarteirinha() {
        return listaCarteirinha;
    }

    public void setListaCarteirinha(List<Vector> listaCarteirinha) {
        this.listaCarteirinha = listaCarteirinha;
    }

    public String getPor() {
        return por;
    }

    public void setPor(String por) {
        this.por = por;
    }

    public String getPorLabel() {
        return porLabel;
    }

    public void setPorLabel(String porLabel) {
        this.porLabel = porLabel;
    }

    public List<Vector> getListaSelecionado() {
        return listaSelecionado;
    }

    public void setListaSelecionado(List<Vector> listaSelecionado) {
        if (toggle != null || toggle) {
//            this.listaSelecionado = listaSelecionado;
//            toggle = true;
        }
        for (int i = 0; i < this.listaSelecionado.size(); i++) {
            for (int j = 0; j < listaSelecionadoMemoria.size(); j++) {
                if (((Integer) ((List) listaSelecionadoMemoria.get(i)).get(0)) != ((Integer) ((List) this.listaSelecionado.get(i)).get(0))) {

                }
            }
        }
    }

    public List getListaHistorico() {
        return listaHistorico;
    }

    public void setListaHistorico(List listaHistorico) {
        this.listaHistorico = listaHistorico;
    }

    public List<Vector> getFilteredCarteirinha() {
        return filteredCarteirinha;
    }

    public void setFilteredCarteirinha(List<Vector> filteredCarteirinha) {
        this.filteredCarteirinha = filteredCarteirinha;
    }

    public void selectedPage(PageEvent event) {
        page = event.getPage();
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void onRowSelect(SelectEvent event) {
        listaSelecionado.add(((Vector) event.getObject()));
    }

    public void onRowUnselect(UnselectEvent event) {
        for (int i = 0; i < listaSelecionado.size(); i++) {
            if (((Vector) listaSelecionado.get(i)).get(0) == ((Vector) event.getObject()).get(0)) {
                listaSelecionado.remove(i);
                break;
            }
        }
    }

    public String clear() {
        listaSelecionado.clear();
        page = null;
        return "cartaoSocial";
    }

    public void listernetFilter(FilterEvent filterEvent) {
        toggle = true;
    }

    public void toggleSelectedListener() {
        Integer pageNumber = listaCarteirinha.size() / 10;
        Integer indexMin = 0;
        Integer indexMax = 0;
        Integer pg = 0;
        if (page != null) {
            pg = page + 1;
        } else {
            pg = 1;
        }
        if (pageNumber == 1) {
            indexMax = listaCarteirinha.size() - 1;
            indexMin = 0;
        } else if (pageNumber > 1) {
            if (page == null || page == 0) {
                indexMin = 0;
                indexMax = 9;
            } else {
                indexMin = page;
                indexMax = page + 10;
            }
        }
        int x = 0;
        listaSelecionado.clear();
        for (int i = indexMin; i < listaCarteirinha.size(); i++) {
            if (x == 10) {
                break;
            }
            listaSelecionado.add(listaCarteirinha.get(i));
            x++;
        }
    }

    public String selectedAll() {
        if (!listaSelecionado.isEmpty()) {
            if (listaSelecionado.size() == listaCarteirinha.size()) {
                listaSelecionado.clear();
            } else {
                listaSelecionado.addAll(listaCarteirinha);
            }
        } else {
            listaSelecionado.addAll(listaCarteirinha);
        }
        return "cartaoSocial";
    }

    public Boolean getToggle() {
        return toggle;
    }

    public void setToggle(Boolean toggle) {
        this.toggle = toggle;
    }

    public Integer getFirstIndex() {
        return firstIndex;
    }

    public void setFirstIndex(Integer firstIndex) {
        if (this.firstIndex > listaCarteirinha.size()) {
            this.firstIndex = 0;
        }
        this.firstIndex = firstIndex;
    }

    public Integer getLastIndex() {
        return lastIndex;
    }

    public void setLastIndex(Integer lastIndex) {
        if (firstIndex > 0) {
            if (this.lastIndex == 0) {
                this.lastIndex = this.firstIndex;
            } else {
                this.lastIndex = lastIndex;
            }
        } else {
            if (this.lastIndex < this.firstIndex) {
                this.lastIndex = this.firstIndex;
            }
            if (this.firstIndex > listaCarteirinha.size()) {
                this.lastIndex = listaCarteirinha.size();
            }
            if (this.lastIndex == 0) {
                this.lastIndex = this.firstIndex;
            }
        }
    }

    public String loadSelecteds() {
        if (firstIndex == 0 && lastIndex == 0) {
            return null;
        }
        listaSelecionado.clear();
        for (int i = firstIndex - 1; i < lastIndex; i++) {
            listaSelecionado.add(listaCarteirinha.get(i));
        }
        return "cartaoSocial";
    }

    public List<SelectItem> getListFilial() {
        if (listFilial.isEmpty()) {
            MacFilial mf = MacFilial.getAcessoFilial();
            idFilial = 0;
            List<Filial> list = new Dao().list(new Filial(), true);
            int j = 0;
            listFilial.add(new SelectItem(j, "TODAS", null));
            j = 1;
            for (int i = 0; i < list.size(); i++) {
                if (disabled) {
                    if (list.get(i).getId() == mf.getFilial().getId()) {
                        idFilial = j;
                    }
                }
                listFilial.add(new SelectItem(j, list.get(i).getFilial().getPessoa().getNome(), "" + list.get(i).getId()));
                j++;
            }
        }
        return listFilial;
    }

    public void setListFilial(List<SelectItem> listFilial) {
        this.listFilial = listFilial;
    }

    public Integer getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(Integer idFilial) {
        this.idFilial = idFilial;
    }

    /**
     * Médtodo genérico para geração de históricos de carteirinhas
     *
     * @param list (Movimentos gerados)
     * @param idModelo
     * @return
     */
    public static boolean isGerarHistoricoCarteirinhas(List<Movimento> list, Integer idModelo) {
        return !CartaoSocialBean.gerarHistoricoCarteirinhas(list, idModelo).isEmpty();
    }

    /**
     * Médtodo genérico para geração de históricos de carteirinhas
     *
     * @param list (Movimentos gerados)
     * @param idCategoria
     * @param idRotina
     * @return
     */
    public static boolean isGerarHistoricoCarteirinhas(List<Movimento> list, Integer idCategoria, Integer idRotina) {
        return !gerarHistoricoCarteirinhas(list, idCategoria, idRotina).isEmpty();
    }

    /**
     * Médtodo genérico para geração de históricos de carteirinhas se retornar
     * uma lista é que existem carteirinhas geradas.
     *
     * @param list
     * @param idCategoria
     * @param idRotina
     * @return
     */
    public static List<HistoricoCarteirinha> gerarHistoricoCarteirinhas(List<Movimento> list, Integer idCategoria, Integer idRotina) {
        SocioCarteirinhaDB socioCarteirinhaDB = new SocioCarteirinhaDBToplink();
        ModeloCarteirinha modeloCarteirinha = socioCarteirinhaDB.pesquisaModeloCarteirinha(idCategoria, idRotina);
        if (modeloCarteirinha != null) {
            return gerarHistoricoCarteirinhas(list, modeloCarteirinha.getId());
        }
        return new ArrayList();
    }

    /**
     *
     * @param list
     * @param idModelo
     * @return
     */
    public static List<HistoricoCarteirinha> gerarHistoricoCarteirinhas(List<Movimento> list, Integer idModelo) {
        HistoricoCarteirinha historicoCarteirinha;
        SocioCarteirinhaDB socioCarteirinhaDB = new SocioCarteirinhaDBToplink();
        Dao dao = new Dao();
        dao.openTransaction();
        List<HistoricoCarteirinha> carteirinhas = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getMatriculaSocios() != null) {
                historicoCarteirinha = new HistoricoCarteirinha();
                historicoCarteirinha.setHora(DataHoje.hora());
                historicoCarteirinha.setDescricao("Impressão de Carteirinha");
                historicoCarteirinha.setEmissao(DataHoje.data());
                historicoCarteirinha.setMovimento(list.get(i));
                historicoCarteirinha.setCarteirinha(socioCarteirinhaDB.pesquisaCarteirinhaPessoa(list.get(i).getBeneficiario().getId(), idModelo));
                if (!dao.save(historicoCarteirinha)) {
                    dao.rollback();
                    return new ArrayList();
                }
                carteirinhas.add(historicoCarteirinha);
            }
        }
        if (carteirinhas.isEmpty()) {
            dao.rollback();
            return new ArrayList();
        }
        dao.commit();
        return carteirinhas;
    }

    public void imprimirSocioCarteirinha(List list) {
        if (list.isEmpty()) {
            return;
        }
        Boolean isBeneficiario = false;
        List<SocioCarteirinha> carteirinhas = new ArrayList<>();
        String type = list.get(0).getClass().getSimpleName();
        if (type.equals("HistoricoCarteirinha")) {
            List<HistoricoCarteirinha> historicoCarteirinhas = list;
            for (int i = 0; i < historicoCarteirinhas.size(); i++) {
                carteirinhas.add(historicoCarteirinhas.get(i).getCarteirinha());
                isBeneficiario = true;
            }
        } else {
            carteirinhas = (List<SocioCarteirinha>) list;
        }

        SocioCarteirinhaDB dbc = new SocioCarteirinhaDBToplink();
        List listAux = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            carteirinhas.get(i).setEmissao(DataHoje.data());
            new Dao().update(carteirinhas.get(i), true);
            if (isBeneficiario) {
                listAux.addAll(dbc.filtroCartao(((HistoricoCarteirinha) list.get(i)).getMovimento().getBeneficiario().getId()));
            } else {
                listAux.addAll(dbc.filtroCartao(carteirinhas.get(i).getPessoa().getId()));
            }
        }
        ImpressaoParaSocios.imprimirCarteirinha(listAux);
        GenericaSessao.put("cartao_social_sucesso", true);
    }

    public List<Vector> getListaSelecionadoMemoria() {
        return listaSelecionadoMemoria;
    }

    public void setListaSelecionadoMemoria(List<Vector> listaSelecionadoMemoria) {
        this.listaSelecionadoMemoria = listaSelecionadoMemoria;
    }

    public Integer getFilialInteger() {
        if (!listFilial.isEmpty()) {
            try {
                return Integer.parseInt(listFilial.get(idFilial).getDescription());
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }
}
