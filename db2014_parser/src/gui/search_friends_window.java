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
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;




import config.config;
import db.db_queries;
//import db.db_queries;
import bl.verifier;


public class search_friends_window extends Composite {
	//private Text friend_name_text;
	
	//data//
	config config1 = new config();
	int window_height = config1.get_window_height();
	int window_width = config1.get_window_width();
	

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

	/**
	 * Create the shell.
	 * @param display
	 */
	public search_friends_window(final Display display, Composite parent, int style) {
		super(parent, style);
		
		
		this.setSize(window_width, window_height);
		FormLayout form_layout_tab = new FormLayout();
		this.setLayout(form_layout_tab);
		
		
		//headline
		Label headline_label = new Label(this, SWT.NONE);
		headline_label.setText("Add Friend:");
		FormData form_data_headline_label = new FormData();
		form_data_headline_label.top = new FormAttachment(0,10);
		form_data_headline_label.left = new FormAttachment(0,30);
		headline_label.setFont(new Font(display, "Ariel",15, java.awt.Font.PLAIN ));
		headline_label.setLayoutData(form_data_headline_label);		
		
		//this.setBackgroundImage(new Image(display,"search_friends_window.png"));
		
		//left area
		Composite left_area = new Composite(this, SWT.BORDER);
		FormData form_data_left_area = new FormData(150, 50); 
		form_data_left_area.top = new FormAttachment(0, 40);
		form_data_left_area.left = new FormAttachment(0, 30);
		left_area.setLayoutData(form_data_left_area);
		GridLayout grid_layout_left_area = new GridLayout(2, false);
		left_area.setLayout(grid_layout_left_area);
		
		
		//search text
		final Text search_text = new Text(left_area, SWT.BORDER);
		GridData grid_data_search_text = new GridData();
		grid_data_search_text.verticalIndent = 10;
		search_text.setLayoutData(grid_data_search_text);
		
		final String friendName = search_text.getText();
		//add button
		Button add_button = new Button(left_area, SWT.PUSH);
		GridData grid_data_add_button = new GridData();
		grid_data_add_button.verticalIndent = 10;
		grid_data_add_button.horizontalIndent = 10;
		add_button.setLayoutData(grid_data_add_button);
		add_button.setText("Add");
		add_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(!verifier.verifyname(search_text.getText())){ 
					MessageBox alertBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
					alertBox.setText("Illegal Username");
					alertBox.setMessage("Friend name length is 1-10 chars \n Only letters or numbers allowed.");
					alertBox.open();
				}


				else if(db_queries.does_user_exists(friendName)){ //to be implemented next on
				


					
				}
				else //no user found
				{
					MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
					messageBox.setText("No user Found");
					messageBox.setMessage("No user Found. Please try again.");
					messageBox.open();
				}
			}

		});


	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}