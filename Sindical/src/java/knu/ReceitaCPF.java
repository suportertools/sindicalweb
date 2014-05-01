/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.7
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package knu;

public class ReceitaCPF {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected ReceitaCPF(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(ReceitaCPF obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        knuJNI.delete_ReceitaCPF(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setCod_erro(int value) {
    knuJNI.ReceitaCPF_cod_erro_set(swigCPtr, this, value);
  }

  public int getCod_erro() {
    return knuJNI.ReceitaCPF_cod_erro_get(swigCPtr, this);
  }

  public void setCodigo_controle(String value) {
    knuJNI.ReceitaCPF_codigo_controle_set(swigCPtr, this, value);
  }

  public String getCodigo_controle() {
    return knuJNI.ReceitaCPF_codigo_controle_get(swigCPtr, this);
  }

  public void setCpf(String value) {
    knuJNI.ReceitaCPF_cpf_set(swigCPtr, this, value);
  }

  public String getCpf() {
    return knuJNI.ReceitaCPF_cpf_get(swigCPtr, this);
  }

  public void setData_emissao(String value) {
    knuJNI.ReceitaCPF_data_emissao_set(swigCPtr, this, value);
  }

  public String getData_emissao() {
    return knuJNI.ReceitaCPF_data_emissao_get(swigCPtr, this);
  }

  public void setDesc_erro(String value) {
    knuJNI.ReceitaCPF_desc_erro_set(swigCPtr, this, value);
  }

  public String getDesc_erro() {
    return knuJNI.ReceitaCPF_desc_erro_get(swigCPtr, this);
  }

  public void setDigito_verificador(String value) {
    knuJNI.ReceitaCPF_digito_verificador_set(swigCPtr, this, value);
  }

  public String getDigito_verificador() {
    return knuJNI.ReceitaCPF_digito_verificador_get(swigCPtr, this);
  }

  public void setNome(String value) {
    knuJNI.ReceitaCPF_nome_set(swigCPtr, this, value);
  }

  public String getNome() {
    return knuJNI.ReceitaCPF_nome_get(swigCPtr, this);
  }

  public void setSituacao(String value) {
    knuJNI.ReceitaCPF_situacao_set(swigCPtr, this, value);
  }

  public String getSituacao() {
    return knuJNI.ReceitaCPF_situacao_get(swigCPtr, this);
  }

  public void setHtml(String value) {
    knuJNI.ReceitaCPF_html_set(swigCPtr, this, value);
  }

  public String getHtml() {
    return knuJNI.ReceitaCPF_html_get(swigCPtr, this);
  }

  public ReceitaCPF() {
    this(knuJNI.new_ReceitaCPF(), true);
  }

}
