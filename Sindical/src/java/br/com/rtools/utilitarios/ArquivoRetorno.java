package br.com.rtools.utilitarios;

import br.com.rtools.financeiro.ContaCobranca;
import br.com.rtools.financeiro.Lote;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.TipoServico;
import br.com.rtools.financeiro.db.*;
import br.com.rtools.movimento.GerarMovimento;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.db.*;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.controleUsuarioJSFBean;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

public abstract class ArquivoRetorno {

    private ContaCobranca contaCobranca;
    private boolean pendentes;
    
    public final static int SICOB = 1;
    public final static int SINDICAL = 2;
    public final static int SIGCB = 3;

    public final static int CAIXA_FEDERAL = 109;
    public final static int REAL = 82;
    //public final static int BANESPA = 82;
    public final static int BANCO_BRASIL = 36;
    public final static int ITAU = 63;
    public abstract List<GenericaRetorno> sicob(boolean baixar, String host);
    public abstract List<GenericaRetorno> sindical(boolean baixar, String host);
    public abstract List<GenericaRetorno> sigCB(boolean baixar, String host);
    public abstract String darBaixaSicob(String caminho, Usuario usuario);
    public abstract String darBaixaSigCB(String caminho, Usuario usuario);
    public abstract String darBaixaSindical(String caminho, Usuario usuario);
    public abstract String darBaixaPadrao(Usuario usuario);

    protected ArquivoRetorno(ContaCobranca contaCobranca, boolean pendentes){
        this.contaCobranca = contaCobranca;
        this.pendentes = pendentes;
    }

