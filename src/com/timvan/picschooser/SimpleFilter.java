/*-----------------------------------------------------------------------------+

			Filename			: PictureFilter.java
		
			Project				: fingerprint-recog
			Package				: application.gui.picturechooser

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

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class SimpleFilter extends FileFilter
{
	//---------------------------------------------------------- CONSTANTS --//

	//---------------------------------------------------------- VARIABLES --//	
	private String description;			// Filter description
	private String extension;			// Filter extension
	
	//------------------------------------------------------- CONSTRUCTORS --//	
	public SimpleFilter(String description, String extension)
	{
		// Copy values
		this.description = description;
		this.extension = extension;
	}
	//------------------------------------------------------------ METHODS --//	
	
	/**
	 * Indicates if the file is acceptable
	 * 
	 * @param file file to verify
	 */
	@Override
	public boolean accept(File file)
	{
		if(file.isDirectory()) 
		{ 
			return true; 
		} 
		String myFile = file.getName().toLowerCase(); 
		return myFile.endsWith(extension);
	}
	
	/** 
	 * Get the filter description
	 * @return the filter description
	 */
	@Override
	public String getDescription()
	{
		return description;
	}
	
	//---------------------------------------------------- PRIVATE METHODS --//
}
