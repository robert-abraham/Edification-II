import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

import org.json.simple.JSONObject;

import javafx.scene.media.AudioClip;

public class CharacterSelectionScreen {
    private final StackPane root;
    private AudioClip hoverSFX; // Sound effect for hovering
    private SettingsLoader loader;
    private Stage primaryStage;
    private MapRegionScreen MPS;
    private boolean selected = false;
    private StackPane rootMenuScreen;

    public CharacterSelectionScreen(Stage stage) {
        root = new StackPane();
        primaryStage = stage;
        hoverSFX = new AudioClip(getClass().getResource("/assets/hover.wav").toExternalForm());
        MPS = new MapRegionScreen(stage);
        List<Emperor> emperors = EmperorData.getEmperors();

        // Create Emperor Grid
        GridPane grid = createEmperorGrid(emperors);

        // Layout
        VBox layout = new VBox(20, grid);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #1f1f1f; -fx-padding: 20;");

        // Root Layout
        root.getChildren().add(layout);

    }

    private GridPane createEmperorGrid(List<Emperor> emperors) {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        for (int i = 0; i < emperors.size(); i++) {
            VBox card = createEmperorCard(emperors.get(i));
            grid.add(card, i % 3, i / 3); // 3 columns
        }

        return grid;
    }

    private VBox createEmperorCard(Emperor emperor) {
        // High-Res Image scaled for the grid
        ImageView imageView = new ImageView(emperor.getHighResImage());
        imageView.setFitWidth(200); // Adjust size for the grid
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        // Name Label
        Label nameLabel = new Label(emperor.getName());
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        // Ability Label
        Label abilityLabel = new Label(emperor.getAbility());
        abilityLabel.setStyle("-fx-text-fill: yellow; -fx-font-size: 14px;");

        // Stats Labels
        Label statsLabel = new Label(
            String.format("Speed: %.1fx | Shooting: %.1fx | Health: %.1fx",
                emperor.getSpeedMultiplier(),
                emperor.getShootingMultiplier(),
                emperor.getHealthMultiplier()
            )
        );
        statsLabel.setStyle("-fx-text-fill: lightgray; -fx-font-size: 12px;");

        // Continent Info
        Label continentLabel = new Label("Continent: " + emperor.getContinent());
        continentLabel.setStyle("-fx-text-fill: lightblue; -fx-font-size: 14px;");

    

        // Card Layout
        VBox card = new VBox(10, imageView, nameLabel, abilityLabel, statsLabel, continentLabel);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: #333; -fx-padding: 10; -fx-border-color: white; -fx-border-width: 2;");

        // Hover Effect
        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: #333; -fx-padding: 10; -fx-border-color: yellow; -fx-border-width: 2;");
            hoverSFX.play(); // Play sound effect
        });

        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: #333; -fx-padding: 10; -fx-border-color: white; -fx-border-width: 2;");
        });

        // Selection Effect
        card.setOnMouseClicked(e -> {
            card.setStyle("-fx-background-color: #333; -fx-padding: 10; -fx-border-color: green; -fx-border-width: 2;");
            selectEmperor(emperor);
        });

        return card;
    }

    private void selectEmperor(Emperor emperor) {
        selected = true; 
        System.out.println("Selected Emperor:");
        System.out.println("Name: " + emperor.getName());
        System.out.println("Ability: " + emperor.getAbility());
        System.out.printf("Stats: Speed: %.1fx | Shooting: %.1fx | Health: %.1fx%n",
            emperor.getSpeedMultiplier(),
            emperor.getShootingMultiplier(),
            emperor.getHealthMultiplier()
        );
        System.out.println("Continent: " + emperor.getContinent());
        System.out.println("Continent File: " + emperor.getContinentFile());
        MPS.setOwnedEmpire(emperor.getName());
        MPS.setOtherRoots(rootMenuScreen);
        MPS.launch(primaryStage);


    }
    public void setOtherRoots(StackPane root) {
        this.rootMenuScreen = root;
    }

    public void refresh() {
        JSONObject settings = loader.loadSettings();
        double sfxVolumeVal = ((Number) settings.get("sfxVolume")).doubleValue();
        hoverSFX.setVolume(sfxVolumeVal);
        if (selected) {
            MPS.launch(primaryStage);
        }
    }

    public StackPane getRoot() {
        return root;
    }
}
