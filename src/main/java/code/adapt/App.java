package code.adapt;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

class App {
    private static void setSymbol(List<String> map, int x, int y, String symbol) {
        String line;
        if (x == 1) {
            y = 4;
        } else {
            y = (y - 1) * 2;
        }

        line = map.get(y);
        if (line.contains(x + "")) {
            line = line.replace(x + "", symbol);
        }

        map.set(y, line);
    }

    private static void printGame(Game game) {
        Position spider = game.getSpider();
        Position prey = game.getPrey();
        List<String> result = new ArrayList<>();

        result.add(" / - - 2 - 3 - 4 - 5 (I)");
        result.add("/      |   |   |   |");
        result.add("|  / - 2 - 3 - 4 - 5 (II)");
        result.add("| /    |   |   |   |");
        result.add("1 - -  2 - 3 - 4 - 5 (III)");
        result.add("| \\    |   |   |   |");
        result.add("|  \\ - 2 - 3 - 4 - 5 (IV)");
        result.add("\\      |   |   |   |");
        result.add(" \\ - - 2 - 3 - 4 - 5 (V)");
        result.add(" 1 - - 2 - 3 - 4 - 5 ");
        setSymbol(result, spider.getX(), spider.getY(), "X");
        setSymbol(result, prey.getX(), prey.getY(), "x");
        setSymbol(result, 1, 1, "O");
        for (int x = 2; x < 6; x++) {
            for (int y = 1; y < 6; y++) {
                setSymbol(result, x, y, "O");
            }
        }

        for (String line : result) {
            System.out.println(line);
        }
    }

    private static void firstPositionsRandom(Game game) {
        Random random = new Random();
        int x1;
        int y1;
        int x2;
        int y2;
        boolean end = false;
        while (!end) {
            x1 = random.nextInt(4) + 1;
            if (x1 == 1) {
                y1 = 1;
            } else {
                y1 = random.nextInt(4) + 1;
            }
            x2 = random.nextInt(4) + 1;
            if (x2 == 1) {
                y2 = 1;
            } else {
                y2 = random.nextInt(4) + 1;
            }
            try {
                game.start(x1, y1, x2, y2);
                end = true;
            } catch (NotCorrectPositionException e) {
                end = false;
            }
        }
    }

    public static void main(String[] args) {
        Game game = new Game();

        Scanner scanner = new Scanner(System.in);
        boolean end = false;
        int turns = 1;
        firstPositionsRandom(game);
        System.out.println("EL JUEGO DE LA ARAÑA");
        System.out.println("--------------------");
        printGame(game);
        while (!end && !game.gameHasFinished()) {
            if (turns % 2 == 0) {
                System.out.println("TURNO DE LA ARAÑA");
            } else {
                System.out.println("TURNO DE LA PRESA");
            }
            System.out.println("Araña => x =" + game.getSpider().getX() + " y = " + game.getSpider().getY());
            System.out.println("Presa => x =" + game.getPrey().getX() + " y = " + game.getPrey().getY());
            System.out.println("Turno = " + turns);
            System.out.println("pulse 'E' para terminar. Otra tecla para continuar");

            try {
                int x = readChar(scanner, "Posicion x");
                if (x == Integer.MIN_VALUE) {
                    end = true;
                    continue;
                }

                int y = readChar(scanner, "Posicion y");
                if (y == Integer.MIN_VALUE) {
                    end = true;
                    continue;
                }

                move(game, turns, x, y);
                turns++;
            } catch (NextPlayerTurnException ex) {
                System.out.println("LE TOCA AL OTRO JUGADOR");
            } catch (NotCorrectPositionException ex) {
                System.out.println("LA POSICION ES INCORRECTA");
            } catch (NumberFormatException ex) {
                System.out.println("Debes introducir un numero, vuelve a intentarlo");
            } finally {
                printGame(game);
            }
        }

        System.out.println("--------------------");
        System.out.println("EL JUEGO HA TERMINADO");
        if (game.haveYouWon()) {
            System.out.println("HAS GANADO");
        } else {
            System.out.println("HAS PERDIDO");
        }
        printGame(game);
    }

    private static void move(Game game, int turns, int x, int y) throws NotCorrectPositionException, NextPlayerTurnException {
        if (turns % 2 == 0) {
            game.moveSpider(x, y);
        } else {
            game.movePrey(x, y);
        }
    }

    private static int readChar(Scanner scanner, String text) {
        int result = Integer.MIN_VALUE;
        System.out.println(text);
        char c = scanner.next().charAt(0);
        if (c != 'e' && c != 'E') {
            result = Integer.parseInt(String.valueOf(c));
        }
        return result;
    }
}
