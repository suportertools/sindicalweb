package br.com.rtools.impressao.beans;

import br.com.rtools.arrecadacao.Rais;
import br.com.rtools.impressao.ParametroProtocolo;
import br.com.rtools.impressao.ParametroRais;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.AnaliseString;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Download;
import br.com.rtools.utilitarios.SalvaArquivos;
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
public class ImprimirRais implements Serializable {

    public void imprimir(Rais r) {
        try {
            Collection lista = parametro(r);
            File fl = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/RAIS.jasper"));
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(fl);
            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
            JasperPrint print = JasperFillManager.fillReport(jasperReport, null, dtSource);
            byte[] arquivo = JasperExportManager.exportReportToPdf(print);
            String nomeDownload = "imp_certificado_rais_" + r.getId() + ".pdf";
            String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/temp");
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
            e.getMessage();
        }
    }

    public Collection<ParametroRais> parametro(Rais r) {
        DaoInterface di = new Dao();
        Juridica sindicato = (Juridica) di.find(new Juridica(), 1);
        Collection lista = new ArrayList<ParametroProtocolo>();
        String empregadoFiliado = "Não";
        if (r.isEmpregadoFiliado()) {
            empregadoFiliado = "Sim";
        }
        String indicadorAlvara = "";
        if (r.getIndicadorAlvara() != null) {
            indicadorAlvara = r.getIndicadorAlvara().getDescricao();
        }
        String tipoDeficiencia;
        String raca;
        String nacionalidade;
        String classificacaoEconomica;
        String escolaridade;
        if (r.getTipoDeficiencia() == null) {
            tipoDeficiencia = "Não possui/informada";
        } else {
            tipoDeficiencia = r.getTipoDeficiencia().getDescricao();
        }
        if (r.getRaca() == null) {
            raca = "Não específicada";
        } else {
            raca = r.getRaca().getDescricao();
        }
        if (r.getNacionalidade() == null) {
            nacionalidade = "Não específicada";
        } else {
            nacionalidade = r.getNacionalidade().getDescricao();
        }
        if (r.getClassificacaoEconomica() == null) {
            classificacaoEconomica = "Não específicada";
        } else {
            classificacaoEconomica = r.getClassificacaoEconomica().getDescricao() + " - De: " + r.getClassificacaoEconomica().getSalarioMinimoInicial() + " - " + r.getClassificacaoEconomica().getSalarioMinimoFinal() + " (SM) ";
        }
        if (r.getEscolaridade() == null) {
            escolaridade = "Não específicada";
        } else {
            escolaridade = r.getEscolaridade().getDescricao();
        }
        int anoBase = Integer.parseInt(DataHoje.livre(r.getEmissao(), "Y")) - 1;

        lista.add(new ParametroRais("" + r.getId(),
                DataHoje.data(),
                r.getEmissaoString(),
                nacionalidade,
                raca,
                r.getEmpresa().getPessoa().getNome(),
                r.getEmpresa().getPessoa().getDocumento(),
                escolaridade,
                r.getSisPessoa().getNome(),
                r.getSisPessoa().getDocumento(),
                r.getSisPessoa().getSexo(),
                r.getSisPessoa().getNascimento(),
                classificacaoEconomica,
                r.getProfissao().getProfissao(),
                r.getProfissao().getCbo(),
                r.getTipoRemuneracao().getDescricao(),
                tipoDeficiencia,
                indicadorAlvara,
                AnaliseString.converteNullString(r.getAdmissaoString()),
                AnaliseString.converteNullString(r.getDemissaoString()),
                r.getAfastamentoString(),
                r.getMotivoAfastamento(),
                r.getObservacao(),
                r.getCargaHorariaString(),
                r.getSalarioString(),
                r.getFuncao(),
                r.getCarteira(), r.getSerie(),
                "",
                "",
                empregadoFiliado, r.getPis(),
                "" + anoBase,
                ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
                sindicato.getPessoa().getNome(),
                sindicato.getPessoa().getSite(),
                sindicato.getPessoa().getTipoDocumento().getDescricao(),
                sindicato.getPessoa().getDocumento(),
                sindicato.getPessoa().getEmail1()
        ));
        return lista;
    }
}
