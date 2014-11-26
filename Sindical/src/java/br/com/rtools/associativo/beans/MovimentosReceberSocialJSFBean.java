package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.Socios;
import br.com.rtools.associativo.db.LancamentoIndividualDB;
import br.com.rtools.associativo.db.LancamentoIndividualDBToplink;
import br.com.rtools.associativo.db.MovimentosReceberSocialDB;
import br.com.rtools.associativo.db.MovimentosReceberSocialDBToplink;
import br.com.rtools.associativo.db.SociosDB;
import br.com.rtools.associativo.db.SociosDBToplink;
import br.com.rtools.financeiro.Baixa;
import br.com.rtools.financeiro.ContaCobranca;
import br.com.rtools.financeiro.FormaPagamento;
import br.com.rtools.financeiro.Guia;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.db.MovimentoDB;
import br.com.rtools.financeiro.db.MovimentoDBToplink;
import br.com.rtools.financeiro.db.ServicoContaCobrancaDB;
import br.com.rtools.financeiro.db.ServicoContaCobrancaDBToplink;
import br.com.rtools.impressao.ParametroEncaminhamento;
import br.com.rtools.impressao.ParametroRecibo;
import br.com.rtools.movimento.GerarMovimento;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.db.PessoaEnderecoDB;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.Diretorio;
import br.com.rtools.utilitarios.Download;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

public class MovimentosReceberSocialJSFBean {

    private String porPesquisa = "abertos";
    private List<DataObject> listaMovimento = new ArrayList();
    private String titular = "";
    private String beneficiario = "";
    private String data = "";
    private String boleto = "";
    private String diasAtraso = "";
    private String multa = "", juros = "", correcao = "";
    private String caixa = "";
    private String documento = "";
    private String referencia = "";
    private String tipo = "";
    private String id_baixa = "";
    private String msgConfirma = "";
    private String desconto = "0";
    private boolean chkSeleciona = false;
    private boolean addMais = false;
    private Pessoa pessoa = new Pessoa();
    private List<Pessoa> listaPessoa = new ArrayList();

    private String matricula = "";
    private String categoria = "";
    private String grupo = "";
    private String status = "";
    
    private String descPesquisaBoleto = "";
    private List<SelectItem> listaContas = new ArrayList();
    private int indexConta = 0;
    
    public Guia pesquisaGuia(int id_lote){
        MovimentoDB db = new MovimentoDBToplink();
        Guia gu = db.pesquisaGuias(id_lote);
        if (gu.getId() != -1) {
            LancamentoIndividualDB dbl = new LancamentoIndividualDBToplink();
            List<Juridica> list = (List<Juridica>) dbl.listaEmpresaConveniadaPorSubGrupo(gu.getSubGrupoConvenio().getId());
            String conveniada = "";
            if (!list.isEmpty()) {
                conveniada = list.get(0).getFantasia();
            }
        }
        return gu;
    }
    
    public List<SelectItem> getListaContas(){
        if (listaContas.isEmpty()){
            ServicoContaCobrancaDB servDB = new ServicoContaCobrancaDBToplink();
            List<ContaCobranca> result = servDB.listaContaCobrancaAtivoAssociativo();
            if (result.isEmpty()) {
                listaContas.add(new SelectItem(0, "Nenhuma Conta Encontrada", "0"));
                return listaContas;
            }
            int contador = 0;
            for (int i = 0; i < result.size(); i++) {
                // LAYOUT 2 = SINDICAL
                if (result.get(i).getLayout().getId() != 2) {
                    listaContas.add(
                            new SelectItem(
                                    contador,
                                    result.get(i).getApelido() + " - " + result.get(i).getCodCedente(), // CODCEDENTE NO CASO DE OUTRAS
                                    Integer.toString(result.get(i).getId())
                            )
                    );
                    contador++;
                }
            }
//            if (!listaContas.isEmpty()) {
//                contaCobranca = (ContaCobranca) new Dao().find(new ContaCobranca(), Integer.parseInt(((SelectItem) listaContas.get(indexConta)).getDescription()));
//            }
        }
        return listaContas;
    }
    
    public void pesquisaBoleto(){
        if (descPesquisaBoleto.isEmpty()){
            if (pessoa.getId() != -1){
                porPesquisa = "todos";
                listaMovimento.clear();
                getListaMovimento();
            }
            return;
        }
        
        try{
            //int numerox = Integer.valueOf(descPesquisaBoleto);
            MovimentosReceberSocialDB db = new MovimentosReceberSocialDBToplink();
            ContaCobranca contaCobranca = (ContaCobranca) new Dao().find(new ContaCobranca(), Integer.parseInt(((SelectItem) listaContas.get(indexConta)).getDescription()));
            Pessoa p = db.pesquisaPessoaPorBoleto(descPesquisaBoleto, contaCobranca.getId());
            listaPessoa.clear();
            pessoa = new Pessoa();
            
            if (p != null){
                pessoa = p;
                listaPessoa.add(p);
            }
            porPesquisa = "todos";
            listaMovimento.clear();
            getListaMovimento();
        }catch(Exception e){
            descPesquisaBoleto = "";
            GenericaMensagem.fatal("Atenção", "Digite um número de Boleto válido!");
        }
    }
    
