package com.gmbh.internetstores.frameworks.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gmbh.internetstores.R;
import com.gmbh.internetstores.frameworks.data.model.ISBikesData;
import com.gmbh.internetstores.frameworks.ui.base.BaseInterface;
import com.gmbh.internetstores.frameworks.ui.deletebike.DeleteBikeActivity;
import com.gmbh.internetstores.frameworks.ui.detailbike.DetailBikeActivity;
import com.gmbh.internetstores.frameworks.ui.editbike.EditBikeActivity;
import com.gmbh.internetstores.frameworks.utils.Constants;
import java.util.ArrayList;
import java.util.List;

public class BikeListAdapter
    extends RecyclerView.Adapter<BikeListAdapter.MyViewHolder> {

  private List<ISBikesData> mList = new ArrayList<>();
  private Activity mActivity;
  RequestOptions requestOptions =
      new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);


  public BikeListAdapter(Activity activity) {
    this.mActivity = activity;
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView imageViewBike, imageViewEdit, imageViewDelete;
    TextView textViewName, textViewCategory, textViewFrameSize, textViewLocation;
    LinearLayout linearLayoutDetail;

    private MyViewHolder(View v) {
      super(v);

      imageViewBike = v.findViewById(R.id.imageViewBike);
      imageViewEdit = v.findViewById(R.id.imageViewEdit);
      imageViewDelete = v.findViewById(R.id.imageViewDelete);
      textViewName = v.findViewById(R.id.textViewName);
      textViewCategory = v.findViewById(R.id.textViewCategory);
      textViewFrameSize = v.findViewById(R.id.textViewFrameSize);
      textViewLocation = v.findViewById(R.id.textViewLocation);
      linearLayoutDetail = v.findViewById(R.id.linearLayoutDetail);
    }
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView =
        LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_bikes, parent, false);

    return new MyViewHolder(itemView);
  }

  @Override
  public int getItemCount() {
    return mList.size();
  }

  @Override
  public void onBindViewHolder(final MyViewHolder holder, int position) {

    final ISBikesData model = mList.get(position);

    if (model.getPhotoUrl() != null) {
      Glide.with(holder.imageViewBike.getContext()).
          load(model.getPhotoUrl()).apply(requestOptions)
          .into(holder.imageViewBike);
    }

    holder.textViewName.setText(model.getName());
    holder.textViewCategory.setText(model.getCategory());
    holder.textViewFrameSize.setText(model.getFrameSize());
    holder.textViewLocation.setText(model.getLocation());

    holder.imageViewEdit.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {

        EditBikeActivity.load((BaseInterface) mActivity,
            false, Constants.PROFIL_UPDATED, model);
      }
    });

    holder.imageViewDelete.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {

        DeleteBikeActivity.load((BaseInterface) mActivity, model);
      }
    });

    holder.linearLayoutDetail.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        DetailBikeActivity.load((BaseInterface) mActivity, model);
      }
    });


  }


  public ISBikesData getItem(int position) {
    return mList.get(position);
  }

  public long getItemId(int position) {
    return position;
  }


  public void addUpdatedData(List<ISBikesData> data) {
    mList.clear();
    mList.addAll(data);
    notifyDataSetChanged();
  }


}
