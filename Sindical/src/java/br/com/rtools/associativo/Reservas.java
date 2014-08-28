package br.com.rtools.associativo;

import br.com.rtools.pessoa.Pessoa;
import javax.persistence.*;

@Entity
@Table(name = "car_reservas")
@NamedQuery(name = "Reservas.pesquisaID", query = "select r from Reservas r where r.id=:pid")
public class Reservas implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_cvenda", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private CVenda cVenda;
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Pessoa pessoa;
    @Column(name = "nr_poltrona", nullable = true)
    private int poltrona;
    @Column(name = "nr_desconto", nullable = true)
    private float desconto;
    @JoinColumn(name = "id_evento_servico", referencedColumnName = "id")
    @OneToOne
    private EventoServico eventoServico;

    public Reservas() {
        this.id = -1;
        this.cVenda = new CVenda();
        this.pessoa = new Pessoa();
        this.poltrona = 0;
        this.desconto = 0;
        this.eventoServico = new EventoServico();
    }

    public Reservas(int id, CVenda cVenda, Pessoa pessoa, int poltrona, float desconto, EventoServico eventoServico) {
        this.id = id;
        this.cVenda = cVenda;
        this.pessoa = pessoa;
        this.poltrona = poltrona;
        this.desconto = desconto;
        this.eventoServico = eventoServico;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CVenda getcVenda() {
        return cVenda;
    }

    public void setcVenda(CVenda cVenda) {
        this.cVenda = cVenda;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public int getPoltrona() {
        return poltrona;
    }

    public void setPoltrona(int poltrona) {
        this.poltrona = poltrona;
    }

    public float getDesconto() {
        return desconto;
    }

    public void setDesconto(float desconto) {
        this.desconto = desconto;
    }

    public EventoServico getEventoServico() {
        return eventoServico;
    }

    public void setEventoServico(EventoServico eventoServico) {
        this.eventoServico = eventoServico;
    }
}
