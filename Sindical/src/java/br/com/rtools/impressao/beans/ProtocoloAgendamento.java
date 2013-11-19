package br.com.rtools.impressao.beans;

import br.com.rtools.homologacao.Agendamento;
import br.com.rtools.impressao.ParametroProtocolo;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.db.PessoaEnderecoDB;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.sistema.Links;
import br.com.rtools.sistema.db.LinksDB;
import br.com.rtools.sistema.db.LinksDBToplink;
import br.com.rtools.utilitarios.Download;
import br.com.rtools.utilitarios.EnviarEmail;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.SalvaArquivos;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
public class ProtocoloAgendamento implements Serializable {

    public void imprimir(Agendamento a) {
        try {
            Collection lista = parametroProtocolos(a);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/PROTOCOLO.jasper"));
            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
            JasperPrint print = JasperFillManager.fillReport(jasperReport, null, dtSource);
            byte[] arquivo = JasperExportManager.exportReportToPdf(print);
            String nomeDownload = "imp_protocolo_" + a.getId() + ".pdf";
            String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/protocolo");
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

    public void enviar(Agendamento a) {
        if (a.getEmail().isEmpty()) {
            GenericaMensagem.info("Validação", "Informar e-mail");
            return;
        }
        try {
            Collection lista = parametroProtocolos(a);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/PROTOCOLO.jasper"));
            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
            JasperPrint print = JasperFillManager.fillReport(jasperReport, null, dtSource);
            byte[] arquivo = JasperExportManager.exportReportToPdf(print);
            String nomeDownload = "envio_protocolo_" + a.getId() + ".pdf";
            String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/protocolo");
            if (!new File(pathPasta).exists()) {
                File fNew = new File(pathPasta);
                fNew.mkdir();
            }
            SalvaArquivos salvaArquivos = new SalvaArquivos(arquivo, nomeDownload, false);
            salvaArquivos.salvaNaPasta(pathPasta);
            LinksDB db = new LinksDBToplink();
            Links link = db.pesquisaNomeArquivo(nomeDownload);
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            if (link == null) {
                link = new Links();
                link.setCaminho("/Sindical/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/protocolo");
                link.setNomeArquivo(nomeDownload);
                link.setPessoa(a.getPessoaEmpresa().getFisica().getPessoa());

                sv.abrirTransacao();
                if (sv.inserirObjeto(link)) {
                    sv.comitarTransacao();
                } else {
                    sv.desfazerTransacao();
                }
            }
            List<Pessoa> p = new ArrayList();
            a.getPessoaEmpresa().getFisica().getPessoa().setEmail1(a.getEmail());
            p.add(a.getPessoaEmpresa().getFisica().getPessoa());
            String[] ret;
            Registro registro = (Registro) new SalvarAcumuladoDBToplink().pesquisaCodigo(1, "Registro");
            if (registro.isEnviarEmailAnexo()) {
                List<File> fls = new ArrayList<File>();
                fls.add(new File(pathPasta + "/" + nomeDownload));
                ret = EnviarEmail.EnviarEmailPersonalizado(registro, p, " <h5>Baixe seu protocolo que esta anexado neste email</5><br /><br />", fls, "Envio de protocolo de homologação");
                salvaArquivos.remover();
            } else {
                ret = EnviarEmail.EnviarEmailPersonalizado(registro,
                        p,
                        " <h5>Visualize seu protocolo clicando no link abaixo</5><br /><br />"
                        + " <a href='" + registro.getUrlPath() + "/Sindical/acessoLinks.jsf?cliente=" + ControleUsuarioBean.getCliente() + "&amp;arquivo=" + nomeDownload + "' target='_blank'>Clique aqui para abrir seu protocolo</a><br />",
                        //" <a href='"+registro.getUrlPath()+"/Sindical/Arquivos/downloads/protocolo/"+nomeDownload+".pdf' target='_blank'>Clique aqui para abrir seu protocolo</a><br />", 
                        new ArrayList(),
                        "Envio de protocolo de homologação");

            }
            if (!ret[1].isEmpty()) {
                GenericaMensagem.info("E-mail", ret[1]);
            } else {
                GenericaMensagem.info("E-mail", ret[0]);
            }
        } catch (JRException e) {
            NovoLog log = new NovoLog();
            log.novo("Erro de envio de protocolo por e-mail:", "Mensagem: " + e.getMessage() + " - Causa: " + e.getCause() + " - Caminho: " + e.getStackTrace().toString());
        }
    }

    public PessoaEndereco pessoaEndereco(Filial f) {
        PessoaEndereco pessoaEndereco = new PessoaEndereco();
        if (f.getId() != -1) {
            PessoaEnderecoDB pessoaEnderecoDB = new PessoaEnderecoDBToplink();
            pessoaEndereco = pessoaEnderecoDB.pesquisaEndPorPessoaTipo(f.getFilial().getPessoa().getId(), 2);
        }
        return pessoaEndereco;
    }

    public Collection<ParametroProtocolo> parametroProtocolos(Agendamento a) {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        Juridica sindicato = (Juridica) sv.pesquisaCodigo(1, "Juridica");
        Juridica contabilidade;
        if (a.getPessoaEmpresa().getJuridica().getContabilidade() != null) {
            contabilidade = a.getPessoaEmpresa().getJuridica().getContabilidade();
        } else {
            contabilidade = new Juridica();
        }

        PessoaEndereco pessoaEndereco = pessoaEndereco(a.getFilial());
        String datax = "", horario = "";
        if (!a.getData().isEmpty()) {
            datax = a.getData();
            horario = a.getHorarios().getHora();
        }
        Registro registro = (Registro) new SalvarAcumuladoDBToplink().pesquisaCodigo(1, "Registro");
        Collection lista = new ArrayList<ParametroProtocolo>();
        lista.add(new ParametroProtocolo(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
                sindicato.getPessoa().getNome(),
                sindicato.getPessoa().getSite(),
                sindicato.getPessoa().getTipoDocumento().getDescricao(),
                sindicato.getPessoa().getDocumento(),
                pessoaEndereco.getEndereco().getDescricaoEndereco().getDescricao(),
                pessoaEndereco.getEndereco().getLogradouro().getDescricao(),
                pessoaEndereco.getNumero(),
                pessoaEndereco.getComplemento(),
                pessoaEndereco.getEndereco().getBairro().getDescricao(),
                pessoaEndereco.getEndereco().getCep(),
                pessoaEndereco.getEndereco().getCidade().getCidade(),
                pessoaEndereco.getEndereco().getCidade().getUf(),
                a.getFilial().getFilial().getPessoa().getTelefone1(),
                a.getFilial().getFilial().getPessoa().getEmail1(),
                String.valueOf(a.getId()),
                datax,
                horario,
                a.getPessoaEmpresa().getJuridica().getPessoa().getDocumento(),
                a.getPessoaEmpresa().getJuridica().getPessoa().getNome(),
                contabilidade.getPessoa().getNome(),
                a.getPessoaEmpresa().getFisica().getPessoa().getNome(),
                a.getPessoaEmpresa().getFisica().getPessoa().getDocumento(),
                registro.getDocumentoHomologacao(),
                registro.getFormaPagamentoHomologacao(),
                a.getEmissao()));
        return lista;
    }
}
