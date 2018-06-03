/*-----------------------------------------------------------------------------+

			Filename			: PictureChooser.java
		
			Project				: fingerprint-recog
			Package				: application.gui.panels

			Developed by		: Thomas DEVAUX & Estelle SENAY
			                      (2007) Concordia University

							-------------------------

	This program is free software. You can redistribute it and/or modify it 
 	under the terms of the GNU Lesser General Public License as published by 
	the Free Software Foundation. Either version 2.1 of the License, or (at your 
    option) any later version.

	This program is distributed in the hope that it will be useful, but WITHOUT 
	ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
	FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for 
    more details.

+-----------------------------------------------------------------------------*/

package src.com.timvan.picschooser;

import javax.swing.JFileChooser;
import java.io.File;

public class PictureChooser extends JFileChooser
{

	
	/**
	 * Construct a picture chooser
	 */
	public PictureChooser()
	{
		// Initialize properties
		setMultiSelectionEnabled(false);
		setDialogTitle("选择一个表情包");
		
		// Initial position
	//	setCurrentDirectory(new java.io.File("./data"));
		setCurrentDirectory(new File(System.getProperty("user.dir") + "/pics"));

		// Set filters
		setFileSelectionMode(JFileChooser.FILES_ONLY);
		setAcceptAllFileFilterUsed(false);

		addChoosableFileFilter(new SimpleFilter("JPEG (*.jpg)",".jpg"));
		
		// Add picture previewer
		PicturePreviewPanel preview = new PicturePreviewPanel();
		setAccessory(preview);
		addPropertyChangeListener(preview);
	}

	//------------------------------------------------------------ METHODS --//	

	//---------------------------------------------------- PRIVATE METHODS --//
}
