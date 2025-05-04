import java.util.Random;
import java.util.Scanner;

/**
 * Ponto de entrada para o jogo de Sudoku
 * 
 * Esta classe inicia o jogo e gerencia o loop principal de interação com o
 * usuário.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== JOGO DE SUDOKU ===");
        System.out.println("Bem-vindo ao jogo de Sudoku! Um puzzle de dificuldade média foi gerado para você.");
        System.out.println("Para jogar, insira a linha (1-9), coluna (1-9) e o valor (1-9) quando solicitado.");
        System.out.println("Divirta-se!\n");

        // Cria e inicia um novo jogo de Sudoku
        SudokuGame game = new SudokuGame();
        game.play();
    }
}

/**
 * Classe principal que controla o fluxo do jogo de Sudoku
 * 
 * Esta classe gerencia as interações do usuário, exibe o tabuleiro e
 * verifica o estado do jogo (verificando se foi concluído corretamente).
 */
class SudokuGame {
    private SudokuBoard board;
    private Scanner scanner;
    private boolean gameOver;

    /**
     * Construtor da classe SudokuGame
     * Inicializa o tabuleiro com um puzzle gerado automaticamente
     * e prepara os recursos necessários para o jogo
     */
    public SudokuGame() {
        // Cria um novo tabuleiro de Sudoku
        board = new SudokuBoard();

        // Gera um puzzle de dificuldade média
        SudokuGenerator generator = new SudokuGenerator();
        generator.generatePuzzle(board, 40); // 40 células vazias = dificuldade média

        // Inicializa o scanner para entrada do usuário
        scanner = new Scanner(System.in);

        // Define o estado inicial do jogo
        gameOver = false;
    }

    /**
     * Método principal que executa o loop do jogo
     * Continua até que o jogo seja concluído
     */
    public void play() {
        while (!gameOver) {
            // Exibe o estado atual do tabuleiro
            board.display();

            // Processa a jogada do usuário
            processMove();

            // Verifica se o jogo foi concluído
            if (board.isFull()) {
                if (new SudokuSolver().isValidSolution(board)) {
                    System.out.println("\nParabéns! Você resolveu o puzzle corretamente!");
                } else {
                    System.out.println("\nO tabuleiro está preenchido, mas a solução não está correta.");
                }
                gameOver = true;
            }
        }

        // Exibe o tabuleiro final
        board.display();

        // Fecha o scanner quando o jogo terminar
        scanner.close();
    }

    /**
     * Processa uma jogada do usuário, solicitando linha, coluna e valor
     * Verifica se a jogada é válida antes de aplicá-la ao tabuleiro
     */
    private void processMove() {
        int row, col, value;
        boolean validMove = false;

        while (!validMove) {
            try {
                // Solicita coordenadas e valor
                System.out.print("\nDigite a linha (1-9): ");
                row = scanner.nextInt() - 1; // Subtrai 1 para converter de 1-9 para 0-8 (índices de array)

                System.out.print("Digite a coluna (1-9): ");
                col = scanner.nextInt() - 1; // Subtrai 1 para converter de 1-9 para 0-8 (índices de array)

                System.out.print("Digite o valor (1-9): ");
                value = scanner.nextInt();

                // Valida a entrada
                if (row < 0 || row > 8 || col < 0 || col > 8 || value < 1 || value > 9) {
                    System.out.println("Entrada inválida! Por favor, insira valores entre 1 e 9.");
                    continue;
                }

                // Verifica se a célula é editável (não faz parte do puzzle original)
                if (!board.isCellEditable(row, col)) {
                    System.out.println("Esta célula faz parte do puzzle original e não pode ser alterada!");
                    continue;
                }

                // Verifica se o movimento é válido pelas regras do Sudoku
                if (!board.isValidMove(row, col, value)) {
                    System.out.println("Movimento inválido! Este valor viola as regras do Sudoku.");
                    continue;
                }

                // Aplica o movimento ao tabuleiro
                board.setCellValue(row, col, value);
                validMove = true;

            } catch (Exception e) {
                System.out.println("Entrada inválida! Por favor, insira números inteiros.");
                scanner.nextLine(); // Limpa o buffer do scanner
            }
        }
    }
}

/**
 * Classe que representa o tabuleiro do jogo de Sudoku
 * 
 * Contém o estado atual do jogo, as células editáveis e os métodos para
 * verificar movimentos válidos e exibir o tabuleiro.
 */
