package dss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import Comercial.*;
import Utilizadores.*;
import DataAccess.*;


/**
 * Classe relativa ao facade.
 * 
 * @author Grupo 10
 */

public class Facade {
    //variaveis de instancia
    private UtilizadorDAO utilizadores; // para puder aceder aos utilizadores da base de dados 
    private ComponenteDAO componentes; // para puder aceder aos componentes da base de dados
    private ModeloDAO modelos; // para puder aceder aos modelos da base de dados
    private PacoteDAO pacotes; // para puder aceder aos pacotes da base de dados
    private ConfiguracaoDAO listaEspera; // para puder aceder às configurações que faltam ser fabricadas e estão registadas na base de
    private ConfAtual confAtual; // variável que será usada quando se estiver a fazer uma configuração para um cliente
    
    
    /**
     * Construtor vazio do Facade
     */
    public Facade(){
        this.utilizadores = new UtilizadorDAO();
        this.componentes = new ComponenteDAO();
        this.modelos = new ModeloDAO();
        this.pacotes = new PacoteDAO();
        this.listaEspera = new ConfiguracaoDAO();
        this.confAtual = new ConfAtual();
    }
    
    /** 
     * Responsável pelo login de um utlizador e devolve o tipo de utilizador que iniciou sessão (lança exceções caso não exista o utilizador ou caso a palavra-passe esteja incorreta).
     * @param user
     * @param pass
     * @return
     * @throws java.lang.Exception
     */
    
    public String login(String user,String pass) throws Exception{
        
        if(utilizadores.containsKey(user)){
            Utilizador u = utilizadores.get(user);
            if(u.getPass().equals(pass)){
                return u.getClass().getSimpleName();
            }else{
                throw new Exception("Palavra-passe incorreta!");
            }
        }
        else throw new Exception("Não existe nenhum utilizador com esse nome!");
    }
    
    /** 
     * Registar um tipo de utilizador no sistema (lança uma exceção caso o utilizador já exista).
     * @param user
     * @param pass
     * @param tipo
     * @throws java.lang.Exception
     */
    public void addUtilizador(String user, String pass, String tipo) throws Exception{
        if(!(utilizadores.containsKey(user))){
            if(tipo.equals("Vendedor")){
                utilizadores.put(new Vendedor(user,pass));
            }
            if (tipo.equals("Fabricante")){
                utilizadores.put(new Fabricante(user,pass));
            }
        }
        else throw new Exception("Já existe um utilizador com esse nome!");
        
    }
    
    /** 
     * Remove um utilizador do sistema (lança uma exceção caso o utilizador não exista).
     * @param user
     * @throws java.lang.Exception
     */
    public void removeUtilizador(String user) throws Exception{
        if(utilizadores.containsKey(user)){
            utilizadores.remove(user);
        }
        else throw new Exception("Não existe nenhum utilizador com esse nome!");
    }
    
    /** 
     * Altera a palavra-passe de um utilizador (lança uma exceção caso o utilizador não exista).
     * @param user
     * @param pass
     * @throws java.lang.Exception
     */
    public void updateUtilizador(String user, String pass) throws Exception{
        if(utilizadores.containsKey(user)){
            utilizadores.update(user, pass);
        }
        else throw new Exception("Esse utilizador não existe!");
    }
    
    
    /** 
     * Recebe o nome do componente e retorna a lista dos nomes dos seus componentes obrigatórios(lança exceção caso não se consiga estabelecer ligação com a base de dados).
     * @param nome
     * @return
     * @throws java.lang.Exception
     */
    public List<String> verificarComponentesEmFalta(String nome) throws Exception{
        Componente adici = componentes.get(nome);
        List<Componente> listaComp = new ArrayList<>();
        listaComp.add(adici);
        componentesObrigatorios(listaComp,new ArrayList<>(listaComp));
        
        //List<Componente> listaComp = this.componentes.getNecessarios(nome);
        List<String> l = new ArrayList<>();
        List<Componente> compsatuais = new ArrayList<>(confAtual.getComponentes());
        List<Pacote> packs = confAtual.getPacotes();
        for(Pacote p : packs){
            List<Componente> compack  = componentes.getComponentesFromPack(p.getNome());
            compsatuais.addAll(compack);
        }
        for(Componente c1 : listaComp){
            if(!compsatuais.contains(c1)) l.add(c1.getNome());
        }
        l.remove(nome);
            
        return l;
    }
    
