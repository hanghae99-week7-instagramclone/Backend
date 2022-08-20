package com.sparta.instagramclone.service;

import org.apache.tika.Tika;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class FileUtils {

    private static final Tika tika = new Tika();

//    public static File multipartFileToFile(MultipartFile multipartFile) {
//        try {
//            File file = new File(multipartFile.getOriginalFilename());
//            multipartFile.transferTo(file);
//            return file;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public static boolean validateImgFile(InputStream inputStream) {
        try {
            List<String> notValidTypeList = Arrays.asList("image/jpeg", "image/pjpeg", "image/png", "image/gif", "image/bmp", "image/x-windows-bmp");

            String mimeType = tika.detect(inputStream);
            System.out.println("MimeType : " + mimeType);

            boolean isValid = notValidTypeList.stream().anyMatch(notValidType ->
                    notValidType.equalsIgnoreCase(mimeType));

            return isValid;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}