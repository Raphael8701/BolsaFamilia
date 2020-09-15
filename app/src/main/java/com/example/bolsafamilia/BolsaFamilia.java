package com.example.bolsafamilia;

public class BolsaFamilia {
    private String municipio, estado_sigla, estado;
    private int beneficiarios;
    private double valorPago;

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getEstadoSigla() { return estado_sigla;}

    public void setEstadoSigla(String estado_sigla) {
        this.estado_sigla = estado_sigla;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getBeneficiarios() {
        return beneficiarios;
    }

    public void setBeneficiarios(int beneficiarios) {
        this.beneficiarios = beneficiarios;
    }

    public double getTotalPago() {
        return valorPago;
    }

    public void setTotalPago(double valorPago) {
        this.valorPago = valorPago;
    }
}
