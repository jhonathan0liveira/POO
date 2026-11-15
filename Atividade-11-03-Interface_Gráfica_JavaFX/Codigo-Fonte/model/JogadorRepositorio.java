package model;

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import com.j256.ormlite.table.TableUtils;
import java.util.List;
import java.util.ArrayList;

public class JogadorRepositorio
{
    private static Database database;
    private static Dao<Jogador, Integer> dao;
    private List<Jogador> loadedJogadores;
    private Jogador loadedJogador;
    
    public JogadorRepositorio(Database database) {
        JogadorRepositorio.setDatabase(database);
        loadedJogadores = new ArrayList<Jogador>();
    }
    
    public static void setDatabase(Database database) {
        JogadorRepositorio.database = database;
        try {
            dao = DaoManager.createDao(database.getConnection(), Jogador.class);
            TableUtils.createTableIfNotExists(database.getConnection(), Jogador.class);
        }
        catch(SQLException e) {
            System.out.println(e);
        }            
    }
    
    public Jogador create(Jogador jogador) {
        int nrows = 0;
        try {
            nrows = dao.create(jogador);
            if ( nrows == 0 )
                throw new SQLException("Error: object not saved");
            this.loadedJogador = jogador;
            loadedJogadores.add(jogador);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return jogador;
    }    

    public void update(Jogador Jogador) {
      int nrows = 0;
        try {
            nrows = dao.update(Jogador);
            if (nrows == 0)
                throw new SQLException("Error: object not updated");
            this.loadedJogador = Jogador;
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void delete(Jogador Jogador) {
      int nrows = 0;
        try {
            nrows = dao.delete(Jogador);
            if (nrows == 0)
                throw new SQLException("Error: object not deleted");
            this.loadedJogadores.remove(Jogador);
            if (this.loadedJogador != null && this.loadedJogador.equals(Jogador)) {
                this.loadedJogador = null;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    public Jogador loadFromId(int id) {
        try {
            this.loadedJogador = dao.queryForId(id);
            if (this.loadedJogador != null)
                this.loadedJogadores.add(this.loadedJogador);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return this.loadedJogador;
    }    
    
    public List<Jogador> loadAll() {
        try {
            this.loadedJogadores =  dao.queryForAll();
            if (this.loadedJogadores.size() != 0)
                this.loadedJogador = this.loadedJogadores.get(0);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return this.loadedJogadores;
    }
}