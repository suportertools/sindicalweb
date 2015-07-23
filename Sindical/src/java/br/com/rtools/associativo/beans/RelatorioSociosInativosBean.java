package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.Categoria;
import br.com.rtools.associativo.GrupoCategoria;
import br.com.rtools.associativo.Socios;
import br.com.rtools.associativo.db.CategoriaDB;
import br.com.rtools.associativo.db.CategoriaDBToplink;
import br.com.rtools.impressao.ParametroSociosInativos;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.relatorios.db.RelatorioGenericoDB;
import br.com.rtools.relatorios.db.RelatorioGenericoDBToplink;
import br.com.rtools.relatorios.db.RelatorioSociosDB;
import br.com.rtools.relatorios.db.RelatorioSociosDBToplink;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.AnaliseString;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Jasper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;

@ManagedBean
@SessionScoped
public class RelatorioSociosInativosBean implements Serializable {

    private List<Socios> listaSocios;
    private List<SelectItem> listaTipoRelatorio;
    private int indexRelatorio;
    private boolean comDependentes;
    private boolean chkDataInativacao;
    private boolean chkDataFiliacao;
    private boolean chkCategoria;
    private boolean chkGrupoCategoria;
    private String dataInativacaoInicial;
    private String dataInativacaoFinal;
    private String dataFiliacaoInicial;
    private String dataFiliacaoFinal;
    private List<SelectItem> listaCategoria;
    private int indexCategoria;
    private List<SelectItem> listaGrupoCategoria;
    private int indexGrupoCategoria;
    private String ordernarPor = "nome";
    private Relatorios relatorios;

    public RelatorioSociosInativosBean() {
        listaSocios = new ArrayList();
        listaTipoRelatorio = new ArrayList();
        listaCategoria = new ArrayList();
        listaGrupoCategoria = new ArrayList();
        this.loadListaRelatorio();
        indexRelatorio = 0;
        comDependentes = false;
        chkDataInativacao = false;
        chkDataFiliacao = false;
        chkCategoria = false;
        chkGrupoCategoria = false;
        dataInativacaoInicial = DataHoje.data();
        dataInativacaoFinal = DataHoje.data();
        dataFiliacaoInicial = DataHoje.data();
        dataFiliacaoFinal = DataHoje.data();
        this.loadListaCategoria();
        this.loadListaGrupoCategoria();
        indexCategoria = 0;
        indexGrupoCategoria = 0;
        relatorios = new Relatorios();
    }

