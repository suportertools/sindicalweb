package br.com.rtools.principal;

import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.GenericaSessao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import oracle.toplink.essentials.config.CacheType;
import oracle.toplink.essentials.config.TopLinkProperties;
// import oracle.toplink.essentials.ejb.cmp3.EntityManagerFactoryProvider;

public class DB {

    private EntityManager entidade;
    private Statement statement;

//  public EntityManager getEntityManager(){
//      if (entidade==null){
//          EntityManagerFactory emf = Persistence.createEntityManagerFactory("ComercioIta");
//          entidade = emf.createEntityManager();
//        }
//        return entidade;
//    } 
    public Statement getStatment() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://192.168.1.60:5432/Rtools", "postgres", "989899");
            if (connection != null) {
                statement = connection.createStatement();
                return statement;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public void closeStatment() throws SQLException {
        statement.getResultSet().close();
        statement.close();
        statement.getConnection().close();
    }

    public EntityManager getEntityManager() {
        if (entidade == null) {
            if (!GenericaSessao.exists("conexao")) {
                String cliente = (String) GenericaSessao.getString("sessaoCliente");
                DataObject config = servidor(cliente);
                try {
                    Map properties = new HashMap();
                    //EntityManagerFactory emf = null;
                    properties.put(TopLinkProperties.CACHE_TYPE_DEFAULT, CacheType.Full);
                    properties.put(TopLinkProperties.JDBC_USER, "postgres");
                    properties.put(TopLinkProperties.TRANSACTION_TYPE, "RESOURCE_LOCAL");
                    properties.put(TopLinkProperties.JDBC_DRIVER, "org.postgresql.Driver");
                    properties.put(TopLinkProperties.JDBC_PASSWORD, config.getArgumento2().toString());
                    properties.put(TopLinkProperties.JDBC_URL, "jdbc:postgresql://" + config.getArgumento0() + ":5432/" + config.getArgumento1().toString());
                    EntityManagerFactory emf = Persistence.createEntityManagerFactory(config.getArgumento1().toString(), properties);
                    //properties.put(EntityManagerFactoryProvider.DDL_GENERATION, EntityManagerFactoryProvider.CREATE_ONLY);
                    //emf = Persistence.createEntityManagerFactory(persist, properties);
                    entidade = emf.createEntityManager();
                    GenericaSessao.put("conexao", emf);
                } catch (Exception e) {
                    return null;
                }
            } else {
                try {
                    EntityManagerFactory emf = (EntityManagerFactory) GenericaSessao.getObject("conexao");
                    entidade = emf.createEntityManager();                    
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return entidade;
    }

    // dataObject.setArgumento0(ip do servidor);
    // dataObject.setArgumento1(base do cliente);
    // dataObject.setArgumento2(senha);
    public DataObject servidor(String cliente) {
        DataObject dataObject = new DataObject();
        if (cliente.equals("ComercioAraras")
                || cliente.equals("ComercioSertaozinho")
                || cliente.equals("FederacaoBH")
                || cliente.equals("SinpaaeRP")
                || cliente.equals("VestuarioLimeira")
                || cliente.equals("ComercioLimeira")
                || cliente.equals("ComercioItapetininga")
                || cliente.equals("SeaacRP")
                || cliente.equals("MetalRP")
                || cliente.equals("ExtrativaRP")
                || cliente.equals("AlimentacaoArceburgo")
                || cliente.equals("FederacaoExtrativaSP")
                || cliente.equals("ExtrativaSP")
                || cliente.equals("HoteleiroRP")
                || cliente.equals("GaragistaRP")
                || cliente.equals("MetalBatatais")) {
            dataObject.setArgumento0("192.168.1.102");
            dataObject.setArgumento1(cliente);
            dataObject.setArgumento2("r#@tools");
        } else if (cliente.equals("Rtools")) {
            dataObject.setArgumento0("192.168.1.60");
            dataObject.setArgumento1(cliente);
            dataObject.setArgumento2("989899");
        } else if (cliente.equals("NovaBase")) {
            dataObject.setArgumento0("192.168.1.69");
            dataObject.setArgumento1(cliente);
            dataObject.setArgumento2("989899");
        } else if (cliente.equals("ComercioRP")) {
            dataObject.setArgumento0("200.152.187.241");
            dataObject.setArgumento1("Sindical");
            dataObject.setArgumento2("989899");
        } else {
            if (cliente.equals("Sindical")) {
                //cliente = "c_limeira_base";
                cliente = "n_base_local";
            }
            dataObject.setArgumento0("localhost");
            dataObject.setArgumento1(cliente);
            dataObject.setArgumento2("989899");
        }
        return dataObject;
    }
}
