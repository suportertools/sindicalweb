package br.com.rtools.pessoa.db;

import br.com.rtools.endereco.Endereco;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.AnaliseString;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;

public class PessoaEnderecoDBToplink extends DB implements PessoaEnderecoDB {

    @Override
    public boolean insert(PessoaEndereco pessoaEndereco) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(pessoaEndereco);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(PessoaEndereco pessoaEndereco) {
        try {
            getEntityManager().merge(pessoaEndereco);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(PessoaEndereco pessoaEndereco) {
        try {
            getEntityManager().remove(pessoaEndereco);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public PessoaEndereco pesquisaCodigo(int id) {
        PessoaEndereco result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("PessoaEndereco.pesquisaID");
            qry.setParameter("pid", id);
            result = (PessoaEndereco) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select pe from PessoaEndereco pe ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List pesquisaEndPorPessoa(int id) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select pe"
                    + "  from PessoaEndereco pe"
                    + " where pe.pessoa.id = :pid"
                    + " order by pe.tipoEndereco.id");
            qry.setParameter("pid", id);
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public PessoaEndereco pesquisaEndPorPessoaTipo(int idPessoa, int idTipoEndereco) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select pe"
                    + "  from PessoaEndereco pe"
                    + " where pe.pessoa.id = :p"
                    + "   and pe.tipoEndereco.id = :t");
            qry.setParameter("p", idPessoa);
            qry.setParameter("t", idTipoEndereco);
            return (PessoaEndereco) qry.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Endereco enderecoReceita(String cep, String[] descricao, String[] bairro) {
        List vetor;
        Endereco endereco = new Endereco();
        SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
        
        try {
            String text_qry = "select * from end_endereco e "
                            + " inner join end_descricao_endereco de on (de.id = e.id_descricao_endereco) "
                            + " inner join end_bairro ba on (ba.id = e.id_bairro) "
                            + " where ds_cep = '"+cep+"'";
            String or_desc = "", or_bairro = "";
            for (int i = 0; i < descricao.length; i++){
                if (descricao.length == 1){
                    text_qry += " and ( upper(translate(de.ds_descricao)) like upper('%"+descricao[i]+"%') ) ";
                    break;
                }else{
                    or_desc += " or upper(translate(de.ds_descricao)) like upper('%"+descricao[i]+"%') ";
                }
            }
            if (descricao.length > 1){
                text_qry += " and ( upper(translate(de.ds_descricao)) like upper('%"+descricao[0]+"%') "+or_desc+") ";
            }
            
            
            for (int i = 0; i < bairro.length; i++){
                if (bairro.length == 1){
                    text_qry += " and ( upper(translate(ba.ds_descricao)) like upper('%"+bairro[i]+"%') ) ";
                    break;
                }else{
                    or_bairro += " or upper(translate(ba.ds_descricao)) like upper('%"+bairro[i]+"%') ";
                }
            }
            if (bairro.length > 1){
                text_qry += " and ( upper(translate(ba.ds_descricao)) like upper('%"+bairro[0]+"%') "+or_bairro+") ";
            }
            
            Query qry = getEntityManager().createNativeQuery(text_qry);
            qry.setMaxResults(1);
            vetor = qry.getResultList();
            if (!vetor.isEmpty()) {
                endereco = (Endereco) dB.pesquisaObjeto((Integer) ((Vector) vetor.get(0)).get(0), "Endereco");
                return endereco;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
