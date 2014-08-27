package br.com.rtools.financeiro;

import br.com.rtools.seguranca.Rotina;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "fin_servico_rotina")
@NamedQuery(name = "ServicoRotina.pesquisaID", query = "select s from ServicoRotina s where s.id=:pid")
public class ServicoRotina implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_servicos", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Servicos servicos;
    @JoinColumn(name = "id_rotina", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Rotina rotina;

    public ServicoRotina() {
        this.id = -1;
        this.servicos = new Servicos();
        this.rotina = new Rotina();
    }

    public ServicoRotina(int id, Servicos servicos, Rotina rotina) {
        this.id = id;
        this.servicos = servicos;
        this.rotina = rotina;
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

    public Rotina getRotina() {
        return rotina;
    }

    public void setRotina(Rotina rotina) {
        this.rotina = rotina;
    }
}
