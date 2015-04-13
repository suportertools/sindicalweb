package br.com.rtools.seguranca;

import br.com.rtools.financeiro.Caixa;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.utilitarios.GenericaSessao;
import javax.persistence.*;

@Entity
@Table(name = "seg_mac_filial")
@NamedQuery(name = "MacFilial.pesquisaID", query = "select mf from MacFilial mf where mf.id = :pid")
public class MacFilial implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_departamento", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Departamento departamento;
    @JoinColumn(name = "id_filial", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Filial filial;
    @Column(name = "ds_mac", nullable = false)
    private String mac;
    @Column(name = "nr_mesa")
    private int mesa;
    @JoinColumn(name = "id_caixa", referencedColumnName = "id")
    @ManyToOne
    private Caixa caixa;
    @Column(name = "ds_descricao", nullable = true, length = 100)
    private String descricao;
    @Column(name = "is_caixa_operador", columnDefinition = "boolean default true")
    private boolean caixaOperador;        

    public MacFilial() {
        this.id = -1;
        this.departamento = new Departamento();
        this.filial = new Filial();
        this.mac = "";
        this.mesa = 0;
        this.caixa = new Caixa();
        this.descricao = "";
        this.caixaOperador = true;
    }

    public MacFilial(int id, Departamento departamento, Filial filial, String mac, int mesa, Caixa caixa, String descricao, boolean caixaOperador) {
        this.id = id;
        this.departamento = departamento;
        this.filial = filial;
        this.mac = mac;
        this.mesa = mesa;
        this.caixa = caixa;
        this.descricao = descricao;
        this.caixaOperador = caixaOperador;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getMesa() {
        return mesa;
    }

    public void setMesa(int mesa) {
        this.mesa = mesa;
    }

    public Caixa getCaixa() {
        return caixa;
    }

    public void setCaixa(Caixa caixa) {
        this.caixa = caixa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public static MacFilial getAcessoFilial() {
        MacFilial macFilial = new MacFilial();
        if (GenericaSessao.exists("acessoFilial")) {
            macFilial = (MacFilial) GenericaSessao.getObject("acessoFilial");
        }
        return macFilial;
    }
    
    public boolean isCaixaOperador() {
        return caixaOperador;
    }

    public void setCaixaOperador(boolean caixaOperador) {
        this.caixaOperador = caixaOperador;
    }
}
