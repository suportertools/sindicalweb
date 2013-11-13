package br.com.rtools.associativo.db;

import br.com.rtools.associativo.HistoricoCarteirinha;
import br.com.rtools.associativo.SocioCarteirinha;
import br.com.rtools.endereco.Endereco;
import java.util.List;
import java.util.Vector;

public interface SocioCarteirinhaDB {

    public SocioCarteirinha pesquisaCodigo(int id);

    public List pesquisaTodos();

    public List pesquisaSocioSemCarteirinha();

    public List pesquisaSocioSemCarteirinhaDependente();

    public List pesquisaSocioCarteirinhaSocio(int idSocio);

    public List<Vector> pesquisaCarteirinha(String tipo, String descricao, String indexOrdem);
    
    public List listaFiltro(String indexFiltro, String descEmpresa, String indexOrdem, boolean fantasia);

    public List filtroCartao(int id_socio);

    public List listaFiltroCartao(int id_socio);

    public List listaPesquisaEtiqueta(int id_pessoa);

    public boolean verificaSocioCarteirinhaExiste(int id_socio);
    
    public List<HistoricoCarteirinha> listaHistoricoCarteirinha(int id_socio);
}
