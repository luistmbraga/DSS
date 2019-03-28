package Comercial;

/**
 * Classe relativa ao Motor (é um tipo de componente).
 * 
 * @author Grupo 10
 */
public class Motor extends Componente{
     /**
      * Construtor parametrizado do Motor.
      * @param preco
      * @param nome
      * @param stock 
      */
     public Motor(double preco, String nome, int stock){
        super(preco, nome, stock);
    }
}
