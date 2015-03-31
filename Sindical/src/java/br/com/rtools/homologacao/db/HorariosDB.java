package br.com.rtools.homologacao.db;

import java.util.List;

public interface HorariosDB {

    public List pesquisaTodosPorFilial(int idFilial, int idDiaSemana);

    public List pesquisaPorHorarioFilial(int idFilial, String horario, int idSemana);

    public List listaHorariosAgrupadosPorFilialSemana(Integer idFilial, Integer idSemana);

    public List pesquisaPorHorarioFilial(Integer idFilial, String horario);
}
