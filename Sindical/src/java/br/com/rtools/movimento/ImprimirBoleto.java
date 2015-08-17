package br.com.rtools.movimento;

import br.com.rtools.arrecadacao.Acordo;
import br.com.rtools.arrecadacao.ConfiguracaoArrecadacao;
import br.com.rtools.arrecadacao.Convencao;
import br.com.rtools.arrecadacao.ConvencaoServico;
import br.com.rtools.arrecadacao.beans.ConfiguracaoArrecadacaoBean;
import br.com.rtools.arrecadacao.dao.ConvencaoServicoDao;
import br.com.rtools.arrecadacao.db.AcordoDB;
import br.com.rtools.arrecadacao.db.AcordoDBToplink;
import br.com.rtools.arrecadacao.db.CnaeConvencaoDB;
import br.com.rtools.arrecadacao.db.CnaeConvencaoDBToplink;
import br.com.rtools.arrecadacao.db.ConvencaoCidadeDB;
import br.com.rtools.arrecadacao.db.ConvencaoCidadeDBToplink;
import br.com.rtools.associativo.db.MovimentosReceberSocialDB;
import br.com.rtools.associativo.db.MovimentosReceberSocialDBToplink;
import br.com.rtools.cobranca.*;
import br.com.rtools.financeiro.Boleto;
import br.com.rtools.financeiro.ContaCobranca;
import br.com.rtools.financeiro.Historico;
import br.com.rtools.financeiro.Lote;
import br.com.rtools.financeiro.MensagemCobranca;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.ServicoContaCobranca;
import br.com.rtools.financeiro.db.ContaCobrancaDB;
import br.com.rtools.financeiro.db.ContaCobrancaDBToplink;
import br.com.rtools.financeiro.db.FinanceiroDB;
import br.com.rtools.financeiro.db.FinanceiroDBToplink;
import br.com.rtools.financeiro.db.MovimentoDB;
import br.com.rtools.financeiro.db.MovimentoDBToplink;
import br.com.rtools.financeiro.db.ServicoContaCobrancaDB;
import br.com.rtools.financeiro.db.ServicoContaCobrancaDBToplink;
import br.com.rtools.impressao.DemonstrativoAcordo;
import br.com.rtools.impressao.ParametroBoleto;
import br.com.rtools.impressao.ParametroBoletoSocial;
import br.com.rtools.impressao.Promissoria;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.db.FilialDB;
import br.com.rtools.pessoa.db.FilialDao;
import br.com.rtools.pessoa.db.FisicaDB;
import br.com.rtools.pessoa.db.FisicaDBToplink;
import br.com.rtools.pessoa.db.JuridicaDB;
import br.com.rtools.pessoa.db.JuridicaDBToplink;
import br.com.rtools.pessoa.db.PessoaEnderecoDB;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.sistema.Links;
import br.com.rtools.utilitarios.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

public class ImprimirBoleto {

    private String pathPasta = "";
    private byte[] arquivo = new byte[0];

    public List<Movimento> atualizaContaCobrancaMovimento(List<Movimento> lista) {
        ServicoContaCobranca scc = new ServicoContaCobranca();
        ServicoContaCobrancaDB db = new ServicoContaCobrancaDBToplink();
        ContaCobrancaDB dbc = new ContaCobrancaDBToplink();
        MovimentoDB dbm = new MovimentoDBToplink();
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

        List<Movimento> listaAdd = new ArrayList();
        Boleto bol = new Boleto();

        for (int i = 0; i < lista.size(); i++) {
            bol = dbm.pesquisaBoletos(lista.get(i).getNrCtrBoleto());
            if (bol == null) {
                ContaCobranca cc = dbc.pesquisaServicoCobranca(lista.get(i).getServicos().getId(), lista.get(i).getTipoServico().getId());
                int id_boleto = dbm.inserirBoletoNativo(cc.getId());
                bol = (Boleto) sv.pesquisaCodigo(id_boleto, "Boleto");

                lista.get(i).setDocumento(bol.getBoletoComposto());
                bol.setNrCtrBoleto(lista.get(i).getNrCtrBoleto());

                sv.abrirTransacao();
                if (sv.alterarObjeto(lista.get(i)) && sv.alterarObjeto(bol)) {
                    sv.comitarTransacao();
                } else {
                    sv.desfazerTransacao();
                }

                continue;
            }

            scc = db.pesquisaServPorIdServIdTipoServIdContCobranca(lista.get(i).getServicos().getId(), lista.get(i).getTipoServico().getId(), bol.getContaCobranca().getId());
            if (scc == null) {
                //return lista; // ALTEREI PORQUE NÃO ESTAVA ATUALIZANDO A LISTA DE MOVIMENTOS A RECEBER (VESTUARIO LIMEIRA) -- EXCLUIR DEPOIS DE 01/12/2014
                continue;
            }

            Movimento mov = new Movimento();
            if (scc.getTipoServico().getId() == 4) {
                mov = new Movimento(-1,
                        null,
                        scc.getServicos().getPlano5(),
                        lista.get(i).getPessoa(),
                        lista.get(i).getServicos(),
                        null,
                        scc.getTipoServico(),
                        lista.get(i).getAcordo(),
                        lista.get(i).getValor(),
                        lista.get(i).getReferencia(),
                        null, // lista.get(i).getVencimento()
                        1,
                        true,
                        "E",
                        false,
                        lista.get(i).getPessoa(),
                        lista.get(i).getPessoa(),
                        "",
                        "",
                        null, // lista.get(i).getVencimento()
                        0,
                        0, 0, 0, 0, 0, 0, lista.get(i).getTipoDocumento(), 0, null);

                GerarMovimento.inativarUmMovimento(lista.get(i), "REIMPRESSÃO COM NOVO CEDENTE.");
                GerarMovimento.salvarUmMovimento(new Lote(), mov);
                listaAdd.add(mov);

                Historico his = new Historico();
                Historico his_pesquisa = new Historico();
                AcordoDB dba = new AcordoDBToplink();
                his_pesquisa = dba.pesquisaHistoricoMov(bol.getContaCobranca().getId(), lista.get(i).getId());

                if (his_pesquisa != null) {
                    his.setMovimento(mov);
                    his.setHistorico(his_pesquisa.getHistorico());
                    his.setComplemento(his_pesquisa.getComplemento());
                }

                sv.abrirTransacao();
                if (sv.inserirObjeto(his)) {
                    sv.comitarTransacao();
                } else {
                    sv.desfazerTransacao();
                }
            } else {
                mov = new Movimento(-1,
                        null,
                        scc.getServicos().getPlano5(),
                        lista.get(i).getPessoa(),
                        lista.get(i).getServicos(),
                        null,
                        scc.getTipoServico(),
                        lista.get(i).getAcordo(),
                        lista.get(i).getValor(),
                        lista.get(i).getReferencia(),
                        null, // lista.get(i).getVencimento()
                        1,
                        true,
                        "E",
                        false,
                        lista.get(i).getPessoa(),
                        lista.get(i).getPessoa(),
                        "",
                        "",
                        null, // lista.get(i).getVencimento()
                        0,
                        0, 0, 0, 0, 0, 0, lista.get(i).getTipoDocumento(), 0, null);

                GerarMovimento.inativarUmMovimento(lista.get(i), "REIMPRESSÃO COM NOVO CEDENTE.");

                GerarMovimento.salvarUmMovimento(new Lote(), mov);
                listaAdd.add(mov);
            }
        }
        if (listaAdd.isEmpty()) {
            return lista;
        } else {
            return listaAdd;
        }
    }

