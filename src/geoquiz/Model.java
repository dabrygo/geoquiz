package geoquiz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Model {

	List<IQuizCountry> quizCountries;
	int index;
	int completed;
	int originalSize;
	private Long seed;

	IQuizCountry[] masterList;
	private Random random;

	public Model() {
		this(null);
	}

	Model(Long seed) {
		this.seed = seed;
		random = (seed == null) ? new Random() : new Random(seed);

		masterList = RegionFactory.regionFrom("WORLD").quizCountries;

		changeQuizCountries("NA");

		index = randomCountryIndex(seed);
		completed = 0;
	}

	private int randomCountryIndex(Long seed) {
		return random.nextInt(quizCountries.size());
	}

	public IQuizCountry currentCountry() {
		if (quizCountries == null || index > quizCountries.size()) {
			return new NullCountry();
		}
		return quizCountries.get(index);
	}

	public boolean moreCountriesInQuiz() {
		return countriesLeft() >= 1;
	}

	int countriesLeft() {
		return quizCountries.size();
	}

	public IQuizCountry nextCountry() {
		completed++;
		if (index < quizCountries.size()) {
			quizCountries.remove(index);
		}
		if (!moreCountriesInQuiz()) {
			return null;
		}
		index = randomCountryIndex(seed);
		return currentCountry();
	}

	public void changeQuizCountries(String region) {
		IQuizCountry[] continent = RegionFactory.regionFrom(region).quizCountries;
		if (continent == null) {
			throw new IllegalArgumentException(String.format("Unknown region code '%s'", region));
		}
		completed = 0;
		originalSize = continent.length;
		quizCountries = new ArrayList<IQuizCountry>(Arrays.asList(continent));
		index = randomCountryIndex(seed);
	}
}
