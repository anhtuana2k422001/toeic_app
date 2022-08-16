package com.example.toeic_app;

import static com.example.toeic_app.DbQuery.ANSWERED;
import static com.example.toeic_app.DbQuery.NOT_VISITED;
import static com.example.toeic_app.DbQuery.REVIEW;
import static com.example.toeic_app.DbQuery.UNANSWERED;
import static com.example.toeic_app.DbQuery.g_catList;
import static com.example.toeic_app.DbQuery.g_quesList;
import static com.example.toeic_app.DbQuery.g_selected_cat_index;
import static com.example.toeic_app.DbQuery.g_selected_test_index;
import static com.example.toeic_app.DbQuery.g_testlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.toeic_app.Adapters.QuestionGridAdapter;
import com.example.toeic_app.Adapters.QuestionsAdapter;

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
    private GridView quesListGV;
    private ImageView markImage;
    private QuestionGridAdapter gridAdapter;
    private CountDownTimer timer;
    private long timeLeft;

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
        gridAdapter = new QuestionGridAdapter(this, g_quesList.size());
        quesListGV.setAdapter(gridAdapter);
        setSnapHelper();
        quesID = 0;
        tvQuesID.setText("1/"+String.valueOf(g_quesList.size())); // Lấy ra số số lượng câu hỏi
        catNameTV.setText(g_catList.get(g_selected_cat_index).getName()); // Lấy ra tên danh mục luyện thi

        setClickListeners(); //
        startTimer();
    }

    @SuppressLint("SetTextI18n")
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
        markImage=findViewById(R.id.mark_image);
        quesListGV = findViewById(R.id.ques_list_gv);
        quesID = 0;
        tvQuesID.setText("1/"+String.valueOf(g_quesList.size()));
        catNameTV.setText(g_catList.get(g_selected_cat_index).getName());
        g_quesList.get(0).setStatus(UNANSWERED);
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
                if(g_quesList.get(quesID).getStatus() == NOT_VISITED)
                    g_quesList.get(quesID).setStatus(UNANSWERED);

                // Kiểm tra bỏ check câu đánh dấu để xem lại
                if(g_quesList.get(quesID).getStatus()==REVIEW)
                {
                    markImage.setVisibility(View.VISIBLE);
                }
                else
                {
                    markImage.setVisibility(View.GONE);
                }

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
                g_quesList.get(quesID).setStatus(UNANSWERED);
                markImage.setVisibility(View.GONE);
                quesAdapter.notifyDataSetChanged();
            }
        });

        // Xem và trả lời câu hỏi nhanh
        quesListB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(! drawer.isDrawerOpen(GravityCompat.END))
                {
                   gridAdapter.notifyDataSetChanged();// thay đổi dữ liệu
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

        markB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(markImage.getVisibility()!=View.VISIBLE)
                {
                    markImage.setVisibility(View.VISIBLE);
                    g_quesList.get(quesID).setStatus(REVIEW);

                }
                else
                {
                    markImage.setVisibility(View.GONE);
                    if(g_quesList.get(quesID).getSelectedAns()!= -1)
                    {
                        g_quesList.get(quesID).setStatus(ANSWERED);
                    }
                    else
                    {
                        g_quesList.get(quesID).setStatus(UNANSWERED);
                    }
                }
            }
        });

        // Gửi đi
        summitB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitTest();
            }
        });

    }

    // Hoàn thành bài làm
    private void submitTest(){
        AlertDialog.Builder builder=new AlertDialog.Builder(QuestionsActivity.this);
        builder.setCancelable(true);
        View view = getLayoutInflater().inflate(R.layout.alert_dialog_layout,null); // lấy lấy layout

        Button cancelB = view.findViewById(R.id.cancelB);
        Button confirmB = view.findViewById(R.id.confirmB);

        builder.setView(view);

        AlertDialog alertDialog=builder.create();

        // Từ chối hoàn thành
        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        // Chấp nhận hoàn thành
        confirmB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                alertDialog.dismiss();
                Intent intent=new Intent(QuestionsActivity.this, ScoreActivity.class);
                long totalTime = (long) g_testlist.get(g_selected_test_index).getTime() *60*1000;
                intent.putExtra("TIME_TAKEN",totalTime-timeLeft);
                startActivity(intent);
                QuestionsActivity.this.finish();
            }
        });
        alertDialog.show();
    }

    // Chạy thời gian khi bắt đầu bài làm
    private void startTimer()
    {
        long totalTime= (long) g_testlist.get(g_selected_test_index).getTime() *60*1000; // lấy thời gian
        timer = new CountDownTimer(totalTime + 1000,1000)
        {
            @Override
            public void onTick(long remainingTime) {
                timeLeft = remainingTime;
                @SuppressLint("DefaultLocale") String time=String.format("%02d:%02d min",
                        TimeUnit.MILLISECONDS.toMinutes(remainingTime),
                        TimeUnit.MILLISECONDS.toSeconds(remainingTime)-
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remainingTime))
                );
                timerTV.setText(time); // Set time vào textView
            }

            // Hết thời gian tự động nộp bài
            @Override
            public void onFinish() {
                Intent intent=new Intent(QuestionsActivity.this, ScoreActivity.class);
                long totalTime = (long) g_testlist.get(g_selected_test_index).getTime() *60*1000;
                intent.putExtra("TIME_TAKEN",totalTime-timeLeft);
                startActivity(intent);
                QuestionsActivity.this.finish();
            }
        };
        timer.start();
    }

    // Đi tới câu hỏi đó
    public void goToQuestion(int position)
    {
        questionsView.smoothScrollToPosition(position);

        if(drawer.isDrawerOpen(GravityCompat.END))
            drawer.closeDrawer(GravityCompat.END);
    }

}