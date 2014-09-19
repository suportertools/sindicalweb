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
import br.com.rtools.utilitarios.Download;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.SalvaArquivos;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@ManagedBean
@ViewScoped
public class SenhaHomologacao implements Serializable {

    public void imprimir(Agendamento a) {
//        if (a.getRecepcao() == null) {
//            return;
//        }
//        if(a.getRecepcao().getPreposto().isEmpty()) {
//            return;
//        }
        try {
            Collection lista = parametros(a);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File((((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/HOM_SENHA.jasper"))));
            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
            JasperPrint print = JasperFillManager.fillReport(jasperReport, null, dtSource);
            byte[] arquivo = JasperExportManager.exportReportToPdf(print);
            String nomeDownload = "senha_" + DataHoje.hora().replace(":", "") + ".pdf";
            String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/senhas");
            Diretorio.criar("Arquivos/senhas");
            if (!new File(pathPasta).exists()) {
                File file = new File(pathPasta);
                file.mkdir();
            }
            SalvaArquivos salvaArquivos = new SalvaArquivos(arquivo, nomeDownload, false);
            salvaArquivos.salvaNaPasta(pathPasta);
            Download download = new Download(nomeDownload, pathPasta, "application/pdf", FacesContext.getCurrentInstance());
            download.baixar();
            download.remover();
        } catch (JRException e) {
        }
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
