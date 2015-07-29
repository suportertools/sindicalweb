package br.com.rtools.pessoa.db;

import br.com.rtools.arrecadacao.Convencao;
import br.com.rtools.arrecadacao.GrupoCidade;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.pessoa.Cnae;
import br.com.rtools.pessoa.Juridica;
import java.util.List;

public interface EnviarArquivosDB {

    public Juridica pesquisaCodigo(int id);

    public List pesquisaContabilidades();
    
    public List pesquisaContabilidades(String inConvencao, String inGrupoCidade);

    public List pesquisaContribuintes(String listaConvencao, String listaGrupoCidade, String listaCnae, boolean empresasDebito, String ids_servicos, String data_vencimento);

    public List<Convencao> listaConvencao();
    
    public List<Convencao> listaConvencao(boolean isContabilidade);

    public List<GrupoCidade> listaGrupoCidadePorConvencao(String listaConvencao);

    public List<Cnae> listaCnaePorConvencao(String listaConvencao);
    
    public List<Servicos> listaServicosAteVencimento();
}