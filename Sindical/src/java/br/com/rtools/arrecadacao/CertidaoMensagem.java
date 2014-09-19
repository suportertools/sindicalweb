package br.com.rtools.arrecadacao;

import br.com.rtools.endereco.Cidade;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "arr_certidao_mensagem")
@NamedQuery(name = "CertidaoMensagem.pesquisaID", query = "select c from CertidaoMensagem c where c.id = :pid")
public class CertidaoMensagem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_certidao_tipo", referencedColumnName = "id")
    @ManyToOne
    private CertidaoTipo certidaoTipo;
    @JoinColumn(name = "id_cidade", referencedColumnName = "id")
    @ManyToOne
    private Cidade cidade;
    @Column(name = "ds_mensagem", length = 8000)
    private String mensagem;    

    public CertidaoMensagem() {
        this.id = -1;
        this.certidaoTipo = new CertidaoTipo();
        this.cidade = new Cidade();
        this.mensagem = "";
    }

    public CertidaoMensagem(int id, CertidaoTipo certidaoTipo, Cidade cidade, String mensagem) {
        this.id = id;
        this.certidaoTipo = certidaoTipo;
        this.cidade = cidade;
        this.mensagem = mensagem;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CertidaoTipo getCertidaoTipo() {
        return certidaoTipo;
    }

    public void setCertidaoTipo(CertidaoTipo certidaoTipo) {
        this.certidaoTipo = certidaoTipo;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    
    
    
}
