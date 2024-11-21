package dev.md19303.lab6;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;


import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import dev.md19303.lab6.Adapter.CardAdapter;
import dev.md19303.lab6.Model.CardData;

public class MainActivity extends AppCompatActivity {
    ViewPager2 viewPager;
    TabLayout tabLayout;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Khởi tạo các thành phần giao diện
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabIndicator);
        btnLogin = findViewById(R.id.btnLogin);

        // Tạo danh sách dữ liệu cho các thẻ (cards)
        List<CardData> cardDataList = new ArrayList<>();
        cardDataList.add(new CardData(R.drawable.cupcake, "Endless Sweet Delights", "Indulge in a world of delicious desserts..."));
        cardDataList.add(new CardData(R.drawable.cake, "Create Your Dream Cake", "Our flexible custom cake options..."));
        cardDataList.add(new CardData(R.drawable.macaroons, "Hassle-Free Ordering", "Enjoy a seamless and convenient ordering..."));

        // Thiết lập Adapter cho ViewPager2
        CardAdapter adapter = new CardAdapter(cardDataList);
        viewPager.setAdapter(adapter);

        // Liên kết TabLayout với ViewPager2 để đồng bộ hóa chỉ báo trang
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {}).attach();

        // Lắng nghe sự kiện thay đổi trang trong ViewPager2
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                // Hiển thị nút "Login" ở trang cuối cùng
                if (position == cardDataList.size() - 1) {
                    btnLogin.setVisibility(View.VISIBLE);
                } else {
                    btnLogin.setVisibility(View.GONE);
                }
            }
        });

        // Xử lý sự kiện click nút "Login"
        btnLogin.setOnClickListener(v -> {
            // Chuyển sang màn hình đăng nhập
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
