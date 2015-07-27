package br.com.rtools.arrecadacao.beans;

import br.com.rtools.arrecadacao.Rais;
import br.com.rtools.arrecadacao.dao.RaisDao;
import br.com.rtools.financeiro.SalarioMinimo;
import br.com.rtools.financeiro.TipoRemuneracao;
import br.com.rtools.financeiro.dao.SalarioMinimoDao;
import br.com.rtools.pessoa.ClassificacaoEconomica;
import br.com.rtools.pessoa.Escolaridade;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.IndicadorAlvara;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Nacionalidade;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.Raca;
import br.com.rtools.pessoa.TipoDeficiencia;
import br.com.rtools.pessoa.TipoDocumento;
import br.com.rtools.pessoa.beans.PesquisarProfissaoBean;
import br.com.rtools.pessoa.db.PessoaEnderecoDB;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.sistema.SisPessoa;
import br.com.rtools.sistema.dao.SisPessoaDao;
import br.com.rtools.utilitarios.AnaliseString;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Mask;
import br.com.rtools.utilitarios.PF;
import br.com.rtools.utilitarios.ValidaDocumentos;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;

@ManagedBean
@SessionScoped
public class RaisBean extends PesquisarProfissaoBean implements Serializable {

    private Rais rais;
    private SalarioMinimo salarioMinimo;
    private List<Rais> listRais;
    private List<SelectItem> listNacionalidade;
    private List<SelectItem> listEscolaridade;
    private List<SelectItem> listRaca;
    private List<SelectItem> listClassificacaoEconomica;
    private List<SelectItem> listTipoRemuneracao;
    private List<SelectItem> listIndicadorAlvara;
    private List<SelectItem> listTipoDeficiencia;
    private Integer indexNacionalidade;
    private Integer indexEscolaridade;
    private Integer indexRaca;
    private Integer indexClassificacaoEconomica;
    private Integer indexTipoRemuneracao;
    private Integer indexIndicadorAlvara;
    private Integer indexTipoDeficiencia;
    private Integer activeIndex;
    private String message;
    private String porPesquisa;
    private String descricaoPesquisa;
    private String comoPesquisa;
    private boolean removeFiltro;
    private String dataEmissao;

    @PostConstruct
    public void init() {
        rais = new Rais();
        rais.setAdmissaoString("");
        rais.setDemissaoString("");
        rais.setAfastamentoString("");
        rais.getSisPessoa().setNascimento("");
        rais.getSisPessoa().setSexo("M");
        SalarioMinimoDao smd = new SalarioMinimoDao();
        salarioMinimo = smd.salarioMinimoVigente();
        if (salarioMinimo == null) {
            salarioMinimo = new SalarioMinimo();
        }
        rais.setResponsavelCadastro(((Usuario) GenericaSessao.getObject("sessaoUsuario")).getPessoa());
        listRais = new ArrayList<Rais>();
        listNacionalidade = new ArrayList<SelectItem>();
        listEscolaridade = new ArrayList<SelectItem>();
        listRaca = new ArrayList<SelectItem>();
        listClassificacaoEconomica = new ArrayList<SelectItem>();
        listTipoRemuneracao = new ArrayList<SelectItem>();
        listTipoDeficiencia = new ArrayList<SelectItem>();
        listIndicadorAlvara = new ArrayList<SelectItem>();
        indexNacionalidade = 0;
        indexEscolaridade = 0;
        indexRaca = 0;
        indexClassificacaoEconomica = 0;
        indexTipoRemuneracao = 0;
        indexIndicadorAlvara = 0;
        indexTipoDeficiencia = 0;
        activeIndex = 0;
        message = "";
        porPesquisa = "todos";
        descricaoPesquisa = "";
        comoPesquisa = "";
        removeFiltro = false;
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("raisBean");
        GenericaSessao.remove("fisicaPesquisa");
        GenericaSessao.remove("pessoaPesquisa");
        GenericaSessao.remove("juridicaPesquisa");
    }

//    public void clear() {
//        GenericaSessao.remove("raisBean");
//    }
    public String clear() {
        GenericaSessao.remove("raisBean");
        return "rais";
    }

