package org.example.Controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import org.example.Data.City;
import org.example.Data.Core;

@Getter
@Setter
public class SupportMethods {
    private City[] cities = City.values();
    private Core core = new Core();
    private BooleanProperty numbersValueCorrect = new SimpleBooleanProperty(false);
    private BooleanProperty dataValueCorrect = new SimpleBooleanProperty(false);
    private BooleanProperty citiesValueCorrect = new SimpleBooleanProperty(false);

    private ObservableList<BooleanProperty> listOfValidators = FXCollections.observableArrayList(
            numbersValueCorrect, dataValueCorrect, citiesValueCorrect);



}