    protected String baixarArquivo(List<GenericaRetorno> listaParametros, String caminho, Usuario usuario){
        String cnpj = "";
        String referencia = "";
        String dataVencto = "";
        //String fatorQuitacao = "";
        String result = "";
        String destino = caminho + "/" +DataHoje.ArrayDataHoje()[2]+"-"+DataHoje.ArrayDataHoje()[1]+"-"+DataHoje.ArrayDataHoje()[0];
        String path = "";
        if (this.pendentes)
            path = caminho +"/pendentes";
        else
            path = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/"+controleUsuarioJSFBean.getCliente()+"/Arquivos/retorno/pendentes/");
        
        boolean moverArquivo = true;
        
        List<String> listaDtPagamentos = new ArrayList<String>();
        List<Float> listaTaxa = new ArrayList<Float>();
        //List<Movimento> listaMovimentos = new ArrayList();
        //List<DocumentoInvalido> listaDocumentoInvalido = new ArrayList();
        //List<Float> listaValor = new ArrayList();
        List<String> errors = new ArrayList<String>();
        
        MovimentoDB db = new MovimentoDBToplink();
        JuridicaDB dbJur = new JuridicaDBToplink();
        PessoaDB dbPes = new PessoaDBToplink();
        //DocumentoInvalidoDB dbDocInv = new DocumentoInvalidoDBToplink();
        FTipoDocumentoDB dbft = new FTipoDocumentoDBToplink();
        FilialDB dbFilial = new FilialDBToplink();
        
        List<Movimento> movimento = new ArrayList();
        //DocumentoInvalido docInv = new DocumentoInvalido();
        File fl = new File(path);
        File listFls[] = fl.listFiles();
        File flDes = new File(destino); // 0 DIA, 1 MES, 2 ANO
        flDes.mkdir();

        TipoServicoDB dbTipo = new TipoServicoDBToplink();
        TipoServico tipoServico = new TipoServico();
        // LAYOUT 2 = SINDICAL
        if (this.getContaCobranca().getLayout().getId() == 2){
            for(int u = 0; u < listaParametros.size(); u ++){
//                listaDocumentoInvalido = dbDocInv.pesquisaNumeroBoletoPessoa();
//                if (!listaDocumentoInvalido.isEmpty()){
//                    for(int w = 0; w < listaDocumentoInvalido.size(); w++){
//                        docInv = listaDocumentoInvalido.get(w);
//                        dbDocInv.getEntityManager().getTransaction().begin();
//                        if (dbDocInv.delete(docInv))
//                            dbDocInv.getEntityManager().getTransaction().commit();
//                        else
//                            dbDocInv.getEntityManager().getTransaction().rollback();
//                        docInv = new DocumentoInvalido();
//                    }
//                    listaDocumentoInvalido = new ArrayList();
//                }
                // VERIFICA O TIPO DA EMPRESA -------------------------------------------------------------------------------------------------
                // ----------------------------------------------------------------------------------------------------------------------------
                if (dbFilial.pesquisaCodigoRegistro(1).getTipoEmpresa().equals("E")){
                    // VERIFICA O ANO QUE VEIO NO ARQUIVO MENOR QUE ANO 2000 -------------------------------------------------------
                    // -------------------------------------------------------------------------------------------------------------
                    if ( Integer.parseInt(listaParametros.get(u).getDataVencimento().substring(4,8)) < 2000 ){
                        referencia = DataHoje.dataReferencia(DataHoje.colocarBarras(listaParametros.get(u).getDataPagamento()));
                        dataVencto = DataHoje.colocarBarras(listaParametros.get(u).getDataPagamento());
                        if( referencia.substring(0, 2).equals("03") )
                            tipoServico = dbTipo.pesquisaCodigo(1);
                        else
                            tipoServico = dbTipo.pesquisaCodigo(2);
                    }else{
                        referencia = DataHoje.dataReferencia(DataHoje.colocarBarras(listaParametros.get(u).getDataVencimento()));
                        dataVencto = DataHoje.colocarBarras(listaParametros.get(u).getDataVencimento());
                        if( referencia.substring(0, 2).equals("03") )
                            tipoServico = dbTipo.pesquisaCodigo(1);
                        else
                            tipoServico = dbTipo.pesquisaCodigo(2);
                    }
                }else{
                    if ( Integer.parseInt(listaParametros.get(u).getDataVencimento().substring(4,8)) < 2000 ){
                        referencia = DataHoje.dataReferencia(DataHoje.colocarBarras(listaParametros.get(u).getDataPagamento()));
                        dataVencto = DataHoje.colocarBarras(listaParametros.get(u).getDataPagamento());
                        if( referencia.substring(0, 2).equals("01") )
                            tipoServico = dbTipo.pesquisaCodigo(1);
                        else
                            tipoServico = dbTipo.pesquisaCodigo(2);
                    }else{
                        referencia = DataHoje.dataReferencia(DataHoje.colocarBarras(listaParametros.get(u).getDataVencimento()));
                        dataVencto = DataHoje.colocarBarras(listaParametros.get(u).getDataVencimento());
                        if( referencia.substring(0, 2).equals("01") )
                            tipoServico = dbTipo.pesquisaCodigo(1);
                        else
                            tipoServico = dbTipo.pesquisaCodigo(2);
                    }
                }
                // ----------------------------------------------------------------------------------------------------------------------------
                // ----------------------------------------------------------------------------------------------------------------------------

                // 1 caso VERIFICA SE EXISTE BOLETO PELO NUMERO DO BOLETO JA BAIXADO -------------------------------------------------------------------
                // -------------------------------------------------------------------------------------------------------------------
                
                String numeroComposto = listaParametros.get(u).getNossoNumero()+
                                        listaParametros.get(u).getDataPagamento()+
                                        listaParametros.get(u).getValorPago().substring(5, listaParametros.get(u).getValorPago().length());
                int nrSequencia = Integer.valueOf(listaParametros.get(u).getSequencialArquivo());
                
                movimento = db.pesquisaMovPorNumDocumentoListBaixado(listaParametros.get(u).getNossoNumero(), this.getContaCobranca().getId());
                if (!movimento.isEmpty()){
                    // EXISTE O BOLETO  MAS CONTEM VALORES DIFERENTES --------------
                    Movimento mov2 = movimento.get(0);
                    
                    Servicos servicos = (Servicos) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(1, "Servicos");
                    
                    movimento = db.pesquisaMovPorNumPessoaListBaixado(numeroComposto, this.getContaCobranca().getId());
                    if (movimento.isEmpty()){
                        Movimento movi = new Movimento(-1, 
                            null, 
                            servicos.getPlano5(), 
                            mov2.getPessoa(),
                            servicos,
                            null,
                            tipoServico,
                            null,
                            0,
                            referencia,
                            dataVencto,
                            1,
                            true,
                            "E",
                            false,
                            mov2.getPessoa(),
                            mov2.getPessoa(),
                            numeroComposto,
                            "",
                            dataVencto,
                            0,0,0,0,0,
                            Moeda.substituiVirgulaFloat(Moeda.converteR$(listaParametros.get(u).getValorTaxa())) / 100,
                            Moeda.substituiVirgulaFloat(Moeda.converteR$(listaParametros.get(u).getValorPago())) / 100,
                            dbft.pesquisaCodigo(2),0);

                        if (GerarMovimento.salvarUmMovimentoBaixa(new Lote(), movi)){
                            float valor_liquido = Moeda.substituiVirgulaFloat(Moeda.converteR$(listaParametros.get(u).getValorCredito())) / 100;
                            GerarMovimento.baixarMovimento(
                                                            movi, 
                                                            usuario, 
                                                            DataHoje.colocarBarras(listaParametros.get(u).getDataPagamento()), 
                                                            valor_liquido, 
                                                            DataHoje.converte(DataHoje.colocarBarras(listaParametros.get(u).getDataCredito())),
                                                            numeroComposto, nrSequencia
                            );      
                        }
                    }else{
                        if (movimento.get(0).getBaixa().getSequenciaBaixa() == 0){
                            movimento.get(0).getBaixa().setSequenciaBaixa(nrSequencia);
                            
                            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
                            sv.abrirTransacao();
                            if (sv.alterarObjeto(movimento.get(0).getBaixa()))
                                sv.comitarTransacao();
                            else
                                sv.desfazerTransacao();
                        }
                    }
                    movimento.clear();
                    continue;
                }
                
                // 2 caso VERIFICA SE EXISTE BOLETO PELO NUMERO DO BOLETO AINDA NÃO BAIXADO -------------------------------------------------------------------
                movimento = db.pesquisaMovPorNumDocumentoListSindical(listaParametros.get(u).getNossoNumero(), this.getContaCobranca().getId());                
                if (!movimento.isEmpty()){
                    // ENCONTROU O BOLETO PRA BAIXAR
                    movimento.get(0).setValorBaixa(Moeda.substituiVirgulaFloat(Moeda.converteR$(listaParametros.get(u).getValorPago())) / 100);
                    movimento.get(0).setTaxa(Moeda.substituiVirgulaFloat(Moeda.converteR$(listaParametros.get(u).getValorTaxa())) / 100);
                    
                    float valor_liquido = Moeda.substituiVirgulaFloat(Moeda.converteR$(listaParametros.get(u).getValorCredito())) / 100;
                    GerarMovimento.baixarMovimento(
                                                    movimento.get(0), 
                                                    usuario, 
                                                    DataHoje.colocarBarras(listaParametros.get(u).getDataPagamento()), 
                                                    valor_liquido, 
                                                    DataHoje.converte(DataHoje.colocarBarras(listaParametros.get(u).getDataCredito())), 
                                                    numeroComposto, nrSequencia
                    );         
                    continue;
                }
                
                // 3 caso VERIFICA SE EXISTE BOLETO PELO CNPJ DA EMPRESA + DATA DE PAGAMENTO + VALOR PAGO BAIXADO ---------------------------
                // ------------------------------------------------------------------------------------------------------
                movimento = db.pesquisaMovPorNumPessoaListBaixado(numeroComposto, this.getContaCobranca().getId());
                
                if (!movimento.isEmpty()){
                    // EXISTE O BOLETO PELO CNPJ DA EMPRESA + DATA DE PAGAMENTO BAIXADO --------------
                    movimento.clear();
                    continue;
                }
                
                List<Juridica> listJuridica = dbJur.pesquisaJuridicaParaRetorno(listaParametros.get(u).getNossoNumero());
                
                if (!listJuridica.isEmpty()){
                    movimento = db.pesquisaMovimentoChaveValor(listJuridica.get(0).getPessoa().getId(), referencia, this.getContaCobranca().getId(), tipoServico.getId());
                    
                    if (!movimento.isEmpty()){
                        movimento.get(0).setValorBaixa(Moeda.substituiVirgulaFloat(Moeda.converteR$(listaParametros.get(u).getValorPago())) / 100);
                        movimento.get(0).setTaxa(Moeda.substituiVirgulaFloat(Moeda.converteR$(listaParametros.get(u).getValorTaxa())) / 100);

                        float valor_liquido = Moeda.substituiVirgulaFloat(Moeda.converteR$(listaParametros.get(u).getValorCredito())) / 100;
                        GerarMovimento.baixarMovimento(
                                                        movimento.get(0), 
                                                        usuario, 
                                                        DataHoje.colocarBarras(listaParametros.get(u).getDataPagamento()), 
                                                        valor_liquido,
                                                        DataHoje.converte(DataHoje.colocarBarras(listaParametros.get(u).getDataCredito())), 
                                                        numeroComposto, nrSequencia
                        );                    
                        continue;
                    }
                    
                    Servicos servicos = (Servicos) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(1, "Servicos");
                    Movimento movi = new Movimento(-1, 
                            null, 
                            servicos.getPlano5(), 
                            listJuridica.get(0).getPessoa(),
                            servicos,
                            null,
                            tipoServico,
                            null,
                            0,
                            referencia,
                            dataVencto,
                            1,
                            true,
                            "E",
                            false,
                            listJuridica.get(0).getPessoa(),
                            listJuridica.get(0).getPessoa(),
                                listaParametros.get(u).getNossoNumero()+
                                listaParametros.get(u).getDataPagamento()+
                                listaParametros.get(u).getValorPago().substring(5, listaParametros.get(u).getValorPago().length()),
                            "",
                            dataVencto,
                            0,0,0,0,0,
                            Moeda.substituiVirgulaFloat(Moeda.converteR$(listaParametros.get(u).getValorTaxa())) / 100,
                            Moeda.substituiVirgulaFloat(Moeda.converteR$(listaParametros.get(u).getValorPago())) / 100,
                            dbft.pesquisaCodigo(2),
                            0);

                    if (GerarMovimento.salvarUmMovimentoBaixa(new Lote(), movi)){
                        float valor_liquido = Moeda.substituiVirgulaFloat(Moeda.converteR$(listaParametros.get(u).getValorCredito())) / 100;
                        GerarMovimento.baixarMovimento(
                                                        movi, 
                                                        usuario, 
                                                        DataHoje.colocarBarras(listaParametros.get(u).getDataPagamento()), 
                                                        valor_liquido, 
                                                        DataHoje.converte(DataHoje.colocarBarras(listaParametros.get(u).getDataCredito())),
                                                        numeroComposto, nrSequencia
                        );      
                    }
                }else{
                    
                    Servicos servicos = (Servicos) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(1, "Servicos");
                    Movimento movi = new Movimento(-1, 
                            null, 
                            servicos.getPlano5(), 
                            dbPes.pesquisaCodigo(0),
                            servicos,
                            null,
                            tipoServico,
                            null,
                            0,
                            referencia,
                            dataVencto,
                            1,
                            true,
                            "E",
                            false,
                            dbPes.pesquisaCodigo(0),
                            dbPes.pesquisaCodigo(0),
                                listaParametros.get(u).getNossoNumero()+
                                listaParametros.get(u).getDataPagamento()+
                                listaParametros.get(u).getValorPago().substring(5, listaParametros.get(u).getValorPago().length()),
                            "",
                            dataVencto,
                            0,0,0,0,0,
                            Moeda.substituiVirgulaFloat(Moeda.converteR$(listaParametros.get(u).getValorTaxa())) / 100,
                            Moeda.substituiVirgulaFloat(Moeda.converteR$(listaParametros.get(u).getValorPago())) / 100,
                            dbft.pesquisaCodigo(2),0);
                    
                    if (GerarMovimento.salvarUmMovimentoBaixa(new Lote(), movi)){
                        float valor_liquido = Moeda.substituiVirgulaFloat(Moeda.converteR$(listaParametros.get(u).getValorCredito())) / 100;
                        GerarMovimento.baixarMovimento(
                                                        movi,
                                                        usuario, 
                                                        DataHoje.colocarBarras(listaParametros.get(u).getDataPagamento()),
                                                        valor_liquido, 
                                                        DataHoje.converte(DataHoje.colocarBarras(listaParametros.get(u).getDataCredito())),
                                                        numeroComposto, nrSequencia
                        );                          
                    }
                }
            }
        }else{
            // SE NÃO FOR SINDICAL ---------------------------
            for(int u = 0; u < listaParametros.size(); u ++){
                if (cnpj.equals("")){
                    cnpj = AnaliseString.mascaraCnpj(listaParametros.get(0).getCnpj());
                    if (dbJur.pesquisaJuridicaPorDoc(cnpj).isEmpty()){
                        errors.add(" Documento não Existe no Sistema! "+ listaParametros.get(u).getCnpj());
                        //return " Documento não Existe no Sistema! "+ listaParametros.get(u).getCnpj();
                    }
                }
                movimento = db.pesquisaMovPorNumDocumentoList(listaParametros.get(u).getNossoNumero(), 
                                                              DataHoje.converte( DataHoje.colocarBarras(listaParametros.get(u).getDataVencimento()) ),
                                                              this.getContaCobranca().getId());
                if (!movimento.isEmpty()){
                    if (movimento.size() == 1){
                        //listaDtPagamentos.add(DataHoje.colocarBarras(listaParametros.get(u).getDataPagamento()));
                        //listaTaxa.add(Moeda.substituiVirgulaFloat(Moeda.converteR$(listaParametros.get(u).getValorTaxa())) / 100);
                        //listaValor.add(Moeda.substituiVirgulaFloat(Moeda.converteR$(listaParametros.get(u).getValorPago())) / 100);
                        
                        movimento.get(0).setValorBaixa(Moeda.substituiVirgulaFloat(Moeda.converteR$(listaParametros.get(u).getValorPago())) / 100);
                        movimento.get(0).setTaxa(Moeda.substituiVirgulaFloat(Moeda.converteR$(listaParametros.get(u).getValorTaxa())) / 100);
                        
                        GerarMovimento.baixarMovimento(movimento.get(0), usuario, DataHoje.colocarBarras(listaParametros.get(u).getDataPagamento()), 0, null, "", 0);
                    }
                }
                movimento = new ArrayList();
                //listaDtPagamentos = new ArrayList<String>();
                //listaTaxa = new ArrayList<Float>();
                //listaValor = new ArrayList();
            }
        }
        
        
        // TERMINAR O CASO DE PENDENTES OU NÃO --------------------------------
        if (listFls != null){
            if (this.pendentes && moverArquivo){
                for (int i = 0; i < listFls.length; i++){
                    flDes = new File(caminho+"/pendentes/"+listFls[i].getName());
                    
                    fl = new File(destino+"/"+listFls[i].getName());
                    if (fl.exists())
                        fl.delete();
                    
                    if (!flDes.renameTo(fl))
                        result = " Erro ao mover arquivo!";
                }
            }else if (this.pendentes && !moverArquivo){
                
            }else if (moverArquivo){
                for (int i = 0; i < listFls.length; i++){
                    try{
                        fl = new File(path+"/"+listFls[i].getName());
                        flDes = new File(destino+"/"+listFls[i].getName());
                        if (flDes.exists())
                            flDes.delete();
                        
                        if ( !fl.renameTo(flDes) )
                            result = " Erro ao mover arquivo!";
                    }catch(Exception e){
                        continue;
                    }
                }
            }else{
                for (int i = 0; i < listFls.length; i++){
                    try{
                        flDes = new File(caminho+"/pendentes"); // 0 DIA, 1 MES, 2 ANO
                        flDes.mkdir();
                        
                        fl = new File(path+"/"+listFls[i].getName());
                        
                        flDes = new File(caminho+"/pendentes/"+listFls[i].getName());
                        if (flDes.exists())
                            flDes.delete();
                        
                        if ( !fl.renameTo(flDes) )
                            result = " Erro ao mover arquivo!";
                    }catch(Exception e){
                        continue;
                    }
                }
            }
        }
        return result;
    }

