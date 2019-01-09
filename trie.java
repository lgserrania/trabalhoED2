/*-------------------------------------------------------------
 *          UNIFAL – Universidade Federal de Alfenas.
 *             BACHARELADO EM CIENCIA DA COMPUTACAO.
 * Atividade.: Implementação da arvore TRIE
 * Disciplina: Estrutura de Dados II
 * Professor.: Luiz Eduardo da Silva
 * Aluno.....: Luis Gustavo de Souza Carvalho
 * Data......: 08/01/2019
 *-------------------------------------------------------------*/
package trie.trabalho;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class trie  implements treeTrie {

    public trie() {
        raiz = new trieNo();
    }

    public void adicionaPalavra(String palavra) {
        int i = 0;
        trieNo ultimo = null; //Variável que irá apontar para o último nó encontrado ou criado
        //Se a raiz não tiver filho, apenas cria um nó novo e adiciona nela
        if(raiz.filho == null){
            trieNo filho = new trieNo();
            filho.caracter = palavra.charAt(i);
            i++;
            raiz.filho = filho;
            ultimo = filho;
        }else{
            //Senão, ultimo recebe raiz
            ultimo = raiz;
        }  
        for(; i < palavra.length(); i++){
            //Atual irá percorrer os filhos do último para procurar
            //O valor da letra, ou criar um nó novo
            trieNo atual = ultimo.filho;
            trieNo anterior = null;
            boolean achou = false;
            //Enquanto o atual for diferente de nulo 
            //e a letra do atual for menor ou igual a letra da palavra
            //Ele irá percorrendo pelos irmãos
            //Caso ache a letra igual, ele sai do laço
            while(atual != null && atual.caracter <= palavra.charAt(i)){
                if(atual.caracter == palavra.charAt(i)){
                    achou = true;
                    ultimo = atual;
                    break;
                }else{
                    anterior = atual;
                    atual = atual.irmao;
                }
            }     
            //Caso não tenha achado a letra, existem dois casos
            //O primeiro é inserir no começo, o segundo é inserir no resto
            //Caso o anterior seja nulo, significa que é no começo
            //Do contrário, é no meio ou fim
            if(!achou){
                trieNo novo = new trieNo();
                if(anterior != null){      
                    novo.caracter = palavra.charAt(i);
                    novo.irmao = anterior.irmao;
                    anterior.irmao = novo;
                    ultimo = novo;
                }else{
                    novo.caracter = palavra.charAt(i);
                    novo.irmao = atual;
                    ultimo.filho = novo;
                    ultimo = novo;
                }
            }
        }
        //Ao sair da repetição, o último nó criado é declarado como fim de palavra
        ultimo.fimPalavra = true;
    }

    public List<String> obtemPalavras(String prefixo) {
        //Caso a raiz não tenha filho, o dicionário está vazio
        if(raiz.filho == null){
            System.out.println("Dicionário vazio");
        }
        trieNo pai = raiz;
        for(int i = 0; i < prefixo.length(); i++){       
           trieNo aux = pai.filho;
           boolean achou = false;
           //Enquanto aux for diferente de nulo
           //Aux irá percorrer os irmãos até achar a letra
           //Caso o caracter de aux seja maior que o da palavra
           //Já informa que o prefixo não existe e sai da função
           while(aux != null){
               if(aux.caracter == prefixo.charAt(i)){
                   achou = true;
                   pai = aux;                   
                   break;
               }else if(aux.caracter < prefixo.charAt(i)){
                   aux = aux.irmao;
               }else{
                   System.out.println("Prefixo não existe");
                   return null;
               }
           }
           
           //Caso tenha percorrido todos os irmãos e não tenha achado
           //Informa que o prefixo não existe
           if(!achou){
               System.out.println("Prefixo não existe");
               return null;
           }                  
        }
        //a variável listaPalavras é iniciada
        this.listaPalavras = new ArrayList();
        //Caso o último nó do prefixo seja o fim de uma palavra, ele é adicionado na lista
        if(pai.fimPalavra){
            //System.out.println(prefixo);      
            this.listaPalavras.add(prefixo);
        }
        
        //Entra na função que percorre os nós para procurar as palavras derivadas do prefixo
        percorreNos(pai.filho, prefixo);
        
        this.noprefixo = pai;
        this.prefixo = prefixo;
        
        return this.listaPalavras;
    }
    
    public String percorreNos(trieNo inicio, String palavra){
        //palavra inicia com o valor do prefixo e, a medida que a recursão desenrola
        //os caracteres lidos serão adicionados ou retirados conforme necessário
        //No começo, irá adicionar o caracter do nó que esta sendo executado
        palavra += inicio.caracter;
        if(inicio.fimPalavra){
            //Caso esse nó seja um fim de palavra, adiciona na lista
            this.listaPalavras.add(palavra);
        }
        //Se o nó tiver um filho, faz a chamada recursiva pro mesmo
        if(inicio.filho != null){
            palavra = percorreNos(inicio.filho, palavra);
        }
        //Como saiu da chamada do filho, retira o caractere que foi inserido nele
        palavra = palavra.substring(0, palavra.length() - 1);
        //Caso tenha um irmão, faz a chamada recursiva pro mesmo
        if(inicio.irmao != null){           
            palavra = percorreNos(inicio.irmao, palavra);
        }
        return palavra;
    }

    //Executa da mesma forma que a função obtemPalavra
    //Porém aqui ele irá apenas retornar false ou true de acordo com a existência ou não da mesma
    public boolean contemPalavra(String palavra) {
        if(raiz.filho == null) return false;
        trieNo pai = raiz;
        for(int i = 0; i < palavra.length(); i++){       
           trieNo aux = pai.filho;
           boolean achou = false;
           while(aux != null){
               if(aux.caracter == palavra.charAt(i)){
                   achou = true;
                   pai = aux;
                   break;
               }else if(aux.caracter < palavra.charAt(i)){
                   aux = aux.irmao;
               }else{
                   return false;
               }
           }           
           if(!achou){
               return false;
           }        
        }
        if(pai.fimPalavra) return true;
        else return false;
    }

    //Pra cada linha do arquivo, ele chama a função de adicionar palavra
    public void carregaDicionario(String nomeArquivo) {
        BufferedReader lerArq;
        try {
            lerArq = new BufferedReader(new InputStreamReader(new FileInputStream(nomeArquivo), "UTF-8"));
            String linha = lerArq.readLine();
            linha = linha.substring(1);
            while(linha != null){
                this.adicionaPalavra(linha);
                linha = lerArq.readLine();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(trie.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(trie.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(trie.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    //Função chamada para a geração do .dot de circulos
    public void geraDot1(String nomeArquivo) throws IOException {
        //Cria um array de nos
        ArrayList<trieNo> nos = new ArrayList();
        //cria o arquivo
        FileWriter arq = new FileWriter(nomeArquivo);
        PrintWriter gravarArq = new PrintWriter(arq);
        //Escreve o cabeçalho
        gravarArq.printf("digraph G {\n");
        //declara o nó raiz no arquivo
        gravarArq.printf("\tn" + raiz.hashCode() + " [shape=circle label=\" \"];\n");
        trieNo pai = raiz;
        trieNo filho = raiz.filho;
        String listanos = "";
        String prefixo = this.prefixo;
        ArrayList<String> ligacoes = new ArrayList();
        //Percorre a árvore através das letras do prefixo, guardando as declarações e ligações em strings
        for(int i = 0; i < prefixo.length(); i++){
            while(filho.caracter != prefixo.charAt(i)){
                filho = filho.irmao;
            }
            if(filho.fimPalavra){
                gravarArq.printf("\tn" + filho.hashCode() + " [shape=circle label=\"*\"];\n");
            }else{
                gravarArq.printf("\tn" + filho.hashCode() + " [shape=circle label=\" \"];\n");
            }
            ligacoes.add("\tn" + pai.hashCode() + " -> n" + filho.hashCode() + " [label=\"" + filho.caracter + "\"];\n");
            pai = filho;
            filho = filho.filho;
        }       
        
        //Caso o no do fim do prefixo tenha um filho, declara o mesmo
        //e manda pra função que irá descobrir os outros nos
        if(noprefixo.filho != null){
            gravarArq.printf("\tn" + noprefixo.filho.hashCode() + "[shape=circle label=\" \"];\n");
            descobreNos1(gravarArq, noprefixo.filho);
        }
        
        //printa todas as ligações do prefixo
        for(int i = 0; i < ligacoes.size(); i++){
            gravarArq.printf(ligacoes.get(i));
        }
        
        nos.add(noprefixo);

        //Descobre e printa as outras ligações
        while(nos.size() > 0){
            ligaNos1(gravarArq, nos);
        }
        
        gravarArq.printf("}");
        gravarArq.close();
    }
    
    //Percorre a árvore após o prefixo identificando e printando os outros nós existentes
    public void descobreNos1(PrintWriter arquivo, trieNo atual){
        if(atual.filho != null){
            if(atual.filho.fimPalavra){
                arquivo.printf("\tn" + atual.filho.hashCode() + " [shape=circle label=\"*\"];" + "\n");
            }else{
                arquivo.printf("\tn" + atual.filho.hashCode() + " [shape=circle label=\" \"];" + "\n");
            }
            descobreNos1(arquivo,atual.filho);
        }

        if(atual.irmao != null){   
            if(atual.irmao.fimPalavra){
                arquivo.printf("\tn" + atual.irmao.hashCode() + " [shape=circle label=\"*\"];" + "\n");
            }else{
                arquivo.printf("\tn" + atual.irmao.hashCode() + " [shape=circle label=\" \"];" + "\n");
            }
            descobreNos1(arquivo, atual.irmao);
        }
    }
    
    //Percorre a árvore após o prefixo identificando e printando as ligações
    public void ligaNos1(PrintWriter arquivo, ArrayList<trieNo> nos){
        
        trieNo atual = nos.get(0);
        if(atual.filho != null){
            trieNo filho = atual.filho;
            arquivo.printf("\tn" + atual.hashCode() + " -> n" + atual.filho.hashCode() + " [label=\"" + atual.filho.caracter +"\"];\n");
            nos.add(filho);
            while(filho.irmao != null){
                arquivo.printf("\tn" + atual.hashCode() + " -> n" + filho.irmao.hashCode() + " [label=\"" + filho.irmao.caracter +"\"];\n");
                nos.add(filho.irmao);
                filho = filho.irmao;
            }
        }
        nos.remove(atual);

    }

    //Gera o arquivo .dot de registro
    public void geraDot2(String nomeArquivo) throws IOException {
        //if(noprefixo == null) noprefixo = raiz;
        FileWriter arq = new FileWriter(nomeArquivo);
        PrintWriter gravarArq = new PrintWriter(arq);
        //escreve o cabeçalho
        gravarArq.printf("digraph G {\n");
        gravarArq.printf("node [shape=record, height=.1];\n");
        //declara a raiz
        gravarArq.printf("\tn" + raiz.hashCode() + "  [label=\"<filho> RAIZ\"];\n");
        trieNo pai = raiz;
        trieNo filho = raiz.filho;
        String listanos = "";
        ArrayList<String> ligacoes = new ArrayList();
        //Identifica os nos e ligacoes do prefixo
        for(int i = 0; i < prefixo.length(); i++){
            while(filho.caracter != prefixo.charAt(i)){
                filho = filho.irmao;
            }
            String fim = "";
            String temfilho = "";
            String temirmao = "";
            if(filho.fimPalavra) fim = "*";
            if(filho.filho == null) temfilho = "/";
            if(filho.irmao == null) temirmao = "/";
            gravarArq.printf("\tn" + filho.hashCode() + "  [label=\"<fim> " + fim + " |<car> "+filho.caracter+" |<filho>"+temfilho+" |<irmao>"+temirmao+"\"];\n");
            ligacoes.add("\t\"n" + pai.hashCode() + "\":filho -> " + "\"n" + filho.hashCode() + "\":car;\n");
            pai = filho;
            filho = filho.filho;
        }    
        ArrayList<trieNo> nos = new ArrayList();
        nos.add(noprefixo);  
        ArrayList<String> listaligacao = new ArrayList();
        ArrayList<String> listarank = new ArrayList();
        //A função ligaNos2 irá identificar os nos, as ligacoes e as relações de rank
        //e ira salvar tudo em arrays
        //Os nos já serão declarados direto
        while(nos.size() > 0){
            ligaNos2(gravarArq, nos, listaligacao, listarank);
        }
        //Printa os ranks
        for(int i = 0; i < listarank.size(); i++){
            gravarArq.printf(listarank.get(i));
        }
        
        //printa as ligações do prefixo
        for(int i = 0; i < ligacoes.size(); i++){
            gravarArq.printf(ligacoes.get(i));
        }
        //printa as ligações do resto
        for(int i = 0; i < listaligacao.size(); i++){
            gravarArq.printf(listaligacao.get(i));
        }
        gravarArq.printf("}");
        gravarArq.close();
        
    }
    
    //percorre a árvore identificando e salvando em arrays as ligações e as relações de ranks
    //os nos são escritos no arquivo direto
    public void ligaNos2(PrintWriter arquivo, ArrayList<trieNo> nos, ArrayList<String> listaligacao, ArrayList<String> listarank){
        
        trieNo atual = nos.get(0);
        
        if(atual.filho != null){
            trieNo filho = atual.filho;
            nos.add(filho);
            String fim = "";
            String temfilho = "";
            String temirmao = "";
            if(filho.fimPalavra) fim = "*";
            if(filho.filho == null) temfilho = "/";
            if(filho.irmao == null) temirmao = "/";
            arquivo.printf("\tn" + filho.hashCode() + "  [label=\"<fim> " + fim + " |<car> "+filho.caracter+" |<filho>"+temfilho+" |<irmao>"+temirmao+"\"];\n");
            listaligacao.add("\t\"n" + atual.hashCode() + "\":filho -> " + "\"n" + filho.hashCode() + "\":car;\n");
            String rank = "";
            rank += "\t{ rank = same; n" + filho.hashCode() + "; ";
            while(filho.irmao != null){
                nos.add(filho.irmao);
                fim = "";
                temfilho = "";
                temirmao = "";
                if(filho.irmao.fimPalavra) fim = "*";
                if(filho.irmao.filho == null) temfilho = "/";
                if(filho.irmao.irmao == null) temirmao = "/";
                arquivo.printf("\tn" + filho.irmao.hashCode() + "  [label=\"<fim> " + fim + " |<car> "+filho.irmao.caracter+" |<filho>"+temfilho+" |<irmao>"+temirmao+"\"];\n");
                rank += "n" + filho.irmao.hashCode() + "; ";
                listaligacao.add("\t\"n" + filho.hashCode() + "\" -> " + "\"n" + filho.irmao.hashCode() + "\";\n");
                filho = filho.irmao;
            }
            rank += "};\n";
            listarank.add(rank);
        }
        nos.remove(atual);

    }

    private trieNo raiz;
    private List<String> listaPalavras;
    private trieNo noprefixo;
    private String prefixo;
}
