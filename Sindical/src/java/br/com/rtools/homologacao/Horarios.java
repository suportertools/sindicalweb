package br.com.rtools.homologacao;

import br.com.rtools.pessoa.Filial;
import br.com.rtools.sistema.Semana;
import java.io.Serializable;
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

@Entity
@Table(name = "HOM_HORARIOS")
@NamedQueries({
    @NamedQuery(name = "Horarios.pesquisaID",    query = "SELECT H FROM Horarios AS H WHERE H.id = :pid"),
    @NamedQuery(name = "Horarios.findAll",       query = "SELECT H FROM Horarios AS H ORDER BY H.semana.id ASC, H.hora ASC ")
})
public class Horarios implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_HORA", length = 5, nullable = false)
    private String hora;
    @Column(name = "NR_QUANTIDADE", nullable = false)
    private int quantidade;
    @Column(name = "ATIVO", nullable = true)
    private boolean ativo;
    @JoinColumn(name = "ID_FILIAL", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private Filial filial;
    @JoinColumn(name = "ID_SEMANA", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private Semana semana;

    public Horarios() {
        id = -1;
        hora = "";
        quantidade = 0;
        ativo = true;
        filial = new Filial();
        semana = new Semana();
    }

    public Horarios(int id, String hora, int quantidade, boolean ativo, Filial filial, Semana semana) {
        this.id = id;
        this.hora = hora;
        this.quantidade = quantidade;
        this.ativo = ativo;
        this.filial = filial;
        this.semana = semana;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public Semana getSemana() {
        return semana;
    }

    public void setSemana(Semana semana) {
        this.semana = semana;
    }
}
