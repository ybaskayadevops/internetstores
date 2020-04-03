package com.gmbh.internetstores.frameworks.utils;

import android.os.Environment;
import com.gmbh.internetstores.MvvmApp;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MediaFileUtils {

  public enum MediaSource {
    CAMERA,
    GALLERY
  }

  private static String BASE_DIR = File.separator + "WhatsAround";
  private static String MEDIA_UPLOAD_DIR = BASE_DIR + File.separator + "Upload";
  private static String PROFILE_CAMERA_DIR = BASE_DIR + File.separator + "Profile";
  private static String PHOTO_CHECKPOINT_DIR = BASE_DIR + File.separator + "Checkpoint";

  private static String generateFileName() {
    return new SimpleDateFormat("yyyy_MM_dd_HHmmss", Locale.US).format(new Date());
  }

  private static File getDir(String directoryName) {
    if (!Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {

      return null;
    }
    File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), directoryName);
    if (!mediaStorageDir.exists()) {
      if (!mediaStorageDir.mkdirs()) {

        return null;
      }
    }
    return mediaStorageDir;
  }

  private static File getPrivateDir(String directoryName) {
    File privateStorageDir = new File(MvvmApp.getApplication().getFilesDir(), directoryName);
    if (!privateStorageDir.exists()) {
      if (!privateStorageDir.mkdirs()) {
        return null;
      }
    }
    return privateStorageDir;
  }

  private static File getUploadDir() {
    return getDir(MEDIA_UPLOAD_DIR);
  }

  public static String getUploadDirStr() {
    return getDir(MEDIA_UPLOAD_DIR).getAbsolutePath();
  }

  private static File getProfileCameraDir() {
    return getDir(PROFILE_CAMERA_DIR);
  }

  private static File getPhotoCheckpointDir() {
    return getPrivateDir(PHOTO_CHECKPOINT_DIR);
  }

  private static File getFile(File dir, String prefix, String suffix) {
    return getFile(dir, prefix, generateFileName(), suffix);
  }

  private static File getFile(File dir, String prefix, String fileName, String suffix) {
    return new File(dir, prefix + fileName + suffix);
  }

  public static File getImageFile() {
    return getFile(getUploadDir(), "IMG_", ".jpg");
  }

  public static File getVideoFile() {
    return getFile(getUploadDir(), "VID_", ".mp4");
  }

  public static File getProfileCameraImage() {
    return getFile(getProfileCameraDir(), "IMG_", ".jpg");
  }

  public static File getPhotoCheckpointImage() {
    return getFile(getPhotoCheckpointDir(), "IMG_", ".jpg");
  }

  public static String getCompressFilePath(String filename) {
    if (filename == null || filename.isEmpty()) {
      return null;
    }
    int extension = filename.lastIndexOf(".");
    return filename.substring(0, extension) + "_compressed" + filename.substring(extension);
  }

  public static String getEditFilePath(String filename) {
    if (filename == null || filename.isEmpty()) {
      return null;
    }
    int extension = filename.lastIndexOf(".");
    return filename.substring(0, extension) + "_edit" + filename.substring(extension);
  }



  public static String getFileName(String filePath) {
    if (filePath == null || filePath.isEmpty()) {
      return "";
    }
    int extension = filePath.lastIndexOf("/");
    return filePath.substring(extension + 1);
  }

  //Return ".jpg" with dot
  public static String getExtension(String s) {
    return s != null && s.lastIndexOf(".") > 0 ? s.substring(s.lastIndexOf(".")) : "";
  }
}