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
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
	private World world;
	private ToggleGroup regions;
	private Model model;

	@SuppressWarnings("unchecked")
	@Override 
	public void start(Stage stage) {
		try {
			FlowPane quiz = new FlowPane();
			model = new Model();
			ImageView flag = new ImageView(model.getFlagOfCountry());
	
			VBox options = new VBox();
			Text progress = new Text(progressText());
			Text result = new Text();
			regions = new ToggleGroup();
			
			RadioButton asia = newRegion("Asia", "AS");
			RadioButton africa = newRegion("Africa", "AF");
			RadioButton northAmerica = newRegion("North America", "NA");
			RadioButton southAmerica = newRegion("South America", "SA");
			RadioButton europe = newRegion("Europe", "EU");
			RadioButton australia = newRegion("Australia", "AU");
			
			regions.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
			    public void changed(ObservableValue<? extends Toggle> ov, Toggle oldToggle, Toggle newToggle) {
					String code = (String)newToggle.getUserData();
					model.changeQuizCountries(code);
					flag.setImage(model.getFlagOfCountry());
					progress.setText(progressText());
			    }
			});
			
			northAmerica.setSelected(true);
	
			options.getChildren().addAll(progress, result, asia, africa, northAmerica, southAmerica, europe, australia);
			
			quiz.getChildren().addAll(flag, options);
			
			world = WorldBuilder.create()
					.resolution(Resolution.HI_RES)
					.mousePressHandler(evt -> {
						CountryPath countryPath = (CountryPath) evt.getSource();
						Locale locale = countryPath.getLocale();
						if (locale.getISO3Country().equals(model.getNameOfCountry())) {
							result.setText("");
							flag.setImage(model.nextCountry());	
							progress.setText(progressText());
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
