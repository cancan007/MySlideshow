package com.example.myslideshow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myslideshow.databinding.FragmentImageBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val IMG_RES_ID = "IMG_RES_ID"   //const: 定数を定義するときに使うが、なくても問題はない、メリットはあったほうが多少コードを減らせる
/**                // 上記の定数 IMG_RES_ID はBundleクラスでデータを取り出すときなどに使うキーを保持するためのもの
 * A simple [Fragment] subclass.
 * Use the [ImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var imageResId: Int? = null    // アーギュメンツから取り出した画像のリソースIDを保持する変数

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {    //let: メソッドを実行するときに使うスコープ関数、　apply: プロパティを設定するときに使うスコープ関数
                 // ?.let : 非nullのとき、実効
            imageResId = it.getInt(IMG_RES_ID)  // アーギュメンツに保存されていた画像のリソースIDを変数imageResIdに保存
        }             // getInt(key): キーを指定して、Int型の値を取り出している。
    }

    private var _binding: FragmentImageBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView(){
        super.onDestroyView()   // super( )というのは、一世代上の親クラスのコンストラクタ
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ImageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic     // この記述があることによって、Javaから、このKotlinを呼び出すときにスタティックメソッドとして使用できるようになる。
        fun newInstance(imageResId: Int) =
            // ImageViewに表示する画像のリソースIDを取り、このフラグメントのインスタンスを作成して戻り値として返す。
            ImageFragment().apply {
                arguments = Bundle().apply {     // アーギュメンツに保存するデータはBundleクラスのインスタンス
                    putInt(IMG_RES_ID, imageResId)    // キーを設定して、値を保存　(Key, Value)
                }
            }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?){   //アクティビティのonCreateメソッドで行うような処理はフラグメントではこのメソッドで行うとよい。
        super.onActivityCreated(savedInstanceState)    // onActivityCreatedはonCreatedメソッド完了後に呼びだされます
        imageResId?.let{
            binding.imageView.setImageResource(it)     // フラグメント内のImageViewに画像を設定しています。
        }
    }
}