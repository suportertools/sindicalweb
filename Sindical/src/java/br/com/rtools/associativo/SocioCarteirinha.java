package br.com.rtools.associativo;

import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "soc_carteirinha")
@NamedQuery(name = "SocioCarteirinha.pesquisaID", query = "select sc from SocioCarteirinha sc where sc.id=:pid")
public class SocioCarteirinha implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_emissao")
    private Date dtEmissao;
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id")
    @ManyToOne
    private Pessoa pessoa;
    @JoinColumn(name = "id_modelo_carteirinha", referencedColumnName = "id")
    @ManyToOne
    private ModeloCarteirinha modeloCarteirinha;
    @Column(name = "nr_cartao")
    private Integer cartao;
    @Column(name = "nr_via")
    private int via;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_validade_carteirinha")
    private Date dtValidadeCarteirinha;
    @Column(name = "is_ativo", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean ativo;

    public SocioCarteirinha() {
        this.id = -1;
        this.setEmissao(DataHoje.data());
        this.pessoa = new Pessoa();
        this.modeloCarteirinha = new ModeloCarteirinha();
        this.cartao = 0;
        this.via = 0;
        this.setValidadeCarteirinha("");
        this.ativo = true;
    }

    public SocioCarteirinha(int id, String emissao, Pessoa pessoa, ModeloCarteirinha modeloCarteirinha, Integer cartao, int via, String validadeCarteirinha, boolean ativo) {
        this.id = id;
        this.setEmissao(emissao);
        this.pessoa = pessoa;
        this.modeloCarteirinha = modeloCarteirinha;
        this.cartao = cartao;
        this.via = via;
        this.setValidadeCarteirinha(validadeCarteirinha);
        this.ativo = ativo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDtEmissao() {
        return dtEmissao;
    }

    public void setDtEmissao(Date dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public String getEmissao() {
        if (dtEmissao != null) {
            return DataHoje.converteData(dtEmissao);
        } else {
            return "";
        }
    }

    public void setEmissao(String emissao) {
        if (!(emissao.isEmpty())) {
            this.dtEmissao = DataHoje.converte(emissao);
        }
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public ModeloCarteirinha getModeloCarteirinha() {
        return modeloCarteirinha;
    }

    public void setModeloCarteirinha(ModeloCarteirinha modeloCarteirinha) {
        this.modeloCarteirinha = modeloCarteirinha;
    }

    public Integer getCartao() {
        return cartao;
    }

    public void setCartao(Integer cartao) {
        this.cartao = cartao;
    }

    public int getVia() {
        return via;
    }

    public void setVia(int via) {
        this.via = via;
    }

    public Date getDtValidadeCarteirinha() {
        return dtValidadeCarteirinha;
    }

    public void setDtValidadeCarteirinha(Date dtValidadeCarteirinha) {
        this.dtValidadeCarteirinha = dtValidadeCarteirinha;
    }

    public String getValidadeCarteirinha() {
        if (dtValidadeCarteirinha != null) {
            return DataHoje.converteData(dtValidadeCarteirinha);
        } else {
            return "";
        }
    }

    public void setValidadeCarteirinha(String validadeCarteirinha) {
        if (!validadeCarteirinha.isEmpty()) {
            this.dtValidadeCarteirinha = DataHoje.converte(validadeCarteirinha);
        }
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

}
