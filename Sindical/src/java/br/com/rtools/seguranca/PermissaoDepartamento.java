package br.com.rtools.seguranca;

import javax.persistence.*;

@Entity
@Table(name="SEG_PERMISSAO_DEPARTAMENTO")
@NamedQuery(name="PermissaoDepartamento.pesquisaID", query="select pd from PermissaoDepartamento pd where pd.id=:pid")

public class PermissaoDepartamento  implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name="ID_PERMISSAO", referencedColumnName="ID", nullable=false)
    @ManyToOne
    private Permissao permissao;
    @JoinColumn(name="ID_NIVEL", referencedColumnName="ID", nullable=false)
    @ManyToOne
    private Nivel nivel;
    @JoinColumn(name="ID_DEPARTAMENTO", referencedColumnName="ID", nullable=false)
    @ManyToOne
    private Departamento departamento;
    
    public PermissaoDepartamento() {
        this.id = -1;
        this.permissao = new Permissao();
        this.nivel = new Nivel();
        this.departamento = new Departamento();
    }
    
    public PermissaoDepartamento(int id, Permissao permissao, Nivel nivel, Departamento departamento) {
        this.id = id;
        this.permissao = permissao;
        this.nivel = nivel;
        this.departamento = departamento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Permissao getPermissao() {
        return permissao;
    }

    public void setPermissao(Permissao permissao) {
        this.permissao = permissao;
    }
}
