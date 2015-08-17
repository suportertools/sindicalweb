package br.com.rtools.homologacao.beans;

import br.com.rtools.pessoa.beans.PesquisarProfissaoBean;
import br.com.rtools.arrecadacao.Oposicao;
import br.com.rtools.arrecadacao.db.WebContabilidadeDB;
import br.com.rtools.arrecadacao.db.WebContabilidadeDBToplink;
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
public final class WebAgendamentoContabilidadeBean extends PesquisarProfissaoBean implements Serializable {

    private List<DataObject> listaHorarios = new ArrayList();
    private List listaEmDebito = new ArrayList();
    private List<SelectItem> listaEmpresas = new ArrayList<SelectItem>();
    private int idStatus = 0;
    private int idMotivoDemissao = 0;
    private int idSelectRadio = 0;
    private String statusEmpresa = "REGULAR";
    private String strEndereco = "";
    private String filtraPor = "todos";
    private boolean chkFiltrar = true;
    private boolean renderBtnAgendar = true;
    private Date data = DataHoje.converte(new DataHoje().incrementarDias(1, DataHoje.data()));
    private Agendamento agendamento = new Agendamento();
    private Agendamento agendamentoProtocolo = new Agendamento();
    private Fisica fisica = new Fisica();
    private PessoaEmpresa pessoaEmpresa = new PessoaEmpresa();
    private Juridica juridica = new Juridica();
    private FilialCidade sindicatoFilial;
    private PessoaEndereco enderecoFilial = new PessoaEndereco();
    private Juridica empresa = new Juridica();
    private PessoaEndereco enderecoEmpresa = new PessoaEndereco();
    private PessoaEndereco enderecoFisica = new PessoaEndereco();
    private String cepEndereco = "";
    private List<Endereco> listaEnderecos = new ArrayList();
    private String strContribuinte = "";
    private Registro registro = new Registro();
    public List<SelectItem> listaStatus = new ArrayList<>();
    public List<SelectItem> listaMotivoDemissao = new ArrayList<>();
    private String tipoTelefone = "telefone";
    private ConfiguracaoHomologacao configuracaoHomologacao = new ConfiguracaoHomologacao();

    private String tipoAviso = null;

    public WebAgendamentoContabilidadeBean() {
        Dao dao = new Dao();
        registro = (Registro) dao.find(new Registro(), 1);
        this.loadListEmpresa();
        this.loadListHorarios();
    }

