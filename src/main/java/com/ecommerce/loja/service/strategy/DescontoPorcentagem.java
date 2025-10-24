package com.ecommerce.loja.service.strategy;

public class DescontoPorcentagem implements PrecoStrategy {

    private double porcentagem;

    public DescontoPorcentagem(double porcentagem) {
        this.porcentagem = porcentagem;
    }

    @Override
    public double calcular(double precoBase) {
        return precoBase - (precoBase * (porcentagem / 100));
    }
}