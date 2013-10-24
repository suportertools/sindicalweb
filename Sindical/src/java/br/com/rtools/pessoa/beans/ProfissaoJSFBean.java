//package br.com.rtools.pessoa.beans;
//
//import br.com.rtools.homologacao.beans.PesquisarProfissaoJSFBean;
//import br.com.rtools.pessoa.Profissao;
//import br.com.rtools.utilitarios.SalvarAcumuladoDB;
//import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
//
//public class ProfissaoJSFBean extends PesquisarProfissaoJSFBean {
//
//    private Profissao prof = new Profissao();
//    private String msgConfirma;
//    private String s_cbo = "";
//    private String s_profissao = "";
//    private int idIndexProfissao = -1;
//
//    public String novaProfissao() {
//        prof = new Profissao();
//        super.profissao = new Profissao();
//        return null;
//        // return "profissao";
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
//    public void editarProfissao() {
//        prof = (Profissao) super.getListaProfissao().get(super.getIdIndexProf());
//    }
//    
//    public void editarProfissao(Profissao p) {
//        prof = (Profissao) p;
//    }
//
//    public String salvarProfissao() {
//        if (prof.getProfissao().equals("")) {
//            msgConfirma = "Digite a profissão!";
//            return null;
//        }
//        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
//        sv.abrirTransacao();
//        if (prof.getId() == -1) {
//            if (sv.descricaoExiste(prof.getProfissao(), "profissao", "Profissao")) {
//                msgConfirma = "Profissão já cadastrada!";
//                return null;
//            }
//            if (!prof.getCbo().equals("")) {
//                if (sv.descricaoExiste(prof.getCbo(), "cbo", "Profissao")) {
//                    msgConfirma = "CBO já existe!";
//                    return null;
//                }
//            }
//            if (sv.inserirObjeto(prof)) {
//                sv.comitarTransacao();
//                msgConfirma = "Profissão salva com sucesso";
//            } else {
//                sv.desfazerTransacao();
//                msgConfirma = "Erro ao salvar profissão!";
//                return null;
//            }
//        } else {
//            if (sv.alterarObjeto(prof)) {
//                sv.comitarTransacao();
//                msgConfirma = "Profissão atualizada com sucesso";
//            } else {
//                sv.desfazerTransacao();
//                msgConfirma = "Erro ao atualizar profissão!";
//                return null;
//            }
//        }
//        super.getListaProfissao().clear();
//        return null;
//    }
//    
//    public String excluirProfissao() {
//
//        if (prof.getId() == -1) {
//            msgConfirma = "Selecione uma profissão para ser excluída!";
//            return null;
//        }
//
//        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
//
//        sv.abrirTransacao();
//        if (sv.deletarObjeto(prof = (Profissao) sv.pesquisaCodigo(prof.getId(), "Profissao"))) {
//            msgConfirma = "Profissão deletada com sucesso!";
//            sv.comitarTransacao();
//        } else {
//            msgConfirma = "Erro ao deletar profissão!";
//            sv.desfazerTransacao();
//        }
//        super.getListaProfissao().clear();
//        prof = new Profissao();
//        return null;
//    }
//
////    public String excluirProfissao(){
////        ProfissaoDB db = new ProfissaoDBToplink();
////        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
////        
////        if (super.profissao.getId() == -1){
////            msgConfirma = "Selecione uma profissão para ser excluída!";
////            return null;
////        }
////        
////        sv.abrirTransacao();
////        if (sv.deletarObjeto(super.profissao = (Profissao)sv.pesquisaCodigo(super.profissao.getId(), "Profissao"))){
////            msgConfirma = "Profissão deletada com sucesso!";
////            sv.comitarTransacao();
////        }else{
////            msgConfirma = "Erro ao deletar profissão!";
////            sv.desfazerTransacao();
////        }
////        super.getListaProfissao().clear();
////        super.profissao = new Profissao();
////        return null;
////    }
//    public int getIdIndexProfissao() {
//        return idIndexProfissao;
//    }
//
//    public void setIdIndexProfissao(int idIndexProfissao) {
//        this.idIndexProfissao = idIndexProfissao;
//    }
//
//    public String getS_cbo() {
//        s_cbo = super.profissao.getCbo();
//        return s_cbo;
//    }
//
//    public void setS_cbo(String s_cbo) {
//        this.s_cbo = s_cbo;
//    }
//
//    public String getS_profissao() {
//        s_profissao = super.profissao.getProfissao();
//        return s_profissao;
//    }
//
//    public void setS_profissao(String s_profissao) {
//        this.s_profissao = s_profissao;
//    }
//
//    public Profissao getProf() {
//        return prof;
//    }
//
//    public void setProf(Profissao prof) {
//        this.prof = prof;
//    }
//}