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
import eu.hansolo.fx.world.CountryPath;
import javafx.scene.image.Image;
import png250px.Png250px;

public class Model {

	class QuizCountry {
		private String iso3name;
		private Image flag;
		private String name;
		private String capital;

		public QuizCountry(Country country) {
			CountryPath countryPath = new CountryPath(country.name());
			Locale locale = countryPath.getLocale();
			this.iso3name = locale.getISO3Country();
			System.out.println(locale.getDisplayCountry());
			this.flag = new Image(Png250px.class.getResourceAsStream("/png250px/" + country.name().toLowerCase() + ".png"));
			this.name = locale.getDisplayCountry();
			this.capital = capitals.get(name) != null 
					     ? capitals.get(name)[1]
					     : "";
		}

		public String toString() {
			return iso3name;
		}
	}

	List<QuizCountry> quizCountries;
	int index;
	int completed;
	int originalSize;
	private Long seed;

	private QuizCountry[] asia;
	private QuizCountry[] africa;
	private QuizCountry[] northAmerica;
	private QuizCountry[] southAmerica;
	private QuizCountry[] europe;
	private QuizCountry[] australia;
	private QuizCountry[] world;
	
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
		
		QuizCountry russia = new QuizCountry(Country.RU);
		asia = new QuizCountry[] { russia };

		QuizCountry algiers = new QuizCountry(Country.DZ);
		africa = new QuizCountry[] { algiers };

		QuizCountry USA = new QuizCountry(Country.US);
		QuizCountry canada = new QuizCountry(Country.CA);
		northAmerica = new QuizCountry[] { USA, canada };

		QuizCountry brazil = new QuizCountry(Country.BR);
		southAmerica = new QuizCountry[] { brazil };

		QuizCountry unitedKingdom = new QuizCountry(Country.GB);
		QuizCountry ireland = new QuizCountry(Country.IE);
		europe = new QuizCountry[] { unitedKingdom, ireland };

		QuizCountry AUS = new QuizCountry(Country.AU);
		australia = new QuizCountry[] { AUS };

			world = new QuizCountry[Country.values().length];
			int i = 0;
	        for (Country country : Country.values()) {
	        	try {
		        	QuizCountry quizCountry = new QuizCountry(country);
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

	private int randomCountryIndex(Long seed) {
		Random random = (seed == null) ? new Random() : new Random(seed);
		return random.nextInt(quizCountries.size());
	}

	public Image getFlagOfCountry() {
		return quizCountries.get(index).flag;
	}

	public String getIsoOfCountry() {
		return moreCountriesInQuiz() ? quizCountries.get(index).iso3name : "";
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
		QuizCountry[] continent = chooseContinentFromCode(region);
		if (continent == null) {
			throw new IllegalArgumentException(String.format("Unknown region code '%s'", region));
		}
		completed = 0;
		originalSize = continent.length;
		quizCountries = new ArrayList<QuizCountry>(Arrays.asList(continent));
		index = randomCountryIndex(seed);
	}

	private QuizCountry[] chooseContinentFromCode(String code) {
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
		return index < quizCountries.size() ? quizCountries.get(index).name : "";
	}
	
	public String getCapital() {
		return quizCountries.get(index).capital;
	}
}
