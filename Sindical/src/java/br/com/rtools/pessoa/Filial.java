package br.com.rtools.pessoa;

import br.com.rtools.utilitarios.BaseEntity;
import java.io.Serializable;
import javax.persistence.*;

/**
 * <p>
 * <strong>Filial</strong></p>
 * <p>
 * <strong>Definição:</strong> Estabelecimento dependente de outro.</p>
 * <p>
 * <strong>Importante:</strong> Utilizar filiais somente caso houver um
 * endereçamento diferente da sede!</p>
 *
 * @author rtools
 */
@Entity
@Table(name = "PES_FILIAL")
@NamedQueries({
    @NamedQuery(name = "Filial.pesquisaID", query = "SELECT FIL FROM Filial AS FIL WHERE FIL.id = :pid"),
    @NamedQuery(name = "Filial.findAll", query = "SELECT FIL FROM Filial AS FIL ORDER BY FIL.filial.pessoa.nome ASC ")
})
public class Filial implements BaseEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_MATRIZ", referencedColumnName = "ID", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private Juridica matriz;
    @JoinColumn(name = "ID_FILIAL", referencedColumnName = "ID", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Juridica filial;
    @Column(name = "NR_CENTRO_CUSTO")
    private int centroCusto;

    public Filial() {
        this.id = -1;
        this.matriz = new Juridica();
        this.filial = new Juridica();
        this.centroCusto = 0;
    }

    public Filial(int id, Juridica matriz, Juridica filial, int centroCusto) {
        this.id = id;
        this.matriz = matriz;
        this.filial = filial;
        this.centroCusto = centroCusto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Juridica getMatriz() {
        return matriz;
    }

    public void setMatriz(Juridica matriz) {
        this.matriz = matriz;
    }

    public Juridica getFilial() {
        return filial;
    }

    public void setFilial(Juridica filial) {
        this.filial = filial;
    }

    public int getCentroCusto() {
        return centroCusto;
    }

    public void setCentroCusto(int centroCusto) {
        this.centroCusto = centroCusto;
    }

    @Override
    public String toString() {
        return "Filial{" + "id=" + id + ", matriz=" + matriz.getId() + ", filial=" + filial.getId() + ", centroCusto=" + centroCusto + '}';
    }
}
