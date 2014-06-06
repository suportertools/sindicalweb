package br.com.rtools.associativo.beans;

import br.com.rtools.arrecadacao.GrupoCidades;
import br.com.rtools.associativo.*;
import br.com.rtools.associativo.db.*;
import br.com.rtools.endereco.Cidade;
import br.com.rtools.financeiro.FTipoDocumento;
import br.com.rtools.financeiro.ServicoPessoa;
import br.com.rtools.financeiro.db.*;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaEmpresa;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.TipoDocumento;
import br.com.rtools.pessoa.beans.FisicaBean;
import br.com.rtools.pessoa.db.*;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import static br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean.getCliente;
import br.com.rtools.sistema.ConfiguracaoUpload;
import br.com.rtools.utilitarios.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.Part;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;

@ManagedBean
@SessionScoped
public class SociosBean implements Serializable {
    private ServicoPessoa servicoPessoa;
    private ServicoCategoria servicoCategoria;
    private Socios socios;
    private MatriculaSocios matriculaSocios;
    private PessoaEmpresa pessoaEmpresa;
    private Fisica dependente;
    private boolean chkContaCobranca;
    private List<SelectItem> listaTipoDocumento;
    private List<SelectItem> listaServicos;
    //private List<SelectItem> listaParentesco;
    private List<DataObject> listaDependentes;
    private List<DataObject> listaDependentesInativos;
    private List<SelectItem> listaMotivoInativacao;
    private int idTipoDocumento;
    private int idServico;
    private int idGrupoCategoria;
    private int idCategoria;
    private int idIndexCombo;
    private int idInativacao;
    private boolean renderServicos;
    private boolean fotoTemp;
    private boolean temFoto;
    private boolean desabilitaImpressao;
    private boolean imprimirVerso;
    private String msgConfirma;
    private String lblSocio;
    private String lblSocioPergunta;
    private String tipoDestinario;
    private String dataInativacao;
    private String dataReativacao;
    private String statusSocio;
    
    List<SelectItem> listaGrupoCategoria = new ArrayList<SelectItem>();
    List<SelectItem> listaCategoria = new ArrayList<SelectItem>();
    private List<Fisica> listaFisica = new ArrayList<Fisica>();
    private Fisica fisicaPesquisa = new Fisica();
    private Pessoa pessoaPesquisa = new Pessoa();
    private List<SelectItem> listaSelectFisica = new ArrayList<SelectItem>();
    
    private Fisica novoDependente = new Fisica();
    private int index_dependente = 0;
    private String[] imagensTipo = new String[]{"jpg", "jpeg", "png", "gif"};
    private List<Socios> listaSocioInativo = new ArrayList<Socios>();
    
    private Part filePart;
    private String fotoTempPerfil = "";
    private Usuario usuario = new Usuario();
    
    private boolean modelVisible = false;
    
    public SociosBean() {
        servicoPessoa = new ServicoPessoa();
        servicoCategoria = new ServicoCategoria();
        socios = new Socios();
        matriculaSocios = new MatriculaSocios();
        pessoaEmpresa = new PessoaEmpresa();
        dependente = new Fisica();
        chkContaCobranca = false;
        listaTipoDocumento = new ArrayList();
        listaServicos = new ArrayList();
        //listaParentesco = new ArrayList();
        listaDependentes = new ArrayList();
        listaDependentesInativos = new ArrayList();
        idTipoDocumento = 0;
        idServico = 0;
        idGrupoCategoria = 0;
        idCategoria = 0;
        idIndexCombo = -1;
        idInativacao = 0;
        renderServicos = true;
        fotoTemp = false;
        temFoto = false;
        desabilitaImpressao = true;
        imprimirVerso = false;
        dataInativacao = DataHoje.data();
        dataReativacao = DataHoje.data();
        msgConfirma = "";
        lblSocio = "";
        lblSocioPergunta = "";
        tipoDestinario = "socio";
        statusSocio = "NOVO";
        listaMotivoInativacao = new ArrayList<SelectItem>();
    }
    
    public void upload(FileUploadEvent event) {
        String fotoTempCaminho = "foto/" + getUsuario().getId();
        File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/temp/" + fotoTempCaminho + "/perfil.png"));
        if (f.exists()) {
            boolean delete = f.delete();
        } else {
            fotoTempPerfil = "";
        }
        ConfiguracaoUpload cu = new ConfiguracaoUpload();
        cu.setArquivo(event.getFile().getFileName());
        cu.setDiretorio("temp/foto/" + getUsuario().getId());
        cu.setArquivo("perfil.png");
        cu.setSubstituir(true);
        cu.setRenomear("perfil.png");
        cu.setEvent(event);
        if (Upload.enviar(cu, true)) {
            fotoTempPerfil = "/Cliente/" + getCliente() + "/temp/" + fotoTempCaminho + "/perfil.png";
            //fotoPerfil = "";
        } else {
            fotoTempPerfil = "";
            //fotoPerfil = "";
        }
        RequestContext.getCurrentInstance().update(":formSocios:tab_view:i_panel_dados");

    }
    
    public String apagarImagem() {
        boolean sucesso = false;
        if (!fotoTempPerfil.equals("")) {
            File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/temp/foto/" + getUsuario().getId() + "/perfil.png"));
            sucesso = f.delete();
        } else {
            if (novoDependente.getId() != -1) {
                File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/Imagens/Fotos/" + novoDependente.getPessoa().getId() + ".png"));
                sucesso = f.delete();
            }
        }
        if (sucesso) {
            fotoTempPerfil = "";
            RequestContext.getCurrentInstance().update(":formSocios:tab_view:i_panel_dados");
        }
        
        return null;
    }
    
