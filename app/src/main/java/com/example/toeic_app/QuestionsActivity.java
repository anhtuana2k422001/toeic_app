package com.example.toeic_app;

import static com.example.toeic_app.DbQuery.g_catList;
import static com.example.toeic_app.DbQuery.g_quesList;
import static com.example.toeic_app.DbQuery.g_selected_cat_index;
import static com.example.toeic_app.DbQuery.g_selected_test_index;
import static com.example.toeic_app.DbQuery.g_testlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class QuestionsActivity extends AppCompatActivity {
    private RecyclerView questionsView;
    private TextView tvQuesID, timerTV, catNameTV;
    private Button summitB, markB, clearSelB;
    private ImageButton prevQuesB, nextQuesB;
    private ImageView quesListB;
    private  int quesID;
    QuestionsAdapter quesAdapter;
    private DrawerLayout drawer; // khái báo 1 layout Drawer
    private ImageButton drawerCloseB;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions_list_layout);

        init();

        quesAdapter = new QuestionsAdapter(g_quesList);
        questionsView.setAdapter(quesAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        questionsView.setLayoutManager(layoutManager);
        setSnapHelper();

        quesID = 0;
        tvQuesID.setText("1/"+String.valueOf(g_quesList.size())); // Lấy ra số số lượng câu hỏi
        catNameTV.setText(g_catList.get(g_selected_cat_index).getName()); // Lấy ra tên danh mục luyện thi

        setClickListeners(); //
        startTimer();
    }

    private void init(){
        questionsView = findViewById(R.id.questions_view);
        tvQuesID = findViewById(R.id.tv_quesID);
        timerTV = findViewById(R.id.tv_timer);
        catNameTV = findViewById(R.id.qa_catName);
        summitB = findViewById(R.id.submitB);
        markB = findViewById(R.id.markB);
        clearSelB = findViewById(R.id.clear_selB);
        prevQuesB = findViewById(R.id.prev_quesB);
        nextQuesB = findViewById(R.id.next_quesB);
        quesListB = findViewById(R.id.ques_list_gridB);
        drawer = findViewById(R.id.drawer_layout);

        drawerCloseB = findViewById(R.id.drawerCloseB);
    }

    private void setSnapHelper(){
        final SnapHelper snapHelper=new PagerSnapHelper();
        snapHelper.attachToRecyclerView(questionsView);
        questionsView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState){
                super.onScrollStateChanged(recyclerView,newState);
                View view=snapHelper.findSnapView(recyclerView.getLayoutManager());
                quesID = recyclerView.getLayoutManager().getPosition(view);
//                if(g_quesList.get(quesID).getStatus()==NOT_VISITED)
//                    g_quesList.get(quesID).setStatus(UNANSWERED);

                // Chuyển sang câu hỏi khác
                tvQuesID.setText(String.valueOf(quesID+1)+ "/"+ String.valueOf(g_quesList.size()));
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView,int dx,int dy){
                super.onScrolled(recyclerView,dx,dy);
            }
        });
    }


    private void setClickListeners(){
        // Quay lại câu hỏi
        prevQuesB.setOnClickListener((view) -> {
            if(quesID> 0)
            {
                questionsView.smoothScrollToPosition(quesID- 1);
            }
        });

         // chuyển sang câu hỏi khác
        nextQuesB.setOnClickListener((view) -> {
            if(quesID<g_quesList.size()-1)
            {
                questionsView.smoothScrollToPosition(quesID+ 1);
            }
        });


        // Xóa lựa chọn
        clearSelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                g_quesList.get(quesID).setSelectedAns(-1);
                quesAdapter.notifyDataSetChanged();
            }
        });

        // Xem và trả lời câu hỏi nhanh
        quesListB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(! drawer.isDrawerOpen(GravityCompat.END))
                {
                   //gridAdapter.notifyDataSetChanged();
                    drawer.openDrawer(GravityCompat.END);
                }
            }
        });

        drawerCloseB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawer.isDrawerOpen(GravityCompat.END))
                {
                    drawer.closeDrawer(GravityCompat.END);
                }
            }
        });

    }

    // Chạy thời gian khi bắt đầu bài làm
    private void startTimer()
    {
        long totalTime= (long) g_testlist.get(g_selected_test_index).getTime() *60*1000; // lấy thời gian
        CountDownTimer timer=new CountDownTimer(totalTime + 1000,1000)
        {
            @Override
            public void onTick(long remainingTime) {
                @SuppressLint("DefaultLocale") String time=String.format("%02d:%02d min",
                        TimeUnit.MILLISECONDS.toMinutes(remainingTime),
                        TimeUnit.MILLISECONDS.toSeconds(remainingTime)-
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remainingTime))
                );
                timerTV.setText(time); // Set time vào textView
            }

            @Override
            public void onFinish() {

            }
        };
        timer.start();
    }

}