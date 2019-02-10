package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.util.*;

/**
 * FXML Controller class
 *
 * @author Jan
 */
public class GraphController implements Initializable {

	@FXML private VBox root;
	@FXML private HBox header;
	@FXML private StackPane content;
	@FXML private VBox firstStage;
	@FXML private TextField hourField;
	@FXML private TextField minuteField;
	@FXML private Button shutdown;
	@FXML private VBox secondStage;
	@FXML private Button cancel;
	@FXML private Arc timerVisual;

	private FadeTransition fade = new FadeTransition();
	private FadeTransition show = new FadeTransition();
	Timeline visualCountdown = new Timeline();
	
	private enum Stage {
		Setup, Countdown;
	}

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		fade.setFromValue(1);
		fade.setToValue(0);
		fade.setDuration(Duration.millis(250));
		show.setFromValue(0);
		show.setToValue(1);
		show.setDuration(Duration.millis(250));
		fade.setOnFinished(e -> show.play());
		visualCountdown.setOnFinished(e -> {
			System.out.println("finished");
		});
	}
	
	@FXML
	public void startCountdown() {
		String hours = hourField.getText();
		String minutes = minuteField.getText();
		
		if (!hours.matches("[0-9]{1,2}")||!minutes.matches("[0-9]{1,2}")){
			hourField.setText("00");
			minuteField.setText("00");
			return;
		}
		
		int parsedHours = Integer.parseInt(hours);
		int parsedMinutes = Integer.parseInt(minutes);
		int countdown_length = (parsedHours*60*60) + (parsedMinutes*60);
		System.out.println(countdown_length);
		
		showStage(Stage.Countdown);
		
		visualCountdown.getKeyFrames().add(new KeyFrame(Duration.ZERO, new KeyValue(timerVisual.lengthProperty(), 0)));
		visualCountdown.getKeyFrames().add(new KeyFrame(Duration.seconds(countdown_length), new KeyValue(timerVisual.lengthProperty(), 360)));
		visualCountdown.play();
	}
	
	@FXML
	public void cancelCountdown() {
		showStage(Stage.Setup);
		visualCountdown.stop();
	}
	
	private void showStage(Stage stage) {
		if (stage == Stage.Countdown) {
			fade.setNode(firstStage);
			show.setNode(secondStage);
			show.setOnFinished(e -> secondStage.toFront());
		} else if (stage == Stage.Setup){
			fade.setNode(secondStage);
			show.setNode(firstStage);
			show.setOnFinished(e -> firstStage.toFront());
		}
		fade.play();
	}
	
	
	
}
