package br.com.rtools.associativo.beans;

import br.com.rtools.impressao.ParametroCarneMensalidades;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.db.PessoaEnderecoDB;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;


@ManagedBean
@SessionScoped
public class CarneMensalidadesBean {
    private String mes;
    private String ano;
    private List listaData;
    private Pessoa pessoa;
    private List<Pessoa> listaPessoa;
    
    @PostConstruct
    public void init(){
        DataHoje dh = new DataHoje();
        mes = DataHoje.DataToArrayString(dh.incrementarMeses(1, DataHoje.data()))[1];
        ano = DataHoje.DataToArrayString(DataHoje.data())[2];
        
        listaData = new ArrayList();
        pessoa = new Pessoa();
        listaPessoa = new ArrayList();
    }
    
    @PreDestroy
    public void destroy(){
        
    }
    
    public void imprimirCarne(){
        Juridica sindicato = (Juridica) (new Dao()).find(new Juridica(), 1);
        PessoaEnderecoDB dbp = new PessoaEnderecoDBToplink();
        PessoaEndereco pe = dbp.pesquisaEndPorPessoaTipo(1, 2);
        
        List result = new ArrayList();
        Collection lista = new ArrayList();
        
        
        List listax = new ArrayList();
        
        listax.add("TESTE 01");
        listax.add("TESTE FUNCIONAL");
        
        
        
        for (int i = 0; i < 4; i++){
            lista.add(
                new ParametroCarneMensalidades(
                    ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
                    sindicato.getPessoa().getNome(),
                    pe.getEndereco().getDescricaoEndereco().getDescricao(),
                    pe.getEndereco().getLogradouro().getDescricao(),
                    pe.getNumero(),
                    pe.getComplemento(),
                    pe.getEndereco().getBairro().getDescricao(),
                    pe.getEndereco().getCep().substring(0, 5) + "-" + pe.getEndereco().getCep().substring(5),
                    pe.getEndereco().getCidade().getCidade(),
                    pe.getEndereco().getCidade().getUf(),
                    sindicato.getPessoa().getTelefone1(),
                    sindicato.getPessoa().getEmail1(),
                    sindicato.getPessoa().getSite(),
                    sindicato.getPessoa().getDocumento(),
                    (i > 1) ? "RAMON ANDRADE" : "MARIA AUGUSTA", 
                    "000125", 
                    "123", 
                    "COMERCIÁRIO",
                    (i > 1) ? "MENSALIDADE TITULAR" : "ACADEMIA",
                    0,
                    i+1,
                    "20/02/2001",
                    "10,00",
                    "50,00",
                    listax
                )
            );
        }
        
        try {
            File file_jasper = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/CARNE_MENSALIDADES.jasper"));
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(file_jasper);

            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dtSource);
            byte[] arquivo = JasperExportManager.exportReportToPdf(jasperPrint);
            
            HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            res.setContentType("application/pdf");
            res.setHeader("Content-disposition", "inline; filename=\"Carnê de Mensalidades.pdf\"");
            res.getOutputStream().write(arquivo);
            res.getCharacterEncoding();
            FacesContext.getCurrentInstance().responseComplete();

        } catch (JRException | IOException e) {
            e.getMessage();
        }        
    }    
    
    public void adicionarData(){
        if (!listaData.isEmpty()){
            boolean existe = false;
            for (Object data : listaData) {
                if (data.toString().equals(mes+"/"+ano)) {
                    existe = true;
                }
            }
            if (!existe)
                listaData.add(mes+"/"+ano);
                
        }else{
            listaData.add(mes+"/"+ano);
        }
    }    
    
    
    public void adicionarTodasData(){
        if (!listaData.isEmpty()){
            boolean existe = false;
            for (int w = 1; w <= 12; w++){
                String mesx = (w < 10) ? "0"+w : ""+w;
                for (int i = 0; i < listaData.size(); i++){
                    if (listaData.get(i).toString().equals(mesx+"/"+ano)){
                        existe = true;
                        break;
                    }
                }
                if (!existe){
                    listaData.add(mesx+"/"+ano);
                }
                existe = false;
            }
        }else{
            for (int w = 1; w <= 12; w++){
                String mesx = (w < 10) ? "0"+w : ""+w;
                listaData.add(mesx+"/"+ano);
            }
        }
    }    
        
    public void removerDataLista(int index) {
        listaData.remove(index);
    }   
       
    public void adicionarPessoa() {
        listaPessoa.add(pessoa);
        pessoa = new Pessoa();
    }
    

    public void removerPessoaLista(int index) {
        listaPessoa.remove(index);
    }    
    
    public void removerPessoa(){
        pessoa = new Pessoa();
    }
    
    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public List getListaData() {
        return listaData;
    }

    public void setListaData(List listaData) {
        this.listaData = listaData;
    }

    public Pessoa getPessoa() {
        if (GenericaSessao.getObject("pessoaPesquisa") != null){
            pessoa = (Pessoa) GenericaSessao.getObject("pessoaPesquisa");
            GenericaSessao.remove("pessoaPesquisa");
            adicionarPessoa();
        }        
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public List<Pessoa> getListaPessoa() {
        return listaPessoa;
    }

    public void setListaPessoa(List<Pessoa> listaPessoa) {
        this.listaPessoa = listaPessoa;
    }
}
// select       se.ds_descricao as servico,  
//     tp.ds_descricao as tipo, 
//     m.ds_referencia, 
//     m.dt_vencimento as vencimento, 
//     func_valor(m.id) as valor, 
//     func_multa_ass(m.id)+func_juros_ass(m.id)+func_correcao_ass(m.id) as acrescimo, 
//     m.nr_desconto as desconto, 
//     m.nr_valor+func_multa_ass(m.id)+func_juros_ass(m.id)+func_correcao_ass(m.id) as vl_calculado, 
//     bx.dt_baixa, 
//     nr_valor_baixa as valor_pago,  
//     m.ds_es as es, 
//     p.ds_nome as responsavel, 
//     b.ds_nome as beneficiario, 
//     p.id as id_responsavel, 
//     m.id as id_movimento, 
//     m.id_lote as lote, 
//     l.dt_lancamento as criacao,  
//     m.ds_documento as boleto, 
//     func_intervalo_dias(m.dt_vencimento,CURRENT_DATE) as dias_atraso, 
//     func_multa_ass(m.id) as multa,  
//     func_juros_ass(m.id) as juros, 
//     func_correcao_ass(m.id) as correcao, 
//     us.ds_nome as caixa,  
//     m.id_baixa as lote_baixa, 
//     l.ds_documento as documento 
// from fin_movimento as m  
//inner join fin_lote as l on l.id=m.id_lote 
//inner join pes_pessoa as p on p.id=m.id_pessoa 
//inner join pes_pessoa as b on b.id=m.id_beneficiario 
//inner join fin_servicos as se on se.id=m.id_servicos 
//inner join fin_tipo_servico as tp on tp.id=m.id_tipo_servico  
// left join fin_baixa as bx on bx.id=m.id_baixa 
// left join seg_usuario as u on u.id=bx.id_usuario 
// left join pes_pessoa as us on us.id=u.id_pessoa 
// where (m.id_pessoa in (50380) or m.id_beneficiario in (50380)) and m.id_baixa is null and m.is_ativo = true and m.id_servicos not in (select sr.id_servicos from fin_servico_rotina sr where id_rotina = 4) 
// order by m.dt_vencimento asc, p.ds_nome, b.ds_nome, se.ds_descricao 