package br.com.rtools.financeiro;

import br.com.rtools.sistema.Cor;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "FIN_PRODUTO")
@NamedQuery(name = "Produto.pesquisaID", query = "SELECT P FROM Produto AS P WHERE P.id = :pid")
public class Produto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "DS_DESCRICAO", length = 100, nullable = false)
    private String descricao;
    @Column(name = "DS_MARCA", length = 100)
    private String marca;
    @Column(name = "DS_FABRICANTE", length = 100)
    private String fabricante;
    @Column(name = "DS_SABOR", length = 100)
    private String sabor;
    @Column(name = "DS_MEDIDA", length = 25)
    private String medida;
    @Column(name = "DS_BARRAS", length = 25)
    private String barras;
    @Column(name = "NR_QTDE_EMBALAGEM")
    private int quantidadeEmbalagem;
    @Column(name = "NR_ESTOQUE")
    private int estoque;
    @Column(name = "NR_ESTOQUE_MINIMO")
    private int estoqueMinimo;
    @Column(name = "NR_ESTOQUE_MAXIMO")
    private int estoqueMaximo;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_CADASTRO")
    private Date cadastro;
    @JoinColumn(name = "ID_GRUPO", referencedColumnName = "ID", nullable = false)
    @OneToOne
    private ProdutoGrupo produtoGrupo;
    @JoinColumn(name = "ID_SUBGRUPO", referencedColumnName = "ID")
    @OneToOne
    private ProdutoSubGrupo produtoSubGrupo;
    @JoinColumn(name = "ID_UNIDADE", referencedColumnName = "ID", nullable = false)
    @OneToOne
    private ProdutoUnidade produtoUnidade;
    @JoinColumn(name = "ID_COR", referencedColumnName = "ID", nullable = false)
    @OneToOne
    private Cor cor;

    public Produto() {
    }

    public Produto(long id, String descricao, String marca, String fabricante, String sabor, String medida, String barras, int quantidadeEmbalagem, int estoque, int estoqueMinimo, int estoqueMaximo, Date cadastro, ProdutoGrupo produtoGrupo, ProdutoSubGrupo produtoSubGrupo, ProdutoUnidade produtoUnidade, Cor cor) {
        this.id = id;
        this.descricao = descricao;
        this.marca = marca;
        this.fabricante = fabricante;
        this.sabor = sabor;
        this.medida = medida;
        this.barras = barras;
        this.quantidadeEmbalagem = quantidadeEmbalagem;
        this.estoque = estoque;
        this.estoqueMinimo = estoqueMinimo;
        this.estoqueMaximo = estoqueMaximo;
        this.cadastro = cadastro;
        this.produtoGrupo = produtoGrupo;
        this.produtoSubGrupo = produtoSubGrupo;
        this.produtoUnidade = produtoUnidade;
        this.cor = cor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getSabor() {
        return sabor;
    }

    public void setSabor(String sabor) {
        this.sabor = sabor;
    }

    public String getMedida() {
        return medida;
    }

    public void setMedida(String medida) {
        this.medida = medida;
    }

    public String getBarras() {
        return barras;
    }

    public void setBarras(String barras) {
        this.barras = barras;
    }

    public int getQuantidadeEmbalagem() {
        return quantidadeEmbalagem;
    }

    public void setQuantidadeEmbalagem(int quantidadeEmbalagem) {
        this.quantidadeEmbalagem = quantidadeEmbalagem;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    public int getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(int estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public int getEstoqueMaximo() {
        return estoqueMaximo;
    }

    public void setEstoqueMaximo(int estoqueMaximo) {
        this.estoqueMaximo = estoqueMaximo;
    }

    public Date getCadastro() {
        return cadastro;
    }

    public void setCadastro(Date cadastro) {
        this.cadastro = cadastro;
    }

    public ProdutoGrupo getProdutoGrupo() {
        return produtoGrupo;
    }

    public void setProdutoGrupo(ProdutoGrupo produtoGrupo) {
        this.produtoGrupo = produtoGrupo;
    }

    public ProdutoSubGrupo getProdutoSubGrupo() {
        return produtoSubGrupo;
    }

    public void setProdutoSubGrupo(ProdutoSubGrupo produtoSubGrupo) {
        this.produtoSubGrupo = produtoSubGrupo;
    }

    public ProdutoUnidade getProdutoUnidade() {
        return produtoUnidade;
    }

    public void setProdutoUnidade(ProdutoUnidade produtoUnidade) {
        this.produtoUnidade = produtoUnidade;
    }

    public Cor getCor() {
        return cor;
    }

    public void setCor(Cor cor) {
        this.cor = cor;
    }
}
