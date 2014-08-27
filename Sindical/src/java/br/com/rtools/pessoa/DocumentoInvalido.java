package br.com.rtools.pessoa;

import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "pes_documento_invalido")
@NamedQuery(name = "DocumentoInvalido.pesquisaID", query = "select docInv from DocumentoInvalido docInv where docInv.id=:pid")
public class DocumentoInvalido implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_documento_invalido", length = 100, nullable = true)
    private String documentoInvalido;
    @Column(name = "checado", nullable = true)
    private boolean checado;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_importacao")
    private Date dtImportacao;

    public DocumentoInvalido() {
        this.id = -1;
        this.documentoInvalido = "";
        this.checado = false;
        setImportacao(DataHoje.data());
    }

    public DocumentoInvalido(int id, String documentoInvalido, boolean checado, String importacao) {
        this.id = id;
        this.documentoInvalido = documentoInvalido;
        this.checado = checado;
        setImportacao(importacao);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDocumentoInvalido() {
        return documentoInvalido;
    }

    public void setDocumentoInvalido(String documentoInvalido) {
        this.documentoInvalido = documentoInvalido;
    }

    public Date getDtImportacao() {
        return dtImportacao;
    }

    public void setDtImportacao(Date dtImportacao) {
        this.dtImportacao = dtImportacao;
    }

    public String getImportacao() {
        return DataHoje.converteData(dtImportacao);
    }

    public void setImportacao(String importacao) {
        this.dtImportacao = DataHoje.converte(importacao);
    }

    public boolean isChecado() {
        return checado;
    }

    public void setChecado(boolean checado) {
        this.checado = checado;
    }
}
