import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Interceptor
public class FhirLoggingInterceptor implements IClientInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(FhirLoggingInterceptor.class);

    @Override
    public void interceptRequest(IHttpRequest request) {
        this.logger.info("Client request: {}", request);
    }

    @Override
    public void interceptResponse(IHttpResponse response) throws IOException {
        this.logger.info("Response Time for {}",response.getRequestStopWatch().toString());
    }
}
