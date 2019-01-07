/*-------------------------------------------------------------
 *          UNIFAL – Universidade Federal de Alfenas.
 *             BACHARELADO EM CIENCIA DA COMPUTACAO.
 * Atividade.: Implementação da arvore TRIE
 * Disciplina: Estrutura de Dados II
 * Professor.: Luiz Eduardo da Silva
 * Aluno.....: Fulano da Silva
 * Data......: 99/99/9999
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
        trieNo ultimo = null;
        if(raiz.filho == null){
            trieNo filho = new trieNo();
            filho.caracter = palavra.charAt(i);
            i++;
            raiz.filho = filho;
            ultimo = filho;
        }else{
            ultimo = raiz;
        }  
        for(; i < palavra.length(); i++){
            trieNo atual = ultimo.filho;
            boolean achou = false;
            while(atual != null){
                if(atual.caracter == palavra.charAt(i)){
                    achou = true;
                    ultimo = atual;
                    break;
                }else{
                    atual = atual.irmao;
                }
            }
            if(!achou){
                atual = ultimo.filho;
                trieNo novo = new trieNo();
                novo.caracter = palavra.charAt(i);
                if(atual == null){
                    ultimo.filho = novo;
                    ultimo = ultimo.filho;
                }else if(atual.caracter > palavra.charAt(i)){
                    novo.irmao = atual;
                    ultimo.filho = novo;
                    ultimo = ultimo.filho;
                }else{
                    boolean inseriu = false;
                    while(atual.irmao != null){
                        if(atual.irmao.caracter > palavra.charAt(i)){
                            novo.irmao = atual.irmao;
                            atual.irmao = novo;
                            ultimo = atual.irmao;
                            inseriu = true;
                            break;
                        }else{
                            atual = atual.irmao;
                        }
                    }
                    if(!inseriu){
                        atual.irmao = novo;
                        ultimo = atual.irmao;
                    }
                }
            }
        }
        ultimo.fimPalavra = true;
    }

    public List<String> obtemPalavras(String prefixo) {
        if(raiz.filho == null){
            System.out.println("Dicionário vazio");
        }
        trieNo pai = raiz;
        for(int i = 0; i < prefixo.length(); i++){       
           trieNo aux = pai.filho;
           boolean achou = false;
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
           
           if(!achou){
               System.out.println("Prefixo não existe");
               return null;
           }                  
        }
        this.listaPalavras = new ArrayList();
        if(pai.fimPalavra){
            //System.out.println(prefixo);      
            this.listaPalavras.add(prefixo);
        }
        
        percorreNos(pai.filho, prefixo);
        
        this.noprefixo = pai;
        this.prefixo = prefixo;
        
        return this.listaPalavras;
    }
    
    public String percorreNos(trieNo inicio, String palavra){
        palavra += inicio.caracter;
        if(inicio.fimPalavra){
            //System.out.println(palavra);
            this.listaPalavras.add(palavra);
        }
        if(inicio.filho != null){
            palavra = percorreNos(inicio.filho, palavra);
        }
        palavra = palavra.substring(0, palavra.length() - 1);
        if(inicio.irmao != null){           
            palavra = percorreNos(inicio.irmao, palavra);
        }
        return palavra;
    }

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

    public void geraDot1(String nomeArquivo) throws IOException {
        ArrayList<trieNo> nos = new ArrayList();
        FileWriter arq = new FileWriter(nomeArquivo);
        PrintWriter gravarArq = new PrintWriter(arq);
        gravarArq.printf("digraph G {\n");
        gravarArq.printf("\tn" + raiz.hashCode() + " [shape=circle label=\" \"];\n");
        trieNo pai = raiz;
        trieNo filho = raiz.filho;
        String listanos = "";
        String prefixo = this.prefixo;
        ArrayList<String> ligacoes = new ArrayList();
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
        
        if(noprefixo.filho != null){
            gravarArq.printf("\tn" + noprefixo.filho.hashCode() + "[shape=circle label=\" \"];\n");
            descobreNos1(gravarArq, noprefixo.filho);
        }
        
        for(int i = 0; i < ligacoes.size(); i++){
            gravarArq.printf(ligacoes.get(i));
        }
        nos.add(noprefixo);

        while(nos.size() > 0){
            ligaNos1(gravarArq, nos);
        }
        
        gravarArq.printf("}");
        gravarArq.close();
    }
    
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

    public void geraDot2(String nomeArquivo) throws IOException {
        if(noprefixo == null) noprefixo = raiz;
        FileWriter arq = new FileWriter(nomeArquivo);
        PrintWriter gravarArq = new PrintWriter(arq);
        gravarArq.printf("digraph G {\n");
        gravarArq.printf("node [shape=record, height=.1];\n");
        gravarArq.printf("\tn" + raiz.hashCode() + "  [label=\"<filho> RAIZ\"];\n");
        trieNo pai = raiz;
        trieNo filho = raiz.filho;
        String listanos = "";
        ArrayList<String> ligacoes = new ArrayList();
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
        while(nos.size() > 0){
            ligaNos2(gravarArq, nos, listaligacao, listarank);
        }
        for(int i = 0; i < listarank.size(); i++){
            gravarArq.printf(listarank.get(i));
        }
        for(int i = 0; i < ligacoes.size(); i++){
            gravarArq.printf(ligacoes.get(i));
        }
        for(int i = 0; i < listaligacao.size(); i++){
            gravarArq.printf(listaligacao.get(i));
        }
        gravarArq.printf("}");
        gravarArq.close();
        
    }
    
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
