package DataAccess;


import java.sql.*;
import Utilizadores.*;

/**
 *
 * @author Grupo 10 
 */
public class UtilizadorDAO{
    
     /**
     * Verifica se existe algum utilizador com uma determinada key(nome do utilizador).
     * @param key
     * @return 
     * @throws java.lang.Exception 
     */
    public boolean containsKey(Object key) throws NullPointerException, Exception {
            boolean resultado = false;
            Connection c = Connect.connect();
            if(c!=null){
                PreparedStatement ps = c.prepareStatement("SELECT nome FROM utilizador WHERE nome = ?");
                ps.setString(1, (String) key);
                ResultSet rs = ps.executeQuery();
                resultado = rs.next();
                Connect.close(c);
            }
            else{throw new Exception("Unable to establish connection");}
            
            return resultado;
            
    }
    
     /**
     * Adiciona um determinado utilizador à base de dados.
     * @param u
     * @throws java.lang.Exception 
     */
    public void put(Utilizador u) throws Exception{
            Connection c = Connect.connect();
            if(c!=null){
                PreparedStatement ps = c.prepareStatement("insert into utilizador values(?,?,?)");
                ps.setString(1, u.getNome());
                ps.setString(2, u.getPass());
                String tipo = u.getClass().getSimpleName();
                ps.setString(3, tipo);
                ps.executeUpdate();
                Connect.close(c);
            }
            else{throw new Exception("Unable to establish connection");}
    }
    
     /**
     * Devolve o utilizador com uma determinada key(nome do utilizador).
     * @param key
     * @return 
     * @throws java.lang.Exception 
     */
    public Utilizador get(Object key) throws Exception{
            Connection c = Connect.connect();
            if(c!=null){
            Utilizador resultado = null;
            PreparedStatement ps = c.prepareStatement("SELECT * FROM utilizador WHERE nome=?");
            ps.setString(1, (String) key);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                String tipo = rs.getString(3);
                if (tipo.equals("Vendedor")){
                    resultado = new Vendedor(rs.getString(1),rs.getString(2));
                }
                if (tipo.equals("Adminstrador")){
                    resultado = new Adminstrador(rs.getString(1),rs.getString(2));
                }
                if (tipo.equals("Fabricante")){
                    resultado = new Fabricante(rs.getString(1), rs.getString(2));
                }
            }
            else {throw new Exception("Não existe nenhum utilizador com o nome "+(String)key);}
            Connect.close(c);
            return resultado;
            }
        else{throw new Exception("Unable to establish connection");}
    }
    
     /**
     * Altera a palavra-passe do utilizador que tem uma determinada key(nome de utilizador).
     * @param key
     * @param passnova
     * @throws java.lang.Exception 
     */
    public void update(Object key, String passnova) throws Exception{
        Connection c = Connect.connect();
        if(c!=null){
            PreparedStatement ps = c.prepareStatement("Update utilizador Set pass = ? where nome = ?");
            ps.setString(1, passnova);
            ps.setString(2, (String) key);
            ps.executeUpdate();
            Connect.close(c);
        }
        else {throw new Exception("Unable to establish connection");}
    }
    
     /**
     * Remove o utilizador que tem uma determinada key(nome do utilizador).
     * @param key
     * @throws java.lang.Exception 
     */
     public void remove(Object key) throws Exception{
        Connection c = Connect.connect();
        if(c!=null){
            PreparedStatement ps = c.prepareStatement("DELETE FROM utilizador where nome = ?");
            ps.setString(1, (String) key);
            ps.executeUpdate();
            Connect.close(c);
        }
        else{throw new Exception("Unable to establish connection");}
    }
    
}
