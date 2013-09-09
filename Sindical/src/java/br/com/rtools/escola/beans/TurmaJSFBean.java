package br.com.rtools.escola.beans;

import br.com.rtools.escola.ComponenteCurricular;
import br.com.rtools.escola.Professor;
import br.com.rtools.escola.Turma;
import br.com.rtools.escola.TurmaProfessor;
import br.com.rtools.escola.db.*;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class TurmaJSFBean implements java.io.Serializable {
    private Turma turma = new Turma();
    private String msgConfirma = "";
    private int idServicos = 0;
    private int idProfessor = 0;
    private int idComponenteCurricular = 0;
    private List<TurmaProfessor> listaProfs = new ArrayList();
    private List<Turma> listaTurma = new ArrayList();
    private int idIndex = -1;
    private int idIndexPesquisa = -1;
    private MacFilial macFilial = new MacFilial();
    private Filial filial = new Filial();    
    private String msgStatusFilial = "";
    

    public String salvar(){
        if (turma.getFilial().getId() == -1) {
            msgConfirma = "Informar a filial! Obs: Necessário acessar o sistema usando autênticação.";
            return null;
        }
        if(turma.getDataInicio().equals("__:__") || turma.getDataInicio().equals("") || turma.getDataInicio().isEmpty()){
            msgConfirma = "Informar a data inicial da turma!";
            return null;
        }
        if(turma.getDataTermino().equals("__:__") || turma.getDataTermino().equals("") || turma.getDataTermino().isEmpty()){
            msgConfirma = "Informar a data de termino da turma!";
            return null;
        }
        int dataInicioInteger = DataHoje.converteDataParaInteger(turma.getDataInicio());
        int dataFinalInteger = DataHoje.converteDataParaInteger(turma.getDataTermino());
        int dataHojeInteger = DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()));
        if (dataInicioInteger < dataHojeInteger) {
            msgConfirma = "A data inicial do curso deve ser maior ou igual a data de hoje!";
            return null;
        }
        if (dataFinalInteger < dataHojeInteger) {
            msgConfirma = "A data final do curso deve ser maior ou igual a data de hoje!";
            return null;
        }
        if (dataFinalInteger < dataInicioInteger) {
            msgConfirma = "A data final deve ser maior ou igual a data inicial!";
            return null;
        }
        if (DataHoje.validaHora(turma.getHoraInicio()).isEmpty()) {
            msgConfirma = "Hora inicial invalida!";
            return null;
        }
        if (DataHoje.validaHora(turma.getHoraTermino()).isEmpty()) {
            msgConfirma = "Hora final invalida!";
            return null;
        }        
        if(turma.getHoraInicio().equals("__:__")){
           turma.setHoraInicio("");
        }
        if(turma.getHoraTermino().equals("__:__")){
           turma.setHoraTermino(""); 
        }
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        ServicosDB db = new ServicosDBToplink();
        sv.abrirTransacao();
        turma.setCursos(db.pesquisaCodigo(Integer.parseInt(getListaServicos().get(idServicos).getDescription())));        
        if (turma.getId() == -1){
            if (!sv.inserirObjeto(turma)){
                msgConfirma = "Erro ao salvar turma!";
                sv.desfazerTransacao();
                return null;
            }
            msgConfirma = "Turma salva com sucesso!";
        }else{
            if (!sv.alterarObjeto(turma)){
                msgConfirma = "Erro ao atualizar turma!";
                sv.desfazerTransacao();
                return null;
            }
            msgConfirma = "Turma atualizada com sucesso!";
        }

        for(int i = 0; i < listaProfs.size(); i++){
            if (listaProfs.get(i).getId() == -1){
                listaProfs.get(i).setTurma(turma);
                if (!sv.inserirObjeto(listaProfs.get(i))){
                    msgConfirma = "Erro ao salvar Lista de Professores!";
                    sv.desfazerTransacao();
                    return null;
                }
            }
        }
        sv.comitarTransacao();
        return null;
    }

