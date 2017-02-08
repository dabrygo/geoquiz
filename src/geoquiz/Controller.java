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
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Controller extends Application {
    private World world;
    private Model model;
    private View view;
    private CountryPath countryPath;
    private Country pressedCountry;

    @SuppressWarnings("unchecked")
    @Override
    public void start(Stage stage) {
        model = new Model(new RegionFactory());

        world = WorldBuilder.create().resolution(Resolution.HI_RES).mousePressHandler(evt -> {
            countryPath = (CountryPath) evt.getSource();
            Locale locale = countryPath.getLocale();
            String desiredIso = model.currentCountry().getAbbreviation();

            pressedCountry = Country.valueOf(locale.getCountry());
            IQuizCountry selectedQuizCountry = model.masterList.get(pressedCountry.ordinal());
            if (model.moreQuestionsInQuiz() || model.getIndexOfNextIncorrectCountry() >= 0) {
                AnswerState answerState = desiredIso.equals(locale.getISO3Country()) ? AnswerState.CORRECT
                        : AnswerState.INCORRECT;
                model.setAnswerState(answerState);
              
                view.updateStatistics(model.getCorrectTally(), model.getIncorrectTally());;
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
                view.updateStatistics(model.getCorrectTally(), model.getIncorrectTally());;
            }
        });
        
        view.clueTypes[0].setSelected(model.showName);        
        view.showOrHideNames();
        view.clueTypes[0].setOnAction(e -> view.showOrHideNames());
        
        view.clueTypes[1].setSelected(model.showCapital);
        view.showOrHideCapitals();
        view.clueTypes[1].setOnAction(e -> view.showOrHideCapitals());
        
        view.clueTypes[2].setSelected(model.showFlag);
        view.showOrHideFlags();        
        view.clueTypes[2].setOnAction(e -> view.showOrHideFlags());

        assignActionsToNavigationButtons();

        Scene scene = new Scene(view);

        stage.setTitle("World Map");
        stage.setScene(scene);
        stage.show();
    }

    private void assignActionsToNavigationButtons() {
        view.getPreviousButton().setOnAction(e -> {
            goToPreviousCountry();
        });
        
        view.getShowAnswerButton().setOnAction(e -> {
            model.currentCountry().setColor(Color.rgb(157,255,120));
            world.resetZoom(); // FIXME Don't reset zoom to zoom into country
            world.zoomToCountry(model.currentCountry().getCountry());
        });

        view.getNextButton().setOnAction(e -> {
            if (!model.moreQuestionsInQuiz()) {
                if (model.hasReviewableQuestions()) {
                    if (!model.hasSeenAllQuestions) {
                        Alert alert = view.reviewIncorrectDialog();
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK) {
                            goToNextCountry();
                        }
                    }
                    else {
                        goToNextCountry();
                    }
                }
                return;
            }
            if (!model.getAnswerState().equals(AnswerState.CORRECT)) {
                Alert alert = view.skipCountryDialog();
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    goToNextCountry();
                    model.setAnswerState(AnswerState.INCORRECT);
                    view.updateStatistics(model.getCorrectTally(), model.getIncorrectTally());;
                }
            } else {
                goToNextCountry();
            }
        });
    }

    private void goToPreviousCountry() {
        unhighlightSelectedCountry();
        view.updateClueCountry(model.previousCountry());
        view.updateProgress(model.index, model.quizCountries.size());
        view.updateGuessedCountry(new NullCountry(), AnswerState.UNKNOWN);
    }

    private void unhighlightSelectedCountry() {
        if (countryPath != null) {
            countryPath.setFill(Color.rgb(217, 217, 220, 1.0));
            countryPath = null;
        }
    }

    private void goToNextCountry() {
        unhighlightSelectedCountry();
        view.updateClueCountry(model.nextCountry(model.getAnswerState()));
        view.updateProgress(model.index, model.quizCountries.size());
        view.updateGuessedCountry(new NullCountry(), AnswerState.UNKNOWN);
        world.resetZoom();
    }

    @Override
    public void stop() {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
