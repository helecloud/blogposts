package lambda_native_demo.service;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import lambda_native_demo.model.DemoResourceDto;
import lambda_native_demo.model.FoobarResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient
@ApplicationScoped
public interface FoobarApiService {
  @GET
  @Path("/foobar/search")
  FoobarResponse searchDemoResource(@HeaderParam("Authorization") String authorization, @QueryParam("term") String term);

  @POST
  @Path("/foobar")
  FoobarResponse createDemoResource(@HeaderParam("Authorization") String authorization, DemoResourceDto dto);

  @PUT
  @Path("/foobar/{id}")
  FoobarResponse updateDemoResource(@HeaderParam("Authorization") String authorization, DemoResourceDto dto, @PathParam("id") Integer id);
}
