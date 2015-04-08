package br.com.rtools.associativo.db;

import br.com.rtools.pessoa.Pessoa;
import java.util.List;

public interface MovimentosReceberSocialDB {
    public List pesquisaListaMovimentos(String id_pessoa, String id_responsavel, String por_status, String referencia);
    public List dadosSocio(int id_lote);
    public Pessoa pesquisaPessoaPorBoleto(String boleto, int id_conta_cobranca);
}
