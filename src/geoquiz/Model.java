package geoquiz;

import java.util.Collections;
import java.util.List;

public class Model {

	List<IQuizCountry> masterList;
	List<IQuizCountry> quizCountries;
	int index;


	public Model() {
		masterList = RegionFactory.regionFrom("WORLD");

		changeQuizCountries("NA");
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
		if (!moreCountriesInQuiz()) {
			return new NullCountry();
		}
		return currentCountry();
	}

	public void changeQuizCountries(String region) {
		Region continent = RegionFactory.regionFrom(region);
		if (continent == null) {
			throw new IllegalArgumentException(String.format("Unknown region code '%s'", region));
		}
		quizCountries = continent;
		Collections.shuffle(quizCountries);
		index = 0;
	}
}
