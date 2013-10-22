package br.com.rtools.pessoa;

import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "PES_SPC")
@NamedQueries({
    @NamedQuery(name = "Spc.pesquisaID", query = "SELECT S FROM Spc AS S WHERE S.id = :pid"),
    @NamedQuery(name = "Spc.findAll", query = "SELECT S FROM Spc AS S ORDER BY S.pessoa.nome ASC, S.dtEntrada ASC "),
    @NamedQuery(name = "Spc.findName", query = "SELECT S FROM Spc AS S WHERE UPPER(S.pessoa.nome) LIKE :pdescricao ORDER BY S.pessoa.nome ASC, S.dtEntrada ASC")
})
public class Spc implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_PESSOA", referencedColumnName = "ID", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private Pessoa pessoa;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_ENTRADA")
    private Date dtEntrada;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_SAIDA")
    private Date dtSaida;
    @Column(name = "DS_OBS", length = 200)
    private String observacao;
            

    public Spc() {
        this.id = -1;
        this.pessoa = new Pessoa();
        this.dtEntrada = new DataHoje().dataHoje();
        this.dtSaida = new DataHoje().dataHoje();
        this.observacao = "";
    }

    public Spc(int id, Pessoa pessoa, String dataEntrada, String dataSaida, String observacao) {
        this.id = id;
        this.pessoa = pessoa;
        this.dtEntrada = DataHoje.converte(dataEntrada);
        this.dtSaida = DataHoje.converte(dataSaida);
        this.observacao = observacao;
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

    public Date getDtEntrada() {
        return dtEntrada;
    }

    public void setDtEntrada(Date dtEntrada) {
        this.dtEntrada = dtEntrada;
    }

    public Date getDtSaida() {
        return dtSaida;
    }

    public void setDtSaida(Date dtSaida) {
        this.dtSaida = dtSaida;
    }

    public String getDataEntrada() {
        return DataHoje.converteData(dtEntrada);
    }

    public void setDataEntrada(String dataEntrada) {
        this.dtEntrada = DataHoje.converte(dataEntrada);
    }

    public String getDataSaida() {
        return DataHoje.converteData(dtSaida);
    }

    public void setDataSaida(String dataSaida) {
        this.dtSaida = DataHoje.converte(dataSaida);
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}