    protected  String baixarArquivoPadrao(List<GenericaRetorno> listaParametros, Usuario usuario){
        return "Processo Concluido []";
    }

    public ContaCobranca getContaCobranca() {
        return contaCobranca;
    }

    public void setContaCobranca(ContaCobranca contaCobranca) {
        this.contaCobranca = contaCobranca;
    }

    public boolean isPendentes() {
        return pendentes;
    }

    public void setPendentes(boolean pendentes) {
        this.pendentes = pendentes;
    }
}


                // VERIFICA SE EXISTE BOLETO NA TABELA DE DOCUMENTOS INVALIDOS  -----------------------------------------
                // ------------------------------------------------------------------------------------------------------
//                listaDocumentoInvalido = dbDocInv.pesquisaNumeroBoleto(listaParametros.get(u).getNossoNumero());
//                if(!listaDocumentoInvalido.isEmpty() && listaDocumentoInvalido.get(0).isChecado()){
////                    listaDtPagamentos.add(DataHoje.colocarBarras(listaParametros.get(u).getDataPagamento()));
////                    listaTaxa.add(Moeda.substituiVirgulaFloat(Moeda.converteR$(listaParametros.get(u).getValorTaxa())) / 100);
////                    listaValor.add(Moeda.substituiVirgulaFloat(Moeda.converteR$(listaParametros.get(u).getValorPago())) / 100);
//                    
//                    docInv = dbDocInv.pesquisaCodigo(listaDocumentoInvalido.get(0).getId());
//                    
//                    Movimento movi = new Movimento(-1, 
//                            null, 
//                            this.getServicoContaCobranca().getServicos().getPlano5(), 
//                            dbPes.pesquisaCodigo(0),
//                            this.getServicoContaCobranca().getServicos(),
//                            null,
//                            tipoServico,
//                            null,
//                            0,
//                            referencia,
//                            dataVencto,
//                            1,
//                            true,
//                            "E",
//                            false,
//                            dbPes.pesquisaCodigo(0),
//                            dbPes.pesquisaCodigo(0),
//                            "",
//                            "",
//                            dataVencto,
//                            0,0,0,0,0,
//                            Moeda.substituiVirgulaFloat(Moeda.converteR$(listaParametros.get(u).getValorTaxa())) / 100,
//                            Moeda.substituiVirgulaFloat(Moeda.converteR$(listaParametros.get(u).getValorPago())) / 100,
//                            dbft.pesquisaCodigo(2),0);
//                    
//                    GerarMovimento.salvarUmMovimento(new Lote(), movi);      
//                    GerarMovimento.baixarMovimento(movi, usuario, DataHoje.colocarBarras(listaParametros.get(u).getDataPagamento()));      
//
//                    dbDocInv.getEntityManager().getTransaction().begin();
//                    if (dbDocInv.delete(docInv))
//                        dbDocInv.getEntityManager().getTransaction().commit();
//                    else
//                        dbDocInv.getEntityManager().getTransaction().rollback();
//                }else{
//                    // VERIFICA SE EXISTE BOLETO PELO NUMERO DO DOCUMENTO DA PESSOA ------------------------------------------------------
//                    // -------------------------------------------------------------------------------------------------------------------
//                    
//                    movimento = db.pesquisaMovPorTipoDocumentoBaixado(listaParametros.get(u).getNossoNumero(), referencia, this.getServicoContaCobranca().getContaCobranca().getId(), tipoServico);
//                    if (movimento.isEmpty()){
//                        movimento = db.pesquisaMovPorTipoDocumentoList(listaParametros.get(u).getNossoNumero(), referencia, this.getServicoContaCobranca().getContaCobranca().getId(), tipoServico);
//                        if (!movimento.isEmpty()){
//                            listaDtPagamentos.add(DataHoje.colocarBarras(listaParametros.get(u).getDataPagamento()));
//                            listaTaxa.add(Moeda.substituiVirgulaFloat(Moeda.converteR$(listaParametros.get(u).getValorTaxa())) / 100);
//                            listaValor.add(Moeda.substituiVirgulaFloat(Moeda.converteR$(listaParametros.get(u).getValorPago())) / 100);
//                            listaMovimentos.add(movimento.get(0));
//                        }else{
//                            List<Juridica> listJuridica = dbJur.pesquisaJuridicaParaRetorno(listaParametros.get(u).getNossoNumero());
//                            if (!listJuridica.isEmpty()){
////                                listaDtPagamentos.add(DataHoje.colocarBarras(listaParametros.get(u).getDataPagamento()));
////                                listaTaxa.add(Moeda.substituiVirgulaFloat(Moeda.converteR$(listaParametros.get(u).getValorTaxa())) / 100);
////                                listaValor.add(Moeda.substituiVirgulaFloat(Moeda.converteR$(listaParametros.get(u).getValorPago())) / 100);
//
//                                Movimento movi = new Movimento(-1, 
//                                        null, 
//                                        this.getServicoContaCobranca().getServicos().getPlano5(), 
//                                        dbPes.pesquisaCodigo(0),
//                                        this.getServicoContaCobranca().getServicos(),
//                                        null,
//                                        tipoServico,
//                                        null,
//                                        Moeda.substituiVirgulaFloat(Moeda.converteR$(listaParametros.get(u).getValorPago())) / 100,
//                                        referencia,
//                                        dataVencto,
//                                        1,
//                                        true,
//                                        "E",
//                                        false,
//                                        dbPes.pesquisaCodigo(0),
//                                        dbPes.pesquisaCodigo(0),
//                                        "",
//                                        "",
//                                        dataVencto,
//                                        0,0,0,0,0,
//                                        Moeda.substituiVirgulaFloat(Moeda.converteR$(listaParametros.get(u).getValorTaxa())) / 100,
//                                        Moeda.substituiVirgulaFloat(Moeda.converteR$(listaParametros.get(u).getValorPago())) / 100,
//                                        dbft.pesquisaCodigo(3),0);
//
//                                GerarMovimento.salvarUmMovimento(new Lote(), movi);
//                                
//                                GerarMovimento.baixarMovimento(movi, usuario, DataHoje.colocarBarras(listaParametros.get(u).getDataPagamento()));   
////                                listaMovBaixar.add(movi);
////                                OperacaoMovimento op = new OperacaoMovimento(listaMovBaixar);
////                                op.gerarMovimentoSemVerificacao( dbRot.pesquisaCodigo(4) );
//                                if (movi.getId() != -1)
//                                    listaMovimentos.add( movi );
//                                else{
//                                    errors.add(" Verifique o Cadastro de: "+ movi.getPessoa().getNome());
//                                    moverArquivo = false;
//                                }
//                            }else{
//                                listaDocumentoInvalido = dbDocInv.pesquisaNumeroBoleto(listaParametros.get(u).getNossoNumero());
//                                if(listaDocumentoInvalido.isEmpty()){
//                                    docInv.setImportacao(DataHoje.data());
//                                    docInv.setChecado(false);
//                                    docInv.setDocumentoInvalido(listaParametros.get(u).getNossoNumero());
//                                    dbDocInv.insert(docInv);
//                                }else{
//                                    docInv = listaDocumentoInvalido.get(0);
//                                    docInv.setImportacao(DataHoje.data());
//                                    dbDocInv.getEntityManager().getTransaction().begin();
//                                    if(dbDocInv.update(docInv))
//                                        dbDocInv.getEntityManager().getTransaction().commit();
//                                    else
//                                        dbDocInv.getEntityManager().getTransaction().rollback();
//                                }
//                                docInv = new DocumentoInvalido();
//                                movimento = new ArrayList();
//                                movimento.add(new Movimento());
//                                //errors.add("Boleto não encontrado "+ listaParametros.get(u).getNossoNumero());
//                                moverArquivo = false;
//                            }
//                        }
//                    }else{
//                        movimento = new ArrayList();
//                        movimento.add(new Movimento());
//                        //errors.add("Boleto ja Baixado "+ listaParametros.get(u).getNossoNumero());
//                    }
//                }       