package geoquiz;
import java.util.Locale;

import eu.hansolo.fx.world.CountryPath;
import eu.hansolo.fx.world.World;
import eu.hansolo.fx.world.World.Resolution;
import eu.hansolo.fx.world.WorldBuilder;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
	private World world;
	private ToggleGroup regions;
	private Model model;
	private Text countryName;
	private ImageView flag;
	private Text progress;
	private Text result;

	@SuppressWarnings("unchecked")
	@Override 
	public void start(Stage stage) {
		try {
			FlowPane quiz = new FlowPane();
			model = new Model();
			
			FlowPane clues = new FlowPane(	);
			clues.setOrientation(Orientation.HORIZONTAL);
			countryName = new Text(getCountryDescription());
			countryName.setFont(new Font(24.0));
			flag = new ImageView(model.getFlagOfCountry());
			clues.getChildren().addAll(countryName, flag);
			
			VBox options = new VBox();
			progress = new Text(progressText());
			result = new Text();
			regions = new ToggleGroup();
			
			RadioButton asia = newRegion("Asia", "AS");
			RadioButton africa = newRegion("Africa", "AF");
			RadioButton northAmerica = newRegion("North America", "NA");
			RadioButton southAmerica = newRegion("South America", "SA");
			RadioButton europe = newRegion("Europe", "EU");
			RadioButton australia = newRegion("Australia", "AU");
			RadioButton wholeWorld = newRegion("World", "WORLD");
			
			regions.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
			    public void changed(ObservableValue<? extends Toggle> ov, Toggle oldToggle, Toggle newToggle) {
					String code = (String)newToggle.getUserData();
					model.changeQuizCountries(code);
					countryName.setText(getCountryDescription());
					flag.setImage(model.getFlagOfCountry());
					progress.setText(progressText());
			    }
			});
			
			northAmerica.setSelected(true);
	
			Button back = new Button("Back");
			Button forward = new Button("Forward");
			forward.setOnAction(e -> {
				goToNextCountry();
			});
			HBox navigator = new HBox(back, forward);
			
			options.getChildren().addAll(progress, result, asia, africa, northAmerica, southAmerica, europe, australia, wholeWorld, navigator);
			
			quiz.getChildren().addAll(clues, options);
			
			world = WorldBuilder.create()
					.resolution(Resolution.HI_RES)
					.mousePressHandler(evt -> {
						CountryPath countryPath = (CountryPath) evt.getSource();
						Locale locale = countryPath.getLocale();
						if (locale.getISO3Country().equals(model.getIsoOfCountry())) {
							goToNextCountry();
						}
						else if (model.moreCountriesInQuiz()){
							result.setText("Incorrect! That's "  + locale.getDisplayCountry() + ".");
						}
						System.out.println(locale.getISO3Country());
					})
					.zoomEnabled(true)
					.selectionEnabled(true)
					.build();
	
			SplitPane splitPane = new SplitPane();
			splitPane.setOrientation(Orientation.VERTICAL);
	
			StackPane map = new StackPane(world);
			map.setBackground(new Background(new BackgroundFill(world.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
	
			splitPane.getItems().addAll(map, quiz);
	
			Scene scene = new Scene(splitPane);
	
			stage.setTitle("World Map");
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void goToNextCountry() {
		result.setText("");
		flag.setImage(model.nextCountry());	
		progress.setText(progressText());
		countryName.setText(getCountryDescription());
	}

	private String getCountryDescription() {
		return model.getNameOfCountry() + ", Capital: " + model.getCapital();
	}

	private String progressText() {
		return String.format("%d / %d", model.completed, model.originalSize);
	}

	private RadioButton newRegion(String name, String abbreviation) {
		RadioButton region = new RadioButton(name);
		region.setUserData(abbreviation);
		region.setToggleGroup(regions);
		return region;
	}

	@Override 
	public void stop() {
		System.exit(0);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
