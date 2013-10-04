package br.com.rtools.homologacao.beans;

import br.com.rtools.arrecadacao.Oposicao;
import br.com.rtools.atendimento.db.AtendimentoDB;
import br.com.rtools.atendimento.db.AtendimentoDBTopLink;
import br.com.rtools.endereco.Endereco;
import br.com.rtools.endereco.db.EnderecoDB;
import br.com.rtools.endereco.db.EnderecoDBToplink;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.homologacao.Agendamento;
import br.com.rtools.homologacao.CancelarHorario;
import br.com.rtools.homologacao.Demissao;
import br.com.rtools.homologacao.Feriados;
import br.com.rtools.homologacao.Horarios;
import br.com.rtools.homologacao.Status;
import br.com.rtools.homologacao.db.*;
import br.com.rtools.impressao.ParametroProtocolo;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.movimento.ImprimirBoleto;
import br.com.rtools.pessoa.*;
import br.com.rtools.pessoa.db.*;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.chamadaPaginaJSFBean;
import br.com.rtools.seguranca.controleUsuario.controleUsuarioJSFBean;
import br.com.rtools.sistema.Links;
import br.com.rtools.sistema.db.LinksDB;
import br.com.rtools.sistema.db.LinksDBToplink;
import br.com.rtools.utilitarios.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
// import java.util.Vector;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class AgendamentoJSFBean extends PesquisarProfissaoJSFBean {

    private int idStatus = 0;
    private int idMotivoDemissao = 0;
    private int idHorarioTransferencia = 0;
    private String tipoAviso = "true";
    private String strEndereco = "";
    private String msgAgendamento = "";
    private String msgConfirma = "";
    private String strSalvar = "Agendar";
    private String statusEmpresa = "REGULAR";
    private String cepEndereco = "";
    private String strContribuinte = "";
    private List listaGrid = new ArrayList();
    private List listaMovimento = new ArrayList();
    private Juridica juridica = new Juridica();
    private Fisica fisica = new Fisica();
    private Date data = DataHoje.dataHoje();
    private Date dataTransferencia = DataHoje.dataHoje();
    private Agendamento agendamento = new Agendamento();
    private PessoaEndereco enderecoEmpresa = new PessoaEndereco();
    private PessoaEmpresa pessoaEmpresa = new PessoaEmpresa();
    private Horarios horarios = new Horarios();
    private boolean renderAgendamento = true;
    private boolean renderConcluir = false;
    private boolean renderCancelarHorario = false;
    private boolean renderCancelar = true;
    private boolean disabledEmpresa = false;
    private boolean disabledProtocolo = false;
    private boolean renderedDisponivel = false;
    private boolean renderTransfere = false;
    private MacFilial macFilial = null;
    private int protocolo = 0;
    private int idIndex = -1;
    private int idIndexEndereco = -1;
    private int id_protocolo = -1;
    private List listaEnderecos = new ArrayList();
    private PessoaEndereco enderecoFisica = new PessoaEndereco();
    private String enviarPara = "contabilidade";
    private PessoaEndereco enderecoFilial = new PessoaEndereco();
    private boolean imprimirPro = false;

    public String salvarTransferencia() {
        if (getDataTransferencia().getDay() == 6 || getDataTransferencia().getDay() == 0) {
            msgConfirma = "Fins de semana não permitido!";
            return null;
        }
        if (DataHoje.converteDataParaInteger(DataHoje.converteData(getDataTransferencia()))
                < DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()))) {
            msgConfirma = "Data anterior ao dia de hoje!";
            return null;
        }
        if (DataHoje.converteDataParaInteger(((new DataHoje()).incrementarMeses(3, DataHoje.data())))
                < DataHoje.converteDataParaInteger(DataHoje.converteData(getDataTransferencia()))) {
            msgConfirma = "Data maior que 3 meses!";
            return null;
        }

        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        agendamento.setDtData(dataTransferencia);
        if (getListaHorarioTransferencia().isEmpty()) {
            msgConfirma = "Não existem horários para esse dia da semana!";
            return null;
        }
        Horarios h = (Horarios) sv.pesquisaCodigo(Integer.valueOf(getListaHorarioTransferencia().get(idHorarioTransferencia).getDescription()), "Horarios");
        agendamento.setHorarios(h);

        sv.abrirTransacao();
        if (!sv.alterarObjeto(agendamento)) {
            sv.desfazerTransacao();
            msgConfirma = "Erro ao transferir horário";
            return null;
        }
        sv.comitarTransacao();
        renderTransfere = false;
        idHorarioTransferencia = 0;
        msgConfirma = "Horário transferido com Sucesso!";
        return null;
    }

    public void trasfere() {
        if (renderTransfere) {
            renderTransfere = false;
        } else {
            renderTransfere = true;
        }
    }

    public String imprimirPlanilha() {
        if (listaMovimento.isEmpty()) {
            return null;
        }
        ImprimirBoleto imp = new ImprimirBoleto();
        List<Movimento> lista = new ArrayList();
        List<Float> listaValores = new ArrayList<Float>();
        for (int i = 0; i < listaMovimento.size(); i++) {
            //lista.add((Movimento) new SalvarAcumuladoDBToplink().pesquisaCodigo( (Integer)((Vector)listaMovimento.get(i)).get(0), "Movimento") );
            Movimento m = (Movimento) new SalvarAcumuladoDBToplink().pesquisaCodigo((Integer) ((List) listaMovimento.get(i)).get(0), "Movimento");
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
        for (int i = 0; i < listaMovimento.size(); i++) {
            // lista.add((Movimento) new SalvarAcumuladoDBToplink().pesquisaCodigo( (Integer)((Vector)listaMovimento.get(i)).get(0), "Movimento") );
            Movimento m = (Movimento) new SalvarAcumuladoDBToplink().pesquisaCodigo((Integer) ((List) listaMovimento.get(i)).get(0), "Movimento");
            lista.add(m);
            listaValores.add(m.getValor());
        }

        if (!lista.isEmpty()) {
            imp.imprimirPlanilha(lista, listaValores, false, false);
        }

        try {
            Registro reg = (Registro) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(1, "Registro");

            Pessoa pessoa;
            if (enviarPara.equals("contabilidade")) {
                if (juridica.getContabilidade() == null) {
                    msgConfirma = "Empresa sem contabilidade";
                    return null;
                }

                if (juridica.getContabilidade().getPessoa().getEmail1().isEmpty()) {
                    msgConfirma = "Contabilidade sem Email para envio";
                    return null;
                }

                pessoa = juridica.getContabilidade().getPessoa();
            } else {
                if (juridica.getPessoa().getEmail1().isEmpty()) {
                    msgConfirma = "Empresa sem Email para envio";
                    return null;
                }

                pessoa = juridica.getPessoa();
            }

            String nome = imp.criarLink(pessoa, reg.getUrlPath() + "/Sindical/Cliente/" + controleUsuarioJSFBean.getCliente() + "/Arquivos/downloads/boletos");
            List<Pessoa> p = new ArrayList();

            p.add(pessoa);

            //String[] ret = new String[2];
            String[] ret;
            if (!reg.isEnviarEmailAnexo()) {
                ret = EnviarEmail.EnviarEmailPersonalizado(reg,
                        p,
                        " <div style='background:#00ccff; padding: 15px; font-size:13pt'>Envio cadastrado para <b>" + pessoa.getNome() + " </b></div><br />"
                        + " <h5>Visualize sua planilha de débitos clicando no link abaixo</h5><br /><br />"
                        + " <a href='" + reg.getUrlPath() + "/Sindical/acessoLinks.jsf?cliente=" + controleUsuarioJSFBean.getCliente() + "&amp;arquivo=" + nome + "'>Clique aqui para abrir Planilha de Débitos</a><br />",
                        new ArrayList(),
                        "Envio de Débitos");
            } else {
                List<File> fls = new ArrayList<File>();
                fls.add(new File(imp.getPathPasta() + "/" + nome));

                ret = EnviarEmail.EnviarEmailPersonalizado(reg,
                        p,
                        " <div style='background:#00ccff; padding: 15px; font-size:13pt'>Envio cadastrado para <b>" + pessoa.getNome() + " </b></div><br />"
                        + " <h5>Baixe sua planilha de débitos anexado neste email</5><br /><br />",
                        fls,
                        "Envio de Débitos");
            }
            if (!ret[1].isEmpty()) {
                msgConfirma = ret[1];
            } else {
                msgConfirma = ret[0];
            }
            //msgConfirma = "Envio Concluído!";
        } catch (Exception erro) {
            System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());

        }
        return null;
    }

    public String atualizar() {
        return "agendamento";
    }

    public List<SelectItem> getListaStatus() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        int i = 0;
        StatusDB db = new StatusDBToplink();
        List select = db.pesquisaTodos();
        if (!select.isEmpty()) {
            while (i < select.size()) {
                result.add(new SelectItem(new Integer(i),
                        (String) ((Status) select.get(i)).getDescricao(),
                        Integer.toString(((Status) select.get(i)).getId())));
                i++;
            }
        }
        return result;
    }

    public List<SelectItem> getListaMotivoDemissao() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        int i = 0;
        DemissaoDB db = new DemissaoDBToplink();
        List select = db.pesquisaTodos();
        if (!select.isEmpty()) {
            while (i < select.size()) {
                result.add(new SelectItem(new Integer(i),
                        (String) ((Demissao) select.get(i)).getDescricao(),
                        Integer.toString(((Demissao) select.get(i)).getId())));
                i++;
            }
        }
        return result;
    }

    public List<SelectItem> getListaHorarioTransferencia() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        AgendamentoDB db = new AgendamentoDBToplink();
        int idDiaSemana = DataHoje.diaDaSemana(dataTransferencia);
        List<Horarios> select = db.pesquisaTodosHorariosDisponiveis(macFilial.getFilial().getId(), idDiaSemana);
        int qnt;
        int j = 0;
        for (int i = 0; i < select.size(); i++) {
            qnt = db.pesquisaQntdDisponivel(macFilial.getFilial().getId(), select.get(i), getDataTransferencia());
            if (qnt == -1) {
                return new ArrayList();
            }

            if (qnt > 0) {
                result.add(new SelectItem(new Integer(j), select.get(i).getHora() + " (" + qnt + ")", String.valueOf(select.get(i).getId())));
                j++;
            }
//            if (qnt < select.get(i).getQuantidade()) {
//                result.add(new SelectItem(new Integer(j), select.get(i).getHora(), String.valueOf(select.get(i).getId())));
//                j++;
//            }
        }
        return result;
    }

    public synchronized List getListaHorarios() {
        listaGrid = new ArrayList();
        List<Agendamento> ag;
        List<Horarios> horario;
        List<CancelarHorario> cancelarHorario = new ArrayList<CancelarHorario>();

        AgendamentoDB db = new AgendamentoDBToplink();
        String agendador;
        String homologador;
        DataObject dtObj;

        if (macFilial == null) {
            return new ArrayList();
        }
        int idNrStatus = Integer.parseInt(((SelectItem) getListaStatus().get(idStatus)).getDescription());
        switch (idNrStatus) {
            //STATUS DISPONIVEL ----------------------------------------------------------------------------------------------
            case 1: {
                CancelarHorarioDB cancelarHorarioDB = new CancelarHorarioDBToplink();
                cancelarHorario = cancelarHorarioDB.listaTodosHorariosCanceladosPorFilial(macFilial.getFilial().getId(), data, null);
                renderedDisponivel = true;
                int diaDaSemana = DataHoje.diaDaSemana(data);
                horario = db.pesquisaTodosHorariosDisponiveis(macFilial.getFilial().getId(), diaDaSemana);
                // horario = db.pesquisaTodosHorariosDisponiveis(macFilial.getFilial().getId());
                strSalvar = "Agendar";
                disabledProtocolo = false;
                int qnt;
                for (int i = 0; i < horario.size(); i++) {
                    qnt = db.pesquisaQntdDisponivel(macFilial.getFilial().getId(), horario.get(i), getData());
                    //qnt = db.pesquisaQuantidadeAgendado(macFilial.getFilial().getId(), horario.get(i), getData());
                    if (qnt == -1) {
                        msgAgendamento = "Erro ao pesquisar horários disponíveis!";
                        listaGrid.clear();
                        break;
                    }
//                    int qntTotal = horario.get(i).getQuantidade() - qnt;
//                    for(int y = 0; y < cancelarHorario.size(); y++) {
//                        if(horario.get(i).getId() == cancelarHorario.get(y).getHorarios().getId()) {
//                            if(cancelarHorario.get(y).getQuantidade() <= qntTotal){
//                                qntTotal = qntTotal - cancelarHorario.get(y).getQuantidade();
//                            }
//                            // break;
//                        }
//                    }
//                    if(qntTotal > 0){
                    if (qnt > 0) {
//                        if (qnt < horario.get(i).getQuantidade()) {
                        dtObj = new DataObject(horario.get(i), // ARG 0 HORA
                                null, // ARG 1 CNPJ
                                null, //ARG 2 NOME
                                null, //ARG 3 HOMOLOGADOR
                                null, // ARG 4 CONTATO
                                null, // ARG 5 TELEFONE
                                null, // ARG 6 USUARIO
                                null,
                                qnt, // ARG 8 QUANTIDADE DISPONÍVEL
                                null);
                        listaGrid.add(dtObj);
//                        }
                    }
//                    }
                }
                break;
            }
            // ---------------------------------------------------------------------------------------------------------------
            // ---------------------------------------------------------------------------------------------------------------

            // STATUS AGENDADO -----------------------------------------------------------------------------------------------
            case 2:
            case 7: {
                renderedDisponivel = false;
                if (idNrStatus == 2) {
                    ag = db.pesquisaAgendamento(2, macFilial.getFilial().getId(), getData(), null, 0, 0, 0);
                } else {
                    ag = db.pesquisaAgendamento(7, macFilial.getFilial().getId(), getData(), null, 0, 0, 0);

                }
                strSalvar = "Atualizar";
                disabledProtocolo = true;
                for (int i = 0; i < ag.size(); i++) {
                    if (ag.get(i).getAgendador() != null) {
                        agendador = ag.get(i).getAgendador().getPessoa().getNome();
                    } else {
                        agendador = "** Web User **";
                    }
                    if (ag.get(i).getHomologador() != null) {
                        homologador = ag.get(i).getHomologador().getPessoa().getNome();
                    } else {
                        homologador = "";
                    }

                    dtObj = new DataObject(ag.get(i).getHorarios(), // ARG 0 HORA
                            ag.get(i).getPessoaEmpresa().getJuridica().getPessoa().getDocumento(), // ARG 1 CNPJ
                            ag.get(i).getPessoaEmpresa().getJuridica().getPessoa().getNome(), //ARG 2 NOME
                            homologador, //ARG 3 HOMOLOGADOR
                            ag.get(i).getContato(), // ARG 4 CONTATO
                            ag.get(i).getTelefone(), // ARG 5 TELEFONE
                            agendador, // ARG 6 USUARIO
                            ag.get(i).getPessoaEmpresa(), // ARG 7 PESSOA EMPRESA
                            null, // ARG 8
                            ag.get(i));// ARG 9 AGENDAMENTO
                    listaGrid.add(dtObj);
                }
                break;
            }
            // ---------------------------------------------------------------------------------------------------------------
            // ---------------------------------------------------------------------------------------------------------------

            // STATUS CANCELADO ----------------------------------------------------------------------------------------------
            case 3: {
                renderedDisponivel = false;
                ag = db.pesquisaAgendamento(3, macFilial.getFilial().getId(), getData(), null, 0, 0, 0);
                strSalvar = "Atualizar";
                disabledProtocolo = true;
                for (int i = 0; i < ag.size(); i++) {
                    if (ag.get(i).getAgendador() != null) {
                        agendador = ag.get(i).getAgendador().getPessoa().getNome();
                    } else {
                        agendador = "** Web User **";
                    }
                    if (ag.get(i).getHomologador() != null) {
                        homologador = ag.get(i).getHomologador().getPessoa().getNome();
                    } else {
                        homologador = "";
                    }

                    dtObj = new DataObject(ag.get(i).getHorarios(), // ARG 0 HORA
                            ag.get(i).getPessoaEmpresa().getJuridica().getPessoa().getDocumento(), // ARG 1 CNPJ
                            ag.get(i).getPessoaEmpresa().getJuridica().getPessoa().getNome(), //ARG 2 NOME
                            homologador, //ARG 3 HOMOLOGADOR
                            ag.get(i).getContato(), // ARG 4 CONTATO
                            ag.get(i).getTelefone(), // ARG 5 TELEFONE
                            agendador, // ARG 6 USUARIO
                            ag.get(i).getPessoaEmpresa(), // ARG 7 PESSOA EMPRESA
                            null, // ARG 8
                            ag.get(i));// ARG 9 AGENDAMENTO
                    listaGrid.add(dtObj);
                }
                break;
            }

            // STATUS HOMOLOGADO ----------------------------------------------------------------------------------------------
            case 4: {
                renderedDisponivel = false;
                ag = db.pesquisaAgendamento(4, macFilial.getFilial().getId(), getData(), null, 0, 0, 0);
                strSalvar = "Atualizar";
                disabledProtocolo = true;
                for (int i = 0; i < ag.size(); i++) {
                    if (ag.get(i).getAgendador() != null) {
                        agendador = ag.get(i).getAgendador().getPessoa().getNome();
                    } else {
                        agendador = "** Web User **";
                    }
                    if (ag.get(i).getHomologador() != null) {
                        homologador = ag.get(i).getHomologador().getPessoa().getNome();
                    } else {
                        homologador = "";
                    }

                    dtObj = new DataObject(ag.get(i).getHorarios(), // ARG 0 HORA
                            ag.get(i).getPessoaEmpresa().getJuridica().getPessoa().getDocumento(), // ARG 1 CNPJ
                            ag.get(i).getPessoaEmpresa().getJuridica().getPessoa().getNome(), //ARG 2 NOME
                            homologador, //ARG 3 HOMOLOGADOR
                            ag.get(i).getContato(), // ARG 4 CONTATO
                            ag.get(i).getTelefone(), // ARG 5 TELEFONE
                            agendador, // ARG 6 USUARIO
                            ag.get(i).getPessoaEmpresa(), // ARG 7 PESSOA EMPRESA
                            null, // ARG 8
                            ag.get(i));// ARG 9 AGENDAMENTO
                    listaGrid.add(dtObj);
                }
                break;
            }

            // STATUS ATENDIMENTO ---------------------------------------------------------------------------------------------
            case 5: {
                renderedDisponivel = false;
                ag = db.pesquisaAgendamento(5, macFilial.getFilial().getId(), getData(), null, 0, 0, 0);
                strSalvar = "Atualizar";
                disabledProtocolo = true;
                for (int i = 0; i < ag.size(); i++) {
                    if (ag.get(i).getAgendador() != null) {
                        agendador = ag.get(i).getAgendador().getPessoa().getNome();
                    } else {
                        agendador = "** Web User **";
                    }
                    if (ag.get(i).getHomologador() != null) {
                        homologador = ag.get(i).getHomologador().getPessoa().getNome();
                    } else {
                        homologador = "";
                    }

                    dtObj = new DataObject(ag.get(i).getHorarios(), // ARG 0 HORA
                            ag.get(i).getPessoaEmpresa().getJuridica().getPessoa().getDocumento(), // ARG 1 CNPJ
                            ag.get(i).getPessoaEmpresa().getJuridica().getPessoa().getNome(), //ARG 2 NOME
                            homologador, //ARG 3 HOMOLOGADOR
                            ag.get(i).getContato(), // ARG 4 CONTATO
                            ag.get(i).getTelefone(), // ARG 5 TELEFONE
                            agendador, // ARG 6 USUARIO
                            ag.get(i).getPessoaEmpresa(), // ARG 7 PESSOA EMPRESA
                            null, // ARG 8
                            ag.get(i));// ARG 9 AGENDAMENTO
                    listaGrid.add(dtObj);
                }
                break;
            }
            //STATUS ENCAIXE ----------------------------------------------------------------------------------------------
            case 6: {
                renderedDisponivel = false;
                // horario = db.pesquisaTodosHorariosDisponiveis(macFilial.getFilial().getId());
                int diaDaSemana = DataHoje.diaDaSemana(data);
                horario = db.pesquisaTodosHorariosDisponiveis(macFilial.getFilial().getId(), diaDaSemana);
                strSalvar = "Agendar";
                disabledProtocolo = false;
                for (int i = 0; i < horario.size(); i++) {
                    dtObj = new DataObject(horario.get(i), // ARG 0 HORA
                            null, // ARG 1 CNPJ
                            null, //ARG 2 NOME
                            null, //ARG 3 HOMOLOGADOR
                            null, // ARG 4 CONTATO
                            null, // ARG 5 TELEFONE
                            null, // ARG 6 USUARIO
                            null,
                            null,
                            null);
                    listaGrid.add(dtObj);
                }
                break;
            }
            // ---------------------------------------------------------------------------------------------------------------
            // ---------------------------------------------------------------------------------------------------------------
        }
        return listaGrid;
    }

    public boolean pesquisarFeriado() {
        FeriadosDB db = new FeriadosDBToplink();
        List listFeriados = db.pesquisarPorData(DataHoje.converteData(getData()));
        if (!listFeriados.isEmpty()) {
            return true;
        }

        listFeriados = db.pesquisarPorDataFilial(DataHoje.converteData(getData()), macFilial.getFilial());
        if (!listFeriados.isEmpty()) {
            return true;
        }

        return false;

    }

    public String agendar() {
        if (getData().getDay() == 6 || getData().getDay() == 0) {
            msgAgendamento = "Fins de semana não permitido!";
            return "agendamento";
        }
        if (DataHoje.converteDataParaInteger(DataHoje.converteData(getData()))
                < DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()))) {
            msgAgendamento = "Data anterior ao dia de hoje!";
            return "agendamento";
        }
        if (DataHoje.converteDataParaInteger(((new DataHoje()).incrementarMeses(3, DataHoje.data())))
                < DataHoje.converteDataParaInteger(DataHoje.converteData(getData()))) {
            msgAgendamento = "Data maior que 3 meses!";
            return "agendamento";
        }

        renderAgendamento = false;
        renderConcluir = true;
        protocolo = 0;
        if (profissao.getId() == -1) {
            ProfissaoDB db = new ProfissaoDBToplink();
            profissao = db.pesquisaCodigo(0);
            profissao.setProfissao("");
        }

        switch (Integer.parseInt(((SelectItem) getListaStatus().get(idStatus)).getDescription())) {
            case 1: {
                if (getData() == null) {
                    msgAgendamento = "Selecione uma data para Agendamento!";
                    renderAgendamento = true;
                    renderConcluir = false;
                } else {
                    renderCancelarHorario = false;
                    renderCancelar = true;
                    if (pesquisarFeriado()) {
                        msgAgendamento = "Esta data esta cadastrada como Feriado!";
                        //gregorianCalendar = new HtmlCalendar();
                        renderAgendamento = true;
                        renderConcluir = false;
                    } else {
                        agendamento.setData(DataHoje.converteData(getData()));
                        agendamento.setHorarios((Horarios) ((DataObject) listaGrid.get(idIndex)).getArgumento0());
                        msgAgendamento = "";
                    }
                }
                break;
            }
            case 2: {
                PessoaEnderecoDB db = new PessoaEnderecoDBToplink();
                agendamento = (Agendamento) ((DataObject) listaGrid.get(idIndex)).getArgumento9();
                fisica = ((PessoaEmpresa) ((DataObject) listaGrid.get(idIndex)).getArgumento7()).getFisica();
                enderecoFisica = db.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 1);
                juridica = ((PessoaEmpresa) ((DataObject) listaGrid.get(idIndex)).getArgumento7()).getJuridica();
                profissao = ((PessoaEmpresa) ((DataObject) listaGrid.get(idIndex)).getArgumento7()).getFuncao();
                pessoaEmpresa = agendamento.getPessoaEmpresa();
                renderCancelarHorario = true;
                renderCancelar = false;
                //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("juridicaPesquisa",juridica);
                for (int i = 0; i < getListaMotivoDemissao().size(); i++) {
                    if (Integer.parseInt(getListaMotivoDemissao().get(i).getDescription()) == agendamento.getDemissao().getId()) {
                        idMotivoDemissao = (Integer) getListaMotivoDemissao().get(i).getValue();
                        break;
                    }
                }
                tipoAviso = String.valueOf(pessoaEmpresa.isAvisoTrabalhado());
                break;
            }
            case 3: {
                renderAgendamento = true;
                renderConcluir = false;
                break;
            }
            case 4: {
                renderAgendamento = true;
                renderConcluir = false;
                break;
            }
            case 5: {
                renderAgendamento = true;
                renderConcluir = false;
                break;
            }
            case 6: {
                if (getData() == null) {
                    msgAgendamento = "Selecione uma data para Agendamento!";
                    renderAgendamento = true;
                    renderConcluir = false;
                } else {
                    renderCancelarHorario = false;
                    renderCancelar = true;
                    if (pesquisarFeriado()) {
                        msgAgendamento = "Esta data esta cadastrada como Feriado!";
                        renderAgendamento = true;
                        renderConcluir = false;
                    } else {
                        agendamento.setData(DataHoje.converteData(getData()));
                        agendamento.setHorarios((Horarios) ((DataObject) listaGrid.get(idIndex)).getArgumento0());
                        msgAgendamento = "";
                    }
                }
                break;
            }
        }
        return "agendamento";
    }

    public String salvar() {
        if (fisica.getPessoa().getNome().equals("") || fisica.getPessoa().getNome() == null) {
            msgConfirma = "Digite o nome do Funcionário!";
            return null;
        }

        if (!strContribuinte.isEmpty()) {
            msgConfirma = "Não é permitido agendar para uma empresa não contribuinte!";
            return null;
        }

        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        FisicaDB dbFis = new FisicaDBToplink();
        TipoDocumentoDB dbTipo = new TipoDocumentoDBToplink();
        DemissaoDB dbDem = new DemissaoDBToplink();
        // List listDocumento = new ArrayList();
        List listDocumento;
        StatusDB dbSta = new StatusDBToplink();
        TipoEnderecoDB dbt = new TipoEnderecoDBToplink();
        int ids[] = {1, 3, 4};
        imprimirPro = false;

        DataHoje dataH = new DataHoje();
        Demissao demissao = dbDem.pesquisaCodigo(Integer.parseInt(((SelectItem) getListaMotivoDemissao().get(idMotivoDemissao)).getDescription()));
        if (!pessoaEmpresa.getDemissao().isEmpty() && pessoaEmpresa.getDemissao() != null) {
            if (demissao.getId() == 1) {
                if (DataHoje.converteDataParaInteger(pessoaEmpresa.getDemissao())
                        > DataHoje.converteDataParaInteger(dataH.incrementarMeses(1, DataHoje.data()))) {
                    msgConfirma = "Por " + demissao.getDescricao() + " data de Demissão não pode ser maior que 30 dias!";
                    return null;
                }
            } else if (demissao.getId() == 2) {
                if (DataHoje.converteDataParaInteger(pessoaEmpresa.getDemissao())
                        > DataHoje.converteDataParaInteger(dataH.incrementarMeses(3, DataHoje.data()))) {
                    msgConfirma = "Por " + demissao.getDescricao() + " data de Demissão não pode ser maior que 90 dias!";
                    return null;
                }
            } else if (demissao.getId() == 3) {
                if (DataHoje.converteDataParaInteger(pessoaEmpresa.getDemissao())
                        > DataHoje.converteDataParaInteger(dataH.incrementarDias(10, DataHoje.data()))) {
                    msgConfirma = "Por " + demissao.getDescricao() + " data de Demissão não pode ser maior que 10 dias!";
                    return null;
                }
            }
        } else {
            msgConfirma = "Data de Demissão é obrigatória!";
            return null;
        }

        if (!pessoaEmpresa.getAdmissao().isEmpty() && pessoaEmpresa.getAdmissao() != null) {
            if (DataHoje.converteDataParaInteger(pessoaEmpresa.getAdmissao())
                    > DataHoje.converteDataParaInteger(pessoaEmpresa.getDemissao())) {
                msgConfirma = "Data de Admissão é maior que data de Demissão!";
                return null;
            }
        } else {
            msgConfirma = "Data de Admissão é obrigatória!";
            return null;
        }

        // SALVAR FISICA -----------------------------------------------
        sv.abrirTransacao();
        fisica.getPessoa().setTipoDocumento(dbTipo.pesquisaCodigo(1));
        if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(fisica.getPessoa().getDocumento()))) {
            msgConfirma = "Documento Inválido!";
            sv.desfazerTransacao();
            return null;
        }

        if (fisica.getId() == -1) {
            if (!dbFis.pesquisaFisicaPorNomeNascRG(fisica.getPessoa().getNome(),
                    fisica.getDtNascimento(),
                    fisica.getRg()).isEmpty()) {
                msgConfirma = "Esta pessoa já esta cadastrada!";
                sv.desfazerTransacao();
                return null;
            }
            listDocumento = dbFis.pesquisaFisicaPorDoc(fisica.getPessoa().getDocumento());
            if (!listDocumento.isEmpty()) {
                msgConfirma = "Documento já existente!";
                sv.desfazerTransacao();
                return null;
            }

            if (sv.inserirObjeto(fisica.getPessoa())) {
                sv.inserirObjeto(fisica);
            } else {
                msgConfirma = "Erro ao Inserir pessoa!";
                sv.desfazerTransacao();
                return null;
            }
        } else {
            listDocumento = dbFis.pesquisaFisicaPorDoc(fisica.getPessoa().getDocumento());
            for (int i = 0; i < listDocumento.size(); i++) {
                if (!listDocumento.isEmpty() && ((Fisica) listDocumento.get(i)).getId() != fisica.getId()) {
                    msgConfirma = "Documento já existente!";
                    sv.desfazerTransacao();
                    return null;
                }
            }
            List<Fisica> fisi = dbFis.pesquisaFisicaPorNomeNascRG(fisica.getPessoa().getNome(),
                    fisica.getDtNascimento(),
                    fisica.getRg());
            if (!fisi.isEmpty()) {
                for (int i = 0; i < fisi.size(); i++) {
                    if (fisi.get(i).getId() != fisica.getId()) {
                        msgConfirma = "Esta pessoa já esta cadastrada!";
                        sv.desfazerTransacao();
                        return null;
                    }
                }
            }
            if (sv.alterarObjeto(fisica.getPessoa())) {
                if (sv.alterarObjeto(fisica)) {
                } else {
                    msgConfirma = "Erro ao alterar Física!";
                    sv.desfazerTransacao();
                    return null;
                }
            } else {
                msgConfirma = "Erro ao alterar Pessoa!";
                sv.desfazerTransacao();
                return null;
            }
        }
        AgendamentoDB dba = new AgendamentoDBToplink();

        if (dba.pesquisaFisicaAgendada(fisica.getId()) != null && agendamento.getId() == -1) {
            msgConfirma = "Pessoa já foi agendada!";
            sv.desfazerTransacao();
            return null;
        }

        AtendimentoDB dbat = new AtendimentoDBTopLink();
        if (dbat.pessoaOposicao(fisica.getPessoa().getDocumento())) {
            msgConfirma = "Pessoa cadastrada em oposição";
            //return null;
        }

        PessoaEnderecoDB dbp = new PessoaEnderecoDBToplink();

        if (enderecoFisica.getId() == -1) {
            if (enderecoFisica.getEndereco().getId() != -1) {
                enderecoFisica.setPessoa(fisica.getPessoa());
                PessoaEndereco pesEnd = enderecoFisica;
                for (int i = 0; i < ids.length; i++) {
                    pesEnd.setTipoEndereco(dbt.pesquisaCodigo(ids[i]));
                    if (!sv.inserirObjeto(pesEnd)) {
                        msgConfirma = "Erro ao Inserir endereço da pessoa!";
                        sv.desfazerTransacao();
                        return null;
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
            for (int i = 0; i < ends.size(); i++) {
                ends.get(i).setComplemento(enderecoFisica.getComplemento());
                ends.get(i).setEndereco(enderecoFisica.getEndereco());
                ends.get(i).setNumero(enderecoFisica.getNumero());
                ends.get(i).setPessoa(enderecoFisica.getPessoa());
                if (!sv.alterarObjeto(ends.get(i))) {
                    msgConfirma = "Erro ao atualizar endereço da pessoa!";
                    sv.desfazerTransacao();
                    return null;
                }
            }
        }
        // -------------------------------------------------------------

        switch (Integer.parseInt(((SelectItem) getListaStatus().get(idStatus)).getDescription())) {
            case 1: {
                pessoaEmpresa.setFisica(fisica);
                pessoaEmpresa.setJuridica(juridica);
                pessoaEmpresa.setFuncao(profissao);
                pessoaEmpresa.setAvisoTrabalhado(Boolean.valueOf(tipoAviso));
                agendamento.setDemissao(demissao);
                agendamento.setHomologador(null);
                agendamento.setPessoaEmpresa(pessoaEmpresa);
                agendamento.setStatus(dbSta.pesquisaCodigo(2));
                break;
            }
            case 2: {
                pessoaEmpresa.setFisica(fisica);
                pessoaEmpresa.setJuridica(juridica);
                pessoaEmpresa.setAvisoTrabalhado(Boolean.valueOf(tipoAviso));
                pessoaEmpresa.setFuncao(profissao);
                agendamento.setDemissao(demissao);
                agendamento.setPessoaEmpresa(pessoaEmpresa);
                break;
            }
            case 6: {
                pessoaEmpresa.setFisica(fisica);
                pessoaEmpresa.setJuridica(juridica);
                pessoaEmpresa.setFuncao(profissao);
                pessoaEmpresa.setAvisoTrabalhado(Boolean.valueOf(tipoAviso));
                agendamento.setDemissao(demissao);
                agendamento.setHomologador(null);
                agendamento.setPessoaEmpresa(pessoaEmpresa);
                agendamento.setStatus(dbSta.pesquisaCodigo(2));
                break;
            }
        }

        if (pessoaEmpresa.getId() == -1) {
            if (!sv.inserirObjeto(pessoaEmpresa)) {
                msgConfirma = "Erro ao inserir Pessoa Empresa!";
                sv.desfazerTransacao();
                return null;
            }
        } else {
            if (!sv.alterarObjeto(pessoaEmpresa)) {
                msgConfirma = "Erro ao alterar Pessoa Empresa!";
                sv.desfazerTransacao();
                return null;
            }
        }

        if (agendamento.getId() == -1) {
            agendamento.setFilial(macFilial.getFilial());
            agendamento.setAgendador((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario")); // USUARIO SESSAO
            agendamento.setRecepcao(null);
            agendamento.setDtEmissao(DataHoje.dataHoje());
            if (sv.inserirObjeto(agendamento)) {
                sv.comitarTransacao();
                msgConfirma = "Para imprimir Protocolo clique aqui!";
                id_protocolo = agendamento.getId();
                //limpar();
            }
        } else {
            if (sv.alterarObjeto(agendamento)) {
                sv.comitarTransacao();
                msgConfirma = "Agendamento atualizado com Sucesso! imprimir Protocolo clicando aqui!";
                id_protocolo = agendamento.getId();
                //limpar();
            }
        }
        imprimirPro = true;
        return null;
    }

    public String salvarMais() {

        return null;
    }

    public String cancelar() {
        strEndereco = "";
        renderAgendamento = true;
        renderConcluir = false;
        tipoAviso = "true";
        msgConfirma = "";
        msgAgendamento = "";
        fisica = new Fisica();
        agendamento = new Agendamento();
        pessoaEmpresa = new PessoaEmpresa();
        profissao = new Profissao();
        juridica = new Juridica();
        listaMovimento.clear();
        enderecoFisica = new PessoaEndereco();
        return "agendamento";
    }

    public String cancelarHorario() {
        AgendamentoDB dbAg = new AgendamentoDBToplink();
        PessoaEmpresaDB dbPesEmp = new PessoaEmpresaDBToplink();
        StatusDB dbSta = new StatusDBToplink();
        agendamento.setStatus(dbSta.pesquisaCodigo(3));
        dbAg.update(agendamento);
        pessoaEmpresa.setDtDemissao(null);
        dbPesEmp.update(pessoaEmpresa);
        strEndereco = "";
        renderAgendamento = true;
        renderConcluir = false;
        renderCancelarHorario = false;
        renderCancelar = true;
        tipoAviso = "true";
        msgConfirma = "";
        msgAgendamento = "";
        fisica = new Fisica();
        agendamento = new Agendamento();
        pessoaEmpresa = new PessoaEmpresa();
        profissao = new Profissao();
        juridica = new Juridica();
        listaMovimento.clear();
        enderecoFisica = new PessoaEndereco();
        return "agendamento";
    }

    public void limpar() {
        strEndereco = "";
        renderAgendamento = true;
        renderConcluir = false;
        tipoAviso = "true";
        msgAgendamento = "";
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
        renderAgendamento = true;
        renderConcluir = false;
        tipoAviso = "true";
        msgAgendamento = "";
        fisica = new Fisica();
        pessoaEmpresa = new PessoaEmpresa();
        agendamento = new Agendamento();
        profissao = new Profissao();
        protocolo = 0;
        enderecoFisica = new PessoaEndereco();
    }

    public void pesquisarFuncionarioCPF() throws IOException {
        msgConfirma = "";
        if (!fisica.getPessoa().getDocumento().isEmpty() && !fisica.getPessoa().getDocumento().equals("___.___.___-__")) {

            if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(fisica.getPessoa().getDocumento()))) {
                msgConfirma = "Documento Inválido!";
                return;
            }

            FisicaDB dbFis = new FisicaDBToplink();
            AgendamentoDB db = new AgendamentoDBToplink();
            PessoaEnderecoDB dbe = new PessoaEnderecoDBToplink();

            String documento = fisica.getPessoa().getDocumento();

            List<Fisica> listFisica = dbFis.pesquisaFisicaPorDocSemLike(documento);
            List<Oposicao> listao = db.pesquisaFisicaOposicaoSemEmpresa(documento);
            List<PessoaEmpresa> listape = db.pesquisaPessoaEmpresaPertencente(documento);

            if (!listFisica.isEmpty()) {
                Agendamento age = db.pesquisaFisicaAgendada(listFisica.get(0).getId());
                if (age != null) {
                    msgConfirma = "CPF já foi agendado";
                    return;
                }

                if (!listape.isEmpty() && listape.get(0).getDtDemissao() != null) {
                    listape.clear();
                }
            }

            // SEM PESSOA FISICA E SEM OPOSICAO
            if (listFisica.isEmpty() && listao.isEmpty()) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/Sindical/agendamento.jsf");
                // SEM PESSOA FISICA E COM OPOSICAO    
            } else if (listFisica.isEmpty() && !listao.isEmpty()) {
                msgConfirma = "CPF cadastrado em oposição em " + listao.get(0).getEmissao();

                fisica.getPessoa().setNome(listao.get(0).getOposicaoPessoa().getNome());
                fisica.setRg(listao.get(0).getOposicaoPessoa().getRg());
                fisica.setSexo(listao.get(0).getOposicaoPessoa().getSexo());
                fisica.getPessoa().setDocumento(documento);

                juridica = listao.get(0).getJuridica();
                // COM FISICA, COM PESSOA EMPRESA E SEM OPOSICAO    
            } else if (!listFisica.isEmpty() && !listape.isEmpty() && listao.isEmpty()) {
                pessoaEmpresa = listape.get(0);
                fisica = pessoaEmpresa.getFisica();
                profissao = pessoaEmpresa.getFuncao();
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("juridicaPesquisa", pessoaEmpresa.getJuridica());
                disabledEmpresa = true;
                enderecoFisica = dbe.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 3);
                protocolo = 0;
                FacesContext.getCurrentInstance().getExternalContext().redirect("/Sindical/agendamento.jsf");
                // COM FISICA, SEM PESSOA EMPRESA E SEM OPOSICAO    
            } else if (!listFisica.isEmpty() && listape.isEmpty() && listao.isEmpty()) {
                fisica = listFisica.get(0);
                pessoaEmpresa = new PessoaEmpresa();
                juridica = new Juridica();
                disabledEmpresa = false;
                enderecoFisica = dbe.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 3);
                FacesContext.getCurrentInstance().getExternalContext().redirect("/Sindical/agendamento.jsf");
                // COM FISICA, COM PESSOA EMPRESA COM OPOSICAO
            } else if (!listFisica.isEmpty() && !listape.isEmpty() && !listao.isEmpty()) {
                msgConfirma = "CPF cadastrado em oposição em " + listao.get(0).getEmissao();

                pessoaEmpresa = listape.get(0);
                fisica = pessoaEmpresa.getFisica();
                profissao = pessoaEmpresa.getFuncao();
                disabledEmpresa = true;
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
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("juridicaPesquisa", juridica);
                // COM FISICA, SEM PESSOA EMPRESA COM OPOSICAO
            } else if (!listFisica.isEmpty() && listape.isEmpty() && !listao.isEmpty()) {
                msgConfirma = "CPF cadastrado em oposição em " + listao.get(0).getEmissao();

                fisica = listFisica.get(0);
                juridica = listao.get(0).getJuridica();
                disabledEmpresa = false;
                enderecoFisica = dbe.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 3);
            }
