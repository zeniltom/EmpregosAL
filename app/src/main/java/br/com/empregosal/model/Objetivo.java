package br.com.empregosal.model;

public class Objetivo {

    private String idObjetivo;
    private String tipoJornada;
    private String tipoContrato;
    private String nivelHierarquico;
    private String salarioDesejado;

    public Objetivo() {

    }

    public String getIdObjetivo() {
        return idObjetivo;
    }

    public void setIdObjetivo(String idObjetivo) {
        this.idObjetivo = idObjetivo;
    }

    public String getNivelHierarquico() {
        return nivelHierarquico;
    }

    public void setNivelHierarquico(String nivelHierarquico) {
        this.nivelHierarquico = nivelHierarquico;
    }

    public String getSalarioDesejado() {
        return salarioDesejado;
    }

    public void setSalarioDesejado(String salarioDesejado) {
        this.salarioDesejado = salarioDesejado;
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public String getTipoJornada() {
        return tipoJornada;
    }

    public void setTipoJornada(String tipoJornada) {
        this.tipoJornada = tipoJornada;
    }
}
