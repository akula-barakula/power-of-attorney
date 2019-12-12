package nl.rabobank.powerofattorney.stub;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

public class CustomResponseTemplateTransformer extends ResponseTemplateTransformer {

    private final ResponseDefinition fileNotFoundResponse = new ResponseDefinitionBuilder().withStatus(404).build();


    public CustomResponseTemplateTransformer() {
        super(true);
    }


    @Override
    public ResponseDefinition transform(
		    final Request request,
		    final ResponseDefinition responseDefinition,
		    final FileSource files,
		    final Parameters parameters) {

        try {
            return super.transform(request, responseDefinition, files, parameters);

        } catch (Exception e) {
            return fileNotFoundResponse;
        }
    }
}