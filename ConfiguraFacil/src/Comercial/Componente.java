package Comercial;

import DataAccess.ComponenteDAO;

/**
 * Classe relativa ao Componente.
 * 
 * @author Grupo 10
 */
public abstract class Componente {
    // variaveis de instância
    private double preco; // preço de um componente
    private final String nome; // nome do componente(único)
    private int stock; // stock (nº de unidade disponíveis) de um componente 
    private ComponenteDAO componentes;  // para puder aceder aos outros componentes (necessários/incompatíveis do dado comp) da base de dados
    
    /**
     * Construtor vazio do Componente
     */
    public Componente(){
        this.preco = 0;
        this.nome = "";
        this.stock = 0;
        this.componentes = new ComponenteDAO();
    }
    
     /**
     * Construtor parametrizado do Componente 
     * @param preco
     * @param nome
     * @param stock
     */
    public Componente(double preco, String nome, int stock){
        this.preco = preco;
        this.nome = nome;
        this.stock = stock;
        this.componentes = new ComponenteDAO();
    }
   
     /**
     * Construtor com 1 componente
     * @param c
     */
    public Componente(Componente c){
        this.preco = c.getPreco();
        this.nome = c.getNome();
        this.stock = c.getStock();
        this.componentes = new ComponenteDAO();
    }
    
     /**
     * Retorna o preço do componente.
     * @return 
     */
    public double getPreco(){
        return this.preco;
    }
    
     /**
     * Retorna o nome do componente.
     * @return 
     */
    public String getNome(){
        return this.nome;
    }
    
    /**
     * Retorna o stock do componente.
     * @return 
     */
    public int getStock(){
        return this.stock;
    }
    
   /**
     * Atualiza o stock com uma dada quantidade.
     * @param quantidade 
     */ 
    public void atualizaStock(int quantidade){
        this.stock = quantidade;
        
    }
    
     /**
     * Atualiza o preço com um dado preço.
     * @param novoPreco
     */ 
    public void atualizaPreco(double novoPreco){
        this.preco = novoPreco;
    }
    
     /**
     * Método equals do componente.
     * @param o
     * @return
     */ 
    public boolean equals(Object o){
        if (o == this) return true;
        if (o == null || o.getClass() != this.getClass()) return false;
        
        Componente c = (Componente) o;
        
        return this.nome.equals(c.getNome());
    }
     
     /**
     * Método toString do componente.
     * @return
     */ 
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.nome).append(" (").append(this.preco);
        sb.append(" euros)");
        return sb.toString();
    }
}
