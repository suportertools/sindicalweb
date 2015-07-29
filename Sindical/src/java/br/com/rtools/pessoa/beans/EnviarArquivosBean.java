package br.com.rtools.pessoa.beans;

import br.com.rtools.arrecadacao.Convencao;
import br.com.rtools.arrecadacao.GrupoCidade;
import br.com.rtools.arrecadacao.lista.ListaRelatorioContabilidade;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.pessoa.Cnae;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.db.EnviarArquivosDB;
import br.com.rtools.pessoa.db.EnviarArquivosDBToplink;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.sistema.ConfiguracaoUpload;
import br.com.rtools.sistema.Email;
import br.com.rtools.sistema.EmailPessoa;
import br.com.rtools.sistema.Mensagem;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.Diretorio;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Mail;
import br.com.rtools.utilitarios.Upload;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.primefaces.event.FileUploadEvent;

@ManagedBean
@SessionScoped
public class EnviarArquivosBean implements Serializable {

    private Mensagem mensagem = new Mensagem();

    private Map<String, Integer> convencaos;
    private Map<String, Integer> grupoCidades;
    private Map<String, Integer> cnaes;

    private List convencaoSelecionada = new ArrayList();
    private List grupoCidadeSelecionada = new ArrayList();
    private List cnaeSelecionado = new ArrayList();
    private ListaRelatorioContabilidade[] empresaSelecionada = null;
    private Juridica[] contribuinteSelecionado = null;

    private List<ListaRelatorioContabilidade> listaContabilidades = new ArrayList();
    private List<ListaRelatorioContabilidade> listaContabilidadesPesquisa = new ArrayList();

    private List<Juridica> listaContribuintes = new ArrayList();
    private List<Juridica> listaContribuintesPesquisa = new ArrayList();

    private List itens = new ArrayList();
    private List listaArquivos = new ArrayList();
    private boolean chkMarcaTodos = false;
    private boolean item = false;
    private boolean adicionar = false;
    private String assunto = "";
    private String msgConfirma = "";
    private String conteudoHTML = "";
    private int quantidadeAnexo = 0;
    private String tipo = "";

    private String descricao = "";
    
    private boolean empresaDebito = false;
    private String cobrarAteVencimento = "";
    private List<DataObject> listaServicosAteVencimento = new ArrayList();
    private boolean marcarServicos = true;

    public EnviarArquivosBean(){
        DataHoje dh = new DataHoje();
        cobrarAteVencimento = dh.decrementarDias(1, DataHoje.data());
        
        loadListaServicosAteVencimento();
    }
    
    public void loadListaServicosAteVencimento(){
        listaServicosAteVencimento.clear();
        EnviarArquivosDB db = new EnviarArquivosDBToplink();
        
        List<Servicos> result = db.listaServicosAteVencimento();
        
        for (Servicos s : result){
            listaServicosAteVencimento.add(new DataObject(true, s));
        }
    }
    
    public void marcar(){
        for (DataObject d : listaServicosAteVencimento){
            d.setArgumento0(marcarServicos);
        }
        listaContribuintes.clear();
    }
    
    /* EMPRESA */
    public void limparEmpresa() {
        listaContribuintes.clear();
    }

    public void todasEmpresa() {
        listaContribuintes.clear();
        adicionar = false;
    }

    public void adicionarEmpresa(Juridica linha) {
        if (!adicionar) {
            listaContribuintes.clear();
            listaContribuintes.add(linha);
            adicionar = true;
        } else {
            listaContribuintes.add(linha);
        }
    }

