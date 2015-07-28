package br.com.rtools.utilitarios;

import br.com.rtools.endereco.Bairro;
import br.com.rtools.endereco.CepAlias;
import br.com.rtools.endereco.Cidade;
import br.com.rtools.endereco.DescricaoEndereco;
import br.com.rtools.endereco.Endereco;
import br.com.rtools.endereco.Logradouro;
import br.com.rtools.endereco.dao.BairroDao;
import br.com.rtools.endereco.dao.DescricaoEnderecoDao;
import br.com.rtools.endereco.dao.LogradouroDao;
import br.com.rtools.endereco.dao.CidadeDao;
import br.com.rtools.endereco.db.EnderecoDao;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.Annotations;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class CEPService {

    private String cep = "";
    private String cepMemoria = "";
    private Endereco endereco = new Endereco();

    /**
     * http://www.republicavirtual.com.br/cep/exemplos.php
     */
    public void procurar() {
        EnderecoDao enderecoDB = new EnderecoDao();
        CidadeDao cidadeDB = new CidadeDao();
        List<Endereco> listaEnderecos = (List<Endereco>) enderecoDB.pesquisaEnderecoCep(cep);
        if (listaEnderecos == null) {
            if (cepMemoria.equals(cep)) {
                return;
            }
            if (cepMemoria.equals("")) {
                cepMemoria = cep;
            }
            cep = cep.replace("-", "");
            String urlString = "http://cep.republicavirtual.com.br/web_cep.php?cep=" + cep + "&formato=query_string";
            // os parametros a serem enviados
            Properties parameters = new Properties();
            parameters.setProperty("cep", cep);
            parameters.setProperty("formato", "xml");
            Iterator i = parameters.keySet().iterator();
            int counter = 0;
            while (i.hasNext()) {
                String name = (String) i.next();
                String value = parameters.getProperty(name);
                urlString += (++counter == 1 ? "?" : "&") + name + "=" + value;
            }
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Request-Method", "GET");
                connection.setDoInput(true);
                connection.setDoOutput(false);
                connection.connect();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder newData = new StringBuilder();
                String s = "";
                while (null != ((s = br.readLine()))) {
                    newData.append(s);
                }
                br.close();
                XStream xstream = new XStream(new DomDriver());
                Annotations.configureAliases(xstream, CepAlias.class);
                xstream.alias("webservicecep", CepAlias.class);
                CepAlias cepAlias = (CepAlias) xstream.fromXML(newData.toString());
                Dao dao = new Dao();
                Cidade cidade = cidadeDB.pesquisaCidadePorEstadoCidade(cepAlias.getUf(), cepAlias.getCidade());
                if (cidade == null) {
                    cidade = new Cidade();
                    cidade.setCidade(cepAlias.getCidade());
                    cidade.setUf(cepAlias.getUf());
                    dao.save(cidade, true);
                }
                LogradouroDao logradouroDao = new LogradouroDao();
                Logradouro logradouro = logradouroDao.pesquisaLogradouroPorDescricao(cepAlias.getTipo_logradouro());
                if (logradouro == null) {
                    logradouro = new Logradouro();
                    logradouro.setDescricao(cepAlias.getTipo_logradouro());
                    dao.save(logradouro, true);
                }
                BairroDao bairroDao = new BairroDao();
                Bairro bairro = bairroDao.pesquisaBairroPorDescricaoCliente(cepAlias.getBairro());
                if (bairro == null) {
                    bairro = new Bairro();
                    bairro.setDescricao(cepAlias.getBairro());
                    dao.save(bairro, true);
                }
                DescricaoEnderecoDao descricaoEnderecoDao = new DescricaoEnderecoDao();
                DescricaoEndereco descricaoEndereco = descricaoEnderecoDao.pesquisaDescricaoEnderecoPorDescricao(cepAlias.getLogradouro());
                if (descricaoEndereco == null) {
                    descricaoEndereco = new DescricaoEndereco();
                    descricaoEndereco.setDescricao(cepAlias.getLogradouro());
                    dao.save(descricaoEndereco, true);
                }
                endereco = new Endereco();
                endereco.setCep(cep);
                endereco.setBairro(bairro);
                endereco.setCidade(cidade);
                endereco.setDescricaoEndereco(descricaoEndereco);
                endereco.setLogradouro(logradouro);
                List list = enderecoDB.pesquisaEndereco(endereco.getDescricaoEndereco().getId(), endereco.getCidade().getId(), endereco.getBairro().getId(), endereco.getLogradouro().getId());
                if (list.isEmpty()) {
                    dao.save(endereco, true);
                } else {
                    endereco = new Endereco();
                    list.clear();
                }
            } catch (IOException e) {
            }
        } else {
            endereco = new Endereco();
            if (listaEnderecos.size() == 1) {
                endereco = listaEnderecos.get(0);
            } else {
                endereco.setBairro(listaEnderecos.get(0).getBairro());
            }
        }
        cep = "";
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCepMemoria() {
        return cepMemoria;
    }

    public void setCepMemoria(String cepMemoria) {
        this.cepMemoria = cepMemoria;
    }
}
