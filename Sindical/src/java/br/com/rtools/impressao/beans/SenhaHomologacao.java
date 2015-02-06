package br.com.rtools.impressao.beans;

import br.com.rtools.homologacao.Agendamento;
import br.com.rtools.homologacao.Senha;
import br.com.rtools.homologacao.db.HomologacaoDB;
import br.com.rtools.homologacao.db.HomologacaoDBToplink;
import br.com.rtools.impressao.ParametroSenha;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Diretorio;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Jasper;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

@ManagedBean
@ViewScoped
public class SenhaHomologacao implements Serializable {

    public void imprimir(Agendamento a) {
        Collection lista = parametros(a);
        imprimir(lista);
    }

    public void imprimir(Collection lista) {
        Jasper.IS_DOWNLOAD = true;
        String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/senhas");
        Diretorio.criar("");
        if (!new File(pathPasta).exists()) {
            File file = new File(pathPasta);
            file.mkdir();
        }
        Jasper.PATH = "";
        Jasper.PART_NAME = "";
        Jasper.printReports("/Relatorios/HOM_SENHA.jasper", "senhas", lista);
    }

    public Collection<ParametroSenha> parametros(Agendamento a) {
        Dao dao = new Dao();
        dao.openTransaction();
        Collection<ParametroSenha> list = parametros(a, dao);
        if (list == null) {
            GenericaMensagem.warn("Erro", "Ao gerar senha!");
            dao.rollback();
            return new ArrayList();
        } else {
            GenericaMensagem.warn("Sucesso", "Senha gerada com sucesso!");
            dao.commit();
        }
        return list;
    }

    public Collection<ParametroSenha> parametros(Agendamento a, Dao dao) {
        if (a.getId() == -1) {
            return null;
        }
        Collection lista = new ArrayList<ParametroSenha>();
        HomologacaoDB db = new HomologacaoDBToplink();
        Senha senha = db.pesquisaSenhaAgendamento(a.getId());
        MacFilial mc = MacFilial.getAcessoFilial();
        if (senha.getId() == -1) {
            senha.setAgendamento(a);
            senha.setDtData(DataHoje.dataHoje());
            senha.setHora(DataHoje.horaMinuto());
            senha.setUsuario(((Usuario) GenericaSessao.getObject("sessaoUsuario")));
            senha.setFilial(mc.getFilial());
            senha.setSenha(db.pesquisaUltimaSenha(mc.getFilial().getId()) + 1);
            if (!dao.save(senha)) {
                return null;
            }
        } else {
            if (!dao.update(senha)) {
                return null;
            }
        }
        try {
            if (senha.getId() != -1) {
                lista.add(new ParametroSenha(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
                        senha.getFilial().getFilial().getPessoa().getNome(),
                        senha.getFilial().getFilial().getPessoa().getDocumento(),
                        senha.getAgendamento().getPessoaEmpresa().getJuridica().getPessoa().getNome(),
                        senha.getAgendamento().getPessoaEmpresa().getJuridica().getPessoa().getDocumento(),
                        (senha.getAgendamento().getRecepcao() == null) ? "" : senha.getAgendamento().getRecepcao().getPreposto(),
                        senha.getAgendamento().getPessoaEmpresa().getFisica().getPessoa().getNome(),
                        senha.getUsuario().getPessoa().getNome(),
                        senha.getData(),
                        senha.getHora(),
                        String.valueOf(senha.getSenha())));
            }
        } catch (Exception e) {
            return null;
        }
        return lista;
    }
}
