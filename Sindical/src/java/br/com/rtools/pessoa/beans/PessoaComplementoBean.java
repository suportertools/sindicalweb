package br.com.rtools.pessoa.beans;

import br.com.rtools.pessoa.*;
import br.com.rtools.pessoa.db.PessoaDB;
import br.com.rtools.pessoa.db.PessoaDBToplink;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class PessoaComplementoBean extends PesquisarProfissaoBean implements Serializable {

    private PessoaComplemento pessoaComplemento = new PessoaComplemento();
    private Pessoa pessoa = new Pessoa();
    private Registro registro = new Registro();
    private int diaVencimento = 0;
    private List<SelectItem> listaDataVencimento = new ArrayList<SelectItem>();

    public void atualizar(int idPessoa) {
        if (idPessoa != -1) {
//            if (pessoaComplemento.getId() == -1) {
//                PessoaDB pessoaDB = new PessoaDBToplink();
//                pessoaComplemento = pessoaDB.pesquisaPessoaComplementoPorPessoa(idPessoa);
//            }
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            pessoaComplemento.setPessoa((Pessoa) salvarAcumuladoDB.pesquisaCodigo(idPessoa, "Pessoa"));
            pessoaComplemento.setNrDiaVencimento(diaVencimento);
            salvarAcumuladoDB.abrirTransacao();
            if (pessoaComplemento.getId() == -1) {
                if (salvarAcumuladoDB.inserirObjeto(pessoaComplemento)) {
                    salvarAcumuladoDB.comitarTransacao();
                } else {
                    salvarAcumuladoDB.desfazerTransacao();
                }
            } else {
                if (salvarAcumuladoDB.alterarObjeto(pessoaComplemento)) {
                    salvarAcumuladoDB.comitarTransacao();
                } else {
                    salvarAcumuladoDB.desfazerTransacao();
                }
            }
        }
    }

    public List<SelectItem> getListaDataVencimento() {
        if (listaDataVencimento.isEmpty()) {
            for (int i = 1; i <= 31; i++) {
                listaDataVencimento.add(new SelectItem(Integer.toString(i)));
            }
        }
        return listaDataVencimento;
    }

    public void setListaDataVencimento(List<SelectItem> listaDataVencimento) {
        this.listaDataVencimento = listaDataVencimento;
    }

    public PessoaComplemento getPessoaComplemento() {
        return pessoaComplemento;
    }

    public void setPessoaComplemento(PessoaComplemento pessoaComplemento) {
        this.pessoaComplemento = pessoaComplemento;
    }

    public String pessoaComplementoPesquisaPessoa(int idPessoa) {
        if (pessoaComplemento.getId() == -1) {
            PessoaDB pessoaDB = new PessoaDBToplink();
            pessoaComplemento = pessoaDB.pesquisaPessoaComplementoPorPessoa(idPessoa);
            if(pessoaComplemento.getId() == -1) {
                diaVencimento = getRegistro().getFinDiaVencimentoCobranca();                
            } else {
                diaVencimento = pessoaComplemento.getNrDiaVencimento();                
            }
        } else {
            diaVencimento = pessoaComplemento.getNrDiaVencimento();            
        }
        return "";
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public int getDiaVencimento() {
        return diaVencimento;
    }

    public void setDiaVencimento(int diaVencimento) {
        this.diaVencimento = diaVencimento;
    }

    public Registro getRegistro() {
        if (registro == null || registro.getId() == -1) {
            SalvarAcumuladoDB sadb = new SalvarAcumuladoDBToplink();
            registro = (Registro) sadb.pesquisaObjeto(1, "Registro");
        }
        return registro;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }
}
