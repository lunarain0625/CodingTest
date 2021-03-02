package com.example.weatherboi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public abstract class BaseAdapter<T,H extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder>{

    protected static final String TAG = BaseAdapter.class.getSimpleName();

    protected final Context context;

    protected int layoutResid;

    protected List<T> datas;

    private OnItemClickListener mOnItemClickListener = null;

    public interface OnItemClickListener{
        void OnItemClick(View view, int position);
    }


    public BaseAdapter(Context context, int layoutResid) {
        this(context, layoutResid, null);
    }

    public BaseAdapter(Context context, int layoutResid, List<T> datas) {
        this.context = context;
        this.layoutResid = layoutResid;
        this.datas = datas == null ? new ArrayList<T>() : datas;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        T item = getItem(position);
        convert((H)holder, item);
    }

    protected abstract void convert(H holder, T item);

    public T getItem(int position) {
        if(position >= datas.size()) return null;
        return datas.get(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutResid,parent,false);
        BaseViewHolder vh = new BaseViewHolder(view, mOnItemClickListener);
        return vh;
    }

    @Override
    public int getItemCount() {
        if(datas == null || datas.size()<=0){
            return 0;
        }
        return datas.size();
    }

    public void clear(){
        for (Iterator it = datas.iterator(); it.hasNext();) {

            T t = (T) it.next();
            int position = datas.indexOf(t);
            it.remove();
            notifyItemRemoved(position);
        }
    }

    public void removeItem(T t){
        int position = datas.indexOf(t);
        datas.remove(position);
        notifyItemRemoved(position);
    }

    public List<T> getDatas(){
        return datas;
    }


    public void addData(List<T> list){
        addData(0, list);
    }

    public void addData(int position, List<T> list){
        if(list!=null && list.size()>0){
            for (T t :
                    list) {
                datas.add(position, t);
                notifyItemInserted(position);
            }
        }
    }

    public void refreshDatas(List<T> list){
        clear();
        if (list != null && list.size()>0){
            int size = list.size();
            for (int i = 0; i < size; i++) {
                datas.add(list.get(i));
                notifyItemInserted(i);
            }
        }
    }

    public  void loadMoreDatas(List<T> list){
        if(list != null && list.size()>0){
            int size = list.size();
            int begin = datas.size();
            for (int i = 0; i < size; i++) {
                datas.add(list.get(i));
                notifyItemInserted(i+begin);
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public int getLayoutResid() {
        return layoutResid;
    }

    public void setLayoutResid(int layoutResid) {
        this.layoutResid = layoutResid;
    }

}
