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
		if (quizCountries == null) {
			return new NullCountry();
		}
		return quizCountries.get(index);
	}

	public IQuizCountry nextCountry() {
		if (!lastQuestion()) {
			index++;
		}
		return currentCountry();
	}

	public boolean lastQuestion() {
		return index == quizCountries.size() - 1;
	}

	public IQuizCountry previousCountry() {
		if (!firstQuestion()) {
			index--;
		}
		return currentCountry();
	}
	
	private boolean firstQuestion() {
		return index == 0;
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
