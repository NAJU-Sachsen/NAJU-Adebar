package de.naju.adebar.services.conversion.chapter;

import de.naju.adebar.app.human.PersonManager;
import de.naju.adebar.controller.forms.chapter.BoardForm;
import de.naju.adebar.model.chapter.Board;
import de.naju.adebar.model.human.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Service to extract the necessary data from a board form
 *
 * @author Rico Bergmann
 */
@Service
public class BoardFormDataExtractor {

  private PersonManager personManager;

  @Autowired
  public BoardFormDataExtractor(PersonManager personManager) {
    Assert.notNull(personManager, "Human manager may not be null");
    this.personManager = personManager;
  }

  /**
   * @param boardForm form containing the information about the board
   * @return the {@link Board} object encoded by the form
   */
  public Board extractBoard(BoardForm boardForm) {
    Person chairman =
        personManager.findPerson(boardForm.getChairmanId()).orElseThrow(IllegalStateException::new);
    Board board;
    if (boardForm.hasEmail()) {
      board = new Board(chairman, boardForm.getEmail());
    } else {
      board = new Board(chairman);
    }

    for (String memberId : boardForm.getMemberIds()) {
      Person member = personManager.findPerson(memberId).orElseThrow(IllegalStateException::new);
      board.addBoardMember(member);
    }

    if (!boardForm.getMemberIds().contains(boardForm.getChairmanId())) {
      board.addBoardMember(chairman);
    }

    return board;
  }

}
