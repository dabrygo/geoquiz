package geoquiz;
import java.util.Locale;

import eu.hansolo.fx.world.Country;
import eu.hansolo.fx.world.CountryPath;
import eu.hansolo.fx.world.World;
import eu.hansolo.fx.world.World.Resolution;
import eu.hansolo.fx.world.WorldBuilder;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Toggle;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Controller extends Application {
	private World world;
	private Model model;
	private View view;
	private CountryDisplay clues;
	private CountryDisplay guessed;
	
	@SuppressWarnings("unchecked")
	@Override 
	public void start(Stage stage) {
		try {
			model = new Model();
			view = new View();
			
			clues = new CountryDisplay(model.currentCountry());
			
			guessed = new CountryDisplay("", "", null);

			view.regions.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
			    public void changed(ObservableValue<? extends Toggle> ov, Toggle oldToggle, Toggle newToggle) {
					String code = (String)newToggle.getUserData();
					System.out.println(code);
					model.changeQuizCountries(code);
					clues.updateCountry(model.currentCountry());;
					updateProgressText();
			    }
			});
			
			view.forward.setOnAction(e -> {
				goToNextCountry();
			});
			
			view.addToLeft(clues);
			view.addToRight(guessed);
			
			world = WorldBuilder.create()
					.resolution(Resolution.HI_RES)
					.mousePressHandler(evt -> {
						CountryPath countryPath = (CountryPath) evt.getSource();
						Locale locale = countryPath.getLocale();
						String selectedIso = model.currentCountry().getAbbreviation();

						Country country = Country.valueOf(locale.getCountry());
						IQuizCountry selectedQuizCountry = model.masterList[country.ordinal()];
						if (locale.getISO3Country().equals(selectedIso)) {
							goToNextCountry();
						}
						else if (model.moreCountriesInQuiz()){
//							result.setText("Incorrect! That's "  + locale.getDisplayCountry() + ".");
							guessed.updateCountry(selectedQuizCountry);
						}
//						System.out.println(locale.getISO3Country());
						System.out.println(locale.getCountry());
					})
					.zoomEnabled(true)
					.selectionEnabled(true)
					.build();
	
			StackPane map = new StackPane(world);
			map.setBackground(new Background(new BackgroundFill(world.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
	
			view.getItems().addAll(map, view.bottom);
	
			Scene scene = new Scene(view);
	
			stage.setTitle("World Map");
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void goToNextCountry() {
		clues.updateCountry(model.nextCountry());
		updateProgressText();
		
		guessed.updateCountry(new NullCountry());;
	}

	private void updateProgressText() {
		view.progress.setText(String.format("%d / %d", model.completed, model.originalSize));
	}
	
	@Override 
	public void stop() {
		System.exit(0);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
