package sample;

import java.util.InputMismatchException;

public class EditorExeption extends InputMismatchException {
    public EditorExeption(String text) { super(text); }
}
