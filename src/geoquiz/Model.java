package geoquiz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Random;

import eu.hansolo.fx.world.Country;
import javafx.scene.image.Image;

public class Model {

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
	private Random random;
	
	public Model() {
		this(null);
	}

	Model(Long seed) {
		this.seed = seed;
		random = (seed == null) ? new Random() : new Random(seed);
		
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
