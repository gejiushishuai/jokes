package chenyu.jokes.feature.Joke;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chenyu.jokes.R;
import chenyu.jokes.base.BaseScrollAdapter;
import chenyu.jokes.feature.main.MainActivity;
import chenyu.jokes.model.Data;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;

/**
 * Created by chenyu on 2017/3/3.
 */

public class JokeAdapter extends BaseScrollAdapter<Data, JokeAdapter.JokeViewHolder> {

  public static final int ACTION_UP = 1;
  public static final int ACTION_DOWN = 2;
  public static final int ACTION_COLLECT = 3;
  public static final int ACTION_COMMENT = 4;

  @Override public int getLayout() {
    return R.layout.item_joke;
  }

  @Override protected JokeViewHolder getViewHolder(View view) {
    return new JokeViewHolder(view);
  }

  @Override public void onBindViewHolder(JokeViewHolder holder, int position) {
    super.onBindViewHolder(holder, position);

    holder.content.setText(mItems.get(position).getContent());
    holder.time.setText(mItems.get(position).getTime());
    holder.joke = mItems.get(position).getContent().toString();
    holder.jokeId = mItems.get(position).id;

    holder.txtUp.setText("顶"+mItems.get(position).up_amount);
    holder.txtDown.setText("踩"+mItems.get(position).down_amount);
    holder.txtComment.setText("评论"+mItems.get(position).comment_amount);
    holder.txtCollect.setText("收藏"+mItems.get(position).collect_amount);
    switch (mItems.get(position).my_attitude) {
      case 0:
        holder.txtUp.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.baseGrey));
        holder.txtDown.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.baseGrey));
        break;
      case 1:
        holder.txtUp.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.baseColor));
        holder.txtDown.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.baseGrey));
        break;
      case -1:
        holder.txtUp.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.baseGrey));
        holder.txtDown.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.baseColor));
        break;
    }
    if (mItems.get(position).my_collected) {
      holder.txtCollect.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.baseColor));
    } else {
      holder.txtCollect.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.baseGrey));
    }
  }

  public  class JokeViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.content) public TextView content;
    @BindView(R.id.time) public TextView time;
    @BindView(R.id.share_icon) public ImageButton shareIcon;
    @BindView(R.id.up) public TextView txtUp;
    @BindView(R.id.down) public TextView txtDown;
    @BindView(R.id.comment) public TextView txtComment;
    @BindView(R.id.collect) public TextView txtCollect;
    public String joke;
    private int jokeId;
    //private int position;
    private JokeFragment jokeFragment;

    public JokeViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
      jokeFragment = ((MainActivity)parent.getContext()).getJokeFragment();
    }

    @OnClick(R.id.share_icon) public void directShare(View view) {
      ((MainActivity) view.getContext()).getShareWindow()
          .setText(joke)
          .showAtLocation(view.getRootView(),
              Gravity.BOTTOM, 0, 0);
    }

    @OnClick({R.id.up, R.id.down, R.id.comment, R.id.collect}) public void onClick(View view) {
      switch (view.getId()) {
        case R.id.up:

          jokeFragment.onUP(jokeId, getAdapterPosition());
          break;
        case R.id.down:
          jokeFragment.onDown(jokeId, getAdapterPosition());
          break;
        case R.id.comment:
          Toast.makeText(itemView.getContext(), "comment", Toast.LENGTH_SHORT).show();
          break;
        case R.id.collect:
          jokeFragment.onCollect(jokeId, getAdapterPosition());
          break;
      }
    }


  }

  public void changeAttitude(int position, int action) {
    switch (action) {
      case ACTION_UP:
        toggleUp(position);
        break;
      case ACTION_DOWN:
        toggleDown(position);
        break;
      case ACTION_COLLECT:
        toggleCollect(position);
        break;
    }
  }

  
  private void toggleUp(int position) {
    switch (mItems.get(position).my_attitude) {
      case 1:
        mItems.get(position).up_amount--;
        mItems.get(position).my_attitude = 0;
        break;
      case 0:
        mItems.get(position).up_amount++;
        mItems.get(position).my_attitude = 1;
        break;
      case -1:
        mItems.get(position).up_amount++;
        mItems.get(position).down_amount --;
        mItems.get(position).my_attitude = 1;
        break;
    }
  }

  private void toggleDown(int position) {
    switch (mItems.get(position).my_attitude) {
      case 1:
        mItems.get(position).up_amount--;
        mItems.get(position).down_amount ++;
        mItems.get(position).my_attitude = -1;
        break;
      case 0:
        mItems.get(position).down_amount++;
        mItems.get(position).my_attitude = -1;
        break;
      case -1:
        mItems.get(position).down_amount --;
        mItems.get(position).my_attitude = 0;
        break;
    }
  }
  private void toggleCollect(int position) {
    if (mItems.get(position).my_collected) {
      mItems.get(position).my_collected = false;
      mItems.get(position).collect_amount --;
    } else {
      mItems.get(position).my_collected = true;
      mItems.get(position).collect_amount ++;
    }
  }
}
