package ClientPackage.View.GUIResources.CustomComponent;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Created by Emanuele on 30/05/2016.
 */
public class NumberTextField extends TextField {

    private final NumberFormat nf;
    private final ObjectProperty<Integer> number = new SimpleObjectProperty<>();

    public NumberTextField(int value) {
        this(value, NumberFormat.getInstance());
        this.initHandlers();
    }

    public NumberTextField(int value, NumberFormat nf) {
        this.nf = nf;
        this.initHandlers();
        this.setNumber(value);
    }

    private void initHandlers() {
        // try to parse when focus is lost or RETURN is hit
        this.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                NumberTextField.this.parseAndFormatInput();
            }
        });
        this.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue.booleanValue()) {
                    NumberTextField.this.parseAndFormatInput();
                }
            }
        });
        // Set text in field if BigDecimal property is changed from outside.
        this.numberProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> obserable, Integer oldValue, Integer newValue) {
                NumberTextField.this.setText(NumberTextField.this.nf.format(newValue));
            }
        });
    }

    /**
     * Tries to parse the user input to a number according to the provided
     * NumberFormat
     */
    private void parseAndFormatInput() {
        try {
            String input = this.getText();
            if (input == null || input.length() == 0) {
                return;
            }
            Number parsedNumber = this.nf.parse(input);
            int newValue = Integer.parseInt(input);
            this.setNumber(newValue);
            this.selectAll();
        } catch (ParseException ex) {
            // If parsing fails keep old number
            this.setText(this.nf.format(this.number.get()));
        }
    }

    public final Integer getNumber() {
        return this.number.get();
    }

    public final void setNumber(int value) {
        this.number.set(value);
    }

    public ObjectProperty<Integer> numberProperty() {
        return this.number;
    }

}
