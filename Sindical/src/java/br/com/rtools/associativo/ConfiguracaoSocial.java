package br.com.rtools.associativo;

import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "conf_social")
public class ConfiguracaoSocial implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "nr_dias_inativa_demissionado")
    private int diasInativaDemissionado;
    @Column(name = "dt_inativa_demissionado")
    @Temporal(TemporalType.DATE)
    private Date dataInativacaoDemissionado;
    @JoinColumn(name = "id_grupo_categoria_inativa_demissionado", referencedColumnName = "id")
    @ManyToOne
    private GrupoCategoria grupoCategoriaInativaDemissionado;
    @Column(name = "is_inativa_demissionado", columnDefinition = "boolean default false")
    private boolean inativaDemissionado;
    @Column(name = "is_recebe_atrasado", columnDefinition = "boolean default false")
    private boolean recebeAtrasado;
    @Column(name = "is_controla_cartao_filial", columnDefinition = "boolean default false")
    private boolean controlaCartaoFilial;
    @Column(name = "nr_cartao_digitos")
    private int cartaoDigitos;    
    @Column(name = "nr_cartao_posicao_via")
    private int cartaoPosicaoVia;    
    @Column(name = "nr_cartao_posicao_codigo")
    private int cartaoPosicaoCodigo;    

    public ConfiguracaoSocial() {
        this.id = -1;
        this.diasInativaDemissionado = 0;
        this.dataInativacaoDemissionado = null;
        this.grupoCategoriaInativaDemissionado = null;
        this.inativaDemissionado = false;
        this.recebeAtrasado = false;
        this.controlaCartaoFilial = false;
        this.cartaoDigitos = 0;
        this.cartaoPosicaoVia = 0;
        this.cartaoPosicaoCodigo = 0;
    }

    public ConfiguracaoSocial(int id, int diasInativaDemissionado, Date dataInativacaoDemissionado, GrupoCategoria grupoCategoriaInativaDemissionado, boolean inativaDemissionado, boolean recebeAtrasado, boolean controlaCartaoFilial, int cartaoDigitos, int cartaoPosicaoVia, int cartaoPosicaoCodigo) {
        this.id = id;
        this.diasInativaDemissionado = diasInativaDemissionado;
        this.dataInativacaoDemissionado = dataInativacaoDemissionado;
        this.grupoCategoriaInativaDemissionado = grupoCategoriaInativaDemissionado;
        this.inativaDemissionado = inativaDemissionado;
        this.recebeAtrasado = recebeAtrasado;
        this.controlaCartaoFilial = controlaCartaoFilial;
        this.cartaoDigitos = cartaoDigitos;
        this.cartaoPosicaoVia = cartaoPosicaoVia;
        this.cartaoPosicaoCodigo = cartaoPosicaoCodigo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDiasInativaDemissionado() {
        return diasInativaDemissionado;
    }

    public void setDiasInativaDemissionado(int diasInativaDemissionado) {
        this.diasInativaDemissionado = diasInativaDemissionado;
    }

    public Date getDataInativacaoDemissionado() {
        return dataInativacaoDemissionado;
    }

    public void setDataInativacaoDemissionado(Date dataInativacaoDemissionado) {
        this.dataInativacaoDemissionado = dataInativacaoDemissionado;
    }

    public String getDataInativacaoDemissionadoString() {
        return DataHoje.converteData(dataInativacaoDemissionado);
    }

    public void setDataInativacaoDemissionadoString(String dataInativacaoDemissionado) {
        this.dataInativacaoDemissionado = DataHoje.converte(dataInativacaoDemissionado);
    }

    public GrupoCategoria getGrupoCategoriaInativaDemissionado() {
        return grupoCategoriaInativaDemissionado;
    }

    public void setGrupoCategoriaInativaDemissionado(GrupoCategoria grupoCategoriaInativaDemissionado) {
        this.grupoCategoriaInativaDemissionado = grupoCategoriaInativaDemissionado;
    }

    public boolean isInativaDemissionado() {
        return inativaDemissionado;
    }

    public void setInativaDemissionado(boolean inativaDemissionado) {
        this.inativaDemissionado = inativaDemissionado;
    }

    public boolean isRecebeAtrasado() {
        return recebeAtrasado;
    }

    public void setRecebeAtrasado(boolean recebeAtrasado) {
        this.recebeAtrasado = recebeAtrasado;
    }

    public boolean isControlaCartaoFilial() {
        return controlaCartaoFilial;
    }

    public void setControlaCartaoFilial(boolean controlaCartaoFilial) {
        this.controlaCartaoFilial = controlaCartaoFilial;
    }

    public int getCartaoDigitos() {
        return cartaoDigitos;
    }

    public void setCartaoDigitos(int cartaoDigitos) {
        this.cartaoDigitos = cartaoDigitos;
    }

    public int getCartaoPosicaoVia() {
        return cartaoPosicaoVia;
    }

    public void setCartaoPosicaoVia(int cartaoPosicaoVia) {
        this.cartaoPosicaoVia = cartaoPosicaoVia;
    }

    public int getCartaoPosicaoCodigo() {
        return cartaoPosicaoCodigo;
    }

    public void setCartaoPosicaoCodigo(int cartaoPosicaoCodigo) {
        this.cartaoPosicaoCodigo = cartaoPosicaoCodigo;
    }
}
