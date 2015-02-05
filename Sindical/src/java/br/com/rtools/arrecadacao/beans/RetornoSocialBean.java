package br.com.rtools.arrecadacao.beans;

import br.com.rtools.financeiro.ContaCobranca;
import br.com.rtools.financeiro.db.ServicoContaCobrancaDB;
import br.com.rtools.financeiro.db.ServicoContaCobrancaDBToplink;
import br.com.rtools.retornos.BancoBrasil;
import br.com.rtools.retornos.CaixaFederal;
import br.com.rtools.retornos.Itau;
import br.com.rtools.retornos.Real;
import br.com.rtools.retornos.Santander;
import br.com.rtools.retornos.Sicoob;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.ArquivoRetorno;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Diretorio;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import org.primefaces.event.FileUploadEvent;


@ManagedBean
@SessionScoped
public class RetornoSocialBean {
    private int indexConta;
    private List<SelectItem> listaContas;
    private ContaCobranca contaCobranca;
    private List<String> listaArquivosPendentes;
    private List<Object[]> listaLogs;
    
    
    @PostConstruct
    public void init(){
        indexConta = 0;
        listaContas = new ArrayList();
        contaCobranca = new ContaCobranca();
        listaArquivosPendentes = new ArrayList();
        listaLogs = new ArrayList();
        
        loadListaContas();
        loadListaArquivosBaixar();
        
    }
    
    @PreDestroy
    public void destroy(){
        GenericaSessao.remove("retornoSocialBean");
        GenericaSessao.remove("logsRetornoSocial");
    }
    
