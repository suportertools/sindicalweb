package br.com.rtools.utilitarios;

import br.com.rtools.pessoa.Juridica;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class Jasper {

    public static String PATH = "downloads/relatorios";
    public static String PART_NAME = "relatorio";
    public static Boolean IS_DOWNLOAD = true;
    public static byte[] BYTES = null;
    public static Boolean IS_HEADER = false;
    /**
     * set: retrato or paisagem
     */
    public static String TYPE = "";

    public static void printReports(String jasperName, String fileName, Collection c) {
        printReports(jasperName, fileName, c, null);
    }

    public static void printReports(String jasperName, String fileName, Collection c, Map parameters) {
        Dao dao = new Dao();
        Juridica juridica = (Juridica) dao.find(new Juridica(), 1);
        byte[] bytesComparer = null;
        byte[] b;
        if (fileName.isEmpty() || jasperName.isEmpty() || c.isEmpty()) {
            GenericaMensagem.info("Sistema", "Erro ao criar relatório!");
            return;
        }
        if (!Diretorio.criar("Arquivos/" + PATH + "/" + fileName)) {
            GenericaMensagem.info("Sistema", "Erro ao criar diretório!");
            return;
        }
        // DEFINE O CABEÇALHO
        FacesContext faces = FacesContext.getCurrentInstance();
        String subreport = "";
        switch (TYPE) {
            case "retrato":
                subreport = ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/CABECALHO_RETRATO.jasper");
                break;
            case "paisagem":
                subreport = ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/CABECALHO_PAISAGEM.jasper");
                break;
            default:
                IS_HEADER = false;
                break;
        }
        if (IS_HEADER) {
            if (parameters == null) {
                parameters = new HashMap();
            }
            parameters.put("sindicato_nome", juridica.getPessoa().getNome());
            parameters.put("sindicato_documento", juridica.getPessoa().getDocumento());
            parameters.put("sindicato_site", juridica.getPessoa().getSite());
            parameters.put("sindicato_logradouro", juridica.getPessoa().getPessoaEndereco().getEndereco().getLogradouro().getDescricao());
            parameters.put("sindicato_endereco", juridica.getPessoa().getPessoaEndereco().getEndereco().getDescricaoEndereco().getDescricao());
            parameters.put("sindicato_numero", juridica.getPessoa().getPessoaEndereco().getNumero());
            parameters.put("sindicato_complemento", juridica.getPessoa().getPessoaEndereco().getComplemento());
            parameters.put("sindicato_bairro", juridica.getPessoa().getPessoaEndereco().getEndereco().getBairro().getDescricao());
            parameters.put("sindicato_cidade", juridica.getPessoa().getPessoaEndereco().getEndereco().getCidade().getCidade());
            parameters.put("sindicato_uf", juridica.getPessoa().getPessoaEndereco().getEndereco().getCidade().getUf());
            parameters.put("sindicato_cep", juridica.getPessoa().getPessoaEndereco().getEndereco().getCep());
            parameters.put("sindicato_logo", ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"));
            parameters.put("template_dir", subreport);
        }
        try {
            JasperReport jasper;
            if (new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Relatorios/" + jasperName)).exists()) {
                jasper = (JasperReport) JRLoader.loadObject(new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Relatorios/ " + jasperName)));
            } else {
                jasper = (JasperReport) JRLoader.loadObject(new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath(jasperName)));
            }
            try {
                JRBeanCollectionDataSource dtSource;
                dtSource = new JRBeanCollectionDataSource(c);
                JasperPrint print = JasperFillManager.fillReport(jasper, parameters, dtSource);
                if (bytesComparer == BYTES) {
                    b = JasperExportManager.exportReportToPdf(print);
                } else {
                    b = BYTES;
                }
                String downloadName = PART_NAME + "_" + fileName + "_" + DataHoje.horaMinuto().replace(":", "") + ".pdf";
                String dirPath = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/" + PATH + "/" + fileName + "/");
                try {
                    File file = new File(dirPath + "/" + downloadName);
                    try (FileOutputStream out = new FileOutputStream(file)) {
                        out.write(b);
                        out.flush();
                    }
                } catch (IOException e) {
                    System.out.println(e);
                    return;
                }
                if (IS_DOWNLOAD) {
                    Download download = new Download(downloadName, dirPath, "application/pdf", FacesContext.getCurrentInstance());
                    download.baixar();
                    download.remover();
                }
            } catch (JRException erro) {
                GenericaMensagem.info("Sistema", "O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
                System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
            }
        } catch (JRException erro) {
            GenericaMensagem.info("Sistema", "O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
            System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
        }
    }

    // USAR - ADICIONAR AO JASPER NO XML
    /**
     *
      <parameter name="sindicato_nome" class="java.lang.String"/>
      <parameter name="sindicato_documento" class="java.lang.String"/>
      <parameter name="sindicato_site" class="java.lang.String"/>
      <parameter name="sindicato_logradouro" class="java.lang.String"/>
      <parameter name="sindicato_endereco" class="java.lang.String"/>
      <parameter name="sindicato_numero" class="java.lang.String"/>
      <parameter name="sindicato_complemento" class="java.lang.String"/>
      <parameter name="sindicato_bairro" class="java.lang.String"/>
      <parameter name="sindicato_cidade" class="java.lang.String"/>
      <parameter name="sindicato_uf" class="java.lang.String"/>
      <parameter name="sindicato_cep" class="java.lang.String"/>
      <parameter name="sindicato_logo" class="java.lang.String"/>
      <parameter name="template_dir" class="java.lang.String"/>
     */
    
    /**
	<pageHeader>
		<band height="66">
			<subreport>
				<reportElement stretchType="RelativeToBandHeight" x="0" y="0" width="200" height="66" isRemoveLineWhenBlank="true" uuid="f01fa284-50bd-4581-997b-00977d4362c4"/>
				<subreportParameter name="sindicato_nome">
					<subreportParameterExpression><![CDATA[$P{sindicato_nome}]]></subreportParameterExpression>
				</subreportParameter>
				<subreportParameter name="sindicato_documento">
					<subreportParameterExpression><![CDATA[$P{sindicato_documento}]]></subreportParameterExpression>
				</subreportParameter>
				<subreportParameter name="sindicato_site">
					<subreportParameterExpression><![CDATA[$P{sindicato_site}]]></subreportParameterExpression>
				</subreportParameter>
				<subreportParameter name="sindicato_logradouro">
					<subreportParameterExpression><![CDATA[$P{sindicato_logradouro}]]></subreportParameterExpression>
				</subreportParameter>
				<subreportParameter name="sindicato_endereco">
					<subreportParameterExpression><![CDATA[$P{sindicato_endereco}]]></subreportParameterExpression>
				</subreportParameter>
				<subreportParameter name="sindicato_numero">
					<subreportParameterExpression><![CDATA[$P{sindicato_numero}]]></subreportParameterExpression>
				</subreportParameter>
				<subreportParameter name="sindicato_complemento">
					<subreportParameterExpression><![CDATA[$P{sindicato_complemento}]]></subreportParameterExpression>
				</subreportParameter>
				<subreportParameter name="sindicato_bairro">
					<subreportParameterExpression><![CDATA[$P{sindicato_bairro}]]></subreportParameterExpression>
				</subreportParameter>
				<subreportParameter name="sindicato_cidade">
					<subreportParameterExpression><![CDATA[$P{sindicato_cidade}]]></subreportParameterExpression>
				</subreportParameter>
				<subreportParameter name="sindicato_uf">
					<subreportParameterExpression><![CDATA[$P{sindicato_uf}]]></subreportParameterExpression>
				</subreportParameter>
				<subreportParameter name="sindicato_cep">
					<subreportParameterExpression><![CDATA[$P{sindicato_cep}]]></subreportParameterExpression>
				</subreportParameter>
				<subreportParameter name="sindicato_logo">
					<subreportParameterExpression><![CDATA[$P{sindicato_logo}]]></subreportParameterExpression>
				</subreportParameter>
				<connectionExpression><![CDATA[$P{REPORT_CONNECTION}]]></connectionExpression>
				<subreportExpression><![CDATA[$P{template_dir}]]></subreportExpression>
			</subreport>
		</band>
	</pageHeader>
     */
}
