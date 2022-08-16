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
        private Button optionA, optionB, optionC,optionD, prevSelectedB;
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            ques = itemView.findViewById(R.id.tv_question);
            optionA = itemView.findViewById(R.id.optionA);
            optionB = itemView.findViewById(R.id.optionB);
            optionC = itemView.findViewById(R.id.optionC);
            optionD = itemView.findViewById(R.id.optionD);
            prevSelectedB = null; // gán bằng null

        }

        private void setData(final int pos){
            ques.setText(questionList.get(pos).getQuestion());
            optionA.setText(questionList.get(pos).getOptionA());
            optionB.setText(questionList.get(pos).getOptionB());
            optionC.setText(questionList.get(pos).getOptionC());

            setOption(optionA, 1, pos);
            setOption(optionB, 2, pos);
            setOption(optionC, 3, pos);
            setOption(optionD, 4, pos);

            // Click vào đáp án A
            optionA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(optionA, 1, pos);
                }
            });


            // Click vào đáp án A
            optionB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(optionB, 2, pos);
                }
            });


            // Click vào đáp án A
            optionC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(optionC, 3, pos);
                }
            });

            // Click vào đáp án A
            optionD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(optionD, 4, pos);
                }
            });

        }

        // Chọn vào một đáp án
        private void selectOption(Button btn, int option_num, int quesID)
        {

            if(prevSelectedB==null)
            {
                btn.setBackgroundResource(R.drawable.selected_btn);
                DbQuery.g_quesList.get(quesID).setSelectedAns(option_num);
               // changeStatus(quesID, ANSWERED);
                prevSelectedB = btn;
            }
            else
            {
                if(prevSelectedB.getId() == btn.getId()){
                    btn.setBackgroundResource(R.drawable.unselected_btn);
                    DbQuery.g_quesList.get(quesID).setSelectedAns(-1);
                   // changeStatus(quesID, UNANSWERED);
                    prevSelectedB = null;
                }
                else
                {
                    prevSelectedB.setBackgroundResource(R.drawable.unselected_btn);
                    btn.setBackgroundResource(R.drawable.selected_btn);
                    DbQuery.g_quesList.get(quesID).setSelectedAns(option_num);
                   // changeStatus(quesID, ANSWERED);
                    prevSelectedB = btn;
                }
            }
        }

        private void setOption(Button btn, int option_num, int quesID)
        {
            if(DbQuery.g_quesList.get(quesID).getSelectedAns() == option_num)
            {
                btn.setBackgroundResource(R.drawable.selected_btn);
            }
            else
            {
                btn.setBackgroundResource(R.drawable.unselected_btn);
            }
        }

    }
}
