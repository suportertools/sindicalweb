package br.com.rtools.associativo.beans;

import br.com.rtools.financeiro.FTipoDocumento;
import br.com.rtools.financeiro.ServicoPessoa;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.*;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.PessoaEmpresa;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.db.*;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.rtools.seguranca.db.RotinaDB;
import br.com.rtools.seguranca.db.RotinaDBToplink;
import br.com.rtools.utilitarios.AnaliseString;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
@SessionScoped
public class ServicoPessoaBean implements Serializable {

    protected String indexTab;
    protected String strEndereco;
    protected ServicoPessoa servicoPessoa;
    protected Fisica titular;
    protected PessoaEndereco titularEndereco;
    protected boolean chkContaCobranca;
    protected boolean renderServicos;
    protected int idTipoDocumento;
    protected Integer idServico;
    protected List<SelectItem> listaTipoDocumento;
    protected List<SelectItem> listaServicos;
    //protected FisicaJSFBean fisicaJSFBean;

    public ServicoPessoaBean() {
        indexTab = "socios";
        strEndereco = "";
        titular = new Fisica();
        titularEndereco = new PessoaEndereco();
        servicoPessoa = new ServicoPessoa();
        chkContaCobranca = false;
        renderServicos = true;
        idTipoDocumento = 0;
        idServico = null;
        listaTipoDocumento = new ArrayList();
        listaServicos = new ArrayList();
    }

    public void pesquisaFisica() {
        Fisica fis = (Fisica) GenericaSessao.getObject("fisicaPesquisa", true);
        if (fis != null) {
            PessoaEnderecoDB db = new PessoaEnderecoDBToplink();
            titular = fis;
            titularEndereco = db.pesquisaEndPorPessoaTipo(fis.getPessoa().getId(), 3);
            servicoPessoa.setPessoa(fis.getPessoa());
        }
    }

