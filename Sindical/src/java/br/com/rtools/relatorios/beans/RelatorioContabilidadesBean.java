package br.com.rtools.relatorios.beans;

import br.com.rtools.endereco.Cidade;
import br.com.rtools.endereco.db.CidadeDB;
import br.com.rtools.endereco.db.CidadeDBToplink;
import br.com.rtools.impressao.ParametroEscritorios;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.TipoEndereco;
import br.com.rtools.pessoa.db.*;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.relatorios.db.RelatorioContabilidadesDB;
import br.com.rtools.relatorios.db.RelatorioContabilidadesDBToplink;
import br.com.rtools.relatorios.db.RelatorioGenericoDB;
import br.com.rtools.relatorios.db.RelatorioGenericoDBToplink;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Download;
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
import javax.faces.model.SelectItem;
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
public class RelatorioContabilidadesBean implements Serializable {

    List<SelectItem> listaCidades = new ArrayList<SelectItem>();
    List<SelectItem> listaTipoRelatorios = new ArrayList<SelectItem>();
    List<SelectItem> listaTipoEndereco = new ArrayList<SelectItem>();
    List<SelectItem> listaQuantidadeInicio = new ArrayList<SelectItem>();
    List<SelectItem> listaQuantidadeFim = new ArrayList<SelectItem>();
    private int idRelatorios = 0;
    private int idQuantidadeInicio = 0;
    private int idQuantidadeFim = 0;
    private int idCidades = 0;
    private int idTipoEndereco = 0;
    private int quantidadeEmpresas = 0;
    private String radioEmpresas = "todas";
    private String radioCidades = "todas";
    private String radioOrdem = "razao";
    private boolean ocultaEmpresas = false;
    private boolean ocultaCidades = false;

