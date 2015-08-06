package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.dao.ExtratoTelaSocialDao;
import br.com.rtools.associativo.db.MovimentosReceberSocialDB;
import br.com.rtools.associativo.db.MovimentosReceberSocialDBToplink;
import br.com.rtools.financeiro.Boleto;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.TipoServico;
import br.com.rtools.financeiro.db.TipoServicoDB;
import br.com.rtools.financeiro.db.TipoServicoDBToplink;
import br.com.rtools.movimento.GerarMovimento;
import br.com.rtools.movimento.ImprimirBoleto;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ControleAcessoBean;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.sistema.Email;
import br.com.rtools.sistema.EmailPessoa;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Mail;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.PF;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;

@ManagedBean
@SessionScoped
public class ExtratoTelaSocialBean implements Serializable {

    private String porPesquisa = "todos";
    private String ordenacao = "referencia";
    private String tipoDataPesquisa = "vencimento";
    private String tipoPessoa = "nenhum";
    private List<DataObject> listaMovimento = new ArrayList();
    private Pessoa pessoa = new Pessoa();
    private String dataInicial = "";
    private String dataFinal = "";
    private String dataRefInicial = "";
    private String dataRefFinal = "";
    private String boletoInicial = "";
    private String boletoFinal = "";
    private Integer idServicos = 0;
    private List<SelectItem> listaServicos = new ArrayList();
    private Integer idTipoServico = 0;
    private List<SelectItem> listaTipoServico = new ArrayList();
    private boolean imprimirVerso = false;
    private String tipoEnvio = "responsavel";
    private String historico = "";
    private String vlRecebido = "0,00";
    private String vlNaoRecebido = "0,00";
    private String vlTaxa = "0,00";
    private String vlTotal = "0,00";
    private String vlLiquido = "0,00";
    private ControleAcessoBean cab = new ControleAcessoBean();

    private String motivoEstorno = "";
    
    @PostConstruct
    public void init() {
        loadListaServicos();
        loadListaTipoServico();
        
        cab = (ControleAcessoBean) GenericaSessao.getObject("controleAcessoBean");
        
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("extratoTelaSocialBean");
    }

    public void loadLista() {
        ExtratoTelaSocialDao dao = new ExtratoTelaSocialDao();

        listaMovimento.clear();
        vlRecebido = "0,00";
        vlNaoRecebido = "0,00";
        vlTaxa = "0,00";
        vlTotal = "0,00";
        vlLiquido = "0,00";
        
        if (dataInicial.isEmpty() && dataFinal.isEmpty() && dataRefInicial.isEmpty() && dataRefFinal.isEmpty() && boletoInicial.isEmpty() && boletoFinal.isEmpty() && pessoa.getId() == -1 && Integer.valueOf(listaServicos.get(idServicos).getDescription()) == 0 && Integer.valueOf(listaTipoServico.get(idTipoServico).getDescription()) == 0){
            GenericaMensagem.warn("Atenção", "Selecione algum filtro para não travar com muitos resultados!");
            return;
        }
        
        if (!tipoPessoa.equals("nenhum") && pessoa.getId() == -1)
            return;

        List<Vector> result = dao.listaMovimentosSocial(
                porPesquisa, ordenacao, tipoDataPesquisa, dataInicial, dataFinal, dataRefInicial, dataRefFinal, boletoInicial, boletoFinal, tipoPessoa, pessoa.getId(), Integer.valueOf(listaServicos.get(idServicos).getDescription()), Integer.valueOf(listaTipoServico.get(idTipoServico).getDescription())
        );

        float valor = 0, valor_baixa = 0, valor_taxa = 0;
        for (Vector vector : result) {
            listaMovimento.add(
                    new DataObject(
                            false,
                            vector,
                            vector.get(0) // id_movimento
                    )
            );

            // SE id_baixa FOR DIFERENTE DE NULL
            if (vector.get(14) != null) {

                valor_baixa = Float.parseFloat(Double.toString((Double) vector.get(12)));
                valor_taxa = Float.parseFloat(Double.toString((Double) vector.get(13)));

                vlRecebido = Moeda.converteR$Float(Moeda.somaValores(valor_baixa, Moeda.converteUS$(vlRecebido)));
                vlTaxa = Moeda.converteR$Float(Moeda.somaValores(valor_taxa, Moeda.converteUS$(vlTaxa)));
            } else {
                valor = Float.parseFloat(Double.toString((Double) vector.get(10)));
                vlNaoRecebido = Moeda.converteR$Float(Moeda.somaValores(valor, Moeda.converteUS$(vlNaoRecebido)));
            }

            vlTotal = Moeda.converteR$Float(Moeda.somaValores(valor_baixa, Moeda.converteUS$(vlTotal)));

            float contaLiquido = Moeda.subtracaoValores(valor_baixa, valor_taxa);
            vlLiquido = Moeda.converteR$Float(Moeda.somaValores(contaLiquido, Moeda.converteUS$(vlLiquido)));
        }

    }

