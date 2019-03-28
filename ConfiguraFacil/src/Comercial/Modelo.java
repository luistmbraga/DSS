package Comercial;

/**
 * Classe relativa ao Modelo.
 * 
 * @author Grupo 10
 */
public class Modelo {
    // variaveis de instância
    private String nome; // nome do Modelo
    private double preco; // preço do Modelo
    
    /**
     * Construtor parametrizado do Modelo.
     * @param nome
     * @param preco 
     */
    public Modelo(String nome,double preco){
        this.nome = nome;
        this.preco = preco;
    }
    /**
     * Retorna o preço do modelo.
     * @return 
     */
    public double getPreco(){
        return this.preco;
    }
    
    /**
     * Retorna o nome do modelo.
     * @return 
     */
    public String getNome(){
        return this.nome;
    }
    
    /**
     * Método toString. 
     * @return 
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.nome).append("   (").append(this.preco).append(" euros) ");
        return sb.toString();
    }
}
