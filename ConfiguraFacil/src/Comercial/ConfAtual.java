package Comercial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe relativa à configuração atual.
 * 
 * @author Grupo 10 
 */
public class ConfAtual {
    // variáveis de instância
    private String nome; // nome do Cliente que está a fazer a configuração atual (pedida no fim da conf realizada)
    private String morada; // morada do Cliente que está a fazer a configuração atual (pedida no fim da conf realizada)
    private int nif; // nif do Cliente que está a fazer a configuração atual (pedida no fim da conf realizada)
    private String telefone; // nome do Cliente que está a fazer a configuração atual (pedida no fim da conf realizada)
    private String email; // email do Cliente que está a fazer a configuração atual (pedida no fim da conf realizada)
    private Modelo modelo; // modelo selecionado para a configuração atual
    private List<Pacote> pacotes; // lista de pacotes escolhidos na configuração atual
    private List<Componente> componentes; // lista de componentes escolhidos na configuração atual
    
    
     /** 
     * Construtor vazio da Configuração atual.
     */
    public ConfAtual(){
        this.nome = null;
        this.morada = null;
        this.nif = 0;
        this.telefone = null;
        this.email = null;
        this.modelo = null;
        this.pacotes = new ArrayList<>();
        this.componentes = new ArrayList<>();
    }
    
     /** 
     * Construtor que define apenas o modelo da configuração atual.
     * @param mod
     */
    public ConfAtual(Modelo mod){
        this.nome = null;
        this.morada = null;
        this.nif = 0;
        this.telefone = null;
        this.email = null;
        this.modelo = mod;
        this.pacotes = new ArrayList<>();
        this.componentes = new ArrayList<>();
    }
    
     /** 
     * Retorna o modelo da confAtual
     * @return
     */
    public Modelo getModelo(){
        return this.modelo;
    }
    
     /** 
     * Retorna a lista dos Pacotes da confAtual
     * @return
     */
    public List<Pacote> getPacotes(){
        return this.pacotes;
    }
    
    /** 
     * Retorna a lista dos Componentes da confAtual
     * @return
     */
    public List<Componente> getComponentes(){
        return this.componentes;
    }
    
    /** 
     * Retorna o nome do cliente da confAtual
     * @return
     */
    public String getNome(){
        return this.nome;
    }
    
    /** 
     * Retorna a morada do cliente da confAtual
     * @return
     */
    public String getMorada(){
        return this.morada;
    }
    
    /** 
     * Retorna o Nif do cliente da confAtual
     * @return
     */
    public int getNif(){
        return this.nif;
    }
    
    /** 
     * Retorna o telefone do cliente da confAtual
     * @return
     */
    public String getTelefone(){
        return this.telefone;
    }
    
    /** 
     * Retorna o email do cliente da confAtual
     * @return
     */
    public String getEmail(){
        return this.email;
    }
    
    /** 
     * Coloca um determinado modelo numa configuração atual.
     * @param m
     */
    public void setModelo(Modelo m){
        this.modelo = m;
    }
    
    /** 
     * Coloca um determinado nome de um cliente numa configuração atual.
     * @param nome
     */
    public void setNome(String nome){
        this.nome = nome;
    }
    
    /** 
     * Coloca uma determinada morada de um cliente numa configuração atual.
     * @param morada
     */
    public void setMorada(String morada){
        this.morada = morada;
    }
    
    /** 
     * Coloca um determinado nome de um cliente numa configuração atual.
     * @param nif
     */
    public void setNif(int nif){
        this.nif = nif;
    }
    
    /** 
     * Coloca um determinado telofone de um cliente numa configuração atual.
     * @param telefone
     */
    public void setTelefone(String telefone){
        this.telefone = telefone;
    }
    
     /** 
     * Coloca um determinado e-mail de um cliente numa configuração atual.
     * @param email
     */
    public void setEmail(String email){
        this.email = email;
    }
    
    /** 
     * Adiciona um dado pacote à lista de pacotes da confAtual 
     * @param p
     */
    public void addPacote(Pacote p){
        this.pacotes.add(p);
    }
    
    /** 
     * Adiciona um dado componente à lista de componentes da confAtual 
     * @param c
     */
    public void addComponente(Componente c){
        this.componentes.add(c);
    }
    
     /** 
     * Retira um dado pacote da lista de pacotes da confAtual 
     * @param p
     */
    public void removePacote(Pacote p){
        this.pacotes.remove(p);
    }
    
    /** 
     * Retira um dado componente da lista de componentes da confAtual 
     * @param p
     */
    public void retiraComponente(Componente c){
        this.componentes.remove(c);
    }
    
    /** 
     * Verifica se existe um dado tipo nos componentes escolhidos e componentes de pacotes escolhidos.
     * @param tipo
     * @return
     * @throws java.lang.Exception
     */
    public boolean existeTipo(String tipo) throws Exception{
         List<Componente> comps = new ArrayList<>(componentes);
         for(Pacote p : pacotes){
             List<Componente> cp = p.getComponentes(); 
             comps.addAll(cp);
         }
         for(Componente c : comps){
             String s = c.getClass().getSimpleName();
             if(s.equals(tipo)) return true;
         }
         return false;
    }
    
    /** 
     * Calcula e retorna o custo da confAtual.
     * @return
     * @throws java.lang.Exception
     */
    public double getCusto() throws Exception{
        double total = 0;
        if(modelo != null) total += modelo.getPreco();
        total += componentes.stream().mapToDouble(Componente :: getPreco).sum();
        for(Pacote p : pacotes){
            total += p.getPrecosReduzidos().values().stream().mapToDouble(e->e).sum();
        }
        return total;
    }
    
    /** 
     * Substitui um componente dos tipo que recebe pelo dado componente.
     * @param c
     */
    public void substituiComp(Componente c){
        retiraTipo(c.getClass().getSimpleName());
        componentes.add(c);
    }
    
    /** 
     * Retira o(s) componente(s) de um dado tipo da confAtual.
     * @param tipo
     */
    public void retiraTipo(String tipo){
        List<Componente> retirados = new ArrayList<>();
        for(Componente ci : componentes){
            if(ci.getClass().getSimpleName().equals(tipo)) retirados.add(ci);
        }
        for(Componente ci : retirados) componentes.remove(ci);
    }
    
    /** 
     * Retira os componentes e pacotes de uma confAtual.
     */
    public void apagaComponentes(){
        this.pacotes = new ArrayList<>();
        this.componentes = new ArrayList<>();
    }
    
    /** 
     * Retorna um map com o nome e respetivo preço dos componente escolhidos da confAtual.
     * @return
     * @throws java.lang.Exception
     */
    public Map<String,Double> getComponentesPreco() throws Exception{
        Map<String,Double> ret = new HashMap<String,Double>();
        for(Componente c : this.componentes){
            ret.put(c.getNome(), c.getPreco());
        }
        
        for(Pacote p : this.pacotes){
            for(Map.Entry<String,Double> e : p.getPrecosReduzidos().entrySet()){
                ret.put(e.getKey(), e.getValue());
            }
        }
        
        return ret;
    }
}
