package br.com.rtools.utilitarios;

import java.io.File;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.ServletContext;
import org.primefaces.model.CroppedImage;

@ManagedBean(name = "cropperBean")
@SessionScoped
public class Cropper implements Serializable {

    private Boolean cut;
    private CroppedImage croppedImage;
    private String newImageName;

    @PostConstruct
    public void init() {
        newImageName = "";
        cut = false;
        croppedImage = null;
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("cropperBean");
    }

    public CroppedImage getCroppedImage() {
        return croppedImage;
    }

    public void setCroppedImage(CroppedImage croppedImage) {
        this.croppedImage = croppedImage;
    }

    public void listenerParamsCrop(String newImageName) {
        this.newImageName = newImageName;
    }

    public synchronized void crop() {
        if (croppedImage == null) {
            return;
        }
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String newFileName = servletContext.getRealPath("") + newImageName;

        FileImageOutputStream imageOutput;
        try {
            imageOutput = new FileImageOutputStream(new File(newFileName));
            imageOutput.write(croppedImage.getBytes(), 0, croppedImage.getBytes().length);
            imageOutput.close();
        } catch (Exception e) {
            GenericaMensagem.error("Erro", "Ao recortar imagem!");
        }
        cut = false;
        newImageName = "";
        croppedImage = null;
        GenericaMensagem.info("Sucesso", "Imagem recortada");
        ((PhotoCam) GenericaSessao.getObject("photoCamBean")).setLoad(true);
        ((PhotoCam) GenericaSessao.getObject("photoCamBean")).setStop(0);
    }

    private String getRandomImageName() {
        int i = (int) (Math.random() * 100000);

        return String.valueOf(i);
    }

    public String getNewImageName() {
        return newImageName;
    }

    public void setNewImageName(String newImageName) {
        this.newImageName = newImageName;
    }

    public Boolean getCut() {
        return cut;
    }

    public void setCut(Boolean cut) {
        this.cut = cut;
    }

    public synchronized void editCrop() {
        cut = !cut;
        if (cut) {
            ((PhotoCam) GenericaSessao.getObject("photoCamBean")).setStop(0);
        } else {
            ((PhotoCam) GenericaSessao.getObject("photoCamBean")).setStop(0);

        }
    }

}
