package com.pentb.asseditor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pentonbin on 17-8-14.
 */
public class AssEditor {

    public static final String EVENT_TEXT = "[Events]";

    private boolean readEventText = false; // 读取到[Events]行
    private boolean readFormatText = false; // 读取到Format行

    private List<String> infoContents = new ArrayList<>();
    private List<String> dialogueContents = new ArrayList<>();

    public void apply(String fileName, int hours, int minutes, int seconds) {
        apply(fileName, hours * 60 * 60 + minutes * 60 + seconds);
    }

    public void apply(String fileName, int seconds) {
        System.out.println("Applying...");
        AssReader reader = new AssReader();
        reader.read(fileName, new AssReader.EditCallback() {
            @Override
            public void edit(String line) {
                if (readEventText) {
                    if (!readFormatText) {
                        readFormatText = true;
                    } else {
                        dialogueContents.add(line);
                        return;
                    }
                }
                if (!readEventText && line.trim().equalsIgnoreCase(EVENT_TEXT)) {
                    readEventText = true;
                }
                infoContents.add(line);
            }
        });
        List<AssDialogue> assDialogues = applyChange(seconds);
        System.out.println("Write to file...");
        writeToFile(fileName, infoContents, assDialogues);
        System.out.println("Done...");
    }

    private void writeToFile(String fileName, List<String> infoContents, List<AssDialogue> assDialogues) {
        BufferedWriter writer = null;
        try {
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
            writer = new BufferedWriter(new FileWriter(file));
            for (String infoContent : infoContents) {
                writer.write(infoContent + "\n");
            }
            for (AssDialogue assDialogue : assDialogues) {
                writer.write(assDialogue.toString() + "\n");
            }

        } catch (Exception e) {

        } finally {
            IOUtils.closeIO(writer);
        }

    }

    private List<AssDialogue> applyChange(int seconds) {
        List<AssDialogue> assDialogues = new ArrayList<>();
        if (!dialogueContents.isEmpty()) {
            for (String dialogue : dialogueContents) {
                AssDialogue assDialogue = AssDialogue.parseDialogue(dialogue, seconds);
                if (assDialogue != null) {
                    assDialogues.add(assDialogue);
                }
            }
        }
        return assDialogues;
    }


    public static void main(String[] args) {
        AssEditor editor = new AssEditor();
        editor.apply("/home/pentonbin/Desktop/Game.of.Thrones.S07E05.720p.HDTV.x264-AVS.ass",
                -89);
    }
}
