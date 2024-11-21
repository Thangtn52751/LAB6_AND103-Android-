package dev.md19303.lab6;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCakeActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 100;

    private EditText nameInput, descriptionInput, priceInput;
    private Spinner distributorSpinner;
    private Button selectImageButton, addCakeButton;
    private ImageView cakeImageView;

    private Uri imageUri; // Để lưu trữ URI của ảnh đã chọn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cake);
        nameInput = findViewById(R.id.cakeName);
        descriptionInput = findViewById(R.id.cakeDescription);
        priceInput = findViewById(R.id.cakePrice);
        distributorSpinner = findViewById(R.id.distributorSpinner);
        selectImageButton = findViewById(R.id.selectImageButton);
        cakeImageView = findViewById(R.id.cakeImageView);
        addCakeButton = findViewById(R.id.addCakeButton);

        // Cấu hình Spinner với dữ liệu
        String[] distributors = {"Distributor A", "Distributor B", "Distributor C"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, distributors);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distributorSpinner.setAdapter(adapter);

        // Kiểm tra quyền truy cập bộ nhớ
        if (!checkStoragePermission()) {
            requestStoragePermission();
        }

        // Chọn ảnh từ bộ nhớ
        selectImageButton.setOnClickListener(v -> openImageChooser());

        // Thêm bánh
        addCakeButton.setOnClickListener(v -> uploadCake());
    }

    // Kiểm tra quyền
    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    // Yêu cầu quyền
    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        }
    }

    // Xử lý kết quả quyền
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Mở trình chọn ảnh
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData(); // Lưu URI ảnh đã chọn

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                cakeImageView.setImageBitmap(bitmap); // Hiển thị ảnh vào ImageView
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Gửi dữ liệu bánh lên server
    private void uploadCake() {
        String name = nameInput.getText().toString();
        String description = descriptionInput.getText().toString();
        String price = priceInput.getText().toString();
        String distributor = distributorSpinner.getSelectedItem().toString();

        if (name.isEmpty() || description.isEmpty() || price.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Mở luồng đầu vào từ URI
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            byte[] imageData = inputStream.readAllBytes();
            inputStream.close();

            RequestBody namePart = RequestBody.create(MediaType.parse("text/plain"), name);
            RequestBody descriptionPart = RequestBody.create(MediaType.parse("text/plain"), description);
            RequestBody pricePart = RequestBody.create(MediaType.parse("text/plain"), price);
            RequestBody distributorPart = RequestBody.create(MediaType.parse("text/plain"), distributor);

            // Sử dụng ContentResolver để đọc dữ liệu ảnh
            RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/*"), imageData);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", "uploaded_image.png", imageRequestBody);

            APIService apiService = RetrofitClient.getClient().create(APIService.class);
            Call<Void> call = apiService.uploadCakeWithImage(namePart, descriptionPart, pricePart, distributorPart, imagePart);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(AddCakeActivity.this, "Cake uploaded successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddCakeActivity.this, "Upload failed!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(AddCakeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("API ERROR", t.getMessage());
                }
            });
        } catch (IOException e) {
            Toast.makeText(this, "Error accessing file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
