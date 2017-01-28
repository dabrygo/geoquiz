package geoquiz;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;

import eu.hansolo.fx.world.Country;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import png250px.Png250px;

public interface IQuizCountry {
    Image getFlag();

    Country getCountry();
    
    String getCapital();

    String getName();

    String getAbbreviation();

    void setColor(Color COLOR);
}

class QuizCountry implements IQuizCountry {
    protected String iso3name;
    protected String name;
    private Country country;

    public QuizCountry(Country country) {
        Locale locale = new Locale("", country.name());
        this.country = country;
        name = locale.getDisplayCountry();
        try {
            iso3name = locale.getISO3Country();
        } catch (MissingResourceException e) {
            iso3name = "";
        }
    }

    @Override
    public Image getFlag() {
        return null;
    }

    @Override
    public String getCapital() {
        return "";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAbbreviation() {
        return iso3name;
    }

    public String toString() {
        return iso3name;
    }

    @Override
    public Country getCountry() {
        return country;
    }

    @Override
    public void setColor(Color COLOR) {
        country.setColor(COLOR);
    }
}

/**
 * @author dbgod
 *
 */
class PictorialQuizCountry extends QuizCountry {
    private static HashMap<String, String[]> capitals;

    private Image flag;
    private String capital;

    static {
        capitals = new HashMap<>();
        try {
            String line;
            BufferedReader reader = new BufferedReader(
                    new FileReader(Paths.get("rsc", "geoquiz", "capitals.txt").toFile()));
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                final String[] otherData = data;
                capitals.put(data[0], otherData);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PictorialQuizCountry(Country country) {
        super(country);
        capital = capitals.get(name) != null ? capitals.get(name)[1] : "";
        String countryAbbreviation = country.name().toLowerCase();
        String imageName = "/png250px/" + countryAbbreviation + ".png";
        try {
            flag = new Image(Png250px.class.getResourceAsStream(imageName));
        } catch (NullPointerException e) {
            if (name.equals("Guadeloupe") || name.equals("Saint Pierre And Miquelon")) {
                imageName = "/png250px/fr.png"; // same as france
                flag = new Image(Png250px.class.getResourceAsStream(imageName));
            } else if (name.equals("Dominica")) {
                imageName = "/geoquiz/dq.png";
                try {
                    flag = new Image(getClass().getResourceAsStream(imageName));
                } catch (Exception e2) {
                    System.out.println(imageName);
                    e2.printStackTrace();
                }
            } else {
                System.err.println("No flag found for " + name + " (" + countryAbbreviation + ")");
                flag = null;
            }
        }
    }

    @Override
    public String getCapital() {
        return capital;
    }

    @Override
    public Image getFlag() {
        return flag;
    }
}

class NullCountry implements IQuizCountry {

    @Override
    public Image getFlag() {
        return null;
    }

    @Override
    public String getCapital() {
        return "";
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getAbbreviation() {
        return "";
    }

    @Override
    public Country getCountry() {
        return null;
    }

    @Override
    public void setColor(Color COLOR) { }
}
