package br.com.rtools.utilitarios;

import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.principal.DBExternal;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

@ManagedBean(name = "jasperBean")
public class Jasper {

    /**
     * Diretório do arquivo
     */
    public static String PATH = "downloads/relatorios";
    /**
     * Nome extra do arquivo
     */
    public static String PART_NAME = "relatorio";
    /**
     * Baixar arquivo (Default true);
     */
    public static Boolean IS_DOWNLOAD = true;
    /**
     * Remover arquivo após gerar (Default true);
     */
    public static Boolean IS_REMOVE_FILE = true;
    /**
     * Uso interno
     */
    public static byte[] BYTES = null;
    /**
     * Se o arquivo vai ter configuração com cabeçalho (SUBREPORT)
     */
    public static Boolean IS_HEADER = false;
    /**
     * Impressão por folha (configurar grupo)
     */
    public static Boolean IS_BY_LEAF = false;
    /**
     * Nome do grupo
     */
    public static String GROUP_NAME = "";
    /**
     * Se o arquivo é comprimido
     */
    public static Boolean COMPRESS_FILE = false;
    /**
     * Limite da compressão do arquivo
     */
    public static Integer COMPRESS_LIMIT = 0;
    /**
     * Define a extensão do arquivo compactado
     */
    public static String COMPRESS_EXTENSION = "zip";
    /**
     * Uso interno
     */
    private static final int MEGABYTE = (1024 * 1024);
    /**
     * Retorna o nome do arquivo gerado
     */
    public static String FILE_NAME_GENERATED = "";
    /**
     * Retorna o nome do arquivo gerado
     */
    public static List LIST_FILE_GENERATED = new ArrayList();
    /**
     * set: retrato or paisagem
     */
    public static String TYPE = "";
    /**
     * Nome do arquivo subreport
     */
    public static String SUBREPORT_NAME = "";
    /**
     * Impressão por folha (configurar grupo)
     */
    public static Boolean IS_REPORT_CONNECTION = false;
    /**
     * Não permite finalizar a compressão, para se obter a lista de arquivos
     * gerados
     */
    public static Boolean NO_COMPACT = false;

    public static void printReports(String jasperName, String fileName, Collection c) {
        printReports(jasperName, fileName, c, null);
    }

    public static void printReports(String jasperName, String fileName, List c, Map parameters) {
        printReports(jasperName, fileName, (Collection) c, parameters);
    }

    public static void printReports(String jasperName, String fileName, Collection c, Map parameters) {
        printReports(jasperName, fileName, c, parameters, new ArrayList());
    }

    /**
     * Envia uma lista com varios arquivos JASPER
     *
     * @param fileName
     * @param jasperListExport
     */
    public static void printReports(String fileName, List jasperListExport) {
        printReports("", fileName, new ArrayList(), null, jasperListExport);
    }

