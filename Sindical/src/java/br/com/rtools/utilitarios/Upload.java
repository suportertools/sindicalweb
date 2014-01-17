package br.com.rtools.utilitarios;

import br.com.rtools.sistema.ConfiguracaoUpload;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

public class Upload {

    public static boolean enviar(ConfiguracaoUpload cu) {
        return enviar(cu, false);
    }

    public static boolean enviar(ConfiguracaoUpload cu, boolean criarDiretorios) {
        if (cu.getEvent().getFile().getFileName() == null) {
            return false;
        }
        String cliente = "";
        if (GenericaSessao.exists("sessaoCliente")) {
            cliente = GenericaSessao.getString("sessaoCliente");
            if (cliente.equals("")) {
                return false;
            }
        }        
        String diretorio = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + cliente + "/" + cu.getDiretorio());
        if (criarDiretorios) {
            if (diretorio.equals("")) {
                return false;
            }
            Diretorio.criar(cu.getDiretorio());
        }
        try {
            File file = new File(diretorio + "/" + cu.getEvent().getFile().getFileName());
            InputStream in = cu.getEvent().getFile().getInputstream();
            FileOutputStream out = new FileOutputStream(file.getPath());
            byte[] buf = new byte[(int) cu.getEvent().getFile().getSize()];
            int count;
            while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }
            in.close();
            out.flush();
            out.close();
            return true;
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }
    }
}
