package br.com.rtools.retornos;

import br.com.rtools.financeiro.ContaCobranca;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.db.*;
import br.com.rtools.movimento.GerarMovimento;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.ArquivoRetorno;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaRetorno;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

public class RetornoPadrao extends ArquivoRetorno {

    public RetornoPadrao(ContaCobranca contaCobranca) {
        super(contaCobranca);
    }

    @Override
    public List<GenericaRetorno> sicob(boolean baixar, String host) {
        Usuario usuario = new Usuario();
        usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");
        GenericaRetorno genericaRetorno = new GenericaRetorno();
        FacesContext context = FacesContext.getCurrentInstance();
        String linha = null;
        String cnpj = "";
        String codigoCedente = "";
        String nossoNumero = "";
        String valorTaxa = "";
        String valorPago = "";
        String dataPagamento = "";
        String dataVencimento = "";
        String caminho = ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/ArquivoRetorno/Padrao");
        //String idContaCobranca = "";
        //Servicos servico = new Servicos();
        ServicosDB dbS = new ServicosDBToplink();
        ContaCobranca contaCobranca = new ContaCobranca();
        ContaCobrancaDB dbC = new ContaCobrancaDBToplink();
        List<Movimento> movimento = new ArrayList();
        MovimentoDB db = new MovimentoDBToplink();
        List<String> errors = new ArrayList<String>();
        boolean moverArquivo = true;
        List<String> listaDtPagamentos = new ArrayList<String>();
        List<Float> listaTaxa = new ArrayList<Float>();
        List<Float> listaValor = new ArrayList();
        File fl = new File(caminho);
        File listFile[] = fl.listFiles();
        List<GenericaRetorno> listaRetorno = new ArrayList();
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        Rotina rotina = (Rotina) salvarAcumuladoDB.pesquisaObjeto(4, "Rotina");
        if (listFile != null) {
            int qntRetornos = listFile.length;
            for (int u = 0; u < qntRetornos; u++) {
                try {
                    FileReader reader = new FileReader(caminho + "/" + listFile[u].getName());
                    BufferedReader buffReader = new BufferedReader(reader);
                    while ((linha = buffReader.readLine()) != null) {
                        cnpj = linha.substring(0, 15).trim();
                        codigoCedente = linha.substring(17, 30).trim();
                        nossoNumero = linha.substring(30, 51).trim();
                        valorTaxa = linha.substring(67, 83).trim();
                        valorPago = linha.substring(51, 67).trim();
                        dataPagamento = linha.substring(83, 92).trim();
                        dataVencimento = "";//linha.substring(83, 92).trim();
                        //idContaCobranca = linha.substring(92, 98).trim();

                        if (!contaCobranca.getCodCedente().trim().equals(codigoCedente.trim())) {
                            contaCobranca = new ContaCobranca();
                            contaCobranca = dbC.pesquisaCobrancaCedente(codigoCedente);
                        }
                        movimento = db.pesquisaMovPorNumDocumentoList(nossoNumero, DataHoje.converte(DataHoje.colocarBarras(dataVencimento)), contaCobranca.getId());
                        if (!movimento.isEmpty()) {
                            if (movimento.size() > 1) {
                                movimento = new ArrayList();
                                listaDtPagamentos = new ArrayList<String>();
                                listaTaxa = new ArrayList<Float>();
                                listaValor = new ArrayList();
                                //contaCobranca = new ContaCobranca();
                                continue;
                            } else {
//                                listaDtPagamentos.add(DataHoje.colocarBarras(dataPagamento));
//                                listaTaxa.add();
//                                listaValor.add();
                            }

                            Movimento movi = movimento.get(0);

                            movi.setValor(Moeda.substituiVirgulaFloat(Moeda.converteR$(valorPago)) / 100);
                            movi.setTaxa(Moeda.substituiVirgulaFloat(Moeda.converteR$(valorTaxa)) / 100);

                            GerarMovimento.salvarUmMovimento(null, movi);
//                            OperacaoMovimento op = new OperacaoMovimento(movimento);
//                            String result = op.darBaixa(usuario, rotina, null, listaDtPagamentos, listaTaxa, listaValor, contaCobranca.getContaBanco().getFilial(), null);
                        }
                        movimento = new ArrayList();
                        listaDtPagamentos = new ArrayList<String>();
                        listaTaxa = new ArrayList<Float>();
                        listaValor = new ArrayList();
                        //contaCobranca = new ContaCobranca();
                    }
                } catch (Exception e) {
                    continue;
                }
            }
            if (moverArquivo) {
                for (int i = 0; i < listFile.length; i++) {
                    try {
                        if (!listFile[i].getName().toLowerCase().startsWith(DataHoje.ArrayDataHoje()[2] + "-" + DataHoje.ArrayDataHoje()[1])) {
                            File fileDel = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/ArquivoRetorno/Padrao/ARQUIVOBAIXA.ret"));
                            if (fileDel.exists()) {
                                fileDel.delete();
                            }
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }
            }
        }
        return listaRetorno;
    }

    @Override
    public List<GenericaRetorno> sindical(boolean baixar, String host) {
        return new ArrayList();
    }

    @Override
    public List<GenericaRetorno> sigCB(boolean baixar, String host) {
        return new ArrayList();
    }

    @Override
    public String darBaixaSindical(String caminho, Usuario usuario) {
        String mensagem = "NÃO EXISTE IMPLEMENTAÇÃO PARA ESTE TIPO!";
        return mensagem;
    }

    @Override
    public String darBaixaSigCB(String caminho, Usuario usuario) {
        String mensagem = "NÃO EXISTE IMPLEMENTAÇÃO PARA ESTE TIPO!";
        return mensagem;
    }
    
    @Override
    public String darBaixaSigCBSocial(String caminho, Usuario usuario) {
        String mensagem = "NÃO EXISTE IMPLEMENTAÇÃO PARA ESTE TIPO!";
        return mensagem;
    }

    @Override
    public String darBaixaSicob(String caminho, Usuario usuario) {
        String mensagem = "NÃO EXISTE IMPLEMENTAÇÃO PARA ESTE TIPO!";
        return mensagem;
    }
    
    @Override
    public String darBaixaSicobSocial(String caminho, Usuario usuario) {
        String mensagem = "NÃO EXISTE IMPLEMENTAÇÃO PARA ESTE TIPO!";
        return mensagem;
    }

    @Override
    public String darBaixaPadrao(Usuario usuario) {
        String mensagem = "";
        mensagem = super.baixarArquivoPadrao(this.sicob(true, ""), usuario);
        return mensagem;
    }
}
