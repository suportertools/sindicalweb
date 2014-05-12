package br.com.rtools.agenda.beans;

import br.com.rtools.agenda.AgendaContato;
import br.com.rtools.agenda.db.AgendaTelefoneDB;
import br.com.rtools.agenda.db.AgendaTelefoneDBToplink;
import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class AgendaNotificaoBean implements Serializable {

    private String dataHoje;

    @PostConstruct
    public void init() {
        dataHoje = DataHoje.data();
    }

    public List<AgendaContato> getListAniversariantes() {
        AgendaTelefoneDB atdb = new AgendaTelefoneDBToplink();
        List list = (List<AgendaContato>) atdb.pesquisaAniversariantesPorPeriodo();
        return list;
    }

    public String getDataHoje() {
        return dataHoje;
    }

    public void setDataHoje(String dataHoje) {
        this.dataHoje = dataHoje;
    }

}
