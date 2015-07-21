package br.com.rtools.seguranca;

import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "seg_rotina_contador",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_rotina_combo", "id_rotina_tela", "id_usuario"})
)
public class RotinaContador implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_rotina_tela", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Rotina rotinaTela;
    @JoinColumn(name = "id_rotina_combo", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Rotina rotinaCombo;
    @Column(name = "nr_contador")
    private Integer contador;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", nullable = true)
    @ManyToOne
    private Usuario usuario;
    @Column(name = "dt_data")
    @Temporal(TemporalType.DATE)
    private Date data;

    public RotinaContador() {
        this.id = null;
        this.rotinaTela = new Rotina();
        this.rotinaCombo = new Rotina();
        this.contador = 0;
        this.usuario = null;
        this.data = DataHoje.dataHoje();
    }

    public RotinaContador(Integer id, Rotina rotinaTela, Rotina rotinaCombo, Integer contador, Usuario usuario) {
        this.id = id;
        this.rotinaTela = rotinaTela;
        this.rotinaCombo = rotinaCombo;
        this.contador = contador;
        this.usuario = usuario;
    }

    public RotinaContador(Integer id, Rotina rotinaTela, Rotina rotinaCombo, String dataString, Usuario usuario) {
        this.id = id;
        this.rotinaTela = rotinaTela;
        this.rotinaCombo = rotinaCombo;
        this.data = DataHoje.converte(dataString);
        this.usuario = usuario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Rotina getRotinaTela() {
        return rotinaTela;
    }

    public void setRotinaTela(Rotina rotinaTela) {
        this.rotinaTela = rotinaTela;
    }

    public Rotina getRotinaCombo() {
        return rotinaCombo;
    }

    public void setRotinaCombo(Rotina rotinaCombo) {
        this.rotinaCombo = rotinaCombo;
    }

    public Integer getContador() {
        return contador;
    }

    public void setContador(Integer contador) {
        this.contador = contador;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RotinaContador other = (RotinaContador) obj;
        return true;
    }

    @Override
    public String toString() {
        return "RotinaContador{" + "id=" + id + ", rotinaTela=" + rotinaTela + ", rotinaCombo=" + rotinaCombo + ", contador=" + contador + ", usuario=" + usuario + ", data=" + data + '}';
    }

}
