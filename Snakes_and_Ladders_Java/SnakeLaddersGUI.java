import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.geom.AffineTransform;


class MainFrame extends JFrame {
	public SnakeLaddersClient client;
	class RoomChoicePanel extends JPanel implements ActionListener{
		int unit;
		int rows;
		int columns;
		public TextField roomField;
		public Button in;
		public Button create;
		public TextArea roomMessBoard;
		public Button logOut;
		public RoomChoicePanel( int u){
			unit = u;
			this.setSize( 160*unit, 90*unit);
			this.setLayout(null);
			
			Label s = new Label("Choose a Room");
			s.setBounds( 50*unit, 1*unit, 60*unit, 9*unit); 
			s.setFont(new Font("Serif",Font.BOLD, 8*unit));
			
			roomField = new TextField();
			roomField.setBounds( 50*unit, 12*unit, 19*unit, 6*unit);
			roomField.setFont(new Font( "Serif", Font.BOLD, 6*unit));
			in = new Button("In");
			in.setBounds( 50*unit, 22*unit, 10*unit, 6*unit); 
			in.addActionListener( this);
			create = new Button("Create");
			create.setBounds( 50*unit, 32*unit, 10*unit, 6*unit); 
			create.addActionListener( this);
			
			roomMessBoard = new TextArea("RoomMessBoard", 85*unit, 30*unit, TextArea.SCROLLBARS_BOTH);
			roomMessBoard.setBounds( 80*unit, 12*unit, 30*unit, 75*unit); 
			roomMessBoard.setEditable(false);
			roomMessBoard.setFont(new Font( "Serif", Font.BOLD, 6*unit));
			
			logOut = new Button("LogOut");
			logOut.setBounds( 149*unit, 83*unit, 10*unit, 6*unit); 
			logOut.addActionListener( this);
			
			this.add( s);
			this.add( roomField);
			this.add( in);
			this.add( create);
			this.add( roomMessBoard);
			this.add( logOut);
		}
		public void UpdateRoomMessBoard( ){
			this.roomMessBoard.setText("");
			for(int i=0; i<client.roomNameList.size();i++){
				this.roomMessBoard.append( String.valueOf(i)+". "+client.roomNameList.get(i)+"\n");
			}
		}
		public void actionPerformed(ActionEvent evt){
			try
			{
				LinkedList<String> messages = new LinkedList<String>();
				messages.add( roomField.getText());
				client.action( ( (Button)evt.getSource()).getLabel(), messages);
			}
			catch(Exception e)
			{
				System.out.println("ArithmeticServer exception: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	class BoardGamePanel extends JPanel implements ActionListener{
		Color[] playerColor = {Color.BLACK, Color.BLUE, Color.RED, Color.ORANGE, Color.CYAN, Color.DARK_GRAY, Color.GRAY, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA, Color.PINK, Color.WHITE, Color.YELLOW};
		class CheckerBoard extends Canvas {
			int unit;
			int boardUnit;
			int rows;
			int columns;
			public CheckerBoard(int u, int c, int r, int bU){
				unit = u;
				columns = c;
				rows = r;
				boardUnit = bU;
				this.setSize( boardUnit*columns*unit+2, boardUnit*rows*unit+2);
			}
			public void paint(Graphics g1) {
				Graphics2D g = (Graphics2D) g1;
				for(int i=0; i<client.mapObjList.size(); i++){
					MapObjImage moi = client.mapObjList.get(i);
					if( moi.objKind == 0){
						drawLadders( g, moi);	
					}
					else if( moi.objKind == 1){
						drawSnakes( g, moi);	
					}
					
				}
				drawPlayerList( g);
				for( int i=0; i<client.playerList.size(); i++){
					if(client.playerList.get(i).memberIndex == client.memberIndex){
						g.setColor( playerColor[i]);
						break;
					}
				}
				
				for( int i=0; i<rows; i+=1){
					for( int j=0; j<columns; j+=1){
						g.drawRect( boardUnit*j*unit+1, boardUnit*i*unit+1, boardUnit*unit, boardUnit*unit);
						int num;
						num = i*rows+j+1;
						num = CoordinateTransform( num, false);
						g.drawString( String.valueOf( (int)num), boardUnit*j*unit+1+boardUnit*unit-3*unit, boardUnit*i*unit+1+boardUnit*unit-4);	
					}
				}
			} 
			public BufferedImage joinBufferedImage(BufferedImage img1,BufferedImage img2) {
				//do some calculate first
				int offset  = 10;
				int height = img1.getHeight()+img2.getHeight();
				int wid = Math.max(img1.getWidth(),img2.getWidth());
				//create a new buffer and draw two image into the new image
				BufferedImage newImage = new BufferedImage( wid, height-offset, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2 = newImage.createGraphics();
				Color oldColor = g2.getColor();
				//draw image
				g2.setColor(oldColor);
				g2.drawImage(img1, null, 0, 0);
				g2.drawImage(img2, null, 0, img1.getHeight()-offset);
				g2.dispose();
				return newImage;
			}
			public void drawPlayerList( Graphics2D g){
				for( int i=0; i<client.playerList.size(); i++){
					int number = 1;
					Player player = client.playerList.get(i);
					for( int j=0; j<i; j++){
						if( player.location == client.playerList.get(j).location){
							number++;
						}
					}
					drawPlayer( g, player, number, i);	
				}
			}
			public void drawPlayer( Graphics2D g, Player p, int orderOnLoc, int playerIndex){
				int coord = CoordinateTransform( p.location, true);
				int rowLoc = (coord-1)/rows;
				int colLoc = (coord-1)%rows;
				int size = 3;
				int rowOrderLoc = (orderOnLoc-1)/size;
				int colOrderLoc = (orderOnLoc-1)%size;
				float rowShiftPart = (float)rowOrderLoc/size;
				float colShiftPart = (float)colOrderLoc/size;
				int rowShift = Math.round(rowShiftPart*boardUnit*unit);
				int colShift = Math.round(colShiftPart*boardUnit*unit);
				//draw image
				g.setColor( playerColor[playerIndex]);
				g.fillRect( colLoc*boardUnit*unit+3+colShift, rowLoc*boardUnit*unit+3+rowShift, boardUnit*unit/4, boardUnit*unit/4);
			}
			public void drawSnakes( Graphics2D g, MapObjImage ladder){
				int coordStart = CoordinateTransform( ladder.start, true);
				int rowStart = (coordStart-1)/rows;
				int colStart = (coordStart-1)%rows;
				int coordDest = CoordinateTransform( ladder.destination, true);
				int rowDest = (coordDest-1)/rows;
				int colDest = (coordDest-1)%rows;
				int coordDistance = (int)Math.round( Pitagor(rowStart-rowDest, colStart-colDest));
				int rowDistance = (rowDest-rowStart)*(-1);
				int colDistance = colDest-colStart;
				BufferedImage oriImage = null;
				BufferedImage head = null;
				BufferedImage body = null;
				BufferedImage tail = null;
				Random randomGenerator = new Random();
				int randomNum;
				try
				{  
					oriImage = ImageIO.read( new File("Pictures\\Snakes.png"));
					randomNum = randomGenerator.nextInt(100)%6;
					head = oriImage.getSubimage( 0+56*randomNum, 0, 56, 36);
					randomNum = randomGenerator.nextInt(100)%6;
					body = oriImage.getSubimage( 0+56*randomNum, 56, 56, 36);
					randomNum = randomGenerator.nextInt(100)%6;
					tail = oriImage.getSubimage( 0+56*randomNum, 56*3-36, 56, 36);
				}
				catch(Exception e)
				{ 
					javax.swing.JOptionPane.showMessageDialog(null, "FileInputError: "); 
				}
				BufferedImage image = head;
				int imageHeight = coordDistance*2+1-1;
				for(int i=1; i<imageHeight; i++){
					image = joinBufferedImage(image, body);
				}
				image = joinBufferedImage(image, tail);
				
				// create the transform, note that the transformations happen
				// in reversed order (so check them backwards)
				AffineTransform at = new AffineTransform();
				// 4. translate it to the center of the component
				//at.translate( getWidth() / 2, getHeight() / 2);
				double angle = Math.atan((double)colDistance/rowDistance);
				double shift = 0.5-(angle/Math.PI);

				at.translate( (colDest+0.1)*boardUnit*unit, (rowDest+shift)*boardUnit*unit);
				// 3. do the actual rotation
				at.rotate( angle);
				//g.setPaint( playerColor[ladder.objKind]);
				
				g.drawImage( image, at, null);
			}
			public void drawLadders( Graphics2D g, MapObjImage ladder){
				int coordStart = CoordinateTransform( ladder.start, true);
				int rowStart = (coordStart-1)/rows;
				int colStart = (coordStart-1)%rows;
				int coordDest = CoordinateTransform( ladder.destination, true);
				int rowDest = (coordDest-1)/rows;
				int colDest = (coordDest-1)%rows;
				int coordDistance = (int)Math.round( Pitagor(rowStart-rowDest, colStart-colDest));
				int rowDistance = (rowDest-rowStart)*(-1);
				int colDistance = colDest-colStart;
				BufferedImage oriImage = null;
				try
				{ 
					Random randomGenerator = new Random();
					int randomS = randomGenerator.nextInt(100)%5;	
					oriImage = ImageIO.read( new File("Pictures\\Ladders" + String.valueOf(randomS)+".png"));			
				}
				catch(Exception e)
				{ 
					javax.swing.JOptionPane.showMessageDialog(null, "FileInputError: "); 
				}
				BufferedImage image = oriImage;
				int imageHeight = coordDistance*2+1;
				for(int i=1; i<imageHeight; i++){
					image = joinBufferedImage(image, oriImage);
				}
				
				// create the transform, note that the transformations happen
				// in reversed order (so check them backwards)
				AffineTransform at = new AffineTransform();
				// 4. translate it to the center of the component
				//at.translate( getWidth() / 2, getHeight() / 2);
				double angle = Math.atan((double)colDistance/rowDistance);
				double shift = 0.5-(angle/Math.PI);

				at.translate( (colDest+0.1)*boardUnit*unit, (rowDest+shift)*boardUnit*unit);
				// 3. do the actual rotation
				at.rotate( angle);
				//g.setPaint( playerColor[ladder.objKind]);
				
				g.drawImage( image, at, null);
			}
			public int CoordinateTransform( int orNum, Boolean whetherIntoCoordinate){
				double num = (double)orNum;
				if( whetherIntoCoordinate){	
					if(Math.ceil(num/10)%2==1){
						num = Math.ceil(num/10)*10*2-9-num;
					}
					else{	
					}
					num = 101-num;
				}else{
					num = 101-num;
					if(Math.ceil(num/10)%2==1){
						num = Math.ceil(num/10)*10*2-9-num;
					}
					else{	
					}
				}
				return (int)num;
			}
			public double Pitagor (int x , int y){
				double c = Math.sqrt((x*x)+(y*y));
				return c;
			}
		}

		public CheckerBoard snakeLadderBoard;
		public Button dice;
		public Button leave;
		public BoardGamePanel(int unit){
			this.setSize( 160*unit, 90*unit);
			this.setLayout(null);
					
			snakeLadderBoard = new CheckerBoard( unit, 10, 10, 7);
			
			snakeLadderBoard.setLocation( 10*unit-2, 10*unit-2);
			dice = new Button("Dice");
			dice.setBounds( 40*unit, 82*unit, 10*unit, 6*unit); 
			dice.addActionListener( this);
			
			leave = new Button("Leave");
			leave.setBounds( 149*unit, 83*unit, 10*unit, 6*unit); 
			leave.addActionListener( this);
			
			this.add( snakeLadderBoard);
			this.add( dice);
			this.add( leave);
		}
		public void actionPerformed(ActionEvent evt){
			try
			{
				LinkedList<String> messages = new LinkedList<String>();
				client.action( ( (Button)evt.getSource()).getLabel(), messages);
			}
			catch(Exception e)
			{
				System.out.println("ArithmeticServer exception: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	class LoginPanel extends JPanel implements ActionListener{
		public Label accTitle; 
		public TextField accInfo;
		public TextField passInfo;
		public Button login;
		public Button reg;
		public LoginPanel(int unit){
			this.setSize( 160*unit, 90*unit); 
			this.setLayout(null);
			
			Label s = new Label("Snakes");
			s.setBounds( 65*unit, 1*unit, 30*unit, 8*unit); 
			s.setFont(new Font("Serif",Font.BOLD, 8*unit));
			Label a = new Label("&");
			a.setBounds( 78*unit, 11*unit, 10*unit, 8*unit); 
			a.setFont(new Font("Serif",Font.BOLD, 8*unit));
			Label l = new Label("Ladders");
			l.setBounds( 75*unit, 21*unit, 30*unit,8*unit); 
			l.setFont(new Font("Serif",Font.BOLD, 8*unit));
			
			Label accTitle = new Label("Account");
			accTitle.setBounds( 30*unit, 30*unit, 25*unit, 10*unit); 
			accTitle.setFont(new Font("Serif",Font.BOLD,6*unit));
			accInfo = new TextField();
			accInfo.setBounds( 60*unit, 30*unit, 40*unit, 10*unit); 
			accInfo.setFont(new Font( "Serif", Font.BOLD, 10*unit));
			
			Label passTitle = new Label("Password");
			passTitle.setBounds( 30*unit, 45*unit, 25*unit, 10*unit); 
			passTitle.setFont(new Font("Serif",Font.BOLD,6*unit));
			passInfo = new TextField(40*unit);
			passInfo.setBounds( 60*unit, 45*unit, 40*unit, 10*unit); 
			passInfo.setFont(new Font( "Serif", Font.BOLD, 10*unit));
			
			login = new Button("Login");
			login.setBounds( 70*unit, 60*unit, 20*unit, 10*unit); 
			login.addActionListener( this);
			reg = new Button("Reg");
			reg.setBounds( 70*unit, 75*unit, 20*unit, 10*unit); 
			reg.addActionListener( this);
			
			this.add( s);
			this.add( a);
			this.add( l);
			this.add( accTitle);
			this.add( accInfo);
			this.add( passTitle);
			this.add( passInfo);
			this.add( login);
			this.add( reg);
			this.setVisible(true);
		}
		public void actionPerformed(ActionEvent evt)
		{
			try
			{
				LinkedList<String> messages = new LinkedList<String>();
				messages.add( accInfo.getText());
				messages.add( passInfo.getText());
				client.action( ( (Button)evt.getSource()).getLabel(), messages);
			}
			catch(Exception e)
			{
				System.out.println("ArithmeticServer exception: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	public LoginPanel loginPanel;
	public BoardGamePanel playingPanel;
	public RoomChoicePanel roomChoicePanel;
	
	public MainFrame( SnakeLaddersClient slc){
		Canvas forResize = new Canvas();
		int unit = 8;
		int width = 160*unit;
		int height = 90*unit;
		forResize.setSize(width, height);
		this.add(forResize);
		this.pack();
		this.remove(forResize);
		this.setVisible(true);	
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginPanel = new LoginPanel( unit);
		playingPanel = new BoardGamePanel( unit);
		roomChoicePanel = new RoomChoicePanel( unit);
		client = slc;
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				LinkedList<String> messages = new LinkedList<String>();
				client.action("close", messages);
				System.exit(0);
			}
		} );
	}
	public void removeAllPanel(){
		this.remove(loginPanel);
		this.remove(playingPanel);
		this.remove(roomChoicePanel);
	}
	public void changePage( String state){
		switch(state){
			case "playing":
				this.removeAllPanel();
				this.add(playingPanel);
				break;
			case "logging":
				this.removeAllPanel();
				this.add(loginPanel);
				break;
			case "room":
				this.removeAllPanel();
				this.add(roomChoicePanel);
				break;
		}
	}
}