package fr.lucassab31.KGInfoServsLauncher;

import java.io.IOException;
import java.text.DecimalFormat;

import fr.trxyy.alternative.alternative_api.GameEngine;
import fr.trxyy.alternative.alternative_api.account.AccountType;
import fr.trxyy.alternative.alternative_api.auth.GameAuth;
import fr.trxyy.alternative.alternative_api.updater.GameUpdater;
import fr.trxyy.alternative.alternative_api.utils.FontLoader;
import fr.trxyy.alternative.alternative_api.utils.Mover;
import fr.trxyy.alternative.alternative_api.utils.config.UserConfig;
import fr.trxyy.alternative.alternative_api.utils.config.UsernameSaver;
import fr.trxyy.alternative.alternative_api_ui.LauncherAlert;
import fr.trxyy.alternative.alternative_api_ui.LauncherPane;
import fr.trxyy.alternative.alternative_api_ui.base.IScreen;
import fr.trxyy.alternative.alternative_api_ui.components.LauncherButton;
import fr.trxyy.alternative.alternative_api_ui.components.LauncherImage;
import fr.trxyy.alternative.alternative_api_ui.components.LauncherLabel;
import fr.trxyy.alternative.alternative_api_ui.components.LauncherPasswordField;
import fr.trxyy.alternative.alternative_api_ui.components.LauncherRectangle;
import fr.trxyy.alternative.alternative_api_ui.components.LauncherTextField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import re.alwyn974.minecraftserverping.MinecraftServerPing;
import re.alwyn974.minecraftserverping.MinecraftServerPingInfos;
import re.alwyn974.minecraftserverping.MinecraftServerPingOptions;

public class LauncherPanel extends IScreen {
	
	public UserConfig userConfig;
	public GameEngine theGameEngine;
	
	private LauncherRectangle topRectangle;
	private LauncherRectangle leftRectangle;
	private LauncherRectangle rightRectangle;
	
	private LauncherLabel titleLabel;
	private LauncherImage titleImage;
	
	private LauncherImage serverStatusImage;
	private LauncherLabel serverStatusLabel;
	private LauncherLabel serverStatusSurvivalLabel;
	private LauncherLabel serverStatusCreativeLabel;
	private LauncherImage serverStatusSurvival;
	private LauncherImage serverStatusCreative;
	private LauncherImage playerCounterImage;
	private LauncherLabel playerCounterLabel;
	private LauncherLabel playerCounter;
	
	private LauncherTextField usernameField;
	private LauncherPasswordField passwordField;
	
	private UsernameSaver usernameSaver;
	
	private LauncherButton closeButton;
	private LauncherButton reduceButton;
	
	private LauncherButton loginButton;
	private LauncherButton settingsButton;
	
	private LauncherButton createaccount;
	private LauncherButton websiteButton;
	
	private Timeline timeline;
	private DecimalFormat decimalFormat = new DecimalFormat(".#");
	private Thread updateThread;
	private GameUpdater gameUpdater = new GameUpdater();
	private LauncherRectangle updateRectangle;
	private LauncherLabel updateLabel;
	private LauncherLabel currentFileLabel;
	private LauncherLabel percentageLabel;
	private LauncherLabel currentStepLabel;