    public void salvarImagem() {
        if (!Diretorio.criar("Imagens/Fotos/")) {
            return;
        }
        String arquivo = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/Imagens/Fotos/");
        boolean error = false;
        if (!fotoTempPerfil.equals("")) {
            File des = new File(arquivo + "/" + novoDependente.getPessoa().getId() + ".png");
            if (des.exists()) {
                des.delete();
            }
            File src = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(fotoTempPerfil));
            boolean rename = src.renameTo(des);
            //fotoPerfil = "/Cliente/" + getCliente() + "/Imagens/Fotos/" + novoDependente.getPessoa().getId() + ".png";
            fotoTempPerfil = "";

            if (!rename) {
                error = true;
            }
        }
        if (!error) {
            File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/temp/foto/" + getUsuario().getId()));
            boolean delete = f.delete();
        }
    }
    
    public String getFotoPerfilDependente() {
        if (!fotoTempPerfil.isEmpty()){
            return fotoTempPerfil;
        }
        
        String caminhoTemp = "/Cliente/" + getCliente() + "/Imagens/Fotos/";
        String arquivo = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(caminhoTemp);
        for (String imagensTipo1 : imagensTipo) {
            File f = new File(arquivo + "/" + novoDependente.getPessoa().getId() + "." + imagensTipo1);
            if (f.exists()) {
                return caminhoTemp + "/" + novoDependente.getPessoa().getId() + "." + imagensTipo1;
            }
        }

        if (novoDependente.getSexo().equals("M"))
            return "/Imagens/user_male.png";
        else
            return "/Imagens/user_female.png";
    }
    
    public void capturar(CaptureEvent captureEvent) {
        String fotoTempCaminho = "foto/" + getUsuario().getId();
        if (PhotoCam.oncapture(captureEvent, "perfil", fotoTempCaminho, true)) {
            File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/temp/" + fotoTempCaminho + "/perfil.png"));
            if (f.exists()) {
                fotoTempPerfil = "/Cliente/" + getCliente() + "/temp/" + fotoTempCaminho + "/perfil.png";
            } else {
                fotoTempPerfil = "";
            }
        }
        RequestContext.getCurrentInstance().update(":formSocios:tab_view:i_panel_dados");
        RequestContext.getCurrentInstance().execute("dgl_captura.hide();");
    }
    
    public String getFotoTipTitular() {
        String caminhoTemp = "/Cliente/" + getCliente() + "/Imagens/Fotos/";
        String arquivo = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(caminhoTemp);
        for (String imagensTipo1 : imagensTipo) {
            File f = new File(arquivo + "/" + servicoPessoa.getPessoa().getId() + "." + imagensTipo1);
            if (f.exists()) {
                return caminhoTemp + "/" + servicoPessoa.getPessoa().getId() + "." + imagensTipo1;
            }
        }
        
        FisicaDB db = new FisicaDBToplink();
        Fisica fis = db.pesquisaFisicaPorPessoa(servicoPessoa.getPessoa().getId());
        if (fis.getSexo().equals("M"))
            return "/Imagens/user_male.png";
        else
            return "/Imagens/user_female.png";
    }    
    
    public void salvarData(){
        if (servicoPessoa.getId() != -1){
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            sv.abrirTransacao();
            
            if (!sv.alterarObjeto(servicoPessoa)){
                // ERRO
                sv.desfazerTransacao();
            }else{
                sv.comitarTransacao();
            }
        }
    }       
    
    public List<Fisica> listaPesquisaDependente(String query){
        List<Fisica> result = new ArrayList<Fisica>();
        FisicaDB db = new FisicaDBToplink();
        listaFisica = db.pesquisaFisicaPorNome(query.toUpperCase());
//        for(int i = 0; i < listaFisica.size(); i++) {
            //result.add(new SelectItem(i, listaFisica.get(i).getPessoa().getNome(), String.valueOf(listaFisica.get(i).getId())));
//        }
        return listaFisica;
    }
    
    public void selectDependente(){
        if (fisicaPesquisa == null){
            
        }else{
            novoDependente = fisicaPesquisa;
        }
    }
    
    
    public String inativarSocio() {
        if (socios.getId() != -1) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            SociosDB db = new SociosDBToplink();
            if (dataInativacao.length() < 10) {
                GenericaMensagem.warn("Erro", "Data de inativação inválida!");
                return null;
            }

            if (DataHoje.converteDataParaInteger(dataInativacao) > DataHoje.converteDataParaInteger(DataHoje.data())) {
                GenericaMensagem.warn("Erro", "Data de inativação não pode ser maior que dia de hoje!");
                return null;
            }

            sv.abrirTransacao();

            ServicoPessoa sp = (ServicoPessoa) sv.pesquisaCodigo(servicoPessoa.getId(), "ServicoPessoa");
            sp.setAtivo(false);
            List<Socios> listaDps = db.listaDependentes(matriculaSocios.getId());
            if (!sv.alterarObjeto(sp)) {
                GenericaMensagem.error("Erro", "Erro ao alterar Serviço Pessoa!");
                sv.desfazerTransacao();
                return null;
            }
            servicoPessoa = sp;
            for (int i = 0; i < listaDps.size(); i++) {
                ServicoPessoa sp2 = (ServicoPessoa) sv.pesquisaCodigo(listaDps.get(i).getServicoPessoa().getId(), "ServicoPessoa");
                sp2.setAtivo(false);
                if (!sv.alterarObjeto(sp2)) {
                    GenericaMensagem.error("Erro", "Erro ao alterar Serviço Pessoa!");
                    sv.desfazerTransacao();
                    return null;
                }
                sp2 = new ServicoPessoa();
            }

            matriculaSocios.setMotivoInativacao((SMotivoInativacao) sv.pesquisaCodigo(Integer.parseInt(listaMotivoInativacao.get(idInativacao).getDescription()), "SMotivoInativacao"));
            matriculaSocios.setInativo(dataInativacao);
            if (!sv.alterarObjeto(matriculaSocios)) {
                GenericaMensagem.error("Erro", "Erro ao alterar matrícula");
                sv.desfazerTransacao();
                return null;
            }
            
            GenericaMensagem.info("Concluído", "Sócio inativado com Sucesso!");
            sv.comitarTransacao();

            FisicaDB dbf = new FisicaDBToplink();
            GenericaSessao.put("fisicaBean", new FisicaBean());
            ((FisicaBean) GenericaSessao.getObject("fisicaBean")).editarFisicaParametro(dbf.pesquisaFisicaPorPessoa(socios.getServicoPessoa().getPessoa().getId()));
            ((FisicaBean) GenericaSessao.getObject("fisicaBean")).setSocios(socios);
            ((FisicaBean) GenericaSessao.getObject("fisicaBean")).showImagemFisica();
            
        } else {
            GenericaMensagem.warn("Erro", "Não existe sócio para ser inativado!");
        }
        return null;
    }

    public String reativarSocio() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        SociosDB db = new SociosDBToplink();

        if (db.pesquisaSocioPorPessoaAtivo(socios.getServicoPessoa().getPessoa().getId()).getId() != -1) {
            GenericaMensagem.warn("Erro", "Este sócio já esta cadastrado!");
            return null;
        }

        sv.abrirTransacao();

        ServicoPessoa sp = (ServicoPessoa) sv.find("ServicoPessoa", servicoPessoa.getId());
        if(sp == null) {
            GenericaMensagem.error("Erro", "Erro ao alterar Serviço Pessoa");
            sv.desfazerTransacao();
            return null;
        }
        sp.setAtivo(true);
        if (!sv.alterarObjeto(sp)) {
            GenericaMensagem.error("Erro", "Erro ao alterar Serviço Pessoa");
            sv.desfazerTransacao();
            return null;
        }
        
        servicoPessoa = sp;
        List<Socios> listaDps = db.listaDependentesInativos(matriculaSocios.getId());
        for (int i = 0; i < listaDps.size(); i++) {
            ServicoPessoa sp2 = (ServicoPessoa) sv.pesquisaCodigo(listaDps.get(i).getServicoPessoa().getId(), "ServicoPessoa");
            sp2.setAtivo(true);
            if (!sv.alterarObjeto(sp2)) {
                GenericaMensagem.error("Erro", "Erro ao alterar Serviço Pessoa do Dependente");
                sv.desfazerTransacao();
                return null;
            }
            sp2 = new ServicoPessoa();
        }
        matriculaSocios.setMotivoInativacao(null);
        matriculaSocios.setDtInativo(null);
        if (!sv.alterarObjeto(matriculaSocios)) {
            GenericaMensagem.error("Erro", "Erro ao ativar matrícula");
            sv.desfazerTransacao();
            return null;
        }
        GenericaMensagem.info("Concluído", "Sócio ativado com Sucesso!");
        dataInativacao = DataHoje.data();
        
        sv.comitarTransacao();
        
        FisicaDB dbf = new FisicaDBToplink();
        GenericaSessao.put("fisicaBean", new FisicaBean());
        ((FisicaBean) GenericaSessao.getObject("fisicaBean")).editarFisicaParametro(dbf.pesquisaFisicaPorPessoa(socios.getServicoPessoa().getPessoa().getId()));
        ((FisicaBean) GenericaSessao.getObject("fisicaBean")).setSocios(socios);
        ((FisicaBean) GenericaSessao.getObject("fisicaBean")).showImagemFisica();
        return null;
    }

    public String salvar() {
        if (!validaSalvar()) {
            return null;
        }
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        MatriculaSocios msMemoria = new MatriculaSocios();
        sv.abrirTransacao();
        try {
            servicoPessoa.setTipoDocumento((FTipoDocumento) sv.pesquisaCodigo(Integer.parseInt(getListaTipoDocumento().get(idTipoDocumento).getDescription()), "FTipoDocumento"));
        } catch (NumberFormatException e) {
            servicoPessoa.setTipoDocumento((FTipoDocumento) sv.pesquisaCodigo(Integer.parseInt(getListaTipoDocumento().get(0).getDescription()), "FTipoDocumento"));
        }
        // NOVO REGISTRO -----------------------
        if (servicoPessoa.getId() == -1) {
            servicoPessoa.setAtivo(true);
            servicoPessoa.setCobranca(servicoPessoa.getPessoa());
            if (!sv.inserirObjeto(servicoPessoa)) {
                GenericaMensagem.error("Erro", "Erro ao salvar Serviço Pessoa!");
                return null;
            }
            matriculaSocios.setMotivoInativacao(null);
        } else {
            // ATUALIZA REGISTRO -------------------
            if (!sv.alterarObjeto(servicoPessoa)) {
                GenericaMensagem.error("Erro", "Erro ao alterar Serviço Pessoa!");
                return null;
            }
        }
        GrupoCategoria grupoCategoria = (GrupoCategoria) sv.pesquisaCodigo(Integer.parseInt(getListaGrupoCategoria().get(idGrupoCategoria).getDescription()), "GrupoCategoria");
//        if(matriculaSocios.getNrMatricula() <= grupoCategoria.getNrProximaMatricula()) {
//            msgConfirma = "Número de matrícula deve ser menor ou igual!";
//            return null;
//        }
        MatriculaSociosDB dbMat = new MatriculaSociosDBToplink();
        matriculaSocios.setCategoria(servicoCategoria.getCategoria());
        matriculaSocios.setTitular(servicoPessoa.getPessoa());
        if (matriculaSocios.getNrMatricula() <= 0) {
            // MATRICULA MENOR QUE ZERO 
            matriculaSocios.setNrMatricula(grupoCategoria.getNrProximaMatricula());
            grupoCategoria.setNrProximaMatricula(matriculaSocios.getNrMatricula() + 1);

        } else if (matriculaSocios.getNrMatricula() > grupoCategoria.getNrProximaMatricula()) {
            // MATRICULA MAIOR QUE A PROXIMA DA CATEGORIA 
            matriculaSocios.setNrMatricula(grupoCategoria.getNrProximaMatricula());
            grupoCategoria.setNrProximaMatricula(matriculaSocios.getNrMatricula() + 1);

        } else if (matriculaSocios.getNrMatricula() < grupoCategoria.getNrProximaMatricula()
                && // MATRICULA MENOR QUE A PROXIMA DA CATEGORIA E NÃO EXISTIR UMA IGUAL 
                dbMat.pesquisaPorNrMatricula(matriculaSocios.getNrMatricula(), servicoCategoria.getCategoria().getId()) != null) {
            matriculaSocios.setNrMatricula(grupoCategoria.getNrProximaMatricula());
            grupoCategoria.setNrProximaMatricula(matriculaSocios.getNrMatricula() + 1);

        } else if (matriculaSocios.getNrMatricula() < grupoCategoria.getNrProximaMatricula()
                && dbMat.pesquisaPorNrMatricula(matriculaSocios.getNrMatricula(), servicoCategoria.getCategoria().getId()) == null) {
            // MATRICULA MENOR QUE A PROXIMA DA CATEGORIA E NÃO EXISTIR
            //////////////////////////////////// NAO FAZ NADA
        }
        if(matriculaSocios.getId() != -1) {
            msMemoria = (MatriculaSocios) sv.pesquisaObjeto(matriculaSocios.getId(), "MatriculaSocios");
        }
        if(msMemoria.getNrMatricula() != matriculaSocios.getNrMatricula() || matriculaSocios.getNrMatricula() == 0) {
            List list = dbMat.listaMatriculaPorGrupoNrMatricula(matriculaSocios.getCategoria().getGrupoCategoria().getId(), matriculaSocios.getNrMatricula());
            if (!list.isEmpty()) {
                GenericaMensagem.error("Erro", "Matrícula já existe!");
                sv.desfazerTransacao();
                return null;
            }
        }
        if (matriculaSocios.getId() == -1) {
            if (!sv.inserirObjeto(matriculaSocios)) {
                GenericaMensagem.error("Erro", "Erro ao Salvar Matrícula!");
                sv.desfazerTransacao();
                return null;
            }
        } else {
            if (!sv.alterarObjeto(matriculaSocios)) {
                GenericaMensagem.error("Erro", "Erro ao Atualizar Matrícula!");
                sv.desfazerTransacao();
                return null;
            }
        }
        sv.alterarObjeto(grupoCategoria);

        socios.setMatriculaSocios(matriculaSocios);
        socios.setParentesco((Parentesco) sv.pesquisaCodigo(1, "Parentesco"));
        socios.setServicoPessoa(servicoPessoa);
        socios.setNrViaCarteirinha(1);
        
        
        DataHoje dh = new DataHoje();
        SociosDB db = new SociosDBToplink();
        SocioCarteirinhaDB dbc = new SocioCarteirinhaDBToplink();
        ModeloCarteirinha modeloc = dbc.pesquisaModeloCarteirinha(Integer.valueOf(listaCategoria.get(idCategoria).getDescription()), -1);
        
        if (modeloc == null){
            sv.desfazerTransacao();
            GenericaMensagem.warn("Erro", "Modelo para categoria não encontrado!");
            return null;
        }
        
        List<SocioCarteirinha> list_carteirinha_socio = db.pesquisaCarteirinhasPorPessoa(socios.getServicoPessoa().getPessoa().getId(), modeloc.getId());
        // VERIFICA SE SÓCIO TEM CARTEIRINHA -- SE TIVER NÃO ADICIONAR --
        if (list_carteirinha_socio.isEmpty()){
            if (modeloc == null){
                GenericaMensagem.error("Erro", "Não existe modelo de carteirinha para esta categoria " +listaCategoria.get(idCategoria).getLabel()+" do sócio!");
                sv.desfazerTransacao();
                return null;
            }
            //Date validadeCarteirinha = DataHoje.converte(dh.incrementarMeses(grupoCategoria.getNrValidadeMesCartao(), DataHoje.data()));
            String validadeCarteirinha = dh.incrementarMeses(grupoCategoria.getNrValidadeMesCartao(), DataHoje.data());

            SocioCarteirinha sc = new SocioCarteirinha(-1, "", servicoPessoa.getPessoa(), modeloc, servicoPessoa.getPessoa().getId(), 1, validadeCarteirinha);
            if (!sv.inserirObjeto(sc)){
                GenericaMensagem.error("Erro", "Não foi possivel salvar Socio Carteirinha!");
                sv.desfazerTransacao();
                return null;
            }
        }
        
        if (socios.getId() == -1) {
            if (!sv.inserirObjeto(socios)) {
                GenericaMensagem.error("Erro", "Erro ao Salvar Sócio!");
                sv.desfazerTransacao();
                return null;
            }
            GenericaMensagem.info("Sucesso", "Pessoa Associada!");
        } else {
            if (!sv.alterarObjeto(socios)) {
                GenericaMensagem.error("Erro", "Erro ao Atualizar Sócio!");
                sv.desfazerTransacao();
                return null;
            }
            GenericaMensagem.info("Sucesso", "Cadastro Atualizado!");
        }
        
        /* 
        // SE TIVER UMA LISTA COM DEPENDENTES
        *
        *
        */
        if (!listaDependentes.isEmpty()) {
            
            ServicoCategoriaDB dbSCat = new ServicoCategoriaDBToplink();
            ParentescoDB dbPar = new ParentescoDBToplink();
            for (int i = 0; i < listaDependentes.size(); i++) {
                if (((Fisica) ((DataObject) listaDependentes.get(i)).getArgumento0()).getId() != -1) {
                    Socios socioDependente = db.pesquisaSocioPorPessoaAtivo(((Fisica) ((DataObject) listaDependentes.get(i)).getArgumento0()).getPessoa().getId());
                    Fisica fisicaDependente = ((Fisica) listaDependentes.get(i).getArgumento0());
                    
                    List<SelectItem> lista_si = (ArrayList<SelectItem>) listaDependentes.get(i).getArgumento6();
                    Parentesco parentesco = dbPar.pesquisaCodigo( Integer.valueOf(lista_si.get( Integer.valueOf(listaDependentes.get(i).getArgumento1().toString()) ).getDescription()) );
                    
                    ServicoCategoria servicoCategoriaDep = dbSCat.pesquisaPorParECat(parentesco.getId(), servicoCategoria.getCategoria().getId());
                    
                    String ref_dependente = (listaDependentes.get(i).getArgumento4() == null ) ? "" : listaDependentes.get(i).getArgumento4().toString();
                    
                    if (servicoCategoriaDep == null) {
                        GenericaMensagem.warn("Erro", "Erro para Serviço Categoria: " + ((Fisica) ((DataObject) listaDependentes.get(i)).getArgumento0()).getPessoa().getNome());
                        sv.desfazerTransacao();
                        return null;
                    }
                    
                    modeloc = dbc.pesquisaModeloCarteirinha(matriculaSocios.getCategoria().getId(), -1);
                    
                    List<SocioCarteirinha> list_carteirinha_dep = db.pesquisaCarteirinhasPorPessoa(socioDependente.getServicoPessoa().getPessoa().getId(), modeloc.getId());
                    // VERIFICA SE SÓCIO DEPENDENTE TEM CARTEIRINHA -- SE TIVER NÃO ADICIONAR --
                    Registro registro = (Registro)sv.pesquisaCodigo(1, "Registro");
                    
                    if (list_carteirinha_dep.isEmpty() && registro.isCarteirinhaDependente()){
                        
                        if (modeloc == null){
                            GenericaMensagem.error("Erro", "Não existe modelo de carteirinha para categoria "+servicoCategoriaDep.getCategoria().getCategoria()+" do dependente "+ socioDependente.getServicoPessoa().getPessoa().getNome());
                            sv.desfazerTransacao();
                            return null;
                        }

                        String validadeCarteirinha = dh.incrementarMeses(grupoCategoria.getNrValidadeMesCartao(), DataHoje.data());

                        SocioCarteirinha sc = new SocioCarteirinha(-1, "", fisicaDependente.getPessoa(), modeloc, fisicaDependente.getPessoa().getId(), 1, validadeCarteirinha);

                        if (!sv.inserirObjeto(sc)){
                            GenericaMensagem.error("Erro", "Não foi possivel salvar Socio Carteirinha Dependente!");
                            sv.desfazerTransacao();
                            return null;
                        }
                    }
                    
                    if (socioDependente.getId() == -1) {
                        ServicoPessoa servicoPessoaDependente = new ServicoPessoa(-1,
                                servicoPessoa.getEmissao(),
                                fisicaDependente.getPessoa(),
                                servicoPessoa.isDescontoFolha(),
                                servicoCategoriaDep.getServicos(),
                                Moeda.substituiVirgulaFloat(listaDependentes.get(i).getArgumento5().toString()),
                                servicoPessoa.getReferenciaVigoracao(),
                                ref_dependente,
                                servicoPessoa.getNrDiaVencimento(),
                                servicoPessoa.getTipoDocumento(),
                                servicoPessoa.getCobranca(),
                                servicoPessoa.isAtivo(),
                                servicoPessoa.isBanco());
                        if (!sv.inserirObjeto(servicoPessoaDependente)) {
                            GenericaMensagem.warn("Erro", "Erro ao salvar Serviço Pessoa: " + ((Fisica) ((DataObject) listaDependentes.get(i)).getArgumento0()).getPessoa().getNome());
                            sv.desfazerTransacao();
                            return null;
                        }
                        socioDependente = new Socios(-1,
                                matriculaSocios,
                                servicoPessoaDependente,
                                parentesco,
                                Integer.parseInt((String) ((DataObject) listaDependentes.get(i)).getArgumento2()),
                                DataHoje.converte((String) ((DataObject) listaDependentes.get(i)).getArgumento3()));

                        if (!sv.inserirObjeto(socioDependente)) {
                            GenericaMensagem.warn("Erro", "Erro ao salvar Sócio: " + ((Fisica) ((DataObject) listaDependentes.get(i)).getArgumento0()).getPessoa().getNome());
                            sv.desfazerTransacao();
                            return null;
                        }
                    } else {
                        ServicoPessoaDB dbSerP = new ServicoPessoaDBToplink();
                        //ServicoPessoa servicoPessoaDependente = dbSerP.pesquisaServicoPessoaPorPessoa(((Fisica) ((DataObject) listaDependentes.get(i)).getArgumento0()).getPessoa().getId());
                        ServicoPessoa servicoPessoaDependente = (ServicoPessoa)sv.pesquisaCodigo(socioDependente.getServicoPessoa().getId(), "ServicoPessoa");

                        servicoPessoaDependente.setEmissao(servicoPessoa.getEmissao());
                        servicoPessoaDependente.setPessoa(fisicaDependente.getPessoa());
                        servicoPessoaDependente.setDescontoFolha(servicoPessoa.isDescontoFolha());
                        servicoPessoaDependente.setServicos(servicoCategoriaDep.getServicos());
                        servicoPessoaDependente.setNrDesconto(Moeda.substituiVirgulaFloat(listaDependentes.get(i).getArgumento5().toString()));
                        servicoPessoaDependente.setReferenciaVigoracao(servicoPessoa.getReferenciaVigoracao());
                        servicoPessoaDependente.setReferenciaValidade(ref_dependente);
                        servicoPessoaDependente.setNrDiaVencimento(servicoPessoa.getNrDiaVencimento());
                        servicoPessoaDependente.setTipoDocumento(servicoPessoa.getTipoDocumento());
                        servicoPessoaDependente.setCobranca(servicoPessoa.getCobranca());
                        servicoPessoaDependente.setAtivo(servicoPessoa.isAtivo());
                        servicoPessoaDependente.setBanco(servicoPessoa.isBanco());
                        if (!sv.alterarObjeto(servicoPessoaDependente)) {
                            GenericaMensagem.error("Erro", "Erro ao Alterar Serviço Pessoa: " + ((Fisica) ((DataObject) listaDependentes.get(i)).getArgumento0()).getPessoa().getNome());
                            sv.desfazerTransacao();
                            return null;
                        }
                        socioDependente.setValidadeCarteirinha((String) ((DataObject) listaDependentes.get(i)).getArgumento3());
                        socioDependente.setServicoPessoa(servicoPessoaDependente);
                        socioDependente.setMatriculaSocios(matriculaSocios);
                        socioDependente.setNrViaCarteirinha(Integer.parseInt((String) ((DataObject) listaDependentes.get(i)).getArgumento2()));
                        socioDependente.setParentesco(parentesco);
                        if (!sv.alterarObjeto(socioDependente)) {
                            GenericaMensagem.error("Erro", "Erro ao Salvar Sócio: " + ((Fisica) ((DataObject) listaDependentes.get(i)).getArgumento0()).getPessoa().getNome());
                            sv.desfazerTransacao();
                            return null;
                        }
                    }
                }
            }
        }
        
        ((FisicaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaBean")).setSocios(socios);
        sv.comitarTransacao();
        
        atualizarListaDependenteAtivo();
        atualizarListaDependenteInativo();
        return null;
    }

    public String editarTitular() {
        if (socios.getId() == -1){
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
            return "pessoaFisica";
        }
        FisicaDB db = new FisicaDBToplink();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fisicaBean", new FisicaBean());
        ((FisicaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaBean")).editarFisicaParametro(db.pesquisaFisicaPorPessoa(socios.getServicoPessoa().getPessoa().getId()));
        ((FisicaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaBean")).setSocios(socios);
        ((FisicaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaBean")).showImagemFisica();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        return "pessoaFisica";
    }

    public boolean validaSalvar() {
        if (matriculaSocios.getEmissao().isEmpty()) {
            GenericaMensagem.warn("Erro", "Data de Emissáo Inválida!");
            return false;
        }

        if (getListaGrupoCategoria().isEmpty()) {
            GenericaMensagem.warn("Erro", "Lista de Grupos Categoria Vazia!");
            return false;
        }

        if (getListaCategoria().isEmpty()) {
            GenericaMensagem.warn("Erro", "Lista de Categoria Vazia!");
            return false;
        }

        if (getListaTipoDocumento().isEmpty()) {
            GenericaMensagem.warn("Erro", "Lista de Tipo Documentos Vazia!");
            return false;
        }

        for (DataObject linha : listaDependentes){
            if ( ((Fisica)linha.getArgumento0()).getId() == -1){
                GenericaMensagem.warn("Erro", "Pesquise um Dependente para Salvar!");
                return false;
            }
            
//            List<SelectItem> lista_si = new ArrayList<SelectItem>();
//            
//            lista_si.addAll( (Collection<? extends SelectItem>) linha.getArgumento6());
//            
//            if ( Integer.valueOf( linha.getArgumento1().toString()  )  == 0){
//                GenericaMensagem.warn("Erro", "Dependente não pode ser salvo sem Parentesco!");
//                return false;
//            }
        }
        
        
        ServicoCategoriaDB dbSCat = new ServicoCategoriaDBToplink();
        int idCat = Integer.parseInt(getListaCategoria().get(idCategoria).getDescription());
        servicoCategoria = dbSCat.pesquisaPorParECat(1, idCat);
        if (servicoCategoria == null) {
            GenericaMensagem.warn("Erro", "Não existe Serviço Categoria cadastrado!");
            return false;
        } else {
            servicoPessoa.setServicos(servicoCategoria.getServicos());
        }

        if (servicoPessoa.isDescontoFolha()) {
            if (pessoaEmpresa.getId() == -1) {
                GenericaMensagem.error("Erro", "Este sócio não possui Empresa para desconto em folha!");
                servicoPessoa.setDescontoFolha(false);
                return false;
            }
            servicoPessoa.setCobranca(pessoaEmpresa.getJuridica().getPessoa());
        } else {
            servicoPessoa.setCobranca(servicoPessoa.getPessoa());
        }

        SociosDB db = new SociosDBToplink();
        if ((servicoPessoa.getId() == -1) && (db.pesquisaSocioPorPessoaAtivo(servicoPessoa.getPessoa().getId()).getId() != -1)) {
            GenericaMensagem.error("Erro", "Esta pessoa já um Sócio Cadastrado!");
            return false;
        }

        ServicoContaCobrancaDB dbSCB = new ServicoContaCobrancaDBToplink();
        if (chkContaCobranca) {
            List l = dbSCB.pesquisaServPorIdServIdTipoServ(servicoCategoria.getServicos().getId(), 1);
            if (!l.isEmpty()) {
                servicoPessoa.setBanco(true);
            } else {
                servicoPessoa.setBanco(false);
                GenericaMensagem.error("Erro", "Não existe Serviço Conta Cobrança!");
                return false;
            }
        } else {
            servicoPessoa.setBanco(false);
        }
        return true;
    }

    public void salvarFisicaDependente() {
        if (!validaSalvarDependente()) {
            return;
        }
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        if (temFoto) {
            novoDependente.setDataFoto(DataHoje.data());
        } else {
            novoDependente.setDtFoto(null);
        }

        if (novoDependente.getId() == -1) {
            novoDependente.getPessoa().setTipoDocumento((TipoDocumento) sv.pesquisaCodigo(1, "TipoDocumento"));
            sv.abrirTransacao();
            if (!sv.inserirObjeto(novoDependente.getPessoa())) {
                GenericaMensagem.error("Erro", "Erro ao salvar Pessoa!");
                sv.desfazerTransacao();
                return;
            }

            if (!sv.inserirObjeto(novoDependente)) {
                GenericaMensagem.error("Erro", "Erro ao salvar Cadastro!");
                sv.desfazerTransacao();
                return;
            }
            sv.comitarTransacao();
            GenericaMensagem.info("Sucesso", "Dependente Salvo!");
        } else {
            sv.abrirTransacao();
            if (!sv.alterarObjeto(novoDependente.getPessoa())) {
                GenericaMensagem.error("Erro", "Erro ao atualizar Pessoa!");
                sv.desfazerTransacao();
                return;
            }

            if (!sv.alterarObjeto(novoDependente)) {
                GenericaMensagem.error("Erro", "Erro ao atualizar Cadastro!");
                sv.desfazerTransacao();
                return;
            }
            sv.comitarTransacao();
            GenericaMensagem.info("Sucesso", "Dependente Atualizado!");
        }
        
        ((DataObject) listaDependentes.get(index_dependente)).setArgumento0(novoDependente);
        atualizaValidadeTela(index_dependente);
        salvarImagem();
        modelVisible = false;
        index_dependente = 0;
        novoDependente = new Fisica();
        RequestContext.getCurrentInstance().execute("dlg_dependente.hide()");
    }

    public boolean validaSalvarDependente() {
        FisicaDB db = new FisicaDBToplink();
        if (novoDependente.getId() == -1) {
            if (!db.pesquisaFisicaPorNomeNascRG(novoDependente.getPessoa().getNome(),
                    novoDependente.getDtNascimento(),
                    novoDependente.getRg()).isEmpty()) {
                GenericaMensagem.error("Erro", "Esta pessoa já esta Cadastrada!");
                return false;
            }

            if (novoDependente.getPessoa().getDocumento().equals("") || novoDependente.getPessoa().getDocumento().equals("0")) {
                novoDependente.getPessoa().setDocumento("0");
            } else {
                List listDocumento = db.pesquisaFisicaPorDoc(novoDependente.getPessoa().getDocumento());
                if (!listDocumento.isEmpty()) {
                    GenericaMensagem.error("Erro", "Documento já existente!");
                    return false;
                }
            }
        } else {
            if (novoDependente.getPessoa().getDocumento().equals("") || novoDependente.getPessoa().getDocumento().equals("0")) {
                novoDependente.getPessoa().setDocumento("0");
            } else {

                List listDocumento = db.pesquisaFisicaPorDoc(novoDependente.getPessoa().getDocumento());
                for (Object listDocumento1 : listDocumento) {
                    if (!listDocumento.isEmpty() && ((Fisica) listDocumento1).getId() != novoDependente.getId()) {
                        GenericaMensagem.error("Erro", "Documento já existente!");
                        return false;
                    }
                }
            }
        }
        
        if (novoDependente.getNascimento().isEmpty() || novoDependente.getNascimento().length() < 10) {
            GenericaMensagem.fatal("Erro", "Data de Nascimento inválida!");
            return false;
        }
        
        if (novoDependente.getPessoa().getNome().equals("")) {
            GenericaMensagem.error("Erro", "O campo nome não pode ser nulo!");
            return false;
        }
        
        if (!novoDependente.getPessoa().getDocumento().isEmpty() && !novoDependente.getPessoa().getDocumento().equals("0")) {
            if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(novoDependente.getPessoa().getDocumento()))) {
                GenericaMensagem.error("Erro", "Documento Inválido!");
                return false;
            }
        }
        
        for (DataObject linha : listaDependentesInativos){
            if ( ((Fisica)linha.getArgumento0()).getId() == novoDependente.getId() ){
                GenericaMensagem.warn("Erro", "Este dependente esta inativado nesta matrícula!");
                return false;
            }
        }
        
    
        SociosDB dbs = new SociosDBToplink();
        Socios soc_dep = dbs.pesquisaSocioPorPessoaAtivo(novoDependente.getPessoa().getId());
        if (soc_dep.getId() != -1 && (soc_dep.getMatriculaSocios().getId() != socios.getMatriculaSocios().getId())) {
            GenericaMensagem.error("Erro", "Esta pessoa já um Dependente Cadastrado!");
            return false;
        }
        
        return true;
    }
    
    public void pesquisaCPF(){
        if (!novoDependente.getPessoa().getDocumento().isEmpty()){
            FisicaDB db = new FisicaDBToplink();
            List<Fisica> listDocumento = db.pesquisaFisicaPorDoc(novoDependente.getPessoa().getDocumento());
            if (!listDocumento.isEmpty()){
                novoDependente = listDocumento.get(0);
            }else if (novoDependente.getId() != -1){
                String doc = novoDependente.getPessoa().getDocumento();

                novoDependente = new Fisica();

                novoDependente.getPessoa().setDocumento(doc);
            }
        }
    }

    public void adicionarDependente() {
        
        
//        if (getListaParentesco().size() == 1 && Integer.valueOf(listaParentesco.get(0).getDescription()) == 0){
//            GenericaMensagem.warn("Erro", "Nenhum Serviço adicionado para Dependentes!");
//            RequestContext.getCurrentInstance().execute("i_dlg_c.show()");
//            return;
//        }
        
        if (Integer.valueOf(listaCategoria.get(idCategoria).getDescription()) == 0){
            GenericaMensagem.warn("Erro", "Nenhuma Categoria Encontrada!");
            RequestContext.getCurrentInstance().execute("i_dlg_c.show()");
            return;
        }
        
        Fisica fisica = new Fisica();
        DataHoje dh = new DataHoje();
        
        CategoriaDB dbCat = new CategoriaDBToplink();
        GrupoCategoria gpCat = dbCat.pesquisaGrupoPorCategoria(Integer.valueOf(listaCategoria.get(idCategoria).getDescription()));
        
        Date validadeCarteirinha = DataHoje.converte(dh.incrementarMeses(gpCat.getNrValidadeMesCartao(), DataHoje.data()));
        
        if (listaDependentes.isEmpty()) {
            fisica.getPessoa().setNome("");
            DataObject dtObj = new DataObject(
                    fisica, // NOME
                    0, // IDPARENTESCO
                    1, // VIA CARTEIRINHA
                    DataHoje.converteData(validadeCarteirinha), // DATA VALIDADE CARTEIRINHA
                    null, // DATA VAL DEP
                    0.0, // DESCONTO
                    new SelectItem(0, "Selecione um Dependente", "0"), // LISTA DE PARENTESCO
                    null,
                    null,
                    null
            );
            listaDependentes.add(dtObj);
        } else {
            for (int i = 0; i < listaDependentes.size(); i++) {
                if (((Fisica) ((DataObject) listaDependentes.get(i)).getArgumento0()).getId() != -1
                        && (i - (listaDependentes.size() - 1) == 0)) {
                    fisica.getPessoa().setNome("");
                    DataObject dtObj = new DataObject(
                            fisica, // NOME
                            0, // IDPARENTESCO
                            1, // VIA CARTEIRINHA
                            DataHoje.converteData(validadeCarteirinha), // DATA VALIDADE CARTEIRINHA
                            null, // DATA VAL DEP
                            0.0, // DESCONTO
                            new SelectItem(0, "Selecione um Dependente", "0"), // LISTA DE PARENTESCO
                            null,
                            null,
                            null
                    );
                    listaDependentes.add(dtObj);
                    break;
                }
            }
        }
        dependente = new Fisica();
    }

    public String novoCadastroDependente() {
        novoDependente = new Fisica();
        fisicaPesquisa = new Fisica();
        temFoto = false;
        return null;
    }

    public void novoVoid() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sociosBean", new SociosBean());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fisicaBean", new FisicaBean());
    }

    public String excluir() {
        if (!validaExcluir()) {
            RequestContext.getCurrentInstance().execute("i_dlg_c.show()");
            return null;
        }
        
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        SociosDB db = new SociosDBToplink();

        // DELETAR DEPENDENTES -----
        sv.abrirTransacao();
        for (int i = 0; i < listaDependentes.size(); i++) {
            Socios soc = db.pesquisaSocioPorPessoa(((Fisica) ((DataObject) listaDependentes.get(i)).getArgumento0()).getPessoa().getId());
            if (soc.getId() == -1) {
                //listaDependentes.remove(idIndexDep);
            } else if (!excluirDependentes(sv, (Socios) sv.pesquisaCodigo(soc.getId(), "Socios"))) {
                sv.desfazerTransacao();
                GenericaMensagem.error("Erro", "Erro ao Excluir Dependentes!");
                RequestContext.getCurrentInstance().execute("i_dlg_c.show()");
                return null;
            }
        }

        SocioCarteirinhaDB dbc = new SocioCarteirinhaDBToplink();
        ModeloCarteirinha modeloc = dbc.pesquisaModeloCarteirinha(socios.getMatriculaSocios().getCategoria().getId(), -1);
        
        List<SocioCarteirinha> list = db.pesquisaCarteirinhasPorPessoa(socios.getServicoPessoa().getPessoa().getId(), modeloc.getId());

        if (!list.isEmpty()) {
            for (SocioCarteirinha socioCarteirinha : list) {
                if (!sv.deletarObjeto(sv.pesquisaCodigo(socioCarteirinha.getId(), "SocioCarteirinha"))) {
                    GenericaMensagem.error("Erro", "Erro ao Excluir carteirinha do Dependente!");
                    RequestContext.getCurrentInstance().execute("i_dlg_c.show()");
                    return null;
                }
            }
        }

        // DELETAR SOCIOS ------
        if (!sv.deletarObjeto(sv.pesquisaCodigo(socios.getId(), "Socios"))) {
            GenericaMensagem.error("Erro", "Erro ao Deletar Sócio!");
            RequestContext.getCurrentInstance().execute("i_dlg_c.show()");
            sv.desfazerTransacao();
            return null;
        }

        if (!sv.deletarObjeto(sv.pesquisaCodigo(matriculaSocios.getId(), "MatriculaSocios"))) {
            GenericaMensagem.error("Erro", "Erro ao Deletar Matricula!");
            RequestContext.getCurrentInstance().execute("i_dlg_c.show()");
            sv.desfazerTransacao();
            return null;
        }

        if (!sv.deletarObjeto(sv.pesquisaCodigo(servicoPessoa.getId(), "ServicoPessoa"))) {
            GenericaMensagem.error("Erro", "Erro ao Deletar Servico Pessoa!");
            RequestContext.getCurrentInstance().execute("i_dlg_c.show()");
            sv.desfazerTransacao();
            return null;
        }
        
        GenericaMensagem.info("Sucesso", "Cadastro Deletado!");
        sv.comitarTransacao();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);

        // FUNCIONANDO --
        //FisicaJSFBean fizx = (FisicaJSFBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fisicaBean", new FisicaJSFBean());
        //fizx.setSocios(new Socios());
        ((FisicaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaBean")).setSocios(new Socios());
        //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("socioPesquisa",socios);
        return "pessoaFisica";
    }

    public boolean validaExcluir() {
        if (servicoPessoa.getId() == -1) {
            GenericaMensagem.error("Erro", "Não existe Sócio para ser Excluído!");
            return false;
        }
        return true;
    }

    public String excluirDependente(DataObject linha, int index) {
        Fisica fi = (Fisica) linha.getArgumento0();
        if (fi.getId() != -1) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            SociosDB db = new SociosDBToplink();
            Socios soc = db.pesquisaSocioPorPessoa(fi.getPessoa().getId());
            
            sv.abrirTransacao();
            if (soc.getId() == -1) {
                listaDependentes.remove(index);
                GenericaMensagem.warn("Erro", "Dependente Excluído!");
                sv.desfazerTransacao();
            } else if (!excluirDependentes(sv, soc)) {
                sv.desfazerTransacao();
                GenericaMensagem.warn("Erro", "Erro ao excluir Dependente!");
            } else {
                listaDependentes.remove(index);
                GenericaMensagem.warn("Erro", "Dependente Excluído!");
                sv.comitarTransacao();
            }
        } else {
            listaDependentes.remove(index);
            GenericaMensagem.warn("Erro", "Dependente Excluído!");
        }
        return null;
    }

    public boolean excluirDependentes(SalvarAcumuladoDB sv, Socios soc) {
        SociosDB db = new SociosDBToplink();
        ServicoPessoaDB dbS = new ServicoPessoaDBToplink();

        SocioCarteirinhaDB dbc = new SocioCarteirinhaDBToplink();
        
        ServicoCategoriaDB dbSCat = new ServicoCategoriaDBToplink();
        
        ServicoCategoria servicoCategoriaDep = dbSCat.pesquisaPorParECat(soc.getParentesco().getId(), servicoCategoria.getCategoria().getId());
        
        ModeloCarteirinha modeloc = dbc.pesquisaModeloCarteirinha(servicoCategoriaDep.getCategoria().getId(), -1);
        
        List<SocioCarteirinha> list = db.pesquisaCarteirinhasPorPessoa(soc.getServicoPessoa().getPessoa().getId(), modeloc.getId());

        if (!list.isEmpty()) {
            for (SocioCarteirinha socioCarteirinha : list) {
                if (!sv.deletarObjeto(sv.pesquisaCodigo(socioCarteirinha.getId(), "SocioCarteirinha"))) {
                    msgConfirma = "Erro ao excluir carteirinha do Dependente!";
                    return false;
                }
            }
        }
        

        if (!sv.deletarObjeto(sv.pesquisaCodigo(soc.getId(), "Socios"))) {
            msgConfirma = "Erro ao excluir Dependente!";
            return false;
        }

        ServicoPessoa serPessoa = dbS.pesquisaServicoPessoaPorPessoa(soc.getServicoPessoa().getPessoa().getId());
        if (!sv.deletarObjeto(sv.pesquisaCodigo(serPessoa.getId(), "ServicoPessoa"))) {
            msgConfirma = "Erro ao excluir serviço pessoa dependente!";
            return false;
        }
        msgConfirma = "Dependente excluído!";
        return true;
    }

    public void editarGenerico(Pessoa sessao, boolean reativar) {
        CategoriaDB dbCat = new CategoriaDBToplink();
        SociosDB db = new SociosDBToplink();
        FisicaDB dbf = new FisicaDBToplink();

        //socSessao = db.pesquisaSocioPorPessoaAtivo(sessao.getId());
        Socios socSessao = db.pesquisaSocioPorPessoa(sessao.getId());
        if (socSessao.getId() != -1 && reativar) {
            socios = socSessao;
        } else {
            return;
        }

        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno").equals("emissaoCarteirinha")) {
            return;
        }

        //Socios soc = db.pesquisaSocioDoDependente(socios.getId());
        // SOCIO DIFERENTE PARA TRAZER NA TELA O TITULAR
        if (socios.getMatriculaSocios().getTitular().getId() != servicoPessoa.getPessoa().getId()) {
            socios = db.pesquisaSocioPorPessoa(socios.getMatriculaSocios().getTitular().getId());
        }

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        servicoPessoa = socios.getServicoPessoa();
        matriculaSocios = socios.getMatriculaSocios();

        GrupoCategoria gpCat = dbCat.pesquisaGrupoPorCategoria(socios.getMatriculaSocios().getCategoria().getId());
        for (int i = 0; i < getListaGrupoCategoria().size(); i++) {
            if (Integer.parseInt((String) getListaGrupoCategoria().get(i).getDescription()) == gpCat.getId()) {
                idGrupoCategoria = i;
                break;
            }
        }
        int qntCategoria = getListaCategoria().size();
        for (int i = 0; i < qntCategoria; i++) {
            if (Integer.parseInt((String) getListaCategoria().get(i).getDescription()) == socios.getMatriculaSocios().getCategoria().getId()) {
                idCategoria = i;
                break;
            }
        }

        for (int i = 0; i < getListaTipoDocumento().size(); i++) {
            if (Integer.parseInt((String) listaTipoDocumento.get(i).getDescription()) == servicoPessoa.getTipoDocumento().getId()) {
                idTipoDocumento = i;
                break;
            }
        }

        atualizarListaDependenteAtivo();
        atualizarListaDependenteInativo();
        
    }

    public void editarOUsalvar(int index) {
        Fisica fisica = (Fisica) listaDependentes.get(index).getArgumento0();
        if (fisica.getId() == -1){
            novoDependente = new Fisica();
        }else{
            novoDependente = fisica;
        }
        
        fisicaPesquisa = new Fisica();
        index_dependente = index;
        
        modelVisible = true;
    }
    
    public void fechaModal() {
        modelVisible = false;
    }

    public void editarDependente(Fisica f) {
        dependente = (Fisica) f;

    }
    
    public void reativarDependente(){
        for (int i = 0; i < listaDependentesInativos.size(); i++){
            String vencimento_dep = DataHoje.data().substring(0, 2)+ "/"+listaDependentesInativos.get(i).getArgumento4().toString();
            String data_hoje =      DataHoje.data();
            
            if ( DataHoje.igualdadeData(vencimento_dep, data_hoje) || DataHoje.maiorData(vencimento_dep, data_hoje) || listaDependentesInativos.get(i).getArgumento4().toString().isEmpty()){
                listaDependentes.add(listaDependentesInativos.get(i));
                listaDependentesInativos.remove(i);
            }
        }
    }

    public void atualizarListaDependenteAtivo(){
        int index = 0;
        SociosDB db = new SociosDBToplink();
        FisicaDB dbf = new FisicaDBToplink();
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        // LISTA DE DEPENDENTES ATIVOS
        List<Socios> listaDepsAtivo = db.listaDependentes(matriculaSocios.getId());
        if (!listaDepsAtivo.isEmpty()) {
            listaDependentes.clear();
            for (int i = 0; i < listaDepsAtivo.size(); i++) {   
                // FISICA, PARENTESCO, VIA_CARTEIRINHA, DATA VALIDADE CARTEIRINHA, DATA VAL DEP
                Fisica fisica = dbf.pesquisaFisicaPorPessoa(listaDepsAtivo.get(i).getServicoPessoa().getPessoa().getId());
                List<Parentesco> listap = getListaParentesco(fisica.getSexo());
                ParentescoDB dbp = new ParentescoDBToplink();
                
                List<SelectItem> lista_si = new ArrayList<SelectItem>();
                for (int w = 0; w < listap.size(); w++) {
                    if (listaDepsAtivo.get(i).getParentesco().getId() == listap.get(w).getId()) {
                        index = w;
                    }
                    
                    lista_si.add(new SelectItem(w, listap.get(w).getParentesco(), Integer.toString(listap.get(w).getId())));
                }
                
                String vencimento_dep = "";
                
                if (!listaDepsAtivo.get(i).getServicoPessoa().getReferenciaValidade().isEmpty())        
                    vencimento_dep = DataHoje.data().substring(0, 2)+ "/"+listaDepsAtivo.get(i).getServicoPessoa().getReferenciaValidade();
                
                String data_hoje = DataHoje.data();

                if ( vencimento_dep.isEmpty() ||
                   (!vencimento_dep.isEmpty() && (DataHoje.igualdadeData(vencimento_dep, data_hoje) || DataHoje.maiorData(vencimento_dep, data_hoje) ))){
                    listaDependentes.add(
                            new DataObject(
                                fisica,
                                index,// PARENTESCO
                                listaDepsAtivo.get(i).getNrViaCarteirinha(), // VIA CARTEIRINHA
                                listaDepsAtivo.get(i).getValidadeCarteirinha(), // DATA VALIDADE CARTEIRINHA
                                listaDepsAtivo.get(i).getServicoPessoa().getReferenciaValidade(), // DATA VAL DEP
                                listaDepsAtivo.get(i).getServicoPessoa().getNrDesconto(), // DESCONTO
                                lista_si, // LISTA DE PARENTESCO
                                null,
                                null,
                                null
                            )
                    );
                }else{
                    // AQUI INATIVA AUTOMATICAMENTE SE O DEPENDENTE ESTIVER COM A REF VALIDADE < QUE A DATA ATUAL
                    sv.abrirTransacao();
                    ServicoPessoa sp2 = (ServicoPessoa) sv.pesquisaCodigo(listaDepsAtivo.get(i).getServicoPessoa().getId(), "ServicoPessoa");
                    sp2.setAtivo(false);
                    if (!sv.alterarObjeto(sp2)) {
                        GenericaMensagem.error("Erro", "Erro ao alterar Serviço Pessoa!");
                        sv.desfazerTransacao();
                        return;
                    }
                    sp2 = new ServicoPessoa();
                    sv.comitarTransacao();
                }
            }
        }
        
    }
    
    public void atualizarListaDependenteInativo(){
        int index = 0;
        SociosDB db = new SociosDBToplink();
        FisicaDB dbf = new FisicaDBToplink();
        // LISTA DE DEPENDENTES INATIVOS
        List<Socios> listaDepsInativo = db.listaDependentesInativos(matriculaSocios.getId());
        if (!listaDepsInativo.isEmpty()) {
            listaDependentesInativos.clear();
            for (int i = 0; i < listaDepsInativo.size(); i++) {
                // FISICA, PARENTESCO, VIA_CARTEIRINHA, DATA VALIDADE CARTEIRINHA, DATA VAL DEP
                Fisica fisica = dbf.pesquisaFisicaPorPessoa(listaDepsInativo.get(i).getServicoPessoa().getPessoa().getId());
                
                List<Parentesco> listap = getListaParentesco(fisica.getSexo());
                ParentescoDB dbp = new ParentescoDBToplink();
                List<SelectItem> lista_si = new ArrayList<SelectItem>();
                for (int w = 0; w < listap.size(); w++) {
                    if (listaDepsInativo.get(i).getParentesco().getId() == listap.get(w).getId()) {
                        index = w;
                    }
                    
                    lista_si.add(new SelectItem(w, listap.get(w).getParentesco(), Integer.toString(listap.get(w).getId())));
                }
                
                listaDependentesInativos.add(
                        new DataObject(
                            fisica,
                            index,// PARENTESCO
                            listaDepsInativo.get(i).getNrViaCarteirinha(), // VIA CARTEIRINHA
                            listaDepsInativo.get(i).getValidadeCarteirinha(), // DATA VALIDADE CARTEIRINHA
                            listaDepsInativo.get(i).getServicoPessoa().getReferenciaValidade(), // DATA VAL DEP
                            listaDepsInativo.get(i).getServicoPessoa().getNrDesconto(), // DESCONTO
                            lista_si, // LISTA DE PARENTESCO
                            null,
                            null,
                            null
                        )
                ); 
            }
        }      
    }
    
    public String atualizaValidade(Parentesco par, Fisica fisica) {
        if (par.getNrValidade() == 0) {
            return null;
        } else if (par.getNrValidade() > 0 && par.isValidade()) {
            if (!fisica.getNascimento().equals("") && fisica.getNascimento() != null) {
                return (new DataHoje()).incrementarAnos(par.getNrValidade(), fisica.getNascimento()).substring(3, 10);
            } else {
                return null;
            }
        } else if (par.getNrValidade() > 0 && !par.isValidade()) {
            return ((new DataHoje()).incrementarAnos(par.getNrValidade(), DataHoje.data())).substring(3, 10);
        }
        return null;
    }

    public void atualizaValidadeTela(int index) {
        ParentescoDB db = new ParentescoDBToplink();
        
        Parentesco par = null;
        Fisica fisica = (Fisica) ((DataObject) listaDependentes.get(index)).getArgumento0();
        
        List<Parentesco> listap = getListaParentesco(fisica.getSexo());
        ParentescoDB dbp = new ParentescoDBToplink();
        List<SelectItem> lista_si = new ArrayList<SelectItem>();
        for (int w = 0; w < listap.size(); w++) {
            lista_si.add(new SelectItem(w, listap.get(w).getParentesco(), Integer.toString(listap.get(w).getId())));
        }
        
        ((DataObject) listaDependentes.get(index)).setArgumento6(lista_si);
        int index_pa = Integer.valueOf( ((DataObject) listaDependentes.get(index)).getArgumento1().toString());
        
        par = db.pesquisaCodigo(Integer.valueOf(lista_si.get( index_pa ).getDescription() ));
        
        ((DataObject) listaDependentes.get(index)).setArgumento4(atualizaValidade(par, fisica));
    }

    public String visualizarCarteirinha() {
        if (socios.getId() == -1) {
            return null;
        }
        
        /*
            COMENTEI TODO ESSE CÓDIGO PORQUE A PRINCIPIO NA MUDANÇA QUANDO SALVAR O SÓCIO ELE SEMPRE TERÁ CARTEIRINHA
            EM FASE DE TESTES 22/05/2014 QUINTA-FEIRA -- COMÉRCIO RP -- DEPOIS EXCLUIR COMENTÁRIO
        */
        

//        boolean comita = false;
//        SociosDB db = new SociosDBToplink();
//
//        String data = DataHoje.data();
//        SocioCarteirinha carteirinha = new SocioCarteirinha();
//
//        
//        CategoriaDB dbCat = new CategoriaDBToplink();
//        GrupoCategoria gpCat = dbCat.pesquisaGrupoPorCategoria(socios.getMatriculaSocios().getCategoria().getId());
        
//        ModeloCarteirinha modeloc = dbc.pesquisaModeloCarteirinhaCategoria(socios.getMatriculaSocios().getCategoria().getId());
        
//        List<SocioCarteirinha> list_carteirinha_socio = db.pesquisaCarteirinhasPorPessoa(socios.getServicoPessoa().getPessoa().getId(), modeloc.getId());
        
        //boolean isSocioCarteirinhaPessoa = dbc.pesquisaSocioCarteirinhaSocio(socios.getServicoPessoa().getPessoa().getId()).isEmpty();
//        boolean isSocioCarteirinhaPessoa = list_carteirinha_socio.isEmpty();

//        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
//        sv.abrirTransacao();
//        if (isSocioCarteirinhaPessoa) {
//            carteirinha.setEmissao(data);
//            carteirinha.setPessoa(socios.getServicoPessoa().getPessoa());
//            if (!sv.inserirObjeto(carteirinha)) {
//                sv.desfazerTransacao();
//                return null;
//            }
//            DataHoje dh = new DataHoje();
//
//            socios.setNrViaCarteirinha(1);
//
//
//            socios.setDtValidadeCarteirinha(DataHoje.converte(dh.incrementarMeses(gpCat.getNrValidadeMesCartao(), DataHoje.data())));
//            
//            if (!sv.alterarObjeto(socios)) {
//                sv.desfazerTransacao();
//                return null;
//            }
//            sv.comitarTransacao();
//            comita = true;
//        } else {
//            carteirinha.setEmissao(data);
//            carteirinha.setPessoa(socios.getServicoPessoa().getPessoa());
//            if (!dbc.verificaSocioCarteirinhaExiste(socios.getServicoPessoa().getPessoa().getId())) {
//                if (!sv.inserirObjeto(carteirinha)) {
//                    sv.desfazerTransacao();
//                    return null;
//                }
//                sv.comitarTransacao();
//                comita = true;
//            }
//        }
        
        SocioCarteirinhaDB dbc = new SocioCarteirinhaDBToplink();
        ModeloCarteirinha modeloc = dbc.pesquisaModeloCarteirinha(socios.getMatriculaSocios().getCategoria().getId(), -1);
        SocioCarteirinha sc = dbc.pesquisaCarteirinhaPessoa(socios.getServicoPessoa().getPessoa().getId(), modeloc.getId());
        
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        if (sc != null && sc.getDtEmissao() == null){
            sv.abrirTransacao();
            sc.setDtEmissao(DataHoje.dataHoje());
            if(!sv.alterarObjeto(sc)){
                GenericaMensagem.warn("Erro", "Não foi possivel alterar data de emissão");
                sv.desfazerTransacao();
                return null;
            }
            sv.comitarTransacao();
        }
        List listaAux = dbc.filtroCartao(socios.getServicoPessoa().getPessoa().getId());
        
        Registro registro = (Registro) sv.pesquisaCodigo(1, "Registro");
        SociosDB db = new SociosDBToplink();
        if (registro.isCarteirinhaDependente() && !listaDependentes.isEmpty()){
            sv.abrirTransacao();
            for (DataObject listaDependente : listaDependentes) {
                Socios socioDependente = db.pesquisaSocioPorPessoaAtivo(((Fisica) listaDependente.getArgumento0()).getPessoa().getId());
                sc = dbc.pesquisaCarteirinhaPessoa(socioDependente.getServicoPessoa().getPessoa().getId(), modeloc.getId());
                
                if (sc != null && sc.getDtEmissao() == null){
                    sc.setDtEmissao(DataHoje.dataHoje());
                    if(!sv.alterarObjeto(sc)){
                        GenericaMensagem.warn("Erro", "Não foi possivel alterar data de emissão do dependente!");
                        sv.desfazerTransacao();
                        return null;
                    }
                }
                listaAux.addAll(dbc.filtroCartao(socioDependente.getServicoPessoa().getPessoa().getId()));
            }
            sv.comitarTransacao();
        }
        
        if (!listaAux.isEmpty()) {
            ((List) listaAux.get(0)).set(6, socios.getValidadeCarteirinha());
            ((List) listaAux.get(0)).set(11, socios.getNrViaCarteirinha());
            ImpressaoParaSocios.imprimirCarteirinha(listaAux);
        } else {
            msgConfirma = "Socio não tem carteirinha";
        }

//        if (!comita) {
//            sv.desfazerTransacao();
//        }      
        return null;
    }

    public String imprimirFichaSocial() {
        String foto = getFotoSocio();
        String path = "/Relatorios/FICHACADASTRO.jasper";
        String pathVerso = "/Relatorios/FICHACADASTROVERSO.jasper";
        String caminhoDiretorio = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/fichas");
        ImpressaoParaSocios.comDependente(
                caminhoDiretorio,
                "ficha_" + socios.getId() + "_" + socios.getServicoPessoa().getPessoa().getId() + ".pdf",
                path,
                pathVerso,
                socios,
                pessoaEmpresa,
                matriculaSocios,
                imprimirVerso,
                foto);
        return null;
    }

    public String imprimirFichaSocialVazia() {
        ImpressaoParaSocios.branco();
        return "menuSocial";
    }

    public void imprimirFichaSocialBranco() {
        ImpressaoParaSocios.branco();
    }

    public String getFotoSocio() {
        FacesContext context = FacesContext.getCurrentInstance();
        File files;
        if (socios.getId() != -1) {
            files = new File(((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/" + socios.getServicoPessoa().getPessoa().getId() + ".png"));
            if (files.exists()) {
                return files.getPath();
            } else {
                return ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Imagens/Fotos/semFoto.jpg");
            }
        } else {
            return ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Imagens/Fotos/semFoto.jpg");
        }
    }

    public void atualizarCategoria(){
        listaCategoria.clear();
        listaServicos.clear();
        //listaParentesco.clear();
        idCategoria = 0;
    }
    
    public List<SelectItem> getListaGrupoCategoria() {
        if (listaGrupoCategoria.isEmpty()){
            SalvarAcumuladoDB sadb = new SalvarAcumuladoDBToplink();
            List<GrupoCategoria> grupoCategorias = (List<GrupoCategoria>) sadb.listaObjeto("GrupoCategoria");
            if (!grupoCategorias.isEmpty()){
                for (int i = 0; i < grupoCategorias.size(); i++) {
                    listaGrupoCategoria.add(new SelectItem(i, grupoCategorias.get(i).getGrupoCategoria(), "" + grupoCategorias.get(i).getId()));
                }
            }else{
                listaGrupoCategoria.add(new SelectItem(0, "Nenhum Grupo Categoria Encontrado", "0"));
            }
            
        }
        return listaGrupoCategoria;
    }

    public List<SelectItem> getListaCategoria() {
        
        if (listaCategoria.isEmpty() && !listaGrupoCategoria.isEmpty()){
            CategoriaDB db = new CategoriaDBToplink();
            List<Categoria> select = db.pesquisaCategoriaPorGrupo(Integer.parseInt(getListaGrupoCategoria().get(idGrupoCategoria).getDescription()));
            if (!select.isEmpty()){
                for (int i = 0; i < select.size(); i++) {
                    listaCategoria.add(new SelectItem(i, select.get(i).getCategoria(), Integer.toString(select.get(i).getId())));
                }
            }else{
                listaCategoria.add(new SelectItem(0, "Nenhuma Categoria Encontrada", "0"));
            }
        }
        return listaCategoria;
    }

    public ServicoPessoa getServicoPessoa() {
        if (GenericaSessao.getObject("fisicaPesquisa") != null && GenericaSessao.getObject("reativarSocio") != null) {
            servicoPessoa.setPessoa(((Fisica) GenericaSessao.getObject("fisicaPesquisa")).getPessoa());
            editarGenerico(((Fisica) GenericaSessao.getObject("fisicaPesquisa")).getPessoa(), 
                            GenericaSessao.getBoolean("reativarSocio"));
            pessoaEmpresa = (PessoaEmpresa) GenericaSessao.getObject("pessoaEmpresaPesquisa");
            GenericaSessao.remove("fisicaPesquisa");
            GenericaSessao.remove("pessoaEmpresaPesquisa");
            GenericaSessao.remove("reativarSocio");
        }
        return servicoPessoa;
    }

    public void setServicoPessoa(ServicoPessoa servicoPessoa) {
        this.servicoPessoa = servicoPessoa;
    }

    public boolean isChkContaCobranca() {
        return chkContaCobranca;
    }

    public void setChkContaCobranca(boolean chkContaCobranca) {
        if (this.chkContaCobranca != chkContaCobranca) {
            listaTipoDocumento.clear();
        }
        this.chkContaCobranca = chkContaCobranca;
    }

    public List<SelectItem> getListaTipoDocumento() {
        if (listaTipoDocumento.isEmpty()) {
            int i = 0;
            FTipoDocumentoDB db = new FTipoDocumentoDBToplink();
            List<FTipoDocumento> select = new ArrayList();
            if (isChkContaCobranca()) {
                select.add(db.pesquisaCodigo(2));
            } else {
                select = db.pesquisaListaTipoExtrato();
            }
            if (!select.isEmpty()) {
                while (i < select.size()) {
                    listaTipoDocumento.add(new SelectItem(new Integer(i),
                            (String) (select.get(i).getDescricao()),
                            Integer.toString(select.get(i).getId())));
                    i++;
                }
            }
        }
        return listaTipoDocumento;
    }

    public void setListaTipoDocumento(List<SelectItem> listaTipoDocumento) {
        this.listaTipoDocumento = listaTipoDocumento;
    }

    public int getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(int idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public boolean isRenderServicos() {
        return renderServicos;
    }

    public void setRenderServicos(boolean renderServicos) {
        this.renderServicos = renderServicos;
    }

    public int getIdServico() {
        return idServico;
    }

    public void setIdServico(int idServico) {
        this.idServico = idServico;
    }

    public List<SelectItem> getListaServicos() {
        if (listaServicos.isEmpty() && !getListaGrupoCategoria().isEmpty() && !getListaCategoria().isEmpty()) {
            int i = 0;
            //ServicosDB db = new ServicosDBToplink();
            //RotinaDB dbr = new RotinaDBToplink();
            ServicoCategoriaDB db = new ServicoCategoriaDBToplink();
            //int id_grupo_categoria = Integer.parseInt(listaGrupoCategoria.get(idGrupoCategoria).getDescription());
            int id_categoria = Integer.parseInt(listaCategoria.get(idCategoria).getDescription());
            
            ServicoCategoria select = db.pesquisaPorParECat(1, id_categoria);
            
            if (select != null) {
                listaServicos.add(new SelectItem(i, select.getServicos().getDescricao(),
                            Integer.toString(select.getServicos().getId()))
                );
            }else{
                listaServicos.add(new SelectItem(0, "Nenhum Serviço Encontrado", "0"));
            }
        }
        return listaServicos;
    }

    public void setListaServicos(List<SelectItem> listaServicos) {
        this.listaServicos = listaServicos;
    }

    public Socios getSocios() {
        return socios;
    }

    public void setSocios(Socios socios) {
        this.socios = socios;
    }

    public int getIdGrupoCategoria() {
        return idGrupoCategoria;
    }

    public void setIdGrupoCategoria(int idGrupoCategoria) {
        this.idGrupoCategoria = idGrupoCategoria;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public MatriculaSocios getMatriculaSocios() {
        PessoaEnderecoDB db = new PessoaEnderecoDBToplink();
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        List<GrupoCidades> cids = (List<GrupoCidades>) salvarAcumuladoDB.listaObjeto("GrupoCidades", true);
        if (socios.getId() == -1 && matriculaSocios.getId() == -1) {
            matriculaSocios.setEmissao(DataHoje.data());
            PessoaEndereco ende = db.pesquisaEndPorPessoaTipo(servicoPessoa.getPessoa().getId(), 3);
            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cidadePesquisa") != null) {
                matriculaSocios.setCidade((Cidade) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cidadePesquisa"));
            } else if (ende != null && ende.getId() != -1) {
                for (int i = 0; i < cids.size(); i++) {
                    if (cids.get(i).getCidade().getId() == ende.getEndereco().getCidade().getId()) {
                        matriculaSocios.setCidade(ende.getEndereco().getCidade());
                        return matriculaSocios;
                    }
                }
                matriculaSocios.setCidade(((PessoaEndereco) db.pesquisaEndPorPessoaTipo(1, 3)).getEndereco().getCidade());
            } else {
                matriculaSocios.setCidade(((PessoaEndereco) db.pesquisaEndPorPessoaTipo(1, 3)).getEndereco().getCidade());
            }
        } else if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cidadePesquisa") != null) {
            matriculaSocios.setCidade((Cidade) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cidadePesquisa"));
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("cidadePesquisa");
        }
        return matriculaSocios;
    }

    public void setMatriculaSocios(MatriculaSocios matriculaSocios) {
        this.matriculaSocios = matriculaSocios;
    }

    public List<DataObject> getListaDependentes() {
        return listaDependentes;
    }

    public void setListaDependentes(List<DataObject> listaDependentes) {
        this.listaDependentes = listaDependentes;
    }
// AQUI LISTA DE PARENTESCO
//    public List<SelectItem> getListaParentesco() {
//        if (listaParentesco.isEmpty() && !listaCategoria.isEmpty()) {
//            ParentescoDB db = new ParentescoDBToplink();
//            if (Integer.valueOf(listaCategoria.get(idCategoria).getDescription()) == 0){
//                listaParentesco.add(new SelectItem(0, "Sem Categoria", "0"));
//                return listaParentesco;
//            }
//            
//            List<Parentesco> select = db.pesquisaTodosSemTitularCategoria(Integer.valueOf(listaCategoria.get(idCategoria).getDescription()));
//            //List<Parentesco> select = db.pesquisaTodosSemTitular();
//            
//            if (!select.isEmpty()){
//                for (int i = 0; i < select.size(); i++) {
//                    listaParentesco.add(new SelectItem(i,
//                            select.get(i).getParentesco(),
//                            Integer.toString(select.get(i).getId()))
//                    );
//                }
//            }else
//                listaParentesco.add(new SelectItem(0, "Sem Categoria", "0"));
//        }
//        return listaParentesco;
//    }
    
    public List<Parentesco> getListaParentesco(String sexo) {
        if (!listaCategoria.isEmpty()) {
            ParentescoDB db = new ParentescoDBToplink();
            if (Integer.valueOf(listaCategoria.get(idCategoria).getDescription()) == 0){
                return new ArrayList<Parentesco>();
            }
            
            //List<Parentesco> select = db.pesquisaTodosSemTitularCategoria(Integer.valueOf(listaCategoria.get(idCategoria).getDescription()));
            List<Parentesco> select = db.pesquisaTodosSemTitularCategoriaSexo(Integer.valueOf(listaCategoria.get(idCategoria).getDescription()), sexo);
            //List<Parentesco> select = db.pesquisaTodosSemTitular();
            
            if (!select.isEmpty())
                return select;
            else
                return new ArrayList<Parentesco>();
        }
        
        return new ArrayList<Parentesco>();
    }
//
//    public void setListaParentesco(List<SelectItem> listaParentesco) {
//        this.listaParentesco = listaParentesco;
//    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public String getPessoaImagem() {
        FacesContext context = FacesContext.getCurrentInstance();
        File files = new File(((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/"));
        File fExiste = new File(((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/fotoTemp.jpg"));
        File listFile[] = files.listFiles();
        String nome;
        //temFoto = false;
        if (fExiste.exists() && dependente.getDataFoto().isEmpty()) {
            fotoTemp = true;
        }
        if (fotoTemp) {
            nome = "fotoTemp";
        } else {
            nome = "semFoto";
        }
        int numArq = listFile.length;
        for (int i = 0; i < numArq; i++) {
            String n = listFile[i].getName();
            for (int o = 0; o < n.length(); o++) {
                if (n.substring(o, o + 1).equals(".")) {
                    n = listFile[i].getName().substring(0, o);
                }
            }
            try {
                if (!fotoTemp) {
                    if (Integer.parseInt(n) == dependente.getPessoa().getId()) {
                        nome = n;
                        fotoTemp = false;
                        String caminho = ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/fotoTemp.jpg");
                        File fl = new File(caminho);
                        fl.delete();
                        break;
                    }
                } else {
                    fotoTemp = false;
                    break;
                }
            } catch (NumberFormatException e) {
                
            }
        }
        return nome + ".jpg";
    }

    public Fisica getDependente() {
        return dependente;
    }

    public void setDependente(Fisica dependente) {
        this.dependente = dependente;
    }

    public int getIdIndexCombo() {
        return idIndexCombo;
    }

    public void setIdIndexCombo(int idIndexCombo) {
        this.idIndexCombo = idIndexCombo;
    }

    public String getLblSocio() {
        if (socios.getId() == -1) {
            lblSocio = "SALVAR ";
        } else {
            lblSocio = "ALTERAR";
        }
        return lblSocio;
    }

    public void setLblSocio(String lblSocio) {
        this.lblSocio = lblSocio;
    }

    public String getLblSocioPergunta() {
        if (socios.getId() == -1) {
            lblSocioPergunta = "Deseja associar esse cadastro? ";
        } else {
            lblSocioPergunta = "Deseja alterar esse cadastro?";
        }
        return lblSocioPergunta;
    }

    public void setLblSocioPergunta(String lblSocioPergunta) {
        this.lblSocioPergunta = lblSocioPergunta;
    }

    public boolean isDesabilitaImpressao() {
        if (socios.getId() != -1 && matriculaSocios.getId() != -1) {
            desabilitaImpressao = false;
        }
        return desabilitaImpressao;
    }

    public void setDesabilitaImpressao(boolean desabilitaImpressao) {
        this.desabilitaImpressao = desabilitaImpressao;
    }

    public String getTipoDestinario() {
        return tipoDestinario;
    }

    public void setTipoDestinario(String tipoDestinario) {
        this.tipoDestinario = tipoDestinario;
    }

    public boolean isImprimirVerso() {
        return imprimirVerso;
    }

    public void setImprimirVerso(boolean imprimirVerso) {
        this.imprimirVerso = imprimirVerso;
    }

    public String getDataInativacao() {
        return dataInativacao;
    }

    public void setDataInativacao(String dataInativacao) {
        this.dataInativacao = dataInativacao;
    }

    public int getIdInativacao() {
        return idInativacao;
    }

    public void setIdInativacao(int idInativacao) {
        this.idInativacao = idInativacao;
    }

    public List<SelectItem> getListaMotivoInativacao() {
        if (listaMotivoInativacao.isEmpty()) {
            SociosDB db = new SociosDBToplink();
            List<SMotivoInativacao> select = db.pesquisaMotivoInativacao();
            for (int i = 0; i < select.size(); i++) {
                listaMotivoInativacao.add(new SelectItem(i, select.get(i).getDescricao(), Integer.toString(select.get(i).getId())));
            }
        }
        return listaMotivoInativacao;
    }

    public String getDataReativacao() {
        return dataReativacao;
    }

    public void setDataReativacao(String dataReativacao) {
        this.dataReativacao = dataReativacao;
    }

    public String getStatusSocio() {
        if (socios.getId() == -1) {
            statusSocio = "STATUS";
        } else {
            if (matriculaSocios.getMotivoInativacao() != null) {
                statusSocio = "INATIVO / " + matriculaSocios.getMotivoInativacao().getDescricao() + " - " + matriculaSocios.getInativo();
            } else {
                statusSocio = "ATIVO";
            }
        }
        return statusSocio;
    }

    public void setStatusSocio(String statusSocio) {
        this.statusSocio = statusSocio;
    }

    public List<Fisica> getListaFisica() {
        return listaFisica;
    }

    public void setListaFisica(List<Fisica> listaFisica) {
        this.listaFisica = listaFisica;
    }

    public List<SelectItem> getListaSelectFisica() {
        return listaSelectFisica;
    }

    public void setListaSelectFisica(List<SelectItem> listaSelectFisica) {
        this.listaSelectFisica = listaSelectFisica;
    }

    public Fisica getFisicaPesquisa() {
        return fisicaPesquisa;
    }

    public void setFisicaPesquisa(Fisica fisicaPesquisa) {
        this.fisicaPesquisa = fisicaPesquisa;
    }

    public Pessoa getPessoaPesquisa() {
        return pessoaPesquisa;
    }

    public void setPessoaPesquisa(Pessoa pessoaPesquisa) {
        this.pessoaPesquisa = pessoaPesquisa;
    }

    public Fisica getNovoDependente() {
        return novoDependente;
    }

    public void setNovoDependente(Fisica novoDependente) {
        this.novoDependente = novoDependente;
    }

    public int getIndex_dependente() {
        return index_dependente;
    }

    public void setIndex_dependente(int index_dependente) {
        this.index_dependente = index_dependente;
    }

    public List<Socios> getListaSocioInativo() {
        return listaSocioInativo;
    }

    public void setListaSocioInativo(List<Socios> listaSocioInativo) {
        this.listaSocioInativo = listaSocioInativo;
    }

    public List<DataObject> getListaDependentesInativos() {
        return listaDependentesInativos;
    }

    public void setListaDependentesInativos(List<DataObject> listaDependentesInativos) {
        this.listaDependentesInativos = listaDependentesInativos;
    }

    public Part getFilePart() {
        return filePart;
    }

    public void setFilePart(Part filePart) {
        this.filePart = filePart;
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

    public String getFotoTempPerfil() {
        return fotoTempPerfil;
    }

    public void setFotoTempPerfil(String fotoTempPerfil) {
        this.fotoTempPerfil = fotoTempPerfil;
    }

    public boolean isModelVisible() {
        return modelVisible;
    }

    public void setModelVisible(boolean modelVisible) {
        this.modelVisible = modelVisible;
    }
}
