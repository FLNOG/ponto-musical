package com.ecommerce.loja.service.strategy;

public class DescontoFixo implements PrecoStrategy {

    private double valorDesconto;

    public DescontoFixo(double valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    @Override
    public double calcular(double precoBase) {
        double resultado = precoBase - valorDesconto;
        return resultado < 0 ? 0 : resultado;
    }
}