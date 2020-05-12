# Java-FX
Java FX kursundan

Runnable thread = new Runnable{
  @override run() {
    try{
      String s = Platform.isFxApplicationThread() ? "UI THREAD" : "BACKGROUND THREAD";
      //sout(s);
      Thread.sleep(10000);
      Platform.runLater(new Runnable {
        @override run() {
          String s = Platform.isFxApplicationThread() ? "UI THREAD" : "BACKGROUND THREAD";
          //sout(s);
          ourLabel.setText("confirmed");
        }
      }
    }catch(InterruptedException e){
    e.getStackTrace();
    }
 }
}
