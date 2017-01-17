package geoquiz;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Random;

import eu.hansolo.fx.world.Country;
import javafx.scene.image.Image;
import png250px.Png250px;

public class Model {

	interface IQuizCountry {
		Image getFlag();
		String getCapital();
		String getName();
		String getAbbreviation();
	}
	
	class QuizCountry implements IQuizCountry {
		protected String iso3name;
		protected String name;
		protected String capital;

		public QuizCountry(Country country) {
			Locale locale = new Locale("", country.name());
			iso3name = locale.getISO3Country();
			name = locale.getDisplayCountry();
			capital = capitals.get(name) != null 
					? capitals.get(name)[1]
					: "";
		}
		
		@Override
		public Image getFlag() {
			return null;
		}

		@Override
		public String getCapital() {
			return capital;
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
	
	class PictorialQuizCountry extends QuizCountry {
		private Image flag;

		public PictorialQuizCountry(Country country) {
			super(country);
			String countryAbbreviation = country.name().toLowerCase();
			String imageName = "/png250px/" + countryAbbreviation + ".png";
			flag = new Image(Png250px.class.getResourceAsStream(imageName));
		}

		@Override
		public Image getFlag() {
			return flag;
		}
	}

	List<IQuizCountry> quizCountries;
	int index;
	int completed;
	int originalSize;
	private Long seed;

	private IQuizCountry[] asia;
	private IQuizCountry[] africa;
	private IQuizCountry[] northAmerica;
	private IQuizCountry[] southAmerica;
	private IQuizCountry[] europe;
	private IQuizCountry[] australia;
	private IQuizCountry[] world;
	
	private Map<String, String[]> capitals;

	public Model() {
		this(null);
	}

	Model(Long seed) {
		this.seed = seed;

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
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		IQuizCountry russia = assignQuizCountry(Country.RU);
		asia = new IQuizCountry[] { russia };

		IQuizCountry algiers = assignQuizCountry(Country.DZ);
		africa = new IQuizCountry[] { algiers };

		IQuizCountry USA = assignQuizCountry(Country.US);
		IQuizCountry canada = assignQuizCountry(Country.CA);
		northAmerica = new IQuizCountry[] { USA, canada };

		IQuizCountry brazil = assignQuizCountry(Country.BR);
		southAmerica = new IQuizCountry[] { brazil };

		IQuizCountry unitedKingdom = assignQuizCountry(Country.GB);
		IQuizCountry ireland = assignQuizCountry(Country.IE);
		europe = new IQuizCountry[] { unitedKingdom, ireland };

		IQuizCountry AUS = assignQuizCountry(Country.AU);
		australia = new IQuizCountry[] { AUS };

		world = new IQuizCountry[Country.values().length];
		int i = 0;
        for (Country country : Country.values()) {
        	try {
	        	IQuizCountry quizCountry = assignQuizCountry(country);
	        	world[i++] = quizCountry;
        	}
        	catch (NullPointerException | MissingResourceException e) { 
        		System.err.println("Could not register " + country.getName());
        	}
        }
		
		changeQuizCountries("WORLD");

		index = randomCountryIndex(seed);
		completed = 0;
	}

	private IQuizCountry assignQuizCountry(Country country) {
		return notTesting() ? new PictorialQuizCountry(country) : new QuizCountry(country);
	}

	private boolean notTesting() {
		return this.seed == null;
	}

	private int randomCountryIndex(Long seed) {
		Random random = (seed == null) ? new Random() : new Random(seed);
		return random.nextInt(quizCountries.size());
	}

	public Image getFlagOfCountry() {
		return thisCountry().getFlag();
	}

	public String getIsoOfCountry() {
		return moreCountriesInQuiz() ? thisCountry().getAbbreviation() : "";
	}

	private IQuizCountry thisCountry() {
		return quizCountries.get(index);
	}

	public boolean moreCountriesInQuiz() {
		return countriesLeft() >= 1;
	}

	int countriesLeft() {
		return quizCountries.size();
	}

	public Image nextCountry() {
		completed++;
		if (index < quizCountries.size()) {
			quizCountries.remove(index);
		}
		if (!moreCountriesInQuiz()) {
			return null;
		}
		index = randomCountryIndex(seed);
		return getFlagOfCountry();
	}

	public void changeQuizCountries(String region) {
		IQuizCountry[] continent = chooseContinentFromCode(region);
		if (continent == null) {
			throw new IllegalArgumentException(String.format("Unknown region code '%s'", region));
		}
		completed = 0;
		originalSize = continent.length;
		quizCountries = new ArrayList<IQuizCountry>(Arrays.asList(continent));
		index = randomCountryIndex(seed);
	}

	private IQuizCountry[] chooseContinentFromCode(String code) {
		switch (code) {
			case "AS": return asia;
			case "AF": return africa;
			case "NA": return northAmerica;
			case "SA": return southAmerica;
			case "EU": return europe;
			case "AU": return australia;
			default: return world;
		}
	}

	public String getNameOfCountry() {
		return index < quizCountries.size() ? thisCountry().getName() : "";
	}
	
	public String getCapital() {
		return thisCountry().getCapital();
	}
}
