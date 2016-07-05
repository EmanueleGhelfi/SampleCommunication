package ClientPackage.View.GUIResources.CustomComponent;

import javafx.beans.binding.NumberBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import java.text.NumberFormat;

/**
 * Created by Emanuele on 30/05/2016.
 */
public class NumberSpinner extends HBox {

    public static final String ARROW = "NumberSpinnerArrow";
    public static final String NUMBER_FIELD = "NumberField";
    public static final String NUMBER_SPINNER = "NumberSpinner";
    public static final String SPINNER_BUTTON_UP = "SpinnerButtonUp";
    public static final String SPINNER_BUTTON_DOWN = "SpinnerButtonDown";
    private final String BUTTONS_BOX = "ButtonsBox";
    private final double ARROW_SIZE = 4;
    private final Button incrementButton;
    private final Button decrementButton;
    private final NumberBinding buttonHeight;
    private final NumberBinding spacing;
    private final NumberTextField numberField;
    private final ObjectProperty<Integer> stepWitdhProperty = new SimpleObjectProperty<>();

    public NumberSpinner() {
        this(0, 0);
    }

    public NumberSpinner(int value, int stepWidth) {
        this(value, stepWidth, NumberFormat.getInstance());
    }

    public NumberSpinner(int value, int stepWidth, NumberFormat nf) {
        setId(NumberSpinner.NUMBER_SPINNER);
        stepWitdhProperty.set(stepWidth);
        this.numberField = new NumberTextField(value, nf);
        this.numberField.setId(NumberSpinner.NUMBER_FIELD);
        this.numberField.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.DOWN) {
                    NumberSpinner.this.decrement();
                    keyEvent.consume();
                }
                if (keyEvent.getCode() == KeyCode.UP) {
                    NumberSpinner.this.increment();
                    keyEvent.consume();
                }
            }
        });
        Path arrowUp = new Path();
        arrowUp.setId(NumberSpinner.ARROW);
        arrowUp.getElements().addAll(new MoveTo(-this.ARROW_SIZE, 0), new LineTo(this.ARROW_SIZE, 0),
                new LineTo(0, -this.ARROW_SIZE), new LineTo(-this.ARROW_SIZE, 0));
        arrowUp.setMouseTransparent(true);

        Path arrowDown = new Path();
        arrowDown.setId(NumberSpinner.ARROW);
        arrowDown.getElements().addAll(new MoveTo(-this.ARROW_SIZE, 0), new LineTo(this.ARROW_SIZE, 0),
                new LineTo(0, this.ARROW_SIZE), new LineTo(-this.ARROW_SIZE, 0));
        arrowDown.setMouseTransparent(true);
        this.buttonHeight = this.numberField.heightProperty().subtract(3).divide(2);
        this.spacing = this.numberField.heightProperty().subtract(2).subtract(this.buttonHeight.multiply(2));
        VBox buttons = new VBox();
        buttons.setId(this.BUTTONS_BOX);
        this.incrementButton = new Button();
        this.incrementButton.setId(NumberSpinner.SPINNER_BUTTON_UP);
        this.incrementButton.prefWidthProperty().bind(this.numberField.heightProperty());
        this.incrementButton.minWidthProperty().bind(this.numberField.heightProperty());
        this.incrementButton.maxHeightProperty().bind(this.buttonHeight.add(this.spacing));
        this.incrementButton.prefHeightProperty().bind(this.buttonHeight.add(this.spacing));
        this.incrementButton.minHeightProperty().bind(this.buttonHeight.add(this.spacing));
        this.incrementButton.setFocusTraversable(false);
        this.incrementButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent ae) {
                NumberSpinner.this.increment();
                ae.consume();
            }
        });
        StackPane incPane = new StackPane();
        incPane.getChildren().addAll(this.incrementButton, arrowUp);
        incPane.setAlignment(Pos.CENTER);
        this.decrementButton = new Button();
        this.decrementButton.setId(NumberSpinner.SPINNER_BUTTON_DOWN);
        this.decrementButton.prefWidthProperty().bind(this.numberField.heightProperty());
        this.decrementButton.minWidthProperty().bind(this.numberField.heightProperty());
        this.decrementButton.maxHeightProperty().bind(this.buttonHeight);
        this.decrementButton.prefHeightProperty().bind(this.buttonHeight);
        this.decrementButton.minHeightProperty().bind(this.buttonHeight);
        this.decrementButton.setFocusTraversable(false);
        this.decrementButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent ae) {
                NumberSpinner.this.decrement();
                ae.consume();
            }
        });

        StackPane decPane = new StackPane();
        decPane.getChildren().addAll(this.decrementButton, arrowDown);
        decPane.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(incPane, decPane);
        getChildren().addAll(this.numberField, buttons);
    }

    /**
     * increment number value by stepWidth
     */
    private void increment() {
        Integer value = this.numberField.getNumber();
        value = value + this.stepWitdhProperty.get();
        this.numberField.setNumber(value);
    }

    /**
     * decrement number value by stepWidth
     */
    private void decrement() {
        Integer value = this.numberField.getNumber();
        value = value - this.stepWitdhProperty.get();
        this.numberField.setNumber(value);
    }

    public ObjectProperty<Integer> numberProperty() {
        return this.numberField.numberProperty();
    }

}