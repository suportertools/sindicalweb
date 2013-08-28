package br.com.rtools.associativo;

import javax.persistence.*;

@Entity
@Table(name="EVE_EVENTO")
@NamedQuery(name="AEvento.pesquisaID", query="select s from AEvento s where s.id=:pid")
public class AEvento implements java.io.Serializable{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name="ID_DESCRICAO_EVENTO", referencedColumnName="ID",  nullable=true)
    @OneToOne
    private DescricaoEvento descricaoEvento;

    public AEvento() {
        this.id = -1;
        this.descricaoEvento = new DescricaoEvento();
    }

    public AEvento(int id, DescricaoEvento descricaoEvento) {
        this.id = id;
        this.descricaoEvento = descricaoEvento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DescricaoEvento getDescricaoEvento() {
        return descricaoEvento;
    }

    public void setDescricaoEvento(DescricaoEvento descricaoEvento) {
        this.descricaoEvento = descricaoEvento;
    }

    



}