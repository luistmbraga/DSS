package DataAccess;

import java.util.List;
import java.util.ArrayList;
import java.sql.*;
import Comercial.*;

/**
 *
 * @author Grupo 10
 */
public class ComponenteDAO {
        
    /**
     * Verifica se um componente com uma certa key (nome do componente) existe.
     * @param key
     * @return 
     * @throws java.lang.Exception 
     */
    public boolean containsKey(Object key) throws NullPointerException, Exception {
            boolean resultado = false;
            Connection c = Connect.connect();
            if(c!=null){
                PreparedStatement ps = c.prepareStatement("SELECT nome FROM componente WHERE nome = ?");
                ps.setString(1, (String) key);
                ResultSet rs = ps.executeQuery();
                resultado = rs.next();
                Connect.close(c);
            }
            else{throw new Exception("Unable to establish connection");}
            
            return resultado;
    }
    
    /**
     * Adiciona um novo componente à base de dados.
     * @param u
     * @throws java.lang.Exception 
     */
    public void put(Componente u) throws Exception{
            Connection c = Connect.connect();
            if(c!=null){
                PreparedStatement ps = c.prepareStatement("INSERT INTO componente VALUES (?,?,?,?)");
                ps.setString(1, u.getNome());
                ps.setDouble(2, u.getPreco());
                ps.setInt(3,u.getStock());
                String tipo = u.getClass().getSimpleName();
                ps.setString(4, tipo);
                ps.executeUpdate();
                Connect.close(c);
            }
            else{throw new Exception("Unable to establish connection");}
    }
    
