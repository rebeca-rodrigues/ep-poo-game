package src;
//adicionei importações necessárias 


import src.classes.Game;
 
/***********************************************************************/
/* Para jogar:                                                         */
/*                                                                     */
/*    - cima, baixo, esquerda, direita: movimentação do player.        */
/*    - control: disparo de projéteis.                                 */
/*    - ESC: para sair do jogo.                                        */
/*                                                                     */
/***********************************************************************/

// Esta classe inicia o jogo e contém o loop principal de execução
public class Main {

    public static void main(String[] args) { // Inicia o jogo
        Game game = new Game(); 
        game.run();
    }
}


