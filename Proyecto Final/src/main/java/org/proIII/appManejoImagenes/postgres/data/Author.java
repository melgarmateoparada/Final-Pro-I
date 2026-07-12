package org.proIII.appManejoImagenes.postgres.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class Author extends JFrame{
    private static final Logger logger = LogManager.getRootLogger();
    private String name;
    private String password;
    private String mail;
    private String biography;
    private JTextField nameText, mailArea, biographyArea;
    private JPasswordField passwordArea;
    private JLabel nameLbl, passswordLbl, mailLbl, biographyLbl;
    private JButton newAuthor;
    private AuthorPostgres authorPostgres;



    public Author(AuthorPostgres authorPostgres){
        super("Author");
        setDefaultCloseOperation(2);
        setLayout(new GridBagLayout());
        this.authorPostgres = authorPostgres;
        unit();
        setSize(new Dimension(600,500));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void unit() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;


        nameLbl = new JLabel("Name: ");
        nameText = new JTextField();

        gbc.gridy = 0;
        add(nameLbl, gbc);
        gbc.gridy = 1;
        add(nameText, gbc);


        passswordLbl = new JLabel("Password: ");
        passwordArea = new JPasswordField();
        gbc.gridy = 2; // Fila 2
        add(passswordLbl, gbc);
        gbc.gridy = 3;
        add(passwordArea, gbc);


        mailLbl = new JLabel("Mail: ");
        mailArea = new JTextField();
        gbc.gridy = 4;
        add(mailLbl, gbc);
        gbc.gridy = 5;
        add(mailArea, gbc);


        biographyLbl = new JLabel("Biography: ");
        biographyArea = new JTextField();
        gbc.gridy = 6;
        add(biographyLbl, gbc);
        gbc.gridy = 7;
        add(biographyArea, gbc);

        newAuthor = new JButton("Save author");
        gbc.gridy = 8;
        add(newAuthor, gbc);


        newAuthor.addActionListener(e ->
                newAuthor()
        );


    } public void newAuthor() {
        name = nameText.getText().trim();
        password = new String(passwordArea.getPassword()).trim();
        mail = mailArea.getText().trim();
        biography = biographyArea.getText().trim();
        if (!name.isEmpty() && !password.isEmpty() && !mail.isEmpty() && !biography.isEmpty()) {
            authorPostgres.insertAuthor(name, password, mail, biography);
            if (authorPostgres.getAuthorIdByName(name) != 0) {
                JOptionPane.showMessageDialog(null, name + " Has been saved");
                dispose();
            }
        }
    }



}
