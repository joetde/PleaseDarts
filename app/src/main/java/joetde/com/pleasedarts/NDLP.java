package joetde.com.pleasedarts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Natural Dart Language Processing
 */
public class NDLP {

    public enum Region {
        NONE(0),
        FIFTEEN(15),
        SIXTEEN(16),
        SEVENTEEN(17),
        EIGHTEEN(18),
        NINETEEN(19),
        TWENTY(20),
        BULLSEYE(25),
        MISS(0);

        private final int id;
        Region(int id) { this.id = id; }
        public int getValue() { return id; }
    }

    public enum Multiplier {
        NONE,
        SINGLE,
        DOUBLE,
        TRIPLE
    }

    public enum Quantifier {
        ONE,
        TWO,
        THREE
    }

    public static final Map<String, Region> regionMap = new HashMap<String, Region>() {{
        put("15", Region.FIFTEEN);
        put("50", Region.FIFTEEN);
        put("fifteen", Region.FIFTEEN);
        put("fifty", Region.FIFTEEN);
        put("16", Region.SIXTEEN);
        put("60", Region.SIXTEEN);
        put("sixteen", Region.SIXTEEN);
        put("sixty", Region.SIXTEEN);
        put("17", Region.SEVENTEEN);
        put("70", Region.SEVENTEEN);
        put("seventeen", Region.SEVENTEEN);
        put("seventy", Region.SEVENTEEN);
        put("18", Region.EIGHTEEN);
        put("80", Region.EIGHTEEN);
        put("eighteen", Region.EIGHTEEN);
        put("eighty", Region.EIGHTEEN);
        put("19", Region.NINETEEN);
        put("90", Region.NINETEEN);
        put("nineteen", Region.NINETEEN);
        put("ninety", Region.NINETEEN);
        put("20", Region.TWENTY);
        put("twenty", Region.TWENTY);
        put("twentie", Region.TWENTY); // Note: trick to get twenties...
        put("tiny", Region.TWENTY);
        put("twin", Region.TWENTY);
        put("bullseye", Region.BULLSEYE);
        put("bull's-eye", Region.BULLSEYE);
        put("lose", Region.BULLSEYE);
        put("bull", Region.BULLSEYE);
        put("bolt", Region.BULLSEYE);
        put("ball", Region.BULLSEYE);
        put("bow", Region.BULLSEYE);
        put("browser", Region.BULLSEYE);
        put("mis", Region.MISS);
        put("miss", Region.MISS);
        put("misse", Region.MISS);
        put("misses", Region.MISS);
        put("mese", Region.MISS);
        put("mace", Region.MISS);
        put("mrs.", Region.MISS);
    }};

    public static final Map<String, Quantifier> quantifierMap = new HashMap<String, Quantifier>() {{
        put("one", Quantifier.ONE);
        put("ones", Quantifier.ONE);
        put("1", Quantifier.ONE);
        put("to", Quantifier.TWO);
        put("do", Quantifier.TWO);
        put("two", Quantifier.TWO);
        put("twos", Quantifier.TWO);
        put("those", Quantifier.TWO);
        put("2", Quantifier.TWO);
        put("three", Quantifier.THREE);
        put("threes", Quantifier.THREE);
        put("free", Quantifier.THREE);
        put("tree", Quantifier.THREE);
        put("trees", Quantifier.THREE);
        put("street", Quantifier.THREE);
        put("3", Quantifier.THREE);
    }};

    public static final Map<String, Multiplier> multiplierMap = new HashMap<String, Multiplier>() {{
        put("single", Multiplier.SINGLE);
        put("double", Multiplier.DOUBLE);
        put("doubles", Multiplier.DOUBLE);
        put("triple", Multiplier.TRIPLE);
        put("triples", Multiplier.TRIPLE);
    }};

    public static Region getRegionFromWord(String word) {
        if (regionMap.containsKey(word)) {
            return regionMap.get(word);
        } else {
            return Region.NONE;
        }
    }

    public boolean isRegion(String word) {
        // TODO add case for 3 digits...
        return regionMap.containsKey(word);
    }