    public void enviarArquivoBaixar(){
        try {
            Usuario usuario = (Usuario) GenericaSessao.getObject("sessaoUsuario");
            String caminhoCompleto = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/retorno/social");
            caminhoCompleto = caminhoCompleto+"/"+contaCobranca.getCodCedente();
            
            String result = "";
            ArquivoRetorno arquivoRetorno;

            if (!listaArquivosPendentes.isEmpty()) {
                if (ArquivoRetorno.CAIXA_FEDERAL == contaCobranca.getContaBanco().getBanco().getId()) {
                    if (ArquivoRetorno.SICOB == contaCobranca.getLayout().getId()) {
                        arquivoRetorno = new CaixaFederal(contaCobranca);
                        result = arquivoRetorno.darBaixaSicobSocial(caminhoCompleto, usuario);
                    } else if (ArquivoRetorno.SIGCB == contaCobranca.getLayout().getId()) {
                        arquivoRetorno = new CaixaFederal(contaCobranca);
                        result = arquivoRetorno.darBaixaSigCBSocial(caminhoCompleto, usuario);
                    }
                    // BANCO DO BRASIL ------------------------------------------------------------------------------
                } else if (ArquivoRetorno.BANCO_BRASIL == contaCobranca.getContaBanco().getBanco().getId()) {
                    if (ArquivoRetorno.SICOB == contaCobranca.getLayout().getId()) {
                        arquivoRetorno = new BancoBrasil(contaCobranca);
                        result = arquivoRetorno.darBaixaSicobSocial(caminhoCompleto, usuario);
                    } else if (ArquivoRetorno.SINDICAL == contaCobranca.getLayout().getId()) {
                        result = "NÃO EXISTE SINDICAL PARA ESTA CONTA!";
                    } else if (ArquivoRetorno.SIGCB == contaCobranca.getLayout().getId()) {
                        result = "NÃO EXISTE SIGCB PARA ESTA CONTA!";
                    }
                    // REAL ------------------------------------------------------------------------------
                } else if (ArquivoRetorno.REAL == contaCobranca.getContaBanco().getBanco().getId()) {
                    if (ArquivoRetorno.SICOB == contaCobranca.getLayout().getId()) {
                        arquivoRetorno = new Real(contaCobranca);
                        result = arquivoRetorno.darBaixaSicobSocial(caminhoCompleto, usuario);
                    } else if (ArquivoRetorno.SINDICAL == contaCobranca.getLayout().getId()) {
                        result = "NÃO EXISTE SINDICAL PARA ESTA CONTA!";
                    } else if (ArquivoRetorno.SIGCB == contaCobranca.getLayout().getId()) {
                        result = "NÃO EXISTE SIGCB PARA ESTA CONTA!";
                    }
                    // ITAU --------------------------------------------------------------------------------
                } else if (ArquivoRetorno.ITAU == contaCobranca.getContaBanco().getBanco().getId()) {
                    if (ArquivoRetorno.SICOB == contaCobranca.getLayout().getId()) {
                        arquivoRetorno = new Itau(contaCobranca);
                        result = arquivoRetorno.darBaixaSicobSocial(caminhoCompleto, usuario);
                    } else if (ArquivoRetorno.SINDICAL == contaCobranca.getLayout().getId()) {
                        result = "NÃO EXISTE SINDICAL PARA ESTA CONTA!";
                    } else if (ArquivoRetorno.SIGCB == contaCobranca.getLayout().getId()) {
                        result = "NÃO EXISTE SIGCB PARA ESTA CONTA!";
                    }
                } else if (ArquivoRetorno.SANTANDER == contaCobranca.getContaBanco().getBanco().getId()) {
                    if (ArquivoRetorno.SICOB == contaCobranca.getLayout().getId()) {
                        arquivoRetorno = new Santander(contaCobranca);
                        result = arquivoRetorno.darBaixaSicobSocial(caminhoCompleto, usuario);
                    } else if (ArquivoRetorno.SINDICAL == contaCobranca.getLayout().getId()) {
                        result = "NÃO EXISTE SINDICAL PARA ESTA CONTA!";
                    } else if (ArquivoRetorno.SIGCB == contaCobranca.getLayout().getId()) {
                        result = "NÃO EXISTE SIGCB PARA ESTA CONTA!";
                    }
                } else if (ArquivoRetorno.SICOOB == contaCobranca.getContaBanco().getBanco().getId()) {
                    if (ArquivoRetorno.SICOB == contaCobranca.getLayout().getId()) {
                        arquivoRetorno = new Sicoob(contaCobranca);
                        result = arquivoRetorno.darBaixaSicobSocial(caminhoCompleto, usuario);
                    } else if (ArquivoRetorno.SINDICAL == contaCobranca.getLayout().getId()) {
                        result = "NÃO EXISTE SINDICAL PARA ESTA CONTA!";
                    } else if (ArquivoRetorno.SIGCB == contaCobranca.getLayout().getId()) {
                        result = "NÃO EXISTE SIGCB PARA ESTA CONTA!";
                    }
                }
            } 
            List<Object[]> listal = (List<Object[]>) GenericaSessao.getObject("logsRetornoSocial");
            
            
            GenericaMensagem.info("Sucesso", "Arquivos Baixados");
            
            for (Object[] obj : listal){
                Object[] list_object = new Object[4];
                list_object[0] = obj[0];
                list_object[1] = obj[1];
                list_object[2] = obj[2];

                switch((int)obj[0]){
                    case 6:
                        GenericaMensagem.warn("Atenção", obj[2].toString());
                        list_object[3] = DataHoje.hora();
                        listaLogs.add(list_object);
                        break;
                    case 7:
                        GenericaMensagem.warn("Atenção", obj[2].toString());
                        list_object[3] = DataHoje.hora();
                        listaLogs.add(list_object);
                        break;
                    case 8:
                        if ( !obj[1].toString().isEmpty() ){
                            list_object[3] = DataHoje.hora();                            
                            GenericaMensagem.error("Boleto não encontrado", obj[1].toString());
                            listaLogs.add(list_object);
                        }
                        break;
                }
            }
            
            loadListaArquivosBaixar();
        } catch (Exception e) {
            
        }
    }
    
