package com.example.chhotawhatsapp.UserActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chhotawhatsapp.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> implements Filterable {

    private  ArrayList<ExampleItem> mExampleList;
    private List<ExampleItem> mExampleListFull;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void removeImage(int position);
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public ImageView mRemoveImage;

        public ExampleViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView);
            mTextView2 = itemView.findViewById(R.id.textView2);
            mRemoveImage = itemView.findViewById(R.id.item_iv_remove);

            mRemoveImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION) {
                        listener.removeImage(position);
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public ExampleAdapter(ArrayList<ExampleItem> exampleList){
        mExampleList = exampleList;
        mExampleListFull = new ArrayList<>(exampleList);
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent,false);
        ExampleViewHolder evh = new ExampleViewHolder(v,mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        ExampleItem currentItem = mExampleList.get(position);

        Bitmap imageBitmap;

        imageBitmap = stringToBitmap(currentItem.getmImageBitMap());

        holder.mImageView.setImageBitmap(imageBitmap);
        holder.mTextView1.setText(currentItem.getmText1());
        holder.mTextView2.setText(currentItem.getmText2());
    }

    private String bitmapToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();
        String imageBitmap = Base64.encodeToString(b,Base64.DEFAULT);

        return imageBitmap;
    }

    private Bitmap stringToBitmap(String string){
        byte[] encodeByte = Base64.decode(string,Base64.DEFAULT);
        Bitmap imageBitmap = BitmapFactory.decodeByteArray(encodeByte,0,encodeByte.length);
        return imageBitmap;
    }
    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public void filterList(ArrayList<ExampleItem> filteredList){
        mExampleList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter(){
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ExampleItem> filteredList = new ArrayList<>();

            if(constraint==null || constraint.length()==0){
                filteredList.addAll(mExampleListFull);
            }
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(ExampleItem item: mExampleListFull){
                    if(item.getmText1().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mExampleList.clear();
            mExampleList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
}



