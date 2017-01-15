package geoquiz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import eu.hansolo.fx.world.Country;
import eu.hansolo.fx.world.CountryPath;
import javafx.scene.image.Image;

public class Model {

	class QuizCountry {
		private String iso3name;
		private Image flag;
		private String name;

		public QuizCountry(Country country) {
			CountryPath countryPath = new CountryPath(country.name());
			this.iso3name = countryPath.getLocale().getISO3Country();
			this.flag = new Image(this.getClass().getResourceAsStream("/geoquiz/" + iso3name + ".png"));
			this.name = new CountryPath(country.name()).getLocale().getDisplayCountry();
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

	public Model() {
		this(null);
	}

	Model(Long seed) {
		this.seed = seed;

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

		changeQuizCountries("NA");

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
			default: return null;
		}
	}

	public String getNameOfCountry() {
		return index < quizCountries.size() ? quizCountries.get(index).name : "";
	}
}
