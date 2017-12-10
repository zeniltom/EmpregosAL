package br.com.empregosal.model;

import com.google.firebase.database.DatabaseReference;

import br.com.empregosal.config.ConfiguracaoFirebase;

public class Vaga {

    private String idVaga;
    private String idEmpresa;
    private String descricao;
    private String cargo;
    private String areaProfissional;
    private String tipoContrato;
    private String nivelHierarquico;
    private String nivelEstudos;
    private String jornada;
    private String faixaSalarial;
    private String localizacao;
    private String dataAnuncio;
    private String CEP;
    private String qtd;

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public void salvar() {
        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase();
        referenciaFirebase.child("vagas").child(getIdVaga()).setValue(this);
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

    public String getCEP() {
        return CEP;
    }

    public void setCEP(String CEP) {
        this.CEP = CEP;
    }

    public String getDataAnuncio() {
        return dataAnuncio;
    }

    public void setDataAnuncio(String dataAnuncio) {
        this.dataAnuncio = dataAnuncio;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getFaixaSalarial() {
        return faixaSalarial;
    }

    public void setFaixaSalarial(String faixaSalarial) {
        this.faixaSalarial = faixaSalarial;
    }

    public String getIdVaga() {
        return idVaga;
    }

    public void setIdVaga(String idVaga) {
        this.idVaga = idVaga;
    }

    public String getJornada() {
        return jornada;
    }

    public void setJornada(String jornada) {
        this.jornada = jornada;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getNivelEstudos() {
        return nivelEstudos;
    }

    public void setNivelEstudos(String nivelEstudos) {
        this.nivelEstudos = nivelEstudos;
    }

    public String getNivelHierarquico() {
        return nivelHierarquico;
    }

    public void setNivelHierarquico(String nivelHierarquico) {
        this.nivelHierarquico = nivelHierarquico;
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public String getQtd() {
        return qtd;
    }

    public void setQtd(String qtd) {
        this.qtd = qtd;
    }
}
