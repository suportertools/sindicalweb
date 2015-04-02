package br.com.rtools.homologacao.db;

import br.com.rtools.arrecadacao.Oposicao;
import br.com.rtools.homologacao.Agendamento;
import br.com.rtools.homologacao.Cancelamento;
import br.com.rtools.homologacao.Horarios;
import br.com.rtools.homologacao.Senha;
import br.com.rtools.pessoa.PessoaEmpresa;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

public interface HomologacaoDB {

    public EntityManager getEntityManager();

    public Agendamento pesquisaProtocolo(int id);

    public List<Agendamento> pesquisaAgendamento(Integer idStatus, Integer idFilial, Date dataInicial, Date dataFinal, Integer idUsuario, Integer idPessoaFisica, Integer idPessoaJuridica, Boolean somenteAtivos, Boolean web);

    public List pesquisaTodos(int idFilial);

    public int pesquisaQntdDisponivel(Integer idFilial, Horarios horarios, Date data);

    public int pesquisaQuantidadeAgendado(int idFilial, Horarios horarios, Date data);

    public List pesquisaAgendadoPorEmpresa(Date data, int idEmpresa);

    public List pesquisaTodosHorariosDisponiveis(Integer idFilial, Integer idDiaSemana);

    public List pesquisaTodosHorariosDisponiveis(Integer idFilial, Integer idDiaSemana, Boolean web);

    //public PessoaEmpresa pesquisaPessoaEmpresaOutra(String doc);
    public PessoaEmpresa pesquisaPessoaEmpresaPertencente(String doc);

    public List pesquisaPessoaDebito(int id_pessoa, String vencimento);

    public List pesquisaAgendadoDataMaior(Date data);

    public List pesquisaAgendadoPorEmpresaDataMaior(int idEmpresa);

    public List pesquisaAgendamentoPorPessoaEmpresa(int idPessoaEmpresa);

    public List pesquisaAgendadoPorEmpresaSemHorario(int id_filial, Date data, int idEmpresa);

    public Oposicao pesquisaFisicaOposicao(String cpf, int id_juridica);

    public List<Oposicao> pesquisaFisicaOposicaoSemEmpresa(String cpf);

    public Oposicao pesquisaFisicaOposicaoAgendamento(String cpf, int id_juridica, String referencia);

    public Agendamento pesquisaFisicaAgendada(int id_fisica, int id_juridica);

    public int pesquisaUltimaSenha(int id_filial);

    public Senha pesquisaSenhaAgendamento(int id_agendamento);

    public Senha pesquisaSenhaAtendimento(int id_filial);

    public Senha pesquisaAtendimentoIniciado(int id_usuario, int nr_mesa, int id_filial);

    public Senha pesquisaAtendimentoIniciadoSimples(int id_filial);

    public boolean verificaNaoAtendidosSegRegistroAgendamento();

    public List<Agendamento> pesquisaAgendamentoPorProtocolo(int numeroProtocolo);

    public boolean existeHorarioDisponivel(Date date, Horarios horarios);

    public Cancelamento pesquisaCancelamentoPorAgendanto(int idAgendamento);

    public List<Senha> listaAtendimentoIniciadoSimples(int id_filial, int id_usuario);

    public List<Senha> listaAtendimentoIniciadoSimplesPesquisa(int id_filial, int id_usuario, int id_status, String tipoData, String dataInicial, String dataFinal, int id_pessoa, String descricaoFisica, String tipoPesquisaFisica);

    public List<Senha> listaAtendimentoIniciadoSimplesUsuario(int id_filial, int id_usuario);

    public Senha pesquisaAtendimentoReserva(int id_filial, int id_usuario);

    public List<Senha> listaSequenciaSenha(int id_filial);

    public PessoaEmpresa pesquisaPessoaEmpresaAdmissao(int id_fisica, int id_juridica, String dataAdmissao);

    public PessoaEmpresa pesquisaPessoaEmpresaDemissao(int id_fisica, int id_juridica, String dataDemissao);

    public Agendamento pesquisaAgendamentoPorPessoaEmpresa(int id_pessoa_empresa, int[] ids_status);

}
