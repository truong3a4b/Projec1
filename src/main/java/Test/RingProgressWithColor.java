package Test;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class RingProgressWithColor extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Kích thước cơ bản
        double centerX = 150; // Tọa độ X của vòng tròn
        double centerY = 150; // Tọa độ Y của vòng tròn
        double radiusOuter = 100; // Bán kính vòng tròn ngoài
        double radiusInner = 70;  // Bán kính vòng tròn trong
        double progress = 0.75;   // Tỷ lệ (0 -> 1)

        // Vòng tròn nền (vòng nhẫn ngoài)
        Circle outerCircle = new Circle(centerX, centerY, radiusOuter, Color.LIGHTGRAY);
        Circle innerCircle = new Circle(centerX, centerY, radiusInner, Color.WHITE);

        // Tạo phần hiển thị tiến độ (arc)
        Arc progressArc = new Arc();
        progressArc.setCenterX(centerX);
        progressArc.setCenterY(centerY);
        progressArc.setRadiusX(radiusOuter);
        progressArc.setRadiusY(radiusOuter);
        progressArc.setStartAngle(90); // Góc bắt đầu
        progressArc.setLength(-360 * progress); // Góc theo tỷ lệ
        progressArc.setType(ArcType.ROUND); // Kiểu kết thúc
        progressArc.setFill(Color.CORNFLOWERBLUE);

        // Nhóm các phần tử
        Group ring = new Group(outerCircle, progressArc, innerCircle);

        // Thiết lập giao diện
        Scene scene = new Scene(ring, 300, 300);
        primaryStage.setTitle("Ring Progress Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