    /** 
     * Recebe a lista dos nomes dos componentes obrigatórios e retorna uma lista com o nome dos componentes que são incompatíves com esa lista.
     * @param l
     * @return
     * @throws java.lang.Exception
     */
    public List<String> verificarIncompatibilidade(List<String> l) throws Exception{
        List<Componente> listaAtual = this.confAtual.getComponentes();
        List<Componente> listaIncompativeis = new ArrayList<>();
        List<String> cd = new ArrayList<>();
        for(String el : l){
            listaIncompativeis = this.componentes.getIncompativeis(el);
            for(Componente d: listaIncompativeis){
                if(listaAtual.contains(d)){
                    cd.add(d.getNome());
                }
            }
        }
        
        for(Pacote p : confAtual.getPacotes()){
            List<Componente> comppack = componentes.getComponentesFromPack(p.getNome());
            for(String el : l){
            listaIncompativeis = this.componentes.getIncompativeis(el);
                for(Componente d: listaIncompativeis){
                    if(comppack.contains(d)){
                        cd.add(d.getNome());
                    }
                }   
            }
        }
        return cd;    
    }
    
    /** 
     * Recebe a lista dos componentes incompatíveis e retorna a lista dos pacotes que contenham algum desses componentes incompatíveis.
     * @param l
     * @return
     * @throws java.lang.Exception
     */
    public List<String> verificarPacotesComComponentesIncompativeis(List<String> l) throws Exception{
        List<Pacote> listaAtualPacote = this.confAtual.getPacotes(); // retorna os pacotes que estao na configuração
        Set<String> cd = new TreeSet<>();
        List<Componente> lista = null;
        String nome = null;
        //Ciclo para guardar a lista dos nomes dos pacotes que estao na lista de incompativeis
        for (Pacote p : listaAtualPacote){
            lista = p.getComponentes(); // recebe a lista das componentes
            for(Componente c : lista){
                nome = c.getNome(); // recebe o nome da componente
                //verifica se faz parte 
                if(l.contains(nome)){
                    cd.add(p.getNome());
                }
            }
        }
        return new ArrayList<>(cd);
    }
    
    /** 
     * Retira da configuração atual os componentes cujo o nome se encontra na lista l recebida.
     * @param l
     * @throws java.lang.Exception
     */
    public void removerListaComponentes(List<String> l) throws Exception{
        for(String d : l){
            Componente c = this.componentes.get(d);
            this.confAtual.retiraComponente(c);
        }
    }
    
    
    /** 
     * Retira da configuração atual os pacotes cujo o nome se encontra na lista l recebida.
     * @param l
     * @throws java.lang.Exception
     */
    public void removerListaPacotes(List<String> l) throws Exception{
        for(String d : l){
            Pacote p = this.pacotes.get(d);
            this.confAtual.removePacote(p);
        }
    }
    
    
    /** 
     * Recebe e adiciona à configuração atual os componentes cujo o nome se encontra na lista recebida.
     * @param l
     * @throws java.lang.Exception
     */
    public void adiconarComponentes(List<String> l) throws Exception{
        List<Componente> comps = new ArrayList<>();
        for(String s : l){
            Componente c = this.componentes.get(s);
            this.confAtual.addComponente(c);
        }
    }
    
    /** 
     * Retorna um map com o nome e respetivo preço dos componentes de um determinado tipo.
     * @param tipo
     * @return 
     * @throws java.lang.Exception 
     */
    public Map<String,Double> getComponentesdoTipo(String tipo) throws Exception{
        Map<String,Double> resultado = new HashMap<>();
        List<Componente> comps = componentes.getComponentesFromTipo(tipo);
        for(Componente c : comps){
            resultado.put(c.getNome(),c.getPreco());
        }
        return resultado;
    }
    
