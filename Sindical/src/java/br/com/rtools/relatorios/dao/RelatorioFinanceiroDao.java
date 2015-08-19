package br.com.rtools.relatorios.dao;

import br.com.rtools.financeiro.Plano5;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.TipoPagamento;
import br.com.rtools.principal.DB;
import br.com.rtools.seguranca.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class RelatorioFinanceiroDao extends DB {

    public List<Object> listaRelatorioFinanceiro(Integer id_contabil, Integer  id_grupo, Integer id_sub_grupo, Integer id_servicos, String dataEmissao, String dataVencimento, String dataQuitacao, String dataImportacao, String dataCredito, String dataFechamentoCaixa, Integer id_caixa_banco, Integer id_caixa, Integer id_operador, Integer id_tipo_quitacao) {
        String select
                = "SELECT \n "
                + "       grupo, \n "
                + "       subgrupo, \n "
                + "       servico, \n "
                + "       sum(valor_baixa) \n "
                + "  FROM movimentos_vw \n ";

        List<String> list_where = new ArrayList();
        
        String where = "";
        // 10/04/2015
        
        // CONTA CONTABIL ---
        if (id_contabil != null){
            list_where.add(" id_conta = "+id_contabil+ " \n ");
        }
        
        // GRUPO ---
        if (id_grupo != null){
            list_where.add(" id_grupo = "+id_grupo+ " \n ");
        }
        
        // SUB GRUPO ---
        if (id_sub_grupo != null){
            list_where.add(" id_subgrupo = "+id_sub_grupo+ " \n ");
        }
        
        // SERVICOS ---
        if (id_servicos != null){
            list_where.add(" id_servico = "+id_servicos+ " \n ");
        }
        
        // DATA EMISSAO ---
        if (!dataEmissao.isEmpty()){
            list_where.add(" emissao = '"+dataEmissao+"' \n ");
        }
        
        // DATA VENCIMENTO ---
        if (!dataVencimento.isEmpty()){
            list_where.add(" vencimento = '"+dataVencimento+"' \n ");
        }
        
        // DATA QUITAÇÃO ---
        if (!dataQuitacao.isEmpty()){
            list_where.add(" baixa = '"+dataQuitacao+"' \n ");
        }
        
        // DATA IMPORTACAO ---
        if (!dataImportacao.isEmpty()){
            list_where.add(" importacao = '"+dataImportacao+"' \n ");
        }
        
        // DATA CREDITO ---
        if (!dataCredito.isEmpty()){
            list_where.add(" dt_credito = '"+dataCredito+"' \n ");
        }
        
        // DATA FECHAMENTO CAIXA---
        if (!dataFechamentoCaixa.isEmpty()){
            list_where.add(" fechamento_caixa = '"+dataFechamentoCaixa+"' \n ");
        }
        
        // CAIXA / BANCO ---
        if (id_caixa_banco != null){
            list_where.add(" id_caixa_banco = "+id_caixa_banco+" \n ");
        }
        
        // CAIXA ---
        if (id_caixa != null){
            list_where.add(" id_caixa = "+id_caixa+" \n ");
        }
        
        // OPERADOR ---
        if (id_operador != null){
            list_where.add(" id_usuario_baixa = "+id_operador+" \n ");
        }
        
        // TIPO QUITAÇÃO ---
        if (id_tipo_quitacao != null){
            list_where.add(" id_tipo_pagamento = "+id_tipo_quitacao+" \n ");
        }
        
        if (list_where.isEmpty()){
            return new ArrayList();
        }
        
        for (String linha : list_where){
            if (where.isEmpty()){
                where = " WHERE " + linha;
            }else{
                where += " AND " + linha;
            }
        }
        
        String group_order = 
                  " GROUP BY \n "
                + "       grupo, \n "
                + "       subgrupo, \n "
                + "       servico \n "
                + " ORDER BY \n "
                + "       grupo,\n "
                + "       subgrupo, \n "
                + "       servico ";
        Query qry = getEntityManager().createNativeQuery(select + where + group_order);

        try {
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }

        return new ArrayList();
    }
    
    public List<Servicos> listaServicosSubGrupo(Integer id_subgrupo){
        Query qry = getEntityManager().createNativeQuery(
                "SELECT s.* \n " +
                "  FROM fin_servicos s \n " +
                " WHERE s.ds_situacao = 'A' \n " +
                (id_subgrupo != null ? "   AND s.id_subgrupo = " + id_subgrupo : "") + " \n " +
                " ORDER BY s.ds_descricao", Servicos.class
        );
        
        try {
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
    
    public List<Usuario> listaUsuario(){
        Query qry = getEntityManager().createQuery(
                "SELECT u " +
                "  FROM Usuario u " +
                " ORDER BY u.pessoa.nome"
        );
        
        try {
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
    
    public List<TipoPagamento> listaTipoQuitacao(){
        Query qry = getEntityManager().createQuery(
                "SELECT tp " +
                "  FROM TipoPagamento tp " +
                " ORDER BY tp.descricao"
        );
        
        try {
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
    
    public List<Object> listaPlanos(){
        Query qry = getEntityManager().createNativeQuery(
                "SELECT id_p1, conta1, id_p2, conta2, id_p3, conta3, id_p4, conta4 \n " +
                "  FROM plano_vw \n " +
                " GROUP BY id_p1, conta1, id_p2, conta2, id_p3, conta3, id_p4, conta4 \n " +
                " ORDER BY conta1, conta2, conta3, conta4 "
        );
        
        try {
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
    
    public List<Plano5> listaPlano5(String ids){
        Query qry = getEntityManager().createNativeQuery(
                "SELECT p5.* \n" +
                "  FROM fin_plano5 p5 \n " +
                " WHERE p5.id_plano4 IN ("+ids+") \n" +
                " ORDER BY p5.ds_conta ", Plano5.class
        );
        
        try {
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
    
    public List<Plano5> listaCaixaBanco(){
        Query qry = getEntityManager().createNativeQuery(
                "SELECT pl5.* \n " +
                "  FROM fin_plano5 pl5 \n " +
                " WHERE pl5.id = 1 OR pl5.id_conta_banco > 0 \n " +
                " ORDER BY pl5.id_conta_banco DESC, pl5.ds_conta", Plano5.class
        );
        
        try {
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
    
    
}
