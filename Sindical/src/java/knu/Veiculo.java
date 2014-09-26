/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.7
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package knu;

public class Veiculo {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected Veiculo(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(Veiculo obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        knuJNI.delete_Veiculo(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setCod_erro(int value) {
    knuJNI.Veiculo_cod_erro_set(swigCPtr, this, value);
  }

  public int getCod_erro() {
    return knuJNI.Veiculo_cod_erro_get(swigCPtr, this);
  }

  public void setDesc_erro(String value) {
    knuJNI.Veiculo_desc_erro_set(swigCPtr, this, value);
  }

  public String getDesc_erro() {
    return knuJNI.Veiculo_desc_erro_get(swigCPtr, this);
  }

  public void setSituacao(String value) {
    knuJNI.Veiculo_situacao_set(swigCPtr, this, value);
  }

  public String getSituacao() {
    return knuJNI.Veiculo_situacao_get(swigCPtr, this);
  }

  public void setModelo(String value) {
    knuJNI.Veiculo_modelo_set(swigCPtr, this, value);
  }

  public String getModelo() {
    return knuJNI.Veiculo_modelo_get(swigCPtr, this);
  }

  public void setMarca(String value) {
    knuJNI.Veiculo_marca_set(swigCPtr, this, value);
  }

  public String getMarca() {
    return knuJNI.Veiculo_marca_get(swigCPtr, this);
  }

  public void setCor(String value) {
    knuJNI.Veiculo_cor_set(swigCPtr, this, value);
  }

  public String getCor() {
    return knuJNI.Veiculo_cor_get(swigCPtr, this);
  }

  public void setAno(String value) {
    knuJNI.Veiculo_ano_set(swigCPtr, this, value);
  }

  public String getAno() {
    return knuJNI.Veiculo_ano_get(swigCPtr, this);
  }

  public void setAno_modelo(String value) {
    knuJNI.Veiculo_ano_modelo_set(swigCPtr, this, value);
  }

  public String getAno_modelo() {
    return knuJNI.Veiculo_ano_modelo_get(swigCPtr, this);
  }

  public void setPlaca(String value) {
    knuJNI.Veiculo_placa_set(swigCPtr, this, value);
  }

  public String getPlaca() {
    return knuJNI.Veiculo_placa_get(swigCPtr, this);
  }

  public void setData(String value) {
    knuJNI.Veiculo_data_set(swigCPtr, this, value);
  }

  public String getData() {
    return knuJNI.Veiculo_data_get(swigCPtr, this);
  }

  public void setUf(String value) {
    knuJNI.Veiculo_uf_set(swigCPtr, this, value);
  }

  public String getUf() {
    return knuJNI.Veiculo_uf_get(swigCPtr, this);
  }

  public void setMunicipio(String value) {
    knuJNI.Veiculo_municipio_set(swigCPtr, this, value);
  }

  public String getMunicipio() {
    return knuJNI.Veiculo_municipio_get(swigCPtr, this);
  }

  public void setChassi(String value) {
    knuJNI.Veiculo_chassi_set(swigCPtr, this, value);
  }

  public String getChassi() {
    return knuJNI.Veiculo_chassi_get(swigCPtr, this);
  }

  public Veiculo() {
    this(knuJNI.new_Veiculo(), true);
  }

}