    /** 
     * Retorna um map com o nome dos componentes e respetivo custo reduzido associado dado o nome de uma pacote.
     * @param p
     * @return 
     * @throws java.lang.Exception 
     */
    public Map<String,Double> getCompfromPack(String p) throws Exception{
        return getPacote(p);
    }    
    
    /** 
     * Calcula e retorna o preço da configuração atual.
     * @return
     * @throws java.lang.Exception
     */
    public double getPrecoConf() throws Exception{
        double r = 0;
        for(Componente c : confAtual.getComponentes()){
            r+=c.getPreco();
        }
        for(Pacote p : confAtual.getPacotes()){
            Map<String,Double> m = p.getPrecosReduzidos();
            for(double v : m.values()){
                r+=v;
            }
        }
        r+=confAtual.getModelo().getPreco();
        return r;
    }
    
    /** 
     * Verifica se já foram selecionados 4 tipos que tem de estar presentes numa configuração (motor, pneu, jantes e pintura) para se puder avançar para a fase de confirmação da configuração.
     * @return
     * @throws java.lang.Exception
     */
    public boolean podefinalConfig() throws Exception{
        List<Componente> compsAtuais = new ArrayList<>(confAtual.getComponentes());
        List<Pacote> packsAtuais = new ArrayList<>(confAtual.getPacotes());
        for(Pacote p : packsAtuais){
            List<Componente> l = componentes.getComponentesFromPack(p.getNome());
            compsAtuais.addAll(l);
        }
        List<String> tipos = new ArrayList<>();
        for(Componente c : compsAtuais){
            String tipo = c.getClass().getSimpleName();
            tipos.add(tipo);
        }

        return (tipos.contains("Motor") && tipos.contains("Pneu") 
                            && tipos.contains("Jantes") && tipos.contains("Pintura"));
    }
    
    /** 
     * Devolve a lista dos nomes de todos os pacotes do sistema.
     * @return 
     * @throws java.lang.Exception 
     */    
    public Set<String> getPacotes() throws Exception{
        return pacotes.keySet();
    }
    
    /**
     * Dado um nome de um pacote, retorna um map com o nome de cada componente do pacote e seu preço reduzido.
     * @param codPac
     * @return 
     * @throws java.lang.Exception 
     */
    private Map<String,Double> getPacote(String codPac) throws Exception{
        Map<String,Double> res = new HashMap<>();
        Pacote pacote = pacotes.get(codPac);
        res = pacote.getPrecosReduzidos();
        return res;
    }
        
    /** 
     * Dado um nome de um pacote, adiciona esse pacote à configuração atual e os componentes obrigatórios associados aos componentes desse pacote e retira os incompativeis (caso um componente de um pacote esteja já nos componentes escolhidos lança uma exceção).
     * @param pac
     * @param obg
     * @param inc
     * @throws java.lang.Exception
     */
    public void adicionaPacote(String pac, Set<String> obg, Set<String> inc) throws Exception{
        Boolean teste = true;
        List<Componente> comps = componentes.getComponentesFromPack(pac);
        for(Componente c1 : comps){
                if(verificaPresenca(c1)) teste = false;
        }
        if(teste){
            confAtual.addPacote(pacotes.get(pac));
            for(String c : inc){
                confAtual.retiraComponente(componentes.get(c));
            }
            for(String s : obg){
                 confAtual.addComponente(componentes.get(s));
            }
            List<Pacote> pacretirar = pacoteIncompativeis(pac);
            for(Pacote p : pacretirar){
                confAtual.removePacote(p);
            }
        }
        else throw new Exception("Já existe um ou mais componentes pertencentes a esse pacote que estão na sua configuração!");
        
    }
    
    /** 
     * Retorna uma lista com os nomes dos componentes da configuração atual.
     * @return
     */
    public List<String> getComponentesConfAtual(){
        List<Componente> l = confAtual.getComponentes();
        List<String> resultado = new ArrayList<>();
        for(Componente c : l){
            resultado.add(c.getNome());
        }
        return resultado;
    }
    
    /** 
     * Retorna uma lista com os nomes dos pacotes da configuração atual.
     * @return
     */
    public List<String> getPacotesConfAtual(){
         List<Pacote> l = confAtual.getPacotes();
        List<String> resultado = new ArrayList<>();
        for(Pacote c : l){
            resultado.add(c.getNome());
        }
        return resultado;
    }
    
