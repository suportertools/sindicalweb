package br.com.rtools.homologacao.beans;

import br.com.rtools.arrecadacao.CnaeConvencao;
import br.com.rtools.pessoa.beans.PesquisarProfissaoBean;
import br.com.rtools.arrecadacao.Oposicao;
import br.com.rtools.arrecadacao.db.CnaeConvencaoDB;
import br.com.rtools.arrecadacao.db.CnaeConvencaoDBToplink;
import br.com.rtools.atendimento.db.AtendimentoDB;
import br.com.rtools.atendimento.db.AtendimentoDBTopLink;
import br.com.rtools.endereco.Endereco;
import br.com.rtools.endereco.db.EnderecoDB;
import br.com.rtools.endereco.db.EnderecoDBToplink;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.homologacao.Agendamento;
import br.com.rtools.homologacao.Cancelamento;
import br.com.rtools.homologacao.ConfiguracaoHomologacao;
import br.com.rtools.homologacao.Demissao;
import br.com.rtools.homologacao.Feriados;
import br.com.rtools.homologacao.Horarios;
import br.com.rtools.homologacao.ListaAgendamento;
import br.com.rtools.homologacao.Status;
import br.com.rtools.homologacao.dao.FeriadosDao;
import br.com.rtools.homologacao.db.*;
import br.com.rtools.impressao.beans.ProtocoloAgendamento;
import br.com.rtools.movimento.ImprimirBoleto;
import br.com.rtools.pessoa.*;
import br.com.rtools.pessoa.db.*;
import br.com.rtools.pessoa.utilitarios.PessoaUtilitarios;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.sistema.Email;
import br.com.rtools.sistema.EmailPessoa;
import br.com.rtools.utilitarios.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class AgendamentoBean extends PesquisarProfissaoBean implements Serializable {

    private int idStatus = 0;
    private int idMotivoDemissao = 0;
    private int idHorarioTransferencia = 0;
    private int idHorarioAlternativo = 0;
    //private String tipoAviso = "true";
    private String strEndereco = "";
    private String statusEmpresa = "REGULAR";
    private String cepEndereco = "";
    private String strContribuinte = "";
    private String emailEmpresa = "";
    private String styleDestaque = "";
    private List listaMovimento = new ArrayList();
    private List<SelectItem> listaStatus = new ArrayList();
    private List<SelectItem> listaDemissao = new ArrayList();
    private List<SelectItem> listaHorarioTransferencia = new ArrayList();
    private List<ListaAgendamento> listaHorarios = new ArrayList();
    private Juridica juridica = new Juridica();
    private Fisica fisica = new Fisica();
    private Date data = DataHoje.dataHoje();
    private Date dataTransferencia = DataHoje.dataHoje();
    private Agendamento agendamento = new Agendamento();
    private PessoaEndereco enderecoEmpresa = new PessoaEndereco();
    private PessoaEmpresa pessoaEmpresa = new PessoaEmpresa();
    private boolean renderCancelarHorario = false;
    private boolean renderCancelar = true;
    private boolean renderedDisponivel = false;
    private boolean ocultarHorarioAlternativo = true;
    private MacFilial macFilial = null;
    private int protocolo = 0;
    private int idIndex = -1;
    private int idIndexEndereco = -1;
    private int id_protocolo = -1;
    private List<Endereco> listaEnderecos = new ArrayList();
    private PessoaEndereco enderecoFisica = new PessoaEndereco();
    private String enviarPara = "contabilidade";
    private PessoaEndereco enderecoFilial = new PessoaEndereco();
    private boolean imprimirPro = false;
    private ConfiguracaoHomologacao configuracaoHomologacao = new ConfiguracaoHomologacao();
    private int counter = 0;

    private Registro registro = new Registro();
    private boolean visibleModal = false;
    private String tipoTelefone = "telefone";
    private Cancelamento cancelamento = new Cancelamento();

    public AgendamentoBean() {
        macFilial = (MacFilial) GenericaSessao.getObject("acessoFilial");

        Dao dao = new Dao();
        registro = (Registro) dao.find(new Registro(), 1);

        if (macFilial != null) {
            this.loadListaHorarios();
            this.loadListaHorariosTransferencia();
        }
    }

    public boolean validaAdmissao() {
        if (fisica.getId() != -1 && juridica.getId() != -1 && !pessoaEmpresa.getAdmissao().isEmpty() && pessoaEmpresa.getId() == -1) {
            HomologacaoDB db = new HomologacaoDBToplink();

            PessoaEmpresa pe = db.pesquisaPessoaEmpresaAdmissao(fisica.getId(), juridica.getId(), pessoaEmpresa.getAdmissao());

            if (pe != null) {
                int[] ids = new int[2];
                ids[0] = 2;
                ids[1] = 4;
                Agendamento a = db.pesquisaAgendamentoPorPessoaEmpresa(pe.getId(), ids);

                if (a != null) {
                    GenericaMensagem.fatal("Atenção", "Esse agendamento já foi " + a.getStatus().getDescricao() + "!");
                    return false;
                }

                pessoaEmpresa = pe;
            }
        }
        return true;
    }

    public void actionValidaAdmissao() {
        validaAdmissao();
    }

    public boolean validaDemissao() {
        if (fisica.getId() != -1 && juridica.getId() != -1 && !pessoaEmpresa.getDemissao().isEmpty() && pessoaEmpresa.getId() == -1) {
            HomologacaoDB db = new HomologacaoDBToplink();

            PessoaEmpresa pe = db.pesquisaPessoaEmpresaDemissao(fisica.getId(), juridica.getId(), pessoaEmpresa.getDemissao());

            if (pe != null) {
                int[] ids = new int[2];
                ids[0] = 2;
                ids[1] = 4;
                Agendamento a = db.pesquisaAgendamentoPorPessoaEmpresa(pe.getId(), ids);

                if (a != null) {
                    GenericaMensagem.fatal("Atenção", "Esse agendamento já foi " + a.getStatus().getDescricao() + "!");
                    return false;
                }
                pessoaEmpresa = pe;
            }
        }
        return true;
    }

    public void actionValidaDemissao() {
        validaDemissao();
    }

    public void alterarTipoMascara() {
        if (tipoTelefone.equals("telefone")) {
            tipoTelefone = "celular";
        } else {
            tipoTelefone = "telefone";
        }
        agendamento.setTelefone("");
    }

    public final void loadListaHorariosTransferencia() {
        listaHorarioTransferencia.clear();
        idHorarioTransferencia = 0;

        HomologacaoDB db = new HomologacaoDBToplink();
        int idDiaSemana = DataHoje.diaDaSemana(dataTransferencia);
        List<Horarios> select = db.pesquisaTodosHorariosDisponiveis(macFilial.getFilial().getId(), idDiaSemana);
        if (select.isEmpty()) {
            listaHorarioTransferencia.add(new SelectItem(0, "Nenhum horário encontrado", "0"));
            return;
        }

        int qnt;
        int j = 0;
        for (Horarios listh : select) {
            qnt = db.pesquisaQntdDisponivel(macFilial.getFilial().getId(), listh, getDataTransferencia());
            if (qnt == -1) {
                listaHorarioTransferencia.add(new SelectItem(0, "Nenhum horário disponível", "0"));
                return;
            }
            if (qnt > 0) {
                listaHorarioTransferencia.add(new SelectItem(j, listh.getHora() + " (" + qnt + ")", String.valueOf(listh.getId())));
                j++;
            }
        }

        if (listaHorarioTransferencia.isEmpty()) {
            listaHorarioTransferencia.add(new SelectItem(0, "Nenhum horário encontrado", "0"));
        }
    }

    public final void loadListaHorarios() {
        listaHorarios.clear();
        if (macFilial == null) {
            return;
        }

        int idNrStatus = Integer.parseInt(((SelectItem) getListaStatus().get(idStatus)).getDescription());
        int diaDaSemana = DataHoje.diaDaSemana(data);
        renderedDisponivel = idNrStatus != 2 && idNrStatus != 3 && idNrStatus != 4 && idNrStatus != 5 && idNrStatus != 6 && idNrStatus != 7;
        HomologacaoDB homologacaoDB = new HomologacaoDBToplink();
        List<Agendamento> agendamentos;
        List<Horarios> horarios;
        if (idNrStatus == 1 || idNrStatus == 6) {
            horarios = homologacaoDB.pesquisaTodosHorariosDisponiveis(macFilial.getFilial().getId(), diaDaSemana);
            for (int j = 0; j < horarios.size(); j++) {
                ListaAgendamento listaAgendamento = new ListaAgendamento();
                if (idNrStatus == 1) {
                    int quantidade = homologacaoDB.pesquisaQntdDisponivel(macFilial.getFilial().getId(), horarios.get(j), getData());
                    if (quantidade == -1) {
                        GenericaMensagem.error("Erro", "Não foi possível pesquisar horários disponíveis!");
                        break;
                    }
                    if (quantidade > 0) {
                        listaAgendamento.setQuantidade(quantidade);
                        listaAgendamento.getAgendamento().setHorarios(horarios.get(j));
                        listaHorarios.add(listaAgendamento);
                    }
                } else {
                    listaAgendamento.getAgendamento().setHorarios(horarios.get(j));
                    listaHorarios.add(listaAgendamento);
                }
            }
        } else {
            agendamentos = homologacaoDB.pesquisaAgendamento(idNrStatus, macFilial.getFilial().getId(), getData(), null, 0, 0, 0, false, false);
            for (Agendamento agenda : agendamentos) {
                ListaAgendamento listaAgendamento = new ListaAgendamento();
                Usuario u = new Usuario();
                listaAgendamento.setAgendamento(agenda);

                if (agenda.getAgendador() == null) {
                    listaAgendamento.setUsuarioAgendador("** Web User **");
                } else {
                    listaAgendamento.setUsuarioAgendador(agenda.getAgendador().getPessoa().getNome());
                }

                AtendimentoDB dbat = new AtendimentoDBTopLink();
                if (dbat.pessoaOposicao(listaAgendamento.getAgendamento().getPessoaEmpresa().getFisica().getPessoa().getDocumento())) {
                    listaAgendamento.setTblEstilo("tblAgendamentoOposicaox");
                }

                listaHorarios.add(listaAgendamento);
            }
        }
    }

    public void salvarTransferencia() {
        if (getDataTransferencia().getDay() == 6 || getDataTransferencia().getDay() == 0) {
            GenericaMensagem.warn("Atenção", "Fins de semana não permitido!");
            return;
        }
        if (DataHoje.converteDataParaInteger(DataHoje.converteData(getDataTransferencia()))
                < DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()))) {
            GenericaMensagem.warn("Atenção", "Data anterior ao dia de hoje!");
            return;
        }

        if (DataHoje.converteDataParaInteger(((new DataHoje()).incrementarMeses(registro.getHomolocaoLimiteMeses(), DataHoje.data())))
                < DataHoje.converteDataParaInteger(DataHoje.converteData(getDataTransferencia()))) {
            GenericaMensagem.warn("Atenção", "Data maior que " + registro.getHomolocaoLimiteMeses() + " meses!");
            return;
        }

        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        agendamento.setDtData(dataTransferencia);
        if (listaHorarioTransferencia.isEmpty()) {
            GenericaMensagem.warn("Atenção", "Não existem horários para esse dia da semana!");
            return;
        }

        Horarios h = (Horarios) sv.find("Horarios", Integer.valueOf(listaHorarioTransferencia.get(idHorarioTransferencia).getDescription()));
        agendamento.setHorarios(h);

        sv.abrirTransacao();
        if (!sv.alterarObjeto(agendamento)) {
            sv.desfazerTransacao();
            GenericaMensagem.error("Erro", "Não foi possível transferir horário, tente novamente!");
            return;
        }
        sv.comitarTransacao();
        GenericaMensagem.info("Sucesso", "Horário transferido!");
        loadListaHorarios();
        PF.closeDialog("dlg_transferir_horario");
    }

    public String imprimirPlanilha() {
        if (listaMovimento.isEmpty()) {
            return null;
        }
        ImprimirBoleto imp = new ImprimirBoleto();
        List<Movimento> lista = new ArrayList();
        List<Float> listaValores = new ArrayList<Float>();
        for (int i = 0; i < listaMovimento.size(); i++) {
            SalvarAcumuladoDB sadb = new SalvarAcumuladoDBToplink();
            Movimento m = (Movimento) sadb.find("Movimento", (Integer) ((List) listaMovimento.get(i)).get(0));
            lista.add(m);
            listaValores.add(m.getValor());
        }
        if (!lista.isEmpty()) {
            imp.imprimirPlanilha(lista, listaValores, false, false);
            imp.visualizar(null);
        }
        return null;
    }

    public String enviarPlanilha() {
        if (listaMovimento.isEmpty()) {
            return null;
        }
        ImprimirBoleto imp = new ImprimirBoleto();
        List<Movimento> lista = new ArrayList();
        List<Float> listaValores = new ArrayList<Float>();
        Dao dao = new Dao();
        for (Object listaMovimento1 : listaMovimento) {
            Movimento m = (Movimento) dao.find(new Movimento(), (Integer) ((List) listaMovimento1).get(0));
            lista.add(m);
            listaValores.add(m.getValor());
        }

        if (!lista.isEmpty()) {
            imp.imprimirPlanilha(lista, listaValores, false, false);
        }

        try {
            Dao di = new Dao();
            Pessoa pessoa = new Pessoa();
            if (enviarPara.equals("contabilidade")) {
                if (juridica.getContabilidade() == null) {
                    GenericaMensagem.warn("Atenção", "Empresa sem Contabilidade!");
                    return null;
                }

                pessoa = (Pessoa) di.find(juridica.getContabilidade().getPessoa());
                if (emailEmpresa.isEmpty()) {
                    if (pessoa.getEmail1().isEmpty()) {
                        GenericaMensagem.warn("Atenção", "Contabilidade sem Email para envio!");
                        return null;
                    }
                } else {
                    pessoa.setEmail1(emailEmpresa);
                }
            } else {
                pessoa = (Pessoa) di.find(juridica.getPessoa());
                if (emailEmpresa.isEmpty()) {
                    if (pessoa.getEmail1().isEmpty()) {
                        GenericaMensagem.warn("Atenção", "Empresa sem Email para envio!");
                        return null;
                    }
                } else {
                    pessoa.setEmail1(emailEmpresa);
                }
            }

            String nome = imp.criarLink(pessoa, registro.getUrlPath() + "/Sindical/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/boletos");
            List<Pessoa> p = new ArrayList();
            p.add(pessoa);
            Mail mail = new Mail();
            Email email = new Email(
                    -1,
                    DataHoje.dataHoje(),
                    DataHoje.livre(new Date(), "HH:mm"),
                    (Usuario) GenericaSessao.getObject("sessaoUsuario"),
                    (Rotina) dao.find(new Rotina(), 113),
                    null,
                    "Envio de Débitos",
                    "",
                    false,
                    false
            );

            if (!registro.isEnviarEmailAnexo()) {
                email.setMensagem(""
                        + " <div style='background:#00ccff; padding: 15px; font-size:13pt'>"
                        + " Envio cadastrado para <b>" + pessoa.getNome() + " </b>"
                        + " </div>"
                        + " <br />"
                        + " <h5>Visualize sua planilha de débitos clicando no link abaixo</h5>"
                        + " <br /><br />"
                        + " <a href='" + registro.getUrlPath() + "/Sindical/acessoLinks.jsf?cliente=" + ControleUsuarioBean.getCliente() + "&amp;arquivo=" + nome + "'>Clique aqui para abrir Planilha de Débitos</a><br />"
                );
            } else {
                List<File> fls = new ArrayList<File>();
                fls.add(new File(imp.getPathPasta() + "/" + nome));
                mail.setFiles(fls);
                email.setMensagem(""
                        + " <div style='background:#00ccff; padding: 15px; font-size:13pt'>         "
                        + "     Envio cadastrado para <b>" + pessoa.getNome() + " </b>              "
                        + " </div><br />                                                            "
                        + " <h5>Baixe sua planilha de débitos anexado neste email</h5><br /><br />   "
                );
            }
            mail.setEmail(email);
            List<EmailPessoa> emailPessoas = new ArrayList<EmailPessoa>();
            EmailPessoa emailPessoa = new EmailPessoa();
            List<Pessoa> pessoas = (List<Pessoa>) p;
            for (Pessoa p1 : pessoas) {
                emailPessoa.setDestinatario(p1.getEmail1());
                emailPessoa.setPessoa(p1);
                emailPessoa.setRecebimento(null);
                emailPessoas.add(emailPessoa);
                mail.setEmailPessoas(emailPessoas);
                emailPessoa = new EmailPessoa();
            }
            String[] retorno = mail.send("personalizado");

            if (!retorno[1].isEmpty()) {
                GenericaMensagem.error("Erro", retorno[1]);
            } else {
                GenericaMensagem.info("OK", retorno[0]);
            }
        } catch (Exception erro) {
            System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());

        }
        return null;
    }

    public String atualizar() {
        return "agendamento";
    }

    public List<SelectItem> getListaStatus() {
        if (listaStatus.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            List<Status> list = (List<Status>) salvarAcumuladoDB.listaObjeto("Status");
            for (int i = 0; i < list.size(); i++) {
                listaStatus.add(new SelectItem(i, list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
            }
        }
        return listaStatus;
    }

    public List<SelectItem> getListaDemissao() {
        if (listaDemissao.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            List<Demissao> list = (List<Demissao>) salvarAcumuladoDB.listaObjeto("Demissao");
            for (int i = 0; i < list.size(); i++) {
                listaDemissao.add(new SelectItem(i, list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
            }
        }
        return listaDemissao;
    }

    public List<SelectItem> getListaHorarioTransferencia() {
        return listaHorarioTransferencia;
    }

    public boolean pesquisarFeriado() {
        FeriadosDao feriadosDao = new FeriadosDao();
        if (macFilial.getFilial().getFilial().getId() != -1) {
            List<Feriados> feriados = feriadosDao.pesquisarPorDataFilialEData(DataHoje.converteData(data), macFilial.getFilial());
            if (!feriados.isEmpty()) {
                return true;
            } else {
                List<Feriados> listFeriados = (List<Feriados>) feriadosDao.pesquisarPorData(DataHoje.converteData(getData()));
                if (!listFeriados.isEmpty()) {
                    for (Feriados f : listFeriados) {
                        if (f.getCidade() == null) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;

    }

    public void agendar(Agendamento a) {
        limpar();
        loadListaHorariosTransferencia();

        if (getData().getDay() == 6 || getData().getDay() == 0) {
            GenericaMensagem.warn("Atenção", "Fins de semana não permitido!");
            return;
        }

        emailEmpresa = "";
        idMotivoDemissao = 0;
        int nrAgendamentoRetroativo = DataHoje.converteDataParaInteger(DataHoje.converteData(registro.getAgendamentoRetroativo()));
        int nrData = DataHoje.converteDataParaInteger(DataHoje.converteData(getData()));
        int nrDataHoje = DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()));
        if (nrAgendamentoRetroativo < nrDataHoje) {
            if (nrData < nrDataHoje) {
                GenericaMensagem.warn("Atenção", "Data anterior ao dia de hoje!");
                return;
            }
        } else {
            GenericaMensagem.info("Informação", "Agendamento retroativo liberado até dia " + registro.getAgendamentoRetroativoString());
        }

        if (DataHoje.converteDataParaInteger(((new DataHoje()).incrementarMeses(registro.getHomolocaoLimiteMeses(), DataHoje.data()))) < nrData) {
            GenericaMensagem.warn("Atenção", "Data maior que " + registro.getHomolocaoLimiteMeses() + " meses!");
            return;
        }
        protocolo = 0;
        if (profissao.getId() == -1) {
            profissao = (Profissao) new Dao().find(new Profissao(), 0);
            profissao.setProfissao("");
        }

        switch (Integer.parseInt(((SelectItem) getListaStatus().get(idStatus)).getDescription())) {
            case 1: {
                if (getData() == null) {
                    GenericaMensagem.warn("Atenção", "Selecione uma data para Agendamento!");
                    return;
                } else {
                    renderCancelarHorario = false;
                    renderCancelar = true;
                    if (pesquisarFeriado()) {
                        GenericaMensagem.warn("Atenção", "Esta data esta cadastrada como Feriado!");
                        return;
                    } else {
                        agendamento.setData(DataHoje.converteData(getData()));
                        Horarios horarios = a.getHorarios();
                        agendamento.setHorarios(horarios);
                    }
                }

                visibleModal = true;
                PF.openDialog("dlg_agendamento");
                break;
            }
            case 2: {
                PessoaEnderecoDB db = new PessoaEnderecoDBToplink();
                agendamento = a;
                fisica = a.getPessoaEmpresa().getFisica();
                enderecoFisica = db.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 1);
                juridica = a.getPessoaEmpresa().getJuridica();
                profissao = a.getPessoaEmpresa().getFuncao();
                pessoaEmpresa = a.getPessoaEmpresa();
                renderCancelarHorario = true;
                renderCancelar = false;
                for (int i = 0; i < getListaDemissao().size(); i++) {
                    if (Integer.parseInt(getListaDemissao().get(i).getDescription()) == a.getDemissao().getId()) {
                        idMotivoDemissao = (Integer) getListaDemissao().get(i).getValue();
                        break;
                    }
                }
                //tipoAviso = String.valueOf(pessoaEmpresa.isAvisoTrabalhado());

                visibleModal = true;
                PF.openDialog("dlg_agendamento");
                break;
            }
            case 3: {
                break;
            }
            case 4: {
                break;
            }
            case 5: {
                break;
            }
            case 6: {
                if (getData() == null) {
                    GenericaMensagem.warn("Atenção", "Selecione uma data para Agendamento!");
                    return;
                } else {
                    renderCancelarHorario = false;
                    renderCancelar = true;
                    if (pesquisarFeriado()) {
                        GenericaMensagem.warn("Atenção", "Esta data esta cadastrada como Feriado!");
                        return;
                    } else {
                        agendamento.setData(DataHoje.converteData(getData()));
                        agendamento.setHorarios(a.getHorarios());
                    }
                }

                visibleModal = true;
                PF.openDialog("dlg_agendamento");
                break;
            }
        }

        if (agendamento.getTelefone().length() > 14) {
            tipoTelefone = "celular";
        } else {
            tipoTelefone = "telefone";
        }
    }

    public void save() {
        if (!validaAdmissao()) {
            return;
        }

        if (!validaDemissao()) {
            return;
        }
        styleDestaque = "";
        Dao dao = new Dao();
        if (configuracaoHomologacao.isValidaNome()) {
            if (fisica.getPessoa().getNome().isEmpty()) {
                GenericaMensagem.warn("Atenção", "Digite o nome do Funcionário!");
                return;
            }
        }

        if (configuracaoHomologacao.isValidaDataNascimento()) {
            if (fisica.getNascimento().isEmpty()) {
                GenericaMensagem.warn("Validação", "Informar data de nascimento!");
                return;
            }
        }

        if (configuracaoHomologacao.isValidaFuncao()) {
            if (profissao.getId() == -1 || profissao.getId() == 0) {
                GenericaMensagem.warn("Atenção", "Informar a Função!");
                return;
            }
        }

        if (configuracaoHomologacao.isValidaCarteira()) {
            if (fisica.getCarteira().isEmpty()) {
                GenericaMensagem.warn("Atenção", "Informar a Carteira!");
                return;
            }
        }

        if (configuracaoHomologacao.isValidaSerie()) {
            if (fisica.getSerie().isEmpty()) {
                GenericaMensagem.warn("Atenção", "Informar a Série!");
                return;
            }
        }

        if (!getStrContribuinte().isEmpty()) {
            GenericaMensagem.error("Atenção", getStrContribuinte());
            return;
        }

        FisicaDB dbFis = new FisicaDBToplink();
        List listDocumento;
        imprimirPro = false;
        DataHoje dataH = new DataHoje();
        Demissao demissao = (Demissao) dao.find(new Demissao(), Integer.parseInt(((SelectItem) getListaDemissao().get(idMotivoDemissao)).getDescription()));

        if (!pessoaEmpresa.getAdmissao().isEmpty() && pessoaEmpresa.getAdmissao() != null) {
            if (DataHoje.converteDataParaInteger(pessoaEmpresa.getAdmissao())
                    > DataHoje.converteDataParaInteger(pessoaEmpresa.getDemissao())) {
                GenericaMensagem.warn("Atenção", "Data de Admissão é maior que data de Demissão!");
                return;
            }
        } else {
            GenericaMensagem.warn("Atenção", "Data de Admissão é obrigatória!");
            return;
        }

        if (!pessoaEmpresa.getDemissao().isEmpty() && pessoaEmpresa.getDemissao() != null) {
            if (demissao.getId() == 1) {
                if (DataHoje.converteDataParaInteger(pessoaEmpresa.getDemissao())
                        > DataHoje.converteDataParaInteger(dataH.incrementarMeses(1, DataHoje.data()))) {
                    GenericaMensagem.warn("Atenção", "Por " + demissao.getDescricao() + " data de Demissão não pode ser maior que 30 dias!");
                    return;
                }
            } else if (demissao.getId() == 2) {
                if (DataHoje.converteDataParaInteger(pessoaEmpresa.getDemissao())
                        > DataHoje.converteDataParaInteger(dataH.incrementarMeses(3, DataHoje.data()))) {
                    GenericaMensagem.warn("Atenção", "Por " + demissao.getDescricao() + " data de Demissão não pode ser maior que 90 dias!");
                    return;
                }
            } else if (demissao.getId() == 3) {
                if (DataHoje.converteDataParaInteger(pessoaEmpresa.getDemissao())
                        > DataHoje.converteDataParaInteger(dataH.incrementarDias(10, DataHoje.data()))) {
                    GenericaMensagem.warn("Atenção", "Por " + demissao.getDescricao() + " data de Demissão não pode ser maior que 10 dias!");
                    return;
                }
            }
        } else {
            GenericaMensagem.warn("Atenção", "Data de Demissão é obrigatória!");
            return;
        }

        // SALVAR FISICA -----------------------------------------------
        fisica.getPessoa().setTipoDocumento((TipoDocumento) dao.find(new TipoDocumento(), 1));
        if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(fisica.getPessoa().getDocumento()))) {
            GenericaMensagem.warn("Atenção", "Documento Inválido!");
            return;
        }

        dao.openTransaction();
        if (fisica.getId() == -1) {
            if (!dbFis.pesquisaFisicaPorNomeNascRG(fisica.getPessoa().getNome(),
                    fisica.getDtNascimento(),
                    fisica.getRg()).isEmpty()) {
                GenericaMensagem.warn("Atenção", "Esta pessoa já esta cadastrada!");
                dao.rollback();
                return;
            }
            listDocumento = dbFis.pesquisaFisicaPorDoc(fisica.getPessoa().getDocumento());
            if (!listDocumento.isEmpty()) {
                dao.rollback();
                GenericaMensagem.warn("Atenção", "Documento já existente!");
                return;
            }

            if (dao.save(fisica.getPessoa())) {
                dao.save(fisica);
            } else {
                dao.rollback();
                GenericaMensagem.error("Erro", "Erro ao inserir Pessoa!");
                return;
            }
        } else {
            listDocumento = dbFis.pesquisaFisicaPorDoc(fisica.getPessoa().getDocumento());
            for (Object listDocumento1 : listDocumento) {
                if (!listDocumento.isEmpty() && ((Fisica) listDocumento1).getId() != fisica.getId()) {
                    dao.rollback();
                    GenericaMensagem.warn("Atenção", "Documento já existente!");
                    return;
                }
            }
            List<Fisica> fisi = dbFis.pesquisaFisicaPorNomeNascRG(fisica.getPessoa().getNome(),
                    fisica.getDtNascimento(),
                    fisica.getRg());
            if (!fisi.isEmpty()) {
                for (Fisica fisi1 : fisi) {
                    if (fisi1.getId() != fisica.getId()) {
                        dao.rollback();
                        GenericaMensagem.warn("Atenção", "Esta pessoa já esta cadastrada!");
                        return;
                    }
                }
            }
            if (dao.update(fisica.getPessoa())) {
                if (dao.update(fisica)) {
                } else {
                    dao.rollback();
                    GenericaMensagem.error("Erro", "Não foi possível inserir Pessoa Física, tente novamente!");
                    return;
                }
            } else {
                dao.rollback();
                GenericaMensagem.error("Erro", "Não foi possível inserir Pessoa, tente novamente!");
                return;
            }
        }
        HomologacaoDB dba = new HomologacaoDBToplink();
        Agendamento age = dba.pesquisaFisicaAgendada(fisica.getId(), juridica.getId());
        if (age != null && agendamento.getId() == -1) {
            dao.rollback();
            GenericaMensagem.warn("Atenção", "Pessoa já foi agendada, na data " + age.getData());
            return;
        }

        boolean isOposicao = false;
        AtendimentoDB dbat = new AtendimentoDBTopLink();
        if (dbat.pessoaOposicao(fisica.getPessoa().getDocumento())) {
            isOposicao = true;
        }

        PessoaEnderecoDB dbp = new PessoaEnderecoDBToplink();

        if (enderecoFisica.getId() == -1) {
            if (enderecoFisica.getEndereco().getId() != -1) {
                enderecoFisica.setPessoa(fisica.getPessoa());
                PessoaEndereco pesEnd = enderecoFisica;
                Object ids[] = {1, 3, 4};
                for (int i = 0; i < ids.length; i++) {
                    pesEnd.setTipoEndereco((TipoEndereco) dao.find(new TipoEndereco(), ids[i]));
                    if (!dao.save(pesEnd)) {
                        dao.rollback();
                        GenericaMensagem.error("Erro", "Não foi possível inserir Pessoa Endereço, tente novamente!");
                        return;
                    }
                    pesEnd = new PessoaEndereco();
                    pesEnd.setComplemento(enderecoFisica.getComplemento());
                    pesEnd.setEndereco(enderecoFisica.getEndereco());
                    pesEnd.setNumero(enderecoFisica.getNumero());
                    pesEnd.setPessoa(enderecoFisica.getPessoa());
                }
            }
        } else {
            List<PessoaEndereco> ends = dbp.pesquisaEndPorPessoa(fisica.getPessoa().getId());
            for (PessoaEndereco end : ends) {
                end.setComplemento(enderecoFisica.getComplemento());
                end.setEndereco(enderecoFisica.getEndereco());
                end.setNumero(enderecoFisica.getNumero());
                end.setPessoa(enderecoFisica.getPessoa());
                if (!dao.update(end)) {
                    dao.rollback();
                    GenericaMensagem.error("Erro", "Não foi possível atualizar Pessoa Endereço, tente novamente!");
                    return;
                }
            }
        }
        // -------------------------------------------------------------
        int idStatusI = Integer.parseInt(((SelectItem) getListaStatus().get(idStatus)).getDescription());
        if (profissao.getId() == -1) {
            profissao = (Profissao) new Dao().find(new Profissao(), 0);
            profissao.setProfissao("");
        }

        switch (idStatusI) {
            case 1: {
                pessoaEmpresa.setFisica(fisica);
                pessoaEmpresa.setJuridica(juridica);
                pessoaEmpresa.setFuncao(profissao);
                pessoaEmpresa.setPrincipal(false);
                //pessoaEmpresa.setAvisoTrabalhado(Boolean.valueOf(tipoAviso));
                agendamento.setDemissao(demissao);
                agendamento.setHomologador(null);
                agendamento.setPessoaEmpresa(pessoaEmpresa);
                agendamento.setStatus((Status) dao.find(new Status(), 2));
                break;
            }
            case 2: {
                pessoaEmpresa.setFisica(fisica);
                pessoaEmpresa.setJuridica(juridica);
                //pessoaEmpresa.setAvisoTrabalhado(Boolean.valueOf(tipoAviso));
                pessoaEmpresa.setFuncao(profissao);
                agendamento.setDemissao(demissao);
                agendamento.setPessoaEmpresa(pessoaEmpresa);
                break;
            }
            case 6: {
                pessoaEmpresa.setFisica(fisica);
                pessoaEmpresa.setJuridica(juridica);
                pessoaEmpresa.setFuncao(profissao);
                //pessoaEmpresa.setAvisoTrabalhado(Boolean.valueOf(tipoAviso));
                agendamento.setDemissao(demissao);
                agendamento.setHomologador(null);
                agendamento.setPessoaEmpresa(pessoaEmpresa);
                agendamento.setStatus((Status) dao.find(new Status(), 2));
                break;
            }
        }

        if (pessoaEmpresa.getId() == -1) {
            if (!dao.save(pessoaEmpresa)) {
                dao.rollback();
                GenericaMensagem.error("Erro", "Não foi possível inserir Pessoa Empresa!");
                return;
            }
        } else {
            if (!dao.update(pessoaEmpresa)) {
                dao.rollback();
                GenericaMensagem.error("Erro", "Não foi possível alterar Pessoa Empresa!");
                return;
            }
        }
        if (configuracaoHomologacao.isValidaContato()) {
            if (agendamento.getContato().isEmpty()) {
                dao.rollback();
                GenericaMensagem.warn("Atenção", "Informar o nome do Contato!");
                return;
            }
        }
        if (configuracaoHomologacao.isValidaTelefone()) {
            if (agendamento.getTelefone().isEmpty()) {
                dao.rollback();
                GenericaMensagem.warn("Atenção", "Informar o telefone para contato!");
                return;
            }
        }
        if (configuracaoHomologacao.isValidaEmail()) {
            if (agendamento.getEmail().isEmpty()) {
                dao.rollback();
                GenericaMensagem.warn("Atenção", "Informar o email!");
                return;
            }
        }
        if (agendamento.getId() == -1) {
            if (idStatusI == 1) {
                if (!dba.existeHorarioDisponivel(agendamento.getDtData(), agendamento.getHorarios())) {
                    dao.rollback();
                    loadListaHorariosTransferencia();
                    GenericaMensagem.fatal("Atenção", "Não existe mais disponibilidade para o horário agendado!");
                    ocultarHorarioAlternativo = false;
                    return;
                }
            }
            agendamento.setFilial(macFilial.getFilial());
            agendamento.setAgendador((Usuario) GenericaSessao.getObject("sessaoUsuario")); // USUARIO SESSAO
            agendamento.setRecepcao(null);
            agendamento.setDtEmissao(DataHoje.dataHoje());
            if (dao.save(agendamento)) {
                dao.commit();
//                msgConfirma = "Para imprimir Protocolo clique aqui! ";
                GenericaMensagem.info("Sucesso", "Agendamento Concluído!");
            } else {
                GenericaMensagem.fatal("Atenção", "Erro ao realizar este Agendamento!");
                dao.rollback();
            }
        } else {
            if (dao.update(agendamento)) {
                dao.commit();
                if (isOposicao) {
                    //msgConfirma = "Agendamento atualizado com Sucesso! imprimir Protocolo clicando aqui! Pessoa cadastrada em oposição. ";
                    styleDestaque = "color: red; font-size: 14pt; font-weight:bold";
                } else {
                    //msgConfirma = "Agendamento atualizado com Sucesso! imprimir Protocolo clicando aqui! ";
                    styleDestaque = "";
                }
                GenericaMensagem.info("Sucesso", "Alteração de Agendamento Concluído!");
            } else {
                GenericaMensagem.fatal("Atenção", "Erro ao atualizar Agendamento!");
                dao.rollback();
            }
        }

        renderCancelarHorario = true;
        loadListaHorariosTransferencia();
        loadListaHorarios();
        id_protocolo = agendamento.getId();
        ocultarHorarioAlternativo = true;
        imprimirPro = true;
    }

    public String salvarMais() {

        return null;
    }

    public String cancelar() {
        strEndereco = "";
        //tipoAviso = "true";
        fisica = new Fisica();
        cepEndereco = "";
        listaEnderecos.clear();
        agendamento = new Agendamento();
        pessoaEmpresa = new PessoaEmpresa();
        profissao = new Profissao();
        juridica = new Juridica();
        listaMovimento.clear();
        enderecoFisica = new PessoaEndereco();
        imprimirPro = false;
        loadListaHorariosTransferencia();
        emailEmpresa = "";
        return "agendamento";
    }

    public String cancelarHorario() {
        PessoaEmpresaDB dbPesEmp = new PessoaEmpresaDBToplink();
        Dao dao = new Dao();
        agendamento.setStatus((Status) dao.find(new Status(), 3));
        dao.openTransaction();
        if (!dao.update(agendamento)) {
            GenericaMensagem.warn("Erro", "Ao cancelar horário!");
            dao.rollback();
            return "agendamento";
        }
        cancelamento.setData(DataHoje.data());
        cancelamento.setUsuario(new PessoaUtilitarios().getUsuarioSessao());
        cancelamento.setAgendamento(agendamento);
        if (!dao.save(cancelamento)) {
            GenericaMensagem.warn("Erro", "Ao cancelar horário!");
            dao.rollback();
            return "agendamento";
        }
        GenericaMensagem.info("Sucesso", "Agendamento Cancelado!");
        dao.commit();
        cancelamento = new Cancelamento();
        pessoaEmpresa.setDtDemissao(null);

        PessoaEmpresa pem = dbPesEmp.pesquisaPessoaEmpresaPorFisica(pessoaEmpresa.getFisica().getId());

        if (pem.getId() == -1) {
            pessoaEmpresa.setPrincipal(true);
        }
        dbPesEmp.update(pessoaEmpresa);
        strEndereco = "";
        renderCancelarHorario = false;
        renderCancelar = true;
        //tipoAviso = "true";
        fisica = new Fisica();
        agendamento = new Agendamento();
        pessoaEmpresa = new PessoaEmpresa();
        profissao = new Profissao();
        juridica = new Juridica();
        listaMovimento.clear();
        enderecoFisica = new PessoaEndereco();

        setVisibleModal(false);
        loadListaHorarios();
        loadListaHorariosTransferencia();
        return "agendamento";
    }

    public void limpar() {
        strEndereco = "";
        //tipoAviso = "true";
        fisica = new Fisica();
        pessoaEmpresa = new PessoaEmpresa();
        agendamento = new Agendamento();
        profissao = new Profissao();
        juridica = new Juridica();
        listaMovimento.clear();
        protocolo = 0;
        enderecoFisica = new PessoaEndereco();
    }

    public void limparMais() {
        strEndereco = "";
        //tipoAviso = "true";
        fisica = new Fisica();
        pessoaEmpresa = new PessoaEmpresa();
        agendamento = new Agendamento();
        profissao = new Profissao();
        protocolo = 0;
        enderecoFisica = new PessoaEndereco();
    }

    public void pesquisarFuncionarioCPF() throws IOException {
        if (agendamento.getId() == -1){
            styleDestaque = "";
            if ((!fisica.getPessoa().getDocumento().isEmpty() && !fisica.getPessoa().getDocumento().equals("___.___.___-__"))) {
                if (!fisica.getPessoa().getDocumento().isEmpty() && !fisica.getPessoa().getDocumento().equals("___.___.___-__")) {
                    if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(fisica.getPessoa().getDocumento()))) {
                        GenericaMensagem.warn("Atenção", "Documento Inválido!");
                        return;
                    }
                }

                FisicaDB dbFis = new FisicaDBToplink();
                HomologacaoDB db = new HomologacaoDBToplink();
                PessoaEnderecoDB dbe = new PessoaEnderecoDBToplink();
                List<Fisica> listFisica = new ArrayList();
                String documento = fisica.getPessoa().getDocumento();
                if ((!fisica.getPessoa().getDocumento().isEmpty() && !fisica.getPessoa().getDocumento().equals("___.___.___-__"))) {
                    listFisica = dbFis.pesquisaFisicaPorDocSemLike(documento);
                }
                if (listFisica.isEmpty()) {
                    if (!fisica.getPessoa().getNome().isEmpty() && !fisica.getNascimento().isEmpty()) {
                        Fisica f = (Fisica) dbFis.pesquisaFisicaPorNomeNascimento(fisica.getPessoa().getNome().trim(), fisica.getDtNascimento());
                        if (f != null) {
                            listFisica.add(f);
                        }
                    }
                }
                List<Oposicao> listao = db.pesquisaFisicaOposicaoSemEmpresa(documento);
                PessoaEmpresa pem = db.pesquisaPessoaEmpresaPertencente(documento);

                if (!listFisica.isEmpty()) {
                    // AQUI
    //                Agendamento age = db.pesquisaFisicaAgendada(listFisica.get(0).getId());
    //                if (age != null) {
    //                    msgConfirma = "CPF já foi agendado:" + age.getData() + " às " + age.getHorarios().getHora() + " h(s) ";
    //                    return;
    //                }
                }

                // SEM PESSOA FISICA E SEM OPOSICAO
                if (listFisica.isEmpty() && listao.isEmpty()) {
                    //FacesContext.getCurrentInstance().getExternalContext().redirect("/Sindical/agendamento.jsf");
                    //msgConfirma = "CPF verificado com sucesso";
                    // SEM PESSOA FISICA E COM OPOSICAO    
                } else if (listFisica.isEmpty() && !listao.isEmpty()) {
                    GenericaMensagem.warn("Atenção", "CPF cadastrado em oposição data: " + listao.get(0).getEmissao());

                    styleDestaque = "color: red; font-size: 14pt; font-weight:bold";
                    fisica.getPessoa().setNome(listao.get(0).getOposicaoPessoa().getNome());
                    fisica.setRg(listao.get(0).getOposicaoPessoa().getRg());
                    fisica.setSexo(listao.get(0).getOposicaoPessoa().getSexo());
                    fisica.getPessoa().setDocumento(documento);
                    juridica = listao.get(0).getJuridica();

                    PF.openDialog("dlg_oposicao");
                    // COM FISICA, COM PESSOA EMPRESA E SEM OPOSICAO    
                } else if (!listFisica.isEmpty() && pem != null && listao.isEmpty()) {
                    //msgConfirma = "CPF verificado com sucesso";
                    pessoaEmpresa = pem;
                    fisica = pessoaEmpresa.getFisica();
                    profissao = (pessoaEmpresa.getFuncao() == null) ? new Profissao() : pessoaEmpresa.getFuncao();
                    GenericaSessao.put("juridicaPesquisa", pessoaEmpresa.getJuridica());
                    enderecoFisica = dbe.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 3);
                    protocolo = 0;
                    //FacesContext.getCurrentInstance().getExternalContext().redirect("/Sindical/agendamento.jsf");
                    // COM FISICA, SEM PESSOA EMPRESA E SEM OPOSICAO    
                } else if (!listFisica.isEmpty() && pem == null && listao.isEmpty()) {
                    //msgConfirma = "CPF verificado com sucesso";
                    fisica = listFisica.get(0);
                    fisica.getPessoa().setDocumento(documento);
                    pessoaEmpresa = new PessoaEmpresa();
                    juridica = new Juridica();
                    enderecoFisica = dbe.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 3);
                    //FacesContext.getCurrentInstance().getExternalContext().redirect("/Sindical/agendamento.jsf");
                    // COM FISICA, COM PESSOA EMPRESA COM OPOSICAO
                } else if (!listFisica.isEmpty() && pem != null && !listao.isEmpty()) {
                    GenericaMensagem.warn("Atenção", "CPF cadastrado em oposição data: " + listao.get(0).getEmissao());

                    styleDestaque = "color: red; font-size: 14pt; font-weight:bold";
                    pessoaEmpresa = pem;
                    fisica = pessoaEmpresa.getFisica();
                    profissao = (pessoaEmpresa.getFuncao() == null) ? new Profissao() : pessoaEmpresa.getFuncao();
                    enderecoFisica = dbe.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 3);
                    protocolo = 0;
                    // FUNCIONARIO JA FOI AGENDADO
    //                if (db.pesquisaFisicaAgendada(fisica.getId()) != null){
    //
    //                }
                    // EMPRESA É A MESMA DA OPOSICAO
                    if (pessoaEmpresa.getJuridica().getId() == listao.get(0).getJuridica().getId()) {
                        juridica = pessoaEmpresa.getJuridica();
                    } else {
                        juridica = listao.get(0).getJuridica();
                        // FUNCIONARIO SEM DATA DE DEMISSAO
    //                    if (pessoaEmpresa.getDemissao().isEmpty()){
    //                        if ((DataHoje.converteDataParaInteger(pessoaEmpresa.getDemissao()) < DataHoje.converteDataParaInteger(listao.get(0).getEmissao()) )){
    //
    //                        }
    //                    }
                    }
                    GenericaSessao.put("juridicaPesquisa", juridica);
                    PF.openDialog("dlg_oposicao");
                    // COM FISICA, SEM PESSOA EMPRESA COM OPOSICAO
                } else if (!listFisica.isEmpty() && pem == null && !listao.isEmpty()) {
                    GenericaMensagem.warn("Atenção", "CPF cadastrado em oposição data: " + listao.get(0).getEmissao());
                    styleDestaque = "color: red; font-size: 14pt; font-weight:bold";

                    fisica = listFisica.get(0);
                    juridica = listao.get(0).getJuridica();
                    enderecoFisica = dbe.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 3);
                    PF.openDialog("dlg_oposicao");
                }
            } else {
                GenericaMensagem.warn("Atenção", "Informar CPF!");
            }
        }
    }

    public String pesquisarProtocolo() {
        HomologacaoDB db = new HomologacaoDBToplink();
        if (protocolo > 0) {
            Agendamento age = new Agendamento();
            age.setData(agendamento.getData());
            age.setHorarios(agendamento.getHorarios());
            Agendamento age2 = db.pesquisaProtocolo(protocolo);
            if (age2 != null) {
                agendamento = age2;
                agendamento.setData(age.getData());
                agendamento.setHorarios(age.getHorarios());
                agendamento.setAgendador((Usuario) GenericaSessao.getObject("sessaoUsuario"));
                agendamento.setFilial(macFilial.getFilial());
                fisica = agendamento.getPessoaEmpresa().getFisica();
                PessoaEnderecoDB dbe = new PessoaEnderecoDBToplink();
                enderecoFisica = dbe.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 3);
                pessoaEmpresa = agendamento.getPessoaEmpresa();
                profissao = pessoaEmpresa.getFuncao();
                GenericaSessao.put("juridicaPesquisa", pessoaEmpresa.getJuridica());
            } else {
                if (agendamento.getId() != -1) {
                    limpar();
                }
            }
        } else {
            if (agendamento.getId() != -1) {
                limpar();
            }
        }
        return "agendamento";
    }

    public String enviarEmailProtocolo() {
        ProtocoloAgendamento protocoloAgendamento = new ProtocoloAgendamento();
        protocoloAgendamento.enviar(agendamento);
        return null;
    }

    public void pesquisaEndereco() {
        EnderecoDB db = new EnderecoDBToplink();
        listaEnderecos.clear();
        if (!cepEndereco.isEmpty()) {
            listaEnderecos = db.pesquisaEnderecoCep(cepEndereco);
        }
    }

    public void limparPesquisaEndereco() {
        listaEnderecos.clear();
    }

    public void editarEndereco(Endereco e) {
        enderecoFisica.setEndereco(e);
    }

    public int getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

    public void refreshForm() {

    }

    public Juridica getJuridica() {
        if (GenericaSessao.exists("juridicaPesquisa")) {
            juridica = (Juridica) GenericaSessao.getObject("juridicaPesquisa", true);
            listaMovimento.clear();
            GenericaSessao.put("pessoaPesquisa", juridica.getPessoa());
            if (juridica.getContabilidade() != null && agendamento.getId() == -1) {
                agendamento.setTelefone(juridica.getContabilidade().getPessoa().getTelefone1());
            }

            if (fisica.getId() != -1) {
                PessoaEmpresaDB db = new PessoaEmpresaDBToplink();
                List<PessoaEmpresa> list_pe = db.listaPessoaEmpresaPorFisicaEmpresaDemissao(fisica.getId(), juridica.getId());

                if (!list_pe.isEmpty()) {
                    pessoaEmpresa = list_pe.get(0);

                    if (pessoaEmpresa.getFuncao() != null) {
                        profissao = pessoaEmpresa.getFuncao();
                    }
                } else {
                    if (validaAdmissao() && validaDemissao()) {
//                        pessoaEmpresa = new PessoaEmpresa();
                        //                      profissao = new Profissao();
                    }
                }
            }
        }
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }

    public PessoaEndereco getEnderecoEmpresa() {
        return enderecoEmpresa;
    }

    public void setEnderecoEmpresa(PessoaEndereco enderecoEmpresa) {
        this.enderecoEmpresa = enderecoEmpresa;
    }

    public String getStrEndereco() {
        PessoaEnderecoDB pessoaEnderecoDB = new PessoaEnderecoDBToplink();
        if (juridica.getId() != -1) {
            enderecoEmpresa = pessoaEnderecoDB.pesquisaEndPorPessoaTipo(juridica.getPessoa().getId(), 5);
            if (enderecoEmpresa.getId() != -1) {
                String strCompl;
                if (enderecoEmpresa.getComplemento().equals("")) {
                    strCompl = " ";
                } else {
                    strCompl = " ( " + enderecoEmpresa.getComplemento() + " ) ";
                }

                strEndereco = enderecoEmpresa.getEndereco().getLogradouro().getDescricao() + " "
                        + enderecoEmpresa.getEndereco().getDescricaoEndereco().getDescricao() + ", " + enderecoEmpresa.getNumero() + " " + enderecoEmpresa.getEndereco().getBairro().getDescricao() + ","
                        + strCompl + enderecoEmpresa.getEndereco().getCidade().getCidade() + " - " + enderecoEmpresa.getEndereco().getCidade().getUf() + " - " + AnaliseString.mascaraCep(enderecoEmpresa.getEndereco().getCep());
            } else {
                strEndereco = "";
            }
        } else {
            enderecoEmpresa = new PessoaEndereco();
            strEndereco = "";
        }
        return strEndereco;
    }

    public void setStrEndereco(String strEndereco) {
        this.strEndereco = strEndereco;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }

    public Fisica getFisica() {
        return fisica;
    }

    public void setFisica(Fisica fisica) {
        this.fisica = fisica;
    }

    public int getIdMotivoDemissao() {
        return idMotivoDemissao;
    }

    public void setIdMotivoDemissao(int idMotivoDemissao) {
        this.idMotivoDemissao = idMotivoDemissao;
    }
