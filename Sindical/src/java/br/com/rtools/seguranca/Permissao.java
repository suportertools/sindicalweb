package br.com.rtools.seguranca;

import javax.persistence.*;

@Entity
@Table(name="SEG_PERMISSAO")
@NamedQuery(name="Permissao.pesquisaID", query="select per from Permissao per where per.id=:pid")
public class Permissao implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name="ID_MODULO", referencedColumnName="ID", nullable=false)
    @ManyToOne
    private Modulo modulo;
    @JoinColumn(name="ID_ROTINA", referencedColumnName="ID", nullable=false)
    @ManyToOne
    private Rotina rotina;
    @JoinColumn(name="ID_EVENTO", referencedColumnName="ID", nullable=false)
    @ManyToOne
    private Evento evento;

    public Permissao() {
        this.id = -1;
        this.modulo = new Modulo();
        this.rotina = new Rotina();
        this.evento = new Evento();
    }

    public Permissao(int id, Modulo modulo, Rotina rotina, Evento evento) {
        this.id = id;
        this.modulo = modulo;
        this.rotina = rotina;
        this.evento = evento;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public Rotina getRotina() {
        return rotina;
    }

    public void setRotina(Rotina rotina) {
        this.rotina = rotina;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }
}