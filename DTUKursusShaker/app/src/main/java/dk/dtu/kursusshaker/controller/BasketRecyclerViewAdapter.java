package dk.dtu.kursusshaker.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.zip.Inflater;

import dk.dtu.kursusshaker.R;

public class BasketRecyclerViewAdapter extends RecyclerView.Adapter<BasketRecyclerViewAdapter.BasketViewHolder> {

    private static final String TAG = "BasketRecyclerViewAdapt";

    private Context mContext;

    SharedPreferences sp;

    ArrayList<String> mCourses;


    public BasketRecyclerViewAdapter(ArrayList<String> courses) {
        this.mCourses = courses;
    }

    @Override
    public BasketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FrameLayout v = (FrameLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_course_layout,parent,false);
        return new BasketViewHolder((TextView) v.findViewById(R.id.basket_course_text_view));
    }

    @Override
    public void onBindViewHolder(@NonNull BasketViewHolder holder, final int position) {
        Log.d(TAG, "OnBindViewHolder called bitch");
        holder.getTextView().setText(mCourses.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onclick: clicked on: " + mCourses.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    public class BasketViewHolder extends RecyclerView.ViewHolder {
        FrameLayout parentLayout;
        public TextView textView;

        public BasketViewHolder(TextView v) {
            super(v);
            parentLayout = v.findViewById(R.id.basket_course_layout_root);
            textView = (TextView) v.findViewById(R.id.basket_course_text_view);
        }

        public TextView getTextView() {
            return textView;
        }

    }
}