//
//    public String getTipoAviso() {
//        return tipoAviso;
//    }
//
//    public void setTipoAviso(String tipoAviso) {
//        this.tipoAviso = tipoAviso;
//    }

    public PessoaEmpresa getPessoaEmpresa() {
        return pessoaEmpresa;
    }

    public void setPessoaEmpresa(PessoaEmpresa pessoaEmpresa) {
        this.pessoaEmpresa = pessoaEmpresa;
    }

    public boolean isRenderCancelarHorario() {
        return renderCancelarHorario;
    }

    public void setRenderCancelarHorario(boolean renderCancelarHorario) {
        this.renderCancelarHorario = renderCancelarHorario;
    }

    public boolean isRenderCancelar() {
        return renderCancelar;
    }

    public void setRenderCancelar(boolean renderCancelar) {
        this.renderCancelar = renderCancelar;
    }

    public String getStatusEmpresa() {
        HomologacaoDB db = new HomologacaoDBToplink();
        if (juridica.getId() != -1 && listaMovimento.isEmpty()) {
            listaMovimento = db.pesquisaPessoaDebito(juridica.getPessoa().getId(), DataHoje.data());
        }
        if (!listaMovimento.isEmpty()) {
            statusEmpresa = "EM DÉBITO";
        } else {
            statusEmpresa = "REGULAR";
        }
        return statusEmpresa;
    }

    public void setStatusEmpresa(String statusEmpresa) {
        this.statusEmpresa = statusEmpresa;
    }

    public int getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(int protocolo) {
        this.protocolo = protocolo;
    }

    public String getCepEndereco() {
        return cepEndereco;
    }

    public void setCepEndereco(String cepEndereco) {
        this.cepEndereco = cepEndereco;
    }

    public List<Endereco> getListaEnderecos() {
        if (listaEnderecos.isEmpty()) {
            if (!cepEndereco.equals("")) {
                EnderecoDB db = new EnderecoDBToplink();
                listaEnderecos = db.pesquisaEnderecoCep(cepEndereco);
            }
        }
        return listaEnderecos;
    }

    public void setListaEnderecos(List listaEnderecos) {
        this.listaEnderecos = listaEnderecos;
    }

    public PessoaEndereco getEnderecoFisica() {
        if (enderecoFisica == null) {
            enderecoFisica = new PessoaEndereco();
        }
        return enderecoFisica;
    }

    public void setEnderecoFisica(PessoaEndereco enderecoFisica) {
        this.enderecoFisica = enderecoFisica;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public int getIdIndexEndereco() {
        return idIndexEndereco;
    }

    public void setIdIndexEndereco(int idIndexEndereco) {
        this.idIndexEndereco = idIndexEndereco;
    }

    public String extratoTela() {
        GenericaSessao.put("pessoaPesquisa", juridica.getPessoa());
        return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).extratoTela();
    }

    public String getStrContribuinte() {
        if (juridica.getId() != -1) {
            JuridicaDB db = new JuridicaDBToplink();
            List<ArrayList> listax = db.listaJuridicaContribuinte(juridica.getId());
            if (!listax.isEmpty()) {
                if (((List) (listax.get(0))).get(11) != null) {
                    return strContribuinte = "Empresa Inativa";
                } else {
                    return strContribuinte = "";
                }
            }
        }
        return strContribuinte = "Empresa não contribuinte, não poderá efetuar um agendamento!";
    }

    public void setStrContribuinte(String strContribuinte) {
        this.strContribuinte = strContribuinte;
    }

    public int getIdHorarioTransferencia() {
        return idHorarioTransferencia;
    }

    public void setIdHorarioTransferencia(int idHorarioTransferencia) {
        this.idHorarioTransferencia = idHorarioTransferencia;
    }

    public Date getDataTransferencia() {
        return dataTransferencia;
    }

    public void setDataTransferencia(Date dataTransferencia) {
        this.dataTransferencia = dataTransferencia;
    }

    public String getEnviarPara() {
        return enviarPara;
    }

    public void setEnviarPara(String enviarPara) {
        this.enviarPara = enviarPara;
    }

    public PessoaEndereco getEnderecoFilial() {
        PessoaEnderecoDB pessoaEnderecoDB = new PessoaEnderecoDBToplink();
        if (enderecoFilial.getId() == -1) {
            enderecoFilial = pessoaEnderecoDB.pesquisaEndPorPessoaTipo(macFilial.getFilial().getFilial().getPessoa().getId(), 2);
        }
        return enderecoFilial;
    }

    public void setEnderecoFilial(PessoaEndereco enderecoFilial) {
        this.enderecoFilial = enderecoFilial;
    }

    public boolean isImprimirPro() {
        return imprimirPro;
    }

    public void setImprimirPro(boolean imprimirPro) {
        this.imprimirPro = imprimirPro;
    }

    public boolean isRenderedDisponivel() {
        return renderedDisponivel;
    }

    public void setRenderedDisponivel(boolean renderedDisponivel) {
        this.renderedDisponivel = renderedDisponivel;
    }

    public void mensagemAgendamento() {
        FeriadosDao feriadosDao = new FeriadosDao();
        if (macFilial.getFilial().getFilial().getId() != -1) {
            List<Feriados> feriados = feriadosDao.pesquisarPorDataFilialEData(DataHoje.converteData(data), macFilial.getFilial());
            if (!feriados.isEmpty()) {
                String msg = "";
                for (int i = 0; i < feriados.size(); i++) {
                    if (i == 0) {
                        msg += feriados.get(i).getNome();
                    } else {
                        msg += ", " + feriados.get(i).getNome();
                    }
                }
                if (!msg.isEmpty()) {
                    GenericaMensagem.warn("Sistema", "Nesta data existem feriados/agenda: " + msg);
                }
            }
        }
    }

    public int getIdHorarioAlternativo() {
        return idHorarioAlternativo;
    }

    public void setIdHorarioAlternativo(int idHorarioAlternativo) {
        this.idHorarioAlternativo = idHorarioAlternativo;
    }

    public void adicionarHorarioAlternativo() {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        agendamento.setHorarios((Horarios) salvarAcumuladoDB.find("Horarios", Integer.parseInt(listaHorarioTransferencia.get(idHorarioAlternativo).getDescription())));
        setOcultarHorarioAlternativo(true);
        loadListaHorariosTransferencia();
    }

    public boolean isOcultarHorarioAlternativo() {
        return ocultarHorarioAlternativo;
    }

    public void setOcultarHorarioAlternativo(boolean ocultarHorarioAlternativo) {
        this.ocultarHorarioAlternativo = ocultarHorarioAlternativo;
    }

    // Verifica os agendamentos que não foram atendidos no menu principal;
    public void verificaNaoAtendidos() {
        HomologacaoDB homologacaoDB = new HomologacaoDBToplink();
        homologacaoDB.verificaNaoAtendidosSegRegistroAgendamento();
    }

    public String getEstiloTabela() {
        return "";
    }

    /**
     * -- STATUS 1 - DISPONIVEL; 2 - AGENDADO; 3 - CANCELADO; 4 - HOMOLOGADO; 5
     * - ATENDIMENTO; 6 - ENCAIXE; 7 - NÃO COMPARACEU
     *
     * @return
     */
    public List<ListaAgendamento> getListaHorarios() {
        return listaHorarios;
    }

    public void setListaHorarios(List<ListaAgendamento> listaHorarios) {
        this.listaHorarios = listaHorarios;
    }

    public String getEmailEmpresa() {
        return emailEmpresa;
    }

    public void setEmailEmpresa(String emailEmpresa) {
        this.emailEmpresa = emailEmpresa;
    }

    public String getStyleDestaque() {
        return styleDestaque;
    }

    public void setStyleDestaque(String styleDestaque) {
        this.styleDestaque = styleDestaque;
    }

    public ConfiguracaoHomologacao getConfiguracaoHomologacao() {
        if (configuracaoHomologacao.getId() == -1) {
            Dao dao = new Dao();
            configuracaoHomologacao = (ConfiguracaoHomologacao) dao.find(new ConfiguracaoHomologacao(), 1);
        }
        return configuracaoHomologacao;
    }

    public void setConfiguracaoHomologacao(ConfiguracaoHomologacao configuracaoHomologacao) {
        this.configuracaoHomologacao = configuracaoHomologacao;
    }

    public int getCounter() {
        return counter;
    }

    public void increment() {
        counter--;
        if (counter == -1) {
            counter = configuracaoHomologacao.getTempoRefreshAgendamento();
        }
    }

    public boolean isVisibleModal() {
        return visibleModal;
    }

    public void setVisibleModal(boolean visibleModal) {
        this.visibleModal = visibleModal;
    }

    public String getTipoTelefone() {
        return tipoTelefone;
    }

    public void setTipoTelefone(String tipoTelefone) {
        this.tipoTelefone = tipoTelefone;
    }

    public Cancelamento getCancelamento() {
        return cancelamento;
    }

    public void setCancelamento(Cancelamento cancelamento) {
        this.cancelamento = cancelamento;
    }

}