	public LauncherPanel(Pane root, GameEngine engine) {
		/** ================= Arrière plan du launcher ================= **/
		this.drawBackgroundImage(engine, root, "background.png");
		
		this.userConfig = new UserConfig(engine);
		this.theGameEngine = engine;
		
		/** ================= Save du login lors de la connexion ================= **/
		this.usernameSaver = new UsernameSaver(engine);
		
		/** ================= Barre du haut ================= **/
		this.topRectangle = new LauncherRectangle(root, 0, 0, engine.getWidth(), 31);
		this.topRectangle.setFill(Color.rgb(255, 255, 255, 0.70));
		
		this.titleLabel = new LauncherLabel(root);
		this.titleLabel.setText("KGInfoServs Launcher");
		this.titleLabel.setFont(FontLoader.loadFont("Roboto-Light.ttf", "Roboto Light", 18F));
		this.titleLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: black");
		this.titleLabel.setPosition(engine.getWidth() / 2 - 80, -4);
		this.titleLabel.setOpacity(0.7);
		this.titleLabel.setSize(500, 40);
		
		this.titleImage = new LauncherImage(root);
		this.titleImage.setImage(getResourceLocation().loadImage(engine, "favicon.png"));
		this.titleImage.setSize(25, 25);
		this.titleImage.setPosition(engine.getWidth() / 3 + 40, 3);
		
		this.closeButton = new LauncherButton(root);
		this.closeButton.setInvisible();
		this.closeButton.setPosition(engine.getWidth() - 35, 2);
		this.closeButton.setSize(15, 15);
		this.closeButton.setBackground(null);
		LauncherImage closeImg = new LauncherImage(root, getResourceLocation().loadImage(engine, "close.png"));
		closeImg.setSize(15, 15);
		this.closeButton.setGraphic(closeImg);
		this.closeButton.setOnAction(event -> {
			System.exit(0);
		});
		
		this.reduceButton = new LauncherButton(root);
		this.reduceButton.setInvisible();
		this.reduceButton.setPosition(engine.getWidth() - 55, 2);
		this.reduceButton.setSize(15, 15);
		this.reduceButton.setBackground(null);
		LauncherImage reduceImg = new LauncherImage(root, getResourceLocation().loadImage(engine, "minimize.png"));
		reduceImg.setSize(15, 15);
		this.reduceButton.setGraphic(reduceImg);
		this.reduceButton.setOnAction(event -> {
			Stage stage = (Stage) ((LauncherButton) event.getSource()).getScene().getWindow();
			stage.setIconified(true);
		});
		
		/** ================= Rectangle gauche ================= **/
		this.leftRectangle = new LauncherRectangle(root, 0, 31, 300, engine.getHeight());
		this.leftRectangle.setFill(Color.rgb(89, 130, 227, 0.70));
		
		this.serverStatusImage = new LauncherImage(root);
		this.serverStatusImage.setImage(getResourceLocation().loadImage(engine, "status.png"));
		this.serverStatusImage.setSize(80, 62);
		this.serverStatusImage.setPosition(110, 50);
		
		this.serverStatusLabel = new LauncherLabel(root);
		this.serverStatusLabel.setText("Status des serveurs");
		this.serverStatusLabel.setFont(FontLoader.loadFont("Roboto-Light.ttf", "Roboto Light", 26F));
		this.serverStatusLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: white");
		this.serverStatusLabel.setPosition(50, 120);
		this.serverStatusLabel.setSize(250, 40);
		
		this.serverStatusSurvivalLabel = new LauncherLabel(root);
		this.serverStatusSurvivalLabel.setText("Survie  :");
		this.serverStatusSurvivalLabel.setFont(FontLoader.loadFont("Roboto-Light.ttf", "Roboto Light", 22F));
		this.serverStatusSurvivalLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: white");
		this.serverStatusSurvivalLabel.setPosition(50, 160);
		this.serverStatusSurvivalLabel.setSize(80, 40);
		
		this.serverStatusSurvival = new LauncherImage(root);
		this.serverStatusSurvival.setPosition(170, 170);
		this.serverStatusSurvival.setSize(26, 26);
		
		this.serverStatusCreativeLabel = new LauncherLabel(root);
		this.serverStatusCreativeLabel.setText("Créatif :");
		this.serverStatusCreativeLabel.setFont(FontLoader.loadFont("Roboto-Light.ttf", "Roboto Light", 22F));
		this.serverStatusCreativeLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: white");
		this.serverStatusCreativeLabel.setPosition(50, 190);
		this.serverStatusCreativeLabel.setSize(80, 40);
		
		this.serverStatusCreative = new LauncherImage(root);
		this.serverStatusCreative.setPosition(170, 200);
		this.serverStatusCreative.setSize(26, 26);
		
		this.playerCounterImage = new LauncherImage(root);
		this.playerCounterImage.setImage(getResourceLocation().loadImage(engine, "player.png"));
		this.playerCounterImage.setSize(98, 91);
		this.playerCounterImage.setPosition(101, 270);
		
		this.playerCounterLabel = new LauncherLabel(root);
		this.playerCounterLabel.setText("Joueurs connecté");
		this.playerCounterLabel.setFont(FontLoader.loadFont("Roboto-Light.ttf", "Roboto Light", 24F));
		this.playerCounterLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: white");
		this.playerCounterLabel.setPosition(50, 370);
		this.playerCounterLabel.setSize(250, 40);
		
		this.playerCounter = new LauncherLabel(root);
		this.playerCounter.setFont(FontLoader.loadFont("Roboto-Light.ttf", "Roboto Light", 20F));
		this.playerCounter.setStyle("-fx-background-color: transparent; -fx-text-fill: white");
		this.playerCounter.setPosition(125, 400);
		this.playerCounter.setSize(100, 40);
		
		MinecraftServerPingInfos.V1_9_HIGHER data;
		try {
			data = new MinecraftServerPing().getPing_V1_9_HIGHER(new MinecraftServerPingOptions().setHostname("play.kginfoservs.com").setPort(25565));
			this.serverStatusSurvival.setImage(getResourceLocation().loadImage(engine, "online.png"));
			this.serverStatusCreative.setImage(getResourceLocation().loadImage(engine, "online.png"));
			this.playerCounter.setText(data.getPlayers().getOnline() + "/" + data.getPlayers().getMax());
//			System.out.println(data.getDescription() + " " + data.getVersion().getName() + " " + data.getLatency() + " " + data.getPlayers().getOnline() + "/" + data.getPlayers().getMax());
		} catch (IOException e) {
			this.serverStatusSurvival.setImage(getResourceLocation().loadImage(engine, "offline.png"));
			this.serverStatusCreative.setImage(getResourceLocation().loadImage(engine, "offline.png"));
			this.playerCounter.setText("NA");
			e.printStackTrace();
		}
		
		this.websiteButton = new LauncherButton(root);
		this.websiteButton.setText("Site web");
		this.websiteButton.setFont(FontLoader.loadFont("Roboto-Light.ttf", "Roboto Light", 30F));
		this.websiteButton.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4); -fx-text-fill: white;");
		this.websiteButton.setInvisible();
		this.websiteButton.setPosition(50, engine.getHeight() - 100);
		this.websiteButton.setSize(200, 40);
		this.websiteButton.setOnAction(event -> {
			openLink("https://kginfoservs.com");
		});
		
