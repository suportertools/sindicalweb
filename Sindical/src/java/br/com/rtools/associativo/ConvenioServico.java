package br.com.rtools.associativo;

import br.com.rtools.financeiro.Servicos;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "SOC_CONVENIO_SERVICO")
@NamedQuery(name = "ConvenioServico.pesquisaID", query = "select cs from ConvenioServico cs where cs.id=:pid")
public class ConvenioServico implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_CONVENIO_SUB_GRUPO", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private SubGrupoConvenio subGrupoConvenio;
    @JoinColumn(name = "ID_SERVICO", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Servicos servicos;

    public ConvenioServico() {
        this.id = -1;
        this.subGrupoConvenio = new SubGrupoConvenio();
        this.servicos = new Servicos();
    }

    public ConvenioServico(int id, SubGrupoConvenio subGrupoConvenio, Servicos servicos) {
        this.id = id;
        this.subGrupoConvenio = subGrupoConvenio;
        this.servicos = servicos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SubGrupoConvenio getSubGrupoConvenio() {
        return subGrupoConvenio;
    }

    public void setSubGrupoConvenio(SubGrupoConvenio subGrupoConvenio) {
        this.subGrupoConvenio = subGrupoConvenio;
    }

    public Servicos getServicos() {
        return servicos;
    }

    public void setServicos(Servicos servicos) {
        this.servicos = servicos;
    }
}