package DataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import Comercial.*;

/**
 *
 * @author Grupo 10
 */
public class ModeloDAO {
    
     /**
     * Verifica se um modelo com uma certa key (nome do modelo) existe.
     * @param key
     * @return 
     * @throws java.lang.Exception 
     */
    public boolean containsKey(Object key) throws SQLException, Exception {
        boolean resultado = false;
        Connection c = Connect.connect();
        if(c!=null){
            PreparedStatement ps = c.prepareStatement("SELECT nome FROM Modelo WHERE nome = ?");
            ps.setString(1, (String) key);
            ResultSet rs = ps.executeQuery();
            resultado = rs.next();
            Connect.close(c);
        }
        else{throw new Exception("Unable to establish connection");}

        return resultado;
            
    }
    
     /**
     * Devolve um modelo com uma determinada key(nome do modelo).
     * @param key
     * @return 
     * @throws java.lang.Exception 
     */
    public Modelo get(Object key) throws SQLException, Exception{
        Connection c = Connect.connect();
        if(c!=null){
            Modelo resultado = null;
            PreparedStatement ps = c.prepareStatement("SELECT * FROM modelo WHERE nome = ?");
            ps.setString(1, (String) key);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                resultado = new Modelo(rs.getString("nome"),rs.getDouble("preco"));
            }
            Connect.close(c);
            return resultado;
        }
        else{throw new Exception("Unable to establish connection");}
    }
    
     /**
     * Devolve o modelo que faz parte de uma configuração com um determinado id.
     * @param id
     * @return 
     * @throws java.lang.Exception 
     */
    public Modelo getModelFromConfig(int id) throws SQLException, Exception{
        Connection c = Connect.connect();
        if(c!=null){
            Modelo resultado = null;
            PreparedStatement ps = c.prepareStatement("SELECT Modelo_nome FROM configuracao WHERE idConfiguracao = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                ps = c.prepareStatement("SELECT * FROM modelo WHERE nome = ?");
                ps.setString(1,rs.getString("Modelo_nome"));
                rs = ps.executeQuery();
                if(rs.next()){
                    resultado = new Modelo(rs.getString("nome"),rs.getDouble("preco"));
                }
            }
            Connect.close(c);
            return resultado;
        }
        else{throw new Exception("Unable to establish connection");}
    }
    
     /**
     * Devolve a lista de todos os modelos presentes na base de dados.
     * @return 
     * @throws java.sql.SQLException 
     * @throws java.lang.Exception 
     */
    public List<Modelo> values() throws SQLException, Exception{
        Connection c = Connect.connect();
        if(c!=null){
            List<Modelo> r = new ArrayList<>();
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("Select * From modelo");
            for(;rs.next();){
                r.add(new Modelo(rs.getString("nome"),rs.getDouble("preco")));
            }
            Connect.close(c);
            return r;
        }
        else {throw new Exception("Unable to establish connection");}
    }
}
