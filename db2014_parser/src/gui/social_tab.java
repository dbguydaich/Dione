package gui;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;


//////////functions the tab is waiting for: //////////////

//List<String> get_user_friends()
//List<String> get_user_recent_social_activity()
//List<String> get_friends_recent_activities()
//List<String> get_user_recent_social_activities()


////////Listeners to be implemented: //////////////

/// add_friend_button, remove_friend_button
















import parser_entities.entity_person;
import config.config;
import db.db_queries_user;
import bl.user_logics;
//import db.db_queries;
import bl.verifier;

public class social_tab extends Composite
{
	
	private Text friend_name_text;
	private Combo remove_friend_combo;
	
	public social_tab(final Display display, Composite parent, int style)
	{
		super(parent, style);
		
		
		List<String> user_friends = null;
		List<String> friends_activities_strings = null;
		List<Label> friends_activities_labels = new ArrayList<Label>();
		List<String> user_social_activities_strings = null;
		List<Label> user_social_activities_labels = new ArrayList<Label>();
		
		
		FormLayout form_layout_tab = new FormLayout();
		this.setLayout(form_layout_tab);
		
		//window background
		String imgURL = ".\\src\\gui\\images\\blue_640_480_3.jpg";
		final Image background = new Image(display, imgURL);
		this.setBackgroundImage(background);
		this.setBackgroundMode(SWT.INHERIT_DEFAULT);
		this.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				background.dispose();
			}		
		});
				

		
		//headline
		Label headline_label = new Label(this, SWT.NONE);
		headline_label.setText("Social Zone");
		final Font font_headline_label = new Font(display, "Ariel", 21, SWT.BOLD );
		headline_label.setFont(font_headline_label);
		headline_label.setLayoutData(gui_utils.form_data_factory(-1, -1, 2, 225));
		headline_label.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_headline_label.dispose();
			}		
		});
		
		
		
		//add friend area
		Composite add_friend_area = new Composite(this, SWT.NONE);
		add_friend_area.setLayoutData(gui_utils.form_data_factory(360, 85, 35, 135));
		GridLayout grid_layout_add_friend_area = new GridLayout(3, false);
		add_friend_area.setLayout(grid_layout_add_friend_area);
				
				
		
		//add friend headline
		Label add_friend_headline = new Label(add_friend_area, SWT.NONE);
		add_friend_headline.setText("Add a Friend");
		final Font font_friend_headline = new Font(display, "Ariel",12, SWT.BOLD );
		add_friend_headline.setFont(font_friend_headline);
		add_friend_headline.setLayoutData(gui_utils.grid_data_factory(5, -1, -1, -1, -1, -1));
		add_friend_headline.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_friend_headline.dispose();
			}		
		});
		
		
		
		//add friend text
		final Text add_friend_text = new Text(add_friend_area, SWT.NONE);
		add_friend_text.setLayoutData(gui_utils.grid_data_factory(120, 20, 10, 5, -1, -1, -1, -1));
		final Font font_add_text = new Font(display, "Ariel",10, SWT.NONE );
		add_friend_text.setFont(font_add_text);
		add_friend_text.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_add_text.dispose();
			}		
		});
		
		
		//add friend BUTTON
		Button add_friend_button = new Button(add_friend_area, SWT.PUSH);
		add_friend_button.setText("Add");
		add_friend_button.setLayoutData(gui_utils.grid_data_factory(60, 30, 10, 5, -1, -1, -1, -1));
		
		add_friend_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(!verifier.verifyname(add_friend_text.getText())){ 
					MessageBox alertBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
					alertBox.setText("Illegal Username");
					alertBox.setMessage("Friend name length is 1-10 chars \n Only letters or numbers allowed.");
					alertBox.open();
				} else
					try {
						System.out.println(add_friend_text.getText());
						
						
						if(user_logics.get_my_name() == add_friend_text.getText())
						{
							MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
							messageBox.setText("Error");
							messageBox.setMessage("You can not be a friend of your self. Sorry.");
							messageBox.open();
							
						}
						
						
						////// implement //// matan please privede the function
						//check that i am not trying to add myself..
						
						
						if(user_logics.does_user_exists((add_friend_text.getText()))){ //to be implemented next on
						
							Integer friend_id = user_logics.get_user_id(add_friend_text.getText());
							Integer current_user_id =   log_in_window.user.get_current_user_id() ;
							user_logics.add_friendship(friend_id, current_user_id);
							MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
							messageBox.setText("SUCCESS");
							messageBox.setMessage("Friend has successfully added");
							messageBox.open();
							update_friends_ddl();
			
						
						
						}

						else//no user found
						{
							MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
							messageBox.setText("No user Found");
							messageBox.setMessage("No user Found");
							messageBox.open();
						}
					} catch (SQLException e) {
						gui_utils.raise_sql_error_window(display);
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}

		});

		
		
	
		
				
		//remove friend headline
		Label remove_friend_headline = new Label(add_friend_area, SWT.NONE);
		remove_friend_headline.setText("Remove a Friend");
		final Font font_remove_friend_headline = new Font(display, "Ariel",12, SWT.BOLD ); 
		remove_friend_headline.setFont(font_remove_friend_headline);
		remove_friend_headline.setLayoutData(gui_utils.grid_data_factory(5, -1, -1, -1, -1, -1));
		remove_friend_headline.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_remove_friend_headline.dispose();
			}		
		});
		
		
		
		//remove friend combo
		remove_friend_combo = new Combo(add_friend_area, SWT.DROP_DOWN);
		remove_friend_combo.setLayoutData(gui_utils.grid_data_factory(100, 15, 10, 5, -1, -1, -1, -1));
		
		
		user_friends = new ArrayList<String>();
		
		try {
			user_friends = log_in_window.user.get_current_user_friends_names();
		} catch (SQLException e2) {
			gui_utils.raise_sql_error_window(display);
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}  // will be used when function exists
		

		final String[] user_friends_arr = user_friends.toArray(new String[user_friends.size()]);//////////////////////////////
		remove_friend_combo.setItems(user_friends_arr);
		
		//remove friend button
		Button remove_friend_button = new Button(add_friend_area, SWT.PUSH);
		remove_friend_button.setText("Remove");
		remove_friend_button.setLayoutData(gui_utils.grid_data_factory(60, 30, 10, 5, -1, -1, -1, -1));
		
		remove_friend_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				
				if( remove_friend_combo.getItems().length==0){ 
					MessageBox alertBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
					alertBox.setText("No friends");
					alertBox.setMessage("You have no friends!");
					alertBox.open();
				}
				
				else if( remove_friend_combo.getText()==""){ 
					MessageBox alertBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
					alertBox.setText("Illegal Username");
					alertBox.setMessage("Please select a friend");
					alertBox.open();
				}


				else 
				{
					
					Integer friend_id;
					try {
						friend_id = log_in_window.user.get_user_id(remove_friend_combo.getText());
						Integer current_user_id =  log_in_window.user.get_current_user_id();
						try {
							if(log_in_window.user.remove_friendship( current_user_id, friend_id)) //to be implemented next on
							{
								
								MessageBox alertBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
								alertBox.setText("Success");
								alertBox.setMessage("Friend has been removed!");
								alertBox.open();
								update_friends_ddl();
										
							}
							
							
							
							
							else //error during removing
							{
								MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
								messageBox.setText("Error");
								messageBox.setMessage("Couldn't remove friend!");
								messageBox.open();
							}
						} catch (SQLException e) {
							gui_utils.raise_sql_error_window(display);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} catch (SQLException e1) {
						gui_utils.raise_sql_error_window(display);
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	
				
				}

		}});

		
		
		//user recent social activity area
		Composite user_social_activity_area = new Composite(this, SWT.NONE);
		user_social_activity_area.setLayoutData(gui_utils.form_data_factory(600, 145, 120, 10));
		GridLayout grid_layout_user_social_activity_area = new GridLayout(1, false);
		user_social_activity_area.setLayout(grid_layout_user_social_activity_area);
		
		
		//user recent activity headline
		Label user_social_activity_headline = new Label(user_social_activity_area, SWT.NONE);
		user_social_activity_headline.setText("Your Recent Social Activity");
		final Font font_user_social_activity_headline = new Font(display, "Ariel",14, SWT.BOLD ); 
		user_social_activity_headline.setFont(font_user_social_activity_headline);
		user_social_activity_headline.setLayoutData(gui_utils.grid_data_factory(180, 0, -1, -1, -1, -1));
		user_social_activity_headline.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_user_social_activity_headline.dispose();
			}		
		});
		
		
		
		//user recent social activities labels
		
		
		try {
			user_social_activities_strings = log_in_window.user.get_user_recent_string_activities(log_in_window.user.get_current_user_id(), 5) ;
		} catch (SQLException e1) {
			user_social_activities_strings = new ArrayList<String>();
			gui_utils.raise_sql_error_window(display);
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} //to be used when function exists
				
		//just for check
