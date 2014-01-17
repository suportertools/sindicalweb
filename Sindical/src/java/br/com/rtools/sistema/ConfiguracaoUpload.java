package br.com.rtools.sistema;

import java.util.ArrayList;
import java.util.List;
import org.primefaces.event.FileUploadEvent;

public class ConfiguracaoUpload {

    private String diretorio;
    private String arquivo;
    private FileUploadEvent event;
    private int tamanhoMaximo;
    private int alturaMaxima;
    private int larguraMaxima;
    private List tiposPermitidos;

    public ConfiguracaoUpload() {
        this.diretorio = "";
        this.arquivo = "";
        this.event = null;
        this.tamanhoMaximo = 0;
        this.alturaMaxima = 0;
        this.larguraMaxima = 0;
        this.tiposPermitidos = new ArrayList();
    }

    public ConfiguracaoUpload(String diretorio, String arquivo, FileUploadEvent event, int tamanhoMaximo, int alturaMaxima, int larguraMaxima, List tiposPermitidos) {
        this.diretorio = diretorio;
        this.arquivo = arquivo;
        this.event = event;
        this.tamanhoMaximo = tamanhoMaximo;
        this.alturaMaxima = alturaMaxima;
        this.larguraMaxima = larguraMaxima;
        this.tiposPermitidos = tiposPermitidos;
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

    public int getTamanhoMaximo() {
        return tamanhoMaximo;
    }

    public void setTamanhoMaximo(int tamanhoMaximo) {
        this.tamanhoMaximo = tamanhoMaximo;
    }

    public int getAlturaMaxima() {
        return alturaMaxima;
    }

    public void setAlturaMaxima(int alturaMaxima) {
        this.alturaMaxima = alturaMaxima;
    }

    public int getLarguraMaxima() {
        return larguraMaxima;
    }

    public void setLarguraMaxima(int larguraMaxima) {
        this.larguraMaxima = larguraMaxima;
    }

    public List getTiposPermitidos() {
        return tiposPermitidos;
    }

    public void setTiposPermitidos(List tiposPermitidos) {
        this.tiposPermitidos = tiposPermitidos;
    }

}
