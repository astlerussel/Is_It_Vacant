package com.example.isitvacant;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.solver.widgets.ConstraintTableLayout.ALIGN_CENTER;

public class BookingActivity extends AppCompatActivity implements View.OnClickListener {

    ViewGroup layout;
    LinearLayout payinglayout;
    TextView pay;
    ArrayList tableids = new ArrayList();
    ArrayList tableids1 = new ArrayList();

    String seats = "_________/"
            + "_________/"
            + "--AA__AA_/"
            + "--T------T/"
            + "--AA__AA_/"
            + "_________/"
            + "--AA__AA_/"
            + "--T------T/"
            + "--AA__AA_/"
            + "_________/"
            + "--AA__AA_/"
            + "--T------T/"
            + "--AA__AA_/"
            + "_________/"
            + "_________/"
           ;


    List<TextView> seatViewList = new ArrayList<>();
    int tableSize = 130;
    int tableGaping = 25;
    int seatSize = 100;
    int seatGaping = 25;

    int STATUS_AVAILABLE = 1;
    int STATUS_BOOKED = 2;
    int STATUS_RESERVED = 3;
    String selectedIds = "";
    private String bookDate;
    private String timeSlot;
    private String no_of_people;
    private static int tableId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        layout = findViewById(R.id.layoutSeat);
        payinglayout = findViewById(R.id.paying_layout);
        pay = findViewById(R.id.pay_ruppes);

        seats = "/" + seats;



        LinearLayout layoutSeat = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutSeat.setOrientation(LinearLayout.VERTICAL);
        layoutSeat.setLayoutParams(params);
        layoutSeat.setPadding(0 * seatGaping, 0 * seatGaping, 0 * seatGaping, 0 * seatGaping);
        layout.addView(layoutSeat);
        Toast.makeText(BookingActivity.this, getIntent().getStringExtra("bookDate")+"  "+getIntent().getStringExtra("timeSlot")+"  "+getIntent().getStringExtra("no_of_people"), Toast.LENGTH_SHORT).show();

        bookDate = getIntent().getStringExtra("bookDate");
        timeSlot = getIntent().getStringExtra("timeSlot");
        no_of_people = getIntent().getStringExtra("no_of_people");

        LinearLayout layout = null;

        int table_count = 0;





        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });



        for (int index = 0; index < seats.length(); index++) {
            if (seats.charAt(index) == '/') {
                layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layoutSeat.addView(layout);
            }
            else if (seats.charAt(index) == 'U') {
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setPadding(0, 0, 0 , 0 * seatGaping);
                view.setGravity(Gravity.CENTER);
                view.setBackgroundResource(R.drawable.chair_booked);
                view.setTextColor(Color.WHITE);
                view.setTag(STATUS_BOOKED);
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                layout.addView(view);
                seatViewList.add(view);
                view.setOnClickListener(this);
            } else if (seats.charAt(index) == 'A') {
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setPadding(0, 0, 0, 0 * seatGaping);

                view.setGravity(Gravity.TOP);
                view.setBackgroundResource(R.drawable.chair1);
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                view.setTextColor(Color.BLACK);

                layout.addView(view);
                seatViewList.add(view);

            } else if (seats.charAt(index) == 'R') {
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setPadding(0, 0, 0, 0 * seatGaping);

                view.setGravity(Gravity.CENTER);
                view.setBackgroundResource(R.drawable.chair_reserved);
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                view.setTextColor(Color.WHITE);
                view.setTag(STATUS_RESERVED);
                layout.addView(view);
                seatViewList.add(view);

            } else if (seats.charAt(index) == '_') {
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setBackgroundColor(Color.TRANSPARENT);
                view.setText("");
                layout.addView(view);
            }
            else if(seats.charAt(index) == 'T')
            {
                table_count++;
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(tableSize, tableSize);
                layoutParams.setMargins(100, 0, 0, 0);
                view.setLayoutParams(layoutParams);
                view.setId(table_count);
                view.setGravity(Gravity.CENTER);
                view.setBackgroundResource(R.drawable.table);
                view.setText("Table "+table_count);
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                view.setTextColor(Color.BLACK);
                layout.addView(view);
                view.setTag(STATUS_AVAILABLE);
                view.setOnClickListener(this);
            } else if (seats.charAt(index) == '-') {
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(25, 25);
                layoutParams.setMargins(10, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setBackgroundColor(Color.TRANSPARENT);
                view.setText("");
                layout.addView(view);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if ((int) view.getTag() == STATUS_AVAILABLE) {
            if (selectedIds.contains(view.getId() + ",")) {
                selectedIds = selectedIds.replace(+view.getId() + ",", "");
                for (int i=0;i<tableids.size();i++){
                    if (tableids.get(i).equals(view.getId())){

                        tableids.remove(i);
                        tableids1.remove(i);
                        Toast.makeText(this, ""+tableids1, Toast.LENGTH_SHORT).show();


                    }
                }

                view.setBackgroundResource(R.drawable.table);
            } else {
                selectedIds = selectedIds + view.getId() + ",";
                view.setBackgroundResource(R.drawable.table_resrved);
                //Toast.makeText(this, "T"+view.getId(), Toast.LENGTH_SHORT).show();
                tableId = view.getId();






                            tableids.add(tableId);
                           // Toast.makeText(this, ""+tableids, Toast.LENGTH_SHORT).show();




                    String tableId1 = "T"+tableId;
                    tableids1.add(tableId1);
                    Toast.makeText(BookingActivity.this, ""+tableids1, Toast.LENGTH_SHORT).show();













                payinglayout.setVisibility(view.VISIBLE);







            }
        } else if ((int) view.getTag() == STATUS_BOOKED) {
            Toast.makeText(this, "Seat " + view.getId() + " is Booked", Toast.LENGTH_SHORT).show();
        } else if ((int) view.getTag() == STATUS_RESERVED) {
            Toast.makeText(this, "Seat " + view.getId() + " is Reserved", Toast.LENGTH_SHORT).show();
        }
    }
}