package com.exemplo.forgeraven3d.model;

public class Impressao {

    private Long id;
    private String nome;
    private String material;
    private String cor;
    private Double pesoGramas;
    private Integer tempoMinutos;
    private String status;

    public Impressao() {
    }

    public Impressao(Long id, String nome, String material, String cor,
                     Double pesoGramas, Integer tempoMinutos, String status) {
        this.id = id;
        this.nome = nome;
        this.material = material;
        this.cor = cor;
        this.pesoGramas = pesoGramas;
        this.tempoMinutos = tempoMinutos;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }

    public Double getPesoGramas() { return pesoGramas; }
    public void setPesoGramas(Double pesoGramas) { this.pesoGramas = pesoGramas; }

    public Integer getTempoMinutos() { return tempoMinutos; }
    public void setTempoMinutos(Integer tempoMinutos) { this.tempoMinutos = tempoMinutos; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}