package geoquiz;

import javafx.geometry.Orientation;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CountryDisplay extends FlowPane {
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
