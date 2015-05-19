package br.com.rtools.retornos;

import br.com.rtools.financeiro.ContaCobranca;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.ArquivoRetorno;
import br.com.rtools.utilitarios.GenericaRetorno;
import br.com.rtools.utilitarios.Moeda;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Itau extends ArquivoRetorno {
    private String linha = "", 
                   pasta = "", 
                   cnpj = "", 
                   codigoCedente = "", 
                   nossoNumero = "", 
                   dataVencimento = "", 
                   valorTaxa = "",
                   valorPago = "",
                   valorCredito = "",
                   valorRepasse = "",
                   dataPagamento = "",
                   dataCredito = "";
    public Itau(ContaCobranca contaCobranca) {
        super(contaCobranca);
    }

    @Override
    public List<GenericaRetorno> sicob(boolean baixar, String host) {
        host = host + "/pendentes/";
        pasta = host;
        

        File fl = new File(host);
        File listFile[] = fl.listFiles();
        List<GenericaRetorno> listaRetorno = new ArrayList();
        if (listFile != null) {
            int qntRetornos = listFile.length;
            for (int u = 0; u < qntRetornos; u++) {
                try {
                    FileReader reader = new FileReader(host + listFile[u].getName());
                    BufferedReader buffReader = new BufferedReader(reader);
                    List lista = new Vector();
                    while ((linha = buffReader.readLine()) != null) {
                        lista.add(linha);
                    }

                    reader.close();
                    buffReader.close();
                    int i = 0;
                    codigoCedente = ((String) lista.get(i)).substring(32, 37);//ok
                    while (i < (lista.size() - 1)) {
                        if (i >= 1) {
                            cnpj = ((String) lista.get(i)).substring(3, 17); //ok

                            nossoNumero = ((String) lista.get(i)).substring(62, 70).trim(); //ok
                            valorTaxa = ((String) lista.get(i)).substring(175, 188);//ok
                            dataVencimento = ((String) lista.get(i)).substring(146, 152); //ok

                            try {
                                int con = Integer.parseInt(dataVencimento);
                                if (con == 0) {
                                    dataVencimento = "11111111";
                                } else {
                                    dataVencimento = dataVencimento.substring(0, dataVencimento.length() - 2) + "20" + dataVencimento.substring(dataVencimento.length() - 2, dataVencimento.length());
                                }
                            } catch (Exception e) {
                            }
                            
                            valorPago = ((String) lista.get(i)).substring(253, 266); //ok VALOR PAGO + TAXA segundo o arquivo
                            float valorx = Moeda.divisaoValores(Moeda.substituiVirgulaFloat(Moeda.converteR$(valorPago)), 100);
                            float taxax = Moeda.divisaoValores(Moeda.substituiVirgulaFloat(Moeda.converteR$(valorTaxa)), 100);
                            String valorlenght = Moeda.converteR$Float(Moeda.somaValores(valorx, taxax)).replace(".", "").replace(",", "");
                            valorPago = "0000000000000".substring(0, 13 - valorlenght.length()) + valorlenght;
                            
                            dataPagamento = ((String) lista.get(i)).substring(295, 301);//ok
                            dataPagamento = dataPagamento.substring(0, dataPagamento.length() - 2) + "20" + dataPagamento.substring(dataPagamento.length() - 2, dataPagamento.length());

                            listaRetorno.add(new GenericaRetorno(
                                    cnpj, //1 ENTIDADE
                                    codigoCedente, //2 NESTE CASO SICAS
                                    nossoNumero, //3
                                    valorPago, //4
                                    valorTaxa, //5
                                    "",//valorCredito,   //6
                                    dataPagamento, //7
                                    dataVencimento,//dataVencimento, //8
                                    "", //9 ACRESCIMO
                                    "", //10 VALOR DESCONTO
                                    "", //11 VALOR ABATIMENTO
                                    "", //12 VALOR REPASSE ...(valorPago - valorCredito)
                                    pasta, // 13 NOME DA PASTA
                                    listFile[u].getName(), //14 NOME DO ARQUIVO
                                    "", //15 DATA CREDITO
                                    "") // 16 SEQUENCIAL DO ARQUIVO
                            );
                        }
                        i++;
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
        return listaRetorno;
    }

    @Override
    public List<GenericaRetorno> sindical(boolean baixar, String host) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<GenericaRetorno> sigCB(boolean baixar, String host) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String darBaixaSicob(String caminho, Usuario usuario) {
        String mensagem = super.baixarArquivo(this.sicob(true, caminho), caminho, usuario);
        return mensagem;
    }
    
    @Override
    public String darBaixaSicobSocial(String caminho, Usuario usuario) {
        String mensagem = super.baixarArquivoSocial(this.sicob(true, caminho), caminho, usuario);
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
    public String darBaixaSindical(String caminho, Usuario usuario) {
        String mensagem = "NÃO EXISTE IMPLEMENTAÇÃO PARA ESTE TIPO!";
        return mensagem;
    }

    @Override
    public String darBaixaPadrao(Usuario usuario) {
        String mensagem = "NÃO EXISTE IMPLEMENTAÇÃO PARA ESTE TIPO!";
        return mensagem;
    }
}
