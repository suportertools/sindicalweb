package br.com.rtools.associativo;

import br.com.rtools.pessoa.Pessoa;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "soc_mensalidades_atrasadas")
public class MensalidadesAtrasadas implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id")
    @ManyToOne
    private Pessoa pessoa;
    @Column(name = "ds_descricao")
    private String descricao;
    @Column(name = "nr_valor")
    private float valor;

    public MensalidadesAtrasadas() {
        this.id = -1;
        this.pessoa = null;
        this.descricao = "";
        this.valor = 0;
    }
    
    public MensalidadesAtrasadas(int id, Pessoa pessoa, String descricao, float valor) {
        this.id = id;
        this.pessoa = pessoa;
        this.descricao = descricao;
        this.valor = valor;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }
    
}
