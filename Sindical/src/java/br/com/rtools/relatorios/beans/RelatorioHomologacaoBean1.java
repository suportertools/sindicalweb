//package br.com.rtools.relatorios.beans;
//
//import br.com.rtools.impressao.ParametroHomologacao;
//import br.com.rtools.pessoa.Filial;
//import br.com.rtools.pessoa.Fisica;
//import br.com.rtools.pessoa.Juridica;
//import br.com.rtools.pessoa.Pessoa;
//import br.com.rtools.relatorios.Relatorios;
//import br.com.rtools.relatorios.db.RelatorioDao;
//import br.com.rtools.relatorios.db.RelatorioDaoToplink;
//import br.com.rtools.relatorios.db.RelatorioHomologacaoDB;
//import br.com.rtools.relatorios.db.RelatorioHomologacaoDBToplink;
//import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
//import br.com.rtools.utilitarios.Dao;
//import br.com.rtools.utilitarios.DaoInterface;
//import br.com.rtools.utilitarios.DataHoje;
//import br.com.rtools.utilitarios.DataObject;
//import br.com.rtools.utilitarios.Download;
//import br.com.rtools.utilitarios.SalvaArquivos;
//import java.io.File;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.Vector;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.SessionScoped;
//import javax.faces.context.FacesContext;
//import javax.faces.model.SelectItem;
//import javax.servlet.ServletContext;
//import net.sf.jasperreports.engine.JasperExportManager;
//import net.sf.jasperreports.engine.JasperFillManager;
//import net.sf.jasperreports.engine.JasperPrint;
//import net.sf.jasperreports.engine.JasperReport;
//import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
//import net.sf.jasperreports.engine.util.JRLoader;
//
//@ManagedBean
//@SessionScoped
//public class RelatorioHomologacaoBean1 implements Serializable {
//
//    private int idRelatorio = 0;
//    private int indexFilial = 0;
//    private String selectAccordion = "simples";
//    private List<DataObject> listaMenuRHomologacao = new ArrayList();
//    private boolean booEmpresa = false;
//    private boolean booFuncionario = false;
//    private boolean booData = false;
//    private boolean booHomologador = false;
//    private boolean booFilial = false;
//    private Juridica juridica = new Juridica();
//    private Fisica fisica = new Fisica();
//    private Pessoa usuario = new Pessoa();
//    private String datai = "";
//    private String dataf = "";
//    private String tipoOrdem = "data";
//    private List<SelectItem> listaFiliais = new ArrayList();
//    
//
//    public String visualizarRelatorio() {
//        RelatorioDao db = new RelatorioDaoToplink();
//        RelatorioHomologacaoDB dbh = new RelatorioHomologacaoDBToplink();
//        Relatorios relatorios = db.pesquisaRelatorios(Integer.parseInt(getListaTipoRelatorios().get(idRelatorio).getDescription()));
//
//        //String di = String.valueOf(datai), df = String.valueOf(dataf);
//        if (booData) {
//        }
//
//        int id_empresa = -1, id_funcionario = -1, id_homologador = -1, id_filial = -1;
//        if (booEmpresa) {
//            if (juridica.getId() != -1) {
//                id_empresa = juridica.getId();
//            }
//        }
//
//        if (booFuncionario) {
//            if (fisica.getId() != -1) {
//                id_funcionario = fisica.getId();
//            }
//        }
//
//        if (booHomologador) {
//            if (usuario.getId() != -1) {
//                id_homologador = usuario.getId();
//            }
//        }
//        
//        if (booFilial) {
//            id_filial = Integer.valueOf(getListaFiliais().get(indexFilial).getDescription());
//        }
//
//        List<Vector> result = dbh.pesquisaHomologacao(relatorios, booEmpresa, id_empresa, booFuncionario, id_funcionario, booData, datai, dataf, booHomologador, id_homologador, id_filial, tipoOrdem);
//        Collection lista = new ArrayList<ParametroHomologacao>();
//
//        for (int i = 0; i < result.size(); i++) {
//            lista.add(new ParametroHomologacao(datai, // DATA INICIAL
//                    dataf, // DATA FINAL
//                    getConverteNullString(result.get(i).get(2)), // DATA 
//                    getConverteNullString(result.get(i).get(3)), // HORA
//                    getConverteNullString(result.get(i).get(4)), // CNPJ
//                    getConverteNullString(result.get(i).get(5)), // EMPRESA
//                    getConverteNullString(result.get(i).get(6)), // FUNCIONARIO
//                    getConverteNullString(result.get(i).get(7)), // CONTATO
//                    getConverteNullString(result.get(i).get(8)), // TELEFONE
//                    getConverteNullString(result.get(i).get(9)), // HOMOLOGADOR
//                    getConverteNullString(result.get(i).get(10)) // OBS
//            ));
//        }
//
//        try {
//            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
//            JasperReport jasper = (JasperReport) JRLoader.loadObject(new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(relatorios.getJasper())));
//            JasperPrint print = JasperFillManager.fillReport(jasper, null, dtSource);
//
//            byte[] arquivo = new byte[0];
//            arquivo = JasperExportManager.exportReportToPdf(print);
//
//            String nomeDownload = relatorios.getNome() + "_" + DataHoje.horaMinuto().replace(":", "") + ".pdf";
//            SalvaArquivos sa = new SalvaArquivos(arquivo, nomeDownload, false);
//
//            String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/relatorios");
//            sa.salvaNaPasta(pathPasta);
//
//            Download download = new Download(nomeDownload, pathPasta, "application/pdf", FacesContext.getCurrentInstance());
//            download.baixar();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public List<SelectItem> getListaFiliais() {
//        if (listaFiliais.isEmpty()) {
//            DaoInterface di = new Dao();
//            List<Filial> list = (List<Filial>) di.list(new Filial(), true);
//            for (int i = 0; i < list.size(); i++) {
//                listaFiliais.add(new SelectItem(i,
//                        list.get(i).getFilial().getPessoa().getDocumento() + " / " + list.get(i).getFilial().getPessoa().getNome(),
//                        Integer.toString(list.get(i).getId())));
//            }
//        }
//        return listaFiliais;
//    }
//
//    
//    public List<SelectItem> getListaTipoRelatorios() {
//        List<SelectItem> relatorios = new Vector<SelectItem>();
//        int i = 0;
//        RelatorioDao db = new RelatorioDaoToplink();
//        List select = db.pesquisaTipoRelatorio(177);
//        while (i < select.size()) {
//            relatorios.add(new SelectItem(new Integer(i),
//                    (String) ((Relatorios) select.get(i)).getNome(),
//                    Integer.toString(((Relatorios) select.get(i)).getId())));
//            i++;
//        }
//        return relatorios;
//    }
//
//    public String getConverteNullString(Object object) {
//        if (object == null) {
//            return "";
//        } else {
//            return String.valueOf(object);
//        }
//    }
//
//    public int getIdRelatorio() {
//        return idRelatorio;
//    }
//
//    public void setIdRelatorio(int idRelatorio) {
//        this.idRelatorio = idRelatorio;
//    }
//
//    public String getSelectAccordion() {
//        return selectAccordion;
//    }
//
//    public void setSelectAccordion(String selectAccordion) {
//        this.selectAccordion = selectAccordion;
//    }
//
//    public List<DataObject> getListaMenuRHomologacao() {
//        if (listaMenuRHomologacao.isEmpty()) {
//            listaMenuRHomologacao.add(new DataObject("* Nome Empresa ", "Editar", null, null, null, null));
//            listaMenuRHomologacao.add(new DataObject("* Nome Funcionário ", "Editar", null, null, null, null));
//            listaMenuRHomologacao.add(new DataObject("* Data Inicial/Final ", "Editar", null, null, null, null));
//            listaMenuRHomologacao.add(new DataObject("* Homologador ", "Editar", null, null, null, null));
//            listaMenuRHomologacao.add(new DataObject("* Filial ", "Editar", null, null, null, null));
//        }
//        return listaMenuRHomologacao;
//    }
//
//    public void setListaMenuRHomologacao(List<DataObject> listaMenuRHomologacao) {
//        this.listaMenuRHomologacao = listaMenuRHomologacao;
//    }
//
//    public String editarOpcao(int index) {
//        if (listaMenuRHomologacao.get(index).getArgumento1().equals("Remover")) {
//            listaMenuRHomologacao.get(index).setArgumento1("Editar");
//        } else {
//            listaMenuRHomologacao.get(index).setArgumento1("Remover");
//        }
//
//        if (index == 0) {
//            if (booEmpresa) {
//                booEmpresa = false;
//            } else {
//                booEmpresa = true;
//            }
//        } else if (index == 1) {
//            if (booFuncionario) {
//                booFuncionario = false;
//            } else {
//                booFuncionario = true;
//            }
//        } else if (index == 2) {
//            if (booData) {
//                booData = false;
//            } else {
//                booData = true;
//            }
//        } else if (index == 3) {
//            if (booHomologador) {
//                booHomologador = false;
//            } else {
//                booHomologador = true;
//            }
//        } else if (index == 4) {
//            if (booFilial) {
//                booFilial = false;
//            } else {
//                booFilial = true;
//            }
//        }
//        return "relatorioHomologacao";
//    }
//
//    public boolean isBooEmpresa() {
//        return booEmpresa;
//    }
//
//    public void setBooEmpresa(boolean booEmpresa) {
//        this.booEmpresa = booEmpresa;
//    }
//
//    public boolean isBooFuncionario() {
//        return booFuncionario;
//    }
//
//    public void setBooFuncionario(boolean booFuncionario) {
//        this.booFuncionario = booFuncionario;
//    }
//
//    public boolean isBooData() {
//        return booData;
//    }
//
//    public void setBooData(boolean booData) {
//        this.booData = booData;
//    }
//
//    public boolean isBooHomologador() {
//        return booHomologador;
//    }
//
//    public void setBooHomologador(boolean booHomologador) {
//        this.booHomologador = booHomologador;
//    }
//
//    public Juridica getJuridica() {
//        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa") != null) {
//            juridica = (Juridica) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa");
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
//        }
//        return juridica;
//    }
//
//    public void setJuridica(Juridica juridica) {
//        this.juridica = juridica;
//    }
//
//    public Fisica getFisica() {
//        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa") != null) {
//            fisica = (Fisica) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa");
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
//        }
//        return fisica;
//    }
//
//    public void setFisica(Fisica fisica) {
//        this.fisica = fisica;
//    }
//
//    public String getDatai() {
//        return datai;
//    }
//
//    public void setDatai(String datai) {
//        this.datai = datai;
//    }
//
//    public String getDataf() {
//        return dataf;
//    }
//
//    public void setDataf(String dataf) {
//        this.dataf = dataf;
//    }
//
//    public Pessoa getUsuario() {
//        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa") != null) {
//            usuario = (Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa");
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
//        }
//        return usuario;
//    }
//
//    public void setUsuario(Pessoa usuario) {
//        this.usuario = usuario;
//    }
//
//    public String getTipoOrdem() {
//        return tipoOrdem;
//    }
//
//    public void setTipoOrdem(String tipoOrdem) {
//        this.tipoOrdem = tipoOrdem;
//    }
//
//    public int getIndexFilial() {
//        return indexFilial;
//    }
//
//    public void setIndexFilial(int indexFilial) {
//        this.indexFilial = indexFilial;
//    }
//
//    public boolean isBooFilial() {
//        return booFilial;
//    }
//
//    public void setBooFilial(boolean booFilial) {
//        this.booFilial = booFilial;
//    }
//}
