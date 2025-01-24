import javafx.scene.image.Image;

public class Emperor {
    private String name;             // Emperor's name
    private String description;      // Emperor's description
    private Image highResImage;      // High-resolution image
    private double speedMultiplier;  // Speed multiplier
    private double shootingMultiplier; // Shooting multiplier
    private double healthMultiplier; // Health multiplier
    private String ability;          // Ability description
    private String continent;        // Continent name
    private String continentFile;    // Continent file name (e.g., AFR.png)

    public Emperor(String name, String description, Image highResImage, double speedMultiplier,
                   double shootingMultiplier, double healthMultiplier, String ability, 
                   String continent, String continentFile) {
        this.name = name;
        this.description = description;
        this.highResImage = highResImage;
        this.speedMultiplier = speedMultiplier;
        this.shootingMultiplier = shootingMultiplier;
        this.healthMultiplier = healthMultiplier;
        this.ability = ability;
        this.continent = continent;
        this.continentFile = continentFile;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Image getHighResImage() {
        return highResImage;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    public double getShootingMultiplier() {
        return shootingMultiplier;
    }

    public double getHealthMultiplier() {
        return healthMultiplier;
    }

    public String getAbility() {
        return ability;
    }

    public String getContinent() {
        return continent;
    }

    public String getContinentFile() {
        return continentFile;
    }
}
