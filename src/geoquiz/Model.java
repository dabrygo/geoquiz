package geoquiz;

import java.util.Collections;
import java.util.List;

public class Model {
	public static final Continent DEFAULT_CONTINENT = Continent.NORTH_AMERICA;
	List<IQuizCountry> masterList;
	List<IQuizCountry> quizCountries;
	int index;

	public Model() {
		masterList = RegionFactory.regionFrom(Continent.WORLD, true);

		changeQuizCountries(DEFAULT_CONTINENT);
	}

	public IQuizCountry currentCountry() {
		if (index < 0 || index >= quizCountries.size()) {
			return new NullCountry();
		}
		return quizCountries.get(index);
	}

	public IQuizCountry nextCountry() {
		if (index <= quizCountries.size()) {
			index++;
		}
		return currentCountry();
	}

	public boolean lastQuestion() {
		return index == quizCountries.size() - 1;
	}

	public IQuizCountry previousCountry() {
		if (index >= 0) {
			index--;
		}
		return currentCountry();
	}
	

	public void changeQuizCountries(Continent regionCode) {
		Region continent = RegionFactory.regionFrom(regionCode, true);
		if (continent == null) {
			throw new IllegalArgumentException(String.format("Unknown region code '%s'", regionCode));
		}
		quizCountries = continent;
		Collections.shuffle(quizCountries);
		index = 0;
	}
}
