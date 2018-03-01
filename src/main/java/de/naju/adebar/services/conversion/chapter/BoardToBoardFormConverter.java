package de.naju.adebar.services.conversion.chapter;

import de.naju.adebar.model.chapter.Board;
import de.naju.adebar.model.persons.PersonId;
import de.naju.adebar.web.validation.chapters.BoardForm;
import org.springframework.stereotype.Service;
import java.util.LinkedList;
import java.util.List;

/**
 * Service to convert a {@link Board} to a corresponding {@link BoardForm}
 * 
 * @author Rico Bergmann
 */
@Service
public class BoardToBoardFormConverter {

  /**
   * Performs the conversion
   * 
   * @param board the board to convert
   * @return the created form
   */
  public BoardForm convertToBoardForm(Board board) {
    if (board == null) {
      return new BoardForm();
    }

    PersonId chairman = board.getChairman().getId();
    String email = board.getEmail();
    List<String> members = new LinkedList<>();

    board.getMembers().forEach(m -> members.add(m.getId().toString()));

    return new BoardForm(chairman.toString(), email, members);
  }

}
