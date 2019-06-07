package ch.m4th1eu.awaked.bootstrap;

import fr.theshark34.openlauncherlib.bootstrap.Bootstrap;
import fr.theshark34.openlauncherlib.bootstrap.LauncherClasspath;
import fr.theshark34.openlauncherlib.bootstrap.LauncherInfos;
import fr.theshark34.openlauncherlib.util.ErrorUtil;
import fr.theshark34.openlauncherlib.util.GameDir;
import fr.theshark34.openlauncherlib.util.SplashScreen;
import fr.theshark34.supdate.SUpdate;
import fr.theshark34.supdate.application.integrated.FileDeleter;
import fr.theshark34.supdate.exception.BadServerResponseException;
import fr.theshark34.supdate.exception.BadServerVersionException;
import fr.theshark34.supdate.exception.ServerDisabledException;
import fr.theshark34.supdate.exception.ServerMissingSomethingException;
import fr.theshark34.swinger.Swinger;

import java.io.File;
import java.io.IOException;

public class AwakedBootstrap {
    private static final LauncherInfos RC_B_INFOS = new LauncherInfos("AWAKΞD - Serveur HunterZ 1.12.2",
            "ch.m4th1eu.awaked.launcher.LauncherFrame");
    private static final File RC_DIR = GameDir.createGameDir("Awaked");
    private static final LauncherClasspath RC_B_CP = new LauncherClasspath(new File(RC_DIR, "Launcher/launcher.jar"),
            new File(RC_DIR, "Launcher/libs/"));
    private static SplashScreen splash;
    private static ErrorUtil errorUtil = new ErrorUtil(new File(RC_DIR, "crashs"));

    public static void main(String[] args) {
        Swinger.setResourcePath("/ch/m4th1eu/awaked/bootstrap/ressources/");
        displaySplash();
        try {
            doUpdate();
        } catch (Exception e) {
            errorUtil.catchError(e, "Impossible de mettre à jour le launcher !");
        }
        try {
            launchLauncher();
        } catch (Exception e) {
            errorUtil.catchError(e, "Impossible de lancer le launcher !");
        }
    }

    private static void displaySplash() {
        splash = new SplashScreen("AWAKΞD - Serveur HunterZ 1.12.2", Swinger.getResource("splash.png"));
        splash.setBackground(Swinger.TRANSPARENT);
        splash.setVisible(true);
    }

    private static void doUpdate() throws BadServerResponseException, ServerDisabledException,
            BadServerVersionException, ServerMissingSomethingException, IOException {
        SUpdate su = new SUpdate("https://mc-awaked.fr/app/webroot/Awaked/BootstrapAwaked/", new File(RC_DIR, "Launcher"));
        su.addApplication(new FileDeleter());
        su.getServerRequester().setRewriteEnabled(true);

        su.start();
    }

    private static void launchLauncher() throws IOException {
        Bootstrap bootstrap = new Bootstrap(RC_B_CP, RC_B_INFOS);
        Process p = bootstrap.launch();

        splash.setVisible(false);
        try {
            p.waitFor();
        } catch (InterruptedException localInterruptedException) {
        }
        System.exit(0);
    }
}