    /** 
     * Dado um nome de um pacote, retorna a lista de componentes obrigatórios associados aos componentes desse mesmo pacote.
     * @param pacote
     * @return 
     * @throws java.lang.Exception 
     */

    private List<String> compObrigatorios(String pacote) throws Exception{
        List<Componente> compsp = componentes.getComponentesFromPack(pacote);
        Set<String> ret = new TreeSet<>();
        
        for(Componente c : compsp){
            List<Componente> l = componentes.getNecessarios(c.getNome());
            for(Componente cl : l){
                ret.add(cl.getNome());
            }
        }
        List<Componente> compsa = confAtual.getComponentes();
        for(Componente c : compsa){
            ret.remove(c.getNome());
        }
        return new ArrayList<>(ret);
    }    
    
    /** 
     * Se na configuração atual houver um pacote que contém a totalidade de certos componentes escolhidos, substitui esses componentes por esse pacote.
     * @throws java.lang.Exception
     */
    public void verificaConfAtual() throws Exception{
        Set<String> pacs = pacotes.keySet();
        List<Componente> compConf = confAtual.getComponentes();
        for(String p : pacs){
            List<Componente> comps = componentes.getComponentesFromPack(p);
            if(compConf.containsAll(comps)){
                for(Componente c : comps){
                    confAtual.retiraComponente(c);
                }
                confAtual.addPacote(pacotes.get(p));
            }
        }
    }
    
    /** 
     * Dado o nome de um pacote, faz a remoção desse pacote à configuração atual, bem como a remoção dos componentes e dos pacotes que essa remoção implica.
     * @param p
     * @throws java.lang.Exception
     */
    public void removePacote(String p) throws Exception{
        List<Componente> compretirar = pacote_Comprem(p); // lista de componentes a retirar
        List<Pacote> pacoteretirar = pacote_Pacrem(p); // lista de pacotes a retirar
        for(Componente c : compretirar){
            confAtual.retiraComponente(c);
        }
        for(Pacote pack : pacoteretirar){
            confAtual.removePacote(pack);
        }
        confAtual.removePacote(pacotes.get(p));
    }
    
    /** 
     * Dado o nome de um pacote que se quer remover, devolva a lista dos componentes que essa remoção implicará.
     * @param p
     * @return 
     * @throws java.lang.Exception 
     */
    private List<Componente> pacote_Comprem(String p) throws Exception{
        List<Componente> comps = componentes.getComponentesFromPack(p);
        List<Componente> compretirar = new ArrayList<>();
        for(Componente c : comps){
            List<Componente> com = CompaRemover(c.getNome());
            for(Componente c2 : com){
                compretirar.add(c2);
            }
        }
        return compretirar;
    } 
    
    /** 
     * Dado o nome de um pacote que se quer remover, devolva a lista dos nomes dos componentes que essa remoção implicará.
     * @param p
     * @return
     * @throws java.lang.Exception
     */
    public List<String> pacote_CompremString(String p) throws Exception{
        List<Componente> comps = componentes.getComponentesFromPack(p);
        List<String> compretirar = new ArrayList<>();
        for(Componente c : comps){
            List<Componente> com = CompaRemover(c.getNome());
            for(Componente c2 : com){
                compretirar.add(c2.getNome());
            }
        }
        return compretirar;
    } 
    
    /** 
     * Dado o nome de um pacote que se quer remover, devolva a lista dos pacotes que essa remoção implicará.
     * @param p
     * @return
     * @throws java.lang.Exception
     */
    private List<Pacote> pacote_Pacrem(String p) throws Exception{
        Set<Pacote> resultado = new TreeSet<>();
        List<Componente> comps = componentes.getComponentesFromPack(p);
        for(Componente c : comps){
            List<Pacote> l = PacoteaRemover(c.getNome());
            for(Pacote pa : l){
                resultado.add(pa);
            }
        }
        return new ArrayList<>(resultado);
       
    }
    