    public void save() {
        if (rais.getAnoBase() <= 0) {
            GenericaMensagem.warn("Validação", "Informar o ano base!");
            return;
        }
        if (rais.getSisPessoa().getNome().equals("")) {
            GenericaMensagem.warn("Validação", "Informar nome!");
            return;
        }
        if (rais.getSisPessoa().getDocumento().equals("")) {
            GenericaMensagem.warn("Validação", "Informar o documento!");
            return;
        }
        if (rais.getSisPessoa().getNascimento().equals("")) {
            GenericaMensagem.warn("Validação", "Informar data de nascimento!");
            return;
        }
        if (rais.getEmpresa().getId() == -1) {
            GenericaMensagem.warn("Validação", "Pesquisar empresa!");
            return;
        }
        if (rais.getAdmissaoString().equals("")) {
            GenericaMensagem.warn("Validação", "Informar a data de admissão!");
            return;
        }
        if (!rais.getDemissaoString().equals("")) {
            int admissao = DataHoje.converteDataParaInteger(rais.getAdmissaoString());
            int demissao = DataHoje.converteDataParaInteger(rais.getDemissaoString());
            if (demissao < admissao) {
                GenericaMensagem.warn("Validação", "Demissão deve ser superior a data de admissão!");
                return;
            }
        }
        if (!rais.getAfastamentoString().equals("")) {
            int admissao = DataHoje.converteDataParaInteger(rais.getAdmissaoString());
            int afastamento = DataHoje.converteDataParaInteger(rais.getAfastamentoString());
            if (afastamento < admissao) {
                GenericaMensagem.warn("Validação", "A data de afastamento deve ser superior a data de admissão!");
                return;
            }
            if (rais.getMotivoAfastamento().equals("")) {
                GenericaMensagem.warn("Validação", "Informar o motivo do afastamento!");
                return;
            }
        }
        if (rais.getProfissao().getId() == -1) {
            GenericaMensagem.warn("Validação", "Informa profissão!");
            return;
        }
        DaoInterface di = new Dao();
        if (Integer.parseInt(getListNacionalidade().get(indexNacionalidade).getDescription()) != 0) {
            rais.setNacionalidade((Nacionalidade) di.find(new Nacionalidade(), Integer.parseInt(getListNacionalidade().get(indexNacionalidade).getDescription())));
        } else {
            rais.setNacionalidade(null);
        }

        if (Integer.parseInt(getListEscolaridade().get(indexEscolaridade).getDescription()) != 0) {
            rais.setEscolaridade((Escolaridade) di.find(new Escolaridade(), Integer.parseInt(getListEscolaridade().get(indexEscolaridade).getDescription())));
        } else {
            rais.setEscolaridade(null);
        }

        if (Integer.parseInt(getListRaca().get(indexRaca).getDescription()) != 0) {
            rais.setRaca((Raca) di.find(new Raca(), Integer.parseInt(getListRaca().get(indexRaca).getDescription())));
        } else {
            rais.setRaca(null);
        }

        if (Integer.parseInt(getListClassificacaoEconomica().get(indexClassificacaoEconomica).getDescription()) != 0) {
            rais.setClassificacaoEconomica((ClassificacaoEconomica) di.find(new ClassificacaoEconomica(), Integer.parseInt(getListClassificacaoEconomica().get(indexClassificacaoEconomica).getDescription())));
        } else {
            rais.setClassificacaoEconomica(null);
        }

        if (Integer.parseInt(getListTipoDeficiencia().get(indexTipoDeficiencia).getDescription()) != 0) {
            rais.setTipoDeficiencia((TipoDeficiencia) di.find(new TipoDeficiencia(), Integer.parseInt(getListTipoDeficiencia().get(indexTipoDeficiencia).getDescription())));
        } else {
            rais.setTipoDeficiencia(null);
        }

        if (Integer.parseInt(getListTipoRemuneracao().get(indexTipoRemuneracao).getDescription()) != 0) {
            rais.setTipoRemuneracao((TipoRemuneracao) di.find(new TipoRemuneracao(), Integer.parseInt(getListTipoRemuneracao().get(indexTipoRemuneracao).getDescription())));
        } else {
            rais.setTipoRemuneracao(null);
        }

        if (rais.isAlvara()) {
            if (Integer.parseInt(getListIndicadorAlvara().get(indexIndicadorAlvara).getDescription()) != 0) {
                rais.setIndicadorAlvara((IndicadorAlvara) di.find(new IndicadorAlvara(), Integer.parseInt(getListIndicadorAlvara().get(indexIndicadorAlvara).getDescription())));
            } else {
                rais.setIndicadorAlvara(null);
            }
        } else {
            rais.setIndicadorAlvara(null);
        }

        di.openTransaction();
        if (rais.getSisPessoa().getId() == -1) {
            if (rais.getSisPessoa().getTipoDocumento().getId() == -1) {
                rais.getSisPessoa().setTipoDocumento((TipoDocumento) di.find(new TipoDocumento(), 1));
            }
            if (rais.getSisPessoa().getEndereco().getId() == -1) {
                rais.getSisPessoa().setEndereco(null);
            }
            if (!di.save(rais.getSisPessoa())) {
                GenericaMensagem.warn("Erro", "Ao adicionar registro!");
                di.rollback();
                return;
            }
        } else {
            if (!di.update(rais.getSisPessoa())) {
                GenericaMensagem.warn("Erro", "Ao atualizar registro!");
                di.rollback();
                return;
            }
        }
        if (rais.getId() == -1) {
            RaisDao raisDao = new RaisDao();
            if (raisDao.existeCadastroAno(rais)) {
                di.rollback();
                GenericaMensagem.warn("Validação", "Certificado Rais já cadastrado para o ano atual, empresa e data de admissão!");
                return;
            }
            if (di.save(rais)) {
                di.commit();
                GenericaMensagem.info("Sucesso", "Registro adicionado");
            } else {
                di.rollback();
                GenericaMensagem.warn("Erro", "Ao adicionar registro!");
            }
        } else {
            if (di.update(rais)) {
                di.commit();
                GenericaMensagem.info("Sucesso", "Registro atualizado");
            } else {
                di.rollback();
                GenericaMensagem.warn("Erro", "Ao atualizar registro!");
            }
        }
    }

//    public String save() {
//        if (rais.getSisPessoa().getNome().equals("")) {
//            GenericaMensagem.warn("Validação", "Informar nome!");
//            return "rais";
//        }
//        if (rais.getSisPessoa().getDocumento().equals("")) {
//            GenericaMensagem.warn("Validação", "Informar o documento!");
//            return "rais";
//        }
//        if (rais.getSisPessoa().getNascimento().equals("")) {
//            GenericaMensagem.warn("Validação", "Informar data de nascimento!");
//            return "rais";
//        }
//        if (rais.getEmpresa().getId() == -1) {
//            GenericaMensagem.warn("Validação", "Pesquisar empresa!");
//            return "rais";
//        }
//        if (rais.getAdmissaoString().equals("")) {
//            GenericaMensagem.warn("Validação", "Informar a data de admissão!");
//            return "rais";
//        }
//        if (!rais.getDemissaoString().equals("")) {
//            int admissao = DataHoje.converteDataParaInteger(rais.getAdmissaoString());
//            int demissao = DataHoje.converteDataParaInteger(rais.getDemissaoString());
//            if (demissao < admissao) {
//                GenericaMensagem.warn("Validação", "Demissão deve ser superior a data de admissão!");
//                return "rais";
//            }
//        }
//        if (!rais.getAfastamentoString().equals("")) {
//            int admissao = DataHoje.converteDataParaInteger(rais.getAdmissaoString());
//            int afastamento = DataHoje.converteDataParaInteger(rais.getAfastamentoString());
//            if (afastamento < admissao) {
//                GenericaMensagem.warn("Validação", "A data de afastamento deve ser superior a data de admissão!");
//                return "rais";
//            }
//            if (rais.getMotivoAfastamento().equals("")) {
//                GenericaMensagem.warn("Validação", "Informar o motivo do afastamento!");
//                return "rais";
//            }
//        }
//        if (rais.getProfissao().getId() == -1) {
//            GenericaMensagem.warn("Validação", "Informa profissão!");
//            return "rais";
//        }
//        DaoInterface di = new Dao();
//        if (Integer.parseInt(getListNacionalidade().get(indexNacionalidade).getDescription()) != 0) {
//            rais.setNacionalidade((Nacionalidade) di.find(new Nacionalidade(), Integer.parseInt(getListNacionalidade().get(indexNacionalidade).getDescription())));
//        } else {
//            rais.setNacionalidade(null);
//        }
//
//        if (Integer.parseInt(getListEscolaridade().get(indexEscolaridade).getDescription()) != 0) {
//            rais.setEscolaridade((Escolaridade) di.find(new Escolaridade(), Integer.parseInt(getListEscolaridade().get(indexEscolaridade).getDescription())));
//        } else {
//            rais.setEscolaridade(null);
//        }
//
//        if (Integer.parseInt(getListClassificacaoEconomica().get(indexClassificacaoEconomica).getDescription()) != 0) {
//            rais.setRaca((Raca) di.find(new Raca(), Integer.parseInt(getListRaca().get(indexRaca).getDescription())));
//        } else {
//            rais.setRaca(null);
//        }
//
//        rais.setTipoDeficiencia((TipoDeficiencia) di.find(new TipoDeficiencia(), Integer.parseInt(getListTipoDeficiencia().get(indexTipoDeficiencia).getDescription())));
//        rais.setTipoRemuneracao((TipoRemuneracao) di.find(new TipoRemuneracao(), Integer.parseInt(getListTipoRemuneracao().get(indexTipoRemuneracao).getDescription())));
//        if (rais.isAlvara()) {
//            rais.setIndicadorAlvara((IndicadorAlvara) di.find(new IndicadorAlvara(), Integer.parseInt(getListIndicadorAlvara().get(indexIndicadorAlvara).getDescription())));
//        } else {
//            rais.setIndicadorAlvara(null);
//        }
//        di.openTransaction();
//        if (rais.getSisPessoa().getId() == -1) {
//            if (rais.getSisPessoa().getTipoDocumento().getId() == -1) {
//                rais.getSisPessoa().setTipoDocumento((TipoDocumento) di.find(new TipoDocumento(), 1));
//            }
//            if (rais.getSisPessoa().getEndereco().getId() == -1) {
//                rais.getSisPessoa().setEndereco(null);
//            }
//            if (!di.save(rais.getSisPessoa())) {
//                GenericaMensagem.warn("Erro", "Ao adicionar registro!");
//                di.rollback();
//                return "rais";
//            }
//        } else {
//            if (!di.update(rais.getSisPessoa())) {
//                GenericaMensagem.warn("Erro", "Ao atualizar registro!");
//                di.rollback();
//                return "rais";
//            }
//        }
//        if (rais.getId() == -1) {
//            RaisDao raisDao = new RaisDao();
//            if (raisDao.existeCadastroAno(rais)) {
//                GenericaMensagem.warn("Validação", "Certificado Rais já cadastrado para o ano atual!");
//                return "rais";
//            }
//            if (di.save(rais)) {
//                di.commit();
//                GenericaMensagem.info("Sucesso", "Registro adicionado");
//            } else {
//                di.rollback();
//                GenericaMensagem.warn("Erro", "Ao adicionar registro!");
//            }
//        } else {
//            if (di.update(rais)) {
//                di.commit();
//                GenericaMensagem.info("Sucesso", "Registro atualizado");
//            } else {
//                di.rollback();
//                GenericaMensagem.warn("Erro", "Ao atualizar registro!");
//            }
//        }
//        return "rais";
//    }
    public void delete() {
        if (rais.getId() != -1) {
            DaoInterface di = new Dao();
            di.openTransaction();
            if (di.delete(rais)) {
                di.commit();
                GenericaMensagem.info("Sucesso", "Registro excluído");
            } else {
                di.rollback();
                GenericaMensagem.warn("Erro", "Ao excluir registro!");
            }
            GenericaSessao.remove("raisBean");
        } else {
            GenericaMensagem.warn("Validação", "Nenhum registro encontrado!");
        }
    }