    public void imprimir() {
        RelatorioSociosDB db = new RelatorioSociosDBToplink();

        int id_categoria = -1, id_grupo_categoria = -1;
        if (chkCategoria) {
            id_categoria = Integer.valueOf(listaCategoria.get(indexCategoria).getDescription());
        }

        if (chkGrupoCategoria) {
            id_grupo_categoria = Integer.valueOf(listaGrupoCategoria.get(indexGrupoCategoria).getDescription());
        }

        List<Vector> result = db.listaSociosInativos(comDependentes, chkDataInativacao, chkDataFiliacao, dataInativacaoInicial, dataInativacaoFinal, dataFiliacaoInicial, dataFiliacaoFinal, id_categoria, id_grupo_categoria, ordernarPor);

        Dao di = new Dao();

        Juridica sindicato = (Juridica) di.find(new Juridica(), 1);
        PessoaEndereco endSindicato = (new PessoaEnderecoDBToplink()).pesquisaEndPorPessoaTipo(sindicato.getId(), 3);

        List<ParametroSociosInativos> lista = new ArrayList();
        for (int i = 0; i < result.size(); i++) {
            lista.add(new ParametroSociosInativos(
                    ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"), // sinLogo
                    sindicato.getPessoa().getSite(), // sinSite
                    sindicato.getPessoa().getNome(), // sinNome
                    endSindicato.getEndereco().getDescricaoEndereco().getDescricao(),
                    endSindicato.getEndereco().getLogradouro().getDescricao(),
                    endSindicato.getNumero(),
                    endSindicato.getComplemento(),
                    endSindicato.getEndereco().getBairro().getDescricao(),
                    endSindicato.getEndereco().getCep(),
                    endSindicato.getEndereco().getCidade().getCidade(),
                    endSindicato.getEndereco().getCidade().getUf(),
                    sindicato.getPessoa().getDocumento(), // sinDocumento
                    result.get(i).get(0).toString(), // nomeTitular
                    result.get(i).get(1).toString(), // codTitular
                    result.get(i).get(2).toString(), // codSocio
                    result.get(i).get(3).toString() + " (" + result.get(i).get(4).toString() + ") ", // nome
                    result.get(i).get(4).toString(), // parentesco
                    result.get(i).get(5).toString(), // matricula
                    result.get(i).get(6).toString(), // categoria
                    DataHoje.converteData((Date) result.get(i).get(7)), // filiacao -- data
                    DataHoje.converteData((Date) result.get(i).get(8)), // inativacao -- data
                    result.get(i).get(9).toString()) // motivo_inativacao
            );
        }

        if (!lista.isEmpty()) {
            try {
                Relatorios relatorios = (Relatorios) di.find(new Relatorios(), Integer.valueOf(listaTipoRelatorio.get(indexRelatorio).getDescription()));
                String jasperName = relatorios.getNome();
                String jasperFile = relatorios.getJasper();
                if (comDependentes) {
                    jasperName = "relatorio sócios inativos dependente";
                    jasperFile = "/Relatorios/SOCIOINATIVODEPENDENTE.jasper";
                }

                Jasper.PART_NAME = AnaliseString.removerAcentos(jasperName.toLowerCase());
                Jasper.PATH = "downloads";
                if (relatorios.getPorFolha()) {
                    Jasper.GROUP_NAME = relatorios.getNomeGrupo();
                }
                Jasper.printReports(jasperFile, "relatorios", (Collection) lista);
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    public void porDataInativacao() {
        chkDataInativacao = (chkDataInativacao == true) ? false : true;
        dataInativacaoInicial = DataHoje.data();
        dataInativacaoFinal = DataHoje.data();
    }

    public void porDataFiliacao() {
        chkDataFiliacao = (chkDataFiliacao == true) ? false : true;
        dataFiliacaoInicial = DataHoje.data();
        dataFiliacaoFinal = DataHoje.data();
    }

    public void porCategoria() {
        chkCategoria = (chkCategoria == true) ? false : true;
    }

    public void porGrupoCategoria() {
        chkGrupoCategoria = (chkGrupoCategoria == true) ? false : true;
    }

    public final void loadListaRelatorio() {
        listaTipoRelatorio.clear();
        RelatorioGenericoDB db = new RelatorioGenericoDBToplink();
        List<Relatorios> select = db.pesquisaTipoRelatorio(270);
        for (int i = 0; i < select.size(); i++) {
            listaTipoRelatorio.add(new SelectItem(i,
                    ((Relatorios) select.get(i)).getNome(),
                    Integer.toString(((Relatorios) select.get(i)).getId()))
            );
        }
    }

    public final void loadListaCategoria() {
        listaCategoria.clear();
        Dao di = new Dao();
        CategoriaDB db = new CategoriaDBToplink();
        //List<Categoria> select = di.list(new Categoria());
        List<Categoria> select = db.pesquisaTodos();
        for (int i = 0; i < select.size(); i++) {
            listaCategoria.add(new SelectItem(i,
                    select.get(i).getCategoria(),
                    Integer.toString(select.get(i).getId()))
            );
        }
    }

    public final void loadListaGrupoCategoria() {
        listaGrupoCategoria.clear();
        Dao di = new Dao();
        //List<Categoria> select = di.list(new Categoria());
        List<GrupoCategoria> select = di.list(new GrupoCategoria());
        for (int i = 0; i < select.size(); i++) {
            listaGrupoCategoria.add(new SelectItem(i,
                    select.get(i).getGrupoCategoria(),
                    Integer.toString(select.get(i).getId()))
            );
        }
    }

    public List<SelectItem> getListaTipoRelatorio() {
        return listaTipoRelatorio;
    }

    public void setListaTipoRelatorio(List<SelectItem> listaTipoRelatorio) {
        this.listaTipoRelatorio = listaTipoRelatorio;
    }

    public int getIndexRelatorio() {
        return indexRelatorio;
    }

    public void setIndexRelatorio(int indexRelatorio) {
        this.indexRelatorio = indexRelatorio;
    }

    public boolean isComDependentes() {
        return comDependentes;
    }

    public void setComDependentes(boolean comDependentes) {
        this.comDependentes = comDependentes;
    }

    public List<Socios> getListaSocios() {
        return listaSocios;
    }

    public void setListaSocios(List<Socios> listaSocios) {
        this.listaSocios = listaSocios;
    }

    public boolean isChkDataInativacao() {
        return chkDataInativacao;
    }

    public void setChkDataInativacao(boolean chkDataInativacao) {
        this.chkDataInativacao = chkDataInativacao;
    }

    public boolean isChkDataFiliacao() {
        return chkDataFiliacao;
    }

    public void setChkDataFiliacao(boolean chkDataFiliacao) {
        this.chkDataFiliacao = chkDataFiliacao;
    }

    public boolean isChkCategoria() {
        return chkCategoria;
    }

    public void setChkCategoria(boolean chkCategoria) {
        this.chkCategoria = chkCategoria;
    }

    public boolean isChkGrupoCategoria() {
        return chkGrupoCategoria;
    }

    public void setChkGrupoCategoria(boolean chkGrupoCategoria) {
        this.chkGrupoCategoria = chkGrupoCategoria;
    }

    public String getDataInativacaoInicial() {
        return dataInativacaoInicial;
    }

    public void setDataInativacaoInicial(String dataInativacaoInicial) {
        this.dataInativacaoInicial = dataInativacaoInicial;
    }

    public String getDataInativacaoFinal() {
        return dataInativacaoFinal;
    }

    public void setDataInativacaoFinal(String dataInativacaoFinal) {
        this.dataInativacaoFinal = dataInativacaoFinal;
    }

    public String getDataFiliacaoInicial() {
        return dataFiliacaoInicial;
    }

    public void setDataFiliacaoInicial(String dataFiliacaoInicial) {
        this.dataFiliacaoInicial = dataFiliacaoInicial;
    }

    public String getDataFiliacaoFinal() {
        return dataFiliacaoFinal;
    }

    public void setDataFiliacaoFinal(String dataFiliacaoFinal) {
        this.dataFiliacaoFinal = dataFiliacaoFinal;
    }

    public List<SelectItem> getListaCategoria() {
        return listaCategoria;
    }

    public void setListaCategoria(List<SelectItem> listaCategoria) {
        this.listaCategoria = listaCategoria;
    }

    public int getIndexCategoria() {
        return indexCategoria;
    }

    public void setIndexCategoria(int indexCategoria) {
        this.indexCategoria = indexCategoria;
    }

    public List<SelectItem> getListaGrupoCategoria() {
        return listaGrupoCategoria;
    }

    public void setListaGrupoCategoria(List<SelectItem> listaGrupoCategoria) {
        this.listaGrupoCategoria = listaGrupoCategoria;
    }

    public int getIndexGrupoCategoria() {
        return indexGrupoCategoria;
    }

    public void setIndexGrupoCategoria(int indexGrupoCategoria) {
        this.indexGrupoCategoria = indexGrupoCategoria;
    }

    public String getOrdernarPor() {
        return ordernarPor;
    }

    public void setOrdernarPor(String ordernarPor) {
        this.ordernarPor = ordernarPor;
    }

    public Relatorios getRelatorios() {
        if (relatorios.getId() == -1) {
            relatorios = (Relatorios) new Dao().find(new Relatorios(), Integer.parseInt(listaTipoRelatorio.get(indexRelatorio).getDescription()));
        }
        return relatorios;
    }

    /**
     * tcase
     * <ul>
     * <li>1 - Relatório == null </li>
     * </ul>
     *
     * @param tcase
     */
    public void listener(Integer tcase) {
        relatorios = new Relatorios();
    }
}
