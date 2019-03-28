package Comercial;

import java.util.HashMap;
import java.util.Map;
import DataAccess.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe relativa à Configuracao
 * 
 * @author Grupo 10
 */
public class Configuracao {
    // variaveis de instância
    private int id; // identificador 
    private String nome; // nome do cliente
    private String morada; // morada do cliente
    private int nif; // nif do cliente
    private String telefone; // telefone do cliente 
    private String email; // email do cliente
    private ModeloDAO modelo; // para puder aceder aos modelos da base de dados (para ir buscar o modelo relativo a esta configuração)
    private PacoteDAO pacotes; // para puder aceder aos pacotes da base de dados (para ir buscar os pacotes relativos a esta configuração)
    private ComponenteDAO componentes; // para puder aceder aos componentes da dados (para ir buscar os componentes relativo a esta configuração)
    
     /**
      * Construtor parametrizado da Configuracao
      * @param id
      * @param nome
      * @param morada
      * @param nif
      * @param telefone
      * @param email 
      */
    public Configuracao(int id, String nome, String morada, int nif, String telefone, 
                            String email){
        this.id = id;
        this.nome = nome;
        this.morada = morada;
        this.nif = nif;
        this.telefone = telefone;
        this.email = email;
        this.modelo = new ModeloDAO();
        this.pacotes = new PacoteDAO();
        this.componentes = new ComponenteDAO();
    }
    
     /**
     * Construtor com 1 configuração
     * @param c
     */
    public Configuracao(Configuracao c){
        this.id = c.getId();
        this.nome = c.getNome();
        this.morada = c.getMorada();
        this.nif = c.getNif();
        this.telefone = c.getTelefone();
        this.email = c.getEmail();
        this.modelo = new ModeloDAO();
        this.pacotes = new PacoteDAO();
        this.componentes = new ComponenteDAO();
    }
    
     /**
     * Retorna o id da configuração.
     * @return 
     */
    public int getId(){
        return this.id;
    }
    
    /**
     * Retorna o nome do cliente da configuração.
     * @return 
     */
    public String getNome(){
        return this.nome;
    }
    
    /**
     * Retorna a morada do cliente da configuração.
     * @return 
     */
    public String getMorada(){
        return this.morada;
    }
     /**
     * Retorna o nif do cliente da configuração.
     * @return 
     */
    public int getNif(){
        return this.nif;
    }
    
     /**
     * Retorna o telefone do cliente da configuração.
     * @return 
     */
    public String getTelefone(){
        return this.telefone;
    }
    
     /**
     * Retorna o e-mail do cliente da configuração.
     * @return 
     */
    public String getEmail(){
        return this.email;
    }
    
     /**
     * Retorna a lista dos componentes que terão que ser gastos pra eventualmente fabricar a conf.
     * @return 
     * @throws java.lang.Exception 
     */
    public List<Componente> getComponenteaGastar() throws Exception{
         List<Componente> lc = this.componentes.getComponentesFromConfig(this.id);
         List<Pacote> lp = this.pacotes.getPacksFromConfig(this.id);
         for(Pacote p : lp){
             List<Componente> aux = this.componentes.getComponentesFromPack(p.getNome());
             lc.addAll(aux);
         }
         return lc;
    }
    
    /**
     * Retorna Apresenta a lista das string's com a informação relativa a uma conf.
     * @return 
     * @throws java.lang.Exception 
     */
    public List<String> infConf() throws Exception{
        List<String> resultado = new ArrayList<>();
        resultado.add("idConfiguracao: " + this.id);
        resultado.add("Nome do cliente: " + this.nome);
        resultado.add("Morada do cliente: " + this.morada);
        resultado.add("Nif do cliente: " + this.nif);
        resultado.add("Telefone do cliente: " + this.telefone);
        resultado.add("Email do cliente: " + this.email);
        Modelo m = this.modelo.getModelFromConfig(this.id);
        resultado.add("Modelo do carro: " + m.getNome());
        List<Componente> lc = this.componentes.getComponentesFromConfig(this.id);
        String s = "Componentes da configuracao:  ";
        for(Componente c : lc){
            s+= c.getNome() + ";  ";
        }
        resultado.add(s);
        List<Pacote> lp = this.pacotes.getPacksFromConfig(this.id);
        String e = "Pacotes da configuracao:  ";
        for(Pacote p : lp){
            e+= p.getNome() + ";  ";
        }
        resultado.add(e);
        return resultado;
    }
}
