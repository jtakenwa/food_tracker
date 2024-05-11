package angers.takenwa.foodtracker;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.content.Context;
import android.util.AttributeSet;


public class ProductCard extends RelativeLayout {
    // Constructor
    public ProductCard(Context context) {
        super(context);
        init();
    }

    // Additional Constructors for XML attributes
    public ProductCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    // Additional Constructors for styles
    public ProductCard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    // Initialize method
    private void init() {
        // Inflate the layout for the custom component
        LayoutInflater.from(getContext()).inflate(R.layout.product_card_layout, this, true);
    }
}

