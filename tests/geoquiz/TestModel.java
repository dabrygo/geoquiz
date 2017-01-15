package geoquiz;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import geoquiz.Model.QuizCountry;

public class TestModel {

	private Model model;

	@Before
	public void setUp() {
		model = new Model();
	}

	@Test
	public void test_nonempty_quiz_by_default() {
		assertTrue(model.moreCountriesInQuiz());
	}

	@SuppressWarnings("unused")
	@Test
	public void test_empty_quiz_when_all_countries_consumed() {
		for (QuizCountry country : model.quizCountries) {
			model.flagOfNextCountry();
		}
		assertFalse(model.moreCountriesInQuiz());
	}

}
