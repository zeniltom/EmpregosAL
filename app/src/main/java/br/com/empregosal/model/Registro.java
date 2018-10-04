package br.com.empregosal.model;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

import br.com.empregosal.config.ConfiguracaoFirebase;

public class Registro implements Serializable{

    private String idCandidatura;
    private String idVaga;
    private String idEmpresa;
    private String idUsuario;
    private String nomeVaga;
    private String nomeUsuario;
    private String nomeEmpresa;

    private String descricao;
    private String cargo;
    private String areaProfissional;
    private String tipoContrato;
    private String nivelHierarquico;
    private String nivelEstudos;
    private String jornada;
    private String faixaSalarial;
    private String localizacao;
    private String data;
    private String dataAnuncio;
    private String CEP;
    private String qtd;
    private String telefone;
    private String email_usuario;

    public String getAreaProfissional() {
        return areaProfissional;
    }

    public void setAreaProfissional(String areaProfissional) {
        this.areaProfissional = areaProfissional;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail_usuario() {
        return email_usuario;
    }

    public void setEmail_usuario(String email_usuario) {
        this.email_usuario = email_usuario;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public String getNivelHierarquico() {
        return nivelHierarquico;
    }

    public void setNivelHierarquico(String nivelHierarquico) {
        this.nivelHierarquico = nivelHierarquico;
    }

    public String getNivelEstudos() {
        return nivelEstudos;
    }

    public void setNivelEstudos(String nivelEstudos) {
        this.nivelEstudos = nivelEstudos;
    }

    public String getJornada() {
        return jornada;
    }

    public void setJornada(String jornada) {
        this.jornada = jornada;
    }

    public String getFaixaSalarial() {
        return faixaSalarial;
    }

    public void setFaixaSalarial(String faixaSalarial) {
        this.faixaSalarial = faixaSalarial;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDataAnuncio() {
        return dataAnuncio;
    }

    public void setDataAnuncio(String dataAnuncio) {
        this.dataAnuncio = dataAnuncio;
    }

    public String getCEP() {
        return CEP;
    }

    public void setCEP(String CEP) {
        this.CEP = CEP;
    }

    public String getQtd() {
        return qtd;
    }

    public void setQtd(String qtd) {
        this.qtd = qtd;
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

    public Registro() {

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

    public void salvar() {
        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase();
        referenciaFirebase.child("registros").child(getIdCandidatura()).setValue(this);
    }

    public String getIdCandidatura() {
        return idCandidatura;
    }

    public void setIdCandidatura(String idCandidatura) {
        this.idCandidatura = idCandidatura;
    }
}
