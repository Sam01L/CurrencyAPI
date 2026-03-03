package com.example.currencyapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.chart.XYChart;

import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.net.HttpURLConnection;
import java.net.URL;

public class HelloController {
    public ChoiceBox<String> baseChoiceBox;
    public TextField rateTextField;
    public ChoiceBox<String> conversionChoiceBox;
    public TextField Amount;
    public TextField ConvertedAmount;
    public javafx.scene.chart.LineChart<Number, Number> CurrencyChart;

    private List<LineChart> historicalData;
    private static final String DATA_FILE = "currency_history.dat";

    public void initialize() throws Exception {
        baseChoiceBox.getItems().addAll("EUR", "USD", "GBP", "JPY", "AUD", "CAD", "CHF", "CNY", "BRL", "INR");
        conversionChoiceBox.getItems().addAll("EUR", "USD", "GBP", "JPY", "AUD", "CAD", "CHF", "CNY", "BRL", "INR");

        baseChoiceBox.setValue("EUR");
        conversionChoiceBox.setValue("USD");

        loadHistoricalData();

        baseChoiceBox.setOnAction(event -> updateChart());
        conversionChoiceBox.setOnAction(event -> updateChart());

        CurrencyChart.getXAxis().setLabel("Days Ago");
        CurrencyChart.getYAxis().setLabel("Exchange Rate");

        updateChart();
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

        saveRateToHistory(baseTypedIn, conversionTypedIn, rateWeGot);
        updateChart();
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

    private void saveRateToHistory(String base, String conversion, Float rate) {
        LocalDate today = LocalDate.now();

        LineChart newData = new LineChart(base, conversion, today, rate);

        boolean updated = false;
        for (int i = 0; i < historicalData.size(); i++) {
            LineChart existing = historicalData.get(i);
            if (existing.getBaseCurrency().equals(base) &&
                    existing.getConversionCurrency().equals(conversion) &&
                    existing.getDate().equals(today)) {
                historicalData.set(i, newData);
                updated = true;
                break;
            }
        }

        if (!updated) {
            historicalData.add(newData);
        }

        saveHistoricalData();
    }

    private void updateChart() {
        String base = baseChoiceBox.getValue();
        String conversion = conversionChoiceBox.getValue();

        List<LineChart> filteredData = historicalData.stream()
                .filter(data -> data.getBaseCurrency().equals(base) &&
                        data.getConversionCurrency().equals(conversion))
                .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                .collect(Collectors.toList());

        CurrencyChart.getData().clear();

        if (filteredData.isEmpty()) {
            return;
        }

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(base + " to " + conversion);

        LocalDate today = LocalDate.now();

        for (LineChart data : filteredData) {
            long daysAgo = ChronoUnit.DAYS.between(data.getDate(), today);
            series.getData().add(new XYChart.Data<>(daysAgo, data.getRate()));
        }

        CurrencyChart.getData().add(series);
    }

    private void loadHistoricalData() {
        try {
            File file = new File(DATA_FILE);
            if (file.exists()) {
                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                historicalData = (List<LineChart>) in.readObject();
                in.close();
                fileIn.close();
                System.out.println("Loaded " + historicalData.size() + " historical records");
            } else {
                historicalData = new ArrayList<>();
                System.out.println("No existing data file, starting fresh");
            }
        } catch (Exception e) {
            historicalData = new ArrayList<>();
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    private void saveHistoricalData() {
        try {
            FileOutputStream fileOut = new FileOutputStream(DATA_FILE);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(historicalData);
            out.close();
            fileOut.close();
            System.out.println("Saved " + historicalData.size() + " historical records");
        } catch (Exception e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
}