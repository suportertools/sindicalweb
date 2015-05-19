package br.com.rtools.associativo.db;

import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.TransferenciaCaixa;
import br.com.rtools.pessoa.Pessoa;
import java.util.List;
import java.util.Vector;

public interface MovimentosReceberSocialDB {
    public List pesquisaListaMovimentos(String id_pessoa, String id_responsavel, String por_status, String referencia, String tipoPessoa, String lote_baixa);
    public List dadosSocio(int id_lote);
    public Pessoa pesquisaPessoaPorBoleto(String boleto, int id_conta_cobranca);
    public float[] pesquisaValorAcrescimo(int id_movimento);
    public List<Vector> listaBoletosAbertosAgrupado(int id_pessoa, boolean atrasados);
    public List<Movimento> listaMovimentosAbertosAnexarAgrupado(int id_pessoa);
    public List<Movimento> listaMovimentosPorNrCtrBoleto(String nr_ctr_boleto);
    public Pessoa responsavelBoleto(String nr_ctr_boleto);
    public List<TransferenciaCaixa> transferenciaCaixa(Integer id_fechamento_caixa_saida);
}
