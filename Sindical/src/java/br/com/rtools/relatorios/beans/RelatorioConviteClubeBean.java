package br.com.rtools.relatorios.beans;

import br.com.rtools.associativo.ConviteAutorizaCortesia;
import br.com.rtools.associativo.ConviteMovimento;
import br.com.rtools.associativo.db.ConviteDB;
import br.com.rtools.associativo.db.ConviteDBToplink;
import br.com.rtools.impressao.ParametroConviteClube;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.relatorios.dao.RelatorioDao;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.sistema.SisPessoa;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.GenericaString;
import br.com.rtools.utilitarios.Jasper;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;

@ManagedBean
@SessionScoped
public class RelatorioConviteClubeBean implements Serializable {

    private ConviteMovimento conviteMovimento = new ConviteMovimento();
    private Fisica fisica = new Fisica();
    private SisPessoa sisPessoa = new SisPessoa();
    private Usuario usuario = new Usuario();
    private List<ParametroConviteClube> parametroConviteClubes = new ArrayList<ParametroConviteClube>();
    private List<SelectItem> listaTipoRelatorios = new ArrayList<SelectItem>();
    private List<SelectItem> listaOperadores = new ArrayList<SelectItem>();
    private List<SelectItem> listaDiretores = new ArrayList<SelectItem>();
    private int idRelatorios = 0;
    private String idCortesia = "todos";
    private int idOperador = 0;
    private int idDiretor = 0;
    private Date dataEmissaoInicial = DataHoje.dataHoje();
    private Date dataEmissaoFinal = DataHoje.dataHoje();
    private Date dataValidadeInicial = DataHoje.dataHoje();
    private Date dataValidadeFinal = DataHoje.dataHoje();
    private boolean socio = false;
    private boolean diretor = false;
    private boolean operador = false;
    private boolean convidado = false;
    private boolean emissao = false;
    private boolean validade = false;
    private boolean cortesia = false;
    private String tipoRelatorio = "Simples";
    private String indexAccordion = "Simples";

    public void visualizar() {
        Relatorios relatorios = null;
        if(!getListaTipoRelatorios().isEmpty()) {
            RelatorioDao rgdb = new RelatorioDao();
            relatorios = rgdb.pesquisaRelatorios(Integer.parseInt(listaTipoRelatorios.get(idRelatorios).getDescription()));
        }
        if(relatorios == null) {
            return;
        }
        if (parametroConviteClubes.isEmpty()) {
            ConviteDB conviteDB = new ConviteDBToplink();
            int pSisPessoaI = 0;
            int pPessoaI = 0;
            int pDiretorI = 0;
            int pOperadorI = 0;
            String pEmissaoIStringI = "";
            String pEmissaoFStringI = "";
            String pValidadeIStringI = "";
            String pValidadeFStringI = "";
            if (convidado) {
                if (sisPessoa.getId() != -1) {
                    pSisPessoaI = sisPessoa.getId();
                }
            }
            if (socio) {
                if (fisica.getId() != -1) {
                    pPessoaI = fisica.getPessoa().getId();
                }
            }
            if (diretor) {
                if (!listaDiretores.isEmpty()) {
                    SalvarAcumuladoDB sadb = new SalvarAcumuladoDBToplink();
                    ConviteAutorizaCortesia cac = (ConviteAutorizaCortesia) sadb.pesquisaObjeto(Integer.parseInt(listaDiretores.get(idDiretor).getDescription()), "ConviteAutorizaCortesia");
                    if(cac != null) {
                        pDiretorI = cac.getPessoa().getId();                        
                    }
                }
            }
            if (operador) {
                if (!listaOperadores.isEmpty()) {
                    pOperadorI = Integer.parseInt(listaOperadores.get(idOperador).getDescription());
                }
            }
            if (emissao) {
                pEmissaoIStringI = DataHoje.converteData(dataEmissaoInicial);
                pEmissaoFStringI = DataHoje.converteData(dataEmissaoFinal);
            }
            if (validade) {
                pEmissaoIStringI = DataHoje.converteData(dataValidadeInicial);
                pEmissaoFStringI = DataHoje.converteData(dataValidadeFinal);
            }
            List list = conviteDB.filtroRelatorio(
                    pSisPessoaI,
                    pPessoaI,
                    pDiretorI,
                    pOperadorI,
                    pEmissaoIStringI,
                    pEmissaoFStringI,
                    pValidadeIStringI,
                    pValidadeFStringI,
                    idCortesia,
                    "",
                    relatorios
            );
            for (Object list1 : list) {
                String cortesiaString = GenericaString.converterNullToString(((List) list1).get(9));
                if(cortesiaString.equals("false")) {
                    cortesiaString = "";
                } else if (cortesiaString.equals("true")) {
                    cortesiaString = "Sim";                    
                }
                String valor = GenericaString.converterNullToString(((List) list1).get(7));
                if(valor.isEmpty()) {
                    valor = "0";
                }
                String valorPago = GenericaString.converterNullToString(((List) list1).get(8));
                if(valorPago.isEmpty()) {
                    valorPago = "0";
                }
                ParametroConviteClube parametroConviteClube
                        = new ParametroConviteClube(
                                GenericaString.converterNullToString(((List) list1).get(0)),
                                GenericaString.converterNullToString(((List) list1).get(1)),
                                GenericaString.converterNullToString(((List) list1).get(2)),
                                GenericaString.converterNullToString(((List) list1).get(3)),
                                GenericaString.converterNullToString(((List) list1).get(4)),
                                GenericaString.converterNullToString(((List) list1).get(5)),
                                GenericaString.converterNullToString(((List) list1).get(6)),
                                new BigDecimal(valor),
                                new BigDecimal(valorPago),
                                cortesiaString,
                                GenericaString.converterNullToString(((List) list1).get(10))
                        );
                parametroConviteClubes.add(parametroConviteClube);
            }

        }
        if (parametroConviteClubes.isEmpty()) {
            GenericaMensagem.info("Sistema", "Não existem registros para o relatório selecionado");
            return;
        }
        Jasper.printReports(relatorios.getJasper(), "convite_clube", (Collection) parametroConviteClubes);
    }

