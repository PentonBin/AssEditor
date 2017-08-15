package com.pentb.asseditor;

/**
 * Created by pentonbin on 17-8-14.
 * <p>
 * ass文件的对话格式如下：
 * Format: Layer, Start, End, Style, Name, MarginL, MarginR, MarginV, Effect, Text
 */
public class AssDialogue {

    public static final int DIALOGUE_INFO_LENGTH = 10; // 不包含format字段

    private String format;
    private String layer; // 有不同图层数值的字幕会在重叠检测中被忽略.大数值的图层会覆盖在小数值的图层上面.
    private String start; // 事件的开始时间,格式为 0:00:00:00（小时:分:秒:毫秒）,注意小时只有一位.
    private String end; // 事件的结束时间,格式为 0:00:00:00（小时:分:秒:毫秒）,注意小时只有一位.
    private String style; // 样式名.
    private String name; // 角色名.说这条对白的角色名.只为了在编辑和设定时间轴时方便辨认.
    private String marginL; // 4 位的左边距覆写值（为像素）.0000 表示使用在 Style 行中定义的值.
    private String marginR; // 4 位的右边距覆写值（为像素）.0000 表示使用在 Style 行中定义的值.
    private String marginV; // 4 位的垂直边距覆写值（为像素）.0000 表示使用在 Style 行中定义的值.
    private String effect; // 过渡效果.可以为空值
    private String text; // 字幕文本.它是作为字幕实际出现在屏幕上的文本.

    public AssDialogue() {
    }

    public AssDialogue(String format, String layer, String start, String end, String style, String name,
                       String marginL, String marginR, String marginV, String effect, String text) {
        this.format = format;
        this.layer = layer;
        this.start = start;
        this.end = end;
        this.style = style;
        this.name = name;
        this.marginL = marginL;
        this.marginR = marginR;
        this.marginV = marginV;
        this.effect = effect;
        this.text = text;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMarginL() {
        return marginL;
    }

    public void setMarginL(String marginL) {
        this.marginL = marginL;
    }

    public String getMarginR() {
        return marginR;
    }

    public void setMarginR(String marginR) {
        this.marginR = marginR;
    }

    public String getMarginV() {
        return marginV;
    }

    public void setMarginV(String marginV) {
        this.marginV = marginV;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return format + ": " +
                layer + "," +
                start + "," +
                end + "," +
                style + "," +
                name + "," +
                marginL + "," +
                marginR + "," +
                marginV + "," +
                effect + "," +
                text + ",";
    }

    /**
     * 对一行对白进行时间修改
     *
     * @param line    对白
     * @param seconds 时间，单位为秒，正数则表示推前，负数则表示推后
     * @return
     */
    public static AssDialogue parseDialogue(String line, int seconds) {
        if (line == null || line.trim().length() == 0) {
            return null;
        }
        if (line.contains(":")) {
            String[] info = line.split(":", 2);
            String[] dialogue = null;
            if (info != null && info.length == 2) {
                if (info[1] != null && info[1].contains(",")) {
                    dialogue = info[1].split(",", DIALOGUE_INFO_LENGTH);
                    if (dialogue != null && dialogue.length == DIALOGUE_INFO_LENGTH) {
                        return new AssDialogue(info[0].trim(), // format
                                dialogue[0].trim(), // layer
                                changeTime(dialogue[1], seconds).trim(), // start
                                changeTime(dialogue[2], seconds).trim(), // end
                                dialogue[3].trim(), // style
                                dialogue[4].trim(), // name
                                dialogue[5].trim(), // marginL
                                dialogue[6].trim(), // marginR
                                dialogue[7].trim(), // marginV
                                dialogue[8].trim(), // effect
                                dialogue[9].trim() // text
                        );
                    }
                }
            }
        }
        return null;
    }

    private static String changeTime(String timeString, int sec) {
        try {
            int hour, minute, second, millisSec; // hour长度为1, minute,second长度均为2, millisSec长度为3
            if (timeString.contains(":")) {
                String[] times = timeString.split(":", 3);
                if (times.length == 3 && times[2].contains(".")) {
                    hour = Integer.valueOf(times[0]);
                    minute = Integer.valueOf(times[1]);
                    if (times[2] != null && times[2].contains(".")) {
                        String[] seconds = times[2].split("\\.", 2);
                        second = Integer.valueOf(seconds[0]);
                        millisSec = Integer.valueOf(seconds[1]);

                        int secondSum = hour * 60 * 60 + minute * 60 + second;
                        int subSecond = secondSum + sec;
                        if (subSecond > 0) {
                            second = subSecond % 60;
                            minute = ((subSecond - second) / 60) % 60;
                            hour = ((subSecond - second) / 60 - minute) / 60;
                            return hour + ":" +
                                    ((minute <= 9 && minute >= 0 ? "0" : "") + minute) + ":" +
                                    ((second <= 9 && second >= 0 ? "0" : "") + second) + "." +
                                    millisSec;
                        }
                    }
                }
            }
        } catch (Exception e) {
            return timeString;
        }
        return timeString;
    }
}
