package de.naju.adebar.util.conversion.chapter;

import de.naju.adebar.controller.forms.chapter.BoardForm;
import de.naju.adebar.model.chapter.Board;
import de.naju.adebar.model.human.Activist;
import de.naju.adebar.model.human.HumanManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Service to extract the necessary data from a board form
 * @author Rico Bergmann
 */
@Service
public class BoardFormDataExtractor {
    private HumanManager humanManager;

    @Autowired
    public BoardFormDataExtractor(HumanManager humanManager) {
        Assert.notNull(humanManager, "Human manager may not be null");
        this.humanManager = humanManager;
    }

    /**
     * @param boardForm form containing the information about the board
     * @return the {@link Board} object encoded by the form
     */
    public Board extractBoard(BoardForm boardForm) {
        Activist chairman = humanManager.findActivist(humanManager.findPerson(boardForm.getChairmanId()).orElseThrow(IllegalArgumentException::new));
        Board board;
        if (boardForm.hasEmail()) {
            board = new Board(chairman, boardForm.getEmail());
        } else {
            board = new Board(chairman);
        }

        for (String memberId : boardForm.getMemberIds()) {
            Activist member = humanManager.findActivist(humanManager.findPerson(memberId).orElseThrow(IllegalArgumentException::new));
            board.addBoardMember(member);
        }

        if (!boardForm.getMemberIds().contains(boardForm.getChairmanId())) {
            board.addBoardMember(chairman);
        }

        return board;
    }

}