//                if (listao.isEmpty()){
//                    
//                        if (!listao.isEmpty()){
//                            msgConfirma = "CPF cadastrado em oposição em "+ listao.get(0).getEmissao();
//
//                        }
//                }else{
//                    fisica = new Fisica();
//
//                    pessoaEmpresa.setJuridica(listao.get(0).getJuridica());
//                    profissao = pessoaEmpresa.getFuncao();
//                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("juridicaPesquisa", pessoaEmpresa.getJuridica());
//                    
//
//                    enderecoFisica = new PessoaEndereco();
//                    return;
//                }

//            // NÃO ESTA LIMPANDO O AGENDAMENTO, PENSAR EM COMO RESOLVER 
//            //agendamento = new Agendamento();
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
//            
//            TipoDocumentoDB dbTipo = new TipoDocumentoDBToplink();
//            PessoaEnderecoDB dbe = new PessoaEnderecoDBToplink();
//            fisica.getPessoa().setTipoDocumento(dbTipo.pesquisaCodigo(1));
//            
//            PessoaEmpresa pe = db.pesquisaPessoaEmpresaOutra(documento);
//            
//            if (pe.getId() != -1){
//                pessoaEmpresa = pe;
//                fisica = pessoaEmpresa.getFisica();
//                profissao = pessoaEmpresa.getFuncao();
//                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("juridicaPesquisa",pessoaEmpresa.getJuridica());
//                disabledEmpresa = true;
//                enderecoFisica = dbe.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 3);
//                protocolo = 0;
//                //agendamento = new Agendamento();
//                FacesContext.getCurrentInstance().getExternalContext().redirect("/Sindical/agendamento.jsf");
//                return;
//            }else{
//                
//                
//                if (list.isEmpty()){
//
//                }else{
//                    pessoaEmpresa = list.get(0);
//                    fisica = pessoaEmpresa.getFisica();
//                    profissao = pessoaEmpresa.getFuncao();
//                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("juridicaPesquisa", pessoaEmpresa.getJuridica());
//                    disabledEmpresa = true;
//                    enderecoFisica = dbe.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 3);
//                    protocolo = 0;
//                    //agendamento = new Agendamento();
//                    FacesContext.getCurrentInstance().getExternalContext().redirect("/Sindical/agendamento.jsf");
//                    return;
//                }
//                
//                
//                for (int i = 0; i < listFisica.size(); i++){
//                    if(!listFisica.isEmpty() && listFisica.get(i).getId() != fisica.getId()){
//                        fisica = listFisica.get(i);
//                        pessoaEmpresa = new PessoaEmpresa();
//                        juridica = new Juridica();
//                        disabledEmpresa = false;
//                        enderecoFisica = dbe.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 3);
//                        //agendamento = new Agendamento();
//                        FacesContext.getCurrentInstance().getExternalContext().redirect("/Sindical/agendamento.jsf");
//                        return;
//                    }
//                }
//                
//                if (fisica.getId() != -1 && juridica.getId() != -1){
//                    Oposicao op = db.pesquisaFisicaOposicao(documento, juridica.getId());
//                    if (op == null)
//                        msgConfirma = "Erro na pesquisa Oposição!";
//                    else if (op.getId() != -1){
//                        msgConfirma = "CPF cadastrado em oposição em "+ op.getEmissao();
////                        fisica = new Fisica();
////                        enderecoFisica = new PessoaEndereco();
//                        return;
//                    }                        
//                }else{
//                    
//                    if (!listao.isEmpty()) {
//                        msgConfirma = "CPF cadastrado em oposição em "+ listao.get(0).getEmissao();
//                        fisica = new Fisica();
//                       
//                        
//                        fisica.getPessoa().setNome(listao.get(0).getOposicaoPessoa().getNome());
//                        fisica.setRg(listao.get(0).getOposicaoPessoa().getRg());
//                        fisica.setSexo(listao.get(0).getOposicaoPessoa().getSexo());
//                        fisica.getPessoa().setDocumento(documento);
//                        
//                        enderecoFisica = new PessoaEndereco();
//                        return;
//                    }        
//                }
//            }
//        }
//        if (fisica.getId() != -1){
//            fisica = new Fisica();
//            pessoaEmpresa = new PessoaEmpresa();
//            enderecoFisica = new PessoaEndereco();
//            juridica = new Juridica();
//            //agendamento = new Agendamento();
//            listaMovimento.clear();
//        }
//        FacesContext.getCurrentInstance().getExternalContext().redirect("/Sindical/agendamento.jsf");
        }
