package geoquiz;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestModel {
	@Rule 
	public ExpectedException thrown = ExpectedException.none();
	
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
		assertNull(model.nextCountry());
	}
	
	@Test
	public void test_one_less_country_when_move_to_next_question() {
		int originalSize = model.countriesLeft();
		model.nextCountry();
		assertTrue(model.countriesLeft() < originalSize);
	}
	
	@Test
	public void test_name_of_country_when_all_questions_completed() {
		completeAllQuestions();
		assertEquals("", model.getNameOfCountry());
	}
	
	@Test
	public void test_number_completed_increments_when_next_country_called() {
		model.nextCountry();
		assertEquals(1, model.completed);
	}
	
	@Test
	public void test_number_completed_resets_when_new_continent_chosen() {
		model.nextCountry();
		model.changeQuizCountries("AS");
		assertEquals(0, model.completed);
	}
	
	@Test
	public void test_illegal_continent_code_throws_exception() {
		thrown.expect(IllegalArgumentException.class);
		model.changeQuizCountries("XXX");
	}
}
