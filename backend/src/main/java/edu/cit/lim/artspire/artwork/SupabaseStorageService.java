package edu.cit.lim.artspire.artwork;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

@Service
public class SupabaseStorageService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    private final String BUCKET_NAME = "artworks";

    public String uploadImage(MultipartFile file) throws IOException {
        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8) + fileExtension;

        // Supabase Storage API URL for uploading to a bucket
        String storageUrl = supabaseUrl + "/storage/v1/object/" + BUCKET_NAME + "/" + uniqueFileName;

        // Prepare connection
        URL url = new URL(storageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + supabaseKey);
        connection.setRequestProperty("Content-Type", file.getContentType());
        connection.setDoOutput(true);

        // Write file content
        try (OutputStream os = connection.getOutputStream()) {
            os.write(file.getBytes());
            os.flush();
        }

        int responseCode = connection.getResponseCode();

        if (responseCode == 200 || responseCode == 201) {
            // Return the public URL
            return supabaseUrl + "/storage/v1/object/public/" + BUCKET_NAME + "/" + uniqueFileName;
        } else {
            // Read error response
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            throw new IOException("Upload failed with code " + responseCode + ": " + response.toString());
        }
    }

    public void deleteImage(String imageUrl) throws IOException {
        // Extract file path from URL
        // URL format: https://project.supabase.co/storage/v1/object/public/bucket-name/filename
        String path = imageUrl.replace(supabaseUrl + "/storage/v1/object/public/", "");

        String storageUrl = supabaseUrl + "/storage/v1/object/" + path;
        URL url = new URL(storageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Authorization", "Bearer " + supabaseKey);

        int responseCode = connection.getResponseCode();
        if (responseCode != 200 && responseCode != 204) {
            throw new IOException("Delete failed with code: " + responseCode);
        }
    }
}
