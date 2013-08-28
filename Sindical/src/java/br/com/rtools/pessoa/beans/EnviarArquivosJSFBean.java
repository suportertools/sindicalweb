package br.com.rtools.pessoa.beans;

import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.db.EnviarArquivosDB;
import br.com.rtools.pessoa.db.EnviarArquivosDBToplink;
import br.com.rtools.pessoa.db.FilialDB;
import br.com.rtools.pessoa.db.FilialDBToplink;
import br.com.rtools.seguranca.controleUsuario.controleUsuarioJSFBean;
import br.com.rtools.sistema.Mensagem;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.EnviarEmail;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

public class EnviarArquivosJSFBean {
    private List<DataObject> listaEmpresas = new ArrayList();
    private List itens = new ArrayList();
    private List listaArquivos = new ArrayList();
    private boolean chkMarcaTodos = true;
    private boolean item = false;
    private String assunto = "";
    private String msgConfirma = "";
    private String conteudoHTML = "";
    private int idIndexEmpresas = -1;
    private int idIndexArquivos = -1;
    private int quantidadeAnexo = 0;
    private Mensagem mensagem = new Mensagem();
    
    public String novo(){
        setItem(false);
        assunto = "";
        msgConfirma = "";
        conteudoHTML = "";
        chkMarcaTodos = false;
        quantidadeAnexo = 0;
        getListaArquivos().clear();
        listaArquivos = new ArrayList();
        listaEmpresas.clear();
        itens.clear();
        itens = new ArrayList();        
        idIndexArquivos = -1;
        idIndexEmpresas = -1;
        mensagem = new Mensagem();
        return "enviarArquivos";
    }
    
    public String enviarArquivos(){
        if (assunto.isEmpty()){
            msgConfirma = "O assunto não pode ser Nulo!";
            return null;
        }
        
        if (conteudoHTML.isEmpty()){
            msgConfirma = "O conteudo HTML não pode ser Nulo!";
            return null;
        }
        FilialDB dbf = new FilialDBToplink();
        List aux = new ArrayList();
        for (int i = 0; i < listaEmpresas.size(); i ++){
            if ((Boolean)listaEmpresas.get(i).getArgumento0()){
                Pessoa pessoa = ((Juridica) listaEmpresas.get(i).getArgumento1()).getPessoa();
                aux.add(pessoa);
            }
        }

        List aux2 = new ArrayList();
        for (int i = 0; i < listaArquivos.size(); i ++)
            aux2.add( (File)((DataObject)listaArquivos.get(i)).getArgumento0() ); 

        String[] retorno = EnviarEmail.EnviarEmailPersonalizado(dbf.pesquisaCodigoRegistro(1), aux, conteudoHTML, aux2, assunto);
        if (retorno[1].isEmpty())
            if(listaArquivos.size() > 0){
                msgConfirma = "Emails " + retorno[0];
            }else{
                msgConfirma = "Email " + retorno[0];                
            }
        else
            msgConfirma = retorno[1];
        return null;
    }
    
    public void excluirArquivos(int index){
        String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/"+controleUsuarioJSFBean.getCliente()+"/Arquivos/Anexos/Pendentes/"+(String) ((DataObject)listaArquivos.get(index)).getArgumento1());
        File fl = new File(caminho);
        fl.delete();
        listaArquivos.remove(index);
        listaArquivos.clear();
    }
      
