


import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.impl.GenericClient;
import org.hl7.fhir.r4.model.Bundle;
import org.junit.Test;

public class SampleClientTest {

    @Test
    public void testEmptyFile(){
         new GenericClient(){
             @Override
               Object execute(){
                 return new Bundle();
             };


         };
        SampleClient.main(new String[]{});
    }

}