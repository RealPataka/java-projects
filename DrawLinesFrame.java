public class DrawLinesFrame extends JFrame {  
	DrawPanel drawPanel;
	private ArrayList<Point> points = new ArrayList<Point>();
	public DrawLinesFrame() {
		drawPanel = new DrawPanel();
		drawPanel.addMouseListener(new MyMouseAdapter());
		getContentPane().add(drawPanel, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 250);
		setVisible(true);
	}
  
	class DrawPanel extends JPanel {		
		public void paintComponent(Graphics g) {
		    super.paintComponent(g);  // paint parent's background
		    Point pt1, pt2;
		    for (int i=0; i<points.size(); i+=2) {
		    	pt1 = (Point) points.get(i);
		    	pt2 = (Point) points.get(i+1);
		    	g.drawLine(pt1.x, pt1.y, pt2.x, pt2.y);
		    }
		}
	}
	
	//This is the class I had to add
	class MyMouseAdapter extends MouseAdapter{
		public void mousePressed(MouseEvent e) {
			Point p = new Point(e.getX(), e.getY());
			points.add(p);
		}
		
		public void mouseReleased(MouseEvent e) {
			Point p = new Point(e.getX(), e.getY());
			points.add(p);
			drawPanel.paintComponent(getGraphics());
			
		}
	}
	//My code ends here
	
	public static void main(String[] args) {
	    SwingUtilities.invokeLater(new Runnable() {
	      public void run() { new DrawLinesFrame(); }
	    });
	}
}