    /** 
     * Dado o nome de um pacote que se quer remover, devolva a lista dos nomes dos pacotes que essa remoção implicará.
     * @param p
     * @return
     * @throws java.lang.Exception
     */
    public List<String> pacote_PacremString(String p) throws Exception{
        Set<String> resultado = new TreeSet<>();
        List<Componente> comps = componentes.getComponentesFromPack(p);
        for(Componente c : comps){
            List<Pacote> l = PacoteaRemover(c.getNome());
            for(Pacote pa : l){
                resultado.add(pa.getNome());
            }
        }
        return new ArrayList<>(resultado);
       
    }
    
    /** 
     * Dado o nome de um pacote, devolve a lista dos pacotes incompatíveis com esse pacote.
     * @param pac
     * @return
     */
    private List<Pacote> pacoteIncompativeis(String pac){
        List<Pacote> retirar = new ArrayList<>();
        try{
            List<Pacote> selecionados = confAtual.getPacotes();
            Pacote adicionar = pacotes.get(pac);
            for(Pacote p : selecionados){
                if(pacoteIncompativel(adicionar, p)) retirar.add(p);
            }
        }
        catch(Exception e){;}
        return retirar;
    }
    
    
    /** 
     * Dado o nome de um componente, verifica se esse componente não pertence à configuração atual.
     * @param comp
     * @return
     * @throws java.lang.Exception
     */
    public boolean verificaPresenca(String comp) throws Exception{
        List<Componente> compsatuais = new ArrayList<>(confAtual.getComponentes());
        List<Pacote> packs = confAtual.getPacotes();
        Componente c = componentes.get(comp);
        for(Pacote pack : packs){
            compsatuais.addAll(componentes.getComponentesFromPack(pack.getNome()));
        }
        for(Componente compo : compsatuais){
            if(compo.equals(c)) return true;
        }
        return false;
    }
    
    /** 
     * Dado um componente, verifica se esse componente não pertence à configuração atual.
     * @param c
     * @return
     * @throws java.lang.Exception
     */
    private boolean verificaPresenca(Componente c) throws Exception{
        List<Componente> compsatuais = new ArrayList<>(confAtual.getComponentes());
        List<Pacote> packs = confAtual.getPacotes();
        for(Pacote pack : packs){
            compsatuais.addAll(componentes.getComponentesFromPack(pack.getNome()));
        }
        for(Componente compo : compsatuais){
            if(compo.equals(c)) return true;
        }
        return false;
    }
        
    /** 
     * Dado o nome de um componente que se quer remover, devolve a lista dos componentes que essa remoção implica.
     * @param c
     * @return
     * @throws java.lang.Exception
     */
    private List<Componente> CompaRemover(String c) throws Exception{
        List<Componente> l = confAtual.getComponentes();
        List<Componente> resultado = new ArrayList<>();
        for(Componente comp : l){
            List<Componente> obrigatorios = componentes.getNecessarios(comp.getNome());
            for(Componente comp2 : obrigatorios){
                if(c.equals(comp2.getNome())) resultado.add(comp);
            }
        }
        return resultado;
    }
    
    /** 
     * Dado o nome de um componente que se quer remover, devolve a lista dos nomes dos componentes (para remover) que a remoção implica.
     * @param c
     * @return
     * @throws java.lang.Exception
     */
    public List<String> CompaRemoverString(String c) throws Exception{
        List<Componente> l = confAtual.getComponentes();
        List<String> resultado = new ArrayList<>();
        for(Componente comp : l){
            List<Componente> obrigatorios = componentes.getNecessarios(comp.getNome());
            for(Componente comp2 : obrigatorios){
                if(c.equals(comp2.getNome())) resultado.add(comp.getNome());
            }
        }
        return resultado;
    }
    
    /** 
     * Dado o nome de um componente que se quer remover, devolve a lista dos pacotes a remover, resultante da remoção do tal componente.
     * @param c
     * @return
     * @throws java.lang.Exception
     */
    private List<Pacote> PacoteaRemover(String c) throws Exception{
        List<Pacote> l = confAtual.getPacotes();
        Set<Pacote> resultado = new TreeSet<>();
        for(Pacote p : l){
            List<Componente> comps = componentes.getComponentesFromPack(p.getNome());
            for(Componente comp1 : comps){
                List<Componente> obrigatorios = componentes.getNecessarios(comp1.getNome());
                for(Componente comp2 : obrigatorios){
                    if(c.equals(comp2.getNome())) resultado.add(p);
                }
            }
        }
        return new ArrayList<>(resultado);
    }
    