    public void imprimir() {
        List<Boleto> listab = new ArrayList();
        Map<Integer, Boleto> hash = new LinkedHashMap();

        for (DataObject datao : listaMovimento) {
            if ((Boolean) datao.getArgumento0() && ((Vector) datao.getArgumento1()).get(15) != null) {
                Boleto boletox = (Boleto) new Dao().find(new Boleto(), ((Vector) datao.getArgumento1()).get(15));

                if (boletox.getNrCtrBoleto().isEmpty()) {
                    GenericaMensagem.fatal("Atenção", "Boleto " + boletox.getNrBoleto() + " sem NrCtrBoleto!, Contate o administrador!");
                    return;
                }

                hash.put(boletox.getId(), boletox);
            }
        }

        for (Map.Entry<Integer, Boleto> entry : hash.entrySet()) {
            listab.add(entry.getValue());
        }

        if (!listab.isEmpty()) {
            ImprimirBoleto ib = new ImprimirBoleto();
            ib.imprimirBoletoSocial(listab, imprimirVerso);
            ib.visualizar(null);
        }
    }

    public void imprimirPlanilha() {

    }

    public void imprimirPromissoria() {

    }

    public void inativarBoleto() {
        if (historico.isEmpty()) {
            GenericaMensagem.warn("Atenção", "Digite um motivo para exclusão!");
            return;
        } else if (historico.length() < 6) {
            GenericaMensagem.warn("Atenção", "Motivo de exclusão inválido!");
            return;
        }

        List<Movimento> listam = new ArrayList();

        if (baixado()) {
            GenericaMensagem.warn("Atenção", "Boletos BAIXADOS não podem ser excluídos!");
            return;
        }

        if (fechadosCaixa()) {
            GenericaMensagem.warn("Atenção", "Boletos COM CAIXA FECHADO não podem ser estornados!");
            return;
        }

        if (acordados()) {
            GenericaMensagem.warn("Atenção", "Boletos do tipo ACORDO não podem ser excluídos!");
            return;
        }

        for (DataObject dh : listaMovimento) {
            if ((Boolean) dh.getArgumento0()) {
                int id_movimento = Integer.valueOf(((Vector) dh.getArgumento1()).get(0).toString());
                Movimento mov = (Movimento) new Dao().find(new Movimento(), id_movimento);
                listam.add(mov);
            }
        }

        if (listam.isEmpty()) {
            GenericaMensagem.warn("Atenção", "Nenhum boletos foi selecionado!");
            return;
        }

        Dao dao = new Dao();
        dao.openTransaction();

        if (!GerarMovimento.inativarArrayMovimento(listam, historico, dao).isEmpty()) {
            GenericaMensagem.error("Atenção", "Ocorreu um erro em uma das exclusões, verifique o log!");
            dao.rollback();
            return;
        } else {
            GenericaMensagem.info("Sucesso", "Boletos foram excluídos!");
        }

        dao.commit();

        loadLista();

        PF.update("formExtratoTelaSocial");
        PF.closeDialog("dlg_excluir");
    }

