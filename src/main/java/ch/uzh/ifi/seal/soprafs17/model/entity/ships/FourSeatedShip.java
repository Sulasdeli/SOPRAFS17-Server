package ch.uzh.ifi.seal.soprafs17.model.entity.ships;

import ch.uzh.ifi.seal.soprafs17.model.entity.Stone;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by ilecipi on 10.03.17.
 */

@Entity
public class FourSeatedShip extends ShipFactory implements IShip, Serializable {

    final int MIN_STONES_REQUIRED=3;
    final int MAX_STONES_POSSIBLE=4;

    @Id
    @GeneratedValue
    private long id;

    @Override
    public void addStone(Stone stone, int i) {
        if(addedStones < MAX_STONES_POSSIBLE&& i >= 0 && i < MAX_STONES_POSSIBLE && stones[i] == null){
            stones[i] = stone;
            addedStones++;
        }
    }

    @Override
    public Stone removeStone(int i) {
        if(addedStones > 0 && stones[i] != null){
            Stone removedStone = stones[i];
            stones[i] = null;
            addedStones--;
            return removedStone;
        }else{
            return null;
        }
    }

    @Override
    public boolean isReady() {
        if(addedStones>=MIN_STONES_REQUIRED){
            return true;
        } else{
            return false;
        }
    }
    @Override
    public void setStones(Stone[] stones) {
        super.setStones(new Stone[this.MAX_STONES_POSSIBLE]);
    }
}
