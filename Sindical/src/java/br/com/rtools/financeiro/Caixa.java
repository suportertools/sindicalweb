package br.com.rtools.financeiro;

import br.com.rtools.pessoa.Filial;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.Moeda;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * <p>
 * <strong>Caixa</strong></p>
 * <p>
 * <strong>Detalhes:</strong>Define a filial, número e detalhes dos caixa que
 * poderão ser atribuídos a um computador da filial específica.</p>
 * <p>
 * <strong>Importante:</strong> Utilizar filiais somente caso houver um
 * endereçamento diferente da sede!</p>
 *
 * @author rtools
 */
@Entity
@Table(name = "fin_caixa",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_filial", "nr_caixa", "ds_descricao"})
)
@NamedQueries({
    @NamedQuery(name = "Caixa.pesquisaID", query = "SELECT C FROM Caixa AS C WHERE C.id = :pid"),
    @NamedQuery(name = "Caixa.findAll", query = "SELECT C FROM Caixa AS C ORDER BY C.caixa ASC, C.descricao ASC ")
})
public class Caixa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "nr_caixa")
    private int caixa;
    @Column(name = "ds_descricao", length = 100)
    private String descricao;
    @JoinColumn(name = "id_filial", referencedColumnName = "id")
    @ManyToOne
    private Filial filial;
    @Column(name = "nr_fundo_fixo")
    private float fundoFixo;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    @ManyToOne
    private Usuario usuario;

    public Caixa() {
        this.id = -1;
        this.caixa = 0;
        this.descricao = "";
        this.filial = new Filial();
        this.fundoFixo = 0;
        this.usuario = null;
    }


    public Caixa(int id, int caixa, String descricao, Filial filial, float fundoFixo, Usuario usuario) {
        this.id = id;
        this.caixa = caixa;
        this.descricao = descricao;
        this.filial = filial;
        this.fundoFixo = fundoFixo;
        this.usuario = usuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCaixa() {
        return caixa;
    }

    public void setCaixa(int caixa) {
        this.caixa = caixa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public float getFundoFixo() {
        return fundoFixo;
    }

    public void setFundoFixo(float fundoFixo) {
        this.fundoFixo = fundoFixo;
    }
    
    public String getFundoFixoString() {
        return Moeda.converteR$Float(fundoFixo);
    }

    public void setFundoFixoString(String fundoFixoString) {
        this.fundoFixo = Moeda.converteUS$(fundoFixoString);
    }    

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
