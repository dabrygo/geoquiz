package geoquiz;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.image.Image;

public class Model {

	class QuizCountry {
		private String name;
		private Image flag;

		public QuizCountry(String name) {
			this.name = name;
			this.flag = new Image(this.getClass().getResourceAsStream("/geoquiz/" + name + ".png"));
		}
	}

	List<QuizCountry> quizCountries;
	int index;
	private Long seed;
	
	public Model() {
		this(null);
	}

	Model(Long seed) {
		this.seed = seed;
		QuizCountry USA = new QuizCountry("USA");
		QuizCountry CAN = new QuizCountry("CAN");
		
		quizCountries = new ArrayList<>();
		quizCountries.add(USA);
		quizCountries.add(CAN);
		
		index = randomCountryIndex(seed);
	}
	
	private int randomCountryIndex(Long seed) {
		Random random = (seed == null)
				      ? new Random() 
				      : new Random(seed);
		return random.nextInt(quizCountries.size());
	}

	public Image getFlagOfCountry() {
		return quizCountries.get(index).flag;
	}
	
	public String getNameOfCountry() {
		return moreCountriesInQuiz() 
			   ? quizCountries.get(index).name
			   : "";
	}
	
	public boolean moreCountriesInQuiz() {
		return countriesLeft() >= 1;
	}
	
	int countriesLeft() {
		return quizCountries.size();
	}
	
	public Image flagOfNextCountry() {
		quizCountries.remove(index);
		if (!moreCountriesInQuiz()) {
			return null; 
		}
		index = randomCountryIndex(seed);
		return getFlagOfCountry();
	}
}