//		user_social_activities_strings = new ArrayList<String>();
//		user_social_activities_strings.add("recent activity 1");
//		user_social_activities_strings.add("recent activity 2");
//		user_social_activities_strings.add("recent activity 3");
//		user_social_activities_strings.add("recent activity 4");
//		user_social_activities_strings.add("recent activity 5");
//		user_social_activities_strings.add("recent activity 6");
		//
				
		final Font font_user_social_activities_labels = new Font(display, "Ariel", 11, SWT.NONE);
		int i = 0;
		for(String str: user_social_activities_strings)
		{
			user_social_activities_labels.add(new Label(user_social_activity_area, SWT.NONE));
			user_social_activities_labels.get(i).setText(str);
			user_social_activities_labels.get(i).setFont(font_user_social_activities_labels);
			
			if(i == 0)
			{
				user_social_activities_labels.get(i).addDisposeListener(new DisposeListener()
				{
					public void widgetDisposed(DisposeEvent e) 
					{
						font_user_social_activities_labels.dispose();
					}		
				});
			}
			
			i++;
		}
		
		if(i == 0)
			font_user_social_activities_labels.dispose();
		
		
		
		
		//friends recent activity area
		Composite friends_activity_area = new Composite(this, SWT.NONE);
		friends_activity_area.setLayoutData(gui_utils.form_data_factory(600, 145, 270, 10));
		GridLayout grid_layout_friends_activity_area = new GridLayout(1, false);
		friends_activity_area.setLayout(grid_layout_friends_activity_area);
		
		
		//friends recent activity headline
		Label friends_activity_headline = new Label(friends_activity_area, SWT.NONE);
		friends_activity_headline.setText("Your Friends Recent Activity");
		final Font font_friends_activity_headline = new Font(display, "Ariel",14, SWT.BOLD );
		friends_activity_headline.setFont(font_friends_activity_headline);
		friends_activity_headline.setLayoutData(gui_utils.grid_data_factory(180, -1, -1, -1, -1, -1));
		friends_activity_headline.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e) 
			{
				font_friends_activity_headline.dispose();
			}		
		});
		
		
		//friends recent activities labels
		friends_activities_strings = new ArrayList<String>();
	
		try {
			friends_activities_strings = log_in_window.user.get_friends_recent_string_activities(); //////matan///// it returns null
			if (friends_activities_strings ==null)  // fix meanwhile 
			{

				friends_activities_strings = new ArrayList<String>();
			}
		} catch (SQLException e1) {
			gui_utils.raise_sql_error_window(display);
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} //to be used when function exists
		
		//just for check
