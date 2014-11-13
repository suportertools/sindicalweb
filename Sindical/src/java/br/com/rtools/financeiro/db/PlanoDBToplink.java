package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.*;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.AnaliseString;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import javax.persistence.Query;

public class PlanoDBToplink extends DB implements PlanoDB {

    @Override
    public boolean insertPlano(Plano plano) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(plano);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean insertPlano2(Plano2 plano2) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(plano2);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean insertPlano3(Plano3 plano3) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(plano3);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean insertPlano4(Plano4 plano4) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(plano4);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean insertPlano5(Plano5 plano5) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(plano5);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean updatePlano(Plano plano) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(plano);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean updatePlano2(Plano2 plano2) {
        try {
            getEntityManager().merge(plano2);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updatePlano3(Plano3 plano3) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(plano3);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean updatePlano4(Plano4 plano4) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(plano4);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean updatePlano5(Plano5 plano5) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(plano5);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean delete(Plano plano) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(plano);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public Plano pesquisaCodigo(int id) {
        Plano result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Plano.pesquisaID");
            qry.setParameter("pid", id);
            result = (Plano) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select p from Plano p ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    public List pesquisaPerPlano(int id) {
        try {
            Query qry = getEntityManager().createQuery("select p.plano2 from Plano p where p.id = :pid");
            qry.setParameter("pid", id);
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    public List pesquisaPerPlano2(int id) {
        try {
            Query qry = getEntityManager().createQuery("select p.plano3 from Plano2 p where p.id = :pid");
            qry.setParameter("pid", id);
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List pesquisaPlanoSimples() {
        List result = null;
        String textQuery = null;

        textQuery = "select from fin_contas_vw";

        try {
            Query qry = getEntityManager().createQuery(textQuery);
            result = qry.getResultList();
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    @Override
    public List pesquisaPlano(String desc, String por, String como, String plano, int id) {
        List result = null;

        String textQuery = null;
        if (como.equals("T")) {
            if (plano.equals("Plano")) {
                textQuery = "select p from Plano p " + " order by p.numero";
            } else if (plano.equals("Plano2")) {
                textQuery = "select p2 from Plano2 p2 where p2.plano.id = " + id + " order by p2.numero";
            } else if (plano.equals("Plano3")) {
                textQuery = "select p3 from Plano3 p3 where p3.plano2.id = " + id + " order by p3.numero";
            } else if (plano.equals("Plano4")) {
                textQuery = "select p4 from Plano4 p4 where p4.plano3.id = " + id + " order by p3.numero";
            } else if (plano.equals("Plano5")) {
                if (id != -1) {
                    textQuery = "select p5 from Plano5 p5 where p5.plano4.id = " + id + " order by p5.numero";
                } else {
                    textQuery = "select p5 from Plano5 p5" + " order by p5.numero";
                }
            }
            try {
                Query qry = getEntityManager().createQuery(textQuery);
                result = qry.getResultList();
            } catch (Exception e) {
                result = null;
            }
        } else {
            if (como.equals("I")) {
                desc = desc.toLowerCase().toUpperCase() + "%";
            } else if (como.equals("P")) {
                desc = "%" + desc.toLowerCase().toUpperCase() + "%";
            }

            if (plano.equals("Plano")) {
                textQuery = "select p from Plano p where p." + por + " like :desc" + " order by p.numero";
            } else if (plano.equals("Plano2")) {
                textQuery = "select p2 from Plano2 p2 where p2." + por + " like :desc and p2.plano.id = " + id + " order by p2.numero";
            } else if (plano.equals("Plano3")) {
                textQuery = "select p3 from Plano3 p3 where p3." + por + " like :desc and p3.plano2.id = " + id + " order by p3.numero";
            } else if (plano.equals("Plano4")) {
                textQuery = "select p4 from Plano4 p4 where p4." + por + " like :desc and p4.plano3.id = " + id + " order by p4.numero";
            } else if (plano.equals("Plano5")) {
                if (id != -1) {
                    textQuery = "select p5 from Plano5 p5 where p5." + por + " like :desc and p5.plano5.id = " + id + " order by p5.numero";
                } else {
                    textQuery = "select p5 from Plano5 p5 where p5." + por + " like :desc" + " order by p5.numero";
                }
            }

            if (desc != null) {
                try {
                    Query qry = getEntityManager().createQuery(textQuery);
                    qry.setParameter("desc", desc);
                    result = qry.getResultList();
                } catch (Exception e) {
                    result = null;
                }
            }

        }

        return result;
    }

    @Override
    public List pesquisaPorPlano(String desc, String por, String como, String plano) {
        desc = AnaliseString.removerAcentos(desc);
        desc = desc.toUpperCase();
        String textQuery = null;
        if (como.equals("T")) {
            textQuery = "";
        } else if (como.equals("P")) {
            desc = "%" + desc.toLowerCase().toUpperCase() + "%";
            textQuery = "SELECT O.* FROM " + plano + " O WHERE UPPER(TRANSLATE(O." + por + ")) LIKE '" + desc + "' ORDER BY O." + por;
        } else if (como.equals("I")) {
            desc = desc.toLowerCase().toUpperCase() + "%";
            textQuery = "SELECT O.* FROM " + plano + " O WHERE UPPER(TRANSLATE(O." + por + ")) LIKE '" + desc + "' ORDER BY O." + por;
        }
        try {
            Query query = getEntityManager().createNativeQuery(textQuery, Plano5.class);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    @Override
    public List pesquisaPlanos(String plano) {

        List result = null;

        String textQuery = null;
        textQuery = "select p from " + plano + " p " + " order by p.classificador, p.numero";
        try {
            Query qry = getEntityManager().createQuery(textQuery);
            result = qry.getResultList();
        } catch (Exception e) {
            result = new ArrayList();
        }

        return result;
    }
}
