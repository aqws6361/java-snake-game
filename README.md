# 🐍 Java Classic Snake Game (經典貪食蛇)

這是一個使用 Java 內建 `Swing` 與 `AWT` 函式庫開發的經典貪食蛇桌面遊戲。
本專案專注於良好的程式碼架構（職責分離）以及流暢的遊戲體驗，並支援跨平台的最高分紀錄儲存功能。

## ✨ 遊戲特色 (Features)

* **無縫穿牆機制 (Wrap-around Boundaries)**：蛇在碰到視窗邊緣時不會死亡，而是會從另一側穿越出現，增加遊戲的靈活性與趣味性。
* **跨平台最高分紀錄 (Cross-Platform High Score Tracking)**：
    * 利用 `Try-with-resources` 確保安全的檔案 I/O 操作。
    * 分數檔案會安全地儲存於使用者的系統家目錄下（`~/.snakegame/scores.txt`），確保無論是在 Windows、macOS 或 Linux 環境下打包成 JAR 檔執行，都不會發生權限不足或路徑錯誤的問題。
* **清晰的軟體架構 (Clean Architecture)**：
    * 實作 **單一職責原則 (Single Responsibility Principle)**。
    * 將「遊戲數據與邏輯（Model）」與「畫面繪製（View）」徹底分離。由 `TimerTask` 專職處理座標更新與碰撞偵測，而 `paintComponent` 僅負責渲染畫面，大幅提升遊戲穩定度與程式碼可維護性。

## 🛠️ 技術棧 (Tech Stack)

* **語言**：Java
* **圖形介面**：Java Swing, Java AWT
* **核心應用**：Object-Oriented Programming (OOP), File I/O, Event Listeners, Timer

## 🚀 如何執行 (How to Run)

### 方法一：透過 JAR 檔直接執行 (推薦)
如果你已經下載了本專案的 `.jar` 執行檔，只要你的電腦有安裝 Java 執行環境 (JRE/JDK)，即可透過以下方式執行：
1. 雙擊該 `.jar` 檔案。
2. 或打開終端機 (Terminal / 命令提示字元)，輸入指令：
   ```bash
   java -jar SnakeGame.jar
