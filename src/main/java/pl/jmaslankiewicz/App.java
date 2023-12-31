package pl.jmaslankiewicz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import pl.jmaslankiewicz.payu.PayU;
import pl.jmaslankiewicz.payu.PayUApiCredentials;
import pl.jmaslankiewicz.productcatalog.HashMapProductStorage;
import pl.jmaslankiewicz.productcatalog.ProductCatalog;
import pl.jmaslankiewicz.sales.Sales;
import pl.jmaslankiewicz.sales.cart.CartStorage;
import pl.jmaslankiewicz.sales.offering.OfferCalculator;
import pl.jmaslankiewicz.sales.payment.PaymentGateway;
import pl.jmaslankiewicz.sales.payment.PayuPaymentGateway;
import pl.jmaslankiewicz.sales.productdetails.InMemoryProductDetailsProvider;
import pl.jmaslankiewicz.sales.productdetails.ProductCatalogProductDetailsProvider;
import pl.jmaslankiewicz.sales.productdetails.ProductDetailsProvider;
import pl.jmaslankiewicz.sales.reservation.InMemoryReservationStorage;
import pl.jmaslankiewicz.web.SessionCurrentCustomerContext;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    ProductCatalog createNewProductCatalog() {
        ProductCatalog productCatalog = new ProductCatalog(new HashMapProductStorage());

        String productId1 = productCatalog.addProduct("Applying UML and Patterns", "Craig Larman");
        productCatalog.assignImage(productId1, "/image/book_1.jpg");
        productCatalog.changePrice(productId1, BigDecimal.TEN);
        productCatalog.publishProduct(productId1);

        String productId2 = productCatalog.addProduct("Clean Code", "Robert Martin");
        productCatalog.assignImage(productId2, "/image/book_2.jpg");
        productCatalog.changePrice(productId2, BigDecimal.valueOf(20.20));
        productCatalog.publishProduct(productId2);

        String productId3 = productCatalog.addProduct("Domain-Driven Design", "Eric Evans");
        productCatalog.assignImage(productId3, "/image/book_3.jpg");
        productCatalog.changePrice(productId3, BigDecimal.valueOf(30.30));
        productCatalog.publishProduct(productId3);

        return productCatalog;
    }

    @Bean
    PaymentGateway createPaymentGateway() {
        return new PayuPaymentGateway(new PayU(PayUApiCredentials.sandbox(), new RestTemplate()));
    }

    @Bean
    Sales createSales(ProductDetailsProvider productDetailsProvider, PaymentGateway paymentGateway) {
        return new Sales(
                new CartStorage(),
                productDetailsProvider,
                new OfferCalculator(productDetailsProvider),
                paymentGateway,
                new InMemoryReservationStorage());
    }

    @Bean
    SessionCurrentCustomerContext currentCustomerContext(HttpSession httpSession) {
        return new SessionCurrentCustomerContext(httpSession);
    }

    @Bean
    ProductDetailsProvider createProductDetailsProvider(ProductCatalog catalog) {
        return new ProductCatalogProductDetailsProvider(catalog);
    }
}
