package Comercial;

/**
 * Classe relativa a DetalheInterior (é um tipo de componente).
 * 
 * @author Grupo 10
 */
public class DetalheInterior extends Componente{
    
    /**
     * Construtor parametrizado do DetalheInterior.
     * @param preco
     * @param nome
     * @param stock 
     */
    public DetalheInterior(double preco, String nome, int stock){
        super(preco, nome, stock);
    }
}
