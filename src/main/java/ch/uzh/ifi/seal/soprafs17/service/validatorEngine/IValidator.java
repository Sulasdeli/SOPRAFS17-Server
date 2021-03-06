package ch.uzh.ifi.seal.soprafs17.service.validatorEngine;

import ch.uzh.ifi.seal.soprafs17.model.entity.Game;
import ch.uzh.ifi.seal.soprafs17.model.entity.moves.AMove;

/**
 * Created by ilecipi on 03.04.17.
 */
public interface IValidator {
    boolean supports(AMove amove);
    void validate(Game game, AMove amove);
}
