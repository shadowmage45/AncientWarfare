/**
   Copyright 2012-2013 John Cummens (aka Shadowmage, Shadowmage4513)
   This software is distributed under the terms of the GNU General Public License.
   Please see COPYING for precise license information.

   This file is part of Ancient Warfare.

   Ancient Warfare is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   Ancient Warfare is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with Ancient Warfare.  If not, see <http://www.gnu.org/licenses/>.
 */
package shadowmage.template_converter;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class Converter
{

private static Converter converter;

public static void main(String[] args)
  {  
  converter = new Converter(args.length==0 ? null : new File(args[0]));
  converter.mainUpdateLoop();    
  }

private Display display;
private Shell shell;

File inputFile;
File outputFile;
int selection = 0;//0==inputFile, 1== outputFile

private Label selectionLabel;
private Label selectionName;

private Label outputLabel;
private Label outputName;

private Button inputBrowse;
private Button outputBrowse;

private Button convert;

private FileDialog fileSelectionGui;

private Converter(File file)
  {
  this.inputFile = file;
  display = new Display();  
  shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN );
  shell.setSize(480, 240);
  shell.setText("Stand-Alone Template Converter");
  shell.open();
    
  selectionLabel = new Label(shell, SWT.NONE);
  selectionLabel.setText("Current Selected Template: ");
  selectionLabel.setLocation(10, 10);
  selectionLabel.pack();
  
  selectionName = new Label(shell, SWT.BORDER);
  selectionName.setText(file==null ? "No Selection!!" : file.getName());
  Point p = selectionLabel.getLocation();
  p.x += selectionLabel.getSize().x + selectionLabel.getBorderWidth();
  selectionName.setLocation(p);  
  selectionName.pack();
  
  inputBrowse = new Button(shell, SWT.NONE);
  inputBrowse.setText("Browse for Input");
  inputBrowse.pack();
  inputBrowse.setLocation(shell.getSize().x - 10 - inputBrowse.getSize().x, selectionName.getLocation().y);
  inputBrowse.addMouseListener(new MouseListener()
    {
    @Override
    public void mouseDoubleClick(MouseEvent e){}

    @Override
    public void mouseDown(MouseEvent e)
      {
      if(fileSelectionGui==null)
        {
        selection = 0;
        fileSelectionGui = new FileDialog(shell);
        fileSelectionGui.open();
        handleFileSelection(new File(fileSelectionGui.getFilterPath(), fileSelectionGui.getFileName()));
        }
      }
    
    @Override
    public void mouseUp(MouseEvent e){}
    }); 
  
  outputLabel = new Label(shell, SWT.NONE);
  outputLabel.setLocation(10, 10+inputBrowse.getSize().y);
  outputLabel.setText("Output Selection: ");
  outputLabel.pack();
  
  outputName = new Label(shell, SWT.BORDER);
  outputName.setLocation(outputLabel.getLocation().x + outputLabel.getBorderWidth() + outputLabel.getSize().x, 10+inputBrowse.getSize().y);
  outputName.setText("No Selection!!");
  outputName.pack();
  
  outputBrowse = new Button(shell, SWT.NONE);
  outputBrowse.setText("Browse for Output");
  outputBrowse.pack();
  outputBrowse.setLocation(shell.getSize().x - 10 - outputBrowse.getSize().x, outputLabel.getLocation().y);  
  outputBrowse.addMouseListener(new MouseListener()
    {
    @Override
    public void mouseDoubleClick(MouseEvent e){}

    @Override
    public void mouseDown(MouseEvent e)
      {
      if(fileSelectionGui==null)
        {
        selection = 1;
        fileSelectionGui = new FileDialog(shell);
        fileSelectionGui.open();
        handleFileSelection(new File(fileSelectionGui.getFilterPath(), fileSelectionGui.getFileName()));
        }
      }
    
    @Override
    public void mouseUp(MouseEvent e){}
    });
  
  if(inputFile==null)
    {
    outputBrowse.setVisible(false);    
    }
  
  convert = new Button(shell, SWT.NONE);
  convert.setText("CONVERT");  
  convert.pack();
  int hx = shell.getSize().x/2;
  int hy = shell.getSize().y/2;
  convert.setLocation(-convert.getSize().x/2 + hx, -convert.getSize().y + hy);
  convert.setVisible(false);
  convert.addMouseListener(new MouseListener()
    {
    @Override
    public void mouseUp(MouseEvent e){}

    @Override
    public void mouseDown(MouseEvent e)
      {
      if(inputFile!=null && outputFile!=null)
        {
        doConversion();
        inputFile = null;
        outputFile = null;
        
        outputBrowse.setVisible(false);
        outputBrowse.pack();
        
        outputName.setText("No Selection!!");
        outputName.pack();
        
        selectionName.setText("No selection!!");
        selectionName.pack();
        
        convert.setVisible(false);
        convert.pack();
        }
      }
    @Override
    public void mouseDoubleClick(MouseEvent e){}
    });  
  }

private void mainUpdateLoop()
  {  
  while(!converter.shell.isDisposed())
    {
    if(!display.readAndDispatch())
      {
      display.sleep();
      }
    }
  display.dispose();  
  }

public void handleFileSelection(File file)
  {
  if(selection==0)
    {
    if(file!=null && file.exists())
      {
      outputBrowse.setVisible(true);
      inputFile = file;
      selectionName.setText(file.getName());
      selectionName.pack();
      }    
    }
  else
    {
    outputFile = file;
    outputName.setText(file == null? "No Selection!!" : file.getName());
    outputName.pack();
    }
  convert.setVisible(outputFile!=null && inputFile!=null);
  fileSelectionGui=null;
  }

private void doConversion()
  {
  if(inputFile==null || outputFile==null){return;}
  TemplateConverter converter = new TemplateConverter(inputFile, outputFile);
  try
    {
    converter.doConversion();
    } 
  catch (IOException e)
    {
    e.printStackTrace();
    }
  }

}
