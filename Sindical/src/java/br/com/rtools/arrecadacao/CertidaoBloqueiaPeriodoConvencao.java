package br.com.rtools.arrecadacao;

import br.com.rtools.endereco.Cidade;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "arr_certidao_bloqueia_periodo_convencao")
@NamedQuery(name = "CertidaoBloqueiaPeriodoConvencao.pesquisaID", query = "select c from CertidaoBloqueiaPeriodoConvencao c where c.id = :pid")
public class CertidaoBloqueiaPeriodoConvencao implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_certidao_tipo", referencedColumnName = "id")
    @ManyToOne
    private CertidaoTipo certidaoTipo;
    @JoinColumn(name = "id_cidade", referencedColumnName = "id")
    @ManyToOne
    private Cidade cidade;
    @Column(name = "is_ativo", columnDefinition = "boolean default true")
    private boolean ativo;

    public CertidaoBloqueiaPeriodoConvencao() {
        this.id = -1;
        this.certidaoTipo = new CertidaoTipo();
        this.cidade = new Cidade();
        this.ativo = true;
    }
    
    public CertidaoBloqueiaPeriodoConvencao(int id, CertidaoTipo certidaoTipo, Cidade cidade, boolean ativo) {
        this.id = id;
        this.certidaoTipo = certidaoTipo;
        this.cidade = cidade;
        this.ativo = ativo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CertidaoTipo getCertidaoTipo() {
        return certidaoTipo;
    }

    public void setCertidaoTipo(CertidaoTipo certidaoTipo) {
        this.certidaoTipo = certidaoTipo;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
