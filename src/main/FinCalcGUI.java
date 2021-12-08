package main;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.text.DecimalFormat;


public class FinCalcGUI extends Application {
    /**
     * Creates an object from which we can get the size of the primary screen.
     */
    private final Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    /**
     * Finds the maximum width of the primary screen, used to set preferential sizes for GUI elements.
     */
    private final int MAX_WIDTH = (int) screenBounds.getWidth();
    /**
     * Finds the maximum height of the primary screen, used to set preferential sizes for GUI elements
     */
    private final int MAX_HEIGHT = (int) screenBounds.getHeight();
    /**
     * Default width of the gui.
     */
    private static final int DEFAULT_WIDTH = 700;
    /**
     * Default height of the gui.
     */
    private static final int DEFAULT_HEIGHT = 350;
    /**
     * Decimal Format for printing the final amount.
     */
    private static final DecimalFormat AMOUNT_FORMAT = new DecimalFormat("###,##0.00");
    /**
     * The Label which display the current interest rate.
     */
    private Label interestLabel;
    /**
     * The field where the principle is entered.
     */
    private TextField principleField;
    /**
     * The field where the duration is entered
     */
    private TextField durationField;
    /**
     * The field where the amount after the calculation is shown.
     */
    private TextField amountField;
    /**
     * The calculator to compute with.
     */
    private FinCalc financialCalculator = new FinCalc();

