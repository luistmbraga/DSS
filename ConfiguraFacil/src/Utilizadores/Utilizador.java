package Utilizadores;

/**
 * Classe relativa ao Utilizador.
 * 
 * @author Grupo 10
 */
public abstract class Utilizador {
    // variaveis de instância
    private String nome; // nome (único)
    private String pass; // palavra-passe
    
    /**
     * Construtor parametrizado.
     * @param nome
     * @param pass 
     */
    public Utilizador(String nome, String pass){
        this.nome = nome;
        this.pass = pass;
    }
    
    /**
     * Construtor com um Utilizador.
     * @param u 
     */
    public Utilizador(Utilizador u){
        this.nome = u.getNome();
        this.pass = u.getPass();
    }
    
    /**
     * Retorna o nome do Utilizador.
     * @return 
     */
    public String getNome(){
        return this.nome;
    }
    
    /**
     * Retorna a palavra-passe do Utilizador.
     * @return 
     */
    public String getPass(){
        return this.pass;
    }
    
    /**
     * Altera o nome do Utilizador.
     * @param nome 
     */
    public void setNome(String nome){
        this.nome = nome;
    }
    
    /**
     * Altera a palavra-passe do Utilizador.
     * @param pass 
     */
    public void setPass(String pass){
        this.pass = pass;
    }
    
    /**
     * Método equals.
     * @param o
     * @return 
     */
    public boolean equals(Object o){
        if (o == this) return true;
        if(o == null || o.getClass() != this.getClass()) return false;
        
        Utilizador u = (Utilizador) o;
        
        return this.nome.equals(u.getNome());
    }
    
    /**
     * Método toString.
     * @return 
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Nome do Utilizador: " ).append(this.nome).append(" \n");
        sb.append("Password: ").append(this.pass).append(" \n");
        return sb.toString();
    }
    
}
