package gui;

// @Override
// public void widgetSelected(SelectionEvent arg0) {
// if(passwordText.getText().equals(repeatPasswordText.getText()))
// display.syncExec(new ThreadAddUser(oparations, userNameText.getText(), passwordText.getText()){
// @Override
// public void run(){
// super.run();
// MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
// messageBox.setText("Connection Error");
// messageBox.setMessage("Error");
// messageBox.open();
// }
// });
// }
// });



/////////////////////////////////////////////////////////////////////////////////


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

import com.sun.javafx.iio.common.ImageDescriptor;

import config.config;
import db.db_queries;
import bl.verifier;


public class search_friends_window extends Composite {
	private Text friend_name_text;
	
	
	//data//
	config config = new config();
	private int window_height = config.get_window_height();
	private int window_width = config.get_window_width();
//
//	 public static void main(String args[]) {
//	 try {
//	 Display display = Display.getDefault();
//	 search_friends_window shell = new search_friends_window(display );
//	// shell.setSize(1000,1000);
//	 shell.open();
//	 shell.layout();
//	 while (!shell.isDisposed()) {
//	 if (!display.readAndDispatch()) {
//	 display.sleep();
//	 }
//	 }
//	 } catch (Exception e) {
//	 e.printStackTrace();
//	 }
//	 }
//
//	/**
//	 * Create the shell.
//	 * @param display
//	 */
	public search_friends_window(final Display display, Composite parent,int style) {
		super(parent, style);
	//	this.setSize(window_width, window_height);
		this.setSize(10,10);
		
	//	db_queries queries = new db_queries();
	
		

		//setImage(SWTResourceManager.getImage(SignUpForm.class, "/movies.png"));
	//	setLayout(null);

		Composite shell = new Composite(this, SWT.NONE);
		shell.setBounds( 0,  0, window_width, window_height);
		shell.setSize( window_height, window_width);
		GridLayout  grid = new GridLayout(2,false);
		shell.setLayout(grid);
		
		friend_name_text = new Text(shell, SWT.BORDER);
		friend_name_text.setBounds(150, 200, 320, 30);
		
		////////////////////////////
		
	
		


		shell.setBackgroundImage(new Image(display,"search_friends_window.png"));

		createContents();
		

		
		
		
		

		Button search_button = new Button(shell, SWT.PUSH);
		search_button.setBounds(270, 280, 90, 30);
		search_button.setText("Search");
		search_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(!verifier.verifyname(friend_name_text.getText())){ 
					MessageBox alertBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
					alertBox.setText("Illegal Username");
					alertBox.setMessage("Friend name length is 1-10 chars \n Only letters or numbers allowed.");
					alertBox.open();
				}


//				else if(does_user_exists(friend_name_text.getText())){ //to be implemented next on
//					display.syncExec(new AddUser(oparations, userNameText.getText(), passwordText.getText()){
//						@Override
//						public void run(){
//							super.run();
//							int result = this.getValue();
//							MessageBox messageBox = new MessageBox(display.getActiveShell(),SWT.ICON_INFORMATION);
//							if (result == 0){
//
//								messageBox.setText("Error");
//								messageBox.setMessage("Connection Error");
//								messageBox.open();
//
//							}else if(result == 1){
//								messageBox.setText("Welcome");
//								messageBox.setMessage("New user was added: " + userNameText.getText());
//								messageBox.open();
//
//								// open main menu
//								int idUser = userNameText.getText().hashCode();
//								dispose();
//								MainMenu MainMenuShell = new MainMenu(display,oparations,false, idUser);
//								MainMenuShell.open();
//								MainMenuShell.layout();
//								while (!MainMenuShell.isDisposed()) {
//									if (!display.readAndDispatch()) {
//										display.sleep();
//									}
//								}
//
//							}
//							else {
//								messageBox.setText("Warning");
//								messageBox.setMessage("User already exist : " + userNameText.getText());
//								messageBox.open();
//							}
//
//						}
//
//
//					});
//				}
				else //no user found
				{
					MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
					messageBox.setText("No user Found");
					messageBox.setMessage("No user Found");
					messageBox.open();
				}
			}

		});


	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
	//	setText("Search Friend window");
		setSize( window_height, window_width);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}