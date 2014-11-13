package br.com.rtools.utilitarios;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpServletResponse;
import javax.faces.context.FacesContext;

public class Download {

    private String filename;
    private String fileLocation;
    private String mimeType;
    private FacesContext facesContext;
    private HttpServletResponse response;
    private File file;

    public Download(
            String filename,
            String fileLocation,
            String mimeType,
            FacesContext facesContext) {
        this.filename = filename;
        this.fileLocation = fileLocation;
        this.mimeType = mimeType;
        this.facesContext = facesContext;
    }

    public synchronized void baixar() {
        ExternalContext context = facesContext.getExternalContext();
        String path = fileLocation; // LOCALIZACAO DO ARQUIVO
        String fullFileName = path + "/" + filename;
        file = new File(fullFileName); // LINHA ALTERADA
        response = (HttpServletResponse) context.getResponse();
        response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\""); // SETA O HEADER COM O QUE VAI APARECER NA HORA DO DOWNLOAD
        response.setContentLength((int) file.length()); // TAMANHO DO ARQUIVO
        response.setContentType(mimeType); // O TIPO DO ARQUIVO
        try {
            FileInputStream in = new FileInputStream(file);
            OutputStream out = response.getOutputStream();
        
            byte[] buf = new byte[(int) file.length()];
            int count;
            while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }

            in.close();
            out.flush();
            out.close();
            facesContext.responseComplete();
        } catch (IOException ex) {
            System.out.println("Error in downloadFile: " + ex.getMessage());
        }
    }
    
    public void remover () {
        if (file.exists()) {
            file.delete();
        }
    }
}
