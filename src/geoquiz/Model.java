package geoquiz;
import java.util.ArrayList;
import java.util.List;

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
	private int index;
	
	public Model() {
		index = 0;
		QuizCountry USA = new QuizCountry("USA");
		QuizCountry CAN = new QuizCountry("CAN");
		
		quizCountries = new ArrayList<>();
		quizCountries.add(USA);
		quizCountries.add(CAN);
	}
	
	public Image getFlagOfCountry() {
		return quizCountries.get(index).flag;
	}
	
	public String getNameOfCountry() {
		return quizCountries.get(index).name;
	}
	
	public boolean moreCountriesInQuiz() {
		return index < quizCountries.size() - 1;
	}
	
	public Image flagOfNextCountry() {
		if (moreCountriesInQuiz()) {
			index++;
			return getFlagOfCountry();
		} 
		return null; 
	}
}
