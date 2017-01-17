package geoquiz;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class View extends SplitPane {
	FlowPane left;
	VBox center;
	HBox bottom;
	FlowPane right;
	Text progress;
	private Text result;
	ToggleGroup regions;
	Button forward;

	public View() {
		setOrientation(Orientation.VERTICAL);
		
		left = new FlowPane();
		makeCenter();
		right = new FlowPane();
		bottom = new HBox(left, center, right);
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
		result = new Text();
		regions = new ToggleGroup();
		center = new VBox(progress, result, asia, africa, northAmerica, southAmerica, europe, australia, wholeWorld, navigator);
	} 

	private RadioButton newRegion(String name, String abbreviation) {
		RadioButton region = new RadioButton(name);
		region.setUserData(abbreviation);
		region.setToggleGroup(regions);
		return region;
	}

	
	public void addToLeft(Node... elements) {
		left.getChildren().addAll(elements);
	}
	
	public void addToCenter(Node... elements) {
		center.getChildren().addAll(elements);
	}
	
	public void addToRight(Node... elements) {
		right.getChildren().addAll(elements);
	}
}
