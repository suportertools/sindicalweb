package br.com.rtools.relatorios.db;

import br.com.rtools.relatorios.Relatorios;
import java.util.List;
import java.util.Vector;

public interface RelatorioSociosDB {

    public List listaEmpresaDoSocio();

    public List listaCidadeDoSocio();

    public List listaCidadeDaEmpresa();

    public List listaSPSocios();

    public List listaSPAcademia();

    public List listaSPEscola();

    public List listaSPConvenioMedico();

    public List pesquisaSocios(Relatorios relatorio, boolean booMatricula, int matricula_inicial, int matricula_final, boolean booIdade, int idade_inicial, int idade_final, boolean booGrupo, String ids_gc, String ids_c,
        boolean booSexo, String tipo_sexo, boolean booGrau, String ids_parentesco, boolean booFoto, String tipo_foto, boolean booCarteirinha, String tipo_carteirinha,
        boolean booTipoPagamento, String ids_pagamento, boolean booCidadeSocio, String ids_cidade_socio, boolean booCidadeEmpresa, String ids_cidade_empresa,
        boolean booAniversario, String meses_aniversario, String dia_inicial, String dia_final, boolean booData, String dt_cadastro, String dt_cadastro_fim, String dt_recadastro,
        String dt_recadastro_fim, String dt_demissao, String dt_demissao_fim, String dt_admissao_socio, String dt_admissao_socio_fim, String dt_admissao_empresa, String dt_admissao_empresa_fim, boolean booVotante, String tipo_votante,
        boolean booEmail, String tipo_email, boolean booTelefone, String tipo_telefone, boolean booEstadoCivil, String tipo_estado_civil, boolean booEmpresas, String tipo_empresa, int id_juridica, Integer minQtdeFuncionario, Integer maxQtdeFuncionario, 
        String data_aposentadoria, String data_aposentadoria_fim, String ordem, String tipoCarencia, Integer carenciaDias, String situacao, boolean booBiometria, String tipoBiometria, boolean booDescontoFolha, String tipoDescontoFolha,
        String data_atualizacao, String data_atualizacao_fim);
    public List<Vector> listaSociosInativos(boolean comDependentes, boolean chkInativacao, boolean chkFiliacao, String dt_inativacao_i, String dt_inativacao_f, String dt_filiacao_i, String dt_filiacao_f, int categoria, int grupoCategoria, String ordernarPor);
}
