package com.example.currencyapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.plaf.ColorUIResource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HelloController {
    public ChoiceBox<String> baseChoiceBox;
    public TextField rateTextField;
    public ChoiceBox<String> conversionChoiceBox;
    public TextField Amount;
    public TextField ConvertedAmount;
    public LineChart<Number,Number> CurrencyChart;

    public void initialize() throws Exception {
        baseChoiceBox.getItems().addAll("EUR", "USD", "GBP", "JPY", "AUD", "CAD", "CHF", "CNY", "BRL", "INR");
        conversionChoiceBox.getItems().addAll("EUR", "USD", "GBP", "JPY", "AUD", "CAD", "CHF", "CNY", "BRL", "INR");

        baseChoiceBox.setValue("EUR");
        conversionChoiceBox.setValue("USD");



        CurrencyChart.getXAxis().setLabel("Time Sam");
        CurrencyChart.getYAxis().setLabel("Amount Sam");

        XYChart.Series<Number,Number> series = new XYChart.Series<Number,Number>();
        series.setName("No of schools in an year");

        series.getData().add(new XYChart.Data<Number,Number>(1970, 15));
        //series.getData().add(new XYChart.Data<Integer,Integer>(1980, 30));
        //series.getData().add(new XYChart.Data<Integer,Integer>(1990, 60));
        //series.getData().add(new XYChart.Data<Integer,Integer>(2000, 120));
        //series.getData().add(new XYChart.Data<Integer,Integer>(2013, 240));
        //series.getData().add(new XYChart.Data<Integer,Integer>(2014, 300));

//Setting the data to Line chart
        CurrencyChart.getData().clear();
        CurrencyChart.getData().add(series);

    }

    public void getRate() throws Exception {
        String baseTypedIn = baseChoiceBox.getValue();
        String conversionTypedIn = conversionChoiceBox.getValue();

        String yourAPIurl = "https://api.frankfurter.dev/v1/latest?base=" + baseTypedIn + "&symbols=" + conversionTypedIn;
        String yourAPIkey = "YOUR API KEY";
        URL APIurl = new URL(yourAPIurl);
        HttpURLConnection APIconnection = (HttpURLConnection) APIurl.openConnection();
        APIconnection.setRequestMethod("GET");
        APIconnection.setRequestProperty("Accept", "application/json");
        APIconnection.setRequestProperty("Authorization", yourAPIkey);
        InputStreamReader APIinStream = new InputStreamReader(APIconnection.getInputStream());
        BufferedReader APIreader = new BufferedReader(APIinStream);
        StringBuilder JSONstring = new StringBuilder();
        String line;
        while ((line = APIreader.readLine()) != null) {
            JSONstring.append(line);
        }
        APIreader.close();
        System.out.println(JSONstring);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(JSONstring.toString());
        String amountString = jsonNode.get("amount").toString();
        String baseString = jsonNode.get("base").toString();

        String rateString = null;
        try {
            rateString = jsonNode.get("rates").get(conversionTypedIn).toString();
        } catch (Exception ex) {
            System.out.println("NO " + conversionTypedIn + " rate");
        }

        CurrencyData myData = new CurrencyData();
        RatesData myRates = new RatesData();
        myData.setRatesData(myRates);
        myData.setAmount(Float.parseFloat(amountString));
        myData.setBase(baseString);
        Float rateWeGot = 0f;

        if (rateString != null) {
            rateWeGot = Float.parseFloat(rateString);
        }

        System.out.println("OBJECT: " + myData);

        rateTextField.setText(String.valueOf(rateWeGot));
    }

    public void convertCurrency() throws Exception {
        String amountText = Amount.getText();

        if (amountText.isEmpty()) {
            ConvertedAmount.setText("Enter amount");
            return;
        }

        float amountValue = Float.parseFloat(amountText);
        String rateText = rateTextField.getText();

        if (rateText.isEmpty() || rateText.equals("0.0")) {
            ConvertedAmount.setText("Get rate first");
            return;
        }

        float rate = Float.parseFloat(rateText);
        float convertedValue = amountValue * rate;

        ConvertedAmount.setText(String.format("%.2f", convertedValue));
    }

    public void setQuickAmount(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonText = clickedButton.getText();
        Amount.setText(buttonText);
    }
}