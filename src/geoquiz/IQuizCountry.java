package geoquiz;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Locale;

import eu.hansolo.fx.world.Country;
import javafx.scene.image.Image;
import png250px.Png250px;

public interface IQuizCountry {
	Image getFlag();
	String getCapital();
	String getName();
	String getAbbreviation();
}

class QuizCountry implements IQuizCountry {
	protected String iso3name;
	protected String name;
	
	public QuizCountry(Country country) {
		Locale locale = new Locale("", country.name());
		name = locale.getDisplayCountry();
		iso3name = locale.getISO3Country();
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
			BufferedReader reader = new BufferedReader(new FileReader(Paths.get("rsc", "geoquiz", "capitals.txt").toFile()));
			while ((line = reader.readLine()) != null) {
				String[] data = line.split(",");
				final String[] otherData = data;
				capitals.put(data[0], otherData);
			}
			reader.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public PictorialQuizCountry(Country country) {
		super(country);
		capital = capitals.get(name) != null 
				? capitals.get(name)[1]
				: "";
		String countryAbbreviation = country.name().toLowerCase();
		String imageName = "/png250px/" + countryAbbreviation + ".png";
		flag = new Image(Png250px.class.getResourceAsStream(imageName));
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
}
