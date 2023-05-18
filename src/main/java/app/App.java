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
    private final Label amount = new Label();

    private final Rectangle[] reels = new Rectangle[NUM_REELS];
    private final String[] currentSymbols = new String[NUM_REELS];

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
        resultLabel.setTextFill(Color.RED);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Slot Machine");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);

        for (int i = 0; i < NUM_REELS; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setHgrow(Priority.ALWAYS);
            gridPane.getColumnConstraints().add(columnConstraints);
        }

        return gridPane;
    }

    private void addReelsToGrid(GridPane gridPane) {
        for (int i = 0; i < NUM_REELS; i++) {
            Rectangle reel = createReel();
            reels[i] = reel;
            gridPane.add(reel, i, 0);
        }
    }

    private Rectangle createReel() {
        Rectangle reel = new Rectangle(80, 120);
        reel.setFill(new ImagePattern(seven));
        reel.setStroke(Color.BLACK);
        reel.setStrokeWidth(2);

        return reel;
    }

    private HBox createResultBox() {
        HBox resultBox = new HBox(10);
        resultBox.setAlignment(Pos.CENTER);
        resultBox.getChildren().addAll(spinButton, resultLabel, amount);


        return resultBox;
    }

    private void spinReels() {
        account = account - 10;
        amount.setText(String.valueOf(account));
        for (int i = 0; i < NUM_REELS; i++) {
            String symbol = SYMBOLS[random.nextInt(SYMBOLS.length)];
            currentSymbols[i] = symbol;
            reels[i].setFill(new ImagePattern(getSymbolImage(symbol)));
        }

        if (isWinningCombination()) {
            account += 1000;
            resultLabel.setTextFill(Color.GREEN);
            resultLabel.setText("You won!");
            popUpWin();
        } else {
            resultLabel.setTextFill(Color.RED);
            resultLabel.setText("Try Again!");
        }
    }

    private Image getSymbolImage(String symbol) {
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
    private boolean isWinningCombination() {
        if (account <= 0) {
            account = 10;
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
        Stage popupwindow=new Stage();

        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("You Won!");


        Label label1= new Label("You Won");


        //Button button1= new Button("Play Again!");

        //button1.setOnAction(e -> popupwindow.close());



        VBox layout= new VBox(10);


        layout.getChildren().addAll(label1/*, button1*/);

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


        Label label1= new Label("You have no money left");

        VBox layout= new VBox(10);


        layout.getChildren().addAll(label1);

        layout.setAlignment(Pos.CENTER);

        Scene scene1= new Scene(layout, 100, 50);

        popupwindow.setScene(scene1);

        popupwindow.showAndWait();

    }
}