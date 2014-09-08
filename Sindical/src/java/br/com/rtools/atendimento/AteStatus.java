package br.com.rtools.atendimento;

import javax.persistence.*;

@Entity
@Table(name = "ate_status")
@NamedQuery(name = "AteStatus.pesquisaID", query = "select ats from AteStatus ats where ats.id = :pid")
public class AteStatus implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 50, nullable = false, unique = true)
    private String descricao;
    @Column(name = "is_reserva", columnDefinition = "boolean default false")
    private boolean reserva;
    
    public AteStatus() {
        this.id = -1;
        this.descricao = "";
        this.reserva = false;
    }

    public AteStatus(int id, String descricao, boolean reserva) {
        this.id = id;
        this.descricao = descricao;
        this.reserva = reserva;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isReserva() {
        return reserva;
    }

    public void setReserva(boolean reserva) {
        this.reserva = reserva;
    }
}
