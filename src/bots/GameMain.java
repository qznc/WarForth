package bots;

import java.awt.Component;
import java.awt.Graphics;
import java.io.IOException;
import java.util.Random;

public class GameMain implements Runnable {
	private static final int SLEEP_TIME = 40;
	private final GameBoard board;
	private Component observer;
	private final Random rnd;
	private boolean running = false;
	private boolean exit_now = false;

	public GameMain(long random_seed, String red_prog, String blue_prog) throws IOException {
		rnd = new Random(random_seed);
		board = new GameBoard(rnd, red_prog, blue_prog);

		// TODO tech demo style:
		board.createBot(Faction.Red, 100, 100);
		board.createBot(Faction.Red, 300, 480);
		board.createBot(Faction.Red, 200, 10);
		board.createBot(Faction.Blue, 200, 100);
		board.createBot(Faction.Blue, 480, 400);
		board.createBot(Faction.Blue, 20, 300);
	}

	public void setObserver(Component observer) {
		this.observer = observer;
	}

	public void start() {
		if (running) return;

		running = true;
		Thread t = new Thread(this);
		t.start();
	}

	public void stop() {
		running = false;
	}


	public void destroy() {
		running = false;
		exit_now = true;
	}

	@Override
	public void run() {
		while (true) {
			if (exit_now) return;

			if (running) {
				board.turn();
				if (observer != null) observer.repaint();
			}

			try	{
				Thread.sleep (SLEEP_TIME);
			} catch (InterruptedException ex)	{
				// do nothing
			}
		}
	}

	@SuppressWarnings("hiding")
	public void paint(Graphics g, Component observer) throws IOException {
		board.paint(g, observer);
	}
}
