package gui;

import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

public class comment_window extends abstract_window {
	
	comment_window local_comment_win;
	
	public comment_window(final Display display, final int movie_id) {
		super(display, SWT.SHELL_TRIM & (~SWT.RESIZE) & (~SWT.MAX));

		local_comment_win = this;
		this.setSize(250, 150);
		this.setText("Make a Comment");

		this.setLayout(new FormLayout());

		/* Window Background */
		String imgURL = ".\\src\\gui\\images\\blue_300.jpg";
		final Image background = new Image(display, imgURL);
		this.setBackgroundImage(background);
		this.setBackgroundMode(SWT.INHERIT_DEFAULT);

		/* comment text */
		final Text comment_text = new Text(this, SWT.WRAP);
		comment_text.setLayoutData(gui_utils.form_data_factory(245, 80, 0, 0));
		comment_text.setTextLimit(68);

		/* comment button */
		Button comment_button = new Button(this, SWT.PUSH);
		comment_button.setLayoutData(gui_utils
				.form_data_factory(70, 35, 85, 90));
		comment_button.setText("Comment");

		comment_button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent arg0) {

				Thread t = new Thread(new Runnable() {

					public void run() {

						display.asyncExec(new Runnable() {

							public void run() {
								try {

									if (log_in_window.user.add_movies_notes(
											movie_id, comment_text.getText()))

									{

										/* showing an informative message box */

										MessageBox messageBox = new MessageBox(
												display.getActiveShell(),
												SWT.ICON_WARNING);

										messageBox.setText("Success");

										messageBox
												.setMessage("Comment added succefully!");

										messageBox.open();
										
										comment_text.setText("");

									}

									else

									{
										MessageBox messageBox = new MessageBox(
												display.getActiveShell(),
												SWT.ICON_WARNING);

										messageBox.setText("Failure");

										messageBox.setMessage("Couldn't rate!");

										messageBox.open();

									}
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}

						});

					}

				});

				gui_utils.executor.execute(t);
			
			}

		});

		/* Disposal Listener */
		this.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {

				background.dispose();
			}
		});
	}
}
