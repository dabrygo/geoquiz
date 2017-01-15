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
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    private World world;

    @SuppressWarnings("unchecked")
	@Override public void init() {
        world = WorldBuilder.create()
                            .resolution(Resolution.HI_RES)
                            .mousePressHandler(evt -> {
                                CountryPath countryPath = (CountryPath) evt.getSource();
                                Locale      locale      = countryPath.getLocale();
                                System.out.println(locale.getDisplayCountry() + " (" + locale.getISO3Country() + ")");
                            })
                            .zoomEnabled(true)
                            .selectionEnabled(true)
                            .build();
    }
    
    @Override 
    public void start(Stage stage) {
    	SplitPane splitPane = new SplitPane();
    	splitPane.setOrientation(Orientation.VERTICAL);
    	
        StackPane map = new StackPane(world);
        map.setBackground(new Background(new BackgroundFill(world.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));

        splitPane.getItems().add(map);
        splitPane.getItems().add(new TextArea("hello there"));
        
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
