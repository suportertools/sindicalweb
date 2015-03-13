package br.com.rtools.pessoa;

import br.com.rtools.seguranca.Departamento;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "pes_biometria_departamento")
public class BiometriaDepartamento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_biometria", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Biometria biometria;
    @JoinColumn(name = "id_departamento", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Departamento departamento;
    @Column(name = "dt_processamento", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date processamento;

    public BiometriaDepartamento() {
        this.id = null;
        this.biometria = null;
        this.departamento = null;
        this.processamento = null;
    }

    public BiometriaDepartamento(Integer id, Biometria biometria, Departamento departamento, Date processamento) {
        this.id = id;
        this.biometria = biometria;
        this.departamento = departamento;
        this.processamento = processamento;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Biometria getBiometria() {
        return biometria;
    }

    public void setBiometria(Biometria biometria) {
        this.biometria = biometria;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Date getProcessamento() {
        return processamento;
    }

    public void setProcessamento(Date processamento) {
        this.processamento = processamento;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BiometriaDepartamento other = (BiometriaDepartamento) obj;
        return true;
    }

    @Override
    public String toString() {
        return "BiometriaDepartamento{" + "id=" + id + ", biometria=" + biometria + ", departamento=" + departamento + ", processamento=" + processamento + '}';
    }

}
