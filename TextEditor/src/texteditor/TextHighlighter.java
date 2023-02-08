/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texteditor;

import java.awt.Color;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Alice
 */
public class TextHighlighter extends DefaultHighlighter.DefaultHighlightPainter{
     public TextHighlighter(Color color) {
        super(color);
    }

    public void highLight(JTextComponent textComp, String[] pattern) {
        removeHighlights(textComp);

        try {
            Highlighter highlighter = textComp.getHighlighter();
            Document doc = textComp.getDocument();
            String text = doc.getText(0, doc.getLength());
            for (int i = 0; i < pattern.length; i++) {
                int pos = 0;

                while ((pos = text.indexOf(pattern[i], pos)) >= 0) {
                    highlighter.addHighlight(pos, pos + pattern[i].length(), this);
                    pos += pattern[i].length();
                }
            }
        } catch (BadLocationException e) {}

    }

    public void removeHighlights(JTextComponent textComp) {

        Highlighter highlighter = textComp.getHighlighter();
        Highlighter.Highlight[] hilites = highlighter.getHighlights();

        for (int i = 0; i < hilites.length; i++) {
            if (hilites[i].getPainter() instanceof TextHighlighter) {
                highlighter.removeHighlight(hilites[i]);
            }
        }
    }
}
