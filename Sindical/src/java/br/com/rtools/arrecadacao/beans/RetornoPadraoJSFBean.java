package br.com.rtools.arrecadacao.beans;

import br.com.rtools.financeiro.ContaCobranca;
import br.com.rtools.financeiro.db.ServicoContaCobrancaDB;
import br.com.rtools.financeiro.db.ServicoContaCobrancaDBToplink;
import br.com.rtools.retornos.BancoBrasil;
import br.com.rtools.retornos.CaixaFederal;
import br.com.rtools.retornos.Itau;
import br.com.rtools.retornos.Real;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.ArquivoRetorno;
import br.com.rtools.utilitarios.DataObject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;

public class RetornoPadraoJSFBean {

    private ContaCobranca contaCobranca = new ContaCobranca();
    private int idContrib = 0;
    private String conteudo = "";
    private String msgConfirma = "";
    private ArrayList<DataObject> listaCaminho = new ArrayList();
    private ArrayList<DataObject> listaConteudo = new ArrayList();
    private ArrayList listaTeste = new ArrayList();
    List<SelectItem> servicoCobranca = new ArrayList<SelectItem>();
    private boolean ckTodos = true;

    public RetornoPadraoJSFBean() {
        //contaCobranca = (ContaCobranca) new SalvarAcumuladoDBToplink().pesquisaCodigo(2, "ContaCobranca");
    }