    /**
     * Devolve um componente da base de dados que tenha a key passada como parametro(nome do componente).
     * @param key
     * @return 
     * @throws java.lang.Exception 
     */
    public Componente get(Object key) throws Exception{
            Connection c = Connect.connect();
            if(c!=null){
            Componente resultado = null;
            PreparedStatement ps = c.prepareStatement("SELECT * FROM componente WHERE nome=?");
            ps.setString(1, (String) key);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                String tipo = rs.getString(4);
                if (tipo.equals("DetalheExterior")){
                    resultado = new DetalheExterior(rs.getDouble(2),rs.getString(1),rs.getInt(3));
                }
                if (tipo.equals("DetalheInterior")){
                    resultado = new DetalheInterior(rs.getDouble(2),rs.getString(1),rs.getInt(3));
                }
                if (tipo.equals("Jantes")){
                    resultado = new Jantes(rs.getDouble(2),rs.getString(1),rs.getInt(3));
                }
                if (tipo.equals("Motor")){
                    resultado = new Motor(rs.getDouble(2),rs.getString(1),rs.getInt(3));
                }
                if (tipo.equals("Pintura")){
                    resultado = new Pintura(rs.getDouble(2),rs.getString(1),rs.getInt(3));
                }
                if (tipo.equals("Pneu")){
                    resultado = new Pneu(rs.getDouble(2),rs.getString(1),rs.getInt(3));
                }
            }
            Connect.close(c);
            return resultado;
            }
        else{throw new Exception("Unable to establish connection");}
    }
    
    /**
     * Remove um componente, com uma certa key (nome do componente), da base de dados.
     * @param key
     * @throws java.lang.Exception 
     */
     public void remove(Object key) throws Exception{
        Connection c = Connect.connect();
        if(c!=null){
            PreparedStatement ps = c.prepareStatement("DELETE ? FROM componente");
            ps.setString(1, (String) key);
            Connect.close(c);
        }
        else{throw new Exception("Unable to establish connection");}
    }
    
    /**
     * Devolve a lista dos componentes incompativeis de um componente com um certo nome. 
     * @param comp
     * @return 
     * @throws java.lang.Exception 
     */
    public List<Componente> getIncompativeis(String comp) throws Exception{
        Connection c = Connect.connect();
        if(c != null){
            Statement st1 = c.createStatement();
            Statement st2 = c.createStatement();
            ResultSet rs1 = st1.executeQuery("Select Componente1 from componentesincompativeis where Componente2 = "+"'"+comp+"'");
            ResultSet rs2 = st2.executeQuery("Select Componente2 from componentesincompativeis where Componente1 = "+"'"+comp+"'");
            List<Componente> resultado = new ArrayList<>();
            for(;rs1.next();){
                ComponenteDAO co = new ComponenteDAO();
                resultado.add(co.get(rs1.getString("Componente1")));
            }
            for(;rs2.next();){
                ComponenteDAO co = new ComponenteDAO();
                resultado.add(co.get(rs2.getString("Componente2")));
            }
            Connect.close(c);
            return resultado;
        }
        else {throw new Exception("Unable to establish connection");}
    }
    
    /**
     * Devolve a lista dos componentes necessários de um componente com um determinado nome.
     * @param comp
     * @return 
     * @throws java.lang.Exception 
     */
    public List<Componente> getNecessarios(String comp) throws Exception{
        Connection c = Connect.connect();
        if(c != null){
            Statement st1 = c.createStatement();
            ResultSet rs1 = st1.executeQuery("Select Componente_necessario from componentesnecessarios where Componente = "+"'"+comp+"'");
            List<Componente> resultado = new ArrayList<>();
            for(;rs1.next();){
                ComponenteDAO co = new ComponenteDAO();
                resultado.add(co.get(rs1.getString("Componente_necessario")));
            }
            Connect.close(c);
            return resultado;
        }
        else {throw new Exception("Unable to establish connection");}
    }
    
    /**
     * Devolve a lista de componentes de uma determinada configuração.
     * @param id
     * @return 
     * @throws java.lang.Exception 
     */
    public List<Componente> getComponentesFromConfig(int id) throws Exception{
            Connection c = Connect.connect();
            if(c!=null){
                Statement s = c.createStatement();
                ResultSet rsl = s.executeQuery("Select Componente_nome From configuracao_has_componente where Configuracao_idConfiguracao = " + id);
                List<Componente> resultado = new ArrayList<>();
                for(;rsl.next();){
                    ComponenteDAO co = new ComponenteDAO();
                    resultado.add(co.get(rsl.getString("Componente_nome")));
                }
                Connect.close(c);
                return resultado;
            }   
            else {throw new Exception("Unable to establish connection");}
    }
    
    /**
     * Devolve a lista dos componentes de um pacote com um determinado nome.
     * @param nome
     * @return 
     * @throws java.lang.Exception 
     */
    public List<Componente> getComponentesFromPack(String nome) throws Exception{
            Connection c = Connect.connect();
            if(c!=null){
                Statement s = c.createStatement();
                ResultSet rsl = s.executeQuery("Select Componente_nome From pacote_has_componente where Pacote_nome = " + "'" + nome + "'");
                List<Componente> resultado = new ArrayList<>();
                for(;rsl.next();){
                    ComponenteDAO co = new ComponenteDAO();
                    resultado.add(co.get(rsl.getString("Componente_nome")));
                }
                Connect.close(c);
                return resultado;
            }   
            else {throw new Exception("Unable to establish connection");}
    }
    
    /**
     * Devolve a lista dos componentes de um determinado tipo.
     * @param tipo
     * @return 
     * @throws java.lang.Exception 
     */
    public List<Componente> getComponentesFromTipo(String tipo) throws Exception{
         Connection c = Connect.connect();
        if(c!=null){
            PreparedStatement ps = c.prepareStatement("Select nome FROM componente where tipo = ?");
            ps.setString(1, (String) tipo);
            ResultSet rs = ps.executeQuery();
            List<Componente> resultado = new ArrayList<>();
            for(;rs.next();){
                ComponenteDAO co = new ComponenteDAO();
                resultado.add(co.get(rs.getString("nome")));
            }
            Connect.close(c);
            return resultado;
        }
        else{throw new Exception("Unable to establish connection");}
    }
    /**
     * Devolve a lista dos componentes todos da base de dados.
     * @return 
     * @throws java.lang.Exception 
     */
    public List<Componente> values() throws Exception{
        Connection c = Connect.connect();
        if(c!=null){
            Statement s = c.createStatement();
            ResultSet rsl = s.executeQuery("Select * from componente");
            List<Componente> resultado = new ArrayList<>();
            for(;rsl.next();){
                ComponenteDAO co = new ComponenteDAO();
                resultado.add(co.get(rsl.getString("nome")));
            }
            Connect.close(c);
            return resultado;
        }
        else{throw new Exception("Unable to establish connection");}
    }
    
    /**
     * Atualiza o stock de um componente com um determinado nome.
     * @param nome
     * @param stock
     * @throws java.lang.Exception 
     */
    public void updateStock(String nome, int stock) throws Exception{
        Connection c = Connect.connect();
        if(c!=null){
            PreparedStatement ps = c.prepareStatement("Update componente Set stock = ? where nome = ?");
            ps.setInt(1, stock);
            ps.setString(2, nome);
            ps.executeUpdate();
            Connect.close(c);
        }
        else{throw new Exception("Unable to establish connection");}
    }
}
