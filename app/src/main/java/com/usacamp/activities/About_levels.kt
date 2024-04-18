package com.usacamp.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.usacamp.R
import com.usacamp.constants.Constants
import com.usacamp.model.MediaItem
import com.usacamp.utils.AboutLevelAdapter
import com.usacamp.utils.ItemOffsetDecoration
import com.usacamp.utils.aboutlevelitem

class About_levels : BaseActivity() {
    var tmpBtn: Button? = null
    private lateinit var adapterAboutLevel: AboutLevelAdapter
    private var aboutlevellist :ArrayList<aboutlevelitem> = ArrayList()
    lateinit var recycleView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_levels)
        //setToolbarProperties()
        tmpBtn = findViewById<View>(R.id.level1) as Button
        recycleView = findViewById(R.id.recyclerView)
        tmpBtn!!.callOnClick()
    }

//    private fun setToolbarProperties() {
//        // toolbar
//        //val toolbar = findViewById<View>(R.id.abouttitlebar) as Toolbar
//        setSupportActionBar(toolbar)
//
//        // add back arrow to toolbar
//        if (supportActionBar != null) {
//            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//            supportActionBar!!.setDisplayShowHomeEnabled(true)
//            //            getSupportActionBar().setDisplayShowTitleEnabled(false);
//            supportActionBar!!.title = "课程解读"
//        }
//    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(menuItem)
    }

    fun onBack(view: View?) {
        finish()
    }

    fun onButtonCamp(v: View) {
        tmpBtn!!.setTextColor(Color.BLACK)
        tmpBtn!!.setBackgroundColor(Color.parseColor("#e7e7e7"))
        val b = v as Button
        b.setTextColor(Color.WHITE)
        b.setBackgroundColor(Color.parseColor("#e29999"))
        var midtxt = b.text as String
        midtxt = midtxt.substring(4)
        tmpBtn = b
        var index : Int = 0
        var tmptList: List<String> = ArrayList()
        var tmpdList: List<String>? = ArrayList()
        when (midtxt.trim()){
            "1"-> {
                tmptList = Constants.About_camp1_t.split(",")
                tmpdList = Constants.About_camp1_d.split(",")
            }
            "2"-> {
                tmptList = Constants.About_camp2_t.split(",")
                tmpdList = Constants.About_camp2_d.split(",")
            }
            "3"-> {
                tmptList = Constants.About_camp3_t.split(",")
                tmpdList = Constants.About_camp3_d.split(",")
            }
            "4"-> {
                tmptList = Constants.About_camp4_t.split(",")
                tmpdList = Constants.About_camp4_d.split(",")
            }
            "5"-> {
                tmptList = Constants.About_camp5_t.split(",")
                tmpdList = Constants.About_camp5_d.split(",")
            }
            "6"-> {
                tmptList = Constants.About_camp6_t.split(",")
                tmpdList = Constants.About_camp6_d.split(",")
            }
            "7"-> {
                tmptList = Constants.About_camp7_t.split(",")
                tmpdList = Constants.About_camp7_d.split(",")
            }
            "8"-> {
                tmptList = Constants.About_camp8_t.split(",")
                tmpdList = Constants.About_camp8_d.split(",")
            }
            "9"-> {
                tmptList = Constants.About_camp9_t.split(",")
                tmpdList = Constants.About_camp9_d.split(",")
            }
            "10"-> {
                tmptList = Constants.About_camp10_t.split(",")
                tmpdList = Constants.About_camp10_d.split(",")
            }
            "11"-> {
                tmptList = Constants.About_camp11_t.split(",")
                tmpdList = Constants.About_camp11_d.split(",")
            }
            "12"-> {
                tmptList = Constants.About_camp12_t.split(",")
                tmpdList = Constants.About_camp12_d.split(",")
            }
        }
        aboutlevellist.clear()
        for(itemtitle in tmptList) {
            var item : aboutlevelitem = aboutlevelitem(itemtitle, tmpdList!!.get(index))
            aboutlevellist.add(item)
            index++
        }
        val gridLayoutManager: GridLayoutManager = GridLayoutManager(this, 3)
        recycleView.layoutManager = gridLayoutManager
        adapterAboutLevel = AboutLevelAdapter(aboutlevellist)
        //recycleView.addItemDecoration(ItemOffsetDecoration(30))
        recycleView.itemAnimator = DefaultItemAnimator()
        recycleView.adapter = adapterAboutLevel
        recycleView.setHasFixedSize(true)
        //        switch(midtxt.trim()){
//            case "1":
//                txt1_1.setText(Constants.About_camp1_1_1);
//                txt1_2.setText(Constants.About_camp1_1_2);
//                txt1_3.setText(Constants.About_camp1_1_3);
//
//                txt2_1.setText(Constants.About_camp1_2_1);
//                txt2_2.setText(Constants.About_camp1_2_2);
//                txt2_3.setText(Constants.About_camp1_2_3);
//
//                txt3_1.setText(Constants.About_camp1_3_1);
//                txt3_2.setText(Constants.About_camp1_3_2);
//                txt3_3.setText(Constants.About_camp1_3_3);
//                break;
//            case "2":
//                txt1_1.setText(Constants.About_camp2_1_1);
//                txt1_2.setText(Constants.About_camp2_1_2);
//                txt1_3.setText(Constants.About_camp2_1_3);
//
//                txt2_1.setText(Constants.About_camp2_2_1);
//                txt2_2.setText(Constants.About_camp2_2_2);
//                txt2_3.setText(Constants.About_camp2_2_3);
//
//                txt3_1.setText(Constants.About_camp2_3_1);
//                txt3_2.setText(Constants.About_camp2_3_2);
//                txt3_3.setText(Constants.About_camp2_3_3);
//                break;
//            case "3":
//                txt1_1.setText(Constants.About_camp3_1_1);
//                txt1_2.setText(Constants.About_camp3_1_2);
//                txt1_3.setText(Constants.About_camp3_1_3);
//
//                txt2_1.setText(Constants.About_camp3_2_1);
//                txt2_2.setText(Constants.About_camp3_2_2);
//                txt2_3.setText(Constants.About_camp3_2_3);
//
//                txt3_1.setText(Constants.About_camp3_3_1);
//                txt3_2.setText(Constants.About_camp3_3_2);
//                txt3_3.setText(Constants.About_camp3_3_3);
//                break;
//            case "4":
//                txt1_1.setText(Constants.About_camp4_1_1);
//                txt1_2.setText(Constants.About_camp4_1_2);
//                txt1_3.setText(Constants.About_camp4_1_3);
//
//                txt2_1.setText(Constants.About_camp4_2_1);
//                txt2_2.setText(Constants.About_camp4_2_2);
//                txt2_3.setText(Constants.About_camp4_2_3);
//
//                txt3_1.setText(Constants.About_camp4_3_1);
//                txt3_2.setText(Constants.About_camp4_3_2);
//                txt3_3.setText(Constants.About_camp4_3_3);
//                break;
//            case "5":
//                txt1_1.setText(Constants.About_camp5_1_1);
//                txt1_2.setText(Constants.About_camp5_1_2);
//                txt1_3.setText(Constants.About_camp5_1_3);
//
//                txt2_1.setText(Constants.About_camp5_2_1);
//                txt2_2.setText(Constants.About_camp5_2_2);
//                txt2_3.setText(Constants.About_camp5_2_3);
//
//                txt3_1.setText(Constants.About_camp5_3_1);
//                txt3_2.setText(Constants.About_camp5_3_2);
//                txt3_3.setText(Constants.About_camp5_3_3);
//                break;
//            case "6":
//                txt1_1.setText(Constants.About_camp6_1_1);
//                txt1_2.setText(Constants.About_camp6_1_2);
//                txt1_3.setText(Constants.About_camp6_1_3);
//
//                txt2_1.setText(Constants.About_camp6_2_1);
//                txt2_2.setText(Constants.About_camp6_2_2);
//                txt2_3.setText(Constants.About_camp6_2_3);
//
//                txt3_1.setText(Constants.About_camp6_3_1);
//                txt3_2.setText(Constants.About_camp6_3_2);
//                txt3_3.setText(Constants.About_camp6_3_3);
//                break;
//            case "7":
//                txt1_1.setText(Constants.About_camp7_1_1);
//                txt1_2.setText(Constants.About_camp7_1_2);
//                txt1_3.setText(Constants.About_camp7_1_3);
//
//                txt2_1.setText(Constants.About_camp7_2_1);
//                txt2_2.setText(Constants.About_camp7_2_2);
//                txt2_3.setText(Constants.About_camp7_2_3);
//
//                txt3_1.setText(Constants.About_camp7_3_1);
//                txt3_2.setText(Constants.About_camp7_3_2);
//                txt3_3.setText(Constants.About_camp7_3_3);
//                break;
//            case "8":
//                txt1_1.setText(Constants.About_camp8_1_1);
//                txt1_2.setText(Constants.About_camp8_1_2);
//                txt1_3.setText(Constants.About_camp8_1_3);
//
//                txt2_1.setText(Constants.About_camp8_2_1);
//                txt2_2.setText(Constants.About_camp8_2_2);
//                txt2_3.setText(Constants.About_camp8_2_3);
//
//                txt3_1.setText(Constants.About_camp8_3_1);
//                txt3_2.setText(Constants.About_camp8_3_2);
//                txt3_3.setText(Constants.About_camp8_3_3);
//                break;
//            case "9":
//                txt1_1.setText(Constants.About_camp9_1_1);
//                txt1_2.setText(Constants.About_camp9_1_2);
//                txt1_3.setText(Constants.About_camp9_1_3);
//
//                txt2_1.setText(Constants.About_camp9_2_1);
//                txt2_2.setText(Constants.About_camp9_2_2);
//                txt2_3.setText(Constants.About_camp9_2_3);
//
//                txt3_1.setText(Constants.About_camp9_3_1);
//                txt3_2.setText(Constants.About_camp9_3_2);
//                txt3_3.setText(Constants.About_camp9_3_3);
//                break;
//            case "10":
//                txt1_1.setText(Constants.About_camp10_1_1);
//                txt1_2.setText(Constants.About_camp10_1_2);
//                txt1_3.setText(Constants.About_camp10_1_3);
//
//                txt2_1.setText(Constants.About_camp10_2_1);
//                txt2_2.setText(Constants.About_camp10_2_2);
//                txt2_3.setText(Constants.About_camp10_2_3);
//
//                txt3_1.setText(Constants.About_camp10_3_1);
//                txt3_2.setText(Constants.About_camp10_3_2);
//                txt3_3.setText(Constants.About_camp10_3_3);
//                break;
//            case "11":
//                txt1_1.setText(Constants.About_camp11_1_1);
//                txt1_2.setText(Constants.About_camp11_1_2);
//                txt1_3.setText(Constants.About_camp11_1_3);
//
//                txt2_1.setText(Constants.About_camp11_2_1);
//                txt2_2.setText(Constants.About_camp11_2_2);
//                txt2_3.setText(Constants.About_camp11_2_3);
//
//                txt3_1.setText(Constants.About_camp11_3_1);
//                txt3_2.setText(Constants.About_camp11_3_2);
//                txt3_3.setText(Constants.About_camp11_3_3);
//                break;
//            case "12":
//                txt1_1.setText(Constants.About_camp12_1_1);
//                txt1_2.setText(Constants.About_camp12_1_2);
//                txt1_3.setText(Constants.About_camp12_1_3);
//
//                txt2_1.setText(Constants.About_camp12_2_1);
//                txt2_2.setText(Constants.About_camp12_2_2);
//                txt2_3.setText(Constants.About_camp12_2_3);
//
//                txt3_1.setText(Constants.About_camp12_3_1);
//                txt3_2.setText(Constants.About_camp12_3_2);
//                txt3_3.setText(Constants.About_camp12_3_3);
//                break;
//                default:
//                    break;
//        }
    }
}