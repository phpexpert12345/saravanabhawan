package com.harperskebab.utils;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Compress {
    private String tag = "Compress class : ";
    private static final int BUFFER = 1024;
    private String file;
    private String zipFile;

    public Compress(String mFile, String mZipFile) {
        file = mFile;
        zipFile = mZipFile;
    }

    public void zip() {
        try {

            BufferedInputStream origin;
            FileOutputStream dest = new FileOutputStream(zipFile);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            byte[] data = new byte[BUFFER];

            showLog("Adding : " + file);
            FileInputStream fi = new FileInputStream(file);
            origin = new BufferedInputStream(fi, BUFFER);
            ZipEntry entry = new ZipEntry(file.substring(file.lastIndexOf("/") + 1));
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            showLog("Zip Created");
            origin.close();
            out.close();
        } catch (Exception e) {

            showLog(e.toString());

        }

    }

    private void showLog(String msg) {
        Log.d(tag, msg);
    }
}
