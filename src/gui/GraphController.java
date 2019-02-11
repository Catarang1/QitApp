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

	private double xOffset = 0;
	private double yOffset = 0;
	private FadeTransition fade = new FadeTransition();
	private FadeTransition show = new FadeTransition();
	Timeline visualCountdown = new Timeline();
	private SVGPath circle = new SVGPath();
	private SVGPath cross = new SVGPath();
	private SVGPath mini = new SVGPath();
	@FXML
	private Text h;
	@FXML
	private Text m;

	private enum Stage {
		Setup, Countdown;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		circle.setFill(Color.web("#1c2023"));
		cross.setFill(Color.web("#1c2023"));
		mini.setFill(Color.web("#1c2023"));

		circle.setContent("M16,8c0,4.4-3.6,8-8,8s-8-3.6-8-8s3.6-8,8-8S16,3.6,16,8z");
		cross.setContent("M8,0C3.6,0,0,3.6,0,8s3.6,8,8,8s8-3.6,8-8S12.4,0,8,0z M12.4,9.6c0.8,0.8,0.8,2,0,2.8C12,12.8,11.5,13,11,13\n"
				+ "	s-1-0.2-1.4-0.6L8,10.8l-1.6,1.6C6,12.8,5.5,13,5,13s-1-0.2-1.4-0.6c-0.8-0.8-0.8-2,0-2.8L5.2,8L3.6,6.4c-0.8-0.8-0.8-2,0-2.8\n"
				+ "	s2-0.8,2.8,0L8,5.2l1.6-1.6c0.8-0.8,2-0.8,2.8,0s0.8,2,0,2.8L10.8,8L12.4,9.6z");
		mini.setContent("M8,0C3.6,0,0,3.6,0,8s3.6,8,8,8s8-3.6,8-8S12.4,0,8,0z M12.2,10H3.8c-1.1,0-2-0.9-2-2s0.9-2,2-2h8.5\n"
				+ "	c1.1,0,2,0.9,2,2S13.3,10,12.2,10z");

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
				shutdownCommand = "shutdown.exe -s -t 0";
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
