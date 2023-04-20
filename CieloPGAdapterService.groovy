import org.apache.log4j.Logger
import consus.constants.StringConstants
import grails.transaction.Transactional
import com.mphrx.commons.interfaces.IAdapterService
import org.apache.http.client.methods.HttpPost
import okhttp3.*;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import com.google.gson.Gson;

@Transactional
public class CieloPGAdapterService implements IAdapterService {

    private static Logger log = Logger.getLogger("com.mphrx.CieloPGAdapterService")


    public def routeToAction(Map requestParams) throws Exception {
        log.info("CieloPGAdapterService - Inside routeToAction with requestParams : " + requestParams)
        log.info("requestParams.action=" + requestParams.actionContext)
        def returnObject = null
        switch (requestParams.actionContext) {
                case "handleRequest":
                    returnObject = handleRequest(requestParams);
                    break;
                case "verifyChecksum":
                    returnObject = handleResponse(requestParams);
                    break
                case StringConstants.VALIDATE_INITIATE_PAYMENT:
                     returnObject = validateIntiatePayment(requestParams)
                    break
        }
        return returnObject;
    }

        List<String> fetchActionsForAdapter() {
            return [StringConstants.VALIDATE_INITIATE_PAYMENT , "handleRequest","verifyChecksum"]
    }


    private Map handleRequest(Map requestParams) {
        log.info("handleRequest() : Inside handleRequest to hit api with requestParams : " + requestParams)

        HttpPost request = new HttpPost("https://cieloecommerce.cielo.com.br/api/public/v1/orders");
        request.addHeader("MerchantId", "b99ffa26-c0c1-44e5-806c-46ef9d61e9cc");
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Cookie", "ARRAffinity=24b69ec43a385a35f00243c885d57b9207d8ca54474a7de7492216dc4f81280d");

        Map reqMap =[
                "OrderNumber":requestParams.OrderNumber,
                "Cart":[
                        "Items":[
                                [
                                        "Name":requestParams.Name,
                                        "UnitPrice":requestParams.UnitPrice,
                                        "Quantity":requestParams.Quantity,
                                        "Type":requestParams.Type
                                ],
                        ]
                ],
                "Shipping":[
                        "TargetZipCode":"21911130",
                        "Type":requestParams.ShippingType,
                        "Services":[
                                [
                                        "Name":requestParams.ServiceName,
                                        "Price":requestParams.ServicePrice
                                ]
                        ],
                        "Address":[
                                "Street":"Rua Cambui",
                                "Number":"92",
                                "Complement":"Apto 201",
                                "District":"Freguesia",
                                "City":"Rio de Janeiro",
                                "State":"RJ"
                        ]
                ],
                "Customer":[
                        "Identity":requestParams.CustomerIdentity,
                       // "Identity":"84261300206",
                        "FullName":requestParams.CustomerFullName,
                        "Email":requestParams.CustomerEmail,
                        "Phone":requestParams.CustomerPhone
                ],
                "Options":[
                        //"ReturnUrl": requestParams.ReturnUrl,
                        "AccessToken" : requestParams.merchant_param1
                ]

        ]
//log.info("requestParams.CustomerIdentity - "+requestParams.CustomerIdentity)
        //log.info("inside handle request - request body for api hit is - "+reqMap)


            //map to json string


        ObjectMapper objectMapper = new ObjectMapper();

        String input = objectMapper.writeValueAsString(reqMap);
        log.info("it is json string - "+input);

        request.setEntity(new StringEntity(input));
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(request)
        System.out.println("Response code: " + response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity);
           log.info("it is result of api hit - "+result);

            //string to map


            HashMap<String, Object> responseMap = new Gson().fromJson(result, HashMap.class);
            log.info("response map to send to template config - "+responseMap)
            log.info("checkoutURL - "+responseMap.settings.checkoutUrl)
            return responseMap
        }

        else{
            print("no response found")
        }




    }
Map handleResponse(Map responseParams) throws Exception {
          return responseParams;
    }
boolean validateIntiatePayment(Map request){
        return true
    }
}

