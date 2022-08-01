    package com.java.problems;

    import java.io.BufferedReader;
    import java.io.IOException;
    import java.io.InputStreamReader;
    import java.util.HashMap;
    import java.util.Map;

    public class RoboTravel {

        static private char[] direction = {'N', 'E', 'S', 'W'};
        private static Map<Character, Pos> mp = new HashMap<>();
        static char curr_dir;

        static {
            mp.put('N', new Pos(1, 0));
            mp.put('E', new Pos(0, 1));
            mp.put('S', new Pos(-1, 0));
            mp.put('W', new Pos(0, -1));
        }

        public static void main(String[] args) throws IOException {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String inputMatrix = br.readLine();
            int M = Integer.parseInt(inputMatrix.split(" ")[0]);
            int N = Integer.parseInt(inputMatrix.split(" ")[1]);
            String input = br.readLine();
            String[] val = input.split(" ");
            int i = Integer.parseInt(val[0]);
            int j = Integer.parseInt(val[1]);
            curr_dir = val[2].charAt(0);

            String instruction = br.readLine();
            br.close();
            boolean[][] mat = new boolean[M+1][N+1];
            String stopPoint = findCoordinates(mat, i, j, instruction);
            System.out.println(stopPoint);
        }

        private static String findCoordinates(boolean[][] mat, int i, int j, String instruction) {

            mat[i][j] = true;
            for(char c : instruction.toCharArray()){
                if(c == 'M'){
                    Pos pos = mp.get(curr_dir);
                    int row = i+ pos.x;
                    int col = j + pos.y;
                    if(isValid(mat, row, col)){
                        i = row;
                        j = col;
                        mat[i][j] = true;
                    } else {
                        return j+ " "+i+" "+curr_dir;
                    }
                } else {
                    if(c == 'L'){
                       setNewDirection(-1);
                    } else {
                        setNewDirection(1);
                    }
                }
            }

            return j+ " "+i+" "+curr_dir;
        }

        private static void setNewDirection(int x) {
            int val = 0;
            for(int i=0; i<4; i++){
                if(direction[i] == curr_dir){
                    val = i;
                    break;
                }
            }
            if(val == 0 && x == -1){
                curr_dir = direction[3];
            } else if(val == 3 && x == 1){
                curr_dir = direction[0];
            } else {
                curr_dir = direction[val+x];
            }
        }

        private static boolean isValid(boolean[][] mat, int i, int j){
            if(i >= 0 && i < mat.length && j >= 0 && j < mat[0].length && !mat[i][j]){
                return true;
            }
            return false;
        }

        static class Pos {
            int x;
            int y;
            public Pos(int x, int y){
                this.x = x;
                this.y = y;
            }
        }
    }