class SudokuBoard {
    private int[][] board; // Matriz 9x9 para armazenar os valores do tabuleiro
    private boolean[][] editable; // Matriz 9x9 para indicar quais células podem ser editadas

    /**
     * Construtor da classe SudokuBoard
     * Inicializa o tabuleiro vazio e define todas as células como editáveis
     */
    public SudokuBoard() {
        // Inicializa o tabuleiro 9x9 com zeros (células vazias)
        board = new int[9][9];

        // Inicializa a matriz de células editáveis (todas são editáveis por padrão)
        editable = new boolean[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                editable[i][j] = true;
            }
        }
    }

    /**
     * Obtém o valor de uma célula específica
     * 
     * @param row linha da célula (0-8)
     * @param col coluna da célula (0-8)
     * @return valor atual da célula
     */
    public int getCellValue(int row, int col) {
        return board[row][col];
    }

    /**
     * Define o valor de uma célula específica
     * 
     * @param row   linha da célula (0-8)
     * @param col   coluna da célula (0-8)
     * @param value valor a ser definido (1-9)
     */
    public void setCellValue(int row, int col, int value) {
        board[row][col] = value;
    }

    /**
     * Verifica se uma célula específica pode ser editada
     * 
     * @param row linha da célula (0-8)
     * @param col coluna da célula (0-8)
     * @return true se a célula for editável, false caso contrário
     */
    public boolean isCellEditable(int row, int col) {
        return editable[row][col];
    }

    /**
     * Define se uma célula específica pode ser editada
     * 
     * @param row      linha da célula (0-8)
     * @param col      coluna da célula (0-8)
     * @param editable true para tornar a célula editável, false caso contrário
     */
    public void setCellEditable(int row, int col, boolean editable) {
        this.editable[row][col] = editable;
    }

    /**
     * Verifica se o tabuleiro está completamente preenchido
     * 
     * @return true se todas as células estiverem preenchidas, false caso contrário
     */
    public boolean isFull() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Verifica se um movimento específico é válido de acordo com as regras do
     * Sudoku
     * 
     * @param row   linha da célula (0-8)
     * @param col   coluna da célula (0-8)
     * @param value valor a ser verificado (1-9)
     * @return true se o movimento for válido, false caso contrário
     */
    public boolean isValidMove(int row, int col, int value) {
        // Verifica a linha
        for (int j = 0; j < 9; j++) {
            if (board[row][j] == value) {
                return false;
            }
        }

        // Verifica a coluna
        for (int i = 0; i < 9; i++) {
            if (board[i][col] == value) {
                return false;
            }
        }

        // Verifica o quadrante 3x3
        int boxRow = row - row % 3;
        int boxCol = col - col % 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[boxRow + i][boxCol + j] == value) {
                    return false;
                }
            }
        }

        // Se passou por todas as verificações, o movimento é válido
        return true;
    }

    /**
     * Exibe o tabuleiro atual no console
     * Formata o tabuleiro para facilitar a visualização
     */
    public void display() {
        System.out.println("\n    1 2 3   4 5 6   7 8 9  ");
        System.out.println("  -------------------------");

        for (int i = 0; i < 9; i++) {
            System.out.print((i + 1) + " | ");

            for (int j = 0; j < 9; j++) {
                // Imprime o valor da célula ou espaço se for zero
                if (board[i][j] == 0) {
                    System.out.print("  ");
                } else {
                    System.out.print(board[i][j] + " ");
                }

                // Adiciona separadores verticais entre os blocos 3x3
                if (j % 3 == 2 && j < 8) {
                    System.out.print("| ");
                }
            }

            System.out.println("|");

            // Adiciona separadores horizontais entre os blocos 3x3
            if (i % 3 == 2 && i < 8) {
                System.out.println("  |-------+-------+-------|");
            }
        }

        System.out.println("  -------------------------");
    }

    /**
     * Cria uma cópia do tabuleiro atual
     * 
     * @return uma nova instância de SudokuBoard com os mesmos valores
     */
    public SudokuBoard copy() {
        SudokuBoard copy = new SudokuBoard();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                copy.board[i][j] = this.board[i][j];
                copy.editable[i][j] = this.editable[i][j];
            }
        }

        return copy;
    }
}

/**
 * Classe responsável por gerar puzzles de Sudoku
 * 
 * Utiliza algoritmos de backtracking para criar tabuleiros válidos
 * e então remove células para criar puzzles de diferentes dificuldades.
 */
