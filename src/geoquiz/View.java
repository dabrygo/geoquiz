package geoquiz;

import eu.hansolo.fx.world.World;
import geoquiz.Model.AnswerState;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class View extends SplitPane {
    private VBox center;
    private BorderPane bottom;
    private Text progress;
    private ToggleGroup regions;
    private Button nextButton;
    CountryDisplay clueCountryDisplay;
    CountryDisplay guessedCountryDisplay;
    private Button previousButton;
    private FlowPane right;
    private Text result;
    private Text correct;
    private Text incorrect;
    private Button showAnswerButton;
    CheckBox[] clueTypes;

    public View(World world, IQuizCountry initialCountry) {
        setOrientation(Orientation.VERTICAL);
        setDividerPosition(0, 0.75); // needed to center zoom into country

        clueCountryDisplay = new CountryDisplay(initialCountry);

        makeCenter();

        right = new FlowPane(Orientation.VERTICAL);
        result = new Text();
        result.setFont(new Font(24.0));
        correct = new Text("Correct: " + 0);
        correct.setFont(new Font(24.0));
        incorrect = new Text("Incorrect: " + 0);
        incorrect.setFont(new Font(24.0));
        guessedCountryDisplay = new CountryDisplay(new NullCountry());
        right.getChildren().addAll(guessedCountryDisplay);

        bottom = new BorderPane();

        HBox navigator = makeNavigationButtons();

        result.setTextAlignment(TextAlignment.CENTER);
        correct.setTextAlignment(TextAlignment.LEFT);
        HBox top = new HBox(incorrect, result, correct);
        bottom.setTop(top);
        bottom.setLeft(clueCountryDisplay);
        bottom.setCenter(center);
        bottom.setRight(guessedCountryDisplay);
        BorderPane.setAlignment(guessedCountryDisplay, Pos.TOP_LEFT);
        bottom.setBottom(navigator);

        StackPane map = new StackPane(world);
        map.setBackground(
                new Background(new BackgroundFill(world.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));

        getItems().addAll(map, bottom);
    }

    private HBox makeNavigationButtons() {
        previousButton = new Button("Previous");
        showAnswerButton = new Button("Show Answer");
        nextButton = new Button("Next");
        HBox navigator = new HBox(previousButton, showAnswerButton, nextButton);
        HBox.setHgrow(previousButton, Priority.ALWAYS);
        HBox.setHgrow(showAnswerButton, Priority.ALWAYS);
        HBox.setHgrow(nextButton, Priority.ALWAYS);
        previousButton.setMaxWidth(Double.MAX_VALUE);
        showAnswerButton.setMaxWidth(Double.MAX_VALUE);
        nextButton.setMaxWidth(Double.MAX_VALUE);
        return navigator;
    }

    private void makeCenter() {
        regions = new ToggleGroup();

        RadioButton asia = newRegion("Asia", Continent.ASIA);
        RadioButton africa = newRegion("Africa", Continent.AFRICA);
        RadioButton northAmerica = newRegion("North America", Continent.NORTH_AMERICA);
        RadioButton southAmerica = newRegion("South America", Continent.SOUTH_AMERICA);
        RadioButton europe = newRegion("Europe", Continent.EUROPE);
        RadioButton australia = newRegion("Australia", Continent.AUSTRALIA);
        RadioButton wholeWorld = newRegion("World", Continent.WORLD);

        for (Toggle toggle : regions.getToggles()) {
            toggle.setSelected(toggle.getUserData().equals(Model.DEFAULT_CONTINENT));
        }

        progress = new Text();
        progress.setFont(new Font(36.0));
        
        clueTypes = new CheckBox[3];
        clueTypes[0] = new CheckBox("Name");
        clueTypes[1] = new CheckBox("Capital");
        clueTypes[2] = new CheckBox("Flag");
        
        center = new VBox(progress, asia, africa, northAmerica, southAmerica, europe, australia, wholeWorld, clueTypes[0], clueTypes[1], clueTypes[2]);
    }

    private RadioButton newRegion(String name, Continent continent) {
        RadioButton region = new RadioButton(name);
        region.setUserData(continent);
        region.setToggleGroup(regions);
        return region;
    }

    public void updateProgress(int currentQuestion, int total) {
        progress.setText(String.format("%d / %d", currentQuestion, total));
    }

    public void updateGuessedCountry(IQuizCountry selectedCountry, AnswerState answerState) {
        String resultText;
        switch (answerState) {
        case CORRECT:
            resultText = "Correct!";
            result.setFill(Color.GREEN);
            break;
        case INCORRECT:
            resultText = "Incorrect";
            result.setFill(Color.RED);
            break;
        default:
            resultText = "";
            result.setFill(null);
        }
        result.setText(resultText);
        guessedCountryDisplay.updateCountry(selectedCountry);
    }

    public void updateClueCountry(IQuizCountry selectedCountry) {
        clueCountryDisplay.updateCountry(selectedCountry);
    }

    public void updateStatistics(int correct, int incorrect) {
        this.correct.setText("Correct: " + correct);     
        this.incorrect.setText("Incorrect: " + incorrect);
    }
    
    public Button getPreviousButton() {
        return previousButton;
    }

    public Button getNextButton() {
        return nextButton;
    }

    public ToggleGroup getRegionRadioButtons() {
        return regions;
    }

    public Alert skipCountryDialog() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Skip Country");
        alert.setHeaderText("");
        alert.setContentText("Do you want to skip this country?");
        return alert;
    }
    
    public Button getShowAnswerButton() {
        return showAnswerButton;
    }

    public void showOrHideNames() {
        clueCountryDisplay.showNames(clueTypes[0].isSelected());
        guessedCountryDisplay.showNames(clueTypes[0].isSelected());
    }
    
    public void showOrHideCapitals() {
        clueCountryDisplay.showCapital(clueTypes[1].isSelected());
        guessedCountryDisplay.showCapital(clueTypes[1].isSelected());
    }

    public void showOrHideFlags() {
        clueCountryDisplay.showFlag(clueTypes[2].isSelected());
        guessedCountryDisplay.showFlag(clueTypes[2].isSelected());
        
    }
}
