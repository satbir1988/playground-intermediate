import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class SampleClient {
    private static final Logger logger = LoggerFactory.getLogger(SampleClient.class);
    private static boolean cacheDisable = false;

    public static void main(String[] theArgs) {

        // Create a FHIR client
        FhirContext fhirContext = FhirContext.forR4();
        IGenericClient client = fhirContext.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
        client.registerInterceptor(new FhirLoggingInterceptor());

        //Get patient names from file
        List<String> patientNameList = readPatientNamesFile();

        //Loop three times
        if(null != patientNameList && !patientNameList.isEmpty()) {
            IntStream.range(0, 3).forEach(index -> {
                long startTime = System.currentTimeMillis();
                if (index == 2) {
                    cacheDisable = true;
                }
                patientNameList.forEach(name -> {
                    searchPatient(client, name, cacheDisable);
                });
                logger.info("Response for loop {} : {}ms", index, System.currentTimeMillis() - startTime);

            });
        }
    }

    private static Bundle searchPatient(IGenericClient client, String patientName, boolean disableCache) {
        CacheControlDirective cacheControlDirective = new CacheControlDirective();
        cacheControlDirective.setNoCache(disableCache);
        return client
                .search()
                .forResource("Patient")
                .where(Patient.FAMILY.matches().value(patientName))
                .cacheControl(cacheControlDirective)
                .returnBundle(Bundle.class)
                .execute();
    }

    private static List<String> readPatientNamesFile() {
        List<String> names = new ArrayList<>();
        InputStream inputStream = SampleClient.class.getResourceAsStream("patient-names.txt");
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                names.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return names;
    }

}
