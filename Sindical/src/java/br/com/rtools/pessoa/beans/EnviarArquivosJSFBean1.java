//package br.com.rtools.pessoa.beans;
//
//import br.com.rtools.arrecadacao.Convencao;
//import br.com.rtools.arrecadacao.GrupoCidade;
//import br.com.rtools.pessoa.Cnae;
//import br.com.rtools.pessoa.Juridica;
//import br.com.rtools.pessoa.Pessoa;
//import br.com.rtools.pessoa.db.EnviarArquivosDB;
//import br.com.rtools.pessoa.db.EnviarArquivosDBToplink;
//import br.com.rtools.seguranca.Registro;
//import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
//import br.com.rtools.sistema.Mensagem;
//import br.com.rtools.utilitarios.DataObject;
//import br.com.rtools.utilitarios.EnviarEmail;
//import br.com.rtools.utilitarios.SalvarAcumuladoDB;
//import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//import javax.faces.context.FacesContext;
//import javax.servlet.ServletContext;
//import org.richfaces.event.FileUploadEvent;
//import org.richfaces.model.UploadedFile;
//
//public class EnviarArquivosJSFBean1 implements java.io.Serializable {
//
//    private List<DataObject> listaEmpresas = new ArrayList();
//    private List<DataObject> listaContribuinte = new ArrayList();
//    private List itens = new ArrayList();
//    private List listaArquivos = new ArrayList();
//    private boolean chkMarcaTodos = false;
//    private boolean item = false;
//    private String assunto = "";
//    private String msgConfirma = "";
//    private String conteudoHTML = "";
//    private int idIndexEmpresas = -1;
//    private int idIndexArquivos = -1;
//    private int quantidadeAnexo = 0;
//    private Mensagem mensagem = new Mensagem();
//    private List<Convencao> listaConvencao = new ArrayList();
//    private List<DataObject> listaConvencaoObject = new ArrayList();
//    private boolean marcarTodosConvencao = false;
//    private List<GrupoCidade> listaGrupoCidadeConvencao = new ArrayList();
//    private List<DataObject> listaGrupoCidadeConvencaoObject = new ArrayList();
//    private boolean marcarTodosGrupoCidade = false;
//    private List<Cnae> listaCnae = new ArrayList();
//    private List<DataObject> listaCnaeObject = new ArrayList();
//    private boolean marcarTodosCnaeConvencao = false;
//
//    public String novo(String tipoNovo) {
//        setItem(false);
//        assunto = "";
//        msgConfirma = "";
//        conteudoHTML = "";
//        chkMarcaTodos = false;
//        quantidadeAnexo = 0;
//        getListaArquivosContabilidade().clear();
//        getListaArquivosContribuinte().clear();
//        listaArquivos = new ArrayList();
//        listaEmpresas.clear();
//        listaContribuinte.clear();
//        itens.clear();
//        itens = new ArrayList();
//        idIndexArquivos = -1;
//        idIndexEmpresas = -1;
//        mensagem = new Mensagem();
//        return tipoNovo;
//    }
//
//    public String enviarArquivos() {
//        if (mensagem.getAssunto().isEmpty()) {
//            msgConfirma = "O assunto não pode ser Nulo!";
//            return null;
//        }
//        if (mensagem.getMensagem().isEmpty()) {
//            msgConfirma = "O conteudo HTML não pode ser Nulo!";
//            return null;
//        }
//        List aux = new ArrayList();
//        boolean isEnviar = false;
//        for (int i = 0; i < listaEmpresas.size(); i++) {
//            if ((Boolean) listaEmpresas.get(i).getArgumento0()) {
//                Pessoa pessoa = ((Juridica) listaEmpresas.get(i).getArgumento1()).getPessoa();
//                aux.add(pessoa);
//                isEnviar = true;
//            }
//        }
//        if (!isEnviar) {
//            msgConfirma = "Selecionar destinatario(s)!";
//            return null;            
//        }
//        List aux2 = new ArrayList();
//        for (int i = 0; i < listaArquivos.size(); i++) {
//            aux2.add((File) ((DataObject) listaArquivos.get(i)).getArgumento0());
//        }
//        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
//        String[] retorno = EnviarEmail.EnviarEmailPersonalizado((Registro) salvarAcumuladoDB.pesquisaCodigo(1, "Registro"), aux, mensagem.getMensagem(), aux2, mensagem.getAssunto());
//        if (retorno[1].isEmpty()) {
//            if (!listaArquivos.isEmpty()) {
//                msgConfirma = "Emails " + retorno[0];
//            } else {
//                msgConfirma = "Email " + retorno[0];
//            }
//        } else {
//            msgConfirma = retorno[1];
//        }
//        return null;
//    }
//
//    public void excluirArquivosContabilidade(int index) {
//        String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/Anexos/Pendentes/ArquivoContabilidade/" + (String) ((DataObject) listaArquivos.get(index)).getArgumento1());
//        File fl = new File(caminho);
//        fl.delete();
//        listaArquivos.remove(index);
//        listaArquivos.clear();
//    }
//
//    public void excluirArquivosContribuinte(int index) {
//        String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/Anexos/Pendentes/ArquivoContribuinte/" + (String) ((DataObject) listaArquivos.get(index)).getArgumento1());
//        File fl = new File(caminho);
//        fl.delete();
//        listaArquivos.remove(index);
//        listaArquivos.clear();
//    }
//
//    public void adicionarArquivo(FileUploadEvent event) {
//        this.itens.add(event.getUploadedFile());
//        UploadedFile uploadedFile = event.getUploadedFile();
//        String nomeArq = event.getUploadedFile().getName();
//        String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/Anexos/");
//        try {
//            File fl = new File(caminho);
//            fl.mkdir();
//            if (!new File(caminho + "/Pendentes/").exists()) {
//                File fl2 = new File(caminho + "/Pendentes/");
//                fl2.mkdir();
//            }
//            fl = new File(caminho + "/Pendentes/" + nomeArq);
//            InputStream in = uploadedFile.getInputStream();
//            FileOutputStream out = new FileOutputStream(fl.getPath());
//
//            byte[] buf = new byte[(int) uploadedFile.getSize()];
//            int count;
//            while ((count = in.read(buf)) >= 0) {
//                out.write(buf, 0, count);
//            }
//            in.close();
//            out.flush();
//            out.close();
//            listaArquivos.clear();
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }
//
////    public String marcar(int index) {
////        if (!listaEmpresas.isEmpty()) {
////            if ((Boolean) (listaEmpresas.get(index).getArgumento0()) == false) {
////                listaEmpresas.get(index).setArgumento0(false);
////            } else {
////                listaEmpresas.get(index).setArgumento0(true);
////            }
////        }
////        return null;
////    }
//    public String marcaTodos() {
//        if (!listaEmpresas.isEmpty()) {
//            for (int i = 0; i < listaEmpresas.size(); i++) {
//                listaEmpresas.get(i).setArgumento0(chkMarcaTodos);
//            }
//        }
//        return null;
//    }
//
//    public List<DataObject> getListaEmpresas() {
//        if (listaEmpresas.isEmpty()) {
//            EnviarArquivosDB db = new EnviarArquivosDBToplink();
//            List lista = db.pesquisaContabilidades();
//            for (int i = 0; i < lista.size(); i++) {
//                Juridica juridica = db.pesquisaCodigo((Integer) ((List) lista.get(i)).get(0));
//                String nomeDocumento = "";
//                if (!juridica.getPessoa().getDocumento().equals("") && !juridica.getPessoa().getDocumento().equals("0")) {
//                    nomeDocumento = "CNPJ: " + juridica.getPessoa().getDocumento() + " - ";
//                }
//                nomeDocumento += juridica.getPessoa().getNome();
//                listaEmpresas.add(new DataObject(item, juridica, nomeDocumento, ((List) lista.get(i)).get(2), ((List) lista.get(i)).get(3), ((List) lista.get(i)).get(4)));
//
//            }
//        }
//        return listaEmpresas;
//    }
//
//    public void setListaEmpresas(List<DataObject> listaEmpresas) {
//        this.listaEmpresas = listaEmpresas;
//    }
//
//    public List getListaArquivosContabilidade() {
//        if (listaArquivos.isEmpty()) {
//            String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/Anexos/Pendentes/ArquivoContabilidade");
//            try {
//                File files = new File(caminho);
//                File listFile[] = files.listFiles();
//                int numArq = listFile.length;
//                int i = 0;
//                while (i < numArq) {
//                    listaArquivos.add(new DataObject(listFile[i], listFile[i].getName()));
//                    i++;
//                }
//                if (listaArquivos.size() > 0) {
//                    setQuantidadeAnexo(listaArquivos.size());
//                } else {
//                    setQuantidadeAnexo(0);
//                }
//                itens.clear();
//            } catch (Exception e) {
//                return new ArrayList();
//            }
//        }
//        return listaArquivos;
//    }
//
//    public List getListaArquivosContribuinte() {
//        if (listaArquivos.isEmpty()) {
//            String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/Anexos/Pendentes/ArquivoContribuinte");
//            try {
//                File files = new File(caminho);
//                File listFile[] = files.listFiles();
//                int numArq = listFile.length;
//                int i = 0;
//                while (i < numArq) {
//                    listaArquivos.add(new DataObject(listFile[i], listFile[i].getName()));
//                    i++;
//                }
//                if (listaArquivos.size() > 0) {
//                    setQuantidadeAnexo(listaArquivos.size());
//                } else {
//                    setQuantidadeAnexo(0);
//                }
//                itens.clear();
//            } catch (Exception e) {
//                return new ArrayList();
//            }
//        }
//        return listaArquivos;
//    }
//
//    public void setListaArquivos(List listaArquivos) {
//        this.listaArquivos = listaArquivos;
//    }
//
//    public boolean isChkMarcaTodos() {
//        return chkMarcaTodos;
//    }
//
//    public void setChkMarcaTodos(boolean chkMarcaTodos) {
//        this.chkMarcaTodos = chkMarcaTodos;
//    }
//
//    public String getAssunto() {
//        return assunto;
//    }
//
//    public void setAssunto(String assunto) {
//        this.assunto = assunto;
//    }
//
//    public String getMsgConfirma() {
//        return msgConfirma;
//    }
//
//    public void setMsgConfirma(String msgConfirma) {
//        this.msgConfirma = msgConfirma;
//    }
//
//    public String getConteudoHTML() {
//        return conteudoHTML;
//    }
//
//    public void setConteudoHTML(String conteudoHTML) {
//        this.conteudoHTML = conteudoHTML;
//    }
//
//    public int getIdIndexEmpresas() {
//        return idIndexEmpresas;
//    }
//
//    public void setIdIndexEmpresas(int idIndexEmpresas) {
//        this.idIndexEmpresas = idIndexEmpresas;
//    }
//
//    public int getIdIndexArquivos() {
//        return idIndexArquivos;
//    }
//
//    public void setIdIndexArquivos(int idIndexArquivos) {
//        this.idIndexArquivos = idIndexArquivos;
//    }
//
//    public List getItens() {
//        return itens;
//    }
//
//    public void setItens(List itens) {
//        this.itens = itens;
//    }
//
//    public int getQuantidadeAnexo() {
//        return quantidadeAnexo;
//    }
//
//    public void setQuantidadeAnexo(int quantidadeAnexo) {
//        this.quantidadeAnexo = quantidadeAnexo;
//    }
//
//    public boolean isItem() {
//        return item;
//    }
//
//    public void setItem(boolean item) {
//        this.item = item;
//    }
//
//    public Mensagem getMensagem() {
//        return mensagem;
//    }
//
//    public void setMensagem(Mensagem mensagem) {
//        this.mensagem = mensagem;
//    }
//
//    public List<DataObject> getListaContribuinte() {
//        if (listaEmpresas.isEmpty()) {
//            EnviarArquivosDB db = new EnviarArquivosDBToplink();
//            List listCon = new ArrayList();
//            List listGC = new ArrayList();
//            List listCnae = new ArrayList();
//
//            for (int x = 0; x < listaConvencaoObject.size(); x++) {
//                if ((Boolean) (listaConvencaoObject.get(x)).getArgumento0() == true) {
//                    listCon.add(((Convencao) (listaConvencaoObject.get(x).getArgumento1())));
//                }
//            }
//            for (int y = 0; y < listaGrupoCidadeConvencaoObject.size(); y++) {
//                if ((Boolean) (listaGrupoCidadeConvencaoObject.get(y)).getArgumento0() == true) {
//                    listGC.add(((GrupoCidade) (listaGrupoCidadeConvencaoObject.get(y).getArgumento1())));
//                }
//            }
//            for (int z = 0; z < listaCnaeObject.size(); z++) {
//                if ((Boolean) (listaCnaeObject.get(z)).getArgumento0() == true) {
//                    listCnae.add(((Cnae) (listaCnaeObject.get(z).getArgumento1())));
//                }
//            }
//            List lista = db.pesquisaContribuintes(listCon, listGC, listCnae);
//            for (int i = 0; i < lista.size(); i++) {
//                Juridica juridica = db.pesquisaCodigo((Integer) ((List) lista.get(i)).get(0));
//                String nomeDocumento = "";
//                if (!juridica.getPessoa().getDocumento().equals("") && !juridica.getPessoa().getDocumento().equals("0")) {
//                    nomeDocumento = "CNPJ: " + juridica.getPessoa().getDocumento() + " - ";
//                }
//                nomeDocumento += juridica.getPessoa().getNome();
//                listaEmpresas.add(new DataObject(item, juridica, nomeDocumento, ((List) lista.get(i)).get(2), ((List) lista.get(i)).get(3), ""));
//            }
//        }
//        return listaEmpresas;
//    }
//
//    public void setListaContribuinte(List<DataObject> listaContribuinte) {
//        this.listaContribuinte = listaContribuinte;
//    }
//
//    // CONVENÇÃO
//    public List<Convencao> getListaConvencao() {
//        if (listaConvencao.isEmpty()) {
//            EnviarArquivosDB enviarArquivosDB = new EnviarArquivosDBToplink();
//            listaConvencao = enviarArquivosDB.listaConvencao();
//        }
//        return listaConvencao;
//    }
//
//    public void setListaConvencao(List<Convencao> listaConvencao) {
//        this.listaConvencao = listaConvencao;
//    }
//
//    public List<DataObject> getListaConvencaoObject() {
//        if (listaConvencaoObject.isEmpty()) {
//            EnviarArquivosDB enviarArquivosDB = new EnviarArquivosDBToplink();
//            List<Convencao> list = enviarArquivosDB.listaConvencao();
//            for (int i = 0; i < list.size(); i++) {
//                listaConvencaoObject.add(new DataObject(false, list.get(i)));
//            }
//        }
//        return listaConvencaoObject;
//    }
//
//    public void setListaConvencaoObject(List<DataObject> listaConvencaoObject) {
//        this.listaConvencaoObject = listaConvencaoObject;
//    }
//
//    public String marcarConvencao(int index) {
//        if (!listaConvencaoObject.isEmpty()) {
//            if ((Boolean) (listaConvencaoObject.get(index).getArgumento0()) == true) {
//                listaConvencaoObject.get(index).setArgumento0(true);
//            } else {
//                listaConvencaoObject.get(index).setArgumento0(false);
//            }
//        }
//        listaEmpresas.clear();
//        listaGrupoCidadeConvencao.clear();
//        listaGrupoCidadeConvencaoObject.clear();
//        listaCnae.clear();
//        listaCnaeObject.clear();
//        getListaGrupoCidadeConvencao();
//        getListaGrupoCidadeConvencaoObject();
//        getListaCnae();
//        getListaCnaeObject();
//        return null;
//    }
//
//    public String ckeckTodasConvencoes() {
//        if (!listaConvencaoObject.isEmpty()) {
//            boolean isMarcar;
//            if (marcarTodosConvencao) {
//                isMarcar = true;
//            } else {
//                isMarcar = false;
//            }
//            if (!listaConvencaoObject.isEmpty()) {
//                for (int i = 0; i < listaConvencaoObject.size(); i++) {
//                    listaConvencaoObject.get(i).setArgumento0(isMarcar);
//                }
//            }
//        }
//        listaEmpresas.clear();
//        listaGrupoCidadeConvencao.clear();
//        listaGrupoCidadeConvencaoObject.clear();
//        listaCnae.clear();
//        listaCnaeObject.clear();
//        getListaGrupoCidadeConvencao();
//        getListaGrupoCidadeConvencaoObject();
//        getListaCnae();
//        getListaCnaeObject();
//        return null;
//    }
//
//    public boolean isMarcarTodosConvencao() {
//        return marcarTodosConvencao;
//    }
//
//    public void setMarcarTodosConvencao(boolean marcarTodosConvencao) {
//        this.marcarTodosConvencao = marcarTodosConvencao;
//    }
//
//    // GRUPO CIDADE POR CONVENÇÃO
//    public List<GrupoCidade> getListaGrupoCidadeConvencao() {
//        if (listaGrupoCidadeConvencao.isEmpty()) {
//            EnviarArquivosDB enviarArquivosDB = new EnviarArquivosDBToplink();
//            List list = new ArrayList();
//            for (int i = 0; i < listaConvencaoObject.size(); i++) {
//                if ((Boolean) (listaConvencaoObject.get(i).getArgumento0()) == true) {
//                    list.add(((Convencao) listaConvencaoObject.get(i).getArgumento1()));
//                }
//            }
//            if (!list.isEmpty()) {
//                listaGrupoCidadeConvencao = enviarArquivosDB.listaConvencaoGrupoCidade(list);
//            }
//        }
//        return listaGrupoCidadeConvencao;
//    }
//
//    public void setListaGrupoCidadeConvencao(List<GrupoCidade> listaGrupoCidadeConvencao) {
//        this.listaGrupoCidadeConvencao = listaGrupoCidadeConvencao;
//    }
//
//    public List<DataObject> getListaGrupoCidadeConvencaoObject() {
//        if (listaGrupoCidadeConvencaoObject.isEmpty()) {
//            for (int i = 0; i < getListaGrupoCidadeConvencao().size(); i++) {
//                listaGrupoCidadeConvencaoObject.add(new DataObject(false, listaGrupoCidadeConvencao.get(i)));
//            }
//        }
//        return listaGrupoCidadeConvencaoObject;
//    }
//
//    public void setListaGrupoCidadeConvencaoObject(List<DataObject> listaGrupoCidadeConvencaoObject) {
//        this.listaGrupoCidadeConvencaoObject = listaGrupoCidadeConvencaoObject;
//    }
//
//    public boolean isMarcarTodosGrupoCidade() {
//        return marcarTodosGrupoCidade;
//    }
//
//    public void setMarcarTodosGrupoCidade(boolean marcarTodosGrupoCidade) {
//        this.marcarTodosGrupoCidade = marcarTodosGrupoCidade;
//    }
//
//    public String marcarGrupoCidade(int index) {
//        if (!listaGrupoCidadeConvencaoObject.isEmpty()) {
//            if ((Boolean) (listaGrupoCidadeConvencaoObject.get(index).getArgumento0()) == true) {
//                listaGrupoCidadeConvencaoObject.get(index).setArgumento0(true);
//            } else {
//                listaGrupoCidadeConvencaoObject.get(index).setArgumento0(false);
//            }
//        }
//        listaEmpresas.clear();
//        return null;
//    }
//
//    public String ckeckTodasGrupoCidade() {
//        if (!listaGrupoCidadeConvencaoObject.isEmpty()) {
//            boolean isMarcar;
//            if (marcarTodosGrupoCidade) {
//                isMarcar = true;
//            } else {
//                isMarcar = false;
//            }
//            if (!listaGrupoCidadeConvencaoObject.isEmpty()) {
//                for (int i = 0; i < listaGrupoCidadeConvencaoObject.size(); i++) {
//                    listaGrupoCidadeConvencaoObject.get(i).setArgumento0(isMarcar);
//                }
//            }
//        }
//        listaEmpresas.clear();
//        return null;
//    }
//
//    // CNAE CONVENÇÃO
//    public List<Cnae> getListaCnae() {
//        if (listaCnae.isEmpty()) {
//            EnviarArquivosDB enviarArquivosDB = new EnviarArquivosDBToplink();
//            List list = new ArrayList();
//            for (int i = 0; i < listaConvencaoObject.size(); i++) {
//                if ((Boolean) (listaConvencaoObject.get(i).getArgumento0()) == true) {
//                    list.add(((Convencao) listaConvencaoObject.get(i).getArgumento1()));
//                }
//            }
//            if (!list.isEmpty()) {
//                listaCnae = enviarArquivosDB.listaCnaeConvencao(list);
//            }
//        }
//        return listaCnae;
//    }
//
//    public void setListaCnae(List<Cnae> listaCnae) {
//        this.listaCnae = listaCnae;
//    }
//
//    public List<DataObject> getListaCnaeObject() {
//        getListaCnae();
//        for (int i = 0; i < listaCnae.size(); i++) {
//            listaCnaeObject.add(new DataObject(false, listaCnae.get(i)));
//        }
//        return listaCnaeObject;
//    }
//
//    public void setListaCnaeObject(List<DataObject> listaCnaeObject) {
//        this.listaCnaeObject = listaCnaeObject;
//    }
//
//    public boolean isMarcarTodosCnaeConvencao() {
//        return marcarTodosCnaeConvencao;
//    }
//
//    public void setMarcarTodosCnaeConvencao(boolean marcarTodosCnaeConvencao) {
//        this.marcarTodosCnaeConvencao = marcarTodosCnaeConvencao;
//    }
//
//    public String marcarCnaeConvencao(int index) {
//        if ((Boolean) (listaCnaeObject.get(index).getArgumento0()) == true) {
//            listaCnaeObject.get(index).setArgumento0(true);
//        } else {
//            listaCnaeObject.get(index).setArgumento0(false);
//        }
//        listaEmpresas.clear();
//        return null;
//    }
//
//    public String ckeckTodasCnaeConvencao() {
//        boolean isMarcar;
//        if (marcarTodosCnaeConvencao) {
//            isMarcar = true;
//        } else {
//            isMarcar = false;
//        }
//        if (!listaCnaeObject.isEmpty()) {
//            for (int i = 0; i < listaCnaeObject.size(); i++) {
//                listaCnaeObject.get(i).setArgumento0(isMarcar);
//            }
//        }
//        listaEmpresas.clear();
//        return null;
//    }
//}