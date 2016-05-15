package net.terminal_end.drawrxandroid

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window

class MainActivity : AppCompatActivity() {

    private lateinit var drawingView: DrawingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)
        drawingView = findViewById(R.id.drawing_view) as DrawingView
        findViewById(R.id.delete_button)!!.setOnClickListener {
            drawingView.delete()
        }
    }

}
