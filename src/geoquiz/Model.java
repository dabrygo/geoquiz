package geoquiz;

import java.util.Collections;
import java.util.List;

public class Model {
	public static final Continent DEFAULT_CONTINENT = Continent.NORTH_AMERICA;

	enum AnswerState {CORRECT, INCORRECT, UNKNOWN};
	
	List<IQuizCountry> masterList;
	List<IQuizCountry> quizCountries;
	int index;
	int correctTally;
	private AnswerState answerState;
	int incorrectTally;
	private IRegionFactory regionFactory;

	public Model(IRegionFactory regionFactory) {
		masterList = regionFactory.regionFrom(Continent.WORLD);
		answerState = AnswerState.UNKNOWN;
		correctTally = 0;
		incorrectTally = 0;
		this.regionFactory = regionFactory;
		changeQuizCountries(DEFAULT_CONTINENT);
	}

	public IQuizCountry currentCountry() {
		if (index < 0 || index >= quizCountries.size()) {
			return new NullCountry();
		}
		return quizCountries.get(index);
	}

	public IQuizCountry nextCountry() {
		if (moreQuestionsInQuiz()) {
			index++;
		}
		return currentCountry();
	}

	boolean moreQuestionsInQuiz() {
		return index < quizCountries.size();
	}

	public IQuizCountry previousCountry() {
		if (index >= 0) {
			index--;
		}
		return currentCountry();
	}
	

	public void changeQuizCountries(Continent regionCode) {
		Region continent = regionFactory.regionFrom(regionCode);
		if (continent == null) {
			throw new IllegalArgumentException(String.format("Unknown region code '%s'", regionCode));
		}
		quizCountries = continent;
		Collections.shuffle(quizCountries);
		index = 0;
		correctTally = 0;
		incorrectTally = 0;
	}
	
	public void setAnswerState(AnswerState answerState) {
		this.answerState = answerState;
	}
	
	public AnswerState getAnswerState() {
		return answerState;
	}
}
