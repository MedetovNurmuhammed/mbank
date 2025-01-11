package mb.service;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

public interface StripeService {
     Charge createCharge(String token, Double amount) throws StripeException;
}
