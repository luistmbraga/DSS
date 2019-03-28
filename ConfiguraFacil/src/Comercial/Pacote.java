package Comercial;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import DataAccess.ComponenteDAO;
/**
 * Classe relativa ao pacote.
 * 
 * @author Grupo 10 
 */
public class Pacote implements Comparable<Pacote>{
    // variaveis de instância
    private String nome; // nome (único) 
    private ComponenteDAO componentes; // para puder aceder aos componentes relativos a um pacote guardados na base de dados
    
    /**
     * Construtor parametrizado
     * @param nome 
     */
    public Pacote(String nome){
        this.nome = nome;
        this.componentes = new ComponenteDAO();
    }
    
    /**
     * Construtor com um pacote
     * @param p 
     */
    public Pacote(Pacote p){
        this.nome = p.getNome();
        this.componentes = new ComponenteDAO();
    }
    
    /**
     * Método compareTo (Ordem é através do nome).
     * @param p
     * @return 
     */
    public int compareTo(Pacote p){
        return this.nome.compareTo(p.getNome());
    }
    
    /**
     * Retorna o nome do pacote.
     * @return 
     */
    public String getNome(){
        return this.nome;
    }
    
    /**
     * Altera o nome de um pacote.
     * @param nome 
     */
    public void setNome(String nome){
        this.nome = nome;
    }
    
    /**
     * Retorna a lista de componentes do pacote.
     * @return
     * @throws Exception 
     */
    public List<Componente> getComponentes() throws Exception{
        return componentes.getComponentesFromPack(nome);
    }
    
    /**
     * Retorna um map com nome e respetivo preço (com desconto por estar no pacote(10%)) dos componentes do pacote.
     * @return
     * @throws Exception 
     */
    public Map<String,Double> getPrecosReduzidos() throws Exception{
        Map<String,Double> ret = new HashMap<>();
        List<Componente> comps = componentes.getComponentesFromPack(this.nome);
        for(Componente c:comps){
            String nome = c.getNome();
            double preco = c.getPreco()*0.9;
            ret.put(nome, preco);
        }
        return ret;
    }
    
    /**
     * Método equals.
     * @param o
     * @return 
     */
    public boolean equals(Object o){
        if (o == this) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        
        Pacote p = (Pacote) o;
        
        return this.nome.equals(p.getNome());
    }
    
    /**
     * Método toString.
     * @return 
     */
    public String toString(){
         StringBuilder sb = new StringBuilder();
         sb.append("Nome do Pacote: ").append(this.nome).append("\n");
         sb.append("Componentes: \n");
         
         return sb.toString();
    }
}
