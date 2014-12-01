package br.com.rtools.utilitarios;

import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import java.io.File;
import java.util.Collection;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class Jasper {

    public static void printReports(String jasperName, String fileName, Collection c) {
        if (fileName.isEmpty() || jasperName.isEmpty() || c.isEmpty()) {
            GenericaMensagem.info("Sistema", "Erro ao criar relatório!");
            return;
        }
        if (!Diretorio.criar("Arquivos/downloads/relatorios/" + fileName)) {
            GenericaMensagem.info("Sistema", "Erro ao criar diretório!");
            return;
        }
        try {
            JasperReport jasper;
            FacesContext faces = FacesContext.getCurrentInstance();
            if (new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Relatorios/" + jasperName)).exists()) {
                jasper = (JasperReport) JRLoader.loadObject(new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Relatorios/ " + jasperName)));
            } else {
                jasper = (JasperReport) JRLoader.loadObject(new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath(jasperName)));
            }
            try {
                JRBeanCollectionDataSource dtSource;
                dtSource = new JRBeanCollectionDataSource(c);
                net.sf.jasperreports.engine.JasperPrint print = JasperFillManager.fillReport(jasper, null, dtSource);
                byte[] arquivo = JasperExportManager.exportReportToPdf(print);
                String nomeDownload = "relatorio_" + fileName + "_" + DataHoje.horaMinuto().replace(":", "") + ".pdf";
                SalvaArquivos salvaArquivos = new SalvaArquivos(arquivo, nomeDownload, false);
                String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/relatorios/" + fileName + "/");
                salvaArquivos.salvaNaPasta(pathPasta);
                Download download = new Download(nomeDownload, pathPasta, "application/pdf", FacesContext.getCurrentInstance());
                download.baixar();
                download.remover();
            } catch (JRException erro) {
                GenericaMensagem.info("Sistema", "O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
                System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
            }
        } catch (JRException erro) {
            GenericaMensagem.info("Sistema", "O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
            System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
        }
    }

}
