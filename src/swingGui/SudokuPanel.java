package swingGui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.event.MouseInputAdapter;

import sudoku.SudokuGenerator;
import sudoku.SudokuPuzzle;
import sudoku.SudokuPuzzleType;

@SuppressWarnings("serial")
public class SudokuPanel extends JPanel {

	private SudokuPuzzle puzzle;
	private int currentlySelectedCol;
	private int currentlySelectedRow;
	private int usedWidth;
	private int usedHeight;
	private int fontSize;

	public SudokuPanel() {
		this.setPreferredSize(new Dimension(540, 450));
		this.addMouseListener(new SudokuPanelMouseAdapter());
		this.puzzle = new SudokuGenerator().generateRandomSudoku(SudokuPuzzleType.TWELVEBYTWELVE);
		setKeyBindings();
		currentlySelectedCol = -1;
		currentlySelectedRow = -1;
		usedWidth = 0;
		usedHeight = 0;
		fontSize = 26;
	}

	public SudokuPanel(SudokuPuzzle puzzle) {
		this.setPreferredSize(new Dimension(540, 450));
		this.addMouseListener(new SudokuPanelMouseAdapter());
		setKeyBindings();
		this.puzzle = puzzle;
		currentlySelectedCol = -1;
		currentlySelectedRow = -1;
		usedWidth = 0;
		usedHeight = 0;
		fontSize = 12;
	}

