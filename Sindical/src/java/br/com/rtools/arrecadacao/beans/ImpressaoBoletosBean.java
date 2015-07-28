package br.com.rtools.arrecadacao.beans;

import br.com.rtools.arrecadacao.CnaeConvencao;
import br.com.rtools.arrecadacao.Convencao;
import br.com.rtools.arrecadacao.GrupoCidade;
import br.com.rtools.arrecadacao.db.ConvencaoCidadeDB;
import br.com.rtools.arrecadacao.db.ConvencaoCidadeDBToplink;
import br.com.rtools.arrecadacao.db.ConvencaoDB;
import br.com.rtools.arrecadacao.db.ConvencaoDBToplink;
import br.com.rtools.financeiro.Impressao;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.ServicoContaCobranca;
import br.com.rtools.financeiro.db.MovimentoDB;
import br.com.rtools.financeiro.db.MovimentoDBToplink;
import br.com.rtools.financeiro.db.ServicoContaCobrancaDB;
import br.com.rtools.financeiro.db.ServicoContaCobrancaDBToplink;
import br.com.rtools.impressao.ParametroContribuintes;
import br.com.rtools.movimento.ImprimirBoleto;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.TipoEndereco;
import br.com.rtools.pessoa.db.JuridicaDB;
import br.com.rtools.pessoa.db.JuridicaDBToplink;
import br.com.rtools.pessoa.db.PessoaEnderecoDB;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.pessoa.db.TipoEnderecoDB;
import br.com.rtools.pessoa.db.TipoEnderecoDBToplink;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.relatorios.db.RelatorioContribuintesDB;
import br.com.rtools.relatorios.db.RelatorioContribuintesDBToplink;
import br.com.rtools.relatorios.dao.RelatorioDao;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.seguranca.utilitarios.SegurancaUtilitariosBean;
import br.com.rtools.sistema.Email;
import br.com.rtools.sistema.EmailPessoa;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.Download;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Linha;
import br.com.rtools.utilitarios.Mail;
import br.com.rtools.utilitarios.SalvaArquivos;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@ManagedBean
@SessionScoped
public class ImpressaoBoletosBean implements Serializable {

    private String escritorio = "null";
    private List<String> listaData = new ArrayList<String>();
    private List<String> listaDataSelecionada = new ArrayList<String>();
    private List<Linha> listaMovGrid = new ArrayList<Linha>();
    private List<Linha> listaMovGridSelecionada = new ArrayList<Linha>();
    List<Movimento> listaAux = new ArrayList<Movimento>();
    private Juridica contabilidade = new Juridica();
    private int boletosSel;
    private int idCombo = 0;
    private int quantidade = 0;
    private int fInicio = 0;
    private int fFim = 0;
    int idData = -2;
    private long totalBoletos = 0;
    private long totalEmpresas = 0;
    private long totalEscritorios = 0;
    private boolean imprimeVerso = true;
    private String msgImpressao = "";
    //private List<DataObject> listaConvencao = new ArrayList();
    private List<Convencao> listaConvencao = new ArrayList();
    private List<Convencao> listaConvencaoSelecionada = new ArrayList();
    //private List<DataObject> listaGrupoCidade = new ArrayList();
    private List<GrupoCidade> listaGrupoCidade = new ArrayList();
    private List<GrupoCidade> listaGrupoSelecionada = new ArrayList();
    private String todasContas = "true";
    private String movimentosSemMensagem = null;
    private int quantidadeEmpresas = 0;
    private String regraEscritorios = "all";
    private String cbEmail = "todos";
    private boolean chkTodosVencimentos = false;

    public String removerContabilidade() {
        contabilidade = new Juridica();
        return "impressaoBoletos";
    }

    public String getEscritorio() {
        return escritorio;
    }

    public void setEscritorio(String escritorio) {
        this.escritorio = escritorio;
    }

    public int getIdCombo() {
        return idCombo;
    }

    public void setIdCombo(int idCombo) {
        this.idCombo = idCombo;
    }

    public boolean getDesabilitarContas() {
        if (this.todasContas.equals("false")) {
            return false;
        } else {
            return true;
        }
    }

