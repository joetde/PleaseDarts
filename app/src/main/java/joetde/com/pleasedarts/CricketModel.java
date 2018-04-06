package joetde.com.pleasedarts;

import java.util.HashMap;
import java.util.Map;

public class CricketModel {
    private Map<NDLP.Region, Integer> mScore[];

    public CricketModel() {
        mScore = new HashMap[2];
        mScore[0] = new HashMap<NDLP.Region, Integer>() {{
            put(NDLP.Region.FIFTEEN, 0);
            put(NDLP.Region.SIXTEEN, 0);
            put(NDLP.Region.SEVENTEEN, 0);
            put(NDLP.Region.EIGHTEEN, 0);
            put(NDLP.Region.NINETEEN, 0);
            put(NDLP.Region.TWENTY, 0);
            put(NDLP.Region.BULLSEYE, 0);
        }};
        mScore[1] = new HashMap<NDLP.Region, Integer>() {{
            put(NDLP.Region.FIFTEEN, 0);
            put(NDLP.Region.SIXTEEN, 0);
            put(NDLP.Region.SEVENTEEN, 0);
            put(NDLP.Region.EIGHTEEN, 0);
            put(NDLP.Region.NINETEEN, 0);
            put(NDLP.Region.TWENTY, 0);
            put(NDLP.Region.BULLSEYE, 0);
        }};
    }

    public void addScore(int player, NDLP.DartScore score) {
        if (score.region == NDLP.Region.MISS) { return; }

        int other_player = 1 - player;
        int value = score.getValue() + mScore[player].get(score.region);
        if (mScore[other_player].get(score.region) >= 3) {
            value = Math.min(3, value);
        }
        mScore[player].put(score.region, value);
    }

    public String getText(int player, NDLP.Region region) {
        int v = mScore[player].get(region);
        switch (v) {
            case 0: return "_";
            case 1: return "/";
            case 2: return "X";
            case 3: return "0";
            default: return String.valueOf(v - 3);
        }
    }

    public int getAdvance(int player) {
        int sum_player = 0;
        int sum_other = 0;

        for (NDLP.Region r : mScore[player].keySet()) {
            sum_player += r.getValue() * Math.max(0, mScore[player].get(r) - 3);
            sum_other += r.getValue() * Math.max(0, mScore[1 - player].get(r) - 3);
        }

        return Math.max(0, sum_player - sum_other);
    }
}