    public void filtrarEmpresa() {
        listaContribuintesPesquisa.clear();

        List<Juridica> lista = new ArrayList();
        getListaContribuintesPesquisa();

        for (int i = 0; i < listaContribuintesPesquisa.size(); i++) {
            if (listaContribuintesPesquisa.get(i).getPessoa().getNome().toUpperCase().contains(descricao.toUpperCase())
                    || listaContribuintesPesquisa.get(i).getPessoa().getDocumento().toUpperCase().contains(descricao.toUpperCase())) {
                lista.add(listaContribuintesPesquisa.get(i));
            }

            if (lista.size() == 10) {
                break;
            }
        }
        if (lista.isEmpty()) {
            descricao = "";
        }

        listaContribuintesPesquisa.clear();
        listaContribuintesPesquisa.addAll(lista);
    }

    /* FIM EMPRESA */
    /* CONTABILIDADE */
    public void todasContabilidade() {
        listaContabilidades.clear();
        adicionar = false;
    }

    public void adicionarContabilidade(ListaRelatorioContabilidade linha) {
        if (!adicionar) {
            listaContabilidades.clear();
            listaContabilidades.add(linha);
            adicionar = true;
        } else {
            listaContabilidades.add(linha);
        }
    }

    public void filtrar() {
        listaContabilidadesPesquisa.clear();

        List<ListaRelatorioContabilidade> lista = new ArrayList();
        getListaContabilidadePesquisa();

        for (int i = 0; i < listaContabilidadesPesquisa.size(); i++) {
            if (listaContabilidadesPesquisa.get(i).getJuridica().getPessoa().getNome().toUpperCase().contains(descricao.toUpperCase())
                    || listaContabilidadesPesquisa.get(i).getJuridica().getPessoa().getDocumento().toUpperCase().contains(descricao.toUpperCase())) {
                lista.add(listaContabilidadesPesquisa.get(i));
            }
            if (lista.size() == 10) {
                break;
            }
        }
        if (lista.isEmpty()) {
            descricao = "";
        }

        listaContabilidadesPesquisa.clear();
        listaContabilidadesPesquisa.addAll(lista);
    }

