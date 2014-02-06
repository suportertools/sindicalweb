package br.com.rtools.sistema.db;

import br.com.rtools.principal.DB;
import br.com.rtools.sistema.SisPessoa;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TemporalType;

public class SisPessoaDBToplink extends DB implements SisPessoaDB {

    @Override
    public List pesquisarSisPessoa(String desc, String por, String como) {
        if (por.equals("cnpj") || por.equals("cpf")) {
            por = "documento";
        }
        List lista;
        String textQuery = null;
        if (como.equals("T")) {
            textQuery = "";
        } else if (como.equals("P")) {
            desc = "%" + desc.toLowerCase().toUpperCase() + "%";
            textQuery = "select objeto from SisPessoa objeto where UPPER(objeto." + por + ") like :desc"
                    + " order by objeto.nome";
        } else if (como.equals("I")) {
            desc = desc.toLowerCase().toUpperCase() + "%";
            textQuery = "select objeto from SisPessoa objeto where UPPER(objeto." + por + ") like :desc"
                    + " order by objeto.nome";
        }
        try {
            Query qry = getEntityManager().createQuery(textQuery);
            if ((desc != null) && (!(como.equals("T")))) {
                qry.setParameter("desc", desc);
            }
            lista = qry.getResultList();
        } catch (Exception e) {
            lista = new ArrayList();
        }
        return lista;
    }

    @Override
    public SisPessoa sisPessoaExiste(SisPessoa sp) {
        return sisPessoaExiste(sp, false);
    }

    @Override
    public SisPessoa sisPessoaExiste(SisPessoa sp, boolean porDocumento) {
        Query qry;
        try {
            if (porDocumento) {
                qry = getEntityManager().createQuery("SELECT SP FROM SisPessoa AS SP WHERE SP.documento = :documento");
                qry.setParameter("documento", sp.getDocumento());
            } else {
                qry = getEntityManager().createQuery("SELECT SP FROM SisPessoa AS SP WHERE SP.nome = :nome AND SP.dtNascimento = :nascimento");
                qry.setParameter("nome", sp.getNome());
                qry.setParameter("nascimento", sp.getDtNascimento(), TemporalType.DATE);
            }
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return (SisPessoa) qry.getSingleResult();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
