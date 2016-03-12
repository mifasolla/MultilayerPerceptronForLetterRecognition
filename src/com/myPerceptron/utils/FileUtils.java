package com.myPerceptron.utils;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by Vika on 10.03.2016.
 */
public final class FileUtils {

    private FileUtils() {
    }

    public static File createImagesDir() {
        File imagesDir = new File(".\\src\\com\\myPerceptron\\data\\images");
        if (imagesDir.exists()) {
            if (!imagesDir.isDirectory()) {
                imagesDir.delete();
                imagesDir.mkdir();
            }
        } else {
            imagesDir.mkdir();
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
