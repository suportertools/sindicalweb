package br.com.rtools.utilitarios;

import br.com.rtools.associativo.MatriculaSocios;
import br.com.rtools.associativo.ModeloCarteirinha;
import br.com.rtools.associativo.SocioCarteirinha;
import br.com.rtools.associativo.Socios;
import br.com.rtools.associativo.db.SociosDB;
import br.com.rtools.associativo.db.SociosDBToplink;
import br.com.rtools.impressao.CartaoSocial;
import br.com.rtools.impressao.FichaSocial;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.PessoaEmpresa;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.db.*;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import com.google.common.collect.HashBiMap;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

public class ImpressaoParaSocios {

    public static boolean imprimirCarteirinha(List listaCartao) {
        List<CartaoSocial> listax = new ArrayList();
        FacesContext context = FacesContext.getCurrentInstance();

        File files = new File(((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/"));
        File listFile[] = files.listFiles();
        List<ModeloCarteirinha> listaModelo = new SalvarAcumuladoDBToplink().listaObjeto("ModeloCarteirinha");
        Map<Integer, List> hash = new HashMap();

        for (int i = 0; i < listaCartao.size(); i++) {
            String imagem = "semFoto.jpg";
            for (int j = 0; j < listFile.length; j++) {
                String[] arrayString = listFile[j].getName().split("\\.");
                String descricaoFile = arrayString[0];
                String idPessoa = ((List) (listaCartao.get(i))).get(0).toString();
                if (descricaoFile.equals(idPessoa)) {
                    imagem = listFile[j].getName();
                    break;
                }
            }

            String matr = "";
            if (((List) (listaCartao.get(i))).get(10) != null) {
                matr = "000000".substring(0, 6 - ((List) (listaCartao.get(i))).get(10).toString().length()) + ((List) (listaCartao.get(i))).get(10).toString();
            }

            String via = "00";
            if (((List) (listaCartao.get(i))).get(11) != null) {
                via = ((List) (listaCartao.get(i))).get(11).toString();
            }
            if (via.length() == 1) {
                via = "0" + via;
            }

            Registro reg = (Registro) new SalvarAcumuladoDBToplink().pesquisaCodigo(1, "Registro");
            String bc = ((List) (listaCartao.get(i))).get(18).toString() + via; //             String bc = ((List) (listaCartao.get(i))).get(0).toString() + via; // id_pessoa
            String barras = "";

            if (reg.isValidadeBarras() && ((List) (listaCartao.get(i))).get(6) != null) {
                Date vencto = DataHoje.converte(((List) (listaCartao.get(i))).get(6).toString());
                if (vencto != null) {
                    Date dataModel = DataHoje.converte("07/10/1997");
                    long dias = vencto.getTime() - dataModel.getTime();
                    long total = dias / 86400000;

                    bc += "0000".substring(0, 4 - Long.toString(total).length()) + Long.toString(total);
                } else {
                    bc += "0000";
                }
                barras = "00000000000000".substring(0, 14 - bc.length()) + bc;
            } else {
                barras = "0000000000".substring(0, 10 - bc.length()) + bc;
            }
            //String barras = "0000000000".substring(0, 10 - bc.length()) + bc;

            SocioCarteirinha carteirinha = new SocioCarteirinha();
            carteirinha = (SocioCarteirinha) new SalvarAcumuladoDBToplink().pesquisaCodigo((Integer) ((List) listaCartao.get(i)).get(19), "SocioCarteirinha");

            ModeloCarteirinha[] mod_obj = new ModeloCarteirinha[listaModelo.size()];
            File file_img = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/cartao.jpg"));

            String caminho_img = "";

            if (file_img.exists()) {
                caminho_img = file_img.getPath();
            }

            String endereco = getConverteNullString(((List) (listaCartao.get(i))).get(21)) + " " +
                              getConverteNullString(((List) (listaCartao.get(i))).get(22)) + ", "+
                              getConverteNullString(((List) (listaCartao.get(i))).get(23)) + " " +
                              getConverteNullString(((List) (listaCartao.get(i))).get(24)) + " - "+
                              getConverteNullString(((List) (listaCartao.get(i))).get(25));
            
            String cidade_uf = getConverteNullString(((List) (listaCartao.get(i))).get(26))+" - "+getConverteNullString(((List) (listaCartao.get(i))).get(27));
            listax.add(
                    new CartaoSocial(
                            matr, // CODIGO
                            barras, // BARRAS 
                            getConverteNullString(((List) (listaCartao.get(i))).get(1)), // NOME
                            getConverteNullString(((List) (listaCartao.get(i))).get(3)), // EMPRESA
                            getConverteNullString(((List) (listaCartao.get(i))).get(2)), // CNPJ
                            getConverteNullString(((List) (listaCartao.get(i))).get(8)), // DATA ADMISSAO
                            getConverteNullString(((List) (listaCartao.get(i))).get(6)), // DATA VALIDADE
                            getConverteNullString(((List) (listaCartao.get(i))).get(5)), // CIDADE
                            getConverteNullString(((List) (listaCartao.get(i))).get(7)), // UF
                            ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"), // LOGO
                            ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/" + imagem), // CAMINHO FOTO
                            getConverteNullString(((List) (listaCartao.get(i))).get(13)), // FILIAÇÃO
                            getConverteNullString(((List) (listaCartao.get(i))).get(14)), // PROFISSÃO
                            getConverteNullString(((List) (listaCartao.get(i))).get(15)), // CPF
                            getConverteNullString(((List) (listaCartao.get(i))).get(16)), // RG
                            Integer.valueOf(((List) (listaCartao.get(i))).get(0).toString()), // ID_PESSOA
                            endereco, // ENDERECO
                            cidade_uf, // CIDADE
                            getConverteNullString(((List) (listaCartao.get(i))).get(29)), // NACIONALIDADE
                            getConverteNullString(((List) (listaCartao.get(i))).get(30)), // NASCIMENTO
                            getConverteNullString(((List) (listaCartao.get(i))).get(31)), // ESTADOCIVIL
                            getConverteNullString(((List) (listaCartao.get(i))).get(32)), // CARTEIRA
                            getConverteNullString(((List) (listaCartao.get(i))).get(33)), // SERIE
                            caminho_img // IMAGEMFUNDO
                    )
            );

            for (ModeloCarteirinha modelo : listaModelo) {
                if (carteirinha.getModeloCarteirinha().getId() == modelo.getId()) {
                    hash.put(modelo.getId(), listax);
                }
            }
        }

        if (listax.isEmpty()) {
            return false;
        }

        try {
            String patch = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos");
            File fileA = new File(patch + "/downloads");
            if (!fileA.exists()) {
                fileA.mkdir();
            }
            File fileB = new File(patch + "/downloads/carteirinhas");
            if (!fileB.exists()) {
                fileB.mkdir();
            }

            ModeloCarteirinha modelo = new ModeloCarteirinha();
            List ljasper = new ArrayList();
            JasperReport jasper;

            for (Entry<Integer, List> entry : hash.entrySet()) {
                modelo = (ModeloCarteirinha) new SalvarAcumuladoDBToplink().pesquisaCodigo(entry.getKey(), "ModeloCarteirinha");
                String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Relatorios/" + modelo.getJasper());

                if (caminho == null) {
                    GenericaMensagem.error("Erro jasper: " + modelo.getJasper(), "Modelo não encontrado na pasta Relatório!");
                    continue;
                }

                File file = new File(
                        ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Relatorios/" + modelo.getJasper())
                );
                //* ADD LISTA DE JASPERS *//
                jasper = (JasperReport) JRLoader.loadObject(file);
                JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(entry.getValue());

                ljasper.add(JasperFillManager.fillReport(jasper, null, dtSource));
            }

            JRPdfExporter exporter = new JRPdfExporter();
            ByteArrayOutputStream retorno = new ByteArrayOutputStream();

            exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, ljasper);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, retorno);
            exporter.setParameter(JRPdfExporterParameter.IS_CREATING_BATCH_MODE_BOOKMARKS, Boolean.TRUE);
            exporter.exportReport();

            String nomeDownload = "cartao_social_" + DataHoje.horaMinuto().replace(":", "") + ".pdf";
            SalvaArquivos sa = new SalvaArquivos(retorno.toByteArray(), nomeDownload, false);
            String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/carteirinhas");
            sa.salvaNaPasta(pathPasta);
            Download download = new Download(nomeDownload, pathPasta, "application/pdf", FacesContext.getCurrentInstance());
            download.baixar();
            download.remover();

            //JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(listax);
            //JasperReport jasper = (JasperReport) JRLoader.loadObject(file);
//            JasperPrint print = JasperFillManager.fillReport(jasper, null, dtSource);
//            byte[] arquivo = JasperExportManager.exportReportToPdf(print);
//            String nomeDownload = "cartao_social_" + DataHoje.horaMinuto().replace(":", "") + ".pdf";
//            SalvaArquivos sa = new SalvaArquivos(arquivo, nomeDownload, false);
//
//            String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/carteirinhas");
//            sa.salvaNaPasta(pathPasta);
//
//            Download download = new Download(nomeDownload, pathPasta, "application/pdf", FacesContext.getCurrentInstance());
//            download.baixar();
//            download.remover();
        } catch (JRException e) {
            return false;
        }
        return true;
    }

    public static void comDependente(String pathPasta, String nomeDownload, String path, String pathVerso, Socios socios, PessoaEmpresa pessoaEmpresa, MatriculaSocios matriculaSocios, boolean imprimirVerso, String fotoSocio) {
        SalvarAcumuladoDB acumuladoDB = new SalvarAcumuladoDBToplink();
        Registro registro = (Registro) acumuladoDB.pesquisaCodigo(1, "Registro");
        Fisica fisica = new Fisica();
        Juridica sindicato = new Juridica();
        FisicaDB db = new FisicaDBToplink();
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        PessoaEndereco pesEndereco, pesDestinatario, pesEndEmpresa, pesEndSindicato = new PessoaEndereco();
        PessoaEnderecoDB dbEnd = new PessoaEnderecoDBToplink();
        //PessoaEmpresa pesEmpresa = new PessoaEmpresa();
        PessoaEmpresaDB dbEmp = new PessoaEmpresaDBToplink();
        SociosDB dbSoc = new SociosDBToplink();
        FacesContext faces = FacesContext.getCurrentInstance();
        try {
            //HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
            Collection listaSocios = new ArrayList<FichaSocial>();
            JasperReport jasper = (JasperReport) JRLoader.loadObject(
                    new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath(path))
            );

            fisica = db.pesquisaFisicaPorPessoa(socios.getServicoPessoa().getPessoa().getId());
            pesEndereco = dbEnd.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 1);
            sindicato = (Juridica) salvarAcumuladoDB.pesquisaCodigo(1, "Juridica");

            if (pessoaEmpresa.getId() != -1) {
                pesEndEmpresa = dbEnd.pesquisaEndPorPessoaTipo(pessoaEmpresa.getJuridica().getPessoa().getId(), 2);
            } else {
                pesEndEmpresa = dbEnd.pesquisaEndPorPessoaTipo(pessoaEmpresa.getJuridica().getPessoa().getId(), 2);
            }

            pesEndSindicato = dbEnd.pesquisaEndPorPessoaTipo(sindicato.getPessoa().getId(), 2);

            pesDestinatario = dbEnd.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 1);

