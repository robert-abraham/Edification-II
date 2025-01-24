import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MapRegionScreen {
    private final StackPane root; // Root container for the screen
    private final ImageView mainMapView; // Main map image
    private final List<ImageView> regionImages; // Region images
    private final List<Button> regionButtons; // Region buttons
    private String ownedEmpire;
    private ShooterGame shooterGame;
    private StackPane rootMenuScreen;

    public MapRegionScreen(Stage primaryStage) {
        root = new StackPane();
        root.setStyle("-fx-background-color: #98cbfc;"); // Background color

        // Main map view
        mainMapView = new ImageView(new Image(getClass().getResource("/assets/map/MAP.png").toExternalForm()));
        mainMapView.setPreserveRatio(true);
        mainMapView.setFitHeight(956); // Scale to screen height

        // Region images and buttons
        regionImages = new ArrayList<>();
        regionButtons = new ArrayList<>();

        // Initialize the ShooterGame instance
        shooterGame = new ShooterGame(primaryStage);
        shooterGame.setOtherRoots(root);
    }
    public void setOtherRoots(StackPane root) {
        rootMenuScreen = root;
    }

    public void launch(Stage stage) {

        stage.getScene().setRoot(root);
        // Add main map to the root
        root.getChildren().add(mainMapView);

        // Load emperor data
        List<Emperor> emperors = EmperorData.getEmperors();

        // Create buttons and region images for other emperors
        VBox buttonLayout = createRegionButtons(emperors, ownedEmpire);

        // Add buttons to the left side of the screen
        HBox layout = new HBox(buttonLayout);
        layout.setAlignment(Pos.CENTER_LEFT);
        layout.setPadding(new Insets(20));
        root.getChildren().add(layout);
        
    }

    private VBox createRegionButtons(List<Emperor> emperors, String selectedEmperorName) {
        VBox buttonLayout = new VBox(10);
        buttonLayout.setAlignment(Pos.TOP_LEFT);

        for (Emperor emperor : emperors) {
            // Highlight the selected emperor's country
            if (emperor.getName().equals(selectedEmperorName)) {
                highlightCountry(emperor);
                continue; // Skip adding a button for the selected emperor
            }

            // Create buttons for other emperors
            createRegionButton(emperor, buttonLayout);
        }

        return buttonLayout;
    }

    private void highlightCountry(Emperor emperor) {
        ImageView regionImage = new ImageView(new Image(getClass().getResource("/assets/map/" + emperor.getContinentFile()).toExternalForm()));
        regionImage.setPreserveRatio(true);
        regionImage.setFitHeight(956); // Match the height of the main map
        regionImage.setVisible(true); // Set as visible since it's owned
        regionImage.setStyle("-fx-opacity: 0.7; -fx-blend-mode: red;"); // Red highlight effect
        regionImages.add(regionImage);
        root.getChildren().add(regionImage);
    }

    private void createRegionButton(Emperor emperor, VBox buttonLayout) {
        // Create the button
        Button button = new Button(emperor.getName());
        button.setStyle("-fx-font-size: 14px; -fx-padding: 10; -fx-background-color: #333; -fx-text-fill: white;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 14px; -fx-padding: 10; -fx-background-color: #555; -fx-text-fill: white;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 14px; -fx-padding: 10; -fx-background-color: #333; -fx-text-fill: white;"));

        regionButtons.add(button);

        // Create the region image (initially hidden)
        ImageView regionImage = new ImageView(new Image(getClass().getResource("/assets/map/" + emperor.getContinentFile()).toExternalForm()));
        regionImage.setPreserveRatio(true);
        regionImage.setFitHeight(956); // Match the height of the main map
        regionImage.setVisible(false); // Initially hidden
        regionImages.add(regionImage);

        // Add the region image to the root
        root.getChildren().add(regionImage);

        // Set button action to trigger the battle and handle the outcome
        button.setOnAction(e -> {
            // Launch the battle
            shooterGame.launchFight(getOwnedEmperor(), emperor, victory -> {
                if (victory) {
                    // If victorious, highlight the region and remove the button
                    regionImage.setVisible(true);
                    buttonLayout.getChildren().remove(button);
                } else {
                    // If defeated, keep the button and display a message (optional)
                    System.out.println("Defeated by " + emperor.getName());
                }
            });
        });

        // Add button to the button layout
        buttonLayout.getChildren().add(button);
    }

    private Emperor getOwnedEmperor() {
        // Fetch the currently owned emperor based on the name
        return EmperorData.getEmperors().stream()
                .filter(emp -> emp.getName().equals(ownedEmpire))
                .findFirst()
                .orElse(null);
    }

    public void setOwnedEmpire(String selectedEmp) {
        this.ownedEmpire = selectedEmp;
    }
}