    public List<SelectItem> getListaTipoRelatorios() {
        if (listaTipoRelatorios.isEmpty()) {
            RelatorioDao db = new RelatorioDao();
            List<Relatorios> list = (List<Relatorios>) db.pesquisaTipoRelatorio(226);
            for (int i = 0; i < list.size(); i++) {
                listaTipoRelatorios.add(new SelectItem(i, list.get(i).getNome(), "" + list.get(i).getId()));
            }
        }
        return listaTipoRelatorios;
    }

    public int getIdRelatorios() {
        return idRelatorios;
    }

    public void setIdRelatorios(int idRelatorios) {
        this.idRelatorios = idRelatorios;
    }

    public void setListaTipoRelatorios(List<SelectItem> listaTipoRelatorios) {
        this.listaTipoRelatorios = listaTipoRelatorios;
    }

    public List<ParametroConviteClube> getParametroConviteClubes() {
        return parametroConviteClubes;
    }

    public void setParametroConviteClubes(List<ParametroConviteClube> parametroConviteClubes) {
        this.parametroConviteClubes = parametroConviteClubes;
    }

    public ConviteMovimento getConviteMovimento() {
        return conviteMovimento;
    }

    public void setConviteMovimento(ConviteMovimento conviteMovimento) {
        this.conviteMovimento = conviteMovimento;
    }

    public Fisica getFisica() {
        if (GenericaSessao.exists("fisicaPesquisa")) {
            fisica = (Fisica) GenericaSessao.getObject("fisicaPesquisa", true);
        }
        return fisica;
    }

    public void setFisica(Fisica fisica) {
        this.fisica = fisica;
    }

    public SisPessoa getSisPessoa() {
        if (GenericaSessao.exists("sisPessoaPesquisa")) {
            sisPessoa = (SisPessoa) GenericaSessao.getObject("sisPessoaPesquisa", true);
        }
        return sisPessoa;
    }