    public String searchSisPessoa() {
        if (rais.getId() != -1) {
            return null;
        }
        if (!rais.getSisPessoa().getNome().isEmpty() && !rais.getSisPessoa().getNascimento().isEmpty() && rais.getSisPessoa().getDocumento().isEmpty()) {
        } else if (!rais.getSisPessoa().getDocumento().isEmpty()) {
            if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(rais.getSisPessoa().getDocumento()))) {
                GenericaMensagem.warn("Validação", "Documento Inválido! Documento nº" + rais.getSisPessoa().getDocumento());
                PF.openDialog("dlg_message");
                rais.getSisPessoa().setDocumento("");
                return null;
            }
        } else {
            return null;
        }
        SisPessoaDao spdb = new SisPessoaDao();
        SisPessoa sp = new SisPessoa();
        sp = rais.getSisPessoa();
        if (sp.getId() == -1) {
            sp = spdb.sisPessoaExiste(rais.getSisPessoa(), true);
            if (sp == null || sp.getId() == -1) {
                sp = new SisPessoa();
                sp = spdb.sisPessoaExiste(rais.getSisPessoa());
                if (sp == null || sp.getId() == -1) {
                    sp = new SisPessoa();
                }
            }
        }
        if (sp.getId() != -1) {
            rais.setSisPessoa(sp);
        }
