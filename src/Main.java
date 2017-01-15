import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.image.Image;
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
	private int index;

	class QuizCountry {
		public String name;
		public Image flag;

		public QuizCountry(String name) {
			this.name = name;
			this.flag = new Image(name + ".png");
		}
	}

	@SuppressWarnings("unchecked")
	@Override 
	public void start(Stage stage) {

		FlowPane quiz = new FlowPane();
		QuizCountry USA = new QuizCountry("USA");
		QuizCountry CAN = new QuizCountry("CAN");
		List<QuizCountry> quizCountries = new ArrayList<>();
		quizCountries.add(USA);
		quizCountries.add(CAN);
		index = 0;
		ImageView flag = new ImageView(quizCountries.get(0).flag);
		Text result = new Text();

		world = WorldBuilder.create()
				.resolution(Resolution.HI_RES)
				.mousePressHandler(evt -> {
					CountryPath countryPath = (CountryPath) evt.getSource();
					Locale      locale      = countryPath.getLocale();
					if (locale.getISO3Country().equals(quizCountries.get(index).name)) {
						result.setText("Correct!");
						if (index < quizCountries.size() - 1) {
							index++;
							flag.setImage(quizCountries.get(index).flag);	
						}
					}
					else {
						result.setText("Incorrect! That's "  + locale.getDisplayCountry() + ".");
					}
					System.out.println(locale.getDisplayCountry() + " (" + locale.getISO3Country() + ")");
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
