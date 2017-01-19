package geoquiz;

import eu.hansolo.fx.world.World;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class View extends SplitPane {
	private VBox center;
	private HBox bottom;
	private Text progress;
	private ToggleGroup regions;
	private Button forward;
	private CountryDisplay clues;
	private CountryDisplay guessed;

	public View(World world, IQuizCountry initialCountry) {
		setOrientation(Orientation.VERTICAL);
		
		clues = new CountryDisplay(initialCountry);
		
		makeCenter();
		
		guessed = new CountryDisplay(new NullCountry());
		
		bottom = new HBox(clues, center, guessed);

		StackPane map = new StackPane(world);
		map.setBackground(new Background(new BackgroundFill(world.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));

		getItems().addAll(map, bottom);
	}

	private void makeCenter() {
		regions = new ToggleGroup();
		
		RadioButton asia = newRegion("Asia", "AS");
		RadioButton africa = newRegion("Africa", "AF");
		RadioButton northAmerica = newRegion("North America", "NA");
		RadioButton southAmerica = newRegion("South America", "SA");
		RadioButton europe = newRegion("Europe", "EU");
		RadioButton australia = newRegion("Australia", "AU");
		RadioButton wholeWorld = newRegion("World", "WORLD");

		northAmerica.setSelected(true);
		
		Button back = new Button("Back");
		forward = new Button("Forward");

		HBox navigator = new HBox(back, forward);
		
		progress = new Text();
		progress.setFont(new Font(36.0));
		center = new VBox(progress, asia, africa, northAmerica, southAmerica, europe, australia, wholeWorld, navigator);
	} 

	private RadioButton newRegion(String name, String abbreviation) {
		RadioButton region = new RadioButton(name);
		region.setUserData(abbreviation);
		region.setToggleGroup(regions);
		return region;
	}
	
	public void updateProgress(int currentQuestion, int total) {
		progress.setText(String.format("%d / %d", currentQuestion, total));
	}
	
	public void updateGuessedCountry(IQuizCountry selectedCountry) {
		guessed.updateCountry(selectedCountry);
	}
	
	public void updateClueCountry(IQuizCountry selectedCountry) {
		clues.updateCountry(selectedCountry);
	}
	
	public Button getForwardButton() {
		return forward;
	}
	
	public ToggleGroup getRegionRadioButtons() {
		return regions;
	}
}
