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
        model = new Model(new RegionFactory());

        world = WorldBuilder.create().resolution(Resolution.HI_RES).mousePressHandler(evt -> {
            CountryPath countryPath = (CountryPath) evt.getSource();
            Locale locale = countryPath.getLocale();
            String selectedIso = model.currentCountry().getAbbreviation();

            Country country = Country.valueOf(locale.getCountry());
            System.out.println(locale.getCountry());
            System.out.println(selectedIso);
            IQuizCountry selectedQuizCountry = model.masterList.get(country.ordinal());
            if (model.moreQuestionsInQuiz()) {
                AnswerState answerState = selectedIso.equals(locale.getISO3Country()) ? AnswerState.CORRECT
                        : AnswerState.INCORRECT;
                model.setAnswerState(answerState);
                view.correct.setText("Correct: " + model.getCorrectTally());
                view.incorrect.setText("Incorrect: " + model.getIncorrectTally());
                view.updateGuessedCountry(selectedQuizCountry, answerState);
            }
        }).zoomEnabled(true).selectionEnabled(true).build();

        view = new View(world, model.currentCountry());

        view.getRegionRadioButtons().selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle oldToggle, Toggle newToggle) {
                Continent continent = (Continent) newToggle.getUserData();
                model.changeQuizCountries(continent);
                view.updateClueCountry(model.currentCountry());
                view.updateProgress(model.index, model.quizCountries.size());
                view.correct.setText("Correct: " + model.getCorrectTally());
                view.incorrect.setText("Incorrect: " + model.getIncorrectTally());
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
                if (result.get() == ButtonType.OK) {
                    goToNextCountry();
                    model.setAnswerState(AnswerState.INCORRECT);
                    view.correct.setText("Correct: " + model.getCorrectTally());
                    view.incorrect.setText("Incorrect: " + model.getIncorrectTally());
                }
            } else {
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
    }

    private void goToNextCountry() {
        view.updateClueCountry(model.nextCountry(model.getAnswerState()));
        view.updateProgress(model.index, model.quizCountries.size());
        view.updateGuessedCountry(new NullCountry(), AnswerState.UNKNOWN);
    }

    @Override
    public void stop() {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