    /** 
     * Dado o nome de um componente que se quer remover, devolve a lista dos nomes dos pacotes a remover, resultante da remoção do tal componente.
     * @param c
     * @return
     * @throws java.lang.Exception
     */
    public List<String> PacoteaRemoverString(String c) throws Exception{
        List<Pacote> l = confAtual.getPacotes();
        Set<String> resultado = new TreeSet<>();
        for(Pacote p : l){
            List<Componente> comps = componentes.getComponentesFromPack(p.getNome());
            for(Componente comp1 : comps){
                List<Componente> obrigatorios = componentes.getNecessarios(comp1.getNome());
                for(Componente comp2 : obrigatorios){
                    if(c.equals(comp2.getNome())) resultado.add(p.getNome());
                }
            }
        }
        return new ArrayList<>(resultado);
    }
    
    
    /** 
     * Retira um componente com determinado nome.
     * @param c
     * @throws java.lang.Exception
     */
    public void removeComponente(String c) throws Exception{
        confAtual.retiraComponente(componentes.get(c));
    }
    
    /** 
     * Verifica se dois pacotes são incompatíveis.
     * @param p_adicionar
     * @param p_escolhido
     * @return 
     */
    private boolean pacoteIncompativel(Pacote p_adicionar, Pacote p_escolhido){
        boolean r = false;
        try{
            List<Componente> comps1 = componentes.getComponentesFromPack(p_adicionar.getNome());
            List<Componente> comps2 = componentes.getComponentesFromPack(p_escolhido.getNome());
            List<Componente> inc1 = new ArrayList<>();
            for(Componente c : comps1){
                List<Componente> aux = componentes.getIncompativeis(c.getNome());
                for(Componente incompativel : aux){
                    inc1.add(incompativel);
                }
            }
            for(Componente c1 : inc1){
                for(Componente c2 : comps2){
                    if(c1.equals(c2)) return true;
                }
            }
            }
        catch(Exception e){;}
        return r;
    }
    
    /** 
     * Dada uma lista dos nomes de componentes, retorna a lista dos componentes incompatíveis presentes na configuração atual desses componentes.
     * @param compAdicionar
     * @return 
     */
    private Map<String,Double> compIncompativeis(List<String> compAdicionar) throws Exception{
        Map<String,Double> compPres = confAtual.getComponentesPreco();
        Map<String,Double> compretirar = new HashMap<>();
        for(String s : compAdicionar){
            List<Componente> aux = componentes.getIncompativeis(s);
            for(Map.Entry<String,Double> c1 : compPres.entrySet()){
                for(Componente c2 : aux){
                    if(c1.getKey().equals(c2.getNome())) compretirar.put(c1.getKey(),c1.getValue());
                }
            }
        }
        
        return compretirar;
    }
    
    /** 
     * Dado o nome de um pacote, devolve um map com o nome do componente e respetivo custo que se tem que adicionar à configuração atual.
     * @param pacote
     * @return
     * @throws java.lang.Exception
     */
    public Map<String,Double> compObrigatoriosM(String pacote) throws Exception{
        
        List<Componente> compsp = componentes.getComponentesFromPack(pacote);
        componentesObrigatorios(compsp,new ArrayList<>(compsp));
        Map<String,Double> ret = new HashMap<>();
        
        for(Componente c : compsp){
                ret.put(c.getNome(),c.getPreco());
        }
        List<Componente> compsa = confAtual.getComponentes();
        for(Componente c : compsa){
            ret.remove(c.getNome());
        }
        for(Componente c : componentes.getComponentesFromPack(pacote)){
            ret.remove(c.getNome());
        }
        return ret;
    }
    
