package geoquiz;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;

public class Model {

	class QuizCountry {
		public String name;
		public Image flag;

		public QuizCountry(String name) {
			this.name = name;
			this.flag = new Image(name + ".png");
		}
	}

	public List<QuizCountry> quizCountries;
	private int index;
	
	public Model() {
		index = 0;
		Model.QuizCountry USA = new Model.QuizCountry("USA");
		Model.QuizCountry CAN = new Model.QuizCountry("CAN");
		
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
		index++;
		return getFlagOfCountry();
	}
}
