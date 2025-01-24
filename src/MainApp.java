import java.io.File;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MainApp extends Application {
    private MediaPlayer mediaPlayer; // Class-level variable to keep reference

    @Override
    public void start(Stage primaryStage) {
        // Create instances of all the scenes
        MenuScreen MenuScreen = new MenuScreen(primaryStage);
        Settings Settings = new Settings(primaryStage);

        //Create Settings File
        SettingsLoader loader = new SettingsLoader();
        Settings.setLoader(loader); // Pass through JSON file interface



        // Set the initial scene
        Scene mainScene = new Scene(MenuScreen.getRoot(), 1470, 956);

        // Configure the stage
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Scene Switcher");
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
       
       //CreateGame Instance
        MainGame MainGame = new MainGame(primaryStage);
        //allow the scenes to interface with each other 
        MenuScreen.setOtherRoots(Settings.getRoot(), MainGame.getRoot());
        Settings.setOtherRoots(MenuScreen.getRoot());

        
        //Play Background Music
        String backgroundMusic = getClass().getResource("/assets/backgroundmusic.mp3").toExternalForm();
        Media media = new Media(backgroundMusic);        
        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
        mediaPlayer.play();
        mediaPlayer.volumeProperty().bind(Settings.getMusic());
        // Show the stage
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}
