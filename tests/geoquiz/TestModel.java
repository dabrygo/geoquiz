package geoquiz;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TestModel {

	private Model model;

	@Before
	public void setUp() {
		model = new Model(new Long(1));
	}
	
	private void completeAllQuestions() {
		model.quizCountries.clear();
	}

	@Test
	public void test_nonempty_quiz_by_default() {
		assertTrue(model.moreCountriesInQuiz());
	}

	@Test
	public void test_empty_quiz_when_all_questions_completed() {
		completeAllQuestions();
		assertFalse(model.moreCountriesInQuiz());
	}
	
	@Test
	public void test_null_flag_when_all_questions_completed() {
		completeAllQuestions();
		assertNull(model.flagOfNextCountry());
	}
	
	@Test
	public void test_one_less_country_when_move_to_next_question() {
		int originalSize = model.countriesLeft();
		model.flagOfNextCountry();
		assertTrue(model.countriesLeft() < originalSize);
	}
	
	@Test
	public void test_name_of_country_when_all_questions_completed() {
		completeAllQuestions();
		assertEquals("", model.getNameOfCountry());
	}

}
