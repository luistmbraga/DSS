package DataAccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import Comercial.*;

/**
 *
 * @author Grupo 10 
 */
public class PacoteDAO {
    
     /**
     * Verifica se existe um pacote com uma determinda key(nome do pacote).
     * @param key
     * @return 
     * @throws java.lang.Exception 
     */
    public boolean containsKey(Object key) throws NullPointerException, Exception {
            boolean resultado = false;
            Connection c = Connect.connect();
            if(c!=null){
                PreparedStatement ps = c.prepareStatement("SELECT nome FROM pacote WHERE nome = ?");
                ps.setString(1, (String) key);
                ResultSet rs = ps.executeQuery();
                resultado = rs.next();
                Connect.close(c);
            }
            else{throw new Exception("Unable to establish connection");}
            
            return resultado;
            
    }
    
     /**
     * Remove um modelo com uma determinada key(nome do pacote).
     * @param key
     * @throws java.lang.Exception 
     */
    public void remove(Object key) throws Exception{
        Connection c = Connect.connect();
        if(c!=null){
            PreparedStatement ps = c.prepareStatement("DELETE ? FROM pacote");
            ps.setString(1, (String) key);
            Connect.close(c);
        }
        else{throw new Exception("Unable to establish connection");}
    }
    
     /**
     * Devolve o pacote que tem uma determinada key (nome do pacote).
     * @param key
     * @return 
     * @throws java.lang.Exception 
     */
    public Pacote get(Object key) throws Exception{
            Connection c = Connect.connect();
            if(c!=null){
            Pacote resultado = null;
            PreparedStatement ps = c.prepareStatement("Select * From pacote Where nome = ?");
            ps.setString(1, (String) key);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) resultado = new Pacote(rs.getString("nome"));
            Connect.close(c);
            return resultado;
            }
        else{throw new Exception("Unable to establish connection");}
    }
    
     /**
     * Adiciona um determinado pacote à base de dados.
     * @param p
     * @throws java.lang.Exception 
     */
     public void put(Pacote p) throws Exception{
            Connection c = Connect.connect();
            if(c!=null){
                PreparedStatement ps = c.prepareStatement("INSERT INTO pacote VALUES (?)");
                ps.setString(1, p.getNome());
                ps.executeUpdate();
                Connect.close(c);
            }
            else{throw new Exception("Unable to establish connection");}
    }
     
     /**
     * Devolve a lista de pacotes pertencentes a uma configuração com um determinado id.
     * @param id
     * @return 
     * @throws java.lang.Exception 
     */ 
    public List<Pacote> getPacksFromConfig(int id) throws Exception{
        Connection c = Connect.connect();
            if(c!=null){
                Statement s = c.createStatement();
                ResultSet rsl = s.executeQuery("Select Pacote_nome From configuracao_has_pacote where Configuracao_idConfiguracao = " + id);
                List<Pacote> resultado = new ArrayList<>();
                for(;rsl.next();){
                    PacoteDAO po = new PacoteDAO();
                    resultado.add(po.get(rsl.getString("Pacote_nome")));
                }
                Connect.close(c);
                return resultado;
            }   
            else {throw new Exception("Unable to establish connection");}
    }
     
     /**
     * Devolve um set com o nome de todos os pacotes pertencentes à base de dados.
     * @return 
     * @throws java.lang.Exception 
     */
    public Set<String> keySet() throws Exception{
        Connection c = Connect.connect();
        Set<String> ret = new TreeSet<>();
        if(c!=null){
            Pacote resultado = null;
            PreparedStatement ps = c.prepareStatement("SELECT nome FROM pacote");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                ret.add(rs.getString(1));
            }
            Connect.close(c);
        }
        else{throw new Exception("Unable to establish connection");}
        return ret;
    }
    
}
