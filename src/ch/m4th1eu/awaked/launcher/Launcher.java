package ch.m4th1eu.awaked.launcher;

import fr.litarvan.openauth.AuthPoints;
import fr.litarvan.openauth.AuthenticationException;
import fr.litarvan.openauth.Authenticator;
import fr.litarvan.openauth.model.AuthAgent;
import fr.litarvan.openauth.model.response.AuthResponse;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.*;
import fr.theshark34.supdate.BarAPI;
import fr.theshark34.supdate.SUpdate;
import fr.theshark34.supdate.application.integrated.FileDeleter;
import fr.theshark34.swinger.Swinger;

import java.io.File;
import java.util.Arrays;

//Il faut lancer la class LauncherFrame gros débile ^^

public class Launcher {
    public static final GameVersion RC_VERSION = new GameVersion("1.12.2", GameType.V1_8_HIGHER);
    public static final GameInfos RC_INFOS = new GameInfos("Awaked", RC_VERSION,
            new GameTweak[]{GameTweak.FORGE});
    public static final File RC_DIR = RC_INFOS.getGameDir();
    public static final File RC_CRASHS_DIR = new File(RC_DIR, "crashs");
    private static AuthInfos authInfos;
    private static Thread updateThread;

    public static void auth(String username, String password) throws AuthenticationException {
        Authenticator authenticator = new Authenticator("https://authserver.mojang.com/",
                AuthPoints.NORMAL_AUTH_POINTS);
        AuthResponse response = authenticator.authenticate(AuthAgent.MINECRAFT, username, password, "");
        authInfos = new AuthInfos(response.getSelectedProfile().getName(), response.getAccessToken(),
                response.getSelectedProfile().getId());
    }

    public static void update() throws Exception {
        SUpdate su = new SUpdate("https://mc-awaked.fr/app/webroot/Awaked/LauncherAwaked/", RC_DIR);
        su.addApplication(new FileDeleter());
        su.getServerRequester().setRewriteEnabled(true);

        updateThread = new Thread() {
            private int val;
            private int max;

            public void run() {
                while (!isInterrupted()) {
                    if (BarAPI.getNumberOfFileToDownload() == 0) {
                        LauncherFrame.getInstance().getLauncherPanel().setInfoText("Vérification des fichiers");
                    } else {
                        this.val = ((int) (BarAPI.getNumberOfTotalDownloadedBytes() / 1000L));
                        this.max = ((int) (BarAPI.getNumberOfTotalBytesToDownload() / 1000L));
                        LauncherFrame.getInstance().getLauncherPanel().getProgressBar().setMaximum(this.max);
                        LauncherFrame.getInstance().getLauncherPanel().getProgressBar().setValue(this.val);

                        LauncherFrame.getInstance().getLauncherPanel()
                                .setInfoText("Téléchargement des fichiers " + BarAPI.getNumberOfDownloadedFiles()
                                        + "/" + BarAPI.getNumberOfFileToDownload() + " "
                                        + Swinger.percentage(this.val, this.max) + "%");
                    }
                }
            }
        };
        updateThread.start();

        su.start();
        updateThread.interrupt();
    }

    public static void launch() throws LaunchException {
        ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(RC_INFOS, GameFolder.BASIC, authInfos);
        profile.getVmArgs().addAll(
                Arrays.asList(LauncherFrame.getInstance().getLauncherPanel().getRamSelector().getRamArguments()));
        ExternalLauncher launcher = new ExternalLauncher(profile);

        LauncherFrame.getInstance().setVisible(false);

        launcher.launch();
    }

    public static void interruptThread() {
        updateThread.interrupt();
    }
}
