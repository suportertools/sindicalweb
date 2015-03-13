/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.rtools.movimento;

import br.com.rtools.financeiro.Baixa;
import br.com.rtools.financeiro.FormaPagamento;
import br.com.rtools.financeiro.Guia;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.db.MovimentoDB;
import br.com.rtools.financeiro.db.MovimentoDBToplink;
import br.com.rtools.impressao.ParametroRecibo;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.db.PessoaEnderecoDB;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Diretorio;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author rtools
 */
public class ImprimirRecibo {

    public String recibo(int id_movimento) {
        MovimentoDB db = new MovimentoDBToplink();
        Movimento movimento = db.pesquisaCodigo(id_movimento);
        try {
            Collection vetor = new ArrayList();
            Juridica sindicato = (Juridica) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(1, "Juridica");
            PessoaEnderecoDB dbp = new PessoaEnderecoDBToplink();
            //MovimentosReceberSocialDB dbs = new MovimentosReceberSocialDBToplink();

            PessoaEndereco pe = dbp.pesquisaEndPorPessoaTipo(1, 2);
            String formas[] = new String[10];

            // PESQUISA FORMA DE PAGAMENTO
            List<FormaPagamento> fp = db.pesquisaFormaPagamento(movimento.getBaixa().getId());

            for (int i = 0; i < fp.size(); i++) {
                // 4 - CHEQUE    
                if (fp.get(i).getTipoPagamento().getId() == 4) {
                    formas[i] = fp.get(i).getTipoPagamento().getDescricao() + ": R$ " + Moeda.converteR$Float(fp.get(i).getValor()) + " (B: " + fp.get(i).getChequeRec().getBanco() + " Ag: " + fp.get(i).getChequeRec().getAgencia() + " C: " + fp.get(i).getChequeRec().getConta() + " CH: " + fp.get(i).getChequeRec().getCheque();
                    // 5 - CHEQUE PRÉ
                } else if (fp.get(i).getTipoPagamento().getId() == 5) {
                    formas[i] = fp.get(i).getTipoPagamento().getDescricao() + ": R$ " + Moeda.converteR$Float(fp.get(i).getValor()) + " (B: " + fp.get(i).getChequeRec().getBanco() + " Ag: " + fp.get(i).getChequeRec().getAgencia() + " C: " + fp.get(i).getChequeRec().getConta() + " CH: " + fp.get(i).getChequeRec().getCheque() + " P: " + fp.get(i).getChequeRec().getVencimento() + ")";
                    // QUALQUER OUTRO    
                } else {
                    formas[i] = fp.get(i).getTipoPagamento().getDescricao() + ": R$ " + Moeda.converteR$Float(fp.get(i).getValor());
                }
            }
            String validade_servico = "";
            DataHoje dataHoje = new DataHoje();
            List<Movimento> lista = db.listaMovimentoBaixaOrder(movimento.getBaixa().getId());
            for (int i = 0; i < lista.size(); i++) {
                // tem casos de ter responsaveis diferentes, resultando em empresas conveniadas diferentes
                Guia gu = db.pesquisaGuias(lista.get(i).getLote().getId());
                String conveniada = "";
                if (gu.getId() != -1) {
                    if (gu.getPessoa() != null) {
                        conveniada = gu.getPessoa().getNome();
                    }
                }
                // ANTIGA EMPRESA CONVENIADA qry com id_sub_grupo_convenio
//                if (lista.get(i).getLote().getRotina().getId() == 132) {
//                    Guia gu = db.pesquisaGuias(lista.get(i).getLote().getId());
//                    if (gu.getId() != -1 && gu.getSubGrupoConvenio() != null) {
//                        LancamentoIndividualDB dbl = new LancamentoIndividualDBToplink();
//                        List<Juridica> list = (List<Juridica>) dbl.listaEmpresaConveniadaPorSubGrupo(gu.getSubGrupoConvenio().getId());
//                        if (!list.isEmpty()) {
//                            conveniada = list.get(0).getFantasia();
//                        }
//                    }
//                }
                if (!lista.get(i).getServicos().isValidadeGuias()) {
                    validade_servico = "";
                } else if (lista.get(i).getServicos().isValidadeGuias() && !lista.get(i).getServicos().isValidadeGuiasVigente()) {
                    validade_servico = " - Validade" + dataHoje.incrementarDias(lista.get(i).getServicos().getValidade(), lista.get(i).getLote().getEmissao());
                } else if (lista.get(i).getServicos().isValidadeGuias() && lista.get(i).getServicos().isValidadeGuiasVigente()) {
                    validade_servico = " - Validade" + DataHoje.converteData(DataHoje.lastDayOfMonth(DataHoje.dataHoje()));
                }

                vetor.add(new ParametroRecibo(
                        ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
                        sindicato.getPessoa().getNome(),
                        pe.getEndereco().getDescricaoEndereco().getDescricao(),
                        pe.getEndereco().getLogradouro().getDescricao(),
                        pe.getNumero(),
                        pe.getComplemento(),
                        pe.getEndereco().getBairro().getDescricao(),
                        pe.getEndereco().getCep().substring(0, 5) + "-" + pe.getEndereco().getCep().substring(5),
                        pe.getEndereco().getCidade().getCidade(),
                        pe.getEndereco().getCidade().getUf(),
                        sindicato.getPessoa().getTelefone1(),
                        sindicato.getPessoa().getEmail1(),
                        sindicato.getPessoa().getSite(),
                        sindicato.getPessoa().getDocumento(),
                        lista.get(i).getPessoa().getNome(), // RESPONSÁVEL
                        String.valueOf(lista.get(i).getPessoa().getId()), // ID_RESPONSAVEL
                        String.valueOf(lista.get(i).getBaixa().getId()), // ID_BAIXA
                        lista.get(i).getBeneficiario().getNome(), // BENEFICIÁRIO
                        lista.get(i).getServicos().getDescricao(), // SERVICO
                        lista.get(i).getVencimento(), // VENCIMENTO
                        new BigDecimal(lista.get(i).getValorBaixa()), // VALOR BAIXA
                        lista.get(i).getBaixa().getUsuario().getLogin(),
                        lista.get(i).getBaixa().getBaixa(),
                        DataHoje.horaMinuto(),
                        formas[0],
                        formas[1],
                        formas[2],
                        formas[3],
                        formas[4],
                        formas[5],
                        formas[6],
                        formas[7],
                        formas[8],
                        formas[9],
                        (conveniada.isEmpty()) ? "" : "Empresa Conveniada: " + conveniada,
                        validade_servico
                )
                );
            }

            File fl = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/RECIBO.jasper"));
            JasperReport jasper = (JasperReport) JRLoader.loadObject(fl);

            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(vetor);
            JasperPrint print = JasperFillManager.fillReport(jasper, null, dtSource);

            byte[] arquivo = JasperExportManager.exportReportToPdf(print);
            salvarRecibo(arquivo, lista.get(0).getBaixa());

            HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            res.setContentType("application/pdf");
            res.setHeader("Content-disposition", "inline; filename=\"" + "boleto_x" + ".pdf\"");
            res.getOutputStream().write(arquivo);
            res.getCharacterEncoding();

            FacesContext.getCurrentInstance().responseComplete();
        } catch (JRException | IOException ex) {
            ex.getMessage();
        }

        return null;
    }

    public void salvarRecibo(byte[] arquivo, Baixa baixa) {
        if (baixa.getCaixa() == null) {
            return;
        }

        String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/" + "Arquivos/recibo/" + baixa.getCaixa().getCaixa() + "/" + DataHoje.converteData(baixa.getDtBaixa()).replace("/", "-"));
        Diretorio.criar("Arquivos/recibo/" + baixa.getCaixa().getCaixa() + "/" + DataHoje.converteData(baixa.getDtBaixa()).replace("/", "-"));

        String path_arquivo = caminho + "/" + String.valueOf(baixa.getUsuario().getId()) + "_" + String.valueOf(baixa.getId()) + ".pdf";
        File file_arquivo = new File(path_arquivo);

        if (file_arquivo.exists()) {
            path_arquivo = caminho + "/" + String.valueOf(baixa.getUsuario().getId()) + "_" + String.valueOf(baixa.getId()) + "_(2).pdf";
        }

        try {
            File fl = new File(path_arquivo);
            FileOutputStream out = new FileOutputStream(fl);
            out.write(arquivo);
            out.flush();
            out.close();
        } catch (IOException e) {
            System.out.println(e);
        }

    }
}
