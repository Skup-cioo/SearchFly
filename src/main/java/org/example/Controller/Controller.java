package org.example.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.Data.City;

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

    private SupportMethods supportMethods;

    @FXML
    private void initialize() {
        supportMethods = new SupportMethods();
        List<String> citiesArray = Arrays.stream(supportMethods.getCities()).map(City::getCityName).collect(Collectors.toList());
        originCity.setItems(FXCollections.observableArrayList(citiesArray));
        destinationCity.setItems(FXCollections.observableArrayList(citiesArray));
        searchButton.setDisable(true);
    }

    @FXML
    public void searchResults() {
        String cheapestResult = "";
        Integer minDays = Integer.parseInt(minDaysStayIn.getText());
        Integer maxDays = Integer.parseInt(maxDaysStayIn.getText());
        LocalDate startRange = startDate.getValue();
        LocalDate endRange = endDate.getValue();
        City.valueOf(originCity.getValue());
        City origin = City.valueOf(originCity.getValue());
        City destination = City.valueOf(destinationCity.getValue());
        try {
            if (minDays.equals(maxDays)) {
                cheapestResult = supportMethods.getCore().cheapestFlyBothSides(startRange, endRange, origin, destination, 1, maxDays).toString();
            } else {
                cheapestResult = supportMethods.getCore().cheapestFlyBothSidesInRange(startRange, endRange, origin, destination, 1, minDays, maxDays).toString();
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        resultLabel.setText(String.format("We found the best option %s", cheapestResult));
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
