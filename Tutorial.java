package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Tutorial extends Main {
	
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
    private Stage primaryStage;
    
	
	private void initContent(){
		Image backgroundImage = new Image(getClass().getResource("/bg1.jpg").toExternalForm());
		
        Rectangle bg = new Rectangle(1280, 720);
        bg.setFill(new ImagePattern(backgroundImage));
        levelWidth = LevelData.TUTO[0].length() * 60;

        score=new Label();
        score.setText(String.format("score: %d", point));
        score.setTextFill(Color.BLACK);
        score.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
        uiRoot.getChildren().add(score);
        
        for (int i=0; i< LevelData.TUTO.length; i++){
            String line = LevelData.TUTO[i];
            for (int j=0; j <line.length();j++){
                switch (line.charAt(j)){
                    case '0':
                        break;
                    case '1':
                        Node platform = createEntity(j*60, i *60, 60, 60, Color.web("00B9AE",1.0));
                        platforms.add(platform);
                        break;
                    case '2':
                    	Node coin = createEntity(j*60, i *60, 60, 60, Color.web("F0E052",1.0));
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
        
        Label move_mess=new Label();
        move_mess.setText(String.format("Utiliser les touches 'W, A, S, D'\n pour sauter et vous deplacer"));
        move_mess.setAlignment(Pos.CENTER);
        move_mess.setTextFill(Color.BLACK);
        move_mess.setPadding(new Insets(300, 0, 0, 200));
        move_mess.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
		gameRoot.getChildren().add(move_mess);
        
        Label coin_mess=new Label();
		coin_mess.setText(String.format("Toucher avec les carres jaune\n pour obtenir des points"));
		coin_mess.setAlignment(Pos.CENTER);
		coin_mess.setTextFill(Color.BLACK);
		coin_mess.setPadding(new Insets(500, 0, 0, 400));
		coin_mess.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
		gameRoot.getChildren().add(coin_mess);
		
		Label platform_mess=new Label();
		platform_mess.setText(String.format("     Sauter sur les plateformes\n pour vous deplacer et obtenir le\n          maximum de coin"));
		platform_mess.setAlignment(Pos.CENTER);
		platform_mess.setTextFill(Color.BLACK);
		platform_mess.setPadding(new Insets(200, 0, 0, 700));
		platform_mess.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
		gameRoot.getChildren().add(platform_mess);
		
		Label obs_mess=new Label();
		obs_mess.setText(String.format("    Eviter de toucher\n avec les carres rouge\n sinon vous êtes mort"));
		obs_mess.setAlignment(Pos.CENTER);
		obs_mess.setTextFill(Color.BLACK);
		obs_mess.setPadding(new Insets(500, 0, 0, 2100));
		obs_mess.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
		gameRoot.getChildren().add(obs_mess);
        
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
	    	Rectangle win=new Rectangle(1280, 720,Color.LIGHTBLUE);
	    	
	    	VBox endBox=new VBox(50);
			endBox.setPadding(new Insets(140, 0, 0, 420));
	    	endBox.setAlignment(Pos.CENTER);
	    	
	    	Label endMessage=new Label();
			endMessage.setText(String.format("C'est Fini, Vous avez terminer le tutoriel."));
			endMessage.setTextFill(Color.web("443E33",1.0));
			endMessage.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
			endBox.getChildren().add(endMessage);
			
			Label final_score=new Label();
			final_score.setText(String.format("Votre score final est %d", point));
			final_score.setTextFill(Color.web("443E33",1.0));
			final_score.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
			endBox.getChildren().add(final_score);
			
			Button RestartButton = createButton("Recommencer", Color.web("776D5A",1.0), Tutorial.class);
	        Button menuButton = createButton("Menu", Color.web("A09CB0",1.0), StartScreen.class);
	        Button quitButton = createButton("Quitter", Color.web("987D7C",1.0), null);
	        endBox.getChildren().addAll(RestartButton, menuButton,quitButton);
			
			
	    	uiRoot.getChildren().addAll(win,endBox);
		}
	 
	private void die() {
	    	
	    	Rectangle gameOverMask=new Rectangle(1280, 720,Color.LIGHTBLUE);
	    	
	    	VBox endBox=new VBox(30);
			endBox.setPadding(new Insets(120, 0, 0, 450));
	    	endBox.setAlignment(Pos.CENTER);
			
			Label endMessage=new Label();
			endMessage.setText(String.format("Vous êtes mort"));
			endMessage.setTextFill(Color.web("443E33",1.0));
			endMessage.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
			endBox.getChildren().add(endMessage);
			
			Label tip =new Label();
			tip.setText(String.format("Astuce: Essaie de sauter au dessus\n des blocks rouge pour ne pas mourrir."));
			tip.setTextFill(Color.web("443E33",1.0));
			tip.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
			endBox.getChildren().add(tip);
			
			Label final_score=new Label();
			final_score.setText(String.format("Votre score est %d", point));
			final_score.setTextFill(Color.web("443E33",1.0));
			final_score.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
			endBox.getChildren().add(final_score);
			
			Button RestartButton = createButton("Recommencer", Color.web("776D5A",1.0), Tutorial.class);
	        Button menuButton = createButton("Menu", Color.web("A09CB0",1.0), StartScreen.class);
	        Button quitButton = createButton("Quitter", Color.web("987D7C",1.0), null);
	        endBox.getChildren().addAll(RestartButton, menuButton, quitButton);
			
	    	uiRoot.getChildren().addAll(gameOverMask,endBox);
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

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		
		initContent();
        Scene scene = new Scene(appRoot);
        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
        
        primaryStage.setTitle("Jump - Tutoriel");
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

}