	private void setKeyBindings() {
		ActionMap actionMap = getActionMap();
		int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
		InputMap inputMap = getInputMap(condition);

		String vk1 = "VK_1";
		String vk2 = "VK_2";
		String vk3 = "VK_3";
		String vk4 = "VK_4";
		String vk5 = "VK_5";
		String vk6 = "VK_6";
		String vk7 = "VK_7";
		String vk8 = "VK_8";
		String vk9 = "VK_9";
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_1, 0), vk1);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_2, 0), vk2);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_3, 0), vk3);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_4, 0), vk4);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_5, 0), vk5);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_6, 0), vk6);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_7, 0), vk7);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_8, 0), vk8);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_9, 0), vk9);

		actionMap.put(vk1, new KeyAction(vk1));
		actionMap.put(vk2, new KeyAction(vk2));
		actionMap.put(vk3, new KeyAction(vk3));
		actionMap.put(vk4, new KeyAction(vk4));
		actionMap.put(vk5, new KeyAction(vk5));
		actionMap.put(vk6, new KeyAction(vk6));
		actionMap.put(vk7, new KeyAction(vk7));
		actionMap.put(vk8, new KeyAction(vk8));
		actionMap.put(vk9, new KeyAction(vk9));
	}

	public void newSudokuPuzzle(SudokuPuzzle puzzle) {
		this.puzzle = puzzle;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(new Color(1.0f, 1.0f, 1.0f));

		int slotWidth = this.getWidth() / puzzle.getNumColumns();
		int slotHeight = this.getHeight() / puzzle.getNumRows();

		usedWidth = (this.getWidth() / puzzle.getNumColumns()) * puzzle.getNumColumns();
		usedHeight = (this.getHeight() / puzzle.getNumRows()) * puzzle.getNumRows();

		g2d.fillRect(0, 0, usedWidth, usedHeight);

		g2d.setColor(new Color(0.0f, 0.0f, 0.0f));
		for (int x = 0; x <= usedWidth; x += slotWidth) {
			if ((x / slotWidth) % puzzle.getBoxWidth() == 0) {
				g2d.setStroke(new BasicStroke(2));
				g2d.drawLine(x, 0, x, usedHeight);
			} else {
				g2d.setStroke(new BasicStroke(1));
				g2d.drawLine(x, 0, x, usedHeight);
			}
		}
		// this will draw the right most line
		// g2d.drawLine(usedWidth - 1, 0, usedWidth - 1,usedHeight);
		for (int y = 0; y <= usedHeight; y += slotHeight) {
			if ((y / slotHeight) % puzzle.getBoxHeight() == 0) {
				g2d.setStroke(new BasicStroke(2));
				g2d.drawLine(0, y, usedWidth, y);
			} else {
				g2d.setStroke(new BasicStroke(1));
				g2d.drawLine(0, y, usedWidth, y);
			}
		}
		// this will draw the bottom line
		// g2d.drawLine(0, usedHeight - 1, usedWidth, usedHeight - 1);
		Font f = new Font("Times New Roman", Font.PLAIN, fontSize);
		g2d.setFont(f);
		FontRenderContext fContext = g2d.getFontRenderContext();
		for (int row = 0; row < puzzle.getNumRows(); row++) {

			for (int col = 0; col < puzzle.getNumColumns(); col++) {

				if (!puzzle.isSlotAvailable(row, col)) {
					int textWidth = (int) f.getStringBounds(puzzle.getValue(row, col), fContext).getWidth();
					int textHeight = (int) f.getStringBounds(puzzle.getValue(row, col), fContext).getHeight();
					g2d.drawString(puzzle.getValue(row, col), (col * slotWidth) + ((slotWidth / 2) - (textWidth / 2)),
							(row * slotHeight) + ((slotHeight / 2) + (textHeight / 2)));
				}
				if (!puzzle.isSlotMutable(row, col)) {

					AttributedString as1 = new AttributedString(puzzle.getValue(row, col));
					as1.addAttribute(TextAttribute.FONT, f);
					as1.addAttribute(TextAttribute.FOREGROUND, Color.red);
					int textWidth = (int) f.getStringBounds(puzzle.getValue(row, col), fContext).getWidth();
					int textHeight = (int) f.getStringBounds(puzzle.getValue(row, col), fContext).getHeight();
					g2d.drawString(as1.getIterator(), (col * slotWidth) + ((slotWidth / 2) - (textWidth / 2)),
							(row * slotHeight) + ((slotHeight / 2) + (textHeight / 2)));
				}

			}
		}
		if (currentlySelectedCol != -1 && currentlySelectedRow != -1) {
			g2d.setColor(new Color(0.0f, 0.0f, 1.0f, 0.3f));
			g2d.fillRect(currentlySelectedCol * slotWidth, currentlySelectedRow * slotHeight, slotWidth, slotHeight);
		}
	}

	public void messageFromNumActionListener(String buttonValue) {
		if (currentlySelectedCol != -1 && currentlySelectedRow != -1) {
			if(!puzzle.makeMove(currentlySelectedRow, currentlySelectedCol, buttonValue, true)) {
				JOptionPane.showMessageDialog(new JFrame(),"The move is not valid");
			} 
			repaint();
		}
	}

	public void messageFromClearActionListener() {
		if (currentlySelectedCol != -1 && currentlySelectedRow != -1) {
			puzzle.clearMove(currentlySelectedRow, currentlySelectedCol, true);
			repaint();
		}
	}

	public class NumActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			messageFromNumActionListener(((JButton) e.getSource()).getText());
		}
	}

	public class ClearActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			messageFromClearActionListener();
		}
	}

	private class KeyAction extends AbstractAction {
		public KeyAction(String actionCommand) {
			putValue(ACTION_COMMAND_KEY, actionCommand);
		}

		@Override
		public void actionPerformed(ActionEvent actionEvt) {
			System.out.println(actionEvt.getActionCommand() + " pressed");
			messageFromNumActionListener(getKeyNumberPressed(actionEvt.getActionCommand()));			
		}
	}
	
	public String getKeyNumberPressed(String keyCode) {
	     String keyNumber;
	     switch (keyCode) {
	         case "VK_1":
	        	 keyNumber = "1";
	             break;
	         case "VK_2":
	        	 keyNumber = "2";
	             break;
	         case "VK_3":
	        	 keyNumber = "3";
	             break;
	         case "VK_4":
	        	 keyNumber = "4";
	             break;
	         case "VK_5":
	        	 keyNumber = "5";
	             break;
	         case "VK_6":
	        	 keyNumber = "6";
	             break;
	         case "VK_7":
	        	 keyNumber = "7";
	             break;
	         case "VK_8":
	        	 keyNumber = "8";
	             break;
	         case "VK_9":
	        	 keyNumber = "9";
	             break;
	         default:
	             throw new IllegalArgumentException("Key: " + keyCode);
	     }
	     return keyNumber;
	}

	private class SudokuPanelMouseAdapter extends MouseInputAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				int slotWidth = usedWidth / puzzle.getNumColumns();
				int slotHeight = usedHeight / puzzle.getNumRows();
				currentlySelectedRow = e.getY() / slotHeight;
				currentlySelectedCol = e.getX() / slotWidth;
				e.getComponent().repaint();
			}
		}
	}
}