    public boolean isPotentialRegion(String word) {
        return regionMap.containsKey(word) ||
                (word.length() == 3 && regionMap.containsKey(word.substring(1)));
    }

    public static class DartScore {
        public Quantifier quantifier;
        public Multiplier multiplier;
        public Region region;

        public DartScore(Quantifier q, Multiplier m, Region r) {
            quantifier = q;
            multiplier = m;
            region = r;
        }

        @Override
        public boolean equals(Object obj) {
            DartScore other = (DartScore) obj;
            return quantifier == other.quantifier && multiplier == other.multiplier && region == other.region;
        }

        @Override
        public String toString() {
            return "" + quantifier.name() + "-" + multiplier.name() + "-" + region.name();
        }

        private static int getValue(Quantifier q) {
            switch (q) {
                case ONE: return 1;
                case TWO: return 2;
                case THREE: return 3;
            }
            return -1;
        }

        private static int getValue(Multiplier m) {
            switch (m) {
                case NONE: return 0;
                case SINGLE: return 1;
                case DOUBLE: return 2;
                case TRIPLE: return 3;
            }
            return -1;
        }

        public int getValue() {
            return getValue(multiplier) * getValue(quantifier);
        }

        public int getQuantifierInt() {
            switch (quantifier) {
                case ONE: return 1;
                case TWO: return 2;
                case THREE: return 3;
                default: return -1;
            }
        }

        public static Region getRegionFromInt(int i) {
            switch (i) {
                case 15: return Region.FIFTEEN;
                case 16: return Region.SIXTEEN;
                case 17: return Region.SEVENTEEN;
                case 18: return Region.EIGHTEEN;
                case 19: return Region.NINETEEN;
                case 20: return Region.TWENTY;
                case 50: return Region.FIFTEEN;
                case 60: return Region.SIXTEEN;
                case 70: return Region.SEVENTEEN;
                case 80: return Region.EIGHTEEN;
                case 90: return Region.NINETEEN;
                default: return Region.NONE;
            }
        }

        public static Quantifier getQuantifierFromInt(int i) {
            switch (i) {
                case 1: return Quantifier.ONE;
                case 2: return Quantifier.TWO;
                case 3: return Quantifier.THREE;
                default: return Quantifier.ONE;
            }
        }

    }

    public static List<DartScore> parseInput(String input) {
        String words[] = input.split("[ :]");

        List l = new ArrayList<DartScore>();
        DartScore last = new DartScore(Quantifier.ONE, Multiplier.NONE, Region.NONE);
        for (String w : words) {
            DartScore current = parseWord(w);

            if (last.region == Region.NONE) {
                if (last.quantifier != Quantifier.ONE) { current.quantifier = last.quantifier; }
                if (last.multiplier != Multiplier.NONE) { current.multiplier = last.multiplier; }
            }

            last = current;

            if (current.region != Region.NONE) {
                l.add(current);
            }
        }
        return l;
    }

    public static DartScore parseWord(String word) {
        // Example: two sixteens -> two sixteenth
        word = word.replaceAll("/", "");
        // Example: three twenties -> 320s
        word = word.replaceAll("s$", "");
        // Example: two twenties -> 220th
        word = word.replaceAll("th$", "");
        // Example: Miss
        word = word.toLowerCase();

        DartScore d = new DartScore(Quantifier.ONE, Multiplier.NONE, Region.NONE);

        if (word.matches("\\d+")) {
            // Only numbers, simple case
            int i = Integer.parseInt(word);
            if (word.length() == 1) {
                d.quantifier = DartScore.getQuantifierFromInt(i);
                return d;
            } else {
                d.quantifier = DartScore.getQuantifierFromInt(i / 100);
                d.region = DartScore.getRegionFromInt(i % 100);
                d.multiplier = Multiplier.SINGLE;
                return d;
            }
        } else {
            if (regionMap.containsKey(word)) {
                d.multiplier = Multiplier.SINGLE;
                d.region = regionMap.get(word);
            } else if (quantifierMap.containsKey(word)) {
                d.quantifier = quantifierMap.get(word);
            } else if (multiplierMap.containsKey(word)) {
                d.multiplier = multiplierMap.get(word);
            }

            return d;
        }
    }
}
