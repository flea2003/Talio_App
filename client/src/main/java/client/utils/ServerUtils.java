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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.inject.Provides;
import commons.Board;
import commons.Card;
import commons.Quote;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.apache.logging.log4j.util.PropertySource;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.inject.Inject;
import java.io.*;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils {

    private String SERVER;

    public String getSERVER() {
        return SERVER;
    }

    @Inject
    public ServerUtils(String server){
        SERVER=server;
    }

    public void setSERVER(String server){
        SERVER=server;
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
                .post(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    public Card deleteCard(long id){
        String endpoint = String.format("api/cards/delete/%d", id);
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
        Collections.sort(res, Comparator.comparingInt(commons.List::getNumberInTheBoard));
        for(commons.List list : res){
            Collections.sort(list.getCards(), Comparator.comparingInt(Card::getNumberInTheList));
        }
        return res;
    }

    public commons.List getList(long id){
        String endpoint = String.format("api/lists/%d", id);
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
        int indx = 0;
        for(Card card : list.cards){
            ++indx;
            card.setNumberInTheList(indx);
        }
        String endpoint = String.format("api/lists/update");
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(list, APPLICATION_JSON), commons.List.class);

    }

//    public commons.List updateListName(commons.List list,String name){
//        String endpoint = String.format("api/lists/changeName/%d", list.id);
//        return ClientBuilder.newClient(new ClientConfig())
//                .target(SERVER).path(endpoint)
//                .request(APPLICATION_JSON)
//                .accept(APPLICATION_JSON)
//                .post(Entity.entity(name, APPLICATION_JSON), commons.List.class);
//    }

    public commons.List getListById(long id){
        String endpoint = String.format("/api/lists/%d", id);
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(commons.List.class);
    }


    public commons.List deleteList(long id){
        String endpoint = String.format("api/lists/delete/%d", id);
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


    public commons.Board getBoardByKey(String key){
        String endpoint = String.format("api/boards/key/%s", key);
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


//    public commons.Board updateBoard(commons.Board board){
//        String endpoint = String.format("api/lists/changeName/%2d", board.id);
//        return ClientBuilder.newClient(new ClientConfig())
//                .target(SERVER).path(endpoint)
//                .request(APPLICATION_JSON)
//                .accept(APPLICATION_JSON)
//                .post(Entity.entity(board, APPLICATION_JSON), commons.Board.class);
//    }

    public Board updateBoard(Board board){
        int indx = 0;
        for(commons.List list : board.lists){
            ++indx;
            list.numberInTheBoard=indx;
        }

        String endpoint = String.format("api/boards/update");
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(board, APPLICATION_JSON), Board.class);
    }

    public commons.Board deleteBoard(long id){
        String endpoint = String.format("api/boards/delete/%d", id);
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete(new GenericType<commons.Board>() {});
    }

    public commons.Board addBoard(Board board){
        String endpoint = String.format("api/boards/", board);
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(board, APPLICATION_JSON), Board.class);
    }

    private StompSession session ;

    /**
     * Creates a websocket connection
     * @param IP the IP address of the server to create a websocket connection
     */
    public void initialiseSession(String IP){
        session=connect("ws://"+IP+":8080/websocket");
    }

    /**
     * Creates a StompSession to be used for receiving updates on the database
     * @param url the url of the websocket connection
     * @return a StompSession to send and receive messages between the client and the server
     */
    private StompSession connect(String url){
        var client=new StandardWebSocketClient();
        var stomp=new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            return stomp.connect(url, new StompSessionHandlerAdapter() {}).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException();
    }

    /**
     * Subscribes to a destination on the WebSocket connection established by session,
     * is used to update the app with new information in real-time
     * @param destination the destination the session will subscribe to
     * @param type the type of the payload
     * @param consumer a consumer of objects of type "type"
     * @param <T> a general object enabling the generalization of the method to any type
     */
    public <T> void refreshLists(String destination,Type type, Consumer<T> consumer){
        session.subscribe(destination, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return type;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                consumer.accept((T) payload);
            }
        });
    }

    public void send(String destination,Object o){
        session.send(destination, o);
    }
}
