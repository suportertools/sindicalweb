package br.com.rtools.sistema;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "sis_arquivo")
public class Arquivo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_nome", length = 255)
    private String nome;
    @Column(name = "ds_caminho", length = 255)
    private String caminho;
    @Column(name = "ds_extensao", length = 10)
    private String extensao;

    public Arquivo() {
        this.id = -1;
        this.nome = "";
        this.caminho = "";
        this.extensao = "";
    }

    public Arquivo(int id, String nome, String caminho, String extensao) {
        this.id = id;
        this.nome = nome;
        this.caminho = caminho;
        this.extensao = extensao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public String getExtensao() {
        return extensao;
    }

    public void setExtensao(String extensao) {
        this.extensao = extensao;
    }

}
