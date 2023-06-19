package app;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.Random;

public class V2 extends Application {

    private static final int NUM_REELS = 3;

    private final Random random = new Random();
    private final Label resultLabel = new Label();
    private final Button spinButton = new Button("Spin");
    private final Label accountValue = new Label();
    private static final Label currentBet = new Label();
    private final Button setBet = new Button("Bet");

    private final Rectangle[] reels = new Rectangle[NUM_REELS];
    private final String[] currentSymbols = new String[NUM_REELS];

    private static int bet = 10;
    private static int account = 1000000;

    // Symbols
    private static final String[] SYMBOLS = {"Cherry", "Lemon", "Orange", "Plum", "Bell", "Bar", "Seven", "Diamond"};
    private final Image seven = new Image("/seven.png");
    private final Image bell = new Image("/bell.png");
    private final Image bar = new Image("/bar.png");
    private final Image orange = new Image("/orange.png");
    private final Image lemon = new Image("/lemon.png");
    private final Image cherry = new Image("/cherry.png");
    private final Image plum = new Image("/plum.png");
    private final Image diamond = new Image("/diamond.png");


    @Override
    public void start(Stage primaryStage) {
        GridPane gridPane = createGridPane();
        addReelsToGrid(gridPane);

        HBox resultBox = createResultBox();
        VBox root = new VBox(gridPane, resultBox);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(25);
        root.setPadding(new Insets(25));

        spinButton.setOnAction(event -> spinReels());
        setBet.setOnAction(event -> makeBetWindow());
        resultLabel.setTextFill(Color.RED);

        primaryStage.getIcons().add(new Image("/icon.png"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Slot Machine");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(30);

        for (int i = 0; i < NUM_REELS; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setHgrow(Priority.ALWAYS);
            gridPane.getColumnConstraints().add(columnConstraints);
        }

        return gridPane;
    }

    public void addReelsToGrid(GridPane gridPane) {
        for (int i = 0; i < NUM_REELS; i++) {
            Rectangle reel = createReel();
            reels[i] = reel;
            gridPane.add(reel, i, 0);
        }
    }

    public Rectangle createReel() {
        Rectangle reel = new Rectangle(80, 120);
        reel.setFill(new ImagePattern(seven));
        reel.setStroke(Color.BLACK);
        reel.setStrokeWidth(2);

        return reel;
    }

    public HBox createResultBox() {
        HBox resultBox = new HBox(10);
        resultBox.setAlignment(Pos.CENTER);
        resultBox.getChildren().addAll(spinButton, setBet, resultLabel, accountValue, currentBet);


        return resultBox;
    }

    public void spinReels() {
        if (account - bet < 0) {
            notEnoughMoneyForBet();
        } else {
            account = account - bet;
            accountValue.setText(String.valueOf(account));
            for (int i = 0; i < NUM_REELS; i++) {
                String symbol = SYMBOLS[random.nextInt(SYMBOLS.length)];
                currentSymbols[i] = symbol;
                reels[i].setFill(new ImagePattern(getSymbolImage(symbol)));
            }

            if (isWinningCombination()) {
                for (String currentSymbol : currentSymbols) {
                    if (currentSymbol.equals("Cherry")) {
                        account += bet * 1.1;
                    }
                    if (currentSymbol.equals("Lemon")) {
                        account += bet * 1.4;
                    }
                    if (currentSymbol.equals("Orange")) {
                        account += bet * 2;
                    }
                    if (currentSymbol.equals("Plum")) {
                        account += bet * 3;
                    }
                    if (currentSymbol.equals("Bell")) {
                        account += bet * 3.6;
                    }
                    if (currentSymbol.equals("Bar")) {
                        account += bet * 5;
                    }
                    if (currentSymbol.equals("Diamond")) {
                        account += bet * 8;
                    }
                    if (currentSymbol.equals("Seven")) {
                        account += bet * 10;
                    }
                }
                resultLabel.setTextFill(Color.GREEN);
                resultLabel.setText("You won!");
                popUpWin();
            } else {
                resultLabel.setTextFill(Color.RED);
                resultLabel.setText("Try Again!");
            }
        }
    }

    public Image getSymbolImage(String symbol) {
        return switch (symbol) {
            case "Cherry" -> cherry;
            case "Lemon" -> lemon;
            case "Orange" -> orange;
            case "Plum" -> plum;
            case "Bell" -> bell;
            case "Bar" -> bar;
            case "Seven" -> seven;
            case "Diamond" -> diamond;
            default -> null;
        };
    }

    public boolean isWinningCombination() {
        if (account <= 0) {
            account = bet;
            popUpNoMoney();
        }

        String symbol = currentSymbols[0];
        for (int i = 1; i < NUM_REELS; i++) {
            if (!currentSymbols[i].equals(symbol)) {
                return false;
            }
        }
        return true;
    }

    // Create a PopUp Window to anounce that you have WON!
    public static void popUpWin() {
        Stage popupwindow = new Stage();

        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("You Won!");


        Label label1 = new Label("You Won");

        VBox layout = new VBox(10);

        layout.getChildren().addAll(label1);

        layout.setAlignment(Pos.CENTER);

        Scene scene1 = new Scene(layout, 100, 50);

        popupwindow.setScene(scene1);

        popupwindow.showAndWait();

    }

    // Create a PopUp Window to anounce that you have no money left!
    public static void popUpNoMoney() {
        Stage popupwindow = new Stage();

        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("No Money!");


        Label label1 = new Label("You have no money left");

        VBox layout = new VBox(10);


        layout.getChildren().addAll(label1);

        layout.setAlignment(Pos.CENTER);

        Scene scene1 = new Scene(layout, 150, 50);

        popupwindow.setScene(scene1);

        popupwindow.showAndWait();

    }

    // Create a PopUp Window to anounce that you have don't have enough money left for the spin!
    public static void notEnoughMoneyForBet() {
        Stage popupwindow = new Stage();

        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("Not enough money left!");


        Label label1 = new Label("You don't have enough money left for your current bet!");

        VBox layout = new VBox(10);


        layout.getChildren().addAll(label1);

        layout.setAlignment(Pos.CENTER);

        Scene scene1 = new Scene(layout, 300, 50);

        popupwindow.setScene(scene1);

        popupwindow.showAndWait();

    }

    public static void makeBetWindow() {
        Stage popupwindow = new Stage();

        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("Set your bet!");

        Button btn = new Button("Set amount");

        Label label = new Label();

        Slider slider = new Slider(10, 1000, 10);

        Bindings.bindBidirectional(label.textProperty(), slider.valueProperty(), new StringConverter<>() {

            @Override
            public String toString(Number object) {
                return String.valueOf(Math.round((Double) object));
            }

            @Override
            public Double fromString(String s) {
                return Double.parseDouble(s);
            }
        });

        btn.setOnAction(event -> setBet((int) Math.round(slider.getValue()), popupwindow));

        HBox layout = new HBox(10);

        layout.getChildren().addAll(slider, label, btn);

        layout.setAlignment(Pos.CENTER);

        Scene scene1 = new Scene(layout, 400, 60);

        popupwindow.setScene(scene1);

        popupwindow.showAndWait();
    }

    public static void setBet(int sliderValue, Stage popupwindow) {
        bet = sliderValue;
        currentBet.setText(String.valueOf(bet));
        popupwindow.close();
    }
}