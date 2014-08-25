package br.com.rtools.utilitarios;

import java.io.File;
import java.io.InputStream;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@SessionScoped
public class FileDownloadBean {

    private StreamedContent file;
    private boolean delete;

    public FileDownloadBean() {
        file = null;
        delete = false;
    }

    /**
     *
     * @param path
     * @param filename
     * @param content (image/jpg)
     */
    public FileDownloadBean(String path, String filename, String content) {
        try {
            File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(path + "/" + filename));
            if (f.exists()) {
                InputStream stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(path + "/" + filename);
                file = new DefaultStreamedContent(stream, content, filename);
                if (delete) {
                    f.delete();
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public StreamedContent getFile() {
        return file;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }
}
