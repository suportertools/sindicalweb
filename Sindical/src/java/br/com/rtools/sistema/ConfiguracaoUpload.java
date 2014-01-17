package br.com.rtools.sistema;

import org.primefaces.event.FileUploadEvent;

public class ConfiguracaoUpload {

    private String diretorio;
    private String arquivo;
    private FileUploadEvent event;

    public ConfiguracaoUpload() {
        this.diretorio = "";
        this.arquivo = "";
        this.event = null;
    }

    public ConfiguracaoUpload(int id, String diretorio, String arquivo, FileUploadEvent event) {
        this.diretorio = diretorio;
        this.arquivo = arquivo;
        this.event = event;
    }

    public String getDiretorio() {
        return diretorio;
    }

    public void setDiretorio(String diretorio) {
        this.diretorio = diretorio;
    }

    public String getArquivo() {
        return arquivo;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

    public FileUploadEvent getEvent() {
        return event;
    }

    public void setEvent(FileUploadEvent event) {
        this.event = event;
    }

}