    public void visualizar() {
        String cidades = "";
        int inicio = 0;
        int fim = 0;

        RelatorioGenericoDB db = new RelatorioGenericoDBToplink();
        RelatorioContabilidadesDB dbConta = new RelatorioContabilidadesDBToplink();
        CidadeDB dbCidade = new CidadeDBToplink();
        PessoaEnderecoDB dbPesEnd = new PessoaEnderecoDBToplink();
        Juridica sindicato = new Juridica();
        PessoaEndereco endSindicato = new PessoaEndereco();
        JuridicaDB dbJur = new JuridicaDBToplink();
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();

        Cidade cidade;
        Relatorios relatorios = db.pesquisaRelatorios(Integer.parseInt(getListaTipoRelatorios().get(idRelatorios).getDescription()));
        TipoEndereco tipoEndereco = (TipoEndereco) salvarAcumuladoDB.pesquisaCodigo(Integer.parseInt(getListaTipoEndereco().get(idTipoEndereco).getDescription()), "TipoEndereco");

        // CONTABILIDADES DO RELATORIO -----------------------------------------------------------
        if (radioEmpresas.equals("comEmpresas")) {
            inicio = Integer.parseInt(listaQuantidadeInicio.get(idQuantidadeInicio).getDescription());
            fim = Integer.parseInt(listaQuantidadeFim.get(idQuantidadeFim).getDescription());
            if (inicio > fim) {
                inicio = fim;
            }
        }

        // CIDADE DO RELATORIO -----------------------------------------------------------
        if (radioCidades.equals("especificas")) {
            cidade = dbCidade.pesquisaCodigo(Integer.parseInt(getListaCidades().get(idCidades).getDescription()));
            cidades = Integer.toString(cidade.getId());
        } else if (radioCidades.equals("local")) {
            cidade = dbPesEnd.pesquisaEndPorPessoaTipo(1, 2).getEndereco().getCidade();
            cidades = Integer.toString(cidade.getId());
        } else if (radioCidades.equals("outras")) {
            cidade = dbPesEnd.pesquisaEndPorPessoaTipo(1, 2).getEndereco().getCidade();
            cidades = Integer.toString(cidade.getId());
        }

        sindicato = dbJur.pesquisaCodigo(1);
        endSindicato = dbPesEnd.pesquisaEndPorPessoaTipo(1, 3);
        List list = dbConta.listaRelatorioContabilidades(radioEmpresas, inicio, fim, radioCidades, cidades, radioOrdem, tipoEndereco.getId());
        if (list.isEmpty()) {
            GenericaMensagem.info("Sistema", "Não existem registros para o relatório selecionado");
            return;
        }        
        try {
            FacesContext faces = FacesContext.getCurrentInstance();
            Collection listaEscritorios = new ArrayList<ParametroEscritorios>();
            JasperReport jasper = (JasperReport) JRLoader.loadObject(new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath(relatorios.getJasper())));
            try {
                String dados[] = new String[8];
                for (int i = 0; i < list.size(); i++) {
                    int quantidade = 0;
                    Juridica juridica = (Juridica) salvarAcumuladoDB.pesquisaObjeto(Integer.parseInt(((List) list.get(i)).get(0).toString()), "Juridica");
                    PessoaEndereco pessoaEndereco = (PessoaEndereco) salvarAcumuladoDB.pesquisaObjeto(Integer.parseInt(((List) list.get(i)).get(1).toString()), "PessoaEndereco");
                    quantidade = Integer.parseInt(((List) list.get(i)).get(2).toString());
                    try {
                        dados[0] = pessoaEndereco.getEndereco().getDescricaoEndereco().getDescricao();
                        dados[1] = pessoaEndereco.getEndereco().getLogradouro().getDescricao();
                        dados[2] = pessoaEndereco.getNumero();
                        dados[3] = pessoaEndereco.getComplemento();
                        dados[4] = pessoaEndereco.getEndereco().getBairro().getDescricao();
                        dados[5] = pessoaEndereco.getEndereco().getCep();
                        dados[6] = pessoaEndereco.getEndereco().getCidade().getCidade();
                        dados[7] = pessoaEndereco.getEndereco().getCidade().getUf();
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
                    listaEscritorios.add(new ParametroEscritorios(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
                            sindicato.getPessoa().getNome(),
                            endSindicato.getEndereco().getDescricaoEndereco().getDescricao(),
                            endSindicato.getEndereco().getLogradouro().getDescricao(),
                            endSindicato.getNumero(),
                            endSindicato.getComplemento(),
                            endSindicato.getEndereco().getBairro().getDescricao(),
                            endSindicato.getEndereco().getCep(),
                            endSindicato.getEndereco().getCidade().getCidade(),
                            endSindicato.getEndereco().getCidade().getUf(),
                            sindicato.getPessoa().getTelefone1(),
                            sindicato.getPessoa().getEmail1(),
                            sindicato.getPessoa().getSite(),
                            sindicato.getPessoa().getTipoDocumento().getDescricao(),
                            sindicato.getPessoa().getDocumento(),
                            juridica.getId(),
                            juridica.getPessoa().getNome(),
                            dados[0], // DESCRICAO ENDERECO CONTABIL
                            dados[1], // LOGRADOURO CONTABIL
                            dados[2], // NUMERO CONTABIL
                            dados[3], // COMPLEMENTO CONTABIL
                            dados[4], // BAIRRO CONTABIL
                            dados[5], // CEP CONTABIL
                            dados[6], // CIDADE CONTABIL
                            dados[7], // UF CONTABIL
                            juridica.getPessoa().getTelefone1(),
                            juridica.getPessoa().getEmail1(),
                            quantidade));
                }
                JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(listaEscritorios);
                JasperPrint print = JasperFillManager.fillReport(jasper, null, dtSource);
                byte[] arquivo = JasperExportManager.exportReportToPdf(print);
                String nomeDownload = "relatorio_escritorios_" + DataHoje.horaMinuto().replace(":", "") + ".pdf";
                SalvaArquivos salvaArquivos = new SalvaArquivos(arquivo, nomeDownload, false);
                String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/relatorios");
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

    public List<SelectItem> getListaTipoRelatorios() {
        if (listaTipoRelatorios.isEmpty()) {
            RelatorioGenericoDB db = new RelatorioGenericoDBToplink();
            List<Relatorios> list = (List<Relatorios>) db.pesquisaTipoRelatorio(6);
            for (int i = 0; i < list.size(); i++) {
                listaTipoRelatorios.add(new SelectItem(new Integer(i), list.get(i).getNome(), Integer.toString(list.get(i).getId())));
            }
        }
        return listaTipoRelatorios;
    }

    public List<SelectItem> getListaCidades() {
        if (listaCidades.isEmpty()) {
            RelatorioGenericoDB db = new RelatorioGenericoDBToplink();
            List<Cidade> list = (List<Cidade>) db.pesquisaCidadesRelatorio();
            for (int i = 0; i < list.size(); i++) {
                listaCidades.add(new SelectItem(new Integer(i), list.get(i).getCidade(), Integer.toString(list.get(i).getId())));
            }
        }
        return listaCidades;
    }

    public List<SelectItem> getListaQuantidadeInicio() {
        quantidadeEmpresas();
        if (listaQuantidadeInicio.isEmpty()) {
            for (int i = 0; i < quantidadeEmpresas; i++ ) {
                boolean itemSelecionado = false;
                if (i == 0) {
                    itemSelecionado = true;
                }                
                listaQuantidadeInicio.add(new SelectItem(new Integer(i), Integer.toString(i+1), Integer.toString(i+1), false, false, itemSelecionado));
            }
        }
        return listaQuantidadeInicio;        
    }

    public List<SelectItem> getListaQuantidadeFim() {
        quantidadeEmpresas();
        if (listaQuantidadeFim.isEmpty()) {
            for (int i = 0; i < quantidadeEmpresas; i++ ) {
                boolean itemSelecionado = false;
                if (i+1 == quantidadeEmpresas) {
                    idQuantidadeFim = i;
                }                
                listaQuantidadeFim.add(new SelectItem(new Integer(i), Integer.toString(i+1), Integer.toString(i+1), false, false, itemSelecionado));
            }
        }
        return listaQuantidadeFim;        
    }

    public void quantidadeEmpresas() {
        if (quantidadeEmpresas <= 0 ) {
            RelatorioContabilidadesDB db = new RelatorioContabilidadesDBToplink();
            quantidadeEmpresas = db.quantidadeEmpresas();        
        }
    }

//    public List<SelectItem> getListaQntEmpresas1() {
//        if (qntEmpresas.isEmpty()) {
//            qntEmpresas = new ArrayList<SelectItem>();
//            int i = 0;
//            RelatorioContabilidadesDB db = new RelatorioContabilidadesDBToplink();
//            List contabilidades = db.pesquisaContabilidades();
//            JuridicaDB dbJur = new JuridicaDBToplink();
////            Juridica contabil = new Juridica();
//            int quantidade = 0;
//            boolean tem = false;
//            int ind = 0;
//            while (i < contabilidades.size()) {
//                tem = false;
//                Juridica contabil = ((Juridica) contabilidades.get(i));
//                quantidade = dbJur.quantidadeEmpresas(contabil.getId());
//                if (quantidade > 0) {
//                    if (qntEmpresas.isEmpty()) {
//                        qntEmpresas.add(new SelectItem(new Integer(ind), Integer.toString(quantidade), String.valueOf(new Integer(ind))));
//                        ind++;
//                    } else {
//                        for (int o = 0; o < qntEmpresas.size(); o++) {
//                            if (quantidade == Integer.parseInt(qntEmpresas.get(o).getLabel())) {
//                                tem = true;
//                            }
//                        }
//                        if (!tem) {
//                            qntEmpresas.add(new SelectItem(new Integer(ind), Integer.toString(quantidade), String.valueOf(new Integer(ind))));
//                            ind++;
//                        }
//                    }
//                }
//                i++;
//            }
//            BubbleSort(qntEmpresas);
//        }
//        return qntEmpresas;
//    }
//    public List<SelectItem> getListaQntEmpresas2() {
//        if (qntEmpresas2.isEmpty()) {
//            qntEmpresas2.addAll(qntEmpresas);
//            idEmpFinal = Integer.parseInt(qntEmpresas2.get(qntEmpresas2.size() - 1).getDescription());
//        }
//        return qntEmpresas2;
//    }
    public List<SelectItem> getListaTipoEndereco() {
        if (listaTipoEndereco.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            List<TipoEndereco> list = (List<TipoEndereco>) salvarAcumuladoDB.pesquisaObjeto(new int[]{2, 3, 4, 5}, "TipoEndereco");
            for (int i = 0; i < list.size(); i++) {
                listaTipoEndereco.add(new SelectItem(new Integer(i), list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
            }
        }
        return listaTipoEndereco;
    }

//    public static void BubbleSort(List<SelectItem> dados) {
//        boolean trocou;
//        int limite = dados.size() - 1;
//        String swap1 = null;
//        String swap2 = null;
//        int i = 0;
//        do {
//            trocou = false;
//            i = 0;
//            while (i < limite) {
//                if ((Integer.parseInt(dados.get(i).getLabel())) > (Integer.parseInt(dados.get(i + 1).getLabel()))) {
//                    swap1 = dados.get(i).getLabel();
//                    swap2 = dados.get(i + 1).getLabel();
//                    dados.get(i).setLabel(swap2);
//                    dados.get(i + 1).setLabel(swap1);
//                    trocou = true;
//                }
//                i++;
//            }
//            limite--;
//        } while (trocou);
//    }

    public int getIdRelatorios() {
        return idRelatorios;
    }

    public void setIdRelatorios(int idRelatorios) {
        this.idRelatorios = idRelatorios;
    }

    public String getRadioEmpresas() {
        return radioEmpresas;
    }

    public void setRadioEmpresas(String radioEmpresas) {
        this.radioEmpresas = radioEmpresas;
    }

    public int getIdCidades() {
        return idCidades;
    }

    public void setIdCidades(int idCidades) {
        this.idCidades = idCidades;
    }

    public String getRadioCidades() {
        return radioCidades;
    }

    public void setRadioCidades(String radioCidades) {
        this.radioCidades = radioCidades;
    }

    public String getRadioOrdem() {
        return radioOrdem;
    }

    public void setRadioOrdem(String radioOrdem) {
        this.radioOrdem = radioOrdem;
    }

    public int getIdTipoEndereco() {
        return idTipoEndereco;
    }

    public void setIdTipoEndereco(int idTipoEndereco) {
        this.idTipoEndereco = idTipoEndereco;
    }

    public boolean isOcultaEmpresas() {
        ocultaEmpresas = radioEmpresas.equals("comEmpresas");
        return ocultaEmpresas;
    }

    public void setOcultaEmpresas(boolean ocultaEmpresas) {
        this.ocultaEmpresas = ocultaEmpresas;
    }

    public boolean isOcultaCidades() {
        ocultaCidades = radioCidades.equals("especificas");
        return ocultaCidades;
    }

    public void setOcultaCidades(boolean ocultaCidades) {
        this.ocultaCidades = ocultaCidades;
    }

    public int getIdQuantidadeInicio() {
        if (idQuantidadeFim < idQuantidadeInicio) {
            idQuantidadeInicio = idQuantidadeFim;
        }        
        return idQuantidadeInicio;
    }

    public void setIdQuantidadeInicio(int idQuantidadeInicio) {
        this.idQuantidadeInicio = idQuantidadeInicio;
    }

    public int getIdQuantidadeFim() {
        if (idQuantidadeFim < idQuantidadeInicio) {
            idQuantidadeInicio = idQuantidadeFim;
        }
        return idQuantidadeFim;
    }

    public void setIdQuantidadeFim(int idQuantidadeFim) {
        this.idQuantidadeFim = idQuantidadeFim;
    }
}
