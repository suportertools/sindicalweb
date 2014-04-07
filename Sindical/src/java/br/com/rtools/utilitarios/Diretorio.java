package br.com.rtools.utilitarios;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

public class Diretorio {

    private final static String cliente = "";

    public static boolean criar(String diretorio) {
        diretorio = "/Cliente/" + getCliente() + "/" + diretorio;
        try {
            String s[] = diretorio.split("/");
            boolean err = false;
            String caminhoContac = "";
            int b = 0;
            String caminho = "";
            for (String item : s) {
                if (!item.equals("")) {
                    if (b == 0) {
                        caminhoContac = item;
                        caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/"+caminhoContac);
                    } else {
                        caminhoContac = "/" + caminhoContac + "/" + item;
                        caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(caminhoContac);
                    }
                    if (!new File(caminho).exists()) {
                        File file = new File(caminho);
                        if (!file.mkdir()) {
                            err = false;
                            break;
                        }
                    }
                    b++;
                }
            }
            if (!err) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean remover(String diretorio) {
        if (new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/" + diretorio)).exists()) {
            File file = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/" + diretorio));
            if (file.delete()) {
                return true;
            }
        }
        return false;
    }

    public static List listaArquivos(String diretorio) {
        List listaArquivos = new ArrayList();
        String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/" + diretorio);
        try {
            File files = new File(caminho);
            if (!files.exists()) {
                return new ArrayList();
            }
            File listFile[] = files.listFiles();
            int numArq = listFile.length;
            int i = 0;
            while (i < numArq) {
                listaArquivos.add(new DataObject(listFile[i], listFile[i].getName(), i));
                i++;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return listaArquivos;
    }

    public static String arquivo(String diretorio, String arquivo) {
        if (arquivo.isEmpty()) {
            return null;
        }
        String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/" + diretorio +"/"+ arquivo);
        try {
            File files = new File(caminho);
            if (!files.exists()) {
                return null;
            }
            return caminho;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getCliente() {
        if (cliente.equals("")) {
            if (GenericaSessao.exists("sessaoCliente")) {
                return GenericaSessao.getString("sessaoCliente");
            }
        }
        return cliente;
    }

}
