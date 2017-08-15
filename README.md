# AssEditor

## 一、功能
能够对ass文件的字幕时间进行修改，但仅支持字幕整体快进或后退，无法修改其中个别对白的时间。

## 二、使用
使用`AssEditor`类的`apply()`方法：
``` java
AssEditor editor = new AssEditor();
editor.apply("/home/pentonbin/Desktop/Game.of.Thrones.S07E05.720p.HDTV.x264-AVS.ass",
      0, // hour
      -1, // minute
      -29); // second
```
以上便是对Game.of.Thrones.S07E05.720p.HDTV.x264-AVS.ass字幕文件的对白时间整体：后退1分钟加29秒，也就是整体后退89秒，或者可以直接调用以下代码达到相同的效果：
``` java
AssEditor editor = new AssEditor();
editor.apply("/home/pentonbin/Desktop/Game.of.Thrones.S07E05.720p.HDTV.x264-AVS.ass",
      -89); // seconds
```

## 三、Feedback
Email：pentonbin@gmail.com
