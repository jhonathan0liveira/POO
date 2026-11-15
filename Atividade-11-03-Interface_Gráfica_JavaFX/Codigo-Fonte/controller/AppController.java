package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import model.Database;
import model.JogadorRepositorio;
import view.AppView;

public class AppController implements Initializable {

    @FXML
    private TableView<model.Jogador> tabela;
    @FXML
    private TableColumn<model.Jogador, Integer> idCol;
    @FXML
    private TableColumn<model.Jogador, String> nomeCol;
    @FXML
    private TableColumn<model.Jogador, String> classeCol;

    @FXML
    private TextField nomeField;
    @FXML
    private ChoiceBox<String> classeChoiceBox;
     @FXML
     
    private Button excluirButton;
    @FXML
    private Button atualizarButton;
    @FXML
    private Button salvarButton;
    @FXML
    private Button limparButton;

    AppView appView;

    private static Database database = new Database("app.sqlite");
    private static JogadorRepositorio jogadorRepo = new JogadorRepositorio(database);

    public AppController() {
        this.appView = new AppView();
    }

    public static void main(String[] args) throws Exception {
        AppController appController = new AppController();
        appController.appView.run(args);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        classeChoiceBox.getItems().addAll("Guerreiro", "Mago", "Arqueiro");
        
        tabela.getSelectionModel().selectedItemProperty().addListener(
        (observableValue, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nomeField.setText(newSelection.getNome());
                classeChoiceBox.setValue(newSelection.getClasse());
            }
        }
    );
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));
        classeCol.setCellValueFactory(new PropertyValueFactory<>("classe"));

        tabela.setItems(loadAllJogadores());
    }
    
    @FXML
    public void onAtualizarButtonAction() {
        model.Jogador jogadorSelecionado = tabela.getSelectionModel().getSelectedItem();

        if (jogadorSelecionado == null) {
            new Alert(AlertType.WARNING, "Selecione um jogador na aba Tabela para atualizar!").show();
            return;
        }

        String nome = nomeField.getText().trim();
        String classe = classeChoiceBox.getValue();

        if (nome.isEmpty() || classe == null) {
            new Alert(AlertType.WARNING, "Preencha o nome e escolha uma classe!").show();
            return;
        }

        try {
            jogadorSelecionado.setNome(nome);
            jogadorSelecionado.setClasse(classe);

            jogadorRepo.update(jogadorSelecionado);
            tabela.refresh();

            onLimparButtonAction();
            tabela.getSelectionModel().clearSelection();
            new Alert(AlertType.INFORMATION, "Jogador atualizado com sucesso!").show();
        } catch (Exception e) {
            new Alert(AlertType.ERROR, "Erro ao atualizar: " + e.getMessage()).show();
        }
    }
    
    @FXML
    public void onExcluirButtonAction() {
        model.Jogador jogadorSelecionado = tabela.getSelectionModel().getSelectedItem();

        if (jogadorSelecionado == null) {
            new Alert(AlertType.WARNING, "Selecione um jogador na tabela para excluir!").show();
            return;
        }

        try {
            jogadorRepo.delete(jogadorSelecionado);
            tabela.getItems().remove(jogadorSelecionado);
            new Alert(AlertType.INFORMATION, "Jogador excluído com sucesso!").show();
        } catch (Exception e) {
            new Alert(AlertType.ERROR, "Erro ao excluir: " + e.getMessage()).show();
        }
    }
    
    @FXML
    public void onSalvarButtonAction() {
        try {
            String nome = nomeField.getText().trim();
            String classe = classeChoiceBox.getValue();

            if (nome.isEmpty() || classe == null) {
                new Alert(AlertType.WARNING, "Preencha o nome e escolha uma classe!").show();
                return;
            }

            model.Jogador jogador = new model.Jogador();
            jogador.setNome(nome);
            jogador.setClasse(classe);

            model.Jogador jogadorSalvo = jogadorRepo.create(jogador);
            tabela.getItems().add(jogadorSalvo);

            onLimparButtonAction();
            new Alert(AlertType.INFORMATION, "Jogador salvo com sucesso!").show();
        } catch (Exception e) {
            new Alert(AlertType.ERROR, "Erro ao salvar: " + e.getMessage()).show();
        }
    }

    @FXML
    public void onLimparButtonAction() {
        nomeField.setText("");
        classeChoiceBox.setValue(null);
    }

    private ObservableList<model.Jogador> loadAllJogadores() {
        ObservableList<model.Jogador> lista = FXCollections.observableArrayList();
        List<model.Jogador> listaFromDatabase = jogadorRepo.loadAll();
        lista.addAll(listaFromDatabase);
        return lista;
    }
}
