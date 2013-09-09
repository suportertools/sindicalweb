package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.ServicoRotina;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.ServicoRotinaDB;
import br.com.rtools.financeiro.db.ServicoRotinaDBToplink;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;

public class ServicoRotinaJSFBean implements java.io.Serializable {

    private int idServicos = 0;
    private int idRotinas = 0;
    private int idIndex = -1;
    private ServicoRotina servicoRotina = new ServicoRotina();
    private List<ServicoRotina> listaServicoRotina = new ArrayList();
    private String msgConfirma = "";
    private List<SelectItem> listaServicos = new ArrayList<SelectItem>();
    private List<SelectItem> listaRotinas = new ArrayList<SelectItem>();

    public List<SelectItem> getListaServicos() {
        if (listaServicos.isEmpty()) {
            ServicosDB db = new ServicosDBToplink();
            List<Servicos> ls = db.pesquisaTodosServicos();
            for (int i = 0; i < ls.size(); i++) {
                listaServicos.add(new SelectItem(new Integer(i),
                        (String) ((Servicos) ls.get(i)).getDescricao(),
                        Integer.toString(((Servicos) ls.get(i)).getId())));
            }
        }
        return listaServicos;
    }

    public List<SelectItem> getListaRotinas() {
        if (listaRotinas.isEmpty()) {
            if (listaServicos.isEmpty()) {
                return listaRotinas;
            }
            ServicoRotinaDB db = new ServicoRotinaDBToplink();
            List list = db.pesquisaTodasRotinasSemServicoOrdenado(Integer.parseInt(listaServicos.get(idServicos).getDescription()));
            for (int i = 0; i < list.size(); i++) {
                listaRotinas.add(new SelectItem(new Integer(i),
                        (String) ((Rotina) list.get(i)).getRotina(),
                        Integer.toString(((Rotina) list.get(i)).getId())));
            }
        }
        return listaRotinas;
    }

    public void limparRotina() {
        listaRotinas.clear();
        idRotinas = 0;
        limparServicoRotina();
    }

    public void limparServicoRotina() {
        listaServicoRotina.clear();
        msgConfirma = "";
    }

    public String adicionar() {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        ServicoRotinaDB servicoRotinaDB = new ServicoRotinaDBToplink();
        if (listaServicos.isEmpty()) {
            msgConfirma = "Serviços não existe!";
            return null;
        }
        if (listaRotinas.isEmpty()) {
            msgConfirma = "Rotina não existe!";
            return null;
        }
        servicoRotina.setServicos((Servicos) salvarAcumuladoDB.pesquisaCodigo(Integer.parseInt(listaServicos.get(idServicos).getDescription()), "Servicos"));
        servicoRotina.setRotina((Rotina) salvarAcumuladoDB.pesquisaCodigo(Integer.parseInt(listaRotinas.get(idRotinas).getDescription()), "Rotina"));
        if (servicoRotinaDB.existeServicoRotina(servicoRotina.getServicos().getId(), servicoRotina.getRotina().getId())) {
            msgConfirma = "Serviço Rotina já existe!";
            return null;
        }
        salvarAcumuladoDB.abrirTransacao();
        if (salvarAcumuladoDB.inserirObjeto(servicoRotina)) {
            NovoLog log = new NovoLog();
            log.novo("Novo registro", "Serviço Rotina inserido " + servicoRotina.getId() + " - Serviços: " + servicoRotina.getServicos().getId() + " - " + servicoRotina.getServicos().getDescricao() + " - Rotina: " + servicoRotina.getRotina().getId() + " - " + servicoRotina.getRotina().getRotina());
            salvarAcumuladoDB.comitarTransacao();
            msgConfirma = "Registro adicionado!";
            listaServicoRotina.clear();
            listaRotinas.clear();
        } else {
            salvarAcumuladoDB.desfazerTransacao();
            msgConfirma = "Erro ao Salvar!";
        }
        servicoRotina = new ServicoRotina();
        return null;
    }

    public String excluir(ServicoRotina sr) {
        if (sr.getId() != -1) {
            servicoRotina = sr;
        }
        if (sr.getId() == -1) {
            msgConfirma = "";
            return null;            
        }
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        servicoRotina = (ServicoRotina) salvarAcumuladoDB.pesquisaCodigo(servicoRotina.getId(), "ServicoRotina");
        salvarAcumuladoDB.abrirTransacao();
        if (salvarAcumuladoDB.deletarObjeto(servicoRotina)) {
            salvarAcumuladoDB.comitarTransacao();
            NovoLog log = new NovoLog();
            log.novo("Excluido", "Serviço Rotina excluído " + servicoRotina.getId() + " - Serviços: " + servicoRotina.getServicos().getId() + " - " + servicoRotina.getServicos().getDescricao() + " - Rotina: " + servicoRotina.getRotina().getId() + " - " + servicoRotina.getRotina().getRotina());
            msgConfirma = "Registro excluído!";
            listaServicoRotina.clear();
            listaRotinas.clear();
        } else {
            salvarAcumuladoDB.desfazerTransacao();
            msgConfirma = "Erro ao Excluir!";
        }
        servicoRotina = new ServicoRotina();
        return null;
    }

    public int getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(int idServicos) {
        this.idServicos = idServicos;
    }

    public int getIdRotinas() {
        return idRotinas;
    }

    public void setIdRotinas(int idRotinas) {
        this.idRotinas = idRotinas;
    }

    public List<ServicoRotina> getListaServicoRotina() {
        if (listaServicoRotina.isEmpty()) {
            ServicoRotinaDB db = new ServicoRotinaDBToplink();
            listaServicoRotina = db.pesquisaServicoRotinaPorServico(Integer.parseInt(getListaServicos().get(idServicos).getDescription()));
        }
        return listaServicoRotina;
    }

    public void setListaServicoRotina(List<ServicoRotina> listaServicoRotina) {
        this.listaServicoRotina = listaServicoRotina;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public ServicoRotina getServicoRotina() {
        return servicoRotina;
    }

    public void setServicoRotina(ServicoRotina servicoRotina) {
        this.servicoRotina = servicoRotina;
    }
}
