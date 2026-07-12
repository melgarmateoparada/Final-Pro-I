package org.proIII.appManejoImagenes.GUI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.proIII.appManejoImagenes.command.Invoker;
import org.proIII.appManejoImagenes.image.ImagePixels;
import org.proIII.appManejoImagenes.postgres.DeleteImage;
import org.proIII.appManejoImagenes.postgres.LoadImage;
import org.proIII.appManejoImagenes.postgres.SaveImage;
import org.proIII.appManejoImagenes.postgres.data.Author;
import org.proIII.appManejoImagenes.postgres.data.AuthorPostgres;
import org.proIII.appManejoImagenes.postgres.data.TagImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class PrincipalFrame extends JFrame {
    private static final Logger logger = LogManager.getRootLogger();
    private JMenuBar menuBar;
    private JMenu menu, menuImage;
    private JMenuItem newPanel, deletePanel, deleteImage, newImage, modify, saveImage, exitApp;
    private PrincipalPanel principalPanel;
    private SecundaryPanel secundaryPanel;
    private Invoker invoker;
    private ImagePixels image;
    private Author author;
    private int id_author, id_tag;
    private TagImage tagImage;
    private String [] categoryTag;
    private String selection;
    private LoadImage loadImage;
    private  DeleteImage deleteImagePostgres;
    private SaveImage saveImagePostgres;
    public PrincipalFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        unit();
    }

    private void unit() {
        setLayout(new BorderLayout());
        createMenu();
        implementPanel();
        menuAccions();
        this.pack();
        this.setLocationRelativeTo(null);
        setVisible(true);
    }


    private void createMenu() {
        menuBar = new JMenuBar();
        menu = new JMenu("Options");
        menuImage = new JMenu("File");
        newPanel = new JMenuItem("New panel");
        deletePanel = new JMenuItem("Delete panel");
        newImage = new JMenuItem("New image");
        modify = new JMenuItem("Modify image");
        deleteImage = new JMenuItem("Delete image");
        saveImage = new JMenuItem("Save");
        exitApp = new JMenuItem("Exit");
        invoker = new Invoker();
        setJMenuBar(menuBar);
        menuBar.add(menuImage);
        menuBar.add(menu);
        menuImage.add(newImage);
        menuImage.add(modify);
        menuImage.add(deleteImage);
        menuImage.add(saveImage);
        menu.add(newPanel);
        menu.add(deletePanel);
        menu.add(exitApp);
    }

    private void menuAccions() {
        newPanel.addActionListener(e -> {
            addPanel();
        });
        deletePanel.addActionListener(e -> {
            deltePanel();
        });
        newImage.addActionListener(e -> {
            importImage();
        });
        saveImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selection = JOptionPane.showInputDialog(null,"New Image: 1 \n Image modified: 2");
                if(selection.equals("1")){
                    saveImage();
                }else if(selection.equals("2")){
                    saveImageModified();
                }else{
                    JOptionPane.showMessageDialog(null, "The option is a mistake");
                }
            }
        });
        deleteImage.addActionListener(e-> deleteImage());
        modify.addActionListener(e-> modifyImage());
        exitApp.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "See you");
            System.exit(0);
        });

    }

    private void saveImageModified() {
        saveImagePostgres = new SaveImage();
        DeleteImage deleteImagePostgres = new DeleteImage();

        try {
            String imageOldName = JOptionPane.showInputDialog(null, "Old image's name");
            int idImage = deleteImagePostgres.getImageIdByName(imageOldName);
            int idAuthor = loadImage.authorImage(imageOldName);

            if (idImage <= 0 || idAuthor <= 0) {
                JOptionPane.showMessageDialog(null, "Imagen o autor no encontrados. Verifique los datos ingresados.");
                return;
            }

            byte[] imageBytes = converToByte(image.getBi());
            if (imageBytes == null) {
                JOptionPane.showMessageDialog(null, "Error al procesar la imagen.");
                return;
            }

            String imageName = JOptionPane.showInputDialog(null, "Image's name: ");
            String imageDescription = JOptionPane.showInputDialog(null, "Description: ");

            saveImagePostgres.insertImagetoModified(idImage, idAuthor, imageBytes, imageName, imageDescription);
            JOptionPane.showMessageDialog(null, "Imagen modificada guardada exitosamente.");
        } catch (Exception e) {
            logger.error("Error al guardar la imagen modificada: ", e);
            JOptionPane.showMessageDialog(null, "Error al guardar la imagen modificada.");
        }
    }


    private void modifyImage() {
        loadImage = new LoadImage();
        String seletionModify = JOptionPane.showInputDialog(null, "1.- Image \n2.- Image modified");
      if(seletionModify!=null) {
          if (seletionModify.equals("1")) {
              String nameImage = JOptionPane.showInputDialog(null, "Enter your image's name");
              loadImage.loadImageToModify(nameImage);
              String passwordAuthor = JOptionPane.showInputDialog(null, "Enter your password");
              int id_author = loadImage.authorImage(nameImage);
              boolean passwordCorrect = loadImage.validateAuthorPassword(id_author, passwordAuthor);
              if (passwordCorrect) {
                  if (loadImage.getImage() != null) {
                      String path = "C:\\Users\\marco\\Documents\\Imagenes\\image.png";
                      try {
                          File imageFile;
                          imageFile = loadImage.byteArrayToFile(loadImage.getImage(), path);
                          newImage(imageFile);
                      } catch (IOException e) {
                          throw new RuntimeException(e);
                      }
                  }
              } else {
                  JOptionPane.showMessageDialog(null, "Password incorrect");
              }

          } else if (seletionModify.equals("2")) {
              String nameImage = JOptionPane.showInputDialog(null, "Enter your image's name");
              loadImage.loadImageFromModify(nameImage);
              String passwordAuthor = JOptionPane.showInputDialog(null, "Enter your password");
              int id_author = loadImage.authorImageModified(nameImage);
              boolean passwordCorrect = loadImage.validateAuthorPassword(id_author, passwordAuthor);
                if(!passwordCorrect){
                return;
                }
                if (passwordCorrect) {
                  if (loadImage.getImage() != null) {
                      String path = "C:\\Users\\marco\\Documents\\Imagenes\\image.png";
                      try {
                          File imageFile;
                          imageFile = loadImage.byteArrayToFile(loadImage.getImage(), path);
                          newImage(imageFile);
                      } catch (IOException e) {
                          throw new RuntimeException(e);
                      }
                  }
              } else {
                  JOptionPane.showMessageDialog(null, "Wrong password");
              }

          } else {
            JOptionPane.showMessageDialog(null, "Option isn't available");
          }
      }else {
          JOptionPane.showMessageDialog(null,"Selection empty");
      }
    }

    private void deleteImage() {
        LoadImage loadImage = new LoadImage();
        deleteImagePostgres = new DeleteImage();

        String nameImageToDelete = JOptionPane.showInputDialog(null, "Enter the name of the image to delete:");
        if (nameImageToDelete == null || nameImageToDelete.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Image name cannot be empty.");
            return;
        }
        int id_author = loadImage.authorImage(nameImageToDelete);
        if (id_author <= 0) {
            JOptionPane.showMessageDialog(null, "The image does not exist or does not have an associated author.");
            return;
        }

        // Obtener el ID de la imagen
        int imageId = deleteImagePostgres.getImageIdByName(nameImageToDelete);
        if (imageId <= 0) {
            JOptionPane.showMessageDialog(null, "The image does not exist.");
            return;
        }


        String passwordAuthor = JOptionPane.showInputDialog(null, "Enter your password:");
        if (passwordAuthor == null || passwordAuthor.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Password cannot be empty.");
            return;
        }

        boolean correctPassword = loadImage.validateAuthorPassword(id_author, passwordAuthor);
        if (!correctPassword) {
            JOptionPane.showMessageDialog(null, "Wrong password.");
            return;
        }

        if (deleteImagePostgres.deleteImageById(imageId)) {
            JOptionPane.showMessageDialog(null, "The image has been deleted successfully.");
        } else {
            JOptionPane.showMessageDialog(null, "An error occurred while deleting the image.");
        }
    }

    private void implementPanel() {
        image = new ImagePixels(500, 600);
        secundaryPanel = new SecundaryPanel(image, invoker);
        this.getContentPane().add(secundaryPanel, BorderLayout.EAST);
        secundaryPanel.setVisible(true);
        principalPanel = new PrincipalPanel(image, secundaryPanel, invoker);
        this.getContentPane().add(principalPanel, BorderLayout.CENTER);
        principalPanel.setVisible(true);

    }

    private void importImage() {
        JFileChooser jFileChooser = new JFileChooser();
        int imageSelected = jFileChooser.showOpenDialog(this);
        if (imageSelected == jFileChooser.APPROVE_OPTION) {//Compare the integer with the value of the jFilechoose, if return 1 continue if return 0 close
            File fileSelected = jFileChooser.getSelectedFile();
            newImage(fileSelected);//File selected will be sending to the imageClass to be change to matrix
        }

    }

    private void newImage(File fileSelected) {
        image = new ImagePixels(fileSelected);
        principalPanel.setImage(image);
        secundaryPanel.setImagen(image);
        logger.info("New image uploaded: ");
        this.pack();
    }


    private void saveImage() {
        String nameAuthor = JOptionPane.showInputDialog(null, "Enter the author's name:");
        AuthorPostgres authorPostgres = new AuthorPostgres();

        if (nameAuthor == null || nameAuthor.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "The name is empty. Please try again.");
            return;
        }
        if (authorPostgres.searchAuthor(nameAuthor)) {
            JOptionPane.showMessageDialog(null, "Author found.");
            id_author = authorPostgres.getAuthorIdByName(nameAuthor);
            categoryTag = new String[]{"Comida", "Retrato", "Paisaje","Blanco y negro", "Arte", "Abstracto", "Animado", "Figuras"};
            selection = (String) JOptionPane.showInputDialog(
                    null,
                    "Select a category:",
                    "Category of tag",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    categoryTag,
                    categoryTag[0]
            );

            if (selection != null) {
                logger.info("You have selected: " + selection);
                tagImage = new TagImage();
                id_tag=tagImage.getTagIdByName(selection);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Author not found. Proceeding to create a new author...");
            author = new Author(authorPostgres);
        }

        if(id_tag != 0) {
             saveImagePostgres = new SaveImage();
            byte[] imageByte;
            imageByte = converToByte(image.getBi());
            String imageName = JOptionPane.showInputDialog(null, "Image's name: ");
            while (imageName.isEmpty() || imageName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "The name of image is empty");
                imageName = JOptionPane.showInputDialog(null, "Name image: ");
            }
            if(!imageName.isEmpty()&& id_author >= 0 && id_author >= 0 ) {
                saveImagePostgres.insertImage(imageByte, imageName, id_author, id_tag);
            }
        }



    }


    private void deltePanel() {
    }

    private void addPanel() {
    }
    public static byte[] converToByte(BufferedImage image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {

            ImageIO.write(image, "PNG", baos);
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }

}
