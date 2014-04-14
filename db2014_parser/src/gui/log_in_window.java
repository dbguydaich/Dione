//package gui;
//
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.events.SelectionAdapter;
//import org.eclipse.swt.events.SelectionEvent;
//import org.eclipse.swt.graphics.Color;
//import org.eclipse.swt.graphics.Font;
//import org.eclipse.swt.layout.FormAttachment;
//import org.eclipse.swt.layout.FormData;
//import org.eclipse.swt.layout.FormLayout;
//import org.eclipse.swt.layout.GridData;
//import org.eclipse.swt.layout.GridLayout;
//import org.eclipse.swt.widgets.Button;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.swt.widgets.Label;
//import org.eclipse.swt.widgets.MessageBox;
//import org.eclipse.swt.widgets.Shell;
//import org.eclipse.swt.widgets.Text;
//
//import bl.verifier;
//import runnableLogic.AddUser;
//import viewModelLayer.InputVerifier;
//
////import db.IdbOparations;
//
//
//public class log_in_window extends Shell
//{
//	public static void main(String args[])
//	{
//		Display display = new Display();
//		log_in_window a = new log_in_window(display);
//		a.open();
//		 while (!display.isDisposed()) {
//			 if (!display.readAndDispatch()) {
//			 display.sleep();
//			 }
//		 }
//		
//	}
////	public Shell shell;
//	public log_in_window(final Display display)
//	{
//		super(display);
//		
//		int window_height = 480;
//		int window_width = 640;
//		//shell = new Shell(display);
//		this.setSize(window_width, window_height);
//		Color log_in_window_color = display.getSystemColor(SWT.COLOR_DARK_CYAN);
//		this.setBackground(log_in_window_color);
//		
//		this.setLayout(new FormLayout());
//		
//		//area
//		Composite area = new Composite(this, SWT.NONE);
//		FormData form_data = new FormData(300, 300); 
//		form_data.top = new FormAttachment(0, 70);
//		form_data.left = new FormAttachment(0, 160);
//		area.setLayoutData(form_data);
//		GridLayout grid_layout_area = new GridLayout(2, false);
//		area.setLayout(grid_layout_area);
//		
//		//headline label
//		Label log_in_label = new Label(area, SWT.NONE);
//		log_in_label.setText("    Log In Or Sign Up:");
//		GridData grid_data_log_in_label = new GridData();
//		grid_data_log_in_label.horizontalSpan = 2;
//		grid_data_log_in_label.horizontalAlignment = SWT.CENTER;
//		grid_data_log_in_label.verticalAlignment = SWT.CENTER;
//		log_in_label.setLayoutData(grid_data_log_in_label);
//		log_in_label.setFont(new Font(display, "Ariel",20, java.awt.Font.PLAIN ));
//		
//		
//		//label username
//		Label username_label = new Label(area, SWT.NONE);
//		username_label.setText("Username:");
//		GridData grid_data_username_label = new GridData();
//		grid_data_username_label.horizontalIndent = 40;
//		username_label.setFont(new Font(display, "Ariel", 12, java.awt.Font.PLAIN));
//		username_label.setLayoutData(grid_data_username_label);
//		
//		
//		//text username
//		final Text username_text = new Text(area, SWT.BORDER);
//		GridData grid_data_username_text = new GridData(100,15);
//		grid_data_username_text.horizontalIndent = 0;
//		username_text.setLayoutData(grid_data_username_text);
//		username_text.setTextLimit(10);
//		
//		//label password
//		Label password_label = new Label(area, SWT.NONE);
//		password_label.setText("Password:");
//		GridData grid_data_password_label = new GridData();
//		grid_data_password_label.horizontalIndent = 40;
//		password_label.setFont(new Font(display, "Ariel", 12, java.awt.Font.PLAIN));
//		password_label.setLayoutData(grid_data_password_label);
//		
//		//text password
//		final Text password_text = new Text(area, SWT.PASSWORD | SWT.BORDER);
//		GridData grid_data_password_text = new GridData(100,15);
//		grid_data_password_text.horizontalIndent = 0;
//		password_text.setLayoutData(grid_data_password_text);
//		password_text.setTextLimit(6);
//		
//		//log in button
//		Button log_in_button = new Button(area, SWT.PUSH);
//		log_in_button.setText("Log In");
//		GridData grid_data_log_in_button = new GridData();
//		grid_data_log_in_button.horizontalIndent = 80;
//		log_in_button.setLayoutData(grid_data_log_in_button);
//		
//		//////
//		log_in_button.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent arg0) {
//				if(!verifier.verifyname(username_text.getText())){ // illegal user name
//					MessageBox alertBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
//					alertBox.setText("Illegal Username");
//					alertBox.setMessage("Friend name length is 1-10 chars \n Only letters or numbers allowed.");
//					alertBox.open();
//				}else if(!verifier.verifyname( password_text.getText())){ // invalid password
//					MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
//					messageBox.setText("Illegal Password");
//					messageBox.setMessage("Password must contain 1-6 alphanumeric chars.");
//					messageBox.open();
//				}
//
////				else if(valid_user(username_text.getText(),password_text.getText())){
////					display.syncExec(new AddUser(oparations, userNameText.getText(), passwordText.getText()){
////						@Override
////						public void run(){
////							super.run();
////							int result = this.getValue();
////							MessageBox messageBox = new MessageBox(display.getActiveShell(),SWT.ICON_INFORMATION);
////							if (result == 0){
////
////								messageBox.setText("Error");
////								messageBox.setMessage("Connection Error");
////								messageBox.open();
////
////							}else if(result == 1){
////								messageBox.setText("Welcome");
////								messageBox.setMessage("New user was added: " + userNameText.getText());
////								messageBox.open();
////
////								// open main menu
////								int idUser = userNameText.getText().hashCode();
////								dispose();
////								MainMenu MainMenuShell = new MainMenu(display,oparations,false, idUser);
////								MainMenuShell.open();
////								MainMenuShell.layout();
////								while (!MainMenuShell.isDisposed()) {
////									if (!display.readAndDispatch()) {
////										display.sleep();
////									}
////								}
////
////							}
////							else {
////								messageBox.setText("Warning");
////								messageBox.setMessage("User already exist : " + userNameText.getText());
////								messageBox.open();
////							}
////
////						}
////
////
////					});
////				}
//				else //passwords not identical
//				{
//					MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
//					messageBox.setText("Warning");
//					messageBox.setMessage("Passwords are not identical");
//					messageBox.open();
//				}
//			}
//
//		});
//		//////
//		
//		//sign up button
//		Button sign_up_button = new Button(area, SWT.PUSH);
//		sign_up_button.setText("Sign Up");
//		GridData grid_data_sign_up_button = new GridData();
//		grid_data_sign_up_button.horizontalIndent = 10;
//		sign_up_button.setLayoutData(grid_data_sign_up_button);
//		sign_up_button.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent arg0) {
//				if(!verifier.verifyname(username_text.getText())){ // illegal user name
//					MessageBox alertBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
//					alertBox.setText("Illegal Username");
//					alertBox.setMessage("Friend name length is 1-10 chars \n Only letters or numbers allowed.");
//					alertBox.open();
//				}else if(!verifier.verifyname( password_text.getText())){ // invalid password
//					MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
//					messageBox.setText("Illegal Password");
//					messageBox.setMessage("Password must contain 1-6 alphanumeric chars.");
//					messageBox.open();
//				}
//		
//		
//	}
//		
//	
//	protected void checkSubclass()
//	{
//	}
//	
//}
