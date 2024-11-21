package dev.md19303.lab6;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileUtils {

    public static String getPath(Context context, Uri uri) {
        try {
            ContentResolver contentResolver = context.getContentResolver();
            String fileName = getFileName(contentResolver, uri);
            File tempFile = new File(context.getCacheDir(), fileName);

            InputStream inputStream = contentResolver.openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(tempFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return tempFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getFileName(ContentResolver contentResolver, Uri uri) {
        String name = "temp_file";
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            if (nameIndex != -1) {
                name = cursor.getString(nameIndex);
            }
            cursor.close();
        }
        return name;
    }
}
