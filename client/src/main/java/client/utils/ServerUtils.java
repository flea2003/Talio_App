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

import commons.Board;
import commons.Card;
import commons.Subtask;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils {

    private String server;

    /**
     * gets the current server
     * @return the current server
     */
    public String getServer() {
        return server;
    }

    /**
     * constructor
     * @param server the current server
     */
    @Inject
    public ServerUtils(String server){
        this.server =server;
    }

    /**
     * sets the current server
     * @param server the server to be set
     */
    public void setServer(String server){
        this.server =server;
    }

    /**
     * sends a get request to trigger the respective method in cardController
     * gets all the cards from the database
     * @return the cards
     */
    public List<Card> getCards(){
        String endpoint = String.format("api/cards");
        return  ClientBuilder.newClient(new ClientConfig())
                .target(server).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Card>>() {});
    }

    /**
     * sends a post request to trigger the respective method in cardController
     * adds a card
     * @param card the card to be added
     * @return the added card
     */
    public Card addCard(Card card){
        String endpoint = String.format("api/cards", card.id);
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path(endpoint)
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .post(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    /**
     * sends a post request to trigger the respective method in cardController
     * updates a card with a new one
     * @param card the new card
     * @return the updated card
     */
    public Card updateCard(Card card){
        String endpoint = String.format("api/cards");

        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    /**
     * sends a delete request to trigger the respective method in cardController
     * deletes a card
     * @param id the id of the card to be deleted
     * @return the deleted card
     */
    public Card deleteCard(long id){
        String endpoint = String.format("api/cards/delete/%d", id);
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete(new GenericType<Card>() {});
    }

    /**
     * sends a get request to trigger the respective method in listController
     * gets all the lists in the database
     * @return the lists
     */
    public List<commons.List> getLists(){
        String endpoint = String.format("api/lists");
        List<commons.List>res =  ClientBuilder.newClient(new ClientConfig())
                .target(server).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<commons.List>>() {});
        return res;
    }

    /**
     * sends a get request to trigger the respective method in listController
     * gets a list from the database
     * @param id the id of the list to be gotten
     * @return the gotten list
     */
    public commons.List getList(long id){
        String endpoint = String.format("api/lists/%d", id);
        return  ClientBuilder.newClient(new ClientConfig())
                .target(server).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<commons.List>() {});
    }

    /**
     * sends a post request to trigger the respective method in listController
     * adds a list in the database
     * @param list the list to be added
     * @return the added list
     */
    public commons.List addList(commons.List list){
        String endpoint = String.format("api/lists");
        return  ClientBuilder.newClient(new ClientConfig())
                .target(server).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(list, APPLICATION_JSON), commons.List.class);
    }

    /**
     * sends a post request to trigger the respective method in listController
     * updates a list with a new one
     * @param list the new list
     * @return the updated list
     */
    public commons.List updateList(commons.List list){
        int indx = 0;
        for(Card card : list.cards){
            ++indx;
            card.setNumberInTheList(indx);
        }
        String endpoint = String.format("api/lists/update");
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(list, APPLICATION_JSON), commons.List.class);

    }

    /**
     * sends a get request to trigger the respective method in listController
     * gets a specific list from the database
     * @param id the id of the list to be gotten
     * @return the gotten list
     */
    public commons.List getListById(long id){
        String endpoint = String.format("/api/lists/%d", id);
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(commons.List.class);
    }

    /**
     * sends a delete request to trigger the respective method in listController
     * deletes a list
     * @param id the id of the list to be deleted
     * @return the deleted list
     */
    public commons.List deleteList(long id){
        String endpoint = String.format("api/lists/delete/%d", id);
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete(new GenericType<commons.List>() {});
    }

    /**
     * sends a get request to trigger the respective method in boardController
     * gets all the boards from the database
     * @return the boards
     */
    public List<commons.Board> getBoards(){
        String endpoint = String.format("api/boards");
        return  ClientBuilder.newClient(new ClientConfig())
                .target(server).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<commons.Board>>() {});
    }

    /**
     * sends a get request to trigger the respective method in boardController
     * gets a specific board from the database
     * @param id the id of the board to be gotten
     * @return the gotten board
     */
    public commons.Board getBoard(long id){
        String endpoint = String.format("api/boards/%d", id);
        var res = ClientBuilder.newClient(new ClientConfig())
                .target(server).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<commons.Board>() {});
        Collections.sort(res.getLists(), Comparator.comparingInt(commons.List::getNumberInTheBoard));
        for(commons.List list : res.getLists()){
            Collections.sort(list.getCards(), Comparator.comparingInt(Card::getNumberInTheList));
        }
        return res;
    }

    /**
     * sends a get request to trigger the respective method in boardController
     * gets a specific board using its key
     * @param key the key of the board to be gotten
     * @return the gotten board
     */
    public commons.Board getBoardByKey(String key){
        String endpoint = String.format("api/boards/key/%s", key);
        return  ClientBuilder.newClient(new ClientConfig())
                .target(server).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<commons.Board>() {});
    }

    /**
     * sends a post request to trigger the respective method in boardController
     * adds a board to the database
     * @param board the board to be added
     * @return the added board
     */
    public commons.Board addList(commons.Board board){
        String endpoint = String.format("api/boards");
        return  ClientBuilder.newClient(new ClientConfig())
                .target(server).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(board, APPLICATION_JSON), commons.Board.class);
    }

    /**
     * sends a post request to trigger the respective method in boardController
     * updates a board with a new one
     * @param board the new board
     * @return the updated board
     */
    public Board updateBoard(Board board){
        int indxList = 0;
        for(commons.List list : board.lists){
            ++indxList;
            list.numberInTheBoard = indxList;
            if(list.getCards() != null) {
                int indxCard = 0;
                for (Card card : list.getCards()) {
                    ++indxCard;
                    card.setNumberInTheList(indxCard);
                }
            }
        }
        String endpoint = String.format("api/boards/update");
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(board, APPLICATION_JSON), Board.class);
    }

    /**
     * sends a delete request to trigger the respective method in boardController
     * deletes a specific board
     * @param id the id of the board to be deleted
     * @return the deleted board
     */
    public commons.Board deleteBoard(long id){
        String endpoint = String.format("api/boards/delete/%d", id);
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete(new GenericType<commons.Board>() {});
    }

    /**
     * sends a post request to trigger the respective method in boardController
     * adds a board to the database
     * @param board the board to be added
     * @return the added board
     */
    public commons.Board addBoard(Board board){
        String endpoint = String.format("api/boards/", board);
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(board, APPLICATION_JSON), Board.class);
    }

    public void deleteSubtask(Subtask subtask){
        String endpoint = String.format("api/subtasks/", subtask);
        ClientBuilder.newClient(new ClientConfig())
                .target(server).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(subtask, APPLICATION_JSON), Subtask.class);
    }

    public Subtask saveSubtask(Subtask subtask){
        String endpoint = String.format("api/subtasks/", subtask);
        return  ClientBuilder.newClient(new ClientConfig())
                .target(server).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(subtask, APPLICATION_JSON), Subtask.class);
    }

    private StompSession session ;

    /**
     * getter for session used for testing
     * @return the session
     */
    public StompSession getSession() {
        return session;
    }

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
    public <T> void refreshBoards(String destination, Type type, Consumer<T> consumer){
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

    /**
     * sends an object to a specific destiantion
     * @param destination the destination where the object will be sent
     * @param o the object to be sent
     */
    public void send(String destination,Object o){
        session.send(destination, o);
    }

    /**
     * sends a post request to trigger the respective method in subtaskController
     * adds a subtask
     * @param subtask the subtask to be added
     * @return the added subtask
     */
    public Subtask addSubtask(Subtask subtask){
        String endpoint = String.format("api/subtasks", subtask.id);
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path(endpoint)
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .post(Entity.entity(subtask, APPLICATION_JSON), Subtask.class);
    }

    /**
     * sends a post request to trigger the respective method in SubtaskController
     * updates a subtask with a new one
     * @param subtask the new subtask
     * @return the updated subtask
     */
    public commons.Subtask updateSubtask(commons.Subtask subtask){
        String endpoint = String.format("api/subtasks/update");
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(subtask, APPLICATION_JSON), commons.Subtask.class);
    }

    /**
     * sends a get request to trigger the respective method in subtaskController
     * gets a specific subtask from the database
     * @param id the id of the subtask to be gotten
     * @return the gotten subtask
     */
    public commons.Subtask getSubtaskById(long id){
        String endpoint = String.format("/api/subtasks/%d", id);
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(commons.Subtask.class);
    }

    /**
     * sends a delete request to trigger the respective method in subtaskController
     * deletes a subtask
     * @param id the id of the subtask to be deleted
     * @return the deleted subtask
     */
    public commons.Subtask deleteSubtask(long id){
        String endpoint = String.format("api/subtasks/delete/%d", id);
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete(new GenericType<commons.Subtask>() {});
    }

    /**
     * sends a get request to trigger the respective method in subtaskController
     * gets all the subtasks from the database
     * @return the subtasks
     */
    public List<Subtask> getSubtasks(){
        String endpoint = String.format("api/subtasks");
        return  ClientBuilder.newClient(new ClientConfig())
                .target(server).path(endpoint)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Subtask>>() {});
    }

    /**
     * sends a get request to trigger the respective method in cardController
     * gets a specific card from the database
     * @param id the id of the card to be gotten
     * @return the gotten card
     */
    public commons.Card getCardById(long id){
        String endpoint = String.format("/api/cards/%d", id);
        try{
            return ClientBuilder.newClient(new ClientConfig())
                    .target(server).path(endpoint)
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .get(commons.Card.class);
        }catch(Exception e){
            return null;
        }
    }
}
