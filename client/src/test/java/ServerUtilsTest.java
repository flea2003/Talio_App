import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;

import jakarta.ws.rs.core.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import client.utils.ServerUtils;
import commons.Card;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ServerUtilsTest{
    @Mock
    private WebTarget webTarget;

    @Mock
    private Invocation.Builder invocationBuilder;

    @InjectMocks
    private ServerUtils cardService;

    @Test
    public void testAddCard() {
        // Create a mock card object to use in the test
        Card card = new Card();
        card.id = 1;
        card.setName("Test Card");

        // Set up the mock behavior for the WebTarget and Invocation.Builder objects
        when(webTarget.path(anyString())).thenReturn(webTarget);
        when(webTarget.request(any(MediaType.class))).thenReturn(invocationBuilder);
        when(invocationBuilder.accept(any(MediaType.class))).thenReturn(invocationBuilder);
        when(invocationBuilder.post(any(Entity.class), eq(Card.class))).thenReturn(card);

        // Call the addCard method on the CardService instance and verify the result
        Card result = cardService.addCard(card);
        assertEquals(card.getId(), result.getId());
        assertEquals(card.getName(), result.getName());
    }
}