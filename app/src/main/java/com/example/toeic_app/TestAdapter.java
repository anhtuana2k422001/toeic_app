package com.example.toeic_app;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHoder> {

    private List<TestModel> test_list;

    public TestAdapter(List<TestModel> test_list) {
        this.test_list = test_list;
    }

    @NonNull
    @Override
    public TestAdapter.ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_item_layout, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestAdapter.ViewHoder holder, int position) {
        int progress = test_list.get(position).getTopScore();
        holder.setData(position, progress);
    }

    @Override
    public int getItemCount() {
        return test_list.size();
    }


    public class ViewHoder extends RecyclerView.ViewHolder {
        private TextView testNo;
        private TextView topScore;
        private ProgressBar progressBar;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            testNo = itemView.findViewById(R.id.testNo);
            topScore = itemView.findViewById(R.id.scoreText);
            progressBar = itemView.findViewById(R.id.testProgressBar);


        }

        public void setData(int pos, int progress){
            testNo.setText("Part "+ String.valueOf(pos + 1));
            topScore.setText(String.valueOf(progress) + " %");
            progressBar.setProgress(progress);


            // Chạy giao diện câu hỏi của mỗi part trong danh mục đó
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DbQuery.g_selected_test_index = pos;
                    Intent intent = new Intent(itemView.getContext(), StartTestActivity.class);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