    public static void printReports(String jasperName, String fileName, Collection c, Map parameters, List jasperListExport) {
        Jasper.LIST_FILE_GENERATED = new ArrayList();
        Dao dao = new Dao();
        Juridica juridica = (Juridica) dao.find(new Juridica(), 1);
        byte[] bytesComparer = null;
        byte[] b = null;
        if (jasperListExport.isEmpty()) {
            if (fileName.isEmpty() || jasperName.isEmpty() || c.isEmpty()) {
                GenericaMensagem.info("Sistema", "Erro ao criar relatório!");
                return;
            }
            jasperName = jasperName.trim();
        } else {
            if (fileName.isEmpty()) {
                GenericaMensagem.info("Sistema", "Erro ao criar relatório!");
                return;
            }
        }
        fileName = fileName.trim();
        fileName = fileName.replace(" ", "_");
        fileName = fileName.replace("/", "");
        fileName = fileName.toLowerCase();
        fileName = AnaliseString.removerAcentos(fileName);
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
        if (parameters == null) {
            parameters = new HashMap();
        }
        if (IS_HEADER) {
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
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        try {
            try {
                byte[] bytes;
                JasperReport subJasper;
                if (!SUBREPORT_NAME.isEmpty()) {
                    if (new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Relatorios/ " + SUBREPORT_NAME)).exists()) {
                        subJasper = (JasperReport) JRLoader.loadObject(new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Relatorios/ " + SUBREPORT_NAME)));
                    } else {
                        subJasper = (JasperReport) JRLoader.loadObject(new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/" + SUBREPORT_NAME)));
                    }
                }
                if (IS_REPORT_CONNECTION) {
                    DBExternal con = new DBExternal();
                    con.setDatabase(GenericaSessao.getString("sessaoCliente"));
                    if (new File(subreport).exists()) {
                        parameters.put("REPORT_CONNECTION", con.getConnection());
                    }
                }
                JasperReport jasper = null;
                if (jasperListExport.isEmpty()) {
                    if (new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Relatorios/" + jasperName)).exists()) {
                        jasper = (JasperReport) JRLoader.loadObject(new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Relatorios/" + jasperName)));
                    } else if (new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "" + jasperName)).exists()) {
                        jasper = (JasperReport) JRLoader.loadObject(new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "" + jasperName)));
                    } else {
                        jasper = (JasperReport) JRLoader.loadObject(new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath(jasperName)));
                    }
                    if (!GROUP_NAME.isEmpty()) {
                        JRGroup[] jRGroups = jasper.getGroups();
                        for (int i = 0; i < jasper.getGroups().length; i++) {
                            if (jRGroups[i].getName().equals(GROUP_NAME)) {
                                if (IS_BY_LEAF) {
                                    ((JRGroup) jasper.getGroups()[i]).setStartNewPage(true);
                                } else {
                                    ((JRGroup) jasper.getGroups()[i]).setStartNewPage(false);
                                }
                                break;
                            }
                        }
                    }
                }
                Integer size = 0;
                if (COMPRESS_FILE && COMPRESS_LIMIT > 0) {
                    if (c.size() > COMPRESS_LIMIT) {
                        size = c.size() / COMPRESS_LIMIT;
                        size = (int) Math.ceil((double) size);
                        size += 1;
                    } else {
                        COMPRESS_FILE = false;
                    }
                }
                Collection[] collections = new Collection[size];
                List listTemp = new ArrayList();
                String downloadName = "";
                String realPath = "";
                String mimeType = "application/pdf";
                JasperPrint print;
                JRBeanCollectionDataSource dtSource;
                List listFilesZip = new ArrayList();
                
                // DEU ERRO NO MOMENTO EM QUE FOI IMPRIMIR UM RELATÓRIO PELA WEB, ONDE NÃO SE TEM Usuario
                // RETORNO NULL
                // DATA DE ALTERAÇÃO 07/05/2015
                // CHAMADO 736 - Priscila
                // Integer idUsuario = ((Usuario) GenericaSessao.getObject("sessaoUsuario")).getId();
                
                Usuario u = (Usuario) GenericaSessao.getObject("sessaoUsuario");
                Pessoa p = (u != null) ? u.getPessoa() : (Pessoa) GenericaSessao.getObject("sessaoUsuarioAcessoWeb");
                Integer idPessoa =  p.getId();
                
                // -----------------------------------------------------------------------------------------------------
                // -----------------------------------------------------------------------------------------------------
                
                if (Jasper.PATH.isEmpty()) {
                    realPath = "/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/" + fileName + "/";
                } else {
                    realPath = "/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/" + PATH + "/" + fileName + "/";
                }
                String dirPath = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(realPath);
                if (!Jasper.PART_NAME.isEmpty()) {
                    Jasper.PART_NAME = Jasper.PART_NAME.trim();
                    Jasper.PART_NAME = Jasper.PART_NAME.toLowerCase();
                    Jasper.PART_NAME = Jasper.PART_NAME.replace(" ", "_");
                    Jasper.PART_NAME = Jasper.PART_NAME.replace("/", "");
                    Jasper.PART_NAME = AnaliseString.removerAcentos(Jasper.PART_NAME);
                    Jasper.PART_NAME = "_" + Jasper.PART_NAME;
                }
                UUID uuid = UUID.randomUUID();
                if (COMPRESS_FILE && COMPRESS_LIMIT > 0) {
                    if (!jasperListExport.isEmpty()) {
                        GenericaMensagem.info("Sistema", "Não é possível comprimir uma lista de Jasper!");
                        destroy();
                        return;
                    }
                    List listCollection = (List) c;
                    c = null;
                    int pos = 0;
                    int y = 0;
                    int x = 0;
                    collections[0] = new ArrayList();
                    for (int i = 0; i < listCollection.size(); i++) {
                        if (pos + 1 <= COMPRESS_LIMIT) {
                            listTemp.add(listCollection.get(x));
                            // listCollection.remove(x);
                            pos++;
                            x++;
                        } else {
                            collections[y] = (Collection) listTemp;
                            if (y + 1 > size) {
                                break;
                            }
                            y++;
                            collections[y] = new ArrayList();
                            listTemp = new ArrayList();
                            pos = 0;
                            i--;
                        }
                    }
                    if (!listTemp.isEmpty()) {
                        collections[y] = (Collection) listTemp;
                    }
                    listCollection.clear();
                    if (Jasper.PATH.isEmpty()) {
                        realPath = "/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/" + fileName + "/";
                    } else {
                        realPath = "/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/" + PATH + "/" + fileName + "/";
                    }
                    jasper.setProperty(fileName, PATH);
                    // JRVariable[] jrvs = jasper.getVariables();
                    for (int i = 0; i < collections.length; i++) {
                        dtSource = new JRBeanCollectionDataSource(collections[i]);
                        print = JasperFillManager.fillReport(jasper, parameters, dtSource);
                        if (bytesComparer == BYTES) {
                            b = JasperExportManager.exportReportToPdf(print);
                        } else {
                            b = BYTES;
                        }
                        if (b.length > MEGABYTE) {
                            bytes = new byte[MEGABYTE * 500];
                        }
                        //downloadName = fileName + PART_NAME + "_" + idUsuario + "_page_" + (i + 1) + "_" + UUID.randomUUID() + ".pdf";
                        downloadName = fileName + PART_NAME + "_" + idPessoa + "_page_" + (i + 1) + "_" + UUID.randomUUID() + ".pdf";
                        try {
                            File file = new File(dirPath + "/" + downloadName);
                            listFilesZip.add(dirPath + "/" + downloadName);
                            try (FileOutputStream out = new FileOutputStream(file)) {
                                out.write(b);
                                out.flush();
                            }
                        } catch (IOException e) {
                            System.out.println(e);
                            return;
                        } catch (OutOfMemoryError e) {
                            MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
                            long maxMemory = heapUsage.getMax() / MEGABYTE;
                            long usedMemory = heapUsage.getUsed() / MEGABYTE;
                            System.out.println("Memória > Tamanho do arquivo não suporta o formato PDF, tente novamente baixando o mesmo compactado. Memória usada: " + usedMemory + "M/" + maxMemory + "M");
                            GenericaMensagem.info("Servidor > Memória", "Tamanho do arquivo não suporta o formato PDF, tente novamente baixando o mesmo compactado. Memória usada: " + usedMemory + "M/" + maxMemory + "M");
                        }
                        // b = null;
                        LIST_FILE_GENERATED.add(dirPath + "/" + downloadName);
                    }
                    //downloadName = fileName + PART_NAME + "_" + idUsuario + "_" + uuid + "." + COMPRESS_EXTENSION;
                    downloadName = fileName + PART_NAME + "_" + idPessoa + "_" + uuid + "." + COMPRESS_EXTENSION;
                    if (!NO_COMPACT) {
                        //Compact.OUT_FILE = fileName + PART_NAME + "_" + idUsuario + "_" + uuid + "." + COMPRESS_EXTENSION;
                        Compact.OUT_FILE = fileName + PART_NAME + "_" + idPessoa + "_" + uuid + "." + COMPRESS_EXTENSION;
                        Compact.setListFiles(listFilesZip);
                        Compact.PATH_OUT_FILE = realPath;
                        try {
                            Compact.toZip();
                        } catch (IOException e) {

                        }
                    }
                } else {
                    // Se o método por ventura passar apagar arquivos gerados, 
                    // acrescentar a linha abaixo, esta contém o id do usuário
                    // downloadName = fileName + PART_NAME + "_" + DataHoje.horaMinuto().replace(":", "") + "_" + idUsuario + ".pdf";                    
                    downloadName = fileName + PART_NAME + "_" + uuid + ".pdf";
                    try {
                        File file = new File(dirPath + "/" + downloadName);
                        if (jasperListExport.isEmpty()) {
                            jasper.setProperty(fileName, PATH);
                            dtSource = new JRBeanCollectionDataSource(c);
                            print = JasperFillManager.fillReport(jasper, parameters, dtSource);
                            if (bytesComparer == BYTES) {
                                b = JasperExportManager.exportReportToPdf(print);
                            } else {
                                b = BYTES;
                            }
                            if (b.length > MEGABYTE) {
                                bytes = new byte[MEGABYTE * 500];
                            }
                        } else {
                            //  VER COM O CLAUDEMIR
                            //                        List listExport = new ArrayList();
                            //                        listExport.add(new File(dirPath + "/" + downloadName));
                            //                        JRPdfExporter exporter = new JRPdfExporter();
                            //                        ByteArrayOutputStream retorno = new ByteArrayOutputStream();
                            //                        exporter.setExporterInput(SimpleExporterInput.getInstance(listExport));
                            //                        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(dirPath + "/" + downloadName));
                            //                        SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
                            //                        configuration.setCreatingBatchModeBookmarks(true);
                            //                        exporter.setConfiguration(configuration);
                            //                        exporter.exportReport();
                            JRPdfExporter exporter = new JRPdfExporter();
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperListExport);
                            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, stream);
                            exporter.setParameter(JRPdfExporterParameter.IS_CREATING_BATCH_MODE_BOOKMARKS, Boolean.TRUE);
                            exporter.exportReport();
                            b = stream.toByteArray();
                            if (b.length > MEGABYTE) {
                                bytes = new byte[MEGABYTE * 500];
                            }
                        }
                        try (FileOutputStream out = new FileOutputStream(file)) {
                            out.write(b);
                            out.flush();
                        }
                    } catch (IOException | JRException e) {
                        System.out.println(e);
                        return;
                    } catch (OutOfMemoryError e) {
                        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
                        long maxMemory = heapUsage.getMax() / MEGABYTE;
                        long usedMemory = heapUsage.getUsed() / MEGABYTE;
                        System.out.println("Memória > Tamanho do arquivo não suporta o formato PDF, tente novamente baixando o mesmo compactado. Memória usada: " + usedMemory + "M/" + maxMemory + "M");
                        GenericaMensagem.info("Servidor > Memória", "Tamanho do arquivo não suporta o formato PDF, tente novamente baixando o mesmo compactado. Memória usada: " + usedMemory + "M/" + maxMemory + "M");
                    }
                    if (COMPRESS_FILE) {
                        if (COMPRESS_EXTENSION.equals("zip")) {
                            mimeType = "application/zip, application/octet-stream";
                        } else {
                            mimeType = "application/x-rar-compressed, application/octet-stream";
                        }
                        Compact.OUT_FILE = fileName + PART_NAME + "_" + uuid + "." + COMPRESS_EXTENSION;
                        Compact.PATH_OUT_FILE = realPath;
                        try {
                            listFilesZip.add(dirPath + "/" + downloadName);
                            Compact.toZip(fileName + PART_NAME + "_" + uuid + "." + COMPRESS_EXTENSION, dirPath + "/" + downloadName);
                            downloadName = fileName + PART_NAME + "_" + uuid + "." + COMPRESS_EXTENSION;
                        } catch (IOException e) {

                        }
                    }
                }
                if (IS_DOWNLOAD) {
                    Download download = new Download(downloadName, dirPath, mimeType, FacesContext.getCurrentInstance());
                    download.baixar();
                    FILE_NAME_GENERATED = dirPath + "/" + downloadName;
                    if (IS_REMOVE_FILE) {
                        download.remover();
                    }
                    if (!listFilesZip.isEmpty()) {
                        for (int i = 0; i < listFilesZip.size(); i++) {
                            File f = new File(listFilesZip.get(i).toString());
                            f.delete();
                        }
                    }
                }
            } catch (JRException erro) {
                GenericaMensagem.info("Sistema", "O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
                System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
            }
        } catch (OutOfMemoryError e) {
            MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
            long maxMemory = heapUsage.getMax() / MEGABYTE;
            long usedMemory = heapUsage.getUsed() / MEGABYTE;
            System.out.println("Memória > Tamanho do arquivo não suporta o formato PDF, tente novamente baixando o mesmo compactado. Memória usada: " + usedMemory + "M/" + maxMemory + "M");
            GenericaMensagem.info("Servidor > Memória", "Tamanho do arquivo não suporta o formato PDF, tente novamente baixando o mesmo compactado. Memória usada: " + usedMemory + "M/" + maxMemory + "M");
        }
        destroy();
    }

    public static void destroy() {
        PATH = "downloads/relatorios";
        PART_NAME = "relatorio";
        IS_DOWNLOAD = true;
        IS_REMOVE_FILE = true;
        BYTES = null;
        IS_HEADER = false;
        // IMPRIME POR FOLHA
        IS_BY_LEAF = false;
        GROUP_NAME = "";
        COMPRESS_FILE = false;
        COMPRESS_LIMIT = 0;
        COMPRESS_EXTENSION = "zip";
        SUBREPORT_NAME = "";
        IS_REPORT_CONNECTION = false;
        NO_COMPACT = false;
    }

    public static String getFILE_NAME_GENERATED() {
        return FILE_NAME_GENERATED;
    }

    public static void setFILE_NAME_GENERATED(String aFILE_NAME_GENERATED) {
        FILE_NAME_GENERATED = aFILE_NAME_GENERATED;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String aTYPE) {
        TYPE = aTYPE;
    }

    public Boolean getIS_BY_LEAF() {
        return IS_BY_LEAF;
    }

    public void setIS_BY_LEAF(Boolean aIS_BY_LEAF) {
        IS_BY_LEAF = aIS_BY_LEAF;
    }

    public Boolean getIS_HEADER() {
        return IS_HEADER;
    }

    public void setIS_HEADER(Boolean aIS_HEADER) {
        IS_HEADER = aIS_HEADER;
    }

    public String getCOMPRESS_EXTENSION() {
        return COMPRESS_EXTENSION;
    }

    public void setCOMPRESS_EXTENSION(String aCOMPRESS_EXTENSION) {
        COMPRESS_EXTENSION = aCOMPRESS_EXTENSION;
    }

    public Boolean getCOMPRESS_FILE() {
        return COMPRESS_FILE;
    }

    public void setCOMPRESS_FILE(Boolean aCOMPRESS_FILE) {
        COMPRESS_FILE = aCOMPRESS_FILE;
    }

    // USAR - ADICIONAR AO JASPER NO XML
    /**
     *
     * <parameter name="sindicato_nome" class="java.lang.String"/>
     * <parameter name="sindicato_documento" class="java.lang.String"/>
     * <parameter name="sindicato_site" class="java.lang.String"/>
     * <parameter name="sindicato_logradouro" class="java.lang.String"/>
     * <parameter name="sindicato_endereco" class="java.lang.String"/>
     * <parameter name="sindicato_numero" class="java.lang.String"/>
     * <parameter name="sindicato_complemento" class="java.lang.String"/>
     * <parameter name="sindicato_bairro" class="java.lang.String"/>
     * <parameter name="sindicato_cidade" class="java.lang.String"/>
     * <parameter name="sindicato_uf" class="java.lang.String"/>
     * <parameter name="sindicato_cep" class="java.lang.String"/>
     * <parameter name="sindicato_logo" class="java.lang.String"/>
     * <parameter name="template_dir" class="java.lang.String"/>
     */
    /**
     * <pageHeader>
     * <band height="66">
     * <subreport>
     * <reportElement stretchType="RelativeToBandHeight" x="0" y="0" width="200" height="66" isRemoveLineWhenBlank="true" uuid="f01fa284-50bd-4581-997b-00977d4362c4"/>
     * <subreportParameter name="sindicato_nome">
     * <subreportParameterExpression><![CDATA[$P{sindicato_nome}]]></subreportParameterExpression>
     * </subreportParameter>
     * <subreportParameter name="sindicato_documento">
     * <subreportParameterExpression><![CDATA[$P{sindicato_documento}]]></subreportParameterExpression>
     * </subreportParameter>
     * <subreportParameter name="sindicato_site">
     * <subreportParameterExpression><![CDATA[$P{sindicato_site}]]></subreportParameterExpression>
     * </subreportParameter>
     * <subreportParameter name="sindicato_logradouro">
     * <subreportParameterExpression><![CDATA[$P{sindicato_logradouro}]]></subreportParameterExpression>
     * </subreportParameter>
     * <subreportParameter name="sindicato_endereco">
     * <subreportParameterExpression><![CDATA[$P{sindicato_endereco}]]></subreportParameterExpression>
     * </subreportParameter>
     * <subreportParameter name="sindicato_numero">
     * <subreportParameterExpression><![CDATA[$P{sindicato_numero}]]></subreportParameterExpression>
     * </subreportParameter>
     * <subreportParameter name="sindicato_complemento">
     * <subreportParameterExpression><![CDATA[$P{sindicato_complemento}]]></subreportParameterExpression>
     * </subreportParameter>
     * <subreportParameter name="sindicato_bairro">
     * <subreportParameterExpression><![CDATA[$P{sindicato_bairro}]]></subreportParameterExpression>
     * </subreportParameter>
     * <subreportParameter name="sindicato_cidade">
     * <subreportParameterExpression><![CDATA[$P{sindicato_cidade}]]></subreportParameterExpression>
     * </subreportParameter>
     * <subreportParameter name="sindicato_uf">
     * <subreportParameterExpression><![CDATA[$P{sindicato_uf}]]></subreportParameterExpression>
     * </subreportParameter>
     * <subreportParameter name="sindicato_cep">
     * <subreportParameterExpression><![CDATA[$P{sindicato_cep}]]></subreportParameterExpression>
     * </subreportParameter>
     * <subreportParameter name="sindicato_logo">
     * <subreportParameterExpression><![CDATA[$P{sindicato_logo}]]></subreportParameterExpression>
     * </subreportParameter>
     * <connectionExpression><![CDATA[$P{REPORT_CONNECTION}]]></connectionExpression>
     * <subreportExpression><![CDATA[$P{template_dir}]]></subreportExpression>
     * </subreport>
     * </band>
     * </pageHeader>
     */
}
