package br.com.rtools.arrecadacao;

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
@Table(name = "arr_convencao_cct_servico")
public class ConvencaoServico implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_convencao_cidade", referencedColumnName = "id")
    @ManyToOne
    private ConvencaoCidade convencaoCidade;
    @Column(name = "ds_clausula_cct", length = 1000, nullable = true)
    private String clausula;

    public ConvencaoServico() {
        this.id = -1;
        this.convencaoCidade = null;
        this.clausula = "";
    }
    
    public ConvencaoServico(int id, ConvencaoCidade convencaoCidade, String clausula) {
        this.id = id;
        this.convencaoCidade = convencaoCidade;
        this.clausula = clausula;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ConvencaoCidade getConvencaoCidade() {
        return convencaoCidade;
    }

    public void setConvencaoCidade(ConvencaoCidade convencaoCidade) {
        this.convencaoCidade = convencaoCidade;
    }

    public String getClausula() {
        return clausula;
    }

    public void setClausula(String clausula) {
        this.clausula = clausula;
    }
}
