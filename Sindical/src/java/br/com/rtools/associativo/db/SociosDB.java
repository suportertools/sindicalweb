package br.com.rtools.associativo.db;

import br.com.rtools.associativo.DescontoSocial;
import br.com.rtools.associativo.SocioCarteirinha;
import br.com.rtools.associativo.Socios;
import br.com.rtools.financeiro.ServicoPessoa;
import br.com.rtools.pessoa.Pessoa;
import java.util.List;
import javax.persistence.EntityManager;

public interface SociosDB {

    public EntityManager getEntityManager();

    public List pesquisaTodos();

    public Socios pesquisaSocioPorId(int idServicoPessoa);

    public List pesquisaSocios(String desc, String por, String como, String status);

    public List<Socios> listaDependentes(int id_matricula);

    public List<Socios> listaDependentesInativos(int id_matricula);

    public Socios pesquisaSocioPorPessoa(int idPessoa);

    public Socios pesquisaSocioPorPessoaEMatriculaSocio(int idPessoa, int idMatriculaSocio);

    public Socios pesquisaSocioDoDependente(int idDependente);

    public Socios pesquisaSocioDoDependente(Pessoa pessoa);

    public List pesquisaSociosAtivos();

    public List pesquisaSociosInativos();

    public Socios pesquisaSocioPorPessoaAtivo(int idPessoa);

    public List pesquisaSocioPorPessoaInativo(int idPessoa);

    public List<Socios> pesquisaSocioPorPessoaTitularInativo(int idPessoa);

    public Socios pesquisaSocioTitularInativoPorPessoa(int idPessoa);
    
    public List<Socios> listaSocioTitularInativoPorPessoa(int idPessoa);

    public List pesquisaDependentesOrdenado(int idPessoaSocio);

    public float descontoSocioEve(int idPessoa, int idServico);

    public List<SocioCarteirinha> pesquisaCarteirinhasPorPessoa(int id_pessoa, int id_modelo);

    public List pesquisaMotivoInativacao();

    public boolean socioDebito(int idPessoa);

    public List<DescontoSocial> listaDescontoSocial(int id_categoria);

    public List<ServicoPessoa> listaServicoPessoaPorDescontoSocial(Integer id_desconto_social, Integer id_pessoa);
}
