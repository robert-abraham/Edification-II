import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.List;

public class EmperorData {
    public static List<Emperor> getEmperors() {
        List<Emperor> emperors = new ArrayList<>();

        try {
            emperors.add(new Emperor(
                "Emperor A",
                "A strategic genius who always stays one step ahead.",
                new Image(EmperorData.class.getResource("/assets/leaders/A.png").toExternalForm()),
                1.2, 1.0, 0.8,
                "Speed Boost: Increases movement speed by 20%.",
                "Africa",
                "AFR.png"
            ));

            emperors.add(new Emperor(
                "Emperor B",
                "A stalwart defender of the realm, ready to protect the weak.",
                new Image(EmperorData.class.getResource("/assets/leaders/B.png").toExternalForm()),
                0.9, 1.0, 1.3,
                "Health Boost: Increases health by 30%.",
                "Antarctica",
                "ANT.png"
            ));

            emperors.add(new Emperor(
                "Emperor C",
                "A conqueror whose armies strike fear into the hearts of foes.",
                new Image(EmperorData.class.getResource("/assets/leaders/C.png").toExternalForm()),
                1.0, 1.2, 0.9,
                "Shooting Boost: Increases attack rate by 20%.",
                "Australia",
                "AUS.png"
            ));

            emperors.add(new Emperor(
                "Emperor D",
                "An innovator who combines strategy with technology.",
                new Image(EmperorData.class.getResource("/assets/leaders/D.png").toExternalForm()),
                1.1, 1.1, 0.9,
                "Balanced Boost: Slight increases to speed and shooting.",
                "Europe",
                "EUR.png"
            ));

            emperors.add(new Emperor(
                "Emperor E",
                "A master of diplomacy and alliances.",
                new Image(EmperorData.class.getResource("/assets/leaders/E.png").toExternalForm()),
                0.9, 4.3, 0.9,
                "Shooting Specialist: Exceptional attack speed.",
                "India",
                "IND.png"
            ));

            emperors.add(new Emperor(
                "Emperor F",
                "A keeper of ancient secrets, wielding mysterious powers.",
                new Image(EmperorData.class.getResource("/assets/leaders/F.png").toExternalForm()),
                1.0, 0.8, 1.2,
                "Tank Boost: Increases survivability significantly.",
                "North America",
                "NOA.png"
            ));

            emperors.add(new Emperor(
                "Emperor G",
                "A protector of nature, leading with balance and wisdom.",
                new Image(EmperorData.class.getResource("/assets/leaders/G.png").toExternalForm()),
                1.3, 0.9, 0.8,
                "Speed Specialist: Unmatched mobility.",
                "South America",
                "SOA.png"
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return emperors;
    }
}