//    public String editar(){
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado",true);
//        turma = (Turma)listaTurma.get(idIndexPesquisa);
//        for (int i = 0; i < getListaServicos().size(); i ++ ){
//            if (Integer.parseInt(getListaServicos().get(i).getDescription()) == turma.getCursos().getId())
//                idServicos = i;
//        }
//        if (turma.getFilial().getId() == -1) {
//            getMacFilial();
//            turma.setFilial(macFilial.getFilial());
//        }          
//        TurmaDB turmaDB = new TurmaDBToplink();
//        this.listaProfs = turmaDB.listaTurmaProfessor(turma.getId());        
//        return "turma";
//    }
    
    public String editar() {
        Turma turmaC = new Turma();
        SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
        turmaC  = (Turma) dB.pesquisaCodigo(listaTurma.get(idIndexPesquisa).getId(), "Turma");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("turmaPesquisa", turmaC);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") == null) {
            return "turma";
        } else {
            return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        }
    }    

    public String excluir(){
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        if (turma.getId() != -1){
            sv.abrirTransacao();
            for(int i = 0; i < listaProfs.size(); i++){
                if (listaProfs.get(i).getId() != -1){
                    if (!sv.deletarObjeto(sv.pesquisaCodigo(listaProfs.get(i).getId(), "TurmaProfessor"))){
                        msgConfirma = "Erro ao excluir Professores!";
                        sv.desfazerTransacao();
                        return null;
                    }
                }
            }

            if (!sv.deletarObjeto(sv.pesquisaCodigo(turma.getId(), "Turma"))){
                msgConfirma = "Erro ao excluir Turma!";
                sv.desfazerTransacao();
                return null;
            }

            msgConfirma = "Cadastro excluído com sucesso!";
            sv.comitarTransacao();
            novoGenerico();
        }else{
            msgConfirma = "Pesquise uma turma para ser excluída!";
        }
        return null;
    }

    public void excluirProfessor(){
        SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
        if ( listaProfs.get(idIndex).getId() != -1){
            dB.abrirTransacao();
            if (!dB.deletarObjeto((TurmaProfessor) dB.pesquisaCodigo(listaProfs.get(idIndex).getId(), "TurmaProfessor" ))){
                msgConfirma = "Erro ao excluir professor";
                dB.desfazerTransacao();
            }else{
//                msgConfirma = "Professor exluído com sucesso!";
                dB.comitarTransacao();
                listaProfs.remove(listaProfs.get(idIndex));
            }
        }else{
            listaProfs.remove(listaProfs.get(idIndex));
        }
        return;
    }

    public String novo(){
        idServicos = 0;
        idProfessor = 0;
        turma = new Turma();
        idComponenteCurricular = 0;
        listaProfs.clear();
        return "turma";
        
    }
    
    public void novoGenerico(){
        idServicos = 0;
        idProfessor = 0;
        turma = new Turma();
        idComponenteCurricular = 0;
        listaProfs.clear();
    }

    public String adicionarProfessor(){
        msgConfirma = "";
        for(int i = 0; i < listaProfs.size(); i++){
            if (listaProfs.get(i).getProfessor().getId() == Integer.parseInt(getListaProfessor().get(idProfessor).getDescription()) &&
                listaProfs.get(i).getComponenteCurricular().getId() == Integer.parseInt(getListaComponente().get(idComponenteCurricular).getDescription())){
                msgConfirma = "Professor e Componente já existem!";
                return null;
            }
        }
        ProfessorDB dbp = new ProfessorDBToplink();
        ComponenteCurricularDB dbc = new ComponenteCurricularDBToplink();
        TurmaProfessor tp = new TurmaProfessor(-1,
                                               null,
                                               dbp.pesquisaCodigo(Integer.parseInt(getListaProfessor().get(idProfessor).getDescription())),
                                               dbc.pesquisaCodigo(Integer.parseInt(getListaComponente().get(idComponenteCurricular).getDescription())));
        listaProfs.add(tp);
//        msgConfirma = "Professor adicionado!";
        return null;
    }

    public List<SelectItem> getListaServicos(){
        List<SelectItem> listaSe = new Vector<SelectItem>();
        int i = 0;
        ServicosDB db = new ServicosDBToplink();
        List select = db.pesquisaTodos(150);
        while (i < select.size()){
            listaSe.add(new SelectItem( new Integer(i),
                        (String) ((Servicos) select.get(i)).getDescricao(),
                        Integer.toString(((Servicos) select.get(i)).getId()) ));
            i++;
        }
        return listaSe;
    }

    public List<SelectItem> getListaProfessor(){
        List<SelectItem> listaP = new Vector<SelectItem>();
        int i = 0;
        ProfessorDB db = new ProfessorDBToplink();
        List select = db.pesquisaTodos();

        while (i < select.size()){
            listaP.add(new SelectItem( new Integer(i),
                        (String) ((Professor) select.get(i)).getProfessor().getNome(),
                        Integer.toString(((Professor) select.get(i)).getId()) ));
            i++;
        }
        return listaP;
    }

    public List<SelectItem> getListaComponente(){
        List<SelectItem> listaC = new Vector<SelectItem>();
        int i = 0;
        ComponenteCurricularDB db = new ComponenteCurricularDBToplink();
        List select = db.pesquisaTodos();
        while (i < select.size()){
            listaC.add(new SelectItem( new Integer(i),
                        (String) ((ComponenteCurricular) select.get(i)).getDescricao(),
                        Integer.toString(((ComponenteCurricular) select.get(i)).getId()) ));
            i++;
        }
        return listaC;
    }

    public Turma getTurma() {
        if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("turmaPesquisa") != null){
            TurmaDB turmaDB = new TurmaDBToplink();
            turma = (Turma) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("turmaPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("turmaPesquisa");
            this.listaProfs = turmaDB.listaTurmaProfessor(turma.getId());              
        }
        if (turma.getFilial().getId() == -1) {
            getMacFilial();
            turma.setFilial(macFilial.getFilial());
        }          
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public int getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(int idServicos) {
        this.idServicos = idServicos;
    }

    public int getIdProfessor() {
        return idProfessor;
    }

    public void setIdProfessor(int idProfessor) {
        this.idProfessor = idProfessor;
    }

    public int getIdComponenteCurricular() {
        return idComponenteCurricular;
    }

    public void setIdComponenteCurricular(int idComponenteCurricular) {
        this.idComponenteCurricular = idComponenteCurricular;
    }

    public List<TurmaProfessor> getListaProfs() {
        return listaProfs;
    }

    public void setListaProfs(List<TurmaProfessor> listaProfs) {
        this.listaProfs = listaProfs;
    }

    public List<Turma> getListaTurma() {
        TurmaDB db = new TurmaDBToplink();
        listaTurma = db.pesquisaTodos();        
        return listaTurma;
    }

    public void setListaTurma(List<Turma> listaTurma) {
        this.listaTurma = listaTurma;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public int getIdIndexPesquisa() {
        return idIndexPesquisa;
    }

    public void setIdIndexPesquisa(int idIndexPesquisa) {
        this.idIndexPesquisa = idIndexPesquisa;
    }

    public String getMsgStatusFilial() {
        if (msgStatusFilial.equals("")) {
            getFilial();
        }
        return msgStatusFilial;
    }

    public void setMsgStatusFilial(String msgStatusFilial) {
        this.msgStatusFilial = msgStatusFilial;
    }

    public MacFilial getMacFilial() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoFilial") != null) {
            macFilial = (MacFilial) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoFilial");
            msgStatusFilial = "";
        } else {
            msgStatusFilial = "Informar filial";
        }
        return macFilial;
    }

    public void setMacFilial(MacFilial macFilial) {
        this.macFilial = macFilial;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }
    
    public String validaHoraInicio(){
        String novaHora = "";
        novaHora = DataHoje.validaHora(turma.getHoraInicio());
        if(novaHora.equals("")){
            turma.setHoraInicio("__:__");
        }else{
            turma.setHoraInicio(novaHora);            
        }
        validaHorarios();
        return "turma";
    }
    
    public String validaHoraTermino(){
        String novaHora = "";
        novaHora = DataHoje.validaHora(turma.getHoraTermino());
        if(novaHora.equals("")){
            turma.setHoraTermino("__:__");
        }else{
            turma.setHoraTermino(novaHora);            
        }
        validaHorarios();
        return "turma";
    }
    
    public String validaHorarios(){
        int n1a = 0;
        int n1b = 0;
        int n2a = 0;
        int n2b = 0;
        if(!turma.getHoraInicio().equals("__:__") && !turma.getHoraTermino().equals("__:__")){
            n1a = Integer.parseInt(turma.getHoraInicio().substring(0, 2));
            n1b = Integer.parseInt(turma.getHoraInicio().substring(3, 4));        
            n2a = Integer.parseInt(turma.getHoraTermino().substring(0, 2));
            n2b = Integer.parseInt(turma.getHoraTermino().substring(3, 4));
            if(n1a >= n2a){
                turma.setHoraTermino("__:__");
            }
        }
        return "turma";
    }
}