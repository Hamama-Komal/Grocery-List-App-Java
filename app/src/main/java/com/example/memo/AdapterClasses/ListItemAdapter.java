package com.example.memo.AdapterClasses;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memo.Activities.UpdateActivity;
import com.example.memo.ModelClasses.ListItemModel;
import com.example.memo.R;
import com.example.memo.databinding.RecyclerItemLayoutBinding;

import java.util.ArrayList;

public class ListItemAdapter  extends RecyclerView.Adapter<ListItemAdapter.MyViewHolder> {

    Context context;
    ArrayList<ListItemModel> list;

    public ListItemAdapter(Context context, ArrayList<ListItemModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ListItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemAdapter.MyViewHolder holder, int position) {

        ListItemModel model = list.get(position);

        String id = String.valueOf(position+1);
        holder.binding.txtId.setText(id);
        holder.binding.itemName.setText(model.getItem());
        holder.binding.itemQuantity.setText(model.getQuantity());
        holder.binding.itemPrice.setText(model.getPrice());
        holder.binding.textDate.setText(model.getDate());

        holder.itemView.setOnClickListener(view -> {

            Intent intent = new Intent(context, UpdateActivity.class);
            intent.putExtra("id", list.get(position).getId());
            intent.putExtra("item", list.get(position).getItem());
            intent.putExtra("quantity", list.get(position).getQuantity());
            intent.putExtra("price", list.get(position).getPrice());
            context.startActivity(intent);


        });



        holder.itemView.setOnLongClickListener(view -> {


            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Confirmation");
            builder.setMessage("Do you want to set this item checked?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Set the check icon visible
                    holder.binding.checkIcon.setVisibility(View.VISIBLE);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Do nothing or handle other actions
                    holder.binding.checkIcon.setVisibility(View.INVISIBLE);
                }
            });
            builder.show();
            return true;

        });
    }

    private void showCheckMark(View itemView, int position) {


    }


    private void showMoreOptions() {
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        RecyclerItemLayoutBinding binding;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RecyclerItemLayoutBinding.bind(itemView);
        }
    }
}

