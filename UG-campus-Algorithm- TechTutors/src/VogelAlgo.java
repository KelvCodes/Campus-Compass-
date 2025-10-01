
        static int[] maxPenalty(int len1, int len2, boolean isRow) {
            int md = IDone[i] : colDone[i])
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

