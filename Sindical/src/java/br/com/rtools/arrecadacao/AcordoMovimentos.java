package br.com.rtools.arrecadacao;

import br.com.rtools.financeiro.Movimento;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "arr_acordo_movimentos")
@NamedQuery(name = "AcordoMovimentos.pesquisaID", query = "select c from AcordoMovimentos c where c.id = :pid")
public class AcordoMovimentos implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_movimento", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Movimento movimento;
    @JoinColumn(name = "id_acordo", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Acordo acordo;
    @Column(name = "bo_acordado", nullable = false)
    private boolean acordado;

    public AcordoMovimentos() {
        this.id = -1;
        this.movimento = new Movimento();
        this.acordo = new Acordo();
        this.acordado = false;
    }

    public AcordoMovimentos(int id, Movimento movimento, Acordo acordo, boolean acordado) {
        this.id = id;
        this.movimento = movimento;
        this.acordo = acordo;
        this.acordado = acordado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Movimento getMovimento() {
        return movimento;
    }

    public void setMovimento(Movimento movimento) {
        this.movimento = movimento;
    }

    public Acordo getAcordo() {
        return acordo;
    }

    public void setAcordo(Acordo acordo) {
        this.acordo = acordo;
    }

    public boolean isAcordado() {
        return acordado;
    }

    public void setAcordado(boolean acordado) {
        this.acordado = acordado;
    }
}
