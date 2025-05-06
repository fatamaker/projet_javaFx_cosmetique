module projectmini {
	requires javafx.controls;
	requires java.sql;
	requires javafx.fxml;
	requires controlsfx;
	requires javafx.graphics;
	opens controller to javafx.fxml;
    exports controller;
	opens application to javafx.graphics, javafx.fxml;
}