    public String enviarArquivoBaixar(boolean pendentes) {
        try {
            Usuario usuario = new Usuario();
            usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");
            //Object objs[] = caminhoServico();
            //String caminhoCompleto =  (String)objs[0];
            //ContaCobranca scc = (ContaCobranca)objs[1];

            ContaCobranca cc = new ContaCobranca();
            ArquivoRetorno arquivoRetorno = null;

            msgConfirma = "";
            String caminhoPendente = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/retorno/pendentes/");

            for (int i = 0; i < listaCaminho.size(); i++) {
                if ((Boolean) listaCaminho.get(i).getArgumento3()) {
                    String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/retorno");
                    cc = ((ContaCobranca) listaCaminho.get(i).getArgumento2());
                    if (cc.getLayout().getId() == 2) {
                        caminho = caminho + "/" + cc.getSicasSindical() + "/";
                        File fl = new File(caminho);
                        if (!fl.exists()) {
                            fl.mkdir();
                        }
                    } else {
                        caminho = caminho + "/" + cc.getCodCedente() + "/";
                        File fl = new File(caminho);
                        if (!fl.exists()) {
                            fl.mkdir();
                        }
                    }

                    criarArquivos(caminhoPendente, listaCaminho.get(i).getArgumento5().toString(), listaCaminho.get(i).getArgumento4().toString());

                    // CAIXA FEDERAL ------------------------------------------------------------------------------
                    if (!listaCaminho.get(i).getArgumento4().toString().equals("vazio")) {
                        if (ArquivoRetorno.CAIXA_FEDERAL == cc.getContaBanco().getBanco().getId()) {
                            if (ArquivoRetorno.SICOB == cc.getLayout().getId()) {
                                arquivoRetorno = new CaixaFederal(cc);
                                msgConfirma = arquivoRetorno.darBaixaSicob(caminho, usuario);
                            } else if (ArquivoRetorno.SINDICAL == cc.getLayout().getId()) {
                                arquivoRetorno = new CaixaFederal(cc);
                                msgConfirma = arquivoRetorno.darBaixaSindical(caminho, usuario);
                            } else if (ArquivoRetorno.SIGCB == cc.getLayout().getId()) {
                                arquivoRetorno = new CaixaFederal(cc);
                                msgConfirma = arquivoRetorno.darBaixaSigCB(caminho, usuario);
                            }
                            // BANCO DO BRASIL ------------------------------------------------------------------------------
                        } else if (ArquivoRetorno.BANCO_BRASIL == cc.getContaBanco().getBanco().getId()) {
                            if (ArquivoRetorno.SICOB == cc.getLayout().getId()) {
                                arquivoRetorno = new BancoBrasil(cc);
                                msgConfirma = arquivoRetorno.darBaixaSicob(caminho, usuario);
                            } else if (ArquivoRetorno.SINDICAL == cc.getLayout().getId()) {
                                msgConfirma = "NÃO EXISTE SINDICAL PARA ESTA CONTA!";
                            } else if (ArquivoRetorno.SIGCB == cc.getLayout().getId()) {
                                msgConfirma = "NÃO EXISTE SIGCB PARA ESTA CONTA!";
                            }
                            // REAL ------------------------------------------------------------------------------
                        } else if (ArquivoRetorno.REAL == cc.getContaBanco().getBanco().getId()) {
                            if (ArquivoRetorno.SICOB == cc.getLayout().getId()) {
                                arquivoRetorno = new Real(cc);
                                msgConfirma = arquivoRetorno.darBaixaSicob(caminho, usuario);
                            } else if (ArquivoRetorno.SINDICAL == cc.getLayout().getId()) {
                                msgConfirma = "NÃO EXISTE SINDICAL PARA ESTA CONTA!";
                            } else if (ArquivoRetorno.SIGCB == cc.getLayout().getId()) {
                                msgConfirma = "NÃO EXISTE SIGCB PARA ESTA CONTA!";
                            }
                            // ITAU --------------------------------------------------------------------------------
                        } else if (ArquivoRetorno.ITAU == cc.getContaBanco().getBanco().getId()) {
                            if (ArquivoRetorno.SICOB == cc.getLayout().getId()) {
                                arquivoRetorno = new Itau(cc);
                                msgConfirma = arquivoRetorno.darBaixaSicob(caminho, usuario);
                            } else if (ArquivoRetorno.SINDICAL == cc.getLayout().getId()) {
                                msgConfirma = "NÃO EXISTE SINDICAL PARA ESTA CONTA!";
                            } else if (ArquivoRetorno.SIGCB == cc.getLayout().getId()) {
                                msgConfirma = "NÃO EXISTE SIGCB PARA ESTA CONTA!";
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            return "retornoPadrao";
        }
        return "retornoPadrao";
    }

    public void criarArquivos(String caminho, String nome, String texto) {
        try {
            String arquivo = caminho + "/" + nome;
            FileOutputStream file = new FileOutputStream(arquivo);
            file.close();

            FileWriter writer = new FileWriter(arquivo);
            BufferedWriter buffWriter = new BufferedWriter(writer);

            buffWriter.write(texto);
            //buffWriter.newLine();
            buffWriter.flush();
            buffWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void atualizaLista() {
        if (!listaCaminho.isEmpty()) {
            for (int i = 0; i < listaCaminho.size(); i++) {
                listaCaminho.get(i).setArgumento3(ckTodos);
            }
        }
    }

    public Object[] caminhoServico() {
        Object obj[] = new Object[2];
        if (contaCobranca.getId() != -1) {
            String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/retorno");
            // LAYOUT 2 SINDICAL
            if (contaCobranca.getLayout().getId() == 2) {
                //caminho = caminho +"/"+ contaCobranca.getApelido()+"_"+contaCobranca.getSicasSindical();
                caminho = caminho + "/" + contaCobranca.getSicasSindical();
            } else {
                //caminho = caminho +"/"+ contaCobranca.getApelido()+"_"+contaCobranca.getCodCedente();
                caminho = caminho + "/" + contaCobranca.getCodCedente();
            }
            obj[0] = caminho;
            obj[1] = contaCobranca;
        }
        return obj;
    }

    public ContaCobranca getContaCobranca() {
        return contaCobranca;
    }

    public void setContaCobranca(ContaCobranca contaCobranca) {
        this.contaCobranca = contaCobranca;
    }

    public int getIdContrib() {
        return idContrib;
    }

    public void setIdContrib(int idContrib) {
        this.idContrib = idContrib;
    }

    public ArrayList<DataObject> getListaCaminho() {
        if (listaCaminho.isEmpty()) {
            ServicoContaCobrancaDB servDB = new ServicoContaCobrancaDBToplink();
            List<ContaCobranca> select = servDB.listaContaCobrancaAtivoArrecadacao();
            if (!select.isEmpty()) {
                List<DataObject> listax = new ArrayList<DataObject>();
                for (int i = 0; i < select.size(); i++) {
                    listaCaminho.add(new DataObject(i, // INDICE
                            select.get(i).getCaminhoRetorno(), // CAMINHO DO ARQUIVO DENTRO DO CONTA COBRANCA
                            select.get(i), // OBJETO CONTA COBRANCA
                            true, // MOSTRAR OU NÃO A CONTA COBRANCA
                            new DataObject("", ""), // ARRAY COM NOME E CONTEUDO DO ARQUIVO QUE VEM DA APPLET
                            null));
                }
            }
        }
        return listaCaminho;
    }

    public void setListaCaminho(ArrayList<DataObject> listaCaminho) {
        this.listaCaminho = listaCaminho;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public boolean isCkTodos() {
        return ckTodos;
    }

    public void setCkTodos(boolean ckTodos) {
        this.ckTodos = ckTodos;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public ArrayList<DataObject> getListaConteudo() {
        if (listaConteudo.isEmpty()) {
            for (int i = 0; i < 3; i++) {
                //listaConteudo.add(new DataObject(i, null, null, null, null, null));
                //listaConteudo.add("Teste "+i);
            }
        }
        return listaConteudo;
    }

    public void setListaConteudo(ArrayList<DataObject> listaConteudo) {
        this.listaConteudo = listaConteudo;
    }

    public ArrayList getListaTeste() {
        return listaTeste;
    }

    public void setListaTeste(ArrayList listaTeste) {
        this.listaTeste = listaTeste;
    }
}
