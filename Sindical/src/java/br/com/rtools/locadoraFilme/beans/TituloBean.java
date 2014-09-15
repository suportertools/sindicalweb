package br.com.rtools.locadoraFilme.beans;

import br.com.rtools.locadoraFilme.Genero;
import br.com.rtools.locadoraFilme.Titulo;
import br.com.rtools.locadoraFilme.dao.TituloDao;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.sistema.ConfiguracaoUpload;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.Diretorio;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.PF;
import br.com.rtools.utilitarios.Upload;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.Part;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

@ManagedBean
@SessionScoped
public class TituloBean {

    private Titulo titulo;
    private Usuario usuario;
    private String descPesquisa;
    private String porPesquisa;
    private String comoPesquisa;
    private int idGenero;
    private List<SelectItem> listGeneroCombo;
    private List<Titulo> listTitulo;
    private String fotoPerfil;
    private String fotoArquivo;
    private String fotoTempPerfil;
    private String fotoTempArquivo;
    private String fileContent;
    private Part file;
    private String[] imagensTipo;

    @PostConstruct
    public void init() {
        titulo = new Titulo();
        usuario = new Usuario();
        descPesquisa = "";
        porPesquisa = "descricao";
        comoPesquisa = "";
        idGenero = 0;
        listGeneroCombo = new ArrayList<SelectItem>();
        listTitulo = new ArrayList<Titulo>();
        fotoPerfil = "";
        fotoArquivo = "";
        fotoTempPerfil = "";
        fotoTempArquivo = "";
        fileContent = "";
        file = null;
        imagensTipo = new String[]{"jpg", "jpeg", "png", "gif"};
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("tituloPesquisa");
        clear();
    }

    public void clear() {
        GenericaSessao.remove("tituloBean");
    }

