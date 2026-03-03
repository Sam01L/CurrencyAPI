package com.example.currencyapi;

import java.io.Serializable;
import java.time.LocalDate;

public class LineChart implements Serializable {
    private String baseCurrency;
    private String conversionCurrency;
    private LocalDate date;
    private Float rate;

    public LineChart() {
    }

    public LineChart(String baseCurrency, String conversionCurrency, LocalDate date, Float rate) {
        this.baseCurrency = baseCurrency;
        this.conversionCurrency = conversionCurrency;
        this.date = date;
        this.rate = rate;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getConversionCurrency() {
        return conversionCurrency;
    }

    public void setConversionCurrency(String conversionCurrency) {
        this.conversionCurrency = conversionCurrency;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public String getCurrencyPair() {
        return baseCurrency + "_" + conversionCurrency;
    }
}
