import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.json.simple.JSONObject;

public class ShooterGame {
    private Stage primaryStage;
    private Random rand = new Random();
    private ArrayList<ImageView> fallingArrows;
    private ArrayList<ImageView> playerArrows;
    private ArrayList<ImageView> goblins;
    private Set<KeyCode> activeKeys = new HashSet<>();
    private SettingsLoader loader;

    private int goblinsShot;
    private int playerHealth;
    private int maxHealth;

    private double playerSpeed;
    private double attackSpeed;
    private double difficulty; 

    private long lastShotTime = 0;

    private ProgressBar healthBar;
    private Text goblinsText;
    private ImageView player;

    private StackPane mps_root;

    private AudioClip arrowSFX;

    private boolean firstArrowHitGround = false;

    public ShooterGame(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setOtherRoots(StackPane root) {
        this.mps_root = root;
    }

    public void launchFight(Emperor playerEmperor, Emperor enemyEmperor, FightResultCallback callback) {
        JSONObject settings = loader.loadSettings();
        difficulty = ((Number) settings.get("difficulty")).doubleValue();
        // Initialize battle parameters based on emperor multipliers
        this.playerSpeed = 5 * playerEmperor.getSpeedMultiplier();
        this.attackSpeed = 1.0 / playerEmperor.getShootingMultiplier();
        this.maxHealth = (int) (100 * playerEmperor.getHealthMultiplier());
        this.playerHealth = maxHealth;
        this.goblinsShot = 0;

        goblins = new ArrayList<>();
        playerArrows = new ArrayList<>();
        fallingArrows = new ArrayList<>();

        Pane pane = new Pane();

        // Load assets
        Image backgroundImage = loadImage("src/assets/Game/background.png");
        Image playerImage = loadImage("src/assets/Game/warrior.png");
        Image arrowUpImage = loadImage("src/assets/Game/ArrowUP.png");
        Image arrowDownImage = loadImage("src/assets/Game/ArrowDOWN.png");
        Image goblinImage = loadImage("src/assets/Game/enemy.png");
        arrowSFX = new AudioClip(new File("src/assets/Game/arrowSFX.wav").toURI().toString());
        arrowSFX.setVolume(((Number) settings.get("sfxVolume")).doubleValue());

        // Set background
        ImageView background = new ImageView(backgroundImage);
        background.setPreserveRatio(true);
        background.setFitHeight(956);
        pane.getChildren().add(background);

        // Set player
        player = new ImageView(playerImage);
        player.setFitWidth(100);
        player.setFitHeight(100);
        player.setX(720);
        player.setY(800);
        pane.getChildren().add(player);

        // Health bar
        healthBar = new ProgressBar(1.0);
        healthBar.setPrefWidth(200);
        healthBar.setLayoutX(20);
        healthBar.setLayoutY(20);
        pane.getChildren().add(healthBar);

        // Goblins text
        goblinsText = new Text("Goblins Shot: " + goblinsShot + " / 20");
        goblinsText.setFont(Font.font(20));
        goblinsText.setX(20);
        goblinsText.setY(60);
        pane.getChildren().add(goblinsText);

        // Instructions
        Text instructions = new Text("Left/Right: Move | Space: Shoot\nDefeat 20 goblins to win!");
        instructions.setFont(Font.font(16));
        instructions.setX(20);
        instructions.setY(90);
        pane.getChildren().add(instructions);

        primaryStage.getScene().setOnKeyPressed(event -> activeKeys.add(event.getCode()));
        primaryStage.getScene().setOnKeyReleased(event -> activeKeys.remove(event.getCode()));

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Player movement
                if (activeKeys.contains(KeyCode.LEFT)) {
                    player.setX(Math.max(0, player.getX() - playerSpeed));
                }
                if (activeKeys.contains(KeyCode.RIGHT)) {
                    player.setX(Math.min(primaryStage.getScene().getWidth() - player.getFitWidth(), player.getX() + playerSpeed));
                }

                // Shooting
                if (activeKeys.contains(KeyCode.SPACE) && now - lastShotTime > attackSpeed * 1_000_000_000) {
                    shootArrow(pane, arrowUpImage);
                    lastShotTime = now;
                }

                // Spawn falling arrows
                if (rand.nextDouble() < difficulty / 300.0) {
                    spawnArrow(pane, arrowDownImage);
                }

                // Spawn goblins after the first arrow hits the ground
                if (firstArrowHitGround && rand.nextDouble() < difficulty / 700.0) {
                    spawnGoblin(pane, goblinImage);
                }

                // Update game elements
                updateFallingArrows(pane);
                updatePlayerArrows(pane);
                updateGoblins(pane);

                // Check win condition
                if (goblinsShot >= 20) {
                    this.stop();
                    showEndMessage(pane, "YOU WIN!", true, callback);
                }

                // Check lose condition
                if (playerHealth <= 0) {
                    this.stop();
                    showEndMessage(pane, "YOU LOSE!", false, callback);
                }
            }
        };

