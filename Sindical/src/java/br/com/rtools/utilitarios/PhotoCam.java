package br.com.rtools.utilitarios;

import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.db.FisicaDB;
import br.com.rtools.pessoa.db.FisicaDBToplink;
import br.com.rtools.seguranca.Usuario;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.ServletContext;
import org.primefaces.event.CaptureEvent;

@ManagedBean(name = "photoCamBean")
@SessionScoped
public class PhotoCam implements Serializable {

    private static String PATH;
    private static String PATH_FILE;
    private static String FILE_TEMP;
    private static String FILE_TEMP_NAME;
    private static String FILE_PERMANENT;
    private static Boolean REPLACE_FILES;
    private static Boolean AUTO_SAVE;
    private static List UPDATES;
    private static String UPDATE;
    private static Boolean SHOW_MESSAGE;
    private static Boolean SUCCESS;
    private String rotinaNome;
    private Boolean visible;

    @PostConstruct
    public void init() {
        PATH = "temp/foto/" + getUsuario().getId();
        PATH_FILE = "perfil";
        FILE_TEMP = "";
        FILE_TEMP_NAME = "";
        FILE_PERMANENT = "/Imagens/user_undefined.png";
        REPLACE_FILES = false;
        UPDATES = new ArrayList();
        SHOW_MESSAGE = false;
        AUTO_SAVE = false;
        SUCCESS = false;
        rotinaNome = "";
        UPDATE = "";
        visible = false;
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("photoCamBean");
    }

    public void load() {

    }

    /**
     *
     * @param path_temp
     * @param filename_temp
     */
    public void load(String path_temp, String filename_temp) {
        PATH = path_temp + "/" + getUsuario().getId() + "/";
        PATH_FILE = filename_temp;
        FILE_TEMP_NAME = filename_temp;
    }

    /**
     *
     * @param replace_file
     */
    public void listenerTemp(boolean replace_file) {
        REPLACE_FILES = replace_file;
    }

    public void listener(String aPATH, String aFILENAME, Boolean replace_file, Boolean autosave) {
        listener(aPATH, aFILENAME, replace_file, autosave, "");

    }

    /**
     * @param aPATH
     * @param aFILENAME
     * @param replace_file
     * @param autosave
     * @param update
     */
    public void listener(String aPATH, String aFILENAME, Boolean replace_file, Boolean autosave, String update) {
        String[] split = null;
        if (!update.isEmpty()) {
            split = update.split(",");
            for (String split1 : split) {
                UPDATES.add(split1);
            }
        }
        FILE_PERMANENT = "/Imagens/user_undefined.png";
        UPDATE = update.trim();
        AUTO_SAVE = autosave;
        PATH = aPATH;
        aFILENAME = aFILENAME + ".png";
        REPLACE_FILES = replace_file;
        PATH_FILE = aFILENAME;
        SHOW_MESSAGE = true;
        visible = true;

    }

    public static void oncapture(CaptureEvent captureEvent, String photo) {
        oncapture(captureEvent, photo, "");
    }

    public static synchronized void oncapture(CaptureEvent captureEvent, String photo, String caminhoTemporario) {
        oncapture(captureEvent, photo, "", false);
    }

    public static synchronized boolean oncapture(CaptureEvent captureEvent, String photo, String aPATH, Boolean create_dirs) {
        if (photo.equals("")) {
            Date date = new Date();
            photo = date.toGMTString();
        }
        String caminho;
        if (!PATH.isEmpty()) {
            caminho = PATH;
        } else {
            caminho = aPATH;
        }
        if (create_dirs) {
            Diretorio.criar(caminho);
        }
        byte[] data = captureEvent.getData();
        if (photo.isEmpty()) {
            photo = PATH_FILE;
        }
        String file_path_local;
        if (photo.contains("png")) {
            file_path_local = "/Cliente/" + getCliente() + "/" + caminho + "/" + photo;
        } else {
            file_path_local = "/Cliente/" + getCliente() + "/" + caminho + "/" + photo + ".png";
        }
        String newFileName = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(file_path_local);
        File f = new File(newFileName);
        FileImageOutputStream imageOutput;
        try {
            imageOutput = new FileImageOutputStream(new File(newFileName));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
            FILE_TEMP = newFileName;
            FILE_TEMP_NAME = file_path_local;
            SUCCESS = true;
            return true;
        } catch (IOException e) {
            //throw new FacesException("Erro ao capturar imagem!");
            return false;
        }
    }