    public void estornarBaixa() {
        if (listaMovimento.isEmpty()) {
            GenericaMensagem.warn("Atenção", "Não existem boletos para serem estornados!");
            return;
        }

        int qnt = 0;
        Movimento mov = null;

        for (DataObject listaMovimento1 : listaMovimento) {
            if ((Boolean) listaMovimento1.getArgumento0()) {
                qnt++;
                mov = (Movimento) new Dao().find(new Movimento(), listaMovimento1.getArgumento2());
            }
        }

        if (qnt == 0) {
            GenericaMensagem.warn("Atenção", "Nenhum Movimento selecionado!");
            return;
        }

        if (qnt > 1) {
            GenericaMensagem.warn("Erro", "Mais de um movimento foi selecionado!");
            return;
        }

        if (!baixado()) {
            GenericaMensagem.warn("Atenção", "Existem boletos que não foram pagos para estornar!");
            return;
        }

        if (fechadosCaixa()) {
            GenericaMensagem.warn("Atenção", "Boletos COM CAIXA FECHADO não podem ser estornados!");
            return;
        }

        boolean est = true;

        if (!mov.isAtivo()) {
            GenericaMensagem.warn("Atenção", "Boleto ID: " + mov.getId() + " esta inativo, não é possivel concluir estorno!");
            return;
        }

        if (motivoEstorno.isEmpty() || motivoEstorno.length() <= 5){
            GenericaMensagem.error("Atenção", "Motivo de Estorno INVÁLIDO!");
            return;
        }        
        
        if (mov.getLote().getRotina() != null && mov.getLote().getRotina().getId() == 132) {
            mov.setAtivo(false);
        }
        
        if (!GerarMovimento.estornarMovimento(mov, motivoEstorno)) {
            est = false;
        }
        
        
        if (!est) {
            GenericaMensagem.warn("Atenção", "Ocorreu erros ao estornar boletos, verifique o log!");
        } else {
            GenericaMensagem.info("Sucesso", "Boletos estornados com sucesso!");
        }

        loadLista();

        PF.update("formExtratoTelaSocial");
        PF.closeDialog("dlg_estornar");
    }

