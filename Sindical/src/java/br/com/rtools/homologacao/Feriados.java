package br.com.rtools.homologacao;

import br.com.rtools.endereco.Cidade;
import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "hom_feriados")
@NamedQueries({
    @NamedQuery(name = "Feriados.pesquisaID", query = "SELECT F FROM Feriados AS F WHERE F.id = :pid"),
    @NamedQuery(name = "Feriados.findAll", query = "SELECT F FROM Feriados AS F ORDER BY F.dtData DESC, F.nome ASC "),
    @NamedQuery(name = "Feriados.findName", query = "SELECT F FROM Feriados AS F WHERE UPPER(F.nome) LIKE :pdescricao ORDER BY F.dtData DESC, F.nome ASC ")
})
public class Feriados implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_nome", length = 50, nullable = true)
    private String nome;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_data")
    private Date dtData;
    @JoinColumn(name = "id_cidade", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Cidade cidade;

    public Feriados() {
        this.id = -1;
        this.setData("");
        this.cidade = new Cidade();
    }

    public Feriados(int id, String data, Cidade cidade) {
        this.id = id;
        this.setData(data);
        this.cidade = cidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDtData() {
        return dtData;
    }

    public void setDtData(Date dtData) {
        this.dtData = dtData;
    }

    public String getData() {
        if (dtData != null) {
            return DataHoje.converteData(dtData);
        } else {
            return "";
        }
    }

    public void setData(String data) {
        if (!(data.isEmpty())) {
            this.dtData = DataHoje.converte(data);
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }
}