    public boolean validaAdmissao() {
        if (fisica.getId() != -1 && empresa.getId() != -1 && !pessoaEmpresa.getAdmissao().isEmpty() && pessoaEmpresa.getId() == -1) {
            HomologacaoDB db = new HomologacaoDBToplink();

            PessoaEmpresa pe = db.pesquisaPessoaEmpresaAdmissao(fisica.getId(), empresa.getId(), pessoaEmpresa.getAdmissao());

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
        if (fisica.getId() != -1 && empresa.getId() != -1 && !pessoaEmpresa.getDemissao().isEmpty() && pessoaEmpresa.getId() == -1) {
            HomologacaoDB db = new HomologacaoDBToplink();

            PessoaEmpresa pe = db.pesquisaPessoaEmpresaDemissao(fisica.getId(), empresa.getId(), pessoaEmpresa.getDemissao());

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

    public void loadListEmpresa() {
        WebContabilidadeDB db = new WebContabilidadeDBToplink();
        JuridicaDB dbJur = new JuridicaDBToplink();
        if (juridica.getId() == -1) {
            juridica = dbJur.pesquisaJuridicaPorPessoa(((Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuarioAcessoWeb")).getId());
        }
        List<Juridica> result = db.listaEmpresasPertContabilidade(juridica.getId());
        if (!result.isEmpty()) {
            for (int i = 0; i < result.size(); i++) {
                listaEmpresas.add(new SelectItem(
                        i,
                        result.get(i).getPessoa().getDocumento() + " - " + result.get(i).getPessoa().getNome(),
                        Integer.toString(result.get(i).getId())
                ));
            }
        }
    }

    public void loadListHorarios() {
        // ENDEREÇO DA EMPRESA SELECIONADA PARA PESQUISAR OS HORÁRIOS
        listaHorarios.clear();
        if (listaEmpresas.isEmpty()) {
            GenericaMensagem.warn("Atenção", "Nenhuma Empresa Encontrada!");
            return;
        }
        Dao dao = new Dao();
        PessoaEnderecoDB dbe = new PessoaEnderecoDBToplink();

        empresa = (Juridica) dao.find(new Juridica(), Integer.parseInt(listaEmpresas.get(idSelectRadio).getDescription()));
        enderecoEmpresa = dbe.pesquisaEndPorPessoaTipo(empresa.getPessoa().getId(), 5);

        sindicatoFilial = new FilialCidade();
        // FILIAL DA EMPRESA
        if (empresa.getId() != -1 && enderecoEmpresa.getId() != -1) {
            FilialCidadeDB db = new FilialCidadeDBToplink();
            sindicatoFilial = db.pesquisaFilialPorCidade(enderecoEmpresa.getEndereco().getCidade().getId());
        }
        if (sindicatoFilial.getId() == -1) {
            GenericaMensagem.warn("Atenção", "Filial não encontrada, não é possível visualizar horários!");
            return;
        }

        List<Agendamento> ag = new ArrayList();
        List<Horarios> horario;

        HomologacaoDB db = new HomologacaoDBToplink();
        String agendador;
        String homologador;
        switch (Integer.parseInt(((SelectItem) getListaStatus().get(idStatus)).getDescription())) {
            //STATUS DISPONIVEL ----------------------------------------------------------------------------------------------
            case 1: {
                int idDiaSemana = DataHoje.diaDaSemana(data);
                horario = (List<Horarios>) db.pesquisaTodosHorariosDisponiveis(getSindicatoFilial().getFilial().getId(), idDiaSemana, true);

                int qnt;
                for (int i = 0; i < horario.size(); i++) {
                    qnt = db.pesquisaQntdDisponivel(getSindicatoFilial().getFilial().getId(), horario.get(i), data);
                    if (qnt == -1) {
                        GenericaMensagem.error("Erro", "Não foi possivel encontrar horários disponíveis, contate seu Sindicato!");
                        break;
                    }
                    if (qnt > 0) {
                        listaHorarios.add(new DataObject(
                                horario.get(i), // ARG 0 HORA
                                null, // ARG 1 CNPJ
                                null, //ARG 2 NOME
                                null, //ARG 3 HOMOLOGADOR
                                null, // ARG 4 CONTATO
                                null, // ARG 5 TELEFONE
                                null, // ARG 6 USUARIO
                                null,
                                qnt, // ARG 8 QUANTIDADE DISPONÍVEL
                                null)
                        );
                    }
                }
                break;
            }

            // STATUS AGENDADO -----------------------------------------------------------------------------------------------
            case 2: {
                if (filtraPor.equals("selecionado")) {
                    ag = db.pesquisaAgendadoPorEmpresaSemHorario(getSindicatoFilial().getFilial().getId(), data, empresa.getPessoa().getId());
                } else {
                    WebContabilidadeDB dbw = new WebContabilidadeDBToplink();
                    List<Juridica> result = dbw.listaEmpresasPertContabilidade(juridica.getId());
                    for (int w = 0; w < listaEmpresas.size(); w++) {
                        ag.addAll(db.pesquisaAgendadoPorEmpresaSemHorario(sindicatoFilial.getFilial().getId(), data, result.get(w).getPessoa().getId()));
                    }
                }
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

                    listaHorarios.add(new DataObject(
                            ag.get(i).getHorarios(), // ARG 0 HORA
                            ag.get(i).getPessoaEmpresa().getJuridica().getPessoa().getDocumento(), // ARG 1 CNPJ
                            ag.get(i).getPessoaEmpresa().getJuridica().getPessoa().getNome(), //ARG 2 NOME
                            homologador, //ARG 3 HOMOLOGADOR
                            ag.get(i).getContato(), // ARG 4 CONTATO
                            ag.get(i).getTelefone(), // ARG 5 TELEFONE
                            agendador, // ARG 6 USUARIO
                            ag.get(i).getPessoaEmpresa(), // ARG 7 PESSOA EMPRESA
                            ag.get(i).getData(), // ARG 8
                            ag.get(i))// ARG 9 AGENDAMENTO
                    );
                }
                break;
            }
        }
    }

    public String imprimirPlanilha() {
        if (listaEmDebito.isEmpty()) {
            return null;
        }
        ImprimirBoleto imp = new ImprimirBoleto();
        List<Movimento> lista = new ArrayList();
        List<Float> listaValores = new ArrayList<Float>();
        Dao dao = new Dao();
        for (int i = 0; i < listaEmDebito.size(); i++) {
            Movimento m = (Movimento) dao.find(new Movimento(), (Integer) ((List) listaEmDebito.get(i)).get(0));
            lista.add(m);
            listaValores.add(m.getValor());
        }
        imp.imprimirPlanilha(lista, listaValores, false, false);
        imp.visualizar(null);
        return null;
    }

    public List<SelectItem> getListaStatus() {
        if (listaStatus.isEmpty()) {
            Dao dao = new Dao();
            List select = new ArrayList();
            select.add((Status) dao.find(new Status(), 1));
            select.add((Status) dao.find(new Status(), 2));
            if (!select.isEmpty()) {
                for (int i = 0; i < select.size(); i++) {
                    listaStatus.add(new SelectItem(new Integer(i),
                            (String) ((Status) select.get(i)).getDescricao(),
                            Integer.toString(((Status) select.get(i)).getId())));
                }
            }
        }
        return listaStatus;
    }

    public List<SelectItem> getListaMotivoDemissao() {
        if (listaMotivoDemissao.isEmpty()) {
            Dao dao = new Dao();
            List select = dao.list(new Demissao());
            if (!select.isEmpty()) {
                listaMotivoDemissao.add(new SelectItem(0, "", "0"));
                for (int i = 0; i < select.size(); i++) {
                    listaMotivoDemissao.add(new SelectItem(i + 1,
                            (String) ((Demissao) select.get(i)).getDescricao(),
                            Integer.toString(((Demissao) select.get(i)).getId())));
                }
            }
        }
        return listaMotivoDemissao;
    }

    public void novoProtocolo() {
        Dao dao = new Dao();
        agendamentoProtocolo = new Agendamento();
        renderBtnAgendar = true;
        empresa = (Juridica) dao.find(new Juridica(), Integer.parseInt(((SelectItem) listaEmpresas.get(idSelectRadio)).getDescription()));
        agendamento.setDtData(null);
        agendamento.setHorarios(null);
        agendamento.setFilial(getSindicatoFilial().getFilial());
        agendamentoProtocolo = agendamento;
        if (empresa.getContabilidade() != null) {
            agendamento.setTelefone(empresa.getContabilidade().getPessoa().getTelefone1());
        }
        if (profissao.getId() == -1) {
            profissao = (Profissao) dao.find(new Profissao(), 0);
        }
    }

    public void agendar(DataObject datao) {
        // CAPITURAR ENDEREÇO DA EMPRESA

        empresa = new Juridica();
        enderecoEmpresa = new PessoaEndereco();
        sindicatoFilial = new FilialCidade();
        agendamento = new Agendamento();
        pessoaEmpresa = new PessoaEmpresa();
        fisica = new Fisica();
        enderecoFisica = new PessoaEndereco();
        idMotivoDemissao = 0;
        tipoAviso = null;

        if (listaEmpresas.isEmpty()) {
            GenericaMensagem.warn("Atenção", "Nenhuma empresa encontrada para Agendar!");
            return;
        }

        Dao dao = new Dao();
        PessoaEnderecoDB pessoaEnderecoDB = new PessoaEnderecoDBToplink();

        empresa = (Juridica) dao.find(new Juridica(), Integer.parseInt(listaEmpresas.get(idSelectRadio).getDescription()));
        enderecoEmpresa = pessoaEnderecoDB.pesquisaEndPorPessoaTipo(empresa.getPessoa().getId(), 5);

        if (enderecoEmpresa.getId() != -1) {
            String strCompl;
            if (enderecoEmpresa.getComplemento().isEmpty()) {
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

        // FILIAL DA EMPRESA
        if (empresa.getId() != -1 && enderecoEmpresa.getId() != -1) {
            FilialCidadeDB db = new FilialCidadeDBToplink();
            sindicatoFilial = db.pesquisaFilialPorCidade(enderecoEmpresa.getEndereco().getCidade().getId());
        }

        switch (Integer.parseInt(((SelectItem) getListaStatus().get(idStatus)).getDescription())) {
            case 1: {
                HomologacaoDB db = new HomologacaoDBToplink();

                List<Agendamento> list_a = db.pesquisaAgendadoPorEmpresaSemHorario(getSindicatoFilial().getFilial().getId(), data, empresa.getPessoa().getId());
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

                if (data == null) {
                    GenericaMensagem.warn("Atenção", "Selecione uma data para Agendamento!");
                    return;
                } else {
                    if (empresa.getContabilidade() != null) {
                        agendamento.setTelefone(empresa.getContabilidade().getPessoa().getTelefone1());
                    }
                    if (profissao.getId() == -1) {
                        profissao = (Profissao) dao.find(new Profissao(), 0);
                    }

                    agendamento.setData(DataHoje.converteData(data));
                    agendamento.setHorarios((Horarios) datao.getArgumento0());
                    agendamento.setFilial(sindicatoFilial.getFilial());
                    agendamentoProtocolo = agendamento;
                }
                break;
            }
            case 2: {
                PessoaEnderecoDB db = new PessoaEnderecoDBToplink();
                renderBtnAgendar = false;
                agendamento = (Agendamento) datao.getArgumento9();
                agendamentoProtocolo = agendamento;
                fisica = ((PessoaEmpresa) datao.getArgumento7()).getFisica();
                enderecoFisica = db.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 1);
                empresa = ((PessoaEmpresa) datao.getArgumento7()).getJuridica();
                pessoaEmpresa = agendamento.getPessoaEmpresa();
                profissao = ((PessoaEmpresa) datao.getArgumento7()).getFuncao();
                for (int i = 0; i < getListaMotivoDemissao().size(); i++) {
                    if (Integer.parseInt(getListaMotivoDemissao().get(i).getDescription()) == agendamento.getDemissao().getId()) {
                        idMotivoDemissao = (Integer) getListaMotivoDemissao().get(i).getValue();
                        break;
                    }
                }
                tipoAviso = String.valueOf(pessoaEmpresa.isAvisoTrabalhado());
                break;
            }
        }
        RequestContext.getCurrentInstance().execute("PF('dlg_agendamento').show();");
    }

    public boolean pesquisarFeriado() {
        FeriadosDao feriadosDao = new FeriadosDao();
        List<Feriados> listFeriados = feriadosDao.pesquisarPorDataFilialEData(DataHoje.converteData(getData()), sindicatoFilial.getFilial());
        if (!listFeriados.isEmpty()) {
            GenericaMensagem.info("Feriado", listFeriados.get(0).getNome());
            return true;
        } else {
            listFeriados = feriadosDao.pesquisarPorData(DataHoje.converteData(getData()));
            PessoaEndereco pe = ((PessoaEndereco) ((List) new PessoaEnderecoDBToplink().pesquisaEndPorPessoa(sindicatoFilial.getFilial().getFilial().getPessoa().getId())).get(0));
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

        if (configuracaoHomologacao.isWebValidaDataNascimento()) {
            if (fisica.getNascimento().isEmpty()) {
                GenericaMensagem.warn("Validação", "Informar data de nascimento!");
                return;
            }
        }

        if (configuracaoHomologacao.isWebValidaFuncao()) {
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

        if (fisica.getPessoa().getNome().isEmpty() || fisica.getPessoa().getNome() == null) {
            GenericaMensagem.warn("Atenção", "Digite o nome do Funcionário!");
            return;
        }
        if (!getStrContribuinte().isEmpty()) {
            GenericaMensagem.warn("Atenção", "Não é permitido agendar para uma empresa não contribuinte!");
            return;
        }
        DataHoje dataH = new DataHoje();
        Demissao demissao = (Demissao) dao.find(new Demissao(), Integer.parseInt(((SelectItem) getListaMotivoDemissao().get(idMotivoDemissao)).getDescription()));
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

        dao.openTransaction();
        if (fisica.getId() == -1) {
            fisica.getPessoa().setTipoDocumento((TipoDocumento) dao.find(new TipoDocumento(), 1));
            if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(fisica.getPessoa().getDocumento()))) {
                GenericaMensagem.warn("Atenção", "Documento Inválido!");
                return;
            }
            if (dao.save(fisica.getPessoa())) {
                dao.save(fisica);
            } else {
                GenericaMensagem.error("Erro", "Não foi possível salvar pessoa!");
                dao.rollback();
                return;
            }
        }

        HomologacaoDB dba = new HomologacaoDBToplink();
        Agendamento age = dba.pesquisaFisicaAgendada(fisica.getId(), empresa.getId());
        if (age != null) {
            GenericaMensagem.warn("Atenção", "Pessoa já foi agendada para empresa " + age.getPessoaEmpresa().getJuridica().getPessoa().getNome());
            dao.rollback();
            return;
        }
        int ids[] = {1, 3, 4};
        if (enderecoFisica.getId() == -1) {
            if (enderecoFisica.getEndereco().getId() != -1) {
                enderecoFisica.setPessoa(fisica.getPessoa());
                PessoaEndereco pesEnd = enderecoFisica;
                for (int i = 0; i < ids.length; i++) {
                    pesEnd.setTipoEndereco((TipoEndereco) dao.find(new TipoEndereco(), ids[i]));
                    if (!dao.save(pesEnd)) {
                        dao.rollback();
                        GenericaMensagem.error("Erro", "Não foi possível salvar endereço da pessoa!");
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
            PessoaEnderecoDB pessoaEnderecoDB = new PessoaEnderecoDBToplink();
            List<PessoaEndereco> ends = pessoaEnderecoDB.pesquisaEndPorPessoa(fisica.getPessoa().getId());
            for (int i = 0; i < ends.size(); i++) {
                ends.get(i).setComplemento(enderecoFisica.getComplemento());
                ends.get(i).setEndereco(enderecoFisica.getEndereco());
                ends.get(i).setNumero(enderecoFisica.getNumero());
                ends.get(i).setPessoa(enderecoFisica.getPessoa());
                if (!dao.update(ends.get(i))) {
                    dao.rollback();
                    GenericaMensagem.error("Erro", "Não foi possível atualizar endereço da pessoa!");
                    return;
                }
            }
        }

        if (pessoaEmpresa == null || pessoaEmpresa.getId() == -1) {
            if (profissao.getId() == -1) {
                profissao = (Profissao) dao.find(new Profissao(), 0);
            }
            pessoaEmpresa.setFuncao(profissao);

            pessoaEmpresa.setFisica(fisica);
            pessoaEmpresa.setJuridica(empresa);
            pessoaEmpresa.setPrincipal(false);

            if (!dao.save(pessoaEmpresa)) {
                dao.rollback();
                GenericaMensagem.error("Erro", "Não foi possível salvar Pessoa Empresa!");
                return;
            }
        } else {
            if (pessoaEmpresa == null || profissao.getId() == -1) {
                profissao = (Profissao) dao.find(new Profissao(), 0);
            }

            pessoaEmpresa.setFuncao(profissao);
            pessoaEmpresa.setPrincipal(false);
            if (!dao.update(pessoaEmpresa)) {
                dao.rollback();
                GenericaMensagem.error("Erro", "Não foi possível atualizar Pessoa Empresa!");
                return;
            }
        }

        if (!listaEmDebito.isEmpty() && !registro.isBloquearHomologacao()) {
            if (!updatePessoaEmpresa(dao)) {
                GenericaMensagem.error("Erro", "Não foi possível atualizar Pessoa Empresa!");
                dao.rollback();
                return;
            }
            GenericaMensagem.warn("Atenção", "Para efetuar esse agendamento CONTATE o Sindicato!");
            dao.commit();
            return;
        }
        if (!listaEmDebito.isEmpty() && (listaEmDebito.size() > registro.getMesesInadimplentesAgenda())) {
            if (!updatePessoaEmpresa(dao)) {
                GenericaMensagem.error("Erro", "Não foi possível atualizar Pessoa Empresa!");
                dao.rollback();
                return;
            }
            GenericaMensagem.warn("Atenção", "Para efetuar esse agendamento CONTATE o Sindicato!");
            dao.commit();
            return;
        }

        AtendimentoDB dbat = new AtendimentoDBTopLink();
        if (dbat.pessoaOposicao(fisica.getPessoa().getDocumento())) {
            if (!updatePessoaEmpresa(dao)) {
                GenericaMensagem.error("Erro", "Não foi possível atualizar Pessoa Empresa!");
                dao.rollback();
                return;
            }
            dao.commit();
            GenericaMensagem.warn("Atenção", "Para efetuar esse agendamento CONTATE o Sindicato!");
        } else {
            Demissao demissaox = (Demissao) dao.find(new Demissao(), Integer.parseInt(((SelectItem) getListaMotivoDemissao().get(idMotivoDemissao)).getDescription()));
            if (agendamento.getId() == -1) {
                agendamento.setAgendador(null);
                agendamento.setRecepcao(null);
                agendamento.setDemissao(demissaox);
                agendamento.setHomologador(null);
                agendamento.setDtEmissao(DataHoje.dataHoje());
                agendamento.setPessoaEmpresa(pessoaEmpresa);
                agendamento.setStatus((Status) dao.find(new Status(), 2));
                if (dao.save(agendamento)) {
                    agendamentoProtocolo = agendamento;
                    GenericaMensagem.info("Sucesso", "Agendamento Salvo!");
                    listaHorarios.clear();
                    loadListHorarios();
                } else {
                    GenericaMensagem.error("Erro", "Não foi possível salvar protocolo!");
                    dao.rollback();
                    //agendamento.setId(-1);
                    return;
                }
            } else {
                agendamento.setDemissao(demissaox);
                if (dao.update(agendamento)) {
                    GenericaMensagem.info("Sucesso", "Agendamento Atualizado!");
                    agendamentoProtocolo = agendamento;
                } else {
                    dao.rollback();
                    GenericaMensagem.error("Erro", "Não foi possível Atualizar protocolo!");
                    return;
                }
            }
            dao.commit();
        }
    }

    public Boolean updatePessoaEmpresa(Dao dao) {
        pessoaEmpresa.setDtDemissao(null);
        if (!dao.update(pessoaEmpresa)) {
            return false;
        }
        return true;
    }

    public void cancelar() {
        strEndereco = "";
        fisica = new Fisica();
        agendamento = new Agendamento();
        agendamentoProtocolo = agendamento;
        pessoaEmpresa = new PessoaEmpresa();
        profissao = new Profissao();
        empresa = new Juridica();
        enderecoFisica = new PessoaEndereco();
    }

    public void limpar() {
        String datax = agendamento.getData();
        Horarios horario = agendamento.getHorarios();
        //strEndereco = "";
        fisica = new Fisica();
        pessoaEmpresa = new PessoaEmpresa();
        agendamento = new Agendamento();
        profissao = new Profissao();
        //empresa = new Juridica();
        enderecoFisica = new PessoaEndereco();

        agendamento.setData(datax);
        agendamento.setHorarios(horario);
        agendamento.setFilial(sindicatoFilial.getFilial());
    }

    public void pesquisarFuncionarioCPF() throws IOException {
        if (!fisica.getPessoa().getDocumento().isEmpty() && !fisica.getPessoa().getDocumento().equals("___.___.___-__")) {
            String documento = fisica.getPessoa().getDocumento();
            Dao dao = new Dao();
            FisicaDB dbFis = new FisicaDBToplink();
            PessoaEnderecoDB dbp = new PessoaEnderecoDBToplink();
            HomologacaoDB db = new HomologacaoDBToplink();
            fisica.getPessoa().setTipoDocumento((TipoDocumento) dao.find(new TipoDocumento(), 1));
            PessoaEmpresa pe = db.pesquisaPessoaEmpresaPertencente(documento);

            if (pe != null && pe.getJuridica().getId() != empresa.getId()) {
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
                        break;
                    }
                }
                if ((enderecoFisica = dbp.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 1)) == null) {
                    enderecoFisica = new PessoaEndereco();
                }
            }

            Oposicao op = db.pesquisaFisicaOposicaoAgendamento(documento, empresa.getId(), DataHoje.ArrayDataHoje()[2] + DataHoje.ArrayDataHoje()[1]);
            if (op == null) {
                //GenericaMensagem.error("Erro", "Não foi possível pesquisar Oposição");
                op = new Oposicao();
            }

            if (fisica.getId() == -1 && op.getId() != -1) {
                fisica.getPessoa().setNome(op.getOposicaoPessoa().getNome());
                fisica.setRg(op.getOposicaoPessoa().getRg());
                fisica.setSexo(op.getOposicaoPessoa().getSexo());
                fisica.getPessoa().setDocumento(documento);
            }

            // VERIFICAÇÃO DE PESSOA EMPRESA SEM DEMISSAO
//            if (fisica.getId() != -1){
//                PessoaEmpresaDB dbx = new PessoaEmpresaDBToplink();
//                List<PessoaEmpresa> list_pe = dbx.listaPessoaEmpresaPorFisicaEmpresaDemissao(fisica.getId(), empresa.getId());
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
        //FacesContext.getCurrentInstance().getExternalContext().redirect("/Sindical/webAgendamentoContabilidade.jsf");
    }

    public String pesquisaEndereco() {
        EnderecoDao db = new EnderecoDao();
        listaEnderecos.clear();
        if (!cepEndereco.isEmpty()) {
            listaEnderecos = db.pesquisaEnderecoCep(cepEndereco);
        }
        return null;
    }

    public void editarEndereco(Endereco endereco) {
        enderecoFisica.setEndereco(endereco);
    }

    public int getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

    public int getIdMotivoDemissao() {
        return idMotivoDemissao;
    }

    public void setIdMotivoDemissao(int idMotivoDemissao) {
        this.idMotivoDemissao = idMotivoDemissao;
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

    public PessoaEmpresa getPessoaEmpresa() {
        return pessoaEmpresa;
    }

    public void setPessoaEmpresa(PessoaEmpresa pessoaEmpresa) {
        this.pessoaEmpresa = pessoaEmpresa;
    }

    public String getStatusEmpresa() {
        HomologacaoDB db = new HomologacaoDBToplink();
        if (empresa.getId() != -1) {
            listaEmDebito = db.pesquisaPessoaDebito(empresa.getPessoa().getId(), DataHoje.data());
        }
        if (!listaEmDebito.isEmpty()) {
            statusEmpresa = "EM DÉBITO";
        } else {
            statusEmpresa = "REGULAR";
        }
        return statusEmpresa;
    }

    public void setStatusEmpresa(String statusEmpresa) {
        this.statusEmpresa = statusEmpresa;
    }

    public String getStrEndereco() {
        return strEndereco;
    }

    public void setStrEndereco(String strEndereco) {
        this.strEndereco = strEndereco;
    }

    public PessoaEndereco getEnderecoEmpresa() {
        return enderecoEmpresa;
    }

    public void setEnderecoEmpresa(PessoaEndereco enderecoEmpresa) {
        this.enderecoEmpresa = enderecoEmpresa;
    }

    public Juridica getJuridica() {
        JuridicaDB db = new JuridicaDBToplink();
        if (juridica.getId() == -1) {
            juridica = db.pesquisaJuridicaPorPessoa(((Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuarioAcessoWeb")).getId());
        }
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }

    public Juridica getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Juridica empresa) {
        this.empresa = empresa;
    }

    public int getIdSelectRadio() {
        return idSelectRadio;
    }

    public void setIdSelectRadio(int idSelectRadio) {
        this.idSelectRadio = idSelectRadio;
    }

    public boolean isChkFiltrar() {
        return chkFiltrar;
    }

    public void setChkFiltrar(boolean chkFiltrar) {
        this.chkFiltrar = chkFiltrar;
    }

    public boolean isRenderBtnAgendar() {
        return renderBtnAgendar;
    }

    public void setRenderBtnAgendar(boolean renderBtnAgendar) {
        this.renderBtnAgendar = renderBtnAgendar;
    }

    public String getCepEndereco() {
        return cepEndereco;
    }

    public void setCepEndereco(String cepEndereco) {
        this.cepEndereco = cepEndereco;
    }

    public List<Endereco> getListaEnderecos() {
        return listaEnderecos;
    }

    public void setListaEnderecos(List<Endereco> listaEnderecos) {
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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getFiltraPor() {
        return filtraPor;
    }

    public void setFiltraPor(String filtraPor) {
        this.filtraPor = filtraPor;
    }

    public String getStrContribuinte() {
        if (empresa.getId() != -1) {
            JuridicaDB db = new JuridicaDBToplink();
            List listax = db.listaJuridicaContribuinte(empresa.getId());
            for (int i = 0; i < listax.size(); i++) {
                if (((List) listax.get(0)).get(11) != null) {
                    return strContribuinte = "Empresa Inativa";
                } else {
                    return strContribuinte = "";
                }
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

    public Agendamento getAgendamentoProtocolo() {
        return agendamentoProtocolo;
    }

    public void setAgendamentoProtocolo(Agendamento agendamentoProtocolo) {
        this.agendamentoProtocolo = agendamentoProtocolo;
    }

    public List<SelectItem> getListaEmpresas() {
        return listaEmpresas;
    }

    public void setListaEmpresas(List<SelectItem> listaEmpresas) {
        this.listaEmpresas = listaEmpresas;
    }

    public List<DataObject> getListaHorarios() {
        return listaHorarios;
    }

    public void setListaHorarios(List<DataObject> listaHorarios) {
        this.listaHorarios = listaHorarios;
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
