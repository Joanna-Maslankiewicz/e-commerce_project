package pl.jmaslankiewicz.sales.productdetails;

import pl.jmaslankiewicz.sales.productdetails.ProductDetails;

import java.util.Optional;

public interface ProductDetailsProvider {
    public Optional<ProductDetails> load(String productId);
}
