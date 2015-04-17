package br.com.rtools.utilitarios;

import br.com.rtools.associativo.beans.GeracaoDebitosCartaoBean;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.db.FisicaDB;
import br.com.rtools.pessoa.db.FisicaDBToplink;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.sistema.ConfiguracaoUpload;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.primefaces.event.FileUploadEvent;

@ManagedBean(name = "uploadBean")
@SessionScoped
public class Upload {

    private ConfiguracaoUpload configuracaoUpload;
    private static String ABSOLUT_PATH;
    private static String PATH;
    private static String PATH_FILE;
    private static String FILE_TEMP;
    private static String FILE_TEMP_NAME;
    private static String FILE_PERMANENT;
    private static List TYPES;
    private static Boolean REPLACE_FILES;
    private static Boolean AUTO_SAVE;
    private static List UPDATES;
    private static Boolean SHOW_MESSAGE;
    private static Boolean SUCCESS;
    private String rotinaNome;

    @PostConstruct
    public void init() {
        ABSOLUT_PATH = "temp";
        PATH = "";
        PATH_FILE = "";
        FILE_TEMP = "";
        FILE_TEMP_NAME = "";
        FILE_PERMANENT = "";
        TYPES = new ArrayList();
        REPLACE_FILES = false;
        UPDATES = new ArrayList();
        SHOW_MESSAGE = false;
        AUTO_SAVE = false;
        configuracaoUpload = new ConfiguracaoUpload();
        SUCCESS = false;
        rotinaNome = "";
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("uploadBean");
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
     * @param types Exemplo: png,jpg,gif
     * @param replace_file
     */
    public void listenerTemp(String types, boolean replace_file) {
        types = types.replace(" ", "");
        TYPES = new ArrayList();
        String[] split = types.split(",");
        for (String split1 : split) {
            TYPES.add(split1);
        }
        TYPES = new ArrayList();
        REPLACE_FILES = replace_file;
    }

    public void listener(String dir, String filename, String types, Boolean replace_file, Boolean autosave) {
        listener(dir, filename, types, replace_file, autosave, "");

    }

    /**
     *
     * @param dir
     * @param filename
     * @param types
     * @param replace_file
     * @param autosave
     * @param update
     */
    public void listener(String dir, String filename, String types, Boolean replace_file, Boolean autosave, String update) {
        types = types.replace(" ", "");
        update = update.replace(" ", "");
        TYPES = new ArrayList();
        String[] split = types.split(",");
        for (String split1 : split) {
            TYPES.add(split1);
        }
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

    public void upload(FileUploadEvent event) {
        try {
            File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/" + ABSOLUT_PATH + "/" + PATH + "/" + PATH_FILE));
            if (f.exists()) {
                boolean delete = f.delete();
            } else {
                FILE_TEMP = "";
            }
            // Diretorio.criar("temp/foto/" + getUsuario().getId(), true);
            configuracaoUpload = new ConfiguracaoUpload();
            configuracaoUpload.setArquivo(event.getFile().getFileName());
            configuracaoUpload.setDiretorio(ABSOLUT_PATH + "/" + PATH);
            configuracaoUpload.setSubstituir(REPLACE_FILES);
            configuracaoUpload.setTiposPermitidos(TYPES);
            configuracaoUpload.setRenomear(PATH_FILE);
            configuracaoUpload.setEvent(event);
            SUCCESS = Upload.enviar(configuracaoUpload, true, SHOW_MESSAGE);
            if (SUCCESS) {
                complete();
                if (!AUTO_SAVE) {
                    FILE_TEMP = "/Cliente/" + getCliente() + "/" + ABSOLUT_PATH + "/" + "/" + PATH_FILE;
                    FILE_PERMANENT = "";
                }
            } else {
                FILE_TEMP = "";
                FILE_PERMANENT = "";
            }
            PF.closeDialog("dlg_loading_image");
        } catch (Exception e) {
        }
    }

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
                Boolean isTipoPermitido = false;
                for (int i = 0; i < cu.getTiposPermitidos().size(); i++) {
                    if (cu.getTiposPermitidos().get(i).toString().equals(cu.getEvent().getFile().getContentType())) {
                        isTipoPermitido = true;
                        break;
                    }
                }
                if (!isTipoPermitido) {
                    if (mensagens) {
                        GenericaMensagem.warn("Validação", "Tipo de arquivo não permitido! Tipos permitidos: " + cu.getTiposPermitidos().toString());
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
            List listContentType = new ArrayList();
            listContentType.add("PNG");
            listContentType.add("JPG");
            listContentType.add("JPEG");
            listContentType.add("GIF");
            for (Object listContentType1 : listContentType) {
                if (cu.getEvent().getFile().getContentType().toUpperCase() == listContentType1) {
                    if (cu.getLarguraMaxima() > 0) {
                        if (mensagens) {
                            GenericaMensagem.warn("Validação", "Largura excedida. Largura máxima: " + cu.getEvent().getFile().getSize());
                        }
                        return false;
                    }
                    if (cu.getAlturaMaxima() > 0) {
                        if (mensagens) {
                            GenericaMensagem.warn("Validação", "Altura excedida. Altura máxima: " + cu.getEvent().getFile().getSize());
                        }
                        return false;
                    }
                }
                break;
            }
            File file = new File(diretorio + "/" + cu.getEvent().getFile().getFileName());
            if (cu.isSubstituir()) {
                if (!cu.getRenomear().equals("")) {
                    File novoNome = new File(diretorio + "/" + cu.getRenomear());
                    if (novoNome.exists()) {
                        novoNome.delete();
                    } else {
                        file.delete();
                    }
                } else {
                    file.delete();
                }
            } else {
                if (file.exists()) {
                    if (mensagens) {
                        GenericaMensagem.warn("Validação", "Arquivo já existe no caminho específicado!");
                    }
                    return false;
                }
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
            if (!cu.getRenomear().equals("")) {
                File novoNome = new File(diretorio + "/" + cu.getRenomear());
                file.renameTo(novoNome);
            }
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

    public Usuario getUsuario() {
        if (GenericaSessao.exists("sessaoUsuario")) {
            return (Usuario) GenericaSessao.getObject("sessaoUsuario");
        }
        return new Usuario();
    }

    public String getCliente() {
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

    public void complete() {
        if (SUCCESS) {
            Fisica f;
            Dao dao = new Dao();
            if (rotinaNome.equals("geracaoDebitosCartao")) {
                FisicaDB fisicaDB = new FisicaDBToplink();
                f = fisicaDB.pesquisaFisicaPorPessoa(Integer.parseInt(PATH_FILE.replace(".png", "")));
                f.setDtFoto(DataHoje.dataHoje());
                if (dao.update(f, true)) {
                    for (int i = 0; i < UPDATES.size(); i++) {
                        PF.update(UPDATES.get(i).toString());
                    }
                }
                UPDATES.clear();
                TYPES.clear();
            }
        }
        SUCCESS = false;
    }
}