    public void fileUpload(FileUploadEvent event) {
        String cod = "";
        if (contaCobranca.getLayout().getId() != 2) {
            cod = contaCobranca.getCodCedente();
        } else{
            return;
        }
        
        Diretorio.criar("Arquivos/retorno/social/" + cod + "/");
        Diretorio.criar("Arquivos/retorno/social/" + cod + "/pendentes/");

        String caminhoB = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/retorno/social/" + cod + "/pendentes/");

        String file_name = event.getFile().getFileName();
        String caminho = caminhoB;
        caminho = caminho + "/" + file_name;
        try {
            File fl = new File(caminho);

            FileOutputStream out;
            InputStream in = event.getFile().getInputstream();
            out = new FileOutputStream(fl.getPath());

            byte[] buf = new byte[(int) event.getFile().getSize()];
            int count;
            while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }

            in.close();
            out.flush();
            out.close();
            
            if (!verificaArquivos(fl, contaCobranca)) {
                GenericaMensagem.error("Erro "+ fl.getName(), "Arquivo não pode ser enviado, verifique se o ARQUIVO e a CONTA estão corretos!");
                fl.delete();
            }
            
            loadListaArquivosBaixar();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    public boolean verificaArquivos(File filex, ContaCobranca scc) {
        try {
            BufferedReader buffReader;
            String linha;
            try (Reader reader = new FileReader(filex)) {
                buffReader = new BufferedReader(reader);
                linha = buffReader.readLine();
            }
            buffReader.close();
            // CAIXA FEDERAL --------------------------------------------------------------------------------
            if (ArquivoRetorno.CAIXA_FEDERAL == scc.getContaBanco().getBanco().getId()) {
                if (ArquivoRetorno.SICOB == scc.getLayout().getId()) {
                    return linha.substring(59, 70).equals(scc.getCodCedente());
                } else if (ArquivoRetorno.SINDICAL == scc.getLayout().getId()) {
                    return linha.substring(33, 38).equals(scc.getSicasSindical());
                } else if (ArquivoRetorno.SIGCB == scc.getLayout().getId()) {
                    return linha.substring(58, 64).equals(scc.getCodCedente());
                }
                // BANCO DO BRASIL ------------------------------------------------------------------------------
            } else if (ArquivoRetorno.BANCO_BRASIL == scc.getContaBanco().getBanco().getId()) {
                if (ArquivoRetorno.SICOB == scc.getLayout().getId()) {
                    return linha.substring(66, 70).equals(scc.getCodCedente());
                } else if (ArquivoRetorno.SINDICAL == scc.getLayout().getId()) {
                    return false;
                } else if (ArquivoRetorno.SIGCB == scc.getLayout().getId()) {
                    return false;
                }
                // REAL --------------------------------------------------------------------------------------
            } else if (ArquivoRetorno.REAL == scc.getContaBanco().getBanco().getId()) {
                if (ArquivoRetorno.SICOB == scc.getLayout().getId()) {
                    return linha.substring(63, 70).equals(scc.getCodCedente());
                } else if (ArquivoRetorno.SINDICAL == scc.getLayout().getId()) {
                    return false;
                } else if (ArquivoRetorno.SIGCB == scc.getLayout().getId()) {
                    return false;
                }
                // ITAU --------------------------------------------------------------------------------------
            } else if (ArquivoRetorno.ITAU == scc.getContaBanco().getBanco().getId()) {
                if (ArquivoRetorno.SICOB == scc.getLayout().getId()) {
                    return linha.substring(32, 37).equals(scc.getCodCedente());
                } else if (ArquivoRetorno.SINDICAL == scc.getLayout().getId()) {
                    return false;
                } else if (ArquivoRetorno.SIGCB == scc.getLayout().getId()) {
                    return false;
                }
            } else if (ArquivoRetorno.SANTANDER == scc.getContaBanco().getBanco().getId()) {
                if (ArquivoRetorno.SICOB == scc.getLayout().getId()) {
                    int codc = Integer.valueOf(linha.substring(53, 61));
                    int compara = Integer.valueOf(scc.getCodCedente());
                    return codc == compara;
                } else if (ArquivoRetorno.SINDICAL == scc.getLayout().getId()) {
                    return false;
                } else if (ArquivoRetorno.SIGCB == scc.getLayout().getId()) {
                    return false;
                }
            } else if (ArquivoRetorno.SICOOB == scc.getContaBanco().getBanco().getId()) {
                if (ArquivoRetorno.SICOB == scc.getLayout().getId()) {
                    int codc = Integer.valueOf(linha.substring(31, 40));
                    int compara = Integer.valueOf(scc.getCodCedente());
                    return codc == compara;
                } else if (ArquivoRetorno.SINDICAL == scc.getLayout().getId()) {
                    return false;
                } else if (ArquivoRetorno.SIGCB == scc.getLayout().getId()) {
                    return false;
                }
            }
        } catch (IOException | NumberFormatException e) {
        }
        return false;
    }    

    public void loadListaArquivosBaixar(){
        listaArquivosPendentes.clear();
        String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/retorno/social");
        caminho = caminho+"/"+contaCobranca.getCodCedente()+"/pendentes";
        try {
            File filex = new File(caminho);
            File listFile[] = filex.listFiles();

            for (File linha_file : listFile) {
                listaArquivosPendentes.add(linha_file.getName());
            }
        } catch (Exception e) {
        }
    }
    
    public void loadListaContas(){
        listaContas.clear();
        
        ServicoContaCobrancaDB servDB = new ServicoContaCobrancaDBToplink();
        List<ContaCobranca> result = servDB.listaContaCobrancaAtivoAssociativo();
        if (result.isEmpty()) {
            listaContas.add(new SelectItem(0, "Nenhuma Conta Encontrada", "0"));
            return;
        }
        int contador = 0;
        for (int i = 0; i < result.size(); i++) {
            // LAYOUT 2 = SINDICAL
            if (result.get(i).getLayout().getId() != 2) {
                listaContas.add(
                        new SelectItem(
                                contador,
                                result.get(i).getApelido() + " - " + result.get(i).getCodCedente(), // CODCEDENTE NO CASO DE OUTRAS
                                Integer.toString(result.get(i).getId())
                        )
                );
                contador++;
            }
        }

        if (!listaContas.isEmpty()) {
            contaCobranca = (ContaCobranca) new Dao().find(new ContaCobranca(), Integer.parseInt(((SelectItem) listaContas.get(indexConta)).getDescription()));
        }
    }
    
    public void atualizaContaCobranca() {
        if (!listaContas.isEmpty()) {
            contaCobranca = (ContaCobranca) new Dao().find(new ContaCobranca(), Integer.parseInt(((SelectItem) listaContas.get(indexConta)).getDescription()));
        }
        loadListaArquivosBaixar();
    }
    

    public void limparArquivosEnviados() {
        String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/retorno/social");
        caminho = caminho+"/"+contaCobranca.getCodCedente()+"/pendentes";
        try {
            File filex = new File(caminho);
            File listFile[] = filex.listFiles();

            for (File linha_file : listFile) {
                linha_file.delete();
            }
            GenericaMensagem.info("OK", "Arquivos Excluídos!");
            loadListaArquivosBaixar();
        } catch (Exception e) {
            GenericaMensagem.error("Erro", "Não foi possível excluir arquivos, tente novamente!");
        }
    }
    
    public int getIndexConta() {
        return indexConta;
    }

    public void setIndexConta(int indexConta) {
        this.indexConta = indexConta;
    }

    public List<SelectItem> getListaContas() {
        return listaContas;
    }

    public void setListaContas(List<SelectItem> listaContas) {
        this.listaContas = listaContas;
    }

    public ContaCobranca getContaCobranca() {
        return contaCobranca;
    }

    public void setContaCobranca(ContaCobranca contaCobranca) {
        this.contaCobranca = contaCobranca;
    }

    public List<String> getListaArquivosPendentes() {
        return listaArquivosPendentes;
    }

    public void setListaArquivosPendentes(List<String> listaArquivosPendentes) {
        this.listaArquivosPendentes = listaArquivosPendentes;
    }

    public List<Object[]> getListaLogs() {
        return listaLogs;
    }

    public void setListaLogs(List<Object[]> listaLogs) {
        this.listaLogs = listaLogs;
    }
    
}