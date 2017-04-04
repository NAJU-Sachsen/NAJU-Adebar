package de.naju.adebar.util.conversion.chapter;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.naju.adebar.controller.forms.chapter.BoardForm;
import de.naju.adebar.model.chapter.Board;
import de.naju.adebar.model.human.Activist;
import de.naju.adebar.model.human.PersonId;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * Service to convert a {@link Board} to a corresponding {@link BoardForm}
 * @author Rico Bergmann
 */
@Service
public class BoardToBoardFormConverter {

    /**
     * Performs the conversion
     * @param board the board to convert
     * @return the created form
     */
    public BoardForm convertToBoardForm(Board board) {
        if (board == null) {
            return new BoardForm();
        }

        PersonId chairman = board.getChairman().getAssociatedPerson();
        String email = board.getEmail();
        List<String> members = new LinkedList<>();

        board.getMembers().forEach(m -> members.add(m.getAssociatedPerson().toString()));


        return new BoardForm(chairman.toString(), email, members);
    }

}
