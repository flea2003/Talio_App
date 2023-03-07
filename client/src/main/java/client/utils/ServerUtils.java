/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import commons.Card;
import org.glassfish.jersey.client.ClientConfig;

import commons.Quote;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

public class ServerUtils {

    private static final String SERVER = "http://localhost:8080/";

    public void getQuotesTheHardWay() throws IOException {
        var url = new URL("http://localhost:8080/api/quotes");
        var is = url.openConnection().getInputStream();
        var br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    public List<Quote> getQuotes() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/quotes") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Quote>>() {});
    }

    public Quote addQuote(Quote quote) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/quotes") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(quote, APPLICATION_JSON), Quote.class);
    }

    public List<commons.List> getLists(){
        List<commons.List>list = new ArrayList<>();
        Map<Long, Card> cards = new HashMap<>();
        cards.put(2L, new Card(2, "Card2"));
        cards.put(3L, new Card(3, "Card3"));
        cards.put(4L, new Card(4, "Card4"));
        Map<Long, Card> cards2 = new HashMap<>();
        cards.put(1L, new Card(1, "LMAO"));
        cards.put(2L, new Card(2, "LOL"));
        cards.put(3L, new Card(3, "ROFL"));
        cards.put(4L, new Card(4, "Card4"));
        cards.put(5L, new Card(2, "LOL"));
        cards.put(6L, new Card(3, "ROFL"));
        cards.put(7L, new Card(4, "Card4"));
        cards.put(8L, new Card(2, "LOL"));
        cards.put(9L, new Card(3, "ROFL"));
        cards.put(10L, new Card(4, "Card4"));
        list.add(new commons.List(1, cards2, "Victor"));
        list.add(new commons.List(1, cards, "Rafael"));
        list.add(new commons.List(1, cards, "Antreas"));
        list.add(new commons.List(1, cards, "Victor"));
        list.add(new commons.List(1, cards2, "Rafael"));
        list.add(new commons.List(1, cards, "Antreas"));
        list.add(new commons.List(1, cards2, "Victor"));
        list.add(new commons.List(1, cards, "Rafael"));
        return list;
    }
}