//        RaisDao raisDao = new RaisDao();
//        if (raisDao.existeCadastroAno(rais)) {
//            GenericaMensagem.warn("Validação", "Pessoa já Cadastrada!");
//            PF.openDialog("dlg_message");
//            rais = new Rais();
//            return null;
//        }
        //PF.update("form_rais:");
        return null;
    }

    public String edit(Rais r) {
        rais = r;
        if (r.getNacionalidade() != null) {
            for (int i = 0; i < getListNacionalidade().size(); i++) {
                if (r.getNacionalidade().getId() == Integer.parseInt(listNacionalidade.get(i).getDescription())) {
                    indexNacionalidade = i;
                    break;
                }
            }
        } else {
            indexNacionalidade = 0;
        }

        if (r.getEscolaridade() != null) {
            for (int i = 0; i < getListEscolaridade().size(); i++) {
                if (r.getEscolaridade().getId() == Integer.parseInt(listEscolaridade.get(i).getDescription())) {
                    indexEscolaridade = i;
                    break;
                }
            }
        } else {
            indexEscolaridade = 0;
        }

        if (r.getRaca() != null) {
            for (int i = 0; i < getListRaca().size(); i++) {
                if (r.getRaca().getId() == Integer.parseInt(listRaca.get(i).getDescription())) {
                    indexRaca = i;
                    break;
                }
            }
        } else {
            indexRaca = 0;
        }

        if (r.getClassificacaoEconomica() != null) {
            for (int i = 0; i < getListClassificacaoEconomica().size(); i++) {
                if (r.getClassificacaoEconomica().getId() == Integer.parseInt(listClassificacaoEconomica.get(i).getDescription())) {
                    indexClassificacaoEconomica = i;
                    break;
                }
            }
        } else {
            indexClassificacaoEconomica = 0;
        }

        if (r.getTipoRemuneracao() != null) {
            for (int i = 0; i < getListTipoRemuneracao().size(); i++) {
                if (r.getTipoRemuneracao().getId() == Integer.parseInt(listTipoRemuneracao.get(i).getDescription())) {
                    indexTipoRemuneracao = i;
                    break;
                }
            }
        } else {
            indexTipoRemuneracao = 0;
        }

        if (r.getIndicadorAlvara() != null) {
            for (int i = 0; i < getListIndicadorAlvara().size(); i++) {
                if (r.getIndicadorAlvara().getId() == Integer.parseInt(listIndicadorAlvara.get(i).getDescription())) {
                    indexIndicadorAlvara = i;
                    break;
                }
            }
        } else {
            indexIndicadorAlvara = 0;
        }

        if (r.getTipoDeficiencia() != null) {
            for (int i = 0; i < getListTipoDeficiencia().size(); i++) {
                if (r.getTipoDeficiencia().getId() == Integer.parseInt(listTipoDeficiencia.get(i).getDescription())) {
                    indexTipoDeficiencia = i;
                    break;
                }
            }
        } else {
            indexTipoDeficiencia = 0;
        }

        GenericaSessao.put("linkClicado", true);
        return "rais";
    }

    public Rais getRais() {
        if (GenericaSessao.exists("sisPessoaPesquisa")) {
            rais.setSisPessoa((SisPessoa) GenericaSessao.getObject("sisPessoaPesquisa", true));
        }
        if (GenericaSessao.exists("fisicaPesquisa")) {
            Fisica f = (Fisica) GenericaSessao.getObject("fisicaPesquisa", true);
            SisPessoa sp = new SisPessoa();
            sp.setDocumento(f.getPessoa().getDocumento());
            sp.setNome(f.getPessoa().getNome());
            sp.setNascimento(f.getNascimento());
            SisPessoaDao spdb = new SisPessoaDao();
            if (sp.getId() == -1) {
                sp = spdb.sisPessoaExiste(sp, true);
                if (sp == null || sp.getId() == -1) {
                    sp = spdb.sisPessoaExiste(sp);
                    if (sp == null || sp.getId() == -1) {
                        sp = new SisPessoa();
                    }
                }
            }
            if (sp.getId() == -1) {
                rais.setCarteira(f.getCarteira());
                rais.setCarteira(f.getCarteira());
                rais.setPis(f.getPis());
                rais.setSerie(f.getSerie());
                rais.getSisPessoa().setTipoDocumento(f.getPessoa().getTipoDocumento());
                rais.getSisPessoa().setRg(f.getRg());
                rais.getSisPessoa().setTelefone(f.getPessoa().getTelefone1());
                rais.getSisPessoa().setNome(f.getPessoa().getNome());
                rais.getSisPessoa().setDocumento(f.getPessoa().getDocumento());
                rais.getSisPessoa().setNascimento(f.getNascimento());
                rais.getSisPessoa().setSexo(f.getSexo());
                PessoaEnderecoDB pedb = new PessoaEnderecoDBToplink();
                PessoaEndereco pe = pedb.pesquisaEndPorPessoaTipo(f.getPessoa().getId(), 1);
                if (pe != null && pe.getId() != -1) {
                    rais.getSisPessoa().setEndereco(pe.getEndereco());
                } else {
                    rais.getSisPessoa().setEndereco(null);
                }
            } else {
                rais.setSisPessoa(sp);
            }
        }
        if (GenericaSessao.exists("juridicaPesquisa")) {
            rais.setEmpresa((Juridica) GenericaSessao.getObject("juridicaPesquisa", true));
        }
        return rais;
    }

    public void setRais(Rais rais) {
        this.rais = rais;
    }

    public List<Rais> getListRais() {
        if (listRais.isEmpty()) {
            RaisDao dao = new RaisDao();
            if (removeFiltro) {
                if (porPesquisa.equals("todos")) {
                    return new ArrayList();
                }
            }
            listRais = dao.pesquisa(descricaoPesquisa, porPesquisa, comoPesquisa);
        }
        return listRais;
    }

    public void setListRais(List<Rais> listRais) {
        this.listRais = listRais;
    }

    public List<SelectItem> getListNacionalidade() {
        if (listNacionalidade.isEmpty()) {
            DaoInterface di = new Dao();
            List<Nacionalidade> list = di.list("Nacionalidade");
            listNacionalidade.add(new SelectItem(0, "Selecionar", "0"));
            for (int i = 0; i < list.size(); i++) {
                listNacionalidade.add(new SelectItem(i + 1, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        return listNacionalidade;
    }

    public void setListNacionalidade(List<SelectItem> listNacionalidade) {
        this.listNacionalidade = listNacionalidade;
    }

    public List<SelectItem> getListEscolaridade() {
        if (listEscolaridade.isEmpty()) {
            DaoInterface di = new Dao();
            List<Escolaridade> list = di.list("Escolaridade");
            if (!list.isEmpty()) {
                listEscolaridade.add(new SelectItem(0, "Selecionar", "0"));
                for (int i = 0; i < list.size(); i++) {
                    listEscolaridade.add(new SelectItem(i + 1, list.get(i).getDescricao(), "" + list.get(i).getId()));
                    indexEscolaridade = i + 1;
                }
            } else {
                listEscolaridade.add(new SelectItem(0, "Nenhuma Escolaridade Encontrada", "0"));
            }
        }
        return listEscolaridade;
    }

    public void setListEscolaridade(List<SelectItem> listEscolaridade) {
        this.listEscolaridade = listEscolaridade;
    }

    public List<SelectItem> getListRaca() {
        if (listRaca.isEmpty()) {
            DaoInterface di = new Dao();
            List<Raca> list = di.list("Raca");
            listRaca.add(new SelectItem(0, "Selecionar", "0"));
            for (int i = 0; i < list.size(); i++) {
                listRaca.add(new SelectItem(i + 1, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        return listRaca;
    }

    public void setListRaca(List<SelectItem> listRaca) {
        this.listRaca = listRaca;
    }

    public List<SelectItem> getListClassificacaoEconomica() {
        if (listClassificacaoEconomica.isEmpty()) {
            DaoInterface di = new Dao();
            List<ClassificacaoEconomica> list = di.list("ClassificacaoEconomica");
            listClassificacaoEconomica.add(new SelectItem(0, "Selecionar", "0"));

            for (int i = 0; i < list.size(); i++) {
                String numInicial = "";
                String numFinal = "";
                if (list.get(i).getSalarioMinimoInicial() == 0 && list.get(i).getSalarioMinimoFinal() == 0) {
                    numInicial = "--";
                    numFinal = "--";
                } else if (list.get(i).getSalarioMinimoFinal() == 0) {
                    numInicial = "" + list.get(i).getSalarioMinimoInicial();
                    numFinal = "*";
                } else {
                    numInicial = "" + list.get(i).getSalarioMinimoInicial();
                    numFinal = "" + list.get(i).getSalarioMinimoFinal();
                }
                listClassificacaoEconomica.add(new SelectItem(i + 1,
                        "Classe: " + list.get(i).getDescricao()
                        + " - De: " + numInicial
                        + " à  " + numFinal + " - (SM)",
                        "" + list.get(i).getId()));
            }
        }
        return listClassificacaoEconomica;
    }

    public void setListClassificacaoEconomica(List<SelectItem> listClassificacaoEconomica) {
        this.listClassificacaoEconomica = listClassificacaoEconomica;
    }

    public List<SelectItem> getListTipoRemuneracao() {
        if (listTipoRemuneracao.isEmpty()) {
            DaoInterface di = new Dao();
            List<TipoRemuneracao> list = di.list("TipoRemuneracao");
            for (int i = 0; i < list.size(); i++) {
                listTipoRemuneracao.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        return listTipoRemuneracao;
    }

    public void setListTipoRemuneracao(List<SelectItem> listTipoRemuneracao) {
        this.listTipoRemuneracao = listTipoRemuneracao;
    }

    public List<SelectItem> getListIndicadorAlvara() {
        if (listIndicadorAlvara.isEmpty()) {
            DaoInterface di = new Dao();
            List<IndicadorAlvara> list = di.list("IndicadorAlvara");
            for (int i = 0; i < list.size(); i++) {
                listIndicadorAlvara.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        return listIndicadorAlvara;
    }

    public void setListIndicadorAlvara(List<SelectItem> listIndicadorAlvara) {
        this.listIndicadorAlvara = listIndicadorAlvara;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getIndexNacionalidade() {
        return indexNacionalidade;
    }

    public void setIndexNacionalidade(Integer indexNacionalidade) {
        this.indexNacionalidade = indexNacionalidade;
    }

    public Integer getIndexEscolaridade() {
        return indexEscolaridade;
    }

    public void setIndexEscolaridade(Integer indexEscolaridade) {
        this.indexEscolaridade = indexEscolaridade;
    }

    public Integer getIndexRaca() {
        return indexRaca;
    }

    public void setIndexRaca(Integer indexRaca) {
        this.indexRaca = indexRaca;
    }

    public Integer getIndexClassificacaoEconomica() {
        return indexClassificacaoEconomica;
    }

    public void setIndexClassificacaoEconomica(Integer indexClassificacaoEconomica) {
        this.indexClassificacaoEconomica = indexClassificacaoEconomica;
    }

    public Integer getIndexTipoRemuneracao() {
        return indexTipoRemuneracao;
    }

    public void setIndexTipoRemuneracao(Integer indexTipoRemuneracao) {
        this.indexTipoRemuneracao = indexTipoRemuneracao;
    }

    public Integer getIndexIndicadorAlvara() {
        return indexIndicadorAlvara;
    }

    public void setIndexIndicadorAlvara(Integer indexIndicadorAlvara) {
        this.indexIndicadorAlvara = indexIndicadorAlvara;
    }

    public List<SelectItem> getListTipoDeficiencia() {
        if (listTipoDeficiencia.isEmpty()) {
            DaoInterface di = new Dao();
            List<TipoDeficiencia> list = di.list("TipoDeficiencia");
            //listTipoDeficiencia.add(new SelectItem(0, "Selecionar", "0"));
            for (int i = 0; i < list.size(); i++) {
                listTipoDeficiencia.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        return listTipoDeficiencia;
    }

    public void setListTipoDeficiencia(List<SelectItem> listTipoDeficiencia) {
        this.listTipoDeficiencia = listTipoDeficiencia;
    }

    public Integer getIndexTipoDeficiencia() {
        return indexTipoDeficiencia;
    }

    public void setIndexTipoDeficiencia(Integer indexTipoDeficiencia) {
        this.indexTipoDeficiencia = indexTipoDeficiencia;
    }

    public SalarioMinimo getSalarioMinimo() {
        return salarioMinimo;
    }

    public void setSalarioMinimo(SalarioMinimo salarioMinimo) {
        this.salarioMinimo = salarioMinimo;
    }

    public String getAno() {
        //return String.valueOf(Integer.valueOf(DataHoje.livre(rais.getEmissao(), "Y")) - 1);
        return String.valueOf(Integer.valueOf(DataHoje.livre(rais.getEmissao(), "Y")));
    }

    public void acaoPesquisaInicial() {
        listRais.clear();
        setComoPesquisa("Inicial");
    }

    public void acaoPesquisaParcial() {
        listRais.clear();
        setComoPesquisa("Parcial");
    }

    public String getPorPesquisa() {
        if (porPesquisa.equals("todos")) {
            descricaoPesquisa = "";
        }
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public String getDescricaoPesquisa() {
        return descricaoPesquisa;
    }

    public void setDescricaoPesquisa(String descricaoPesquisa) {
        this.descricaoPesquisa = descricaoPesquisa;
    }

    public String getComoPesquisa() {
        return comoPesquisa;
    }

    public void setComoPesquisa(String comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    public boolean isRemoveFiltro() {
        return removeFiltro;
    }

    public void setRemoveFiltro(boolean removeFiltro) {
        this.removeFiltro = removeFiltro;
    }

    public String getMascara() {
        return Mask.getMascaraPesquisa(porPesquisa, true);
    }

    public void listenerActiveIndex(TabChangeEvent event) {
        setActiveIndex((Integer) ((TabView) event.getComponent()).getActiveIndex());
    }

    public Integer getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(Integer activeIndex) {
        this.activeIndex = activeIndex;
    }

    public String getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(String dataEmissao) {
        this.dataEmissao = dataEmissao;
    }
}
