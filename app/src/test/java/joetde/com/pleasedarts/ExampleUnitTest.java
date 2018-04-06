package joetde.com.pleasedarts;

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ExampleUnitTest {

    private void store() {
        String tests[] = {"3/16", "220", "to miss", "to mess", "to mrs."};
    }

    @Test
    public void parseOneMiss() {
        assertThat(NDLP.parseInput("one miss"), is(Arrays.asList(new NDLP.DartScore(NDLP.Quantifier.ONE, NDLP.Multiplier.SINGLE, NDLP.Region.MISS))));
    }

    @Test
    public void parseWordOneDigit() {
        assertThat(NDLP.parseWord("1"), is(new NDLP.DartScore(NDLP.Quantifier.ONE, NDLP.Multiplier.NONE, NDLP.Region.NONE)));
        assertThat(NDLP.parseWord("2"), is(new NDLP.DartScore(NDLP.Quantifier.TWO, NDLP.Multiplier.NONE, NDLP.Region.NONE)));
        assertThat(NDLP.parseWord("3"), is(new NDLP.DartScore(NDLP.Quantifier.THREE, NDLP.Multiplier.NONE, NDLP.Region.NONE)));
        assertThat(NDLP.parseWord("4"), is(new NDLP.DartScore(NDLP.Quantifier.ONE, NDLP.Multiplier.NONE, NDLP.Region.NONE)));
    }

    @Test
    public void parseWordTwoDigits() {
        assertThat(NDLP.parseWord("11"), is(new NDLP.DartScore(NDLP.Quantifier.ONE, NDLP.Multiplier.SINGLE, NDLP.Region.NONE)));
        assertThat(NDLP.parseWord("15"), is(new NDLP.DartScore(NDLP.Quantifier.ONE, NDLP.Multiplier.SINGLE, NDLP.Region.FIFTEEN)));
        assertThat(NDLP.parseWord("16"), is(new NDLP.DartScore(NDLP.Quantifier.ONE, NDLP.Multiplier.SINGLE, NDLP.Region.SIXTEEN)));
        assertThat(NDLP.parseWord("20"), is(new NDLP.DartScore(NDLP.Quantifier.ONE, NDLP.Multiplier.SINGLE, NDLP.Region.TWENTY)));
    }

    @Test
    public void parseWordThreeDigits() {
        assertThat(NDLP.parseWord("111"), is(new NDLP.DartScore(NDLP.Quantifier.ONE, NDLP.Multiplier.SINGLE, NDLP.Region.NONE)));
        assertThat(NDLP.parseWord("115"), is(new NDLP.DartScore(NDLP.Quantifier.ONE, NDLP.Multiplier.SINGLE, NDLP.Region.FIFTEEN)));
        assertThat(NDLP.parseWord("216"), is(new NDLP.DartScore(NDLP.Quantifier.TWO, NDLP.Multiplier.SINGLE, NDLP.Region.SIXTEEN)));
        assertThat(NDLP.parseWord("320"), is(new NDLP.DartScore(NDLP.Quantifier.THREE, NDLP.Multiplier.SINGLE, NDLP.Region.TWENTY)));
        assertThat(NDLP.parseWord("3/16"), is(new NDLP.DartScore(NDLP.Quantifier.THREE, NDLP.Multiplier.SINGLE, NDLP.Region.SIXTEEN)));
        assertThat(NDLP.parseWord("320s"), is(new NDLP.DartScore(NDLP.Quantifier.THREE, NDLP.Multiplier.SINGLE, NDLP.Region.TWENTY)));
        assertThat(NDLP.parseWord("220th"), is(new NDLP.DartScore(NDLP.Quantifier.TWO, NDLP.Multiplier.SINGLE, NDLP.Region.TWENTY)));
    }

    @Test
    public void parseWordOneWord() {
        assertThat(NDLP.parseWord("twenties"), is(new NDLP.DartScore(NDLP.Quantifier.ONE, NDLP.Multiplier.SINGLE, NDLP.Region.TWENTY)));
    }

    @Test
    public void parseInputOneWords() {
        assertThat(NDLP.parseInput("bullseye"), is(Arrays.asList(new NDLP.DartScore(NDLP.Quantifier.ONE, NDLP.Multiplier.SINGLE, NDLP.Region.BULLSEYE))));
    }

    @Test
    public void parseInputTwoWords() {
        assertThat(NDLP.parseInput("two fifteens"), is(Arrays.asList(new NDLP.DartScore(NDLP.Quantifier.TWO, NDLP.Multiplier.SINGLE, NDLP.Region.FIFTEEN))));
        assertThat(NDLP.parseInput("to fifteens"), is(Arrays.asList(new NDLP.DartScore(NDLP.Quantifier.TWO, NDLP.Multiplier.SINGLE, NDLP.Region.FIFTEEN))));
        assertThat(NDLP.parseInput("one bullseye"), is(Arrays.asList(new NDLP.DartScore(NDLP.Quantifier.ONE, NDLP.Multiplier.SINGLE, NDLP.Region.BULLSEYE))));
        assertThat(NDLP.parseInput("double bullseye"), is(Arrays.asList(new NDLP.DartScore(NDLP.Quantifier.ONE, NDLP.Multiplier.DOUBLE, NDLP.Region.BULLSEYE))));
        assertThat(NDLP.parseInput("three twenties"), is(Arrays.asList(new NDLP.DartScore(NDLP.Quantifier.THREE, NDLP.Multiplier.SINGLE, NDLP.Region.TWENTY))));
        assertThat(NDLP.parseInput("3 meses"), is(Arrays.asList(new NDLP.DartScore(NDLP.Quantifier.THREE, NDLP.Multiplier.SINGLE, NDLP.Region.MISS))));
        assertThat(NDLP.parseInput("one miss"), is(Arrays.asList(new NDLP.DartScore(NDLP.Quantifier.ONE, NDLP.Multiplier.SINGLE, NDLP.Region.MISS))));
    }

    @Test
    public void parseInputMultipleEntries() {
        assertThat(NDLP.parseInput("118:1 Bullseye"), is(Arrays.asList(new NDLP.DartScore(NDLP.Quantifier.ONE, NDLP.Multiplier.SINGLE, NDLP.Region.EIGHTEEN),
                new NDLP.DartScore(NDLP.Quantifier.ONE, NDLP.Multiplier.SINGLE, NDLP.Region.BULLSEYE))));
    }
}