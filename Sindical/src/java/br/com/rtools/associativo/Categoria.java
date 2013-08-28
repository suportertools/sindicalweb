package br.com.rtools.associativo;

import br.com.rtools.pessoa.Juridica;
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
@Table(name="SOC_CATEGORIA")
@NamedQuery(name="Categoria.pesquisaID", query="select c from Categoria c where c.id=:pid")
public class Categoria implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Column(name="DS_CATEGORIA", length=100, nullable=true)
    private String categoria;
    @JoinColumn(name="ID_GRUPO_CATEGORIA", referencedColumnName="ID", nullable=true)
    @ManyToOne
    private GrupoCategoria grupoCategoria;
    @Column(name="NR_CARENCIA_BALCAO", length=10,nullable=true)
    private int nrCarenciaBalcao;
    @Column(name="NR_CARENCIA_DESC_FOLHA", length=10,nullable=true)
    private int nrCarenciaDescFolha;
    @Column(name = "EMPRESA_OBRIGATORIA", nullable = true)
    private boolean empresaObrigatoria;
    @Column(name = "VOTANTE", nullable = true)
    private boolean votante;
    @Column(name = "USA_CLUBE_SEGUNDA", nullable = true)
    private boolean usaClubeSegunda;
    @Column(name = "USA_CLUBE_TERCA", nullable = true)
    private boolean usaClubeTerca;
    @Column(name = "USA_CLUBE_QUARTA", nullable = true)
    private boolean usaClubeQuarta;
    @Column(name = "USA_CLUBE_QUINTA", nullable = true)
    private boolean usaClubeQuinta;
    @Column(name = "USA_CLUBE_SEXTA", nullable = true)
    private boolean usaClubeSexta;
    @Column(name = "USA_CLUBE_SABADO", nullable = true)
    private boolean usaClubeSabado;
    @Column(name = "USA_CLUBE_DOMINGO", nullable = true)
    private boolean usaClubeDomingo;

    public Categoria() {
        this.id = -1;
        this.categoria = "";
        this.grupoCategoria = new GrupoCategoria();
        this.nrCarenciaBalcao = 0;
        this.nrCarenciaDescFolha = 0;
        this.empresaObrigatoria = false;
        this.votante = false;
        this.usaClubeSegunda = false;
        this.usaClubeTerca = false;
        this.usaClubeQuarta = false;
        this.usaClubeQuinta = false;
        this.usaClubeSexta = false;
        this.usaClubeSabado = false;
        this.usaClubeDomingo = false;
    }

    public Categoria(int id, String categoria, GrupoCategoria grupoCategoria, int nrCarenciaBalcao, int nrCarenciaDescFolha, 
                    boolean empresaObrigatoria,boolean votante, boolean usaClubeSegunda, boolean usaClubeTerca, boolean usaClubeQuarta,
                    boolean usaClubeQuinta, boolean usaClubeSexta, boolean usaClubeSabado, boolean usaClubeDomingo) {
        this.id = id;
        this.categoria = categoria;
        this.grupoCategoria = grupoCategoria;
        this.nrCarenciaBalcao = nrCarenciaBalcao;
        this.nrCarenciaDescFolha = nrCarenciaDescFolha;
        this.empresaObrigatoria = empresaObrigatoria;
        this.votante = votante;
        this.usaClubeSegunda = usaClubeSegunda;
        this.usaClubeTerca = usaClubeTerca;
        this.usaClubeQuarta = usaClubeQuarta;
        this.usaClubeQuinta = usaClubeQuinta;
        this.usaClubeSexta = usaClubeSexta;
        this.usaClubeSabado = usaClubeSabado;
        this.usaClubeDomingo = usaClubeDomingo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GrupoCategoria getGrupoCategoria() {
        return grupoCategoria;
    }

    public void setGrupoCategoria(GrupoCategoria grupoCategoria) {
        this.grupoCategoria = grupoCategoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getNrCarenciaBalcao() {
        return nrCarenciaBalcao;
    }

    public void setNrCarenciaBalcao(int nrCarenciaBalcao) {
        this.nrCarenciaBalcao = nrCarenciaBalcao;
    }

    public int getNrCarenciaDescFolha() {
        return nrCarenciaDescFolha;
    }

    public void setNrCarenciaDescFolha(int nrCarenciaDescFolha) {
        this.nrCarenciaDescFolha = nrCarenciaDescFolha;
    }

    public boolean isEmpresaObrigatoria() {
        return empresaObrigatoria;
    }

    public void setEmpresaObrigatoria(boolean empresaObrigatoria) {
        this.empresaObrigatoria = empresaObrigatoria;
    }

    public boolean isUsaClubeSegunda() {
        return usaClubeSegunda;
    }

    public void setUsaClubeSegunda(boolean usaClubeSegunda) {
        this.usaClubeSegunda = usaClubeSegunda;
    }

    public boolean isUsaClubeTerca() {
        return usaClubeTerca;
    }

    public void setUsaClubeTerca(boolean usaClubeTerca) {
        this.usaClubeTerca = usaClubeTerca;
    }

    public boolean isUsaClubeQuarta() {
        return usaClubeQuarta;
    }

    public void setUsaClubeQuarta(boolean usaClubeQuarta) {
        this.usaClubeQuarta = usaClubeQuarta;
    }

    public boolean isUsaClubeQuinta() {
        return usaClubeQuinta;
    }

    public void setUsaClubeQuinta(boolean usaClubeQuinta) {
        this.usaClubeQuinta = usaClubeQuinta;
    }

    public boolean isUsaClubeSexta() {
        return usaClubeSexta;
    }

    public void setUsaClubeSexta(boolean usaClubeSexta) {
        this.usaClubeSexta = usaClubeSexta;
    }

    public boolean isUsaClubeSabado() {
        return usaClubeSabado;
    }

    public void setUsaClubeSabado(boolean usaClubeSabado) {
        this.usaClubeSabado = usaClubeSabado;
    }

    public boolean isUsaClubeDomingo() {
        return usaClubeDomingo;
    }

    public void setUsaClubeDomingo(boolean usaClubeDomingo) {
        this.usaClubeDomingo = usaClubeDomingo;
    }

    public boolean isVotante() {
        return votante;
    }

    public void setVotante(boolean votante) {
        this.votante = votante;
    }

}
