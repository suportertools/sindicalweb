package br.com.rtools.arrecadacao;

import br.com.rtools.pessoa.Porte;
import br.com.rtools.utilitarios.DataHoje;
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
import java.io.Serializable;

@Entity
@Table(name = "arr_piso_salarial_lote")
@NamedQuery(name = "PisoSalarialLote.pesquisaID", query = "select psl from PisoSalarialLote psl where psl.id = :pid")
public class PisoSalarialLote implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_patronal", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Patronal patronal;
    @JoinColumn(name = "id_porte", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Porte porte;
    @Column(name = "nr_ano", length = 4, nullable = false)
    private int ano;
    @Column(name = "ds_mensagem", length = 3000, nullable = true)
    private String mensagem;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_validade")
    private Date dtValidade;

    public PisoSalarialLote() {
        this.id = -1;
        this.patronal = new Patronal();
        this.porte = new Porte();
        this.ano = 0;
        this.mensagem = "";
        this.setValidade("");
    }

    public PisoSalarialLote(int id, Patronal patronal, Porte porte, int ano, String mensagem, String validade) {
        this.id = id;
        this.patronal = patronal;
        this.porte = porte;
        this.ano = ano;
        this.mensagem = mensagem;
        this.setValidade(validade);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Patronal getPatronal() {
        return patronal;
    }

    public void setPatronal(Patronal patronal) {
        this.patronal = patronal;
    }

    public Porte getPorte() {
        return porte;
    }

    public void setPorte(Porte porte) {
        this.porte = porte;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Date getDtValidade() {
        return dtValidade;
    }

    public void setDtValidade(Date dtValidade) {
        this.dtValidade = dtValidade;
    }

    public String getValidade() {
        return DataHoje.converteData(dtValidade);
    }

    public void setValidade(String validade) {
        this.dtValidade = DataHoje.converte(validade);
    }
}
