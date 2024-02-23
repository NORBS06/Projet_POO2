package application;
	
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class Main extends Application {

    //maps keycode to boolean - keycode is the javafx enumeration
    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
    private ArrayList<Node> platforms = new ArrayList<>();
    private ArrayList<Node> coins = new ArrayList<>();
    private ArrayList<Node> bomb_kills = new ArrayList<>();
    private ArrayList<Node> doors = new ArrayList<>();
    
    private Pane appRoot = new Pane();
    private Pane gameRoot = new Pane();
    private Pane uiRoot = new Pane();

    private Node player;
    private Point2D playerVelocity = new Point2D(0, 0);
    private boolean canJump = true;
    private int levelWidth;
    private int point = 0;
    private Label score;
    
    
    private void initContent(){
        Rectangle bg = new Rectangle(1280, 720);
        bg.setFill(Color.web("EAF4F4",1.0));
        levelWidth = LevelData.LEVEL1[0].length() * 60;

        score=new Label();
        score.setText(String.format("score: %d", point));
        score.setTextFill(Color.BLACK);
        score.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
        uiRoot.getChildren().add(score);
        
        for (int i=0; i< LevelData.LEVEL1.length; i++){
            String line = LevelData.LEVEL1[i];
            for (int j=0; j <line.length();j++){
                switch (line.charAt(j)){
                    case '0':
                        break;
                    case '1':
                        Node platform = createEntity(j*60, i *60, 60, 60, Color.web("00B9AE",1.0));
                        platforms.add(platform);
                        break;
                    case '2':
                    	Node coin = createEntity(j*60, i *60, 60, 60, Color.web("FFFF00",1.0));
                    	coins.add(coin);
                    	break;
                    case '3':
                    	Node bomb_kill = createEntity(j*60, i *60, 60, 60, Color.web("F00000",1.0));
                    	bomb_kills.add(bomb_kill);
                    	break;
                    case '4':
                    	Node door = createEntity(j*60, i *60, 60, 60, Color.web("FAB040",1.0));
                    	doors.add(door);
                    	break;
                }
            }
        }
        player = createEntity(0, 600, 40, 40, Color.web("03312E",1.0));
        player.translateXProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();
            if (offset > 640 && offset < levelWidth-640){
                gameRoot.setLayoutX(-(offset-640));
            }
        });
        
        appRoot.getChildren().addAll(bg, gameRoot, uiRoot);
    }
    

    private void update(){
        if (isPressed(KeyCode.W) && player.getTranslateY() >= 5){
            jumpPlayer();
        }
        if (isPressed(KeyCode.A) && player.getTranslateX() >=5){
            movePlayerX(-5);
        }
        if (isPressed(KeyCode.D) && player.getTranslateX() + 40 <=levelWidth-5){
            movePlayerX(5);
        }
        if (playerVelocity.getY() < 10){
            playerVelocity = playerVelocity.add(0, 1);
        }
        movePlayerY((int)playerVelocity.getY());
    
    	for (Node coin : coins) {
    		if (player.getBoundsInParent().intersects(coin.getBoundsInParent())){
    			coin.getProperties().put("alive", false);
    			point+=50;
    			score.setText(String.format("score: %d",point));
    		}
    	}
    	for (Iterator<Node> it = coins.iterator(); it.hasNext(); ) {
    		Node coin = it.next();
    		if (!(Boolean)coin.getProperties().get("alive")) {
    			it.remove();
    			gameRoot.getChildren().remove(coin);
    		}
    	}
    	
    	for (Node bomb_kill : bomb_kills) {
    		if (player.getBoundsInParent().intersects(bomb_kill.getBoundsInParent())){
    			bomb_kill.getProperties().put("alive", false);
    			die();
    		}
    	}
    	for (Iterator<Node> it = bomb_kills.iterator(); it.hasNext(); ) {
    		Node bomb_kill = it.next();
    		if (!(Boolean)bomb_kill.getProperties().get("alive")) {
    			it.remove();
    			gameRoot.getChildren().remove(bomb_kill);
    		}
    	}
    	
    	for (Node door : doors) {
    		if (player.getBoundsInParent().intersects(door.getBoundsInParent())){
    			door.getProperties().put("alive", false);
    			winner_board();
    		}
    	}
    }


    private void winner_board() {
    	Rectangle win=new Rectangle(1280, 720,Color.BLACK);
    	
    	VBox endBox=new VBox(50);
		endBox.setPadding(new Insets(100, 0, 0, 500));
    	endBox.setAlignment(Pos.CENTER);
    	
    	Label endMessage=new Label();
		endMessage.setText(String.format("C'est Fini, Vous avez fini le jeu!!"));
		endMessage.setTextFill(Color.WHITE);
		endMessage.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
		endBox.getChildren().add(endMessage);
		
		Label final_score=new Label();
		final_score.setText(String.format("Votre score final est %d", point));
		final_score.setTextFill(Color.WHITE);
		final_score.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
		endBox.getChildren().add(final_score);
		
		Button RestartButton =new Button();
		RestartButton.setText("Recommencer");
		RestartButton.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
		RestartButton.setPrefSize(200, 50);
		RestartButton.setOnAction(e->{
			uiRoot.getChildren().remove(win);
			uiRoot.getChildren().remove(endBox);
			uiRoot.getChildren().remove(score);
			gameRoot.getChildren().remove(player);
			point=0;
			try {
				start(new Stage());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		endBox.getChildren().add(RestartButton);
		
		Button menuButton =new Button();
		menuButton.setText("Menu");
		menuButton.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
		menuButton.setPrefSize(200, 50);
		menuButton.setOnAction(e->{
			StartScreen menu = new StartScreen();
			try {
					menu.start(new Stage());
	           } catch (Exception ex) {
	               ex.printStackTrace();
	           }
		});
		endBox.getChildren().add(menuButton);
		
		Button quitButton =new Button();
		quitButton.setText("Quitter");
		quitButton.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
		quitButton.setPrefSize(200, 50);
		quitButton.setOnAction(e->{
			Platform.exit();
		});
		endBox.getChildren().add(quitButton);
		
		
    	uiRoot.getChildren().addAll(win,endBox);
	}
    
    
	private void die() {
    	
    	Rectangle gameOverMask=new Rectangle(1280, 720,Color.BLACK);
    	
    	VBox endBox=new VBox(50);
		endBox.setPadding(new Insets(100, 0, 0, 480));
    	endBox.setAlignment(Pos.CENTER);
		
		Label endMessage=new Label();
		endMessage.setText(String.format("C'est Fini, vous êtes mort!"));
		endMessage.setTextFill(Color.WHITE);
		endMessage.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
		endBox.getChildren().add(endMessage);
		
		Label final_score=new Label();
		final_score.setText(String.format("Votre score final est %d", point));
		final_score.setTextFill(Color.WHITE);
		final_score.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
		endBox.getChildren().add(final_score);
		
		Button RestartButton =new Button();
		RestartButton.setText("Recommencer");
		RestartButton.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
		RestartButton.setPrefSize(200, 50);
		RestartButton.setOnAction(e->{
			uiRoot.getChildren().remove(gameOverMask);
			uiRoot.getChildren().remove(endBox);
			uiRoot.getChildren().remove(score);
			gameRoot.getChildren().remove(player);
			point=0;
			try {
				start(new Stage());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		endBox.getChildren().add(RestartButton);
		
		Button menuButton =new Button();
		menuButton.setText("Menu");
		menuButton.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
		menuButton.setPrefSize(200, 50);
		menuButton.setOnAction(e->{
			StartScreen menu = new StartScreen();
			try {
					menu.start(new Stage());
	           } catch (Exception ex) {
	               ex.printStackTrace();
	           }
		});
		endBox.getChildren().add(menuButton);
		
		Button quitButton =new Button();
		quitButton.setText("Quitter");
		quitButton.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
		quitButton.setPrefSize(200, 50);
		quitButton.setOnAction(e->{
			Platform.exit();
		});
		endBox.getChildren().add(quitButton);
		
		
    	uiRoot.getChildren().addAll(gameOverMask,endBox);
    }
    
    private void movePlayerX(int value){
    boolean movingRight = value > 0;
    for (int i=0; i < Math.abs(value);i++){
        for (Node platform : platforms){
            if(player.getBoundsInParent().intersects(platform.getBoundsInParent())){
                if(movingRight){
                    if (player.getTranslateX() + 40 == platform.getTranslateX()){
                        return;
                    }
                }else {
                    if (player.getTranslateX() == platform.getTranslateX() + 60) {
                        return;
                    }
                }
            }
        }
        player.setTranslateX(player.getTranslateX() + (movingRight ? 1 : -1));
        }
    }
    

    private void movePlayerY(int value){
        boolean movingDown = value > 0;
        for (int i=0; i < Math.abs(value);i++){
            for (Node platform : platforms){
                if(player.getBoundsInParent().intersects(platform.getBoundsInParent())){
                    if(movingDown){
                        if (player.getTranslateY() + 40 == platform.getTranslateY()){
                            canJump = true;
                            return;
                        }
                    }else {
                        if (player.getTranslateY() == platform.getTranslateY() + 60) {
                            return;
                        }
                    }
                }
            }
            player.setTranslateY(player.getTranslateY() + (movingDown ? 1 : -1));
        }
    }
    

    private void jumpPlayer(){
    if(canJump){
        playerVelocity = playerVelocity.add(0, -30);
        canJump = false;
        }
    }
    

    private Node createEntity(int x, int y, int w, int h, Color color){
        Rectangle entity = new Rectangle(w, h);
        entity.setTranslateX(x);
        entity.setTranslateY(y);
        entity.setFill(color);
        entity.getProperties().put("alive", true);
        gameRoot.getChildren().add(entity);
        return entity;

    }
    
    
    private boolean isPressed(KeyCode key){
    return keys.getOrDefault(key, false);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        initContent();
        Scene scene = new Scene(appRoot);
        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
        
        primaryStage.setTitle("Jump");
        primaryStage.setScene(scene);
        primaryStage.setMaxHeight(720);
        primaryStage.setMaxWidth(1280);
        primaryStage.setResizable(false);
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                	update();
            }
        };
        
        timer.start();
    }
    

    public static void main(String[] args) {
        launch(args);
    }
}