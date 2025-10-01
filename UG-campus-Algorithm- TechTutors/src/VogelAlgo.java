

n, boolean isRow) {
            int min1 = Integer.MAX_VALUE, min2 = Integer.MAX_VALUE;
            int minP = -1;
            for (int i = 0; i < len; i++) {
                if (isRow ? colDone[i] : rowDone[i])
                    continue;
                int c = isRow ? costs[j][i] : costs[i][j];
                if (c < min1) {
                    min2 = min1;
                    min1 = c;
                    minP = i;
                } else if (c < min2)
                    min2 = c;
            }
            return new int[]{min2 - min1, min1, minP};
        }

        static int[] maxPenalty(int len1, int len2, boolean isRow) {
            int md = Integer.MIN_VALUE;
            int pc = -1, pm = -1, mc = -1;
            for (int i = 0; i < len1; i++) {
                if (isRow ? rowDone[i] : colDone[i])
                    continue;
                int[] res = diff(i, len2, isRow);
                if (res[0] > md) {
                    md = res[0];  // max diff
                    pm = i;       // pos of max diff
                    mc = res[1];  // min cost
                    pc = res[2];  // pos of min cost
                }
            }
            return isRow ? new int[]{pm, pc, mc, md} : new int[]{pc, pm, mc, md};
        }
    }

