//package br.com.rtools.homologacao.beans;
//
//import br.com.rtools.pessoa.beans.PesquisarProfissaoBean;
//import br.com.rtools.arrecadacao.Oposicao;
//import br.com.rtools.atendimento.db.AtendimentoDB;
//import br.com.rtools.atendimento.db.AtendimentoDBTopLink;
//import br.com.rtools.endereco.Endereco;
//import br.com.rtools.endereco.db.EnderecoDB;
//import br.com.rtools.endereco.db.EnderecoDBToplink;
//import br.com.rtools.financeiro.Movimento;
//import br.com.rtools.homologacao.Agendamento;
//import br.com.rtools.homologacao.Demissao;
//import br.com.rtools.homologacao.Feriados;
//import br.com.rtools.homologacao.Horarios;
//import br.com.rtools.homologacao.ListaAgendamento;
//import br.com.rtools.homologacao.Status;
//import br.com.rtools.homologacao.db.*;
//import br.com.rtools.impressao.beans.ProtocoloAgendamento;
//import br.com.rtools.movimento.ImprimirBoleto;
//import br.com.rtools.pessoa.*;
//import br.com.rtools.pessoa.db.*;
//import br.com.rtools.seguranca.MacFilial;
//import br.com.rtools.seguranca.Registro;
//import br.com.rtools.seguranca.Usuario;
//import br.com.rtools.seguranca.controleUsuario.ChamadaPaginaBean;
//import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
//import br.com.rtools.utilitarios.*;
//import java.io.File;
//import java.io.IOException;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.SessionScoped;
//import javax.faces.context.FacesContext;
//import javax.faces.model.SelectItem;
//
//@ManagedBean
//@SessionScoped
//public class AgendamentoBean1 extends PesquisarProfissaoBean implements Serializable {
//
//    private int idStatus = 0;
//    private int idMotivoDemissao = 0;
//    private int idHorarioTransferencia = 0;
//    private int idHorarioAlternativo = 0;
//    private String tipoAviso = "true";
//    private String strEndereco = "";
//    private String msgAgendamento = "";
//    private String msgConfirma = "";
//    private String strSalvar = "Agendar";
//    private String statusEmpresa = "REGULAR";
//    private String cepEndereco = "";
//    private String strContribuinte = "";
//    private String emailEmpresa = "";
//    private String styleDestaque = "";
//    private List listaGrid = new ArrayList();
//    private List listaMovimento = new ArrayList();
//    private List<SelectItem> listaStatus = new ArrayList<SelectItem>();
//    private List<SelectItem> listaDemissao = new ArrayList<SelectItem>();
//    private List<SelectItem> listaHorarioTransferencia = new ArrayList<SelectItem>();
//    private List<ListaAgendamento> listaHorarios = new ArrayList<ListaAgendamento>();
//    private Juridica juridica = new Juridica();
//    private Fisica fisica = new Fisica();
//    private Date data = DataHoje.dataHoje();
//    private Date dataTransferencia = DataHoje.dataHoje();
//    private Agendamento agendamento = new Agendamento();
//    private PessoaEndereco enderecoEmpresa = new PessoaEndereco();
//    private PessoaEmpresa pessoaEmpresa = new PessoaEmpresa();
//    private boolean renderAgendamento = true;
//    private boolean renderConcluir = false;
//    private boolean renderCancelarHorario = false;
//    private boolean renderCancelar = true;
//    private boolean disabledEmpresa = false;
//    private boolean disabledProtocolo = false;
//    private boolean renderedDisponivel = false;
//    private boolean renderTransfere = false;
//    private boolean ocultarHorarioAlternativo = true;
//    private MacFilial macFilial = null;
//    private int protocolo = 0;
//    private int idIndex = -1;
//    private int idIndexEndereco = -1;
//    private int id_protocolo = -1;
//    private List listaEnderecos = new ArrayList();
//    private PessoaEndereco enderecoFisica = new PessoaEndereco();
//    private String enviarPara = "contabilidade";
//    private PessoaEndereco enderecoFilial = new PessoaEndereco();
//    private boolean imprimirPro = false;
//
//    public String salvarTransferencia() {
//        if (getDataTransferencia().getDay() == 6 || getDataTransferencia().getDay() == 0) {
//            msgConfirma = "Fins de semana não permitido!";
//            return null;
//        }
//        if (DataHoje.converteDataParaInteger(DataHoje.converteData(getDataTransferencia()))
//                < DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()))) {
//            msgConfirma = "Data anterior ao dia de hoje!";
//            return null;
//        }
//        if (DataHoje.converteDataParaInteger(((new DataHoje()).incrementarMeses(3, DataHoje.data())))
//                < DataHoje.converteDataParaInteger(DataHoje.converteData(getDataTransferencia()))) {
//            msgConfirma = "Data maior que 3 meses!";
//            return null;
//        }
//
//        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
//        agendamento.setDtData(dataTransferencia);
//        if (getListaHorarioTransferencia().isEmpty()) {
//            msgConfirma = "Não existem horários para esse dia da semana!";
//            return null;
//        }
//        Horarios h = (Horarios) sv.find("Horarios", Integer.valueOf(getListaHorarioTransferencia().get(idHorarioTransferencia).getDescription()));
//        agendamento.setHorarios(h);
//
//        sv.abrirTransacao();
//        if (!sv.alterarObjeto(agendamento)) {
//            sv.desfazerTransacao();
//            msgConfirma = "Erro ao transferir horário";
//            return null;
//        }
//        sv.comitarTransacao();
//        listaHorarioTransferencia.clear();
//        renderTransfere = false;
//        idHorarioTransferencia = 0;
//        msgConfirma = "Horário transferido com Sucesso!";
//        return null;
//    }
//
//    public void trasfere() {
//        if (renderTransfere) {
//            renderTransfere = false;
//        } else {
//            renderTransfere = true;
//        }
//    }
//
//    public String imprimirPlanilha() {
//        if (listaMovimento.isEmpty()) {
//            return null;
//        }
//        ImprimirBoleto imp = new ImprimirBoleto();
//        List<Movimento> lista = new ArrayList();
//        List<Float> listaValores = new ArrayList<Float>();
//        for (int i = 0; i < listaMovimento.size(); i++) {
//            SalvarAcumuladoDB sadb = new SalvarAcumuladoDBToplink();
//            Movimento m = (Movimento) sadb.find("Movimento", (Integer) ((List) listaMovimento.get(i)).get(0));
//            lista.add(m);
//            listaValores.add(m.getValor());
//        }
//        if (!lista.isEmpty()) {
//            imp.imprimirPlanilha(lista, listaValores, false, false);
//            imp.visualizar(null);
//        }
//        return null;
//    }
//
//    public String enviarPlanilha() {
//        if (listaMovimento.isEmpty()) {
//            return null;
//        }
//        ImprimirBoleto imp = new ImprimirBoleto();
//        List<Movimento> lista = new ArrayList();
//        List<Float> listaValores = new ArrayList<Float>();
//        for (Object listaMovimento1 : listaMovimento) {
//            Movimento m = (Movimento) new SalvarAcumuladoDBToplink().find("Movimento", (Integer) ((List) listaMovimento1).get(0));
//            lista.add(m);
//            listaValores.add(m.getValor());
//        }
//
//        if (!lista.isEmpty()) {
//            imp.imprimirPlanilha(lista, listaValores, false, false);
//        }
//
//        try {
//            Registro reg = (Registro) (new SalvarAcumuladoDBToplink()).find("Registro", 1);
//
//            Pessoa pessoa = new Pessoa();
//            String emailPessoaHistorico = "";
//            if (enviarPara.equals("contabilidade")) {
//                if (juridica.getContabilidade() == null) {
//                    msgConfirma = "Empresa sem contabilidade";
//                    return null;
//                }
//
//                if (juridica.getContabilidade().getPessoa().getEmail1().isEmpty()) {
//                    if (emailEmpresa.equals("")) {
//                        msgConfirma = "Contabilidade sem Email para envio";
//                        return null;
//                    }
//                }
//                pessoa = juridica.getContabilidade().getPessoa();
//                pessoa.setEmail1(emailEmpresa);
//            } else {
//                if (juridica.getPessoa().getEmail1().isEmpty()) {
//                    if (emailEmpresa.equals("")) {
//                        msgConfirma = "Empresa sem Email para envio";
//                        return null;
//                    }
//                }
//                pessoa = juridica.getPessoa();
//                pessoa.setEmail1(emailEmpresa);
//            }
//
//            String nome = imp.criarLink(pessoa, reg.getUrlPath() + "/Sindical/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/boletos");
//            List<Pessoa> p = new ArrayList();
//
//            p.add(pessoa);
//            //String[] ret = new String[2];
//            String[] ret;
//            if (!reg.isEnviarEmailAnexo()) {
//                ret = EnviarEmail.EnviarEmailPersonalizado(reg,
//                        p,
//                        " <div style='background:#00ccff; padding: 15px; font-size:13pt'>Envio cadastrado para <b>" + pessoa.getNome() + " </b></div><br />"
//                        + " <h5>Visualize sua planilha de débitos clicando no link abaixo</h5><br /><br />"
//                        + " <a href='" + reg.getUrlPath() + "/Sindical/acessoLinks.jsf?cliente=" + ControleUsuarioBean.getCliente() + "&amp;arquivo=" + nome + "'>Clique aqui para abrir Planilha de Débitos</a><br />",
//                        new ArrayList(),
//                        "Envio de Débitos");
//            } else {
//                List<File> fls = new ArrayList<File>();
//                fls.add(new File(imp.getPathPasta() + "/" + nome));
//
//                ret = EnviarEmail.EnviarEmailPersonalizado(reg,
//                        p,
//                        " <div style='background:#00ccff; padding: 15px; font-size:13pt'>Envio cadastrado para <b>" + pessoa.getNome() + " </b></div><br />"
//                        + " <h5>Baixe sua planilha de débitos anexado neste email</5><br /><br />",
//                        fls,
//                        "Envio de Débitos");
//            }
//            if (!ret[1].isEmpty()) {
//                msgConfirma = ret[1];
//            } else {
//                msgConfirma = ret[0];
//            }
//            //msgConfirma = "Envio Concluído!";
//        } catch (Exception erro) {
//            System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
//
//        }
//        return null;
//    }
//
//    public String atualizar() {
//        return "agendamento";
//    }
//
//    public List<SelectItem> getListaStatus() {
//        if (listaStatus.isEmpty()) {
//            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
//            List<Status> list = (List<Status>) salvarAcumuladoDB.listaObjeto("Status");
//            for (int i = 0; i < list.size(); i++) {
//                listaStatus.add(new SelectItem(i, list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
//            }
//        }
//        return listaStatus;
//    }
//
//    public List<SelectItem> getListaDemissao() {
//        if (listaDemissao.isEmpty()) {
//            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
//            List<Demissao> list = (List<Demissao>) salvarAcumuladoDB.listaObjeto("Demissao");
//            for (int i = 0; i < list.size(); i++) {
//                listaDemissao.add(new SelectItem(i, list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
//            }
//        }
//        return listaDemissao;
//    }
//
//    public List<SelectItem> getListaHorarioTransferencia() {
//        listaHorarioTransferencia.clear();
//        if (listaHorarioTransferencia.isEmpty()) {
//            HomologacaoDB db = new HomologacaoDBToplink();
//            int idDiaSemana = DataHoje.diaDaSemana(dataTransferencia);
//            List<Horarios> select = db.pesquisaTodosHorariosDisponiveis(macFilial.getFilial().getId(), idDiaSemana);
//            int qnt;
//            int j = 0;
//            for (int i = 0; i < select.size(); i++) {
//                qnt = db.pesquisaQntdDisponivel(macFilial.getFilial().getId(), select.get(i), getDataTransferencia());
//                if (qnt == -1) {
//                    return new ArrayList();
//                }
//                if (qnt > 0) {
//                    listaHorarioTransferencia.add(new SelectItem(j, select.get(i).getHora() + " (" + qnt + ")", String.valueOf(select.get(i).getId())));
//                    j++;
//                }
//            }
//        }
//        return listaHorarioTransferencia;
//    }
//
//    public boolean pesquisarFeriado() {
//        FeriadosDB feriadosDB = new FeriadosDBToplink();
//        if (macFilial.getFilial().getFilial().getId() != -1) {
//            List<Feriados> feriados = feriadosDB.pesquisarPorDataFilialEData(DataHoje.converteData(data), macFilial.getFilial());
//            if (!feriados.isEmpty()) {
//                return true;
//            } else {
//                List<Feriados> listFeriados = (List<Feriados>) feriadosDB.pesquisarPorData(DataHoje.converteData(getData()));
//                if (!listFeriados.isEmpty()) {
//                    for (Feriados f : listFeriados) {
//                        if (f.getCidade() == null) {
//                            return true;
//                        }
//                    }
//                }
//            }
//        }
//        return false;
//
//    }
//
//    public String agendar(Agendamento a) {
//        if (getData().getDay() == 6 || getData().getDay() == 0) {
//            msgAgendamento = "Fins de semana não permitido!";
//            return null;
//        }
//        emailEmpresa = "";
//        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
//        Registro reg = (Registro) salvarAcumuladoDB.find("Registro", 1);
//        int nrAgendamentoRetroativo = DataHoje.converteDataParaInteger(DataHoje.converteData(reg.getAgendamentoRetroativo()));
//        int nrData = DataHoje.converteDataParaInteger(DataHoje.converteData(getData()));
//        int nrDataHoje = DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()));
//        if (nrAgendamentoRetroativo < nrDataHoje) {
//            if (nrData < nrDataHoje) {
//                msgAgendamento = "Data anterior ao dia de hoje!";
//                return null;
//            }
//        } else {
//            msgAgendamento = "Agendamento retroativo liberado até dia " + reg.getAgendamentoRetroativoString();
//        }
//        if (DataHoje.converteDataParaInteger(((new DataHoje()).incrementarMeses(3, DataHoje.data()))) < nrData) {
//            msgAgendamento = "Data maior que 3 meses!";
//            return null;
//        }
//
//        renderAgendamento = false;
//        renderConcluir = true;
//        protocolo = 0;
//        if (profissao.getId() == -1) {
//            profissao = (Profissao) salvarAcumuladoDB.find("Profissao", 0);
//            profissao.setProfissao("");
//        }
//
//        switch (Integer.parseInt(((SelectItem) getListaStatus().get(idStatus)).getDescription())) {
//            case 1: {
//                if (getData() == null) {
//                    msgAgendamento = "Selecione uma data para Agendamento!";
//                    renderAgendamento = true;
//                    renderConcluir = false;
//                } else {
//                    renderCancelarHorario = false;
//                    renderCancelar = true;
//                    if (pesquisarFeriado()) {
//                        msgAgendamento = "Esta data esta cadastrada como Feriado!";
//                        renderAgendamento = true;
//                        renderConcluir = false;
//                    } else {
//                        agendamento.setData(DataHoje.converteData(getData()));
//                        Horarios horarios = a.getHorarios();
//                        agendamento.setHorarios(horarios);
//                        msgAgendamento = "";
//                    }
//                }
//                break;
//            }
//            case 2: {
//                PessoaEnderecoDB db = new PessoaEnderecoDBToplink();
//                agendamento = a;
//                fisica = a.getPessoaEmpresa().getFisica();
//                enderecoFisica = db.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 1);
//                juridica = a.getPessoaEmpresa().getJuridica();
//                profissao = a.getPessoaEmpresa().getFuncao();
//                pessoaEmpresa = agendamento.getPessoaEmpresa();
//                renderCancelarHorario = true;
//                renderCancelar = false;
//                for (int i = 0; i < getListaDemissao().size(); i++) {
//                    if (Integer.parseInt(getListaDemissao().get(i).getDescription()) == agendamento.getDemissao().getId()) {
//                        idMotivoDemissao = (Integer) getListaDemissao().get(i).getValue();
//                        break;
//                    }
//                }
//                tipoAviso = String.valueOf(pessoaEmpresa.isAvisoTrabalhado());
//                break;
//            }
//            case 3: {
//                renderAgendamento = true;
//                renderConcluir = false;
//                break;
//            }
//            case 4: {
//                renderAgendamento = true;
//                renderConcluir = false;
//                break;
//            }
//            case 5: {
//                renderAgendamento = true;
//                renderConcluir = false;
//                break;
//            }
//            case 6: {
//                if (getData() == null) {
//                    msgAgendamento = "Selecione uma data para Agendamento!";
//                    renderAgendamento = true;
//                    renderConcluir = false;
//                } else {
//                    renderCancelarHorario = false;
//                    renderCancelar = true;
//                    if (pesquisarFeriado()) {
//                        msgAgendamento = "Esta data esta cadastrada como Feriado!";
//                        renderAgendamento = true;
//                        renderConcluir = false;
//                    } else {
//                        agendamento.setData(DataHoje.converteData(getData()));
//                        agendamento.setHorarios(a.getHorarios());
//                        msgAgendamento = "";
//                    }
//                }
//                break;
//            }
//        }
//        return "agendamento";
//    }
//
//    public String salvar() {
//        styleDestaque = "";
//        msgConfirma = "";
//        if (fisica.getPessoa().getNome().equals("") || fisica.getPessoa().getNome() == null) {
//            msgConfirma = "Digite o nome do Funcionário!";
//            return null;
//        }
//        if (!strContribuinte.isEmpty()) {
//            msgConfirma = "Não é permitido agendar para uma empresa não contribuinte!";
//            return null;
//        }
//        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
//        FisicaDB dbFis = new FisicaDBToplink();
//        List listDocumento;
//        imprimirPro = false;
//        DataHoje dataH = new DataHoje();
//        Demissao demissao = (Demissao) sv.find("Demissao", Integer.parseInt(((SelectItem) getListaDemissao().get(idMotivoDemissao)).getDescription()));
//        if (!pessoaEmpresa.getDemissao().isEmpty() && pessoaEmpresa.getDemissao() != null) {
//            if (demissao.getId() == 1) {
//                if (DataHoje.converteDataParaInteger(pessoaEmpresa.getDemissao())
//                        > DataHoje.converteDataParaInteger(dataH.incrementarMeses(1, DataHoje.data()))) {
//                    msgConfirma = "Por " + demissao.getDescricao() + " data de Demissão não pode ser maior que 30 dias!";
//                    return null;
//                }
//            } else if (demissao.getId() == 2) {
//                if (DataHoje.converteDataParaInteger(pessoaEmpresa.getDemissao())
//                        > DataHoje.converteDataParaInteger(dataH.incrementarMeses(3, DataHoje.data()))) {
//                    msgConfirma = "Por " + demissao.getDescricao() + " data de Demissão não pode ser maior que 90 dias!";
//                    return null;
//                }
//            } else if (demissao.getId() == 3) {
//                if (DataHoje.converteDataParaInteger(pessoaEmpresa.getDemissao())
//                        > DataHoje.converteDataParaInteger(dataH.incrementarDias(10, DataHoje.data()))) {
//                    msgConfirma = "Por " + demissao.getDescricao() + " data de Demissão não pode ser maior que 10 dias!";
//                    return null;
//                }
//            }
//        } else {
//            msgConfirma = "Data de Demissão é obrigatória!";
//            return null;
//        }
//
//        if (!pessoaEmpresa.getAdmissao().isEmpty() && pessoaEmpresa.getAdmissao() != null) {
//            if (DataHoje.converteDataParaInteger(pessoaEmpresa.getAdmissao())
//                    > DataHoje.converteDataParaInteger(pessoaEmpresa.getDemissao())) {
//                msgConfirma = "Data de Admissão é maior que data de Demissão!";
//                return null;
//            }
//        } else {
//            msgConfirma = "Data de Admissão é obrigatória!";
//            return null;
//        }
//
//        // SALVAR FISICA -----------------------------------------------
//        sv.abrirTransacao();
//        fisica.getPessoa().setTipoDocumento((TipoDocumento) sv.find("TipoDocumento", 1));
//        if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(fisica.getPessoa().getDocumento()))) {
//            sv.desfazerTransacao();
//            msgConfirma = "Documento Inválido!";
//            return null;
//        }
//
//        if (fisica.getId() == -1) {
//            if (!dbFis.pesquisaFisicaPorNomeNascRG(fisica.getPessoa().getNome(),
//                    fisica.getDtNascimento(),
//                    fisica.getRg()).isEmpty()) {
//                msgConfirma = "Esta pessoa já esta cadastrada!";
//                sv.desfazerTransacao();
//                return null;
//            }
//            listDocumento = dbFis.pesquisaFisicaPorDoc(fisica.getPessoa().getDocumento());
//            if (!listDocumento.isEmpty()) {
//                sv.desfazerTransacao();
//                msgConfirma = "Documento já existente!";
//                return null;
//            }
//
//            if (sv.inserirObjeto(fisica.getPessoa())) {
//                sv.inserirObjeto(fisica);
//            } else {
//                sv.desfazerTransacao();
//                msgConfirma = "Erro ao Inserir pessoa!";
//                return null;
//            }
//        } else {
//            listDocumento = dbFis.pesquisaFisicaPorDoc(fisica.getPessoa().getDocumento());
//            for (Object listDocumento1 : listDocumento) {
//                if (!listDocumento.isEmpty() && ((Fisica) listDocumento1).getId() != fisica.getId()) {
//                    sv.desfazerTransacao();
//                    msgConfirma = "Documento já existente!";
//                    return null;
//                }
//            }
//            List<Fisica> fisi = dbFis.pesquisaFisicaPorNomeNascRG(fisica.getPessoa().getNome(),
//                    fisica.getDtNascimento(),
//                    fisica.getRg());
//            if (!fisi.isEmpty()) {
//                for (Fisica fisi1 : fisi) {
//                    if (fisi1.getId() != fisica.getId()) {
//                        sv.desfazerTransacao();
//                        msgConfirma = "Esta pessoa já esta cadastrada!";
//                        return null;
//                    }
//                }
//            }
//            if (sv.alterarObjeto(fisica.getPessoa())) {
//                if (sv.alterarObjeto(fisica)) {
//                } else {
//                    sv.desfazerTransacao();
//                    msgConfirma = "Erro ao alterar Física!";
//                    return null;
//                }
//            } else {
//                sv.desfazerTransacao();
//                msgConfirma = "Erro ao alterar Pessoa!";
//                return null;
//            }
//        }
//        HomologacaoDB dba = new HomologacaoDBToplink();
//
//        if (dba.pesquisaFisicaAgendada(fisica.getId()) != null && agendamento.getId() == -1) {
//            sv.desfazerTransacao();
//            msgConfirma = "Pessoa já foi agendada!";
//            return null;
//        }
//        boolean isOposicao = false;
//        AtendimentoDB dbat = new AtendimentoDBTopLink();
//        if (dbat.pessoaOposicao(fisica.getPessoa().getDocumento())) {
//            isOposicao = true;
//            //return null;
//        }
//
//        PessoaEnderecoDB dbp = new PessoaEnderecoDBToplink();
//
//        if (enderecoFisica.getId() == -1) {
//            if (enderecoFisica.getEndereco().getId() != -1) {
//                enderecoFisica.setPessoa(fisica.getPessoa());
//                PessoaEndereco pesEnd = enderecoFisica;
//                int ids[] = {1, 3, 4};
//                for (int i = 0; i < ids.length; i++) {
//                    pesEnd.setTipoEndereco((TipoEndereco) sv.pesquisaObjeto(ids[i], "TipoEndereco"));
//                    if (!sv.inserirObjeto(pesEnd)) {
//                        sv.desfazerTransacao();
//                        msgConfirma = "Erro ao Inserir endereço da pessoa!";
//                        return null;
//                    }
//                    pesEnd = new PessoaEndereco();
//                    pesEnd.setComplemento(enderecoFisica.getComplemento());
//                    pesEnd.setEndereco(enderecoFisica.getEndereco());
//                    pesEnd.setNumero(enderecoFisica.getNumero());
//                    pesEnd.setPessoa(enderecoFisica.getPessoa());
//                }
//            }
//        } else {
//            List<PessoaEndereco> ends = dbp.pesquisaEndPorPessoa(fisica.getPessoa().getId());
//            for (PessoaEndereco end : ends) {
//                end.setComplemento(enderecoFisica.getComplemento());
//                end.setEndereco(enderecoFisica.getEndereco());
//                end.setNumero(enderecoFisica.getNumero());
//                end.setPessoa(enderecoFisica.getPessoa());
//                if (!sv.alterarObjeto(end)) {
//                    sv.desfazerTransacao();
//                    msgConfirma = "Erro ao atualizar endereço da pessoa!";
//                    return null;
//                }
//            }
//        }
//        // -------------------------------------------------------------
//        int idStatusI = Integer.parseInt(((SelectItem) getListaStatus().get(idStatus)).getDescription());
//        switch (idStatusI) {
//            case 1: {
//                pessoaEmpresa.setFisica(fisica);
//                pessoaEmpresa.setJuridica(juridica);
//                pessoaEmpresa.setFuncao(profissao);
//                pessoaEmpresa.setAvisoTrabalhado(Boolean.valueOf(tipoAviso));
//                agendamento.setDemissao(demissao);
//                agendamento.setHomologador(null);
//                agendamento.setPessoaEmpresa(pessoaEmpresa);
//                agendamento.setStatus((Status) sv.find("Status", 2));
//                break;
//            }
//            case 2: {
//                pessoaEmpresa.setFisica(fisica);
//                pessoaEmpresa.setJuridica(juridica);
//                pessoaEmpresa.setAvisoTrabalhado(Boolean.valueOf(tipoAviso));
//                pessoaEmpresa.setFuncao(profissao);
//                agendamento.setDemissao(demissao);
//                agendamento.setPessoaEmpresa(pessoaEmpresa);
//                break;
//            }
//            case 6: {
//                pessoaEmpresa.setFisica(fisica);
//                pessoaEmpresa.setJuridica(juridica);
//                pessoaEmpresa.setFuncao(profissao);
//                pessoaEmpresa.setAvisoTrabalhado(Boolean.valueOf(tipoAviso));
//                agendamento.setDemissao(demissao);
//                agendamento.setHomologador(null);
//                agendamento.setPessoaEmpresa(pessoaEmpresa);
//                agendamento.setStatus((Status) sv.find("Status", 2));
//                break;
//            }
//        }
//
//        if (pessoaEmpresa.getId() == -1) {
//            if (!sv.inserirObjeto(pessoaEmpresa)) {
//                sv.desfazerTransacao();
//                msgConfirma = "Erro ao inserir Pessoa Empresa!";
//                return null;
//            }
//        } else {
//            if (!sv.alterarObjeto(pessoaEmpresa)) {
//                sv.desfazerTransacao();
//                msgConfirma = "Erro ao alterar Pessoa Empresa!";
//                return null;
//            }
//        }
//        if (agendamento.getContato().isEmpty()) {
//            sv.desfazerTransacao();
//            msgConfirma = "Informar o nome do contato!";
//            return null;
//        }
//        if (agendamento.getId() == -1) {
//            if (idStatusI == 1) {
//                if (!dba.existeHorarioDisponivel(agendamento.getDtData(), agendamento.getHorarios())) {
//                    sv.desfazerTransacao();
//                    listaHorarioTransferencia.clear();
//                    msgConfirma = "Não existe mais disponibilidade para o horário agendado!";
//                    ocultarHorarioAlternativo = false;
//                    return null;
//                }
//            }
//            agendamento.setFilial(macFilial.getFilial());
//            agendamento.setAgendador((Usuario) GenericaSessao.getObject("sessaoUsuario")); // USUARIO SESSAO
//            agendamento.setRecepcao(null);
//            agendamento.setDtEmissao(DataHoje.dataHoje());
//            if (sv.inserirObjeto(agendamento)) {
//                sv.comitarTransacao();
//                msgConfirma = "Para imprimir Protocolo clique aqui! ";
//            } else {
//                agendamento.setId(-1);
//                msgConfirma = "Erro ao realizar este agendamento!";
//                sv.desfazerTransacao();
//            }
//        } else {
//            if (sv.alterarObjeto(agendamento)) {
//                sv.comitarTransacao();
//                if (isOposicao) {
//                    msgConfirma = "Agendamento atualizado com Sucesso! imprimir Protocolo clicando aqui! Pessoa cadastrada em oposição. ";
//                    styleDestaque = "color: red; font-size: 14pt; font-weight:bold";
//                } else {
//                    msgConfirma = "Agendamento atualizado com Sucesso! imprimir Protocolo clicando aqui! ";
//                    styleDestaque = "";
//                }
//            } else {
//                msgConfirma = "Erro ao atualizar agendamento!";
//                sv.desfazerTransacao();
//            }
//        }
//        strSalvar = "Atualizar";
//        renderCancelarHorario = true;
//        listaHorarioTransferencia.clear();
//        id_protocolo = agendamento.getId();
//        ocultarHorarioAlternativo = true;
//        imprimirPro = true;
//        return null;
//    }
//
//    public String salvarMais() {
//
//        return null;
//    }
//
//    public String cancelar() {
//        strEndereco = "";
//        renderAgendamento = true;
//        renderConcluir = false;
//        tipoAviso = "true";
//        msgConfirma = "";
//        msgAgendamento = "";
//        fisica = new Fisica();
//        cepEndereco = "";
//        listaEnderecos.clear();
//        agendamento = new Agendamento();
//        pessoaEmpresa = new PessoaEmpresa();
//        profissao = new Profissao();
//        juridica = new Juridica();
//        listaMovimento.clear();
//        enderecoFisica = new PessoaEndereco();
//        imprimirPro = false;
//        renderTransfere = false;
//        listaHorarioTransferencia.clear();
//        emailEmpresa = "";
//        return "agendamento";
//    }
//
//    public String cancelarHorario() {
//        PessoaEmpresaDB dbPesEmp = new PessoaEmpresaDBToplink();
//        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
//        agendamento.setStatus((Status) salvarAcumuladoDB.find("Status", 3));
//        salvarAcumuladoDB.abrirTransacao();
//        if (salvarAcumuladoDB.alterarObjeto(agendamento)) {
//            salvarAcumuladoDB.comitarTransacao();
//            msgConfirma = "Agendamento cancelado com sucesso";
//        } else {
//            salvarAcumuladoDB.desfazerTransacao();
//            msgConfirma = "";
//        }
//        pessoaEmpresa.setDtDemissao(null);
//        dbPesEmp.update(pessoaEmpresa);
//        strEndereco = "";
//        renderAgendamento = true;
//        renderConcluir = false;
//        renderCancelarHorario = false;
//        renderCancelar = true;
//        tipoAviso = "true";
//        msgConfirma = "";
//        msgAgendamento = "";
//        fisica = new Fisica();
//        agendamento = new Agendamento();
//        pessoaEmpresa = new PessoaEmpresa();
//        profissao = new Profissao();
//        juridica = new Juridica();
//        listaMovimento.clear();
//        enderecoFisica = new PessoaEndereco();
//        return "agendamento";
//    }
//
//    public void limpar() {
//        strEndereco = "";
//        renderAgendamento = true;
//        renderConcluir = false;
//        tipoAviso = "true";
//        msgAgendamento = "";
//        fisica = new Fisica();
//        pessoaEmpresa = new PessoaEmpresa();
//        agendamento = new Agendamento();
//        profissao = new Profissao();
//        juridica = new Juridica();
//        listaMovimento.clear();
//        protocolo = 0;
//        enderecoFisica = new PessoaEndereco();
//    }
//
//    public void limparMais() {
//        strEndereco = "";
//        renderAgendamento = true;
//        renderConcluir = false;
//        tipoAviso = "true";
//        msgAgendamento = "";
//        fisica = new Fisica();
//        pessoaEmpresa = new PessoaEmpresa();
//        agendamento = new Agendamento();
//        profissao = new Profissao();
//        protocolo = 0;
//        enderecoFisica = new PessoaEndereco();
//    }
//
//    public void pesquisarFuncionarioCPF() throws IOException {
//        msgConfirma = "";
//        styleDestaque = "";
//        if (!fisica.getPessoa().getDocumento().isEmpty() && !fisica.getPessoa().getDocumento().equals("___.___.___-__")) {
//
//            if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(fisica.getPessoa().getDocumento()))) {
//                msgConfirma = "Documento Inválido!";
//                return;
//            }
//
//            FisicaDB dbFis = new FisicaDBToplink();
//            HomologacaoDB db = new HomologacaoDBToplink();
//            PessoaEnderecoDB dbe = new PessoaEnderecoDBToplink();
//
//            String documento = fisica.getPessoa().getDocumento();
//
//            List<Fisica> listFisica = dbFis.pesquisaFisicaPorDocSemLike(documento);
//            List<Oposicao> listao = db.pesquisaFisicaOposicaoSemEmpresa(documento);
//            List<PessoaEmpresa> listape = db.pesquisaPessoaEmpresaPertencente(documento);
//
//            if (!listFisica.isEmpty()) {
//                Agendamento age = db.pesquisaFisicaAgendada(listFisica.get(0).getId());
//                if (age != null) {
//                    msgConfirma = "CPF já foi agendado:" + age.getData() + " às " + age.getHorarios().getHora() + " h(s) ";
//                    return;
//                }
//
//                if (!listape.isEmpty() && listape.get(0).getDtDemissao() != null) {
//                    listape.clear();
//                }
//            }
//
//            // SEM PESSOA FISICA E SEM OPOSICAO
//            if (listFisica.isEmpty() && listao.isEmpty()) {
//                //FacesContext.getCurrentInstance().getExternalContext().redirect("/Sindical/agendamento.jsf");
//                msgConfirma = "CPF verificado com sucesso";
//                // SEM PESSOA FISICA E COM OPOSICAO    
//            } else if (listFisica.isEmpty() && !listao.isEmpty()) {
//                msgConfirma = "CPF cadastrado em oposição em " + listao.get(0).getEmissao();
//                styleDestaque = "color: red; font-size: 14pt; font-weight:bold";
//                fisica.getPessoa().setNome(listao.get(0).getOposicaoPessoa().getNome());
//                fisica.setRg(listao.get(0).getOposicaoPessoa().getRg());
//                fisica.setSexo(listao.get(0).getOposicaoPessoa().getSexo());
//                fisica.getPessoa().setDocumento(documento);
//                juridica = listao.get(0).getJuridica();
//                // COM FISICA, COM PESSOA EMPRESA E SEM OPOSICAO    
//            } else if (!listFisica.isEmpty() && !listape.isEmpty() && listao.isEmpty()) {
//                msgConfirma = "CPF verificado com sucesso";
//                pessoaEmpresa = listape.get(0);
//                fisica = pessoaEmpresa.getFisica();
//                profissao = pessoaEmpresa.getFuncao();
//                GenericaSessao.put("juridicaPesquisa", pessoaEmpresa.getJuridica());
//                disabledEmpresa = true;
//                enderecoFisica = dbe.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 3);
//                protocolo = 0;
//                //FacesContext.getCurrentInstance().getExternalContext().redirect("/Sindical/agendamento.jsf");
//                // COM FISICA, SEM PESSOA EMPRESA E SEM OPOSICAO    
//            } else if (!listFisica.isEmpty() && listape.isEmpty() && listao.isEmpty()) {
//                msgConfirma = "CPF verificado com sucesso";
//                fisica = listFisica.get(0);
//                pessoaEmpresa = new PessoaEmpresa();
//                juridica = new Juridica();
//                disabledEmpresa = false;
//                enderecoFisica = dbe.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 3);
//                //FacesContext.getCurrentInstance().getExternalContext().redirect("/Sindical/agendamento.jsf");
//                // COM FISICA, COM PESSOA EMPRESA COM OPOSICAO
//            } else if (!listFisica.isEmpty() && !listape.isEmpty() && !listao.isEmpty()) {
//                msgConfirma = "CPF cadastrado em oposição em " + listao.get(0).getEmissao();
//                styleDestaque = "color: red; font-size: 14pt; font-weight:bold";
//                pessoaEmpresa = listape.get(0);
//                fisica = pessoaEmpresa.getFisica();
//                profissao = pessoaEmpresa.getFuncao();
//                disabledEmpresa = true;
//                enderecoFisica = dbe.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 3);
//                protocolo = 0;
//                // FUNCIONARIO JA FOI AGENDADO
////                if (db.pesquisaFisicaAgendada(fisica.getId()) != null){
////
////                }
//                // EMPRESA É A MESMA DA OPOSICAO
//                if (pessoaEmpresa.getJuridica().getId() == listao.get(0).getJuridica().getId()) {
//                    juridica = pessoaEmpresa.getJuridica();
//                } else {
//                    juridica = listao.get(0).getJuridica();
//                    // FUNCIONARIO SEM DATA DE DEMISSAO
////                    if (pessoaEmpresa.getDemissao().isEmpty()){
////                        if ((DataHoje.converteDataParaInteger(pessoaEmpresa.getDemissao()) < DataHoje.converteDataParaInteger(listao.get(0).getEmissao()) )){
////
////                        }
////                    }
//                }
//                GenericaSessao.put("juridicaPesquisa", juridica);
//                // COM FISICA, SEM PESSOA EMPRESA COM OPOSICAO
//            } else if (!listFisica.isEmpty() && listape.isEmpty() && !listao.isEmpty()) {
//                msgConfirma = "CPF cadastrado em oposição em " + listao.get(0).getEmissao();
//                styleDestaque = "color: red; font-size: 14pt; font-weight:bold";
//
//                fisica = listFisica.get(0);
//                juridica = listao.get(0).getJuridica();
//                disabledEmpresa = false;
//                enderecoFisica = dbe.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 3);
//            }
//        } else {
//            msgConfirma = "Informar CPF!";
//        }
//    }
//
//    public String pesquisarProtocolo() {
//        HomologacaoDB db = new HomologacaoDBToplink();
//        if (protocolo > 0) {
//            Agendamento age = new Agendamento();
//            age.setData(agendamento.getData());
//            age.setHorarios(agendamento.getHorarios());
//            Agendamento age2 = db.pesquisaProtocolo(protocolo);
//            if (age2 != null) {
//                agendamento = age2;
//                agendamento.setData(age.getData());
//                agendamento.setHorarios(age.getHorarios());
//                agendamento.setAgendador((Usuario) GenericaSessao.getObject("sessaoUsuario"));
//                agendamento.setFilial(macFilial.getFilial());
//                fisica = agendamento.getPessoaEmpresa().getFisica();
//                PessoaEnderecoDB dbe = new PessoaEnderecoDBToplink();
//                enderecoFisica = dbe.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 3);
//                pessoaEmpresa = agendamento.getPessoaEmpresa();
//                profissao = pessoaEmpresa.getFuncao();
//                GenericaSessao.put("juridicaPesquisa", pessoaEmpresa.getJuridica());
//            } else {
//                if (agendamento.getId() != -1) {
//                    limpar();
//                }
//            }
//        } else {
//            if (agendamento.getId() != -1) {
//                limpar();
//            }
//        }
//        return "agendamento";
//    }
//
//    public String enviarEmailProtocolo() {
//        ProtocoloAgendamento protocoloAgendamento = new ProtocoloAgendamento();
//        protocoloAgendamento.enviar(agendamento);
//        return null;
//    }
//
//    public void pesquisaEndereco() {
//        if (!cepEndereco.equals("")) {
//            listaEnderecos.clear();
//        }
//    }
//
//    public void limparPesquisaEndereco() {
//        listaEnderecos.clear();
//    }
//
//    public void editarEndereco(Endereco e) {
//        enderecoFisica.setEndereco(e);
//    }
//
//    public int getIdStatus() {
//        return idStatus;
//    }
//
//    public void setIdStatus(int idStatus) {
//        this.idStatus = idStatus;
//    }
//
//    public void refreshForm() {
//    }
//
//    public boolean isRenderAgendamento() {
//        return renderAgendamento;
//    }
//
//    public void setRenderAgendamento(boolean renderAgendamento) {
//        this.renderAgendamento = renderAgendamento;
//    }
//
//    public boolean isRenderConcluir() {
//        return renderConcluir;
//    }
//
//    public void setRenderConcluir(boolean renderConcluir) {
//        this.renderConcluir = renderConcluir;
//    }
//
//    public Juridica getJuridica() {
//        if (GenericaSessao.exists("juridicaPesquisa")) {
//            juridica = (Juridica) GenericaSessao.getObject("juridicaPesquisa", true);
//            listaMovimento.clear();
//            GenericaSessao.put("pessoaPesquisa", juridica.getPessoa());
//            if (juridica.getContabilidade() != null && agendamento.getId() == -1) {
//                agendamento.setTelefone(juridica.getContabilidade().getPessoa().getTelefone1());
//            }
//        }
//        return juridica;
//    }
//
//    public void setJuridica(Juridica juridica) {
//        this.juridica = juridica;
//    }
//
//    public PessoaEndereco getEnderecoEmpresa() {
//        return enderecoEmpresa;
//    }
//
//    public void setEnderecoEmpresa(PessoaEndereco enderecoEmpresa) {
//        this.enderecoEmpresa = enderecoEmpresa;
//    }
//
//    public String getStrEndereco() {
//        PessoaEnderecoDB pessoaEnderecoDB = new PessoaEnderecoDBToplink();
//        if (juridica.getId() != -1) {
//            enderecoEmpresa = pessoaEnderecoDB.pesquisaEndPorPessoaTipo(juridica.getPessoa().getId(), 5);
//            if (enderecoEmpresa.getId() != -1) {
//                String strCompl;
//                if (enderecoEmpresa.getComplemento().equals("")) {
//                    strCompl = " ";
//                } else {
//                    strCompl = " ( " + enderecoEmpresa.getComplemento() + " ) ";
//                }
//
//                strEndereco = enderecoEmpresa.getEndereco().getLogradouro().getDescricao() + " "
//                        + enderecoEmpresa.getEndereco().getDescricaoEndereco().getDescricao() + ", " + enderecoEmpresa.getNumero() + " " + enderecoEmpresa.getEndereco().getBairro().getDescricao() + ","
//                        + strCompl + enderecoEmpresa.getEndereco().getCidade().getCidade() + " - " + enderecoEmpresa.getEndereco().getCidade().getUf() + " - " + AnaliseString.mascaraCep(enderecoEmpresa.getEndereco().getCep());
//            } else {
//                strEndereco = "";
//            }
//        } else {
//            enderecoEmpresa = new PessoaEndereco();
//            strEndereco = "";
//        }
//        return strEndereco;
//    }
//
//    public void setStrEndereco(String strEndereco) {
//        this.strEndereco = strEndereco;
//    }
//
//    public Date getData() {
//        return data;
//    }
//
//    public void setData(Date data) {
//        this.data = data;
//    }
//
//    public String getMsgAgendamento() {
//        if (GenericaSessao.exists("acessoFilial")) {
//            macFilial = (MacFilial) GenericaSessao.getObject("acessoFilial");
//        } else {
//            msgAgendamento = "Não existe filial definida!";
//        }
//        return msgAgendamento;
//    }
//
//    public void setMsgAgendamento(String msgAgendamento) {
//        this.msgAgendamento = msgAgendamento;
//    }
//
//    public Agendamento getAgendamento() {
//        return agendamento;
//    }
//
//    public void setAgendamento(Agendamento agendamento) {
//        this.agendamento = agendamento;
//    }
//
//    public Fisica getFisica() {
//        return fisica;
//    }
//
//    public void setFisica(Fisica fisica) {
//        this.fisica = fisica;
//    }
//
//    public int getIdMotivoDemissao() {
//        return idMotivoDemissao;
//    }
//
//    public void setIdMotivoDemissao(int idMotivoDemissao) {
//        this.idMotivoDemissao = idMotivoDemissao;
//    }
//
//    public String getTipoAviso() {
//        return tipoAviso;
//    }
//
//    public void setTipoAviso(String tipoAviso) {
//        this.tipoAviso = tipoAviso;
//    }
//
//    public String getMsgConfirma() {
//        return msgConfirma;
//    }
//
//    public void setMsgConfirma(String msgConfirma) {
//        this.msgConfirma = msgConfirma;
//    }
//
//    public PessoaEmpresa getPessoaEmpresa() {
//        return pessoaEmpresa;
//    }
//
//    public void setPessoaEmpresa(PessoaEmpresa pessoaEmpresa) {
//        this.pessoaEmpresa = pessoaEmpresa;
//    }
//
//    public boolean isRenderCancelarHorario() {
//        return renderCancelarHorario;
//    }
//
//    public void setRenderCancelarHorario(boolean renderCancelarHorario) {
//        this.renderCancelarHorario = renderCancelarHorario;
//    }
//
//    public boolean isRenderCancelar() {
//        return renderCancelar;
//    }
//
//    public void setRenderCancelar(boolean renderCancelar) {
//        this.renderCancelar = renderCancelar;
//    }
//
//    public boolean isDisabledEmpresa() {
//        return disabledEmpresa;
//    }
//
//    public void setDisabledEmpresa(boolean disabledEmpresa) {
//        this.disabledEmpresa = disabledEmpresa;
//    }
//
//    public String getStrSalvar() {
//        return strSalvar;
//    }
//
//    public void setStrSalvar(String strSalvar) {
//        this.strSalvar = strSalvar;
//    }
//
//    public String getStatusEmpresa() {
//        HomologacaoDB db = new HomologacaoDBToplink();
//        if (juridica.getId() != -1 && listaMovimento.isEmpty()) {
//            listaMovimento = db.pesquisaPessoaDebito(juridica.getPessoa().getId(), DataHoje.data());
//        }
//        if (!listaMovimento.isEmpty()) {
//            statusEmpresa = "EM DÉBITO";
//        } else {
//            statusEmpresa = "REGULAR";
//        }
//        return statusEmpresa;
//    }
//
//    public void setStatusEmpresa(String statusEmpresa) {
//        this.statusEmpresa = statusEmpresa;
//    }
//
//    public int getProtocolo() {
//        return protocolo;
//    }
//
//    public void setProtocolo(int protocolo) {
//        this.protocolo = protocolo;
//    }
//
//    public boolean isDisabledProtocolo() {
//        return disabledProtocolo;
//    }
//
//    public void setDisabledProtocolo(boolean disabledProtocolo) {
//        this.disabledProtocolo = disabledProtocolo;
//    }
//
//    public String getCepEndereco() {
//        return cepEndereco;
//    }
//
//    public void setCepEndereco(String cepEndereco) {
//        this.cepEndereco = cepEndereco;
//    }
//
//    public List getListaEnderecos() {
//        if (listaEnderecos.isEmpty()) {
//            if (!cepEndereco.equals("")) {
//                EnderecoDB db = new EnderecoDBToplink();
//                listaEnderecos = db.pesquisaEnderecoCep(cepEndereco);
//            }
//        }
//        return listaEnderecos;
//    }
//
//    public void setListaEnderecos(List listaEnderecos) {
//        this.listaEnderecos = listaEnderecos;
//    }
//
//    public PessoaEndereco getEnderecoFisica() {
//        if (enderecoFisica == null) {
//            enderecoFisica = new PessoaEndereco();
//        }
//        return enderecoFisica;
//    }
//
//    public void setEnderecoFisica(PessoaEndereco enderecoFisica) {
//        this.enderecoFisica = enderecoFisica;
//    }
//
//    public int getIdIndex() {
//        return idIndex;
//    }
//
//    public void setIdIndex(int idIndex) {
//        this.idIndex = idIndex;
//    }
//
//    public int getIdIndexEndereco() {
//        return idIndexEndereco;
//    }
//
//    public void setIdIndexEndereco(int idIndexEndereco) {
//        this.idIndexEndereco = idIndexEndereco;
//    }
//
//    public String extratoTela() {
//        GenericaSessao.put("pessoaPesquisa", juridica.getPessoa());
//        return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).extratoTela();
//    }
//
//    public String getStrContribuinte() {
//        if (juridica.getId() != -1) {
//            JuridicaDB db = new JuridicaDBToplink();
//            List<ArrayList> listax = db.listaJuridicaContribuinte(juridica.getId());
//            if (!listax.isEmpty()) {
//                if (((List) (listax.get(0))).get(11) != null) {
//                    return strContribuinte = "Empresa Inativa";
//                } else {
//                    return strContribuinte = "";
//                }
//            }
//        }
//        return strContribuinte = "Empresa não contribuinte, não poderá efetuar um agendamento!";
//    }
//
//    public void setStrContribuinte(String strContribuinte) {
//        this.strContribuinte = strContribuinte;
//    }
//
//    public boolean isRenderTransfere() {
//        return renderTransfere;
//    }
//
//    public void setRenderTransfere(boolean renderTransfere) {
//        this.renderTransfere = renderTransfere;
//    }
//
//    public int getIdHorarioTransferencia() {
//        return idHorarioTransferencia;
//    }
//
//    public void setIdHorarioTransferencia(int idHorarioTransferencia) {
//        this.idHorarioTransferencia = idHorarioTransferencia;
//    }
//
//    public Date getDataTransferencia() {
//        return dataTransferencia;
//    }
//
//    public void setDataTransferencia(Date dataTransferencia) {
//        this.dataTransferencia = dataTransferencia;
//    }
//
//    public String getEnviarPara() {
//        return enviarPara;
//    }
//
//    public void setEnviarPara(String enviarPara) {
//        this.enviarPara = enviarPara;
//    }
//
//    public PessoaEndereco getEnderecoFilial() {
//        PessoaEnderecoDB pessoaEnderecoDB = new PessoaEnderecoDBToplink();
//        if (enderecoFilial.getId() == -1) {
//            enderecoFilial = pessoaEnderecoDB.pesquisaEndPorPessoaTipo(macFilial.getFilial().getFilial().getPessoa().getId(), 2);
//        }
//        return enderecoFilial;
//    }
//
//    public void setEnderecoFilial(PessoaEndereco enderecoFilial) {
//        this.enderecoFilial = enderecoFilial;
//    }
//
//    public boolean isImprimirPro() {
//        return imprimirPro;
//    }
//
//    public void setImprimirPro(boolean imprimirPro) {
//        this.imprimirPro = imprimirPro;
//    }
//
//    public boolean isRenderedDisponivel() {
//        return renderedDisponivel;
//    }
//
//    public void setRenderedDisponivel(boolean renderedDisponivel) {
//        this.renderedDisponivel = renderedDisponivel;
//    }
//
//    public void mensagemAgendamento() {
//        msgAgendamento = "";
//        FeriadosDB feriadosDB = new FeriadosDBToplink();
//        if (macFilial.getFilial().getFilial().getId() != -1) {
//            List<Feriados> feriados = feriadosDB.pesquisarPorDataFilialEData(DataHoje.converteData(data), macFilial.getFilial());
//            if (!feriados.isEmpty()) {
//                msgAgendamento = "Nesta data existem feriados/agenda: ";
//                GenericaMensagem.warn("Sistema", msgAgendamento);
//                for (int i = 0; i < feriados.size(); i++) {
//                    if (i == 0) {
//                        msgAgendamento += feriados.get(i).getNome();
//                    } else {
//                        msgAgendamento += ", " + feriados.get(i).getNome();
//                    }
//                }
//            }
//        }
//    }
//
//    public int getIdHorarioAlternativo() {
//        return idHorarioAlternativo;
//    }
//
//    public void setIdHorarioAlternativo(int idHorarioAlternativo) {
//        this.idHorarioAlternativo = idHorarioAlternativo;
//    }
//
//    public void adicionarHorarioAlternativo() {
//        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
//        agendamento.setHorarios((Horarios) salvarAcumuladoDB.find("Horarios", Integer.parseInt(listaHorarioTransferencia.get(idHorarioAlternativo).getDescription())));
//        listaHorarioTransferencia.clear();
//        setOcultarHorarioAlternativo(true);
//    }
//
//    public boolean isOcultarHorarioAlternativo() {
//        return ocultarHorarioAlternativo;
//    }
//
//    public void setOcultarHorarioAlternativo(boolean ocultarHorarioAlternativo) {
//        this.ocultarHorarioAlternativo = ocultarHorarioAlternativo;
//    }
//
//    // Verifica os agendamentos que não foram atendidos no menu principal;
//    public void verificaNaoAtendidos() {
//        HomologacaoDB homologacaoDB = new HomologacaoDBToplink();
//        homologacaoDB.verificaNaoAtendidosSegRegistroAgendamento();
//    }
//
//    /**
//     * -- STATUS 1 - DISPONIVEL; 2 - AGENDADO; 3 - CANCELADO; 4 - HOMOLOGADO; 5
//     * - ATENDIMENTO; 6 - ENCAIXE; 7 - NÃO COMPARACEU
//     *
//     * @return
//     */
//    public List<ListaAgendamento> getListaHorarios() {
//        listaGrid = new ArrayList();
//        listaHorarios.clear();
//        if (macFilial == null) {
//            return new ArrayList();
//        }
//        int idNrStatus = Integer.parseInt(((SelectItem) getListaStatus().get(idStatus)).getDescription());
//        int diaDaSemana = DataHoje.diaDaSemana(data);
//        disabledProtocolo = idNrStatus != 1 && idNrStatus != 6;
//        renderedDisponivel = idNrStatus != 2 && idNrStatus != 3 && idNrStatus != 4 && idNrStatus != 5 && idNrStatus != 6 && idNrStatus != 7;
//        HomologacaoDB homologacaoDB = new HomologacaoDBToplink();
//        List<Agendamento> agendamentos;
//        List<Horarios> horarios;
//        if (idNrStatus == 1 || idNrStatus == 6) {
//            horarios = homologacaoDB.pesquisaTodosHorariosDisponiveis(macFilial.getFilial().getId(), diaDaSemana);
//            for (int j = 0; j < horarios.size(); j++) {
//                ListaAgendamento listaAgendamento = new ListaAgendamento();
//                if (idNrStatus == 1) {
//                    int quantidade = homologacaoDB.pesquisaQntdDisponivel(macFilial.getFilial().getId(), horarios.get(j), getData());
//                    if (quantidade == -1) {
//                        msgAgendamento = "Erro ao pesquisar horários disponíveis!";
//                        listaHorarios.clear();
//                        break;
//                    }
//                    if (quantidade > 0) {
//                        listaAgendamento.setQuantidade(quantidade);
//                        listaAgendamento.getAgendamento().setHorarios(horarios.get(j));
//                        listaHorarios.add(listaAgendamento);
//                    }
//                } else {
//                    listaAgendamento.getAgendamento().setHorarios(horarios.get(j));
//                    listaHorarios.add(listaAgendamento);
//                }
//            }
//            strSalvar = "Agendar";
//        } else {
//            agendamentos = homologacaoDB.pesquisaAgendamento(idNrStatus, macFilial.getFilial().getId(), getData(), null, 0, 0, 0);
//            for (Agendamento agendamento1 : agendamentos) {
//                ListaAgendamento listaAgendamento = new ListaAgendamento();
//                Usuario u = new Usuario();
//                listaAgendamento.setAgendamento(agendamento1);
//                if (agendamento1.getAgendador() == null) {
//                    listaAgendamento.setUsuarioAgendador("** Web User **");
//                } else {
//                    listaAgendamento.setUsuarioAgendador(agendamento1.getAgendador().getPessoa().getNome());
//                }
//                listaHorarios.add(listaAgendamento);
//            }
//            strSalvar = "Atualizar";
//        }
//        return listaHorarios;
//    }
//
//    public void setListaHorarios(List<ListaAgendamento> listaHorarios) {
//        this.listaHorarios = listaHorarios;
//    }
//
//    public String getEmailEmpresa() {
//        return emailEmpresa;
//    }
//
//    public void setEmailEmpresa(String emailEmpresa) {
//        this.emailEmpresa = emailEmpresa;
//    }
//
//    public String getStyleDestaque() {
//        return styleDestaque;
//    }
//
//    public void setStyleDestaque(String styleDestaque) {
//        this.styleDestaque = styleDestaque;
//    }
//}
