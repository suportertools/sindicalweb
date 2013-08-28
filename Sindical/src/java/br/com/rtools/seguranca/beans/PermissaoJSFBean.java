package br.com.rtools.seguranca.beans;

import br.com.rtools.seguranca.*;
import br.com.rtools.seguranca.db.*;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
// import java.util.Vector;
import javax.faces.model.SelectItem;

public class PermissaoJSFBean {

    private Permissao permissao = new Permissao();
    private Modulo modulo;
    private Rotina rotina;
    private Evento evento;
    private PermissaoDepartamento permissaoDepartamento = new PermissaoDepartamento();
    private String msgConfirma;
    private String indicaTab = "permissao";
    private String tabDisabled = "true";
    private List<Permissao> listaPermissoes = new ArrayList();
    private List listPerDisponivel = new ArrayList();
    private List listPerAdicionada = new ArrayList();
    private boolean carregaPerDisponivel = true;
    private boolean carregaPerAdicionada = true;
    private int idModulo;
    private int idRotina;
    private int idEvento;
    private int idDepartamento;
    private int idNivel;
    private int idIndex = -1;

    public void refreshForm() {
    }

    public String adicionarPermissao() {
        PermissaoDB db = new PermissaoDBToplink();
        RotinaDB rotinaDB = new RotinaDBToplink();
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        modulo = (Modulo) sv.pesquisaCodigo(Integer.valueOf(getListaModulos().get(idModulo).getDescription()), "Modulo");
        rotina = rotinaDB.pesquisaCodigo(Integer.valueOf(getListaRotinas().get(idRotina).getDescription()));
        if (db.pesquisaPermissaoModRot(modulo.getId(), rotina.getId()).isEmpty()) {
            for (int i = 0; i < getListaEventos().size(); i++) {
                evento = (Evento) sv.pesquisaCodigo(Integer.valueOf(getListaEventos().get(i).getDescription()), "Evento");
                permissao.setModulo(modulo);
                permissao.setRotina(rotina);
                permissao.setEvento(evento);
                if (db.insert(permissao)) {
                    msgConfirma = "Registro Adicionado!";
                    listaPermissoes.clear();
                }
                permissao = new Permissao();
            }
        } else {
            msgConfirma = "Permissão já Existente!";
        }
        permissao = new Permissao();
        return null;
    }

    public String novo() {
        permissao = new Permissao();
        setIdModulo(0);
        setIdRotina(0);
        setIdEvento(0);
        setTabDisabled("true");
        return "permissao";
    }

    public String btnExcluir() {
        PermissaoDB db = new PermissaoDBToplink();
        permissao = (Permissao) listaPermissoes.get(idIndex);
        int idMod = permissao.getModulo().getId();
        int idRot = permissao.getRotina().getId();
        List listaPer = db.pesquisaPermissaoModRot(idMod, idRot);
        for (int i = 0; i < listaPer.size(); i++) {
            permissao = (Permissao) listaPer.get(i);
            db.getEntityManager().getTransaction().begin();
            permissao = db.pesquisaCodigo(permissao.getId());
            if (db.delete(permissao)) {
                db.getEntityManager().getTransaction().commit();
                msgConfirma = "Registro excluído!";
                listaPermissoes.clear();
            } else {
                db.getEntityManager().getTransaction().rollback();
                msgConfirma = "Erro ao Excluir Permissão!";
                break;
            }
            permissao = new Permissao();
        }
        return null;
    }

    public List getPermissaoDisponivel() {
        String ids = "";
        if (carregaPerDisponivel) {
            listPerDisponivel = new ArrayList();
            PermissaoDepartamentoDB db = new PermissaoDepartamentoDBToplink();
            int idDepto = Integer.parseInt(getListaDepartamentos().get(idDepartamento).getDescription());
            int idNiv = Integer.parseInt(getListaNiveis().get(idNivel).getDescription());
            List listIds = db.pesquisaPermissaoAdc(idDepto, idNiv);
            if (!listIds.isEmpty()) {
                for (int o = 0; o < listIds.size(); o++) {
                    if (ids.length() > 0 && o != listIds.size()) {
                        ids = ids + ",";
                    }
                    ids = ids + Integer.toString(((PermissaoDepartamento) listIds.get(o)).getId());
                }
            }
            DataObject dtObject;
            List result = db.pesquisaPermissaDisponivel(ids);
            for (int i = 0; i < result.size(); i++) {
                dtObject = new DataObject(new Boolean(false),
                        (Permissao) result.get(i));
                listPerDisponivel.add(dtObject);
            }
            carregaPerDisponivel = false;
        }
        return listPerDisponivel;
    }