        // Switch to battle scene
        primaryStage.getScene().setRoot(pane);
        timer.start();
    }

    private void showEndMessage(Pane pane, String message, boolean isWin, FightResultCallback callback) {
        Text endText = new Text(message);
        endText.setFont(Font.font("Arial", 48));
        endText.setX((pane.getWidth() - endText.getLayoutBounds().getWidth()) / 2);
        endText.setY((pane.getHeight() - endText.getLayoutBounds().getHeight()) / 2);
        pane.getChildren().add(endText);

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> {
            primaryStage.getScene().setRoot(mps_root); // Return to the map region scene
            callback.onResult(isWin); // Notify the caller with the result
        });
        pause.play();
    }

    private Image loadImage(String path) {
        try {
            return new Image(new File(path).toURI().toString());
        } catch (Exception e) {
            System.err.println("Error loading image: " + path);
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    private void spawnArrow(Pane pane, Image arrowImage) {
        ImageView arrow = new ImageView(arrowImage);
        arrow.setFitWidth(20);
        arrow.setFitHeight(60);
        arrow.setX(rand.nextDouble() * (pane.getWidth() - 20));
        arrow.setY(-60);
        fallingArrows.add(arrow);
        pane.getChildren().add(arrow);
    }

    private void shootArrow(Pane pane, Image arrowImage) {
        ImageView arrow = new ImageView(arrowImage);
        arrow.setFitWidth(20);
        arrow.setFitHeight(40);
        arrow.setX(player.getX() + player.getFitWidth() / 2 - 10);
        arrow.setY(player.getY() - 40);
        playerArrows.add(arrow);
        pane.getChildren().add(arrow);
        arrowSFX.play();
    }

    private void spawnGoblin(Pane pane, Image goblinImage) {
        ImageView goblin = new ImageView(goblinImage);
        goblin.setFitWidth(60);
        goblin.setFitHeight(60);
        goblin.setX(rand.nextDouble() * (pane.getWidth() - 60));
        goblin.setY(-60);
        goblins.add(goblin);
        pane.getChildren().add(goblin);
    }

    private void updateFallingArrows(Pane pane) {
        for (int i = 0; i < fallingArrows.size(); i++) {
            ImageView arrow = fallingArrows.get(i);
            arrow.setY(arrow.getY() + 3);

            if (!firstArrowHitGround && arrow.getY() > 956) {
                firstArrowHitGround = true;
            }

            if (arrow.getBoundsInParent().intersects(player.getBoundsInParent())) {
                playerHealth -= 10;
                healthBar.setProgress((double) playerHealth / maxHealth);
                pane.getChildren().remove(arrow);
                fallingArrows.remove(i);
                i--;
            }

            if (arrow.getY() > 956) {
                pane.getChildren().remove(arrow);
                fallingArrows.remove(i);
                i--;
            }
        }
    }

    private void updatePlayerArrows(Pane pane) {
        for (int i = 0; i < playerArrows.size(); i++) {
            ImageView arrow = playerArrows.get(i);
            arrow.setY(arrow.getY() - 5);

            if (arrow.getY() < 0) {
                pane.getChildren().remove(arrow);
                playerArrows.remove(i);
                i--;
            }
        }
    }

    private void updateGoblins(Pane pane) {
        for (int i = 0; i < goblins.size(); i++) {
            ImageView goblin = goblins.get(i);
            goblin.setY(goblin.getY() + 2);

            if (goblin.getBoundsInParent().intersects(player.getBoundsInParent())) {
                playerHealth -= 20;
                healthBar.setProgress((double) playerHealth / maxHealth);
                pane.getChildren().remove(goblin);
                goblins.remove(i);
                i--;
                continue;
            }

            for (int j = 0; j < playerArrows.size(); j++) {
                ImageView arrow = playerArrows.get(j);
                if (goblin.getBoundsInParent().intersects(arrow.getBoundsInParent())) {
                    goblinsShot++;
                    goblinsText.setText("Goblins Shot: " + goblinsShot + " / 20");
                    pane.getChildren().remove(goblin);
                    pane.getChildren().remove(arrow);
                    goblins.remove(i);
                    playerArrows.remove(j);
                    i--;
                    break;
                }
            }

            if (goblin.getY() > 956) {
                pane.getChildren().remove(goblin);
                goblins.remove(i);
                i--;
            }
        }
    }

    public interface FightResultCallback {
        void onResult(boolean victory);
    }
}
