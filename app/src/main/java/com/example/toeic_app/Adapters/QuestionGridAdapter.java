package com.example.toeic_app.Adapters;
import static com.example.toeic_app.DbQuery.ANSWERED;
import static com.example.toeic_app.DbQuery.NOT_VISITED;
import static com.example.toeic_app.DbQuery.REVIEW;
import static com.example.toeic_app.DbQuery.UNANSWERED;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.toeic_app.DbQuery;
import com.example.toeic_app.QuestionsActivity;
import com.example.toeic_app.R;

public class QuestionGridAdapter extends BaseAdapter {
    public QuestionGridAdapter(Context context, int numOfQues) {
        this.numOfQues = numOfQues;
        this.context = context;
    }

    private int numOfQues;
    private Context context;

    @Override
    public int getCount() {
        return numOfQues;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View myview;

        if(view == null){
            myview= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ques_grid_item,viewGroup,false);
        }
        else
        {
            myview = view;
        }


        // DI chuyển tới cấu hỏi
        myview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(context instanceof QuestionsActivity)
                    ((QuestionsActivity)context).goToQuestion(i);
            }
        });

        TextView quesTV = myview.findViewById(R.id.ques_num);
        quesTV.setText(String.valueOf(i+1));
        Log.d("LOGGGGGGGG",String.valueOf(DbQuery.g_quesList.get(i).getStatus()));

        // Xét màu theo trạng thái của câu hỏi
        switch (DbQuery.g_quesList.get(i).getStatus())
        {
            case  NOT_VISITED :
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myview.getContext(), R.color.gray)));
                break;
            case  UNANSWERED :
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myview.getContext(), R.color.red)));
                break;
            case  ANSWERED :
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myview.getContext(), R.color.green)));
                break;
            case  REVIEW :
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myview.getContext(), R.color.pink)));
                break;
            default:
                break;
        }
        return myview;
    }

}
