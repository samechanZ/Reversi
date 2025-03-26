import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) throws Exception {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Button b = new Button("Hello World");
        b.setOnAction(e -> System.out.println("Hello World"));
        Scene scene = new Scene(new StackPane(b), 640, 480);
        stage.setScene(scene);
        stage.show();
    }
}