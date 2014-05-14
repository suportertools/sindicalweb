package br.com.rtools.seguranca.beans;

import br.com.rtools.pessoa.Filial;
import br.com.rtools.seguranca.Departamento;
import br.com.rtools.seguranca.FilialDepartamento;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class FilialDepartamentoBean {

    private FilialDepartamento filialDepartamento;
    private List<FilialDepartamento> listFilialDepartamentos;
    private List<Filial> listFiliais;
    private List<Departamento> listDepartamentos;
    private Filial filial;
    private Departamento departamento;

    @PostConstruct
    public void init() {
        filialDepartamento = new FilialDepartamento();
        listFilialDepartamentos = new ArrayList<FilialDepartamento>();
        listFiliais = new ArrayList<Filial>();
        listDepartamentos = new ArrayList<Departamento>();
        filial = new Filial();
        departamento = new Departamento();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("filialDepartamentoBean");
    }

    public void clear() {
        GenericaSessao.remove("filialDepartamentoBean");
    }

    public void clear(int ctype) {
        if (ctype == 1) {
            listDepartamentos.clear();            
        } else if (ctype == 2) {
            departamento = new Departamento();
            listDepartamentos.clear();
            listFilialDepartamentos.clear();
        }
    }

    public void save() {
        boolean err = false;
        if (!listFilialDepartamentos.isEmpty()) {
            Dao dao = new Dao();
            dao.openTransaction();
            for (FilialDepartamento fd : listFilialDepartamentos) {
                if (fd.getId() == -1) {
                    if (!dao.save(fd)) {
                        err = true;
                        break;
                    }
                }
            }
            if (err) {
                GenericaMensagem.warn("Erro", "Ao salvar registro(s)");
                dao.rollback();
            } else {
                GenericaMensagem.info("Sucesso", "Registro(s) adicionado(s) com sucesso");
                listFilialDepartamentos.clear();
                dao.commit();
            }
        } else {
            GenericaMensagem.warn("Validação", "Nenhum item novo!");
        }
    }

    public void add() {
        if (listFiliais.isEmpty()) {
            GenericaMensagem.warn("Validação", "Cadastrar filiais!");
            return;
        }
        if (listFiliais.isEmpty()) {
            GenericaMensagem.warn("Validação", "Cadastrar departamentos!");
            return;
        }
        for (FilialDepartamento fd : listFilialDepartamentos) {
            if (fd.getFilial().getId() == filial.getId() && fd.getDepartamento().getId() == departamento.getId()) {
                GenericaMensagem.warn("Validação", "Item já adicionado a lista!");
                return;
            }
        }
        for (int i = 0; i < listDepartamentos.size(); i++) {
            if (departamento.getId() == listDepartamentos.get(i).getId()) {
                listDepartamentos.remove(i);
                break;
            }
        }
        filialDepartamento.setFilial(filial);
        filialDepartamento.setDepartamento(departamento);
        listFilialDepartamentos.add(filialDepartamento);
        filialDepartamento = new FilialDepartamento();

    }

    public void remove(int index) {
        for (int i = 0; i < listFilialDepartamentos.size(); i++) {
            if (i == index) {
                if (listFilialDepartamentos.get(i).getId() != -1) {
                    Dao dao = new Dao();
                    if (!dao.delete(listFilialDepartamentos.get(i), true)) {
                        GenericaMensagem.warn("Erro", "Ao excluir registro");
                        return;
                    }
                }
                listFilialDepartamentos.remove(i);
                listDepartamentos.clear();
                GenericaMensagem.info("Sucesso", "Registro excluído");
                return;
            }
        }
    }

    public FilialDepartamento getFilialDepartamento() {

        return filialDepartamento;
    }

    public void setFilialDepartamento(FilialDepartamento filialDepartamento) {
        this.filialDepartamento = filialDepartamento;
    }

    public List<FilialDepartamento> getListFilialDepartamentos() {
        if (listFilialDepartamentos.isEmpty()) {
            Dao dao = new Dao();
            List<FilialDepartamento> list = (List<FilialDepartamento>) dao.listQuery("FilialDepartamento", "findFilial", new Object[]{filial.getId()});
            if (!list.isEmpty()) {
                listFilialDepartamentos.addAll(list);
            }
        }
        return listFilialDepartamentos;
    }

    public void setListFilialDepartamentos(List<FilialDepartamento> listFilialDepartamentos) {
        this.listFilialDepartamentos = listFilialDepartamentos;
    }

    public List<Filial> getListFiliais() {
        if (listFiliais.isEmpty()) {
            Dao dao = new Dao();
            listFiliais = (List<Filial>) dao.list("Filial", true);
        }
        return listFiliais;
    }

    public void setListFiliais(List<Filial> listFiliais) {
        this.listFiliais = listFiliais;
    }

    public List<Departamento> getListDepartamentos() {
        if (listDepartamentos.isEmpty()) {
            Dao dao = new Dao();
            listDepartamentos = (List<Departamento>) dao.listQuery("FilialDepartamento", "findDepartamento", new Object[]{filial.getId()});
            if (!listDepartamentos.isEmpty()) {
                departamento = listDepartamentos.get(0);
            }
        }
        return listDepartamentos;
    }

    public void setListDepartamentos(List<Departamento> listDepartamentos) {
        this.listDepartamentos = listDepartamentos;
    }

    public Filial getFilial() {
        if (!listFiliais.isEmpty()) {
            if (filial.getId() == -1) {
                filial = listFiliais.get(0);
            }
        }
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

}
