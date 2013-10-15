package br.com.rtools.relatorios.beans;

import br.com.rtools.associativo.Categoria;
import br.com.rtools.associativo.GrupoCategoria;
import br.com.rtools.associativo.Parentesco;
import br.com.rtools.associativo.db.CategoriaDB;
import br.com.rtools.associativo.db.CategoriaDBToplink;
import br.com.rtools.associativo.db.GrupoCategoriaDB;
import br.com.rtools.associativo.db.GrupoCategoriaDBToplink;
import br.com.rtools.associativo.db.ParentescoDB;
import br.com.rtools.associativo.db.ParentescoDBToplink;
import br.com.rtools.endereco.Cidade;
import br.com.rtools.financeiro.FTipoDocumento;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.FTipoDocumentoDB;
import br.com.rtools.financeiro.db.FTipoDocumentoDBToplink;
import br.com.rtools.impressao.ParametroSocios;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.db.JuridicaDB;
import br.com.rtools.pessoa.db.JuridicaDBToplink;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.relatorios.db.RelatorioGenericoDB;
import br.com.rtools.relatorios.db.RelatorioGenericoDBToplink;
import br.com.rtools.relatorios.db.RelatorioSociosDB;
import br.com.rtools.relatorios.db.RelatorioSociosDBToplink;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.Download;
import br.com.rtools.utilitarios.SalvaArquivos;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class RelatorioSociosJSFBean {

    private String dataCadastro = "";
    private String dataCadastroFim = "";
    private String dataRecadastro = "";
    private String dataRecadastroFim = "";
    private String dataDemissao = "";
    private String dataDemissaoFim = "";
    private String dataAdmissaoSocio = "";
    private String dataAdmissaoSocioFim = "";
    private String dataAdmissaoEmpresa = "";
    private String dataAdmissaoEmpresaFim = "";
    private String tipoEleicao = "todos";
    private String tipoSexo = "M";
    private String tipoCarteirinha = "com";
    private String tipoSuspensos = "todos";
    private String tipoDescontoFolha = "todos";
    private String tipoFotos = "com";
    private String tipoDescontoGeracao = "todos";
    private String tipoEmpresas = "todas";
    private String tipoOrdem = "nome";
    private String tipoEmail = "todos";
    private String tipoTelefone = "todos";
    private String tipoEstadoCivil = "Solteiro(a)";
    private boolean chkGrupo = true;
    private boolean chkCategoria = true;
    private boolean chkGrau = false;
    private boolean chkTipoCobranca = false;
    private boolean chkCidadesSocio = false;
    private boolean chkCidadesEmpresa = false;
    private boolean chkMeses = false;
    private boolean chkTodos = false;
    private boolean chkSocios = false;
    private boolean chkEscola = false;
    private boolean chkAcademia = false;
    private boolean chkConvênioMedico = false;
    private boolean chkServicos = false;
    private boolean chkEmpresa = false;
    private int idEmpresas = 0;
    private int idDias = 0;
    private int matriculaInicial = 0;
    private int matriculaFinal = 9999999;
    private int idadeInicial = 0;
    private int idadeFinal = 500;
    private int diaInicial = 1;
    private int diaFinal = 31;
    private int idRelatorio = 0;
    private List<DataObject> listaTipoCobranca = new ArrayList();
    private List<DataObject> listaCidadesSocio = new ArrayList();
    private List<DataObject> listaCidadesEmpresa = new ArrayList();
    private List<DataObject> listaMeses = new ArrayList();
    private List<DataObject> listaParentesco = new ArrayList();
    private List listaServicos = new ArrayList();
    private String selectAccordion = "simples";
    private List<DataObject> listaMenuRSocial = new ArrayList();
    private List<DataObject> listaGrupo = new ArrayList();
    private List<DataObject> listaCategoria = new ArrayList();
    private boolean booMatricula = false;
    private boolean booIdade = false;
    private boolean booGrupoCategoria = false;
    private boolean booSexo = false;
    private boolean booGrau = false;
    private boolean booFotos = false;
    private boolean booCarteirinha = false;
    private boolean booTipoCobranca = false;
    private boolean booCidadeSocio = false;
    private boolean booCidadeEmpresa = false;
    private boolean booAniversario = false;
    private boolean booData = false;
    private boolean booVotante = false;
    private boolean booEmail = false;
    private boolean booTelefone = false;
    private boolean booEstadoCivil = false;
    private boolean booEmpresa = false;

    public String editarOpcao(int index) {
        if (listaMenuRSocial.get(index).getArgumento1().equals("Remover")) {
            listaMenuRSocial.get(index).setArgumento1("Editar");
        } else {
            listaMenuRSocial.get(index).setArgumento1("Remover");
        }

        if (index == 0) {
            if (booMatricula) {
                booMatricula = false;
            } else {
                booMatricula = true;
            }
        } else if (index == 1) {
            if (booIdade) {
                booIdade = false;
            } else {
                booIdade = true;
            }
        } else if (index == 2) {
            if (booGrupoCategoria) {
                booGrupoCategoria = false;
            } else {
                booGrupoCategoria = true;
            }
        } else if (index == 3) {
            if (booSexo) {
                booSexo = false;
            } else {
                booSexo = true;
            }
        } else if (index == 4) {
            if (booGrau) {
                booGrau = false;
            } else {
                booGrau = true;
            }
        } else if (index == 5) {
            if (booFotos) {
                booFotos = false;
            } else {
                booFotos = true;
            }
        } else if (index == 6) {
            if (booCarteirinha) {
                booCarteirinha = false;
            } else {
                booCarteirinha = true;
            }
        } else if (index == 7) {
            if (booTipoCobranca) {
                booTipoCobranca = false;
            } else {
                booTipoCobranca = true;
            }
        } else if (index == 8) {
            if (booCidadeSocio) {
                booCidadeSocio = false;
            } else {
                booCidadeSocio = true;
            }
        } else if (index == 9) {
            if (booCidadeEmpresa) {
                booCidadeEmpresa = false;
            } else {
                booCidadeEmpresa = true;
            }
        } else if (index == 10) {
            if (booAniversario) {
                booAniversario = false;
            } else {
                booAniversario = true;
            }
        } else if (index == 11) {
            if (booData) {
                booData = false;
            } else {
                booData = true;
            }
        } else if (index == 12) {
            if (booVotante) {
                booVotante = false;
            } else {
                booVotante = true;
            }
        } else if (index == 13) {
            if (booEmail) {
                booEmail = false;
            } else {
                booEmail = true;
            }
        } else if (index == 14) {
            if (booTelefone) {
                booTelefone = false;
            } else {
                booTelefone = true;
            }
        } else if (index == 15) {
            if (booEstadoCivil) {
                booEstadoCivil = false;
            } else {
                booEstadoCivil = true;
            }
        } else if (index == 16) {
            if (booEmpresa) {
                booEmpresa = false;
            } else {
                booEmpresa = true;
            }
        }
        return "relatorioSocios";
    }

    public List<DataObject> getListaMenuRSocial() {
        if (listaMenuRSocial.isEmpty()) {
            listaMenuRSocial.add(new DataObject("* Número da Matricula ", "Editar", null, null, null, null));
            listaMenuRSocial.add(new DataObject("* Idade ", "Editar", null, null, null, null));
            listaMenuRSocial.add(new DataObject("* Grupo / Categoria ", "Editar", null, null, null, null));
            listaMenuRSocial.add(new DataObject("* Sexo ", "Editar", null, null, null, null));
            listaMenuRSocial.add(new DataObject("* Grau ", "Editar", null, null, null, null));
            listaMenuRSocial.add(new DataObject("* Fotos ", "Editar", null, null, null, null));
            listaMenuRSocial.add(new DataObject("* Carteirinha ", "Editar", null, null, null, null));
            listaMenuRSocial.add(new DataObject("* Tipo de Pagamento ", "Editar", null, null, null, null));
            listaMenuRSocial.add(new DataObject("* Cidade do Sócio ", "Editar", null, null, null, null));
            listaMenuRSocial.add(new DataObject("* Cidade do Empresa ", "Editar", null, null, null, null));
            listaMenuRSocial.add(new DataObject("* Aniversário ", "Editar", null, null, null, null));
            listaMenuRSocial.add(new DataObject("* Pesquisa por Datas ", "Editar", null, null, null, null));
            listaMenuRSocial.add(new DataObject("* Votante ", "Editar", null, null, null, null));
            listaMenuRSocial.add(new DataObject("* Email ", "Editar", null, null, null, null));
            listaMenuRSocial.add(new DataObject("* Telefone ", "Editar", null, null, null, null));
            listaMenuRSocial.add(new DataObject("* Estado Civil ", "Editar", null, null, null, null));
            listaMenuRSocial.add(new DataObject("* Empresas ", "Editar", null, null, null, null));
        }
        return listaMenuRSocial;
    }

    public void setListaMenuRSocial(List<DataObject> listaMenuRSocial) {
        this.listaMenuRSocial = listaMenuRSocial;
    }

    public List<SelectItem> getListaTipoRelatorios() {
        List<SelectItem> relatorios = new Vector<SelectItem>();
        int i = 0;
        RelatorioGenericoDB db = new RelatorioGenericoDBToplink();
        List select = db.pesquisaTipoRelatorio(171);
        while (i < select.size()) {
            relatorios.add(new SelectItem(new Integer(i),
                    (String) ((Relatorios) select.get(i)).getNome(),
                    Integer.toString(((Relatorios) select.get(i)).getId())));
            i++;
        }
        return relatorios;
    }

    public String visualizarRelatorio() {
        RelatorioGenericoDB db = new RelatorioGenericoDBToplink();
        RelatorioSociosDB dbS = new RelatorioSociosDBToplink();
        Relatorios relatorios = db.pesquisaRelatorios(Integer.parseInt(getListaTipoRelatorios().get(idRelatorio).getDescription()));

        String ids_gc = "", ids_c = "";
        if (booGrupoCategoria) {
            for (int i = 0; i < listaGrupo.size(); i++) {
                if ((Boolean) listaGrupo.get(i).getArgumento0()) {
                    if (ids_gc.length() > 0 && i != listaGrupo.size()) {
                        ids_gc += ",";
                    }
                    ids_gc += String.valueOf(((GrupoCategoria) listaGrupo.get(i).getArgumento1()).getId());
                }
            }
            for (int i = 0; i < listaCategoria.size(); i++) {
                if ((Boolean) listaCategoria.get(i).getArgumento0()) {
                    if (ids_c.length() > 0 && i != listaCategoria.size()) {
                        ids_c += ",";
                    }
                    ids_c += String.valueOf(((Categoria) listaCategoria.get(i).getArgumento1()).getId());
                }
            }
        }

        String meses = "";
        String di = String.valueOf(diaInicial), df = String.valueOf(diaFinal);
        if (booAniversario) {
            for (int i = 0; i < listaMeses.size(); i++) {
                if ((Boolean) listaMeses.get(i).getArgumento0()) {
                    if (meses.length() > 0 && i != listaMeses.size()) {
                        meses += ",";
                    }
                    meses += listaMeses.get(i).getArgumento2();
                }
            }

            if (di.length() == 1) {
                di = "0" + di;
            }
            if (df.length() == 1) {
                df = "0" + df;
            }
        }

        String ids_pagamento = "";
        for (int i = 0; i < listaTipoCobranca.size(); i++) {
            if ((Boolean) listaTipoCobranca.get(i).getArgumento0()) {
                if (ids_pagamento.length() > 0 && i != listaTipoCobranca.size()) {
                    ids_pagamento += ",";
                }
                ids_pagamento += ((FTipoDocumento) listaTipoCobranca.get(i).getArgumento1()).getId();
            }
        }

        String ids_cidade_socio = "";
        for (int i = 0; i < listaCidadesSocio.size(); i++) {
            if ((Boolean) listaCidadesSocio.get(i).getArgumento0()) {
                if (ids_cidade_socio.length() > 0 && i != listaCidadesSocio.size()) {
                    ids_cidade_socio += ",";
                }
                ids_cidade_socio += ((Cidade) listaCidadesSocio.get(i).getArgumento1()).getId();
            }
        }

        String ids_cidade_empresa = "";
        for (int i = 0; i < listaCidadesEmpresa.size(); i++) {
            if ((Boolean) listaCidadesEmpresa.get(i).getArgumento0()) {
                if (ids_cidade_empresa.length() > 0 && i != listaCidadesEmpresa.size()) {
                    ids_cidade_empresa += ",";
                }
                ids_cidade_empresa += ((Cidade) listaCidadesEmpresa.get(i).getArgumento1()).getId();
            }
        }

        String ids_parentesco = "";
        for (int i = 0; i < listaParentesco.size(); i++) {
            if ((Boolean) listaParentesco.get(i).getArgumento0()) {
                if (ids_parentesco.length() > 0 && i != listaParentesco.size()) {
                    ids_parentesco += ",";
                }
                ids_parentesco += ((Parentesco) listaParentesco.get(i).getArgumento1()).getId();
            }
        }

        int id_juridica = -1;
        if (!getListaEmpresas().isEmpty()) {
            JuridicaDB dbJ = new JuridicaDBToplink();
            id_juridica = dbJ.pesquisaCodigo(Integer.parseInt(getListaEmpresas().get(idEmpresas).getDescription())).getId();
        }

        List<Vector> result = dbS.pesquisaSocios(relatorios, booMatricula, matriculaInicial, matriculaFinal, booIdade, idadeInicial, idadeFinal, booGrupoCategoria, ids_gc, ids_c,
                booSexo, tipoSexo, booGrau, ids_parentesco, booFotos, tipoFotos, booCarteirinha, tipoCarteirinha,
                booTipoCobranca, ids_pagamento, booCidadeSocio, ids_cidade_socio, booCidadeEmpresa, ids_cidade_empresa,
                booAniversario, meses, di, df, booData, dataCadastro, dataCadastroFim, dataRecadastro, dataRecadastroFim, dataDemissao, dataDemissaoFim, dataAdmissaoSocio,
                dataAdmissaoSocioFim, dataAdmissaoEmpresa, dataAdmissaoEmpresaFim, booVotante, tipoEleicao,
                booEmail, tipoEmail, booTelefone, tipoTelefone, booEstadoCivil, tipoEstadoCivil, booEmpresa, tipoEmpresas, id_juridica, tipoOrdem);

        Collection lista = new ArrayList<ParametroSocios>();

        for (int i = 0; i < result.size(); i++) {
            lista.add(new ParametroSocios(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Imagens/LogoCliente.png"),
                    getConverteNullString(result.get(i).get(1)), // SITE
                    getConverteNullString(result.get(i).get(2)), // SIN NOME
                    getConverteNullString(result.get(i).get(3)), // SIN ENDERECO
                    getConverteNullString(result.get(i).get(4)), // SIN LOGRADOURO
                    getConverteNullString(result.get(i).get(5)), // SIN NUMERO
                    getConverteNullString(result.get(i).get(6)), // SIN COMPLEMENTO
                    getConverteNullString(result.get(i).get(7)), // SIN BAIRRO
                    getConverteNullString(result.get(i).get(8)), // SIN CEP
                    getConverteNullString(result.get(i).get(9)), // SIN CIDADE
                    getConverteNullString(result.get(i).get(10)),// SIN UF 
                    getConverteNullString(result.get(i).get(11)),// SIN DOCUMENTO 
                    getConverteNullInt(result.get(i).get(12)),// CODIGO 
                    (Date) result.get(i).get(13),// CADASTRO
                    getConverteNullString(result.get(i).get(14)),// NOME
                    getConverteNullString(result.get(i).get(15)),// CPF
                    getConverteNullString(result.get(i).get(16)),// TELEFONE
                    getConverteNullString(result.get(i).get(17)),// UF EMISSAO RG
                    getConverteNullString(result.get(i).get(18)),// ESTADO CIVIL
                    getConverteNullString(result.get(i).get(19)),// CTPS
                    getConverteNullString(result.get(i).get(20)),// PAI
                    getConverteNullString(result.get(i).get(21)),// SEXO
                    getConverteNullString(result.get(i).get(22)),// MAE
                    getConverteNullString(result.get(i).get(23)),// NACIONALIDADE
                    getConverteNullString(result.get(i).get(24)),// NIT
                    getConverteNullString(result.get(i).get(25)),// ORGAO EMISSAO RG
                    getConverteNullString(result.get(i).get(26)),// PIS
                    getConverteNullString(result.get(i).get(27)),// SERIE
                    (Date) result.get(i).get(28),// APOSENTADORIA ------------
                    getConverteNullString(result.get(i).get(29)),// NATURALIDADE
                    (Date) result.get(i).get(30),// RECADASTRO
                    (Date) result.get(i).get(31),// DT NASCIMENTO -------------
                    (Date) result.get(i).get(32),// DT FOTO -------------------
                    getConverteNullString(result.get(i).get(33)),// RG
                    "",// CAMINHO DA FOTO SOCIO
                    getConverteNullString(result.get(i).get(35)),// LOGRADOURO
                    getConverteNullString(result.get(i).get(36)),// ENDERECO
                    getConverteNullString(result.get(i).get(37)),// NUMERO
                    getConverteNullString(result.get(i).get(38)),// COMPLEMENTO
                    getConverteNullString(result.get(i).get(39)),// BAIRRO
                    getConverteNullString(result.get(i).get(40)),// CIDADE
                    getConverteNullString(result.get(i).get(41)),// UF
                    getConverteNullString(result.get(i).get(42)),// CEP
                    getConverteNullString(result.get(i).get(43)),// SETOR
                    (Date) result.get(i).get(44),// DT ADMISSAO ---------------
                    getConverteNullString(result.get(i).get(45)),// PROFISSAO
                    getConverteNullString(result.get(i).get(46)),// EMPRESA FANTASIA
                    getConverteNullString(result.get(i).get(47)),// NOME EMPRESA
                    getConverteNullString(result.get(i).get(48)),// EMPRESA CNPJ
                    getConverteNullString(result.get(i).get(49)),// EMPRESA TELEFONE
                    getConverteNullString(result.get(i).get(50)),// EMPRESA LOGRADOURO
                    getConverteNullString(result.get(i).get(51)),// EMPRESA ENDERECO
                    getConverteNullString(result.get(i).get(52)),// EMPRESA NUMERO
                    getConverteNullString(result.get(i).get(53)),// "       COMPLEMENTO 
                    getConverteNullString(result.get(i).get(54)),// "       BAIRRO
                    getConverteNullString(result.get(i).get(55)),// "       CIDADE
                    getConverteNullString(result.get(i).get(56)),// "       UF
                    getConverteNullString(result.get(i).get(57)),// "       CEP
                    getConverteNullString(result.get(i).get(58)),// TITULAR
                    getConverteNullString(result.get(i).get(59)),// COD SOCIO
                    getConverteNullString(result.get(i).get(60)),// NOME SOCIO
                    getConverteNullString(result.get(i).get(61)),// PARENTESCO 
                    getConverteNullInt(result.get(i).get(62)),// MATRICULA
                    getConverteNullString(result.get(i).get(63)),// CATEGORIA
                    getConverteNullString(result.get(i).get(64)),// GRUPO CATEGORIA
                    (Date) result.get(i).get(65),// DT FILIACAO --------------
                    (Date) result.get(i).get(66),// INATIVACAO ---------------
                    (Boolean) result.get(i).get(67),// VOTANTE
                    getConverteNullString(result.get(i).get(68)),// GRAU
                    new BigDecimal(Float.parseFloat(getConverteNullString(result.get(i).get(58)))),// NR DESCONTO
                    (Boolean) result.get(i).get(70),
                    getConverteNullString(result.get(i).get(71)),// TIPO COBRANCA
                    getConverteNullInt(result.get(i).get(72)),// COD TIPO COBRANCA
                    getConverteNullString(result.get(i).get(73)),// TELEFONE2
                    getConverteNullString(result.get(i).get(74)) // TELEFONE3                                          
                    ));

        }
        try {

            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
            JasperReport jasper = (JasperReport) JRLoader.loadObject(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(relatorios.getJasper()));
            JasperPrint print = JasperFillManager.fillReport(jasper, null, dtSource);

            byte[] arquivo = new byte[0];
            arquivo = JasperExportManager.exportReportToPdf(print);

            String nomeDownload = relatorios.getNome() + "_" + DataHoje.horaMinuto().replace(":", "") + ".pdf";
            SalvaArquivos sa = new SalvaArquivos(arquivo, nomeDownload, false);

            String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/relatorios");
            sa.salvaNaPasta(pathPasta);

            Download download = new Download(nomeDownload, pathPasta, "application/pdf", FacesContext.getCurrentInstance());
            download.baixar();
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    public List<DataObject> getListaCategoria() {
        if (listaCategoria.isEmpty()) {
            CategoriaDB db = new CategoriaDBToplink();
            List<Categoria> result = new ArrayList();
            if (!listaGrupo.isEmpty()) {

                String ids = "";
                for (int i = 0; i < listaGrupo.size(); i++) {
                    if ((Boolean) listaGrupo.get(i).getArgumento0()) {
                        if (ids.length() > 0 && i != listaGrupo.size()) {
                            ids += ",";
                        }
                        ids += ((GrupoCategoria) listaGrupo.get(i).getArgumento1()).getId();
                    }
                }
                if (!ids.isEmpty()) {
                    result = db.pesquisaCategoriaPorGrupoIds(ids);
                }
//                }else{
//                    result = db.pesquisaTodos();
//                }
            }
            for (int i = 0; i < result.size(); i++) {
                listaCategoria.add(new DataObject(true, result.get(i)));
            }
        }
        return listaCategoria;
    }

    public List<SelectItem> getListaEmpresas() {
        List<SelectItem> empresas = new Vector<SelectItem>();
        if (tipoEmpresas.equals("especificas")) {
            int i = 0;
            RelatorioSociosDB db = new RelatorioSociosDBToplink();
            List<Juridica> select = db.listaEmpresaDoSocio();
            while (i < select.size()) {
                empresas.add(new SelectItem(new Integer(i),
                        (String) ((Juridica) select.get(i)).getPessoa().getNome(),
                        Integer.toString(((Juridica) select.get(i)).getId())));
                i++;
            }
        }
        return empresas;
    }

    public List<DataObject> getListaTipoCobranca() {
        if (listaTipoCobranca.isEmpty()) {
            FTipoDocumentoDB db = new FTipoDocumentoDBToplink();
            List select = new ArrayList();
            select.add(db.pesquisaCodigo(2));
            select.addAll(db.pesquisaListaTipoExtrato());
            for (int i = 0; i < select.size(); i++) {
                listaTipoCobranca.add(new DataObject(false, (FTipoDocumento) select.get(i)));
            }
        }
        return listaTipoCobranca;
    }

    public void setListaTipoCobranca(List<DataObject> listaTipoCobranca) {
        this.listaTipoCobranca = listaTipoCobranca;
    }

    public void marcarTipos() {
        for (int i = 0; i < listaTipoCobranca.size(); i++) {
            listaTipoCobranca.get(i).setArgumento0(chkTipoCobranca);
        }
    }

    public void marcarGrau() {
        for (int i = 0; i < listaParentesco.size(); i++) {
            listaParentesco.get(i).setArgumento0(chkGrau);
        }
    }

    public List getListaCidadesSocio() {
        if (listaCidadesSocio.isEmpty()) {
            RelatorioSociosDB db = new RelatorioSociosDBToplink();
            List select = new ArrayList();
            select.addAll(db.listaCidadeDoSocio());
            for (int i = 0; i < select.size(); i++) {
                listaCidadesSocio.add(new DataObject(false, ((Cidade) select.get(i))));
            }
        }
        return listaCidadesSocio;
    }

    public void setListaCidadesSocio(List listaCidadesSocio) {
        this.listaCidadesSocio = listaCidadesSocio;
    }

    public void marcarCidadesSocio() {
        for (int i = 0; i < listaCidadesSocio.size(); i++) {
            ((DataObject) listaCidadesSocio.get(i)).setArgumento0(chkCidadesSocio);
        }
    }

    public List getListaCidadesEmpresa() {
        if (listaCidadesEmpresa.isEmpty()) {
            RelatorioSociosDB db = new RelatorioSociosDBToplink();
            List select = new ArrayList();
            select.addAll(db.listaCidadeDaEmpresa());
            for (int i = 0; i < select.size(); i++) {
                listaCidadesEmpresa.add(new DataObject(false, ((Cidade) select.get(i))));
            }
        }
        return listaCidadesEmpresa;
    }

    public void setListaCidadesEmpresa(List listaCidadesEmpresa) {
        this.listaCidadesEmpresa = listaCidadesEmpresa;
    }

    public void marcarCidadesEmpresa() {
        for (int i = 0; i < listaCidadesEmpresa.size(); i++) {
            listaCidadesEmpresa.get(i).setArgumento0(chkCidadesEmpresa);
        }
    }

    public List<DataObject> getListaMeses() {
        if (listaMeses.isEmpty()) {
            listaMeses.add(new DataObject(false, "Janeiro", "01", null, null, null));
            listaMeses.add(new DataObject(false, "Fevereiro", "02", null, null, null));
            listaMeses.add(new DataObject(false, "Março", "03", null, null, null));
            listaMeses.add(new DataObject(false, "Abril", "04", null, null, null));
            listaMeses.add(new DataObject(false, "Maio", "05", null, null, null));
            listaMeses.add(new DataObject(false, "Junho", "06", null, null, null));
            listaMeses.add(new DataObject(false, "Julho", "07", null, null, null));
            listaMeses.add(new DataObject(false, "Agosto", "08", null, null, null));
            listaMeses.add(new DataObject(false, "Setembro", "09", null, null, null));
            listaMeses.add(new DataObject(false, "Outubro", "10", null, null, null));
            listaMeses.add(new DataObject(false, "Novembro", "11", null, null, null));
            listaMeses.add(new DataObject(false, "Dezembro", "12", null, null, null));
        }
        return listaMeses;
    }

    public void setListaMeses(List listaMeses) {
        this.listaMeses = listaMeses;
    }

    public void marcarMeses() {
        for (int i = 0; i < listaMeses.size(); i++) {
            ((DataObject) listaMeses.get(i)).setArgumento0(chkMeses);
        }
    }

    public List getListaServicos() {
        if (listaServicos.isEmpty()) {
            RelatorioSociosDB db = new RelatorioSociosDBToplink();
            List select = new ArrayList();
            if (chkSocios) {
                select.addAll(db.listaSPSocios());
            }
            if (chkConvênioMedico) {
                select.addAll(db.listaSPConvenioMedico());
            }
            if (chkAcademia) {
                select.addAll(db.listaSPAcademia());
            }
            if (chkEscola) {
                select.addAll(db.listaSPEscola());
            }
            for (int i = 0; i < select.size(); i++) {
                listaServicos.add(new DataObject(false, (Servicos) select.get(i)));
            }
        }
        return listaServicos;
    }

    public void setListaServicos(List listaServicos) {
        this.listaServicos = listaServicos;
    }

    public void marcarServicos() {
        for (int i = 0; i < listaServicos.size(); i++) {
            ((DataObject) listaServicos.get(i)).setArgumento0(chkServicos);
        }
    }

    public void marcarInscritos() {
        chkSocios = chkTodos;
        chkAcademia = chkTodos;
        chkConvênioMedico = chkTodos;
        chkEscola = chkTodos;
        refreshFormServicos();
    }

    public void refreshForm() {
    }

    public String getConverteNullString(Object object) {
        if (object == null) {
            return "";
        } else {
            return String.valueOf(object);
        }
    }

    public int getConverteNullInt(Object object) {
        if (object == null) {
            return 0;
        } else {
            return (Integer) object;
        }
    }

    public void refreshFormServicos() {
        listaServicos.clear();
    }

    public String getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getDataRecadastro() {
        return dataRecadastro;
    }

    public void setDataRecadastro(String dataRecadastro) {
        this.dataRecadastro = dataRecadastro;
    }

    public boolean isChkGrupo() {
        return chkGrupo;
    }

    public void setChkGrupo(boolean chkGrupo) {
        this.chkGrupo = chkGrupo;
    }

    public boolean isChkCategoria() {
        return chkCategoria;
    }

    public void setChkCategoria(boolean chkCategoria) {
        this.chkCategoria = chkCategoria;
    }

    public int getMatriculaInicial() {
        return matriculaInicial;
    }

    public void setMatriculaInicial(int matriculaInicial) {
        this.matriculaInicial = matriculaInicial;
    }

    public int getMatriculaFinal() {
        return matriculaFinal;
    }

    public void setMatriculaFinal(int matriculaFinal) {
        this.matriculaFinal = matriculaFinal;
    }

    public String getTipoEleicao() {
        return tipoEleicao;
    }

    public void setTipoEleicao(String tipoEleicao) {
        this.tipoEleicao = tipoEleicao;
    }

    public int getIdadeInicial() {
        return idadeInicial;
    }

    public void setIdadeInicial(int idadeInicial) {
        this.idadeInicial = idadeInicial;
    }

    public int getIdadeFinal() {
        return idadeFinal;
    }

    public void setIdadeFinal(int idadeFinal) {
        this.idadeFinal = idadeFinal;
    }

    public String getTipoSexo() {
        return tipoSexo;
    }

    public void setTipoSexo(String tipoSexo) {
        this.tipoSexo = tipoSexo;
    }

    public boolean isChkGrau() {
        return chkGrau;
    }

    public void setChkGrau(boolean chkGrau) {
        this.chkGrau = chkGrau;
    }

    public String getTipoCarteirinha() {
        return tipoCarteirinha;
    }

    public void setTipoCarteirinha(String tipoCarteirinha) {
        this.tipoCarteirinha = tipoCarteirinha;
    }

    public String getTipoSuspensos() {
        return tipoSuspensos;
    }

    public void setTipoSuspensos(String tipoSuspensos) {
        this.tipoSuspensos = tipoSuspensos;
    }

    public String getTipoDescontoFolha() {
        return tipoDescontoFolha;
    }

    public void setTipoDescontoFolha(String tipoDescontoFolha) {
        this.tipoDescontoFolha = tipoDescontoFolha;
    }

    public String getTipoFotos() {
        return tipoFotos;
    }

    public void setTipoFotos(String tipoFotos) {
        this.tipoFotos = tipoFotos;
    }

    public String getTipoDescontoGeracao() {
        return tipoDescontoGeracao;
    }

    public void setTipoDescontoGeracao(String tipoDescontoGeracao) {
        this.tipoDescontoGeracao = tipoDescontoGeracao;
    }

    public String getTipoEmpresas() {
        return tipoEmpresas;
    }

    public void setTipoEmpresas(String tipoEmpresas) {
        this.tipoEmpresas = tipoEmpresas;
    }

    public int getIdEmpresas() {
        return idEmpresas;
    }

    public void setIdEmpresas(int idEmpresas) {
        this.idEmpresas = idEmpresas;
    }

    public boolean isChkTipoCobranca() {
        return chkTipoCobranca;
    }

    public void setChkTipoCobranca(boolean chkTipoCobranca) {
        this.chkTipoCobranca = chkTipoCobranca;
    }

    public boolean isChkCidadesSocio() {
        return chkCidadesSocio;
    }

    public void setChkCidadesSocio(boolean chkCidadesSocio) {
        this.chkCidadesSocio = chkCidadesSocio;
    }

    public boolean isChkCidadesEmpresa() {
        return chkCidadesEmpresa;
    }

    public void setChkCidadesEmpresa(boolean chkCidadesEmpresa) {
        this.chkCidadesEmpresa = chkCidadesEmpresa;
    }

    public boolean isChkMeses() {
        return chkMeses;
    }

    public void setChkMeses(boolean chkMeses) {
        this.chkMeses = chkMeses;
    }

    public int getIdDias() {
        return idDias;
    }

    public void setIdDias(int idDias) {
        this.idDias = idDias;
    }

    public int getDiaInicial() {
        return diaInicial;
    }

    public void setDiaInicial(int diaInicial) {
        this.diaInicial = diaInicial;
    }

    public int getDiaFinal() {
        return diaFinal;
    }

    public void setDiaFinal(int diaFinal) {
        this.diaFinal = diaFinal;
    }

    public String getTipoOrdem() {
        return tipoOrdem;
    }

    public void setTipoOrdem(String tipoOrdem) {
        this.tipoOrdem = tipoOrdem;
    }

    public boolean isChkTodos() {
        return chkTodos;
    }

    public void setChkTodos(boolean chkTodos) {
        this.chkTodos = chkTodos;
    }

    public boolean isChkSocios() {
        return chkSocios;
    }

    public void setChkSocios(boolean chkSocios) {
        this.chkSocios = chkSocios;
    }

    public boolean isChkEscola() {
        return chkEscola;
    }

    public void setChkEscola(boolean chkEscola) {
        this.chkEscola = chkEscola;
    }

    public boolean isChkAcademia() {
        return chkAcademia;
    }

    public void setChkAcademia(boolean chkAcademia) {
        this.chkAcademia = chkAcademia;
    }

    public boolean isChkConvênioMedico() {
        return chkConvênioMedico;
    }

    public void setChkConvênioMedico(boolean chkConvênioMedico) {
        this.chkConvênioMedico = chkConvênioMedico;
    }

    public boolean isChkServicos() {
        return chkServicos;
    }

    public void setChkServicos(boolean chkServicos) {
        this.chkServicos = chkServicos;
    }

    public int getIdRelatorio() {
        return idRelatorio;
    }

    public void setIdRelatorio(int idRelatorio) {
        this.idRelatorio = idRelatorio;
    }

    public String getSelectAccordion() {
        return selectAccordion;
    }

    public void setSelectAccordion(String selectAccordion) {
        this.selectAccordion = selectAccordion;
    }

    public boolean isBooMatricula() {
        return booMatricula;
    }

    public void setBooMatricula(boolean booMatricula) {
        this.booMatricula = booMatricula;
    }

    public boolean isBooIdade() {
        return booIdade;
    }

    public void setBooIdade(boolean booIdade) {
        this.booIdade = booIdade;
    }

    public boolean isBooGrupoCategoria() {
        return booGrupoCategoria;
    }

    public void setBooGrupoCategoria(boolean booGrupoCategoria) {
        this.booGrupoCategoria = booGrupoCategoria;
    }

    public List<DataObject> getListaGrupo() {
        if (listaGrupo.isEmpty()) {
            GrupoCategoriaDB db = new GrupoCategoriaDBToplink();
            List<GrupoCategoria> result = db.pesquisaTodos();
            for (int i = 0; i < result.size(); i++) {
                listaGrupo.add(new DataObject(true, result.get(i)));
            }
        }
        return listaGrupo;
    }

    public void setListaGrupo(List<DataObject> listaGrupo) {
        this.listaGrupo = listaGrupo;
    }

    public void marcarGrupos() {
        for (int i = 0; i < listaGrupo.size(); i++) {
            listaGrupo.get(i).setArgumento0(chkGrupo);
        }
        listaCategoria.clear();
    }

    public void marcarUmGrupo() {
        listaCategoria.clear();
    }

    public void marcarCategorias() {
        for (int i = 0; i < listaCategoria.size(); i++) {
            listaCategoria.get(i).setArgumento0(chkCategoria);
        }
    }

    public boolean isBooSexo() {
        return booSexo;
    }

    public void setBooSexo(boolean booSexo) {
        this.booSexo = booSexo;
    }

    public boolean isBooGrau() {
        return booGrau;
    }

    public void setBooGrau(boolean booGrau) {
        this.booGrau = booGrau;
    }

    public boolean isBooFotos() {
        return booFotos;
    }

    public void setBooFotos(boolean booFotos) {
        this.booFotos = booFotos;
    }

    public boolean isBooCarteirinha() {
        return booCarteirinha;
    }

    public void setBooCarteirinha(boolean booCarteirinha) {
        this.booCarteirinha = booCarteirinha;
    }

    public boolean isBooTipoCobranca() {
        return booTipoCobranca;
    }

    public void setBooTipoCobranca(boolean booTipoCobranca) {
        this.booTipoCobranca = booTipoCobranca;
    }

    public boolean isBooCidadeSocio() {
        return booCidadeSocio;
    }

    public void setBooCidadeSocio(boolean booCidadeSocio) {
        this.booCidadeSocio = booCidadeSocio;
    }

    public boolean isBooCidadeEmpresa() {
        return booCidadeEmpresa;
    }

    public void setBooCidadeEmpresa(boolean booCidadeEmpresa) {
        this.booCidadeEmpresa = booCidadeEmpresa;
    }

    public boolean isBooAniversario() {
        return booAniversario;
    }

    public void setBooAniversario(boolean booAniversario) {
        this.booAniversario = booAniversario;
    }

    public boolean isBooData() {
        return booData;
    }

    public void setBooData(boolean booData) {
        this.booData = booData;
    }

    public String getDataDemissao() {
        return dataDemissao;
    }

    public void setDataDemissao(String dataDemissao) {
        this.dataDemissao = dataDemissao;
    }

    public boolean isBooVotante() {
        return booVotante;
    }

    public void setBooVotante(boolean booVotante) {
        this.booVotante = booVotante;
    }

    public boolean isBooEmail() {
        return booEmail;
    }

    public void setBooEmail(boolean booEmail) {
        this.booEmail = booEmail;
    }

    public boolean isBooTelefone() {
        return booTelefone;
    }

    public void setBooTelefone(boolean booTelefone) {
        this.booTelefone = booTelefone;
    }

    public String getTipoEmail() {
        return tipoEmail;
    }

    public void setTipoEmail(String tipoEmail) {
        this.tipoEmail = tipoEmail;
    }

    public String getTipoTelefone() {
        return tipoTelefone;
    }

    public void setTipoTelefone(String tipoTelefone) {
        this.tipoTelefone = tipoTelefone;
    }

    public boolean isBooEstadoCivil() {
        return booEstadoCivil;
    }

    public void setBooEstadoCivil(boolean booEstadoCivil) {
        this.booEstadoCivil = booEstadoCivil;
    }

    public String getTipoEstadoCivil() {
        return tipoEstadoCivil;
    }

    public void setTipoEstadoCivil(String tipoEstadoCivil) {
        this.tipoEstadoCivil = tipoEstadoCivil;
    }

    public List<DataObject> getListaParentesco() {
        if (listaParentesco.isEmpty()) {
            ParentescoDB db = new ParentescoDBToplink();
            List select = db.pesquisaTodos();
            for (int i = 0; i < select.size(); i++) {
                listaParentesco.add(new DataObject(false, ((Parentesco) select.get(i))));
            }
        }
        return listaParentesco;
    }

    public void setListaParentesco(List<DataObject> listaParentesco) {
        this.listaParentesco = listaParentesco;
    }

    public boolean isChkEmpresa() {
        return chkEmpresa;
    }

    public void setChkEmpresa(boolean chkEmpresa) {
        this.chkEmpresa = chkEmpresa;
    }

    public boolean isBooEmpresa() {
        return booEmpresa;
    }

    public void setBooEmpresa(boolean booEmpresa) {
        this.booEmpresa = booEmpresa;
    }

    public String getDataAdmissaoSocio() {
        return dataAdmissaoSocio;
    }

    public void setDataAdmissaoSocio(String dataAdmissaoSocio) {
        this.dataAdmissaoSocio = dataAdmissaoSocio;
    }

    public String getDataAdmissaoEmpresa() {
        return dataAdmissaoEmpresa;
    }

    public void setDataAdmissaoEmpresa(String dataAdmissaoEmpresa) {
        this.dataAdmissaoEmpresa = dataAdmissaoEmpresa;
    }

    public String getDataCadastroFim() {
        return dataCadastroFim;
    }

    public void setDataCadastroFim(String dataCadastroFim) {
        this.dataCadastroFim = dataCadastroFim;
    }

    public String getDataRecadastroFim() {
        return dataRecadastroFim;
    }

    public void setDataRecadastroFim(String dataRecadastroFim) {
        this.dataRecadastroFim = dataRecadastroFim;
    }

    public String getDataDemissaoFim() {
        return dataDemissaoFim;
    }

    public void setDataDemissaoFim(String dataDemissaoFim) {
        this.dataDemissaoFim = dataDemissaoFim;
    }

    public String getDataAdmissaoSocioFim() {
        return dataAdmissaoSocioFim;
    }

    public void setDataAdmissaoSocioFim(String dataAdmissaoSocioFim) {
        this.dataAdmissaoSocioFim = dataAdmissaoSocioFim;
    }

    public String getDataAdmissaoEmpresaFim() {
        return dataAdmissaoEmpresaFim;
    }

    public void setDataAdmissaoEmpresaFim(String dataAdmissaoEmpresaFim) {
        this.dataAdmissaoEmpresaFim = dataAdmissaoEmpresaFim;
    }
}