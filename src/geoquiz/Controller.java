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
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Toggle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Controller extends Application {
	private World world;
	private Model model;
	private View view;
	private CountryDisplay clues;
	private CountryDisplay guessed;

	class CountryDisplay extends FlowPane {
		private Text nameText;
		private Text capitalText;
		private ImageView flagImage;
		
		public CountryDisplay(String name, String capital, Image flag) {
			setOrientation(Orientation.VERTICAL);
			nameText = new Text("Name: " + name);
			nameText.setFont(new Font(24.0));
			
			capitalText = new Text("Capital: " + capital);
			capitalText.setFont(new Font(24.0));
			
			flagImage = new ImageView(flag);
			
			getChildren().addAll(nameText, capitalText, flagImage);
		}
		
		public CountryDisplay(IQuizCountry country) {
			this(country.getName(), country.getCapital(), country.getFlag());
		}
		public void setName(String name) {
			nameText.setText("Name: " + name);
		}

		public void setCapital(String capital) {
			capitalText.setText("Capital: " + capital);
		}
		
		public void setFlag(Image flag) {
			flagImage.setImage(flag);
		}

		public void updateCountry(IQuizCountry theNextCountry) {
			setName(theNextCountry.getName());
			setCapital(theNextCountry.getCapital());
			setFlag(theNextCountry.getFlag());
		}
	}
	
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
						if (locale.getISO3Country().equals(selectedIso)) {
							goToNextCountry();
						}
						else if (model.moreCountriesInQuiz()){
//							result.setText("Incorrect! That's "  + locale.getDisplayCountry() + ".");
							guessed.setName(locale.getDisplayCountry());
							
							Country country = Country.valueOf(locale.getCountry());
							IQuizCountry selectedQuizCountry = model.masterList[country.ordinal()];
							guessed.setCapital(selectedQuizCountry.getCapital());
							guessed.setFlag(selectedQuizCountry.getFlag());
						}
						System.out.println(locale.getISO3Country());
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
//		result.setText("");
		clues.updateCountry(model.nextCountry());
		updateProgressText();
		
		guessed.setFlag(null);
		guessed.setName("");
		guessed.setCapital("");
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
