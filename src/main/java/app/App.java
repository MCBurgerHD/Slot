package app;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
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
    private static final Label currentBet = new Label();
    /*private final Button higherButton = new Button("Higher");
    private final Button lowerButton = new Button("Lower");*/
    private final Button setBet = new Button("Bet");

    private final Rectangle[] reels = new Rectangle[NUM_REELS];
    private final String[] currentSymbols = new String[NUM_REELS];

    private static int bet = 10;
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
        root.setSpacing(25);
        root.setPadding(new Insets(25));

        spinButton.setOnAction(event -> spinReels());
        /*higherButton.setOnAction(event -> betHigher());
        lowerButton.setOnAction(event -> betLower());*/
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
        resultBox.getChildren().addAll(spinButton/*, higherButton, lowerButton*/, setBet , resultLabel, accountValue, currentBet);


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
            currentBet.setText(String.valueOf(bet));
        } else {
            resultLabel.setText("max Bet!");
        }
    }

    public void betLower() {
        if (bet == 10) {
            resultLabel.setText("min Bet!");
        } else {
            bet -= 10;
            currentBet.setText(String.valueOf(bet));
        }
    }

    // Create a PopUp Window to anounce that you have WON!
    public static void popUpWin() {
        Stage popupwindow = new Stage();

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
        Stage popupwindow = new Stage();

        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("No Money!");


        Label label1= new Label("You have no money left");

        VBox layout= new VBox(10);


        layout.getChildren().addAll(label1);

        layout.setAlignment(Pos.CENTER);

        Scene scene1= new Scene(layout, 150, 50);

        popupwindow.setScene(scene1);

        popupwindow.showAndWait();

    }

    // Create a PopUp Window to anounce that you have don't have enough money left for the spin!
    public static void notEnoughMoneyForBet() {
        Stage popupwindow = new Stage();

        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("Not enough money left!");


        Label label1= new Label("You don't have enough money left for your current bet!");

        VBox layout= new VBox(10);


        layout.getChildren().addAll(label1);

        layout.setAlignment(Pos.CENTER);

        Scene scene1= new Scene(layout, 300, 50);

        popupwindow.setScene(scene1);

        popupwindow.showAndWait();

    }

    public static void makeBetWindow() {
        Stage popupwindow = new Stage();

        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("Set your bet!");

        Button btn10 = new Button("10");
        Button btn20 = new Button("20");
        Button btn30 = new Button("30");
        Button btn40 = new Button("40");
        Button btn50 = new Button("50");
        Button btn60 = new Button("60");
        Button btn70 = new Button("70");
        Button btn80 = new Button("80");
        Button btn90 = new Button("90");
        Button btn100 = new Button("100");
        Button btn110 = new Button("110");
        Button btn120 = new Button("120");
        Button btn130 = new Button("130");
        Button btn140 = new Button("140");
        Button btn150 = new Button("150");
        Button btn160 = new Button("160");
        Button btn170 = new Button("170");
        Button btn180 = new Button("180");
        Button btn190 = new Button("190");
        Button btn200 = new Button("200");
        Button btn210 = new Button("210");
        Button btn220 = new Button("220");
        Button btn230 = new Button("230");
        Button btn240 = new Button("240");
        Button btn250 = new Button("250");
        Button btn260 = new Button("260");
        Button btn270 = new Button("270");
        Button btn280 = new Button("280");
        Button btn290 = new Button("290");
        Button btn300 = new Button("300");

        btn10.setPrefSize(40, 30);
        btn20.setPrefSize(40, 30);
        btn30.setPrefSize(40, 30);
        btn40.setPrefSize(40, 30);
        btn50.setPrefSize(40, 30);
        btn60.setPrefSize(40, 30);
        btn70.setPrefSize(40, 30);
        btn80.setPrefSize(40, 30);
        btn90.setPrefSize(40, 30);
        btn100.setPrefSize(40, 30);
        btn110.setPrefSize(40, 30);
        btn120.setPrefSize(40, 30);
        btn130.setPrefSize(40, 30);
        btn140.setPrefSize(40, 30);
        btn150.setPrefSize(40, 30);
        btn160.setPrefSize(40, 30);
        btn170.setPrefSize(40, 30);
        btn180.setPrefSize(40, 30);
        btn190.setPrefSize(40, 30);
        btn200.setPrefSize(40, 30);
        btn210.setPrefSize(40, 30);
        btn220.setPrefSize(40, 30);
        btn230.setPrefSize(40, 30);
        btn240.setPrefSize(40, 30);
        btn250.setPrefSize(40, 30);
        btn260.setPrefSize(40, 30);
        btn270.setPrefSize(40, 30);
        btn280.setPrefSize(40, 30);
        btn290.setPrefSize(40, 30);
        btn300.setPrefSize(40, 30);


        btn10.setOnAction(event -> setBet(10,popupwindow));
        btn20.setOnAction(event -> setBet(20,popupwindow));
        btn30.setOnAction(event -> setBet(30,popupwindow));
        btn40.setOnAction(event -> setBet(40,popupwindow));
        btn50.setOnAction(event -> setBet(50,popupwindow));
        btn60.setOnAction(event -> setBet(60,popupwindow));
        btn70.setOnAction(event -> setBet(70,popupwindow));
        btn80.setOnAction(event -> setBet(80,popupwindow));
        btn90.setOnAction(event -> setBet(90,popupwindow));
        btn100.setOnAction(event -> setBet(100,popupwindow));
        btn110.setOnAction(event -> setBet(110,popupwindow));
        btn120.setOnAction(event -> setBet(120,popupwindow));
        btn130.setOnAction(event -> setBet(130,popupwindow));
        btn140.setOnAction(event -> setBet(140,popupwindow));
        btn150.setOnAction(event -> setBet(150,popupwindow));
        btn160.setOnAction(event -> setBet(160,popupwindow));
        btn180.setOnAction(event -> setBet(180,popupwindow));
        btn190.setOnAction(event -> setBet(190,popupwindow));
        btn200.setOnAction(event -> setBet(200,popupwindow));
        btn210.setOnAction(event -> setBet(210,popupwindow));
        btn220.setOnAction(event -> setBet(220,popupwindow));
        btn230.setOnAction(event -> setBet(230,popupwindow));
        btn240.setOnAction(event -> setBet(240,popupwindow));
        btn250.setOnAction(event -> setBet(250,popupwindow));
        btn260.setOnAction(event -> setBet(260,popupwindow));
        btn280.setOnAction(event -> setBet(280,popupwindow));
        btn290.setOnAction(event -> setBet(290,popupwindow));
        btn300.setOnAction(event -> setBet(300,popupwindow));

        VBox vBox = new VBox(10);
        HBox layout = new HBox(10);
        HBox layout2 = new HBox(10);
        HBox layout3 = new HBox(10);

        layout.getChildren().addAll(btn10, btn20, btn30, btn40, btn50, btn60, btn70, btn80, btn90, btn100);

        layout2.getChildren().addAll(btn110, btn120, btn130, btn140, btn150, btn160, btn170, btn180, btn190, btn200);

        layout3.getChildren().addAll(btn210, btn220, btn230, btn240, btn250, btn260, btn270, btn280, btn290, btn300);

        layout.setAlignment(Pos.CENTER);
        layout2.setAlignment(Pos.CENTER);

        VBox.setMargin(layout, new Insets(10,0, 0, 0));

        vBox.getChildren().add(0, layout);
        vBox.getChildren().add(1, layout2);
        vBox.getChildren().add(2, layout3);

        Scene scene1 = new Scene(vBox, 500, 130);

        popupwindow.setScene(scene1);

        popupwindow.showAndWait();
    }

    public static void setBet(int value, Stage popupwindow) {
        bet = value;
        currentBet.setText(String.valueOf(bet));
        popupwindow.close();
    }
}