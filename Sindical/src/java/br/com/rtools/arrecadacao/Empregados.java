package br.com.rtools.arrecadacao;

import br.com.rtools.pessoa.Juridica;
import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "arr_empregados")
@NamedQuery(name = "Empregados.pesquisaID", query = "select e from Empregados e where e.id = :pid")
public class Empregados  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_juridica", referencedColumnName = "id")
    @ManyToOne
    private Juridica juridica;
    @Column(name = "nr_quantidade")
    private int quantidade;
    @Column(name = "ds_referencia", length = 7)
    private String referencia;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_lancamento")
    private Date dtLancamento;    

    public Empregados() {
        this.id = -1;
        this.juridica = new Juridica();
        this.quantidade = 0;
        this.referencia = "";
        this.dtLancamento = DataHoje.dataHoje();
    }
    
    
    public Empregados(int id, Juridica juridica, int quantidade, String referencia, Date dtLancamento) {
        this.id = id;
        this.juridica = juridica;
        this.quantidade = quantidade;
        this.referencia = referencia;
        this.dtLancamento = dtLancamento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Juridica getJuridica() {
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Date getDtLancamento() {
        return dtLancamento;
    }

    public void setDtLancamento(Date dtLancamento) {
        this.dtLancamento = dtLancamento;
    }
    
    
    
}
