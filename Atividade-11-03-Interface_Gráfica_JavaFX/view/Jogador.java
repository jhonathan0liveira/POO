package view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Jogador {

    private SimpleIntegerProperty id;
    private SimpleStringProperty nome;
    private SimpleStringProperty classe;

    public Jogador(int id, String nome, String classe) {
        this.id = new SimpleIntegerProperty(id);
        this.nome = new SimpleStringProperty(nome);
        this.classe = new SimpleStringProperty(classe);
    }

    public int getId() {
        return this.id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getNome() {
        return this.nome.get();
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public String getClasse() {
        return this.classe.get();
    }

    public void setClasse(String classe) {
        this.classe.set(classe);
    }
}