    public List<SelectItem> getListGeneroCombo() {
        if (listGeneroCombo.isEmpty()) {
            Dao dao = new Dao();
            List<Genero> list = (List<Genero>) dao.list(new Genero(), true);
            for (int i = 0; i < list.size(); i++) {
                listGeneroCombo.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        return listGeneroCombo;
    }

    public synchronized void save() {
        if (listGeneroCombo.isEmpty()) {
            GenericaMensagem.warn("Validação", "Cadastrar gêneros!");
            return;
        }
        if (titulo.getDescricao().isEmpty()) {
            GenericaMensagem.warn("Validação", "Digite o nome do titulo!");
            return;
        }
        Dao dao = new Dao();
        titulo.setGenero((Genero) dao.find(new Genero(), Integer.parseInt(listGeneroCombo.get(idGenero).getDescription())));
        if (titulo.getDuracao().isEmpty()) {
            GenericaMensagem.warn("Validação", "Informar o tempo de duração!");
            return;
        }
        int hora = Integer.parseInt(titulo.getDuracao().substring(0, 2));
        if (hora > 8) {
            GenericaMensagem.warn("Validação", "O tempo de duração deve ser inferior ou igual a 8 horas!");
            return;
        }
        int anoParametro = 1895;
        int ano = Integer.parseInt(titulo.getAnoLancamentoString());
        if (ano < anoParametro) {
            GenericaMensagem.warn("Validação", "O Ano de lançamento deve ser igual ou superior a 1895!");
            return;
        }
        NovoLog novoLog = new NovoLog();
        if (titulo.getId() == -1) {
            TituloDao tituloDao = new TituloDao();
            if (tituloDao.pesquisaTitulo(titulo.getDescricao()) != null) {
                GenericaMensagem.warn("Validação", "Titulo já existe!");
                return;
            }
            if (dao.save(titulo, true)) {
                GenericaMensagem.info("Sucesso", "Registro inserido");
                novoLog.save(""
                        + "ID: " + titulo.getId()
                        + " - Gênero: (" + titulo.getGenero().getId() + ") - " + titulo.getGenero().getDescricao()
                        + " - Título: " + titulo.getDescricao()
                        + " - Formato: " + titulo.getFormato()
                        + " - Legenda: " + titulo.getLegenda()
                        + " - Duração: " + titulo.getDuracao()
                        + " - Ano: " + titulo.getAnoLancamento()
                        + " - Autor: " + titulo.getAutor()
                );
                saveImage();
            } else {
                GenericaMensagem.warn("Erro", "Ao inserir registro!");
            }
        } else {
            Titulo t = (Titulo) dao.find(titulo);
            String beforeUpdate
                    = "ID: " + t.getId()
                    + " - Gênero: (" + t.getGenero().getId() + ") - " + t.getGenero().getDescricao()
                    + " - Título: " + t.getDescricao()
                    + " - Formato: " + t.getFormato()
                    + " - Legenda: " + t.getLegenda()
                    + " - Duração: " + t.getDuracao()
                    + " - Ano: " + t.getAnoLancamento()
                    + " - Autor: " + t.getAutor();
            if (dao.update(titulo, true)) {
                GenericaMensagem.info("Sucesso", "Registro atualizado");
                novoLog.update(beforeUpdate,
                        "ID: " + titulo.getId()
                        + " - Gênero: (" + titulo.getGenero().getId() + ") - " + titulo.getGenero().getDescricao()
                        + " - Título: " + titulo.getDescricao()
                        + " - Formato: " + titulo.getFormato()
                        + " - Legenda: " + titulo.getLegenda()
                        + " - Duração: " + titulo.getDuracao()
                        + " - Ano: " + titulo.getAnoLancamento()
                        + " - Autor: " + titulo.getAutor()
                );
                saveImage();
            } else {
                GenericaMensagem.warn("Erro", "Ao atualizar registro!");
            }
        }
    }

    public synchronized void delete() {
        if (titulo.getId() != -1) {
            Dao dao = new Dao();
            NovoLog novoLog = new NovoLog();
            if (dao.delete(titulo, true)) {
                GenericaMensagem.info("Sucesso", "Registro removido");
                novoLog.delete(""
                        + "ID: " + titulo.getId()
                        + " - Gênero: (" + titulo.getGenero().getId() + ") - " + titulo.getGenero().getDescricao()
                        + " - Título: " + titulo.getDescricao()
                        + " - Formato: " + titulo.getFormato()
                        + " - Legenda: " + titulo.getLegenda()
                        + " - Duração: " + titulo.getDuracao()
                        + " - Ano: " + titulo.getAnoLancamento()
                        + " - Autor: " + titulo.getAutor()
                );
                deleteImage();
                clear();
            } else {
                GenericaMensagem.warn("Erro", "Ao remover registro!");
            }
        } else {
            GenericaMensagem.warn("Validação", "Nenhum registro selecionado!");
        }
    }

    public String link(Titulo t) {
        titulo = t;
        GenericaSessao.put("tituloPesquisa", titulo);
        GenericaSessao.put("linkClicado", true);
        showImagem();
        if (!GenericaSessao.exists("urlRetorno")) {
            return "titulo";
        } else {
            return (String) GenericaSessao.getString("urlRetorno");
        }
    }

    public void acaoPesquisaInicial() {
        listTitulo.clear();
        comoPesquisa = "I";
    }

    public void acaoPesquisaParcial() {
        listTitulo.clear();
        comoPesquisa = "P";
    }

    public List<Titulo> getListTitulo() {
        TituloDao tituloDao = new TituloDao();
        if (descPesquisa.equals("")) {
            listTitulo = new ArrayList();
            return listTitulo;
        } else {
            listTitulo = tituloDao.pesquisaTitulos(descPesquisa, porPesquisa, comoPesquisa);
            return listTitulo;
        }
    }

    public void refreshForm() {
    }

    public String validaHora(String hora) {
        int n1 = 0;
        int n2 = 0;
        if (hora.length() == 1) {
            hora = "0" + hora + ":00";
        }

        if (hora.length() == 2) {
            if ((Integer.parseInt(hora) >= 0) && (Integer.parseInt(hora) <= 23)) {
                hora = hora + ":00";
            } else {
                hora = "";
            }
        } else if (hora.length() == 3) {
            n1 = Integer.parseInt(hora.substring(0, 2));
            String pontos = hora.substring(2, 3);

            if (((n1 >= 0) && (n1 <= 23)) && pontos.equals(":")) {
                hora = hora + "00";
            } else {
                hora = "";
            }
        } else if (hora.length() == 4) {
            n1 = Integer.parseInt(hora.substring(0, 2));
            n2 = Integer.parseInt(hora.substring(3, 4));
            String pontos = hora.substring(2, 3);

            if ((pontos.equals(":")) && ((n1 >= 0) && (n1 <= 23)) && ((n2 >= 0) && (n2 <= 5))) {
                hora = hora + "0";
            } else {
                hora = "";
            }
        } else if (hora.length() == 5) {
            n1 = Integer.parseInt(hora.substring(0, 2));
            n2 = Integer.parseInt(hora.substring(3, 5));
            String pontos = hora.substring(2, 3);

            if (!(((n1 >= 0) && (n1 <= 23)) && ((n2 >= 0) && (n2 <= 59)) && (pontos.equals(":")))) {
                hora = "";
            }
        }
        return hora;
    }

    public void validaDuracao() {
        this.titulo.setDuracao(this.validaHora(this.titulo.getDuracao()));
    }

    public Titulo getTitulo() {
        return titulo;
    }

    public void setTitulo(Titulo titulo) {
        this.titulo = titulo;
    }

    public String getComoPesquisa() {
        return comoPesquisa;
    }

    public void setComoPesquisa(String comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    public String getDescPesquisa() {
        return descPesquisa;
    }

    public void setDescPesquisa(String descPesquisa) {
        this.descPesquisa = descPesquisa;
    }

    public void setListGeneroCombo(List<SelectItem> listGeneroCombo) {
        this.listGeneroCombo = listGeneroCombo;
    }

    public int getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(int idGenero) {
        this.idGenero = idGenero;
    }

    public void setListTitulo(List<Titulo> listTitulo) {
        this.listTitulo = listTitulo;
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public void upload(FileUploadEvent event) {
        String fotoTempCaminho = "locadora/titulo/" + getUsuario().getId();
        File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/temp/" + fotoTempCaminho + "/titulo.png"));
        if (f.exists()) {
            boolean delete = f.delete();
        } else {
            fotoTempPerfil = "";
        }
        ConfiguracaoUpload cu = new ConfiguracaoUpload();
        cu.setArquivo(event.getFile().getFileName());
        cu.setDiretorio("temp/locadora/titulo/" + getUsuario().getId());
        cu.setArquivo("titulo.png");
        cu.setSubstituir(true);
        cu.setRenomear("titulo.png");
        cu.setEvent(event);
        if (Upload.enviar(cu, true)) {
            fotoTempPerfil = "/Cliente/" + getCliente() + "/temp/" + fotoTempCaminho + "/titulo.png";
            fotoPerfil = "";
        } else {
            fotoTempPerfil = "";
            fotoPerfil = "";
        }
        PF.update("form_titulo:");

    }

    public void upload() {
        try {
            fileContent = new Scanner(file.getInputStream()).useDelimiter("\\A").next();
        } catch (IOException e) {
            // Error handling
        }
    }

    public void saveImage() {
        if (!Diretorio.criar("Imagens/locadora/titulo")) {
            return;
        }
        String arquivo = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/Imagens/locadora/titulo");
        boolean error = false;
        if (!fotoTempPerfil.equals("")) {
            File des = new File(arquivo + "/" + titulo.getId() + ".png");
            if (des.exists()) {
                des.delete();
            }
            File src = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(fotoTempPerfil));
            boolean rename = src.renameTo(des);
            fotoPerfil = "/Cliente/" + getCliente() + "/Imagens/locadora/titulo/" + titulo.getId() + ".png";
            fotoTempPerfil = "";

            if (!rename) {
                error = true;
            }
        }
        if (!error) {
            File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/temp/locadora/titulo/" + getUsuario().getId()));
            boolean delete = f.delete();
        }
        if (titulo.getFoto() == null) {
            Dao dao = new Dao();
            titulo.setFoto(new Date());
            dao.update(titulo, true);
        }
    }

    public void deleteImage() {
        boolean sucesso = false;
        File f;
        if (!fotoTempPerfil.equals("")) {
            f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/temp/locadora/titulo/" + getUsuario().getId() + "/titulo.png"));
            sucesso = f.delete();
        } else {
            if (titulo.getId() != -1) {
                f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/Imagens/locadora/titulo/" + titulo.getId() + ".png"));
                sucesso = f.delete();
            }
        }
        if (sucesso) {
            fotoTempPerfil = "";
            fotoPerfil = "";
            RequestContext.getCurrentInstance().update(":form_titulo");
            if (titulo.getFoto() == null) {
                Dao dao = new Dao();
                titulo.setFoto(null);
                dao.delete(titulo, true);
            }
        }
    }

    public Usuario getUsuario() {
        if (GenericaSessao.exists("sessaoUsuario")) {
            usuario = (Usuario) GenericaSessao.getObject("sessaoUsuario");
        }
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getFotoArquivo() {
        return fotoArquivo;
    }

    public void setFotoArquivo(String fotoArquivo) {
        this.fotoArquivo = fotoArquivo;
    }

    public String getFotoTempPerfil() {
        return fotoTempPerfil;
    }

    public void setFotoTempPerfil(String fotoTempPerfil) {
        this.fotoTempPerfil = fotoTempPerfil;
    }

    public String getFotoTempArquivo() {
        return fotoTempArquivo;
    }

    public void setFotoTempArquivo(String fotoTempArquivo) {
        this.fotoTempArquivo = fotoTempArquivo;
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public String getCliente() {
        if (GenericaSessao.exists("sessaoCliente")) {
            return GenericaSessao.getString("sessaoCliente");
        }
        return "";
    }

    public void showImagem() {
        showImagem("");
    }

    public String showImagem(String imageName) {
        String fotoMemoria = "";
        String caminhoTemp = "/Cliente/" + getCliente() + "/Imagens/locadora/titulo";
        String arquivo = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(caminhoTemp);
        for (String imagensTipo1 : imagensTipo) {
            File f;
            if (imageName.isEmpty()) {
                f = new File(arquivo + "/" + titulo.getId() + "." + imagensTipo1);
            } else {
                f = new File(arquivo + "/" + imageName + "." + imagensTipo1);
            }
            if (f.exists()) {
                if (imageName.isEmpty()) {
                    fotoPerfil = caminhoTemp + "/" + titulo.getId() + "." + imagensTipo1;
                } else {
                    fotoMemoria = caminhoTemp + "/" + imageName + "." + imagensTipo1;
                }
                fotoTempPerfil = "";
                break;
            } else {
                fotoPerfil = "";
                fotoTempPerfil = "";
            }
        }
        return fotoMemoria;
    }
}
