package gui;


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



import config.config;
//import db.db_queries;
import bl.verifier;

public class social_tab extends Composite
{
	
	private Text friend_name_text;
	
	public social_tab(final Display display, Composite parent, int style)
	{
		super(parent, style);
		
		
		final List<String> user_friends;
		List<String> friends_activities_strings;
		List<Label> friends_activities_labels = new ArrayList<Label>();
		List<String> user_social_activities_strings;
		List<Label> user_social_activities_labels = new ArrayList<Label>();
		
		
		FormLayout form_layout_tab = new FormLayout();
		this.setLayout(form_layout_tab);
		Color color = display.getSystemColor(SWT.COLOR_GRAY);
		this.setBackground(color);
		color.dispose();
		
		
		//headline
		Label headline_label = new Label(this, SWT.NONE);
		headline_label.setText("Social Zone");
		FormData form_data_headline_label = new FormData();
		form_data_headline_label.top = new FormAttachment(0,2);
		form_data_headline_label.left = new FormAttachment(0,257);
		headline_label.setFont(new Font(display, "Ariel",15, java.awt.Font.PLAIN ));
		headline_label.setLayoutData(form_data_headline_label);	
		
		
		//add friend area
		Composite add_friend_area = new Composite(this, SWT.NONE);
		FormData form_data_add_friend_area = new FormData(295, 185); 
		form_data_add_friend_area.top = new FormAttachment(0, 30);
		form_data_add_friend_area.left = new FormAttachment(0, 10);
		add_friend_area.setLayoutData(form_data_add_friend_area);
		GridLayout grid_layout_add_friend_area = new GridLayout(1, false);
		add_friend_area.setLayout(grid_layout_add_friend_area);
				
				
		//add friend headline
		Label add_friend_headline = new Label(add_friend_area, SWT.NONE);
		add_friend_headline.setText("Add a Friend");
		GridData grid_data_add_friend_headline = new GridData();
		grid_data_add_friend_headline.horizontalIndent = 75;
		add_friend_headline.setFont(new Font(display, "Ariel",14, java.awt.Font.PLAIN ));
		add_friend_headline.setLayoutData(grid_data_add_friend_headline);
		
		
		//add friend text
		final Text add_friend_text = new Text(add_friend_area, SWT.BORDER);
		GridData grid_data_add_friend_text = new GridData(100, 15);
		grid_data_add_friend_text.horizontalIndent = 74;
		grid_data_add_friend_text.verticalIndent = 25;
		add_friend_text.setLayoutData(grid_data_add_friend_text);
		
		
		//add friend BUTTON
		Button add_friend_button = new Button(add_friend_area, SWT.PUSH);
		GridData grid_data_add_friend_button = new GridData(70, 35);
		grid_data_add_friend_button.horizontalIndent = 95;
		grid_data_add_friend_button.verticalIndent = 8;
		add_friend_button.setText("Add");
		add_friend_button.setLayoutData(grid_data_add_friend_button);
		
		add_friend_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(!verifier.verifyname(add_friend_text.getText())){ 
					MessageBox alertBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
					alertBox.setText("Illegal Username");
					alertBox.setMessage("Friend name length is 1-10 chars \n Only letters or numbers allowed.");
					alertBox.open();
				}


//				else if(does_user_exists(friend_name_text.getText())){ //to be implemented next on
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

		
		
		
		//remove friend area
		Composite remove_friend_area = new Composite(this, SWT.NONE);
		FormData form_data_remove_friend_area = new FormData(295, 185); 
		form_data_remove_friend_area.top = new FormAttachment(0, 220);
		form_data_remove_friend_area.left = new FormAttachment(0, 10);
		remove_friend_area.setLayoutData(form_data_remove_friend_area);
		GridLayout grid_layout_remove_friend_area = new GridLayout(2, false);
		remove_friend_area.setLayout(grid_layout_remove_friend_area);
				
				
		//remove friend headline
		Label remove_friend_headline = new Label(remove_friend_area, SWT.NONE);
		remove_friend_headline.setText("Remove a Friend");
		GridData grid_data_remove_friend_headline = new GridData();
		grid_data_remove_friend_headline.horizontalIndent = 55;
		grid_data_remove_friend_headline.horizontalSpan = 2;
		remove_friend_headline.setFont(new Font(display, "Ariel",14, java.awt.Font.PLAIN ));
		remove_friend_headline.setLayoutData(grid_data_remove_friend_headline);
		
		
		
		
		//remove friend combo
		final Combo remove_friend_combo = new Combo(remove_friend_area, SWT.DROP_DOWN);
		GridData grid_data_remove_friend_combo = new GridData(100, 15);
		grid_data_remove_friend_combo.horizontalIndent = 30;
		grid_data_remove_friend_combo.verticalIndent = 25;
		remove_friend_combo.setLayoutData(grid_data_remove_friend_combo);
		
		//user_friends = get_user_friends();   will be used when function exists
		
		//just for check
		user_friends = new ArrayList<String>();
		user_friends.add("user1");
		user_friends.add("user2");
		user_friends.add("another user");
		//
		
		String[] user_friends_arr = user_friends.toArray(new String[user_friends.size()]);
		remove_friend_combo.setItems(user_friends_arr);
		
		//remove friend button
		Button remove_friend_button = new Button(remove_friend_area, SWT.PUSH);
		GridData grid_data_remove_friend_button = new GridData(70, 35);
		grid_data_remove_friend_button.horizontalIndent = 15;
		grid_data_remove_friend_button.verticalIndent = 25;
		remove_friend_button.setText("Remove");
		remove_friend_button.setLayoutData(grid_data_remove_friend_button);
		
		
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


			//	else if(remove_friend( get_current_user_id(),remove_friend_combo.getText())){ //to be implemented next on
//				MessageBox alertBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
//				alertBox.setText("Success");
//				alertBox.setMessage("Friend has been removed!");
//				alertBox.open();
//						
				
//
//
//					});
//				}
//				else //error during removing
//				{
//					MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING);
//					messageBox.setText("Error");
//					messageBox.setMessage("Couldn't remove friend!");
//					messageBox.open();
//				}
			}

		});

		
		
		
		
		//user recent social activity area
		Composite user_social_activity_area = new Composite(this, SWT.NONE);
		FormData form_data_user_social_activity_area = new FormData(295, 185); 
		form_data_user_social_activity_area.top = new FormAttachment(0, 30);
		form_data_user_social_activity_area.left = new FormAttachment(0, 310);
		user_social_activity_area.setLayoutData(form_data_user_social_activity_area);
		GridLayout grid_layout_user_social_activity_area = new GridLayout(1, false);
		user_social_activity_area.setLayout(grid_layout_user_social_activity_area);
		
		//user recent activity headline
		Label user_social_activity_headline = new Label(user_social_activity_area, SWT.NONE);
		user_social_activity_headline.setText("Your Recent Social Activity");
		GridData grid_data_user_social_activity_headline = new GridData();
		grid_data_user_social_activity_headline.verticalIndent = 0;
		user_social_activity_headline.setFont(new Font(display, "Ariel",14, java.awt.Font.PLAIN ));
		user_social_activity_headline.setLayoutData(grid_data_user_social_activity_headline);
		
		
		
		//user recent social activities labels
		
		//user_activities_strings = get_user_recent_social_activities(); //to be used when function exists
				
		//just for check
		user_social_activities_strings = new ArrayList<String>();
		user_social_activities_strings.add("recent activity 1");
		user_social_activities_strings.add("recent activity 2");
		user_social_activities_strings.add("recent activity 3");
		user_social_activities_strings.add("recent activity 4");
		user_social_activities_strings.add("recent activity 5");
		user_social_activities_strings.add("recent activity 6");
		//
				
		int i = 0;
		for(String str: user_social_activities_strings)
		{
			user_social_activities_labels.add(new Label(user_social_activity_area, SWT.NONE));
			user_social_activities_labels.get(i).setText(user_social_activities_strings.get(i));
			user_social_activities_labels.get(i).setFont(new Font(display, "Ariel", 12, java.awt.Font.PLAIN));
			i++;
		}
		
		
		
		
		//friends recent activity area
		Composite friends_activity_area = new Composite(this, SWT.NONE);
		FormData form_data_friends_activity_area = new FormData(295, 185); 
		form_data_friends_activity_area.top = new FormAttachment(0, 220);
		form_data_friends_activity_area.left = new FormAttachment(0, 310);
		friends_activity_area.setLayoutData(form_data_friends_activity_area);
		GridLayout grid_layout_friends_activity_area = new GridLayout(1, false);
		friends_activity_area.setLayout(grid_layout_friends_activity_area);
		
		
		//friends recent activity headline
		Label friends_activity_headline = new Label(friends_activity_area, SWT.NONE);
		friends_activity_headline.setText("Your Friends Recent Activity");
		GridData grid_data_friends_activity_headline = new GridData();
		grid_data_friends_activity_headline.verticalIndent = 0;
		friends_activity_headline.setFont(new Font(display, "Ariel",14, java.awt.Font.PLAIN ));
		friends_activity_headline.setLayoutData(grid_data_friends_activity_headline);
		
		
		
		//friends recent activities labels
		
		//friends_activities_strings = get_friends_recent_activities(); //to be used when function exists
		
		//just for check
		friends_activities_strings = new ArrayList<String>();
		friends_activities_strings.add("friend recent activity 1");
		friends_activities_strings.add("friend recent activity 2");
		friends_activities_strings.add("friend recent activity 3");
		friends_activities_strings.add("friend recent activity 4");
		friends_activities_strings.add("friend recent activity 5");
		friends_activities_strings.add("friend recent activity 6");
		//
		
		i = 0;
		for(String str: friends_activities_strings)
		{
			friends_activities_labels.add(new Label(friends_activity_area, SWT.NONE));
			friends_activities_labels.get(i).setText(friends_activities_strings.get(i));
			friends_activities_labels.get(i).setFont(new Font(display, "Ariel", 12, java.awt.Font.PLAIN));
			i++;
		}
		
		
	}
	
	
	
}
