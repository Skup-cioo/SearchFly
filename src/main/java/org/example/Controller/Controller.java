package org.example.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.example.Data.City;
import org.example.Data.Result;
import org.example.Data.Uri;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Controller {
    @FXML
    private Button searchButton;

    @FXML
    private CheckBox fillAllDataCheckBox;

    @FXML
    private TextField minDaysStayIn;

    @FXML
    private TextField maxDaysStayIn;

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private ComboBox<String> originCity;

    @FXML
    private ComboBox<String> destinationCity;

    @FXML
    private Label resultLabel;

    @FXML
    private Hyperlink linkToFruPage;

    @FXML
    private VBox resultVBox;

    private SupportMethods supportMethods;
    private Result bestResult;

    @FXML
    private void initialize() {
        supportMethods = new SupportMethods();
        List<String> citiesArray = Arrays.stream(supportMethods.getCities()).map(City::getCityName).collect(Collectors.toList());
        originCity.setItems(FXCollections.observableArrayList(citiesArray));
        destinationCity.setItems(FXCollections.observableArrayList(citiesArray));
        searchButton.setDisable(true);
        resultVBox.setVisible(false);
    }

    @FXML
    public void searchResults() {
        String cheapestResult = "";
        Integer minDays = Integer.parseInt(minDaysStayIn.getText());
        Integer maxDays = Integer.parseInt(maxDaysStayIn.getText());
        LocalDate startRange = startDate.getValue();
        LocalDate endRange = endDate.getValue();
        City origin = City.valueOf(originCity.getValue());
        City destination = City.valueOf(destinationCity.getValue());
        try {
            if (minDays.equals(maxDays)) {
                bestResult = supportMethods.getCore().cheapestFlyBothSides(startRange, endRange, origin, destination, 1, maxDays);
            } else {
                bestResult = supportMethods.getCore().cheapestFlyBothSidesInRange(startRange, endRange, origin, destination, 1, minDays, maxDays);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        resultVBox.setVisible(true);
        resultLabel.setText(String.format("We found the best option %s", bestResult.toString()));
    }

    @FXML
    public void openHyperLink(ActionEvent actionEvent) {
        LocalDate startDate = bestResult.getStartDate();
        LocalDate endDate = bestResult.getEndDate();
        City origin = City.valueOf(originCity.getValue());
        City destination = City.valueOf(destinationCity.getValue());

        String flightSearchUrl = String.format("%s/flight_search/from/%s/to/%s/from_code/%s/to_code/%s/dd/%s/rd/%s/ad/1",
                Uri.FRU.getValue(), origin.getPolishName(), destination.getPolishName(),
                origin.getAirportCode(), destination.getAirportCode(), startDate.toString(), endDate.toString());

        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(URI.create(flightSearchUrl));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void checkNumberInput(javafx.scene.input.KeyEvent keyEvent) {
        if (!keyEvent.getCharacter().matches("\\d")) {
            minDaysStayIn.clear();
            maxDaysStayIn.clear();
        }
        supportMethods.getNumbersValueCorrect().setValue(minDaysStayIn.getText() != null && maxDaysStayIn.getText() != null);
    }

    @FXML
    public void checkCitiesInput() {
        supportMethods.getCitiesValueCorrect().setValue(originCity.getValue() != null && destinationCity.getValue() != null && !Objects.equals(destinationCity.getValue(), originCity.getValue()));
    }

    private void checkDatesInput() {
        if ((startDate.getValue() != null && endDate.getValue() != null) && (startDate.getValue().isAfter(LocalDate.now()) && endDate.getValue().isAfter(startDate.getValue()))) {
            supportMethods.getDataValueCorrect().setValue(true);
        }
    }

    @FXML
    public void updateButtonState() {
        checkDatesInput();
        for (BooleanProperty value : supportMethods.getListOfValidators()) {
            if (!value.getValue()) {
                searchButton.setDisable(true);
                fillAllDataCheckBox.setSelected(false);
                return;
            }
        }
        searchButton.setDisable(false);
    }
}
