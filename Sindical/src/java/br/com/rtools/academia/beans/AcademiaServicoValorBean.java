package br.com.rtools.academia.beans;

import br.com.rtools.academia.AcademiaGrade;
import br.com.rtools.academia.AcademiaServicoValor;
import br.com.rtools.academia.db.AcademiaDB;
import br.com.rtools.academia.db.AcademiaDBToplink;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.sistema.Periodo;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class AcademiaServicoValorBean implements Serializable {

    private AcademiaServicoValor academiaServicoValor = new AcademiaServicoValor();
    private List<AcademiaServicoValor> listaAcademiaServicoValors = new ArrayList<AcademiaServicoValor>();
    private List<SelectItem> listaServicos = new ArrayList<SelectItem>();
    private List<SelectItem> listaPeriodos = new ArrayList<SelectItem>();
    private List<SelectItem> listaGrades = new ArrayList<SelectItem>();
    private int idServicos = 0;
    private int idPeriodo = 0;
    private int idGrade = 0;
    private int maximoParcelas = 0;
    private boolean ocultaParcelas = true;

    public void salvar() {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        if (listaServicos.isEmpty()) {
            GenericaMensagem.warn("Sistema", "Cadastrar serviços!");
            return;
        }
        if (listaPeriodos.isEmpty()) {
            GenericaMensagem.warn("Sistema", "Cadastrar grade de horários e dias da semana!");
            return;
        }
        if (listaPeriodos.isEmpty()) {
            GenericaMensagem.warn("Sistema", "Cadastrar lista de períodos!");
            return;
        }
        academiaServicoValor.setServicos((Servicos) salvarAcumuladoDB.pesquisaObjeto(Integer.parseInt(listaServicos.get(idServicos).getDescription()), "Servicos"));
        academiaServicoValor.setAcademiaGrade((AcademiaGrade) salvarAcumuladoDB.pesquisaObjeto(Integer.parseInt(listaGrades.get(idGrade).getDescription()), "AcademiaGrade"));
        academiaServicoValor.setPeriodo((Periodo) salvarAcumuladoDB.pesquisaObjeto(Integer.parseInt(listaPeriodos.get(idPeriodo).getDescription()), "Periodo"));
        if (academiaServicoValor.getId() == -1) {
            AcademiaDB academiaDB = new AcademiaDBToplink();
            if (((AcademiaServicoValor) academiaDB.existeAcademiaServicoValor(academiaServicoValor)) != null) {
                GenericaMensagem.warn("Sistema", "Horário já cadastrado!");
                return;
            }
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.inserirObjeto(academiaServicoValor)) {
                salvarAcumuladoDB.comitarTransacao();
                GenericaMensagem.info("Sucesso", "Registro inserido");
                listaAcademiaServicoValors.clear();
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                GenericaMensagem.warn("Erro", "Ao adicionar registro!");
            }
        } else {
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.alterarObjeto(academiaServicoValor)) {
                salvarAcumuladoDB.comitarTransacao();
                GenericaMensagem.info("Sucesso", "Registro atualizado");
                listaAcademiaServicoValors.clear();
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                GenericaMensagem.warn("Erro", "Ao atualizar registro!");
            }
        }
        novo();
    }

    public void novo() {
        idGrade = 0;
        idPeriodo = 0;
        academiaServicoValor = new AcademiaServicoValor();
        ocultaParcelas = true;
    }

    public void editar(AcademiaServicoValor asv) {
        academiaServicoValor = asv;
        for (int i = 0; i < listaServicos.size(); i++) {
            if (Integer.parseInt(listaServicos.get(i).getDescription()) == asv.getServicos().getId()) {
                idServicos = i;
                break;
            }
        }
        for (int i = 0; i < listaGrades.size(); i++) {
            if (Integer.parseInt(listaGrades.get(i).getDescription()) == asv.getAcademiaGrade().getId()) {
                idGrade = i;
                break;
            }
        }
        for (int i = 0; i < listaPeriodos.size(); i++) {
            if (Integer.parseInt(listaPeriodos.get(i).getDescription()) == asv.getPeriodo().getId()) {
                idPeriodo = i;
                break;
            }
        }
    }

    public void excluir(AcademiaServicoValor asv) {
        if (asv.getId() != -1) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.deletarObjeto((AcademiaServicoValor) salvarAcumuladoDB.pesquisaCodigo(asv.getId(), "AcademiaServicoValor"))) {
                salvarAcumuladoDB.comitarTransacao();
                GenericaMensagem.info("Sucesso", "Excluído com sucesso");
                listaAcademiaServicoValors.clear();
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                GenericaMensagem.warn("Erro", "Ao excluir registro!");
            }
        }
    }

    public List<AcademiaServicoValor> getListaAcademiaServicoValors() {
        if (listaAcademiaServicoValors.isEmpty()) {
            AcademiaDB academiaDB = new AcademiaDBToplink();
            listaAcademiaServicoValors = academiaDB.listaAcademiaServicoValor(Integer.parseInt(listaServicos.get(idServicos).getDescription()));
        }
        return listaAcademiaServicoValors;
    }

    public void setListaAcademiaServicoValors(List<AcademiaServicoValor> listaAcademiaServicoValors) {
        this.listaAcademiaServicoValors = listaAcademiaServicoValors;
    }

    public AcademiaServicoValor getAcademiaServicoValor() {
        return academiaServicoValor;
    }

    public void setAcademiaServicoValor(AcademiaServicoValor academiaServicoValor) {
        this.academiaServicoValor = academiaServicoValor;
    }

    public List<SelectItem> getListaServicos() {
        if (listaServicos.isEmpty()) {
            ServicosDB db = new ServicosDBToplink();
            List list = db.pesquisaTodos(122);
            for (int i = 0; i < list.size(); i++) {
                listaServicos.add(new SelectItem(new Integer(i),
                        (String) ((Servicos) list.get(i)).getDescricao(),
                        Integer.toString(((Servicos) list.get(i)).getId())));
            }
        }
        return listaServicos;
    }

    public void setListaServicos(List<SelectItem> listaServicos) {
        this.listaServicos = listaServicos;
    }

    public int getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(int idServicos) {
        this.idServicos = idServicos;
    }

    public List<SelectItem> getListaPeriodos() {
        if (listaPeriodos.isEmpty()) {
            SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
            List<Periodo> list = (List<Periodo>) dB.listaObjeto("Periodo", true);
            for (int i = 0; i < list.size(); i++) {
                listaPeriodos.add(new SelectItem(new Integer(i), list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
            }
        }
        return listaPeriodos;
    }

    public void setListaPeriodos(List<SelectItem> listaPeriodos) {
        this.listaPeriodos = listaPeriodos;
    }

    public int getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public List<SelectItem> getListaGrades() {
        if (listaGrades.isEmpty()) {
            SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
            List<AcademiaGrade> list = (List<AcademiaGrade>) dB.listaObjeto("AcademiaGrade", true);
            for (int i = 0; i < list.size(); i++) {
                listaGrades.add(new SelectItem(new Integer(i), list.get(i).getHoraInicio() + " - " + list.get(i).getHoraFim(), Integer.toString(list.get(i).getId())));
            }
        }
        return listaGrades;
    }

    public void setListaGrades(List<SelectItem> listaGrades) {
        this.listaGrades = listaGrades;
    }

    public int getIdGrade() {
        return idGrade;
    }

    public void setIdGrade(int idGrade) {
        this.idGrade = idGrade;
    }

    public boolean isOcultaParcelas() {
        if (!listaPeriodos.isEmpty()) {
            int id = Integer.parseInt(listaPeriodos.get(idPeriodo).getDescription());
            switch (id) {
                case 5:
                    ocultaParcelas = false;
                    maximoParcelas = 2;
                    break;
                case 6:
                    ocultaParcelas = false;
                    maximoParcelas = 4;
                    break;
                case 7:
                    ocultaParcelas = false;
                    maximoParcelas = 7;
                    break;
                default:
                    ocultaParcelas = true;
            }
        }
        return ocultaParcelas;
    }

    public void setOcultaParcelas(boolean ocultaParcelas) {
        this.ocultaParcelas = ocultaParcelas;
    }

    public int getMaximoParcelas() {
        return maximoParcelas;
    }

    public void setMaximoParcelas(int maximoParcelas) {
        this.maximoParcelas = maximoParcelas;
    }

}
