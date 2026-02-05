package com.example.currencyapi;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RatesData {
    @JsonProperty("USD")
    public Float USD;

    public RatesData() {
    }

    public RatesData(Float USD) {
        this.USD = USD;
    }

    public Float getUSD() {
        return USD;
    }

    public void setUSD(String USD) {
        this.USD = Float.parseFloat(USD);
    }

    public void setUSD(Float USD) {
        this.USD = USD;
    }

    public Float getOutput() {
        return USD;
    }

    public void setOutput(String USD) {
        this.USD = Float.parseFloat(USD);
    }

    public void setOutput(Float USD) {
        this.USD = USD;
    }
    /*public String getUsDollarRate() {
        return usDollarRate;
    }

    public void setUsDollarRate(String usDollarRate) {
        this.usDollarRate = usDollarRate;
    }
    String usDollarRate;

     */
}
