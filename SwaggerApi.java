package automationfiles;

import static io.restassured.RestAssured.*;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import junit.framework.Assert;

public class SwaggerApi {
	
	String access_token;


	@Test(priority=1)
	public void getAccessToken() {
		
	Response response=given().when().header("Authorization", "Basic dXBza2lsbHNfcmVzdF9hZG1pbl9vYXV0aF9jbGllbnQ6dXBza2lsbHNfcmVzdF9hZG1pbl9vYXV0aF9zZWNyZXQ=")
	.log().all()
	.post("http://rest-api.upskills.in/api/rest_admin/oauth2/token/client_credentials");
	response.prettyPrint();
	System.out.println("Status Code is " +response.statusCode());
	access_token=response.asString().substring(276, 316);
	System.out.println(access_token);
	
	}
	
	
	@Test(priority=2, dependsOnMethods="getAccessToken")
	public void getAdminLogin() {
		 String payload = "{\n" +
		            "    \"username\":\"upskills_admin\",\n" +
		            "    \"password\":\"Talent4$$\"\n" +
		            "}";
		Response response=given().headers("Content-Type", "application/json").body(payload).auth().oauth2(access_token)
		.when().post("http://rest-api.upskills.in/api/rest_admin/login");
		 
		response.prettyPrint();
		//System.out.println("Status Code is " +response.statusCode());
		Assert.assertEquals(response.getStatusCode(), 200);

		}
	
	
	@Test(priority=3, dependsOnMethods="getAdminLogin")
	public void addnewcustomer(){
        String payload =" {\r\n"
        		+ "\"firstname\": \"User\",\r\n"
        		+ "\"lastname\": \"preeto\",\r\n"
        		+ "\"email\": \"preeto@gmail.com\",\r\n"
        		+ "\"password\": \"password\",\r\n"
        		+ "\"confirm\": \"password\", \r\n"
        		+ "\"telephone\": \"1-541-754-3010\"\r\n"
        		+ "}";
        Response responsePost = RestAssured.given().header("Content-Type","application/json").body(payload).auth().oauth2(access_token)
                .when().post("http://rest-api.upskills.in/api/rest_admin/customers");
        String responsebody = responsePost.getBody().asString();
        System.out.println(responsebody);
        //Assert.assertEquals(responsePost.getStatusCode(), 200);


		}
	
	@Test(priority=4, dependsOnMethods="getAdminLogin")
	public void addonfilter(){
        
        Response responsePost = RestAssured.given().header("added_on", "2017-04-30").header("Content-Type","application/json").auth().oauth2(access_token)
                .when().post("http://rest-api.upskills.in/api/rest_admin/customers/added_on/2017-04-30");
        String responsebody = responsePost.getBody().asString();
        System.out.println(responsebody);
       
		}
	
	
	@Test(priority=5, dependsOnMethods="getAdminLogin")
	public void limitfilter(){
        
        Response responsePost =given().header("limit=6", "page=1", "Content-Type","application/json").auth().oauth2(access_token)
                .when().get("http://rest-api.upskills.in/api/rest_admin/customers/limit/6/page/1");
        String responsebody = responsePost.getBody().asString();
        System.out.println(responsebody);
	}
	
	
	@Test(priority=6, dependsOnMethods="getAdminLogin")
	public void getAdminLogout() {
		 
		Response response=given().headers("Content-Type", "application/json").when().auth().oauth2(access_token)
				.post("http://rest-api.upskills.in/api/rest_admin/logout");
		 
		response.prettyPrint();
		System.out.println("Status Code is " +response.statusCode());
	
		}
	
	
}
