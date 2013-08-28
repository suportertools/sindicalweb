package br.com.rtools.arrecadacao.beans;

import br.com.rtools.arrecadacao.CnaeConvencao;
import br.com.rtools.arrecadacao.Convencao;
import br.com.rtools.arrecadacao.db.CnaeConvencaoDB;
import br.com.rtools.arrecadacao.db.CnaeConvencaoDBToplink;
import br.com.rtools.arrecadacao.db.ConvencaoDB;
import br.com.rtools.arrecadacao.db.ConvencaoDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Cnae;
import br.com.rtools.pessoa.db.CnaeDB;
import br.com.rtools.pessoa.db.CnaeDBToplink;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.model.SelectItem;

public class CnaeConvencaoJSFBean {
    private List<DataObject> listaCnaes = new ArrayList();
    private List<DataObject> listaCnaesAdc = new ArrayList();
    private Cnae cnae = new Cnae();
    private String descricao = "";
    private int idConvencao = 0;
    private String msgConfirma = "";
    
    public int getIdConvencao() {
        return idConvencao;
    }

    public void setIdConvencao(int idConvencao) {
        this.idConvencao = idConvencao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void refreshForm(){

    }

    public String salvarSelecionados(){
        NovoLog log = new NovoLog();

        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        
        int iConvencao = Integer.parseInt(getListaConvencao().get(idConvencao).getDescription());
        Convencao convencao = (Convencao)sv.pesquisaCodigo( iConvencao, "Convencao" );
        
        sv.abrirTransacao();
        for (int i = 0; i < listaCnaes.size(); i++){
                if ((Boolean)listaCnaes.get(i).getArgumento0()){
                CnaeConvencao cnaeConvencao = new CnaeConvencao(-1, (Cnae)listaCnaes.get(i).getArgumento1(), convencao);
                        
                if (sv.inserirObjeto(cnaeConvencao)){
                    msgConfirma = "CNAES adcionados com sucesso!";
                    log.novo("Novo registro", "CNAE inserido "+cnaeConvencao.getId()+" - "+cnaeConvencao.getConvencao().getDescricao()+" - "+cnaeConvencao.getCnae().getCnae()+"( Numero: "+cnaeConvencao.getCnae().getNumero()+" )");
                }else{
                    sv.desfazerTransacao();
                    msgConfirma = "Erro ao adicionar CNAE!";
                    return null;
                }

            }
        }
        sv.comitarTransacao();
        
        atualizaListaDisponiveis();
        atualizarListaAdc();
        return null;
    }

   public void atualizaListaDisponiveis(){
       listaCnaes.clear();
       CnaeDB dbCnae = new CnaeDBToplink();
       List<Cnae> list = dbCnae.pesquisaCnaeSemConvencao(descricao);
       for (int i = 0; i < list.size(); i++){
           listaCnaes.add( new DataObject(false, list.get(i)));
       }
   }

   public void atualizarListaAdc(){
       listaCnaesAdc.clear();
       CnaeConvencaoDB db = new CnaeConvencaoDBToplink();
       int iConvencao = Integer.parseInt(getListaConvencao().get(idConvencao).getDescription());
       List<CnaeConvencao> listaCnaeCon = db.pesquisarCnaeConvencaoPorConvencao(iConvencao);
       for (int i = 0; i < listaCnaeCon.size(); i++){
           listaCnaesAdc.add( new DataObject(false, (CnaeConvencao)(listaCnaeCon.get(i))) );
       }
   }   
   
   public String excluirTodos(){
       NovoLog log = new NovoLog();

       SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
       if(!listaCnaesAdc.isEmpty()){
           sv.abrirTransacao();
           for (int i = 0; i < listaCnaesAdc.size(); i++){
               CnaeConvencao cn = (CnaeConvencao)sv.pesquisaCodigo( ((CnaeConvencao)listaCnaesAdc.get(i).getArgumento1()).getId() , "CnaeConvencao");
               
               if (!sv.deletarObjeto(cn)){
                   sv.desfazerTransacao();
                   msgConfirma = "Erro ao excluir Registros!";
                   return null;
               }
               log.novo("Excluido", cn.getId()+" - "+cn.getConvencao().getDescricao()+" - "+cn.getCnae().getCnae()+"( Numero: "+cn.getCnae().getNumero()+" )");
           }
           sv.comitarTransacao();
           
           atualizaListaDisponiveis();
           atualizarListaAdc();
           msgConfirma = "Cnaes excluídos com sucesso!";
       }
       return null;
   }

    public String excluirSelecionados(){
        NovoLog log = new NovoLog();

        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        if(!listaCnaesAdc.isEmpty()){
            sv.abrirTransacao();
            for(int i = 0; i < listaCnaesAdc.size(); i++){
                if( (Boolean)listaCnaesAdc.get(i).getArgumento0() ){
                    CnaeConvencao cn = (CnaeConvencao)sv.pesquisaCodigo( ((CnaeConvencao)listaCnaesAdc.get(i).getArgumento1()).getId() , "CnaeConvencao");
                    if (!sv.deletarObjeto(cn)){
                        sv.desfazerTransacao();
                        msgConfirma = "Erro ao excluir Registros!";
                        return null;
                    }
                    log.novo("Excluido", cn.getId()+" - "+cn.getConvencao().getDescricao()+" - "+cn.getCnae().getCnae()+"( Numero: "+cn.getCnae().getNumero()+" )");
                }
            }
            sv.comitarTransacao();
            
            atualizaListaDisponiveis();
            atualizarListaAdc();
            msgConfirma = "Cnaes excluídos com sucesso!";
        }
        return null;
    }

   public List<SelectItem> getListaConvencao(){
       List<SelectItem> convencoes = new Vector<SelectItem>();
       int i = 0;
       ConvencaoDB db = new ConvencaoDBToplink();
       List select = db.pesquisaTodos();
       while (i < select.size()){
           convencoes.add(new SelectItem(new Integer(i),
                  (String) ((Convencao) select.get(i)).getDescricao(),
                  Integer.toString(((Convencao) select.get(i)).getId())  ));
          i++;
       }
       return convencoes;
   }

    public Cnae getCnae() {
        return cnae;
    }

    public void setCnae(Cnae cnae) {
        this.cnae = cnae;
    }

    public List<DataObject> getListaCnaes() {
        if (listaCnaes.isEmpty() && !descricao.isEmpty()){
            atualizaListaDisponiveis();
        }
        return listaCnaes;
    }

    public void setListaCnaes(List<DataObject> listaCnaes) {
        this.listaCnaes = listaCnaes;
    }

    public List<DataObject> getListaCnaesAdc() {
        if(!getListaConvencao().isEmpty()){
            int iConvencao = Integer.parseInt(getListaConvencao().get(idConvencao).getDescription());   
            if (listaCnaesAdc.isEmpty()){
                atualizarListaAdc();
            }else if ( iConvencao !=  ((CnaeConvencao)listaCnaesAdc.get(0).getArgumento1()).getConvencao().getId() ){
                atualizarListaAdc();
            }        
        }
        return listaCnaesAdc;
    }

    public void setListaCnaesAdc(List<DataObject> listaCnaesAdc) {
        this.listaCnaesAdc = listaCnaesAdc;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }
}