package br.com.rtools.sistema;

import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;

public class Mensagem {

    private String assunto;
    private String mensagem;
    private Date data;

    public Mensagem() {
        this.assunto = "";
        this.mensagem = "Dígite seu texto aqui";
        this.data = DataHoje.dataHoje();
    }

    public Mensagem(String data, String assunto, String mensagem) {
        this.assunto = assunto;
        this.mensagem = mensagem;
        this.data = DataHoje.converte(data);
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getData() {
        return DataHoje.converteData(data);
    }

    public void setData(String data) {
        this.data = DataHoje.converte(data);
    }

    public Date getDtData() {
        return data;
    }

    public void setDtData(Date data) {
        this.data = data;
    }
}