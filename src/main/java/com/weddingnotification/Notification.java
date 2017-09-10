package com.weddingnotification;

import com.weddingnotification.util.StringUtil;
import com.weddingnotification.httpClient.HttpClient;
import com.weddingnotification.status.ApiStatus;

import org.json.JSONObject;
import org.json.JSONArray;

import java.util.List;

import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;


/**
 * @author Neel Jain
 */
public class Notification {

    private static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJq"
                                        + "dGkiOiJiYzBkNGY0Mi0zY2RiLTQ5MzItYTM4YS1hZ" 
                                        + "TRlM2FhMTI1YTAifQ.HE055uaemHssulDcWRKeqCB" 
                                        + "AO6pmGP_hV6uK_4q6G-w"; 

    private JSONObject notificationMessage;
        // complete notification message to be sent to ionic publisher

    public Notification(String profile, String message, List<String> devices) {
        notificationMessage = constructJsonMessage(profile, devices, message);
    }

    public void post() {
        try {
            HttpClient client = new HttpClient("https://api.ionic.io/push/notifications");
            client.method("POST");
            client.property("content-type", "application/json");
            client.property("Authorization", "Bearer "+API_KEY);
            client.property("Accept", "application/json");
            client.property("User-Agent", 
                "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

            Writer writer = new BufferedWriter(
                              new OutputStreamWriter(client.connection().getOutputStream(), "UTF-8"));
            writer.write(notificationMessage.toString());
            writer.close();

            if (client.connection().getResponseCode() == ApiStatus.CREATED.getStatus()) {
                System.out.println(StringUtil.getStringFromInputStream(client.connection().getInputStream()));
            } else {
                System.out.println(client.connection().getResponseCode());
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JSONObject constructJsonMessage(String profile, List<String> tokens, String message) {
        JSONObject holder = new JSONObject();
        holder.put("tokens", tokens);
        holder.put("profile", profile);
        JSONObject body = new JSONObject();
        body.put("message", message);
        holder.put("notification", body);
        return holder;
    }
}
