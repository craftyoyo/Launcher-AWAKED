package ch.m4th1eu.awaked.launcher;

import fr.litarvan.openauth.AuthenticationException;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.util.Saver;
import fr.theshark34.openlauncherlib.util.ramselector.RamSelector;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;
import fr.theshark34.swinger.textured.STexturedProgressBar;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static ch.m4th1eu.awaked.launcher.Launcher.RC_INFOS;

public class LauncherPanel extends JPanel implements SwingerEventListener {
    private Image background = Swinger.getResource("background.png");
    private static Saver saver = new Saver(new File(Launcher.RC_DIR, "\\config\\" + "launcher-settings.txt"));

    private JTextField usernameField = new JTextField(saver.get("username"));
    private JPasswordField passwordField = new JPasswordField(saver.get("password"));


    private STexturedButton closeButton = new STexturedButton(Swinger.getResource("close.png"), Swinger.getResource("close_hovered.png"));
    private STexturedButton reduceButton = new STexturedButton(Swinger.getResource("reduce.png"), Swinger.getResource("reduce_hovered.png"));

    private STexturedButton playButton = new STexturedButton(Swinger.getResource("play.png"), Swinger.getResource("play_hover.png"), Swinger.getResource("play_hover.png"));
    private STexturedButton optionsButton = new STexturedButton(Swinger.getResource("options.png"), Swinger.getResource("options_hovered.png"));

    private STexturedButton discordButton = new STexturedButton(Swinger.getResource("discord.png"), Swinger.getResource("discord_hovered.png"));
    private STexturedButton webButton = new STexturedButton(Swinger.getResource("web.png"), Swinger.getResource("web_hovered.png"));

    private STexturedProgressBar progressBar = new STexturedProgressBar(Swinger.getResource("bar.png"), Swinger.getResource("bar_loaded.png"));

    public final File SAVER_DIR = RC_INFOS.getGameDir();
    JEditorPane jep = new JEditorPane();


    private JLabel infoLabel = new JLabel("", 0);
    private RamSelector ramSelector = new RamSelector(new File(Launcher.RC_DIR, "/config/" + "ram.txt"));