            String dados[] = new String[32];

            try {
                dados[0] = pesEndereco.getEndereco().getLogradouro().getDescricao();
                dados[1] = pesEndereco.getEndereco().getDescricaoEndereco().getDescricao();
                dados[2] = pesEndereco.getNumero();
                dados[3] = pesEndereco.getComplemento();
                dados[4] = pesEndereco.getEndereco().getBairro().getDescricao();
                dados[5] = pesEndereco.getEndereco().getCidade().getCidade();
                dados[6] = pesEndereco.getEndereco().getCidade().getUf();
                dados[7] = AnaliseString.mascaraCep(pesEndereco.getEndereco().getCep());
            } catch (Exception e) {
                dados[0] = "";
                dados[1] = "";
                dados[2] = "";
                dados[3] = "";
                dados[4] = "";
                dados[5] = "";
                dados[6] = "";
                dados[7] = "";
            }

            try {
                dados[8] = pesDestinatario.getEndereco().getLogradouro().getDescricao();
                dados[9] = pesDestinatario.getEndereco().getDescricaoEndereco().getDescricao();
                dados[10] = pesDestinatario.getNumero();
                dados[11] = pesDestinatario.getComplemento();
                dados[12] = pesDestinatario.getEndereco().getBairro().getDescricao();
                dados[13] = pesDestinatario.getEndereco().getCidade().getCidade();
                dados[14] = pesDestinatario.getEndereco().getCidade().getUf();
                dados[15] = AnaliseString.mascaraCep(pesDestinatario.getEndereco().getCep());
                dados[26] = pesDestinatario.getPessoa().getDocumento();
                dados[27] = pesDestinatario.getPessoa().getNome();
            } catch (Exception e) {
                dados[8] = "";
                dados[9] = "";
                dados[10] = "";
                dados[11] = "";
                dados[12] = "";
                dados[13] = "";
                dados[14] = "";
                dados[15] = "";
                dados[26] = "";
                dados[27] = "";
            }

