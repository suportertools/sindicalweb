package br.com.rtools.homologacao.beans;

import br.com.rtools.pessoa.beans.PesquisarProfissaoBean;
import br.com.rtools.arrecadacao.Oposicao;
import br.com.rtools.atendimento.db.AtendimentoDB;
import br.com.rtools.atendimento.db.AtendimentoDBTopLink;
import br.com.rtools.endereco.Endereco;
import br.com.rtools.endereco.dao.EnderecoDao;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.homologacao.Agendamento;
import br.com.rtools.homologacao.ConfiguracaoHomologacao;
import br.com.rtools.homologacao.Demissao;
import br.com.rtools.homologacao.Feriados;
import br.com.rtools.homologacao.Horarios;
import br.com.rtools.homologacao.Status;
import br.com.rtools.homologacao.dao.FeriadosDao;
import br.com.rtools.homologacao.db.*;
import br.com.rtools.movimento.ImprimirBoleto;
import br.com.rtools.pessoa.*;
import br.com.rtools.pessoa.db.*;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.utilitarios.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.primefaces.context.RequestContext;

@ManagedBean
@SessionScoped
public class WebAgendamentoContribuinteBean extends PesquisarProfissaoBean implements Serializable {

    //private String msgAgendamento = "";
    private String strEndereco = "";
    private String statusEmpresa = "REGULAR";
    private Date data = DataHoje.converte(new DataHoje().incrementarDias(1, DataHoje.data()));
    private int idStatus = 0;
    private int idMotivoDemissao = 0;
    private List listaEmDebito = new ArrayList();
    private Agendamento agendamento = new Agendamento();
    private Agendamento agendamentoProtocolo = new Agendamento();
    private Juridica juridica;
    private FilialCidade sindicatoFilial = new FilialCidade();
    private PessoaEndereco enderecoFilial = new PessoaEndereco();
    private Fisica fisica = new Fisica();
    private PessoaEndereco enderecoEmpresa = new PessoaEndereco();
    private PessoaEmpresa pessoaEmpresa = new PessoaEmpresa();
    private PessoaEndereco enderecoFisica = new PessoaEndereco();
    private String cepEndereco = "";
    private List listaEnderecos = new ArrayList();
    private boolean readonlyFisica = false;
    private boolean readonlyEndereco = false;
    private String strContribuinte = "";
    private Registro registro = new Registro();

    private final List<DataObject> listaHorarios = new ArrayList<DataObject>();
    private final List<SelectItem> listaStatus = new ArrayList<SelectItem>();
    private final List<SelectItem> listaMotivoDemissao = new ArrayList<SelectItem>();
    private String tipoTelefone = "telefone";
    private ConfiguracaoHomologacao configuracaoHomologacao = new ConfiguracaoHomologacao();

    private String tipoAviso = null;

    public WebAgendamentoContribuinteBean() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuarioAcessoWeb") != null) {
            JuridicaDB db = new JuridicaDBToplink();
            FilialCidadeDB dbf = new FilialCidadeDBToplink();
            PessoaEnderecoDB dbp = new PessoaEnderecoDBToplink();

