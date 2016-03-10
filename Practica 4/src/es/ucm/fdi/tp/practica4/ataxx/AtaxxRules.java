package es.ucm.fdi.tp.practica4.ataxx;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.Utils;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.FiniteRectBoard;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Pair;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;

/**
 * Rules for ConnectN game.
 * <ul>
 * <li>The game is played on an NxN board (with N>=3).</li>
 * <li>The number of players is between 2 and 4.</li>
 * <li>The player turn in the given order, each placing a piece on an empty
 * cell. The winner is the one who construct a line (horizontal, vertical or
 * diagonal) with N consecutive pieces of the same type.</li>
 * </ul>
 * 
 * <p>
 * Reglas del juego ConnectN.
 * <ul>
 * <li>El juego se juega en un tablero NxN (con N>=3).</li>
 * <li>El numero de jugadores esta entre 2 y 4.</li>
 * <li>Los jugadores juegan en el orden proporcionado, cada uno colocando una
 * ficha en una casilla vacia. El ganador es el que consigua construir una linea
 * (horizontal, vertical o diagonal) de N fichas consecutivas del mismo tipo.
 * </li>
 * </ul>
 *
 */
public class AtaxxRules implements GameRules {

	// This object is returned by gameOver to indicate that the game is not
	// over. Just to avoid creating it multiple times, etc.
	//
	protected final Pair<State, Piece> gameInPlayResult = new Pair<State, Piece>(State.InPlay, null);

	private int dim;
	private int obstacles;

	public AtaxxRules(int dim, int obs) {
		if (dim < 5) {
			throw new GameError("Dimension must be at least 5: " + dim);
		}
		else if (dim % 2 == 0){
			throw new GameError("Dimension must be odd: " + dim);
		}
		else if (obs > dim * dim){
			throw new GameError("The number of obstacules must be less than " + (dim*dim));
		}
		else {
			this.dim = dim;
			this.obstacles = obs;
		}
	}

	@Override
	public String gameDesc() {
		if (obstacles == 0){
			return "Ataxx " + dim + "x" + dim;
		}
		else return "Ataxx " + dim + "x" + dim + " with " + obstacles + "obstacles";
	}

	@Override
	public Board createBoard(List<Piece> pieces) {
		return initializeBoard(pieces, new FiniteRectBoard(dim, dim));
	}
	
	/**
	 * Inicializa el tablero, poniendo fichas en las posiciones nuevas
	 * @param pieces Lista de piezas de los jugadores
	 * @param board Tablero
	 */
	private Board initializeBoard(List<Piece> pieces, Board board){
		board.setPosition(0, 0, pieces.get(0));
		board.setPosition(dim - 1, dim - 1, pieces.get(0));
		board.setPosition(0, dim - 1, pieces.get(1));
		board.setPosition(dim - 1, 0, pieces.get(1));
		//Players == 3
		if (pieces.size() == 3){
			board.setPosition(dim / 2, 0, pieces.get(2));
			board.setPosition(dim / 2, dim - 1, pieces.get(2));
		}
		//Players == 4
		else if (pieces.size() == 4){
			board.setPosition(dim / 2, 0, pieces.get(2));
			board.setPosition(dim / 2, dim - 1, pieces.get(2));
			board.setPosition(0, dim / 2, pieces.get(3));
			board.setPosition(dim - 1, dim / 2, pieces.get(3));
		}
		else if (pieces.size() != 2){
			throw new GameError("El numero de jugadores no es 2, 3 o 4, asi que creo el tablero por defecto");  
		}
		//Añadir Obstaculos (en que posiciones los pongo) obstaculos
		int i = 0;
		Piece obstacle = new Piece("*#123456780");
		
		while(i < obstacles){
			int a = Utils.randomInt(dim - 1);
			int b = Utils.randomInt(dim - 1);
			//Si la posicion no esta ocupada, creo el obstaculo
			if (board.getPosition(a, b) == null){
				board.setPosition(a, b, obstacle);
				i++;
			}
		}
		
		
		return board;
	}

	@Override
	public Piece initialPlayer(Board board, List<Piece> playersPieces) {
		return playersPieces.get(0);
	}

	@Override
	public int minPlayers() {
		return 2;
	}

	@Override
	public int maxPlayers() {
		return 4;
	}

	@Override
	//cambiar
	public Pair<State, Piece> updateState(Board board, List<Piece> playersPieces, Piece lastPlayer) {
		int j;
		Piece p;
		//Contar las fichas, el que tenga mas de los 2 gana el juego
		
		
		// check rows & cols
		for (int i = 0; i < dim; i++) {
			// row i
			p = board.getPosition(i, 0);
			if (p != null) {
				j = 1;
				while (j < dim && board.getPosition(i, j) == p)
					j++;
				if (j == dim)
					return new Pair<State, Piece>(State.Won, p);
			}

			// col i
			p = board.getPosition(0, i);
			if (p != null) {
				j = 1;
				while (j < dim && board.getPosition(j, i) == p)
					j++;
				if (j == dim)
					return new Pair<State, Piece>(State.Won, p);
			}
		}

		// diagonal 1 - left-up to right-bottom
		p = board.getPosition(0, 0);
		if (p != null) {
			j = 1;
			while (j < dim && board.getPosition(j, j) == p) {
				j++;
			}
			if (j == dim) {
				return new Pair<State, Piece>(State.Won, p);
			}
		}

		// diagonal 2 - left-bottom to right-up
		p = board.getPosition(dim - 1, 0);
		if (p != null) {
			j = 1;
			while (j < dim && board.getPosition(dim - j - 1, j) == p) {
				j++;
			}
			if (j == dim) {
				return new Pair<State, Piece>(State.Won, p);
			}
		}

		if (board.isFull()) {
			return new Pair<State, Piece>(State.Draw, null);
		}

		return gameInPlayResult;
	}

	@Override
	public Piece nextPlayer(Board board, List<Piece> playersPieces, Piece lastPlayer) {
		List<Piece> pieces = playersPieces;
		int i = pieces.indexOf(lastPlayer);
		return pieces.get((i + 1) % pieces.size());
	}

	@Override
	public double evaluate(Board board, List<Piece> playersPieces, Piece turn) {
		return 0;
	}

	@Override
	//Hacer solo la lista de movimientos de la pieza pasada por parametro
	public List<GameMove> validMoves(Board board, List<Piece> playersPieces, Piece turn) {
		List<GameMove> moves = new ArrayList<GameMove>();
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getCols(); j++) {
				//Si la pieza que pasamos esta en esa posicion, miramos todos los casos
				if (board.getPosition(i, j) == turn) {
					for (int k = 0; k < 5; k++){
						for (int l = 0; l < 5; l++){
							//Si la posicion nueva esta vacia la añado
							if (board.getPosition(i + k - 2, j - l - 2) == null){
								moves.add(new AtaxxMove(i, j, i + k - 2, j - l - 2, turn));
							}
						}
					}
					
				}
				else {
					System.out.println("La posicion indicada no tiene nunguna pieza");
				}
			}
		}
		return moves;
	}

}
