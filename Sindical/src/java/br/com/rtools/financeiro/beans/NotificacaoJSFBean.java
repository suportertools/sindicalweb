package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.CobrancaEnvio;
import br.com.rtools.financeiro.CobrancaLote;
import br.com.rtools.financeiro.CobrancaTipo;
import br.com.rtools.financeiro.db.FinanceiroDB;
import br.com.rtools.financeiro.db.FinanceiroDBToplink;
import br.com.rtools.impressao.ParametroNotificacao;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.controleUsuarioJSFBean;
import br.com.rtools.sistema.Links;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Download;
import br.com.rtools.utilitarios.EnviarEmail;
import br.com.rtools.utilitarios.SalvaArquivos;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class NotificacaoJSFBean {
    private int idLista = 0;
    private int idTipoEnvio = 0;
    private List<SelectItem> itensLista = new ArrayList();
    private List<SelectItem> listaTipoEnvio = new ArrayList();
    private String msgConfirma = "";
    private CobrancaLote lote = new CobrancaLote();
    
    
    public String salvar(){
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();
        
        if (!sv.alterarObjeto(lote)){
            msgConfirma = "Erro ao atualizar Mensagem";
            sv.desfazerTransacao();
            return null;
        }
        
        sv.comitarTransacao();
        msgConfirma = "Atualizado com sucesso!";
        return null;
    }
    
    public String gerarNotificacao(){
        if (((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario")).getId() == -1){
            msgConfirma = "Usuário não esta na sessão, faça seu login novamente!";
            return null;
        }
        lote.setUsuario(((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario")));
        lote.setDtEmissao(DataHoje.dataHoje());
        
        FinanceiroDB db = new FinanceiroDBToplink();
        if (db.pesquisaCobrancaLote(lote.getUsuario().getId(), lote.getDtEmissao()) != null){
            msgConfirma = "Notificação já gerada hoje!";
            return null;
        }
        
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();
        if (!sv.inserirObjeto(lote)){
            msgConfirma = "Erro ao Gerar Lote";
            sv.desfazerTransacao();
            return null;
        }
        sv.comitarTransacao();        
        
        sv.abrirTransacao();
        if (!sv.inserirQuery("insert into fin_cobranca (id_movimento,id_lote) (" +
                             "select m.id, " +lote.getId()+
                             "  from  pes_juridica_vw as pj " +
                             " inner join fin_movimento      as m  on m.id_pessoa = pj.id_pessoa " +
                             " inner join pes_juridica_vw as sind on sind.id_pessoa = 1" +
                             " inner join pes_juridica as j on j.id_pessoa = pj.id_pessoa " +
                             " inner join arr_contribuintes_vw as co on co.id_juridica = j.id " +
                             " where m.id_baixa is null and is_ativo = true and m.dt_vencimento < '"+ DataHoje.data() +"')")){
            msgConfirma = "Erro ao inserir Cobrança";
            lote = new CobrancaLote();
            sv.desfazerTransacao();
            return null;
        }
        
        sv.comitarTransacao();        
        msgConfirma = "Gerado com sucesso!";
        itensLista.clear();
        return null;
    }
    
    public String enviarNotificacao(){
        if (lote.getId() == -1){
            msgConfirma = "Selecione um Lote para envio!";
            return null;
        }        
        
        if (((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario")).getId() == -1){
            msgConfirma = "Usuário não esta na sessão, faça seu login novamente!";
            return null;
        }
        
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        CobrancaTipo ct = (CobrancaTipo)sv.pesquisaCodigo(Integer.valueOf(listaTipoEnvio.get(idTipoEnvio).getDescription()), "CobrancaTipo");
        
        FinanceiroDB db = new FinanceiroDBToplink();
        List<Vector> result = db.listaNotificacao(ct.getId(), lote.getId());
        List<ParametroNotificacao> listax = new ArrayList();
        
        CobrancaEnvio ce = new CobrancaEnvio();
        ce.setDtEmissao(DataHoje.dataHoje());
        ce.setHora(DataHoje.horaMinuto());
        ce.setLote(lote);
        ce.setUsuario(((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario")));
        ce.setTipoCobranca(ct);
        
        sv.abrirTransacao();
        if (!sv.inserirObjeto(ce)){
            msgConfirma = "Erro ao salvar Cobrança Envio";
            sv.desfazerTransacao();
            return null;
        }
        
        if (ct.getId() == 4 || ct.getId() == 5){
            int id_compara = 0; boolean enviar = false;
            //PessoaDB dbp = new PessoaDBToplink();
            //JuridicaDB dbj = new JuridicaDBToplink();
            Pessoa pes = new Pessoa();
            String jasper = "";
            for (int i = 0; i < result.size(); i++){
                listax.add(new ParametroNotificacao(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Imagens/LogoCliente.png"), 
                                                    getConverteNullString(result.get(i).get(1)),
                                                    getConverteNullString(result.get(i).get(2)), 
                                                    getConverteNullString(result.get(i).get(3)), 
                                                    getConverteNullString(result.get(i).get(4)), 
                                                    getConverteNullString(result.get(i).get(5)), 
                                                    getConverteNullString(result.get(i).get(6)),
                                                    getConverteNullString(result.get(i).get(7)),
                                                    getConverteNullString(result.get(i).get(8)),
                                                    getConverteNullString(result.get(i).get(9)),
                                                    getConverteNullString(result.get(i).get(10)), 
                                                    getConverteNullString(result.get(i).get(11)), 
                                                    getConverteNullString(result.get(i).get(12)),
                                                    getConverteNullString(result.get(i).get(13)), 
                                                    getConverteNullString(result.get(i).get(14)),
                                                    getConverteNullString(result.get(i).get(15)),
                                                    getConverteNullString(result.get(i).get(16)), 
                                                    getConverteNullString(result.get(i).get(17)), 
                                                    getConverteNullString(result.get(i).get(18)), 
                                                    getConverteNullString(result.get(i).get(19)),
                                                    getConverteNullString(result.get(i).get(20)),
                                                    getConverteNullString(result.get(i).get(21)), 
                                                    getConverteNullString(result.get(i).get(22)),
                                                    getConverteNullString(result.get(i).get(23)), 
                                                    getConverteNullString(result.get(i).get(24)), 
                                                    getConverteNullString(result.get(i).get(25)), 
                                                    getConverteNullString(result.get(i).get(26)), 
                                                    getConverteNullString(result.get(i).get(27)), 
                                                    getConverteNullString(result.get(i).get(28)), 
                                                    getConverteNullString(result.get(i).get(29)), 
                                                    getConverteNullString(result.get(i).get(30)),
                                                    getConverteNullString(result.get(i).get(31)),
                                                    getConverteNullString(result.get(i).get(32)),
                                                    getConverteNullString(result.get(i).get(33)),
                                                    getConverteNullString(result.get(i).get(34)),
                                                    getConverteNullString(result.get(i).get(35)),
                                                    getConverteNullString(result.get(i).get(36)),
                                                    getConverteNullString(result.get(i).get(37))
                        )
                    );
                
                try{
                    if (ct.getId() == 4){
                        jasper = "NOTIFICACAO_ARRECADACAO_ESCRITORIO.jasper";
                        id_compara = getConverteNullInt( result.get(i).get(38) ); // ID_JURIDICA
                        if (id_compara != getConverteNullInt( result.get(i+1).get(38) )){
                            enviar = true;
                            pes = ((Juridica)sv.pesquisaCodigo(id_compara, "Juridica")).getPessoa();
                        }
                    }else{
                        jasper = "NOTIFICACAO_ARRECADACAO_EMPRESA.jasper";
                        id_compara = getConverteNullInt( result.get(i).get(39) ); // ID_PESSOA
                        if (id_compara != getConverteNullInt( result.get(i+1).get(39) )){
                            enviar = true;
                            pes = (Pessoa)sv.pesquisaCodigo(id_compara, "Pessoa");
                            //pes = ((Juridica)sv.pesquisaCodigo(id_compara, "Juridica")).getPessoa();
                        }
                    }
                }catch(Exception e){
                    if (ct.getId() == 4)
                        pes = ((Juridica)sv.pesquisaCodigo(id_compara, "Juridica")).getPessoa();
                    else
                        pes = (Pessoa)sv.pesquisaCodigo(id_compara, "Pessoa");
                    enviar = true;
                }
                
                if (enviar){
                    try{
                        if (!pes.getEmail1().isEmpty())
                            enviarEmail(pes, listax, sv, jasper);
                    }catch(Exception e){}
                    enviar = false;
                    listax.clear();
                }
            }
        }else{
            for (int i = 0; i < result.size(); i++){
                listax.add(new ParametroNotificacao(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Imagens/LogoCliente.png"), 
                                                    getConverteNullString(result.get(i).get(1)),
                                                    getConverteNullString(result.get(i).get(2)), 
                                                    getConverteNullString(result.get(i).get(3)), 
                                                    getConverteNullString(result.get(i).get(4)), 
                                                    getConverteNullString(result.get(i).get(5)), 
                                                    getConverteNullString(result.get(i).get(6)),
                                                    getConverteNullString(result.get(i).get(7)),
                                                    getConverteNullString(result.get(i).get(8)),
                                                    getConverteNullString(result.get(i).get(9)),
                                                    getConverteNullString(result.get(i).get(10)), 
                                                    getConverteNullString(result.get(i).get(11)), 
                                                    getConverteNullString(result.get(i).get(12)),
                                                    getConverteNullString(result.get(i).get(13)), 
                                                    getConverteNullString(result.get(i).get(14)),
                                                    getConverteNullString(result.get(i).get(15)),
                                                    getConverteNullString(result.get(i).get(16)), 
                                                    getConverteNullString(result.get(i).get(17)), 
                                                    getConverteNullString(result.get(i).get(18)), 
                                                    getConverteNullString(result.get(i).get(19)),
                                                    getConverteNullString(result.get(i).get(20)),
                                                    getConverteNullString(result.get(i).get(21)), 
                                                    getConverteNullString(result.get(i).get(22)),
                                                    getConverteNullString(result.get(i).get(23)), 
                                                    getConverteNullString(result.get(i).get(24)), 
                                                    getConverteNullString(result.get(i).get(25)), 
                                                    getConverteNullString(result.get(i).get(26)), 
                                                    getConverteNullString(result.get(i).get(27)), 
                                                    getConverteNullString(result.get(i).get(28)), 
                                                    getConverteNullString(result.get(i).get(29)), 
                                                    getConverteNullString(result.get(i).get(30)),
                                                    getConverteNullString(result.get(i).get(31)),
                                                    getConverteNullString(result.get(i).get(32)),
                                                    getConverteNullString(result.get(i).get(33)),
                                                    getConverteNullString(result.get(i).get(34)),
                                                    getConverteNullString(result.get(i).get(35)),
                                                    getConverteNullString(result.get(i).get(36)),
                                                    getConverteNullString(result.get(i).get(37))
                        )
                    );
            }
            
            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(listax);
            JasperReport jasper = null;
            String nomeArq = "notificacao_";
            try{
                if (ct.getId() == 1){
                    jasper = (JasperReport) JRLoader.loadObject( ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/NOTIFICACAO_ARRECADACAO_ESCRITORIO.jasper") );
                }else if (ct.getId() == 2){
                    jasper = (JasperReport) JRLoader.loadObject( ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/NOTIFICACAO_ARRECADACAO_EMPRESA.jasper") );
                }else if (ct.getId() == 3){
                    jasper = (JasperReport) JRLoader.loadObject( ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/NOTIFICACAO_ARRECADACAO_EMPRESA.jasper") );
                }
                

                JasperPrint print = JasperFillManager.fillReport(jasper, null, dtSource);
                byte[] arquivo = new byte[0];
                arquivo = JasperExportManager.exportReportToPdf(print);

                String nomeDownload = nomeArq + DataHoje.hora().replace(":", "") +".pdf";
                SalvaArquivos sa = new SalvaArquivos(arquivo, nomeDownload, false);
                String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/"+ controleUsuarioJSFBean.getCliente()+"/Arquivos/notificacao");
                sa.salvaNaPasta(pathPasta); 
                
                Download download =  new Download(nomeDownload, pathPasta, "application/pdf", FacesContext.getCurrentInstance());
                download.baixar();
            }catch(Exception e){
                
            }
        }                   
        
        if (!result.isEmpty())
            sv.comitarTransacao();
        return null;
    }

    public String enviarEmail(Pessoa pessoa, List<ParametroNotificacao> lista, SalvarAcumuladoDB sv, String nomeJasper){
        
        JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
        JasperReport jasper = null;
        String nomeArq = "notificacao_";
        try{
            jasper = (JasperReport) JRLoader.loadObject( ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/"+nomeJasper) );
            JasperPrint print = JasperFillManager.fillReport(jasper, null, dtSource);

            byte[] arquivo = new byte[0];
            arquivo = JasperExportManager.exportReportToPdf(print);

            String nomeDownload = nomeArq + pessoa.getId() + DataHoje.hora().replace(":", "") +".pdf";
            SalvaArquivos sa = new SalvaArquivos(arquivo, nomeDownload, false);
            String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/"+ controleUsuarioJSFBean.getCliente()+"/Arquivos/notificacao");
            sa.salvaNaPasta(pathPasta);        
            Registro reg = (Registro)sv.pesquisaCodigo(1, "Registro");
        
            Links link = new Links();
            link.setCaminho(reg.getUrlPath()+"/Sindical/Cliente/"+controleUsuarioJSFBean.getCliente()+"/Arquivos/notificacao");
            link.setNomeArquivo(nomeDownload);
            link.setPessoa(pessoa);

            if (!sv.inserirObjeto(link)){
                msgConfirma = "Erro ao salvar Link de envio!";
                sv.desfazerTransacao();
                return null;
            }
            
            List<Pessoa> pes_add = new ArrayList();
            pes_add.add(pessoa);
            
            
            String[] ret = new String[2];
            if (!reg.isEnviarEmailAnexo()){
                ret = EnviarEmail.EnviarEmailPersonalizado(reg, 
                                    pes_add, 
                                    " <h5>Visualize sua notificação clicando no link abaixo</5><br /><br />" +
                                    " <a href='"+reg.getUrlPath()+"/Sindical/acessoLinks.jsf?cliente="+controleUsuarioJSFBean.getCliente()+"&amp;arquivo="+nomeDownload+"' target='_blank'>Clique aqui para abrir a Notificação</a><br />", 
                                    new ArrayList(), 
                                    "Envio de Notificação");
            }else{
                 List<File> fls = new ArrayList<File>();
                fls.add(new File(link.getCaminho()+"/"+link.getNomeArquivo()));
                            
                ret = EnviarEmail.EnviarEmailPersonalizado(reg, 
                                                    pes_add, 
                                                    " <h5>Baixe sua notificação em Anexada neste email</5><br /><br />", 
                                                    fls, 
                                                    "Envio de Notificação");
            }
            if (!ret[1].isEmpty())
                msgConfirma = ret[1];
            else
                msgConfirma = ret[0];
        }catch(Exception e){
            
        }
        return null;
    }
    
    public void alteraCombo(){
        if (!itensLista.isEmpty()){
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            if (Integer.valueOf(itensLista.get(idLista).getDescription()) == -1)
                lote = new CobrancaLote();
            else
                lote = (CobrancaLote)sv.pesquisaCodigo(Integer.valueOf(itensLista.get(idLista).getDescription()), "CobrancaLote");
        }        
    }

   public String getConverteNullString(Object object){
       if (object == null)
           return "";
       else
           return String.valueOf(object);
   }    
    
    public int getConverteNullInt(Object object){
        if (object == null)
            return 0;
        else
            return (Integer)object;
    }      
    
    public int getIdLista() {
        return idLista;
    }

    public void setIdLista(int idLista) {
        this.idLista = idLista;
    }

    public List<SelectItem> getItensLista() {
        if (itensLista.isEmpty()){
            FinanceiroDB db = new FinanceiroDBToplink();
            List<CobrancaLote> result = db.listaCobrancaLote();
            itensLista.add(new SelectItem(0, "<< Novo >>", String.valueOf(-1)));
            for (int i = 0; i < result.size(); i++){
                itensLista.add(new SelectItem(new Integer(i+1), 
                                              result.get(i).getEmissao()+" - "+result.get(i).getUsuario().getLogin(), 
                                              String.valueOf(result.get(i).getId())
                    )
                );
            }
        }
        return itensLista;
    }

    public void setItensLista(List<SelectItem> itensLista) {
        this.itensLista = itensLista;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public CobrancaLote getLote() {
        return lote;
    }

    public void setLote(CobrancaLote lote) {
        this.lote = lote;
    }

    public List<SelectItem> getListaTipoEnvio() {
        if (listaTipoEnvio.isEmpty()){
            FinanceiroDB db = new FinanceiroDBToplink();
            List<CobrancaTipo> result = db.listaCobrancaTipoEnvio();
            for (int i = 0; i < result.size(); i++){
                listaTipoEnvio.add(new SelectItem(new Integer(i), 
                                              result.get(i).getDescricao(), 
                                              String.valueOf(result.get(i).getId())
                    )
                );
            }
        }        
        return listaTipoEnvio;
    }

    public void setListaTipoEnvio(List<SelectItem> listaTipoEnvio) {
        this.listaTipoEnvio = listaTipoEnvio;
    }

    public int getIdTipoEnvio() {
        return idTipoEnvio;
    }

    public void setIdTipoEnvio(int idTipoEnvio) {
        this.idTipoEnvio = idTipoEnvio;
    }
}