            try {
                dados[16] = pessoaEmpresa.getJuridica().getPessoa().getNome();
                dados[17] = pessoaEmpresa.getJuridica().getPessoa().getTelefone1();
                dados[18] = pessoaEmpresa.getFuncao().getProfissao();
                dados[19] = pesEndEmpresa.getEndereco().getDescricaoEndereco().getDescricao();
                dados[20] = pesEndEmpresa.getNumero();
                dados[21] = pesEndEmpresa.getComplemento();
                dados[22] = pesEndEmpresa.getEndereco().getBairro().getDescricao();
                dados[23] = pesEndEmpresa.getEndereco().getCidade().getCidade();
                dados[24] = pesEndEmpresa.getEndereco().getCidade().getUf();
                dados[25] = AnaliseString.mascaraCep(pesEndEmpresa.getEndereco().getCep());
                dados[28] = pessoaEmpresa.getAdmissao();
                dados[29] = pessoaEmpresa.getJuridica().getPessoa().getDocumento();
                dados[30] = pessoaEmpresa.getJuridica().getFantasia();
                dados[31] = pesEndEmpresa.getEndereco().getLogradouro().getDescricao();
            } catch (Exception e) {
                dados[16] = "";
                dados[17] = "";
                dados[18] = "";
                dados[19] = "";
                dados[20] = "";
                dados[21] = "";
                dados[22] = "";
                dados[23] = "";
                dados[24] = "";
                dados[25] = "";
                dados[28] = "";
                dados[29] = "";
                dados[30] = "";
                dados[31] = "";
            }
            String assinatura = "";
            File f = new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/assinatura.jpg"));
            if (f.exists()) {
                assinatura = ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/assinatura.jpg");
            }
            try {
                listaSocios.add(new FichaSocial(0,
                        matriculaSocios.getTitular().getId(),
                        matriculaSocios.getNrMatricula(),
                        matriculaSocios.getDtEmissao(),
                        null,
                        matriculaSocios.getCategoria().getGrupoCategoria().getGrupoCategoria(),
                        matriculaSocios.getCategoria().getCategoria(),
                        fisica.getPessoa().getNome(),
                        fisica.getSexo(),
                        fisica.getDtNascimento(),
                        fisica.getNaturalidade(),
                        fisica.getNacionalidade(),
                        fisica.getRg(),
                        fisica.getPessoa().getDocumento(),
                        fisica.getCarteira(),
                        fisica.getSerie(),
                        fisica.getEstadoCivil(),
                        fisica.getPai(),
                        fisica.getMae(),
                        fisica.getPessoa().getTelefone1(),
                        fisica.getPessoa().getTelefone3(),
                        fisica.getPessoa().getEmail1(),
                        dados[0],
                        dados[1],
                        dados[2],
                        dados[3],
                        dados[4],
                        dados[5],
                        dados[6],
                        dados[7],
                        imprimirVerso,
                        dados[26],
                        dados[27],
                        dados[8],
                        dados[9],
                        dados[10],
                        dados[11],
                        dados[12],
                        dados[13],
                        dados[14],
                        dados[15],
                        dados[16],
                        dados[17],
                        null, // fax
                        DataHoje.converte(dados[28]),
                        dados[18],
                        dados[19],
                        dados[20],
                        dados[21],
                        dados[22],
                        dados[23],
                        dados[24],
                        dados[25],
                        ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
                        registro.getFichaSocial(), // obs
                        socios.getParentesco().getParentesco(),
                        sindicato.getPessoa().getNome(),
                        pesEndSindicato.getEndereco().getDescricaoEndereco().getDescricao(),
                        pesEndSindicato.getNumero(),
                        pesEndSindicato.getComplemento(),
                        pesEndSindicato.getEndereco().getBairro().getDescricao(),
                        pesEndSindicato.getEndereco().getCidade().getCidade(),
                        pesEndSindicato.getEndereco().getCidade().getUf(),
                        AnaliseString.mascaraCep(pesEndSindicato.getEndereco().getCep()),
                        sindicato.getPessoa().getDocumento(),
                        "",
                        ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
                        fotoSocio,
                        sindicato.getPessoa().getEmail1(),
                        sindicato.getPessoa().getSite(),
                        sindicato.getPessoa().getTelefone1(),
                        ((ServletContext) faces.getExternalContext().getContext()).getRealPath(pathVerso),
                        dados[29],
                        fisica.getDtRecadastro(),
                        dados[30],
                        pesEndSindicato.getEndereco().getLogradouro().getDescricao(),
                        dados[31], assinatura));

                List<Socios> deps = dbSoc.pesquisaDependentesOrdenado(matriculaSocios.getId());
                for (int n = 0; n < deps.size(); n++) {
                    listaSocios.add(new FichaSocial(0,
                            deps.get(n).getServicoPessoa().getPessoa().getId(),
                            matriculaSocios.getNrMatricula(),
                            null,
                            null,
                            "",
                            matriculaSocios.getCategoria().getCategoria(),
                            deps.get(n).getServicoPessoa().getPessoa().getNome(),
                            db.pesquisaFisicaPorPessoa(deps.get(n).getServicoPessoa().getPessoa().getId()).getSexo(),
                            db.pesquisaFisicaPorPessoa(deps.get(n).getServicoPessoa().getPessoa().getId()).getDtNascimento(),
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            imprimirVerso,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            null,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
                            registro.getFichaSocial(), // obs
                            deps.get(n).getParentesco().getParentesco(),
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
                            ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Imagens/Fotos/semFoto.jpg"),
                            "",
                            "",
                            "",
                            ((ServletContext) faces.getExternalContext().getContext()).getRealPath(pathVerso),
                            "",
                            null,
                            "",
                            "",
                            "",
                            assinatura)
                    );
                }
                JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(listaSocios);
                JasperPrint print = JasperFillManager.fillReport(
                        jasper,
                        null,
                        dtSource);
                byte[] arquivo = JasperExportManager.exportReportToPdf(print);
//                     response.setContentType("application/pdf");
//                     response.setContentLength(arquivo.length);
//                     ServletOutputStream saida = response.getOutputStream();
//                     saida.write(arquivo, 0, arquivo.length);
//                     saida.flush();
//                     saida.close();

                SalvaArquivos sa = new SalvaArquivos(arquivo,
                        nomeDownload,
                        false);
                sa.salvaNaPasta(pathPasta);

                Download download = new Download(nomeDownload,
                        pathPasta,
                        "application/pdf",
                        FacesContext.getCurrentInstance());
                download.baixar();
                download.remover();
            } catch (JRException erro) {
                System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
            }
        } catch (JRException erro) {
            System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
        }
    }

    public static void semDependente(String pathPasta, String nomeDownload, String path, String pathVerso, Socios socios, PessoaEmpresa pessoaEmpresa, MatriculaSocios matriculaSocios, boolean imprimirVerso, List<Socios> listaDependentes) {
        Fisica fisica = new Fisica();
        Juridica sindicato = new Juridica();
        FisicaDB db = new FisicaDBToplink();
        JuridicaDB dbJur = new JuridicaDBToplink();
        PessoaEndereco pesEndereco, pesDestinatario, pesEndEmpresa, pesEndSindicato = new PessoaEndereco();
        PessoaEnderecoDB dbEnd = new PessoaEnderecoDBToplink();
        PessoaEmpresa pesEmpresa = new PessoaEmpresa();
        PessoaEmpresaDB dbEmp = new PessoaEmpresaDBToplink();
        String dados[] = new String[32];
        List<Socios> listaSocs = new ArrayList();
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        try {
            FacesContext faces = FacesContext.getCurrentInstance();
            //HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
            Collection listaSocios = new ArrayList<FichaSocial>();
            JasperReport jasper = (JasperReport) JRLoader.loadObject(
                    new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath(path))
            );
            sindicato = (Juridica) salvarAcumuladoDB.pesquisaCodigo(1, "Juridica");
            pesEndSindicato = dbEnd.pesquisaEndPorPessoaTipo(sindicato.getPessoa().getId(), 2);

            listaSocs.add(socios);
            listaSocs.addAll(listaDependentes);

            for (int i = 0; i < listaSocs.size(); i++) {
                fisica = db.pesquisaFisicaPorPessoa(listaSocs.get(i).getServicoPessoa().getPessoa().getId());
                pesEndereco = dbEnd.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 1);
                pesEmpresa = dbEmp.pesquisaPessoaEmpresaPorFisica(fisica.getId());
                if (pesEmpresa.getId() != -1) {
                    pesEndEmpresa = dbEnd.pesquisaEndPorPessoaTipo(pesEmpresa.getJuridica().getPessoa().getId(), 2);
                } else {
                    pesEndEmpresa = new PessoaEndereco();
                }

                pesDestinatario = dbEnd.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 1);

                try {
                    dados[0] = pesEndereco.getEndereco().getLogradouro().getDescricao();
                    dados[1] = pesEndereco.getEndereco().getDescricaoEndereco().getDescricao();
                    dados[2] = pesEndereco.getNumero();
                    dados[3] = pesEndereco.getComplemento();
                    dados[4] = pesEndereco.getEndereco().getBairro().getDescricao();
                    dados[5] = pesEndereco.getEndereco().getCidade().getCidade();
                    dados[6] = pesEndereco.getEndereco().getCidade().getUf();
                    dados[7] = AnaliseString.mascaraCep(pesEndereco.getEndereco().getCep());
                } catch (Exception e) {
                    dados[0] = "";
                    dados[1] = "";
                    dados[2] = "";
                    dados[3] = "";
                    dados[4] = "";
                    dados[5] = "";
                    dados[6] = "";
                    dados[7] = "";
                }
                try {
                    dados[8] = pesDestinatario.getEndereco().getLogradouro().getDescricao();
                    dados[9] = pesDestinatario.getEndereco().getDescricaoEndereco().getDescricao();
                    dados[10] = pesDestinatario.getNumero();
                    dados[11] = pesDestinatario.getComplemento();
                    dados[12] = pesDestinatario.getEndereco().getBairro().getDescricao();
                    dados[13] = pesDestinatario.getEndereco().getCidade().getCidade();
                    dados[14] = pesDestinatario.getEndereco().getCidade().getUf();
                    dados[15] = AnaliseString.mascaraCep(pesDestinatario.getEndereco().getCep());
                    dados[26] = pesDestinatario.getPessoa().getDocumento();
                    dados[27] = pesDestinatario.getPessoa().getNome();
                } catch (Exception e) {
                    dados[8] = "";
                    dados[9] = "";
                    dados[10] = "";
                    dados[11] = "";
                    dados[12] = "";
                    dados[13] = "";
                    dados[14] = "";
                    dados[15] = "";
                    dados[26] = "";
                    dados[27] = "";
                }
                try {
                    dados[16] = pesEmpresa.getJuridica().getPessoa().getNome();
                    dados[17] = pesEmpresa.getJuridica().getPessoa().getTelefone1();
                    dados[18] = pesEmpresa.getFuncao().getProfissao();
                    dados[19] = pesEndEmpresa.getEndereco().getDescricaoEndereco().getDescricao();
                    dados[20] = pesEndEmpresa.getNumero();
                    dados[21] = pesEndEmpresa.getComplemento();
                    dados[22] = pesEndEmpresa.getEndereco().getBairro().getDescricao();
                    dados[23] = pesEndEmpresa.getEndereco().getCidade().getCidade();
                    dados[24] = pesEndEmpresa.getEndereco().getCidade().getUf();
                    dados[25] = AnaliseString.mascaraCep(pesEndEmpresa.getEndereco().getCep());
                    dados[28] = pesEmpresa.getAdmissao();
                    dados[29] = pesEmpresa.getJuridica().getPessoa().getDocumento();
                    dados[30] = pesEmpresa.getJuridica().getFantasia();
                    dados[31] = pesEndEmpresa.getEndereco().getLogradouro().getDescricao();
                } catch (Exception e) {
                    dados[16] = "";
                    dados[17] = "";
                    dados[18] = "";
                    dados[19] = "";
                    dados[20] = "";
                    dados[21] = "";
                    dados[22] = "";
                    dados[23] = "";
                    dados[24] = "";
                    dados[25] = "";
                    dados[28] = "";
                    dados[29] = "";
                    dados[30] = "";
                    dados[31] = "";
                }

                try {
                    listaSocios.add(new FichaSocial(0,
                            listaSocs.get(i).getMatriculaSocios().getTitular().getId(),
                            listaSocs.get(i).getMatriculaSocios().getNrMatricula(),
                            listaSocs.get(i).getServicoPessoa().getDtEmissao(),
                            null,
                            listaSocs.get(i).getMatriculaSocios().getCategoria().getGrupoCategoria().getGrupoCategoria(),
                            listaSocs.get(i).getMatriculaSocios().getCategoria().getCategoria(),
                            fisica.getPessoa().getNome(),
                            fisica.getSexo(),
                            fisica.getDtNascimento(),
                            fisica.getNaturalidade(),
                            fisica.getNacionalidade(),
                            fisica.getRg(),
                            fisica.getPessoa().getDocumento(),
                            fisica.getCarteira(),
                            fisica.getSerie(),
                            fisica.getEstadoCivil(),
                            fisica.getPai(),
                            fisica.getMae(),
                            fisica.getPessoa().getTelefone1(),
                            fisica.getPessoa().getTelefone3(),
                            fisica.getPessoa().getEmail1(),
                            dados[0],
                            dados[1],
                            dados[2],
                            dados[3],
                            dados[4],
                            dados[5],
                            dados[6],
                            dados[7],
                            false,
                            dados[26],
                            dados[27],
                            dados[8],
                            dados[9],
                            dados[10],
                            dados[11],
                            dados[12],
                            dados[13],
                            dados[14],
                            dados[15],
                            dados[16],
                            dados[17],
                            null, // fax
                            DataHoje.converte(dados[28]),
                            dados[18],
                            dados[19],
                            dados[20],
                            dados[21],
                            dados[22],
                            dados[23],
                            dados[24],
                            dados[25],
                            ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
                            "", // obs
                            listaSocs.get(i).getParentesco().getParentesco(),
                            sindicato.getPessoa().getNome(),
                            pesEndSindicato.getEndereco().getDescricaoEndereco().getDescricao(),
                            pesEndSindicato.getNumero(),
                            pesEndSindicato.getComplemento(),
                            pesEndSindicato.getEndereco().getBairro().getDescricao(),
                            pesEndSindicato.getEndereco().getCidade().getCidade(),
                            pesEndSindicato.getEndereco().getCidade().getUf(),
                            AnaliseString.mascaraCep(pesEndSindicato.getEndereco().getCep()),
                            sindicato.getPessoa().getDocumento(),
                            "",
                            ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
                            getFotoSocio(listaSocs.get(i)),
                            sindicato.getPessoa().getEmail1(),
                            sindicato.getPessoa().getSite(),
                            sindicato.getPessoa().getTelefone1(),
                            ((ServletContext) faces.getExternalContext().getContext()).getRealPath(pathVerso),
                            dados[29],
                            fisica.getDtRecadastro(),
                            dados[30],
                            pesEndSindicato.getEndereco().getLogradouro().getDescricao(),
                            dados[31], ""));
                } catch (Exception erro) {
                    System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
                    continue;
                }
            }
            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(listaSocios);
            JasperPrint print = JasperFillManager.fillReport(
                    jasper,
                    null,
                    dtSource);
            byte[] arquivo = JasperExportManager.exportReportToPdf(print);
            //response.setContentType("application/pdf");
            //response.setContentLength(arquivo.length);
            //ServletOutputStream saida = response.getOutputStream();
            ///saida.write(arquivo, 0, arquivo.length);
            //saida.flush();
            //saida.close();

            SalvaArquivos sa = new SalvaArquivos(arquivo,
                    nomeDownload,
                    false);
            sa.salvaNaPasta(pathPasta);

            Download download = new Download(nomeDownload,
                    pathPasta,
                    "application/pdf",
                    FacesContext.getCurrentInstance());
            download.baixar();
        } catch (JRException erro) {
            System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
        }
        //return null;
    }

    public static void branco() {
        String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/fichas");
        PessoaEnderecoDB enderecoDB = new PessoaEnderecoDBToplink();
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        try {
            FacesContext faces = FacesContext.getCurrentInstance();
            Collection listaSocios = new ArrayList<FichaSocial>();
            JasperReport jasper = (JasperReport) JRLoader.loadObject(new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/FICHACADASTROBRANCO.jasper")));
            Juridica sindicato = (Juridica) salvarAcumuladoDB.pesquisaCodigo(1, "Juridica");
            PessoaEndereco pessoaEndereco = enderecoDB.pesquisaEndPorPessoaTipo(sindicato.getPessoa().getId(), 2);
            Registro registro = (Registro) salvarAcumuladoDB.pesquisaCodigo(1, "Registro");
            try {
                listaSocios.add(new FichaSocial(0,
                        0, // ID TITULAR
                        0, // NR MATRICULA
                        new Date(), // DATA EMISSÃO
                        new Date(),
                        "", // MATR GRUPO
                        "", // MATR SOC_CATEGORIA
                        "", // NOME
                        "", // SEXO
                        new Date(), // NASCIMENTO
                        "", // NATURALIDADE
                        "", // NACIONALIDADE
                        "", // RG
                        "", // DOCUMENTO
                        "", // CARTEIRINHA
                        "", // SERIE
                        "", // ESTADO CÍVIL
                        "", // PAI
                        "", // MÃE
                        "", // TEL1
                        "", // TEL2
                        "", // EMAIL1
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        false,
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "", // fax
                        DataHoje.converte(DataHoje.data()),
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
                        registro.getFichaSocial(), // obs
                        "",
                        sindicato.getPessoa().getNome(),
                        pessoaEndereco.getEndereco().getDescricaoEndereco().getDescricao(),
                        pessoaEndereco.getNumero(),
                        pessoaEndereco.getComplemento(),
                        pessoaEndereco.getEndereco().getBairro().getDescricao(),
                        pessoaEndereco.getEndereco().getCidade().getCidade(),
                        pessoaEndereco.getEndereco().getCidade().getUf(),
                        AnaliseString.mascaraCep(pessoaEndereco.getEndereco().getCep()),
                        sindicato.getPessoa().getDocumento(),
                        "",
                        ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
                        "",
                        sindicato.getPessoa().getEmail1(),
                        sindicato.getPessoa().getSite(),
                        sindicato.getPessoa().getTelefone1(),
                        "",
                        "",
                        new Date(),
                        "",
                        pessoaEndereco.getEndereco().getLogradouro().getDescricao(),
                        "", ""));
            } catch (Exception erro) {
                System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
            }

            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(listaSocios);
            JasperPrint print = JasperFillManager.fillReport(
                    jasper,
                    null,
                    dtSource);
            byte[] arquivo = JasperExportManager.exportReportToPdf(print);
            String nomeDownload = "ficha_branco.pdf";
            SalvaArquivos sa = new SalvaArquivos(arquivo, nomeDownload, false);
            sa.salvaNaPasta(pathPasta);
            Download download = new Download(nomeDownload,
                    pathPasta,
                    "application/pdf",
                    FacesContext.getCurrentInstance());
            download.baixar();
            download.remover();
        } catch (JRException e) {
        }
    }

    public static String getFotoSocio(Socios socios) {
        FacesContext context = FacesContext.getCurrentInstance();
        File files;
        //if (socios.getId() != -1){
        files = new File(((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/" + socios.getServicoPessoa().getPessoa().getId() + ".jpg"));
        if (files.exists()) {
            return files.getPath();
        } else {
            return ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Imagens/Fotos/semFoto.jpg");
        }
        //}else
        //    return ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Imagens/Fotos/semFoto.jpg");
    }

    public static String getConverteNullString(Object object) {
        if (object == null) {
            return "";
        } else {
            return String.valueOf(object);
        }
    }
}
