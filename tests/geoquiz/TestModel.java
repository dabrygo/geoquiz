package geoquiz;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

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

	@Test
	public void test_empty_quiz_when_all_questions_completed() {
		model.index = model.lastCountry();
		assertFalse(model.moreCountriesInQuiz());
	}
	
	@Test
	public void test_null_flag_when_all_questions_completed() {
		model.index = model.lastCountry();
		assertNull(model.flagOfNextCountry());
	}

}
