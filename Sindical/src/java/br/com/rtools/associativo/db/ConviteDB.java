package br.com.rtools.associativo.db;

import br.com.rtools.associativo.ConviteServico;
import br.com.rtools.associativo.ConviteSuspencao;
import br.com.rtools.sistema.SisPessoa;
import java.util.List;

public interface ConviteDB {

    public List<ConviteServico> conviteServicoExiste(ConviteServico cs);

    public boolean existeSisPessoaSuspensa(ConviteSuspencao cs);

    public List<ConviteSuspencao> listaPessoasSuspensas(ConviteSuspencao cs, boolean filtro, boolean fitroPorPessoa);

    public List<ConviteSuspencao> listaPessoasSuspensas(ConviteSuspencao cs, boolean filtro, boolean fitroPorPessoa, String descricaoPesquisa, String porPesquisa, String comoPesquisa);

    public List pesquisaConviteMovimento(String descricaoPesquisa, String porPesquisa, String comoPesquisa);

    public boolean limiteConvitePorSocio(int quantidadeConvites, int quantidadeDias, int idPessoaSocio);

    public boolean limiteConviteConvidado(int quantidadeConvites, int quantidadeDias, int idSisPessoa);

    public boolean socio(SisPessoa s);

}
