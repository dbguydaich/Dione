package gui;

import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;

public class gui_utils
{
	
	/*
	 * -1 for disabling any parameter
	 */
	public static GridData grid_data_factory(int width, int height, int horizontalIndent, int verticalIndent, int horizontalSpan, int verticalSpan, 
											int horizontalAlignment, int verticalAlignment)
	{
		
		GridData my_grid;
		
		if(width != -1 && height != -1)
			my_grid = new GridData(width, height);
		else
			my_grid = new GridData();
		
		if(verticalIndent != -1)
			my_grid.verticalIndent = verticalIndent;
		if(horizontalIndent != -1)
			my_grid.horizontalIndent = horizontalIndent;
		if(horizontalSpan != -1)
			my_grid.horizontalSpan = horizontalSpan;
		if(verticalSpan != -1)
			my_grid.verticalSpan = verticalSpan;
		if(horizontalAlignment != -1)
			my_grid.horizontalAlignment = horizontalAlignment;
		if(verticalAlignment != -1)
			my_grid.verticalAlignment = verticalAlignment;
		
		return my_grid;
		
	}
	
	
	
	public static GridData grid_data_factory(int horizontalIndent, int verticalIndent, int horizontalSpan, int verticalSpan, 
			int horizontalAlignment, int verticalAlignment)
	{
	
		GridData my_grid = new GridData();
		
		if(verticalIndent != -1)
			my_grid.verticalIndent = verticalIndent;
		if(horizontalIndent != -1)
			my_grid.horizontalIndent = horizontalIndent;
		if(horizontalSpan != -1)
			my_grid.horizontalSpan = horizontalSpan;
		if(verticalSpan != -1)
			my_grid.verticalSpan = verticalSpan;
		if(horizontalAlignment != -1)
			my_grid.horizontalAlignment = horizontalAlignment;
		if(verticalAlignment != -1)
			my_grid.verticalAlignment = verticalAlignment;
		
		return my_grid;
	
	}
	
	
	/*
	 * expects a valid width and height parameters.
	 * -1 for disabling other parameters
	 */
	public static FormData form_data_factory(int width, int height, int top, int left)
	{
		FormData my_form;
		
		if(width != -1 && height != -1)
			my_form = new FormData(width, height);
		else
		{
			my_form = new FormData();
			if(width != -1)
				my_form.width = width;
			if(height != -1)
				my_form.height = height;
		}
		
		if(top != -1)
			my_form.top = new FormAttachment(0, top);
		if(left != -1)
			my_form.left = new FormAttachment(0, left);
		
		return my_form;
	}
	
	
	
	
}
