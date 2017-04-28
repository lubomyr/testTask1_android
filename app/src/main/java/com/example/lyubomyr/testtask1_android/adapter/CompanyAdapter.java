package com.example.lyubomyr.testtask1_android.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.lyubomyr.testtask1_android.R;
import com.example.lyubomyr.testtask1_android.dialog.AddEditDialog;
import com.example.lyubomyr.testtask1_android.entity.Company;

import java.util.List;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.ViewHolder> {
    private int resources;
    private List<Company> items;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public CompanyAdapter(Context context, int resources, List<Company> items) {
        this.context = context;
        this.items = items;
        this.resources = resources;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(resources, parent, false);

        return new ViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Company item = items.get(position);
        holder.setItem(item);
        holder.nameTextView.setText(item.getName());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        List<Company> children = item.getChildren();
        CompanyAdapter adapter = new CompanyAdapter(context, R.layout.item_company, children);
        adapter.setOnItemClickListener(onItemClickListener);
        holder.recyclerView.setAdapter(adapter);
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, Company item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CompanyAdapter.OnItemClickListener onItemClickListener;
        TextView nameTextView;
        RecyclerView recyclerView;
        Button addButton;
        Button editButton;
        Button deleteButton;
        private Company item;

        ViewHolder(View itemView, CompanyAdapter.OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            nameTextView  = (TextView) itemView.findViewById(R.id.name);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view);
            addButton = (Button) itemView.findViewById(R.id.add_company);
            editButton = (Button) itemView.findViewById(R.id.edit_company);
            deleteButton = (Button) itemView.findViewById(R.id.delete_company);
            addButton.setOnClickListener(this);
            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);
        }

        public void setItem(Company item) {
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, item);
        }
    }
}