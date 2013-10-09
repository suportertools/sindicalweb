package br.com.rtools.seguranca.beans;

import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.controleUsuario.controleUsuarioJSFBean;
import br.com.rtools.seguranca.db.PesquisaLogDB;
import br.com.rtools.seguranca.db.PesquisaLogDBTopLink;
import br.com.rtools.seguranca.db.RotinaDB;
import br.com.rtools.seguranca.db.RotinaDBToplink;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.swing.JOptionPane;

public class PesquisaLogJSFBean {

    private String dataInicioString = "";
    private String dataFinalString = "";
    private String descricao = "";
    private String diretorio = "";
    private String porPesquisa = "todos";
    private List listaDiretorio = new ArrayList();
    private int idRotina = 0;
    private boolean tipoPesquisa = false;
    private Rotina rotina = new Rotina();
    private List list = new ArrayList();
    private boolean desabilitaRotinas = true;
    private boolean desabilitaDescricao = true;

    public PesquisaLogJSFBean() {
        actTipoPesquisa();
        diretorio = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + controleUsuarioJSFBean.getCliente() + "/logs/");
    }

    public String atualizar() {
        list.clear();
        desabilitaRotinas = true;
        desabilitaDescricao = true;
        porPesquisa = "todos";
        descricao = "";
        return "pesquisaLog";
    }

    public String pesquisar() {
        getListaDiretorio();
        return null;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List getListaDiretorio() {
        getList();
        listaDiretorio.clear();
        int dataInicioInt = 0;
        int dataFinalInt = 0;
        if (!dataInicioString.equals("")) {
            dataInicioInt = DataHoje.converteDataParaInteger(dataInicioString);
        }
        if (!dataFinalString.equals("")) {
            dataFinalInt = DataHoje.converteDataParaInteger(dataFinalString);
        }
        String descPesquisa = "";
        if (porPesquisa.equals("nomeArquivo")) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            Rotina r = (Rotina) salvarAcumuladoDB.pesquisaCodigo(Integer.parseInt(getListaRotinas().get(idRotina).getDescription()), "Rotina");
            descPesquisa = r.getPagina();
            desabilitaDescricao = false;
        } else if (porPesquisa.equals("descricao")) {
            descPesquisa = descricao;
            desabilitaDescricao = false;
        } else if (porPesquisa.equals("todos")) {
            desabilitaDescricao = true;
        }
        for (int i = 0; i < list.size(); i++) {
            DataObject dataObject = (DataObject) list.get(i);
            int dataFile = DataHoje.converteDataParaInteger(dataObject.getArgumento2().toString());
            if (!dataInicioString.equals("") && !dataFinalString.equals("")) {
                if (dataFinalInt >= dataInicioInt) {
                    if (dataFile <= dataFinalInt && dataFile >= dataInicioInt) {
                        if (!descPesquisa.equals("")) {
                            if (porPesquisa.equals("nomeArquivo")) {
                                if (("\"/Sindical/" + dataObject.getArgumento4().toString() + "\"").equals(descPesquisa)) {
                                    listaDiretorio.add(dataObject);
                                }
                            } else if (porPesquisa.equals("descricao")) {
                                if (((dataObject.getArgumento3().toString()).toUpperCase()).equals(descPesquisa.toUpperCase()) || ((dataObject.getArgumento5().toString()).toUpperCase()).equals(descPesquisa.toUpperCase())) {
                                    listaDiretorio.add(dataObject);
                                }
                            } else if (porPesquisa.equals("todos")) {
                                listaDiretorio.add(dataObject);
                            }
                        } else {
                            listaDiretorio.add(dataObject);
                        }
                    }
                }
            } else if (!dataInicioString.equals("")) {
                if (dataFile == dataInicioInt) {
                    if (!descPesquisa.equals("")) {
                        if (porPesquisa.equals("nomeArquivo")) {
                            if (("\"/Sindical/" + dataObject.getArgumento4().toString() + "\"").equals(descPesquisa)) {
                                listaDiretorio.add(dataObject);
                            }
                        } else if (porPesquisa.equals("descricao")) {
                            if (((dataObject.getArgumento3().toString()).toUpperCase()).equals(descPesquisa.toUpperCase()) || ((dataObject.getArgumento5().toString()).toUpperCase()).equals(descPesquisa.toUpperCase())) {
                                listaDiretorio.add(dataObject);
                            }
                        } else if (porPesquisa.equals("todos")) {
                            listaDiretorio.add(dataObject);
                        }
                    } else {
                        listaDiretorio.add(dataObject);
                    }
                }

            } else {
                if (porPesquisa.equals("nomeArquivo")) {
                    if (("\"/Sindical/" + dataObject.getArgumento4().toString() + "\"").equals(descPesquisa)) {
                        listaDiretorio.add(dataObject);
                    }
                } else if (porPesquisa.equals("descricao")) {
                    if (((dataObject.getArgumento3().toString()).toUpperCase()).equals(descPesquisa.toUpperCase()) || ((dataObject.getArgumento5().toString()).toUpperCase()).equals(descPesquisa.toUpperCase())) {
                        listaDiretorio.add(dataObject);
                    }
                } else if (porPesquisa.equals("todos")) {
                    listaDiretorio.add(dataObject);
                }
            }

        }
        return listaDiretorio;
    }

    public String adicionaArquivo(String caminho, String dirName) {
        File dir = new File(caminho);

        if (dir.isDirectory()) {

            PesquisaLogDB db = new PesquisaLogDBTopLink();

            File[] sub = dir.listFiles();

            for (int i = 0; i < sub.length; i++) {

                sub[i].getName();
                String[] arrayString = sub[i].getName().split("_");
                String descricaoFile = arrayString[1];
                String descricaoPagina = descricaoFile + ".jsf";
                String[] arrayDateFileString = arrayString[2].split(".txt");
                String dateFile = arrayDateFileString[0];

                DataObject ob = new DataObject(link(dirName, sub[i].getName()), sub[i].getName(), converteData(dateFile), null, null, null);

                if (!getDescricao().equals("") && !getDescricao().toUpperCase().equals(("indexLogin").toUpperCase()) && !getDescricao().toUpperCase().equals(("simples").toUpperCase())) {
                    if (db.verificaRotina(descricaoFile)) {
                        listaDiretorio.add(ob);
                    }
                } else if (porPesquisa.equals("nomeArquivo")) {
                    SalvarAcumuladoDB acumuladoDB = new SalvarAcumuladoDBToplink();
                    rotina = (Rotina) acumuladoDB.pesquisaCodigo(Integer.parseInt(getListaRotinas().get(idRotina).getDescription()), "Rotina");
                    if (("/Sindical/" + descricaoPagina).equals(rotina.getPagina())) {
                        listaDiretorio.add(ob);
                    }
                } else if (getDescricao().toUpperCase().equals(("indexLogin").toUpperCase()) || getDescricao().toUpperCase().equals(("index").toUpperCase()) || getDescricao().toUpperCase().equals(("login").toUpperCase())) {
                    if (descricaoFile.equals("indexLogin")) {
                        listaDiretorio.add(ob);
                    }
                } else if (getDescricao().toUpperCase().equals(("simples").toUpperCase())) {
                    if (descricaoFile.equals("simples")) {
                        listaDiretorio.add(ob);
                    }
                } else if (dataInicioString.equals(dataFinalString)) {
                    if (dataInicioString.equals(converteData(dateFile)) || dataFinalString.equals(converteData(dateFile))) {
                        listaDiretorio.add(ob);
                    }
                } else if (getDescricao().equals("") && (!dataInicioString.equals("") || !dataFinalString.equals(""))) {
                    if (dataInicioString.equals(converteData(dateFile)) || dataFinalString.equals(converteData(dateFile)) || dirName.equals(dateFile)) {
                        listaDiretorio.add(ob);
                    }
                }
            }
        }
        return null;

    }

    public String converteData(String data) {
        String novaData = data.substring(8, 10) + "/" + data.substring(5, 7) + "/" + data.substring(0, 4);
        return novaData;
    }

    public String link(String dirName, String filename) {
        Registro reg = (Registro) new SalvarAcumuladoDBToplink().pesquisaCodigo(1, "Registro");
        String url = reg.getUrlPath() + "/Sindical/Cliente/" + controleUsuarioJSFBean.getCliente() + "/logs/" + dirName + "/" + filename;
        return url;
    }

    public List lDiretorios(String caminho) {
        File dir = new File(caminho);
        if (dir.isFile()) {
            getList().add(dir.getPath());
        }
        return getList();
    }

    public void setListaDiretorio(List listaDiretorio) {
        this.listaDiretorio = listaDiretorio;
    }

    public String getDataInicioString() {
        return dataInicioString;
    }

    public void setDataInicioString(String dataInicioString) {
        this.dataInicioString = dataInicioString;
    }

    public String getDataFinalString() {
        return dataFinalString;
    }

    public void setDataFinalString(String dataFinalString) {
        this.dataFinalString = dataFinalString;
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public String pesquisarx() {
        buscarArquivoPorNome(getDescricao(), "/Sindical/Cliente/" + controleUsuarioJSFBean.getCliente() + "/logs/");
        return null;
    }

    public ArrayList buscarArquivoPorNome(String palavra, String caminhoInicial) {
        ArrayList lista = new ArrayList();
        try {
            File arquivo = new File(caminhoInicial);
            lista = buscar(arquivo, palavra, lista);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Caminho Inválido");
        }
        return lista;
    }

    public ArrayList buscar(File arquivo, String palavra, ArrayList lista) {
        if (arquivo.isDirectory()) {
            File[] subPastas = arquivo.listFiles();
            for (int i = 0; i < subPastas.length; i++) {
                lista = buscar(subPastas[i], palavra, lista);
                if (arquivo.getName().equalsIgnoreCase(palavra)) {
                    lista.add(arquivo.getAbsolutePath());
                } else if (arquivo.getName().indexOf(palavra) > -1) {
                    lista.add(arquivo.getAbsolutePath());
                }
            }
        } else if (arquivo.getName().equalsIgnoreCase(palavra)) {
            lista.add(arquivo.getAbsolutePath());
        } else if (arquivo.getName().indexOf(palavra) > -1) {
            lista.add(arquivo.getAbsolutePath());
        }
        return lista;
    }
//    
//    public void ListarFicheiros(String filename) throws IOException{  
//        List<File> arquivos = new ArrayList<File>();   
//        File dir = new File(filename);  
//        if (dir.isDirectory()) {    
//            File[] sub = dir.listFiles();    
//            for (File f : sub) {
//            if (f.isDirectory()) {    
//                    //recursividade aqui  
//                    String aaaa = f.getPath();
//                    arquivos.addAll(ListarFicheiros(aaaa));  
//                } else {    
//                    arquivos.add (f);    
//                }  
//            }  
//        }  
//        ObjectOutputStream obj = new ObjectOutputStream (s.getOutputStream());  
//        obj.writeObject(arquivos);   
//        obj.flush();  
//    }

    public List<SelectItem> getListaRotinas() {
        List<SelectItem> listaRotinas = new Vector<SelectItem>();
        if (listaRotinas.isEmpty()) {
            RotinaDB rotinaDB = new RotinaDBToplink();
            List list = rotinaDB.pesquisaTodosOrdenado();
            int i = 0;
            while (i < list.size()) {
                listaRotinas.add(new SelectItem(new Integer(i),
                        (String) ((Rotina) list.get(i)).getRotina(),
                        Integer.toString(((Rotina) list.get(i)).getId())));
                i++;
            }
        }
        return listaRotinas;
    }

    public int getIdRotina() {
        return idRotina;
    }

    public void setIdRotina(int idRotina) {
        this.idRotina = idRotina;
    }

    public boolean isTipoPesquisa() {
        return tipoPesquisa;
    }

    public void setTipoPesquisa(boolean tipoPesquisa) {
        this.tipoPesquisa = tipoPesquisa;
    }

    public String actTipoPesquisa() {
        if (porPesquisa.equals("todos")) {
            desabilitaDescricao = true;
            desabilitaRotinas = true;
            descricao = "";
            diretorio = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + controleUsuarioJSFBean.getCliente() + "/logs/");
        } else if (porPesquisa.equals("nomeArquivo")) {
            desabilitaDescricao = false;
            desabilitaRotinas = true;
            tipoPesquisa = false;
            descricao = "";
        } else if (porPesquisa.equals("descricao")) {
            desabilitaDescricao = true;
            desabilitaRotinas = false;
            tipoPesquisa = true;
        }
        return "pesquisaLog";
    }

    public Rotina getRotina() {
        return rotina;
    }

    public void setRotina(Rotina rotina) {
        this.rotina = rotina;
    }

    public List getList() {
        if (list.isEmpty()) {
            File dir = new File(diretorio);
            if (dir.isDirectory()) {
                File[] sub = dir.listFiles();
                for (int i = 0; i < sub.length; i++) {
                    File subdir = new File(diretorio + "/" + sub[i].getName());
                    String dataString = converteData(sub[i].getName());
                    File[] subfile = subdir.listFiles();
                    for (int y = 0; y < subfile.length; y++) {
                        String[] arrayString = subfile[y].getName().split("_");
                        String descricaoFile = arrayString[1].toString();
                        String descricaoPagina = descricaoFile + ".jsf";
                        RotinaDB rotinaDB = new RotinaDBToplink();
                        Rotina rotina = new Rotina();
                        rotina = rotinaDB.pesquisaPaginaRotina(descricaoPagina);
                        DataObject ob = null;
                        if (rotina.getId() != -1) {
                            ob = new DataObject(link(sub[i].getName(), subfile[y].getName()), sub[i].getName(), dataString, descricaoFile, descricaoPagina, rotina.getRotina());
                        } else {
                            ob = new DataObject(link(sub[i].getName(), subfile[y].getName()), sub[i].getName(), dataString, descricaoFile, descricaoPagina, descricaoFile);
                        }
                        list.add(ob);
                    }
                }
            }
        }
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public boolean isDesabilitaDescricao() {
        return desabilitaDescricao;
    }

    public void setDesabilitaDescricao(boolean desabilitaDescricao) {
        this.desabilitaDescricao = desabilitaDescricao;
    }
//    public String getOrdernarPorData() {
//        if(ordernarPorData.equals("ASC")){
//            Collections.sort (listaDiretorio, new Comparator<DataObject> () {    
//                public int compare (DataObject d1, DataObject d2) {    
//                    return d1.getArgumento1().toString().compareTo(d2.getArgumento1().toString());    
//                }    
//            });       
//            ordernarPorData = "DESC";
//        }else{
//            Collections.sort(listaDiretorio, new Comparator<DataObject> () {    
//                public int compare (DataObject d1, DataObject d2) {    
//                    return d1.getArgumento1().toString().compareTo(d2.getArgumento1().toString());    
//                }    
//            });
//            ordernarPorData = "ASC";            
//        }
//        return ordernarPorData;
//    }
//
//    public void setOrdernarPorData(String ordernarPorData) {
//        this.ordernarPorData = ordernarPorData;
//    }

    public boolean isDesabilitaRotinas() {
        return desabilitaRotinas;
    }

    public void setDesabilitaRotinas(boolean desabilitaRotinas) {
        this.desabilitaRotinas = desabilitaRotinas;
    }
}
