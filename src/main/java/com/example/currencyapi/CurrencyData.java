package com.example.currencyapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;

//@JsonIgnoreProperties(value = {"USD"})

public class CurrencyData {
    @JsonProperty("amount")
    Float amount;
    @JsonProperty("base")
    String base;
    @JsonProperty("date")
    LocalDate date;
    @JsonProperty("rates")
    RatesData ratesData;

    public Float getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = Float.parseFloat(amount);
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = LocalDate.parse(date);
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public RatesData getRatesData() {
        return ratesData;
    }

    public void setRatesData(RatesData ratesData) {
        this.ratesData = ratesData;
    }



/*{.
        "amount": 1,
        "base": "EUR",
        "date": "2026-02-02",
        "rates": {
        "USD": 1.184
        }
        }
        /
 */

    public String toString() {
        return "CurrencyData: amount=" + amount +" Sam Liu:";
    }

}