package server.services;

import commons.Board;
import commons.Card;
import commons.Subtask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public BoardService(BoardRepository boardRepository, SimpMessagingTemplate messagingTemplate) {
        this.boardRepository = boardRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public Board getBoardById(long id) {
        return boardRepository.findById(id).get();
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
}


