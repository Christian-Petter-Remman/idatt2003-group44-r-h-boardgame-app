module edu.ntnu.idi.idatt {
  requires javafx.controls;

  opens edu.ntnu.idi.idatt.filehandling to javafx.fxml;

  exports edu.ntnu.idi.idatt.filehandling;
}
