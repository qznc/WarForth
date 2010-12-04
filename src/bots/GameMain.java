package bots;

import java.awt.Component;
import java.awt.Graphics;
import java.util.Random;

public class GameMain extends Thread {
	private final GameBoard board;
	private Component observer;
	private final Random rnd;

	public GameMain(long random_seed) {
		rnd = new Random(random_seed);
		board = new GameBoard(rnd);
	}

	public void setObserver(Component observer) {
		this.observer = observer;
	}

	@Override
	public void run() {
		while (true) {
			board.turn();

			if (observer != null) observer.repaint();

			try	{
				Thread.sleep (80);
			} catch (InterruptedException ex)	{
				// do nothing
			}
		}
	}

	@SuppressWarnings("hiding")
	public void paint(Graphics g, Component observer) {
		board.paint(g, observer);
	}
}