    public byte[] imprimirBoleto(List<Movimento> lista, List<Float> listaValores, List<String> listaVencimentos, boolean imprimeVerso) {
        int i = 0;
        String mensagemErroMovimento = "Movimento(s) sem mensagem: ";
        try {
            FacesContext faces = FacesContext.getCurrentInstance();
            Collection vetor = new ArrayList();
            PessoaEnderecoDB pesEndDB = new PessoaEnderecoDBToplink();
            JuridicaDB jurDB = new JuridicaDBToplink();
            String swap[] = new String[50];
            PessoaEndereco pe = null;
            MovimentoDB movDB = new MovimentoDBToplink();

            CnaeConvencaoDB cnaeConv = new CnaeConvencaoDBToplink();
            Cobranca cobranca = null;
            BigDecimal valor;
            String mensagem = "";
            MensagemCobranca mensagemCobranca = null;
            Historico historico = null;

            File file_jasper = new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/BOLETO.jasper"));
            //File file_jasper = new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/teste.jasper"));

            JasperReport jasper = (JasperReport) JRLoader.loadObject(file_jasper);

            while (i < lista.size()) {
                if (lista.get(i).getBaixa() != null && lista.get(i).getBaixa().getId() != -1) {
                    break;
                }

                Boleto boletox = movDB.pesquisaBoletos(lista.get(i).getNrCtrBoleto());
                if (boletox.getContaCobranca().getLayout().getId() == 2) {
                    swap[40] = ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/SINDICAL.jasper");
                } else {
                    swap[40] = ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/SICOB.jasper");
                }
                swap[43] = "";
                swap[42] = "";
                float vlOriginal = lista.get(i).getValor();

                // ALTERA VALOR PARA SAIR NA REPRESENTAÇÃO NUMÉRICA
                lista.get(i).setValor(new BigDecimal(listaValores.get(i)).floatValue());

                // ALTERA O VENCIMENTO PARA SAIR NA REPRESENTAÇÃO NUMÉRICA
                Movimento mov = lista.get(i);
                mov.setVencimento(listaVencimentos.get(i));

                cobranca = Cobranca.retornaCobranca(mov.getPessoa().getId(), mov.getValor(), mov.getDtVencimento(), boletox);

                if (boletox.getContaCobranca().getLayout().getId() == Cobranca.SINDICAL) {
                    swap[43] = "EXERC " + lista.get(i).getReferencia().substring(3);
                    swap[42] = "BLOQUETO DE CONTRIBUIÇÃO SINDICAL URBANA.";
                }

                try {
                    swap[0] = jurDB.pesquisaJuridicaPorPessoa(lista.get(i).getPessoa().getId()).getContabilidade().getPessoa().getNome();
                } catch (Exception e) {
                    swap[0] = "";
                }

                Convencao conv = new Convencao();
                try {
                    conv = cnaeConv.pesquisarCnaeConvencaoPorPessoa(lista.get(i).getPessoa().getId());
                    swap[1] = conv.getDescricao();
                } catch (Exception e) {
                    swap[1] = "";
                }

                // ENDEREÇO DE COBRANCA DA PESSOA -------------------------------------------------------------------------
                // NO CASO PODE SER OU NÃO O ENDEREÇO DA CONTABILIDADE ----------------------------------------------------
                try {
                    pe = pesEndDB.pesquisaEndPorPessoaTipo(lista.get(i).getPessoa().getId(), 3);
                    swap[2] = pe.getEndereco().getEnderecoSimplesToString();
                    swap[3] = pe.getNumero();
                    swap[4] = pe.getComplemento();
                    swap[5] = pe.getEndereco().getBairro().getDescricao();
                    swap[6] = pe.getEndereco().getCidade().getCidade();
                    swap[7] = pe.getEndereco().getCidade().getUf();
                    swap[8] = pe.getEndereco().getCep().substring(0, 5) + "-" + pe.getEndereco().getCep().substring(5);
                } catch (Exception e) {
                    swap[2] = "";
                    swap[3] = "";
                    swap[4] = "";
                    swap[5] = "";
                    swap[6] = "";
                    swap[7] = "";
                    swap[8] = "";
                }

                // ENDEREÇO SOMENTE DA PESSOA -------------------------------------------------------------------------
                int id_cidade_endereco = 0;
                try {
                    pe = pesEndDB.pesquisaEndPorPessoaTipo(lista.get(i).getPessoa().getId(), 2);
                    if (pe == null) {
                        pe = pesEndDB.pesquisaEndPorPessoaTipo(lista.get(i).getPessoa().getId(), 1);
                    } else {
                        //pe = pesEndDB.pesquisaEndPorPessoaTipo(lista.get(i).getPessoa().getId(), 2);
                    }
                    id_cidade_endereco = pe.getEndereco().getCidade().getId();
                    swap[9] = pe.getEndereco().getEnderecoSimplesToString();
                    swap[10] = pe.getNumero();
                    swap[11] = pe.getComplemento();
                    swap[12] = pe.getEndereco().getBairro().getDescricao();
                    swap[13] = pe.getEndereco().getCidade().getCidade();
                    swap[14] = pe.getEndereco().getCidade().getUf();
                    swap[15] = pe.getEndereco().getCep().substring(0, 5) + "-" + pe.getEndereco().getCep().substring(5);
                } catch (Exception e) {
                    swap[9] = "";
                    swap[10] = "";
                    swap[11] = "";
                    swap[12] = "";
                    swap[13] = "";
                    swap[14] = "";
                    swap[15] = "";
                }

                try {
                    swap[16] = boletox.getContaCobranca().getCedente();
                    swap[17] = "";//jurDB.pesquisaJuridicaPorPessoa(i).getPessoa().getNome();
                } catch (Exception e) {
                    swap[16] = "-1";
                    swap[17] = "";
                }

                // ESSE PEGA O ENDEREÇO COMERCIAL DO SINDICATO.
                try {
                    pe = pesEndDB.pesquisaEndPorPessoaTipo(1, 2);
                    swap[18] = pe.getEndereco().getEnderecoSimplesToString();
                    swap[19] = pe.getNumero();
                    swap[20] = pe.getComplemento();
                    swap[21] = pe.getEndereco().getBairro().getDescricao();
                    swap[22] = pe.getEndereco().getCidade().getCidade();
                    swap[23] = pe.getEndereco().getCidade().getUf();
                    swap[24] = pe.getEndereco().getCep().substring(0, 5) + "-" + pe.getEndereco().getCep().substring(5);
                    swap[30] = pe.getPessoa().getDocumento();
                } catch (Exception e) {
                    swap[18] = "";
                    swap[19] = "";
                    swap[20] = "";
                    swap[21] = "";
                    swap[22] = "";
                    swap[23] = "";
                    swap[24] = "";
                }

                try {
                    swap[26] = cobranca.representacao();
                    swap[27] = cobranca.codigoBarras();
                } catch (Exception e) {
                    swap[26] = "";
                    swap[27] = "";
                }

                // VOLTA O VALOR ORIGINAL DEPOIS DE TER ALTERADO NA REPRESENTAÇÃO NUMERICA
                lista.get(i).setValor(vlOriginal);
                // VERIFICA SE O ENDEREÇO DE COBRANCA É IGUAL AO ENDERECO COMERCIAL --------------------------------------------------------
//                if (swap[2].equals(swap[9]) &&
//                    swap[3].equals(swap[10]) &&
//                    swap[4].equals(swap[11])){
//                        swap[0] = "";
//                        swap[2] = "";
//                        swap[3] = "";
//                        swap[4] = "";
//                        swap[5] = "";
//                        swap[6] = "";
//                        swap[7] = "";
//                        swap[8] = "";
//                }

                try {
//                    swap[44] = lista.get(i).getContaCobranca().getCodigoSindical().substring(0, 3) + "." + //codigosindical
//                            lista.get(i).getContaCobranca().getCodigoSindical().substring(3, 6) + "."
//                            + lista.get(i).getContaCobranca().getCodigoSindical().substring(6, lista.get(i).getContaCobranca().getCodigoSindical().length()) + "-"
//                            + cobranca.moduloOnze(lista.get(i).getContaCobranca().getCodigoSindical());
                    FilialDB filialDB = new FilialDao();
                    String entidade = filialDB.pesquisaRegistroPorFilial(1).getTipoEntidade();
                    String sicas = boletox.getContaCobranca().getSicasSindical();
                    if (entidade.equals("S")) {
                        swap[44] = "S-" + sicas;
                    } else if (entidade.equals("C")) {
                        swap[44] = "C-" + sicas.substring(sicas.length() - 3, sicas.length());
                    } else if (entidade.equals("F")) {
                        swap[44] = "F-" + sicas.substring(sicas.length() - 3, sicas.length());
                    }
                } catch (Exception e) {
                    swap[44] = "";
                }

                valor = new BigDecimal(listaValores.get(i));
                if (valor.toString().equals("0")) {
                    valor = null;
                    //valor = new BigDecimal(0.0);
                }

                ConvencaoCidadeDB dbCon = new ConvencaoCidadeDBToplink();
                if (lista.get(i).getTipoServico().getId() != 4) {
                    mensagemCobranca = movDB.pesquisaMensagemCobranca(lista.get(i).getId());
                    mensagem = mensagemCobranca.getMensagemConvencao().getMensagemContribuinte();//mensagem
                    swap[25] = mensagemCobranca.getMensagemConvencao().getMensagemCompensacao();
                } else {
                    historico = movDB.pesquisaHistorico(lista.get(i).getId());

                    if (historico == null) {
                        mensagemErroMovimento += "Sem histórico para Acordo id_movimento " + lista.get(i).getId();
                        GenericaMensagem.error("Erro", mensagemErroMovimento);
                        //continue;
                    } else {
                        mensagem = historico.getHistorico();
                    }

                    swap[25] = movDB.pesquisaDescMensagem(lista.get(i).getTipoServico().getId(), lista.get(i).getServicos().getId(), conv.getId(), dbCon.pesquisaGrupoCidadeJuridica(conv.getId(), id_cidade_endereco).getId());
                }

                mensagemErroMovimento += " " + swap[0] + "\n "
                        + lista.get(i).getPessoa().getNome() + "\n"
                        + lista.get(i).getDocumento() + "\n";

                if ((historico == null) && (mensagemCobranca == null)) {
                    break;
                }

                String codc = cobranca.getCedenteFormatado();
                // CAIXA EXIGE QUE SE COLOQUE O AGENCIA/COD SINDICAL NA FICHA DE COMPENSACAO NO LUGAR DO AG/COD CEDENDE,
                // POREM CONCATENANDO COM O DIGITO VERIFICADOR DO COD CEDENTE EX.
                // 0242/004.136.02507-5 >>>>> FICARA : 0242/S02507-5
                
                String referencia = "Ref:. "+lista.get(i).getReferencia(), descricaoServico = "Contribuição:. " + lista.get(i).getServicos().getDescricao();
                if (boletox.getContaCobranca().getLayout().getId() == 2) {
                    //codc = swap[44] + "-" + codc.substring(codc.length() - 1, codc.length()); 17/03/2014 -- HOMOLOGAÇÃO DE ARCERBURGO EXIRGIU A RETIRADA DESDE DV
                    codc = swap[44];
                }else{
                    // SE NÃO FOR SINDICAL E FOR ACORDO NÃO MOSTRAR REFERÊNCIA 
                    if (lista.get(i).getServicos().getId() != 1 && lista.get(i).getTipoServico().getId() == 4) {
                        referencia = "";
                    }
                    
                    if (!lista.get(i).getServicos().isBoleto()){
                        descricaoServico = "";
                    }
                    
                    ConvencaoServico cservico = new ConvencaoServicoDao().pesquisaConvencaoServico(conv.getId(), dbCon.pesquisaGrupoCidadeJuridica(conv.getId(), id_cidade_endereco).getId());
                    if (cservico != null)
                        descricaoServico = cservico.getClausula() + " - " + descricaoServico;
                }
                
                vetor.add(new ParametroBoleto(
                        referencia, // ref (referencia)
                        imprimeVerso, // imprimeVerso
                        swap[0], //escritorio
                        descricaoServico, //  contribuicao (servico)
                        lista.get(i).getTipoServico().getDescricao(), // tipo
                        swap[1], //  grupo (convencao)
                        lista.get(i).getPessoa().getDocumento(), // cgc (cnpj)
                        lista.get(i).getPessoa().getNome(), //  sacado
                        valor, //  valor
                        swap[2],//endereco
                        swap[3],//numero
                        swap[4],//complemento
                        swap[5],//bairro
                        swap[6],//cidade
                        swap[7],//estado
                        swap[8],//cep
                        lista.get(i).getDocumento(),// boleto
                        swap[9],// sacado_endereco
                        swap[10],//sacado_numero
                        swap[11],//sacado_complemento
                        swap[12],//sacado_bairro
                        swap[13],//sacado_cidade
                        swap[14],//sacado_estado
                        swap[15],//sacado_cep
                        cobranca.getNossoNumeroFormatado(),//nossonum (nosso numero)
                        DataHoje.data(),// datadoc
                        listaVencimentos.get(i),// VENCIMENTO
                        //lista.get(i).getVencimento(),// VENCIMENTO
                        cobranca.codigoBanco(),// codbanco
                        boletox.getContaCobranca().getMoeda(),//moeda
                        boletox.getContaCobranca().getEspecieMoeda(),// especie_doc
                        boletox.getContaCobranca().getEspecieDoc(),//especie
                        cobranca.getAgenciaFormatada(),//cod_agencia
                        codc,//codcedente
                        boletox.getContaCobranca().getAceite(),//aceite
                        boletox.getContaCobranca().getCarteira(),//carteira
                        lista.get(i).getReferencia().substring(3),//exercicio
                        swap[16],//nomeentidade
                        swap[40],
                        //   ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/SICOB.jasper"),//LAYOUT
                        mensagem,//movDB.pesquisaMensagemCobranca(lista.get(i).getId()).getMensagemConvencao().getMensagemContribuinte(),//mensagem
                        boletox.getContaCobranca().getLocalPagamento(),//local_pag
                        swap[18],//endent
                        swap[19],//nument (numero entidade)
                        swap[20],//compent
                        swap[21],//baient
                        swap[22],//cident
                        swap[23],//estent
                        swap[24],//cepent
                        swap[30],//cgcent
                        swap[26],//REPNUM
                        swap[27],//CODBAR
                        swap[25],//mensagem_boleto
                        ((ServletContext) faces.getExternalContext().getContext()).getRealPath(boletox.getContaCobranca().getContaBanco().getBanco().getLogo().trim()),
                        ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),//logoEmpresa
                        ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Imagens/serrilha.GIF"),//serrilha
                        jurDB.pesquisaJuridicaPorPessoa(lista.get(i).getPessoa().getId()).getCnae().getNumero().substring(0, 3),//cnae
                        boletox.getContaCobranca().getCategoriaSindical(),//categoria
                        swap[44], //codigosindical
                        swap[43], //usoBanco
                        swap[42], //textoTitulo
                        ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/BOLETO_VERSO.jasper"),//caminhoVerso
                        boletox.getContaCobranca().getContaBanco().getFilial().getFilial().getPessoa().getNome(),
                        descricaoServico
                ));
                i++;
            }

            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(vetor);
            JasperPrint print = JasperFillManager.fillReport(
                    jasper,
                    null,
                    dtSource);
            arquivo = JasperExportManager.exportReportToPdf(print);
            pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/boletos");

        } catch (Exception e) {
            int x = i;
            System.err.println("O arquivo não foi gerado corretamente! Erro: " + e.getMessage() + " " + mensagemErroMovimento);
        }
        return arquivo;
    }

    public byte[] imprimirAcordo(List<Movimento> lista, Acordo acordo, Historico historico, boolean imprimir_pro) {
        try {
            FacesContext faces = FacesContext.getCurrentInstance();
            JasperReport jasper;
            Collection vetor = new ArrayList();
            PessoaEnderecoDB pesEndDB = new PessoaEnderecoDBToplink();
            PessoaEndereco pe = null;
            JuridicaDB jurDB = new JuridicaDBToplink();
            Juridica juridica = new Juridica();
            int i = 0;
            String swap[] = new String[35];
            Pessoa pessoa = null;
            Filial filial = null;

            if (!lista.isEmpty()) {
                SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
                ConfiguracaoArrecadacaoBean cab = new ConfiguracaoArrecadacaoBean();
                cab.init();
                filial = cab.getConfiguracaoArrecadacao().getFilial();                
                //filial = (Filial) salvarAcumuladoDB.pesquisaCodigo(1, "Filial");
                pessoa = lista.get(0).getPessoa();
            }

            try {
                juridica = jurDB.pesquisaJuridicaPorPessoa(pessoa.getId());
                swap[0] = juridica.getPessoa().getNome();
                swap[1] = juridica.getPessoa().getDocumento();
            } catch (Exception e) {
                swap[0] = "";
                swap[1] = "";
            }

            try {
                pe = pesEndDB.pesquisaEndPorPessoaTipo(pessoa.getId(), 2);
                swap[2] = pe.getEndereco().getEnderecoSimplesToString();
                swap[3] = pe.getNumero();
                swap[4] = pe.getComplemento();
                swap[5] = pe.getEndereco().getBairro().getDescricao();
                swap[6] = pe.getEndereco().getCidade().getCidade();
                swap[7] = pe.getEndereco().getCidade().getUf();
                swap[8] = pe.getEndereco().getCep().substring(0, 5) + "-" + pe.getEndereco().getCep().substring(5);
                swap[23] = pessoa.getTelefone1();
            } catch (Exception e) {
                swap[2] = "";
                swap[3] = "";
                swap[4] = "";
                swap[5] = "";
                swap[6] = "";
                swap[7] = "";
                swap[8] = "";
                swap[23] = "";
            }

            try {
                pe = pesEndDB.pesquisaEndPorPessoaTipo(filial.getFilial().getPessoa().getId(), 3);
                swap[9] = filial.getFilial().getPessoa().getNome();
                swap[10] = filial.getFilial().getPessoa().getTipoDocumento().getDescricao();
                swap[11] = pe.getEndereco().getDescricaoEndereco().getDescricao();
                swap[12] = pe.getEndereco().getLogradouro().getDescricao();
                swap[13] = pe.getNumero();
                swap[14] = pe.getComplemento();
                swap[15] = pe.getEndereco().getBairro().getDescricao();
                swap[16] = pe.getEndereco().getCidade().getCidade();
                swap[17] = pe.getEndereco().getCidade().getUf();
                swap[18] = pe.getEndereco().getCep().substring(0, 5) + "-" + pe.getEndereco().getCep().substring(5);
                swap[19] = filial.getFilial().getPessoa().getDocumento();
                swap[20] = filial.getFilial().getPessoa().getTelefone1();
                swap[21] = filial.getFilial().getPessoa().getSite();
                swap[22] = filial.getFilial().getPessoa().getEmail1();
            } catch (Exception e) {
                swap[9] = "";
                swap[10] = "";
                swap[11] = "";
                swap[12] = "";
                swap[13] = "";
                swap[14] = "";
                swap[15] = "";
                swap[16] = "";
                swap[17] = "";
                swap[18] = "";
                swap[19] = "";
                swap[20] = "";
                swap[21] = "";
                swap[22] = "";
            }

            try {
                pe = pesEndDB.pesquisaEndPorPessoaTipo(juridica.getContabilidade().getPessoa().getId(), 3);
                swap[34] = juridica.getContabilidade().getPessoa().getNome();
                swap[24] = pe.getEndereco().getDescricaoEndereco().getDescricao();
                swap[25] = pe.getEndereco().getLogradouro().getDescricao();
                swap[26] = pe.getNumero();
                swap[27] = pe.getComplemento();
                swap[28] = pe.getEndereco().getBairro().getDescricao();
                swap[29] = pe.getEndereco().getCep().substring(0, 5) + "-" + pe.getEndereco().getCep().substring(5);
                swap[30] = pe.getEndereco().getCidade().getCidade();
                swap[31] = pe.getEndereco().getCidade().getUf();
                swap[32] = juridica.getContabilidade().getPessoa().getTelefone1();
                swap[33] = juridica.getContabilidade().getPessoa().getEmail1();
            } catch (Exception e) {
                swap[34] = "";
                swap[24] = "";
                swap[25] = "";
                swap[26] = "";
                swap[27] = "";
                swap[28] = "";
                swap[29] = "";
                swap[30] = "";
                swap[31] = "";
                swap[32] = "";
                swap[33] = "";
            }

            MovimentoDB db = new MovimentoDBToplink();

            while (i < lista.size()) {
                Boleto boleto = db.pesquisaBoletos(lista.get(i).getNrCtrBoleto());

                BigDecimal valor = new BigDecimal(0), multa = new BigDecimal(0), juros = new BigDecimal(0), correcao = new BigDecimal(0), desconto = new BigDecimal(0);

                vetor.add(new DemonstrativoAcordo(
                        acordo.getId(), // codacordo
                        acordo.getData(), // data
                        acordo.getContato(), // contato
                        swap[0], // razao
                        swap[1], // cnpj
                        swap[2], //endereco
                        swap[3], // numero
                        swap[4], // complemento
                        swap[5], // bairro
                        swap[6], // cidade
                        swap[8], // cep
                        swap[7], // uf
                        swap[23], // telefone
                        historico.getHistorico(), // obs
                        lista.get(i).getServicos().getDescricao(), // desc_contribuicao
                        lista.get(i).getDocumento(), // boleto
                        lista.get(i).getVencimento(), // vencto
                        new BigDecimal(lista.get(i).getValor()), // vlrpagar
                        ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"), // sinLogo
                        swap[9], // sinNome
                        swap[11], // sinEndereco
                        swap[12], // sinLogradouro
                        swap[13], // sinNumero
                        swap[14], // sinComplemento
                        swap[15], // sinBairro
                        swap[18], // sinCep
                        swap[16], // sinCidade
                        swap[17], // sinUF
                        swap[20], // sinTelefone
                        swap[22], // sinEmail
                        swap[21], // sinSite
                        swap[10], // sinTipoDocumento
                        swap[19], // sinDocumento
                        swap[34], // escNome
                        swap[24], // escEndereco
                        swap[25], // escLogradouro
                        swap[26], // escNumero
                        swap[27], // escComplemento
                        swap[28], // escBairro
                        swap[29], // escCep
                        swap[30], // escCidade
                        swap[31], // escUF
                        swap[32], // escTelefone
                        swap[33], // escEmail
                        valor,
                        multa,
                        juros,
                        correcao,
                        desconto,
                        lista.get(i).getTipoServico().getDescricao(),
                        lista.get(i).getReferencia(),
                        "Planilha de Débito Referente ao Acordo Número " + acordo.getId(),
                        acordo.getUsuario().getPessoa().getNome(),
                        acordo.getEmail()
                ));
                i++;
            }

            List ljasper = new ArrayList();
            //* JASPER 1 *//
            jasper = (JasperReport) JRLoader.loadObject(
                    new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/DEMOSTRATIVO_ACORDO.jasper"))
            );
            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(vetor);
            ljasper.add(JasperFillManager.fillReport(jasper, null, dtSource));
            //* ------------- *//

            JRPdfExporter exporter = new JRPdfExporter();
            ByteArrayOutputStream retorno = new ByteArrayOutputStream();

            exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, ljasper);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, retorno);
            exporter.setParameter(JRPdfExporterParameter.IS_CREATING_BATCH_MODE_BOOKMARKS, Boolean.TRUE);
            exporter.exportReport();

            arquivo = retorno.toByteArray();

            pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/boletos");
        } catch (Exception erro) {
            System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
        }
        return arquivo;
    }

    public byte[] imprimirPromissoria(List<Movimento> lista, boolean imprimirVerso) {
        try {
            FacesContext faces = FacesContext.getCurrentInstance();
            JasperReport jasper;
            Collection vetor = new ArrayList();
            PessoaEnderecoDB pesEndDB = new PessoaEnderecoDBToplink();
            PessoaEndereco pe = null;
            JuridicaDB jurDB = new JuridicaDBToplink();
            Juridica juridica = new Juridica();
            int i = 0;
            String swap[] = new String[35];
            Pessoa pessoa = null;
            Filial filial = null;

            if (!lista.isEmpty()) {
                SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
                ConfiguracaoArrecadacaoBean cab = new ConfiguracaoArrecadacaoBean();
                cab.init();
                filial = cab.getConfiguracaoArrecadacao().getFilial();                
                //filial = (Filial) salvarAcumuladoDB.pesquisaCodigo(1, "Filial");
                pessoa = lista.get(0).getPessoa();
            }

            try {
                juridica = jurDB.pesquisaJuridicaPorPessoa(pessoa.getId());
                swap[0] = juridica.getPessoa().getNome();
                swap[1] = juridica.getPessoa().getDocumento();
            } catch (Exception e) {
                swap[0] = "";
                swap[1] = "";
            }

            try {
                pe = pesEndDB.pesquisaEndPorPessoaTipo(pessoa.getId(), 2);
                swap[2] = pe.getEndereco().getEnderecoSimplesToString();
                swap[3] = pe.getNumero();
                swap[4] = pe.getComplemento();
                swap[5] = pe.getEndereco().getBairro().getDescricao();
                swap[6] = pe.getEndereco().getCidade().getCidade();
                swap[7] = pe.getEndereco().getCidade().getUf();
                swap[8] = pe.getEndereco().getCep().substring(0, 5) + "-" + pe.getEndereco().getCep().substring(5);
                swap[23] = pessoa.getTelefone1();
            } catch (Exception e) {
                swap[2] = "";
                swap[3] = "";
                swap[4] = "";
                swap[5] = "";
                swap[6] = "";
                swap[7] = "";
                swap[8] = "";
                swap[23] = "";
            }

            try {
                pe = pesEndDB.pesquisaEndPorPessoaTipo(filial.getFilial().getPessoa().getId(), 3);
                swap[9] = filial.getFilial().getPessoa().getNome();
                swap[10] = filial.getFilial().getPessoa().getTipoDocumento().getDescricao();
                swap[11] = pe.getEndereco().getDescricaoEndereco().getDescricao();
                swap[12] = pe.getEndereco().getLogradouro().getDescricao();
                swap[13] = pe.getNumero();
                swap[14] = pe.getComplemento();
                swap[15] = pe.getEndereco().getBairro().getDescricao();
                swap[16] = pe.getEndereco().getCidade().getCidade();
                swap[17] = pe.getEndereco().getCidade().getUf();
                swap[18] = pe.getEndereco().getCep().substring(0, 5) + "-" + pe.getEndereco().getCep().substring(5);
                swap[19] = filial.getFilial().getPessoa().getDocumento();
                swap[20] = filial.getFilial().getPessoa().getTelefone1();
                swap[21] = filial.getFilial().getPessoa().getSite();
                swap[22] = filial.getFilial().getPessoa().getEmail1();
            } catch (Exception e) {
                swap[9] = "";
                swap[10] = "";
                swap[11] = "";
                swap[12] = "";
                swap[13] = "";
                swap[14] = "";
                swap[15] = "";
                swap[16] = "";
                swap[17] = "";
                swap[18] = "";
                swap[19] = "";
                swap[20] = "";
                swap[21] = "";
                swap[22] = "";
            }

            try {
                pe = pesEndDB.pesquisaEndPorPessoaTipo(juridica.getContabilidade().getPessoa().getId(), 3);
                swap[34] = juridica.getContabilidade().getPessoa().getNome();
                swap[24] = pe.getEndereco().getDescricaoEndereco().getDescricao();
                swap[25] = pe.getEndereco().getLogradouro().getDescricao();
                swap[26] = pe.getNumero();
                swap[27] = pe.getComplemento();
                swap[28] = pe.getEndereco().getBairro().getDescricao();
                swap[29] = pe.getEndereco().getCep().substring(0, 5) + "-" + pe.getEndereco().getCep().substring(5);
                swap[30] = pe.getEndereco().getCidade().getCidade();
                swap[31] = pe.getEndereco().getCidade().getUf();
                swap[32] = juridica.getContabilidade().getPessoa().getTelefone1();
                swap[33] = juridica.getContabilidade().getPessoa().getEmail1();
            } catch (Exception e) {
                swap[34] = "";
                swap[24] = "";
                swap[25] = "";
                swap[26] = "";
                swap[27] = "";
                swap[28] = "";
                swap[29] = "";
                swap[30] = "";
                swap[31] = "";
                swap[32] = "";
                swap[33] = "";
            }

            MovimentoDB db = new MovimentoDBToplink();

            while (i < lista.size()) {
                ValorExtenso ve = new ValorExtenso(new BigDecimal(lista.get(i).getValor()));
                vetor.add(new Promissoria(
                        "(" + lista.get(i).getAcordo().getId() + ") " + (i + 1) + "/" + lista.size(), // numero
                        ve.toString(), // extenso
                        new BigDecimal(lista.get(i).getValor()), // valor
                        swap[0], // razao
                        juridica.getPessoa().getTipoDocumento().getId() == 4 ? "" : juridica.getPessoa().getTipoDocumento().getDescricao(), // tipodocumento 
                        juridica.getPessoa().getTipoDocumento().getId() == 4 ? "" : juridica.getPessoa().getDocumento(), // documento
                        swap[2], // endereco
                        swap[4], // complemento
                        swap[3], // numero
                        swap[5], // bairro
                        swap[6], // cidade
                        swap[8], // cep
                        swap[7], // uf
                        swap[9], // sinnome
                        swap[19], // sinDocumento
                        swap[16], // sinCidade
                        swap[17], // sinUF
                        lista.get(i).getVencimento(),//DataHoje.DataToArray(lista.get(i).getVencimento())[2]+"-"+DataHoje.DataToArray(lista.get(i).getVencimento())[1]+"-"+DataHoje.DataToArray(lista.get(i).getVencimento())[0], // vencto
                        ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Imagens/promissoria.jpg"),
                        DataHoje.dataExtenso(DataHoje.data())) // fundo_promissoria
                );
                i++;
            }

            List ljasper = new ArrayList();
            //* JASPER 1 *//
            jasper = (JasperReport) JRLoader.loadObject(
                    new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/PROMISSORIA.jasper"))
            );
            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(vetor);
            ljasper.add(JasperFillManager.fillReport(jasper, null, dtSource));
            //* ------------- *//

            JRPdfExporter exporter = new JRPdfExporter();
            ByteArrayOutputStream retorno = new ByteArrayOutputStream();

            exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, ljasper);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, retorno);
            exporter.setParameter(JRPdfExporterParameter.IS_CREATING_BATCH_MODE_BOOKMARKS, Boolean.TRUE);
            exporter.exportReport();

            arquivo = retorno.toByteArray();

            pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/boletos");
        } catch (Exception erro) {
            System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
        }
        return arquivo;
    }

    public byte[] imprimirPlanilha(List<Movimento> lista, List<Float> listaValores, boolean calculo, boolean imprimirVerso) {
        try {
            FacesContext faces = FacesContext.getCurrentInstance();
            JasperReport jasper;
            Collection vetor = new ArrayList();
            PessoaEnderecoDB pesEndDB = new PessoaEnderecoDBToplink();
            PessoaEndereco pe = null;
            JuridicaDB jurDB = new JuridicaDBToplink();
            Juridica juridica = new Juridica();
            int i = 0;
            String swap[] = new String[35];
            Pessoa pessoa = null;
            Filial filial = null;

            if (!lista.isEmpty()) {
                SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
                ConfiguracaoArrecadacaoBean cab = new ConfiguracaoArrecadacaoBean();
                cab.init();
                filial = cab.getConfiguracaoArrecadacao().getFilial();                
                //filial = (Filial) salvarAcumuladoDB.pesquisaCodigo(1, "Filial");
                pessoa = lista.get(0).getPessoa();
            }

            try {
                juridica = jurDB.pesquisaJuridicaPorPessoa(pessoa.getId());
                swap[0] = juridica.getPessoa().getNome();
                swap[1] = juridica.getPessoa().getDocumento();
            } catch (Exception e) {
                swap[0] = "";
                swap[1] = "";
            }

            try {
                pe = pesEndDB.pesquisaEndPorPessoaTipo(pessoa.getId(), 2);
                swap[2] = pe.getEndereco().getEnderecoSimplesToString();
                swap[3] = pe.getNumero();
                swap[4] = pe.getComplemento();
                swap[5] = pe.getEndereco().getBairro().getDescricao();
                swap[6] = pe.getEndereco().getCidade().getCidade();
                swap[7] = pe.getEndereco().getCidade().getUf();
                swap[8] = pe.getEndereco().getCep().substring(0, 5) + "-" + pe.getEndereco().getCep().substring(5);
                swap[23] = pessoa.getTelefone1();
            } catch (Exception e) {
                swap[2] = "";
                swap[3] = "";
                swap[4] = "";
                swap[5] = "";
                swap[6] = "";
                swap[7] = "";
                swap[8] = "";
                swap[23] = "";
            }

            try {
                pe = pesEndDB.pesquisaEndPorPessoaTipo(filial.getFilial().getPessoa().getId(), 3);
                swap[9] = filial.getFilial().getPessoa().getNome();
                swap[10] = filial.getFilial().getPessoa().getTipoDocumento().getDescricao();
                swap[11] = pe.getEndereco().getDescricaoEndereco().getDescricao();
                swap[12] = pe.getEndereco().getLogradouro().getDescricao();
                swap[13] = pe.getNumero();
                swap[14] = pe.getComplemento();
                swap[15] = pe.getEndereco().getBairro().getDescricao();
                swap[16] = pe.getEndereco().getCidade().getCidade();
                swap[17] = pe.getEndereco().getCidade().getUf();
                swap[18] = pe.getEndereco().getCep().substring(0, 5) + "-" + pe.getEndereco().getCep().substring(5);
                swap[19] = filial.getFilial().getPessoa().getDocumento();
                swap[20] = filial.getFilial().getPessoa().getTelefone1();
                swap[21] = filial.getFilial().getPessoa().getSite();
                swap[22] = filial.getFilial().getPessoa().getEmail1();
            } catch (Exception e) {
                swap[9] = "";
                swap[10] = "";
                swap[11] = "";
                swap[12] = "";
                swap[13] = "";
                swap[14] = "";
                swap[15] = "";
                swap[16] = "";
                swap[17] = "";
                swap[18] = "";
                swap[19] = "";
                swap[20] = "";
                swap[21] = "";
                swap[22] = "";
            }

            try {
                pe = pesEndDB.pesquisaEndPorPessoaTipo(juridica.getContabilidade().getPessoa().getId(), 3);
                swap[34] = juridica.getContabilidade().getPessoa().getNome();
                swap[24] = pe.getEndereco().getDescricaoEndereco().getDescricao();
                swap[25] = pe.getEndereco().getLogradouro().getDescricao();
                swap[26] = pe.getNumero();
                swap[27] = pe.getComplemento();
                swap[28] = pe.getEndereco().getBairro().getDescricao();
                swap[29] = pe.getEndereco().getCep().substring(0, 5) + "-" + pe.getEndereco().getCep().substring(5);
                swap[30] = pe.getEndereco().getCidade().getCidade();
                swap[31] = pe.getEndereco().getCidade().getUf();
                swap[32] = juridica.getContabilidade().getPessoa().getTelefone1();
                swap[33] = juridica.getContabilidade().getPessoa().getEmail1();
            } catch (Exception e) {
                swap[34] = "";
                swap[24] = "";
                swap[25] = "";
                swap[26] = "";
                swap[27] = "";
                swap[28] = "";
                swap[29] = "";
                swap[30] = "";
                swap[31] = "";
                swap[32] = "";
                swap[33] = "";
            }

            MovimentoDB db = new MovimentoDBToplink();

            while (i < lista.size()) {
                BigDecimal valor, multa, juros, correcao, desconto;
                List<Vector> lAcres = new Vector();
                MovimentoDB dbm = new MovimentoDBToplink();

                if (calculo) {
                    lAcres = dbm.pesquisaAcrescimo(lista.get(i).getId());
                    if (lAcres.isEmpty()) {
                        valor = new BigDecimal(0);
                        multa = new BigDecimal(0);
                        juros = new BigDecimal(0);
                        correcao = new BigDecimal(0);
                        desconto = new BigDecimal(0);
                    } else {
                        valor = new BigDecimal(((Double) lAcres.get(0).get(0)).floatValue());
                        multa = new BigDecimal(((Double) lAcres.get(0).get(1)).floatValue());
                        juros = new BigDecimal(((Double) lAcres.get(0).get(2)).floatValue());
                        correcao = new BigDecimal(((Double) lAcres.get(0).get(3)).floatValue());
                        desconto = new BigDecimal(((Double) lAcres.get(0).get(4)).floatValue());
                    }
                } else {
                    valor = new BigDecimal(lista.get(i).getValorBaixa());
                    multa = new BigDecimal(lista.get(i).getMulta());
                    juros = new BigDecimal(lista.get(i).getJuros());
                    correcao = new BigDecimal(lista.get(i).getCorrecao());
                    desconto = new BigDecimal(lista.get(i).getDesconto());
                }

                vetor.add(new DemonstrativoAcordo(
                        //acordo.getId(), // codacordo
                        //acordo.getData(), // data
                        //acordo.getContato(), // contato
                        0, // codacordo
                        lista.get(i).getVencimento(), // data
                        "", // contato
                        swap[0], // razao
                        swap[1], // cnpj
                        swap[2], //endereco
                        swap[3], // numero
                        swap[4], // complemento
                        swap[5], // bairro
                        swap[6], // cidade
                        swap[8], // cep
                        swap[7], // uf
                        swap[23], // telefone
                        //                        historico.getHistorico(), // obs
                        "", // obs
                        lista.get(i).getServicos().getDescricao(), // desc_contribuicao
                        lista.get(i).getDocumento(), // boleto
                        lista.get(i).getVencimento(), // vencto
                        valor, // vlrpagar
                        ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"), // sinLogo
                        swap[9], // sinNome
                        swap[11], // sinEndereco
                        swap[12], // sinLogradouro
                        swap[13], // sinNumero
                        swap[14], // sinComplemento
                        swap[15], // sinBairro
                        swap[18], // sinCep
                        swap[16], // sinCidade
                        swap[17], // sinUF
                        swap[20], // sinTelefone
                        swap[22], // sinEmail
                        swap[21], // sinSite
                        swap[10], // sinTipoDocumento
                        swap[19], // sinDocumento
                        swap[34], // escNome
                        swap[24], // escEndereco
                        swap[25], // escLogradouro
                        swap[26], // escNumero
                        swap[27], // escComplemento
                        swap[28], // escBairro
                        swap[29], // escCep
                        swap[30], // escCidade
                        swap[31], // escUF
                        swap[32], // escTelefone
                        swap[33], // escEmail
                        //new BigDecimal(lista.get(i).getValor()),
                        new BigDecimal(listaValores.get(i)),
                        multa,
                        juros,
                        correcao,
                        desconto,
                        lista.get(i).getTipoServico().getDescricao(),
                        lista.get(i).getReferencia(),
                        "Planilha de Débito",
                        "",
                        ""));
                i++;
            }

            List ljasper = new ArrayList();
            //* JASPER 1 *//
            jasper = (JasperReport) JRLoader.loadObject(
                    new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/PLANILHA_DE_DEBITO.jasper"))
            );
            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(vetor);
            ljasper.add(JasperFillManager.fillReport(jasper, null, dtSource));
            //* ------------- *//

            JRPdfExporter exporter = new JRPdfExporter();
            ByteArrayOutputStream retorno = new ByteArrayOutputStream();

            exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, ljasper);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, retorno);
            exporter.setParameter(JRPdfExporterParameter.IS_CREATING_BATCH_MODE_BOOKMARKS, Boolean.TRUE);
            exporter.exportReport();

            arquivo = retorno.toByteArray();

            pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/boletos");
        } catch (Exception erro) {
            System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
        }
        return arquivo;
    }

    public byte[] imprimirAcordoAcordado(List<Movimento> lista, Acordo acordo, String historico, boolean imprimirVerso) {
        try {
            FacesContext faces = FacesContext.getCurrentInstance();
            JasperReport jasper;
            Collection vetor1 = new ArrayList(), vetor2 = new ArrayList();
            PessoaEnderecoDB pesEndDB = new PessoaEnderecoDBToplink();
            PessoaEndereco pe = null;
            JuridicaDB jurDB = new JuridicaDBToplink();
            Juridica juridica = new Juridica();
            int i = 0;
            String swap[] = new String[35];
            Pessoa pessoa = null;
            Filial filial = null;

            if (!lista.isEmpty()) {
                SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
                
                ConfiguracaoArrecadacaoBean cab = new ConfiguracaoArrecadacaoBean();
                cab.init();
                filial = cab.getConfiguracaoArrecadacao().getFilial();
                //filial = (Filial) salvarAcumuladoDB.pesquisaCodigo(1, "Filial");
                pessoa = lista.get(0).getPessoa();
            }

            try {
                juridica = jurDB.pesquisaJuridicaPorPessoa(pessoa.getId());
                swap[0] = juridica.getPessoa().getNome();
                swap[1] = juridica.getPessoa().getDocumento();
            } catch (Exception e) {
                swap[0] = "";
                swap[1] = "";
            }

            try {
                pe = pesEndDB.pesquisaEndPorPessoaTipo(pessoa.getId(), 2);
                swap[2] = pe.getEndereco().getEnderecoSimplesToString();
                swap[3] = pe.getNumero();
                swap[4] = pe.getComplemento();
                swap[5] = pe.getEndereco().getBairro().getDescricao();
                swap[6] = pe.getEndereco().getCidade().getCidade();
                swap[7] = pe.getEndereco().getCidade().getUf();
                swap[8] = pe.getEndereco().getCep().substring(0, 5) + "-" + pe.getEndereco().getCep().substring(5);
                swap[23] = pessoa.getTelefone1();
            } catch (Exception e) {
                swap[2] = "";
                swap[3] = "";
                swap[4] = "";
                swap[5] = "";
                swap[6] = "";
                swap[7] = "";
                swap[8] = "";
                swap[23] = "";
            }

            try {
                pe = pesEndDB.pesquisaEndPorPessoaTipo(filial.getFilial().getPessoa().getId(), 3);
                swap[9] = filial.getFilial().getPessoa().getNome();
                swap[10] = filial.getFilial().getPessoa().getTipoDocumento().getDescricao();
                swap[11] = pe.getEndereco().getDescricaoEndereco().getDescricao();
                swap[12] = pe.getEndereco().getLogradouro().getDescricao();
                swap[13] = pe.getNumero();
                swap[14] = pe.getComplemento();
                swap[15] = pe.getEndereco().getBairro().getDescricao();
                swap[16] = pe.getEndereco().getCidade().getCidade();
                swap[17] = pe.getEndereco().getCidade().getUf();
                swap[18] = pe.getEndereco().getCep().substring(0, 5) + "-" + pe.getEndereco().getCep().substring(5);
                swap[19] = filial.getFilial().getPessoa().getDocumento();
                swap[20] = filial.getFilial().getPessoa().getTelefone1();
                swap[21] = filial.getFilial().getPessoa().getSite();
                swap[22] = filial.getFilial().getPessoa().getEmail1();
            } catch (Exception e) {
                swap[9] = "";
                swap[10] = "";
                swap[11] = "";
                swap[12] = "";
                swap[13] = "";
                swap[14] = "";
                swap[15] = "";
                swap[16] = "";
                swap[17] = "";
                swap[18] = "";
                swap[19] = "";
                swap[20] = "";
                swap[21] = "";
                swap[22] = "";
            }

            try {
                pe = pesEndDB.pesquisaEndPorPessoaTipo(juridica.getContabilidade().getPessoa().getId(), 3);
                swap[34] = juridica.getContabilidade().getPessoa().getNome();
                swap[24] = pe.getEndereco().getDescricaoEndereco().getDescricao();
                swap[25] = pe.getEndereco().getLogradouro().getDescricao();
                swap[26] = pe.getNumero();
                swap[27] = pe.getComplemento();
                swap[28] = pe.getEndereco().getBairro().getDescricao();
                swap[29] = pe.getEndereco().getCep().substring(0, 5) + "-" + pe.getEndereco().getCep().substring(5);
                swap[30] = pe.getEndereco().getCidade().getCidade();
                swap[31] = pe.getEndereco().getCidade().getUf();
                swap[32] = juridica.getContabilidade().getPessoa().getTelefone1();
                swap[33] = juridica.getContabilidade().getPessoa().getEmail1();
            } catch (Exception e) {
                swap[34] = "";
                swap[24] = "";
                swap[25] = "";
                swap[26] = "";
                swap[27] = "";
                swap[28] = "";
                swap[29] = "";
                swap[30] = "";
                swap[31] = "";
                swap[32] = "";
                swap[33] = "";
            }

            MovimentoDB db = new MovimentoDBToplink();

            List<Vector> lAcres = new Vector();

            while (i < lista.size()) {
                Boleto boleto = db.pesquisaBoletos(lista.get(i).getNrCtrBoleto());

                lAcres = db.pesquisaAcrescimo(lista.get(i).getId());
                BigDecimal valor, multa, juros, correcao, desconto;

                if (lAcres.isEmpty()) {
                    valor = new BigDecimal(0);
                    multa = new BigDecimal(0);
                    juros = new BigDecimal(0);
                    correcao = new BigDecimal(0);
                    desconto = new BigDecimal(0);
                } else {
                    valor = new BigDecimal(((Double) lAcres.get(0).get(0)).floatValue());
                    multa = new BigDecimal(((Double) lAcres.get(0).get(1)).floatValue());
                    juros = new BigDecimal(((Double) lAcres.get(0).get(2)).floatValue());
                    correcao = new BigDecimal(((Double) lAcres.get(0).get(3)).floatValue());
                    desconto = new BigDecimal(((Double) lAcres.get(0).get(4)).floatValue());

//                    valor = new BigDecimal(Moeda.subtracaoValores(
//                                    Moeda.somaValores(Moeda.somaValores(multa.floatValue(), juros.floatValue()), correcao.floatValue()), desconto.floatValue()
//                                )
//                    );
                }

                BigDecimal valor_calculado = new BigDecimal(Moeda.somaValores(lista.get(i).getValor(), Moeda.subtracaoValores(
                        Moeda.somaValores(Moeda.somaValores(multa.floatValue(), juros.floatValue()), correcao.floatValue()), desconto.floatValue())));

                if (lista.get(i).getTipoServico().getId() == 4 && lista.get(i).isAtivo()) {
                    vetor1.add(new DemonstrativoAcordo(
                            acordo.getId(), // codacordo
                            acordo.getData(), // data
                            acordo.getContato(), // contato
                            swap[0], // razao
                            swap[1], // cnpj
                            swap[2], //endereco
                            swap[3], // numero
                            swap[4], // complemento
                            swap[5], // bairro
                            swap[6], // cidade
                            swap[8], // cep
                            swap[7], // uf
                            swap[23], // telefone
                            historico, // obs
                            lista.get(i).getServicos().getDescricao(), // desc_contribuicao
                            lista.get(i).getDocumento(), // boleto
                            lista.get(i).getVencimento(), // vencto
                            //new BigDecimal(lista.get(i).getValor()), // vlrpagar
                            valor_calculado, // vlrpagar
                            ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"), // sinLogo
                            swap[9], // sinNome
                            swap[11], // sinEndereco
                            swap[12], // sinLogradouro
                            swap[13], // sinNumero
                            swap[14], // sinComplemento
                            swap[15], // sinBairro
                            swap[18], // sinCep
                            swap[16], // sinCidade
                            swap[17], // sinUF
                            swap[20], // sinTelefone
                            swap[22], // sinEmail
                            swap[21], // sinSite
                            swap[10], // sinTipoDocumento
                            swap[19], // sinDocumento
                            swap[34], // escNome
                            swap[24], // escEndereco
                            swap[25], // escLogradouro
                            swap[26], // escNumero
                            swap[27], // escComplemento
                            swap[28], // escBairro
                            swap[29], // escCep
                            swap[30], // escCidade
                            swap[31], // escUF
                            swap[32], // escTelefone
                            swap[33], // escEmail
                            valor,
                            multa,
                            juros,
                            correcao,
                            desconto,
                            lista.get(i).getTipoServico().getDescricao(),
                            lista.get(i).getReferencia(),
                            "Planilha de Débito Referente ao Acordo Número " + acordo.getId(),
                            acordo.getUsuario().getPessoa().getNome(),
                            acordo.getEmail()
                    ));
                } else if (!lista.get(i).isAtivo()) {
                    vetor2.add(new DemonstrativoAcordo(
                            acordo.getId(), // codacordo
                            acordo.getData(), // data
                            acordo.getContato(), // contato
                            swap[0], // razao
                            swap[1], // cnpj
                            swap[2], //endereco
                            swap[3], // numero
                            swap[4], // complemento
                            swap[5], // bairro
                            swap[6], // cidade
                            swap[8], // cep
                            swap[7], // uf
                            swap[23], // telefone
                            historico, // obs
                            lista.get(i).getServicos().getDescricao(), // desc_contribuicao
                            lista.get(i).getDocumento(), // boleto
                            lista.get(i).getVencimento(), // vencto
                            //new BigDecimal(lista.get(i).getValor()), // vlrpagar
                            valor_calculado, // vlrpagar
                            ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"), // sinLogo
                            swap[9], // sinNome
                            swap[11], // sinEndereco
                            swap[12], // sinLogradouro
                            swap[13], // sinNumero
                            swap[14], // sinComplemento
                            swap[15], // sinBairro
                            swap[18], // sinCep
                            swap[16], // sinCidade
                            swap[17], // sinUF
                            swap[20], // sinTelefone
                            swap[22], // sinEmail
                            swap[21], // sinSite
                            swap[10], // sinTipoDocumento
                            swap[19], // sinDocumento
                            swap[34], // escNome
                            swap[24], // escEndereco
                            swap[25], // escLogradouro
                            swap[26], // escNumero
                            swap[27], // escComplemento
                            swap[28], // escBairro
                            swap[29], // escCep
                            swap[30], // escCidade
                            swap[31], // escUF
                            swap[32], // escTelefone
                            swap[33], // escEmail
                            valor,
                            multa,
                            juros,
                            correcao,
                            desconto,
                            lista.get(i).getTipoServico().getDescricao(),
                            lista.get(i).getReferencia(),
                            "Planilha de Débito Referente ao Acordo Número " + acordo.getId(),
                            acordo.getUsuario().getPessoa().getNome(),
                            acordo.getEmail()
                    ));
                }
                i++;
            }

            List ljasper = new ArrayList();
            //* JASPER 1 *//
            jasper = (JasperReport) JRLoader.loadObject(
                    new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/DEMOSTRATIVO_ACORDO.jasper"))
            );
            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(vetor1);
            ljasper.add(JasperFillManager.fillReport(jasper, null, dtSource));

            //* JASPER 2 *//
            jasper = (JasperReport) JRLoader.loadObject(
                    new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/PLANILHA_DE_DEBITO.jasper"))
            );
            dtSource = new JRBeanCollectionDataSource(vetor2);
            ljasper.add(JasperFillManager.fillReport(jasper, null, dtSource));

            Jasper.PART_NAME = "";
            Jasper.printReports("planilha_acordo", ljasper);
            //* ------------- *//
//
//
//
//            JRPdfExporter exporter = new JRPdfExporter();
//            ByteArrayOutputStream retorno = new ByteArrayOutputStream();
//
//            exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, ljasper);
//            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, retorno);
//            exporter.setParameter(JRPdfExporterParameter.IS_CREATING_BATCH_MODE_BOOKMARKS, Boolean.TRUE);
//            exporter.exportReport();
//
//            arquivo = retorno.toByteArray();
//
//            pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/boletos");
        } catch (Exception erro) {
            System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
        }
        return arquivo;
    }

    public byte[] imprimirAcordoPromissoria(List<Movimento> lista, Acordo acordo, Historico historico, boolean imprimir_pro) {
        try {
            FacesContext faces = FacesContext.getCurrentInstance();
            JasperReport jasper;
            Collection vetor1 = new ArrayList(), vetor2 = new ArrayList(), vetor3 = new ArrayList();
            PessoaEnderecoDB pesEndDB = new PessoaEnderecoDBToplink();
            PessoaEndereco pe = null;
            JuridicaDB jurDB = new JuridicaDBToplink();
            Juridica juridica = new Juridica();
            int i = 0;
            String swap[] = new String[35];
            Pessoa pessoa = null;
            Filial filial = null;

            if (!lista.isEmpty()) {
                SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
                ConfiguracaoArrecadacaoBean cab = new ConfiguracaoArrecadacaoBean();
                cab.init();
                filial = cab.getConfiguracaoArrecadacao().getFilial();
                //filial = (Filial) salvarAcumuladoDB.pesquisaCodigo(1, "Filial");
                pessoa = lista.get(0).getPessoa();
            }

            try {
                juridica = jurDB.pesquisaJuridicaPorPessoa(pessoa.getId());
                swap[0] = juridica.getPessoa().getNome();
                swap[1] = juridica.getPessoa().getDocumento();
            } catch (Exception e) {
                swap[0] = "";
                swap[1] = "";
            }

            try {
                pe = pesEndDB.pesquisaEndPorPessoaTipo(pessoa.getId(), 2);
                swap[2] = pe.getEndereco().getEnderecoSimplesToString();
                swap[3] = pe.getNumero();
                swap[4] = pe.getComplemento();
                swap[5] = pe.getEndereco().getBairro().getDescricao();
                swap[6] = pe.getEndereco().getCidade().getCidade();
                swap[7] = pe.getEndereco().getCidade().getUf();
                swap[8] = pe.getEndereco().getCep().substring(0, 5) + "-" + pe.getEndereco().getCep().substring(5);
                swap[23] = pessoa.getTelefone1();
            } catch (Exception e) {
                swap[2] = "";
                swap[3] = "";
                swap[4] = "";
                swap[5] = "";
                swap[6] = "";
                swap[7] = "";
                swap[8] = "";
                swap[23] = "";
            }

            try {
                pe = pesEndDB.pesquisaEndPorPessoaTipo(filial.getFilial().getPessoa().getId(), 3);
                swap[9] = filial.getFilial().getPessoa().getNome();
                swap[10] = filial.getFilial().getPessoa().getTipoDocumento().getDescricao();
                swap[11] = pe.getEndereco().getDescricaoEndereco().getDescricao();
                swap[12] = pe.getEndereco().getLogradouro().getDescricao();
                swap[13] = pe.getNumero();
                swap[14] = pe.getComplemento();
                swap[15] = pe.getEndereco().getBairro().getDescricao();
                swap[16] = pe.getEndereco().getCidade().getCidade();
                swap[17] = pe.getEndereco().getCidade().getUf();
                swap[18] = pe.getEndereco().getCep().substring(0, 5) + "-" + pe.getEndereco().getCep().substring(5);
                swap[19] = filial.getFilial().getPessoa().getDocumento();
                swap[20] = filial.getFilial().getPessoa().getTelefone1();
                swap[21] = filial.getFilial().getPessoa().getSite();
                swap[22] = filial.getFilial().getPessoa().getEmail1();
            } catch (Exception e) {
                swap[9] = "";
                swap[10] = "";
                swap[11] = "";
                swap[12] = "";
                swap[13] = "";
                swap[14] = "";
                swap[15] = "";
                swap[16] = "";
                swap[17] = "";
                swap[18] = "";
                swap[19] = "";
                swap[20] = "";
                swap[21] = "";
                swap[22] = "";
            }

            try {
                pe = pesEndDB.pesquisaEndPorPessoaTipo(juridica.getContabilidade().getPessoa().getId(), 3);
                swap[34] = juridica.getContabilidade().getPessoa().getNome();
                swap[24] = pe.getEndereco().getDescricaoEndereco().getDescricao();
                swap[25] = pe.getEndereco().getLogradouro().getDescricao();
                swap[26] = pe.getNumero();
                swap[27] = pe.getComplemento();
                swap[28] = pe.getEndereco().getBairro().getDescricao();
                swap[29] = pe.getEndereco().getCep().substring(0, 5) + "-" + pe.getEndereco().getCep().substring(5);
                swap[30] = pe.getEndereco().getCidade().getCidade();
                swap[31] = pe.getEndereco().getCidade().getUf();
                swap[32] = juridica.getContabilidade().getPessoa().getTelefone1();
                swap[33] = juridica.getContabilidade().getPessoa().getEmail1();
            } catch (Exception e) {
                swap[34] = "";
                swap[24] = "";
                swap[25] = "";
                swap[26] = "";
                swap[27] = "";
                swap[28] = "";
                swap[29] = "";
                swap[30] = "";
                swap[31] = "";
                swap[32] = "";
                swap[33] = "";
            }

            MovimentoDB dbm = new MovimentoDBToplink();
            int qnt = dbm.pesquisaAcordoAberto(acordo.getId()).size();
            while (i < lista.size()) {
                List<Vector> lAcres = new Vector();

                lAcres = dbm.pesquisaAcrescimo(lista.get(i).getId());
                BigDecimal valor, multa, juros, correcao, desconto;

                if (lAcres.isEmpty()) {
                    valor = new BigDecimal(0);
                    multa = new BigDecimal(0);
                    juros = new BigDecimal(0);
                    correcao = new BigDecimal(0);
                    desconto = new BigDecimal(0);
                } else {
                    valor = new BigDecimal(((Double) lAcres.get(0).get(0)).floatValue());
                    multa = new BigDecimal(((Double) lAcres.get(0).get(1)).floatValue());
                    juros = new BigDecimal(((Double) lAcres.get(0).get(2)).floatValue());
                    correcao = new BigDecimal(((Double) lAcres.get(0).get(3)).floatValue());
                    desconto = new BigDecimal(((Double) lAcres.get(0).get(4)).floatValue());
                }

                BigDecimal valor_calculado = new BigDecimal(Moeda.somaValores(lista.get(i).getValor(), Moeda.subtracaoValores(
                        Moeda.somaValores(Moeda.somaValores(multa.floatValue(), juros.floatValue()), correcao.floatValue()), desconto.floatValue())));

                if (lista.get(i).getTipoServico().getId() == 4) {
                    vetor1.add(new DemonstrativoAcordo(
                            acordo.getId(), // codacordo
                            acordo.getData(), // data
                            acordo.getContato(), // contato
                            swap[0], // razao
                            swap[1], // cnpj
                            swap[2], //endereco
                            swap[3], // numero
                            swap[4], // complemento
                            swap[5], // bairro
                            swap[6], // cidade
                            swap[8], // cep
                            swap[7], // uf
                            swap[23], // telefone
                            historico.getHistorico(), // obs
                            lista.get(i).getServicos().getDescricao(), // desc_contribuicao
                            lista.get(i).getDocumento(), // boleto
                            lista.get(i).getVencimento(), // vencto
                            valor_calculado, // vlrpagar
                            ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"), // sinLogo
                            swap[9], // sinNome
                            swap[11], // sinEndereco
                            swap[12], // sinLogradouro
                            swap[13], // sinNumero
                            swap[14], // sinComplemento
                            swap[15], // sinBairro
                            swap[18], // sinCep
                            swap[16], // sinCidade
                            swap[17], // sinUF
                            swap[20], // sinTelefone
                            swap[22], // sinEmail
                            swap[21], // sinSite
                            swap[10], // sinTipoDocumento
                            swap[19], // sinDocumento
                            swap[34], // escNome
                            swap[24], // escEndereco
                            swap[25], // escLogradouro
                            swap[26], // escNumero
                            swap[27], // escComplemento
                            swap[28], // escBairro
                            swap[29], // escCep
                            swap[30], // escCidade
                            swap[31], // escUF
                            swap[32], // escTelefone
                            swap[33], // escEmail
                            valor,
                            multa,
                            juros,
                            correcao,
                            desconto,
                            lista.get(i).getTipoServico().getDescricao(),
                            lista.get(i).getReferencia(),
                            "Planilha de Débito Referente ao Acordo Número " + acordo.getId(),
                            acordo.getUsuario().getPessoa().getNome(),
                            acordo.getEmail()
                    ));

                    ValorExtenso ve = new ValorExtenso(new BigDecimal(lista.get(i).getValor()));
                    vetor3.add(new Promissoria(
                            "(" + lista.get(i).getAcordo().getId() + ") " + (vetor3.size() + 1) + "/" + qnt, // numero
                            ve.toString(), // extenso
                            new BigDecimal(lista.get(i).getValor()), // valor
                            swap[0], // razao
                            juridica.getPessoa().getTipoDocumento().getId() == 4 ? "" : juridica.getPessoa().getTipoDocumento().getDescricao(), // tipodocumento 
                            juridica.getPessoa().getTipoDocumento().getId() == 4 ? "" : juridica.getPessoa().getDocumento(), // documento
                            swap[2], // endereco
                            swap[4], // complemento
                            swap[3], // numero
                            swap[5], // bairro
                            swap[6], // cidade
                            swap[8], // cep
                            swap[7], // uf
                            swap[9], // sinnome
                            swap[19], // sinDocumento
                            swap[16], // sinCidade
                            swap[17], // sinUF
                            lista.get(i).getVencimento(),// DataHoje.DataToArray(lista.get(i).getVencimento())[2]+"-"+DataHoje.DataToArray(lista.get(i).getVencimento())[1]+"-"+DataHoje.DataToArray(lista.get(i).getVencimento())[0], // vencto
                            ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Imagens/promissoria.jpg"),
                            DataHoje.dataExtenso(DataHoje.data())) // fundo_promissoria
                    );
                } else {
                    vetor2.add(new DemonstrativoAcordo(
                            acordo.getId(), // codacordo
                            acordo.getData(), // data
                            acordo.getContato(), // contato
                            swap[0], // razao
                            swap[1], // cnpj
                            swap[2], //endereco
                            swap[3], // numero
                            swap[4], // complemento
                            swap[5], // bairro
                            swap[6], // cidade
                            swap[8], // cep
                            swap[7], // uf
                            swap[23], // telefone
                            historico.getHistorico(), // obs
                            lista.get(i).getServicos().getDescricao(), // desc_contribuicao
                            lista.get(i).getDocumento(), // boleto
                            lista.get(i).getVencimento(), // vencto
                            //new BigDecimal(lista.get(i).getValor()), // vlrpagar
                            valor_calculado, // vlrpagar
                            ((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"), // sinLogo
                            swap[9], // sinNome
                            swap[11], // sinEndereco
                            swap[12], // sinLogradouro
                            swap[13], // sinNumero
                            swap[14], // sinComplemento
                            swap[15], // sinBairro
                            swap[18], // sinCep
                            swap[16], // sinCidade
                            swap[17], // sinUF
                            swap[20], // sinTelefone
                            swap[22], // sinEmail
                            swap[21], // sinSite
                            swap[10], // sinTipoDocumento
                            swap[19], // sinDocumento
                            swap[34], // escNome
                            swap[24], // escEndereco
                            swap[25], // escLogradouro
                            swap[26], // escNumero
                            swap[27], // escComplemento
                            swap[28], // escBairro
                            swap[29], // escCep
                            swap[30], // escCidade
                            swap[31], // escUF
                            swap[32], // escTelefone
                            swap[33], // escEmail
                            valor,
                            multa,
                            juros,
                            correcao,
                            desconto,
                            lista.get(i).getTipoServico().getDescricao(),
                            lista.get(i).getReferencia(),
                            "Planilha de Débito Referente ao Acordo Número " + acordo.getId(),
                            acordo.getUsuario().getPessoa().getNome(),
                            acordo.getEmail()
                    ));
                }
                i++;

            }

            List ljasper = new ArrayList();
            //* JASPER 1 *//
            jasper = (JasperReport) JRLoader.loadObject(
                    new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/DEMOSTRATIVO_ACORDO.jasper"))
            );
            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(vetor1);
            ljasper.add(JasperFillManager.fillReport(jasper, null, dtSource));
            //* ------------- *//

            //* JASPER 2 *//
            jasper = (JasperReport) JRLoader.loadObject(
                    new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/PLANILHA_DE_DEBITO.jasper"))
            );
            dtSource = new JRBeanCollectionDataSource(vetor2);
            ljasper.add(JasperFillManager.fillReport(jasper, null, dtSource));
            //* ------------- *//

            if (imprimir_pro) {
                //* JASPER 3 *//
                jasper = (JasperReport) JRLoader.loadObject(
                        new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Relatorios/PROMISSORIA.jasper"))
                );
                dtSource = new JRBeanCollectionDataSource(vetor3);
                ljasper.add(JasperFillManager.fillReport(jasper, null, dtSource));
                //* ------------- *//
            }

            JRPdfExporter exporter = new JRPdfExporter();
            ByteArrayOutputStream retorno = new ByteArrayOutputStream();

            exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, ljasper);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, retorno);
            exporter.setParameter(JRPdfExporterParameter.IS_CREATING_BATCH_MODE_BOOKMARKS, Boolean.TRUE);
            exporter.exportReport();

            arquivo = retorno.toByteArray();

            pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/boletos");
        } catch (Exception erro) {
            System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
        }
        return arquivo;
    }

    public byte[] imprimirBoletoSocial(Boleto boleto, boolean imprimeVerso) {
        List<Boleto> l = new ArrayList();
        l.add(boleto);
        return imprimirBoletoSocial(l, imprimeVerso);
    }
    
    public byte[] imprimirBoletoSocial(List<Boleto> listaBoleto, boolean imprimeVerso) {
        List lista = new ArrayList();
        Filial filial = (Filial) new Dao().find(new Filial(), 1);
        FinanceiroDB db = new FinanceiroDBToplink();

        try {
            File file_jasper = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/BOLETO_SOCIAL.jasper"));
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(file_jasper);

            File file_jasper_verso = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/BOLETO_SOCIAL_VERSO.jasper"));
            JasperReport jasperReportVerso = (JasperReport) JRLoader.loadObject(file_jasper_verso);

            List<JasperPrint> jasperPrintList = new ArrayList();
            File file_promo = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/BannerPromoBoleto.png"));
            if (!file_promo.exists()) {
                file_promo = null;
            }

            File file_promo_verso = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoBoletoVersoSocial.png"));
            if (!file_promo_verso.exists()) {
                file_promo_verso = null;
            }
            
            MovimentosReceberSocialDB dbs = new MovimentosReceberSocialDBToplink();
            JuridicaDB dbj = new JuridicaDBToplink();
            FisicaDB dbf = new FisicaDBToplink();
            
            for (Boleto boleto : listaBoleto) {
                // PESSOA RESPONSÁVEL PELO BOLETO
                Pessoa pessoa = dbs.responsavelBoleto(boleto.getNrCtrBoleto());
                List<Vector> lista_socio = null;
                String contabilidade = "";
                if (dbf.pesquisaFisicaPorPessoa(pessoa.getId()) != null)
                    lista_socio = db.listaBoletoSocioFisica(boleto.getNrCtrBoleto()); // NR_CTR_BOLETO
                 else {
                    lista_socio = db.listaBoletoSocioJuridica(boleto.getNrCtrBoleto()); // NR_CTR_BOLETO
                    Juridica j = dbj.pesquisaJuridicaPorPessoa(pessoa.getId());
                    String doc = (j.getContabilidade() != null && 
                                  !j.getContabilidade().getPessoa().getDocumento().isEmpty() && 
                                  !j.getContabilidade().getPessoa().getDocumento().equals("0") ) ? j.getContabilidade().getPessoa().getDocumento() + " - " : " ";
                    
                    contabilidade = (j.getContabilidade() != null) ? "CONTABILIDADE : " + doc + j.getContabilidade().getPessoa().getNome() : "";
                }
                
                Cobranca cobranca = null;
                // SOMA VALOR DAS ATRASADAS
                float valor_total_atrasadas = 0, valor_total = 0, valor_boleto = 0;
                List<String> list_at = new ArrayList();
                for (Vector listax : lista_socio) {
                    // SE vencimento_movimento FOR MENOR QUE vencimento_boleto_original
                    if (DataHoje.menorData(DataHoje.converteData((Date) listax.get(38)), "01/" + DataHoje.converteData((Date) listax.get(40)).substring(3))) {
                        valor_total_atrasadas = Moeda.somaValores(valor_total_atrasadas, Moeda.converteUS$(listax.get(14).toString()));
                        list_at.add(DataHoje.converteData((Date) listax.get(38)));
                    } else {
                        valor_total = Moeda.somaValores(valor_total, Moeda.converteUS$(listax.get(14).toString()));
                    }
                    valor_boleto = Moeda.somaValores(valor_total, valor_total_atrasadas);
                }
                
                String mensagemAtrasadas = "Mensalidades Atrasadas Corrigidas";
                if (!list_at.isEmpty()) {
                    mensagemAtrasadas = "Mensalidades Atrasadas Corrigidas de " + list_at.get(0).substring(3) + " até " + list_at.get(list_at.size() - 1).substring(3);
                }
                if (cobranca == null) {
                    cobranca = Cobranca.retornaCobranca(null, valor_boleto, boleto.getDtVencimento(), boleto);
                }

                int qntItens = 0;
                for (int w = 0; w < lista_socio.size(); w++) {
                    if (DataHoje.maiorData(DataHoje.converteData((Date) lista_socio.get(w).get(38)), "01/"+DataHoje.converteData((Date) lista_socio.get(w).get(40)).substring(3))) {
                        qntItens++; 
                        float valor = Moeda.converteUS$(lista_socio.get(w).get(14).toString());
                        lista.add(new ParametroBoletoSocial(
                                ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"), // LOGO SINDICATO
                                filial.getFilial().getPessoa().getNome(),
                                lista_socio.get(w).get(5).toString(), // CODIGO
                                lista_socio.get(w).get(6).toString(), // RESPONSAVEL
                                boleto.getVencimento(), // VENCIMENTO
                                (lista_socio.get(w).get(8) == null) ? "" : lista_socio.get(w).get(8).toString(), // MATRICULA
                                (lista_socio.get(w).get(10) == null) ? "" : lista_socio.get(w).get(10).toString(), // CATEGORIA
                                (lista_socio.get(w).get(9) == null) ? "" : lista_socio.get(w).get(9).toString(), // GRUPO
                                lista_socio.get(w).get(12).toString(), // CODIGO BENEFICIARIO
                                lista_socio.get(w).get(13).toString(), // BENEFICIARIO
                                lista_socio.get(w).get(11).toString(), // SERVICO
                                Moeda.converteR$Float(valor), // VALOR
                                Moeda.converteR$Float(valor_total), // VALOR TOTAL
                                //Moeda.converteR$(lista_socio.get(w).get(15).toString()), // VALOR ATRASADAS
                                Moeda.converteR$Float(valor_total_atrasadas), // VALOR ATRASADAS
                                Moeda.converteR$Float(Moeda.somaValores(valor_total, valor_total_atrasadas)), // VALOR ATÉ VENCIMENTO
                                file_promo == null ? null : file_promo.getAbsolutePath(), // LOGO PROMOÇÃO
                                ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(boleto.getContaCobranca().getContaBanco().getBanco().getLogo().trim()), // LOGO BANCO
                                lista_socio.get(w).get(16).toString(), // MENSAGEM
                                lista_socio.get(w).get(18).toString(), // AGENCIA
                                cobranca.representacao(), // REPRESENTACAO
                                lista_socio.get(w).get(19).toString(), // CODIGO CEDENTE
                                lista_socio.get(w).get(20).toString(), // NOSSO NUMENTO
                                DataHoje.converteData((Date) lista_socio.get(w).get(4)), // PROCESSAMENTO
                                cobranca.codigoBarras(), // CODIGO DE BARRAS
                                ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Imagens/serrilha.GIF"), // SERRILHA
                                lista_socio.get(w).get(31).toString() + " " + lista_socio.get(w).get(32).toString(), // ENDERECO RESPONSAVEL
                                lista_socio.get(w).get(26).toString() + " " + lista_socio.get(w).get(27).toString(), // ENDERECO FILIAL
                                lista_socio.get(w).get(35).toString() + " " + lista_socio.get(w).get(34).toString() + " " + lista_socio.get(w).get(33).toString(), // COMPLEMENTO RESPONSAVEL
                                lista_socio.get(w).get(28).toString() + " - " + lista_socio.get(w).get(29).toString() + " " + lista_socio.get(w).get(30).toString(), // COMPLEMENTO FILIAL
                                lista_socio.get(w).get(24).toString(), // CNPJ FILIAL
                                lista_socio.get(w).get(25).toString(), // TELEFONE FILIAL
                                lista_socio.get(w).get(21).toString(), // EMAIL FILIAL
                                lista_socio.get(w).get(23).toString(), // SITE FILIAL
                                file_promo_verso == null ? null : file_promo_verso.getAbsolutePath(), // LOGO BOLETO VERSO SOCIAL
                                lista_socio.get(w).get(37).toString(), // LOCAL DE PAGAMENTO
                                lista_socio.get(w).get(36).toString(), // INFORMATIVO
                                pessoa.getTipoDocumento().getDescricao() + ": " + pessoa.getDocumento(),
                                //String.valueOf(lista_socio.size()), // QUANTIDADE DE ITENS PARA MOSTRAR OS ATRASADOS TAMBEḾ
                                String.valueOf(qntItens), // QUANTIDADE DE ITENS
                                boleto.getContaCobranca().getContaBanco().getBanco().getNumero(),
                                mensagemAtrasadas,
                                boleto.getVencimento().substring(3), // VENCIMENTO SERVIÇO
                                contabilidade, // CONTABILIDADE DA PESSOA JURÍDICA
                                boleto.getMensagem() // MENSAGEM QUE FICA ACIMA DE "Mensalidades Atrasadas"
                        ));
                    }
                }

                JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
                jasperPrintList.add(JasperFillManager.fillReport(jasperReport, null, dtSource));
                if (imprimeVerso) {
                    dtSource = new JRBeanCollectionDataSource(lista);
                    jasperPrintList.add(JasperFillManager.fillReport(jasperReportVerso, null, dtSource));
                }

                lista.clear();
            }

            JRPdfExporter exporter = new JRPdfExporter();
            ByteArrayOutputStream retorno = new ByteArrayOutputStream();

            exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, retorno);
            exporter.setParameter(JRPdfExporterParameter.IS_CREATING_BATCH_MODE_BOOKMARKS, Boolean.TRUE);
            exporter.exportReport();

            arquivo = retorno.toByteArray();

        } catch (JRException e) {
            e.getMessage();
        }
        return arquivo;
    }

    byte[] concat(byte[]... arrays) {
        // Determine the length of the result array
        int totalLength = 0;
        for (int i = 0; i < arrays.length; i++) {
            totalLength += arrays[i].length;
        }

        // create the result array
        byte[] result = new byte[totalLength];

        // copy the source arrays into the result array
        int currentIndex = 0;
        for (int i = 0; i < arrays.length; i++) {
            System.arraycopy(arrays[i], 0, result, currentIndex, arrays[i].length);
            currentIndex += arrays[i].length;
        }
        return result;
    }

    public void visualizar(File file) {
        if (file != null) {
            byte[] arq = new byte[(int) file.length()];
            try {
                HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                res.setContentType("application/pdf");
                res.setHeader("Content-disposition", "inline; filename=\"" + file.getName() + ".pdf\"");
                res.getOutputStream().write(arq);
                res.getCharacterEncoding();
                FacesContext.getCurrentInstance().responseComplete();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        if (arquivo.length > 0) {
            try {
                HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                res.setContentType("application/pdf");
                res.setHeader("Content-disposition", "inline; filename=\"" + "boleto_x" + ".pdf\"");
                res.getOutputStream().write(arquivo);
                res.getCharacterEncoding();
                FacesContext.getCurrentInstance().responseComplete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void baixarArquivo() {
        SalvaArquivos sa = new SalvaArquivos(arquivo, "boleto_x.pdf", false);
        sa.salvaNaPasta(pathPasta);
        Download download = new Download("boleto_x.pdf", pathPasta, "application/pdf", FacesContext.getCurrentInstance());
        download.baixar();
    }

    public String criarLink(Pessoa pessoa, String caminho) {
        String hash = String.valueOf(pessoa.getId()) + "_" + String.valueOf(DataHoje.converteDataParaInteger(DataHoje.data())) + "_" + DataHoje.horaSemPonto() + ".pdf";
        SalvaArquivos sa = new SalvaArquivos(arquivo, hash, false);
        sa.salvaNaPasta(pathPasta);

        Links links = new Links();
        links.setCaminho(caminho);
        links.setNomeArquivo(hash);
        links.setPessoa(pessoa);

        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();

        if (sv.inserirObjeto(links)) {
            sv.comitarTransacao();
            return hash;
        } else {
            sv.desfazerTransacao();
            return "";
        }
    }

    public String getPathPasta() {
        return pathPasta;
    }

    public void setPathPasta(String pathPasta) {
        this.pathPasta = pathPasta;
    }
}