    public synchronized void capture(CaptureEvent captureEvent) throws InterruptedException {
        if (PhotoCam.oncapture(captureEvent, PATH_FILE, "", true)) {
            complete();
            File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/" + PATH + "/" + PATH_FILE));
            if (f.exists()) {
                FILE_PERMANENT = "/Cliente/" + getCliente() + "/" + PATH + "/" + PATH_FILE;
            } else {
                FILE_PERMANENT = "/Imagens/user_undefined.png";
            }
            // Thread.sleep(5000);
        }
//        RequestContext.getCurrentInstance().update(":form_pessoa_fisica");
//        RequestContext.getCurrentInstance().execute("dgl_captura.hide();");
    }

    public Usuario getUsuario() {
        if (GenericaSessao.exists("sessaoUsuario")) {
            return (Usuario) GenericaSessao.getObject("sessaoUsuario");
        }
        return new Usuario();
    }

    public static String getCliente() {
        if (GenericaSessao.exists("sessaoCliente")) {
            return GenericaSessao.getString("sessaoCliente");
        }
        return "";
    }

    public String getFILE_TEMP() {
        return FILE_TEMP;
    }

    public void setFILE_TEMP(String aFILE_TEMP) {
        FILE_TEMP = aFILE_TEMP;
    }

    public String getFILE_PERMANENT() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }
        return FILE_PERMANENT;
    }

    public void setFILE_PERMANENT(String aFILE_PERMANENT) {
        FILE_PERMANENT = aFILE_PERMANENT;
    }

    public Boolean getSUCCESS() {
        return SUCCESS;
    }

    public void setSUCCESS(Boolean aSUCCESS) {
        SUCCESS = aSUCCESS;
    }

    public String getRotinaNome() {
        return rotinaNome;
    }

    public void setRotinaNome(String rotinaNome) {
        this.rotinaNome = rotinaNome;
    }

    public String getPATH() {
        return PATH;
    }

    public void setPATH(String aPATH) {
        PATH = aPATH;
    }

    public void complete() {
        if (SUCCESS) {
            Fisica f;
            Dao dao = new Dao();
            switch (rotinaNome) {
                case "geracaoDebitosCartao":
                case "pessoaFisica":
                case "matriculaAcademia":
                case "usuario":
                    try {
                        FisicaDB fisicaDB = new FisicaDBToplink();
                        f = fisicaDB.pesquisaFisicaPorPessoa(Integer.parseInt(PATH_FILE.replace(".png", "")));
                        f.setDtFoto(DataHoje.dataHoje());
                        if (!dao.update(f, true)) {
                            SUCCESS = false;
                        }
                    } catch (Exception e) {

                    }
                    break;
            }
        }
        SUCCESS = false;
    }

    public String getUPDATE() {
        return UPDATE;
    }

    public void setUPDATE(String aUPDATE) {
        UPDATE = aUPDATE;
    }

    public void waiting(Integer sleep) {
        try {
            Thread.sleep(sleep);
            PF.closeDialog("dlg_loading_photo");
            PF.update(UPDATE);
        } catch (InterruptedException ex) {

        }
    }

    public void waiting() {
        waiting(5000);
    }

    public void close() {
        SUCCESS = false;
        PATH = "temp/foto/" + getUsuario().getId();
        PATH_FILE = "perfil";
        FILE_TEMP = "";
        FILE_TEMP_NAME = "";
        FILE_PERMANENT = "/Imagens/user_undefined.png";
        REPLACE_FILES = false;
        SHOW_MESSAGE = false;
        AUTO_SAVE = false;
        SUCCESS = false;
        UPDATES = new ArrayList();
        visible = false;
    }

    public String closeRefresh() {
        close();
        return rotinaNome;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

}
