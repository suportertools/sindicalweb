package br.com.rtools.homologacao.db;

import java.util.List;

public interface HorariosDB {

    public List pesquisaTodosPorFilial(int idFilial, int idDiaSemana);

    public List pesquisaPorHorarioFilial(int idFilial, String horario, int idSemana);
}
