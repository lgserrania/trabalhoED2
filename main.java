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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class main {

    public static void main(String[] args) throws UnsupportedEncodingException, IOException {
        Scanner entrada = new Scanner(System.in, "ISO-8859-1");
        trie t = new trie();
        t.carregaDicionario("portuguese-brazil.txt");
        if(t.contemPalavra("amorsdasf")){
            System.out.println("Contem");
        }else{
            System.out.println("Não contém");
        }

    }

}
