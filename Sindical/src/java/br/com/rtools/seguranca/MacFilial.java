package br.com.rtools.seguranca;

import br.com.rtools.financeiro.Caixa;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.utilitarios.GenericaSessao;
import javax.persistence.*;

@Entity
@Table(name = "SEG_MAC_FILIAL")
@NamedQuery(name = "MacFilial.pesquisaID", query = "select mf from MacFilial mf where mf.id = :pid")
public class MacFilial implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_DEPARTAMENTO", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Departamento departamento;
    @JoinColumn(name = "ID_FILIAL", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Filial filial;
    @Column(name = "DS_MAC", nullable = false)
    private String mac;
    @Column(name = "NR_MESA")
    private int mesa;
    @JoinColumn(name = "ID_CAIXA", referencedColumnName = "ID")
    @ManyToOne
    private Caixa caixa;

    public MacFilial() {
        this.id = -1;
        this.departamento = new Departamento();
        this.filial = new Filial();
        this.mac = "";
        this.mesa = 0;
        this.caixa = new Caixa();
    }

    public MacFilial(int id, Departamento departamento, Filial filial, String mac, int mesa, Caixa caixa) {
        this.id = id;
        this.departamento = departamento;
        this.filial = filial;
        this.mac = mac;
        this.mesa = mesa;
        this.caixa = caixa;
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
        
    public static MacFilial getAcessoFilial() {
        MacFilial macFilial = new MacFilial();
        if (GenericaSessao.exists("acessoFilial")) {
            macFilial = (MacFilial) GenericaSessao.getObject("acessoFilial");
        }
        return macFilial;
    }

    public Caixa getCaixa() {
        return caixa;
    }

    public void setCaixa(Caixa caixa) {
        this.caixa = caixa;
    }
}