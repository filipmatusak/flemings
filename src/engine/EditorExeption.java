/** Trieda na vypis vynimiek, ktore nastali pri editovani mapy */

package engine;

import java.util.InputMismatchException;

public class EditorExeption extends InputMismatchException {
    public EditorExeption(String text) { super(text); }
}