    /** 
     * Dado um nome de um pacote, devolve um map com o nome dos componentes e respetivo preço que têm incompatibilidade com esse pacote.
     * @param pac
     * @return 
     * @throws java.lang.Exception
     */
    public Map<String,Double> verificarIncompatibilidade(String pac) throws Exception{
        List<String> obrigatorios = new ArrayList<>(compObrigatoriosM(pac).keySet());
        obrigatorios.addAll(componentes.getComponentesFromPack(pac)
                                                    .stream()
                                                    .map(p->p.getNome()).collect(Collectors.toList()));
        Map<String,Double> compretirar = compIncompativeis(obrigatorios);
        
        return compretirar;
    }
    
     /** 
     * Adiciona os dados do cliente à configuração atual.
     * @param nome
     * @param email
     * @param morada
     * @param tele
     * @param nif
     */
    public void addDadosConfig(String nome, String email, String morada, String tele, String nif){
        confAtual.setNome(nome);
        confAtual.setEmail(email);
        confAtual.setMorada(morada);
        confAtual.setTelefone(tele);
        confAtual.setNif(Integer.parseInt(nif));
    }
    
     /** 
     * Adiciona a configuração atual à lista de espera.
     * @throws java.lang.Exception
     */
    public void addConfToLEsp() throws Exception{
        listaEspera.put(confAtual);
    }
    
     /** 
     * Devolve a lista de espera com os id's das configurações.
     * @return 
     * @throws java.lang.Exception
     */
    public List<Integer> getListaEspera() throws Exception{
        return listaEspera.getListaEspera();
    }
    
     /** 
     * Devolve a informação de uma configuração com um determinado id que se encontra na lista de espera.
     * @param id
     * @return 
     * @throws java.lang.Exception
     */
    public List<String> getConfig(int id) throws Exception{
         return listaEspera.get(id).infConf();
    }
    
     /** 
     * Verifica se os componentes de uma dada lista têm todos stock suficiente.
     * @param l
     * @return 
     */
    private boolean stockSuficiente(List<Componente> l){
         return l.stream().noneMatch((c) -> (c.getStock() == 0));
    }
    
     /** 
     * Devolve o id da configuração que começou a ser fabricada (retira da lista de espera essa configuração e respetivo stock usado). Se não houver nenhuma configuração que possa ser fabricada, lança uma exceção.
     * @return 
     * @throws java.lang.Exception 
     */
    public int FabricaProx() throws Exception{
        List<Integer> lespera = listaEspera.getListaEspera();
        int fabricar = -1;
        for(int i : lespera){
            if(fabricar == -1){
                List<Componente> l = listaEspera.get(i).getComponenteaGastar();
                if(stockSuficiente(l)) fabricar = i;
            }
            else break;
        }
        if(fabricar == -1) throw new Exception("Não é possível fabricar nenhuma configuração de momento");
        else{
            for(Componente c : listaEspera.get(fabricar).getComponenteaGastar()){
                componentes.updateStock(c.getNome(), c.getStock()-1);
            }
            listaEspera.remove(fabricar);
            return fabricar;
        }
    }
    
     /** 
     * Devolve um map com o nome do componente e respetivo stock de todos os componentes.
     * @return 
     * @throws java.lang.Exception 
     */
    public Map<String,Integer> getListaStock() throws Exception{
         List<Componente> lc = this.componentes.values();
         Map<String,Integer> resultado = new HashMap<>();
         for(Componente c : lc){
             resultado.put(c.getNome(), c.getStock());
         }
         return resultado;
    }
    
     /** 
     * Dado o nome de um componente e o stock que se pretende adicionar, adiciona-se esse stock ao stock do componente.
     * @param c
     * @param stockadicionar
     * @throws java.lang.Exception
     */
    public void atualizaStock(String c, int stockadicionar) throws Exception{
       int stockexistente = componentes.get(c).getStock();
       componentes.updateStock(c, stockexistente + stockadicionar);
    }
    
     /** 
     * Calcula o preço da configuração atual.
     * @return
     * @throws java.lang.Exception
     */
    public double getCustoConfAtual() throws Exception{
        return confAtual.getCusto();
    }
    
