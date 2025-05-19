module projectmini {
   
    requires javafx.fxml;
    requires java.sql;
	
	requires controlsfx;
	requires javafx.graphics;
	requires javafx.base;
	requires javafx.controls;
	opens controller to javafx.fxml;
    exports controller;
	opens application to javafx.graphics, javafx.fxml;
	opens application.models to javafx.base;
}