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
import javafx.scene.Scene;
import javafx.scene.control.Toggle;
import javafx.stage.Stage;

public class Controller extends Application {
	private World world;
	private Model model;
	private View view;
	
	@SuppressWarnings("unchecked")
	@Override 
	public void start(Stage stage) {
		model = new Model();

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
						view.updateGuessedCountry(selectedQuizCountry);
					}
					System.out.println(locale.getCountry());
				})
				.zoomEnabled(true)
				.selectionEnabled(true)
				.build();
		
		view = new View(world, model.currentCountry());

		view.getRegionRadioButtons().selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
		    public void changed(ObservableValue<? extends Toggle> ov, Toggle oldToggle, Toggle newToggle) {
				String code = (String)newToggle.getUserData();
				System.out.println(code);
				model.changeQuizCountries(code);
				view.updateClueCountry(model.currentCountry());
				view.updateProgress(model.completed, model.originalSize);
		    }
		});
		
		view.getForwardButton().setOnAction(e -> {
			goToNextCountry();
		});

		Scene scene = new Scene(view);

		stage.setTitle("World Map");
		stage.setScene(scene);
		stage.show();
	}

	private void goToNextCountry() {
		view.updateClueCountry(model.nextCountry());
		view.updateProgress(model.completed, model.originalSize);
		
		view.updateGuessedCountry(new NullCountry());
	}
	
	@Override 
	public void stop() {
		System.exit(0);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