    public int getQuantidade() {
        if (!(getListaMovGrid().isEmpty())) {
            if (((quantidade <= listaMovGrid.size()) && (fInicio <= listaMovGrid.size()) && (fFim <= listaMovGrid.size()))
                    && ((fInicio != 0) && (fFim != 0))) {
                quantidade = (fFim - fInicio) + 1;
            }
        }
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getfInicio() {
        return fInicio;
    }

    public void setfInicio(int fInicio) {
        this.fInicio = fInicio;
    }

    public int getfFim() {
        return fFim;
    }

    public void setfFim(int fFim) {
        this.fFim = fFim;
    }

    public int getBoletosSel() {
        return boletosSel;
    }

    public void setBoletosSel(int boletosSel) {
        this.boletosSel = boletosSel;
    }

    public List<Linha> getListaMovGrid() {
        return listaMovGrid;
    }

    public synchronized void carregarGrid() {
        try {
            listaMovGrid.clear();
            listaMovGridSelecionada.clear();
            ServicoContaCobrancaDB servDB = new ServicoContaCobrancaDBToplink();
            MovimentoDB movDB = new MovimentoDBToplink();
            List<Linha> listaSwap = new ArrayList<Linha>();
            ServicoContaCobranca contaCobranca;
            Linha linha = new Linha();

            List<Integer> listG = new ArrayList();
            List<Integer> listC = new ArrayList();
            Object[] result = new Object[]{new ArrayList(), new Integer(0)};

            totalBoletos = 0;
            totalEmpresas = 0;
            totalEscritorios = 0;

            try {
                contaCobranca = servDB.pesquisaCodigo(Integer.parseInt(((SelectItem) getListaServicoCobranca().get(idCombo)).getDescription()));
            } catch (Exception e) {
                contaCobranca = new ServicoContaCobranca();
            }


            if ((!listaGrupoSelecionada.isEmpty()) && (!listaConvencaoSelecionada.isEmpty())) {
                for (int i = 0; i < listaGrupoSelecionada.size(); i++) {
                    listG.add(listaGrupoSelecionada.get(i).getId());
                }
                
                for (int i = 0; i < listaConvencaoSelecionada.size(); i++) {
                    listC.add(listaConvencaoSelecionada.get(i).getId());
                }
            }

            if (!(listaDataSelecionada.isEmpty())) {
                List ids = new ArrayList<String>();

                for (int i = 0; i < listaDataSelecionada.size(); i++) {
                    ids.add(listaDataSelecionada.get(i));
                }
                Vector vetorAux = new Vector();

                if (this.regraEscritorios.equals("all")) {
                    this.quantidadeEmpresas = 0;
                } else if (this.quantidadeEmpresas == 0) {
                    this.quantidadeEmpresas = 1;
                }

                int id_esc = 0;

                if (contabilidade.getId() != -1) {
                    id_esc = contabilidade.getId();
                }

                result = movDB.listaImpressaoGeral(
                        contaCobranca.getServicos().getId(),
                        contaCobranca.getTipoServico().getId(),
                        contaCobranca.getContaCobranca().getId(),
                        escritorio,
                        ids,
                        listC,
                        listG,
                        this.todasContas,
                        cbEmail,
                        id_esc);

                Vector v = (Vector) result[0];

                int auxEsc = 0;
                int auxEmpresa = 0;
                int ate = 0, apartir = 0;
                for (int i = 0; i < v.size(); i++) {
                    vetorAux = (Vector) v.get(i);
                    if (this.regraEscritorios.equals("ate")) {
                        if (((Long) vetorAux.get(11)) <= this.quantidadeEmpresas) {
                            linha = addGrid(vetorAux, ate);
                            if (auxEsc != ((Integer) vetorAux.get(9))) {
                                totalEscritorios++;
                            }
                            auxEsc = ((Integer) vetorAux.get(9));
                            if (auxEmpresa != ((Integer) vetorAux.get(10))) {
                                totalEmpresas++;
                            }
                            auxEmpresa = ((Integer) vetorAux.get(10));
                            totalBoletos++;
                            listaSwap.add(linha);
                            ate++;
                        }
                    } else if (this.regraEscritorios.equals("apartir")) {
                        if (((Long) vetorAux.get(11)) >= this.quantidadeEmpresas) {
                            linha = addGrid(vetorAux, apartir);
                            if (auxEsc != ((Integer) vetorAux.get(9))) {
                                totalEscritorios++;
                            }
                            auxEsc = ((Integer) vetorAux.get(9));
                            if (auxEmpresa != ((Integer) vetorAux.get(10))) {
                                totalEmpresas++;
                            }
                            auxEmpresa = ((Integer) vetorAux.get(10));
                            totalBoletos++;
                            listaSwap.add(linha);
                            apartir++;
                        }
                    } else {
                        linha = addGrid((Vector) v.get(i), i);
                        if (auxEsc != ((Integer) vetorAux.get(9))) {
                            totalEscritorios++;
                        }
                        auxEsc = ((Integer) vetorAux.get(9));
                        if (auxEmpresa != ((Integer) vetorAux.get(10))) {
                            totalEmpresas++;
                        }
                        auxEmpresa = ((Integer) vetorAux.get(10));
                        totalBoletos++;
                        listaSwap.add(linha);
                    }
                }
            }
            listaMovGrid = listaSwap;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Linha addGrid(Vector vetorAux, int i) {
        List lista = new ArrayList();
        Linha linha = new Linha();
        lista.add(new Boolean(false)); //marcar
        lista.add(new Integer(i + 1)); //indice
        lista.add(vetorAux.get(0));    //boleto
        lista.add(vetorAux.get(1));    //razao
        lista.add(vetorAux.get(2));    //cnpj
        lista.add(vetorAux.get(3));    //escritorio
        lista.add(vetorAux.get(4));    //servico
        lista.add(vetorAux.get(5));    //tipo
        lista.add(DataHoje.converteData((Date) vetorAux.get(6))); //vencimento
        lista.add(vetorAux.get(7));    //referencia
        lista.add(vetorAux.get(8));    //id
        lista.add(vetorAux.get(9));    //id_contabilidade ( pessoa )
        lista.add(vetorAux.get(10));   //id_juridica

        linha = Linha.preencherLinha(
                linha,
                lista,
                0);

        return linha;
    }

    public void setListaMovGrid(List<Linha> listaMovGrid) {
        this.listaMovGrid = listaMovGrid;
    }

    public void filtrar() {
        carregarGrid();
    }

    public synchronized void refreshForm() {
    }

    public void alterarTodasDatas() {
        listaMovGrid.clear();
        listaMovGridSelecionada.clear();
        if (this.todasContas.equals("true")) {
            idData = -2;
        } else {
            idData = -1;
        }
    }

    public boolean getDesabilitaComboQuantidadeEmpresas() {
        if (regraEscritorios.equals("all")) {
            return true;
        } else {
            return false;
        }
    }

    public synchronized List<String> getListaData() {
        try {
            ServicoContaCobrancaDB servDB = new ServicoContaCobrancaDBToplink();
            ServicoContaCobranca contaCobranca;
            try {
                contaCobranca = servDB.pesquisaCodigo(Integer.parseInt(((SelectItem) getListaServicoCobranca().get(idCombo)).getDescription()));
            } catch (Exception e) {
                contaCobranca = new ServicoContaCobranca();
            }
            MovimentoDB db = new MovimentoDBToplink();
            List lista = new ArrayList();
            int i = 0;
            if (this.todasContas.equals("false")) {
                if (contaCobranca.getId() != idData) {
                    listaData.clear();
                    idData = contaCobranca.getId();
                    lista = db.datasMovimento(
                            contaCobranca.getServicos().getId(),
                            contaCobranca.getTipoServico().getId(),
                            contaCobranca.getContaCobranca().getId());
                }

            } else {
                if (idData == -2) {
                    idData = -1;
                    listaData.clear();
                    lista = db.datasMovimento();
                }
            }

            if (lista == null) {
                lista = new ArrayList<DataObject>();
            }
            while (i < lista.size()) {
                listaData.add(DataHoje.converteData((Date) lista.get(i)));
                i++;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return listaData;

    }
    
    public void limparSelecao(){
        listaMovGridSelecionada.clear();
        quantidade = 0;
        fInicio = 0;
        fFim = 0;
        
    }
    
    public void limparTotais(){
        listaMovGrid.clear();
        listaMovGridSelecionada.clear();
        totalBoletos = 0;
        totalEmpresas = 0;
        totalEscritorios = 0;
    }
    
    public synchronized void controleMovimentos() {
        int i = 0;
        listaMovGridSelecionada.clear();
        if ((quantidade != 0) && fInicio == 0 && fFim == 0) {//CASO 1 SOMENTE POR QUANTIDADE
            if (quantidade > listaMovGrid.size()) {
                quantidade = listaMovGrid.size();
            }
            while (i < quantidade) {
                //listaMovGrid.get(i).setValor(new Boolean(true));
                listaMovGridSelecionada.add(listaMovGrid.get(i));
                i++;
            }
        } else if (quantidade == 0 && fInicio != 0 && fFim == 0) {//CASO 2 SOMENTE POR INICIO
            if (fInicio <= listaMovGrid.size()) {
                i = fInicio - 1;
                while (i < listaMovGrid.size()) {
                    quantidade++;
                    //listaMovGrid.get(i).setValor(new Boolean(true));
                    listaMovGridSelecionada.add(listaMovGrid.get(i));
                    i++;
                }
            }
        } else if (quantidade != 0 && fInicio != 0 && fFim == 0) {//CASO 3 SOMENTE POR INICIO E QUANTIDADE
            if ((quantidade <= listaMovGrid.size()) && (fInicio <= listaMovGrid.size())) {
                int o = 0;
                i = fInicio - 1;
                while ((o < quantidade) && (i < listaMovGrid.size())) {
                    //listaMovGrid.get(i).setValor(new Boolean(true));
                    listaMovGridSelecionada.add(listaMovGrid.get(i));
                    i++;
                    o++;
                }
            }
        } else if (quantidade == 0 && fInicio != 0 && fFim != 0) {//CASO 4 SOMENTE POR INICIO E FIM
            if ((fInicio <= listaMovGrid.size()) && (fFim <= listaMovGrid.size())) {
                i = fInicio - 1;
                while (i < fFim) {
                    quantidade++;
                    //listaMovGrid.get(i).setValor(new Boolean(true));
                    listaMovGridSelecionada.add(listaMovGrid.get(i));
                    i++;
                }
            }
        } else if (quantidade == 0 && fInicio == 0 && fFim != 0) {//CASO 5 SOMENTE POR FIM
            if (fFim <= listaMovGrid.size()) {
                while (i < fFim) {
                    quantidade++;
                    //listaMovGrid.get(i).setValor(new Boolean(true));
                    listaMovGridSelecionada.add(listaMovGrid.get(i));
                    i++;
                }
            }
        } else if (quantidade != 0 && fInicio == 0 && fFim != 0) {//CASO 6 SOMENTE POR FIM E QUANTIDADE
            if ((quantidade <= listaMovGrid.size()) && (fFim <= listaMovGrid.size())) {
                if ((quantidade - fFim) < 0) {
                    i = fFim - quantidade;
                } else {
                    i = quantidade - fFim;
                }
                quantidade = 0;
                while (i < fFim) {
                    quantidade++;
                    //listaMovGrid.get(i).setValor(new Boolean(true));
                    listaMovGridSelecionada.add(listaMovGrid.get(i));
                    i++;
                }
            }
        } else if (quantidade != 0 && fInicio != 0 && fFim != 0) {//CASO 7 POR QUANTIDADE INICIO E FIM
            if ((quantidade <= listaMovGrid.size()) && (fInicio <= listaMovGrid.size()) && (fFim <= listaMovGrid.size())) {
                i = fInicio - 1;
                if (quantidade > 1) {
                    quantidade = (fFim - fInicio) + 1;
                }
                quantidade = 0;
                while (i < fFim) {
                    quantidade++;
                    //listaMovGrid.get(i).setValor(new Boolean(true));
                    listaMovGridSelecionada.add(listaMovGrid.get(i));
                    i++;
                }
            }
        }
    }


    public List<SelectItem> getListaServicoCobranca() {
        List<SelectItem> servicoCobranca = new ArrayList<SelectItem>();
        int i = 0;
        ServicoContaCobrancaDB servDB = new ServicoContaCobrancaDBToplink();
        List<ServicoContaCobranca> select = servDB.pesquisaTodosTipoUm();
        if (select == null) {
            select = new ArrayList<ServicoContaCobranca>();
        }
        while (i < select.size()) {
            servicoCobranca.add(
                    new SelectItem(
                    i,
                    select.get(i).getServicos().getDescricao() + " - "
                    + select.get(i).getTipoServico().getDescricao() + " - "
                    + select.get(i).getContaCobranca().getCodCedente(), //+" "+
                    //select.get(i).getContaCobranca().getCedente(),
                    Integer.toString(select.get(i).getId())));
            i++;
        }
        return servicoCobranca;
    }

    public String criarArquivoBanco() {
        List movs = new ArrayList();
        MovimentoDB db = new MovimentoDBToplink();
        try {
            ArquivoBancoBean arquivoBanco = new ArquivoBancoBean();
            Movimento mov = new Movimento();
            if (todasContas.equals("true")) {
                msgImpressao = "Selecione específicas para gerar o Arquivo!";
                return "impressaoBoletos";
            }

            for (int o = 0; o < listaMovGrid.size(); o++) {
                if ((Boolean) listaMovGrid.get(o).getValor()) {
                    mov = db.pesquisaCodigo(
                            (Integer) listaMovGrid.get(o).getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getValor());
                    movs.add(mov);
                }
            }
            if (!movs.isEmpty()) {
                if (arquivoBanco.criarArquivoTXT(movs)) {
                    msgImpressao = "Arquivo gerado com sucesso!";
                } else {
                    msgImpressao = "Erro ao processar arquivos!";
                }
            } else {
                msgImpressao = "Lista vazia!";
            }
        } catch (Exception e) {
            System.out.println("Não foi possivel criar arquivo de envio! " + e);
        }
        return "impressaoBoletos";
    }

    public String baixarArquivosGerados() {
        ArquivoBancoBean arquivoBanco = new ArquivoBancoBean();
        arquivoBanco.baixarArquivosGerados();

        return null;
    }

    public void limparIn() {
        ArquivoBancoBean arquivoBanco = new ArquivoBancoBean();
        arquivoBanco.limparDiretorio("");
    }

    public String imprimirBoleto() {
        MovimentoDB db = new MovimentoDBToplink();
        List<Movimento> lista = new ArrayList<Movimento>();
        List<Float> listaValores = new ArrayList<Float>();
        List<String> listaVencimentos = new ArrayList<String>();
        Movimento mov = new Movimento();
        
        SegurancaUtilitariosBean su = new SegurancaUtilitariosBean();
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        
        sv.abrirTransacao();
        for (int i = 0; i < listaMovGridSelecionada.size(); i++) {
            mov = db.pesquisaCodigo(
                    (Integer) listaMovGridSelecionada.get(i).getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getValor());
            lista.add(mov);
            listaValores.add(mov.getValor());
            listaVencimentos.add(mov.getVencimento());
            
            Impressao impressao = new Impressao();
            
            impressao.setUsuario(su.getSessaoUsuario());
            impressao.setDtVencimento(mov.getDtVencimento());
            impressao.setMovimento(mov);

            if (!sv.inserirObjeto(impressao)){
                sv.desfazerTransacao();
                GenericaMensagem.error("Erro", "Não foi possível SALVAR impressão!");
                return null;
            }            
        }
        sv.comitarTransacao();
        
        ImprimirBoleto imp = new ImprimirBoleto();
        imp.imprimirBoleto(lista, listaValores, listaVencimentos, imprimeVerso);
        //imp.visualizar(null);
        imp.baixarArquivo();
        return null;
    }

    public String getPainelMenssagem() {
        if (movimentosSemMensagem == null) {
            return "";
        } else {
            return "boletoSemMensagem";
        }
    }

    public String etiquetaEmpresa() {
        String condicao = "";
        String escritorios = "";
        String cidades = "";
        String pCidade = "";
        String ordem = "";
        String cnaes = "";

        RelatorioDao db = new RelatorioDao();
        RelatorioContribuintesDB dbContri = new RelatorioContribuintesDBToplink();
        JuridicaDB dbJur = new JuridicaDBToplink();
        PessoaEnderecoDB dbPesEnd = new PessoaEnderecoDBToplink();
        ConvencaoDB dbConv = new ConvencaoDBToplink();
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();

        Relatorios relatorios = new Relatorios();
        Juridica sindicato = new Juridica();
        PessoaEndereco endSindicato = new PessoaEndereco();
        PessoaEndereco endEmpresa = new PessoaEndereco();
        PessoaEndereco endEscritorio = new PessoaEndereco();
        List listaCnaes = new ArrayList();
        TipoEndereco tipoEndereco = new TipoEndereco();
        relatorios = db.pesquisaRelatoriosPorJasper("/Relatorios/ETCONTRIBUINTES6181.jasper");
        tipoEndereco = (TipoEndereco) salvarAcumuladoDB.pesquisaCodigo(2, "TipoEndereco");
        // CONDICAO DO RELATORIO -----------------------------------------------------------
        condicao = "ativos";

        // ESCRITORIO DO RELATORIO -----------------------------------------------------------
        escritorios = "todos";

        // CIDADE DO RELATORIO -----------------------------------------------------------
        pCidade = "todas";

        // ORDEM DO RELATORIO -----------------------------------------------------------
        ordem = "razao";

        // CNAES DO RELATORIO -----------------------------------------------------------
        List<Convencao> resultConvencoes = dbConv.pesquisaTodos();
        String ids = "", idsJuridica = "";
        for (int i = 0; i < resultConvencoes.size(); i++) {
            if (ids.length() > 0 && i != resultConvencoes.size()) {
                ids = ids + ",";
            }
            ids = ids + String.valueOf(resultConvencoes.get(i).getId());
        }
        List<CnaeConvencao> resultCnaeConvencao = new ArrayList();
        if (!ids.isEmpty()) {
            resultCnaeConvencao = dbContri.pesquisarCnaeConvencaoPorConvencao(ids);
        }

        if (!resultConvencoes.isEmpty()) {
            for (int i = 0; i < resultCnaeConvencao.size(); i++) {
                listaCnaes.add(resultCnaeConvencao.get(i));
            }
            for (int i = 0; i < listaCnaes.size(); i++) {
                if (cnaes.length() > 0 && i != resultCnaeConvencao.size()) {
                    cnaes = cnaes + ",";
                }
                cnaes = cnaes + Integer.toString(((CnaeConvencao) listaCnaes.get(i)).getCnae().getId());
            }
        } else {
            cnaes = "";
        }

        for (int i = 0; i < listaMovGridSelecionada.size(); i++) {
            if (idsJuridica.length() > 0 && i != listaMovGridSelecionada.size()) {
                idsJuridica = idsJuridica + ",";
            }
            idsJuridica = idsJuridica + ((Integer) listaMovGridSelecionada.get(i).getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getValor());
        }

        sindicato = dbJur.pesquisaCodigo(1);
        endSindicato = dbPesEnd.pesquisaEndPorPessoaTipo(1, 3);
        List<Juridica> result = new ArrayList();
        if (!resultCnaeConvencao.isEmpty() && !listaCnaes.isEmpty() && !idsJuridica.isEmpty()) {
            result = dbContri.listaRelatorioContribuintesPorJuridica(condicao, escritorios, pCidade, cidades, ordem, cnaes, tipoEndereco.getId(), idsJuridica);
        }

        try {
            FacesContext faces = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
            byte[] arquivo = new byte[0];
            JasperReport jasper = null;
            Collection listaContrs = new ArrayList<ParametroContribuintes>();
            
            File fl = new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath(relatorios.getJasper()));
            jasper = (JasperReport) JRLoader.loadObject(fl);
            try {
                String dados[] = new String[21];
                for (int i = 0; i < result.size(); i++) {
                    endEmpresa = dbPesEnd.pesquisaEndPorPessoaTipo(result.get(i).getPessoa().getId(), tipoEndereco.getId());
                    try {
                        dados[0] = endEmpresa.getEndereco().getDescricaoEndereco().getDescricao();
                        dados[1] = endEmpresa.getEndereco().getLogradouro().getDescricao();
                        dados[2] = endEmpresa.getEndereco().getBairro().getDescricao();
                        dados[3] = endEmpresa.getEndereco().getCep();
                        dados[4] = endEmpresa.getEndereco().getCidade().getCidade();
                        dados[5] = endEmpresa.getEndereco().getCidade().getUf();
                    } catch (Exception e) {
                        dados[0] = "";
                        dados[1] = "";
                        dados[2] = "";
                        dados[3] = "";
                        dados[4] = "";
                        dados[5] = "";
                    }

                    try {
                        dados[6] = String.valueOf(result.get(i).getCnae().getId());
                        dados[7] = result.get(i).getCnae().getNumero();
                        dados[8] = result.get(i).getCnae().getCnae();
                    } catch (Exception e) {
                        dados[6] = "0";
                        dados[7] = "";
                        dados[8] = "";
                    }

                    try {
                        dados[9] = String.valueOf(result.get(i).getContabilidade().getId());
                        dados[10] = result.get(i).getContabilidade().getPessoa().getNome();
                        dados[11] = result.get(i).getContabilidade().getPessoa().getTelefone1();
                        dados[12] = result.get(i).getContabilidade().getPessoa().getEmail1();
                    } catch (Exception e) {
                        dados[9] = "0";
                        dados[10] = "";
                        dados[11] = "";
                        dados[12] = "";
                    }

                    try {
                        endEscritorio = dbPesEnd.pesquisaEndPorPessoaTipo(result.get(i).getContabilidade().getPessoa().getId(), 2);
                        dados[13] = endEscritorio.getEndereco().getDescricaoEndereco().getDescricao();
                        dados[14] = endEscritorio.getEndereco().getLogradouro().getDescricao();
                        dados[15] = endEscritorio.getNumero();
                        dados[16] = endEscritorio.getComplemento();
                        dados[17] = endEscritorio.getEndereco().getBairro().getDescricao();
                        dados[18] = endEscritorio.getEndereco().getCep();
                        dados[19] = endEscritorio.getEndereco().getCidade().getCidade();
                        dados[20] = endEscritorio.getEndereco().getCidade().getUf();
                    } catch (Exception e) {
                        dados[13] = "";
                        dados[14] = "";
                        dados[15] = "";
                        dados[16] = "";
                        dados[17] = "";
                        dados[18] = "";
                        dados[19] = "";
                        dados[20] = "";
                    }
                    listaContrs.add(new ParametroContribuintes(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Imagens/LogoCliente.png"),
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
                            result.get(i).getId(),
                            result.get(i).getPessoa().getNome(),
                            dados[0], // DESCRICAO ENDERECO
                            dados[1], // LOGRADOURO
                            endEmpresa.getNumero(),
                            endEmpresa.getComplemento(),
                            dados[2], // BAIRRO
                            dados[3], // CEP
                            dados[4], // CIDADE
                            dados[5], // UF
                            result.get(i).getPessoa().getTelefone1(),
                            result.get(i).getPessoa().getEmail1(),
                            result.get(i).getPessoa().getTipoDocumento().getDescricao(),
                            result.get(i).getPessoa().getDocumento(),
                            Integer.parseInt(dados[6]), // ID CNAE
                            dados[7], // NUMERO CNAE
                            dados[8], // DESCRICAO CNAE
                            Integer.parseInt(dados[9]), // ID CONTABILIDADE
                            dados[10], // NOME CONTABILIDADE
                            dados[13], // DESCRICAO ENDERECO CONTABILIDADE
                            dados[14], // LOGRADOURO CONTABILIDADE
                            dados[15], // NUMERO CONTABILIDADE
                            dados[16], // COMPLEMENTO CONTABILIDADE
                            dados[17], // BAIRRO CONTABILIDADE
                            dados[18], // CEP CONTABILIDADE
                            dados[19], // CIDADE CONTABILIDADE
                            dados[20], // UF CONTABILIDADE
                            dados[11], // TELEFONE CONTABILIDADE
                            dados[12] // EMAIL CONTABILIDADE
                            ));
                    endEmpresa = new PessoaEndereco();
                    endEscritorio = new PessoaEndereco();
                }
                JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(listaContrs);
                JasperPrint print = JasperFillManager.fillReport(
                        jasper,
                        null,
                        dtSource);
                arquivo = JasperExportManager.exportReportToPdf(print);

                String nomeDownload = "etiquetas_empresa_" + DataHoje.horaMinuto().replace(":", "") + ".pdf";

                SalvaArquivos sa = new SalvaArquivos(arquivo, nomeDownload, false);
                String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/etiquetas");

                sa.salvaNaPasta(pathPasta);

                Download download = new Download(nomeDownload,
                        pathPasta,
                        "application/pdf",
                        FacesContext.getCurrentInstance());
                download.baixar();
            } catch (Exception erro) {
                System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
                return null;
            }
        } catch (Exception erro) {
            System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
            return null;
        }
        return null;
    }

    public String etiquetaEscritorio() {
        String condicao = "";
        String escritorios = "";
        String cidades = "";
        String pCidade = "";
        String ordem = "";
        String cnaes = "";

        RelatorioDao db = new RelatorioDao();
        RelatorioContribuintesDB dbContri = new RelatorioContribuintesDBToplink();
        JuridicaDB dbJur = new JuridicaDBToplink();
        PessoaEnderecoDB dbPesEnd = new PessoaEnderecoDBToplink();
        TipoEnderecoDB dbTipoEnd = new TipoEnderecoDBToplink();
        ConvencaoDB dbConv = new ConvencaoDBToplink();
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();

        Relatorios relatorios = new Relatorios();
        Juridica sindicato = new Juridica();
        PessoaEndereco endSindicato = new PessoaEndereco();
        PessoaEndereco endEmpresa = new PessoaEndereco();
        PessoaEndereco endEscritorio = new PessoaEndereco();
        List listaCnaes = new ArrayList();
        TipoEndereco tipoEndereco = new TipoEndereco();
        relatorios = db.pesquisaRelatoriosPorJasper("/Relatorios/ETESCRITORIO6181.jasper");
        tipoEndereco = (TipoEndereco) salvarAcumuladoDB.pesquisaCodigo(2, "TipoEndereco");
        // CONDICAO DO RELATORIO -----------------------------------------------------------
        condicao = "ativos";

        // ESCRITORIO DO RELATORIO -----------------------------------------------------------
        escritorios = "todos";

        // CIDADE DO RELATORIO -----------------------------------------------------------
        pCidade = "todas";

        // ORDEM DO RELATORIO -----------------------------------------------------------
        ordem = "escritorio";

        // CNAES DO RELATORIO -----------------------------------------------------------
        List<Convencao> resultConvencoes = dbConv.pesquisaTodos();
        String ids = "", idsJuridica = "";
        for (int i = 0; i < resultConvencoes.size(); i++) {
            if (ids.length() > 0 && i != resultConvencoes.size()) {
                ids = ids + ",";
            }
            ids = ids + String.valueOf(resultConvencoes.get(i).getId());
        }
        List<CnaeConvencao> resultCnaeConvencao = new ArrayList();
        if (!ids.isEmpty()) {
            resultCnaeConvencao = dbContri.pesquisarCnaeConvencaoPorConvencao(ids);
        }

        if (!resultConvencoes.isEmpty()) {
            for (int i = 0; i < resultCnaeConvencao.size(); i++) {
                listaCnaes.add(resultCnaeConvencao.get(i));
            }
            for (int i = 0; i < listaCnaes.size(); i++) {
                if (cnaes.length() > 0 && i != resultCnaeConvencao.size()) {
                    cnaes = cnaes + ",";
                }
                cnaes = cnaes + Integer.toString(((CnaeConvencao) listaCnaes.get(i)).getCnae().getId());
            }
        } else {
            cnaes = "";
        }

        int idContabil1 = 0, idContabil2 = 0;
        boolean um = true;
        for (int i = 0; i < listaMovGridSelecionada.size(); i++) {
            if (um) {
                if (idsJuridica.length() > 0 && i != listaMovGridSelecionada.size()) {
                    idsJuridica = idsJuridica + ",";
                }
                idsJuridica = idsJuridica + ((Integer) listaMovGridSelecionada.get(i).getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getValor());
                um = false;
            } else {
                idContabil1 = ((Integer) listaMovGridSelecionada.get(i - 1).getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getValor());
                idContabil2 = ((Integer) listaMovGridSelecionada.get(i).getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getValor());
                if (idContabil1 != idContabil2) {
                    if (idsJuridica.length() > 0 && i != listaMovGridSelecionada.size()) {
                        idsJuridica = idsJuridica + ",";
                    }
                    idsJuridica = idsJuridica + ((Integer) listaMovGridSelecionada.get(i).getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getValor());
                }
            }
        }


        sindicato = dbJur.pesquisaCodigo(1);
        endSindicato = dbPesEnd.pesquisaEndPorPessoaTipo(1, 3);
        List<Juridica> result = new ArrayList();
        if (!resultCnaeConvencao.isEmpty() && !listaCnaes.isEmpty() && !idsJuridica.isEmpty()) {
            result = dbContri.listaRelatorioContribuintesPorJuridica(condicao, escritorios, pCidade, cidades, ordem, cnaes, tipoEndereco.getId(), idsJuridica);
        }

        try {
            FacesContext faces = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
            byte[] arquivo = new byte[0];
            JasperReport jasper = null;
            Collection listaContrs = new ArrayList<ParametroContribuintes>();
            
            File fl = new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath(relatorios.getJasper()));
            jasper = (JasperReport) JRLoader.loadObject(fl);
            try {
                String dados[] = new String[21];
                for (int i = 0; i < result.size(); i++) {
                    endEmpresa = dbPesEnd.pesquisaEndPorPessoaTipo(result.get(i).getPessoa().getId(), tipoEndereco.getId());
                    try {
                        dados[0] = endEmpresa.getEndereco().getDescricaoEndereco().getDescricao();
                        dados[1] = endEmpresa.getEndereco().getLogradouro().getDescricao();
                        dados[2] = endEmpresa.getEndereco().getBairro().getDescricao();
                        dados[3] = endEmpresa.getEndereco().getCep();
                        dados[4] = endEmpresa.getEndereco().getCidade().getCidade();
                        dados[5] = endEmpresa.getEndereco().getCidade().getUf();
                    } catch (Exception e) {
                        dados[0] = "";
                        dados[1] = "";
                        dados[2] = "";
                        dados[3] = "";
                        dados[4] = "";
                        dados[5] = "";
                    }

                    try {
                        dados[6] = String.valueOf(result.get(i).getCnae().getId());
                        dados[7] = result.get(i).getCnae().getNumero();
                        dados[8] = result.get(i).getCnae().getCnae();
                    } catch (Exception e) {
                        dados[6] = "0";
                        dados[7] = "";
                        dados[8] = "";
                    }

                    try {
                        dados[9] = String.valueOf(result.get(i).getContabilidade().getId());
                        dados[10] = result.get(i).getContabilidade().getPessoa().getNome();
                        dados[11] = result.get(i).getContabilidade().getPessoa().getTelefone1();
                        dados[12] = result.get(i).getContabilidade().getPessoa().getEmail1();
                    } catch (Exception e) {
                        dados[9] = "0";
                        dados[10] = "";
                        dados[11] = "";
                        dados[12] = "";
                    }

                    try {
                        endEscritorio = dbPesEnd.pesquisaEndPorPessoaTipo(result.get(i).getContabilidade().getPessoa().getId(), 2);
                        dados[13] = endEscritorio.getEndereco().getDescricaoEndereco().getDescricao();
                        dados[14] = endEscritorio.getEndereco().getLogradouro().getDescricao();
                        dados[15] = endEscritorio.getNumero();
                        dados[16] = endEscritorio.getComplemento();
                        dados[17] = endEscritorio.getEndereco().getBairro().getDescricao();
                        dados[18] = endEscritorio.getEndereco().getCep();
                        dados[19] = endEscritorio.getEndereco().getCidade().getCidade();
                        dados[20] = endEscritorio.getEndereco().getCidade().getUf();
                    } catch (Exception e) {
                        dados[13] = "";
                        dados[14] = "";
                        dados[15] = "";
                        dados[16] = "";
                        dados[17] = "";
                        dados[18] = "";
                        dados[19] = "";
                        dados[20] = "";
                    }
                    listaContrs.add(new ParametroContribuintes(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
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
                            result.get(i).getId(),
                            result.get(i).getPessoa().getNome(),
                            dados[0], // DESCRICAO ENDERECO
                            dados[1], // LOGRADOURO
                            endEmpresa.getNumero(),
                            endEmpresa.getComplemento(),
                            dados[2], // BAIRRO
                            dados[3], // CEP
                            dados[4], // CIDADE
                            dados[5], // UF
                            result.get(i).getPessoa().getTelefone1(),
                            result.get(i).getPessoa().getEmail1(),
                            result.get(i).getPessoa().getTipoDocumento().getDescricao(),
                            result.get(i).getPessoa().getDocumento(),
                            Integer.parseInt(dados[6]), // ID CNAE
                            dados[7], // NUMERO CNAE
                            dados[8], // DESCRICAO CNAE
                            Integer.parseInt(dados[9]), // ID CONTABILIDADE
                            dados[10], // NOME CONTABILIDADE
                            dados[13], // DESCRICAO ENDERECO CONTABILIDADE
                            dados[14], // LOGRADOURO CONTABILIDADE
                            dados[15], // NUMERO CONTABILIDADE
                            dados[16], // COMPLEMENTO CONTABILIDADE
                            dados[17], // BAIRRO CONTABILIDADE
                            dados[18], // CEP CONTABILIDADE
                            dados[19], // CIDADE CONTABILIDADE
                            dados[20], // UF CONTABILIDADE
                            dados[11], // TELEFONE CONTABILIDADE
                            dados[12] // EMAIL CONTABILIDADE
                            ));
                    endEmpresa = new PessoaEndereco();
                    endEscritorio = new PessoaEndereco();
                }
                JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(listaContrs);
                JasperPrint print = JasperFillManager.fillReport(
                        jasper,
                        null,
                        dtSource);
                arquivo = JasperExportManager.exportReportToPdf(print);

                String nomeDownload = "etiquetas_contabil_" + DataHoje.horaMinuto().replace(":", "") + ".pdf";

                SalvaArquivos sa = new SalvaArquivos(arquivo, nomeDownload, false);
                String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/etiquetas");

                sa.salvaNaPasta(pathPasta);

                Download download = new Download(nomeDownload,
                        pathPasta,
                        "application/pdf",
                        FacesContext.getCurrentInstance());
                download.baixar();
            } catch (Exception erro) {
                System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
                return null;
            }
        } catch (Exception erro) {
            System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
            return null;
        }
        return null;
    }

    public boolean getDesabilitaVi() {
        if (cbEmail.equals("todos")) {
            return false;
        } else if (cbEmail.equals("com")) {
            return true;
        } else if (cbEmail.equals("sem")) {
            return false;
        }
        return false;
    }

    public boolean getDesabilitaEmail() {
        if (cbEmail.equals("todos")) {
            return true;
        } else if (cbEmail.equals("com")) {
            return false;
        } else if (cbEmail.equals("sem")) {

            return true;
        }
        return true;
    }

    public void enviarEmail() {
        Movimento movimento = new Movimento();
        Juridica juridica = new Juridica();

        JuridicaDB dbj = new JuridicaDBToplink();
        MovimentoDB dbM = new MovimentoDBToplink();

        List<Movimento> movadd = new ArrayList();
        List<Float> listaValores = new ArrayList<Float>();
        List<String> listaVencimentos = new ArrayList<String>();


        boolean enviar = false;
        int id_contabil = 0, id_empresa = 0, id_compara = 0;
        
        for (int i = 0; i < listaMovGridSelecionada.size(); i++) {
            id_contabil = (Integer) listaMovGridSelecionada.get(i).getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getValor();
            id_empresa = (Integer) listaMovGridSelecionada.get(i).getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getValor();

            /* ENVIO PARA CONTABILIDADE */
            movimento = dbM.pesquisaCodigo((Integer) listaMovGridSelecionada.get(i).getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getValor());
            juridica = dbj.pesquisaCodigo(id_empresa);

            if (id_contabil != 0 && juridica.isEmailEscritorio()) {
                movadd.add(movimento);
                listaValores.add(movimento.getValor());
                listaVencimentos.add(movimento.getVencimento());

                juridica = dbj.pesquisaJuridicaPorPessoa(id_contabil);

                try {
                    id_compara = (Integer) listaMovGridSelecionada.get(i + 1).getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getValor();
                    if (id_contabil != id_compara) {
                        enviar = true;
                    }
                } catch (Exception e) {
                    enviar = true;
                }
                /* ENVIO PARA EMPRESA */
            } else {
                movadd.add(movimento);
                listaValores.add(movimento.getValor());
                listaVencimentos.add(movimento.getVencimento());

                try {
                    id_compara = (Integer) listaMovGridSelecionada.get(i + 1).getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getValor();
                    if (id_empresa != id_compara) {
                        enviar = true;
                    }
                } catch (Exception e) {
                    enviar = true;
                }
            }

            if (enviar) {
                enviar(movadd, listaValores, listaVencimentos, juridica);
                enviar = false;
                movadd.clear();
                listaValores.clear();
                listaVencimentos.clear();
            }
        }
    }

    public void enviar(List<Movimento> mov, List<Float> listaValores, List<String> listaVencimentos, Juridica jur) {
        try {

            Registro reg = new Registro();
            reg = (Registro) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(1, "Registro");

            ImprimirBoleto imp = new ImprimirBoleto();
            imp.imprimirBoleto(mov, listaValores, listaVencimentos, false);
            String nome = imp.criarLink(jur.getPessoa(), reg.getUrlPath() + "/Sindical/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/boletos");
            List<Pessoa> pessoas = new ArrayList();
            pessoas.add(jur.getPessoa());

            String mensagem = "";
            List<File> fls = new ArrayList<File>();
            
            String nome_envio = "";
            if (mov.size() == 1)
                nome_envio = "Boleto " + mov.get(0).getServicos().getDescricao()+" N° "+mov.get(0).getDocumento();
            else
                nome_envio = "Boleto";
            
            if (!reg.isEnviarEmailAnexo()) {
                mensagem = " <div style=\"background:#00ccff; padding: 15px; font-size:13pt\">Envio cadastrado para <b>" + jur.getPessoa().getNome() + " </b></div><br />"
                         + " <h5>Visualize seu boleto clicando no link abaixo</h5><br /><br />"
                         + " <a href=\"" + reg.getUrlPath() + "/Sindical/acessoLinks.jsf?cliente=" + ControleUsuarioBean.getCliente() + "&amp;arquivo=" + nome + "\">Clique aqui para abrir boleto</a><br />";
            } else {
                fls.add(new File(imp.getPathPasta() + "/" + nome));
                mensagem = " <div style='background:#00ccff; padding: 15px; font-size:13pt'>Envio cadastrado para <b>" + jur.getPessoa().getNome() + " </b></div><br />"
                         + " <h5>Baixe seu boleto anexado neste email</5><br /><br />";
            }
            
            DaoInterface di = new Dao();
            Mail mail = new Mail();
            mail.setFiles(fls);
            mail.setEmail(
                    new Email(
                            -1,
                            DataHoje.dataHoje(),
                            DataHoje.livre(new Date(), "HH:mm"),
                            (Usuario) GenericaSessao.getObject("sessaoUsuario"),
                            (Rotina) di.find(new Rotina(), 90),
                            null,
                            nome_envio,
                            mensagem,
                            false,
                            false
                    )
            );
            
            List<EmailPessoa> emailPessoas = new ArrayList<EmailPessoa>();
            EmailPessoa emailPessoa = new EmailPessoa();
            for (Pessoa pe : pessoas) {
                emailPessoa.setDestinatario(pe.getEmail1());
                emailPessoa.setPessoa(pe);
                emailPessoa.setRecebimento(null);
                emailPessoas.add(emailPessoa);
                mail.setEmailPessoas(emailPessoas);
                emailPessoa = new EmailPessoa();
            }
            
            String[] retorno = mail.send("personalizado");
            msgImpressao = "Envio Concluído!";
        } catch (Exception erro) {
            System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());

        }
    }

//    public String enviarEmail(){
//        int idMovimento = 0, idJuridica = 0,qnt = 0;
//        JuridicaDB dbJ = new JuridicaDBToplink();
//        MovimentoDB dbM = new MovimentoDBToplink();
//        Juridica juri = null;
//        List<Movimento> mov = new ArrayList();
//        byte[] arquivo = new byte[0];
//        for(int i = 0; i < listaMovGrid.size(); i++){
//            if ((Boolean) listaMovGrid.get(i).getValor()){
//                qnt = qnt + 1;
//                idJuridica = ( (Integer) getListaMovGrid().get(i).getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getValor());
//                idMovimento = (Integer) listaMovGrid.get(i).getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getValor();
//            }
//        }
//        if (listaMovGrid.size() > 0){
//            if (qnt > 1){
//                // QUANTIDADE MAIOR QUE 1 SELECIONADO
//                msgImpressao = "Só é permitido enviar email para uma empresa!";
//            }else{
//                juri = dbJ.pesquisaCodigo(idJuridica);
//                
//                Registro reg = new Registro();
//                reg = (Registro)(new SalvarAcumuladoDBToplink()).pesquisaCodigo(1, "Registro");
//                if ( juri.getPessoa().getEmail1().equals("") || juri.getPessoa().getEmail1() == null){
//                    msgImpressao = "Empresa não contem e-mail para envio!";
//                    return null;
//                }
//                mov.add(dbM.pesquisaCodigo(idMovimento));
//                
//                ImprimirBoleto imp = new ImprimirBoleto();
//                imp.imprimirBoleto(mov, false);
//                String nome = imp.criarLink(mov.get(0).getPessoa(), reg.getUrlPath()+"/Sindical/Arquivos/downloads/boletos");
//                List<Pessoa> p = new ArrayList();
//
//                p.add(mov.get(0).getPessoa());
//                String[] ret = EnviarEmail.EnviarEmailPersonalizado(reg, 
//                                                    p, 
//                                                    " <h5>Visualize seu boleto clicando no link abaixo</h5><br /><br />" +
//                                                    " <a href='"+reg.getUrlPath()+"/Sindical/acessoLinks.jsf?"+nome+"'>Clique aqui para abrir boleto</a><br />", 
//                                                    new ArrayList(), 
//                                                    "Envio de Boleto");
//                if (!ret[1].isEmpty())
//                    msgImpressao = ret[1];
//                else
//                    msgImpressao = ret[0];                
//                mov.clear();
//                p.clear();
//            }
//        }else
//            msgImpressao = "Nao existe boleto na lista!";
//        return null;
//    }
    public long getTotalBoletos() {
        return totalBoletos;
    }

    public void setTotalBoletos(long totalBoletos) {
        this.totalBoletos = totalBoletos;
    }

    public long getTotalEmpresas() {
        return totalEmpresas;
    }

    public void setTotalEmpresas(long totalEmpresas) {
        this.totalEmpresas = totalEmpresas;
    }

    public long getTotalEscritorios() {
        return totalEscritorios;
    }

    public void setTotalEscritorios(long totalEscritorios) {
        this.totalEscritorios = totalEscritorios;
    }

    public boolean isImprimeVerso() {
        return imprimeVerso;
    }

    public void setImprimeVerso(boolean imprimeVerso) {
        this.imprimeVerso = imprimeVerso;
    }

    public List<Convencao> getListaConvencao() {
        if (listaConvencao.isEmpty()) {
            ConvencaoDB convencaoDB = new ConvencaoDBToplink();
            listaConvencao = convencaoDB.pesquisaTodos();
        }
        return listaConvencao;
    }

    public List<GrupoCidade> getListaGrupoCidade() {
        if (listaGrupoCidade.isEmpty()) {
            if (!listaConvencaoSelecionada.isEmpty()){
                ConvencaoCidadeDB convencaoCidadeDB = new ConvencaoCidadeDBToplink();
                List<Integer> listInt = new ArrayList();

                for (int i = 0; i < listaConvencaoSelecionada.size(); i++) {
                    listInt.add(listaConvencaoSelecionada.get(i).getId());
                }

                listaGrupoCidade = convencaoCidadeDB.pesquisarConvencaoCidade(listInt);
            }
        }
        return listaGrupoCidade;
    }

    public void limpaGrupoCidade(){
        listaGrupoCidade.clear();
        listaGrupoSelecionada.clear();
    }
    
    public void setListaConvencao(List<Convencao> listaConvencao) {
        this.listaConvencao = listaConvencao;
    }

    public void setListaGrupoCidade(List<GrupoCidade> listaGrupoCidade) {
        this.listaGrupoCidade = listaGrupoCidade;
    }

    public String getMsgImpressao() {
        return msgImpressao;
    }

    public void setMsgImpressao(String msgImpressao) {
        this.msgImpressao = msgImpressao;
    }

    public String getTodasContas() {
        return todasContas;
    }

    public void setTodasContas(String todasContas) {
        this.todasContas = todasContas;
    }

    public String getMovimentosSemMensagem() {
        return movimentosSemMensagem;
    }

    public void setMovimentosSemMensagem(String movimentosSemMensagem) {
        this.movimentosSemMensagem = movimentosSemMensagem;
    }

    public String getRegraEscritorios() {
        return regraEscritorios;
    }

    public void setRegraEscritorios(String regraEscritorios) {
        this.regraEscritorios = regraEscritorios;
    }

    public int getQuantidadeEmpresas() {
        return quantidadeEmpresas;
    }

    public void setQuantidadeEmpresas(int quantidadeEmpresas) {
        this.quantidadeEmpresas = quantidadeEmpresas;
    }

    public boolean isChkTodosVencimentos() {
        return chkTodosVencimentos;
    }

    public void setChkTodosVencimentos(boolean chkTodosVencimentos) {
        this.chkTodosVencimentos = chkTodosVencimentos;
    }

    public String getCbEmail() {
        return cbEmail;
    }

    public void setCbEmail(String cbEmail) {
        this.cbEmail = cbEmail;
    }

    public Juridica getContabilidade() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa") != null) {
            contabilidade = (Juridica) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
        }
        return contabilidade;
    }

    public void setContabilidade(Juridica contabilidade) {
        this.contabilidade = contabilidade;
    }

    public List<Convencao> getListaConvencaoSelecionada() {
        return listaConvencaoSelecionada;
    }

    public void setListaConvencaoSelecionada(List<Convencao> listaConvencaoSelecionada) {
        this.listaConvencaoSelecionada = listaConvencaoSelecionada;
    }

    public List<GrupoCidade> getListaGrupoSelecionada() {
        return listaGrupoSelecionada;
    }

    public void setListaGrupoSelecionada(List<GrupoCidade> listaGrupoSelecionada) {
        this.listaGrupoSelecionada = listaGrupoSelecionada;
    }

    public List<String> getListaDataSelecionada() {
        return listaDataSelecionada;
    }

    public void setListaDataSelecionada(List<String> listaDataSelecionada) {
        this.listaDataSelecionada = listaDataSelecionada;
    }

    public List<Linha> getListaMovGridSelecionada() {
        return listaMovGridSelecionada;
    }

    public void setListaMovGridSelecionada(List<Linha> listaMovGridSelecionada) {
        this.listaMovGridSelecionada = listaMovGridSelecionada;
    }
}