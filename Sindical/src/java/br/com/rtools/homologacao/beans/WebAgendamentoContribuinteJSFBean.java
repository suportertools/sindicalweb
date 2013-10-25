package br.com.rtools.homologacao.beans;

import br.com.rtools.pessoa.beans.PesquisarProfissaoBean;
import br.com.rtools.arrecadacao.Oposicao;
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
import br.com.rtools.impressao.ParametroProtocolo;
import br.com.rtools.movimento.ImprimirBoleto;
import br.com.rtools.pessoa.*;
import br.com.rtools.pessoa.db.*;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class WebAgendamentoContribuinteJSFBean extends PesquisarProfissaoBean {

    private String header = "Agendamento Web";
    private String msgAgendamento = "";
    private String msgConfirma = "";
    private String strEndereco = "";
    private String tipoAviso = "true";
    private String strSalvar = "Agendar";
    private String statusEmpresa = "REGULAR";
    private boolean renderAgendamento = true;
    private boolean renderConcluir = false;
    private boolean renderBtnAgendar = true;
    private Date data = DataHoje.converte(new DataHoje().incrementarDias(1, DataHoje.data()));
    private int idStatus = 0;
    private int idIndex = -1;
    private int idIndexEndereco = -1;
    private int idMotivoDemissao = 0;
    private List listaGrid = new ArrayList();
    private List listaEmDebito = new ArrayList();
    private Agendamento agendamento = new Agendamento();
    private Juridica juridica;
    private FilialCidade sindicatoFilial;
    private PessoaEndereco enderecoFilial = new PessoaEndereco();
    private Fisica fisica = new Fisica();
    private PessoaEndereco enderecoEmpresa = new PessoaEndereco();
    private PessoaEmpresa pessoaEmpresa = new PessoaEmpresa();
    private PessoaEndereco enderecoFisica = new PessoaEndereco();
    private String cepEndereco = "";
    private List listaEnderecos = new ArrayList();
    private boolean imprimirPro = false;
    private boolean readonlyFisica = false;
    private boolean readonlyEndereco = false;
    private String strContribuinte = "";
    private int id_protocolo = -1;
    private Registro registro = new Registro();

    public WebAgendamentoContribuinteJSFBean() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuarioAcessoWeb") != null) {
            JuridicaDB db = new JuridicaDBToplink();
            juridica = db.pesquisaJuridicaPorPessoa(((Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuarioAcessoWeb")).getId());
        }
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
            if (juridica.getContabilidade() != null) {
                contabilidade = juridica.getContabilidade();
            } else {
                contabilidade = new Juridica();
            }

            getSindicatoFilial();
            getEnderecoFilial();

            String datax = "", horario = "";
            if (!age.getData().isEmpty()) {
                datax = age.getData();
                horario = age.getHorarios().getHora();
            }

            lista.add(new ParametroProtocolo(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
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
                    sindicatoFilial.getFilial().getFilial().getPessoa().getTelefone1(),
                    sindicatoFilial.getFilial().getFilial().getPessoa().getEmail1(),
                    String.valueOf(age.getId()),
                    datax,
                    horario,
                    juridica.getPessoa().getDocumento(),
                    juridica.getPessoa().getNome(),
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

            String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/protocolo");

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

    public List<SelectItem> getListaStatus() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        int i = 0;
        StatusDB db = new StatusDBToplink();
        List select = new ArrayList();
        select.add(db.pesquisaCodigo(1));
        select.add(db.pesquisaCodigo(2));
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

    public synchronized List getListaHorarios() {
        listaGrid = new ArrayList();
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
                horario = db.pesquisaTodosHorariosDisponiveis(getSindicatoFilial().getFilial().getId(), idDiaSemana);
                strSalvar = "Agendar";
                int qnt;
                for (int i = 0; i < horario.size(); i++) {
                    qnt = db.pesquisaQntdDisponivel(getSindicatoFilial().getFilial().getId(), horario.get(i), data);
                    if (qnt == -1) {
                        msgAgendamento = "Erro ao pesquisar horários disponíveis!";
                        listaGrid.clear();
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
                        listaGrid.add(dtObj);
                    }
                }
                break;
            }
            // STATUS AGENDADO -----------------------------------------------------------------------------------------------
            case 2: {
                strSalvar = "Atualizar";
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
                            ag.get(i).getData(), // ARG 8
                            ag.get(i));// ARG 9 AGENDAMENTO
                    listaGrid.add(dtObj);
                }
                break;
            }
            // ---------------------------------------------------------------------------------------------------------------
            // ---------------------------------------------------------------------------------------------------------------
        }
        return listaGrid;
    }

    public String novoProtocolo() {
        imprimirPro = false;
        renderBtnAgendar = true;
        renderAgendamento = false;
        renderConcluir = true;
        agendamento.setDtData(null);
        agendamento.setHorarios(null);
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        agendamento.setFilial((Filial) salvarAcumuladoDB.pesquisaCodigo(1, "Filial"));
        if (profissao.getId() == -1) {
            ProfissaoDB dbp = new ProfissaoDBToplink();
            profissao = dbp.pesquisaCodigo(0);
        }
        msgAgendamento = "";
        return "webAgendamentoContribuinte";
    }

    public String salvar() {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        PessoaEnderecoDB dbp = new PessoaEnderecoDBToplink();
        int ids[] = {1, 3, 4};
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        Registro reg = (Registro) salvarAcumuladoDB.pesquisaCodigo(1, "Registro");
        imprimirPro = false;
        if (!listaEmDebito.isEmpty() && !reg.isBloquearHomologacao()) {
            msgConfirma = "Empresa não poderá agendar em Débito. Contate seu Sindicato!";
            return null;
        }

        if (reg.getMesesInadimplentesAgenda() != 0) {
            msgConfirma = "Favor entrar em contato com seu Sindicato! \n clique e imprima seu relatório de débitos";
            return null;
        }

        if (fisica.getPessoa().getNome().equals("") || fisica.getPessoa().getNome() == null) {
            msgConfirma = "Digite o nome do Funcionário!";
            return null;
        }

        if (!strContribuinte.isEmpty()) {
            msgConfirma = "Não é permitido agendar para uma empresa não contribuinte!";
            return null;
        }

        DataHoje dataH = new DataHoje();
        Demissao demissao = (Demissao) salvarAcumuladoDB.pesquisaCodigo(Integer.parseInt(((SelectItem) getListaMotivoDemissao().get(idMotivoDemissao)).getDescription()), "Demissao");
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

        sv.abrirTransacao();
        if (fisica.getId() == -1) {
            fisica.getPessoa().setTipoDocumento((TipoDocumento) salvarAcumuladoDB.pesquisaCodigo(1, "TipoDocumento"));
            if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(fisica.getPessoa().getDocumento()))) {
                msgConfirma = "Documento Inválido!";
                return null;
            }
            if (sv.inserirObjeto(fisica.getPessoa())) {
                sv.inserirObjeto(fisica);
            } else {
                msgConfirma = "Erro ao Inserir pessoa!";
                sv.desfazerTransacao();
                return null;
            }
        }

        HomologacaoDB dba = new HomologacaoDBToplink();
        Agendamento age = dba.pesquisaFisicaAgendada(fisica.getId());
        if (age != null) {
            msgConfirma = "Pessoa já foi agendada para empresa " + age.getPessoaEmpresa().getJuridica().getPessoa().getNome();
            sv.desfazerTransacao();
            return null;
        }

        if (enderecoFisica.getId() == -1) {
            if (enderecoFisica.getEndereco().getId() != -1) {
                enderecoFisica.setPessoa(fisica.getPessoa());
                PessoaEndereco pesEnd = enderecoFisica;
                for (int i = 0; i < ids.length; i++) {
                    pesEnd.setTipoEndereco((TipoEndereco) salvarAcumuladoDB.pesquisaCodigo(ids[i], "TipoEndereco"));
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
                ends.get(i).setComplemento(msgAgendamento);
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

        if (pessoaEmpresa.getId() == -1) {
            pessoaEmpresa.setFisica(fisica);
            pessoaEmpresa.setJuridica(juridica);
            pessoaEmpresa.setFuncao(profissao);
            pessoaEmpresa.setAvisoTrabalhado(Boolean.valueOf(tipoAviso));
            if (!sv.inserirObjeto(pessoaEmpresa)) {
                msgConfirma = "Erro ao Pessoa Empresa!";
                sv.desfazerTransacao();
                return null;
            }
        } else {
            pessoaEmpresa.setAvisoTrabalhado(Boolean.valueOf(tipoAviso));
            if (!sv.alterarObjeto(pessoaEmpresa)) {
                msgConfirma = "Erro ao atualizar Pessoa Empresa!";
                sv.desfazerTransacao();
                return null;
            }
        }

        AtendimentoDB dbat = new AtendimentoDBTopLink();
        if (dbat.pessoaOposicao(fisica.getPessoa().getDocumento())) {
            msgConfirma = "Pessoa cadastrada em oposição, não poderá ser agendada, contate seu Sindicato";
            sv.comitarTransacao();
            return null;
        } else {
            if (agendamento.getId() == -1) {
                agendamento.setAgendador(null);
                agendamento.setRecepcao(null);
                agendamento.setDtEmissao(DataHoje.dataHoje());
                agendamento.setDemissao((Demissao) salvarAcumuladoDB.pesquisaCodigo(Integer.parseInt(((SelectItem) getListaMotivoDemissao().get(idMotivoDemissao)).getDescription()), "Demissao"));
                agendamento.setHomologador(null);
                agendamento.setPessoaEmpresa(pessoaEmpresa);
                agendamento.setStatus((Status) salvarAcumuladoDB.pesquisaCodigo(2, "Status"));
                if (sv.inserirObjeto(agendamento)) {
                    msgConfirma = "Para imprimir Protocolo clique aqui!";
                    id_protocolo = agendamento.getId();
                    limpar();
                } else {
                    msgConfirma = "Erro ao salvar protocolo!";
                    sv.desfazerTransacao();
                    return null;
                }
            } else {
                agendamento.setDemissao((Demissao) salvarAcumuladoDB.pesquisaCodigo(Integer.parseInt(((SelectItem) getListaMotivoDemissao().get(idMotivoDemissao)).getDescription()), "Demissao"));
                if (sv.alterarObjeto(agendamento)) {
                    msgConfirma = "Para imprimir Protocolo clique aqui!";
                    id_protocolo = agendamento.getId();
                    limpar();
                } else {
                    msgConfirma = "Erro ao atualizar protocolo!";
                    sv.desfazerTransacao();
                    return null;
                }
            }
            sv.comitarTransacao();
            imprimirPro = true;
        }
        return null;
    }

    public String agendar() {
        if (data.getDay() == 6 || data.getDay() == 0) {
            msgAgendamento = "Fins de semana não permitido!";
            return "webAgendamentoContribuinte";
        }

        if (DataHoje.converteDataParaInteger(DataHoje.converteData(data))
                == DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()))) {
            msgAgendamento = "Data deve ser apartir de hoje, caso deseje marcar para esta data contate seu Sindicato!";
            return "webAgendamentoContribuinte";
        }

        if (DataHoje.converteDataParaInteger(DataHoje.converteData(getData()))
                < DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()))) {
            msgAgendamento = "Data anterior ao dia de hoje!";
            return "webAgendamentoContribuinte";
        }
        if (DataHoje.converteDataParaInteger(((new DataHoje()).incrementarMeses(3, DataHoje.data())))
                < DataHoje.converteDataParaInteger(DataHoje.converteData(getData()))) {
            msgAgendamento = "Data maior que 3 meses!";
            return "webAgendamentoContribuinte";
        }

        if (pesquisarFeriado()) {
            msgAgendamento = "Esta data esta cadastrada como Feriado!";
            return "webAgendamentoContribuinte";
        }

        renderAgendamento = false;
        renderConcluir = true;
        header = "Agendamento Web >> Concluir";
        switch (Integer.parseInt(((SelectItem) getListaStatus().get(idStatus)).getDescription())) {
            case 1: {
                SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
                if (data == null) {
                    msgAgendamento = "Selecione uma data para Agendamento!";
                    renderAgendamento = true;
                    renderConcluir = false;
                } else {
                    renderBtnAgendar = true;
                    if (profissao.getId() == -1) {
                        ProfissaoDB dbp = new ProfissaoDBToplink();
                        profissao = dbp.pesquisaCodigo(0);
                    }

                    agendamento.setData(DataHoje.converteData(data));
                    agendamento.setHorarios((Horarios) ((DataObject) listaGrid.get(idIndex)).getArgumento0());
                    agendamento.setFilial((Filial) salvarAcumuladoDB.pesquisaCodigo(1, "Filial"));
                    msgAgendamento = "";
                }
                break;
            }

            case 2: {
                PessoaEnderecoDB db = new PessoaEnderecoDBToplink();
                renderBtnAgendar = false;
                agendamento = (Agendamento) ((DataObject) listaGrid.get(idIndex)).getArgumento9();
                fisica = ((PessoaEmpresa) ((DataObject) listaGrid.get(idIndex)).getArgumento7()).getFisica();
                enderecoFisica = db.pesquisaEndPorPessoaTipo(fisica.getPessoa().getId(), 1);
                juridica = ((PessoaEmpresa) ((DataObject) listaGrid.get(idIndex)).getArgumento7()).getJuridica();
                pessoaEmpresa = agendamento.getPessoaEmpresa();
                profissao = ((PessoaEmpresa) ((DataObject) listaGrid.get(idIndex)).getArgumento7()).getFuncao();
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
        return "webAgendamentoContribuinte";
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

    public boolean pesquisarFeriado() {
        FeriadosDB db = new FeriadosDBToplink();
        List listFeriados = db.pesquisarPorData(DataHoje.converteData(getData()));
        if (!listFeriados.isEmpty()) {
            return true;
        }
        return false;

    }

    public void limpar() {
        strEndereco = "";
        renderAgendamento = true;
        renderConcluir = false;
        tipoAviso = "true";
        //msgConfirma = "";
        msgAgendamento = "";
        header = "Agendamento Web";
        fisica = new Fisica();
        pessoaEmpresa = new PessoaEmpresa();
        agendamento = new Agendamento();
        profissao = new Profissao();
        enderecoFisica = new PessoaEndereco();
    }

    public String cancelar() {
        strEndereco = "";
        renderAgendamento = true;
        renderConcluir = false;
        tipoAviso = "true";
        msgConfirma = "";
        msgAgendamento = "";
        header = "Agendamento Web";
        fisica = new Fisica();
        agendamento = new Agendamento();
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
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            fisica.getPessoa().setTipoDocumento((TipoDocumento) salvarAcumuladoDB.pesquisaCodigo(1, "TipoDocumento"));
            PessoaEmpresa pe = db.pesquisaPessoaEmpresaOutra(documento);

            if (pe.getId() != -1 && pe.getJuridica().getId() != juridica.getId()) {
                msgConfirma = "Esta pessoa pertence a Empresa " + pe.getJuridica().getPessoa().getNome();
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
        msgConfirma = "";
        FacesContext.getCurrentInstance().getExternalContext().redirect("/Sindical/webAgendamentoContribuinte.jsf");
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
        return null;
    }

    public void refreshForm() {
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public boolean isRenderAgendamento() {
        return renderAgendamento;
    }

    public void setRenderAgendamento(boolean renderAgendamento) {
        this.renderAgendamento = renderAgendamento;
    }

    public int getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

    public String getMsgAgendamento() {
        return msgAgendamento;
    }

    public void setMsgAgendamento(String msgAgendamento) {
        this.msgAgendamento = msgAgendamento;
    }

    public boolean isRenderConcluir() {
        return renderConcluir;
    }

    public void setRenderConcluir(boolean renderConcluir) {
        this.renderConcluir = renderConcluir;
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

    public String getStrSalvar() {
        return strSalvar;
    }

    public void setStrSalvar(String strSalvar) {
        this.strSalvar = strSalvar;
    }

    public String getStatusEmpresa() {
        HomologacaoDB db = new HomologacaoDBToplink();
        if (juridica.getId() != -1) {
            listaEmDebito = db.pesquisaPessoaDebito(juridica.getPessoa().getId(), DataHoje.data());
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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public boolean isImprimirPro() {
        return imprimirPro;
    }

    public void setImprimirPro(boolean imprimirPro) {
        this.imprimirPro = imprimirPro;
    }

    public String getStrContribuinte() {
        if (juridica.getId() != -1) {
            JuridicaDB db = new JuridicaDBToplink();
            List listax = db.listaJuridicaContribuinte(juridica.getId());
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
//
//    public String getAbrirModal() {
//        return abrirModal;
//    }
//
//    public void setAbrirModal(String abrirModal) {
//        this.abrirModal = abrirModal;
//    }
//
//    public Date getDataOposicao() {
//        return dataOposicao;
//    }
//
//    public void setDataOposicao(Date dataOposicao) {
//        this.dataOposicao = dataOposicao;
//    }

    public FilialCidade getSindicatoFilial() {
        if (enderecoEmpresa.getId() == -1) {
            getStrEndereco();
        }

        if (juridica.getId() != -1 && enderecoEmpresa.getId() != -1) {
            FilialCidadeDB db = new FilialCidadeDBToplink();
            sindicatoFilial = db.pesquisaFilialPorCidade(enderecoEmpresa.getEndereco().getCidade().getId());
        }
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

    public int getId_protocolo() {
        return id_protocolo;
    }

    public void setId_protocolo(int id_protocolo) {
        this.id_protocolo = id_protocolo;
    }

    public Registro getRegistro() {
        if (registro.getId() == -1) {
            registro = (Registro) new SalvarAcumuladoDBToplink().pesquisaCodigo(1, "Registro");
        }
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
}
