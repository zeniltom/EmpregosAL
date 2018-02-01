package br.com.empregosal.model;

public class Experiencia {

    private String idExperiencia;
    private String nomeDaEmpresa;
    private String cargo;
    private String salario;
    private String nivelHieraquico;
    private String areaProfissional;
    private String especialidade;
    private String dataInicio;
    private String dataSaida;
    private String descricaoAtividades;
    private String estado;

    public Experiencia() {

    }

    public String getAreaProfissional() {
        return areaProfissional;
    }

    public void setAreaProfissional(String areaProfissional) {
        this.areaProfissional = areaProfissional;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(String dataSaida) {
        this.dataSaida = dataSaida;
    }

    public String getDescricaoAtividades() {
        return descricaoAtividades;
    }

    public void setDescricaoAtividades(String descricaoAtividades) {
        this.descricaoAtividades = descricaoAtividades;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getIdExperiencia() {
        return idExperiencia;
    }

    public void setIdExperiencia(String idExperiencia) {
        this.idExperiencia = idExperiencia;
    }

    public String getNivelHieraquico() {
        return nivelHieraquico;
    }

    public void setNivelHieraquico(String nivelHieraquico) {
        this.nivelHieraquico = nivelHieraquico;
    }

    public String getNomeDaEmpresa() {
        return nomeDaEmpresa;
    }

    public void setNomeDaEmpresa(String nomeDaEmpresa) {
        this.nomeDaEmpresa = nomeDaEmpresa;
    }

    public String getSalario() {
        return salario;
    }

    public void setSalario(String salario) {
        this.salario = salario;
    }

    @Override
    public String toString() {
        return cargo + ", " + nomeDaEmpresa + "\n" +
                dataInicio + " até " + dataSaida + "\n" +
                " -" + descricaoAtividades;
    }
}
