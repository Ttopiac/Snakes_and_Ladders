使用此程式碼，請依照下述步驟使用：

1. 滑鼠雙擊compiler.bat，此動作將編譯其中所有的.java檔案。
2. 滑鼠雙擊SIRMI.bat，此動作將啟動RMI Registry。
3. 待五秒後滑鼠雙擊SIServer.bat，此動作將啟動蛇梯棋的Server端。
4. 滑鼠雙擊SIClient.bat，此動作將啟動蛇梯棋的Client端。
5. 假若要啟動多個Client端，可執行步驟4多次。


p.s.	假若想要client註冊多筆帳戶，可於cmd中執行SnakeLaddersTestClient.class，
	並於後給予兩筆輸入參數，第一個參數表示起始的帳號(必須為數字)，
	第二個參數表示想要註冊的帳號數量(必須為數字)
	Ex: 	於cmd中 執行下述指令 起始帳號為0 可註冊500筆帳號 (帳號名稱依序為0~499)
		java SnakeLaddersTestClient 0 500