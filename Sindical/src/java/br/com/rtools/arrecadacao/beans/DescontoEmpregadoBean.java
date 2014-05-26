package br.com.rtools.arrecadacao.beans;

import br.com.rtools.arrecadacao.Convencao;
import br.com.rtools.arrecadacao.DescontoEmpregado;
import br.com.rtools.arrecadacao.GrupoCidade;
import br.com.rtools.arrecadacao.db.*;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class DescontoEmpregadoBean implements Serializable {

    private DescontoEmpregado descontoEmpregado = new DescontoEmpregado();
    private int idServicos = 0;
    private int idGrupoCidade = 0;
    private int idConvencao = 0;
    private String msgConfirma = "";
    private String percentual = "0.0";
    private String valor = "0";
    private List<SelectItem> listaServicos = new ArrayList<SelectItem>();
    private List<SelectItem> listaGrupoCidade = new ArrayList<SelectItem>();
    private List<SelectItem> listaConvencao = new ArrayList<SelectItem>();
    private List<DescontoEmpregado> listaDescontoEmpregado = new ArrayList();
    private int idIndex = -1;
    private boolean limpar = false;

    public int getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(int idServicos) {
        this.idServicos = idServicos;
    }

    public DescontoEmpregado getDescontoEmpregado() {
        return descontoEmpregado;
    }

    public void setDescontoEmpregado(DescontoEmpregado descontoEmpregado) {
        this.descontoEmpregado = descontoEmpregado;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public String novo() {
        descontoEmpregado = new DescontoEmpregado();
        listaDescontoEmpregado.clear();
        msgConfirma = "";
        limpar = false;
        idServicos = 0;
        idGrupoCidade = 0;
        idConvencao = 0;
        valor = "0";
        percentual = "0.0";
        return "descontoEmpregado";
    }

    public void limpar() {
        if (limpar == true) {
            novo();
        }
        descontoEmpregado = new DescontoEmpregado();
        listaDescontoEmpregado.clear();
        msgConfirma = "";
        limpar = false;
        idServicos = 0;
        idGrupoCidade = 0;
        idConvencao = 0;
        valor = "0";
        percentual = "0.0";
    }

    public String salvar() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        DescontoEmpregadoDB db = new DescontoEmpregadoDBToplink();
        Servicos servicos = new Servicos();
        GrupoCidade grupoCidade = new GrupoCidade();
        Convencao convencao = new Convencao();
        NovoLog log = new NovoLog();
        sv.abrirTransacao();
        servicos = (Servicos) sv.pesquisaCodigo(Integer.parseInt(listaServicos.get(idServicos).getDescription()), "Servicos");
        grupoCidade = (GrupoCidade) sv.pesquisaCodigo(Integer.parseInt(listaGrupoCidade.get(idGrupoCidade).getDescription()), "GrupoCidade");
        convencao = (Convencao) sv.pesquisaCodigo(Integer.parseInt(listaConvencao.get(idConvencao).getDescription()), "Convencao");
        if (percentual.isEmpty()) {
            percentual = "0.0";
        }
        descontoEmpregado.setPercentual(Moeda.substituiVirgulaFloat(percentual));
        if (valor.isEmpty()) {
            valor = "0";
        }
        descontoEmpregado.setValorEmpregado(Moeda.substituiVirgulaFloat(valor));

        if (descontoEmpregado.getPercentual() <= 0 && descontoEmpregado.getValorEmpregado() <= 0) {
            msgConfirma = "Digite um Percentual ou Valor de empregado!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        if (descontoEmpregado.getId() == -1) {
            if (DataHoje.validaReferencias(descontoEmpregado.getReferenciaInicial(), descontoEmpregado.getReferenciaFinal())) {
                descontoEmpregado.setServicos(servicos);
                descontoEmpregado.setGrupoCidade(grupoCidade);
                descontoEmpregado.setConvencao(convencao);
                if (sv.inserirObjeto(descontoEmpregado)) {
                    sv.comitarTransacao();
                    msgConfirma = "Desconto salvo com Sucesso!";
                    GenericaMensagem.info("Sucesso", msgConfirma);
                    log.save(
                            "ID: " + descontoEmpregado.getId()
                            + " - Servico: (" + descontoEmpregado.getServicos().getId() + ") " + descontoEmpregado.getServicos().getDescricao()
                            + " - Valor: " + descontoEmpregado.getValorEmpregado()
                            + " - Grupo Cidade: (" + descontoEmpregado.getGrupoCidade().getId() + ") " + descontoEmpregado.getGrupoCidade().getDescricao()
                            + " - Convencao: (" + descontoEmpregado.getConvencao().getId() + ") " + descontoEmpregado.getConvencao().getDescricao()
                    );
                } else {
                    sv.desfazerTransacao();
                    msgConfirma = "Erro ao salvar Desconto!";
                    GenericaMensagem.warn("Erro", msgConfirma);
                }
            } else {
                sv.desfazerTransacao();
                msgConfirma = "Referência incorreta!";
                GenericaMensagem.warn("Erro", msgConfirma);
            }
        } else {
            DescontoEmpregado d = (DescontoEmpregado) sv.pesquisaCodigo(descontoEmpregado.getId(), "DescontoEmpregado");
            String beforeUpdate
                    = "ID: " + d.getId()
                    + " - Servico: (" + d.getServicos().getId() + ") " + d.getServicos().getDescricao()
                    + " - Valor: " + d.getValorEmpregado()
                    + " - Grupo Cidade: (" + d.getGrupoCidade().getId() + ") " + d.getGrupoCidade().getDescricao()
                    + " - Convencao: (" + d.getConvencao().getId() + ") " + d.getConvencao().getDescricao();
            descontoEmpregado.setServicos(servicos);
            descontoEmpregado.setGrupoCidade(grupoCidade);
            descontoEmpregado.setConvencao(convencao);
            if (sv.alterarObjeto(descontoEmpregado)) {
                sv.comitarTransacao();
                msgConfirma = "Desconto atualizado com Sucesso!";
                GenericaMensagem.info("Sucesso", msgConfirma);
                log.update(beforeUpdate,
                        "ID: " + descontoEmpregado.getId()
                        + " - Servico: (" + descontoEmpregado.getServicos().getId() + ") " + descontoEmpregado.getServicos().getDescricao()
                        + " - Valor: " + descontoEmpregado.getValorEmpregado()
                        + " - Grupo Cidade: (" + descontoEmpregado.getGrupoCidade().getId() + ") " + descontoEmpregado.getGrupoCidade().getDescricao()
                        + " - Convencao: (" + descontoEmpregado.getConvencao().getId() + ") " + descontoEmpregado.getConvencao().getDescricao()
                );
            } else {
                sv.desfazerTransacao();
            }
        }
        return null;
    }

    public String btnExcluir(DescontoEmpregado de) {
        NovoLog log = new NovoLog();
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();
        descontoEmpregado = (DescontoEmpregado) sv.pesquisaCodigo(de.getId(), "DescontoEmpregado");
        if (sv.deletarObjeto(descontoEmpregado)) {
            limpar = true;
            msgConfirma = "Desconto Excluido com Sucesso!";
            GenericaMensagem.info("Sucesso", msgConfirma);
            log.delete(
                    "ID: " + descontoEmpregado.getId()
                    + " - Servico: (" + descontoEmpregado.getServicos().getId() + ") " + descontoEmpregado.getServicos().getDescricao()
                    + " - Valor: " + descontoEmpregado.getValorEmpregado()
                    + " - Grupo Cidade: (" + descontoEmpregado.getGrupoCidade().getId() + ") " + descontoEmpregado.getGrupoCidade().getDescricao()
                    + " - Convencao: (" + descontoEmpregado.getConvencao().getId() + ") " + descontoEmpregado.getConvencao().getDescricao()
            );
            sv.comitarTransacao();
        } else {
            msgConfirma = "Desconto não pode ser Excluido!";
            GenericaMensagem.warn("Erro", msgConfirma);
            sv.desfazerTransacao();
        }
        return null;
    }

    public String editar(DescontoEmpregado de) {
        descontoEmpregado = de;//(DescontoEmpregado) listaDescontoEmpregado.get(idIndex);
        if (descontoEmpregado != null) {
            int i = 0;
            if (descontoEmpregado.getServicos().getId() != -1) {
                for (i = 0; i < listaServicos.size(); i++) {
                    if (Integer.parseInt(listaServicos.get(i).getDescription()) == descontoEmpregado.getServicos().getId()) {
                        setIdServicos(i);
                        break;
                    }
                }
            }
            if (descontoEmpregado.getGrupoCidade().getId() != -1) {
                for (i = 0; i < listaGrupoCidade.size(); i++) {
                    if (Integer.parseInt(listaGrupoCidade.get(i).getDescription()) == descontoEmpregado.getGrupoCidade().getId()) {
                        setIdGrupoCidade(i);
                        break;
                    }
                }
            }
            if (descontoEmpregado.getConvencao().getId() != -1) {
                for (i = 0; i < listaConvencao.size(); i++) {
                    if (Integer.parseInt(listaConvencao.get(i).getDescription()) == descontoEmpregado.getConvencao().getId()) {
                        setIdConvencao(i);
                        break;
                    }
                }
            }
            percentual = Float.toString(descontoEmpregado.getPercentual());
            valor = Float.toString(descontoEmpregado.getValorEmpregado());
        }
        return "descontoEmpregado";
    }

    public List<SelectItem> getListaServico() {
        if (listaServicos.isEmpty()) {
            int i = 0;
            ServicosDB db = new ServicosDBToplink();
            List select = db.pesquisaTodos(4);
            while (i < select.size()) {
                listaServicos.add(new SelectItem(new Integer(i),
                        (String) ((Servicos) select.get(i)).getDescricao(),
                        Integer.toString(((Servicos) select.get(i)).getId())));
                i++;
            }
        }
        return listaServicos;
    }

    public List<SelectItem> getListaGrupoCidade() {
        listaGrupoCidade = new ArrayList();
        int i = 0;
        ConvencaoCidadeDB db = new ConvencaoCidadeDBToplink();
        List select = db.pesquisarGruposPorConvencao(Integer.parseInt(getListaConvencao().get(idConvencao).getDescription()));
        while (i < select.size()) {
            listaGrupoCidade.add(new SelectItem(new Integer(i),
                    (String) ((GrupoCidade) select.get(i)).getDescricao(),
                    Integer.toString(((GrupoCidade) select.get(i)).getId())));
            i++;
        }
        return listaGrupoCidade;
    }

    public List<SelectItem> getListaConvencao() {
        if (listaConvencao.isEmpty()) {
            int i = 0;
            ConvencaoDB db = new ConvencaoDBToplink();
            List select = db.pesquisaTodos();
            while (i < select.size()) {
                listaConvencao.add(new SelectItem(new Integer(i),
                        (String) ((Convencao) select.get(i)).getDescricao(),
                        Integer.toString(((Convencao) select.get(i)).getId())));
                i++;
            }
        }

        return listaConvencao;
    }

    public List<DescontoEmpregado> getListaDescontoEmpregado() {
        DescontoEmpregadoDB db = new DescontoEmpregadoDBToplink();
        listaDescontoEmpregado = db.pesquisaTodos();
        return listaDescontoEmpregado;
    }

    public int getIdGrupoCidade() {
        return idGrupoCidade;
    }

    public void setIdGrupoCidade(int idGrupoCidade) {
        this.idGrupoCidade = idGrupoCidade;
    }

    public int getIdConvencao() {
        return idConvencao;
    }

    public void setIdConvencao(int idConvencao) {
        this.idConvencao = idConvencao;
    }

    public String getPercentual() {
        return percentual;
    }

    public void setPercentual(String percentual) {
        this.percentual = Moeda.substituiVirgula(percentual);
    }

    public String getValor() {
        return Moeda.converteR$(valor);
    }

    public void setValor(String valor) {
        this.valor = Moeda.substituiVirgula(valor);
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public boolean isLimpar() {
        return limpar;
    }

    public void setLimpar(boolean limpar) {
        this.limpar = limpar;
    }
}
