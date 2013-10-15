package br.com.rtools.financeiro.beans;

import br.com.rtools.arrecadacao.db.GrupoCidadesDB;
import br.com.rtools.arrecadacao.db.GrupoCidadesDBToplink;
import br.com.rtools.endereco.Cidade;
import br.com.rtools.financeiro.CobrancaEnvio;
import br.com.rtools.financeiro.CobrancaLote;
import br.com.rtools.financeiro.CobrancaTipo;
import br.com.rtools.financeiro.PollingEmail;
import br.com.rtools.financeiro.db.NotificacaoDB;
import br.com.rtools.financeiro.db.NotificacaoDBToplink;
import br.com.rtools.impressao.ParametroNotificacao;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.sistema.Links;
import br.com.rtools.sistema.db.LinksDB;
import br.com.rtools.sistema.db.LinksDBToplink;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.Download;
import br.com.rtools.utilitarios.EnviarEmail;
import br.com.rtools.utilitarios.SalvaArquivos;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
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

public class NotificacaoJSFBean implements Serializable {

    private int idLista = 0;
    private int idTipoEnvio = 0;
    private List<SelectItem> itensLista = new ArrayList();
    private List<SelectItem> listaTipoEnvio = new ArrayList();
    private String msgConfirma = "";
    private CobrancaLote lote = new CobrancaLote();
    private List<DataObject> listaNotificacao = new ArrayList();
    private int quantidade = 0;
    private int quantidadeMenu = 0;
    private boolean chkTodos = true;
    private boolean empresa = true;
    private boolean habilitaNot = false;
    private List<DataObject> listaEmpresaAdd = new ArrayList<DataObject>();
    private List<DataObject> listaContabilAdd = new ArrayList<DataObject>();
    private Registro registro = null;
    private List<DataObject> listaCidadesBase = new ArrayList<DataObject>();
    private boolean chkCidadesBase = false;
    private boolean comContabil = false;
    private boolean semContabil = false;
    private String tabAtiva = "todos";
    private String query = "";
    private int valorAtual = 0;
    private boolean progressoAtivo = false;
    private List<DataObject> listaArquivo = new ArrayList();

