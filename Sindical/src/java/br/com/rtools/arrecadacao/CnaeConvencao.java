package br.com.rtools.arrecadacao;

import br.com.rtools.pessoa.Cnae;
import javax.persistence.*;

@Entity
@Table(name = "ARR_CNAE_CONVENCAO")
@NamedQuery(name = "CnaeConvencao.pesquisaID", query = "select cc from CnaeConvencao cc where cc.id = :pid")
public class CnaeConvencao implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_CNAE", referencedColumnName = "ID", nullable = false)
    @OneToOne
    private Cnae cnae;
    @JoinColumn(name = "ID_CONVENCAO", referencedColumnName = "ID", nullable = false)
    @OneToOne
    private Convencao convencao;

    public CnaeConvencao() {
        this.id = -1;
        this.cnae = new Cnae();
        this.convencao = new Convencao();
    }

    public CnaeConvencao(int id, Cnae cnae, Convencao convencao) {
        this.id = id;
        this.cnae = cnae;
        this.convencao = convencao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cnae getCnae() {
        return cnae;
    }

    public void setCnae(Cnae cnae) {
        this.cnae = cnae;
    }

    public Convencao getConvencao() {
        return convencao;
    }

    public void setConvencao(Convencao convencao) {
        this.convencao = convencao;
    }
}