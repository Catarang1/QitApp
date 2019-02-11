package gui;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.*;
import javafx.animation.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
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
	@FXML private Button minimize;
	@FXML private Button close;
	@FXML private Text h;
	@FXML private Text m;
	
	private double xOffset = 0;
	private double yOffset = 0;
	private FadeTransition fade = new FadeTransition();
	private FadeTransition show = new FadeTransition();
	Timeline visualCountdown = new Timeline();
	private enum Stage {Setup, Countdown;}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		header.setOnMousePressed(e -> {
			xOffset = e.getSceneX();
			yOffset = e.getSceneY();
		});

		header.setOnMouseDragged((MouseEvent event) -> {
			QitWindow.stage.setX(event.getScreenX() - xOffset);
			QitWindow.stage.setY(event.getScreenY() - yOffset);
		});

		fade.setFromValue(1);
		fade.setToValue(0);
		fade.setDuration(Duration.millis(150));
		show.setFromValue(0);
		show.setToValue(1);
		show.setDuration(Duration.millis(150));
		fade.setOnFinished(e -> show.play());
		visualCountdown.setOnFinished(e -> {
			String shutdownCommand = "";
			String osName = System.getProperty("os.name");
			if (osName.startsWith("Win")) {
				shutdownCommand = "shutdown.exe -s -f -t 0";
			} else if (osName.startsWith("Linux") || osName.startsWith("Mac")) {
				shutdownCommand = "shutdown -h now";
			} else {
				System.err.println("Shutdown unsupported operating system ...");
				System.exit(0);
			}
			Runtime runtime = Runtime.getRuntime();
			try {
				Process proc = runtime.exec(shutdownCommand);
			} catch (IOException ex) {
				Logger.getLogger(GraphController.class.getName()).log(Level.SEVERE, null, ex);
			}
			System.exit(0);
		});
	}

	@FXML
	public void startCountdown() {
		visualCountdown.getKeyFrames().clear();
		String hours = hourField.getText();
		String minutes = minuteField.getText();

		if (!hours.matches("[0-9]{1,2}") || !minutes.matches("[0-9]{1,2}")) {
			hourField.setText("00");
			minuteField.setText("00");
			return;
		}

		int parsedHours = Integer.parseInt(hours);
		int parsedMinutes = Integer.parseInt(minutes);
		int countdown_length = (parsedHours * 60 * 60) + (parsedMinutes * 60);
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
		} else if (stage == Stage.Setup) {
			fade.setNode(secondStage);
			show.setNode(firstStage);
			show.setOnFinished(e -> firstStage.toFront());
		}
		fade.play();
	}

	@FXML
	public void close() {
		QitWindow.stage.close();
	}

	@FXML
	public void minimize() {
		QitWindow.stage.setIconified(true);
	}

}
