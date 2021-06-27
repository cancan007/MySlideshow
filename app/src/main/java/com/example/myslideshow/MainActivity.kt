package com.example.myslideshow

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.myslideshow.databinding.ActivityMainBinding
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
// アクティビティの動作は、onCreate, onResume, onPauseなどで行っており、MainActivity内のクラスは前述の関数を実行する中で用いる数値やIDなどを抽出または作成するためのものなので、子classは戻り値に着目
    private lateinit var player: MediaPlayer   // lateinit でonCreateで初期化するまで、nullを入れて保持することができる。

    class MyAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa){  // FragmentStateAdapterクラスからメソッドを継承するため記述

        private val resources = listOf( // listOf(): 変更不可のリスト
            R.drawable.slide00, R.drawable.slide01,
            R.drawable.slide02, R.drawable.slide03,
            R.drawable.slide04, R.drawable.slide05,
            R.drawable.slide06, R.drawable.slide07,
            R.drawable.slide08, R.drawable.slide09
        )

        override fun getItemCount(): Int = resources.size  // getItemCount():画像の総数を返す。　sizeで要素数のプロパティを得る。

        override fun createFragment(position: Int): Fragment
                = ImageFragment.newInstance(resources[position])  //ImageFragmentのインスタンスを返すことによって、引数で指定したページ番号を読み取り、戻り値として返す。
    }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {     //onCreate: Activityが初めて生成され、Activityの初期化は全てここに書く。つまり全て初期化される
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pager.adapter = MyAdapter(this)    // adapterでViewPager2とFragmentStateAdapterを関連づける
        binding.pager.setPageTransformer(ViewPager2PageTransformation())   //引数のクラスをsetPageTransformerメソッドを使ってViewPager2に設定する

        val handler = Handler(Looper.getMainLooper())   // Looper.getMainLooper()でメインスレッドに接続
        timer(period = 5000){     // timer: 引数に[ms]での時間間隔を指定し、その間隔で下記を実効
            handler.post{    // handler.post: UIスレッドでRunnableを行う。
                        // Runnableの定義:  インスタンスを1つのスレッドで実行するすべてのクラスで、Runnableインタフェースを実装する必要があります。このクラスは、引数のないメソッドrunを定義しなければいけません。
                binding.apply{
                    pager.currentItem = (pager.currentItem + 1)%10   // +1し10で割った余りをページ番号としている
                }
            }
        }
        player = MediaPlayer.create(this,R.raw.getdown)    // createメソッドでMediaPlayerのインスタンスを生成します。第一引数でアクティビティ、第二引数でサウンドファイルのリソースID
        player.isLooping = true    //音楽再生を繰り返す

    }
    override fun onResume(){   //onResumeはアクティビティが画面表示されるときに呼ばれる。
        super.onResume()
        player.start()
    }

    override fun onPause(){   //アクティビティが中断されたら呼ばれる。
        super.onPause()
        player.pause()  //一時停止
    }

    class ViewPager2PageTransformation: ViewPager2.PageTransformer{
        override fun transformPage(page: View, position:Float){

            when {
                position < -1 ->{   //ページの位置が画面の左側(position=-1)にはみ出してしまい見えない時のビューのプロパティを指定。
                    page.alpha = 0.2f  //alpha: ビューの透過率　0.2fで薄く表示
                    page.scaleX = 0.2f //scale: 0.2fで実際のビューの20%の大きさに縮小
                    page.scaleY = 0.2f
                }
                position <= 1 ->{    //ページが画面上に表示されているときの処理(position=-0.5~0.5)
                    page.alpha = Math.max(0.2f, 1 - Math.abs(position))
                    page.scaleX = Math.max(0.2f, 1 - Math.abs(position))    //Math.max: 引数のうち大きいほうの値を返す
                    page.scaleY = Math.max(0.2f, 1 - Math.abs(position))    //Math.abs: 絶対値を返す
                }
                else ->{    //ページの位置が右側(position=1)にはみ出して見えないとき
                    page.alpha = 0.2f
                    page.scaleX = 0.2f
                    page.scaleY = 0.2f
                }
            }
        }
    }
}