    public void salvarRecibo(byte[] arquivo, Baixa baixa) {
        //SalvaArquivos sa = new SalvaArquivos(arquivo, String.valueOf(baixa.getId()), false);
        if (baixa.getCaixa() == null) {
            return;
        }

        String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/" + "Arquivos/recibo/" + baixa.getCaixa().getCaixa() + "/" + DataHoje.converteData(baixa.getDtBaixa()).replace("/", "-"));
        Diretorio.criar("Arquivos/recibo/" + baixa.getCaixa().getCaixa() + "/" + DataHoje.converteData(baixa.getDtBaixa()).replace("/", "-"));

        String path_arquivo = caminho + "/" + String.valueOf(baixa.getUsuario().getId()) + "_" + String.valueOf(baixa.getId()) + ".pdf";
        File file_arquivo = new File(path_arquivo);

        if (file_arquivo.exists()) {
            path_arquivo = caminho + "/" + String.valueOf(baixa.getUsuario().getId()) + "_" + String.valueOf(baixa.getId()) + "_(2).pdf";
        }

        try {
            File fl = new File(path_arquivo);
            FileOutputStream out = new FileOutputStream(fl);
            out.write(arquivo);
            out.flush();
            out.close();
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    public String recibo(int id_movimento) {

        MovimentoDB db = new MovimentoDBToplink();
        Movimento movimento = new Movimento();

        movimento = db.pesquisaCodigo(id_movimento);
        try {
            Collection vetor = new ArrayList();
            Juridica sindicato = (Juridica) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(1, "Juridica");
            PessoaEnderecoDB dbp = new PessoaEnderecoDBToplink();
                //MovimentosReceberSocialDB dbs = new MovimentosReceberSocialDBToplink();

            PessoaEndereco pe = dbp.pesquisaEndPorPessoaTipo(1, 2);
            String formas[] = new String[10];

            // PESQUISA FORMA DE PAGAMENTO
            List<FormaPagamento> fp = db.pesquisaFormaPagamento(movimento.getBaixa().getId());

            for (int i = 0; i < fp.size(); i++) {
                // 4 - CHEQUE    
                if (fp.get(i).getTipoPagamento().getId() == 4) {
                    formas[i] = fp.get(i).getTipoPagamento().getDescricao() + ": R$ " + Moeda.converteR$Float(fp.get(i).getValor()) + " (B: " + fp.get(i).getChequeRec().getBanco() + " Ag: " + fp.get(i).getChequeRec().getAgencia() + " C: " + fp.get(i).getChequeRec().getConta() + " CH: " + fp.get(i).getChequeRec().getCheque();
                    // 5 - CHEQUE PRÉ
                } else if (fp.get(i).getTipoPagamento().getId() == 5) {
                    formas[i] = fp.get(i).getTipoPagamento().getDescricao() + ": R$ " + Moeda.converteR$Float(fp.get(i).getValor()) + " (B: " + fp.get(i).getChequeRec().getBanco() + " Ag: " + fp.get(i).getChequeRec().getAgencia() + " C: " + fp.get(i).getChequeRec().getConta() + " CH: " + fp.get(i).getChequeRec().getCheque() + " P: " + fp.get(i).getChequeRec().getVencimento() + ")";
                    // QUALQUER OUTRO    
                } else {
                    formas[i] = fp.get(i).getTipoPagamento().getDescricao() + ": R$ " + Moeda.converteR$Float(fp.get(i).getValor());
                }
            }

            List<Movimento> lista = db.listaMovimentoBaixaOrder(movimento.getBaixa().getId());
            for (int i = 0; i < lista.size(); i++) {
                String conveniada = "";
                if (lista.get(i).getLote().getRotina().getId() == 132) {
                    Guia gu = db.pesquisaGuias(lista.get(i).getLote().getId());
                    if (gu.getId() != -1) {
                        LancamentoIndividualDB dbl = new LancamentoIndividualDBToplink();
                        List<Juridica> list = (List<Juridica>) dbl.listaEmpresaConveniadaPorSubGrupo(gu.getSubGrupoConvenio().getId());
                        if (!list.isEmpty()) {
                            conveniada = list.get(0).getFantasia();
                        }
                    }
                }

                vetor.add(new ParametroRecibo(
                        ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
                        sindicato.getPessoa().getNome(),
                        pe.getEndereco().getDescricaoEndereco().getDescricao(),
                        pe.getEndereco().getLogradouro().getDescricao(),
                        pe.getNumero(),
                        pe.getComplemento(),
                        pe.getEndereco().getBairro().getDescricao(),
                        pe.getEndereco().getCep().substring(0, 5) + "-" + pe.getEndereco().getCep().substring(5),
                        pe.getEndereco().getCidade().getCidade(),
                        pe.getEndereco().getCidade().getUf(),
                        sindicato.getPessoa().getTelefone1(),
                        sindicato.getPessoa().getEmail1(),
                        sindicato.getPessoa().getSite(),
                        sindicato.getPessoa().getDocumento(),
                        lista.get(i).getLote().getPessoa().getNome(), // RESPONSÁVEL
                        String.valueOf(lista.get(i).getLote().getPessoa().getId()), // ID_RESPONSAVEL
                        String.valueOf(lista.get(i).getBaixa().getId()), // ID_BAIXA
                        lista.get(i).getBeneficiario().getNome(), // BENEFICIÁRIO
                        lista.get(i).getServicos().getDescricao(), // SERVICO
                        lista.get(i).getVencimento(), // VENCIMENTO
                        new BigDecimal(lista.get(i).getValorBaixa()), // VALOR BAIXA
                        lista.get(i).getBaixa().getUsuario().getLogin(),
                        lista.get(i).getBaixa().getBaixa(),
                        DataHoje.horaMinuto(),
                        formas[0],
                        formas[1],
                        formas[2],
                        formas[3],
                        formas[4],
                        formas[5],
                        formas[6],
                        formas[7],
                        formas[8],
                        formas[9],
                        ( conveniada.isEmpty() ) ? "" : "Empresa Conveniada: " + conveniada
                )
                );
            }
            
            File fl = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/RECIBO.jasper"));
            JasperReport jasper = (JasperReport) JRLoader.loadObject(fl);

            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(vetor);
            JasperPrint print = JasperFillManager.fillReport(jasper, null, dtSource);

            byte[] arquivo = JasperExportManager.exportReportToPdf(print);
            salvarRecibo(arquivo, lista.get(0).getBaixa());

            HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            res.setContentType("application/pdf");
            res.setHeader("Content-disposition", "inline; filename=\"" + "boleto_x" + ".pdf\"");
            res.getOutputStream().write(arquivo);
            res.getCharacterEncoding();

            FacesContext.getCurrentInstance().responseComplete();
        } catch (JRException | IOException ex) {
            Logger.getLogger(MovimentosReceberSocialJSFBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
    
    public void encaminhamento(int id_lote){
        Juridica sindicato = (Juridica) (new Dao()).find(new Juridica(), 1);
        PessoaEnderecoDB dbp = new PessoaEnderecoDBToplink();
        PessoaEndereco pe = dbp.pesquisaEndPorPessoaTipo(1, 2);
        MovimentoDB db = new MovimentoDBToplink();
        
        Collection vetor = new ArrayList();
        
        Guia guia = pesquisaGuia(id_lote);
        
        if (guia.getId() == -1){
            return;
        }
        
        SociosDB dbs = new SociosDBToplink();
        
        
        List<Movimento> list_movimentos = db.pesquisaGuia(guia);
        //List<Movimento> list_test = db.pesquisaGuia((Guia)new Dao().find(new Guia(), 3));

        //list_movimentos.addAll(list_test);
        
        Socios socios = dbs.pesquisaSocioPorPessoaAtivo(list_movimentos.get(0).getBeneficiario().getId());
        
        String str_servicos = "", str_usuario = "", str_nome = "", str_validade = "";
        
                
        PessoaEndereco pe_empresa = dbp.pesquisaEndPorPessoaTipo(guia.getPessoa().getId(), 5);
        String complemento = (pe_empresa.getComplemento().isEmpty()) ? "" : " ( "+pe_empresa.getComplemento()+" ) ";
        String endereco = pe_empresa.getEndereco().getLogradouro().getDescricao() + " "
                        + pe_empresa.getEndereco().getDescricaoEndereco().getDescricao() + ", " + pe_empresa.getNumero() + " - " + complemento
                        + pe_empresa.getEndereco().getBairro().getDescricao() + ", "
                        + pe_empresa.getEndereco().getCidade().getCidade() + "  -  "
                        + pe_empresa.getEndereco().getCidade().getUf();
        
        str_usuario = list_movimentos.get(0).getBaixa().getUsuario().getPessoa().getNome();

        str_nome = list_movimentos.get(0).getBeneficiario().getNome();
        
        List<JasperPrint> list_jasper = new ArrayList();
        Map<String, String> hash = new HashMap();
        try{
            String path = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads");  
            String nameFile = "encaminhamento_" + DataHoje.livre(DataHoje.dataHoje(), "yyyyMMdd-HHmmss") + ".pdf";
            File fl = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/ENCAMINHAMENTO.jasper"));
            JasperReport jasper = (JasperReport) JRLoader.loadObject(fl);

            for (Movimento movs : list_movimentos){
//                if (!str_servicos.isEmpty())
//                    str_servicos += ", "+movs.getServicos().getDescricao();
//                else
//                    str_servicos = movs.getServicos().getDescricao();

                DataHoje dh = new DataHoje();
                str_validade = dh.incrementarDias(movs.getServicos().getValidade(), guia.getLote().getEmissao());

                if (hash.containsKey(str_validade)){
                    //str_servicos += ", "+movs.getServicos().getDescricao();
                    hash.put(str_validade, hash.get(str_validade)+", "+movs.getServicos().getDescricao());
                }else{
                    //str_servicos = movs.getServicos().getDescricao();
                    hash.put(str_validade, movs.getServicos().getDescricao());
                }
                
                //if (!compara_validade.equals(str_validade)){

                //}
                //compara_validade = str_validade;
                
                //vetor.clear();
            }

            for (Map.Entry<String, String> entry : hash.entrySet()) {
                
                vetor.add(new ParametroEncaminhamento(
                        ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
                        sindicato.getPessoa().getNome(),
                        pe.getEndereco().getDescricaoEndereco().getDescricao(),
                        pe.getEndereco().getLogradouro().getDescricao(),
                        pe.getNumero(),
                        pe.getComplemento(),
                        pe.getEndereco().getBairro().getDescricao(),
                        pe.getEndereco().getCep().substring(0, 5) + "-" + pe.getEndereco().getCep().substring(5),
                        pe.getEndereco().getCidade().getCidade(),
                        pe.getEndereco().getCidade().getUf(),
                        sindicato.getPessoa().getTelefone1(),
                        sindicato.getPessoa().getEmail1(),
                        sindicato.getPessoa().getSite(),
                        sindicato.getPessoa().getDocumento(),
                        String.valueOf(guia.getId()), // GUIA
                        String.valueOf(guia.getPessoa().getId()), // CODIGO
                        guia.getSubGrupoConvenio().getGrupoConvenio().getDescricao(), // GRUPO
                        guia.getSubGrupoConvenio().getDescricao(), // SUB GRUPO
                        pe_empresa.getPessoa().getNome(), // EMPRESA CONVENIADA
                        endereco, // EMPRESA ENDERECO
                        pe_empresa.getPessoa().getTelefone1(), // EMPRESA TELEFONE
                        entry.getValue(),//str_servicos, // SERVICOS
                        guia.getLote().getEmissao(), // EMISSAO
                        entry.getKey(),//str_validade, // VALIDADE
                        str_usuario, // USUARIO
                        str_nome,
                        socios.getParentesco().getParentesco(),
                        (socios.getMatriculaSocios().getId() == -1) ? "" : String.valueOf(socios.getMatriculaSocios().getId()),
                        socios.getMatriculaSocios().getCategoria().getCategoria()
                ));
                
                JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(vetor);
                JasperPrint print = JasperFillManager.fillReport(jasper, null, dtSource);
                list_jasper.add(print);
                
                vetor.clear();
            }
                
                    
            JRPdfExporter exporter = new JRPdfExporter();
            //ByteArrayOutputStream output = new ByteArrayOutputStream();
            
            exporter.setExporterInput(SimpleExporterInput.getInstance(list_jasper));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(path+"/"+nameFile));
            
            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
            
            configuration.setCreatingBatchModeBookmarks(true);
            
            exporter.setConfiguration(configuration);
            exporter.exportReport();

            File flx = new File(path);
            
            if (flx.exists()){
                Download download = new Download(
                        nameFile,
                        path,
                        "application/pdf",
                        FacesContext.getCurrentInstance()
                );
                
                download.baixar();
                download.remover();
            }            
//            byte[] arquivo = JasperExportManager.exportReportToPdf(print);
//
//            HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//            res.setContentType("application/pdf");
//            res.setHeader("Content-disposition", "inline; filename=\"" + "Impressão de Encaminhamento" + ".pdf\"");
//            res.getOutputStream().write(arquivo);
//            res.getCharacterEncoding();
//
//            FacesContext.getCurrentInstance().responseComplete();
        }catch(JRException e){
            e.getMessage();
        }
    }

    public String removerPesquisa() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        pessoa = new Pessoa();
        return "movimentosReceberSocial";
    }

    public String removerPessoaLista(int index) {
        listaPessoa.remove(index);
        listaMovimento.clear();
        return "movimentosReceberSocial";
    }

    public boolean baixado() {
        for (int i = 0; i < listaMovimento.size(); i++) {
            if (((Boolean) listaMovimento.get(i).getArgumento0()) && Moeda.converteUS$(listaMovimento.get(i).getArgumento11().toString()) > 0.0) {
                return true;
            }
        }
        return false;
    }

    public boolean semValor() {
        for (int i = 0; i < listaMovimento.size(); i++) {
            if ((Boolean) listaMovimento.get(i).getArgumento0() && Moeda.converteUS$(listaMovimento.get(i).getArgumento9().toString()) <= 0.0) {
                return true;
            }
        }
        return false;
    }

    public boolean acordados() {
        for (int i = 0; i < listaMovimento.size(); i++) {
            if ((Boolean) listaMovimento.get(i).getArgumento0() && String.valueOf(listaMovimento.get(i).getArgumento3()).equals("Acordo")) {
                return true;
            }
        }
        return false;
    }

    public String estornarBaixa() {
        if (listaMovimento.isEmpty()) {
            msgConfirma = "Não existem boletos para serem estornados!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        MovimentoDB db = new MovimentoDBToplink();
        int qnt = 0;
        Movimento mov = null;

        for (int i = 0; i < listaMovimento.size(); i++) {
            if ((Boolean) listaMovimento.get(i).getArgumento0()) {
                qnt++;
                mov = db.pesquisaCodigo(Integer.parseInt(String.valueOf(listaMovimento.get(i).getArgumento1())));
            }
        }

        if (qnt == 0) {
            msgConfirma = "Nenhum Movimento selecionado!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        if (qnt > 1) {
            msgConfirma = "Mais de um movimento foi selecionado!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        if (!baixado()) {
            msgConfirma = "Existem boletos que não foram pagos para estornar!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        boolean est = true;

        if (!mov.isAtivo()) {
            msgConfirma = "Boleto ID: " + mov.getId() + " esta inativo, não é possivel concluir estorno!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        if (mov.getLote().getRotina() != null && mov.getLote().getRotina().getId() == 132) {
            mov.setAtivo(false);
        }

        if (!GerarMovimento.estornarMovimento(mov)) {
            est = false;
        }

        if (!est) {
            msgConfirma = "Ocorreu erros ao estornar boletos, verifique o log!";
            GenericaMensagem.warn("Erro", msgConfirma);
        } else {
            msgConfirma = "Boletos estornados com sucesso!";
            GenericaMensagem.info("Sucesso", msgConfirma);
        }
        listaMovimento.clear();
        chkSeleciona = true;
        return null;
    }

    public String telaBaixa() {
        List lista = new ArrayList();
        MovimentoDB db = new MovimentoDBToplink();
        Movimento movimento = new Movimento();
        MacFilial macFilial = (MacFilial) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoFilial");

        if (macFilial == null) {
            msgConfirma = "Não existe filial na sessão!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        if (macFilial.getCaixa() == null) {
            msgConfirma = "Configurar Caixa nesta estação de trabalho!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        if (baixado()) {
            msgConfirma = "Existem boletos baixados na lista!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        if (semValor()) {
            msgConfirma = "Boletos sem valor não podem ser Baixados!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        if (!listaMovimento.isEmpty()) {
            for (int i = 0; i < listaMovimento.size(); i++) {
                if ((Boolean) listaMovimento.get(i).getArgumento0()) {
                    movimento = db.pesquisaCodigo(Integer.parseInt(String.valueOf(listaMovimento.get(i).getArgumento1())));

                    movimento.setMulta(Moeda.converteUS$(listaMovimento.get(i).getArgumento19().toString()));
                    movimento.setJuros(Moeda.converteUS$(listaMovimento.get(i).getArgumento20().toString()));
                    movimento.setCorrecao(Moeda.converteUS$(listaMovimento.get(i).getArgumento21().toString()));
                    movimento.setDesconto(Moeda.converteUS$(listaMovimento.get(i).getArgumento8().toString()));

                    //movimento.setValor( Float.valueOf( listaMovimento.get(i).getArgumento9().toString() ) );
                    movimento.setValor(Moeda.converteUS$(listaMovimento.get(i).getArgumento6().toString()));

                    // movimento.setValorBaixa( Moeda.subtracaoValores(movimento.getValor(), movimento.getDesconto()) );
                    movimento.setValorBaixa(Moeda.converteUS$(listaMovimento.get(i).getArgumento9().toString()));

                    lista.add(movimento);
                }
            }
            if (!lista.isEmpty()) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaMovimento", lista);
                return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).baixaGeral();
            } else {
                msgConfirma = "Nenhum boleto foi selecionado";
                GenericaMensagem.warn("Erro", msgConfirma);
            }
        } else {
            msgConfirma = "Lista vazia!";
            GenericaMensagem.warn("Erro", msgConfirma);
        }
        return null;
    }

    public String telaMovimento(int id_movimento) {
        List lista = new ArrayList();
        MovimentoDB db = new MovimentoDBToplink();
        Movimento movimento = new Movimento();

//                    movimento = db.pesquisaCodigo(Integer.parseInt(String.valueOf(listaMovimento.get(i).getArgumento1())));
        movimento = db.pesquisaCodigo(id_movimento);

//                    movimento.setMulta(Moeda.converteUS$(listaMovimento.get(i).getArgumento19().toString()));
//                    movimento.setJuros(Moeda.converteUS$( listaMovimento.get(i).getArgumento20().toString()));
//                    movimento.setCorrecao(Moeda.converteUS$( listaMovimento.get(i).getArgumento21().toString()));
//
//                    movimento.setDesconto(Moeda.converteUS$(listaMovimento.get(i).getArgumento8().toString()));
//
//                    movimento.setValor(Moeda.converteUS$(listaMovimento.get(i).getArgumento6().toString()));
        // movimento.setValorBaixa( Moeda.subtracaoValores(movimento.getValor(), movimento.getDesconto()) );
//                    movimento.setValorBaixa(Moeda.converteUS$(listaMovimento.get(i).getArgumento9().toString()));
        lista.add(movimento);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaMovimento", lista);
        return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).alterarMovimento();
    }

    public String telaAcordo() {
        List lista = new ArrayList();
        MovimentoDB db = new MovimentoDBToplink();
        Movimento movimento = new Movimento();
        if (baixado()) {
            msgConfirma = "Existem boletos baixados na lista!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        if (semValor()) {
            msgConfirma = "Boletos sem valor não podem ser Acordados!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        if (acordados()) {
            msgConfirma = "Boletos do tipo Acordo não podem ser Reacordados!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }
        if (!listaMovimento.isEmpty()) {
            for (int i = 0; i < listaMovimento.size(); i++) {
                if ((Boolean) listaMovimento.get(i).getArgumento0()) {
                    movimento = db.pesquisaCodigo(Integer.parseInt(String.valueOf(listaMovimento.get(i).getArgumento1())));

                    movimento.setMulta(Moeda.converteUS$(listaMovimento.get(i).getArgumento19().toString()));
                    movimento.setJuros(Moeda.converteUS$(listaMovimento.get(i).getArgumento20().toString()));
                    movimento.setCorrecao(Moeda.converteUS$(listaMovimento.get(i).getArgumento21().toString()));

                    movimento.setDesconto(Moeda.converteUS$(listaMovimento.get(i).getArgumento8().toString()));

                    movimento.setValor(Moeda.converteUS$(listaMovimento.get(i).getArgumento6().toString()));

                    // movimento.setValorBaixa( Moeda.subtracaoValores(movimento.getValor(), movimento.getDesconto()) );
                    movimento.setValorBaixa(Moeda.converteUS$(listaMovimento.get(i).getArgumento9().toString()));
                    lista.add(movimento);
                }
            }
            if (!lista.isEmpty()) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaMovimento", lista);
                return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).acordo();
            } else {
                msgConfirma = "Nenhum boleto foi selecionado";
                GenericaMensagem.warn("Erro", msgConfirma);
            }
        } else {
            msgConfirma = "Lista vazia!";
            GenericaMensagem.warn("Erro", msgConfirma);
        }
        return null;
    }

    public void calculoDesconto() {
        float descPorcento = 0;
        float desc = 0;
        float acre = 0;
        float calc = Moeda.substituiVirgulaFloat(getValorPraDesconto());
        if (Float.valueOf(desconto) > calc) {
            desconto = String.valueOf(calc);
        }

        descPorcento = Moeda.multiplicarValores(Moeda.divisaoValores(Float.valueOf(desconto), calc), 100);

        for (int i = 0; i < listaMovimento.size(); i++) {
            if ((Boolean) listaMovimento.get(i).getArgumento0() && Moeda.converteUS$(listaMovimento.get(i).getArgumento11().toString()) == 0.0) {
                acre = Moeda.converteUS$(listaMovimento.get(i).getArgumento7().toString());

                float valor_calc = Moeda.somaValores(Moeda.converteUS$(listaMovimento.get(i).getArgumento6().toString()), acre);
                desc = Moeda.divisaoValores(Moeda.multiplicarValores(valor_calc, descPorcento), 100);

                listaMovimento.get(i).setArgumento8(Moeda.converteR$(String.valueOf(desc)));
                listaMovimento.get(i).setArgumento9(Moeda.converteR$(String.valueOf(Moeda.subtracaoValores(valor_calc, desc))));
            } else {
                listaMovimento.get(i).setArgumento8("0,00");
                listaMovimento.get(i).setArgumento9(Moeda.converteR$(listaMovimento.get(i).getArgumento6().toString()));
            }
        }
    }

    public void atualizarStatus() {
        listaMovimento.clear();
    }

    public String getTotal() {
        if (!listaMovimento.isEmpty()) {
            float soma = 0;
            for (int i = 0; i < listaMovimento.size(); i++) {
                if ((Boolean) listaMovimento.get(i).getArgumento0() && Moeda.converteUS$(listaMovimento.get(i).getArgumento11().toString()) == 0.0) {
                    soma = Moeda.somaValores(soma, Moeda.converteUS$(listaMovimento.get(i).getArgumento6().toString()));
                }
            }

            return Moeda.converteR$Float(soma);
        } else {
            return "0";
        }
    }

    public String getAcrescimo() {
        if (!listaMovimento.isEmpty()) {
            float soma = 0;
            for (int i = 0; i < listaMovimento.size(); i++) {
                if ((Boolean) listaMovimento.get(i).getArgumento0()) {
                    soma = Moeda.somaValores(soma, Moeda.converteUS$(listaMovimento.get(i).getArgumento7().toString()));
                }
            }

            return Moeda.converteR$Float(soma);
        } else {
            return "0";
        }
    }

    public String getValorPraDesconto() {
        if (!listaMovimento.isEmpty()) {
            float soma = 0;
            for (int i = 0; i < listaMovimento.size(); i++) {
                if ((Boolean) listaMovimento.get(i).getArgumento0() && Moeda.converteUS$(listaMovimento.get(i).getArgumento11().toString()) == 0.0) {
                    soma = Moeda.somaValores(soma, Moeda.converteUS$(listaMovimento.get(i).getArgumento24().toString()));
                }
            }
            return Moeda.converteR$Float(soma);
        } else {
            return "0";
        }
    }

    public String getTotalCalculado() {
        if (!listaMovimento.isEmpty()) {
            float soma = 0;
            for (int i = 0; i < listaMovimento.size(); i++) {
                if ((Boolean) listaMovimento.get(i).getArgumento0() && Moeda.converteUS$(listaMovimento.get(i).getArgumento11().toString()) == 0.0) {
                    soma = Moeda.somaValores(soma, Moeda.converteUS$(listaMovimento.get(i).getArgumento9().toString()));
                }
            }
            return Moeda.converteR$Float(soma);
        } else {
            return "0";
        }
    }

    public void complementoPessoa(DataObject linha) {
        // COMENTARIO PARA ORDEM QUE VEM DA QUERY
        //titular = (String) linha.getArgumento15(); // 13 - TITULAR
        tipo = (String) linha.getArgumento3(); // 1 - TIPO SERVIÇO
        referencia = (String) linha.getArgumento4(); // 2 - REFERENCIA
        id_baixa = linha.getArgumento26().toString(); // 23 - ID_BAIXA

        beneficiario = (String) linha.getArgumento14(); // 12 - BENEFICIARIO
        data = linha.getArgumento16().toString(); // 16 - CRIACAO
        boleto = (String) linha.getArgumento17(); // 17 - BOLETO
        diasAtraso = linha.getArgumento18().toString(); // 18 - DIAS EM ATRASO
        multa = "R$ " + Moeda.converteR$(linha.getArgumento19().toString()); // 19 - MULTA
        juros = "R$ " + Moeda.converteR$(linha.getArgumento20().toString()); // 20 - JUROS
        correcao = "R$ " + Moeda.converteR$(linha.getArgumento21().toString()); // 21 - CORRECAO
        caixa = (linha.getArgumento22() == null) ? "Nenhum" : linha.getArgumento22().toString(); // 22 - CAIXA
        documento = (linha.getArgumento23() == null) ? "Sem Documento" : linha.getArgumento23().toString(); // 24 - DOCUMENTO

        int id_lote = Integer.valueOf(linha.getArgumento27().toString());

        MovimentosReceberSocialDB db = new MovimentosReceberSocialDBToplink();
        List<Vector> lista = db.dadosSocio(id_lote);

        if (!lista.isEmpty()) {
            titular = lista.get(0).get(0).toString(); // TITULAR
            matricula = lista.get(0).get(1).toString(); // MATRICULA
            categoria = lista.get(0).get(2).toString(); // CATEGORIA
            grupo = lista.get(0).get(3).toString(); // GRUPO
            status = lista.get(0).get(4).toString(); // CASE
        } else {
            titular = "";
            matricula = "";
            categoria = "";
            grupo = "";
            status = "";
        }
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public void marcarTodos() {
        for (int i = 0; i < listaMovimento.size(); i++) {
            listaMovimento.get(i).setArgumento0(chkSeleciona);
        }
    }

    public List<DataObject> getListaMovimento() {
        if (listaMovimento.isEmpty() && !listaPessoa.isEmpty()) {
            MovimentosReceberSocialDB db = new MovimentosReceberSocialDBToplink();
            String ids = "";
            for (int i = 0; i < listaPessoa.size(); i++) {
                if (ids.length() > 0 && i != listaPessoa.size()) {
                    ids = ids + ",";
                }
                ids = ids + String.valueOf(listaPessoa.get(i).getId());
            }
            List<Vector> lista = db.pesquisaListaMovimentos(ids, porPesquisa);
            //float soma = 0;
            boolean chk = false, disabled = false;
            String dataBaixa = "";

            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).get(8) != null) {
                    dataBaixa = DataHoje.converteData((Date) lista.get(i).get(8));
                } else {
                    dataBaixa = "";
                }
                //soma = Moeda.somaValores(Moeda.converteR$(lista.get(i).get(5).toString()), Moeda.converteUS$(listaMovimento.get(i).getArgumento9().toString()));
                // DATA DE HOJE MENOR OU IGUAL A DATA DE VENCIMENTO
                if (DataHoje.converteDataParaInteger(DataHoje.converteData((Date) lista.get(i).get(3)))
                        <= DataHoje.converteDataParaInteger(DataHoje.data())
                        && dataBaixa.isEmpty()) {
                    chk = true;
                } else {
                    chk = false;
                }

                // DATA DE HOJE MENOR QUE DATA DE VENCIMENTO
                if (DataHoje.converteDataParaInteger(DataHoje.converteData((Date) lista.get(i).get(3)))
                        < DataHoje.converteDataParaInteger(DataHoje.data()) && dataBaixa.isEmpty()) {
                    disabled = true;
                } else {
                    disabled = false;
                }

                listaMovimento.add(new DataObject(
                        chk, // ARG 0
                        lista.get(i).get(14), // ARG 1 ID_MOVIMENTO
                        lista.get(i).get(0), // ARG 2 SERVICO
                        lista.get(i).get(1), // ARG 3 TIPO_SERVICO
                        lista.get(i).get(2), // ARG 4 REFERENCIA
                        DataHoje.converteData((Date) lista.get(i).get(3)), // ARG 5 VENCIMENTO
                        Moeda.converteR$(getConverteNullString(lista.get(i).get(4))), // ARG 6 VALOR
                        Moeda.converteR$(getConverteNullString(lista.get(i).get(5))), // ARG 7 ACRESCIMO
                        Moeda.converteR$(getConverteNullString(lista.get(i).get(6))), // ARG 8 DESCONTO
                        Moeda.converteR$(getConverteNullString(lista.get(i).get(7))), // ARG 9 VALOR CALCULADO
                        dataBaixa, // ARG 10 DATA BAIXA
                        Moeda.converteR$(getConverteNullString(lista.get(i).get(9))), // ARG 11 VALOR_BAIXA
                        lista.get(i).get(10), // ARG 12 ES
                        lista.get(i).get(11), // ARG 13 RESPONSAVEL
                        lista.get(i).get(12), // ARG 14 BENEFICIARIO
                        lista.get(i).get(13), // ARG 15 TITULAR
                        DataHoje.converteData((Date) lista.get(i).get(16)), // ARG 16 CRIACAO
                        lista.get(i).get(17), // ARG 17 BOLETO
                        lista.get(i).get(18), // ARG 18 DIAS DE ATRASO
                        Moeda.converteR$(getConverteNullString(lista.get(i).get(19))), // ARG 29 MULTA
                        Moeda.converteR$(getConverteNullString(lista.get(i).get(20))), // ARG 20 JUROS
                        Moeda.converteR$(getConverteNullString(lista.get(i).get(21))), // ARG 21 CORRECAO
                        getConverteNullString(lista.get(i).get(22)), // ARG 22 CAIXA
                        lista.get(i).get(24), // ARG 23 DOCUMENTO
                        Moeda.converteR$(getConverteNullString(lista.get(i).get(7))), // ARG 24 VALOR CALCULADO ORIGINAL
                        disabled,
                        //lista.get(i).get(18), // ARG 26 ID_BAIXA
                        lista.get(i).get(23), // ARG 26 ID_BAIXA
                        lista.get(i).get(15), // ARG 27 ID_LOTE
                        (!descPesquisaBoleto.isEmpty() && descPesquisaBoleto.equals(lista.get(i).get(17))) ? "tblListaBoleto" : "" // BOLETO PESQUISADO -- ARG 28
                        //(!descPesquisaBoleto.isEmpty() && descPesquisaBoleto == lista.get(i).get(17)) ? "tblListaBoleto" : "" // BOLETO PESQUISADO -- ARG 28
                )
                );
            }
        }
        return listaMovimento;
    }

    public void setListaMovimento(List<DataObject> listaMovimento) {
        this.listaMovimento = listaMovimento;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getBoleto() {
        return boleto;
    }

    public void setBoleto(String boleto) {
        this.boleto = boleto;
    }

    public String getDiasAtraso() {
        return diasAtraso;
    }

    public void setDiasAtraso(String diasAtraso) {
        this.diasAtraso = diasAtraso;
    }

    public String getMulta() {
        return multa;
    }

    public void setMulta(String multa) {
        this.multa = multa;
    }

    public String getJuros() {
        return juros;
    }

    public void setJuros(String juros) {
        this.juros = juros;
    }

    public String getCorrecao() {
        return correcao;
    }

    public void setCorrecao(String correcao) {
        this.correcao = correcao;
    }

    public String getCaixa() {
        return caixa;
    }

    public void setCaixa(String caixa) {
        this.caixa = caixa;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getDesconto() {
        if (desconto.isEmpty()) {
            desconto = "0";
        }
        return Moeda.converteR$(desconto);
    }

    public void setDesconto(String desconto) {
        if (desconto.isEmpty()) {
            desconto = "0";
        }
        this.desconto = Moeda.substituiVirgula(desconto);
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public boolean isChkSeleciona() {
        return chkSeleciona;
    }

    public void setChkSeleciona(boolean chkSeleciona) {
        this.chkSeleciona = chkSeleciona;
    }

    public void adicionarPesquisa() {
        addMais = true;
        //return "movimentosReceberSocial";
    }

    public Pessoa getPessoa() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa") != null) {
            if (!addMais) {
                pessoa = new Pessoa();
                pessoa = (Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa");

                listaPessoa.clear();

                listaPessoa.add(pessoa);
                listaMovimento.clear();
            } else {
                listaPessoa.add((Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa"));
                listaMovimento.clear();
                addMais = false;
            }
            calculoDesconto();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        }
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public List<Pessoa> getListaPessoa() {
        return listaPessoa;
    }

    public void setListaPessoa(List<Pessoa> listaPessoa) {
        this.listaPessoa = listaPessoa;
    }

    public String getConverteNullString(Object object) {
        if (object == null) {
            return "";
        } else {
            return String.valueOf(object);
        }
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getId_baixa() {
        return id_baixa;
    }

    public void setId_baixa(String id_baixa) {
        this.id_baixa = id_baixa;
    }

    public String getDescPesquisaBoleto() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa") != null) {
            descPesquisaBoleto = "";
        }
        return descPesquisaBoleto;
    }

    public void setDescPesquisaBoleto(String descPesquisaBoleto) {
        this.descPesquisaBoleto = descPesquisaBoleto;
    }

    public void setListaContas(List<SelectItem> listaContas) {
        this.listaContas = listaContas;
    }

    public int getIndexConta() {
        return indexConta;
    }

    public void setIndexConta(int indexConta) {
        this.indexConta = indexConta;
    }
}
