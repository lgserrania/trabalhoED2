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

class trieNo {

    boolean fimPalavra;
    char caracter;
    trieNo filho;
    trieNo irmao;
    
    public trieNo() {
        fimPalavra = false;
        caracter = ' ';
        filho = null;
        irmao = null;
    }

    public trieNo(char caracter) {
        this();
        this.caracter = caracter;
    }

 
}
