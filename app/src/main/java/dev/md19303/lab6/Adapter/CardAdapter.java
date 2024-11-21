package dev.md19303.lab6.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.List;

import dev.md19303.lab6.Model.CardData;
import dev.md19303.lab6.R;

// Adapter cho RecyclerView hiển thị danh sách các thẻ (cards)
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private final List<CardData> cardDataList;

    // Hàm khởi tạo Adapter, nhận vào danh sách dữ liệu thẻ
    public CardAdapter(List<CardData> cardDataList) {
        if (cardDataList == null) {
            throw new IllegalArgumentException("cardDataList không được null");
        }
        this.cardDataList = cardDataList;
    }

    @NonNull
    @Override
    // Hàm tạo ViewHolder mới khi RecyclerView cần
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout cho từng mục thẻ
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    // Hàm liên kết dữ liệu từ danh sách với ViewHolder tại vị trí cụ thể
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardData cardData = cardDataList.get(position);

        if (cardData != null) {
            holder.image.setImageResource(cardData.getImageResId()); // Gán ảnh từ tài nguyên
            holder.title.setText(cardData.getTitle()); // Gán tiêu đề
            holder.description.setText(cardData.getDescription()); // Gán mô tả
        }
    }

    @Override
    // Hàm trả về tổng số mục dữ liệu trong danh sách
    public int getItemCount() {
        return cardDataList != null ? cardDataList.size() : 0;
    }

    // Lớp ViewHolder đại diện cho từng mục trong RecyclerView
    static class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView image; // Hình ảnh của thẻ
        TextView title, description; // Tiêu đề và mô tả của thẻ

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);

            // Khởi tạo các thành phần giao diện
            image = itemView.findViewById(R.id.cardImage);
            title = itemView.findViewById(R.id.cardTitle);
            description = itemView.findViewById(R.id.cardDescription);
        }
    }
}
