package fr.dauphine.javaavance.phineloops;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import org.vaadin.hezamu.canvas.Canvas;
//import humbleloop.src.main.java.fr.dauphine.javaavance.phineloops.*;
import java.io.*;


/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

  private Canvas canvas;
  private VerticalLayout layout;
  private VerticalLayout layoutRight;
  protected String fileName;
  protected SquareGrid squareGrid;

  @Override
  protected void init(VaadinRequest vaadinRequest) {

    class ImageUploader implements Upload.Receiver {
      public File file;

      public OutputStream receiveUpload(String filename, String mimeType) {
        FileOutputStream fos = null;
        try {
          file = new File(filename);
          fos = new FileOutputStream(file);
          fileName = filename;
        } catch (final java.io.FileNotFoundException e) {
          return null;
        }
        return fos;
      }
    };

    layout = new VerticalLayout();

    ImageUploader receiver = new ImageUploader();

    Upload upload = new Upload("Upload it here", receiver);
    upload.addFinishedListener(new Upload.FinishedListener() {
      @Override
      public void uploadFinished(Upload.FinishedEvent finishedEvent) {
        draw(fileName);
      }
    });
    upload.setImmediateMode(false);

    layout.addComponent(upload);

    layout.setHeight("130%");
    layout.setWidth("900px");
    layout.setSpacing(true);
    layout.setMargin(true);
    setContent(layout);

    canvas = new Canvas();
    layout.addComponent(canvas);
    canvas.setWidth(900 + "px");
    canvas.setHeight(900 + "px");
    canvas.setSizeFull();
    canvas.setFillStyle("#FFF");
    canvas.fillRect(0, 0, 700, 700);
    layout.setExpandRatio(canvas, 1);

  }

  public void draw(String filename) {

    SquareGrid squareGrid = new SquareGrid(filename);
    canvas.setFillStyle("#000");
    canvas.fillRect(0, 0, 700, 700);
    int size = Math.min(600 / squareGrid.getCols(), 600 / squareGrid.getRows());
    int xPos = 350 - size * squareGrid.getCols() / 2;
    int yPos = 350 - size * squareGrid.getRows() / 2;
    if((xPos<0)||(yPos<0)||(size<5)){
      canvas.setFillStyle("#FFF");
      canvas.fillRect(0, 0, 700, 700);
      canvas.setFillStyle("#F00");
      canvas.fillText("The grid is too large, cannot be display", 300, 300,500);
    }
    else {
      for (int k = 0; k < squareGrid.getCells().size(); k++) {
        canvas.setFillStyle("#FFF");
        canvas.fillRect(xPos, yPos, size, size);
        canvas.setFillStyle("#F00");
        if (squareGrid.getCells().get(k).getValue().getConnections().get(Math.floorMod(0 - squareGrid.getCells().get(k).getValue().offset, 4))) {
          canvas.fillRect(xPos + 2 * size / 5, yPos, size / 5, 3 * size / 5);
        }
        if (squareGrid.getCells().get(k).getValue().getConnections().get(Math.floorMod(1 - squareGrid.getCells().get(k).getValue().offset, 4))) {
          canvas.fillRect(xPos + 2 * size / 5, yPos + 2 * size / 5, 3 * size / 5, size / 5);
        }
        if (squareGrid.getCells().get(k).getValue().getConnections().get(Math.floorMod(2 - squareGrid.getCells().get(k).getValue().offset, 4))) {
          canvas.fillRect(xPos + 2 * size / 5, yPos + 2 * size / 5, size / 5, 3 * size / 5);
        }
        if (squareGrid.getCells().get(k).getValue().getConnections().get(Math.floorMod(3 - squareGrid.getCells().get(k).getValue().offset, 4))) {
          canvas.fillRect(xPos, yPos + 2 * size / 5, 3 * size / 5, size / 5);
        }
        if (((k + 1) % squareGrid.getCols()) == 0) {
          yPos += size;
          xPos = 350 - size * squareGrid.getCols() / 2;
        } else {
          xPos += size;
        }
      }
    }
  }

  @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
  @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
  public static class MyUIServlet extends VaadinServlet {
  }
}
