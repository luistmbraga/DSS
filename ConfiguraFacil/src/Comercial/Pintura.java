package Comercial;

/**
 * Classe relativa a Pintura (é um tipo de componente).
 * 
 * @author Grupo 10
 */
public class Pintura extends Componente{
    /**
     * Construtor parametrizado da Pintura.
     * @param preco
     * @param nome
     * @param stock 
     */
    public Pintura(double preco, String nome, int stock){
        super(preco, nome, stock);
    }
            
}