    private void applyFont() {
        try {
            String ubuntu_font = Font.createFont(0, new File(new File(SAVER_DIR + "\\font\\"), "Ubuntu-R.ttf")).getFamily();

            Font ubuntu_20 = new Font(ubuntu_font, Font.PLAIN, 20);

            Font ubuntu_19 = new Font(ubuntu_font, Font.PLAIN, 19);
            Font ubuntu_18 = new Font(ubuntu_font, Font.PLAIN, 18);

            Font ubuntu_16 = new Font(ubuntu_font, Font.PLAIN, 16);
            Font ubuntu_14 = new Font(ubuntu_font, Font.PLAIN, 14);

            //Application de la police personnalisée.
            usernameField.setFont(ubuntu_20);
            passwordField.setFont(passwordField.getFont().deriveFont(20.0F));
            infoLabel.setFont(ubuntu_16);
            jep.setFont(ubuntu_18);


        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LauncherPanel() {

        WriteRam();
        downloadFont();
        applyFont();

        setLayout(null);
        setBackground(Swinger.TRANSPARENT);


        usernameField.setForeground(Color.WHITE);
        usernameField.setOpaque(false);
        usernameField.setBorder(null);
        usernameField.setBounds(150, 182, 250, 60);
        add(usernameField);

        passwordField.setForeground(Color.WHITE);
        passwordField.setOpaque(false);
        passwordField.setBorder(null);
        passwordField.setBounds(195, 292, 250, 60);
        add(passwordField);

        optionsButton.setBounds(850, 370, 40, 40);
        optionsButton.addEventListener(this);
        add(optionsButton);

        reduceButton.setBounds(805, 7, 40, 40);
        reduceButton.addEventListener(this);
        add(reduceButton);

        closeButton.setBounds(850, 7, 40, 40);
        closeButton.addEventListener(this);
        add(closeButton);

        playButton.setBounds(195, 385, 200, 60);
        playButton.addEventListener(this);
        add(playButton);

        webButton.setBounds(720, 420, 80, 80);
        webButton.addEventListener(this);
        add(webButton);

        discordButton.setBounds(810, 420, 80, 80);
        discordButton.addEventListener(this);
        add(discordButton);

        progressBar.setBounds(-7, 465, 700, 30);
        add(progressBar);

        infoLabel.setForeground(Color.WHITE);
        infoLabel.setBounds(0, 437, 700, 30);
        add(infoLabel);

        HTMLEditorKit kit = new HTMLEditorKit();
        jep.setBounds(620, 80, 260, 200);
        jep.setOpaque(false);
        jep.setForeground(Color.WHITE);
        jep.setEditable(false);
        jep.setEditorKit(kit);

        try {
            jep.setPage(new URL("https://mc-awaked.fr/app/webroot/Awaked/news.txt"));
            this.add(jep);
        } catch (IOException e) {
            jep.setContentType("text/html ");
            jep.setText("Impossible d'afficher les news...");
        }

    }

    public void onEvent(SwingerEvent e) {
        if (e.getSource() == closeButton) {
            WriteAccount();
            ramSelector.save();

            System.exit(0);
        } else if (e.getSource() == reduceButton) {
            LauncherFrame.getInstance().setState(1);
        } else if (e.getSource() == optionsButton) {
            ramSelector.display();
        } else if (e.getSource() == webButton) {
            Desktop w = Desktop.getDesktop();
            try {
                w.browse(new URI("https://mc-awaked.fr/"));
            } catch (IOException | URISyntaxException e1) {
                e1.printStackTrace();
            }
        } else if (e.getSource() == discordButton) {
            Desktop w = Desktop.getDesktop();
            try {
                w.browse(new URI("https://discord.gg/sVMb3vE"));
            } catch (IOException | URISyntaxException e1) {
                e1.printStackTrace();
            }
        } else if (e.getSource() == playButton) {
            launch();
        }
    }

    public void launch() {
        setFieldsEnabled(false);
        if ((usernameField.getText().replaceAll(" ", "").length() == 0)
                || (passwordField.getText().length() == 0)) {
            JOptionPane.showMessageDialog(this, "Erreur, veuillez entrer un nom d'utilisateur et un mot de passe valide.",
                    "Erreur", 0);
            setFieldsEnabled(true);
            return;
        }
        Thread t = new Thread() {
            public void run() {
                try {
                    Launcher.auth(usernameField.getText(),
                            passwordField.getText());
                } catch (AuthenticationException e) {
                    JOptionPane.showMessageDialog(LauncherPanel.this,
                            "Erreur impossible de ce connecter : " + e.getErrorModel().getErrorMessage(), "Erreur",
                            0);
                    setFieldsEnabled(true);
                    return;
                }
                try {
                    Launcher.update();
                } catch (Exception e) {
                    Launcher.interruptThread();
                    JOptionPane.showMessageDialog(LauncherPanel.this,
                            "Impossible de mettre à jour le launcher.",
                            "Erreur", 0);
                    return;
                }
                WriteAccount();
                ramSelector.save();

                try {
                    Launcher.launch();
                } catch (LaunchException e) {
                    JOptionPane.showMessageDialog(LauncherPanel.this,
                            "Impossible de lancer le jeu!", "Erreur", 0);
                    setFieldsEnabled(true);
                }
            }
        };
        t.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }


    public void setFieldsEnabled(boolean enabled) {
        usernameField.setEnabled(enabled);
        passwordField.setEnabled(enabled);
        playButton.setEnabled(enabled);
    }

    public void downloadFont() {
        URL inputUrl = getClass().getResource("/ch/m4th1eu/awaked/launcher/ressources/Ubuntu-R.ttf");
        File dest = new File(SAVER_DIR + "\\font\\" + "/Ubuntu-R.ttf");
        try {
            FileUtils.copyURLToFile(inputUrl, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public STexturedProgressBar getProgressBar() {
        return progressBar;
    }

    public void setInfoText(String text) {
        infoLabel.setText(text);
    }

    public RamSelector getRamSelector() {
        return ramSelector;
    }

    public void WriteAccount() {
        File folder = new File(new File(String.valueOf(SAVER_DIR)), "\\config\\");
        File userfile = new File(new File(SAVER_DIR + "\\config\\"), "launcher-settings.txt");
        if (folder.exists()) {
            if (userfile.exists()) {
                try {
                    FileWriter checker = new FileWriter(new File(SAVER_DIR + "\\config\\" + "launcher-settings.txt"));
                    checker.write("username=" + this.usernameField.getText());
                    checker.write("\npassword=" + this.passwordField.getText());

                    checker.close();
                } catch (Exception localException) {
                }
            } else {
                try {
                    userfile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    FileWriter checker = new FileWriter(new File(SAVER_DIR + "\\config\\" + "launcher-settings.txt"));
                    checker.write("username=" + this.usernameField.getText());
                    checker.write("\npassword=" + this.passwordField.getPassword());
                    checker.close();
                } catch (Exception localException1) {
                }
            }
        } else {
            folder.mkdirs();
            try {
                userfile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                FileWriter checker = new FileWriter(new File(SAVER_DIR + "\\config\\" + "launcher-settings.txt"));
                checker.write("username=" + this.usernameField.getText());
                checker.write("\npassword=" + this.passwordField.getPassword());

                checker.close();
            } catch (Exception localException2) {
            }
        }
    }


    public void WriteRam() {
        File folder = new File(new File(String.valueOf(SAVER_DIR)), "\\config\\");
        File userfile = new File(new File(SAVER_DIR + "\\config\\"), "ram.txt");
        if (folder.exists()) {
            if (!userfile.exists()) {
                try {
                    FileWriter checker = new FileWriter(new File(SAVER_DIR + "\\config\\" + "ram.txt"));

                    checker.close();
                } catch (Exception localException) {
                }
            }
        } else {
            folder.mkdirs();
            try {
                userfile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                FileWriter checker = new FileWriter(new File(SAVER_DIR + "\\config\\" + "ram.txt"));

                checker.close();
            } catch (Exception localException2) {
            }
        }
    }
}
