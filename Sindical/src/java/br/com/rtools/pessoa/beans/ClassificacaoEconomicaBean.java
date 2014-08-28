package br.com.rtools.pessoa.beans;

import br.com.rtools.financeiro.SalarioMinimo;
import br.com.rtools.financeiro.dao.SalarioMinimoDao;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.ClassificacaoEconomica;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.RowEditEvent;

@ManagedBean
@SessionScoped
public class ClassificacaoEconomicaBean implements Serializable {

    private ClassificacaoEconomica classificacaoEconomica;
    private SalarioMinimo salarioMinimo;
    private List<ClassificacaoEconomica> listClassificacaoEconomica;

    @PostConstruct
    public void init() {
        salarioMinimo = new SalarioMinimo();
        classificacaoEconomica = new ClassificacaoEconomica();
        listClassificacaoEconomica = new ArrayList<ClassificacaoEconomica>();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("classificacaoEconomicaBean");
    }

    public void clear() {
        classificacaoEconomica = new ClassificacaoEconomica();
        listClassificacaoEconomica = new ArrayList<ClassificacaoEconomica>();
    }

    public void update(RowEditEvent event) {
        ClassificacaoEconomica ce = (ClassificacaoEconomica) event.getObject();
        if (ce.getId() != -1) {
            NovoLog novoLog = new NovoLog();
            DaoInterface di = new Dao();
            ClassificacaoEconomica ceBefore = (ClassificacaoEconomica) di.find(ce);
            String beforeUpdate
                    = "ID: " + ceBefore.getId()
                    + " - Descrição: " + ceBefore.getDescricao()
                    + " - M. Inicial: " + ceBefore.getSalarioMinimoInicial()
                    + " - M. Final: " + ceBefore.getSalarioMinimoFinal();
            di.openTransaction();
            if (di.update(ceBefore)) {
                di.commit();
                listClassificacaoEconomica.clear();
                novoLog.update(beforeUpdate,
                        "ID: " + ce.getId()
                        + " - Descrição: " + ce.getDescricao()
                        + " - M. Inicial: " + ce.getSalarioMinimoInicial()
                        + " - M. Final: " + ce.getSalarioMinimoFinal()
                );
                GenericaMensagem.info("Sucesso", "Registro atualizado");
            } else {
                di.rollback();
                GenericaMensagem.warn("Erro", "Ao atualizar registro!");
            }
        }
    }

    public void delete(ClassificacaoEconomica sm) {
        classificacaoEconomica = sm;
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        di.openTransaction();
        if (di.delete(classificacaoEconomica)) {
            novoLog.delete(
                    "ID: " + classificacaoEconomica.getId()
                    + " - Descrição: " + classificacaoEconomica.getDescricao()
                    + " - M. Inicial: " + classificacaoEconomica.getSalarioMinimoInicial()
                    + " - M. Final: " + classificacaoEconomica.getSalarioMinimoInicial()
            );
            di.commit();
            clear();
            GenericaMensagem.info("Sucesso", "Registro removido");
        } else {
            di.rollback();
            GenericaMensagem.warn("Erro", "Ao remover registro!");
        }
    }

    public void edit(ClassificacaoEconomica sm) {
        classificacaoEconomica = sm;
    }

    public ClassificacaoEconomica getClassificacaoEconomica() {
        return classificacaoEconomica;
    }

    public void setClassificacaoEconomica(ClassificacaoEconomica classificacaoEconomica) {
        this.classificacaoEconomica = classificacaoEconomica;
    }

    public List<ClassificacaoEconomica> getListClassificacaoEconomica() {
        if (listClassificacaoEconomica.isEmpty()) {
            DaoInterface di = new Dao();
            listClassificacaoEconomica = (List<ClassificacaoEconomica>) di.list("ClassificacaoEconomica", true);
        }
        return listClassificacaoEconomica;
    }

    public void setListClassificacaoEconomica(List<ClassificacaoEconomica> listClassificacaoEconomica) {
        this.listClassificacaoEconomica = listClassificacaoEconomica;
    }

    public SalarioMinimo getSalarioMinimo() {
        if (salarioMinimo.getId() == -1) {
            SalarioMinimoDao dao = new SalarioMinimoDao();
            salarioMinimo = dao.salarioMinimoVigente();
            if(salarioMinimo == null) {
                salarioMinimo = new SalarioMinimo();
            }
        }
        return salarioMinimo;
    }

    public void setSalarioMinimo(SalarioMinimo salarioMinimo) {
        this.salarioMinimo = salarioMinimo;
    }

}
