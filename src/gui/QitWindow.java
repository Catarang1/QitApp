package gui;

import java.io.*;
import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.stage.*;

/**
 *
 * @author Jan
 */
public class QitWindow extends Application {
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	private FXMLLoader loader;
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		loader = new FXMLLoader(getClass().getResource("graph.fxml"));
		root = loader.load();
		scene = new Scene(root);
		stage = new Stage(StageStyle.UNDECORATED);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
}
