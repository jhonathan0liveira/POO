# Atividade Banco de Dados com ORM

Aluno: Jhonathan de Oliveira Silva  
Matricula: 202602988

# Explicação

  Para esta atividade foi escolhida a classe monstro a fim de simular um catalogo de monstros de um jogo de rpg, a atividade consiste em criar diferentes tipos de monstros, atualizar seus dados, excluir monstros da lista e fazer buscas utilizando o ID do monstro. Foram implementadas três classes elas são database responsável por criar o banco de dados que ira armazenar os dados dos monstros, a classe monstro funciona como um modelo de dados e sua função é representar a estrutura do monstro dentro do sistema e a classe CatalogoDeMonstros que liga a classe Monstro com a classe Database além de executar as ações de excluir, criar, atualizar e buscar um determinado objeto monstro no banco de dados. O código-fonte foi desenvolvido utilizando a IDE blueJ e para a criação do banco de dados foi utilizado ORMLite.  


## Classe database
```java
package projeto;
import java.sql.*;
import com.j256.ormlite.jdbc.JdbcConnectionSource;

public class Database
{
   private String databaseName = null;
   private JdbcConnectionSource connection = null;
   
   public Database(String databaseName) {
       this.databaseName = databaseName;
   }    
   
   public JdbcConnectionSource getConnection() throws SQLException {
      if ( databaseName == null ) {
          throw new SQLException("database name is null");
      }
      if ( connection == null ) {
          try {
              connection = new JdbcConnectionSource("jdbc:sqlite:"+databaseName);             
            } catch ( Exception e ) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                System.exit(0);
            }
            System.out.println("Opened database successfully");
      }
      return connection;
   }
   
   public void close() {
       if ( connection != null ) {
           try {
               connection.close();
               this.connection = null;
           } catch (java.lang.Exception e) {
               System.err.println(e);
           }
       }
   }
}
```
## Classe CatalogoDeMonstro
```java
package projeto;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import com.j256.ormlite.table.TableUtils;
import java.util.List;
import java.util.ArrayList;

public class CatalogoDeMonstro
{
    private static Database database;
    private static Dao<Monstro, Integer> dao;
    private List<Monstro> loadedMonstros;
    private Monstro loadedMonstro; 
    
    public CatalogoDeMonstro(Database database) {
        CatalogoDeMonstro.setDatabase(database);
        loadedMonstros = new ArrayList<Monstro>();
    }
    
    public static void setDatabase(Database database) {
        CatalogoDeMonstro.database = database;
        try {
            dao = DaoManager.createDao(database.getConnection(), Monstro.class);
            TableUtils.createTableIfNotExists(database.getConnection(), Monstro.class);
        }
        catch(SQLException e) {
            System.out.println(e);
        }            
    }
    
    public Monstro create(Monstro monstro) {
        int nrows = 0;
        try {
            nrows = dao.create(monstro);
            if ( nrows == 0 )
                throw new SQLException("Error: object not saved");
            this.loadedMonstro = monstro;
            loadedMonstros.add(monstro);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return monstro;
    }    

    public void update(Monstro monstro) {
        try {
            int nrows = dao.update(monstro);
            if (nrows == 0) {
                throw new SQLException("Erro: Objeto não encontrado para atualizar.");
            }
            System.out.println("Monstro atualizado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar: " + e);
        }
    }

    public void delete(Monstro monstro) {
        try {
            int nrows = dao.delete(monstro);
            if (nrows == 0) {
                throw new SQLException("Erro: Objeto não encontrado para deletar.");
            }
            loadedMonstros.remove(monstro);
            System.out.println("Monstro deletado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao deletar: " + e);
        }
    }
    
    public Monstro loadFromId(int id) {
        try {
            this.loadedMonstro = dao.queryForId(id);
            if (this.loadedMonstro != null)
                this.loadedMonstros.add(this.loadedMonstro);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return this.loadedMonstro;
    }    
    
    public List<Monstro> loadAll() {
        try {
            this.loadedMonstros =  dao.queryForAll();
            if (this.loadedMonstros.size() != 0)
                this.loadedMonstro = this.loadedMonstros.get(0);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return this.loadedMonstros;
    }        
}
```
## Classe Monstro 
```java
package projeto;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.DataType;

@DatabaseTable(tableName = "monstro")
public class Monstro
{   
    @DatabaseField(generatedId = true)
    private int id;
    
    @DatabaseField
    private String tipo;
    
    @DatabaseField
    public double vida;
    
    @DatabaseField
    public double dano;    

//Start GetterSetterExtension Source Code
  
    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }
    
    public String getTipo(){
        return this.tipo;
    }

    public void setTipo(String tipo){
        this.tipo = tipo;
    }
 
    public double getVida(){
        return this.vida;
    }

    public void setVida(double vida){
        this.vida = vida;
    }

    public double getDano(){
        return this.dano;
    }
    
    public void setDano(double dano){
        this.dano = dano;
    }
    
}//End class
```
