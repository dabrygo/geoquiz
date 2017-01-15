import java.util.Locale;

import eu.hansolo.fx.world.CountryPath;
import eu.hansolo.fx.world.World;
import eu.hansolo.fx.world.World.Resolution;
import eu.hansolo.fx.world.WorldBuilder;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
	private World world;

	@SuppressWarnings("unchecked")
	@Override 
	public void start(Stage stage) {

		FlowPane quiz = new FlowPane();
		Model model = new Model();
		ImageView flag = new ImageView(model.getFlagOfCountry());
		Text result = new Text();

		world = WorldBuilder.create()
				.resolution(Resolution.HI_RES)
				.mousePressHandler(evt -> {
					CountryPath countryPath = (CountryPath) evt.getSource();
					Locale      locale      = countryPath.getLocale();
					if (locale.getISO3Country().equals(model.getNameOfCountry())) {
						if (model.moreCountriesInQuiz()) {
							result.setText("");
							flag.setImage(model.flagOfNextCountry());	
						}
						else {
							flag.setImage(null);
						}
					}
					else {
						result.setText("Incorrect! That's "  + locale.getDisplayCountry() + ".");
					}
				})
				.zoomEnabled(true)
				.selectionEnabled(true)
				.build();

		SplitPane splitPane = new SplitPane();
		splitPane.setOrientation(Orientation.VERTICAL);

		StackPane map = new StackPane(world);
		map.setBackground(new Background(new BackgroundFill(world.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));


		quiz.getChildren().addAll(flag, result);
		splitPane.getItems().addAll(map, quiz);

		Scene scene = new Scene(splitPane);

		stage.setTitle("World Map");
		stage.setScene(scene);
		stage.show();
	}

	@Override 
	public void stop() {
		System.exit(0);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
