package br.com.rtools.sistema.beans;

import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.sistema.ConfiguracaoUpload;
import br.com.rtools.utilitarios.Diretorio;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.MemoryFile;
import br.com.rtools.utilitarios.Upload;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.primefaces.event.FileUploadEvent;

@ManagedBean
@SessionScoped
public class UploadFilesBean implements Serializable {

    private List<MemoryFile> listFiles;
    private String path;
    private Long maxSize;
    private Long maxFiles;

    public UploadFilesBean() {
        this.listFiles = new ArrayList<>();
        this.path = "";
        this.maxSize = null;
        this.maxFiles = null;
    }

    public UploadFilesBean(String path) {
        this.path = path;
    }

    public UploadFilesBean(String path, List<MemoryFile> listFiles) {
        this.path = path;
        this.listFiles = listFiles;
    }

    public UploadFilesBean(String path, Long maxSize, Long maxFiles) {
        this.path = path;
        this.maxSize = maxSize;
        this.maxFiles = maxFiles;
    }

    @PostConstruct
    public void init() {

    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("arquivosBean");
    }

    public void upload(FileUploadEvent event) {
        if (!path.isEmpty()) {
            long countSize = 0;
            long countFiles = 0;
            if (maxSize != null || maxFiles != null) {
                for (int i = 0; i < listFiles.size(); i++) {
                    countSize += listFiles.get(i).getFile().length();
                    countFiles++;
                }
                if (maxSize != null && countSize >= maxSize) {
                    GenericaMensagem.warn("Sistema", "Tamanho máximo de arquivos permitido");
                    return;
                }
                if (maxFiles != null && countFiles >= maxFiles) {
                    GenericaMensagem.warn("Sistema", "Limite de arquivos excedido. Máximo " + countFiles + " arquivos.");
                    return;
                }
            }
            ConfiguracaoUpload configuracaoUpload = new ConfiguracaoUpload();
            configuracaoUpload.setArquivo(event.getFile().getFileName());
            configuracaoUpload.setDiretorio(path);
            configuracaoUpload.setEvent(event);
            if (Upload.enviar(configuracaoUpload, true)) {
                listFiles.clear();
            }
        }
    }

    public List<MemoryFile> getListFiles() {
        if (listFiles == null) {
            listFiles = new ArrayList<>();
        }
        if (!path.isEmpty() && listFiles.isEmpty()) {
            listFiles = Diretorio.listMemoryFiles(path);
        }
        return listFiles;
    }

    public List<MemoryFile> listFilesByPath(String path) {
        if (listFiles == null) {
            listFiles = new ArrayList<>();
        }
        if (!path.isEmpty() && listFiles.isEmpty()) {
            listFiles = Diretorio.listMemoryFiles(path);
        }
        return listFiles;
    }

    public void setListFiles(List<MemoryFile> listFiles) {
        this.listFiles = listFiles;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void remove(int index) {
        String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/" + path + "/" + (String) listFiles.get(index).getName());
        File fl = new File(caminho);
        fl.delete();
        listFiles.remove(index);
        listFiles.clear();
    }

    public Long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Long maxSize) {
        this.maxSize = maxSize;
    }

    public Long getMaxFiles() {
        return maxFiles;
    }

    public void setMaxFiles(Long maxFiles) {
        this.maxFiles = maxFiles;
    }

}
