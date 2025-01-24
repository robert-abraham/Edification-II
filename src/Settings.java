import org.json.simple.JSONObject;

import javafx.beans.property.DoubleProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Settings {
    private final StackPane root; // Root layout for Scene 2
    private StackPane rootMenuScreen; // Reference to Scene 1's root
    private Slider music; 
    private Slider difficulty;
    private Slider SoundFX;

    
    private SettingsLoader loader;

    public Settings(Stage stage) {
        // Initialize the root
        JSONObject settings = loader.loadSettings();

        root = new StackPane();
        
        // Load and scale the background image
        Image image = new Image("assets/SS2.png"); // Ensure "SS2.png" is in the assets folder
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(1470); 
        imageView.setFitHeight(956); 

        
        // Create sliders    
        double musicVolumeVal = ((Number) settings.get("musicVolume")).doubleValue();
        double difficultyVal = ((Number) settings.get("difficulty")).doubleValue();
        double sfxVolumeVal = ((Number) settings.get("sfxVolume")).doubleValue();

        difficulty = new Slider(0, 100 ,difficultyVal);
        music = new Slider(0,1,musicVolumeVal);
        SoundFX = new Slider(0,1, sfxVolumeVal);
        

        // Style sliders (set width)
        difficulty.setPrefWidth(530);
        music.setPrefWidth(530);
        SoundFX.setPrefWidth(530);
        

        // Position sliders using Pane
        Pane settingsPane = new Pane();
        settingsPane.setPrefSize(1470, 956); // Screen size for MacBook Air

        // Calculate positions for the sliders
        double leftOffset = 730; // Distance from the left edge
        double verticalSpacing = 80; // Vertical spacing between sliders
        double sliderHeight = 10; // Approximate slider height for alignment
        double topOffset = 320; // Center vertically

        // Position each slider
        difficulty.setLayoutX(leftOffset);
        difficulty.setLayoutY(topOffset);
        music.setLayoutX(leftOffset);
        music.setLayoutY(topOffset + sliderHeight + verticalSpacing);
        SoundFX.setLayoutX(leftOffset);
        SoundFX.setLayoutY(topOffset + 2 * (sliderHeight + verticalSpacing));

        // Position buttons using Pane
        Button backButton = new Button(); // Top button (Go to Scene 3)
        backButton.setLayoutX(584); // Position buttonPane at the left
        backButton.setLayoutY(790);

        String transparentStyle = "-fx-background-color: transparent; -fx-border-color: transparent;";
        backButton.setStyle(transparentStyle);
        backButton.setPrefSize(300, 101);
        backButton.setOnAction(event -> {
            settings.put("musicVolume", music.getValue());
            settings.put("difficulty", difficulty.getValue());
            settings.put("sfxVolume", SoundFX.getValue());
            loader.saveSettings(settings);
            stage.getScene().setRoot(rootMenuScreen);
            });

        
        settingsPane.getChildren().addAll(difficulty, music, SoundFX,backButton);

        // Add the image and slider pane to the root
        root.getChildren().addAll(imageView, settingsPane);
    }

    public DoubleProperty getMusic(){
        return music.valueProperty();
    }


    public StackPane getRoot() {
        return root;
    }

    public void setOtherRoots(StackPane rootMenuScreen) {
        this.rootMenuScreen = rootMenuScreen;
    }

    public void setLoader(SettingsLoader loader){
        this.loader = loader;
    }
}
