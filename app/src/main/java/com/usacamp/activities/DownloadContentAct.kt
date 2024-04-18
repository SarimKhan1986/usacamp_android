package com.usacamp.activities
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stephenvinouze.segmentedprogressbar.SegmentedProgressBar
import com.stephenvinouze.segmentedprogressbar.models.SegmentColor
import com.stephenvinouze.segmentedprogressbar.models.SegmentCoordinates
import com.usacamp.R
import com.usacamp.activities.ui.theme.UsacampTheme
import com.usacamp.constants.Constants
import kotlinx.coroutines.*
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.time.format.TextStyle
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class DownloadContentAct : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UsacampTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    DownloadZipFileButton(MyApplication.mNetProc.mLoginUserInf.mnSelectedLevel)
                }
            }
        }
    }
    @Composable
    fun HelloWorldPreview() {
        Text("Hello World")
    }
    fun Finish() {
        finish()
    }
    @Composable
    fun DownloadZipFileButton(levelId: Int) {
        val context = LocalContext.current
        var downloadProgress by remember { mutableStateOf(0) }
        var extractProgress by remember { mutableStateOf(0) }
        var showProgress by remember { mutableStateOf(false) }

        showProgress = true
        LaunchedEffect(true) {
            withContext(Dispatchers.IO) {
                if (!IsControlJsExist(context)) {
                    val downloadedFile = downloadFile(context, 0) { progress ->
                        downloadProgress = progress
                    }
                    // 2. 파일 압축해제
                    unpackZip(downloadedFile.path) { progress ->
                        extractProgress = progress
                    }
                }
                downloadProgress = 0
                extractProgress = 0
                // 1. 파일 다운로드
                val downloadedFile = downloadFile(context, levelId) { progress ->
                    downloadProgress = progress
                }
                // 2. 파일 압축해제
                unpackZip(downloadedFile.path) { progress ->
                    extractProgress = progress
                }
                MyApplication.getInstance().saveLevelListInOfflineMode(levelId)
                // 압축 해제 완료 후 프로그레스바 숨기기
                showProgress = false
                Finish()
                gotoLearnActivity(context, levelId)
            }
        }

        // 다운로드 및 압축해제 진행 상태를 보여줄 프로그레스바
        if (showProgress) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    StripedProgressIndicator(progress = downloadProgress / 100f)
                    SegmentedProgressIndicator(backgroundColor = Color(67, 205, 27), progress = extractProgress / 100f, numberOfSegments = 15, progressHeight = 50.dp,color = Color(67, 205, 27), modifier = Modifier.width(350.dp))

                    //LinearProgressIndicator(progress = extractProgress / 100f)

                }
            }
        }
    }
    @Preview(showBackground = true)

    @Composable
    fun PreviewFunction()
    {
        DownloadZipFileButton(1)
    }
    @Composable
    fun downloadProgressBar( progress : Float)
    {
            Column (modifier = Modifier
                .padding(all = 10.dp)
               ,
                horizontalAlignment = Alignment.CenterHorizontally){
                LinearProgressIndicator(
                    progress = progress,
                )

        }
    }

    @Composable
    fun StripedProgressIndicator(
        modifier: Modifier = Modifier,
        progress: Float,
        stripeColor: Color = Color(138,205,109),
        stripeColorSecondary: Color = Color(94,189,69),
        backgroundColor: Color = Color(221,224,224),
        clipShape: Shape = RoundedCornerShape(16.dp)
    ) {
        Row (modifier = Modifier
            .padding(all = 10.dp)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            ) {
            Box(
                modifier = modifier
                    .clip(clipShape)
                    .background(backgroundColor)
                    .height(20.dp)
                    .width(300.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(clipShape)
                        .background(createStripeBrush(stripeColor, stripeColorSecondary, 5.dp))
                        .fillMaxHeight()
                        .fillMaxWidth(progress)
                )
            }
            ImageAndText(text = (progress * 100).toString())
        }
    }
    @Composable
    fun ImageAndText(
        modifier: Modifier = Modifier.offset(x = (-10).dp),
        painter: Painter = painterResource(R.drawable.circle),
        text: String
    ) {
        Box(
            modifier = modifier.wrapContentSize(Alignment.CenterEnd),
        ) {
            Image(
                painter = painter,
                contentDescription = "",
            )
            Text(
                text = "$text%",
                color = White,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(alignment = Alignment.Center)
            )

        }
    }
    @Composable
    private fun createStripeBrush(
        stripeColor: Color,
        stripeBg: Color,
        stripeWidth: Dp
    ): Brush {
        val stripeWidthPx = with(LocalDensity.current) { stripeWidth.toPx() }
        val brushSizePx = 2 * stripeWidthPx
        val stripeStart = stripeWidthPx / brushSizePx

        return Brush.linearGradient(
            stripeStart to stripeBg,
            stripeStart to stripeColor,
            start = Offset(0f, 0f),
            end = Offset(brushSizePx, brushSizePx),
            tileMode = TileMode.Repeated
        )
    }
    fun gotoLearnActivity(context: Context, level: Int) {
        val LearnActvity = Intent(
            context,
            LearnActivity::class.java
        )
        LearnActvity.putExtra("LearnMode", 0)
        LearnActvity.putExtra("LevelId", level)
        MyApplication.mNetProc.mLoginUserInf.mnSelectedLevel = level
        context.startActivity(LearnActvity)

    }

    private suspend fun unpackZip(zippath: String, onProgress: (Int) -> Unit): Boolean {
        val zipin: InputStream
        val zis: ZipInputStream
        try {
            val zipfile = zippath?.let { File(it) }
            var totalcount = zipfile?.let { getZipEntryCount(it) }
            val parentFolder = zipfile?.parentFile?.path
            var filename: String

            zipin = FileInputStream(zippath)

            zis = ZipInputStream(BufferedInputStream(zipin))
            var ze: ZipEntry?
            val buffer = ByteArray(4096)
            var count: Int
            var extracted = 0
            withContext(Dispatchers.IO) {
                while (zis.nextEntry.also { ze = it } != null) {
                    filename = ze?.name ?: ""
                    if (ze?.isDirectory == true) {
                        val fmd = File("$parentFolder/$filename")
                        fmd.mkdirs()
                        continue
                    }
                    var destination = "$parentFolder/$filename"
                    val fout = FileOutputStream(destination)
                    while (zis.read(buffer).also { count = it } != -1) {
                        fout.write(buffer, 0, count)
                    }
                    fout.close()
                    zis.closeEntry()
                    extracted++
                    // 진행 상태 업데이트
                    val progress = (extracted * 100 / totalcount!!)
                    onProgress(progress)
                }
                zis.close()

                val f = File(zippath)
                f.deleteRecursively()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
        return true
    }

    fun getZipEntryCount(zipFile: File): Int {
        val zipInputStream = ZipInputStream(BufferedInputStream(FileInputStream(zipFile)))
        var entry: ZipEntry? = zipInputStream.nextEntry
        var count = 0
        while (entry != null) {
            if (!entry.isDirectory) {
                count++
            }
            entry = zipInputStream.nextEntry
        }
        return count
    }

    private suspend fun downloadFile(
        context: Context,
        level: Int,
        onProgress: (Int) -> Unit
    ): File {
        //Creating download url
        val url = MakeUrl(level)
        // 다운로드할 파일 생성
        val downloadedFile = File(context.filesDir, GetFolderName(level, context))
        // URL 주소로부터 파일 다운로드
        withContext(Dispatchers.IO) {
            val urlConnection = URL(url).openConnection() as HttpURLConnection
            urlConnection.connect()
            val totalLength = urlConnection.contentLength
            var downloadedLength = 0
            val input = urlConnection.inputStream
            val output = downloadedFile.outputStream()
            val buffer = ByteArray(4096)
            var count: Int
            while (input.read(buffer).also { count = it } != -1) {
                downloadedLength += count
                output.write(buffer, 0, count)
                // 진행 상태 업데이트
                val progress = (downloadedLength * 100f / totalLength).toInt()
                onProgress(progress)
            }
            output.close()
            input.close()
            urlConnection.disconnect()
        }
        return downloadedFile
    }

    fun MakeDirectories(folderPath: String, context: Context) {
        val folder = File(context.filesDir, folderPath)
        folder.mkdirs()

    }

    fun MakeUrl(nLevel: Int): String {
        var url: String
        if (nLevel == 0)
            url = Constants.CONTENT_URL + "assets.zip"
        else
            url = Constants.CONTENT_URL + "camp" + nLevel.toString() + ".zip"
        return url;
    }

    fun IsControlJsExist(context: Context): Boolean {
        var contrlJS = File(context.filesDir, "content/assets/js/control.js")
        return contrlJS.exists()
    }

    fun GetFolderName(nLevel: Int, context: Context): String {
        var name = "content/"
//    when (nLevel) {
//        in 1..4 ->  "basecamp/"
//        in 5..8 ->  "camp750/"
//        in 9..12 ->  "camp1500/"
//        else ->  "assets/"
//    }
        MakeDirectories(name, context)
        if (nLevel == 0)
            name += "assets.zip"
        else
            name += "camp$nLevel.zip"

        return name
    }

    fun GetUnZipFolderName(nLevel: Int): String {
        var name =
            when (nLevel) {
                in 1..4 -> "basecamp"
                in 5..8 -> "camp750"
                in 9..12 -> "camp1500"
                else -> "assets"
            }
        val folder = File(Constants.LOCAL_URL, name)
        if (!folder.exists())
            folder.mkdirs()
        return name
        ///MakeDirectories(name, context)
        //return name
    }

    private val BackgroundOpacity = 0.25f
    private  val NumberOfSegments = 8
    private val ProgressHeight = 4.dp
    private val SegmentGap = 8.dp

    @Composable
    fun SegmentedProgressIndicator(
        /*@FloatRange(from = 0.0, to = 1.0)*/
        progress: Float,
        modifier: Modifier = Modifier,
        color: Color = MaterialTheme.colors.primary,
        backgroundColor: Color = color.copy(alpha = BackgroundOpacity),
        progressHeight: Dp = ProgressHeight,
        numberOfSegments: Int = NumberOfSegments,
        segmentGap: Dp = SegmentGap
    ) {
        Text(text = (progress * 100).toString() + "%", modifier = Modifier,color = Color(
            76,
            175,
            80,
            255
        ), fontSize = 30.sp)
        check(progress in 0f..1f) { "Invalid progress $progress" }
        check(numberOfSegments > 0) { "Number of segments must be greater than 0" }

        val gap: Float
        val barHeight: Float
        with(LocalDensity.current) {
            gap = segmentGap.toPx()
            barHeight = progressHeight.toPx()
        }
        Canvas(
            modifier
                .progressSemantics(progress)
                .height(progressHeight)

        ) {

            drawOutlineSegments(1f, backgroundColor, barHeight, numberOfSegments, gap)
            drawSegments(progress, color, barHeight, numberOfSegments, gap)
        }
    }
    private fun DrawScope.drawSegments(
        progress: Float,
        color: Color,
        segmentHeight: Float,
        segments: Int,
        segmentGap: Float,
    ) {
        val width = size.width
        val gaps = (segments - 1) * segmentGap
        val segmentWidth = (width - gaps) / segments
        val barsWidth = segmentWidth * segments
        val start: Float
        val end: Float

        val isLtr = layoutDirection == LayoutDirection.Ltr
        if (isLtr) {
            start = 0f
            end = barsWidth * progress + (progress * segments).toInt() * segmentGap
        } else {
            start = width
            end = (width - (barsWidth * progress + (progress * (segments - 1)).toInt() * segmentGap))
        }

        repeat(segments) { index ->
            val offset = index * (segmentWidth + segmentGap)
            val segmentStart: Float
            val segmentEnd: Float
            if (isLtr) {
                segmentStart = start + offset
                segmentEnd = (segmentStart + segmentWidth).coerceAtMost(end)
            } else {
                segmentEnd = width - offset
                segmentStart = (segmentEnd - segmentWidth).coerceAtLeast(end)
            }
            if (isLtr && offset <= end || !isLtr && segmentEnd > end) {
                drawRect(
                    color,
                    Offset(segmentStart, 0f),
                    Size(segmentEnd - segmentStart, segmentHeight),
                    1.0f,
                    Fill
                    )
            }
        }
    }
    private fun DrawScope.drawOutlineSegments(
        progress: Float,
        color: Color,
        segmentHeight: Float,
        segments: Int,
        segmentGap: Float,
    ) {
        val width = size.width
        val gaps = (segments - 1) * segmentGap
        val segmentWidth = (width - gaps) / segments
        val barsWidth = segmentWidth * segments
        val start: Float
        val end: Float

        val isLtr = layoutDirection == LayoutDirection.Ltr
        if (isLtr) {
            start = 0f
            end = barsWidth * progress + (progress * segments).toInt() * segmentGap
        } else {
            start = width
            end = (width - (barsWidth * progress + (progress * (segments - 1)).toInt() * segmentGap))
        }

        repeat(segments) { index ->
            val offset = index * (segmentWidth + segmentGap)
            val segmentStart: Float
            val segmentEnd: Float
            if (isLtr) {
                segmentStart = start + offset
                segmentEnd = (segmentStart + segmentWidth).coerceAtMost(end)
            } else {
                segmentEnd = width - offset
                segmentStart = (segmentEnd - segmentWidth).coerceAtLeast(end)
            }
            if (isLtr && offset <= end || !isLtr && segmentEnd > end) {
                drawRect(
                    color,
                    Offset(segmentStart, 0f),
                    Size(segmentEnd - segmentStart, segmentHeight),
                    1.0f,
                    Stroke(5f)
                )
            }
        }
    }

}