		/** ================= Rectangle droit ================= **/
		this.rightRectangle = new LauncherRectangle(root, 650, 31, 300, engine.getHeight());
		this.rightRectangle.setFill(Color.rgb(89, 130, 227, 0.7));
		
		this.drawLogo(engine, getResourceLocation().loadImage(engine, "website_icon.png"), 736, 75, 128, 128, root, Mover.DONT_MOVE);
		
		this.usernameField = new LauncherTextField(root);
		this.usernameField.setText(this.usernameSaver.getUsername());
		this.usernameField.setPosition(700, engine.getHeight() / 2 - 57);
		this.usernameField.setSize(200, 50);
		this.usernameField.setFont(FontLoader.loadFont("Roboto-Light.ttf", "Roboto Light", 18F));
		this.usernameField.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4); -fx-text-fill: white;");
		this.usernameField.setVoidText("Nom de compte");
		
		this.passwordField = new LauncherPasswordField(root);
		this.passwordField.setPosition(700, engine.getHeight() / 2);
		this.passwordField.setSize(200, 50);
		this.passwordField.setFont(FontLoader.loadFont("Roboto-Light.ttf", "Roboto Light", 18F));
		this.passwordField.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4); -fx-text-fill: white;");
		this.passwordField.setVoidText("Mot de passe");
		
		this.loginButton = new LauncherButton(root);
		this.loginButton.setText("Se connecter");
		this.loginButton.setFont(FontLoader.loadFont("Roboto-Light.ttf", "Roboto Light", 22F));
		this.loginButton.setPosition(700, engine.getHeight() / 2 + 60);
		this.loginButton.setSize(200, 45);
		this.loginButton.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4); -fx-text-fill: white;");
		this.loginButton.setAction(event -> {
			if (this.usernameField.getText().length() < 3) {
				new LauncherAlert("Connexion echouee", "Le username doit contenir plus de 3 carateres");
			} else if (this.usernameField.getText().length() > 3 && !this.passwordField.getText().isEmpty()) {
				GameAuth auth = new GameAuth(this.usernameField.getText(), this.passwordField.getText(), AccountType.MOJANG);
				if (auth.isLogged()) {
					this.usernameSaver.writeUsername(this.usernameField.getText());
					this.update(engine, auth);
				} else {
					new LauncherAlert("Connexion echouee", "Identification incorrects");
				}
			} else {
				new LauncherAlert("Connexion echouee", "La connexion a echouee");
			}
		});
		
		this.settingsButton = new LauncherButton(root);
		this.settingsButton.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4); -fx-text-fill: white;");
		LauncherImage settingsImg = new LauncherImage(root, getResourceLocation().loadImage(engine, "settings.png"));
		settingsImg.setSize(27, 27);
		this.settingsButton.setGraphic(settingsImg);
		this.settingsButton.setPosition(750, engine.getHeight() / 2 + 130);
		this.settingsButton.setSize(100, 40);
		 this.settingsButton.setOnAction(new EventHandler<ActionEvent>() {
			 @Override
			 public void handle(ActionEvent event) {
			 Scene scene = new Scene(createSettingsPanel());
			 Stage stage = new Stage();
			 scene.setFill(Color.TRANSPARENT);
			 stage.setResizable(false);
			 stage.initStyle(StageStyle.TRANSPARENT);
			 stage.setTitle("Parametres Launcher");
			 stage.setWidth(500);
			 stage.setHeight(230);
			 stage.setScene(scene);
			 stage.initModality(Modality.APPLICATION_MODAL);
			 stage.showAndWait();
			 }
			 });
		
		this.createaccount = new LauncherButton(root);
		this.createaccount.setText("Creer un compte");
		this.createaccount.setFont(FontLoader.loadFont("Roboto-Light.ttf", "Roboto Light", 14F));
		this.createaccount.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4); -fx-text-fill: white;");
		this.createaccount.setInvisible();
		this.createaccount.setPosition(725, engine.getHeight() / 2 + 200);
		this.createaccount.setSize(150, 40);
		this.createaccount.setOnAction(event -> {
			openLink("https://www.minecraft.net/fr-fr/get-minecraft");
		});
		
		/** ================= Rectangle central ================= **/
		this.updateRectangle = new LauncherRectangle(root, engine.getWidth() / 2 - 175, engine.getHeight() / 2 - 60, 350, 180);
		this.updateRectangle.setArcWidth(10.0);
		this.updateRectangle.setArcHeight(10.0);
		this.updateRectangle.setFill(Color.rgb(0, 0, 0, 0.60));
		this.updateRectangle.setVisible(false);
		
		this.updateLabel = new LauncherLabel(root);
		this.updateLabel.setText("- MISE A JOUR -");
		this.updateLabel.setAlignment(Pos.CENTER);
		this.updateLabel.setFont(FontLoader.loadFont("Roboto-Light.ttf", "Roboto Light", 22F));
		this.updateLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
		this.updateLabel.setPosition(engine.getWidth() / 2 - 95, engine.getHeight() / 2 - 55);
		this.updateLabel.setSize(190, 40);
		this.updateLabel.setVisible(false);
		
		this.currentStepLabel = new LauncherLabel(root);
		this.currentStepLabel.setText("Preparation de la mise à jour");
		this.currentStepLabel.setAlignment(Pos.CENTER);
		this.currentStepLabel.setFont(Font.font("Verdana", FontPosture.ITALIC, 18F));
		this.currentStepLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
		this.currentStepLabel.setPosition(engine.getWidth() / 2 - 160, engine.getHeight() / 2 + 83);
		this.currentStepLabel.setSize(320, 40);
		this.currentStepLabel.setOpacity(0.4);
		this.currentStepLabel.setVisible(false);
		
		this.currentFileLabel = new LauncherLabel(root);
		this.currentFileLabel.setAlignment(Pos.CENTER);
		this.currentFileLabel.setFont(FontLoader.loadFont("Roboto-Light.ttf", "Roboto Light", 18F));
		this.currentFileLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
		this.currentFileLabel.setPosition(engine.getWidth() / 2 - 160, engine.getHeight() / 2 + 25);
		this.currentFileLabel.setSize(320, 40);
		this.currentFileLabel.setVisible(false);
		
		this.percentageLabel = new LauncherLabel(root);
		this.percentageLabel.setAlignment(Pos.CENTER);
		this.percentageLabel.setText("0%");
		this.percentageLabel.setFont(FontLoader.loadFont("Roboto-Light.ttf", "Roboto Light", 30F));
		this.percentageLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
		this.percentageLabel.setPosition(engine.getWidth() / 2 - 50, engine.getHeight() / 2 - 5);
		this.percentageLabel.setOpacity(0.8);
		this.percentageLabel.setSize(100, 40);
		this.percentageLabel.setVisible(false);
	}
	
	public void update(GameEngine engine, GameAuth auth) {
		/** ================= Désactivation + invisibilité login system ================= **/
		this.usernameField.setDisable(true);
		this.usernameField.setVisible(false);
		this.passwordField.setDisable(true);
		this.passwordField.setVisible(false);
		this.loginButton.setDisable(true);
		this.loginButton.setVisible(false);
		this.settingsButton.setDisable(true);
		this.settingsButton.setVisible(false);
		
		/** ================= Affichage update ================= **/
		this.updateRectangle.setVisible(true);
		this.updateLabel.setVisible(true);
		this.currentStepLabel.setVisible(true);
		this.currentFileLabel.setVisible(true);
		this.percentageLabel.setVisible(true);
		
		gameUpdater.reg(engine);
		gameUpdater.reg(auth.getSession());
		engine.reg(this.gameUpdater);
		this.updateThread = new Thread() {
			public void run() {
				engine.getGameUpdater().run();
			}
		};
		this.updateThread.start();
		
		this.timeline = new Timeline(new KeyFrame[] {
				new KeyFrame(javafx.util.Duration.seconds(0.0D), e -> updateDownload(engine),
				new javafx.animation.KeyValue[0]),
				new KeyFrame(javafx.util.Duration.seconds(0.1D),
				new javafx.animation.KeyValue[0])});
		this.timeline.setCycleCount(Animation.INDEFINITE);
		this.timeline.play();
	}

	public void updateDownload(GameEngine engine) {
		if (engine.getGameUpdater().downloadedFiles > 0) {
			this.percentageLabel.setText(decimalFormat.format(engine.getGameUpdater().downloadedFiles * 100.0D / engine.getGameUpdater().filesToDownload) + "%");
		}
		this.currentFileLabel.setText(engine.getGameUpdater().getCurrentFile());
		this.currentStepLabel.setText(engine.getGameUpdater().getCurrentInfo());
	}
	
	 private Parent createSettingsPanel() {
		 LauncherPane contentPane = new LauncherPane(theGameEngine);
		 Rectangle rect = new Rectangle(500, 230);
		 rect.setArcHeight(15.0);
		 rect.setArcWidth(15.0);
		 contentPane.setClip(rect);
		 contentPane.setStyle("-fx-background-color: transparent;");
		 new LauncherSettings(contentPane, theGameEngine, this);
		 return contentPane;
		 }
}
