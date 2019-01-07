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
