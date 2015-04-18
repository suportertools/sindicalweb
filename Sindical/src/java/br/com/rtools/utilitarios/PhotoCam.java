package br.com.rtools.utilitarios;

import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.db.FisicaDB;
import br.com.rtools.pessoa.db.FisicaDBToplink;
import br.com.rtools.seguranca.Usuario;
import java.io.File;
import java.io.IOException;
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
public class PhotoCam {

    private static String ABSOLUT_PATH;
    private static String PATH;
    private static String PATH_FILE;
    private static String FILE_TEMP;
    private static String FILE_TEMP_NAME;
    private static String FILE_PERMANENT;
    private static Boolean REPLACE_FILES;
    private static Boolean AUTO_SAVE;
    private static List UPDATES;
    private static Boolean SHOW_MESSAGE;
    private static Boolean SUCCESS;
    private String rotinaNome;

    @PostConstruct
    public void init() {
        ABSOLUT_PATH = "temp";
        PATH = "foto/" + getUsuario().getId();
        PATH_FILE = "perfil";
        FILE_TEMP = "";
        FILE_TEMP_NAME = "";
        FILE_PERMANENT = "";
        REPLACE_FILES = false;
        UPDATES = new ArrayList();
        SHOW_MESSAGE = false;
        AUTO_SAVE = false;
        SUCCESS = false;
        rotinaNome = "";
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

    public void listener(String dir, String filename, Boolean replace_file, Boolean autosave) {
        listener(dir, filename, replace_file, autosave, "");

    }

    /**
     *
     * @param dir
     * @param filename
     * @param replace_file
     * @param autosave
     * @param update
     */
    public void listener(String dir, String filename, Boolean replace_file, Boolean autosave, String update) {
        String[] split = null;
        if (!update.isEmpty()) {
            split = update.split(",");
            for (String split1 : split) {
                UPDATES.add(split1);
            }
        }
        AUTO_SAVE = autosave;
        ABSOLUT_PATH = dir;
        if (ABSOLUT_PATH.toUpperCase().equals("IMAGENS")) {
            if (PATH.isEmpty()) {
                PATH = "Fotos";
            }
            filename = filename + ".png";
        }
        REPLACE_FILES = replace_file;
        PATH_FILE = filename;
        SHOW_MESSAGE = true;

    }

    public static void oncapture(CaptureEvent captureEvent, String photo) {
        oncapture(captureEvent, photo, "");
    }

    public static synchronized void oncapture(CaptureEvent captureEvent, String photo, String caminhoTemporario) {
        oncapture(captureEvent, photo, "", false);
    }

    public static synchronized boolean oncapture(CaptureEvent captureEvent, String photo, String caminhoTemporario, boolean diretorio) {
        if (photo.equals("")) {
            Date date = new Date();
            photo = date.toGMTString();
        }
        String caminho = ABSOLUT_PATH;
        byte[] data = captureEvent.getData();
        if (diretorio) {
            if (PATH == null || PATH.isEmpty()) {
                caminho = ABSOLUT_PATH + "/" + caminhoTemporario;
            } else {
                if (caminhoTemporario.isEmpty()) {
                    caminho = ABSOLUT_PATH + "/" + PATH;
                } else {
                    if(caminhoTemporario.equals("PATH")) {
                        caminho = ABSOLUT_PATH + "/" + PATH + "/" + caminhoTemporario;                        
                    } else {
                        caminho = ABSOLUT_PATH + "/" + caminhoTemporario;
                    }
                }
            }
            Diretorio.criar(caminho);
        }
        if (photo.isEmpty()) {
            photo = PATH_FILE;
        }
        String file_path_local = "";
        if (photo.contains("png")) {
            file_path_local = "/Cliente/" + getCliente() + "/" + caminho + "/" + photo;
        } else {
            file_path_local = "/Cliente/" + getCliente() + "/" + caminho + "/" + photo + ".png";
        }
        String newFileName = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(file_path_local);
        File f = new File(newFileName);
//        if(REPLACE_FILES) {
//            if (f.exists()) {
//                f.delete();
//            }            
//        }
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

    public synchronized void capture(CaptureEvent captureEvent) {
        if (PhotoCam.oncapture(captureEvent, PATH_FILE, PATH, true)) {
            File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/" + ABSOLUT_PATH + "/" + PATH + "/perfil.png"));
            if (f.exists()) {
                FILE_TEMP = "/Cliente/" + getCliente() + "/" + ABSOLUT_PATH + "/" + PATH + "/perfil.png";
                if (AUTO_SAVE) {

                }
            } else {
            }
            if (SUCCESS) {
                if (!AUTO_SAVE) {
                    FILE_TEMP = "/Cliente/" + getCliente() + "/" + ABSOLUT_PATH + "/" + "/" + PATH_FILE;
                    FILE_PERMANENT = "";
                }
            } else {
                FILE_TEMP = "";
                FILE_PERMANENT = "";
            }
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

    public String getABSOLUT_PATH() {
        return ABSOLUT_PATH;
    }

    public void setABSOLUT_PATH(String aABSOLUT_PATH) {
        ABSOLUT_PATH = aABSOLUT_PATH;
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
            if (rotinaNome.equals("geracaoDebitosCartao")) {
                FisicaDB fisicaDB = new FisicaDBToplink();
                f = fisicaDB.pesquisaFisicaPorPessoa(Integer.parseInt(PATH_FILE.replace(".png", "")));
                f.setDtFoto(DataHoje.dataHoje());
                if (!dao.update(f, true)) {
                    SUCCESS = false;
                }
            }
        }
        SUCCESS = false;
    }

    public String getUpdate() throws InterruptedException, IOException {
        if (SUCCESS) {
            complete();
            Thread.sleep(5000);
            if (UPDATES.isEmpty()) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/Sindical/" + rotinaNome + ".jsf");
            } else {
                for (int i = 0; i < UPDATES.size(); i++) {
                    PF.update(UPDATES.get(i).toString());
                }
                UPDATES.clear();
                PF.closeDialog("dlg_loading_photo");
            }
        }
        return rotinaNome;
    }

    public String getUpdates() throws InterruptedException {
        String updates = "";
        if (SUCCESS) {
            complete();
            Thread.sleep(5000);
            UPDATES.clear();
            SUCCESS = false;
            for (int i = 0; i < UPDATES.size(); i++) {
                updates += UPDATES.get(i).toString();
            }
        }
        return updates;
    }

}
