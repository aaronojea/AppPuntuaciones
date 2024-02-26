package ad.apppuntuaciones;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class ControladorPuntuaciones {

    @FXML
    private TableView<Map<String, Object>> tblPuntuaciones;
    @FXML
    private TableView<Map<String, Object>> tblJuegos;
    @FXML
    private TableColumn<Map, String> colIdJuego;
    @FXML
    private TableColumn<Map, String> colNombreJuego;
    @FXML
    private TableColumn<Map, String> colIdPuntuacion;
    @FXML
    private TableColumn<Map, String> colIdJuegoPuntuacion;
    @FXML
    private TableColumn<Map, String> colNombrePuntuacion;
    @FXML
    private TableColumn<Map, String> colPuntuacion;

    private static final String BASE_URL = "http://localhost:8080";

    public void initialize() {

        colIdJuego.setCellValueFactory(new MapValueFactory<>("id"));
        colNombreJuego.setCellValueFactory(new MapValueFactory<>("nombre"));
        colIdPuntuacion.setCellValueFactory(new MapValueFactory<>("id"));
        colPuntuacion.setCellValueFactory(new MapValueFactory<>("puntuacion"));
        colNombrePuntuacion.setCellValueFactory(new MapValueFactory<>("nombre"));
        colIdJuegoPuntuacion.setCellValueFactory(new MapValueFactory<>("juego"));
        colIdJuegoPuntuacion.setCellValueFactory(cellData -> {
            Object juegoObject = cellData.getValue().get("juego");
            if (juegoObject instanceof Map) {
                Object idJuego = ((Map<?, ?>) juegoObject).get("nombre");
                if (idJuego != null) {
                    return new SimpleStringProperty(String.valueOf(idJuego));
                }
            }
            return new SimpleStringProperty("");
        });
        cargarJuegos();
        cargarPuntuaciones();
    }

    private void cargarJuegos() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/juegos/alfabetico"))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(this::respuestaJuegos)
                .join();
    }

    private void cargarPuntuaciones() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/puntuaciones/alfabetico"))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(this::respuestaPuntuaciones)
                .join();
    }

    private void respuestaJuegos(String respuesta) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Convertir la respuesta JSON a una lista de mapas
            ObservableList<Map<String, Object>> listaJuegos = FXCollections.observableArrayList(
                    objectMapper.readValue(respuesta, Map[].class)
            );

            // Llenar la tabla de juegos con los datos
            tblJuegos.setItems(listaJuegos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void respuestaPuntuaciones(String respuesta) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Convertir la respuesta JSON a una lista de mapas
            ObservableList<Map<String, Object>> listaPuntuaciones = FXCollections.observableArrayList(
                    objectMapper.readValue(respuesta, Map[].class)
            );

            // Llenar la tabla de puntuaciones con los datos
            tblPuntuaciones.setItems(listaPuntuaciones);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}