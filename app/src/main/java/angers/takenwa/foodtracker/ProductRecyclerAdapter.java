package angers.takenwa.foodtracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.NewsViewHolder>{


    List<Product> productList;
    ProductRecyclerAdapter(List<Product> productList){
        this.productList = productList;
    }

    private OnProductClickListener productClickListener;

    ProductRecyclerAdapter(List<Product> productList, OnProductClickListener productClickListener) {
        this.productList = productList;
        this.productClickListener = productClickListener;
    }




    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return null;
        View view = LayoutInflater.from(parent. getContext()). inflate(R.layout.product,parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.nameTextView.setText(product.getProductName());
        holder.dateTextView.setText(product.getExpirationDate());
        Picasso.get().load(product.getImageUri())
                .error(R.drawable.food_bank)
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productClickListener.onProductClick(product);
            }
        });


    }


    @Override
    public int getItemCount() {
        //return 0;
        return productList.size();
    }

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    class NewsViewHolder
            extends RecyclerView. ViewHolder{
        TextView nameTextView, dateTextView;
        ImageView imageView;
        public NewsViewHolder (@NonNull View itemView) {
            super (itemView);
            nameTextView = itemView.findViewById(R.id.product_name);//findViewbyId(R.id.article_title);  // @+id/product_name
            dateTextView = itemView.findViewById(R.id.expiration_date); // @+id/expiration_date
            imageView = itemView.findViewById(R.id.product_image); // @+id/product_image
        }
    }
}





