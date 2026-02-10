package com.example.currencyapi;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RatesData {
    @JsonProperty("AUD")
    public Float AUD;
    @JsonProperty("BRL")
    public Float BRL;
    @JsonProperty("USD")
    public Float USD;
    @JsonProperty("EUR")
    public Float EUR;

    public RatesData() {
    }

    public Float getAUD() {
        return AUD;
    }

    public void setAUD(Float AUD) {
        this.AUD = AUD;
    }

    public Float getBRL() {
        return BRL;
    }

    public void setBRL(Float BRL) {
        this.BRL = BRL;
    }

    public Float getUSD() {
        return USD;
    }

    public void setUSD(Float USD) {
        this.USD = USD;
    }

    public Float getEUR() {
        return EUR;
    }

    public void setEUR(Float EUR) {
        this.EUR = EUR;
    }
}
