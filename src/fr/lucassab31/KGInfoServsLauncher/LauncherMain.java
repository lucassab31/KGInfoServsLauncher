package fr.lucassab31.KGInfoServsLauncher;

import fr.trxyy.alternative.alternative_api.GameConnect;
import fr.trxyy.alternative.alternative_api.GameEngine;
import fr.trxyy.alternative.alternative_api.GameFolder;
import fr.trxyy.alternative.alternative_api.GameLinks;
import fr.trxyy.alternative.alternative_api.GameStyle;
import fr.trxyy.alternative.alternative_api.GameVersion;
import fr.trxyy.alternative.alternative_api.LauncherPreferences;
import fr.trxyy.alternative.alternative_api.maintenance.GameMaintenance;
import fr.trxyy.alternative.alternative_api.maintenance.Maintenance;
import fr.trxyy.alternative.alternative_api_ui.LauncherPane;
import fr.trxyy.alternative.alternative_api_ui.base.AlternativeBase;
import fr.trxyy.alternative.alternative_api_ui.base.LauncherBase;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LauncherMain extends AlternativeBase {
	
	/** ================= Nom du dossier d'installation  ================= **/
	private GameFolder gameFolder = new GameFolder("KGInfoServsLauncher");
	/** ================= Préférence du launcher (Nom, taille) ================= **/
	private LauncherPreferences launcherPreference = new LauncherPreferences("KGInfoServs Launcher", 950, 600, true);
	/** ================= Moteur du jeu (dossier, preférences, version, style) ================= **/
	private GameEngine gameEngine = new GameEngine(gameFolder, launcherPreference, GameVersion.V_1_16_1, GameStyle.OPTIFINE);
	/** ================= Autorisation des maintenances ================= **/
	private GameMaintenance gameMaintenance = new GameMaintenance(Maintenance.USE, gameEngine);
	/** ================= Lien de téléchargement des fichiers du jeu ================= **/
	private GameLinks gameLinks =  new GameLinks("https://download.kginfoservs.com/KGInfoServsLauncher/files/", "1.16.2.json");
	/** ================= Connexion automatique à un serveur ================= **/
	private GameConnect gameConnect = new GameConnect("play.kginfoservs.com", "25565");

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Scene scene = new Scene(createContent());
		this.gameEngine.reg(primaryStage);
		this.gameEngine.reg(this.gameLinks);
		this.gameEngine.reg(this.gameMaintenance);
		/** ================= Connexion automatique à un serveur ================= **/
//		this.gameEngine.reg(this.gameConnect);
		LauncherBase launcherBase = new LauncherBase(primaryStage, scene, StageStyle.UNDECORATED, gameEngine);
		/** ================= icon du launcher dans la barre des taches ================= **/
		launcherBase.setIconImage(primaryStage, getResourceLocation().loadImage(gameEngine, "favicon.png"));
	}
	
	private Parent createContent() {
		LauncherPane contentPane = new LauncherPane(gameEngine);
		new LauncherPanel(contentPane, gameEngine);
		return contentPane;
	}
}
