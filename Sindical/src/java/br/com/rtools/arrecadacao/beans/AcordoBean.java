package br.com.rtools.arrecadacao.beans;

import br.com.rtools.arrecadacao.Acordo;
import br.com.rtools.arrecadacao.db.AcordoDB;
import br.com.rtools.arrecadacao.db.AcordoDBToplink;
import br.com.rtools.associativo.beans.MovimentosReceberSocialBean;
import br.com.rtools.financeiro.ContaCobranca;
import br.com.rtools.financeiro.Historico;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.TipoServico;
import br.com.rtools.financeiro.beans.MovimentosReceberBean;
import br.com.rtools.financeiro.db.ContaCobrancaDB;
import br.com.rtools.financeiro.db.ContaCobrancaDBToplink;
import br.com.rtools.financeiro.db.FTipoDocumentoDB;
import br.com.rtools.financeiro.db.FTipoDocumentoDBToplink;
import br.com.rtools.financeiro.db.MovimentoDB;
import br.com.rtools.financeiro.db.MovimentoDBToplink;
import br.com.rtools.financeiro.db.TipoServicoDB;
import br.com.rtools.financeiro.db.TipoServicoDBToplink;
import br.com.rtools.movimento.GerarMovimento;
import br.com.rtools.movimento.ImprimirBoleto;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.db.FilialDB;
import br.com.rtools.pessoa.db.FilialDao;
import br.com.rtools.pessoa.db.JuridicaDB;
import br.com.rtools.pessoa.db.JuridicaDBToplink;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.EnviarEmail;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class AcordoBean implements Serializable {

    private List<DataObject> listaVizualizado = new ArrayList();
    private List<DataObject> listaOperado = new ArrayList();
    private Acordo acordo = new Acordo();
    private int idServicos = 0;
    private int idVencimento = 0;
    private int idVencimentoSind = 0;
    private int parcela = 1;
    private float total = 0;
    private String valorEntrada = "0";
    private String valorEntradaSind = "0";
    private String vencimento = DataHoje.data();
    private int frequencia = 30;
    private int frequenciaSind = 30;
    private String totalSindical = "0";
    private String totalOutras = "0";
    private List<int[]> quantidade = new ArrayList<int[]>();
    List<Boolean> listaMarcados = new ArrayList<Boolean>();
    private String ultimaData = "";
    //private String mensagem = "";
    private boolean imprimeVerso = false;
    private Historico historico = new Historico();
    private boolean imprimir = true;
    private boolean imprimir_pro = false;
    private List<Movimento> listaMovs = new ArrayList();
    private Pessoa pessoa = new Pessoa();
    private Pessoa pessoaEnvio = new Pessoa();
    private String emailPara = "contabilidade";

    private String emailContato = "";

    public String converteValorString(String valor) {
        return Moeda.converteR$(valor);
    }

    public void verificaEmail() {
        Juridica jur = new Juridica();
        JuridicaDB db = new JuridicaDBToplink();
        jur = db.pesquisaJuridicaPorPessoa(pessoa.getId());
        if (pessoaEnvio.getEmail1().isEmpty()) {
            GenericaMensagem.warn("Atenção", "Digite um email válido!");
            pessoaEnvio = new Pessoa();
            return;
        }

        if (emailPara.equals("contabilidade")) {
            if (jur.getContabilidade() == null) {
                GenericaMensagem.warn("Atenção", "Empresa sem contabilidade vinculada!");
                pessoaEnvio = new Pessoa();
                return;
            }

            if (!jur.getContabilidade().getPessoa().getEmail1().isEmpty() && !pessoaEnvio.getEmail1().isEmpty()) {
                if (jur.getContabilidade().getPessoa().getEmail1().equals(pessoaEnvio.getEmail1())) {
                    pessoaEnvio = jur.getContabilidade().getPessoa();
                } else {
                    SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
                    jur.getContabilidade().getPessoa().setEmail1(pessoaEnvio.getEmail1());
                    sv.abrirTransacao();

                    if (sv.alterarObjeto(jur.getContabilidade().getPessoa())) {
                        sv.comitarTransacao();
                    } else {
                        sv.desfazerTransacao();
                    }

                    pessoaEnvio = jur.getContabilidade().getPessoa();
                }
            } else if (!jur.getContabilidade().getPessoa().getEmail1().isEmpty()) {
                pessoaEnvio = jur.getContabilidade().getPessoa();
            } else if (!pessoaEnvio.getEmail1().isEmpty()) {
                SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
                jur.getContabilidade().getPessoa().setEmail1(pessoaEnvio.getEmail1());
                sv.abrirTransacao();

                if (sv.alterarObjeto(jur.getContabilidade().getPessoa())) {
                    sv.comitarTransacao();
                } else {
                    sv.desfazerTransacao();
                }

                pessoaEnvio = jur.getContabilidade().getPessoa();
            } else {
                GenericaMensagem.warn("Atenção", "Digite um email válido!");
                pessoaEnvio = new Pessoa();
            }
        } else {
            if (!jur.getPessoa().getEmail1().isEmpty() && !pessoaEnvio.getEmail1().isEmpty()) {
                if (jur.getPessoa().getEmail1().equals(pessoaEnvio.getEmail1())) {
                    pessoaEnvio = jur.getPessoa();
                } else {
                    SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
                    jur.getPessoa().setEmail1(pessoaEnvio.getEmail1());
                    sv.abrirTransacao();

                    if (sv.alterarObjeto(jur.getPessoa())) {
                        sv.comitarTransacao();
                    } else {
                        sv.desfazerTransacao();
                    }

                    pessoaEnvio = jur.getPessoa();
                }
            } else if (!jur.getPessoa().getEmail1().isEmpty()) {
                pessoaEnvio = jur.getPessoa();
            } else if (!pessoaEnvio.getEmail1().isEmpty()) {
                SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
                jur.getPessoa().setEmail1(pessoaEnvio.getEmail1());
                sv.abrirTransacao();

                if (sv.alterarObjeto(jur.getPessoa())) {
                    sv.comitarTransacao();
                } else {
                    sv.desfazerTransacao();
                }

                pessoaEnvio = jur.getPessoa();
            } else {
                GenericaMensagem.warn("Atenção", "Digite um email válido!");
                pessoaEnvio = new Pessoa();
            }
        }
    }

    public String enviarEmail() {
        List<Movimento> listaImp = new ArrayList();
        List<Float> listaValores = new ArrayList<Float>();
        List<String> listaVencimentos = new ArrayList<String>();
        Registro reg = new Registro();

        for (int i = 0; i < listaOperado.size(); i++) {
            listaImp.add(((Movimento) listaOperado.get(i).getArgumento2()));
            listaValores.add(((Movimento) listaOperado.get(i).getArgumento2()).getValor());
            listaVencimentos.add(((Movimento) listaOperado.get(i).getArgumento2()).getVencimento());
        }
        verificaEmail();
        if (!listaImp.isEmpty() && pessoaEnvio.getId() != -1) {
            for (int i = 0; i < listaImp.size(); i++) {
                ImprimirBoleto imp = new ImprimirBoleto();
                imp.imprimirBoleto(listaImp, listaValores, listaVencimentos, imprimeVerso);
                String patch = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos");
                if (!new File(patch + "/downloads").exists()) {
                    File file = new File(patch + "/downloads");
                    file.mkdir();
                }
                if (!new File(patch + "/downloads/boletos").exists()) {
                    File file = new File(patch + "/downloads/boletos");
                    file.mkdir();
                }
                String nome = imp.criarLink(listaImp.get(i).getPessoa(), reg.getUrlPath() + "/Sindical/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/boletos");

                reg = (Registro) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(1, "Registro");
                List<Pessoa> p = new ArrayList();
                p.add(pessoaEnvio);

                String[] ret = new String[2];
                String nome_envio = "";
                if (listaImp.size() == 1) {
                    nome_envio = "Boleto " + listaImp.get(0).getServicos().getDescricao() + " N° " + listaImp.get(0).getDocumento();
                } else {
                    nome_envio = "Boleto";
                }

                if (!reg.isEnviarEmailAnexo()) {
                    ret = EnviarEmail.EnviarEmailPersonalizado(
                            reg,
                            p,
                            " <h5>Visualize seu boleto clicando no link abaixo</5><br /><br />"
                            + " <a href='" + reg.getUrlPath() + "/Sindical/acessoLinks.jsf?cliente=" + ControleUsuarioBean.getCliente() + "&amp;arquivo=" + nome + "' target='_blank'>Clique aqui para abrir boleto</a><br />",
                            new ArrayList(),
                            nome_envio
                    );
                } else {
                    List<File> fls = new ArrayList<File>();
                    fls.add(new File(imp.getPathPasta() + "/" + nome));

                    ret = EnviarEmail.EnviarEmailPersonalizado(
                            reg,
                            p,
                            " <h5>Baixe seu boleto anexado neste email</5><br /><br />",
                            fls,
                            nome_envio
                    );
                }
                if (!ret[1].isEmpty()) {
                    GenericaMensagem.warn("Atenção", ret[1]);
                } else {
                    GenericaMensagem.info("OK", ret[0]);

                }
                listaImp.clear();
                p.clear();
            }
        }
        return null;
    }

    public List<SelectItem> getListaVencimento() {
        List<SelectItem> vencto = new Vector<SelectItem>();
        int i = 0;
        DataHoje data = new DataHoje();
        vencto.add(new SelectItem(
                i,
                vencimento)
        );
        i++;
        while (i < 31) {
            vencto.add(new SelectItem(
                    i,
                    data.incrementarDias(i, vencimento))
            );
            i++;
        }
        return vencto;
    }

    public void imprimirAcordo() {
//        if ((!opMovimentos.getListaAcordo().isEmpty()) && (mensagem.equals("Imprimir Boletos"))){
//            OperacaoMovimento opAcordo = new OperacaoMovimento(opMovimentos.getListaAcordo());
//            opAcordo.imprimirBoleto(imprimeVerso);
//        }
    }

    public List getListaFolha() {
        AcordoDB dbac = new AcordoDBToplink();
        List listaFolha = new ArrayList();
        listaFolha = dbac.pesquisaTodasFolhas();
        return listaFolha;
    }

    public void refreshForm() {
    }

    public List<DataObject> getListaVizualizado() {
        if (listaVizualizado.isEmpty() && !listaMovs.isEmpty() && pessoa.getId() != -1) {
            historico.setHistorico("Acordo correspondente a: ");
            List<DataObject> aux = new ArrayList();
            aux.add(null);
            aux.add(null);
            aux.add(null);
            aux.add(null);
            float soma_sind = 0,
                    soma_assis = 0,
                    soma_conf = 0,
                    soma_neg = 0;

            String h_sind = "",
                    h_assis = "",
                    h_conf = "",
                    h_neg = "";

            for (int i = 0; i < listaMovs.size(); i++) {
                if (listaMovs.get(i).getServicos().getId() == 1) {
                    // SINDICAL
                    soma_sind = Moeda.somaValores(soma_sind, listaMovs.get(i).getValorBaixa());
                    String h_sind_boleto = "Sindical - " + listaMovs.get(i).getReferencia();
                    if (h_sind.isEmpty()) {
                        h_sind += "Sindical - " + listaMovs.get(i).getReferencia();
                    } else {
                        h_sind += ", " + listaMovs.get(i).getReferencia();
                    }

                    if (aux.get(0) != null) {
                        aux.add(new DataObject(listaMovs.get(i).getServicos(), listaMovs.get(i).getValorBaixa(), listaMovs.get(i).getReferencia(), h_sind_boleto, null, null));
                    } else {
                        aux.set(0, new DataObject(listaMovs.get(i).getServicos(), listaMovs.get(i).getValorBaixa(), listaMovs.get(i).getReferencia(), h_sind_boleto, null, null));
                    }

                    valorEntradaSind = Moeda.converteR$Float(listaMovs.get(i).getValorBaixa());
                } else if (listaMovs.get(i).getServicos().getId() == 2) {
                    // ASSISTENCIAL
                    soma_assis = Moeda.somaValores(soma_assis, listaMovs.get(i).getValorBaixa());
                    if (h_assis.isEmpty()) {
                        h_assis += "Assistencial - " + listaMovs.get(i).getReferencia();
                    } else {
                        h_assis += ", " + listaMovs.get(i).getReferencia();
                    }

                    aux.set(1, new DataObject(listaMovs.get(i).getServicos(), soma_assis, null, h_assis, null, null));

                } else if (listaMovs.get(i).getServicos().getId() == 3) {
                    // CONFEDERATIVA
                    soma_conf = Moeda.somaValores(soma_conf, listaMovs.get(i).getValorBaixa());
                    if (h_conf.isEmpty()) {
                        h_conf += "Confederativa - " + listaMovs.get(i).getReferencia();
                    } else {
                        h_conf += ", " + listaMovs.get(i).getReferencia();
                    }

                    aux.set(2, new DataObject(listaMovs.get(i).getServicos(), soma_conf, null, h_conf, null, null));

                } else if (listaMovs.get(i).getServicos().getId() == 4) {
                    // NEGOCIAL
                    soma_neg = Moeda.somaValores(soma_neg, listaMovs.get(i).getValorBaixa());
                    if (h_neg.isEmpty()) {
                        h_neg += "Negocial - " + listaMovs.get(i).getReferencia();
                    } else {
                        h_neg += ", " + listaMovs.get(i).getReferencia();
                    }

                    aux.set(3, new DataObject(listaMovs.get(i).getServicos(), soma_neg, null, h_neg, null, null));
                }
            }

            historico.setHistorico(historico.getHistorico() + h_sind + " " + h_assis + " " + h_conf + " " + h_neg);
            totalSindical = Moeda.converteR$Float(soma_sind);
            float soma_total = Moeda.somaValores(Moeda.somaValores(soma_assis, soma_conf), soma_neg);
            totalOutras = Moeda.converteR$Float(soma_total);
            total = Moeda.somaValores(soma_sind, soma_total);
            for (int i = 0; i < aux.size(); i++) {
                if (aux.get(i) != null) {
                    listaVizualizado.add(new DataObject(aux.get(i).getArgumento0(), Moeda.converteR$Float((Float) aux.get(i).getArgumento1()), aux.get(i).getArgumento2(), (String) aux.get(i).getArgumento3(), null, null));
                }
            }
        }
        return listaVizualizado;
    }

    public synchronized void efetuarAcordo() {
        if (listaOperado.isEmpty()) {
            GenericaMensagem.error("Atenção", "Acordo não foi gerado!");
            return;
        }
        List<Movimento> listaAcordo = new ArrayList();
        List<String> listaHistorico = new ArrayList();

        for (DataObject listaOperado1 : listaOperado) {
            listaAcordo.add((Movimento) listaOperado1.getArgumento2());
            listaHistorico.add((String) listaOperado1.getArgumento3());
        }

        try {
            // 07-11-2011 dep arrecad. secrp rogerio afirmou que o nr_ctr_boleto dos acordados tem que ser zerados,
            // para que nao haja conflito com os novos boletos gerados (* (nr_num_documento, nr_ctr_boleto, id_conta_cobranca) *)
            String mensagem = GerarMovimento.salvarListaAcordo(acordo, listaAcordo, listaMovs, listaHistorico);
            if (mensagem.isEmpty()) {
                GenericaMensagem.info("Sucesso", "Acordo Concluído!");
            }

            imprimir = false;

            String url = (String) GenericaSessao.getString("urlRetorno");
            switch (url) {
                case "movimentosReceber":
                    ((MovimentosReceberBean) GenericaSessao.getObject("movimentosReceberBean")).getListMovimentoReceber().clear();
                    ((MovimentosReceberBean) GenericaSessao.getObject("movimentosReceberBean")).setDesconto("0");
                    break;
                case "movimentosReceberSocial":
                    ((MovimentosReceberSocialBean) GenericaSessao.getObject("movimentosReceberSocialBean")).getListaMovimento().clear();
                    break;
            }
            if (!mensagem.isEmpty()) {
                GenericaMensagem.error("Atenção", mensagem);
            }
        } catch (Exception e) {
            GenericaMensagem.error("Atenção", "Acordo não foi gerado");

        }
    }

    public synchronized String subirData() {
        String vencimentoOut = getListaVencimento().get(idVencimento).getLabel();
        String vencimentoSind = getListaVencimento().get(idVencimentoSind).getLabel();
        if (listaOperado.isEmpty()) {
            return null;
        }

        int i = 0;
        int j = 0;
        List listas = new ArrayList();
        List<Integer> subLista = new ArrayList();
        DataHoje data = new DataHoje();
        String dataPrincipal = "";
        String referencia = "";
        while (i < listaOperado.size()) {
            if ((Boolean) listaOperado.get(i).getArgumento0()) {
                subLista.add(i);
            } else {
                if (!(subLista.isEmpty())) {
                    listas.add(subLista);
                    subLista = new ArrayList();
                }
                while (i < listaOperado.size()) {
                    if (listaOperado.size() > (i + 1)) {
                        if ((Boolean) listaOperado.get(i + 1).getArgumento0()) {
                            break;
                        }
                    }
                    i++;
                }
            }
            i++;
        }
        if (!(subLista.isEmpty())) {
            listas.add(subLista);
            subLista = new ArrayList();
        }
        i = 0;
        j = 0;
        String date = null;
        Servicos servico = null;
        while (i < listas.size()) {
            j = 0;
            Movimento movimento = (Movimento) listaOperado.get(((List<Integer>) listas.get(i)).get(j)).getArgumento2();
            date = movimento.getVencimento();
            servico = movimento.getServicos();
            if (servico.getId() == 1) {
                if (frequenciaSind == 30) {
                    if ((DataHoje.menorData(data.decrementarMeses(1, date), vencimentoSind))
                            && (!DataHoje.igualdadeData(data.decrementarMeses(1, date), vencimentoSind))) {
                        i++;
                        continue;
                    }
                    dataPrincipal = movimento.getVencimento();
                    dataPrincipal = data.decrementarMeses(1, dataPrincipal);
                    referencia = data.decrementarMeses(1, dataPrincipal);// AQUI
                } else if (frequenciaSind == 7) {
                    if ((DataHoje.menorData(data.decrementarSemanas(1, date), vencimentoSind))
                            && (!DataHoje.igualdadeData(data.decrementarSemanas(1, date), vencimentoSind))) {
                        i++;
                        continue;
                    }
                    dataPrincipal = movimento.getVencimento();
                    dataPrincipal = data.decrementarSemanas(1, dataPrincipal);
                    referencia = data.decrementarSemanas(1, dataPrincipal);// AQUI
                }
            } else {
                if (frequencia == 30) {
                    if ((DataHoje.menorData(data.decrementarMeses(1, date), vencimentoOut))
                            && (!DataHoje.igualdadeData(data.decrementarMeses(1, date), vencimentoOut))) {
                        i++;
                        continue;
                    }
                    dataPrincipal = movimento.getVencimento();
                    dataPrincipal = data.decrementarMeses(1, dataPrincipal);
                    referencia = data.decrementarMeses(1, dataPrincipal);
                } else if (frequencia == 7) {
                    if ((DataHoje.menorData(data.decrementarSemanas(1, date), vencimentoOut))
                            && (!DataHoje.igualdadeData(data.decrementarSemanas(1, date), vencimentoOut))) {
                        i++;
                        continue;
                    }
                    dataPrincipal = movimento.getVencimento();
                    dataPrincipal = data.decrementarSemanas(1, dataPrincipal);
                    referencia = data.decrementarSemanas(1, dataPrincipal);
                }
            }

            while (j < ((List<Integer>) listas.get(i)).size()) {
                ((Movimento) listaOperado.get(((List<Integer>) listas.get(i)).get(j)).getArgumento2()).setVencimento(dataPrincipal);
                if (movimento.getServicos().getId() != 1) {
                    ((Movimento) listaOperado.get(((List<Integer>) listas.get(i)).get(j)).getArgumento2()).setReferencia(referencia.substring(3));
                }
                j++;
            }

            i++;
        }
        BubbleSort(listaOperado);
        ordernarPorServico();
        while (i < listaOperado.size()) {
            listaOperado.get(i).setArgumento1(i + 1);
            i++;
        }
        return null;
    }

    public synchronized String descerData() {
        if (listaOperado.isEmpty()) {
            return null;
        }
        int i = 0;
        int j = 0;
        List listas = new ArrayList();
        List<Integer> subLista = new ArrayList();
        DataHoje data = new DataHoje();
        String dataPrincipal = "";
        String referencia = "";
        while (i < listaOperado.size()) {
            if ((Boolean) listaOperado.get(i).getArgumento0()) {
                subLista.add(i);
            } else {
                if (!(subLista.isEmpty())) {
                    listas.add(subLista);
                    subLista = new ArrayList();
                }
                while (i < listaOperado.size()) {
                    if (listaOperado.size() > (i + 1)) {
                        if ((Boolean) listaOperado.get(i + 1).getArgumento0()) {
                            break;
                        }
                    }
                    i++;
                }
            }
            i++;
        }
        if (!(subLista.isEmpty())) {
            listas.add(subLista);
            subLista = new ArrayList();
        }
        i = 0;
        j = 0;
        String date = null;
        Servicos servico = null;
        while (i < listas.size()) {
            j = 0;
            Movimento movimento = ((Movimento) listaOperado.get(((List<Integer>) listas.get(i)).get(j)).getArgumento2());
            date = movimento.getVencimento();
            servico = movimento.getServicos();

            if (servico.getId() == 1) {
                if (frequenciaSind == 30) {
                    if (DataHoje.maiorData(data.incrementarMeses(1, date), ultimaData)) {
                        i++;
                        continue;
                    }
                    referencia = movimento.getVencimento(); // AQUI
                    dataPrincipal = data.incrementarMeses(1, referencia);
                } else if (frequenciaSind == 7) {
                    if (DataHoje.maiorData(data.incrementarSemanas(1, date), ultimaData)) {
                        i++;
                        continue;
                    }
                    referencia = movimento.getVencimento();// AQUI
                    dataPrincipal = data.incrementarSemanas(1, referencia);
                }
            } else {
                if (frequencia == 30) {
                    if (DataHoje.maiorData(data.incrementarMeses(1, date), ultimaData)) {
                        i++;
                        continue;
                    }
                    referencia = movimento.getVencimento();
                    dataPrincipal = data.incrementarMeses(1, referencia);
                } else if (frequencia == 7) {
                    if (DataHoje.maiorData(data.incrementarSemanas(1, date), ultimaData)) {
                        i++;
                        continue;
                    }
                    referencia = movimento.getVencimento();
                    dataPrincipal = data.incrementarSemanas(1, referencia);
                }
            }
            while (j < ((List<Integer>) listas.get(i)).size()) {
                ((Movimento) listaOperado.get(((List<Integer>) listas.get(i)).get(j)).getArgumento2()).setVencimento(dataPrincipal);
                if (((Movimento) listaOperado.get(((List<Integer>) listas.get(i)).get(j)).getArgumento2()).getServicos().getId() != 1) {
                    ((Movimento) listaOperado.get(((List<Integer>) listas.get(i)).get(j)).getArgumento2()).setReferencia(referencia.substring(3));
                }
                j++;
            }
            i++;
        }
        BubbleSort(listaOperado);
        ordernarPorServico();
        i = 0;
        while (i < listaOperado.size()) {
            listaOperado.get(i).setArgumento1(i + 1);
            i++;
        }

        return null;
    }

    public synchronized void adicionarParcela() {
        try {
            FilialDB dbFil = new FilialDao();
            TipoServicoDB dbTipoServico = new TipoServicoDBToplink();
            ContaCobrancaDB ctaCobraDB = new ContaCobrancaDBToplink();
            FTipoDocumentoDB dbft = new FTipoDocumentoDBToplink();
            TipoServico tipoServico = dbTipoServico.pesquisaCodigo(4);
            DataHoje data = new DataHoje();
            int j = 0, k = 0;
            Servicos servico = null;
            ContaCobranca contaCobranca = null;
            listaOperado.clear();
            String ultimoVencimento = getListaVencimento().get(idVencimento).getLabel();
            String ultimoVencimentoSind = getListaVencimento().get(idVencimentoSind).getLabel();
            float valorTotalOutras = 0;
            float valorSwap = Moeda.substituiVirgulaFloat(valorEntrada);
            float valorTotal = Moeda.converteFloatR$Float(Moeda.substituiVirgulaFloat(totalOutras));
            float[] vetorEntrada = new float[listaVizualizado.size()];
            float pdE = Moeda.divisaoValores(valorSwap, valorTotal);
            float valorParcela = 0;
            for (int i = 0; i < listaVizualizado.size(); i++) {
                if (((Servicos) listaVizualizado.get(i).getArgumento0()).getId() != 1) {
                    vetorEntrada[i] = Moeda.substituiVirgulaFloat((String) listaVizualizado.get(i).getArgumento1());
                    if (listaVizualizado.size() > 1) {
                        vetorEntrada[i] = Moeda.converteFloatR$Float(Moeda.multiplicarValores(vetorEntrada[i], pdE));
                    } else {
                        vetorEntrada[i] = valorSwap;
                    }
                } else {
                    vetorEntrada[i] = 0;
                }
            }

            for (int i = 0; i < listaVizualizado.size(); i++) {
                servico = (Servicos) listaVizualizado.get(i).getArgumento0();
                contaCobranca = ctaCobraDB.pesquisaServicoCobranca(servico.getId(), tipoServico.getId());
                if (contaCobranca != null) {
                    if (servico.getId() != 1) {
                        ultimoVencimento = getListaVencimento().get(idVencimento).getLabel();
                        j = 0;
                        if (parcela > 1) {
                            valorTotalOutras = Moeda.substituiVirgulaFloat((String) listaVizualizado.get(i).getArgumento1());
                            valorTotalOutras = Moeda.subtracaoValores(valorTotalOutras, vetorEntrada[i]);
                            valorSwap = vetorEntrada[i];
                            valorParcela = Moeda.converteFloatR$Float(Moeda.divisaoValores(valorTotalOutras, parcela - 1));
                        } else {
                            valorSwap = Moeda.substituiVirgulaFloat((String) listaVizualizado.get(i).getArgumento1());
                        }
                        while (j < parcela) {
                            if (j != 0) {
                                if ((Moeda.subtracaoValores(valorTotalOutras, valorParcela) != 0) && ((j + 1) == parcela)) {
                                    valorParcela = valorTotalOutras;
                                } else {
                                    valorTotalOutras = Moeda.subtracaoValores(valorTotalOutras, valorParcela);
                                }
                                valorSwap = valorParcela;
                            }

                            Movimento mov = new Movimento(-1,
                                    null,
                                    servico.getPlano5(),
                                    pessoa,
                                    servico,
                                    null,
                                    tipoServico,
                                    null,
                                    valorSwap,
                                    referencia(ultimoVencimento),
                                    ultimoVencimento,
                                    1,
                                    true,
                                    "E",
                                    false,
                                    pessoa,
                                    pessoa,
                                    "",
                                    "",
                                    ultimoVencimento,
                                    0,
                                    0, 0, 0, 0, 0, 0, dbft.pesquisaCodigo(2), 0, null);

                            listaOperado.add(new DataObject(false, ++k, mov, (String) listaVizualizado.get(i).getArgumento3(), null, null));

                            if (j == 0) {
                                ultimoVencimento = acordo.getData();
                            }

                            if (frequencia == 30) {
                                ultimoVencimento = data.incrementarMeses(1, ultimoVencimento);
                                if (ultimoVencimento.substring(3, 5).equals("02")) {
                                    ultimoVencimento = acordo.getData().substring(0, 2) + ultimoVencimento.substring(2);
                                }
                            } else if (frequencia == 7) {
                                ultimoVencimento = data.incrementarSemanas(1, ultimoVencimento);
                            }
                            j++;

                        }
                    } else {
                        Movimento mov = new Movimento(-1,
                                null,
                                servico.getPlano5(),
                                pessoa,
                                servico,
                                null,
                                tipoServico,
                                null,
                                Moeda.substituiVirgulaFloat((String) listaVizualizado.get(i).getArgumento1()),
                                (String) listaVizualizado.get(i).getArgumento2(),
                                //referencia(ultimoVencimentoSind), 
                                ultimoVencimentoSind,
                                1,
                                true,
                                "E",
                                false,
                                pessoa,
                                pessoa,
                                "",
                                "",
                                ultimoVencimentoSind,
                                0,
                                0, 0, 0, 0, 0, 0, dbft.pesquisaCodigo(2), 0, null);

                        listaOperado.add(new DataObject(false, ++k, mov, (String) listaVizualizado.get(i).getArgumento3(), null, null));

                        if (parcela > 1) {
                            if (frequenciaSind == 30) {
                                ultimoVencimentoSind = data.incrementarMeses(1, ultimoVencimentoSind);
                                if (ultimoVencimentoSind.substring(3, 5).equals("02")) {
                                    ultimoVencimentoSind = acordo.getData().substring(0, 2) + ultimoVencimentoSind.substring(2);
                                }
                            } else if (frequenciaSind == 7) {
                                ultimoVencimentoSind = data.incrementarSemanas(1, ultimoVencimentoSind);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
        BubbleSort(listaOperado);
        ultimaData = ((Movimento) listaOperado.get(listaOperado.size() - 1).getArgumento2()).getVencimento();
    }

    public void imprimirBoletos() {
        ImprimirBoleto imp = new ImprimirBoleto();
        List<Float> listaValores = new ArrayList<Float>();
        List<String> listaVencimentos = new ArrayList<String>();
        List listaImp = new ArrayList();
        for (int i = 0; i < listaOperado.size(); i++) {
            listaImp.add(((Movimento) listaOperado.get(i).getArgumento2()));
            listaValores.add(((Movimento) listaOperado.get(i).getArgumento2()).getValor());
            listaVencimentos.add(((Movimento) listaOperado.get(i).getArgumento2()).getVencimento());

        }
        if (!listaImp.isEmpty()) {
            imp.imprimirBoleto(listaImp, listaValores, listaVencimentos, false);
            imp.visualizar(null);
        }
    }

    public void imprimirPlanilha() {
        ImprimirBoleto imp = new ImprimirBoleto();
        List listaImp = new ArrayList();
//        for (int i = 0; i < listaOperado.size(); i++){
//            listaImp.add(((Movimento) listaOperado.get(i).getArgumento2()));
//        }
        MovimentoDB db = new MovimentoDBToplink();
        listaImp.addAll(db.pesquisaAcordoTodos(acordo.getId()));

        if (!listaImp.isEmpty()) {
            imp.imprimirAcordoPromissoria(listaImp, acordo, historico, imprimir_pro);
            imp.visualizar(null);
        }

    }

    public String referencia(String data) {
        if (data.length() == 10) {
            String ref = data.substring(3);
            String mes = ref.substring(0, 2);
            if (!(mes.equals("01"))) {
                if ((Integer.parseInt(mes) - 1) < 10) {
                    ref = "0" + Integer.toString(Integer.parseInt(mes) - 1) + data.substring(5);
                } else {
                    ref = Integer.toString(Integer.parseInt(mes) - 1) + data.substring(5);
                }
            } else {
                ref = "12/" + Integer.toString(Integer.parseInt(data.substring(6)) - 1);
            }
            return ref;
        } else {
            return null;
        }
    }

    public List<DataObject> getListaOperado() {
        return listaOperado;
    }

    public void setListaOperado(List<DataObject> listaOperado) {
        this.listaOperado = listaOperado;
    }

    public Acordo getAcordo() {
        return acordo;
    }

    public void setAcordo(Acordo acordo) {
        this.acordo = acordo;
    }

    public int getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(int idServicos) {
        this.idServicos = idServicos;
    }

    public int getParcela() {
        return parcela;
    }

    public void setParcela(int parcela) {
        this.parcela = parcela;
    }

    public String getTotal() {
        return Moeda.converteR$Float(total);
    }

    public void setTotal(String total) {
        this.total = Moeda.substituiVirgulaFloat(total);
    }

    public void limparEntrada() {
        valorEntrada = "0";
    }

    public String getValorEntrada() {
        float valorTmp = Moeda.substituiVirgulaFloat(valorEntrada);
        float totalOutra = Moeda.substituiVirgulaFloat(totalOutras);
        if (valorEntrada.equals("0") || valorEntrada.equals("0,00")) {
            float valorTmp2 = Moeda.divisaoValores(totalOutra, parcela);
            if (parcela > 1) {
                valorEntrada = Moeda.converteR$Float(valorTmp2);
                return valorEntrada;
            }
        } else {
            if (valorTmp > (Moeda.multiplicarValores(totalOutra, (float) 0.05))
                    && valorTmp < (Moeda.multiplicarValores(totalOutra, (float) 0.8))) {
                return Moeda.converteR$(valorEntrada);
            } else {
                float valorTmp2 = Moeda.divisaoValores(totalOutra, parcela);
                if (parcela > 1) {
                    valorEntrada = Moeda.converteR$Float(valorTmp2);
//                    return Moeda.converteR$(valorEntrada);
                }
            }
        }
        return Moeda.converteR$(valorEntrada);
    }

    public void setValorEntrada(String valorEntrada) {
        this.valorEntrada = Moeda.substituiVirgula(valorEntrada);
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public int getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(int frequencia) {
        this.frequencia = frequencia;
    }

    public int getIdVencimento() {
        return idVencimento;
    }

    public void setIdVencimento(int idVencimento) {
        this.idVencimento = idVencimento;
    }

    public String getValorEntradaSind() {
        for (int i = 0; i < quantidade.size(); i++) {
            if (quantidade.get(i)[0] == 1) { // 1 ref. id sindical
                for (int j = 0; j < listaVizualizado.size(); j++) {
                    if (((Servicos) listaVizualizado.get(j).getArgumento0()).getId() == 1) {
                        if (Moeda.substituiVirgulaFloat(valorEntradaSind) != (Float) listaVizualizado.get(j).getArgumento1()) {
                            valorEntradaSind = (String) getListaVizualizado().get(j).getArgumento1();
                        }
                    }
                }
            }
        }

        return Moeda.converteR$(valorEntradaSind);
    }

    public void setValorEntradaSind(String valorEntradaSind) {
        this.valorEntradaSind = Moeda.substituiVirgula(valorEntradaSind);
    }

    public String getTotalSindical() {
        return Moeda.converteR$(totalSindical);
    }

    public void setTotalSindical(String totalSindical) {
        this.totalSindical = Moeda.substituiVirgula(totalSindical);
    }

    public String getTotalOutras() {
        return Moeda.converteR$(totalOutras);
    }

    public void setTotalOutras(String totalOutras) {
        this.totalOutras = Moeda.substituiVirgula(totalOutras);
    }

    public synchronized void ordernarPorServico() {
        int i = 0;
        int indI = 0, indF = 0;
        String data = ((Movimento) listaOperado.get(i).getArgumento2()).getVencimento();
        while (i < listaOperado.size()) {
            if (!data.equals(((Movimento) listaOperado.get(i).getArgumento2()).getVencimento())) {
                BubbleSortServico(listaOperado.subList(indI, indF));
                indI = indF;
                indF++;
                data = ((Movimento) listaOperado.get(i).getArgumento2()).getVencimento();
            } else {
                indF++;
            }
            i++;
        }
    }

    public static void BubbleSort(List<DataObject> dados) {
        boolean trocou;
        int limite = dados.size() - 1;
        Object swap1 = null;
        Object swap2 = null;
        int i = 0;
        do {
            trocou = false;
            i = 0;
            while (i < limite) {
                if (((Movimento) dados.get(i).getArgumento2()).getDtVencimento().after(
                        ((Movimento) dados.get(i + 1).getArgumento2()).getDtVencimento())) {

                    swap1 = dados.get(i).getArgumento0();
                    swap2 = dados.get(i + 1).getArgumento0();
                    dados.get(i).setArgumento0(swap2);
                    dados.get(i + 1).setArgumento0(swap1);

                    swap1 = dados.get(i).getArgumento1();
                    swap2 = dados.get(i + 1).getArgumento1();
                    dados.get(i).setArgumento1(swap2);
                    dados.get(i + 1).setArgumento1(swap1);

                    swap1 = dados.get(i).getArgumento2();
                    swap2 = dados.get(i + 1).getArgumento2();
                    dados.get(i).setArgumento2(swap2);
                    dados.get(i + 1).setArgumento2(swap1);

                    swap1 = dados.get(i).getArgumento3();
                    swap2 = dados.get(i + 1).getArgumento3();
                    dados.get(i).setArgumento3(swap2);
                    dados.get(i + 1).setArgumento3(swap1);
                    trocou = true;
                }
                i++;
            }
            limite--;
        } while (trocou);
    }

    public static void BubbleSortServico(List<DataObject> dados) {
        boolean trocou;
        int limite = dados.size() - 1;
        Object swap1 = null;
        Object swap2 = null;
        int i = 0;
        int result = -1;
        do {
            trocou = false;
            i = 0;
            while (i < limite) {
                result = ((Movimento) dados.get(i).getArgumento2()).getServicos().getDescricao().compareTo(
                        ((Movimento) dados.get(i + 1).getArgumento2()).getServicos().getDescricao());
                if (result > 0) {
                    swap1 = dados.get(i).getArgumento0();
                    swap2 = dados.get(i + 1).getArgumento0();
                    dados.get(i).setArgumento0(swap2);
                    dados.get(i + 1).setArgumento0(swap1);

                    swap1 = dados.get(i).getArgumento1();
                    swap2 = dados.get(i + 1).getArgumento1();
                    dados.get(i).setArgumento1(swap2);
                    dados.get(i + 1).setArgumento1(swap1);

                    swap1 = dados.get(i).getArgumento2();
                    swap2 = dados.get(i + 1).getArgumento2();
                    dados.get(i).setArgumento2(swap2);
                    dados.get(i + 1).setArgumento2(swap1);
                    trocou = true;
                }
                i++;
            }
            limite--;
        } while (trocou);
    }

    public int getIdVencimentoSind() {
        return idVencimentoSind;
    }

    public void setIdVencimentoSind(int idVencimentoSind) {
        this.idVencimentoSind = idVencimentoSind;
    }

    public int getFrequenciaSind() {
        return frequenciaSind;
    }

    public void setFrequenciaSind(int frequenciaSind) {
        this.frequenciaSind = frequenciaSind;
    }

    public boolean isImprimeVerso() {
        return imprimeVerso;
    }

    public void setImprimeVerso(boolean imprimeVerso) {
        this.imprimeVerso = imprimeVerso;
    }

    public Historico getHistorico() {
        return historico;
    }

    public void setHistorico(Historico historico) {
        this.historico = historico;
    }

    public boolean isImprimir() {
        return imprimir;
    }

    public void setImprimir(boolean imprimir) {
        this.imprimir = imprimir;
    }

    public List<Movimento> getListaMovs() {
        return listaMovs;
    }

    public void setListaMovs(List<Movimento> listaMovs) {
        this.listaMovs = listaMovs;
    }

    public Pessoa getPessoa() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("listaMovimento") != null) {
            listaMovs = (List) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("listaMovimento");
            pessoa = listaMovs.get(0).getPessoa();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("listaMovimento");
        }
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public void setListaVizualizado(List<DataObject> listaVizualizado) {
        this.listaVizualizado = listaVizualizado;
    }

    public String getEmailPara() {
        return emailPara;
    }

    public void setEmailPara(String emailPara) {
        this.emailPara = emailPara;
    }

    public Pessoa getPessoaEnvio() {
        return pessoaEnvio;
    }

    public void setPessoaEnvio(Pessoa pessoaEnvio) {
        this.pessoaEnvio = pessoaEnvio;
    }

    public boolean isImprimir_pro() {
        return imprimir_pro;
    }

    public void setImprimir_pro(boolean imprimir_pro) {
        this.imprimir_pro = imprimir_pro;
    }

    public String getEmailContato() {
        return emailContato;
    }

    public void setEmailContato(String emailContato) {
        this.emailContato = emailContato;
    }
}