    public NotificacaoJSFBean() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        registro = (Registro) sv.pesquisaCodigo(1, "Registro");
    }

    public void iniciar() {
        progressoAtivo = true;
        valorAtual = 0;
    }

    public void increment() {
        if (valorAtual < 100) {
            valorAtual += 2;
            if (valorAtual >= 100) {
                progressoAtivo = false;
            }
        }
    }

    public void gerarParaTodas() {
        listaNotificacao.clear();
        listaEmpresaAdd.clear();
        listaContabilAdd.clear();
        comContabil = false;
        semContabil = false;
    }

    public void gerarComContabil() {
        listaNotificacao.clear();
        listaEmpresaAdd.clear();
        listaContabilAdd.clear();
        comContabil = true;
        semContabil = false;
    }

    public void gerarSemContabil() {
        listaNotificacao.clear();
        listaEmpresaAdd.clear();
        listaContabilAdd.clear();
        comContabil = false;
        semContabil = true;
    }

    public void addEmpresa() {
        listaNotificacao.clear();
        empresa = true;
        comContabil = false;
        semContabil = false;
        listaContabilAdd.clear();
    }

    public void addContabil() {
        listaNotificacao.clear();
        empresa = false;
        comContabil = false;
        semContabil = false;
        listaEmpresaAdd.clear();
    }

    public void addCidades() {
        listaNotificacao.clear();
        comContabil = false;
        semContabil = false;
    }

    public synchronized List<DataObject> getListaNotificacao() {
        if (listaNotificacao.isEmpty()) {
            NotificacaoDB db = new NotificacaoDBToplink();

            String empresas = "", contabils = "", cidades = "";
            for (int i = 0; i < listaEmpresaAdd.size(); i++) {
                if (empresas.length() > 0 && i != listaEmpresaAdd.size()) {
                    empresas += ",";
                }
                empresas += listaEmpresaAdd.get(i).getArgumento0();
            }

            for (int i = 0; i < listaContabilAdd.size(); i++) {
                if (contabils.length() > 0 && i != listaContabilAdd.size()) {
                    contabils += ",";
                }
                contabils += listaContabilAdd.get(i).getArgumento0();
            }

            for (int i = 0; i < listaCidadesBase.size(); i++) {
                if ((Boolean) listaCidadesBase.get(i).getArgumento0()) {
                    if (cidades.length() > 0 && i != listaCidadesBase.size()) {
                        cidades += ",";
                    }
                    cidades += ((Cidade) listaCidadesBase.get(i).getArgumento1()).getId();
                }
            }

            List<Vector> result = null;
            Object[] obj = new Object[2];

            if (lote.getId() != -1) {
                obj = db.listaParaNotificacao(lote.getId(), DataHoje.data(), empresas, contabils, cidades, comContabil, semContabil);
            } else {
                obj = db.listaParaNotificacao(-1, DataHoje.data(), empresas, contabils, cidades, comContabil, semContabil);
            }

            result = (Vector) obj[1];
            if (!result.isEmpty()) {
                query = String.valueOf(obj[0]);
                for (int i = 0; i < result.size(); i++) {

                    String noti = (result.get(i).get(5) == null) ? "Nunca" : DataHoje.converteData((Date) result.get(i).get(5));
                    listaNotificacao.add(new DataObject(true, result.get(i), noti, null, null, null));
//                    List<Vector> listaNots = db.listaNotificado( (Integer) result.get(i).get(0) );
//                    if (listaNots.isEmpty())
//                        listaNotificacao.add(new DataObject(true, result.get(i), "Nunca", null, null, null));
//                    else{
//                        listaNotificacao.add(new DataObject(true, result.get(i), listaNots.get(1), null, null, null));
//                    }
                }
                quantidade = listaNotificacao.size();
            }
        }
        if (listaNotificacao.isEmpty()) {
            query = "";
        }
        return listaNotificacao;
    }

    public String salvar() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();

        if (!sv.alterarObjeto(lote)) {
            msgConfirma = "Erro ao atualizar Mensagem";
            sv.desfazerTransacao();
            return null;
        }

        LinksDB db = new LinksDBToplink();
        try {
            String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/notificacao/" + lote.getId());
            File files = new File(caminho);
            File listFile[] = files.listFiles();

            for (int i = 0; i < listFile.length; i++) {
                Links link = db.pesquisaNomeArquivo(listFile[i].getName());
                if (link == null) {
                    continue;
                }

                if (sv.deletarObjeto(sv.pesquisaCodigo(link.getId(), "Links"))) {
                    File f_delete = new File(caminho + "/" + listFile[i].getName());
                    if (f_delete.exists()) {
                        f_delete.delete();
                    }
                }
                listaArquivo.clear();
            }
        } catch (Exception e) {
            sv.desfazerTransacao();
            return null;
        }

        sv.comitarTransacao();
        msgConfirma = "Atualizado com sucesso!";
        return null;
    }

    public String gerarNotificacao() {
        if (((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario")).getId() == -1) {
            msgConfirma = "Usuário não esta na sessão, faça seu login novamente!";
            return null;
        }

        if (lote.getId() != -1) {
            lote = new CobrancaLote();
        }
        lote.setUsuario(((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario")));
        lote.setDtEmissao(DataHoje.dataHoje());
        lote.setHora(DataHoje.horaMinuto());

        NotificacaoDB db = new NotificacaoDBToplink();
//        if (db.pesquisaCobrancaLote(lote.getUsuario().getId(), lote.getDtEmissao()) != null) {
//            msgConfirma = "Notificação já gerada hoje!";
//            return null;
//        }

        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();
        if (!sv.inserirObjeto(lote)) {
            msgConfirma = "Erro ao Gerar Lote";
            sv.desfazerTransacao();
            return null;
        }
        sv.comitarTransacao();

        sv.abrirTransacao();
        //for (int i = 0; i < listaNotificacao.size(); i++) {
        if (!sv.inserirQuery("insert into fin_cobranca (id_movimento,id_lote) (select m.id, " + lote.getId() + query + " and fc.id_lote is null order by c.ds_nome, c.id_pessoa)")) {
//            if ((Boolean) listaNotificacao.get(i).getArgumento0()) {
//                if (!sv.inserirQuery("insert into fin_cobranca (id_movimento,id_lote) values (" + ((Vector) listaNotificacao.get(i).getArgumento1()).get(0) + "," + lote.getId() + ")")) {
            msgConfirma = "Erro ao inserir Cobrança";
            sv.desfazerTransacao();

            sv.abrirTransacao();
            sv.deletarObjeto(sv.pesquisaCodigo(lote.getId(), "CobrancaLote"));
            sv.comitarTransacao();
            lote = new CobrancaLote();
            return null;
//                }
//            }
        }

        sv.comitarTransacao();
        msgConfirma = "Gerado com sucesso!";
        itensLista.clear();

        listaNotificacao.clear();
        listaEmpresaAdd.clear();
        listaContabilAdd.clear();
        lote = new CobrancaLote();
        chkTodos = true;
        return null;
    }

    public synchronized void enviarPeloMenu() {
        NotificacaoDB db = new NotificacaoDBToplink();
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        boolean erro = false;
        Usuario usu = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");
        if (usu.getId() == -1) {
            msgConfirma = "Usuário não esta na sessão, faça seu login novamente!";
            return;
        }
        Registro reg = (Registro) sv.pesquisaCodigo(1, "Registro");

        List<Vector> lista = db.pollingEmail(reg.getLimiteEnvios(), usu.getId());
        if (!lista.isEmpty()) {
            sv.abrirTransacao();

            for (int i = 0; i < lista.size(); i++) {
                PollingEmail pe = (PollingEmail) sv.pesquisaCodigo((Integer) lista.get(i).get(0), "PollingEmail");

                enviarEmailPolling(pe.getLinks());

                pe.setAtivo(false);
                pe.setEnvio(DataHoje.data());
                if (!sv.alterarObjeto(pe)) {
                    erro = true;
                    break;
                }
            }
        }

        if (!erro) {
            sv.comitarTransacao();
            habilitaNot = false;
        } else {
            sv.desfazerTransacao();
            habilitaNot = true;
            return;
        }

        sv.abrirTransacao();
        lista.clear();
        lista = db.pollingEmail(reg.getLimiteEnvios(), usu.getId());
        if (lista.isEmpty()) {
            lista = db.pollingEmailNovo(reg.getLimiteEnvios());
            if (!lista.isEmpty()) {
                String ph = DataHoje.incrementarHora(DataHoje.horaMinuto(), reg.getIntervaloEnvios());
                for (int i = 0; i < lista.size(); i++) {
                    PollingEmail pe = (PollingEmail) sv.pesquisaCodigo((Integer) lista.get(i).get(0), "PollingEmail");
                    pe.setAtivo(true);
                    pe.setEmissao(DataHoje.data());
                    pe.setHora(ph);

                    if (!sv.alterarObjeto(pe)) {
                        erro = true;
                        break;
                    }
                }
            }

        }

        if (!erro) {
            sv.comitarTransacao();
            habilitaNot = false;
        } else {
            sv.desfazerTransacao();
            habilitaNot = true;
        }
    }

    public String enviarNotificacao() throws JRException {
        if (lote.getId() == -1) {
            msgConfirma = "Selecione um Lote para envio!";
            return null;
        }

        if (((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario")).getId() == -1) {
            msgConfirma = "Usuário não esta na sessão, faça seu login novamente!";
            return null;
        }

        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        CobrancaTipo ct = (CobrancaTipo) sv.pesquisaCodigo(Integer.valueOf(listaTipoEnvio.get(idTipoEnvio).getDescription()), "CobrancaTipo");

        NotificacaoDB db = new NotificacaoDBToplink();
        List<Vector> result = db.listaNotificacaoEnvio(ct.getId(), lote.getId());

        if (result.isEmpty()) {
            msgConfirma = "Lista de parametros para envio vazia!";
            return null;
        }

        List<ParametroNotificacao> listax = new ArrayList();
        CobrancaEnvio ce = db.pesquisaCobrancaEnvio(lote.getId());

        sv.abrirTransacao();
        if (ce.getId() == -1) {
            ce.setDtEmissao(DataHoje.dataHoje());
            ce.setHora(DataHoje.horaMinuto());
            ce.setLote(lote);
            ce.setUsuario(((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario")));
            ce.setTipoCobranca(ct);
            if (!sv.inserirObjeto(ce)) {
                msgConfirma = "Erro ao salvar Cobrança Envio";
                sv.desfazerTransacao();
                return null;
            }
        }

        if (ct.getId() == 4 || ct.getId() == 5) {
            List<Vector> lista = db.pollingEmailTrue();
            if (!lista.isEmpty()) {
                msgConfirma = "Existem notificações às " + lista.get(0).get(1) + " para serem enviadas, conclua o envio antes de notificar mais!";
                sv.desfazerTransacao();
                return null;
            }

            int id_compara = 0;
            boolean enviar = false;

            Pessoa pes = new Pessoa();
            String jasper = "";

            int atual = 1;
            String ph = "";
            for (int i = 0; i < result.size(); i++) {
                listax.add(new ParametroNotificacao(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Imagens/LogoCliente.png"),
                        getConverteNullString(result.get(i).get(1)),
                        getConverteNullString(result.get(i).get(2)),
                        getConverteNullString(result.get(i).get(3)),
                        getConverteNullString(result.get(i).get(4)),
                        getConverteNullString(result.get(i).get(5)),
                        getConverteNullString(result.get(i).get(6)),
                        getConverteNullString(result.get(i).get(7)),
                        getConverteNullString(result.get(i).get(8)),
                        getConverteNullString(result.get(i).get(9)),
                        getConverteNullString(result.get(i).get(10)),
                        getConverteNullString(result.get(i).get(11)),
                        getConverteNullString(result.get(i).get(12)),
                        getConverteNullString(result.get(i).get(13)),
                        getConverteNullString(result.get(i).get(14)),
                        getConverteNullString(result.get(i).get(15)),
                        getConverteNullString(result.get(i).get(16)),
                        getConverteNullString(result.get(i).get(17)),
                        getConverteNullString(result.get(i).get(18)),
                        getConverteNullString(result.get(i).get(19)),
                        getConverteNullString(result.get(i).get(20)),
                        getConverteNullString(result.get(i).get(21)),
                        getConverteNullString(result.get(i).get(22)),
                        getConverteNullString(result.get(i).get(23)),
                        getConverteNullString(result.get(i).get(24)),
                        getConverteNullString(result.get(i).get(25)),
                        getConverteNullString(result.get(i).get(26)),
                        getConverteNullString(result.get(i).get(27)),
                        getConverteNullString(result.get(i).get(28)),
                        getConverteNullString(result.get(i).get(29)),
                        getConverteNullString(result.get(i).get(30)),
                        getConverteNullString(result.get(i).get(31)),
                        getConverteNullString(result.get(i).get(32)),
                        getConverteNullString(result.get(i).get(33)),
                        getConverteNullString(result.get(i).get(34)),
                        getConverteNullString(result.get(i).get(35)),
                        getConverteNullString(result.get(i).get(36)),
                        getConverteNullString(result.get(i).get(37))));

                try {
                    if (ct.getId() == 4) {
                        jasper = "NOTIFICACAO_ARRECADACAO_ESCRITORIO.jasper";
                        id_compara = getConverteNullInt(result.get(i).get(38)); // ID_JURIDICA
                        if (id_compara != getConverteNullInt(result.get(i + 1).get(38))) {
                            enviar = true;
                            pes = ((Juridica) sv.pesquisaCodigo(id_compara, "Juridica")).getPessoa();
                        }
                    } else {
                        jasper = "NOTIFICACAO_ARRECADACAO_EMPRESA.jasper";
                        id_compara = getConverteNullInt(result.get(i).get(39)); // ID_PESSOA
                        if (id_compara != getConverteNullInt(result.get(i + 1).get(39))) {
                            enviar = true;
                            pes = (Pessoa) sv.pesquisaCodigo(id_compara, "Pessoa");
                            //pes = ((Juridica)sv.pesquisaCodigo(id_compara, "Juridica")).getPessoa();
                        }
                    }
                } catch (Exception e) {
                    if (ct.getId() == 4) {
                        pes = ((Juridica) sv.pesquisaCodigo(id_compara, "Juridica")).getPessoa();
                    } else {
                        pes = (Pessoa) sv.pesquisaCodigo(id_compara, "Pessoa");
                    }
                    enviar = true;
                }

                if (enviar) {
                    try {
                        if (atual <= registro.getLimiteEnvios() && ph.isEmpty()) {
                            if (!pes.getEmail1().isEmpty()) {
                                enviarEmail(pes, listax, sv, jasper);
                                atual++;
                            }
                        } else {
                            if (atual > registro.getLimiteEnvios() && ph.isEmpty()) {
                                atual = 1;
                                ph = DataHoje.incrementarHora(DataHoje.horaMinuto(), registro.getIntervaloEnvios());
                            }

                            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(listax);
                            JasperReport jasperx = null;
                            jasperx = (JasperReport) JRLoader.loadObject(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/" + jasper));
                            String nomeArq = "notificacao_";

                            JasperPrint print = JasperFillManager.fillReport(jasperx, null, dtSource);
                            byte[] arquivo = new byte[0];
                            arquivo = JasperExportManager.exportReportToPdf(print);

                            String nomeDownload = nomeArq + DataHoje.hora().replace(":", "") + ".pdf";
                            Thread.sleep(2000);
                            SalvaArquivos sa = new SalvaArquivos(arquivo, nomeDownload, false);
                            String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/notificacao/" + lote.getId());

                            File create = new File(pathPasta);
                            if (!create.exists()) {
                                create.mkdir();
                            }

                            sa.salvaNaPasta(pathPasta);

                            Links link = new Links();
                            link.setCaminho(registro.getUrlPath() + "/Sindical/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/notificacao/" + lote.getId());
                            link.setNomeArquivo(nomeDownload);
                            link.setPessoa(pes);
                            link.setDescricao(listaTipoEnvio.get(idTipoEnvio).getLabel());

                            if (!sv.inserirObjeto(link)) {
                                msgConfirma = "Erro ao salvar Link de envio!";
                                sv.desfazerTransacao();
                                return null;
                            }

                            PollingEmail pe = new PollingEmail();
                            pe.setDtEmissao(DataHoje.dataHoje());
                            pe.setLinks(link);
                            pe.setCobrancaEnvio(ce);

                            if (atual <= registro.getLimiteEnvios()) {
                                pe.setAtivo(true);
                                pe.setHora(ph);
                            } else {
                                pe.setAtivo(false);
                            }

                            if (!sv.inserirObjeto(pe)) {
                                msgConfirma = "Erro ao salvar Polling de envio!";
                                sv.desfazerTransacao();
                                return null;
                            }
                            atual++;
                        }
                    } catch (Exception e) {
                    }
                    enviar = false;
                    listax.clear();
                }
            }
        } else {
            int id_compara = 0;
            boolean imprimir = false;
            int atual = 0, limite = 5000;
            JasperReport jasper = null;

            for (int i = 0; i < result.size(); i++) {
                listax.add(new ParametroNotificacao(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Imagens/LogoCliente.png"),
                        getConverteNullString(result.get(i).get(1)),
                        getConverteNullString(result.get(i).get(2)),
                        getConverteNullString(result.get(i).get(3)),
                        getConverteNullString(result.get(i).get(4)),
                        getConverteNullString(result.get(i).get(5)),
                        getConverteNullString(result.get(i).get(6)),
                        getConverteNullString(result.get(i).get(7)),
                        getConverteNullString(result.get(i).get(8)),
                        getConverteNullString(result.get(i).get(9)),
                        getConverteNullString(result.get(i).get(10)),
                        getConverteNullString(result.get(i).get(11)),
                        getConverteNullString(result.get(i).get(12)),
                        getConverteNullString(result.get(i).get(13)),
                        getConverteNullString(result.get(i).get(14)),
                        getConverteNullString(result.get(i).get(15)),
                        getConverteNullString(result.get(i).get(16)),
                        getConverteNullString(result.get(i).get(17)),
                        getConverteNullString(result.get(i).get(18)),
                        getConverteNullString(result.get(i).get(19)),
                        getConverteNullString(result.get(i).get(20)),
                        getConverteNullString(result.get(i).get(21)),
                        getConverteNullString(result.get(i).get(22)),
                        getConverteNullString(result.get(i).get(23)),
                        getConverteNullString(result.get(i).get(24)),
                        getConverteNullString(result.get(i).get(25)),
                        getConverteNullString(result.get(i).get(26)),
                        getConverteNullString(result.get(i).get(27)),
                        getConverteNullString(result.get(i).get(28)),
                        getConverteNullString(result.get(i).get(29)),
                        getConverteNullString(result.get(i).get(30)),
                        getConverteNullString(result.get(i).get(31)),
                        getConverteNullString(result.get(i).get(32)),
                        getConverteNullString(result.get(i).get(33)),
                        getConverteNullString(result.get(i).get(34)),
                        getConverteNullString(result.get(i).get(35)),
                        getConverteNullString(result.get(i).get(36)),
                        getConverteNullString(result.get(i).get(37))));

                JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(listax);
                String nomeArq = "notificacao_";
                try {
                    if (ct.getId() == 1) {
                        id_compara = getConverteNullInt(result.get(i).get(38)); // ID_JURIDICA
                        jasper = (JasperReport) JRLoader.loadObject(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/NOTIFICACAO_ARRECADACAO_ESCRITORIO.jasper"));
                        if (id_compara != getConverteNullInt(result.get(i + 1).get(38)) && !imprimir) {
                            imprimir = true;
                        }
                    } else if (ct.getId() == 2) {
                        id_compara = getConverteNullInt(result.get(i).get(39)); // ID_PESSOA
                        jasper = (JasperReport) JRLoader.loadObject(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/NOTIFICACAO_ARRECADACAO_EMPRESA.jasper"));
                        if (id_compara != getConverteNullInt(result.get(i + 1).get(39)) && !imprimir) {
                            imprimir = true;
                        }
                    } else if (ct.getId() == 3) {
                        jasper = (JasperReport) JRLoader.loadObject(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/NOTIFICACAO_ARRECADACAO_EMPRESA.jasper"));
                        id_compara = getConverteNullInt(result.get(i).get(39)); // ID_PESSOA
                        if (id_compara != getConverteNullInt(result.get(i + 1).get(39)) && !imprimir) {
                            imprimir = true;
                        }
                    }
                } catch (Exception e) {
                    imprimir = true;
                    atual = limite;
                }

                try {
                    if (imprimir && (atual >= limite)) {
                        JasperPrint print = JasperFillManager.fillReport(jasper, null, dtSource);
                        byte[] arquivo = new byte[0];
                        arquivo = JasperExportManager.exportReportToPdf(print);

                        String nomeDownload = nomeArq + DataHoje.hora().replace(":", "") + ".pdf";
                        Thread.sleep(2000);
                        SalvaArquivos sa = new SalvaArquivos(arquivo, nomeDownload, false);
                        String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/notificacao/" + lote.getId());

                        File create = new File(pathPasta);
                        if (!create.exists()) {
                            create.mkdir();
                        }

                        sa.salvaNaPasta(pathPasta);

                        Links link = new Links();
                        link.setCaminho(registro.getUrlPath() + "/Sindical/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/notificacao/" + lote.getId());
                        link.setNomeArquivo(nomeDownload);
                        link.setPessoa(null);
                        link.setDescricao(listaTipoEnvio.get(idTipoEnvio).getLabel());

                        if (!sv.inserirObjeto(link)) {
                            msgConfirma = "Erro ao salvar Link de envio!";
                            sv.desfazerTransacao();
                            return null;
                        }

//                        Download download = new Download(nomeDownload, pathPasta, "application/pdf", FacesContext.getCurrentInstance());
//                        download.baixar();

                        imprimir = false;
                        atual = 0;
                        listax.clear();
                    }

                } catch (Exception e) {
                }
                atual++;
            }
        }

        if (!result.isEmpty()) {
            sv.comitarTransacao();
        }

        listaNotificacao.clear();
        return "notificacao";
    }

//    try {
//                    if (ct.getId() == 1) {
//                        id_compara = getConverteNullInt(result.get(i).get(38)); // ID_JURIDICA
//                        if (id_compara != getConverteNullInt(result.get(i + 1).get(38)) && (atual >= limite)) {
//                            jasper = (JasperReport) JRLoader.loadObject(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/NOTIFICACAO_ARRECADACAO_ESCRITORIO.jasper"));
//                            imprimir = true;
//                            atual = 0;
//                            //pes = ((Juridica) sv.pesquisaCodigo(id_compara, "Juridica")).getPessoa();
//                        }
//                    } else if (ct.getId() == 2) {
//                        id_compara = getConverteNullInt(result.get(i).get(39)); // ID_PESSOA
//                        if (id_compara != getConverteNullInt(result.get(i + 1).get(39))  && (atual >= limite)) {
//                            jasper = (JasperReport) JRLoader.loadObject(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/NOTIFICACAO_ARRECADACAO_EMPRESA.jasper"));
//                            imprimir = true;
//                            atual = 0;
//                            //pes = (Pessoa) sv.pesquisaCodigo(id_compara, "Pessoa");
//                            //pes = ((Juridica)sv.pesquisaCodigo(id_compara, "Juridica")).getPessoa();
//                        }
//                    } else if (ct.getId() == 3) {
//                        id_compara = getConverteNullInt(result.get(i).get(39)); // ID_PESSOA
//                        if (id_compara != getConverteNullInt(result.get(i + 1).get(39))  && (atual >= limite)) {
//                            jasper = (JasperReport) JRLoader.loadObject(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/NOTIFICACAO_ARRECADACAO_EMPRESA.jasper"));
//                            imprimir = true;
//                            atual = 0;
//                            //pes = (Pessoa) sv.pesquisaCodigo(id_compara, "Pessoa");
//                            //pes = ((Juridica)sv.pesquisaCodigo(id_compara, "Juridica")).getPessoa();
//                        }
//                    }
//                } catch (Exception e) {
//                    if (ct.getId() == 1) {
//                        jasper = (JasperReport) JRLoader.loadObject(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/NOTIFICACAO_ARRECADACAO_ESCRITORIO.jasper"));
//                        //pes = ((Juridica) sv.pesquisaCodigo(id_compara, "Juridica")).getPessoa();
//                    } else if (ct.getId() == 2) {
//                        jasper = (JasperReport) JRLoader.loadObject(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/NOTIFICACAO_ARRECADACAO_EMPRESA.jasper"));
//                        //pes = (Pessoa) sv.pesquisaCodigo(id_compara, "Pessoa");
//                    } else if (ct.getId() == 3) {
//                        jasper = (JasperReport) JRLoader.loadObject(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/NOTIFICACAO_ARRECADACAO_EMPRESA.jasper"));
//                        //pes = (Pessoa) sv.pesquisaCodigo(id_compara, "Pessoa");
//                    }
//                    imprimir = true;
//                }
//                atual++;
    public String enviarEmailPolling(Links link) {
        try {
            if (link.getPessoa().getEmail1().isEmpty()) {
                return null;
            }
            List<Pessoa> pes_add = new ArrayList();
            pes_add.add(link.getPessoa());
            String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/notificacao/" + lote.getId());
            String[] ret = new String[2];
            if (!registro.isEnviarEmailAnexo()) {
                ret = EnviarEmail.EnviarEmailPersonalizado(registro,
                        pes_add,
                        " <h5>Visualize sua notificação clicando no link abaixo</5><br /><br />"
                        + " <a href='" + registro.getUrlPath() + "/Sindical/acessoLinks.jsf?cliente=" + ControleUsuarioBean.getCliente() + "&amp;arquivo=" + link.getNomeArquivo() + "' target='_blank'>Clique aqui para abrir a Notificação</a><br />",
                        new ArrayList(),
                        "Envio de Notificação");
            } else {
                List<File> fls = new ArrayList<File>();
                //fls.add(new File(link.getCaminho() + "/" + link.getNomeArquivo()));
                fls.add(new File(pathPasta + "/" + link.getNomeArquivo()));

                ret = EnviarEmail.EnviarEmailPersonalizado(registro,
                        pes_add,
                        " <h5>Baixe sua notificação em Anexada neste email</5><br /><br />",
                        fls,
                        "Envio de Notificação");
            }
            if (!ret[1].isEmpty()) {
                msgConfirma = ret[1];
            } else {
                msgConfirma = ret[0];
            }
        } catch (Exception e) {
        }
        return null;
    }

    public String enviarEmail(Pessoa pessoa, List<ParametroNotificacao> lista, SalvarAcumuladoDB sv, String nomeJasper) {

        JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
        JasperReport jasper = null;
        String nomeArq = "notificacao_";
        try {
            jasper = (JasperReport) JRLoader.loadObject(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/" + nomeJasper));
            JasperPrint print = JasperFillManager.fillReport(jasper, null, dtSource);

            byte[] arquivo = new byte[0];
            arquivo = JasperExportManager.exportReportToPdf(print);

            String nomeDownload = nomeArq + DataHoje.hora().replace(":", "") + ".pdf";
            SalvaArquivos sa = new SalvaArquivos(arquivo, nomeDownload, false);
            String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/notificacao/" + lote.getId());
            sa.salvaNaPasta(pathPasta);

            Links link = new Links();
            link.setCaminho(registro.getUrlPath() + "/Sindical/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/notificacao/" + lote.getId());
            link.setNomeArquivo(nomeDownload);
            link.setPessoa(pessoa);
            link.setDescricao(listaTipoEnvio.get(idTipoEnvio).getLabel());

            if (!sv.inserirObjeto(link)) {
                msgConfirma = "Erro ao salvar Link de envio!";
                sv.desfazerTransacao();
                return null;
            }

            List<Pessoa> pes_add = new ArrayList();
            pes_add.add(pessoa);

            String[] ret = new String[2];
            if (!registro.isEnviarEmailAnexo()) {
                ret = EnviarEmail.EnviarEmailPersonalizado(registro,
                        pes_add,
                        " <h5>Visualize sua notificação clicando no link abaixo</5><br /><br />"
                        + " <a href='" + registro.getUrlPath() + "/Sindical/acessoLinks.jsf?cliente=" + ControleUsuarioBean.getCliente() + "&amp;arquivo=" + nomeDownload + "' target='_blank'>Clique aqui para abrir a Notificação</a><br />",
                        new ArrayList(),
                        "Envio de Notificação");
            } else {
                List<File> fls = new ArrayList<File>();
                fls.add(new File(pathPasta + "/" + link.getNomeArquivo()));

                ret = EnviarEmail.EnviarEmailPersonalizado(registro,
                        pes_add,
                        " <h5>Baixe sua notificação em Anexada neste email</5><br /><br />",
                        fls,
                        "Envio de Notificação");
            }
            if (!ret[1].isEmpty()) {
                msgConfirma = ret[1];
            } else {
                msgConfirma = ret[0];
            }
        } catch (Exception e) {
        }
        return null;
    }

    public void alteraCombo() {
        if (!itensLista.isEmpty()) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            if (Integer.valueOf(itensLista.get(idLista).getDescription()) == -1) {
                lote = new CobrancaLote();
            } else {
                lote = (CobrancaLote) sv.pesquisaCodigo(Integer.valueOf(itensLista.get(idLista).getDescription()), "CobrancaLote");
            }
        }
        listaNotificacao.clear();
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

    public int getIdLista() {
        return idLista;
    }

    public void setIdLista(int idLista) {
        this.idLista = idLista;
    }

    public List<SelectItem> getItensLista() {
        if (itensLista.isEmpty()) {
            NotificacaoDB db = new NotificacaoDBToplink();
            List<CobrancaLote> result = db.listaCobrancaLote();
            itensLista.add(new SelectItem(0, "<< Gerar novo Lote de Notificação >>", String.valueOf(-1)));
            for (int i = 0; i < result.size(); i++) {
                itensLista.add(new SelectItem(new Integer(i + 1),
                        "Lote gerado - " + result.get(i).getEmissao() + " às " + result.get(i).getHora() + " - " + result.get(i).getUsuario().getLogin(),
                        String.valueOf(result.get(i).getId())));
            }
        }
        return itensLista;
    }

    public void setItensLista(List<SelectItem> itensLista) {
        this.itensLista = itensLista;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public CobrancaLote getLote() {
        return lote;
    }

    public void setLote(CobrancaLote lote) {
        this.lote = lote;
    }

    public List<SelectItem> getListaTipoEnvio() {
        if (listaTipoEnvio.isEmpty()) {
            NotificacaoDB db = new NotificacaoDBToplink();
            List<CobrancaTipo> result = db.listaCobrancaTipoEnvio();
            for (int i = 0; i < result.size(); i++) {
                listaTipoEnvio.add(new SelectItem(new Integer(i),
                        result.get(i).getDescricao(),
                        String.valueOf(result.get(i).getId())));
            }
        }
        return listaTipoEnvio;
    }

    public void setListaTipoEnvio(List<SelectItem> listaTipoEnvio) {
        this.listaTipoEnvio = listaTipoEnvio;
    }

    public int getIdTipoEnvio() {
        return idTipoEnvio;
    }

    public void setIdTipoEnvio(int idTipoEnvio) {
        this.idTipoEnvio = idTipoEnvio;
    }

    public int getQuantidade() {
//        if (quantidade == 0){
//            getListaNotificacao();
//            quantidade = listaNotificacao.size();
//        }
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void marcarTodos() {
        for (int i = 0; i < listaNotificacao.size(); i++) {
            listaNotificacao.get(i).setArgumento0(chkTodos);
        }
    }

    public boolean isChkTodos() {
        return chkTodos;
    }

    public void setChkTodos(boolean chkTodos) {
        this.chkTodos = chkTodos;
    }

    public List<DataObject> getListaEmpresaAdd() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa") != null && empresa) {
            Juridica j = (Juridica) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa");

            for (int i = 0; i < listaEmpresaAdd.size(); i++) {
                if (listaEmpresaAdd.get(i).getArgumento0().equals(j.getId())) {
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
                    return listaEmpresaAdd;
                }
            }

            listaEmpresaAdd.add(new DataObject(j.getId(), j));
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
            listaNotificacao.clear();
        }
        return listaEmpresaAdd;
    }

    public void setListaEmpresaAdd(List<DataObject> listaEmpresaAdd) {
        this.listaEmpresaAdd = listaEmpresaAdd;
    }

    public List<DataObject> getListaContabilAdd() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa") != null && !empresa) {
            Juridica j = (Juridica) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa");

            for (int i = 0; i < listaContabilAdd.size(); i++) {
                if (listaContabilAdd.get(i).getArgumento0().equals(j.getId())) {
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
                    return listaContabilAdd;
                }
            }

            listaContabilAdd.add(new DataObject(j.getId(), j));
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
            listaNotificacao.clear();
        }
        return listaContabilAdd;
    }

    public void setListaContabilAdd(List<DataObject> listaContabilAdd) {
        this.listaContabilAdd = listaContabilAdd;
    }

    public boolean isHabilitaNot() {
        NotificacaoDB db = new NotificacaoDBToplink();
        Registro reg = (Registro) new SalvarAcumuladoDBToplink().pesquisaCodigo(1, "Registro");
//        if (registro == null){
//            registro = (Registro) new SalvarAcumuladoDBToplink().pesquisaCodigo(1, "Registro");
//        }
        Usuario usu = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");
        if (usu == null || (usu != null && usu.getId() == -1)) {
            return false;
        }
        List<Vector> lista = db.pollingEmail(reg.getLimiteEnvios(), ((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario")).getId());
        if (!lista.isEmpty()) {
            habilitaNot = true;
            quantidadeMenu = lista.size();
        } else {
            habilitaNot = false;
        }
        return habilitaNot;
    }

    public void setHabilitaNot(boolean habilitaNot) {
        this.habilitaNot = habilitaNot;
    }

    public int getQuantidadeMenu() {
        return quantidadeMenu;
    }

    public void setQuantidadeMenu(int quantidadeMenu) {
        this.quantidadeMenu = quantidadeMenu;
    }

    public List<DataObject> getListaCidadesBase() {
        if (listaCidadesBase.isEmpty()) {
            GrupoCidadesDB db = new GrupoCidadesDBToplink();
            List select = new ArrayList();
            select.addAll(db.pesquisaCidadesBase());
            for (int i = 0; i < select.size(); i++) {
                listaCidadesBase.add(new DataObject(false, ((Cidade) select.get(i))));
            }
        }
        return listaCidadesBase;
    }

    public boolean isChkCidadesBase() {
        return chkCidadesBase;
    }

    public void setChkCidadesBase(boolean chkCidadesBase) {
        this.chkCidadesBase = chkCidadesBase;
    }

    public void marcarCidadesBase() {
        for (int i = 0; i < listaCidadesBase.size(); i++) {
            listaCidadesBase.get(i).setArgumento0(chkCidadesBase);
        }
        listaNotificacao.clear();
    }

    public String getTabAtiva() {
        return tabAtiva;
    }

    public void setTabAtiva(String tabAtiva) {
        this.tabAtiva = tabAtiva;
    }

    public int getValorAtual() {
        return valorAtual;
    }

    public void setValorAtual(int valorAtual) {
        this.valorAtual = valorAtual;
    }

    public boolean isProgressoAtivo() {
        return progressoAtivo;
    }

    public void setProgressoAtivo(boolean progressoAtivo) {
        this.progressoAtivo = progressoAtivo;
    }

    public List<DataObject> getListaArquivo() {
        if (listaNotificacao.isEmpty()) {
            listaArquivo.clear();
        }
        if (listaArquivo.isEmpty()) {
            LinksDB db = new LinksDBToplink();
            try {
                String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/notificacao/" + lote.getId());
                File files = new File(caminho);
                File listFile[] = files.listFiles();

                for (int i = 0; i < listFile.length; i++) {
                    Links link = db.pesquisaNomeArquivo(listFile[i].getName());
                    if (link == null) {
                        continue;
                    }

                    listaArquivo.add(new DataObject(registro.getUrlPath() + "/Sindical/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/notificacao/" + lote.getId() + "/" + listFile[i].getName(), i + 1 + " - " + link.getDescricao()));
                }
            } catch (Exception e) {
                return new ArrayList();
            }
        }
        return listaArquivo;
    }

    public void setListaArquivo(List<DataObject> listaArquivo) {
        this.listaArquivo = listaArquivo;
    }
}
