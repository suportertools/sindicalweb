package br.com.rtools.impressao.beans;

import br.com.rtools.homologacao.Agendamento;
import br.com.rtools.impressao.ParametroProtocolo;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.db.PessoaEnderecoDB;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.Download;
import br.com.rtools.utilitarios.SalvaArquivos;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.File;
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
public class ProtocoloAgendamento {

    public void imprimir(Agendamento a) {
        Collection lista = new ArrayList<ParametroProtocolo>();
        try {
            JasperReport jasper = (JasperReport) JRLoader.loadObject(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/PROTOCOLO.jasper"));
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

            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
            JasperPrint print = JasperFillManager.fillReport(
                    jasper,
                    null,
                    dtSource);
            byte[] arquivo = JasperExportManager.exportReportToPdf(print);
            String nomeDownload = "imp_protocolo_" + a.getId() + ".pdf";

            String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/protocolo");
            if (!new File(pathPasta).exists()) {
                File file = new File(pathPasta);
                file.mkdir();
            }

            SalvaArquivos sa = new SalvaArquivos(arquivo, nomeDownload, false);
            sa.salvaNaPasta(pathPasta);

            Download download = new Download(nomeDownload,
                    pathPasta,
                    "application/pdf",
                    FacesContext.getCurrentInstance());
            download.baixar();
        } catch (JRException e) {
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
}
