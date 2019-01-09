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
        t.obtemPalavras("carros");
        t.geraDot2("arvore.dot");

    }

}