            juridica = db.pesquisaJuridicaPorPessoa(((Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuarioAcessoWeb")).getId());
            enderecoEmpresa = dbp.pesquisaEndPorPessoaTipo(juridica.getPessoa().getId(), 5);

            if (enderecoEmpresa.getId() != -1) {
                sindicatoFilial = dbf.pesquisaFilialPorCidade(enderecoEmpresa.getEndereco().getCidade().getId());
            }
            Dao dao = new Dao();
            registro = (Registro) dao.find(new Registro(), 1);
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

    public List<SelectItem> getListaStatus() {
        if (listaStatus.isEmpty()) {
            Dao dao = new Dao();
            List<Status> select = new ArrayList();
            select.add((Status) dao.find(new Status(), 1));
            select.add((Status) dao.find(new Status(), 2));
            for (int i = 0; i < select.size(); i++) {
                listaStatus.add(new SelectItem(i, select.get(i).getDescricao(), Integer.toString(select.get(i).getId())));
            }
        }
        return listaStatus;
    }

    public List<SelectItem> getListaMotivoDemissao() {
        if (listaMotivoDemissao.isEmpty()) {
            Dao dao = new Dao();
            List<Demissao> select = dao.list(new Demissao());
            listaMotivoDemissao.add(new SelectItem(0, "", "0"));
            for (int i = 0; i < select.size(); i++) {
                listaMotivoDemissao.add(new SelectItem(i + 1, select.get(i).getDescricao(), Integer.toString(select.get(i).getId())));
            }
        }
        return listaMotivoDemissao;
    }

    public synchronized List<DataObject> getListaHorarios() {
        if (listaHorarios.isEmpty()) {
            List<Agendamento> ag;
            List<Horarios> horario;
            HomologacaoDB db = new HomologacaoDBToplink();
            String agendador;
            String homologador;
            DataObject dtObj;
            switch (Integer.parseInt(((SelectItem) getListaStatus().get(idStatus)).getDescription())) {
                //STATUS DISPONIVEL ----------------------------------------------------------------------------------------------
                case 1: {
                    // TIRAR VALOR 1 DA PESQUISA PELO MAC DA FILIAL
                    int idDiaSemana = DataHoje.diaDaSemana(data);
                    horario = db.pesquisaTodosHorariosDisponiveis(sindicatoFilial.getFilial().getId(), idDiaSemana, true);
                    int qnt;
                    for (int i = 0; i < horario.size(); i++) {
                        qnt = db.pesquisaQntdDisponivel(getSindicatoFilial().getFilial().getId(), horario.get(i), data);
                        if (qnt == -1) {
                            //msgAgendamento = "Erro ao pesquisar horários disponíveis!";
                            GenericaMensagem.error("Erro", "Não foi possível pesquisar horários disponíveis!");
                            listaHorarios.clear();
                            break;
                        }
                        if (qnt > 0) {
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
                            listaHorarios.add(dtObj);
                        }
                    }
                    break;
                }
                // STATUS AGENDADO -----------------------------------------------------------------------------------------------
                case 2: {
                    ag = db.pesquisaAgendadoPorEmpresaSemHorario(getSindicatoFilial().getFilial().getId(), data, ((Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuarioAcessoWeb")).getId());
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
                        listaHorarios.add(dtObj);
                    }
                    break;
                }
                // ---------------------------------------------------------------------------------------------------------------
                // ---------------------------------------------------------------------------------------------------------------
            }
        }
        return listaHorarios;
    }

    public String novoProtocolo() {
        agendamento.setDtData(null);
        agendamento.setHorarios(null);
        Dao dao = new Dao();
        agendamento.setFilial(getSindicatoFilial().getFilial());
        setAgendamentoProtocolo(agendamento);
        if (profissao.getId() == -1) {
            profissao = (Profissao) dao.find(new Profissao(), 0);
        }
        return "webAgendamentoContribuinte";
    }

    public void salvar() {
        if (!validaAdmissao()) {
            return;
        }

        if (!validaDemissao()) {
            return;
        }
        Dao dao = new Dao();

        if (listaMotivoDemissao.get(idMotivoDemissao).getDescription().equals("0")) {
            GenericaMensagem.warn("Validação", "Selecione um Motivo de Demissão!");
            return;
        }

        if (tipoAviso == null || tipoAviso.isEmpty()) {
            GenericaMensagem.warn("Validação", "Selecione um Tipo de Aviso!");
            return;
        }
        pessoaEmpresa.setAvisoTrabalhado(tipoAviso.equals("true"));

        configuracaoHomologacao = (ConfiguracaoHomologacao) dao.find(new ConfiguracaoHomologacao(), 1);
        if (configuracaoHomologacao.isWebValidaDataNascimento()) {
            if (fisica.getNascimento().isEmpty()) {
                GenericaMensagem.warn("Validação", "Informar data de nascimento!");
                return;
            }
        }
        if (configuracaoHomologacao.isValidaFuncao()) {
            if (profissao.getId() == -1) {
                GenericaMensagem.warn("Validação", "Informar a função/profissão!");
                return;
            }
        }
        if (configuracaoHomologacao.isWebValidaCarteira()) {
            if (fisica.getCarteira().isEmpty()) {
                GenericaMensagem.warn("Validação", "Informar o número da carteira de trabalho!");
                return;
            }
        }
        if (configuracaoHomologacao.isWebValidaSerie()) {
            if (fisica.getSerie().isEmpty()) {
                GenericaMensagem.warn("Validação", "Informar o número de série da carteira de trabalho!");
                return;
            }
        }
        if (configuracaoHomologacao.isWebValidaContato()) {
            if (agendamento.getContato().isEmpty()) {
                GenericaMensagem.warn("Validação", "Informar nome do contato!");
                return;
            }
        }
        if (configuracaoHomologacao.isWebValidaEmail()) {
            if (agendamento.getEmail().isEmpty()) {
                GenericaMensagem.warn("Validação", "Informar email!");
                return;
            }
        }
        if (configuracaoHomologacao.isWebValidaTelefone()) {
            if (agendamento.getTelefone().isEmpty()) {
                GenericaMensagem.warn("Validação", "Informar telefone!");
                return;
            }
        }

        if (configuracaoHomologacao.isWebValidaAdmissao()) {
            if (pessoaEmpresa.getAdmissao().isEmpty()) {
                GenericaMensagem.warn("Validação", "Informar data de admissão!");
                return;
            }
        }

        if (fisica.getPessoa().getNome().equals("") || fisica.getPessoa().getNome() == null) {
            GenericaMensagem.warn("Atenção", "Digite o nome do Funcionário!");
            return;
        }

        if (!getStrContribuinte().isEmpty()) {
            GenericaMensagem.error("Atenção", "Não é permitido agendar para uma empresa não contribuinte!");
            return;
        }

        DataHoje dataH = new DataHoje();
        Demissao demissao = (Demissao) dao.find(new Demissao(), Integer.parseInt(listaMotivoDemissao.get(idMotivoDemissao).getDescription()));
        if (!pessoaEmpresa.getDemissao().isEmpty() && pessoaEmpresa.getDemissao() != null) {
            if (demissao.getId() == 1) {
                if (DataHoje.converteDataParaInteger(pessoaEmpresa.getDemissao()) > DataHoje.converteDataParaInteger(dataH.incrementarMeses(1, DataHoje.data()))) {
                    GenericaMensagem.warn("Atenção", "Por " + demissao.getDescricao() + " data de Demissão não pode ser maior que 30 dias!");
                    return;
                }
            } else if (demissao.getId() == 2) {
                if (DataHoje.converteDataParaInteger(pessoaEmpresa.getDemissao()) > DataHoje.converteDataParaInteger(dataH.incrementarMeses(3, DataHoje.data()))) {
                    GenericaMensagem.warn("Atenção", "Por " + demissao.getDescricao() + " data de Demissão não pode ser maior que 90 dias!");
                    return;
                }
            } else if (demissao.getId() == 3) {
                if (DataHoje.converteDataParaInteger(pessoaEmpresa.getDemissao()) > DataHoje.converteDataParaInteger(dataH.incrementarDias(10, DataHoje.data()))) {
                    GenericaMensagem.warn("Atenção", "Por " + demissao.getDescricao() + " data de Demissão não pode ser maior que 10 dias!");
                    return;
                }
            }
        } else {
            GenericaMensagem.warn("Atenção", "Data de Demissão é obrigatória!");
            return;
        }

        dao.openTransaction();
        if (fisica.getId() == -1) {
            fisica.getPessoa().setTipoDocumento((TipoDocumento) dao.find(new TipoDocumento(), 1));
            if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(fisica.getPessoa().getDocumento()))) {
                GenericaMensagem.error("Atenção", "Documento Inválido!");
                return;
            }
            if (dao.save(fisica.getPessoa())) {
                dao.save(fisica);
            } else {
                GenericaMensagem.error("Atenção", "Erro ao inserir Pessoa, tente novamente!");
                dao.rollback();
                return;
            }
        }

        HomologacaoDB dba = new HomologacaoDBToplink();
        Agendamento age = dba.pesquisaFisicaAgendada(fisica.getId(), juridica.getId());
        if (age != null) {
            GenericaMensagem.warn("Atenção", "Pessoa já foi agendada para empresa " + age.getPessoaEmpresa().getJuridica().getPessoa().getNome());
            dao.rollback();
            return;
        }

        PessoaEnderecoDB dbp = new PessoaEnderecoDBToplink();
        int ids[] = {1, 3, 4};

        if (enderecoFisica.getId() == -1) {
            if (enderecoFisica.getEndereco().getId() != -1) {
                enderecoFisica.setPessoa(fisica.getPessoa());
                PessoaEndereco pesEnd = enderecoFisica;
                for (int i = 0; i < ids.length; i++) {
                    pesEnd.setTipoEndereco((TipoEndereco) dao.find(new TipoEndereco(), ids[i]));
                    if (!dao.save(pesEnd)) {
                        GenericaMensagem.error("Atenção", "Erro ao inserir Endereço da Pessoa!");
                        dao.rollback();
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
                    GenericaMensagem.error("Atenção", "Erro ao atualizar Endereço da Pessoa!");
                    dao.rollback();
                    return;
                }
            }
        }

        if (pessoaEmpresa.getId() == -1) {
            pessoaEmpresa.setFisica(fisica);
            pessoaEmpresa.setJuridica(juridica);
            if (profissao.getId() == -1) {
                profissao = (Profissao) dao.find(new Profissao(), 0);
            }
            pessoaEmpresa.setFuncao(profissao);
            pessoaEmpresa.setPrincipal(false);

            if (!dao.save(pessoaEmpresa)) {
                GenericaMensagem.error("Atenção", "Erro ao adicionar Pessoa Empresa!");
                dao.rollback();
                return;
            }
        } else {
            if (profissao.getId() == -1) {
                profissao = (Profissao) dao.find(new Profissao(), 0);
            }
            pessoaEmpresa.setFuncao(profissao);
            pessoaEmpresa.setPrincipal(false);

            if (!dao.update(pessoaEmpresa)) {
                GenericaMensagem.error("Atenção", "Erro ao atualizar Pessoa Empresa!");
                dao.rollback();
                return;
            }
        }

        if (!listaEmDebito.isEmpty() && !registro.isBloquearHomologacao()) {
            GenericaMensagem.error("Atenção", "Para efetuar esse agendamento CONTATE o Sindicato!");
            dao.commit();
            return;
        }

        if (!listaEmDebito.isEmpty() && (listaEmDebito.size() > registro.getMesesInadimplentesAgenda())) {
            GenericaMensagem.error("Atenção", "Para efetuar esse agendamento CONTATE o Sindicato!");
            dao.commit();
            return;
        }

        AtendimentoDB dbat = new AtendimentoDBTopLink();
        if (dbat.pessoaOposicao(fisica.getPessoa().getDocumento())) {
            GenericaMensagem.warn("Atenção", "Para efetuar esse agendamento CONTATE o Sindicato!");
            dao.commit();
        } else {
            if (agendamento.getId() == -1) {
                agendamento.setAgendador(null);
                agendamento.setRecepcao(null);
                agendamento.setDtEmissao(DataHoje.dataHoje());
                agendamento.setDemissao(demissao);
                agendamento.setHomologador(null);
                agendamento.setPessoaEmpresa(pessoaEmpresa);
                agendamento.setStatus((Status) dao.find(new Status(), 2));
                if (dao.save(agendamento)) {
                    //msgConfirma = "Agendamento realizado com sucesso!";
                    GenericaMensagem.info("Sucesso", "Agendamento Concluído!");
                    setAgendamentoProtocolo(agendamento);
                    listaHorarios.clear();
                    PF.openDialog("dlg_protocolo");
                } else {
                    //msgConfirma = "Erro ao salvar protocolo!";
                    GenericaMensagem.error("Atenção", "Erro ao salvar protocolo!");
                    dao.rollback();
                    return;
                }
            } else {
                agendamento.setDemissao(demissao);
                if (dao.update(agendamento)) {
                    //msgConfirma = "Agendamento atualizado com sucesso!";
                    GenericaMensagem.info("Sucesso", "Agendamento Atualizado!");
                    setAgendamentoProtocolo(agendamento);
                    listaHorarios.clear();
                } else {
                    //msgConfirma = "Erro ao atualizar protocolo!";
                    GenericaMensagem.error("Atenção", "Erro ao atualizar Protocolo!");
                    dao.rollback();
                    return;
                }
            }
            dao.commit();
        }
    }

    public void agendar(DataObject datao) {
        fisica = new Fisica();
        agendamento = new Agendamento();
        enderecoFisica = new PessoaEndereco();
        profissao = new Profissao();
        pessoaEmpresa = new PessoaEmpresa();
        idMotivoDemissao = 0;
        tipoAviso = null;

        switch (Integer.parseInt(((SelectItem) getListaStatus().get(idStatus)).getDescription())) {
            // STATUS DISPONÍVEL
            case 1: {
                HomologacaoDB db = new HomologacaoDBToplink();

                List<Agendamento> list_a = db.pesquisaAgendadoPorEmpresaSemHorario(getSindicatoFilial().getFilial().getId(), data, juridica.getPessoa().getId());
                if (list_a.size() >= sindicatoFilial.getFilial().getQuantidadeAgendamentosPorEmpresa()) {
                    GenericaMensagem.warn("Atenção", "Limite de Agendamentos para hoje é de " + sindicatoFilial.getFilial().getQuantidadeAgendamentosPorEmpresa());
                    return;
                }

                if (data.getDay() == 6 || data.getDay() == 0) {
                    GenericaMensagem.warn("Atenção", "Fins de semana não é permitido!");
                    return;
                }

                if (DataHoje.converteDataParaInteger(DataHoje.converteData(data)) == DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()))) {
                    GenericaMensagem.warn("Atenção", "Data deve ser apartir de hoje, caso deseje marcar para esta data contate seu Sindicato!");
                    return;
                }

                if (DataHoje.converteDataParaInteger(DataHoje.converteData(getData())) < DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()))) {
                    GenericaMensagem.warn("Atenção", "Data anterior ao dia de hoje!");
                    return;
                }
                if (DataHoje.converteDataParaInteger(((new DataHoje()).incrementarMeses(3, DataHoje.data()))) < DataHoje.converteDataParaInteger(DataHoje.converteData(getData()))) {
                    GenericaMensagem.warn("Atenção", "Data maior que 3 meses!");
                    return;
                }

                if (pesquisarFeriado()) {
                    GenericaMensagem.warn("Atenção", "Esta data esta cadastrada como Feriado!");
                    return;
                }

                Dao dao = new Dao();
                if (data == null) {
                    GenericaMensagem.warn("Atenção", "Selecione uma data para Agendamento!");
                    return;
                } else {
                    if (profissao.getId() == -1) {
                        profissao = (Profissao) dao.find(new Profissao(), 0);
                    }
                    agendamento.setData(DataHoje.converteData(data));
                    agendamento.setHorarios((Horarios) datao.getArgumento0());
                    agendamento.setFilial(getSindicatoFilial().getFilial());
                }
                setAgendamentoProtocolo(agendamento);
                break;
            }

            // STATUS AGENDADO
            case 2: {
                PessoaEnderecoDB db = new PessoaEnderecoDBToplink();
                agendamento = (Agendamento) datao.getArgumento9();
                fisica = ((PessoaEmpresa) datao.getArgumento7()).getFisica();
                enderecoFisica = db.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 1);
                juridica = ((PessoaEmpresa) datao.getArgumento7()).getJuridica();
                pessoaEmpresa = agendamento.getPessoaEmpresa();
                PessoaEmpresa pe = ((PessoaEmpresa) datao.getArgumento7());
                if (pe.getFuncao() == null) {
                    profissao = new Profissao();
                } else {
                    profissao = ((PessoaEmpresa) datao.getArgumento7()).getFuncao();
                }
                for (int i = 0; i < getListaMotivoDemissao().size(); i++) {
                    if (Integer.parseInt(getListaMotivoDemissao().get(i).getDescription()) == agendamento.getDemissao().getId()) {
                        idMotivoDemissao = (Integer) getListaMotivoDemissao().get(i).getValue();
                        break;
                    }
                }
                setAgendamentoProtocolo(agendamento);
                tipoAviso = String.valueOf(pessoaEmpresa.isAvisoTrabalhado());
                break;
            }
        }
        RequestContext.getCurrentInstance().execute("PF('dlg_agendamento').show();");
    }

    public String imprimirPlanilha() {
        if (listaEmDebito.isEmpty()) {
            return null;
        }
        Dao dao = new Dao();
        ImprimirBoleto imp = new ImprimirBoleto();
        List<Movimento> lista = new ArrayList();
        List<Float> listaValores = new ArrayList();
        for (int i = 0; i < listaEmDebito.size(); i++) {
            Movimento m = (Movimento) dao.find(new Movimento(), (Integer) ((List) listaEmDebito.get(i)).get(0));
            lista.add(m);
            listaValores.add(m.getValor());
        }
        imp.imprimirPlanilha(lista, listaValores, false, false);
        imp.visualizar(null);
        return null;
    }

    public boolean pesquisarFeriado() {
        FeriadosDao feriadosDao = new FeriadosDao();
        List<Feriados> listFeriados = feriadosDao.pesquisarPorDataFilialEData(DataHoje.converteData(getData()), getSindicatoFilial().getFilial());
        if (!listFeriados.isEmpty()) {
            GenericaMensagem.info("Feriado", listFeriados.get(0).getNome());
            return true;
        } else {
            listFeriados = feriadosDao.pesquisarPorData(DataHoje.converteData(getData()));
            PessoaEndereco pe = ((PessoaEndereco) ((List) new PessoaEnderecoDBToplink().pesquisaEndPorPessoa(getSindicatoFilial().getFilial().getFilial().getPessoa().getId())).get(0));
            if (!listFeriados.isEmpty()) {
                for (int i = 0; i < listFeriados.size(); i++) {
                    if (listFeriados.get(i).getCidade() == null) {
                        GenericaMensagem.info("Feriado", listFeriados.get(0).getNome());
                        return true;
                    }
                    if (listFeriados.get(i).getCidade().getId() == pe.getEndereco().getCidade().getId()) {
                        GenericaMensagem.info("Feriado", listFeriados.get(0).getNome());
                        return true;
                    }
                }
            }
        }
        return false;

    }

    public void limpar() {
        String datax = agendamento.getData();
        Horarios horario = agendamento.getHorarios();

        //strEndereco = "";
        fisica = new Fisica();
        pessoaEmpresa = new PessoaEmpresa();
        agendamento = new Agendamento();
        profissao = new Profissao();
        enderecoFisica = new PessoaEndereco();

        agendamento.setData(datax);
        agendamento.setHorarios(horario);
        agendamento.setFilial(sindicatoFilial.getFilial());
    }

    public String cancelar() {
        strEndereco = "";
        fisica = new Fisica();
        agendamento = new Agendamento();
        agendamentoProtocolo = agendamento;
        pessoaEmpresa = new PessoaEmpresa();
        profissao = new Profissao();
        enderecoFisica = new PessoaEndereco();
        return "webAgendamentoContribuinte";
    }

    public void pesquisarFuncionarioCPF() throws IOException {
        if (!fisica.getPessoa().getDocumento().isEmpty() && !fisica.getPessoa().getDocumento().equals("___.___.___-__")) {
            String documento = fisica.getPessoa().getDocumento();
            FisicaDB dbFis = new FisicaDBToplink();
            PessoaEnderecoDB dbp = new PessoaEnderecoDBToplink();
            HomologacaoDB db = new HomologacaoDBToplink();
            Dao dao = new Dao();
            fisica.getPessoa().setTipoDocumento((TipoDocumento) dao.find(new TipoDocumento(), 1));
            PessoaEmpresa pe = db.pesquisaPessoaEmpresaPertencente(documento);

            if (pe != null && pe.getJuridica().getId() != juridica.getId()) {
                //msgConfirma = "Esta pessoa pertence a Empresa " + pe.getJuridica().getPessoa().getNome();
                GenericaMensagem.warn("Atenção", "Esta pessoa pertence a Empresa " + pe.getJuridica().getPessoa().getNome());
                fisica = new Fisica();
                enderecoFisica = new PessoaEndereco();
                return;
            }

            List<Fisica> listFisica = dbFis.pesquisaFisicaPorDocSemLike(fisica.getPessoa().getDocumento());
            if (!listFisica.isEmpty()) {
                for (int i = 0; i < listFisica.size(); i++) {
                    if (listFisica.get(i).getId() != fisica.getId()) {
                        fisica = listFisica.get(i);
                        readonlyFisica = true;
                        break;
                    }
                }
                if ((enderecoFisica = dbp.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 1)) == null) {
                    enderecoFisica = new PessoaEndereco();
                    readonlyEndereco = false;
                } else {
                    readonlyEndereco = true;
                }
            } else {
                readonlyFisica = false;
            }

            Oposicao op = db.pesquisaFisicaOposicaoAgendamento(documento, juridica.getId(), DataHoje.ArrayDataHoje()[2] + DataHoje.ArrayDataHoje()[1]);
            if (op == null) {
                //msgConfirma = "Erro na pesquisa Oposição!";
                op = new Oposicao();
            }

            if (fisica.getId() == -1 && op.getId() != -1) {
                fisica.getPessoa().setNome(op.getOposicaoPessoa().getNome());
                fisica.setRg(op.getOposicaoPessoa().getRg());
                fisica.setSexo(op.getOposicaoPessoa().getSexo());
                fisica.getPessoa().setDocumento(documento);
            }

            if (op.getId() != -1) {
                //msgConfirma = "Este CPF possui carta de oposição em "+op.getEmissao();
                //return;
            }
        } else {
            if (fisica.getId() != -1) {
                fisica = new Fisica();
                enderecoFisica = new PessoaEndereco();
            }
        }

        // VERIFICAÇÃO DE PESSOA EMPRESA SEM DEMISSAO
