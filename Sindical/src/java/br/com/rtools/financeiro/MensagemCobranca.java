package br.com.rtools.financeiro;

import br.com.rtools.arrecadacao.MensagemConvencao;
import javax.persistence.*;

@Entity
@Table(name = "fin_mensagem_cobranca")
@NamedQuery(name = "MensagemCobranca.pesquisaID", query = "select m from MensagemCobranca m where m.id=:pid")
public class MensagemCobranca implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_movimento", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Movimento movimento;
    @JoinColumn(name = "id_mensagem_convencao", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private MensagemConvencao mensagemConvencao;

    public MensagemCobranca() {
        this.id = -1;
        this.movimento = new Movimento();
        this.mensagemConvencao = new MensagemConvencao();
    }

    public MensagemCobranca(int id, Movimento movimento, MensagemConvencao mensagemConvencao) {
        this.id = id;
        this.movimento = movimento;
        this.mensagemConvencao = mensagemConvencao;
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

    public MensagemConvencao getMensagemConvencao() {
        return mensagemConvencao;
    }

    public void setMensagemConvencao(MensagemConvencao mensagemConvencao) {
        this.mensagemConvencao = mensagemConvencao;
    }
}
