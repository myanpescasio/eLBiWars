package eLBiWarsClient;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class ElbiWars_UI extends JPanel implements MouseListener,Runnable,ActionListener{
	private static final long serialVersionUID = 1L;

	int MODE = -1;
	int buildingType = -1;
	int troopType = -1;
	JPanel buildingsPanel = new JPanel();
	JButton[] buildingsButton = new JButton[8];
	JPanel troopsPanel = new JPanel();
	JButton[] troopsButton = new JButton[10];
	JPanel bottomPanel = new JPanel();
	JButton modeChange = new JButton("Mode");
	LinkedList<ElbiWars_Troops> chars = new LinkedList<ElbiWars_Troops>();
	LinkedList<ElbiWars_Building> builds = new LinkedList<ElbiWars_Building>();
	static Client window;
	@SuppressWarnings("deprecation")
	public ElbiWars_UI(){
		this.setPreferredSize(new Dimension(600,600));
		this.setBackground(Color.GREEN);
		this.addMouseListener(this);
		
		buildingsPanel.setLayout(new GridLayout(1,5));
		for(int i=0; i<8; i++){
			buildingsButton[i] = new JButton(Integer.toString(i));
			buildingsButton[i].addActionListener(this);
			buildingsPanel.add(buildingsButton[i]);
		}
		
		troopsPanel.setLayout(new GridLayout(2,5));
		for(int i=0; i<10; i++){
			troopsButton[i] = new JButton(Integer.toString(i));
			troopsButton[i].addActionListener(this);
			troopsPanel.add(troopsButton[i]);
		}
		
		this.setLayout(new BorderLayout());
		
		bottomPanel.add(buildingsPanel);
		bottomPanel.add(troopsPanel);
		this.add(bottomPanel, BorderLayout.SOUTH);
		bottomPanel.hide();
		this.add(modeChange, BorderLayout.NORTH);
		modeChange.addActionListener(this);
		
	}

	
	public static void main(String[] args){
		final JPanel cards = new JPanel();
		JPanel welcome = new JPanel();
		cards.setLayout(new CardLayout());
		
		JFrame mainFrame = new JFrame("Elbi Wars");
		ElbiWars_UI mainPanel = new ElbiWars_UI();
		JPanel mainpanelscaffold = new JPanel();
		window = new Client(welcome);
		
		mainpanelscaffold.setLayout(new BorderLayout());
		mainpanelscaffold.add(mainPanel, BorderLayout.WEST);
		mainpanelscaffold.add(window.Initialize2(), BorderLayout.EAST);
		
		Thread t = new Thread(mainPanel);
		t.start();
		
		
		cards.add(welcome, "Welcome");
		cards.add(mainpanelscaffold, "Main");
		 
		CardLayout cardLayout = (CardLayout) cards.getLayout();
		cardLayout.show(cards, "Welcome");
		
		JButton button = new JButton("Go!");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cardLayout = (CardLayout) cards.getLayout();
				boolean tester = window.connect(e);		
				if(tester == true){
					cardLayout.show(cards, "Main");
				}else{
					JOptionPane.showMessageDialog(null, "CANNOT CONNECT TO SERVER.");
				}
				
			}
		});
		
		welcome.add(button);
		mainFrame.setContentPane(cards);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		mainFrame.pack();

	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
		if(MODE == 0){
			if(builds.size() <= 2 && buildingType != -1){
				ElbiWars_Building newBuilding = new ElbiWars_Building(arg0.getX(), arg0.getY(), buildingType);
				newBuilding.setBounds(arg0.getX(), arg0.getY(), 30, 30);
				builds.add(newBuilding);
			}
		}else if(MODE == 1){
			ElbiWars_Troops troop = new ElbiWars_Troops(arg0.getX(), arg0.getY(), troopType);
			if(troopType != -1 && checkCollision(troop) == false){
				Random random = new Random();
				if((builds.size() * random.nextInt())%2 == 0){
					troop.a = builds.getLast();
				} else {
					troop.a = builds.getFirst();
				}
				
				chars.add(troop);
				
				Thread k = new Thread(chars.getLast());
				k.start();
			}
		}
		
		this.revalidate();
		this.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
	    //Convert to Java2D Object
	    Graphics2D g2 = (Graphics2D) g;
	    
		    if(buildingType != -1){
		    	for(ElbiWars_Building c: builds){
				    g2.drawRect(c.xcoordinate, c.ycoordinate,30,30);
			    }
		    }
	  
		    if(troopType != -1){
			    for(ElbiWars_Troops b: chars){
			    	g2.drawOval(b.xcoordinate, b.ycoordinate,10,10);
			    }
		    }
		    
	} 
	
	public void run() {
		while(true){
			this.revalidate();
			this.repaint();
			
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == modeChange){
			if(MODE == -1){
				MODE = 0;
				bottomPanel.show();
				troopsPanel.hide();
			} else {
				MODE = 1;
				troopsPanel.show();
				buildingsPanel.hide();
			}
		}
		
		for(int i=0; i<8; i++){
			if(e.getSource() == buildingsButton[i]){
				buildingType = i;
				System.out.println("Building type = "+buildingType);
			}
		}
		
		for(int j=0; j<10; j++){
			if(e.getSource() == troopsButton[j]){
				troopType = j;
				System.out.println("Troop type = "+troopType);
			}
		}
		
	}
	
	public boolean checkCollision(ElbiWars_Troops a){
		Rectangle obj1 = a.getBounds();
		
		for(ElbiWars_Building z: builds){
			Rectangle obj2 = z.getBounds();
			
			if(obj1.intersects(obj2)){
				System.out.println("Troop "+a.type+" intersects with "+z.type);
				return true;
			}
		}
		
		return false;
		
	}
}
