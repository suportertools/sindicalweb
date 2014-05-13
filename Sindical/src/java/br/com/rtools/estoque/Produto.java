package br.com.rtools.estoque;

import br.com.rtools.sistema.Cor;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Moeda;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "EST_PRODUTO")
public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_DESCRICAO", length = 100, nullable = false)
    private String descricao;
    @Column(name = "DS_MODELO", length = 100)
    private String modelo;
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
    @Column(name = "DS_OBS", length = 5000)
    private String observacao;
    @Column(name = "NR_QTDE_EMBALAGEM", columnDefinition = "INTEGER DEFAULT 0")
    private int quantidadeEmbalagem;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_CADASTRO")
    private Date dtCadastro;
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
    @Column(name = "NR_VALOR", columnDefinition = "DOUBLE PRECISION DEFAULT 0", nullable = true)
    private float valor;

    public Produto() {
        this.id = -1;
        this.descricao = "";
        this.modelo = "";
        this.marca = "";
        this.fabricante = "";
        this.sabor = "";
        this.medida = "";
        this.barras = "";
        this.observacao = "";
        this.quantidadeEmbalagem = 0;
        this.dtCadastro = new Date();
        this.produtoGrupo = new ProdutoGrupo();
        this.produtoSubGrupo = new ProdutoSubGrupo();
        this.produtoUnidade = new ProdutoUnidade();
        this.cor = new Cor();
        this.valor = 0;
    }

    public Produto(int id, String descricao, String modelo, String marca, String fabricante, String sabor, String medida, String observacao, String barras, int quantidadeEmbalagem, String cadastro, ProdutoGrupo produtoGrupo, ProdutoSubGrupo produtoSubGrupo, ProdutoUnidade produtoUnidade, Cor cor, float valor) {
        this.id = id;
        this.descricao = descricao;
        this.modelo = modelo;
        this.marca = marca;
        this.fabricante = fabricante;
        this.sabor = sabor;
        this.medida = medida;
        this.observacao = observacao;
        this.barras = barras;
        this.quantidadeEmbalagem = quantidadeEmbalagem;
        this.dtCadastro = DataHoje.converte(cadastro);
        this.produtoGrupo = produtoGrupo;
        this.produtoSubGrupo = produtoSubGrupo;
        this.produtoUnidade = produtoUnidade;
        this.cor = cor;
        this.valor = valor;
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

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
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

    public Date geDttCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(Date dtCadastro) {
        this.dtCadastro = dtCadastro;
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

    public String getCadastro() {
        return DataHoje.converteData(dtCadastro);
    }

    public void setCadastro(String cadastro) {
        this.dtCadastro = DataHoje.converte(cadastro);
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @Override
    public String toString() {
        return "Produto{" + "id=" + id + ", descricao=" + descricao + ", modelo=" + modelo + ", marca=" + marca + ", fabricante=" + fabricante + ", sabor=" + sabor + ", medida=" + medida + ", barras=" + barras + ", observacao=" + observacao + ", quantidadeEmbalagem=" + quantidadeEmbalagem + ", dtCadastro=" + dtCadastro + ", produtoGrupo=" + produtoGrupo + ", produtoSubGrupo=" + produtoSubGrupo + ", produtoUnidade=" + produtoUnidade + ", cor=" + cor + '}';
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
        this.valor = Moeda.converteUS$(valor);
    }
}
