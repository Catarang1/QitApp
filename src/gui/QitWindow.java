package gui;

import java.io.*;
import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

/**
 *
 * @author Jan
 */
public class QitWindow extends Application {
	
	public static Stage stage;
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
	                             
	public static void main(String[] args) {
		launch(args);
	}
	
}
