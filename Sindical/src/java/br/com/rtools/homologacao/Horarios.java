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
@Table(name = "hom_horarios")
@NamedQueries({
    @NamedQuery(name = "Horarios.pesquisaID", query = "SELECT H FROM Horarios AS H WHERE H.id = :pid"),
    @NamedQuery(name = "Horarios.findAll", query = "SELECT H FROM Horarios AS H ORDER BY H.semana.id ASC, H.hora ASC ")
})
public class Horarios implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_hora", length = 5, nullable = false)
    private String hora;
    @Column(name = "nr_quantidade", nullable = false)
    private int quantidade;
    @Column(name = "ativo", nullable = true)
    private boolean ativo;
    @JoinColumn(name = "id_filial", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Filial filial;
    @JoinColumn(name = "id_semana", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Semana semana;
    @Column(name = "is_web", columnDefinition = "BOOLEAN DEFAULT TRUE", nullable = false)
    private Boolean web;

    public Horarios() {
        id = -1;
        hora = "";
        quantidade = 0;
        ativo = true;
        filial = new Filial();
        semana = new Semana();
        web = true;
    }

    public Horarios(int id, String hora, int quantidade, boolean ativo, Filial filial, Semana semana, Boolean web) {
        this.id = id;
        this.hora = hora;
        this.quantidade = quantidade;
        this.ativo = ativo;
        this.filial = filial;
        this.semana = semana;
        this.web = web;
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

    public Boolean getWeb() {
        return web;
    }

    public void setWeb(Boolean web) {
        this.web = web;
    }
}
