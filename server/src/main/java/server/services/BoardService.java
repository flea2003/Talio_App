package server.services;

import commons.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;

@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public BoardService(BoardRepository boardRepository, SimpMessagingTemplate messagingTemplate) {
        this.boardRepository = boardRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public java.util.List<Board> getAllBoards() {
        return boardRepository.findAll();
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
}


