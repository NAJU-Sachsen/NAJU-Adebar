package de.naju.adebar.services.conversion.chapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import de.naju.adebar.model.chapter.Board;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonManager;
import de.naju.adebar.web.validation.chapters.BoardForm;

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
      if (memberId.equals(boardForm.getChairmanId())) {
        continue;
      }
      Person member = personManager.findPerson(memberId).orElseThrow(IllegalStateException::new);
      board.addBoardMember(member);
    }

    return board;
  }

}
