package com.dmuk.rellik.busstop.adapters;

import static com.dmuk.rellik.busstop.R.id.checkBoxNo;
import static com.dmuk.rellik.busstop.R.id.checkBoxYes;
import static com.dmuk.rellik.busstop.R.id.textViewQuestion;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import com.dmuk.rellik.busstop.R;
import com.dmuk.rellik.busstop.InterfaceForReports.DataForReports;
import java.util.List;

/* By Eaun Ballinger 2018 */

public class QuestionsAdapterView extends
    RecyclerView.Adapter<QuestionsAdapterView.QuestionViewHolder> implements
    DataForReports {

  private DataForReports checkViewClickListener;
  private int passID;
  private String passQuestion, passLevel, getDefault;
  private ThreadLocal<List<ViewQuestionsList>> DatabaseList;
  private Context mContext;

  public QuestionsAdapterView(Context pContext, List<ViewQuestionsList> DataList) {
    this.DatabaseList = new ThreadLocal<>();
    this.DatabaseList.set(DataList);
    this.mContext = pContext;
  }

  @Override
  public QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v;
    v = LayoutInflater.
        from(parent.getContext()).
        inflate(R.layout.viewquestion, parent, false);

    checkViewClickListener = (DataForReports) mContext;
    return new QuestionViewHolder(v);
  }

  @Override
  public void onBindViewHolder(QuestionViewHolder holder, int position) {
    ViewQuestionsList ci = DatabaseList.get().get(position);
    holder.get_question_text.setText(ci.getQuestionList());
    holder.get_level_text.setText(ci.getLevelList());
    holder.vID = Integer.parseInt(ci.get_idList());
    holder.itemView.setTag(DatabaseList.get().get(position));
    passQuestion = ci.getQuestionList();
    holder.checkDefault = ci.getDefaultList();
    holder.get_Box_No.setTag(holder.getAdapterPosition());
    holder.get_Box_Yes.setTag(holder.getAdapterPosition());
  }


  @Override
  public int getItemCount() {
    return DatabaseList.get().size();
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public int getItemViewType(int position) {
    return position;
  }

  @Override
  public void IdataForTheReport(int position, String CheckedMessage, String CheckedLevel,
      Boolean IsItChecked, String theDefault) {
  }


  @Override
  public void ideleteUnclickedForReport(int ID) {

  }

  public class QuestionViewHolder extends RecyclerView.ViewHolder {

    private TextView get_question_text;
    private TextView get_level_text;
    private CheckBox get_Box_No, get_Box_Yes;
    private String checkDefault;

    private int vID;

    private QuestionViewHolder(View v) {
      super(v);
      get_Box_No = v.findViewById(checkBoxNo);
      get_Box_Yes = v.findViewById(checkBoxYes);
      get_question_text = v.findViewById(textViewQuestion);
      get_level_text = v.findViewById(R.id.textViewLevel);
      passQuestion = get_level_text.getText().toString();
      get_Box_Yes.setOnCheckedChangeListener((buttonView, isChecked) -> {
        if (isChecked) {
          getDefault = checkDefault;
          get_Box_No.setChecked(false);
          Log.d("check default", getDefault);
          passQuestion = get_question_text.getText().toString();
          passLevel = get_level_text.getText().toString();
          passID = Integer.parseInt(get_Box_Yes.getTag().toString()) + 1;
          checkViewClickListener.IdataForTheReport(passID, passQuestion, passLevel,
              true, getDefault);

        } else {
          checkViewClickListener.ideleteUnclickedForReport(passID);
        }
      });

      get_Box_No.setOnCheckedChangeListener((buttonView, isChecked) -> {
        if (isChecked) {
          getDefault = checkDefault;
          get_Box_Yes.setChecked(false);
          Log.d("check default", getDefault);
          passQuestion = get_question_text.getText().toString();
          passID = Integer.parseInt(get_Box_No.getTag().toString()) + 1;
          passLevel = get_level_text.getText().toString();
          checkViewClickListener.IdataForTheReport(passID, passQuestion, passLevel,
              false, getDefault);

        } else {
          checkViewClickListener.ideleteUnclickedForReport(passID);
        }
      });
    }
  }
}