     /** 
     * Verifica se uma determinada quantia, é suficiente para realizar uma configuração ótima.
     * @param quantia
     * @return
     * @throws java.lang.Exception
     */
    public boolean isValidValue(double quantia) throws Exception{
        List<Componente> l1 = new ArrayList<>(confAtual.getComponentes());
        List<Componente> l2 = new ArrayList<>(l1);
        
        componentesObrigatorios(l1,l2);
        double total = confAtual.getModelo().getPreco();
        for(Componente c : l1){
            total+=c.getPreco();
        }
        if(total > quantia) return false;
        return true; //Verificar o preço que irá custar ainda com os necessarios, ou se calhar adicionar antes os necessarios
    }
    
    /** 
     * Dada uma quantia, devolve um map com o nome e preço dos componentes da melhor configuração ótima.
     * @param quantia
     * @return
     * @throws java.lang.Exception
     */
    public Map<String,Double> getBestConf(double quantia) throws Exception{
        //IMPLEMENTAR
        List<Componente> l = new ArrayList<>(confAtual.getComponentes());
        confAtual.apagaComponentes();
        componentesObrigatorios(l,new ArrayList<>(l));
        l.forEach((c) -> confAtual.addComponente(c));
        return confAtual.getComponentesPreco();
    }
    
     /** 
     * Retorna o nome do modelo da configuração atual.
     * @return
     */
    public String getModeloAtual() {
        return this.confAtual.getModelo().getNome();
    }
    
     /** 
     * Recursivamente calcula os componentes obrigatórios a adicionar.
     * @param l1
     * @param l2
     */
    private void componentesObrigatorios(List<Componente> l1, List<Componente> l2)throws Exception{
        List<Componente> l3 = new ArrayList<>();
        for(Componente c : l2){
            for(Componente n : componentes.getNecessarios(c.getNome())){
                if(!l1.contains(n)){
                    l1.add(n);
                    l3.add(n);
                }
            }
        }
        if(!l3.isEmpty()) componentesObrigatorios(l1,l3);
    }
    
     /** 
     * Verifica se a configuração atual já tem modelo associado.
     * @return
     */
    public boolean isModelSelected(){
        return confAtual.getModelo() != null;
    }
    
     /** 
     * Apaga os componentes e pacotes da configuração atual.
     */
    public void apagaComponentes(){
        confAtual.apagaComponentes();
    }

     /** 
     * Verifica se a configuração atual tem os componentes básicos.
     * @return
     */
    public boolean temComponentesBasicos() {
        boolean motor = false;
        boolean pintura = false;
        for(Componente c : confAtual.getComponentes()){
            if(c instanceof Motor){
                motor = true;
            }
            if(c instanceof Pintura){
                pintura = true;
            }
        }
        return pintura && motor;
    }
    
     /** 
     * Retorna um map com o nome do modelo e respetivo preço de todos os modelos disponíveis.
     * @return
     */
    public Map<String,Double> getModelos(){
        Map<String,Double> ret = new HashMap<>();
        try{
            this.modelos.values().forEach((m) -> ret.put(m.getNome(),m.getPreco()));
        }catch(Exception e){}
        return ret;
    }
    
     /** 
     * Dado o nome de um modelo, associa esse modelo à configuração atual.
     * @param s
     * @throws java.lang.Exception
     */
    public void associarModeloSimples(String s) throws Exception{
        this.confAtual.setModelo(this.modelos.get(s));
    }
    
     /** 
     * Devolve um map com o nome do componente e respetivo preço dos componentes de um determinado tipo.
     * @param tipo
     * @return
     */
    public Map<String,Double> getComponentesdoTipoM(String tipo) throws Exception{
        Map<String,Double> resultado = new HashMap<>();
        List<Componente> comps = componentes.getComponentesFromTipo(tipo);
        for(Componente c : comps){
            resultado.put(c.getNome(),c.getPreco());
        }
        return resultado;
    }
    
     /** 
     * Remove os componentes de um determinado tipo da configuração atual.
     * @param tipo
     */
    public void retiraTipo(String tipo){
        this.confAtual.retiraTipo(tipo);
    }
    
     /** 
     * Dado o nome de um componente, adiciona esse componente à configuração atual, retirando o componente do mesmo tipo que ele.
     * @param comp
     */
    public void adicionaComponente(String comp)throws Exception{
        this.confAtual.substituiComp(componentes.get(comp));
    }
}

