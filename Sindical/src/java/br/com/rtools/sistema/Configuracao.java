package br.com.rtools.sistema;

import javax.persistence.*;

@Entity
@Table(name="SIS_CONFIGURACAO")
@NamedQuery(name="Configuracao.pesquisaID", query="select c from Configuracao c where c.id = :pid")
public class Configuracao implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Column(name="DS_NOME_CLIENTE", length=300, nullable=false)
    private String nomeCliente;
    @Column(name="DS_PERSISTENCE", length=200, nullable=false)
    private String persistence;
    @Column(name="DS_CAMINHO_SISTEMA", length=200, nullable=false)
    private String caminhoSistema;
    @Column(name="DS_IDENTIFICA", length=100, nullable=false)
    private String identifica;

    public Configuracao() {
        this.id = -1;
        this.nomeCliente = "";
        this.persistence = "";
        this.caminhoSistema = "";
        this.identifica = "";
    }
    
    public Configuracao(int id, String nomeCliente, String persistence, String caminhoSistema, String identifica) {
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.persistence = persistence;
        this.caminhoSistema = caminhoSistema;
        this.identifica = identifica;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }    

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getPersistence() {
        return persistence;
    }

    public void setPersistence(String persistence) {
        this.persistence = persistence;
    }

    public String getCaminhoSistema() {
        return caminhoSistema;
    }

    public void setCaminhoSistema(String caminhoSistema) {
        this.caminhoSistema = caminhoSistema;
    }

    public String getIdentifica() {
        return identifica;
    }

    public void setIdentifica(String identifica) {
        this.identifica = identifica;
    }
    
}
