//
//package br.com.rtools.erro;
//
//import br.com.rtools.financeiro.Movimento;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ErroMovimento extends Erro{
//
//    public ErroMovimento(){
//        super();
//    }
//
//    @Override
//    public boolean adicionarObjetoEmErro(int id, Object objeto) {
//        Movimento movimento = null;
//        try{
//            movimento = (Movimento) objeto;
//            return super.adicionarObjetoEmErro(id, objeto);
//        }catch (Exception e){
//            return false;
//        }
//    }
//
//    @Override
//    protected String getConteudo(int id) {
//        String result = "";
//        try{
//            List<Movimento> lista = (ArrayList) super.hashObject.get(id);
//            for (Movimento movimento : lista){
//                result += (movimento.getNumero() + " - " +
//                           movimento.getPessoa().getNome() + ", " +
//                           movimento.getReferencia() + ", " +
//                           movimento.getServicos().getDescricao() + ", " +
//                           movimento.getTipoServico().getDescricao()
//                          ) + "\n";
//            }
//        }catch (Exception e){
//            result = "";
//        }
//        return result;
//    }
//}
