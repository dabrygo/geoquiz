package geoquiz;

import eu.hansolo.fx.world.World;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
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
	private CountryDisplay clues;
	private CountryDisplay guessed;
	private Button previousButton;
	private FlowPane right;
	private Text result;

	enum AnswerState {Correct, Incorrect, Unknown};
	
	public View(World world, IQuizCountry initialCountry) {
		setOrientation(Orientation.VERTICAL);

		clues = new CountryDisplay(initialCountry);

		makeCenter();

		right = new FlowPane(Orientation.VERTICAL);
		result = new Text();
		result.setFont(new Font(24.0));;
		guessed = new CountryDisplay(new NullCountry());
		right.getChildren().addAll(result, guessed);

		bottom = new BorderPane();

		previousButton = new Button("Previous");
		nextButton = new Button("Next");
		HBox navigator = new HBox(previousButton, nextButton);
		HBox.setHgrow(previousButton, Priority.ALWAYS);
		HBox.setHgrow(nextButton, Priority.ALWAYS);
		previousButton.setMaxWidth(Double.MAX_VALUE);
		nextButton.setMaxWidth(Double.MAX_VALUE);

		result.setTextAlignment(TextAlignment.CENTER);
		bottom.setTop(result);
		BorderPane.setAlignment(result, Pos.CENTER);
		bottom.setLeft(clues);
		bottom.setCenter(center);
		bottom.setRight(guessed);
		BorderPane.setAlignment(guessed, Pos.TOP_LEFT);
		bottom.setBottom(navigator);

		StackPane map = new StackPane(world);
		map.setBackground(
				new Background(new BackgroundFill(world.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));

		getItems().addAll(map, bottom);
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
		center = new VBox(progress, asia, africa, northAmerica, southAmerica, europe, australia, wholeWorld);
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
		switch(answerState) {
		case Correct: 
			resultText = "Correct!"; 
			result.setFill(Color.GREEN);
			break;
		case Incorrect: 
			resultText = "Incorrect"; 
			result.setFill(Color.RED);
			break;
		default: 
			resultText = "";
			result.setFill(null);
		}
		result.setText(resultText);
		guessed.updateCountry(selectedCountry);
	}

	public void updateClueCountry(IQuizCountry selectedCountry) {
		clues.updateCountry(selectedCountry);
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
}
