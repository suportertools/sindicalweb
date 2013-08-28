//package br.com.rtools.associativo.beans;
//
//import br.com.rtools.arrecadacao.GrupoCidades;
//import br.com.rtools.arrecadacao.db.GrupoCidadesDB;
//import br.com.rtools.arrecadacao.db.GrupoCidadesDBToplink;
//import br.com.rtools.associativo.Categoria;
//import br.com.rtools.associativo.GrupoCategoria;
//import br.com.rtools.associativo.MatriculaSocios;
//import br.com.rtools.associativo.Parentesco;
//import br.com.rtools.associativo.SMotivoInativacao;
//import br.com.rtools.associativo.ServicoCategoria;
//import br.com.rtools.associativo.SocioCarteirinha;
//import br.com.rtools.associativo.Socios;
//import br.com.rtools.associativo.db.CategoriaDB;
//import br.com.rtools.associativo.db.CategoriaDBToplink;
//import br.com.rtools.associativo.db.GrupoCategoriaDB;
//import br.com.rtools.associativo.db.GrupoCategoriaDBToplink;
//import br.com.rtools.associativo.db.MatriculaSociosDB;
//import br.com.rtools.associativo.db.MatriculaSociosDBToplink;
//import br.com.rtools.associativo.db.ParentescoDB;
//import br.com.rtools.associativo.db.ParentescoDBToplink;
//import br.com.rtools.associativo.db.ServicoCategoriaDB;
//import br.com.rtools.associativo.db.ServicoCategoriaDBToplink;
//import br.com.rtools.associativo.db.SocioCarteirinhaDB;
//import br.com.rtools.associativo.db.SocioCarteirinhaDBToplink;
//import br.com.rtools.associativo.db.SociosDB;
//import br.com.rtools.associativo.db.SociosDBToplink;
//import br.com.rtools.endereco.Cidade;
//import br.com.rtools.financeiro.ServicoPessoa;
//import br.com.rtools.financeiro.db.ServicoPessoaDB;
//import br.com.rtools.financeiro.db.ServicoPessoaDBToplink;
//import br.com.rtools.impressao.FichaSocial;
//import br.com.rtools.pessoa.Fisica;
//import br.com.rtools.pessoa.Juridica;
//import br.com.rtools.pessoa.Pessoa;
//import br.com.rtools.pessoa.PessoaEmpresa;
//import br.com.rtools.pessoa.PessoaEndereco;
//import br.com.rtools.pessoa.beans.FisicaJSFBean;
//import br.com.rtools.pessoa.db.FilialDB;
//import br.com.rtools.pessoa.db.FilialDBToplink;
//import br.com.rtools.pessoa.db.FisicaDB;
//import br.com.rtools.pessoa.db.FisicaDBToplink;
//import br.com.rtools.pessoa.db.JuridicaDB;
//import br.com.rtools.pessoa.db.JuridicaDBToplink;
//import br.com.rtools.pessoa.db.PessoaEmpresaDB;
//import br.com.rtools.pessoa.db.PessoaEmpresaDBToplink;
//import br.com.rtools.pessoa.db.PessoaEnderecoDB;
//import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
//import br.com.rtools.pessoa.db.TipoDocumentoDB;
//import br.com.rtools.pessoa.db.TipoDocumentoDBToplink;
//import br.com.rtools.utilitarios.AnaliseString;
//import br.com.rtools.utilitarios.DataHoje;
//import br.com.rtools.utilitarios.DataObject;
//import br.com.rtools.utilitarios.Download;
//import br.com.rtools.utilitarios.SalvarAcumuladoDB;
//import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
//import br.com.rtools.utilitarios.ValidaDocumentos;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.Vector;
//import javax.faces.context.FacesContext;
//import javax.faces.model.SelectItem;
//import javax.servlet.ServletContext;
//import javax.servlet.ServletOutputStream;
//import javax.servlet.http.HttpServletResponse;
//import net.sf.jasperreports.engine.JasperExportManager;
//import net.sf.jasperreports.engine.JasperFillManager;
//import net.sf.jasperreports.engine.JasperPrint;
//import net.sf.jasperreports.engine.JasperReport;
//import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
//import net.sf.jasperreports.engine.util.JRLoader;
//
//public class SociosJSFBeanAntigo{
//    private Socios socios;
//    private PessoaEmpresa pessoaEmpresa;
//    private MatriculaSocios matriculaSocios;
//    private Fisica dependente;
//    private int idGrupoCategoria;
//    private int idCategoria;
//    private int idInativacao;
//    private int indiceDependente;
//    private String tipoDestinario;
//    private String msgConfirma;
//    private String comoPesquisa;
//    private String porPesquisa;
//    private String statusPesquisa;
//    private String strEnderecoEmpresa;
//    private String msgConfirmaTela;
//    private String descPesquisa;
//    private String statusSocio;
//    private String statusCor;
//    private String strInativacao;
//    private String dataInativacao;
//    private String dataReativacao;
//    private String statusModal;
//    private boolean imprimirVerso;
//    private boolean desabilitaImpressao;
//    private boolean desabilitaCategoria;
//    private boolean temFoto;
//    private boolean fotoTemp;
//    private List<SelectItem> listaMotivoInativacao;
//    private List<SelectItem> listaParentesco;
//    private List listaDepsBanco;
//    private ServicoPessoaJSFBean servicoPessoa;
//    private List listaDependentes;
//    private FisicaJSFBean fisicaJSFBean;
//    private List itens;
//
//    public SociosJSFBeanAntigo(){
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("servicoPessoaBean",new ServicoPessoaJSFBean());
//        servicoPessoa = ((ServicoPessoaJSFBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("servicoPessoaBean"));
//        servicoPessoa.renderServicos = false;
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fisicaBean",new FisicaJSFBean());
//        fisicaJSFBean = ((FisicaJSFBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaBean"));
//        socios = new Socios();
//        pessoaEmpresa = new PessoaEmpresa();
//        matriculaSocios = new MatriculaSocios();
//        idGrupoCategoria = 0;
//        idCategoria = 0;
//        imprimirVerso = false;
//        comoPesquisa = "";
//        porPesquisa = "";
//        msgConfirma = "";
//        strEnderecoEmpresa = "";
////        htmlTable = new HtmlDataTable();
//        descPesquisa = "";
//        msgConfirmaTela = "";
////        htmlTablePesquisa = new HtmlDataTable();
//        statusPesquisa = "";
//        desabilitaImpressao = true;
//        idInativacao = 0;
//        statusSocio = "ATIVO";
//        statusCor = "blue";
//        strInativacao = "";
//        dataInativacao = DataHoje.data();
//        dataReativacao = DataHoje.data();
//        statusModal = "panelInativar";
//        desabilitaCategoria = false;
//        //listaGrupoCategoria = new Vector<SelectItem>();
//        listaMotivoInativacao = new Vector<SelectItem>();
//        //listaCategoria = new Vector<SelectItem>();
//        listaParentesco = new Vector<SelectItem>();
//        listaDependentes = new ArrayList();
//        dependente = new Fisica();
//        temFoto = false;
//        fotoTemp = false;
//        itens = new ArrayList();
//        listaDepsBanco = new ArrayList();    
//    }
//
//    public List<SelectItem> getListaGrupoCategoria(){
//        List<SelectItem> listaGrupoCategoria = new Vector<SelectItem>();;
//        int i = 0;
//        GrupoCategoriaDB db = new GrupoCategoriaDBToplink();
//        List select = db.pesquisaTodos();
//        while (i < select.size()){
//            listaGrupoCategoria.add(new SelectItem( new Integer(i),
//                        (String) ((GrupoCategoria) select.get(i)).getGrupoCategoria(),
//                        Integer.toString(((GrupoCategoria) select.get(i)).getId()) ));
//            i++;
//        }
//        return listaGrupoCategoria;
//    }
//
//    public List<SelectItem> getListaMotivoInativacao(){
//        if (listaMotivoInativacao.isEmpty()){
//            SociosDB db = new SociosDBToplink();
//            List select = db.pesquisaMotivoInativacao();
//            for (int i = 0; i < select.size(); i++){
//                listaMotivoInativacao.add(new SelectItem( new Integer(i),
//                            (String) ((SMotivoInativacao) select.get(i)).getNome(),
//                            Integer.toString(((SMotivoInativacao) select.get(i)).getId()) ));
//            }
//        }
//        return listaMotivoInativacao;
//    }
//
//    public List<SelectItem> getListaCategoria(){
//        //listaCategoria.clear();
//        List<SelectItem> listaCategoria = new Vector<SelectItem>();
//        int i = 0;
//        if (!getListaGrupoCategoria().isEmpty()){
//            CategoriaDB db = new CategoriaDBToplink();
//            List select = db.pesquisaCategoriaPorGrupo( Integer.parseInt( getListaGrupoCategoria().get(idGrupoCategoria).getDescription() ));
//            while (i < select.size()){
//                listaCategoria.add(new SelectItem( new Integer(i),
//                            (String) ((Categoria) select.get(i)).getCategoria(),
//                            Integer.toString(((Categoria) select.get(i)).getId()) ));
//                i++;
//            }
//        }
//        return listaCategoria;
//    }
//
//    public List<SelectItem> getListaParentesco(){
//        if (listaParentesco.isEmpty()){
//            int i = 0;
//            ParentescoDB db = new ParentescoDBToplink();
//            List select = db.pesquisaTodosSemTitular();
//            while (i < select.size()){
//                listaParentesco.add(new SelectItem( new Integer(i),
//                            (String) ((Parentesco) select.get(i)).getParentesco(),
//                            Integer.toString(((Parentesco) select.get(i)).getId()) ));
//                i++;
//            }
//        }
//        return listaParentesco;
//    }
//
//    public String novo(){
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sociosBean",new SociosJSFBean());
//        return "socios";
//    }
//
//    public void novoGenerico(){
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("servicoPessoaBean",new ServicoPessoaJSFBean());
//        servicoPessoa = ((ServicoPessoaJSFBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("servicoPessoaBean"));
//        servicoPessoa.renderServicos = false;
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fisicaBean",new FisicaJSFBean());
//        fisicaJSFBean = ((FisicaJSFBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaBean"));
//        socios = new Socios();
//        pessoaEmpresa = new PessoaEmpresa();
//        matriculaSocios = new MatriculaSocios();
//        idGrupoCategoria = 0;
//        idCategoria = 0;
//        imprimirVerso = false;
//        comoPesquisa = "";
//        porPesquisa = "";
//        strEnderecoEmpresa = "";
////        htmlTable = new HtmlDataTable();
//        descPesquisa = "";
////        htmlTablePesquisa = new HtmlDataTable();
//        statusPesquisa = "";
//        desabilitaImpressao = true;
//        idInativacao = 0;
//        statusSocio = "ATIVO";
//        statusCor = "blue";
//        strInativacao = "";
//        dataInativacao = DataHoje.data();
//        dataReativacao = DataHoje.data();
//        statusModal = "panelInativar";
//        desabilitaCategoria = false;
//        //listaGrupoCategoria = new Vector<SelectItem>();
//        listaMotivoInativacao = new Vector<SelectItem>();
//        //listaCategoria = new Vector<SelectItem>();
//        listaParentesco = new Vector<SelectItem>();
//        listaDependentes = new ArrayList();
//        dependente = new Fisica();
//        temFoto = false;
//        fotoTemp = false;
//        itens = new ArrayList();
//        listaDepsBanco = new ArrayList();
//        //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sociosBean",new SociosJSFBean());
//    }
//
//    public String adicionarDependente(){
//        Fisica fisica = new Fisica();
//        if (listaDependentes.isEmpty()){
//            fisica.getPessoa().setNome("NENHUM");
//            DataObject dtObj =  new DataObject(fisica, // NOME
//                                               0,      // IDPARENTESCO
//                                               1,      // VIA CARTEIRINHA
//                                               (new DataHoje()).incrementarAnos(5, DataHoje.data()),   // DATA VALIDADE CARTEIRINHA
//                                               null,      // DATA VAL DEP
//                                               null);
//            listaDependentes.add(dtObj);
//            //servicoPessoa.adicionaTitular = false;
//        }else{
//            for (int i = 0; i < listaDependentes.size(); i++){
//                if ( ((Fisica)((DataObject)listaDependentes.get(i)).getArgumento0()).getId() != -1
//                        && ( i - ( listaDependentes.size() - 1 ) == 0)){
//                    fisica.getPessoa().setNome("NENHUM");
//                    DataObject dtObj =  new DataObject(fisica, // NOME
//                                                       0,      // IDPARENTESCO
//                                                       1,      // VIA CARTEIRINHA
//                                                       (new DataHoje()).incrementarAnos(5, DataHoje.data()),   // DATA VALIDADE CARTEIRINHA
//                                                       null,      // DATA VAL DEP
//                                                       null);
//                    listaDependentes.add(dtObj);
//                    //servicoPessoa.adicionaTitular = false;
//                    msgConfirmaTela = "";
//                    break;
//                }
//            }
//        }
//        return null;
//    }
//
//    public List getListDependentes(){
//        if (servicoPessoa.servicoPessoa.getId() != -1){
//            Fisica fisica = new Fisica();
//            Parentesco par = new Parentesco();
//            SociosDB db = new SociosDBToplink();
//            FisicaDB dbFis = new FisicaDBToplink();
//            ParentescoDB dbPar = new ParentescoDBToplink();
//            List listaPar = dbPar.pesquisaTodosSemTitular();
//            int qntParentesco = listaPar.size();
//            int index = 0;
//            boolean atualiza = true;
//            if (servicoPessoa.servicoPessoa.getId() == -1){
//                if (!listaDependentes.isEmpty()){
//                    if ( FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa") != null){
//                        for (int i = 0; i < listaDependentes.size(); i++){
//                            if ( ((Fisica)((DataObject)listaDependentes.get(i)).getArgumento0()).getId() == -1 ){
//                                fisica = (Fisica)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa");
//                                if (fisica.getPessoa().getId() == servicoPessoa.getServicoPessoa().getPessoa().getId()){
//                                    msgConfirmaTela = "Dependente não pode ser Titular!";
//                                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
//                                    return listaDependentes;
//                                }
//                                if (db.pesquisaSocioPorPessoaAtivo( fisica.getPessoa().getId() ).getId() != -1){
//                                    msgConfirmaTela = "Este dependente já um sócio Cadastrado!";
//                                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
//                                    return listaDependentes;
//                                }
//
//                                for (int w = 0; w < listaDependentes.size(); w++){
//                                    if (((Fisica)((DataObject)listaDependentes.get(w)).getArgumento0()).getId() == fisica.getId()){
//                                        atualiza = false;
//                                        msgConfirmaTela = "Dependente já existe!";
//                                        break;
//                                    }
//                                }
//                                par = dbPar.pesquisaCodigo( Integer.parseInt(getListaParentesco().get( Integer.parseInt((String)((DataObject)listaDependentes.get(i)).getArgumento1()) ).getDescription() ));
//                                if (atualiza){
//                                    if (par.getNrValidade() == 0){
//                                        ((DataObject)listaDependentes.get(i)).setArgumento4(null);
//                                    }else if (par.getNrValidade() > 0 && par.isValidade()){
//                                        if (!fisica.getNascimento().equals("") && fisica.getNascimento() != null){
//                                            ((DataObject)listaDependentes.get(i)).setArgumento4(
//                                                       ((new DataHoje()).incrementarAnos(par.getNrValidade(),fisica.getNascimento())).substring(3, 10)
//                                            );
//                                        }else{
//                                            ((DataObject)listaDependentes.get(i)).setArgumento4(null);
//                                        }
//                                    }else if (par.getNrValidade() > 0 && !par.isValidade()){
//                                        ((DataObject)listaDependentes.get(i)).setArgumento4(
//                                                   ((new DataHoje()).incrementarAnos(par.getNrValidade(), DataHoje.data())).substring(3, 10)
//                                        );
//                                    }
//                                    ((DataObject)listaDependentes.get(i)).setArgumento0(fisica);
//                                    msgConfirmaTela = "Dependente adicionado!";
//                                    fisicaJSFBean.novoGenerico();
//                                    //servicoPessoa.adicionaTitular = true;
//                                }
//                                break;
//                            }
//                        }
//                    }
//                }
//            }else{
//                Fisica fis = new Fisica();
//                if (listaDepsBanco.isEmpty()){
//                    String validadeDep = "";
//                    listaDepsBanco = db.pesquisaDependentes(matriculaSocios.getId());
//                    if (!listaDepsBanco.isEmpty())
//                        listaDependentes.clear();
//                    for (int i = 0; i < listaDepsBanco.size(); i++){
//                        fis = dbFis.pesquisaFisicaPorPessoa(((Socios)listaDepsBanco.get(i)).getServicoPessoa().getPessoa().getId());
//
//                        for(int w = 0; w < qntParentesco; w++){
//                            if ( ((Socios)listaDepsBanco.get(i)).getParentesco().getId() == ((Parentesco)listaPar.get(w)).getId() ){
//                                index = w;
//                                break;
//                            }
//                        }
//
//                        if (((Socios)listaDepsBanco.get(i)).getParentesco().getNrValidade() == 0){
//                            validadeDep = "";
//                        }else if (((Socios)listaDepsBanco.get(i)).getParentesco().getNrValidade() > 0 && ((Socios)listaDepsBanco.get(i)).getParentesco().isValidade()){
//                            if (!fis.getNascimento().equals("") && fis.getNascimento() != null){
//                                validadeDep = ((new DataHoje()).incrementarAnos(((Socios)listaDepsBanco.get(i)).getParentesco().getNrValidade(),fis.getNascimento())).substring(3, 10);
//                            }else{
//                                validadeDep = "";
//                            }
//                        }else if (((Socios)listaDepsBanco.get(i)).getParentesco().getNrValidade() > 0 && !((Socios)listaDepsBanco.get(i)).getParentesco().isValidade()){
//                            validadeDep = ((new DataHoje()).incrementarAnos(((Socios)listaDepsBanco.get(i)).getParentesco().getNrValidade(), DataHoje.data())).substring(3, 10);
//                        }
//
//                        DataObject dtObj =  new DataObject( fis, // NOME
//                                                           index, // IDPARENTESCO
//                                                           ((Socios)listaDepsBanco.get(i)).getNrViaCarteirinha(),      // VIA CARTEIRINHA
//                                                           ((Socios)listaDepsBanco.get(i)).getValidadeCarteirinha(),   // DATA VALIDADE
//                                                           validadeDep,// DATA VAL DEP
//                                                           null);
//                        listaDependentes.add(dtObj);
//                        index = 0;
//                        fis = new Fisica();
//                    }
//                }
//                if (!listaDependentes.isEmpty()){
//                    if ( FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa") != null){
//                        for (int i = 0; i < listaDependentes.size(); i++){
//                            if ( ((Fisica)((DataObject)listaDependentes.get(i)).getArgumento0()).getId() == -1 ){
//                                fisica = (Fisica)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa");
//                                if (fisica.getPessoa().getId() == servicoPessoa.servicoPessoa.getPessoa().getId()){
//                                    msgConfirmaTela = "Dependente não pode ser Titular!";
//                                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
//                                    return listaDependentes;
//                                }
//                                if (db.pesquisaSocioPorPessoaAtivo( fisica.getPessoa().getId() ).getId() != -1){
//                                    msgConfirmaTela = "Este dependente já um sócio Cadastrado!";
//                                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
//                                    return listaDependentes;
//                                }
//                                for (int w = 0; w < listaDependentes.size(); w++){
//                                    if (((Fisica)((DataObject)listaDependentes.get(w)).getArgumento0()).getId()
//                                        == fisica.getId()){
//                                        atualiza = false;
//                                        msgConfirmaTela = "Dependente já existe!";
//                                        break;
//                                    }
//                                }
//                                par = dbPar.pesquisaCodigo( Integer.parseInt(getListaParentesco().get( Integer.parseInt((String)((DataObject)listaDependentes.get(i)).getArgumento1()) ).getDescription() ));
//                                if (atualiza){
//                                    if (par.getNrValidade() == 0){
//                                        ((DataObject)listaDependentes.get(i)).setArgumento4(null);
//                                    }else if (par.getNrValidade() > 0 && par.isValidade()){
//                                        if (!fisica.getNascimento().equals("") && fisica.getNascimento() != null){
//                                            ((DataObject)listaDependentes.get(i)).setArgumento4(
//                                                       ((new DataHoje()).incrementarAnos(par.getNrValidade(),fisica.getNascimento())).substring(3, 10)
//                                            );
//                                        }else{
//                                            ((DataObject)listaDependentes.get(i)).setArgumento4(null);
//                                        }
//                                    }else if (par.getNrValidade() > 0 && !par.isValidade()){
//                                        ((DataObject)listaDependentes.get(i)).setArgumento4(
//                                                   ((new DataHoje()).incrementarAnos(par.getNrValidade(), DataHoje.data())).substring(3, 10)
//                                        );
//                                    }
//                                    ((DataObject)listaDependentes.get(i)).setArgumento0(fisica);
//                                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
//                                    msgConfirmaTela = "Dependente adicionado!";
//                                }
//                                break;
//                            }
//                        }
//                    }
//                }
//                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
//            }
//        }
//        return listaDependentes;
//    }
//
//    public String excluirDependente(){
////        if (servicoPessoa.servicoPessoa.getId() != -1){
////            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
////            SociosDB db = new SociosDBToplink();
////            sv.abrirTransacao();
////            Socios soc = db.pesquisaSocioPorPessoa(((Fisica)((DataObject) htmlTable.getRowData()).getArgumento0()).getPessoa().getId());
////            if (soc.getId() == -1){
////                listaDependentes.remove(htmlTable.getRowIndex());
////                sv.desfazerTransacao();
////            }else if (!excluirDependentes(sv, soc)){
////                sv.desfazerTransacao();
////            }else{
////                sv.comitarTransacao();
////                listaDependentes.remove(htmlTable.getRowIndex());
////                //servicoPessoa.adicionaTitular = true;
////            }
////        }else{
////            listaDependentes.remove(htmlTable.getRowIndex());
////            //servicoPessoa.adicionaTitular = true;
////        }
//        return "socios";
//    }
//
//    public boolean excluirDependentes(SalvarAcumuladoDB sv, Socios soc){
//        SociosDB db = new SociosDBToplink();
//        ServicoPessoaDB dbS = new ServicoPessoaDBToplink();
//
//        List<SocioCarteirinha> list = db.pesquisaCarteirinhasPorSocio(soc.getId());
//
//        if (!list.isEmpty()){
//            for (SocioCarteirinha socioCarteirinha : list) {
//                if (!sv.deletarObjeto(sv.pesquisaCodigo(socioCarteirinha.getId(), "SocioCarteirinha"))){
//                    msgConfirmaTela = "Erro ao excluir carteirinha do Dependente!";
//                    return false;
//                }
//            }
//        }
//
//        if (!sv.deletarObjeto(sv.pesquisaCodigo(soc.getId(),"Socios"))){
//            msgConfirmaTela = "Erro ao excluir Dependente!";
//            return false;
//        }
//
//        ServicoPessoa serPessoa = dbS.pesquisaServicoPessoaPorPessoa(soc.getServicoPessoa().getPessoa().getId());
//        if (!sv.deletarObjeto(sv.pesquisaCodigo(serPessoa.getId(),"ServicoPessoa"))){
//            msgConfirmaTela = "Erro ao excluir serviço pessoa dependente!";
//            return false;
//        }
//        msgConfirmaTela = "Dependente excluído!";
//        return true;
//    }
//
//    public String atualizaRefDep(){
////        ParentescoDB db = new ParentescoDBToplink();
////        if (( (Fisica)((DataObject)htmlTable.getRowData()).getArgumento0() ).getId() != -1){
////            Parentesco par = db.pesquisaCodigo(Integer.parseInt(
////                                                             getListaParentesco().get(Integer.parseInt((String) ( (DataObject)htmlTable.getRowData()).getArgumento1())).getDescription())
////                                              );
////            if (par.getNrValidade() == 0){
////                ((DataObject)htmlTable.getRowData()).setArgumento4(null);
////            }else if (par.getNrValidade() > 0 && par.isValidade()){
////                if (!( (Fisica)((DataObject)htmlTable.getRowData()).getArgumento0() ).getNascimento().equals("") && ((Fisica)((DataObject)htmlTable.getRowData()).getArgumento0()).getNascimento() != null){
////                    ((DataObject)htmlTable.getRowData()).setArgumento4(
////                               ((new DataHoje()).incrementarAnos(par.getNrValidade(),( (Fisica)((DataObject)htmlTable.getRowData()).getArgumento0() ).getNascimento())).substring(3, 10)
////                    );
////                }else{
////                    ((DataObject)htmlTable.getRowData()).setArgumento4(null);
////                }
////            }else if (par.getNrValidade() > 0 && !par.isValidade()){
////                ((DataObject)htmlTable.getRowData()).setArgumento4(
////                           ((new DataHoje()).incrementarAnos(par.getNrValidade(), DataHoje.data())).substring(3, 10)
////                );
////            }
////        }
//        return "socios";
//    }
//
//    public String salvar(){
//        SalvarAcumuladoDB dbSalvar = new SalvarAcumuladoDBToplink();
//        SociosDB db = new SociosDBToplink();
//        MatriculaSociosDB dbMat = new MatriculaSociosDBToplink();
//        ServicoPessoaDB dbSerP = new ServicoPessoaDBToplink();
//        ServicoPessoa servicoP = new ServicoPessoa();
//        ParentescoDB dbPar = new ParentescoDBToplink();
//        ServicoCategoriaDB dbSCat = new ServicoCategoriaDBToplink();
//        ServicoCategoria servicoCategoria = new ServicoCategoria();
//        //pessoaEmpresa = new PessoaEmpresa();
//        GrupoCategoriaDB dbGCat = new GrupoCategoriaDBToplink();
//        GrupoCategoria gpCategoria = new GrupoCategoria();
//        Socios socioDep = new Socios();
//
//        if ( servicoPessoa.servicoPessoa.getPessoa().getId() == -1 ){
//            msgConfirma = "Pesquise um sócio!";
//            return null;
//        }
//        if (matriculaSocios.getCidade().getId() == -1 ){
//            msgConfirma = "Não pode ser salvo sem Cidade de Convênio!";
//            return null;
//        }
//
//        if (matriculaSocios.getEmissao().isEmpty()){
//            msgConfirma = "Data de emissáo inválida!";
//            return null;
//        }
//
//        if (getListaGrupoCategoria().isEmpty()){
//            msgConfirma = "Lista de Grupos Categoria Vazia!";
//            return null;
//        }
//
//        if (getListaCategoria().isEmpty()){
//            msgConfirma = "Lista de Categoria Vazia!";
//            return null;
//        }
//
//        if (servicoPessoa.listaTipoDocumento.isEmpty()){
//            msgConfirma = "Lista de Tipo Documentos Vazia!";
//            return null;
//        }
//        int idCat = Integer.parseInt(getListaCategoria().get(idCategoria).getDescription());
//        servicoCategoria = dbSCat.pesquisaPorParECat(1, idCat);
//        if (servicoCategoria == null){
//            msgConfirma = "Não existe Serviço Categoria cadastrado!";
//            return null;
//        }
//        gpCategoria = dbGCat.pesquisaCodigo( Integer.parseInt(getListaGrupoCategoria().get(idGrupoCategoria).getDescription()) );
//        dbSalvar.abrirTransacao();
//        if (servicoPessoa.servicoPessoa.isDescontoFolha()){
//            if (pessoaEmpresa.getId() == -1){
//                msgConfirma = "Este sócio não possui empresa para desconto em folha!";
//                servicoPessoa.servicoPessoa.setDescontoFolha(false);
//                dbSalvar.desfazerTransacao();
//                return null;
//            }
//            servicoPessoa.servicoPessoa.setCobranca(pessoaEmpresa.getJuridica().getPessoa());
//        }else{
//            servicoPessoa.servicoPessoa.setCobranca(servicoPessoa.servicoPessoa.getPessoa());
//        }
//        if (servicoPessoa.servicoPessoa.getId() == -1){
//            if (db.pesquisaSocioPorPessoaAtivo( servicoPessoa.servicoPessoa.getPessoa().getId() ).getId() != -1){
//                msgConfirma = "Esta pessoa já um sócio Cadastrado!";
//                dbSalvar.desfazerTransacao();
//                return null;
//            }
//            servicoPessoa.servicoPessoa.setAtivo(true);
//            if ( servicoPessoa.salvarServicoPessoa( servicoCategoria.getServicos() , dbSalvar) ){
//                    matriculaSocios.setCategoria( servicoCategoria.getCategoria());
//                    matriculaSocios.setMotivoInativacao(null);
//                    matriculaSocios.setTitular(servicoPessoa.servicoPessoa.getPessoa());
//                    if (matriculaSocios.getNrMatricula() <= 0){
//                        matriculaSocios.setNrMatricula(gpCategoria.getNrProximaMatricula());
//                        gpCategoria.setNrProximaMatricula(matriculaSocios.getNrMatricula()+1);
//                    }else if (matriculaSocios.getNrMatricula() > gpCategoria.getNrProximaMatricula()){
//                            matriculaSocios.setNrMatricula(gpCategoria.getNrProximaMatricula());
//                            gpCategoria.setNrProximaMatricula(matriculaSocios.getNrMatricula()+1);
//                    }else if (matriculaSocios.getNrMatricula() < gpCategoria.getNrProximaMatricula() &&
//                            dbMat.pesquisaPorNrMatricula(matriculaSocios.getNrMatricula(), servicoCategoria.getCategoria().getId()) != null){
//                            matriculaSocios.setNrMatricula(gpCategoria.getNrProximaMatricula());
//                            gpCategoria.setNrProximaMatricula(matriculaSocios.getNrMatricula()+1);
//                    }else if (matriculaSocios.getNrMatricula() < gpCategoria.getNrProximaMatricula() &&
//                            dbMat.pesquisaPorNrMatricula(matriculaSocios.getNrMatricula(), servicoCategoria.getCategoria().getId()) == null){
//                            //////////////////////////////////// NAO FAZ NADA
//                    }
//
//                    if (dbSalvar.inserirObjeto(matriculaSocios)){
//                        dbSalvar.alterarObjeto(gpCategoria);
//                        socios.setMatriculaSocios(matriculaSocios);
//                        socios.setParentesco(dbPar.pesquisaCodigo(1));
//                        socios.setServicoPessoa(servicoPessoa.servicoPessoa);
//                        socios.setNrViaCarteirinha(1);
//                        dbSalvar.inserirObjeto(socios);
//                        int idParentesco = 0;
//                        for(int i = 0; i < listaDependentes.size(); i++){
//                            idParentesco = Integer.parseInt(getListaParentesco().get( Integer.parseInt((String)((DataObject)listaDependentes.get(i)).getArgumento1()) ).getDescription() );
//                            servicoCategoria = dbSCat.pesquisaPorParECat(idParentesco, idCat);
//                            if (servicoCategoria == null){
//                                msgConfirma = "Erro para Serviço Categoria: "+((Fisica)((DataObject)listaDependentes.get(i)).getArgumento0()).getPessoa().getNome();
//                                dbSalvar.desfazerTransacao();
//                                return null;
//                            }
//                            socioDep = new Socios();
//                            servicoP.setDescontoFolha(servicoPessoa.servicoPessoa.isDescontoFolha());
//                            servicoP.setEmissao(DataHoje.data());
//                            servicoP.setNrDesconto(servicoPessoa.servicoPessoa.getNrDesconto());
//                            servicoP.setNrDiaVencimento(servicoPessoa.servicoPessoa.getNrDiaVencimento());
//                            servicoP.setReferenciaValidade((String)((DataObject)listaDependentes.get(i)).getArgumento4());
//                            servicoP.setReferenciaVigoracao(servicoPessoa.servicoPessoa.getReferenciaVigoracao());
//                            servicoP.setServicos(servicoCategoria.getServicos());
//                            servicoP.setTipoDocumento(servicoPessoa.servicoPessoa.getTipoDocumento());
//                            servicoP.setAtivo(servicoPessoa.servicoPessoa.isAtivo());
//                            servicoP.setPessoa( ((Fisica)((DataObject)listaDependentes.get(i)).getArgumento0()).getPessoa() );
//                            servicoP.setCobranca(servicoPessoa.servicoPessoa.getCobranca());
//                            dbSalvar.inserirObjeto(servicoP);
//
//                            socioDep.setValidadeCarteirinha( (String)((DataObject)listaDependentes.get(i)).getArgumento3() );
//                            socioDep.setServicoPessoa(servicoP);
//                            socioDep.setMatriculaSocios(matriculaSocios);
//                            socioDep.setNrViaCarteirinha( Integer.parseInt((String)((DataObject)listaDependentes.get(i)).getArgumento2()) );
//                            socioDep.setParentesco(dbPar.pesquisaCodigo(idParentesco));
//                            dbSalvar.inserirObjeto(socioDep);
//
//                            servicoP = new ServicoPessoa();
//                            socioDep = new Socios();
//                        }
//                        msgConfirma = "Sócio cadastrado com sucesso!";
//                        dbSalvar.comitarTransacao();
//                    }else{
//                        msgConfirma = "Erro ao Salvar Matricula!";
//                        dbSalvar.desfazerTransacao();
//                    }
//            }else{
//                if (servicoPessoa.chkContaCobranca){
//                    msgConfirma = "Não existe Serviço Conta Cobrança!";
//                }else
//                    msgConfirma = "Erro ao salvar Serviço Pessoa!";
//                dbSalvar.desfazerTransacao();
//            }
//        }else{
//            if ( servicoPessoa.atualizarServicoPessoa( servicoCategoria.getServicos(), dbSalvar )){
//                matriculaSocios.setCategoria( servicoCategoria.getCategoria() );
//                matriculaSocios.setTitular(servicoPessoa.servicoPessoa.getPessoa());
//                    if (matriculaSocios.getNrMatricula() <= 0){
//                        matriculaSocios.setNrMatricula(gpCategoria.getNrProximaMatricula());
//                        gpCategoria.setNrProximaMatricula(matriculaSocios.getNrMatricula()+1);
//                    }else if (matriculaSocios.getNrMatricula() > gpCategoria.getNrProximaMatricula()){
//                            matriculaSocios.setNrMatricula(gpCategoria.getNrProximaMatricula());
//                            gpCategoria.setNrProximaMatricula(matriculaSocios.getNrMatricula()+1);
//                    }else if (matriculaSocios.getNrMatricula() < gpCategoria.getNrProximaMatricula() &&
//                            dbMat.pesquisaPorNrMatricula(matriculaSocios.getNrMatricula(), servicoCategoria.getCategoria().getId()) != null){
//                            matriculaSocios.setNrMatricula(gpCategoria.getNrProximaMatricula());
//                            gpCategoria.setNrProximaMatricula(matriculaSocios.getNrMatricula()+1);
//                    }else if (matriculaSocios.getNrMatricula() < gpCategoria.getNrProximaMatricula() &&
//                            dbMat.pesquisaPorNrMatricula(matriculaSocios.getNrMatricula(), servicoCategoria.getCategoria().getId()) == null){
//                            //////////////////////////////////// NAO FAZ NADA
//                    }
//                    if (dbSalvar.alterarObjeto(matriculaSocios)){
//                        dbSalvar.alterarObjeto(gpCategoria);
//                        socios.setMatriculaSocios(matriculaSocios);
//                        socios.setParentesco(dbPar.pesquisaCodigo(1));
//                        socios.setServicoPessoa(servicoPessoa.servicoPessoa);
//                        socios.setNrViaCarteirinha(1);
//                        dbSalvar.alterarObjeto(socios);
//                        int idParentesco = 0;
//                        if (servicoPessoa.servicoPessoa.isAtivo()){
//                            for(int i = 0; i < listaDependentes.size(); i++){
//                                socioDep = new Socios();
//                                if (((Fisica)((DataObject)listaDependentes.get(i)).getArgumento0()).getId() != -1){
//                                    socioDep = db.pesquisaSocioPorPessoaAtivo(((Fisica)((DataObject)listaDependentes.get(i)).getArgumento0()).getPessoa().getId());
//                                    if (socioDep.getId() == -1){
//                                        idParentesco = Integer.parseInt(getListaParentesco().get( Integer.parseInt((String)((DataObject)listaDependentes.get(i)).getArgumento1()) ).getDescription() );
//                                        servicoCategoria = dbSCat.pesquisaPorParECat(idParentesco, idCat);
//                                        if (servicoCategoria == null){
//                                            msgConfirma = "Erro para Serviço Categoria: "+((Fisica)((DataObject)listaDependentes.get(i)).getArgumento0()).getPessoa().getNome();
//                                            dbSalvar.desfazerTransacao();
//                                            return null;
//                                        }
//
//                                        socioDep = new Socios();
//                                        servicoP.setDescontoFolha(servicoPessoa.servicoPessoa.isDescontoFolha());
//                                        servicoP.setEmissao(DataHoje.data());
//                                        servicoP.setNrDesconto(servicoPessoa.servicoPessoa.getNrDesconto());
//                                        servicoP.setNrDiaVencimento(servicoPessoa.servicoPessoa.getNrDiaVencimento());
//                                        servicoP.setReferenciaValidade((String)((DataObject)listaDependentes.get(i)).getArgumento4());
//                                        servicoP.setReferenciaVigoracao(servicoPessoa.servicoPessoa.getReferenciaVigoracao());
//                                        servicoP.setServicos(servicoCategoria.getServicos());
//                                        servicoP.setTipoDocumento(servicoPessoa.servicoPessoa.getTipoDocumento());
//                                        servicoP.setPessoa( ((Fisica)((DataObject)listaDependentes.get(i)).getArgumento0()).getPessoa() );
//                                        servicoP.setAtivo(servicoPessoa.servicoPessoa.isAtivo());
//                                        servicoP.setCobranca(servicoPessoa.servicoPessoa.getCobranca());
//                                        dbSalvar.inserirObjeto(servicoP);
//
//                                        socioDep.setValidadeCarteirinha( (String)((DataObject)listaDependentes.get(i)).getArgumento3() );
//                                        socioDep.setServicoPessoa(servicoP);
//                                        socioDep.setMatriculaSocios(matriculaSocios);
//                                        socioDep.setNrViaCarteirinha( Integer.parseInt((String)((DataObject)listaDependentes.get(i)).getArgumento2()) );
//                                        socioDep.setParentesco(dbPar.pesquisaCodigo(idParentesco));
//                                        dbSalvar.inserirObjeto(socioDep);
//
//                                    }else{
//                                        idParentesco = Integer.parseInt(getListaParentesco().get( Integer.parseInt((String)((DataObject)listaDependentes.get(i)).getArgumento1()) ).getDescription() );
//                                        servicoCategoria = dbSCat.pesquisaPorParECat(idParentesco, idCat);
//                                        if (servicoCategoria == null){
//                                            msgConfirma = "Erro para Serviço Categoria: "+((Fisica)((DataObject)listaDependentes.get(i)).getArgumento0()).getPessoa().getNome();
//                                            dbSalvar.desfazerTransacao();
//                                            return null;
//                                        }
//
//                                        servicoP = dbSerP.pesquisaServicoPessoaPorPessoa(((Fisica)((DataObject)listaDependentes.get(i)).getArgumento0()).getPessoa().getId());
//                                        servicoP.setDescontoFolha(servicoPessoa.servicoPessoa.isDescontoFolha());
//                                        servicoP.setNrDesconto(servicoPessoa.servicoPessoa.getNrDesconto());
//                                        servicoP.setNrDiaVencimento(servicoPessoa.servicoPessoa.getNrDiaVencimento());
//                                        servicoP.setReferenciaValidade((String)((DataObject)listaDependentes.get(i)).getArgumento4());
//                                        servicoP.setReferenciaVigoracao(servicoPessoa.servicoPessoa.getReferenciaVigoracao());
//                                        servicoP.setServicos(servicoCategoria.getServicos());
//                                        servicoP.setTipoDocumento(servicoPessoa.servicoPessoa.getTipoDocumento());
//                                        servicoP.setPessoa( ((Fisica)((DataObject)listaDependentes.get(i)).getArgumento0()).getPessoa() );
//                                        servicoP.setAtivo(servicoPessoa.servicoPessoa.isAtivo());
//                                        servicoP.setCobranca(servicoPessoa.servicoPessoa.getCobranca());
//                                        dbSalvar.alterarObjeto(servicoP);
//
//                                        socioDep.setValidadeCarteirinha( (String)((DataObject)listaDependentes.get(i)).getArgumento3() );
//                                        socioDep.setServicoPessoa(servicoP);
//                                        socioDep.setMatriculaSocios(matriculaSocios);
//                                        socioDep.setNrViaCarteirinha( Integer.parseInt((String)((DataObject)listaDependentes.get(i)).getArgumento2()) );
//                                        socioDep.setParentesco(dbPar.pesquisaCodigo(idParentesco));
//                                        dbSalvar.alterarObjeto(socioDep);
//                                    }
//                                }
//                                servicoP = new ServicoPessoa();
//                                socioDep = new Socios();
//                            }
//                        }
//                        msgConfirma = "Sócio atualizado com sucesso!";
//                        dbSalvar.comitarTransacao();
//                    }else{
//                        msgConfirma = "Erro ao Atualizar Matricula!";
//                        dbSalvar.desfazerTransacao();
//                    }
//            }else{
//                if (servicoPessoa.chkContaCobranca){
//                    msgConfirma = "Não existe Serviço Conta Cobrança!";
//                }else
//                    msgConfirma = "Erro ao Atualizar Serviço Pessoa!";
//                dbSalvar.desfazerTransacao();
//            }
//        }
//        return null;
//    }
//
//    public String salvarFisicaDependente(){
//        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
//        FisicaDB db = new FisicaDBToplink();
//        TipoDocumentoDB dbDoc = new TipoDocumentoDBToplink();
//        List listDocumento = new ArrayList();
//        if (temFoto){
//            dependente.setDataFoto(DataHoje.data());
//        }else{
//            dependente.setDtFoto(null);
//        }
//        if ( dependente.getNascimento().isEmpty() || dependente.getNascimento().length() < 10){
//            msgConfirma = "Data de Nascimento esta inválida!";
//            return null;
//        }
//        if (dependente.getPessoa().getNome().equals("")){
//            msgConfirma = "O campo nome não pode ser nulo! ";
//            return null;
//        }
//
//        if (dependente.getId() == -1){
//
//            if (dependente.getPessoa().getDocumento().equals("") || dependente.getPessoa().getDocumento().equals("0")){
//                dependente.getPessoa().setDocumento("0");
//            }else{
//                if( !ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(dependente.getPessoa().getDocumento()))){
//                    msgConfirma = "Documento Invalido!";
//                    return null;
//                }
//                listDocumento = db.pesquisaFisicaPorDoc(dependente.getPessoa().getDocumento());
//                if(!listDocumento.isEmpty()){
//                    msgConfirma = "Documento já existente!";
//                    return null;
//                }
//            }
//
//            if (!db.pesquisaFisicaPorNomeNascRG(dependente.getPessoa().getNome(),
//                                                dependente.getDtNascimento(),
//                                                dependente.getRg()).isEmpty()){
//                msgConfirma = "Esta pessoa já esta cadastrada!";
//                return null;
//            }
//
//            dependente.getPessoa().setTipoDocumento(dbDoc.pesquisaCodigo(1));
//            sv.abrirTransacao();
//            if (!sv.inserirObjeto(dependente.getPessoa())){
//                msgConfirma = "Erro ao salvar Pessoa!";
//                sv.desfazerTransacao();
//                return null;
//            }
//
//            if (!sv.inserirObjeto(dependente)){
//                msgConfirma = "Erro ao salvar Cadastro!";
//                sv.desfazerTransacao();
//                return null;
//            }
//            sv.comitarTransacao();
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fisicaPesquisa",dependente);
//            msgConfirma = "Dependente salvo com sucesso!";
//        }else{
//            if (dependente.getPessoa().getDocumento().equals("") || dependente.getPessoa().getDocumento().equals("0")){
//                 dependente.getPessoa().setDocumento("0");
//            }else{
//               if( !ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(dependente.getPessoa().getDocumento()))){
//                    msgConfirma = "Documento Inválido!";
//                    return null;
//                }
//                listDocumento = db.pesquisaFisicaPorDoc(dependente.getPessoa().getDocumento());
//                for (int i = 0; i < listDocumento.size(); i++){
//                    if(!listDocumento.isEmpty() && ((Fisica)listDocumento.get(i)).getId() != dependente.getId()){
//                        msgConfirma = "Documento já existente!";
//                        return null;
//                    }
//                }
//            }
//            sv.abrirTransacao();
//            if (!sv.alterarObjeto(dependente.getPessoa())){
//                msgConfirma = "Erro ao atualizar Pessoa!";
//                sv.desfazerTransacao();
//                return null;
//            }
//
//            if (!sv.alterarObjeto(dependente)){
//                msgConfirma = "Erro ao atualizar Cadastro!";
//                sv.desfazerTransacao();
//                return null;
//            }
//            sv.comitarTransacao();
//            ((DataObject)listaDependentes.get(indiceDependente)).setArgumento0(dependente);
//            msgConfirma = "Dependente atualizado com sucesso!";
//        }
//        return null;
//    }
//
//    public void salvarImagem(){
//        FacesContext context = FacesContext.getCurrentInstance();
//        if(dependente.getId() != -1){
//        String caminho = ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Imagens/Fotos/"+String.valueOf(dependente.getPessoa().getId())+".jpg");
//        String caminho2 = ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Imagens/Fotos/fotoTemp.jpg");
//        try{
//            File fl = new File(caminho);
//            File item = new File(caminho2);
//            FileInputStream in = new FileInputStream(item);
//            FileOutputStream out = new FileOutputStream(fl.getPath());
//
//            byte[] buf = new byte[(int)item.length()];
//            int count;
//            while ((count = in.read(buf)) >= 0) {
//                out.write(buf, 0, count);
//            }
//            in.close();
//            out.flush();
//            out.close();
//            }catch(Exception e){
//                System.out.println(e);
//            }
//        }
//    }
//
//    public void excluirImagem(){
//        FacesContext context = FacesContext.getCurrentInstance();
//        String caminho = "";
//        caminho = ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Imagens/Fotos/fotoTemp.jpg");
//        try{
//            File fl = new File(caminho);
//            if (fl.exists()){
//                fl.delete();
//            }else if(dependente.getId() != -1){
//                caminho = ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Imagens/Fotos/"+String.valueOf(dependente.getPessoa().getId())+".jpg");
//                fl = new File(caminho);
//                fl.delete();
//            }
//        }catch(Exception e){
//            System.out.println(e);
//        }
//    }
//
////    public void upload(UploadEvent event){
////        this.itens.add(event.getUploadItem());
////        UploadItem item = event.getUploadItem();
////        FacesContext facesContext = FacesContext.getCurrentInstance();
////        String nomeArq = "fotoTemp";
////        fotoTemp = true;
////
////        String caminho = ((ServletContext) facesContext.getExternalContext().getContext()).getRealPath("/Imagens/Fotos/");
////        caminho = caminho + "/" + nomeArq+".jpg";
////        try{
////            File fl = new File(caminho);
////            FileInputStream in = new FileInputStream(item.getFile());
////            FileOutputStream out = new FileOutputStream(fl.getPath());
////
////            byte[] buf = new byte[(int)item.getFile().length()];
////            int count;
////            while ((count = in.read(buf)) >= 0) {
////                out.write(buf, 0, count);
////            }
////            in.close();
////            out.flush();
////            out.close();
////            temFoto = true;
////            }catch(Exception e){
////                temFoto = false;
////                System.out.println(e);
////            }
////    }
//
//    public String getPessoaImagem(){
//        FacesContext context = FacesContext.getCurrentInstance();
//        File files;
//        files = new File(((ServletContext) context.getExternalContext().getContext()).getRealPath("/Imagens/Fotos/"));
//        File listFile[] = files.listFiles();
//        String nome = "";
//        String caminho = "";
//        temFoto = false;
//        if (fotoTemp){
//            nome = "fotoTemp";
//        }else
//            nome = "semFoto";
//        int numArq = listFile.length;
//        for (int i = 0; i < numArq;i++){
//            String n = listFile[i].getName();
//            for (int o = 0; o < n.length();o++ ){
//                if (n.substring(o, o+1).equals(".")){
//                    n = listFile[i].getName().substring(0, o);
//                }
//            }
//            try{
//                if (!fotoTemp){
//                    if (Integer.parseInt(n) == dependente.getPessoa().getId()){
//                        nome = n;
//                        fotoTemp = false;
//                        temFoto = true;
//                        caminho = ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Imagens/Fotos/fotoTemp.jpg");
//                        File fl = new File(caminho);
//                        fl.delete();
//                        break;
//                    }
//                }else{
//                    fotoTemp = false;
//                    break;
//                }
//            }catch(Exception e){
//                continue;
//            }
//        }
//        return nome+".jpg";
//    }
//
////    public List getDependentes(){
////        if (servicoPessoaJSFBean.isDependentes()){
////            Fisica fisica = new Fisica();
////            Parentesco par = new Parentesco();
////            SociosDB db = new SociosDBToplink();
////            FisicaDB dbFis = new FisicaDBToplink();
////            ParentescoDB dbPar = new ParentescoDBToplink();
////            List listaPar = dbPar.pesquisaTodosSemTitular();
////            int qntParentesco = listaPar.size();
////            int index = 0;
////            boolean atualiza = true;
////            if (servicoPessoaJSFBean.getServicoPessoa().getId() == -1){
////                if (!listaDependentes.isEmpty()){
////                    if ( FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa") != null){
////                        for (int i = 0; i < listaDependentes.size(); i++){
////                            if ( ((Fisica)((DataObject)listaDependentes.get(i)).getArgumento0()).getId() == -1 ){
////                                fisica = (Fisica)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa");
////                                if (fisica.getPessoa().getId() == servicoPessoaJSFBean.getServicoPessoa().getPessoa().getId()){
////                                    msgConfirmaTela = "Dependente não pode ser Titular!";
////                                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
////                                    return listaDependentes;
////                                }
////                                if (db.pesquisaSocioPorPessoaAtivo( fisica.getPessoa().getId() ) != null){
////                                    msgConfirmaTela = "Este dependente já um sócio Cadastrado!";
////                                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
////                                    return listaDependentes;
////                                }
////
////                                for (int w = 0; w < listaDependentes.size(); w++){
////                                    if (((Fisica)((DataObject)listaDependentes.get(w)).getArgumento0()).getId()
////                                        == fisica.getId()){
////                                        atualiza = false;
////                                        msgConfirmaTela = "Dependente já existe!";
////                                        break;
////                                    }
////                                }
////                                par = dbPar.pesquisaCodigo( Integer.parseInt(getListaParentesco().get( Integer.parseInt((String)((DataObject)listaDependentes.get(i)).getArgumento1()) ).getDescription() ));
////                                if (atualiza){
////                                    if (par.getNrValidade() == 0){
////                                        ((DataObject)listaDependentes.get(i)).setArgumento4(null);
////                                    }else if (par.getNrValidade() > 0 && par.isValidade()){
////                                        if (!fisica.getNascimento().equals("") && fisica.getNascimento() != null){
////                                            ((DataObject)listaDependentes.get(i)).setArgumento4(
////                                                       ((new DataHoje()).incrementarAnos(par.getNrValidade(),fisica.getNascimento())).substring(3, 10)
////                                            );
////                                        }else{
////                                            ((DataObject)listaDependentes.get(i)).setArgumento4(null);
////                                        }
////                                    }else if (par.getNrValidade() > 0 && !par.isValidade()){
////                                        ((DataObject)listaDependentes.get(i)).setArgumento4(
////                                                   ((new DataHoje()).incrementarAnos(par.getNrValidade(), DataHoje.data())).substring(3, 10)
////                                        );
////                                    }
////                                    ((DataObject)listaDependentes.get(i)).setArgumento0(fisica);
////                                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
////                                    msgConfirmaTela = "Dependente adicionado!";
////                                }
////                                break;
////                            }
////                        }
////                    }
////                }
////                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
////            }else{
////                Fisica fis = new Fisica();
////                if (listaDepsBanco.isEmpty()){
////                    String validadeDep = "";
////                    listaDepsBanco = db.pesquisaDependentes(matriculaSocios.getId());
////                    if (!listaDepsBanco.isEmpty())
////                        listaDependentes.clear();
////                    for (int i = 0; i < listaDepsBanco.size(); i++){
////                        fis = dbFis.pesquisaFisicaPorPessoa(((Socios)listaDepsBanco.get(i)).getServicoPessoa().getPessoa().getId());
////
////                        for(int w = 0; w < qntParentesco; w++){
////                            if ( ((Socios)listaDepsBanco.get(i)).getParentesco().getId() == ((Parentesco)listaPar.get(w)).getId() ){
////                                index = w;
////                                break;
////                            }
////                        }
////
////                        if (((Socios)listaDepsBanco.get(i)).getParentesco().getNrValidade() == 0){
////                            validadeDep = "";
////                        }else if (((Socios)listaDepsBanco.get(i)).getParentesco().getNrValidade() > 0 && ((Socios)listaDepsBanco.get(i)).getParentesco().isValidade()){
////                            if (!fis.getNascimento().equals("") && fis.getNascimento() != null){
////                                validadeDep = ((new DataHoje()).incrementarAnos(((Socios)listaDepsBanco.get(i)).getParentesco().getNrValidade(),fis.getNascimento())).substring(3, 10);
////                            }else{
////                                validadeDep = "";
////                            }
////                        }else if (((Socios)listaDepsBanco.get(i)).getParentesco().getNrValidade() > 0 && !((Socios)listaDepsBanco.get(i)).getParentesco().isValidade()){
////                            validadeDep = ((new DataHoje()).incrementarAnos(((Socios)listaDepsBanco.get(i)).getParentesco().getNrValidade(), DataHoje.data())).substring(3, 10);
////                        }
////
////                        DataObject dtObj =  new DataObject( fis, // NOME
////                                                           index, // IDPARENTESCO
////                                                           ((Socios)listaDepsBanco.get(i)).getNrViaCarteirinha(),      // VIA CARTEIRINHA
////                                                           ((Socios)listaDepsBanco.get(i)).getValidadeCarteirinha(),   // DATA VALIDADE
////                                                           validadeDep,// DATA VAL DEP
////                                                           null);
////                        listaDependentes.add(dtObj);
////                        index = 0;
////                        fis = new Fisica();
////                    }
////                }
////                if (!listaDependentes.isEmpty()){
////                    if ( FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa") != null){
////                        for (int i = 0; i < listaDependentes.size(); i++){
////                            if ( ((Fisica)((DataObject)listaDependentes.get(i)).getArgumento0()).getId() == -1 ){
////                                fisica = (Fisica)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa");
////                                if (fisica.getPessoa().getId() == servicoPessoaJSFBean.getServicoPessoa().getPessoa().getId()){
////                                    msgConfirmaTela = "Dependente não pode ser Titular!";
////                                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
////                                    return listaDependentes;
////                                }
////                                if (db.pesquisaSocioPorPessoaAtivo( fisica.getPessoa().getId() ) != null){
////                                    msgConfirmaTela = "Este dependente já um sócio Cadastrado!";
////                                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
////                                    return listaDependentes;
////                                }
////                                for (int w = 0; w < listaDependentes.size(); w++){
////                                    if (((Fisica)((DataObject)listaDependentes.get(w)).getArgumento0()).getId()
////                                        == fisica.getId()){
////                                        atualiza = false;
////                                        msgConfirmaTela = "Dependente já existe!";
////                                        break;
////                                    }
////                                }
////                                par = dbPar.pesquisaCodigo( Integer.parseInt(getListaParentesco().get( Integer.parseInt((String)((DataObject)listaDependentes.get(i)).getArgumento1()) ).getDescription() ));
////                                if (atualiza){
////                                    if (par.getNrValidade() == 0){
////                                        ((DataObject)listaDependentes.get(i)).setArgumento4(null);
////                                    }else if (par.getNrValidade() > 0 && par.isValidade()){
////                                        if (!fisica.getNascimento().equals("") && fisica.getNascimento() != null){
////                                            ((DataObject)listaDependentes.get(i)).setArgumento4(
////                                                       ((new DataHoje()).incrementarAnos(par.getNrValidade(),fisica.getNascimento())).substring(3, 10)
////                                            );
////                                        }else{
////                                            ((DataObject)listaDependentes.get(i)).setArgumento4(null);
////                                        }
////                                    }else if (par.getNrValidade() > 0 && !par.isValidade()){
////                                        ((DataObject)listaDependentes.get(i)).setArgumento4(
////                                                   ((new DataHoje()).incrementarAnos(par.getNrValidade(), DataHoje.data())).substring(3, 10)
////                                        );
////                                    }
////                                    ((DataObject)listaDependentes.get(i)).setArgumento0(fisica);
////                                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
////                                    msgConfirmaTela = "Dependente adicionado!";
////                                }
////                                break;
////                            }
////                        }
////                    }
////                }
////                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
////            }
////            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
////        }
////        return listaDependentes;
////    }
//
//    public String inativarSocio(){
//        if (socios.getId() != -1){
//            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
//            SociosDB db = new SociosDBToplink();
//            if(dataInativacao.length() < 10){
//                msgConfirma = "Data de inativação inválida!";
//                return null;
//            }
//
//            if(DataHoje.converteDataParaInteger(dataInativacao) > DataHoje.converteDataParaInteger(DataHoje.data()) ){
//                msgConfirma = "Data de inativação maior que dia de hoje!";
//                return null;
//            }
//
//            sv.abrirTransacao();
//
//            ServicoPessoa sp = (ServicoPessoa) sv.pesquisaCodigo(servicoPessoa.servicoPessoa.getId(),"ServicoPessoa");
//            sp.setAtivo(false);
//            List<Socios> listaDps = db.pesquisaDependentes(matriculaSocios.getId());
//            if (!sv.alterarObjeto(sp)){
//                msgConfirma = "Erro ao alterar serviço pessoa";
//                sv.desfazerTransacao();
//                return null;
//            }
//            servicoPessoa.servicoPessoa = sp;
//            ServicoPessoa sp2 = new ServicoPessoa();
//            for(int i = 0; i < listaDps.size();i++){
//                sp2 = (ServicoPessoa)sv.pesquisaCodigo(listaDps.get(i).getServicoPessoa().getId(),"ServicoPessoa");
//                sp2.setAtivo(false);
//                if (!sv.alterarObjeto(sp2)){
//                    msgConfirma = "Erro ao alterar serviço pessoa do Dependente";
//                    sv.desfazerTransacao();
//                    return null;
//                }
//                sp2 = new ServicoPessoa();
//            }
//
//            matriculaSocios.setMotivoInativacao((SMotivoInativacao)sv.pesquisaCodigo(Integer.parseInt(getListaMotivoInativacao().get(idInativacao).getDescription()),"SMotivoInativacao"));
//            matriculaSocios.setInativo(dataInativacao);
//            if (!sv.alterarObjeto(matriculaSocios)){
//                msgConfirma = "Erro ao alterar matrícula";
//                sv.desfazerTransacao();
//                return null;
//            }
//
//            msgConfirma = "Sócio inativado com sucesso!";
//            sv.comitarTransacao();
//        }else{
//            msgConfirma = "Não existe sócio para ser inativado!";
//        }
//        return null;
//    }
//
//    public String reativarSocio(){
//        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
//        SociosDB db = new SociosDBToplink();
//
//        if (db.pesquisaSocioPorPessoaAtivo(socios.getServicoPessoa().getPessoa().getId()).getId() != -1){
//            msgConfirma = "Este sócio já esta cadastrado!";
//            return null;
//        }
//
//        sv.abrirTransacao();
//
//        ServicoPessoa sp = (ServicoPessoa) sv.pesquisaCodigo(servicoPessoa.servicoPessoa.getId(),"ServicoPessoa");
//        sp.setAtivo(true);
//        if (!sv.alterarObjeto(sp)){
//            msgConfirma = "Erro ao alterar serviço pessoa";
//            sv.desfazerTransacao();
//            return null;
//        }
//        servicoPessoa.servicoPessoa = sp;
//        ServicoPessoa sp2 = new ServicoPessoa();
//        List<Socios> listaDps = db.pesquisaDependentes(matriculaSocios.getId());
//        for(int i = 0; i < listaDps.size();i++){
//            sp2 = (ServicoPessoa)sv.pesquisaCodigo(listaDps.get(i).getServicoPessoa().getId(),"ServicoPessoa");
//            sp2.setAtivo(true);
//            if (!sv.alterarObjeto(sp2)){
//                msgConfirma = "Erro ao alterar serviço pessoa do Dependente";
//                sv.desfazerTransacao();
//                return null;
//            }
//            sp2 = new ServicoPessoa();
//        }
//        matriculaSocios.setMotivoInativacao(null);
//        matriculaSocios.setDtInativo(null);
//        if (!sv.alterarObjeto(matriculaSocios)){
//            msgConfirma = "Erro ao ativar matrícula";
//            sv.desfazerTransacao();
//            return null;
//        }
//        msgConfirma = "Sócio ativado com sucesso!";
//        dataInativacao = DataHoje.data();
//        sv.comitarTransacao();
//        return null;
//    }
//
//    public String editar(){
//        CategoriaDB dbCat = new CategoriaDBToplink();
//        GrupoCategoria gpCat = new GrupoCategoria();
//        SociosDB db = new SociosDBToplink();
//        Socios soc = new Socios();
////        socios = (Socios)htmlTablePesquisa.getRowData();
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado",true);
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("socioPesquisa",socios);
//        descPesquisa = "";
//        porPesquisa = "nome";
//        comoPesquisa = "";
//        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno").equals("emissaoCarteirinha")){
//            return (String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
//        }
//
//        soc = db.pesquisaSocioDoDependente(socios.getId());
//        if (soc != null)
//            socios = soc;
//        servicoPessoa.servicoPessoa = socios.getServicoPessoa();
//        matriculaSocios = socios.getMatriculaSocios();
//        if (!matriculaSocios.getInativo().isEmpty())
//            dataInativacao = matriculaSocios.getInativo();
//        else
//            dataInativacao = DataHoje.data();
//        listaDependentes = new ArrayList();
//        listaDepsBanco = new ArrayList();
//        gpCat = dbCat.pesquisaGrupoPorCategoria(socios.getMatriculaSocios().getCategoria().getId());
//        for (int i = 0; i < getListaGrupoCategoria().size(); i ++){
//            if (Integer.parseInt( (String)getListaGrupoCategoria().get(i).getDescription() ) == gpCat.getId()){
//                idGrupoCategoria = i;
//                break;
//            }
//        }
//        int qntCategoria = getListaCategoria().size();
//        for (int i = 0; i < qntCategoria; i ++){
//            if (Integer.parseInt( (String)getListaCategoria().get(i).getDescription() ) == socios.getMatriculaSocios().getCategoria().getId()){
//                idCategoria = i;
//                break;
//            }
//        }
//        servicoPessoa.editar(socios.getServicoPessoa());
//
//        PessoaEmpresaDB dbEP = new PessoaEmpresaDBToplink();
//        PessoaEnderecoDB dbEnd = new PessoaEnderecoDBToplink();
//        FisicaDB dbFis = new FisicaDBToplink();
//        servicoPessoa.titular = dbFis.pesquisaFisicaPorPessoa(servicoPessoa.servicoPessoa.getPessoa().getId());
//        servicoPessoa.titularEndereco = dbEnd.pesquisaEndPorPessoaTipo(servicoPessoa.titular.getPessoa().getId(), 3);
//        pessoaEmpresa = dbEP.pesquisaPessoaEmpresaPorFisica(servicoPessoa.titular.getId());
//        //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fisicaPesquisa", fisica);
//        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") == null)
//            return "socios";
//        else
//            return (String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
//    }
//
//    public void editarGenerico(Pessoa sessao){
//            CategoriaDB dbCat = new CategoriaDBToplink();
//            GrupoCategoria gpCat = new GrupoCategoria();
//            SociosDB db = new SociosDBToplink();
//            Socios soc = new Socios();
//            Socios socSessao = new Socios();
//
//            socSessao = db.pesquisaSocioPorPessoaAtivo(sessao.getId());
//            if (socSessao.getId() != -1)
//                socios = socSessao;
//            else{
//                novoGenerico();
//                //servicoPessoa.servicoPessoa.setPessoa(sessao);
//                return;
//            }
//            descPesquisa = "";
//            porPesquisa = "nome";
//            comoPesquisa = "";
//            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno").equals("emissaoCarteirinha")){
//                return ;
//            }
//
//            soc = db.pesquisaSocioDoDependente(socios.getId());
//            if (soc != null){
//                socios = soc;
//            }
//            
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado",true);
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("socioPesquisa",socios);
//            servicoPessoa.servicoPessoa = socios.getServicoPessoa();
//            matriculaSocios = socios.getMatriculaSocios();
//            if (!matriculaSocios.getInativo().isEmpty())
//                dataInativacao = matriculaSocios.getInativo();
//            else
//                dataInativacao = DataHoje.data();
//            listaDependentes = new ArrayList();
//            listaDepsBanco = new ArrayList();
//            gpCat = dbCat.pesquisaGrupoPorCategoria(socios.getMatriculaSocios().getCategoria().getId());
//            for (int i = 0; i < getListaGrupoCategoria().size(); i ++){
//                if (Integer.parseInt( (String)getListaGrupoCategoria().get(i).getDescription() ) == gpCat.getId()){
//                    idGrupoCategoria = i;
//                    break;
//                }
//            }
//            int qntCategoria = getListaCategoria().size();
//            for (int i = 0; i < qntCategoria; i ++){
//                if (Integer.parseInt( (String)getListaCategoria().get(i).getDescription() ) == socios.getMatriculaSocios().getCategoria().getId()){
//                    idCategoria = i;
//                    break;
//                }
//            }
//            servicoPessoa.editar(socios.getServicoPessoa());
//
//            PessoaEmpresaDB dbEP = new PessoaEmpresaDBToplink();
//            PessoaEnderecoDB dbEnd = new PessoaEnderecoDBToplink();
//            FisicaDB dbFis = new FisicaDBToplink();
//            servicoPessoa.titular = dbFis.pesquisaFisicaPorPessoa(servicoPessoa.servicoPessoa.getPessoa().getId());
//            servicoPessoa.titularEndereco = dbEnd.pesquisaEndPorPessoaTipo(servicoPessoa.titular.getPessoa().getId(), 3);
//            pessoaEmpresa = dbEP.pesquisaPessoaEmpresaPorFisica(servicoPessoa.titular.getId());
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fisicaPesquisa", servicoPessoa.titular);
//    }
//
//    public List getPesquisaSocios(){
//        List result = null;
//        SociosDB db = new SociosDBToplink();
//        result = db.pesquisaSocios(descPesquisa , porPesquisa, comoPesquisa, statusPesquisa);
//    return result;
//    }
//
//    public String excluir(){
//        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
//        SociosDB db = new SociosDBToplink();
//        SocioCarteirinhaDB dbC = new SocioCarteirinhaDBToplink();
//        if (servicoPessoa.servicoPessoa.getId() != -1){
//            // DELETAR DEPENDENTES -----
//            sv.abrirTransacao();
//            for (int i = 0; i < listaDependentes.size();i++){
//                Socios soc = db.pesquisaSocioPorPessoa(((Fisica)((DataObject) listaDependentes.get(i)).getArgumento0()).getPessoa().getId());
//                if (soc.getId() == -1){
////                    listaDependentes.remove(htmlTable.getRowIndex());
//                }else if (!excluirDependentes(sv, (Socios)sv.pesquisaCodigo(soc.getId(), "Socios"))){
//                    sv.desfazerTransacao();
//                    return null;
//                }
//            }
//
//            // DELETAR SOCIOS CARTEIRINHA -------
//            List<SocioCarteirinha> socs = dbC.pesquisaSocioCarteirinhaSocio(socios.getId());
//            for(int w = 0; w < socs.size();w++){
//                if (!sv.deletarObjeto(sv.pesquisaCodigo(socs.get(w).getId(),"SocioCarteirinha"))){
//                    msgConfirma = "Erro ao Deletar Sócio carteirinha!";
//                    sv.desfazerTransacao();
//                    return null;
//                }
//            }
//
//            // DELETAR SOCIOS ------
//            if (!sv.deletarObjeto(sv.pesquisaCodigo(socios.getId(), "Socios"))){
//                msgConfirma = "Erro ao Deletar Sócio!";
//                sv.desfazerTransacao();
//                return null;
//            }
//
//            if (!sv.deletarObjeto(sv.pesquisaCodigo(matriculaSocios.getId(), "MatriculaSocios"))){
//                msgConfirma = "Erro ao Deletar Matricula!";
//                sv.desfazerTransacao();
//                return null;
//            }
//
//            if (!sv.deletarObjeto(sv.pesquisaCodigo(servicoPessoa.servicoPessoa.getId(), "ServicoPessoa"))){
//                msgConfirma = "Erro ao Deletar Servico Pessoa!";
//                sv.desfazerTransacao();
//                return null;
//            }
//            msgConfirma = "Cadastro deletado com sucesso!";
//            sv.comitarTransacao();
//            novoGenerico();
//        }
//        return null;
//    }
////    public String excluir(){
////        SociosDB db = new SociosDBToplink();
////        SocioCarteirinhaDB dbC = new SocioCarteirinhaDBToplink();
////        ServicoPessoaDB dbS = new ServicoPessoaDBToplink();
////        MatriculaSociosDB dbM = new MatriculaSociosDBToplink();
////        List listDeps = new ArrayList();
////        if (servicoPessoa.servicoPessoa.getId() != -1){
////            // DELETAR DEPENDENTES -----
////            listDeps = db.pesquisaDependentes(matriculaSocios.getId());
////            Socios soc = new Socios();
////            List<SocioCarteirinha> listCart = new ArrayList();
////            ServicoPessoa servP = new ServicoPessoa();
////            for (int i = 0; i < listDeps.size(); i++){
////                soc = db.pesquisaSocioPorId( ((Socios)listDeps.get(i)).getServicoPessoa().getId() );
////                if (soc == null){
////                    listaDependentes.remove(i);
////                }
////                servP = dbS.pesquisaServicoPessoaPorPessoa(soc.getServicoPessoa().getPessoa().getId());
////                listCart = dbC.pesquisaSocioCarteirinhaSocio(soc.getId());
////                for(int w = 0; w < listCart.size();w++){
////                    dbC.delete(dbC.pesquisaCodigo( listCart.get(w).getId() ));
////                }
////                if (db.delete(soc)){
////                    if(dbS.delete(servP)){
////
////                    }else{
////                        continue;
////                    }
////                }else{
////                    continue;
////                }
////                soc = new Socios();
////                servP = new ServicoPessoa();
////            }
////            List<SocioCarteirinha> socs = dbC.pesquisaSocioCarteirinhaSocio(socios.getId());
////            for(int w = 0; w < socs.size();w++){
////                dbC.delete(dbC.pesquisaCodigo(socs.get(w).getId()));
////            }
////
////            if(db.delete( db.pesquisaCodigo(socios.getId()))){
////                if(dbM.delete(dbM.pesquisaCodigo(matriculaSocios.getId()))){
////                    if (dbS.delete(dbS.pesquisaCodigo(servicoPessoa.servicoPessoa.getId()))){
////                        msgConfirma = "Cadastro deletado com sucesso!";
////                    }else
////                        msgConfirma = "Erro ao Deletar Servico Pessoa!";
////                }else
////                    msgConfirma = "Erro ao Deletar Matricula!";
////            }else
////                msgConfirma = "Erro ao Deletar Sócio!";
////        }
////        novoGenerico();
////        return null;
////    }
//
//    public String imprimirFichaSocial(){
//        Fisica fisica = new Fisica();
//        Juridica sindicato = new Juridica();
//        FisicaDB db = new FisicaDBToplink();
//        JuridicaDB dbJur= new JuridicaDBToplink();
//        PessoaEndereco pesEndereco, pesDestinatario, pesEndEmpresa, pesEndSindicato = new PessoaEndereco();
//        PessoaEnderecoDB dbEnd = new PessoaEnderecoDBToplink();
//        //PessoaEmpresa pesEmpresa = new PessoaEmpresa();
//        PessoaEmpresaDB dbEmp = new PessoaEmpresaDBToplink();
//        SociosDB dbSoc = new SociosDBToplink();
//            try{
//                FacesContext faces = FacesContext.getCurrentInstance();
//                HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
//                byte[] arquivo = new byte[0];
//                JasperReport jasper = null;
//                Collection listaSocios = new ArrayList<FichaSocial>();
//                jasper = (JasperReport) JRLoader.loadObject(
//                     ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/FICHACADASTRO.jasper"));
//
//                fisica = db.pesquisaFisicaPorPessoa(socios.getServicoPessoa().getPessoa().getId());
//                pesEndereco = dbEnd.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 1);
//                sindicato = dbJur.pesquisaCodigo(1);
//
//                if (pessoaEmpresa.getId() != -1)
//                    pesEndEmpresa = dbEnd.pesquisaEndPorPessoaTipo(pessoaEmpresa.getJuridica().getPessoa().getId(), 2);
//                else
//                    pesEndEmpresa = new PessoaEndereco();
//
//                pesEndSindicato = dbEnd.pesquisaEndPorPessoaTipo(sindicato.getPessoa().getId(), 2);
//
//                // DESTINATARIO -------------------------------------------------------------------------
//                if (tipoDestinario.equals("socio")){
//                    pesDestinatario = dbEnd.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 1);
//                }else{
//                    if (pessoaEmpresa.getId() != -1)
//                        pesDestinatario = dbEnd.pesquisaEndPorPessoaTipo(pessoaEmpresa.getJuridica().getPessoa().getId(), 2);
//                    else
//                        pesDestinatario = new PessoaEndereco();
//                }
//                // --------------------------------------------------------------------------------------
//                // --------------------------------------------------------------------------------------
//                String dados[] = new String[30];
//
//                try{
//                    dados[0] = pesEndereco.getEndereco().getLogradouro().getDescricao();
//                    dados[1] = pesEndereco.getEndereco().getDescricaoEndereco().getDescricao();
//                    dados[2] = pesEndereco.getNumero();
//                    dados[3] = pesEndereco.getComplemento();
//                    dados[4] = pesEndereco.getEndereco().getBairro().getDescricao();
//                    dados[5] = pesEndereco.getEndereco().getCidade().getCidade();
//                    dados[6] = pesEndereco.getEndereco().getCidade().getUf();
//                    dados[7] = AnaliseString.mascaraCep(pesEndereco.getEndereco().getCep());
//                }catch(Exception e){
//                    dados[0] = "";
//                    dados[1] = "";
//                    dados[2] = "";
//                    dados[3] = "";
//                    dados[4] = "";
//                    dados[5] = "";
//                    dados[6] = "";
//                    dados[7] = "";
//                }
//
//                try{
//                    dados[8] = pesDestinatario.getEndereco().getLogradouro().getDescricao();
//                    dados[9] = pesDestinatario.getEndereco().getDescricaoEndereco().getDescricao();
//                    dados[10] = pesDestinatario.getNumero();
//                    dados[11] = pesDestinatario.getComplemento();
//                    dados[12] = pesDestinatario.getEndereco().getBairro().getDescricao();
//                    dados[13] = pesDestinatario.getEndereco().getCidade().getCidade();
//                    dados[14] = pesDestinatario.getEndereco().getCidade().getUf();
//                    dados[15] = AnaliseString.mascaraCep(pesDestinatario.getEndereco().getCep());
//                    dados[26] = pesDestinatario.getPessoa().getDocumento();
//                    dados[27] =  pesDestinatario.getPessoa().getNome();
//                }catch(Exception e){
//                    dados[8] = "";
//                    dados[9] = "";
//                    dados[10] = "";
//                    dados[11] = "";
//                    dados[12] = "";
//                    dados[13] = "";
//                    dados[14] = "";
//                    dados[15] = "";
//                    dados[26] = "";
//                    dados[27] = "";
//                }
//
//                try{
//                    dados[16] = pessoaEmpresa.getJuridica().getPessoa().getNome();
//                    dados[17] = pessoaEmpresa.getJuridica().getPessoa().getTelefone1();
//                    dados[18] = pessoaEmpresa.getFuncao().getProfissao();
//                    dados[19] = pesEndEmpresa.getEndereco().getDescricaoEndereco().getDescricao();
//                    dados[20] = pesEndEmpresa.getNumero();
//                    dados[21] = pesEndEmpresa.getComplemento();
//                    dados[22] = pesEndEmpresa.getEndereco().getBairro().getDescricao();
//                    dados[23] = pesEndEmpresa.getEndereco().getCidade().getCidade();
//                    dados[24] = pesEndEmpresa.getEndereco().getCidade().getUf();
//                    dados[25] = AnaliseString.mascaraCep(pesEndEmpresa.getEndereco().getCep());
//                    dados[28] = pessoaEmpresa.getAdmissao();
//                    dados[29] = pessoaEmpresa.getJuridica().getPessoa().getDocumento();
//                }catch(Exception e){
//                    dados[16] = "";
//                    dados[17] = "";
//                    dados[18] = "";
//                    dados[19] = "";
//                    dados[20] = "";
//                    dados[21] = "";
//                    dados[22] = "";
//                    dados[23] = "";
//                    dados[24] = "";
//                    dados[25] = "";
//                    dados[28] = "";
//                    dados[29] = "";
//                }
//
//                try{
//                    listaSocios.add(new FichaSocial(0,
//                                                    matriculaSocios.getTitular().getId(),
//                                                    matriculaSocios.getNrMatricula(),
//                                                    socios.getServicoPessoa().getDtEmissao(),
//                                                    null,
//                                                    matriculaSocios.getCategoria().getGrupoCategoria().getGrupoCategoria(),
//                                                    matriculaSocios.getCategoria().getCategoria(),
//                                                    fisica.getPessoa().getNome(),
//                                                    fisica.getSexo(),
//                                                    fisica.getDtNascimento(),
//                                                    fisica.getNaturalidade(),
//                                                    fisica.getNacionalidade(),
//                                                    fisica.getRg(),
//                                                    fisica.getPessoa().getDocumento(),
//                                                    fisica.getCarteira(),
//                                                    fisica.getSerie(),
//                                                    fisica.getEstadoCivil(),
//                                                    fisica.getPai(),
//                                                    fisica.getMae(),
//                                                    fisica.getPessoa().getTelefone1(),
//                                                    fisica.getPessoa().getTelefone3(),
//                                                    fisica.getPessoa().getEmail1(),
//                                                    dados[0],
//                                                    dados[1],
//                                                    dados[2],
//                                                    dados[3],
//                                                    dados[4],
//                                                    dados[5],
//                                                    dados[6],
//                                                    dados[7],
//                                                    imprimirVerso,
//                                                    dados[26],
//                                                    dados[27],
//                                                    dados[8],
//                                                    dados[9],
//                                                    dados[10],
//                                                    dados[11],
//                                                    dados[12],
//                                                    dados[13],
//                                                    dados[14],
//                                                    dados[15],
//                                                    dados[16],
//                                                    dados[17],
//                                                    null, // fax
//                                                    DataHoje.converte(dados[28]),
//                                                    dados[18],
//                                                    dados[19],
//                                                    dados[20],
//                                                    dados[21],
//                                                    dados[22],
//                                                    dados[23],
//                                                    dados[24],
//                                                    dados[25],
//                                                    ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Imagens/LogoCliente.png"),
//                                                    "", // obs
//                                                    socios.getParentesco().getParentesco(),
//                                                    sindicato.getPessoa().getNome(),
//                                                    pesEndSindicato.getEndereco().getDescricaoEndereco().getDescricao(),
//                                                    pesEndSindicato.getNumero(),
//                                                    pesEndSindicato.getComplemento(),
//                                                    pesEndSindicato.getEndereco().getBairro().getDescricao(),
//                                                    pesEndSindicato.getEndereco().getCidade().getCidade(),
//                                                    pesEndSindicato.getEndereco().getCidade().getUf(),
//                                                    AnaliseString.mascaraCep(pesEndSindicato.getEndereco().getCep()),
//                                                    sindicato.getPessoa().getDocumento(),
//                                                    "",
//                                                    ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Imagens/LogoCliente.png"),
//                                                    getFotoSocio(),
//                                                    sindicato.getPessoa().getEmail1(),
//                                                    sindicato.getPessoa().getSite(),
//                                                    sindicato.getPessoa().getTelefone1(),
//                                                    ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/FICHACADASTROVERSO.jasper"),
//                                                    dados[29],
//                                                    fisica.getDtRecadastro()));
//
//                    List<Socios> deps = dbSoc.pesquisaDependentesOrdenado(matriculaSocios.getId());
//                    for(int n = 0; n < deps.size();n ++){
//                        listaSocios.add(new FichaSocial(0,
//                                                        deps.get(n).getServicoPessoa().getPessoa().getId(),
//                                                        matriculaSocios.getNrMatricula(),
//                                                        null,
//                                                        null,
//                                                        "",
//                                                        matriculaSocios.getCategoria().getCategoria(),
//                                                        deps.get(n).getServicoPessoa().getPessoa().getNome(),
//                                                        db.pesquisaFisicaPorPessoa(deps.get(n).getServicoPessoa().getPessoa().getId()).getSexo(),
//                                                        db.pesquisaFisicaPorPessoa(deps.get(n).getServicoPessoa().getPessoa().getId()).getDtNascimento(),
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        imprimirVerso,
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        null,
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Imagens/LogoCliente.png"),
//                                                        "",
//                                                        deps.get(n).getParentesco().getParentesco(),
//                                                        "",
//                                                        "", 
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Imagens/LogoCliente.png"),
//                                                        ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Imagens/Fotos/semFoto.jpg"),
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/FICHACADASTROVERSO.jasper"),
//                                                        "",
//                                                        null));
//                    }
//                    JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(listaSocios);
//                    JasperPrint print = JasperFillManager.fillReport(
//                             jasper,
//                             null,
//                             dtSource);
//                     arquivo = JasperExportManager.exportReportToPdf(print);
//                     response.setContentType("application/pdf");
//                     response.setContentLength(arquivo.length);
//                     ServletOutputStream saida = response.getOutputStream();
//                     saida.write(arquivo, 0, arquivo.length);
//                     saida.flush();
//                     saida.close();
//                } catch (Exception erro) {
//                    System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
//                    return null;
//                }
//        } catch (Exception erro) {
//            System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
//            return null;
//        }
//        FacesContext.getCurrentInstance().responseComplete();
//        Download download =  new Download(
//                "Ficha Social "+fisica.getPessoa().getId()+".pdf",
//                ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/socios.jsf"),
//                "application/pdf",
//                FacesContext.getCurrentInstance());
//        download.baixar();
//        return null;
//    }
//
//    public String visualizarCarteirinha(){
//        FilialDB dbF = new FilialDBToplink();
//        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
//        SocioCarteirinha soc = new SocioCarteirinha();
//        if (dbF.pesquisaCodigoRegistro(1).isCarteirinhaDependente()){
//            imprimirCarteirinhaComDependente();
//        }else{
//            imprimirCarteirinhaSemDependente();
//        }
//        soc.setEmissao(DataHoje.data());
//        soc.setSocios(socios);
//        db.insert(soc);
//        soc = new SocioCarteirinha();
//        for (int i = 0; i < listaDepsBanco.size();i++){
//            soc.setEmissao(DataHoje.data());
//            soc.setSocios((Socios)listaDepsBanco.get(i));
//            db.insert(soc);
//            soc = new SocioCarteirinha();
//        }
//        return null;
//    }
//
//    public String imprimirCarteirinhaComDependente(){
//        Fisica fisica = new Fisica();
//        Juridica sindicato = new Juridica();
//        FisicaDB db = new FisicaDBToplink();
//        JuridicaDB dbJur= new JuridicaDBToplink();
//        PessoaEndereco pesEndereco, pesDestinatario, pesEndEmpresa, pesEndSindicato = new PessoaEndereco();
//        PessoaEnderecoDB dbEnd = new PessoaEnderecoDBToplink();
//        //PessoaEmpresa pesEmpresa = new PessoaEmpresa();
//        PessoaEmpresaDB dbEmp = new PessoaEmpresaDBToplink();
//        SociosDB dbSoc = new SociosDBToplink();
//            try{
//                FacesContext faces = FacesContext.getCurrentInstance();
//                HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
//                byte[] arquivo = new byte[0];
//                JasperReport jasper = null;
//                Collection listaSocios = new ArrayList<FichaSocial>();
//                jasper = (JasperReport) JRLoader.loadObject(
//                     ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/Personalizado/CARTEIRINHA.jasper"));
//
//                fisica = db.pesquisaFisicaPorPessoa(socios.getServicoPessoa().getPessoa().getId());
//                pesEndereco = dbEnd.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 1);
//                sindicato = dbJur.pesquisaCodigo(1);
//
//                if (pessoaEmpresa.getId() != -1)
//                    pesEndEmpresa = dbEnd.pesquisaEndPorPessoaTipo(pessoaEmpresa.getJuridica().getPessoa().getId(), 2);
//                else
//                    pesEndEmpresa = dbEnd.pesquisaEndPorPessoaTipo(pessoaEmpresa.getJuridica().getPessoa().getId(), 2);
//                
//                pesEndSindicato = dbEnd.pesquisaEndPorPessoaTipo(sindicato.getPessoa().getId(), 2);
//
//                pesDestinatario = dbEnd.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 1);
//
//                String dados[] = new String[30];
//
//                try{
//                    dados[0] = pesEndereco.getEndereco().getLogradouro().getDescricao();
//                    dados[1] = pesEndereco.getEndereco().getDescricaoEndereco().getDescricao();
//                    dados[2] = pesEndereco.getNumero();
//                    dados[3] = pesEndereco.getComplemento();
//                    dados[4] = pesEndereco.getEndereco().getBairro().getDescricao();
//                    dados[5] = pesEndereco.getEndereco().getCidade().getCidade();
//                    dados[6] = pesEndereco.getEndereco().getCidade().getUf();
//                    dados[7] = AnaliseString.mascaraCep(pesEndereco.getEndereco().getCep());
//                }catch(Exception e){
//                    dados[0] = "";
//                    dados[1] = "";
//                    dados[2] = "";
//                    dados[3] = "";
//                    dados[4] = "";
//                    dados[5] = "";
//                    dados[6] = "";
//                    dados[7] = "";
//                }
//
//                try{
//                    dados[8] = pesDestinatario.getEndereco().getLogradouro().getDescricao();
//                    dados[9] = pesDestinatario.getEndereco().getDescricaoEndereco().getDescricao();
//                    dados[10] = pesDestinatario.getNumero();
//                    dados[11] = pesDestinatario.getComplemento();
//                    dados[12] = pesDestinatario.getEndereco().getBairro().getDescricao();
//                    dados[13] = pesDestinatario.getEndereco().getCidade().getCidade();
//                    dados[14] = pesDestinatario.getEndereco().getCidade().getUf();
//                    dados[15] = AnaliseString.mascaraCep(pesDestinatario.getEndereco().getCep());
//                    dados[26] = pesDestinatario.getPessoa().getDocumento();
//                    dados[27] =  pesDestinatario.getPessoa().getNome();
//                }catch(Exception e){
//                    dados[8] = "";
//                    dados[9] = "";
//                    dados[10] = "";
//                    dados[11] = "";
//                    dados[12] = "";
//                    dados[13] = "";
//                    dados[14] = "";
//                    dados[15] = "";
//                    dados[26] = "";
//                    dados[27] = "";
//                }
//
//                try{
//                    dados[16] = pessoaEmpresa.getJuridica().getPessoa().getNome();
//                    dados[17] = pessoaEmpresa.getJuridica().getPessoa().getTelefone1();
//                    dados[18] = pessoaEmpresa.getFuncao().getProfissao();
//                    dados[19] = pesEndEmpresa.getEndereco().getDescricaoEndereco().getDescricao();
//                    dados[20] = pesEndEmpresa.getNumero();
//                    dados[21] = pesEndEmpresa.getComplemento();
//                    dados[22] = pesEndEmpresa.getEndereco().getBairro().getDescricao();
//                    dados[23] = pesEndEmpresa.getEndereco().getCidade().getCidade();
//                    dados[24] = pesEndEmpresa.getEndereco().getCidade().getUf();
//                    dados[25] = AnaliseString.mascaraCep(pesEndEmpresa.getEndereco().getCep());
//                    dados[28] = pessoaEmpresa.getAdmissao();
//                    dados[29] = pessoaEmpresa.getJuridica().getPessoa().getDocumento();
//                }catch(Exception e){
//                    dados[16] = "";
//                    dados[17] = "";
//                    dados[18] = "";
//                    dados[19] = "";
//                    dados[20] = "";
//                    dados[21] = "";
//                    dados[22] = "";
//                    dados[23] = "";
//                    dados[24] = "";
//                    dados[25] = "";
//                    dados[28] = "";
//                    dados[29] = "";
//                }
//
//                try{
//                    listaSocios.add(new FichaSocial(0,
//                                                    matriculaSocios.getTitular().getId(),
//                                                    matriculaSocios.getNrMatricula(),
//                                                    socios.getServicoPessoa().getDtEmissao(),
//                                                    null,
//                                                    matriculaSocios.getCategoria().getGrupoCategoria().getGrupoCategoria(),
//                                                    matriculaSocios.getCategoria().getCategoria(),
//                                                    fisica.getPessoa().getNome(),
//                                                    fisica.getSexo(),
//                                                    fisica.getDtNascimento(),
//                                                    fisica.getNaturalidade(),
//                                                    fisica.getNacionalidade(),
//                                                    fisica.getRg(),
//                                                    fisica.getPessoa().getDocumento(),
//                                                    fisica.getCarteira(),
//                                                    fisica.getSerie(),
//                                                    fisica.getEstadoCivil(),
//                                                    fisica.getPai(),
//                                                    fisica.getMae(),
//                                                    fisica.getPessoa().getTelefone1(),
//                                                    fisica.getPessoa().getTelefone3(),
//                                                    fisica.getPessoa().getEmail1(),
//                                                    dados[0],
//                                                    dados[1],
//                                                    dados[2],
//                                                    dados[3],
//                                                    dados[4],
//                                                    dados[5],
//                                                    dados[6],
//                                                    dados[7],
//                                                    imprimirVerso,
//                                                    dados[26],
//                                                    dados[27],
//                                                    dados[8],
//                                                    dados[9],
//                                                    dados[10],
//                                                    dados[11],
//                                                    dados[12],
//                                                    dados[13],
//                                                    dados[14],
//                                                    dados[15],
//                                                    dados[16],
//                                                    dados[17],
//                                                    null, // fax
//                                                    DataHoje.converte(dados[28]),
//                                                    dados[18],
//                                                    dados[19],
//                                                    dados[20],
//                                                    dados[21],
//                                                    dados[22],
//                                                    dados[23],
//                                                    dados[24],
//                                                    dados[25],
//                                                    ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Imagens/LogoCliente.png"),
//                                                    "", // obs
//                                                    socios.getParentesco().getParentesco(),
//                                                    sindicato.getPessoa().getNome(),
//                                                    pesEndSindicato.getEndereco().getDescricaoEndereco().getDescricao(),
//                                                    pesEndSindicato.getNumero(),
//                                                    pesEndSindicato.getComplemento(),
//                                                    pesEndSindicato.getEndereco().getBairro().getDescricao(),
//                                                    pesEndSindicato.getEndereco().getCidade().getCidade(),
//                                                    pesEndSindicato.getEndereco().getCidade().getUf(),
//                                                    AnaliseString.mascaraCep(pesEndSindicato.getEndereco().getCep()),
//                                                    sindicato.getPessoa().getDocumento(),
//                                                    "",
//                                                    ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Imagens/LogoCliente.png"),
//                                                    getFotoSocio(),
//                                                    sindicato.getPessoa().getEmail1(),
//                                                    sindicato.getPessoa().getSite(),
//                                                    sindicato.getPessoa().getTelefone1(),
//                                                    ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/Personalizado/CARTEIRINHAVERSO.jasper"),
//                                                    dados[29],
//                                                    fisica.getDtRecadastro()));
//
//                    List<Socios> deps = dbSoc.pesquisaDependentesOrdenado(matriculaSocios.getId());
//                    for(int n = 0; n < deps.size();n ++){
//                        listaSocios.add(new FichaSocial(0,
//                                                        deps.get(n).getServicoPessoa().getPessoa().getId(),
//                                                        matriculaSocios.getNrMatricula(),
//                                                        null,
//                                                        null,
//                                                        "",
//                                                        matriculaSocios.getCategoria().getCategoria(),
//                                                        deps.get(n).getServicoPessoa().getPessoa().getNome(),
//                                                        db.pesquisaFisicaPorPessoa(deps.get(n).getServicoPessoa().getPessoa().getId()).getSexo(),
//                                                        db.pesquisaFisicaPorPessoa(deps.get(n).getServicoPessoa().getPessoa().getId()).getDtNascimento(),
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        imprimirVerso,
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        null,
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Imagens/LogoCliente.png"),
//                                                        "",
//                                                        deps.get(n).getParentesco().getParentesco(),
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Imagens/LogoCliente.png"),
//                                                        ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Imagens/Fotos/semFoto.jpg"),
//                                                        "",
//                                                        "",
//                                                        "",
//                                                        ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/Personalizado/CARTEIRINHAVERSO.jasper"),
//                                                        "",
//                                                        null));
//                    }
//                    JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(listaSocios);
//                    JasperPrint print = JasperFillManager.fillReport(
//                             jasper,
//                             null,
//                             dtSource);
//                     arquivo = JasperExportManager.exportReportToPdf(print);
//                     response.setContentType("application/pdf");
//                     response.setContentLength(arquivo.length);
//                     ServletOutputStream saida = response.getOutputStream();
//                     saida.write(arquivo, 0, arquivo.length);
//                     saida.flush();
//                     saida.close();
//                } catch (Exception erro) {
//                    System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
//                    return null;
//                }
//        } catch (Exception erro) {
//            System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
//            return null;
//        }
//        FacesContext.getCurrentInstance().responseComplete();
//        Download download =  new Download(
//                "Ficha Social "+fisica.getPessoa().getId()+".pdf",
//                ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/socios.jsf"),
//                "application/pdf",
//                FacesContext.getCurrentInstance());
//        download.baixar();
//        return null;
//    }
//
//    public void imprimirCarteirinhaSemDependente(){
//        Fisica fisica = new Fisica();
//        Juridica sindicato = new Juridica();
//        FisicaDB db = new FisicaDBToplink();
//        JuridicaDB dbJur= new JuridicaDBToplink();
//        PessoaEndereco pesEndereco, pesDestinatario, pesEndEmpresa, pesEndSindicato = new PessoaEndereco();
//        PessoaEnderecoDB dbEnd = new PessoaEnderecoDBToplink();
//        PessoaEmpresa pesEmpresa = new PessoaEmpresa();
//        PessoaEmpresaDB dbEmp = new PessoaEmpresaDBToplink();
//        String dados[] = new String[30];
//        List<Socios> listaSocs = new ArrayList();
//            try{
//                FacesContext faces = FacesContext.getCurrentInstance();
//                HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
//                byte[] arquivo = new byte[0];
//                JasperReport jasper = null;
//                Collection listaSocios = new ArrayList<FichaSocial>();
//                jasper = (JasperReport) JRLoader.loadObject(
//                     ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/Personalizado/CARTEIRINHA.jasper"));
//
//                sindicato = dbJur.pesquisaCodigo(1);
//                pesEndSindicato = dbEnd.pesquisaEndPorPessoaTipo(sindicato.getPessoa().getId(), 2);
//
//                listaSocs.add(socios);
//                listaSocs.addAll(listaDepsBanco);
//
//                for (int i = 0; i < listaSocs.size();i++){
//                    fisica = db.pesquisaFisicaPorPessoa( listaSocs.get(i).getServicoPessoa().getPessoa().getId() );
//                    pesEndereco = dbEnd.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 1);
//                    pesEmpresa = dbEmp.pesquisaPessoaEmpresaPorFisica(fisica.getId());
//                    if (pesEmpresa.getId() != -1)
//                        pesEndEmpresa = dbEnd.pesquisaEndPorPessoaTipo(pesEmpresa.getJuridica().getPessoa().getId(), 2);
//                    else
//                        pesEndEmpresa = new PessoaEndereco();
//
//                    pesDestinatario = dbEnd.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 1);
//
//                    try{
//                        dados[0] = pesEndereco.getEndereco().getLogradouro().getDescricao();
//                        dados[1] = pesEndereco.getEndereco().getDescricaoEndereco().getDescricao();
//                        dados[2] = pesEndereco.getNumero();
//                        dados[3] = pesEndereco.getComplemento();
//                        dados[4] = pesEndereco.getEndereco().getBairro().getDescricao();
//                        dados[5] = pesEndereco.getEndereco().getCidade().getCidade();
//                        dados[6] = pesEndereco.getEndereco().getCidade().getUf();
//                        dados[7] = AnaliseString.mascaraCep(pesEndereco.getEndereco().getCep());
//                    }catch(Exception e){
//                        dados[0] = "";
//                        dados[1] = "";
//                        dados[2] = "";
//                        dados[3] = "";
//                        dados[4] = "";
//                        dados[5] = "";
//                        dados[6] = "";
//                        dados[7] = "";
//                    }
//                    try{
//                        dados[8] = pesDestinatario.getEndereco().getLogradouro().getDescricao();
//                        dados[9] = pesDestinatario.getEndereco().getDescricaoEndereco().getDescricao();
//                        dados[10] = pesDestinatario.getNumero();
//                        dados[11] = pesDestinatario.getComplemento();
//                        dados[12] = pesDestinatario.getEndereco().getBairro().getDescricao();
//                        dados[13] = pesDestinatario.getEndereco().getCidade().getCidade();
//                        dados[14] = pesDestinatario.getEndereco().getCidade().getUf();
//                        dados[15] = AnaliseString.mascaraCep(pesDestinatario.getEndereco().getCep());
//                        dados[26] = pesDestinatario.getPessoa().getDocumento();
//                        dados[27] =  pesDestinatario.getPessoa().getNome();
//                    }catch(Exception e){
//                        dados[8] = "";
//                        dados[9] = "";
//                        dados[10] = "";
//                        dados[11] = "";
//                        dados[12] = "";
//                        dados[13] = "";
//                        dados[14] = "";
//                        dados[15] = "";
//                        dados[26] = "";
//                        dados[27] = "";
//                    }
//                    try{
//                        dados[16] = pesEmpresa.getJuridica().getPessoa().getNome();
//                        dados[17] = pesEmpresa.getJuridica().getPessoa().getTelefone1();
//                        dados[18] = pesEmpresa.getFuncao().getProfissao();
//                        dados[19] = pesEndEmpresa.getEndereco().getDescricaoEndereco().getDescricao();
//                        dados[20] = pesEndEmpresa.getNumero();
//                        dados[21] = pesEndEmpresa.getComplemento();
//                        dados[22] = pesEndEmpresa.getEndereco().getBairro().getDescricao();
//                        dados[23] = pesEndEmpresa.getEndereco().getCidade().getCidade();
//                        dados[24] = pesEndEmpresa.getEndereco().getCidade().getUf();
//                        dados[25] = AnaliseString.mascaraCep(pesEndEmpresa.getEndereco().getCep());
//                        dados[28] = pesEmpresa.getAdmissao();
//                        dados[29] = pesEmpresa.getJuridica().getPessoa().getDocumento();
//                    }catch(Exception e){
//                        dados[16] = "";
//                        dados[17] = "";
//                        dados[18] = "";
//                        dados[19] = "";
//                        dados[20] = "";
//                        dados[21] = "";
//                        dados[22] = "";
//                        dados[23] = "";
//                        dados[24] = "";
//                        dados[25] = "";
//                        dados[28] = "";
//                        dados[29] = "";
//                    }
//
//                    try{
//                        listaSocios.add(new FichaSocial(0,
//                                                        listaSocs.get(i).getMatriculaSocios().getTitular().getId(),
//                                                        listaSocs.get(i).getMatriculaSocios().getNrMatricula(),
//                                                        listaSocs.get(i).getServicoPessoa().getDtEmissao(),
//                                                        null,
//                                                        listaSocs.get(i).getMatriculaSocios().getCategoria().getGrupoCategoria().getGrupoCategoria(),
//                                                        listaSocs.get(i).getMatriculaSocios().getCategoria().getCategoria(),
//                                                        fisica.getPessoa().getNome(),
//                                                        fisica.getSexo(),
//                                                        fisica.getDtNascimento(),
//                                                        fisica.getNaturalidade(),
//                                                        fisica.getNacionalidade(),
//                                                        fisica.getRg(),
//                                                        fisica.getPessoa().getDocumento(),
//                                                        fisica.getCarteira(),
//                                                        fisica.getSerie(),
//                                                        fisica.getEstadoCivil(),
//                                                        fisica.getPai(),
//                                                        fisica.getMae(),
//                                                        fisica.getPessoa().getTelefone1(),
//                                                        fisica.getPessoa().getTelefone3(),
//                                                        fisica.getPessoa().getEmail1(),
//                                                        dados[0],
//                                                        dados[1],
//                                                        dados[2],
//                                                        dados[3],
//                                                        dados[4],
//                                                        dados[5],
//                                                        dados[6],
//                                                        dados[7],
//                                                        false,
//                                                        dados[26],
//                                                        dados[27],
//                                                        dados[8],
//                                                        dados[9],
//                                                        dados[10],
//                                                        dados[11],
//                                                        dados[12],
//                                                        dados[13],
//                                                        dados[14],
//                                                        dados[15],
//                                                        dados[16],
//                                                        dados[17],
//                                                        null, // fax
//                                                        DataHoje.converte(dados[28]),
//                                                        dados[18],
//                                                        dados[19],
//                                                        dados[20],
//                                                        dados[21],
//                                                        dados[22],
//                                                        dados[23],
//                                                        dados[24],
//                                                        dados[25],
//                                                        ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Imagens/LogoCliente.png"),
//                                                        "", // obs
//                                                        listaSocs.get(i).getParentesco().getParentesco(),
//                                                        sindicato.getPessoa().getNome(),
//                                                        pesEndSindicato.getEndereco().getDescricaoEndereco().getDescricao(),
//                                                        pesEndSindicato.getNumero(),
//                                                        pesEndSindicato.getComplemento(),
//                                                        pesEndSindicato.getEndereco().getBairro().getDescricao(),
//                                                        pesEndSindicato.getEndereco().getCidade().getCidade(),
//                                                        pesEndSindicato.getEndereco().getCidade().getUf(),
//                                                        AnaliseString.mascaraCep(pesEndSindicato.getEndereco().getCep()),
//                                                        sindicato.getPessoa().getDocumento(),
//                                                        "",
//                                                        ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Imagens/LogoCliente.png"),
//                                                        getFotoSocioVarios(listaSocs.get(i)),
//                                                        sindicato.getPessoa().getEmail1(),
//                                                        sindicato.getPessoa().getSite(),
//                                                        sindicato.getPessoa().getTelefone1(),
//                                                        ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/Personalizado/CARTEIRINHAVERSO.jasper"),
//                                                        dados[29],
//                                                        fisica.getDtRecadastro()));
//                    } catch (Exception erro) {
//                        System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
//                        continue;
//                    }
//                }
//                JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(listaSocios);
//                JasperPrint print = JasperFillManager.fillReport(
//                         jasper,
//                         null,
//                         dtSource);
//                 arquivo = JasperExportManager.exportReportToPdf(print);
//                 response.setContentType("application/pdf");
//                 response.setContentLength(arquivo.length);
//                 ServletOutputStream saida = response.getOutputStream();
//                 saida.write(arquivo, 0, arquivo.length);
//                 saida.flush();
//                 saida.close();
//
//                FacesContext.getCurrentInstance().responseComplete();
//                Download download =  new Download(
//                        "Ficha Social "+fisica.getPessoa().getId()+".pdf",
//                        ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/socios.jsf"),
//                        "application/pdf",
//                        FacesContext.getCurrentInstance());
//                download.baixar();
//            } catch (Exception erro) {
//                System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
//                return;
//            }
//    }
//
//    public String getFotoSocio(){
//        FacesContext context = FacesContext.getCurrentInstance();
//        File files;
//        if (socios.getId() != -1){
//            files = new File(((ServletContext) context.getExternalContext().getContext()).getRealPath("/Imagens/Fotos/"+socios.getServicoPessoa().getPessoa().getId()+".jpg" ));
//            if (files.exists())
//                return files.getPath();
//            else
//                return ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Imagens/Fotos/semFoto.jpg");
//        }else
//            return ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Imagens/Fotos/semFoto.jpg");
//    }
//
//    public String getFotoSocioVarios(Socios soc){
//        FacesContext context = FacesContext.getCurrentInstance();
//        File files;
//        if (socios.getId() != -1){
//            files = new File(((ServletContext) context.getExternalContext().getContext()).getRealPath("/Imagens/Fotos/"+soc.getServicoPessoa().getPessoa().getId()+".jpg" ));
//            if (files.exists())
//                return files.getPath();
//            else
//                return ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Imagens/Fotos/semFoto.jpg");
//        }else
//            return ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Imagens/Fotos/semFoto.jpg");
//    }
//
//    public String refreshDependente(){
//        if (((Fisica)((DataObject)listaDependentes.get(indiceDependente)).getArgumento0()).getId() != -1){
//            FisicaDB db = new FisicaDBToplink();
//            dependente = db.pesquisaCodigo(((Fisica)((DataObject)listaDependentes.get(indiceDependente)).getArgumento0()).getId());
//        }else{
//            dependente = new Fisica();
//        }
//        return null;
//    }
//
//
//    public void indice(){
////        indiceDependente = htmlTable.getRowIndex();
////        servicoPessoa.adicionaTitular = false;
//    }
//
//    public void refreshForm(){
//
//    }
//
//    public void acaoPesquisaInicial(){
//        comoPesquisa = "I";
//    }
//
//    public void acaoPesquisaParcial(){
//        comoPesquisa = "P";
//    }
//
//    public Socios getSocios() {
//        return socios;
//    }
//
//    public void setSocios(Socios socios) {
//        this.socios = socios;
//    }
//
//    public String getMsgConfirma() {
//        return msgConfirma;
//    }
//
//    public void setMsgConfirma(String msgConfirma) {
//        this.msgConfirma = msgConfirma;
//    }
//
//    public int getIdCategoria() {
//        return idCategoria;
//    }
//
//    public void setIdCategoria(int idCategoria) {
//        this.idCategoria = idCategoria;
//    }
//
//    public int getIdGrupoCategoria() {
//        return idGrupoCategoria;
//    }
//
//    public void setIdGrupoCategoria(int idGrupoCategoria) {
//        this.idGrupoCategoria = idGrupoCategoria;
//    }
//
//    public String getStrEnderecoEmpresa() {
//        PessoaEnderecoDB pessoaEnderecoDB = new PessoaEnderecoDBToplink();
//        PessoaEndereco pesEndereco = new PessoaEndereco();
//       if (pessoaEmpresa.getJuridica().getPessoa().getId() != -1){
//           pesEndereco = pessoaEnderecoDB.pesquisaEndPorPessoaTipo(pessoaEmpresa.getJuridica().getPessoa().getId(), 2);
//            if (pesEndereco != null && pesEndereco.getId() != -1){
//                String strCompl = "";
//                if(pesEndereco.getComplemento().equals(""))
//                    strCompl = " ";
//                else
//                    strCompl = " ( "+pesEndereco.getComplemento()+ " ) ";
//
//                    strEnderecoEmpresa = pesEndereco.getEndereco().getLogradouro().getDescricao()+ " " +
//                                         pesEndereco.getEndereco().getDescricaoEndereco().getDescricao()+", " +pesEndereco.getNumero()+" " +pesEndereco.getEndereco().getBairro().getDescricao()+","+
//                                         strCompl+pesEndereco.getEndereco().getCidade().getCidade() +" - "+ pesEndereco.getEndereco().getCidade().getUf()+ " - "+AnaliseString.mascaraCep(pesEndereco.getEndereco().getCep());
//            }else{
//                strEnderecoEmpresa = " NENHUM ";
//            }
//       }else{
//           pesEndereco = new PessoaEndereco();
//           strEnderecoEmpresa = "";
//       }
//        return strEnderecoEmpresa;
//    }
//
//    public void setStrEnderecoEmpresa(String strEnderecoEmpresa) {
//        this.strEnderecoEmpresa = strEnderecoEmpresa;
//    }
//
//    public MatriculaSocios getMatriculaSocios() {
//        PessoaEnderecoDB db = new PessoaEnderecoDBToplink();
//        GrupoCidadesDB dbGCids = new GrupoCidadesDBToplink();
//        List<GrupoCidades> cids = dbGCids.pesquisaTodos();
//        if (socios.getId() == -1 && matriculaSocios.getId() == -1){
//            matriculaSocios.setEmissao(DataHoje.data());
//            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cidadePesquisa") != null){
//                matriculaSocios.setCidade((Cidade) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cidadePesquisa"));
//            }else if (servicoPessoa.titularEndereco != null && servicoPessoa.titularEndereco.getId() != -1){
//                for(int i = 0; i < cids.size(); i++){
//                    if(cids.get(i).getCidade().getId() == servicoPessoa.titularEndereco.getEndereco().getCidade().getId()){
//                        matriculaSocios.setCidade(servicoPessoa.titularEndereco.getEndereco().getCidade());
//                        return matriculaSocios;
//                    }
//                }
//                matriculaSocios.setCidade(((PessoaEndereco)db.pesquisaEndPorPessoaTipo(1, 3)).getEndereco().getCidade());
//            }else{
//                matriculaSocios.setCidade( ((PessoaEndereco)db.pesquisaEndPorPessoaTipo(1, 3)).getEndereco().getCidade() );
//            }
//        }
//        if ((Fisica)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa") != null){
//            if (matriculaSocios.getTitular().getId() != ((Fisica)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa")).getPessoa().getId()
//                    ){//&& servicoPessoa.adicionaTitular){
//                        editarGenerico(((Fisica)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa")).getPessoa());
//            }
//        }
//        return matriculaSocios;
//    }
//
//    public void setMatriculaSocios(MatriculaSocios matriculaSocios) {
//        this.matriculaSocios = matriculaSocios;
//    }
//
//    public String getMsgConfirmaTela() {
//        return msgConfirmaTela;
//    }
//
//    public void setMsgConfirmaTela(String msgConfirmaTela) {
//        this.msgConfirmaTela = msgConfirmaTela;
//    }
//
//    public String getDescPesquisa() {
//        return descPesquisa;
//    }
//
//    public void setDescPesquisa(String descPesquisa) {
//        this.descPesquisa = descPesquisa;
//    }
//
//    public String getPorPesquisa() {
//        return porPesquisa;
//    }
//
//    public void setPorPesquisa(String porPesquisa) {
//        this.porPesquisa = porPesquisa;
//    }
//
//    public String getStatusPesquisa() {
//        return statusPesquisa;
//    }
//
//    public void setStatusPesquisa(String statusPesquisa) {
//        this.statusPesquisa = statusPesquisa;
//    }
//
//    public boolean isDesabilitaImpressao() {
//        if (socios.getId() != -1 && matriculaSocios.getId() != -1)
//            desabilitaImpressao = false;
//        return desabilitaImpressao;
//    }
//
//    public void setDesabilitaImpressao(boolean desabilitaImpressao) {
//        this.desabilitaImpressao = desabilitaImpressao;
//    }
//
//    public boolean isImprimirVerso() {
//        return imprimirVerso;
//    }
//
//    public void setImprimirVerso(boolean imprimirVerso) {
//        this.imprimirVerso = imprimirVerso;
//    }
//
//    public String getTipoDestinario() {
//        return tipoDestinario;
//    }
//
//    public void setTipoDestinario(String tipoDestinario) {
//        this.tipoDestinario = tipoDestinario;
//    }
//
//    public int getIdInativacao() {
//        return idInativacao;
//    }
//
//    public void setIdInativacao(int idInativacao) {
//        this.idInativacao = idInativacao;
//    }
//
//    public String getStatusSocio() {
//        if (socios.getId() == -1){
//            statusSocio = "STATUS";
//            strInativacao = "<< NOVO SÓCIO >>";
//        }else{
//            if (matriculaSocios.getMotivoInativacao() != null){
//                statusSocio = "INATIVO";
//                strInativacao = matriculaSocios.getMotivoInativacao().getNome() +" - "+ matriculaSocios.getInativo();
//            }else{
//                statusSocio = "ATIVO";
//                strInativacao = "";
//            }
//        }
//        return statusSocio;
//    }
//
//    public void setStatusSocio(String statusSocio) {
//        this.statusSocio = statusSocio;
//    }
//
//    public String getStatusCor() {
//        if (socios.getId() == -1)    {
//            statusCor = "orange";
//        }else{
//            if (matriculaSocios.getMotivoInativacao() != null){
//                statusCor = "red";
//            }else{
//                statusCor = "blue";
//            }
//        }
//        return statusCor;
//    }
//
//    public void setStatusCor(String statusCor) {
//        this.statusCor = statusCor;
//    }
//
//    public String getDataInativacao() {
//        return dataInativacao;
//    }
//
//    public void setDataInativacao(String dataInativacao) {
//        this.dataInativacao = dataInativacao;
//    }
//
//    public String getDataReativacao() {
//        return dataReativacao;
//    }
//
//    public void setDataReativacao(String dataReativacao) {
//        this.dataReativacao = dataReativacao;
//    }
//
//    public String getStatusModal() {
//        if (socios.getId() == -1)    {
//            statusModal = "panelInativar";
//        }else{
//            if (matriculaSocios.getMotivoInativacao() != null){
//                statusModal = "panelReativar";
//            }else{
//                statusModal = "panelInativar";
//            }
//        }
//        return statusModal;
//    }
//
//    public void setStatusModal(String statusModal) {
//        this.statusModal = statusModal;
//    }
//
//    public boolean isDesabilitaCategoria() {
//        // ROTINA QUE DESABILITA SÓ FOI MODIFICADA AQUI...
//        // SE APAGAR FUNCIONARÁ NORMALMENTE DE NOVO
//        if (socios.getId() != -1)
//            desabilitaCategoria = true;
//        else
//            desabilitaCategoria = false;
//        return desabilitaCategoria;
//    }
//
//    public void setDesabilitaCategoria(boolean desabilitaCategoria) {
//        this.desabilitaCategoria = desabilitaCategoria;
//    }
//
//    public PessoaEmpresa getPessoaEmpresa() {
//        if ( servicoPessoa.titular.getId() != pessoaEmpresa.getFisica().getPessoa().getId()){
//            PessoaEmpresaDB dbEP = new PessoaEmpresaDBToplink();
//            pessoaEmpresa = dbEP.pesquisaPessoaEmpresaPorFisica(servicoPessoa.titular.getId());
//        }
//        return pessoaEmpresa;
//    }
//
//    public void setPessoaEmpresa(PessoaEmpresa pessoaEmpresa) {
//        this.pessoaEmpresa = pessoaEmpresa;
//    }
//
//    public String getStrInativacao() {
//        return strInativacao;
//    }
//
//    public void setStrInativacao(String strInativacao) {
//        this.strInativacao = strInativacao;
//    }
//
//    public Fisica getDependente() {
//        return dependente;
//    }
//
//    public void setDependente(Fisica dependente) {
//        this.dependente = dependente;
//    }
//}