    public String novaContabilidade() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("enviarArquivosBean", new EnviarArquivosBean());
        return "enviarArquivosContabilidade";
    }
    /* FIM CONTABILIDADE */

    public String novoContribuinte() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("enviarArquivosBean", new EnviarArquivosBean());
        return "enviarArquivosContribuinte";
    }

    public void novo() {
        mensagem = new Mensagem();
        assunto = "";
        msgConfirma = "";
    }

    public void enviarArquivos(String tipoEnvio) {
        if (mensagem.getAssunto().isEmpty()) {
            GenericaMensagem.warn("Validação", "Informa assunto!");
            return;
        }
        if (mensagem.getMensagem().isEmpty()) {
            GenericaMensagem.warn("Validação", "Informar mensagem!");
            return;
        }
        int rotina = 0;
        boolean isEnviar = false;
        Juridica[] juridicasSelecionadas = null;
        ListaRelatorioContabilidade[] contabilidadesSelecionadas = null;
        List aux = new ArrayList();
        if (tipoEnvio.equals("contribuinte")) {
            rotina = 176;
            if (contribuinteSelecionado != null) {
                isEnviar = true;
                juridicasSelecionadas = contribuinteSelecionado;
            }
            for (Juridica j : juridicasSelecionadas) {
                Pessoa pessoa = j.getPessoa();
                aux.add(pessoa);
            }
        } else {
            rotina = 175;
            if (empresaSelecionada != null) {
                isEnviar = true;
                contabilidadesSelecionadas = empresaSelecionada;
            }
            for (ListaRelatorioContabilidade l : contabilidadesSelecionadas) {
                Pessoa pessoa = l.getJuridica().getPessoa();
                aux.add(pessoa);
            }
        }
        if (!isEnviar) {
            GenericaMensagem.warn("Validação", "Selecionar um destinatário!");
            return;
        }
        List aux2 = new ArrayList();
        for (int i = 0; i < listaArquivos.size(); i++) {
            aux2.add((File) ((DataObject) listaArquivos.get(i)).getArgumento0());
        }
//        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
//        Registro r = (Registro) salvarAcumuladoDB.pesquisaObjeto(1, "Registro");
//        String[] retorno = EnviarEmail.EnviarEmailPersonalizado(r, aux, mensagem.getMensagem(), aux2, mensagem.getAssunto());
        DaoInterface di = new Dao();
        Mail mail = new Mail();
        mail.setFiles(aux2);
        mail.setEmail(
                new Email(
                        -1,
                        DataHoje.dataHoje(),
                        DataHoje.livre(new Date(), "HH:mm"),
                        (Usuario) GenericaSessao.getObject("sessaoUsuario"),
                        (Rotina) di.find(new Rotina(), rotina),
                        null,
                        mensagem.getAssunto(),
                        mensagem.getMensagem(),
                        false,
                        false
                )
        );
        List<EmailPessoa> emailPessoas = new ArrayList<EmailPessoa>();
        EmailPessoa emailPessoa = new EmailPessoa();
        List<Pessoa> pessoas = (List<Pessoa>) aux;
        for (Pessoa p : pessoas) {
            emailPessoa.setDestinatario(p.getEmail1());
            emailPessoa.setPessoa(p);
            emailPessoa.setRecebimento(null);
            emailPessoas.add(emailPessoa);
            mail.setEmailPessoas(emailPessoas);
            emailPessoa = new EmailPessoa();
        }
        String[] retorno = mail.send("personalizado");

        if (retorno[1].isEmpty()) {
            if (!listaArquivos.isEmpty()) {
                GenericaMensagem.info("Sucesso", "Email(s) " + retorno[0]);
            } else {
                GenericaMensagem.info("Sucesso", "Email " + retorno[0]);
            }
        } else {
            msgConfirma = retorno[1];
            GenericaMensagem.warn("Falha", "Email(s) " + retorno[1]);
        }
    }

    public void excluirArquivosContabilidade(int index) {
        String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/Anexos/Pendentes/ArquivoContabilidade/" + (String) ((DataObject) listaArquivos.get(index)).getArgumento1());
        File fl = new File(caminho);
        fl.delete();
        listaArquivos.remove(index);
        listaArquivos.clear();
        getListaArquivosContabilidade();
    }

    public void excluirArquivosContribuinte(int index) {
        String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/Anexos/Pendentes/ArquivoContribuinte/" + (String) ((DataObject) listaArquivos.get(index)).getArgumento1());
        File fl = new File(caminho);
        fl.delete();
        listaArquivos.remove(index);
        listaArquivos.clear();
        getListaArquivosContribuinte();
    }

    public List<ListaRelatorioContabilidade> getListaContabilidade() {
        if (listaContabilidades.isEmpty() && !adicionar) {
            EnviarArquivosDB db = new EnviarArquivosDBToplink();
            List lista = db.pesquisaContabilidades(convencoesSelecionadasId(), gruposCidadeSelecionadosId());
            for (int i = 0; i < lista.size(); i++) {
                ListaRelatorioContabilidade listaRelatorioContabilidade = new ListaRelatorioContabilidade();
                listaRelatorioContabilidade.setJuridica(db.pesquisaCodigo((Integer) ((List) lista.get(i)).get(0)));
                String string = ((List) lista.get(i)).get(3).toString();
                int quantidade = Integer.parseInt(string);
                listaRelatorioContabilidade.setQuantidade(quantidade);
                listaContabilidades.add(listaRelatorioContabilidade);
            }
        }
        return listaContabilidades;
    }

    public void setListaContabilidade(List<ListaRelatorioContabilidade> listaContabilidades) {
        this.listaContabilidades = listaContabilidades;
    }

    public List<ListaRelatorioContabilidade> getListaContabilidadePesquisa() {
        if (listaContabilidadesPesquisa.isEmpty() && !descricao.isEmpty()) {
            EnviarArquivosDB db = new EnviarArquivosDBToplink();
            List lista = db.pesquisaContabilidades();
            for (int i = 0; i < lista.size(); i++) {
                ListaRelatorioContabilidade listaRelatorioContabilidade = new ListaRelatorioContabilidade();
                listaRelatorioContabilidade.setJuridica(db.pesquisaCodigo((Integer) ((List) lista.get(i)).get(0)));
                String string = ((List) lista.get(i)).get(3).toString();
                int quantidade = Integer.parseInt(string);
                listaRelatorioContabilidade.setQuantidade(quantidade);
                listaContabilidadesPesquisa.add(listaRelatorioContabilidade);
            }
        }
        return listaContabilidadesPesquisa;
    }

    public void setListaContabilidadePesquisa(List<ListaRelatorioContabilidade> listaContabilidadesPesquisa) {
        this.listaContabilidadesPesquisa = listaContabilidadesPesquisa;
    }

    public List getListaArquivosContabilidade() {
        if (listaArquivos.isEmpty()) {
            listaArquivos = Diretorio.listaArquivos("Arquivos/Anexos/Pendentes/ArquivoContabilidade");
            if (listaArquivos.size() > 0) {
                setQuantidadeAnexo(listaArquivos.size());
            } else {
                setQuantidadeAnexo(0);
            }
            itens.clear();
        }
        return listaArquivos;
    }

    public List getListaArquivosContribuinte() {
        if (listaArquivos.isEmpty()) {
            listaArquivos = Diretorio.listaArquivos("Arquivos/Anexos/Pendentes/ArquivoContribuinte");
            if (listaArquivos.size() > 0) {
                setQuantidadeAnexo(listaArquivos.size());
            } else {
                setQuantidadeAnexo(0);
            }
            itens.clear();
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

    public List getItens() {
        return itens;
    }

    public void setItens(List itens) {
        this.itens = itens;
    }

    public int getQuantidadeAnexo() {
        if (contribuinteSelecionado != null) {
            quantidadeAnexo = this.contribuinteSelecionado.length;
        }
        if (empresaSelecionada != null) {
            quantidadeAnexo = this.empresaSelecionada.length;
        }
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

    public List<Juridica> getListaContribuintes() {
        if (listaContribuintes.isEmpty() && !adicionar) {
            EnviarArquivosDB db = new EnviarArquivosDBToplink();
            String ids_servicos = "";
            if (empresaDebito && !cobrarAteVencimento.isEmpty()){
                for (DataObject d : listaServicosAteVencimento){
                    if ((Boolean) d.getArgumento0()){
                        if (ids_servicos.isEmpty())
                            ids_servicos = ""+((Servicos) d.getArgumento1()).getId();
                        else
                            ids_servicos += ", "+((Servicos) d.getArgumento1()).getId();
                    }
                }
            }
            
            List list = db.pesquisaContribuintes(convencoesSelecionadasId(), gruposCidadeSelecionadosId(), cnaesSelecionadosId(), empresaDebito, ids_servicos, cobrarAteVencimento);
            for (int i = 0; i < list.size(); i++) {
                Juridica juridica = db.pesquisaCodigo((Integer) ((List) list.get(i)).get(0));
                listaContribuintes.add(juridica);
            }
        }
        return listaContribuintes;
    }

    public List<Juridica> getListaContribuintesPesquisa() {
        if (listaContribuintesPesquisa.isEmpty() && !descricao.isEmpty()) {
            EnviarArquivosDB db = new EnviarArquivosDBToplink();
            String ids_servicos = "";
            if (empresaDebito && !cobrarAteVencimento.isEmpty()){
                for (DataObject d : listaServicosAteVencimento){
                    if ((Boolean) d.getArgumento0()){
                        if (ids_servicos.isEmpty())
                            ids_servicos = ""+((Servicos) d.getArgumento1()).getId();
                        else
                            ids_servicos += ", "+((Servicos) d.getArgumento1()).getId();
                    }
                }
            }
            List list = db.pesquisaContribuintes(convencoesSelecionadasId(), gruposCidadeSelecionadosId(), cnaesSelecionadosId(), empresaDebito, ids_servicos, cobrarAteVencimento);
            for (int i = 0; i < list.size(); i++) {
                Juridica juridica = db.pesquisaCodigo((Integer) ((List) list.get(i)).get(0));
                listaContribuintesPesquisa.add(juridica);
            }
        }
        return listaContribuintesPesquisa;
    }

    public Map<String, Integer> getConvencaos() {
        if (convencaos == null) {
            List<Convencao> list = new ArrayList<>();
            convencaos = new HashMap<String, Integer>();
            EnviarArquivosDB enviarArquivosDB = new EnviarArquivosDBToplink();
            if (getTipo().equals("contabilidade")) {
                list = enviarArquivosDB.listaConvencao(true);
            } else {
                list = enviarArquivosDB.listaConvencao();
            }
            for (int i = 0; i < list.size(); i++) {
                convencaos.put(list.get(i).getDescricao(), list.get(i).getId());
            }
        }
        return convencaos;
    }

    public void setConvencaos(Map<String, Integer> convencaos) {
        this.convencaos = convencaos;
    }

    public Map<String, Integer> getGrupoCidades() {
        grupoCidades = null;
        if (!convencaoSelecionada.isEmpty()) {
            grupoCidades = new HashMap<String, Integer>();
            String ids = convencoesSelecionadasId();
            if (!ids.isEmpty()) {
                EnviarArquivosDB enviarArquivosDB = new EnviarArquivosDBToplink();
                List<GrupoCidade> list = enviarArquivosDB.listaGrupoCidadePorConvencao(ids);
                for (int i = 0; i < list.size(); i++) {
                    grupoCidades.put(list.get(i).getDescricao(), list.get(i).getId());
                }
            }
        } else {
            grupoCidadeSelecionada = new ArrayList();
        }
        return grupoCidades;
    }

    public void setGrupoCidades(HashMap<String, Integer> grupoCidades) {
        this.grupoCidades = grupoCidades;
    }

    public Map<String, Integer> getCnaes() {
        cnaes = null;
        if (!convencaoSelecionada.isEmpty()) {
            cnaes = new HashMap<String, Integer>();
            String ids = convencoesSelecionadasId();
            if (!ids.isEmpty()) {
                EnviarArquivosDB enviarArquivosDB = new EnviarArquivosDBToplink();
                List<Cnae> list = enviarArquivosDB.listaCnaePorConvencao(ids);
                for (int i = 0; i < list.size(); i++) {
                    cnaes.put(list.get(i).getCnae() + " - " + list.get(i).getNumero(), list.get(i).getId());
                }
            }
        } else {
            cnaeSelecionado = new ArrayList();
        }
        return cnaes;
    }

    public void setCnaes(Map<String, Integer> cnaes) {
        this.cnaes = cnaes;
    }

    public List<Convencao> getConvencaoSelecionada() {
        return convencaoSelecionada;
    }

    public void setConvencaoSelecionada(List<Convencao> convencaoSelecionada) {
        this.convencaoSelecionada = convencaoSelecionada;
    }

    public List<GrupoCidade> getGrupoCidadeSelecionada() {
        return grupoCidadeSelecionada;
    }

    public void setGrupoCidadeSelecionada(List<GrupoCidade> grupoCidadeSelecionada) {
        this.grupoCidadeSelecionada = grupoCidadeSelecionada;
    }

    public List<Cnae> getCnaeSelecionado() {
        return cnaeSelecionado;
    }

    public void setCnaeSelecionado(List<Cnae> cnaeSelecionado) {
        this.cnaeSelecionado = cnaeSelecionado;
    }

    public ListaRelatorioContabilidade[] getEmpresaSelecionada() {
        return empresaSelecionada;
    }

    public void setEmpresaSelecionada(ListaRelatorioContabilidade[] empresaSelecionada) {
        this.empresaSelecionada = empresaSelecionada;
    }

    public Juridica[] getContribuinteSelecionado() {
        return contribuinteSelecionado;
    }

    public void setContribuinteSelecionado(Juridica[] contribuinteSelecionado) {
        this.contribuinteSelecionado = contribuinteSelecionado;
    }

    public String convencoesSelecionadasId() {
        String ids = "";
        if (convencaoSelecionada != null) {
            for (int i = 0; i < convencaoSelecionada.size(); i++) {
                if (ids.isEmpty()) {
                    ids = "" + convencaoSelecionada.get(i);
                } else {
                    ids += "," + convencaoSelecionada.get(i);
                }
            }
        }
        return ids;
    }

    public String gruposCidadeSelecionadosId() {
        String ids = "";
        if (grupoCidadeSelecionada != null) {
            for (int i = 0; i < grupoCidadeSelecionada.size(); i++) {
                if (ids.isEmpty()) {
                    ids = "" + grupoCidadeSelecionada.get(i);
                } else {
                    ids += "," + grupoCidadeSelecionada.get(i);
                }
            }
        }
        return ids;
    }

    public String cnaesSelecionadosId() {
        String ids = "";
        if (cnaeSelecionado != null) {
            for (int i = 0; i < cnaeSelecionado.size(); i++) {
                if (ids.isEmpty()) {
                    ids = "" + cnaeSelecionado.get(i);
                } else {
                    ids += "," + cnaeSelecionado.get(i);
                }
            }

        }
        return ids;
    }

    public void uploadContribuinte(FileUploadEvent event) {
        ConfiguracaoUpload configuracaoUpload = new ConfiguracaoUpload();
        configuracaoUpload.setArquivo(event.getFile().getFileName());
        configuracaoUpload.setDiretorio("Arquivos/Anexos/Pendentes/ArquivoContribuinte");
        configuracaoUpload.setEvent(event);
        if (Upload.enviar(configuracaoUpload, true)) {
            listaArquivos.clear();
        }
        getListaArquivosContribuinte();
    }

    public void uploadContabilidade(FileUploadEvent event) {
        ConfiguracaoUpload configuracaoUpload = new ConfiguracaoUpload();
        configuracaoUpload.setArquivo(event.getFile().getFileName());
        configuracaoUpload.setDiretorio("Arquivos/Anexos/Pendentes/ArquivoContabilidade");
        configuracaoUpload.setEvent(event);
        if (Upload.enviar(configuracaoUpload, true)) {
            listaArquivos.clear();
        }
        getListaArquivosContabilidade();
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isAdicionar() {
        return adicionar;
    }

    public void setAdicionar(boolean adicionar) {
        this.adicionar = adicionar;
    }

    public void defineIipoEnvio(String tipo) {
        GenericaSessao.put("enviarArquivosTipo", tipo);
    }

    public String getTipo() {
        if (GenericaSessao.exists("enviarArquivosTipo")) {
            this.tipo = GenericaSessao.getString("enviarArquivosTipo", true);
        }
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isEmpresaDebito() {
        return empresaDebito;
    }

    public void setEmpresaDebito(boolean empresaDebito) {
        this.empresaDebito = empresaDebito;
    }

    public String getCobrarAteVencimento() {
        return cobrarAteVencimento;
    }

    public void setCobrarAteVencimento(String cobrarAteVencimento) {
        this.cobrarAteVencimento = cobrarAteVencimento;
    }

    public List<DataObject> getListaServicosAteVencimento() {
        return listaServicosAteVencimento;
    }

    public void setListaServicosAteVencimento(List<DataObject> listaServicosAteVencimento) {
        this.listaServicosAteVencimento = listaServicosAteVencimento;
    }

    public boolean isMarcarServicos() {
        return marcarServicos;
    }

    public void setMarcarServicos(boolean marcarServicos) {
        this.marcarServicos = marcarServicos;
    }
}
