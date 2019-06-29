package com.dmuk.rellik.busstop.adapters;

/* By Eaun Ballinger 2018 */

public class ViewQuestionsList {

  private String _idList, questionList, levelList, defaultList;

  public ViewQuestionsList() {
  }

  public ViewQuestionsList(String _id, String question, String level, String _default) {

    this._idList = _id;
    this.questionList = question;
    this.levelList = level;
    this.defaultList = _default;

  }

  public String get_idList() {
    return _idList;
  }

  public void set_idList(String _idList) {
    this._idList = _idList;
  }

  public String getQuestionList() {
    return questionList;
  }

  public void setQuestionList(String questionList) {
    this.questionList = questionList;
  }

  public String getLevelList() {
    return levelList;
  }

  public void setLevelList(String levelList) {
    this.levelList = levelList;
  }

  public String getDefaultList() {
    return defaultList;
  }

  public void setDefaultList(String defaultList) {
    this.defaultList = defaultList;
  }
}
