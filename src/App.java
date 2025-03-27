import com.sun.management.GcInfo;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {
    private int[][] board; // ゲーム盤を表す2次元配列
    private int currentPlayer = 1; // 現在のプレイヤー（1: プレイヤー1, 2: プレイヤー2)

    public static void main(String[] args) throws Exception {
        Application.launch(args);
    }

    @Override
    public void start(Stage gameStage) throws Exception {
        // ゲーム盤の初期化
        board = new int[8][8]; // 8x8のオセロ盤
        initializeBoard();

        // Canvasの作成
        Canvas canvas = new Canvas(400, 400);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // 碁盤を描画
        drawBoard(gc);

        canvas.setOnMouseClicked((MouseEvent event) -> {
            int size = 50; // マス目のサイズ
            int x = (int) (event.getX() / size); // クリックされた位置のx座標を計算
            int y = (int) (event.getY() / size); // クリックされた位置のy座標を計算

            // 石を配置
            if (isValidMove(x, y, currentPlayer)) {
                placePiece(x, y, currentPlayer);
                currentPlayer = (currentPlayer == 1) ? 2 : 1; // プレイヤーを切り替え
                drawBoard(gc); // 盤面を再描画
            }
        });

        // Canvasをシーンに追加
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, 400, 400);
        gameStage.setScene(scene);
        gameStage.setTitle("Reversi with Canvas");
        gameStage.show();
    }

    // ゲーム盤を初期化するメソッド
    private void initializeBoard() {
        // すべてのマスを空にする
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = 0; // 0は空のマスを表す
            }
        }

        // 初期配置（中央に4つの石を配置）
        board[3][3] = 1; 
        board[3][4] = 2; 
        board[4][3] = 2; 
        board[4][4] = 1; 
    }

    // 碁盤を描画するメソッド
    private void drawBoard(GraphicsContext gc) {
        int size = 50; // マス目のサイズ
        gc.clearRect(0, 0, 400, 400); // 画面をクリア
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                gc.strokeRect(i * size, j * size, size, size); // マス目を描画
                if (board[i][j] == 1) {
                    gc.fillOval(i * size + 5, j * size + 5, size - 10, size - 10); // プレイヤー1の石
                } else if (board[i][j] == 2) {
                    gc.strokeOval(i * size + 5, j * size + 5, size - 10, size - 10); // プレイヤー2の石
                }
            }
        }
    }

    // 石を配置するメソッド
    private void placePiece(int x, int y, int player) {
        // 指定された位置に石を配置するロジックを実装
        if (isValidMove(x, y, player)) { 
            board[x][y] = player; 
            flipPieces(x, y, player); 
        }
    }

    // 石をひっくり返すメソッド
    private void flipPieces(int x, int y, int player) {
        // 石をひっくり返すロジックを実装
        int[][] tempBoard = board.clone();

        for (int di = -1; di <= 1; di++) {
            for (int dj = -1; dj <= 1; dj++) {
                System.out.println("di: " + di + ", dj: " + dj);
                if (di == 0 && dj == 0) {
                    continue;
                }
                int i = di;
                int j = dj;
                boolean isThisDirectionFlip = false;
                while (true) {
                    // 盤面の外に出た場合
                    if (x + i < 0 || x + i >= 8 || y + j < 0 || y + j >= 8) {
                        break;
                    }
                    if (board[x + i][y + j] == 0) {
                        break;
                    } else if (board[x + i][y + j] != player) {
                        i += di;
                        j += dj;
                        isThisDirectionFlip = true;
                    } else {
                        if (isThisDirectionFlip) {
                            while (true) {
                                i -= di;
                                j -= dj;
                                if (i == 0 && j == 0) {
                                    break;
                                }
                                tempBoard[x + i][y + j] = player;
                            }
                            break;
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        System.out.println("tempBoard: " + tempBoard);
        board = tempBoard;
    }

    // 有効な手を判定するメソッド
    private boolean isValidMove(int x, int y, int player) {
        // 指定された位置が有効な手かどうかを判定するロジックを実装
        for (int di = -1; di <= 1; di++) {
            for (int dj = -1; dj <= 1; dj++) {
                if (di == 0 && dj == 0) {
                    continue;
                }
                int i = di;
                int j = dj;
                boolean isThisDirectionFlip = false;
                while (true) {
                    // 盤面の外に出た場合
                    if (x + i < 0 || x + i >= 8 || y + j < 0 || y + j >= 8) {
                        break;
                    }
                    if (board[x + i][y + j] == 0) {
                        break;
                    } else if (board[x + i][y + j] != player) {
                        i += di;
                        j += dj;
                        isThisDirectionFlip = true;
                    } else {
                        if (isThisDirectionFlip) {
                            return true;
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        return false;
    }

    // ゲーム終了を判定するメソッド
    private boolean isGameOver() {
        // ゲームが終了したかどうかを判定するロジックを実装
        // すべてのマスが埋まっているかどうか
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
            }
        }
        // どちらかのプレイヤーが石を置ける場所があるかどうか
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isValidMove(i, j, 1) || isValidMove(i, j, 2)) {
                    return false;
                }
            }
        }
        return true;
    }

    // 勝者を判定するメソッド
    private int determineWinner() {
        // 勝者を判定するロジックを実装
        int player1Count = 0;
        int player2Count = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 1) {
                    player1Count++;
                } else if (board[i][j] == 2) {
                    player2Count++;
                }
            }
        }

        if (player1Count > player2Count) {
            return 1;
        } else if (player1Count < player2Count) {
            return 2;
        }
        return 0;
        // 0: 引き分け, 1: プレイヤー1, 2: プレイヤー2
    }
}