    public void enviarEmail() {
        if (baixado()) {
            GenericaMensagem.warn("Atenção", "Boletos BAIXADOS não podem ser excluídos!");
            return;
        }

        if (fechadosCaixa()) {
            GenericaMensagem.warn("Atenção", "Boletos COM CAIXA FECHADO não podem ser estornados!");
            return;
        }

        List<Boleto> listab = new ArrayList();
        Map<Integer, Boleto> hash = new LinkedHashMap();

        for (DataObject datao : listaMovimento) {
            if ((Boolean) datao.getArgumento0() && ((Vector) datao.getArgumento1()).get(15) != null) {
                Boleto boletox = (Boleto) new Dao().find(new Boleto(), ((Vector) datao.getArgumento1()).get(15));

                if (boletox.getNrCtrBoleto().isEmpty()) {
                    GenericaMensagem.fatal("Atenção", "Boleto " + boletox.getNrBoleto() + " sem NrCtrBoleto!, Contate o administrador!");
                    return;
                }

                hash.put(boletox.getId(), boletox);
            }
        }

        for (Map.Entry<Integer, Boleto> entry : hash.entrySet()) {
            listab.add(entry.getValue());
        }

        for(Boleto bol : listab){
         
            try {
                Registro reg = (Registro) new Dao().find(new Registro(), 1);

                ImprimirBoleto ib = new ImprimirBoleto();
                ib.imprimirBoletoSocial(bol, false);
                ib.setPathPasta(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/boletos"));
                Pessoa pessoa_envio = null;
                MovimentosReceberSocialDB dbs = new MovimentosReceberSocialDBToplink();

                if (tipoEnvio.equals("responsavel")){
                    pessoa_envio = dbs.responsavelBoleto(bol.getNrCtrBoleto());
                }

                String nome = ib.criarLink(pessoa_envio, reg.getUrlPath() + "/Sindical/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/boletos");
                List<Pessoa> pessoas = new ArrayList();
                pessoas.add(pessoa_envio);

                String mensagem = "";
                List<File> fls = new ArrayList();
                String nome_envio = "";
//                if (mov.size() == 1) {
//                    nome_envio = "Boleto " + mov.get(0).getServicos().getDescricao() + " N° " + mov.get(0).getDocumento();
//                } else {
                    nome_envio = "Boleto Associativo";
//                }

                if (!reg.isEnviarEmailAnexo()) {
                    mensagem = " <div style='background:#00ccff; padding: 15px; font-size:13pt'>Enviado para <b>" + pessoa_envio.getNome() + " </b></div><br />"
                            + " <h5>Visualize seu boleto clicando no link abaixo</h5><br /><br />"
                            + " <a href='" + reg.getUrlPath() + "/Sindical/acessoLinks.jsf?cliente=" + ControleUsuarioBean.getCliente() + "&amp;arquivo=" + nome + "'>Clique aqui para abrir boleto</a><br />";
                } else {
                    fls.add(new File(ib.getPathPasta() + "/" + nome));
                    mensagem = " <div style='background:#00ccff; padding: 15px; font-size:13pt'>Enviado para <b>" + pessoa_envio.getNome() + " </b></div><br />"
                            + " <h5>Segue boleto em anexo</h5><br /><br />";
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
                                (Rotina) di.find(new Rotina(), 96),
                                null,
                                nome_envio,
                                mensagem,
                                false,
                                false
                        )
                );

                List<EmailPessoa> emailPessoas = new ArrayList();
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
                //String[] retorno = new String[2];

                if (!retorno[1].isEmpty()) {
                    GenericaMensagem.warn("Erro", retorno[1]);
                } else {
                    GenericaMensagem.info("Sucesso", retorno[0]);
                }
            } catch (Exception erro) {
                System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());

            }
        }
    }

    public void excluirAcordo() {

    }

    public void loadListaServicos() {
        listaServicos.clear();

//        ServicosDB db = new ServicosDBToplink();
        List<Servicos> select = new ExtratoTelaSocialDao().listaServicosAssociativo();

        listaServicos.add(new SelectItem(0, "-- Selecione um Serviço --", "0"));
        for (int i = 0; i < select.size(); i++) {
            listaServicos.add(new SelectItem(
                    i + 1,
                    select.get(i).getDescricao(),
                    Integer.toString(select.get(i).getId())
            ));
        }
    }

    public void loadListaTipoServico() {
        listaTipoServico.clear();

        TipoServicoDB db = new TipoServicoDBToplink();
        List<TipoServico> select = db.pesquisaTodos();

        listaTipoServico.add(new SelectItem(0, "-- Selecione um Tipo --", "0"));
        for (int i = 0; i < select.size(); i++) {
            listaTipoServico.add(new SelectItem(
                    i + 1,
                    select.get(i).getDescricao(),
                    Integer.toString(select.get(i).getId())
            ));
        }
    }

    public void removerPessoa() {
        pessoa = new Pessoa();
        loadLista();
    }

    public void limparDatas() {
        if (tipoDataPesquisa.equals("referencia")) {
            dataInicial = "";
            dataFinal = "";
        } else {
            dataRefInicial = "";
            dataRefFinal = "";
        }
    }

    public String converteData(Date data) {
        return DataHoje.converteData(data);
    }

    public String converteValor(String valor) {
        return Moeda.converteR$(valor);
    }

    public boolean baixado() {
        for (DataObject listaMovimento1 : listaMovimento) {
            if (((Boolean) listaMovimento1.getArgumento0()) && ((Vector) listaMovimento1.getArgumento1()).get(14) != null) {
                return true;
            }
        }
        return false;
    }

    public boolean acordados() {
        for (DataObject listaMovimento1 : listaMovimento) {
            if ((Boolean) listaMovimento1.getArgumento0() && String.valueOf(((Vector) listaMovimento1.getArgumento1()).get(7)).equals("Acordo")) {
                return true;
            }
        }
        return false;
    }

    public boolean fechadosCaixa() {
        for (DataObject listaMovimento1 : listaMovimento) {
            if ((Boolean) listaMovimento1.getArgumento0()) {
                Movimento mov = (Movimento) new Dao().find(new Movimento(), listaMovimento1.getArgumento2());
                if (mov.getBaixa() != null && mov.getBaixa().getFechamentoCaixa() != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getQntBoletos() {
        String n;
        if (!listaMovimento.isEmpty()) {
            n = Integer.toString(listaMovimento.size()) + ((listaMovimento.size() == 15000) ? " limite" : "");
        } else {
            n = "0";
        }
        return n;
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public List<DataObject> getListaMovimento() {
        return listaMovimento;
    }

    public void setListaMovimento(List<DataObject> listaMovimento) {
        this.listaMovimento = listaMovimento;
    }

    public Pessoa getPessoa() {
        if (GenericaSessao.exists("pessoaPesquisa")) {
            pessoa = (Pessoa) GenericaSessao.getObject("pessoaPesquisa", true);
            loadLista();
        }
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getOrdenacao() {
        return ordenacao;
    }

    public void setOrdenacao(String ordenacao) {
        this.ordenacao = ordenacao;
    }

    public String getTipoDataPesquisa() {
        return tipoDataPesquisa;
    }

    public void setTipoDataPesquisa(String tipoDataPesquisa) {
        this.tipoDataPesquisa = tipoDataPesquisa;
    }

    public String getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(String dataInicial) {
        this.dataInicial = dataInicial;
    }

    public String getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(String dataFinal) {
        this.dataFinal = dataFinal;
    }

    public String getDataRefInicial() {
        return dataRefInicial;
    }

    public void setDataRefInicial(String dataRefInicial) {
        this.dataRefInicial = dataRefInicial;
    }

    public String getDataRefFinal() {
        return dataRefFinal;
    }

    public void setDataRefFinal(String dataRefFinal) {
        this.dataRefFinal = dataRefFinal;
    }

    public String getBoletoInicial() {
        return boletoInicial;
    }

    public void setBoletoInicial(String boletoInicial) {
        this.boletoInicial = boletoInicial;
    }

    public String getBoletoFinal() {
        return boletoFinal;
    }

    public void setBoletoFinal(String boletoFinal) {
        this.boletoFinal = boletoFinal;
    }

    public Integer getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(Integer idServicos) {
        this.idServicos = idServicos;
    }

    public List<SelectItem> getListaServicos() {
        return listaServicos;
    }

    public void setListaServicos(List<SelectItem> listaServicos) {
        this.listaServicos = listaServicos;
    }

    public Integer getIdTipoServico() {
        return idTipoServico;
    }

    public void setIdTipoServico(Integer idTipoServico) {
        this.idTipoServico = idTipoServico;
    }

    public List<SelectItem> getListaTipoServico() {
        return listaTipoServico;
    }

    public void setListaTipoServico(List<SelectItem> listaTipoServico) {
        this.listaTipoServico = listaTipoServico;
    }

    public boolean isImprimirVerso() {
        return imprimirVerso;
    }

    public void setImprimirVerso(boolean imprimirVerso) {
        this.imprimirVerso = imprimirVerso;
    }

    public String getTipoEnvio() {
        return tipoEnvio;
    }

    public void setTipoEnvio(String tipoEnvio) {
        this.tipoEnvio = tipoEnvio;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }

    public String getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(String tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public String getVlRecebido() {
        return vlRecebido;
    }

    public void setVlRecebido(String vlRecebido) {
        this.vlRecebido = vlRecebido;
    }

    public String getVlNaoRecebido() {
        return vlNaoRecebido;
    }

    public void setVlNaoRecebido(String vlNaoRecebido) {
        this.vlNaoRecebido = vlNaoRecebido;
    }

    public String getVlTaxa() {
        return vlTaxa;
    }

    public void setVlTaxa(String vlTaxa) {
        this.vlTaxa = vlTaxa;
    }

    public String getVlTotal() {
        return vlTotal;
    }

    public void setVlTotal(String vlTotal) {
        this.vlTotal = vlTotal;
    }

    public String getVlLiquido() {
        return vlLiquido;
    }

    public void setVlLiquido(String vlLiquido) {
        this.vlLiquido = vlLiquido;
    }

    public ControleAcessoBean getCab() {
        return cab;
    }

    public void setCab(ControleAcessoBean cab) {
        this.cab = cab;
    }

    public String getMotivoEstorno() {
        return motivoEstorno;
    }

    public void setMotivoEstorno(String motivoEstorno) {
        this.motivoEstorno = motivoEstorno;
    }
}
