package geoquiz;
import java.util.Locale;
import java.util.Optional;

import eu.hansolo.fx.world.Country;
import eu.hansolo.fx.world.CountryPath;
import eu.hansolo.fx.world.World;
import eu.hansolo.fx.world.World.Resolution;
import eu.hansolo.fx.world.WorldBuilder;
import geoquiz.Model.AnswerState;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Toggle;
import javafx.stage.Stage;

public class Controller extends Application {
	private World world;
	private Model model;
	private View view;
	
	@SuppressWarnings("unchecked")
	@Override 
	public void start(Stage stage) {
		model = new Model(false);

		world = WorldBuilder.create()
				.resolution(Resolution.HI_RES)
				.mousePressHandler(evt -> {
					CountryPath countryPath = (CountryPath) evt.getSource();
					Locale locale = countryPath.getLocale();
					String selectedIso = model.currentCountry().getAbbreviation();

					Country country = Country.valueOf(locale.getCountry());
					System.out.println(locale.getCountry());
					System.out.println(selectedIso);
					IQuizCountry selectedQuizCountry = model.masterList.get(country.ordinal());
					if (model.moreQuestionsInQuiz()){
						AnswerState answerState = selectedIso.equals(locale.getISO3Country())
												? AnswerState.CORRECT
												: AnswerState.INCORRECT;
						if (answerState.equals(AnswerState.CORRECT)) {
							if (model.getAnswerState().equals(AnswerState.INCORRECT)) {
								model.incorrectTally--;
							}
							model.correctTally++;
							view.correct.setText("Correct: " + model.correctTally);
						}
						else {
							if (model.getAnswerState().equals(AnswerState.UNKNOWN)) {
								model.incorrectTally++;
							}
							view.incorrect.setText("Incorrect: " + model.incorrectTally);
						}

						model.setAnswerState(answerState);
						view.updateGuessedCountry(selectedQuizCountry, answerState);
					}
				})
				.zoomEnabled(true)
				.selectionEnabled(true)
				.build();
		
		view = new View(world, model.currentCountry());

		view.getRegionRadioButtons().selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
		    public void changed(ObservableValue<? extends Toggle> ov, Toggle oldToggle, Toggle newToggle) {
				Continent continent = (Continent)newToggle.getUserData();
				model.changeQuizCountries(continent, false);
				view.updateClueCountry(model.currentCountry());
				view.updateProgress(model.index, model.quizCountries.size());
				view.correct.setText("Correct: " + model.correctTally);
				view.incorrect.setText("Incorrect: " + model.incorrectTally);
		    }
		});
		
		view.getPreviousButton().setOnAction(e -> {
			goToPreviousCountry();
		});
		
		view.getNextButton().setOnAction(e -> {
			if (!model.moreQuestionsInQuiz()) {
				return;
			}
			if (!model.getAnswerState().equals(AnswerState.CORRECT)) {
				Alert alert = view.skipCountryDialog();

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK){
					goToNextCountry();
					if (!model.getAnswerState().equals(AnswerState.INCORRECT)) {
						model.incorrectTally++;
					}
					view.incorrect.setText("Incorrect: " + model.incorrectTally);
				} 
			}
			else {
				goToNextCountry();
			}
		});

		Scene scene = new Scene(view);

		stage.setTitle("World Map");
		stage.setScene(scene);
		stage.show();
	}

	private void goToPreviousCountry() {
		view.updateClueCountry(model.previousCountry());
		view.updateProgress(model.index, model.quizCountries.size());
		view.updateGuessedCountry(new NullCountry(), AnswerState.UNKNOWN);
		model.setAnswerState(AnswerState.UNKNOWN);
	}

	private void goToNextCountry() {
		view.updateClueCountry(model.nextCountry());
		view.updateProgress(model.index, model.quizCountries.size());
		view.updateGuessedCountry(new NullCountry(), AnswerState.UNKNOWN);
		model.setAnswerState(AnswerState.UNKNOWN);
	}
	
	@Override 
	public void stop() {
		System.exit(0);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
