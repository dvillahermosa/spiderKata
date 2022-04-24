package code.adapt;


public class Game {

    private static final int MIN_DISTANCE_AT_BEGINNING = 3;

    private enum Turn {
        SPIDER,
        PREY,
        END
    }

    private Position spider;
    private Position prey;
    private Turn turn;
    private boolean youWon;
    private int turns;

    public Game() {
        this.turn = Turn.PREY;
        youWon = false;
    }

    public void start(int spiderX, int spiderY, int preyX, int preyY) throws NotCorrectPositionException {
        int distance = calculateDistance(spiderX, spiderY, preyX, preyY);
        if (distance < MIN_DISTANCE_AT_BEGINNING) {
            throw new NotCorrectPositionException();
        }

        this.spider = new Position(spiderX, spiderY);
        this.prey = new Position(preyX, preyY);
        this.turns = 1;
    }

    private int calculateDistance(int x, int y, int nextX, int nextY) {
        int distance = Math.abs(x - nextX);
        if ((x != 1) && (nextX != 1)) {
            distance += Math.abs(y - nextY);
        }
        return distance;
    }

    public Position getSpider() {
        return this.spider;
    }

    public Position getPrey() {
        return this.prey;
    }

    private void updatePosition(int x, int y) {
        if (this.turn == Turn.END) {
            return;
        }

        if (this.turn == Turn.SPIDER) {
            this.spider = new Position(x, y);
            this.turn = Turn.PREY;
        } else if (this.turn == Turn.PREY) {
            this.prey = new Position(x, y);
            this.turn = Turn.SPIDER;
        }

        youWon = this.prey.equals(this.spider);
        this.turns++;
        if ((youWon) || (turns > 10)) {
            turn = Turn.END;
        }
    }

    public void moveSpider(int x, int y) throws NotCorrectPositionException, NextPlayerTurnException {
        move(x, y, Turn.SPIDER);
    }

    private boolean isNotCorrectNextPosition(int x, int y, int nextX, int nextY) {
        int distance = calculateDistance(x, y, nextX, nextY);
        return distance > 1;
    }

    private void checkMovePlayer(int x, int y) throws NotCorrectPositionException {
        if (isOutOfBoundaries(x, y) || isInNotCorrectFirstCell(x, y)) {
            throw new NotCorrectPositionException();
        }
    }

    private void move(int x, int y, Turn myTurn) throws NotCorrectPositionException, NextPlayerTurnException {
        checkTurn(myTurn);

        checkMovePlayer(x, y);

        checkNotCorrectPosition(x, y);

        updatePosition(x, y);
    }

    public void movePrey(int x, int y) throws NotCorrectPositionException, NextPlayerTurnException {
        move(x, y, Turn.PREY);
    }

    private void checkTurn(Turn prey) throws NextPlayerTurnException {
        if (this.turn != prey) {
            throw new NextPlayerTurnException();
        }
    }

    private void checkNotCorrectPosition(int x, int y) throws NotCorrectPositionException {
        Position pos = getTurnPosition();
        if (pos == null || isNotCorrectNextPosition(pos.getX(), pos.getY(), x, y)) {
            throw new NotCorrectPositionException();
        }
    }

    private Position getTurnPosition() {
        if (this.turn == Turn.PREY) {
            return this.prey;
        }
        if (this.turn == Turn.SPIDER) {
            return this.spider;
        }

        return null;
    }

    private boolean isInNotCorrectFirstCell(int x, int y) {
        return x == 1 && y != 1;
    }

    private boolean isOutOfBoundaries(int x, int y) {
        return (x > 5) || (x < 1) || (y > 5) || (y < 1);
    }

    public boolean haveYouWon() {
        return youWon;
    }

    public boolean gameHasFinished() {
        return turn == Turn.END;
    }
}
