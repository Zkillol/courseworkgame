module com.example.gameforcourse {
    requires javafx.controls;
    requires javafx.fxml;
    exports com.example.game; // Позволяет другим модулям использовать этот пакет
    opens com.example.game to javafx.fxml; // Для использования с FXML
}
