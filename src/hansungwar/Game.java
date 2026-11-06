package hansungwar;

import javax.swing.*;

public class Game {
    private void start() {
        JFrame window = new JFrame("Hansung War");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.add(new GamePanel());
        window.pack();
        window.setLocationRelativeTo(null);	// 화면 가운데 띄우기

        window.setVisible(true);
    }

    public static void main(String[] args) {
    	Game game = new Game();
        game.start();
    }
}