package application;
 
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
 
public class StartScreen extends Application {
 
    private Stage primaryStage;
 
    public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Jump - Accueil");
 
        Label title = new Label();
        title.setText(String.format("JUMP"));
        title.setTextFill(Color.BLACK);
        title.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 100));
 
        // Création des boutons
        Button startButton = createButton("Commencer", Color.web("776D5A",1.0), Main.class);
        Button tutorialButton = createButton("Tutoriel", Color.web("A09CB0",1.0), Tutorial.class);
        Button quitButton = createButton("Quitter", Color.web("987D7C",1.0), null);
 
        // Création de la disposition VBox
        VBox layout = new VBox(50);
        layout.setAlignment(Pos.CENTER);
        layout.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        layout.getChildren().addAll(title, startButton, tutorialButton, quitButton);
 
        // Création de la scène
        Scene scene = new Scene(layout, 1280, 720);
 
        // Affichage de la scène
        primaryStage.setScene(scene);
        primaryStage.show();
    }
 
    private Button createButton(String buttonText, Color backgroundColor, Class<? extends Application> appClass) {
        Button button = new Button(buttonText);
        button.setPrefSize(200, 50);
        button.setTextFill(Color.WHITE);
        button.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
        button.setBackground(new Background(new BackgroundFill(backgroundColor, CornerRadii.EMPTY, null)));
 
        if (appClass != null) {
            button.setOnAction(e -> {
                try {
                    Application appInstance = appClass.getDeclaredConstructor().newInstance();
                    appInstance.start(new Stage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                primaryStage.close();
            });
        } else {
            button.setOnAction(e -> primaryStage.close());
        }
 
        return button;
    }
}