package br.com.rtools.academia.lista;

import br.com.rtools.academia.AcademiaGrade;
import br.com.rtools.academia.dao.AcademiaDao;

public class ListaAcademiaGradeSemana {

    private AcademiaGrade academiaGrade;
    private boolean dom;
    private boolean seg;
    private boolean ter;
    private boolean qua;
    private boolean qui;
    private boolean sex;
    private boolean sab;

    public ListaAcademiaGradeSemana() {
        academiaGrade = new AcademiaGrade();
        this.dom = false;
        this.seg = false;
        this.ter = false;
        this.qua = false;
        this.qui = false;
        this.sex = false;
        this.sab = false;
    }

    public ListaAcademiaGradeSemana(AcademiaGrade academiaGrade, boolean dom, boolean seg, boolean ter, boolean qua, boolean qui, boolean sex, boolean sab) {
        this.academiaGrade = academiaGrade;
        this.dom = dom;
        this.seg = seg;
        this.ter = ter;
        this.qua = qua;
        this.qui = qui;
        this.sex = sex;
        this.sab = sab;
    }

    public AcademiaGrade getAcademiaGrade() {
        return academiaGrade;
    }

    public void setAcademiaGrade(AcademiaGrade academiaGrade) {
        this.academiaGrade = academiaGrade;
    }

    public boolean isDom() {
        if (academiaGrade.getId() != -1) {
            AcademiaDao academiaDao = new AcademiaDao();
            this.dom = academiaDao.existeAcademiaSemana(academiaGrade.getId(), 1);
        }
        return dom;
    }

    public void setDom(boolean dom) {
        this.dom = dom;
    }

    public boolean isSeg() {
        if (academiaGrade.getId() != -1) {
            AcademiaDao academiaDao = new AcademiaDao();
            this.seg = academiaDao.existeAcademiaSemana(academiaGrade.getId(), 2);
        }
        return seg;
    }

    public void setSeg(boolean seg) {
        this.seg = seg;
    }

    public boolean isTer() {
        if (academiaGrade.getId() != -1) {
            AcademiaDao academiaDao = new AcademiaDao();
            this.ter = academiaDao.existeAcademiaSemana(academiaGrade.getId(), 3);
        }
        return ter;
    }

    public void setTer(boolean ter) {
        this.ter = ter;
    }

    public boolean isQua() {
        if (academiaGrade.getId() != -1) {
            AcademiaDao academiaDao = new AcademiaDao();
            this.qua = academiaDao.existeAcademiaSemana(academiaGrade.getId(), 4);
        }
        return qua;
    }

    public void setQua(boolean qua) {
        this.qua = qua;
    }

    public boolean isQui() {
        if (academiaGrade.getId() != -1) {
            AcademiaDao academiaDao = new AcademiaDao();
            this.qui = academiaDao.existeAcademiaSemana(academiaGrade.getId(), 5);
        }
        return qui;
    }

    public void setQui(boolean qui) {
        this.qui = qui;
    }

    public boolean isSex() {
        if (academiaGrade.getId() != -1) {
            AcademiaDao academiaDao = new AcademiaDao();
            this.sex = academiaDao.existeAcademiaSemana(academiaGrade.getId(), 6);
        }
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public boolean isSab() {
        if (academiaGrade.getId() != -1) {
            AcademiaDao academiaDao = new AcademiaDao();
            this.sab = academiaDao.existeAcademiaSemana(academiaGrade.getId(), 7);
        }
        return sab;
    }

    public void setSab(boolean sab) {
        this.sab = sab;
    }
}
