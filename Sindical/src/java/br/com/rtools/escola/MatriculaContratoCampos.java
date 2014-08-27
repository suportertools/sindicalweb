package br.com.rtools.escola;

import br.com.rtools.seguranca.Modulo;
import javax.persistence.*;

@Entity
@Table(name = "matr_contrato_campos")
@NamedQueries({
    @NamedQuery(name = "MatriculaContratoCampos.pesquisaID", query = "SELECT MCC FROM MatriculaContratoCampos AS MCC WHERE MCC.id = :pid"),
    @NamedQuery(name = "MatriculaContratoCampos.findAll", query = "SELECT MCC FROM MatriculaContratoCampos AS MCC ORDER BY MCC.modulo.descricao ASC, MCC.campo ASC, MCC.variavel ASC")
})
public class MatriculaContratoCampos implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_modulo", referencedColumnName = "id")
    @ManyToOne
    private Modulo modulo;
    @Column(name = "ds_campo", length = 100)
    private String campo;
    @Column(name = "ds_variavel")
    private String variavel;
    @Column(name = "ds_observacao", length = 1500)
    private String observacao;

    public MatriculaContratoCampos() {
        this.id = -1;
        this.modulo = new Modulo();
        this.campo = "";
        this.variavel = "";
        this.observacao = "";
    }

    public MatriculaContratoCampos(int id, Modulo modulo, String campo, String variavel, String observacao) {
        this.id = id;
        this.modulo = modulo;
        this.campo = campo;
        this.variavel = variavel;
        this.observacao = observacao;
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

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getVariavel() {
        return variavel;
    }

    public void setVariavel(String variavel) {
        this.variavel = variavel;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
