package server.services;

import commons.Board;
import commons.Card;
import commons.Subtask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final SimpMessagingTemplate messagingTemplate;

    private String serverPassword;

    @Autowired
    public BoardService(BoardRepository boardRepository, SimpMessagingTemplate messagingTemplate) {
        this.boardRepository = boardRepository;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * generates a random password for the server after the server starts
     */
    @PostConstruct
    private void initPassword(){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        serverPassword = generatedString;
        System.out.println(serverPassword);
    }

    public Board getBoardById(long id) {
        Board board = boardRepository.findById(id).get();
        for(commons.List list : board.getLists()){
            Collections.sort(list.getCards(), Comparator.comparingInt(Card::getNumberInTheList));
            for(Card card : list.getCards()){
                Collections.sort(card.getSubtasks() , Comparator.comparingInt(Subtask::getNumberInTheCard));
            }
        }
        return board;
    }

    public Board saveBoard(Board board) {
        Board savedBoard = boardRepository.save(board);
        messagingTemplate.convertAndSend("/topic/updates", true);
        return savedBoard;
    }

    public void deleteBoard(Board board) {
        boardRepository.deleteById(board.getId());
        messagingTemplate.convertAndSend("/topic/updates", true);
    }

    public Board getBoardByKey(String key){
        return boardRepository.findBoardByKey(key).orElse(null);
    }


    public java.util.List<Board> getAllBoards() {
        List<Board> res =  boardRepository.findAll();
        for(Board board: res){
            Collections.sort(board.getLists(), Comparator.comparingInt(commons.List::getNumberInTheBoard));
            for(commons.List list : board.getLists()){
                Collections.sort(list.getCards(), Comparator.comparingInt(Card::getNumberInTheList));
                for(Card card : list.getCards()){
                    Collections.sort(card.getSubtasks() , Comparator.comparingInt(Subtask::getNumberInTheCard));
                }
            }
        }
        return res;
    }


    /**
     * checks if the password is correct
     * @param password the password to check
     * @return true if the password is correct
     */
    public boolean checkPassword(String password) {
        System.out.println(serverPassword.equals(password));
        if(serverPassword.equals(password))
            return true;
        return false;
    }
}


