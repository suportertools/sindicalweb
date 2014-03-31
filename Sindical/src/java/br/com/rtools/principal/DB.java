package br.com.rtools.principal;

import br.com.rtools.sistema.Configuracao;
import br.com.rtools.utilitarios.GenericaRequisicao;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.GenericaString;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import oracle.toplink.essentials.config.CacheType;
import oracle.toplink.essentials.config.TopLinkProperties;
import oracle.toplink.essentials.ejb.cmp3.EntityManagerFactoryProvider;
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
    public EntityManager getEntityManager() {
        if (entidade == null) {
            if (!GenericaSessao.exists("conexao")) {
                String cliente = (String) GenericaSessao.getString("sessaoCliente");
                Configuracao configuracao = servidor(cliente);
                try {
                    Map properties = new HashMap();
                    properties.put(TopLinkProperties.CACHE_TYPE_DEFAULT, CacheType.Full);
                    properties.put(TopLinkProperties.JDBC_USER, "postgres");
                    properties.put(TopLinkProperties.TRANSACTION_TYPE, "RESOURCE_LOCAL");
                    properties.put(TopLinkProperties.JDBC_DRIVER, "org.postgresql.Driver");
                    properties.put(TopLinkProperties.JDBC_PASSWORD, configuracao.getSenha());
                    properties.put(TopLinkProperties.JDBC_URL, "jdbc:postgresql://" + configuracao.getHost() + ":5432/" + configuracao.getPersistence());
                    EntityManagerFactory emf = Persistence.createEntityManagerFactory(configuracao.getPersistence(), properties);
                    String createTable = GenericaString.converterNullToString(GenericaRequisicao.getParametro("createTable"));
                    if (createTable.equals("criar")) {
                        properties.put(EntityManagerFactoryProvider.DDL_GENERATION, EntityManagerFactoryProvider.CREATE_ONLY);
                    }
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

    public Configuracao servidor(String cliente) {
        Configuracao configuracao = new Configuracao();
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
                || cliente.equals("MetalBatatais")
                || cliente.equals("SeaacFranca")) {
            configuracao.setCaminhoSistema(cliente);
            configuracao.setPersistence(cliente);
            configuracao.setHost("192.168.1.102");
            configuracao.setSenha("r#@tools");
        } else if (cliente.equals("Rtools")) {
            configuracao.setCaminhoSistema(cliente);
            configuracao.setPersistence(cliente);
            configuracao.setHost("192.168.1.60");
            configuracao.setSenha("989899");
        } else if (cliente.equals("NovaBase")) {
            configuracao.setCaminhoSistema(cliente);
            configuracao.setPersistence(cliente);
            configuracao.setHost("192.168.1.69");
            configuracao.setSenha("989899");
        } else if (cliente.equals("ComercioRP")) {
            configuracao.setCaminhoSistema(cliente);
            configuracao.setPersistence("Sindical");
            configuracao.setHost("200.152.187.241");
            configuracao.setSenha("989899");
        } else {
            if (cliente.equals("Sindical")) {
                cliente = "c_limeira_base";
                //cliente = "n_base_local";
            }
            configuracao.setCaminhoSistema(cliente);
            configuracao.setPersistence(cliente);
            configuracao.setHost("localhost");
            configuracao.setSenha("989899");
        }
        return configuracao;
    }
}