//            if (fisica.getId() != -1){
//                PessoaEmpresaDB dbx = new PessoaEmpresaDBToplink();
//                List<PessoaEmpresa> list_pe = dbx.listaPessoaEmpresaPorFisicaEmpresaDemissao(fisica.getId(), juridica.getId());
//
//                if (!list_pe.isEmpty()){
//                    pessoaEmpresa = list_pe.get(0);
//                    
//                    if (pessoaEmpresa.getFuncao() != null)
//                        profissao = pessoaEmpresa.getFuncao();
//                }else{
//                    if (validaAdmissao() && validaDemissao()){
////                        pessoaEmpresa = new PessoaEmpresa();
//  //                      profissao = new Profissao();
//                    }
//                }
//            }        
        //msgConfirma = "";
        //FacesContext.getCurrentInstance().getExternalContext().redirect("/Sindical/webAgendamentoContribuinte.jsf");
    }

    public String pesquisaEndereco() {
        EnderecoDao db = new EnderecoDao();
        listaEnderecos.clear();
        if (!cepEndereco.isEmpty()) {
            listaEnderecos = db.pesquisaEnderecoCep(cepEndereco);
        }
        return null;
    }

    public String editarEndereco(Endereco ende) {
        enderecoFisica.setEndereco(ende);
        return null;
    }

    public int getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }

    public Juridica getJuridica() {
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }

    public Fisica getFisica() {
        return fisica;
    }

    public void setFisica(Fisica fisica) {
        this.fisica = fisica;
    }

    public PessoaEndereco getEnderecoEmpresa() {
        return enderecoEmpresa;
    }

    public void setEnderecoEmpresa(PessoaEndereco enderecoEmpresa) {
        this.enderecoEmpresa = enderecoEmpresa;
    }

    public PessoaEmpresa getPessoaEmpresa() {
        return pessoaEmpresa;
    }

    public void setPessoaEmpresa(PessoaEmpresa pessoaEmpresa) {
        this.pessoaEmpresa = pessoaEmpresa;
    }

    public String getStrEndereco() {
        PessoaEnderecoDB pessoaEnderecoDB = new PessoaEnderecoDBToplink();
        if (juridica.getId() != -1) {
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

    public int getIdMotivoDemissao() {
        return idMotivoDemissao;
    }

    public void setIdMotivoDemissao(int idMotivoDemissao) {
        this.idMotivoDemissao = idMotivoDemissao;
    }

    public String getStatusEmpresa() {
        //if (statusEmpresa.isEmpty()) {
        HomologacaoDB db = new HomologacaoDBToplink();
        if (juridica.getId() != -1) {
            listaEmDebito = db.pesquisaPessoaDebito(juridica.getPessoa().getId(), DataHoje.data());
        }
        if (!listaEmDebito.isEmpty()) {
            statusEmpresa = "EM DÉBITO";
        } else {
            statusEmpresa = "REGULAR";
        }
        //}
        return statusEmpresa;
    }

    public void setStatusEmpresa(String statusEmpresa) {
        this.statusEmpresa = statusEmpresa;
    }

    public String getCepEndereco() {
        return cepEndereco;
    }

    public void setCepEndereco(String cepEndereco) {
        this.cepEndereco = cepEndereco;
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

    public List getListaEnderecos() {
        return listaEnderecos;
    }

    public void setListaEnderecos(List listaEnderecos) {
        this.listaEnderecos = listaEnderecos;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getStrContribuinte() {
        if (juridica.getId() != -1) {
            JuridicaDB db = new JuridicaDBToplink();
            List listax = db.listaJuridicaContribuinte(juridica.getId());

            if (((List) listax.get(0)).get(11) != null) {
                return strContribuinte = "Empresa Inativa";
            } else {
                return strContribuinte = "";
            }

        }
        return strContribuinte = "Empresa não contribuinte, não poderá efetuar um agendamento!";
    }

    public FilialCidade getSindicatoFilial() {
        return sindicatoFilial;
    }

    public void setSindicatoFilial(FilialCidade sindicatoFilial) {
        this.sindicatoFilial = sindicatoFilial;
    }

    public PessoaEndereco getEnderecoFilial() {
        PessoaEnderecoDB pessoaEnderecoDB = new PessoaEnderecoDBToplink();
        if (enderecoFilial.getId() == -1) {
            enderecoFilial = pessoaEnderecoDB.pesquisaEndPorPessoaTipo(sindicatoFilial.getFilial().getFilial().getPessoa().getId(), 2);
        }
        return enderecoFilial;
    }

    public void setEnderecoFilial(PessoaEndereco enderecoFilial) {
        this.enderecoFilial = enderecoFilial;
    }

    public Registro getRegistro() {
        return registro;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }

    public boolean isReadonlyFisica() {
        return readonlyFisica;
    }

    public void setReadonlyFisica(boolean readonlyFisica) {
        this.readonlyFisica = readonlyFisica;
    }

    public boolean isReadonlyEndereco() {
        return readonlyEndereco;
    }

    public void setReadonlyEndereco(boolean readonlyEndereco) {
        this.readonlyEndereco = readonlyEndereco;
    }

    public Agendamento getAgendamentoProtocolo() {
        return agendamentoProtocolo;
    }

    public void setAgendamentoProtocolo(Agendamento agendamentoProtocolo) {
        this.agendamentoProtocolo = agendamentoProtocolo;
    }

    public String getTipoTelefone() {
        return tipoTelefone;
    }

    public void setTipoTelefone(String tipoTelefone) {
        this.tipoTelefone = tipoTelefone;
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

    public String getTipoAviso() {
        return tipoAviso;
    }

    public void setTipoAviso(String tipoAviso) {
        this.tipoAviso = tipoAviso;
    }
}
