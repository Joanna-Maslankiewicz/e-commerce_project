package pl.jmaslankiewicz.sales.payment;

public interface PaymentGateway {
    PaymentData register(RegisterPaymentRequest request);
}
