package br.com.rtools.relatorios.db;

import br.com.rtools.principal.DB;
import br.com.rtools.relatorios.Relatorios;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class RelatorioHomologacaoDBToplink extends DB implements RelatorioHomologacaoDB{
    @Override
    public List pesquisaHomologacao(Relatorios relatorio, boolean booEmpresa, int id_empresa, boolean booFuncionario, int id_funcionario, boolean booData, String data_ini, String data_fim,
                               boolean booHomologador, int id_homologador, String ordem){
        String textQry = "select " +
                         " to_char(a.dt_data,'dd/mm/yyyy') as DataInicial," +
                         " to_char(a.dt_data,'dd/mm/yyyy') as DataFinal," +
                         " to_char(a.dt_data,'dd/mm/yyyy') as Data," +
                         " h.ds_hora as Hora," +
                         " empresa.ds_documento as Cnpj," +
                         " empresa.ds_nome as Empresa," +
                         " func.ds_nome as Funcionario," +
                         " a.ds_contato as Contato," +
                         " a.ds_telefone as Telefone," +
                         " hom.ds_nome as Homologador," +
                         " a.ds_obs as Obs" +
                         " from hom_agendamento          as a" +
                         " inner join hom_horarios       as h       on h.id = a.id_horario" +
                         " inner join seg_usuario        as u       on u.id = a.id_homologador" +
                         " inner join pes_pessoa         as hom     on hom.id = u.id_pessoa" +
                         " inner join pes_pessoa_empresa as pe      on pe.id = a.id_pessoa_empresa" +
                         " inner join pes_juridica       as j       on j.id = pe.id_juridica" +
                         " inner join pes_fisica         as f       on f.id = pe.id_fisica" +
                         " inner join pes_pessoa         as func    on func.id = f.id_pessoa" +
                         " inner join pes_pessoa         as empresa on empresa.id = j.id_pessoa";
        
        String filtro = "";
        
        if (relatorio.getQry() == null || relatorio.getQry().isEmpty()){
            filtro = " where ";
        }else{
            filtro = " where " +relatorio.getQry()+ " and ";
        }
       
        if (booData){
            // DATA DE CADASTRO ---------------
            if (!data_ini.isEmpty() && !data_fim.isEmpty())
                filtro += " a.dt_data >= '"+data_ini+"' and a.dt_data <= '"+data_fim+"'";
            else if (!data_ini.isEmpty())
                filtro += " a.dt_data = '"+data_ini+"'";
        }else{
            filtro += " a.dt_data >= '01/01/1900' and a.dt_data <= '01/01/2030'";
        }
            
        if (booEmpresa){
            if (id_empresa != -1)
                filtro += " and j.id = "+id_empresa;
        }
        
        if (booFuncionario){
            if (id_funcionario != -1)
                filtro += " and f.id = "+id_funcionario;
        }
        
        if (booHomologador){
            if (id_homologador != -1)
                filtro += " and u.id_pessoa = "+id_homologador;
        }
        
        String tordem = "";
        if (ordem.equals("data")){
            tordem = " a.dt_data desc, h.ds_hora, empresa.ds_nome";
        }else if (ordem.equals("empresa")){
            tordem = " empresa.ds_nome ";
        }else if (ordem.equals("funcionario")){
            tordem = " func.ds_nome ";
        }else if (ordem.equals("homologador")){
            tordem = " hom.ds_nome ";
        }
        
        // ORDEM DA QRY
        if (relatorio.getQryOrdem() == null || relatorio.getQryOrdem().isEmpty()){
            filtro += " order by "+tordem;
        }else{
            filtro += " order by "+ relatorio.getQryOrdem() +", "+ tordem;
        }        
        
        try{
            
            Query qry = getEntityManager().createNativeQuery(textQry + filtro);
//            String novaQuery = textQry + filtro;
            return qry.getResultList();
        }catch(Exception e){
            return new ArrayList();
        }
    }
}
