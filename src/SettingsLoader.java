// Java program to read and write a settings JSON file

import org.json.simple.JSONObject; // Use org.json.simple library
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SettingsLoader {
    private static final String SETTINGS_FILE = "settings.json";

    // Method to create a default settings JSON
    public static JSONObject getDefaultSettings() {
        JSONObject defaultSettings = new JSONObject();
        defaultSettings.put("musicVolume", 0.5);
        defaultSettings.put("difficulty", 50);
        defaultSettings.put("sfxVolume", 0.5);
        return defaultSettings;
    }

    // Method to save settings to a file
    public static void saveSettings(JSONObject settings) {
        try (FileWriter file = new FileWriter(SETTINGS_FILE)) {
            file.write(settings.toJSONString());
            file.flush();
            System.out.println("Settings saved successfully!");
        } catch (IOException e) {
            System.err.println("Error saving settings: " + e.getMessage());
        }
    }

    // Method to load settings from a file
    public static JSONObject loadSettings() {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(SETTINGS_FILE)) {
            // Parse the JSON file
            return (JSONObject) parser.parse(reader);
        } catch (IOException | ParseException e) {
            System.err.println("Error loading settings: " + e.getMessage());
            System.out.println("Creating default settings...");
            return getDefaultSettings();
        }
    }
}
