package br.com.rtools.utilitarios;

import br.com.rtools.logSistema.NovoLog;
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
        return enviar(cu, criarDiretorios, false);
    }

    public static boolean enviar(ConfiguracaoUpload cu, boolean criarDiretorios, boolean mensagens) {
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
            if (!cu.getTiposPermitidos().isEmpty()) {
                for (int i = 0; i < cu.getTiposPermitidos().size(); i++) {
                    if (cu.getTiposPermitidos().get(i) != cu.getEvent().getFile().getContentType()) {
                        if (mensagens) {
                            GenericaMensagem.warn("Validação", "Tipo de arquivo não permitido! Tipos permitidos: " + cu.getTiposPermitidos().get(i).toString());
                        }
                        return false;
                    }
                }
            }
            if (cu.getTamanhoMaximo() > 0) {
                if (cu.getEvent().getFile().getSize() <= cu.getTamanhoMaximo()) {
                    if (mensagens) {
                        GenericaMensagem.warn("Validação", "Tamanho excedido. Tamanho máximo " + cu.getEvent().getFile().getSize());
                    }
                }
                return false;
            }
            if (cu.getLarguraMaxima() > 0) {
                if (mensagens) {
                    //GenericaMensagem.warn("Validação", "Largura excedida. Largura máxima: " + cu.getEvent().getFile().getSize());
                }
                //return false;
            }
            if (cu.getAlturaMaxima() > 0) {
                if (mensagens) {
                    //GenericaMensagem.warn("Validação", "Altura excedida. Altura máxima: " + cu.getEvent().getFile().getSize());
                }
                //return false;
            }
            File file = new File(diretorio + "/" + cu.getEvent().getFile().getFileName());
            if (file.exists()) {
                if (mensagens) {
                    GenericaMensagem.warn("Validação", "Arquivo já existe no caminho específicado!");
                }
                return false;
            }
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
            NovoLog log = new NovoLog();
            if (mensagens) {
                GenericaMensagem.warn("Erro", e.getMessage());
            }
            log.novo("Upload de arquivos", e.getMessage());
            System.out.println(e);
            return false;
        }
    }
}
