import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;


class Tests {

  //  @Test
   /// public void testSum() {
       // assertEquals(sum, testSum);

    private Platform platform = new Platform(100,100);
    private int x = platform.getX();
    private int X = platform.getX();
    @Test
    void testEquals() {
        assertEquals(x, X);
    }
    @ParameterizedTest
    @ValueSource ( ints= {100,200,300,400})
    void testingTheCollisions(int x) {
        assertTrue(platform.platformCollide(x,100,50,50));
    }
    @ParameterizedTest
    @ValueSource ( ints= {100,200,300,400})
    void testingTheCollisionsFail(int x_) {
        assertFalse(platform.platformCollide(x_,650,5,5));
    }
}
