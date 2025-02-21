module edu.ntnu.idi.idatt {
  requires javafx.controls;

  opens edu.ntnu.idi.idatt.controller to javafx.fxml;

  exports edu.ntnu.idi.idatt.controller;
}
