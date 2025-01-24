import com.sun.tools.javac.Main;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MenuScreen {
    private final StackPane root; // Root layout for Scene 1
    private StackPane rootScene2; // Reference to Scene 2's root
    private StackPane rootScene3; // Reference to Scene 3's root
    private MainGame mainGame;
    public MenuScreen(Stage stage) {
        // Initialize the root
        root = new StackPane();
        mainGame = new MainGame(stage);
        mainGame.setOtherRoots(root);

        // Load and scale the image
        Image image = new Image("assets/SS4.png"); // Ensure "SS4.png" is in the assets folder
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(1470); // Screen width
        imageView.setFitHeight(956); // Screen height

        // Create buttons
        Button playNowButton = new Button(); // Top button (Go to Scene 3)
        Button settingsButton = new Button(); // Middle button (Go to Scene 2)
        Button exitButton = new Button(); // Bottom button (Exit program)

        // Style buttons to make them transparent
        String transparentStyle = "-fx-background-color: transparent; -fx-border-color: transparent;";

        playNowButton.setPrefSize(390, 101);
        settingsButton.setPrefSize(390, 101);
        exitButton.setPrefSize(390, 101);

        playNowButton.setStyle(transparentStyle);
        settingsButton.setStyle(transparentStyle);
        exitButton.setStyle(transparentStyle);

        // Set button actions
        playNowButton.setOnAction(event -> mainGame.launch_game()); // Switch to Scene 3
        settingsButton.setOnAction(event -> stage.getScene().setRoot(rootScene2)); // Switch to Scene 2
        exitButton.setOnAction(event -> System.exit(0)); // Exit the program

        // Position buttons using Pane
        Pane buttonPane = new Pane();
        buttonPane.setPrefSize(1470, 956); // Screen size

        // Calculate positions for the buttons
        double leftOffset = 46; // Distance from the left edge
        double bottomOffset = 182; // Distance from the bottom for the last button
        double buttonHeight = 101; // Button height
        double buttonSpacing = 18; // Vertical spacing between buttons

        // Position each button
        playNowButton.setLayoutX(leftOffset);
        playNowButton.setLayoutY(956 - bottomOffset - (2 * buttonHeight + 2 * buttonSpacing)); // Top button (Play Now)

        settingsButton.setLayoutX(leftOffset);
        settingsButton.setLayoutY(956 - bottomOffset - (buttonHeight + buttonSpacing)); // Middle button (Settings)

        exitButton.setLayoutX(leftOffset);
        exitButton.setLayoutY(956 - bottomOffset); // Bottom button (Exit)

        // Add buttons to the pane
        buttonPane.getChildren().addAll(playNowButton, settingsButton, exitButton);

        // Add the image and button pane to the root
        root.getChildren().addAll(imageView, buttonPane);
    }

    public StackPane getRoot() {
        return root;
    }

    public void setOtherRoots(StackPane rootScene2, StackPane rootScene3) {
        this.rootScene2 = rootScene2;
        this.rootScene3 = rootScene3;
    }
}
