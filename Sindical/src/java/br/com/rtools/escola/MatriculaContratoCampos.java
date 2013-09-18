package br.com.rtools.escola;

import br.com.rtools.seguranca.Modulo;
import javax.persistence.*;

@Entity
@Table(name = "MATR_CONTRATO_CAMPOS")
@NamedQuery(name = "MatriculaContratoCampos.pesquisaID", query = "SELECT MCC FROM MatriculaContratoCampos AS MCC WHERE MCC.id=:pid")
public class MatriculaContratoCampos implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_MODULO", referencedColumnName = "ID")
    @ManyToOne
    private Modulo modulo;
    @Column(name = "DS_CAMPO", length = 100)
    private String campo;
    @Column(name = "DS_VARIAVEL")
    private String variavel;
    @Column(name = "DS_OBSERVACAO", length = 1500)
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