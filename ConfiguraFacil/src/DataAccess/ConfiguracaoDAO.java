package DataAccess;

import java.sql.*;
import Comercial.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Grupo 10
 */
public class ConfiguracaoDAO{
    
    /**
     * Verifica se existe uma configuração com uma certa key(id).
     * @param key
     * @return 
     * @throws java.lang.Exception 
     */
    public boolean containsKey(Object key) throws NullPointerException, Exception {
            boolean resultado = false;
            Connection c = Connect.connect();
            if(c!=null){
                PreparedStatement ps = c.prepareStatement("SELECT idConfiguracao FROM configuracao WHERE configuracao = ?");
                ps.setInt(1, (int) key);
                ResultSet rs = ps.executeQuery();
                resultado = rs.next();
                Connect.close(c);
            }
            else{throw new Exception("Unable to establish connection");}
            
            return resultado;
            
    }
    
    /**
     * Adiciona à base de dados uma configuração, bem como os seus componentes e pacotes a partir da configuração atual.
     * @param ca
     * @throws java.lang.Exception 
     */
    public void put(ConfAtual ca) throws Exception{
            Connection c = Connect.connect();
            if(c!=null){
                //try{
                    //c.setAutoCommit(false);
                    PreparedStatement ps = c.prepareStatement("INSERT INTO configuracao (Modelo_nome,nif,telefone,email,nome,morada)"
                                                  + "VALUES (?,?,?,?,?,?)");
                    ps.setString(1, ca.getModelo().getNome());
                    ps.setInt(2, ca.getNif());
                    ps.setString(3, ca.getTelefone());
                    ps.setString(4, ca.getEmail());
                    ps.setString(5, ca.getNome());
                    ps.setString(6, ca.getMorada());
                    ps.executeUpdate();
                    
                    Statement s = c.createStatement();
                    ResultSet rs = s.executeQuery("select idConfiguracao from configuracao where idConfiguracao = (Select max(idConfiguracao) From configuracao);");
                    int id = 0;
                    if(rs.next()) id = rs.getInt("idConfiguracao");
                    else {throw new Exception("Erro: não conseguiu identificar o id da ultima conf");}
                    
                    ps = c.prepareStatement("Insert Into configuracao_has_componente VALUES (?,?)");
                    ps.setInt(1, id);
                    for(Componente co : ca.getComponentes()){
                        ps.setString(2, co.getNome());
                        ps.executeUpdate();
                    }

                    ps = c.prepareStatement("Insert Into configuracao_has_pacote VALUES (?,?)");
                    ps.setInt(1, id);
                    for(Pacote p : ca.getPacotes()){
                        ps.setString(2, p.getNome());
                        ps.executeUpdate();
                    }
                    //c.commit();
                //}catch(SQLException e){
                  //  c.rollback();
                //}finally{
                  //  Connect.close(c);
                }
            else{throw new Exception("Unable to establish connection");}
            Connect.close(c);
    }
    
    /**
     * Devolve uma configuração com uma determinada key(id).
     * @param key
     * @return 
     * @throws java.lang.Exception 
     */
    public Configuracao get(Object key) throws Exception{
            Connection c = Connect.connect();
            if(c!=null){
                Configuracao resultado = null;
                PreparedStatement ps = c.prepareStatement("SELECT * FROM configuracao WHERE idConfiguracao = ?");
                ps.setInt(1, (int) key);
                ResultSet rs = ps.executeQuery();
                if (rs.next()){
                    resultado = new Configuracao(rs.getInt("idConfiguracao"),rs.getString("nome"),
                            rs.getString("morada"),rs.getInt("nif"),rs.getString("telefone"),rs.getString("email"));
                }
                Connect.close(c);
                return resultado;
            }
        else{throw new Exception("Unable to establish connection");}
    }
    
    /**
     * Remove os componentes e pacotes associadas a uma determinada configuração e, em seguida, a própria configuração.
     * @param key
     * @throws java.lang.Exception 
     */
     public void remove(Object key) throws Exception{
        Connection c = Connect.connect();
        if(c!=null){
            //try{
              //  c.setAutoCommit(false);
                PreparedStatement ps = c.prepareStatement("DELETE FROM configuracao_has_componente WHERE Configuracao_idConfiguracao = ?");
                ps.setInt(1, (int) key);
                ps.executeUpdate();
                
                ps = c.prepareStatement("DELETE FROM configuracao_has_pacote WHERE Configuracao_idConfiguracao = ?");
                ps.setInt(1, (int) key);
                ps.executeUpdate();
                
                ps = c.prepareStatement("DELETE FROM configuracao WHERE idConfiguracao = ?");
                ps.setInt(1, (int) key);
                ps.executeUpdate();
                
                //c.commit();
           // }catch(SQLException e){
             //   c.rollback();
            //}finally{
              //  Connect.close(c);
              //}
        }
        else{throw new Exception("Unable to establish connection");}
        
        Connect.close(c);
    }
    
     /**
     * Devolve a lista dos id's(inteiros) das configurações presentes na base de dados (que ainda faltam mandar fabricar).
     * @return 
     * @throws java.lang.Exception 
     */
    public List<Integer> getListaEspera() throws Exception{
          Connection c = Connect.connect();
        if(c!=null){
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("Select idConfiguracao from configuracao");
            List<Integer> resultado = new ArrayList<>();
            for(;rs.next();){
                resultado.add(rs.getInt("idConfiguracao"));
            }
            Connect.close(c);
            return resultado;
        }
        else{throw new Exception("Unable to establish connection");} 
    } 

}

