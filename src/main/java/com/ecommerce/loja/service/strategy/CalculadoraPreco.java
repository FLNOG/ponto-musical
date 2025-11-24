package com.ecommerce.loja.service.strategy;

import org.springframework.stereotype.Component;

@Component
public class CalculadoraPreco {

    private PrecoStrategy strategy;

    public void setStrategy(PrecoStrategy strategy) {
        this.strategy = strategy;
    }

    public double calcularPreco(double precoBase) {
        if (strategy == null) return precoBase;
        return strategy.calcular(precoBase);
    }
}