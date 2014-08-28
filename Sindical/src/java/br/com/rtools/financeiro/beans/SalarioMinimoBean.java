package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.SalarioMinimo;
import br.com.rtools.logSistema.NovoLog;
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

@ManagedBean
@SessionScoped
public class SalarioMinimoBean implements Serializable {

    private SalarioMinimo salarioMinimo;
    private List<SalarioMinimo> listSalarioMinimo;

    @PostConstruct
    public void init() {
        salarioMinimo = new SalarioMinimo();
        listSalarioMinimo = new ArrayList<SalarioMinimo>();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("salarioMinimoBean");
    }

    public void clear() {
        salarioMinimo = new SalarioMinimo();
        listSalarioMinimo = new ArrayList<SalarioMinimo>();
    }

    public void save() {
        if (salarioMinimo.getVigenciaString().equals("")) {
            GenericaMensagem.warn("Validação", "Informar a data de vigência!");
            return;
        }
        if (salarioMinimo.getPublicacaoString().equals("")) {
            GenericaMensagem.warn("Validação", "Informar a data de publicação!");
            return;
        }
        if (salarioMinimo.getValorMensal() == 0) {
            GenericaMensagem.warn("Validação", "Informar valor mensal maior que 0!");
            return;
        }
        DaoInterface di = new Dao();
        if (salarioMinimo.getId() == -1) {
            for (SalarioMinimo sm : listSalarioMinimo) {
                if (sm.getPublicacao() == salarioMinimo.getPublicacao() && sm.getValorMensal() == salarioMinimo.getValorMensal()) {
                    GenericaMensagem.warn("Validação", "Valor de salario já cadastrado para o mês!");
                    return;
                }
            }
        }
        NovoLog novoLog = new NovoLog();
        di.openTransaction();
        if (salarioMinimo.getId() == -1) {
            if (di.save(salarioMinimo)) {
                di.commit();
                novoLog.save(
                        "ID: " + salarioMinimo.getId()
                        + " - Vigência: " + salarioMinimo.getVigenciaString()
                        + " - Valor Mensal: " + salarioMinimo.getValorMensalString()
                        + " - Valor Diário: " + salarioMinimo.getValorDiarioString()
                        + " - Valor Hora: " + salarioMinimo.getValorHoraString()
                        + " - Norma: " + salarioMinimo.getNorma()
                        + " - Publicação: " + salarioMinimo.getPublicacaoString()
                );
                clear();
                GenericaMensagem.info("Sucesso", "Registro inserido");
            } else {
                di.rollback();
                GenericaMensagem.warn("Erro", "Ao inserir registro!");
            }
        } else {
            SalarioMinimo sm = (SalarioMinimo) di.find(salarioMinimo);
            String beforeUpdate
                    = "ID: " + sm.getId()
                    + " - Vigência: " + sm.getVigenciaString()
                    + " - Valor Mensal: " + sm.getValorMensalString()
                    + " - Valor Diário: " + sm.getValorDiarioString()
                    + " - Valor Hora: " + sm.getValorHoraString()
                    + " - Norma: " + sm.getNorma()
                    + " - Publicação: " + sm.getPublicacaoString();
            if (di.update(salarioMinimo)) {
                di.commit();
                novoLog.update(beforeUpdate,
                        "ID: " + salarioMinimo.getId()
                        + " - Vigência: " + salarioMinimo.getVigenciaString()
                        + " - Valor Mensal: " + salarioMinimo.getValorMensalString()
                        + " - Valor Diário: " + salarioMinimo.getValorDiarioString()
                        + " - Valor Hora: " + salarioMinimo.getValorHoraString()
                        + " - Norma: " + salarioMinimo.getNorma()
                        + " - Publicação: " + salarioMinimo.getPublicacaoString()
                );
                clear();
                GenericaMensagem.info("Sucesso", "Registro atualizado");
            } else {
                di.rollback();
                GenericaMensagem.warn("Erro", "Ao atualizar registro!");
            }
        }
    }

    public void delete(SalarioMinimo sm) {
        salarioMinimo = sm;
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        di.openTransaction();
        if (di.delete(salarioMinimo)) {
            novoLog.delete(
                    "ID: " + salarioMinimo.getId()
                    + " - Vigência: " + salarioMinimo.getVigenciaString()
                    + " - Valor Mensal: " + salarioMinimo.getValorMensalString()
                    + " - Valor Diário: " + salarioMinimo.getValorDiarioString()
                    + " - Valor Hora: " + salarioMinimo.getValorHoraString()
                    + " - Norma: " + salarioMinimo.getNorma()
                    + " - Publicação: " + salarioMinimo.getPublicacaoString()
            );
            di.commit();
            clear();
            GenericaMensagem.info("Sucesso", "Registro removido");
        } else {
            di.rollback();
            GenericaMensagem.warn("Erro", "Ao remover registro!");
        }
    }

    public void edit(SalarioMinimo sm) {
        salarioMinimo = sm;
    }

    public SalarioMinimo getSalarioMinimo() {
        return salarioMinimo;
    }

    public void setSalarioMinimo(SalarioMinimo salarioMinimo) {
        this.salarioMinimo = salarioMinimo;
    }

    public List<SalarioMinimo> getListSalarioMinimo() {
        if (listSalarioMinimo.isEmpty()) {
            DaoInterface di = new Dao();
            listSalarioMinimo = (List<SalarioMinimo>) di.list("SalarioMinimo", true);
        }
        return listSalarioMinimo;
    }

    public void setListSalarioMinimo(List<SalarioMinimo> listSalarioMinimo) {
        this.listSalarioMinimo = listSalarioMinimo;
    }

}
