package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.DescontoServicoEmpresa;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

public class DescontoServicoEmpresaDBTopLink extends DB implements DescontoServicoEmpresaDB {

    @Override
    public boolean existeDescontoServicoEmpresa(DescontoServicoEmpresa descontoServicoEmpresa) {
        try {
            Query query = getEntityManager().createQuery(" SELECT DSE FROM DescontoServicoEmpresa AS DSE WHERE DSE.juridica.id = :idJuridica AND DSE.servicos.id = :idServicos ");
            query.setParameter("idJuridica", descontoServicoEmpresa.getJuridica().getId());
            query.setParameter("idServicos", descontoServicoEmpresa.getServicos().getId());
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public List<DescontoServicoEmpresa> listaTodos() {
        try {
            Query query = getEntityManager().createQuery(" SELECT DSE FROM DescontoServicoEmpresa AS DSE ORDER BY DSE.juridica.pessoa.nome ASC, DSE.servicos.descricao ASC ");
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    @Override
    public List<DescontoServicoEmpresa> listaTodosPorEmpresa(int idJuridica) {
        try {
            Query query = getEntityManager().createQuery(" SELECT DSE FROM DescontoServicoEmpresa AS DSE WHERE DSE.juridica.id = :idJuridica ORDER BY DSE.juridica.pessoa.nome ASC, DSE.servicos.descricao ASC ");
            query.setParameter("idJuridica", idJuridica);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    @Override
    public List<DescontoServicoEmpresa> pesquisaDescontoServicoEmpresas(String pesquisaPor, String descricao, String comoPesquisa) {
        Query query;
        if (pesquisaPor.equals("nome")) {
            String queryComoPesquisa;
            if (comoPesquisa.equals("I")) {
                queryComoPesquisa = " UPPER(DSE.juridica.pessoa.nome) LIKE '" + descricao.toUpperCase() + "%' OR UPPER(DSE.juridica.fantasia) LIKE '" + descricao.toUpperCase() + "%' ";
            } else {
                queryComoPesquisa = " UPPER(DSE.juridica.pessoa.nome) LIKE '%" + descricao.toUpperCase() + "%' OR UPPER(DSE.juridica.fantasia) LIKE '%" + descricao.toUpperCase() + "%' ";
            }
            query = getEntityManager().createQuery(" SELECT DSE FROM DescontoServicoEmpresa AS DSE WHERE " + queryComoPesquisa + " ORDER BY DSE.juridica.pessoa.nome ASC, DSE.servicos.descricao ASC ");
        } else if (pesquisaPor.equals("cnpj")) {
            query = getEntityManager().createQuery(" SELECT DSE FROM DescontoServicoEmpresa AS DSE WHERE DSE.juridica.pessoa.documento = '" + descricao + "'");
        } else {
            query = getEntityManager().createQuery(" SELECT DSE FROM DescontoServicoEmpresa AS DSE ORDER BY DSE.juridica.pessoa.nome ASC, DSE.servicos.descricao ASC LIMIT 100");
        }
        try {
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    @Override
    public DescontoServicoEmpresa pesquisaDescontoServicoEmpresa(DescontoServicoEmpresa descontoServicoEmpresa) {
        try {
            Query query = getEntityManager().createQuery(" SELECT DSE FROM DescontoServicoEmpresa AS DSE WHERE DSE.juridica.id = :idJuridica AND DSE.servicos.id = :idServicos ");
            query.setParameter("idJuridica", descontoServicoEmpresa.getJuridica().getId());
            query.setParameter("idServicos", descontoServicoEmpresa.getServicos().getId());
            List list = query.getResultList();
            if (!list.isEmpty()) {
                descontoServicoEmpresa = (DescontoServicoEmpresa) query.getSingleResult();
            }
        } catch (Exception e) {
            return descontoServicoEmpresa;
        }
        return descontoServicoEmpresa;
    }

    @Override
    public List<Servicos> listaTodosServicosDisponiveis(Integer id_empresa, Integer id_subgrupo_financeiro) {
        return listaTodosServicosDisponiveis(id_empresa, null, id_subgrupo_financeiro);
    }

    @Override
    public List<Servicos> listaTodosServicosDisponiveis(Integer id_empresa, Integer id_grupo_financeiro, Integer id_subgrupo_financeiro) {
        try {
            Query query;
            if (id_grupo_financeiro != null) {
                query = getEntityManager().createQuery(" SELECT S FROM Servicos AS S WHERE S.situacao = 'A' AND S.id NOT IN (SELECT DSE.servicos.id FROM DescontoServicoEmpresa AS DSE WHERE DSE.juridica.id = :juridica ) AND S.subGrupoFinanceiro.grupoFinanceiro.id = :grupoFinanceiro ORDER BY S.descricao ASC");
                query.setParameter("grupoFinanceiro", id_grupo_financeiro);
            } else {
                query = getEntityManager().createQuery(" SELECT S FROM Servicos AS S WHERE S.situacao = 'A' AND S.id NOT IN (SELECT DSE.servicos.id FROM DescontoServicoEmpresa AS DSE WHERE DSE.juridica.id = :juridica ) AND S.subGrupoFinanceiro.id = :subGrupoFinanceiro ORDER BY S.descricao ASC");
                query.setParameter("subGrupoFinanceiro", id_subgrupo_financeiro);
            }
            query.setParameter("juridica", id_empresa);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }
}
