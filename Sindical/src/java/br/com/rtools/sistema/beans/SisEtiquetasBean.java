package br.com.rtools.sistema.beans;

import br.com.rtools.impressao.Etiquetas;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.sistema.SisEtiquetas;
import br.com.rtools.sistema.dao.SisEtiquetasDao;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.GenericaString;
import br.com.rtools.utilitarios.Jasper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class SisEtiquetasBean implements Serializable {

    private SisEtiquetas sisEtiquetas;
    private List<SisEtiquetas> listSisEtiquetas;

    @PostConstruct
    public void init() {
        sisEtiquetas = new SisEtiquetas();
        listSisEtiquetas = new ArrayList<>();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("sisEtiquetasBean");
        GenericaSessao.remove("usuarioPesquisa");
    }

    public void clear() {
        GenericaSessao.remove("sisEtiquetasBean");
    }

    public void save() {
        if (sisEtiquetas.getTitulo().isEmpty()) {
            GenericaMensagem.warn("Validação", "Informar titulo!");
            return;
        }
        if (sisEtiquetas.getDetalhes().isEmpty()) {
            GenericaMensagem.warn("Validação", "Descrever os detalhes!");
            return;
        }
        if (sisEtiquetas.getSql().isEmpty()) {
            GenericaMensagem.warn("Validação", "Informar código SQL!");
            return;
        }
        Dao dao = new Dao();
        if (sisEtiquetas.getId() == null) {
            if (dao.save(sisEtiquetas, true)) {
                GenericaMensagem.info("Sucesso", "Registro inserido");
                listSisEtiquetas.clear();
            } else {
                GenericaMensagem.warn("Erro", "Ao inserir registro!");
            }
        } else {
            if (dao.update(sisEtiquetas, true)) {
                GenericaMensagem.info("Sucesso", "Registro atualizado");
                listSisEtiquetas.clear();
            } else {
                GenericaMensagem.warn("Erro", "Ao atualizar registro!");
            }
        }
    }

    public void delete(SisEtiquetas se) {
        Dao dao = new Dao();
        sisEtiquetas = se;
        if (dao.delete(sisEtiquetas, true)) {
            GenericaMensagem.info("Sucesso", "Registro excluído");
            listSisEtiquetas.clear();
            sisEtiquetas = new SisEtiquetas();
            return;
        }
        GenericaMensagem.warn("Erro", "Ao excluir registro!");
    }

    public void edit(SisEtiquetas se) {
        sisEtiquetas = se;
    }

    public void print() {
        print(sisEtiquetas);
    }

    public void print(SisEtiquetas se) {
        SisEtiquetasDao sisEtiquetasDao = new SisEtiquetasDao();
        List list = sisEtiquetasDao.execute(se.getId());
        List<Etiquetas> c = new ArrayList<>();
        Etiquetas e = new Etiquetas();
        for (Object list1 : list) {
            try {
                e = new Etiquetas(
                        GenericaString.converterNullToString(((List) list1).get(0)), // Nome
                        GenericaString.converterNullToString(((List) list1).get(1)), // Logradouro
                        GenericaString.converterNullToString(((List) list1).get(2)), // Endereço
                        GenericaString.converterNullToString(((List) list1).get(3)), // Número
                        GenericaString.converterNullToString(((List) list1).get(4)), // Bairro
                        GenericaString.converterNullToString(((List) list1).get(5)), // Cidade
                        GenericaString.converterNullToString(((List) list1).get(6)), // UF
                        GenericaString.converterNullToString(((List) list1).get(7)), // Cep
                        GenericaString.converterNullToString(((List) list1).get(8)), // Complemento
                        GenericaString.converterNullToString(((List) list1).get(9)) /// Observação
                );
            } catch (Exception ex) {
                e = new Etiquetas(
                        GenericaString.converterNullToString(((List) list1).get(0)), // Nome
                        GenericaString.converterNullToString(((List) list1).get(1)), // Logradouro
                        GenericaString.converterNullToString(((List) list1).get(2)), // Endereço
                        GenericaString.converterNullToString(((List) list1).get(3)), // Número
                        GenericaString.converterNullToString(((List) list1).get(4)), // Bairro
                        GenericaString.converterNullToString(((List) list1).get(5)), // Cidade
                        GenericaString.converterNullToString(((List) list1).get(6)), // UF
                        GenericaString.converterNullToString(((List) list1).get(7)), // Cep               
                        GenericaString.converterNullToString(((List) list1).get(8)) /// Complemento
                );
            }
            c.add(e);
        }

        if (c.isEmpty()) {
            GenericaMensagem.info("Sistema", "Nenhum registro encontrado!");
            return;
        }

        Jasper.printReports(
                "/Relatorios/ETIQUETAS.jasper",
                "etiquetas",
                (Collection) c
        );
    }

    public SisEtiquetas getSisEtiquetas() {
        if (GenericaSessao.exists("usuarioPesquisa")) {
            sisEtiquetas.setSolicitante((Usuario) GenericaSessao.getObject("usuarioPesquisa", true));
        }
        return sisEtiquetas;
    }

    public void setSisEtiquetas(SisEtiquetas sisEtiquetas) {
        this.sisEtiquetas = sisEtiquetas;
    }

    public List<SisEtiquetas> getListSisEtiquetas() {
        if (listSisEtiquetas.isEmpty()) {
            SisEtiquetasDao sisEtiquetasDao = new SisEtiquetasDao();
            try {
                listSisEtiquetas = sisEtiquetasDao.findByUser(((Usuario) GenericaSessao.getObject("sessaoUsuario")).getId());
            } catch (Exception e) {

            }
        }
        return listSisEtiquetas;
    }

    public void setListSisEtiquetas(List<SisEtiquetas> listSisEtiquetas) {
        this.listSisEtiquetas = listSisEtiquetas;
    }

}
