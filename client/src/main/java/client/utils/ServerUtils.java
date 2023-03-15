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

import client.scenes.MainCtrl;
import client.scenes.ServerConnectCtrl;
import com.google.inject.Provides;
import commons.Board;
import commons.Card;
import commons.Quote;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientConfig;

import javax.inject.Inject;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils {

    private String SERVER;

    @Inject
    public ServerUtils(String server){
        SERVER=server;
    }

    public void setSERVER(String server){
        SERVER=server;
        System.out.println(SERVER);
    }

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

    public List<Card> getCards(){
        String endpoint = String.format("api/cards");
        return  ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Card>>() {});
    }

    public Card addCard(Card card){
        String endpoint = String.format("api/cards", card.id);
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path(endpoint)
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .post(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    public Card updateCard(Card card){
        String endpoint = String.format("api/cards");
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    public Card deleteCard(long id){
        String endpoint = String.format("api/cards/delete/%2d", id);
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete(new GenericType<Card>() {});
    }

    public List<commons.List> getLists(){
        String endpoint = String.format("api/lists");
        List<commons.List>res =  ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<commons.List>>() {});
        return res;
    }

    public commons.List getList(long id){
        String endpoint = String.format("api/lists/%2d", id);
        return  ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<commons.List>() {});
    }

    public commons.List addList(commons.List list){
        String endpoint = String.format("api/lists");
        return  ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(list, APPLICATION_JSON), commons.List.class);
    }

    public commons.List updateList(commons.List list){
        String endpoint = String.format("api/lists/update");
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(list, APPLICATION_JSON), commons.List.class);
    }

    public commons.List updateListName(commons.List list){
        String endpoint = String.format("api/lists/changeName/%d", list.id);
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(list, APPLICATION_JSON), commons.List.class);
    }

    public commons.List deleteList(long id){
        String endpoint = String.format("api/lists/delete/%2d", id);
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete(new GenericType<commons.List>() {});
    }

    public List<commons.Board> getBoards(){
        String endpoint = String.format("api/boards");
        return  ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<commons.Board>>() {});
    }

    public commons.Board getBoard(long id){
        String endpoint = String.format("api/boards/%2d", id);
        return  ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<commons.Board>() {});
    }

    public commons.Board addList(commons.Board board){
        String endpoint = String.format("api/boards");
        return  ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(board, APPLICATION_JSON), commons.Board.class);
    }

    public commons.Board updateBoard(commons.Board board){
        String endpoint = String.format("api/lists/changeName/%2d", board.id);
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(board, APPLICATION_JSON), commons.Board.class);
    }

    public commons.Board deleteBoard(long id){
        String endpoint = String.format("api/lists/delete/%2d", id);
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete(new GenericType<commons.Board>() {});
    }
//    public List<commons.List> getLists(){
//        List<commons.List>list = new ArrayList<>();
//        ArrayList<Card> cards = new ArrayList<>();
//        cards.add(new Card("Uno Dos", "Card2", null));
//        cards.add(new Card("HAHAHHA", "Card3", null));
//        cards.add(new Card("Ole", "Card4", null));
//
//        ArrayList<Card> cards2 = new ArrayList<>();
//        cards2.add(new Card("test", "LMAO", null));
//        cards2.add(new Card("wext", "ROFL", null));
//        cards2.add(new Card("rest", "Card4", null));
//
//        list.add(new commons.List(1, cards, "Test", null));
//        list.add(new commons.List(2, cards2, "Testing", null));
//        return list;
//
//    }
}
