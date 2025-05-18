module projectmini {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
	
	requires controlsfx;
	requires javafx.graphics;
	requires javafx.base;
	opens controller to javafx.fxml;
    exports controller;
	opens application to javafx.graphics, javafx.fxml;
	opens application.models to javafx.base;
}
