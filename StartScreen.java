package application;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StartScreen extends Application {

   public static void main(String[] args) {
       launch(args);
   }

   @Override
   public void start(Stage primaryStage) throws Exception {
       primaryStage.setTitle("Jump - Accueil");

       // Création des boutons
       Button startButton = new Button("Start");
       Button tutorialButton = new Button("Tutoriel");
       Button quitButton = new Button("Quitter");

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

//       tutorialButton.setOnAction(e -> {
//           Tuto tuto = new Tuto(); 
//           try {
//               tuto.start(new Stage());
//           } catch (Exception ex) {
//               ex.printStackTrace();
//           }
//           primaryStage.close();
//       });

       quitButton.setOnAction(e -> primaryStage.close());

       // Création de la disposition VBox
       VBox layout = new VBox(20);
       layout.getChildren().addAll(startButton, tutorialButton, quitButton);

       // Création de la scène
       Scene scene = new Scene(layout, 1280, 720);

       // Affichage de la scène
       primaryStage.setScene(scene);
       primaryStage.show();
   }
}