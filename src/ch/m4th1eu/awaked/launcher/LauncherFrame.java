package ch.m4th1eu.awaked.launcher;

import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.util.WindowMover;

import javax.swing.*;

public class LauncherFrame extends JFrame {
    private static LauncherFrame instance;
    private LauncherPanel launcherPanel;

    public LauncherFrame() {
        setTitle("AWAKΞD - Serveur HunterZ 1.12.2 - Minecraft");

        setSize(900, 500);
        setDefaultCloseOperation(3);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setBackground(Swinger.TRANSPARENT);
        //setIconImage(Swinger.getResource("favicon.png"));
        this.launcherPanel = new LauncherPanel();
        setContentPane(this.launcherPanel = new LauncherPanel());

        WindowMover mover = new WindowMover(this);
        addMouseListener(mover);
        addMouseMotionListener(mover);

        setVisible(true);
    }

    public static void main(String[] args) {
        Swinger.setSystemLookNFeel();
        Swinger.setResourcePath("/ch/m4th1eu/awaked/launcher/ressources/");
        Launcher.RC_CRASHS_DIR.mkdir();

        instance = new LauncherFrame();
    }

    public static LauncherFrame getInstance() {
        return instance;
    }

    public LauncherPanel getLauncherPanel() {
        return this.launcherPanel;
    }
}
