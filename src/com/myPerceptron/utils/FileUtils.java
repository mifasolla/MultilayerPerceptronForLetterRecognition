package com.myPerceptron.utils;

import com.myPerceptron.MainApp;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by Vika on 10.03.2016.
 */
public final class FileUtils {

    private FileUtils() {
    }

    public static File createImagesDir(MainApp mainApp) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File imagesDir = directoryChooser.showDialog(mainApp.getPrimaryStage());

       // File imagesDir = new File(".\\src\\com\\myPerceptron\\data\\images");
        if(imagesDir != null) {
            if (imagesDir.exists()) {
                if (!imagesDir.isDirectory()) {
                    imagesDir.delete();
                    imagesDir.mkdir();
                }
            } else {
                imagesDir.mkdir();
            }
        }

        return imagesDir;
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

}
