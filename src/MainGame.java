import javafx.beans.property.DoubleProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

public class MainGame {
    private final StackPane root; // Root layout for MainGame
    private StackPane rootMenuScreen; // Reference to Scene 1's root
    private CharacterSelectionScreen SelectScreen;
    private Stage primarystage;
    public MainGame(Stage stage) {
        primarystage = stage; 
        root = new StackPane();
        SelectScreen = new CharacterSelectionScreen(primarystage);


    }
    public void launch_game(){
        SelectScreen.refresh();
        primarystage.getScene().setRoot(SelectScreen.getRoot()); // Go back to Scene 
        primarystage.getScene().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    primarystage.getScene().setRoot(rootMenuScreen);
                    break;
                default:
                    break;
            }
        });
    }
    public void setOtherRoots(StackPane rootMenuScreen) {
        this.rootMenuScreen = rootMenuScreen;
    }
    public StackPane getRoot() {
        return root;
    }

}
