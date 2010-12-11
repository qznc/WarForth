package game;

import java.awt.Component;
import java.awt.Graphics;
import java.io.IOException;
import java.util.Random;

public class GameMain implements Runnable {
	private static final int SLEEP_TIME = 40;
	private final GameBoard board;
	private Component observer;
	private final Random rnd;
	private boolean thread_running = false;
	private boolean exit_now = false;

	public GameMain(long random_seed, String red_prog, String blue_prog) throws IOException {
		rnd = new Random(random_seed);
		board = new GameBoard(rnd, red_prog, blue_prog);
	}

	public void setObserver(Component observer) {
		this.observer = observer;
	}

	/**
	 * Start this game in its own Thread
	 */
	public void start() {
		if (thread_running) return;

		thread_running = true;
		Thread t = new Thread(this);
		t.start();
	}

	public void stop() {
		thread_running = false;
	}


	public void destroy() {
		thread_running = false;
		exit_now = true;
	}

	/**
	 * The game main loop with Observers/GUIs
	 */
	@Override
	public void run() {
		while (true) {
			if (exit_now) return;

			if (thread_running) {
				boolean finished = board.turn();
				if (observer != null) observer.repaint();
				if (finished) {
					exit_now = true;
					thread_running = false;
					return;
				}
			}

			try	{
				Thread.sleep (SLEEP_TIME);
			} catch (InterruptedException ex)	{
				return;
			}
		}
	}

	public Faction getWinner() {
		return board.getWinner();
	}

	/**
	 * The games main loop without observers
	 * @return	the winner
	 */
	public Faction runNonthreaded() {
		while (true) {
			boolean finished = board.turn();
			if (finished) {
				return board.getWinner();
			}
		}
	}

	public void paint(Graphics g, Component obs) throws IOException {
		board.paint(g, obs);
	}
}
