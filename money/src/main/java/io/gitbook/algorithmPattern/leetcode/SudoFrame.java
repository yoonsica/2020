package io.gitbook.algorithmPattern.leetcode;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

/**
 * @Classname SudoFrame
 * @Description TODO
 * @Date 2022/4/19 上午8:44
 * @Author shengli
 */
public class SudoFrame extends Frame {
    char[][] sudo;
    char[][] cmp;//用来与原属组比较，新的值会红色
    Sudo sd;
    public SudoFrame(char[][] sudo) {
        sd = new Sudo();
        cmp = new char[9][9];
        setSize(500,500);
        setTitle("解数独");
        setLocation(300,200);
        if(sd.isValidSudoku(sudo)){
            this.sudo = sudo;
        }else {
            this.sudo = new char[9][9];
        }
        //tmp = Arrays.copyOf(sudo,sudo.length); 注意这里是二维数组，这样拷贝之后原数组值变化后新数组会跟着变化
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cmp[i][j] = sudo[i][j];
            }
        }
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
        JPanel cp = new JPanel();
        Border border = BorderFactory.createLineBorder(Color.BLACK,1);
        cp.setLayout(new GridLayout(3,3,0,0));
        JPanel[][] jps = new JPanel[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JPanel p = new JPanel();
                p.setLayout(new GridLayout(3,3,0,0));
                p.setBorder(border);
                cp.add(p);
                jps[i][j] = p;
            }
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField tf = new JTextField();
                if(sudo[i][j] != '.'){
                    tf.setText(sudo[i][j]+"");
                }
                tf.setHorizontalAlignment(JTextField.CENTER);
                tf.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
                jps[i/3][j/3].add(tf);
                tf.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusLost(FocusEvent e) {
                        String t = tf.getText();
                        if(t.length() == 0){
                            return;
                        }
                        if(t.length() > 1 || !Character.isDigit(t.charAt(0))|| t.charAt(0)=='0'){
                            JOptionPane.showMessageDialog(null, "请填入一位1-9的数字", "错误", JOptionPane. ERROR_MESSAGE);
                            tf.setText("");
                        }
                        getSudo(jps);
                        if (!sd.isValidSudoku(sudo)) {
                            JOptionPane.showMessageDialog(null, "不是一个规范的数独", "错误", JOptionPane. ERROR_MESSAGE);
                            tf.setText("");
                        }
                    }
                });
            }
        }
        add(cp,BorderLayout.CENTER);
        JPanel cp1 = new JPanel();
        JButton submitJB = new JButton("计算");
        cp1.add(submitJB,BorderLayout.CENTER);
        add(cp1,BorderLayout.SOUTH);
        submitJB.addActionListener(e -> {
            getSudo(jps);
            if (!sd.isValidSudoku(sudo)) {
                JOptionPane.showMessageDialog(null, "不是一个规范的数独", "错误", JOptionPane.ERROR_MESSAGE);
            } else {
                getResultFrame(sudo).setVisible(true);
            }

        });
        setVisible(true);
    }

    /**
     * 获取结果面板
     * @param sudo
     * @return
     */
    public Frame getResultFrame(char[][] sudo) {
        Frame rFrame = new Frame("答案");
        rFrame.setSize(500,500);
        rFrame.setLocation(300,200);
        JPanel cp = new JPanel();
        Border border = BorderFactory.createLineBorder(Color.BLACK,1);
        cp.setLayout(new GridLayout(3,3,0,0));
        JPanel[][] jps = new JPanel[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JPanel p = new JPanel();
                p.setLayout(new GridLayout(3,3,0,0));
                p.setBorder(border);
                cp.add(p);
                jps[i][j] = p;
            }
        }
        sd.solveSudoku(sudo);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField tf = new JTextField();
                tf.setText(sudo[i][j]+"");
                if(cmp[i][j] == '.') {
                    tf.setForeground(Color.RED);
                }
                tf.setEditable(false);
                tf.setHorizontalAlignment(JTextField.CENTER);
                tf.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
                jps[i/3][j/3].add(tf);
            }
        }
        rFrame.add(cp,BorderLayout.CENTER);
        rFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                rFrame.dispose();
                //System.exit(0);
            }
        });
        return rFrame;
    }
    /**
     * 获取当前用户写入的全部数字
     * @param jps 3个3*3的
     */
    private void getSudo(JPanel[][] jps) {
        for (int m = 0; m < 3; m++) {
            for (int n = 0; n < 3; n++) {
                Component[] tfs = jps[m][n].getComponents();
                int i = 3 * m,j = 3 * n;
                for (Component component : tfs) {
                    if(j == 3 * (n + 1)){
                        j = 3 * n;
                        i++;
                        if(i == 3 * (m + 1)){
                            break;
                        }
                    }
                    JTextField tf = (JTextField) component;
                    if (!tf.getText().equals("")) {
                        sudo[i][j] = tf.getText().trim().charAt(0);
                    } else {
                        sudo[i][j] = '.';
                    }
                    j++;
                }
            }
        }

    }

    public static void main(String[] args) {
        new SudoFrame(new char[][]{
                {'5','3','.','.','7','.','.','.','.'},{'6','.','.','1','9','5','.','.','.'},{'.','9','8','.','.','.','.','6','.'},{'8','.','.','.','6','.','.','.','3'},{'4','.','.','8','.','3','.','.','1'},{'7','.','.','.','2','.','.','.','6'},{'.','6','.','.','.','.','2','8','.'},{'.','.','.','4','1','9','.','.','5'},{'.','.','.','.','8','.','.','7','9'}
        });
    }


}
