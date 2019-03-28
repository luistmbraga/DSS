package Comercial;

/**
 * Classe relativa ao Pneu (é um tipo de componente).
 *  
 * @author Grupo 10
 */
public class Pneu extends Componente{
    /**
     * Construtor parametrizado do Pneu.
     * @param preco
     * @param nome
     * @param stock 
     */
     public Pneu(double preco, String nome, int stock){
        super(preco, nome, stock);
    }
}
