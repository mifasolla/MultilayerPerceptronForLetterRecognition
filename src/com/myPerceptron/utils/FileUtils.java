package com.myPerceptron.utils;

import com.myPerceptron.MainApp;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.Scanner;

/**
 * Created by Vika on 10.03.2016.
 */
public final class FileUtils {

    private FileUtils() {
    }

    public static File openDir(MainApp mainApp, String pathTo) throws FileNotFoundException, UnsupportedEncodingException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(getDefaultPath(pathTo));
        File dir = directoryChooser.showDialog(mainApp.getPrimaryStage());

        if(dir != null) {
            if (dir.exists()) {
                if (!dir.isDirectory()) {
                    dir.delete();
                    dir.mkdir();
                }
            } else {
                dir.mkdir();
            }
        }
        updatePathTo(dir, pathTo);

        return dir;
    }

    public static FilenameFilter createFileNameFilter(final String filter) {
        return new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name != null) {
                    if (name.contains(filter)) return true;
                }
                return false;
            }
        };
    }

    public static File createPictureFile(File imagesDir, String picturePrefix, int pictureNumber) {
        return new File(imagesDir, picturePrefix + pictureNumber + ").png");
    }

    public static File openFile(MainApp mainApp, String pathTo, String extDescription, String extensionFilter) throws FileNotFoundException, UnsupportedEncodingException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(getDefaultPath(pathTo));

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(extDescription, extensionFilter);
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
        updatePathTo(file, pathTo);

        return file;
    }

    public static File saveFile(MainApp mainApp, String pathTo, String extDescription, String extensionFilter) throws FileNotFoundException, UnsupportedEncodingException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(getDefaultPath(pathTo));

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(extDescription, extensionFilter);
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
        updatePathTo(file, pathTo);

        return file;
    }

    private static File getDefaultPath(String pathTo) throws FileNotFoundException {
        File cache = new File(pathTo);
        Scanner cacheScan = new Scanner(cache);
        String cachePath = "";
        if (cacheScan.hasNext()) {
            cachePath = cacheScan.next();
            return new File(cachePath);
        }
        return null;
    }

    private static void updatePathTo(File chosenFile, String oldPathTo) throws FileNotFoundException {
        if(chosenFile != null) {
            File updateCache = new File(oldPathTo);
            PrintWriter writer = new PrintWriter(updateCache);
            String updatePath = chosenFile.getParent();
            if (updatePath.contains("src\\")) {
                updatePath = updatePath.substring(updatePath.indexOf("src\\"));
            }
            updatePath.replace("\\", "\\\\");

            writer.println(updatePath);
            writer.close();
        }
    }

}
