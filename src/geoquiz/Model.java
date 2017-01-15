package geoquiz;
import java.util.ArrayList;
import java.util.Arrays;
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
		
		public String toString() {
			return name;
		}
	}

	List<QuizCountry> quizCountries;
	int index;
	private Long seed;
	
	private QuizCountry[] asia;
	private QuizCountry[] africa;
	private QuizCountry[] northAmerica;
	private QuizCountry[] southAmerica;
	private QuizCountry[] europe;
	private QuizCountry[] australia;
	
	public Model() {
		this(null);
	}

	Model(Long seed) {
		this.seed = seed;
		
		QuizCountry RUS = new QuizCountry("RUS");
		asia = new QuizCountry[] { RUS };
		
		QuizCountry DZA = new QuizCountry("DZA");
		africa = new QuizCountry[] { DZA };
		
		QuizCountry USA = new QuizCountry("USA");
		QuizCountry CAN = new QuizCountry("CAN");
		northAmerica = new QuizCountry[] { USA, CAN };
		
		QuizCountry BRA = new QuizCountry("BRA");
		southAmerica = new QuizCountry[] { BRA };
		
		QuizCountry GBR = new QuizCountry("GBR");
		QuizCountry IRL = new QuizCountry("IRL");
		europe = new QuizCountry[] { GBR, IRL };
		
		QuizCountry AUS = new QuizCountry("AUS");
		australia = new QuizCountry[] { AUS };
		
		changeQuizCountries("NA");
		
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
	
	public void changeQuizCountries(String region) {
		if (region.equals("AS")) {
			quizCountries = new ArrayList<QuizCountry>(Arrays.asList(asia));
		}
		else if (region.equals("AF")) {
			quizCountries = new ArrayList<QuizCountry>(Arrays.asList(africa));
		}
		else if (region.equals("NA")) {
			quizCountries = new ArrayList<QuizCountry>(Arrays.asList(northAmerica));
		}
		else if (region.equals("SA")) {
			quizCountries = new ArrayList<QuizCountry>(Arrays.asList(southAmerica));
		}
		else if (region.equals("EU")) {
			quizCountries = new ArrayList<QuizCountry>(Arrays.asList(europe));
		}
		else if (region.equals("AU")) {
			quizCountries = new ArrayList<QuizCountry>(Arrays.asList(australia));
		}
		else {
			throw new IllegalArgumentException(String.format("Unknown region code '%s'", region));
		}
		index = randomCountryIndex(seed);
	}
}
