package Comercial;

/**
 * Classe relativa a DetalheExterior (é um tipo de componente).
 * 
 * @author Grupo 10
 */
public class DetalheExterior extends Componente{
     
     /**
      * Construtor parametrizado do DetalheExterior
      * @param preco
      * @param nome
      * @param stock 
      */
     public DetalheExterior(double preco, String nome, int stock){
        super(preco, nome, stock);
    }
}
