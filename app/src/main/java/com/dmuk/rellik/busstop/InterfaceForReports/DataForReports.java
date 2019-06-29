package com.dmuk.rellik.busstop.InterfaceForReports;

/* By Eaun Ballinger 2018 */

public interface DataForReports {


  void IdataForTheReport(int position, String CheckedMessage, String CheckedLevel,
      Boolean IsItChecked, String theDefault);

  void ideleteUnclickedForReport(int ID);

}
