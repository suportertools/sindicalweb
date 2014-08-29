package br.com.rtools.associativo;

import br.com.rtools.financeiro.Servicos;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "conv_servico")
@NamedQueries({
    @NamedQuery(name = "ConviteServico.pesquisaID", query = "SELECT CONS FROM ConviteServico AS CONS WHERE CONS.id = :pid"),
    @NamedQuery(name = "ConviteServico.findAll", query = "SELECT CONS FROM ConviteServico AS CONS ORDER BY CONS.servicos.descricao ASC ")
})

public class ConviteServico implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_servico", referencedColumnName = "id")
    @ManyToOne
    private Servicos servicos;
    @Column(name = "is_segunda", columnDefinition = "boolean default false")
    private boolean segunda;
    @Column(name = "is_terca", columnDefinition = "boolean default false")
    private boolean terca;
    @Column(name = "is_quarta", columnDefinition = "boolean default false")
    private boolean quarta;
    @Column(name = "is_quinta", columnDefinition = "boolean default false")
    private boolean quinta;
    @Column(name = "is_sexta", columnDefinition = "boolean default false")
    private boolean sexta;
    @Column(name = "is_sabado", columnDefinition = "boolean default false")
    private boolean sabado;
    @Column(name = "is_domingo", columnDefinition = "boolean default false")
    private boolean domingo;
    @Column(name = "is_feriado", columnDefinition = "boolean default false")
    private boolean feriado;

    public ConviteServico() {
        this.id = -1;
        this.servicos = new Servicos();
        this.segunda = false;
        this.terca = false;
        this.quarta = false;
        this.quinta = false;
        this.sexta = false;
        this.sabado = false;
        this.domingo = false;
        this.feriado = false;
    }

    public ConviteServico(int id, Servicos servicos, boolean segunda, boolean terca, boolean quarta, boolean quinta, boolean sexta, boolean sabado, boolean domingo, boolean feriado) {
        this.id = id;
        this.servicos = servicos;
        this.segunda = segunda;
        this.terca = terca;
        this.quarta = quarta;
        this.quinta = quinta;
        this.sexta = sexta;
        this.sabado = sabado;
        this.domingo = domingo;
        this.feriado = feriado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Servicos getServicos() {
        return servicos;
    }

    public void setServicos(Servicos servicos) {
        this.servicos = servicos;
    }

    public boolean isSegunda() {
        return segunda;
    }

    public void setSegunda(boolean segunda) {
        this.segunda = segunda;
    }

    public boolean isTerca() {
        return terca;
    }

    public void setTerca(boolean terca) {
        this.terca = terca;
    }

    public boolean isQuarta() {
        return quarta;
    }

    public void setQuarta(boolean quarta) {
        this.quarta = quarta;
    }

    public boolean isQuinta() {
        return quinta;
    }

    public void setQuinta(boolean quinta) {
        this.quinta = quinta;
    }

    public boolean isSexta() {
        return sexta;
    }

    public void setSexta(boolean sexta) {
        this.sexta = sexta;
    }

    public boolean isSabado() {
        return sabado;
    }

    public void setSabado(boolean sabado) {
        this.sabado = sabado;
    }

    public boolean isDomingo() {
        return domingo;
    }

    public void setDomingo(boolean domingo) {
        this.domingo = domingo;
    }

    public boolean isFeriado() {
        return feriado;
    }

    public void setFeriado(boolean feriado) {
        this.feriado = feriado;
    }
    
    @Override
    public String toString() {
        return "ConviteServico{" + "id=" + id + ", servicos=" + servicos + ", segunda=" + segunda + ", terca=" + terca + ", quarta=" + quarta + ", quinta=" + quinta + ", sexta=" + sexta + ", sabado=" + sabado + ", domingo=" + domingo + ", feriado=" + feriado + '}';
    }    

}
