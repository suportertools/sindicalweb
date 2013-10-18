package br.com.rtools.associativo.beans;

import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.Download;
import br.com.rtools.utilitarios.GenericaQuery;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

@ManagedBean
@SessionScoped
public class ContratoBean implements Serializable {

    private File files;
    private List listaArquivo;
    private List itens;
    private List<String> listaAtributo;
    private List<String> listaConteudo;
    private String nomeArquivo;

    public ContratoBean() {
        files = null;
        listaArquivo = new ArrayList<GenericaQuery>();
        itens = new ArrayList();
        listaAtributo = new ArrayList<String>();
        listaConteudo = new ArrayList<String>();
    }

    public List getListaArquivo() {
        return listaArquivo;
    }

//    public synchronized void upload(UploadEvent event){
//        this.getItens().add(event.getUploadItem());
//        UploadItem item = event.getUploadItem();
//        try{
//            File fl = new File(
//                        ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Arquivos/ArquivoContrato/")
//                        +"/" +item.getFileName()
//                    );
//            nomeArquivo = item.getFileName();
//            FileInputStream in = new FileInputStream(item.getFile());
//            FileOutputStream out = new FileOutputStream(fl.getPath());
//            byte[] buf = new byte[(int)item.getFile().length()];
//            int count;
//            while ((count = in.read(buf)) >= 0) {
//                out.write(buf, 0, count);
//            }
//            in.close();
//            out.flush();
//            out.close();
//        }catch(Exception e){
//            System.out.println(e);
//        }
//    }
    public String download() {
        //GenericaQuery genericaQuery = (GenericaQuery) htmlDataTable.getRowData();
        String arquivoDownload = "Imprimir_Contrato.xml";
        String diretorio = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/ArquivoContrato/");

        listaAtributo.add("@criatura@");
        listaAtributo.add("@característica@");
        listaConteudo.add("Vampiro");
        listaConteudo.add("Doidão");
        this.gerarArquivo(nomeArquivo, arquivoDownload, diretorio);
        Download download = new Download(
                arquivoDownload,
                diretorio,
                "xml",
                FacesContext.getCurrentInstance());
        download.baixar();
        return null;
    }

    public void gerarArquivo(String arquivoEntrada, String arquivoSaida, String diretorio) {
        if (listaAtributo.size() == listaConteudo.size()) {
            try {
                BufferedReader in = new BufferedReader(new FileReader(diretorio + "/" + arquivoEntrada));
                BufferedWriter out = new BufferedWriter(new FileWriter(diretorio + "/" + arquivoSaida));
                String linha;
                while (in.ready()) {
                    linha = in.readLine();
                    for (int i = 0; i < listaAtributo.size(); i++) {
                        linha = linha.replace(listaAtributo.get(i), listaConteudo.get(i));
                    }
                    out.append(linha);
                }
                in.close();
                out.close();
            } catch (IOException e) {
                e.getMessage();
            }
        }
    }

    public void preencherLista() {
        listaArquivo.clear();
        GenericaQuery generica = new GenericaQuery();
        FacesContext context = FacesContext.getCurrentInstance();
        setFiles(new File(((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/ArquivoContrato/")));
        File listFile[] = getFiles().listFiles();
        int i = 0;
        while (i < listFile.length) {
            if (listFile[i].getName().length() > 9) {
                if (!listFile[i].getName().substring(0, 9).equals("Imprimir_")) {
                    continue;
                }
            }
            generica = new GenericaQuery(
                    listFile[i].getName(),
                    "",
                    "",
                    "",
                    "",
                    "");
            listaArquivo.add(generica);
            i++;
        }
    }

    public String imprimirDocumentos() {

        return null;
    }

    public void refreshForm() {
    }

    public List getItens() {
        return itens;
    }

    public void setItens(List itens) {
        this.itens = itens;
    }

    public void setListaArquivo(List listaArquivo) {
        this.listaArquivo = listaArquivo;
    }

    public File getFiles() {
        return files;
    }

    public void setFiles(File files) {
        this.files = files;
    }

    public List<String> getListaAtributo() {
        return listaAtributo;
    }

    public void setListaAtributo(List<String> listaAtributo) {
        this.listaAtributo = listaAtributo;
    }

    public List<String> getListaConteudo() {
        return listaConteudo;
    }

    public void setListaConteudo(List<String> listaConteudo) {
        this.listaConteudo = listaConteudo;
    }
}