package main;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
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
    private static final int DEFAULT_WIDTH = 600;
    /**
     * Default height of the gui.
     */
    private static final int DEFAULT_HEIGHT = 400;
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
     * Decimal Format for printing the final amount.
     */
    private static final DecimalFormat amountFormat = new DecimalFormat("0.00");

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Denotes the size of the grid for the calculator
        final int colCount = 3;
        final int rowCount = 3;

        primaryStage.setMinWidth(275);
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

        Label principleLabel = new Label("Principle");
        VBox principleBox = new VBox();
        principleBox.setAlignment(Pos.CENTER);
        principleBox.getChildren().addAll(principleLabel, this.principleField);

        //Configure durationField
        this.durationField = new TextField(Float.toString(this.financialCalculator.getDuration()));
        this.durationField.setPrefHeight(50);
        this.durationField.setPrefWidth(MAX_WIDTH / (double) colCount);
        this.durationField.setPromptText("Enter the duration (in months) here...");

        Label durationLabel = new Label("Duration (months)");
        VBox durationBox = new VBox();
        durationBox.setAlignment(Pos.CENTER);
        durationBox.getChildren().addAll(durationLabel, this.durationField);

        //Configure amountField
        this.amountField = new TextField();
        updateAmount();
        this.amountField.setPrefHeight(50);
        this.amountField.setPrefWidth(MAX_WIDTH / (double) colCount);
        this.amountField.setPromptText("The calculated amount will appear here...");
        this.amountField.setEditable(false);

        Label amountLabel = new Label("Amount after Interest");
        VBox amountBox = new VBox();
        amountBox.setAlignment(Pos.CENTER);
        amountBox.getChildren().addAll(amountLabel, this.amountField);

        //Configure typeChooser
        ObservableList<String> computationOptions = FXCollections.observableArrayList("Compound Interest","Simple Interest");
        ComboBox computationChooser = new ComboBox(computationOptions);

        Label computationLabel = new Label("Pick a calculation type");
        VBox computationBox = new VBox();
        computationBox.setAlignment(Pos.CENTER);
        computationBox.getChildren().addAll(computationLabel, computationChooser);

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

        //Start scene
        primaryStage.setScene(scene);
        primaryStage.setTitle("Financial Calculator");
        primaryStage.getIcons().add(new Image("file:./src/res/icon.png"));
        primaryStage.show();
    }

    private void updateAmount() {
        float amount = this.financialCalculator.compute();
        this.amountField.setText(this.amountFormat.format(amount));
    }

    private void updateInterest(float interestRate) {
        this.financialCalculator.setInterestRate(interestRate);
        this.interestLabel.setText("Interest Rate: " + interestRate + "%");
        updateAmount();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
