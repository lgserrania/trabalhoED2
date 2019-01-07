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
import java.util.List;

public interface treeTrie {

    public void adicionaPalavra(String palavra);

    public List<String> obtemPalavras(String prefixo);

    public boolean contemPalavra(String palavra);

    public void carregaDicionario(String nomeArquivo);

    public void geraDot1(String nomeArquivo) throws IOException;

    public void geraDot2(String nomeArquivo)throws IOException;

}
