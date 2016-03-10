package es.ucm.fdi.tp.practica4.ataxx;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.Utils;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * A random player for ConnectN.
 * 
 * <p>
 * Un jugador aleatorio para ConnectN.
 *
 */
public class AtaxxRandomPlayer extends Player {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public GameMove requestMove(Piece p, Board board, List<Piece> pieces, GameRules rules) {
		if (board.isFull()) {
			throw new GameError("The board is full, cannot make a random move!!");
		}
		/*
		int rows = board.getRows();
		int cols = board.getCols();

		// pick an initial random position
		int currRow = Utils.randomInt(rows);
		int currCol = Utils.randomInt(cols);

		// start at (currRow,currCol) and look for the first empty position.
		while (true) {
			if (board.getPosition(currRow, currCol) != null) {
				//hecho para que en el caso 4, sea +2, y en el caso 0, sea -2
				int nextRow = Utils.randomInt(4) + currRow - 2;
				int nextCol = Utils.randomInt(4) + currCol - 2;
				//La ficha tiene que moverse
				while (nextRow == nextCol && nextRow == 0){
					nextRow = Utils.randomInt(4) + currRow - 2;
					nextCol = Utils.randomInt(4) + currCol - 2;
				}
				//Mirar si hay proteccion contra salirse del tablero
				return createMove(currRow, currCol, nextRow, nextCol, p);
			}
			currCol = (currCol + 1) % cols;
			if (currCol == 0) {
				currRow = (currRow + 1) % rows;
			}
		}
		*/
		List<GameMove> moves = rules.validMoves(board, pieces, p);
		return moves.get(Utils.randomInt(moves.size() - 1));
		
	}

	/**
	 * Creates the actual move to be returned by the player. Separating this
	 * method from {@link #requestMove(Piece, Board, List, GameRules)} allows us
	 * to reuse it for other similar games by overriding this method.
	 * 
	 * <p>
	 * Crea el movimiento concreto que sera devuelto por el jugador. Se separa
	 * este metodo de {@link #requestMove(Piece, Board, List, GameRules)} para
	 * permitir la reutilizacion de esta clase en otros juegos similares,
	 * sobrescribiendo este metodo.
	 * 
	 * @param rowOrigin
	 *            rowOrigin number.
	 * @param colOrigin
	 *            colOriginumn number..
	 * @param p
	 *            Piece to place at ({@code rowOrigin},{@code colOrigin}).
	 * @return
	 */
	protected GameMove createMove(int rowOrigin, int colOrigin, int rowDest, int colDest, Piece p) {
		return new AtaxxMove(rowOrigin, colOrigin, rowDest, colDest, p);
	}

}
