package br.com.rtools.associativo.beans;

import br.com.rtools.financeiro.Caixa;
import br.com.rtools.financeiro.db.FinanceiroDBToplink;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@ManagedBean
@SessionScoped
public class reciboGeradoBean implements Serializable {

    private TreeNode root;
    private TreeNode selectedRoot;
    private List<DataObject> listaArquivo = new ArrayList<>();

    public reciboGeradoBean() {
        root = new DefaultTreeNode("root", null);

        List<Caixa> listaCaixa = (new FinanceiroDBToplink()).listaCaixa();

        for (Caixa caixa : listaCaixa) {
            //TreeNode tn_caixa = new DefaultTreeNode("Caixa - " + caixa.getCaixa() + " " + caixa.getDescricao(), root);  
            TreeNode tn_caixa = new DefaultTreeNode(caixa.getCaixa() + " - " + caixa.getDescricao(), root);

            String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/" + "Arquivos/recibo/" + caixa.getCaixa() + "/");
            File file_caminho = new File(caminho + "/");
            File lista_files[] = file_caminho.listFiles();
            if (lista_files != null) {
                for (File lista_file : lista_files) {
                    File file_data = new File(caminho + "/" + lista_file.getName() + "/");

                    TreeNode tn_data = new DefaultTreeNode(lista_file.getName(), tn_caixa);

                    //File lista_datas[] = file_data.listFiles();
                    // LISTA ARQUIVOS DENTRO DAS DATAS
//                    for (File lista_data : lista_datas) {
//                        TreeNode tn_file = new DefaultTreeNode(lista_data.getName(), tn_data);
//                        
//                    }    
                }
            }

        }

    }

    public String view(DataObject dob) throws IOException {
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        Registro reg = (Registro) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(1, "Registro");
        String path_recibo = reg.getUrlPath() + "/Sindical/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/recibo/" + dob.getArgumento1().toString() + "/" + ((File) dob.getArgumento0()).getName();
        response.sendRedirect(path_recibo);
        return null;
    }

    public void selected() {
        if (selectedRoot != null && !selectedRoot.getParent().getRowKey().equals("root")) {
            listaArquivo.clear();
            //Caixa caixa = (Caixa)selectedRoot.getData();
            //String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/"+"Arquivos/recibo/"+caixa.getCaixa()+"/");

            String[] split = selectedRoot.getParent().getData().toString().split("-");
            String cx = split[0].trim();
            //String cx = selectedRoot.getParent().getData().toString().sreplaceFirst("-", "");
            String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/" + "Arquivos/recibo/" + cx + "/");
            String path_recibo = cx + "/" + selectedRoot.getData().toString();

            File file_data = new File(caminho + "/" + selectedRoot.getData().toString() + "/");
            File lista_datas[] = file_data.listFiles();

            for (File lista_data : lista_datas) {
                listaArquivo.add(new DataObject(lista_data, path_recibo));
            }
        } else {
            listaArquivo.clear();
        }
    }

    public TreeNode getRoot() {
        return root;
    }

    public List<DataObject> getListaArquivo() {
        if (listaArquivo.isEmpty()) {

        }
        return listaArquivo;
    }

    public void setListaArquivo(List<DataObject> listaArquivo) {
        this.listaArquivo = listaArquivo;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode getSelectedRoot() {
        return selectedRoot;
    }

    public void setSelectedRoot(TreeNode selectedRoot) {
        this.selectedRoot = selectedRoot;
    }

}
