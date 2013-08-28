package br.com.rtools.associativo;

import br.com.rtools.utilitarios.Moeda;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="EVE_EVENTO_SERVICO_VALOR")
@NamedQuery(name="EventoServicoValor.pesquisaID", query="select es from EventoServicoValor es where es.id=:pid")
public class EventoServicoValor implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name="ID_EVENTO_SERVICO", referencedColumnName="ID", nullable=false)
    @OneToOne
    private EventoServico eventoServico;
    @Column(name="NR_VALOR", nullable=true)
    private float valor;
    @Column(name="NR_IDADE_INICIAL", nullable=false)
    private int idadeInicial;
    @Column(name="NR_IDADE_FINAL", nullable=false)
    private int idadeFinal;
    @Column(name="DS_SEXO", nullable=true, length=1)
    private String sexo;

    public EventoServicoValor() {
        this.id = -1;
        this.eventoServico = new EventoServico();
        this.valor = 0;
        this.idadeInicial = 0;
        this.idadeFinal = 0;
        this.sexo = "A";
    }

    public EventoServicoValor(int id, EventoServico eventoServico, float valor, int idadeInicial, int idadeFinal, String sexo) {
        this.id = id;
        this.eventoServico = eventoServico;
        this.valor = valor;
        this.idadeInicial = idadeInicial;
        this.idadeFinal = idadeFinal;
        this.sexo = sexo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EventoServico getEventoServico() {
        return eventoServico;
    }

    public void setEventoServico(EventoServico eventoServico) {
        this.eventoServico = eventoServico;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getValorString() {
        return Moeda.converteR$Float(valor);
    }

    public void setValorString(String valor) {
        this.valor = Moeda.substituiVirgulaFloat(valor);
    }

    public int getIdadeInicial() {
        return idadeInicial;
    }

    public void setIdadeInicial(int idadeInicial) {
        this.idadeInicial = idadeInicial;
    }

    public int getIdadeFinal() {
        return idadeFinal;
    }

    public void setIdadeFinal(int idadeFinal) {
        this.idadeFinal = idadeFinal;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    

}
