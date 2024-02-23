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

   public static void main(String[] args) {
       launch(args);
   }

   @Override
   public void start(Stage primaryStage) throws Exception {
       primaryStage.setTitle("Jump - Accueil");
       
       Label title=new Label();
       title.setText(String.format("JUMP"));
       title.setTextFill(Color.BLACK);
       title.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 100));

       // Création des boutons
       Button startButton = new Button("Commencer");
       Button tutorialButton = new Button("Tutoriel");
       Button quitButton = new Button("Quitter");
       
       startButton.setPrefSize(200, 50);
       tutorialButton.setPrefSize(200, 50);
       quitButton.setPrefSize(200, 50);
       
       startButton.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
       tutorialButton.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
       quitButton.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));

       // Gestion des événements des boutons
       startButton.setOnAction(e -> {
           Main mainApp = new Main();
           try {
               mainApp.start(new Stage());
           } catch (Exception ex) {
               ex.printStackTrace();
           }
           primaryStage.close();
       });

       tutorialButton.setOnAction(e -> {
    	   Tutorial tuto = new Tutorial(); 
           try {
               tuto.start(new Stage());
           } catch (Exception ex) {
               ex.printStackTrace();
           }
           primaryStage.close();
       });

       quitButton.setOnAction(e -> primaryStage.close());

       // Création de la disposition VBox
       VBox layout = new VBox(50);
       layout.setAlignment(Pos.CENTER);
       layout.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
       layout.getChildren().addAll(title, startButton, tutorialButton, quitButton);

       // Création de la scène
       Scene scene = new Scene(layout, 1280, 720);
       scene.setFill(Color.web("EAF4F4",1.0));

       // Affichage de la scène
       primaryStage.setScene(scene);
       primaryStage.show();
   }
}