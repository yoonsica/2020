package io.gitbook.algorithmPattern.leetcode;

/**
 * @Classname Sudo
 * @Description TODO
 * @Date 2022/4/19 上午8:42
 * @Author shengli
 */
class Sudo {
    public void solveSudoku(char[][] board) {
        int[][][] m = new int[3][3][9];//3*3
        int[][] v = new int[9][9];//行 可用为0，不可用为1
        int[][] h = new int[9][9];//列
        //初始化
        for(int i = 0;i < 9;i++){
            for(int j = 0;j < 9;j++) {
                if(board[i][j] != '.') {
                    v[i][board[i][j] - '1'] = 1;
                    h[j][board[i][j] - '1'] = 1;
                    m[i / 3][j / 3][board[i][j] - '1'] = 1;
                }
            }
        }
        dfs(board,m,v,h,0,0);
    }

    private boolean dfs(char[][] board,int[][][] m,int[][] v,int[][] h,int row,int col) {
        if(col == 9){
            row++;
            col = 0;
            if(row == 9){
                return true;
            }
        }
        if(board[row][col] == '.'){
            for(int i = 0;i < 9;i++){
                if(m[row/3][col/3][i]==0 && v[row][i]==0
                        && h[col][i]==0){//如果可用
                    board[row][col] = (char)('1' + i);
                    m[row/3][col/3][i] = 1;//置为不可用
                    v[row][i] = 1;
                    h[col][i] = 1;
                    if(dfs(board,m,v,h,row,col+1)){
                        return true;
                    }else{
                        board[row][col] = '.';//恢复
                        m[row/3][col/3][i] = 0;
                        v[row][i] = 0;
                        h[col][i] = 0;
                    }
                }
            }
            return false;
        }else {
            return dfs(board,m,v,h,row,col + 1);
        }
    }

    /**
     * 验证数独是否有效
     * @param board
     * @return
     */
    public boolean isValidSudoku(char[][] board) {
        int[][][] m = new int[3][3][9];//3*3 值为1，代表对应数字已经被使用
        int[][] v = new int[9][9];//行
        int[][] h = new int[9][9];//列
        //行
        for(int i = 0;i < 9;i++){
            for(int j = 0;j < 9;j++) {
                if(board[i][j] != '.') {
                    if(v[i][board[i][j] - '1'] == 1 || h[j][board[i][j] - '1'] == 1
                            || m[i / 3][j / 3][board[i][j] - '1'] == 1) {
                        return false;
                    }
                    v[i][board[i][j] - '1'] = 1;
                    h[j][board[i][j] - '1'] = 1;
                    m[i / 3][j / 3][board[i][j] - '1'] = 1;
                }
            }
        }
        return true;
    }
}