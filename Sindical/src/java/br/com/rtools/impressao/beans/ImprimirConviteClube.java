package br.com.rtools.impressao.beans;

import br.com.rtools.associativo.ConviteMovimento;
import br.com.rtools.impressao.ConviteClube;
import br.com.rtools.impressao.ParametroProtocolo;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.db.PessoaEnderecoDB;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.sistema.Links;
import br.com.rtools.sistema.db.LinksDB;
import br.com.rtools.sistema.db.LinksDBToplink;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Diretorio;
import br.com.rtools.utilitarios.Download;
import br.com.rtools.utilitarios.EnviarEmail;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaString;
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
public class ImprimirConviteClube implements Serializable {

    public void imprimir(ConviteMovimento cm) {
        try {
            Collection lista = parametroConvite(cm);
            File fl = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/CONVITE_CLUBE.jasper"));
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(fl);
            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
            JasperPrint print = JasperFillManager.fillReport(jasperReport, null, dtSource);
            Diretorio.criar("Arquivos/downloads/convite");
            byte[] arquivo = JasperExportManager.exportReportToPdf(print);
            String nomeDownload = "imp_convite_clube_" + cm.getId() + ".pdf";
            String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/convite");
            SalvaArquivos salvaArquivos = new SalvaArquivos(arquivo, nomeDownload, false);
            salvaArquivos.salvaNaPasta(pathPasta);
            Download download = new Download(nomeDownload, pathPasta, "application/pdf", FacesContext.getCurrentInstance());
            download.baixar();
            download.remover();
        } catch (JRException e) {
            return;
        }
    }

    public void enviar(ConviteMovimento cm) {
        if (cm.getSisPessoa().getEmail1().isEmpty()) {
            GenericaMensagem.info("Validação", "Informar e-mail");
            return;
        }
        try {
            Collection lista = parametroConvite(cm);
            File fl = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/CONVITE_CLUBE.jasper"));
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(fl);
            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
            JasperPrint print = JasperFillManager.fillReport(jasperReport, null, dtSource);
            byte[] arquivo = JasperExportManager.exportReportToPdf(print);
            String nomeDownload = "imp_convite_clube_" + cm.getId() + ".pdf";
            String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/protocolo");
            Diretorio.criar("Arquivos/downloads/convite");
            SalvaArquivos salvaArquivos = new SalvaArquivos(arquivo, nomeDownload, false);
            salvaArquivos.salvaNaPasta(pathPasta);
            LinksDB db = new LinksDBToplink();
            Links link = db.pesquisaNomeArquivo(nomeDownload);
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            if (link == null) {
                link = new Links();
                link.setCaminho("/Sindical/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/convite");
                link.setNomeArquivo(nomeDownload);
                sv.abrirTransacao();
                if (sv.inserirObjeto(link)) {
                    sv.comitarTransacao();
                } else {
                    sv.desfazerTransacao();
                }
            }
            Pessoa pessoa = new Pessoa();
            pessoa.setNome(cm.getSisPessoa().getNome());
            pessoa.setDocumento(cm.getSisPessoa().getDocumento());
            pessoa.setEmail1(cm.getSisPessoa().getEmail1());
            List<Pessoa> p = new ArrayList();
            p.add(pessoa);
            String[] ret;
            Registro registro = (Registro) new SalvarAcumuladoDBToplink().pesquisaCodigo(1, "Registro");
            if (registro.isEnviarEmailAnexo()) {
                List<File> fls = new ArrayList<File>();
                fls.add(new File(pathPasta + "/" + nomeDownload));
                ret = EnviarEmail.EnviarEmailPersonalizado(registro, p, " <h5>Baixe seu convite que esta anexado neste email</5><br /><br />", fls, "Envio de convite clube");
                salvaArquivos.remover();
            } else {
                ret = EnviarEmail.EnviarEmailPersonalizado(registro,
                        p,
                        " <h5>Visualize seu protocolo clicando no link abaixo</5><br /><br />"
                        + " <a href='" + registro.getUrlPath() + "/Sindical/acessoLinks.jsf?cliente=" + ControleUsuarioBean.getCliente() + "&amp;arquivo=" + nomeDownload + "' target='_blank'>Clique aqui para abrir seu protocolo</a><br />",
                        //" <a href='"+registro.getUrlPath()+"/Sindical/Arquivos/downloads/protocolo/"+nomeDownload+".pdf' target='_blank'>Clique aqui para abrir seu protocolo</a><br />", 
                        new ArrayList(),
                        "Envio de convite clube");

            }
            if (!ret[1].isEmpty()) {
                GenericaMensagem.info("E-mail", ret[1]);
            } else {
                GenericaMensagem.info("E-mail", ret[0]);
            }
        } catch (JRException e) {
            NovoLog log = new NovoLog();
            log.live("Erro de envio de protocolo por e-mail: Mensagem: " + e.getMessage());
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

    public Collection<ParametroProtocolo> parametroConvite(ConviteMovimento cm) {
        if (cm.getId() == -1) {
            return new ArrayList();
        }
        Collection lista = new ArrayList<ConviteClube>();
        DataHoje dh = new DataHoje();

        List listSemana = new ArrayList();
        if (cm.getConviteServico().isDomingo()) {
            listSemana.add("Dom");
        }
        if (cm.getConviteServico().isSegunda()) {
            listSemana.add("Seg");
        }
        if (cm.getConviteServico().isTerca()) {
            listSemana.add("Ter");
        }
        if (cm.getConviteServico().isQuarta()) {
            listSemana.add("Qua");
        }
        if (cm.getConviteServico().isQuinta()) {
            listSemana.add("Qui");
        }
        if (cm.getConviteServico().isSexta()) {
            listSemana.add("Sex");
        }
        if (cm.getConviteServico().isSabado()) {
            listSemana.add("Sáb");
        }
        if (cm.getConviteServico().isFeriado()) {
            listSemana.add("Feriado");
        }

        lista.add(new ConviteClube(
                cm.getSisPessoa().getNome(),
                cm.getEmissao(),
                "VÁLIDO ATÉ " + dh.incrementarMeses(1, cm.getValidade()),
                ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoConvite.png"),
                "00000000000000".substring(0, 14 - (""+cm.getId()).length()) + cm.getId(),
                GenericaString.converterNullToString(cm.getSisPessoa().getObservacao()),
                "NO(S) DIA(S): " + listSemana
        ));
        return lista;
    }
}
