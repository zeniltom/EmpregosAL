package br.com.empregosal.model;

public class Candidatura {

    private String idCandidatura;
    private String idVaga;
    private String idEmpresa;
    private String idUsuario;
    private String idVagaUsuario;
    private String nomeVaga;
    private String nomeUsuario;
    private String nomeEmpresa;


    public String getIdVagaUsuario() {
        return idVagaUsuario;
    }

    public void setIdVagaUsuario(String idVagaUsuario) {
        this.idVagaUsuario = idVagaUsuario;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNomeVaga() {
        return nomeVaga;
    }

    public void setNomeVaga(String nomeVaga) {
        this.nomeVaga = nomeVaga;
    }

    public Candidatura() {

    }

    public String getIdVaga() {
        return idVaga;
    }

    public void setIdVaga(String idVaga) {
        this.idVaga = idVaga;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;

    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getNomeUsuario() {

        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getIdCandidatura() {
        return idCandidatura;
    }

    public void setIdCandidatura(String idCandidatura) {
        this.idCandidatura = idCandidatura;
    }
}