    public List<SelectItem> getListaModulos() {
        PermissaoUsuarioDB db = new PermissaoUsuarioDBToplink();
        List<SelectItem> result = new ArrayList<SelectItem>();
        List modulos = db.pesquisaTodosModuloOrdenado();
        for (int i = 0; i  < modulos.size(); i++) {
            result.add(new SelectItem(new Integer(i),
                    ((Modulo) modulos.get(i)).getDescricao(),
                    Integer.toString(((Modulo) modulos.get(i)).getId())));
        }
        return result;
    }

    public List<SelectItem> getListaRotinas() {
        RotinaDB rotinaDB = new RotinaDBToplink();
        List<SelectItem> result = new ArrayList<SelectItem>();
        List rotinas = rotinaDB.pesquisaTodosOrdenadoAtivo();
        for (int i = 0; i  < rotinas.size(); i++) {
            result.add(new SelectItem(new Integer(i),
                    ((Rotina) rotinas.get(i)).getRotina(),
                    Integer.toString(((Rotina) rotinas.get(i)).getId())));
        }
        return result;
    }

    public List<SelectItem> getListaEventos() {
        PermissaoUsuarioDB db = new PermissaoUsuarioDBToplink();
        List<SelectItem> result = new ArrayList<SelectItem>();
        List eventos = db.pesquisaTodosEventoOrdenado();
        for (int i = 0; i  < eventos.size(); i++) {
            result.add(new SelectItem(new Integer(i),
                    ((Evento) eventos.get(i)).getDescricao(),
                    Integer.toString(((Evento) eventos.get(i)).getId())));
        }
        return result;
    }

    public List<SelectItem> getListaNiveis() {
        PermissaoUsuarioDB db = new PermissaoUsuarioDBToplink();
        List<SelectItem> result = new ArrayList<SelectItem>();
        List niveis = db.pesquisaTodosNiveis();
        for (int i = 0; i  < niveis.size(); i++) {
            result.add(new SelectItem(new Integer(i),
                    ((Nivel) niveis.get(i)).getDescricao(),
                    Integer.toString(((Nivel) niveis.get(i)).getId())));
        }
        carregaPerAdicionada = true;
        carregaPerDisponivel = true;
        return result;
    }

    public List<SelectItem> getListaDepartamentos() {
        PermissaoUsuarioDB db = new PermissaoUsuarioDBToplink();
        List<SelectItem> result = new ArrayList<SelectItem>();
        List departamentos = db.pesquisaTodosDepOrdenado();
        for (int i = 0; i  < departamentos.size(); i++) {
            result.add(new SelectItem(new Integer(i),
                    ((Departamento) departamentos.get(i)).getDescricao(),
                    Integer.toString(((Departamento) departamentos.get(i)).getId())));
        }
        carregaPerAdicionada = true;
        carregaPerDisponivel = true;
        return result;
    }

