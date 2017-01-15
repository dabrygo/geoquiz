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

	@SuppressWarnings("unchecked")
	@Override 
	public void start(Stage stage) {
		try {
			FlowPane quiz = new FlowPane();
			Model model = new Model();
			ImageView flag = new ImageView(model.getFlagOfCountry());
	
			VBox options = new VBox();
			Text result = new Text();
			ToggleGroup region = new ToggleGroup();
			RadioButton northAmerica = new RadioButton("North America");
			northAmerica.setUserData("NA");
			RadioButton europe = new RadioButton("Europe");
			europe.setUserData("EU");
			
			region.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
			    public void changed(ObservableValue<? extends Toggle> ov, Toggle oldToggle, Toggle newToggle) {
					String code = (String)newToggle.getUserData();
					model.changeQuizCountries(code);
					flag.setImage(model.getFlagOfCountry());
			    }
			});
			
			northAmerica.setToggleGroup(region);
			europe.setToggleGroup(region);
			northAmerica.setSelected(true);
	
			options.getChildren().addAll(result, northAmerica, europe);
			
			quiz.getChildren().addAll(flag, options);
			
			world = WorldBuilder.create()
					.resolution(Resolution.HI_RES)
					.mousePressHandler(evt -> {
						CountryPath countryPath = (CountryPath) evt.getSource();
						Locale locale = countryPath.getLocale();
						if (locale.getISO3Country().equals(model.getNameOfCountry())) {
							result.setText("");
							flag.setImage(model.flagOfNextCountry());	
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

	@Override 
	public void stop() {
		System.exit(0);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