    /**
     * Launch the window.
     *
     * @param primaryStage The primary stage for the FinancialCalculatorGUI Window.
     * @throws Exception An Exception.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        //Denotes the size of the grid for the calculator
        final int colCount = 3;
        final int rowCount = 3;

        primaryStage.setMinWidth(350);
        primaryStage.setMinHeight(300);

        //Create Row and Column Constraints.  Used to set sizes in Calculator grid

        RowConstraints rc = new RowConstraints();
        rc.setPercentHeight(100d / rowCount);
        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(100d / colCount);

        //Create a GridPane for Calculator buttons, etc.
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        //Apply Row and Column Constraints
        for(int i = 0; i < rowCount; i++)
            grid.getRowConstraints().add(rc);
        for(int i = 0; i < colCount; i++)
            grid.getColumnConstraints().add(cc);

        //Create a scene for the calculator
        Scene scene = new Scene(grid, DEFAULT_WIDTH, DEFAULT_HEIGHT);

        //Configure principleField
        this.principleField = new TextField(Float.toString(this.financialCalculator.getPrincipal()));
        this.principleField.setPrefHeight(50);
        this.principleField.setPrefWidth(MAX_WIDTH / (double) colCount);
        this.principleField.setPromptText("Enter the principle value here...");
        this.principleField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if(newValue.length() > 0 && !newValue.contains("d") && !newValue.contains("f")) {
                        financialCalculator.setPrincipal(Float.parseFloat(newValue));
                        updateAmount();
                    } else {
                        principleField.setText(oldValue);
                    }
                } catch(NumberFormatException nfe) { //revert to previous value
                    principleField.setText(oldValue);
                }
            }
        });

        Label principleLabel = new Label("Principal Amount");
        VBox principleBox = new VBox();
        principleBox.setAlignment(Pos.CENTER);
        principleBox.getChildren().addAll(principleLabel, this.principleField);

        //Configure durationField
        this.durationField = new TextField(Integer.toString(this.financialCalculator.getDuration()));
        this.durationField.setPrefHeight(50);
        this.durationField.setPrefWidth(MAX_WIDTH / (double) colCount);
        this.durationField.setPromptText("Enter the duration (in months) here...");
        this.durationField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    durationField.setText(newValue.replaceAll("[^\\d]", ""));
                } else if(durationField.getLength() > 0){
                    financialCalculator.setDuration(Integer.parseInt(durationField.getText()));
                    updateAmount();
                }
            }
        });

        Label durationLabel = new Label("Duration (months)");
        VBox durationBox = new VBox();
        durationBox.setAlignment(Pos.CENTER);
        durationBox.getChildren().addAll(durationLabel, this.durationField);

        //Configure amountField
        this.amountField = new TextField();
        this.amountField.setPrefHeight(50);
        this.amountField.setPrefWidth(MAX_WIDTH / (double) colCount);
        this.amountField.setPromptText("The calculated amount will appear here...");
        this.amountField.setEditable(false);
        updateAmount();

        Label amountLabel = new Label("Amount after Interest");
        VBox amountBox = new VBox();
        amountBox.setAlignment(Pos.CENTER);
        amountBox.getChildren().addAll(amountLabel, this.amountField);

        //Configure typeChooser
        ObservableList<String> computationOptions = FXCollections.observableArrayList("Simple Interest","Compound Interest");
        ComboBox computationChooser = new ComboBox(computationOptions);
        computationChooser.getSelectionModel().selectFirst();
        computationChooser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                switch ((String)computationChooser.getSelectionModel().getSelectedItem())  {
                    case "Simple Interest" :
                        financialCalculator.setOption(FinCalc.computationOptions.SIMPLE);
                        updateAmount();
                        break;
                    case "Compound Interest" :
                        financialCalculator.setOption(FinCalc.computationOptions.COMPOUND);
                        updateAmount();
                        break;
                }
            }
        });

        Label computationLabel = new Label("Pick a Calculation Type");
        Label computationKeyLabel = new Label("Press 'I' to Switch Calculation Type");
        VBox computationBox = new VBox();
        computationBox.setAlignment(Pos.CENTER);
        computationBox.getChildren().addAll(computationLabel, computationChooser, computationKeyLabel);

        //Create interest slider
        Slider interestSlider = new Slider(0, 100, 0);
        interestSlider.setShowTickMarks(true);
        interestSlider.setShowTickLabels(true);
        interestSlider.setMajorTickUnit(1f);
        interestSlider.setPrefWidth(MAX_WIDTH / (double) colCount);
        interestSlider.setPrefHeight(2 * MAX_HEIGHT / (double) rowCount);
        interestSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldNum, Number newNum) {
                double roundedNum = newNum.doubleValue();
                // Round to 2 decimal places
                roundedNum *= 100;
                roundedNum = Math.round(roundedNum);
                roundedNum /= 100;
                updateInterest((float)roundedNum);
            }
        });

        this.interestLabel = new Label();
        updateInterest(FinCalc.DEFAULT_INTEREST_RATE);
        VBox interestBox = new VBox();
        interestBox.setAlignment(Pos.CENTER);
        interestBox.getChildren().addAll(this.interestLabel, interestSlider);

        //Add items to grid
        grid.add(principleBox, 0, 0);
        grid.add(durationBox, 1, 0);
        grid.add(computationBox,2, 0);
        grid.add(interestBox, 0, 1, 3, 1);
        grid.add(amountBox, 0, 2, 3, 1);

        grid.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        if (event.getCode() == KeyCode.I) {
                            if (computationChooser.getSelectionModel().getSelectedIndex() >= (computationOptions.size() - 1)) {
                                computationChooser.getSelectionModel().selectFirst();
                            } else {
                                computationChooser.getSelectionModel().select(computationChooser.getSelectionModel().getSelectedIndex() + 1);
                            }
                        }
                        event.consume();
                    }
                });
                /*
                scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        if (event.getCode() == KeyCode.C)
                            if (computationChooser.getSelectionModel().getSelectedIndex() == computationChooser.getChildrenUnmodifiable().size() - 1)
                                computationChooser.getSelectionModel().selectFirst();
                            else
                                computationChooser.getSelectionModel().select(computationChooser.getSelectionModel().getSelectedIndex() + 1);
                    }
                }); */

        //Start scene
        primaryStage.setScene(scene);
        primaryStage.setTitle("Financial Calculator");
        primaryStage.getIcons().add(new Image("file:./src/res/icon.png"));
        primaryStage.show();
    }

    /**
     * Computes the new amount after interest.
     */
    private void updateAmount() {
        float amount = this.financialCalculator.compute();
        this.amountField.setText("$" + AMOUNT_FORMAT.format(amount));
    }

    /**
     * Updates the interest rate of financialCalculator and the interestLabel.
     *
     * @param interestRate The new interest rate.
     */
    private void updateInterest(float interestRate) {
        this.financialCalculator.setInterestRate(interestRate/100);
        this.interestLabel.setText("Interest Rate (Monthly): " + interestRate + "%");
        updateAmount();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
