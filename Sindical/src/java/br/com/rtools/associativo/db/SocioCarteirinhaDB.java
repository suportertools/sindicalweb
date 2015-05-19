package br.com.rtools.associativo.db;

import br.com.rtools.associativo.AutorizaImpressaoCartao;
import br.com.rtools.associativo.HistoricoCarteirinha;
import br.com.rtools.associativo.ModeloCarteirinha;
import br.com.rtools.associativo.ModeloCarteirinhaCategoria;
import br.com.rtools.associativo.SocioCarteirinha;
import java.util.List;
import java.util.Vector;

public interface SocioCarteirinhaDB {

    public SocioCarteirinha pesquisaCodigo(int id);

    public List pesquisaTodos();

    public List pesquisaSocioSemCarteirinha();

    public List pesquisaSocioSemCarteirinhaDependente();

    public List pesquisaSocioCarteirinhaSocio(int idSocio);

    public List<Vector> pesquisaCarteirinha(String tipo, String descricao, String indexOrdem, Integer id_filial);
    
    //public List listaFiltro(String indexFiltro, String descEmpresa, String indexOrdem, boolean fantasia);

    public List filtroCartao(int id_socio);

    //public List listaFiltroCartao(int id_socio);

    public List listaPesquisaEtiqueta(int id_pessoa);

    public boolean verificaSocioCarteirinhaExiste(int id_pessoa);
    
    public List<HistoricoCarteirinha> listaHistoricoCarteirinha(int id_pessoa);
    
    public List<AutorizaImpressaoCartao> listaAutoriza(int id_pessoa, int id_modelo);
    
    public AutorizaImpressaoCartao pesquisaAutorizaSemHistorico(int id_pessoa, int id_modelo);
    
    public AutorizaImpressaoCartao pesquisaAutorizaPorHistorico(int id_historico);
            
    public List<SocioCarteirinha> listaSocioCarteirinhaAutoriza(int id_pessoa, int id_modelo);
    
    public ModeloCarteirinha pesquisaModeloCarteirinha(int id_categoria, int id_rotina);
    
    public ModeloCarteirinhaCategoria pesquisaModeloCarteirinhaCategoria(int id_modelo, int id_categoria, int id_rotina);
    
    public SocioCarteirinha pesquisaCarteirinhaPessoa(int id_pessoa, int id_modelo);
    
}
