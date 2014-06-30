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
import br.com.rtools.sistema.db.SisPessoaDB;
import br.com.rtools.sistema.db.SisPessoaDBToplink;
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
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

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
    private int indexNacionalidade;
    private int indexEscolaridade;
    private int indexRaca;
    private int indexClassificacaoEconomica;
    private int indexTipoRemuneracao;
    private int indexIndicadorAlvara;
    private int indexTipoDeficiencia;
    private String message;
    private String porPesquisa;
    private String descricaoPesquisa;
    private String comoPesquisa;
    private boolean removeFiltro;

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
        message = "";
        porPesquisa = "todos";
        descricaoPesquisa = "";
        comoPesquisa = "";
        removeFiltro = false;
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("raisBean");
    }

    public void clear() {
        GenericaSessao.remove("raisBean");
    }

    public void save() {
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
        rais.setNacionalidade((Nacionalidade) di.find(new Nacionalidade(), Integer.parseInt(getListNacionalidade().get(indexNacionalidade).getDescription())));
        rais.setEscolaridade((Escolaridade) di.find(new Escolaridade(), Integer.parseInt(getListEscolaridade().get(indexEscolaridade).getDescription())));
        rais.setRaca((Raca) di.find(new Raca(), Integer.parseInt(getListRaca().get(indexRaca).getDescription())));
        rais.setClassificacaoEconomica((ClassificacaoEconomica) di.find(new ClassificacaoEconomica(), Integer.parseInt(getListClassificacaoEconomica().get(indexClassificacaoEconomica).getDescription())));
        rais.setTipoDeficiencia((TipoDeficiencia) di.find(new TipoDeficiencia(), Integer.parseInt(getListTipoDeficiencia().get(indexTipoDeficiencia).getDescription())));
        rais.setTipoRemuneracao((TipoRemuneracao) di.find(new TipoRemuneracao(), Integer.parseInt(getListTipoRemuneracao().get(indexTipoRemuneracao).getDescription())));
        if (rais.isAlvara()) {
            rais.setIndicadorAlvara((IndicadorAlvara) di.find(new IndicadorAlvara(), Integer.parseInt(getListIndicadorAlvara().get(indexIndicadorAlvara).getDescription())));
        } else {
            rais.setIndicadorAlvara(null);
        }
        di.openTransaction();
        if (rais.getSisPessoa().getId() == -1) {
            if(rais.getSisPessoa().getTipoDocumento().getId() == -1) {
                rais.getSisPessoa().setTipoDocumento((TipoDocumento) di.find(new TipoDocumento(), 1));                
            }
            if(rais.getSisPessoa().getEndereco().getId() == -1) { 
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
                GenericaMensagem.warn("Validação", "Certificado Rais já cadastrado para o ano atual!");
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

    public void searchSisPessoa() {
        if(rais.getId() != -1) {
            return;
        }
        if (!rais.getSisPessoa().getNome().isEmpty() && !rais.getSisPessoa().getNascimento().isEmpty() && rais.getSisPessoa().getDocumento().isEmpty()) {
        } else if (!rais.getSisPessoa().getDocumento().isEmpty()) {
            if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(rais.getSisPessoa().getDocumento()))) {
                GenericaMensagem.warn("Validação", "Documento Inválido! Documento nº" + rais.getSisPessoa().getDocumento());
                PF.openDialog("dlg_message");
                rais.getSisPessoa().setDocumento("");
                return;
            }
        } else {
            return;
        }
        SisPessoaDB spdb = new SisPessoaDBToplink();
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
        if(sp.getId() != -1) {
            rais.setSisPessoa(sp);
        }
    }

    public String edit(Rais r) {
        rais = r;
        for (int i = 0; i < listNacionalidade.size(); i++) {
            if (r.getNacionalidade().getId() == Integer.parseInt(listNacionalidade.get(i).getDescription())) {
                indexNacionalidade = i;
                break;
            }
        }
        for (int i = 0; i < listEscolaridade.size(); i++) {
            if (r.getEscolaridade().getId() == Integer.parseInt(listEscolaridade.get(i).getDescription())) {
                indexEscolaridade = i;
                break;
            }

        }
        for (int i = 0; i < listRaca.size(); i++) {
            if (r.getRaca().getId() == Integer.parseInt(listRaca.get(i).getDescription())) {
                indexRaca = i;
                break;
            }
        }
        for (int i = 0; i < listClassificacaoEconomica.size(); i++) {
            if (r.getClassificacaoEconomica().getId() == Integer.parseInt(listClassificacaoEconomica.get(i).getDescription())) {
                indexClassificacaoEconomica = i;
                break;
            }
        }
        for (int i = 0; i < listTipoRemuneracao.size(); i++) {
            if (r.getTipoRemuneracao().getId() == Integer.parseInt(listTipoRemuneracao.get(i).getDescription())) {
                indexTipoRemuneracao = i;
                break;
            }
        }
        for (int i = 0; i < listIndicadorAlvara.size(); i++) {
            if (r.getIndicadorAlvara().getId() == Integer.parseInt(listIndicadorAlvara.get(i).getDescription())) {
                indexIndicadorAlvara = i;
                break;
            }
        }
        for (int i = 0; i < listTipoDeficiencia.size(); i++) {
            if (r.getTipoDeficiencia().getId() == Integer.parseInt(listTipoDeficiencia.get(i).getDescription())) {
                indexTipoDeficiencia = i;
                break;
            }
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
            SisPessoaDB spdb = new SisPessoaDBToplink();
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
            for (int i = 0; i < list.size(); i++) {
                listNacionalidade.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
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
            for (int i = 0; i < list.size(); i++) {
                listEscolaridade.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
                indexEscolaridade = i;
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
            for (int i = 0; i < list.size(); i++) {
                listRaca.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
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
                listClassificacaoEconomica.add(new SelectItem(i,
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

    public int getIndexNacionalidade() {
        return indexNacionalidade;
    }

    public void setIndexNacionalidade(int indexNacionalidade) {
        this.indexNacionalidade = indexNacionalidade;
    }

    public int getIndexEscolaridade() {
        return indexEscolaridade;
    }

    public void setIndexEscolaridade(int indexEscolaridade) {
        this.indexEscolaridade = indexEscolaridade;
    }

    public int getIndexRaca() {
        return indexRaca;
    }

    public void setIndexRaca(int indexRaca) {
        this.indexRaca = indexRaca;
    }

    public int getIndexClassificacaoEconomica() {
        return indexClassificacaoEconomica;
    }

    public void setIndexClassificacaoEconomica(int indexClassificacaoEconomica) {
        this.indexClassificacaoEconomica = indexClassificacaoEconomica;
    }

    public int getIndexTipoRemuneracao() {
        return indexTipoRemuneracao;
    }

    public void setIndexTipoRemuneracao(int indexTipoRemuneracao) {
        this.indexTipoRemuneracao = indexTipoRemuneracao;
    }

    public int getIndexIndicadorAlvara() {
        return indexIndicadorAlvara;
    }

    public void setIndexIndicadorAlvara(int indexIndicadorAlvara) {
        this.indexIndicadorAlvara = indexIndicadorAlvara;
    }

    public List<SelectItem> getListTipoDeficiencia() {
        if (listTipoDeficiencia.isEmpty()) {
            DaoInterface di = new Dao();
            List<TipoDeficiencia> list = di.list("TipoDeficiencia");
            for (int i = 0; i < list.size(); i++) {
                listTipoDeficiencia.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        return listTipoDeficiencia;
    }

    public void setListTipoDeficiencia(List<SelectItem> listTipoDeficiencia) {
        this.listTipoDeficiencia = listTipoDeficiencia;
    }

    public int getIndexTipoDeficiencia() {
        return indexTipoDeficiencia;
    }

    public void setIndexTipoDeficiencia(int indexTipoDeficiencia) {
        this.indexTipoDeficiencia = indexTipoDeficiencia;
    }

    public SalarioMinimo getSalarioMinimo() {
        return salarioMinimo;
    }

    public void setSalarioMinimo(SalarioMinimo salarioMinimo) {
        this.salarioMinimo = salarioMinimo;
    }

    public String getAno() {
        return DataHoje.livre(new Date(), "Y");
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
}
