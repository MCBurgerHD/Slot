package app;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Random;

public class App extends Application {

    private static final int NUM_REELS = 3;

    private final Random random = new Random();
    private final Label resultLabel = new Label();
    private final Button spinButton = new Button("Spin");
    private final Label accountValue = new Label();
    private final Label currntBet = new Label();
    private final Button higherButton = new Button("Higher");
    private final Button lowerButton = new Button("Lower");

    private final Rectangle[] reels = new Rectangle[NUM_REELS];
    private final String[] currentSymbols = new String[NUM_REELS];

    private int bet = 10;
    private int account = 10000;

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
        root.setSpacing(20);
        root.setPadding(new Insets(20));

        spinButton.setOnAction(event -> spinReels());
        higherButton.setOnAction(event -> betHigher());
        lowerButton.setOnAction(event -> betLower());
        resultLabel.setTextFill(Color.RED);

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
        resultBox.getChildren().addAll(spinButton, higherButton, lowerButton, resultLabel, accountValue, currntBet);


        return resultBox;
    }

    public void spinReels() {
        account = account - bet;
        accountValue.setText(String.valueOf(account));
        for (int i = 0; i < NUM_REELS; i++) {
            String symbol = SYMBOLS[random.nextInt(SYMBOLS.length)];
            currentSymbols[i] = symbol;
            reels[i].setFill(new ImagePattern(getSymbolImage(symbol)));
        }

        if (isWinningCombination()) {
            account += bet * 2;
            resultLabel.setTextFill(Color.GREEN);
            resultLabel.setText("You won!");
            popUpWin();
        } else {
            resultLabel.setTextFill(Color.RED);
            resultLabel.setText("Try Again!");
        }
    }

    public Image getSymbolImage(String symbol) {
        switch (symbol) {
            case "Cherry":
                return cherry;
            case "Lemon":
                return lemon;
            case "Orange":
                return orange;
            case "Plum":
                return plum;
            case "Bell":
                return bell;
            case "Bar":
                return bar;
            case "Seven":
                return seven;
            case "Diamond":
                return diamond;
        }
        return null;
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

    public void betHigher() {
        if (bet < 1000) {
            bet += 10;
            currntBet.setText(String.valueOf(bet));
        } else {
            resultLabel.setText("max Bet!");
        }
    }

    public void betLower() {
        if (bet == 10) {
            resultLabel.setText("min Bet!");
        } else {
            bet -= 10;
            currntBet.setText(String.valueOf(bet));
        }
    }

    // Create a PopUp Window to anounce that you have WON!
    public static void popUpWin() {
        Stage popupwindow=new Stage();

        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("You Won!");


        Label label1= new Label("You Won");

        VBox layout= new VBox(10);

        layout.getChildren().addAll(label1);

        layout.setAlignment(Pos.CENTER);

        Scene scene1= new Scene(layout, 100, 50);

        popupwindow.setScene(scene1);

        popupwindow.showAndWait();

    }

    // Create a PopUp Window to anounce that you have no money left!
    public static void popUpNoMoney() {
        Stage popupwindow=new Stage();

        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("No Money!");


        Label label1= new Label("No money left");

        VBox layout= new VBox(10);


        layout.getChildren().addAll(label1);

        layout.setAlignment(Pos.CENTER);

        Scene scene1= new Scene(layout, 150, 50);

        popupwindow.setScene(scene1);

        popupwindow.showAndWait();

    }
}