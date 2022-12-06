package com.example.alineshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdaptor extends RecyclerView.Adapter<ItemsAdaptor.ItemsAdapterVh> {

    private List<ItemModel> m_itemModelList;

    public ItemsAdaptor(List<ItemModel> itemModelList) {
        this.m_itemModelList = itemModelList;
    }

    @NonNull
    @Override
    public ItemsAdapterVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_users, parent, false);
        return new ItemsAdapterVh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsAdaptor.ItemsAdapterVh holder, int position) {
        ItemModel itemModel = m_itemModelList.get(position);

        String name = itemModel.getName();
        String prefix = itemModel.getName().substring(0, 1);

        holder.tvUsername.setText(name);
        holder.tvPrefix.setText(prefix);
    }

    @Override
    public int getItemCount() {
        return m_itemModelList.size();
    }

//    @Override
//    public Filter getFilter() {
//
//        Filter filter = new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence charSequence) {
//                FilterResults filterResults = new FilterResults();
//
//                if (charSequence == null | charSequence.length() == 0) {
//                    filterResults.count = m_getItemModelListFiltered.size();
//                    filterResults.values = m_getItemModelListFiltered;
//                } else {
//                    String searchChar = charSequence.toString().toLowerCase();
//                    List<ItemModel> resultData = new ArrayList<>();
//
//                    for (ItemModel itemModel : m_getItemModelListFiltered) {
//                        if (itemModel.getName().toLowerCase().contains(searchChar)) {
//                            resultData.add(itemModel);
//                        }
//                    }
//                    filterResults.count = resultData.size();
//                    filterResults.values = resultData;
//                }
//
//                return filterResults;
//            }
//
//            @Override
//            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//                m_itemModelList = (List<ItemModel>) filterResults.values;
//                notifyDataSetChanged();
//            }
//        };
//        return filter;
//    }

    public interface SelectedItem { void selectedItem(ItemModel itemModel); }

    public class ItemsAdapterVh extends RecyclerView.ViewHolder {

        private TextView tvPrefix, tvUsername;
        private ImageView imIcon;
        View m_view;

        public ItemsAdapterVh(@NonNull View itemView) {
            super(itemView);

            m_view = itemView;

            tvPrefix = m_view.findViewById(R.id.prefix);
            tvUsername = m_view.findViewById(R.id.username);
            imIcon = m_view.findViewById(R.id.imageView);

            m_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //m_selectedItem.selectedItem(m_itemModelList.get(getAbsoluteAdapterPosition()));
                }
            });
        }
    }
}
