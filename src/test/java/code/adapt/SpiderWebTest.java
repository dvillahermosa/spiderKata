package code.adapt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;


public class SpiderWebTest {

    private Game game;

    @BeforeEach
    private void beforeEach() {
        this.game = new Game();
    }

    @Test
    public void itShouldStartTheGame() throws NotCorrectPositionException {
        game.start(1, 1, 4, 4);

        Position spider = game.getSpider();
        Position prey = game.getPrey();

        assertEquals(1, spider.getX());
        assertEquals(1, spider.getY());
        assertEquals(4, prey.getX());
        assertEquals(4, prey.getY());
    }

    @Test
    public void it_should_throws_an_NotCorrectPositionsException_with_a_no_valid_start_position() {
        assertThrows(NotCorrectPositionException.class, () -> game.start(1, 1, 1, 3));
    }

    @ParameterizedTest
    @CsvSource({"6,1", "-1,2", "6,-1", "1,6", "1,-5", "1,2", "1,3", "1,4", "1,5"})
    public void it_should_throws_an_NotCorrectPositionsException_with_a_no_valid_move_position_for_spider(String strX, String strY) throws NotCorrectPositionException, NextPlayerTurnException {
        int x = Integer.parseInt(strX);
        int y = Integer.parseInt(strY);
        game.start(1, 1, 4, 4);
        game.movePrey(4, 5);

        assertThrows(NotCorrectPositionException.class, () -> game.moveSpider(x, y));
    }

    @ParameterizedTest
    @CsvSource({"6,1", "-1,2", "6,-1", "1,6", "1,-5", "1,2", "1,3", "1,4", "1,5"})
    public void it_should_throws_an_NotCorrectPositionsException_with_a_no_valid_move_position_for_Prey(String strX, String strY) throws NotCorrectPositionException {
        int x = Integer.parseInt(strX);
        int y = Integer.parseInt(strY);
        game.start(1, 1, 4, 4);

        assertThrows(NotCorrectPositionException.class, () -> game.movePrey(x, y));
    }

    @Test
    public void it_should_throws_an_NextPlayerTurnException_when_spider_move_twice_at_once() throws NotCorrectPositionException {
        game.start(1, 1, 4, 3);

        assertThrows(NextPlayerTurnException.class, () -> {
            game.moveSpider(4, 4);
            game.moveSpider(4, 5);
        });
    }

    @Test
    public void it_should_throws_an_NextPlayerTurnException_when_Prey_move_twice_at_once() throws NotCorrectPositionException {
        game.start(1, 1, 4, 4);

        assertThrows(NextPlayerTurnException.class, () -> {
            game.movePrey(4, 4);
            game.movePrey(4, 5);
        });
    }

    @ParameterizedTest
    @CsvSource({"2,5", "2,4", "3,3", "3,4", "3,5", "4,4", "4,5", "5,5"})
    public void it_should_throws_an_NotCorrectPositionsException_if_the_spider_move_more_than_one(String strX, String strY) throws NotCorrectPositionException, NextPlayerTurnException {
        int x = Integer.parseInt(strX);
        int y = Integer.parseInt(strY);
        game.start(2, 2, 4, 4);
        game.movePrey(3, 4);

        assertThrows(NotCorrectPositionException.class, () -> game.moveSpider(x, y));
    }

    @ParameterizedTest
    @CsvSource({"4,2", "5,5", "5,3", "5,2", "3,3", "3,2", "2,2"})
    public void it_should_throws_an_NotCorrectPositionsException_if_the_prey_move_more_than_one(String strX, String strY)
            throws NotCorrectPositionException {
        int x = Integer.parseInt(strX);
        int y = Integer.parseInt(strY);
        game.start(2, 2, 4, 4);

        assertThrows(NotCorrectPositionException.class, () -> game.movePrey(x, y));
    }

    @ParameterizedTest
    @CsvSource({"2,2", "2,3", "2,4", "2,5"})
    public void the_spider_should_move_to_all_rows_from_position_1(String strX, String strY) throws NotCorrectPositionException, NextPlayerTurnException {
        int x = Integer.parseInt(strX);
        int y = Integer.parseInt(strY);

        game.start(1, 1, 4, 4);
        game.movePrey(3, 4);

        game.moveSpider(x, y);

        Position spider = game.getSpider();
        Position prey = game.getPrey();
        assertEquals(x, spider.getX());
        assertEquals(y, spider.getY());
        assertEquals(3, prey.getX());
        assertEquals(4, prey.getY());
    }

    @Test
    public void spider_catch_the_other_spider_and_win() throws NextPlayerTurnException, NotCorrectPositionException {
        game.start(2, 2, 4, 4);
        game.movePrey(4, 5);
        game.moveSpider(2, 3);
        game.movePrey(4, 4);
        game.moveSpider(3, 3);
        game.movePrey(3, 4);
        game.moveSpider(3, 4);

        assertTrue(game.gameHasFinished());
        assertTrue(game.haveYouWon());
    }

    @Test
    public void spider_not_catch_the_other_spider_and_lose_after_10_turns() throws NextPlayerTurnException, NotCorrectPositionException {
        game.start(2, 2, 4, 4);
        game.movePrey(4, 5);
        game.moveSpider(2, 3);
        game.movePrey(4, 4);
        game.moveSpider(3, 3);
        game.movePrey(3, 4);
        game.moveSpider(3, 2);
        game.movePrey(3, 5);
        game.moveSpider(2, 2);
        game.movePrey(4, 5);
        game.moveSpider(1, 1);

        assertTrue(game.gameHasFinished());
        assertFalse(game.haveYouWon());
    }
}