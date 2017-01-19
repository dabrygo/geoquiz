package geoquiz;

import java.util.Collections;
import java.util.List;

public class Model {
	public static final Continent DEFAULT_CONTINENT = Continent.SOUTH_AMERICA;
	List<IQuizCountry> masterList;
	List<IQuizCountry> quizCountries;
	int index;

	public Model() {
		masterList = RegionFactory.regionFrom(Continent.WORLD);

		changeQuizCountries(DEFAULT_CONTINENT);
	}

	public IQuizCountry currentCountry() {
		if (quizCountries == null || !moreCountriesInQuiz()) {
			return new NullCountry();
		}
		return quizCountries.get(index);
	}

	public boolean moreCountriesInQuiz() {
		return index < quizCountries.size();
	}

	public IQuizCountry nextCountry() {
		index++;
		return currentCountry();
	}

	public void changeQuizCountries(Continent regionCode) {
		Region continent = RegionFactory.regionFrom(regionCode);
		if (continent == null) {
			throw new IllegalArgumentException(String.format("Unknown region code '%s'", regionCode));
		}
		quizCountries = continent;
		Collections.shuffle(quizCountries);
		index = 0;
	}
}
