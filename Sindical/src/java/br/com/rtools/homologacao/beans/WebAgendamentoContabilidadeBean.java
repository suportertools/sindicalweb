package br.com.rtools.homologacao.beans;

import br.com.rtools.pessoa.beans.PesquisarProfissaoBean;
import br.com.rtools.arrecadacao.Oposicao;
import br.com.rtools.arrecadacao.db.WebContabilidadeDB;
import br.com.rtools.arrecadacao.db.WebContabilidadeDBToplink;
import br.com.rtools.atendimento.db.AtendimentoDB;
import br.com.rtools.atendimento.db.AtendimentoDBTopLink;
import br.com.rtools.endereco.Endereco;
import br.com.rtools.endereco.db.EnderecoDB;
import br.com.rtools.endereco.db.EnderecoDBToplink;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.homologacao.Agendamento;
import br.com.rtools.homologacao.Demissao;
import br.com.rtools.homologacao.Horarios;
import br.com.rtools.homologacao.Status;
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
    
    public WebAgendamentoContabilidadeBean() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        registro = (Registro) sv.find(new Registro(), 1);
        this.loadListEmpresa();
        this.loadListHorarios();
    }

    public void alterarTipoMascara(){
        if (tipoTelefone.equals("telefone"))
            tipoTelefone = "celular";
        else
            tipoTelefone = "telefone";
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
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        PessoaEnderecoDB dbe = new PessoaEnderecoDBToplink();

        empresa = (Juridica) sv.pesquisaCodigo(Integer.parseInt(listaEmpresas.get(idSelectRadio).getDescription()), "Juridica");
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

        List<Agendamento> ag = new ArrayList<Agendamento>();
        List<Horarios> horario;

        HomologacaoDB db = new HomologacaoDBToplink();
        String agendador;
        String homologador;
        switch (Integer.parseInt(((SelectItem) getListaStatus().get(idStatus)).getDescription())) {
            //STATUS DISPONIVEL ----------------------------------------------------------------------------------------------
            case 1: {
                int idDiaSemana = DataHoje.diaDaSemana(data);
                horario = (List<Horarios>) db.pesquisaTodosHorariosDisponiveis(getSindicatoFilial().getFilial().getId(), idDiaSemana);

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
        for (int i = 0; i < listaEmDebito.size(); i++) {
            Movimento m = (Movimento) new SalvarAcumuladoDBToplink().pesquisaCodigo((Integer) ((List) listaEmDebito.get(i)).get(0), "Movimento");
            lista.add(m);
            listaValores.add(m.getValor());
        }
        imp.imprimirPlanilha(lista, listaValores, false, false);
        imp.visualizar(null);
        return null;
    }

    public List<SelectItem> getListaStatus() {
        if (listaStatus.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            List select = new ArrayList();
            select.add((Status) salvarAcumuladoDB.pesquisaCodigo(1, "Status"));
            select.add((Status) salvarAcumuladoDB.pesquisaCodigo(2, "Status"));
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
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            List select = salvarAcumuladoDB.listaObjeto("Demissao");
            if (!select.isEmpty()) {
                for (int i = 0; i < select.size(); i++) {
                    listaMotivoDemissao.add(new SelectItem(new Integer(i),
                            (String) ((Demissao) select.get(i)).getDescricao(),
                            Integer.toString(((Demissao) select.get(i)).getId())));
                }
            }
        }
        return listaMotivoDemissao;
    }

    public void novoProtocolo() {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        agendamentoProtocolo = new Agendamento();
        renderBtnAgendar = true;
        empresa = (Juridica) salvarAcumuladoDB.pesquisaCodigo(Integer.parseInt(((SelectItem) listaEmpresas.get(idSelectRadio)).getDescription()), "Juridica");
        agendamento.setDtData(null);
        agendamento.setHorarios(null);
        //agendamento.setFilial((Filial) salvarAcumuladoDB.pesquisaCodigo(1, "Filial"));
        agendamento.setFilial(getSindicatoFilial().getFilial());
        agendamentoProtocolo = agendamento;
        if (empresa.getContabilidade() != null) {
            agendamento.setTelefone(empresa.getContabilidade().getPessoa().getTelefone1());
        }
        if (profissao.getId() == -1) {
            profissao = (Profissao) salvarAcumuladoDB.pesquisaCodigo(0, "Profissao");
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
        
        if (listaEmpresas.isEmpty()) {
            GenericaMensagem.warn("Atenção", "Nenhuma empresa encontrada para Agendar!");
            return;
        }

        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        PessoaEnderecoDB pessoaEnderecoDB = new PessoaEnderecoDBToplink();

        empresa = (Juridica) salvarAcumuladoDB.pesquisaCodigo(Integer.parseInt(listaEmpresas.get(idSelectRadio).getDescription()), "Juridica");
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
                        profissao = (Profissao) salvarAcumuladoDB.pesquisaCodigo(0, "Profissao");
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
                //tipoAviso = String.valueOf(pessoaEmpresa.isAvisoTrabalhado());
                break;
            }
        }
        RequestContext.getCurrentInstance().execute("PF('dlg_agendamento').show();");
    }

    public boolean pesquisarFeriado() {
        FeriadosDB db = new FeriadosDBToplink();
        List listFeriados = db.pesquisarPorDataFilial(DataHoje.converteData(getData()), sindicatoFilial.getFilial());
        if (!listFeriados.isEmpty()) {
            return true;
        }
        return false;
    }

    public void salvar() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

        if (!listaEmDebito.isEmpty() && !registro.isBloquearHomologacao()) {
            GenericaMensagem.warn("Atenção", "Para efetuar esse agendamento CONTATE o Sindicato!");
            return;
        }
        if (!listaEmDebito.isEmpty() && (listaEmDebito.size() > registro.getMesesInadimplentesAgenda())) {
            GenericaMensagem.warn("Atenção", "Para efetuar esse agendamento CONTATE o Sindicato!");
            return;
        }

        if (fisica.getPessoa().getNome().isEmpty() || fisica.getPessoa().getNome() == null) {
            GenericaMensagem.warn("Atenção", "Digite o nome do Funcionário!");
            return;
        }

        if (!strContribuinte.isEmpty()) {
            GenericaMensagem.warn("Atenção", "Não é permitido agendar para uma empresa não contribuinte!");
            return;
        }
        int ids[] = {1, 3, 4};
        DataHoje dataH = new DataHoje();
        Demissao demissao = (Demissao) sv.find(new Demissao(), Integer.parseInt(((SelectItem) getListaMotivoDemissao().get(idMotivoDemissao)).getDescription()));
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

        sv.abrirTransacao();
        if (fisica.getId() == -1) {
            fisica.getPessoa().setTipoDocumento((TipoDocumento) sv.find(new TipoDocumento(), 1));
            if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(fisica.getPessoa().getDocumento()))) {
                GenericaMensagem.warn("Atenção", "Documento Inválido!");
                return;
            }
            if (sv.inserirObjeto(fisica.getPessoa())) {
                sv.inserirObjeto(fisica);
            } else {
                GenericaMensagem.error("Erro", "Não foi possível salvar pessoa!");
                sv.desfazerTransacao();
                return;
            }
        }

        HomologacaoDB dba = new HomologacaoDBToplink();
        Agendamento age = dba.pesquisaFisicaAgendada(fisica.getId());
        if (age != null) {
            GenericaMensagem.warn("Atenção", "Pessoa já foi agendada para empresa " + age.getPessoaEmpresa().getJuridica().getPessoa().getNome());
            sv.desfazerTransacao();
            return;
        }

        if (enderecoFisica.getId() == -1) {
            if (enderecoFisica.getEndereco().getId() != -1) {
                enderecoFisica.setPessoa(fisica.getPessoa());
                PessoaEndereco pesEnd = enderecoFisica;
                for (int i = 0; i < ids.length; i++) {
                    pesEnd.setTipoEndereco((TipoEndereco) sv.pesquisaObjeto(ids[i], "TipoEndereco"));
                    if (!sv.inserirObjeto(pesEnd)) {
                        sv.desfazerTransacao();
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
                if (!sv.alterarObjeto(ends.get(i))) {
                    sv.desfazerTransacao();
                    GenericaMensagem.error("Erro", "Não foi possível atualizar endereço da pessoa!");
                    return;
                }
            }
        }

        if (pessoaEmpresa.getId() == -1) {
            Profissao p = profissao;
            if (p == null) {
                p = (Profissao) sv.find(new Profissao(), 0);
            }
            pessoaEmpresa.setFisica(fisica);
            pessoaEmpresa.setJuridica(empresa);
            pessoaEmpresa.setFuncao(p);
            if (!sv.inserirObjeto(pessoaEmpresa)) {
                sv.desfazerTransacao();
                GenericaMensagem.error("Erro", "Não foi possível salvar Pessoa Empresa!");
                return;
            }
        } else {
            if (!sv.alterarObjeto(pessoaEmpresa)) {
                sv.desfazerTransacao();
                GenericaMensagem.error("Erro", "Não foi possível atualizar Pessoa Empresa!");
                return;
            }
        }

        AtendimentoDB dbat = new AtendimentoDBTopLink();
        if (dbat.pessoaOposicao(fisica.getPessoa().getDocumento())) {
            sv.comitarTransacao();
            GenericaMensagem.warn("Atenção", "Para efetuar esse agendamento CONTATE o Sindicato!");
            return;
        } else {
            Demissao demissaox = (Demissao) sv.find(new Demissao(), Integer.parseInt(((SelectItem) getListaMotivoDemissao().get(idMotivoDemissao)).getDescription()));
            if (agendamento.getId() == -1) {
                agendamento.setAgendador(null);
                agendamento.setRecepcao(null);
                agendamento.setDemissao(demissaox);
                agendamento.setHomologador(null);
                agendamento.setDtEmissao(DataHoje.dataHoje());
                agendamento.setPessoaEmpresa(pessoaEmpresa);
                agendamento.setStatus((Status) sv.find(new Status(), 2));
                if (sv.inserirObjeto(agendamento)) {
                    agendamentoProtocolo = agendamento;
                    GenericaMensagem.info("Sucesso", "Agendamento Salvo!");
                } else {
                    GenericaMensagem.error("Erro", "Não foi possível salvar protocolo!");
                    sv.desfazerTransacao();
                    //agendamento.setId(-1);
                    return;
                }
            } else {
                agendamento.setDemissao(demissaox);
                if (sv.alterarObjeto(agendamento)) {
                    GenericaMensagem.info("Sucesso", "Agendamento Atualizado!");
                    agendamentoProtocolo = agendamento;
                } else {
                    sv.desfazerTransacao();
                    GenericaMensagem.error("Erro", "Não foi possível Atualizar protocolo!");
                    return;
                }
            }
            sv.comitarTransacao();
        }
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
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            FisicaDB dbFis = new FisicaDBToplink();
            PessoaEnderecoDB dbp = new PessoaEnderecoDBToplink();
            HomologacaoDB db = new HomologacaoDBToplink();
            fisica.getPessoa().setTipoDocumento((TipoDocumento) salvarAcumuladoDB.pesquisaCodigo(1, "TipoDocumento"));
            PessoaEmpresa pe = db.pesquisaPessoaEmpresaOutra(documento);

            if (pe.getId() != -1 && pe.getJuridica().getId() != empresa.getId()) {
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
        EnderecoDB db = new EnderecoDBToplink();
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
}
