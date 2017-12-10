package br.com.empregosal.viacep.org.ceprest.model;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.empregosal.viacep.org.ceprest.library.HTTPRequest;

public class ViaCEP {
    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String localidade;
    private String uf;
    private String unidade;
    private String ibge;
    private String gia;

    public void buscar(String cep) {
        this.cep = cep;

        String url = "http://viacep.com.br/ws/" + cep + "/json/";
        try {
            JSONObject obj = new JSONObject(HTTPRequest.get(url));
            if (!obj.has("erro")) {
                this.cep = obj.getString("cep");
                this.logradouro = obj.getString("logradouro");
                this.complemento = obj.getString("complemento");
                this.bairro = obj.getString("bairro");
                this.localidade = obj.getString("localidade");
                this.uf = obj.getString("uf");
                this.unidade = obj.getString("unidade");
                this.ibge = obj.getString("ibge");
                this.gia = obj.getString("gia");
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getIbge() {
        return ibge;
    }

    public void setIbge(String ibge) {
        this.ibge = ibge;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

}