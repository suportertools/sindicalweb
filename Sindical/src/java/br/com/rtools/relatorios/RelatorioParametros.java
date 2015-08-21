package br.com.rtools.relatorios;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "sis_relatorio_parametros")
public class RelatorioParametros implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_relatorio", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Relatorios relatorio;
    @Column(name = "ds_parametro", length = 100, nullable = false)
    private String parametro;    
    @Column(name = "ds_apelido", length = 100, nullable = false)
    private String apelido;    

    public RelatorioParametros() {
        this.id = -1;
        this.relatorio = new Relatorios();
        this.parametro = "";
        this.apelido = "";
    }
    
    public RelatorioParametros(Integer id, Relatorios relatorio, String parametro, String apelido) {
        this.id = id;
        this.relatorio = relatorio;
        this.parametro = parametro;
        this.apelido = apelido;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Relatorios getRelatorio() {
        return relatorio;
    }

    public void setRelatorio(Relatorios relatorio) {
        this.relatorio = relatorio;
    }

    public String getParametro() {
        return parametro;
    }

    public void setParametro(String parametro) {
        this.parametro = parametro;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }
}