class SudokuGenerator {
    private Random random;
    private SudokuSolver solver;

    /**
     * Construtor da classe SudokuGenerator
     * Inicializa o gerador aleatório e o solucionador
     */
    public SudokuGenerator() {
        random = new Random();
        solver = new SudokuSolver();
    }

    /**
     * Gera um puzzle de Sudoku com o número especificado de células vazias
     * 
     * @param board      o tabuleiro a ser preenchido
     * @param emptyCells número de células que devem ficar vazias
     */
    public void generatePuzzle(SudokuBoard board, int emptyCells) {
        // Preenche o tabuleiro com uma solução completa e válida
        fillBoard(board);

        // Remove células para criar o puzzle
        removeDigits(board, emptyCells);

        // Marca as células do puzzle original como não editáveis
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board.getCellValue(i, j) != 0) {
                    board.setCellEditable(i, j, false);
                }
            }
        }
    }

    /**
     * Preenche o tabuleiro com uma solução completa e válida
     * 
     * @param board o tabuleiro a ser preenchido
     */
    private void fillBoard(SudokuBoard board) {
        // Limpa o tabuleiro
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board.setCellValue(i, j, 0);
            }
        }

        // Preenche as células diagonais principais primeiro (para garantir um tabuleiro
        // válido)
        fillDiagonal(board);

        // Usa o solucionador para preencher o resto do tabuleiro
        solver.solve(board);
    }

    /**
     * Preenche os blocos 3x3 diagonais com valores válidos
     * 
     * @param board o tabuleiro a ser preenchido
     */
    private void fillDiagonal(SudokuBoard board) {
        // Preenche os três blocos 3x3 na diagonal principal
        for (int block = 0; block < 3; block++) {
            fillBox(board, block * 3, block * 3);
        }
    }

    /**
     * Preenche um bloco 3x3 com valores válidos
     * 
     * @param board    o tabuleiro a ser preenchido
     * @param startRow linha inicial do bloco
     * @param startCol coluna inicial do bloco
     */
    private void fillBox(SudokuBoard board, int startRow, int startCol) {
        // Cria uma lista de valores de 1 a 9
        int[] values = new int[9];
        for (int i = 0; i < 9; i++) {
            values[i] = i + 1;
        }

        // Embaralha os valores
        shuffleArray(values);

        // Preenche o bloco 3x3
        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board.setCellValue(startRow + i, startCol + j, values[index++]);
            }
        }
    }

    /**
     * Embaralha os elementos de um array
     * 
     * @param array o array a ser embaralhado
     */
    private void shuffleArray(int[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    /**
     * Remove dígitos do tabuleiro para criar o puzzle
     * 
     * @param board o tabuleiro completo
     * @param count número de células a serem esvaziadas
     */
    private void removeDigits(SudokuBoard board, int count) {
        // Cria uma lista de todas as posições (0-80)
        int[] positions = new int[81];
        for (int i = 0; i < 81; i++) {
            positions[i] = i;
        }

        // Embaralha as posições
        shuffleArray(positions);

        // Remove os dígitos nas posições selecionadas
        for (int i = 0; i < count; i++) {
            int pos = positions[i];
            int row = pos / 9;
            int col = pos % 9;

            // Guarda o valor original
            int originalValue = board.getCellValue(row, col);

            // Remove o valor (define como 0)
            board.setCellValue(row, col, 0);

            // Cria uma cópia do tabuleiro para verificar
            SudokuBoard boardCopy = board.copy();

            // Conta quantas soluções existem
            int solutions = solver.countSolutions(boardCopy);

            // Se houver mais de uma solução, restaura o valor
            if (solutions != 1) {
                board.setCellValue(row, col, originalValue);
                i--; // Tenta outra posição
            }
        }
    }
}

/**
 * Classe responsável por resolver e verificar soluções de Sudoku
 * 
 * Utiliza algoritmos de backtracking para resolver puzzles e
 * verificar se uma solução é válida.
 */
class SudokuSolver {
    private int solutionCount;

    /**
     * Resolve um tabuleiro de Sudoku usando backtracking
     * 
     * @param board o tabuleiro a ser resolvido
     * @return true se encontrou uma solução, false caso contrário
     */
    public boolean solve(SudokuBoard board) {
        // Encontra uma célula vazia
        int[] emptyCell = findEmptyCell(board);

        // Se não houver células vazias, o tabuleiro está resolvido
        if (emptyCell == null) {
            return true;
        }

        int row = emptyCell[0];
        int col = emptyCell[1];

        // Tenta cada valor possível (1-9)
        for (int num = 1; num <= 9; num++) {
            // Verifica se o valor é válido nesta posição
            if (board.isValidMove(row, col, num)) {
                // Define o valor
                board.setCellValue(row, col, num);

                // Tenta resolver o resto do tabuleiro
                if (solve(board)) {
                    return true;
                }

                // Se não foi possível resolver, desfaz a jogada (backtracking)
                board.setCellValue(row, col, 0);
            }
        }

        // Se nenhum valor funcionou, retorna false (sem solução)
        return false;
    }

    /**
     * Conta quantas soluções diferentes existem para um tabuleiro
     * Implementado para garantir que os puzzles gerados tenham solução única
     * 
     * @param board o tabuleiro a ser verificado
     * @return o número de soluções encontradas
     */
    public int countSolutions(SudokuBoard board) {
        solutionCount = 0;
        countSolutionsRecursive(board);
        return solutionCount;
    }

    /**
     * Método recursivo para contar soluções
     * 
     * @param board o tabuleiro a ser verificado
     */
    private void countSolutionsRecursive(SudokuBoard board) {
        // Se já encontramos mais de uma solução, podemos parar
        if (solutionCount > 1) {
            return;
        }

        // Encontra uma célula vazia
        int[] emptyCell = findEmptyCell(board);

        // Se não houver células vazias, encontramos uma solução
        if (emptyCell == null) {
            solutionCount++;
            return;
        }

        int row = emptyCell[0];
        int col = emptyCell[1];

        // Tenta cada valor possível (1-9)
        for (int num = 1; num <= 9; num++) {
            // Verifica se o valor é válido nesta posição
            if (board.isValidMove(row, col, num)) {
                // Define o valor
                board.setCellValue(row, col, num);

                // Tenta resolver o resto do tabuleiro
                countSolutionsRecursive(board);

                // Se já encontramos mais de uma solução, podemos parar
                if (solutionCount > 1) {
                    return;
                }

                // Desfaz a jogada (backtracking)
                board.setCellValue(row, col, 0);
            }
        }
    }

    /**
     * Encontra uma célula vazia no tabuleiro
     * 
     * @param board o tabuleiro a ser verificado
     * @return um array [row, col] com as coordenadas da célula vazia, ou null se
     *         não houver
     */
    private int[] findEmptyCell(SudokuBoard board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board.getCellValue(i, j) == 0) {
                    return new int[] { i, j };
                }
            }
        }
        return null;
    }

    /**
     * Verifica se uma solução de Sudoku é válida
     * 
     * @param board o tabuleiro a ser verificado
     * @return true se a solução for válida, false caso contrário
     */
    public boolean isValidSolution(SudokuBoard board) {
        // Verifica cada linha
        for (int row = 0; row < 9; row++) {
            boolean[] used = new boolean[10]; // índices 1-9 (ignoramos o 0)

            for (int col = 0; col < 9; col++) {
                int val = board.getCellValue(row, col);

                // Verifica se o valor é válido (1-9)
                if (val < 1 || val > 9) {
                    return false;
                }

                // Verifica se o valor já foi usado nesta linha
                if (used[val]) {
                    return false;
                }

                used[val] = true;
            }
        }

        // Verifica cada coluna
        for (int col = 0; col < 9; col++) {
            boolean[] used = new boolean[10]; // índices 1-9 (ignoramos o 0)

            for (int row = 0; row < 9; row++) {
                int val = board.getCellValue(row, col);

                // Verifica se o valor já foi usado nesta coluna
                if (used[val]) {
                    return false;
                }

                used[val] = true;
            }
        }

        // Verifica cada bloco 3x3
        for (int boxRow = 0; boxRow < 3; boxRow++) {
            for (int boxCol = 0; boxCol < 3; boxCol++) {
                boolean[] used = new boolean[10]; // índices 1-9 (ignoramos o 0)

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        int row = boxRow * 3 + i;
                        int col = boxCol * 3 + j;
                        int val = board.getCellValue(row, col);

                        // Verifica se o valor já foi usado neste bloco
                        if (used[val]) {
                            return false;
                        }

                        used[val] = true;
                    }
                }
            }
        }

        // Se passou por todas as verificações, a solução é válida
        return true;
    }
}