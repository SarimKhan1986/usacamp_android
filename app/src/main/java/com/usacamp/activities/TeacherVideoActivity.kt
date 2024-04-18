package com.usacamp.activities
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.usacamp.R
import com.usacamp.model.MediaItem
import com.usacamp.utils.ItemOffsetDecoration
import com.usacamp.utils.TeacherVideoAdapter

class TeacherVideoActivity : BaseActivity() {
    private lateinit var adapterTeacher: TeacherVideoAdapter
    private var videolist :ArrayList<MediaItem>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_video)
        val recycleView:RecyclerView = findViewById(R.id.teachervideolist)
        videolist = MyApplication.mNetProc.mLoginUserInf.mlistTeacherMedia
        val gridLayoutManager:GridLayoutManager = GridLayoutManager(this, 2)
        recycleView.layoutManager = gridLayoutManager
        adapterTeacher = TeacherVideoAdapter(videolist)
        recycleView.addItemDecoration(ItemOffsetDecoration(30))
        recycleView.itemAnimator = DefaultItemAnimator()
        recycleView.adapter = adapterTeacher
        recycleView.setHasFixedSize(true)
    }

    fun onBack(view: View) {finish()}
}



