package com.example.toeic_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHoder> {
    private List<QuestionModel> questionList;

    public QuestionsAdapter(List<QuestionModel> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item_layout, parent, false);
        return new ViewHoder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        holder.setData(position);
    }


    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {

        private TextView ques;
        private Button optionA, optionB, optionC,optionD;
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            ques = itemView.findViewById(R.id.tv_question);
            optionA = itemView.findViewById(R.id.optionA);
            optionB = itemView.findViewById(R.id.optionB);
            optionC = itemView.findViewById(R.id.optionC);
            optionD = itemView.findViewById(R.id.optionD);

        }

        private void setData(final int post){
            ques.setText(questionList.get(post).getQuestion());
            optionA.setText(questionList.get(post).getOptionA());
            optionB.setText(questionList.get(post).getOptionB());
            optionC.setText(questionList.get(post).getOptionC());
            optionD.setText(questionList.get(post).getOptionD());
        }
    }
}
