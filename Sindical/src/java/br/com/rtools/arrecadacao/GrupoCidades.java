

package br.com.rtools.arrecadacao;

import br.com.rtools.endereco.Cidade;
import javax.persistence.*;

@Entity
@Table(name="ARR_GRUPO_CIDADES")
@NamedQuery(name="GrupoCidades.pesquisaID", query="select c from GrupoCidades c where c.id=:pid")
public class GrupoCidades implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name="ID_CIDADE", referencedColumnName="ID", nullable=false)
    @ManyToOne
    private Cidade cidade;
    @JoinColumn(name="ID_GRUPO_CIDADE", referencedColumnName="ID", nullable=false)
    @ManyToOne
    private GrupoCidade grupoCidade;

    public GrupoCidades() {
        this.id = -1;
        this.cidade = new Cidade();
        this.grupoCidade = new GrupoCidade();
    }

    public GrupoCidades(int id, Cidade cidade, GrupoCidade grupoCidade) {
        this.id = id;
        this.cidade = cidade;
        this.grupoCidade = grupoCidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public GrupoCidade getGrupoCidade() {
        return grupoCidade;
    }

    public void setGrupoCidade(GrupoCidade grupoCidade) {
        this.grupoCidade = grupoCidade;
    }
  
}
