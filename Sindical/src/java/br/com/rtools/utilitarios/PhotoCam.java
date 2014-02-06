package br.com.rtools.utilitarios;

import static br.com.rtools.utilitarios.Diretorio.getCliente;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.ServletContext;
import org.primefaces.event.CaptureEvent;

public class PhotoCam {

    public static void oncapture(CaptureEvent captureEvent, String photo) {
        oncapture(captureEvent, photo, "");
    }

    public static void oncapture(CaptureEvent captureEvent, String photo, String caminhoTemporario) {
        oncapture(captureEvent, photo, "", false);
    }

    public static boolean oncapture(CaptureEvent captureEvent, String photo, String caminhoTemporario, boolean diretorio) {
        if (photo.equals("")) {
            Date date = new Date();
            photo = date.toGMTString();
        }
        String caminho = "temp";
        byte[] data = captureEvent.getData();
        if (diretorio) {
            caminho = "temp/" + caminhoTemporario;
            Diretorio.criar(caminho);
        }
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String newFileName = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/" + caminho +"/"+ photo +".png");
        File f = new File(newFileName);

        if (f.exists()) {
            f.delete();
        }
        FileImageOutputStream imageOutput;
        try {
            imageOutput = new FileImageOutputStream(new File(newFileName));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
            return true;
        } catch (IOException e) {
            //throw new FacesException("Erro ao capturar imagem!");
            return false;
        }
    }

}
