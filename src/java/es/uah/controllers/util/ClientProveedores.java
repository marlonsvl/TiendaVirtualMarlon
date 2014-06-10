/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uah.controllers.util;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author marlonvinan
 */
public class ClientProveedores {

    public ClientProveedores() {
    }

    public static String getResource(String uri) {
        try {
            Client cliente = Client.create();
            WebResource webResurce =
                    cliente.resource(uri);
            ClientResponse response = webResurce.accept("application/json").get(ClientResponse.class);
            if (response.getStatus() != 200) {
                System.out.println(response.getStatus());
                return response.getStatus() + "";
            } else {
                return response.getEntity(String.class);
            }
        } catch (Exception ex) {
            //ex.printStackTrace();
            return "";
        }
    }
    
    public static void persist(String url, String json){
        //DefaultHttpClient httpClient = new DefaultHttpClient();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPut put = new HttpPut(url);
        try {
            StringEntity input = new StringEntity(json);
            input.setContentType("application/json");
            put.setEntity(input);
            httpclient.execute(put);
            System.out.println("successfully");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            System.err.println("error in ioexception "+ex);
            ex.printStackTrace();
        }finally{
            httpclient.getConnectionManager().shutdown();
        }
    }
    
    public static String jsonEditado(String json, int c){
        JSONArray arr = null;
        JSONObject obj = null;
        try {
            arr = new JSONArray(json);
            obj = arr.getJSONObject(0);
            int cant = obj.getInt("cantidad");
            int res = cant - c;
            obj.put("cantidad", res);
            System.out.println(">>>>>>>>>json editado res: "+res);
            System.out.println(">>>>>>>>>json editado cantidad: "+obj.getString("cantidad"));
        } catch (JSONException ex) {
            ex.printStackTrace();
            System.err.println(ex.getCause());
        }
        return obj.toString();
    }
    
    
    
//    public static void main(String [] args){
////        persist("http://localhost:8080/ProveedoresWS/webresources/productosprov/editPP", 
////                "[{\"productosProvPK\":{\"cod\":\"1\",\"cif\":\"2P\"},\"nombre\":\"Intel Core i7-4770K\",\"precio\":250.9,\"cantidad\":15,\"fechaent\":1385355600000,\"proveedores\":{\"cif\":\"2P\",\"nombre\":\"Prov2\",\"direccion\":\"Barcelona\",\"tfno\":\"936557766\"}}]");
//        //jsonEditado("[{\"productosProvPK\":{\"cod\":\"1\",\"cif\":\"2P\"},\"nombre\":\"Intel Core i7-4770K\",\"precio\":250.9,\"cantidad\":15,\"fechaent\":1385355600000,\"proveedores\":{\"cif\":\"2P\",\"nombre\":\"Prov2\",\"direccion\":\"Barcelona\",\"tfno\":\"936557766\"}}]", 30);
//        longToDate(1385355600000l);
//    }
}