    public void adicionarArquivo(FileUploadEvent event){
        this.itens.add(event.getUploadedFile());
        UploadedFile item = event.getUploadedFile();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String nomeArq = event.getUploadedFile().getName();
        
        String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/"+controleUsuarioJSFBean.getCliente()+"/Arquivos/Anexos/");
        try{
            File fl = new File(caminho);
            fl.mkdir();
            if(!new File(caminho+"/Pendentes/").exists()){
                File fl2 = new File(caminho+"/Pendentes/");
                fl2.mkdir();
            }
            fl = new File(caminho+"/Pendentes/"+nomeArq);
            InputStream in = item.getInputStream();
            FileOutputStream out = new FileOutputStream(fl.getPath());

            byte[] buf = new byte[(int)item.getSize()];
            int count;
            while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }
            in.close();
            out.flush();
            out.close();
            listaArquivos.clear();
        }catch(Exception e){
            System.out.println(e);
        }
    }    
    
    public String marcaTodos(){
        if (!listaEmpresas.isEmpty()){
            for (int i = 0; i < listaEmpresas.size(); i++){
                listaEmpresas.get(i).setArgumento0(chkMarcaTodos);
            }
        }     
        return null;
    }

    public List<DataObject> getListaEmpresas() {
        if (listaEmpresas.isEmpty()){
            EnviarArquivosDB db = new EnviarArquivosDBToplink();
            List lista = db.pesquisaContabilidades();
            for (int i = 0; i < lista.size(); i++){
                Juridica juridica = db.pesquisaCodigo( (Integer)((List)lista.get(i)).get(0) );
                String nomeDocumento = "";
                if(!juridica.getPessoa().getDocumento().equals("") && !juridica.getPessoa().getDocumento().equals("0")){
                    nomeDocumento = "CNPJ: " + juridica.getPessoa().getDocumento() +" - ";
                }
                nomeDocumento += juridica.getPessoa().getNome();
                listaEmpresas.add(new DataObject(item, juridica, nomeDocumento,((List)lista.get(i)).get(2), ((List)lista.get(i)).get(3), ((List)lista.get(i)).get(4)));
    
            }
        }
        return listaEmpresas;
    }

    public void setListaEmpresas(List<DataObject> listaEmpresas) {
        this.listaEmpresas = listaEmpresas;
    }

    public List getListaArquivos() {
        if (listaArquivos.isEmpty()){
            String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/"+controleUsuarioJSFBean.getCliente()+"/Arquivos/Anexos/Pendentes");
            try{
                File files = new File(caminho);
                File listFile[] = files.listFiles();
                int numArq = listFile.length;
                int i = 0;
                while (i < numArq){
                    listaArquivos.add(new DataObject(listFile[i], listFile[i].getName()));
                    i++;
                }
                if(listaArquivos.size() > 0){
                    setQuantidadeAnexo(listaArquivos.size());
                }else{
                    setQuantidadeAnexo(0);
                }
                itens.clear();
            }catch(Exception e){
                return new ArrayList();
            }       
        } 
        return listaArquivos;
    }

    public void setListaArquivos(List listaArquivos) {
        this.listaArquivos = listaArquivos;
    }

    public boolean isChkMarcaTodos() {
        return chkMarcaTodos;
    }

    public void setChkMarcaTodos(boolean chkMarcaTodos) {
        this.chkMarcaTodos = chkMarcaTodos;
    }
    
    public void clickMarcarTodos(){
        listaEmpresas.clear();
        if(item == false){
            item = true;
        }else{
            item = false;            
        }
        getListaEmpresas();
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public String getConteudoHTML() {
        return conteudoHTML;
    }

    public void setConteudoHTML(String conteudoHTML) {
        this.conteudoHTML = conteudoHTML;
    }

    public int getIdIndexEmpresas() {
        return idIndexEmpresas;
    }

    public void setIdIndexEmpresas(int idIndexEmpresas) {
        this.idIndexEmpresas = idIndexEmpresas;
    }

    public int getIdIndexArquivos() {
        return idIndexArquivos;
    }

    public void setIdIndexArquivos(int idIndexArquivos) {
        this.idIndexArquivos = idIndexArquivos;
    }

    public List getItens() {
        return itens;
    }

    public void setItens(List itens) {
        this.itens = itens;
    }

    public int getQuantidadeAnexo() {
        return quantidadeAnexo;
    }

    public void setQuantidadeAnexo(int quantidadeAnexo) {
        this.quantidadeAnexo = quantidadeAnexo;
    }

    public boolean isItem() {
        return item;
    }

    public void setItem(boolean item) {
        this.item = item;
    }

    public Mensagem getMensagem() {
        return mensagem;
    }

    public void setMensagem(Mensagem mensagem) {
        this.mensagem = mensagem;
    }
}