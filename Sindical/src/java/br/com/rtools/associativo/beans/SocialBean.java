package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.dao.SociosDao;
import br.com.rtools.pessoa.Pessoa;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class SocialBean {

    public Boolean getExistPessoasMesmaMatricula() {
        return new SociosDao().existPessoasMesmaMatricula();
    }

    public List<Pessoa> getListPessoasMesmaMatricula() {
        return new SociosDao().listPessoasMesmaMatricula();
    }

    public Boolean getExistatriculaAtivaAtivacaoDesordenada() {
        return new SociosDao().existMatriculaAtivaAtivacaoDesordenada();
    }

    public List<Pessoa> getListatriculaAtivaAtivacaoDesordenada() {
        return new SociosDao().listMatriculaAtivaAtivacaoDesordenada();
    }

}