    public String chamadaPesquisa() {
        return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).pesquisaPessoaFisica();
    }

    public void editar(ServicoPessoa servicoPessoa) {
        int qntTipoDocumento = getListaTipoDocumento().size();
        int qntServicos = getListaServicos().size();
        chkContaCobranca = servicoPessoa.isBanco();
        this.servicoPessoa = servicoPessoa;

        FisicaDB db = new FisicaDBToplink();
        titular = db.pesquisaFisicaPorPessoa(servicoPessoa.getPessoa().getId());

        PessoaEnderecoDB dbe = new PessoaEnderecoDBToplink();
        titularEndereco = dbe.pesquisaEndPorPessoaTipo(titular.getPessoa().getId(), 3);

        for (int i = 0; i < qntTipoDocumento; i++) {
            if (Integer.parseInt((String) getListaTipoDocumento().get(i).getDescription()) == servicoPessoa.getTipoDocumento().getId()) {
                setIdTipoDocumento(i);
                break;
            }
        }

        for (int i = 0; i < qntServicos; i++) {
            if (Integer.parseInt((String) getListaServicos().get(i).getDescription()) == servicoPessoa.getServicos().getId()) {
                setIdServico(i);
                break;
            }
        }
    }

    public String getStrEndereco() {
        if (titular.getId() != -1) {
            if (titularEndereco != null && titularEndereco.getId() != -1) {
                String strCompl = "";
                if (titularEndereco.getComplemento().equals("")) {
                    strCompl = " ";
                } else {
                    strCompl = " ( " + titularEndereco.getComplemento() + " ) ";
                }

                strEndereco = titularEndereco.getEndereco().getLogradouro().getDescricao() + " "
                        + titularEndereco.getEndereco().getDescricaoEndereco().getDescricao() + ", " + titularEndereco.getNumero() + " " + titularEndereco.getEndereco().getBairro().getDescricao() + ","
                        + strCompl + titularEndereco.getEndereco().getCidade().getCidade() + " - " + titularEndereco.getEndereco().getCidade().getUf() + " - " + AnaliseString.mascaraCep(titularEndereco.getEndereco().getCep());
            } else {
                strEndereco = " NENHUM ";
            }
        } else {
            titularEndereco = new PessoaEndereco();
            strEndereco = "";
        }
        return strEndereco;
    }

    public void setStrEndereco(String strEndereco) {
        this.strEndereco = strEndereco;
    }

    public List<SelectItem> getListaTipoDocumento() {
        if (listaTipoDocumento.isEmpty()) {
            FTipoDocumentoDB db = new FTipoDocumentoDBToplink();
            List<FTipoDocumento> select = new ArrayList();
            if (isChkContaCobranca()) {
                select.add(db.pesquisaCodigo(2));
            } else {
                select = db.pesquisaListaTipoExtrato();
            }
            for (int i = 0; i < select.size(); i++) {
                listaTipoDocumento.add(new SelectItem(i,
                        (String) (select.get(i).getDescricao()),
                        Integer.toString(select.get(i).getId())));
            }
        }
        return listaTipoDocumento;
    }

    public List<SelectItem> getListaServicos() {
        if (listaServicos.isEmpty()) {
            int idRotina = 0;
            ServicosDB db = new ServicosDBToplink();
            RotinaDB dbr = new RotinaDBToplink();
            idRotina = ((Rotina) dbr.pesquisaPaginaRotina(getRefreshPagina())).getId();
            List<Servicos> select = db.pesquisaTodos(idRotina);
            for (int i = 0; i < select.size(); i++) {
                listaServicos.add(new SelectItem(i, (String) select.get(i).getDescricao(), Integer.toString(select.get(i).getId())));
            }
        }
        return listaServicos;
    }

    public String salvarServicoPessoa(Servicos servico, SalvarAcumuladoDB dbSalvar) {
        ServicosDB dbServico = new ServicosDBToplink();
        ServicoContaCobrancaDB dbSCB = new ServicoContaCobrancaDBToplink();
        FTipoDocumentoDB dbFTipo = new FTipoDocumentoDBToplink();

        // --------------------------------------------
        if (getListaServicos().isEmpty()) {
            return "Cadastrar serviços!";
        }
        if (servico == null) {
            servicoPessoa.setServicos(dbServico.pesquisaCodigo(Integer.parseInt(getListaServicos().get(idServico).getDescription())));
        } else {
            servicoPessoa.setServicos(dbServico.pesquisaCodigo(servico.getId()));
        }
        try {
            servicoPessoa.setTipoDocumento(dbFTipo.pesquisaCodigo(Integer.parseInt(getListaTipoDocumento().get(idTipoDocumento).getDescription())));
        } catch (Exception e) {
            servicoPessoa.setTipoDocumento(dbFTipo.pesquisaCodigo(Integer.parseInt(getListaTipoDocumento().get(0).getDescription())));
        }

        if (chkContaCobranca) {
            List l = dbSCB.pesquisaServPorIdServIdTipoServ(servicoPessoa.getServicos().getId(), 1);
            if (!l.isEmpty()) {
                servicoPessoa.setBanco(true);
            } else {
                servicoPessoa.setBanco(false);
                return "Não Existe serviço conta cobrança!";
            }
        } else {
            servicoPessoa.setBanco(false);
        }

        PessoaEmpresaDB dbp = new PessoaEmpresaDBToplink();
        PessoaEmpresa pe = null;
        if (servicoPessoa.isDescontoFolha() && titular.getId() != -1) {
            pe = dbp.pesquisaPessoaEmpresaPorFisica(titular.getId());
            if (pe.getId() != -1) {
                servicoPessoa.setCobranca(pe.getJuridica().getPessoa());
            } else {
                servicoPessoa.setCobranca(servicoPessoa.getPessoa());
            }
        } else {
            servicoPessoa.setCobranca(servicoPessoa.getPessoa());
        }

        if (servicoPessoa.getPessoa().getId() != -1) {
            if (dbSalvar.inserirObjeto(servicoPessoa)) {
                return "";
            } else {
                return "Erro ao salvar serviço pessoa!";
            }
        } else {
            return "Não existe pessoa pesquisada";
        }
    }

    public String atualizarServicoPessoa(Servicos servico, SalvarAcumuladoDB dbSalvar) {
        ServicosDB dbServico = new ServicosDBToplink();
        ServicoContaCobrancaDB dbSCB = new ServicoContaCobrancaDBToplink();
        FTipoDocumentoDB dbFTipo = new FTipoDocumentoDBToplink();

        if (servico == null) {
            servicoPessoa.setServicos(dbServico.pesquisaCodigo(Integer.parseInt(getListaServicos().get(idServico).getDescription())));
        } else {
            servicoPessoa.setServicos(dbServico.pesquisaCodigo(servico.getId()));
        }
        // --------------------------------------------
        try {
            servicoPessoa.setTipoDocumento(dbFTipo.pesquisaCodigo(Integer.parseInt(getListaTipoDocumento().get(idTipoDocumento).getDescription())));
        } catch (Exception e) {
            servicoPessoa.setTipoDocumento(dbFTipo.pesquisaCodigo(Integer.parseInt(getListaTipoDocumento().get(0).getDescription())));
        }

        if (chkContaCobranca) {
            List l = dbSCB.pesquisaServPorIdServIdTipoServ(servicoPessoa.getServicos().getId(), 1);
            if (!l.isEmpty()) {
                servicoPessoa.setBanco(true);
            } else {
                servicoPessoa.setBanco(false);
                return "Não Existe serviço conta cobrança!";
            }
        } else {
            servicoPessoa.setBanco(false);
        }
        // --------------------------------------------

        PessoaEmpresaDB dbp = new PessoaEmpresaDBToplink();
        PessoaEmpresa pe = null;
        if (servicoPessoa.isDescontoFolha() && titular.getId() != -1) {
            pe = dbp.pesquisaPessoaEmpresaPorFisica(titular.getId());
            if (pe.getId() != -1) {
                servicoPessoa.setCobranca(pe.getJuridica().getPessoa());
            } else {
                servicoPessoa.setCobranca(servicoPessoa.getPessoa());
            }
        } else {
            servicoPessoa.setCobranca(servicoPessoa.getPessoa());
        }

        if (servicoPessoa.getPessoa().getId() != -1) {
            if (dbSalvar.alterarObjeto(servicoPessoa)) {
                return "";
            } else {
                return "Erro ao alterar serviço pessoa!";
            }
        } else {
            return "Não existe pessoa pesquisada";
        }
    }

    public String getRefreshPagina() {
        return converteURL(((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI());
    }

    public String converteURL(String urlDest) {
        String url = "";
        url = urlDest;
        int iniURL = url.lastIndexOf("/");
        int fimURL = url.lastIndexOf(".");
        return url.substring(iniURL + 1, fimURL);
    }

    public void refreshFormServico() {
    }

    public String getIndexTab() {
        return indexTab;
    }

    public void setIndexTab(String indexTab) {
        this.indexTab = indexTab;
    }

    public Fisica getTitular() {
        pesquisaFisica();
        return titular;
    }

    public void setTitular(Fisica titular) {
        this.titular = titular;
    }

    public PessoaEndereco getTitularEndereco() {
        return titularEndereco;
    }

    public void setTitularEndereco(PessoaEndereco titularEndereco) {
        this.titularEndereco = titularEndereco;
    }

    public ServicoPessoa getServicoPessoa() {
        return servicoPessoa;
    }

    public void setServicoPessoa(ServicoPessoa servicoPessoa) {
        this.servicoPessoa = servicoPessoa;
    }

    public boolean isChkContaCobranca() {
        return chkContaCobranca;
    }

    public void setChkContaCobranca(boolean chkContaCobranca) {
        listaTipoDocumento.clear();
        this.chkContaCobranca = chkContaCobranca;
    }

    public boolean isRenderServicos() {
        return renderServicos;
    }

    public void setRenderServicos(boolean renderServicos) {
        this.renderServicos = renderServicos;
    }

    public int getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(int idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public Integer getIdServico() {
        return idServico;
    }

    public void setIdServico(Integer idServico) {
        this.idServico = idServico;
    }
}
