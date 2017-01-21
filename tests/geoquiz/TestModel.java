package geoquiz;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import eu.hansolo.fx.world.Country;
import geoquiz.Model.AnswerState;

public class TestModel {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model;

    @Before
    public void setUp() {
        model = new Model(new LightweightRegionFactory());
    }

    private void completeAllQuestions() {
        model.index = model.quizCountries.size();
    }

    @Test
    public void test_nonempty_quiz_by_default() {
        assertTrue(model.moreQuestionsInQuiz());
    }

    @Test
    public void test_no_more_questions_in_quiz_when_all_questions_completed() {
        completeAllQuestions();
        assertFalse(model.moreQuestionsInQuiz());
    }

    @Test
    public void test_null_country_when_all_questions_completed() {
        completeAllQuestions();
        assertTrue(model.nextCountry(null) instanceof NullCountry);
    }

    @Test
    public void test_null_flag_when_all_questions_completed() {
        completeAllQuestions();
        assertNull(model.nextCountry(null).getFlag());
    }

    @Test
    public void test_index_increments_when_move_to_next_question() {
        int originalIndex = model.index;
        model.nextCountry(null);
        assertEquals(originalIndex + 1, model.index);
    }

    @Test
    public void test_name_of_country_when_all_questions_completed() {
        completeAllQuestions();
        assertEquals("", model.currentCountry().getName());
    }

    @Test
    public void test_number_completed_increments_when_next_country_called() {
        model.nextCountry(null);
        assertEquals(1, model.index);
    }

    @Test
    public void test_number_completed_resets_when_new_continent_chosen() {
        model.nextCountry(null);
        model.changeQuizCountries(Continent.ASIA);
        assertEquals(0, model.index);
    }

    @Ignore("Don't know first country when using Collections.shuffle")
    @Test
    public void test_know_full_name_of_country() {
        assertEquals("Vietnam", model.currentCountry().getName());
    }

    @Ignore("Don't know first country when using Collections.shuffle")
    @Test
    public void test_know_iso_code_of_country() {
        assertEquals("VNM", model.currentCountry().getAbbreviation());
    }

    @Test
    public void test_every_country_has_a_name() {
        for (Country country : Country.values()) {
            Locale locale = new Locale("", country.name());
            assertFalse(locale.getDisplayCountry().equals(""));
        }
    }

    @Test
    public void test_cannot_go_before_first_question() {
        model.index = 0;
        model.previousCountry();
        assertEquals(0, model.index);
    }

    @Test
    public void test_country_changes_when_going_forward() {
        IQuizCountry firstCountry = model.currentCountry();
        model.nextCountry(AnswerState.CORRECT);
        assertFalse(firstCountry.equals(model.currentCountry()));
    }

    @Test
    public void test_can_go_back_to_first_country() {
        IQuizCountry firstCountry = model.currentCountry();
        model.nextCountry(AnswerState.CORRECT);
        assertEquals(firstCountry, model.previousCountry());
    }
}