    public void setSisPessoa(SisPessoa sisPessoa) {
        this.sisPessoa = sisPessoa;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getIdCortesia() {
        return idCortesia;
    }

    public void setIdCortesia(String idCortesia) {
        this.idCortesia = idCortesia;
    }

    public Date getDataEmissaoInicial() {
        return dataEmissaoInicial;
    }

    public void setDataEmissaoInicial(Date dataEmissaoInicial) {
        this.dataEmissaoInicial = dataEmissaoInicial;
    }

    public Date getDataEmissaoFinal() {
        return dataEmissaoFinal;
    }

    public void setDataEmissaoFinal(Date dataEmissaoFinal) {
        this.dataEmissaoFinal = dataEmissaoFinal;
    }

    public Date getDataValidadeInicial() {
        return dataValidadeInicial;
    }

    public void setDataValidadeInicial(Date dataValidadeInicial) {
        this.dataValidadeInicial = dataValidadeInicial;
    }

    public Date getDataValidadeFinal() {
        return dataValidadeFinal;
    }

    public void setDataValidadeFinal(Date dataValidadeFinal) {
        this.dataValidadeFinal = dataValidadeFinal;
    }

    public boolean isSocio() {
        return socio;
    }

    public void setSocio(boolean socio) {
        this.socio = socio;
    }

    public boolean isDiretor() {
        return diretor;
    }

    public void setDiretor(boolean diretor) {
        this.diretor = diretor;
    }

    public boolean isOperador() {
        return operador;
    }

    public void setOperador(boolean operador) {
        this.operador = operador;
    }

    public boolean isConvidado() {
        return convidado;
    }

    public void setConvidado(boolean convidado) {
        this.convidado = convidado;
    }

    public boolean isEmissao() {
        return emissao;
    }

    public void setEmissao(boolean emissao) {
        this.emissao = emissao;
    }

    public boolean isValidade() {
        return validade;
    }

    public void setValidade(boolean validade) {
        this.validade = validade;
    }

    public String getTipoRelatorio() {
        return tipoRelatorio;
    }

    public void setTipoRelatorio(String tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
    }

    public void tipoRelatorioChange(TabChangeEvent event) {
        tipoRelatorio = event.getTab().getTitle();
        indexAccordion = ((AccordionPanel) event.getComponent()).getActiveIndex();
        if (tipoRelatorio.equals("Simples")) {
            limpar();
            socio = false;
            diretor = false;
            operador = false;
            convidado = false;
            emissao = false;
            validade = false;
            cortesia = false;
        }
    }

    public boolean isCortesia() {
        return cortesia;
    }

    public void setCortesia(boolean cortesia) {
        this.cortesia = cortesia;
    }

    public int getIdOperador() {
        return idOperador;
    }

    public void setIdOperador(int idOperador) {
        this.idOperador = idOperador;
    }

    public int getIdDiretor() {
        return idDiretor;
    }

    public void setIdDiretor(int idDiretor) {
        this.idDiretor = idDiretor;
    }

    public List<SelectItem> getListaOperadores() {
        if (listaOperadores.isEmpty()) {
            ConviteDB cdb = new ConviteDBToplink();
            List<Usuario> list = (List<Usuario>) cdb.listaUsuariosDisponiveis();
            for (int i = 0; i < list.size(); i++) {
                listaOperadores.add(new SelectItem(i, list.get(i).getPessoa().getNome(), "" + list.get(i).getId()));
            }
        }
        return listaOperadores;
    }

    public void setListaOperadores(List<SelectItem> listaOperadores) {
        this.listaOperadores = listaOperadores;
    }

    public List<SelectItem> getListaDiretores() {
        if (listaDiretores.isEmpty()) {
            SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
            List<ConviteAutorizaCortesia> list = (List<ConviteAutorizaCortesia>) dB.listaObjeto("ConviteAutorizaCortesia");
            int i = 0;
            for (ConviteAutorizaCortesia cac : list) {
                listaDiretores.add(new SelectItem(i, cac.getPessoa().getNome(), "" + cac.getId()));
                i++;
            }
        }
        return listaDiretores;
    }

    public void setListaDiretores(List<SelectItem> listaDiretores) {
        this.listaDiretores = listaDiretores;
    }

    public void selecionaDataEmissaoInicial(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataEmissaoInicial = DataHoje.converte(format.format(event.getObject()));
    }

    public void selecionaDataEmissaoFinal(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataEmissaoFinal = DataHoje.converte(format.format(event.getObject()));
    }

    public void selecionaDataValidadeInicial(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataValidadeInicial = DataHoje.converte(format.format(event.getObject()));
    }

    public void selecionaDataValidadeFinal(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataValidadeFinal = DataHoje.converte(format.format(event.getObject()));
    }

    public void limpar() {
        if (!convidado) {
            sisPessoa = new SisPessoa();
        }
        if (!socio) {
            fisica = new Fisica();
        }
        if (!diretor) {
            listaDiretores.clear();
            idDiretor = 0;
        }
        if (!operador) {
            listaOperadores.clear();
            idOperador = 0;
        }
        if (!cortesia) {
            idCortesia = "todos";
        }
        if (!emissao) {
            dataEmissaoInicial = DataHoje.dataHoje();
            dataEmissaoFinal = DataHoje.dataHoje();
        }
        if (!validade) {
            dataValidadeInicial = DataHoje.dataHoje();
            dataValidadeFinal = DataHoje.dataHoje();
        }
    }

    public void close(String close) {
        if (close.equals("convidado")) {
            sisPessoa = new SisPessoa();
            convidado = false;
        } else if (close.equals("socio")) {
            fisica = new Fisica();
            socio = false;
        } else if (close.equals("diretor")) {
            listaDiretores.clear();
            idDiretor = 0;
            diretor = false;
        } else if (close.equals("operador")) {
            listaOperadores.clear();
            idOperador = 0;
            operador = false;
        } else if (close.equals("cortesia")) {
            idCortesia = "todos";
            cortesia = false;
        } else if (close.equals("emissao")) {
            dataEmissaoInicial = DataHoje.dataHoje();
            dataEmissaoFinal = DataHoje.dataHoje();
            emissao = false;
        } else if (close.equals("validade")) {
            dataValidadeInicial = DataHoje.dataHoje();
            dataValidadeFinal = DataHoje.dataHoje();
            validade = false;
        }
        RequestContext.getCurrentInstance().update("form_relatorio:id_panel");
    }

    public String getIndexAccordion() {
        return indexAccordion;
    }

    public void setIndexAccordion(String indexAccordion) {
        this.indexAccordion = indexAccordion;
    }

}
