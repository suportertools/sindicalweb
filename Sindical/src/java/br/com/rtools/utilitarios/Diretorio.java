package br.com.rtools.utilitarios;

import java.io.File;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

public class Diretorio {

    public static boolean criar(String diretorio) {
        String cliente = "";
        if (GenericaSessao.exists("sessaoCliente")) {
            cliente = GenericaSessao.getString("sessaoCliente");
            if (cliente.equals("")) {
                return false;
            }
        }
        diretorio = "/Cliente/" + cliente + "/" + diretorio;
        try {
            String s[] = diretorio.split("/");
            boolean err = false;
            String caminhoContac = "";
            int b = 0;
            for (int i = 0; i < s.length; i++) {
                if(!s[i].equals("")) {
                    if(b == 0) {
                        caminhoContac = s[i];
                    } else {
                        caminhoContac = "/"+caminhoContac+"/"+s[i];
                    }
                    String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(caminhoContac);
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
        String cliente = "";
        if (GenericaSessao.exists("sessaoCliente")) {
            cliente = GenericaSessao.getString("sessaoCliente");
            if (cliente.equals("")) {
                return false;
            }
        }
        if (new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + cliente + "/" + diretorio)).exists()) {
            File file = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + cliente + "/" + diretorio));
            if (file.delete()) {
                return true;
            }
        }
        return false;
    }

}
