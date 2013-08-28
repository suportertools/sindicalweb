
package br.com.rtools.arrecadacao;

import br.com.rtools.financeiro.Movimento;
import javax.persistence.*;

@Entity
@Table(name="ARR_ACORDO_MOVIMENTOS")
@NamedQuery(name="AcordoMovimentos.pesquisaID", query="select c from AcordoMovimentos c where c.id = :pid")
public class AcordoMovimentos implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name="ID_MOVIMENTO", referencedColumnName="ID", nullable=false)
    @ManyToOne
    private Movimento movimento;
    @JoinColumn(name="ID_ACORDO", referencedColumnName="ID", nullable=false)
    @ManyToOne
    private Acordo acordo;
    @Column(name="BO_ACORDADO", nullable=false)
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