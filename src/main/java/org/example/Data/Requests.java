package org.example.Data;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;


public class Requests {
    private Header prepareHeader(String key, String value) {
        return new Header(key, value);
    }

    protected Response sendPostRequest(String body) {
        return RestAssured.given()
                .baseUri(Uri.BASE.getValue())
                .basePath("/gateway/travelOffers/")
                .contentType(ContentType.JSON)
                .header(prepareHeader("X-Affiliate", "fru"))
                .body(body)
                .when()
                .post()
                .then()
                .extract().response();
    }

    protected Response sendGetRequest(String id) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .baseUri(Uri.BASE.getValue())
                .basePath("/gateway/travelOffers/{id}")
                .pathParam("id", id)
                .when()
                .get()
                .then()
                .extract().response();
    }
}
