import game.GameMain;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Graphics;
import java.io.IOException;

import javax.swing.JFrame;



public class WarForthGUI extends Canvas {
	private static final long serialVersionUID = 3986727766226586544L;
	private static GameMain game;

	public static void main(String[] args) {
		JFrame f = new JFrame("WarForth");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		game = WarForthHeadless.createGame(args);
		if (game == null) return;

		Component content = new WarForthGUI();
		content.setSize(500, 500);
		f.add(content);
		game.setObserver(content);

		game.start();

		f.pack();
		f.setVisible(true);
	}

	@Override
	public void update(Graphics g) {
		paint(g);
	}

	@Override
	public void paint(Graphics g) {
		try {
			game.paint(g, this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