    public String adicionarPermissaoDpto() {
        if (!listPerDisponivel.isEmpty()) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
//            PermissaoUsuarioDB dbpu = new PermissaoUsuarioDBToplink();
//            UsuarioAcesso user = new UsuarioAcesso();
            sv.abrirTransacao();
            for (int i = 0; i < listPerDisponivel.size(); i++) {
                if ((Boolean) ((DataObject) listPerDisponivel.get(i)).getArgumento0() == true) {
                    Permissao perm = (Permissao) ((DataObject) listPerDisponivel.get(i)).getArgumento1();
                    Departamento depto = (Departamento) sv.pesquisaCodigo(Integer.parseInt(getListaDepartamentos().get(idDepartamento).getDescription()), "Departamento");
                    Nivel niv = (Nivel) sv.pesquisaCodigo(Integer.parseInt(getListaNiveis().get(idNivel).getDescription()), "Nivel");
                    permissaoDepartamento.setPermissao(perm);
                    permissaoDepartamento.setDepartamento(depto);
                    permissaoDepartamento.setNivel(niv);

                    if (!sv.inserirObjeto(permissaoDepartamento)) {
                        sv.desfazerTransacao();
                        return "permissaoDepartamento";
                    }

//                    List<PermissaoUsuario> lista = dbpu.pesquisaPermissaoUser(depto.getId(), niv.getId());
//
//                    for (int w = 0; w < lista.size(); w++) {
//                        user.setPermissao(permissaoDepartamento.getPermissao());
//                        user.setUsuario(lista.get(w).getUsuario());
//
//                        if (!sv.inserirObjeto(user)) {
//                            sv.desfazerTransacao();
//                            return "permissaoDepartamento";
//                        }
//
//                        user = new UsuarioAcesso();
//                    }
                    permissaoDepartamento = new PermissaoDepartamento();
                }
            }
            sv.comitarTransacao();
        }
        carregaPerAdicionada = true;
        carregaPerDisponivel = true;
        return "permissaoDepartamento";
    }

    public String adicionarTodasPermissaoDpto() {
        if (!listPerDisponivel.isEmpty()) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
//            PermissaoUsuarioDB dbpu = new PermissaoUsuarioDBToplink();
//            UsuarioAcesso user = new UsuarioAcesso();
            sv.abrirTransacao();
            for (int i = 0; i < listPerDisponivel.size(); i++) {
                Permissao perm = (Permissao) ((DataObject) listPerDisponivel.get(i)).getArgumento1();
                Departamento depto = (Departamento) sv.pesquisaCodigo(Integer.parseInt(getListaDepartamentos().get(idDepartamento).getDescription()), "Departamento");
                Nivel niv = (Nivel) sv.pesquisaCodigo(Integer.parseInt(getListaNiveis().get(idNivel).getDescription()), "Nivel");
                permissaoDepartamento.setPermissao(perm);
                permissaoDepartamento.setDepartamento(depto);
                permissaoDepartamento.setNivel(niv);

                if (!sv.inserirObjeto(permissaoDepartamento)) {
                    sv.desfazerTransacao();
                    return "permissaoDepartamento";
                }

//                List<PermissaoUsuario> lista = dbpu.pesquisaPermissaoUser(depto.getId(), niv.getId());
//
//                for (int w = 0; w < lista.size(); w++) {
//                    user.setPermissao(permissaoDepartamento.getPermissao());
//                    user.setUsuario(lista.get(w).getUsuario());
//
//                    if (!sv.inserirObjeto(user)) {
//                        sv.desfazerTransacao();
//                        return "permissaoDepartamento";
//                    }
//
//                    user = new UsuarioAcesso();
//                }
                permissaoDepartamento = new PermissaoDepartamento();
            }
            sv.comitarTransacao();
        }
        carregaPerAdicionada = true;
        carregaPerDisponivel = true;
        return "permissaoDepartamento";
    }

    public String excluirPermissaoDepto() {
        if (!listPerAdicionada.isEmpty()) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
//            PermissaoUsuarioDB dbpu = new PermissaoUsuarioDBToplink();
            sv.abrirTransacao();
            for (int i = 0; i < listPerAdicionada.size(); i++) {
                if ((Boolean) ((DataObject) listPerAdicionada.get(i)).getArgumento0() == true) {

                    permissaoDepartamento = (PermissaoDepartamento) ((DataObject) listPerAdicionada.get(i)).getArgumento2();
                    if (!sv.deletarObjeto((PermissaoDepartamento) sv.pesquisaCodigo(permissaoDepartamento.getId(), "PermissaoDepartamento"))) {
                        sv.desfazerTransacao();
                        return "permissaoDepartamento";
                    }

//                    List<UsuarioAcesso> lista = dbpu.pesquisaAcesso(permissaoDepartamento.getPermissao().getId());
//                    for (int w = 0; w < lista.size(); w++) {
//                        if (!sv.deletarObjeto(sv.pesquisaCodigo(lista.get(w).getId(), "UsuarioAcesso"))) {
//                            sv.desfazerTransacao();
//                            return "permissaoDepartamento";
//                        }
//                    }
                }
                permissaoDepartamento = new PermissaoDepartamento();
            }
            sv.comitarTransacao();
        }
        carregaPerAdicionada = true;
        carregaPerDisponivel = true;
        return "permissaoDepartamento";
    }

    public String excluirTodasPermissaoDepto() {
        if (!listPerAdicionada.isEmpty()) {
//            PermissaoUsuarioDB dbpu = new PermissaoUsuarioDBToplink();
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            sv.abrirTransacao();
            for (int i = 0; i < listPerAdicionada.size(); i++) {
                permissaoDepartamento = (PermissaoDepartamento) ((DataObject) listPerAdicionada.get(i)).getArgumento2();
                if (!sv.deletarObjeto((PermissaoDepartamento) sv.pesquisaCodigo(permissaoDepartamento.getId(), "PermissaoDepartamento"))) {
                    sv.desfazerTransacao();
                    return "permissaoDepartamento";
                }
//                List<UsuarioAcesso> lista = dbpu.pesquisaAcesso(permissaoDepartamento.getPermissao().getId());
//                for (int w = 0; w < lista.size(); w++) {
//                    if (!sv.deletarObjeto(sv.pesquisaCodigo(lista.get(w).getId(), "UsuarioAcesso"))) {
//                        sv.desfazerTransacao();
//                        return "permissaoDepartamento";
//                    }
//                }
            }
            sv.comitarTransacao();
        }
        permissaoDepartamento = new PermissaoDepartamento();
        carregaPerAdicionada = true;
        carregaPerDisponivel = true;
        return "permissaoDepartamento";
    }

    public List getPermissaoAdicionada() {
        if (carregaPerAdicionada) {
            listPerAdicionada = new ArrayList();
            PermissaoDepartamentoDB db = new PermissaoDepartamentoDBToplink();
            int idDepto = Integer.parseInt(getListaDepartamentos().get(idDepartamento).getDescription());
            int idNiv = Integer.parseInt(getListaNiveis().get(idNivel).getDescription());
            List result = db.pesquisaPermissaoAdc(idDepto, idNiv);
            DataObject dtObject;
            for (int i = 0; i < result.size(); i++) {
                dtObject = new DataObject(new Boolean(false),
                        ((PermissaoDepartamento) result.get(i)).getPermissao(),
                        ((PermissaoDepartamento) result.get(i)),
                        null,
                        null,
                        null);
                listPerAdicionada.add(dtObject);
            }
            carregaPerAdicionada = false;
        }
        return listPerAdicionada;
    }

    public List getListaPermissaoDpto() {
        PermissaoDepartamentoDB db = new PermissaoDepartamentoDBToplink();
        List result = db.pesquisaTodos();
        return result;
    }

    public Permissao getPermissao() {
        return permissao;
    }

    public void setPermissao(Permissao permissao) {
        this.permissao = permissao;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public int getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(int idModulo) {
        this.idModulo = idModulo;
    }

    public int getIdRotina() {
        return idRotina;
    }

    public void setIdRotina(int idRotina) {
        this.idRotina = idRotina;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public String getIndicaTab() {
        return indicaTab;
    }

    public void setIndicaTab(String indicaTab) {
        this.indicaTab = indicaTab;
    }

    public String getTabDisabled() {
        return tabDisabled;
    }

    public void setTabDisabled(String tabDisabled) {
        this.tabDisabled = tabDisabled;
    }

    public int getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public int getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(int idNivel) {
        this.idNivel = idNivel;
    }

    public PermissaoDepartamento getPermissaoDepartamento() {
        return permissaoDepartamento;
    }

    public void setPermissaoDepartamento(PermissaoDepartamento permissaoDepartamento) {
        this.permissaoDepartamento = permissaoDepartamento;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public List<Permissao> getListaPermissoes() {
        if (listaPermissoes.isEmpty()) {
            PermissaoDB db = new PermissaoDBToplink();
            listaPermissoes = db.pesquisaTodosAgrupados();
        }
        return listaPermissoes;
    }

    public void setListaPermissoes(List<Permissao> listaPermissoes) {
        this.listaPermissoes = listaPermissoes;
    }
}