//    public void pesquisarFuncionarioCPF() throws IOException{
//        if (!fisica.getPessoa().getDocumento().isEmpty() && !fisica.getPessoa().getDocumento().equals("___.___.___-__")){
//            String documento = fisica.getPessoa().getDocumento();
//            
//            fisica = new Fisica();
//            fisica.getPessoa().setDocumento(documento);
//            pessoaEmpresa = new PessoaEmpresa();
//            enderecoFisica = new PessoaEndereco();
//            juridica = new Juridica();
//            listaMovimento.clear();
//            // NÃO ESTA LIMPANDO O AGENDAMENTO, PENSAR EM COMO RESOLVER 
//            //agendamento = new Agendamento();
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
//            
//            AgendamentoDB db = new AgendamentoDBToplink();
//            TipoDocumentoDB dbTipo = new TipoDocumentoDBToplink();
//            PessoaEnderecoDB dbe = new PessoaEnderecoDBToplink();
//            FisicaDB dbFis = new FisicaDBToplink();
//            fisica.getPessoa().setTipoDocumento(dbTipo.pesquisaCodigo(1));
//            
//            PessoaEmpresa pe = db.pesquisaPessoaEmpresaOutra(documento);
//            
//            if (pe.getId() != -1){
//                pessoaEmpresa = pe;
//                fisica = pessoaEmpresa.getFisica();
//                profissao = pessoaEmpresa.getFuncao();
//                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("juridicaPesquisa",pessoaEmpresa.getJuridica());
//                disabledEmpresa = true;
//                enderecoFisica = dbe.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 3);
//                protocolo = 0;
//                //agendamento = new Agendamento();
//                FacesContext.getCurrentInstance().getExternalContext().redirect("/Sindical/agendamento.jsf");
//                return;
//            }else{
//                List<PessoaEmpresa> list = db.pesquisaPessoaEmpresaPertencente(documento);
//                
//                if (list.isEmpty()){
//                    
//                }else{
//                    pessoaEmpresa = list.get(0);
//                    fisica = pessoaEmpresa.getFisica();
//                    profissao = pessoaEmpresa.getFuncao();
//                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("juridicaPesquisa", pessoaEmpresa.getJuridica());
//                    disabledEmpresa = true;
//                    enderecoFisica = dbe.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 3);
//                    protocolo = 0;
//                    //agendamento = new Agendamento();
//                    FacesContext.getCurrentInstance().getExternalContext().redirect("/Sindical/agendamento.jsf");
//                    return;
//                }
//                
//                List<Fisica> listFisica = dbFis.pesquisaFisicaPorDocSemLike(documento);
//                for (int i = 0; i < listFisica.size(); i++){
//                    if(!listFisica.isEmpty() && listFisica.get(i).getId() != fisica.getId()){
//                        fisica = listFisica.get(i);
//                        pessoaEmpresa = new PessoaEmpresa();
//                        juridica = new Juridica();
//                        disabledEmpresa = false;
//                        enderecoFisica = dbe.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 3);
//                        //agendamento = new Agendamento();
//                        FacesContext.getCurrentInstance().getExternalContext().redirect("/Sindical/agendamento.jsf");
//                        return;
//                    }
//                }
//                
//                if (fisica.getId() != -1 && juridica.getId() != -1){
//                    Oposicao op = db.pesquisaFisicaOposicao(documento, juridica.getId());
//                    if (op == null)
//                        msgConfirma = "Erro na pesquisa Oposição!";
//                    else if (op.getId() != -1){
//                        msgConfirma = "CPF cadastrado em oposição em "+ op.getEmissao();
////                        fisica = new Fisica();
////                        enderecoFisica = new PessoaEndereco();
//                        return;
//                    }                        
//                }else{
//                    List<Oposicao> listao = db.pesquisaFisicaOposicaoSemEmpresa(documento);
//                    if (!listao.isEmpty()) {
//                        msgConfirma = "CPF cadastrado em oposição em "+ listao.get(0).getEmissao();
//                        fisica = new Fisica();
//                       
//                        
//                        fisica.getPessoa().setNome(listao.get(0).getOposicaoPessoa().getNome());
//                        fisica.setRg(listao.get(0).getOposicaoPessoa().getRg());
//                        fisica.setSexo(listao.get(0).getOposicaoPessoa().getSexo());
//                        fisica.getPessoa().setDocumento(documento);
//                        
//                        enderecoFisica = new PessoaEndereco();
//                        return;
//                    }        
//                }
//            }
//        }
//        if (fisica.getId() != -1){
//            fisica = new Fisica();
//            pessoaEmpresa = new PessoaEmpresa();
//            enderecoFisica = new PessoaEndereco();
//            juridica = new Juridica();
//            //agendamento = new Agendamento();
//            listaMovimento.clear();
//        }
//        FacesContext.getCurrentInstance().getExternalContext().redirect("/Sindical/agendamento.jsf");
    }

    public String imprimirProtocolo(int proto) {
        if (proto == -1) {
            proto = id_protocolo;
        }

        Collection lista = new ArrayList<ParametroProtocolo>();
        try {
            JasperReport jasper = (JasperReport) JRLoader.loadObject(
                    ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/PROTOCOLO.jasper"));

            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

            Agendamento age = (Agendamento) sv.pesquisaCodigo(proto, "Agendamento");
            Juridica sindicato = (Juridica) sv.pesquisaCodigo(1, "Juridica");

            Juridica contabilidade;
            if (age.getPessoaEmpresa().getJuridica().getContabilidade() != null) {
                contabilidade = age.getPessoaEmpresa().getJuridica().getContabilidade();
            } else {
                contabilidade = new Juridica();
            }

            getEnderecoFilial();

            String datax = "", horario = "";
            if (!age.getData().isEmpty()) {
                datax = age.getData();
                horario = age.getHorarios().getHora();
            }
            Registro registro = (Registro) new SalvarAcumuladoDBToplink().pesquisaCodigo(1, "Registro");

            lista.add(new ParametroProtocolo(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + controleUsuarioJSFBean.getCliente() + "/Imagens/LogoCliente.png"),
                    sindicato.getPessoa().getNome(),
                    sindicato.getPessoa().getSite(),
                    sindicato.getPessoa().getTipoDocumento().getDescricao(),
                    sindicato.getPessoa().getDocumento(),
                    enderecoFilial.getEndereco().getDescricaoEndereco().getDescricao(),
                    enderecoFilial.getEndereco().getLogradouro().getDescricao(),
                    enderecoFilial.getNumero(),
                    enderecoFilial.getComplemento(),
                    enderecoFilial.getEndereco().getBairro().getDescricao(),
                    enderecoFilial.getEndereco().getCep(),
                    enderecoFilial.getEndereco().getCidade().getCidade(),
                    enderecoFilial.getEndereco().getCidade().getUf(),
                    macFilial.getFilial().getFilial().getPessoa().getTelefone1(),
                    macFilial.getFilial().getFilial().getPessoa().getEmail1(),
                    String.valueOf(age.getId()),
                    datax,
                    horario,
                    age.getPessoaEmpresa().getJuridica().getPessoa().getDocumento(),
                    age.getPessoaEmpresa().getJuridica().getPessoa().getNome(),
                    contabilidade.getPessoa().getNome(),
                    age.getPessoaEmpresa().getFisica().getPessoa().getNome(),
                    age.getPessoaEmpresa().getFisica().getPessoa().getDocumento(),
                    registro.getDocumentoHomologacao(),
                    registro.getFormaPagamentoHomologacao(),
                    age.getEmissao()));


            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
            JasperPrint print = JasperFillManager.fillReport(
                    jasper,
                    null,
                    dtSource);
            byte[] arquivo = JasperExportManager.exportReportToPdf(print);

            String nomeDownload = "imp_protocolo_" + proto + ".pdf";

            String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + controleUsuarioJSFBean.getCliente() + "/Arquivos/downloads/protocolo");

            SalvaArquivos sa = new SalvaArquivos(arquivo, nomeDownload, false);
            sa.salvaNaPasta(pathPasta);

            Download download = new Download(nomeDownload,
                    pathPasta,
                    "application/pdf",
                    FacesContext.getCurrentInstance());
            download.baixar();
        } catch (Exception e) {
        }
        return null;
    }

    public String pesquisarProtocolo() {
        AgendamentoDB db = new AgendamentoDBToplink();
        if (protocolo > 0) {
            Agendamento age = new Agendamento(), age2 = new Agendamento();
            age.setData(agendamento.getData());
            age.setHorarios(agendamento.getHorarios());
            age2 = db.pesquisaProtocolo(protocolo);
            if (age2 != null) {
                agendamento = age2;
                agendamento.setData(age.getData());
                agendamento.setHorarios(age.getHorarios());
                agendamento.setAgendador((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario"));
                agendamento.setFilial(macFilial.getFilial());
                fisica = agendamento.getPessoaEmpresa().getFisica();
                PessoaEnderecoDB dbe = new PessoaEnderecoDBToplink();
                enderecoFisica = dbe.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 3);
                pessoaEmpresa = agendamento.getPessoaEmpresa();
                profissao = pessoaEmpresa.getFuncao();
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("juridicaPesquisa", pessoaEmpresa.getJuridica());
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
        if (agendamento.getId() == -1) {
            agendamento = (Agendamento) new SalvarAcumuladoDBToplink().pesquisaCodigo(id_protocolo, "Agendamento");
        }
        imprimirPro = false;

        if (agendamento.getEmail().isEmpty()) {
            msgConfirma = "Email destinatário vazio!";
            return null;
        }

        try {
            JasperReport jasper = (JasperReport) JRLoader.loadObject(
                    ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/PROTOCOLO.jasper"));

            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            Juridica sindicato = (Juridica) sv.pesquisaCodigo(1, "Juridica");

            Juridica contabilidade;
            if (agendamento.getPessoaEmpresa().getJuridica().getContabilidade() != null) {
                contabilidade = agendamento.getPessoaEmpresa().getJuridica().getContabilidade();
            } else {
                contabilidade = new Juridica();
            }

            getEnderecoFilial();

            String datax = "", horario = "";
            if (!agendamento.getData().isEmpty()) {
                datax = agendamento.getData();
                horario = agendamento.getHorarios().getHora();
            }
            Registro registro = (Registro) new SalvarAcumuladoDBToplink().pesquisaCodigo(1, "Registro");

            Collection lista = new ArrayList<ParametroProtocolo>();
            lista.add(new ParametroProtocolo(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Imagens/LogoCliente.png"),
                    sindicato.getPessoa().getNome(),
                    sindicato.getPessoa().getSite(),
                    sindicato.getPessoa().getTipoDocumento().getDescricao(),
                    sindicato.getPessoa().getDocumento(),
                    enderecoFilial.getEndereco().getDescricaoEndereco().getDescricao(),
                    enderecoFilial.getEndereco().getLogradouro().getDescricao(),
                    enderecoFilial.getNumero(),
                    enderecoFilial.getComplemento(),
                    enderecoFilial.getEndereco().getBairro().getDescricao(),
                    enderecoFilial.getEndereco().getCep(),
                    enderecoFilial.getEndereco().getCidade().getCidade(),
                    enderecoFilial.getEndereco().getCidade().getUf(),
                    macFilial.getFilial().getFilial().getPessoa().getTelefone1(),
                    macFilial.getFilial().getFilial().getPessoa().getEmail1(),
                    String.valueOf(agendamento.getId()),
                    datax,
                    horario,
                    agendamento.getPessoaEmpresa().getJuridica().getPessoa().getDocumento(),
                    agendamento.getPessoaEmpresa().getJuridica().getPessoa().getNome(),
                    contabilidade.getPessoa().getNome(),
                    agendamento.getPessoaEmpresa().getFisica().getPessoa().getNome(),
                    agendamento.getPessoaEmpresa().getFisica().getPessoa().getDocumento(),
                    registro.getDocumentoHomologacao(),
                    registro.getFormaPagamentoHomologacao(),
                    DataHoje.data()));


            //byte[] arquivo = new byte[0];
            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
            JasperPrint print = JasperFillManager.fillReport(
                    jasper,
                    null,
                    dtSource);
            byte[] arquivo = JasperExportManager.exportReportToPdf(print);

            String nomeDownload = "envio_protocolo_" + agendamento.getId() + ".pdf";
            String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + controleUsuarioJSFBean.getCliente() + "/Arquivos/downloads/protocolo");
            if (!new File(pathPasta).exists()) {
                File fNew = new File(pathPasta);
                fNew.mkdir();
            }

            SalvaArquivos sa = new SalvaArquivos(arquivo, nomeDownload, false);
            sa.salvaNaPasta(pathPasta);

            LinksDB db = new LinksDBToplink();
            Links link = db.pesquisaNomeArquivo(nomeDownload);

            if (link == null) {
                link = new Links();
                link.setCaminho("/Sindical/Cliente/" + controleUsuarioJSFBean.getCliente() + "/Arquivos/downloads/protocolo");
                link.setNomeArquivo(nomeDownload);
                link.setPessoa(agendamento.getPessoaEmpresa().getFisica().getPessoa());

                sv.abrirTransacao();
                if (sv.inserirObjeto(link)) {
                    sv.comitarTransacao();
                } else {
                    sv.desfazerTransacao();
                }
            }

            List<Pessoa> p = new ArrayList();
            agendamento.getPessoaEmpresa().getFisica().getPessoa().setEmail1(agendamento.getEmail());
            p.add(agendamento.getPessoaEmpresa().getFisica().getPessoa());
            String[] ret;
            //String[] ret = new String[2];
            if (registro.isEnviarEmailAnexo()) {
                //EnviarEmail.EnviarEmailComAnexo(registro, juridica, horario, null);
                List<File> fls = new ArrayList<File>();
                fls.add(new File(pathPasta + "/" + nomeDownload));

                ret = EnviarEmail.EnviarEmailPersonalizado(registro,
                        p,
                        " <h5>Baixe seu protocolo que esta anexado neste email</5><br /><br />",
                        fls,
                        "Envio de protocolo de homologação");
            } else {
                ret = EnviarEmail.EnviarEmailPersonalizado(registro,
                        p,
                        " <h5>Visualize seu protocolo clicando no link abaixo</5><br /><br />"
                        + " <a href='" + registro.getUrlPath() + "/Sindical/acessoLinks.jsf?cliente=" + controleUsuarioJSFBean.getCliente() + "&amp;arquivo=" + nomeDownload + "' target='_blank'>Clique aqui para abrir seu protocolo</a><br />",
                        //" <a href='"+registro.getUrlPath()+"/Sindical/Arquivos/downloads/protocolo/"+nomeDownload+".pdf' target='_blank'>Clique aqui para abrir seu protocolo</a><br />", 
                        new ArrayList(),
                        "Envio de protocolo de homologação");

            }
            if (!ret[1].isEmpty()) {
                msgConfirma = ret[1];
                limpar();
            } else {
                msgConfirma = ret[0];
            }
        } catch (Exception e) {
            NovoLog log = new NovoLog();
            log.novo("Erro de envio de protocolo por e-mail:", "Mensagem: " + e.getMessage() + " - Causa: " + e.getCause() + " - Caminho: " + e.getStackTrace().toString());
        }
        return null;
    }

    public String pesquisaEndereco() {
        EnderecoDB db = new EnderecoDBToplink();
        listaEnderecos.clear();
        if (!cepEndereco.isEmpty()) {
            listaEnderecos = db.pesquisaEnderecoCep(cepEndereco);
        }
        return null;
    }

    public String editarEndereco() {
        enderecoFisica.setEndereco((Endereco) listaEnderecos.get(idIndexEndereco));
        //return null;
        return "agendamento";
    }

    public int getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

    public void refreshForm() {
    }

    public boolean isRenderAgendamento() {
        return renderAgendamento;
    }

    public void setRenderAgendamento(boolean renderAgendamento) {
        this.renderAgendamento = renderAgendamento;
    }

    public boolean isRenderConcluir() {
        return renderConcluir;
    }

    public void setRenderConcluir(boolean renderConcluir) {
        this.renderConcluir = renderConcluir;
    }

    public Juridica getJuridica() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa") != null) {
            juridica = (Juridica) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa");
            listaMovimento.clear();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pessoaPesquisa", juridica.getPessoa());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
            if (juridica.getContabilidade() != null && agendamento.getId() == -1) {
                agendamento.setTelefone(juridica.getContabilidade().getPessoa().getTelefone1());
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

    public String getMsgAgendamento() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoFilial") == null) {
            msgAgendamento = "Não existe filial definida!";
        } else {
            macFilial = (MacFilial) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoFilial");
        }
        return msgAgendamento;
    }

    public void setMsgAgendamento(String msgAgendamento) {
        this.msgAgendamento = msgAgendamento;
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

    public String getTipoAviso() {
        return tipoAviso;
    }

    public void setTipoAviso(String tipoAviso) {
        this.tipoAviso = tipoAviso;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

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

    public boolean isDisabledEmpresa() {
        return disabledEmpresa;
    }

    public void setDisabledEmpresa(boolean disabledEmpresa) {
        this.disabledEmpresa = disabledEmpresa;
    }

    public String getStrSalvar() {
        return strSalvar;
    }

    public void setStrSalvar(String strSalvar) {
        this.strSalvar = strSalvar;
    }

    public String getStatusEmpresa() {
        AgendamentoDB db = new AgendamentoDBToplink();
        if (juridica.getId() != -1 && listaMovimento.isEmpty()) {
            listaMovimento = db.pesquisaEmpresaEmDebito(juridica.getPessoa().getId(), DataHoje.data());
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

    public boolean isDisabledProtocolo() {
        return disabledProtocolo;
    }

    public void setDisabledProtocolo(boolean disabledProtocolo) {
        this.disabledProtocolo = disabledProtocolo;
    }

    public String getCepEndereco() {
        return cepEndereco;
    }

    public void setCepEndereco(String cepEndereco) {
        this.cepEndereco = cepEndereco;
    }

    public List getListaEnderecos() {
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
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pessoaPesquisa", juridica.getPessoa());
        return ((chamadaPaginaJSFBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).extratoTela();
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

    public boolean isRenderTransfere() {
        return renderTransfere;
    }

    public void setRenderTransfere(boolean renderTransfere) {
        this.renderTransfere = renderTransfere;
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

    public void verificaNaoAtendidos() {
        AgendamentoDB dB = new AgendamentoDBToplink();
        if (dB.verificaNaoAtendidosSegRegistroAgendamento()) {
            return;
        }
        return;
    }

    public void mensagemAgendamento() {
        msgAgendamento = "";
        FeriadosDB feriadosDB = new FeriadosDBToplink();
        if (macFilial.getFilial().getFilial().getId() != -1) {
            List<Feriados> feriados = feriadosDB.pesquisarPorDataFilialEData(DataHoje.converteData(data), macFilial.getFilial());
            if (!feriados.isEmpty()) {
                msgAgendamento = "Nesta data existem feriados/agenda: ";
                for (int i = 0; i < feriados.size(); i++) {
                    if (i == 0) {
                        msgAgendamento += feriados.get(i).getNome();
                    } else {
                        msgAgendamento +=  ", " + feriados.get(i).getNome();
                    }
                }
            }
        }
    }
}
