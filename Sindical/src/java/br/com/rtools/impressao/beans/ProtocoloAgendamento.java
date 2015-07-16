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
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.sistema.Email;
import br.com.rtools.sistema.EmailPessoa;
import br.com.rtools.sistema.Links;
import br.com.rtools.sistema.db.LinksDB;
import br.com.rtools.sistema.db.LinksDBToplink;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Jasper;
import br.com.rtools.utilitarios.Mail;
import br.com.rtools.utilitarios.SalvaArquivos;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
            Jasper.PART_NAME = "" + a.getId();
            Jasper.PATH = "downloads";
            Jasper.IGNORE_UUID = true;
            Jasper.printReports("/Relatorios/PROTOCOLO.jasper", "protocolo", (Collection) parametroProtocolos(a));
        } catch (Exception e) {
        }
    }

    public void enviar(Agendamento a) {
        if (a.getEmail().isEmpty()) {
            GenericaMensagem.info("Validação", "Informar e-mail");
            return;
        }
        try {
            Collection lista = parametroProtocolos(a);
            File fl = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/PROTOCOLO.jasper"));
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(fl);
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
            Dao dao = new Dao();
            if (link == null) {
                link = new Links();
                link.setCaminho("/Sindical/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/protocolo");
                link.setNomeArquivo(nomeDownload);
                link.setPessoa(a.getPessoaEmpresa().getFisica().getPessoa());

                dao.openTransaction();
                if (dao.save(link)) {
                    dao.commit();
                } else {
                    dao.rollback();
                }
            }
            List<Pessoa> p = new ArrayList();
            a.getPessoaEmpresa().getFisica().getPessoa().setEmail1(a.getEmail());
            p.add(a.getPessoaEmpresa().getFisica().getPessoa());
            Registro registro = (Registro) dao.find(new Registro(), 1);
            Mail mail = new Mail();
            Email email = new Email(
                    -1,
                    DataHoje.dataHoje(),
                    DataHoje.livre(new Date(), "HH:mm"),
                    (Usuario) GenericaSessao.getObject("sessaoUsuario"),
                    (Rotina) dao.find(new Rotina(), 113),
                    null,
                    "Protocolo de homologação n°" + a.getId(),
                    "",
                    false,
                    false
            );
            if (registro.isEnviarEmailAnexo()) {
                List<File> fls = new ArrayList<File>();
                fls.add(new File(pathPasta + "/" + nomeDownload));
                mail.setFiles(fls);
                email.setMensagem("<h5>Baixe seu protocolo que esta anexado neste email</5><br /><br />");
            } else {
                email.setMensagem(" <h5>Visualize seu protocolo clicando no link abaixo</5><br /><br />    "
                        + " <a href='" + registro.getUrlPath() + "/Sindical/acessoLinks.jsf?cliente=" + ControleUsuarioBean.getCliente() + "&amp;arquivo=" + nomeDownload + "' target='_blank'>Clique aqui para abrir seu protocolo</a><br />"
                );
            }
            mail.setEmail(email);
            List<EmailPessoa> emailPessoas = new ArrayList<EmailPessoa>();
            EmailPessoa emailPessoa = new EmailPessoa();
            List<Pessoa> pessoas = (List<Pessoa>) p;
            for (Pessoa p1 : pessoas) {
                emailPessoa.setDestinatario(p1.getEmail1());
                emailPessoa.setPessoa(p1);
                emailPessoa.setRecebimento(null);
                emailPessoas.add(emailPessoa);
                mail.setEmailPessoas(emailPessoas);
                emailPessoa = new EmailPessoa();
            }
            String[] retorno = mail.send("personalizado");
            if (!retorno[1].isEmpty()) {
                GenericaMensagem.warn("E-mail", retorno[1]);
            } else {
                GenericaMensagem.info("E-mail", retorno[0]);
            }
            if (!mail.getEmailArquivos().isEmpty()) {
                salvaArquivos.remover();
            }
        } catch (JRException e) {
            NovoLog log = new NovoLog();
            log.live("Erro de envio de protocolo por e-mail: Mensagem: " + e.getMessage() + " - Causa: " + e.getCause() + " - Caminho: " + e.getStackTrace().toString());
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
        Dao dao = new Dao();
        Juridica sindicato = (Juridica) dao.find(new Juridica(), 1);
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
        Registro registro = (Registro) dao.find(new Registro(), 1);
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