//		friends_activities_strings = new ArrayList<String>();
//		friends_activities_strings.add("friend recent activity 1");
//		friends_activities_strings.add("friend recent activity 2");
//		friends_activities_strings.add("friend recent activity 3");
//		friends_activities_strings.add("friend recent activity 4");
//		friends_activities_strings.add("friend recent activity 5");
//		friends_activities_strings.add("friend recent activity 6");
		//
		
		final Font font_friends_activities_labels = new Font(display, "Ariel", 11, SWT.NONE);
		i = 0;
		for(String str: friends_activities_strings)
		{
			friends_activities_labels.add(new Label(friends_activity_area, SWT.NONE));
			friends_activities_labels.get(i).setText(str);
			friends_activities_labels.get(i).setLayoutData(gui_utils.grid_data_factory(-1, -1, -1, -1, -1, -1));
			friends_activities_labels.get(i).setFont(font_friends_activities_labels);
			if(i == 0)
			{
				friends_activities_labels.get(i).addDisposeListener(new DisposeListener()
				{
					public void widgetDisposed(DisposeEvent e) 
					{
						font_friends_activities_labels.dispose();
					}		
				});
			}
			
			i++;
		}
		
		if(i == 0)
			font_friends_activities_labels.dispose();
		
	
	}

	
	
	
	
	
private void update_friends_ddl()
{
	System.out.println("eneterd function");
	List<String> user_friends = new ArrayList<String>();
	
	try {
		user_friends = log_in_window.user.get_current_user_friends_names();
	} catch (SQLException e2) {
	
	}  
	System.out.println(user_friends);
	final String[] user_friends_arr = user_friends.toArray(new String[user_friends.size()]);//////////////////////////////
	remove_friend_combo.setItems(user_friends_arr);
	
	
}
	
}
